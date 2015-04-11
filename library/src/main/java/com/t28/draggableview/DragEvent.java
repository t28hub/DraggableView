package com.t28.draggableview;

import android.graphics.Point;

public class DragEvent {
    private final Action mAction;
    private final Point mPoint;

    DragEvent(Action action, Point point) {
        mAction = action;
        mPoint = new Point(point);
    }

    public Action getAction() {
        return mAction;
    }

    public Point getPoint() {
        return new Point(mPoint);
    }

    public enum Action {
        DRAG_START,
        DRAG_END,
        DRAG,
        DROP
    }
}
