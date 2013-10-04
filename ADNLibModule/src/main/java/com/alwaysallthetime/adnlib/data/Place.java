package com.alwaysallthetime.adnlib.data;

import java.util.ArrayList;

public class Place implements IAppDotNetObject {
    private String factualId;
    private String name;
    private String address;
    private String addressExtended;
    private String locality;
    private String region;
    private String adminRegion;
    private String postTown;
    private String poBox;
    private String postcode;
    private String countryCode;
    private double latitude;
    private double longitude;
    private boolean isOpen;
    private String telephone;
    private String fax;
    private String website;
    private ArrayList<Category> categories;

    public String getFactualId() {
        return factualId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressExtended() {
        return addressExtended;
    }

    public String getLocality() {
        return locality;
    }

    public String getRegion() {
        return region;
    }

    public String getAdminRegion() {
        return adminRegion;
    }

    public String getPostTown() {
        return postTown;
    }

    public String getPoBox() {
        return poBox;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFax() {
        return fax;
    }

    public String getWebsite() {
        return website;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
