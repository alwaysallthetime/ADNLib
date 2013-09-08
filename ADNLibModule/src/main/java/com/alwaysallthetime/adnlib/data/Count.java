package com.alwaysallthetime.adnlib.data;

public class Count implements IAppDotNetObject {
    private int value;

    public Count(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}