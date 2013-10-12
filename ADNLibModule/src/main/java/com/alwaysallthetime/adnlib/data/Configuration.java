package com.alwaysallthetime.adnlib.data;

public class Configuration implements IAppDotNetObject {
    private TextConfiguration text;
    private ResourceConfiguration user;
    private ResourceConfiguration file;
    private ResourceConfiguration post;
    private ResourceConfiguration message;
    private ResourceConfiguration channel;

    public TextConfiguration getText() {
        return text;
    }

    public ResourceConfiguration getUser() {
        return user;
    }

    public ResourceConfiguration getFile() {
        return file;
    }

    public ResourceConfiguration getPost() {
        return post;
    }

    public ResourceConfiguration getMessage() {
        return message;
    }

    public ResourceConfiguration getChannel() {
        return channel;
    }
}
