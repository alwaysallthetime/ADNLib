package com.alwaysallthetime.adnlib.request;

import android.net.Uri;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AppDotNetApiRequest extends AppDotNetRequest {
    private static final String ADN_API_BASE_URL = "https://alpha-api.app.net/stream/0/";

    public static final Uri ADN_API_BASE = Uri.parse(ADN_API_BASE_URL);

    public AppDotNetApiRequest(AppDotNetResponseHandler handler, boolean authenticated, String requestMethod,
                               QueryParameters queryParameters, String... pathComponents) {
        super(handler, authenticated, requestMethod, queryParameters, ADN_API_BASE, pathComponents);
    }

    public AppDotNetApiRequest(AppDotNetResponseHandler handler, String requestMethod, QueryParameters queryParameters,
                               String... pathComponents) {
        this(handler, true, requestMethod, queryParameters, pathComponents);
    }

    public AppDotNetApiRequest(AppDotNetResponseHandler handler, QueryParameters queryParameters, String... pathComponents) {
        this(handler, AppDotNetClient.METHOD_GET, queryParameters, pathComponents);
    }

    @Override
    public void writeBody(HttpURLConnection connection) throws IOException {
        throw new UnsupportedOperationException("request has no body");
    }
}