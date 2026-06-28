package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.EpubBook;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/epub/BookProcessor.class */
public interface BookProcessor {
    public static final BookProcessor IDENTITY_BOOKPROCESSOR = book -> {
        return book;
    };

    EpubBook processBook(EpubBook book);
}
