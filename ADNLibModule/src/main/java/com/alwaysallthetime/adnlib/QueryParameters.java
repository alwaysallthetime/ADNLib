package com.alwaysallthetime.adnlib;

import java.util.HashMap;
import java.util.List;

public class QueryParameters extends HashMap<String, String> {
    // very unlikely you'd ever need more than 4 query parameters on one request
    private static final int INITIAL_CAPACITY = 4;
    private static final float LOAD_FACTOR = 1.0f;

    /**
     * Get a new QueryParameters that consists of two sets of QueryParamters combined.
     *
     * Since it is possible that there will be conflicting parameters, the second set will be applied
     * "on top of" the first set, i.e. the topParameters can override the bottomParameters
     *
     * @param bottomParameters
     * @param topParameters
     * @return a new QueryParamters consisting of the values form both specified QueryParameters objects.
     */
    public static QueryParameters getCombinedParameters(QueryParameters bottomParameters, QueryParameters topParameters) {
        QueryParameters newParams = new QueryParameters();
        newParams.putAll(bottomParameters);
        newParams.putAll(topParameters);
        return newParams;
    }

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