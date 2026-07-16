// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model;

import kotlin.Result$Companion;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.Collection;
import io.legado.app.data.entities.SearchBook;
import java.util.List;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.Result;
import kotlin.ResultKt;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.BookChapter;
import kotlin.text.StringsKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.data.entities.Book;
import io.legado.app.utils.StringExtensionsKt;
import kotlin.coroutines.Continuation;
import io.legado.app.model.webBook.WebBook;
import kotlin.text.Regex;
import io.legado.app.utils.HtmlFormatter;
import java.util.Date;
import org.jetbrains.annotations.Nullable;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import java.text.SimpleDateFormat;
import org.jetbrains.annotations.NotNull;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003?\u0006\u0002\u0010\u0006J3\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0004H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0015J!\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0004H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J!\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u001aJ\u0010\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u0004H\u0016J\u001a\u0010\u001b\u001a\u00020\u00052\b\u0010\u001d\u001a\u0004\u0018\u00010\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u0004J$\u0010\u001b\u001a\u00020\u00052\b\u0010\u001d\u001a\u0004\u0018\u00010\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001f\u001a\u00020 H\u0016J!\u0010!\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u0004H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J!\u0010#\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J!\u0010$\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u001aR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004?\u0006\u0002\n\u0000R\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003?\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006%" }, d2 = { "Lio/legado/app/model/Debugger;", "Lio/legado/app/model/DebugLog;", "logMsg", "Lkotlin/Function1;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "debugTimeFormat", "Ljava/text/SimpleDateFormat;", "getLogMsg", "()Lkotlin/jvm/functions/Function1;", "startTime", "", "contentDebug", "webBook", "Lio/legado/app/model/webBook/WebBook;", "book", "Lio/legado/app/data/entities/Book;", "bookChapter", "Lio/legado/app/data/entities/BookChapter;", "nextChapterUrl", "(Lio/legado/app/model/webBook/WebBook;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exploreDebug", "url", "(Lio/legado/app/model/webBook/WebBook;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "infoDebug", "(Lio/legado/app/model/webBook/WebBook;Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "log", "message", "sourceUrl", "msg", "isHtml", "", "searchDebug", "key", "startDebug", "tocDebug", "reader-pro" })
public final class Debugger implements DebugLog
{
    @NotNull
    private final Function1<String, Unit> logMsg;
    @NotNull
    private final SimpleDateFormat debugTimeFormat;
    private long startTime;
    
    public Debugger(@NotNull final Function1<? super String, Unit> logMsg) {
        Intrinsics.checkNotNullParameter((Object)logMsg, "logMsg");
        this.logMsg = (Function1<String, Unit>)logMsg;
        this.debugTimeFormat = new SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault());
        this.startTime = System.currentTimeMillis();
    }
    
    @NotNull
    public final Function1<String, Unit> getLogMsg() {
        return this.logMsg;
    }
    
    public final void log(@Nullable final String sourceUrl, @Nullable final String msg) {
        this.log(sourceUrl, msg, false);
    }
    
    @Override
    public void log(@NotNull final String message) {
        Intrinsics.checkNotNullParameter((Object)message, "message");
        final String time = this.debugTimeFormat.format(new Date(System.currentTimeMillis() - this.startTime));
        this.logMsg.invoke((Object)new StringBuilder().append((Object)time).append(' ').append(message).toString());
    }
    
    @Override
    public void log(@Nullable final String sourceUrl, @Nullable final String msg, final boolean isHtml) {
        if (sourceUrl == null || msg == null) {
            return;
        }
        DebuggerKt.access$getLogger$p().info("sourceUrl: {}, msg: {}", (Object)sourceUrl, (Object)msg);
        String printMsg = msg;
        if (isHtml) {
            printMsg = HtmlFormatter.format$default(HtmlFormatter.INSTANCE, msg, null, 2, null);
        }
        final String time = this.debugTimeFormat.format(new Date(System.currentTimeMillis() - this.startTime));
        printMsg = new StringBuilder().append((Object)time).append(' ').append((Object)printMsg).toString();
        this.logMsg.invoke((Object)printMsg);
    }
    
    @Nullable
    public final Object startDebug(@NotNull final WebBook webBook, @NotNull final String key, @NotNull final Continuation<? super Unit> $completion) {
        final BookSource bookSource = webBook.getBookSource();
        webBook.setDebugLogger(this);
        this.startTime = System.currentTimeMillis();
        if (StringExtensionsKt.isAbsUrl(key)) {
            final Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
            book.setOrigin(bookSource.getBookSourceUrl());
            book.setBookUrl(key);
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u21d2\u5f00\u59cb\u8bbf\u95ee\u8be6\u60c5\u9875:", (Object)key));
            final Object infoDebug = this.infoDebug(webBook, book, $completion);
            if (infoDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return infoDebug;
            }
            return Unit.INSTANCE;
        }
        else if (StringsKt.contains$default((CharSequence)key, (CharSequence)"::", false, 2, (Object)null)) {
            final String url = StringsKt.substringAfter$default(key, "::", (String)null, 2, (Object)null);
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u21d2\u5f00\u59cb\u8bbf\u95ee\u53d1\u73b0\u9875:", (Object)url));
            final Object exploreDebug = this.exploreDebug(webBook, url, $completion);
            if (exploreDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return exploreDebug;
            }
            return Unit.INSTANCE;
        }
        else if (StringsKt.startsWith$default(key, "++", false, 2, (Object)null)) {
            final int beginIndex = 2;
            if (key == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring = key.substring(beginIndex);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            final String url = substring;
            final Book book2 = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
            book2.setOrigin(bookSource.getBookSourceUrl());
            book2.setTocUrl(url);
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u21d2\u5f00\u59cb\u8bbf\u76ee\u5f55\u9875:", (Object)url));
            final Object tocDebug = this.tocDebug(webBook, book2, $completion);
            if (tocDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return tocDebug;
            }
            return Unit.INSTANCE;
        }
        else if (StringsKt.startsWith$default(key, "--", false, 2, (Object)null)) {
            final int beginIndex2 = 2;
            if (key == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring2 = key.substring(beginIndex2);
            Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.String).substring(startIndex)");
            final String url = substring2;
            final Book book2 = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
            book2.setOrigin(bookSource.getBookSourceUrl());
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u21d2\u5f00\u59cb\u8bbf\u6b63\u6587\u9875:", (Object)url));
            final BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
            chapter.setTitle("\u8c03\u8bd5");
            chapter.setUrl(url);
            final Object contentDebug = this.contentDebug(webBook, book2, chapter, null, $completion);
            if (contentDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return contentDebug;
            }
            return Unit.INSTANCE;
        }
        else {
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u21d2\u5f00\u59cb\u641c\u7d22\u5173\u952e\u5b57:", (Object)key));
            final Object searchDebug = this.searchDebug(webBook, key, $completion);
            if (searchDebug == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return searchDebug;
            }
            return Unit.INSTANCE;
        }
    }
    
    private final Object exploreDebug(WebBook webBook, final String url, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof Debugger$exploreDebug.Debugger$exploreDebug$1) {
                final Debugger$exploreDebug.Debugger$exploreDebug$1 debugger$exploreDebug$1 = (Debugger$exploreDebug.Debugger$exploreDebug$1)$completion;
                if ((debugger$exploreDebug$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Debugger$exploreDebug.Debugger$exploreDebug$1 debugger$exploreDebug$2 = debugger$exploreDebug$1;
                    debugger$exploreDebug$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new Debugger$exploreDebug.Debugger$exploreDebug$1(this, (Continuation)$completion);
        }
        final Object $result = ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object l$2 = null;
        while (true) {
            switch (((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    webBook.setDebugLogger(this);
                    this.log("\ufe3e\u5f00\u59cb\u89e3\u6790\u53d1\u73b0\u9875");
                    final Debugger debugger = this;
                    Object o = null;
                    Label_0243: {
                        try {
                            final Result$Companion companion = Result.Companion;
                            final Debugger debugger2 = debugger;
                            int n = 0;
                            final WebBook webBook2 = webBook;
                            final Integer boxInt = Boxing.boxInt(1);
                            final Continuation $completion2 = $continuation;
                            ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$0 = this;
                            ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$1 = webBook;
                            ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).label = 1;
                            Object exploreBook;
                            if ((exploreBook = webBook2.exploreBook(url, boxInt, (Continuation<? super List<SearchBook>>)$completion2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                o = Result.constructor-impl((Object)exploreBook);
                                break Label_0243;
                                n = 0;
                                webBook = (WebBook)((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$1;
                                this = (Debugger)((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                exploreBook = $result;
                                continue;
                            }
                        }
                        catch (final Throwable t) {
                            final Result$Companion companion2 = Result.Companion;
                            o = Result.constructor-impl(ResultKt.createFailure(t));
                        }
                    }
                    l$2 = o;
                    if (!Result.isSuccess-impl(l$2)) {
                        break;
                    }
                    final List exploreBooks = (List)l$2;
                    final int n2 = 0;
                    final List it = exploreBooks;
                    final int n3 = 0;
                    if (exploreBooks.isEmpty()) {
                        this.log(webBook.getSourceUrl(), "\ufe3d\u672a\u83b7\u53d6\u5230\u4e66\u7c4d");
                        break;
                    }
                    this.log("\u250c\u53d1\u73b0\u7ed3\u679c\u5217\u8868");
                    this.log(Intrinsics.stringPlus("\u2514", (Object)GsonExtensionsKt.getGSON().toJson((Object)exploreBooks)));
                    this.log(webBook.getSourceUrl(), "\ufe3d\u53d1\u73b0\u9875\u89e3\u6790\u5b8c\u6210\n\n");
                    final Debugger debugger3 = this;
                    final WebBook webBook3 = webBook;
                    final Book book = exploreBooks.get(0).toBook();
                    final Continuation $completion3 = $continuation;
                    ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$0 = this;
                    ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$1 = webBook;
                    ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$2 = l$2;
                    ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).label = 2;
                    if (debugger3.infoDebug(webBook3, book, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    continue;
                }
                case 2: {
                    final int n2 = 0;
                    final int n3 = 0;
                    l$2 = ((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$2;
                    webBook = (WebBook)((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$1;
                    this = (Debugger)((Debugger$exploreDebug.Debugger$exploreDebug$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            break;
        }
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(l$2);
        if (exceptionOrNull-impl != null) {
            final Throwable it2 = exceptionOrNull-impl;
            final int n4 = 0;
            this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", (Object)it2.getLocalizedMessage()));
            throw it2;
        }
        return Unit.INSTANCE;
    }
    
    private final Object searchDebug(WebBook webBook, final String key, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof Debugger$searchDebug.Debugger$searchDebug$1) {
                final Debugger$searchDebug.Debugger$searchDebug$1 debugger$searchDebug$1 = (Debugger$searchDebug.Debugger$searchDebug$1)$completion;
                if ((debugger$searchDebug$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Debugger$searchDebug.Debugger$searchDebug$1 debugger$searchDebug$2 = debugger$searchDebug$1;
                    debugger$searchDebug$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new Debugger$searchDebug.Debugger$searchDebug$1(this, (Continuation)$completion);
        }
        final Object $result = ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object l$2 = null;
        while (true) {
            switch (((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    webBook.setDebugLogger(this);
                    DefaultImpls.log$default(this, null, "\ufe3e\u5f00\u59cb\u89e3\u6790\u641c\u7d22\u9875", false, 5, null);
                    final Debugger debugger = this;
                    Object o = null;
                    Label_0250: {
                        try {
                            final Result$Companion companion = Result.Companion;
                            final Debugger debugger2 = debugger;
                            int n = 0;
                            final WebBook webBook2 = webBook;
                            final Integer boxInt = Boxing.boxInt(1);
                            final Continuation $completion2 = $continuation;
                            ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$0 = this;
                            ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$1 = webBook;
                            ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).label = 1;
                            Object searchBook;
                            if ((searchBook = webBook2.searchBook(key, boxInt, (Continuation<? super List<SearchBook>>)$completion2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                o = Result.constructor-impl((Object)searchBook);
                                break Label_0250;
                                n = 0;
                                webBook = (WebBook)((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$1;
                                this = (Debugger)((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                searchBook = $result;
                                continue;
                            }
                        }
                        catch (final Throwable t) {
                            final Result$Companion companion2 = Result.Companion;
                            o = Result.constructor-impl(ResultKt.createFailure(t));
                        }
                    }
                    l$2 = o;
                    if (!Result.isSuccess-impl(l$2)) {
                        break;
                    }
                    final List searchBooks = (List)l$2;
                    final int n2 = 0;
                    final List it = searchBooks;
                    final int n3 = 0;
                    if (searchBooks.isEmpty()) {
                        this.log(webBook.getSourceUrl(), "\ufe3d\u672a\u83b7\u53d6\u5230\u4e66\u7c4d");
                        break;
                    }
                    this.log("\u250c\u641c\u7d22\u7ed3\u679c\u5217\u8868");
                    this.log(Intrinsics.stringPlus("\u2514", (Object)GsonExtensionsKt.getGSON().toJson((Object)searchBooks)));
                    this.log(webBook.getSourceUrl(), "\ufe3d\u641c\u7d22\u9875\u89e3\u6790\u5b8c\u6210\n\n");
                    final Debugger debugger3 = this;
                    final WebBook webBook3 = webBook;
                    final Book book = searchBooks.get(0).toBook();
                    final Continuation $completion3 = $continuation;
                    ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$0 = this;
                    ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$1 = webBook;
                    ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$2 = l$2;
                    ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).label = 2;
                    if (debugger3.infoDebug(webBook3, book, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    continue;
                }
                case 2: {
                    final int n2 = 0;
                    final int n3 = 0;
                    l$2 = ((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$2;
                    webBook = (WebBook)((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$1;
                    this = (Debugger)((Debugger$searchDebug.Debugger$searchDebug$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            break;
        }
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(l$2);
        if (exceptionOrNull-impl != null) {
            final Throwable it2 = exceptionOrNull-impl;
            final int n4 = 0;
            this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", (Object)it2.getLocalizedMessage()));
            throw it2;
        }
        return Unit.INSTANCE;
    }
    
    private final Object infoDebug(WebBook webBook, final Book book, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof Debugger$infoDebug.Debugger$infoDebug$1) {
                final Debugger$infoDebug.Debugger$infoDebug$1 debugger$infoDebug$1 = (Debugger$infoDebug.Debugger$infoDebug$1)$completion;
                if ((debugger$infoDebug$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Debugger$infoDebug.Debugger$infoDebug$1 debugger$infoDebug$2 = debugger$infoDebug$1;
                    debugger$infoDebug$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new Debugger$infoDebug.Debugger$infoDebug$1(this, (Continuation)$completion);
        }
        final Object $result = ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object l$2 = null;
        while (true) {
            switch (((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    webBook.setDebugLogger(this);
                    DefaultImpls.log$default(this, null, "\ufe3e\u5f00\u59cb\u89e3\u6790\u8be6\u60c5\u9875", false, 5, null);
                    final Debugger debugger = this;
                    Object o2 = null;
                    Label_0249: {
                        try {
                            final Result$Companion companion = Result.Companion;
                            final Debugger debugger2 = debugger;
                            int n = 0;
                            final WebBook webBook2 = webBook;
                            final boolean b = false;
                            final Continuation continuation = $continuation;
                            final int n2 = 2;
                            final Object o = null;
                            ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$0 = this;
                            ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$1 = webBook;
                            ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).label = 1;
                            Object bookInfo$default;
                            if ((bookInfo$default = WebBook.getBookInfo$default(webBook2, book, b, continuation, n2, o)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                o2 = Result.constructor-impl((Object)bookInfo$default);
                                break Label_0249;
                                n = 0;
                                webBook = (WebBook)((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$1;
                                this = (Debugger)((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                bookInfo$default = $result;
                                continue;
                            }
                        }
                        catch (final Throwable t) {
                            final Result$Companion companion2 = Result.Companion;
                            o2 = Result.constructor-impl(ResultKt.createFailure(t));
                        }
                    }
                    l$2 = o2;
                    if (!Result.isSuccess-impl(l$2)) {
                        break;
                    }
                    final Book it = (Book)l$2;
                    final int n3 = 0;
                    this.log("\u250c\u4e66\u7c4d\u8be6\u60c5");
                    this.log(Intrinsics.stringPlus("\u2514", (Object)GsonExtensionsKt.getGSON().toJson((Object)it)));
                    this.log(webBook.getSourceUrl(), "\ufe3d\u8be6\u60c5\u9875\u89e3\u6790\u5b8c\u6210\n\n");
                    final Debugger debugger3 = this;
                    final WebBook webBook3 = webBook;
                    final Book book2 = it;
                    final Continuation $completion2 = $continuation;
                    ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$0 = this;
                    ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$1 = webBook;
                    ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$2 = l$2;
                    ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).label = 2;
                    if (debugger3.tocDebug(webBook3, book2, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    continue;
                }
                case 2: {
                    final int n3 = 0;
                    l$2 = ((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$2;
                    webBook = (WebBook)((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$1;
                    this = (Debugger)((Debugger$infoDebug.Debugger$infoDebug$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            break;
        }
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(l$2);
        if (exceptionOrNull-impl != null) {
            final Throwable it2 = exceptionOrNull-impl;
            final int n4 = 0;
            this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", (Object)it2.getLocalizedMessage()));
            throw it2;
        }
        return Unit.INSTANCE;
    }
    
    private final Object tocDebug(WebBook webBook, Book book, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof Debugger$tocDebug.Debugger$tocDebug$1) {
                final Debugger$tocDebug.Debugger$tocDebug$1 debugger$tocDebug$1 = (Debugger$tocDebug.Debugger$tocDebug$1)$completion;
                if ((debugger$tocDebug$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Debugger$tocDebug.Debugger$tocDebug$1 debugger$tocDebug$2 = debugger$tocDebug$1;
                    debugger$tocDebug$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new Debugger$tocDebug.Debugger$tocDebug$1(this, (Continuation)$completion);
        }
        final Object $result = ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object l$2 = null;
        while (true) {
            switch (((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    webBook.setDebugLogger(this);
                    DefaultImpls.log$default(this, null, "\ufe3e\u5f00\u59cb\u89e3\u6790\u76ee\u5f55\u9875", false, 5, null);
                    final Debugger debugger = this;
                    Object o = null;
                    Label_0261: {
                        try {
                            final Result$Companion companion = Result.Companion;
                            final Debugger debugger2 = debugger;
                            int n = 0;
                            final WebBook webBook2 = webBook;
                            final Book book2 = book;
                            final Continuation $completion2 = $continuation;
                            ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$0 = this;
                            ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$1 = webBook;
                            ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$2 = book;
                            ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).label = 1;
                            Object chapterList2;
                            if ((chapterList2 = webBook2.getChapterList(book2, (Continuation<? super List<BookChapter>>)$completion2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                o = Result.constructor-impl((Object)chapterList2);
                                break Label_0261;
                                n = 0;
                                book = (Book)((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$2;
                                webBook = (WebBook)((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$1;
                                this = (Debugger)((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                chapterList2 = $result;
                                continue;
                            }
                        }
                        catch (final Throwable t) {
                            final Result$Companion companion2 = Result.Companion;
                            o = Result.constructor-impl(ResultKt.createFailure(t));
                        }
                    }
                    l$2 = o;
                    if (!Result.isSuccess-impl(l$2)) {
                        break;
                    }
                    final List chapterList = (List)l$2;
                    final int n2 = 0;
                    final List list = chapterList;
                    if (list == null) {
                        break;
                    }
                    final List it = list;
                    final int n3 = 0;
                    if (it.isEmpty()) {
                        this.log(webBook.getSourceUrl(), "\ufe3d\u76ee\u5f55\u5217\u8868\u4e3a\u7a7a");
                        break;
                    }
                    this.log("\u250c\u76ee\u5f55\u5217\u8868");
                    this.log(Intrinsics.stringPlus("\u2514", (Object)GsonExtensionsKt.getGSON().toJson((Object)it)));
                    this.log(webBook.getSourceUrl(), "\ufe3d\u76ee\u5f55\u9875\u89e3\u6790\u5b8c\u6210\n\n");
                    final String nextChapterUrl = (it.size() > 1) ? it.get(1).getUrl() : null;
                    final Debugger debugger3 = this;
                    final WebBook webBook3 = webBook;
                    final Book book3 = book;
                    final BookChapter bookChapter = it.get(0);
                    final String nextChapterUrl2 = nextChapterUrl;
                    final Continuation $completion3 = $continuation;
                    ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$0 = this;
                    ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$1 = webBook;
                    ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$2 = l$2;
                    ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).label = 2;
                    if (debugger3.contentDebug(webBook3, book3, bookChapter, nextChapterUrl2, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    continue;
                }
                case 2: {
                    final int n2 = 0;
                    final int n3 = 0;
                    l$2 = ((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$2;
                    webBook = (WebBook)((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$1;
                    this = (Debugger)((Debugger$tocDebug.Debugger$tocDebug$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            break;
        }
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(l$2);
        if (exceptionOrNull-impl != null) {
            final Throwable it2 = exceptionOrNull-impl;
            final int n4 = 0;
            this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", (Object)it2.getLocalizedMessage()));
            throw it2;
        }
        return Unit.INSTANCE;
    }
    
    private final Object contentDebug(WebBook webBook, final Book book, final BookChapter bookChapter, final String nextChapterUrl, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0055: {
            if ($completion instanceof Debugger$contentDebug.Debugger$contentDebug$1) {
                final Debugger$contentDebug.Debugger$contentDebug$1 debugger$contentDebug$1 = (Debugger$contentDebug.Debugger$contentDebug$1)$completion;
                if ((debugger$contentDebug$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Debugger$contentDebug.Debugger$contentDebug$1 debugger$contentDebug$2 = debugger$contentDebug$1;
                    debugger$contentDebug$2.label -= Integer.MIN_VALUE;
                    break Label_0055;
                }
            }
            $continuation = (Continuation)new Debugger$contentDebug.Debugger$contentDebug$1(this, (Continuation)$completion);
        }
        final Object $result = ((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        while (true) {
            switch (((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    webBook.setDebugLogger(this);
                    this.log(webBook.getSourceUrl(), "\ufe3e\u5f00\u59cb\u89e3\u6790\u6b63\u6587\u9875");
                    final Debugger debugger = this;
                    Object o = null;
                    Label_0246: {
                        try {
                            final Result$Companion companion = Result.Companion;
                            final Debugger debugger2 = debugger;
                            int n = 0;
                            final WebBook webBook2 = webBook;
                            final Continuation $completion2 = $continuation;
                            ((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).L$0 = this;
                            ((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).L$1 = webBook;
                            ((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).label = 1;
                            Object bookContent;
                            if ((bookContent = webBook2.getBookContent(book, bookChapter, nextChapterUrl, (Continuation<? super String>)$completion2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                o = Result.constructor-impl((Object)bookContent);
                                break Label_0246;
                                n = 0;
                                webBook = (WebBook)((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).L$1;
                                this = (Debugger)((Debugger$contentDebug.Debugger$contentDebug$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                bookContent = $result;
                                continue;
                            }
                        }
                        catch (final Throwable t) {
                            final Result$Companion companion2 = Result.Companion;
                            o = Result.constructor-impl(ResultKt.createFailure(t));
                        }
                    }
                    final Object o2 = o;
                    if (Result.isSuccess-impl(o2)) {
                        final String it = (String)o2;
                        final int n2 = 0;
                        this.log("\u250c\u6b63\u6587\u5185\u5bb9");
                        this.log(Intrinsics.stringPlus("\u2514", (Object)it));
                        this.log(webBook.getSourceUrl(), "\ufe3d\u6b63\u6587\u9875\u89e3\u6790\u5b8c\u6210");
                    }
                    final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(o2);
                    if (exceptionOrNull-impl != null) {
                        final Throwable it2 = exceptionOrNull-impl;
                        final int n3 = 0;
                        this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", (Object)it2.getLocalizedMessage()));
                    }
                    return Unit.INSTANCE;
                }
                case 1: {
                    continue;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            break;
        }
    }
}
