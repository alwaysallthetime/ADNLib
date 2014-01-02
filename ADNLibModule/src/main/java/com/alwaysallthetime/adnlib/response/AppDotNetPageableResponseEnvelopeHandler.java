package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObjectList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class AppDotNetPageableResponseEnvelopeHandler<T extends IPageableAppDotNetObjectList> extends AppDotNetBaseResponseEnvelopeHandler<T> {
    protected AppDotNetPageableResponseEnvelopeHandler(TypeToken<ResponseEnvelope<? extends T>> typeToken) {
        super(typeToken);
    }
}
