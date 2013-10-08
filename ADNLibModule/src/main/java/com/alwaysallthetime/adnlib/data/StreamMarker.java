package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class StreamMarker implements IAppDotNetObject {
    private String id;
    @Expose(serialize = false)
    private String lastReadId;
    private String name;
    private int percentage;
    @Expose(serialize = false)
    private Date updatedAt;
    @Expose(serialize = false)
    private String version;

    public StreamMarker() {}

    public StreamMarker(String id, String name, int percentage) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastReadId() {
        return lastReadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getVersion() {
        return version;
    }
}
