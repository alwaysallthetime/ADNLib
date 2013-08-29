package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.AccessToken;
import com.alwaysallthetime.adnlib.data.Token;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;

public abstract class LoginResponseHandler extends AppDotNetResponseHandler<AccessToken> {
    protected LoginResponseHandler() {
        super(new TypeToken<AccessToken>(){});
    }

    @Override
    protected <T> T parseResponse(Reader reader) {
        return null;
    }

    @Override
    public void handleResponse(Reader reader) {}

    @Override
    public void onSuccess(AccessToken accessToken) {
        onSuccess(accessToken.getAccessToken(), accessToken.getToken());
    }

    public abstract void onSuccess(String accessToken, Token token);
}