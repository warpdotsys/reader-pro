package io.legado.app.model.localBook

import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.MD5Utils
import me.ag2s.umdlib.umd.UmdReader
import java.io.File
import java.io.FileInputStream

/**
 * UMD local book via me.ag2s.umdlib (ported).
 */
object UmdFile {
    private val cache = java.util.concurrent.ConcurrentHashMap<String, me.ag2s.umdlib.domain.UmdBook>()

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val umd = load(book) ?: return arrayListOf(
            BookChapter(url = "0", title = book.name.ifEmpty { "UMD(无法解析)" }, bookUrl = book.bookUrl, index = 0)
        )
        val list = ArrayList<BookChapter>()
        val titles = umd.chapters.getTitles()
        for (i in titles.indices) {
            list += BookChapter(
                url = i.toString(),
                title = umd.chapters.getTitle(i).ifBlank { "第${i + 1}章" },
                bookUrl = book.bookUrl,
                index = i
            )
        }
        if (list.isEmpty()) {
            list += BookChapter(url = "0", title = umd.header.title.ifEmpty { book.name }, bookUrl = book.bookUrl, index = 0)
        }
        // fill meta
        if (book.name.isBlank()) book.name = umd.header.title
        if (book.author.isBlank()) book.author = umd.header.author
        if (book.kind.isNullOrBlank()) book.kind = umd.header.bookType
        book.totalChapterNum = list.size
        book.latestChapterTitle = list.lastOrNull()?.title
        saveCover(book, umd)
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val umd = load(book) ?: return "【UMD 解析失败】"
        return try {
            umd.chapters.getContentString(chapter.index)
        } catch (_: Exception) {
            "【UMD 章节读取失败 index=${chapter.index}】"
        }
    }

    private fun load(book: Book): me.ag2s.umdlib.domain.UmdBook? {
        val key = book.bookUrl
        cache[key]?.let { return it }
        val file = book.localFile()
        if (!file.isFile) return null
        return try {
            FileInputStream(file).use { ins ->
                UmdReader().read(ins).also { cache[key] = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveCover(book: Book, umd: me.ag2s.umdlib.domain.UmdBook) {
        val data = umd.cover.coverData ?: return
        try {
            val ns = book.namespace ?: book.getUserNameSpace()
            val rel = "assets/$ns/covers/${MD5Utils.md5Encode16(book.bookUrl)}.jpg"
            val abs = File(ExtKt.getWorkDir("storage", rel))
            abs.parentFile?.mkdirs()
            if (!abs.exists()) abs.writeBytes(data)
            book.coverUrl = "/$rel".replace('\\', '/')
        } catch (_: Exception) {
        }
    }
}
