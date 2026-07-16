package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class MediaType implements Serializable {
   private static final long serialVersionUID = -7256091153727506788L;
   private final String name;
   private final String defaultExtension;
   private final Collection<String> extensions;

   public MediaType(String name, String defaultExtension) {
      this(name, defaultExtension, new String[]{defaultExtension});
   }

   public MediaType(String name, String defaultExtension, String[] extensions) {
      this(name, defaultExtension, Arrays.asList(extensions));
   }

   @Override
   public int hashCode() {
      return this.name == null ? 0 : this.name.hashCode();
   }

   public MediaType(String name, String defaultExtension, Collection<String> mextensions) {
      this.name = name;
      this.defaultExtension = defaultExtension;
      this.extensions = mextensions;
   }

   public String getName() {
      return this.name;
   }

   public String getDefaultExtension() {
      return this.defaultExtension;
   }

   public Collection<String> getExtensions() {
      return this.extensions;
   }

   @Override
   public boolean equals(Object otherMediaType) {
      return !(otherMediaType instanceof MediaType) ? false : this.name.equals(((MediaType)otherMediaType).getName());
   }

   @Override
   public String toString() {
      return this.name;
   }
}
