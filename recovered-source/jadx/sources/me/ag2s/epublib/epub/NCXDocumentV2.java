package me.ag2s.epublib.epub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Identifier;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.TOCReference;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV2.class */
public class NCXDocumentV2 {
    public static final String NAMESPACE_NCX = "http://www.daisy.org/z3986/2005/ncx/";
    public static final String PREFIX_NCX = "ncx";
    public static final String NCX_ITEM_ID = "ncx";
    public static final String DEFAULT_NCX_HREF = "toc.ncx";
    public static final String PREFIX_DTB = "dtb";
    private static final String TAG;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV2$NCXAttributeValues.class */
    private interface NCXAttributeValues {
        public static final String chapter = "chapter";
        public static final String version = "2005-1";
    }

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV2$NCXAttributes.class */
    private interface NCXAttributes {
        public static final String src = "src";
        public static final String name = "name";
        public static final String content = "content";
        public static final String id = "id";
        public static final String playOrder = "playOrder";
        public static final String clazz = "class";
        public static final String version = "version";
    }

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/NCXDocumentV2$NCXTags.class */
    private interface NCXTags {
        public static final String ncx = "ncx";
        public static final String meta = "meta";
        public static final String navPoint = "navPoint";
        public static final String navMap = "navMap";
        public static final String navLabel = "navLabel";
        public static final String content = "content";
        public static final String text = "text";
        public static final String docTitle = "docTitle";
        public static final String docAuthor = "docAuthor";
        public static final String head = "head";
    }

    static {
        $assertionsDisabled = !NCXDocumentV2.class.desiredAssertionStatus();
        TAG = NCXDocumentV2.class.getName();
    }

    public static Resource read(EpubBook book, EpubReader epubReader) {
        Resource ncxResource = null;
        if (book.getSpine().getTocResource() == null) {
            System.err.println(TAG + " Book does not contain a table of contents file");
            return null;
        }
        try {
            ncxResource = book.getSpine().getTocResource();
            if (ncxResource == null) {
                return null;
            }
            System.out.println(TAG + " ncxResource.getHref()" + ncxResource.getHref());
            Document ncxDocument = ResourceUtil.getAsDocument(ncxResource);
            Element navMapElement = DOMUtil.getFirstElementByTagNameNS(ncxDocument.getDocumentElement(), NAMESPACE_NCX, NCXTags.navMap);
            if (navMapElement == null) {
                return null;
            }
            TableOfContents tableOfContents = new TableOfContents(readTOCReferences(navMapElement.getChildNodes(), book));
            book.setTableOfContents(tableOfContents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ncxResource;
    }

    static List<TOCReference> readTOCReferences(NodeList navpoints, EpubBook book) {
        if (navpoints == null) {
            return new ArrayList();
        }
        List<TOCReference> result = new ArrayList<>(navpoints.getLength());
        for (int i = 0; i < navpoints.getLength(); i++) {
            Node node = navpoints.item(i);
            if (node.getNodeType() == 1 && node.getLocalName().equals(NCXTags.navPoint)) {
                TOCReference tocReference = readTOCReference((Element) node, book);
                result.add(tocReference);
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
        List<TOCReference> childTOCReferences = readTOCReferences(navpointElement.getChildNodes(), book);
        result.setChildren(childTOCReferences);
        return result;
    }

    private static String readNavReference(Element navpointElement) {
        Element contentElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, NAMESPACE_NCX, "content");
        if (contentElement == null) {
            return null;
        }
        String result = DOMUtil.getAttribute(contentElement, NAMESPACE_NCX, NCXAttributes.src);
        try {
            result = URLDecoder.decode(result, Constants.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String readNavLabel(Element navpointElement) {
        Element navLabel = DOMUtil.getFirstElementByTagNameNS(navpointElement, NAMESPACE_NCX, NCXTags.navLabel);
        if ($assertionsDisabled || navLabel != null) {
            return DOMUtil.getTextChildrenContent(DOMUtil.getFirstElementByTagNameNS(navLabel, NAMESPACE_NCX, NCXTags.text));
        }
        throw new AssertionError();
    }

    public static void write(EpubWriter epubWriter, EpubBook book, ZipOutputStream resultStream) throws IOException {
        resultStream.putNextEntry(new ZipEntry(book.getSpine().getTocResource().getHref()));
        XmlSerializer out = EpubProcessorSupport.createXmlSerializer(resultStream);
        write(out, book);
        out.flush();
    }

    public static void write(XmlSerializer xmlSerializer, EpubBook book) throws IllegalStateException, IOException, IllegalArgumentException {
        write(xmlSerializer, book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }

    public static Resource createNCXResource(EpubBook book) throws IllegalStateException, IOException, IllegalArgumentException {
        return createNCXResource(book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }

    public static Resource createNCXResource(List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalStateException, IOException, IllegalArgumentException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        XmlSerializer out = EpubProcessorSupport.createXmlSerializer(data);
        write(out, identifiers, title, authors, tableOfContents);
        return new Resource("ncx", data.toByteArray(), DEFAULT_NCX_HREF, MediaTypes.NCX);
    }

    public static void write(XmlSerializer serializer, List<Identifier> identifiers, String title, List<Author> authors, TableOfContents tableOfContents) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startDocument(Constants.CHARACTER_ENCODING, false);
        serializer.setPrefix(PackageDocumentBase.PREFIX_OPF, NAMESPACE_NCX);
        serializer.startTag(NAMESPACE_NCX, "ncx");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "version", NCXAttributeValues.version);
        serializer.startTag(NAMESPACE_NCX, "head");
        for (Identifier identifier : identifiers) {
            writeMetaElement(identifier.getScheme(), identifier.getValue(), serializer);
        }
        writeMetaElement(PackageDocumentBase.OPFValues.generator, Constants.EPUB_GENERATOR_NAME, serializer);
        writeMetaElement("depth", String.valueOf(tableOfContents.calculateDepth()), serializer);
        writeMetaElement("totalPageCount", "0", serializer);
        writeMetaElement("maxPageNumber", "0", serializer);
        serializer.endTag(NAMESPACE_NCX, "head");
        serializer.startTag(NAMESPACE_NCX, NCXTags.docTitle);
        serializer.startTag(NAMESPACE_NCX, NCXTags.text);
        serializer.text(StringUtil.defaultIfNull(title));
        serializer.endTag(NAMESPACE_NCX, NCXTags.text);
        serializer.endTag(NAMESPACE_NCX, NCXTags.docTitle);
        for (Author author : authors) {
            serializer.startTag(NAMESPACE_NCX, NCXTags.docAuthor);
            serializer.startTag(NAMESPACE_NCX, NCXTags.text);
            serializer.text(author.getLastname() + ", " + author.getFirstname());
            serializer.endTag(NAMESPACE_NCX, NCXTags.text);
            serializer.endTag(NAMESPACE_NCX, NCXTags.docAuthor);
        }
        serializer.startTag(NAMESPACE_NCX, NCXTags.navMap);
        writeNavPoints(tableOfContents.getTocReferences(), 1, serializer);
        serializer.endTag(NAMESPACE_NCX, NCXTags.navMap);
        serializer.endTag(NAMESPACE_NCX, "ncx");
        serializer.endDocument();
    }

    private static void writeMetaElement(String dtbName, String content, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startTag(NAMESPACE_NCX, "meta");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "name", "dtb:" + dtbName);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "content", content);
        serializer.endTag(NAMESPACE_NCX, "meta");
    }

    private static int writeNavPoints(List<TOCReference> tocReferences, int playOrder, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
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

    private static void writeNavPointStart(TOCReference tocReference, int playOrder, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startTag(NAMESPACE_NCX, NCXTags.navPoint);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "id", "navPoint-" + playOrder);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, NCXAttributes.playOrder, String.valueOf(playOrder));
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, NCXAttributes.clazz, NCXAttributeValues.chapter);
        serializer.startTag(NAMESPACE_NCX, NCXTags.navLabel);
        serializer.startTag(NAMESPACE_NCX, NCXTags.text);
        serializer.text(tocReference.getTitle());
        serializer.endTag(NAMESPACE_NCX, NCXTags.text);
        serializer.endTag(NAMESPACE_NCX, NCXTags.navLabel);
        serializer.startTag(NAMESPACE_NCX, "content");
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, NCXAttributes.src, tocReference.getCompleteHref());
        serializer.endTag(NAMESPACE_NCX, "content");
    }

    private static void writeNavPointEnd(TOCReference tocReference, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.endTag(NAMESPACE_NCX, NCXTags.navPoint);
    }
}
