package com.alwaysallthetime.adnlib.data;

public class AccessToken implements IAppDotNetObject {
    private String accessToken;
    private Token token;
    private String error;
    private String errorSlug;

    public String getAccessToken() {
        return accessToken;
    }

    public Token getToken() {
        return token;
    }

    public String getError() {
        return error;
    }

    public String getErrorSlug() {
        return errorSlug;
    }

    public boolean isError() {
        return error != null;
    }
}