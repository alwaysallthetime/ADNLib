package com.alwaysallthetime.adnlib;

import android.os.AsyncTask;
import android.os.Build;

import com.alwaysallthetime.adnlib.request.AppDotNetRequest;

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

    protected void execute(AppDotNetRequest request) {
        if (request.isAuthenticated() && !hasToken()) {
            throw new IllegalStateException("authentication token not set");
        }

        final AppDotNetClientTask task = new AppDotNetClientTask(authHeader);

        // AsyncTask was changed in Honeycomb to execute in serial by default, at which time
        // executeOnExecutor was added to specify parallel execution.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
        } else {
            task.execute(request);
        }
    }
}