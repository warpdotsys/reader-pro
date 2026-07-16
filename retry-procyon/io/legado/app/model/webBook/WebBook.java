// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.webBook;

import java.util.Iterator;
import kotlin.Result$Companion;
import io.legado.app.exception.NoStackTraceException;
import kotlin.coroutines.jvm.internal.Boxing;
import io.legado.app.model.DebugLog$DefaultImpls;
import kotlin.Unit;
import io.legado.app.data.entities.Book;
import java.util.ArrayList;
import io.legado.app.help.http.StrResponse;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.Ref$ObjectRef;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import java.util.Map;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.data.entities.BaseSource;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import io.legado.app.data.entities.SearchBook;
import java.util.List;
import kotlin.coroutines.Continuation;
import io.legado.app.model.Debug;
import io.legado.app.data.entities.rule.ContentRule;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.data.entities.rule.SearchRule;
import io.legado.app.data.entities.rule.ExploreRule;
import kotlin.Result;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import io.legado.app.model.DebugLog;
import org.jetbrains.annotations.NotNull;
import io.legado.app.data.entities.BookSource;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B1\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\tB/\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\fJ+\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 2\u0006\u0010\"\u001a\u00020\u00032\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010%J-\u0010&\u001a\u00020\u00032\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020*2\n\b\u0002\u0010+\u001a\u0004\u0018\u00010\u0003H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010,J#\u0010-\u001a\u00020(2\u0006\u0010'\u001a\u00020(2\b\b\u0002\u0010.\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010/J#\u0010-\u001a\u00020(2\u0006\u00100\u001a\u00020\u00032\b\b\u0002\u0010.\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u00101J\u001f\u00102\u001a\b\u0012\u0004\u0012\u00020*0 2\u0006\u0010'\u001a\u00020(H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u00103J2\u00104\u001a\b\u0012\u0004\u0012\u00020(052\u0006\u00106\u001a\u00020\u00032\u0006\u00107\u001a\u00020\u0003H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b8\u00109J+\u0010:\u001a\b\u0012\u0004\u0012\u00020!0 2\u0006\u0010;\u001a\u00020\u00032\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010%R\u0011\u0010\n\u001a\u00020\u000b?\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00078F?\u0006\u0006\u001a\u0004\b\u0016\u0010\u0012R\u0011\u0010\u0017\u001a\u00020\u00038F?\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u00038F?\u0006\u0006\u001a\u0004\b\u001b\u0010\u0019R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0019\"\u0004\b\u001d\u0010\u001e\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001¡§\u0006<" }, d2 = { "Lio/legado/app/model/webBook/WebBook;", "", "bookSourceString", "", "debugLog", "", "debugLogger", "Lio/legado/app/model/DebugLog;", "userNameSpace", "(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;)V", "bookSource", "Lio/legado/app/data/entities/BookSource;", "(Lio/legado/app/data/entities/BookSource;ZLio/legado/app/model/DebugLog;Ljava/lang/String;)V", "getBookSource", "()Lio/legado/app/data/entities/BookSource;", "getDebugLog", "()Z", "getDebugLogger", "()Lio/legado/app/model/DebugLog;", "setDebugLogger", "(Lio/legado/app/model/DebugLog;)V", "debugger", "getDebugger", "sourceUrl", "getSourceUrl", "()Ljava/lang/String;", "userNS", "getUserNS", "getUserNameSpace", "setUserNameSpace", "(Ljava/lang/String;)V", "exploreBook", "", "Lio/legado/app/data/entities/SearchBook;", "url", "page", "", "(Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookContent", "book", "Lio/legado/app/data/entities/Book;", "bookChapter", "Lio/legado/app/data/entities/BookChapter;", "nextChapterUrl", "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookInfo", "canReName", "(Lio/legado/app/data/entities/Book;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "bookUrl", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChapterList", "(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "preciseSearch", "Lkotlin/Result;", "name", "author", "preciseSearch-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchBook", "key", "reader-pro" })
public final class WebBook
{
    @NotNull
    private final BookSource bookSource;
    private final boolean debugLog;
    @Nullable
    private DebugLog debugLogger;
    @Nullable
    private String userNameSpace;
    
    public WebBook(@NotNull final BookSource bookSource, final boolean debugLog, @Nullable final DebugLog debugLogger, @Nullable final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)bookSource, "bookSource");
        this.bookSource = bookSource;
        this.debugLog = debugLog;
        this.debugLogger = debugLogger;
        this.userNameSpace = userNameSpace;
    }
    
    @NotNull
    public final BookSource getBookSource() {
        return this.bookSource;
    }
    
    public final boolean getDebugLog() {
        return this.debugLog;
    }
    
    @Nullable
    public final DebugLog getDebugLogger() {
        return this.debugLogger;
    }
    
    public final void setDebugLogger(@Nullable final DebugLog <set-?>) {
        this.debugLogger = <set-?>;
    }
    
    @Nullable
    public final String getUserNameSpace() {
        return this.userNameSpace;
    }
    
    public final void setUserNameSpace(@Nullable final String <set-?>) {
        this.userNameSpace = <set-?>;
    }
    
    public WebBook(@NotNull final String bookSourceString, final boolean debugLog, @Nullable final DebugLog debugLogger, @Nullable final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)bookSourceString, "bookSourceString");
        final Object fromJson-IoAF18A = BookSource.Companion.fromJson-IoAF18A(bookSourceString);
        final BookSource bookSource = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
        this((bookSource == null) ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource, debugLog, debugLogger, userNameSpace);
    }
    
    @NotNull
    public final String getSourceUrl() {
        return this.bookSource.getBookSourceUrl();
    }
    
    @Nullable
    public final DebugLog getDebugger() {
        if (this.debugLogger != null) {
            return this.debugLogger;
        }
        if (this.debugLog) {
            return (DebugLog)Debug.INSTANCE;
        }
        return null;
    }
    
    @NotNull
    public final String getUserNS() {
        final String userNameSpace = this.userNameSpace;
        return (userNameSpace == null) ? "unknow" : userNameSpace;
    }
    
    @Nullable
    public final Object searchBook(@NotNull final String key, @Nullable final Integer page, @NotNull final Continuation<? super List<SearchBook>> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lio/legado/app/model/webBook/WebBook$searchBook$1;
        //     4: ifeq            39
        //     7: aload_3        
        //     8: checkcast       Lio/legado/app/model/webBook/WebBook$searchBook$1;
        //    11: astore          26
        //    13: aload           26
        //    15: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           26
        //    26: dup            
        //    27: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.label:I
        //    36: goto            50
        //    39: new             Lio/legado/app/model/webBook/WebBook$searchBook$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_3        
        //    45: invokespecial   io/legado/app/model/webBook/WebBook$searchBook$1.<init>:(Lio/legado/app/model/webBook/WebBook;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          27
        //    62: aload           $continuation
        //    64: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.label:I
        //    67: tableswitch {
        //                0: 92
        //                1: 320
        //                2: 577
        //          default: 730
        //        }
        //    92: aload           $result
        //    94: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    97: new             Lio/legado/app/data/entities/SearchBook;
        //   100: dup            
        //   101: aconst_null    
        //   102: aconst_null    
        //   103: aconst_null    
        //   104: iconst_0       
        //   105: aconst_null    
        //   106: aconst_null    
        //   107: aconst_null    
        //   108: aconst_null    
        //   109: aconst_null    
        //   110: aconst_null    
        //   111: aconst_null    
        //   112: aconst_null    
        //   113: lconst_0       
        //   114: aconst_null    
        //   115: iconst_0       
        //   116: sipush          32767
        //   119: aconst_null    
        //   120: invokespecial   io/legado/app/data/entities/SearchBook.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;IILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   123: astore          variableBook
        //   125: aload           variableBook
        //   127: aload_0         /* this */
        //   128: invokevirtual   io/legado/app/model/webBook/WebBook.getUserNS:()Ljava/lang/String;
        //   131: invokevirtual   io/legado/app/data/entities/SearchBook.setUserNameSpace:(Ljava/lang/String;)V
        //   134: aload_0         /* this */
        //   135: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   138: aload_0         /* this */
        //   139: invokevirtual   io/legado/app/model/webBook/WebBook.getUserNS:()Ljava/lang/String;
        //   142: invokevirtual   io/legado/app/data/entities/BookSource.setUserNameSpace:(Ljava/lang/String;)V
        //   145: aload_0         /* this */
        //   146: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   149: aload_0         /* this */
        //   150: invokevirtual   io/legado/app/model/webBook/WebBook.getDebugger:()Lio/legado/app/model/DebugLog;
        //   153: invokevirtual   io/legado/app/data/entities/BookSource.setLogger:(Lio/legado/app/model/DebugLog;)V
        //   156: aload_0         /* this */
        //   157: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   160: invokevirtual   io/legado/app/data/entities/BookSource.getSearchUrl:()Ljava/lang/String;
        //   163: astore          6
        //   165: aload           6
        //   167: ifnonnull       174
        //   170: aconst_null    
        //   171: goto            704
        //   174: aload           6
        //   176: astore          7
        //   178: iconst_0       
        //   179: istore          8
        //   181: iconst_0       
        //   182: istore          9
        //   184: aload           7
        //   186: astore          searchUrl
        //   188: iconst_0       
        //   189: istore          $i$a$-let-WebBook$searchBook$2
        //   191: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl;
        //   194: dup            
        //   195: aload           searchUrl
        //   197: aload_1         /* key */
        //   198: aload_2         /* page */
        //   199: aconst_null    
        //   200: aconst_null    
        //   201: aload_0         /* this */
        //   202: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   205: invokevirtual   io/legado/app/data/entities/BookSource.getBookSourceUrl:()Ljava/lang/String;
        //   208: aload_0         /* this */
        //   209: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   212: checkcast       Lio/legado/app/data/entities/BaseSource;
        //   215: aload           variableBook
        //   217: checkcast       Lio/legado/app/model/analyzeRule/RuleDataInterface;
        //   220: aconst_null    
        //   221: aload_0         /* this */
        //   222: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   225: iconst_1       
        //   226: invokevirtual   io/legado/app/data/entities/BookSource.getHeaderMap:(Z)Ljava/util/HashMap;
        //   229: checkcast       Ljava/util/Map;
        //   232: aload_0         /* this */
        //   233: invokevirtual   io/legado/app/model/webBook/WebBook.getDebugger:()Lio/legado/app/model/DebugLog;
        //   236: sipush          280
        //   239: aconst_null    
        //   240: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   243: astore          analyzeUrl
        //   245: new             Lkotlin/jvm/internal/Ref$ObjectRef;
        //   248: dup            
        //   249: invokespecial   kotlin/jvm/internal/Ref$ObjectRef.<init>:()V
        //   252: astore          res
        //   254: aload           res
        //   256: astore          14
        //   258: aload           analyzeUrl
        //   260: aconst_null    
        //   261: aconst_null    
        //   262: iconst_0       
        //   263: aload           $continuation
        //   265: bipush          7
        //   267: aconst_null    
        //   268: aload           $continuation
        //   270: aload_0         /* this */
        //   271: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$0:Ljava/lang/Object;
        //   274: aload           $continuation
        //   276: aload           variableBook
        //   278: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$1:Ljava/lang/Object;
        //   281: aload           $continuation
        //   283: aload           analyzeUrl
        //   285: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$2:Ljava/lang/Object;
        //   288: aload           $continuation
        //   290: aload           res
        //   292: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$3:Ljava/lang/Object;
        //   295: aload           $continuation
        //   297: aload           14
        //   299: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$4:Ljava/lang/Object;
        //   302: aload           $continuation
        //   304: iconst_1       
        //   305: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.label:I
        //   308: invokestatic    io/legado/app/model/analyzeRule/AnalyzeUrl.getStrResponseAwait$default:(Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   311: dup            
        //   312: aload           27
        //   314: if_acmpne       379
        //   317: aload           27
        //   319: areturn        
        //   320: iconst_0       
        //   321: istore          $i$a$-let-WebBook$searchBook$2
        //   323: aload           $continuation
        //   325: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$4:Ljava/lang/Object;
        //   328: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   331: astore          14
        //   333: aload           $continuation
        //   335: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$3:Ljava/lang/Object;
        //   338: checkcast       Lkotlin/jvm/internal/Ref$ObjectRef;
        //   341: astore          res
        //   343: aload           $continuation
        //   345: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$2:Ljava/lang/Object;
        //   348: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl;
        //   351: astore          analyzeUrl
        //   353: aload           $continuation
        //   355: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$1:Ljava/lang/Object;
        //   358: checkcast       Lio/legado/app/data/entities/SearchBook;
        //   361: astore          4
        //   363: aload           $continuation
        //   365: getfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$0:Ljava/lang/Object;
        //   368: checkcast       Lio/legado/app/model/webBook/WebBook;
        //   371: astore_0       
        //   372: aload           $result
        //   374: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   377: aload           $result
        //   379: astore          15
        //   381: aload           14
        //   383: aload           15
        //   385: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   388: aload_0        
        //   389: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   392: invokevirtual   io/legado/app/data/entities/BookSource.getLoginCheckJs:()Ljava/lang/String;
        //   395: astore          16
        //   397: aload           16
        //   399: ifnonnull       405
        //   402: goto            489
        //   405: aload           16
        //   407: astore          17
        //   409: iconst_0       
        //   410: istore          18
        //   412: iconst_0       
        //   413: istore          19
        //   415: aload           17
        //   417: astore          checkJs
        //   419: iconst_0       
        //   420: istore          $i$a$-let-WebBook$searchBook$2$1
        //   422: aload           checkJs
        //   424: checkcast       Ljava/lang/CharSequence;
        //   427: astore          22
        //   429: iconst_0       
        //   430: istore          23
        //   432: aload           22
        //   434: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   437: ifne            444
        //   440: iconst_1       
        //   441: goto            445
        //   444: iconst_0       
        //   445: ifeq            487
        //   448: aload           res
        //   450: aload           analyzeUrl
        //   452: aload           checkJs
        //   454: aload           res
        //   456: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   459: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.evalJS:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
        //   462: astore          22
        //   464: aload           22
        //   466: ifnonnull       479
        //   469: new             Ljava/lang/NullPointerException;
        //   472: dup            
        //   473: ldc             "null cannot be cast to non-null type io.legado.app.help.http.StrResponse"
        //   475: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   478: athrow         
        //   479: aload           22
        //   481: checkcast       Lio/legado/app/help/http/StrResponse;
        //   484: putfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   487: nop            
        //   488: nop            
        //   489: getstatic       io/legado/app/model/webBook/BookList.INSTANCE:Lio/legado/app/model/webBook/BookList;
        //   492: aload           res
        //   494: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   497: checkcast       Lio/legado/app/help/http/StrResponse;
        //   500: invokevirtual   io/legado/app/help/http/StrResponse.getBody:()Ljava/lang/String;
        //   503: aload_0        
        //   504: invokevirtual   io/legado/app/model/webBook/WebBook.getBookSource:()Lio/legado/app/data/entities/BookSource;
        //   507: aload           analyzeUrl
        //   509: aload           res
        //   511: getfield        kotlin/jvm/internal/Ref$ObjectRef.element:Ljava/lang/Object;
        //   514: checkcast       Lio/legado/app/help/http/StrResponse;
        //   517: invokevirtual   io/legado/app/help/http/StrResponse.getUrl:()Ljava/lang/String;
        //   520: aload           4
        //   522: iconst_1       
        //   523: aload_0        
        //   524: invokevirtual   io/legado/app/model/webBook/WebBook.getDebugger:()Lio/legado/app/model/DebugLog;
        //   527: aload           $continuation
        //   529: aload           $continuation
        //   531: aconst_null    
        //   532: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$0:Ljava/lang/Object;
        //   535: aload           $continuation
        //   537: aconst_null    
        //   538: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$1:Ljava/lang/Object;
        //   541: aload           $continuation
        //   543: aconst_null    
        //   544: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$2:Ljava/lang/Object;
        //   547: aload           $continuation
        //   549: aconst_null    
        //   550: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$3:Ljava/lang/Object;
        //   553: aload           $continuation
        //   555: aconst_null    
        //   556: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.L$4:Ljava/lang/Object;
        //   559: aload           $continuation
        //   561: iconst_2       
        //   562: putfield        io/legado/app/model/webBook/WebBook$searchBook$1.label:I
        //   565: invokevirtual   io/legado/app/model/webBook/BookList.analyzeBookList:(Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Lio/legado/app/data/entities/SearchBook;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   568: dup            
        //   569: aload           27
        //   571: if_acmpne       587
        //   574: aload           27
        //   576: areturn        
        //   577: iconst_0       
        //   578: istore          $i$a$-let-WebBook$searchBook$2
        //   580: aload           $result
        //   582: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   585: aload           $result
        //   587: checkcast       Ljava/lang/Iterable;
        //   590: astore          $this$map$iv
        //   592: iconst_0       
        //   593: istore          $i$f$map
        //   595: aload           $this$map$iv
        //   597: astore          18
        //   599: new             Ljava/util/ArrayList;
        //   602: dup            
        //   603: aload           $this$map$iv
        //   605: bipush          10
        //   607: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   610: invokespecial   java/util/ArrayList.<init>:(I)V
        //   613: checkcast       Ljava/util/Collection;
        //   616: astore          destination$iv$iv
        //   618: iconst_0       
        //   619: istore          $i$f$mapTo
        //   621: aload           $this$mapTo$iv$iv
        //   623: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   628: astore          21
        //   630: aload           21
        //   632: invokeinterface java/util/Iterator.hasNext:()Z
        //   637: ifeq            696
        //   640: aload           21
        //   642: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   647: astore          item$iv$iv
        //   649: aload           destination$iv$iv
        //   651: aload           item$iv$iv
        //   653: checkcast       Lio/legado/app/data/entities/SearchBook;
        //   656: astore          23
        //   658: astore          14
        //   660: iconst_0       
        //   661: istore          $i$a$-map-WebBook$searchBook$2$2
        //   663: aload           it
        //   665: ldc_w           ""
        //   668: invokevirtual   io/legado/app/data/entities/SearchBook.setTocHtml:(Ljava/lang/String;)V
        //   671: aload           it
        //   673: ldc_w           ""
        //   676: invokevirtual   io/legado/app/data/entities/SearchBook.setInfoHtml:(Ljava/lang/String;)V
        //   679: aload           it
        //   681: astore          15
        //   683: aload           14
        //   685: aload           15
        //   687: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   692: pop            
        //   693: goto            630
        //   696: aload           destination$iv$iv
        //   698: checkcast       Ljava/util/List;
        //   701: nop            
        //   702: nop            
        //   703: nop            
        //   704: astore          5
        //   706: aload           5
        //   708: ifnonnull       727
        //   711: iconst_0       
        //   712: istore          6
        //   714: new             Ljava/util/ArrayList;
        //   717: dup            
        //   718: invokespecial   java/util/ArrayList.<init>:()V
        //   721: checkcast       Ljava/util/List;
        //   724: goto            729
        //   727: aload           5
        //   729: areturn        
        //   730: new             Ljava/lang/IllegalStateException;
        //   733: dup            
        //   734: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //   737: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   740: athrow         
        //    Signature:
        //  (Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation<-Ljava/util/List<Lio/legado/app/data/entities/SearchBook;>;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  key          
        //  page         
        //  $completion  
        //    StackMapTable: 00 14 27 FF 00 0A 00 1B 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 6A 00 00 FF 00 29 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FF 00 51 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 07 00 84 00 07 00 4F 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FF 00 91 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FF 00 3A 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 07 00 84 00 00 00 00 00 00 01 07 00 98 07 00 A7 07 00 A7 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 01 07 00 04 FF 00 19 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 07 00 84 00 00 00 00 00 00 01 07 00 98 07 00 A7 07 00 A7 07 00 04 07 00 4F 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FF 00 26 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 07 00 84 00 00 00 00 00 00 01 07 00 98 07 00 A7 07 00 A7 07 00 04 07 00 4F 07 00 4F 01 01 07 00 4F 01 07 00 C3 01 00 07 00 04 07 00 6A 07 00 04 00 00 40 01 FF 00 21 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 07 00 84 00 00 00 00 00 00 01 07 00 98 07 00 A7 07 00 A7 07 00 04 07 00 4F 07 00 4F 01 01 07 00 4F 01 07 00 04 01 00 07 00 04 07 00 6A 07 00 04 00 01 07 00 A7 07 FF 00 01 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 07 00 84 00 00 00 00 00 00 01 07 00 98 07 00 A7 07 00 A7 07 00 04 07 00 4F 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FF 00 57 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FF 00 09 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 01 07 00 04 FF 00 2A 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 01 00 00 00 00 07 00 E6 01 07 00 E6 07 00 F3 01 07 00 F9 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 FB 00 41 FF 00 07 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 01 07 01 0C FF 00 16 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 07 01 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00 41 07 01 0C FF 00 00 00 1C 07 00 02 07 00 4F 07 01 2C 07 01 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 04 07 00 6A 07 00 04 00 00
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
    
    public static /* synthetic */ Object searchBook$default(final WebBook webBook, final String key, Integer value, final Continuation $completion, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            value = 1;
        }
        return webBook.searchBook(key, value, (Continuation<? super List<SearchBook>>)$completion);
    }
    
    @Nullable
    public final Object exploreBook(@NotNull final String url, @Nullable final Integer page, @NotNull final Continuation<? super List<SearchBook>> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof WebBook$exploreBook.WebBook$exploreBook$1) {
                final WebBook$exploreBook.WebBook$exploreBook$1 webBook$exploreBook$1 = (WebBook$exploreBook.WebBook$exploreBook$1)$completion;
                if ((webBook$exploreBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebBook$exploreBook.WebBook$exploreBook$1 webBook$exploreBook$2 = webBook$exploreBook$1;
                    webBook$exploreBook$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new WebBook$exploreBook.WebBook$exploreBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        SearchBook variableBook = null;
        AnalyzeUrl analyzeUrl = null;
        Object l$4 = null;
        Ref$ObjectRef res = null;
        Object strResponseAwait$default = null;
        switch (((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                variableBook = new SearchBook((String)null, (String)null, (String)null, 0, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 0L, (String)null, 0, 32767, (DefaultConstructorMarker)null);
                variableBook.setUserNameSpace(this.getUserNS());
                this.getBookSource().setUserNameSpace(this.getUserNS());
                this.getBookSource().setLogger(this.getDebugger());
                analyzeUrl = new AnalyzeUrl(url, null, page, null, null, this.getBookSource().getBookSourceUrl(), (BaseSource)this.getBookSource(), (RuleDataInterface)variableBook, null, this.getBookSource().getHeaderMap(true), this.getDebugger(), 282, null);
                res = (Ref$ObjectRef)(l$4 = new Ref$ObjectRef());
                final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                final String s = null;
                final String s2 = null;
                final boolean b = false;
                final Continuation continuation = $continuation;
                final int n = 7;
                final Object o = null;
                ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$0 = this;
                ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$1 = variableBook;
                ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$2 = analyzeUrl;
                ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$3 = res;
                ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$4 = l$4;
                ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).label = 1;
                if ((strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl2, s, s2, b, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                l$4 = ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$4;
                res = (Ref$ObjectRef)((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$3;
                analyzeUrl = (AnalyzeUrl)((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$2;
                variableBook = (SearchBook)((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$1;
                this = (WebBook)((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            }
            case 2: {
                ResultKt.throwOnFailure($result);
                return $result;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        ((Ref$ObjectRef)l$4).element = strResponseAwait$default;
        final String loginCheckJs = this.getBookSource().getLoginCheckJs();
        if (loginCheckJs != null) {
            final String checkJs = loginCheckJs;
            final int n2 = 0;
            if (!StringsKt.isBlank((CharSequence)checkJs)) {
                final Ref$ObjectRef ref$ObjectRef = res;
                final Object evalJS = analyzeUrl.evalJS(checkJs, res.element);
                if (evalJS == null) {
                    throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                }
                ref$ObjectRef.element = evalJS;
            }
        }
        final BookList instance = BookList.INSTANCE;
        final String body = ((StrResponse)res.element).getBody();
        final BookSource bookSource = this.getBookSource();
        final AnalyzeUrl analyzeUrl3 = analyzeUrl;
        final String url2 = ((StrResponse)res.element).getUrl();
        final SearchBook variableBook2 = variableBook;
        final boolean isSearch = false;
        final DebugLog debugger = this.getDebugger();
        final Continuation $completion2 = $continuation;
        ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$0 = null;
        ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$1 = null;
        ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$2 = null;
        ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$3 = null;
        ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).L$4 = null;
        ((WebBook$exploreBook.WebBook$exploreBook$1)$continuation).label = 2;
        Object analyzeBookList;
        if ((analyzeBookList = instance.analyzeBookList(body, bookSource, analyzeUrl3, url2, variableBook2, isSearch, debugger, (Continuation<? super ArrayList<SearchBook>>)$completion2)) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return analyzeBookList;
    }
    
    @Nullable
    public final Object getBookInfo(@NotNull Book book, boolean var_2_18E, @NotNull final Continuation<? super Book> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof WebBook$getBookInfo.WebBook$getBookInfo$1) {
                final WebBook$getBookInfo.WebBook$getBookInfo$1 webBook$getBookInfo$1 = (WebBook$getBookInfo.WebBook$getBookInfo$1)$completion;
                if ((webBook$getBookInfo$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebBook$getBookInfo.WebBook$getBookInfo$1 webBook$getBookInfo$2 = webBook$getBookInfo$1;
                    webBook$getBookInfo$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new WebBook$getBookInfo.WebBook$getBookInfo$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0677: {
            AnalyzeUrl analyzeUrl = null;
            Object l$4 = null;
            Ref$ObjectRef res = null;
            Object strResponseAwait$default = null;
            Label_0454: {
                switch (((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        book.setType(this.getBookSource().getBookSourceType());
                        book.setUserNameSpace(this.getUserNS());
                        this.getBookSource().setUserNameSpace(this.getUserNS());
                        this.getBookSource().setLogger(this.getDebugger());
                        final CharSequence charSequence = book.getInfoHtml();
                        if (charSequence != null && charSequence.length() != 0) {
                            final BookInfo instance = BookInfo.INSTANCE;
                            final String infoHtml = book.getInfoHtml();
                            final BookSource bookSource = this.getBookSource();
                            final String bookUrl = book.getBookUrl();
                            final String bookUrl2 = book.getBookUrl();
                            final boolean b = canReName;
                            final DebugLog debugLog = null;
                            final Continuation continuation = $continuation;
                            final int n = 64;
                            final Object o = null;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$0 = book;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).label = 1;
                            if (BookInfo.analyzeBookInfo$default(instance, book, infoHtml, bookSource, bookUrl, bookUrl2, b, debugLog, continuation, n, o) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            break;
                        }
                        else {
                            analyzeUrl = new AnalyzeUrl(book.getBookUrl(), null, null, null, null, this.getBookSource().getBookSourceUrl(), (BaseSource)this.getBookSource(), (RuleDataInterface)book, null, this.getBookSource().getHeaderMap(true), this.getDebugger(), 286, null);
                            res = (Ref$ObjectRef)(l$4 = new Ref$ObjectRef());
                            final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                            final String s = null;
                            final String s2 = null;
                            final boolean b2 = false;
                            final Continuation continuation2 = $continuation;
                            final int n2 = 7;
                            final Object o2 = null;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$0 = this;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$1 = book;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$2 = analyzeUrl;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$3 = res;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$4 = l$4;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).Z$0 = var_2_18E;
                            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).label = 2;
                            if ((strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl2, s, s2, b2, continuation2, n2, o2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            break Label_0454;
                        }
                        break;
                    }
                    case 1: {
                        book = (Book)((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break;
                    }
                    case 2: {
                        var_2_18E = ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).Z$0;
                        l$4 = ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$4;
                        res = (Ref$ObjectRef)((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$3;
                        analyzeUrl = (AnalyzeUrl)((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$2;
                        book = (Book)((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$1;
                        this = (WebBook)((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        strResponseAwait$default = $result;
                        break Label_0454;
                    }
                    case 3: {
                        book = (Book)((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break Label_0677;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                return book;
            }
            ((Ref$ObjectRef)l$4).element = strResponseAwait$default;
            final String loginCheckJs = this.getBookSource().getLoginCheckJs();
            if (loginCheckJs != null) {
                final String checkJs = loginCheckJs;
                final int n3 = 0;
                if (!StringsKt.isBlank((CharSequence)checkJs)) {
                    final Ref$ObjectRef ref$ObjectRef = res;
                    final Object evalJS = analyzeUrl.evalJS(checkJs, res.element);
                    if (evalJS == null) {
                        throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                    }
                    ref$ObjectRef.element = evalJS;
                }
            }
            final BookInfo instance2 = BookInfo.INSTANCE;
            final Book book2 = book;
            final String body = ((StrResponse)res.element).getBody();
            final BookSource bookSource2 = this.getBookSource();
            final String bookUrl3 = book.getBookUrl();
            final String url = ((StrResponse)res.element).getUrl();
            final boolean canReName2 = var_2_18E;
            final DebugLog debugger = this.getDebugger();
            final Continuation $completion2 = $continuation;
            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$0 = book;
            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$1 = null;
            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$2 = null;
            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$3 = null;
            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).L$4 = null;
            ((WebBook$getBookInfo.WebBook$getBookInfo$1)$continuation).label = 3;
            if (instance2.analyzeBookInfo(book2, body, bookSource2, bookUrl3, url, canReName2, debugger, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        book.setTocHtml(null);
        return book;
    }
    
    public static /* synthetic */ Object getBookInfo$default(final WebBook webBook, final Book book, boolean canReName, final Continuation $completion, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            canReName = true;
        }
        return webBook.getBookInfo(book, canReName, (Continuation<? super Book>)$completion);
    }
    
    @Nullable
    public final Object getBookInfo(@NotNull final String bookUrl, final boolean canReName, @NotNull final Continuation<? super Book> $completion) {
        final Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
        book.setBookUrl(bookUrl);
        book.setOrigin(this.getBookSource().getBookSourceUrl());
        book.setOriginName(this.getBookSource().getBookSourceName());
        book.setOriginOrder(this.getBookSource().getCustomOrder());
        book.setType(this.getBookSource().getBookSourceType());
        book.setUserNameSpace(this.getUserNS());
        return this.getBookInfo(book, canReName, $completion);
    }
    
    @Nullable
    public final Object getChapterList(@NotNull Book book, @NotNull final Continuation<? super List<BookChapter>> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof WebBook$getChapterList.WebBook$getChapterList$1) {
                final WebBook$getChapterList.WebBook$getChapterList$1 webBook$getChapterList$1 = (WebBook$getChapterList.WebBook$getChapterList$1)$completion;
                if ((webBook$getChapterList$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebBook$getChapterList.WebBook$getChapterList$1 webBook$getChapterList$2 = webBook$getChapterList$1;
                    webBook$getChapterList$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new WebBook$getChapterList.WebBook$getChapterList$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        AnalyzeUrl analyzeUrl = null;
        Object l$4 = null;
        Ref$ObjectRef res = null;
        Object strResponseAwait$default = null;
        Label_0420: {
            Object analyzeChapterList$default = null;
            switch (((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    book.setType(this.getBookSource().getBookSourceType());
                    book.setUserNameSpace(this.getUserNS());
                    this.getBookSource().setUserNameSpace(this.getUserNS());
                    this.getBookSource().setLogger(this.getDebugger());
                    if (Intrinsics.areEqual((Object)book.getBookUrl(), (Object)book.getTocUrl())) {
                        final CharSequence charSequence = book.getTocHtml();
                        if (charSequence != null && charSequence.length() != 0) {
                            final BookChapterList instance = BookChapterList.INSTANCE;
                            final String tocHtml = book.getTocHtml();
                            final BookSource bookSource = this.getBookSource();
                            final String tocUrl = book.getTocUrl();
                            final String tocUrl2 = book.getTocUrl();
                            final DebugLog debugLog = null;
                            final Continuation continuation = $continuation;
                            final int n = 32;
                            final Object o = null;
                            ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).label = 1;
                            if ((analyzeChapterList$default = BookChapterList.analyzeChapterList$default(instance, book, tocHtml, bookSource, tocUrl, tocUrl2, debugLog, continuation, n, o)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            break;
                        }
                    }
                    analyzeUrl = new AnalyzeUrl(book.getTocUrl(), null, null, null, null, book.getBookUrl(), (BaseSource)this.getBookSource(), (RuleDataInterface)book, null, this.getBookSource().getHeaderMap(true), this.getDebugger(), 286, null);
                    res = (Ref$ObjectRef)(l$4 = new Ref$ObjectRef());
                    final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                    final String s = null;
                    final String s2 = null;
                    final boolean b = false;
                    final Continuation continuation2 = $continuation;
                    final int n2 = 7;
                    final Object o2 = null;
                    ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$0 = this;
                    ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$1 = book;
                    ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$2 = analyzeUrl;
                    ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$3 = res;
                    ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$4 = l$4;
                    ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).label = 2;
                    if ((strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl2, s, s2, b, continuation2, n2, o2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break Label_0420;
                }
                case 1: {
                    ResultKt.throwOnFailure($result);
                    analyzeChapterList$default = $result;
                    break;
                }
                case 2: {
                    l$4 = ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$4;
                    res = (Ref$ObjectRef)((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$3;
                    analyzeUrl = (AnalyzeUrl)((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$2;
                    book = (Book)((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$1;
                    this = (WebBook)((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    strResponseAwait$default = $result;
                    break Label_0420;
                }
                case 3: {
                    ResultKt.throwOnFailure($result);
                    return $result;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            return analyzeChapterList$default;
        }
        ((Ref$ObjectRef)l$4).element = strResponseAwait$default;
        final String loginCheckJs = this.getBookSource().getLoginCheckJs();
        if (loginCheckJs != null) {
            final String checkJs = loginCheckJs;
            final int n3 = 0;
            if (!StringsKt.isBlank((CharSequence)checkJs)) {
                final Ref$ObjectRef ref$ObjectRef = res;
                final Object evalJS = analyzeUrl.evalJS(checkJs, res.element);
                if (evalJS == null) {
                    throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                }
                ref$ObjectRef.element = evalJS;
            }
        }
        final BookChapterList instance2 = BookChapterList.INSTANCE;
        final Book book2 = book;
        final String body = ((StrResponse)res.element).getBody();
        final BookSource bookSource2 = this.getBookSource();
        final String tocUrl3 = book.getTocUrl();
        final String url = ((StrResponse)res.element).getUrl();
        final DebugLog debugger = this.getDebugger();
        final Continuation $completion2 = $continuation;
        ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$0 = null;
        ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$1 = null;
        ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$2 = null;
        ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$3 = null;
        ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).L$4 = null;
        ((WebBook$getChapterList.WebBook$getChapterList$1)$continuation).label = 3;
        Object analyzeChapterList;
        if ((analyzeChapterList = instance2.analyzeChapterList(book2, body, bookSource2, tocUrl3, url, debugger, (Continuation<? super List<BookChapter>>)$completion2)) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return analyzeChapterList;
    }
    
    @Nullable
    public final Object getBookContent(@NotNull Book book, @NotNull BookChapter bookChapter, @Nullable String nextChapterUrl, @NotNull final Continuation<? super String> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof WebBook$getBookContent.WebBook$getBookContent$1) {
                final WebBook$getBookContent.WebBook$getBookContent$1 webBook$getBookContent$1 = (WebBook$getBookContent.WebBook$getBookContent$1)$completion;
                if ((webBook$getBookContent$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebBook$getBookContent.WebBook$getBookContent$1 webBook$getBookContent$2 = webBook$getBookContent$1;
                    webBook$getBookContent$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new WebBook$getBookContent.WebBook$getBookContent$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object strResponseAwait$default = null;
        switch (((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                book.setUserNameSpace(this.getUserNS());
                this.getBookSource().setUserNameSpace(this.getUserNS());
                this.getBookSource().setLogger(this.getDebugger());
                final CharSequence charSequence = this.getBookSource().getContentRule().getContent();
                if (charSequence == null || charSequence.length() == 0) {
                    final DebugLog debugger = this.getDebugger();
                    if (debugger != null) {
                        DebugLog$DefaultImpls.log$default(debugger, this.getBookSource().getBookSourceUrl(), Intrinsics.stringPlus("\u21d2\u6b63\u6587\u89c4\u5219\u4e3a\u7a7a,\u4f7f\u7528\u7ae0\u8282\u94fe\u63a5: ", (Object)bookChapter.getUrl()), false, 4, (Object)null);
                    }
                    return bookChapter.getUrl();
                }
                if (bookChapter.isVolume() && StringsKt.startsWith$default(bookChapter.getUrl(), bookChapter.getTitle(), false, 2, (Object)null)) {
                    final DebugLog debugger2 = this.getDebugger();
                    if (debugger2 != null) {
                        DebugLog$DefaultImpls.log$default(debugger2, this.getBookSource().getBookSourceUrl(), "\u21d2\u4e00\u7ea7\u76ee\u5f55\u6b63\u6587\u4e0d\u89e3\u6790\u89c4\u5219", false, 4, (Object)null);
                    }
                    final String tag = bookChapter.getTag();
                    return (tag == null) ? "" : tag;
                }
                WebBookKt.access$getLogger$p().info("bookChapterUrl: {}", (Object)bookChapter.getUrl(), (Object)bookChapter.getAbsoluteURL());
                final AnalyzeUrl analyzeUrl2;
                final AnalyzeUrl analyzeUrl = analyzeUrl2 = new AnalyzeUrl(bookChapter.getAbsoluteURL(), null, null, null, null, book.getTocUrl(), (BaseSource)this.getBookSource(), (RuleDataInterface)book, bookChapter, this.getBookSource().getHeaderMap(true), this.getDebugger(), 30, null);
                final String webJs = this.getBookSource().getContentRule().getWebJs();
                final String sourceRegex = this.getBookSource().getContentRule().getSourceRegex();
                final boolean b = false;
                final Continuation continuation = $continuation;
                final int n = 4;
                final Object o = null;
                ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$0 = this;
                ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$1 = book;
                ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$2 = bookChapter;
                ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$3 = nextChapterUrl;
                ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).label = 1;
                if ((strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl2, webJs, sourceRegex, b, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                nextChapterUrl = (String)((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$3;
                bookChapter = (BookChapter)((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$2;
                book = (Book)((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$1;
                this = (WebBook)((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            }
            case 2: {
                ResultKt.throwOnFailure($result);
                return $result;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final StrResponse res = (StrResponse)strResponseAwait$default;
        final BookContent instance = BookContent.INSTANCE;
        final String body = res.getBody();
        final Book book2 = book;
        final BookChapter bookChapter2 = bookChapter;
        final BookSource bookSource = this.getBookSource();
        final String url = bookChapter.getUrl();
        final String url2 = res.getUrl();
        final String nextChapterUrl2 = nextChapterUrl;
        final DebugLog debugger3 = this.getDebugger();
        final Continuation $completion2 = $continuation;
        ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$0 = null;
        ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$1 = null;
        ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$2 = null;
        ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).L$3 = null;
        ((WebBook$getBookContent.WebBook$getBookContent$1)$continuation).label = 2;
        Object analyzeContent;
        if ((analyzeContent = instance.analyzeContent(body, book2, bookChapter2, bookSource, url, url2, nextChapterUrl2, debugger3, (Continuation<? super String>)$completion2)) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return analyzeContent;
    }
    
    @Nullable
    public final Object preciseSearch-0E7RQCE(@NotNull String name, @NotNull String author, @NotNull final Continuation<? super Result<Book>> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof WebBook$preciseSearch.WebBook$preciseSearch$1) {
                final WebBook$preciseSearch.WebBook$preciseSearch$1 webBook$preciseSearch$1 = (WebBook$preciseSearch.WebBook$preciseSearch$1)$completion;
                if ((webBook$preciseSearch$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebBook$preciseSearch.WebBook$preciseSearch$1 webBook$preciseSearch$2 = webBook$preciseSearch$1;
                    webBook$preciseSearch$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new WebBook$preciseSearch.WebBook$preciseSearch$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
    Label_0152_Outer:
        while (true) {
            while (true) {
                switch (((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        Object o4 = null;
                        try {
                            final Result$Companion companion = Result.Companion;
                            int n = 0;
                            final WebBook webBook = this;
                            final String s = name;
                            final Integer n2 = null;
                            final Continuation continuation = $continuation;
                            final int n3 = 2;
                            final Object o = null;
                            ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$0 = this;
                            ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$1 = name;
                            ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$2 = author;
                            ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).label = 1;
                            Object searchBook$default;
                            if ((searchBook$default = searchBook$default(webBook, s, n2, continuation, n3, o)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            Iterable $this$firstOrNull$iv;
                            int $i$f$firstOrNull;
                            SearchBook it;
                            int n4;
                            Object o2;
                            SearchBook searchBook2;
                            SearchBook searchBook;
                            int n5;
                            Book book;
                            WebBook webBook2;
                            Book book2;
                            boolean b;
                            Continuation continuation2;
                            int n6;
                            Object o3;
                            Object bookInfo$default;
                            Label_0280_Outer:Label_0389_Outer:
                            while (true) {
                                $this$firstOrNull$iv = (Iterable)searchBook$default;
                                $i$f$firstOrNull = 0;
                                while (true) {
                                    while (true) {
                                        for (final Object element$iv : $this$firstOrNull$iv) {
                                            it = (SearchBook)element$iv;
                                            n4 = 0;
                                            if (Boxing.boxBoolean(Intrinsics.areEqual((Object)it.getName(), (Object)name) && Intrinsics.areEqual((Object)it.getAuthor(), (Object)author))) {
                                                o2 = element$iv;
                                                searchBook2 = (SearchBook)o2;
                                                if (searchBook2 == null) {
                                                    throw new NoStackTraceException("\u672a\u641c\u7d22\u5230 " + name + '(' + author + ") \u4e66\u7c4d");
                                                }
                                                searchBook = searchBook2;
                                                n5 = 0;
                                                book = searchBook.toBook();
                                                if (StringsKt.isBlank((CharSequence)book.getTocUrl())) {
                                                    webBook2 = this;
                                                    book2 = book;
                                                    b = false;
                                                    continuation2 = $continuation;
                                                    n6 = 2;
                                                    o3 = null;
                                                    ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$0 = null;
                                                    ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$1 = null;
                                                    ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$2 = null;
                                                    ((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).label = 2;
                                                    if ((bookInfo$default = getBookInfo$default(webBook2, book2, b, continuation2, n6, o3)) == coroutine_SUSPENDED) {
                                                        return coroutine_SUSPENDED;
                                                    }
                                                    book = (Book)bookInfo$default;
                                                }
                                                o4 = Result.constructor-impl((Object)book);
                                                return o4;
                                            }
                                        }
                                        o2 = null;
                                        continue Label_0389_Outer;
                                    }
                                    n = 0;
                                    n5 = 0;
                                    ResultKt.throwOnFailure($result);
                                    bookInfo$default = $result;
                                    continue Label_0152_Outer;
                                }
                                n = 0;
                                author = (String)((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$2;
                                name = (String)((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$1;
                                this = (WebBook)((WebBook$preciseSearch.WebBook$preciseSearch$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                searchBook$default = $result;
                                continue Label_0280_Outer;
                            }
                        }
                        catch (final Throwable t) {
                            final Result$Companion companion2 = Result.Companion;
                            o4 = Result.constructor-impl(ResultKt.createFailure(t));
                        }
                        return o4;
                    }
                    case 1: {
                        continue;
                    }
                    case 2: {
                        continue Label_0152_Outer;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                break;
            }
            break;
        }
    }
}
