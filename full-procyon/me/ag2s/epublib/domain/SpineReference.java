// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.io.Serializable;

public class SpineReference extends ResourceReference implements Serializable
{
    private static final long serialVersionUID = -7921609197351510248L;
    private boolean linear;
    
    public SpineReference(final Resource resource) {
        this(resource, true);
    }
    
    public SpineReference(final Resource resource, final boolean linear) {
        super(resource);
        this.linear = linear;
    }
    
    public boolean isLinear() {
        return this.linear;
    }
    
    public void setLinear(final boolean linear) {
        this.linear = linear;
    }
}
