package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.TxtTocRule
import io.legado.app.help.DefaultData
import io.legado.app.utils.EncodingDetect
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.StringUtils
import io.legado.app.utils.Utf8BomUtils
import java.io.Closeable
import java.io.InputStream
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.Arrays
import java.util.regex.Matcher
import java.util.regex.Pattern

public class TextFile(book: Book) {
   private final val blank: Byte
   private final val book: Book
   private final val bufferSize: Int
   private final var charset: Charset
   private final val maxLengthWithNoToc: Int
   private final val maxLengthWithToc: Int

   init {
      this.book = book;
      this.blank = 10;
      this.bufferSize = 512000;
      this.maxLengthWithNoToc = 10240;
      this.maxLengthWithToc = 102400;
      this.charset = this.book.fileCharset();
   }

   @Throws(java/io/FileNotFoundException::class)
   public fun getChapterList(): ArrayList<BookChapter> {
      label110: {
         if (this.book.getCharset() == null || StringsKt.isBlank(this.book.getTocUrl())) {
            val toc: Closeable = LocalBook.INSTANCE.getBookInputStream(this.book);
            var var19: java.lang.Throwable = null as java.lang.Throwable;

            try {
               try {
                  val `index$iv`: InputStream = toc as InputStream;
                  val `item$iv`: ByteArray = new byte[this.bufferSize];
                  val length: Int = `index$iv`.read(`item$iv`);
                  val blockContent: java.lang.CharSequence = this.book.getCharset();
                  if (blockContent == null || StringsKt.isBlank(blockContent)) {
                     val var10000: Book = this.book;
                     val var10001: EncodingDetect = EncodingDetect.INSTANCE;
                     val var10002: ByteArray = Arrays.copyOf(`item$iv`, length);
                     var10000.setCharset(var10001.getEncode(var10002));
                  }

                  this.charset = this.book.fileCharset();
                  if (StringsKt.isBlank(this.book.getTocUrl())) {
                     val var28: java.lang.String = new java.lang.String(`item$iv`, 0, length, this.charset);
                     val var37: Book = this.book;
                     val var32: Pattern = this.getTocRule(var28);
                     val var39: java.lang.String;
                     if (var32 == null) {
                        var39 = "";
                     } else {
                        val var35: java.lang.String = var32.pattern();
                        var39 = if (var35 == null) "" else var35;
                     }

                     var37.setTocUrl(var39);
                  }
               } catch (var12: java.lang.Throwable) {
                  var19 = var12;
                  throw var12;
               }
            } catch (var13: java.lang.Throwable) {
               CloseableKt.closeFinally(toc, var19);
            }

            CloseableKt.closeFinally(toc, null as java.lang.Throwable);
         }

         val var40: Pattern = Pattern.compile(this.book.getTocUrl(), 8);
         val var16: ArrayList = this.analyze(var40);
         val var18: java.lang.Iterable = var16;
         var var24: Int = 0;

         for (Object item$ivx : $this$forEachIndexed$iv) {
            val var27: Int = var24++;
            if (var27 < 0) {
               CollectionsKt.throwIndexOverflow();
            }

            val var33: BookChapter = `item$ivx` as BookChapter;
            (`item$ivx` as BookChapter).setIndex(var27);
            var33.setBookUrl(this.book.getBookUrl());
            var33.setUrl(MD5Utils.INSTANCE.md5Encode16("${this.book.getOriginName()}$var27${var33.getTitle()}"));
         }

         this.book.setLatestChapterTitle(CollectionsKt.last(var16).getTitle());
         this.book.setTotalChapterNum(var16.size());
         return var16;
      }
   }

   private fun analyze(pattern: Pattern?): ArrayList<BookChapter> {
      val toc: java.lang.CharSequence = if (pattern == null) null else pattern.pattern();
      if (toc == null || toc.length() == 0) {
         return analyze$default(this, 0L, 0L, 3, null);
      } else if (pattern == null) {
         return analyze$default(this, 0L, 0L, 3, null);
      } else {
         label275: {
            val var47: ArrayList = new ArrayList();
            val var49: Closeable = LocalBook.INSTANCE.getBookInputStream(this.book);
            var var51: java.lang.Throwable = null as java.lang.Throwable;

            try {
               try {
                  val bis: InputStream = var49 as InputStream;
                  var curOffset: Long = 0L;
                  val buffer: ByteArray = new byte[this.bufferSize];
                  var bufferStart: Int = 3;
                  bis.read(buffer, 0, 3);
                  if (Utf8BomUtils.INSTANCE.hasBom(buffer)) {
                     bufferStart = 0;
                     curOffset = 3L;
                  }

                  while (true) {
                     var end: Int = bis.read(buffer, bufferStart, this.bufferSize - bufferStart);
                     if (end <= 0) {
                        break;
                     }

                     end = bufferStart + end;
                     if (bufferStart + end == this.bufferSize) {
                        var var56: Int = bufferStart + end - 1;
                        if (0 <= bufferStart + end - 1) {
                           do {
                              val var59: Int = var56--;
                              if (buffer[var59] == this.blank) {
                                 end = var59;
                                 break;
                              }
                           } while (0 <= var56);
                        }
                     }

                     val var53: java.lang.String = new java.lang.String(buffer, 0, end, this.charset);
                     ArraysKt.copyInto(buffer, buffer, 0, end, bufferStart + end);
                     bufferStart = bufferStart + end - end;
                     var var58: Int = 0;
                     val var62: Matcher = pattern.matcher(var53);
                     val var61: Matcher = var62;

                     while (matcher.find()) {
                        val var63: Int = var61.start();
                        val var10000: java.lang.String = var53.substring(var58, var63);
                        if (var10000 == null) {
                           throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }

                        val var95: ByteArray = var10000.getBytes(this.charset);
                        val chapterLength: Int = var95.length;
                        var var66: BookChapter = CollectionsKt.lastOrNull(var47);
                        val var96: Long;
                        if (var66 == null) {
                           var96 = curOffset;
                        } else {
                           val curChapter: java.lang.Long = var66.getStart();
                           var96 = if (curChapter == null) curOffset else curChapter;
                        }

                        if (this.book.getSplitLongChapter() && curOffset + chapterLength - var96 > this.maxLengthWithToc) {
                           var66 = CollectionsKt.lastOrNull(var47);
                           if (var66 != null) {
                              var66.setEnd(var66.getStart());
                           }

                           val var82: BookChapter = CollectionsKt.lastOrNull(var47);
                           val var75: java.lang.String = if (var82 == null) null else var82.getTitle();
                           val var98: Int;
                           if (var75 == null) {
                              var98 = 0;
                           } else {
                              val var99: ByteArray = var75.getBytes(this.charset);
                              var98 = if (var99 == null) 0 else var99.length;
                           }

                           val var87: ArrayList = this.analyze(var96 + (long)var98, curOffset + (long)chapterLength);
                           if (var75 != null) {
                              val `$this$forEachIndexed$iv`: java.lang.Iterable = var87;
                              var `index$iv`: Int = 0;

                              for (Object item$iv : $this$forEachIndexed$iv) {
                                 val var38: Int = `index$iv`++;
                                 if (var38 < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                 }

                                 (`item$iv` as BookChapter).setTitle("$var75(${var38 + 1})");
                              }
                           }

                           var47.addAll(var87);
                           val var90: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                           val var91: java.lang.String = var61.group();
                           var90.setTitle(var91);
                           var90.setStart(curOffset + (long)chapterLength);
                           var47.add(var90);
                        } else if (var58 != 0 || var63 == 0) {
                           if (!var47.isEmpty()) {
                              var66 = CollectionsKt.last(var47);
                              var66.setVolume(StringsKt.isBlank(StringsKt.substringAfter$default(var10000, var66.getTitle(), null, 2, null)));
                              val var102: java.lang.Long = var66.getStart();
                              val var103: Long = var102;
                              if (var10000 == null) {
                                 throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                              }

                              val var10002: ByteArray = var10000.getBytes(this.charset);
                              var66.setEnd(var103 + (long)var10002.length);
                              val var80: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                              val var85: java.lang.String = var61.group();
                              var80.setTitle(var85);
                              var80.setStart(var66.getEnd());
                              var47.add(var80);
                           } else {
                              var66 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                              val var81: java.lang.String = var61.group();
                              var66.setTitle(var81);
                              var66.setStart(curOffset);
                              var66.setEnd(curOffset);
                              var47.add(var66);
                           }
                        } else if (var47.isEmpty()) {
                           if (StringUtils.INSTANCE.trim(var10000).length() > 0) {
                              var66 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                              var66.setTitle("前言");
                              var66.setStart(curOffset);
                              var66.setEnd((long)chapterLength);
                              var47.add(var66);
                           }

                           var66 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                           val var77: java.lang.String = var61.group();
                           var66.setTitle(var77);
                           var66.setStart((long)chapterLength);
                           var47.add(var66);
                        } else {
                           var66 = CollectionsKt.last(var47);
                           var66.setVolume(StringsKt.isBlank(StringsKt.substringAfter$default(var10000, var66.getTitle(), null, 2, null)));
                           val var10001: java.lang.Long = var66.getEnd();
                           var66.setEnd(var10001 + (long)chapterLength);
                           val var78: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                           val chapters: java.lang.String = var61.group();
                           var78.setTitle(chapters);
                           var78.setStart(var66.getEnd());
                           var47.add(var78);
                        }

                        var58 += var10000.length();
                     }

                     curOffset += end;
                     val var64: BookChapter = CollectionsKt.lastOrNull(var47);
                     if (var64 != null) {
                        var64.setEnd(curOffset);
                     }
                  }
               } catch (var43: java.lang.Throwable) {
                  var51 = var43;
                  throw var43;
               }
            } catch (var44: java.lang.Throwable) {
               CloseableKt.closeFinally(var49, var51);
            }

            CloseableKt.closeFinally(var49, null as java.lang.Throwable);
         }
      }
   }

   private fun analyze(fileStart: Long = 0L, fileEnd: Long = java.lang.Long.MAX_VALUE): ArrayList<BookChapter> {
      label92: {
         val toc: ArrayList = new ArrayList();
         val var32: Closeable = LocalBook.INSTANCE.getBookInputStream(this.book);
         var var33: java.lang.Throwable = null as java.lang.Throwable;

         try {
            try {
               val bis: InputStream = var32 as InputStream;
               var blockPos: Int = 0;
               var curOffset: Long = 0L;
               var chapterPos: Int = 0;
               val buffer: ByteArray = new byte[this.bufferSize];
               var var36: Int = 3;
               if (fileStart == 0L) {
                  bis.read(buffer, 0, 3);
                  if (Utf8BomUtils.INSTANCE.hasBom(buffer)) {
                     var36 = 0;
                     curOffset = 3L;
                  }
               } else {
                  bis.skip(fileStart);
                  curOffset = fileStart;
                  var36 = 0;
               }

               while (fileEnd - curOffset - var36 > 0L) {
                  val chapter: Int = bis.read(buffer, var36, (int)Math.min((long)(this.bufferSize - var36), fileEnd - curOffset - (long)var36));
                  if (chapter <= 0) {
                     break;
                  }

                  blockPos++;
                  var var40: Int = 0;
                  var var35: Int = chapter + var36;
                  var var43: Int = chapter + var36;
                  chapterPos = 0;

                  while (strLength > 0) {
                     chapterPos++;
                     if (var43 > this.maxLengthWithNoToc) {
                        var var45: Int = var35;
                        var chapterx: Int = var40 + this.maxLengthWithNoToc;
                        if (var40 + this.maxLengthWithNoToc < var35) {
                           do {
                              val var38: Int = chapterx++;
                              if (buffer[var38] == this.blank) {
                                 var45 = var38;
                                 break;
                              }
                           } while (chapterx < var35);
                        }

                        val var48: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        var48.setTitle("第$blockPos章($chapterPos)");
                        val var39: BookChapter = CollectionsKt.lastOrNull(toc);
                        var var10001: java.lang.Long;
                        if (var39 == null) {
                           var10001 = curOffset;
                        } else {
                           val var27: java.lang.Long = var39.getEnd();
                           var10001 = if (var27 == null) curOffset else var27;
                        }

                        var48.setStart(var10001);
                        var10001 = var48.getStart();
                        var48.setEnd(var10001 + (long)var45 - (long)var40);
                        toc.add(var48);
                        var43 -= var45 - var40;
                        var40 = var45;
                     } else {
                        ArraysKt.copyInto(buffer, buffer, 0, var35 - var43, var35);
                        var35 -= var43;
                        var36 = var43;
                        var43 = 0;
                     }
                  }

                  curOffset += var35;
               }

               if (var36 <= 100 && !toc.isEmpty()) {
                  val var42: BookChapter = CollectionsKt.lastOrNull(toc);
                  if (var42 != null) {
                     val var54: java.lang.Long = var42.getEnd();
                     var42.setEnd(var54 + (long)var36);
                  }
               } else {
                  val var41: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                  var41.setTitle("第$blockPos章($chapterPos)");
                  val var44: BookChapter = CollectionsKt.lastOrNull(toc);
                  var var52: java.lang.Long;
                  if (var44 == null) {
                     var52 = curOffset;
                  } else {
                     val var46: java.lang.Long = var44.getEnd();
                     var52 = if (var46 == null) curOffset else var46;
                  }

                  var41.setStart(var52);
                  var52 = var41.getStart();
                  var41.setEnd(var52 + (long)var36);
                  toc.add(var41);
               }
            } catch (var28: java.lang.Throwable) {
               var33 = var28;
               throw var28;
            }
         } catch (var29: java.lang.Throwable) {
            CloseableKt.closeFinally(var32, var33);
         }

         CloseableKt.closeFinally(var32, null as java.lang.Throwable);
      }
   }

   private fun getTocRule(content: String): Pattern? {
      val rules: java.util.List = CollectionsKt.reversed(this.getTocRules());
      var maxCs: Int = 1;
      var tocPattern: Pattern = null;

      for (TxtTocRule tocRule : rules) {
         val var10000: Pattern = Pattern.compile(tocRule.getRule(), 8);
         val var11: Matcher = var10000.matcher(content);
         var var12: Int = 0;

         while (matcher.find()) {
            var12++;
         }

         if (var12 >= maxCs) {
            maxCs = var12;
            tocPattern = var10000;
         }
      }

      return tocPattern;
   }

   private fun getTocRules(): List<TxtTocRule> {
      val `$this$filter$iv`: java.lang.Iterable = DefaultData.INSTANCE.getTxtTocRules();
      val `destination$iv$iv`: java.util.Collection = new ArrayList();

      for (Object element$iv$iv : $this$filter$iv) {
         if ((`element$iv$iv` as TxtTocRule).getEnable()) {
            `destination$iv$iv`.add(`element$iv$iv`);
         }
      }

      return `destination$iv$iv` as MutableList<TxtTocRule>;
   }

   public companion object {
      @Throws(java/io/FileNotFoundException::class)
      public fun getChapterList(book: Book): ArrayList<BookChapter> {
         return new TextFile(book).getChapterList();
      }

      @Throws(java/io/FileNotFoundException::class)
      public fun getContent(book: Book, bookChapter: BookChapter): String {
         // $VF: Couldn't be decompiled
         // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
         // java.lang.ClassCastException: class org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent cannot be cast to class org.jetbrains.java.decompiler.modules.decompiler.exps.IfExprent (org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent and org.jetbrains.java.decompiler.modules.decompiler.exps.IfExprent are in unnamed module of loader 'app')
         //   at org.jetbrains.java.decompiler.modules.decompiler.stats.IfStatement.initExprents(IfStatement.java:276)
         //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.initStatementExprents(ExprProcessor.java:189)
         //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.initStatementExprents(ExprProcessor.java:192)
         //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.initStatementExprents(ExprProcessor.java:192)
         //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.processStatement(ExprProcessor.java:148)
         //
         // Bytecode:
         // 00: aload 1
         // 01: ldc "book"
         // 03: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullParameter (Ljava/lang/Object;Ljava/lang/String;)V
         // 06: aload 2
         // 07: ldc "bookChapter"
         // 09: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullParameter (Ljava/lang/Object;Ljava/lang/String;)V
         // 0c: aload 2
         // 0d: invokevirtual io/legado/app/data/entities/BookChapter.getEnd ()Ljava/lang/Long;
         // 10: dup
         // 11: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
         // 14: invokevirtual java/lang/Long.longValue ()J
         // 17: aload 2
         // 18: invokevirtual io/legado/app/data/entities/BookChapter.getStart ()Ljava/lang/Long;
         // 1b: dup
         // 1c: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
         // 1f: invokevirtual java/lang/Long.longValue ()J
         // 22: lsub
         // 23: l2i
         // 24: istore 3
         // 25: iload 3
         // 26: newarray 8
         // 28: astore 4
         // 2a: getstatic io/legado/app/model/localBook/LocalBook.INSTANCE Lio/legado/app/model/localBook/LocalBook;
         // 2d: aload 1
         // 2e: invokevirtual io/legado/app/model/localBook/LocalBook.getBookInputStream (Lio/legado/app/data/entities/Book;)Ljava/io/InputStream;
         // 31: checkcast java/io/Closeable
         // 34: astore 5
         // 36: bipush 0
         // 37: istore 6
         // 39: bipush 0
         // 3a: istore 7
         // 3c: aconst_null
         // 3d: checkcast java/lang/Throwable
         // 40: astore 7
         // 42: nop
         // 43: aload 5
         // 45: checkcast java/io/InputStream
         // 48: astore 8
         // 4a: bipush 0
         // 4b: istore 9
         // 4d: aload 8
         // 4f: aload 2
         // 50: invokevirtual io/legado/app/data/entities/BookChapter.getStart ()Ljava/lang/Long;
         // 53: dup
         // 54: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
         // 57: invokevirtual java/lang/Long.longValue ()J
         // 5a: invokevirtual java/io/InputStream.skip (J)J
         // 5d: pop2
         // 5e: aload 8
         // 60: aload 4
         // 62: invokevirtual java/io/InputStream.read ([B)I
         // 65: istore 8
         // 67: aload 5
         // 69: aload 7
         // 6b: invokestatic kotlin/io/CloseableKt.closeFinally (Ljava/io/Closeable;Ljava/lang/Throwable;)V
         // 6e: goto 86
         // 71: astore 8
         // 73: aload 8
         // 75: astore 7
         // 77: aload 8
         // 79: athrow
         // 7a: astore 8
         // 7c: aload 5
         // 7e: aload 7
         // 80: invokestatic kotlin/io/CloseableKt.closeFinally (Ljava/io/Closeable;Ljava/lang/Throwable;)V
         // 83: aload 8
         // 85: athrow
         // 86: aload 1
         // 87: invokevirtual io/legado/app/data/entities/Book.getCharset ()Ljava/lang/String;
         // 8a: ifnonnull 9b
         // 8d: aload 1
         // 8e: getstatic io/legado/app/utils/EncodingDetect.INSTANCE Lio/legado/app/utils/EncodingDetect;
         // 91: aload 1
         // 92: invokevirtual io/legado/app/data/entities/Book.getLocalFile ()Ljava/io/File;
         // 95: invokevirtual io/legado/app/utils/EncodingDetect.getEncode (Ljava/io/File;)Ljava/lang/String;
         // 98: invokevirtual io/legado/app/data/entities/Book.setCharset (Ljava/lang/String;)V
         // 9b: aload 1
         // 9c: invokevirtual io/legado/app/data/entities/Book.fileCharset ()Ljava/nio/charset/Charset;
         // 9f: astore 5
         // a1: bipush 0
         // a2: istore 6
         // a4: new java/lang/String
         // a7: dup
         // a8: aload 4
         // aa: aload 5
         // ac: invokespecial java/lang/String.<init> ([BLjava/nio/charset/Charset;)V
         // af: aload 2
         // b0: invokevirtual io/legado/app/data/entities/BookChapter.getTitle ()Ljava/lang/String;
         // b3: aconst_null
         // b4: bipush 2
         // b5: aconst_null
         // b6: invokestatic kotlin/text/StringsKt.substringAfter$default (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
         // b9: checkcast java/lang/CharSequence
         // bc: astore 5
         // be: ldc "^[\\n\\s]+"
         // c0: astore 6
         // c2: bipush 0
         // c3: istore 7
         // c5: new kotlin/text/Regex
         // c8: dup
         // c9: aload 6
         // cb: invokespecial kotlin/text/Regex.<init> (Ljava/lang/String;)V
         // ce: astore 6
         // d0: ldc "　　"
         // d2: astore 7
         // d4: bipush 0
         // d5: istore 8
         // d7: aload 6
         // d9: aload 5
         // db: aload 7
         // dd: invokevirtual kotlin/text/Regex.replace (Ljava/lang/CharSequence;Ljava/lang/String;)Ljava/lang/String;
         // e0: areturn
      }
   }
}
