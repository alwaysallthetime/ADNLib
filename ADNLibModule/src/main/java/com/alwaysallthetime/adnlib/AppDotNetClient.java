package com.alwaysallthetime.adnlib;

import android.os.AsyncTask;
import android.os.Build;

import com.alwaysallthetime.adnlib.data.Post;
import com.alwaysallthetime.adnlib.data.User;
import com.alwaysallthetime.adnlib.request.AppDotNetApiImageUploadRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiJsonRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetOAuthRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetRequest;
import com.alwaysallthetime.adnlib.response.AccessTokenResponseHandler;
import com.alwaysallthetime.adnlib.response.LoginResponseHandler;
import com.alwaysallthetime.adnlib.response.PostResponseHandler;
import com.alwaysallthetime.adnlib.response.TokenResponseHandler;
import com.alwaysallthetime.adnlib.response.UserListResponseHandler;
import com.alwaysallthetime.adnlib.response.UserResponseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLSocketFactory;

public class AppDotNetClient {
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";

    protected static final int ID_LENGTH = 10; // max string length of user ID including delimiter
    protected static final String ENDPOINT_USERS = "users";
    protected static final String ENDPOINT_POSTS = "posts";

    protected String authHeader;
    protected String languageHeader;
    protected List<NameValuePair> authParams;
    protected SSLSocketFactory sslSocketFactory;

    public AppDotNetClient() {
        final Locale locale = Locale.getDefault();
        languageHeader = String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }

    public AppDotNetClient(String token) {
        this();
        setToken(token);
    }

    public AppDotNetClient(String clientId, String passwordGrantSecret) {
        this();
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
        final StringBuilder buffer = new StringBuilder(userIds.size() * ID_LENGTH);
        for (final String userId : userIds) {
            buffer.append(userId);
            buffer.append(',');
        }
        final String userIdsString = buffer.substring(0, buffer.length() - 1);
        retrieveUsers(userIdsString, queryParameters, responseHandler);
    }

    public void retrieveUsersById(List<String> userIds, UserListResponseHandler responseHandler) {
        retrieveUsersById(userIds, null, responseHandler);
    }

    public void retrieveUsers(List<User> users, QueryParameters queryParameters, UserListResponseHandler responseHandler) {
        final ArrayList<String> userIds = new ArrayList<String>(users.size());
        for (final User user : users) {
            userIds.add(user.getId());
        }
        retrieveUsersById(userIds, queryParameters, responseHandler);
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

    /*
     * POST
     */

    public void createPost(Post post, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiJsonRequest(responseHandler, post, queryParameters, ENDPOINT_POSTS));
    }

    public void createPost(Post post, PostResponseHandler responseHandler) {
        createPost(post, null, responseHandler);
    }

    public void retrievePost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, queryParameters, ENDPOINT_POSTS, postId));
    }

    public void retrievePost(String postId, PostResponseHandler responseHandler) {
        retrievePost(postId, null, responseHandler);
    }

    public void deletePost(String postId, QueryParameters queryParameters, PostResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, METHOD_DELETE, queryParameters, ENDPOINT_POSTS, postId));
    }

    public void deletePost(String postId, PostResponseHandler responseHandler) {
        deletePost(postId, null, responseHandler);
    }

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
     * TOKEN
     */

    public void retrieveCurrentToken(TokenResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, "token"));
    }


    protected void execute(AppDotNetRequest request) {
        if (request.isAuthenticated() && !hasToken()) {
            throw new IllegalStateException("authentication token not set");
        }

        final AppDotNetClientTask task = new AppDotNetClientTask(authHeader, languageHeader, sslSocketFactory);

        // AsyncTask was changed in Honeycomb to execute in serial by default, at which time
        // executeOnExecutor was added to specify parallel execution.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
        } else {
            task.execute(request);
        }
    }
}