// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.rss;

import io.legado.app.utils.NetworkUtils;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.help.http.StrResponse;
import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import java.util.Map;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.data.entities.BaseSource;
import kotlin.coroutines.jvm.internal.Boxing;
import io.legado.app.model.analyzeRule.RuleData;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import io.legado.app.data.entities.RssArticle;
import java.util.List;
import kotlin.Pair;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.Nullable;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.RssSource;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002JO\u0010\u0003\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00042\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J3\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0015" }, d2 = { "Lio/legado/app/model/rss/Rss;", "", "()V", "getArticles", "Lkotlin/Pair;", "", "Lio/legado/app/data/entities/RssArticle;", "", "sortName", "sortUrl", "rssSource", "Lio/legado/app/data/entities/RssSource;", "page", "", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/RssSource;ILio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getContent", "rssArticle", "ruleContent", "(Lio/legado/app/data/entities/RssArticle;Ljava/lang/String;Lio/legado/app/data/entities/RssSource;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class Rss
{
    @NotNull
    public static final Rss INSTANCE;
    
    private Rss() {
    }
    
    @Nullable
    public final Object getArticles(@NotNull String sortName, @NotNull String sortUrl, @NotNull RssSource rssSource, final int page, @Nullable DebugLog debugLog, @NotNull final Continuation<? super Pair<? extends List<RssArticle>, String>> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof Rss$getArticles.Rss$getArticles$1) {
                final Rss$getArticles.Rss$getArticles$1 rss$getArticles$1 = (Rss$getArticles.Rss$getArticles$1)$completion;
                if ((rss$getArticles$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Rss$getArticles.Rss$getArticles$1 rss$getArticles$2 = rss$getArticles$1;
                    rss$getArticles$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new Rss$getArticles.Rss$getArticles$1(this, (Continuation)$completion);
        }
        final Object $result = ((Rss$getArticles.Rss$getArticles$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        RuleData ruleData = null;
        Object strResponseAwait$default = null;
        switch (((Rss$getArticles.Rss$getArticles$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                ruleData = new RuleData();
                final AnalyzeUrl analyzeUrl2;
                final AnalyzeUrl analyzeUrl = analyzeUrl2 = new AnalyzeUrl(sortUrl, null, Boxing.boxInt(page), null, null, null, rssSource, ruleData, null, BaseSource.DefaultImpls.getHeaderMap$default(rssSource, false, 1, null), debugLog, 314, null);
                final String s = null;
                final String s2 = null;
                final boolean b = false;
                final Continuation continuation = $continuation;
                final int n = 7;
                final Object o = null;
                ((Rss$getArticles.Rss$getArticles$1)$continuation).L$0 = sortName;
                ((Rss$getArticles.Rss$getArticles$1)$continuation).L$1 = sortUrl;
                ((Rss$getArticles.Rss$getArticles$1)$continuation).L$2 = rssSource;
                ((Rss$getArticles.Rss$getArticles$1)$continuation).L$3 = debugLog;
                ((Rss$getArticles.Rss$getArticles$1)$continuation).L$4 = ruleData;
                ((Rss$getArticles.Rss$getArticles$1)$continuation).label = 1;
                if ((strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl2, s, s2, b, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                ruleData = (RuleData)((Rss$getArticles.Rss$getArticles$1)$continuation).L$4;
                debugLog = (DebugLog)((Rss$getArticles.Rss$getArticles$1)$continuation).L$3;
                rssSource = (RssSource)((Rss$getArticles.Rss$getArticles$1)$continuation).L$2;
                sortUrl = (String)((Rss$getArticles.Rss$getArticles$1)$continuation).L$1;
                sortName = (String)((Rss$getArticles.Rss$getArticles$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final String body = ((StrResponse)strResponseAwait$default).getBody();
        return RssParserByRule.INSTANCE.parseXML(sortName, sortUrl, body, rssSource, ruleData, debugLog);
    }
    
    @Nullable
    public final Object getContent(@NotNull RssArticle rssArticle, @NotNull String ruleContent, @NotNull RssSource rssSource, @Nullable DebugLog debugLog, @NotNull final Continuation<? super String> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof Rss$getContent.Rss$getContent$1) {
                final Rss$getContent.Rss$getContent$1 rss$getContent$1 = (Rss$getContent.Rss$getContent$1)$completion;
                if ((rss$getContent$1.label & Integer.MIN_VALUE) != 0x0) {
                    final Rss$getContent.Rss$getContent$1 rss$getContent$2 = rss$getContent$1;
                    rss$getContent$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new Rss$getContent.Rss$getContent$1(this, (Continuation)$completion);
        }
        final Object $result = ((Rss$getContent.Rss$getContent$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object strResponseAwait$default = null;
        switch (((Rss$getContent.Rss$getContent$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final AnalyzeUrl analyzeUrl2;
                final AnalyzeUrl analyzeUrl = analyzeUrl2 = new AnalyzeUrl(rssArticle.getLink(), null, null, null, null, rssArticle.getOrigin(), rssSource, rssArticle, null, BaseSource.DefaultImpls.getHeaderMap$default(rssSource, false, 1, null), debugLog, 286, null);
                final String s = null;
                final String s2 = null;
                final boolean b = false;
                final Continuation continuation = $continuation;
                final int n = 7;
                final Object o = null;
                ((Rss$getContent.Rss$getContent$1)$continuation).L$0 = rssArticle;
                ((Rss$getContent.Rss$getContent$1)$continuation).L$1 = ruleContent;
                ((Rss$getContent.Rss$getContent$1)$continuation).L$2 = rssSource;
                ((Rss$getContent.Rss$getContent$1)$continuation).L$3 = debugLog;
                ((Rss$getContent.Rss$getContent$1)$continuation).label = 1;
                if ((strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl2, s, s2, b, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                debugLog = (DebugLog)((Rss$getContent.Rss$getContent$1)$continuation).L$3;
                rssSource = (RssSource)((Rss$getContent.Rss$getContent$1)$continuation).L$2;
                ruleContent = (String)((Rss$getContent.Rss$getContent$1)$continuation).L$1;
                rssArticle = (RssArticle)((Rss$getContent.Rss$getContent$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                strResponseAwait$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final String body = ((StrResponse)strResponseAwait$default).getBody();
        final AnalyzeRule analyzeRule = new AnalyzeRule(rssArticle, rssSource, debugLog);
        AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(NetworkUtils.INSTANCE.getAbsoluteURL(rssArticle.getOrigin(), rssArticle.getLink()));
        return AnalyzeRule.getString$default(analyzeRule, ruleContent, null, false, 6, null);
    }
    
    static {
        INSTANCE = new Rss();
    }
}
