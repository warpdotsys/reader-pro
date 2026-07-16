package io.legado.app.help

import com.google.gson.JsonParser
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object ContentProcessor {
    data class ReplaceRule(
        val name: String = "",
        val pattern: String = "",
        val replacement: String = "",
        val isRegex: Boolean = true,
        val isEnabled: Boolean = true,
        val scope: String = "content",
        val timeoutMs: Long = 3000,
        val bookName: String = ""
    )

    private val pool = Executors.newCachedThreadPool()

    fun loadRules(userNameSpace: String): List<ReplaceRule> {
        val raw = ExtKt.getStorage("data", userNameSpace, "replaceRule") ?: return emptyList()
        return try {
            JsonParser.parseString(raw).asJsonArray.mapNotNull { el ->
                val o = el.asJsonObject
                ReplaceRule(
                    name = o.get("name")?.asString ?: "",
                    pattern = o.get("pattern")?.asString ?: o.get("regex")?.asString ?: return@mapNotNull null,
                    replacement = o.get("replacement")?.asString ?: o.get("replace")?.asString ?: "",
                    isRegex = o.get("isRegex")?.asBoolean ?: true,
                    isEnabled = o.get("isEnabled")?.asBoolean ?: o.get("enable")?.asBoolean ?: true,
                    scope = o.get("scope")?.asString ?: "content",
                    timeoutMs = o.get("timeout")?.asLong ?: o.get("timeoutMillisecond")?.asLong ?: 3000L,
                    bookName = o.get("bookName")?.asString ?: ""
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun applyContent(ns: String, book: Book?, content: String): String {
        var text = content
        loadRules(ns).filter {
            it.isEnabled && (it.scope == "content" || it.scope == "all" || it.scope.isBlank()) && matches(it, book)
        }.forEach { text = applyOne(text, it) }
        return text
    }

    fun applyTitle(ns: String, book: Book?, title: String): String {
        var text = title
        loadRules(ns).filter {
            it.isEnabled && (it.scope == "title" || it.scope == "all") && matches(it, book)
        }.forEach { text = applyOne(text, it) }
        return text
    }

    private fun matches(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        return name.contains(rule.bookName)
    }

    private fun applyOne(text: String, r: ReplaceRule): String {
        val task = Callable {
            if (r.isRegex) text.replace(Regex(r.pattern), r.replacement)
            else text.replace(r.pattern, r.replacement)
        }
        val f = pool.submit(task)
        return try {
            f.get(r.timeoutMs.coerceAtLeast(100), TimeUnit.MILLISECONDS)
        } catch (_: TimeoutException) {
            f.cancel(true); text
        } catch (_: Exception) {
            text
        }
    }
}
