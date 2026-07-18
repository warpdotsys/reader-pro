package me.ag2s.epublib.epub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.ag2s.epublib.domain.EpubBook;

public class BookProcessorPipeline implements BookProcessor {
   private static final String TAG = BookProcessorPipeline.class.getName();
   private List bookProcessors;

   public BookProcessorPipeline() {
      this((List)null);
   }

   public BookProcessorPipeline(List bookProcessingPipeline) {
      this.bookProcessors = bookProcessingPipeline;
   }

   public EpubBook processBook(EpubBook book) {
      if (this.bookProcessors == null) {
         return book;
      } else {
         for(BookProcessor bookProcessor : this.bookProcessors) {
            try {
               book = bookProcessor.processBook(book);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }

         return book;
      }
   }

   public void addBookProcessor(BookProcessor bookProcessor) {
      if (this.bookProcessors == null) {
         this.bookProcessors = new ArrayList();
      }

      this.bookProcessors.add(bookProcessor);
   }

   public void addBookProcessors(Collection bookProcessors) {
      if (this.bookProcessors == null) {
         this.bookProcessors = new ArrayList();
      }

      this.bookProcessors.addAll(bookProcessors);
   }

   public List getBookProcessors() {
      return this.bookProcessors;
   }

   public void setBookProcessingPipeline(List bookProcessingPipeline) {
      this.bookProcessors = bookProcessingPipeline;
   }
}
