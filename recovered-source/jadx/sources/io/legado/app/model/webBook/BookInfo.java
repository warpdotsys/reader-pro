package io.legado.app.model.webBook;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.help.BookHelp;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.StringExtensionsKt;
import io.legado.app.utils.StringUtils;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.JobKt;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: BookInfo.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/webBook/BookInfo.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JO\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0011JW\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"Lio/legado/app/model/webBook/BookInfo;", PackageDocumentBase.PREFIX_OPF, "()V", "analyzeBookInfo", PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", NCXDocumentV3.XHTMLTgs.body, PackageDocumentBase.PREFIX_OPF, "bookSource", "Lio/legado/app/data/entities/BookSource;", "baseUrl", "redirectUrl", "canReName", PackageDocumentBase.PREFIX_OPF, "debugLog", "Lio/legado/app/model/DebugLog;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "analyzeRule", "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class BookInfo {

    @NotNull
    public static final BookInfo INSTANCE = new BookInfo();

    private BookInfo() {
    }

    public static /* synthetic */ Object analyzeBookInfo$default(BookInfo bookInfo, Book book, String str, BookSource bookSource, String str2, String str3, boolean z, DebugLog debugLog, Continuation continuation, int i, Object obj) throws Exception {
        if ((i & 64) != 0) {
            debugLog = null;
        }
        return bookInfo.analyzeBookInfo(book, str, bookSource, str2, str3, z, debugLog, continuation);
    }

    @Nullable
    public final Object analyzeBookInfo(@NotNull Book book, @Nullable String body, @NotNull BookSource bookSource, @NotNull String baseUrl, @NotNull String redirectUrl, boolean canReName, @Nullable DebugLog debugLog, @NotNull Continuation<? super Unit> $completion) throws Exception {
        if (body == null) {
            throw new Exception(Intrinsics.stringPlus("error_get_web_content: ", baseUrl));
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", baseUrl), false, 4, null);
        }
        AnalyzeRule analyzeRule = new AnalyzeRule(book, bookSource, debugLog);
        AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(baseUrl);
        analyzeRule.setRedirectUrl(redirectUrl);
        Object objAnalyzeBookInfo = analyzeBookInfo(book, body, analyzeRule, bookSource, baseUrl, redirectUrl, canReName, debugLog, $completion);
        return objAnalyzeBookInfo == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objAnalyzeBookInfo : Unit.INSTANCE;
    }

    public static /* synthetic */ Object analyzeBookInfo$default(BookInfo bookInfo, Book book, String str, AnalyzeRule analyzeRule, BookSource bookSource, String str2, String str3, boolean z, DebugLog debugLog, Continuation continuation, int i, Object obj) throws Exception {
        if ((i & Wbxml.EXT_T_0) != 0) {
            debugLog = null;
        }
        return bookInfo.analyzeBookInfo(book, str, analyzeRule, bookSource, str2, str3, z, debugLog, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x020f  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object analyzeBookInfo(@NotNull Book book, @Nullable String body, @NotNull AnalyzeRule analyzeRule, @NotNull BookSource bookSource, @NotNull String baseUrl, @NotNull String redirectUrl, boolean canReName, @Nullable DebugLog debugLog, @NotNull Continuation<? super Unit> $completion) throws Exception {
        boolean z;
        Unit unit;
        String it;
        if (body == null) {
            throw new Exception(Intrinsics.stringPlus("error_get_web_content: ", baseUrl));
        }
        BookInfoRule infoRule = bookSource.getBookInfoRule();
        String it2 = infoRule.getInit();
        if (it2 != null) {
            if (it2.length() > 0) {
                JobKt.ensureActive($completion.getContext());
                if (debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "≡执行详情页初始化规则", false, 4, null);
                    Unit unit2 = Unit.INSTANCE;
                }
                AnalyzeRule.setContent$default(analyzeRule, analyzeRule.getElement(it2), null, 2, null);
            }
            Unit unit3 = Unit.INSTANCE;
        }
        if (canReName) {
            String canReName2 = infoRule.getCanReName();
            if (!(canReName2 == null || StringsKt.isBlank(canReName2))) {
                z = true;
            }
        } else {
            z = false;
        }
        boolean mCanReName = z;
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取书名", false, 4, null);
            Unit unit4 = Unit.INSTANCE;
        }
        String it3 = BookHelp.INSTANCE.formatBookName(AnalyzeRule.getString$default(analyzeRule, infoRule.getName(), (Object) null, false, 6, (Object) null));
        if (it3.length() > 0) {
            if (mCanReName) {
                book.setName(it3);
            } else {
                if (book.getName().length() == 0) {
                }
            }
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", it3), false, 4, null);
            Unit unit5 = Unit.INSTANCE;
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取作者", false, 4, null);
            Unit unit6 = Unit.INSTANCE;
        }
        String it4 = BookHelp.INSTANCE.formatBookAuthor(AnalyzeRule.getString$default(analyzeRule, infoRule.getAuthor(), (Object) null, false, 6, (Object) null));
        if (it4.length() > 0) {
            if (mCanReName) {
                book.setAuthor(it4);
            } else {
                if (book.getAuthor().length() == 0) {
                }
            }
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", it4), false, 4, null);
            Unit unit7 = Unit.INSTANCE;
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取分类", false, 4, null);
            Unit unit8 = Unit.INSTANCE;
        }
        try {
            List stringList$default = AnalyzeRule.getStringList$default(analyzeRule, infoRule.getKind(), (Object) null, false, 6, (Object) null);
            if (stringList$default != null && (it = CollectionsKt.joinToString$default(stringList$default, ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null)) != null) {
                if (it.length() > 0) {
                    book.setKind(it);
                }
                Unit unit9 = Unit.INSTANCE;
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getKind()), false, 4, null);
                Unit unit10 = Unit.INSTANCE;
            }
        } catch (Exception e) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e.getLocalizedMessage()), false, 4, null);
                Unit unit11 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取字数", false, 4, null);
            Unit unit12 = Unit.INSTANCE;
        }
        try {
            String it5 = StringUtils.INSTANCE.wordCountFormat(AnalyzeRule.getString$default(analyzeRule, infoRule.getWordCount(), (Object) null, false, 6, (Object) null));
            if (it5.length() > 0) {
                book.setWordCount(it5);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getWordCount()), false, 4, null);
                Unit unit13 = Unit.INSTANCE;
            }
        } catch (Exception e2) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e2.getLocalizedMessage()), false, 4, null);
                Unit unit14 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取最新章节", false, 4, null);
            Unit unit15 = Unit.INSTANCE;
        }
        try {
            String it6 = AnalyzeRule.getString$default(analyzeRule, infoRule.getLastChapter(), (Object) null, false, 6, (Object) null);
            if (it6.length() > 0) {
                book.setLatestChapterTitle(it6);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getLatestChapterTitle()), false, 4, null);
                Unit unit16 = Unit.INSTANCE;
            }
        } catch (Exception e3) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e3.getLocalizedMessage()), false, 4, null);
                Unit unit17 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取简介", false, 4, null);
            Unit unit18 = Unit.INSTANCE;
        }
        try {
            String it7 = AnalyzeRule.getString$default(analyzeRule, infoRule.getIntro(), (Object) null, false, 6, (Object) null);
            if (it7.length() > 0) {
                book.setIntro(StringExtensionsKt.htmlFormat(it7));
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getIntro()), false, 4, null);
                Unit unit19 = Unit.INSTANCE;
            }
        } catch (Exception e4) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e4.getLocalizedMessage()), false, 4, null);
                Unit unit20 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取封面链接", false, 4, null);
            Unit unit21 = Unit.INSTANCE;
        }
        try {
            String it8 = AnalyzeRule.getString$default(analyzeRule, infoRule.getCoverUrl(), (Object) null, false, 6, (Object) null);
            if (it8.length() > 0) {
                book.setCoverUrl(NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, it8));
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getCoverUrl()), false, 4, null);
                Unit unit22 = Unit.INSTANCE;
            }
        } catch (Exception e5) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e5.getLocalizedMessage()), false, 4, null);
                Unit unit23 = Unit.INSTANCE;
            }
        }
        JobKt.ensureActive($completion.getContext());
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取目录链接", false, 4, null);
            Unit unit24 = Unit.INSTANCE;
        }
        book.setTocUrl(AnalyzeRule.getString$default(analyzeRule, infoRule.getTocUrl(), (Object) null, true, 2, (Object) null));
        if (book.getTocUrl().length() == 0) {
            book.setTocUrl(baseUrl);
        }
        if (Intrinsics.areEqual(book.getTocUrl(), baseUrl)) {
            book.setTocHtml(body);
        }
        if (debugLog == null) {
            unit = null;
        } else {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getTocUrl()), false, 4, null);
            unit = Unit.INSTANCE;
        }
        return unit == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? unit : Unit.INSTANCE;
    }
}
