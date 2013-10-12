package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.Configuration;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class ConfigurationResponseHandler extends AppDotNetResponseEnvelopeHandler<Configuration> {
    protected ConfigurationResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Configuration>>(){});
    }
}