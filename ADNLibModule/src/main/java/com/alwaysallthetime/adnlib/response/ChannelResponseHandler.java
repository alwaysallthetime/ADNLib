package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.Channel;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class ChannelResponseHandler extends AppDotNetResponseEnvelopeHandler<Channel> {
    protected ChannelResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Channel>>(){});
    }
}