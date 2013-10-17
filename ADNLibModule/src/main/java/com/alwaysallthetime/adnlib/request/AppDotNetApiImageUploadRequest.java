package com.alwaysallthetime.adnlib.request;

import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class AppDotNetApiImageUploadRequest extends AppDotNetApiUploadRequest {
    public AppDotNetApiImageUploadRequest(AppDotNetResponseHandler handler, String filename, byte[] image, int offset,
                                          int count, QueryParameters queryParameters, String... pathComponents) {
        super(handler, filename, image, offset, count, queryParameters, pathComponents);
    }
    public AppDotNetApiImageUploadRequest(AppDotNetResponseHandler handler, String filename, byte[] image,
                                          QueryParameters queryParameters, String... pathComponents) {
        super(handler, filename, image, 0, image.length, queryParameters, pathComponents);
    }

    @Override
    public void writeBody(HttpURLConnection connection) throws IOException {
        connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        connection.setChunkedStreamingMode(0);

        final OutputStream outputStream = connection.getOutputStream();
        final OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

        writeBoundary(streamWriter);
        streamWriter.write(String.format(CONTENT_DISPOSITION_HEADER_WITH_FILENAME, bodyFilename, bodyFilename));
        streamWriter.write("\r\n");
        streamWriter.flush();

        outputStream.write(bodyBytes, bodyOffset, bodyCount);

        writeBoundary(streamWriter);
        streamWriter.close();
    }
}