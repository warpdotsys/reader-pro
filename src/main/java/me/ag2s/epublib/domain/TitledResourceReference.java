package me.ag2s.epublib.domain;

import java.io.Serializable;
import me.ag2s.epublib.util.StringUtil;

public class TitledResourceReference extends ResourceReference implements Serializable {
   private static final long serialVersionUID = 3918155020095190080L;
   private String fragmentId;
   private String title;

   /** @deprecated */
   @Deprecated
   public TitledResourceReference(Resource resource) {
      this(resource, (String)null);
   }

   public TitledResourceReference(Resource resource, String title) {
      this(resource, title, (String)null);
   }

   public TitledResourceReference(Resource resource, String title, String fragmentId) {
      super(resource);
      this.title = title;
      this.fragmentId = fragmentId;
   }

   public String getFragmentId() {
      return this.fragmentId;
   }

   public void setFragmentId(String fragmentId) {
      this.fragmentId = fragmentId;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getCompleteHref() {
      return StringUtil.isBlank(this.fragmentId) ? this.resource.getHref() : this.resource.getHref() + '#' + this.fragmentId;
   }

   public Resource getResource() {
      if (this.resource != null && this.title != null) {
         this.resource.setTitle(this.title);
      }

      return this.resource;
   }

   public void setResource(Resource resource, String fragmentId) {
      super.setResource(resource);
      this.fragmentId = fragmentId;
   }

   public void setResource(Resource resource) {
      this.setResource(resource, (String)null);
   }
}
