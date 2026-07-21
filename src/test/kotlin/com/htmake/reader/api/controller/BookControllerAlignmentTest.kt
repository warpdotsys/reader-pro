package com.htmake.reader.api.controller

import io.legado.app.data.entities.Book
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class BookControllerAlignmentTest {

    @Test
    fun `book controller retains the target controller surface`() {
        val contextMethods = listOf(
            "getInvalidBookSources",
            "getBookInfo",
            "getBookCover",
            "importBookPreview",
            "getTxtTocRules",
            "getChapterListByRule",
            "refreshLocalBook",
            "getChapterList",
            "saveBookProgress",
            "getBookContent",
            "saveBookContent",
            "exploreBook",
            "searchBook",
            "searchBookMulti",
            "searchBookMultiSSE",
            "searchBookSource",
            "searchBookSourceSSE",
            "getAvailableBookSource",
            "getBookshelf",
            "getShelfBook",
            "saveBook",
            "setBookSource",
            "saveBookConfig",
            "saveBookGroupId",
            "addBookGroupMulti",
            "removeBookGroupMulti",
            "deleteBook",
            "deleteBooks",
            "bookSourceDebugSSE",
            "cacheBookSSE",
            "cacheBookOnServer",
            "deleteBookCache",
            "textToSpeech",
            "getShelfBookWithCacheInfo",
            "exportBook",
            "searchBookContent",
            "backupToMongodb",
            "restoreFromMongodb",
        )
        contextMethods.forEach { method ->
            assertDescriptor(
                BookController::class.java,
                method,
                "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            )
        }

        assertDescriptor(
            BookController::class.java,
            "saveBookToShelf",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/vertx/ext/web/RoutingContext;)Lkotlin/Pair;",
        )
        assertDescriptor(
            BookController::class.java,
            "getLocalChapterList",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;Z" +
                "Lkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertDescriptor(
            BookController::class.java,
            "cacheBookOnServer",
            "(Lio/vertx/core/json/JsonArray;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertDescriptor(
            BookController::class.java,
            "saveShelfBookProgress",
            "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;" +
                "Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertEquals(
            "io.legado.app.utils.ACache",
            BookController::class.java.getDeclaredField("bookInfoCache").type.name,
        )
        assertEquals(Int::class.javaPrimitiveType, BookController::class.java.getDeclaredField("concurrentLoopCount").type)
        assertEquals(
            "io.vertx.ext.web.client.WebClient",
            BookController::class.java.getDeclaredField("webClient").type.name,
        )
        assertEquals("kotlin.Lazy", BookController::class.java.getDeclaredField("backupFileNames\$delegate").type.name)
    }

    @Test
    fun `book controller keeps target Kotlin default overloads`() {
        listOf(
            "searchBookWithSource",
            "saveBookCover",
            "getBookShelfBooks",
            "getLocalChapterList",
            "getBookSourceString",
            "saveShelfBookLatestChapter",
            "saveBookSources",
            "extractEpub",
            "extractCbz",
            "convertPdfToImage",
            "convertPdfPageToImage",
            "saveToWebdav",
            "createUserBackup",
            "ttsByEdge",
            "ttsByApi",
            "ttsByTextToSpeechCn",
        ).forEach { method ->
            assertTrue(
                BookController::class.java.declaredMethods.any { it.name == "${method}\$default" },
                "missing $method default overload",
            )
        }

        assertDescriptor(
            BookController::class.java,
            "getChapterCacheDir",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/io/File;",
        )
        assertDescriptor(
            BookController::class.java,
            "getCachedChapterContentSet",
            "(Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/util/Set;",
        )
        assertDescriptor(
            BookController::class.java,
            "searchChapter",
            "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;" +
                "Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
    }

    @Test
    fun `book controller does not collapse overloaded cache entry point`() {
        val overloads = BookController::class.java.declaredMethods.filter { it.name == "cacheBookOnServer" }
        assertTrue(overloads.any { it.parameterTypes.firstOrNull() == RoutingContext::class.java })
        assertTrue(overloads.any { it.parameterTypes.firstOrNull() == JsonArray::class.java })
        assertEquals(Book::class.java, BookController::class.java.getDeclaredMethod(
            "getShelfBookByURL",
            String::class.java,
            String::class.java,
        ).returnType)
    }

    private fun assertDescriptor(type: Class<*>, name: String, expected: String) {
        val actual = type.declaredMethods.filter { it.name == name }.map(::descriptor)
        assertTrue(expected in actual, "$type.$name descriptors were $actual")
    }

    private fun descriptor(method: java.lang.reflect.Method): String =
        method.parameterTypes.joinToString(separator = "", prefix = "(", postfix = ")") { descriptor(it) } +
            descriptor(method.returnType)

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
