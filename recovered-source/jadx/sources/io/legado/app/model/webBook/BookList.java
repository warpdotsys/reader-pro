package io.legado.app.model.webBook;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.SearchBook;
import io.legado.app.data.entities.rule.BookListRule;
import io.legado.app.data.entities.rule.ExploreRule;
import io.legado.app.help.BookHelp;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.StringExtensionsKt;
import io.legado.app.utils.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlinx.coroutines.JobKt;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BookList.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/BookList.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002Ja\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0013JQ\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0018Já\u0001\u0010\u0019\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001b\u001a\u00020\u00102\u0010\u0010\u001c\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010\u001f\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010 \u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010!\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010\"\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010#\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010$\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\u0010\u0010%\u001a\f\u0012\b\u0012\u00060\u001eR\u00020\u00160\u001d2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010&\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006'"}, d2 = {"Lio/legado/app/model/webBook/BookList;", PackageDocumentBase.PREFIX_OPF, "()V", "analyzeBookList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/SearchBook;", "Lkotlin/collections/ArrayList;", NCXDocumentV3.XHTMLTgs.body, PackageDocumentBase.PREFIX_OPF, "bookSource", "Lio/legado/app/data/entities/BookSource;", "analyzeUrl", "Lio/legado/app/model/analyzeRule/AnalyzeUrl;", "baseUrl", "variableBook", "isSearch", PackageDocumentBase.PREFIX_OPF, "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Lio/legado/app/data/entities/SearchBook;ZLio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getInfoItem", "analyzeRule", "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "variable", "(Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Lio/legado/app/model/analyzeRule/AnalyzeUrl;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSearchItem", "item", "log", "ruleName", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;", "ruleBookUrl", "ruleAuthor", "ruleKind", "ruleCoverUrl", "ruleWordCount", "ruleIntro", "ruleLastChapter", "(Ljava/lang/Object;Lio/legado/app/model/analyzeRule/AnalyzeRule;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;ZLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class BookList {

    @NotNull
    public static final BookList INSTANCE = new BookList();

    /* JADX INFO: renamed from: io.legado.app.model.webBook.BookList$analyzeBookList$1, reason: invalid class name */
    /* JADX INFO: compiled from: BookList.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/BookList$analyzeBookList$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookList.kt", l = {46, 73, 88}, i = {2, 2, 2, 2, 2, 2, 2, 2}, s = {"L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "L$14", "L$15"}, n = {"ruleName", "ruleBookUrl", "ruleAuthor", "ruleCoverUrl", "ruleIntro", "ruleKind", "ruleLastChapter", "ruleWordCount"}, m = "analyzeBookList", c = "io.legado.app.model.webBook.BookList")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        Object L$10;
        Object L$11;
        Object L$12;
        Object L$13;
        Object L$14;
        Object L$15;
        Object L$16;
        int I$0;
        int I$1;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookList.this.analyzeBookList(null, null, null, null, null, false, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.webBook.BookList$getInfoItem$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookList.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/BookList$getInfoItem$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookList.kt", l = {125}, i = {0}, s = {"L$0"}, n = {"book"}, m = "getInfoItem", c = "io.legado.app.model.webBook.BookList")
    static final class C01661 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01661(Continuation<? super C01661> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookList.this.getInfoItem(null, null, null, null, null, null, null, (Continuation) this);
        }
    }

    private BookList() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:127:0x0451, code lost:

        kotlin.coroutines.jvm.internal.Boxing.boxBoolean(r29.add(r0));
     */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0603  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0607  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x01a0  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01a4  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x045b  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:114:0x0603 -> B:97:0x0451). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object analyzeBookList(@Nullable String body, @NotNull BookSource bookSource, @NotNull AnalyzeUrl analyzeUrl, @NotNull String baseUrl, @NotNull SearchBook variableBook, boolean isSearch, @Nullable DebugLog debugLog, @NotNull Continuation<? super ArrayList<SearchBook>> $completion) throws Exception {
        AnonymousClass1 anonymousClass1;
        int i;
        int i2;
        Iterator<Object> it;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default2;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default3;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default4;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default5;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default6;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default7;
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default8;
        AnalyzeRule analyzeRule;
        ArrayList bookList;
        Object searchItem;
        Object infoItem;
        Object infoItem2;
        SearchBook searchBook;
        SearchBook searchBook2;
        SearchBook searchBook3;
        ExploreRule searchRule;
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
                bookList = new ArrayList();
                if (body == null) {
                    throw new Exception("error_get_web_content");
                }
                if (debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", analyzeUrl.getRuleUrl()), false, 4, null);
                    Unit unit = Unit.INSTANCE;
                }
                analyzeRule = new AnalyzeRule(variableBook, bookSource, debugLog);
                AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(baseUrl);
                analyzeRule.setRedirectUrl(baseUrl);
                String it2 = bookSource.getBookUrlPattern();
                if (it2 != null) {
                    JobKt.ensureActive(anonymousClass1.getContext());
                    if (new Regex(it2).matches(baseUrl)) {
                        if (debugLog != null) {
                            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "≡链接为详情页", false, 4, null);
                            Unit unit2 = Unit.INSTANCE;
                        }
                        anonymousClass1.L$0 = body;
                        anonymousClass1.L$1 = bookList;
                        anonymousClass1.label = 1;
                        infoItem2 = INSTANCE.getInfoItem(body, analyzeRule, bookSource, analyzeUrl, baseUrl, variableBook.getVariable(), debugLog, anonymousClass1);
                        if (infoItem2 == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        searchBook = (SearchBook) infoItem2;
                        if (searchBook == null) {
                            searchBook.setInfoHtml(body);
                            Boxing.boxBoolean(bookList.add(searchBook));
                        }
                        return bookList;
                    }
                    Unit unit3 = Unit.INSTANCE;
                }
                i2 = 0;
                if (isSearch) {
                    searchRule = bookSource.getSearchRule();
                } else {
                    String bookList2 = bookSource.getExploreRule().getBookList();
                    searchRule = bookList2 == null || StringsKt.isBlank(bookList2) ? bookSource.getSearchRule() : bookSource.getExploreRule();
                }
                BookListRule bookListRule = searchRule;
                String bookList3 = bookListRule.getBookList();
                String ruleList = bookList3 == null ? PackageDocumentBase.PREFIX_OPF : bookList3;
                if (StringsKt.startsWith$default(ruleList, "-", false, 2, (Object) null)) {
                    i2 = 1;
                    String strSubstring = ruleList.substring(1);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    ruleList = strSubstring;
                }
                if (StringsKt.startsWith$default(ruleList, "+", false, 2, (Object) null)) {
                    String str = ruleList;
                    if (str == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring2 = str.substring(1);
                    Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
                    ruleList = strSubstring2;
                }
                if (debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取书籍列表", false, 4, null);
                    Unit unit4 = Unit.INSTANCE;
                }
                List<Object> elements = analyzeRule.getElements(ruleList);
                JobKt.ensureActive(anonymousClass1.getContext());
                if (elements.isEmpty()) {
                    String bookUrlPattern = bookSource.getBookUrlPattern();
                    if (bookUrlPattern == null || bookUrlPattern.length() == 0) {
                        if (debugLog != null) {
                            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "└列表为空,按详情页解析", false, 4, null);
                            Unit unit5 = Unit.INSTANCE;
                        }
                        anonymousClass1.L$0 = body;
                        anonymousClass1.L$1 = bookList;
                        anonymousClass1.label = 2;
                        infoItem = getInfoItem(body, analyzeRule, bookSource, analyzeUrl, baseUrl, variableBook.getVariable(), debugLog, anonymousClass1);
                        if (infoItem == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        searchBook3 = (SearchBook) infoItem;
                        if (searchBook3 == null) {
                            searchBook3.setInfoHtml(body);
                            Boxing.boxBoolean(bookList.add(searchBook3));
                        }
                        return bookList;
                    }
                }
                listSplitSourceRule$default8 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getName(), false, 2, null);
                listSplitSourceRule$default7 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getBookUrl(), false, 2, null);
                listSplitSourceRule$default6 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getAuthor(), false, 2, null);
                listSplitSourceRule$default5 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getCoverUrl(), false, 2, null);
                listSplitSourceRule$default4 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getIntro(), false, 2, null);
                listSplitSourceRule$default3 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getKind(), false, 2, null);
                listSplitSourceRule$default2 = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getLastChapter(), false, 2, null);
                listSplitSourceRule$default = AnalyzeRule.splitSourceRule$default(analyzeRule, bookListRule.getWordCount(), false, 2, null);
                if (debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└列表大小:", Boxing.boxInt(elements.size())), false, 4, null);
                    Unit unit6 = Unit.INSTANCE;
                }
                it = elements.iterator();
                i = 0;
                if (it.hasNext()) {
                    int index = i;
                    i++;
                    Object item = it.next();
                    String variable = variableBook.getVariable();
                    boolean z = index == 0;
                    BookList bookList4 = this;
                    AnalyzeRule analyzeRule2 = analyzeRule;
                    BookSource bookSource2 = bookSource;
                    String str2 = baseUrl;
                    boolean z2 = z;
                    anonymousClass1.L$0 = this;
                    anonymousClass1.L$1 = body;
                    anonymousClass1.L$2 = bookSource;
                    anonymousClass1.L$3 = baseUrl;
                    anonymousClass1.L$4 = variableBook;
                    anonymousClass1.L$5 = debugLog;
                    anonymousClass1.L$6 = bookList;
                    anonymousClass1.L$7 = analyzeRule;
                    anonymousClass1.L$8 = listSplitSourceRule$default8;
                    anonymousClass1.L$9 = listSplitSourceRule$default7;
                    anonymousClass1.L$10 = listSplitSourceRule$default6;
                    anonymousClass1.L$11 = listSplitSourceRule$default5;
                    anonymousClass1.L$12 = listSplitSourceRule$default4;
                    anonymousClass1.L$13 = listSplitSourceRule$default3;
                    anonymousClass1.L$14 = listSplitSourceRule$default2;
                    anonymousClass1.L$15 = listSplitSourceRule$default;
                    anonymousClass1.L$16 = it;
                    anonymousClass1.I$0 = i2;
                    anonymousClass1.I$1 = i;
                    anonymousClass1.label = 3;
                    searchItem = bookList4.getSearchItem(item, analyzeRule2, bookSource2, str2, variable, z2, listSplitSourceRule$default8, listSplitSourceRule$default7, listSplitSourceRule$default6, listSplitSourceRule$default3, listSplitSourceRule$default5, listSplitSourceRule$default, listSplitSourceRule$default4, listSplitSourceRule$default2, debugLog, anonymousClass1);
                    if (searchItem == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    searchBook2 = (SearchBook) searchItem;
                    if (searchBook2 != null) {
                        if (Intrinsics.areEqual(baseUrl, searchBook2.getBookUrl())) {
                            searchBook2.setInfoHtml(body);
                            Boxing.boxBoolean(bookList.add(searchBook2));
                        } else {
                            Boxing.boxBoolean(bookList.add(searchBook2));
                        }
                    }
                    if (it.hasNext()) {
                        if (i2 != 0) {
                            CollectionsKt.reverse(bookList);
                        }
                        return bookList;
                    }
                }
                break;
            case 1:
                bookList = (ArrayList) anonymousClass1.L$1;
                body = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                infoItem2 = $result;
                searchBook = (SearchBook) infoItem2;
                if (searchBook == null) {
                }
                return bookList;
            case 2:
                bookList = (ArrayList) anonymousClass1.L$1;
                body = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                infoItem = $result;
                searchBook3 = (SearchBook) infoItem;
                if (searchBook3 == null) {
                }
                return bookList;
            case 3:
                i = anonymousClass1.I$1;
                i2 = anonymousClass1.I$0;
                it = (Iterator) anonymousClass1.L$16;
                listSplitSourceRule$default = (List) anonymousClass1.L$15;
                listSplitSourceRule$default2 = (List) anonymousClass1.L$14;
                listSplitSourceRule$default3 = (List) anonymousClass1.L$13;
                listSplitSourceRule$default4 = (List) anonymousClass1.L$12;
                listSplitSourceRule$default5 = (List) anonymousClass1.L$11;
                listSplitSourceRule$default6 = (List) anonymousClass1.L$10;
                listSplitSourceRule$default7 = (List) anonymousClass1.L$9;
                listSplitSourceRule$default8 = (List) anonymousClass1.L$8;
                analyzeRule = (AnalyzeRule) anonymousClass1.L$7;
                bookList = (ArrayList) anonymousClass1.L$6;
                debugLog = (DebugLog) anonymousClass1.L$5;
                variableBook = (SearchBook) anonymousClass1.L$4;
                baseUrl = (String) anonymousClass1.L$3;
                bookSource = (BookSource) anonymousClass1.L$2;
                body = (String) anonymousClass1.L$1;
                this = (BookList) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                searchItem = $result;
                searchBook2 = (SearchBook) searchItem;
                if (searchBook2 != null) {
                }
                if (it.hasNext()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object analyzeBookList$default(BookList bookList, String str, BookSource bookSource, AnalyzeUrl analyzeUrl, String str2, SearchBook searchBook, boolean z, DebugLog debugLog, Continuation continuation, int i, Object obj) throws Exception {
        if ((i & 32) != 0) {
            z = true;
        }
        if ((i & 64) != 0) {
            debugLog = null;
        }
        return bookList.analyzeBookList(str, bookSource, analyzeUrl, str2, searchBook, z, debugLog, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getInfoItem(String body, AnalyzeRule analyzeRule, BookSource bookSource, AnalyzeUrl analyzeUrl, String baseUrl, String variable, DebugLog debugLog, Continuation<? super SearchBook> $completion) {
        C01661 c01661;
        Book book;
        if ($completion instanceof C01661) {
            c01661 = (C01661) $completion;
            if ((c01661.label & Integer.MIN_VALUE) != 0) {
                c01661.label -= Integer.MIN_VALUE;
            } else {
                c01661 = new C01661($completion);
            }
        }
        Object $result = c01661.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01661.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, variable, null, false, null, -536870913, 1, null);
                book.setBookUrl(analyzeUrl.getRuleUrl());
                book.setOrigin(bookSource.getBookSourceUrl());
                book.setOriginName(bookSource.getBookSourceName());
                book.setOriginOrder(bookSource.getCustomOrder());
                book.setType(bookSource.getBookSourceType());
                book.setUserNameSpace(analyzeRule.getUserNameSpace());
                analyzeRule.setRuleData(book);
                c01661.L$0 = book;
                c01661.label = 1;
                if (BookInfo.INSTANCE.analyzeBookInfo(book, body, analyzeRule, bookSource, baseUrl, baseUrl, false, debugLog, c01661) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                book = (Book) c01661.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!StringsKt.isBlank(book.getName())) {
            return book.toSearchBook();
        }
        return null;
    }

    static /* synthetic */ Object getInfoItem$default(BookList bookList, String str, AnalyzeRule analyzeRule, BookSource bookSource, AnalyzeUrl analyzeUrl, String str2, String str3, DebugLog debugLog, Continuation continuation, int i, Object obj) {
        if ((i & 64) != 0) {
            debugLog = null;
        }
        return bookList.getInfoItem(str, analyzeRule, bookSource, analyzeUrl, str2, str3, debugLog, continuation);
    }

    static /* synthetic */ Object getSearchItem$default(BookList bookList, Object obj, AnalyzeRule analyzeRule, BookSource bookSource, String str, String str2, boolean z, List list, List list2, List list3, List list4, List list5, List list6, List list7, List list8, DebugLog debugLog, Continuation continuation, int i, Object obj2) {
        if ((i & 16384) != 0) {
            debugLog = null;
        }
        return bookList.getSearchItem(obj, analyzeRule, bookSource, str, str2, z, list, list2, list3, list4, list5, list6, list7, list8, debugLog, continuation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object getSearchItem(Object item, AnalyzeRule analyzeRule, BookSource bookSource, String baseUrl, String variable, boolean log, List<AnalyzeRule.SourceRule> ruleName, List<AnalyzeRule.SourceRule> ruleBookUrl, List<AnalyzeRule.SourceRule> ruleAuthor, List<AnalyzeRule.SourceRule> ruleKind, List<AnalyzeRule.SourceRule> ruleCoverUrl, List<AnalyzeRule.SourceRule> ruleWordCount, List<AnalyzeRule.SourceRule> ruleIntro, List<AnalyzeRule.SourceRule> ruleLastChapter, DebugLog debugLog, Continuation<? super SearchBook> $completion) {
        SearchBook searchBook = new SearchBook(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, variable, 0, 24575, null);
        searchBook.setOrigin(bookSource.getBookSourceUrl());
        searchBook.setOriginName(bookSource.getBookSourceName());
        searchBook.setType(bookSource.getBookSourceType());
        searchBook.setOriginOrder(bookSource.getCustomOrder());
        searchBook.setUserNameSpace(analyzeRule.getUserNameSpace());
        analyzeRule.setRuleData(searchBook);
        AnalyzeRule.setContent$default(analyzeRule, item, null, 2, null);
        JobKt.ensureActive($completion.getContext());
        if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取书名", false, 4, null);
        }
        searchBook.setName(BookHelp.INSTANCE.formatBookName(AnalyzeRule.getString$default(analyzeRule, (List) ruleName, (Object) null, false, 6, (Object) null)));
        if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getName()), false, 4, null);
        }
        if (searchBook.getName().length() > 0) {
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取作者", false, 4, null);
            }
            searchBook.setAuthor(BookHelp.INSTANCE.formatBookAuthor(AnalyzeRule.getString$default(analyzeRule, (List) ruleAuthor, (Object) null, false, 6, (Object) null)));
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getAuthor()), false, 4, null);
            }
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取分类", false, 4, null);
            }
            try {
                List stringList$default = AnalyzeRule.getStringList$default(analyzeRule, (List) ruleKind, (Object) null, false, 6, (Object) null);
                searchBook.setKind(stringList$default == null ? null : CollectionsKt.joinToString$default(stringList$default, ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getKind()), false, 4, null);
                }
            } catch (Exception e) {
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e.getLocalizedMessage()), false, 4, null);
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取字数", false, 4, null);
            }
            try {
                searchBook.setWordCount(StringUtils.INSTANCE.wordCountFormat(AnalyzeRule.getString$default(analyzeRule, (List) ruleWordCount, (Object) null, false, 6, (Object) null)));
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getWordCount()), false, 4, null);
                }
            } catch (Exception e2) {
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e2.getLocalizedMessage()), false, 4, null);
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取最新章节", false, 4, null);
            }
            try {
                searchBook.setLatestChapterTitle(AnalyzeRule.getString$default(analyzeRule, (List) ruleLastChapter, (Object) null, false, 6, (Object) null));
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getLatestChapterTitle()), false, 4, null);
                }
            } catch (Exception e3) {
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e3.getLocalizedMessage()), false, 4, null);
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取简介", false, 4, null);
            }
            try {
                searchBook.setIntro(StringExtensionsKt.htmlFormat(AnalyzeRule.getString$default(analyzeRule, (List) ruleIntro, (Object) null, false, 6, (Object) null)));
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getIntro()), false, 4, null);
                }
            } catch (Exception e4) {
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e4.getLocalizedMessage()), false, 4, null);
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取封面链接", false, 4, null);
            }
            try {
                String it = AnalyzeRule.getString$default(analyzeRule, (List) ruleCoverUrl, (Object) null, false, 6, (Object) null);
                if (it.length() > 0) {
                    searchBook.setCoverUrl(NetworkUtils.INSTANCE.getAbsoluteURL(baseUrl, it));
                }
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getCoverUrl()), false, 4, null);
                }
            } catch (Exception e5) {
                if (log && debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", e5.getLocalizedMessage()), false, 4, null);
                }
            }
            JobKt.ensureActive($completion.getContext());
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取详情页链接", false, 4, null);
            }
            searchBook.setBookUrl(AnalyzeRule.getString$default(analyzeRule, (List) ruleBookUrl, (Object) null, true, 2, (Object) null));
            if (searchBook.getBookUrl().length() == 0) {
                searchBook.setBookUrl(baseUrl);
            }
            if (log && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getBookUrl()), false, 4, null);
            }
            return searchBook;
        }
        return null;
    }
}
