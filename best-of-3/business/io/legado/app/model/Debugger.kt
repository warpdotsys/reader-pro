/** Business rewrite from reader-pro-3.2.14.jar — phase7. */

package io.legado.app.model

import io.legado.app.data.entities.Book
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.HtmlFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Debug entry key forms (from jar):
 * - absolute URL → infoDebug
 * - contains `::` → exploreDebug (after :: is explore URL)
 * - starts with `++` → tocDebug (toc url)
 * - starts with `--` → contentDebug (content url)
 * - else → searchDebug
 */
class Debugger(val logMsg: (String) -> Unit) : DebugLog {
    private val debugTimeFormat = SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault())
    private var startTime = System.currentTimeMillis()

    override fun log(sourceUrl: String?, msg: String?) = log(sourceUrl, msg, false)

    override fun log(message: String) {
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $message")
    }

    override fun log(sourceUrl: String?, msg: String?, isHtml: Boolean) {
        if (msg == null) return
        var printMsg = msg
        if (isHtml) printMsg = HtmlFormatter.formatKeepImg(msg)
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $printMsg")
    }

    suspend fun startDebug(webBook: WebBook, key: String) {
        val bookSource = webBook.getBookSource()
        val origin = bookSource.bookSourceUrl
        startTime = System.currentTimeMillis()

        when {
            key.startsWith("http://") || key.startsWith("https://") -> {
                log(origin, "⇒开始访问详情页:$key")
                val book = Book(bookUrl = key, origin = origin)
                infoDebug(webBook, book)
            }
            key.contains("::") -> {
                val url = key.substringAfter("::")
                log(origin, "⇒开始访问发现页:$url")
                exploreDebug(webBook, url)
            }
            key.startsWith("++") -> {
                val tocUrl = key.substring(2)
                log(origin, "⇒开始访问目录页:$tocUrl")
                val book = Book(bookUrl = tocUrl, tocUrl = tocUrl, origin = origin)
                tocDebug(webBook, book)
            }
            key.startsWith("--") -> {
                val contentUrl = key.substring(2)
                log(origin, "⇒开始访问正文页:$contentUrl")
                val book = Book(bookUrl = contentUrl, origin = origin)
                val chapter = io.legado.app.data.entities.BookChapter(url = contentUrl, title = "debug", bookUrl = contentUrl)
                contentDebug(webBook, book, chapter)
            }
            else -> {
                log(origin, "⇒开始搜索关键字:$key")
                searchDebug(webBook, key)
            }
        }
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
        infoDebug(webBook, Book(bookUrl = first.bookUrl, name = first.name, author = first.author, origin = origin, coverUrl = first.coverUrl))
    }

    private suspend fun exploreDebug(webBook: WebBook, url: String) {
        val origin = webBook.getBookSource().bookSourceUrl
        val list = webBook.exploreBook(url, 1)
        log(origin, "≡发现 ${list.size} 条")
        list.take(5).forEachIndexed { i, s ->
            log(origin, "  [$i] ${s.name} - ${s.author}")
        }
        val first = list.firstOrNull() ?: return
        infoDebug(webBook, Book(bookUrl = first.bookUrl, name = first.name, author = first.author, origin = origin))
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
        chapters.take(3).forEach { log(origin, "  - ${it.title}") }
        if (chapters.size > 3) log(origin, "  …")
        val first = chapters.firstOrNull() ?: return
        contentDebug(webBook, book, first, chapters.getOrNull(1)?.url)
    }

    private suspend fun contentDebug(
        webBook: WebBook,
        book: Book,
        chapter: io.legado.app.data.entities.BookChapter,
        nextUrl: String? = null
    ) {
        val origin = webBook.getBookSource().bookSourceUrl
        val content = webBook.getBookContent(book, chapter, nextUrl)
        val preview = content.take(200).replace("\n", " ")
        log(origin, "≡正文 length=${content.length} 预览: $preview")
    }
}
