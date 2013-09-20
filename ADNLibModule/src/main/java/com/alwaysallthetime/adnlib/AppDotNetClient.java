package com.alwaysallthetime.adnlib;

import android.os.AsyncTask;
import android.os.Build;

import com.alwaysallthetime.adnlib.data.Annotatable;
import com.alwaysallthetime.adnlib.data.Channel;
import com.alwaysallthetime.adnlib.data.Message;
import com.alwaysallthetime.adnlib.data.Post;
import com.alwaysallthetime.adnlib.data.PrivateMessage;
import com.alwaysallthetime.adnlib.data.User;
import com.alwaysallthetime.adnlib.request.AppDotNetApiImageUploadRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiJsonRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetApiRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetOAuthRequest;
import com.alwaysallthetime.adnlib.request.AppDotNetRequest;
import com.alwaysallthetime.adnlib.response.AccessTokenResponseHandler;
import com.alwaysallthetime.adnlib.response.ChannelListResponseHandler;
import com.alwaysallthetime.adnlib.response.ChannelResponseHandler;
import com.alwaysallthetime.adnlib.response.CountResponseHandler;
import com.alwaysallthetime.adnlib.response.LoginResponseHandler;
import com.alwaysallthetime.adnlib.response.MessageListResponseHandler;
import com.alwaysallthetime.adnlib.response.MessageResponseHandler;
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

    protected static final int ID_LENGTH = 10; // max string length of object ID including delimiter
    protected static final String ENDPOINT_USERS = "users";
    protected static final String ENDPOINT_POSTS = "posts";
    protected static final String ENDPOINT_CHANNELS = "channels";
    protected static final String ENDPOINT_MESSAGES = "messages";

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

        queryParameters.put("text", query);
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

    public void retrieveMessagesById(List<String> messageIds, QueryParameters queryParameters, MessageListResponseHandler responseHandler) {
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
     * TOKEN
     */

    public void retrieveCurrentToken(TokenResponseHandler responseHandler) {
        execute(new AppDotNetApiRequest(responseHandler, null, "token"));
    }


    /*
     * MISC
     */

    protected String getIdString(List<String> ids) {
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