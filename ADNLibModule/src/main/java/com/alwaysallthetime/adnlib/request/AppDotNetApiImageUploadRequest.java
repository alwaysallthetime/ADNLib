package com.alwaysallthetime.adnlib.request;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class AppDotNetApiImageUploadRequest extends AppDotNetApiRequest {
    private static final String DELIMITER = "--";
    private static final String BOUNDARY = "-bWljaGFlbCBqYWNrc29uIGlzIHN0aWxsIGFsaXZl-";
    private static final String CONTENT_TYPE = "multipart/form-data; boundary=" + BOUNDARY;
    private static final String PART_HEADER = "\r\nContent-Disposition: form-data; name=\"%1$s\"; filename=\"%1$s\"\r\n\r\n";

    private final String bodyFilename;
    private final byte[] bodyBytes;
    private final int bodyOffset;
    private final int bodyCount;

    public AppDotNetApiImageUploadRequest(AppDotNetResponseHandler handler, String filename, byte[] image, int offset,
                                          int count, QueryParameters queryParameters, String... pathComponents) {
        super(handler, AppDotNetClient.METHOD_POST, queryParameters, pathComponents);
        hasBody = true;
        bodyFilename = filename;
        bodyBytes = image;
        bodyOffset = offset;
        bodyCount = count;
    }

    public AppDotNetApiImageUploadRequest(AppDotNetResponseHandler handler, String filename, byte[] image,
                                          QueryParameters queryParameters, String... pathComponents) {
        this(handler, filename, image, 0, image.length, queryParameters, pathComponents);
    }

    @Override
    public void writeBody(HttpURLConnection connection) throws IOException {
        connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        connection.setChunkedStreamingMode(0);

        final OutputStream outputStream = connection.getOutputStream();
        final OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

        streamWriter.write(DELIMITER);
        streamWriter.write(BOUNDARY);
        streamWriter.write(String.format(PART_HEADER, bodyFilename));
        streamWriter.flush();

        outputStream.write(bodyBytes, bodyOffset, bodyCount);

        streamWriter.write(DELIMITER);
        streamWriter.write(BOUNDARY);
        streamWriter.write(DELIMITER);
        streamWriter.close();
    }
}