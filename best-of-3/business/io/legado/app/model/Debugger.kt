package io.legado.app.model

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.HtmlFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Book-source debug entry (legado key forms):
 * - `http(s)://…` → info → toc → content
 * - contains `::` → explore (url after ::)
 * - starts with `++` → toc only
 * - starts with `--` → content only
 * - else → search → first hit detail chain
 */
class Debugger(private val logMsg: (String) -> Unit) : DebugLog {
    private val debugTimeFormat = SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault())
    private var startTime = System.currentTimeMillis()

    override fun log(source: String?, msg: String?) {
        if (msg == null) return
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $msg")
    }

    fun logHtml(source: String?, msg: String?) {
        if (msg == null) return
        log(source, HtmlFormatter.formatKeepImg(msg).take(500))
    }

    suspend fun startDebug(webBook: WebBook, key: String) {
        val origin = webBook.getBookSource().bookSourceUrl
        startTime = System.currentTimeMillis()
        log(origin, "◇开始调试 key=$key")
        when {
            key.startsWith("http://", true) || key.startsWith("https://", true) -> {
                log(origin, "⇒详情页:$key")
                infoDebug(webBook, Book(bookUrl = key, origin = origin))
            }
            key.contains("::") -> {
                val url = key.substringAfter("::")
                log(origin, "⇒发现页:$url")
                exploreDebug(webBook, url)
            }
            key.startsWith("++") -> {
                val tocUrl = key.removePrefix("++")
                log(origin, "⇒目录页:$tocUrl")
                tocDebug(webBook, Book(bookUrl = tocUrl, tocUrl = tocUrl, origin = origin))
            }
            key.startsWith("--") -> {
                val contentUrl = key.removePrefix("--")
                log(origin, "⇒正文页:$contentUrl")
                val book = Book(bookUrl = contentUrl, origin = origin)
                val chapter = BookChapter(url = contentUrl, title = "debug", bookUrl = contentUrl)
                contentDebug(webBook, book, chapter)
            }
            else -> {
                log(origin, "⇒搜索关键字:$key")
                searchDebug(webBook, key)
            }
        }
        log(origin, "◇结束")
    }

    private suspend fun searchDebug(webBook: WebBook, key: String) {
        val origin = webBook.getBookSource().bookSourceUrl
        val list = webBook.searchBook(key, 1)
        log(origin, "≡搜索到 ${list.size} 条")
        list.take(5).forEachIndexed { i, s ->
            log(origin, "  [$i] ${s.name} - ${s.author} | ${s.bookUrl}")
        }
        val first = list.firstOrNull() ?: run {
            log(origin, "※未搜索到结果")
            return
        }
        log(origin, "⇒取第一条进入详情")
        infoDebug(
            webBook,
            Book(
                bookUrl = first.bookUrl,
                name = first.name,
                author = first.author,
                origin = origin,
                coverUrl = first.coverUrl
            )
        )
    }

    private suspend fun exploreDebug(webBook: WebBook, url: String) {
        val origin = webBook.getBookSource().bookSourceUrl
        val list = webBook.exploreBook(url, 1)
        log(origin, "≡发现 ${list.size} 条")
        list.take(5).forEachIndexed { i, s ->
            log(origin, "  [$i] ${s.name} - ${s.author}")
        }
        val first = list.firstOrNull() ?: return
        infoDebug(
            webBook,
            Book(bookUrl = first.bookUrl, name = first.name, author = first.author, origin = origin)
        )
    }

    private suspend fun infoDebug(webBook: WebBook, book: Book) {
        val origin = webBook.getBookSource().bookSourceUrl
        val info = webBook.getBookInfo(book.bookUrl)
        log(origin, "≡详情《${info.name}》 author=${info.author} toc=${info.tocUrl}")
        tocDebug(webBook, info)
    }

    private suspend fun tocDebug(webBook: WebBook, book: Book) {
        val origin = webBook.getBookSource().bookSourceUrl
        val chapters = webBook.getChapterList(book)
        log(origin, "≡目录 ${chapters.size} 章")
        chapters.take(5).forEach { log(origin, "  - ${it.title}") }
        if (chapters.size > 5) log(origin, "  …")
        val first = chapters.firstOrNull() ?: return
        contentDebug(webBook, book, first, chapters.getOrNull(1)?.url)
    }

    private suspend fun contentDebug(
        webBook: WebBook,
        book: Book,
        chapter: BookChapter,
        nextUrl: String? = null
    ) {
        val origin = webBook.getBookSource().bookSourceUrl
        val content = webBook.getBookContent(book, chapter, nextUrl)
        val preview = content.take(200).replace("\n", " ")
        log(origin, "≡正文 length=${content.length} 预览: $preview")
    }
}
