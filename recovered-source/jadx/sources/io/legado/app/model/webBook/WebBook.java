package io.legado.app.model.webBook;

import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.SearchBook;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.Debug;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: WebBook.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/WebBook.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B1\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\tB/\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\fJ+\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 2\u0006\u0010\"\u001a\u00020\u00032\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010%J-\u0010&\u001a\u00020\u00032\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020*2\n\b\u0002\u0010+\u001a\u0004\u0018\u00010\u0003H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010,J#\u0010-\u001a\u00020(2\u0006\u0010'\u001a\u00020(2\b\b\u0002\u0010.\u001a\u00020\u0005H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010/J#\u0010-\u001a\u00020(2\u0006\u00100\u001a\u00020\u00032\b\b\u0002\u0010.\u001a\u00020\u0005H\u0086@ø\u0001\u0000¢\u0006\u0002\u00101J\u001f\u00102\u001a\b\u0012\u0004\u0012\u00020*0 2\u0006\u0010'\u001a\u00020(H\u0086@ø\u0001\u0000¢\u0006\u0002\u00103J2\u00104\u001a\b\u0012\u0004\u0012\u00020(052\u0006\u00106\u001a\u00020\u00032\u0006\u00107\u001a\u00020\u0003H\u0086@ø\u0001\u0000ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b8\u00109J+\u0010:\u001a\b\u0012\u0004\u0012\u00020!0 2\u0006\u0010;\u001a\u00020\u00032\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010%R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00078F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0012R\u0011\u0010\u0017\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0019R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0019\"\u0004\b\u001d\u0010\u001e\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006<"}, d2 = {"Lio/legado/app/model/webBook/WebBook;", PackageDocumentBase.PREFIX_OPF, "bookSourceString", PackageDocumentBase.PREFIX_OPF, "debugLog", PackageDocumentBase.PREFIX_OPF, "debugLogger", "Lio/legado/app/model/DebugLog;", "userNameSpace", "(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;)V", "bookSource", "Lio/legado/app/data/entities/BookSource;", "(Lio/legado/app/data/entities/BookSource;ZLio/legado/app/model/DebugLog;Ljava/lang/String;)V", "getBookSource", "()Lio/legado/app/data/entities/BookSource;", "getDebugLog", "()Z", "getDebugLogger", "()Lio/legado/app/model/DebugLog;", "setDebugLogger", "(Lio/legado/app/model/DebugLog;)V", "debugger", "getDebugger", "sourceUrl", "getSourceUrl", "()Ljava/lang/String;", "userNS", "getUserNS", "getUserNameSpace", "setUserNameSpace", "(Ljava/lang/String;)V", "exploreBook", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/SearchBook;", RSSKeywords.RSS_ITEM_URL, "page", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;Ljava/lang/Integer;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookContent", "book", "Lio/legado/app/data/entities/Book;", "bookChapter", "Lio/legado/app/data/entities/BookChapter;", "nextChapterUrl", "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookInfo", "canReName", "(Lio/legado/app/data/entities/Book;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "bookUrl", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChapterList", "(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "preciseSearch", "Lkotlin/Result;", "name", "author", "preciseSearch-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchBook", "key", "reader-pro"})
public final class WebBook {

    @NotNull
    private final BookSource bookSource;
    private final boolean debugLog;

    @Nullable
    private DebugLog debugLogger;

    @Nullable
    private String userNameSpace;

    /* JADX INFO: renamed from: io.legado.app.model.webBook.WebBook$exploreBook$1, reason: invalid class name */
    /* JADX INFO: compiled from: WebBook.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/WebBook$exploreBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebBook.kt", l = {111, 118}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3"}, n = {"this", "variableBook", "analyzeUrl", "res"}, m = "exploreBook", c = "io.legado.app.model.webBook.WebBook")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebBook.this.exploreBook(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.webBook.WebBook$getBookContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: WebBook.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/WebBook$getBookContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebBook.kt", l = {255, 259}, i = {}, s = {}, n = {}, m = "getBookContent", c = "io.legado.app.model.webBook.WebBook")
    static final class C01671 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C01671(Continuation<? super C01671> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebBook.this.getBookContent(null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.webBook.WebBook$getBookInfo$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: WebBook.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/WebBook$getBookInfo$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebBook.kt", l = {138, 156, 164}, i = {1, 1}, s = {"L$2", "L$3"}, n = {"analyzeUrl", "res"}, m = "getBookInfo", c = "io.legado.app.model.webBook.WebBook")
    static final class C01681 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        boolean Z$0;
        /* synthetic */ Object result;
        int label;

        C01681(Continuation<? super C01681> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebBook.this.getBookInfo((Book) null, false, (Continuation<? super Book>) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.webBook.WebBook$getChapterList$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: WebBook.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/WebBook$getChapterList$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebBook.kt", l = {Wbxml.OPAQUE, 211, 218}, i = {1, 1}, s = {"L$2", "L$3"}, n = {"analyzeUrl", "res"}, m = "getChapterList", c = "io.legado.app.model.webBook.WebBook")
    static final class C01691 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C01691(Continuation<? super C01691> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebBook.this.getChapterList(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.webBook.WebBook$searchBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: WebBook.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/WebBook$searchBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebBook.kt", l = {Wbxml.PI, 74}, i = {0, 0}, s = {"L$2", "L$3"}, n = {"analyzeUrl", "res"}, m = "searchBook", c = "io.legado.app.model.webBook.WebBook")
    static final class C01701 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C01701(Continuation<? super C01701> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebBook.this.searchBook(null, null, (Continuation) this);
        }
    }

    public WebBook(@NotNull BookSource bookSource, boolean debugLog, @Nullable DebugLog debugLogger, @Nullable String userNameSpace) {
        Intrinsics.checkNotNullParameter(bookSource, "bookSource");
        this.bookSource = bookSource;
        this.debugLog = debugLog;
        this.debugLogger = debugLogger;
        this.userNameSpace = userNameSpace;
    }

    public /* synthetic */ WebBook(BookSource bookSource, boolean z, DebugLog debugLog, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(bookSource, (i & 2) != 0 ? true : z, (i & 4) != 0 ? null : debugLog, (i & 8) != 0 ? null : str);
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

    public final void setDebugLogger(@Nullable DebugLog debugLog) {
        this.debugLogger = debugLog;
    }

    @Nullable
    public final String getUserNameSpace() {
        return this.userNameSpace;
    }

    public final void setUserNameSpace(@Nullable String str) {
        this.userNameSpace = str;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public WebBook(@NotNull String bookSourceString, boolean debugLog, @Nullable DebugLog debugLogger, @Nullable String userNameSpace) {
        Intrinsics.checkNotNullParameter(bookSourceString, "bookSourceString");
        Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSourceString);
        BookSource bookSource = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
        this(bookSource == null ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource, debugLog, debugLogger, userNameSpace);
    }

    public /* synthetic */ WebBook(String str, boolean z, DebugLog debugLog, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? true : z, (i & 4) != 0 ? null : debugLog, (i & 8) != 0 ? null : str2);
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
            return Debug.INSTANCE;
        }
        return null;
    }

    @NotNull
    public final String getUserNS() {
        String str = this.userNameSpace;
        return str == null ? "unknow" : str;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0280 A[LOOP:0: B:38:0x0276->B:40:0x0280, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x02c7  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x02d7  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBook(@NotNull String key, @Nullable Integer page, @NotNull Continuation<? super List<SearchBook>> $completion) throws Exception {
        C01701 c01701;
        Object objAnalyzeBookList;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef res;
        AnalyzeUrl analyzeUrl;
        SearchBook variableBook;
        Object strResponseAwait$default;
        ArrayList arrayList;
        String checkJs;
        if ($completion instanceof C01701) {
            c01701 = (C01701) $completion;
            if ((c01701.label & Integer.MIN_VALUE) != 0) {
                c01701.label -= Integer.MIN_VALUE;
            } else {
                c01701 = new C01701($completion);
            }
        }
        Object $result = c01701.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01701.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                variableBook = new SearchBook(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
                variableBook.setUserNameSpace(getUserNS());
                getBookSource().setUserNameSpace(getUserNS());
                getBookSource().setLogger(getDebugger());
                String searchUrl = getBookSource().getSearchUrl();
                if (searchUrl == null) {
                    arrayList = null;
                    ArrayList arrayList2 = arrayList;
                    return arrayList2 != null ? new ArrayList() : arrayList2;
                }
                analyzeUrl = new AnalyzeUrl(searchUrl, key, page, null, null, getBookSource().getBookSourceUrl(), getBookSource(), variableBook, null, getBookSource().getHeaderMap(true), getDebugger(), 280, null);
                res = new Ref.ObjectRef();
                objectRef = res;
                c01701.L$0 = this;
                c01701.L$1 = variableBook;
                c01701.L$2 = analyzeUrl;
                c01701.L$3 = res;
                c01701.L$4 = objectRef;
                c01701.label = 1;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, c01701, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                objectRef.element = strResponseAwait$default;
                checkJs = this.getBookSource().getLoginCheckJs();
                if (checkJs != null) {
                    if (!StringsKt.isBlank(checkJs)) {
                        Ref.ObjectRef objectRef2 = res;
                        Object objEvalJS = analyzeUrl.evalJS(checkJs, res.element);
                        if (objEvalJS == null) {
                            throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                        }
                        objectRef2.element = (StrResponse) objEvalJS;
                    }
                }
                c01701.L$0 = null;
                c01701.L$1 = null;
                c01701.L$2 = null;
                c01701.L$3 = null;
                c01701.L$4 = null;
                c01701.label = 2;
                objAnalyzeBookList = BookList.INSTANCE.analyzeBookList(((StrResponse) res.element).getBody(), this.getBookSource(), analyzeUrl, ((StrResponse) res.element).getUrl(), variableBook, true, this.getDebugger(), c01701);
                if (objAnalyzeBookList == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Iterable $this$map$iv = (Iterable) objAnalyzeBookList;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                for (Object item$iv$iv : $this$map$iv) {
                    SearchBook it = (SearchBook) item$iv$iv;
                    it.setTocHtml(PackageDocumentBase.PREFIX_OPF);
                    it.setInfoHtml(PackageDocumentBase.PREFIX_OPF);
                    destination$iv$iv.add(it);
                }
                arrayList = (List) destination$iv$iv;
                ArrayList arrayList22 = arrayList;
                if (arrayList22 != null) {
                }
                break;
            case 1:
                objectRef = (Ref.ObjectRef) c01701.L$4;
                res = (Ref.ObjectRef) c01701.L$3;
                analyzeUrl = (AnalyzeUrl) c01701.L$2;
                variableBook = (SearchBook) c01701.L$1;
                this = (WebBook) c01701.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                objectRef.element = strResponseAwait$default;
                checkJs = this.getBookSource().getLoginCheckJs();
                if (checkJs != null) {
                }
                c01701.L$0 = null;
                c01701.L$1 = null;
                c01701.L$2 = null;
                c01701.L$3 = null;
                c01701.L$4 = null;
                c01701.label = 2;
                objAnalyzeBookList = BookList.INSTANCE.analyzeBookList(((StrResponse) res.element).getBody(), this.getBookSource(), analyzeUrl, ((StrResponse) res.element).getUrl(), variableBook, true, this.getDebugger(), c01701);
                if (objAnalyzeBookList == coroutine_suspended) {
                }
                Iterable $this$map$iv2 = (Iterable) objAnalyzeBookList;
                Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
                while (r0.hasNext()) {
                }
                arrayList = (List) destination$iv$iv2;
                ArrayList arrayList222 = arrayList;
                if (arrayList222 != null) {
                }
                break;
            case 2:
                ResultKt.throwOnFailure($result);
                objAnalyzeBookList = $result;
                Iterable $this$map$iv22 = (Iterable) objAnalyzeBookList;
                Collection destination$iv$iv22 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv22, 10));
                while (r0.hasNext()) {
                }
                arrayList = (List) destination$iv$iv22;
                ArrayList arrayList2222 = arrayList;
                if (arrayList2222 != null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object searchBook$default(WebBook webBook, String str, Integer num, Continuation continuation, int i, Object obj) throws Exception {
        if ((i & 2) != 0) {
            num = 1;
        }
        return webBook.searchBook(str, num, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object exploreBook(@NotNull String url, @Nullable Integer page, @NotNull Continuation<? super List<SearchBook>> $completion) throws Exception {
        AnonymousClass1 anonymousClass1;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef res;
        AnalyzeUrl analyzeUrl;
        SearchBook variableBook;
        Object strResponseAwait$default;
        if ($completion instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) $completion;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1($completion);
            }
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                variableBook = new SearchBook(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
                variableBook.setUserNameSpace(getUserNS());
                getBookSource().setUserNameSpace(getUserNS());
                getBookSource().setLogger(getDebugger());
                analyzeUrl = new AnalyzeUrl(url, null, page, null, null, getBookSource().getBookSourceUrl(), getBookSource(), variableBook, null, getBookSource().getHeaderMap(true), getDebugger(), 282, null);
                res = new Ref.ObjectRef();
                objectRef = res;
                anonymousClass1.L$0 = this;
                anonymousClass1.L$1 = variableBook;
                anonymousClass1.L$2 = analyzeUrl;
                anonymousClass1.L$3 = res;
                anonymousClass1.L$4 = objectRef;
                anonymousClass1.label = 1;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, anonymousClass1, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                objectRef = (Ref.ObjectRef) anonymousClass1.L$4;
                res = (Ref.ObjectRef) anonymousClass1.L$3;
                analyzeUrl = (AnalyzeUrl) anonymousClass1.L$2;
                variableBook = (SearchBook) anonymousClass1.L$1;
                this = (WebBook) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            case 2:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        objectRef.element = strResponseAwait$default;
        String checkJs = this.getBookSource().getLoginCheckJs();
        if (checkJs != null) {
            if (!StringsKt.isBlank(checkJs)) {
                Ref.ObjectRef objectRef2 = res;
                Object objEvalJS = analyzeUrl.evalJS(checkJs, res.element);
                if (objEvalJS == null) {
                    throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                }
                objectRef2.element = (StrResponse) objEvalJS;
            }
        }
        anonymousClass1.L$0 = null;
        anonymousClass1.L$1 = null;
        anonymousClass1.L$2 = null;
        anonymousClass1.L$3 = null;
        anonymousClass1.L$4 = null;
        anonymousClass1.label = 2;
        Object objAnalyzeBookList = BookList.INSTANCE.analyzeBookList(((StrResponse) res.element).getBody(), this.getBookSource(), analyzeUrl, ((StrResponse) res.element).getUrl(), variableBook, false, this.getDebugger(), anonymousClass1);
        return objAnalyzeBookList == coroutine_suspended ? coroutine_suspended : objAnalyzeBookList;
    }

    public static /* synthetic */ Object exploreBook$default(WebBook webBook, String str, Integer num, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            num = 1;
        }
        return webBook.exploreBook(str, num, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x01e0  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0292  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookInfo(@NotNull Book book, boolean canReName, @NotNull Continuation<? super Book> $completion) throws Exception {
        C01681 c01681;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef res;
        AnalyzeUrl analyzeUrl;
        Object strResponseAwait$default;
        String checkJs;
        BookInfo bookInfo;
        Book book2;
        String body;
        BookSource bookSource;
        String bookUrl;
        String url;
        boolean z;
        if ($completion instanceof C01681) {
            c01681 = (C01681) $completion;
            if ((c01681.label & Integer.MIN_VALUE) != 0) {
                c01681.label -= Integer.MIN_VALUE;
            } else {
                c01681 = new C01681($completion);
            }
        }
        Object $result = c01681.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01681.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                book.setType(getBookSource().getBookSourceType());
                book.setUserNameSpace(getUserNS());
                getBookSource().setUserNameSpace(getUserNS());
                getBookSource().setLogger(getDebugger());
                String infoHtml = book.getInfoHtml();
                if (!(infoHtml == null || infoHtml.length() == 0)) {
                    BookInfo bookInfo2 = BookInfo.INSTANCE;
                    String infoHtml2 = book.getInfoHtml();
                    BookSource bookSource2 = getBookSource();
                    String bookUrl2 = book.getBookUrl();
                    String bookUrl3 = book.getBookUrl();
                    boolean z2 = canReName;
                    c01681.L$0 = book;
                    c01681.label = 1;
                    if (BookInfo.analyzeBookInfo$default(bookInfo2, book, infoHtml2, bookSource2, bookUrl2, bookUrl3, z2, null, c01681, 64, null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return book;
                }
                analyzeUrl = new AnalyzeUrl(book.getBookUrl(), null, null, null, null, getBookSource().getBookSourceUrl(), getBookSource(), book, null, getBookSource().getHeaderMap(true), getDebugger(), 286, null);
                res = new Ref.ObjectRef();
                objectRef = res;
                c01681.L$0 = this;
                c01681.L$1 = book;
                c01681.L$2 = analyzeUrl;
                c01681.L$3 = res;
                c01681.L$4 = objectRef;
                c01681.Z$0 = canReName;
                c01681.label = 2;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, c01681, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                objectRef.element = strResponseAwait$default;
                checkJs = this.getBookSource().getLoginCheckJs();
                if (checkJs != null) {
                    if (!StringsKt.isBlank(checkJs)) {
                        Ref.ObjectRef objectRef2 = res;
                        Object objEvalJS = analyzeUrl.evalJS(checkJs, res.element);
                        if (objEvalJS == null) {
                            throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                        }
                        objectRef2.element = (StrResponse) objEvalJS;
                    }
                }
                bookInfo = BookInfo.INSTANCE;
                book2 = book;
                body = ((StrResponse) res.element).getBody();
                bookSource = this.getBookSource();
                bookUrl = book.getBookUrl();
                url = ((StrResponse) res.element).getUrl();
                z = !canReName;
                c01681.L$0 = book;
                c01681.L$1 = null;
                c01681.L$2 = null;
                c01681.L$3 = null;
                c01681.L$4 = null;
                c01681.label = 3;
                if (bookInfo.analyzeBookInfo(book2, body, bookSource, bookUrl, url, z, this.getDebugger(), c01681) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                book.setTocHtml(null);
                return book;
            case 1:
                book = (Book) c01681.L$0;
                ResultKt.throwOnFailure($result);
                return book;
            case 2:
                canReName = c01681.Z$0;
                objectRef = (Ref.ObjectRef) c01681.L$4;
                res = (Ref.ObjectRef) c01681.L$3;
                analyzeUrl = (AnalyzeUrl) c01681.L$2;
                book = (Book) c01681.L$1;
                this = (WebBook) c01681.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                objectRef.element = strResponseAwait$default;
                checkJs = this.getBookSource().getLoginCheckJs();
                if (checkJs != null) {
                }
                bookInfo = BookInfo.INSTANCE;
                book2 = book;
                body = ((StrResponse) res.element).getBody();
                bookSource = this.getBookSource();
                bookUrl = book.getBookUrl();
                url = ((StrResponse) res.element).getUrl();
                if (!canReName) {
                }
                c01681.L$0 = book;
                c01681.L$1 = null;
                c01681.L$2 = null;
                c01681.L$3 = null;
                c01681.L$4 = null;
                c01681.label = 3;
                if (bookInfo.analyzeBookInfo(book2, body, bookSource, bookUrl, url, z, this.getDebugger(), c01681) == coroutine_suspended) {
                }
                book.setTocHtml(null);
                return book;
            case 3:
                book = (Book) c01681.L$0;
                ResultKt.throwOnFailure($result);
                book.setTocHtml(null);
                return book;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object getBookInfo$default(WebBook webBook, Book book, boolean z, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        return webBook.getBookInfo(book, z, (Continuation<? super Book>) continuation);
    }

    public static /* synthetic */ Object getBookInfo$default(WebBook webBook, String str, boolean z, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        return webBook.getBookInfo(str, z, (Continuation<? super Book>) continuation);
    }

    @Nullable
    public final Object getBookInfo(@NotNull String bookUrl, boolean canReName, @NotNull Continuation<? super Book> $completion) {
        Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
        book.setBookUrl(bookUrl);
        book.setOrigin(getBookSource().getBookSourceUrl());
        book.setOriginName(getBookSource().getBookSourceName());
        book.setOriginOrder(getBookSource().getCustomOrder());
        book.setType(getBookSource().getBookSourceType());
        book.setUserNameSpace(getUserNS());
        return getBookInfo(book, canReName, $completion);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getChapterList(@NotNull Book book, @NotNull Continuation<? super List<BookChapter>> $completion) throws Exception {
        C01691 c01691;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef res;
        AnalyzeUrl analyzeUrl;
        Object strResponseAwait$default;
        if ($completion instanceof C01691) {
            c01691 = (C01691) $completion;
            if ((c01691.label & Integer.MIN_VALUE) != 0) {
                c01691.label -= Integer.MIN_VALUE;
            } else {
                c01691 = new C01691($completion);
            }
        }
        Object $result = c01691.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01691.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                book.setType(getBookSource().getBookSourceType());
                book.setUserNameSpace(getUserNS());
                getBookSource().setUserNameSpace(getUserNS());
                getBookSource().setLogger(getDebugger());
                if (Intrinsics.areEqual(book.getBookUrl(), book.getTocUrl())) {
                    String tocHtml = book.getTocHtml();
                    if (!(tocHtml == null || tocHtml.length() == 0)) {
                        c01691.label = 1;
                        Object objAnalyzeChapterList$default = BookChapterList.analyzeChapterList$default(BookChapterList.INSTANCE, book, book.getTocHtml(), getBookSource(), book.getTocUrl(), book.getTocUrl(), null, c01691, 32, null);
                        return objAnalyzeChapterList$default == coroutine_suspended ? coroutine_suspended : objAnalyzeChapterList$default;
                    }
                }
                analyzeUrl = new AnalyzeUrl(book.getTocUrl(), null, null, null, null, book.getBookUrl(), getBookSource(), book, null, getBookSource().getHeaderMap(true), getDebugger(), 286, null);
                res = new Ref.ObjectRef();
                objectRef = res;
                c01691.L$0 = this;
                c01691.L$1 = book;
                c01691.L$2 = analyzeUrl;
                c01691.L$3 = res;
                c01691.L$4 = objectRef;
                c01691.label = 2;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, c01691, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                return $result;
            case 2:
                objectRef = (Ref.ObjectRef) c01691.L$4;
                res = (Ref.ObjectRef) c01691.L$3;
                analyzeUrl = (AnalyzeUrl) c01691.L$2;
                book = (Book) c01691.L$1;
                this = (WebBook) c01691.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            case 3:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        objectRef.element = strResponseAwait$default;
        String checkJs = this.getBookSource().getLoginCheckJs();
        if (checkJs != null) {
            if (!StringsKt.isBlank(checkJs)) {
                Ref.ObjectRef objectRef2 = res;
                Object objEvalJS = analyzeUrl.evalJS(checkJs, res.element);
                if (objEvalJS == null) {
                    throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
                }
                objectRef2.element = (StrResponse) objEvalJS;
            }
        }
        c01691.L$0 = null;
        c01691.L$1 = null;
        c01691.L$2 = null;
        c01691.L$3 = null;
        c01691.L$4 = null;
        c01691.label = 3;
        Object objAnalyzeChapterList = BookChapterList.INSTANCE.analyzeChapterList(book, ((StrResponse) res.element).getBody(), this.getBookSource(), book.getTocUrl(), ((StrResponse) res.element).getUrl(), this.getDebugger(), c01691);
        return objAnalyzeChapterList == coroutine_suspended ? coroutine_suspended : objAnalyzeChapterList;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookContent(@NotNull Book book, @NotNull BookChapter bookChapter, @Nullable String nextChapterUrl, @NotNull Continuation<? super String> $completion) throws Exception {
        C01671 c01671;
        Object strResponseAwait$default;
        if ($completion instanceof C01671) {
            c01671 = (C01671) $completion;
            if ((c01671.label & Integer.MIN_VALUE) != 0) {
                c01671.label -= Integer.MIN_VALUE;
            } else {
                c01671 = new C01671($completion);
            }
        }
        Object $result = c01671.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01671.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                book.setUserNameSpace(getUserNS());
                getBookSource().setUserNameSpace(getUserNS());
                getBookSource().setLogger(getDebugger());
                String content = getBookSource().getContentRule().getContent();
                if (content == null || content.length() == 0) {
                    DebugLog debugger = getDebugger();
                    if (debugger != null) {
                        DebugLog.DefaultImpls.log$default(debugger, getBookSource().getBookSourceUrl(), Intrinsics.stringPlus("⇒正文规则为空,使用章节链接: ", bookChapter.getUrl()), false, 4, null);
                    }
                    return bookChapter.getUrl();
                }
                if (!bookChapter.isVolume() || !StringsKt.startsWith$default(bookChapter.getUrl(), bookChapter.getTitle(), false, 2, (Object) null)) {
                    WebBookKt.logger.info("bookChapterUrl: {}", bookChapter.getUrl(), bookChapter.getAbsoluteURL());
                    AnalyzeUrl analyzeUrl = new AnalyzeUrl(bookChapter.getAbsoluteURL(), null, null, null, null, book.getTocUrl(), getBookSource(), book, bookChapter, getBookSource().getHeaderMap(true), getDebugger(), 30, null);
                    c01671.L$0 = this;
                    c01671.L$1 = book;
                    c01671.L$2 = bookChapter;
                    c01671.L$3 = nextChapterUrl;
                    c01671.label = 1;
                    strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, getBookSource().getContentRule().getWebJs(), getBookSource().getContentRule().getSourceRegex(), false, c01671, 4, null);
                    if (strResponseAwait$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    DebugLog debugger2 = getDebugger();
                    if (debugger2 != null) {
                        DebugLog.DefaultImpls.log$default(debugger2, getBookSource().getBookSourceUrl(), "⇒一级目录正文不解析规则", false, 4, null);
                    }
                    String tag = bookChapter.getTag();
                    return tag == null ? PackageDocumentBase.PREFIX_OPF : tag;
                }
                break;
            case 1:
                nextChapterUrl = (String) c01671.L$3;
                bookChapter = (BookChapter) c01671.L$2;
                book = (Book) c01671.L$1;
                this = (WebBook) c01671.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            case 2:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        StrResponse res = (StrResponse) strResponseAwait$default;
        c01671.L$0 = null;
        c01671.L$1 = null;
        c01671.L$2 = null;
        c01671.L$3 = null;
        c01671.label = 2;
        Object objAnalyzeContent = BookContent.INSTANCE.analyzeContent(res.getBody(), book, bookChapter, this.getBookSource(), bookChapter.getUrl(), res.getUrl(), nextChapterUrl, this.getDebugger(), c01671);
        if (objAnalyzeContent == coroutine_suspended) {
            return coroutine_suspended;
        }
        return objAnalyzeContent;
    }

    public static /* synthetic */ Object getBookContent$default(WebBook webBook, Book book, BookChapter bookChapter, String str, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            str = null;
        }
        return webBook.getBookContent(book, bookChapter, str, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00d9 A[Catch: Throwable -> 0x01c9, TryCatch #0 {Throwable -> 0x01c9, blocks: (B:11:0x0064, B:18:0x00be, B:19:0x00cf, B:21:0x00d9, B:23:0x00f8, B:27:0x0109, B:31:0x0118, B:45:0x018f, B:46:0x01b9, B:34:0x0125, B:36:0x014b, B:43:0x0185, B:47:0x01ba, B:17:0x00b6, B:42:0x017d), top: B:54:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0125 A[Catch: Throwable -> 0x01c9, TryCatch #0 {Throwable -> 0x01c9, blocks: (B:11:0x0064, B:18:0x00be, B:19:0x00cf, B:21:0x00d9, B:23:0x00f8, B:27:0x0109, B:31:0x0118, B:45:0x018f, B:46:0x01b9, B:34:0x0125, B:36:0x014b, B:43:0x0185, B:47:0x01ba, B:17:0x00b6, B:42:0x017d), top: B:54:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0117 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /* JADX INFO: renamed from: preciseSearch-0E7RQCE, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object m269preciseSearch0E7RQCE(@NotNull String name, @NotNull String author, @NotNull Continuation<? super Result<Book>> $completion) {
        WebBook$preciseSearch$1 webBook$preciseSearch$1;
        Object obj;
        Object bookInfo$default;
        Object objSearchBook$default;
        Book book;
        Iterator it;
        Object obj2;
        SearchBook searchBook;
        if ($completion instanceof WebBook$preciseSearch$1) {
            webBook$preciseSearch$1 = (WebBook$preciseSearch$1) $completion;
            if ((webBook$preciseSearch$1.label & Integer.MIN_VALUE) != 0) {
                webBook$preciseSearch$1.label -= Integer.MIN_VALUE;
            } else {
                webBook$preciseSearch$1 = new WebBook$preciseSearch$1(this, $completion);
            }
        }
        Object $result = webBook$preciseSearch$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        switch (webBook$preciseSearch$1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                Result.Companion companion2 = Result.Companion;
                webBook$preciseSearch$1.L$0 = this;
                webBook$preciseSearch$1.L$1 = name;
                webBook$preciseSearch$1.L$2 = author;
                webBook$preciseSearch$1.label = 1;
                objSearchBook$default = searchBook$default(this, name, null, webBook$preciseSearch$1, 2, null);
                if (objSearchBook$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Iterable $this$firstOrNull$iv = (Iterable) objSearchBook$default;
                it = $this$firstOrNull$iv.iterator();
                while (true) {
                    if (it.hasNext()) {
                        obj2 = null;
                    } else {
                        Object element$iv = it.next();
                        SearchBook it2 = (SearchBook) element$iv;
                        if (Boxing.boxBoolean(Intrinsics.areEqual(it2.getName(), name) && Intrinsics.areEqual(it2.getAuthor(), author)).booleanValue()) {
                            obj2 = element$iv;
                        }
                    }
                }
                searchBook = (SearchBook) obj2;
                if (searchBook != null) {
                    throw new NoStackTraceException("未搜索到 " + name + '(' + author + ") 书籍");
                }
                book = searchBook.toBook();
                if (StringsKt.isBlank(book.getTocUrl())) {
                    webBook$preciseSearch$1.L$0 = null;
                    webBook$preciseSearch$1.L$1 = null;
                    webBook$preciseSearch$1.L$2 = null;
                    webBook$preciseSearch$1.label = 2;
                    bookInfo$default = getBookInfo$default(this, book, false, (Continuation) webBook$preciseSearch$1, 2, (Object) null);
                    if (bookInfo$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    book = (Book) bookInfo$default;
                }
                obj = Result.constructor-impl(book);
                return obj;
            case 1:
                author = (String) webBook$preciseSearch$1.L$2;
                name = (String) webBook$preciseSearch$1.L$1;
                this = (WebBook) webBook$preciseSearch$1.L$0;
                ResultKt.throwOnFailure($result);
                objSearchBook$default = $result;
                Iterable $this$firstOrNull$iv2 = (Iterable) objSearchBook$default;
                it = $this$firstOrNull$iv2.iterator();
                while (true) {
                    if (it.hasNext()) {
                    }
                }
                searchBook = (SearchBook) obj2;
                if (searchBook != null) {
                }
                break;
            case 2:
                ResultKt.throwOnFailure($result);
                bookInfo$default = $result;
                book = (Book) bookInfo$default;
                obj = Result.constructor-impl(book);
                return obj;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
