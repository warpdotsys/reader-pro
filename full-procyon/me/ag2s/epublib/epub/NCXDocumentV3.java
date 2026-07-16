// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.MediaTypes;
import java.util.Iterator;
import org.xmlpull.v1.XmlSerializer;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.Identifier;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import me.ag2s.epublib.domain.TOCReference;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import me.ag2s.epublib.domain.TableOfContents;
import org.w3c.dom.Element;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.MediaType;

public class NCXDocumentV3
{
    public static final String NAMESPACE_XHTML = "http://www.w3.org/1999/xhtml";
    public static final String NAMESPACE_EPUB = "http://www.idpf.org/2007/ops";
    public static final String LANGUAGE = "en";
    public static final String PREFIX_XHTML = "html";
    public static final String NCX_ITEM_ID = "htmltoc";
    public static final String DEFAULT_NCX_HREF = "toc.xhtml";
    public static final String V3_NCX_PROPERTIES = "nav";
    public static final MediaType V3_NCX_MEDIATYPE;
    private static final String TAG;
    
    public static Resource read(final EpubBook book, final EpubReader epubReader) {
        Resource ncxResource = null;
        if (book.getSpine().getTocResource() == null) {
            System.err.println(NCXDocumentV3.TAG + " Book does not contain a table of contents file");
            return null;
        }
        try {
            ncxResource = book.getSpine().getTocResource();
            if (ncxResource == null) {
                return null;
            }
            if (ncxResource.getHref().endsWith(".ncx")) {
                System.err.println(NCXDocumentV3.TAG + " \u8be5epub\u6587\u4ef6\u4e0d\u6807\u51c6\uff0c\u4f7f\u7528\u4e86epub2\u7684\u76ee\u5f55\u6587\u4ef6");
                return NCXDocumentV2.read(book, epubReader);
            }
            System.out.println(NCXDocumentV3.TAG + " " + ncxResource.getHref());
            final Document ncxDocument = ResourceUtil.getAsDocument(ncxResource);
            System.out.println(NCXDocumentV3.TAG + " " + ncxDocument.getNodeName());
            Element navMapElement = (Element)ncxDocument.getElementsByTagName("nav").item(0);
            if (navMapElement == null) {
                System.out.println(NCXDocumentV3.TAG + " epub3\u76ee\u5f55\u6587\u4ef6\u672a\u53d1\u73b0nav\u8282\u70b9\uff0c\u5c1d\u8bd5\u4f7f\u7528epub2\u7684\u89c4\u5219\u89e3\u6790");
                return NCXDocumentV2.read(book, epubReader);
            }
            navMapElement = (Element)navMapElement.getElementsByTagName("ol").item(0);
            System.out.println(NCXDocumentV3.TAG + " " + navMapElement.getTagName());
            final TableOfContents tableOfContents = new TableOfContents(readTOCReferences(navMapElement.getChildNodes(), book));
            System.out.println(NCXDocumentV3.TAG + " " + tableOfContents.toString());
            book.setTableOfContents(tableOfContents);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return ncxResource;
    }
    
    private static List<TOCReference> doToc(final Node n, final EpubBook book) {
        final List<TOCReference> result = new ArrayList<TOCReference>();
        if (n == null || n.getNodeType() != 1) {
            return result;
        }
        final Element el = (Element)n;
        final NodeList nodeList = el.getElementsByTagName("li");
        for (int i = 0; i < nodeList.getLength(); ++i) {
            result.add(readTOCReference((Element)nodeList.item(i), book));
        }
        return result;
    }
    
    static List<TOCReference> readTOCReferences(final NodeList navpoints, final EpubBook book) {
        if (navpoints == null) {
            return new ArrayList<TOCReference>();
        }
        final List<TOCReference> result = new ArrayList<TOCReference>(navpoints.getLength());
        for (int i = 0; i < navpoints.getLength(); ++i) {
            final Node node = navpoints.item(i);
            if (node != null) {
                if (node.getNodeType() == 1) {
                    final Element el = (Element)node;
                    if (el.getTagName().equals("li")) {
                        result.add(readTOCReference(el, book));
                    }
                }
            }
        }
        return result;
    }
    
    static TOCReference readTOCReference(final Element navpointElement, final EpubBook book) {
        final String label = readNavLabel(navpointElement);
        String tocResourceRoot = StringUtil.substringBeforeLast(book.getSpine().getTocResource().getHref(), '/');
        if (tocResourceRoot.length() == book.getSpine().getTocResource().getHref().length()) {
            tocResourceRoot = "";
        }
        else {
            tocResourceRoot += "/";
        }
        final String reference = StringUtil.collapsePathDots(tocResourceRoot + readNavReference(navpointElement));
        final String href = StringUtil.substringBefore(reference, '#');
        final String fragmentId = StringUtil.substringAfter(reference, '#');
        final Resource resource = book.getResources().getByHref(href);
        if (resource == null) {
            System.err.println(NCXDocumentV3.TAG + " Resource with href " + href + " in NCX document not found");
        }
        System.out.println(NCXDocumentV3.TAG + " label:" + label);
        System.out.println(NCXDocumentV3.TAG + " href:" + href);
        System.out.println(NCXDocumentV3.TAG + " fragmentId:" + fragmentId);
        final TOCReference result = new TOCReference(label, resource, fragmentId);
        final List<TOCReference> childTOCReferences = doToc(navpointElement, book);
        result.setChildren(childTOCReferences);
        return result;
    }
    
    private static String readNavReference(final Element navpointElement) {
        final Element contentElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "", "a");
        if (contentElement == null) {
            return null;
        }
        String result = DOMUtil.getAttribute(contentElement, "", "href");
        try {
            result = URLDecoder.decode(result, "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private static String readNavLabel(final Element navpointElement) {
        Element labelElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "", "a");
        assert labelElement != null;
        String label = labelElement.getTextContent();
        if (StringUtil.isNotBlank(label)) {
            return label;
        }
        labelElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "", "span");
        assert labelElement != null;
        label = labelElement.getTextContent();
        return label;
    }
    
    public static Resource createNCXResource(final EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
        return createNCXResource(book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }
    
    public static Resource createNCXResource(final List<Identifier> identifiers, final String title, final List<Author> authors, final TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        final XmlSerializer out = EpubProcessorSupport.createXmlSerializer(data);
        write(out, identifiers, title, authors, tableOfContents);
        final Resource resource = new Resource("htmltoc", data.toByteArray(), "toc.xhtml", NCXDocumentV3.V3_NCX_MEDIATYPE);
        resource.setProperties("nav");
        return resource;
    }
    
    public static void write(final XmlSerializer xmlSerializer, final EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
        write(xmlSerializer, book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }
    
    public static void write(final XmlSerializer serializer, final List<Identifier> identifiers, final String title, final List<Author> authors, final TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startDocument("UTF-8", Boolean.valueOf(false));
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
        serializer.text("\u76ee\u5f55");
        serializer.endTag("http://www.w3.org/1999/xhtml", "h2");
        writeNavPoints(tableOfContents.getTocReferences(), 1, serializer);
        serializer.endTag("http://www.w3.org/1999/xhtml", "nav");
        serializer.endTag("http://www.w3.org/1999/xhtml", "body");
        serializer.endTag("http://www.w3.org/1999/xhtml", "html");
        serializer.endDocument();
    }
    
    private static int writeNavPoints(final List<TOCReference> tocReferences, int playOrder, final XmlSerializer serializer) throws IOException {
        writeOlStart(serializer);
        for (final TOCReference tocReference : tocReferences) {
            if (tocReference.getResource() == null) {
                playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
            }
            else {
                writeNavPointStart(tocReference, serializer);
                ++playOrder;
                if (!tocReference.getChildren().isEmpty()) {
                    playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
                }
                writeNavPointEnd(tocReference, serializer);
            }
        }
        writeOlSEnd(serializer);
        return playOrder;
    }
    
    private static void writeNavPointStart(final TOCReference tocReference, final XmlSerializer serializer) throws IOException {
        writeLiStart(serializer);
        final String title = tocReference.getTitle();
        final String href = tocReference.getCompleteHref();
        if (StringUtil.isNotBlank(href)) {
            writeLabel(title, href, serializer);
        }
        else {
            writeLabel(title, serializer);
        }
    }
    
    private static void writeNavPointEnd(final TOCReference tocReference, final XmlSerializer serializer) throws IOException {
        writeLiEnd(serializer);
    }
    
    protected static void writeLabel(final String title, final String href, final XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", "a");
        serializer.attribute("", "href", href);
        serializer.text(title);
        serializer.endTag("http://www.w3.org/1999/xhtml", "a");
    }
    
    protected static void writeLabel(final String title, final XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", "span");
        serializer.text(title);
        serializer.endTag("http://www.w3.org/1999/xhtml", "span");
    }
    
    private static void writeLiStart(final XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", "li");
        System.out.println(NCXDocumentV3.TAG + " writeLiStart");
    }
    
    private static void writeLiEnd(final XmlSerializer serializer) throws IOException {
        serializer.endTag("http://www.w3.org/1999/xhtml", "li");
        System.out.println(NCXDocumentV3.TAG + " writeLiEND");
    }
    
    private static void writeOlStart(final XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", "ol");
        System.out.println(NCXDocumentV3.TAG + " writeOlStart");
    }
    
    private static void writeOlSEnd(final XmlSerializer serializer) throws IOException {
        serializer.endTag("http://www.w3.org/1999/xhtml", "ol");
        System.out.println(NCXDocumentV3.TAG + " writeOlEnd");
    }
    
    private static void writeHead(final String title, final XmlSerializer serializer) throws IOException {
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
    
    static {
        V3_NCX_MEDIATYPE = MediaTypes.XHTML;
        TAG = NCXDocumentV3.class.getName();
    }
    
    private interface XHTMLAttributeValues
    {
        public static final String Content_Type = "Content-Type";
        public static final String HTML_UTF8 = "text/html; charset=utf-8";
        public static final String lang = "en";
        public static final String epub_type = "toc";
        public static final String role_toc = "doc-toc";
    }
    
    private interface XHTMLAttributes
    {
        public static final String xmlns = "xmlns";
        public static final String xmlns_epub = "xmlns:epub";
        public static final String lang = "lang";
        public static final String xml_lang = "xml:lang";
        public static final String rel = "rel";
        public static final String type = "type";
        public static final String epub_type = "epub:type";
        public static final String id = "id";
        public static final String role = "role";
        public static final String href = "href";
        public static final String http_equiv = "http-equiv";
        public static final String content = "content";
    }
    
    private interface XHTMLTgs
    {
        public static final String html = "html";
        public static final String head = "head";
        public static final String title = "title";
        public static final String meta = "meta";
        public static final String link = "link";
        public static final String body = "body";
        public static final String h1 = "h1";
        public static final String h2 = "h2";
        public static final String nav = "nav";
        public static final String ol = "ol";
        public static final String li = "li";
        public static final String a = "a";
        public static final String span = "span";
    }
}
