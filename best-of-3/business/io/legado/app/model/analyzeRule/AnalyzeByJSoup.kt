/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * CSS / JSoup mode with && (concat) || (first hit) %% (zip) via RuleAnalyzer.
 */
class AnalyzeByJSoup(content: Any?) {
    private val doc: Element? = when (content) {
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> content?.toString()?.let { Jsoup.parse(it) }
    }

    fun getString(content: Any?, rule: String): String {
        if (rule.isEmpty()) return ""
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||")
        if (rules.size == 1) return getStringSingle(content, rules[0])
        val parts = ArrayList<String>()
        for (rl in rules) {
            val s = getStringSingle(content, rl)
            if (s.isNotEmpty()) {
                parts += s
                if (analyzer.elementsType == "||") break
            }
        }
        return parts.joinToString("\n")
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        if (rule.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getStringListSingle(content, rules[0])
        val results = ArrayList<List<String>>()
        for (rl in rules) {
            val part = getStringListSingle(content, rl)
            if (part.isNotEmpty()) {
                results += part
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return if (analyzer.elementsType == "%%") zipByIndex(results) else results.flatten()
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        if (rule.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getElementsSingle(content, rules[0])
        val results = ArrayList<List<Element>>()
        for (rl in rules) {
            val part = getElementsSingle(content, rl).filterIsInstance<Element>()
            if (part.isNotEmpty()) {
                results += part
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return if (analyzer.elementsType == "%%") zipByIndex(results) else results.flatten()
    }

    private fun getStringSingle(content: Any?, rule: String): String {
        val el = elementOf(content) ?: return ""
        val (css, attr) = splitAttr(rule)
        val selected = if (css.isEmpty()) el else el.selectFirst(css) ?: return ""
        return readAttr(selected, attr)
    }

    private fun getStringListSingle(content: Any?, rule: String): List<String> {
        val el = elementOf(content) ?: return emptyList()
        val (css, attr) = splitAttr(rule)
        val els: Elements = if (css.isEmpty()) Elements(el) else el.select(css)
        return els.map { readAttr(it, attr) }
    }

    private fun getElementsSingle(content: Any?, rule: String): List<Any> {
        val el = elementOf(content) ?: return emptyList()
        val (css, _) = splitAttr(rule)
        return el.select(css.ifEmpty { "*" }).toList()
    }

    private fun readAttr(el: Element, attr: String): String = when (attr) {
        "", "text" -> el.text()
        "textNodes" -> el.textNodes().joinToString("\n") { it.text() }
        "ownText" -> el.ownText()
        "html", "innerHtml" -> el.html()
        "outerHtml", "all" -> el.outerHtml()
        else -> el.attr(attr)
    }

    private fun elementOf(content: Any?): Element? = when (content) {
        null -> doc
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> doc
    }

    private fun splitAttr(rule: String): Pair<String, String> {
        // last @xxx is attribute; avoid @@ default prefix already stripped by AnalyzeRule
        val idx = rule.lastIndexOf('@')
        if (idx <= 0) return rule to "text"
        // @text / @html / @href
        return rule.substring(0, idx) to rule.substring(idx + 1)
    }

    private fun <T> zipByIndex(results: List<List<T>>): List<T> {
        if (results.isEmpty()) return emptyList()
        val out = ArrayList<T>()
        val max = results.maxOf { it.size }
        for (i in 0 until max) {
            for (list in results) if (i < list.size) out += list[i]
        }
        return out
    }
}
