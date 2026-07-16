// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.rss;

import java.util.Collection;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.Iterator;
import kotlin.collections.CollectionsKt;
import io.legado.app.utils.NetworkUtils;
import java.util.Locale;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import java.util.ArrayList;
import io.legado.app.exception.NoStackTraceException;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.data.entities.RssArticle;
import java.util.List;
import kotlin.Pair;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.RuleData;
import io.legado.app.data.entities.RssSource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0098\u0001\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0010\u0010\r\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0011\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0012\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0013\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0002JN\u0010\u0016\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00172\u0006\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u00062\b\u0010\u001b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015¡§\u0006 " }, d2 = { "Lio/legado/app/model/rss/RssParserByRule;", "", "()V", "getItem", "Lio/legado/app/data/entities/RssArticle;", "sourceUrl", "", "item", "analyzeRule", "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "variable", "log", "", "ruleTitle", "", "Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;", "rulePubDate", "ruleDescription", "ruleImage", "ruleLink", "debugLog", "Lio/legado/app/model/DebugLog;", "parseXML", "Lkotlin/Pair;", "", "sortName", "sortUrl", "body", "rssSource", "Lio/legado/app/data/entities/RssSource;", "ruleData", "Lio/legado/app/model/analyzeRule/RuleData;", "reader-pro" })
public final class RssParserByRule
{
    @NotNull
    public static final RssParserByRule INSTANCE;
    
    private RssParserByRule() {
    }
    
    @NotNull
    public final Pair<List<RssArticle>, String> parseXML(@NotNull final String sortName, @NotNull final String sortUrl, @Nullable final String body, @NotNull final RssSource rssSource, @NotNull final RuleData ruleData, @Nullable final DebugLog debugLog) throws Exception {
        Intrinsics.checkNotNullParameter((Object)sortName, "sortName");
        Intrinsics.checkNotNullParameter((Object)sortUrl, "sortUrl");
        Intrinsics.checkNotNullParameter((Object)rssSource, "rssSource");
        Intrinsics.checkNotNullParameter((Object)ruleData, "ruleData");
        final String sourceUrl = rssSource.getSourceUrl();
        String nextUrl = null;
        final CharSequence charSequence = body;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            throw new NoStackTraceException(Intrinsics.stringPlus("error_get_web_content: ", (Object)rssSource.getSourceUrl()));
        }
        String ruleArticles = rssSource.getRuleArticles();
        final CharSequence charSequence2 = ruleArticles;
        if (charSequence2 == null || StringsKt.isBlank(charSequence2)) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u21d2\u5217\u8868\u89c4\u5219\u4e3a\u7a7a, \u4f7f\u7528\u9ed8\u8ba4\u89c4\u5219\u89e3\u6790", false, 4, null);
            }
            return RssParserDefault.INSTANCE.parseXML(sortName, body, sourceUrl, debugLog);
        }
        final List articleList = new ArrayList();
        final AnalyzeRule analyzeRule = new AnalyzeRule(ruleData, rssSource, debugLog);
        AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(sortUrl);
        analyzeRule.setRedirectUrl(sortUrl);
        boolean reverse = false;
        if (StringsKt.startsWith$default(ruleArticles, "-", false, 2, (Object)null)) {
            reverse = true;
            final String s = ruleArticles;
            final int beginIndex = 1;
            final String s2 = s;
            if (s2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring = s2.substring(beginIndex);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            ruleArticles = substring;
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u5217\u8868", false, 4, null);
        }
        final List collections = analyzeRule.getElements(ruleArticles);
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514\u5217\u8868\u5927\u5c0f:", (Object)collections.size()), false, 4, null);
        }
        final CharSequence charSequence3 = rssSource.getRuleNextPage();
        if (charSequence3 != null && charSequence3.length() != 0) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u4e0b\u4e00\u9875\u94fe\u63a5", false, 4, null);
            }
            final String ruleNextPage = rssSource.getRuleNextPage();
            Intrinsics.checkNotNull((Object)ruleNextPage);
            final String s3 = ruleNextPage;
            final Locale default1 = Locale.getDefault();
            Intrinsics.checkNotNullExpressionValue((Object)default1, "getDefault()");
            final Locale locale = default1;
            final String s4 = s3;
            if (s4 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String upperCase = s4.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue((Object)upperCase, "(this as java.lang.String).toUpperCase(locale)");
            if (Intrinsics.areEqual((Object)upperCase, (Object)"PAGE")) {
                nextUrl = sortUrl;
            }
            else {
                nextUrl = AnalyzeRule.getString$default(analyzeRule, rssSource.getRuleNextPage(), null, false, 6, null);
                if (nextUrl.length() > 0) {
                    nextUrl = NetworkUtils.INSTANCE.getAbsoluteURL(sortUrl, nextUrl);
                }
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514", (Object)nextUrl), false, 4, null);
            }
        }
        final List ruleTitle = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleTitle(), false, 2, null);
        final List rulePubDate = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRulePubDate(), false, 2, null);
        final List ruleDescription = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleDescription(), false, 2, null);
        final List ruleImage = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleImage(), false, 2, null);
        final List ruleLink = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleLink(), false, 2, null);
        final String variable = ruleData.getVariable();
        final Iterator iterator = collections.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final int index = n;
            ++n;
            final Object item = iterator.next();
            final RssArticle item2 = this.getItem(sourceUrl, item, analyzeRule, variable, index == 0, ruleTitle, rulePubDate, ruleDescription, ruleImage, ruleLink, debugLog);
            if (item2 == null) {
                continue;
            }
            final RssArticle it = item2;
            final int n2 = 0;
            it.setSort(sortName);
            it.setOrigin(sourceUrl);
            articleList.add(it);
        }
        if (reverse) {
            CollectionsKt.reverse(articleList);
        }
        return (Pair<List<RssArticle>, String>)new Pair((Object)articleList, (Object)nextUrl);
    }
    
    private final RssArticle getItem(final String sourceUrl, final Object item, final AnalyzeRule analyzeRule, final String variable, final boolean log, final List<AnalyzeRule.SourceRule> ruleTitle, final List<AnalyzeRule.SourceRule> rulePubDate, final List<AnalyzeRule.SourceRule> ruleDescription, final List<AnalyzeRule.SourceRule> ruleImage, final List<AnalyzeRule.SourceRule> ruleLink, final DebugLog debugLog) {
        final RssArticle rssArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, variable, 1023, null);
        analyzeRule.setRuleData(rssArticle);
        AnalyzeRule.setContent$default(analyzeRule, item, null, 2, null);
        if (debugLog != null) {
            debugLog.log(sourceUrl, "\u250c\u83b7\u53d6\u6807\u9898", log);
        }
        rssArticle.setTitle(AnalyzeRule.getString$default(analyzeRule, ruleTitle, null, false, 6, null));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("\u2514", (Object)rssArticle.getTitle()), log);
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "\u250c\u83b7\u53d6\u65f6\u95f4", log);
        }
        rssArticle.setPubDate(AnalyzeRule.getString$default(analyzeRule, rulePubDate, null, false, 6, null));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("\u2514", (Object)rssArticle.getPubDate()), log);
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "\u250c\u83b7\u53d6\u63cf\u8ff0", log);
        }
        final Collection collection = ruleDescription;
        if (collection == null || collection.isEmpty()) {
            rssArticle.setDescription(null);
            if (debugLog != null) {
                debugLog.log(sourceUrl, "\u2514\u63cf\u8ff0\u89c4\u5219\u4e3a\u7a7a\uff0c\u5c06\u4f1a\u89e3\u6790\u5185\u5bb9\u9875", log);
            }
        }
        else {
            rssArticle.setDescription(AnalyzeRule.getString$default(analyzeRule, ruleDescription, null, false, 6, null));
            if (debugLog != null) {
                debugLog.log(sourceUrl, Intrinsics.stringPlus("\u2514", (Object)rssArticle.getDescription()), log);
            }
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "\u250c\u83b7\u53d6\u56fe\u7247url", log);
        }
        rssArticle.setImage(AnalyzeRule.getString$default(analyzeRule, ruleImage, null, true, 2, null));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("\u2514", (Object)rssArticle.getImage()), log);
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "\u250c\u83b7\u53d6\u6587\u7ae0\u94fe\u63a5", log);
        }
        rssArticle.setLink(NetworkUtils.INSTANCE.getAbsoluteURL(sourceUrl, AnalyzeRule.getString$default(analyzeRule, ruleLink, null, false, 6, null)));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("\u2514", (Object)rssArticle.getLink()), log);
        }
        if (StringsKt.isBlank((CharSequence)rssArticle.getTitle())) {
            return null;
        }
        return rssArticle;
    }
    
    static {
        INSTANCE = new RssParserByRule();
    }
}
