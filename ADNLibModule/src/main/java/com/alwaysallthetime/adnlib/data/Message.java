package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

public class Message extends AbstractPost {
    @Expose(serialize = false)
    private String channelId;

    public Message() {}

    public Message(String text) {
        super(text);
    }

    public Message(boolean machineOnly) {
        super(machineOnly);
    }

    public String getChannelId() {
        return channelId;
    }
}
