package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.exception.NoStackTraceException
import io.legado.app.exception.TocEmptyException
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.lang.reflect.Method
import kotlin.coroutines.Continuation
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class WebBookAlignmentTest {

    @Test
    fun `web book exposes target jar coroutine descriptors`() {
        val methods = WebBook::class.java.declaredMethods.associateBy { it.name }

        assertEquals(
            "(Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            descriptor(methods.getValue("searchBook"))
        )
        assertEquals(
            "(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            descriptor(methods.getValue("getChapterList"))
        )
        assertEquals(
            "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;" +
                "Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            descriptor(methods.getValue("getBookContent"))
        )
        assertEquals(
            "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            descriptor(methods.getValue("preciseSearch-0E7RQCE"))
        )

        val constructors = WebBook::class.java.declaredConstructors.map { constructor ->
            constructor.parameterTypes.joinToString(separator = "", prefix = "(", postfix = ")") {
                descriptor(it)
            }
        }.toSet()
        assertTrue(
            "(Lio/legado/app/data/entities/BookSource;ZLio/legado/app/model/DebugLog;" +
                "Ljava/lang/String;)" in constructors
        )
        assertTrue(
            "(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;)" in constructors
        )
        assertTrue(
            "(Lio/legado/app/data/entities/BookSource;ZLio/legado/app/model/DebugLog;" +
                "Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)" in constructors
        )
        assertTrue(
            "(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;" +
                "ILkotlin/jvm/internal/DefaultConstructorMarker;)" in constructors
        )
    }

    @Test
    fun `parser objects retain target jar suspend ABI`() {
        assertSuspendDescriptor(
            BookList::class.java,
            "analyzeBookList",
            "(Ljava/lang/String;Lio/legado/app/data/entities/BookSource;" +
                "Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;" +
                "Lio/legado/app/data/entities/SearchBook;ZLio/legado/app/model/DebugLog;" +
                "Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertSuspendDescriptor(
            BookInfo::class.java,
            "analyzeBookInfo",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;" +
                "Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Z" +
                "Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertSuspendDescriptor(
            BookInfo::class.java,
            "analyzeBookInfo",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;" +
                "Lio/legado/app/model/analyzeRule/AnalyzeRule;" +
                "Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Z" +
                "Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertSuspendDescriptor(
            BookChapterList::class.java,
            "analyzeChapterList",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;" +
                "Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;" +
                "Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertSuspendDescriptor(
            BookContent::class.java,
            "analyzeContent",
            "(Ljava/lang/String;Lio/legado/app/data/entities/Book;" +
                "Lio/legado/app/data/entities/BookChapter;Lio/legado/app/data/entities/BookSource;" +
                "Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;" +
                "Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
    }

    @Test
    fun `synthetic constructor masks preserve explicit namespace and apply defaults`() {
        val constructor = WebBook::class.java.declaredConstructors.single {
            it.parameterTypes.size == 6 && it.parameterTypes.first() == BookSource::class.java
        }
        val source = BookSource()
        val logger = object : DebugLog {}

        val explicitNamespace = constructor.newInstance(
            source,
            false,
            logger,
            "tenant-a",
            6,
            null
        ) as WebBook
        assertTrue(explicitNamespace.debugLog)
        assertNull(explicitNamespace.debugLogger)
        assertEquals("tenant-a", explicitNamespace.userNameSpace)

        val allDefaults = constructor.newInstance(
            source,
            false,
            logger,
            "tenant-a",
            14,
            null
        ) as WebBook
        assertTrue(allDefaults.debugLog)
        assertNull(allDefaults.debugLogger)
        assertNull(allDefaults.userNameSpace)
    }

    @Test
    fun `namespace logger and precise search failure match target jar`() = runBlocking {
        val source = BookSource(searchUrl = null)
        val explicitLogger = object : DebugLog {}
        val webBook = WebBook(
            bookSource = source,
            debugLog = false,
            debugLogger = explicitLogger
        )

        assertEquals("unknow", webBook.userNS)
        assertTrue(webBook.searchBook("missing").isEmpty())
        assertEquals("unknow", source.getUserNameSpace())
        assertSame(explicitLogger, source.getLogger())

        val result = webBook.preciseSearch("Missing", "Nobody")
        assertFalse(result.isSuccess)
        val exception = assertIs<NoStackTraceException>(result.exceptionOrNull())
        assertEquals("未搜索到 Missing(Nobody) 书籍", exception.message)

        webBook.userNameSpace = "tenant-a"
        val book = Book(tocUrl = "https://example.test/book")
        val chapter = BookChapter(url = "https://example.test/chapter")
        assertEquals(chapter.url, webBook.getBookContent(book, chapter))
        assertEquals("tenant-a", book.getUserNameSpace())
        assertEquals("tenant-a", source.getUserNameSpace())
    }

    @Test
    fun `parser null bodies retain target jar exception messages`() = runBlocking {
        val source = BookSource(bookSourceUrl = "https://source.test")
        val book = Book()
        val chapter = BookChapter()
        val analyzeUrl = AnalyzeUrl("https://example.test", source = source)
        val analyzeRule = AnalyzeRule(book, source)

        assertExceptionMessage("error_get_web_content") {
            BookList.analyzeBookList(
                null,
                source,
                analyzeUrl,
                "https://example.test",
                io.legado.app.data.entities.SearchBook()
            )
        }
        assertExceptionMessage("error_get_web_content: https://example.test") {
            BookInfo.analyzeBookInfo(
                book,
                null,
                source,
                "https://example.test",
                "https://redirect.test",
                true
            )
        }
        assertExceptionMessage("error_get_web_content: https://example.test") {
            BookInfo.analyzeBookInfo(
                book,
                null,
                analyzeRule,
                source,
                "https://example.test",
                "https://redirect.test",
                true
            )
        }
        assertExceptionMessage("error_get_web_content") {
            BookChapterList.analyzeChapterList(
                book,
                null,
                source,
                "https://example.test",
                "https://redirect.test"
            )
        }
        assertExceptionMessage("error_get_web_content") {
            BookContent.analyzeContent(
                null,
                book,
                chapter,
                source,
                "https://example.test",
                "https://redirect.test"
            )
        }

        val tocError = try {
            BookChapterList.analyzeChapterList(
                book,
                "<html></html>",
                source,
                "https://example.test",
                "https://redirect.test"
            )
            throw AssertionError("Expected an empty chapter list to fail")
        } catch (error: TocEmptyException) {
            error
        }
        assertEquals("目录为空", tocError.message)
    }

    private suspend fun assertExceptionMessage(expected: String, block: suspend () -> Unit) {
        val error = try {
            block()
            throw AssertionError("Expected parser to reject a null response body")
        } catch (error: Exception) {
            error
        }
        assertEquals(expected, error.message)
    }

    private fun assertSuspendDescriptor(type: Class<*>, name: String, expected: String) {
        val descriptors = type.declaredMethods
            .filter { it.name == name && it.parameterTypes.lastOrNull() == Continuation::class.java }
            .map(::descriptor)
        assertTrue(expected in descriptors)
    }

    private fun descriptor(method: Method): String = method.parameterTypes.joinToString(
        separator = "",
        prefix = "(",
        postfix = ")${descriptor(method.returnType)}"
    ) { descriptor(it) }

    private fun descriptor(type: Class<*>): String = when {
        type === Void.TYPE -> "V"
        type === Boolean::class.javaPrimitiveType -> "Z"
        type === Byte::class.javaPrimitiveType -> "B"
        type === Char::class.javaPrimitiveType -> "C"
        type === Short::class.javaPrimitiveType -> "S"
        type === Int::class.javaPrimitiveType -> "I"
        type === Long::class.javaPrimitiveType -> "J"
        type === Float::class.javaPrimitiveType -> "F"
        type === Double::class.javaPrimitiveType -> "D"
        type.isArray -> "[${descriptor(type.componentType)}"
        else -> "L${type.name.replace('.', '/')};"
    }
}
