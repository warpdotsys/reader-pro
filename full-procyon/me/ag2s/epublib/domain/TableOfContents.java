// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class TableOfContents implements Serializable
{
    private static final long serialVersionUID = -3147391239966275152L;
    public static final String DEFAULT_PATH_SEPARATOR = "/";
    private List<TOCReference> tocReferences;
    
    public TableOfContents() {
        this(new ArrayList<TOCReference>());
    }
    
    public TableOfContents(final List<TOCReference> tocReferences) {
        this.tocReferences = tocReferences;
    }
    
    public List<TOCReference> getTocReferences() {
        return this.tocReferences;
    }
    
    public void setTocReferences(final List<TOCReference> tocReferences) {
        this.tocReferences = tocReferences;
    }
    
    public TOCReference addSection(final Resource resource, final String path) {
        return this.addSection(resource, path, "/");
    }
    
    public TOCReference addSection(final Resource resource, final String path, final String pathSeparator) {
        final String[] pathElements = path.split(pathSeparator);
        return this.addSection(resource, pathElements);
    }
    
    private static TOCReference findTocReferenceByTitle(final String title, final List<TOCReference> tocReferences) {
        for (final TOCReference tocReference : tocReferences) {
            if (title.equals(tocReference.getTitle())) {
                return tocReference;
            }
        }
        return null;
    }
    
    public TOCReference addSection(final Resource resource, final String[] pathElements) {
        if (pathElements == null || pathElements.length == 0) {
            return null;
        }
        TOCReference result = null;
        List<TOCReference> currentTocReferences = this.tocReferences;
        for (final String currentTitle : pathElements) {
            result = findTocReferenceByTitle(currentTitle, currentTocReferences);
            if (result == null) {
                result = new TOCReference(currentTitle, null);
                currentTocReferences.add(result);
            }
            currentTocReferences = result.getChildren();
        }
        result.setResource(resource);
        return result;
    }
    
    public TOCReference addSection(final Resource resource, final int[] pathElements, final String sectionTitlePrefix, final String sectionNumberSeparator) {
        if (pathElements == null || pathElements.length == 0) {
            return null;
        }
        TOCReference result = null;
        List<TOCReference> currentTocReferences = this.tocReferences;
        for (int i = 0; i < pathElements.length; ++i) {
            final int currentIndex = pathElements[i];
            if (currentIndex > 0 && currentIndex < currentTocReferences.size() - 1) {
                result = currentTocReferences.get(currentIndex);
            }
            else {
                result = null;
            }
            if (result == null) {
                this.paddTOCReferences(currentTocReferences, pathElements, i, sectionTitlePrefix, sectionNumberSeparator);
                result = currentTocReferences.get(currentIndex);
            }
            currentTocReferences = result.getChildren();
        }
        result.setResource(resource);
        return result;
    }
    
    private void paddTOCReferences(final List<TOCReference> currentTocReferences, final int[] pathElements, final int pathPos, final String sectionPrefix, final String sectionNumberSeparator) {
        for (int i = currentTocReferences.size(); i <= pathElements[pathPos]; ++i) {
            final String sectionTitle = this.createSectionTitle(pathElements, pathPos, i, sectionPrefix, sectionNumberSeparator);
            currentTocReferences.add(new TOCReference(sectionTitle, null));
        }
    }
    
    private String createSectionTitle(final int[] pathElements, final int pathPos, final int lastPos, final String sectionPrefix, final String sectionNumberSeparator) {
        final StringBuilder title = new StringBuilder(sectionPrefix);
        for (int i = 0; i < pathPos; ++i) {
            if (i > 0) {
                title.append(sectionNumberSeparator);
            }
            title.append(pathElements[i] + 1);
        }
        if (pathPos > 0) {
            title.append(sectionNumberSeparator);
        }
        title.append(lastPos + 1);
        return title.toString();
    }
    
    public TOCReference addTOCReference(final TOCReference tocReference) {
        if (this.tocReferences == null) {
            this.tocReferences = new ArrayList<TOCReference>();
        }
        this.tocReferences.add(tocReference);
        return tocReference;
    }
    
    public List<Resource> getAllUniqueResources() {
        final Set<String> uniqueHrefs = new HashSet<String>();
        final List<Resource> result = new ArrayList<Resource>();
        getAllUniqueResources(uniqueHrefs, result, this.tocReferences);
        return result;
    }
    
    private static void getAllUniqueResources(final Set<String> uniqueHrefs, final List<Resource> result, final List<TOCReference> tocReferences) {
        for (final TOCReference tocReference : tocReferences) {
            final Resource resource = tocReference.getResource();
            if (resource != null && !uniqueHrefs.contains(resource.getHref())) {
                uniqueHrefs.add(resource.getHref());
                result.add(resource);
            }
            getAllUniqueResources(uniqueHrefs, result, tocReference.getChildren());
        }
    }
    
    public int size() {
        return getTotalSize(this.tocReferences);
    }
    
    private static int getTotalSize(final Collection<TOCReference> tocReferences) {
        int result = tocReferences.size();
        for (final TOCReference tocReference : tocReferences) {
            result += getTotalSize(tocReference.getChildren());
        }
        return result;
    }
    
    public int calculateDepth() {
        return this.calculateDepth(this.tocReferences, 0);
    }
    
    private int calculateDepth(final List<TOCReference> tocReferences, final int currentDepth) {
        int maxChildDepth = 0;
        for (final TOCReference tocReference : tocReferences) {
            final int childDepth = this.calculateDepth(tocReference.getChildren(), 1);
            if (childDepth > maxChildDepth) {
                maxChildDepth = childDepth;
            }
        }
        return currentDepth + maxChildDepth;
    }
}
