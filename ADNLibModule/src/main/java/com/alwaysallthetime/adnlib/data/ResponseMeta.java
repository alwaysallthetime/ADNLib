package com.alwaysallthetime.adnlib.data;

public class ResponseMeta {
    private int code;
    private String errorMessage;
    private String errorSlug;
    private String errorId;
    private String maxId;
    private String minId;
    private boolean more;

    public int getCode() {
        return code;
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

    public String getMaxId() {
        return maxId;
    }

    public String getMinId() {
        return minId;
    }

    public boolean isMore() {
        return more;
    }
}
