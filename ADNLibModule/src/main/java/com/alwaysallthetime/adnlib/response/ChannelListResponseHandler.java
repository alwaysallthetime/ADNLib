package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.ChannelList;
import com.alwaysallthetime.adnlib.data.PostList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class ChannelListResponseHandler extends AppDotNetResponseEnvelopeHandler<ChannelList> {
    protected ChannelListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<ChannelList>>(){});
    }
}