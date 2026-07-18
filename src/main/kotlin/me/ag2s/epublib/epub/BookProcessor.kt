package me.ag2s.epublib.epub
import me.ag2s.epublib.domain.EpubBook
fun interface BookProcessor { fun processBook(book: EpubBook): EpubBook; companion object { @JvmField val IDENTITY_BOOKPROCESSOR = BookProcessor { it } } }
