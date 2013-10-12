package com.alwaysallthetime.adnlib.data;

public class ResourceConfiguration implements IAppDotNetObject {
    private int annotationMaxBytes;
    private int textMaxLength;

    public int getAnnotationMaxBytes() {
        return annotationMaxBytes;
    }

    public int getTextMaxLength() {
        return textMaxLength;
    }
}
