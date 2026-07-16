package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.EpubBook;

public interface BookProcessor {
   BookProcessor IDENTITY_BOOKPROCESSOR = book -> book;

   EpubBook processBook(EpubBook book);
}
