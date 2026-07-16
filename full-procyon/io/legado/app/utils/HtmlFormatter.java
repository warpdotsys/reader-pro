// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.net.URL;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import java.util.regex.Pattern;
import kotlin.text.Regex;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001a\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000e\u001a\u00020\u0004J\u001c\u0010\u000f\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004?\u0006\u0002\n\u0000¡§\u0006\u0012" }, d2 = { "Lio/legado/app/utils/HtmlFormatter;", "", "()V", "commentRegex", "Lkotlin/text/Regex;", "formatImagePattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "notImgHtmlRegex", "otherHtmlRegex", "wrapHtmlRegex", "format", "", "html", "otherRegex", "formatKeepImg", "redirectUrl", "Ljava/net/URL;", "reader-pro" })
public final class HtmlFormatter
{
    @NotNull
    public static final HtmlFormatter INSTANCE;
    @NotNull
    private static final Regex wrapHtmlRegex;
    @NotNull
    private static final Regex commentRegex;
    @NotNull
    private static final Regex notImgHtmlRegex;
    @NotNull
    private static final Regex otherHtmlRegex;
    private static final Pattern formatImagePattern;
    
    private HtmlFormatter() {
    }
    
    @NotNull
    public final String format(@Nullable final String html, @NotNull final Regex otherRegex) {
        Intrinsics.checkNotNullParameter((Object)otherRegex, "otherRegex");
        if (html == null) {
            return "";
        }
        return new Regex("[\\n\\s]+$").replace((CharSequence)new Regex("^[\\n\\s]+").replace((CharSequence)new Regex("\\s*\\n+\\s*").replace((CharSequence)otherRegex.replace((CharSequence)HtmlFormatter.commentRegex.replace((CharSequence)HtmlFormatter.wrapHtmlRegex.replace((CharSequence)html, "\n"), ""), ""), "\n\u3000\u3000"), "\u3000\u3000"), "");
    }
    
    @NotNull
    public final String formatKeepImg(@Nullable final String html, @Nullable final URL redirectUrl) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3       
        //     2: aload_3        
        //     3: ifnonnull       9
        //     6: ldc             ""
        //     8: areturn        
        //     9: aload_0         /* this */
        //    10: aload_1         /* html */
        //    11: getstatic       io/legado/app/utils/HtmlFormatter.notImgHtmlRegex:Lkotlin/text/Regex;
        //    14: invokevirtual   io/legado/app/utils/HtmlFormatter.format:(Ljava/lang/String;Lkotlin/text/Regex;)Ljava/lang/String;
        //    17: astore_3        /* keepImgHtml */
        //    18: getstatic       io/legado/app/utils/HtmlFormatter.formatImagePattern:Ljava/util/regex/Pattern;
        //    21: aload_3         /* keepImgHtml */
        //    22: checkcast       Ljava/lang/CharSequence;
        //    25: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //    28: astore          matcher
        //    30: iconst_0       
        //    31: istore          appendPos
        //    33: new             Ljava/lang/StringBuffer;
        //    36: dup            
        //    37: invokespecial   java/lang/StringBuffer.<init>:()V
        //    40: astore          sb
        //    42: aload           matcher
        //    44: invokevirtual   java/util/regex/Matcher.find:()Z
        //    47: ifeq            401
        //    50: aconst_null    
        //    51: astore          param
        //    53: ldc             ""
        //    55: astore          param
        //    57: aload           sb
        //    59: checkcast       Ljava/lang/Appendable;
        //    62: iconst_2       
        //    63: anewarray       Ljava/lang/CharSequence;
        //    66: astore          8
        //    68: aload           8
        //    70: iconst_0       
        //    71: aload_3         /* keepImgHtml */
        //    72: astore          9
        //    74: aload           matcher
        //    76: invokevirtual   java/util/regex/Matcher.start:()I
        //    79: istore          10
        //    81: iconst_0       
        //    82: istore          11
        //    84: aload           9
        //    86: dup            
        //    87: ifnonnull       100
        //    90: new             Ljava/lang/NullPointerException;
        //    93: dup            
        //    94: ldc             "null cannot be cast to non-null type java.lang.String"
        //    96: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //    99: athrow         
        //   100: iload           appendPos
        //   102: iload           10
        //   104: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   107: dup            
        //   108: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   110: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   113: checkcast       Ljava/lang/CharSequence;
        //   116: aastore        
        //   117: aload           8
        //   119: iconst_1       
        //   120: new             Ljava/lang/StringBuilder;
        //   123: dup            
        //   124: invokespecial   java/lang/StringBuilder.<init>:()V
        //   127: ldc             "<img src=\""
        //   129: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   132: getstatic       io/legado/app/utils/NetworkUtils.INSTANCE:Lio/legado/app/utils/NetworkUtils;
        //   135: aload_2         /* redirectUrl */
        //   136: aload           matcher
        //   138: iconst_1       
        //   139: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   142: astore          11
        //   144: aload           11
        //   146: ifnonnull       153
        //   149: aconst_null    
        //   150: goto            322
        //   153: aload           11
        //   155: astore          12
        //   157: iconst_0       
        //   158: istore          13
        //   160: iconst_0       
        //   161: istore          14
        //   163: aload           12
        //   165: astore          15
        //   167: astore          27
        //   169: astore          26
        //   171: astore          25
        //   173: istore          24
        //   175: astore          23
        //   177: astore          22
        //   179: iconst_0       
        //   180: istore          $i$a$-let-HtmlFormatter$formatKeepImg$1
        //   182: getstatic       io/legado/app/model/analyzeRule/AnalyzeUrl.Companion:Lio/legado/app/model/analyzeRule/AnalyzeUrl$Companion;
        //   185: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$Companion.getParamPattern:()Ljava/util/regex/Pattern;
        //   188: aload           it
        //   190: checkcast       Ljava/lang/CharSequence;
        //   193: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   196: astore          urlMatcher
        //   198: aload           urlMatcher
        //   200: invokevirtual   java/util/regex/Matcher.find:()Z
        //   203: ifeq            302
        //   206: bipush          44
        //   208: istore          18
        //   210: aload           it
        //   212: astore          19
        //   214: aload           urlMatcher
        //   216: invokevirtual   java/util/regex/Matcher.end:()I
        //   219: istore          20
        //   221: iconst_0       
        //   222: istore          21
        //   224: aload           19
        //   226: iload           20
        //   228: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   231: dup            
        //   232: ldc             "(this as java.lang.String).substring(startIndex)"
        //   234: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   237: astore          19
        //   239: iconst_0       
        //   240: istore          20
        //   242: new             Ljava/lang/StringBuilder;
        //   245: dup            
        //   246: invokespecial   java/lang/StringBuilder.<init>:()V
        //   249: iload           18
        //   251: invokestatic    java/lang/String.valueOf:(C)Ljava/lang/String;
        //   254: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   257: aload           19
        //   259: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   262: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   265: astore          param
        //   267: aload           it
        //   269: astore          18
        //   271: iconst_0       
        //   272: istore          19
        //   274: aload           urlMatcher
        //   276: invokevirtual   java/util/regex/Matcher.start:()I
        //   279: istore          20
        //   281: iconst_0       
        //   282: istore          21
        //   284: aload           18
        //   286: iload           19
        //   288: iload           20
        //   290: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   293: dup            
        //   294: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   296: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   299: goto            304
        //   302: aload           it
        //   304: nop            
        //   305: astore          28
        //   307: aload           22
        //   309: aload           23
        //   311: iload           24
        //   313: aload           25
        //   315: aload           26
        //   317: aload           27
        //   319: aload           28
        //   321: nop            
        //   322: astore          10
        //   324: aload           10
        //   326: ifnonnull       338
        //   329: aload           matcher
        //   331: iconst_2       
        //   332: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   335: goto            340
        //   338: aload           10
        //   340: astore          9
        //   342: aload           9
        //   344: ifnonnull       360
        //   347: aload           matcher
        //   349: iconst_3       
        //   350: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   353: dup            
        //   354: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNull:(Ljava/lang/Object;)V
        //   357: goto            362
        //   360: aload           9
        //   362: invokevirtual   io/legado/app/utils/NetworkUtils.getAbsoluteURL:(Ljava/net/URL;Ljava/lang/String;)Ljava/lang/String;
        //   365: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   368: aload           param
        //   370: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   373: ldc             "\">"
        //   375: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   378: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   381: checkcast       Ljava/lang/CharSequence;
        //   384: aastore        
        //   385: aload           8
        //   387: invokestatic    kotlin/text/StringsKt.append:(Ljava/lang/Appendable;[Ljava/lang/CharSequence;)Ljava/lang/Appendable;
        //   390: pop            
        //   391: aload           matcher
        //   393: invokevirtual   java/util/regex/Matcher.end:()I
        //   396: istore          appendPos
        //   398: goto            42
        //   401: iload           appendPos
        //   403: aload_3         /* keepImgHtml */
        //   404: invokevirtual   java/lang/String.length:()I
        //   407: if_icmpge       457
        //   410: aload           sb
        //   412: aload_3         /* keepImgHtml */
        //   413: astore          7
        //   415: aload_3         /* keepImgHtml */
        //   416: invokevirtual   java/lang/String.length:()I
        //   419: istore          8
        //   421: iconst_0       
        //   422: istore          9
        //   424: aload           7
        //   426: dup            
        //   427: ifnonnull       440
        //   430: new             Ljava/lang/NullPointerException;
        //   433: dup            
        //   434: ldc             "null cannot be cast to non-null type java.lang.String"
        //   436: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   439: athrow         
        //   440: iload           appendPos
        //   442: iload           8
        //   444: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   447: dup            
        //   448: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   450: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   453: invokevirtual   java/lang/StringBuffer.append:(Ljava/lang/String;)Ljava/lang/StringBuffer;
        //   456: pop            
        //   457: aload           sb
        //   459: invokevirtual   java/lang/StringBuffer.toString:()Ljava/lang/String;
        //   462: astore          7
        //   464: aload           7
        //   466: ldc             "sb.toString()"
        //   468: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   471: aload           7
        //   473: areturn        
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  html         
        //  redirectUrl  
        //    StackMapTable: 00 0E FC 00 09 07 00 3A FE 00 20 07 00 56 01 07 00 53 FF 00 39 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 01 01 00 04 07 00 5C 07 00 C4 01 07 00 3A FF 00 34 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 01 07 00 3A 00 06 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 FF 00 94 00 1C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 01 07 00 3A 07 00 3A 01 01 07 00 3A 01 07 00 56 00 00 00 00 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 00 00 41 07 00 3A FF 00 11 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 01 07 00 3A 00 07 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 07 00 3A FF 00 0F 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 07 00 3A 07 00 3A 00 06 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 FF 00 01 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 07 00 3A 07 00 3A 00 07 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 07 00 3A FF 00 13 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 07 00 3A 07 00 3A 00 06 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 FF 00 01 00 0C 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 07 00 C4 07 00 3A 07 00 3A 07 00 3A 00 07 07 00 5C 07 00 C4 01 07 00 70 07 00 79 07 00 C2 07 00 3A FF 00 26 00 07 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 00 00 FF 00 26 00 0A 07 00 02 07 00 3A 07 00 C2 07 00 3A 07 00 56 01 07 00 53 07 00 3A 01 01 00 02 07 00 53 07 00 3A F8 00 10
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static {
        INSTANCE = new HtmlFormatter();
        wrapHtmlRegex = new Regex("</?(?:div|p|br|hr|h\\d|article|dd|dl)[^>]*>");
        commentRegex = new Regex("<!--[^>]*-->");
        notImgHtmlRegex = new Regex("</?(?!img)[a-zA-Z]+(?=[ >])[^<>]*>");
        otherHtmlRegex = new Regex("</?[a-zA-Z]+(?=[ >])[^<>]*>");
        formatImagePattern = Pattern.compile("<img[^>]*src *= *\"([^\"{]*\\{(?:[^{}]|\\{[^}]+\\})+\\})\"[^>]*>|<img[^>]*data-[^=]*= *\"([^\"]*)\"[^>]*>|<img[^>]*src *= *\"([^\"]*)\"[^>]*>", 2);
    }
}
