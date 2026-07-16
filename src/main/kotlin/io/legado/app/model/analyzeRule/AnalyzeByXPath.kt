package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import us.codecraft.xsoup.Xsoup

/**
 * XPath engine powered by **Xsoup** (Jsoup + XPath), closer to legado/seimicrawler usage.
 * Supports `&&` / `||` / `%%` via [RuleAnalyzer].
 */
class AnalyzeByXPath(content: Any?) {
    private val doc: Document = when (content) {
        is Document -> content
        is Element -> {
            val owner = content.ownerDocument()
            if (owner != null) owner else Jsoup.parse(content.outerHtml())
        }
        is String -> Jsoup.parse(content)
        else -> Jsoup.parse(content?.toString() ?: "")
    }

    fun getString(content: Any?, rule: String): String =
        getStringList(content, rule).firstOrNull() ?: ""

    fun getStringList(content: Any?, rule: String): List<String> {
        if (rule.isBlank()) return emptyList()
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getStringListSingle(content, rules[0])
        val parts = ArrayList<List<String>>()
        for (rl in rules) {
            val part = getStringListSingle(content, rl)
            if (part.isNotEmpty()) {
                parts += part
                if (analyzer.elementsType == "||") break
            }
        }
        if (parts.isEmpty()) return emptyList()
        return if (analyzer.elementsType == "%%") zipByIndex(parts) else parts.flatten()
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        if (rule.isBlank()) return emptyList()
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getElementsSingle(content, rules[0])
        val parts = ArrayList<List<Any>>()
        for (rl in rules) {
            val part = getElementsSingle(content, rl)
            if (part.isNotEmpty()) {
                parts += part
                if (analyzer.elementsType == "||") break
            }
        }
        if (parts.isEmpty()) return emptyList()
        return if (analyzer.elementsType == "%%") zipByIndex(parts) else parts.flatten()
    }

    fun getStringList(xPath: String): List<String> = getStringList(null, xPath)

    private fun getStringListSingle(content: Any?, rule: String): List<String> {
        val root = elementOf(content)
        val r = normalize(rule)
        // attribute / text node shortcuts
        return try {
            val elements = evalElements(root, stripResult(r))
            when {
                r.endsWith("/text()") || r.endsWith("//text()") ->
                    elements.map { it.text() }.filter { it.isNotBlank() }
                r.contains("/@") -> {
                    val attr = r.substringAfterLast("/@")
                    elements.map { it.attr(attr) }.filter { it.isNotBlank() }
                }
                else -> {
                    // Xsoup list() returns strings when xpath ends with text()
                    val xs = Xsoup.compile(r).evaluate(root)
                    val list = xs.list()
                    if (list.isNotEmpty()) list.filter { it.isNotBlank() }
                    else elements.map { it.text() }.filter { it.isNotBlank() }
                }
            }
        } catch (_: Exception) {
            // CSS fallback
            try {
                root.select(xpathToCss(r)).map { it.text() }.filter { it.isNotBlank() }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    private fun getElementsSingle(content: Any?, rule: String): List<Any> {
        val root = elementOf(content)
        val r = normalize(stripResult(rule))
        return try {
            evalElements(root, r)
        } catch (_: Exception) {
            try {
                root.select(xpathToCss(r)).toList()
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    private fun evalElements(root: Element, xpath: String): List<Element> {
        val xs = Xsoup.compile(xpath).evaluate(root)
        val elements: Elements = xs.getElements()
        return elements.toList()
    }

    private fun elementOf(content: Any?): Element = when (content) {
        null -> doc
        is Document -> content
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> doc
    }

    private fun normalize(rule: String): String {
        var r = rule.trim()
        if (r.startsWith("@XPath:", true)) r = r.substringAfter(':')
        return r
    }

    private fun stripResult(rule: String): String {
        var r = rule
        // /text() and /@attr kept for string path; for elements strip trailing text()
        if (r.endsWith("/text()")) r = r.removeSuffix("/text()")
        if (r.contains("/@") && !r.endsWith(")")) {
            // keep parent path for element selection when reading attr later
            r = r.substringBeforeLast("/@")
        }
        return r
    }

    private fun <T> zipByIndex(parts: List<List<T>>): List<T> {
        val max = parts.maxOf { it.size }
        val out = ArrayList<T>()
        for (i in 0 until max) {
            for (p in parts) if (i < p.size) out += p[i]
        }
        return out
    }

    /** Last-resort CSS mapping */
    private fun xpathToCss(xp: String): String {
        var r = xp.trim()
        if (r.startsWith("//")) r = r.removePrefix("//")
        r = r.replace("//", " ")
        r = r.replace(Regex("""\[@(\w+)='([^']*)']"""), "[$1=$2]")
        r = r.replace(Regex("""\[@(\w+)="([^"]*)"]"""), "[$1=$2]")
        r = r.replace(Regex("""/text\(\)"""), "")
        r = r.replace("/", " > ")
        return r.ifBlank { "*" }
    }
}
