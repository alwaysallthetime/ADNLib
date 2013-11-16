package com.alwaysallthetime.adnlib;

import java.util.List;

public class PostSearchQueryParameters extends QueryParameters {
    // maximum number of parameters for a Post search
    private static final int INITIAL_CAPACITY = 29;
    private static final float LOAD_FACTOR = 1.0f;

    public static final String INDEX_COMPLETE = "complete";
    public static final String ORDER_ID = "id";
    public static final String ORDER_SCORE = "score";

    public PostSearchQueryParameters() {
        super(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public PostSearchQueryParameters(IQueryParameter... queryParameters) {
        super(queryParameters);
    }

    public PostSearchQueryParameters(String query) {
        super();
        setQuery(query);
    }

    /**
     * Type of index to use. The default (and currently, the only) index is complete, which searches
     * all posts. We may add additional index types later (e.g., an index only of recent posts, for
     * speed.)
     *
     * @param index
     * @see com.alwaysallthetime.adnlib.PostSearchQueryParameters#INDEX_COMPLETE
     */
    public void setIndex(String index) {
        put("index", index);
    }

    /**
     * One of: id (default), score. Searches of ordering id are returned in roughly the same order
     * as other streams, and support pagination. Searches of ordering score are returned by a
     * relevance score. Currently, the only ordering that supports pagination is id, and we are
     * working on improving relevance scores.
     *
     * @param order
     * @see com.alwaysallthetime.adnlib.PostSearchQueryParameters#ORDER_ID
     * @see com.alwaysallthetime.adnlib.PostSearchQueryParameters#ORDER_SCORE
     */
    public void setOrder(String order) {
        put("order", order);
    }

    /**
     * Automatically attempts to extract hashtags and mentions while searching text.
     *
     * @param query
     */
    public void setQuery(String query) {
        put("query", query);
    }

    /**
     * Include posts containing certain text.
     *
     * @param text
     */
    public void setText(String text) {
        put("text", text);
    }

    /**
     * Only include posts tagged with certain hashtags.
     * Do not include #
     *
     * @param hashtags
     */
    public void setHashtags(List<String> hashtags) {
        put("hashtags", getCommaDelimitedString(hashtags, 12));
    }

    /**
     * Only include posts linking to certain URLs.
     *
     * @param links
     */
    public void setLinks(List<String> links) {
        put("links", getCommaDelimitedString(links, 30));
    }

    /**
     * Only include posts linking to certain domains.
     * Do not include www.
     *
     * @param linkDomains
     */
    public void setLinkDomains(List<String> linkDomains) {
        put("link_domains", getCommaDelimitedString(linkDomains, 20));
    }

    /**
     * Only include posts mentioning certain users, by username.
     * Do not include @
     *
     * @param mentions
     */
    public void setMentions(List<String> mentions) {
        put("mentions", getCommaDelimitedString(mentions, 12));
    }

    /**
     * Only include posts directed at certain users, by username.
     * Do not include @
     *
     * @param mentions
     */
    public void setLeadingMentions(List<String> mentions) {
        put("leading_mentions", getCommaDelimitedString(mentions, 12));
    }

    /**
     * Only include posts with a specific annotation type, e.g., net.app.core.fallback_url
     *
     * @param mentions
     */
    public void setAnnotationTypes(List<String> mentions) {
        put("annotation_types", getCommaDelimitedString(mentions, 25));
    }

    /**
     * Only include posts with a specific file type attached via the net.app.core.file_list annotation
     *
     * @param attachmentTypes
     */
    public void setAttachmentTypes(List<String> attachmentTypes) {
        put("attachment_types", getCommaDelimitedString(attachmentTypes, 25));
    }

    /**
     * Only include posts which are crossposts of a specific URL, via the net.app.core.crosspost annotation
     *
     * @param crosspostUrl
     */
    public void setAttachmentTypes(String crosspostUrl) {
        put("crosspost_url", crosspostUrl);
    }

    /**
     * Similar to crosspost_url, but only match on the host portion of the URL. Do not include "www."
     *
     * @param crosspostDomain
     */
    public void setCrosspostDomain(String crosspostDomain) {
        put("crosspost_domain", crosspostDomain);
    }

    /**
     * Only include posts which are check-ins at a specific place, via the net.app.core.checkin annotation
     *
     * @param placeId
     */
    public void setPlaceId(String placeId) {
        put("place_id", placeId);
    }

    /**
     * Only include replies
     *
     * @param isReply
     */
    public void setIsReply(boolean isReply) {
        put("is_reply", isReply ? "1" : "0");
    }

    /**
     * Only include posts with leading mentions, i.e., posts which were directed at other users
     *
     * @param isDirected
     */
    public void setIsDirected(boolean isDirected) {
        put("is_directed", isDirected ? "1" : "0");
    }

    /**
     * This might be broken. See: https://github.com/appdotnet/api-spec/issues/356
     *
     * Only include posts containing geo coordinates, i.e., tagged with the net.app.core.location
     * annotation
     *
     * @param hasLocation
     */
    public void setHasLocation(boolean hasLocation) {
        put("has_location", hasLocation ? "1" : "0");
    }

    /**
     * Only include posts containing place IDs, i.e., tagged with the net.app.core.checkin annotation
     *
     * @param hasCheckin
     */
    public void setHasCheckin(boolean hasCheckin) {
        put("has_checkin", hasCheckin ? "1" : "0");
    }

    /**
     * Only include posts which are crossposts, i.e., tagged with the net.app.core.crosspost annotation
     *
     * @param isCrosspost
     */
    public void setIsCrosspost(boolean isCrosspost) {
        put("is_crosspost", isCrosspost ? "1" : "0");
    }

    /**
     * Only include posts with file attachments
     *
     * @param hasAttachment
     */
    public void setHasAttachment(boolean hasAttachment) {
        put("has_attachment", hasAttachment ? "1" : "0");
    }

    /**
     * Only include posts with photo oembed annotations
     *
     * @param hasOEmbedPhoto
     */
    public void setHasOEmbedPhoto(boolean hasOEmbedPhoto) {
        put("has_oembed_photo", hasOEmbedPhoto ? "1" : "0");
    }

    /**
     * Only include posts with video (not html5video) oembed annotations
     *
     * @param hasOEmbedVideo
     */
    public void setHasOEmbedVideo(boolean hasOEmbedVideo) {
        put("has_oembed_video", hasOEmbedVideo ? "1" : "0");
    }

    /**
     * Only include posts with html5video oembed annotations
     *
     * @param hasOEmbedHTML5Video
     */
    public void setHasOEmbedHTML5Video(boolean hasOEmbedHTML5Video) {
        put("has_oembed_html5video", hasOEmbedHTML5Video ? "1" : "0");
    }

    /**
     * Only include posts with rich oembed annotations
     *
     * @param hasOEmbedRich
     */
    public void setHasOEmbedRich(boolean hasOEmbedRich) {
        put("has_oembed_rich", hasOEmbedRich ? "1" : "0");
    }

    /**
     * Only include posts with a certain language tagged with the net.app.core.language annotation.
     *
     * @param language
     */
    public void setLanguage(String language) {
        put("language", language);
    }

    /**
     * Only include posts created by a certain app. Use the alphanumeric client_id.
     *
     * @param clientId
     */
    public void setClientId(String clientId) {
        put("client_id", clientId);
    }

    /**
     * Only include posts created by a specific user. Use the user ID, not the username.
     *
     * @param creatorId
     */
    public void setCreatorId(String creatorId) {
        put("creator_id", creatorId);
    }

    /**
     * Only include immediate replies to a given post ID.
     *
     * @param replyTo
     */
    public void setReplyTo(String replyTo) {
        put("reply_to", replyTo);
    }

    /**
     * Only include posts on a specific thread.
     *
     * @param threadId
     */
    public void setThreadId(String threadId) {
        put("thread_id", threadId);
    }
}
