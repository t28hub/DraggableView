package com.t28.draggablelistview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DraggableListView extends RecyclerView {
    private static final int NO_DEF_STYLE = 0;
    private static final int INITIAL_POINTER_INDEX = 0;

    private final PointF mTouchDownPoint;
    private final PointF mTouchMovePoint;

    private int mDragPointerId = MotionEvent.INVALID_POINTER_ID;

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
}
