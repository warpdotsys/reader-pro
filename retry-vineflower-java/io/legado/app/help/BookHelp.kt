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

   public suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.code.cfg.ExceptionRangeCFG.isCircular()" because "range" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.graphToStatement(DomHelper.java:84)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:203)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 4
      // 002: instanceof io/legado/app/help/BookHelp$saveImage$1
      // 005: ifeq 02b
      // 008: aload 4
      // 00a: checkcast io/legado/app/help/BookHelp$saveImage$1
      // 00d: astore 13
      // 00f: aload 13
      // 011: getfield io/legado/app/help/BookHelp$saveImage$1.label I
      // 014: ldc_w -2147483648
      // 017: iand
      // 018: ifeq 02b
      // 01b: aload 13
      // 01d: dup
      // 01e: getfield io/legado/app/help/BookHelp$saveImage$1.label I
      // 021: ldc_w -2147483648
      // 024: isub
      // 025: putfield io/legado/app/help/BookHelp$saveImage$1.label I
      // 028: goto 037
      // 02b: new io/legado/app/help/BookHelp$saveImage$1
      // 02e: dup
      // 02f: aload 0
      // 030: aload 4
      // 032: invokespecial io/legado/app/help/BookHelp$saveImage$1.<init> (Lio/legado/app/help/BookHelp;Lkotlin/coroutines/Continuation;)V
      // 035: astore 13
      // 037: aload 13
      // 039: getfield io/legado/app/help/BookHelp$saveImage$1.result Ljava/lang/Object;
      // 03c: astore 12
      // 03e: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 041: astore 14
      // 043: aload 13
      // 045: getfield io/legado/app/help/BookHelp$saveImage$1.label I
      // 048: tableswitch 399 0 2 28 90 235
      // 064: aload 12
      // 066: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 069: getstatic io/legado/app/help/BookHelp.downloadImages Ljava/util/concurrent/CopyOnWriteArraySet;
      // 06c: aload 3
      // 06d: invokevirtual java/util/concurrent/CopyOnWriteArraySet.contains (Ljava/lang/Object;)Z
      // 070: ifeq 0d1
      // 073: ldc2_w 100
      // 076: aload 13
      // 078: aload 13
      // 07a: aload 0
      // 07b: putfield io/legado/app/help/BookHelp$saveImage$1.L$0 Ljava/lang/Object;
      // 07e: aload 13
      // 080: aload 1
      // 081: putfield io/legado/app/help/BookHelp$saveImage$1.L$1 Ljava/lang/Object;
      // 084: aload 13
      // 086: aload 2
      // 087: putfield io/legado/app/help/BookHelp$saveImage$1.L$2 Ljava/lang/Object;
      // 08a: aload 13
      // 08c: aload 3
      // 08d: putfield io/legado/app/help/BookHelp$saveImage$1.L$3 Ljava/lang/Object;
      // 090: aload 13
      // 092: bipush 1
      // 093: putfield io/legado/app/help/BookHelp$saveImage$1.label I
      // 096: invokestatic kotlinx/coroutines/DelayKt.delay (JLkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 099: dup
      // 09a: aload 14
      // 09c: if_acmpne 0cd
      // 09f: aload 14
      // 0a1: areturn
      // 0a2: aload 13
      // 0a4: getfield io/legado/app/help/BookHelp$saveImage$1.L$3 Ljava/lang/Object;
      // 0a7: checkcast java/lang/String
      // 0aa: astore 3
      // 0ab: aload 13
      // 0ad: getfield io/legado/app/help/BookHelp$saveImage$1.L$2 Ljava/lang/Object;
      // 0b0: checkcast io/legado/app/data/entities/Book
      // 0b3: astore 2
      // 0b4: aload 13
      // 0b6: getfield io/legado/app/help/BookHelp$saveImage$1.L$1 Ljava/lang/Object;
      // 0b9: checkcast io/legado/app/data/entities/BookSource
      // 0bc: astore 1
      // 0bd: aload 13
      // 0bf: getfield io/legado/app/help/BookHelp$saveImage$1.L$0 Ljava/lang/Object;
      // 0c2: checkcast io/legado/app/help/BookHelp
      // 0c5: astore 0
      // 0c6: aload 12
      // 0c8: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 0cb: aload 12
      // 0cd: pop
      // 0ce: goto 069
      // 0d1: aload 0
      // 0d2: aload 2
      // 0d3: aload 3
      // 0d4: invokevirtual io/legado/app/help/BookHelp.getImage (Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/io/File;
      // 0d7: invokevirtual java/io/File.exists ()Z
      // 0da: ifeq 0e1
      // 0dd: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 0e0: areturn
      // 0e1: getstatic io/legado/app/help/BookHelp.downloadImages Ljava/util/concurrent/CopyOnWriteArraySet;
      // 0e4: aload 3
      // 0e5: invokevirtual java/util/concurrent/CopyOnWriteArraySet.add (Ljava/lang/Object;)Z
      // 0e8: pop
      // 0e9: new io/legado/app/model/analyzeRule/AnalyzeUrl
      // 0ec: dup
      // 0ed: aload 3
      // 0ee: aconst_null
      // 0ef: aconst_null
      // 0f0: aconst_null
      // 0f1: aconst_null
      // 0f2: aconst_null
      // 0f3: aload 1
      // 0f4: checkcast io/legado/app/data/entities/BaseSource
      // 0f7: aconst_null
      // 0f8: aconst_null
      // 0f9: aconst_null
      // 0fa: aconst_null
      // 0fb: sipush 1982
      // 0fe: aconst_null
      // 0ff: invokespecial io/legado/app/model/analyzeRule/AnalyzeUrl.<init> (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 102: astore 5
      // 104: nop
      // 105: aload 5
      // 107: aload 13
      // 109: aload 13
      // 10b: aload 2
      // 10c: putfield io/legado/app/help/BookHelp$saveImage$1.L$0 Ljava/lang/Object;
      // 10f: aload 13
      // 111: aload 3
      // 112: putfield io/legado/app/help/BookHelp$saveImage$1.L$1 Ljava/lang/Object;
      // 115: aload 13
      // 117: aconst_null
      // 118: putfield io/legado/app/help/BookHelp$saveImage$1.L$2 Ljava/lang/Object;
      // 11b: aload 13
      // 11d: aconst_null
      // 11e: putfield io/legado/app/help/BookHelp$saveImage$1.L$3 Ljava/lang/Object;
      // 121: aload 13
      // 123: bipush 2
      // 124: putfield io/legado/app/help/BookHelp$saveImage$1.label I
      // 127: invokevirtual io/legado/app/model/analyzeRule/AnalyzeUrl.getByteArrayAwait (Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 12a: dup
      // 12b: aload 14
      // 12d: if_acmpne 14d
      // 130: aload 14
      // 132: areturn
      // 133: aload 13
      // 135: getfield io/legado/app/help/BookHelp$saveImage$1.L$1 Ljava/lang/Object;
      // 138: checkcast java/lang/String
      // 13b: astore 3
      // 13c: aload 13
      // 13e: getfield io/legado/app/help/BookHelp$saveImage$1.L$0 Ljava/lang/Object;
      // 141: checkcast io/legado/app/data/entities/Book
      // 144: astore 2
      // 145: nop
      // 146: aload 12
      // 148: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 14b: aload 12
      // 14d: astore 6
      // 14f: bipush 0
      // 150: istore 7
      // 152: bipush 0
      // 153: istore 8
      // 155: aload 6
      // 157: checkcast [B
      // 15a: astore 9
      // 15c: bipush 0
      // 15d: istore 10
      // 15f: getstatic io/legado/app/utils/FileUtils.INSTANCE Lio/legado/app/utils/FileUtils;
      // 162: getstatic io/legado/app/help/BookHelp.INSTANCE Lio/legado/app/help/BookHelp;
      // 165: aload 2
      // 166: invokevirtual io/legado/app/help/BookHelp.getBookCacheDir (Lio/legado/app/data/entities/Book;)Ljava/io/File;
      // 169: bipush 2
      // 16a: anewarray 77
      // 16d: astore 11
      // 16f: aload 11
      // 171: bipush 0
      // 172: ldc_w "images"
      // 175: aastore
      // 176: aload 11
      // 178: bipush 1
      // 179: new java/lang/StringBuilder
      // 17c: dup
      // 17d: invokespecial java/lang/StringBuilder.<init> ()V
      // 180: getstatic io/legado/app/utils/MD5Utils.INSTANCE Lio/legado/app/utils/MD5Utils;
      // 183: aload 3
      // 184: invokevirtual io/legado/app/utils/MD5Utils.md5Encode16 (Ljava/lang/String;)Ljava/lang/String;
      // 187: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 18a: bipush 46
      // 18c: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 18f: getstatic io/legado/app/help/BookHelp.INSTANCE Lio/legado/app/help/BookHelp;
      // 192: aload 3
      // 193: invokevirtual io/legado/app/help/BookHelp.getImageSuffix (Ljava/lang/String;)Ljava/lang/String;
      // 196: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 199: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 19c: aastore
      // 19d: aload 11
      // 19f: invokevirtual io/legado/app/utils/FileUtils.createFileIfNotExist (Ljava/io/File;[Ljava/lang/String;)Ljava/io/File;
      // 1a2: aload 9
      // 1a4: invokestatic kotlin/io/FilesKt.writeBytes (Ljava/io/File;[B)V
      // 1a7: nop
      // 1a8: nop
      // 1a9: getstatic io/legado/app/help/BookHelp.downloadImages Ljava/util/concurrent/CopyOnWriteArraySet;
      // 1ac: aload 3
      // 1ad: invokevirtual java/util/concurrent/CopyOnWriteArraySet.remove (Ljava/lang/Object;)Z
      // 1b0: pop
      // 1b1: goto 1d3
      // 1b4: astore 6
      // 1b6: aload 6
      // 1b8: invokevirtual java/lang/Exception.printStackTrace ()V
      // 1bb: getstatic io/legado/app/help/BookHelp.downloadImages Ljava/util/concurrent/CopyOnWriteArraySet;
      // 1be: aload 3
      // 1bf: invokevirtual java/util/concurrent/CopyOnWriteArraySet.remove (Ljava/lang/Object;)Z
      // 1c2: pop
      // 1c3: goto 1d3
      // 1c6: astore 6
      // 1c8: getstatic io/legado/app/help/BookHelp.downloadImages Ljava/util/concurrent/CopyOnWriteArraySet;
      // 1cb: aload 3
      // 1cc: invokevirtual java/util/concurrent/CopyOnWriteArraySet.remove (Ljava/lang/Object;)Z
      // 1cf: pop
      // 1d0: aload 6
      // 1d2: athrow
      // 1d3: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 1d6: areturn
      // 1d7: new java/lang/IllegalStateException
      // 1da: dup
      // 1db: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 1de: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 1e1: athrow
   }

   public fun getImage(book: Book, src: String): File {
      return FileExtensionsKt.getFile(this.getBookCacheDir(book), "images", "${MD5Utils.INSTANCE.md5Encode16(src)}.${this.getImageSuffix(src)}");
   }

   public fun getImageSuffix(src: String): String {
      val suffix: java.lang.String = StringsKt.substringBefore$default(StringsKt.substringAfterLast$default(src, ".", null, 2, null), ",", null, 2, null);
      return if (suffix.length() <= 5 && new Regex("^[a-z0-9]+$", RegexOption.IGNORE_CASE).matches(suffix)) suffix else "jpg";
   }
}
