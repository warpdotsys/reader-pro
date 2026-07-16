// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import java.util.Iterator;
import me.ag2s.epublib.domain.MediaTypes;
import java.io.ByteArrayOutputStream;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.Identifier;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Node;
import java.util.ArrayList;
import me.ag2s.epublib.domain.TOCReference;
import java.util.List;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.EpubBook;

public class NCXDocumentV2
{
    public static final String NAMESPACE_NCX = "http://www.daisy.org/z3986/2005/ncx/";
    public static final String PREFIX_NCX = "ncx";
    public static final String NCX_ITEM_ID = "ncx";
    public static final String DEFAULT_NCX_HREF = "toc.ncx";
    public static final String PREFIX_DTB = "dtb";
    private static final String TAG;
    
    public static Resource read(final EpubBook book, final EpubReader epubReader) {
        Resource ncxResource = null;
        if (book.getSpine().getTocResource() == null) {
            System.err.println(NCXDocumentV2.TAG + " Book does not contain a table of contents file");
            return null;
        }
        try {
            ncxResource = book.getSpine().getTocResource();
            if (ncxResource == null) {
                return null;
            }
            System.out.println(NCXDocumentV2.TAG + " ncxResource.getHref()" + ncxResource.getHref());
            final Document ncxDocument = ResourceUtil.getAsDocument(ncxResource);
            final Element navMapElement = DOMUtil.getFirstElementByTagNameNS(ncxDocument.getDocumentElement(), "http://www.daisy.org/z3986/2005/ncx/", "navMap");
            if (navMapElement == null) {
                return null;
            }
            final TableOfContents tableOfContents = new TableOfContents(readTOCReferences(navMapElement.getChildNodes(), book));
            book.setTableOfContents(tableOfContents);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return ncxResource;
    }
    
    static List<TOCReference> readTOCReferences(final NodeList navpoints, final EpubBook book) {
        if (navpoints == null) {
            return new ArrayList<TOCReference>();
        }
        final List<TOCReference> result = new ArrayList<TOCReference>(navpoints.getLength());
        for (int i = 0; i < navpoints.getLength(); ++i) {
            final Node node = navpoints.item(i);
            if (node.getNodeType() == 1) {
                if (node.getLocalName().equals("navPoint")) {
                    final TOCReference tocReference = readTOCReference((Element)node, book);
                    result.add(tocReference);
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
            System.err.println(NCXDocumentV2.TAG + " Resource with href " + href + " in NCX document not found");
        }
        System.out.println(NCXDocumentV2.TAG + " label:" + label);
        System.out.println(NCXDocumentV2.TAG + " href:" + href);
        System.out.println(NCXDocumentV2.TAG + " fragmentId:" + fragmentId);
        final TOCReference result = new TOCReference(label, resource, fragmentId);
        final List<TOCReference> childTOCReferences = readTOCReferences(navpointElement.getChildNodes(), book);
        result.setChildren(childTOCReferences);
        return result;
    }
    
    private static String readNavReference(final Element navpointElement) {
        final Element contentElement = DOMUtil.getFirstElementByTagNameNS(navpointElement, "http://www.daisy.org/z3986/2005/ncx/", "content");
        if (contentElement == null) {
            return null;
        }
        String result = DOMUtil.getAttribute(contentElement, "http://www.daisy.org/z3986/2005/ncx/", "src");
        try {
            result = URLDecoder.decode(result, "UTF-8");
        }
        catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private static String readNavLabel(final Element navpointElement) {
        final Element navLabel = DOMUtil.getFirstElementByTagNameNS(navpointElement, "http://www.daisy.org/z3986/2005/ncx/", "navLabel");
        assert navLabel != null;
        return DOMUtil.getTextChildrenContent(DOMUtil.getFirstElementByTagNameNS(navLabel, "http://www.daisy.org/z3986/2005/ncx/", "text"));
    }
    
    public static void write(final EpubWriter epubWriter, final EpubBook book, final ZipOutputStream resultStream) throws IOException {
        resultStream.putNextEntry(new ZipEntry(book.getSpine().getTocResource().getHref()));
        final XmlSerializer out = EpubProcessorSupport.createXmlSerializer(resultStream);
        write(out, book);
        out.flush();
    }
    
    public static void write(final XmlSerializer xmlSerializer, final EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
        write(xmlSerializer, book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }
    
    public static Resource createNCXResource(final EpubBook book) throws IllegalArgumentException, IllegalStateException, IOException {
        return createNCXResource(book.getMetadata().getIdentifiers(), book.getTitle(), book.getMetadata().getAuthors(), book.getTableOfContents());
    }
    
    public static Resource createNCXResource(final List<Identifier> identifiers, final String title, final List<Author> authors, final TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        final XmlSerializer out = EpubProcessorSupport.createXmlSerializer(data);
        write(out, identifiers, title, authors, tableOfContents);
        return new Resource("ncx", data.toByteArray(), "toc.ncx", MediaTypes.NCX);
    }
    
    public static void write(final XmlSerializer serializer, final List<Identifier> identifiers, final String title, final List<Author> authors, final TableOfContents tableOfContents) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startDocument("UTF-8", Boolean.valueOf(false));
        serializer.setPrefix("", "http://www.daisy.org/z3986/2005/ncx/");
        serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "ncx");
        serializer.attribute("", "version", "2005-1");
        serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "head");
        for (final Identifier identifier : identifiers) {
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
        for (final Author author : authors) {
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
    
    private static void writeMetaElement(final String dtbName, final String content, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("http://www.daisy.org/z3986/2005/ncx/", "meta");
        serializer.attribute("", "name", "dtb:" + dtbName);
        serializer.attribute("", "content", content);
        serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "meta");
    }
    
    private static int writeNavPoints(final List<TOCReference> tocReferences, int playOrder, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        for (final TOCReference tocReference : tocReferences) {
            if (tocReference.getResource() == null) {
                playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
            }
            else {
                writeNavPointStart(tocReference, playOrder, serializer);
                ++playOrder;
                if (!tocReference.getChildren().isEmpty()) {
                    playOrder = writeNavPoints(tocReference.getChildren(), playOrder, serializer);
                }
                writeNavPointEnd(tocReference, serializer);
            }
        }
        return playOrder;
    }
    
    private static void writeNavPointStart(final TOCReference tocReference, final int playOrder, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
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
    
    private static void writeNavPointEnd(final TOCReference tocReference, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.endTag("http://www.daisy.org/z3986/2005/ncx/", "navPoint");
    }
    
    static {
        TAG = NCXDocumentV2.class.getName();
    }
    
    private interface NCXAttributeValues
    {
        public static final String chapter = "chapter";
        public static final String version = "2005-1";
    }
    
    private interface NCXAttributes
    {
        public static final String src = "src";
        public static final String name = "name";
        public static final String content = "content";
        public static final String id = "id";
        public static final String playOrder = "playOrder";
        public static final String clazz = "class";
        public static final String version = "version";
    }
    
    private interface NCXTags
    {
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
}
