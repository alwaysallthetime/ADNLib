package com.alwaysallthetime.adnlib.resourceStream;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObject;
import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObjectList;
import com.alwaysallthetime.adnlib.data.ResponseMeta;
import com.alwaysallthetime.adnlib.response.ResourceStreamResponseHandler;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class ResourceStream<T extends ResourceStreamResponseHandler> {
    private static final int DEFAULT_RETRIEVE_COUNT = 80;

    @Expose(serialize = false)
    protected AppDotNetClient client;
    @Expose(serialize = false)
    private  int retrieveCount;
    @Expose(serialize = false)
    private boolean isRequestInProgress;
    @Expose(serialize = false)
    private ArrayList<IPageableAppDotNetObject> objects;

    private String minId;
    private String maxId;
    private boolean isLoadedToTail;

    public ResourceStream(AppDotNetClient client) {
        this(client, DEFAULT_RETRIEVE_COUNT);
    }

    public ResourceStream(AppDotNetClient client, int count) {
        this.client = client;
        this.retrieveCount = count;
        objects = new ArrayList<IPageableAppDotNetObject>(0);
    }

    public void setClient(AppDotNetClient client) {
        this.client = client;
        if(retrieveCount == 0) {
           retrieveCount = DEFAULT_RETRIEVE_COUNT;
        }
    }

    public void setObjects(ArrayList<IPageableAppDotNetObject> objects) {
        this.objects = objects;
    }

    protected abstract void retrieveObjects(QueryParameters queryParameters, ResourceStreamResponseHandlerInternal responseHandler);

    protected abstract class ResourceStreamResponseHandlerInternal {
        public abstract void onSuccess(IPageableAppDotNetObjectList objects, ResponseMeta responseMeta);
        public abstract void onException(Exception e);
    }

    public void reload(T responseHandler) {
        minId = null;
        maxId = null;
        objects = new ArrayList<IPageableAppDotNetObject>(0);
        isLoadedToTail = false;

        loadTowardTail(responseHandler);
    }

    public void loadTowardTail(T responseHandler) {
        if(isLoadedToTail) {
            responseHandler.onSuccess(null);
        } else {
            load(getLoadTowardTailParameters(), null, responseHandler);
        }
    }

    public void loadTowardHead(T responseHandler) {
        load(getLoadTowardHeadParameters(), null, responseHandler);
    }

    public void loadToTail(final T responseHandler) {
        load(getLoadTowardTailParameters(), new ResourceStreamResponseHandlerInternal() {
            @Override
            public void onSuccess(IPageableAppDotNetObjectList objects, ResponseMeta responseMeta) {
                if(responseHandler.getResponseMeta().isMore()) {
                    loadToTail(responseHandler);
                } else {
                    responseHandler.onSuccess(objects);
                }
            }

            @Override
            public void onException(Exception e) {
                responseHandler.onError(e);
            }
        }, null);
    }

    public void loadToHead(final T responseHandler) {
        load(getLoadTowardHeadParameters(), new ResourceStreamResponseHandlerInternal() {
            @Override
            public void onSuccess(IPageableAppDotNetObjectList objects, ResponseMeta responseMeta) {
                if(responseHandler.getResponseMeta().isMore()) {
                    loadToHead(responseHandler);
                } else {
                    responseHandler.onSuccess(objects);
                }
            }

            @Override
            public void onException(Exception e) {
                responseHandler.onError(e);
            }
        }, null);
    }

    public boolean isRequestInProgress() {
        return isRequestInProgress;
    }

    public List<? extends IPageableAppDotNetObject> getObjects() {
        return objects;
    }

    private void load(final QueryParameters queryParameters, final ResourceStreamResponseHandlerInternal internalResponseHandler, final T responseHandler) {
        isRequestInProgress = true;

        retrieveObjects(queryParameters, new ResourceStreamResponseHandlerInternal() {
            @Override
            public void onSuccess(IPageableAppDotNetObjectList responseData, ResponseMeta meta) {
                // Insert objects into array (at head or at tail)
                // -- copy, combine, sort
                // -- set
                ArrayList<IPageableAppDotNetObject> newObjects = new ArrayList<IPageableAppDotNetObject>(responseData.size() + objects.size());
                newObjects.addAll(objects);
                newObjects.addAll(responseData);
                Collections.sort(newObjects, new PageableObjectComparator());

                objects = newObjects;

                if(minId == null || (meta.getMinId() != null && compareIds(minId, meta.getMinId()) == 1)) {
                    minId = meta.getMinId();
                }

                if(maxId == null || (meta.getMaxId() != null && compareIds(maxId, meta.getMaxId()) == 1)) {
                    maxId = meta.getMaxId();
                }

                if(Integer.parseInt(queryParameters.get("count")) > 0 && !meta.isMore()) {
                    isLoadedToTail = true;
                }

                isRequestInProgress = false;

                if(internalResponseHandler != null) {
                    internalResponseHandler.onSuccess(responseData, meta);
                } else if(responseHandler != null) {
                    responseHandler.onSuccess(responseData);
                }
            }

            @Override
            public void onException(Exception error) {
                isRequestInProgress = false;
                if(internalResponseHandler != null) {
                    internalResponseHandler.onException(error);
                } else if(responseHandler != null) {
                    responseHandler.onError(error);
                }
            }
        });
    }

    private QueryParameters getLoadTowardTailParameters() {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.put("before_id", minId);
        queryParameters.put("count", String.valueOf(retrieveCount));
        return queryParameters;
    }

    private QueryParameters getLoadTowardHeadParameters() {
        int retrieveCount = this.retrieveCount;
        if(maxId != null) {
            retrieveCount = -retrieveCount;
        }
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.put("since_id", maxId);
        queryParameters.put("count", String.valueOf(retrieveCount));
        return queryParameters;
    }

    private int compareIds(String id1, String id2) {
        return Integer.valueOf(id1).compareTo(Integer.valueOf(id2));
    }

    private class PageableObjectComparator implements Comparator<IPageableAppDotNetObject> {
        @Override
        public int compare(IPageableAppDotNetObject lhs, IPageableAppDotNetObject rhs) {
            Integer lhsInteger = new Integer(Integer.parseInt(lhs.getPaginationId()));
            Integer rhsInteger = new Integer(Integer.parseInt(rhs.getPaginationId()));
            //we want desc order (reverse chronological order)
            return rhsInteger.compareTo(lhsInteger);
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }
}
