package io.legado.app.data.entities

import com.htmake.reader.utils.LRUCache
import com.htmake.reader.utils.UserMutex
import io.legado.app.model.DebugLog
import io.legado.app.utils.attempt
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RecoveredSupportTypesTest {

    @Test
    fun `bookmark keyword and search result preserve jar defaults`() {
        val before = System.currentTimeMillis()
        val bookmark = Bookmark()
        val keyword = SearchKeyword()
        val after = System.currentTimeMillis()

        assertTrue(bookmark.time in before..after)
        assertEquals("", bookmark.bookName)
        assertEquals("", bookmark.bookAuthor)
        assertEquals(0, bookmark.chapterIndex)
        assertEquals(1, keyword.usage)
        assertTrue(keyword.lastUseTime in before..after)
        assertEquals(SearchResult(), SearchResult())
    }

    @Test
    fun `attempt carries successful nulls and short circuits failures`() {
        val nullSuccess = attempt<String?> { null }
        assertTrue(nullSuccess.hasValue)
        assertFalse(nullSuccess.isError)
        assertNull(nullSuccess.value)

        val transformed = attempt { 21 }.then { it * 2 }
        assertEquals(42, transformed.value)
        assertNull(transformed.error)

        val failure = attempt<Int> { error("boom") }
        val chained = failure.then { it * 2 }
        assertTrue(failure.isError)
        assertSame(failure, chained)
        assertEquals("boom", chained.error?.message)
    }

    @Test
    fun `lru cache updates recency before evicting`() {
        val cache = LRUCache<String, Int>(2)
        cache.put("a", 1)
        cache.put("b", 2)
        assertEquals("b:2 a:1 ", cache.toString())

        assertEquals(1, cache.get("a"))
        cache.put("c", 3)

        assertNull(cache.get("b"))
        assertEquals("c:3 a:1 ", cache.toString())
        assertEquals(1, cache.remove("a")?.value)
        assertEquals("c:3 ", cache.toString())

        cache.clear()
        assertEquals("", cache.toString())
    }

    @Test
    fun `user mutex reuses the locker for a key`() = runBlocking {
        val key = "recovered-support-types"
        val first = UserMutex.getLocker(key)
        val second = UserMutex.getLocker(key)

        assertSame(first, second)
    }

    @Test
    fun `rss article identity and variables follow jar behavior`() {
        val article = RssArticle(
            origin = "feed",
            title = "first title",
            link = "https://example.test/article",
            variable = "{\"seed\":\"value\"}"
        )
        val sameIdentity = article.copy(title = "different title", read = true)

        assertEquals(article, sameIdentity)
        assertEquals(article.hashCode(), sameIdentity.hashCode())
        assertEquals("value", article.getVariable("seed"))

        article.putVariable("next", "item")
        article.putVariable("seed", null)
        assertEquals("item", article.getVariable("next"))
        assertNull(article.getVariable("seed"))
        assertNotNull(article.variable)

        article.setUserNameSpace("alice")
        assertEquals("alice", article.getUserNameSpace())
    }

    @Test
    fun `http tts parses legacy login ui and keeps jar defaults`() {
        val parsed = HttpTTS.fromJson(
            """{
                "id": 17,
                "name": "sample",
                "url": "https://example.test/tts",
                "loginUi": [{"name": "token"}],
                "header": "{\"X-Test\":\"yes\"}"
            }"""
        ).getOrThrow()

        assertEquals(17L, parsed.id)
        assertEquals("httpTts:17", parsed.getKey())
        assertTrue(parsed.loginUi?.contains("token") == true)
        assertNull(parsed.concurrentRate)
        assertFalse(parsed.enabledCookieJar ?: true)
        assertTrue(parsed.lastUpdateTime > 0)

        val defaults = HttpTTS()
        assertEquals("0", defaults.concurrentRate)
        assertFalse(defaults.enabledCookieJar ?: true)
    }

    @Test
    fun `rss source parser and comparison retain jar compatibility rules`() {
        val parsed = RssSource.fromJson(
            """{
                "sourceUrl": "https://example.test/feed",
                "sourceName": "Example",
                "variableComment": "ignored by migration",
                "loginUi": [{"name": "ignored"}],
                "enabledCookieJar": true
            }"""
        ).getOrThrow()

        assertEquals("", parsed.sourceIcon)
        assertTrue(parsed.enabled)
        assertTrue(parsed.enabledCookieJar == true)
        assertNull(parsed.variableComment)
        assertNull(parsed.loginUi)

        parsed.sortUrl = "News::/news&&Books::/books"
        assertEquals(listOf("News" to "/news", "Books" to "/books"), parsed.sortUrls())

        val equivalent = parsed.copy(
            sourceName = "name is not compared",
            sourceGroup = "",
            sourceComment = ""
        )
        parsed.sourceGroup = null
        parsed.sourceComment = null
        assertTrue(parsed.equal(equivalent))

        equivalent.enabledCookieJar = false
        assertFalse(parsed.equal(equivalent))

        parsed.setLogger(object : DebugLog {})
        assertNull(parsed.getLogger())
    }
}
