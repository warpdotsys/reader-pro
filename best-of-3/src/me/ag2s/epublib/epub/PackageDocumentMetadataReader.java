//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.Identifier;
import me.ag2s.epublib.util.StringUtil;
import me.ag2s.epublib.domain.Date;
import java.util.ArrayList;
import me.ag2s.epublib.domain.Author;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.HashMap;
import javax.xml.namespace.QName;
import java.util.Map;
import org.w3c.dom.Element;
import me.ag2s.epublib.domain.Metadata;
import org.w3c.dom.Document;

class PackageDocumentMetadataReader extends PackageDocumentBase
{
    private static final String TAG;

    public static Metadata readMetadata(final Document packageDocument) {
        final Metadata result = new Metadata();
        final Element metadataElement = DOMUtil.getFirstElementByTagNameNS(packageDocument.getDocumentElement(), "http://www.idpf.org/2007/opf", "metadata");
        if (metadataElement == null) {
            System.err.println(PackageDocumentMetadataReader.TAG + " Package does not contain element " + "metadata");
            return result;
        }
        result.setTitles(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "title"));
        result.setPublishers(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "publisher"));
        result.setDescriptions(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "description"));
        result.setRights(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "rights"));
        result.setTypes(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "type"));
        result.setSubjects(DOMUtil.getElementsTextChild(metadataElement, "http://purl.org/dc/elements/1.1/", "subject"));
        result.setIdentifiers(readIdentifiers(metadataElement));
        result.setAuthors(readCreators(metadataElement));
        result.setContributors(readContributors(metadataElement));
        result.setDates(readDates(metadataElement));
        result.setOtherProperties(readOtherProperties(metadataElement));
        result.setMetaAttributes(readMetaProperties(metadataElement));
        final Element languageTag = DOMUtil.getFirstElementByTagNameNS(metadataElement, "http://purl.org/dc/elements/1.1/", "language");
        if (languageTag != null) {
            result.setLanguage(DOMUtil.getTextChildrenContent(languageTag));
        }
        return result;
    }

    private static Map<QName, String> readOtherProperties(final Element metadataElement) {
        final Map<QName, String> result = new HashMap<QName, String>();
        final NodeList metaTags = metadataElement.getElementsByTagName("meta");
        for (int i = 0; i < metaTags.getLength(); ++i) {
            final Node metaNode = metaTags.item(i);
            final Node property = metaNode.getAttributes().getNamedItem("property");
            if (property != null) {
                final String name = property.getNodeValue();
                final String value = metaNode.getTextContent();
                result.put(new QName(name), value);
            }
        }
        return result;
    }

    private static Map<String, String> readMetaProperties(final Element metadataElement) {
        final Map<String, String> result = new HashMap<String, String>();
        final NodeList metaTags = metadataElement.getElementsByTagName("meta");
        for (int i = 0; i < metaTags.getLength(); ++i) {
            final Element metaElement = (Element)metaTags.item(i);
            final String name = metaElement.getAttribute("name");
            final String value = metaElement.getAttribute("content");
            result.put(name, value);
        }
        return result;
    }

    private static String getBookIdId(final Document document) {
        final Element packageElement = DOMUtil.getFirstElementByTagNameNS(document.getDocumentElement(), "http://www.idpf.org/2007/opf", "package");
        if (packageElement == null) {
            return null;
        }
        return DOMUtil.getAttribute(packageElement, "http://www.idpf.org/2007/opf", "unique-identifier");
    }

    private static List<Author> readCreators(final Element metadataElement) {
        return readAuthors("creator", metadataElement);
    }

    private static List<Author> readContributors(final Element metadataElement) {
        return readAuthors("contributor", metadataElement);
    }

    private static List<Author> readAuthors(final String authorTag, final Element metadataElement) {
        final NodeList elements = metadataElement.getElementsByTagNameNS("http://purl.org/dc/elements/1.1/", authorTag);
        final List<Author> result = new ArrayList<Author>(elements.getLength());
        for (int i = 0; i < elements.getLength(); ++i) {
            final Element authorElement = (Element)elements.item(i);
            final Author author = createAuthor(authorElement);
            if (author != null) {
                result.add(author);
            }
        }
        return result;
    }

    private static List<Date> readDates(final Element metadataElement) {
        final NodeList elements = metadataElement.getElementsByTagNameNS("http://purl.org/dc/elements/1.1/", "date");
        final List<Date> result = new ArrayList<Date>(elements.getLength());
        for (int i = 0; i < elements.getLength(); ++i) {
            final Element dateElement = (Element)elements.item(i);
            try {
                final Date date = new Date(DOMUtil.getTextChildrenContent(dateElement), DOMUtil.getAttribute(dateElement, "http://www.idpf.org/2007/opf", "event"));
                result.add(date);
            }
            catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static Author createAuthor(final Element authorElement) {
        final String authorString = DOMUtil.getTextChildrenContent(authorElement);
        if (StringUtil.isBlank(authorString)) {
            return null;
        }
        final int spacePos = authorString.lastIndexOf(32);
        Author result;
        if (spacePos < 0) {
            result = new Author(authorString);
        }
        else {
            result = new Author(authorString.substring(0, spacePos), authorString.substring(spacePos + 1));
        }
        result.setRole(DOMUtil.getAttribute(authorElement, "http://www.idpf.org/2007/opf", "role"));
        return result;
    }

    private static List<Identifier> readIdentifiers(final Element metadataElement) {
        final NodeList identifierElements = metadataElement.getElementsByTagNameNS("http://purl.org/dc/elements/1.1/", "identifier");
        if (identifierElements.getLength() == 0) {
            System.err.println(PackageDocumentMetadataReader.TAG + " Package does not contain element " + "identifier");
            return new ArrayList<Identifier>();
        }
        final String bookIdId = getBookIdId(metadataElement.getOwnerDocument());
        final List<Identifier> result = new ArrayList<Identifier>(identifierElements.getLength());
        for (int i = 0; i < identifierElements.getLength(); ++i) {
            final Element identifierElement = (Element)identifierElements.item(i);
            final String schemeName = DOMUtil.getAttribute(identifierElement, "http://www.idpf.org/2007/opf", "scheme");
            final String identifierValue = DOMUtil.getTextChildrenContent(identifierElement);
            if (!StringUtil.isBlank(identifierValue)) {
                final Identifier identifier = new Identifier(schemeName, identifierValue);
                if (identifierElement.getAttribute("id").equals(bookIdId)) {
                    identifier.setBookId(true);
                }
                result.add(identifier);
            }
        }
        return result;
    }

    static {
        TAG = PackageDocumentMetadataReader.class.getName();
    }
}
