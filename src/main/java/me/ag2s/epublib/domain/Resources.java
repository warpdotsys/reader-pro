package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ag2s.epublib.util.StringUtil;

public class Resources implements Serializable {
   private static final long serialVersionUID = 2450876953383871451L;
   private static final String IMAGE_PREFIX = "image_";
   private static final String ITEM_PREFIX = "item_";
   private int lastId = 1;
   private Map<String, Resource> resources = new HashMap();

   public Resource add(Resource resource) {
      this.fixResourceHref(resource);
      this.fixResourceId(resource);
      this.resources.put(resource.getHref(), resource);
      return resource;
   }

   public void fixResourceId(Resource resource) {
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

   private String makeValidId(String resourceId, Resource resource) {
      if (StringUtil.isNotBlank(resourceId) && !Character.isJavaIdentifierStart(resourceId.charAt(0))) {
         resourceId = this.getResourceItemPrefix(resource) + resourceId;
      }

      return resourceId;
   }

   private String getResourceItemPrefix(Resource resource) {
      String result;
      if (MediaTypes.isBitmapImage(resource.getMediaType())) {
         result = "image_";
      } else {
         result = "item_";
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

      String prefix = this.getResourceItemPrefix(resource);

      String result;
      StringBuilder var10000;
      for(result = prefix + counter; this.containsId(result); result = var10000.append(counter).toString()) {
         var10000 = (new StringBuilder()).append(prefix);
         ++counter;
      }

      this.lastId = counter;
      return result;
   }

   public boolean containsId(String id) {
      if (StringUtil.isBlank(id)) {
         return false;
      } else {
         for(Resource resource : this.resources.values()) {
            if (id.equals(resource.getId())) {
               return true;
            }
         }

         return false;
      }
   }

   public Resource getById(String id) {
      if (StringUtil.isBlank(id)) {
         return null;
      } else {
         for(Resource resource : this.resources.values()) {
            if (id.equals(resource.getId())) {
               return resource;
            }
         }

         return null;
      }
   }

   public Resource getByProperties(String properties) {
      if (StringUtil.isBlank(properties)) {
         return null;
      } else {
         for(Resource resource : this.resources.values()) {
            if (properties.equals(resource.getProperties())) {
               return resource;
            }
         }

         return null;
      }
   }

   public Resource remove(String href) {
      return (Resource)this.resources.remove(href);
   }

   private void fixResourceHref(Resource resource) {
      if (!StringUtil.isNotBlank(resource.getHref()) || this.resources.containsKey(resource.getHref())) {
         if (StringUtil.isBlank(resource.getHref())) {
            if (resource.getMediaType() == null) {
               throw new IllegalArgumentException("Resource must have either a MediaType or a href");
            }

            int i = 1;

            String href;
            MediaType var10001;
            for(href = this.createHref(resource.getMediaType(), i); this.resources.containsKey(href); href = this.createHref(var10001, i)) {
               var10001 = resource.getMediaType();
               ++i;
            }

            resource.setHref(href);
         }

      }
   }

   private String createHref(MediaType mediaType, int counter) {
      return MediaTypes.isBitmapImage(mediaType) ? "image_" + counter + mediaType.getDefaultExtension() : "item_" + counter + mediaType.getDefaultExtension();
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
      if (StringUtil.isBlank(href)) {
         return true;
      } else {
         return !this.resources.containsKey(StringUtil.substringBefore(href, '#'));
      }
   }

   public boolean containsByHref(String href) {
      return !this.notContainsByHref(href);
   }

   public void set(Collection<Resource> resources) {
      this.resources.clear();
      this.addAll(resources);
   }

   public void addAll(Collection<Resource> resources) {
      for(Resource resource : resources) {
         this.fixResourceHref(resource);
         this.resources.put(resource.getHref(), resource);
      }

   }

   public void set(Map<String, Resource> resources) {
      this.resources = new HashMap(resources);
   }

   public Resource getByIdOrHref(String idOrHref) {
      Resource resource = this.getById(idOrHref);
      if (resource == null) {
         resource = this.getByHref(idOrHref);
      }

      return resource;
   }

   public Resource getByHref(String href) {
      if (StringUtil.isBlank(href)) {
         return null;
      } else {
         href = StringUtil.substringBefore(href, '#');
         return (Resource)this.resources.get(href);
      }
   }

   public Resource findFirstResourceByMediaType(MediaType mediaType) {
      return findFirstResourceByMediaType(this.resources.values(), mediaType);
   }

   public static Resource findFirstResourceByMediaType(Collection<Resource> resources, MediaType mediaType) {
      for(Resource resource : resources) {
         if (resource.getMediaType() == mediaType) {
            return resource;
         }
      }

      return null;
   }

   public List<Resource> getResourcesByMediaType(MediaType mediaType) {
      List<Resource> result = new ArrayList();
      if (mediaType == null) {
         return result;
      } else {
         for(Resource resource : this.getAll()) {
            if (resource.getMediaType() == mediaType) {
               result.add(resource);
            }
         }

         return result;
      }
   }

   public List<Resource> getResourcesByMediaTypes(MediaType[] mediaTypes) {
      List<Resource> result = new ArrayList();
      if (mediaTypes == null) {
         return result;
      } else {
         List<MediaType> mediaTypesList = Arrays.asList(mediaTypes);

         for(Resource resource : this.getAll()) {
            if (mediaTypesList.contains(resource.getMediaType())) {
               result.add(resource);
            }
         }

         return result;
      }
   }

   public Collection<String> getAllHrefs() {
      return this.resources.keySet();
   }
}
