package com.t28.draggableview;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

public class HorizontalShadowBuilder extends DraggableView.ShadowBuilder {
    public HorizontalShadowBuilder(View view) {
        super(view);
    }

    @Override
    protected boolean onMove(@NonNull Point newPoint) {
        final Drawable shadow = getShadow();
        final Rect oldBounds = shadow.getBounds();
        final int halfWidth = oldBounds.width() / 2;

        final Rect newBounds = new Rect();
        newBounds.left = newPoint.x - halfWidth;
        newBounds.top = oldBounds.top;
        newBounds.right = newPoint.x + halfWidth;
        newBounds.bottom = oldBounds.bottom;
        shadow.setBounds(newBounds);

        return oldBounds.equals(newBounds);
    }
}
