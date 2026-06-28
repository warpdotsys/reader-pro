package io.legado.app.model.rss;

import io.legado.app.data.entities.RssArticle;
import io.legado.app.data.entities.RssSource;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.model.analyzeRule.RuleData;
import io.legado.app.utils.NetworkUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: RssParserByRule.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/rss/RssParserByRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0098\u0001\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0010\u0010\r\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0011\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0012\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\u0010\u0010\u0013\u001a\f\u0012\b\u0012\u00060\u000fR\u00020\t0\u000e2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0002JN\u0010\u0016\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00172\u0006\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u00062\b\u0010\u001b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015¨\u0006 "}, d2 = {"Lio/legado/app/model/rss/RssParserByRule;", PackageDocumentBase.PREFIX_OPF, "()V", "getItem", "Lio/legado/app/data/entities/RssArticle;", "sourceUrl", PackageDocumentBase.PREFIX_OPF, "item", "analyzeRule", "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "variable", "log", PackageDocumentBase.PREFIX_OPF, "ruleTitle", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;", "rulePubDate", "ruleDescription", "ruleImage", "ruleLink", "debugLog", "Lio/legado/app/model/DebugLog;", "parseXML", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, "sortName", "sortUrl", NCXDocumentV3.XHTMLTgs.body, "rssSource", "Lio/legado/app/data/entities/RssSource;", "ruleData", "Lio/legado/app/model/analyzeRule/RuleData;", "reader-pro"})
public final class RssParserByRule {

    @NotNull
    public static final RssParserByRule INSTANCE = new RssParserByRule();

    private RssParserByRule() {
    }

    @NotNull
    public final Pair<List<RssArticle>, String> parseXML(@NotNull String sortName, @NotNull String sortUrl, @Nullable String body, @NotNull RssSource rssSource, @NotNull RuleData ruleData, @Nullable DebugLog debugLog) throws Exception {
        Intrinsics.checkNotNullParameter(sortName, "sortName");
        Intrinsics.checkNotNullParameter(sortUrl, "sortUrl");
        Intrinsics.checkNotNullParameter(rssSource, "rssSource");
        Intrinsics.checkNotNullParameter(ruleData, "ruleData");
        String sourceUrl = rssSource.getSourceUrl();
        String nextUrl = null;
        String str = body;
        if (str == null || StringsKt.isBlank(str)) {
            throw new NoStackTraceException(Intrinsics.stringPlus("error_get_web_content: ", rssSource.getSourceUrl()));
        }
        String ruleArticles = rssSource.getRuleArticles();
        String str2 = ruleArticles;
        if (str2 == null || StringsKt.isBlank(str2)) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "⇒列表规则为空, 使用默认规则解析", false, 4, null);
            }
            return RssParserDefault.INSTANCE.parseXML(sortName, body, sourceUrl, debugLog);
        }
        List articleList = new ArrayList();
        AnalyzeRule analyzeRule = new AnalyzeRule(ruleData, rssSource, debugLog);
        AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(sortUrl);
        analyzeRule.setRedirectUrl(sortUrl);
        boolean reverse = false;
        if (StringsKt.startsWith$default(ruleArticles, "-", false, 2, (Object) null)) {
            reverse = true;
            if (ruleArticles == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring = ruleArticles.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            ruleArticles = strSubstring;
        }
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取列表", false, 4, null);
        }
        List<Object> elements = analyzeRule.getElements(ruleArticles);
        if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└列表大小:", Integer.valueOf(elements.size())), false, 4, null);
        }
        String ruleNextPage = rssSource.getRuleNextPage();
        if (!(ruleNextPage == null || ruleNextPage.length() == 0)) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取下一页链接", false, 4, null);
            }
            String ruleNextPage2 = rssSource.getRuleNextPage();
            Intrinsics.checkNotNull(ruleNextPage2);
            Locale locale = Locale.getDefault();
            Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
            if (ruleNextPage2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String upperCase = ruleNextPage2.toUpperCase(locale);
            Intrinsics.checkNotNullExpressionValue(upperCase, "(this as java.lang.String).toUpperCase(locale)");
            if (Intrinsics.areEqual(upperCase, "PAGE")) {
                nextUrl = sortUrl;
            } else {
                nextUrl = AnalyzeRule.getString$default(analyzeRule, rssSource.getRuleNextPage(), (Object) null, false, 6, (Object) null);
                if (nextUrl.length() > 0) {
                    nextUrl = NetworkUtils.INSTANCE.getAbsoluteURL(sortUrl, nextUrl);
                }
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", nextUrl), false, 4, null);
            }
        }
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleTitle(), false, 2, null);
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default2 = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRulePubDate(), false, 2, null);
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default3 = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleDescription(), false, 2, null);
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default4 = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleImage(), false, 2, null);
        List<AnalyzeRule.SourceRule> listSplitSourceRule$default5 = AnalyzeRule.splitSourceRule$default(analyzeRule, rssSource.getRuleLink(), false, 2, null);
        String variable = ruleData.getVariable();
        int i = 0;
        for (Object item : elements) {
            int index = i;
            i++;
            RssArticle it = getItem(sourceUrl, item, analyzeRule, variable, index == 0, listSplitSourceRule$default, listSplitSourceRule$default2, listSplitSourceRule$default3, listSplitSourceRule$default4, listSplitSourceRule$default5, debugLog);
            if (it != null) {
                it.setSort(sortName);
                it.setOrigin(sourceUrl);
                articleList.add(it);
            }
        }
        if (reverse) {
            CollectionsKt.reverse(articleList);
        }
        return new Pair<>(articleList, nextUrl);
    }

    private final RssArticle getItem(String sourceUrl, Object item, AnalyzeRule analyzeRule, String variable, boolean log, List<AnalyzeRule.SourceRule> ruleTitle, List<AnalyzeRule.SourceRule> rulePubDate, List<AnalyzeRule.SourceRule> ruleDescription, List<AnalyzeRule.SourceRule> ruleImage, List<AnalyzeRule.SourceRule> ruleLink, DebugLog debugLog) {
        RssArticle rssArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, variable, 1023, null);
        analyzeRule.setRuleData(rssArticle);
        AnalyzeRule.setContent$default(analyzeRule, item, null, 2, null);
        if (debugLog != null) {
            debugLog.log(sourceUrl, "┌获取标题", log);
        }
        rssArticle.setTitle(AnalyzeRule.getString$default(analyzeRule, (List) ruleTitle, (Object) null, false, 6, (Object) null));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getTitle()), log);
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "┌获取时间", log);
        }
        rssArticle.setPubDate(AnalyzeRule.getString$default(analyzeRule, (List) rulePubDate, (Object) null, false, 6, (Object) null));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getPubDate()), log);
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "┌获取描述", log);
        }
        List<AnalyzeRule.SourceRule> list = ruleDescription;
        if (list == null || list.isEmpty()) {
            rssArticle.setDescription(null);
            if (debugLog != null) {
                debugLog.log(sourceUrl, "└描述规则为空，将会解析内容页", log);
            }
        } else {
            rssArticle.setDescription(AnalyzeRule.getString$default(analyzeRule, (List) ruleDescription, (Object) null, false, 6, (Object) null));
            if (debugLog != null) {
                debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getDescription()), log);
            }
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "┌获取图片url", log);
        }
        rssArticle.setImage(AnalyzeRule.getString$default(analyzeRule, (List) ruleImage, (Object) null, true, 2, (Object) null));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getImage()), log);
        }
        if (debugLog != null) {
            debugLog.log(sourceUrl, "┌获取文章链接", log);
        }
        rssArticle.setLink(NetworkUtils.INSTANCE.getAbsoluteURL(sourceUrl, AnalyzeRule.getString$default(analyzeRule, (List) ruleLink, (Object) null, false, 6, (Object) null)));
        if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getLink()), log);
        }
        if (StringsKt.isBlank(rssArticle.getTitle())) {
            return null;
        }
        return rssArticle;
    }
}
