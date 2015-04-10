package com.t28.draggablelistview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DraggableListView extends RecyclerView {
    private static final int NO_DEF_STYLE = 0;

    private final PointF mTouchDownPoint;
    private final PointF mTouchMovePoint;

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
        return false;
    }

    private boolean onTouchDown(MotionEvent event) {
        mTouchDownPoint.x = event.getX();
        mTouchDownPoint.y = event.getY();
        return false;
    }

    private boolean onTouchUp(MotionEvent event) {
        return false;
    }

    private boolean onTouchMove(MotionEvent event) {
        mTouchMovePoint.x = event.getX();
        mTouchMovePoint.y = event.getY();
        return false;
    }

    private boolean onTouchCancel(MotionEvent event) {
        return false;
    }
}
