// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.webBook;

import java.net.URL;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.collections.CollectionsKt;
import java.util.Collection;
import io.legado.app.model.DebugLog$DefaultImpls;
import io.legado.app.utils.HtmlFormatter;
import java.util.ArrayList;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import java.util.List;
import kotlin.Pair;
import io.legado.app.data.entities.rule.ContentRule;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002Jr\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00060\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J[\u0010\u0003\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00052\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0019" }, d2 = { "Lio/legado/app/model/webBook/BookContent;", "", "()V", "analyzeContent", "Lkotlin/Pair;", "", "", "book", "Lio/legado/app/data/entities/Book;", "baseUrl", "redirectUrl", "body", "contentRule", "Lio/legado/app/data/entities/rule/ContentRule;", "chapter", "Lio/legado/app/data/entities/BookChapter;", "bookSource", "Lio/legado/app/data/entities/BookSource;", "nextChapterUrl", "printLog", "", "debugLog", "Lio/legado/app/model/DebugLog;", "bookChapter", "(Ljava/lang/String;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class BookContent
{
    @NotNull
    public static final BookContent INSTANCE;
    
    private BookContent() {
    }
    
    @Nullable
    public final Object analyzeContent(@Nullable final String body, @NotNull final Book book, @NotNull final BookChapter bookChapter, @NotNull final BookSource bookSource, @NotNull final String baseUrl, @NotNull final String redirectUrl, @Nullable final String nextChapterUrl, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super String> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: instanceof      Lio/legado/app/model/webBook/BookContent$analyzeContent$1;
        //     5: ifeq            41
        //     8: aload           9
        //    10: checkcast       Lio/legado/app/model/webBook/BookContent$analyzeContent$1;
        //    13: astore          27
        //    15: aload           27
        //    17: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.label:I
        //    20: ldc             -2147483648
        //    22: iand           
        //    23: ifeq            41
        //    26: aload           27
        //    28: dup            
        //    29: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.label:I
        //    32: ldc             -2147483648
        //    34: isub           
        //    35: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.label:I
        //    38: goto            53
        //    41: new             Lio/legado/app/model/webBook/BookContent$analyzeContent$1;
        //    44: dup            
        //    45: aload_0        
        //    46: aload           9
        //    48: invokespecial   io/legado/app/model/webBook/BookContent$analyzeContent$1.<init>:(Lio/legado/app/model/webBook/BookContent;Lkotlin/coroutines/Continuation;)V
        //    51: astore          $continuation
        //    53: aload           $continuation
        //    55: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.result:Ljava/lang/Object;
        //    58: astore          $result
        //    60: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    63: astore          28
        //    65: aload           $continuation
        //    67: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.label:I
        //    70: tableswitch {
        //                0: 96
        //                1: 699
        //                2: 1219
        //          default: 1684
        //        }
        //    96: aload           $result
        //    98: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   101: aload_1         /* body */
        //   102: astore          10
        //   104: aload           10
        //   106: ifnonnull       119
        //   109: new             Ljava/lang/Exception;
        //   112: dup            
        //   113: ldc             "error_get_web_content"
        //   115: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   118: athrow         
        //   119: aload           debugLog
        //   121: astore          10
        //   123: aload           10
        //   125: ifnonnull       131
        //   128: goto            151
        //   131: aload           10
        //   133: aload           bookSource
        //   135: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   138: ldc             "\u2261\u83b7\u53d6\u6210\u529f:"
        //   140: aload           baseUrl
        //   142: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   145: iconst_0       
        //   146: iconst_4       
        //   147: aconst_null    
        //   148: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   151: aload           nextChapterUrl
        //   153: checkcast       Ljava/lang/CharSequence;
        //   156: astore          11
        //   158: iconst_0       
        //   159: istore          12
        //   161: iconst_0       
        //   162: istore          13
        //   164: aload           11
        //   166: ifnull          179
        //   169: aload           11
        //   171: invokeinterface java/lang/CharSequence.length:()I
        //   176: ifne            183
        //   179: iconst_1       
        //   180: goto            184
        //   183: iconst_0       
        //   184: ifne            192
        //   187: aload           nextChapterUrl
        //   189: goto            196
        //   192: aconst_null    
        //   193: checkcast       Ljava/lang/String;
        //   196: astore          mNextChapterUrl
        //   198: new             Ljava/lang/StringBuilder;
        //   201: dup            
        //   202: invokespecial   java/lang/StringBuilder.<init>:()V
        //   205: astore          content
        //   207: iconst_1       
        //   208: anewarray       Ljava/lang/String;
        //   211: astore          13
        //   213: aload           13
        //   215: iconst_0       
        //   216: aload           redirectUrl
        //   218: aastore        
        //   219: aload           13
        //   221: invokestatic    kotlin/collections/CollectionsKt.arrayListOf:([Ljava/lang/Object;)Ljava/util/ArrayList;
        //   224: astore          nextUrlList
        //   226: aload           bookSource
        //   228: invokevirtual   io/legado/app/data/entities/BookSource.getContentRule:()Lio/legado/app/data/entities/rule/ContentRule;
        //   231: astore          contentRule
        //   233: new             Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   236: dup            
        //   237: aload_2         /* book */
        //   238: checkcast       Lio/legado/app/model/analyzeRule/RuleDataInterface;
        //   241: aload           bookSource
        //   243: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   246: aload           debugLog
        //   248: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule.<init>:(Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/DebugLog;)V
        //   251: aload_1         /* body */
        //   252: aload           baseUrl
        //   254: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setContent:(Ljava/lang/Object;Ljava/lang/String;)Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   257: astore          analyzeRule
        //   259: aload           analyzeRule
        //   261: aload           redirectUrl
        //   263: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setRedirectUrl:(Ljava/lang/String;)Ljava/net/URL;
        //   266: pop            
        //   267: aload           analyzeRule
        //   269: aload_3         /* bookChapter */
        //   270: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setChapter:(Lio/legado/app/data/entities/BookChapter;)V
        //   273: aload           analyzeRule
        //   275: aload           mNextChapterUrl
        //   277: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setNextChapterUrl:(Ljava/lang/String;)V
        //   280: aload           $continuation
        //   282: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   287: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //   290: new             Lkotlin/jvm/internal/Ref$ObjectRef;
        //   293: dup            
        //   294: invokespecial   kotlin/jvm/internal/Ref$ObjectRef.<init>:()V
        //   297: astore          contentData
        //   299: aload           contentData
        //   301: aload_0         /* this */
        //   302: aload_2         /* book */
        //   303: aload           baseUrl
        //   305: aload           redirectUrl
        //   307: aload_1         /* body */
        //   308: aload           contentRule
        //   310: aload_3         /* bookChapter */
        //   311: aload           bookSource
        //   313: aload           mNextChapterUrl
        //   315: iconst_0       
        //   316: aload           debugLog
        //   318: sipush          256
        //   321: aconst_null    
        //   322: invokestatic    io/legado/app/model/webBook/BookContent.analyzeContent$default:(Lio/legado/app/model/webBook/BookContent;Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/rule/ContentRule;Lio/legado/app/data/entities/BookChapter;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;ZLio/legado/app/model/DebugLog;ILjava/lang/Object;)Lkotlin/Pair;
        //   325: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   328: aload           content
        //   330: aload           contentData
        //   332: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   335: checkcast       Lkotlin/Pair;
        //   338: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //   341: checkcast       Ljava/lang/String;
        //   344: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   347: pop            
        //   348: aload           contentData
        //   350: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   353: checkcast       Lkotlin/Pair;
        //   356: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   359: checkcast       Ljava/util/List;
        //   362: invokeinterface java/util/List.size:()I
        //   367: iconst_1       
        //   368: if_icmpne       1041
        //   371: new             Lkotlin/jvm/internal/Ref$ObjectRef;
        //   374: dup            
        //   375: invokespecial   kotlin/jvm/internal/Ref$ObjectRef.<init>:()V
        //   378: astore          nextUrl
        //   380: aload           nextUrl
        //   382: aload           contentData
        //   384: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   387: checkcast       Lkotlin/Pair;
        //   390: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   393: checkcast       Ljava/util/List;
        //   396: iconst_0       
        //   397: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   402: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   405: aload           nextUrl
        //   407: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   410: checkcast       Ljava/lang/CharSequence;
        //   413: astore          17
        //   415: iconst_0       
        //   416: istore          18
        //   418: aload           17
        //   420: invokeinterface java/lang/CharSequence.length:()I
        //   425: ifle            432
        //   428: iconst_1       
        //   429: goto            433
        //   432: iconst_0       
        //   433: ifeq            999
        //   436: aload           nextUrlList
        //   438: aload           nextUrl
        //   440: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   443: invokevirtual   java/util/ArrayList.contains:(Ljava/lang/Object;)Z
        //   446: ifne            999
        //   449: aload           mNextChapterUrl
        //   451: checkcast       Ljava/lang/CharSequence;
        //   454: astore          17
        //   456: iconst_0       
        //   457: istore          18
        //   459: iconst_0       
        //   460: istore          19
        //   462: aload           17
        //   464: ifnull          477
        //   467: aload           17
        //   469: invokeinterface java/lang/CharSequence.length:()I
        //   474: ifne            481
        //   477: iconst_1       
        //   478: goto            482
        //   481: iconst_0       
        //   482: ifne            520
        //   485: getstatic       io/legado/app/utils/NetworkUtils.INSTANCE:Lio/legado/app/utils/NetworkUtils;
        //   488: aload           redirectUrl
        //   490: aload           nextUrl
        //   492: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   495: checkcast       Ljava/lang/String;
        //   498: invokevirtual   io/legado/app/utils/NetworkUtils.getAbsoluteURL:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   501: getstatic       io/legado/app/utils/NetworkUtils.INSTANCE:Lio/legado/app/utils/NetworkUtils;
        //   504: aload           redirectUrl
        //   506: aload           mNextChapterUrl
        //   508: invokevirtual   io/legado/app/utils/NetworkUtils.getAbsoluteURL:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   511: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   514: ifeq            520
        //   517: goto            999
        //   520: aload           nextUrlList
        //   522: aload           nextUrl
        //   524: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   527: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   530: pop            
        //   531: aload           $continuation
        //   533: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   538: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //   541: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl;
        //   544: dup            
        //   545: aload           nextUrl
        //   547: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   550: checkcast       Ljava/lang/String;
        //   553: aconst_null    
        //   554: aconst_null    
        //   555: aconst_null    
        //   556: aconst_null    
        //   557: aconst_null    
        //   558: aload           bookSource
        //   560: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   563: aload_2         /* book */
        //   564: checkcast       Lio/legado/app/model/analyzeRule/RuleDataInterface;
        //   567: aconst_null    
        //   568: aload           bookSource
        //   570: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   573: iconst_0       
        //   574: iconst_1       
        //   575: aconst_null    
        //   576: invokestatic    io/legado/app/data/entities/BaseSource$DefaultImpls.getHeaderMap$default:(Lio/legado/app/data/entities/BaseSource;ZILjava/lang/Object;)Ljava/util/HashMap;
        //   579: checkcast       Ljava/util/Map;
        //   582: aload           debugLog
        //   584: sipush          318
        //   587: aconst_null    
        //   588: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   591: aconst_null    
        //   592: aconst_null    
        //   593: iconst_0       
        //   594: aload           $continuation
        //   596: bipush          7
        //   598: aconst_null    
        //   599: aload           $continuation
        //   601: aload_2         /* book */
        //   602: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$0:Ljava/lang/Object;
        //   605: aload           $continuation
        //   607: aload_3         /* bookChapter */
        //   608: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$1:Ljava/lang/Object;
        //   611: aload           $continuation
        //   613: aload           bookSource
        //   615: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$2:Ljava/lang/Object;
        //   618: aload           $continuation
        //   620: aload           redirectUrl
        //   622: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$3:Ljava/lang/Object;
        //   625: aload           $continuation
        //   627: aload           debugLog
        //   629: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$4:Ljava/lang/Object;
        //   632: aload           $continuation
        //   634: aload           mNextChapterUrl
        //   636: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$5:Ljava/lang/Object;
        //   639: aload           $continuation
        //   641: aload           content
        //   643: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$6:Ljava/lang/Object;
        //   646: aload           $continuation
        //   648: aload           nextUrlList
        //   650: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$7:Ljava/lang/Object;
        //   653: aload           $continuation
        //   655: aload           contentRule
        //   657: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$8:Ljava/lang/Object;
        //   660: aload           $continuation
        //   662: aload           analyzeRule
        //   664: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$9:Ljava/lang/Object;
        //   667: aload           $continuation
        //   669: aload           contentData
        //   671: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$10:Ljava/lang/Object;
        //   674: aload           $continuation
        //   676: aload           nextUrl
        //   678: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$11:Ljava/lang/Object;
        //   681: aload           $continuation
        //   683: iconst_1       
        //   684: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.label:I
        //   687: invokestatic    io/legado/app/model/analyzeRule/AnalyzeUrl.getStrResponseAwait$default:(Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   690: dup            
        //   691: aload           28
        //   693: if_acmpne       824
        //   696: aload           28
        //   698: areturn        
        //   699: aload           $continuation
        //   701: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$11:Ljava/lang/Object;
        //   704: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   707: astore          16
        //   709: aload           $continuation
        //   711: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$10:Ljava/lang/Object;
        //   714: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   717: astore          15
        //   719: aload           $continuation
        //   721: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$9:Ljava/lang/Object;
        //   724: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   727: astore          14
        //   729: aload           $continuation
        //   731: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$8:Ljava/lang/Object;
        //   734: checkcast       Lio/legado/app/data/entities/rule/ContentRule;
        //   737: astore          13
        //   739: aload           $continuation
        //   741: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$7:Ljava/lang/Object;
        //   744: checkcast       Ljava/util/ArrayList;
        //   747: astore          12
        //   749: aload           $continuation
        //   751: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$6:Ljava/lang/Object;
        //   754: checkcast       Ljava/lang/StringBuilder;
        //   757: astore          11
        //   759: aload           $continuation
        //   761: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$5:Ljava/lang/Object;
        //   764: checkcast       Ljava/lang/String;
        //   767: astore          10
        //   769: aload           $continuation
        //   771: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$4:Ljava/lang/Object;
        //   774: checkcast       Lio/legado/app/model/DebugLog;
        //   777: astore          8
        //   779: aload           $continuation
        //   781: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$3:Ljava/lang/Object;
        //   784: checkcast       Ljava/lang/String;
        //   787: astore          6
        //   789: aload           $continuation
        //   791: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$2:Ljava/lang/Object;
        //   794: checkcast       Lio/legado/app/data/entities/BookSource;
        //   797: astore          4
        //   799: aload           $continuation
        //   801: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$1:Ljava/lang/Object;
        //   804: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   807: astore_3       
        //   808: aload           $continuation
        //   810: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$0:Ljava/lang/Object;
        //   813: checkcast       Lio/legado/app/data/entities/Book;
        //   816: astore_2       
        //   817: aload           $result
        //   819: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   822: aload           $result
        //   824: checkcast       Lio/legado/app/help/http/StrResponse;
        //   827: astore          res
        //   829: aload           res
        //   831: invokevirtual   io/legado/app/help/http/StrResponse.getBody:()Ljava/lang/String;
        //   834: astore          18
        //   836: aload           18
        //   838: ifnonnull       844
        //   841: goto            405
        //   844: aload           18
        //   846: astore          19
        //   848: iconst_0       
        //   849: istore          20
        //   851: iconst_0       
        //   852: istore          21
        //   854: aload           19
        //   856: astore          nextBody
        //   858: iconst_0       
        //   859: istore          $i$a$-let-BookContent$analyzeContent$2
        //   861: aload           15
        //   863: getstatic       io/legado/app/model/webBook/BookContent.INSTANCE:Lio/legado/app/model/webBook/BookContent;
        //   866: aload_2        
        //   867: aload           16
        //   869: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   872: checkcast       Ljava/lang/String;
        //   875: aload           res
        //   877: invokevirtual   io/legado/app/help/http/StrResponse.getUrl:()Ljava/lang/String;
        //   880: aload           nextBody
        //   882: aload           13
        //   884: aload_3        
        //   885: aload           4
        //   887: aload           10
        //   889: iconst_0       
        //   890: aload           8
        //   892: invokespecial   io/legado/app/model/webBook/BookContent.analyzeContent:(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/rule/ContentRule;Lio/legado/app/data/entities/BookChapter;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;ZLio/legado/app/model/DebugLog;)Lkotlin/Pair;
        //   895: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   898: aload           16
        //   900: aload           15
        //   902: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   905: checkcast       Lkotlin/Pair;
        //   908: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   911: checkcast       Ljava/util/Collection;
        //   914: astore          24
        //   916: iconst_0       
        //   917: istore          25
        //   919: aload           24
        //   921: invokeinterface java/util/Collection.isEmpty:()Z
        //   926: ifne            933
        //   929: iconst_1       
        //   930: goto            934
        //   933: iconst_0       
        //   934: ifeq            963
        //   937: aload           15
        //   939: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   942: checkcast       Lkotlin/Pair;
        //   945: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   948: checkcast       Ljava/util/List;
        //   951: iconst_0       
        //   952: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   957: checkcast       Ljava/lang/String;
        //   960: goto            966
        //   963: ldc_w           ""
        //   966: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   969: aload           11
        //   971: ldc_w           "\n"
        //   974: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   977: aload           15
        //   979: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   982: checkcast       Lkotlin/Pair;
        //   985: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //   988: checkcast       Ljava/lang/String;
        //   991: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   994: pop            
        //   995: nop            
        //   996: goto            405
        //   999: aload           8
        //  1001: astore          17
        //  1003: aload           17
        //  1005: ifnonnull       1011
        //  1008: goto            1286
        //  1011: aload           17
        //  1013: aload           4
        //  1015: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1018: ldc_w           "\u25c7\u672c\u7ae0\u603b\u9875\u6570:"
        //  1021: aload           12
        //  1023: invokevirtual   java/util/ArrayList.size:()I
        //  1026: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1029: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1032: iconst_0       
        //  1033: iconst_4       
        //  1034: aconst_null    
        //  1035: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1038: goto            1286
        //  1041: aload           15
        //  1043: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1046: checkcast       Lkotlin/Pair;
        //  1049: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //  1052: checkcast       Ljava/util/List;
        //  1055: invokeinterface java/util/List.size:()I
        //  1060: iconst_1       
        //  1061: if_icmple       1286
        //  1064: aload           $continuation
        //  1066: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //  1071: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //  1074: aload           8
        //  1076: astore          16
        //  1078: aload           16
        //  1080: ifnonnull       1086
        //  1083: goto            1127
        //  1086: aload           16
        //  1088: aload           4
        //  1090: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1093: ldc_w           "\u25c7\u5e76\u53d1\u89e3\u6790\u6b63\u6587,\u603b\u9875\u6570:"
        //  1096: aload           15
        //  1098: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //  1101: checkcast       Lkotlin/Pair;
        //  1104: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //  1107: checkcast       Ljava/util/List;
        //  1110: invokeinterface java/util/List.size:()I
        //  1115: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1118: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1121: iconst_0       
        //  1122: iconst_4       
        //  1123: aconst_null    
        //  1124: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1127: invokestatic    kotlinx/coroutines/Dispatchers.getIO:()Lkotlinx/coroutines/CoroutineDispatcher;
        //  1130: checkcast       Lkotlin/coroutines/CoroutineContext;
        //  1133: new             Lio/legado/app/model/webBook/BookContent$analyzeContent$3;
        //  1136: dup            
        //  1137: aload           15
        //  1139: aload           4
        //  1141: aload_2        
        //  1142: aload           8
        //  1144: aload           13
        //  1146: aload_3        
        //  1147: aload           10
        //  1149: aload           11
        //  1151: aconst_null    
        //  1152: invokespecial   io/legado/app/model/webBook/BookContent$analyzeContent$3.<init>:(Lkotlin/jvm/internal/Ref$ObjectRef;Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Lio/legado/app/model/DebugLog;Lio/legado/app/data/entities/rule/ContentRule;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Ljava/lang/StringBuilder;Lkotlin/coroutines/Continuation;)V
        //  1155: checkcast       Lkotlin/jvm/functions/Function2;
        //  1158: aload           $continuation
        //  1160: aload           $continuation
        //  1162: aload_3        
        //  1163: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$0:Ljava/lang/Object;
        //  1166: aload           $continuation
        //  1168: aload           4
        //  1170: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$1:Ljava/lang/Object;
        //  1173: aload           $continuation
        //  1175: aload           8
        //  1177: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$2:Ljava/lang/Object;
        //  1180: aload           $continuation
        //  1182: aload           11
        //  1184: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$3:Ljava/lang/Object;
        //  1187: aload           $continuation
        //  1189: aload           13
        //  1191: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$4:Ljava/lang/Object;
        //  1194: aload           $continuation
        //  1196: aload           14
        //  1198: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$5:Ljava/lang/Object;
        //  1201: aload           $continuation
        //  1203: iconst_2       
        //  1204: putfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.label:I
        //  1207: invokestatic    kotlinx/coroutines/BuildersKt.withContext:(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1210: dup            
        //  1211: aload           28
        //  1213: if_acmpne       1285
        //  1216: aload           28
        //  1218: areturn        
        //  1219: aload           $continuation
        //  1221: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$5:Ljava/lang/Object;
        //  1224: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //  1227: astore          14
        //  1229: aload           $continuation
        //  1231: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$4:Ljava/lang/Object;
        //  1234: checkcast       Lio/legado/app/data/entities/rule/ContentRule;
        //  1237: astore          13
        //  1239: aload           $continuation
        //  1241: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$3:Ljava/lang/Object;
        //  1244: checkcast       Ljava/lang/StringBuilder;
        //  1247: astore          11
        //  1249: aload           $continuation
        //  1251: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$2:Ljava/lang/Object;
        //  1254: checkcast       Lio/legado/app/model/DebugLog;
        //  1257: astore          8
        //  1259: aload           $continuation
        //  1261: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$1:Ljava/lang/Object;
        //  1264: checkcast       Lio/legado/app/data/entities/BookSource;
        //  1267: astore          4
        //  1269: aload           $continuation
        //  1271: getfield        io/legado/app/model/webBook/BookContent$analyzeContent$1.L$0:Ljava/lang/Object;
        //  1274: checkcast       Lio/legado/app/data/entities/BookChapter;
        //  1277: astore_3       
        //  1278: aload           $result
        //  1280: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1283: aload           $result
        //  1285: pop            
        //  1286: aload           11
        //  1288: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1291: astore          17
        //  1293: aload           17
        //  1295: ldc_w           "content.toString()"
        //  1298: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1301: aload           17
        //  1303: astore          contentStr
        //  1305: aload           13
        //  1307: invokevirtual   io/legado/app/data/entities/rule/ContentRule.getReplaceRegex:()Ljava/lang/String;
        //  1310: astore          replaceRegex
        //  1312: aload           replaceRegex
        //  1314: checkcast       Ljava/lang/CharSequence;
        //  1317: astore          18
        //  1319: iconst_0       
        //  1320: istore          19
        //  1322: iconst_0       
        //  1323: istore          20
        //  1325: aload           18
        //  1327: ifnull          1340
        //  1330: aload           18
        //  1332: invokeinterface java/lang/CharSequence.length:()I
        //  1337: ifne            1344
        //  1340: iconst_1       
        //  1341: goto            1345
        //  1344: iconst_0       
        //  1345: ifne            1362
        //  1348: aload           14
        //  1350: aload           replaceRegex
        //  1352: aload           contentStr
        //  1354: iconst_0       
        //  1355: iconst_4       
        //  1356: aconst_null    
        //  1357: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.getString$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
        //  1360: astore          contentStr
        //  1362: aload           8
        //  1364: astore          18
        //  1366: aload           18
        //  1368: ifnonnull       1374
        //  1371: goto            1390
        //  1374: aload           18
        //  1376: aload           4
        //  1378: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1381: ldc_w           "\u250c\u83b7\u53d6\u7ae0\u8282\u540d\u79f0"
        //  1384: iconst_0       
        //  1385: iconst_4       
        //  1386: aconst_null    
        //  1387: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1390: aload           8
        //  1392: astore          18
        //  1394: aload           18
        //  1396: ifnonnull       1402
        //  1399: goto            1425
        //  1402: aload           18
        //  1404: aload           4
        //  1406: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1409: ldc_w           "\u2514"
        //  1412: aload_3        
        //  1413: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //  1416: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1419: iconst_0       
        //  1420: iconst_4       
        //  1421: aconst_null    
        //  1422: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1425: aload           8
        //  1427: astore          18
        //  1429: aload           18
        //  1431: ifnonnull       1437
        //  1434: goto            1479
        //  1437: aload           18
        //  1439: aload           4
        //  1441: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1444: new             Ljava/lang/StringBuilder;
        //  1447: dup            
        //  1448: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1451: ldc_w           "\u250c\u83b7\u53d6\u6b63\u6587\u5185\u5bb9 (\u957f\u5ea6\uff1a"
        //  1454: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1457: aload           contentStr
        //  1459: invokevirtual   java/lang/String.length:()I
        //  1462: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1465: bipush          41
        //  1467: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //  1470: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1473: iconst_0       
        //  1474: iconst_4       
        //  1475: aconst_null    
        //  1476: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1479: aload           contentStr
        //  1481: invokevirtual   java/lang/String.length:()I
        //  1484: sipush          300
        //  1487: if_icmple       1648
        //  1490: aload           8
        //  1492: astore          18
        //  1494: aload           18
        //  1496: ifnonnull       1502
        //  1499: goto            1681
        //  1502: aload           18
        //  1504: aload           4
        //  1506: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1509: new             Ljava/lang/StringBuilder;
        //  1512: dup            
        //  1513: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1516: ldc_w           "\u2514\n"
        //  1519: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1522: aload           contentStr
        //  1524: astore          19
        //  1526: iconst_0       
        //  1527: istore          20
        //  1529: sipush          150
        //  1532: istore          21
        //  1534: iconst_0       
        //  1535: istore          22
        //  1537: aload           19
        //  1539: dup            
        //  1540: ifnonnull       1554
        //  1543: new             Ljava/lang/NullPointerException;
        //  1546: dup            
        //  1547: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //  1550: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //  1553: athrow         
        //  1554: iload           20
        //  1556: iload           21
        //  1558: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  1561: dup            
        //  1562: ldc_w           "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //  1565: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1568: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1571: ldc_w           " ... "
        //  1574: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1577: aload           contentStr
        //  1579: astore          19
        //  1581: aload           contentStr
        //  1583: invokevirtual   java/lang/String.length:()I
        //  1586: sipush          150
        //  1589: isub           
        //  1590: istore          20
        //  1592: aload           contentStr
        //  1594: invokevirtual   java/lang/String.length:()I
        //  1597: istore          21
        //  1599: iconst_0       
        //  1600: istore          22
        //  1602: aload           19
        //  1604: dup            
        //  1605: ifnonnull       1619
        //  1608: new             Ljava/lang/NullPointerException;
        //  1611: dup            
        //  1612: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //  1615: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //  1618: athrow         
        //  1619: iload           20
        //  1621: iload           21
        //  1623: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  1626: dup            
        //  1627: ldc_w           "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //  1630: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //  1633: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1636: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1639: iconst_0       
        //  1640: iconst_4       
        //  1641: aconst_null    
        //  1642: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1645: goto            1681
        //  1648: aload           8
        //  1650: astore          18
        //  1652: aload           18
        //  1654: ifnonnull       1660
        //  1657: goto            1681
        //  1660: aload           18
        //  1662: aload           4
        //  1664: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1667: ldc_w           "\u2514\n"
        //  1670: aload           contentStr
        //  1672: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1675: iconst_0       
        //  1676: iconst_4       
        //  1677: aconst_null    
        //  1678: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1681: aload           contentStr
        //  1683: areturn        
        //  1684: new             Ljava/lang/IllegalStateException;
        //  1687: dup            
        //  1688: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1691: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1694: athrow         
        //    Signature:
        //  (Ljava/lang/String;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation<-Ljava/lang/String;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name            Flags  
        //  --------------  -----
        //  body            
        //  book            
        //  bookChapter     
        //  bookSource      
        //  baseUrl         
        //  redirectUrl     
        //  nextChapterUrl  
        //  debugLog        
        //  $completion     
        //    StackMapTable: 00 32 29 FF 00 0B 00 1C 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 1A 00 00 FF 00 2A 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 16 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 0B 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 F5 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 13 FF 00 1B 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 F5 07 00 4F 01 01 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 03 40 01 07 43 07 00 55 FF 00 D0 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 1A 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 4F 01 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 40 01 FF 00 2B 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 4F 01 01 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 03 40 01 25 FF 00 B2 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 7C 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 01 07 00 04 FF 00 13 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 FB 07 00 55 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 58 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 FB 07 00 55 07 00 55 01 01 07 00 55 01 07 01 08 01 07 00 04 07 00 1A 07 00 04 00 01 07 00 88 FF 00 00 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 FB 07 00 55 07 00 55 01 01 07 00 55 01 07 01 08 01 07 00 04 07 00 1A 07 00 04 00 02 07 00 88 01 5C 07 00 88 FF 00 02 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 FB 07 00 55 07 00 55 01 01 07 00 55 01 07 01 08 01 07 00 04 07 00 1A 07 00 04 00 02 07 00 88 07 00 55 FF 00 20 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 4F 01 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 0B 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 88 07 00 F5 01 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 1D 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 2C 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 07 00 55 07 00 57 07 00 A7 07 00 F3 07 00 64 07 00 88 07 00 F5 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 28 FF 00 5B 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 41 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 01 07 00 04 00 FF 00 35 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 07 00 55 07 00 55 07 00 4F 01 01 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 03 40 01 10 FF 00 0B 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 07 00 55 07 00 55 07 00 F5 01 01 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 0F 0B 16 0B 29 16 FF 00 33 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 07 00 55 07 00 55 07 00 F5 07 00 55 01 01 01 00 00 00 07 00 04 07 00 1A 07 00 04 00 04 07 00 F5 07 00 55 07 00 57 07 00 55 FF 00 40 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 07 00 55 07 00 55 07 00 F5 07 00 55 01 01 01 00 00 00 07 00 04 07 00 1A 07 00 04 00 04 07 00 F5 07 00 55 07 00 57 07 00 55 FF 00 1C 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 07 00 55 07 00 55 07 00 F5 01 01 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 0B FF 00 14 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 07 00 57 00 07 00 F3 07 00 64 00 07 00 55 07 00 55 07 00 F5 00 01 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00 FF 00 02 00 1D 07 00 02 07 00 55 07 00 F9 07 00 F7 07 00 3B 07 00 55 07 00 55 07 00 55 07 00 F5 07 00 7C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1A 07 00 04 00 00
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
    
    private final Pair<String, List<String>> analyzeContent(final Book book, final String baseUrl, final String redirectUrl, final String body, final ContentRule contentRule, final BookChapter chapter, final BookSource bookSource, final String nextChapterUrl, final boolean printLog, final DebugLog debugLog) throws Exception {
        final AnalyzeRule analyzeRule = new AnalyzeRule((RuleDataInterface)book, (BaseSource)bookSource, debugLog);
        analyzeRule.setContent(body, baseUrl);
        analyzeRule.setChapter(chapter);
        final URL rUrl = analyzeRule.setRedirectUrl(redirectUrl);
        analyzeRule.setNextChapterUrl(nextChapterUrl);
        final ArrayList nextUrlList = new ArrayList();
        analyzeRule.setChapter(chapter);
        String content = AnalyzeRule.getString$default(analyzeRule, contentRule.getContent(), null, false, 6, null);
        content = HtmlFormatter.INSTANCE.formatKeepImg(content, rUrl);
        final String nextUrlRule = contentRule.getNextContentUrl();
        final CharSequence charSequence = nextUrlRule;
        if (charSequence != null && charSequence.length() != 0) {
            if (printLog) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u6b63\u6587\u4e0b\u4e00\u9875\u94fe\u63a5", false, 4, (Object)null);
                }
            }
            final List stringList$default = AnalyzeRule.getStringList$default(analyzeRule, nextUrlRule, null, true, 2, null);
            if (stringList$default != null) {
                final List it = stringList$default;
                final int n = 0;
                nextUrlList.addAll(it);
            }
            if (printLog) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)CollectionsKt.joinToString$default((Iterable)nextUrlList, (CharSequence)"\uff0c", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null)), false, 4, (Object)null);
                }
            }
        }
        return (Pair<String, List<String>>)new Pair((Object)content, (Object)nextUrlList);
    }
    
    static {
        INSTANCE = new BookContent();
    }
}
