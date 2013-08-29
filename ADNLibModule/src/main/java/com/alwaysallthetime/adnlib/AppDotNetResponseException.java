package com.alwaysallthetime.adnlib;

import com.alwaysallthetime.adnlib.data.ResponseMeta;

public class AppDotNetResponseException extends AppDotNetClientException {
    private int status;
    private String errorMessage;
    private String errorSlug;
    private String errorId;

    public AppDotNetResponseException(String detailMessage, int status) {
        super(String.format("%s (%s)", detailMessage, status));
        this.errorMessage = detailMessage;
        this.status = status;
    }

    public AppDotNetResponseException(ResponseMeta responseMeta) {
        this(responseMeta.getErrorMessage(), responseMeta.getCode());
        this.errorSlug = responseMeta.getErrorSlug();
        this.errorId = responseMeta.getErrorId();
    }

    public AppDotNetResponseException(String errorMessage, String errorSlug) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorSlug = errorSlug;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorSlug() {
        return errorSlug;
    }

    public String getErrorId() {
        return errorId;
    }
}