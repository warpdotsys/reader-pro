// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.Serializable;

public class EpubBook implements Serializable
{
    private static final long serialVersionUID = 2068355170895770100L;
    private Resources resources;
    private Metadata metadata;
    private Spine spine;
    private TableOfContents tableOfContents;
    private final Guide guide;
    private Resource opfResource;
    private Resource ncxResource;
    private Resource coverImage;
    private String version;
    
    public EpubBook() {
        this.resources = new Resources();
        this.metadata = new Metadata();
        this.spine = new Spine();
        this.tableOfContents = new TableOfContents();
        this.guide = new Guide();
        this.version = "2.0";
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    public boolean isEpub3() {
        return this.version.startsWith("3.");
    }
    
    public TOCReference addSection(final TOCReference parentSection, final String sectionTitle, final Resource resource) {
        return this.addSection(parentSection, sectionTitle, resource, null);
    }
    
    public TOCReference addSection(final TOCReference parentSection, final String sectionTitle, final Resource resource, final String fragmentId) {
        this.getResources().add(resource);
        if (this.spine.findFirstResourceById(resource.getId()) < 0) {
            this.spine.addSpineReference(new SpineReference(resource));
        }
        return parentSection.addChildSection(new TOCReference(sectionTitle, resource, fragmentId));
    }
    
    public TOCReference addSection(final String title, final Resource resource) {
        return this.addSection(title, resource, null);
    }
    
    public TOCReference addSection(final String title, final Resource resource, final String fragmentId) {
        this.getResources().add(resource);
        final TOCReference tocReference = this.tableOfContents.addTOCReference(new TOCReference(title, resource, fragmentId));
        if (this.spine.findFirstResourceById(resource.getId()) < 0) {
            this.spine.addSpineReference(new SpineReference(resource));
        }
        return tocReference;
    }
    
    public void generateSpineFromTableOfContents() {
        final Spine spine = new Spine(this.tableOfContents);
        spine.setTocResource(this.spine.getTocResource());
        this.spine = spine;
    }
    
    public Metadata getMetadata() {
        return this.metadata;
    }
    
    public void setMetadata(final Metadata metadata) {
        this.metadata = metadata;
    }
    
    public void setResources(final Resources resources) {
        this.resources = resources;
    }
    
    public Resource addResource(final Resource resource) {
        return this.resources.add(resource);
    }
    
    public Resources getResources() {
        return this.resources;
    }
    
    public Spine getSpine() {
        return this.spine;
    }
    
    public void setSpine(final Spine spine) {
        this.spine = spine;
    }
    
    public TableOfContents getTableOfContents() {
        return this.tableOfContents;
    }
    
    public void setTableOfContents(final TableOfContents tableOfContents) {
        this.tableOfContents = tableOfContents;
    }
    
    public Resource getCoverPage() {
        Resource coverPage = this.guide.getCoverPage();
        if (coverPage == null) {
            coverPage = this.spine.getResource(0);
        }
        return coverPage;
    }
    
    public void setCoverPage(final Resource coverPage) {
        if (coverPage == null) {
            return;
        }
        if (this.resources.notContainsByHref(coverPage.getHref())) {
            this.resources.add(coverPage);
        }
        this.guide.setCoverPage(coverPage);
    }
    
    public String getTitle() {
        return this.getMetadata().getFirstTitle();
    }
    
    public Resource getCoverImage() {
        return this.coverImage;
    }
    
    public void setCoverImage(final Resource coverImage) {
        if (coverImage == null) {
            return;
        }
        if (this.resources.notContainsByHref(coverImage.getHref())) {
            this.resources.add(coverImage);
        }
        this.coverImage = coverImage;
    }
    
    public Guide getGuide() {
        return this.guide;
    }
    
    public List<Resource> getContents() {
        final Map<String, Resource> result = new LinkedHashMap<String, Resource>();
        addToContentsResult(this.getCoverPage(), result);
        for (final SpineReference spineReference : this.getSpine().getSpineReferences()) {
            addToContentsResult(spineReference.getResource(), result);
        }
        for (final Resource resource : this.getTableOfContents().getAllUniqueResources()) {
            addToContentsResult(resource, result);
        }
        for (final GuideReference guideReference : this.getGuide().getReferences()) {
            addToContentsResult(guideReference.getResource(), result);
        }
        return new ArrayList<Resource>(result.values());
    }
    
    private static void addToContentsResult(final Resource resource, final Map<String, Resource> allReachableResources) {
        if (resource != null && !allReachableResources.containsKey(resource.getHref())) {
            allReachableResources.put(resource.getHref(), resource);
        }
    }
    
    public Resource getOpfResource() {
        return this.opfResource;
    }
    
    public void setOpfResource(final Resource opfResource) {
        this.opfResource = opfResource;
    }
    
    public void setNcxResource(final Resource ncxResource) {
        this.ncxResource = ncxResource;
    }
    
    public Resource getNcxResource() {
        return this.ncxResource;
    }
}
