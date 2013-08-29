package com.alwaysallthetime.adnlib.request;

import android.net.Uri;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

public abstract class AppDotNetFormRequest extends AppDotNetRequest {
    protected AppDotNetFormRequest(AppDotNetResponseHandler handler, Uri baseUri, List<? extends NameValuePair> body, String... pathComponents) {
        super(handler, false, AppDotNetClient.METHOD_POST, null, baseUri, pathComponents);
        setBody(URLEncodedUtils.format(body, "UTF-8"));
    }

    @Override
    public void writeBody(HttpURLConnection connection) throws IOException {
        connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE_FORM);
        writeFixedLengthBody(connection);
    }
}