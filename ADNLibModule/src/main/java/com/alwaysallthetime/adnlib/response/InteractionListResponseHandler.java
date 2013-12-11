package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.InteractionList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class InteractionListResponseHandler extends AppDotNetResponseEnvelopeHandler<InteractionList> {
    protected InteractionListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<InteractionList>>(){});
    }
}
