package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.File;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class FileResponseHandler extends AppDotNetResponseEnvelopeHandler<File> {
    protected FileResponseHandler() {
        super(new TypeToken<ResponseEnvelope<File>>(){});
    }
}