package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.AppDotNetResponseException;
import com.alwaysallthetime.adnlib.data.AccessToken;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;

public class AccessTokenResponseHandler extends AppDotNetResponseHandler<AccessToken> {
    private LoginResponseHandler loginResponseHandler;
    private AppDotNetClient client;

    public AccessTokenResponseHandler(AppDotNetClient client, LoginResponseHandler loginResponseHandler) {
        super(new TypeToken<AccessToken>(){});
        this.client = client;
        this.loginResponseHandler = loginResponseHandler;
    }

    @Override
    public void handleResponse(Reader reader) {
        final AccessToken response = parseResponse(reader);
        if (response.isError()) {
            onError(new AppDotNetResponseException(response.getError(), response.getErrorSlug()));
        } else {
            onSuccess(response);
        }
    }

    @Override
    public void onSuccess(AccessToken accessToken) {
        client.setToken(accessToken.getAccessToken());
        loginResponseHandler.onSuccess(accessToken);
    }

    @Override
    public void onError(Exception error) {
        loginResponseHandler.onError(error);
    }
}