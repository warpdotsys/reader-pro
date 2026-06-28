package me.ag2s.epublib.epub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.Date;
import me.ag2s.epublib.domain.Identifier;
import me.ag2s.epublib.domain.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/PackageDocumentMetadataReader.class */
class PackageDocumentMetadataReader extends PackageDocumentBase {
    private static final String TAG = PackageDocumentMetadataReader.class.getName();

    PackageDocumentMetadataReader() {
    }

    public static Metadata readMetadata(Document packageDocument) {
        Metadata result = new Metadata();
        Element metadataElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.metadata);
        if (metadataElement == null) {
            System.err.println(TAG + " Package does not contain element " + PackageDocumentBase.OPFTags.metadata);
            return result;
        }
        result.setTitles(DOMUtil.getElementsTextChild(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, "title"));
        result.setPublishers(DOMUtil.getElementsTextChild(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, PackageDocumentBase.DCTags.publisher));
        result.setDescriptions(DOMUtil.getElementsTextChild(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, "description"));
        result.setRights(DOMUtil.getElementsTextChild(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, PackageDocumentBase.DCTags.rights));
        result.setTypes(DOMUtil.getElementsTextChild(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, "type"));
        result.setSubjects(DOMUtil.getElementsTextChild(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, PackageDocumentBase.DCTags.subject));
        result.setIdentifiers(readIdentifiers(metadataElement));
        result.setAuthors(readCreators(metadataElement));
        result.setContributors(readContributors(metadataElement));
        result.setDates(readDates(metadataElement));
        result.setOtherProperties(readOtherProperties(metadataElement));
        result.setMetaAttributes(readMetaProperties(metadataElement));
        Element languageTag = DOMUtil.getFirstElementByTagNameNS(metadataElement, PackageDocumentBase.NAMESPACE_DUBLIN_CORE, PackageDocumentBase.DCTags.language);
        if (languageTag != null) {
            result.setLanguage(DOMUtil.getTextChildrenContent(languageTag));
        }
        return result;
    }

    private static Map<QName, String> readOtherProperties(Element metadataElement) {
        Map<QName, String> result = new HashMap<>();
        NodeList metaTags = metadataElement.getElementsByTagName("meta");
        for (int i = 0; i < metaTags.getLength(); i++) {
            Node metaNode = metaTags.item(i);
            Node property = metaNode.getAttributes().getNamedItem(PackageDocumentBase.OPFAttributes.property);
            if (property != null) {
                String name = property.getNodeValue();
                String value = metaNode.getTextContent();
                result.put(new QName(name), value);
            }
        }
        return result;
    }

    private static Map<String, String> readMetaProperties(Element metadataElement) {
        Map<String, String> result = new HashMap<>();
        NodeList metaTags = metadataElement.getElementsByTagName("meta");
        for (int i = 0; i < metaTags.getLength(); i++) {
            Element metaElement = (Element) metaTags.item(i);
            String name = metaElement.getAttribute("name");
            String value = metaElement.getAttribute("content");
            result.put(name, value);
        }
        return result;
    }

    private static String getBookIdId(Document document) {
        Element packageElement = DOMUtil.getFirstElementByTagNameNS(document.getDocumentElement(), PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFTags.packageTag);
        if (packageElement == null) {
            return null;
        }
        return DOMUtil.getAttribute(packageElement, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFAttributes.uniqueIdentifier);
    }

    private static List<Author> readCreators(Element metadataElement) {
        return readAuthors(PackageDocumentBase.DCTags.creator, metadataElement);
    }

    private static List<Author> readContributors(Element metadataElement) {
        return readAuthors(PackageDocumentBase.DCTags.contributor, metadataElement);
    }

    private static List<Author> readAuthors(String authorTag, Element metadataElement) {
        NodeList elements = metadataElement.getElementsByTagNameNS(PackageDocumentBase.NAMESPACE_DUBLIN_CORE, authorTag);
        List<Author> result = new ArrayList<>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++) {
            Element authorElement = (Element) elements.item(i);
            Author author = createAuthor(authorElement);
            if (author != null) {
                result.add(author);
            }
        }
        return result;
    }

    private static List<Date> readDates(Element metadataElement) {
        NodeList elements = metadataElement.getElementsByTagNameNS(PackageDocumentBase.NAMESPACE_DUBLIN_CORE, PackageDocumentBase.DCTags.date);
        List<Date> result = new ArrayList<>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++) {
            Element dateElement = (Element) elements.item(i);
            try {
                Date date = new Date(DOMUtil.getTextChildrenContent(dateElement), DOMUtil.getAttribute(dateElement, PackageDocumentBase.NAMESPACE_OPF, PackageDocumentBase.OPFAttributes.event));
                result.add(date);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static Author createAuthor(Element authorElement) {
        Author result;
        String authorString = DOMUtil.getTextChildrenContent(authorElement);
        if (StringUtil.isBlank(authorString)) {
            return null;
        }
        int spacePos = authorString.lastIndexOf(32);
        if (spacePos < 0) {
            result = new Author(authorString);
        } else {
            result = new Author(authorString.substring(0, spacePos), authorString.substring(spacePos + 1));
        }
        result.setRole(DOMUtil.getAttribute(authorElement, PackageDocumentBase.NAMESPACE_OPF, "role"));
        return result;
    }

    private static List<Identifier> readIdentifiers(Element metadataElement) {
        NodeList identifierElements = metadataElement.getElementsByTagNameNS(PackageDocumentBase.NAMESPACE_DUBLIN_CORE, PackageDocumentBase.DCTags.identifier);
        if (identifierElements.getLength() == 0) {
            System.err.println(TAG + " Package does not contain element " + PackageDocumentBase.DCTags.identifier);
            return new ArrayList();
        }
        String bookIdId = getBookIdId(metadataElement.getOwnerDocument());
        List<Identifier> result = new ArrayList<>(identifierElements.getLength());
        for (int i = 0; i < identifierElements.getLength(); i++) {
            Element identifierElement = (Element) identifierElements.item(i);
            String schemeName = DOMUtil.getAttribute(identifierElement, PackageDocumentBase.NAMESPACE_OPF, "scheme");
            String identifierValue = DOMUtil.getTextChildrenContent(identifierElement);
            if (!StringUtil.isBlank(identifierValue)) {
                Identifier identifier = new Identifier(schemeName, identifierValue);
                if (identifierElement.getAttribute("id").equals(bookIdId)) {
                    identifier.setBookId(true);
                }
                result.add(identifier);
            }
        }
        return result;
    }
}
