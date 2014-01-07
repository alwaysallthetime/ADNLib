package com.alwaysallthetime.adnlib.resourceStream;

import android.content.SharedPreferences;

import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObject;
import com.alwaysallthetime.adnlib.gson.AppDotNetGson;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ResourceStreamSerializer {
    public static void serialize(SharedPreferences preferences, String key, ResourceStream resourceStream) {
        String resourceStreamJson = AppDotNetGson.getInstance().toJson(resourceStream);
        String objectsJson = AppDotNetGson.getPersistenceInstance().toJson(resourceStream.getObjects());
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, resourceStreamJson);
        edit.putString(key + "_objects", objectsJson);
        edit.commit();
    }

    public static void remove(SharedPreferences preferences, String key) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(key);
        edit.remove(key + "_objects");
        edit.commit();
    }

    public static ResourceStream deserializeStream(SharedPreferences preferences, String key, Class resourceStreamType, Class objectType) {
        String json = preferences.getString(key, null);
        if(json != null) {
            ResourceStream stream = (ResourceStream) AppDotNetGson.getInstance().fromJson(json, resourceStreamType);

            Gson gson = AppDotNetGson.getPersistenceInstance();
            String objectsJson = preferences.getString(key + "_objects", null);
            ArrayList<IPageableAppDotNetObject> objects = (ArrayList<IPageableAppDotNetObject>) gson.fromJson(objectsJson, objectType);
            if(objects != null) {
                stream.setObjects(objects);
            }
            return stream;
        }
        return null;
    }
}
