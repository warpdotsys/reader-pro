// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import java.util.Arrays;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.collections.CollectionsKt;
import java.io.File;
import org.jsoup.Connection$Response;
import io.legado.app.help.http.StrResponse;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookSource;
import io.legado.app.utils.StringUtils;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import com.script.Bindings;
import io.legado.app.constant.AppConst;
import io.legado.app.help.CacheManager;
import io.legado.app.help.http.CookieStore;
import com.script.SimpleBindings;
import kotlin.Unit;
import java.util.regex.Matcher;
import kotlin.text.Regex;
import java.util.HashMap;
import kotlin.Result$Companion;
import kotlin.ResultKt;
import org.jsoup.nodes.Entities;
import kotlin.Result;
import io.legado.app.utils.TextUtils;
import java.util.Iterator;
import io.legado.app.utils.NetworkUtils;
import kotlin.text.StringsKt;
import java.util.ArrayList;
import java.util.Map;
import org.mozilla.javascript.NativeObject;
import java.util.Collection;
import java.util.List;
import kotlin.jvm.JvmOverloads;
import io.legado.app.utils.StringExtensionsKt;
import io.legado.app.data.entities.BaseBook;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import java.util.regex.Pattern;
import java.net.URL;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.model.DebugLog;
import org.jetbrains.annotations.Nullable;
import io.legado.app.data.entities.BaseSource;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import io.legado.app.help.JsExtensions;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 g2\u00020\u0001:\u0003ghiB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007?\u0006\u0002\u0010\bJ\u0012\u00108\u001a\u0004\u0018\u00010\u00102\u0006\u00109\u001a\u00020\u0010H\u0016J\u001c\u0010:\u001a\u0004\u0018\u00010\u001e2\u0006\u0010;\u001a\u00020\u00102\n\b\u0002\u0010<\u001a\u0004\u0018\u00010\u001eJ\u000e\u0010=\u001a\u00020\u00102\u0006\u0010>\u001a\u00020\u0010J\u0010\u0010?\u001a\u00020\n2\u0006\u0010@\u001a\u00020\u001eH\u0002J\u0010\u0010A\u001a\u00020\f2\u0006\u0010@\u001a\u00020\u001eH\u0002J\u0010\u0010B\u001a\u00020\u000e2\u0006\u0010@\u001a\u00020\u001eH\u0002J\u0010\u0010C\u001a\u0004\u0018\u00010\u001e2\u0006\u0010D\u001a\u00020\u0010J\u0014\u0010E\u001a\b\u0012\u0004\u0012\u00020\u001e0F2\u0006\u0010D\u001a\u00020\u0010J\n\u0010G\u001a\u0004\u0018\u00010\u0007H\u0016J\n\u0010H\u001a\u0004\u0018\u00010\u0005H\u0016J(\u0010I\u001a\u00020\u00102\b\u0010D\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010J\u001a\u0004\u0018\u00010\u001e2\b\b\u0002\u0010K\u001a\u00020'H\u0007J0\u0010I\u001a\u00020\u00102\u0010\u0010L\u001a\f\u0012\b\u0012\u00060MR\u00020\u00000F2\n\b\u0002\u0010J\u001a\u0004\u0018\u00010\u001e2\b\b\u0002\u0010K\u001a\u00020'H\u0007J0\u0010N\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010F2\b\u0010O\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010J\u001a\u0004\u0018\u00010\u001e2\b\b\u0002\u0010K\u001a\u00020'H\u0007J8\u0010N\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010F2\u0010\u0010L\u001a\f\u0012\b\u0012\u00060MR\u00020\u00000F2\n\b\u0002\u0010J\u001a\u0004\u0018\u00010\u001e2\b\b\u0002\u0010K\u001a\u00020'H\u0007J\b\u0010P\u001a\u00020\u0010H\u0016J\u0016\u0010Q\u001a\u00020\u00102\u0006\u0010>\u001a\u00020\u00102\u0006\u0010R\u001a\u00020\u0010J\u001c\u0010S\u001a\u00020T2\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00100VH\u0002J\u0006\u0010W\u001a\u00020TJ\u0006\u0010X\u001a\u00020TJ\u0006\u0010Y\u001a\u00020TJ\u001c\u0010Z\u001a\u00020\u00102\u0006\u0010<\u001a\u00020\u00102\n\u0010O\u001a\u00060MR\u00020\u0000H\u0002J\u0010\u0010[\u001a\u00020\u00002\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010J\u001e\u0010\\\u001a\u00020\u00002\b\u0010\u001f\u001a\u0004\u0018\u00010\u001e2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0007J\u0010\u0010]\u001a\u0004\u0018\u0001002\u0006\u0010^\u001a\u00020\u0010J4\u0010_\u001a\u00020\u00102\u0006\u0010D\u001a\u00020\u00102\"\u0010`\u001a\u001e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00100aj\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0010`bH\u0002J$\u0010c\u001a\f\u0012\b\u0012\u00060MR\u00020\u00000F2\b\u0010D\u001a\u0004\u0018\u00010\u00102\b\b\u0002\u0010d\u001a\u00020'J\u0012\u0010e\u001a\u0004\u0018\u00010\u00102\b\u0010f\u001a\u0004\u0018\u00010\u0010R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e?\u0006\u0002\n\u0000R\"\u0010\u0011\u001a\u0004\u0018\u00010\u00102\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u00158F?\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\"\u0010\u001f\u001a\u0004\u0018\u00010\u001e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u001e@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u000e\u0010&\u001a\u00020'X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020'X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010)\u001a\u0004\u0018\u00010\u0010X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0013\"\u0004\b+\u0010,R\u000e\u0010-\u001a\u00020'X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020'X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020'X\u0082\u000e?\u0006\u0002\n\u0000R\"\u00101\u001a\u0004\u0018\u0001002\b\u0010\u000f\u001a\u0004\u0018\u000100@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004?\u0006\u0002\n\u0000：\u0006j" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeRule;", "Lio/legado/app/help/JsExtensions;", "ruleData", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "source", "Lio/legado/app/data/entities/BaseSource;", "debugLog", "Lio/legado/app/model/DebugLog;", "(Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/DebugLog;)V", "analyzeByJSonPath", "Lio/legado/app/model/analyzeRule/AnalyzeByJSonPath;", "analyzeByJSoup", "Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;", "analyzeByXPath", "Lio/legado/app/model/analyzeRule/AnalyzeByXPath;", "<set-?>", "", "baseUrl", "getBaseUrl", "()Ljava/lang/String;", "book", "Lio/legado/app/data/entities/BaseBook;", "getBook", "()Lio/legado/app/data/entities/BaseBook;", "chapter", "Lio/legado/app/data/entities/BookChapter;", "getChapter", "()Lio/legado/app/data/entities/BookChapter;", "setChapter", "(Lio/legado/app/data/entities/BookChapter;)V", "", "content", "getContent", "()Ljava/lang/Object;", "getDebugLog", "()Lio/legado/app/model/DebugLog;", "setDebugLog", "(Lio/legado/app/model/DebugLog;)V", "isJSON", "", "isRegex", "nextChapterUrl", "getNextChapterUrl", "setNextChapterUrl", "(Ljava/lang/String;)V", "objectChangedJP", "objectChangedJS", "objectChangedXP", "Ljava/net/URL;", "redirectUrl", "getRedirectUrl", "()Ljava/net/URL;", "getRuleData", "()Lio/legado/app/model/analyzeRule/RuleDataInterface;", "setRuleData", "(Lio/legado/app/model/analyzeRule/RuleDataInterface;)V", "ajax", "urlStr", "evalJS", "jsStr", "result", "get", "key", "getAnalyzeByJSonPath", "o", "getAnalyzeByJSoup", "getAnalyzeByXPath", "getElement", "ruleStr", "getElements", "", "getLogger", "getSource", "getString", "mContent", "isUrl", "ruleList", "Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;", "getStringList", "rule", "getUserNameSpace", "put", "value", "putRule", "", "map", "", "reGetBook", "refreshBookUrl", "refreshTocUrl", "replaceRegex", "setBaseUrl", "setContent", "setRedirectUrl", "url", "splitPutRule", "putMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "splitSourceRule", "allInOne", "toNumChapter", "s", "Companion", "Mode", "SourceRule", "reader-pro" })
public final class AnalyzeRule implements JsExtensions
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private RuleDataInterface ruleData;
    @Nullable
    private final BaseSource source;
    @Nullable
    private DebugLog debugLog;
    @Nullable
    private BookChapter chapter;
    @Nullable
    private String nextChapterUrl;
    @Nullable
    private Object content;
    @Nullable
    private String baseUrl;
    @Nullable
    private URL redirectUrl;
    private boolean isJSON;
    private boolean isRegex;
    @Nullable
    private AnalyzeByXPath analyzeByXPath;
    @Nullable
    private AnalyzeByJSoup analyzeByJSoup;
    @Nullable
    private AnalyzeByJSonPath analyzeByJSonPath;
    private boolean objectChangedXP;
    private boolean objectChangedJS;
    private boolean objectChangedJP;
    private static final Pattern putPattern;
    private static final Pattern evalPattern;
    private static final Pattern regexPattern;
    private static final Pattern titleNumPattern;
    
    public AnalyzeRule(@NotNull final RuleDataInterface ruleData, @Nullable final BaseSource source, @Nullable final DebugLog debugLog) {
        Intrinsics.checkNotNullParameter((Object)ruleData, "ruleData");
        this.ruleData = ruleData;
        this.source = source;
        this.debugLog = debugLog;
    }
    
    @NotNull
    public final RuleDataInterface getRuleData() {
        return this.ruleData;
    }
    
    public final void setRuleData(@NotNull final RuleDataInterface <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.ruleData = <set-?>;
    }
    
    @Nullable
    public final DebugLog getDebugLog() {
        return this.debugLog;
    }
    
    public final void setDebugLog(@Nullable final DebugLog <set-?>) {
        this.debugLog = <set-?>;
    }
    
    @Nullable
    public final BaseBook getBook() {
        final RuleDataInterface ruleData = this.ruleData;
        return (ruleData instanceof BaseBook) ? ((BaseBook)ruleData) : null;
    }
    
    @Nullable
    public final BookChapter getChapter() {
        return this.chapter;
    }
    
    public final void setChapter(@Nullable final BookChapter <set-?>) {
        this.chapter = <set-?>;
    }
    
    @Nullable
    public final String getNextChapterUrl() {
        return this.nextChapterUrl;
    }
    
    public final void setNextChapterUrl(@Nullable final String <set-?>) {
        this.nextChapterUrl = <set-?>;
    }
    
    @Nullable
    public final Object getContent() {
        return this.content;
    }
    
    @Nullable
    public final String getBaseUrl() {
        return this.baseUrl;
    }
    
    @Nullable
    public final URL getRedirectUrl() {
        return this.redirectUrl;
    }
    
    @NotNull
    @Override
    public String getUserNameSpace() {
        return this.ruleData.getUserNameSpace();
    }
    
    @Nullable
    @Override
    public BaseSource getSource() {
        return this.source;
    }
    
    @Nullable
    @Override
    public DebugLog getLogger() {
        return this.debugLog;
    }
    
    @JvmOverloads
    @NotNull
    public final AnalyzeRule setContent(@Nullable final Object content, @Nullable final String baseUrl) {
        if (content == null) {
            throw new AssertionError((Object)"\u5185\u5bb9\u4e0d\u53ef\u7a7a\uff08Content cannot be null\uff09");
        }
        this.content = content;
        this.isJSON = StringExtensionsKt.isJson(content.toString());
        this.setBaseUrl(baseUrl);
        this.objectChangedXP = true;
        this.objectChangedJS = true;
        this.objectChangedJP = true;
        return this;
    }
    
    public static /* synthetic */ AnalyzeRule setContent$default(final AnalyzeRule analyzeRule, final Object content, String baseUrl, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            baseUrl = null;
        }
        return analyzeRule.setContent(content, baseUrl);
    }
    
    @NotNull
    public final AnalyzeRule setBaseUrl(@Nullable final String baseUrl) {
        if (baseUrl != null) {
            final String it = baseUrl;
            final int n = 0;
            this.baseUrl = baseUrl;
        }
        return this;
    }
    
    @Nullable
    public final URL setRedirectUrl(@NotNull final String url) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        try {
            this.redirectUrl = new URL(url);
        }
        catch (final Exception e) {
            this.log("URL(" + url + ") error\n" + (Object)e.getLocalizedMessage());
        }
        return this.redirectUrl;
    }
    
    private final AnalyzeByXPath getAnalyzeByXPath(final Object o) {
        AnalyzeByXPath analyzeByXPath;
        if (!Intrinsics.areEqual(o, this.content)) {
            analyzeByXPath = new AnalyzeByXPath(o);
        }
        else {
            if (this.analyzeByXPath == null || this.objectChangedXP) {
                final Object content = this.content;
                Intrinsics.checkNotNull(content);
                this.analyzeByXPath = new AnalyzeByXPath(content);
                this.objectChangedXP = false;
            }
            Intrinsics.checkNotNull((Object)(analyzeByXPath = this.analyzeByXPath));
        }
        return analyzeByXPath;
    }
    
    private final AnalyzeByJSoup getAnalyzeByJSoup(final Object o) {
        AnalyzeByJSoup analyzeByJSoup;
        if (!Intrinsics.areEqual(o, this.content)) {
            analyzeByJSoup = new AnalyzeByJSoup(o);
        }
        else {
            if (this.analyzeByJSoup == null || this.objectChangedJS) {
                final Object content = this.content;
                Intrinsics.checkNotNull(content);
                this.analyzeByJSoup = new AnalyzeByJSoup(content);
                this.objectChangedJS = false;
            }
            Intrinsics.checkNotNull((Object)(analyzeByJSoup = this.analyzeByJSoup));
        }
        return analyzeByJSoup;
    }
    
    private final AnalyzeByJSonPath getAnalyzeByJSonPath(final Object o) {
        AnalyzeByJSonPath analyzeByJSonPath;
        if (!Intrinsics.areEqual(o, this.content)) {
            analyzeByJSonPath = new AnalyzeByJSonPath(o);
        }
        else {
            if (this.analyzeByJSonPath == null || this.objectChangedJP) {
                final Object content = this.content;
                Intrinsics.checkNotNull(content);
                this.analyzeByJSonPath = new AnalyzeByJSonPath(content);
                this.objectChangedJP = false;
            }
            Intrinsics.checkNotNull((Object)(analyzeByJSonPath = this.analyzeByJSonPath));
        }
        return analyzeByJSonPath;
    }
    
    @JvmOverloads
    @Nullable
    public final List<String> getStringList(@Nullable final String rule, @Nullable final Object mContent, final boolean isUrl) {
        final CharSequence charSequence = rule;
        if (charSequence == null || charSequence.length() == 0) {
            return null;
        }
        final List ruleList = this.splitSourceRule(rule, false);
        return this.getStringList(ruleList, mContent, isUrl);
    }
    
    public static /* synthetic */ List getStringList$default(final AnalyzeRule analyzeRule, final String rule, Object mContent, boolean isUrl, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            mContent = null;
        }
        if ((n & 0x4) != 0x0) {
            isUrl = false;
        }
        return analyzeRule.getStringList(rule, mContent, isUrl);
    }
    
    @JvmOverloads
    @Nullable
    public final List<String> getStringList(@NotNull final List<SourceRule> ruleList, @Nullable final Object mContent, final boolean isUrl) {
        Intrinsics.checkNotNullParameter((Object)ruleList, "ruleList");
        Object result = null;
        final Object content = (mContent == null) ? this.content : mContent;
        if (content != null && !ruleList.isEmpty()) {
            result = content;
            if (content instanceof NativeObject) {
                final Object value = ((NativeObject)content).get((Object)ruleList.get(0).getRule$reader_pro());
                result = ((value == null) ? null : value.toString());
            }
            else {
                for (final SourceRule sourceRule : ruleList) {
                    this.putRule(sourceRule.getPutMap$reader_pro());
                    sourceRule.makeUpRule(result);
                    final Object o = result;
                    if (o == null) {
                        continue;
                    }
                    final Object it = o;
                    final int n = 0;
                    if (sourceRule.getRule$reader_pro().length() > 0) {
                        result = switch (WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                            case 1 -> this.evalJS(sourceRule.getRule$reader_pro(), result);
                            case 2 -> this.getAnalyzeByJSonPath(it).getStringList$reader_pro(sourceRule.getRule$reader_pro());
                            case 3 -> this.getAnalyzeByXPath(it).getStringList$reader_pro(sourceRule.getRule$reader_pro());
                            case 4 -> this.getAnalyzeByJSoup(it).getStringList$reader_pro(sourceRule.getRule$reader_pro());
                            default -> sourceRule.getRule$reader_pro();
                        };
                    }
                    if (sourceRule.getReplaceRegex$reader_pro().length() > 0 && result instanceof List) {
                        final ArrayList newList = new ArrayList();
                        for (final Object item : (List)result) {
                            newList.add(this.replaceRegex(String.valueOf(item), sourceRule));
                        }
                        result = newList;
                    }
                    else {
                        if (sourceRule.getReplaceRegex$reader_pro().length() <= 0) {
                            continue;
                        }
                        result = this.replaceRegex(String.valueOf(result), sourceRule);
                    }
                }
            }
        }
        if (result == null) {
            return null;
        }
        if (result instanceof String) {
            result = StringsKt.split$default((CharSequence)result, new String[] { "\n" }, false, 0, 6, (Object)null);
        }
        if (isUrl) {
            final ArrayList urlList = new ArrayList();
            if (result instanceof List) {
                for (final Object url : (List)result) {
                    final String absoluteURL = NetworkUtils.INSTANCE.getAbsoluteURL(this.redirectUrl, String.valueOf(url));
                    if (absoluteURL.length() > 0 && !urlList.contains(absoluteURL)) {
                        urlList.add(absoluteURL);
                    }
                }
            }
            return urlList;
        }
        final Object o3 = result;
        return (o3 instanceof List) ? ((List<String>)o3) : null;
    }
    
    public static /* synthetic */ List getStringList$default(final AnalyzeRule analyzeRule, final List ruleList, Object mContent, boolean isUrl, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            mContent = null;
        }
        if ((n & 0x4) != 0x0) {
            isUrl = false;
        }
        return analyzeRule.getStringList(ruleList, mContent, isUrl);
    }
    
    @JvmOverloads
    @NotNull
    public final String getString(@Nullable final String ruleStr, @Nullable final Object mContent, final boolean isUrl) {
        if (TextUtils.isEmpty(ruleStr)) {
            return "";
        }
        final List ruleList = splitSourceRule$default(this, ruleStr, false, 2, null);
        return this.getString(ruleList, mContent, isUrl);
    }
    
    public static /* synthetic */ String getString$default(final AnalyzeRule analyzeRule, final String ruleStr, Object mContent, boolean isUrl, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            mContent = null;
        }
        if ((n & 0x4) != 0x0) {
            isUrl = false;
        }
        return analyzeRule.getString(ruleStr, mContent, isUrl);
    }
    
    @JvmOverloads
    @NotNull
    public final String getString(@NotNull final List<SourceRule> ruleList, @Nullable final Object mContent, final boolean isUrl) {
        Intrinsics.checkNotNullParameter((Object)ruleList, "ruleList");
        Object result = null;
        final Object content = (mContent == null) ? this.content : mContent;
        if (content != null && !ruleList.isEmpty()) {
            result = content;
            if (result instanceof NativeObject) {
                final Object value = ((NativeObject)result).get((Object)ruleList.get(0).getRule$reader_pro());
                result = ((value == null) ? null : value.toString());
            }
            else {
                for (final SourceRule sourceRule : ruleList) {
                    this.putRule(sourceRule.getPutMap$reader_pro());
                    sourceRule.makeUpRule(result);
                    final Object o = result;
                    if (o == null) {
                        continue;
                    }
                    final Object it = o;
                    final int n = 0;
                    if (!StringsKt.isBlank((CharSequence)sourceRule.getRule$reader_pro()) || sourceRule.getReplaceRegex$reader_pro().length() == 0) {
                        result = switch (WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                            case 1 -> this.evalJS(sourceRule.getRule$reader_pro(), it);
                            case 2 -> this.getAnalyzeByJSonPath(it).getString(sourceRule.getRule$reader_pro());
                            case 3 -> this.getAnalyzeByXPath(it).getString(sourceRule.getRule$reader_pro());
                            case 4 -> isUrl ? this.getAnalyzeByJSoup(it).getString0$reader_pro(sourceRule.getRule$reader_pro()) : this.getAnalyzeByJSoup(it).getString$reader_pro(sourceRule.getRule$reader_pro());
                            default -> sourceRule.getRule$reader_pro();
                        };
                    }
                    if (result == null || sourceRule.getReplaceRegex$reader_pro().length() <= 0) {
                        continue;
                    }
                    result = this.replaceRegex(String.valueOf(result), sourceRule);
                }
            }
        }
        if (result == null) {
            result = "";
        }
        Object o3;
        try {
            final Result$Companion companion = Result.Companion;
            final int n2 = 0;
            o3 = Result.constructor-impl((Object)Entities.unescape(String.valueOf(result)));
        }
        catch (final Throwable t) {
            final Result$Companion companion2 = Result.Companion;
            o3 = Result.constructor-impl(ResultKt.createFailure(t));
        }
        final String s = (String)o3;
        final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl((Object)s);
        if (exceptionOrNull-impl != null) {
            final Throwable it2 = exceptionOrNull-impl;
            final int n3 = 0;
            this.log(Intrinsics.stringPlus("Entities.unescape() error\n", (Object)it2.getLocalizedMessage()));
        }
        final String s2 = s;
        final Throwable exceptionOrNull-impl2 = Result.exceptionOrNull-impl((Object)s2);
        String value2;
        if (exceptionOrNull-impl2 == null) {
            value2 = s2;
        }
        else {
            final Throwable it3 = exceptionOrNull-impl2;
            final int n4 = 0;
            value2 = String.valueOf(result);
        }
        final String str = value2;
        if (isUrl) {
            Intrinsics.checkNotNullExpressionValue((Object)str, "str");
            String absoluteURL;
            if (StringsKt.isBlank((CharSequence)str)) {
                final String baseUrl = this.baseUrl;
                absoluteURL = ((baseUrl == null) ? "" : baseUrl);
            }
            else {
                absoluteURL = NetworkUtils.INSTANCE.getAbsoluteURL(this.redirectUrl, str);
            }
            return absoluteURL;
        }
        Intrinsics.checkNotNullExpressionValue((Object)str, "str");
        return str;
    }
    
    public static /* synthetic */ String getString$default(final AnalyzeRule analyzeRule, final List ruleList, Object mContent, boolean isUrl, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            mContent = null;
        }
        if ((n & 0x4) != 0x0) {
            isUrl = false;
        }
        return analyzeRule.getString(ruleList, mContent, isUrl);
    }
    
    @Nullable
    public final Object getElement(@NotNull final String ruleStr) {
        Intrinsics.checkNotNullParameter((Object)ruleStr, "ruleStr");
        if (TextUtils.isEmpty(ruleStr)) {
            return null;
        }
        Object result = null;
        final Object content = this.content;
        final List ruleList = this.splitSourceRule(ruleStr, true);
        if (content != null && !ruleList.isEmpty()) {
            result = content;
            for (final SourceRule sourceRule : ruleList) {
                this.putRule(sourceRule.getPutMap$reader_pro());
                sourceRule.makeUpRule(result);
                final Object o = result;
                if (o == null) {
                    continue;
                }
                final Object it = o;
                final int n = 0;
                result = switch (WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                    case 5 -> AnalyzeByRegex.getElement$default(AnalyzeByRegex.INSTANCE, String.valueOf(result), StringExtensionsKt.splitNotBlank(sourceRule.getRule$reader_pro(), "&&"), 0, 4, null);
                    case 1 -> this.evalJS(sourceRule.getRule$reader_pro(), it);
                    case 2 -> this.getAnalyzeByJSonPath(it).getObject$reader_pro(sourceRule.getRule$reader_pro());
                    case 3 -> this.getAnalyzeByXPath(it).getElements$reader_pro(sourceRule.getRule$reader_pro());
                    default -> this.getAnalyzeByJSoup(it).getElements$reader_pro(sourceRule.getRule$reader_pro());
                };
                if (sourceRule.getReplaceRegex$reader_pro().length() <= 0) {
                    continue;
                }
                result = this.replaceRegex(String.valueOf(result), sourceRule);
            }
        }
        return result;
    }
    
    @NotNull
    public final List<Object> getElements(@NotNull final String ruleStr) {
        Intrinsics.checkNotNullParameter((Object)ruleStr, "ruleStr");
        Object result = null;
        final Object content = this.content;
        final List ruleList = this.splitSourceRule(ruleStr, true);
        if (content != null && !ruleList.isEmpty()) {
            result = content;
            for (final SourceRule sourceRule : ruleList) {
                this.putRule(sourceRule.getPutMap$reader_pro());
                final Object o = result;
                if (o == null) {
                    continue;
                }
                final Object it = o;
                final int n = 0;
                result = switch (WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                    case 5 -> AnalyzeByRegex.getElements$default(AnalyzeByRegex.INSTANCE, String.valueOf(result), StringExtensionsKt.splitNotBlank(sourceRule.getRule$reader_pro(), "&&"), 0, 4, null);
                    case 1 -> this.evalJS(sourceRule.getRule$reader_pro(), result);
                    case 2 -> this.getAnalyzeByJSonPath(it).getList$reader_pro(sourceRule.getRule$reader_pro());
                    case 3 -> this.getAnalyzeByXPath(it).getElements$reader_pro(sourceRule.getRule$reader_pro());
                    default -> this.getAnalyzeByJSoup(it).getElements$reader_pro(sourceRule.getRule$reader_pro());
                };
                if (sourceRule.getReplaceRegex$reader_pro().length() <= 0) {
                    continue;
                }
                result = this.replaceRegex(String.valueOf(result), sourceRule);
            }
        }
        final Object o3 = result;
        if (o3 == null) {
            return new ArrayList<Object>();
        }
        final Object it2 = o3;
        final int n2 = 0;
        return (List)it2;
    }
    
    private final void putRule(final Map<String, String> map) {
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            this.put(key, getString$default(this, value, null, false, 6, null));
        }
    }
    
    private final String splitPutRule(final String ruleStr, final HashMap<String, String> putMap) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_3        /* vRuleStr */
        //     2: getstatic       io/legado/app/model/analyzeRule/AnalyzeRule.putPattern:Ljava/util/regex/Pattern;
        //     5: aload_3         /* vRuleStr */
        //     6: checkcast       Ljava/lang/CharSequence;
        //     9: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //    12: astore          putMatcher
        //    14: aload           putMatcher
        //    16: invokevirtual   java/util/regex/Matcher.find:()Z
        //    19: ifeq            219
        //    22: aload_3         /* vRuleStr */
        //    23: aload           putMatcher
        //    25: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //    28: astore          5
        //    30: aload           5
        //    32: ldc_w           "putMatcher.group()"
        //    35: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //    38: aload           5
        //    40: ldc_w           ""
        //    43: iconst_0       
        //    44: iconst_4       
        //    45: aconst_null    
        //    46: invokestatic    kotlin/text/StringsKt.replace$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
        //    49: astore_3        /* vRuleStr */
        //    50: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //    53: astore          6
        //    55: aload           putMatcher
        //    57: iconst_1       
        //    58: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //    61: astore          json$iv
        //    63: iconst_0       
        //    64: istore          $i$f$fromJsonObject
        //    66: iconst_0       
        //    67: istore          9
        //    69: nop            
        //    70: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //    73: astore          10
        //    75: iconst_0       
        //    76: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //    78: aload           $this$fromJsonObject$iv
        //    80: aload           json$iv
        //    82: iconst_0       
        //    83: istore          $i$f$genericType
        //    85: new             Lio/legado/app/model/analyzeRule/AnalyzeRule$splitPutRule$$inlined$fromJsonObject$1;
        //    88: dup            
        //    89: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule$splitPutRule$$inlined$fromJsonObject$1.<init>:()V
        //    92: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeRule$splitPutRule$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
        //    95: astore          13
        //    97: aload           13
        //    99: ldc_w           "object : TypeToken<T>() {}.type"
        //   102: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   105: aload           13
        //   107: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   110: dup            
        //   111: instanceof      Ljava/util/Map;
        //   114: ifne            119
        //   117: pop            
        //   118: aconst_null    
        //   119: checkcast       Ljava/util/Map;
        //   122: astore          null
        //   124: iconst_0       
        //   125: istore          12
        //   127: aload           11
        //   129: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   132: astore          10
        //   134: goto            157
        //   137: astore          11
        //   139: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   142: astore          12
        //   144: iconst_0       
        //   145: istore          13
        //   147: aload           11
        //   149: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   152: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   155: astore          10
        //   157: aload           10
        //   159: nop            
        //   160: astore          null
        //   162: iconst_0       
        //   163: istore          7
        //   165: aload           6
        //   167: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //   170: ifeq            177
        //   173: aconst_null    
        //   174: goto            179
        //   177: aload           6
        //   179: checkcast       Ljava/util/Map;
        //   182: astore          5
        //   184: aload           5
        //   186: ifnonnull       192
        //   189: goto            14
        //   192: aload           5
        //   194: astore          6
        //   196: iconst_0       
        //   197: istore          7
        //   199: iconst_0       
        //   200: istore          8
        //   202: aload           6
        //   204: astore          it
        //   206: iconst_0       
        //   207: istore          $i$a$-let-AnalyzeRule$splitPutRule$1
        //   209: aload_2         /* putMap */
        //   210: aload           it
        //   212: invokevirtual   java/util/HashMap.putAll:(Ljava/util/Map;)V
        //   215: nop            
        //   216: goto            14
        //   219: aload_3         /* vRuleStr */
        //   220: areturn        
        //    Signature:
        //  (Ljava/lang/String;Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;)Ljava/lang/String;
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  ruleStr  
        //  putMap   
        //    StackMapTable: 00 08 FD 00 0E 07 00 88 07 02 06 FF 00 68 00 0E 07 00 02 07 00 88 07 02 2F 07 00 88 07 02 06 07 00 88 07 02 26 07 00 88 01 01 07 02 3F 01 01 07 02 41 00 01 07 00 04 FF 00 11 00 0A 07 00 02 07 00 88 07 02 2F 07 00 88 07 02 06 07 00 88 07 02 26 07 00 88 01 01 00 01 07 01 7F FD 00 13 07 00 04 07 00 04 FF 00 13 00 0C 07 00 02 07 00 88 07 02 2F 07 00 88 07 02 06 07 00 88 07 00 04 01 01 01 07 00 04 07 00 04 00 00 41 07 00 04 FF 00 0C 00 0C 07 00 02 07 00 88 07 02 2F 07 00 88 07 02 06 07 01 10 07 00 04 01 01 01 07 00 04 07 00 04 00 00 FF 00 1A 00 05 07 00 02 07 00 88 07 02 2F 07 00 88 07 02 06 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  69     134    137    157    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final String replaceRegex(final String result, final SourceRule rule) {
        if (rule.getReplaceRegex$reader_pro().length() == 0) {
            return result;
        }
        Object vResult = null;
        vResult = result;
        Object o3;
        if (rule.getReplaceFirst$reader_pro()) {
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Pattern pattern = Pattern.compile(rule.getReplaceRegex$reader_pro());
                final Matcher matcher = pattern.matcher((CharSequence)vResult);
                String replaceFirst;
                if (matcher.find()) {
                    final String group = matcher.group(0);
                    Intrinsics.checkNotNull((Object)group);
                    replaceFirst = new Regex(rule.getReplaceRegex$reader_pro()).replaceFirst((CharSequence)group, rule.getReplacement$reader_pro());
                }
                else {
                    replaceFirst = "";
                }
                o = Result.constructor-impl((Object)replaceFirst);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(o2);
            Object replaceFirst$default;
            if (exceptionOrNull-impl == null) {
                replaceFirst$default = o2;
            }
            else {
                final Throwable it = exceptionOrNull-impl;
                final int n2 = 0;
                replaceFirst$default = StringsKt.replaceFirst$default((String)vResult, rule.getReplaceRegex$reader_pro(), rule.getReplacement$reader_pro(), false, 4, (Object)null);
            }
            o3 = replaceFirst$default;
        }
        else {
            Object o4;
            try {
                final Result$Companion companion3 = Result.Companion;
                final int n3 = 0;
                o4 = Result.constructor-impl((Object)new Regex(rule.getReplaceRegex$reader_pro()).replace((CharSequence)vResult, rule.getReplacement$reader_pro()));
            }
            catch (final Throwable t2) {
                final Result$Companion companion4 = Result.Companion;
                o4 = Result.constructor-impl(ResultKt.createFailure(t2));
            }
            final Object o5 = o4;
            final Throwable exceptionOrNull-impl2 = Result.exceptionOrNull-impl(o5);
            Object replace$default;
            if (exceptionOrNull-impl2 == null) {
                replace$default = o5;
            }
            else {
                final Throwable it = exceptionOrNull-impl2;
                final int n4 = 0;
                replace$default = StringsKt.replace$default((String)vResult, rule.getReplaceRegex$reader_pro(), rule.getReplacement$reader_pro(), false, 4, (Object)null);
            }
            o3 = replace$default;
        }
        vResult = o3;
        return (String)vResult;
    }
    
    @NotNull
    public final List<SourceRule> splitSourceRule(@Nullable final String ruleStr, final boolean allInOne) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: checkcast       Ljava/lang/CharSequence;
        //     4: astore_3       
        //     5: iconst_0       
        //     6: istore          4
        //     8: iconst_0       
        //     9: istore          5
        //    11: aload_3        
        //    12: ifnull          24
        //    15: aload_3        
        //    16: invokeinterface java/lang/CharSequence.length:()I
        //    21: ifne            28
        //    24: iconst_1       
        //    25: goto            29
        //    28: iconst_0       
        //    29: ifeq            36
        //    32: invokestatic    kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
        //    35: areturn        
        //    36: new             Ljava/util/ArrayList;
        //    39: dup            
        //    40: invokespecial   java/util/ArrayList.<init>:()V
        //    43: astore_3        /* ruleList */
        //    44: getstatic       io/legado/app/model/analyzeRule/AnalyzeRule$Mode.Default:Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;
        //    47: astore          mMode
        //    49: iconst_0       
        //    50: istore          start
        //    52: iload_2         /* allInOne */
        //    53: ifeq            85
        //    56: aload_1         /* ruleStr */
        //    57: ldc_w           ":"
        //    60: iconst_0       
        //    61: iconst_2       
        //    62: aconst_null    
        //    63: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
        //    66: ifeq            85
        //    69: getstatic       io/legado/app/model/analyzeRule/AnalyzeRule$Mode.Regex:Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;
        //    72: astore          mMode
        //    74: aload_0         /* this */
        //    75: iconst_1       
        //    76: putfield        io/legado/app/model/analyzeRule/AnalyzeRule.isRegex:Z
        //    79: iconst_1       
        //    80: istore          start
        //    82: goto            97
        //    85: aload_0         /* this */
        //    86: getfield        io/legado/app/model/analyzeRule/AnalyzeRule.isRegex:Z
        //    89: ifeq            97
        //    92: getstatic       io/legado/app/model/analyzeRule/AnalyzeRule$Mode.Regex:Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;
        //    95: astore          mMode
        //    97: aconst_null    
        //    98: astore          tmp
        //   100: getstatic       io/legado/app/constant/AppPattern.INSTANCE:Lio/legado/app/constant/AppPattern;
        //   103: invokevirtual   io/legado/app/constant/AppPattern.getJS_PATTERN:()Ljava/util/regex/Pattern;
        //   106: aload_1         /* ruleStr */
        //   107: checkcast       Ljava/lang/CharSequence;
        //   110: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   113: astore          jsMatcher
        //   115: aload           jsMatcher
        //   117: invokevirtual   java/util/regex/Matcher.find:()Z
        //   120: ifeq            429
        //   123: aload           jsMatcher
        //   125: invokevirtual   java/util/regex/Matcher.start:()I
        //   128: iload           start
        //   130: if_icmple       367
        //   133: aload_1         /* ruleStr */
        //   134: astore          8
        //   136: aload           jsMatcher
        //   138: invokevirtual   java/util/regex/Matcher.start:()I
        //   141: istore          9
        //   143: iconst_0       
        //   144: istore          10
        //   146: aload           8
        //   148: dup            
        //   149: ifnonnull       163
        //   152: new             Ljava/lang/NullPointerException;
        //   155: dup            
        //   156: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //   159: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   162: athrow         
        //   163: iload           start
        //   165: iload           9
        //   167: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   170: dup            
        //   171: ldc_w           "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   174: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   177: astore          8
        //   179: nop            
        //   180: iconst_0       
        //   181: istore          $i$f$trim
        //   183: aload           $this$trim$iv
        //   185: checkcast       Ljava/lang/CharSequence;
        //   188: astore          $this$trim$iv$iv
        //   190: iconst_0       
        //   191: istore          $i$f$trim
        //   193: iconst_0       
        //   194: istore          startIndex$iv$iv
        //   196: aload           $this$trim$iv$iv
        //   198: invokeinterface java/lang/CharSequence.length:()I
        //   203: iconst_1       
        //   204: isub           
        //   205: istore          endIndex$iv$iv
        //   207: iconst_0       
        //   208: istore          startFound$iv$iv
        //   210: iload           startIndex$iv$iv
        //   212: iload           endIndex$iv$iv
        //   214: if_icmpgt       304
        //   217: iload           startFound$iv$iv
        //   219: ifne            227
        //   222: iload           startIndex$iv$iv
        //   224: goto            229
        //   227: iload           endIndex$iv$iv
        //   229: istore          index$iv$iv
        //   231: aload           $this$trim$iv$iv
        //   233: iload           index$iv$iv
        //   235: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   240: istore          it
        //   242: iconst_0       
        //   243: istore          $i$a$-trim-AnalyzeRule$splitSourceRule$1
        //   245: iload           it
        //   247: bipush          32
        //   249: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   252: ifgt            259
        //   255: iconst_1       
        //   256: goto            260
        //   259: iconst_0       
        //   260: istore          match$iv$iv
        //   262: iload           startFound$iv$iv
        //   264: ifne            287
        //   267: iload           match$iv$iv
        //   269: ifne            278
        //   272: iconst_1       
        //   273: istore          startFound$iv$iv
        //   275: goto            301
        //   278: iload           startIndex$iv$iv
        //   280: iconst_1       
        //   281: iadd           
        //   282: istore          startIndex$iv$iv
        //   284: goto            301
        //   287: iload           match$iv$iv
        //   289: ifne            295
        //   292: goto            304
        //   295: iload           endIndex$iv$iv
        //   297: iconst_1       
        //   298: isub           
        //   299: istore          endIndex$iv$iv
        //   301: goto            210
        //   304: aload           $this$trim$iv$iv
        //   306: iload           startIndex$iv$iv
        //   308: iload           endIndex$iv$iv
        //   310: iconst_1       
        //   311: iadd           
        //   312: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   317: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   320: astore          tmp
        //   322: aload           tmp
        //   324: checkcast       Ljava/lang/CharSequence;
        //   327: astore          8
        //   329: iconst_0       
        //   330: istore          9
        //   332: aload           8
        //   334: invokeinterface java/lang/CharSequence.length:()I
        //   339: ifle            346
        //   342: iconst_1       
        //   343: goto            347
        //   346: iconst_0       
        //   347: ifeq            367
        //   350: aload_3         /* ruleList */
        //   351: new             Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;
        //   354: dup            
        //   355: aload_0         /* this */
        //   356: aload           tmp
        //   358: aload           mMode
        //   360: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule$SourceRule.<init>:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;)V
        //   363: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   366: pop            
        //   367: aload_3         /* ruleList */
        //   368: new             Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;
        //   371: dup            
        //   372: aload_0         /* this */
        //   373: aload           jsMatcher
        //   375: iconst_2       
        //   376: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   379: astore          9
        //   381: aload           9
        //   383: ifnonnull       395
        //   386: aload           jsMatcher
        //   388: iconst_1       
        //   389: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   392: goto            397
        //   395: aload           9
        //   397: astore          8
        //   399: aload           8
        //   401: ldc_w           "jsMatcher.group(2) ?: jsMatcher.group(1)"
        //   404: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   407: aload           8
        //   409: getstatic       io/legado/app/model/analyzeRule/AnalyzeRule$Mode.Js:Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;
        //   412: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule$SourceRule.<init>:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;)V
        //   415: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   418: pop            
        //   419: aload           jsMatcher
        //   421: invokevirtual   java/util/regex/Matcher.end:()I
        //   424: istore          start
        //   426: goto            115
        //   429: aload_1         /* ruleStr */
        //   430: invokevirtual   java/lang/String.length:()I
        //   433: iload           start
        //   435: if_icmple       663
        //   438: aload_1         /* ruleStr */
        //   439: astore          8
        //   441: iconst_0       
        //   442: istore          9
        //   444: aload           8
        //   446: dup            
        //   447: ifnonnull       461
        //   450: new             Ljava/lang/NullPointerException;
        //   453: dup            
        //   454: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //   457: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   460: athrow         
        //   461: iload           start
        //   463: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   466: dup            
        //   467: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   470: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   473: astore          8
        //   475: nop            
        //   476: iconst_0       
        //   477: istore          $i$f$trim
        //   479: aload           $this$trim$iv
        //   481: checkcast       Ljava/lang/CharSequence;
        //   484: astore          $this$trim$iv$iv
        //   486: iconst_0       
        //   487: istore          $i$f$trim
        //   489: iconst_0       
        //   490: istore          startIndex$iv$iv
        //   492: aload           $this$trim$iv$iv
        //   494: invokeinterface java/lang/CharSequence.length:()I
        //   499: iconst_1       
        //   500: isub           
        //   501: istore          endIndex$iv$iv
        //   503: iconst_0       
        //   504: istore          startFound$iv$iv
        //   506: iload           startIndex$iv$iv
        //   508: iload           endIndex$iv$iv
        //   510: if_icmpgt       600
        //   513: iload           startFound$iv$iv
        //   515: ifne            523
        //   518: iload           startIndex$iv$iv
        //   520: goto            525
        //   523: iload           endIndex$iv$iv
        //   525: istore          index$iv$iv
        //   527: aload           $this$trim$iv$iv
        //   529: iload           index$iv$iv
        //   531: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   536: istore          it
        //   538: iconst_0       
        //   539: istore          $i$a$-trim-AnalyzeRule$splitSourceRule$2
        //   541: iload           it
        //   543: bipush          32
        //   545: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   548: ifgt            555
        //   551: iconst_1       
        //   552: goto            556
        //   555: iconst_0       
        //   556: istore          match$iv$iv
        //   558: iload           startFound$iv$iv
        //   560: ifne            583
        //   563: iload           match$iv$iv
        //   565: ifne            574
        //   568: iconst_1       
        //   569: istore          startFound$iv$iv
        //   571: goto            597
        //   574: iload           startIndex$iv$iv
        //   576: iconst_1       
        //   577: iadd           
        //   578: istore          startIndex$iv$iv
        //   580: goto            597
        //   583: iload           match$iv$iv
        //   585: ifne            591
        //   588: goto            600
        //   591: iload           endIndex$iv$iv
        //   593: iconst_1       
        //   594: isub           
        //   595: istore          endIndex$iv$iv
        //   597: goto            506
        //   600: aload           $this$trim$iv$iv
        //   602: iload           startIndex$iv$iv
        //   604: iload           endIndex$iv$iv
        //   606: iconst_1       
        //   607: iadd           
        //   608: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   613: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   616: astore          tmp
        //   618: aload           tmp
        //   620: checkcast       Ljava/lang/CharSequence;
        //   623: astore          8
        //   625: iconst_0       
        //   626: istore          9
        //   628: aload           8
        //   630: invokeinterface java/lang/CharSequence.length:()I
        //   635: ifle            642
        //   638: iconst_1       
        //   639: goto            643
        //   642: iconst_0       
        //   643: ifeq            663
        //   646: aload_3         /* ruleList */
        //   647: new             Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;
        //   650: dup            
        //   651: aload_0         /* this */
        //   652: aload           tmp
        //   654: aload           mMode
        //   656: invokespecial   io/legado/app/model/analyzeRule/AnalyzeRule$SourceRule.<init>:(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;)V
        //   659: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   662: pop            
        //   663: aload_3         /* ruleList */
        //   664: checkcast       Ljava/util/List;
        //   667: areturn        
        //    Signature:
        //  (Ljava/lang/String;Z)Ljava/util/List<Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;>;
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  ruleStr   
        //  allInOne  
        //    StackMapTable: 00 26 FE 00 18 07 00 D5 01 01 03 40 01 06 FF 00 30 00 06 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 00 00 0B FD 00 11 07 00 88 07 02 06 FF 00 2F 00 0B 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 07 00 88 01 01 00 01 07 00 88 FF 00 2E 00 0F 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 07 00 88 01 07 00 D5 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 29 00 0F 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 07 00 D5 01 07 00 D5 01 01 01 01 00 00 40 01 FF 00 13 00 08 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 00 00 FF 00 1B 00 0A 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 00 07 00 88 00 04 07 01 3B 08 01 70 08 01 70 07 00 02 FF 00 01 00 0A 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 00 07 00 88 00 05 07 01 3B 08 01 70 08 01 70 07 00 02 07 00 88 F9 00 1F FF 00 1F 00 0A 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 07 00 88 01 00 01 07 00 88 FF 00 2C 00 0F 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 07 00 88 01 07 00 D5 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 29 00 0F 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 07 00 D5 01 07 00 D5 01 01 01 01 00 00 40 01 FF 00 13 00 08 07 00 02 07 00 88 01 07 01 3B 07 01 23 01 07 00 88 07 02 06 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static /* synthetic */ List splitSourceRule$default(final AnalyzeRule analyzeRule, final String ruleStr, boolean allInOne, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            allInOne = false;
        }
        return analyzeRule.splitSourceRule(ruleStr, allInOne);
    }
    
    @NotNull
    public final String put(@NotNull final String key, @NotNull final String value) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        final BookChapter chapter = this.chapter;
        Unit instance;
        if (chapter == null) {
            instance = null;
        }
        else {
            chapter.putVariable(key, value);
            instance = Unit.INSTANCE;
        }
        final Unit unit = instance;
        Unit instance2;
        if (unit == null) {
            final BaseBook book = this.getBook();
            if (book == null) {
                instance2 = null;
            }
            else {
                book.putVariable(key, value);
                instance2 = Unit.INSTANCE;
            }
        }
        else {
            instance2 = unit;
        }
        if (instance2 == null) {
            this.ruleData.putVariable(key, value);
        }
        return value;
    }
    
    @NotNull
    public final String get(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (Intrinsics.areEqual((Object)key, (Object)"bookName")) {
            final BaseBook book = this.getBook();
            if (book != null) {
                final BaseBook it = book;
                final int n = 0;
                return it.getName();
            }
        }
        else if (Intrinsics.areEqual((Object)key, (Object)"title")) {
            final BookChapter chapter = this.chapter;
            if (chapter != null) {
                final BookChapter it2 = chapter;
                final int n2 = 0;
                return it2.getTitle();
            }
        }
        final BookChapter chapter2 = this.chapter;
        final String s = (chapter2 == null) ? null : chapter2.getVariable(key);
        String s3;
        if (s == null) {
            final BaseBook book2 = this.getBook();
            final String s2 = (book2 == null) ? null : book2.getVariable(key);
            if (s2 == null) {
                final RuleDataInterface ruleData = this.ruleData;
                if (ruleData == null) {
                    s3 = "";
                }
                else {
                    final String variable = ruleData.getVariable(key);
                    s3 = ((variable == null) ? "" : variable);
                }
            }
            else {
                s3 = s2;
            }
        }
        else {
            s3 = s;
        }
        return s3;
    }
    
    @Nullable
    public final Object evalJS(@NotNull final String jsStr, @Nullable final Object result) {
        Intrinsics.checkNotNullParameter((Object)jsStr, "jsStr");
        final SimpleBindings bindings = new SimpleBindings();
        ((Map)bindings).put("java", this);
        ((Map)bindings).put("cookie", new CookieStore(this.getUserNameSpace()));
        ((Map)bindings).put("cache", new CacheManager(this.getUserNameSpace()));
        ((Map)bindings).put("source", this.source);
        ((Map)bindings).put("book", this.getBook());
        ((Map)bindings).put("result", result);
        ((Map)bindings).put("baseUrl", this.baseUrl);
        ((Map)bindings).put("chapter", this.chapter);
        final Map map = (Map)bindings;
        final String s = "title";
        final BookChapter chapter = this.chapter;
        map.put(s, (chapter == null) ? null : chapter.getTitle());
        ((Map)bindings).put("src", this.content);
        ((Map)bindings).put("nextChapterUrl", this.nextChapterUrl);
        return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, (Bindings)bindings);
    }
    
    @Nullable
    @Override
    public String ajax(@NotNull final String urlStr) {
        Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
        return (String)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeRule$ajax.AnalyzeRule$ajax$1(urlStr, this, (Continuation)null), 1, (Object)null);
    }
    
    @Nullable
    public final String toNumChapter(@Nullable final String s) {
        if (s == null) {
            return null;
        }
        final Matcher matcher = AnalyzeRule.titleNumPattern.matcher(s);
        if (matcher.find()) {
            return new StringBuilder().append((Object)matcher.group(1)).append(StringUtils.INSTANCE.stringToInt(matcher.group(2))).append((Object)matcher.group(3)).toString();
        }
        return s;
    }
    
    public final void reGetBook() {
        final BaseSource source = this.source;
        final BookSource bookSource = (source instanceof BookSource) ? ((BookSource)source) : null;
        final BaseBook book2 = this.getBook();
        final Book book = (book2 instanceof Book) ? ((Book)book2) : null;
        if (bookSource == null || book == null) {
            return;
        }
        BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeRule$reGetBook.AnalyzeRule$reGetBook$1(bookSource, this, book, (Continuation)null), 1, (Object)null);
    }
    
    public final void refreshBookUrl() {
        BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeRule$refreshBookUrl.AnalyzeRule$refreshBookUrl$1(this, (Continuation)null), 1, (Object)null);
    }
    
    public final void refreshTocUrl() {
        BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeRule$refreshTocUrl.AnalyzeRule$refreshTocUrl$1(this, (Continuation)null), 1, (Object)null);
    }
    
    @Nullable
    @Override
    public byte[] aesBase64DecodeToByteArray(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesBase64DecodeToByteArray(str, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String aesBase64DecodeToString(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesBase64DecodeToString(str, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String aesDecodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return DefaultImpls.aesDecodeArgsBase64Str(data, key, mode, padding, iv);
    }
    
    @Nullable
    @Override
    public byte[] aesDecodeToByteArray(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesDecodeToByteArray(str, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String aesDecodeToString(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesDecodeToString(str, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String aesEncodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return DefaultImpls.aesEncodeArgsBase64Str(data, key, mode, padding, iv);
    }
    
    @Nullable
    @Override
    public byte[] aesEncodeToBase64ByteArray(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesEncodeToBase64ByteArray(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String aesEncodeToBase64String(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesEncodeToBase64String(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public byte[] aesEncodeToByteArray(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesEncodeToByteArray(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String aesEncodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.aesEncodeToString(data, key, transformation, iv);
    }
    
    @NotNull
    @Override
    public StrResponse[] ajaxAll(@NotNull final String[] urlList) {
        return DefaultImpls.ajaxAll(urlList);
    }
    
    @NotNull
    @Override
    public String androidId() {
        return DefaultImpls.androidId();
    }
    
    @NotNull
    @Override
    public String base64Decode(@NotNull final String str) {
        return DefaultImpls.base64Decode(str);
    }
    
    @NotNull
    @Override
    public String base64Decode(@NotNull final String str, final int flags) {
        return DefaultImpls.base64Decode(str, flags);
    }
    
    @Nullable
    @Override
    public byte[] base64DecodeToByteArray(@Nullable final String str) {
        return DefaultImpls.base64DecodeToByteArray(str);
    }
    
    @Nullable
    @Override
    public byte[] base64DecodeToByteArray(@Nullable final String str, final int flags) {
        return DefaultImpls.base64DecodeToByteArray(str, flags);
    }
    
    @Nullable
    @Override
    public String base64Encode(@NotNull final String str) {
        return DefaultImpls.base64Encode(str);
    }
    
    @Nullable
    @Override
    public String base64Encode(@NotNull final String str, final int flags) {
        return DefaultImpls.base64Encode(str, flags);
    }
    
    @Nullable
    @Override
    public String cacheFile(@NotNull final String urlStr) {
        return DefaultImpls.cacheFile(urlStr);
    }
    
    @Nullable
    @Override
    public String cacheFile(@NotNull final String urlStr, final int saveTime) {
        return DefaultImpls.cacheFile(urlStr, saveTime);
    }
    
    @NotNull
    @Override
    public StrResponse connect(@NotNull final String urlStr) {
        return DefaultImpls.connect(urlStr);
    }
    
    @NotNull
    @Override
    public StrResponse connect(@NotNull final String urlStr, @Nullable final String header) {
        return DefaultImpls.connect(urlStr, header);
    }
    
    @Override
    public void deleteFile(@NotNull final String path) {
        DefaultImpls.deleteFile(path);
    }
    
    @Nullable
    @Override
    public String desBase64DecodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.desBase64DecodeToString(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String desDecodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.desDecodeToString(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String desEncodeToBase64String(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.desEncodeToBase64String(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String desEncodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return DefaultImpls.desEncodeToString(data, key, transformation, iv);
    }
    
    @Nullable
    @Override
    public String digestBase64Str(@NotNull final String data, @NotNull final String algorithm) {
        return DefaultImpls.digestBase64Str(data, algorithm);
    }
    
    @Nullable
    @Override
    public String digestHex(@NotNull final String data, @NotNull final String algorithm) {
        return DefaultImpls.digestHex(data, algorithm);
    }
    
    @NotNull
    @Override
    public String downloadFile(@NotNull final String content, @NotNull final String url) {
        return DefaultImpls.downloadFile(content, url);
    }
    
    @NotNull
    @Override
    public String encodeURI(@NotNull final String str) {
        return DefaultImpls.encodeURI(str);
    }
    
    @NotNull
    @Override
    public String encodeURI(@NotNull final String str, @NotNull final String enc) {
        return DefaultImpls.encodeURI(str, enc);
    }
    
    @NotNull
    @Override
    public Connection$Response get(@NotNull final String urlStr, @NotNull final Map<String, String> headers) {
        return DefaultImpls.get(urlStr, headers);
    }
    
    @NotNull
    @Override
    public String getCookie(@NotNull final String tag, @Nullable final String key) {
        return DefaultImpls.getCookie(tag, key);
    }
    
    @NotNull
    @Override
    public File getFile(@NotNull final String path) {
        return DefaultImpls.getFile(path);
    }
    
    @NotNull
    @Override
    public String getTxtInFolder(@NotNull final String unzipPath) {
        return DefaultImpls.getTxtInFolder(unzipPath);
    }
    
    @Nullable
    @Override
    public byte[] getZipByteArrayContent(@NotNull final String url, @NotNull final String path) {
        return DefaultImpls.getZipByteArrayContent(url, path);
    }
    
    @NotNull
    @Override
    public String getZipStringContent(@NotNull final String url, @NotNull final String path) {
        return DefaultImpls.getZipStringContent(url, path);
    }
    
    @NotNull
    @Override
    public String getZipStringContent(@NotNull final String url, @NotNull final String path, @NotNull final String charsetName) {
        return DefaultImpls.getZipStringContent(url, path, charsetName);
    }
    
    @NotNull
    @Override
    public Connection$Response head(@NotNull final String urlStr, @NotNull final Map<String, String> headers) {
        return DefaultImpls.head(urlStr, headers);
    }
    
    @NotNull
    @Override
    public String htmlFormat(@NotNull final String str) {
        return DefaultImpls.htmlFormat(str);
    }
    
    @NotNull
    @Override
    public String importScript(@NotNull final String path) {
        return DefaultImpls.importScript(path);
    }
    
    @NotNull
    @Override
    public String log(@NotNull final String msg) {
        return DefaultImpls.log(msg);
    }
    
    @Override
    public void logType(@Nullable final Object any) {
        DefaultImpls.logType(any);
    }
    
    @Override
    public void longToast(@Nullable final Object msg) {
        DefaultImpls.longToast(msg);
    }
    
    @NotNull
    @Override
    public String md5Encode(@NotNull final String str) {
        return DefaultImpls.md5Encode(str);
    }
    
    @NotNull
    @Override
    public String md5Encode16(@NotNull final String str) {
        return DefaultImpls.md5Encode16(str);
    }
    
    @NotNull
    @Override
    public Connection$Response post(@NotNull final String urlStr, @NotNull final String body, @NotNull final Map<String, String> headers) {
        return DefaultImpls.post(urlStr, body, headers);
    }
    
    @Nullable
    @Override
    public QueryTTF queryBase64TTF(@Nullable final String base64) {
        return DefaultImpls.queryBase64TTF(base64);
    }
    
    @Nullable
    @Override
    public QueryTTF queryTTF(@Nullable final String str) {
        return DefaultImpls.queryTTF(str);
    }
    
    @NotNull
    @Override
    public String randomUUID() {
        return DefaultImpls.randomUUID();
    }
    
    @Nullable
    @Override
    public byte[] readFile(@NotNull final String path) {
        return DefaultImpls.readFile(path);
    }
    
    @NotNull
    @Override
    public String readTxtFile(@NotNull final String path) {
        return DefaultImpls.readTxtFile(path);
    }
    
    @NotNull
    @Override
    public String readTxtFile(@NotNull final String path, @NotNull final String charsetName) {
        return DefaultImpls.readTxtFile(path, charsetName);
    }
    
    @NotNull
    @Override
    public String replaceFont(@NotNull final String text, @Nullable final QueryTTF font1, @Nullable final QueryTTF font2) {
        return DefaultImpls.replaceFont(text, font1, font2);
    }
    
    @NotNull
    @Override
    public String timeFormat(final long time) {
        return DefaultImpls.timeFormat(time);
    }
    
    @Nullable
    @Override
    public String timeFormatUTC(final long time, @NotNull final String format, final int sh) {
        return DefaultImpls.timeFormatUTC(time, format, sh);
    }
    
    @Override
    public void toast(@Nullable final Object msg) {
        DefaultImpls.toast(msg);
    }
    
    @Nullable
    @Override
    public String tripleDESDecodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return DefaultImpls.tripleDESDecodeArgsBase64Str(data, key, mode, padding, iv);
    }
    
    @Nullable
    @Override
    public String tripleDESDecodeStr(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return DefaultImpls.tripleDESDecodeStr(data, key, mode, padding, iv);
    }
    
    @Nullable
    @Override
    public String tripleDESEncodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return DefaultImpls.tripleDESEncodeArgsBase64Str(data, key, mode, padding, iv);
    }
    
    @Nullable
    @Override
    public String tripleDESEncodeBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return DefaultImpls.tripleDESEncodeBase64Str(data, key, mode, padding, iv);
    }
    
    @NotNull
    @Override
    public String unzipFile(@NotNull final String zipPath) {
        return DefaultImpls.unzipFile(zipPath);
    }
    
    @NotNull
    @Override
    public String utf8ToGbk(@NotNull final String str) {
        return DefaultImpls.utf8ToGbk(str);
    }
    
    @Nullable
    @Override
    public String webView(@Nullable final String html, @Nullable final String url, @Nullable final String js) {
        return DefaultImpls.webView(html, url, js);
    }
    
    @JvmOverloads
    @NotNull
    public final AnalyzeRule setContent(@Nullable final Object content) {
        return setContent$default(this, content, null, 2, null);
    }
    
    @JvmOverloads
    @Nullable
    public final List<String> getStringList(@Nullable final String rule, @Nullable final Object mContent) {
        return getStringList$default(this, rule, mContent, false, 4, null);
    }
    
    @JvmOverloads
    @Nullable
    public final List<String> getStringList(@Nullable final String rule) {
        return getStringList$default(this, rule, null, false, 6, null);
    }
    
    @JvmOverloads
    @Nullable
    public final List<String> getStringList(@NotNull final List<SourceRule> ruleList, @Nullable final Object mContent) {
        Intrinsics.checkNotNullParameter((Object)ruleList, "ruleList");
        return getStringList$default(this, ruleList, mContent, false, 4, null);
    }
    
    @JvmOverloads
    @Nullable
    public final List<String> getStringList(@NotNull final List<SourceRule> ruleList) {
        Intrinsics.checkNotNullParameter((Object)ruleList, "ruleList");
        return getStringList$default(this, ruleList, null, false, 6, null);
    }
    
    @JvmOverloads
    @NotNull
    public final String getString(@Nullable final String ruleStr, @Nullable final Object mContent) {
        return getString$default(this, ruleStr, mContent, false, 4, null);
    }
    
    @JvmOverloads
    @NotNull
    public final String getString(@Nullable final String ruleStr) {
        return getString$default(this, ruleStr, null, false, 6, null);
    }
    
    @JvmOverloads
    @NotNull
    public final String getString(@NotNull final List<SourceRule> ruleList, @Nullable final Object mContent) {
        Intrinsics.checkNotNullParameter((Object)ruleList, "ruleList");
        return getString$default(this, ruleList, mContent, false, 4, null);
    }
    
    @JvmOverloads
    @NotNull
    public final String getString(@NotNull final List<SourceRule> ruleList) {
        Intrinsics.checkNotNullParameter((Object)ruleList, "ruleList");
        return getString$default(this, ruleList, null, false, 6, null);
    }
    
    public static final /* synthetic */ boolean access$isJSON$p(final AnalyzeRule $this) {
        return $this.isJSON;
    }
    
    public static final /* synthetic */ Pattern access$getEvalPattern$cp() {
        return AnalyzeRule.evalPattern;
    }
    
    public static final /* synthetic */ Pattern access$getRegexPattern$cp() {
        return AnalyzeRule.regexPattern;
    }
    
    static {
        Companion = new Companion(null);
        putPattern = Pattern.compile("@put:(\\{[^}]+?\\})", 2);
        evalPattern = Pattern.compile("@get:\\{[^}]+?\\}|\\{\\{[\\w\\W]*?\\}\\}", 2);
        regexPattern = Pattern.compile("\\$\\d{1,2}");
        titleNumPattern = Pattern.compile("(\u7b2c)(.+?)(\u7ae0)");
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0019\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J\u0010\u0010(\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J\u0010\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\u0001J\u0010\u0010,\u001a\u00020*2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082D?\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u00020\u0005X\u0080\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR0\u0010\u000f\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0010j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`\u0011X\u0080\u0004?\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0015X\u0080\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u0003X\u0080\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020\u0003X\u0080\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u001c\"\u0004\b!\u0010\u001eR\u001a\u0010\"\u001a\u00020\u0003X\u0080\u000e?\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u001c\"\u0004\b$\u0010\u001eR\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00030&X\u0082\u0004?\u0006\u0002\n\u0000R\u0014\u0010'\u001a\b\u0012\u0004\u0012\u00020\b0&X\u0082\u0004?\u0006\u0002\n\u0000：\u0006-" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeRule$SourceRule;", "", "ruleStr", "", "mode", "Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;", "(Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;)V", "defaultRuleType", "", "getRuleType", "jsRuleType", "getMode$reader_pro", "()Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;", "setMode$reader_pro", "(Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;)V", "putMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getPutMap$reader_pro", "()Ljava/util/HashMap;", "replaceFirst", "", "getReplaceFirst$reader_pro", "()Z", "setReplaceFirst$reader_pro", "(Z)V", "replaceRegex", "getReplaceRegex$reader_pro", "()Ljava/lang/String;", "setReplaceRegex$reader_pro", "(Ljava/lang/String;)V", "replacement", "getReplacement$reader_pro", "setReplacement$reader_pro", "rule", "getRule$reader_pro", "setRule$reader_pro", "ruleParam", "Ljava/util/ArrayList;", "ruleType", "isRule", "makeUpRule", "", "result", "splitRegex", "reader-pro" })
    public final class SourceRule
    {
        @NotNull
        private Mode mode;
        @NotNull
        private String rule;
        @NotNull
        private String replaceRegex;
        @NotNull
        private String replacement;
        private boolean replaceFirst;
        @NotNull
        private final HashMap<String, String> putMap;
        @NotNull
        private final ArrayList<String> ruleParam;
        @NotNull
        private final ArrayList<Integer> ruleType;
        private final int getRuleType;
        private final int jsRuleType;
        private final int defaultRuleType;
        
        public SourceRule(@NotNull final String ruleStr, final Mode mode) {
            Intrinsics.checkNotNullParameter((Object)AnalyzeRule.this, "this$0");
            Intrinsics.checkNotNullParameter((Object)ruleStr, "ruleStr");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            this.mode = mode;
            this.replaceRegex = "";
            this.replacement = "";
            this.putMap = new HashMap<String, String>();
            this.ruleParam = new ArrayList<String>();
            this.ruleType = new ArrayList<Integer>();
            this.getRuleType = -2;
            this.jsRuleType = -1;
            String rule;
            if (this.mode == Mode.Js || this.mode == Mode.Regex) {
                rule = ruleStr;
            }
            else if (StringsKt.startsWith(ruleStr, "@CSS:", true)) {
                this.mode = Mode.Default;
                rule = ruleStr;
            }
            else if (StringsKt.startsWith$default(ruleStr, "@@", false, 2, (Object)null)) {
                this.mode = Mode.Default;
                Intrinsics.checkNotNullExpressionValue((Object)(rule = ruleStr.substring(2)), "(this as java.lang.String).substring(startIndex)");
            }
            else if (StringsKt.startsWith(ruleStr, "@XPath:", true)) {
                this.mode = Mode.XPath;
                Intrinsics.checkNotNullExpressionValue((Object)(rule = ruleStr.substring(7)), "(this as java.lang.String).substring(startIndex)");
            }
            else if (StringsKt.startsWith(ruleStr, "@Json:", true)) {
                this.mode = Mode.Json;
                Intrinsics.checkNotNullExpressionValue((Object)(rule = ruleStr.substring(6)), "(this as java.lang.String).substring(startIndex)");
            }
            else if (AnalyzeRule.access$isJSON$p(AnalyzeRule.this) || StringsKt.startsWith$default(ruleStr, "$.", false, 2, (Object)null) || StringsKt.startsWith$default(ruleStr, "$[", false, 2, (Object)null)) {
                this.mode = Mode.Json;
                rule = ruleStr;
            }
            else if (StringsKt.startsWith$default(ruleStr, "/", false, 2, (Object)null)) {
                this.mode = Mode.XPath;
                rule = ruleStr;
            }
            else {
                rule = ruleStr;
            }
            this.rule = rule;
            this.rule = AnalyzeRule.this.splitPutRule(this.rule, this.putMap);
            int start = 0;
            String tmp = null;
            final Matcher evalMatcher = AnalyzeRule.access$getEvalPattern$cp().matcher(this.rule);
            if (evalMatcher.find()) {
                final String rule2 = this.rule;
                final int start2 = evalMatcher.start();
                final String s = rule2;
                if (s == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring = s.substring(start, start2);
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                tmp = substring;
                if (this.mode != Mode.Js && this.mode != Mode.Regex && (evalMatcher.start() == 0 || !StringsKt.contains$default((CharSequence)tmp, (CharSequence)"##", false, 2, (Object)null))) {
                    this.mode = Mode.Regex;
                }
                do {
                    if (evalMatcher.start() > start) {
                        final String rule3 = this.rule;
                        final int start3 = evalMatcher.start();
                        final String s2 = rule3;
                        if (s2 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring2 = s2.substring(start, start3);
                        Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        tmp = substring2;
                        this.splitRegex(tmp);
                    }
                    final String group = evalMatcher.group();
                    Intrinsics.checkNotNullExpressionValue((Object)group, "evalMatcher.group()");
                    tmp = group;
                    if (StringsKt.startsWith(tmp, "@get:", true)) {
                        this.ruleType.add(this.getRuleType);
                        final ArrayList<String> ruleParam = this.ruleParam;
                        final String substring3 = tmp.substring(6, StringsKt.getLastIndex((CharSequence)tmp));
                        Intrinsics.checkNotNullExpressionValue((Object)substring3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        ruleParam.add(substring3);
                    }
                    else if (StringsKt.startsWith$default(tmp, "{{", false, 2, (Object)null)) {
                        this.ruleType.add(this.jsRuleType);
                        final ArrayList<String> ruleParam2 = this.ruleParam;
                        final String substring4 = tmp.substring(2, tmp.length() - 2);
                        Intrinsics.checkNotNullExpressionValue((Object)substring4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        ruleParam2.add(substring4);
                    }
                    else {
                        this.splitRegex(tmp);
                    }
                    start = evalMatcher.end();
                } while (evalMatcher.find());
            }
            if (this.rule.length() > start) {
                final String rule4 = this.rule;
                if (rule4 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring5 = rule4.substring(start);
                Intrinsics.checkNotNullExpressionValue((Object)substring5, "(this as java.lang.String).substring(startIndex)");
                tmp = substring5;
                this.splitRegex(tmp);
            }
        }
        
        @NotNull
        public final Mode getMode$reader_pro() {
            return this.mode;
        }
        
        public final void setMode$reader_pro(@NotNull final Mode <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.mode = <set-?>;
        }
        
        @NotNull
        public final String getRule$reader_pro() {
            return this.rule;
        }
        
        public final void setRule$reader_pro(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.rule = <set-?>;
        }
        
        @NotNull
        public final String getReplaceRegex$reader_pro() {
            return this.replaceRegex;
        }
        
        public final void setReplaceRegex$reader_pro(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.replaceRegex = <set-?>;
        }
        
        @NotNull
        public final String getReplacement$reader_pro() {
            return this.replacement;
        }
        
        public final void setReplacement$reader_pro(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.replacement = <set-?>;
        }
        
        public final boolean getReplaceFirst$reader_pro() {
            return this.replaceFirst;
        }
        
        public final void setReplaceFirst$reader_pro(final boolean <set-?>) {
            this.replaceFirst = <set-?>;
        }
        
        @NotNull
        public final HashMap<String, String> getPutMap$reader_pro() {
            return this.putMap;
        }
        
        private final void splitRegex(final String ruleStr) {
            int start = 0;
            String tmp = null;
            final List ruleStrArray = StringsKt.split$default((CharSequence)ruleStr, new String[] { "##" }, false, 0, 6, (Object)null);
            final Matcher regexMatcher = AnalyzeRule.access$getRegexPattern$cp().matcher(ruleStrArray.get(0));
            if (regexMatcher.find()) {
                if (this.mode != Mode.Js && this.mode != Mode.Regex) {
                    this.mode = Mode.Regex;
                }
                do {
                    if (regexMatcher.start() > start) {
                        final int start2 = regexMatcher.start();
                        if (ruleStr == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring = ruleStr.substring(start, start2);
                        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        tmp = substring;
                        this.ruleType.add(this.defaultRuleType);
                        this.ruleParam.add(tmp);
                    }
                    final String group = regexMatcher.group();
                    Intrinsics.checkNotNullExpressionValue((Object)group, "regexMatcher.group()");
                    tmp = group;
                    final ArrayList<Integer> ruleType = this.ruleType;
                    final String substring2 = tmp.substring(1);
                    Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.String).substring(startIndex)");
                    ruleType.add(Integer.parseInt(substring2));
                    this.ruleParam.add(tmp);
                    start = regexMatcher.end();
                } while (regexMatcher.find());
            }
            if (ruleStr.length() > start) {
                if (ruleStr == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring3 = ruleStr.substring(start);
                Intrinsics.checkNotNullExpressionValue((Object)substring3, "(this as java.lang.String).substring(startIndex)");
                tmp = substring3;
                this.ruleType.add(this.defaultRuleType);
                this.ruleParam.add(tmp);
            }
        }
        
        public final void makeUpRule(@Nullable final Object result) {
            final StringBuilder infoVal = new StringBuilder();
            if (!this.ruleParam.isEmpty()) {
                int index = this.ruleParam.size();
                while (true) {
                    final int n = index;
                    index = n - 1;
                    if (n <= 0) {
                        break;
                    }
                    final Integer value = this.ruleType.get(index);
                    Intrinsics.checkNotNullExpressionValue((Object)value, "ruleType[index]");
                    final int regType = value.intValue();
                    if (regType > this.defaultRuleType) {
                        final List list = (result instanceof List) ? ((List)result) : null;
                        Unit instance;
                        if (list == null) {
                            instance = null;
                        }
                        else {
                            final List $this$makeUpRule_u24lambda_u2d1 = list;
                            final int n2 = 0;
                            if ($this$makeUpRule_u24lambda_u2d1.size() > regType) {
                                final String s = $this$makeUpRule_u24lambda_u2d1.get(regType);
                                if (s != null) {
                                    final String it = s;
                                    final int n3 = 0;
                                    infoVal.insert(0, it);
                                }
                            }
                            instance = Unit.INSTANCE;
                        }
                        if (instance != null) {
                            continue;
                        }
                        infoVal.insert(0, this.ruleParam.get(index));
                    }
                    else if (regType == this.jsRuleType) {
                        final String value2 = this.ruleParam.get(index);
                        Intrinsics.checkNotNullExpressionValue((Object)value2, "ruleParam[index]");
                        if (this.isRule(value2)) {
                            final AnalyzeRule this$0 = AnalyzeRule.this;
                            final SourceRule[] array = { null };
                            final int n4 = 0;
                            final AnalyzeRule this$2 = AnalyzeRule.this;
                            final String value3 = this.ruleParam.get(index);
                            Intrinsics.checkNotNullExpressionValue((Object)value3, "ruleParam[index]");
                            array[n4] = this$2.new SourceRule(value3, null, 2, null);
                            final String it2 = AnalyzeRule.getString$default(this$0, CollectionsKt.arrayListOf((Object[])array), null, false, 6, null);
                            final int n5 = 0;
                            infoVal.insert(0, it2);
                        }
                        else {
                            final AnalyzeRule this$3 = AnalyzeRule.this;
                            final String value4 = this.ruleParam.get(index);
                            Intrinsics.checkNotNullExpressionValue((Object)value4, "ruleParam[index]");
                            final Object jsEval = this$3.evalJS(value4, result);
                            if (jsEval == null) {
                                continue;
                            }
                            if (jsEval instanceof String) {
                                infoVal.insert(0, (String)jsEval);
                            }
                            else if (jsEval instanceof Double && ((Number)jsEval).doubleValue() % 1.0 == 0.0) {
                                final StringBuilder sb = infoVal;
                                final int offset = 0;
                                final StringCompanionObject instance2 = StringCompanionObject.INSTANCE;
                                final String s2 = "%.0f";
                                final Object[] array2 = { jsEval };
                                final String format = s2;
                                final Object[] original = array2;
                                final String format2 = String.format(format, Arrays.copyOf(original, original.length));
                                Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
                                sb.insert(offset, format2);
                            }
                            else {
                                infoVal.insert(0, jsEval.toString());
                            }
                        }
                    }
                    else if (regType == this.getRuleType) {
                        final StringBuilder sb2 = infoVal;
                        final int offset2 = 0;
                        final AnalyzeRule this$4 = AnalyzeRule.this;
                        final String value5 = this.ruleParam.get(index);
                        Intrinsics.checkNotNullExpressionValue((Object)value5, "ruleParam[index]");
                        sb2.insert(offset2, this$4.get(value5));
                    }
                    else {
                        infoVal.insert(0, this.ruleParam.get(index));
                    }
                }
                final String string = infoVal.toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "infoVal.toString()");
                this.rule = string;
            }
            final List ruleStrS = StringsKt.split$default((CharSequence)this.rule, new String[] { "##" }, false, 0, 6, (Object)null);
            final String s3 = ruleStrS.get(0);
            if (s3 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            this.rule = StringsKt.trim((CharSequence)s3).toString();
            if (ruleStrS.size() > 1) {
                this.replaceRegex = ruleStrS.get(1);
            }
            if (ruleStrS.size() > 2) {
                this.replacement = ruleStrS.get(2);
            }
            if (ruleStrS.size() > 3) {
                this.replaceFirst = true;
            }
        }
        
        private final boolean isRule(final String ruleStr) {
            return StringsKt.startsWith$default((CharSequence)ruleStr, '@', false, 2, (Object)null) || StringsKt.startsWith$default(ruleStr, "$.", false, 2, (Object)null) || StringsKt.startsWith$default(ruleStr, "$[", false, 2, (Object)null) || StringsKt.startsWith$default(ruleStr, "//", false, 2, (Object)null);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007：\u0006\b" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeRule$Mode;", "", "(Ljava/lang/String;I)V", "XPath", "Json", "Default", "Js", "Regex", "reader-pro" })
    public enum Mode
    {
        XPath, 
        Json, 
        Default, 
        Js, 
        Regex;
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000：\u0006\t" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeRule$Companion;", "", "()V", "evalPattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "putPattern", "regexPattern", "titleNumPattern", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
