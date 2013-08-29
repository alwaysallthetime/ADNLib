ADNLib
======
App.net library for Android


### Status
ADNLib is a work in progress. Currently it only supports a basic set of User and Post actions. It also supports password flow authentication, or you can retrieve an access token on your own and pass it to the client. ADNLib is set up to be used with Android Studio.

### Overview
The main class is `AppDotNetClient`. ADNLib uses Gson to serialize from and deserialize to a set of first-class data objects that live in the `data` package (e.g. `User` and `Post`). Because ADNLib uses Android's AsyncTask for multithreaded network access, client requests require a response handler, which live in the `response` package (e.g. `LoginResponseHandler` and `PostResponseHandler`). On each response handler you must override `onSuccess`, and you may optionally override `onError`.

### Initialization
#### With existing access token
```
AppDotNetClient client = new AppDotNetClient(accessToken);
```
or
```
AppDotNetClient client = new AppDotNetClient();
...
client.setToken(accessToken);
```

#### Using password flow authentication
```
AppDotNetClient client = new AppDotNetClient(clientId, passwordGrantSecret);
client.authenticateWithPassword(username, password, scope, new LoginResponseHandler() {
    @Override
    public void onSuccess(String accessToken, Token token) {
        // The access token has already been set on the client; you don't need to call setToken() here.
    }
});
```

### Basic examples
#### Get current user
```
client.retrieveCurrentUser(new UserResponseHandler() {
    @Override
    public void onSuccess(User user) {

    }
});
```

#### Star a post
```
client.starPost(postId, new PostResponseHandler() {
    @Override
    public void onSuccess(Post post) {
        
    }
});
```

#### Create a new post
```
Post post = new Post("test post");
client.createPost(post, new PostResponseHandler() {
    @Override
    public void onSuccess(Post responsePost) {
        
    }
});
```

#### Follow a user
```
client.followUser(userId, new UserResponseHandler() {
    @Override
    public void onSuccess(User user) {
        
    }
});
```

### Query parameters
Each client method has a variation that takes a `QueryParameters` instance. The general endpoint parameters are pre-defined on the `GeneralParameter` enum.
#### Example
```
client.retrieveUser(userId, new QueryParameters(GeneralParameter.INCLUDE_ANNOTATIONS), new UserResponseHandler() {
    @Override
    public void onSuccess(User user) {
        
    }
});
```

### TODO
- documentation
- remainder of User and Post endpoints
- Channels and Messages
- Files
- Places
- WebView component for client-side authentication flow
- pagination
- streaming API
- filters
- unit tests
