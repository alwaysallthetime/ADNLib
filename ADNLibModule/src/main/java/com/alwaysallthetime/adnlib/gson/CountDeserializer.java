package com.alwaysallthetime.adnlib.gson;

import com.alwaysallthetime.adnlib.data.Count;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class CountDeserializer implements JsonDeserializer<Count> {
    @Override
    public Count deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new Count(json.getAsInt());
    }
}