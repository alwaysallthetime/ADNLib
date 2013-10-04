package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.Place;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class PlaceResponseHandler extends AppDotNetResponseEnvelopeHandler<Place> {
    protected PlaceResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Place>>(){});
    }
}