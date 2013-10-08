package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.alwaysallthetime.adnlib.data.StreamMarkerList;
import com.google.gson.reflect.TypeToken;

public abstract class StreamMarkerListResponseHandler extends AppDotNetResponseEnvelopeHandler<StreamMarkerList> {
    protected StreamMarkerListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<StreamMarkerList>>(){});
    }
}