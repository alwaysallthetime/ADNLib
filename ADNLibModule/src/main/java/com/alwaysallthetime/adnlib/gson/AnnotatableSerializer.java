package com.alwaysallthetime.adnlib.gson;

import com.alwaysallthetime.adnlib.data.Annotatable;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

class AnnotatableSerializer implements JsonSerializer<Annotatable> {
    private static final Gson gson = AppDotNetGson.getAdapterSafeInstance();

    /*
     * Skips serializing the annotations field if it is empty.
     */
    @Override
    public JsonElement serialize(Annotatable annotatable, Type type, JsonSerializationContext context) {
        final JsonElement element = gson.toJsonTree(annotatable, type);

        if (!annotatable.hasAnnotations())
            element.getAsJsonObject().remove("annotations");

        return element;
    }
}