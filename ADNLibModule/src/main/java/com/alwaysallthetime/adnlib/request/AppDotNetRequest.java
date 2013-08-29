package com.alwaysallthetime.adnlib.request;

import android.net.Uri;
import android.util.Log;

import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.gson.AppDotNetGson;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public abstract class AppDotNetRequest {
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";

    protected static final String HEADER_CONTENT_TYPE = "Content-Type";
    protected static final Gson gson = AppDotNetGson.getInstance();

    private static final String TAG = "AppDotNetRequest";

    protected URL url;
    protected AppDotNetResponseHandler handler;
    protected boolean authenticated;
    protected String method;
    protected boolean hasBody = false; // if true, writeBody will be called

    private String fixedLengthBody; // may be unused even if hasBody is true, e.g. image upload

    protected AppDotNetRequest(AppDotNetResponseHandler handler, boolean authenticated, String requestMethod, QueryParameters queryParameters,
                               Uri baseUri, String... pathComponents) {
        this.handler = handler;
        this.authenticated = authenticated;
        method = requestMethod;
        url = buildUrl(baseUri, queryParameters, pathComponents);
    }

    public URL getUrl() {
        return url;
    }

    public AppDotNetResponseHandler getHandler() {
        return handler;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getMethod() {
        return method;
    }

    public boolean hasBody() {
        return hasBody;
    }

    protected URL buildUrl(Uri baseUri, QueryParameters queryParameters, String... pathComponents) {
        final Uri.Builder builder = baseUri.buildUpon();

        // concatenate path components together
        for (String pathComponent : pathComponents) {
            builder.appendEncodedPath(pathComponent);
        }

        if (queryParameters != null) {
            // add all specified parameters to query string
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        try {
            return new URL(builder.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to construct URL", e);
            handler.onError(e);
            return null;
        }
    }

    protected void setBody(String body) {
        fixedLengthBody = body;
        hasBody = true;
    }

    public abstract void writeBody(HttpURLConnection connection) throws IOException;

    protected void writeFixedLengthBody(HttpURLConnection connection) throws IOException {
        final byte[] bodyBytes = fixedLengthBody.getBytes("UTF-8");
        connection.setFixedLengthStreamingMode(bodyBytes.length);

        final OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bodyBytes);
        outputStream.close();
    }
}