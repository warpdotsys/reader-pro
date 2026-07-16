// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.Serializable;

public class TOCReference extends TitledResourceReference implements Serializable
{
    private static final long serialVersionUID = 5787958246077042456L;
    private List<TOCReference> children;
    private static final Comparator<TOCReference> COMPARATOR_BY_TITLE_IGNORE_CASE;
    
    @Deprecated
    public TOCReference() {
        this(null, (Resource)null, null);
    }
    
    public TOCReference(final String name, final Resource resource) {
        this(name, resource, null);
    }
    
    public TOCReference(final String name, final Resource resource, final String fragmentId) {
        this(name, resource, fragmentId, new ArrayList<TOCReference>());
    }
    
    public TOCReference(final String title, final Resource resource, final String fragmentId, final List<TOCReference> children) {
        super(resource, title, fragmentId);
        this.children = children;
    }
    
    public static Comparator<TOCReference> getComparatorByTitleIgnoreCase() {
        return TOCReference.COMPARATOR_BY_TITLE_IGNORE_CASE;
    }
    
    public List<TOCReference> getChildren() {
        return this.children;
    }
    
    public TOCReference addChildSection(final TOCReference childSection) {
        this.children.add(childSection);
        return childSection;
    }
    
    public void setChildren(final List<TOCReference> children) {
        this.children = children;
    }
    
    static {
        COMPARATOR_BY_TITLE_IGNORE_CASE = ((tocReference1, tocReference2) -> String.CASE_INSENSITIVE_ORDER.compare(tocReference1.getTitle(), tocReference2.getTitle()));
    }
}
