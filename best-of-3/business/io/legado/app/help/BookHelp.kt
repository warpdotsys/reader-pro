/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.help

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object BookHelp {
    private val downloadImages = ConcurrentHashMap.newKeySet<String>()

    fun getBookCacheDir(book: Book): File =
        File(book.rootDir ?: ".", "cache", book.name + "_" + book.author)

    fun getImage(book: Book, src: String): File {
        val name = MD5Utils.md5Encode16(src) + "." + getImageSuffix(src)
        return File(getBookCacheDir(book), "images/$name")
    }

    fun getImageSuffix(src: String): String =
        src.substringAfterLast('.').substringBefore('?').takeIf { it.length in 1..5 } ?: "jpg"

    suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
        while (src in downloadImages) delay(100)
        if (getImage(book, src).exists()) return
        downloadImages.add(src)
        try {
            val bytes = AnalyzeUrl(mUrl = src, source = bookSource).getByteArrayAwait()
            val f = FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", MD5Utils.md5Encode16(src) + "." + getImageSuffix(src))
            f.writeBytes(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            downloadImages.remove(src)
        }
    }

    suspend fun saveImages(
        scope: CoroutineScope,
        bookSource: BookSource,
        book: Book,
        chapter: BookChapter,
        content: String
    ) {
        // extract <img src> and saveImage each — see decompiled BookHelp
        val regex = Regex("""<img[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
        regex.findAll(content).forEach { m ->
            saveImage(bookSource, book, m.groupValues[1])
        }
    }
}
