/** Business rewrite from reader-pro-3.2.14.jar — phase2. Readability/audit. */

package io.legado.app.model.analyzeRule

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Configuration

/** JSONPath mode (Jayway). */
class AnalyzeByJSonPath(content: Any?) {
    private val document: Any? = when (content) {
        is String -> try { JsonPath.parse(content) } catch (_: Exception) { null }
        else -> content
    }

    fun getString(content: Any?, rule: String): String {
        return try {
            val ctx = parse(content)
            ctx?.read<Any>(rule)?.toString() ?: ""
        } catch (_: Exception) { "" }
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        return try {
            val ctx = parse(content)
            val v = ctx?.read<Any>(rule)
            when (v) {
                is List<*> -> v.map { it.toString() }
                null -> emptyList()
                else -> listOf(v.toString())
            }
        } catch (_: Exception) { emptyList() }
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        return try {
            val ctx = parse(content)
            when (val v = ctx?.read<Any>(rule)) {
                is List<*> -> v.filterNotNull()
                null -> emptyList()
                else -> listOf(v)
            }
        } catch (_: Exception) { emptyList() }
    }

    private fun parse(content: Any?): com.jayway.jsonpath.DocumentContext? = when (content) {
        is com.jayway.jsonpath.DocumentContext -> content
        is String -> JsonPath.parse(content)
        else -> document as? com.jayway.jsonpath.DocumentContext
    }
}
