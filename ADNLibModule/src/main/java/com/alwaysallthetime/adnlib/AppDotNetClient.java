package com.alwaysallthetime.adnlib;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.alwaysallthetime.adnlib.data.Annotatable;
import com.alwaysallthetime.adnlib.data.Channel;
import com.alwaysallthetime.adnlib.data.Entities;
import com.alwaysallthetime.adnlib.data.File;
import com.alwaysallthetime.adnlib.data.Message;
import com.alwaysallthetime.adnlib.data.Post;
import com.alwaysallthetime.adnlib.data.PrivateMessage;
import com.alwaysallthetime.adnlib.data.StreamMarker;
import com.alwaysallthetime.adnlib.data.StreamMarkerList;
import com.alwaysallthetime.adnlib.data.User;
import com.alwaysallthetime.adnlib.request.AppDotNetApiFileUploadRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiImageUploadRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiJsonRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetOAuthRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetRequest;
import com.alwaysallthetime.adnlib.response.AccessTokenResponseHandler;
import com.alwaysallthetime.adnlib.response.ChannelListResponseHandler;
import com.alwaysallthetime.adnlib.response.ChannelResponseHandler;
import com.alwaysallthetime.adnlib.response.ConfigurationResponseHandler;
import com.alwaysallthetime.adnlib.response.CountResponseHandler;
import com.alwaysallthetime.adnlib.response.FileListResponseHandler;
import com.alwaysallthetime.adnlib.response.FileResponseHandler;
import com.alwaysallthetime.adnlib.response.IdListResponseHandler;
import com.alwaysallthetime.adnlib.response.InteractionListResponseHandler;
import com.alwaysallthetime.adnlib.response.LoginResponseHandler;
import com.alwaysallthetime.adnlib.response.MessageListResponseHandler;
import com.alwaysallthetime.adnlib.response.MessageResponseHandler;
import com.alwaysallthetime.adnlib.response.PlaceListResponseHandler;
import com.alwaysallthetime.adnlib.response.PlaceResponseHandler;
import com.alwaysallthetime.adnlib.response.PostListResponseHandler;
import com.alwaysallthetime.adnlib.response.PostResponseHandler;
import com.alwaysallthetime.adnlib.response.StreamMarkerListResponseHandler;
import com.alwaysallthetime.adnlib.response.StreamMarkerResponseHandler;
import com.alwaysallthetime.adnlib.response.TokenResponseHandler;
import com.alwaysallthetime.adnlib.response.UserListResponseHandler;
import com.alwaysallthetime.adnlib.response.UserResponseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

public class AppDotNetClient {
    //currently this is broadcasted if a status code >= 400 is received.
    public static final String INTENT_ACTION_RECEIVED_FAILURE_STATUS_CODE = "com.alwaysallthetime.adnlib.AppDotNetClient.intent.failure";
    public static final String EXTRA_STATUS_CODE = "com.alwaysallthetime.adnlib.extras.statusCode";

    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";

    protected static final int ID_LENGTH = 10; // max string length of object ID including delimiter
    protected static final String ENDPOINT_USERS = "users";
    protected static final String ENDPOINT_POSTS = "posts";
    protected static final String ENDPOINT_STARS = "stars";
    protected static final String ENDPOINT_INTERACTIONS = "interactions";
    protected static final String ENDPOINT_MENTIONS = "mentions";
    protected static final String ENDPOINT_CHANNELS = "channels";
    protected static final String ENDPOINT_MESSAGES = "messages";
    protected static final String ENDPOINT_PLACES = "places";
    protected static final String ENDPOINT_FILES = "files";
    protected static final String ENDPOINT_CONFIGURATION = "config";

    protected Context context;
    protected String authHeader;
    protected String languageHeader;
    protected List<NameValuePair> authParams;
    protected SSLSocketFactory sslSocketFactory;

    public AppDotNetClient(Context applicationContext) {
        final Locale locale = Locale.getDefault();
        languageHeader = String.format("%s-%s", locale.getLanguage(), locale.getCountry());
        this.context = applicationContext;
    }

    public AppDotNetClient(Context applicationContext, String token) {
        this(applicationContext);
        setToken(token);
    }

    public AppDotNetClient(Context applicationContext, String clientId, String passwordGrantSecret) {
        this(applicationContext);
        authParams = new ArrayList<NameValuePair>(3);
        authParams.add(new BasicNameValuePair("client_id", clientId));
        authParams.add(new BasicNameValuePair("password_grant_secret", passwordGrantSecret));
        authParams.add(new BasicNameValuePair("grant_type", "password"));
    }

    public void setToken(String token) {
        authHeader = "Bearer " + token;
    }

    public boolean hasToken() {
        return authHeader != null;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    /*
     * OAUTH
     */

    public void authenticateWithPassword(String username, String password, String scope, LoginResponseHandler responseHandler) {
        if (authParams == null)
            throw new IllegalStateException("client must be constructed with client ID and password grant secret");

        final List<NameValuePair> params = getAuthenticationParams(username, password, scope);
        final AccessTokenResponseHandler tokenResponseHandler = new AccessTokenResponseHandler(this, responseHandler);
        execute(new AppDotNetOAuthRequest(tokenResponseHandler, params, "access_token"));
    }

    protected List<NameValuePair> getAuthenticationParams(String username, String password, String scope) {
        final List<NameValuePair> params = new ArrayList<NameValuePair>(authParams.size() + 3);
        params.addAll(authParams);
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        if (scope != null)
            params.add(new BasicNameValuePair("scope", scope));

        return params;
    }

    /*
     * USER
     */

    public void retrieveUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userId));
    }

    public void retrieveUser(String userId, UserResponseHandler responseHandler) {
        retrieveUser(userId, null, responseHandler);
    }

    public void retrieveCurrentUser(QueryParameters queryParameters, UserResponseHandler responseHandler) {
        retrieveUser("me", queryParameters, responseHandler);
    }

    public void retrieveCurrentUser(UserResponseHandler responseHandler) {
        retrieveCurrentUser(null, responseHandler);
    }

    public void updateCurrentUser(User user, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, METHOD_PUT, user, queryParameters, ENDPOINT_USERS, "me"));
    }

    public void updateCurrentUser(User user, UserResponseHandler responseHandler) {
        updateCurrentUser(user, null, responseHandler);
    }

    public void updateAvatar(byte[] image, int offset, int count, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiImageUploadRequest(responseHandler, "avatar", image, offset, count, queryParameters,
                ENDPOINT_USERS, "me/avatar"));
    }

    public void updateAvatar(byte[] image, int offset, int count, UserResponseHandler responseHandler) {
        updateAvatar(image, offset, count, null, responseHandler);
    }

    public void updateAvatar(byte[] image, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        updateAvatar(image, 0, image.length, queryParameters, responseHandler);
    }

    public void updateAvatar(byte[] image, UserResponseHandler responseHandler) {
        updateAvatar(image, null, responseHandler);
    }

    public void updateCover(byte[] image, int offset, int count, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiImageUploadRequest(responseHandler, "cover", image, offset, count, queryParameters,
                ENDPOINT_USERS, "me/cover"));
    }

    public void updateCover(byte[] image, int offset, int count, UserResponseHandler responseHandler) {
        updateCover(image, offset, count, null, responseHandler);
    }

    public void updateCover(byte[] image, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        updateCover(image, 0, image.length, queryParameters, responseHandler);
    }

    public void updateCover(byte[] image, UserResponseHandler responseHandler) {
        updateCover(image, null, responseHandler);
    }

    public void followUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_USERS, userId, "follow"));
    }

    public void followUser(String userId, UserResponseHandler responseHandler) {
        followUser(userId, null, responseHandler);
    }

    public void unfollowUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_USERS, userId, "follow"));
    }

    public void unfollowUser(String userId, UserResponseHandler responseHandler) {
        unfollowUser(userId, null, responseHandler);
    }

    public void muteUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_USERS, userId, "mute"));
    }

    public void muteUser(String userId, UserResponseHandler responseHandler) {
        muteUser(userId, null, responseHandler);
    }

    public void unmuteUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_USERS, userId, "mute"));
    }

    public void unmuteUser(String userId, UserResponseHandler responseHandler) {
        unmuteUser(userId, null, responseHandler);
    }

    public void blockUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_USERS, userId, "block"));
    }

    public void blockUser(String userId, UserResponseHandler responseHandler) {
        blockUser(userId, null, responseHandler);
    }

    public void unblockUser(String userId, QueryParameters queryParameters, UserResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_USERS, userId, "block"));
    }

    public void unblockUser(String userId, UserResponseHandler responseHandler) {
        unblockUser(userId, null, responseHandler);
    }

    protected void retrieveUsers(String userIds, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        if (queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("ids", userIds);
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS));
    }

    public void retrieveUsersById(List<String> userIds, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveUsers(getIdString(userIds), queryParameters, responseHandler);
    }

    public void retrieveUsersById(List<String> userIds, UserListResponseHandler responseHandler) {
        retrieveUsersById(userIds, null, responseHandler);
    }

    public void retrieveUsers(List<User> users, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveUsers(getObjectIdString(users), queryParameters, responseHandler);
    }

    public void retrieveUsers(List<User> users, UserListResponseHandler responseHandler) {
        retrieveUsers(users, null, responseHandler);
    }

    public void retrieveUsersWithSearchQuery(String query, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        if (queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("q", query);
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, "search"));
    }

    public void retrieveUsersWithSearchQuery(String query, UserListResponseHandler responseHandler) {
        retrieveUsersWithSearchQuery(query, null, responseHandler);
    }

    public void retrieveFollowedUsers(User user, UserListResponseHandler responseHandler) {
        retrieveFollowedUsers(user.getId(), null, responseHandler);
    }

    public void retrieveFollowedUsers(String userId, UserListResponseHandler responseHandler) {
        retrieveFollowedUsers(userId, null, responseHandler);
    }

    public void retrieveFollowedUsers(User user, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveFollowedUsers(user.getId(), queryParameters, responseHandler);
    }

    public void retrieveFollowedUsers(String userId, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userId, "following"));
    }

    public void retrieveUserFollowers(User user, UserListResponseHandler responseHandler) {
        retrieveUserFollowers(user.getId(), null, responseHandler);
    }

    public void retrieveUserFollowers(String userId, UserListResponseHandler responseHandler) {
        retrieveUserFollowers(userId, null, responseHandler);
    }

    public void retrieveUserFollowers(User user, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveUserFollowers(user.getId(), queryParameters, responseHandler);
    }

    public void retrieveUserFollowers(String userId, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userId, "followers"));
    }

    public void retrieveIdsOfFollowedUsers(User user, IdListResponseHandler responseHandler) {
        retrieveIdsOfFollowedUsers(user.getId(), responseHandler);
    }

    public void retrieveIdsOfFollowedUsers(String userId, IdListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, ENDPOINT_USERS, userId, "following", "ids"));
    }

    public void retrieveIdsOfFollowers(User user, IdListResponseHandler responseHandler) {
        retrieveIdsOfFollowers(user.getId(), responseHandler);
    }

    public void retrieveIdsOfFollowers(String userId, IdListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, ENDPOINT_USERS, userId, "followers", "ids"));
    }

    public void retrieveMutedUsers(User user, UserListResponseHandler responseHandler) {
        retrieveMutedUsers(user.getId(), null, responseHandler);
    }

    public void retrieveMutedUsers(String userId, UserListResponseHandler responseHandler) {
        retrieveMutedUsers(userId, null, responseHandler);
    }

    public void retrieveMutedUsers(User user, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveMutedUsers(user.getId(), queryParameters, responseHandler);
    }

    public void retrieveMutedUsers(String userId, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userId, "muted"));
    }

    public void retrieveBlockedUsers(User user, UserListResponseHandler responseHandler) {
        retrieveBlockedUsers(user.getId(), null, responseHandler);
    }

    public void retrieveBlockedUsers(String userId, UserListResponseHandler responseHandler) {
        retrieveBlockedUsers(userId, null, responseHandler);
    }

    public void retrieveBlockedUsers(User user, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveBlockedUsers(user.getId(), queryParameters, responseHandler);
    }

    public void retrieveBlockedUsers(String userId, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userId, "blocked"));
    }

    public void retrieveReposters(Post post, UserListResponseHandler responseHandler) {
        retrieveReposters(post.getId(), null, responseHandler);
    }

    public void retrieveReposters(String postId, UserListResponseHandler responseHandler) {
        retrieveReposters(postId, null, responseHandler);
    }

    public void retrieveReposters(Post post, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveReposters(post.getId(), queryParameters, responseHandler);
    }

    public void retrieveReposters(String postId, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, postId, "reposters"));
    }

    public void retrieveStarrers(Post post, UserListResponseHandler responseHandler) {
        retrieveStarrers(post.getId(), null, responseHandler);
    }

    public void retrieveStarrers(String postId, UserListResponseHandler responseHandler) {
        retrieveStarrers(postId, null, responseHandler);
    }

    public void retrieveStarrers(Post post, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        retrieveStarrers(post.getId(), queryParameters, responseHandler);
    }

    public void retrieveStarrers(String postId, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, postId, "stars"));
    }

    /*
     * INTERACTIONS
     */

    public void retrieveCurrentUserInteractions(InteractionListResponseHandler responseHandler) {
        retrieveCurrentUserInteractions(null, responseHandler);
    }

    public void retrieveCurrentUserInteractions(QueryParameters queryParameters, InteractionListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, "me", ENDPOINT_INTERACTIONS));
    }

    /*
     * POST - CREATION
     */

    public void createPost(Post post, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, post, queryParameters, ENDPOINT_POSTS));
    }

    public void createPost(Post post, PostResponseHandler responseHandler) {
        createPost(post, null, responseHandler);
    }

    /*
     * POST - RETRIEVAL BY ID
     */

    public void retrievePost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, postId));
    }

    public void retrievePost(String postId, PostResponseHandler responseHandler) {
        retrievePost(postId, null, responseHandler);
    }

    public void retrievePosts(List<Post> posts, PostListResponseHandler responseHandler) {
        retrievePosts(posts, null, responseHandler);
    }

    public void retrievePosts(List<Post> posts, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePosts(getObjectIdString(posts), queryParameters, responseHandler);
    }

    public void retrievePostsById(List<String> postIds, PostListResponseHandler responseHandler) {
        retrievePostsById(postIds, null, responseHandler);
    }

    public void retrievePostsById(List<String> postIds, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePosts(getIdString(postIds), queryParameters, responseHandler);
    }

    protected void retrievePosts(String postIds, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        if(queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("ids", postIds);
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS));
    }

    /*
     * POST - RETRIEVAL BY USER
     */

    public void retrievePostsForUser(User user, PostListResponseHandler responseHandler) {
        retrievePostsForUser(user.getId(), null, responseHandler);
    }

    public void retrievePostsForUser(User user, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsForUser(user.getId(), queryParameters, responseHandler);
    }

    public void retrievePostsForUserId(String userId, PostListResponseHandler responseHandler) {
        retrievePostsForUser(userId, null, responseHandler);
    }

    public void retrievePostsForUserId(String userId, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsForUser(userId, queryParameters, responseHandler);
    }

    public void retrievePostsForCurrentUser(PostListResponseHandler responseHandler) {
        retrievePostsForUser("me", null, responseHandler);
    }

    public void retrievePostsForCurrentUser(QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsForUser("me", queryParameters, responseHandler);
    }

    protected void retrievePostsForUser(String userString, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userString, ENDPOINT_POSTS));
    }

    /*
     * POST - RETRIEVE STARS
     */

    public void retrieveStarredPostsForUser(User user, PostListResponseHandler responseHandler) {
        retrieveStarredPostsForUser(user.getId(), null, responseHandler);
    }

    public void retrieveStarredPostsForUser(User user, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrieveStarredPostsForUser(user.getId(), queryParameters, responseHandler);
    }

    public void retrieveStarredPostsForUserId(String userId, PostListResponseHandler responseHandler) {
        retrieveStarredPostsForUser(userId, null, responseHandler);
    }

    public void retrieveStarredPostsForUserId(String userId, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrieveStarredPostsForUser(userId, queryParameters, responseHandler);
    }

    public void retrieveStarredPostsForCurrentUser(PostListResponseHandler responseHandler) {
        retrieveStarredPostsForUser("me", null, responseHandler);
    }

    public void retrieveStarredPostsForCurrentUser(QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrieveStarredPostsForUser("me", queryParameters, responseHandler);
    }

    protected void retrieveStarredPostsForUser(String userString, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userString, ENDPOINT_STARS));
    }

    /*
     * POST - RETRIEVE MENTIONS
     */

    public void retrievePostsMentioningUser(User user, PostListResponseHandler responseHandler) {
        retrievePostsMentioningUser(user.getId(), null, responseHandler);
    }

    public void retrievePostsMentioningUser(User user, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsMentioningUser(user.getId(), queryParameters, responseHandler);
    }

    public void retrievePostsMentioningUserWithId(String userId, PostListResponseHandler responseHandler) {
        retrievePostsMentioningUser(userId, null, responseHandler);
    }

    public void retrievePostsMentioningUserWithId(String userId, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsMentioningUser(userId, queryParameters, responseHandler);
    }

    public void retrievePostsMentioningCurrentUser(PostListResponseHandler responseHandler) {
        retrievePostsMentioningUser("me", null, responseHandler);
    }

    public void retrievePostsMentioningCurrentUser(QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsMentioningUser("me", queryParameters, responseHandler);
    }

    protected void retrievePostsMentioningUser(String userString, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, userString, ENDPOINT_MENTIONS));
    }

    /*
     * POST - RETRIEVE BY HASHTAG
     */

    public void retrievePostsWithHashtag(Entities.Hashtag hashtag, PostListResponseHandler responseHandler) {
        retrievePostsWithHashtag(hashtag.getName(), null, responseHandler);
    }

    public void retrievePostsWithHashtag(Entities.Hashtag hashtag, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrievePostsWithHashtag(hashtag.getName(), queryParameters, responseHandler);
    }

    public void retrievePostsWithHashtag(String hashtag, PostListResponseHandler responseHandler) {
        retrievePostsWithHashtag(hashtag, null, responseHandler);
    }

    public void retrievePostsWithHashtag(String hashtag, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, "tag", hashtag));
    }

    /*
     * POST - RETRIEVE REPLIES
     */

    public void retrieveRepliesToPost(Post post, PostListResponseHandler responseHandler) {
        retrieveRepliesToPost(post.getId(), null, responseHandler);
    }

    public void retrieveRepliesToPost(Post post, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        retrieveRepliesToPost(post.getId(), queryParameters, responseHandler);
    }

    public void retrieveRepliesToPost(String postId, PostListResponseHandler responseHandler) {
        retrieveRepliesToPost(postId, null, responseHandler);
    }

    public void retrieveRepliesToPost(String postId, QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, postId, "replies"));
    }

    public void deletePost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_POSTS, postId));
    }

    /*
     * POST - DELETION
     */

    public void deletePost(String postId, PostResponseHandler responseHandler) {
        deletePost(postId, null, responseHandler);
    }

    /*
     * POST - REPOST
     */

    public void repostPost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_POSTS, postId, "repost"));
    }

    public void repostPost(String postId, PostResponseHandler responseHandler) {
        repostPost(postId, null, responseHandler);
    }

    public void unrepostPost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_POSTS, postId, "repost"));
    }

    public void unrepostPost(String postId, PostResponseHandler responseHandler) {
        unrepostPost(postId, null, responseHandler);
    }

    /*
     * POST - STAR
     */

    public void starPost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_POSTS, postId, "star"));
    }

    public void starPost(String postId, PostResponseHandler responseHandler) {
        starPost(postId, null, responseHandler);
    }

    public void unstarPost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_POSTS, postId, "star"));
    }

    public void unstarPost(String postId, PostResponseHandler responseHandler) {
        unstarPost(postId, null, responseHandler);
    }

    /*
     * POST - PERSONALIZED STREAM
     */

    public void retrievePersonalizedStream(PostListResponseHandler responseHandler) {
        retrievePersonalizedStream(null, responseHandler);
    }

    public void retrievePersonalizedStream(QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, "stream"));
    }

    /*
     * POST - UNIFIED STREAM
     */

    public void retrieveUnifiedStream(PostListResponseHandler responseHandler) {
        retrieveUnifiedStream(null, responseHandler);
    }

    public void retrieveUnifiedStream(QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, "stream", "unified"));
    }

    /*
     * POST - GLOBAL STREAM
     */

    public void retrieveGlobalStream(PostListResponseHandler responseHandler) {
        retrieveGlobalStream(null, responseHandler);
    }

    public void retrieveGlobalStream(QueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, "stream", "global"));
    }

    /*
     * POST - REPORT
     */
    public void reportPost(Post post, PostResponseHandler responseHandler) {
        reportPost(post.getId(), null, responseHandler);
    }

    public void reportPost(Post post, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        reportPost(post.getId(), queryParameters, responseHandler);
    }

    public void reportPost(String postId, PostResponseHandler responseHandler) {
        reportPost(postId, null, responseHandler);
    }

    public void reportPost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_POSTS, postId, "report"));
    }

    /*
     * POST - SEARCH
     */

    public void retrievePostsWithSearchQuery(PostSearchQueryParameters queryParameters, PostListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, "search"));
    }

    /*
     * CHANNEL
     */

    public void retrieveCurrentUserSubscribedChannels(QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS));
    }

    public void retrieveCurrentUserSubscribedChannels(ChannelListResponseHandler responseHandler) {
        retrieveCurrentUserSubscribedChannels(null, responseHandler);
    }

    public void createChannel(Channel channel, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, channel, queryParameters, ENDPOINT_CHANNELS));
    }

    public void createChannel(Channel channel, ChannelResponseHandler responseHandler) {
        createChannel(channel, null, responseHandler);
    }

    public void retrieveChannel(String channelId, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS, channelId));
    }

    public void retrieveChannel(String channelId, ChannelResponseHandler responseHandler) {
        retrieveChannel(channelId, null, responseHandler);
    }

    protected void retrieveChannels(String channelIds, QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        if (queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("ids", channelIds);
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS));
    }

    public void retrieveChannelsById(List<String> channelIds, QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        retrieveChannels(getIdString(channelIds), queryParameters, responseHandler);
    }

    public void retrieveChannelsById(List<String> channelIds, ChannelListResponseHandler responseHandler) {
        retrieveChannelsById(channelIds, null, responseHandler);
    }

    public void retrieveChannels(List<Channel> channels, QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        retrieveChannels(getObjectIdString(channels), queryParameters, responseHandler);
    }

    public void retrieveChannels(List<Channel> channels, ChannelListResponseHandler responseHandler) {
        retrieveChannels(channels, null, responseHandler);
    }

    public void retrieveChannels(QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS, "search"));
    }

    public void retrieveChannelsWithSearchQuery(String query, ChannelListResponseHandler responseHandler) {
        retrieveChannelsWithSearchQuery(query, null, responseHandler);
    }

    public void retrieveChannelsWithSearchQuery(String query, QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        if (queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("q", query);
        retrieveChannels(queryParameters, responseHandler);
    }

    public void retrieveCurrentUserChannels(QueryParameters queryParameters, ChannelListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, "me", ENDPOINT_CHANNELS));
    }

    public void retrieveCurrentUserChannels(ChannelListResponseHandler responseHandler) {
        retrieveCurrentUserChannels(null, responseHandler);
    }

    public void retrieveUnreadPrivateMessageChannelCount(CountResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, ENDPOINT_USERS, "me", ENDPOINT_CHANNELS, "pm/num_unread"));
    }

    public void updateChannel(Channel channel, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, METHOD_PUT, channel, queryParameters, ENDPOINT_CHANNELS, channel.getId()));
    }

    public void updateChannel(Channel channel, ChannelResponseHandler responseHandler) {
        updateChannel(channel, null, responseHandler);
    }

    public void updateChannel(String channelId, Map<String, Object> body, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, METHOD_PUT, body, queryParameters, ENDPOINT_CHANNELS, channelId));
    }

    public void subscribeChannel(String channelId, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_CHANNELS, channelId, "subscribe"));
    }

    public void subscribeChannel(String channelId, ChannelResponseHandler responseHandler) {
        subscribeChannel(channelId, null, responseHandler);
    }

    public void subscribeChannel(Channel channel, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        subscribeChannel(channel.getId(), queryParameters, responseHandler);
    }

    public void subscribeChannel(Channel channel, ChannelResponseHandler responseHandler) {
        subscribeChannel(channel, null, responseHandler);
    }

    public void unsubscribeChannel(String channelId, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_CHANNELS, channelId, "subscribe"));
    }

    public void unsubscribeChannel(String channelId, ChannelResponseHandler responseHandler) {
        unsubscribeChannel(channelId, null, responseHandler);
    }

    public void unsubscribeChannel(Channel channel, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        unsubscribeChannel(channel.getId(), queryParameters, responseHandler);
    }

    public void unsubscribeChannel(Channel channel, ChannelResponseHandler responseHandler) {
        unsubscribeChannel(channel, null, responseHandler);
    }

    public void muteChannel(String channelId, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_POST, queryParameters, ENDPOINT_CHANNELS, channelId, "mute"));
    }

    public void muteChannel(String channelId, ChannelResponseHandler responseHandler) {
        muteChannel(channelId, null, responseHandler);
    }

    public void muteChannel(Channel channel, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        muteChannel(channel.getId(), queryParameters, responseHandler);
    }

    public void muteChannel(Channel channel, ChannelResponseHandler responseHandler) {
        muteChannel(channel, null, responseHandler);
    }

    public void unmuteChannel(String channelId, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_CHANNELS, channelId, "mute"));
    }

    public void unmuteChannel(String channelId, ChannelResponseHandler responseHandler) {
        unmuteChannel(channelId, null, responseHandler);
    }

    public void unmuteChannel(Channel channel, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        unmuteChannel(channel.getId(), queryParameters, responseHandler);
    }

    public void unmuteChannel(Channel channel, ChannelResponseHandler responseHandler) {
        unmuteChannel(channel, null, responseHandler);
    }

    public void deactivateChannel(String channelId, ChannelResponseHandler responseHandler) {
        deactivateChannel(channelId, null, responseHandler);
    }

    public void deactivateChannel(String channelId, QueryParameters queryParameters, ChannelResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_CHANNELS, channelId));
    }

    /*
     * MESSAGE
     */

    public void retrieveMessagesInChannel(String channelId, QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS, channelId, ENDPOINT_MESSAGES));
    }

    public void retrieveMessagesInChannel(String channelId, MessageListResponseHandler responseHandler) {
        retrieveMessagesInChannel(channelId, null, responseHandler);
    }

    public void retrieveMessagesInChannel(Channel channel, QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
        retrieveMessagesInChannel(channel.getId(), queryParameters, responseHandler);
    }

    public void retrieveMessagesInChannel(Channel channel, MessageListResponseHandler responseHandler) {
        retrieveMessagesInChannel(channel, null, responseHandler);
    }

    public void createMessage(String channelId, Message message, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, message, queryParameters, ENDPOINT_CHANNELS, channelId, ENDPOINT_MESSAGES));
    }

    public void createMessage(String channelId, Message message, MessageResponseHandler responseHandler) {
        createMessage(channelId, message, null, responseHandler);
    }

    public void createMessage(Channel channel, Message message, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        createMessage(channel.getId(), message, queryParameters, responseHandler);
    }

    public void createMessage(Channel channel, Message message, MessageResponseHandler responseHandler) {
        createMessage(channel, message, null, responseHandler);
    }

    public void createPrivateMessage(PrivateMessage message, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        if (message.getDestinations() == null)
            throw new IllegalArgumentException("private message must specify destinations");

        createMessage("pm", message, queryParameters, responseHandler);
    }

    public void createPrivateMessage(PrivateMessage message, MessageResponseHandler responseHandler) {
        createPrivateMessage(message, null, responseHandler);
    }

    public void retrieveMessage(String channelId, String messageId, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS, channelId, ENDPOINT_MESSAGES, messageId));
    }

    public void retrieveMessage(String channelId, String messageId, MessageResponseHandler responseHandler) {
        retrieveMessage(channelId, messageId, null, responseHandler);
    }

    public void retrieveMessage(Channel channel, String messageId, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        retrieveMessage(channel.getId(), messageId, queryParameters, responseHandler);
    }

    public void retrieveMessage(Channel channel, String messageId, MessageResponseHandler responseHandler) {
        retrieveMessage(channel, messageId, null, responseHandler);
    }

    protected void retrieveMessages(String messageIds, QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
        if (queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("ids", messageIds);
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_CHANNELS, ENDPOINT_MESSAGES));
    }

    public void retrieveMessagesById(Collection<String> messageIds, QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
        retrieveMessages(getIdString(messageIds), queryParameters, responseHandler);
    }

    public void retrieveMessagesById(List<String> messageIds, MessageListResponseHandler responseHandler) {
        retrieveMessagesById(messageIds, null, responseHandler);
    }

    public void retrieveMessages(List<Message> messages, QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
        retrieveMessages(getObjectIdString(messages), queryParameters, responseHandler);
    }

    public void retrieveMessages(List<Message> messages, MessageListResponseHandler responseHandler) {
        retrieveMessages(messages, null, responseHandler);
    }

    public void retrieveCurrentUserMessages(QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, "me", ENDPOINT_MESSAGES));
    }

    public void retrieveCurrentUserMessages(MessageListResponseHandler responseHandler) {
        retrieveCurrentUserMessages(null, responseHandler);
    }

    public void deleteMessage(String channelId, String messageId, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_CHANNELS, channelId, ENDPOINT_MESSAGES, messageId));
    }

    public void deleteMessage(String channelId, String messageId, MessageResponseHandler responseHandler) {
        deleteMessage(channelId, messageId, null, responseHandler);
    }

    public void deleteMessage(Message message, QueryParameters queryParameters, MessageResponseHandler responseHandler) {
        deleteMessage(message.getChannelId(), message.getId(), queryParameters, responseHandler);
    }

    public void deleteMessage(Message message, MessageResponseHandler responseHandler) {
        deleteMessage(message, null, responseHandler);
    }

    /*
     * PLACE
     */

    public void retrievePlace(String factualId, PlaceResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, ENDPOINT_PLACES, factualId));
    }

    public void retrievePlacesWithSearchQuery(PlaceSearchQueryParameters queryParameters, PlaceListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_PLACES, "search"));
    }

    /*
     * STREAM MARKER
     */

    public void updateStreamMarker(StreamMarker streamMarker, StreamMarkerResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, streamMarker, null, ENDPOINT_POSTS, "marker"));
    }

    public void updateStreamMarkers(StreamMarkerList streamMarkers, StreamMarkerListResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, streamMarkers, null, ENDPOINT_POSTS, "marker"));
    }

    /*
     * FILE
     */
    public void retrieveCurrentUserFiles(FileListResponseHandler responseHandler) {
        retrieveCurrentUserFiles(null, responseHandler);
    }

    public void retrieveCurrentUserFiles(QueryParameters queryParameters, FileListResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_USERS, "me", ENDPOINT_FILES));
    }

    public void retrieveFiles(List<File> files, FileListResponseHandler responseHandler) {
        retrieveFiles(files, null, responseHandler);
    }

    public void retrieveFiles(List<File> files, QueryParameters queryParameters, FileListResponseHandler responseHandler) {
        retrieveFiles(getObjectIdString(files), queryParameters, responseHandler);
    }

    public void retrieveFilesById(List<String> fileIds, FileListResponseHandler responseHandler) {
        retrieveFilesById(fileIds, null, responseHandler);
    }

    public void retrieveFilesById(List<String> fileIds, QueryParameters queryParameters, FileListResponseHandler responseHandler) {
        retrieveFiles(getIdString(fileIds), queryParameters, responseHandler);
    }

    protected void retrieveFiles(String fileIds, QueryParameters queryParameters, FileListResponseHandler responseHandler) {
        if (queryParameters == null)
            queryParameters = new QueryParameters();

        queryParameters.put("ids", fileIds);
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_FILES));
    }

    public void createFile(File file, byte[] fileData, String mimeType, FileResponseHandler responseHandler) {
        execute(new AppDotNetApiFileUploadRequest(responseHandler, file, fileData, mimeType, ENDPOINT_FILES));
    }

    public void deleteFile(String fileId, FileResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, null, ENDPOINT_FILES, fileId));
    }

    /*
     * TOKEN
     */

    public void retrieveCurrentToken(TokenResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, "token"));
    }

    /*
     * CONFIGURATION
     */

    public void retrieveConfiguration(ConfigurationResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, ENDPOINT_CONFIGURATION));
    }

    /*
     * MISC
     */

    protected String getIdString(Collection<String> ids) {
        final StringBuilder buffer = new StringBuilder(ids.size() * ID_LENGTH);
        for (final String id : ids) {
            buffer.append(id);
            buffer.append(',');
        }

        return buffer.substring(0, buffer.length() - 1);
    }

    protected String getObjectIdString(List<? extends Annotatable> objects) {
        final ArrayList<String> ids = new ArrayList<String>(objects.size());
        for (final Annotatable object : objects) {
            ids.add(object.getId());
        }

        return getIdString(ids);
    }

    protected void execute(AppDotNetRequest request) {
        if (request.isAuthenticated() && !hasToken()) {
            throw new IllegalStateException("authentication token not set");
        }

        final AppDotNetClientTask task = new AppDotNetClientTask(context, authHeader, languageHeader, sslSocketFactory);

        // AsyncTask was changed in Honeycomb to execute in serial by default, at which time
        // executeOnExecutor was added to specify parallel execution.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
        } else {
            task.execute(request);
        }
    }
}
