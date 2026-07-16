/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.DefaultData
import io.legado.app.utils.MD5Utils
import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern

class TextFile(private val book: Book) {
    private val bufferSize = 512_000
    private var charset: Charset = Charset.forName(book.charset ?: "UTF-8")

    fun getChapterList(): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()

        if (book.charset.isNullOrBlank() || book.tocUrl.isBlank()) {
            val len = minOf(bufferSize.toLong(), file.length()).toInt().coerceAtLeast(1)
            val buf = ByteArray(len)
            file.inputStream().use { it.read(buf) }
            if (book.charset.isNullOrBlank()) {
                book.charset = detectCharset(buf) ?: "UTF-8"
            }
            charset = Charset.forName(book.charset)
            if (book.tocUrl.isBlank()) {
                val head = String(buf, charset)
                book.tocUrl = selectTocRule(head)
            }
        } else {
            charset = Charset.forName(book.charset)
        }

        val pattern = Pattern.compile(book.tocUrl, Pattern.MULTILINE)
        val chapters = analyze(file, pattern)
        chapters.forEachIndexed { i, ch ->
            ch.index = i
            ch.bookUrl = book.bookUrl
            ch.url = MD5Utils.md5Encode16("${book.name}$i${ch.title}")
        }
        if (chapters.isNotEmpty()) {
            book.latestChapterTitle = chapters.last().title
            book.totalChapterNum = chapters.size
        }
        return chapters
    }

    fun getContent(chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        // re-scan chapter offsets
        val chapters = getChapterList()
        val idx = chapters.indexOfFirst { it.index == chapter.index || it.title == chapter.title }
            .takeIf { it >= 0 } ?: return null
        val start = chapters[idx].byteStart
        val end = chapters.getOrNull(idx + 1)?.byteStart ?: file.length()
        if (end < start) return null
        val bytes = ByteArray((end - start).toInt())
        file.inputStream().use { ins ->
            ins.skip(start)
            var off = 0
            while (off < bytes.size) {
                val n = ins.read(bytes, off, bytes.size - off)
                if (n <= 0) break
                off += n
            }
        }
        return String(bytes, charset).trim()
    }

    private fun analyze(file: File, pattern: Pattern): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        // char-index based for simplicity; store as byteStart approx via string offset mapping
        val text = file.readText(charset)
        val m = pattern.matcher(text)
        var first = true
        while (m.find()) {
            val start = m.start()
            if (first && start > 0) {
                list += BookChapter(title = "前言", index = 0).also { it.byteStart = 0L }
            }
            first = false
            val title = m.group()?.trim()?.take(80) ?: "章节"
            // approximate byte offset: for UTF-8 not exact but ok for many files; better map via charset encoder
            val byteStart = text.substring(0, start).toByteArray(charset).size.toLong()
            list += BookChapter(title = title).also { it.byteStart = byteStart }
        }
        if (list.isEmpty()) {
            list += BookChapter(title = book.name.ifEmpty { "正文" }).also { it.byteStart = 0L }
        }
        return list
    }

    /** Pick best enabled rule from DefaultData by match count on sample. */
    private fun selectTocRule(sample: String): String {
        val rules = DefaultData.txtTocRules.filter { it.enable && it.rule.isNotBlank() }
        var best = DEFAULT_TOC
        var bestCount = 0
        for (r in rules) {
            try {
                val n = Pattern.compile(r.rule, Pattern.MULTILINE).matcher(sample)
                    .results().count().toInt()
                if (n in 3..500 && n > bestCount) {
                    bestCount = n
                    best = r.rule
                }
            } catch (_: Exception) {
            }
        }
        return best
    }

    private fun detectCharset(buf: ByteArray): String? {
        if (buf.size >= 3 && buf[0] == 0xEF.toByte() && buf[1] == 0xBB.toByte() && buf[2] == 0xBF.toByte()) {
            return "UTF-8"
        }
        return "UTF-8"
    }

    companion object {
        const val DEFAULT_TOC =
            "^\\s*第[0-9零一二三四五六七八九十百千万]+[章节回卷].{0,30}$"
    }
}

var BookChapter.byteStart: Long
    get() = tag?.toLongOrNull() ?: 0L
    set(v) { tag = v.toString() }
