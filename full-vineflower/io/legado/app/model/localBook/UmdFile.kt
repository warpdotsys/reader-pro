package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import java.io.File
import java.io.InputStream
import java.nio.file.Paths
import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics
import me.ag2s.umdlib.domain.UmdBook
import me.ag2s.umdlib.domain.UmdChapters
import me.ag2s.umdlib.domain.UmdCover
import me.ag2s.umdlib.domain.UmdHeader
import me.ag2s.umdlib.umd.UmdReader

public class UmdFile(book: Book) {
   public final var book: Book
      internal set

   private final var umdBook: UmdBook?
      private final get() {
         if (this.umdBook != null) {
            return this.umdBook;
         } else {
            this.umdBook = this.readUmd();
            return this.umdBook;
         }
      }


   init {
      this.book = book;

      try {
         if (this.getUmdBook() != null) {
            ;
         }
      } catch (var8: Exception) {
         var8.printStackTrace();
      }
   }

   private fun readUmd(): UmdBook? {
      return new UmdReader().read(LocalBook.INSTANCE.getBookInputStream(this.book));
   }

   private fun upBookInfo() {
      if (this.getUmdBook() == null) {
         uFile = null;
         this.book.setIntro("书籍导入异常");
      } else {
         val var10000: UmdBook = this.getUmdBook();
         val hd: UmdHeader = var10000.getHeader();
         val var4: Book = this.book;
         var var2: java.lang.String = hd.getTitle();
         var4.setName(var2);
         val var5: Book = this.book;
         var2 = hd.getAuthor();
         var5.setAuthor(var2);
         this.book.setKind(hd.getBookType());
         this.updateCover();
      }
   }

   private fun updateCover() {
      if (this.getUmdBook() == null) {
         uFile = null;
      } else {
         val relativeCoverUrl: java.lang.String = Paths.get(
               "assets", this.book.getUserNameSpace(), "covers", Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), ".jpg")
            )
            .toString();
         this.book.setCoverUrl(Intrinsics.stringPlus("/", StringsKt.replace$default(relativeCoverUrl, "\\", "/", false, 4, null)));
         val var12: java.lang.String = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
         if (!new File(var12).exists()) {
            val var13: UmdBook = this.getUmdBook();
            if (var13 != null) {
               val var5: UmdCover = var13.getCover();
               if (var5 != null) {
                  val var6: ByteArray = var5.getCoverData();
                  if (var6 != null) {
                     FileUtils.INSTANCE.writeBytes(var12, var6);
                  }
               }
            }
         }
      }
   }

   private fun getContent(chapter: BookChapter): String? {
      val var2: UmdBook = this.getUmdBook();
      val var10000: java.lang.String;
      if (var2 == null) {
         var10000 = null;
      } else {
         val var3: UmdChapters = var2.getChapters();
         var10000 = if (var3 == null) null else var3.getContentString(chapter.getIndex());
      }

      return var10000;
   }

   private fun getChapterList(): ArrayList<BookChapter> {
      val chapterList: ArrayList = new ArrayList();
      val var2: UmdBook = this.getUmdBook();
      if (var2 != null) {
         val var3: UmdChapters = var2.getChapters();
         if (var3 != null) {
            val var4: java.util.List = var3.getTitles();
            if (var4 != null) {
               val `$this$forEachIndexed$iv`: java.lang.Iterable = var4;
               var `index$iv`: Int = 0;

               for (Object item$iv : $this$forEachIndexed$iv) {
                  val var10: Int = `index$iv`++;
                  if (var10 < 0) {
                     CollectionsKt.throwIndexOverflow();
                  }

                  val `$noName_1`: ByteArray = `item$iv` as ByteArray;
                  val var18: UmdBook = this.getUmdBook();
                  val title: java.lang.String = var18.getChapters().getTitle(var10);
                  val chapter: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                  chapter.setTitle(title);
                  chapter.setIndex(var10);
                  chapter.setBookUrl(this.getBook().getBookUrl());
                  chapter.setUrl(java.lang.String.valueOf(var10));
                  System.out.println(Intrinsics.stringPlus("UMD", chapter.getUrl()));
                  chapterList.add(chapter);
               }
            }
         }
      }

      val var19: Book = this.book;
      val var17: BookChapter = CollectionsKt.lastOrNull(chapterList);
      var19.setLatestChapterTitle(if (var17 == null) null else var17.getTitle());
      this.book.setTotalChapterNum(chapterList.size());
      return chapterList;
   }

   private fun getImage(href: String): InputStream? {
      return null;
   }

   public companion object {
      private final var uFile: UmdFile?

      @Synchronized
      private fun getUFile(book: Book): UmdFile {
         if (UmdFile.access$getUFile$cp() != null) {
            var var2: UmdFile = UmdFile.access$getUFile$cp();
            if ((if (var2 == null) null else var2.getBook().getBookUrl()) == book.getBookUrl()) {
               var2 = UmdFile.access$getUFile$cp();
               if (var2 != null) {
                  var2.setBook(book);
               }

               val var6: UmdFile = UmdFile.access$getUFile$cp();
               return var6;
            }
         }

         UmdFile.access$setUFile$cp(new UmdFile(book));
         val var5: UmdFile = UmdFile.access$getUFile$cp();
         return var5;
      }

      @Synchronized
      public fun getChapterList(book: Book): ArrayList<BookChapter> {
         return UmdFile.access$getChapterList(this.getUFile(book));
      }

      @Synchronized
      public fun getContent(book: Book, chapter: BookChapter): String? {
         return UmdFile.access$getContent(this.getUFile(book), chapter);
      }

      @Synchronized
      public fun getImage(book: Book, href: String): InputStream? {
         return UmdFile.access$getImage(this.getUFile(book), href);
      }

      @Synchronized
      public fun upBookInfo(book: Book, onlyCover: Boolean = false) {
         if (onlyCover) {
            UmdFile.access$updateCover(this.getUFile(book));
         } else {
            UmdFile.access$upBookInfo(this.getUFile(book));
         }
      }
   }
}
