package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.FileList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class FileListResponseHandler extends AppDotNetResponseEnvelopeHandler<FileList> {
    protected FileListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<FileList>>(){});
    }
}