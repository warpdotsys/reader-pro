package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ag2s.epublib.util.StringUtil;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/domain/Resources.class */
public class Resources implements Serializable {
    private static final long serialVersionUID = 2450876953383871451L;
    private static final String IMAGE_PREFIX = "image_";
    private static final String ITEM_PREFIX = "item_";
    private int lastId = 1;
    private Map<String, Resource> resources = new HashMap();

    public Resource add(Resource resource) {
        fixResourceHref(resource);
        fixResourceId(resource);
        this.resources.put(resource.getHref(), resource);
        return resource;
    }

    public void fixResourceId(Resource resource) {
        String resourceId = resource.getId();
        if (StringUtil.isBlank(resource.getId())) {
            String resourceId2 = StringUtil.substringBeforeLast(resource.getHref(), '.');
            resourceId = StringUtil.substringAfterLast(resourceId2, '/');
        }
        String resourceId3 = makeValidId(resourceId, resource);
        if (StringUtil.isBlank(resourceId3) || containsId(resourceId3)) {
            resourceId3 = createUniqueResourceId(resource);
        }
        resource.setId(resourceId3);
    }

    private String makeValidId(String resourceId, Resource resource) {
        if (StringUtil.isNotBlank(resourceId) && !Character.isJavaIdentifierStart(resourceId.charAt(0))) {
            resourceId = getResourceItemPrefix(resource) + resourceId;
        }
        return resourceId;
    }

    private String getResourceItemPrefix(Resource resource) {
        String result;
        if (MediaTypes.isBitmapImage(resource.getMediaType())) {
            result = IMAGE_PREFIX;
        } else {
            result = ITEM_PREFIX;
        }
        return result;
    }

    private String createUniqueResourceId(Resource resource) {
        int counter = this.lastId;
        if (counter == Integer.MAX_VALUE) {
            if (this.resources.size() == Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Resources contains 2147483647 elements: no new elements can be added");
            }
            counter = 1;
        }
        String prefix = getResourceItemPrefix(resource);
        String str = prefix + counter;
        while (true) {
            String result = str;
            if (containsId(result)) {
                counter++;
                str = prefix + counter;
            } else {
                this.lastId = counter;
                return result;
            }
        }
    }

    public boolean containsId(String id) {
        if (StringUtil.isBlank(id)) {
            return false;
        }
        for (Resource resource : this.resources.values()) {
            if (id.equals(resource.getId())) {
                return true;
            }
        }
        return false;
    }

    public Resource getById(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        for (Resource resource : this.resources.values()) {
            if (id.equals(resource.getId())) {
                return resource;
            }
        }
        return null;
    }

    public Resource getByProperties(String properties) {
        if (StringUtil.isBlank(properties)) {
            return null;
        }
        for (Resource resource : this.resources.values()) {
            if (properties.equals(resource.getProperties())) {
                return resource;
            }
        }
        return null;
    }

    public Resource remove(String href) {
        return this.resources.remove(href);
    }

    private void fixResourceHref(Resource resource) {
        if ((!StringUtil.isNotBlank(resource.getHref()) || this.resources.containsKey(resource.getHref())) && StringUtil.isBlank(resource.getHref())) {
            if (resource.getMediaType() == null) {
                throw new IllegalArgumentException("Resource must have either a MediaType or a href");
            }
            int i = 1;
            String strCreateHref = createHref(resource.getMediaType(), 1);
            while (true) {
                String href = strCreateHref;
                if (this.resources.containsKey(href)) {
                    i++;
                    strCreateHref = createHref(resource.getMediaType(), i);
                } else {
                    resource.setHref(href);
                    return;
                }
            }
        }
    }

    private String createHref(MediaType mediaType, int counter) {
        if (MediaTypes.isBitmapImage(mediaType)) {
            return IMAGE_PREFIX + counter + mediaType.getDefaultExtension();
        }
        return ITEM_PREFIX + counter + mediaType.getDefaultExtension();
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

    public boolean notContainsByHref(String href) {
        return StringUtil.isBlank(href) || !this.resources.containsKey(StringUtil.substringBefore(href, '#'));
    }

    public boolean containsByHref(String href) {
        return !notContainsByHref(href);
    }

    public void set(Collection<Resource> resources) {
        this.resources.clear();
        addAll(resources);
    }

    public void addAll(Collection<Resource> resources) {
        for (Resource resource : resources) {
            fixResourceHref(resource);
            this.resources.put(resource.getHref(), resource);
        }
    }

    public void set(Map<String, Resource> resources) {
        this.resources = new HashMap(resources);
    }

    public Resource getByIdOrHref(String idOrHref) {
        Resource resource = getById(idOrHref);
        if (resource == null) {
            resource = getByHref(idOrHref);
        }
        return resource;
    }

    public Resource getByHref(String href) {
        if (StringUtil.isBlank(href)) {
            return null;
        }
        return this.resources.get(StringUtil.substringBefore(href, '#'));
    }

    public Resource findFirstResourceByMediaType(MediaType mediaType) {
        return findFirstResourceByMediaType(this.resources.values(), mediaType);
    }

    public static Resource findFirstResourceByMediaType(Collection<Resource> resources, MediaType mediaType) {
        for (Resource resource : resources) {
            if (resource.getMediaType() == mediaType) {
                return resource;
            }
        }
        return null;
    }

    public List<Resource> getResourcesByMediaType(MediaType mediaType) {
        List<Resource> result = new ArrayList<>();
        if (mediaType == null) {
            return result;
        }
        for (Resource resource : getAll()) {
            if (resource.getMediaType() == mediaType) {
                result.add(resource);
            }
        }
        return result;
    }

    public List<Resource> getResourcesByMediaTypes(MediaType[] mediaTypes) {
        List<Resource> result = new ArrayList<>();
        if (mediaTypes == null) {
            return result;
        }
        List<MediaType> mediaTypesList = Arrays.asList(mediaTypes);
        for (Resource resource : getAll()) {
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
