package com.alwaysallthetime.adnlib.data;

import java.util.ArrayList;
import java.util.Date;

public class Interaction implements IPageableAppDotNetObject {
    private String action;
    private Date eventDate;
    private ArrayList<Object> objects;
    private ArrayList<User> users;
    private String paginationId;

    public String getAction() {
        return action;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public String getPaginationId() {
        return paginationId;
    }
}
