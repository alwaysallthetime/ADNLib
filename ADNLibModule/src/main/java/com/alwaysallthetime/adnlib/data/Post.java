package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

public class Post extends AbstractPost {
    @Expose(serialize = false)
    private String canonicalUrl;
    @Expose(serialize = false)
    private int numStars;
    @Expose(serialize = false)
    private int numReposts;
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

    public Post() {}

    public Post(String text) {
        super(text);
    }

    public Post(boolean machineOnly) {
        super(machineOnly);
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public int getNumStars() {
        return numStars;
    }

    public int getNumReposts() {
        return numReposts;
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
