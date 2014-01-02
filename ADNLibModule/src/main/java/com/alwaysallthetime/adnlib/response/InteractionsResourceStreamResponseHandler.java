package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.InteractionList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class InteractionsResourceStreamResponseHandler extends ResourceStreamResponseHandler<InteractionList> {
    protected InteractionsResourceStreamResponseHandler() {
        super(new TypeToken<ResponseEnvelope<? extends InteractionList>>(){});
    }
}