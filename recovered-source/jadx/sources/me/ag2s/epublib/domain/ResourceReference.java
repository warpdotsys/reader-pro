package me.ag2s.epublib.domain;

import java.io.Serializable;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/domain/ResourceReference.class */
public class ResourceReference implements Serializable {
    private static final long serialVersionUID = 2596967243557743048L;
    protected Resource resource;

    public ResourceReference(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getResourceId() {
        if (this.resource != null) {
            return this.resource.getId();
        }
        return null;
    }
}
