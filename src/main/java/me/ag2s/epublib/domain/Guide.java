package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Guide implements Serializable {
   private static final long serialVersionUID = -6256645339915751189L;
   public static final String DEFAULT_COVER_TITLE = "cover";
   private List references = new ArrayList();
   private static final int COVERPAGE_NOT_FOUND = -1;
   private static final int COVERPAGE_UNITIALIZED = -2;
   private int coverPageIndex = -1;

   public List getReferences() {
      return this.references;
   }

   public void setReferences(List references) {
      this.references = references;
      this.uncheckCoverPage();
   }

   private void uncheckCoverPage() {
      this.coverPageIndex = -2;
   }

   public GuideReference getCoverReference() {
      this.checkCoverPage();
      return this.coverPageIndex >= 0 ? (GuideReference)this.references.get(this.coverPageIndex) : null;
   }

   public int setCoverReference(GuideReference guideReference) {
      if (this.coverPageIndex >= 0) {
         this.references.set(this.coverPageIndex, guideReference);
      } else {
         this.references.add(0, guideReference);
         this.coverPageIndex = 0;
      }

      return this.coverPageIndex;
   }

   private void checkCoverPage() {
      if (this.coverPageIndex == -2) {
         this.initCoverPage();
      }

   }

   private void initCoverPage() {
      int result = -1;

      for(int i = 0; i < this.references.size(); ++i) {
         GuideReference guideReference = (GuideReference)this.references.get(i);
         if (guideReference.getType().equals("cover")) {
            result = i;
            break;
         }
      }

      this.coverPageIndex = result;
   }

   public Resource getCoverPage() {
      GuideReference guideReference = this.getCoverReference();
      return guideReference == null ? null : guideReference.getResource();
   }

   public void setCoverPage(Resource coverPage) {
      GuideReference coverpageGuideReference = new GuideReference(coverPage, "cover", "cover");
      this.setCoverReference(coverpageGuideReference);
   }

   public ResourceReference addReference(GuideReference reference) {
      this.references.add(reference);
      this.uncheckCoverPage();
      return reference;
   }

   public List getGuideReferencesByType(String referenceTypeName) {
      List<GuideReference> result = new ArrayList();

      for(GuideReference guideReference : this.references) {
         if (referenceTypeName.equalsIgnoreCase(guideReference.getType())) {
            result.add(guideReference);
         }
      }

      return result;
   }
}
