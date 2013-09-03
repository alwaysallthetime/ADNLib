package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.MessageList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class MessageListResponseHandler extends AppDotNetResponseEnvelopeHandler<MessageList> {
    protected MessageListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<MessageList>>(){});
    }
}