// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.webBook;

import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.StringExtensionsKt;
import io.legado.app.utils.StringUtils;
import kotlin.jvm.functions.Function1;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.help.BookHelp;
import io.legado.app.model.DebugLog$DefaultImpls;
import kotlinx.coroutines.JobKt;
import java.util.List;
import kotlin.text.StringsKt;
import kotlin.Unit;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.data.entities.Book;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import java.util.ArrayList;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.SearchBook;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.data.entities.BookSource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002Ja\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0013JQ\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u00e1\u0001\u0010\u0019\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001b\u001a\u00020\u00102\u0010\u0010\u001c\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010\u001f\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010 \u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010!\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010\"\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010#\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010$\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010%\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010&\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006'" }, d2 = { "Lio/legado/app/model/webBook/BookList;", "", "()V", "analyzeBookList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/SearchBook;", "Lkotlin/collections/ArrayList;", "body", "", "bookSource", "Lio/legado/app/data/entities/BookSource;", "analyzeUrl", "Lio/legado/app/model/analyzeRule/AnalyzeUrl;", "baseUrl", "variableBook", "isSearch", "", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Lio/legado/app/data/entities/SearchBook;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getInfoItem", "analyzeRule", "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "variable", "(Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSearchItem", "item", "log", "ruleName", "", "Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;", "ruleBookUrl", "ruleAuthor", "ruleKind", "ruleCoverUrl", "ruleWordCount", "ruleIntro", "ruleLastChapter", "(Ljava/lang/Object;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class BookList
{
    @NotNull
    public static final BookList INSTANCE;
    
    private BookList() {
    }
    
    @Nullable
    public final Object analyzeBookList(@Nullable final String body, @NotNull final BookSource bookSource, @NotNull final AnalyzeUrl analyzeUrl, @NotNull final String baseUrl, @NotNull final SearchBook variableBook, final boolean isSearch, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super ArrayList<SearchBook>> $completion) throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: instanceof      Lio/legado/app/model/webBook/BookList$analyzeBookList$1;
        //     5: ifeq            41
        //     8: aload           8
        //    10: checkcast       Lio/legado/app/model/webBook/BookList$analyzeBookList$1;
        //    13: astore          34
        //    15: aload           34
        //    17: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //    20: ldc             -2147483648
        //    22: iand           
        //    23: ifeq            41
        //    26: aload           34
        //    28: dup            
        //    29: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //    32: ldc             -2147483648
        //    34: isub           
        //    35: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //    38: goto            53
        //    41: new             Lio/legado/app/model/webBook/BookList$analyzeBookList$1;
        //    44: dup            
        //    45: aload_0        
        //    46: aload           8
        //    48: invokespecial   io/legado/app/model/webBook/BookList$analyzeBookList$1.<init>:(Lio/legado/app/model/webBook/BookList;Lkotlin/coroutines/Continuation;)V
        //    51: astore          $continuation
        //    53: aload           $continuation
        //    55: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.result:Ljava/lang/Object;
        //    58: astore          $result
        //    60: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    63: astore          35
        //    65: aload           $continuation
        //    67: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //    70: tableswitch {
        //                0: 100
        //                1: 377
        //                2: 835
        //                3: 1341
        //          default: 1609
        //        }
        //   100: aload           $result
        //   102: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   105: new             Ljava/util/ArrayList;
        //   108: dup            
        //   109: invokespecial   java/util/ArrayList.<init>:()V
        //   112: astore          bookList
        //   114: aload_1         /* body */
        //   115: astore          10
        //   117: aload           10
        //   119: ifnonnull       132
        //   122: new             Ljava/lang/Exception;
        //   125: dup            
        //   126: ldc             "error_get_web_content"
        //   128: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   131: athrow         
        //   132: aload           10
        //   134: pop            
        //   135: aload           debugLog
        //   137: astore          10
        //   139: aload           10
        //   141: ifnonnull       148
        //   144: aconst_null    
        //   145: goto            172
        //   148: aload           10
        //   150: aload_2         /* bookSource */
        //   151: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   154: ldc             "\u2261\u83b7\u53d6\u6210\u529f:"
        //   156: aload_3         /* analyzeUrl */
        //   157: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getRuleUrl:()Ljava/lang/String;
        //   160: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   163: iconst_0       
        //   164: iconst_4       
        //   165: aconst_null    
        //   166: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   169: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   172: pop            
        //   173: new             Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   176: dup            
        //   177: aload           variableBook
        //   179: checkcast       Lio/legado/app/model/analyzeRule/RuleDataInterface;
        //   182: aload_2         /* bookSource */
        //   183: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   186: aload           debugLog
        //   188: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule.<init>:(Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/DebugLog;)V
        //   191: astore          analyzeRule
        //   193: aload           analyzeRule
        //   195: aload_1         /* body */
        //   196: aconst_null    
        //   197: iconst_2       
        //   198: aconst_null    
        //   199: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.setContent$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   202: aload           baseUrl
        //   204: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setBaseUrl:(Ljava/lang/String;)Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //   207: pop            
        //   208: aload           analyzeRule
        //   210: aload           baseUrl
        //   212: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.setRedirectUrl:(Ljava/lang/String;)Ljava/net/URL;
        //   215: pop            
        //   216: aload_2         /* bookSource */
        //   217: invokevirtual   io/legado/app/data/entities/BookSource.getBookUrlPattern:()Ljava/lang/String;
        //   220: astore          11
        //   222: aload           11
        //   224: ifnonnull       231
        //   227: aconst_null    
        //   228: goto            461
        //   231: aload           11
        //   233: astore          12
        //   235: iconst_0       
        //   236: istore          13
        //   238: iconst_0       
        //   239: istore          14
        //   241: aload           12
        //   243: astore          it
        //   245: iconst_0       
        //   246: istore          $i$a$-let-BookList$analyzeBookList$2
        //   248: aload           $continuation
        //   250: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   255: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //   258: aload           baseUrl
        //   260: checkcast       Ljava/lang/CharSequence;
        //   263: astore          17
        //   265: aload           it
        //   267: astore          18
        //   269: iconst_0       
        //   270: istore          19
        //   272: new             Lkotlin/text/Regex;
        //   275: dup            
        //   276: aload           18
        //   278: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //   281: astore          18
        //   283: iconst_0       
        //   284: istore          19
        //   286: aload           18
        //   288: aload           17
        //   290: invokevirtual   kotlin/text/Regex.matches:(Ljava/lang/CharSequence;)Z
        //   293: ifeq            457
        //   296: aload           debugLog
        //   298: astore          17
        //   300: aload           17
        //   302: ifnonnull       309
        //   305: aconst_null    
        //   306: goto            326
        //   309: aload           17
        //   311: aload_2         /* bookSource */
        //   312: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   315: ldc             "\u2261\u94fe\u63a5\u4e3a\u8be6\u60c5\u9875"
        //   317: iconst_0       
        //   318: iconst_4       
        //   319: aconst_null    
        //   320: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   323: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   326: pop            
        //   327: getstatic       io/legado/app/model/webBook/BookList.INSTANCE:Lio/legado/app/model/webBook/BookList;
        //   330: aload_1         /* body */
        //   331: aload           analyzeRule
        //   333: aload_2         /* bookSource */
        //   334: aload_3         /* analyzeUrl */
        //   335: aload           baseUrl
        //   337: aload           variableBook
        //   339: invokevirtual   io/legado/app/data/entities/SearchBook.getVariable:()Ljava/lang/String;
        //   342: aload           debugLog
        //   344: aload           $continuation
        //   346: aload           $continuation
        //   348: aload_1         /* body */
        //   349: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$0:Ljava/lang/Object;
        //   352: aload           $continuation
        //   354: aload           bookList
        //   356: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$1:Ljava/lang/Object;
        //   359: aload           $continuation
        //   361: iconst_1       
        //   362: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //   365: invokespecial   io/legado/app/model/webBook/BookList.getInfoItem:(Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   368: dup            
        //   369: aload           35
        //   371: if_acmpne       406
        //   374: aload           35
        //   376: areturn        
        //   377: iconst_0       
        //   378: istore          $i$a$-let-BookList$analyzeBookList$2
        //   380: aload           $continuation
        //   382: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$1:Ljava/lang/Object;
        //   385: checkcast       Ljava/util/ArrayList;
        //   388: astore          9
        //   390: aload           $continuation
        //   392: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$0:Ljava/lang/Object;
        //   395: checkcast       Ljava/lang/String;
        //   398: astore_1       
        //   399: aload           $result
        //   401: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   404: aload           $result
        //   406: checkcast       Lio/legado/app/data/entities/SearchBook;
        //   409: astore          17
        //   411: aload           17
        //   413: ifnonnull       420
        //   416: aconst_null    
        //   417: goto            453
        //   420: aload           17
        //   422: astore          18
        //   424: iconst_0       
        //   425: istore          19
        //   427: iconst_0       
        //   428: istore          20
        //   430: aload           18
        //   432: astore          searchBook
        //   434: iconst_0       
        //   435: istore          $i$a$-let-BookList$analyzeBookList$2$1
        //   437: aload           searchBook
        //   439: aload_1        
        //   440: invokevirtual   io/legado/app/data/entities/SearchBook.setInfoHtml:(Ljava/lang/String;)V
        //   443: aload           9
        //   445: aload           searchBook
        //   447: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   450: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxBoolean:(Z)Ljava/lang/Boolean;
        //   453: pop            
        //   454: aload           9
        //   456: areturn        
        //   457: nop            
        //   458: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   461: pop            
        //   462: aconst_null    
        //   463: astore          11
        //   465: iconst_0       
        //   466: istore          reverse
        //   468: nop            
        //   469: iload           6
        //   471: ifeq            484
        //   474: aload_2        
        //   475: invokevirtual   io/legado/app/data/entities/BookSource.getSearchRule:()Lio/legado/app/data/entities/rule/SearchRule;
        //   478: checkcast       Lio/legado/app/data/entities/rule/BookListRule;
        //   481: goto            540
        //   484: aload_2        
        //   485: invokevirtual   io/legado/app/data/entities/BookSource.getExploreRule:()Lio/legado/app/data/entities/rule/ExploreRule;
        //   488: invokevirtual   io/legado/app/data/entities/rule/ExploreRule.getBookList:()Ljava/lang/String;
        //   491: checkcast       Ljava/lang/CharSequence;
        //   494: astore          14
        //   496: iconst_0       
        //   497: istore          15
        //   499: iconst_0       
        //   500: istore          16
        //   502: aload           14
        //   504: ifnull          515
        //   507: aload           14
        //   509: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   512: ifeq            519
        //   515: iconst_1       
        //   516: goto            520
        //   519: iconst_0       
        //   520: ifeq            533
        //   523: aload_2        
        //   524: invokevirtual   io/legado/app/data/entities/BookSource.getSearchRule:()Lio/legado/app/data/entities/rule/SearchRule;
        //   527: checkcast       Lio/legado/app/data/entities/rule/BookListRule;
        //   530: goto            540
        //   533: aload_2        
        //   534: invokevirtual   io/legado/app/data/entities/BookSource.getExploreRule:()Lio/legado/app/data/entities/rule/ExploreRule;
        //   537: checkcast       Lio/legado/app/data/entities/rule/BookListRule;
        //   540: astore          bookListRule
        //   542: aload           bookListRule
        //   544: invokeinterface io/legado/app/data/entities/rule/BookListRule.getBookList:()Ljava/lang/String;
        //   549: astore          15
        //   551: aload           15
        //   553: ifnonnull       561
        //   556: ldc             ""
        //   558: goto            563
        //   561: aload           15
        //   563: astore          ruleList
        //   565: aload           ruleList
        //   567: ldc             "-"
        //   569: iconst_0       
        //   570: iconst_2       
        //   571: aconst_null    
        //   572: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //   575: ifeq            606
        //   578: iconst_1       
        //   579: istore          reverse
        //   581: aload           ruleList
        //   583: astore          15
        //   585: iconst_1       
        //   586: istore          16
        //   588: iconst_0       
        //   589: istore          17
        //   591: aload           15
        //   593: iload           16
        //   595: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   598: dup            
        //   599: ldc             "(this as java.lang.String).substring(startIndex)"
        //   601: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   604: astore          ruleList
        //   606: aload           ruleList
        //   608: ldc             "+"
        //   610: iconst_0       
        //   611: iconst_2       
        //   612: aconst_null    
        //   613: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //   616: ifeq            658
        //   619: aload           ruleList
        //   621: astore          15
        //   623: iconst_1       
        //   624: istore          16
        //   626: iconst_0       
        //   627: istore          17
        //   629: aload           15
        //   631: dup            
        //   632: ifnonnull       645
        //   635: new             Ljava/lang/NullPointerException;
        //   638: dup            
        //   639: ldc             "null cannot be cast to non-null type java.lang.String"
        //   641: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   644: athrow         
        //   645: iload           16
        //   647: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   650: dup            
        //   651: ldc             "(this as java.lang.String).substring(startIndex)"
        //   653: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   656: astore          ruleList
        //   658: aload           7
        //   660: astore          15
        //   662: aload           15
        //   664: ifnonnull       671
        //   667: aconst_null    
        //   668: goto            688
        //   671: aload           15
        //   673: aload_2        
        //   674: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   677: ldc             "\u250c\u83b7\u53d6\u4e66\u7c4d\u5217\u8868"
        //   679: iconst_0       
        //   680: iconst_4       
        //   681: aconst_null    
        //   682: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   685: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   688: pop            
        //   689: aload           analyzeRule
        //   691: aload           ruleList
        //   693: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule.getElements:(Ljava/lang/String;)Ljava/util/List;
        //   696: astore          collections
        //   698: aload           $continuation
        //   700: invokeinterface kotlin/coroutines/Continuation.getContext:()Lkotlin/coroutines/CoroutineContext;
        //   705: invokestatic    kotlinx/coroutines/JobKt.ensureActive:(Lkotlin/coroutines/CoroutineContext;)V
        //   708: aload           collections
        //   710: invokeinterface java/util/List.isEmpty:()Z
        //   715: ifeq            912
        //   718: aload_2        
        //   719: invokevirtual   io/legado/app/data/entities/BookSource.getBookUrlPattern:()Ljava/lang/String;
        //   722: checkcast       Ljava/lang/CharSequence;
        //   725: astore          15
        //   727: iconst_0       
        //   728: istore          16
        //   730: iconst_0       
        //   731: istore          17
        //   733: aload           15
        //   735: ifnull          748
        //   738: aload           15
        //   740: invokeinterface java/lang/CharSequence.length:()I
        //   745: ifne            752
        //   748: iconst_1       
        //   749: goto            753
        //   752: iconst_0       
        //   753: ifeq            912
        //   756: aload           7
        //   758: astore          15
        //   760: aload           15
        //   762: ifnonnull       769
        //   765: aconst_null    
        //   766: goto            786
        //   769: aload           15
        //   771: aload_2        
        //   772: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   775: ldc             "\u2514\u5217\u8868\u4e3a\u7a7a,\u6309\u8be6\u60c5\u9875\u89e3\u6790"
        //   777: iconst_0       
        //   778: iconst_4       
        //   779: aconst_null    
        //   780: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //   783: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   786: pop            
        //   787: aload_0        
        //   788: aload_1        
        //   789: aload           analyzeRule
        //   791: aload_2        
        //   792: aload_3        
        //   793: aload           4
        //   795: aload           5
        //   797: invokevirtual   io/legado/app/data/entities/SearchBook.getVariable:()Ljava/lang/String;
        //   800: aload           7
        //   802: aload           $continuation
        //   804: aload           $continuation
        //   806: aload_1        
        //   807: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$0:Ljava/lang/Object;
        //   810: aload           $continuation
        //   812: aload           9
        //   814: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$1:Ljava/lang/Object;
        //   817: aload           $continuation
        //   819: iconst_2       
        //   820: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //   823: invokespecial   io/legado/app/model/webBook/BookList.getInfoItem:(Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   826: dup            
        //   827: aload           35
        //   829: if_acmpne       861
        //   832: aload           35
        //   834: areturn        
        //   835: aload           $continuation
        //   837: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$1:Ljava/lang/Object;
        //   840: checkcast       Ljava/util/ArrayList;
        //   843: astore          9
        //   845: aload           $continuation
        //   847: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$0:Ljava/lang/Object;
        //   850: checkcast       Ljava/lang/String;
        //   853: astore_1       
        //   854: aload           $result
        //   856: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   859: aload           $result
        //   861: checkcast       Lio/legado/app/data/entities/SearchBook;
        //   864: astore          15
        //   866: aload           15
        //   868: ifnonnull       875
        //   871: aconst_null    
        //   872: goto            908
        //   875: aload           15
        //   877: astore          16
        //   879: iconst_0       
        //   880: istore          17
        //   882: iconst_0       
        //   883: istore          18
        //   885: aload           16
        //   887: astore          searchBook
        //   889: iconst_0       
        //   890: istore          $i$a$-let-BookList$analyzeBookList$3
        //   892: aload           searchBook
        //   894: aload_1        
        //   895: invokevirtual   io/legado/app/data/entities/SearchBook.setInfoHtml:(Ljava/lang/String;)V
        //   898: aload           9
        //   900: aload           searchBook
        //   902: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   905: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxBoolean:(Z)Ljava/lang/Boolean;
        //   908: pop            
        //   909: goto            1606
        //   912: aload           10
        //   914: aload           13
        //   916: invokeinterface io/legado/app/data/entities/rule/BookListRule.getName:()Ljava/lang/String;
        //   921: iconst_0       
        //   922: iconst_2       
        //   923: aconst_null    
        //   924: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   927: astore          ruleName
        //   929: aload           10
        //   931: aload           13
        //   933: invokeinterface io/legado/app/data/entities/rule/BookListRule.getBookUrl:()Ljava/lang/String;
        //   938: iconst_0       
        //   939: iconst_2       
        //   940: aconst_null    
        //   941: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   944: astore          ruleBookUrl
        //   946: aload           10
        //   948: aload           13
        //   950: invokeinterface io/legado/app/data/entities/rule/BookListRule.getAuthor:()Ljava/lang/String;
        //   955: iconst_0       
        //   956: iconst_2       
        //   957: aconst_null    
        //   958: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   961: astore          ruleAuthor
        //   963: aload           10
        //   965: aload           13
        //   967: invokeinterface io/legado/app/data/entities/rule/BookListRule.getCoverUrl:()Ljava/lang/String;
        //   972: iconst_0       
        //   973: iconst_2       
        //   974: aconst_null    
        //   975: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   978: astore          ruleCoverUrl
        //   980: aload           10
        //   982: aload           13
        //   984: invokeinterface io/legado/app/data/entities/rule/BookListRule.getIntro:()Ljava/lang/String;
        //   989: iconst_0       
        //   990: iconst_2       
        //   991: aconst_null    
        //   992: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //   995: astore          ruleIntro
        //   997: aload           10
        //   999: aload           13
        //  1001: invokeinterface io/legado/app/data/entities/rule/BookListRule.getKind:()Ljava/lang/String;
        //  1006: iconst_0       
        //  1007: iconst_2       
        //  1008: aconst_null    
        //  1009: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //  1012: astore          ruleKind
        //  1014: aload           10
        //  1016: aload           13
        //  1018: invokeinterface io/legado/app/data/entities/rule/BookListRule.getLastChapter:()Ljava/lang/String;
        //  1023: iconst_0       
        //  1024: iconst_2       
        //  1025: aconst_null    
        //  1026: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //  1029: astore          ruleLastChapter
        //  1031: aload           10
        //  1033: aload           13
        //  1035: invokeinterface io/legado/app/data/entities/rule/BookListRule.getWordCount:()Ljava/lang/String;
        //  1040: iconst_0       
        //  1041: iconst_2       
        //  1042: aconst_null    
        //  1043: invokestatic    io/legado/app/model/analyzeRule/AnalyzeRule.splitSourceRule$default:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;ZILjava/lang/Object;)Ljava/util/List;
        //  1046: astore          ruleWordCount
        //  1048: aload           7
        //  1050: astore          23
        //  1052: aload           23
        //  1054: ifnonnull       1061
        //  1057: aconst_null    
        //  1058: goto            1092
        //  1061: aload           23
        //  1063: aload_2        
        //  1064: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //  1067: ldc_w           "\u2514\u5217\u8868\u5927\u5c0f:"
        //  1070: aload           11
        //  1072: invokeinterface java/util/List.size:()I
        //  1077: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //  1080: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1083: iconst_0       
        //  1084: iconst_4       
        //  1085: aconst_null    
        //  1086: invokestatic    io/legado/app/model/DebugLog$DefaultImpls.log$default:(Lio/legado/app/model/DebugLog;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)V
        //  1089: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1092: pop            
        //  1093: aload           11
        //  1095: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //  1100: astore          23
        //  1102: iconst_0       
        //  1103: istore          24
        //  1105: aload           23
        //  1107: invokeinterface java/util/Iterator.hasNext:()Z
        //  1112: ifeq            1593
        //  1115: iload           24
        //  1117: istore          index
        //  1119: iload           24
        //  1121: iconst_1       
        //  1122: iadd           
        //  1123: istore          24
        //  1125: aload           23
        //  1127: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1132: astore          item
        //  1134: aload           5
        //  1136: invokevirtual   io/legado/app/data/entities/SearchBook.getVariable:()Ljava/lang/String;
        //  1139: astore          28
        //  1141: iload           index
        //  1143: ifne            1150
        //  1146: iconst_1       
        //  1147: goto            1151
        //  1150: iconst_0       
        //  1151: istore          29
        //  1153: aload_0        
        //  1154: aload           item
        //  1156: aload           10
        //  1158: aload_2        
        //  1159: aload           4
        //  1161: aload           28
        //  1163: iload           29
        //  1165: ifeq            1172
        //  1168: iconst_1       
        //  1169: goto            1173
        //  1172: iconst_0       
        //  1173: aload           ruleName
        //  1175: aload           ruleBookUrl
        //  1177: aload           ruleAuthor
        //  1179: aload           ruleKind
        //  1181: aload           ruleCoverUrl
        //  1183: aload           ruleWordCount
        //  1185: aload           ruleIntro
        //  1187: aload           ruleLastChapter
        //  1189: aload           7
        //  1191: aload           $continuation
        //  1193: aload           $continuation
        //  1195: aload_0        
        //  1196: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$0:Ljava/lang/Object;
        //  1199: aload           $continuation
        //  1201: aload_1        
        //  1202: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$1:Ljava/lang/Object;
        //  1205: aload           $continuation
        //  1207: aload_2        
        //  1208: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$2:Ljava/lang/Object;
        //  1211: aload           $continuation
        //  1213: aload           4
        //  1215: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$3:Ljava/lang/Object;
        //  1218: aload           $continuation
        //  1220: aload           5
        //  1222: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$4:Ljava/lang/Object;
        //  1225: aload           $continuation
        //  1227: aload           7
        //  1229: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$5:Ljava/lang/Object;
        //  1232: aload           $continuation
        //  1234: aload           9
        //  1236: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$6:Ljava/lang/Object;
        //  1239: aload           $continuation
        //  1241: aload           10
        //  1243: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$7:Ljava/lang/Object;
        //  1246: aload           $continuation
        //  1248: aload           ruleName
        //  1250: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$8:Ljava/lang/Object;
        //  1253: aload           $continuation
        //  1255: aload           ruleBookUrl
        //  1257: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$9:Ljava/lang/Object;
        //  1260: aload           $continuation
        //  1262: aload           ruleAuthor
        //  1264: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$10:Ljava/lang/Object;
        //  1267: aload           $continuation
        //  1269: aload           ruleCoverUrl
        //  1271: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$11:Ljava/lang/Object;
        //  1274: aload           $continuation
        //  1276: aload           ruleIntro
        //  1278: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$12:Ljava/lang/Object;
        //  1281: aload           $continuation
        //  1283: aload           ruleKind
        //  1285: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$13:Ljava/lang/Object;
        //  1288: aload           $continuation
        //  1290: aload           ruleLastChapter
        //  1292: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$14:Ljava/lang/Object;
        //  1295: aload           $continuation
        //  1297: aload           ruleWordCount
        //  1299: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$15:Ljava/lang/Object;
        //  1302: aload           $continuation
        //  1304: aload           23
        //  1306: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$16:Ljava/lang/Object;
        //  1309: aload           $continuation
        //  1311: iload           12
        //  1313: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.I$0:I
        //  1316: aload           $continuation
        //  1318: iload           24
        //  1320: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.I$1:I
        //  1323: aload           $continuation
        //  1325: iconst_3       
        //  1326: putfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.label:I
        //  1329: invokespecial   io/legado/app/model/webBook/BookList.getSearchItem:(Ljava/lang/Object;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1332: dup            
        //  1333: aload           35
        //  1335: if_acmpne       1529
        //  1338: aload           35
        //  1340: areturn        
        //  1341: aload           $continuation
        //  1343: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.I$1:I
        //  1346: istore          24
        //  1348: aload           $continuation
        //  1350: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.I$0:I
        //  1353: istore          12
        //  1355: aload           $continuation
        //  1357: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$16:Ljava/lang/Object;
        //  1360: checkcast       Ljava/util/Iterator;
        //  1363: astore          23
        //  1365: aload           $continuation
        //  1367: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$15:Ljava/lang/Object;
        //  1370: checkcast       Ljava/util/List;
        //  1373: astore          ruleWordCount
        //  1375: aload           $continuation
        //  1377: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$14:Ljava/lang/Object;
        //  1380: checkcast       Ljava/util/List;
        //  1383: astore          ruleLastChapter
        //  1385: aload           $continuation
        //  1387: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$13:Ljava/lang/Object;
        //  1390: checkcast       Ljava/util/List;
        //  1393: astore          ruleKind
        //  1395: aload           $continuation
        //  1397: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$12:Ljava/lang/Object;
        //  1400: checkcast       Ljava/util/List;
        //  1403: astore          ruleIntro
        //  1405: aload           $continuation
        //  1407: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$11:Ljava/lang/Object;
        //  1410: checkcast       Ljava/util/List;
        //  1413: astore          ruleCoverUrl
        //  1415: aload           $continuation
        //  1417: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$10:Ljava/lang/Object;
        //  1420: checkcast       Ljava/util/List;
        //  1423: astore          ruleAuthor
        //  1425: aload           $continuation
        //  1427: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$9:Ljava/lang/Object;
        //  1430: checkcast       Ljava/util/List;
        //  1433: astore          ruleBookUrl
        //  1435: aload           $continuation
        //  1437: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$8:Ljava/lang/Object;
        //  1440: checkcast       Ljava/util/List;
        //  1443: astore          ruleName
        //  1445: aload           $continuation
        //  1447: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$7:Ljava/lang/Object;
        //  1450: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeRule;
        //  1453: astore          10
        //  1455: aload           $continuation
        //  1457: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$6:Ljava/lang/Object;
        //  1460: checkcast       Ljava/util/ArrayList;
        //  1463: astore          9
        //  1465: aload           $continuation
        //  1467: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$5:Ljava/lang/Object;
        //  1470: checkcast       Lio/legado/app/model/DebugLog;
        //  1473: astore          7
        //  1475: aload           $continuation
        //  1477: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$4:Ljava/lang/Object;
        //  1480: checkcast       Lio/legado/app/data/entities/SearchBook;
        //  1483: astore          5
        //  1485: aload           $continuation
        //  1487: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$3:Ljava/lang/Object;
        //  1490: checkcast       Ljava/lang/String;
        //  1493: astore          4
        //  1495: aload           $continuation
        //  1497: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$2:Ljava/lang/Object;
        //  1500: checkcast       Lio/legado/app/data/entities/BookSource;
        //  1503: astore_2       
        //  1504: aload           $continuation
        //  1506: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$1:Ljava/lang/Object;
        //  1509: checkcast       Ljava/lang/String;
        //  1512: astore_1       
        //  1513: aload           $continuation
        //  1515: getfield        io/legado/app/model/webBook/BookList$analyzeBookList$1.L$0:Ljava/lang/Object;
        //  1518: checkcast       Lio/legado/app/model/webBook/BookList;
        //  1521: astore_0       
        //  1522: aload           $result
        //  1524: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1527: aload           $result
        //  1529: checkcast       Lio/legado/app/data/entities/SearchBook;
        //  1532: astore          27
        //  1534: aload           27
        //  1536: ifnonnull       1543
        //  1539: aconst_null    
        //  1540: goto            1589
        //  1543: aload           27
        //  1545: astore          28
        //  1547: iconst_0       
        //  1548: istore          29
        //  1550: iconst_0       
        //  1551: istore          30
        //  1553: aload           28
        //  1555: astore          searchBook
        //  1557: iconst_0       
        //  1558: istore          $i$a$-let-BookList$analyzeBookList$4
        //  1560: aload           4
        //  1562: aload           searchBook
        //  1564: invokevirtual   io/legado/app/data/entities/SearchBook.getBookUrl:()Ljava/lang/String;
        //  1567: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //  1570: ifeq            1579
        //  1573: aload           searchBook
        //  1575: aload_1        
        //  1576: invokevirtual   io/legado/app/data/entities/SearchBook.setInfoHtml:(Ljava/lang/String;)V
        //  1579: aload           9
        //  1581: aload           searchBook
        //  1583: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1586: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxBoolean:(Z)Ljava/lang/Boolean;
        //  1589: pop            
        //  1590: goto            1105
        //  1593: iload           12
        //  1595: ifeq            1606
        //  1598: aload           9
        //  1600: checkcast       Ljava/util/List;
        //  1603: invokestatic    kotlin/collections/CollectionsKt.reverse:(Ljava/util/List;)V
        //  1606: aload           9
        //  1608: areturn        
        //  1609: new             Ljava/lang/IllegalStateException;
        //  1612: dup            
        //  1613: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1616: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1619: athrow         
        //    Exceptions:
        //  throws java.lang.Exception
        //    Signature:
        //  (Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Lio/legado/app/data/entities/SearchBook;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation<-Ljava/util/ArrayList<Lio/legado/app/data/entities/SearchBook;>;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name          Flags  
        //  ------------  -----
        //  body          
        //  bookSource    
        //  analyzeUrl    
        //  baseUrl       
        //  variableBook  
        //  isSearch      
        //  debugLog      
        //  $completion   
        //    StackMapTable: 00 35 29 FF 00 0B 00 23 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 1B 00 00 FF 00 2E 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 1F 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 9C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 0F 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 01 53 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 57 07 00 56 FF 00 3A 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 9C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 4D 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 9C 07 00 9C 01 01 07 00 9C 01 07 01 53 07 00 82 01 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 50 07 00 56 FF 00 32 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 1C 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 00 04 FF 00 0D 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 00 00 00 00 00 00 01 07 00 8D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 60 07 01 87 FF 00 03 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 9C 07 00 9C 01 01 07 00 9C 01 07 00 80 07 00 82 01 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 03 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 9C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 00 56 FF 00 16 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 1E 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 00 07 00 80 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 03 40 01 0C FF 00 06 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 00 AF FF 00 14 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 07 00 AF 00 07 00 9C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 41 07 00 9C FF 00 2A 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 07 00 AF 07 00 9C 07 00 9C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 26 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 07 00 AF 07 00 9C 07 00 9C 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 00 9C FF 00 0C 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 07 00 AF 07 00 9C 07 00 9C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 0C 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 05 01 07 00 AF 07 00 9C 07 01 53 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 50 07 00 56 FF 00 3B 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 DF 01 07 00 AF 07 00 9C 07 00 80 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 03 40 01 FF 00 0F 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 DF 01 07 00 AF 07 00 9C 07 01 53 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 50 07 00 56 FF 00 30 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 19 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 00 04 FF 00 0D 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 00 00 00 00 00 07 00 8D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 60 07 01 87 FF 00 03 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 DF 01 07 00 AF 07 00 9C 07 00 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 94 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 07 00 DF 01 07 00 AF 07 00 9C 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 53 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 5E 07 00 56 FF 00 0C 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 2C 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 01 07 00 04 00 07 00 9C 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 40 01 FF 00 14 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 01 07 00 04 00 07 00 9C 01 00 00 00 07 00 04 07 00 1B 07 00 04 00 06 07 00 02 07 00 04 07 00 5C 07 00 3D 07 00 9C 07 00 9C FF 00 00 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 01 07 00 04 00 07 00 9C 01 00 00 00 07 00 04 07 00 1B 07 00 04 00 07 07 00 02 07 00 04 07 00 5C 07 00 3D 07 00 9C 07 00 9C 01 FF 00 A7 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 BB 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 00 04 FF 00 0D 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 00 00 07 00 8D 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 23 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 00 00 07 00 8D 07 00 8D 01 01 07 00 8D 01 07 00 04 07 00 1B 07 00 04 00 00 FF 00 09 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 00 00 07 00 8D 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 01 07 01 87 FF 00 03 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 07 00 5C 00 01 00 00 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 00 DF 07 01 14 01 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 0C 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 07 00 35 00 00 00 00 00 07 00 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00 FF 00 02 00 24 07 00 02 07 00 9C 07 00 3D 07 00 45 07 00 9C 07 00 8D 01 07 01 53 07 00 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 1B 07 00 04 00 00
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
    
    private final Object getInfoItem(final String body, final AnalyzeRule analyzeRule, final BookSource bookSource, final AnalyzeUrl analyzeUrl, final String baseUrl, final String variable, final DebugLog debugLog, final Continuation<? super SearchBook> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookList$getInfoItem.BookList$getInfoItem$1) {
                final BookList$getInfoItem.BookList$getInfoItem$1 bookList$getInfoItem$1 = (BookList$getInfoItem.BookList$getInfoItem$1)$completion;
                if ((bookList$getInfoItem$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookList$getInfoItem.BookList$getInfoItem$1 bookList$getInfoItem$2 = bookList$getInfoItem$1;
                    bookList$getInfoItem$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookList$getInfoItem.BookList$getInfoItem$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookList$getInfoItem.BookList$getInfoItem$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Book book = null;
        switch (((BookList$getInfoItem.BookList$getInfoItem$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, variable, null, false, null, -536870913, 1, null);
                book.setBookUrl(analyzeUrl.getRuleUrl());
                book.setOrigin(bookSource.getBookSourceUrl());
                book.setOriginName(bookSource.getBookSourceName());
                book.setOriginOrder(bookSource.getCustomOrder());
                book.setType(bookSource.getBookSourceType());
                book.setUserNameSpace(analyzeRule.getUserNameSpace());
                analyzeRule.setRuleData((RuleDataInterface)book);
                final BookInfo instance = BookInfo.INSTANCE;
                final Book book2 = book;
                final boolean canReName = false;
                final Continuation $completion2 = $continuation;
                ((BookList$getInfoItem.BookList$getInfoItem$1)$continuation).L$0 = book;
                ((BookList$getInfoItem.BookList$getInfoItem$1)$continuation).label = 1;
                if (instance.analyzeBookInfo(book2, body, analyzeRule, bookSource, baseUrl, baseUrl, canReName, debugLog, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                book = (Book)((BookList$getInfoItem.BookList$getInfoItem$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!StringsKt.isBlank((CharSequence)book.getName())) {
            return book.toSearchBook();
        }
        return null;
    }
    
    private final Object getSearchItem(final Object item, final AnalyzeRule analyzeRule, final BookSource bookSource, final String baseUrl, final String variable, final boolean log, final List<AnalyzeRule.SourceRule> ruleName, final List<AnalyzeRule.SourceRule> ruleBookUrl, final List<AnalyzeRule.SourceRule> ruleAuthor, final List<AnalyzeRule.SourceRule> ruleKind, final List<AnalyzeRule.SourceRule> ruleCoverUrl, final List<AnalyzeRule.SourceRule> ruleWordCount, final List<AnalyzeRule.SourceRule> ruleIntro, final List<AnalyzeRule.SourceRule> ruleLastChapter, final DebugLog debugLog, final Continuation<? super SearchBook> $completion) {
        final SearchBook searchBook = new SearchBook((String)null, (String)null, (String)null, 0, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 0L, variable, 0, 24575, (DefaultConstructorMarker)null);
        searchBook.setOrigin(bookSource.getBookSourceUrl());
        searchBook.setOriginName(bookSource.getBookSourceName());
        searchBook.setType(bookSource.getBookSourceType());
        searchBook.setOriginOrder(bookSource.getCustomOrder());
        searchBook.setUserNameSpace(analyzeRule.getUserNameSpace());
        analyzeRule.setRuleData((RuleDataInterface)searchBook);
        AnalyzeRule.setContent$default(analyzeRule, item, null, 2, null);
        JobKt.ensureActive($completion.getContext());
        if (log) {
            if (debugLog != null) {
                DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u4e66\u540d", false, 4, (Object)null);
            }
        }
        searchBook.setName(BookHelp.INSTANCE.formatBookName(AnalyzeRule.getString$default(analyzeRule, ruleName, null, false, 6, null)));
        if (log) {
            if (debugLog != null) {
                DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getName()), false, 4, (Object)null);
            }
        }
        if (searchBook.getName().length() > 0) {
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u4f5c\u8005", false, 4, (Object)null);
                }
            }
            searchBook.setAuthor(BookHelp.INSTANCE.formatBookAuthor(AnalyzeRule.getString$default(analyzeRule, ruleAuthor, null, false, 6, null)));
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getAuthor()), false, 4, (Object)null);
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u5206\u7c7b", false, 4, (Object)null);
                }
            }
            try {
                final SearchBook searchBook2 = searchBook;
                final List stringList$default = AnalyzeRule.getStringList$default(analyzeRule, ruleKind, null, false, 6, null);
                searchBook2.setKind((stringList$default == null) ? null : CollectionsKt.joinToString$default((Iterable)stringList$default, (CharSequence)",", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null));
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getKind()), false, 4, (Object)null);
                    }
                }
            }
            catch (final Exception e) {
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, (Object)null);
                    }
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u5b57\u6570", false, 4, (Object)null);
                }
            }
            try {
                searchBook.setWordCount(StringUtils.INSTANCE.wordCountFormat(AnalyzeRule.getString$default(analyzeRule, ruleWordCount, null, false, 6, null)));
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getWordCount()), false, 4, (Object)null);
                    }
                }
            }
            catch (final Exception e) {
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, (Object)null);
                    }
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u6700\u65b0\u7ae0\u8282", false, 4, (Object)null);
                }
            }
            try {
                searchBook.setLatestChapterTitle(AnalyzeRule.getString$default(analyzeRule, ruleLastChapter, null, false, 6, null));
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getLatestChapterTitle()), false, 4, (Object)null);
                    }
                }
            }
            catch (final Exception e) {
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, (Object)null);
                    }
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u7b80\u4ecb", false, 4, (Object)null);
                }
            }
            try {
                searchBook.setIntro(StringExtensionsKt.htmlFormat(AnalyzeRule.getString$default(analyzeRule, ruleIntro, null, false, 6, null)));
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getIntro()), false, 4, (Object)null);
                    }
                }
            }
            catch (final Exception e) {
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, (Object)null);
                    }
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u5c01\u9762\u94fe\u63a5", false, 4, (Object)null);
                }
            }
            try {
                final String it = AnalyzeRule.getString$default(analyzeRule, ruleCoverUrl, null, false, 6, null);
                final int n = 0;
                if (it.length() > 0) {
                    searchBook.setCoverUrl(NetworkUtils.INSTANCE.getAbsoluteURL(baseUrl, it));
                }
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getCoverUrl()), false, 4, (Object)null);
                    }
                }
            }
            catch (final Exception e) {
                if (log) {
                    if (debugLog != null) {
                        DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, (Object)null);
                    }
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u8be6\u60c5\u9875\u94fe\u63a5", false, 4, (Object)null);
                }
            }
            searchBook.setBookUrl(AnalyzeRule.getString$default(analyzeRule, ruleBookUrl, null, true, 2, null));
            if (searchBook.getBookUrl().length() == 0) {
                searchBook.setBookUrl(baseUrl);
            }
            if (log) {
                if (debugLog != null) {
                    DebugLog$DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)searchBook.getBookUrl()), false, 4, (Object)null);
                }
            }
            return searchBook;
        }
        return null;
    }
    
    static {
        INSTANCE = new BookList();
    }
}
