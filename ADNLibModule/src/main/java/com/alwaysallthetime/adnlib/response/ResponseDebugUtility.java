package com.alwaysallthetime.adnlib.response;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ResponseDebugUtility {
    private static final String TAG = "ADNLib_ResponseDebugUtility";

    public static void logResponse(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                total.append(line);
            }
            Log.d(TAG, total.toString());
        } catch(IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
