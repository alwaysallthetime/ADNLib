package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.IdList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class IdListResponseHandler extends AppDotNetResponseEnvelopeHandler<IdList> {
    protected IdListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<IdList>>(){});
    }
}
