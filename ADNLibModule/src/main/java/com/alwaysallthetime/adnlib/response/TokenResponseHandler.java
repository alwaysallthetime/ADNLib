package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.alwaysallthetime.adnlib.data.Token;
import com.google.gson.reflect.TypeToken;

public abstract class TokenResponseHandler extends AppDotNetResponseEnvelopeHandler<Token> {
    protected TokenResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Token>>(){});
    }
}