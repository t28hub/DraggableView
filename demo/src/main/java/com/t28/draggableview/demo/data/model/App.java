package com.t28.draggableview.demo.data.model;

import android.graphics.drawable.Drawable;

public class App {
    private final String mPackageName;
    private final String mName;
    private final Drawable mIcon;

    private App(Builder builder) {
        mPackageName = builder.mPackageName;
        mName = builder.mName;
        mIcon = builder.mIcon;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getName() {
        return mName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public static final class Builder {
        private String mPackageName;
        private String mName;
        private Drawable mIcon;

        public Builder() {
        }

        public Builder setPackageName(String name) {
            mPackageName = name;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            mIcon = icon;
            return this;
        }

        public App build() {
            return new App(this);
        }
    }
}
