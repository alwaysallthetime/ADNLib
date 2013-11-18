package com.alwaysallthetime.adnlib.data;

import com.alwaysallthetime.adnlib.gson.AppDotNetGson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Channel extends Annotatable {
    @Expose(serialize = false)
    private Counts counts;
    private String type;
    @Expose(serialize = false)
    private User owner;
    private Acl readers;
    private Acl writers;
    @Expose(serialize = false)
    private boolean youMuted;
    @Expose(serialize = false)
    private boolean youSubscribed;
    @Expose(serialize = false)
    private boolean youCanEdit;
    @Expose(serialize = false)
    private boolean hasUnread;
    @Expose(serialize = false)
    private String recentMessageId;
    @Expose(serialize = false)
    private Message recentMessage;
    @Expose(serialize = false)
    private StreamMarker marker;

    public Channel(String type, boolean immutable) {
        this.type = type;
        counts = new Counts();
        readers = new Acl(immutable);
        writers = new Acl(immutable);
    }

    public Channel(String type) {
        this(type, true);
    }

    public Channel() {
        this(null, true);
    }

    public Channel copyForWriting() {
        final Channel copy = AppDotNetGson.copyForWriting(this);
        copy.id = this.id;
        copy.setType(null);
        copy.clearAnnotations();

        if (copy.getReaders().isImmutable())
            copy.setReaders(null);

        if (copy.getWriters().isImmutable())
            copy.setWriters(null);

        return copy;
    }

    public int getMessageCount() {
        return counts.messages;
    }

    public int getSubscriberCount() {
        return counts.subscribers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public Acl getReaders() {
        return readers;
    }

    public void setReaders(Acl readers) {
        this.readers = readers;
    }

    public Acl getWriters() {
        return writers;
    }

    public void setWriters(Acl writers) {
        this.writers = writers;
    }

    public boolean isMuted() {
        return youMuted;
    }

    public boolean isSubscribed() {
        return youSubscribed;
    }

    public boolean isEditable() {
        return youCanEdit;
    }

    public boolean isUnread() {
        return hasUnread;
    }

    public String getRecentMessageId() {
        return recentMessageId;
    }

    public Message getRecentMessage() {
        return recentMessage;
    }

    public StreamMarker getMarker() {
        return marker;
    }

    private static class Counts {
        private int messages;
        private int subscribers;
    }

    public static class Acl {
        private boolean anyUser;
        private boolean immutable = true;
        @SerializedName("public")
        private boolean publicAccess;
        private String[] userIds;
        @Expose(serialize = false)
        private boolean you;

        public Acl() {}

        public Acl(boolean immutable) {
            this.immutable = immutable;
        }

        public boolean isAnyUser() {
            return anyUser;
        }

        public void setAnyUser(boolean anyUser) {
            this.anyUser = anyUser;

            if (anyUser) {
                publicAccess = false;
                userIds = null;
            }
        }

        public boolean isImmutable() {
            return immutable;
        }

        public void setImmutable(boolean immutable) {
            this.immutable = immutable;
        }

        public boolean isPublic() {
            return publicAccess;
        }

        public void setPublic(boolean publicAccess) {
            this.publicAccess = publicAccess;

            if (publicAccess) {
                anyUser = false;
                userIds = null;
            }
        }

        public String[] getUserIds() {
            return userIds;
        }

        public void setUserIds(String[] userIds) {
            this.userIds = userIds;

            if (userIds != null) {
                anyUser = false;
                publicAccess = false;
            }
        }

        public boolean isCurrentUserAuthorized() {
            return you;
        }
    }
}
