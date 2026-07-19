package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.io.InputStream
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode

class PdfFile(var book: Book) {

    var info: MutableMap<String, Any>? = null
    var cover: InputStream? = null

    private fun parseBookInfo(): Pair<MutableMap<String, Any>?, InputStream?> {
        return Pair(info, cover)
    }

    @Suppress("UNCHECKED_CAST")
    private fun upBookInfo() {
        val result = parseBookInfo()
        if (result.first != null) {
            val bookInfo = result.first as Map<String, Any>
            val info = bookInfo["ComicInfo"] as Map<String, Any>?
            book.name = (info?.get("Title") ?: book.name) as String
            book.author = (info?.get("Writer") ?: book.author) as String
        }
        updateCover()
    }

    private fun updateCover() = Unit

    @Suppress("UNUSED_PARAMETER")
    private fun getContent(chapter: BookChapter): String? = ""

    private fun getChapterList(): ArrayList<BookChapter> {
        if (book.tocUrl.isEmpty()) {
            book.tocUrl = "page"
        }
        return if (book.tocUrl == "page") getChapterListByPage() else getChapterListByOutline()
    }

    private fun getChapterListByPage(): ArrayList<BookChapter> {
        val chapterList = ArrayList<BookChapter>()
        val document = PDDocument.load(book.getLocalFile())
        for (pageIndex in 0 until document.numberOfPages) {
            val name = "output-$pageIndex.png"
            val chapter = BookChapter()
            chapter.title = name
            chapter.index = pageIndex
            chapter.bookUrl = book.bookUrl
            chapter.url = name
            chapter.start = pageIndex.toLong()
            chapter.end = pageIndex.toLong()
            chapterList.add(chapter)
        }
        book.latestChapterTitle = chapterList.lastOrNull()?.title
        book.totalChapterNum = chapterList.size
        document.close()
        return chapterList
    }

    private fun getChapterListByOutline(): ArrayList<BookChapter> {
        val chapterList = ArrayList<BookChapter>()
        val document = PDDocument.load(book.getLocalFile())
        val outline = document.documentCatalog.documentOutline ?: return chapterList
        processOutline(document, chapterList, outline)
        if (chapterList.isNotEmpty()) {
            chapterList.last().end = document.numberOfPages.toLong()
        }
        document.close()
        return chapterList
    }

    private fun processOutline(
        document: PDDocument,
        chapterList: ArrayList<BookChapter>,
        outline: PDOutlineNode
    ) {
        var current = outline.firstChild
        while (current != null) {
            val page = current.findDestinationPage(document)
            val pageIndex = document.documentCatalog.pages.indexOf(page)
            if (chapterList.isEmpty() && pageIndex >= 1) {
                val chapter = BookChapter()
                chapter.title = "\u9996\u7ae0"
                chapter.index = 0
                chapter.bookUrl = book.bookUrl
                chapter.url = "chapter-0"
                chapter.start = 0L
                chapter.end = pageIndex.toLong()
                chapterList.add(chapter)
            }
            if (chapterList.isNotEmpty()) {
                if (chapterList.last().start == pageIndex.toLong()) {
                    current = current.nextSibling
                    continue
                }
                val chapter = BookChapter()
                chapter.title = current.title
                chapter.index = chapterList.size
                chapter.bookUrl = book.bookUrl
                chapter.url = "chapter-${chapterList.size}"
                chapter.start = pageIndex.toLong()
                chapterList.last().end = pageIndex.toLong() - 1L
                chapterList.add(chapter)
            }
            if (current.hasChildren()) {
                processOutline(document, chapterList, current)
            }
            current = current.nextSibling
        }
    }

    companion object {
        private var cFile: PdfFile? = null

        @Synchronized
        private fun getPdfFile(book: Book): PdfFile {
            cFile?.let {
                if (it.book.bookUrl == book.bookUrl) {
                    it.book = book
                    return it
                }
            }
            return PdfFile(book).also { cFile = it }
        }

        @Synchronized
        fun getChapterList(book: Book): ArrayList<BookChapter> {
            return getPdfFile(book).getChapterList()
        }

        @Synchronized
        fun getContent(book: Book, chapter: BookChapter): String? {
            return getPdfFile(book).getContent(chapter)
        }

        @Synchronized
        fun upBookInfo(book: Book, onlyCover: Boolean = false) {
            if (onlyCover) {
                getPdfFile(book).updateCover()
            } else {
                getPdfFile(book).upBookInfo()
            }
        }
    }
}
