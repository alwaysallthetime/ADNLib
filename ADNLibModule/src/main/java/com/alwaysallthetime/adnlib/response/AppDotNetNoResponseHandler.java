package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.IAppDotNetObject;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public class AppDotNetNoResponseHandler extends AppDotNetResponseEnvelopeHandler {
    public AppDotNetNoResponseHandler() {
        super(new TypeToken<ResponseEnvelope>(){});
    }

    @Override
    public void onSuccess(IAppDotNetObject responseData) {}
}