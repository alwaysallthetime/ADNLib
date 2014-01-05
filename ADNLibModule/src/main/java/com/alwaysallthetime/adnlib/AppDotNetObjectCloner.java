package com.alwaysallthetime.adnlib;

import com.alwaysallthetime.adnlib.data.IAppDotNetObject;
import com.alwaysallthetime.adnlib.gson.AppDotNetGson;
import com.google.gson.Gson;

public class AppDotNetObjectCloner {
    public static Object getClone(IAppDotNetObject object) {
        Gson gson = AppDotNetGson.getPersistenceInstance();
        Class<?> aClass = ((Object) object).getClass();
        return gson.fromJson(gson.toJson(object), aClass);
    }
}
