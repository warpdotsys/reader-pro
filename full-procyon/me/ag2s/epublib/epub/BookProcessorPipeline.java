// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import me.ag2s.epublib.domain.EpubBook;
import java.util.List;

public class BookProcessorPipeline implements BookProcessor
{
    private static final String TAG;
    private List<BookProcessor> bookProcessors;
    
    public BookProcessorPipeline() {
        this(null);
    }
    
    public BookProcessorPipeline(final List<BookProcessor> bookProcessingPipeline) {
        this.bookProcessors = bookProcessingPipeline;
    }
    
    @Override
    public EpubBook processBook(EpubBook book) {
        if (this.bookProcessors == null) {
            return book;
        }
        for (final BookProcessor bookProcessor : this.bookProcessors) {
            try {
                book = bookProcessor.processBook(book);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return book;
    }
    
    public void addBookProcessor(final BookProcessor bookProcessor) {
        if (this.bookProcessors == null) {
            this.bookProcessors = new ArrayList<BookProcessor>();
        }
        this.bookProcessors.add(bookProcessor);
    }
    
    public void addBookProcessors(final Collection<BookProcessor> bookProcessors) {
        if (this.bookProcessors == null) {
            this.bookProcessors = new ArrayList<BookProcessor>();
        }
        this.bookProcessors.addAll(bookProcessors);
    }
    
    public List<BookProcessor> getBookProcessors() {
        return this.bookProcessors;
    }
    
    public void setBookProcessingPipeline(final List<BookProcessor> bookProcessingPipeline) {
        this.bookProcessors = bookProcessingPipeline;
    }
    
    static {
        TAG = BookProcessorPipeline.class.getName();
    }
}
