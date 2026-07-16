/** Business rewrite from reader-pro-3.2.14.jar — phase3. */

package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.seimicrawler.xpath.JXDocument
import org.seimicrawler.xpath.JXNode

/**
 * XPath evaluator using seimicrawler (same as jar).
 * Supports && (union all) || (first non-empty) %% (zip by index) via RuleAnalyzer.
 */
class AnalyzeByXPath(doc: Any?) {
    private val jxNode: Any = parse(doc)

    private fun parse(doc: Any?): Any {
        return when (doc) {
            is JXNode -> if (doc.isElement) doc else strToJXDocument(doc.toString())
            is Document -> JXDocument.create(doc)
            is Element -> JXDocument.create(Elements(doc))
            is Elements -> JXDocument.create(doc)
            is String -> strToJXDocument(doc)
            null -> strToJXDocument("")
            else -> strToJXDocument(doc.toString())
        }
    }

    /** Wrap incomplete HTML fragments so XPath engine can parse tables. */
    private fun strToJXDocument(html: String): JXDocument {
        var html1 = html
        if (html1.endsWith("</td>")) html1 = "<tr>$html1</tr>"
        if (html1.endsWith("</tr>") || html1.endsWith("</tbody>")) html1 = "<table>$html1</table>"
        return JXDocument.create(html1)
    }

    private fun getResult(xPath: String): List<JXNode> {
        return try {
            when (val n = jxNode) {
                is JXNode -> n.sel(xPath) ?: emptyList()
                is JXDocument -> n.selN(xPath) ?: emptyList()
                else -> emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getElements(xPath: String): List<Any> {
        if (xPath.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(xPath)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getResult(rules[0])

        val results = ArrayList<List<JXNode>>()
        for (rl in rules) {
            val part = getElements(rl).filterIsInstance<JXNode>()
            if (part.isNotEmpty()) {
                results.add(part)
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return when (analyzer.elementsType) {
            "%%" -> zipByIndex(results)
            else -> results.flatten()
        }
    }

    fun getStringList(xPath: String): List<String> {
        if (xPath.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(xPath)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) {
            return getResult(xPath).map { it.asString() ?: "" }
        }
        val results = ArrayList<List<String>>()
        for (rl in rules) {
            val part = getStringList(rl)
            if (part.isNotEmpty()) {
                results.add(part)
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return when (analyzer.elementsType) {
            "%%" -> zipByIndex(results)
            else -> results.flatten()
        }
    }

    fun getString(rule: String): String {
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||")
        if (rules.size == 1) {
            val nodes = getResult(rule)
            return nodes.joinToString("\n") { it.asString() ?: "" }
        }
        val parts = ArrayList<String>()
        for (rl in rules) {
            val s = getString(rl)
            if (s.isNotEmpty()) {
                parts += s
                if (analyzer.elementsType == "||") break
            }
        }
        return parts.joinToString("\n")
    }

    // Adapt to AnalyzeRule call sites that pass (content, rule)
    fun getString(content: Any?, rule: String): String = AnalyzeByXPath(content ?: jxNode).getString(rule)
    fun getStringList(content: Any?, rule: String): List<String> = AnalyzeByXPath(content ?: jxNode).getStringList(rule)
    fun getElements(content: Any?, rule: String): List<Any> = AnalyzeByXPath(content ?: jxNode).getElements(rule)

    private fun <T> zipByIndex(results: List<List<T>>): List<T> {
        if (results.isEmpty()) return emptyList()
        val out = ArrayList<T>()
        val max = results.maxOf { it.size }
        for (i in 0 until max) {
            for (list in results) {
                if (i < list.size) out += list[i]
            }
        }
        return out
    }
}
