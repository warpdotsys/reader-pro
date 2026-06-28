package io.legado.app.model.rss;

import io.legado.app.data.entities.BaseSource;
import io.legado.app.data.entities.RssArticle;
import io.legado.app.data.entities.RssSource;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.model.analyzeRule.RuleData;
import io.legado.app.utils.NetworkUtils;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Rss.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/rss/Rss.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JO\u0010\u0003\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00042\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J3\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"Lio/legado/app/model/rss/Rss;", PackageDocumentBase.PREFIX_OPF, "()V", "getArticles", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/RssArticle;", PackageDocumentBase.PREFIX_OPF, "sortName", "sortUrl", "rssSource", "Lio/legado/app/data/entities/RssSource;", "page", PackageDocumentBase.PREFIX_OPF, "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/RssSource;ILio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContent", "rssArticle", "ruleContent", "(Lio/legado/app/data/entities/RssArticle;Ljava/lang/String;Lio/legado/app/data/entities/RssSource;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class Rss {

    @NotNull
    public static final Rss INSTANCE = new Rss();

    /* JADX INFO: renamed from: io.legado.app.model.rss.Rss$getArticles$1, reason: invalid class name */
    /* JADX INFO: compiled from: Rss.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/rss/Rss$getArticles$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Rss.kt", l = {32}, i = {0, 0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3", "L$4"}, n = {"sortName", "sortUrl", "rssSource", "debugLog", "ruleData"}, m = "getArticles", c = "io.legado.app.model.rss.Rss")
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
            return Rss.this.getArticles(null, null, null, 0, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.model.rss.Rss$getContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Rss.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/rss/Rss$getContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "Rss.kt", l = {52}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3"}, n = {"rssArticle", "ruleContent", "rssSource", "debugLog"}, m = "getContent", c = "io.legado.app.model.rss.Rss")
    static final class C01651 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C01651(Continuation<? super C01651> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return Rss.this.getContent(null, null, null, null, (Continuation) this);
        }
    }

    private Rss() {
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getArticles(@NotNull String sortName, @NotNull String sortUrl, @NotNull RssSource rssSource, int page, @Nullable DebugLog debugLog, @NotNull Continuation<? super Pair<? extends List<RssArticle>, String>> $completion) {
        AnonymousClass1 anonymousClass1;
        RuleData ruleData;
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
                ruleData = new RuleData();
                AnalyzeUrl analyzeUrl = new AnalyzeUrl(sortUrl, null, Boxing.boxInt(page), null, null, null, rssSource, ruleData, null, BaseSource.DefaultImpls.getHeaderMap$default(rssSource, false, 1, null), debugLog, 314, null);
                anonymousClass1.L$0 = sortName;
                anonymousClass1.L$1 = sortUrl;
                anonymousClass1.L$2 = rssSource;
                anonymousClass1.L$3 = debugLog;
                anonymousClass1.L$4 = ruleData;
                anonymousClass1.label = 1;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, anonymousClass1, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ruleData = (RuleData) anonymousClass1.L$4;
                debugLog = (DebugLog) anonymousClass1.L$3;
                rssSource = (RssSource) anonymousClass1.L$2;
                sortUrl = (String) anonymousClass1.L$1;
                sortName = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        String body = ((StrResponse) strResponseAwait$default).getBody();
        return RssParserByRule.INSTANCE.parseXML(sortName, sortUrl, body, rssSource, ruleData, debugLog);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getContent(@NotNull RssArticle rssArticle, @NotNull String ruleContent, @NotNull RssSource rssSource, @Nullable DebugLog debugLog, @NotNull Continuation<? super String> $completion) {
        C01651 c01651;
        Object strResponseAwait$default;
        if ($completion instanceof C01651) {
            c01651 = (C01651) $completion;
            if ((c01651.label & Integer.MIN_VALUE) != 0) {
                c01651.label -= Integer.MIN_VALUE;
            } else {
                c01651 = new C01651($completion);
            }
        }
        Object $result = c01651.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01651.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                AnalyzeUrl analyzeUrl = new AnalyzeUrl(rssArticle.getLink(), null, null, null, null, rssArticle.getOrigin(), rssSource, rssArticle, null, BaseSource.DefaultImpls.getHeaderMap$default(rssSource, false, 1, null), debugLog, 286, null);
                c01651.L$0 = rssArticle;
                c01651.L$1 = ruleContent;
                c01651.L$2 = rssSource;
                c01651.L$3 = debugLog;
                c01651.label = 1;
                strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, c01651, 7, null);
                if (strResponseAwait$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                debugLog = (DebugLog) c01651.L$3;
                rssSource = (RssSource) c01651.L$2;
                ruleContent = (String) c01651.L$1;
                rssArticle = (RssArticle) c01651.L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        String body = ((StrResponse) strResponseAwait$default).getBody();
        AnalyzeRule analyzeRule = new AnalyzeRule(rssArticle, rssSource, debugLog);
        AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(NetworkUtils.INSTANCE.getAbsoluteURL(rssArticle.getOrigin(), rssArticle.getLink()));
        return AnalyzeRule.getString$default(analyzeRule, ruleContent, (Object) null, false, 6, (Object) null);
    }
}
