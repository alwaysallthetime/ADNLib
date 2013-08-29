package com.alwaysallthetime.adnlib;

import android.os.AsyncTask;
import android.os.Build;

import com.alwaysallthetime.adnlib.request.AppDotNetOAuthRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetRequest;
import com.alwaysallthetime.adnlib.response.AccessTokenResponseHandler;
import com.alwaysallthetime.adnlib.response.LoginResponseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class AppDotNetClient {
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";

    protected String authHeader;
    protected List<NameValuePair> authParams;

    public AppDotNetClient() {}

    public AppDotNetClient(String token) {
        setToken(token);
    }

    public AppDotNetClient(String clientId, String passwordGrantSecret) {
        authParams = new ArrayList<NameValuePair>(3);
        authParams.add(new BasicNameValuePair("client_id", clientId));
        authParams.add(new BasicNameValuePair("password_grant_secret", passwordGrantSecret));
        authParams.add(new BasicNameValuePair("grant_type", "password"));
    }

    public void setToken(String token) {
        authHeader = "Bearer " + token;
    }

    public boolean hasToken() {
        return authHeader != null;
    }

    /*
     * OAUTH
     */

    public void authenticateWithPassword(String username, String password, String scope, LoginResponseHandler responseHandler) {
        if (authParams == null)
            throw new IllegalStateException("client must be constructed with client ID and password grant secret");

        final List<NameValuePair> params = getAuthenticationParams(username, password, scope);
        final AccessTokenResponseHandler tokenResponseHandler = new AccessTokenResponseHandler(this, responseHandler);
        execute(new AppDotNetOAuthRequest(tokenResponseHandler, params, "access_token"));
    }

    protected List<NameValuePair> getAuthenticationParams(String username, String password, String scope) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>(authParams.size() + 3);
        params.addAll(authParams);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        if (scope != null)
            params.add(new BasicNameValuePair("scope", scope));

        return params;
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