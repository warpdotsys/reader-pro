package io.legado.app.help;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import io.legado.app.constant.AppConst;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.data.entities.rule.ContentRule;
import io.legado.app.data.entities.rule.ExploreRule;
import io.legado.app.data.entities.rule.SearchRule;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.model.Debug;
import io.legado.app.model.DebugLog;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.JsonExtensionsKt;
import io.legado.app.utils.StringExtensionsKt;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: SourceAnalyzer.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/SourceAnalyzer.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u001cB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\f\u0010\rJ*\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\u0006\u0010\u0010\u001a\u00020\u0011ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0012\u0010\u0013J*\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u000f0\b2\u0006\u0010\n\u001a\u00020\u000bø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0012\u0010\rJ\u0014\u0010\u0014\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u000b2\b\u0010\u0019\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u000b2\b\u0010\u001b\u001a\u0004\u0018\u00010\u000bH\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u001d"}, d2 = {"Lio/legado/app/help/SourceAnalyzer;", PackageDocumentBase.PREFIX_OPF, "()V", "headerPattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "jsPattern", "jsonToBookSource", "Lkotlin/Result;", "Lio/legado/app/data/entities/BookSource;", "json", PackageDocumentBase.PREFIX_OPF, "jsonToBookSource-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "jsonToBookSources", PackageDocumentBase.PREFIX_OPF, "inputStream", "Ljava/io/InputStream;", "jsonToBookSources-IoAF18A", "(Ljava/io/InputStream;)Ljava/lang/Object;", "toNewRule", "oldRule", "toNewUrl", "oldUrl", "toNewUrls", "oldUrls", "uaToHeader", "ua", "BookSourceAny", "reader-pro"})
public final class SourceAnalyzer {

    @NotNull
    public static final SourceAnalyzer INSTANCE = new SourceAnalyzer();
    private static final Pattern headerPattern = Pattern.compile("@Header:\\{.+?\\}", 2);
    private static final Pattern jsPattern = Pattern.compile("\\{\\{.+?\\}\\}", 2);

    private SourceAnalyzer() {
    }

    @NotNull
    /* JADX INFO: renamed from: jsonToBookSources-IoAF18A, reason: not valid java name */
    public final Object m171jsonToBookSourcesIoAF18A(@NotNull String json) {
        Object obj;
        Intrinsics.checkNotNullParameter(json, "json");
        try {
            Result.Companion companion = Result.Companion;
            List bookSources = new ArrayList();
            if (StringExtensionsKt.isJsonArray(json)) {
                Object obj2 = JsonExtensionsKt.getJsonPath().parse(json).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(obj2, "jsonPath.parse(json).read(\"$\")");
                List<Map> items = (List) obj2;
                for (Map item : items) {
                    DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse(item);
                    SourceAnalyzer sourceAnalyzer = INSTANCE;
                    String strJsonString = jsonItem.jsonString();
                    Intrinsics.checkNotNullExpressionValue(strJsonString, "jsonItem.jsonString()");
                    Object objM173jsonToBookSourceIoAF18A = sourceAnalyzer.m173jsonToBookSourceIoAF18A(strJsonString);
                    ResultKt.throwOnFailure(objM173jsonToBookSourceIoAF18A);
                    BookSource it = (BookSource) objM173jsonToBookSourceIoAF18A;
                    bookSources.add(it);
                }
            } else if (StringExtensionsKt.isJsonObject(json)) {
                Object objM173jsonToBookSourceIoAF18A2 = INSTANCE.m173jsonToBookSourceIoAF18A(json);
                ResultKt.throwOnFailure(objM173jsonToBookSourceIoAF18A2);
                BookSource it2 = (BookSource) objM173jsonToBookSourceIoAF18A2;
                bookSources.add(it2);
            } else {
                throw new NoStackTraceException("格式不对");
            }
            obj = Result.constructor-impl(bookSources);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        return obj;
    }

    @NotNull
    /* JADX INFO: renamed from: jsonToBookSources-IoAF18A, reason: not valid java name */
    public final Object m172jsonToBookSourcesIoAF18A(@NotNull InputStream inputStream) {
        Object obj;
        Object obj2;
        Intrinsics.checkNotNullParameter(inputStream, "inputStream");
        try {
            Result.Companion companion = Result.Companion;
            List bookSources = new ArrayList();
            try {
                Result.Companion companion2 = Result.Companion;
                Object obj3 = JsonExtensionsKt.getJsonPath().parse(inputStream).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(obj3, "jsonPath.parse(inputStream).read(\"$\")");
                List<Map> items = (List) obj3;
                for (Map item : items) {
                    DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse(item);
                    SourceAnalyzer sourceAnalyzer = INSTANCE;
                    String strJsonString = jsonItem.jsonString();
                    Intrinsics.checkNotNullExpressionValue(strJsonString, "jsonItem.jsonString()");
                    Object objM173jsonToBookSourceIoAF18A = sourceAnalyzer.m173jsonToBookSourceIoAF18A(strJsonString);
                    ResultKt.throwOnFailure(objM173jsonToBookSourceIoAF18A);
                    BookSource it = (BookSource) objM173jsonToBookSourceIoAF18A;
                    bookSources.add(it);
                }
                obj2 = Result.constructor-impl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion3 = Result.Companion;
                obj2 = Result.constructor-impl(ResultKt.createFailure(th));
            }
            if (Result.exceptionOrNull-impl(obj2) != null) {
                Object obj4 = JsonExtensionsKt.getJsonPath().parse(inputStream).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(obj4, "jsonPath.parse(inputStream).read(\"$\")");
                Map item2 = (Map) obj4;
                DocumentContext jsonItem2 = JsonExtensionsKt.getJsonPath().parse(item2);
                SourceAnalyzer sourceAnalyzer2 = INSTANCE;
                String strJsonString2 = jsonItem2.jsonString();
                Intrinsics.checkNotNullExpressionValue(strJsonString2, "jsonItem.jsonString()");
                Object objM173jsonToBookSourceIoAF18A2 = sourceAnalyzer2.m173jsonToBookSourceIoAF18A(strJsonString2);
                ResultKt.throwOnFailure(objM173jsonToBookSourceIoAF18A2);
                BookSource it2 = (BookSource) objM173jsonToBookSourceIoAF18A2;
                bookSources.add(it2);
            }
            obj = Result.constructor-impl(bookSources);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th2));
        }
        return obj;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0342  */
    /* JADX WARN: Type inference failed for: r2v13, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$4] */
    /* JADX WARN: Type inference failed for: r2v180, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource-IoAF18A$$inlined$fromJsonObject$1] */
    /* JADX WARN: Type inference failed for: r2v20, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$6] */
    /* JADX WARN: Type inference failed for: r2v27, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$8] */
    /* JADX WARN: Type inference failed for: r2v34, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$10] */
    /* JADX WARN: Type inference failed for: r2v39, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$9] */
    /* JADX WARN: Type inference failed for: r2v44, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$7] */
    /* JADX WARN: Type inference failed for: r2v49, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$5] */
    /* JADX WARN: Type inference failed for: r2v54, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$3] */
    /* JADX WARN: Type inference failed for: r2v59, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$1] */
    /* JADX WARN: Type inference failed for: r2v6, types: [io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$2] */
    @NotNull
    /* JADX INFO: renamed from: jsonToBookSource-IoAF18A, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object m173jsonToBookSourceIoAF18A(@NotNull String json) {
        Object obj;
        Object obj2;
        String string;
        Object obj3;
        BookSource bookSource;
        ExploreRule exploreRule;
        Object obj4;
        BookSource bookSource2;
        SearchRule searchRule;
        Object obj5;
        BookSource bookSource3;
        BookInfoRule bookInfoRule;
        Object obj6;
        BookSource bookSource4;
        TocRule tocRule;
        Object obj7;
        BookSource bookSource5;
        ContentRule contentRule;
        Object obj8;
        Object obj9;
        Object obj10;
        Object obj11;
        Object obj12;
        int i;
        Intrinsics.checkNotNullParameter(json, "json");
        BookSource source = new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null);
        Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
        String json$iv = StringsKt.trim(json).toString();
        try {
            Result.Companion companion = Result.Companion;
            Type type = new TypeToken<BookSourceAny>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource-IoAF18A$$inlined$fromJsonObject$1
            }.getType();
            Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
            Object objFromJson = $this$fromJsonObject$iv.fromJson(json$iv, type);
            if (!(objFromJson instanceof BookSourceAny)) {
                objFromJson = null;
            }
            obj = Result.constructor-impl((BookSourceAny) objFromJson);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        Object obj13 = obj;
        Throwable it = Result.exceptionOrNull-impl(obj13);
        if (it != null) {
            DebugLog.DefaultImpls.log$default(Debug.INSTANCE, "转化书源出错", it.getLocalizedMessage(), false, 4, null);
            Unit unit = Unit.INSTANCE;
        }
        BookSourceAny sourceAny = (BookSourceAny) (Result.isFailure-impl(obj13) ? null : obj13);
        try {
            Result.Companion companion3 = Result.Companion;
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.Companion;
            obj2 = Result.constructor-impl(ResultKt.createFailure(th2));
        }
        if ((sourceAny == null ? null : sourceAny.getRuleToc()) == null) {
            ReadContext readContext = JsonExtensionsKt.getJsonPath().parse(StringsKt.trim(json).toString());
            Intrinsics.checkNotNullExpressionValue(readContext, "jsonItem");
            String string2 = JsonExtensionsKt.readString(readContext, "bookSourceUrl");
            if (string2 == null) {
                throw new NoStackTraceException("格式不对");
            }
            source.setBookSourceUrl(string2);
            String string3 = JsonExtensionsKt.readString(readContext, "bookSourceName");
            source.setBookSourceName(string3 == null ? PackageDocumentBase.PREFIX_OPF : string3);
            source.setBookSourceGroup(JsonExtensionsKt.readString(readContext, "bookSourceGroup"));
            String string4 = JsonExtensionsKt.readString(readContext, "bookSourceComment");
            source.setBookSourceComment(string4 == null ? PackageDocumentBase.PREFIX_OPF : string4);
            source.setBookUrlPattern(JsonExtensionsKt.readString(readContext, "ruleBookUrlPattern"));
            Integer num = JsonExtensionsKt.readInt(readContext, "serialNumber");
            source.setCustomOrder(num == null ? 0 : num.intValue());
            source.setHeader(INSTANCE.uaToHeader(JsonExtensionsKt.readString(readContext, "httpUserAgent")));
            source.setSearchUrl(INSTANCE.toNewUrl(JsonExtensionsKt.readString(readContext, "ruleSearchUrl")));
            source.setExploreUrl(INSTANCE.toNewUrls(JsonExtensionsKt.readString(readContext, "ruleFindUrl")));
            String sourceType = JsonExtensionsKt.readString(readContext, "bookSourceType");
            if (sourceType != null) {
                switch (sourceType.hashCode()) {
                    case 49:
                        i = !sourceType.equals("1") ? 0 : 1;
                        break;
                    case 50:
                        if (sourceType.equals("2")) {
                            i = 2;
                        }
                        break;
                    case 51:
                        if (sourceType.equals("3")) {
                            i = 3;
                        }
                        break;
                    case 2157948:
                        if (sourceType.equals("FILE")) {
                            i = 3;
                        }
                        break;
                    case 3143036:
                        if (sourceType.equals("file")) {
                            i = 3;
                        }
                        break;
                    case 62628790:
                        if (sourceType.equals("AUDIO")) {
                            i = 1;
                        }
                        break;
                    case 69775675:
                        if (sourceType.equals("IMAGE")) {
                            i = 2;
                        }
                        break;
                    case 93166550:
                        if (sourceType.equals("audio")) {
                            i = 1;
                        }
                        break;
                    case 100313435:
                        if (sourceType.equals("image")) {
                            i = 2;
                        }
                        break;
                    default:
                        break;
                }
                source.setBookSourceType(i);
                Boolean bool = JsonExtensionsKt.readBool(readContext, "enable");
                source.setEnabled(bool == null ? true : bool.booleanValue());
                String exploreUrl = source.getExploreUrl();
                if (exploreUrl == null || StringsKt.isBlank(exploreUrl)) {
                    source.setEnabledExplore(false);
                }
                source.setRuleSearch(new SearchRule(INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchList")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchName")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchAuthor")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchIntroduce")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchKind")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchLastChapter")), null, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchNoteUrl")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleSearchCoverUrl")), null, 576, null));
                source.setRuleExplore(new ExploreRule(INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindList")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindName")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindAuthor")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindIntroduce")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindKind")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindLastChapter")), null, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindNoteUrl")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleFindCoverUrl")), null, 576, null));
                source.setRuleBookInfo(new BookInfoRule(INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookInfoInit")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookName")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookAuthor")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleIntroduce")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookKind")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookLastChapter")), null, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleCoverUrl")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleChapterUrl")), null, null, 1600, null));
                source.setRuleToc(new TocRule(null, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleChapterList")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleChapterName")), INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleContentUrl")), null, null, null, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleChapterUrlNext")), 113, null));
                String newRule = INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookContent"));
                String content = newRule == null ? PackageDocumentBase.PREFIX_OPF : newRule;
                if (StringsKt.startsWith$default(content, "$", false, 2, (Object) null) && !StringsKt.startsWith$default(content, "$.", false, 2, (Object) null)) {
                    String strSubstring = content.substring(1);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    content = strSubstring;
                }
                source.setRuleContent(new ContentRule(content, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleContentUrlNext")), null, null, INSTANCE.toNewRule(JsonExtensionsKt.readString(readContext, "ruleBookContentReplace")), null, 44, null));
                Unit unit2 = Unit.INSTANCE;
            }
            return obj2;
        }
        source.setBookSourceUrl(sourceAny.getBookSourceUrl());
        source.setBookSourceName(sourceAny.getBookSourceName());
        source.setBookSourceGroup(sourceAny.getBookSourceGroup());
        source.setBookSourceType(sourceAny.getBookSourceType());
        source.setBookUrlPattern(sourceAny.getBookUrlPattern());
        source.setCustomOrder(sourceAny.getCustomOrder());
        source.setEnabled(sourceAny.getEnabled());
        source.setEnabledExplore(sourceAny.getEnabledExplore());
        source.setEnabledCookieJar(Boolean.valueOf(sourceAny.getEnabledCookieJar()));
        source.setConcurrentRate(sourceAny.getConcurrentRate());
        source.setHeader(sourceAny.getHeader());
        Object loginUrl = sourceAny.getLoginUrl();
        if (loginUrl == null) {
            string = null;
        } else if (loginUrl instanceof String) {
            string = String.valueOf(sourceAny.getLoginUrl());
        } else {
            ReadContext readContext2 = JsonPath.parse(sourceAny.getLoginUrl());
            Intrinsics.checkNotNullExpressionValue(readContext2, "parse(sourceAny.loginUrl)");
            string = JsonExtensionsKt.readString(readContext2, RSSKeywords.RSS_ITEM_URL);
        }
        source.setLoginUrl(string);
        source.setLoginCheckJs(sourceAny.getLoginCheckJs());
        source.setBookSourceComment(sourceAny.getBookSourceComment());
        source.setLastUpdateTime(sourceAny.getLastUpdateTime());
        source.setRespondTime(sourceAny.getRespondTime());
        source.setWeight(sourceAny.getWeight());
        source.setExploreUrl(sourceAny.getExploreUrl());
        if (sourceAny.getRuleExplore() instanceof String) {
            Gson $this$fromJsonObject$iv2 = GsonExtensionsKt.getGSON();
            String json$iv2 = String.valueOf(sourceAny.getRuleExplore());
            try {
                Result.Companion companion5 = Result.Companion;
                Type type2 = new TypeToken<ExploreRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$1
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type2, "object : TypeToken<T>() {}.type");
                Object objFromJson2 = $this$fromJsonObject$iv2.fromJson(json$iv2, type2);
                if (!(objFromJson2 instanceof ExploreRule)) {
                    objFromJson2 = null;
                }
                obj12 = Result.constructor-impl((ExploreRule) objFromJson2);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.Companion;
                obj12 = Result.constructor-impl(ResultKt.createFailure(th3));
            }
            Object obj14 = obj12;
            bookSource = source;
            exploreRule = (ExploreRule) (Result.isFailure-impl(obj14) ? null : obj14);
        } else {
            Gson $this$fromJsonObject$iv3 = GsonExtensionsKt.getGSON();
            String json$iv3 = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleExplore());
            try {
                Result.Companion companion7 = Result.Companion;
                Type type3 = new TypeToken<ExploreRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$2
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type3, "object : TypeToken<T>() {}.type");
                Object objFromJson3 = $this$fromJsonObject$iv3.fromJson(json$iv3, type3);
                if (!(objFromJson3 instanceof ExploreRule)) {
                    objFromJson3 = null;
                }
                obj3 = Result.constructor-impl((ExploreRule) objFromJson3);
            } catch (Throwable th4) {
                Result.Companion companion8 = Result.Companion;
                obj3 = Result.constructor-impl(ResultKt.createFailure(th4));
            }
            Object obj15 = obj3;
            bookSource = source;
            exploreRule = (ExploreRule) (Result.isFailure-impl(obj15) ? null : obj15);
        }
        bookSource.setRuleExplore(exploreRule);
        source.setSearchUrl(sourceAny.getSearchUrl());
        if (sourceAny.getRuleSearch() instanceof String) {
            Gson $this$fromJsonObject$iv4 = GsonExtensionsKt.getGSON();
            String json$iv4 = String.valueOf(sourceAny.getRuleSearch());
            try {
                Result.Companion companion9 = Result.Companion;
                Type type4 = new TypeToken<SearchRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$3
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type4, "object : TypeToken<T>() {}.type");
                Object objFromJson4 = $this$fromJsonObject$iv4.fromJson(json$iv4, type4);
                if (!(objFromJson4 instanceof SearchRule)) {
                    objFromJson4 = null;
                }
                obj11 = Result.constructor-impl((SearchRule) objFromJson4);
            } catch (Throwable th5) {
                Result.Companion companion10 = Result.Companion;
                obj11 = Result.constructor-impl(ResultKt.createFailure(th5));
            }
            Object obj16 = obj11;
            bookSource2 = source;
            searchRule = (SearchRule) (Result.isFailure-impl(obj16) ? null : obj16);
        } else {
            Gson $this$fromJsonObject$iv5 = GsonExtensionsKt.getGSON();
            String json$iv5 = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleSearch());
            try {
                Result.Companion companion11 = Result.Companion;
                Type type5 = new TypeToken<SearchRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$4
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type5, "object : TypeToken<T>() {}.type");
                Object objFromJson5 = $this$fromJsonObject$iv5.fromJson(json$iv5, type5);
                if (!(objFromJson5 instanceof SearchRule)) {
                    objFromJson5 = null;
                }
                obj4 = Result.constructor-impl((SearchRule) objFromJson5);
            } catch (Throwable th6) {
                Result.Companion companion12 = Result.Companion;
                obj4 = Result.constructor-impl(ResultKt.createFailure(th6));
            }
            Object obj17 = obj4;
            bookSource2 = source;
            searchRule = (SearchRule) (Result.isFailure-impl(obj17) ? null : obj17);
        }
        bookSource2.setRuleSearch(searchRule);
        if (sourceAny.getRuleBookInfo() instanceof String) {
            Gson $this$fromJsonObject$iv6 = GsonExtensionsKt.getGSON();
            String json$iv6 = String.valueOf(sourceAny.getRuleBookInfo());
            try {
                Result.Companion companion13 = Result.Companion;
                Type type6 = new TypeToken<BookInfoRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$5
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type6, "object : TypeToken<T>() {}.type");
                Object objFromJson6 = $this$fromJsonObject$iv6.fromJson(json$iv6, type6);
                if (!(objFromJson6 instanceof BookInfoRule)) {
                    objFromJson6 = null;
                }
                obj10 = Result.constructor-impl((BookInfoRule) objFromJson6);
            } catch (Throwable th7) {
                Result.Companion companion14 = Result.Companion;
                obj10 = Result.constructor-impl(ResultKt.createFailure(th7));
            }
            Object obj18 = obj10;
            bookSource3 = source;
            bookInfoRule = (BookInfoRule) (Result.isFailure-impl(obj18) ? null : obj18);
        } else {
            Gson $this$fromJsonObject$iv7 = GsonExtensionsKt.getGSON();
            String json$iv7 = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleBookInfo());
            try {
                Result.Companion companion15 = Result.Companion;
                Type type7 = new TypeToken<BookInfoRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$6
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type7, "object : TypeToken<T>() {}.type");
                Object objFromJson7 = $this$fromJsonObject$iv7.fromJson(json$iv7, type7);
                if (!(objFromJson7 instanceof BookInfoRule)) {
                    objFromJson7 = null;
                }
                obj5 = Result.constructor-impl((BookInfoRule) objFromJson7);
            } catch (Throwable th8) {
                Result.Companion companion16 = Result.Companion;
                obj5 = Result.constructor-impl(ResultKt.createFailure(th8));
            }
            Object obj19 = obj5;
            bookSource3 = source;
            bookInfoRule = (BookInfoRule) (Result.isFailure-impl(obj19) ? null : obj19);
        }
        bookSource3.setRuleBookInfo(bookInfoRule);
        if (sourceAny.getRuleToc() instanceof String) {
            Gson $this$fromJsonObject$iv8 = GsonExtensionsKt.getGSON();
            String json$iv8 = String.valueOf(sourceAny.getRuleToc());
            try {
                Result.Companion companion17 = Result.Companion;
                Type type8 = new TypeToken<TocRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$7
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type8, "object : TypeToken<T>() {}.type");
                Object objFromJson8 = $this$fromJsonObject$iv8.fromJson(json$iv8, type8);
                if (!(objFromJson8 instanceof TocRule)) {
                    objFromJson8 = null;
                }
                obj9 = Result.constructor-impl((TocRule) objFromJson8);
            } catch (Throwable th9) {
                Result.Companion companion18 = Result.Companion;
                obj9 = Result.constructor-impl(ResultKt.createFailure(th9));
            }
            Object obj20 = obj9;
            bookSource4 = source;
            tocRule = (TocRule) (Result.isFailure-impl(obj20) ? null : obj20);
        } else {
            Gson $this$fromJsonObject$iv9 = GsonExtensionsKt.getGSON();
            String json$iv9 = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleToc());
            try {
                Result.Companion companion19 = Result.Companion;
                Type type9 = new TypeToken<TocRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$8
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type9, "object : TypeToken<T>() {}.type");
                Object objFromJson9 = $this$fromJsonObject$iv9.fromJson(json$iv9, type9);
                if (!(objFromJson9 instanceof TocRule)) {
                    objFromJson9 = null;
                }
                obj6 = Result.constructor-impl((TocRule) objFromJson9);
            } catch (Throwable th10) {
                Result.Companion companion20 = Result.Companion;
                obj6 = Result.constructor-impl(ResultKt.createFailure(th10));
            }
            Object obj21 = obj6;
            bookSource4 = source;
            tocRule = (TocRule) (Result.isFailure-impl(obj21) ? null : obj21);
        }
        bookSource4.setRuleToc(tocRule);
        if (sourceAny.getRuleContent() instanceof String) {
            Gson $this$fromJsonObject$iv10 = GsonExtensionsKt.getGSON();
            String json$iv10 = String.valueOf(sourceAny.getRuleContent());
            try {
                Result.Companion companion21 = Result.Companion;
                Type type10 = new TypeToken<ContentRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$9
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type10, "object : TypeToken<T>() {}.type");
                Object objFromJson10 = $this$fromJsonObject$iv10.fromJson(json$iv10, type10);
                if (!(objFromJson10 instanceof ContentRule)) {
                    objFromJson10 = null;
                }
                obj8 = Result.constructor-impl((ContentRule) objFromJson10);
            } catch (Throwable th11) {
                Result.Companion companion22 = Result.Companion;
                obj8 = Result.constructor-impl(ResultKt.createFailure(th11));
            }
            Object obj22 = obj8;
            bookSource5 = source;
            contentRule = (ContentRule) (Result.isFailure-impl(obj22) ? null : obj22);
        } else {
            Gson $this$fromJsonObject$iv11 = GsonExtensionsKt.getGSON();
            String json$iv11 = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleContent());
            try {
                Result.Companion companion23 = Result.Companion;
                Type type11 = new TypeToken<ContentRule>() { // from class: io.legado.app.help.SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$10
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type11, "object : TypeToken<T>() {}.type");
                Object objFromJson11 = $this$fromJsonObject$iv11.fromJson(json$iv11, type11);
                if (!(objFromJson11 instanceof ContentRule)) {
                    objFromJson11 = null;
                }
                obj7 = Result.constructor-impl((ContentRule) objFromJson11);
            } catch (Throwable th12) {
                Result.Companion companion24 = Result.Companion;
                obj7 = Result.constructor-impl(ResultKt.createFailure(th12));
            }
            Object obj23 = obj7;
            bookSource5 = source;
            contentRule = (ContentRule) (Result.isFailure-impl(obj23) ? null : obj23);
        }
        bookSource5.setRuleContent(contentRule);
        obj2 = Result.constructor-impl(source);
        return obj2;
    }

    /* JADX INFO: compiled from: SourceAnalyzer.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/SourceAnalyzer$BookSourceAny.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\be\b\u0086\b\u0018\u00002\u00020\u0001B\u009d\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u001fJ\t\u0010\\\u001a\u00020\u0003HÆ\u0003J\u000b\u0010]\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010^\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010_\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010`\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010a\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010c\u001a\u00020\u0015HÆ\u0003J\t\u0010d\u001a\u00020\u0015HÆ\u0003J\t\u0010e\u001a\u00020\u0007HÆ\u0003J\u000b\u0010f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010g\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010h\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010i\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010j\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010k\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010l\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\u000b\u0010m\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\t\u0010n\u001a\u00020\u0003HÆ\u0003J\t\u0010o\u001a\u00020\u0007HÆ\u0003J\u000b\u0010p\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010q\u001a\u00020\u0007HÆ\u0003J\t\u0010r\u001a\u00020\u000bHÆ\u0003J\t\u0010s\u001a\u00020\u000bHÆ\u0003J\t\u0010t\u001a\u00020\u000bHÆ\u0003J¡\u0002\u0010u\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u00072\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÆ\u0001J\u0013\u0010v\u001a\u00020\u000b2\b\u0010w\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010x\u001a\u00020\u0007HÖ\u0001J\t\u0010y\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010!\"\u0004\b%\u0010#R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010!\"\u0004\b'\u0010#R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010!\"\u0004\b-\u0010#R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010!\"\u0004\b/\u0010#R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010!\"\u0004\b1\u0010#R\u001a\u0010\t\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u0010)\"\u0004\b3\u0010+R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u001a\u0010\r\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u00105\"\u0004\b9\u00107R\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u00105\"\u0004\b;\u00107R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010!\"\u0004\b=\u0010#R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010!\"\u0004\b?\u0010#R\u001a\u0010\u0014\u001a\u00020\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010!\"\u0004\bE\u0010#R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bF\u0010G\"\u0004\bH\u0010IR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010G\"\u0004\bK\u0010IR\u001a\u0010\u0016\u001a\u00020\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010A\"\u0004\bM\u0010CR\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bN\u0010G\"\u0004\bO\u0010IR\u001c\u0010\u001e\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bP\u0010G\"\u0004\bQ\u0010IR\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010G\"\u0004\bS\u0010IR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bT\u0010G\"\u0004\bU\u0010IR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0001X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bV\u0010G\"\u0004\bW\u0010IR\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010!\"\u0004\bY\u0010#R\u001a\u0010\u0017\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010)\"\u0004\b[\u0010+¨\u0006z"}, d2 = {"Lio/legado/app/help/SourceAnalyzer$BookSourceAny;", PackageDocumentBase.PREFIX_OPF, "bookSourceName", PackageDocumentBase.PREFIX_OPF, "bookSourceGroup", "bookSourceUrl", "bookSourceType", PackageDocumentBase.PREFIX_OPF, "bookUrlPattern", "customOrder", "enabled", PackageDocumentBase.PREFIX_OPF, "enabledExplore", "enabledCookieJar", "concurrentRate", "header", "loginUrl", "loginUi", "loginCheckJs", "bookSourceComment", "lastUpdateTime", PackageDocumentBase.PREFIX_OPF, "respondTime", "weight", "exploreUrl", "ruleExplore", "searchUrl", "ruleSearch", "ruleBookInfo", "ruleToc", "ruleContent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZZLjava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getBookSourceComment", "()Ljava/lang/String;", "setBookSourceComment", "(Ljava/lang/String;)V", "getBookSourceGroup", "setBookSourceGroup", "getBookSourceName", "setBookSourceName", "getBookSourceType", "()I", "setBookSourceType", "(I)V", "getBookSourceUrl", "setBookSourceUrl", "getBookUrlPattern", "setBookUrlPattern", "getConcurrentRate", "setConcurrentRate", "getCustomOrder", "setCustomOrder", "getEnabled", "()Z", "setEnabled", "(Z)V", "getEnabledCookieJar", "setEnabledCookieJar", "getEnabledExplore", "setEnabledExplore", "getExploreUrl", "setExploreUrl", "getHeader", "setHeader", "getLastUpdateTime", "()J", "setLastUpdateTime", "(J)V", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "()Ljava/lang/Object;", "setLoginUi", "(Ljava/lang/Object;)V", "getLoginUrl", "setLoginUrl", "getRespondTime", "setRespondTime", "getRuleBookInfo", "setRuleBookInfo", "getRuleContent", "setRuleContent", "getRuleExplore", "setRuleExplore", "getRuleSearch", "setRuleSearch", "getRuleToc", "setRuleToc", "getSearchUrl", "setSearchUrl", "getWeight", "setWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "reader-pro"})
    public static final /* data */ class BookSourceAny {

        @NotNull
        private String bookSourceName;

        @Nullable
        private String bookSourceGroup;

        @NotNull
        private String bookSourceUrl;
        private int bookSourceType;

        @Nullable
        private String bookUrlPattern;
        private int customOrder;
        private boolean enabled;
        private boolean enabledExplore;
        private boolean enabledCookieJar;

        @Nullable
        private String concurrentRate;

        @Nullable
        private String header;

        @Nullable
        private Object loginUrl;

        @Nullable
        private Object loginUi;

        @Nullable
        private String loginCheckJs;

        @Nullable
        private String bookSourceComment;
        private long lastUpdateTime;
        private long respondTime;
        private int weight;

        @Nullable
        private String exploreUrl;

        @Nullable
        private Object ruleExplore;

        @Nullable
        private String searchUrl;

        @Nullable
        private Object ruleSearch;

        @Nullable
        private Object ruleBookInfo;

        @Nullable
        private Object ruleToc;

        @Nullable
        private Object ruleContent;

        @NotNull
        public final String component1() {
            return this.bookSourceName;
        }

        @Nullable
        public final String component2() {
            return this.bookSourceGroup;
        }

        @NotNull
        public final String component3() {
            return this.bookSourceUrl;
        }

        public final int component4() {
            return this.bookSourceType;
        }

        @Nullable
        public final String component5() {
            return this.bookUrlPattern;
        }

        public final int component6() {
            return this.customOrder;
        }

        public final boolean component7() {
            return this.enabled;
        }

        public final boolean component8() {
            return this.enabledExplore;
        }

        public final boolean component9() {
            return this.enabledCookieJar;
        }

        @Nullable
        public final String component10() {
            return this.concurrentRate;
        }

        @Nullable
        public final String component11() {
            return this.header;
        }

        @Nullable
        public final Object component12() {
            return this.loginUrl;
        }

        @Nullable
        public final Object component13() {
            return this.loginUi;
        }

        @Nullable
        public final String component14() {
            return this.loginCheckJs;
        }

        @Nullable
        public final String component15() {
            return this.bookSourceComment;
        }

        public final long component16() {
            return this.lastUpdateTime;
        }

        public final long component17() {
            return this.respondTime;
        }

        public final int component18() {
            return this.weight;
        }

        @Nullable
        public final String component19() {
            return this.exploreUrl;
        }

        @Nullable
        public final Object component20() {
            return this.ruleExplore;
        }

        @Nullable
        public final String component21() {
            return this.searchUrl;
        }

        @Nullable
        public final Object component22() {
            return this.ruleSearch;
        }

        @Nullable
        public final Object component23() {
            return this.ruleBookInfo;
        }

        @Nullable
        public final Object component24() {
            return this.ruleToc;
        }

        @Nullable
        public final Object component25() {
            return this.ruleContent;
        }

        @NotNull
        public final BookSourceAny copy(@NotNull String bookSourceName, @Nullable String bookSourceGroup, @NotNull String bookSourceUrl, int bookSourceType, @Nullable String bookUrlPattern, int customOrder, boolean enabled, boolean enabledExplore, boolean enabledCookieJar, @Nullable String concurrentRate, @Nullable String header, @Nullable Object loginUrl, @Nullable Object loginUi, @Nullable String loginCheckJs, @Nullable String bookSourceComment, long lastUpdateTime, long respondTime, int weight, @Nullable String exploreUrl, @Nullable Object ruleExplore, @Nullable String searchUrl, @Nullable Object ruleSearch, @Nullable Object ruleBookInfo, @Nullable Object ruleToc, @Nullable Object ruleContent) {
            Intrinsics.checkNotNullParameter(bookSourceName, "bookSourceName");
            Intrinsics.checkNotNullParameter(bookSourceUrl, "bookSourceUrl");
            return new BookSourceAny(bookSourceName, bookSourceGroup, bookSourceUrl, bookSourceType, bookUrlPattern, customOrder, enabled, enabledExplore, enabledCookieJar, concurrentRate, header, loginUrl, loginUi, loginCheckJs, bookSourceComment, lastUpdateTime, respondTime, weight, exploreUrl, ruleExplore, searchUrl, ruleSearch, ruleBookInfo, ruleToc, ruleContent);
        }

        public static /* synthetic */ BookSourceAny copy$default(BookSourceAny bookSourceAny, String str, String str2, String str3, int i, String str4, int i2, boolean z, boolean z2, boolean z3, String str5, String str6, Object obj, Object obj2, String str7, String str8, long j, long j2, int i3, String str9, Object obj3, String str10, Object obj4, Object obj5, Object obj6, Object obj7, int i4, Object obj8) {
            if ((i4 & 1) != 0) {
                str = bookSourceAny.bookSourceName;
            }
            if ((i4 & 2) != 0) {
                str2 = bookSourceAny.bookSourceGroup;
            }
            if ((i4 & 4) != 0) {
                str3 = bookSourceAny.bookSourceUrl;
            }
            if ((i4 & 8) != 0) {
                i = bookSourceAny.bookSourceType;
            }
            if ((i4 & 16) != 0) {
                str4 = bookSourceAny.bookUrlPattern;
            }
            if ((i4 & 32) != 0) {
                i2 = bookSourceAny.customOrder;
            }
            if ((i4 & 64) != 0) {
                z = bookSourceAny.enabled;
            }
            if ((i4 & Wbxml.EXT_T_0) != 0) {
                z2 = bookSourceAny.enabledExplore;
            }
            if ((i4 & 256) != 0) {
                z3 = bookSourceAny.enabledCookieJar;
            }
            if ((i4 & 512) != 0) {
                str5 = bookSourceAny.concurrentRate;
            }
            if ((i4 & 1024) != 0) {
                str6 = bookSourceAny.header;
            }
            if ((i4 & 2048) != 0) {
                obj = bookSourceAny.loginUrl;
            }
            if ((i4 & 4096) != 0) {
                obj2 = bookSourceAny.loginUi;
            }
            if ((i4 & IOUtil.DEFAULT_BUFFER_SIZE) != 0) {
                str7 = bookSourceAny.loginCheckJs;
            }
            if ((i4 & 16384) != 0) {
                str8 = bookSourceAny.bookSourceComment;
            }
            if ((i4 & 32768) != 0) {
                j = bookSourceAny.lastUpdateTime;
            }
            if ((i4 & 65536) != 0) {
                j2 = bookSourceAny.respondTime;
            }
            if ((i4 & 131072) != 0) {
                i3 = bookSourceAny.weight;
            }
            if ((i4 & 262144) != 0) {
                str9 = bookSourceAny.exploreUrl;
            }
            if ((i4 & 524288) != 0) {
                obj3 = bookSourceAny.ruleExplore;
            }
            if ((i4 & 1048576) != 0) {
                str10 = bookSourceAny.searchUrl;
            }
            if ((i4 & 2097152) != 0) {
                obj4 = bookSourceAny.ruleSearch;
            }
            if ((i4 & 4194304) != 0) {
                obj5 = bookSourceAny.ruleBookInfo;
            }
            if ((i4 & 8388608) != 0) {
                obj6 = bookSourceAny.ruleToc;
            }
            if ((i4 & 16777216) != 0) {
                obj7 = bookSourceAny.ruleContent;
            }
            return bookSourceAny.copy(str, str2, str3, i, str4, i2, z, z2, z3, str5, str6, obj, obj2, str7, str8, j, j2, i3, str9, obj3, str10, obj4, obj5, obj6, obj7);
        }

        @NotNull
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("BookSourceAny(bookSourceName=").append(this.bookSourceName).append(", bookSourceGroup=").append((Object) this.bookSourceGroup).append(", bookSourceUrl=").append(this.bookSourceUrl).append(", bookSourceType=").append(this.bookSourceType).append(", bookUrlPattern=").append((Object) this.bookUrlPattern).append(", customOrder=").append(this.customOrder).append(", enabled=").append(this.enabled).append(", enabledExplore=").append(this.enabledExplore).append(", enabledCookieJar=").append(this.enabledCookieJar).append(", concurrentRate=").append((Object) this.concurrentRate).append(", header=").append((Object) this.header).append(", loginUrl=");
            sb.append(this.loginUrl).append(", loginUi=").append(this.loginUi).append(", loginCheckJs=").append((Object) this.loginCheckJs).append(", bookSourceComment=").append((Object) this.bookSourceComment).append(", lastUpdateTime=").append(this.lastUpdateTime).append(", respondTime=").append(this.respondTime).append(", weight=").append(this.weight).append(", exploreUrl=").append((Object) this.exploreUrl).append(", ruleExplore=").append(this.ruleExplore).append(", searchUrl=").append((Object) this.searchUrl).append(", ruleSearch=").append(this.ruleSearch).append(", ruleBookInfo=").append(this.ruleBookInfo);
            sb.append(", ruleToc=").append(this.ruleToc).append(", ruleContent=").append(this.ruleContent).append(')');
            return sb.toString();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v154 */
        /* JADX WARN: Type inference failed for: r1v155 */
        /* JADX WARN: Type inference failed for: r1v156 */
        /* JADX WARN: Type inference failed for: r1v159 */
        /* JADX WARN: Type inference failed for: r1v160 */
        /* JADX WARN: Type inference failed for: r1v161 */
        /* JADX WARN: Type inference failed for: r1v29, types: [int] */
        /* JADX WARN: Type inference failed for: r1v33, types: [int] */
        /* JADX WARN: Type inference failed for: r1v37, types: [int] */
        public int hashCode() {
            int iHashCode = ((((((((((this.bookSourceName.hashCode() * 31) + (this.bookSourceGroup == null ? 0 : this.bookSourceGroup.hashCode())) * 31) + this.bookSourceUrl.hashCode()) * 31) + Integer.hashCode(this.bookSourceType)) * 31) + (this.bookUrlPattern == null ? 0 : this.bookUrlPattern.hashCode())) * 31) + Integer.hashCode(this.customOrder)) * 31;
            boolean z = this.enabled;
            ?? r1 = z;
            if (z) {
                r1 = 1;
            }
            int i = (iHashCode + r1) * 31;
            boolean z2 = this.enabledExplore;
            ?? r12 = z2;
            if (z2) {
                r12 = 1;
            }
            int i2 = (i + r12) * 31;
            boolean z3 = this.enabledCookieJar;
            ?? r13 = z3;
            if (z3) {
                r13 = 1;
            }
            return ((((((((((((((((((((((((((((((((i2 + r13) * 31) + (this.concurrentRate == null ? 0 : this.concurrentRate.hashCode())) * 31) + (this.header == null ? 0 : this.header.hashCode())) * 31) + (this.loginUrl == null ? 0 : this.loginUrl.hashCode())) * 31) + (this.loginUi == null ? 0 : this.loginUi.hashCode())) * 31) + (this.loginCheckJs == null ? 0 : this.loginCheckJs.hashCode())) * 31) + (this.bookSourceComment == null ? 0 : this.bookSourceComment.hashCode())) * 31) + Long.hashCode(this.lastUpdateTime)) * 31) + Long.hashCode(this.respondTime)) * 31) + Integer.hashCode(this.weight)) * 31) + (this.exploreUrl == null ? 0 : this.exploreUrl.hashCode())) * 31) + (this.ruleExplore == null ? 0 : this.ruleExplore.hashCode())) * 31) + (this.searchUrl == null ? 0 : this.searchUrl.hashCode())) * 31) + (this.ruleSearch == null ? 0 : this.ruleSearch.hashCode())) * 31) + (this.ruleBookInfo == null ? 0 : this.ruleBookInfo.hashCode())) * 31) + (this.ruleToc == null ? 0 : this.ruleToc.hashCode())) * 31) + (this.ruleContent == null ? 0 : this.ruleContent.hashCode());
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof BookSourceAny)) {
                return false;
            }
            BookSourceAny bookSourceAny = (BookSourceAny) other;
            return Intrinsics.areEqual(this.bookSourceName, bookSourceAny.bookSourceName) && Intrinsics.areEqual(this.bookSourceGroup, bookSourceAny.bookSourceGroup) && Intrinsics.areEqual(this.bookSourceUrl, bookSourceAny.bookSourceUrl) && this.bookSourceType == bookSourceAny.bookSourceType && Intrinsics.areEqual(this.bookUrlPattern, bookSourceAny.bookUrlPattern) && this.customOrder == bookSourceAny.customOrder && this.enabled == bookSourceAny.enabled && this.enabledExplore == bookSourceAny.enabledExplore && this.enabledCookieJar == bookSourceAny.enabledCookieJar && Intrinsics.areEqual(this.concurrentRate, bookSourceAny.concurrentRate) && Intrinsics.areEqual(this.header, bookSourceAny.header) && Intrinsics.areEqual(this.loginUrl, bookSourceAny.loginUrl) && Intrinsics.areEqual(this.loginUi, bookSourceAny.loginUi) && Intrinsics.areEqual(this.loginCheckJs, bookSourceAny.loginCheckJs) && Intrinsics.areEqual(this.bookSourceComment, bookSourceAny.bookSourceComment) && this.lastUpdateTime == bookSourceAny.lastUpdateTime && this.respondTime == bookSourceAny.respondTime && this.weight == bookSourceAny.weight && Intrinsics.areEqual(this.exploreUrl, bookSourceAny.exploreUrl) && Intrinsics.areEqual(this.ruleExplore, bookSourceAny.ruleExplore) && Intrinsics.areEqual(this.searchUrl, bookSourceAny.searchUrl) && Intrinsics.areEqual(this.ruleSearch, bookSourceAny.ruleSearch) && Intrinsics.areEqual(this.ruleBookInfo, bookSourceAny.ruleBookInfo) && Intrinsics.areEqual(this.ruleToc, bookSourceAny.ruleToc) && Intrinsics.areEqual(this.ruleContent, bookSourceAny.ruleContent);
        }

        public BookSourceAny() {
            this(null, null, null, 0, null, 0, false, false, false, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 33554431, null);
        }

        public BookSourceAny(@NotNull String bookSourceName, @Nullable String bookSourceGroup, @NotNull String bookSourceUrl, int bookSourceType, @Nullable String bookUrlPattern, int customOrder, boolean enabled, boolean enabledExplore, boolean enabledCookieJar, @Nullable String concurrentRate, @Nullable String header, @Nullable Object loginUrl, @Nullable Object loginUi, @Nullable String loginCheckJs, @Nullable String bookSourceComment, long lastUpdateTime, long respondTime, int weight, @Nullable String exploreUrl, @Nullable Object ruleExplore, @Nullable String searchUrl, @Nullable Object ruleSearch, @Nullable Object ruleBookInfo, @Nullable Object ruleToc, @Nullable Object ruleContent) {
            Intrinsics.checkNotNullParameter(bookSourceName, "bookSourceName");
            Intrinsics.checkNotNullParameter(bookSourceUrl, "bookSourceUrl");
            this.bookSourceName = bookSourceName;
            this.bookSourceGroup = bookSourceGroup;
            this.bookSourceUrl = bookSourceUrl;
            this.bookSourceType = bookSourceType;
            this.bookUrlPattern = bookUrlPattern;
            this.customOrder = customOrder;
            this.enabled = enabled;
            this.enabledExplore = enabledExplore;
            this.enabledCookieJar = enabledCookieJar;
            this.concurrentRate = concurrentRate;
            this.header = header;
            this.loginUrl = loginUrl;
            this.loginUi = loginUi;
            this.loginCheckJs = loginCheckJs;
            this.bookSourceComment = bookSourceComment;
            this.lastUpdateTime = lastUpdateTime;
            this.respondTime = respondTime;
            this.weight = weight;
            this.exploreUrl = exploreUrl;
            this.ruleExplore = ruleExplore;
            this.searchUrl = searchUrl;
            this.ruleSearch = ruleSearch;
            this.ruleBookInfo = ruleBookInfo;
            this.ruleToc = ruleToc;
            this.ruleContent = ruleContent;
        }

        public /* synthetic */ BookSourceAny(String str, String str2, String str3, int i, String str4, int i2, boolean z, boolean z2, boolean z3, String str5, String str6, Object obj, Object obj2, String str7, String str8, long j, long j2, int i3, String str9, Object obj3, String str10, Object obj4, Object obj5, Object obj6, Object obj7, int i4, DefaultConstructorMarker defaultConstructorMarker) {
            this((i4 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i4 & 2) != 0 ? null : str2, (i4 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i4 & 8) != 0 ? 0 : i, (i4 & 16) != 0 ? null : str4, (i4 & 32) != 0 ? 0 : i2, (i4 & 64) != 0 ? true : z, (i4 & Wbxml.EXT_T_0) != 0 ? true : z2, (i4 & 256) != 0 ? false : z3, (i4 & 512) != 0 ? null : str5, (i4 & 1024) != 0 ? null : str6, (i4 & 2048) != 0 ? null : obj, (i4 & 4096) != 0 ? null : obj2, (i4 & IOUtil.DEFAULT_BUFFER_SIZE) != 0 ? null : str7, (i4 & 16384) != 0 ? PackageDocumentBase.PREFIX_OPF : str8, (i4 & 32768) != 0 ? 0L : j, (i4 & 65536) != 0 ? 180000L : j2, (i4 & 131072) != 0 ? 0 : i3, (i4 & 262144) != 0 ? null : str9, (i4 & 524288) != 0 ? null : obj3, (i4 & 1048576) != 0 ? null : str10, (i4 & 2097152) != 0 ? null : obj4, (i4 & 4194304) != 0 ? null : obj5, (i4 & 8388608) != 0 ? null : obj6, (i4 & 16777216) != 0 ? null : obj7);
        }

        @NotNull
        public final String getBookSourceName() {
            return this.bookSourceName;
        }

        public final void setBookSourceName(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.bookSourceName = str;
        }

        @Nullable
        public final String getBookSourceGroup() {
            return this.bookSourceGroup;
        }

        public final void setBookSourceGroup(@Nullable String str) {
            this.bookSourceGroup = str;
        }

        @NotNull
        public final String getBookSourceUrl() {
            return this.bookSourceUrl;
        }

        public final void setBookSourceUrl(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.bookSourceUrl = str;
        }

        public final int getBookSourceType() {
            return this.bookSourceType;
        }

        public final void setBookSourceType(int i) {
            this.bookSourceType = i;
        }

        @Nullable
        public final String getBookUrlPattern() {
            return this.bookUrlPattern;
        }

        public final void setBookUrlPattern(@Nullable String str) {
            this.bookUrlPattern = str;
        }

        public final int getCustomOrder() {
            return this.customOrder;
        }

        public final void setCustomOrder(int i) {
            this.customOrder = i;
        }

        public final boolean getEnabled() {
            return this.enabled;
        }

        public final void setEnabled(boolean z) {
            this.enabled = z;
        }

        public final boolean getEnabledExplore() {
            return this.enabledExplore;
        }

        public final void setEnabledExplore(boolean z) {
            this.enabledExplore = z;
        }

        public final boolean getEnabledCookieJar() {
            return this.enabledCookieJar;
        }

        public final void setEnabledCookieJar(boolean z) {
            this.enabledCookieJar = z;
        }

        @Nullable
        public final String getConcurrentRate() {
            return this.concurrentRate;
        }

        public final void setConcurrentRate(@Nullable String str) {
            this.concurrentRate = str;
        }

        @Nullable
        public final String getHeader() {
            return this.header;
        }

        public final void setHeader(@Nullable String str) {
            this.header = str;
        }

        @Nullable
        public final Object getLoginUrl() {
            return this.loginUrl;
        }

        public final void setLoginUrl(@Nullable Object obj) {
            this.loginUrl = obj;
        }

        @Nullable
        public final Object getLoginUi() {
            return this.loginUi;
        }

        public final void setLoginUi(@Nullable Object obj) {
            this.loginUi = obj;
        }

        @Nullable
        public final String getLoginCheckJs() {
            return this.loginCheckJs;
        }

        public final void setLoginCheckJs(@Nullable String str) {
            this.loginCheckJs = str;
        }

        @Nullable
        public final String getBookSourceComment() {
            return this.bookSourceComment;
        }

        public final void setBookSourceComment(@Nullable String str) {
            this.bookSourceComment = str;
        }

        public final long getLastUpdateTime() {
            return this.lastUpdateTime;
        }

        public final void setLastUpdateTime(long j) {
            this.lastUpdateTime = j;
        }

        public final long getRespondTime() {
            return this.respondTime;
        }

        public final void setRespondTime(long j) {
            this.respondTime = j;
        }

        public final int getWeight() {
            return this.weight;
        }

        public final void setWeight(int i) {
            this.weight = i;
        }

        @Nullable
        public final String getExploreUrl() {
            return this.exploreUrl;
        }

        public final void setExploreUrl(@Nullable String str) {
            this.exploreUrl = str;
        }

        @Nullable
        public final Object getRuleExplore() {
            return this.ruleExplore;
        }

        public final void setRuleExplore(@Nullable Object obj) {
            this.ruleExplore = obj;
        }

        @Nullable
        public final String getSearchUrl() {
            return this.searchUrl;
        }

        public final void setSearchUrl(@Nullable String str) {
            this.searchUrl = str;
        }

        @Nullable
        public final Object getRuleSearch() {
            return this.ruleSearch;
        }

        public final void setRuleSearch(@Nullable Object obj) {
            this.ruleSearch = obj;
        }

        @Nullable
        public final Object getRuleBookInfo() {
            return this.ruleBookInfo;
        }

        public final void setRuleBookInfo(@Nullable Object obj) {
            this.ruleBookInfo = obj;
        }

        @Nullable
        public final Object getRuleToc() {
            return this.ruleToc;
        }

        public final void setRuleToc(@Nullable Object obj) {
            this.ruleToc = obj;
        }

        @Nullable
        public final Object getRuleContent() {
            return this.ruleContent;
        }

        public final void setRuleContent(@Nullable Object obj) {
            this.ruleContent = obj;
        }
    }

    private final String toNewRule(String oldRule) {
        String str = oldRule;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        String newRule = oldRule;
        boolean reverse = false;
        boolean allinone = false;
        if (StringsKt.startsWith$default(oldRule, "-", false, 2, (Object) null)) {
            reverse = true;
            if (oldRule == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring = oldRule.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            newRule = strSubstring;
        }
        if (StringsKt.startsWith$default(newRule, "+", false, 2, (Object) null)) {
            allinone = true;
            String str2 = newRule;
            if (str2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring2 = str2.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
            newRule = strSubstring2;
        }
        if (!StringsKt.startsWith(newRule, "@CSS:", true) && !StringsKt.startsWith(newRule, "@XPath:", true) && !StringsKt.startsWith$default(newRule, "//", false, 2, (Object) null) && !StringsKt.startsWith$default(newRule, "##", false, 2, (Object) null) && !StringsKt.startsWith$default(newRule, ":", false, 2, (Object) null) && !StringsKt.contains(newRule, "@js:", true) && !StringsKt.contains(newRule, "<js>", true)) {
            if (StringsKt.contains$default(newRule, "#", false, 2, (Object) null) && !StringsKt.contains$default(newRule, "##", false, 2, (Object) null)) {
                newRule = StringsKt.replace$default(oldRule, "#", "##", false, 4, (Object) null);
            }
            if (StringsKt.contains$default(newRule, "|", false, 2, (Object) null) && !StringsKt.contains$default(newRule, "||", false, 2, (Object) null)) {
                if (StringsKt.contains$default(newRule, "##", false, 2, (Object) null)) {
                    List list = StringsKt.split$default(newRule, new String[]{"##"}, false, 0, 6, (Object) null);
                    if (StringsKt.contains$default((CharSequence) list.get(0), "|", false, 2, (Object) null)) {
                        newRule = StringsKt.replace$default((String) list.get(0), "|", "||", false, 4, (Object) null);
                        int i = 1;
                        int size = list.size();
                        if (1 < size) {
                            do {
                                int i2 = i;
                                i++;
                                newRule = ((Object) newRule) + "##" + ((String) list.get(i2));
                            } while (i < size);
                        }
                    }
                } else {
                    newRule = StringsKt.replace$default(newRule, "|", "||", false, 4, (Object) null);
                }
            }
            if (StringsKt.contains$default(newRule, "&", false, 2, (Object) null) && !StringsKt.contains$default(newRule, "&&", false, 2, (Object) null) && !StringsKt.contains$default(newRule, "http", false, 2, (Object) null) && !StringsKt.startsWith$default(newRule, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
                newRule = StringsKt.replace$default(newRule, "&", "&&", false, 4, (Object) null);
            }
        }
        if (allinone) {
            newRule = Intrinsics.stringPlus("+", newRule);
        }
        if (reverse) {
            newRule = Intrinsics.stringPlus("-", newRule);
        }
        return newRule;
    }

    private final String toNewUrls(String oldUrls) {
        String str = oldUrls;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        if (StringsKt.startsWith$default(oldUrls, "@js:", false, 2, (Object) null) || StringsKt.startsWith$default(oldUrls, "<js>", false, 2, (Object) null)) {
            return oldUrls;
        }
        if (!StringsKt.contains$default(oldUrls, "\n", false, 2, (Object) null) && !StringsKt.contains$default(oldUrls, "&&", false, 2, (Object) null)) {
            return toNewUrl(oldUrls);
        }
        Iterable urls = new Regex("(&&|\r?\n)+").split(oldUrls, 0);
        Iterable $this$map$iv = urls;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            String it = (String) item$iv$iv;
            String newUrl = INSTANCE.toNewUrl(it);
            destination$iv$iv.add(newUrl == null ? null : new Regex("\n\\s*").replace(newUrl, PackageDocumentBase.PREFIX_OPF));
        }
        return CollectionsKt.joinToString$default((List) destination$iv$iv, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }

    private final String toNewUrl(String oldUrl) {
        String str = oldUrl;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        String url = oldUrl;
        if (StringsKt.startsWith(oldUrl, "<js>", true)) {
            return StringsKt.replace$default(StringsKt.replace$default(url, "=searchKey", "={{key}}", false, 4, (Object) null), "=searchPage", "={{page}}", false, 4, (Object) null);
        }
        HashMap map = new HashMap();
        Matcher mather = headerPattern.matcher(url);
        if (mather.find()) {
            String header = mather.group();
            Intrinsics.checkNotNullExpressionValue(header, "header");
            url = StringsKt.replace$default(url, header, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
            HashMap map2 = map;
            String strSubstring = header.substring(8);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            map2.put("headers", strSubstring);
        }
        List urlList = StringsKt.split$default(url, new String[]{"|"}, false, 0, 6, (Object) null);
        String url2 = (String) urlList.get(0);
        if (urlList.size() > 1) {
            map.put("charset", StringsKt.split$default((CharSequence) urlList.get(1), new String[]{"="}, false, 0, 6, (Object) null).get(1));
        }
        Matcher mather2 = jsPattern.matcher(url2);
        ArrayList<String> jsList = new ArrayList();
        while (mather2.find()) {
            jsList.add(mather2.group());
            url2 = StringsKt.replace$default(url2, (String) CollectionsKt.last(jsList), Intrinsics.stringPlus("$", Integer.valueOf(jsList.size() - 1)), false, 4, (Object) null);
        }
        String url3 = StringsKt.replace$default(new Regex("searchPage([-+]1)").replace(new Regex("<searchPage([-+]1)>").replace(StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(url2, "{", "<", false, 4, (Object) null), "}", ">", false, 4, (Object) null), "searchKey", "{{key}}", false, 4, (Object) null), "{{page$1}}"), "{{page$1}}"), "searchPage", "{{page}}", false, 4, (Object) null);
        int i = 0;
        for (String item : jsList) {
            int index = i;
            i++;
            url3 = StringsKt.replace$default(url3, Intrinsics.stringPlus("$", Integer.valueOf(index)), StringsKt.replace$default(StringsKt.replace$default(item, "searchKey", "key", false, 4, (Object) null), "searchPage", "page", false, 4, (Object) null), false, 4, (Object) null);
        }
        List urlList2 = StringsKt.split$default(url3, new String[]{"@"}, false, 0, 6, (Object) null);
        String url4 = (String) urlList2.get(0);
        if (urlList2.size() > 1) {
            map.put("method", "POST");
            map.put(NCXDocumentV3.XHTMLTgs.body, urlList2.get(1));
        }
        if (map.size() > 0) {
            url4 = url4 + ',' + ((Object) GsonExtensionsKt.getGSON().toJson(map));
        }
        return url4;
    }

    private final String uaToHeader(String ua) {
        String str = ua;
        if (str == null || str.length() == 0) {
            return null;
        }
        Map map = MapsKt.mapOf(new Pair(AppConst.UA_NAME, ua));
        return GsonExtensionsKt.getGSON().toJson(map);
    }
}
