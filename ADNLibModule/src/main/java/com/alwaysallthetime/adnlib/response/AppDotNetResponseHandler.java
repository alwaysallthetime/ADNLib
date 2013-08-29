package com.alwaysallthetime.adnlib.response;

import android.util.Log;

import com.alwaysallthetime.adnlib.data.IAppDotNetObject;
import com.alwaysallthetime.adnlib.gson.AppDotNetGson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;

public abstract class AppDotNetResponseHandler<T extends IAppDotNetObject> {
    private static final String TAG = "AppDotNetResponseHandler";

    private final Gson gson;
    private final Type responseType;

    // subclasses must call this constructor and initialize responseType since there is no default constructor
    protected AppDotNetResponseHandler(TypeToken typeToken) {
        gson = AppDotNetGson.getInstance();
        responseType = typeToken.getType();
    }

    protected <T> T parseResponse(Reader reader) {
        return gson.fromJson(reader, responseType);
    }

    public abstract void handleResponse(Reader reader);

    public abstract void onSuccess(T responseData);

    public void onError(Exception error) {
        Log.e(TAG, error.getMessage(), error);
    }
}