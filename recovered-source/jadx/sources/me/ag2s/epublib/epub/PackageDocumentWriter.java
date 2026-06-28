package me.ag2s.epublib.epub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Guide;
import me.ag2s.epublib.domain.GuideReference;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.Spine;
import me.ag2s.epublib.domain.SpineReference;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.StringUtil;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/epub/PackageDocumentWriter.class */
public class PackageDocumentWriter extends PackageDocumentBase {
    private static final String TAG = PackageDocumentWriter.class.getName();

    public static void write(EpubWriter epubWriter, XmlSerializer serializer, EpubBook book) {
        try {
            serializer.startDocument(Constants.CHARACTER_ENCODING, false);
            serializer.setPrefix(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.NAMESPACE_OPF);
            serializer.setPrefix(PackageDocumentBase.PREFIX_DUBLIN_CORE, PackageDocumentBase.NAMESPACE_DUBLIN_CORE);
            serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.packageTag);
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "version", book.getVersion());
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.uniqueIdentifier, PackageDocumentBase.BOOK_ID_ID);
            PackageDocumentMetadataWriter.writeMetaData(book, serializer);
            writeManifest(book, epubWriter, serializer);
            writeSpine(book, epubWriter, serializer);
            writeGuide(book, epubWriter, serializer);
            serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.packageTag);
            serializer.endDocument();
            serializer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeSpine(EpubBook book, EpubWriter epubWriter, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.spine);
        Resource tocResource = book.getSpine().getTocResource();
        String tocResourceId = tocResource.getId();
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "toc", tocResourceId);
        if (book.getCoverPage() != null && book.getSpine().findFirstResourceById(book.getCoverPage().getId()) < 0) {
            serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.itemref);
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.idref, book.getCoverPage().getId());
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.linear, PackageDocumentBase.OPFValues.no);
            serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.itemref);
        }
        writeSpineItems(book.getSpine(), serializer);
        serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.spine);
    }

    private static void writeManifest(EpubBook book, EpubWriter epubWriter, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.manifest);
        serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, "item");
        if (book.isEpub3()) {
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.properties, "nav");
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "id", NCXDocumentV3.NCX_ITEM_ID);
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "href", NCXDocumentV3.DEFAULT_NCX_HREF);
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.media_type, NCXDocumentV3.V3_NCX_MEDIATYPE.getName());
        } else {
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "id", epubWriter.getNcxId());
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "href", epubWriter.getNcxHref());
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.media_type, epubWriter.getNcxMediaType());
        }
        serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, "item");
        for (Resource resource : getAllResourcesSortById(book)) {
            writeItem(book, resource, serializer);
        }
        serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.manifest);
    }

    private static List<Resource> getAllResourcesSortById(EpubBook book) {
        List<Resource> allResources = new ArrayList<>(book.getResources().getAll());
        Collections.sort(allResources, (resource1, resource2) -> {
            return resource1.getId().compareToIgnoreCase(resource2.getId());
        });
        return allResources;
    }

    private static void writeItem(EpubBook book, Resource resource, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        if (resource != null) {
            if (resource.getMediaType() == MediaTypes.NCX && book.getSpine().getTocResource() != null) {
                return;
            }
            if (StringUtil.isBlank(resource.getId())) {
                System.err.println(TAG + " resource id must not be empty (href: " + resource.getHref() + ", mediatype:" + resource.getMediaType() + ")");
                return;
            }
            if (StringUtil.isBlank(resource.getHref())) {
                System.err.println(TAG + " resource href must not be empty (id: " + resource.getId() + ", mediatype:" + resource.getMediaType() + ")");
                return;
            }
            if (resource.getMediaType() == null) {
                System.err.println(TAG + " resource mediatype must not be empty (id: " + resource.getId() + ", href:" + resource.getHref() + ")");
                return;
            }
            serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, "item");
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "id", resource.getId());
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "href", resource.getHref());
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.media_type, resource.getMediaType().getName());
            serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, "item");
        }
    }

    private static void writeSpineItems(Spine spine, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        for (SpineReference spineReference : spine.getSpineReferences()) {
            serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.itemref);
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.idref, spineReference.getResourceId());
            if (!spineReference.isLinear()) {
                serializer.attribute(PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.OPFAttributes.linear, PackageDocumentBase.OPFValues.no);
            }
            serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.itemref);
        }
    }

    private static void writeGuide(EpubBook book, EpubWriter epubWriter, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.guide);
        ensureCoverPageGuideReferenceWritten(book.getGuide(), epubWriter, serializer);
        for (GuideReference reference : book.getGuide().getReferences()) {
            writeGuideReference(reference, serializer);
        }
        serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.guide);
    }

    private static void ensureCoverPageGuideReferenceWritten(Guide guide, EpubWriter epubWriter, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        if (!guide.getGuideReferencesByType("cover").isEmpty()) {
            return;
        }
        Resource coverPage = guide.getCoverPage();
        if (coverPage != null) {
            writeGuideReference(new GuideReference(guide.getCoverPage(), "cover", "cover"), serializer);
        }
    }

    private static void writeGuideReference(GuideReference reference, XmlSerializer serializer) throws IllegalStateException, IOException, IllegalArgumentException {
        if (reference == null) {
            return;
        }
        serializer.startTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.reference);
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "type", reference.getType());
        serializer.attribute(PackageDocumentBase.PREFIX_OPF, "href", reference.getCompleteHref());
        if (StringUtil.isNotBlank(reference.getTitle())) {
            serializer.attribute(PackageDocumentBase.PREFIX_OPF, "title", reference.getTitle());
        }
        serializer.endTag(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.reference);
    }
}
