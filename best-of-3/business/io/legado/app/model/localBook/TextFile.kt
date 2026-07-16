package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.DefaultData
import io.legado.app.utils.MD5Utils
import java.nio.charset.Charset
import java.util.regex.Pattern

class TextFile(private val book: Book) {
    private var charset: Charset = Charset.forName(book.charset ?: "UTF-8")

    fun getChapterList(): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        if (book.charset.isNullOrBlank()) book.charset = "UTF-8"
        charset = Charset.forName(book.charset)
        if (book.tocUrl.isBlank()) {
            val head = file.inputStream().use { ins ->
                val buf = ByteArray(minOf(512_000, file.length().toInt().coerceAtLeast(1)))
                ins.read(buf); String(buf, charset)
            }
            book.tocUrl = selectTocRule(head)
        }
        val pattern = Pattern.compile(book.tocUrl, Pattern.MULTILINE)
        val text = file.readText(charset)
        val m = pattern.matcher(text)
        val list = ArrayList<BookChapter>()
        var last = 0
        var idx = 0
        while (m.find()) {
            if (list.isNotEmpty()) {
                list.last().end = m.start().toLong()
            } else if (m.start() > 0) {
                list += BookChapter(title = "前言", index = idx++, bookUrl = book.bookUrl, byteStart = 0, start = 0)
                list.last().end = m.start().toLong()
                idx = list.size
            }
            list += BookChapter(
                title = m.group()?.trim() ?: "章节",
                index = idx++,
                bookUrl = book.bookUrl,
                byteStart = m.start().toLong(),
                start = m.start().toLong(),
                url = MD5Utils.md5Encode16("${book.name}$idx")
            )
            last = m.end()
        }
        if (list.isEmpty()) {
            list += BookChapter(title = book.name.ifEmpty { "全文" }, index = 0, bookUrl = book.bookUrl, url = "0")
        } else {
            list.last().end = text.length.toLong()
        }
        book.totalChapterNum = list.size
        book.latestChapterTitle = list.last().title
        return list
    }

    fun getContent(chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val text = file.readText(charset)
        val start = (chapter.start ?: chapter.byteStart).toInt().coerceIn(0, text.length)
        val end = (chapter.end ?: text.length.toLong()).toInt().coerceIn(start, text.length)
        return text.substring(start, end).trim()
    }

    private fun selectTocRule(head: String): String {
        val candidates = DefaultData.enabledTxtTocRules().ifEmpty { DefaultData.txtTocRules }
        var best = candidates.first().rule
        var bestCount = 0
        for (r in candidates) {
            val c = try {
                Pattern.compile(r.rule, Pattern.MULTILINE).matcher(head).let { m ->
                    var n = 0; while (m.find()) n++; n
                }
            } catch (_: Exception) { 0 }
            if (c > bestCount) { bestCount = c; best = r.rule }
        }
        // prefer at least 2 hits; otherwise still return best
        return best
    }
}

