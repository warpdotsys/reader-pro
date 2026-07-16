// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.io.Serializable;

public class ResourceReference implements Serializable
{
    private static final long serialVersionUID = 2596967243557743048L;
    protected Resource resource;
    
    public ResourceReference(final Resource resource) {
        this.resource = resource;
    }
    
    public Resource getResource() {
        return this.resource;
    }
    
    public void setResource(final Resource resource) {
        this.resource = resource;
    }
    
    public String getResourceId() {
        if (this.resource != null) {
            return this.resource.getId();
        }
        return null;
    }
}
