package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.alwaysallthetime.adnlib.data.User;
import com.google.gson.reflect.TypeToken;

public abstract class UserResponseHandler extends AppDotNetResponseEnvelopeHandler<User> {
    protected UserResponseHandler() {
        super(new TypeToken<ResponseEnvelope<User>>(){});
    }
}