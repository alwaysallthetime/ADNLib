package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.Message;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class MessageResponseHandler extends AppDotNetResponseEnvelopeHandler<Message> {
    protected MessageResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Message>>(){});
    }
}