package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.alwaysallthetime.adnlib.data.StreamMarker;
import com.google.gson.reflect.TypeToken;

/**
 * Created by brambley on 10/8/13.
 */
public abstract class StreamMarkerResponseHandler extends AppDotNetResponseEnvelopeHandler<StreamMarker> {
    protected StreamMarkerResponseHandler() {
        super(new TypeToken<ResponseEnvelope<StreamMarker>>(){});
    }
}