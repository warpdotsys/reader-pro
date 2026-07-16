package io.legado.app.utils

import java.io.InputStream
import java.util.LinkedHashMap
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource

public object XmlUtils {
   public fun xml2map(source: Any): MutableMap<String, Any> {
      val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      val doc: java.util.Map = new LinkedHashMap();

      try {
         val var8: DocumentBuilder = factory.newDocumentBuilder();
         if (source is java.lang.String) {
            val var12: NodeList = var8.parse(source as java.lang.String).getChildNodes();
            return this.parseNode(var12);
         } else if (source is InputStream) {
            val var11: NodeList = var8.parse(source as InputStream).getChildNodes();
            return this.parseNode(var11);
         } else if (source is InputSource) {
            val var6: NodeList = var8.parse(source as InputSource).getChildNodes();
            return this.parseNode(var6);
         } else {
            return doc;
         }
      } catch (var7: Exception) {
         var7.printStackTrace();
         return doc;
      }
   }

   public fun parseNode(list: NodeList): MutableMap<String, Any> {
      val doc: java.util.Map = new LinkedHashMap();
      var var9: Int = 0;
      val var4: Int = list.getLength();
      if (0 < var4) {
         do {
            val node: Node = list.item(var9++);
            if (node.getNodeType() == 1) {
               val childNodes: NodeList = node.getChildNodes();
               if (childNodes.getLength() == 1 && node.getFirstChild().getNodeType() == 3) {
                  val var10: java.lang.String = node.getNodeName();
                  val var11: java.lang.String = node.getFirstChild().getNodeValue();
                  doc.put(var10, var11);
               } else if (childNodes.getLength() > 1) {
                  val var8: java.lang.String = node.getNodeName();
                  doc.put(var8, this.parseNode(childNodes));
               }
            }
         } while (var9 < var4);
      }

      return doc;
   }
}
