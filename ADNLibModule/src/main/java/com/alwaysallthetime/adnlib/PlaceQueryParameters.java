package com.alwaysallthetime.adnlib;

public class PlaceQueryParameters extends QueryParameters {
    // maximum number of parameters for a Place search
    private static final int INITIAL_CAPACITY = 9;
    private static final float LOAD_FACTOR = 1.0f;

    public PlaceQueryParameters() {
        super(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public void setLatitude(double latitude) {
        put("latitude", String.valueOf(latitude));
    }

    public void setLongitude(double longitude) {
        put("longitude", String.valueOf(longitude));
    }

    public void setQuery(String query) {
        put("q", query);
    }

    public void setRadius(float radius) {
        put("radius", String.valueOf(radius));
    }

    public void setCount(int count) {
        put("count", String.valueOf(count));
    }

    public void setRemoveClosed(boolean removeClosed) {
        put("remove_closed", removeClosed ? "1" : "0");
    }

    public void setAltitude(float altitude) {
        put("altitude", String.valueOf(altitude));
    }

    public void setHorizontalAccuracy(float accuracy) {
        put("horizontal_accuracy", String.valueOf(accuracy));
    }

    public void setVerticalAccuracy(float accuracy) {
        put("vertical_accuracy", String.valueOf(accuracy));
    }
}