/** Business rewrite from reader-pro-3.2.14.jar — phase7. */

package io.legado.app.help

import com.google.gson.JsonParser
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Apply replaceRule.json.
 *
 * Fields (legado-compatible):
 * - pattern / regex
 * - replacement / replace
 * - isRegex (default true)
 * - isEnabled / enable
 * - scope: content | title (default content)
 * - timeout: ms for single rule (default 3000)
 * - name
 * - bookName: if set, only apply when book.name contains / matches
 * - useReplace: optional
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
        val bookName: String = "" // empty = all books
    )

    private val pool = Executors.newCachedThreadPool()

    fun loadRules(userNameSpace: String): List<ReplaceRule> {
        val raw = ExtKt.getStorage("data", userNameSpace, "replaceRule") ?: return emptyList()
        return try {
            val arr = JsonParser.parseString(raw).asJsonArray
            arr.mapNotNull { el ->
                val o = el.asJsonObject
                ReplaceRule(
                    name = o.get("name")?.asString ?: "",
                    pattern = o.get("pattern")?.asString ?: o.get("regex")?.asString ?: return@mapNotNull null,
                    replacement = o.get("replacement")?.asString ?: o.get("replace")?.asString ?: "",
                    isRegex = o.get("isRegex")?.asBoolean ?: true,
                    isEnabled = o.get("isEnabled")?.asBoolean
                        ?: o.get("enable")?.asBoolean
                        ?: o.get("isEnabled")?.asBoolean
                        ?: true,
                    scope = o.get("scope")?.asString ?: "content",
                    timeoutMs = o.get("timeout")?.asLong
                        ?: o.get("timeoutMillisecond")?.asLong
                        ?: 3000L,
                    bookName = o.get("bookName")?.asString
                        ?: o.get("nameFilter")?.asString
                        ?: ""
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun applyContent(userNameSpace: String, book: Book?, content: String): String {
        var text = content
        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && (it.scope == "content" || it.scope == "all" || it.scope.isBlank()) && matchesBook(it, book)
        }
        for (r in rules) {
            text = applyOne(text, r)
        }
        return text
    }

    fun applyTitle(userNameSpace: String, book: Book?, title: String): String {
        var text = title
        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && (it.scope == "title" || it.scope == "all") && matchesBook(it, book)
        }
        for (r in rules) {
            text = applyOne(text, r)
        }
        return text
    }

    private fun matchesBook(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        val filter = rule.bookName
        // regex: prefix  or /pattern/
        return when {
            filter.startsWith("regex:") -> runCatching {
                name.contains(Regex(filter.removePrefix("regex:")))
            }.getOrDefault(false)
            filter.startsWith("/") && filter.endsWith("/") && filter.length > 2 -> runCatching {
                Regex(filter.substring(1, filter.length - 1)).containsMatchIn(name)
            }.getOrDefault(false)
            else -> name.contains(filter) || runCatching { name.matches(Regex(filter)) }.getOrDefault(false)
        }
    }

    private fun applyOne(text: String, r: ReplaceRule): String {
        val task = Callable {
            if (r.isRegex) text.replace(Regex(r.pattern), r.replacement)
            else text.replace(r.pattern, r.replacement)
        }
        val future = pool.submit(task)
        return try {
            future.get(r.timeoutMs.coerceAtLeast(100), TimeUnit.MILLISECONDS)
        } catch (_: TimeoutException) {
            future.cancel(true)
            text // skip on timeout
        } catch (_: Exception) {
            text
        }
    }
}
