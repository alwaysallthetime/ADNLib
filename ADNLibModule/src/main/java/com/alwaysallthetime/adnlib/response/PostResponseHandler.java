package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.Post;
import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.google.gson.reflect.TypeToken;

public abstract class PostResponseHandler extends AppDotNetResponseEnvelopeHandler<Post> {
    protected PostResponseHandler() {
        super(new TypeToken<ResponseEnvelope<Post>>(){});
    }
}