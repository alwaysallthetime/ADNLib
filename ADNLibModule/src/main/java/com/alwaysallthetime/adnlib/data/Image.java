package com.alwaysallthetime.adnlib.data;

import android.net.Uri;

import com.google.gson.annotations.Expose;

public class Image {
    private int height;
    private int width;
    private String url;
    @Expose(serialize = false)
    private boolean isDefault;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlForSize(int width, int height) {
        Uri uri = Uri.parse(getUrl());
        Uri.Builder builder = uri.buildUpon();

        if (width > 0) {
            builder.appendQueryParameter("w", String.valueOf(width));
        }

        if (height > 0) {
            builder.appendQueryParameter("h", String.valueOf(height));
        }

        return builder.build().toString();
    }

    public boolean isDefault() {
        return isDefault;
    }
}