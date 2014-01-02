package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.IAppDotNetObject;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class AppDotNetResponseEnvelopeHandler<T extends IAppDotNetObject> extends AppDotNetBaseResponseEnvelopeHandler<T> {
    protected AppDotNetResponseEnvelopeHandler(TypeToken<ResponseEnvelope<T>> typeToken) {
        super(typeToken);
    }

}