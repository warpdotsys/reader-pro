// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Spine implements Serializable
{
    private static final long serialVersionUID = 3878483958947357246L;
    private Resource tocResource;
    private List<SpineReference> spineReferences;
    
    public Spine() {
        this(new ArrayList<SpineReference>());
    }
    
    public Spine(final TableOfContents tableOfContents) {
        this.spineReferences = createSpineReferences(tableOfContents.getAllUniqueResources());
    }
    
    public Spine(final List<SpineReference> spineReferences) {
        this.spineReferences = spineReferences;
    }
    
    public static List<SpineReference> createSpineReferences(final Collection<Resource> resources) {
        final List<SpineReference> result = new ArrayList<SpineReference>(resources.size());
        for (final Resource resource : resources) {
            result.add(new SpineReference(resource));
        }
        return result;
    }
    
    public List<SpineReference> getSpineReferences() {
        return this.spineReferences;
    }
    
    public void setSpineReferences(final List<SpineReference> spineReferences) {
        this.spineReferences = spineReferences;
    }
    
    public Resource getResource(final int index) {
        if (index < 0 || index >= this.spineReferences.size()) {
            return null;
        }
        return this.spineReferences.get(index).getResource();
    }
    
    public int findFirstResourceById(final String resourceId) {
        if (StringUtil.isBlank(resourceId)) {
            return -1;
        }
        for (int i = 0; i < this.spineReferences.size(); ++i) {
            final SpineReference spineReference = this.spineReferences.get(i);
            if (resourceId.equals(spineReference.getResourceId())) {
                return i;
            }
        }
        return -1;
    }
    
    public SpineReference addSpineReference(final SpineReference spineReference) {
        if (this.spineReferences == null) {
            this.spineReferences = new ArrayList<SpineReference>();
        }
        this.spineReferences.add(spineReference);
        return spineReference;
    }
    
    public SpineReference addResource(final Resource resource) {
        return this.addSpineReference(new SpineReference(resource));
    }
    
    public int size() {
        return this.spineReferences.size();
    }
    
    public void setTocResource(final Resource tocResource) {
        this.tocResource = tocResource;
    }
    
    public Resource getTocResource() {
        return this.tocResource;
    }
    
    public int getResourceIndex(final Resource currentResource) {
        if (currentResource == null) {
            return -1;
        }
        return this.getResourceIndex(currentResource.getHref());
    }
    
    public int getResourceIndex(final String resourceHref) {
        int result = -1;
        if (StringUtil.isBlank(resourceHref)) {
            return result;
        }
        for (int i = 0; i < this.spineReferences.size(); ++i) {
            if (resourceHref.equals(this.spineReferences.get(i).getResource().getHref())) {
                result = i;
                break;
            }
        }
        return result;
    }
    
    public boolean isEmpty() {
        return this.spineReferences.isEmpty();
    }
}
