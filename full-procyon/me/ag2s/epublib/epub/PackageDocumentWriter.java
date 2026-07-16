// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.Guide;
import me.ag2s.epublib.domain.GuideReference;
import me.ag2s.epublib.domain.SpineReference;
import me.ag2s.epublib.domain.Spine;
import me.ag2s.epublib.util.StringUtil;
import me.ag2s.epublib.domain.MediaTypes;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import me.ag2s.epublib.domain.Resource;
import java.io.IOException;
import me.ag2s.epublib.domain.EpubBook;
import org.xmlpull.v1.XmlSerializer;

public class PackageDocumentWriter extends PackageDocumentBase
{
    private static final String TAG;
    
    public static void write(final EpubWriter epubWriter, final XmlSerializer serializer, final EpubBook book) {
        try {
            serializer.startDocument("UTF-8", Boolean.valueOf(false));
            serializer.setPrefix("", "http://www.idpf.org/2007/opf");
            serializer.setPrefix("dc", "http://purl.org/dc/elements/1.1/");
            serializer.startTag("http://www.idpf.org/2007/opf", "package");
            serializer.attribute("", "version", book.getVersion());
            serializer.attribute("", "unique-identifier", "duokan-book-id");
            PackageDocumentMetadataWriter.writeMetaData(book, serializer);
            writeManifest(book, epubWriter, serializer);
            writeSpine(book, epubWriter, serializer);
            writeGuide(book, epubWriter, serializer);
            serializer.endTag("http://www.idpf.org/2007/opf", "package");
            serializer.endDocument();
            serializer.flush();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void writeSpine(final EpubBook book, final EpubWriter epubWriter, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("http://www.idpf.org/2007/opf", "spine");
        final Resource tocResource = book.getSpine().getTocResource();
        final String tocResourceId = tocResource.getId();
        serializer.attribute("", "toc", tocResourceId);
        if (book.getCoverPage() != null && book.getSpine().findFirstResourceById(book.getCoverPage().getId()) < 0) {
            serializer.startTag("http://www.idpf.org/2007/opf", "itemref");
            serializer.attribute("", "idref", book.getCoverPage().getId());
            serializer.attribute("", "linear", "no");
            serializer.endTag("http://www.idpf.org/2007/opf", "itemref");
        }
        writeSpineItems(book.getSpine(), serializer);
        serializer.endTag("http://www.idpf.org/2007/opf", "spine");
    }
    
    private static void writeManifest(final EpubBook book, final EpubWriter epubWriter, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("http://www.idpf.org/2007/opf", "manifest");
        serializer.startTag("http://www.idpf.org/2007/opf", "item");
        if (book.isEpub3()) {
            serializer.attribute("", "properties", "nav");
            serializer.attribute("", "id", "htmltoc");
            serializer.attribute("", "href", "toc.xhtml");
            serializer.attribute("", "media-type", NCXDocumentV3.V3_NCX_MEDIATYPE.getName());
        }
        else {
            serializer.attribute("", "id", epubWriter.getNcxId());
            serializer.attribute("", "href", epubWriter.getNcxHref());
            serializer.attribute("", "media-type", epubWriter.getNcxMediaType());
        }
        serializer.endTag("http://www.idpf.org/2007/opf", "item");
        for (final Resource resource : getAllResourcesSortById(book)) {
            writeItem(book, resource, serializer);
        }
        serializer.endTag("http://www.idpf.org/2007/opf", "manifest");
    }
    
    private static List<Resource> getAllResourcesSortById(final EpubBook book) {
        final List<Resource> allResources = new ArrayList<Resource>(book.getResources().getAll());
        Collections.sort(allResources, (resource1, resource2) -> resource1.getId().compareToIgnoreCase(resource2.getId()));
        return allResources;
    }
    
    private static void writeItem(final EpubBook book, final Resource resource, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (resource == null || (resource.getMediaType() == MediaTypes.NCX && book.getSpine().getTocResource() != null)) {
            return;
        }
        if (StringUtil.isBlank(resource.getId())) {
            System.err.println(PackageDocumentWriter.TAG + " resource id must not be empty (href: " + resource.getHref() + ", mediatype:" + resource.getMediaType() + ")");
            return;
        }
        if (StringUtil.isBlank(resource.getHref())) {
            System.err.println(PackageDocumentWriter.TAG + " resource href must not be empty (id: " + resource.getId() + ", mediatype:" + resource.getMediaType() + ")");
            return;
        }
        if (resource.getMediaType() == null) {
            System.err.println(PackageDocumentWriter.TAG + " resource mediatype must not be empty (id: " + resource.getId() + ", href:" + resource.getHref() + ")");
            return;
        }
        serializer.startTag("http://www.idpf.org/2007/opf", "item");
        serializer.attribute("", "id", resource.getId());
        serializer.attribute("", "href", resource.getHref());
        serializer.attribute("", "media-type", resource.getMediaType().getName());
        serializer.endTag("http://www.idpf.org/2007/opf", "item");
    }
    
    private static void writeSpineItems(final Spine spine, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        for (final SpineReference spineReference : spine.getSpineReferences()) {
            serializer.startTag("http://www.idpf.org/2007/opf", "itemref");
            serializer.attribute("", "idref", spineReference.getResourceId());
            if (!spineReference.isLinear()) {
                serializer.attribute("", "linear", "no");
            }
            serializer.endTag("http://www.idpf.org/2007/opf", "itemref");
        }
    }
    
    private static void writeGuide(final EpubBook book, final EpubWriter epubWriter, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("http://www.idpf.org/2007/opf", "guide");
        ensureCoverPageGuideReferenceWritten(book.getGuide(), epubWriter, serializer);
        for (final GuideReference reference : book.getGuide().getReferences()) {
            writeGuideReference(reference, serializer);
        }
        serializer.endTag("http://www.idpf.org/2007/opf", "guide");
    }
    
    private static void ensureCoverPageGuideReferenceWritten(final Guide guide, final EpubWriter epubWriter, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (!guide.getGuideReferencesByType("cover").isEmpty()) {
            return;
        }
        final Resource coverPage = guide.getCoverPage();
        if (coverPage != null) {
            writeGuideReference(new GuideReference(guide.getCoverPage(), "cover", "cover"), serializer);
        }
    }
    
    private static void writeGuideReference(final GuideReference reference, final XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (reference == null) {
            return;
        }
        serializer.startTag("http://www.idpf.org/2007/opf", "reference");
        serializer.attribute("", "type", reference.getType());
        serializer.attribute("", "href", reference.getCompleteHref());
        if (StringUtil.isNotBlank(reference.getTitle())) {
            serializer.attribute("", "title", reference.getTitle());
        }
        serializer.endTag("http://www.idpf.org/2007/opf", "reference");
    }
    
    static {
        TAG = PackageDocumentWriter.class.getName();
    }
}
