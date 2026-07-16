// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.collections.MapsKt;
import kotlin.Pair;
import java.util.regex.Matcher;
import io.legado.app.utils.GsonExtensionsKt;
import kotlin.text.Regex;
import kotlin.collections.CollectionsKt;
import java.util.HashMap;
import kotlin.text.StringsKt;
import kotlin.Unit;
import java.io.InputStream;
import com.jayway.jsonpath.DocumentContext;
import java.util.Iterator;
import kotlin.Result$Companion;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.data.entities.BookSource;
import kotlin.ResultKt;
import java.util.Map;
import com.jayway.jsonpath.Predicate;
import io.legado.app.utils.JsonExtensionsKt;
import io.legado.app.utils.StringExtensionsKt;
import java.util.ArrayList;
import java.util.List;
import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u001cB\u0007\b\u0002?\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\f\u0010\rJ*\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\u0006\u0010\u0010\u001a\u00020\u0011\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u0012\u0010\u0013J*\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\u0006\u0010\n\u001a\u00020\u000b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u0012\u0010\rJ\u0014\u0010\u0014\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0019\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u000b2\b\u0010\u001b\u001a\u0004\u0018\u00010\u000bH\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001¡§\u0006\u001d" }, d2 = { "Lio/legado/app/help/SourceAnalyzer;", "", "()V", "headerPattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "jsPattern", "jsonToBookSource", "Lkotlin/Result;", "Lio/legado/app/data/entities/BookSource;", "json", "", "jsonToBookSource-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "jsonToBookSources", "", "inputStream", "Ljava/io/InputStream;", "jsonToBookSources-IoAF18A", "(Ljava/io/InputStream;)Ljava/lang/Object;", "toNewRule", "oldRule", "toNewUrl", "oldUrl", "toNewUrls", "oldUrls", "uaToHeader", "ua", "BookSourceAny", "reader-pro" })
public final class SourceAnalyzer
{
    @NotNull
    public static final SourceAnalyzer INSTANCE;
    private static final Pattern headerPattern;
    private static final Pattern jsPattern;
    
    private SourceAnalyzer() {
    }
    
    @NotNull
    public final Object jsonToBookSources-IoAF18A(@NotNull final String json) {
        Intrinsics.checkNotNullParameter((Object)json, "json");
        Object o;
        try {
            final Result$Companion companion = Result.Companion;
            final int n = 0;
            final List bookSources = new ArrayList();
            if (StringExtensionsKt.isJsonArray(json)) {
                final Object read = JsonExtensionsKt.getJsonPath().parse(json).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(read, "jsonPath.parse(json).read(\"$\")");
                final List items = (List)read;
                for (final Map item : items) {
                    final DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse((Object)item);
                    final SourceAnalyzer instance = SourceAnalyzer.INSTANCE;
                    final String jsonString = jsonItem.jsonString();
                    Intrinsics.checkNotNullExpressionValue((Object)jsonString, "jsonItem.jsonString()");
                    final Object jsonToBookSource-IoAF18A = instance.jsonToBookSource-IoAF18A(jsonString);
                    ResultKt.throwOnFailure(jsonToBookSource-IoAF18A);
                    final BookSource it = (BookSource)jsonToBookSource-IoAF18A;
                    final int n2 = 0;
                    bookSources.add(it);
                }
            }
            else {
                if (!StringExtensionsKt.isJsonObject(json)) {
                    throw new NoStackTraceException("\u683c\u5f0f\u4e0d\u5bf9");
                }
                final Object jsonToBookSource-IoAF18A2 = SourceAnalyzer.INSTANCE.jsonToBookSource-IoAF18A(json);
                ResultKt.throwOnFailure(jsonToBookSource-IoAF18A2);
                final BookSource it2 = (BookSource)jsonToBookSource-IoAF18A2;
                final int n3 = 0;
                bookSources.add(it2);
            }
            o = Result.constructor-impl((Object)bookSources);
        }
        catch (final Throwable t) {
            final Result$Companion companion2 = Result.Companion;
            o = Result.constructor-impl(ResultKt.createFailure(t));
        }
        return o;
    }
    
    @NotNull
    public final Object jsonToBookSources-IoAF18A(@NotNull final InputStream inputStream) {
        Intrinsics.checkNotNullParameter((Object)inputStream, "inputStream");
        Object o2;
        try {
            final Result$Companion companion = Result.Companion;
            final int n = 0;
            final List bookSources = new ArrayList();
            Object o;
            try {
                final Result$Companion companion2 = Result.Companion;
                final int n2 = 0;
                final Object read = JsonExtensionsKt.getJsonPath().parse(inputStream).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(read, "jsonPath.parse(inputStream).read(\"$\")");
                final List items = (List)read;
                for (final Map item : items) {
                    final DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse((Object)item);
                    final SourceAnalyzer instance = SourceAnalyzer.INSTANCE;
                    final String jsonString = jsonItem.jsonString();
                    Intrinsics.checkNotNullExpressionValue((Object)jsonString, "jsonItem.jsonString()");
                    final Object jsonToBookSource-IoAF18A = instance.jsonToBookSource-IoAF18A(jsonString);
                    ResultKt.throwOnFailure(jsonToBookSource-IoAF18A);
                    final BookSource it = (BookSource)jsonToBookSource-IoAF18A;
                    final int n3 = 0;
                    bookSources.add(it);
                }
                o = Result.constructor-impl((Object)Unit.INSTANCE);
            }
            catch (final Throwable t) {
                final Result$Companion companion3 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(o);
            if (exceptionOrNull-impl != null) {
                final Throwable it2 = exceptionOrNull-impl;
                final int n4 = 0;
                final Object read2 = JsonExtensionsKt.getJsonPath().parse(inputStream).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(read2, "jsonPath.parse(inputStream).read(\"$\")");
                final Map item2 = (Map)read2;
                final DocumentContext jsonItem2 = JsonExtensionsKt.getJsonPath().parse((Object)item2);
                final SourceAnalyzer instance2 = SourceAnalyzer.INSTANCE;
                final String jsonString2 = jsonItem2.jsonString();
                Intrinsics.checkNotNullExpressionValue((Object)jsonString2, "jsonItem.jsonString()");
                final Object jsonToBookSource-IoAF18A2 = instance2.jsonToBookSource-IoAF18A(jsonString2);
                ResultKt.throwOnFailure(jsonToBookSource-IoAF18A2);
                final BookSource it3 = (BookSource)jsonToBookSource-IoAF18A2;
                final int n5 = 0;
                bookSources.add(it3);
            }
            o2 = Result.constructor-impl((Object)bookSources);
        }
        catch (final Throwable t2) {
            final Result$Companion companion4 = Result.Companion;
            o2 = Result.constructor-impl(ResultKt.createFailure(t2));
        }
        return o2;
    }
    
    @NotNull
    public final Object jsonToBookSource-IoAF18A(@NotNull final String json) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "json"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: new             Lio/legado/app/data/entities/BookSource;
        //     9: dup            
        //    10: aconst_null    
        //    11: aconst_null    
        //    12: aconst_null    
        //    13: iconst_0       
        //    14: aconst_null    
        //    15: iconst_0       
        //    16: iconst_0       
        //    17: iconst_0       
        //    18: aconst_null    
        //    19: aconst_null    
        //    20: aconst_null    
        //    21: aconst_null    
        //    22: aconst_null    
        //    23: aconst_null    
        //    24: aconst_null    
        //    25: aconst_null    
        //    26: lconst_0       
        //    27: lconst_0       
        //    28: iconst_0       
        //    29: aconst_null    
        //    30: aconst_null    
        //    31: aconst_null    
        //    32: aconst_null    
        //    33: aconst_null    
        //    34: aconst_null    
        //    35: aconst_null    
        //    36: ldc             67108863
        //    38: aconst_null    
        //    39: invokespecial   io/legado/app/data/entities/BookSource.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZLjava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Lio/legado/app/data/entities/rule/ExploreRule;Ljava/lang/String;Lio/legado/app/data/entities/rule/SearchRule;Lio/legado/app/data/entities/rule/BookInfoRule;Lio/legado/app/data/entities/rule/TocRule;Lio/legado/app/data/entities/rule/ContentRule;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //    42: astore_2        /* source */
        //    43: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //    46: astore          4
        //    48: aload_1         /* json */
        //    49: astore          5
        //    51: iconst_0       
        //    52: istore          6
        //    54: aload           5
        //    56: checkcast       Ljava/lang/CharSequence;
        //    59: invokestatic    kotlin/text/StringsKt.trim:(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;
        //    62: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    65: astore          5
        //    67: nop            
        //    68: iconst_0       
        //    69: istore          $i$f$fromJsonObject
        //    71: iconst_0       
        //    72: istore          7
        //    74: nop            
        //    75: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //    78: astore          8
        //    80: iconst_0       
        //    81: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //    83: aload           $this$fromJsonObject$iv
        //    85: aload           json$iv
        //    87: iconst_0       
        //    88: istore          $i$f$genericType
        //    90: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource-IoAF18A$$inlined$fromJsonObject$1;
        //    93: dup            
        //    94: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource-IoAF18A$$inlined$fromJsonObject$1.<init>:()V
        //    97: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource-IoAF18A$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
        //   100: astore          11
        //   102: aload           11
        //   104: ldc             "object : TypeToken<T>() {}.type"
        //   106: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   109: aload           11
        //   111: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   114: dup            
        //   115: instanceof      Lio/legado/app/help/SourceAnalyzer$BookSourceAny;
        //   118: ifne            123
        //   121: pop            
        //   122: aconst_null    
        //   123: checkcast       Lio/legado/app/help/SourceAnalyzer$BookSourceAny;
        //   126: astore          null
        //   128: iconst_0       
        //   129: istore          10
        //   131: aload           9
        //   133: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   136: astore          8
        //   138: goto            161
        //   141: astore          9
        //   143: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   146: astore          10
        //   148: iconst_0       
        //   149: istore          11
        //   151: aload           9
        //   153: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   156: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   159: astore          8
        //   161: aload           8
        //   163: nop            
        //   164: astore          null
        //   166: iconst_0       
        //   167: istore          5
        //   169: iconst_0       
        //   170: istore          6
        //   172: aload           4
        //   174: invokestatic    kotlin/Result.exceptionOrNull-impl:(Ljava/lang/Object;)Ljava/lang/Throwable;
        //   177: dup            
        //   178: ifnull          230
        //   181: astore          6
        //   183: iconst_0       
        //   184: istore          7
        //   186: iconst_0       
        //   187: istore          8
        //   189: aload           6
        //   191: astore          9
        //   193: iconst_0       
        //   194: istore          10
        //   196: aload           9
        //   198: astore          it
        //   200: iconst_0       
        //   201: istore          $i$a$-onFailure-SourceAnalyzer$jsonToBookSource$sourceAny$1
        //   203: getstatic       io/legado/app/model/Debug.INSTANCE:Lio/legado/app/model/Debug;
        //   206: checkcast       Lio/legado/app/model/DebugLog;
        //   209: ldc             "\u8f6c\u5316\u4e66\u6e90\u51fa\u9519"
        //   211: aload           it
        //   213: invokevirtual   java/lang/Throwable.getLocalizedMessage:()Ljava/lang/String;
        //   216: iconst_0       
        //   217: iconst_4       
        //   218: aconst_null    
        //   219: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   222: nop            
        //   223: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   226: pop            
        //   227: goto            231
        //   230: pop            
        //   231: aload           4
        //   233: astore          4
        //   235: iconst_0       
        //   236: istore          5
        //   238: aload           4
        //   240: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //   243: ifeq            250
        //   246: aconst_null    
        //   247: goto            252
        //   250: aload           4
        //   252: checkcast       Lio/legado/app/help/SourceAnalyzer$BookSourceAny;
        //   255: astore_3        /* sourceAny */
        //   256: iconst_0       
        //   257: istore          4
        //   259: nop            
        //   260: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   263: astore          5
        //   265: iconst_0       
        //   266: istore          $i$a$-runCatching-SourceAnalyzer$jsonToBookSource$1
        //   268: aload_3         /* sourceAny */
        //   269: astore          7
        //   271: aload           7
        //   273: ifnonnull       280
        //   276: aconst_null    
        //   277: goto            285
        //   280: aload           7
        //   282: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleToc:()Ljava/lang/Object;
        //   285: ifnonnull       1718
        //   288: aload_2         /* source */
        //   289: astore          7
        //   291: iconst_0       
        //   292: istore          8
        //   294: iconst_0       
        //   295: istore          9
        //   297: aload           7
        //   299: astore          $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   301: iconst_0       
        //   302: istore          $i$a$-apply-SourceAnalyzer$jsonToBookSource$1$1
        //   304: invokestatic    io/legado/app/utils/JsonExtensionsKt.getJsonPath:()Lcom/jayway/jsonpath/ParseContext;
        //   307: aload_1         /* json */
        //   308: astore          12
        //   310: iconst_0       
        //   311: istore          13
        //   313: aload           12
        //   315: checkcast       Ljava/lang/CharSequence;
        //   318: invokestatic    kotlin/text/StringsKt.trim:(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;
        //   321: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   324: invokeinterface com/jayway/jsonpath/ParseContext.parse:(Ljava/lang/String;)Lcom/jayway/jsonpath/DocumentContext;
        //   329: astore          jsonItem
        //   331: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   333: aload           jsonItem
        //   335: ldc             "jsonItem"
        //   337: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   340: aload           jsonItem
        //   342: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   345: ldc             "bookSourceUrl"
        //   347: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   350: astore          12
        //   352: aload           12
        //   354: ifnonnull       367
        //   357: new             Lio/legado/app/exception/NoStackTraceException;
        //   360: dup            
        //   361: ldc             "\u683c\u5f0f\u4e0d\u5bf9"
        //   363: invokespecial   io/legado/app/exception/NoStackTraceException.<init>:(Ljava/lang/String;)V
        //   366: athrow         
        //   367: aload           12
        //   369: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceUrl:(Ljava/lang/String;)V
        //   372: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   374: aload           jsonItem
        //   376: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   379: ldc             "bookSourceName"
        //   381: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   384: astore          12
        //   386: aload           12
        //   388: ifnonnull       396
        //   391: ldc             ""
        //   393: goto            398
        //   396: aload           12
        //   398: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceName:(Ljava/lang/String;)V
        //   401: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   403: aload           jsonItem
        //   405: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   408: ldc             "bookSourceGroup"
        //   410: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   413: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceGroup:(Ljava/lang/String;)V
        //   416: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   418: aload           jsonItem
        //   420: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   423: ldc_w           "bookSourceComment"
        //   426: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   429: astore          12
        //   431: aload           12
        //   433: ifnonnull       441
        //   436: ldc             ""
        //   438: goto            443
        //   441: aload           12
        //   443: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceComment:(Ljava/lang/String;)V
        //   446: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   448: aload           jsonItem
        //   450: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   453: ldc_w           "ruleBookUrlPattern"
        //   456: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   459: invokevirtual   io/legado/app/data/entities/BookSource.setBookUrlPattern:(Ljava/lang/String;)V
        //   462: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   464: aload           jsonItem
        //   466: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   469: ldc_w           "serialNumber"
        //   472: invokestatic    io/legado/app/utils/JsonExtensionsKt.readInt:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/Integer;
        //   475: astore          12
        //   477: aload           12
        //   479: ifnonnull       486
        //   482: iconst_0       
        //   483: goto            491
        //   486: aload           12
        //   488: invokevirtual   java/lang/Integer.intValue:()I
        //   491: invokevirtual   io/legado/app/data/entities/BookSource.setCustomOrder:(I)V
        //   494: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   496: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   499: aload           jsonItem
        //   501: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   504: ldc_w           "httpUserAgent"
        //   507: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   510: invokespecial   io/legado/app/help/SourceAnalyzer.uaToHeader:(Ljava/lang/String;)Ljava/lang/String;
        //   513: invokevirtual   io/legado/app/data/entities/BookSource.setHeader:(Ljava/lang/String;)V
        //   516: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   518: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   521: aload           jsonItem
        //   523: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   526: ldc_w           "ruleSearchUrl"
        //   529: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   532: invokespecial   io/legado/app/help/SourceAnalyzer.toNewUrl:(Ljava/lang/String;)Ljava/lang/String;
        //   535: invokevirtual   io/legado/app/data/entities/BookSource.setSearchUrl:(Ljava/lang/String;)V
        //   538: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   540: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   543: aload           jsonItem
        //   545: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   548: ldc_w           "ruleFindUrl"
        //   551: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   554: invokespecial   io/legado/app/help/SourceAnalyzer.toNewUrls:(Ljava/lang/String;)Ljava/lang/String;
        //   557: invokevirtual   io/legado/app/data/entities/BookSource.setExploreUrl:(Ljava/lang/String;)V
        //   560: aload           jsonItem
        //   562: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   565: ldc_w           "bookSourceType"
        //   568: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   571: astore          sourceType
        //   573: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   575: aload           sourceType
        //   577: astore          13
        //   579: aload           13
        //   581: ifnull          834
        //   584: aload           13
        //   586: invokevirtual   java/lang/String.hashCode:()I
        //   589: lookupswitch {
        //               49: 672
        //               50: 700
        //               51: 742
        //          2157948: 770
        //          3143036: 728
        //          62628790: 784
        //          69775675: 714
        //          93166550: 756
        //          100313435: 686
        //          default: 834
        //        }
        //   672: aload           13
        //   674: ldc_w           "1"
        //   677: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   680: ifne            806
        //   683: goto            834
        //   686: aload           13
        //   688: ldc_w           "image"
        //   691: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   694: ifne            814
        //   697: goto            834
        //   700: aload           13
        //   702: ldc_w           "2"
        //   705: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   708: ifne            818
        //   711: goto            834
        //   714: aload           13
        //   716: ldc_w           "IMAGE"
        //   719: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   722: ifne            810
        //   725: goto            834
        //   728: aload           13
        //   730: ldc_w           "file"
        //   733: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   736: ifne            826
        //   739: goto            834
        //   742: aload           13
        //   744: ldc_w           "3"
        //   747: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   750: ifne            830
        //   753: goto            834
        //   756: aload           13
        //   758: ldc_w           "audio"
        //   761: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   764: ifne            802
        //   767: goto            834
        //   770: aload           13
        //   772: ldc_w           "FILE"
        //   775: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   778: ifne            822
        //   781: goto            834
        //   784: aload           13
        //   786: ldc_w           "AUDIO"
        //   789: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   792: ifne            798
        //   795: goto            834
        //   798: iconst_1       
        //   799: goto            835
        //   802: iconst_1       
        //   803: goto            835
        //   806: iconst_1       
        //   807: goto            835
        //   810: iconst_2       
        //   811: goto            835
        //   814: iconst_2       
        //   815: goto            835
        //   818: iconst_2       
        //   819: goto            835
        //   822: iconst_3       
        //   823: goto            835
        //   826: iconst_3       
        //   827: goto            835
        //   830: iconst_3       
        //   831: goto            835
        //   834: iconst_0       
        //   835: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceType:(I)V
        //   838: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   840: aload           jsonItem
        //   842: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   845: ldc_w           "enable"
        //   848: invokestatic    io/legado/app/utils/JsonExtensionsKt.readBool:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/Boolean;
        //   851: astore          13
        //   853: aload           13
        //   855: ifnonnull       862
        //   858: iconst_1       
        //   859: goto            867
        //   862: aload           13
        //   864: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   867: invokevirtual   io/legado/app/data/entities/BookSource.setEnabled:(Z)V
        //   870: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   872: invokevirtual   io/legado/app/data/entities/BookSource.getExploreUrl:()Ljava/lang/String;
        //   875: checkcast       Ljava/lang/CharSequence;
        //   878: astore          13
        //   880: iconst_0       
        //   881: istore          15
        //   883: iconst_0       
        //   884: istore          16
        //   886: aload           13
        //   888: ifnull          899
        //   891: aload           13
        //   893: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   896: ifeq            903
        //   899: iconst_1       
        //   900: goto            904
        //   903: iconst_0       
        //   904: ifeq            913
        //   907: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   909: iconst_0       
        //   910: invokevirtual   io/legado/app/data/entities/BookSource.setEnabledExplore:(Z)V
        //   913: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //   915: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   918: aload           jsonItem
        //   920: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   923: ldc_w           "ruleSearchList"
        //   926: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   929: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //   932: astore          13
        //   934: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   937: aload           jsonItem
        //   939: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   942: ldc_w           "ruleSearchName"
        //   945: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   948: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //   951: astore          15
        //   953: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   956: aload           jsonItem
        //   958: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   961: ldc_w           "ruleSearchAuthor"
        //   964: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   967: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //   970: astore          16
        //   972: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   975: aload           jsonItem
        //   977: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   980: ldc_w           "ruleSearchIntroduce"
        //   983: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //   986: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //   989: astore          17
        //   991: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   994: aload           jsonItem
        //   996: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //   999: ldc_w           "ruleSearchKind"
        //  1002: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1005: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1008: astore          18
        //  1010: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1013: aload           jsonItem
        //  1015: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1018: ldc_w           "ruleSearchNoteUrl"
        //  1021: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1024: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1027: astore          19
        //  1029: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1032: aload           jsonItem
        //  1034: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1037: ldc_w           "ruleSearchCoverUrl"
        //  1040: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1043: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1046: astore          20
        //  1048: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1051: aload           jsonItem
        //  1053: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1056: ldc_w           "ruleSearchLastChapter"
        //  1059: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1062: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1065: astore          21
        //  1067: new             Lio/legado/app/data/entities/rule/SearchRule;
        //  1070: dup            
        //  1071: aload           13
        //  1073: aload           15
        //  1075: aload           16
        //  1077: aload           17
        //  1079: aload           18
        //  1081: aload           21
        //  1083: aconst_null    
        //  1084: aload           19
        //  1086: aload           20
        //  1088: aconst_null    
        //  1089: sipush          576
        //  1092: aconst_null    
        //  1093: invokespecial   io/legado/app/data/entities/rule/SearchRule.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1096: invokevirtual   io/legado/app/data/entities/BookSource.setRuleSearch:(Lio/legado/app/data/entities/rule/SearchRule;)V
        //  1099: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //  1101: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1104: aload           jsonItem
        //  1106: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1109: ldc_w           "ruleFindList"
        //  1112: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1115: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1118: astore          13
        //  1120: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1123: aload           jsonItem
        //  1125: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1128: ldc_w           "ruleFindName"
        //  1131: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1134: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1137: astore          15
        //  1139: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1142: aload           jsonItem
        //  1144: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1147: ldc_w           "ruleFindAuthor"
        //  1150: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1153: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1156: astore          16
        //  1158: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1161: aload           jsonItem
        //  1163: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1166: ldc_w           "ruleFindIntroduce"
        //  1169: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1172: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1175: astore          17
        //  1177: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1180: aload           jsonItem
        //  1182: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1185: ldc_w           "ruleFindKind"
        //  1188: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1191: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1194: astore          18
        //  1196: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1199: aload           jsonItem
        //  1201: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1204: ldc_w           "ruleFindNoteUrl"
        //  1207: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1210: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1213: astore          19
        //  1215: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1218: aload           jsonItem
        //  1220: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1223: ldc_w           "ruleFindCoverUrl"
        //  1226: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1229: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1232: astore          20
        //  1234: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1237: aload           jsonItem
        //  1239: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1242: ldc_w           "ruleFindLastChapter"
        //  1245: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1248: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1251: astore          21
        //  1253: new             Lio/legado/app/data/entities/rule/ExploreRule;
        //  1256: dup            
        //  1257: aload           13
        //  1259: aload           15
        //  1261: aload           16
        //  1263: aload           17
        //  1265: aload           18
        //  1267: aload           21
        //  1269: aconst_null    
        //  1270: aload           19
        //  1272: aload           20
        //  1274: aconst_null    
        //  1275: sipush          576
        //  1278: aconst_null    
        //  1279: invokespecial   io/legado/app/data/entities/rule/ExploreRule.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1282: invokevirtual   io/legado/app/data/entities/BookSource.setRuleExplore:(Lio/legado/app/data/entities/rule/ExploreRule;)V
        //  1285: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //  1287: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1290: aload           jsonItem
        //  1292: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1295: ldc_w           "ruleBookInfoInit"
        //  1298: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1301: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1304: astore          13
        //  1306: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1309: aload           jsonItem
        //  1311: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1314: ldc_w           "ruleBookName"
        //  1317: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1320: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1323: astore          15
        //  1325: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1328: aload           jsonItem
        //  1330: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1333: ldc_w           "ruleBookAuthor"
        //  1336: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1339: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1342: astore          16
        //  1344: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1347: aload           jsonItem
        //  1349: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1352: ldc_w           "ruleIntroduce"
        //  1355: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1358: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1361: astore          17
        //  1363: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1366: aload           jsonItem
        //  1368: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1371: ldc_w           "ruleBookKind"
        //  1374: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1377: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1380: astore          18
        //  1382: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1385: aload           jsonItem
        //  1387: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1390: ldc_w           "ruleCoverUrl"
        //  1393: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1396: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1399: astore          19
        //  1401: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1404: aload           jsonItem
        //  1406: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1409: ldc_w           "ruleBookLastChapter"
        //  1412: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1415: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1418: astore          20
        //  1420: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1423: aload           jsonItem
        //  1425: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1428: ldc_w           "ruleChapterUrl"
        //  1431: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1434: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1437: astore          21
        //  1439: new             Lio/legado/app/data/entities/rule/BookInfoRule;
        //  1442: dup            
        //  1443: aload           13
        //  1445: aload           15
        //  1447: aload           16
        //  1449: aload           17
        //  1451: aload           18
        //  1453: aload           20
        //  1455: aconst_null    
        //  1456: aload           19
        //  1458: aload           21
        //  1460: aconst_null    
        //  1461: aconst_null    
        //  1462: sipush          1600
        //  1465: aconst_null    
        //  1466: invokespecial   io/legado/app/data/entities/rule/BookInfoRule.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1469: invokevirtual   io/legado/app/data/entities/BookSource.setRuleBookInfo:(Lio/legado/app/data/entities/rule/BookInfoRule;)V
        //  1472: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //  1474: new             Lio/legado/app/data/entities/rule/TocRule;
        //  1477: dup            
        //  1478: aconst_null    
        //  1479: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1482: aload           jsonItem
        //  1484: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1487: ldc_w           "ruleChapterList"
        //  1490: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1493: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1496: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1499: aload           jsonItem
        //  1501: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1504: ldc_w           "ruleChapterName"
        //  1507: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1510: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1513: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1516: aload           jsonItem
        //  1518: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1521: ldc_w           "ruleContentUrl"
        //  1524: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1527: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1530: aconst_null    
        //  1531: aconst_null    
        //  1532: aconst_null    
        //  1533: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1536: aload           jsonItem
        //  1538: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1541: ldc_w           "ruleChapterUrlNext"
        //  1544: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1547: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1550: bipush          113
        //  1552: aconst_null    
        //  1553: invokespecial   io/legado/app/data/entities/rule/TocRule.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1556: invokevirtual   io/legado/app/data/entities/BookSource.setRuleToc:(Lio/legado/app/data/entities/rule/TocRule;)V
        //  1559: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1562: aload           jsonItem
        //  1564: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1567: ldc_w           "ruleBookContent"
        //  1570: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1573: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1576: astore          15
        //  1578: aload           15
        //  1580: ifnonnull       1588
        //  1583: ldc             ""
        //  1585: goto            1590
        //  1588: aload           15
        //  1590: astore          content
        //  1592: aload           content
        //  1594: ldc             "$"
        //  1596: iconst_0       
        //  1597: iconst_2       
        //  1598: aconst_null    
        //  1599: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //  1602: ifeq            1645
        //  1605: aload           content
        //  1607: ldc_w           "$."
        //  1610: iconst_0       
        //  1611: iconst_2       
        //  1612: aconst_null    
        //  1613: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //  1616: ifne            1645
        //  1619: aload           content
        //  1621: astore          15
        //  1623: iconst_1       
        //  1624: istore          16
        //  1626: iconst_0       
        //  1627: istore          17
        //  1629: aload           15
        //  1631: iload           16
        //  1633: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //  1636: dup            
        //  1637: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //  1640: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1643: astore          content
        //  1645: aload           $this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9
        //  1647: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1650: aload           jsonItem
        //  1652: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1655: ldc_w           "ruleBookContentReplace"
        //  1658: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1661: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1664: astore          15
        //  1666: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //  1669: aload           jsonItem
        //  1671: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1674: ldc_w           "ruleContentUrlNext"
        //  1677: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1680: invokespecial   io/legado/app/help/SourceAnalyzer.toNewRule:(Ljava/lang/String;)Ljava/lang/String;
        //  1683: astore          16
        //  1685: new             Lio/legado/app/data/entities/rule/ContentRule;
        //  1688: dup            
        //  1689: aload           content
        //  1691: aload           16
        //  1693: aconst_null    
        //  1694: aconst_null    
        //  1695: aload           15
        //  1697: aconst_null    
        //  1698: bipush          44
        //  1700: aconst_null    
        //  1701: invokespecial   io/legado/app/data/entities/rule/ContentRule.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //  1704: invokevirtual   io/legado/app/data/entities/BookSource.setRuleContent:(Lio/legado/app/data/entities/rule/ContentRule;)V
        //  1707: nop            
        //  1708: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1711: pop            
        //  1712: aload           7
        //  1714: pop            
        //  1715: goto            3430
        //  1718: aload_2         /* source */
        //  1719: aload_3         /* sourceAny */
        //  1720: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getBookSourceUrl:()Ljava/lang/String;
        //  1723: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceUrl:(Ljava/lang/String;)V
        //  1726: aload_2         /* source */
        //  1727: aload_3         /* sourceAny */
        //  1728: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getBookSourceName:()Ljava/lang/String;
        //  1731: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceName:(Ljava/lang/String;)V
        //  1734: aload_2         /* source */
        //  1735: aload_3         /* sourceAny */
        //  1736: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getBookSourceGroup:()Ljava/lang/String;
        //  1739: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceGroup:(Ljava/lang/String;)V
        //  1742: aload_2         /* source */
        //  1743: aload_3         /* sourceAny */
        //  1744: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getBookSourceType:()I
        //  1747: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceType:(I)V
        //  1750: aload_2         /* source */
        //  1751: aload_3         /* sourceAny */
        //  1752: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getBookUrlPattern:()Ljava/lang/String;
        //  1755: invokevirtual   io/legado/app/data/entities/BookSource.setBookUrlPattern:(Ljava/lang/String;)V
        //  1758: aload_2         /* source */
        //  1759: aload_3         /* sourceAny */
        //  1760: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getCustomOrder:()I
        //  1763: invokevirtual   io/legado/app/data/entities/BookSource.setCustomOrder:(I)V
        //  1766: aload_2         /* source */
        //  1767: aload_3         /* sourceAny */
        //  1768: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getEnabled:()Z
        //  1771: invokevirtual   io/legado/app/data/entities/BookSource.setEnabled:(Z)V
        //  1774: aload_2         /* source */
        //  1775: aload_3         /* sourceAny */
        //  1776: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getEnabledExplore:()Z
        //  1779: invokevirtual   io/legado/app/data/entities/BookSource.setEnabledExplore:(Z)V
        //  1782: aload_2         /* source */
        //  1783: aload_3         /* sourceAny */
        //  1784: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getEnabledCookieJar:()Z
        //  1787: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  1790: invokevirtual   io/legado/app/data/entities/BookSource.setEnabledCookieJar:(Ljava/lang/Boolean;)V
        //  1793: aload_2         /* source */
        //  1794: aload_3         /* sourceAny */
        //  1795: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getConcurrentRate:()Ljava/lang/String;
        //  1798: invokevirtual   io/legado/app/data/entities/BookSource.setConcurrentRate:(Ljava/lang/String;)V
        //  1801: aload_2         /* source */
        //  1802: aload_3         /* sourceAny */
        //  1803: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getHeader:()Ljava/lang/String;
        //  1806: invokevirtual   io/legado/app/data/entities/BookSource.setHeader:(Ljava/lang/String;)V
        //  1809: aload_2         /* source */
        //  1810: aload_3         /* sourceAny */
        //  1811: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getLoginUrl:()Ljava/lang/Object;
        //  1814: astore          7
        //  1816: aload           7
        //  1818: ifnonnull       1825
        //  1821: aconst_null    
        //  1822: goto            1871
        //  1825: aload           7
        //  1827: instanceof      Ljava/lang/String;
        //  1830: ifeq            1843
        //  1833: aload_3         /* sourceAny */
        //  1834: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getLoginUrl:()Ljava/lang/Object;
        //  1837: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  1840: goto            1871
        //  1843: aload_3         /* sourceAny */
        //  1844: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getLoginUrl:()Ljava/lang/Object;
        //  1847: invokestatic    com/jayway/jsonpath/JsonPath.parse:(Ljava/lang/Object;)Lcom/jayway/jsonpath/DocumentContext;
        //  1850: astore          8
        //  1852: aload           8
        //  1854: ldc_w           "parse(sourceAny.loginUrl)"
        //  1857: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1860: aload           8
        //  1862: checkcast       Lcom/jayway/jsonpath/ReadContext;
        //  1865: ldc_w           "url"
        //  1868: invokestatic    io/legado/app/utils/JsonExtensionsKt.readString:(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/String;
        //  1871: invokevirtual   io/legado/app/data/entities/BookSource.setLoginUrl:(Ljava/lang/String;)V
        //  1874: aload_2         /* source */
        //  1875: aload_3         /* sourceAny */
        //  1876: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getLoginCheckJs:()Ljava/lang/String;
        //  1879: invokevirtual   io/legado/app/data/entities/BookSource.setLoginCheckJs:(Ljava/lang/String;)V
        //  1882: aload_2         /* source */
        //  1883: aload_3         /* sourceAny */
        //  1884: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getBookSourceComment:()Ljava/lang/String;
        //  1887: invokevirtual   io/legado/app/data/entities/BookSource.setBookSourceComment:(Ljava/lang/String;)V
        //  1890: aload_2         /* source */
        //  1891: aload_3         /* sourceAny */
        //  1892: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getLastUpdateTime:()J
        //  1895: invokevirtual   io/legado/app/data/entities/BookSource.setLastUpdateTime:(J)V
        //  1898: aload_2         /* source */
        //  1899: aload_3         /* sourceAny */
        //  1900: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRespondTime:()J
        //  1903: invokevirtual   io/legado/app/data/entities/BookSource.setRespondTime:(J)V
        //  1906: aload_2         /* source */
        //  1907: aload_3         /* sourceAny */
        //  1908: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getWeight:()I
        //  1911: invokevirtual   io/legado/app/data/entities/BookSource.setWeight:(I)V
        //  1914: aload_2         /* source */
        //  1915: aload_3         /* sourceAny */
        //  1916: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getExploreUrl:()Ljava/lang/String;
        //  1919: invokevirtual   io/legado/app/data/entities/BookSource.setExploreUrl:(Ljava/lang/String;)V
        //  1922: aload_2         /* source */
        //  1923: aload_3         /* sourceAny */
        //  1924: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleExplore:()Ljava/lang/Object;
        //  1927: instanceof      Ljava/lang/String;
        //  1930: ifeq            2076
        //  1933: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  1936: astore          7
        //  1938: aload_3         /* sourceAny */
        //  1939: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleExplore:()Ljava/lang/Object;
        //  1942: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  1945: astore          8
        //  1947: astore          22
        //  1949: iconst_0       
        //  1950: istore          $i$f$fromJsonObject
        //  1952: iconst_0       
        //  1953: istore          10
        //  1955: nop            
        //  1956: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  1959: astore          11
        //  1961: iconst_0       
        //  1962: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  1964: aload           $this$fromJsonObject$iv
        //  1966: aload           json$iv
        //  1968: iconst_0       
        //  1969: istore          $i$f$genericType
        //  1971: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$1;
        //  1974: dup            
        //  1975: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$1.<init>:()V
        //  1978: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
        //  1981: astore          14
        //  1983: aload           14
        //  1985: ldc             "object : TypeToken<T>() {}.type"
        //  1987: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1990: aload           14
        //  1992: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  1995: dup            
        //  1996: instanceof      Lio/legado/app/data/entities/rule/ExploreRule;
        //  1999: ifne            2004
        //  2002: pop            
        //  2003: aconst_null    
        //  2004: checkcast       Lio/legado/app/data/entities/rule/ExploreRule;
        //  2007: astore          null
        //  2009: iconst_0       
        //  2010: istore          13
        //  2012: aload           12
        //  2014: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2017: astore          11
        //  2019: goto            2042
        //  2022: astore          12
        //  2024: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2027: astore          13
        //  2029: iconst_0       
        //  2030: istore          14
        //  2032: aload           12
        //  2034: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2037: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2040: astore          11
        //  2042: aload           11
        //  2044: nop            
        //  2045: astore          23
        //  2047: aload           22
        //  2049: aload           23
        //  2051: astore          7
        //  2053: iconst_0       
        //  2054: istore          8
        //  2056: aload           7
        //  2058: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2061: ifeq            2068
        //  2064: aconst_null    
        //  2065: goto            2070
        //  2068: aload           7
        //  2070: checkcast       Lio/legado/app/data/entities/rule/ExploreRule;
        //  2073: goto            2219
        //  2076: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2079: astore          7
        //  2081: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2084: aload_3         /* sourceAny */
        //  2085: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleExplore:()Ljava/lang/Object;
        //  2088: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //  2091: astore          8
        //  2093: astore          22
        //  2095: iconst_0       
        //  2096: istore          $i$f$fromJsonObject
        //  2098: iconst_0       
        //  2099: istore          10
        //  2101: nop            
        //  2102: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2105: astore          11
        //  2107: iconst_0       
        //  2108: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  2110: aload           $this$fromJsonObject$iv
        //  2112: aload           json$iv
        //  2114: iconst_0       
        //  2115: istore          $i$f$genericType
        //  2117: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$2;
        //  2120: dup            
        //  2121: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$2.<init>:()V
        //  2124: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$2.getType:()Ljava/lang/reflect/Type;
        //  2127: astore          14
        //  2129: aload           14
        //  2131: ldc             "object : TypeToken<T>() {}.type"
        //  2133: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  2136: aload           14
        //  2138: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  2141: dup            
        //  2142: instanceof      Lio/legado/app/data/entities/rule/ExploreRule;
        //  2145: ifne            2150
        //  2148: pop            
        //  2149: aconst_null    
        //  2150: checkcast       Lio/legado/app/data/entities/rule/ExploreRule;
        //  2153: astore          null
        //  2155: iconst_0       
        //  2156: istore          13
        //  2158: aload           12
        //  2160: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2163: astore          11
        //  2165: goto            2188
        //  2168: astore          12
        //  2170: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2173: astore          13
        //  2175: iconst_0       
        //  2176: istore          14
        //  2178: aload           12
        //  2180: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2183: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2186: astore          11
        //  2188: aload           11
        //  2190: nop            
        //  2191: astore          23
        //  2193: aload           22
        //  2195: aload           23
        //  2197: astore          7
        //  2199: iconst_0       
        //  2200: istore          8
        //  2202: aload           7
        //  2204: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2207: ifeq            2214
        //  2210: aconst_null    
        //  2211: goto            2216
        //  2214: aload           7
        //  2216: checkcast       Lio/legado/app/data/entities/rule/ExploreRule;
        //  2219: invokevirtual   io/legado/app/data/entities/BookSource.setRuleExplore:(Lio/legado/app/data/entities/rule/ExploreRule;)V
        //  2222: aload_2         /* source */
        //  2223: aload_3         /* sourceAny */
        //  2224: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getSearchUrl:()Ljava/lang/String;
        //  2227: invokevirtual   io/legado/app/data/entities/BookSource.setSearchUrl:(Ljava/lang/String;)V
        //  2230: aload_2         /* source */
        //  2231: aload_3         /* sourceAny */
        //  2232: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleSearch:()Ljava/lang/Object;
        //  2235: instanceof      Ljava/lang/String;
        //  2238: ifeq            2384
        //  2241: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2244: astore          7
        //  2246: aload_3         /* sourceAny */
        //  2247: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleSearch:()Ljava/lang/Object;
        //  2250: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2253: astore          8
        //  2255: astore          22
        //  2257: iconst_0       
        //  2258: istore          $i$f$fromJsonObject
        //  2260: iconst_0       
        //  2261: istore          10
        //  2263: nop            
        //  2264: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2267: astore          11
        //  2269: iconst_0       
        //  2270: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  2272: aload           $this$fromJsonObject$iv
        //  2274: aload           json$iv
        //  2276: iconst_0       
        //  2277: istore          $i$f$genericType
        //  2279: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$3;
        //  2282: dup            
        //  2283: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$3.<init>:()V
        //  2286: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$3.getType:()Ljava/lang/reflect/Type;
        //  2289: astore          14
        //  2291: aload           14
        //  2293: ldc             "object : TypeToken<T>() {}.type"
        //  2295: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  2298: aload           14
        //  2300: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  2303: dup            
        //  2304: instanceof      Lio/legado/app/data/entities/rule/SearchRule;
        //  2307: ifne            2312
        //  2310: pop            
        //  2311: aconst_null    
        //  2312: checkcast       Lio/legado/app/data/entities/rule/SearchRule;
        //  2315: astore          null
        //  2317: iconst_0       
        //  2318: istore          13
        //  2320: aload           12
        //  2322: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2325: astore          11
        //  2327: goto            2350
        //  2330: astore          12
        //  2332: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2335: astore          13
        //  2337: iconst_0       
        //  2338: istore          14
        //  2340: aload           12
        //  2342: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2345: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2348: astore          11
        //  2350: aload           11
        //  2352: nop            
        //  2353: astore          23
        //  2355: aload           22
        //  2357: aload           23
        //  2359: astore          7
        //  2361: iconst_0       
        //  2362: istore          8
        //  2364: aload           7
        //  2366: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2369: ifeq            2376
        //  2372: aconst_null    
        //  2373: goto            2378
        //  2376: aload           7
        //  2378: checkcast       Lio/legado/app/data/entities/rule/SearchRule;
        //  2381: goto            2527
        //  2384: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2387: astore          7
        //  2389: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2392: aload_3         /* sourceAny */
        //  2393: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleSearch:()Ljava/lang/Object;
        //  2396: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //  2399: astore          8
        //  2401: astore          22
        //  2403: iconst_0       
        //  2404: istore          $i$f$fromJsonObject
        //  2406: iconst_0       
        //  2407: istore          10
        //  2409: nop            
        //  2410: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2413: astore          11
        //  2415: iconst_0       
        //  2416: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  2418: aload           $this$fromJsonObject$iv
        //  2420: aload           json$iv
        //  2422: iconst_0       
        //  2423: istore          $i$f$genericType
        //  2425: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$4;
        //  2428: dup            
        //  2429: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$4.<init>:()V
        //  2432: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$4.getType:()Ljava/lang/reflect/Type;
        //  2435: astore          14
        //  2437: aload           14
        //  2439: ldc             "object : TypeToken<T>() {}.type"
        //  2441: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  2444: aload           14
        //  2446: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  2449: dup            
        //  2450: instanceof      Lio/legado/app/data/entities/rule/SearchRule;
        //  2453: ifne            2458
        //  2456: pop            
        //  2457: aconst_null    
        //  2458: checkcast       Lio/legado/app/data/entities/rule/SearchRule;
        //  2461: astore          null
        //  2463: iconst_0       
        //  2464: istore          13
        //  2466: aload           12
        //  2468: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2471: astore          11
        //  2473: goto            2496
        //  2476: astore          12
        //  2478: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2481: astore          13
        //  2483: iconst_0       
        //  2484: istore          14
        //  2486: aload           12
        //  2488: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2491: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2494: astore          11
        //  2496: aload           11
        //  2498: nop            
        //  2499: astore          23
        //  2501: aload           22
        //  2503: aload           23
        //  2505: astore          7
        //  2507: iconst_0       
        //  2508: istore          8
        //  2510: aload           7
        //  2512: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2515: ifeq            2522
        //  2518: aconst_null    
        //  2519: goto            2524
        //  2522: aload           7
        //  2524: checkcast       Lio/legado/app/data/entities/rule/SearchRule;
        //  2527: invokevirtual   io/legado/app/data/entities/BookSource.setRuleSearch:(Lio/legado/app/data/entities/rule/SearchRule;)V
        //  2530: aload_2         /* source */
        //  2531: aload_3         /* sourceAny */
        //  2532: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleBookInfo:()Ljava/lang/Object;
        //  2535: instanceof      Ljava/lang/String;
        //  2538: ifeq            2684
        //  2541: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2544: astore          7
        //  2546: aload_3         /* sourceAny */
        //  2547: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleBookInfo:()Ljava/lang/Object;
        //  2550: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2553: astore          8
        //  2555: astore          22
        //  2557: iconst_0       
        //  2558: istore          $i$f$fromJsonObject
        //  2560: iconst_0       
        //  2561: istore          10
        //  2563: nop            
        //  2564: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2567: astore          11
        //  2569: iconst_0       
        //  2570: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  2572: aload           $this$fromJsonObject$iv
        //  2574: aload           json$iv
        //  2576: iconst_0       
        //  2577: istore          $i$f$genericType
        //  2579: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$5;
        //  2582: dup            
        //  2583: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$5.<init>:()V
        //  2586: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$5.getType:()Ljava/lang/reflect/Type;
        //  2589: astore          14
        //  2591: aload           14
        //  2593: ldc             "object : TypeToken<T>() {}.type"
        //  2595: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  2598: aload           14
        //  2600: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  2603: dup            
        //  2604: instanceof      Lio/legado/app/data/entities/rule/BookInfoRule;
        //  2607: ifne            2612
        //  2610: pop            
        //  2611: aconst_null    
        //  2612: checkcast       Lio/legado/app/data/entities/rule/BookInfoRule;
        //  2615: astore          null
        //  2617: iconst_0       
        //  2618: istore          13
        //  2620: aload           12
        //  2622: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2625: astore          11
        //  2627: goto            2650
        //  2630: astore          12
        //  2632: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2635: astore          13
        //  2637: iconst_0       
        //  2638: istore          14
        //  2640: aload           12
        //  2642: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2645: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2648: astore          11
        //  2650: aload           11
        //  2652: nop            
        //  2653: astore          23
        //  2655: aload           22
        //  2657: aload           23
        //  2659: astore          7
        //  2661: iconst_0       
        //  2662: istore          8
        //  2664: aload           7
        //  2666: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2669: ifeq            2676
        //  2672: aconst_null    
        //  2673: goto            2678
        //  2676: aload           7
        //  2678: checkcast       Lio/legado/app/data/entities/rule/BookInfoRule;
        //  2681: goto            2827
        //  2684: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2687: astore          7
        //  2689: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2692: aload_3         /* sourceAny */
        //  2693: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleBookInfo:()Ljava/lang/Object;
        //  2696: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //  2699: astore          8
        //  2701: astore          22
        //  2703: iconst_0       
        //  2704: istore          $i$f$fromJsonObject
        //  2706: iconst_0       
        //  2707: istore          10
        //  2709: nop            
        //  2710: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2713: astore          11
        //  2715: iconst_0       
        //  2716: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  2718: aload           $this$fromJsonObject$iv
        //  2720: aload           json$iv
        //  2722: iconst_0       
        //  2723: istore          $i$f$genericType
        //  2725: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$6;
        //  2728: dup            
        //  2729: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$6.<init>:()V
        //  2732: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$6.getType:()Ljava/lang/reflect/Type;
        //  2735: astore          14
        //  2737: aload           14
        //  2739: ldc             "object : TypeToken<T>() {}.type"
        //  2741: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  2744: aload           14
        //  2746: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  2749: dup            
        //  2750: instanceof      Lio/legado/app/data/entities/rule/BookInfoRule;
        //  2753: ifne            2758
        //  2756: pop            
        //  2757: aconst_null    
        //  2758: checkcast       Lio/legado/app/data/entities/rule/BookInfoRule;
        //  2761: astore          null
        //  2763: iconst_0       
        //  2764: istore          13
        //  2766: aload           12
        //  2768: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2771: astore          11
        //  2773: goto            2796
        //  2776: astore          12
        //  2778: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2781: astore          13
        //  2783: iconst_0       
        //  2784: istore          14
        //  2786: aload           12
        //  2788: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2791: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2794: astore          11
        //  2796: aload           11
        //  2798: nop            
        //  2799: astore          23
        //  2801: aload           22
        //  2803: aload           23
        //  2805: astore          7
        //  2807: iconst_0       
        //  2808: istore          8
        //  2810: aload           7
        //  2812: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2815: ifeq            2822
        //  2818: aconst_null    
        //  2819: goto            2824
        //  2822: aload           7
        //  2824: checkcast       Lio/legado/app/data/entities/rule/BookInfoRule;
        //  2827: invokevirtual   io/legado/app/data/entities/BookSource.setRuleBookInfo:(Lio/legado/app/data/entities/rule/BookInfoRule;)V
        //  2830: aload_2         /* source */
        //  2831: aload_3         /* sourceAny */
        //  2832: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleToc:()Ljava/lang/Object;
        //  2835: instanceof      Ljava/lang/String;
        //  2838: ifeq            2984
        //  2841: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2844: astore          7
        //  2846: aload_3         /* sourceAny */
        //  2847: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleToc:()Ljava/lang/Object;
        //  2850: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2853: astore          8
        //  2855: astore          22
        //  2857: iconst_0       
        //  2858: istore          $i$f$fromJsonObject
        //  2860: iconst_0       
        //  2861: istore          10
        //  2863: nop            
        //  2864: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2867: astore          11
        //  2869: iconst_0       
        //  2870: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  2872: aload           $this$fromJsonObject$iv
        //  2874: aload           json$iv
        //  2876: iconst_0       
        //  2877: istore          $i$f$genericType
        //  2879: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$7;
        //  2882: dup            
        //  2883: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$7.<init>:()V
        //  2886: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$7.getType:()Ljava/lang/reflect/Type;
        //  2889: astore          14
        //  2891: aload           14
        //  2893: ldc             "object : TypeToken<T>() {}.type"
        //  2895: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  2898: aload           14
        //  2900: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  2903: dup            
        //  2904: instanceof      Lio/legado/app/data/entities/rule/TocRule;
        //  2907: ifne            2912
        //  2910: pop            
        //  2911: aconst_null    
        //  2912: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //  2915: astore          null
        //  2917: iconst_0       
        //  2918: istore          13
        //  2920: aload           12
        //  2922: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2925: astore          11
        //  2927: goto            2950
        //  2930: astore          12
        //  2932: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  2935: astore          13
        //  2937: iconst_0       
        //  2938: istore          14
        //  2940: aload           12
        //  2942: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  2945: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  2948: astore          11
        //  2950: aload           11
        //  2952: nop            
        //  2953: astore          23
        //  2955: aload           22
        //  2957: aload           23
        //  2959: astore          7
        //  2961: iconst_0       
        //  2962: istore          8
        //  2964: aload           7
        //  2966: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  2969: ifeq            2976
        //  2972: aconst_null    
        //  2973: goto            2978
        //  2976: aload           7
        //  2978: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //  2981: goto            3127
        //  2984: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2987: astore          7
        //  2989: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  2992: aload_3         /* sourceAny */
        //  2993: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleToc:()Ljava/lang/Object;
        //  2996: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //  2999: astore          8
        //  3001: astore          22
        //  3003: iconst_0       
        //  3004: istore          $i$f$fromJsonObject
        //  3006: iconst_0       
        //  3007: istore          10
        //  3009: nop            
        //  3010: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3013: astore          11
        //  3015: iconst_0       
        //  3016: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  3018: aload           $this$fromJsonObject$iv
        //  3020: aload           json$iv
        //  3022: iconst_0       
        //  3023: istore          $i$f$genericType
        //  3025: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$8;
        //  3028: dup            
        //  3029: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$8.<init>:()V
        //  3032: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$8.getType:()Ljava/lang/reflect/Type;
        //  3035: astore          14
        //  3037: aload           14
        //  3039: ldc             "object : TypeToken<T>() {}.type"
        //  3041: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  3044: aload           14
        //  3046: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  3049: dup            
        //  3050: instanceof      Lio/legado/app/data/entities/rule/TocRule;
        //  3053: ifne            3058
        //  3056: pop            
        //  3057: aconst_null    
        //  3058: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //  3061: astore          null
        //  3063: iconst_0       
        //  3064: istore          13
        //  3066: aload           12
        //  3068: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3071: astore          11
        //  3073: goto            3096
        //  3076: astore          12
        //  3078: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3081: astore          13
        //  3083: iconst_0       
        //  3084: istore          14
        //  3086: aload           12
        //  3088: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  3091: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3094: astore          11
        //  3096: aload           11
        //  3098: nop            
        //  3099: astore          23
        //  3101: aload           22
        //  3103: aload           23
        //  3105: astore          7
        //  3107: iconst_0       
        //  3108: istore          8
        //  3110: aload           7
        //  3112: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  3115: ifeq            3122
        //  3118: aconst_null    
        //  3119: goto            3124
        //  3122: aload           7
        //  3124: checkcast       Lio/legado/app/data/entities/rule/TocRule;
        //  3127: invokevirtual   io/legado/app/data/entities/BookSource.setRuleToc:(Lio/legado/app/data/entities/rule/TocRule;)V
        //  3130: aload_2         /* source */
        //  3131: aload_3         /* sourceAny */
        //  3132: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleContent:()Ljava/lang/Object;
        //  3135: instanceof      Ljava/lang/String;
        //  3138: ifeq            3284
        //  3141: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  3144: astore          7
        //  3146: aload_3         /* sourceAny */
        //  3147: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleContent:()Ljava/lang/Object;
        //  3150: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  3153: astore          8
        //  3155: astore          22
        //  3157: iconst_0       
        //  3158: istore          $i$f$fromJsonObject
        //  3160: iconst_0       
        //  3161: istore          10
        //  3163: nop            
        //  3164: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3167: astore          11
        //  3169: iconst_0       
        //  3170: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  3172: aload           $this$fromJsonObject$iv
        //  3174: aload           json$iv
        //  3176: iconst_0       
        //  3177: istore          $i$f$genericType
        //  3179: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$9;
        //  3182: dup            
        //  3183: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$9.<init>:()V
        //  3186: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$9.getType:()Ljava/lang/reflect/Type;
        //  3189: astore          14
        //  3191: aload           14
        //  3193: ldc             "object : TypeToken<T>() {}.type"
        //  3195: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  3198: aload           14
        //  3200: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  3203: dup            
        //  3204: instanceof      Lio/legado/app/data/entities/rule/ContentRule;
        //  3207: ifne            3212
        //  3210: pop            
        //  3211: aconst_null    
        //  3212: checkcast       Lio/legado/app/data/entities/rule/ContentRule;
        //  3215: astore          null
        //  3217: iconst_0       
        //  3218: istore          13
        //  3220: aload           12
        //  3222: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3225: astore          11
        //  3227: goto            3250
        //  3230: astore          12
        //  3232: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3235: astore          13
        //  3237: iconst_0       
        //  3238: istore          14
        //  3240: aload           12
        //  3242: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  3245: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3248: astore          11
        //  3250: aload           11
        //  3252: nop            
        //  3253: astore          23
        //  3255: aload           22
        //  3257: aload           23
        //  3259: astore          7
        //  3261: iconst_0       
        //  3262: istore          8
        //  3264: aload           7
        //  3266: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  3269: ifeq            3276
        //  3272: aconst_null    
        //  3273: goto            3278
        //  3276: aload           7
        //  3278: checkcast       Lio/legado/app/data/entities/rule/ContentRule;
        //  3281: goto            3427
        //  3284: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  3287: astore          7
        //  3289: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //  3292: aload_3         /* sourceAny */
        //  3293: invokevirtual   io/legado/app/help/SourceAnalyzer$BookSourceAny.getRuleContent:()Ljava/lang/Object;
        //  3296: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //  3299: astore          8
        //  3301: astore          22
        //  3303: iconst_0       
        //  3304: istore          $i$f$fromJsonObject
        //  3306: iconst_0       
        //  3307: istore          10
        //  3309: nop            
        //  3310: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3313: astore          11
        //  3315: iconst_0       
        //  3316: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //  3318: aload           $this$fromJsonObject$iv
        //  3320: aload           json$iv
        //  3322: iconst_0       
        //  3323: istore          $i$f$genericType
        //  3325: new             Lio/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$10;
        //  3328: dup            
        //  3329: invokespecial   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$10.<init>:()V
        //  3332: invokevirtual   io/legado/app/help/SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$10.getType:()Ljava/lang/reflect/Type;
        //  3335: astore          14
        //  3337: aload           14
        //  3339: ldc             "object : TypeToken<T>() {}.type"
        //  3341: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  3344: aload           14
        //  3346: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //  3349: dup            
        //  3350: instanceof      Lio/legado/app/data/entities/rule/ContentRule;
        //  3353: ifne            3358
        //  3356: pop            
        //  3357: aconst_null    
        //  3358: checkcast       Lio/legado/app/data/entities/rule/ContentRule;
        //  3361: astore          null
        //  3363: iconst_0       
        //  3364: istore          13
        //  3366: aload           12
        //  3368: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3371: astore          11
        //  3373: goto            3396
        //  3376: astore          12
        //  3378: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3381: astore          13
        //  3383: iconst_0       
        //  3384: istore          14
        //  3386: aload           12
        //  3388: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  3391: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3394: astore          11
        //  3396: aload           11
        //  3398: nop            
        //  3399: astore          23
        //  3401: aload           22
        //  3403: aload           23
        //  3405: astore          7
        //  3407: iconst_0       
        //  3408: istore          8
        //  3410: aload           7
        //  3412: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //  3415: ifeq            3422
        //  3418: aconst_null    
        //  3419: goto            3424
        //  3422: aload           7
        //  3424: checkcast       Lio/legado/app/data/entities/rule/ContentRule;
        //  3427: invokevirtual   io/legado/app/data/entities/BookSource.setRuleContent:(Lio/legado/app/data/entities/rule/ContentRule;)V
        //  3430: aload_2         /* source */
        //  3431: astore          null
        //  3433: iconst_0       
        //  3434: istore          7
        //  3436: aload           6
        //  3438: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3441: astore          5
        //  3443: goto            3466
        //  3446: astore          6
        //  3448: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //  3451: astore          7
        //  3453: iconst_0       
        //  3454: istore          8
        //  3456: aload           6
        //  3458: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //  3461: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //  3464: astore          5
        //  3466: aload           5
        //  3468: areturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  json  
        //    StackMapTable: 00 70 FF 00 7B 00 0C 07 00 02 07 00 90 07 00 6A 00 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 01 07 00 04 FF 00 11 00 08 07 00 02 07 00 90 07 00 6A 00 07 00 CC 07 00 90 01 01 00 01 07 00 10 FD 00 13 07 00 04 07 00 04 FF 00 44 00 0A 07 00 02 07 00 90 07 00 6A 00 07 00 04 01 01 01 07 00 04 07 00 04 00 01 07 00 10 FF 00 00 00 0A 07 00 02 07 00 90 07 00 6A 00 07 00 04 01 00 01 00 07 00 04 00 00 12 41 07 00 04 FF 00 1B 00 0A 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 D2 00 07 00 04 00 00 44 07 00 04 FF 00 51 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 01 07 00 3A 00 01 07 00 6A 5C 07 00 6A FF 00 01 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 01 07 00 3A 00 02 07 00 6A 07 00 90 6A 07 00 6A FF 00 01 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 01 07 00 3A 00 02 07 00 6A 07 00 90 FF 00 2A 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 01 14 01 07 00 3A 00 01 07 00 6A FF 00 04 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 01 14 01 07 00 3A 00 02 07 00 6A 01 FF 00 B4 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 00 90 07 00 3A 00 01 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 4D 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A 43 07 00 6A FF 00 00 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 00 90 07 00 3A 00 02 07 00 6A 01 FF 00 1A 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 01 5A 07 00 3A 00 01 07 00 6A FF 00 04 00 0F 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 01 5A 07 00 3A 00 02 07 00 6A 01 FF 00 1F 00 11 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 00 B8 07 00 3A 01 01 00 00 03 40 01 08 FF 02 A2 00 16 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 00 90 07 00 3A 07 00 90 07 00 90 07 00 90 07 00 90 07 00 90 07 00 90 07 00 90 00 00 41 07 00 90 FF 00 36 00 16 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 6A 01 01 07 00 6A 01 07 00 90 07 00 90 07 00 3A 07 00 90 00 00 07 00 90 07 00 90 07 00 90 07 00 90 00 00 FF 00 48 00 0A 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 D2 00 07 00 04 00 00 FF 00 6A 00 0A 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 00 07 00 04 00 01 07 00 6A 51 07 00 6A FF 00 1B 00 0A 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 00 07 00 04 00 02 07 00 6A 07 00 90 FF 00 84 00 17 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 00 01 07 00 04 FF 00 11 00 17 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 00 00 00 00 00 00 00 00 00 00 00 07 00 6A 00 01 07 00 10 FF 00 13 00 17 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 FF 00 05 00 0A 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 00 07 00 04 00 01 07 00 6A FF 00 49 00 17 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 00 01 07 00 04 FF 00 11 00 17 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 00 00 00 00 00 00 00 00 00 00 00 07 00 6A 00 01 07 00 10 FF 00 13 00 17 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 FF 00 02 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 01 99 FF 00 5C 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 45 07 00 6A FF 00 49 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 FF 00 02 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 01 80 FF 00 54 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 45 07 00 6A FF 00 49 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 FF 00 02 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 01 B0 FF 00 54 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 45 07 00 6A FF 00 49 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 FF 00 02 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 01 B9 FF 00 54 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 45 07 00 6A FF 00 49 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 92 01 01 07 02 88 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 04 FF 00 11 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 00 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 10 FF 00 13 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 CC 07 00 90 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 00 FF 00 19 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 01 07 00 6A FF 00 01 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 00 04 FF 00 02 00 18 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 00 00 00 00 00 00 00 07 00 6A 07 00 04 00 02 07 00 6A 07 01 DC FF 00 02 00 0D 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 92 01 07 00 04 01 01 00 00 07 00 04 00 00 FF 00 0F 00 05 07 00 02 07 00 90 07 00 6A 07 00 D2 01 00 01 07 00 10 FF 00 13 00 09 07 00 02 07 00 90 07 00 6A 07 00 D2 01 07 00 04 07 00 04 00 01 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  74     138    141    161    Ljava/lang/Throwable;
        //  1955   2019   2022   2042   Ljava/lang/Throwable;
        //  2101   2165   2168   2188   Ljava/lang/Throwable;
        //  2263   2327   2330   2350   Ljava/lang/Throwable;
        //  2409   2473   2476   2496   Ljava/lang/Throwable;
        //  2563   2627   2630   2650   Ljava/lang/Throwable;
        //  2709   2773   2776   2796   Ljava/lang/Throwable;
        //  2863   2927   2930   2950   Ljava/lang/Throwable;
        //  3009   3073   3076   3096   Ljava/lang/Throwable;
        //  3163   3227   3230   3250   Ljava/lang/Throwable;
        //  3309   3373   3376   3396   Ljava/lang/Throwable;
        //  259    3443   3446   3466   Ljava/lang/Throwable;
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
    
    private final String toNewRule(final String oldRule) {
        final CharSequence charSequence = oldRule;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            return null;
        }
        String newRule = oldRule;
        boolean reverse = false;
        boolean allinone = false;
        if (StringsKt.startsWith$default(oldRule, "-", false, 2, (Object)null)) {
            reverse = true;
            final int beginIndex = 1;
            if (oldRule == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring = oldRule.substring(beginIndex);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            newRule = substring;
        }
        if (StringsKt.startsWith$default(newRule, "+", false, 2, (Object)null)) {
            allinone = true;
            final String s = newRule;
            final int beginIndex2 = 1;
            final String s2 = s;
            if (s2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring2 = s2.substring(beginIndex2);
            Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.String).substring(startIndex)");
            newRule = substring2;
        }
        if (!StringsKt.startsWith(newRule, "@CSS:", true) && !StringsKt.startsWith(newRule, "@XPath:", true) && !StringsKt.startsWith$default(newRule, "//", false, 2, (Object)null) && !StringsKt.startsWith$default(newRule, "##", false, 2, (Object)null) && !StringsKt.startsWith$default(newRule, ":", false, 2, (Object)null) && !StringsKt.contains((CharSequence)newRule, (CharSequence)"@js:", true) && !StringsKt.contains((CharSequence)newRule, (CharSequence)"<js>", true)) {
            if (StringsKt.contains$default((CharSequence)newRule, (CharSequence)"#", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)newRule, (CharSequence)"##", false, 2, (Object)null)) {
                newRule = StringsKt.replace$default(oldRule, "#", "##", false, 4, (Object)null);
            }
            if (StringsKt.contains$default((CharSequence)newRule, (CharSequence)"|", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)newRule, (CharSequence)"||", false, 2, (Object)null)) {
                if (StringsKt.contains$default((CharSequence)newRule, (CharSequence)"##", false, 2, (Object)null)) {
                    final List list = StringsKt.split$default((CharSequence)newRule, new String[] { "##" }, false, 0, 6, (Object)null);
                    if (StringsKt.contains$default((CharSequence)list.get(0), (CharSequence)"|", false, 2, (Object)null)) {
                        newRule = StringsKt.replace$default((String)list.get(0), "|", "||", false, 4, (Object)null);
                        int j = 1;
                        final int size = list.size();
                        if (j < size) {
                            do {
                                final int i = j;
                                ++j;
                                newRule = (Object)newRule + "##" + list.get(i);
                            } while (j < size);
                        }
                    }
                }
                else {
                    newRule = StringsKt.replace$default(newRule, "|", "||", false, 4, (Object)null);
                }
            }
            if (StringsKt.contains$default((CharSequence)newRule, (CharSequence)"&", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)newRule, (CharSequence)"&&", false, 2, (Object)null) && !StringsKt.contains$default((CharSequence)newRule, (CharSequence)"http", false, 2, (Object)null) && !StringsKt.startsWith$default(newRule, "/", false, 2, (Object)null)) {
                newRule = StringsKt.replace$default(newRule, "&", "&&", false, 4, (Object)null);
            }
        }
        if (allinone) {
            newRule = Intrinsics.stringPlus("+", (Object)newRule);
        }
        if (reverse) {
            newRule = Intrinsics.stringPlus("-", (Object)newRule);
        }
        return newRule;
    }
    
    private final String toNewUrls(final String oldUrls) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: checkcast       Ljava/lang/CharSequence;
        //     4: astore_2       
        //     5: iconst_0       
        //     6: istore_3       
        //     7: iconst_0       
        //     8: istore          4
        //    10: aload_2        
        //    11: ifnull          21
        //    14: aload_2        
        //    15: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //    18: ifeq            25
        //    21: iconst_1       
        //    22: goto            26
        //    25: iconst_0       
        //    26: ifeq            31
        //    29: aconst_null    
        //    30: areturn        
        //    31: aload_1         /* oldUrls */
        //    32: ldc_w           "@js:"
        //    35: iconst_0       
        //    36: iconst_2       
        //    37: aconst_null    
        //    38: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //    41: ifne            57
        //    44: aload_1         /* oldUrls */
        //    45: ldc_w           "<js>"
        //    48: iconst_0       
        //    49: iconst_2       
        //    50: aconst_null    
        //    51: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //    54: ifeq            59
        //    57: aload_1         /* oldUrls */
        //    58: areturn        
        //    59: aload_1         /* oldUrls */
        //    60: checkcast       Ljava/lang/CharSequence;
        //    63: ldc_w           "\n"
        //    66: checkcast       Ljava/lang/CharSequence;
        //    69: iconst_0       
        //    70: iconst_2       
        //    71: aconst_null    
        //    72: invokestatic    kotlin/text/StringsKt.contains$default:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;ZILjava/lang/Object;)Z
        //    75: ifne            103
        //    78: aload_1         /* oldUrls */
        //    79: checkcast       Ljava/lang/CharSequence;
        //    82: ldc_w           "&&"
        //    85: checkcast       Ljava/lang/CharSequence;
        //    88: iconst_0       
        //    89: iconst_2       
        //    90: aconst_null    
        //    91: invokestatic    kotlin/text/StringsKt.contains$default:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;ZILjava/lang/Object;)Z
        //    94: ifne            103
        //    97: aload_0         /* this */
        //    98: aload_1         /* oldUrls */
        //    99: invokespecial   io/legado/app/help/SourceAnalyzer.toNewUrl:(Ljava/lang/String;)Ljava/lang/String;
        //   102: areturn        
        //   103: aload_1         /* oldUrls */
        //   104: checkcast       Ljava/lang/CharSequence;
        //   107: astore_3       
        //   108: ldc_w           "(&&|\r?\n)+"
        //   111: astore          4
        //   113: iconst_0       
        //   114: istore          5
        //   116: new             Lkotlin/text/Regex;
        //   119: dup            
        //   120: aload           4
        //   122: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //   125: astore          4
        //   127: iconst_0       
        //   128: istore          5
        //   130: iconst_0       
        //   131: istore          6
        //   133: aload           4
        //   135: aload_3        
        //   136: iload           5
        //   138: invokevirtual   kotlin/text/Regex.split:(Ljava/lang/CharSequence;I)Ljava/util/List;
        //   141: astore_2        /* urls */
        //   142: aload_2         /* urls */
        //   143: checkcast       Ljava/lang/Iterable;
        //   146: astore_3        /* $this$map$iv */
        //   147: iconst_0       
        //   148: istore          $i$f$map
        //   150: aload_3         /* $this$map$iv */
        //   151: astore          5
        //   153: new             Ljava/util/ArrayList;
        //   156: dup            
        //   157: aload_3         /* $this$map$iv */
        //   158: bipush          10
        //   160: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   163: invokespecial   java/util/ArrayList.<init>:(I)V
        //   166: checkcast       Ljava/util/Collection;
        //   169: astore          destination$iv$iv
        //   171: iconst_0       
        //   172: istore          $i$f$mapTo
        //   174: aload           $this$mapTo$iv$iv
        //   176: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   181: astore          8
        //   183: aload           8
        //   185: invokeinterface java/util/Iterator.hasNext:()Z
        //   190: ifeq            293
        //   193: aload           8
        //   195: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   200: astore          item$iv$iv
        //   202: aload           destination$iv$iv
        //   204: aload           item$iv$iv
        //   206: checkcast       Ljava/lang/String;
        //   209: astore          10
        //   211: astore          17
        //   213: iconst_0       
        //   214: istore          $i$a$-map-SourceAnalyzer$toNewUrls$1
        //   216: getstatic       io/legado/app/help/SourceAnalyzer.INSTANCE:Lio/legado/app/help/SourceAnalyzer;
        //   219: aload           it
        //   221: invokespecial   io/legado/app/help/SourceAnalyzer.toNewUrl:(Ljava/lang/String;)Ljava/lang/String;
        //   224: astore          12
        //   226: aload           12
        //   228: ifnonnull       235
        //   231: aconst_null    
        //   232: goto            277
        //   235: aload           12
        //   237: checkcast       Ljava/lang/CharSequence;
        //   240: astore          13
        //   242: ldc_w           "\n\\s*"
        //   245: astore          14
        //   247: iconst_0       
        //   248: istore          15
        //   250: new             Lkotlin/text/Regex;
        //   253: dup            
        //   254: aload           14
        //   256: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //   259: astore          14
        //   261: ldc             ""
        //   263: astore          15
        //   265: iconst_0       
        //   266: istore          16
        //   268: aload           14
        //   270: aload           13
        //   272: aload           15
        //   274: invokevirtual   kotlin/text/Regex.replace:(Ljava/lang/CharSequence;Ljava/lang/String;)Ljava/lang/String;
        //   277: nop            
        //   278: astore          18
        //   280: aload           17
        //   282: aload           18
        //   284: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   289: pop            
        //   290: goto            183
        //   293: aload           destination$iv$iv
        //   295: checkcast       Ljava/util/List;
        //   298: nop            
        //   299: checkcast       Ljava/lang/Iterable;
        //   302: ldc_w           "\n"
        //   305: checkcast       Ljava/lang/CharSequence;
        //   308: aconst_null    
        //   309: aconst_null    
        //   310: iconst_0       
        //   311: aconst_null    
        //   312: aconst_null    
        //   313: bipush          62
        //   315: aconst_null    
        //   316: invokestatic    kotlin/collections/CollectionsKt.joinToString$default:(Ljava/lang/Iterable;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/String;
        //   319: areturn        
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  oldUrls  
        //    StackMapTable: 00 0B FE 00 15 07 00 B8 01 01 03 40 01 04 19 01 2B FF 00 4F 00 09 07 00 02 07 00 90 07 00 22 07 02 EC 01 07 02 EC 07 02 F6 01 07 00 49 00 00 FF 00 33 00 12 07 00 02 07 00 90 07 00 22 07 02 EC 01 07 02 EC 07 02 F6 01 07 00 49 07 00 04 07 00 90 01 07 00 90 00 00 00 00 07 02 F6 00 00 69 07 00 90 FF 00 0F 00 09 07 00 02 07 00 90 07 00 22 07 02 EC 01 07 02 EC 07 02 F6 01 07 00 49 00 00
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
    
    private final String toNewUrl(final String oldUrl) {
        final CharSequence charSequence = oldUrl;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            return null;
        }
        String url = oldUrl;
        if (StringsKt.startsWith(oldUrl, "<js>", true)) {
            url = StringsKt.replace$default(StringsKt.replace$default(url, "=searchKey", "={{key}}", false, 4, (Object)null), "=searchPage", "={{page}}", false, 4, (Object)null);
            return url;
        }
        final HashMap map = new HashMap();
        Matcher mather = SourceAnalyzer.headerPattern.matcher(url);
        if (mather.find()) {
            final String header = mather.group();
            final String s = url;
            Intrinsics.checkNotNullExpressionValue((Object)header, "header");
            url = StringsKt.replace$default(s, header, "", false, 4, (Object)null);
            final Map map2 = map;
            final String s2 = "headers";
            final String substring = header.substring(8);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            map2.put(s2, substring);
        }
        List urlList = StringsKt.split$default((CharSequence)url, new String[] { "|" }, false, 0, 6, (Object)null);
        url = urlList.get(0);
        if (urlList.size() > 1) {
            map.put("charset", StringsKt.split$default((CharSequence)urlList.get(1), new String[] { "=" }, false, 0, 6, (Object)null).get(1));
        }
        mather = SourceAnalyzer.jsPattern.matcher(url);
        final ArrayList jsList = new ArrayList();
        while (mather.find()) {
            jsList.add(mather.group());
            url = StringsKt.replace$default(url, (String)CollectionsKt.last((List)jsList), Intrinsics.stringPlus("$", (Object)(jsList.size() - 1)), false, 4, (Object)null);
        }
        url = StringsKt.replace$default(StringsKt.replace$default(url, "{", "<", false, 4, (Object)null), "}", ">", false, 4, (Object)null);
        url = StringsKt.replace$default(url, "searchKey", "{{key}}", false, 4, (Object)null);
        url = StringsKt.replace$default(new Regex("searchPage([-+]1)").replace((CharSequence)new Regex("<searchPage([-+]1)>").replace((CharSequence)url, "{{page$1}}"), "{{page$1}}"), "searchPage", "{{page}}", false, 4, (Object)null);
        final Iterator iterator = jsList.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final int index = n;
            ++n;
            final String item = (String)iterator.next();
            url = StringsKt.replace$default(url, Intrinsics.stringPlus("$", (Object)index), StringsKt.replace$default(StringsKt.replace$default(item, "searchKey", "key", false, 4, (Object)null), "searchPage", "page", false, 4, (Object)null), false, 4, (Object)null);
        }
        urlList = StringsKt.split$default((CharSequence)url, new String[] { "@" }, false, 0, 6, (Object)null);
        url = urlList.get(0);
        if (urlList.size() > 1) {
            map.put("method", "POST");
            map.put("body", urlList.get(1));
        }
        if (map.size() > 0) {
            url = url + ',' + (Object)GsonExtensionsKt.getGSON().toJson((Object)map);
        }
        return url;
    }
    
    private final String uaToHeader(final String ua) {
        final CharSequence charSequence = ua;
        if (charSequence == null || charSequence.length() == 0) {
            return null;
        }
        final Map map = MapsKt.mapOf(new Pair((Object)"User-Agent", (Object)ua));
        return GsonExtensionsKt.getGSON().toJson((Object)map);
    }
    
    static {
        INSTANCE = new SourceAnalyzer();
        headerPattern = Pattern.compile("@Header:\\{.+?\\}", 2);
        jsPattern = Pattern.compile("\\{\\{.+?\\}\\}", 2);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\be\b\u0086\b\u0018\u00002\u00020\u0001B\u009d\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0001?\u0006\u0002\u0010\u001fJ\t\u0010\\\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010]\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010^\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010_\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\u000b\u0010`\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\u000b\u0010a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010c\u001a\u00020\u0015H\u00c6\u0003J\t\u0010d\u001a\u00020\u0015H\u00c6\u0003J\t\u0010e\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010g\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010h\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\u000b\u0010i\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010j\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\u000b\u0010k\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\u000b\u0010l\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\u000b\u0010m\u001a\u0004\u0018\u00010\u0001H\u00c6\u0003J\t\u0010n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010o\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010p\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010q\u001a\u00020\u0007H\u00c6\u0003J\t\u0010r\u001a\u00020\u000bH\u00c6\u0003J\t\u0010s\u001a\u00020\u000bH\u00c6\u0003J\t\u0010t\u001a\u00020\u000bH\u00c6\u0003J?\u0002\u0010u\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u00072\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00c6\u0001J\u0013\u0010v\u001a\u00020\u000b2\b\u0010w\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010x\u001a\u00020\u0007H\u00d6\u0001J\t\u0010y\u001a\u00020\u0003H\u00d6\u0001R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b$\u0010!\"\u0004\b%\u0010#R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b&\u0010!\"\u0004\b'\u0010#R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b,\u0010!\"\u0004\b-\u0010#R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b.\u0010!\"\u0004\b/\u0010#R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b0\u0010!\"\u0004\b1\u0010#R\u001a\u0010\t\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b2\u0010)\"\u0004\b3\u0010+R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u001a\u0010\r\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b8\u00105\"\u0004\b9\u00107R\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b:\u00105\"\u0004\b;\u00107R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b<\u0010!\"\u0004\b=\u0010#R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b>\u0010!\"\u0004\b?\u0010#R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bD\u0010!\"\u0004\bE\u0010#R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bF\u0010G\"\u0004\bH\u0010IR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010G\"\u0004\bK\u0010IR\u001a\u0010\u0016\u001a\u00020\u0015X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bL\u0010A\"\u0004\bM\u0010CR\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bN\u0010G\"\u0004\bO\u0010IR\u001c\u0010\u001e\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bP\u0010G\"\u0004\bQ\u0010IR\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bR\u0010G\"\u0004\bS\u0010IR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bT\u0010G\"\u0004\bU\u0010IR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0001X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bV\u0010G\"\u0004\bW\u0010IR\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bX\u0010!\"\u0004\bY\u0010#R\u001a\u0010\u0017\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010)\"\u0004\b[\u0010+¡§\u0006z" }, d2 = { "Lio/legado/app/help/SourceAnalyzer$BookSourceAny;", "", "bookSourceName", "", "bookSourceGroup", "bookSourceUrl", "bookSourceType", "", "bookUrlPattern", "customOrder", "enabled", "", "enabledExplore", "enabledCookieJar", "concurrentRate", "header", "loginUrl", "loginUi", "loginCheckJs", "bookSourceComment", "lastUpdateTime", "", "respondTime", "weight", "exploreUrl", "ruleExplore", "searchUrl", "ruleSearch", "ruleBookInfo", "ruleToc", "ruleContent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZZLjava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getBookSourceComment", "()Ljava/lang/String;", "setBookSourceComment", "(Ljava/lang/String;)V", "getBookSourceGroup", "setBookSourceGroup", "getBookSourceName", "setBookSourceName", "getBookSourceType", "()I", "setBookSourceType", "(I)V", "getBookSourceUrl", "setBookSourceUrl", "getBookUrlPattern", "setBookUrlPattern", "getConcurrentRate", "setConcurrentRate", "getCustomOrder", "setCustomOrder", "getEnabled", "()Z", "setEnabled", "(Z)V", "getEnabledCookieJar", "setEnabledCookieJar", "getEnabledExplore", "setEnabledExplore", "getExploreUrl", "setExploreUrl", "getHeader", "setHeader", "getLastUpdateTime", "()J", "setLastUpdateTime", "(J)V", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "()Ljava/lang/Object;", "setLoginUi", "(Ljava/lang/Object;)V", "getLoginUrl", "setLoginUrl", "getRespondTime", "setRespondTime", "getRuleBookInfo", "setRuleBookInfo", "getRuleContent", "setRuleContent", "getRuleExplore", "setRuleExplore", "getRuleSearch", "setRuleSearch", "getRuleToc", "setRuleToc", "getSearchUrl", "setSearchUrl", "getWeight", "setWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "reader-pro" })
    public static final class BookSourceAny
    {
        @NotNull
        private String bookSourceName;
        @Nullable
        private String bookSourceGroup;
        @NotNull
        private String bookSourceUrl;
        private int bookSourceType;
        @Nullable
        private String bookUrlPattern;
        private int customOrder;
        private boolean enabled;
        private boolean enabledExplore;
        private boolean enabledCookieJar;
        @Nullable
        private String concurrentRate;
        @Nullable
        private String header;
        @Nullable
        private Object loginUrl;
        @Nullable
        private Object loginUi;
        @Nullable
        private String loginCheckJs;
        @Nullable
        private String bookSourceComment;
        private long lastUpdateTime;
        private long respondTime;
        private int weight;
        @Nullable
        private String exploreUrl;
        @Nullable
        private Object ruleExplore;
        @Nullable
        private String searchUrl;
        @Nullable
        private Object ruleSearch;
        @Nullable
        private Object ruleBookInfo;
        @Nullable
        private Object ruleToc;
        @Nullable
        private Object ruleContent;
        
        public BookSourceAny(@NotNull final String bookSourceName, @Nullable final String bookSourceGroup, @NotNull final String bookSourceUrl, final int bookSourceType, @Nullable final String bookUrlPattern, final int customOrder, final boolean enabled, final boolean enabledExplore, final boolean enabledCookieJar, @Nullable final String concurrentRate, @Nullable final String header, @Nullable final Object loginUrl, @Nullable final Object loginUi, @Nullable final String loginCheckJs, @Nullable final String bookSourceComment, final long lastUpdateTime, final long respondTime, final int weight, @Nullable final String exploreUrl, @Nullable final Object ruleExplore, @Nullable final String searchUrl, @Nullable final Object ruleSearch, @Nullable final Object ruleBookInfo, @Nullable final Object ruleToc, @Nullable final Object ruleContent) {
            Intrinsics.checkNotNullParameter((Object)bookSourceName, "bookSourceName");
            Intrinsics.checkNotNullParameter((Object)bookSourceUrl, "bookSourceUrl");
            this.bookSourceName = bookSourceName;
            this.bookSourceGroup = bookSourceGroup;
            this.bookSourceUrl = bookSourceUrl;
            this.bookSourceType = bookSourceType;
            this.bookUrlPattern = bookUrlPattern;
            this.customOrder = customOrder;
            this.enabled = enabled;
            this.enabledExplore = enabledExplore;
            this.enabledCookieJar = enabledCookieJar;
            this.concurrentRate = concurrentRate;
            this.header = header;
            this.loginUrl = loginUrl;
            this.loginUi = loginUi;
            this.loginCheckJs = loginCheckJs;
            this.bookSourceComment = bookSourceComment;
            this.lastUpdateTime = lastUpdateTime;
            this.respondTime = respondTime;
            this.weight = weight;
            this.exploreUrl = exploreUrl;
            this.ruleExplore = ruleExplore;
            this.searchUrl = searchUrl;
            this.ruleSearch = ruleSearch;
            this.ruleBookInfo = ruleBookInfo;
            this.ruleToc = ruleToc;
            this.ruleContent = ruleContent;
        }
        
        @NotNull
        public final String getBookSourceName() {
            return this.bookSourceName;
        }
        
        public final void setBookSourceName(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.bookSourceName = <set-?>;
        }
        
        @Nullable
        public final String getBookSourceGroup() {
            return this.bookSourceGroup;
        }
        
        public final void setBookSourceGroup(@Nullable final String <set-?>) {
            this.bookSourceGroup = <set-?>;
        }
        
        @NotNull
        public final String getBookSourceUrl() {
            return this.bookSourceUrl;
        }
        
        public final void setBookSourceUrl(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.bookSourceUrl = <set-?>;
        }
        
        public final int getBookSourceType() {
            return this.bookSourceType;
        }
        
        public final void setBookSourceType(final int <set-?>) {
            this.bookSourceType = <set-?>;
        }
        
        @Nullable
        public final String getBookUrlPattern() {
            return this.bookUrlPattern;
        }
        
        public final void setBookUrlPattern(@Nullable final String <set-?>) {
            this.bookUrlPattern = <set-?>;
        }
        
        public final int getCustomOrder() {
            return this.customOrder;
        }
        
        public final void setCustomOrder(final int <set-?>) {
            this.customOrder = <set-?>;
        }
        
        public final boolean getEnabled() {
            return this.enabled;
        }
        
        public final void setEnabled(final boolean <set-?>) {
            this.enabled = <set-?>;
        }
        
        public final boolean getEnabledExplore() {
            return this.enabledExplore;
        }
        
        public final void setEnabledExplore(final boolean <set-?>) {
            this.enabledExplore = <set-?>;
        }
        
        public final boolean getEnabledCookieJar() {
            return this.enabledCookieJar;
        }
        
        public final void setEnabledCookieJar(final boolean <set-?>) {
            this.enabledCookieJar = <set-?>;
        }
        
        @Nullable
        public final String getConcurrentRate() {
            return this.concurrentRate;
        }
        
        public final void setConcurrentRate(@Nullable final String <set-?>) {
            this.concurrentRate = <set-?>;
        }
        
        @Nullable
        public final String getHeader() {
            return this.header;
        }
        
        public final void setHeader(@Nullable final String <set-?>) {
            this.header = <set-?>;
        }
        
        @Nullable
        public final Object getLoginUrl() {
            return this.loginUrl;
        }
        
        public final void setLoginUrl(@Nullable final Object <set-?>) {
            this.loginUrl = <set-?>;
        }
        
        @Nullable
        public final Object getLoginUi() {
            return this.loginUi;
        }
        
        public final void setLoginUi(@Nullable final Object <set-?>) {
            this.loginUi = <set-?>;
        }
        
        @Nullable
        public final String getLoginCheckJs() {
            return this.loginCheckJs;
        }
        
        public final void setLoginCheckJs(@Nullable final String <set-?>) {
            this.loginCheckJs = <set-?>;
        }
        
        @Nullable
        public final String getBookSourceComment() {
            return this.bookSourceComment;
        }
        
        public final void setBookSourceComment(@Nullable final String <set-?>) {
            this.bookSourceComment = <set-?>;
        }
        
        public final long getLastUpdateTime() {
            return this.lastUpdateTime;
        }
        
        public final void setLastUpdateTime(final long <set-?>) {
            this.lastUpdateTime = <set-?>;
        }
        
        public final long getRespondTime() {
            return this.respondTime;
        }
        
        public final void setRespondTime(final long <set-?>) {
            this.respondTime = <set-?>;
        }
        
        public final int getWeight() {
            return this.weight;
        }
        
        public final void setWeight(final int <set-?>) {
            this.weight = <set-?>;
        }
        
        @Nullable
        public final String getExploreUrl() {
            return this.exploreUrl;
        }
        
        public final void setExploreUrl(@Nullable final String <set-?>) {
            this.exploreUrl = <set-?>;
        }
        
        @Nullable
        public final Object getRuleExplore() {
            return this.ruleExplore;
        }
        
        public final void setRuleExplore(@Nullable final Object <set-?>) {
            this.ruleExplore = <set-?>;
        }
        
        @Nullable
        public final String getSearchUrl() {
            return this.searchUrl;
        }
        
        public final void setSearchUrl(@Nullable final String <set-?>) {
            this.searchUrl = <set-?>;
        }
        
        @Nullable
        public final Object getRuleSearch() {
            return this.ruleSearch;
        }
        
        public final void setRuleSearch(@Nullable final Object <set-?>) {
            this.ruleSearch = <set-?>;
        }
        
        @Nullable
        public final Object getRuleBookInfo() {
            return this.ruleBookInfo;
        }
        
        public final void setRuleBookInfo(@Nullable final Object <set-?>) {
            this.ruleBookInfo = <set-?>;
        }
        
        @Nullable
        public final Object getRuleToc() {
            return this.ruleToc;
        }
        
        public final void setRuleToc(@Nullable final Object <set-?>) {
            this.ruleToc = <set-?>;
        }
        
        @Nullable
        public final Object getRuleContent() {
            return this.ruleContent;
        }
        
        public final void setRuleContent(@Nullable final Object <set-?>) {
            this.ruleContent = <set-?>;
        }
        
        @NotNull
        public final String component1() {
            return this.bookSourceName;
        }
        
        @Nullable
        public final String component2() {
            return this.bookSourceGroup;
        }
        
        @NotNull
        public final String component3() {
            return this.bookSourceUrl;
        }
        
        public final int component4() {
            return this.bookSourceType;
        }
        
        @Nullable
        public final String component5() {
            return this.bookUrlPattern;
        }
        
        public final int component6() {
            return this.customOrder;
        }
        
        public final boolean component7() {
            return this.enabled;
        }
        
        public final boolean component8() {
            return this.enabledExplore;
        }
        
        public final boolean component9() {
            return this.enabledCookieJar;
        }
        
        @Nullable
        public final String component10() {
            return this.concurrentRate;
        }
        
        @Nullable
        public final String component11() {
            return this.header;
        }
        
        @Nullable
        public final Object component12() {
            return this.loginUrl;
        }
        
        @Nullable
        public final Object component13() {
            return this.loginUi;
        }
        
        @Nullable
        public final String component14() {
            return this.loginCheckJs;
        }
        
        @Nullable
        public final String component15() {
            return this.bookSourceComment;
        }
        
        public final long component16() {
            return this.lastUpdateTime;
        }
        
        public final long component17() {
            return this.respondTime;
        }
        
        public final int component18() {
            return this.weight;
        }
        
        @Nullable
        public final String component19() {
            return this.exploreUrl;
        }
        
        @Nullable
        public final Object component20() {
            return this.ruleExplore;
        }
        
        @Nullable
        public final String component21() {
            return this.searchUrl;
        }
        
        @Nullable
        public final Object component22() {
            return this.ruleSearch;
        }
        
        @Nullable
        public final Object component23() {
            return this.ruleBookInfo;
        }
        
        @Nullable
        public final Object component24() {
            return this.ruleToc;
        }
        
        @Nullable
        public final Object component25() {
            return this.ruleContent;
        }
        
        @NotNull
        public final BookSourceAny copy(@NotNull final String bookSourceName, @Nullable final String bookSourceGroup, @NotNull final String bookSourceUrl, final int bookSourceType, @Nullable final String bookUrlPattern, final int customOrder, final boolean enabled, final boolean enabledExplore, final boolean enabledCookieJar, @Nullable final String concurrentRate, @Nullable final String header, @Nullable final Object loginUrl, @Nullable final Object loginUi, @Nullable final String loginCheckJs, @Nullable final String bookSourceComment, final long lastUpdateTime, final long respondTime, final int weight, @Nullable final String exploreUrl, @Nullable final Object ruleExplore, @Nullable final String searchUrl, @Nullable final Object ruleSearch, @Nullable final Object ruleBookInfo, @Nullable final Object ruleToc, @Nullable final Object ruleContent) {
            Intrinsics.checkNotNullParameter((Object)bookSourceName, "bookSourceName");
            Intrinsics.checkNotNullParameter((Object)bookSourceUrl, "bookSourceUrl");
            return new BookSourceAny(bookSourceName, bookSourceGroup, bookSourceUrl, bookSourceType, bookUrlPattern, customOrder, enabled, enabledExplore, enabledCookieJar, concurrentRate, header, loginUrl, loginUi, loginCheckJs, bookSourceComment, lastUpdateTime, respondTime, weight, exploreUrl, ruleExplore, searchUrl, ruleSearch, ruleBookInfo, ruleToc, ruleContent);
        }
        
        @NotNull
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("BookSourceAny(bookSourceName=").append(this.bookSourceName).append(", bookSourceGroup=").append((Object)this.bookSourceGroup).append(", bookSourceUrl=").append(this.bookSourceUrl).append(", bookSourceType=").append(this.bookSourceType).append(", bookUrlPattern=").append((Object)this.bookUrlPattern).append(", customOrder=").append(this.customOrder).append(", enabled=").append(this.enabled).append(", enabledExplore=").append(this.enabledExplore).append(", enabledCookieJar=").append(this.enabledCookieJar).append(", concurrentRate=").append((Object)this.concurrentRate).append(", header=").append((Object)this.header).append(", loginUrl=");
            sb.append(this.loginUrl).append(", loginUi=").append(this.loginUi).append(", loginCheckJs=").append((Object)this.loginCheckJs).append(", bookSourceComment=").append((Object)this.bookSourceComment).append(", lastUpdateTime=").append(this.lastUpdateTime).append(", respondTime=").append(this.respondTime).append(", weight=").append(this.weight).append(", exploreUrl=").append((Object)this.exploreUrl).append(", ruleExplore=").append(this.ruleExplore).append(", searchUrl=").append((Object)this.searchUrl).append(", ruleSearch=").append(this.ruleSearch).append(", ruleBookInfo=").append(this.ruleBookInfo);
            sb.append(", ruleToc=").append(this.ruleToc).append(", ruleContent=").append(this.ruleContent).append(')');
            return sb.toString();
        }
        
        @Override
        public int hashCode() {
            int result = this.bookSourceName.hashCode();
            result = result * 31 + ((this.bookSourceGroup == null) ? 0 : this.bookSourceGroup.hashCode());
            result = result * 31 + this.bookSourceUrl.hashCode();
            result = result * 31 + Integer.hashCode(this.bookSourceType);
            result = result * 31 + ((this.bookUrlPattern == null) ? 0 : this.bookUrlPattern.hashCode());
            result = result * 31 + Integer.hashCode(this.customOrder);
            final int n = result * 31;
            int enabled;
            if ((enabled = (this.enabled ? 1 : 0)) != 0) {
                enabled = 1;
            }
            result = n + enabled;
            final int n2 = result * 31;
            int enabledExplore;
            if ((enabledExplore = (this.enabledExplore ? 1 : 0)) != 0) {
                enabledExplore = 1;
            }
            result = n2 + enabledExplore;
            final int n3 = result * 31;
            int enabledCookieJar;
            if ((enabledCookieJar = (this.enabledCookieJar ? 1 : 0)) != 0) {
                enabledCookieJar = 1;
            }
            result = n3 + enabledCookieJar;
            result = result * 31 + ((this.concurrentRate == null) ? 0 : this.concurrentRate.hashCode());
            result = result * 31 + ((this.header == null) ? 0 : this.header.hashCode());
            result = result * 31 + ((this.loginUrl == null) ? 0 : this.loginUrl.hashCode());
            result = result * 31 + ((this.loginUi == null) ? 0 : this.loginUi.hashCode());
            result = result * 31 + ((this.loginCheckJs == null) ? 0 : this.loginCheckJs.hashCode());
            result = result * 31 + ((this.bookSourceComment == null) ? 0 : this.bookSourceComment.hashCode());
            result = result * 31 + Long.hashCode(this.lastUpdateTime);
            result = result * 31 + Long.hashCode(this.respondTime);
            result = result * 31 + Integer.hashCode(this.weight);
            result = result * 31 + ((this.exploreUrl == null) ? 0 : this.exploreUrl.hashCode());
            result = result * 31 + ((this.ruleExplore == null) ? 0 : this.ruleExplore.hashCode());
            result = result * 31 + ((this.searchUrl == null) ? 0 : this.searchUrl.hashCode());
            result = result * 31 + ((this.ruleSearch == null) ? 0 : this.ruleSearch.hashCode());
            result = result * 31 + ((this.ruleBookInfo == null) ? 0 : this.ruleBookInfo.hashCode());
            result = result * 31 + ((this.ruleToc == null) ? 0 : this.ruleToc.hashCode());
            result = result * 31 + ((this.ruleContent == null) ? 0 : this.ruleContent.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof BookSourceAny)) {
                return false;
            }
            final BookSourceAny bookSourceAny = (BookSourceAny)other;
            return Intrinsics.areEqual((Object)this.bookSourceName, (Object)bookSourceAny.bookSourceName) && Intrinsics.areEqual((Object)this.bookSourceGroup, (Object)bookSourceAny.bookSourceGroup) && Intrinsics.areEqual((Object)this.bookSourceUrl, (Object)bookSourceAny.bookSourceUrl) && this.bookSourceType == bookSourceAny.bookSourceType && Intrinsics.areEqual((Object)this.bookUrlPattern, (Object)bookSourceAny.bookUrlPattern) && this.customOrder == bookSourceAny.customOrder && this.enabled == bookSourceAny.enabled && this.enabledExplore == bookSourceAny.enabledExplore && this.enabledCookieJar == bookSourceAny.enabledCookieJar && Intrinsics.areEqual((Object)this.concurrentRate, (Object)bookSourceAny.concurrentRate) && Intrinsics.areEqual((Object)this.header, (Object)bookSourceAny.header) && Intrinsics.areEqual(this.loginUrl, bookSourceAny.loginUrl) && Intrinsics.areEqual(this.loginUi, bookSourceAny.loginUi) && Intrinsics.areEqual((Object)this.loginCheckJs, (Object)bookSourceAny.loginCheckJs) && Intrinsics.areEqual((Object)this.bookSourceComment, (Object)bookSourceAny.bookSourceComment) && this.lastUpdateTime == bookSourceAny.lastUpdateTime && this.respondTime == bookSourceAny.respondTime && this.weight == bookSourceAny.weight && Intrinsics.areEqual((Object)this.exploreUrl, (Object)bookSourceAny.exploreUrl) && Intrinsics.areEqual(this.ruleExplore, bookSourceAny.ruleExplore) && Intrinsics.areEqual((Object)this.searchUrl, (Object)bookSourceAny.searchUrl) && Intrinsics.areEqual(this.ruleSearch, bookSourceAny.ruleSearch) && Intrinsics.areEqual(this.ruleBookInfo, bookSourceAny.ruleBookInfo) && Intrinsics.areEqual(this.ruleToc, bookSourceAny.ruleToc) && Intrinsics.areEqual(this.ruleContent, bookSourceAny.ruleContent);
        }
        
        public BookSourceAny() {
            this(null, null, null, 0, null, 0, false, false, false, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 33554431, null);
        }
    }
}
