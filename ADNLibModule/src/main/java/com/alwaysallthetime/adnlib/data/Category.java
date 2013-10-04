package com.alwaysallthetime.adnlib.data;

import java.util.ArrayList;

public class Category implements IAppDotNetObject {
    private String id;
    private ArrayList<String> labels;

    public String getId() {
        return id;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }
}
