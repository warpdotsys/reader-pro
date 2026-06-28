package io.legado.app.model.webBook;

import io.legado.app.data.entities.BaseSource;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.rule.ContentRule;
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

/* JADX INFO: compiled from: BookContent.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/webBook/BookContent$analyzeContent$3$asyncArray$1$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "BookContent.kt", l = {95}, i = {0}, s = {"L$0"}, n = {"urlStr"}, m = "invokeSuspend", c = "io.legado.app.model.webBook.BookContent$analyzeContent$3$asyncArray$1$1")
final class BookContent$analyzeContent$3$asyncArray$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super String>, Object> {
    Object L$0;
    int label;
    final /* synthetic */ Ref.ObjectRef<Pair<String, List<String>>> $contentData;
    final /* synthetic */ int $tmp;
    final /* synthetic */ BookSource $bookSource;
    final /* synthetic */ Book $book;
    final /* synthetic */ DebugLog $debugLog;
    final /* synthetic */ ContentRule $contentRule;
    final /* synthetic */ BookChapter $bookChapter;
    final /* synthetic */ String $mNextChapterUrl;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookContent$analyzeContent$3$asyncArray$1$1(Ref.ObjectRef<Pair<String, List<String>>> $contentData, int $tmp, BookSource $bookSource, Book $book, DebugLog $debugLog, ContentRule $contentRule, BookChapter $bookChapter, String $mNextChapterUrl, Continuation<? super BookContent$analyzeContent$3$asyncArray$1$1> $completion) {
        super(2, $completion);
        this.$contentData = $contentData;
        this.$tmp = $tmp;
        this.$bookSource = $bookSource;
        this.$book = $book;
        this.$debugLog = $debugLog;
        this.$contentRule = $contentRule;
        this.$bookChapter = $bookChapter;
        this.$mNextChapterUrl = $mNextChapterUrl;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new BookContent$analyzeContent$3$asyncArray$1$1(this.$contentData, this.$tmp, this.$bookSource, this.$book, this.$debugLog, this.$contentRule, this.$bookChapter, this.$mNextChapterUrl, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super String> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        String urlStr;
        Object strResponseAwait$default;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                urlStr = (String) ((List) ((Pair) this.$contentData.element).getSecond()).get(this.$tmp);
                AnalyzeUrl analyzeUrl = new AnalyzeUrl(urlStr, null, null, null, null, null, this.$bookSource, this.$book, null, BaseSource.DefaultImpls.getHeaderMap$default(this.$bookSource, false, 1, null), this.$debugLog, 318, null);
                this.L$0 = urlStr;
                this.label = 1;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, (Continuation) this, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                urlStr = (String) this.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        StrResponse res = (StrResponse) strResponseAwait$default;
        String url = res.getUrl();
        String body = res.getBody();
        Intrinsics.checkNotNull(body);
        return BookContent.INSTANCE.analyzeContent(this.$book, urlStr, url, body, this.$contentRule, this.$bookChapter, this.$bookSource, this.$mNextChapterUrl, false, this.$debugLog).getFirst();
    }
}
