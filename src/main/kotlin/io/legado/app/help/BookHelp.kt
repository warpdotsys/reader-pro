package io.legado.app.help

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import kotlinx.coroutines.CoroutineScope
import java.io.File

object BookHelp {
    fun getBookCacheDir(book: Book): File =
        File(File(book.rootDir ?: "."), "cache/${book.name}_${book.author}")

    fun getImage(book: Book, src: String): File {
        val name = MD5Utils.md5Encode16(src) + ".jpg"
        return File(getBookCacheDir(book), "images/$name")
    }

    suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
        if (getImage(book, src).exists()) return
        try {
            val bytes = AnalyzeUrl(mUrl = src, source = bookSource).getByteArrayAwait()
            val f = FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", MD5Utils.md5Encode16(src) + ".jpg")
            f.writeBytes(bytes)
        } catch (_: Exception) {
        }
    }

    suspend fun saveImages(
        scope: CoroutineScope,
        bookSource: BookSource,
        book: Book,
        chapter: BookChapter,
        content: String
    ) {
        val regex = Regex("""<img[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
        regex.findAll(content).forEach { m -> saveImage(bookSource, book, m.groupValues[1]) }
    }
}
