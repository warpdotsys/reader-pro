//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.epub;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Element;

class DOMUtil
{
    public static String getAttribute(final Element element, final String namespace, final String attribute) {
        String result = element.getAttributeNS(namespace, attribute);
        if (StringUtil.isEmpty(result)) {
            result = element.getAttribute(attribute);
        }
        return result;
    }

    public static List<String> getElementsTextChild(final Element parentElement, final String namespace, final String tagName) {
        final NodeList elements = parentElement.getElementsByTagNameNS(namespace, tagName);
        final List<String> result = new ArrayList<String>(elements.getLength());
        for (int i = 0; i < elements.getLength(); ++i) {
            result.add(getTextChildrenContent((Element)elements.item(i)));
        }
        return result;
    }

    public static String getFindAttributeValue(final Document document, final String namespace, final String elementName, final String findAttributeName, final String findAttributeValue, final String resultAttributeName) {
        final NodeList metaTags = document.getElementsByTagNameNS(namespace, elementName);
        for (int i = 0; i < metaTags.getLength(); ++i) {
            final Element metaElement = (Element)metaTags.item(i);
            if (findAttributeValue.equalsIgnoreCase(metaElement.getAttribute(findAttributeName)) && StringUtil.isNotBlank(metaElement.getAttribute(resultAttributeName))) {
                return metaElement.getAttribute(resultAttributeName);
            }
        }
        return null;
    }

    public static NodeList getElementsByTagNameNS(final Element parentElement, final String namespace, final String tagName) {
        NodeList nodes = parentElement.getElementsByTagNameNS(namespace, tagName);
        if (nodes.getLength() != 0) {
            return nodes;
        }
        nodes = parentElement.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        return nodes;
    }

    public static NodeList getElementsByTagNameNS(final Document parentElement, final String namespace, final String tagName) {
        NodeList nodes = parentElement.getElementsByTagNameNS(namespace, tagName);
        if (nodes.getLength() != 0) {
            return nodes;
        }
        nodes = parentElement.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        return nodes;
    }

    public static Element getFirstElementByTagNameNS(final Element parentElement, final String namespace, final String tagName) {
        NodeList nodes = parentElement.getElementsByTagNameNS(namespace, tagName);
        if (nodes.getLength() != 0) {
            return (Element)nodes.item(0);
        }
        nodes = parentElement.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) {
            return null;
        }
        return (Element)nodes.item(0);
    }

    public static String getTextChildrenContent(final Element parentElement) {
        if (parentElement == null) {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        final NodeList childNodes = parentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            final Node node = childNodes.item(i);
            if (node != null) {
                if (node.getNodeType() == 3) {
                    result.append(((Text)node).getData());
                }
            }
        }
        return result.toString().trim();
    }
}
