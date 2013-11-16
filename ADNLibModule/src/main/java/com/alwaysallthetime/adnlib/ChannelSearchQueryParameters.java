package com.alwaysallthetime.adnlib;

import java.util.List;

public class ChannelSearchQueryParameters extends QueryParameters {
    // maximum number of parameters for a Channel search
    private static final int INITIAL_CAPACITY = 7;
    private static final float LOAD_FACTOR = 1.0f;

    public static final String ORDER_POPULARITY = "popularity"; //default
    public static final String ORDER_ID = "id";
    public static final String ORDER_ACTIVITY = "activity";

    public ChannelSearchQueryParameters() {
        super(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public void setType(String type) {
        put("type", type);
    }

    public void setCreatorId(String creatorId) {
        put("creator_id", creatorId);
    }

    public void setIsPrivate(boolean isPrivate) {
        put("is_private", isPrivate ? "1" : "0");
    }

    public void setIsPublic(boolean isPublic) {
        put("is_public", isPublic ? "1" : "0");
    }

    public void setTags(List<String> tags) {
        put("tags", getCommaDelimitedString(tags, 20));
    }

    public void setQuery(String query) {
        put("q", query);
    }

    public void setOrder(String order) {
        put("order", order);
    }
}
