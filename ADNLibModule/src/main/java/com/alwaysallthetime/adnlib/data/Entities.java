package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Entities {
    private ArrayList<Mention> mentions;
    @Expose(serialize = false)
    private ArrayList<Hashtag> hashtags;
    private ArrayList<Link> links;
    private boolean parseLinks;

    public Entities() {
        mentions = new ArrayList<Mention>();
        hashtags = new ArrayList<Hashtag>();
        links = new ArrayList<Link>();
    }

    public ArrayList<Mention> getMentions() {
        return mentions;
    }

    public boolean addMentionByName(String name) {
        final Mention mention = new Mention();
        mention.name = name;
        return mentions.add(mention);
    }

    public boolean addMentionById(String id) {
        final Mention mention = new Mention();
        mention.id = id;
        return mentions.add(mention);
    }

    public ArrayList<Hashtag> getHashtags() {
        return hashtags;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public boolean addLink(int pos, int len, String url) {
        final Link link = new Link();
        link.pos = pos;
        link.len = len;
        link.url = url;
        return links.add(link);
    }

    public void setParseLinks(boolean parseLinks) {
        this.parseLinks = parseLinks;
    }

    public static abstract class Entity {
        public abstract int getPos();
        public abstract int getLen();
    }

    public static class Mention extends Entity {
        private String name;
        private String id;
        @Expose(serialize = false)
        private int pos;
        @Expose(serialize = false)
        private int len;
        @Expose(serialize = false)
        private boolean isLeading;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public int getPos() {
            return pos;
        }

        public int getLen() {
            return len;
        }

        public boolean isLeading() {
            return isLeading;
        }
    }

    public static class Hashtag extends Entity {
        private String name;
        @Expose(serialize = false)
        private int pos;
        @Expose(serialize = false)
        private int len;

        public String getName() {
            return name;
        }

        public int getPos() {
            return pos;
        }

        public int getLen() {
            return len;
        }
    }

    public static class Link extends Entity {
        @Expose(serialize = false)
        private String text;
        private String url;
        private int pos;
        private int len;
        @Expose(serialize = false)
        private int amendedLen;

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }

        public int getPos() {
            return pos;
        }

        public int getLen() {
            return len;
        }

        public int getAmendedLen() {
            return amendedLen;
        }
    }
}