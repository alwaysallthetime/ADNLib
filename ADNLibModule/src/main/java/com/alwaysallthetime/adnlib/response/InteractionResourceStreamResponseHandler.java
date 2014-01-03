package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.InteractionList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class InteractionResourceStreamResponseHandler<T> extends ResourceStreamResponseHandler<InteractionList> {
    protected InteractionResourceStreamResponseHandler() {
        super(new TypeToken<ResponseEnvelope<? extends InteractionList>>(){});
    }
}