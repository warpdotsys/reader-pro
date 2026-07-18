package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableOfContents implements Serializable {
   private static final long serialVersionUID = -3147391239966275152L;
   public static final String DEFAULT_PATH_SEPARATOR = "/";
   private List tocReferences;

   public TableOfContents() {
      this(new ArrayList());
   }

   public TableOfContents(List tocReferences) {
      this.tocReferences = tocReferences;
   }

   public List getTocReferences() {
      return this.tocReferences;
   }

   public void setTocReferences(List tocReferences) {
      this.tocReferences = tocReferences;
   }

   public TOCReference addSection(Resource resource, String path) {
      return this.addSection(resource, path, "/");
   }

   public TOCReference addSection(Resource resource, String path, String pathSeparator) {
      String[] pathElements = path.split(pathSeparator);
      return this.addSection(resource, pathElements);
   }

   private static TOCReference findTocReferenceByTitle(String title, List tocReferences) {
      for(TOCReference tocReference : tocReferences) {
         if (title.equals(tocReference.getTitle())) {
            return tocReference;
         }
      }

      return null;
   }

   public TOCReference addSection(Resource resource, String[] pathElements) {
      if (pathElements != null && pathElements.length != 0) {
         TOCReference result = null;
         List<TOCReference> currentTocReferences = this.tocReferences;

         for(String currentTitle : pathElements) {
            result = findTocReferenceByTitle(currentTitle, currentTocReferences);
            if (result == null) {
               result = new TOCReference(currentTitle, (Resource)null);
               currentTocReferences.add(result);
            }

            currentTocReferences = result.getChildren();
         }

         result.setResource(resource);
         return result;
      } else {
         return null;
      }
   }

   public TOCReference addSection(Resource resource, int[] pathElements, String sectionTitlePrefix, String sectionNumberSeparator) {
      if (pathElements != null && pathElements.length != 0) {
         TOCReference result = null;
         List<TOCReference> currentTocReferences = this.tocReferences;

         for(int i = 0; i < pathElements.length; ++i) {
            int currentIndex = pathElements[i];
            if (currentIndex > 0 && currentIndex < currentTocReferences.size() - 1) {
               result = (TOCReference)currentTocReferences.get(currentIndex);
            } else {
               result = null;
            }

            if (result == null) {
               this.paddTOCReferences(currentTocReferences, pathElements, i, sectionTitlePrefix, sectionNumberSeparator);
               result = (TOCReference)currentTocReferences.get(currentIndex);
            }

            currentTocReferences = result.getChildren();
         }

         result.setResource(resource);
         return result;
      } else {
         return null;
      }
   }

   private void paddTOCReferences(List currentTocReferences, int[] pathElements, int pathPos, String sectionPrefix, String sectionNumberSeparator) {
      for(int i = currentTocReferences.size(); i <= pathElements[pathPos]; ++i) {
         String sectionTitle = this.createSectionTitle(pathElements, pathPos, i, sectionPrefix, sectionNumberSeparator);
         currentTocReferences.add(new TOCReference(sectionTitle, (Resource)null));
      }

   }

   private String createSectionTitle(int[] pathElements, int pathPos, int lastPos, String sectionPrefix, String sectionNumberSeparator) {
      StringBuilder title = new StringBuilder(sectionPrefix);

      for(int i = 0; i < pathPos; ++i) {
         if (i > 0) {
            title.append(sectionNumberSeparator);
         }

         title.append(pathElements[i] + 1);
      }

      if (pathPos > 0) {
         title.append(sectionNumberSeparator);
      }

      title.append(lastPos + 1);
      return title.toString();
   }

   public TOCReference addTOCReference(TOCReference tocReference) {
      if (this.tocReferences == null) {
         this.tocReferences = new ArrayList();
      }

      this.tocReferences.add(tocReference);
      return tocReference;
   }

   public List getAllUniqueResources() {
      Set<String> uniqueHrefs = new HashSet();
      List<Resource> result = new ArrayList();
      getAllUniqueResources(uniqueHrefs, result, this.tocReferences);
      return result;
   }

   private static void getAllUniqueResources(Set uniqueHrefs, List result, List tocReferences) {
      for(TOCReference tocReference : tocReferences) {
         Resource resource = tocReference.getResource();
         if (resource != null && !uniqueHrefs.contains(resource.getHref())) {
            uniqueHrefs.add(resource.getHref());
            result.add(resource);
         }

         getAllUniqueResources(uniqueHrefs, result, tocReference.getChildren());
      }

   }

   public int size() {
      return getTotalSize(this.tocReferences);
   }

   private static int getTotalSize(Collection tocReferences) {
      int result = tocReferences.size();

      for(TOCReference tocReference : tocReferences) {
         result += getTotalSize(tocReference.getChildren());
      }

      return result;
   }

   public int calculateDepth() {
      return this.calculateDepth(this.tocReferences, 0);
   }

   private int calculateDepth(List tocReferences, int currentDepth) {
      int maxChildDepth = 0;

      for(TOCReference tocReference : tocReferences) {
         int childDepth = this.calculateDepth(tocReference.getChildren(), 1);
         if (childDepth > maxChildDepth) {
            maxChildDepth = childDepth;
         }
      }

      return currentDepth + maxChildDepth;
   }
}
