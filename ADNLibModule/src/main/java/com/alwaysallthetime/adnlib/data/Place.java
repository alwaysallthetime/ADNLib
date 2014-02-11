package com.alwaysallthetime.adnlib.data;

import java.util.ArrayList;

public class Place implements IAppDotNetObject {
    protected String factualId;
    protected String name;
    protected String address;
    protected String addressExtended;
    protected String locality;
    protected String region;
    protected String adminRegion;
    protected String postTown;
    protected String poBox;
    protected String postcode;
    protected String countryCode;
    protected double latitude;
    protected double longitude;
    protected boolean isOpen;
    protected String telephone;
    protected String fax;
    protected String website;
    protected ArrayList<Category> categories;

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
