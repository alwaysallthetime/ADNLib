package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class File extends Annotatable {
    @Expose(serialize = false)
    private boolean complete;
    @Expose(serialize = false)
    private String createdAt;
    @Expose(serialize = false)
    private Map<String, DerivedFile> derivedFiles;
    @Expose(serialize = false)
    private String fileToken;
    @Expose(serialize = false)
    private boolean fileTokenRead;
    @Expose(serialize = false)
    private ImageInfo imageInfo;
    private String kind;
    @Expose(serialize = false)
    private String mimeType;
    private String name;
    @SerializedName("public")
    private boolean isPublic;
    @Expose(serialize = false)
    private String sha1;
    @Expose(serialize = false)
    private int size;
    @Expose(serialize = false)
    private Source source;
    @Expose(serialize = false)
    private int totalSize;
    private String type;
    @Expose(serialize = false)
    private String url;
    @Expose(serialize = false)
    private String urlExpires;
    @Expose(serialize = false)
    private String urlPermanent;
    @Expose(serialize = false)
    private String urlShort;
    @Expose(serialize = false)
    private User user;

    public File() {}

    public File(String kind, String type, String name, boolean isPublic) {
        this.kind = kind;
        this.type = type;
        this.name = name;
        this.isPublic = isPublic;
    }

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
