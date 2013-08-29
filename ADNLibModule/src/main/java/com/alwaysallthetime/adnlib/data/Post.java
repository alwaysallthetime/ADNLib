package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Post extends Annotatable implements IAppDotNetObject {
    @Expose(serialize = false)
    private String id;
    @Expose(serialize = false)
    private User user;
    @Expose(serialize = false)
    private Date createdAt;
    private String text;
    @Expose(serialize = false)
    private String html;
    @Expose(serialize = false)
    private App source;
    private String replyTo;
    @Expose(serialize = false)
    private String canonicalUrl;
    @Expose(serialize = false)
    private String threadId;
    @Expose(serialize = false)
    private int numReplies;
    @Expose(serialize = false)
    private int numStars;
    @Expose(serialize = false)
    private int numReposts;
    private Entities entities;
    @Expose(serialize = false)
    private boolean isDeleted;
    private boolean machineOnly;
    @Expose(serialize = false)
    private boolean youStarred;
    @Expose(serialize = false)
    private User[] starredBy;
    @Expose(serialize = false)
    private boolean youReposted;
    @Expose(serialize = false)
    private User[] reposters;
    @Expose(serialize = false)
    private Post repostOf;

    public Post() {
    }

    public Post(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public App getSource() {
        return source;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public String getThreadId() {
        return threadId;
    }

    public int getNumReplies() {
        return numReplies;
    }

    public int getNumStars() {
        return numStars;
    }

    public int getNumReposts() {
        return numReposts;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isMachineOnly() {
        return machineOnly;
    }

    public void setMachineOnly(boolean machineOnly) {
        this.machineOnly = machineOnly;
    }

    public boolean isStarredByYou() {
        return youStarred;
    }

    public User[] getStarredBy() {
        return starredBy;
    }

    public boolean isRepostedByYou() {
        return youReposted;
    }

    public User[] getReposters() {
        return reposters;
    }

    public Post getRepostOf() {
        return repostOf;
    }
}