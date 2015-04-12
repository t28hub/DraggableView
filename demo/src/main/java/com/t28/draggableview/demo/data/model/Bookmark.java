package com.t28.draggableview.demo.data.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Bookmark {
    private final String mTitle;
    private final Uri mUrl;
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

    public Uri getUrl() {
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
        private Uri mUrl;
        private Drawable mFavicon;
        private Drawable mThumbnail;

        public Builder() {
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setUrl(Uri url) {
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
