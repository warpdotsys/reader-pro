package io.legado.app.model.webBook;

import io.legado.app.data.entities.BaseSource;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BookChapterList.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/webBook/BookChapterList$analyzeChapterList$3$asyncArray$1$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/BookChapter;", "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "BookChapterList.kt", l = {91, 92}, i = {0}, s = {"L$0"}, n = {"urlStr"}, m = "invokeSuspend", c = "io.legado.app.model.webBook.BookChapterList$analyzeChapterList$3$asyncArray$1$1")
final class BookChapterList$analyzeChapterList$3$asyncArray$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends BookChapter>>, Object> {
    Object L$0;
    int label;
    final /* synthetic */ Ref.ObjectRef<Pair<List<BookChapter>, List<String>>> $chapterData;
    final /* synthetic */ int $tmp;
    final /* synthetic */ BookSource $bookSource;
    final /* synthetic */ Book $book;
    final /* synthetic */ DebugLog $debugLog;
    final /* synthetic */ TocRule $tocRule;
    final /* synthetic */ Ref.ObjectRef<String> $listRule;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookChapterList$analyzeChapterList$3$asyncArray$1$1(Ref.ObjectRef<Pair<List<BookChapter>, List<String>>> $chapterData, int $tmp, BookSource $bookSource, Book $book, DebugLog $debugLog, TocRule $tocRule, Ref.ObjectRef<String> $listRule, Continuation<? super BookChapterList$analyzeChapterList$3$asyncArray$1$1> $completion) {
        super(2, $completion);
        this.$chapterData = $chapterData;
        this.$tmp = $tmp;
        this.$bookSource = $bookSource;
        this.$book = $book;
        this.$debugLog = $debugLog;
        this.$tocRule = $tocRule;
        this.$listRule = $listRule;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new BookChapterList$analyzeChapterList$3$asyncArray$1$1(this.$chapterData, this.$tmp, this.$bookSource, this.$book, this.$debugLog, this.$tocRule, this.$listRule, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super List<BookChapter>> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x00f2  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(@NotNull Object $result) {
        Object objAnalyzeChapterList;
        String urlStr;
        Object strResponseAwait$default;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                urlStr = (String) ((List) ((Pair) this.$chapterData.element).getSecond()).get(this.$tmp);
                AnalyzeUrl analyzeUrl = new AnalyzeUrl(urlStr, null, null, null, null, null, this.$bookSource, this.$book, null, BaseSource.DefaultImpls.getHeaderMap$default(this.$bookSource, false, 1, null), this.$debugLog, 318, null);
                this.L$0 = urlStr;
                this.label = 1;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, (Continuation) this, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                StrResponse res = (StrResponse) strResponseAwait$default;
                String url = res.getUrl();
                String body = res.getBody();
                Intrinsics.checkNotNull(body);
                this.L$0 = null;
                this.label = 2;
                objAnalyzeChapterList = BookChapterList.INSTANCE.analyzeChapterList(this.$book, urlStr, url, body, this.$tocRule, (String) this.$listRule.element, this.$bookSource, false, false, this.$debugLog, (Continuation) this);
                if (objAnalyzeChapterList == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return ((Pair) objAnalyzeChapterList).getFirst();
            case 1:
                urlStr = (String) this.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                StrResponse res2 = (StrResponse) strResponseAwait$default;
                String url2 = res2.getUrl();
                String body2 = res2.getBody();
                Intrinsics.checkNotNull(body2);
                this.L$0 = null;
                this.label = 2;
                objAnalyzeChapterList = BookChapterList.INSTANCE.analyzeChapterList(this.$book, urlStr, url2, body2, this.$tocRule, (String) this.$listRule.element, this.$bookSource, false, false, this.$debugLog, (Continuation) this);
                if (objAnalyzeChapterList == coroutine_suspended) {
                }
                return ((Pair) objAnalyzeChapterList).getFirst();
            case 2:
                ResultKt.throwOnFailure($result);
                objAnalyzeChapterList = $result;
                return ((Pair) objAnalyzeChapterList).getFirst();
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
