package com.alwaysallthetime.adnlib.request;

import android.net.Uri;

import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import org.apache.http.NameValuePair;

import java.util.List;

public class AppDotNetOAuthRequest extends AppDotNetFormRequest {
    private static final String ADN_OAUTH_BASE_URL = "https://account.app.net/oauth/";
    private static final Uri ADN_OAUTH_BASE = Uri.parse(ADN_OAUTH_BASE_URL);

    public AppDotNetOAuthRequest(AppDotNetResponseHandler handler, List<? extends NameValuePair> body, String... pathComponents) {
        super(handler, ADN_OAUTH_BASE, body, pathComponents);
    }
}