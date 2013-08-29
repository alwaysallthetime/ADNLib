package com.alwaysallthetime.adnlib.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

// Adapted from Gson's DefaultDateTypeAdapter to provide ISO 8601 exclusively
class Iso8601DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final SimpleDateFormat iso8601Format;

    public Iso8601DateTypeAdapter() {
        iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        synchronized (iso8601Format) {
            String dateFormatAsString = iso8601Format.format(src);
            return new JsonPrimitive(dateFormatAsString);
        }
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        synchronized (iso8601Format) {
            try {
                return iso8601Format.parse(json.getAsString());
            } catch (ParseException e) {
                throw new JsonSyntaxException(json.getAsString(), e);
            }
        }
    }
}