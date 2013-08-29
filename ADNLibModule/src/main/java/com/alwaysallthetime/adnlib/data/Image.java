package com.alwaysallthetime.adnlib.data;

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

    public boolean isDefault() {
        return isDefault;
    }
}