package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public abstract class Annotatable implements IAppDotNetObject {
    @Expose(serialize = false)
    protected String id;
    protected ArrayList<Annotation> annotations;

    protected Annotatable() {
        annotations = new ArrayList<Annotation>();
    }

    public String getId() {
        return id;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    public boolean addAnnotation(Annotation annotation) {
        return annotations.add(annotation);
    }

    public Annotation removeAnnotation(int index) {
        return annotations.remove(index);
    }

    public void clearAnnotations() {
        annotations = null;
    }
}