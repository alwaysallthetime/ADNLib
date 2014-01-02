package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObjectList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.alwaysallthetime.adnlib.response.AppDotNetPageableResponseEnvelopeHandler;
import com.google.gson.reflect.TypeToken;

public abstract class ResourceStreamResponseHandler<T extends IPageableAppDotNetObjectList> extends AppDotNetPageableResponseEnvelopeHandler<T> {
    protected ResourceStreamResponseHandler(TypeToken<ResponseEnvelope<? extends T>> typeToken) {
        super(typeToken);
    }
}