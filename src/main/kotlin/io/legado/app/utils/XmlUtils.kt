package io.legado.app.utils

import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource

object XmlUtils {

    fun xml2map(source: Any): MutableMap<String, Any> {
        val factory = DocumentBuilderFactory.newInstance()
        val doc: MutableMap<String, Any> = linkedMapOf()
        return try {
            val builder = factory.newDocumentBuilder()
            if (source is String) {
                val document = builder.parse(source)
                return parseNode(document.getChildNodes())
            }
            if (source is InputStream) {
                val document = builder.parse(source)
                return parseNode(document.getChildNodes())
            }
            if (source is InputSource) {
                val document = builder.parse(source)
                return parseNode(document.getChildNodes())
            }
            doc
        } catch (e: Exception) {
            e.printStackTrace()
            doc
        }
    }

    fun parseNode(list: NodeList): MutableMap<String, Any> {
        val doc = linkedMapOf<String, Any>()
        for (i in 0 until list.length) {
            val node = list.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val childNodes = node.childNodes
                if (childNodes.length == 1 && node.firstChild.nodeType == Node.TEXT_NODE) {
                    doc[node.nodeName] = node.firstChild.nodeValue
                } else if (childNodes.length > 1) {
                    doc[node.nodeName] = parseNode(childNodes)
                }
            }
        }
        return doc
    }
}
