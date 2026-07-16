package io.legado.app.help

import com.google.gson.JsonParser
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Apply user replace rules to chapter title/content.
 * Supports scope = content | title | all, optional bookName filter
 * (`plain`, `regex:pattern`, or `/pattern/`).
 */
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
        return parseRulesJson(raw)
    }

    fun parseRulesJson(raw: String): List<ReplaceRule> {
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

    fun applyContent(ns: String, book: Book?, content: String): String =
        applyRules(loadRules(ns), book, content, "content")

    fun applyTitle(ns: String, book: Book?, title: String): String =
        applyRules(loadRules(ns), book, title, "title")

    /** Direct apply (tests / offline). */
    fun applyRules(
        rules: List<ReplaceRule>,
        book: Book?,
        text: String,
        scope: String
    ): String {
        var out = text
        rules.filter {
            it.isEnabled &&
                (it.scope.equals(scope, true) || it.scope.equals("all", true) ||
                    (scope == "content" && it.scope.isBlank())) &&
                matchesBook(it, book)
        }.forEach { out = applyOne(out, it) }
        return out
    }

    fun matchesBook(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        val filter = rule.bookName.trim()
        return when {
            filter.startsWith("regex:", ignoreCase = true) -> {
                val pat = filter.substringAfter(':')
                runCatching { Regex(pat).containsMatchIn(name) }.getOrDefault(false)
            }
            filter.length >= 2 && filter.startsWith('/') && filter.endsWith('/') -> {
                val pat = filter.substring(1, filter.length - 1)
                runCatching { Regex(pat).containsMatchIn(name) }.getOrDefault(false)
            }
            else -> name.contains(filter)
        }
    }

    private fun applyOne(text: String, r: ReplaceRule): String {
        val task = Callable {
            if (r.isRegex) {
                // use REPLACE_FIRST only if pattern ends with $single — keep full replace
                text.replace(Regex(r.pattern), r.replacement)
            } else {
                text.replace(r.pattern, r.replacement)
            }
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
