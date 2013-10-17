package com.alwaysallthetime.adnlib.request;

import com.alwaysallthetime.adnlib.data.File;
import com.alwaysallthetime.adnlib.response.AppDotNetResponseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class AppDotNetApiFileUploadRequest extends AppDotNetApiUploadRequest {
    private File file;
    private String mimeType;

    public AppDotNetApiFileUploadRequest(AppDotNetResponseHandler handler, File file, byte[] fileData, String mimeType, String... pathComponents) {
        super(handler, file.getName(), fileData, 0, fileData.length, null, pathComponents);
        this.file = file;
        this.mimeType = mimeType;
    }

    @Override
    public void writeBody(HttpURLConnection connection) throws IOException {
        connection.setRequestProperty(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        connection.setChunkedStreamingMode(0);

        final OutputStream outputStream = connection.getOutputStream();
        final OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

        writeBoundary(streamWriter);
        streamWriter.write(String.format(CONTENT_DISPOSITION_HEADER_WITH_FILENAME, "content", bodyFilename));
        streamWriter.write(String.format(CONTENT_TYPE_HEADER, mimeType));
        streamWriter.write("\r\n");
        streamWriter.flush();

        outputStream.write(bodyBytes, bodyOffset, bodyCount);
        writeBoundary(streamWriter);
        writeDispositionHeader(streamWriter, "type", file.getType());

        String kind = file.getKind();
        if(kind != null) {
            writeBoundary(streamWriter);
            writeDispositionHeader(streamWriter, "kind", kind);
        }

        writeBoundary(streamWriter);
        writeDispositionHeader(streamWriter, "name", file.getName());

        writeBoundary(streamWriter);
        writeDispositionHeader(streamWriter, "public", String.valueOf(file.isPublic()));

        writeBoundary(streamWriter);
        streamWriter.close();
    }
}