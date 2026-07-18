package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import me.ag2s.epublib.util.StringUtil;

public class Identifier implements Serializable {
   private static final long serialVersionUID = 955949951416391810L;
   private boolean bookId;
   private String scheme;
   private String value;

   public Identifier() {
      this("UUID", UUID.randomUUID().toString());
   }

   public Identifier(String scheme, String value) {
      this.bookId = false;
      this.scheme = scheme;
      this.value = value;
   }

   public static Identifier getBookIdIdentifier(List<Identifier> identifiers) {
      if (identifiers != null && !identifiers.isEmpty()) {
         Identifier result = null;

         for(Identifier identifier : identifiers) {
            if (identifier.isBookId()) {
               result = identifier;
               break;
            }
         }

         if (result == null) {
            result = (Identifier)identifiers.get(0);
         }

         return result;
      } else {
         return null;
      }
   }

   public String getScheme() {
      return this.scheme;
   }

   public void setScheme(String scheme) {
      this.scheme = scheme;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setBookId(boolean bookId) {
      this.bookId = bookId;
   }

   public boolean isBookId() {
      return this.bookId;
   }

   public int hashCode() {
      return StringUtil.defaultIfNull(this.scheme).hashCode() ^ StringUtil.defaultIfNull(this.value).hashCode();
   }

   public boolean equals(Object otherIdentifier) {
      if (!(otherIdentifier instanceof Identifier)) {
         return false;
      } else {
         return StringUtil.equals(this.scheme, ((Identifier)otherIdentifier).scheme) && StringUtil.equals(this.value, ((Identifier)otherIdentifier).value);
      }
   }

   public String toString() {
      return StringUtil.isBlank(this.scheme) ? "" + this.value : "" + this.scheme + ":" + this.value;
   }

   public interface Scheme {
      String UUID = "UUID";
      String ISBN = "ISBN";
      String URL = "URL";
      String URI = "URI";
   }
}
