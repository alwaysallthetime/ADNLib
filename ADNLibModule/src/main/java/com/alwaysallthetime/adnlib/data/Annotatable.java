package com.alwaysallthetime.adnlib.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public abstract class Annotatable implements IPageableAppDotNetIdObject {
    @Expose(serialize = false)
    protected String id;
    @Expose(serialize = false)
    protected String paginationId;
    protected ArrayList<Annotation> annotations;

    public String getId() {
        return id;
    }

    @Override
    public String getPaginationId() {
        return paginationId;
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

    public Annotation getFirstAnnotationOfType(String annotationType) {
        if(annotations != null) {
            for(Annotation a : annotations) {
                if(a.getType().equals(annotationType)) {
                    return a;
                }
            }
        }
        return null;
    }

    public List<Annotation> getAnnotationsOfType(String annotationType) {
        ArrayList<Annotation> typedAnnotations = new ArrayList<Annotation>();
        if(annotations != null) {
            for(Annotation a : annotations) {
                if(a.getType().equals(annotationType)) {
                    typedAnnotations.add(a);
                }
            }
        }
        return typedAnnotations;
    }
}