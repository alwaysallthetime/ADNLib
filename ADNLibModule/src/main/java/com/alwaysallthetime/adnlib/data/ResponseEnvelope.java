package com.alwaysallthetime.adnlib.data;

public class ResponseEnvelope<T extends IAppDotNetObject> implements IAppDotNetObject {
    private ResponseMeta meta;
    private T data;

    public ResponseMeta getMeta() {
        return meta;
    }

    public T getData() {
        return data;
    }
}
