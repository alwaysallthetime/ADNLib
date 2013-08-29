package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.AppDotNetResponseException;
import com.alwaysallthetime.adnlib.data.IAppDotNetObject;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.net.HttpURLConnection;

public abstract class AppDotNetResponseEnvelopeHandler<T extends IAppDotNetObject> extends AppDotNetResponseHandler<T> {
    protected AppDotNetResponseEnvelopeHandler(TypeToken<ResponseEnvelope<T>> typeToken) {
        super(typeToken);
    }

    @Override
    public void handleResponse(Reader reader) {
        final ResponseEnvelope<T> response = parseResponse(reader);
        final int status = response.getMeta().getCode();
        if (status != HttpURLConnection.HTTP_OK) {
            onError(new AppDotNetResponseException(response.getMeta()));
        } else {
            onSuccess(response.getData());
        }
    }
}