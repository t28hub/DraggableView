package com.t28.draggableview.demo.data.model;

import android.graphics.drawable.Drawable;

public class Bookmark {
    private final String mTitle;
    private final String mUrl;
    private final Drawable mFavicon;
    private final Drawable mThumbnail;

    private Bookmark(Builder builder) {
        mTitle = builder.mTitle;
        mUrl = builder.mUrl;
        mFavicon = builder.mFavicon;
        mThumbnail = builder.mThumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public Drawable getFavicon() {
        return mFavicon;
    }

    public Drawable getThumbnail() {
        return mThumbnail;
    }

    public static final class Builder {
        private String mTitle;
        private String mUrl;
        private Drawable mFavicon;
        private Drawable mThumbnail;

        public Builder() {
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setFavicon(Drawable favicon) {
            mFavicon = favicon;
            return this;
        }

        public Builder setThumbnail(Drawable thumbnail) {
            mThumbnail = thumbnail;
            return this;
        }

        public Bookmark build() {
            return new Bookmark(this);
        }
    }
}
