package com.t28.draggableview;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

public class VerticalShadowBuilder extends DraggableView.ShadowBuilder {
    public VerticalShadowBuilder(View view) {
        super(view);
    }

    @Override
    protected boolean onMove(@NonNull Point newPoint) {
        final Drawable shadow = getShadow();
        final Rect oldBounds = shadow.getBounds();
        final int halfHeight = oldBounds.height() / 2;

        final Rect newBounds = new Rect();
        newBounds.left = oldBounds.left;
        newBounds.top = newPoint.y - halfHeight;
        newBounds.right = oldBounds.right;
        newBounds.bottom = newPoint.y + halfHeight;
        shadow.setBounds(newBounds);

        return oldBounds.equals(newBounds);
    }
}
