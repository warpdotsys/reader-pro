package me.ag2s.epublib.epub
import me.ag2s.epublib.util.StringUtil
import org.w3c.dom.*
internal object DOMUtil { @JvmStatic fun getAttribute(e: Element, ns: String?, a: String): String = e.getAttributeNS(ns, a).let { if (StringUtil.isEmpty(it)) e.getAttribute(a) else it }; @JvmStatic fun getTextChildrenContent(e: Element?): String? { if (e == null) return null; return buildString { val nodes=e.childNodes; for(i in 0 until nodes.length) (nodes.item(i) as? Text)?.let { append(it.data) } }.trim() } }
