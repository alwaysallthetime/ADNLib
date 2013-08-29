package com.alwaysallthetime.adnlib;

import android.os.AsyncTask;
import android.util.Log;

import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;
import com.alwaysallthetime.adnlib.request.AppDotNetRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

class AppDotNetClientTask extends AsyncTask<AppDotNetRequest, Void, Void> {
    private static final String TAG = "AppDotNetClientTask";

    private final String authorizationHeader;

    public AppDotNetClientTask(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }

    @Override
    protected Void doInBackground(AppDotNetRequest... requests) {
        for (AppDotNetRequest request : requests) {
            final AppDotNetResponseHandler handler = request.getHandler();
            final URL url = request.getUrl();
            HttpURLConnection connection = null;
            int responseCode = 0;
            InputStream inputStream;

            Log.d(TAG, "Connecting to " + url.toString());
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(request.getMethod());

                if (request.isAuthenticated())
                    connection.setRequestProperty("Authorization", authorizationHeader);

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
                    return null;
                }
            }

            final String contentType = connection.getContentType();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Nothing else to do for a 204
                handler.onSuccess(null);
                return null;

            } else if (contentType == null || !contentType.equals(AppDotNetRequest.CONTENT_TYPE_JSON)) {
                // If the response is not JSON, assume there was an error (probably 500 or 503) and the status
                // message is the error.
                try {
                    final String message = connection.getResponseMessage();
                    handler.onError(new AppDotNetResponseException(message, responseCode));
                } catch (IOException e) {
                    handler.onError(e);
                }

                return null;
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
        }

        return null;
    }
}