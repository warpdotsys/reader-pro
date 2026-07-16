// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import me.ag2s.epublib.domain.SpineReference;
import java.util.ArrayList;
import me.ag2s.epublib.domain.Spine;
import java.util.Iterator;
import me.ag2s.epublib.domain.Guide;
import me.ag2s.epublib.domain.GuideReference;
import me.ag2s.epublib.util.StringUtil;
import me.ag2s.epublib.domain.MediaType;
import org.w3c.dom.NodeList;
import me.ag2s.epublib.domain.MediaTypes;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.w3c.dom.Element;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.util.Map;
import org.w3c.dom.Document;
import java.util.HashMap;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Resource;

public class PackageDocumentReader extends PackageDocumentBase
{
    private static final String TAG;
    private static final String[] POSSIBLE_NCX_ITEM_IDS;
    
    public static void read(final Resource packageResource, final EpubReader epubReader, final EpubBook book, Resources resources) throws SAXException, IOException {
        final Document packageDocument = ResourceUtil.getAsDocument(packageResource);
        final String packageHref = packageResource.getHref();
        resources = fixHrefs(packageHref, resources);
        readGuide(packageDocument, epubReader, book, resources);
        final Map<String, String> idMapping = new HashMap<String, String>();
        final String version = DOMUtil.getAttribute(packageDocument.getDocumentElement(), "", "version");
        resources = readManifest(packageDocument, packageHref, epubReader, resources, idMapping);
        book.setResources(resources);
        book.setVersion(version);
        readCover(packageDocument, book);
        book.setMetadata(PackageDocumentMetadataReader.readMetadata(packageDocument));
        book.setSpine(readSpine(packageDocument, book.getResources(), idMapping));
        if (book.getCoverPage() == null && book.getSpine().size() > 0) {
            book.setCoverPage(book.getSpine().getResource(0));
        }
    }
    
    private static Resources readManifest(final Document packageDocument, final String packageHref, final EpubReader epubReader, final Resources resources, final Map<String, String> idMapping) {
        final Element manifestElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), "http://www.idpf.org/2007/opf", "manifest");
        final Resources result = new Resources();
        if (manifestElement == null) {
            System.err.println(PackageDocumentReader.TAG + " Package does not contain element " + "manifest");
            return result;
        }
        final NodeList itemElements = manifestElement.getElementsByTagNameNS("http://www.idpf.org/2007/opf", "item");
        for (int i = 0; i < itemElements.getLength(); ++i) {
            final Element itemElement = (Element)itemElements.item(i);
            final String id = DOMUtil.getAttribute(itemElement, "http://www.idpf.org/2007/opf", "id");
            String href = DOMUtil.getAttribute(itemElement, "http://www.idpf.org/2007/opf", "href");
            try {
                href = URLDecoder.decode(href, "UTF-8");
            }
            catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String mediaTypeName = DOMUtil.getAttribute(itemElement, "http://www.idpf.org/2007/opf", "media-type");
            final Resource resource = resources.remove(href);
            if (resource == null) {
                System.err.println(PackageDocumentReader.TAG + " resource with href '" + href + "' not found");
            }
            else {
                resource.setId(id);
                final String properties = DOMUtil.getAttribute(itemElement, "http://www.idpf.org/2007/opf", "properties");
                resource.setProperties(properties);
                final MediaType mediaType = MediaTypes.getMediaTypeByName(mediaTypeName);
                if (mediaType != null) {
                    resource.setMediaType(mediaType);
                }
                result.add(resource);
                idMapping.put(id, resource.getId());
            }
        }
        return result;
    }
    
    private static void readGuide(final Document packageDocument, final EpubReader epubReader, final EpubBook book, final Resources resources) {
        final Element guideElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), "http://www.idpf.org/2007/opf", "guide");
        if (guideElement == null) {
            return;
        }
        final Guide guide = book.getGuide();
        final NodeList guideReferences = guideElement.getElementsByTagNameNS("http://www.idpf.org/2007/opf", "reference");
        for (int i = 0; i < guideReferences.getLength(); ++i) {
            final Element referenceElement = (Element)guideReferences.item(i);
            final String resourceHref = DOMUtil.getAttribute(referenceElement, "http://www.idpf.org/2007/opf", "href");
            if (!StringUtil.isBlank(resourceHref)) {
                final Resource resource = resources.getByHref(StringUtil.substringBefore(resourceHref, '#'));
                if (resource == null) {
                    System.err.println(PackageDocumentReader.TAG + " Guide is referencing resource with href " + resourceHref + " which could not be found");
                }
                else {
                    final String type = DOMUtil.getAttribute(referenceElement, "http://www.idpf.org/2007/opf", "type");
                    if (StringUtil.isBlank(type)) {
                        System.err.println(PackageDocumentReader.TAG + " Guide is referencing resource with href " + resourceHref + " which is missing the 'type' attribute");
                    }
                    else {
                        final String title = DOMUtil.getAttribute(referenceElement, "http://www.idpf.org/2007/opf", "title");
                        if (!"cover".equalsIgnoreCase(type)) {
                            final GuideReference reference = new GuideReference(resource, type, title, StringUtil.substringAfter(resourceHref, '#'));
                            guide.addReference(reference);
                        }
                    }
                }
            }
        }
    }
    
    static Resources fixHrefs(final String packageHref, final Resources resourcesByHref) {
        final int lastSlashPos = packageHref.lastIndexOf(47);
        if (lastSlashPos < 0) {
            return resourcesByHref;
        }
        final Resources result = new Resources();
        for (final Resource resource : resourcesByHref.getAll()) {
            if (StringUtil.isNotBlank(resource.getHref()) && resource.getHref().length() > lastSlashPos) {
                resource.setHref(resource.getHref().substring(lastSlashPos + 1));
            }
            result.add(resource);
        }
        return result;
    }
    
    private static Spine readSpine(final Document packageDocument, final Resources resources, final Map<String, String> idMapping) {
        final Element spineElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), "http://www.idpf.org/2007/opf", "spine");
        if (spineElement == null) {
            System.err.println(PackageDocumentReader.TAG + " Element " + "spine" + " not found in package document, generating one automatically");
            return generateSpineFromResources(resources);
        }
        final Spine result = new Spine();
        final String tocResourceId = DOMUtil.getAttribute(spineElement, "http://www.idpf.org/2007/opf", "toc");
        System.out.println(PackageDocumentReader.TAG + " " + tocResourceId);
        result.setTocResource(findTableOfContentsResource(tocResourceId, resources));
        final NodeList spineNodes = DOMUtil.getElementsByTagNameNS(packageDocument, "http://www.idpf.org/2007/opf", "itemref");
        if (spineNodes == null) {
            System.err.println(PackageDocumentReader.TAG + " spineNodes is null");
            return result;
        }
        final List<SpineReference> spineReferences = new ArrayList<SpineReference>(spineNodes.getLength());
        for (int i = 0; i < spineNodes.getLength(); ++i) {
            final Element spineItem = (Element)spineNodes.item(i);
            final String itemref = DOMUtil.getAttribute(spineItem, "http://www.idpf.org/2007/opf", "idref");
            if (StringUtil.isBlank(itemref)) {
                System.err.println(PackageDocumentReader.TAG + " itemref with missing or empty idref");
            }
            else {
                String id = idMapping.get(itemref);
                if (id == null) {
                    id = itemref;
                }
                final Resource resource = resources.getByIdOrHref(id);
                if (resource == null) {
                    System.err.println(PackageDocumentReader.TAG + " resource with id '" + id + "' not found");
                }
                else {
                    final SpineReference spineReference = new SpineReference(resource);
                    if ("no".equalsIgnoreCase(DOMUtil.getAttribute(spineItem, "http://www.idpf.org/2007/opf", "linear"))) {
                        spineReference.setLinear(false);
                    }
                    spineReferences.add(spineReference);
                }
            }
        }
        result.setSpineReferences(spineReferences);
        return result;
    }
    
    private static Spine generateSpineFromResources(final Resources resources) {
        final Spine result = new Spine();
        final List<String> resourceHrefs = new ArrayList<String>(resources.getAllHrefs());
        Collections.sort(resourceHrefs, String.CASE_INSENSITIVE_ORDER);
        for (final String resourceHref : resourceHrefs) {
            final Resource resource = resources.getByHref(resourceHref);
            if (resource.getMediaType() == MediaTypes.NCX) {
                result.setTocResource(resource);
            }
            else {
                if (resource.getMediaType() != MediaTypes.XHTML) {
                    continue;
                }
                result.addSpineReference(new SpineReference(resource));
            }
        }
        return result;
    }
    
    static Resource findTableOfContentsResource(final String tocResourceId, final Resources resources) {
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
        tocResource = resources.findFirstResourceByMediaType(MediaTypes.NCX);
        if (tocResource == null) {
            for (final String possibleNcxItemId : PackageDocumentReader.POSSIBLE_NCX_ITEM_IDS) {
                tocResource = resources.getByIdOrHref(possibleNcxItemId);
                if (tocResource != null) {
                    break;
                }
                tocResource = resources.getByIdOrHref(possibleNcxItemId.toUpperCase());
                if (tocResource != null) {
                    break;
                }
            }
        }
        if (tocResource == null) {
            System.err.println(PackageDocumentReader.TAG + " Could not find table of contents resource. Tried resource with id '" + tocResourceId + "', " + "toc" + ", " + "toc".toUpperCase() + " and any NCX resource.");
        }
        return tocResource;
    }
    
    static Set<String> findCoverHrefs(final Document packageDocument) {
        final Set<String> result = new HashSet<String>();
        final String coverResourceId = DOMUtil.getFindAttributeValue(packageDocument, "http://www.idpf.org/2007/opf", "meta", "name", "cover", "content");
        if (StringUtil.isNotBlank(coverResourceId)) {
            final String coverHref = DOMUtil.getFindAttributeValue(packageDocument, "http://www.idpf.org/2007/opf", "item", "id", coverResourceId, "href");
            if (StringUtil.isNotBlank(coverHref)) {
                result.add(coverHref);
            }
            else {
                result.add(coverResourceId);
            }
        }
        final String coverHref = DOMUtil.getFindAttributeValue(packageDocument, "http://www.idpf.org/2007/opf", "reference", "type", "cover", "href");
        if (StringUtil.isNotBlank(coverHref)) {
            result.add(coverHref);
        }
        return result;
    }
    
    private static void readCover(final Document packageDocument, final EpubBook book) {
        final Collection<String> coverHrefs = findCoverHrefs(packageDocument);
        for (final String coverHref : coverHrefs) {
            final Resource resource = book.getResources().getByHref(coverHref);
            if (resource == null) {
                System.err.println(PackageDocumentReader.TAG + " Cover resource " + coverHref + " not found");
            }
            else if (resource.getMediaType() == MediaTypes.XHTML) {
                book.setCoverPage(resource);
            }
            else {
                if (!MediaTypes.isBitmapImage(resource.getMediaType())) {
                    continue;
                }
                book.setCoverImage(resource);
            }
        }
    }
    
    static {
        TAG = PackageDocumentReader.class.getName();
        POSSIBLE_NCX_ITEM_IDS = new String[] { "toc", "ncx", "ncxtoc", "htmltoc" };
    }
}
