package com.alwaysallthetime.adnlib.data;

import java.util.HashMap;

public class Annotation {
    private String type;
    private HashMap<String, Object> value;

    public Annotation() {
        value = new HashMap<String, Object>();
    }

    public Annotation(String type) {
        this();
        this.type = type;
    }

    public Annotation(String type, HashMap<String, Object> value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, Object> getValue() {
        return value;
    }

    public void setValue(HashMap<String, Object> value) {
        this.value = value;
    }

    public Object addValue(String key, Object value) {
        return this.value.put(key, value);
    }

    public Object removeValue(String key) {
        return value.remove(key);
    }

    public Object getValueForKey(String key) {
        return value.get(key);
    }
}