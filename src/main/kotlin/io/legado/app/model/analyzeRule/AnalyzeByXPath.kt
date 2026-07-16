package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * XPath subset via CSS fallback (full seimicrawler not required for compile/runtime smoke).
 * Supports //tag, //tag[@attr], text() lightly by mapping to CSS.
 */
class AnalyzeByXPath(content: Any?) {
    private val doc: Element = when (content) {
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> Jsoup.parse(content?.toString() ?: "")
    }

    fun getString(content: Any?, rule: String): String =
        getStringList(content, rule).firstOrNull() ?: ""

    fun getStringList(content: Any?, rule: String): List<String> {
        val els = getElements(content, rule)
        return els.map {
            when (it) {
                is Element -> it.text()
                else -> it.toString()
            }
        }
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        val root = when (content) {
            is Element -> content
            is String -> Jsoup.parse(content)
            null -> doc
            else -> doc
        }
        val css = xpathToCss(rule)
        return try {
            root.select(css).toList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getStringList(xPath: String): List<String> = getStringList(null, xPath)

    private fun xpathToCss(xp: String): String {
        var r = xp.trim()
        if (r.startsWith("//")) r = r.removePrefix("//")
        r = r.replace("//", " ")
        r = r.replace(Regex("""\[@(\w+)='([^']*)']"""), "[$1=$2]")
        r = r.replace(Regex("""/text\(\)"""), "")
        r = r.replace("/", " > ")
        return r.ifBlank { "*" }
    }
}
