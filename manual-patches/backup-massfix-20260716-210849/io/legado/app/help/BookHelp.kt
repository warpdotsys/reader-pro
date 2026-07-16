package io.legado.app.help

import io.legado.app.constant.AppPattern
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.localBook.LocalBook
import io.legado.app.utils.FileExtensionsKt
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.NetworkUtils
import java.io.File
import java.util.ArrayList
import java.util.Arrays
import java.util.concurrent.CopyOnWriteArraySet
import java.util.regex.Matcher
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.StringCompanionObject
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object BookHelp {
   private const val cacheImageFolderName: String = "images"
   private final val downloadImages: CopyOnWriteArraySet<String> = new CopyOnWriteArraySet()

   private fun formatFolderName(folderName: String): String {
      return new Regex("[\\\\/:*?\"<>|.]").replace(folderName, "");
   }

   public fun formatAuthor(author: String?): String {
      val var10000: java.lang.String;
      if (author == null) {
         var10000 = "";
      } else {
         val var3: java.lang.String = new Regex("作\\s*者[\\s:：]*").replace(author, "");
         if (var3 == null) {
            var10000 = "";
         } else {
            val var16: java.lang.String = new Regex("\\s+").replace(var3, " ");
            if (var16 == null) {
               var10000 = "";
            } else {
               val var26: java.lang.CharSequence = var16;
               var `startIndex$iv$iv`: Int = 0;
               var `endIndex$iv$iv`: Int = var26.length() - 1;
               var `startFound$iv$iv`: Boolean = false;

               while (startIndex$iv$iv <= endIndex$iv$iv) {
                  val var27: Boolean = Intrinsics.compare(var26.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
                  if (!`startFound$iv$iv`) {
                     if (!var27) {
                        `startFound$iv$iv` = true;
                     } else {
                        `startIndex$iv$iv`++;
                     }
                  } else {
                     if (!var27) {
                        break;
                     }

                     `endIndex$iv$iv`--;
                  }
               }

               val var19: java.lang.String = var26.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
               var10000 = if (var19 == null) "" else var19;
            }
         }
      }

      return var10000;
   }

   public fun formatBookName(name: String): String {
      val var14: java.lang.CharSequence = AppPattern.INSTANCE.getNameRegex().replace(name, "");
      var `startIndex$iv$iv`: Int = 0;
      var `endIndex$iv$iv`: Int = var14.length() - 1;
      var `startFound$iv$iv`: Boolean = false;

      while (startIndex$iv$iv <= endIndex$iv$iv) {
         val var16: Boolean = Intrinsics.compare(var14.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
         if (!`startFound$iv$iv`) {
            if (!var16) {
               `startFound$iv$iv` = true;
            } else {
               `startIndex$iv$iv`++;
            }
         } else {
            if (!var16) {
               break;
            }

            `endIndex$iv$iv`--;
         }
      }

      return var14.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
   }

   public fun formatBookAuthor(author: String): String {
      val var14: java.lang.CharSequence = AppPattern.INSTANCE.getAuthorRegex().replace(author, "");
      var `startIndex$iv$iv`: Int = 0;
      var `endIndex$iv$iv`: Int = var14.length() - 1;
      var `startFound$iv$iv`: Boolean = false;

      while (startIndex$iv$iv <= endIndex$iv$iv) {
         val var16: Boolean = Intrinsics.compare(var14.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
         if (!`startFound$iv$iv`) {
            if (!var16) {
               `startFound$iv$iv` = true;
            } else {
               `startIndex$iv$iv`++;
            }
         } else {
            if (!var16) {
               break;
            }

            `endIndex$iv$iv`--;
         }
      }

      return var14.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
   }

   public fun getBookCacheDir(book: Book): File {
      val md5Encode: java.lang.String = MD5Utils.INSTANCE.md5Encode(book.getBookUrl()).toString();
      val bookDir: java.lang.String = book.getBookDir();
      if (bookDir.length() == 0) {
         throw new Exception("bookDir不能为空");
      } else {
         val var6: File = FileExtensionsKt.getFile(new File(bookDir), md5Encode);
         if (!var6.exists()) {
            var6.mkdirs();
         }

         return var6;
      }
   }

   public fun getContent(book: Book, bookChapter: BookChapter): String? {
      val var10000: File = this.getBookCacheDir(book);
      val content: Array<java.lang.String> = new java.lang.String[1];
      val var5: StringCompanionObject = StringCompanionObject.INSTANCE;
      val var7: Array<Any> = new Object[]{bookChapter.getIndex()};
      val var10003: java.lang.String = java.lang.String.format("%d.txt", Arrays.copyOf(var7, var7.length));
      content[0] = var10003;
      val file: File = FileExtensionsKt.getFile(var10000, content);
      if (file.exists()) {
         return FilesKt.readText$default(file, null, 1, null);
      } else if (book.isLocalBook()) {
         val var9: java.lang.String = LocalBook.INSTANCE.getContent(book, bookChapter);
         if (var9 != null && book.isEpub()) {
            this.saveText(book, bookChapter, var9);
         }

         return var9;
      } else {
         return null;
      }
   }

   public fun delContent(book: Book, bookChapter: BookChapter) {
      val var10000: FileUtils = FileUtils.INSTANCE;
      val var10001: File = this.getBookCacheDir(book);
      val var3: Array<java.lang.String> = new java.lang.String[1];
      val var4: StringCompanionObject = StringCompanionObject.INSTANCE;
      val var6: Array<Any> = new Object[]{bookChapter.getIndex()};
      val var10004: java.lang.String = java.lang.String.format("%d.txt", Arrays.copyOf(var6, var6.length));
      var3[0] = var10004;
      var10000.createFileIfNotExist(var10001, var3).delete();
   }

   public suspend fun saveContent(scope: CoroutineScope, bookSource: BookSource, book: Book, bookChapter: BookChapter, content: String) {
      this.saveText(book, bookChapter, content);
      val var10000: Any = this.saveImages(scope, bookSource, book, bookChapter, content, `$completion`);
      return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
   }

   public fun saveText(book: Book, bookChapter: BookChapter, content: String) {
      val var10000: FileUtils = FileUtils.INSTANCE;
      val var10001: File = this.getBookCacheDir(book);
      val var4: Array<java.lang.String> = new java.lang.String[1];
      val var5: StringCompanionObject = StringCompanionObject.INSTANCE;
      val var7: Array<Any> = new Object[]{bookChapter.getIndex()};
      val var10004: java.lang.String = java.lang.String.format("%d.txt", Arrays.copyOf(var7, var7.length));
      var4[0] = var10004;
      FilesKt.writeText$default(var10000.createFileIfNotExist(var10001, var4), content, null, 2, null);
   }

   public suspend fun saveImages(scope: CoroutineScope, bookSource: BookSource, book: Book, bookChapter: BookChapter, content: String) {
      var `$continuation`: Continuation;
      label49: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label49;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.saveImages(null, null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var25: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10: java.util.Iterator;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val awaitList: ArrayList = new ArrayList();

            val var27: java.lang.Iterable;
            for (Object element$iv : var27) {
               val matcher: Matcher = AppPattern.INSTANCE.getImgPattern().matcher(`element$iv` as java.lang.String);
               if (matcher.find()) {
                  val var15: java.lang.String = matcher.group(1);
                  if (var15 != null) {
                     val mSrc: java.lang.String = NetworkUtils.INSTANCE.getAbsoluteURL(bookChapter.getUrl(), var15);
                     Boxing.boxBoolean(
                        awaitList.add(
                           BuildersKt.async$default(
                              scope, null, null, (new Function2<CoroutineScope, Continuation<? super Integer>, Object>(bookSource, book, mSrc, null) {
                                 int label;

                                 {
                                    super(2, `$completionx`);
                                    this.$bookSource = `$bookSource`;
                                    this.$book = `$book`;
                                    this.$mSrc = `$mSrc`;
                                 }

                                 @Nullable
                                 @Override
                                 public final Object invokeSuspend(@NotNull Object $result) {
                                    val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                    switch (this.label) {
                                       case 0:
                                          ResultKt.throwOnFailure(`$result`);
                                          val var10000: BookHelp = BookHelp.INSTANCE;
                                          val var10001: BookSource = this.$bookSource;
                                          val var10002: Book = this.$book;
                                          val var10003: java.lang.String = this.$mSrc;
                                          val var10004: Continuation = this;
                                          this.label = 1;
                                          if (var10000.saveImage(var10001, var10002, var10003, var10004) === var2) {
                                             return var2;
                                          }
                                          break;
                                       case 1:
                                          ResultKt.throwOnFailure(`$result`);
                                          break;
                                       default:
                                          throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                    }

                                    return Boxing.boxInt(1);
                                 }

                                 @NotNull
                                 @Override
                                 public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                                    return new <anonymous constructor>(this.$bookSource, this.$book, this.$mSrc, `$completion`);
                                 }

                                 @Nullable
                                 public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Integer> p2) {
                                    return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                                 }
                              }) as Function2, 3, null
                           )
                        )
                     );
                  }
               }
            }

            var10 = awaitList.iterator();
            break;
         case 1:
            var10 = `$continuation`.L$0 as java.util.Iterator;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      while (var10.hasNext()) {
         val var33: Deferred = var10.next() as Deferred;
         `$continuation`.L$0 = var10;
         `$continuation`.label = 1;
         if (var33.await(`$continuation`) === var25) {
            return var25;
         }
      }

      return Unit.INSTANCE;
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookHelp.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: decompilation failed (see manual patch)
     */
    public suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
        while (downloadImages.contains(src)) {
            delay(100L)
        }
        if (getImage(book, src).exists()) {
            return
        }
        downloadImages.add(src)
        try {
            val analyzeUrl = AnalyzeUrl(
                mUrl = src,
                baseUrl = null,
                source = bookSource
            )
            val bytes = analyzeUrl.getByteArrayAwait()
            val fileName = MD5Utils.md5Encode16(src) + '.' + getImageSuffix(src)
            val file = FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", fileName)
            file.writeBytes(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            downloadImages.remove(src)
        }
    }

   public fun getImage(book: Book, src: String): File {
      return FileExtensionsKt.getFile(this.getBookCacheDir(book), "images", "${MD5Utils.INSTANCE.md5Encode16(src)}.${this.getImageSuffix(src)}");
   }

   public fun getImageSuffix(src: String): String {
      val suffix: java.lang.String = StringsKt.substringBefore$default(StringsKt.substringAfterLast$default(src, ".", null, 2, null), ",", null, 2, null);
      return if (suffix.length() <= 5 && new Regex("^[a-z0-9]+$", RegexOption.IGNORE_CASE).matches(suffix)) suffix else "jpg";
   }
}
