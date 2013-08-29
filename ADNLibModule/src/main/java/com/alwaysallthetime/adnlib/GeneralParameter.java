package com.alwaysallthetime.adnlib;

public enum GeneralParameter implements IQueryParameter {
    INCLUDE_ANNOTATIONS("include_annotations"),
    INCLUDE_USER_ANNOTATIONS("include_user_annotations"),
    INCLUDE_POST_ANNOTATIONS("include_post_annotations"),
    INCLUDE_CHANNEL_ANNOTATIONS("include_channel_annotations"),
    INCLUDE_MESSAGE_ANNOTATIONS("include_message_annotations"),
    INCLUDE_FILE_ANNOTATIONS("include_file_annotations"),
    EXCLUDE_HTML("include_html", false),
    INCLUDE_MUTED("include_muted"),
    EXCLUDE_DELETED("include_deleted", false),
    INCLUDE_DIRECTED_POSTS("include_directed_posts"),
    INCLUDE_MACHINE("include_machine"),
    INCLUDE_STARRED_BY("include_starred_by"),
    INCLUDE_REPOSTERS("include_reposters"),
    INCLUDE_MARKER("include_marker"),
    EXCLUDE_READ("include_read", false),
    INCLUDE_RECENT_MESSAGE("include_recent_message"),
    EXCLUDE_INCOMPLETE("include_incomplete", false),
    EXCLUDE_PRIVATE("include_private", false);

    private String name;
    private String value;

    private GeneralParameter(String name, boolean value) {
        this.name = name;
        this.value = (value ? "1" : "0");
    }

    private GeneralParameter(String name) {
        this(name, true);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}