package com.alwaysallthetime.adnlib.resourceStream;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.data.InteractionList;
import com.alwaysallthetime.adnlib.response.InteractionListResponseHandler;
import com.alwaysallthetime.adnlib.response.InteractionsResourceStreamResponseHandler;

public class InteractionResourceStream extends ResourceStream<InteractionsResourceStreamResponseHandler> {
    private QueryParameters queryParameters;

    public InteractionResourceStream(AppDotNetClient client) {
        super(client);
    }

    public InteractionResourceStream(AppDotNetClient client, QueryParameters parameters) {
        this(client);
        queryParameters = parameters;
    }

    @Override
    protected void retrieveObjects(QueryParameters queryParameters, final ResourceStreamResponseHandlerInternal responseHandler) {
        QueryParameters params = QueryParameters.getCombinedParameters(this.queryParameters, queryParameters);
        client.retrieveCurrentUserInteractions(params, new InteractionListResponseHandler() {
            @Override
            public void onSuccess(InteractionList responseData) {
                responseHandler.onSuccess(responseData, getResponseMeta());
            }

            @Override
            public void onError(Exception error) {
                super.onError(error);
                responseHandler.onException(error);
            }
        });
    }
}
