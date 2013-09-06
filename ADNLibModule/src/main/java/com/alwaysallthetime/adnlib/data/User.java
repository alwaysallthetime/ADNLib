package com.alwaysallthetime.adnlib.data;

import com.alwaysallthetime.adnlib.gson.AppDotNetGson;
import com.google.gson.annotations.Expose;

import java.util.Date;

public class User extends Annotatable {
    @Expose(serialize = false)
    private String username;
    private String name;
    private Description description;
    private String timezone;
    private String locale;
    @Expose(serialize = false)
    private Image avatarImage;
    @Expose(serialize = false)
    private Image coverImage;
    @Expose(serialize = false)
    private String type;
    @Expose(serialize = false)
    private Date createdAt;
    @Expose(serialize = false)
    private Counts counts;
    @Expose(serialize = false)
    private boolean followsYou;
    @Expose(serialize = false)
    private boolean youBlocked;
    @Expose(serialize = false)
    private boolean youFollow;
    @Expose(serialize = false)
    private boolean youMuted;
    @Expose(serialize = false)
    private boolean youCanSubscribe;
    @Expose(serialize = false)
    private boolean youCanFollow;
    @Expose(serialize = false)
    private String verifiedDomain;
    @Expose(serialize = false)
    private String canonicalUrl;

    public User() {
        description = new Description();
    }

    public User copyForWriting() {
        final User copy = AppDotNetGson.copyForWriting(this);
        copy.getDescription().clearEntities();
        copy.clearAnnotations();
        return copy;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Image getAvatarImage() {
        return avatarImage;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public String getType() {
        return type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Counts getCounts() {
        return counts;
    }

    public boolean isFollowingYou() {
        return followsYou;
    }

    public boolean isBlocked() {
        return youBlocked;
    }

    public boolean isFollowed() {
        return youFollow;
    }

    public boolean isMuted() {
        return youMuted;
    }

    public boolean isSubscribable() {
        return youCanSubscribe;
    }

    public boolean isFollowable() {
        return youCanFollow;
    }

    public String getVerifiedDomain() {
        return verifiedDomain;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public static class Description {
        private String text;
        @Expose(serialize = false)
        private String html;
        private Entities entities;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getHtml() {
            return html;
        }

        public Entities getEntities() {
            return entities;
        }

        public void setEntities(Entities entities) {
            this.entities = entities;
        }

        public void clearEntities() {
            entities = null;
        }
    }

    public static class Counts {
        private int following;
        private int followers;
        private int posts;
        private int stars;

        protected Counts() {
        }

        public int getFollowing() {
            return following;
        }

        public int getFollowers() {
            return followers;
        }

        public int getPosts() {
            return posts;
        }

        public int getStars() {
            return stars;
        }
    }
}
