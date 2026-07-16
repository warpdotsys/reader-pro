// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Guide implements Serializable
{
    private static final long serialVersionUID = -6256645339915751189L;
    public static final String DEFAULT_COVER_TITLE = "cover";
    private List<GuideReference> references;
    private static final int COVERPAGE_NOT_FOUND = -1;
    private static final int COVERPAGE_UNITIALIZED = -2;
    private int coverPageIndex;
    
    public Guide() {
        this.references = new ArrayList<GuideReference>();
        this.coverPageIndex = -1;
    }
    
    public List<GuideReference> getReferences() {
        return this.references;
    }
    
    public void setReferences(final List<GuideReference> references) {
        this.references = references;
        this.uncheckCoverPage();
    }
    
    private void uncheckCoverPage() {
        this.coverPageIndex = -2;
    }
    
    public GuideReference getCoverReference() {
        this.checkCoverPage();
        if (this.coverPageIndex >= 0) {
            return this.references.get(this.coverPageIndex);
        }
        return null;
    }
    
    public int setCoverReference(final GuideReference guideReference) {
        if (this.coverPageIndex >= 0) {
            this.references.set(this.coverPageIndex, guideReference);
        }
        else {
            this.references.add(0, guideReference);
            this.coverPageIndex = 0;
        }
        return this.coverPageIndex;
    }
    
    private void checkCoverPage() {
        if (this.coverPageIndex == -2) {
            this.initCoverPage();
        }
    }
    
    private void initCoverPage() {
        int result = -1;
        for (int i = 0; i < this.references.size(); ++i) {
            final GuideReference guideReference = this.references.get(i);
            if (guideReference.getType().equals("cover")) {
                result = i;
                break;
            }
        }
        this.coverPageIndex = result;
    }
    
    public Resource getCoverPage() {
        final GuideReference guideReference = this.getCoverReference();
        if (guideReference == null) {
            return null;
        }
        return guideReference.getResource();
    }
    
    public void setCoverPage(final Resource coverPage) {
        final GuideReference coverpageGuideReference = new GuideReference(coverPage, "cover", "cover");
        this.setCoverReference(coverpageGuideReference);
    }
    
    public ResourceReference addReference(final GuideReference reference) {
        this.references.add(reference);
        this.uncheckCoverPage();
        return reference;
    }
    
    public List<GuideReference> getGuideReferencesByType(final String referenceTypeName) {
        final List<GuideReference> result = new ArrayList<GuideReference>();
        for (final GuideReference guideReference : this.references) {
            if (referenceTypeName.equalsIgnoreCase(guideReference.getType())) {
                result.add(guideReference);
            }
        }
        return result;
    }
}
