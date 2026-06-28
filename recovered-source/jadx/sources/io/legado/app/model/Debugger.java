package io.legado.app.model;

import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.SearchBook;
import io.legado.app.model.DebugLog;
import io.legado.app.model.webBook.WebBook;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.HtmlFormatter;
import io.legado.app.utils.StringExtensionsKt;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Debugger.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/Debugger.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0002\u0010\u0006J3\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0004H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0015J!\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0004H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0018J!\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u001aJ\u0010\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u0004H\u0016J\u001a\u0010\u001b\u001a\u00020\u00052\b\u0010\u001d\u001a\u0004\u0018\u00010\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u0004J$\u0010\u001b\u001a\u00020\u00052\b\u0010\u001d\u001a\u0004\u0018\u00010\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001f\u001a\u00020 H\u0016J!\u0010!\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u0004H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0018J!\u0010#\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u0004H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0018J!\u0010$\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u001aR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006%"}, d2 = {"Lio/legado/app/model/Debugger;", "Lio/legado/app/model/DebugLog;", "logMsg", "Lkotlin/Function1;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "(Lkotlin/jvm/functions/Function1;)V", "debugTimeFormat", "Ljava/text/SimpleDateFormat;", "getLogMsg", "()Lkotlin/jvm/functions/Function1;", "startTime", PackageDocumentBase.PREFIX_OPF, "contentDebug", "webBook", "Lio/legado/app/model/webBook/WebBook;", "book", "Lio/legado/app/data/entities/Book;", "bookChapter", "Lio/legado/app/data/entities/BookChapter;", "nextChapterUrl", "(Lio/legado/app/model/webBook/WebBook;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exploreDebug", RSSKeywords.RSS_ITEM_URL, "(Lio/legado/app/model/webBook/WebBook;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "infoDebug", "(Lio/legado/app/model/webBook/WebBook;Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "log", "message", "sourceUrl", "msg", "isHtml", PackageDocumentBase.PREFIX_OPF, "searchDebug", "key", "startDebug", "tocDebug", "reader-pro"})
public final class Debugger implements DebugLog {

    @NotNull
    private final Function1<String, Unit> logMsg;

    @NotNull
    private final SimpleDateFormat debugTimeFormat;
    private long startTime;

    /* JADX INFO: renamed from: io.legado.app.model.Debugger$contentDebug$1, reason: invalid class name */
    /* JADX INFO: compiled from: Debugger.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/Debugger$contentDebug$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Debugger.kt", l = {184}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "webBook"}, m = "contentDebug", c = "io.legado.app.model.Debugger")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return Debugger.this.contentDebug(null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.Debugger$exploreDebug$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Debugger.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/Debugger$exploreDebug$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Debugger.kt", l = {95, 102}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "webBook"}, m = "exploreDebug", c = "io.legado.app.model.Debugger")
    static final class C01521 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01521(Continuation<? super C01521> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return Debugger.this.exploreDebug(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.Debugger$infoDebug$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Debugger.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/Debugger$infoDebug$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Debugger.kt", l = {138, 143}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "webBook"}, m = "infoDebug", c = "io.legado.app.model.Debugger")
    static final class C01531 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01531(Continuation<? super C01531> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return Debugger.this.infoDebug(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.Debugger$searchDebug$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Debugger.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/Debugger$searchDebug$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Debugger.kt", l = {117, 124}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "webBook"}, m = "searchDebug", c = "io.legado.app.model.Debugger")
    static final class C01541 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01541(Continuation<? super C01541> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return Debugger.this.searchDebug(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.Debugger$tocDebug$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Debugger.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/Debugger$tocDebug$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Debugger.kt", l = {155, 164}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "webBook", "book"}, m = "tocDebug", c = "io.legado.app.model.Debugger")
    static final class C01551 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01551(Continuation<? super C01551> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return Debugger.this.tocDebug(null, null, (Continuation) this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Debugger(@NotNull Function1<? super String, Unit> logMsg) {
        Intrinsics.checkNotNullParameter(logMsg, "logMsg");
        this.logMsg = logMsg;
        this.debugTimeFormat = new SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault());
        this.startTime = System.currentTimeMillis();
    }

    @NotNull
    public final Function1<String, Unit> getLogMsg() {
        return this.logMsg;
    }

    public final void log(@Nullable String sourceUrl, @Nullable String msg) {
        log(sourceUrl, msg, false);
    }

    @Override // io.legado.app.model.DebugLog
    public void log(@NotNull String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        String time = this.debugTimeFormat.format(new Date(System.currentTimeMillis() - this.startTime));
        this.logMsg.invoke(((Object) time) + ' ' + message);
    }

    @Override // io.legado.app.model.DebugLog
    public void log(@Nullable String sourceUrl, @Nullable String msg, boolean isHtml) {
        if (sourceUrl == null || msg == null) {
            return;
        }
        DebuggerKt.logger.info("sourceUrl: {}, msg: {}", sourceUrl, msg);
        String printMsg = msg;
        if (isHtml) {
            printMsg = HtmlFormatter.format$default(HtmlFormatter.INSTANCE, msg, null, 2, null);
        }
        String time = this.debugTimeFormat.format(new Date(System.currentTimeMillis() - this.startTime));
        this.logMsg.invoke(new StringBuilder().append((Object) time).append(' ').append((Object) printMsg).toString());
    }

    @Nullable
    public final Object startDebug(@NotNull WebBook webBook, @NotNull String key, @NotNull Continuation<? super Unit> $completion) throws Throwable {
        BookSource bookSource = webBook.getBookSource();
        webBook.setDebugLogger(this);
        this.startTime = System.currentTimeMillis();
        if (StringExtensionsKt.isAbsUrl(key)) {
            Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
            book.setOrigin(bookSource.getBookSourceUrl());
            book.setBookUrl(key);
            log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访问详情页:", key));
            Object objInfoDebug = infoDebug(webBook, book, $completion);
            return objInfoDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objInfoDebug : Unit.INSTANCE;
        }
        if (StringsKt.contains$default(key, "::", false, 2, (Object) null)) {
            String url = StringsKt.substringAfter$default(key, "::", (String) null, 2, (Object) null);
            log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访问发现页:", url));
            Object objExploreDebug = exploreDebug(webBook, url, $completion);
            return objExploreDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objExploreDebug : Unit.INSTANCE;
        }
        if (StringsKt.startsWith$default(key, "++", false, 2, (Object) null)) {
            if (key == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String url2 = key.substring(2);
            Intrinsics.checkNotNullExpressionValue(url2, "(this as java.lang.String).substring(startIndex)");
            Book book2 = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
            book2.setOrigin(bookSource.getBookSourceUrl());
            book2.setTocUrl(url2);
            log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访目录页:", url2));
            Object obj = tocDebug(webBook, book2, $completion);
            return obj == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? obj : Unit.INSTANCE;
        }
        if (!StringsKt.startsWith$default(key, "--", false, 2, (Object) null)) {
            log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始搜索关键字:", key));
            Object objSearchDebug = searchDebug(webBook, key, $completion);
            return objSearchDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objSearchDebug : Unit.INSTANCE;
        }
        if (key == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String url3 = key.substring(2);
        Intrinsics.checkNotNullExpressionValue(url3, "(this as java.lang.String).substring(startIndex)");
        Book book3 = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
        book3.setOrigin(bookSource.getBookSourceUrl());
        log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访正文页:", url3));
        BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
        chapter.setTitle("调试");
        chapter.setUrl(url3);
        Object objContentDebug = contentDebug(webBook, book3, chapter, null, $completion);
        return objContentDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objContentDebug : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x020e  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object exploreDebug(WebBook webBook, String url, Continuation<? super Unit> $completion) throws Throwable {
        C01521 c01521;
        Object obj;
        Object obj2;
        Throwable it;
        Object objExploreBook;
        if ($completion instanceof C01521) {
            c01521 = (C01521) $completion;
            if ((c01521.label & Integer.MIN_VALUE) != 0) {
                c01521.label -= Integer.MIN_VALUE;
            } else {
                c01521 = new C01521($completion);
            }
        }
        Object $result = c01521.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        switch (c01521.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                webBook.setDebugLogger(this);
                log("︾开始解析发现页");
                Result.Companion companion2 = Result.Companion;
                Debugger debugger = this;
                c01521.L$0 = this;
                c01521.L$1 = webBook;
                c01521.label = 1;
                objExploreBook = webBook.exploreBook(url, Boxing.boxInt(1), c01521);
                if (objExploreBook == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = Result.constructor-impl((List) objExploreBook);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                    List exploreBooks = (List) obj2;
                    if (!exploreBooks.isEmpty()) {
                        this.log("┌发现结果列表");
                        this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(exploreBooks)));
                        this.log(webBook.getSourceUrl(), "︽发现页解析完成\n\n");
                        c01521.L$0 = this;
                        c01521.L$1 = webBook;
                        c01521.L$2 = obj2;
                        c01521.label = 2;
                        if (this.infoDebug(webBook, ((SearchBook) exploreBooks.get(0)).toBook(), c01521) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        this.log(webBook.getSourceUrl(), "︽未获取到书籍");
                    }
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                    this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", it.getLocalizedMessage()));
                    throw it;
                }
                return Unit.INSTANCE;
            case 1:
                webBook = (WebBook) c01521.L$1;
                this = (Debugger) c01521.L$0;
                ResultKt.throwOnFailure($result);
                objExploreBook = $result;
                obj = Result.constructor-impl((List) objExploreBook);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                }
                break;
            case 2:
                obj2 = c01521.L$2;
                webBook = (WebBook) c01521.L$1;
                this = (Debugger) c01521.L$0;
                ResultKt.throwOnFailure($result);
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0215  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchDebug(WebBook webBook, String key, Continuation<? super Unit> $completion) throws Throwable {
        C01541 c01541;
        Object obj;
        Object obj2;
        Throwable it;
        Object objSearchBook;
        if ($completion instanceof C01541) {
            c01541 = (C01541) $completion;
            if ((c01541.label & Integer.MIN_VALUE) != 0) {
                c01541.label -= Integer.MIN_VALUE;
            } else {
                c01541 = new C01541($completion);
            }
        }
        Object $result = c01541.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        switch (c01541.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                webBook.setDebugLogger(this);
                DebugLog.DefaultImpls.log$default(this, null, "︾开始解析搜索页", false, 5, null);
                Result.Companion companion2 = Result.Companion;
                Debugger debugger = this;
                c01541.L$0 = this;
                c01541.L$1 = webBook;
                c01541.label = 1;
                objSearchBook = webBook.searchBook(key, Boxing.boxInt(1), c01541);
                if (objSearchBook == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = Result.constructor-impl((List) objSearchBook);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                    List searchBooks = (List) obj2;
                    if (!searchBooks.isEmpty()) {
                        this.log("┌搜索结果列表");
                        this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(searchBooks)));
                        this.log(webBook.getSourceUrl(), "︽搜索页解析完成\n\n");
                        c01541.L$0 = this;
                        c01541.L$1 = webBook;
                        c01541.L$2 = obj2;
                        c01541.label = 2;
                        if (this.infoDebug(webBook, ((SearchBook) searchBooks.get(0)).toBook(), c01541) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        this.log(webBook.getSourceUrl(), "︽未获取到书籍");
                    }
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                    this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", it.getLocalizedMessage()));
                    throw it;
                }
                return Unit.INSTANCE;
            case 1:
                webBook = (WebBook) c01541.L$1;
                this = (Debugger) c01541.L$0;
                ResultKt.throwOnFailure($result);
                objSearchBook = $result;
                obj = Result.constructor-impl((List) objSearchBook);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                }
                break;
            case 2:
                obj2 = c01541.L$2;
                webBook = (WebBook) c01541.L$1;
                this = (Debugger) c01541.L$0;
                ResultKt.throwOnFailure($result);
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x010b  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object infoDebug(WebBook webBook, Book book, Continuation<? super Unit> $completion) throws Throwable {
        C01531 c01531;
        Object obj;
        Object obj2;
        Throwable it;
        Object bookInfo$default;
        if ($completion instanceof C01531) {
            c01531 = (C01531) $completion;
            if ((c01531.label & Integer.MIN_VALUE) != 0) {
                c01531.label -= Integer.MIN_VALUE;
            } else {
                c01531 = new C01531($completion);
            }
        }
        Object $result = c01531.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        switch (c01531.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                webBook.setDebugLogger(this);
                DebugLog.DefaultImpls.log$default(this, null, "︾开始解析详情页", false, 5, null);
                Result.Companion companion2 = Result.Companion;
                Debugger debugger = this;
                c01531.L$0 = this;
                c01531.L$1 = webBook;
                c01531.label = 1;
                bookInfo$default = WebBook.getBookInfo$default(webBook, book, false, (Continuation) c01531, 2, (Object) null);
                if (bookInfo$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = Result.constructor-impl((Book) bookInfo$default);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                    Book it2 = (Book) obj2;
                    this.log("┌书籍详情");
                    this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(it2)));
                    this.log(webBook.getSourceUrl(), "︽详情页解析完成\n\n");
                    c01531.L$0 = this;
                    c01531.L$1 = webBook;
                    c01531.L$2 = obj2;
                    c01531.label = 2;
                    if (this.tocDebug(webBook, it2, c01531) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it == null) {
                    return Unit.INSTANCE;
                }
                this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", it.getLocalizedMessage()));
                throw it;
            case 1:
                webBook = (WebBook) c01531.L$1;
                this = (Debugger) c01531.L$0;
                ResultKt.throwOnFailure($result);
                bookInfo$default = $result;
                obj = Result.constructor-impl((Book) bookInfo$default);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it == null) {
                }
                break;
            case 2:
                obj2 = c01531.L$2;
                webBook = (WebBook) c01531.L$1;
                this = (Debugger) c01531.L$0;
                ResultKt.throwOnFailure($result);
                it = Result.exceptionOrNull-impl(obj2);
                if (it == null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0152  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01fe  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object tocDebug(WebBook webBook, Book book, Continuation<? super Unit> $completion) throws Throwable {
        C01551 c01551;
        Object obj;
        Object obj2;
        Throwable it;
        List chapterList;
        Object chapterList2;
        if ($completion instanceof C01551) {
            c01551 = (C01551) $completion;
            if ((c01551.label & Integer.MIN_VALUE) != 0) {
                c01551.label -= Integer.MIN_VALUE;
            } else {
                c01551 = new C01551($completion);
            }
        }
        Object $result = c01551.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        switch (c01551.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                webBook.setDebugLogger(this);
                DebugLog.DefaultImpls.log$default(this, null, "︾开始解析目录页", false, 5, null);
                Result.Companion companion2 = Result.Companion;
                Debugger debugger = this;
                c01551.L$0 = this;
                c01551.L$1 = webBook;
                c01551.L$2 = book;
                c01551.label = 1;
                chapterList2 = webBook.getChapterList(book, c01551);
                if (chapterList2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                obj = Result.constructor-impl((List) chapterList2);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2) && (chapterList = (List) obj2) != null) {
                    if (!(chapterList.isEmpty())) {
                        this.log("┌目录列表");
                        this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(chapterList)));
                        this.log(webBook.getSourceUrl(), "︽目录页解析完成\n\n");
                        String nextChapterUrl = chapterList.size() > 1 ? ((BookChapter) chapterList.get(1)).getUrl() : null;
                        c01551.L$0 = this;
                        c01551.L$1 = webBook;
                        c01551.L$2 = obj2;
                        c01551.label = 2;
                        if (this.contentDebug(webBook, book, (BookChapter) chapterList.get(0), nextChapterUrl, c01551) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        this.log(webBook.getSourceUrl(), "︽目录列表为空");
                    }
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                    this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", it.getLocalizedMessage()));
                    throw it;
                }
                return Unit.INSTANCE;
            case 1:
                book = (Book) c01551.L$2;
                webBook = (WebBook) c01551.L$1;
                this = (Debugger) c01551.L$0;
                ResultKt.throwOnFailure($result);
                chapterList2 = $result;
                obj = Result.constructor-impl((List) chapterList2);
                obj2 = obj;
                if (Result.isSuccess-impl(obj2)) {
                    if (!(chapterList.isEmpty())) {
                    }
                }
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                }
                break;
            case 2:
                obj2 = c01551.L$2;
                webBook = (WebBook) c01551.L$1;
                this = (Debugger) c01551.L$0;
                ResultKt.throwOnFailure($result);
                it = Result.exceptionOrNull-impl(obj2);
                if (it != null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object contentDebug(WebBook webBook, Book book, BookChapter bookChapter, String nextChapterUrl, Continuation<? super Unit> $completion) {
        AnonymousClass1 anonymousClass1;
        Object obj;
        Object bookContent;
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
        try {
            switch (anonymousClass1.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    webBook.setDebugLogger(this);
                    log(webBook.getSourceUrl(), "︾开始解析正文页");
                    Result.Companion companion = Result.Companion;
                    Debugger debugger = this;
                    anonymousClass1.L$0 = this;
                    anonymousClass1.L$1 = webBook;
                    anonymousClass1.label = 1;
                    bookContent = webBook.getBookContent(book, bookChapter, nextChapterUrl, anonymousClass1);
                    if (bookContent == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    webBook = (WebBook) anonymousClass1.L$1;
                    this = (Debugger) anonymousClass1.L$0;
                    ResultKt.throwOnFailure($result);
                    bookContent = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            obj = Result.constructor-impl((String) bookContent);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        Object obj2 = obj;
        if (Result.isSuccess-impl(obj2)) {
            this.log("┌正文内容");
            this.log(Intrinsics.stringPlus("└", (String) obj2));
            this.log(webBook.getSourceUrl(), "︽正文页解析完成");
        }
        Throwable it = Result.exceptionOrNull-impl(obj2);
        if (it != null) {
            this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", it.getLocalizedMessage()));
        }
        return Unit.INSTANCE;
    }
}
