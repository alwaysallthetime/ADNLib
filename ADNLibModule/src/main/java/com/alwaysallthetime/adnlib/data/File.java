package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class File extends Annotatable {
    private boolean complete;
    private String createdAt;
    private Map<String, DerivedFile> derivedFiles;
    private String fileToken;
    private boolean fileTokenRead;
    private ImageInfo imageInfo;
    private String kind;
    private String mimeType;
    private String name;
    @SerializedName("public")
    private boolean isPublic;
    private String sha1;
    private int size;
    private Source source;
    private int totalSize;
    private String type;
    private String url;
    private String urlExpires;
    private String urlPermanent;
    private String urlShort;
    private User user;

    public boolean isComplete() {
        return complete;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Map<String, DerivedFile> getDerivedFiles() {
        return derivedFiles;
    }

    public String getFileToken() {
        return fileToken;
    }

    public boolean isFileTokenRead() {
        return fileTokenRead;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public String getKind() {
        return kind;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getSha1() {
        return sha1;
    }

    public int getSize() {
        return size;
    }

    public Source getSource() {
        return source;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlExpires() {
        return urlExpires;
    }

    public String getUrlPermanent() {
        return urlPermanent;
    }

    public String getUrlShort() {
        return urlShort;
    }

    public User getUser() {
        return user;
    }
}
