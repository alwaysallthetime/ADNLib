package com.alwaysallthetime.adnlib.gson;

import com.alwaysallthetime.adnlib.data.AbstractPost;
import com.alwaysallthetime.adnlib.data.Annotatable;
import com.alwaysallthetime.adnlib.data.Count;
import com.alwaysallthetime.adnlib.data.IAppDotNetObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Date;

public class AppDotNetGson {
    static Gson adapterSafeInstance;

    private static Gson instance;
    private static Gson persistenceInstance;

    static {
        final GsonBuilder builder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new Iso8601DateTypeAdapter());

        persistenceInstance = builder.create();

        adapterSafeInstance = builder
                .addSerializationExclusionStrategy(new IncludeFieldsByDefaultSerializationExclusionStrategy())
                .create();

        instance = builder
                .registerTypeHierarchyAdapter(Annotatable.class, new AnnotatableSerializer())
                .registerTypeAdapter(Count.class, new CountDeserializer())
                .create();
    }

    /**
     * A Gson instance for serializing to and deserializing from the App.net API.
     *
     * Fields that shouldn't be sent to the API are excluded from serialization, e.g. createdAt.
     *
     * @return a Gson instance
     */
    public static Gson getInstance() {
        return instance;
    }

    /**
     * A Gson instance suitable for disk persistence, in that it will serialize and deserialize all fields.
     *
     * @return a Gson instance
     */
    public static Gson getPersistenceInstance() {
        return persistenceInstance;
    }

    /**
     * A Gson instance without certain customer adapters. This allows those adapters to get the "default" serialization
     * before modifying it.
     *
     * @return a Gson instance
     */
    static Gson getAdapterSafeInstance() {
        return adapterSafeInstance;
    }

    private static <T extends IAppDotNetObject> T copy(IAppDotNetObject object, Gson gson) {
        final JsonElement json = gson.toJsonTree(object);
        return (T) gson.fromJson(json, object.getClass());
    }

    public static <T extends IAppDotNetObject> T copy(IAppDotNetObject object) {
        return copy(object, persistenceInstance);
    }

    public static <T extends IAppDotNetObject> T copyForWriting(IAppDotNetObject object) {
        return copy(object, instance);
    }
}