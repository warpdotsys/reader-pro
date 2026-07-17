package io.legado.app.model.analyzeRule

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class AnalyzeByJSonPath(content: Any?) {
    private val ctx = parseAny(content)

    private fun parseAny(content: Any?): DocumentContext = try {
        when (content) {
            null -> JsonPath.parse("{}")
            is String -> JsonPath.parse(content)
            is Map<*, *> -> JsonPath.parse(JsonObject(content as Map<String, Any?>).encode())
            is List<*> -> JsonPath.parse(JsonArray(content).encode())
            else -> JsonPath.parse(content.toString())
        }
    } catch (_: Exception) {
        JsonPath.parse("{}")
    }

    fun getString(content: Any?, rule: String): String {
        return try {
            val c = if (content != null) parseAny(content) else ctx
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
            val c = if (content != null) parseAny(content) else ctx
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

    /** 与原版一致：元素保持原始对象（Map/List），后续规则继续在元素上求值 */
    fun getElements(content: Any?, rule: String): List<Any> = try {
        val c = if (content != null) parseAny(content) else ctx
        val v = c.read<Any>(rule)
        when (v) {
            is List<*> -> v.filterNotNull()
            null -> emptyList()
            else -> listOf(v)
        }
    } catch (_: Exception) {
        emptyList()
    }
}
