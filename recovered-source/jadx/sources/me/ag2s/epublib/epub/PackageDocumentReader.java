package me.ag2s.epublib.epub;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Guide;
import me.ag2s.epublib.domain.GuideReference;
import me.ag2s.epublib.domain.MediaType;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.domain.Spine;
import me.ag2s.epublib.domain.SpineReference;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/PackageDocumentReader.class */
public class PackageDocumentReader extends PackageDocumentBase {
    private static final String TAG = PackageDocumentReader.class.getName();
    private static final String[] POSSIBLE_NCX_ITEM_IDS = {"toc", "ncx", "ncxtoc", NCXDocumentV3.NCX_ITEM_ID};

    public static void read(Resource packageResource, EpubReader epubReader, EpubBook book, Resources resources) throws SAXException, IOException {
        Document packageDocument = ResourceUtil.getAsDocument(packageResource);
        String packageHref = packageResource.getHref();
        Resources resources2 = fixHrefs(packageHref, resources);
        readGuide(packageDocument, epubReader, book, resources2);
        Map<String, String> idMapping = new HashMap<>();
        String version = DOMUtil.getAttribute(packageDocument.getDocumentElement(), PackageDocumentBase.PREFIX_OPF, "version");
        book.setResources(readManifest(packageDocument, packageHref, epubReader, resources2, idMapping));
        book.setVersion(version);
        readCover(packageDocument, book);
        book.setMetadata(PackageDocumentMetadataReader.readMetadata(packageDocument));
        book.setSpine(readSpine(packageDocument, book.getResources(), idMapping));
        if (book.getCoverPage() == null && book.getSpine().size() > 0) {
            book.setCoverPage(book.getSpine().getResource(0));
        }
    }

    private static Resources readManifest(Document packageDocument, String packageHref, EpubReader epubReader, Resources resources, Map<String, String> idMapping) {
        Element manifestElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.manifest);
        Resources result = new Resources();
        if (manifestElement == null) {
            System.err.println(TAG + " Package does not contain element " + PackageDocumentBase.OPFTags.manifest);
            return result;
        }
        NodeList itemElements = manifestElement.getElementsByTagNameNS(PackageDocumentBase.NAMESPACE_OPF, "item");
        for (int i = 0; i < itemElements.getLength(); i++) {
            Element itemElement = (Element) itemElements.item(i);
            String id = DOMUtil.getAttribute(itemElement, PackageDocumentBase.NAMESPACE_OPF, "id");
            String href = DOMUtil.getAttribute(itemElement, PackageDocumentBase.NAMESPACE_OPF, "href");
            try {
                href = URLDecoder.decode(href, Constants.CHARACTER_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String mediaTypeName = DOMUtil.getAttribute(itemElement, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFAttributes.media_type);
            Resource resource = resources.remove(href);
            if (resource == null) {
                System.err.println(TAG + " resource with href '" + href + "' not found");
            } else {
                resource.setId(id);
                String properties = DOMUtil.getAttribute(itemElement, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFAttributes.properties);
                resource.setProperties(properties);
                MediaType mediaType = MediaTypes.getMediaTypeByName(mediaTypeName);
                if (mediaType != null) {
                    resource.setMediaType(mediaType);
                }
                result.add(resource);
                idMapping.put(id, resource.getId());
            }
        }
        return result;
    }

    private static void readGuide(Document packageDocument, EpubReader epubReader, EpubBook book, Resources resources) {
        Element guideElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.guide);
        if (guideElement == null) {
            return;
        }
        Guide guide = book.getGuide();
        NodeList guideReferences = guideElement.getElementsByTagNameNS(PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.reference);
        for (int i = 0; i < guideReferences.getLength(); i++) {
            Element referenceElement = (Element) guideReferences.item(i);
            String resourceHref = DOMUtil.getAttribute(referenceElement, PackageDocumentBase.NAMESPACE_OPF, "href");
            if (!StringUtil.isBlank(resourceHref)) {
                Resource resource = resources.getByHref(StringUtil.substringBefore(resourceHref, '#'));
                if (resource == null) {
                    System.err.println(TAG + " Guide is referencing resource with href " + resourceHref + " which could not be found");
                } else {
                    String type = DOMUtil.getAttribute(referenceElement, PackageDocumentBase.NAMESPACE_OPF, "type");
                    if (StringUtil.isBlank(type)) {
                        System.err.println(TAG + " Guide is referencing resource with href " + resourceHref + " which is missing the 'type' attribute");
                    } else {
                        String title = DOMUtil.getAttribute(referenceElement, PackageDocumentBase.NAMESPACE_OPF, "title");
                        if (!"cover".equalsIgnoreCase(type)) {
                            GuideReference reference = new GuideReference(resource, type, title, StringUtil.substringAfter(resourceHref, '#'));
                            guide.addReference(reference);
                        }
                    }
                }
            }
        }
    }

    static Resources fixHrefs(String packageHref, Resources resourcesByHref) {
        int lastSlashPos = packageHref.lastIndexOf(47);
        if (lastSlashPos < 0) {
            return resourcesByHref;
        }
        Resources result = new Resources();
        for (Resource resource : resourcesByHref.getAll()) {
            if (StringUtil.isNotBlank(resource.getHref()) && resource.getHref().length() > lastSlashPos) {
                resource.setHref(resource.getHref().substring(lastSlashPos + 1));
            }
            result.add(resource);
        }
        return result;
    }

    private static Spine readSpine(Document packageDocument, Resources resources, Map<String, String> idMapping) {
        Element spineElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.spine);
        if (spineElement == null) {
            System.err.println(TAG + " Element " + PackageDocumentBase.OPFTags.spine + " not found in package document, generating one automatically");
            return generateSpineFromResources(resources);
        }
        Spine result = new Spine();
        String tocResourceId = DOMUtil.getAttribute(spineElement, PackageDocumentBase.NAMESPACE_OPF, "toc");
        System.out.println(TAG + " " + tocResourceId);
        result.setTocResource(findTableOfContentsResource(tocResourceId, resources));
        NodeList spineNodes = DOMUtil.getElementsByTagNameNS(packageDocument, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.itemref);
        if (spineNodes == null) {
            System.err.println(TAG + " spineNodes is null");
            return result;
        }
        List<SpineReference> spineReferences = new ArrayList<>(spineNodes.getLength());
        for (int i = 0; i < spineNodes.getLength(); i++) {
            Element spineItem = (Element) spineNodes.item(i);
            String itemref = DOMUtil.getAttribute(spineItem, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFAttributes.idref);
            if (StringUtil.isBlank(itemref)) {
                System.err.println(TAG + " itemref with missing or empty idref");
            } else {
                String id = idMapping.get(itemref);
                if (id == null) {
                    id = itemref;
                }
                Resource resource = resources.getByIdOrHref(id);
                if (resource == null) {
                    System.err.println(TAG + " resource with id '" + id + "' not found");
                } else {
                    SpineReference spineReference = new SpineReference(resource);
                    if (PackageDocumentBase.OPFValues.no.equalsIgnoreCase(DOMUtil.getAttribute(spineItem, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFAttributes.linear))) {
                        spineReference.setLinear(false);
                    }
                    spineReferences.add(spineReference);
                }
            }
        }
        result.setSpineReferences(spineReferences);
        return result;
    }

    private static Spine generateSpineFromResources(Resources resources) {
        Spine result = new Spine();
        List<String> resourceHrefs = new ArrayList<>(resources.getAllHrefs());
        Collections.sort(resourceHrefs, String.CASE_INSENSITIVE_ORDER);
        for (String resourceHref : resourceHrefs) {
            Resource resource = resources.getByHref(resourceHref);
            if (resource.getMediaType() == MediaTypes.NCX) {
                result.setTocResource(resource);
            } else if (resource.getMediaType() == MediaTypes.XHTML) {
                result.addSpineReference(new SpineReference(resource));
            }
        }
        return result;
    }

    static Resource findTableOfContentsResource(String tocResourceId, Resources resources) {
        Resource tocResource = resources.getByProperties("nav");
        if (tocResource != null) {
            return tocResource;
        }
        if (StringUtil.isNotBlank(tocResourceId)) {
            tocResource = resources.getByIdOrHref(tocResourceId);
        }
        if (tocResource != null) {
            return tocResource;
        }
        Resource tocResource2 = resources.findFirstResourceByMediaType(MediaTypes.NCX);
        if (tocResource2 == null) {
            for (String possibleNcxItemId : POSSIBLE_NCX_ITEM_IDS) {
                tocResource2 = resources.getByIdOrHref(possibleNcxItemId);
                if (tocResource2 != null) {
                    break;
                }
                tocResource2 = resources.getByIdOrHref(possibleNcxItemId.toUpperCase());
                if (tocResource2 != null) {
                    break;
                }
            }
        }
        if (tocResource2 == null) {
            System.err.println(TAG + " Could not find table of contents resource. Tried resource with id '" + tocResourceId + "', toc, " + "toc".toUpperCase() + " and any NCX resource.");
        }
        return tocResource2;
    }

    static Set<String> findCoverHrefs(Document packageDocument) {
        Set<String> result = new HashSet<>();
        String coverResourceId = DOMUtil.getFindAttributeValue(packageDocument, PackageDocumentBase.NAMESPACE_OPF, "meta", "name", "cover", "content");
        if (StringUtil.isNotBlank(coverResourceId)) {
            String coverHref = DOMUtil.getFindAttributeValue(packageDocument, PackageDocumentBase.NAMESPACE_OPF, "item", "id", coverResourceId, "href");
            if (StringUtil.isNotBlank(coverHref)) {
                result.add(coverHref);
            } else {
                result.add(coverResourceId);
            }
        }
        String coverHref2 = DOMUtil.getFindAttributeValue(packageDocument, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.reference, "type", "cover", "href");
        if (StringUtil.isNotBlank(coverHref2)) {
            result.add(coverHref2);
        }
        return result;
    }

    private static void readCover(Document packageDocument, EpubBook book) {
        Collection<String> coverHrefs = findCoverHrefs(packageDocument);
        for (String coverHref : coverHrefs) {
            Resource resource = book.getResources().getByHref(coverHref);
            if (resource == null) {
                System.err.println(TAG + " Cover resource " + coverHref + " not found");
            } else if (resource.getMediaType() == MediaTypes.XHTML) {
                book.setCoverPage(resource);
            } else if (MediaTypes.isBitmapImage(resource.getMediaType())) {
                book.setCoverImage(resource);
            }
        }
    }
}
