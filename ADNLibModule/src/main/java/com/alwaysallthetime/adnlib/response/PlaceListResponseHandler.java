package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.PlaceList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class PlaceListResponseHandler extends AppDotNetResponseEnvelopeHandler<PlaceList> {
    protected PlaceListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<PlaceList>>(){});
    }
}