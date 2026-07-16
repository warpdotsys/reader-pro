package me.ag2s.epublib.epub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Identifier;
import me.ag2s.epublib.domain.MediaType;
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

public class NCXDocumentV3 {
   public static final String NAMESPACE_XHTML = "http://www.w3.org/1999/xhtml";
   public static final String NAMESPACE_EPUB = "http://www.idpf.org/2007/ops";
   public static final String LANGUAGE = "en";
   public static final String PREFIX_XHTML = "html";
   public static final String NCX_ITEM_ID = "htmltoc";
   public static final String DEFAULT_NCX_HREF = "toc.xhtml";
   public static final String V3_NCX_PROPERTIES = "nav";
   public static final MediaType V3_NCX_MEDIATYPE = MediaTypes.XHTML;
   private static final String TAG = NCXDocumentV3.class.getName();

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

            if (ncxResource.getHref().endsWith(".ncx")) {
               System.err.println(TAG + " 该epub文件不标准，使用了epub2的目录文件");
               return NCXDocumentV2.read(book, epubReader);
            }

            System.out.println(TAG + " " + ncxResource.getHref());
            Document ncxDocument = ResourceUtil.getAsDocument(ncxResource);
            System.out.println(TAG + " " + ncxDocument.getNodeName());
            Element navMapElement = (Element)ncxDocument.getElementsByTagName("nav").item(0);
            if (navMapElement == null) {
               System.out.println(TAG + " epub3目录文件未发现nav节点，尝试使用epub2的规则解析");
               return NCXDocumentV2.read(book, epubReader);
            }

            navMapElement = (Element)navMapElement.getElementsByTagName("ol").item(0);
            System.out.println(TAG + " " + navMapElement.getTagName());
            TableOfContents tableOfContents = new TableOfContents(readTOCReferences(navMapElement.getChildNodes(), book));
            System.out.println(TAG + " " + tableOfContents.toString());
            book.setTableOfContents(tableOfContents);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         return ncxResource;
      }
   }

   private static List<TOCReference> doToc(Node n, EpubBook book) {
      List<TOCReference> result = new ArrayList<>();
      if (n != null && n.getNodeType() == 1) {
         Element el = (Element)n;
         NodeList nodeList = el.getElementsByTagName("li");

         for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(readTOCReference((Element)nodeList.item(i), book));
         }

         return result;
      } else {
         return result;
      }
   }

   static List<TOCReference> readTOCReferences(NodeList navpoints, EpubBook book) {
      if (navpoints == null) {
         return new ArrayList<>();
      } else {
         List<TOCReference> result = new ArrayList<>(navpoints.getLength());

         for (int i = 0; i < navpoints.getLength(); i++) {
            Node node = navpoints.item(i);
            if (node != null && node.getNodeType() == 1) {
               Element el = (Element)node;
               if (el.getTagName().equals("li")) {
                  result.add(readTOCReference(el, book));
               }
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
      List<TOCReference> childTOCReferences = doToc(navpointElement, book);
      result.setChildren(childTOCReferences);
      return result;
   }

   private static String readNavReference(Element navpointElement) {
      Element contentElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "", "a");
      if (contentElement == null) {
         return null;
      } else {
         String result = DOMUtil.getAttribute(contentElement, "", "href");

         try {
            result = URLDecoder.decode(result, "UTF-8");
         } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
         }

         return result;
      }
   }

   private static String readNavLabel(Element navpointElement) {
      Element labelElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "", "a");

      assert labelElement != null;

      String label = labelElement.getTextContent();
      if (StringUtil.isNotBlank(label)) {
         return label;
      } else {
         labelElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "", "span");

         assert labelElement != null;

         return labelElement.getTextContent();
      }
   }

   public static Resource createNCXResource(EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
      return createNCXResource(book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
   }

   public static Resource createNCXResource(List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
      ByteArrayOutputStream data = new ByteArrayOutputStream();
      XmlSerializer out = EpubProcessorSupport.createXmlSerializer(data);
      write(out, identifiers, title, authors, tableOfContents);
      Resource resource = new Resource("htmltoc", data.toByteArray(), "toc.xhtml", V3_NCX_MEDIATYPE);
      resource.setProperties("nav");
      return resource;
   }

   public static void write(XmlSerializer xmlSerializer, EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
      write(xmlSerializer, book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
   }

   public static void write(XmlSerializer serializer, List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
      serializer.startDocument("UTF-8", false);
      serializer.setPrefix("", "http://www.w3.org/1999/xhtml");
      serializer.startTag("http://www.w3.org/1999/xhtml", "html");
      serializer.attribute("", "xmlns:epub", "http://www.idpf.org/2007/ops");
      serializer.attribute("", "xml:lang", "en");
      serializer.attribute("", "lang", "en");
      writeHead(title, serializer);
      serializer.startTag("http://www.w3.org/1999/xhtml", "body");
      serializer.startTag("http://www.w3.org/1999/xhtml", "h1");
      serializer.text(title);
      serializer.endTag("http://www.w3.org/1999/xhtml", "h1");
      serializer.startTag("http://www.w3.org/1999/xhtml", "nav");
      serializer.attribute("", "epub:type", "toc");
      serializer.attribute("", "id", "toc");
      serializer.attribute("", "role", "doc-toc");
      serializer.startTag("http://www.w3.org/1999/xhtml", "h2");
      serializer.text("目录");
      serializer.endTag("http://www.w3.org/1999/xhtml", "h2");
      writeNavPoints(tableOfContents.getTocReferences(), 1, serializer);
      serializer.endTag("http://www.w3.org/1999/xhtml", "nav");
      serializer.endTag("http://www.w3.org/1999/xhtml", "body");
      serializer.endTag("http://www.w3.org/1999/xhtml", "html");
      serializer.endDocument();
   }

   private static int writeNavPoints(List<TOCReference> tocReferences, int playOrder, XmlSerializer serializer) throws IOException {
      writeOlStart(serializer);

      for (TOCReference tocReference : tocReferences) {
         if (tocReference.getResource() == null) {
            playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
         } else {
            writeNavPointStart(tocReference, serializer);
            playOrder++;
            if (!tocReference.getChildren().isEmpty()) {
               playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
            }

            writeNavPointEnd(tocReference, serializer);
         }
      }

      writeOlSEnd(serializer);
      return playOrder;
   }

   private static void writeNavPointStart(TOCReference tocReference, XmlSerializer serializer) throws IOException {
      writeLiStart(serializer);
      String title = tocReference.getTitle();
      String href = tocReference.getCompleteHref();
      if (StringUtil.isNotBlank(href)) {
         writeLabel(title, href, serializer);
      } else {
         writeLabel(title, serializer);
      }
   }

   private static void writeNavPointEnd(TOCReference tocReference, XmlSerializer serializer) throws IOException {
      writeLiEnd(serializer);
   }

   protected static void writeLabel(String title, String href, XmlSerializer serializer) throws IOException {
      serializer.startTag("http://www.w3.org/1999/xhtml", "a");
      serializer.attribute("", "href", href);
      serializer.text(title);
      serializer.endTag("http://www.w3.org/1999/xhtml", "a");
   }

   protected static void writeLabel(String title, XmlSerializer serializer) throws IOException {
      serializer.startTag("http://www.w3.org/1999/xhtml", "span");
      serializer.text(title);
      serializer.endTag("http://www.w3.org/1999/xhtml", "span");
   }

   private static void writeLiStart(XmlSerializer serializer) throws IOException {
      serializer.startTag("http://www.w3.org/1999/xhtml", "li");
      System.out.println(TAG + " writeLiStart");
   }

   private static void writeLiEnd(XmlSerializer serializer) throws IOException {
      serializer.endTag("http://www.w3.org/1999/xhtml", "li");
      System.out.println(TAG + " writeLiEND");
   }

   private static void writeOlStart(XmlSerializer serializer) throws IOException {
      serializer.startTag("http://www.w3.org/1999/xhtml", "ol");
      System.out.println(TAG + " writeOlStart");
   }

   private static void writeOlSEnd(XmlSerializer serializer) throws IOException {
      serializer.endTag("http://www.w3.org/1999/xhtml", "ol");
      System.out.println(TAG + " writeOlEnd");
   }

   private static void writeHead(String title, XmlSerializer serializer) throws IOException {
      serializer.startTag("http://www.w3.org/1999/xhtml", "head");
      serializer.startTag("http://www.w3.org/1999/xhtml", "title");
      serializer.text(StringUtil.defaultIfNull(title));
      serializer.endTag("http://www.w3.org/1999/xhtml", "title");
      serializer.startTag("http://www.w3.org/1999/xhtml", "link");
      serializer.attribute("", "rel", "stylesheet");
      serializer.attribute("", "type", "text/css");
      serializer.attribute("", "href", "css/style.css");
      serializer.endTag("http://www.w3.org/1999/xhtml", "link");
      serializer.startTag("http://www.w3.org/1999/xhtml", "meta");
      serializer.attribute("", "http-equiv", "Content-Type");
      serializer.attribute("", "content", "text/html; charset=utf-8");
      serializer.endTag("http://www.w3.org/1999/xhtml", "meta");
      serializer.endTag("http://www.w3.org/1999/xhtml", "head");
   }

   private interface XHTMLAttributeValues {
      String Content_Type = "Content-Type";
      String HTML_UTF8 = "text/html; charset=utf-8";
      String lang = "en";
      String epub_type = "toc";
      String role_toc = "doc-toc";
   }

   private interface XHTMLAttributes {
      String xmlns = "xmlns";
      String xmlns_epub = "xmlns:epub";
      String lang = "lang";
      String xml_lang = "xml:lang";
      String rel = "rel";
      String type = "type";
      String epub_type = "epub:type";
      String id = "id";
      String role = "role";
      String href = "href";
      String http_equiv = "http-equiv";
      String content = "content";
   }

   private interface XHTMLTgs {
      String html = "html";
      String head = "head";
      String title = "title";
      String meta = "meta";
      String link = "link";
      String body = "body";
      String h1 = "h1";
      String h2 = "h2";
      String nav = "nav";
      String ol = "ol";
      String li = "li";
      String a = "a";
      String span = "span";
   }
}
