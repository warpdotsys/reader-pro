/** Business rewrite from reader-pro-3.2.14.jar — phase7. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.ContentProcessor
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.FileUtils
import io.vertx.ext.web.RoutingContext
import me.ag2s.epublib.domain.Author
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Metadata
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.epub.EpubWriter
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

/**
 * exportBook: type=txt|epub → file under storage/data/{user}/export/
 */
object BookExport {

    suspend fun exportBook(ctrl: BookController, context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!ctrl.checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = ctrl.getUserNameSpace(context)
        val type = context.queryParam("type").firstOrNull()
            ?: context.bodyAsJson?.getString("type")
            ?: "txt"
        val bookUrl = context.queryParam("bookUrl").firstOrNull()
            ?: context.bodyAsJson?.getString("bookUrl")
            ?: return rd.setErrorMsg("bookUrl 不能为空")
        val book = ctrl.getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍不存在")
        val source = ctrl.getBookSourceString(book, ns) ?: ""
        val exportDir = File(ExtKt.getWorkDir("storage", "data", ns, "export")).apply { mkdirs() }
        val file = when (type.lowercase()) {
            "epub" -> exportToEpub(exportDir, book, source, ns, ctrl)
            else -> exportToTxt(exportDir, book, source, ns, ctrl)
        }
        return rd.setData(
            mapOf(
                "path" to file.absolutePath,
                "name" to file.name,
                "size" to file.length(),
                "type" to type
            )
        )
    }

    suspend fun exportToTxt(
        exportDir: File,
        book: Book,
        bookSource: String,
        userNameSpace: String,
        ctrl: BookController
    ): File {
        val safeName = book.name.ifEmpty { "book" }.replace(Regex("""[\\/:*?"<>|]"""), "_")
        val author = book.author.ifEmpty { "未知" }
        val out = File(exportDir, "《${safeName}》作者：${author}.txt")
        if (out.exists()) out.delete()
        out.parentFile?.mkdirs()
        val charset = Charset.forName("UTF-8")
        getAllContents(book, bookSource, userNameSpace, ctrl) { text, _ ->
            out.appendText(text, charset)
        }
        return out
    }

    suspend fun exportToEpub(
        exportDir: File,
        book: Book,
        bookSource: String?,
        userNameSpace: String,
        ctrl: BookController
    ): File {
        val safeName = book.name.ifEmpty { "book" }.replace(Regex("""[\\/:*?"<>|]"""), "_")
        val out = File(exportDir, "${safeName}.epub")
        val epub = EpubBook()
        val md: Metadata = epub.metadata
        md.addTitle(book.name)
        if (book.author.isNotEmpty()) md.addAuthor(Author(book.author))
        val chapters = ctrl.getLocalChapterList(book, bookSource, false, userNameSpace, false, null)
        chapters.forEachIndexed { i, ch ->
            var content = fetchChapter(book, bookSource, userNameSpace, ch, chapters.getOrNull(i + 1)?.url, ctrl)
            content = ContentProcessor.applyContent(userNameSpace, book, content)
            val html = """<!DOCTYPE html><html><head><meta charset="utf-8"/><title>${esc(ch.title)}</title></head>
<body><h1>${esc(ch.title)}</h1>${toHtmlParagraphs(content)}</body></html>"""
            val href = "chapter_$i.xhtml"
            epub.addSection(ch.title, Resource(html.toByteArray(Charsets.UTF_8), href))
        }
        FileOutputStream(out).use { EpubWriter().write(epub, it) }
        return out
    }

    private suspend fun getAllContents(
        book: Book,
        bookSource: String,
        userNameSpace: String,
        ctrl: BookController,
        append: (String, Any?) -> Unit
    ) {
        val chapters = ctrl.getLocalChapterList(book, bookSource, false, userNameSpace, false, null)
        append("《${book.name}》\n作者：${book.author}\n\n", null)
        chapters.forEachIndexed { i, ch ->
            val title = ContentProcessor.applyTitle(userNameSpace, book, ch.title)
            append("\n\n$title\n\n", null)
            var content = fetchChapter(book, bookSource, userNameSpace, ch, chapters.getOrNull(i + 1)?.url, ctrl)
            content = ContentProcessor.applyContent(userNameSpace, book, content)
            // strip tags lightly for txt
            content = content.replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
                .replace(Regex("<[^>]+>"), "")
            append(content, null)
        }
    }

    private suspend fun fetchChapter(
        book: Book,
        bookSource: String?,
        userNameSpace: String,
        ch: BookChapter,
        nextUrl: String?,
        ctrl: BookController
    ): String {
        return if (book.isLocalBook) {
            LocalBook.getContent(book, ch) ?: ""
        } else {
            val src = bookSource ?: return ""
            WebBook(src, false, null, userNameSpace).getBookContent(book, ch, nextUrl)
        }
    }

    private fun toHtmlParagraphs(text: String): String {
        if (text.contains("<p", ignoreCase = true) || text.contains("<div", ignoreCase = true)) return text
        return text.split(Regex("\n+")).joinToString("") { "<p>${esc(it)}</p>" }
    }

    private fun esc(s: String) =
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}

