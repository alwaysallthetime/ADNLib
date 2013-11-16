package com.alwaysallthetime.adnlib;

import java.util.HashMap;
import java.util.List;

public class QueryParameters extends HashMap<String, String> {
    // very unlikely you'd ever need more than 4 query parameters on one request
    private static final int INITIAL_CAPACITY = 4;
    private static final float LOAD_FACTOR = 1.0f;

    public QueryParameters() {
        this(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    protected QueryParameters(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    public QueryParameters(IQueryParameter... queryParameters) {
        super(INITIAL_CAPACITY + queryParameters.length, LOAD_FACTOR);
        put(queryParameters);
    }

    public void put(IQueryParameter... queryParameters) {
        for (IQueryParameter queryParameter : queryParameters) {
            put(queryParameter.getName(), queryParameter.getValue());
        }
    }

    protected String getCommaDelimitedString(List<String> strings, int bestGuessMaxLength) {
        final StringBuilder buffer = new StringBuilder(strings.size() * bestGuessMaxLength);
        for(final String str : strings) {
            buffer.append(str);
            buffer.append(',');
        }

        return buffer.substring(0, buffer.length() - 1);
    }
}