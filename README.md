ADNLib
======
App.net library for Android


### Status
ADNLib is a work in progress. Currently it only supports a basic set of User and Post actions. It also supports password flow authentication, or you can retrieve an access token on your own and pass it to the client. ADNLib is set up to be used with Android Studio.

### Getting Started
*ADNLib is a standalone Android Studio project, but here we describe how to add it as a module to your existing project. These instructions were written for Android Studio 0.2.6. It is still a "Preview" that often works in mysterious ways. For this reason, we do things a little funky. If you are an Android Studio pro, then feel free to set up the project however you'd prefer. Please note that these instructions may become stale as Android Studio updates are released.*

1. Clone ADNLib into the root of your Android Studio project. 
2. Close your project.
3. Open the .idea/modules.xml file for your project and add the following lines to your ``<modules>``:
    
    ```
    <module fileurl="file://$PROJECT_DIR$/ADNLib/ADNLib.iml" 
            filepath="$PROJECT_DIR$/ADNLib/ADNLib.iml" />
    <module fileurl="file://$PROJECT_DIR$/ADNLib/ADNLibModule/ADNLibModule.iml"
            filepath="$PROJECT_DIR$/ADNLib/ADNLibModule/ADNLibModule.iml" />
    ```
4. Reopen your Android Studio project. You will notice that ADNLib and ADNLibModule are now recognized as modules (bold in the Project View)
5. File > Project Structure
6. In the Modules section, select your project's main module, open the "Dependencies" tab, click the "+" button and add a Module Dependency on ADNLibModule.
7. Open your project's top-level settings.gradle file and add an include for ADNLibModule, e.g.
`` include ':YourMainModule', ':ADNLib:ADNLibModule' ``
8. Open the build.gradle *within* your main module (not the top-level build.gradle). In the dependencies block, add the following line:
`` compile project(':ADNLib:ADNLibModule') ``
9. You should be set. In your main Activity try importing and constructing a new AppDotNetClient(), e.g.

```java
import android.app.Activity;
import android.os.Bundle;

import com.alwaysallthetime.adnlib.AppDotNetClient;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        AppDotNetClient client = new AppDotNetClient();
    }
}
```

#### Troubleshooting
* You might need to copy the local.properties from the root of the project to the ADNLibModule folder. On OS X, this file typically contains: ``sdk.dir=/Applications/Android Studio.app/sdk``
* If it still doesn't compile and run, close Android Studio, blow in the end of the cartridge, and restart.

### Overview
The main class is `AppDotNetClient`. ADNLib uses Gson to serialize from and deserialize to a set of first-class data objects that live in the `data` package (e.g. `User` and `Post`). Because ADNLib uses Android's AsyncTask for multithreaded network access, client requests require a response handler, which live in the `response` package (e.g. `LoginResponseHandler` and `PostResponseHandler`). On each response handler you must override `onSuccess`, and you may optionally override `onError`.

### Initialization
#### With existing access token
```java
AppDotNetClient client = new AppDotNetClient(accessToken);
```
or
```java
AppDotNetClient client = new AppDotNetClient();
...
client.setToken(accessToken);
```

#### Using password flow authentication
```java
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
```java
client.retrieveCurrentUser(new UserResponseHandler() {
    @Override
    public void onSuccess(User user) {

    }
});
```

#### Star a post
```java
client.starPost(postId, new PostResponseHandler() {
    @Override
    public void onSuccess(Post post) {
        
    }
});
```

#### Create a new post
```java
Post post = new Post("test post");
client.createPost(post, new PostResponseHandler() {
    @Override
    public void onSuccess(Post responsePost) {
        
    }
});
```

#### Follow a user
```java
client.followUser(userId, new UserResponseHandler() {
    @Override
    public void onSuccess(User user) {
        
    }
});
```

### Query parameters
Each client method has a variation that takes a `QueryParameters` instance. The general endpoint parameters are pre-defined on the `GeneralParameter` enum.
#### Example
```java
client.retrieveUser(userId, new QueryParameters(GeneralParameter.INCLUDE_ANNOTATIONS), new UserResponseHandler() {
    @Override
    public void onSuccess(User user) {
        
    }
});
```

### TODO
- documentation
- remainder of User endpoints.
- Creating Derived Files
- WebView component for client-side authentication flow
- pagination
- streaming API
- filters
- unit tests
