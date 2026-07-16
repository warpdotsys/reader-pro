// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import me.ag2s.epublib.domain.EpubBook;

public interface BookProcessor
{
    public static final BookProcessor IDENTITY_BOOKPROCESSOR = book -> book;
    
    EpubBook processBook(final EpubBook book);
}
