package com.t28.draggableview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DraggableView extends RecyclerView {
    private static final int NO_DEF_STYLE = 0;
    private static final int INITIAL_POINTER_INDEX = 0;
    private static final int SCROLL_DETECTION_INTERVAL = 50;
    private static final int NO_DISTANCE = 0;
    private static final int LEFTWARD_DISTANCE = -1;
    private static final int RIGHTWARD_DISTANCE = 1;
    private static final int UPPER_DISTANCE = -1;
    private static final int LOWER_DISTANCE = 1;

    private final Point mTouchDownPoint;
    private final Point mTouchMovePoint;

    private int mDragPointerId = MotionEvent.INVALID_POINTER_ID;
    private long mDraggingItemId = NO_ID;
    private View mDraggingView;
    private ShadowBuilder mShadowBuilder;
    private OnDragListener mDragListener;

    public DraggableView(Context context) {
        this(context, null, NO_DEF_STYLE);
    }

    public DraggableView(Context context, AttributeSet attrs) {
        this(context, attrs, NO_DEF_STYLE);
    }

    public DraggableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchDownPoint = new Point();
        mTouchMovePoint = new Point();
        mDragPointerId = MotionEvent.INVALID_POINTER_ID;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final boolean isHandled = handleTouchEvent(event);
        if (isHandled) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean isHandled = handleTouchEvent(event);
        if (isHandled) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowBuilder != null) {
            mShadowBuilder.onDraw(canvas);
        }
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (!(adapter instanceof Adapter)) {
            final String message = String.format("'adapter' must be an instance of %s", Adapter.class.getCanonicalName());
            throw new IllegalArgumentException(message);
        }
        super.setAdapter(adapter);
    }

    @Override
    public Adapter getAdapter() {
        return (Adapter) super.getAdapter();
    }

    public boolean isDragging() {
        return mDraggingView != null;
    }

    public void startDrag(View view, ShadowBuilder shadowBuilder) {
        if (view == null) {
            throw new NullPointerException("'view == null'");
        }
        if (shadowBuilder == null) {
            throw new NullPointerException("'shadowBuilder == null'");
        }

        final ViewHolder viewHolder = getChildViewHolder(view);
        if (viewHolder == null) {
            final String message = String.format("View(%s) is not found in %s", view, this);
            throw new IllegalArgumentException(message);
        }

        if (isDragging()) {
            final String message = String.format("Another view(%s) is dragging", mDraggingView);
            throw new IllegalStateException(message);
        }

        final int adapterPosition = viewHolder.getAdapterPosition();
        mDraggingItemId = getAdapter().getItemId(adapterPosition);
        mDraggingView = view;
        mShadowBuilder = shadowBuilder;
        invalidate();
    }

    public void setOnDragListener(OnDragListener listener) {
        mDragListener = listener;
    }

    private boolean handleTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            return onTouchDown(event);
        }
        if (action == MotionEvent.ACTION_UP) {
            return onTouchUp(event);
        }
        if (action == MotionEvent.ACTION_MOVE) {
            return onTouchMove(event);
        }
        if (action == MotionEvent.ACTION_CANCEL) {
            return onTouchCancel(event);
        }
        if (action == MotionEvent.ACTION_POINTER_UP) {
            return onTouchPointerUp(event);
        }
        return false;
    }

    private boolean onTouchDown(MotionEvent event) {
        mDragPointerId = event.getPointerId(INITIAL_POINTER_INDEX);
        mTouchDownPoint.x = (int) event.getX(mDragPointerId);
        mTouchDownPoint.y = (int) event.getY(mDragPointerId);
        return false;
    }

    private boolean onTouchUp(MotionEvent event) {
        if (!isDragging()) {
            return false;
        }

        mTouchMovePoint.x = (int) event.getX(mDragPointerId);
        mTouchMovePoint.y = (int) event.getY(mDragPointerId);

        reset();
        return false;
    }

    private boolean onTouchMove(MotionEvent event) {
        if (!isDragging()) {
            return false;
        }

        mTouchMovePoint.x = (int) event.getX(mDragPointerId);
        mTouchMovePoint.y = (int) event.getY(mDragPointerId);

        if (mShadowBuilder.onMove(new Point(mTouchMovePoint))) {
            invalidate();
        }

        final View underView = findChildViewUnder(mTouchMovePoint.x, mTouchMovePoint.y);
        if (moveTo(underView)) {
            mDraggingView = underView;
        }

        handleScroll();
        return true;
    }

    private boolean onTouchCancel(MotionEvent event) {
        if (!isDragging()) {
            return false;
        }

        mTouchMovePoint.x = (int) event.getX(mDragPointerId);
        mTouchMovePoint.y = (int) event.getY(mDragPointerId);

        reset();
        return false;
    }

    private boolean onTouchPointerUp(MotionEvent event) {
        return onTouchUp(event);
    }

    private void reset() {
        mDragPointerId = MotionEvent.INVALID_POINTER_ID;
        mDraggingItemId = NO_ID;
        mDraggingView = null;
        mShadowBuilder = null;
        invalidate();
    }

    private boolean moveTo(View underView) {
        if (underView == null) {
            return false;
        }

        final int fromPosition = findPositionForItemId(mDraggingItemId);
        final int toPosition = getChildAdapterPosition(underView);
        if (fromPosition < 0 || toPosition < 0) {
            return false;
        }

        final boolean isMoved = getAdapter().move(fromPosition, toPosition);
        if (!isMoved) {
            return false;
        }

        if (fromPosition == 0 || toPosition == 0) {
            scrollToPosition(0);
        }
        return true;
    }

    private int findPositionForItemId(long itemId) {
        final Adapter adapter = getAdapter();
        final int itemCount = adapter.getItemCount();
        for (int position = 0; position < itemCount; position++) {
            if (adapter.getItemId(position) == itemId) {
                return position;
            }
        }
        return NO_POSITION;
    }

    private void handleScroll() {
        // postDelayedで呼び出された時に既にドラッグが完了している可能性がある。
        if (!isDragging()) {
            return;
        }

        final boolean isScrolled = scrollIfNeeded();
        if (!isScrolled) {
            return;
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isDragging()) {
                    return;
                }
                handleScroll();
            }
        }, SCROLL_DETECTION_INTERVAL);
    }

    private boolean scrollIfNeeded() {
        final Rect shadowBounds = mShadowBuilder.getBounds();
        final int scrollY = computeScrollY(shadowBounds);
        final int scrollX = computeScrollX(shadowBounds);
        if (scrollX == NO_DISTANCE && scrollY == NO_DISTANCE) {
            return false;
        }

        scrollBy(scrollX, scrollY);
        return true;
    }

    private int computeScrollX(Rect shadowBounds) {
        final int shadowWidth = shadowBounds.width();
        final int thresholdLeft = getLeft() + shadowWidth;
        if (canScrollHorizontally(LEFTWARD_DISTANCE) && mTouchMovePoint.x < thresholdLeft) {
            return LEFTWARD_DISTANCE;
        }

        final int thresholdRight = getRight() - shadowWidth;
        if (canScrollHorizontally(RIGHTWARD_DISTANCE) && thresholdRight < mTouchMovePoint.x) {
            return RIGHTWARD_DISTANCE;
        }
        return NO_DISTANCE;
    }

    private int computeScrollY(Rect shadowBounds) {
        final int shadowHeight = shadowBounds.height();
        final int thresholdTop = getTop() - shadowHeight;
        if (canScrollVertically(UPPER_DISTANCE) && mTouchMovePoint.y < thresholdTop) {
            return UPPER_DISTANCE;
        }

        final int thresholdBottom = getBottom() - shadowHeight;
        if (canScrollVertically(LOWER_DISTANCE) && thresholdBottom < mTouchMovePoint.y) {
            return LOWER_DISTANCE;
        }
        return NO_DISTANCE;
    }

    public interface OnDragListener {
        void onDrag();
    }

    public static abstract class Adapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {
        public abstract long getItemId(int position);

        public abstract boolean move(int position1, int position2);
    }

    public static class ShadowBuilder {
        private final Drawable mShadow;

        public ShadowBuilder(View view) {
            if (view == null) {
                throw new NullPointerException("view == null");
            }
            mShadow = createShadow(view);
            mShadow.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }

        protected Drawable createShadow(@NonNull View view) {
            final Bitmap.Config config = Bitmap.Config.ARGB_8888;
            final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
            final Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return new BitmapDrawable(view.getResources(), bitmap.copy(config, false));
        }

        @NonNull
        protected Drawable getShadow() {
            return mShadow;
        }

        protected void onDraw(@NonNull Canvas canvas) {
            mShadow.draw(canvas);
        }

        protected boolean onMove(@NonNull Point newPoint) {
            final Rect oldBounds = mShadow.getBounds();
            final int halfWidth = oldBounds.width() / 2;
            final int halfHeight = oldBounds.height() / 2;

            final Rect newBounds = new Rect();
            newBounds.left = newPoint.x - halfWidth;
            newBounds.top = newPoint.y - halfHeight;
            newBounds.right = newPoint.x + halfWidth;
            newBounds.bottom = newPoint.y + halfHeight;
            mShadow.setBounds(newBounds);

            return oldBounds.equals(newBounds);
        }

        Rect getBounds() {
            return mShadow.getBounds();
        }
    }
}
