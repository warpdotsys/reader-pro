// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;
import java.io.Serializable;

public class TitledResourceReference extends ResourceReference implements Serializable
{
    private static final long serialVersionUID = 3918155020095190080L;
    private String fragmentId;
    private String title;
    
    @Deprecated
    public TitledResourceReference(final Resource resource) {
        this(resource, null);
    }
    
    public TitledResourceReference(final Resource resource, final String title) {
        this(resource, title, null);
    }
    
    public TitledResourceReference(final Resource resource, final String title, final String fragmentId) {
        super(resource);
        this.title = title;
        this.fragmentId = fragmentId;
    }
    
    public String getFragmentId() {
        return this.fragmentId;
    }
    
    public void setFragmentId(final String fragmentId) {
        this.fragmentId = fragmentId;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getCompleteHref() {
        if (StringUtil.isBlank(this.fragmentId)) {
            return this.resource.getHref();
        }
        return this.resource.getHref() + '#' + this.fragmentId;
    }
    
    @Override
    public Resource getResource() {
        if (this.resource != null && this.title != null) {
            this.resource.setTitle(this.title);
        }
        return this.resource;
    }
    
    public void setResource(final Resource resource, final String fragmentId) {
        super.setResource(resource);
        this.fragmentId = fragmentId;
    }
    
    @Override
    public void setResource(final Resource resource) {
        this.setResource(resource, null);
    }
}
