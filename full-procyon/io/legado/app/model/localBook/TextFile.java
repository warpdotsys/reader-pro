// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.localBook;

import kotlin.text.StringsKt;
import kotlin.text.Regex;
import io.legado.app.utils.EncodingDetect;
import java.util.regex.Matcher;
import java.util.Iterator;
import io.legado.app.data.entities.TxtTocRule;
import kotlin.io.CloseableKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.utils.Utf8BomUtils;
import java.io.InputStream;
import java.io.Closeable;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import io.legado.app.data.entities.BookChapter;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import java.nio.charset.Charset;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\"\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J,\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0014H\u0002J\u0016\u0010\u0016\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u0010J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082D?\u0006\u0002\n\u0000¡§\u0006\u001e" }, d2 = { "Lio/legado/app/model/localBook/TextFile;", "", "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "blank", "", "bufferSize", "", "charset", "Ljava/nio/charset/Charset;", "maxLengthWithNoToc", "maxLengthWithToc", "analyze", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "Lkotlin/collections/ArrayList;", "pattern", "Ljava/util/regex/Pattern;", "fileStart", "", "fileEnd", "getChapterList", "getTocRule", "content", "", "getTocRules", "", "Lio/legado/app/data/entities/TxtTocRule;", "Companion", "reader-pro" })
public final class TextFile
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final Book book;
    private final byte blank;
    private final int bufferSize;
    private final int maxLengthWithNoToc;
    private final int maxLengthWithToc;
    @NotNull
    private Charset charset;
    
    public TextFile(@NotNull final Book book) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        this.book = book;
        this.blank = 10;
        this.bufferSize = 512000;
        this.maxLengthWithNoToc = 10240;
        this.maxLengthWithToc = 102400;
        this.charset = this.book.fileCharset();
    }
    
    @NotNull
    public final ArrayList<BookChapter> getChapterList() throws FileNotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //     4: invokevirtual   io/legado/app/data/entities/Book.getCharset:()Ljava/lang/String;
        //     7: ifnull          26
        //    10: aload_0         /* this */
        //    11: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //    14: invokevirtual   io/legado/app/data/entities/Book.getTocUrl:()Ljava/lang/String;
        //    17: checkcast       Ljava/lang/CharSequence;
        //    20: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //    23: ifeq            280
        //    26: getstatic       io/legado/app/model/localBook/LocalBook.INSTANCE:Lio/legado/app/model/localBook/LocalBook;
        //    29: aload_0         /* this */
        //    30: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //    33: invokevirtual   io/legado/app/model/localBook/LocalBook.getBookInputStream:(Lio/legado/app/data/entities/Book;)Ljava/io/InputStream;
        //    36: checkcast       Ljava/io/Closeable;
        //    39: astore_1       
        //    40: iconst_0       
        //    41: istore_2       
        //    42: iconst_0       
        //    43: istore_3       
        //    44: aconst_null    
        //    45: checkcast       Ljava/lang/Throwable;
        //    48: astore_3       
        //    49: nop            
        //    50: aload_1        
        //    51: checkcast       Ljava/io/InputStream;
        //    54: astore          bis
        //    56: iconst_0       
        //    57: istore          $i$a$-use-TextFile$getChapterList$1
        //    59: aload_0         /* this */
        //    60: getfield        io/legado/app/model/localBook/TextFile.bufferSize:I
        //    63: newarray        B
        //    65: astore          buffer
        //    67: aload           bis
        //    69: aload           buffer
        //    71: invokevirtual   java/io/InputStream.read:([B)I
        //    74: istore          length
        //    76: aload_0         /* this */
        //    77: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //    80: invokevirtual   io/legado/app/data/entities/Book.getCharset:()Ljava/lang/String;
        //    83: checkcast       Ljava/lang/CharSequence;
        //    86: astore          8
        //    88: iconst_0       
        //    89: istore          9
        //    91: iconst_0       
        //    92: istore          10
        //    94: aload           8
        //    96: ifnull          107
        //    99: aload           8
        //   101: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   104: ifeq            111
        //   107: iconst_1       
        //   108: goto            112
        //   111: iconst_0       
        //   112: ifeq            148
        //   115: aload_0         /* this */
        //   116: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   119: getstatic       io/legado/app/utils/EncodingDetect.INSTANCE:Lio/legado/app/utils/EncodingDetect;
        //   122: aload           buffer
        //   124: astore          8
        //   126: iconst_0       
        //   127: istore          9
        //   129: aload           8
        //   131: iload           length
        //   133: invokestatic    java/util/Arrays.copyOf:([BI)[B
        //   136: dup            
        //   137: ldc             "java.util.Arrays.copyOf(this, newSize)"
        //   139: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   142: invokevirtual   io/legado/app/utils/EncodingDetect.getEncode:([B)Ljava/lang/String;
        //   145: invokevirtual   io/legado/app/data/entities/Book.setCharset:(Ljava/lang/String;)V
        //   148: aload_0         /* this */
        //   149: aload_0         /* this */
        //   150: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   153: invokevirtual   io/legado/app/data/entities/Book.fileCharset:()Ljava/nio/charset/Charset;
        //   156: putfield        io/legado/app/model/localBook/TextFile.charset:Ljava/nio/charset/Charset;
        //   159: aload_0         /* this */
        //   160: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   163: invokevirtual   io/legado/app/data/entities/Book.getTocUrl:()Ljava/lang/String;
        //   166: checkcast       Ljava/lang/CharSequence;
        //   169: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   172: ifeq            248
        //   175: iconst_0       
        //   176: istore          9
        //   178: aload_0         /* this */
        //   179: getfield        io/legado/app/model/localBook/TextFile.charset:Ljava/nio/charset/Charset;
        //   182: astore          10
        //   184: iconst_0       
        //   185: istore          11
        //   187: new             Ljava/lang/String;
        //   190: dup            
        //   191: aload           buffer
        //   193: iload           9
        //   195: iload           length
        //   197: aload           10
        //   199: invokespecial   java/lang/String.<init>:([BIILjava/nio/charset/Charset;)V
        //   202: astore          blockContent
        //   204: aload_0         /* this */
        //   205: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   208: aload_0         /* this */
        //   209: aload           blockContent
        //   211: invokespecial   io/legado/app/model/localBook/TextFile.getTocRule:(Ljava/lang/String;)Ljava/util/regex/Pattern;
        //   214: astore          9
        //   216: aload           9
        //   218: ifnonnull       226
        //   221: ldc             ""
        //   223: goto            245
        //   226: aload           9
        //   228: invokevirtual   java/util/regex/Pattern.pattern:()Ljava/lang/String;
        //   231: astore          10
        //   233: aload           10
        //   235: ifnonnull       243
        //   238: ldc             ""
        //   240: goto            245
        //   243: aload           10
        //   245: invokevirtual   io/legado/app/data/entities/Book.setTocUrl:(Ljava/lang/String;)V
        //   248: nop            
        //   249: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   252: astore          4
        //   254: aload_1        
        //   255: aload_3        
        //   256: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   259: goto            280
        //   262: astore          4
        //   264: aload           4
        //   266: astore_3       
        //   267: aload           4
        //   269: athrow         
        //   270: astore          4
        //   272: aload_1        
        //   273: aload_3        
        //   274: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //   277: aload           4
        //   279: athrow         
        //   280: aload_0         /* this */
        //   281: aload_0         /* this */
        //   282: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   285: invokevirtual   io/legado/app/data/entities/Book.getTocUrl:()Ljava/lang/String;
        //   288: astore_2       
        //   289: bipush          8
        //   291: istore_3       
        //   292: iconst_0       
        //   293: istore          4
        //   295: aload_2        
        //   296: iload_3        
        //   297: invokestatic    java/util/regex/Pattern.compile:(Ljava/lang/String;I)Ljava/util/regex/Pattern;
        //   300: dup            
        //   301: ldc             "java.util.regex.Pattern.compile(this, flags)"
        //   303: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   306: invokespecial   io/legado/app/model/localBook/TextFile.analyze:(Ljava/util/regex/Pattern;)Ljava/util/ArrayList;
        //   309: astore_1        /* toc */
        //   310: aload_1         /* toc */
        //   311: checkcast       Ljava/lang/Iterable;
        //   314: astore_2        /* $this$forEachIndexed$iv */
        //   315: iconst_0       
        //   316: istore_3        /* $i$f$forEachIndexed */
        //   317: iconst_0       
        //   318: istore          index$iv
        //   320: aload_2         /* $this$forEachIndexed$iv */
        //   321: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   326: astore          5
        //   328: aload           5
        //   330: invokeinterface java/util/Iterator.hasNext:()Z
        //   335: ifeq            446
        //   338: aload           5
        //   340: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   345: astore          item$iv
        //   347: iload           index$iv
        //   349: iinc            index$iv, 1
        //   352: istore          7
        //   354: iconst_0       
        //   355: istore          8
        //   357: iload           7
        //   359: ifge            365
        //   362: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   365: iload           7
        //   367: aload           item$iv
        //   369: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   372: astore          9
        //   374: istore          index
        //   376: iconst_0       
        //   377: istore          $i$a$-forEachIndexed-TextFile$getChapterList$2
        //   379: aload           bookChapter
        //   381: iload           index
        //   383: invokevirtual   io/legado/app/data/entities/BookChapter.setIndex:(I)V
        //   386: aload           bookChapter
        //   388: aload_0         /* this */
        //   389: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   392: invokevirtual   io/legado/app/data/entities/Book.getBookUrl:()Ljava/lang/String;
        //   395: invokevirtual   io/legado/app/data/entities/BookChapter.setBookUrl:(Ljava/lang/String;)V
        //   398: aload           bookChapter
        //   400: getstatic       io/legado/app/utils/MD5Utils.INSTANCE:Lio/legado/app/utils/MD5Utils;
        //   403: new             Ljava/lang/StringBuilder;
        //   406: dup            
        //   407: invokespecial   java/lang/StringBuilder.<init>:()V
        //   410: aload_0         /* this */
        //   411: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   414: invokevirtual   io/legado/app/data/entities/Book.getOriginName:()Ljava/lang/String;
        //   417: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   420: iload           index
        //   422: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   425: aload           bookChapter
        //   427: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   430: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   433: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   436: invokevirtual   io/legado/app/utils/MD5Utils.md5Encode16:(Ljava/lang/String;)Ljava/lang/String;
        //   439: invokevirtual   io/legado/app/data/entities/BookChapter.setUrl:(Ljava/lang/String;)V
        //   442: nop            
        //   443: goto            328
        //   446: nop            
        //   447: aload_0         /* this */
        //   448: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   451: aload_1         /* toc */
        //   452: checkcast       Ljava/util/List;
        //   455: invokestatic    kotlin/collections/CollectionsKt.last:(Ljava/util/List;)Ljava/lang/Object;
        //   458: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   461: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   464: invokevirtual   io/legado/app/data/entities/Book.setLatestChapterTitle:(Ljava/lang/String;)V
        //   467: aload_0         /* this */
        //   468: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   471: aload_1         /* toc */
        //   472: invokevirtual   java/util/ArrayList.size:()I
        //   475: invokevirtual   io/legado/app/data/entities/Book.setTotalChapterNum:(I)V
        //   478: aload_1         /* toc */
        //   479: areturn        
        //    Exceptions:
        //  throws java.io.FileNotFoundException
        //    Signature:
        //  ()Ljava/util/ArrayList<Lio/legado/app/data/entities/BookChapter;>;
        //    StackMapTable: 00 0F 1A FF 00 50 00 0B 07 00 02 07 00 53 01 07 00 38 07 00 55 01 07 01 00 01 07 00 41 01 01 00 00 03 40 01 FF 00 23 00 0B 07 00 02 07 00 53 01 07 00 38 07 00 55 01 07 01 00 01 07 00 04 01 01 00 00 FF 00 4D 00 0C 07 00 02 07 00 53 01 07 00 38 07 00 55 01 07 01 00 01 07 00 73 07 00 7E 07 01 02 01 00 01 07 00 27 FF 00 10 00 0C 07 00 02 07 00 53 01 07 00 38 07 00 55 01 07 01 00 01 07 00 73 07 00 7E 07 00 73 01 00 01 07 00 27 FF 00 01 00 0C 07 00 02 07 00 53 01 07 00 38 07 00 55 01 07 01 00 01 07 00 73 07 00 7E 07 00 04 01 00 02 07 00 27 07 00 73 FF 00 02 00 09 07 00 02 07 00 53 01 07 00 38 07 00 55 01 07 01 00 01 07 00 04 00 00 FF 00 0D 00 04 07 00 02 07 00 53 01 07 00 38 00 01 07 00 38 47 07 00 38 F8 00 09 FF 00 2F 00 06 07 00 02 07 00 E4 07 00 9B 01 01 07 00 A1 00 00 FE 00 24 07 00 04 01 01 F8 00 50
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  49     254    262    270    Ljava/lang/Throwable;
        //  49     254    270    280    Any
        //  262    270    270    280    Any
        //  270    272    270    280    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final ArrayList<BookChapter> analyze(final Pattern pattern) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_2        
        //     3: ifnonnull       10
        //     6: aconst_null    
        //     7: goto            14
        //    10: aload_2        
        //    11: invokevirtual   java/util/regex/Pattern.pattern:()Ljava/lang/String;
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: astore_2       
        //    18: iconst_0       
        //    19: istore_3       
        //    20: iconst_0       
        //    21: istore          4
        //    23: aload_2        
        //    24: ifnull          36
        //    27: aload_2        
        //    28: invokeinterface java/lang/CharSequence.length:()I
        //    33: ifne            40
        //    36: iconst_1       
        //    37: goto            41
        //    40: iconst_0       
        //    41: ifeq            53
        //    44: aload_0         /* this */
        //    45: lconst_0       
        //    46: lconst_0       
        //    47: iconst_3       
        //    48: aconst_null    
        //    49: invokestatic    io/legado/app/model/localBook/TextFile.analyze$default:(Lio/legado/app/model/localBook/TextFile;JJILjava/lang/Object;)Ljava/util/ArrayList;
        //    52: areturn        
        //    53: aload_1         /* pattern */
        //    54: astore_2       
        //    55: aload_2        
        //    56: ifnonnull       68
        //    59: aload_0         /* this */
        //    60: lconst_0       
        //    61: lconst_0       
        //    62: iconst_3       
        //    63: aconst_null    
        //    64: invokestatic    io/legado/app/model/localBook/TextFile.analyze$default:(Lio/legado/app/model/localBook/TextFile;JJILjava/lang/Object;)Ljava/util/ArrayList;
        //    67: areturn        
        //    68: aload_2        
        //    69: pop            
        //    70: iconst_0       
        //    71: istore_3       
        //    72: new             Ljava/util/ArrayList;
        //    75: dup            
        //    76: invokespecial   java/util/ArrayList.<init>:()V
        //    79: astore_2        /* toc */
        //    80: getstatic       io/legado/app/model/localBook/LocalBook.INSTANCE:Lio/legado/app/model/localBook/LocalBook;
        //    83: aload_0         /* this */
        //    84: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //    87: invokevirtual   io/legado/app/model/localBook/LocalBook.getBookInputStream:(Lio/legado/app/data/entities/Book;)Ljava/io/InputStream;
        //    90: checkcast       Ljava/io/Closeable;
        //    93: astore_3       
        //    94: iconst_0       
        //    95: istore          4
        //    97: iconst_0       
        //    98: istore          5
        //   100: aconst_null    
        //   101: checkcast       Ljava/lang/Throwable;
        //   104: astore          5
        //   106: nop            
        //   107: aload_3        
        //   108: checkcast       Ljava/io/InputStream;
        //   111: astore          bis
        //   113: iconst_0       
        //   114: istore          $i$a$-use-TextFile$analyze$1
        //   116: aconst_null    
        //   117: astore          blockContent
        //   119: lconst_0       
        //   120: lstore          curOffset
        //   122: iconst_0       
        //   123: istore          length
        //   125: aload_0         /* this */
        //   126: getfield        io/legado/app/model/localBook/TextFile.bufferSize:I
        //   129: newarray        B
        //   131: astore          buffer
        //   133: iconst_3       
        //   134: istore          bufferStart
        //   136: aload           bis
        //   138: aload           buffer
        //   140: iconst_0       
        //   141: iconst_3       
        //   142: invokevirtual   java/io/InputStream.read:([BII)I
        //   145: pop            
        //   146: getstatic       io/legado/app/utils/Utf8BomUtils.INSTANCE:Lio/legado/app/utils/Utf8BomUtils;
        //   149: aload           buffer
        //   151: invokevirtual   io/legado/app/utils/Utf8BomUtils.hasBom:([B)Z
        //   154: ifeq            165
        //   157: iconst_0       
        //   158: istore          bufferStart
        //   160: ldc2_w          3
        //   163: lstore          curOffset
        //   165: aload           bis
        //   167: aload           buffer
        //   169: iload           bufferStart
        //   171: aload_0         /* this */
        //   172: getfield        io/legado/app/model/localBook/TextFile.bufferSize:I
        //   175: iload           bufferStart
        //   177: isub           
        //   178: invokevirtual   java/io/InputStream.read:([BII)I
        //   181: istore          14
        //   183: iconst_0       
        //   184: istore          15
        //   186: iconst_0       
        //   187: istore          16
        //   189: iload           14
        //   191: istore          it
        //   193: iconst_0       
        //   194: istore          $i$a$-also-TextFile$analyze$1$1
        //   196: iload           it
        //   198: istore          length
        //   200: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   203: pop            
        //   204: iload           14
        //   206: ifle            1550
        //   209: iload           bufferStart
        //   211: iload           length
        //   213: iadd           
        //   214: istore          end
        //   216: iload           end
        //   218: aload_0         /* this */
        //   219: getfield        io/legado/app/model/localBook/TextFile.bufferSize:I
        //   222: if_icmpne       272
        //   225: iload           bufferStart
        //   227: iload           length
        //   229: iadd           
        //   230: iconst_1       
        //   231: isub           
        //   232: istore          15
        //   234: iconst_0       
        //   235: iload           15
        //   237: if_icmpgt       272
        //   240: iload           15
        //   242: istore          i
        //   244: iinc            15, -1
        //   247: aload           buffer
        //   249: iload           i
        //   251: baload         
        //   252: aload_0         /* this */
        //   253: getfield        io/legado/app/model/localBook/TextFile.blank:B
        //   256: if_icmpne       266
        //   259: iload           i
        //   261: istore          end
        //   263: goto            272
        //   266: iconst_0       
        //   267: iload           15
        //   269: if_icmple       240
        //   272: iconst_0       
        //   273: istore          15
        //   275: aload_0         /* this */
        //   276: getfield        io/legado/app/model/localBook/TextFile.charset:Ljava/nio/charset/Charset;
        //   279: astore          16
        //   281: iconst_0       
        //   282: istore          17
        //   284: new             Ljava/lang/String;
        //   287: dup            
        //   288: aload           buffer
        //   290: iload           15
        //   292: iload           end
        //   294: aload           16
        //   296: invokespecial   java/lang/String.<init>:([BIILjava/nio/charset/Charset;)V
        //   299: astore          blockContent
        //   301: aload           buffer
        //   303: aload           buffer
        //   305: iconst_0       
        //   306: iload           end
        //   308: iload           bufferStart
        //   310: iload           length
        //   312: iadd           
        //   313: invokestatic    kotlin/collections/ArraysKt.copyInto:([B[BIII)[B
        //   316: pop            
        //   317: iload           bufferStart
        //   319: iload           length
        //   321: iadd           
        //   322: iload           end
        //   324: isub           
        //   325: istore          bufferStart
        //   327: iload           end
        //   329: istore          length
        //   331: iconst_0       
        //   332: istore          seekPos
        //   334: aload_1         /* pattern */
        //   335: aload           blockContent
        //   337: checkcast       Ljava/lang/CharSequence;
        //   340: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   343: astore          17
        //   345: aload           17
        //   347: ldc_w           "pattern.matcher(blockContent)"
        //   350: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   353: aload           17
        //   355: astore          matcher
        //   357: aload           matcher
        //   359: invokevirtual   java/util/regex/Matcher.find:()Z
        //   362: ifeq            1509
        //   365: aload           matcher
        //   367: invokevirtual   java/util/regex/Matcher.start:()I
        //   370: istore          chapterStart
        //   372: aload           blockContent
        //   374: astore          19
        //   376: iconst_0       
        //   377: istore          20
        //   379: aload           19
        //   381: iload           seekPos
        //   383: iload           chapterStart
        //   385: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   388: dup            
        //   389: ldc_w           "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   392: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   395: astore          chapterContent
        //   397: aload           chapterContent
        //   399: astore          20
        //   401: aload_0         /* this */
        //   402: getfield        io/legado/app/model/localBook/TextFile.charset:Ljava/nio/charset/Charset;
        //   405: astore          21
        //   407: iconst_0       
        //   408: istore          22
        //   410: aload           20
        //   412: dup            
        //   413: ifnonnull       427
        //   416: new             Ljava/lang/NullPointerException;
        //   419: dup            
        //   420: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //   423: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   426: athrow         
        //   427: aload           21
        //   429: invokevirtual   java/lang/String.getBytes:(Ljava/nio/charset/Charset;)[B
        //   432: dup            
        //   433: ldc_w           "(this as java.lang.String).getBytes(charset)"
        //   436: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   439: arraylength    
        //   440: istore          chapterLength
        //   442: aload_2         /* toc */
        //   443: checkcast       Ljava/util/List;
        //   446: invokestatic    kotlin/collections/CollectionsKt.lastOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   449: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   452: astore          22
        //   454: aload           22
        //   456: ifnonnull       464
        //   459: lload           curOffset
        //   461: goto            486
        //   464: aload           22
        //   466: invokevirtual   io/legado/app/data/entities/BookChapter.getStart:()Ljava/lang/Long;
        //   469: astore          23
        //   471: aload           23
        //   473: ifnonnull       481
        //   476: lload           curOffset
        //   478: goto            486
        //   481: aload           23
        //   483: invokevirtual   java/lang/Long.longValue:()J
        //   486: lstore          lastStart
        //   488: aload_0         /* this */
        //   489: getfield        io/legado/app/model/localBook/TextFile.book:Lio/legado/app/data/entities/Book;
        //   492: invokevirtual   io/legado/app/data/entities/Book.getSplitLongChapter:()Z
        //   495: ifeq            915
        //   498: lload           curOffset
        //   500: iload           chapterLength
        //   502: i2l            
        //   503: ladd           
        //   504: lload           lastStart
        //   506: lsub           
        //   507: aload_0         /* this */
        //   508: getfield        io/legado/app/model/localBook/TextFile.maxLengthWithToc:I
        //   511: i2l            
        //   512: lcmp           
        //   513: ifle            915
        //   516: aload_2         /* toc */
        //   517: checkcast       Ljava/util/List;
        //   520: invokestatic    kotlin/collections/CollectionsKt.lastOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   523: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   526: astore          22
        //   528: aload           22
        //   530: ifnonnull       537
        //   533: aconst_null    
        //   534: goto            572
        //   537: aload           22
        //   539: astore          23
        //   541: iconst_0       
        //   542: istore          26
        //   544: iconst_0       
        //   545: istore          27
        //   547: aload           23
        //   549: astore          it
        //   551: iconst_0       
        //   552: istore          $i$a$-let-TextFile$analyze$1$2
        //   554: aload           it
        //   556: aload           it
        //   558: invokevirtual   io/legado/app/data/entities/BookChapter.getStart:()Ljava/lang/Long;
        //   561: invokevirtual   io/legado/app/data/entities/BookChapter.setEnd:(Ljava/lang/Long;)V
        //   564: nop            
        //   565: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   568: pop            
        //   569: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   572: pop            
        //   573: aload_2         /* toc */
        //   574: checkcast       Ljava/util/List;
        //   577: invokestatic    kotlin/collections/CollectionsKt.lastOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   580: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   583: astore          23
        //   585: aload           23
        //   587: ifnonnull       594
        //   590: aconst_null    
        //   591: goto            599
        //   594: aload           23
        //   596: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   599: astore          lastTitle
        //   601: aload           lastTitle
        //   603: astore          26
        //   605: aload           26
        //   607: ifnonnull       614
        //   610: iconst_0       
        //   611: goto            659
        //   614: aload           26
        //   616: astore          28
        //   618: aload_0         /* this */
        //   619: getfield        io/legado/app/model/localBook/TextFile.charset:Ljava/nio/charset/Charset;
        //   622: astore          29
        //   624: iconst_0       
        //   625: istore          30
        //   627: aload           28
        //   629: aload           29
        //   631: invokevirtual   java/lang/String.getBytes:(Ljava/nio/charset/Charset;)[B
        //   634: dup            
        //   635: ldc_w           "(this as java.lang.String).getBytes(charset)"
        //   638: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   641: astore          27
        //   643: aload           27
        //   645: ifnonnull       652
        //   648: iconst_0       
        //   649: goto            659
        //   652: aload           27
        //   654: arraylength    
        //   655: istore          28
        //   657: iload           28
        //   659: istore          lastTitleLength
        //   661: aload_0         /* this */
        //   662: lload           lastStart
        //   664: iload           lastTitleLength
        //   666: i2l            
        //   667: ladd           
        //   668: lload           curOffset
        //   670: iload           chapterLength
        //   672: i2l            
        //   673: ladd           
        //   674: invokespecial   io/legado/app/model/localBook/TextFile.analyze:(JJ)Ljava/util/ArrayList;
        //   677: astore          chapters
        //   679: aload           lastTitle
        //   681: astore          27
        //   683: aload           27
        //   685: ifnonnull       692
        //   688: aconst_null    
        //   689: goto            832
        //   692: aload           27
        //   694: astore          28
        //   696: iconst_0       
        //   697: istore          29
        //   699: iconst_0       
        //   700: istore          30
        //   702: aload           28
        //   704: astore          it
        //   706: iconst_0       
        //   707: istore          $i$a$-let-TextFile$analyze$1$3
        //   709: aload           chapters
        //   711: checkcast       Ljava/lang/Iterable;
        //   714: astore          $this$forEachIndexed$iv
        //   716: iconst_0       
        //   717: istore          $i$f$forEachIndexed
        //   719: iconst_0       
        //   720: istore          index$iv
        //   722: aload           $this$forEachIndexed$iv
        //   724: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   729: astore          36
        //   731: aload           36
        //   733: invokeinterface java/util/Iterator.hasNext:()Z
        //   738: ifeq            823
        //   741: aload           36
        //   743: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   748: astore          item$iv
        //   750: iload           index$iv
        //   752: iinc            index$iv, 1
        //   755: istore          38
        //   757: iconst_0       
        //   758: istore          39
        //   760: iload           38
        //   762: ifge            768
        //   765: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   768: iload           38
        //   770: aload           item$iv
        //   772: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   775: astore          40
        //   777: istore          index
        //   779: iconst_0       
        //   780: istore          $i$a$-forEachIndexed-TextFile$analyze$1$3$1
        //   782: aload           bookChapter
        //   784: new             Ljava/lang/StringBuilder;
        //   787: dup            
        //   788: invokespecial   java/lang/StringBuilder.<init>:()V
        //   791: aload           lastTitle
        //   793: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   796: bipush          40
        //   798: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   801: iload           index
        //   803: iconst_1       
        //   804: iadd           
        //   805: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   808: bipush          41
        //   810: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   813: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   816: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   819: nop            
        //   820: goto            731
        //   823: nop            
        //   824: nop            
        //   825: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   828: pop            
        //   829: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   832: pop            
        //   833: aload_2         /* toc */
        //   834: aload           chapters
        //   836: checkcast       Ljava/util/Collection;
        //   839: invokevirtual   java/util/ArrayList.addAll:(Ljava/util/Collection;)Z
        //   842: pop            
        //   843: new             Lio/legado/app/data/entities/BookChapter;
        //   846: dup            
        //   847: aconst_null    
        //   848: aconst_null    
        //   849: iconst_0       
        //   850: aconst_null    
        //   851: aconst_null    
        //   852: iconst_0       
        //   853: aconst_null    
        //   854: aconst_null    
        //   855: aconst_null    
        //   856: aconst_null    
        //   857: aconst_null    
        //   858: aconst_null    
        //   859: aconst_null    
        //   860: sipush          8191
        //   863: aconst_null    
        //   864: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   867: astore          curChapter
        //   869: aload           curChapter
        //   871: aload           matcher
        //   873: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //   876: astore          28
        //   878: aload           28
        //   880: ldc_w           "matcher.group()"
        //   883: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   886: aload           28
        //   888: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   891: aload           curChapter
        //   893: lload           curOffset
        //   895: iload           chapterLength
        //   897: i2l            
        //   898: ladd           
        //   899: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   902: invokevirtual   io/legado/app/data/entities/BookChapter.setStart:(Ljava/lang/Long;)V
        //   905: aload_2         /* toc */
        //   906: aload           curChapter
        //   908: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   911: pop            
        //   912: goto            1496
        //   915: iload           seekPos
        //   917: ifne            1225
        //   920: iload           chapterStart
        //   922: ifeq            1225
        //   925: aload_2         /* toc */
        //   926: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //   929: ifeq            1097
        //   932: getstatic       io/legado/app/utils/StringUtils.INSTANCE:Lio/legado/app/utils/StringUtils;
        //   935: aload           chapterContent
        //   937: invokevirtual   io/legado/app/utils/StringUtils.trim:(Ljava/lang/String;)Ljava/lang/String;
        //   940: checkcast       Ljava/lang/CharSequence;
        //   943: astore          22
        //   945: iconst_0       
        //   946: istore          23
        //   948: aload           22
        //   950: invokeinterface java/lang/CharSequence.length:()I
        //   955: ifle            962
        //   958: iconst_1       
        //   959: goto            963
        //   962: iconst_0       
        //   963: ifeq            1028
        //   966: new             Lio/legado/app/data/entities/BookChapter;
        //   969: dup            
        //   970: aconst_null    
        //   971: aconst_null    
        //   972: iconst_0       
        //   973: aconst_null    
        //   974: aconst_null    
        //   975: iconst_0       
        //   976: aconst_null    
        //   977: aconst_null    
        //   978: aconst_null    
        //   979: aconst_null    
        //   980: aconst_null    
        //   981: aconst_null    
        //   982: aconst_null    
        //   983: sipush          8191
        //   986: aconst_null    
        //   987: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   990: astore          qyChapter
        //   992: aload           qyChapter
        //   994: ldc_w           "\u524d\u8a00"
        //   997: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //  1000: aload           qyChapter
        //  1002: lload           curOffset
        //  1004: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1007: invokevirtual   io/legado/app/data/entities/BookChapter.setStart:(Ljava/lang/Long;)V
        //  1010: aload           qyChapter
        //  1012: iload           chapterLength
        //  1014: i2l            
        //  1015: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1018: invokevirtual   io/legado/app/data/entities/BookChapter.setEnd:(Ljava/lang/Long;)V
        //  1021: aload_2         /* toc */
        //  1022: aload           qyChapter
        //  1024: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1027: pop            
        //  1028: new             Lio/legado/app/data/entities/BookChapter;
        //  1031: dup            
        //  1032: aconst_null    
        //  1033: aconst_null    
        //  1034: iconst_0       
        //  1035: aconst_null    
        //  1036: aconst_null    
        //  1037: iconst_0       
        //  1038: aconst_null    
        //  1039: aconst_null    
        //  1040: aconst_null    
        //  1041: aconst_null    
        //  1042: aconst_null    
        //  1043: aconst_null    
        //  1044: aconst_null    
        //  1045: sipush          8191
        //  1048: aconst_null    
        //  1049: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1052: astore          curChapter
        //  1054: aload           curChapter
        //  1056: aload           matcher
        //  1058: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //  1061: astore          23
        //  1063: aload           23
        //  1065: ldc_w           "matcher.group()"
        //  1068: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1071: aload           23
        //  1073: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //  1076: aload           curChapter
        //  1078: iload           chapterLength
        //  1080: i2l            
        //  1081: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1084: invokevirtual   io/legado/app/data/entities/BookChapter.setStart:(Ljava/lang/Long;)V
        //  1087: aload_2         /* toc */
        //  1088: aload           curChapter
        //  1090: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1093: pop            
        //  1094: goto            1496
        //  1097: aload_2         /* toc */
        //  1098: checkcast       Ljava/util/List;
        //  1101: invokestatic    kotlin/collections/CollectionsKt.last:(Ljava/util/List;)Ljava/lang/Object;
        //  1104: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1107: astore          lastChapter
        //  1109: aload           lastChapter
        //  1111: aload           chapterContent
        //  1113: aload           lastChapter
        //  1115: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //  1118: aconst_null    
        //  1119: iconst_2       
        //  1120: aconst_null    
        //  1121: invokestatic    kotlin/text/StringsKt.substringAfter$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
        //  1124: checkcast       Ljava/lang/CharSequence;
        //  1127: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //  1130: invokevirtual   io/legado/app/data/entities/BookChapter.setVolume:(Z)V
        //  1133: aload           lastChapter
        //  1135: aload           lastChapter
        //  1137: invokevirtual   io/legado/app/data/entities/BookChapter.getEnd:()Ljava/lang/Long;
        //  1140: dup            
        //  1141: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNull:(Ljava/lang/Object;)V
        //  1144: invokevirtual   java/lang/Long.longValue:()J
        //  1147: iload           chapterLength
        //  1149: i2l            
        //  1150: ladd           
        //  1151: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1154: invokevirtual   io/legado/app/data/entities/BookChapter.setEnd:(Ljava/lang/Long;)V
        //  1157: new             Lio/legado/app/data/entities/BookChapter;
        //  1160: dup            
        //  1161: aconst_null    
        //  1162: aconst_null    
        //  1163: iconst_0       
        //  1164: aconst_null    
        //  1165: aconst_null    
        //  1166: iconst_0       
        //  1167: aconst_null    
        //  1168: aconst_null    
        //  1169: aconst_null    
        //  1170: aconst_null    
        //  1171: aconst_null    
        //  1172: aconst_null    
        //  1173: aconst_null    
        //  1174: sipush          8191
        //  1177: aconst_null    
        //  1178: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1181: astore          curChapter
        //  1183: aload           curChapter
        //  1185: aload           matcher
        //  1187: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //  1190: astore          26
        //  1192: aload           26
        //  1194: ldc_w           "matcher.group()"
        //  1197: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1200: aload           26
        //  1202: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //  1205: aload           curChapter
        //  1207: aload           lastChapter
        //  1209: invokevirtual   io/legado/app/data/entities/BookChapter.getEnd:()Ljava/lang/Long;
        //  1212: invokevirtual   io/legado/app/data/entities/BookChapter.setStart:(Ljava/lang/Long;)V
        //  1215: aload_2         /* toc */
        //  1216: aload           curChapter
        //  1218: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1221: pop            
        //  1222: goto            1496
        //  1225: aload_2         /* toc */
        //  1226: checkcast       Ljava/util/Collection;
        //  1229: astore          22
        //  1231: iconst_0       
        //  1232: istore          23
        //  1234: aload           22
        //  1236: invokeinterface java/util/Collection.isEmpty:()Z
        //  1241: ifne            1248
        //  1244: iconst_1       
        //  1245: goto            1249
        //  1248: iconst_0       
        //  1249: ifeq            1421
        //  1252: aload_2         /* toc */
        //  1253: checkcast       Ljava/util/List;
        //  1256: invokestatic    kotlin/collections/CollectionsKt.last:(Ljava/util/List;)Ljava/lang/Object;
        //  1259: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1262: astore          lastChapter
        //  1264: aload           lastChapter
        //  1266: aload           chapterContent
        //  1268: aload           lastChapter
        //  1270: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //  1273: aconst_null    
        //  1274: iconst_2       
        //  1275: aconst_null    
        //  1276: invokestatic    kotlin/text/StringsKt.substringAfter$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
        //  1279: checkcast       Ljava/lang/CharSequence;
        //  1282: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //  1285: invokevirtual   io/legado/app/data/entities/BookChapter.setVolume:(Z)V
        //  1288: aload           lastChapter
        //  1290: aload           lastChapter
        //  1292: invokevirtual   io/legado/app/data/entities/BookChapter.getStart:()Ljava/lang/Long;
        //  1295: dup            
        //  1296: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNull:(Ljava/lang/Object;)V
        //  1299: invokevirtual   java/lang/Long.longValue:()J
        //  1302: aload           chapterContent
        //  1304: astore          23
        //  1306: aload_0         /* this */
        //  1307: getfield        io/legado/app/model/localBook/TextFile.charset:Ljava/nio/charset/Charset;
        //  1310: astore          26
        //  1312: iconst_0       
        //  1313: istore          27
        //  1315: aload           23
        //  1317: dup            
        //  1318: ifnonnull       1332
        //  1321: new             Ljava/lang/NullPointerException;
        //  1324: dup            
        //  1325: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //  1328: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //  1331: athrow         
        //  1332: aload           26
        //  1334: invokevirtual   java/lang/String.getBytes:(Ljava/nio/charset/Charset;)[B
        //  1337: dup            
        //  1338: ldc_w           "(this as java.lang.String).getBytes(charset)"
        //  1341: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1344: arraylength    
        //  1345: i2l            
        //  1346: ladd           
        //  1347: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1350: invokevirtual   io/legado/app/data/entities/BookChapter.setEnd:(Ljava/lang/Long;)V
        //  1353: new             Lio/legado/app/data/entities/BookChapter;
        //  1356: dup            
        //  1357: aconst_null    
        //  1358: aconst_null    
        //  1359: iconst_0       
        //  1360: aconst_null    
        //  1361: aconst_null    
        //  1362: iconst_0       
        //  1363: aconst_null    
        //  1364: aconst_null    
        //  1365: aconst_null    
        //  1366: aconst_null    
        //  1367: aconst_null    
        //  1368: aconst_null    
        //  1369: aconst_null    
        //  1370: sipush          8191
        //  1373: aconst_null    
        //  1374: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1377: astore          curChapter
        //  1379: aload           curChapter
        //  1381: aload           matcher
        //  1383: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //  1386: astore          26
        //  1388: aload           26
        //  1390: ldc_w           "matcher.group()"
        //  1393: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1396: aload           26
        //  1398: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //  1401: aload           curChapter
        //  1403: aload           lastChapter
        //  1405: invokevirtual   io/legado/app/data/entities/BookChapter.getEnd:()Ljava/lang/Long;
        //  1408: invokevirtual   io/legado/app/data/entities/BookChapter.setStart:(Ljava/lang/Long;)V
        //  1411: aload_2         /* toc */
        //  1412: aload           curChapter
        //  1414: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1417: pop            
        //  1418: goto            1496
        //  1421: new             Lio/legado/app/data/entities/BookChapter;
        //  1424: dup            
        //  1425: aconst_null    
        //  1426: aconst_null    
        //  1427: iconst_0       
        //  1428: aconst_null    
        //  1429: aconst_null    
        //  1430: iconst_0       
        //  1431: aconst_null    
        //  1432: aconst_null    
        //  1433: aconst_null    
        //  1434: aconst_null    
        //  1435: aconst_null    
        //  1436: aconst_null    
        //  1437: aconst_null    
        //  1438: sipush          8191
        //  1441: aconst_null    
        //  1442: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1445: astore          curChapter
        //  1447: aload           curChapter
        //  1449: aload           matcher
        //  1451: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //  1454: astore          23
        //  1456: aload           23
        //  1458: ldc_w           "matcher.group()"
        //  1461: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1464: aload           23
        //  1466: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //  1469: aload           curChapter
        //  1471: lload           curOffset
        //  1473: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1476: invokevirtual   io/legado/app/data/entities/BookChapter.setStart:(Ljava/lang/Long;)V
        //  1479: aload           curChapter
        //  1481: lload           curOffset
        //  1483: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1486: invokevirtual   io/legado/app/data/entities/BookChapter.setEnd:(Ljava/lang/Long;)V
        //  1489: aload_2         /* toc */
        //  1490: aload           curChapter
        //  1492: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1495: pop            
        //  1496: iload           seekPos
        //  1498: aload           chapterContent
        //  1500: invokevirtual   java/lang/String.length:()I
        //  1503: iadd           
        //  1504: istore          seekPos
        //  1506: goto            357
        //  1509: lload           curOffset
        //  1511: iload           length
        //  1513: i2l            
        //  1514: ladd           
        //  1515: lstore          curOffset
        //  1517: aload_2         /* toc */
        //  1518: checkcast       Ljava/util/List;
        //  1521: invokestatic    kotlin/collections/CollectionsKt.lastOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //  1524: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1527: astore          17
        //  1529: aload           17
        //  1531: ifnonnull       1537
        //  1534: goto            165
        //  1537: aload           17
        //  1539: lload           curOffset
        //  1541: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  1544: invokevirtual   io/legado/app/data/entities/BookChapter.setEnd:(Ljava/lang/Long;)V
        //  1547: goto            165
        //  1550: nop            
        //  1551: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1554: astore          6
        //  1556: aload_3        
        //  1557: aload           5
        //  1559: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //  1562: aload           6
        //  1564: goto            1587
        //  1567: astore          6
        //  1569: aload           6
        //  1571: astore          5
        //  1573: aload           6
        //  1575: athrow         
        //  1576: astore          6
        //  1578: aload_3        
        //  1579: aload           5
        //  1581: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
        //  1584: aload           6
        //  1586: athrow         
        //  1587: pop            
        //  1588: invokestatic    java/lang/System.gc:()V
        //  1591: invokestatic    java/lang/System.runFinalization:()V
        //  1594: aload_2         /* toc */
        //  1595: areturn        
        //    Signature:
        //  (Ljava/util/regex/Pattern;)Ljava/util/ArrayList<Lio/legado/app/data/entities/BookChapter;>;
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  pattern  
        //    StackMapTable: 00 2D FC 00 0A 07 00 7E 43 07 00 73 FF 00 15 00 05 07 00 02 07 00 7E 07 00 41 01 01 00 00 03 40 01 0B FF 00 0E 00 05 07 00 02 07 00 7E 07 00 7E 01 01 00 00 FF 00 60 00 0D 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 00 00 FF 00 4A 00 12 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 01 01 01 00 00 19 05 FF 00 54 00 10 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 00 00 FF 00 45 00 16 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 07 00 73 07 00 73 07 01 02 01 00 01 07 00 73 FF 00 24 00 16 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 B0 00 00 FC 00 10 07 01 47 FF 00 04 00 16 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 B0 00 01 04 FD 00 32 00 04 62 07 00 86 FF 00 15 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 B0 07 00 B0 04 00 00 44 07 00 73 FF 00 0E 00 19 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 73 07 00 B0 04 07 00 73 00 00 FF 00 25 00 1D 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 73 07 00 B0 04 07 00 73 07 01 00 07 00 73 07 01 02 01 00 00 FF 00 06 00 19 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 73 07 00 B0 04 07 00 73 00 01 01 FF 00 20 00 1A 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 73 01 04 07 00 E4 07 00 73 00 00 FF 00 26 00 23 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 73 01 04 07 00 E4 07 00 73 07 00 73 01 01 07 00 73 01 07 00 9B 01 01 07 00 A1 00 00 FE 00 24 07 00 04 01 01 F8 00 36 FF 00 08 00 1A 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 73 01 04 07 00 E4 07 00 73 00 01 07 00 86 FF 00 52 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 B0 00 04 00 00 FF 00 2E 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 41 01 04 00 00 40 01 FF 00 40 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 04 01 04 00 00 FF 00 44 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 B0 00 04 00 00 FB 00 7F FF 00 16 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 01 60 01 04 00 00 40 01 FF 00 52 00 1A 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 B0 07 00 73 04 07 01 02 01 00 03 07 00 B0 04 07 00 73 FF 00 58 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 01 60 01 04 00 00 FF 00 4A 00 18 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 01 07 00 73 01 07 00 73 07 01 02 07 00 04 00 04 00 00 FF 00 0C 00 10 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 07 01 26 00 00 FC 00 1B 07 00 B0 FF 00 0C 00 12 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 55 01 07 00 73 04 01 07 01 00 01 01 01 01 01 01 00 00 FF 00 10 00 06 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 00 01 07 00 38 48 07 00 38 FF 00 0A 00 12 07 00 02 07 00 7E 07 00 E4 07 00 53 01 07 00 38 07 00 86 01 07 00 73 04 01 07 01 00 01 01 01 01 01 01 00 01 07 00 86
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  106    1556   1567   1576   Ljava/lang/Throwable;
        //  106    1556   1576   1587   Any
        //  1567   1576   1576   1587   Any
        //  1576   1578   1576   1587   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:361)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:504)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:547)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final ArrayList<BookChapter> analyze(final long fileStart, final long fileEnd) {
        final ArrayList toc = new ArrayList();
        final Closeable closeable = LocalBook.INSTANCE.getBookInputStream(this.book);
        Throwable t = null;
        try {
            final InputStream bis = (InputStream)closeable;
            final int n = 0;
            int blockPos = 0;
            long curOffset = 0L;
            int chapterPos = 0;
            int length = 0;
            final byte[] buffer = new byte[this.bufferSize];
            int bufferStart = 0;
            bufferStart = 3;
            if (fileStart == 0L) {
                bis.read(buffer, 0, 3);
                if (Utf8BomUtils.INSTANCE.hasBom(buffer)) {
                    bufferStart = 0;
                    curOffset = 3L;
                }
            }
            else {
                bis.skip(fileStart);
                curOffset = fileStart;
                bufferStart = 0;
            }
            while (fileEnd - curOffset - bufferStart > 0L) {
                final int it = bis.read(buffer, bufferStart, (int)Math.min(this.bufferSize - bufferStart, fileEnd - curOffset - bufferStart));
                final int n2 = 0;
                if ((length = it) <= 0) {
                    break;
                }
                ++blockPos;
                int chapterOffset = 0;
                int strLength;
                length = (strLength = length + bufferStart);
                chapterPos = 0;
                while (strLength > 0) {
                    ++chapterPos;
                    if (strLength > this.maxLengthWithNoToc) {
                        int end = length;
                        int j = chapterOffset + this.maxLengthWithNoToc;
                        if (j < length) {
                            do {
                                final int i = j;
                                ++j;
                                if (buffer[i] == this.blank) {
                                    end = i;
                                    break;
                                }
                            } while (j < length);
                        }
                        final BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        chapter.setTitle(new StringBuilder().append('\u7b2c').append(blockPos).append("\u7ae0(").append(chapterPos).append(')').toString());
                        final BookChapter bookChapter = chapter;
                        final BookChapter bookChapter2 = (BookChapter)CollectionsKt.lastOrNull((List)toc);
                        Long value;
                        if (bookChapter2 == null) {
                            value = curOffset;
                        }
                        else {
                            final Long end2 = bookChapter2.getEnd();
                            value = ((end2 == null) ? Long.valueOf(curOffset) : end2);
                        }
                        bookChapter.setStart(value);
                        final BookChapter bookChapter3 = chapter;
                        final Long start = chapter.getStart();
                        Intrinsics.checkNotNull((Object)start);
                        bookChapter3.setEnd(start + end - chapterOffset);
                        toc.add(chapter);
                        strLength -= end - chapterOffset;
                        chapterOffset = end;
                    }
                    else {
                        ArraysKt.copyInto(buffer, buffer, 0, length - strLength, length);
                        length -= strLength;
                        bufferStart = strLength;
                        strLength = 0;
                    }
                }
                curOffset += length;
            }
            if (bufferStart > 100 || toc.isEmpty()) {
                final BookChapter chapter2 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                chapter2.setTitle(new StringBuilder().append('\u7b2c').append(blockPos).append("\u7ae0(").append(chapterPos).append(')').toString());
                final BookChapter bookChapter4 = chapter2;
                final BookChapter bookChapter5 = (BookChapter)CollectionsKt.lastOrNull((List)toc);
                Long value2;
                if (bookChapter5 == null) {
                    value2 = curOffset;
                }
                else {
                    final Long end3 = bookChapter5.getEnd();
                    value2 = ((end3 == null) ? Long.valueOf(curOffset) : end3);
                }
                bookChapter4.setStart(value2);
                final BookChapter bookChapter6 = chapter2;
                final Long start2 = chapter2.getStart();
                Intrinsics.checkNotNull((Object)start2);
                bookChapter6.setEnd(start2 + bufferStart);
                toc.add(chapter2);
            }
            else {
                final BookChapter bookChapter7 = (BookChapter)CollectionsKt.lastOrNull((List)toc);
                if (bookChapter7 != null) {
                    final BookChapter it2 = bookChapter7;
                    final int n3 = 0;
                    final BookChapter bookChapter8 = it2;
                    final Long end4 = it2.getEnd();
                    Intrinsics.checkNotNull((Object)end4);
                    bookChapter8.setEnd(end4 + bufferStart);
                    final Unit instance = Unit.INSTANCE;
                }
            }
        }
        catch (final Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            CloseableKt.closeFinally(closeable, t);
        }
        return toc;
    }
    
    private final Pattern getTocRule(final String content) {
        final List rules = CollectionsKt.reversed((Iterable)this.getTocRules());
        int maxCs = 1;
        Pattern tocPattern = null;
        for (final TxtTocRule tocRule : rules) {
            final Pattern compile = Pattern.compile(tocRule.getRule(), 8);
            Intrinsics.checkNotNullExpressionValue((Object)compile, "java.util.regex.Pattern.compile(this, flags)");
            final Pattern pattern = compile;
            final Matcher matcher = pattern.matcher(content);
            int cs = 0;
            while (matcher.find()) {
                ++cs;
            }
            if (cs >= maxCs) {
                maxCs = cs;
                tocPattern = pattern;
            }
        }
        return tocPattern;
    }
    
    private final List<TxtTocRule> getTocRules() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokevirtual   io/legado/app/help/DefaultData.getTxtTocRules:()Ljava/util/List;
        //     6: checkcast       Ljava/lang/Iterable;
        //     9: astore_1        /* $this$filter$iv */
        //    10: iconst_0       
        //    11: istore_2        /* $i$f$filter */
        //    12: aload_1         /* $this$filter$iv */
        //    13: astore_3       
        //    14: new             Ljava/util/ArrayList;
        //    17: dup            
        //    18: invokespecial   java/util/ArrayList.<init>:()V
        //    21: checkcast       Ljava/util/Collection;
        //    24: astore          destination$iv$iv
        //    26: iconst_0       
        //    27: istore          $i$f$filterTo
        //    29: aload_3         /* $this$filterTo$iv$iv */
        //    30: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    35: astore          6
        //    37: aload           6
        //    39: invokeinterface java/util/Iterator.hasNext:()Z
        //    44: ifeq            87
        //    47: aload           6
        //    49: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    54: astore          element$iv$iv
        //    56: aload           element$iv$iv
        //    58: checkcast       Lio/legado/app/data/entities/TxtTocRule;
        //    61: astore          it
        //    63: iconst_0       
        //    64: istore          $i$a$-filter-TextFile$getTocRules$1
        //    66: aload           it
        //    68: invokevirtual   io/legado/app/data/entities/TxtTocRule.getEnable:()Z
        //    71: ifeq            37
        //    74: aload           destination$iv$iv
        //    76: aload           element$iv$iv
        //    78: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //    83: pop            
        //    84: goto            37
        //    87: aload           destination$iv$iv
        //    89: checkcast       Ljava/util/List;
        //    92: nop            
        //    93: areturn        
        //    Signature:
        //  ()Ljava/util/List<Lio/legado/app/data/entities/TxtTocRule;>;
        //    StackMapTable: 00 02 FF 00 25 00 07 07 00 02 07 00 9B 01 07 00 9B 07 01 60 01 07 00 A1 00 00 31
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0005¡§\u0006\f" }, d2 = { "Lio/legado/app/model/localBook/TextFile$Companion;", "", "()V", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "Lkotlin/collections/ArrayList;", "book", "Lio/legado/app/data/entities/Book;", "getContent", "", "bookChapter", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final ArrayList<BookChapter> getChapterList(@NotNull final Book book) throws FileNotFoundException {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            return new TextFile(book).getChapterList();
        }
        
        @NotNull
        public final String getContent(@NotNull final Book book, @NotNull final BookChapter bookChapter) throws FileNotFoundException {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)bookChapter, "bookChapter");
            final Long end = bookChapter.getEnd();
            Intrinsics.checkNotNull((Object)end);
            final long longValue = end;
            final Long start = bookChapter.getStart();
            Intrinsics.checkNotNull((Object)start);
            final int count = (int)(longValue - start);
            final byte[] buffer = new byte[count];
            final Closeable closeable = LocalBook.INSTANCE.getBookInputStream(book);
            Throwable t = null;
            try {
                final InputStream bis = (InputStream)closeable;
                final int n = 0;
                final InputStream inputStream = bis;
                final Long start2 = bookChapter.getStart();
                Intrinsics.checkNotNull((Object)start2);
                inputStream.skip(start2);
                bis.read(buffer);
            }
            catch (final Throwable t2) {
                t = t2;
                throw t2;
            }
            finally {
                CloseableKt.closeFinally(closeable, t);
            }
            if (book.getCharset() == null) {
                book.setCharset(EncodingDetect.INSTANCE.getEncode(book.getLocalFile()));
            }
            return new Regex("^[\\n\\s]+").replace((CharSequence)StringsKt.substringAfter$default(new String(buffer, book.fileCharset()), bookChapter.getTitle(), (String)null, 2, (Object)null), "\u3000\u3000");
        }
    }
}
