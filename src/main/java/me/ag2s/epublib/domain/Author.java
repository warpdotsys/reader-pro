package me.ag2s.epublib.domain;

import java.io.Serializable;
import me.ag2s.epublib.util.StringUtil;

public class Author implements Serializable {
   private static final long serialVersionUID = 6663408501416574200L;
   private String firstname;
   private String lastname;
   private Relator relator;

   public Author(String singleName) {
      this("", singleName);
   }

   public Author(String firstname, String lastname) {
      this.relator = Relator.AUTHOR;
      this.firstname = firstname;
      this.lastname = lastname;
   }

   public String getFirstname() {
      return this.firstname;
   }

   public void setFirstname(String firstname) {
      this.firstname = firstname;
   }

   public String getLastname() {
      return this.lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = lastname;
   }

   public String toString() {
      return this.lastname + ", " + this.firstname;
   }

   public int hashCode() {
      return StringUtil.hashCode(this.firstname, this.lastname);
   }

   public boolean equals(Object authorObject) {
      if (!(authorObject instanceof Author)) {
         return false;
      } else {
         Author other = (Author)authorObject;
         return StringUtil.equals(this.firstname, other.firstname) && StringUtil.equals(this.lastname, other.lastname);
      }
   }

   public void setRole(String code) {
      Relator result = Relator.byCode(code);
      if (result == null) {
         result = Relator.AUTHOR;
      }

      this.relator = result;
   }

   public Relator getRelator() {
      return this.relator;
   }

   public void setRelator(Relator relator) {
      this.relator = relator;
   }
}
