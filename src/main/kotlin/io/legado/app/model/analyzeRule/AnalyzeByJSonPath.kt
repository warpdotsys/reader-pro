package io.legado.app.model.analyzeRule

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException

class AnalyzeByJSonPath(content: Any?) {
    private val ctx = try {
        when (content) {
            is String -> JsonPath.parse(content)
            else -> JsonPath.parse(content?.toString() ?: "{}")
        }
    } catch (_: Exception) {
        JsonPath.parse("{}")
    }

    fun getString(content: Any?, rule: String): String {
        return try {
            val c = content?.let {
                if (it is String) JsonPath.parse(it) else ctx
            } ?: ctx
            val v = c.read<Any>(rule)
            when (v) {
                is List<*> -> v.firstOrNull()?.toString() ?: ""
                else -> v?.toString() ?: ""
            }
        } catch (_: PathNotFoundException) {
            ""
        } catch (_: Exception) {
            ""
        }
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        return try {
            val c = content?.let {
                if (it is String) JsonPath.parse(it) else ctx
            } ?: ctx
            val v = c.read<Any>(rule)
            when (v) {
                is List<*> -> v.mapNotNull { it?.toString() }
                null -> emptyList()
                else -> listOf(v.toString())
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getElements(content: Any?, rule: String): List<Any> =
        getStringList(content, rule)
}
