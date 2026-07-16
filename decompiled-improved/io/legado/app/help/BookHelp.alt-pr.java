// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help;

import kotlin.text.RegexOption;
import kotlinx.coroutines.DelayKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.model.DebugLog;
import java.util.Map;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.data.entities.BaseSource;
import java.util.regex.Matcher;
import kotlinx.coroutines.Deferred;
import java.util.Iterator;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlinx.coroutines.CoroutineStart;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.constant.AppPattern;
import kotlin.text.StringsKt;
import java.util.ArrayList;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import io.legado.app.data.entities.BookSource;
import kotlinx.coroutines.CoroutineScope;
import io.legado.app.utils.FileUtils;
import io.legado.app.model.localBook.LocalBook;
import java.nio.charset.Charset;
import kotlin.io.FilesKt;
import java.util.Arrays;
import kotlin.jvm.internal.StringCompanionObject;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.utils.FileExtensionsKt;
import io.legado.app.utils.MD5Utils;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.Nullable;
import kotlin.text.Regex;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0004J\u000e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0004J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\nJ\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\u0017\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0004J\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004J9\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010 J+\u0010!\u001a\u00020\b2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\"J9\u0010#\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010 J\u001e\u0010$\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006%" }, d2 = { "Lio/legado/app/help/BookHelp;", "", "()V", "cacheImageFolderName", "", "downloadImages", "Ljava/util/concurrent/CopyOnWriteArraySet;", "delContent", "", "book", "Lio/legado/app/data/entities/Book;", "bookChapter", "Lio/legado/app/data/entities/BookChapter;", "formatAuthor", "author", "formatBookAuthor", "formatBookName", "name", "formatFolderName", "folderName", "getBookCacheDir", "Ljava/io/File;", "getContent", "getImage", "src", "getImageSuffix", "saveContent", "scope", "Lkotlinx/coroutines/CoroutineScope;", "bookSource", "Lio/legado/app/data/entities/BookSource;", "content", "(Lkotlinx/coroutines/CoroutineScope;Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveImage", "(Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveImages", "saveText", "reader-pro" })
public final class BookHelp
{
    @NotNull
    public static final BookHelp INSTANCE;
    @NotNull
    private static final String cacheImageFolderName = "images";
    @NotNull
    private static final CopyOnWriteArraySet<String> downloadImages;
    
    private BookHelp() {
    }
    
    private final String formatFolderName(final String folderName) {
        return new Regex("[\\\\/:*?\"<>|.]").replace((CharSequence)folderName, "");
    }
    
    @NotNull
    public final String formatAuthor(@Nullable final String author) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_2        
        //     3: ifnonnull       11
        //     6: ldc             ""
        //     8: goto            272
        //    11: aload_2        
        //    12: checkcast       Ljava/lang/CharSequence;
        //    15: astore          4
        //    17: ldc             "\u4f5c\\s*\u8005[\\s:\uff1a]*"
        //    19: astore          5
        //    21: iconst_0       
        //    22: istore          6
        //    24: new             Lkotlin/text/Regex;
        //    27: dup            
        //    28: aload           5
        //    30: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //    33: astore          5
        //    35: ldc             ""
        //    37: astore          6
        //    39: iconst_0       
        //    40: istore          7
        //    42: aload           5
        //    44: aload           4
        //    46: aload           6
        //    48: invokevirtual   kotlin/text/Regex.replace:(Ljava/lang/CharSequence;Ljava/lang/String;)Ljava/lang/String;
        //    51: astore_3       
        //    52: aload_3        
        //    53: ifnonnull       61
        //    56: ldc             ""
        //    58: goto            272
        //    61: aload_3        
        //    62: checkcast       Ljava/lang/CharSequence;
        //    65: astore          5
        //    67: ldc             "\\s+"
        //    69: astore          6
        //    71: iconst_0       
        //    72: istore          7
        //    74: new             Lkotlin/text/Regex;
        //    77: dup            
        //    78: aload           6
        //    80: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //    83: astore          6
        //    85: ldc             " "
        //    87: astore          7
        //    89: iconst_0       
        //    90: istore          8
        //    92: aload           6
        //    94: aload           5
        //    96: aload           7
        //    98: invokevirtual   kotlin/text/Regex.replace:(Ljava/lang/CharSequence;Ljava/lang/String;)Ljava/lang/String;
        //   101: astore          4
        //   103: aload           4
        //   105: ifnonnull       113
        //   108: ldc             ""
        //   110: goto            272
        //   113: aload           4
        //   115: astore          6
        //   117: nop            
        //   118: iconst_0       
        //   119: istore          $i$f$trim
        //   121: aload           $this$trim$iv
        //   123: checkcast       Ljava/lang/CharSequence;
        //   126: astore          $this$trim$iv$iv
        //   128: iconst_0       
        //   129: istore          $i$f$trim
        //   131: iconst_0       
        //   132: istore          startIndex$iv$iv
        //   134: aload           $this$trim$iv$iv
        //   136: invokeinterface java/lang/CharSequence.length:()I
        //   141: iconst_1       
        //   142: isub           
        //   143: istore          endIndex$iv$iv
        //   145: iconst_0       
        //   146: istore          startFound$iv$iv
        //   148: iload           startIndex$iv$iv
        //   150: iload           endIndex$iv$iv
        //   152: if_icmpgt       242
        //   155: iload           startFound$iv$iv
        //   157: ifne            165
        //   160: iload           startIndex$iv$iv
        //   162: goto            167
        //   165: iload           endIndex$iv$iv
        //   167: istore          index$iv$iv
        //   169: aload           $this$trim$iv$iv
        //   171: iload           index$iv$iv
        //   173: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   178: istore          it
        //   180: iconst_0       
        //   181: istore          $i$a$-trim-BookHelp$formatAuthor$1
        //   183: iload           it
        //   185: bipush          32
        //   187: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   190: ifgt            197
        //   193: iconst_1       
        //   194: goto            198
        //   197: iconst_0       
        //   198: istore          match$iv$iv
        //   200: iload           startFound$iv$iv
        //   202: ifne            225
        //   205: iload           match$iv$iv
        //   207: ifne            216
        //   210: iconst_1       
        //   211: istore          startFound$iv$iv
        //   213: goto            239
        //   216: iload           startIndex$iv$iv
        //   218: iconst_1       
        //   219: iadd           
        //   220: istore          startIndex$iv$iv
        //   222: goto            239
        //   225: iload           match$iv$iv
        //   227: ifne            233
        //   230: goto            242
        //   233: iload           endIndex$iv$iv
        //   235: iconst_1       
        //   236: isub           
        //   237: istore          endIndex$iv$iv
        //   239: goto            148
        //   242: aload           $this$trim$iv$iv
        //   244: iload           startIndex$iv$iv
        //   246: iload           endIndex$iv$iv
        //   248: iconst_1       
        //   249: iadd           
        //   250: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   255: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   258: astore          5
        //   260: aload           5
        //   262: ifnonnull       270
        //   265: ldc             ""
        //   267: goto            272
        //   270: aload           5
        //   272: areturn        
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  author  
        //    StackMapTable: 00 0F FC 00 0B 07 00 4D FF 00 31 00 08 07 00 02 07 00 4D 07 00 4D 07 00 4D 07 00 0F 07 00 13 07 00 4D 01 00 00 FF 00 33 00 09 07 00 02 07 00 4D 07 00 4D 07 00 4D 07 00 4D 07 00 0F 07 00 13 07 00 4D 01 00 00 FF 00 22 00 0D 07 00 02 07 00 4D 07 00 4D 07 00 4D 07 00 4D 07 00 0F 07 00 4D 01 07 00 0F 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 1B 00 0D 07 00 02 07 00 4D 07 00 4D 07 00 4D 07 00 4D 07 00 4D 07 00 4D 01 07 00 0F 01 01 01 01 00 00 FF 00 01 00 03 07 00 02 07 00 4D 07 00 4D 00 01 07 00 4D
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
    
    @NotNull
    public final String formatBookName(@NotNull final String name) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "name"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* name */
        //     7: checkcast       Ljava/lang/CharSequence;
        //    10: astore_2       
        //    11: getstatic       io/legado/app/constant/AppPattern.INSTANCE:Lio/legado/app/constant/AppPattern;
        //    14: invokevirtual   io/legado/app/constant/AppPattern.getNameRegex:()Lkotlin/text/Regex;
        //    17: astore_3       
        //    18: ldc             ""
        //    20: astore          4
        //    22: iconst_0       
        //    23: istore          5
        //    25: aload_3        
        //    26: aload_2        
        //    27: aload           4
        //    29: invokevirtual   kotlin/text/Regex.replace:(Ljava/lang/CharSequence;Ljava/lang/String;)Ljava/lang/String;
        //    32: astore_2       
        //    33: nop            
        //    34: iconst_0       
        //    35: istore_3        /* $i$f$trim */
        //    36: aload_2         /* $this$trim$iv */
        //    37: checkcast       Ljava/lang/CharSequence;
        //    40: astore          $this$trim$iv$iv
        //    42: iconst_0       
        //    43: istore          $i$f$trim
        //    45: iconst_0       
        //    46: istore          startIndex$iv$iv
        //    48: aload           $this$trim$iv$iv
        //    50: invokeinterface java/lang/CharSequence.length:()I
        //    55: iconst_1       
        //    56: isub           
        //    57: istore          endIndex$iv$iv
        //    59: iconst_0       
        //    60: istore          startFound$iv$iv
        //    62: iload           startIndex$iv$iv
        //    64: iload           endIndex$iv$iv
        //    66: if_icmpgt       156
        //    69: iload           startFound$iv$iv
        //    71: ifne            79
        //    74: iload           startIndex$iv$iv
        //    76: goto            81
        //    79: iload           endIndex$iv$iv
        //    81: istore          index$iv$iv
        //    83: aload           $this$trim$iv$iv
        //    85: iload           index$iv$iv
        //    87: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    92: istore          it
        //    94: iconst_0       
        //    95: istore          $i$a$-trim-BookHelp$formatBookName$1
        //    97: iload           it
        //    99: bipush          32
        //   101: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   104: ifgt            111
        //   107: iconst_1       
        //   108: goto            112
        //   111: iconst_0       
        //   112: istore          match$iv$iv
        //   114: iload           startFound$iv$iv
        //   116: ifne            139
        //   119: iload           match$iv$iv
        //   121: ifne            130
        //   124: iconst_1       
        //   125: istore          startFound$iv$iv
        //   127: goto            153
        //   130: iload           startIndex$iv$iv
        //   132: iconst_1       
        //   133: iadd           
        //   134: istore          startIndex$iv$iv
        //   136: goto            153
        //   139: iload           match$iv$iv
        //   141: ifne            147
        //   144: goto            156
        //   147: iload           endIndex$iv$iv
        //   149: iconst_1       
        //   150: isub           
        //   151: istore          endIndex$iv$iv
        //   153: goto            62
        //   156: aload           $this$trim$iv$iv
        //   158: iload           startIndex$iv$iv
        //   160: iload           endIndex$iv$iv
        //   162: iconst_1       
        //   163: iadd           
        //   164: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   169: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   172: areturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  name  
        //    StackMapTable: 00 0A FF 00 3E 00 09 07 00 02 07 00 4D 07 00 4D 01 07 00 0F 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02
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
    
    @NotNull
    public final String formatBookAuthor(@NotNull final String author) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "author"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* author */
        //     7: checkcast       Ljava/lang/CharSequence;
        //    10: astore_2       
        //    11: getstatic       io/legado/app/constant/AppPattern.INSTANCE:Lio/legado/app/constant/AppPattern;
        //    14: invokevirtual   io/legado/app/constant/AppPattern.getAuthorRegex:()Lkotlin/text/Regex;
        //    17: astore_3       
        //    18: ldc             ""
        //    20: astore          4
        //    22: iconst_0       
        //    23: istore          5
        //    25: aload_3        
        //    26: aload_2        
        //    27: aload           4
        //    29: invokevirtual   kotlin/text/Regex.replace:(Ljava/lang/CharSequence;Ljava/lang/String;)Ljava/lang/String;
        //    32: astore_2       
        //    33: nop            
        //    34: iconst_0       
        //    35: istore_3        /* $i$f$trim */
        //    36: aload_2         /* $this$trim$iv */
        //    37: checkcast       Ljava/lang/CharSequence;
        //    40: astore          $this$trim$iv$iv
        //    42: iconst_0       
        //    43: istore          $i$f$trim
        //    45: iconst_0       
        //    46: istore          startIndex$iv$iv
        //    48: aload           $this$trim$iv$iv
        //    50: invokeinterface java/lang/CharSequence.length:()I
        //    55: iconst_1       
        //    56: isub           
        //    57: istore          endIndex$iv$iv
        //    59: iconst_0       
        //    60: istore          startFound$iv$iv
        //    62: iload           startIndex$iv$iv
        //    64: iload           endIndex$iv$iv
        //    66: if_icmpgt       156
        //    69: iload           startFound$iv$iv
        //    71: ifne            79
        //    74: iload           startIndex$iv$iv
        //    76: goto            81
        //    79: iload           endIndex$iv$iv
        //    81: istore          index$iv$iv
        //    83: aload           $this$trim$iv$iv
        //    85: iload           index$iv$iv
        //    87: invokeinterface java/lang/CharSequence.charAt:(I)C
        //    92: istore          it
        //    94: iconst_0       
        //    95: istore          $i$a$-trim-BookHelp$formatBookAuthor$1
        //    97: iload           it
        //    99: bipush          32
        //   101: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   104: ifgt            111
        //   107: iconst_1       
        //   108: goto            112
        //   111: iconst_0       
        //   112: istore          match$iv$iv
        //   114: iload           startFound$iv$iv
        //   116: ifne            139
        //   119: iload           match$iv$iv
        //   121: ifne            130
        //   124: iconst_1       
        //   125: istore          startFound$iv$iv
        //   127: goto            153
        //   130: iload           startIndex$iv$iv
        //   132: iconst_1       
        //   133: iadd           
        //   134: istore          startIndex$iv$iv
        //   136: goto            153
        //   139: iload           match$iv$iv
        //   141: ifne            147
        //   144: goto            156
        //   147: iload           endIndex$iv$iv
        //   149: iconst_1       
        //   150: isub           
        //   151: istore          endIndex$iv$iv
        //   153: goto            62
        //   156: aload           $this$trim$iv$iv
        //   158: iload           startIndex$iv$iv
        //   160: iload           endIndex$iv$iv
        //   162: iconst_1       
        //   163: iadd           
        //   164: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   169: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   172: areturn        
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  author  
        //    StackMapTable: 00 0A FF 00 3E 00 09 07 00 02 07 00 4D 07 00 4D 01 07 00 0F 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02
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
    
    @NotNull
    public final File getBookCacheDir(@NotNull final Book book) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        final String md5Encode = MD5Utils.INSTANCE.md5Encode(book.getBookUrl()).toString();
        final String bookDir = book.getBookDir();
        if (bookDir.length() == 0) {
            throw new Exception("bookDir\u4e0d\u80fd\u4e3a\u7a7a");
        }
        final File localCacheDir = FileExtensionsKt.getFile(new File(bookDir), new String[] { md5Encode });
        if (!localCacheDir.exists()) {
            localCacheDir.mkdirs();
        }
        return localCacheDir;
    }
    
    @Nullable
    public final String getContent(@NotNull final Book book, @NotNull final BookChapter bookChapter) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        Intrinsics.checkNotNullParameter((Object)bookChapter, "bookChapter");
        final File bookCacheDir = this.getBookCacheDir(book);
        final String[] array = { null };
        final int n = 0;
        final StringCompanionObject instance = StringCompanionObject.INSTANCE;
        final String s = "%d.txt";
        final Object[] array2 = { bookChapter.getIndex() };
        final String format = s;
        final Object[] original = array2;
        final String format2 = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
        array[n] = format2;
        final File file = FileExtensionsKt.getFile(bookCacheDir, array);
        if (file.exists()) {
            return FilesKt.readText$default(file, (Charset)null, 1, (Object)null);
        }
        if (book.isLocalBook()) {
            final String content = LocalBook.INSTANCE.getContent(book, bookChapter);
            if (content != null && book.isEpub()) {
                this.saveText(book, bookChapter, content);
            }
            return content;
        }
        return null;
    }
    
    public final void delContent(@NotNull final Book book, @NotNull final BookChapter bookChapter) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        Intrinsics.checkNotNullParameter((Object)bookChapter, "bookChapter");
        final FileUtils instance = FileUtils.INSTANCE;
        final File bookCacheDir = this.getBookCacheDir(book);
        final String[] subDirFiles = { null };
        final int n = 0;
        final StringCompanionObject instance2 = StringCompanionObject.INSTANCE;
        final String s = "%d.txt";
        final Object[] array = { bookChapter.getIndex() };
        final String format = s;
        final Object[] original = array;
        final String format2 = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
        subDirFiles[n] = format2;
        instance.createFileIfNotExist(bookCacheDir, subDirFiles).delete();
    }
    
    @Nullable
    public final Object saveContent(@NotNull final CoroutineScope scope, @NotNull final BookSource bookSource, @NotNull final Book book, @NotNull final BookChapter bookChapter, @NotNull final String content, @NotNull final Continuation<? super Unit> $completion) {
        this.saveText(book, bookChapter, content);
        final Object saveImages = this.saveImages(scope, bookSource, book, bookChapter, content, $completion);
        if (saveImages == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return saveImages;
        }
        return Unit.INSTANCE;
    }
    
    public final void saveText(@NotNull final Book book, @NotNull final BookChapter bookChapter, @NotNull final String content) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        Intrinsics.checkNotNullParameter((Object)bookChapter, "bookChapter");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        final FileUtils instance = FileUtils.INSTANCE;
        final File bookCacheDir = this.getBookCacheDir(book);
        final String[] subDirFiles = { null };
        final int n = 0;
        final StringCompanionObject instance2 = StringCompanionObject.INSTANCE;
        final String s = "%d.txt";
        final Object[] array = { bookChapter.getIndex() };
        final String format = s;
        final Object[] original = array;
        final String format2 = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
        subDirFiles[n] = format2;
        FilesKt.writeText$default(instance.createFileIfNotExist(bookCacheDir, subDirFiles), content, (Charset)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object saveImages(@NotNull final CoroutineScope scope, @NotNull final BookSource bookSource, @NotNull final Book book, @NotNull final BookChapter bookChapter, @NotNull final String content, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0055: {
            if ($completion instanceof BookHelp$saveImages.BookHelp$saveImages$1) {
                final BookHelp$saveImages.BookHelp$saveImages$1 bookHelp$saveImages$1 = (BookHelp$saveImages.BookHelp$saveImages$1)$completion;
                if ((bookHelp$saveImages$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookHelp$saveImages.BookHelp$saveImages$1 bookHelp$saveImages$2 = bookHelp$saveImages$1;
                    bookHelp$saveImages$2.label -= Integer.MIN_VALUE;
                    break Label_0055;
                }
            }
            $continuation = (Continuation)new BookHelp$saveImages.BookHelp$saveImages$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookHelp$saveImages.BookHelp$saveImages$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Iterator iterator2 = null;
        switch (((BookHelp$saveImages.BookHelp$saveImages$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final ArrayList awaitList = new ArrayList();
                Iterable $this$forEach$iv = StringsKt.split$default((CharSequence)content, new String[] { "\n" }, false, 0, 6, (Object)null);
                int $i$f$forEach = 0;
                for (final Object element$iv : $this$forEach$iv) {
                    final String it = (String)element$iv;
                    final int n = 0;
                    final Matcher matcher = AppPattern.INSTANCE.getImgPattern().matcher(it);
                    if (matcher.find()) {
                        final String group = matcher.group(1);
                        if (group == null) {
                            continue;
                        }
                        final String src = group;
                        final int n2 = 0;
                        final String mSrc = NetworkUtils.INSTANCE.getAbsoluteURL(bookChapter.getUrl(), src);
                        final Deferred req = BuildersKt.async$default(scope, (CoroutineContext)null, (CoroutineStart)null, (Function2)new BookHelp$saveImages$2$1$req.BookHelp$saveImages$2$1$req$1(bookSource, book, mSrc, (Continuation)null), 3, (Object)null);
                        Boxing.boxBoolean(awaitList.add(req));
                    }
                }
                $this$forEach$iv = awaitList;
                $i$f$forEach = 0;
                iterator2 = $this$forEach$iv.iterator();
                break;
            }
            case 1: {
                final int $i$f$forEach = 0;
                final int n3 = 0;
                iterator2 = (Iterator)((BookHelp$saveImages.BookHelp$saveImages$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        while (true) {
            if (!iterator2.hasNext()) {
                return Unit.INSTANCE;
            }
            final Object element$iv = iterator2.next();
            final Deferred it2 = (Deferred)element$iv;
            final int n3 = 0;
            final Deferred deferred = it2;
            final Continuation continuation = $continuation;
            ((BookHelp$saveImages.BookHelp$saveImages$1)$continuation).L$0 = iterator2;
            ((BookHelp$saveImages.BookHelp$saveImages$1)$continuation).label = 1;
            if (deferred.await(continuation) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
            continue;
        }
    }
    
    @Nullable
    public final Object saveImage(@Nullable BookSource bookSource, @NotNull Book book, @NotNull String src, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0055: {
            if ($completion instanceof BookHelp$saveImage.BookHelp$saveImage$1) {
                final BookHelp$saveImage.BookHelp$saveImage$1 bookHelp$saveImage$1 = (BookHelp$saveImage.BookHelp$saveImage$1)$completion;
                if ((bookHelp$saveImage$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookHelp$saveImage.BookHelp$saveImage$1 bookHelp$saveImage$2 = bookHelp$saveImage$1;
                    bookHelp$saveImage$2.label -= Integer.MIN_VALUE;
                    break Label_0055;
                }
            }
            $continuation = (Continuation)new BookHelp$saveImage.BookHelp$saveImage$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        while (true) {
            while (true) {
                switch (((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        break;
                    }
                    case 1: {
                        src = (String)((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$3;
                        book = (Book)((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$2;
                        bookSource = (BookSource)((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$1;
                        this = (BookHelp)((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break;
                    }
                    case 2: {
                        Label_0307: {
                            break Label_0307;
                            BookHelp.downloadImages.add(src);
                            final AnalyzeUrl analyzeUrl = new AnalyzeUrl(src, null, null, null, null, null, (BaseSource)bookSource, null, null, null, null, 1982, null);
                            try {
                                final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                                final Continuation $completion2 = $continuation;
                                ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$0 = book;
                                ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$1 = src;
                                ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$2 = null;
                                ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$3 = null;
                                ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).label = 2;
                                Object byteArrayAwait;
                                if ((byteArrayAwait = analyzeUrl2.getByteArrayAwait((Continuation<? super byte[]>)$completion2)) == coroutine_SUSPENDED) {
                                    return coroutine_SUSPENDED;
                                }
                                while (true) {
                                    final byte[] it = (byte[])byteArrayAwait;
                                    final int n = 0;
                                    FilesKt.writeBytes(FileUtils.INSTANCE.createFileIfNotExist(BookHelp.INSTANCE.getBookCacheDir(book), "images", MD5Utils.INSTANCE.md5Encode16(src) + '.' + BookHelp.INSTANCE.getImageSuffix(src)), it);
                                    return Unit.INSTANCE;
                                    src = (String)((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$1;
                                    book = (Book)((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    byteArrayAwait = $result;
                                    continue;
                                }
                            }
                            catch (final Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                BookHelp.downloadImages.remove(src);
                            }
                        }
                        return Unit.INSTANCE;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                if (BookHelp.downloadImages.contains(src)) {
                    final long n2 = 100L;
                    final Continuation continuation = $continuation;
                    ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$0 = this;
                    ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$1 = bookSource;
                    ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$2 = book;
                    ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).L$3 = src;
                    ((BookHelp$saveImage.BookHelp$saveImage$1)$continuation).label = 1;
                    if (DelayKt.delay(n2, continuation) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                else {
                    if (this.getImage(book, src).exists()) {
                        return Unit.INSTANCE;
                    }
                    continue;
                }
                break;
            }
            continue;
        }
    }
    
    @NotNull
    public final File getImage(@NotNull final Book book, @NotNull final String src) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        Intrinsics.checkNotNullParameter((Object)src, "src");
        return FileExtensionsKt.getFile(this.getBookCacheDir(book), new String[] { "images", MD5Utils.INSTANCE.md5Encode16(src) + '.' + this.getImageSuffix(src) });
    }
    
    @NotNull
    public final String getImageSuffix(@NotNull final String src) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        String suffix = StringsKt.substringBefore$default(StringsKt.substringAfterLast$default(src, ".", (String)null, 2, (Object)null), ",", (String)null, 2, (Object)null);
        final Regex fileSuffixRegex = new Regex("^[a-z0-9]+$", RegexOption.IGNORE_CASE);
        if (suffix.length() > 5 || !fileSuffixRegex.matches((CharSequence)suffix)) {
            suffix = "jpg";
        }
        return suffix;
    }
    
    static {
        INSTANCE = new BookHelp();
        downloadImages = new CopyOnWriteArraySet<String>();
    }
}
