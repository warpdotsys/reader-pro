package com.htmake.reader.schedule

import com.htmake.reader.api.controller.MongoBackup
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Refresh latest chapter metadata for shelf books (all user namespaces).
 * Used by scheduled shelf job — best-effort, per-book timeout.
 */
object ShelfRefresh {

    data class Result(
        val users: Int = 0,
        val books: Int = 0,
        val updated: Int = 0,
        val failed: Int = 0,
        val skipped: Int = 0
    )

    fun refreshAll(
        debugLog: Boolean = false,
        perBookTimeoutMs: Long = 15_000,
        maxBooksPerUser: Int = 50
    ): Result {
        var users = 0
        var books = 0
        var updated = 0
        var failed = 0
        var skipped = 0
        val namespaces = MongoBackup.listUserNamespaces()
        for (ns in namespaces) {
            users++
            val r = refreshUser(ns, debugLog, perBookTimeoutMs, maxBooksPerUser)
            books += r.books
            updated += r.updated
            failed += r.failed
            skipped += r.skipped
        }
        return Result(users, books, updated, failed, skipped)
    }

    fun refreshUser(
        ns: String,
        debugLog: Boolean = false,
        perBookTimeoutMs: Long = 15_000,
        maxBooksPerUser: Int = 50
    ): Result {
        val raw = ExtKt.getStorage("data", ns, "bookshelf") ?: return Result()
        val arr = ExtKt.asJsonArray(raw) ?: return Result()
        if (arr.isEmpty) return Result()
        val list = arr.list.toMutableList()
        var books = 0
        var updated = 0
        var failed = 0
        var skipped = 0
        var changed = false
        val limit = minOf(list.size, maxBooksPerUser)
        for (i in 0 until limit) {
            val o = arr.getJsonObject(i) ?: continue
            val book = try {
                o.mapTo(Book::class.java)
            } catch (_: Exception) {
                skipped++
                continue
            }
            books++
            if (!book.canUpdate || book.isLocalBook) {
                skipped++
                continue
            }
            val sourceStr = findBookSource(ns, book)
            if (sourceStr == null) {
                skipped++
                continue
            }
            val beforeTitle = book.latestChapterTitle
            val beforeNum = book.totalChapterNum
            try {
                val ok = runBlocking {
                    withTimeoutOrNull(perBookTimeoutMs) {
                        refreshOne(book, sourceStr, ns, debugLog)
                        true
                    } ?: false
                }
                if (!ok) {
                    failed++
                    continue
                }
                if (book.latestChapterTitle != beforeTitle || book.totalChapterNum != beforeNum) {
                    updated++
                }
                book.lastCheckTime = System.currentTimeMillis()
                list[i] = JsonObject.mapFrom(book)
                changed = true
            } catch (_: Exception) {
                failed++
                book.lastCheckError = "update failed"
                book.lastCheckTime = System.currentTimeMillis()
                list[i] = JsonObject.mapFrom(book)
                changed = true
            }
        }
        if (changed) {
            ExtKt.saveStorage(arrayOf("data", ns, "bookshelf"), JsonArray(list).encode())
        }
        return Result(1, books, updated, failed, skipped)
    }

    private suspend fun refreshOne(book: Book, sourceStr: String, ns: String, debugLog: Boolean) {
        val chapters = try {
            WebBook(sourceStr, debugLog, null, ns).getChapterList(book)
        } catch (_: Exception) {
            if (book.isLocalBook) LocalBook.getChapterList(book) else emptyList()
        }
        if (chapters.isNotEmpty()) {
            book.totalChapterNum = chapters.size
            book.latestChapterTitle = chapters.last().title
            book.lastCheckCount = chapters.size
            book.lastCheckError = null
        }
        book.lastCheckTime = System.currentTimeMillis()
    }

    private fun findBookSource(ns: String, book: Book): String? {
        val origin = book.origin
        if (origin.isBlank() || origin == "loc_book") return null
        val arr = ExtKt.asJsonArray(ExtKt.getStorage("data", ns, "bookSource")) ?: return null
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i) ?: continue
            if (o.getString("bookSourceUrl") == origin) return o.encode()
        }
        return null
    }
}
