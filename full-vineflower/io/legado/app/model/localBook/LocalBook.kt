package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.exception.TocEmptyException
import io.legado.app.help.BookHelp
import io.legado.app.utils.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Result.Companion
import kotlin.jvm.internal.Intrinsics

public object LocalBook {
   private final val nameAuthorPatterns: Array<Pattern>

   @Throws(java/io/FileNotFoundException::class, java/lang/SecurityException::class)
   public fun getBookInputStream(book: Book): InputStream {
      val file: File = book.getLocalFile();
      if (file.exists()) {
         return new FileInputStream(file);
      } else {
         throw new FileNotFoundException(Intrinsics.stringPlus(book.getName(), " 文件不存在"));
      }
   }

   @Throws(java/lang/Exception::class)
   public fun getChapterList(book: Book): ArrayList<BookChapter> {
      val chapters: ArrayList = if (book.isEpub())
         EpubFile.Companion.getChapterList(book)
         else
         (
            if (book.isUmd())
               UmdFile.Companion.getChapterList(book)
               else
               (
                  if (book.isCbz())
                     CbzFile.Companion.getChapterList(book)
                     else
                     (if (book.isPdf()) PdfFile.Companion.getChapterList(book) else TextFile.Companion.getChapterList(book))
               )
         );
      if (chapters.isEmpty()) {
         throw new TocEmptyException(Intrinsics.stringPlus("Chapterlist is empty  ", book.getLocalFile()));
      } else {
         return chapters;
      }
   }

   public fun getContent(book: Book, chapter: BookChapter): String? {
      return if (book.isEpub())
         EpubFile.Companion.getContent(book, chapter)
         else
         (
            if (book.isUmd())
               UmdFile.Companion.getContent(book, chapter)
               else
               (
                  if (book.isCbz())
                     CbzFile.Companion.getContent(book, chapter)
                     else
                     (if (book.isPdf()) PdfFile.Companion.getContent(book, chapter) else TextFile.Companion.getContent(book, chapter))
               )
         );
   }

   public fun analyzeNameAuthor(fileName: String): Pair<String, String> {
      val tempFileName: java.lang.String = StringsKt.substringBeforeLast$default(fileName, ".", null, 2, null);
      val var5: Array<Pattern> = nameAuthorPatterns;
      var var6: Int = 0;
      val var7: Int = nameAuthorPatterns.length;

      while (var6 < var7) {
         val pattern: Pattern = var5[var6];
         var6++;
         val var10: Matcher = pattern.matcher(tempFileName);
         val it: Matcher = if (var10.find()) var10 else null;
         if (it != null) {
            val var10000: java.lang.String = it.group(2);
            val group3: java.lang.String = it.group(1);
            val group1: java.lang.String = if (group3 == null) "" else group3;
            val var17: java.lang.String = it.group(3);
            return new Pair<>(var10000, BookHelp.INSTANCE.formatBookAuthor(Intrinsics.stringPlus(group1, if (var17 == null) "" else var17)));
         }
      }

      val var20: java.lang.String = BookHelp.INSTANCE.formatBookName(tempFileName);
      val var24: java.lang.String = BookHelp.INSTANCE.formatBookAuthor(StringsKt.replace$default(tempFileName, var20, "", false, 4, null));
      val var23: java.lang.String = if (var24.length() != tempFileName.length()) var24 else null;
      return new Pair<>(var20, if (var23 == null) "" else var23);
   }

   public fun deleteBook(book: Book) {
      try {
         var var8: Companion = Result.Companion;
         val var11: File = book.getLocalFile();
         if ((book.isLocalTxt() || book.isUmd()) && var11.exists()) {
            var11.delete();
         }

         if (book.isEpub()) {
            val var13: File = var11.getParentFile();
            if (var13.exists()) {
               FileUtils.INSTANCE.delete(var13, true);
            }
         }

         var8 = (Companion)Result.constructor-impl(Unit.INSTANCE);
      } catch (var7: java.lang.Throwable) {
         val bookFile: Companion = Result.Companion;
         val var3: Any = Result.constructor-impl(ResultKt.createFailure(var7));
      }
   }
}
