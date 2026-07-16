package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class AnalyzeByJSoup(content: Any?) {
    private val root: Element = when (content) {
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> Jsoup.parse(content?.toString() ?: "")
    }

    fun getString(content: Any?, rule: String): String {
        val el = elementOf(content) ?: return ""
        val (css, attr) = splitAttr(rule)
        val selected = if (css.isEmpty()) el else el.selectFirst(css) ?: return ""
        return readAttr(selected, attr)
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        val el = elementOf(content) ?: return emptyList()
        val (css, attr) = splitAttr(rule)
        val els: Elements = if (css.isEmpty()) Elements(el) else el.select(css)
        return els.map { readAttr(it, attr) }.filter { it.isNotEmpty() }
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        val el = elementOf(content) ?: return emptyList()
        val (css, _) = splitAttr(rule)
        return if (css.isEmpty()) listOf(el) else el.select(css).toList()
    }

    private fun elementOf(content: Any?): Element? = when (content) {
        null -> root
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> root
    }

    private fun splitAttr(rule: String): Pair<String, String> {
        val idx = rule.lastIndexOf('@')
        return if (idx > 0) rule.substring(0, idx) to rule.substring(idx + 1)
        else rule to "text"
    }

    private fun readAttr(el: Element, attr: String): String = when (attr.lowercase()) {
        "text", "textNodes" -> el.text()
        "html", "innerHtml" -> el.html()
        "outerHtml" -> el.outerHtml()
        "href", "src" -> el.attr(attr)
        else -> el.attr(attr).ifEmpty { el.text() }
    }
}
