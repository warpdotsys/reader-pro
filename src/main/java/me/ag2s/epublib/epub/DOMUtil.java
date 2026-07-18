package me.ag2s.epublib.epub;

import java.util.ArrayList;
import java.util.List;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

class DOMUtil {
   public static String getAttribute(Element element, String namespace, String attribute) {
      String result = element.getAttributeNS(namespace, attribute);
      if (StringUtil.isEmpty(result)) {
         result = element.getAttribute(attribute);
      }

      return result;
   }

   public static List getElementsTextChild(Element parentElement, String namespace, String tagName) {
      NodeList elements = parentElement.getElementsByTagNameNS(namespace, tagName);
      List<String> result = new ArrayList(elements.getLength());

      for(int i = 0; i < elements.getLength(); ++i) {
         result.add(getTextChildrenContent((Element)elements.item(i)));
      }

      return result;
   }

   public static String getFindAttributeValue(Document document, String namespace, String elementName, String findAttributeName, String findAttributeValue, String resultAttributeName) {
      NodeList metaTags = document.getElementsByTagNameNS(namespace, elementName);

      for(int i = 0; i < metaTags.getLength(); ++i) {
         Element metaElement = (Element)metaTags.item(i);
         if (findAttributeValue.equalsIgnoreCase(metaElement.getAttribute(findAttributeName)) && StringUtil.isNotBlank(metaElement.getAttribute(resultAttributeName))) {
            return metaElement.getAttribute(resultAttributeName);
         }
      }

      return null;
   }

   public static NodeList getElementsByTagNameNS(Element parentElement, String namespace, String tagName) {
      NodeList nodes = parentElement.getElementsByTagNameNS(namespace, tagName);
      if (nodes.getLength() != 0) {
         return nodes;
      } else {
         nodes = parentElement.getElementsByTagName(tagName);
         return nodes.getLength() == 0 ? null : nodes;
      }
   }

   public static NodeList getElementsByTagNameNS(Document parentElement, String namespace, String tagName) {
      NodeList nodes = parentElement.getElementsByTagNameNS(namespace, tagName);
      if (nodes.getLength() != 0) {
         return nodes;
      } else {
         nodes = parentElement.getElementsByTagName(tagName);
         return nodes.getLength() == 0 ? null : nodes;
      }
   }

   public static Element getFirstElementByTagNameNS(Element parentElement, String namespace, String tagName) {
      NodeList nodes = parentElement.getElementsByTagNameNS(namespace, tagName);
      if (nodes.getLength() != 0) {
         return (Element)nodes.item(0);
      } else {
         nodes = parentElement.getElementsByTagName(tagName);
         return nodes.getLength() == 0 ? null : (Element)nodes.item(0);
      }
   }

   public static String getTextChildrenContent(Element parentElement) {
      if (parentElement == null) {
         return null;
      } else {
         StringBuilder result = new StringBuilder();
         NodeList childNodes = parentElement.getChildNodes();

         for(int i = 0; i < childNodes.getLength(); ++i) {
            Node node = childNodes.item(i);
            if (node != null && node.getNodeType() == 3) {
               result.append(((Text)node).getData());
            }
         }

         return result.toString().trim();
      }
   }
}
