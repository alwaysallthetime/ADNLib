package com.alwaysallthetime.adnlib.data;

public class Token implements IAppDotNetObject {
    private App app;
    private String clientId;
    private String[] scopes;
    private Limits limits;
    private Storage storage;
    private User user;
    private String inviteLink;

    public App getApp() {
        return app;
    }

    public String getClientId() {
        return clientId;
    }

    public String[] getScopes() {
        return scopes;
    }

    public Limits getLimits() {
        return limits;
    }

    public Storage getStorage() {
        return storage;
    }

    public User getUser() {
        return user;
    }

    public String getInviteLink() {
        return inviteLink;
    }

    public static class Limits {
        private int following;
        private long maxFileSize;

        protected Limits() {
        }

        public int getFollowing() {
            return following;
        }

        public long getMaxFileSize() {
            return maxFileSize;
        }
    }

    public static class Storage {
        private long available;
        private long used;

        protected Storage() {
        }

        public long getAvailable() {
            return available;
        }

        public long getUsed() {
            return used;
        }
    }
}