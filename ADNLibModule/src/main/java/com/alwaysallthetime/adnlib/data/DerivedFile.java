package com.alwaysallthetime.adnlib.data;

import java.util.Date;

public class DerivedFile implements IAppDotNetObject {
    private String name;
    private String mimeType;
    private String sha1;
    private int size;
    private String url;
    private Date urlExpires;

    public String getName() {
        return name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getSha1() {
        return sha1;
    }

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public Date getUrlExpires() {
        return urlExpires;
    }
}
