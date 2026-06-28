package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.ag2s.epublib.util.StringUtil;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/Spine.class */
public class Spine implements Serializable {
    private static final long serialVersionUID = 3878483958947357246L;
    private Resource tocResource;
    private List<SpineReference> spineReferences;

    public Spine() {
        this(new ArrayList());
    }

    public Spine(TableOfContents tableOfContents) {
        this.spineReferences = createSpineReferences(tableOfContents.getAllUniqueResources());
    }

    public Spine(List<SpineReference> spineReferences) {
        this.spineReferences = spineReferences;
    }

    public static List<SpineReference> createSpineReferences(Collection<Resource> resources) {
        List<SpineReference> result = new ArrayList<>(resources.size());
        for (Resource resource : resources) {
            result.add(new SpineReference(resource));
        }
        return result;
    }

    public List<SpineReference> getSpineReferences() {
        return this.spineReferences;
    }

    public void setSpineReferences(List<SpineReference> spineReferences) {
        this.spineReferences = spineReferences;
    }

    public Resource getResource(int index) {
        if (index < 0 || index >= this.spineReferences.size()) {
            return null;
        }
        return this.spineReferences.get(index).getResource();
    }

    public int findFirstResourceById(String resourceId) {
        if (StringUtil.isBlank(resourceId)) {
            return -1;
        }
        for (int i = 0; i < this.spineReferences.size(); i++) {
            SpineReference spineReference = this.spineReferences.get(i);
            if (resourceId.equals(spineReference.getResourceId())) {
                return i;
            }
        }
        return -1;
    }

    public SpineReference addSpineReference(SpineReference spineReference) {
        if (this.spineReferences == null) {
            this.spineReferences = new ArrayList();
        }
        this.spineReferences.add(spineReference);
        return spineReference;
    }

    public SpineReference addResource(Resource resource) {
        return addSpineReference(new SpineReference(resource));
    }

    public int size() {
        return this.spineReferences.size();
    }

    public void setTocResource(Resource tocResource) {
        this.tocResource = tocResource;
    }

    public Resource getTocResource() {
        return this.tocResource;
    }

    public int getResourceIndex(Resource currentResource) {
        if (currentResource == null) {
            return -1;
        }
        return getResourceIndex(currentResource.getHref());
    }

    public int getResourceIndex(String resourceHref) {
        int result = -1;
        if (StringUtil.isBlank(resourceHref)) {
            return -1;
        }
        int i = 0;
        while (true) {
            if (i >= this.spineReferences.size()) {
                break;
            }
            if (!resourceHref.equals(this.spineReferences.get(i).getResource().getHref())) {
                i++;
            } else {
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
