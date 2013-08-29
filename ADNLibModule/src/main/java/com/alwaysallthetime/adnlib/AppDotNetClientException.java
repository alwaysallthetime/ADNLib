package com.alwaysallthetime.adnlib;

public class AppDotNetClientException extends Exception {
    public AppDotNetClientException(String detailMessage) {
        super(detailMessage);
    }

    public AppDotNetClientException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AppDotNetClientException(Throwable throwable) {
        super(throwable);
    }
}