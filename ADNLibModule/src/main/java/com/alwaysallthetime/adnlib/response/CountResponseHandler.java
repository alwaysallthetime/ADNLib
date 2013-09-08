package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.Count;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class CountResponseHandler extends AppDotNetResponseEnvelopeHandler<Count> {
    protected CountResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Count>>(){});
    }
}