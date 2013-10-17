package com.alwaysallthetime.adnlib.request;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class AppDotNetApiUploadRequest extends AppDotNetApiRequest {
    private static final String DELIMITER = "--";
    private static final String BOUNDARY = "bWljaGFlbCBqYWNrc29uIGlzIHN0aWxsIGFsaXZl";
    protected static final String CONTENT_TYPE = "multipart/form-data; boundary=" + BOUNDARY;
    protected static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition: form-data; name=\"%1$s\"\r\n";
    protected static final String CONTENT_DISPOSITION_HEADER_WITH_FILENAME = "Content-Disposition: form-data; name=\"%1$s\"; filename=\"%2$s\"\r\n";
    protected static final String CONTENT_TYPE_HEADER = "Content-Type: %1$s\r\n";

    protected final String bodyFilename;
    protected final byte[] bodyBytes;
    protected final int bodyOffset;
    protected final int bodyCount;

    public AppDotNetApiUploadRequest(AppDotNetResponseHandler handler, String filename, byte[] image, int offset,
                                         int count, QueryParameters queryParameters, String... pathComponents) {
        super(handler, AppDotNetClient.METHOD_POST, queryParameters, pathComponents);

        hasBody = true;
        bodyFilename = filename;
        bodyBytes = image;
        bodyOffset = offset;
        bodyCount = count;
    }

    protected void writeDispositionHeader(OutputStreamWriter streamWriter, String name, String body) throws IOException {
        streamWriter.write(String.format(CONTENT_DISPOSITION_HEADER, name));
        streamWriter.write("\r\n");
        streamWriter.write(body);
    }

    protected void writeBoundary(OutputStreamWriter streamWriter) throws IOException {
        streamWriter.write(DELIMITER);
        streamWriter.write(BOUNDARY);
        streamWriter.write("\r\n");
    }
}
