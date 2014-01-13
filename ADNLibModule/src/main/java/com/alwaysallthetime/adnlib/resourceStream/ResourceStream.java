package com.alwaysallthetime.adnlib.resourceStream;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.QueryParameters;
import com.alwaysallthetime.adnlib.data.IPageableAppDotNetIdObject;
import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObject;
import com.alwaysallthetime.adnlib.data.IPageableAppDotNetObjectList;
import com.alwaysallthetime.adnlib.data.ResponseMeta;
import com.alwaysallthetime.adnlib.response.ResourceStreamResponseHandler;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public abstract class ResourceStream<T extends ResourceStreamResponseHandler> {
    private static final int DEFAULT_RETRIEVE_COUNT = 80;

    @Expose(serialize = false)
    protected AppDotNetClient client;
    @Expose(serialize = false)
    private  int retrieveCount;
    @Expose(serialize = false)
    private boolean isRequestInProgress;
    @Expose(serialize = false)
    private TreeMap<String, IPageableAppDotNetObject> objects;
    @Expose(serialize = false)
    private HashMap<String, String> objectPaginationIdsForIds; //id : paginationId

    private String minId;
    private String maxId;
    private boolean isLoadedToTail;

    protected abstract void retrieveObjects(QueryParameters queryParameters, ResourceStreamResponseHandlerInternal responseHandler);

    protected abstract class ResourceStreamResponseHandlerInternal {
        public abstract void onSuccess(IPageableAppDotNetObjectList objects, ResponseMeta responseMeta);
        public abstract void onException(Exception e);
    }

    public ResourceStream(AppDotNetClient client) {
        this(client, DEFAULT_RETRIEVE_COUNT);
    }

    public ResourceStream(AppDotNetClient client, int count) {
        this.client = client;
        this.retrieveCount = count;
        objects = new TreeMap<String, IPageableAppDotNetObject>(new PageableObjectIdComparator());
        objectPaginationIdsForIds = new HashMap<String, String>(0);
    }

    public synchronized void setClient(AppDotNetClient client) {
        this.client = client;
        if(retrieveCount == 0) {
           retrieveCount = DEFAULT_RETRIEVE_COUNT;
        }
        if(objects == null) {
            objects = new TreeMap<String, IPageableAppDotNetObject>(new PageableObjectIdComparator());
            objectPaginationIdsForIds = new HashMap<String, String>(0);
        }
    }

    public synchronized void setObjects(ArrayList<IPageableAppDotNetObject> newObjects) {
        objects = new TreeMap<String, IPageableAppDotNetObject>(new PageableObjectIdComparator());
        objectPaginationIdsForIds = new HashMap<String, String>(newObjects.size());

        //all objects will either have an id or won't (they're homogeneous)
        Boolean hasId = null;
        for(IPageableAppDotNetObject o : newObjects) {
            String paginationId = o.getPaginationId();
            objects.put(paginationId, o);

            if(hasId == null) {
                hasId = o instanceof IPageableAppDotNetIdObject;
            }
            if(hasId) {
                objectPaginationIdsForIds.put(((IPageableAppDotNetIdObject)o).getId(), paginationId);
            }
        }
    }

    /**
     * Clear the state of this ResourceStream and retrieve a batch of objects starting
     * at the head of the stream.
     *
     * @param responseHandler
     */
    public void reload(T responseHandler) {
        minId = null;
        maxId = null;
        objects = new TreeMap<String, IPageableAppDotNetObject>(new PageableObjectIdComparator());
        isLoadedToTail = false;

        loadTowardTail(responseHandler);
    }

    /**
     * Clear the state of this ResourceStream and retrieve all objects in the stream.
     *
     * @param responseHandler
     */
    public void reloadToTail(T responseHandler) {
        minId = null;
        maxId = null;
        objects = new TreeMap<String, IPageableAppDotNetObject>(new PageableObjectIdComparator());
        isLoadedToTail = false;

        loadToTail(responseHandler);
    }

    /**
     * Load more objects in this stream.
     *
     * @param responseHandler
     */
    public void loadTowardTail(T responseHandler) {
        if(isLoadedToTail) {
            responseHandler.onSuccess(null);
        } else {
            load(getLoadTowardTailParameters(), null, responseHandler);
        }
    }

    /**
     * Load the newest objects in this stream.
     *
     * @param responseHandler
     */
    public void loadTowardHead(T responseHandler) {
        load(getLoadTowardHeadParameters(), null, responseHandler);
    }

    /**
     * Load the rest of the objects in this stream, starting from the oldest known object.
     *
     * @param responseHandler
     */
    public void loadToTail(final T responseHandler) {
        load(getLoadTowardTailParameters(), new ResourceStreamResponseHandlerInternal() {
            @Override
            public void onSuccess(IPageableAppDotNetObjectList objects, ResponseMeta responseMeta) {
                if(responseMeta.isMore()) {
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

    /**
     * Load the rest of the newest objects in this stream, starting from the latest known object.
     *
     * @param responseHandler
     */
    public void loadToHead(final T responseHandler) {
        load(getLoadTowardHeadParameters(), new ResourceStreamResponseHandlerInternal() {
            @Override
            public void onSuccess(IPageableAppDotNetObjectList objects, ResponseMeta responseMeta) {
                if(responseMeta.isMore()) {
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

    public String getMaxId() {
        return maxId;
    }

    public String getMinId() {
        return minId;
    }

    public synchronized List<? extends IPageableAppDotNetObject> getObjects() {
        return new ArrayList<IPageableAppDotNetObject>(objects.values());
    }

    private synchronized void load(final QueryParameters queryParameters, final ResourceStreamResponseHandlerInternal internalResponseHandler, final T responseHandler) {
        isRequestInProgress = true;

        retrieveObjects(queryParameters, new ResourceStreamResponseHandlerInternal() {
            @Override
            public void onSuccess(IPageableAppDotNetObjectList responseData, ResponseMeta meta) {
                TreeMap<String, IPageableAppDotNetObject> newObjects = new TreeMap<String, IPageableAppDotNetObject>(new PageableObjectIdComparator());

                //1. remove any existing objects that have the same pagination id or the same id as this object.
                //2. add the object to the new objects map
                //3. add the existing objects to the new objects map
                //
                //(sorting is done automatically thanks to TreeMap)

                for(Object obj : responseData) {
                    String id = (obj instanceof IPageableAppDotNetIdObject) ? ((IPageableAppDotNetIdObject)obj).getId() : null;
                    String paginationId = ((IPageableAppDotNetObject)obj).getPaginationId();

                    objects.remove(paginationId);
                    if(id != null) {
                        //we see if there is an existing object in 'objects' with this same id
                        //by checking to see if we get a pagination id back for this id.
                        //if we do, then use the pagination id to remove the existing object from
                        //the main objects map.
                        String paginationIdForId = objectPaginationIdsForIds.get(id);
                        if(paginationIdForId != null) {
                            objects.remove(paginationIdForId);
                        }
                        objectPaginationIdsForIds.put(id, paginationId);
                    }

                    newObjects.put(paginationId, (IPageableAppDotNetObject) obj);
                }

                newObjects.putAll(objects);
                objects = newObjects;

                if(minId == null || (meta.getMinId() != null && compareIds(minId, meta.getMinId()) == 1)) {
                    minId = meta.getMinId();
                }

                if(maxId == null || (meta.getMaxId() != null && compareIds(maxId, meta.getMaxId()) == -1)) {
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

    private class PageableObjectIdComparator implements Comparator<String> {
        @Override
        public int compare(String lhs, String rhs) {
            //we want desc order (reverse chronological order)
            return compareIds(rhs, lhs);
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }
}
