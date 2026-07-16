package me.ag2s.epublib.epub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Identifier;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.TOCReference;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

public class NCXDocumentV2 {
   public static final String NAMESPACE_NCX = "http://www.daisy.org/z3986/2005/ncx/";
   public static final String PREFIX_NCX = "ncx";
   public static final String NCX_ITEM_ID = "ncx";
   public static final String DEFAULT_NCX_HREF = "toc.ncx";
   public static final String PREFIX_DTB = "dtb";
   private static final String TAG = NCXDocumentV2.class.getName();

   public static Resource read(EpubBook book, EpubReader epubReader) {
      Resource ncxResource = null;
      if (book.getSpine().getTocResource() == null) {
         System.err.println(TAG + " Book does not contain a table of contents file");
         return null;
      } else {
         try {
            ncxResource = book.getSpine().getTocResource();
            if (ncxResource == null) {
               return null;
            }

            System.out.println(TAG + " ncxResource.getHref()" + ncxResource.getHref());
            Document ncxDocument = ResourceUtil.getAsDocument(ncxResource);
            Element navMapElement = DOMUtil.getFirstElementByTagNameNS(ncxDocument.getDocumentElement(), "http://www.daisy.org/z3986/2005/ncx/", "navMap");
            if (navMapElement == null) {
               return null;
            }

            TableOfContents tableOfContents = new TableOfContents(readTOCReferences(navMapElement.getChildNodes(), book));
            book.setTableOfContents(tableOfContents);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         return ncxResource;
      }
   }

   static List<TOCReference> readTOCReferences(NodeList navpoints, EpubBook book) {
      if (navpoints == null) {
         return new ArrayList<>();
      } else {
         List<TOCReference> result = new ArrayList<>(navpoints.getLength());

         for (int i = 0; i < navpoints.getLength(); i++) {
            Node node = navpoints.item(i);
            if (node.getNodeType() == 1 && node.getLocalName().equals("navPoint")) {
               TOCReference tocReference = readTOCReference((Element)node, book);
               result.add(tocReference);
            }
         }

         return result;
      }
   }

   static TOCReference readTOCReference(Element navpointElement, EpubBook book) {
      String label = readNavLabel(navpointElement);
      String tocResourceRoot = StringUtil.substringBeforeLast(book.getSpine().getTocResource().getHref(), '/');
      if (tocResourceRoot.length() == book.getSpine().getTocResource().getHref().length()) {
         tocResourceRoot = "";
      } else {
         tocResourceRoot = tocResourceRoot + "/";
      }

      String reference = StringUtil.collapsePathDots(tocResourceRoot + readNavReference(navpointElement));
      String href = StringUtil.substringBefore(reference, '#');
      String fragmentId = StringUtil.substringAfter(reference, '#');
      Resource resource = book.getResources().getByHref(href);
      if (resource == null) {
         System.err.println(TAG + " Resource with href " + href + " in NCX document not found");
      }

      System.out.println(TAG + " label:" + label);
      System.out.println(TAG + " href:" + href);
      System.out.println(TAG + " fragmentId:" + fragmentId);
      TOCReference result = new TOCReference(label, resource, fragmentId);
      List<TOCReference> childTOCReferences = readTOCReferences(navpointElement.getChildNodes(), book);
      result.setChildren(childTOCReferences);
      return result;
   }

   private static String readNavReference(Element navpointElement) {
      Element contentElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "http://www.daisy.org/z3986/2005/ncx/", "content");
      if (contentElement == null) {
         return null;
      } else {
         String result = DOMUtil.getAttribute(contentElement, "http://www.daisy.org/z3986/2005/ncx/", "src");

         try {
            result = URLDecoder.decode(result, "UTF-8");
         } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
         }

         return result;
      }
   }

   private static String readNavLabel(Element navpointElement) {
      Element navLabel = DOMUtil.getFirstElementByTagNameNS(navpointElement, "http://www.daisy.org/z3986/2005/ncx/", "navLabel");

      assert navLabel != null;

      return DOMUtil.getTextChildrenContent(DOMUtil.getFirstElementByTagNameNS(navLabel, "http://www.daisy.org/z3986/2005/ncx/", "text"));
   }

   public static void write(EpubWriter epubWriter, EpubBook book, ZipOutputStream resultStream) throws IOException {
      resultStream.putNextEntry(new ZipEntry(book.getSpine().getTocResource().getHref()));
      XmlSerializer out = EpubProcessorSupport.createXmlSerializer(resultStream);
      write(out, book);
      out.flush();
   }

   public static void write(XmlSerializer xmlSerializer, EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
      write(xmlSerializer, book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
   }

   public static Resource createNCXResource(EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
      return createNCXResource(book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
   }

   public static Resource createNCXResource(List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
      ByteArrayOutputStream data = new ByteArrayOutputStream();
      XmlSerializer out = EpubProcessorSupport.createXmlSerializer(data);
      write(out, identifiers, title, authors, tableOfContents);
      return new Resource("ncx", data.toByteArray(), "toc.ncx", MediaTypes.NCX);
   }

   public static void write(XmlSerializer serializer, List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
      serializer.startDocument("UTF-8", false);
      serializer.setPrefix("", "http://www.daisy.org/z3986/2005/ncx/");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "ncx");
      serializer.attribute("", "version", "2005-1");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "head");

      for (Identifier identifier : identifiers) {
         writeMetaElement(identifier.getScheme(), identifier.getValue(), serializer);
      }

      writeMetaElement("generator", "Ag2S EpubLib", serializer);
      writeMetaElement("depth", String.valueOf(tableOfContents.calculateDepth()), serializer);
      writeMetaElement("totalPageCount", "0", serializer);
      writeMetaElement("maxPageNumber", "0", serializer);
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "head");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "docTitle");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "text");
      serializer.text(StringUtil.defaultIfNull(title));
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "text");
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "docTitle");

      for (Author author : authors) {
         serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "docAuthor");
         serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "text");
         serializer.text(author.getLastname() + ", " + author.getFirstname());
         serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "text");
         serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "docAuthor");
      }

      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "navMap");
      writeNavPoints(tableOfContents.getTocReferences(), 1, serializer);
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "navMap");
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "ncx");
      serializer.endDocument();
   }

   private static void writeMetaElement(String dtbName, String content, XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "meta");
      serializer.attribute("", "name", "dtb:" + dtbName);
      serializer.attribute("", "content", content);
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "meta");
   }

   private static int writeNavPoints(List<TOCReference> tocReferences, int playOrder, XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
      for (TOCReference tocReference : tocReferences) {
         if (tocReference.getResource() == null) {
            playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
         } else {
            writeNavPointStart(tocReference, playOrder, serializer);
            playOrder++;
            if (!tocReference.getChildren().isEmpty()) {
               playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
            }

            writeNavPointEnd(tocReference, serializer);
         }
      }

      return playOrder;
   }

   private static void writeNavPointStart(TOCReference tocReference, int playOrder, XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "navPoint");
      serializer.attribute("", "id", "navPoint-" + playOrder);
      serializer.attribute("", "playOrder", String.valueOf(playOrder));
      serializer.attribute("", "class", "chapter");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "navLabel");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "text");
      serializer.text(tocReference.getTitle());
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "text");
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "navLabel");
      serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "content");
      serializer.attribute("", "src", tocReference.getCompleteHref());
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "content");
   }

   private static void writeNavPointEnd(TOCReference tocReference, XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
      serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "navPoint");
   }

   private interface NCXAttributeValues {
      String chapter = "chapter";
      String version = "2005-1";
   }

   private interface NCXAttributes {
      String src = "src";
      String name = "name";
      String content = "content";
      String id = "id";
      String playOrder = "playOrder";
      String clazz = "class";
      String version = "version";
   }

   private interface NCXTags {
      String ncx = "ncx";
      String meta = "meta";
      String navPoint = "navPoint";
      String navMap = "navMap";
      String navLabel = "navLabel";
      String content = "content";
      String text = "text";
      String docTitle = "docTitle";
      String docAuthor = "docAuthor";
      String head = "head";
   }
}
