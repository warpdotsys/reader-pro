//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.domain;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import me.ag2s.epublib.util.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Resources implements Serializable
{
    private static final long serialVersionUID = 2450876953383871451L;
    private static final String IMAGE_PREFIX = "image_";
    private static final String ITEM_PREFIX = "item_";
    private int lastId;
    private Map<String, Resource> resources;

    public Resources() {
        this.lastId = 1;
        this.resources = new HashMap<String, Resource>();
    }

    public Resource add(final Resource resource) {
        this.fixResourceHref(resource);
        this.fixResourceId(resource);
        this.resources.put(resource.getHref(), resource);
        return resource;
    }

    public void fixResourceId(final Resource resource) {
        String resourceId = resource.getId();
        if (StringUtil.isBlank(resource.getId())) {
            resourceId = StringUtil.substringBeforeLast(resource.getHref(), '.');
            resourceId = StringUtil.substringAfterLast(resourceId, '/');
        }
        resourceId = this.makeValidId(resourceId, resource);
        if (StringUtil.isBlank(resourceId) || this.containsId(resourceId)) {
            resourceId = this.createUniqueResourceId(resource);
        }
        resource.setId(resourceId);
    }

    private String makeValidId(String resourceId, final Resource resource) {
        if (StringUtil.isNotBlank(resourceId) && !Character.isJavaIdentifierStart(resourceId.charAt(0))) {
            resourceId = this.getResourceItemPrefix(resource) + resourceId;
        }
        return resourceId;
    }

    private String getResourceItemPrefix(final Resource resource) {
        String result;
        if (MediaTypes.isBitmapImage(resource.getMediaType())) {
            result = "image_";
        }
        else {
            result = "item_";
        }
        return result;
    }

    private String createUniqueResourceId(final Resource resource) {
        int counter = this.lastId;
        if (counter == Integer.MAX_VALUE) {
            if (this.resources.size() == Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Resources contains 2147483647 elements: no new elements can be added");
            }
            counter = 1;
        }
        String prefix;
        String result;
        for (prefix = this.getResourceItemPrefix(resource), result = prefix + counter; this.containsId(result); result = prefix + ++counter) {}
        this.lastId = counter;
        return result;
    }

    public boolean containsId(final String id) {
        if (StringUtil.isBlank(id)) {
            return false;
        }
        for (final Resource resource : this.resources.values()) {
            if (id.equals(resource.getId())) {
                return true;
            }
        }
        return false;
    }

    public Resource getById(final String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        for (final Resource resource : this.resources.values()) {
            if (id.equals(resource.getId())) {
                return resource;
            }
        }
        return null;
    }

    public Resource getByProperties(final String properties) {
        if (StringUtil.isBlank(properties)) {
            return null;
        }
        for (final Resource resource : this.resources.values()) {
            if (properties.equals(resource.getProperties())) {
                return resource;
            }
        }
        return null;
    }

    public Resource remove(final String href) {
        return this.resources.remove(href);
    }

    private void fixResourceHref(final Resource resource) {
        if (StringUtil.isNotBlank(resource.getHref()) && !this.resources.containsKey(resource.getHref())) {
            return;
        }
        if (StringUtil.isBlank(resource.getHref())) {
            if (resource.getMediaType() == null) {
                throw new IllegalArgumentException("Resource must have either a MediaType or a href");
            }
            int i;
            String href;
            for (i = 1, href = this.createHref(resource.getMediaType(), i); this.resources.containsKey(href); href = this.createHref(resource.getMediaType(), ++i)) {}
            resource.setHref(href);
        }
    }

    private String createHref(final MediaType mediaType, final int counter) {
        if (MediaTypes.isBitmapImage(mediaType)) {
            return "image_" + counter + mediaType.getDefaultExtension();
        }
        return "item_" + counter + mediaType.getDefaultExtension();
    }

    public boolean isEmpty() {
        return this.resources.isEmpty();
    }

    public int size() {
        return this.resources.size();
    }

    public Map<String, Resource> getResourceMap() {
        return this.resources;
    }

    public Collection<Resource> getAll() {
        return this.resources.values();
    }

    public boolean notContainsByHref(final String href) {
        return StringUtil.isBlank(href) || !this.resources.containsKey(StringUtil.substringBefore(href, '#'));
    }

    public boolean containsByHref(final String href) {
        return !this.notContainsByHref(href);
    }

    public void set(final Collection<Resource> resources) {
        this.resources.clear();
        this.addAll(resources);
    }

    public void addAll(final Collection<Resource> resources) {
        for (final Resource resource : resources) {
            this.fixResourceHref(resource);
            this.resources.put(resource.getHref(), resource);
        }
    }

    public void set(final Map<String, Resource> resources) {
        this.resources = new HashMap<String, Resource>(resources);
    }

    public Resource getByIdOrHref(final String idOrHref) {
        Resource resource = this.getById(idOrHref);
        if (resource == null) {
            resource = this.getByHref(idOrHref);
        }
        return resource;
    }

    public Resource getByHref(String href) {
        if (StringUtil.isBlank(href)) {
            return null;
        }
        href = StringUtil.substringBefore(href, '#');
        return this.resources.get(href);
    }

    public Resource findFirstResourceByMediaType(final MediaType mediaType) {
        return findFirstResourceByMediaType(this.resources.values(), mediaType);
    }

    public static Resource findFirstResourceByMediaType(final Collection<Resource> resources, final MediaType mediaType) {
        for (final Resource resource : resources) {
            if (resource.getMediaType() == mediaType) {
                return resource;
            }
        }
        return null;
    }

    public List<Resource> getResourcesByMediaType(final MediaType mediaType) {
        final List<Resource> result = new ArrayList<Resource>();
        if (mediaType == null) {
            return result;
        }
        for (final Resource resource : this.getAll()) {
            if (resource.getMediaType() == mediaType) {
                result.add(resource);
            }
        }
        return result;
    }

    public List<Resource> getResourcesByMediaTypes(final MediaType[] mediaTypes) {
        final List<Resource> result = new ArrayList<Resource>();
        if (mediaTypes == null) {
            return result;
        }
        final List<MediaType> mediaTypesList = Arrays.asList(mediaTypes);
        for (final Resource resource : this.getAll()) {
            if (mediaTypesList.contains(resource.getMediaType())) {
                result.add(resource);
            }
        }
        return result;
    }

    public Collection<String> getAllHrefs() {
        return this.resources.keySet();
    }
}
