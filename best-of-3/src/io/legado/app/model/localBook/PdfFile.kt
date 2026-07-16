package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.io.InputStream
import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics
import okhttp3.internal.Util
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode

public class PdfFile(book: Book) {
   public final var book: Book
      internal set

   public final var cover: InputStream?
      internal set

   public final var info: MutableMap<String, Any>?
      internal set

   init {
      this.book = book;
   }

   private fun parseBookInfo(): Pair<MutableMap<String, Any>?, InputStream?> {
      return new Pair<>(this.info, this.cover);
   }

   private fun upBookInfo() {
      val result: Pair = this.parseBookInfo();
      if (result.getFirst() != null) {
         var info: java.util.Map = (java.util.Map)result.getFirst();
         if (info == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<kotlin.String, kotlin.Any>");
         }

         var var4: java.util.Map = info.get("ComicInfo") as java.util.Map;
         info = if (var4 == null) null else var4;
         var4 = (java.util.Map)(if ((if (var4 == null) null else var4) == null) null else (if (var4 == null) null else var4).get("Title"));
         this.book.setName((if (var4 == null) this.book.getName() else var4) as java.lang.String);
         var4 = (java.util.Map)(if (info == null) null else info.get("Writer"));
         this.book.setAuthor((if (var4 == null) this.book.getAuthor() else var4) as java.lang.String);
      }

      this.updateCover();
   }

   private fun updateCover() {
   }

   private fun getContent(chapter: BookChapter): String? {
      return "";
   }

   private fun getChapterList(): ArrayList<BookChapter> {
      if (this.book.getTocUrl().length() == 0) {
         this.book.setTocUrl("page");
      }

      return if (this.book.getTocUrl() == "page") this.getChapterListByPage() else this.getChapterListByOutline();
   }

   private fun getChapterListByPage(): ArrayList<BookChapter> {
      val chapterList: ArrayList = new ArrayList();
      val document: PDDocument = PDDocument.load(this.book.getLocalFile());
      var var3: Int = 0;
      val var4: Int = document.getNumberOfPages();
      if (0 < var4) {
         do {
            val pageIndex: Int = var3++;
            val name: java.lang.String = "output-$pageIndex.png";
            val chapter: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
            chapter.setTitle(name);
            chapter.setIndex(pageIndex);
            chapter.setBookUrl(this.book.getBookUrl());
            chapter.setUrl(name);
            chapter.setStart((long)pageIndex);
            chapter.setEnd((long)pageIndex);
            chapterList.add(chapter);
         } while (var3 < var4);
      }

      val var10000: Book = this.book;
      val var8: BookChapter = CollectionsKt.lastOrNull(chapterList);
      var10000.setLatestChapterTitle(if (var8 == null) null else var8.getTitle());
      this.book.setTotalChapterNum(chapterList.size());
      Util.closeQuietly(document);
      return chapterList;
   }

   private fun getChapterListByOutline(): ArrayList<BookChapter> {
      val chapterList: ArrayList = new ArrayList();
      val document: PDDocument = PDDocument.load(this.book.getLocalFile());
      val outline: PDDocumentOutline = document.getDocumentCatalog().getDocumentOutline();
      if (outline == null) {
         return chapterList;
      } else {
         this.processOutline(document, chapterList, outline);
         if (chapterList.size() > 0) {
            (chapterList.get(chapterList.size() - 1) as BookChapter).setEnd((long)document.getNumberOfPages());
         }

         Util.closeQuietly(document);
         return chapterList;
      }
   }

   private fun processOutline(document: PDDocument, chapterList: ArrayList<BookChapter>, outline: PDOutlineNode) {
      var current: PDOutlineItem = outline.getFirstChild();

      while (current != null) {
         val pageIndex: Int = document.getDocumentCatalog().getPages().indexOf(current.findDestinationPage(document));
         if (chapterList.size() == 0 && pageIndex >= 1) {
            val chapter: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
            chapter.setTitle("首章");
            chapter.setIndex(0);
            chapter.setBookUrl(this.book.getBookUrl());
            chapter.setUrl("chapter-0");
            chapter.setStart(0L);
            chapter.setEnd((long)pageIndex);
            chapterList.add(chapter);
         }

         if (chapterList.size() > 0) {
            val var10000: java.lang.Long = (chapterList.get(chapterList.size() - 1) as BookChapter).getStart();
            val var9: Long = pageIndex;
            if (var10000 != null) {
               if (var10000 == var9) {
                  current = current.getNextSibling();
                  continue;
               }
            }

            val var10: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
            val var8: java.lang.String = current.getTitle();
            var10.setTitle(var8);
            var10.setIndex(chapterList.size());
            var10.setBookUrl(this.book.getBookUrl());
            var10.setUrl(Intrinsics.stringPlus("chapter-", chapterList.size()));
            var10.setStart((long)pageIndex);
            (chapterList.get(chapterList.size() - 1) as BookChapter).setEnd((long)pageIndex - 1L);
            chapterList.add(var10);
         }

         if (current.hasChildren()) {
            this.processOutline(document, chapterList, current);
         }

         current = current.getNextSibling();
      }
   }

   public companion object {
      private final var cFile: PdfFile?

      @Synchronized
      private fun getPdfFile(book: Book): PdfFile {
         if (PdfFile.access$getCFile$cp() != null) {
            var var2: PdfFile = PdfFile.access$getCFile$cp();
            if ((if (var2 == null) null else var2.getBook().getBookUrl()) == book.getBookUrl()) {
               var2 = PdfFile.access$getCFile$cp();
               if (var2 != null) {
                  var2.setBook(book);
               }

               val var6: PdfFile = PdfFile.access$getCFile$cp();
               return var6;
            }
         }

         PdfFile.access$setCFile$cp(new PdfFile(book));
         val var5: PdfFile = PdfFile.access$getCFile$cp();
         return var5;
      }

      @Synchronized
      public fun getChapterList(book: Book): ArrayList<BookChapter> {
         return PdfFile.access$getChapterList(this.getPdfFile(book));
      }

      @Synchronized
      public fun getContent(book: Book, chapter: BookChapter): String? {
         return PdfFile.access$getContent(this.getPdfFile(book), chapter);
      }

      @Synchronized
      public fun upBookInfo(book: Book, onlyCover: Boolean = false) {
         if (onlyCover) {
            PdfFile.access$updateCover(this.getPdfFile(book));
         } else {
            PdfFile.access$upBookInfo(this.getPdfFile(book));
         }
      }
   }
}
