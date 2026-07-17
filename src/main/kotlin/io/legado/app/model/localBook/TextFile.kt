package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.DefaultData
import io.legado.app.utils.MD5Utils
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class TextFile(private val book: Book) {
    private var charset: Charset = Charset.forName(book.charset ?: "UTF-8")

    fun getChapterList(): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()

        val head = readHead(file)
        if (book.charset.isNullOrBlank()) book.charset = detectCharset(head).name()
        charset = Charset.forName(book.charset)
        if (book.tocUrl.isBlank()) book.tocUrl = selectTocRule(String(head, charset))

        val pattern = book.tocUrl.takeIf { it.isNotBlank() }
            ?.let { Pattern.compile(it, Pattern.MULTILINE) }
        val chapters = if (pattern == null) analyzeWithoutToc(file) else analyzeWithToc(file, pattern)
        if (chapters.isEmpty()) {
            chapters += BookChapter(
                title = book.name.ifEmpty { "全文" },
                index = 0,
                bookUrl = book.bookUrl,
                start = 0,
                end = file.length(),
                byteStart = 0,
                url = "0"
            )
        }

        chapters.forEachIndexed { index, chapter ->
            chapter.index = index
            chapter.bookUrl = book.bookUrl
            chapter.byteStart = chapter.start ?: 0
            chapter.url = MD5Utils.md5Encode16("${book.originName}$index${chapter.title}")
        }
        book.totalChapterNum = chapters.size
        book.latestChapterTitle = chapters.last().title
        return chapters
    }

    fun getContent(chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        if (book.charset.isNullOrBlank()) book.charset = detectCharset(readHead(file)).name()
        charset = Charset.forName(book.charset)

        val start = (chapter.start ?: chapter.byteStart).coerceIn(0, file.length())
        val end = (chapter.end ?: file.length()).coerceIn(start, file.length())
        val bytes = file.inputStream().use { input ->
            input.skipFully(start)
            input.readNBytes((end - start).coerceAtMost(Int.MAX_VALUE.toLong()).toInt())
        }
        return String(bytes, charset)
            .substringAfter(chapter.title, missingDelimiterValue = "")
            .replaceFirst(Regex("^[\\n\\s]+"), "\u3000\u3000")
    }

    private fun analyzeWithToc(file: File, pattern: Pattern): ArrayList<BookChapter> {
        val chapters = ArrayList<BookChapter>()
        file.inputStream().use { input ->
            val buffer = ByteArray(BUFFER_SIZE)
            var bufferStart = BOM_SIZE
            var offset = 0L
            val prefixLength = input.read(buffer, 0, BOM_SIZE).coerceAtLeast(0)
            if (prefixLength == BOM_SIZE && hasUtf8Bom(buffer)) {
                bufferStart = 0
                offset = BOM_SIZE.toLong()
            } else {
                bufferStart = prefixLength
            }

            while (true) {
                val read = input.read(buffer, bufferStart, BUFFER_SIZE - bufferStart)
                if (read <= 0) {
                    if (bufferStart > 0) {
                        val block = String(buffer, 0, bufferStart, charset)
                        appendTocMatches(chapters, block, offset, pattern)
                    }
                    break
                }
                val available = bufferStart + read
                val end = if (available == BUFFER_SIZE) {
                    buffer.lastIndexOf(NEWLINE, available - 1).takeIf { it >= 0 } ?: available
                } else {
                    available
                }
                val block = String(buffer, 0, end, charset)
                appendTocMatches(chapters, block, offset, pattern)
                offset += end
                val remainder = available - end
                if (remainder > 0) System.arraycopy(buffer, end, buffer, 0, remainder)
                bufferStart = remainder
            }
        }
        return chapters
    }

    private fun appendTocMatches(
        chapters: ArrayList<BookChapter>,
        block: String,
        offset: Long,
        pattern: Pattern
    ) {
        val matcher = pattern.matcher(block)
        var seek = 0
        while (matcher.find()) {
            val contentBeforeTitle = block.substring(seek, matcher.start())
            val contentLength = contentBeforeTitle.toByteArray(charset).size.toLong()
            if (chapters.isEmpty() && seek == 0 && matcher.start() > 0) {
                if (contentBeforeTitle.isNotBlank()) {
                    chapters += chapter("前言", offset, offset + contentLength)
                }
                chapters += chapter(matcher.group(), offset + contentLength)
            } else if (chapters.isEmpty()) {
                chapters += chapter(matcher.group(), offset, offset)
            } else if (seek == 0 && matcher.start() > 0) {
                chapters.last().end = (chapters.last().end ?: offset) + contentLength
                chapters += chapter(matcher.group(), chapters.last().end ?: offset)
            } else {
                chapters.last().end = (chapters.last().start ?: offset) + contentLength
                chapters += chapter(matcher.group(), chapters.last().end ?: offset)
            }
            // Keep the previous title in the next span so chapter.end includes it.
            seek = matcher.start()
        }
        chapters.lastOrNull()?.end = offset + block.toByteArray(charset).size
    }

    private fun analyzeWithoutToc(file: File): ArrayList<BookChapter> {
        val chapters = ArrayList<BookChapter>()
        file.inputStream().use { input ->
            val buffer = ByteArray(BUFFER_SIZE)
            var bufferStart = 0
            var offset = 0L
            var block = 0
            var part = 0
            val prefixLength = input.read(buffer, 0, BOM_SIZE).coerceAtLeast(0)
            if (prefixLength == BOM_SIZE && hasUtf8Bom(buffer)) offset = BOM_SIZE.toLong()
            else bufferStart = prefixLength

            while (true) {
                val remaining = BUFFER_SIZE - bufferStart
                val read = input.read(buffer, bufferStart, remaining)
                if (read <= 0) break
                var length = bufferStart + read
                block++
                var chapterOffset = 0
                while (length - chapterOffset > MAX_LENGTH_WITHOUT_TOC) {
                    var end = chapterOffset + MAX_LENGTH_WITHOUT_TOC
                    while (end < length && buffer[end] != NEWLINE) end++
                    part++
                    val start = chapters.lastOrNull()?.end ?: offset
                    chapters += chapter("第${block}章($part)", start, start + (end - chapterOffset))
                    chapterOffset = end
                }
                val remainder = length - chapterOffset
                if (remainder > 0) System.arraycopy(buffer, chapterOffset, buffer, 0, remainder)
                offset += chapterOffset
                bufferStart = remainder
            }
            if (bufferStart > 100 || chapters.isEmpty()) {
                part++
                val start = chapters.lastOrNull()?.end ?: offset
                chapters += chapter("第${block.coerceAtLeast(1)}章($part)", start, start + bufferStart)
            } else {
                chapters.last().end = (chapters.last().end ?: offset) + bufferStart
            }
        }
        return chapters
    }

    private fun chapter(title: String, start: Long, end: Long? = null) =
        BookChapter(title = title.trim(), start = start, end = end, byteStart = start)

    private fun readHead(file: File): ByteArray = file.inputStream().use { input ->
        input.readNBytes(BUFFER_SIZE)
    }

    private fun selectTocRule(head: String): String {
        val candidates = DefaultData.enabledTxtTocRules().ifEmpty { DefaultData.txtTocRules }
        var best: String? = null
        var bestCount = 1
        for (rule in candidates.asReversed()) {
            val count = runCatching {
                Pattern.compile(rule.rule, Pattern.MULTILINE).matcher(head).let { matcher ->
                    generateSequence { if (matcher.find()) Unit else null }.count()
                }
            }.getOrDefault(0)
            if (count >= bestCount) {
                bestCount = count
                best = rule.rule
            }
        }
        return best.orEmpty()
    }

    private fun detectCharset(bytes: ByteArray): Charset = when {
        bytes.size >= 3 && hasUtf8Bom(bytes) -> StandardCharsets.UTF_8
        bytes.size >= 2 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte() -> Charset.forName("UTF-16LE")
        bytes.size >= 2 && bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte() -> Charset.forName("UTF-16BE")
        isUtf8(bytes) -> StandardCharsets.UTF_8
        else -> Charset.forName("GB18030")
    }

    private fun isUtf8(bytes: ByteArray): Boolean = runCatching {
        StandardCharsets.UTF_8.newDecoder()
            .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT)
            .decode(java.nio.ByteBuffer.wrap(bytes))
        true
    }.getOrDefault(false)

    private fun hasUtf8Bom(bytes: ByteArray) =
        bytes.size >= BOM_SIZE && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()

    private fun java.io.InputStream.skipFully(count: Long) {
        var remaining = count
        while (remaining > 0) {
            val skipped = skip(remaining)
            if (skipped > 0) remaining -= skipped else if (read() < 0) break else remaining--
        }
    }

    private fun ByteArray.lastIndexOf(value: Byte, fromIndex: Int): Int {
        for (index in fromIndex downTo 0) if (this[index] == value) return index
        return -1
    }

    private companion object {
        const val BUFFER_SIZE = 512_000
        const val MAX_LENGTH_WITHOUT_TOC = 10_240
        const val BOM_SIZE = 3
        const val NEWLINE: Byte = 10
    }
}
