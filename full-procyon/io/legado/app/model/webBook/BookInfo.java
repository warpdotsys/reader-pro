// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.webBook;

import java.util.List;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.StringExtensionsKt;
import io.legado.app.utils.StringUtils;
import kotlin.jvm.functions.Function1;
import kotlin.collections.CollectionsKt;
import io.legado.app.help.BookHelp;
import kotlin.text.StringsKt;
import kotlinx.coroutines.JobKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.BookSource;
import org.jetbrains.annotations.Nullable;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002JO\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0011JW\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0015" }, d2 = { "Lio/legado/app/model/webBook/BookInfo;", "", "()V", "analyzeBookInfo", "", "book", "Lio/legado/app/data/entities/Book;", "body", "", "bookSource", "Lio/legado/app/data/entities/BookSource;", "baseUrl", "redirectUrl", "canReName", "", "debugLog", "Lio/legado/app/model/DebugLog;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "analyzeRule", "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class BookInfo
{
    @NotNull
    public static final BookInfo INSTANCE;
    
    private BookInfo() {
    }
    
    @Nullable
    public final Object analyzeBookInfo(@NotNull final Book book, @Nullable final String body, @NotNull final BookSource bookSource, @NotNull final String baseUrl, @NotNull final String redirectUrl, final boolean canReName, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super Unit> $completion) throws Exception {
        if (body == null) {
            throw new Exception(Intrinsics.stringPlus("error_get_web_content: ", (Object)baseUrl));
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2261\u83b7\u53d6\u6210\u529f:", (Object)baseUrl), false, 4, null);
        }
        final AnalyzeRule analyzeRule = new AnalyzeRule(book, bookSource, debugLog);
        AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(baseUrl);
        analyzeRule.setRedirectUrl(redirectUrl);
        final Object analyzeBookInfo = this.analyzeBookInfo(book, body, analyzeRule, bookSource, baseUrl, redirectUrl, canReName, debugLog, $completion);
        if (analyzeBookInfo == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return analyzeBookInfo;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object analyzeBookInfo(@NotNull final Book book, @Nullable final String body, @NotNull final AnalyzeRule analyzeRule, @NotNull final BookSource bookSource, @NotNull final String baseUrl, @NotNull final String redirectUrl, final boolean canReName, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super Unit> $completion) throws Exception {
        if (body == null) {
            throw new Exception(Intrinsics.stringPlus("error_get_web_content: ", (Object)baseUrl));
        }
        final BookInfoRule infoRule = bookSource.getBookInfoRule();
        final String init = infoRule.getInit();
        if (init != null) {
            final String it = init;
            final int n = 0;
            if (it.length() > 0) {
                JobKt.ensureActive($completion.getContext());
                if (debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u2261\u6267\u884c\u8be6\u60c5\u9875\u521d\u59cb\u5316\u89c4\u5219", false, 4, null);
                    final Unit instance = Unit.INSTANCE;
                }
                AnalyzeRule.setContent$default(analyzeRule, analyzeRule.getElement(it), null, 2, null);
            }
            final Unit instance2 = Unit.INSTANCE;
        }
        boolean b = false;
        Label_0202: {
            if (canReName) {
                final CharSequence charSequence = infoRule.getCanReName();
                if (charSequence != null && !StringsKt.isBlank(charSequence)) {
                    b = true;
                    break Label_0202;
                }
            }
            b = false;
        }
        final boolean mCanReName = b;
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u4e66\u540d", false, 4, null);
            final Unit instance3 = Unit.INSTANCE;
        }
        String it = BookHelp.INSTANCE.formatBookName(AnalyzeRule.getString$default(analyzeRule, infoRule.getName(), null, false, 6, null));
        final int n2 = 0;
        if (it.length() > 0 && (mCanReName || book.getName().length() == 0)) {
            book.setName(it);
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)it), false, 4, null);
            final Unit instance4 = Unit.INSTANCE;
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u4f5c\u8005", false, 4, null);
            final Unit instance5 = Unit.INSTANCE;
        }
        it = BookHelp.INSTANCE.formatBookAuthor(AnalyzeRule.getString$default(analyzeRule, infoRule.getAuthor(), null, false, 6, null));
        final int n3 = 0;
        if (it.length() > 0 && (mCanReName || book.getAuthor().length() == 0)) {
            book.setAuthor(it);
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)it), false, 4, null);
            final Unit instance6 = Unit.INSTANCE;
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u5206\u7c7b", false, 4, null);
            final Unit instance7 = Unit.INSTANCE;
        }
        try {
            final List stringList$default = AnalyzeRule.getStringList$default(analyzeRule, infoRule.getKind(), null, false, 6, null);
            if (stringList$default != null) {
                final String joinToString$default = CollectionsKt.joinToString$default((Iterable)stringList$default, (CharSequence)",", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
                if (joinToString$default != null) {
                    final String it2 = joinToString$default;
                    final int n4 = 0;
                    if (it2.length() > 0) {
                        book.setKind(it2);
                    }
                    final Unit instance8 = Unit.INSTANCE;
                }
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)book.getKind()), false, 4, null);
                final Unit instance9 = Unit.INSTANCE;
            }
        }
        catch (final Exception e) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, null);
                final Unit instance10 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u5b57\u6570", false, 4, null);
            final Unit instance11 = Unit.INSTANCE;
        }
        try {
            it = StringUtils.INSTANCE.wordCountFormat(AnalyzeRule.getString$default(analyzeRule, infoRule.getWordCount(), null, false, 6, null));
            final int n5 = 0;
            if (it.length() > 0) {
                book.setWordCount(it);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)book.getWordCount()), false, 4, null);
                final Unit instance12 = Unit.INSTANCE;
            }
        }
        catch (final Exception e) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, null);
                final Unit instance13 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u6700\u65b0\u7ae0\u8282", false, 4, null);
            final Unit instance14 = Unit.INSTANCE;
        }
        try {
            it = AnalyzeRule.getString$default(analyzeRule, infoRule.getLastChapter(), null, false, 6, null);
            final int n6 = 0;
            if (it.length() > 0) {
                book.setLatestChapterTitle(it);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)book.getLatestChapterTitle()), false, 4, null);
                final Unit instance15 = Unit.INSTANCE;
            }
        }
        catch (final Exception e) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, null);
                final Unit instance16 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u7b80\u4ecb", false, 4, null);
            final Unit instance17 = Unit.INSTANCE;
        }
        try {
            it = AnalyzeRule.getString$default(analyzeRule, infoRule.getIntro(), null, false, 6, null);
            final int n7 = 0;
            if (it.length() > 0) {
                book.setIntro(StringExtensionsKt.htmlFormat(it));
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)book.getIntro()), false, 4, null);
                final Unit instance18 = Unit.INSTANCE;
            }
        }
        catch (final Exception e) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, null);
                final Unit instance19 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u5c01\u9762\u94fe\u63a5", false, 4, null);
            final Unit instance20 = Unit.INSTANCE;
        }
        try {
            it = AnalyzeRule.getString$default(analyzeRule, infoRule.getCoverUrl(), null, false, 6, null);
            final int n8 = 0;
            if (it.length() > 0) {
                book.setCoverUrl(NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, it));
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)book.getCoverUrl()), false, 4, null);
                final Unit instance21 = Unit.INSTANCE;
            }
        }
        catch (final Exception e) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)e.getLocalizedMessage()), false, 4, null);
                final Unit instance22 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "\u250c\u83b7\u53d6\u76ee\u5f55\u94fe\u63a5", false, 4, null);
            final Unit instance23 = Unit.INSTANCE;
        }
        book.setTocUrl(AnalyzeRule.getString$default(analyzeRule, infoRule.getTocUrl(), null, true, 2, null));
        if (book.getTocUrl().length() == 0) {
            book.setTocUrl(baseUrl);
        }
        if (Intrinsics.areEqual((Object)book.getTocUrl(), (Object)baseUrl)) {
            book.setTocHtml(body);
        }
        Unit instance24;
        Unit unit;
        if (debugLog == null) {
            unit = (instance24 = null);
        }
        else {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("\u2514", (Object)book.getTocUrl()), false, 4, null);
            unit = (instance24 = Unit.INSTANCE);
        }
        if (instance24 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return unit;
        }
        return Unit.INSTANCE;
    }
    
    static {
        INSTANCE = new BookInfo();
    }
}
