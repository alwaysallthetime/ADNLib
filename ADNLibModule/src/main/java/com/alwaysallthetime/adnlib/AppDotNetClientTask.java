package com.alwaysallthetime.adnlib;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.alwaysallthetime.adnlib.request.AppDotNetRequest;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

class AppDotNetClientTask extends AsyncTask<AppDotNetRequest, Void, Integer> {
    private static final String TAG = "AppDotNetClientTask";

    private final String authorizationHeader;
    private final String languageHeader;
    private final SSLSocketFactory sslSocketFactory;
    private final Context context;

    public AppDotNetClientTask(Context context, String authorizationHeader, String languageHeader, SSLSocketFactory sslSocketFactory) {
        this.context = context;
        this.authorizationHeader = authorizationHeader;
        this.languageHeader = languageHeader;
        this.sslSocketFactory = sslSocketFactory;
    }

    public AppDotNetClientTask(Context context, String authorizationHeader, SSLSocketFactory sslSocketFactory) {
        this(context, authorizationHeader, null, sslSocketFactory);
    }

    public AppDotNetClientTask(Context context, String authorizationHeader) {
        this(context, authorizationHeader, null);
    }

    @Override
    protected Integer doInBackground(AppDotNetRequest... requests) {
        //the number of requests is always 1
        AppDotNetRequest request = requests[0];

        final AppDotNetResponseHandler handler = request.getHandler();
        final URL url = request.getUrl();
        HttpURLConnection connection = null;
        int responseCode = 0;
        InputStream inputStream;

        Log.d(TAG, "Connecting to " + url.toString());
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());

            if (sslSocketFactory != null && url.getProtocol().equals("https"))
                ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);

            if (request.isAuthenticated())
                connection.setRequestProperty("Authorization", authorizationHeader);

            if (languageHeader != null)
                connection.setRequestProperty("Accept-Language", languageHeader);

            if (request.hasBody()) {
                connection.setDoOutput(true);
                request.writeBody(connection);
            }

            responseCode = connection.getResponseCode();
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            // HttpURLConnection throws IOException for non-200 statuses, but App.net provides a full response
            // envelope that can be parsed normally below. Therefore if the error stream exists, treat it as the
            // input stream.
            inputStream = connection.getErrorStream();

            if (inputStream == null) {
                // Otherwise pass the exception to the handler's onError.
                handler.onError(e);
                return responseCode;
            }
        }

        final String contentType = connection.getContentType();
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            // Nothing else to do for a 204
            handler.onSuccess(null);
            return responseCode;

        } else if (contentType == null || !contentType.equals(AppDotNetRequest.CONTENT_TYPE_JSON)) {
            // If the response is not JSON, assume there was an error (probably 500 or 503) and the status
            // message is the error.
            try {
                final String message = connection.getResponseMessage();
                handler.onError(new AppDotNetResponseException(message, responseCode));
            } catch (IOException e) {
                handler.onError(e);
            }

            return responseCode;
        }

        final Reader reader = new InputStreamReader(inputStream);
        handler.handleResponse(reader);

        try {
            reader.close();
        } catch (IOException e) {
            handler.onError(e);
        } finally {
            connection.disconnect();
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer statusCode) {
        super.onPostExecute(statusCode);

        if(statusCode != null && statusCode >= 400) {
            Intent broadcast = new Intent(AppDotNetClient.INTENT_ACTION_RECEIVED_FAILURE_STATUS_CODE);
            broadcast.putExtra(AppDotNetClient.EXTRA_STATUS_CODE, statusCode);
            context.sendBroadcast(broadcast);
        }
    }
}