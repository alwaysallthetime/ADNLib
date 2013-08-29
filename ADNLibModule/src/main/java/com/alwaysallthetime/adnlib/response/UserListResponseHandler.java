package com.alwaysallthetime.adnlib.response;

import com.alwaysallthetime.adnlib.data.ResponseEnvelope;
import com.alwaysallthetime.adnlib.data.UserList;
import com.google.gson.reflect.TypeToken;

public abstract class UserListResponseHandler extends AppDotNetResponseEnvelopeHandler<UserList> {
    protected UserListResponseHandler() {
        super(new TypeToken<ResponseEnvelope<UserList>>(){});
    }
}