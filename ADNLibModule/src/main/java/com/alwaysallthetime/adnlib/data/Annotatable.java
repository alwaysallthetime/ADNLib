package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public abstract class Annotatable implements IAppDotNetObject {
    @Expose(serialize = false)
    protected String id;
    protected ArrayList<Annotation> annotations;

    public String getId() {
        return id;
    }

    public boolean hasAnnotations() {
        return annotations != null && annotations.size() > 0;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    public boolean addAnnotation(Annotation annotation) {
        if (annotations == null)
            annotations = new ArrayList<Annotation>();

        return annotations.add(annotation);
    }

    public Annotation removeAnnotation(int index) {
        if (annotations == null)
            return null;

        return annotations.remove(index);
    }

    public void clearAnnotations() {
        annotations = null;
    }
}