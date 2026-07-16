/** Business rewrite from reader-pro-3.2.14.jar — phase5. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.localBook.LocalMedia
import io.legado.app.model.localBook.localFile
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.util.UUID

/**
 * Local book import preview / open + binary media streaming endpoints helpers.
 */
object LocalBookApi {

    fun importPreview(context: RoutingContext, ctrl: BookController): ReturnData {
        val rd = ReturnData()
        val ns = ctrl.getUserNameSpace(context)
        val uploads = context.fileUploads()
        if (uploads.isEmpty()) return rd.setErrorMsg("请上传文件")
        val books = ArrayList<Map<String, Any?>>()
        val dir = File(ExtKt.getWorkDir("storage", "data", ns, "local")).apply { mkdirs() }
        for (up in uploads) {
            val name = up.fileName()
            val dest = File(dir, "${UUID.randomUUID()}_$name")
            File(up.uploadedFileName()).copyTo(dest, overwrite = true)
            val (bookName, author) = LocalBook.analyzeNameAuthor(name)
            val book = Book(
                bookUrl = dest.absolutePath,
                origin = "loc_book",
                originName = name,
                name = bookName,
                author = author,
                rootDir = ExtKt.getWorkDir(),
                userNameSpace = ns,
                canUpdate = false,
                isInShelf = false
            )
            try {
                val chapters = LocalBook.getChapterList(book)
                books += mapOf(
                    "book" to book,
                    "chapterCount" to chapters.size,
                    "latest" to book.latestChapterTitle
                )
            } catch (e: Exception) {
                books += mapOf("book" to book, "error" to e.message)
            }
        }
        return rd.setData(books)
    }

    fun streamChapterImage(context: RoutingContext, ctrl: BookController) {
        val ns = ctrl.getUserNameSpace(context)
        val bookUrl = context.queryParam("bookUrl").firstOrNull() ?: run {
            context.response().setStatusCode(400).end("bookUrl required"); return
        }
        val index = context.queryParam("index").firstOrNull()?.toIntOrNull() ?: 0
        val book = ctrl.getShelfBookByURL(bookUrl, ns)
            ?: Book(bookUrl = bookUrl, origin = "loc_book", rootDir = ExtKt.getWorkDir())
        val chapters = try {
            LocalBook.getChapterList(book)
        } catch (_: Exception) {
            emptyList()
        }
        val ch = chapters.getOrNull(index) ?: BookChapter(index = index, url = context.queryParam("url").firstOrNull() ?: "")
        val bytes = LocalMedia.getChapterImage(book, ch)
        if (bytes == null) {
            context.response().setStatusCode(404).end()
            return
        }
        val ct = LocalMedia.guessContentType(ch.url)
        context.response()
            .putHeader("Content-Type", ct)
            .putHeader("Cache-Control", "public, max-age=86400")
            .end(io.vertx.core.buffer.Buffer.buffer(bytes))
    }

    fun streamCover(context: RoutingContext, ctrl: BookController) {
        val bookUrl = context.queryParam("bookUrl").firstOrNull()
            ?: context.queryParam("path").firstOrNull()
        if (bookUrl.isNullOrEmpty()) {
            context.response().setStatusCode(400).end(); return
        }
        val ns = ctrl.getUserNameSpace(context)
        val book = ctrl.getShelfBookByURL(bookUrl, ns)
            ?: Book(bookUrl = bookUrl, origin = "loc_book", rootDir = ExtKt.getWorkDir())
        val bytes = LocalMedia.getCover(book)
        if (bytes == null) {
            context.response().setStatusCode(404).end(); return
        }
        context.response()
            .putHeader("Content-Type", "image/jpeg")
            .end(io.vertx.core.buffer.Buffer.buffer(bytes))
    }
}

fun BookController.importBookPreview(context: RoutingContext): ReturnData {
    if (!checkAuth(context)) return ReturnData().setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    return LocalBookApi.importPreview(context, this)
}

fun BookController.refreshLocalBook(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val url = context.bodyAsJson?.getString("bookUrl")
        ?: context.queryParam("bookUrl").firstOrNull()
        ?: return rd.setErrorMsg("bookUrl 不能为空")
    val book = getShelfBookByURL(url, ns) ?: return rd.setErrorMsg("书籍不存在")
    return try {
        val chapters = LocalBook.getChapterList(book)
        // refresh shelf chapter count
        // editShelfBook suspend — call from coroutine context in real app
        rd.setData(mapOf("chapters" to chapters.size, "title" to book.latestChapterTitle))
    } catch (e: Exception) {
        rd.setErrorMsg(e.message ?: "刷新失败")
    }
}
