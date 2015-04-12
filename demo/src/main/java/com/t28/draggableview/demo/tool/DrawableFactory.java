package com.t28.draggableview.demo.tool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableFactory {
    private static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;

    private DrawableFactory() {
    }

    public static Drawable decodeByteArray(Resources resources, byte[] data) {
        if (resources == null) {
            throw new NullPointerException("resources == null");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("'data' must not be empty");
        }

        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return new BitmapDrawable(resources, bitmap.copy(DEFAULT_CONFIG, false));
    }
}
