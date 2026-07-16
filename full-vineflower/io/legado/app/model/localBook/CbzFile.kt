package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.XmlUtils
import java.io.File
import java.io.InputStream
import java.nio.file.Paths
import java.util.ArrayList
import java.util.Enumeration
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.jvm.internal.Intrinsics

public class CbzFile(book: Book) {
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
      if (this.cover == null && this.info == null) {
         val zf: ZipFile = new ZipFile(this.book.getLocalFile());
         val entries: Enumeration = zf.entries();
         val imageExt: java.util.List = CollectionsKt.listOf(new java.lang.String[]{"jpg", "jpeg", "gif", "png", "bmp", "webp", "svg"});

         while (entries.hasMoreElements()) {
            var name: java.lang.String = (java.lang.String)entries.nextElement();
            if (name == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }

            val var9: ZipEntry = name as ZipEntry;
            if (!(name as ZipEntry).isDirectory()) {
               name = var9.getName();
               if (name.equals("ComicInfo.xml")) {
                  val ext: InputStream = zf.getInputStream(var9);
                  val var10001: XmlUtils = XmlUtils.INSTANCE;
                  this.info = var10001.xml2map(ext);
               } else if (this.cover == null) {
                  val var10000: FileUtils = FileUtils.INSTANCE;
                  val var7: java.lang.String = FileUtils.getFileExtetion$default(var10000, name, null, 2, null);
                  if (var7 == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var12: java.lang.String = var7.toLowerCase(Locale.ROOT);
                  if (imageExt.contains(var12)) {
                     this.cover = zf.getInputStream(var9);
                  }
               }
            }

            if (this.cover != null && this.info != null) {
               break;
            }
         }

         return new Pair<>(this.info, this.cover);
      } else {
         return new Pair<>(this.info, this.cover);
      }
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
      val relativeCoverUrl: java.lang.String = Paths.get(
            "assets", this.book.getUserNameSpace(), "covers", Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), ".jpg")
         )
         .toString();
      this.book.setCoverUrl(Intrinsics.stringPlus("/", StringsKt.replace$default(relativeCoverUrl, "\\", "/", false, 4, null)));
      val var7: java.lang.String = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
      if (!new File(var7).exists()) {
         val var8: Pair = this.parseBookInfo();
         if (var8.getSecond() != null) {
            val var6: Any = var8.getSecond();
            if (var6 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.io.InputStream");
            }

            FileUtils.INSTANCE.writeInputStream(var7, var6 as InputStream);
         }
      }
   }

   private fun getContent(chapter: BookChapter): String? {
      return "";
   }

   private fun getChapterList(): ArrayList<BookChapter> {
      val chapterList: ArrayList = new ArrayList();
      val entries: Enumeration = new ZipFile(this.book.getLocalFile()).entries();
      val imageFileList: ArrayList = new ArrayList();

      while (entries.hasMoreElements()) {
         var name: java.lang.String = (java.lang.String)entries.nextElement();
         if (name == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
         }

         val var10: ZipEntry = name as ZipEntry;
         if (!(name as ZipEntry).isDirectory()) {
            name = var10.getName();
            if (!StringsKt.endsWith$default(name, ".xml", false, 2, null)) {
               imageFileList.add(name);
            }
         }
      }

      CollectionsKt.sort(imageFileList);
      var var11: Int = 0;
      val var14: Int = imageFileList.size();
      if (0 < var14) {
         do {
            val i: Int = var11++;
            var chapter: BookChapter = (BookChapter)imageFileList.get(i);
            val namex: java.lang.String = chapter as java.lang.String;
            chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
            chapter.setTitle(namex);
            chapter.setIndex(i);
            chapter.setBookUrl(this.book.getBookUrl());
            chapter.setUrl(namex);
            chapterList.add(chapter);
         } while (var11 < var14);
      }

      val var10000: Book = this.book;
      val var12: BookChapter = CollectionsKt.lastOrNull(chapterList);
      var10000.setLatestChapterTitle(if (var12 == null) null else var12.getTitle());
      this.book.setTotalChapterNum(chapterList.size());
      return chapterList;
   }

   public companion object {
      private final var cFile: CbzFile?

      @Synchronized
      private fun getCbzFile(book: Book): CbzFile {
         if (CbzFile.access$getCFile$cp() != null) {
            var var2: CbzFile = CbzFile.access$getCFile$cp();
            if ((if (var2 == null) null else var2.getBook().getBookUrl()) == book.getBookUrl()) {
               var2 = CbzFile.access$getCFile$cp();
               if (var2 != null) {
                  var2.setBook(book);
               }

               val var6: CbzFile = CbzFile.access$getCFile$cp();
               return var6;
            }
         }

         CbzFile.access$setCFile$cp(new CbzFile(book));
         val var5: CbzFile = CbzFile.access$getCFile$cp();
         return var5;
      }

      @Synchronized
      public fun getChapterList(book: Book): ArrayList<BookChapter> {
         return CbzFile.access$getChapterList(this.getCbzFile(book));
      }

      @Synchronized
      public fun getContent(book: Book, chapter: BookChapter): String? {
         return CbzFile.access$getContent(this.getCbzFile(book), chapter);
      }

      @Synchronized
      public fun upBookInfo(book: Book, onlyCover: Boolean = false) {
         if (onlyCover) {
            CbzFile.access$updateCover(this.getCbzFile(book));
         } else {
            CbzFile.access$upBookInfo(this.getCbzFile(book));
         }
      }
   }
}
