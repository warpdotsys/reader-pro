package me.ag2s.epublib.epub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import me.ag2s.epublib.Constants;
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

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV3.class */
public class NCXDocumentV3 {
    public static final String NAMESPACE_XHTML = "http://www.w3.org/1999/xhtml";
    public static final String NAMESPACE_EPUB = "http://www.idpf.org/2007/ops";
    public static final String LANGUAGE = "en";
    public static final String PREFIX_XHTML = "html";
    public static final String NCX_ITEM_ID = "htmltoc";
    public static final String DEFAULT_NCX_HREF = "toc.xhtml";
    public static final String V3_NCX_PROPERTIES = "nav";
    public static final MediaType V3_NCX_MEDIATYPE;
    private static final String TAG;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV3$XHTMLAttributeValues.class */
    private interface XHTMLAttributeValues {
        public static final String Content_Type = "Content-Type";
        public static final String HTML_UTF8 = "text/html; charset=utf-8";
        public static final String lang = "en";
        public static final String epub_type = "toc";
        public static final String role_toc = "doc-toc";
    }

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV3$XHTMLAttributes.class */
    private interface XHTMLAttributes {
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

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV3$XHTMLTgs.class */
    private interface XHTMLTgs {
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

    static {
        $assertionsDisabled = !NCXDocumentV3.class.desiredAssertionStatus();
        V3_NCX_MEDIATYPE = MediaTypes.XHTML;
        TAG = NCXDocumentV3.class.getName();
    }

    public static Resource read(EpubBook book, EpubReader epubReader) {
        Resource ncxResource = null;
        if (book.getSpine().getTocResource() == null) {
            System.err.println(TAG + " Book does not contain a table of contents file");
            return null;
        }
        try {
            ncxResource = book.getSpine().getTocResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Element navMapElement = (Element) ncxDocument.getElementsByTagName("nav").item(0);
        if (navMapElement == null) {
            System.out.println(TAG + " epub3目录文件未发现nav节点，尝试使用epub2的规则解析");
            return NCXDocumentV2.read(book, epubReader);
        }
        Element navMapElement2 = (Element) navMapElement.getElementsByTagName(XHTMLTgs.ol).item(0);
        System.out.println(TAG + " " + navMapElement2.getTagName());
        TableOfContents tableOfContents = new TableOfContents(readTOCReferences(navMapElement2.getChildNodes(), book));
        System.out.println(TAG + " " + tableOfContents.toString());
        book.setTableOfContents(tableOfContents);
        return ncxResource;
    }

    private static List<TOCReference> doToc(Node n, EpubBook book) {
        List<TOCReference> result = new ArrayList<>();
        if (n == null || n.getNodeType() != 1) {
            return result;
        }
        Element el = (Element) n;
        NodeList nodeList = el.getElementsByTagName(XHTMLTgs.li);
        for (int i = 0; i < nodeList.getLength(); i++) {
            result.add(readTOCReference((Element) nodeList.item(i), book));
        }
        return result;
    }

    static List<TOCReference> readTOCReferences(NodeList navpoints, EpubBook book) {
        if (navpoints == null) {
            return new ArrayList();
        }
        List<TOCReference> result = new ArrayList<>(navpoints.getLength());
        for (int i = 0; i < navpoints.getLength(); i++) {
            Node node = navpoints.item(i);
            if (node != null && node.getNodeType() == 1) {
                Element el = (Element) node;
                if (el.getTagName().equals(XHTMLTgs.li)) {
                    result.add(readTOCReference(el, book));
                }
            }
        }
        return result;
    }

    static TOCReference readTOCReference(Element navpointElement, EpubBook book) {
        String tocResourceRoot;
        String label = readNavLabel(navpointElement);
        String tocResourceRoot2 = StringUtil.substringBeforeLast(book.getSpine().getTocResource().getHref(), '/');
        if (tocResourceRoot2.length() == book.getSpine().getTocResource().getHref().length()) {
            tocResourceRoot = PackageDocumentBase.PREFIX_OPF;
        } else {
            tocResourceRoot = tocResourceRoot2 + TableOfContents.DEFAULT_PATH_SEPARATOR;
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
        Element contentElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, PackageDocumentBase.PREFIX_OPF, XHTMLTgs.a);
        if (contentElement == null) {
            return null;
        }
        String result = DOMUtil.getAttribute(contentElement, PackageDocumentBase.PREFIX_OPF, "href");
        try {
            result = URLDecoder.decode(result, Constants.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String readNavLabel(Element navpointElement) {
        Element labelElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, PackageDocumentBase.PREFIX_OPF, XHTMLTgs.a);
        if (!$assertionsDisabled && labelElement == null) {
            throw new AssertionError();
        }
        String label = labelElement.getTextContent();
        if (StringUtil.isNotBlank(label)) {
            return label;
        }
        Element labelElement2 = DOMUtil.getFirstElementByTagNameNS(navpointElement, PackageDocumentBase.PREFIX_OPF, XHTMLTgs.span);
        if ($assertionsDisabled || labelElement2 != null) {
            return labelElement2.getTextContent();
        }
        throw new AssertionError();
    }

    public static Resource createNCXResource(EpubBook book) throws IllegalStateException, IOException, IllegalArgumentException {
        return createNCXResource(book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }

    public static Resource createNCXResource(List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalStateException, IOException, IllegalArgumentException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        XmlSerializer out = EpubProcessorSupport.createXmlSerializer(data);
        write(out, identifiers, title, authors, tableOfContents);
        Resource resource = new Resource(NCX_ITEM_ID, data.toByteArray(), DEFAULT_NCX_HREF, V3_NCX_MEDIATYPE);
        resource.setProperties("nav");
        return resource;
    }

    public static void write(XmlSerializer xmlSerializer, EpubBook book) throws IllegalStateException, IOException, IllegalArgumentException {
        write(xmlSerializer, book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }

    public static void write(XmlSerializer serializer, List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startDocument(Constants.CHARACTER_ENCODING, false);
        serializer.setPrefix(PackageDocumentBase.PREFIX_OPF, "http://www.w3.org/1999/xhtml");
        serializer.startTag("http://www.w3.org/1999/xhtml", "html");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, XHTMLAttributes.xmlns_epub, NAMESPACE_EPUB);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, XHTMLAttributes.xml_lang, "en");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, XHTMLAttributes.lang, "en");
        writeHead(title, serializer);
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.body);
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.h1);
        serializer.text(title);
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.h1);
        serializer.startTag("http://www.w3.org/1999/xhtml", "nav");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, XHTMLAttributes.epub_type, "toc");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "id", "toc");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "role", XHTMLAttributeValues.role_toc);
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.h2);
        serializer.text("目录");
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.h2);
        writeNavPoints(tableOfContents.getTocReferences(), 1, serializer);
        serializer.endTag("http://www.w3.org/1999/xhtml", "nav");
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.body);
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
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.a);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "href", href);
        serializer.text(title);
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.a);
    }

    protected static void writeLabel(String title, XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.span);
        serializer.text(title);
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.span);
    }

    private static void writeLiStart(XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.li);
        System.out.println(TAG + " writeLiStart");
    }

    private static void writeLiEnd(XmlSerializer serializer) throws IOException {
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.li);
        System.out.println(TAG + " writeLiEND");
    }

    private static void writeOlStart(XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", XHTMLTgs.ol);
        System.out.println(TAG + " writeOlStart");
    }

    private static void writeOlSEnd(XmlSerializer serializer) throws IOException {
        serializer.endTag("http://www.w3.org/1999/xhtml", XHTMLTgs.ol);
        System.out.println(TAG + " writeOlEnd");
    }

    private static void writeHead(String title, XmlSerializer serializer) throws IOException {
        serializer.startTag("http://www.w3.org/1999/xhtml", "head");
        serializer.startTag("http://www.w3.org/1999/xhtml", "title");
        serializer.text(StringUtil.defaultIfNull(title));
        serializer.endTag("http://www.w3.org/1999/xhtml", "title");
        serializer.startTag("http://www.w3.org/1999/xhtml", "link");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, XHTMLAttributes.rel, "stylesheet");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "type", "text/css");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "href", "css/style.css");
        serializer.endTag("http://www.w3.org/1999/xhtml", "link");
        serializer.startTag("http://www.w3.org/1999/xhtml", "meta");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, XHTMLAttributes.http_equiv, XHTMLAttributeValues.Content_Type);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "content", XHTMLAttributeValues.HTML_UTF8);
        serializer.endTag("http://www.w3.org/1999/xhtml", "meta");
        serializer.endTag("http://www.w3.org/1999/xhtml", "head");
    }
}
