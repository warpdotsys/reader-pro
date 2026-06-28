package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.EpubBook;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/BookProcessor.class */
public interface BookProcessor {
    public static final BookProcessor IDENTITY_BOOKPROCESSOR = book -> {
        return book;
    };

    EpubBook processBook(EpubBook book);
}
