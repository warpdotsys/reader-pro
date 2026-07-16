/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.apache.pdfbox.pdmodel.PDDocument

/** PDF: one chapter per page (pdfbox). */
object PdfFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val f = book.localFile()
        if (!f.isFile) return list
        try {
            PDDocument.load(f).use { doc ->
                val n = doc.numberOfPages
                for (i in 0 until n) {
                    list += BookChapter(
                        url = "page:${i + 1}",
                        title = "第${i + 1}页",
                        index = i,
                        bookUrl = book.bookUrl
                    )
                }
                book.totalChapterNum = n
                book.latestChapterTitle = list.lastOrNull()?.title
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val page = chapter.url.removePrefix("page:").toIntOrNull() ?: (chapter.index + 1)
        // Full text extraction optional; jar may render images via convertPdfToImage
        return try {
            PDDocument.load(book.localFile()).use { doc ->
                val stripper = org.apache.pdfbox.text.PDFTextStripper()
                stripper.startPage = page
                stripper.endPage = page
                stripper.getText(doc)
            }
        } catch (_: Exception) {
            """<p>PDF 第${page}页</p>"""
        }
    }
}
