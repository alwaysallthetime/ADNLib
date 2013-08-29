package com.alwaysallthetime.adnlib.data;

import java.util.ArrayList;

public abstract class Annotatable {
    protected ArrayList<Annotation> annotations;

    protected Annotatable() {
        annotations = new ArrayList<Annotation>();
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