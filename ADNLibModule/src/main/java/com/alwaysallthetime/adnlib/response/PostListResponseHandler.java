package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.PostList;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class PostListResponseHandler extends AppDotNetResponseEnvelopeHandler<PostList> {
    protected PostListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<PostList>>(){});
    }
}