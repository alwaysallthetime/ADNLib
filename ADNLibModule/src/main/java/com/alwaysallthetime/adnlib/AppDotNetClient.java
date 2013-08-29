package com.alwaysallthetime.adnlib;

public class AppDotNetClient {
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";

    protected String authHeader;

    public AppDotNetClient() {}

    public AppDotNetClient(String token) {
        setToken(token);
    }

    public void setToken(String token) {
        authHeader = "Bearer " + token;
    }

    public boolean hasToken() {
        return authHeader != null;
    }
}