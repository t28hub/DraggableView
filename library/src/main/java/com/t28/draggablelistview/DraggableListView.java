package com.t28.draggablelistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DraggableListView extends RecyclerView {
    private static final int NO_DEF_STYLE = 0;
    private static final int INITIAL_POINTER_INDEX = 0;

    private final PointF mTouchDownPoint;
    private final PointF mTouchMovePoint;

    private int mDragPointerId = MotionEvent.INVALID_POINTER_ID;
    private long mDraggingItemId = NO_ID;
    private View mDraggingView;

    public DraggableListView(Context context) {
        this(context, null, NO_DEF_STYLE);
    }

    public DraggableListView(Context context, AttributeSet attrs) {
        this(context, attrs, NO_DEF_STYLE);
    }

    public DraggableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchDownPoint = new PointF();
        mTouchMovePoint = new PointF();
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

    public void startDrag(View view) {
        final ViewHolder viewHolder = getChildViewHolder(view);
        if (viewHolder == null) {
            final String message = String.format("View(%s) is not found in %s", view, this);
            throw new IllegalArgumentException(message);
        }

        if (isDragging()) {
            final String message = String.format("Another view(%s) is dragging", mDraggingView);
            throw new IllegalStateException(message);
        }

        mDraggingItemId = viewHolder.getItemId();
        mDraggingView = view;
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
        mTouchDownPoint.x = event.getX();
        mTouchDownPoint.y = event.getY();
        return false;
    }

    private boolean onTouchUp(MotionEvent event) {
        return false;
    }

    private boolean onTouchMove(MotionEvent event) {
        mTouchMovePoint.x = event.getX(mDragPointerId);
        mTouchMovePoint.y = event.getY(mDragPointerId);
        return false;
    }

    private boolean onTouchCancel(MotionEvent event) {
        return false;
    }

    private boolean onTouchPointerUp(MotionEvent event) {
        return onTouchUp(event);
    }

    public static abstract class Adapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {
        public abstract long getItemId(int position);
    }

    public static abstract class ShadowBuilder {
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
    }
}
