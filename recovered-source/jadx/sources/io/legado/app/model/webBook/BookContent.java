package io.legado.app.model.webBook;

import io.legado.app.data.entities.BaseSource;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.rule.ContentRule;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.utils.HtmlFormatter;
import io.legado.app.utils.NetworkUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.JobKt;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: BookContent.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/webBook/BookContent.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002Jr\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00060\u00042\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0002J[\u0010\u0003\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00052\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"Lio/legado/app/model/webBook/BookContent;", PackageDocumentBase.PREFIX_OPF, "()V", "analyzeContent", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", "baseUrl", "redirectUrl", NCXDocumentV3.XHTMLTgs.body, "contentRule", "Lio/legado/app/data/entities/rule/ContentRule;", NCXDocumentV2.NCXAttributeValues.chapter, "Lio/legado/app/data/entities/BookChapter;", "bookSource", "Lio/legado/app/data/entities/BookSource;", "nextChapterUrl", "printLog", PackageDocumentBase.PREFIX_OPF, "debugLog", "Lio/legado/app/model/DebugLog;", "bookChapter", "(Ljava/lang/String;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Lio/legado/app/data/entities/BookSource;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class BookContent {

    @NotNull
    public static final BookContent INSTANCE = new BookContent();

    /* JADX INFO: renamed from: io.legado.app.model.webBook.BookContent$analyzeContent$1, reason: invalid class name */
    /* JADX INFO: compiled from: BookContent.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/webBook/BookContent$analyzeContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookContent.kt", l = {69, 84}, i = {}, s = {}, n = {}, m = "analyzeContent", c = "io.legado.app.model.webBook.BookContent")
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
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookContent.this.analyzeContent(null, null, null, null, null, null, null, null, (Continuation) this);
        }
    }

    private BookContent() {
    }

    /* JADX WARN: Path cross not found for [B:39:0x01d3, B:41:0x01dd], limit reached: 111 */
    /* JADX WARN: Removed duplicated region for block: B:101:0x05d2  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0670  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x03f3  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0532  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x053c  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0544  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x055e  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x057a  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x059d  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:53:0x0346 -> B:30:0x0195). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object analyzeContent(@Nullable String body, @NotNull Book book, @NotNull BookChapter bookChapter, @NotNull BookSource bookSource, @NotNull String baseUrl, @NotNull String redirectUrl, @Nullable String nextChapterUrl, @Nullable DebugLog debugLog, @NotNull Continuation<? super String> $completion) {
        AnonymousClass1 anonymousClass1;
        AnalyzeRule analyzeRule;
        ContentRule contentRule;
        StringBuilder content;
        Ref.ObjectRef nextUrl;
        Ref.ObjectRef contentData;
        ArrayList nextUrlList;
        String mNextChapterUrl;
        Object strResponseAwait$default;
        String contentStr;
        String str;
        DebugLog debugLog2;
        DebugLog debugLog3;
        DebugLog debugLog4;
        String nextBody;
        DebugLog debugLog5;
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
                if (body == null) {
                    throw new Exception("error_get_web_content");
                }
                if (debugLog != null) {
                    DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", baseUrl), false, 4, null);
                }
                String str2 = nextChapterUrl;
                mNextChapterUrl = !(str2 == null || str2.length() == 0) ? nextChapterUrl : (String) null;
                content = new StringBuilder();
                nextUrlList = CollectionsKt.arrayListOf(new String[]{redirectUrl});
                contentRule = bookSource.getContentRule();
                analyzeRule = new AnalyzeRule(book, bookSource, debugLog).setContent(body, baseUrl);
                analyzeRule.setRedirectUrl(redirectUrl);
                analyzeRule.setChapter(bookChapter);
                analyzeRule.setNextChapterUrl(mNextChapterUrl);
                JobKt.ensureActive(anonymousClass1.getContext());
                contentData = new Ref.ObjectRef();
                contentData.element = analyzeContent$default(this, book, baseUrl, redirectUrl, body, contentRule, bookChapter, bookSource, mNextChapterUrl, false, debugLog, 256, null);
                content.append((String) ((Pair) contentData.element).getFirst());
                if (((List) ((Pair) contentData.element).getSecond()).size() != 1) {
                    if (((List) ((Pair) contentData.element).getSecond()).size() > 1) {
                        JobKt.ensureActive(anonymousClass1.getContext());
                        if (debugLog != null) {
                            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇并发解析正文,总页数:", Boxing.boxInt(((List) ((Pair) contentData.element).getSecond()).size())), false, 4, null);
                        }
                        anonymousClass1.L$0 = bookChapter;
                        anonymousClass1.L$1 = bookSource;
                        anonymousClass1.L$2 = debugLog;
                        anonymousClass1.L$3 = content;
                        anonymousClass1.L$4 = contentRule;
                        anonymousClass1.L$5 = analyzeRule;
                        anonymousClass1.label = 2;
                        if (BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass3(contentData, bookSource, book, debugLog, contentRule, bookChapter, mNextChapterUrl, content, null), anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    String string = content.toString();
                    Intrinsics.checkNotNullExpressionValue(string, "content.toString()");
                    contentStr = string;
                    String replaceRegex = contentRule.getReplaceRegex();
                    str = replaceRegex;
                    if (!(str != null || str.length() == 0)) {
                    }
                    debugLog2 = debugLog;
                    if (debugLog2 != null) {
                    }
                    debugLog3 = debugLog;
                    if (debugLog3 != null) {
                    }
                    debugLog4 = debugLog;
                    if (debugLog4 != null) {
                    }
                    if (contentStr.length() <= 300) {
                    }
                    return contentStr;
                }
                nextUrl = new Ref.ObjectRef();
                nextUrl.element = ((List) ((Pair) contentData.element).getSecond()).get(0);
                if ((((CharSequence) nextUrl.element).length() <= 0) && !nextUrlList.contains(nextUrl.element)) {
                    String str3 = mNextChapterUrl;
                    if (!(str3 != null || str3.length() == 0) || !Intrinsics.areEqual(NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, (String) nextUrl.element), NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, mNextChapterUrl))) {
                        nextUrlList.add(nextUrl.element);
                        JobKt.ensureActive(anonymousClass1.getContext());
                        anonymousClass1.L$0 = book;
                        anonymousClass1.L$1 = bookChapter;
                        anonymousClass1.L$2 = bookSource;
                        anonymousClass1.L$3 = redirectUrl;
                        anonymousClass1.L$4 = debugLog;
                        anonymousClass1.L$5 = mNextChapterUrl;
                        anonymousClass1.L$6 = content;
                        anonymousClass1.L$7 = nextUrlList;
                        anonymousClass1.L$8 = contentRule;
                        anonymousClass1.L$9 = analyzeRule;
                        anonymousClass1.L$10 = contentData;
                        anonymousClass1.L$11 = nextUrl;
                        anonymousClass1.label = 1;
                        strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(new AnalyzeUrl((String) nextUrl.element, null, null, null, null, null, bookSource, book, null, BaseSource.DefaultImpls.getHeaderMap$default(bookSource, false, 1, null), debugLog, 318, null), null, null, false, anonymousClass1, 7, null);
                        if (strResponseAwait$default == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        StrResponse res = (StrResponse) strResponseAwait$default;
                        nextBody = res.getBody();
                        if (nextBody != null) {
                            contentData.element = INSTANCE.analyzeContent(book, (String) nextUrl.element, res.getUrl(), nextBody, contentRule, bookChapter, bookSource, mNextChapterUrl, false, debugLog);
                            Ref.ObjectRef objectRef = nextUrl;
                            if (!((Collection) ((Pair) contentData.element).getSecond()).isEmpty()) {
                                objectRef.element = (String) ((List) ((Pair) contentData.element).getSecond()).get(0);
                                content.append("\n").append((String) ((Pair) contentData.element).getFirst());
                            } else {
                                objectRef.element = PackageDocumentBase.PREFIX_OPF;
                                content.append("\n").append((String) ((Pair) contentData.element).getFirst());
                            }
                        }
                        if (((CharSequence) nextUrl.element).length() <= 0) {
                            String str32 = mNextChapterUrl;
                            if (!(str32 != null || str32.length() == 0)) {
                            }
                            nextUrlList.add(nextUrl.element);
                            JobKt.ensureActive(anonymousClass1.getContext());
                            anonymousClass1.L$0 = book;
                            anonymousClass1.L$1 = bookChapter;
                            anonymousClass1.L$2 = bookSource;
                            anonymousClass1.L$3 = redirectUrl;
                            anonymousClass1.L$4 = debugLog;
                            anonymousClass1.L$5 = mNextChapterUrl;
                            anonymousClass1.L$6 = content;
                            anonymousClass1.L$7 = nextUrlList;
                            anonymousClass1.L$8 = contentRule;
                            anonymousClass1.L$9 = analyzeRule;
                            anonymousClass1.L$10 = contentData;
                            anonymousClass1.L$11 = nextUrl;
                            anonymousClass1.label = 1;
                            strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(new AnalyzeUrl((String) nextUrl.element, null, null, null, null, null, bookSource, book, null, BaseSource.DefaultImpls.getHeaderMap$default(bookSource, false, 1, null), debugLog, 318, null), null, null, false, anonymousClass1, 7, null);
                            if (strResponseAwait$default == coroutine_suspended) {
                            }
                            StrResponse res2 = (StrResponse) strResponseAwait$default;
                            nextBody = res2.getBody();
                            if (nextBody != null) {
                            }
                            if (((CharSequence) nextUrl.element).length() <= 0) {
                            }
                        }
                    }
                }
                debugLog5 = debugLog;
                if (debugLog5 != null) {
                    DebugLog.DefaultImpls.log$default(debugLog5, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇本章总页数:", Boxing.boxInt(nextUrlList.size())), false, 4, null);
                }
                String string2 = content.toString();
                Intrinsics.checkNotNullExpressionValue(string2, "content.toString()");
                contentStr = string2;
                String replaceRegex2 = contentRule.getReplaceRegex();
                str = replaceRegex2;
                if (!(str != null || str.length() == 0)) {
                    contentStr = AnalyzeRule.getString$default(analyzeRule, replaceRegex2, (Object) contentStr, false, 4, (Object) null);
                }
                debugLog2 = debugLog;
                if (debugLog2 != null) {
                    DebugLog.DefaultImpls.log$default(debugLog2, bookSource.getBookSourceUrl(), "┌获取章节名称", false, 4, null);
                }
                debugLog3 = debugLog;
                if (debugLog3 != null) {
                    DebugLog.DefaultImpls.log$default(debugLog3, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", bookChapter.getTitle()), false, 4, null);
                }
                debugLog4 = debugLog;
                if (debugLog4 != null) {
                    DebugLog.DefaultImpls.log$default(debugLog4, bookSource.getBookSourceUrl(), "┌获取正文内容 (长度：" + contentStr.length() + ')', false, 4, null);
                }
                if (contentStr.length() <= 300) {
                    DebugLog debugLog6 = debugLog;
                    if (debugLog6 != null) {
                        String bookSourceUrl = bookSource.getBookSourceUrl();
                        StringBuilder sbAppend = new StringBuilder().append("└\n");
                        String str4 = contentStr;
                        if (str4 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring = str4.substring(0, 150);
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        StringBuilder sbAppend2 = sbAppend.append(strSubstring).append(" ... ");
                        String str5 = contentStr;
                        int length = contentStr.length() - 150;
                        int length2 = contentStr.length();
                        if (str5 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring2 = str5.substring(length, length2);
                        Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        DebugLog.DefaultImpls.log$default(debugLog6, bookSourceUrl, sbAppend2.append(strSubstring2).toString(), false, 4, null);
                    }
                } else {
                    DebugLog debugLog7 = debugLog;
                    if (debugLog7 != null) {
                        DebugLog.DefaultImpls.log$default(debugLog7, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└\n", contentStr), false, 4, null);
                    }
                }
                return contentStr;
            case 1:
                nextUrl = (Ref.ObjectRef) anonymousClass1.L$11;
                contentData = (Ref.ObjectRef) anonymousClass1.L$10;
                analyzeRule = (AnalyzeRule) anonymousClass1.L$9;
                contentRule = (ContentRule) anonymousClass1.L$8;
                nextUrlList = (ArrayList) anonymousClass1.L$7;
                content = (StringBuilder) anonymousClass1.L$6;
                mNextChapterUrl = (String) anonymousClass1.L$5;
                debugLog = (DebugLog) anonymousClass1.L$4;
                redirectUrl = (String) anonymousClass1.L$3;
                bookSource = (BookSource) anonymousClass1.L$2;
                bookChapter = (BookChapter) anonymousClass1.L$1;
                book = (Book) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                StrResponse res22 = (StrResponse) strResponseAwait$default;
                nextBody = res22.getBody();
                if (nextBody != null) {
                }
                if (((CharSequence) nextUrl.element).length() <= 0) {
                }
                debugLog5 = debugLog;
                if (debugLog5 != null) {
                }
                String string22 = content.toString();
                Intrinsics.checkNotNullExpressionValue(string22, "content.toString()");
                contentStr = string22;
                String replaceRegex22 = contentRule.getReplaceRegex();
                str = replaceRegex22;
                if (!(str != null || str.length() == 0)) {
                }
                debugLog2 = debugLog;
                if (debugLog2 != null) {
                }
                debugLog3 = debugLog;
                if (debugLog3 != null) {
                }
                debugLog4 = debugLog;
                if (debugLog4 != null) {
                }
                if (contentStr.length() <= 300) {
                }
                return contentStr;
            case 2:
                analyzeRule = (AnalyzeRule) anonymousClass1.L$5;
                contentRule = (ContentRule) anonymousClass1.L$4;
                content = (StringBuilder) anonymousClass1.L$3;
                debugLog = (DebugLog) anonymousClass1.L$2;
                bookSource = (BookSource) anonymousClass1.L$1;
                bookChapter = (BookChapter) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                String string222 = content.toString();
                Intrinsics.checkNotNullExpressionValue(string222, "content.toString()");
                contentStr = string222;
                String replaceRegex222 = contentRule.getReplaceRegex();
                str = replaceRegex222;
                if (!(str != null || str.length() == 0)) {
                }
                debugLog2 = debugLog;
                if (debugLog2 != null) {
                }
                debugLog3 = debugLog;
                if (debugLog3 != null) {
                }
                debugLog4 = debugLog;
                if (debugLog4 != null) {
                }
                if (contentStr.length() <= 300) {
                }
                return contentStr;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object analyzeContent$default(BookContent bookContent, String str, Book book, BookChapter bookChapter, BookSource bookSource, String str2, String str3, String str4, DebugLog debugLog, Continuation continuation, int i, Object obj) {
        if ((i & 64) != 0) {
            str4 = null;
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            debugLog = null;
        }
        return bookContent.analyzeContent(str, book, bookChapter, bookSource, str2, str3, str4, debugLog, continuation);
    }

    /* JADX INFO: renamed from: io.legado.app.model.webBook.BookContent$analyzeContent$3, reason: invalid class name */
    /* JADX INFO: compiled from: BookContent.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/webBook/BookContent$analyzeContent$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BookContent.kt", l = {104}, i = {0}, s = {"L$0"}, n = {"$this$withContext"}, m = "invokeSuspend", c = "io.legado.app.model.webBook.BookContent$analyzeContent$3")
    static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$1;
        Object L$2;
        Object L$3;
        int I$0;
        int I$1;
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ Ref.ObjectRef<Pair<String, List<String>>> $contentData;
        final /* synthetic */ BookSource $bookSource;
        final /* synthetic */ Book $book;
        final /* synthetic */ DebugLog $debugLog;
        final /* synthetic */ ContentRule $contentRule;
        final /* synthetic */ BookChapter $bookChapter;
        final /* synthetic */ String $mNextChapterUrl;
        final /* synthetic */ StringBuilder $content;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Ref.ObjectRef<Pair<String, List<String>>> $contentData, BookSource $bookSource, Book $book, DebugLog $debugLog, ContentRule $contentRule, BookChapter $bookChapter, String $mNextChapterUrl, StringBuilder $content, Continuation<? super AnonymousClass3> $completion) {
            super(2, $completion);
            this.$contentData = $contentData;
            this.$bookSource = $bookSource;
            this.$book = $book;
            this.$debugLog = $debugLog;
            this.$contentRule = $contentRule;
            this.$bookChapter = $bookChapter;
            this.$mNextChapterUrl = $mNextChapterUrl;
            this.$content = $content;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass3 = new AnonymousClass3(this.$contentData, this.$bookSource, this.$book, this.$debugLog, this.$contentRule, this.$bookChapter, this.$mNextChapterUrl, this.$content, $completion);
            anonymousClass3.L$0 = value;
            return anonymousClass3;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            int length;
            Deferred[] deferredArr;
            StringBuilder sb;
            CoroutineScope $this$withContext;
            int i;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$withContext = (CoroutineScope) this.L$0;
                    int size = ((List) ((Pair) this.$contentData.element).getSecond()).size();
                    Deferred[] asyncArray = new Deferred[size];
                    for (int i2 = 0; i2 < size; i2++) {
                        int i3 = i2;
                        asyncArray[i3] = BuildersKt.async$default($this$withContext, Dispatchers.getIO(), (CoroutineStart) null, new BookContent$analyzeContent$3$asyncArray$1$1(this.$contentData, i3, this.$bookSource, this.$book, this.$debugLog, this.$contentRule, this.$bookChapter, this.$mNextChapterUrl, null), 2, (Object) null);
                    }
                    sb = this.$content;
                    deferredArr = asyncArray;
                    length = deferredArr.length;
                    i = 0;
                    break;
                case 1:
                    int i4 = this.I$1;
                    length = this.I$0;
                    StringBuilder sb2 = (StringBuilder) this.L$3;
                    deferredArr = (Deferred[]) this.L$2;
                    sb = (StringBuilder) this.L$1;
                    $this$withContext = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    sb2.append((String) $result);
                    i = i4 + 1;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            while (i < length) {
                Deferred deferred = deferredArr[i];
                JobKt.ensureActive($this$withContext.getCoroutineContext());
                StringBuilder sbAppend = sb.append("\n");
                this.L$0 = $this$withContext;
                this.L$1 = sb;
                this.L$2 = deferredArr;
                this.L$3 = sbAppend;
                this.I$0 = length;
                this.I$1 = i;
                this.label = 1;
                Object objAwait = deferred.await(this);
                if (objAwait == coroutine_suspended) {
                    return coroutine_suspended;
                }
                sbAppend.append((String) objAwait);
                i++;
            }
            return Unit.INSTANCE;
        }
    }

    static /* synthetic */ Pair analyzeContent$default(BookContent bookContent, Book book, String str, String str2, String str3, ContentRule contentRule, BookChapter bookChapter, BookSource bookSource, String str4, boolean z, DebugLog debugLog, int i, Object obj) throws Exception {
        if ((i & 256) != 0) {
            z = true;
        }
        if ((i & 512) != 0) {
            debugLog = null;
        }
        return bookContent.analyzeContent(book, str, str2, str3, contentRule, bookChapter, bookSource, str4, z, debugLog);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Pair<String, List<String>> analyzeContent(Book book, String baseUrl, String redirectUrl, String body, ContentRule contentRule, BookChapter chapter, BookSource bookSource, String nextChapterUrl, boolean printLog, DebugLog debugLog) throws Exception {
        AnalyzeRule analyzeRule = new AnalyzeRule(book, bookSource, debugLog);
        analyzeRule.setContent(body, baseUrl);
        analyzeRule.setChapter(chapter);
        URL rUrl = analyzeRule.setRedirectUrl(redirectUrl);
        analyzeRule.setNextChapterUrl(nextChapterUrl);
        ArrayList nextUrlList = new ArrayList();
        analyzeRule.setChapter(chapter);
        String content = AnalyzeRule.getString$default(analyzeRule, contentRule.getContent(), (Object) null, false, 6, (Object) null);
        String content2 = HtmlFormatter.INSTANCE.formatKeepImg(content, rUrl);
        String nextUrlRule = contentRule.getNextContentUrl();
        String str = nextUrlRule;
        if (!(str == null || str.length() == 0)) {
            if (printLog && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取正文下一页链接", false, 4, null);
            }
            List it = AnalyzeRule.getStringList$default(analyzeRule, nextUrlRule, (Object) null, true, 2, (Object) null);
            if (it != null) {
                nextUrlList.addAll(it);
            }
            if (printLog && debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", CollectionsKt.joinToString$default(nextUrlList, "，", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null)), false, 4, null);
            }
        }
        return new Pair<>(content2, nextUrlList);
    }
}
