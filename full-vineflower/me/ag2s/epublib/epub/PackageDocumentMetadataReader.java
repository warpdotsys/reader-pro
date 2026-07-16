package me.ag2s.epublib.epub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.Date;
import me.ag2s.epublib.domain.Identifier;
import me.ag2s.epublib.domain.Metadata;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class PackageDocumentMetadataReader extends PackageDocumentBase {
   private static final String TAG = PackageDocumentMetadataReader.class.getName();

   public static Metadata readMetadata(Document packageDocument) {
      Metadata result = new Metadata();
      Element metadataElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), "http://www.idpf.org/2007/opf", "metadata");
      if (metadataElement == null) {
         System.err.println(TAG + " Package does not contain element " + "metadata");
         return result;
      } else {
         result.setTitles(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "title"));
         result.setPublishers(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "publisher"));
         result.setDescriptions(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "description"));
         result.setRights(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "rights"));
         result.setTypes(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "type"));
         result.setSubjects(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "subject"));
         result.setIdentifiers(readIdentifiers(metadataElement));
         result.setAuthors(readCreators(metadataElement));
         result.setContributors(readContributors(metadataElement));
         result.setDates(readDates(metadataElement));
         result.setOtherProperties(readOtherProperties(metadataElement));
         result.setMetaAttributes(readMetaProperties(metadataElement));
         Element languageTag = DOMUtil.getFirstElementByTagNameNS(metadataElement, "http://purl.org/dc/elements/1.1/", "language");
         if (languageTag != null) {
            result.setLanguage(DOMUtil.getTextChildrenContent(languageTag));
         }

         return result;
      }
   }

   private static Map<QName, String> readOtherProperties(Element metadataElement) {
      Map<QName, String> result = new HashMap<>();
      NodeList metaTags = metadataElement.getElementsByTagName("meta");

      for (int i = 0; i < metaTags.getLength(); i++) {
         Node metaNode = metaTags.item(i);
         Node property = metaNode.getAttributes().getNamedItem("property");
         if (property != null) {
            String name = property.getNodeValue();
            String value = metaNode.getTextContent();
            result.put(new QName(name), value);
         }
      }

      return result;
   }

   private static Map<String, String> readMetaProperties(Element metadataElement) {
      Map<String, String> result = new HashMap<>();
      NodeList metaTags = metadataElement.getElementsByTagName("meta");

      for (int i = 0; i < metaTags.getLength(); i++) {
         Element metaElement = (Element)metaTags.item(i);
         String name = metaElement.getAttribute("name");
         String value = metaElement.getAttribute("content");
         result.put(name, value);
      }

      return result;
   }

   private static String getBookIdId(Document document) {
      Element packageElement = DOMUtil.getFirstElementByTagNameNS(document.getDocumentElement(), "http://www.idpf.org/2007/opf", "package");
      return packageElement == null ? null : DOMUtil.getAttribute(packageElement, "http://www.idpf.org/2007/opf", "unique-identifier");
   }

   private static List<Author> readCreators(Element metadataElement) {
      return readAuthors("creator", metadataElement);
   }

   private static List<Author> readContributors(Element metadataElement) {
      return readAuthors("contributor", metadataElement);
   }

   private static List<Author> readAuthors(String authorTag, Element metadataElement) {
      NodeList elements = metadataElement.getElementsByTagNameNS("http://purl.org/dc/elements/1.1/", authorTag);
      List<Author> result = new ArrayList<>(elements.getLength());

      for (int i = 0; i < elements.getLength(); i++) {
         Element authorElement = (Element)elements.item(i);
         Author author = createAuthor(authorElement);
         if (author != null) {
            result.add(author);
         }
      }

      return result;
   }

   private static List<Date> readDates(Element metadataElement) {
      NodeList elements = metadataElement.getElementsByTagNameNS("http://purl.org/dc/elements/1.1/", "date");
      List<Date> result = new ArrayList<>(elements.getLength());

      for (int i = 0; i < elements.getLength(); i++) {
         Element dateElement = (Element)elements.item(i);

         try {
            Date date = new Date(DOMUtil.getTextChildrenContent(dateElement), DOMUtil.getAttribute(dateElement, "http://www.idpf.org/2007/opf", "event"));
            result.add(date);
         } catch (IllegalArgumentException var7) {
            var7.printStackTrace();
         }
      }

      return result;
   }

   private static Author createAuthor(Element authorElement) {
      String authorString = DOMUtil.getTextChildrenContent(authorElement);
      if (StringUtil.isBlank(authorString)) {
         return null;
      } else {
         int spacePos = authorString.lastIndexOf(32);
         Author result;
         if (spacePos < 0) {
            result = new Author(authorString);
         } else {
            result = new Author(authorString.substring(0, spacePos), authorString.substring(spacePos + 1));
         }

         result.setRole(DOMUtil.getAttribute(authorElement, "http://www.idpf.org/2007/opf", "role"));
         return result;
      }
   }

   private static List<Identifier> readIdentifiers(Element metadataElement) {
      NodeList identifierElements = metadataElement.getElementsByTagNameNS("http://purl.org/dc/elements/1.1/", "identifier");
      if (identifierElements.getLength() == 0) {
         System.err.println(TAG + " Package does not contain element " + "identifier");
         return new ArrayList<>();
      } else {
         String bookIdId = getBookIdId(metadataElement.getOwnerDocument());
         List<Identifier> result = new ArrayList<>(identifierElements.getLength());

         for (int i = 0; i < identifierElements.getLength(); i++) {
            Element identifierElement = (Element)identifierElements.item(i);
            String schemeName = DOMUtil.getAttribute(identifierElement, "http://www.idpf.org/2007/opf", "scheme");
            String identifierValue = DOMUtil.getTextChildrenContent(identifierElement);
            if (!StringUtil.isBlank(identifierValue)) {
               Identifier identifier = new Identifier(schemeName, identifierValue);
               if (identifierElement.getAttribute("id").equals(bookIdId)) {
                  identifier.setBookId(true);
               }

               result.add(identifier);
            }
         }

         return result;
      }
   }
}
