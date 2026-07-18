package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.ag2s.epublib.util.StringUtil;

public class Spine implements Serializable {
   private static final long serialVersionUID = 3878483958947357246L;
   private Resource tocResource;
   private List spineReferences;

   public Spine() {
      this((List)(new ArrayList()));
   }

   public Spine(TableOfContents tableOfContents) {
      this.spineReferences = createSpineReferences(tableOfContents.getAllUniqueResources());
   }

   public Spine(List spineReferences) {
      this.spineReferences = spineReferences;
   }

   public static List createSpineReferences(Collection resources) {
      List<SpineReference> result = new ArrayList(resources.size());

      for(Resource resource : resources) {
         result.add(new SpineReference(resource));
      }

      return result;
   }

   public List getSpineReferences() {
      return this.spineReferences;
   }

   public void setSpineReferences(List spineReferences) {
      this.spineReferences = spineReferences;
   }

   public Resource getResource(int index) {
      return index >= 0 && index < this.spineReferences.size() ? ((SpineReference)this.spineReferences.get(index)).getResource() : null;
   }

   public int findFirstResourceById(String resourceId) {
      if (StringUtil.isBlank(resourceId)) {
         return -1;
      } else {
         for(int i = 0; i < this.spineReferences.size(); ++i) {
            SpineReference spineReference = (SpineReference)this.spineReferences.get(i);
            if (resourceId.equals(spineReference.getResourceId())) {
               return i;
            }
         }

         return -1;
      }
   }

   public SpineReference addSpineReference(SpineReference spineReference) {
      if (this.spineReferences == null) {
         this.spineReferences = new ArrayList();
      }

      this.spineReferences.add(spineReference);
      return spineReference;
   }

   public SpineReference addResource(Resource resource) {
      return this.addSpineReference(new SpineReference(resource));
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
      return currentResource == null ? -1 : this.getResourceIndex(currentResource.getHref());
   }

   public int getResourceIndex(String resourceHref) {
      int result = -1;
      if (StringUtil.isBlank(resourceHref)) {
         return result;
      } else {
         for(int i = 0; i < this.spineReferences.size(); ++i) {
            if (resourceHref.equals(((SpineReference)this.spineReferences.get(i)).getResource().getHref())) {
               result = i;
               break;
            }
         }

         return result;
      }
   }

   public boolean isEmpty() {
      return this.spineReferences.isEmpty();
   }
}
