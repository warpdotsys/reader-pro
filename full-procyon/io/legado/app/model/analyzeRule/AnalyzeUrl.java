// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import io.legado.app.utils.GsonExtensionsKt;
import kotlin.text.StringsKt;
import java.io.File;
import org.jsoup.Connection$Response;
import kotlin.text.MatchResult;
import okhttp3.ResponseBody;
import io.legado.app.utils.Base64;
import kotlin.text.Regex;
import io.legado.app.constant.AppPattern;
import kotlin.jvm.JvmOverloads;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import java.util.Iterator;
import java.util.List;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request$Builder;
import io.legado.app.help.http.OkHttpUtilsKt;
import kotlin.jvm.functions.Function1;
import io.legado.app.help.http.HttpHelperKt;
import io.legado.app.adapters.ReaderAdapterInterface;
import io.legado.app.adapters.ReaderAdapterHelper;
import io.legado.app.utils.StringUtils;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import io.legado.app.help.http.StrResponse;
import kotlin.coroutines.Continuation;
import kotlin.Unit;
import com.script.Bindings;
import io.legado.app.constant.AppConst;
import io.legado.app.data.entities.Book;
import io.legado.app.help.CacheManager;
import io.legado.app.help.http.CookieStore;
import com.script.SimpleBindings;
import io.legado.app.utils.EncoderUtils;
import java.net.URLEncoder;
import io.legado.app.utils.NetworkUtils;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.regex.Matcher;
import io.legado.app.utils.StringExtensionsKt;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import java.util.regex.Pattern;
import io.legado.app.help.http.RequestMethod;
import java.util.LinkedHashMap;
import java.util.HashMap;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BaseSource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import io.legado.app.help.JsExtensions;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0017\u0018\u0000 l2\u00020\u0001:\u0003lmnB\u008f\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u0012\u0016\b\u0002\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0011\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0013?\u0006\u0002\u0010\u0014J\u0010\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020\u0003H\u0002J\b\u0010C\u001a\u00020AH\u0002J\b\u0010D\u001a\u00020AH\u0002J\u001c\u0010E\u001a\u0004\u0018\u00010F2\u0006\u0010G\u001a\u00020\u00032\n\b\u0002\u0010H\u001a\u0004\u0018\u00010FJ\u0012\u0010I\u001a\u00020A2\b\u0010J\u001a\u0004\u0018\u00010KH\u0002J\n\u0010L\u001a\u0004\u0018\u00010KH\u0002J\u000e\u0010M\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003J\u0006\u0010N\u001a\u00020OJ\u0011\u0010P\u001a\u00020OH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010QJ\n\u0010R\u001a\u0004\u0018\u00010\u0013H\u0016J\u0006\u0010S\u001a\u00020TJ\u0011\u0010U\u001a\u00020TH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010QJ\n\u0010V\u001a\u0004\u0018\u00010\u000bH\u0016J*\u0010W\u001a\u00020X2\n\b\u0002\u0010G\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010Y\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010>\u001a\u00020\"H\u0007J3\u0010Z\u001a\u00020X2\n\b\u0002\u0010G\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010Y\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010>\u001a\u00020\"H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010[J\u0006\u0010\\\u001a\u00020\u0003J\b\u0010]\u001a\u00020\u0003H\u0016J\u0006\u0010^\u001a\u00020AJ\u0006\u0010_\u001a\u00020\"J\u0016\u0010`\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010a\u001a\u00020\u0003J\b\u0010b\u001a\u00020AH\u0002J\u000e\u0010c\u001a\u00020A2\u0006\u0010d\u001a\u00020TJ\u0012\u0010e\u001a\u00020A2\b\u0010f\u001a\u0004\u0018\u00010\u0003H\u0002J)\u0010g\u001a\u00020X2\u0006\u0010h\u001a\u00020\u00032\u0006\u0010i\u001a\u00020F2\u0006\u0010j\u001a\u00020\u0003H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010kR\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\"\u0010\u001a\u001a\u0004\u0018\u00010\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0003@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u0004?\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u000e\u0010!\u001a\u00020\"X\u0082\u0004?\u0006\u0002\n\u0000R*\u0010#\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030$j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`%X\u0082\u0004?\u0006\u0002\n\u0000R-\u0010&\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030'j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`(?\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003?\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0016R\u000e\u0010-\u001a\u00020.X\u0082\u000e?\u0006\u0002\n\u0000R\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0006?\u0006\n\n\u0002\u00101\u001a\u0004\b/\u00100R\u0010\u00102\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u00103\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u0006X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u0004?\u0006\u0002\n\u0000R\u001e\u00105\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b6\u0010\u0016R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004?\u0006\u0002\n\u0000R\u0015\u0010\b\u001a\u0004\u0018\u00010\u0006?\u0006\n\n\u0002\u00101\u001a\u0004\b7\u00100R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003?\u0006\b\n\u0000\u001a\u0004\b8\u0010\u0016R\"\u00109\u001a\u0004\u0018\u00010\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0003@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b:\u0010\u0016R\u001e\u0010;\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b<\u0010\u0016R\u000e\u0010=\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\"X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019：\u0006o" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeUrl;", "Lio/legado/app/help/JsExtensions;", "mUrl", "", "key", "page", "", "speakText", "speakSpeed", "baseUrl", "source", "Lio/legado/app/data/entities/BaseSource;", "ruleData", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "chapter", "Lio/legado/app/data/entities/BookChapter;", "headerMapF", "", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;)V", "getBaseUrl", "()Ljava/lang/String;", "setBaseUrl", "(Ljava/lang/String;)V", "<set-?>", "body", "getBody", "charset", "getDebugLog", "()Lio/legado/app/model/DebugLog;", "setDebugLog", "(Lio/legado/app/model/DebugLog;)V", "enabledCookieJar", "", "fieldMap", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "headerMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getHeaderMap", "()Ljava/util/HashMap;", "getKey", "getMUrl", "method", "Lio/legado/app/help/http/RequestMethod;", "getPage", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "proxy", "queryStr", "retry", "ruleUrl", "getRuleUrl", "getSpeakSpeed", "getSpeakText", "type", "getType", "url", "getUrl", "urlNoQuery", "useWebView", "webJs", "analyzeFields", "", "fieldsTxt", "analyzeJs", "analyzeUrl", "evalJS", "", "jsStr", "result", "fetchEnd", "concurrentRecord", "Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;", "fetchStart", "get", "getByteArray", "", "getByteArrayAwait", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLogger", "getResponse", "Lokhttp3/Response;", "getResponseAwait", "getSource", "getStrResponse", "Lio/legado/app/help/http/StrResponse;", "sourceRegex", "getStrResponseAwait", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserAgent", "getUserNameSpace", "initUrl", "isPost", "put", "value", "replaceKeyPageJs", "saveCookieJar", "response", "setCookie", "tag", "upload", "fileName", "file", "contentType", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "ConcurrentRecord", "UrlOption", "reader-pro" })
public final class AnalyzeUrl implements JsExtensions
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final String mUrl;
    @Nullable
    private final String key;
    @Nullable
    private final Integer page;
    @Nullable
    private final String speakText;
    @Nullable
    private final Integer speakSpeed;
    @NotNull
    private String baseUrl;
    @Nullable
    private final BaseSource source;
    @Nullable
    private final RuleDataInterface ruleData;
    @Nullable
    private final BookChapter chapter;
    @Nullable
    private DebugLog debugLog;
    @NotNull
    private String ruleUrl;
    @NotNull
    private String url;
    @Nullable
    private String body;
    @Nullable
    private String type;
    @NotNull
    private final HashMap<String, String> headerMap;
    @NotNull
    private String urlNoQuery;
    @Nullable
    private String queryStr;
    @NotNull
    private final LinkedHashMap<String, String> fieldMap;
    @Nullable
    private String charset;
    @NotNull
    private RequestMethod method;
    @Nullable
    private String proxy;
    private int retry;
    private boolean useWebView;
    @Nullable
    private String webJs;
    private final boolean enabledCookieJar;
    @NotNull
    private static final Pattern paramPattern;
    private static final Pattern pagePattern;
    @NotNull
    private static final HashMap<String, ConcurrentRecord> concurrentRecordMap;
    
    public AnalyzeUrl(@NotNull final String mUrl, @Nullable final String key, @Nullable final Integer page, @Nullable final String speakText, @Nullable final Integer speakSpeed, @NotNull final String baseUrl, @Nullable final BaseSource source, @Nullable final RuleDataInterface ruleData, @Nullable final BookChapter chapter, @Nullable final Map<String, String> headerMapF, @Nullable final DebugLog debugLog) {
        Intrinsics.checkNotNullParameter((Object)mUrl, "mUrl");
        Intrinsics.checkNotNullParameter((Object)baseUrl, "baseUrl");
        this.mUrl = mUrl;
        this.key = key;
        this.page = page;
        this.speakText = speakText;
        this.speakSpeed = speakSpeed;
        this.baseUrl = baseUrl;
        this.source = source;
        this.ruleData = ruleData;
        this.chapter = chapter;
        this.debugLog = debugLog;
        this.ruleUrl = "";
        this.url = "";
        this.headerMap = new HashMap<String, String>();
        this.urlNoQuery = "";
        this.fieldMap = new LinkedHashMap<String, String>();
        this.method = RequestMethod.GET;
        final BaseSource source2 = this.source;
        boolean enabledCookieJar;
        if (source2 == null) {
            enabledCookieJar = false;
        }
        else {
            final Boolean enabledCookieJar2 = source2.getEnabledCookieJar();
            enabledCookieJar = (enabledCookieJar2 != null && enabledCookieJar2);
        }
        this.enabledCookieJar = enabledCookieJar;
        if (!StringExtensionsKt.isDataUrl(this.mUrl)) {
            final Matcher urlMatcher = AnalyzeUrl.paramPattern.matcher(this.baseUrl);
            if (urlMatcher.find()) {
                final String baseUrl2 = this.baseUrl;
                final int beginIndex = 0;
                final int start = urlMatcher.start();
                final String s = baseUrl2;
                if (s == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring = s.substring(beginIndex, start);
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                this.baseUrl = substring;
            }
            Map<String, String> map;
            if (headerMapF == null) {
                final BaseSource source3 = this.source;
                map = ((source3 == null) ? null : source3.getHeaderMap(true));
            }
            else {
                map = headerMapF;
            }
            final Map<String, String> map2 = map;
            if (map2 != null) {
                final Map it = map2;
                final int n = 0;
                this.getHeaderMap().putAll(it);
                if (it.containsKey("proxy")) {
                    this.proxy = it.get("proxy");
                    this.getHeaderMap().remove("proxy");
                }
            }
            this.initUrl();
        }
    }
    
    @NotNull
    public final String getMUrl() {
        return this.mUrl;
    }
    
    @Nullable
    public final String getKey() {
        return this.key;
    }
    
    @Nullable
    public final Integer getPage() {
        return this.page;
    }
    
    @Nullable
    public final String getSpeakText() {
        return this.speakText;
    }
    
    @Nullable
    public final Integer getSpeakSpeed() {
        return this.speakSpeed;
    }
    
    @NotNull
    public final String getBaseUrl() {
        return this.baseUrl;
    }
    
    public final void setBaseUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.baseUrl = <set-?>;
    }
    
    @Nullable
    public final DebugLog getDebugLog() {
        return this.debugLog;
    }
    
    public final void setDebugLog(@Nullable final DebugLog <set-?>) {
        this.debugLog = <set-?>;
    }
    
    @NotNull
    public final String getRuleUrl() {
        return this.ruleUrl;
    }
    
    @NotNull
    public final String getUrl() {
        return this.url;
    }
    
    @Nullable
    public final String getBody() {
        return this.body;
    }
    
    @Nullable
    public final String getType() {
        return this.type;
    }
    
    @NotNull
    public final HashMap<String, String> getHeaderMap() {
        return this.headerMap;
    }
    
    @NotNull
    @Override
    public String getUserNameSpace() {
        final RuleDataInterface ruleData = this.ruleData;
        String s;
        if (ruleData == null) {
            s = "unknow";
        }
        else {
            final String userNameSpace = ruleData.getUserNameSpace();
            s = ((userNameSpace == null) ? "unknow" : userNameSpace);
        }
        return s;
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
    
    public final void initUrl() {
        this.ruleUrl = this.mUrl;
        this.analyzeJs();
        this.replaceKeyPageJs();
        this.analyzeUrl();
    }
    
    private final void analyzeJs() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_1        /* start */
        //     2: aconst_null    
        //     3: astore_2        /* tmp */
        //     4: getstatic       io/legado/app/constant/AppPattern.INSTANCE:Lio/legado/app/constant/AppPattern;
        //     7: invokevirtual   io/legado/app/constant/AppPattern.getJS_PATTERN:()Ljava/util/regex/Pattern;
        //    10: aload_0         /* this */
        //    11: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //    14: checkcast       Ljava/lang/CharSequence;
        //    17: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //    20: astore_3        /* jsMatcher */
        //    21: aload_3         /* jsMatcher */
        //    22: invokevirtual   java/util/regex/Matcher.find:()Z
        //    25: ifeq            345
        //    28: aload_3         /* jsMatcher */
        //    29: invokevirtual   java/util/regex/Matcher.start:()I
        //    32: iload_1         /* start */
        //    33: if_icmple       268
        //    36: aload_0         /* this */
        //    37: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //    40: astore          4
        //    42: aload_3         /* jsMatcher */
        //    43: invokevirtual   java/util/regex/Matcher.start:()I
        //    46: istore          5
        //    48: iconst_0       
        //    49: istore          6
        //    51: aload           4
        //    53: dup            
        //    54: ifnonnull       67
        //    57: new             Ljava/lang/NullPointerException;
        //    60: dup            
        //    61: ldc             "null cannot be cast to non-null type java.lang.String"
        //    63: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //    66: athrow         
        //    67: iload_1         /* start */
        //    68: iload           5
        //    70: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    73: dup            
        //    74: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //    76: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //    79: astore          4
        //    81: nop            
        //    82: iconst_0       
        //    83: istore          $i$f$trim
        //    85: aload           $this$trim$iv
        //    87: checkcast       Ljava/lang/CharSequence;
        //    90: astore          $this$trim$iv$iv
        //    92: iconst_0       
        //    93: istore          $i$f$trim
        //    95: iconst_0       
        //    96: istore          startIndex$iv$iv
        //    98: aload           $this$trim$iv$iv
        //   100: invokeinterface java/lang/CharSequence.length:()I
        //   105: iconst_1       
        //   106: isub           
        //   107: istore          endIndex$iv$iv
        //   109: iconst_0       
        //   110: istore          startFound$iv$iv
        //   112: iload           startIndex$iv$iv
        //   114: iload           endIndex$iv$iv
        //   116: if_icmpgt       206
        //   119: iload           startFound$iv$iv
        //   121: ifne            129
        //   124: iload           startIndex$iv$iv
        //   126: goto            131
        //   129: iload           endIndex$iv$iv
        //   131: istore          index$iv$iv
        //   133: aload           $this$trim$iv$iv
        //   135: iload           index$iv$iv
        //   137: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   142: istore          it
        //   144: iconst_0       
        //   145: istore          $i$a$-trim-AnalyzeUrl$analyzeJs$1
        //   147: iload           it
        //   149: bipush          32
        //   151: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   154: ifgt            161
        //   157: iconst_1       
        //   158: goto            162
        //   161: iconst_0       
        //   162: istore          match$iv$iv
        //   164: iload           startFound$iv$iv
        //   166: ifne            189
        //   169: iload           match$iv$iv
        //   171: ifne            180
        //   174: iconst_1       
        //   175: istore          startFound$iv$iv
        //   177: goto            203
        //   180: iload           startIndex$iv$iv
        //   182: iconst_1       
        //   183: iadd           
        //   184: istore          startIndex$iv$iv
        //   186: goto            203
        //   189: iload           match$iv$iv
        //   191: ifne            197
        //   194: goto            206
        //   197: iload           endIndex$iv$iv
        //   199: iconst_1       
        //   200: isub           
        //   201: istore          endIndex$iv$iv
        //   203: goto            112
        //   206: aload           $this$trim$iv$iv
        //   208: iload           startIndex$iv$iv
        //   210: iload           endIndex$iv$iv
        //   212: iconst_1       
        //   213: iadd           
        //   214: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   219: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   222: astore_2        /* tmp */
        //   223: aload_2         /* tmp */
        //   224: checkcast       Ljava/lang/CharSequence;
        //   227: astore          4
        //   229: iconst_0       
        //   230: istore          5
        //   232: aload           4
        //   234: invokeinterface java/lang/CharSequence.length:()I
        //   239: ifle            246
        //   242: iconst_1       
        //   243: goto            247
        //   246: iconst_0       
        //   247: ifeq            268
        //   250: aload_0         /* this */
        //   251: aload_2         /* tmp */
        //   252: ldc_w           "@result"
        //   255: aload_0         /* this */
        //   256: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   259: iconst_0       
        //   260: iconst_4       
        //   261: aconst_null    
        //   262: invokestatic    kotlin/text/StringsKt.replace$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
        //   265: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   268: aload_0         /* this */
        //   269: aload_0         /* this */
        //   270: aload_3         /* jsMatcher */
        //   271: iconst_2       
        //   272: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   275: astore          6
        //   277: aload           6
        //   279: ifnonnull       290
        //   282: aload_3         /* jsMatcher */
        //   283: iconst_1       
        //   284: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   287: goto            292
        //   290: aload           6
        //   292: astore          5
        //   294: aload           5
        //   296: ldc_w           "jsMatcher.group(2) ?: jsMatcher.group(1)"
        //   299: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   302: aload           5
        //   304: aload_0         /* this */
        //   305: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   308: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.evalJS:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
        //   311: astore          4
        //   313: aload           4
        //   315: ifnonnull       329
        //   318: new             Ljava/lang/NullPointerException;
        //   321: dup            
        //   322: ldc_w           "null cannot be cast to non-null type kotlin.String"
        //   325: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   328: athrow         
        //   329: aload           4
        //   331: checkcast       Ljava/lang/String;
        //   334: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   337: aload_3         /* jsMatcher */
        //   338: invokevirtual   java/util/regex/Matcher.end:()I
        //   341: istore_1        /* start */
        //   342: goto            21
        //   345: aload_0         /* this */
        //   346: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   349: invokevirtual   java/lang/String.length:()I
        //   352: iload_1         /* start */
        //   353: if_icmple       581
        //   356: aload_0         /* this */
        //   357: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   360: astore          4
        //   362: iconst_0       
        //   363: istore          5
        //   365: aload           4
        //   367: dup            
        //   368: ifnonnull       381
        //   371: new             Ljava/lang/NullPointerException;
        //   374: dup            
        //   375: ldc             "null cannot be cast to non-null type java.lang.String"
        //   377: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   380: athrow         
        //   381: iload_1         /* start */
        //   382: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   385: dup            
        //   386: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   389: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   392: astore          4
        //   394: nop            
        //   395: iconst_0       
        //   396: istore          $i$f$trim
        //   398: aload           $this$trim$iv
        //   400: checkcast       Ljava/lang/CharSequence;
        //   403: astore          $this$trim$iv$iv
        //   405: iconst_0       
        //   406: istore          $i$f$trim
        //   408: iconst_0       
        //   409: istore          startIndex$iv$iv
        //   411: aload           $this$trim$iv$iv
        //   413: invokeinterface java/lang/CharSequence.length:()I
        //   418: iconst_1       
        //   419: isub           
        //   420: istore          endIndex$iv$iv
        //   422: iconst_0       
        //   423: istore          startFound$iv$iv
        //   425: iload           startIndex$iv$iv
        //   427: iload           endIndex$iv$iv
        //   429: if_icmpgt       519
        //   432: iload           startFound$iv$iv
        //   434: ifne            442
        //   437: iload           startIndex$iv$iv
        //   439: goto            444
        //   442: iload           endIndex$iv$iv
        //   444: istore          index$iv$iv
        //   446: aload           $this$trim$iv$iv
        //   448: iload           index$iv$iv
        //   450: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   455: istore          it
        //   457: iconst_0       
        //   458: istore          $i$a$-trim-AnalyzeUrl$analyzeJs$2
        //   460: iload           it
        //   462: bipush          32
        //   464: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   467: ifgt            474
        //   470: iconst_1       
        //   471: goto            475
        //   474: iconst_0       
        //   475: istore          match$iv$iv
        //   477: iload           startFound$iv$iv
        //   479: ifne            502
        //   482: iload           match$iv$iv
        //   484: ifne            493
        //   487: iconst_1       
        //   488: istore          startFound$iv$iv
        //   490: goto            516
        //   493: iload           startIndex$iv$iv
        //   495: iconst_1       
        //   496: iadd           
        //   497: istore          startIndex$iv$iv
        //   499: goto            516
        //   502: iload           match$iv$iv
        //   504: ifne            510
        //   507: goto            519
        //   510: iload           endIndex$iv$iv
        //   512: iconst_1       
        //   513: isub           
        //   514: istore          endIndex$iv$iv
        //   516: goto            425
        //   519: aload           $this$trim$iv$iv
        //   521: iload           startIndex$iv$iv
        //   523: iload           endIndex$iv$iv
        //   525: iconst_1       
        //   526: iadd           
        //   527: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   532: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   535: astore_2        /* tmp */
        //   536: aload_2         /* tmp */
        //   537: checkcast       Ljava/lang/CharSequence;
        //   540: astore          4
        //   542: iconst_0       
        //   543: istore          5
        //   545: aload           4
        //   547: invokeinterface java/lang/CharSequence.length:()I
        //   552: ifle            559
        //   555: iconst_1       
        //   556: goto            560
        //   559: iconst_0       
        //   560: ifeq            581
        //   563: aload_0         /* this */
        //   564: aload_2         /* tmp */
        //   565: ldc_w           "@result"
        //   568: aload_0         /* this */
        //   569: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   572: iconst_0       
        //   573: iconst_4       
        //   574: aconst_null    
        //   575: invokestatic    kotlin/text/StringsKt.replace$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
        //   578: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   581: return         
        //    StackMapTable: 00 21 FE 00 15 01 07 00 91 07 00 81 FF 00 2D 00 07 07 00 02 01 07 00 91 07 00 81 07 00 91 01 01 00 01 07 00 91 FF 00 2C 00 0B 07 00 02 01 07 00 91 07 00 81 07 00 91 01 07 00 79 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 27 00 0B 07 00 02 01 07 00 91 07 00 81 07 00 79 01 07 00 79 01 01 01 01 00 00 40 01 FF 00 14 00 04 07 00 02 01 07 00 91 07 00 81 00 00 FF 00 15 00 07 07 00 02 01 07 00 91 07 00 81 00 00 07 00 91 00 02 07 00 02 07 00 02 FF 00 01 00 07 07 00 02 01 07 00 91 07 00 81 00 00 07 00 91 00 03 07 00 02 07 00 02 07 00 91 FF 00 24 00 07 07 00 02 01 07 00 91 07 00 81 07 00 04 07 00 91 07 00 91 00 01 07 00 02 F8 00 0F FF 00 23 00 06 07 00 02 01 07 00 91 07 00 81 07 00 91 01 00 01 07 00 91 FF 00 2B 00 0B 07 00 02 01 07 00 91 07 00 81 07 00 91 01 07 00 79 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 27 00 0B 07 00 02 01 07 00 91 07 00 81 07 00 79 01 07 00 79 01 01 01 01 00 00 40 01 FF 00 14 00 04 07 00 02 01 07 00 91 07 00 81 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final void replaceKeyPageJs() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //     4: checkcast       Ljava/lang/CharSequence;
        //     7: ldc_w           "{{"
        //    10: checkcast       Ljava/lang/CharSequence;
        //    13: iconst_0       
        //    14: iconst_2       
        //    15: aconst_null    
        //    16: invokestatic    kotlin/text/StringsKt.contains$default:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;ZILjava/lang/Object;)Z
        //    19: ifeq            111
        //    22: aload_0         /* this */
        //    23: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //    26: checkcast       Ljava/lang/CharSequence;
        //    29: ldc_w           "}}"
        //    32: checkcast       Ljava/lang/CharSequence;
        //    35: iconst_0       
        //    36: iconst_2       
        //    37: aconst_null    
        //    38: invokestatic    kotlin/text/StringsKt.contains$default:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;ZILjava/lang/Object;)Z
        //    41: ifeq            111
        //    44: new             Lio/legado/app/model/analyzeRule/RuleAnalyzer;
        //    47: dup            
        //    48: aload_0         /* this */
        //    49: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //    52: iconst_0       
        //    53: iconst_2       
        //    54: aconst_null    
        //    55: invokespecial   io/legado/app/model/analyzeRule/RuleAnalyzer.<init>:(Ljava/lang/String;ZILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //    58: astore_1        /* analyze */
        //    59: aload_1         /* analyze */
        //    60: ldc_w           "{{"
        //    63: ldc_w           "}}"
        //    66: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl$replaceKeyPageJs$url$1;
        //    69: dup            
        //    70: aload_0         /* this */
        //    71: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl$replaceKeyPageJs$url$1.<init>:(Lio/legado/app/model/analyzeRule/AnalyzeUrl;)V
        //    74: checkcast       Lkotlin/jvm/functions/Function1;
        //    77: invokevirtual   io/legado/app/model/analyzeRule/RuleAnalyzer.innerRule:(Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/String;
        //    80: astore_2        /* url */
        //    81: aload_2         /* url */
        //    82: checkcast       Ljava/lang/CharSequence;
        //    85: astore_3       
        //    86: iconst_0       
        //    87: istore          4
        //    89: aload_3        
        //    90: invokeinterface java/lang/CharSequence.length:()I
        //    95: ifle            102
        //    98: iconst_1       
        //    99: goto            103
        //   102: iconst_0       
        //   103: ifeq            111
        //   106: aload_0         /* this */
        //   107: aload_2         /* url */
        //   108: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   111: aload_0         /* this */
        //   112: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.page:Ljava/lang/Integer;
        //   115: astore_1       
        //   116: aload_1        
        //   117: ifnonnull       123
        //   120: goto            629
        //   123: aload_1        
        //   124: astore_2       
        //   125: iconst_0       
        //   126: istore_3       
        //   127: iconst_0       
        //   128: istore          4
        //   130: aload_2        
        //   131: checkcast       Ljava/lang/Number;
        //   134: invokevirtual   java/lang/Number.intValue:()I
        //   137: istore          it
        //   139: iconst_0       
        //   140: istore          $i$a$-let-AnalyzeUrl$replaceKeyPageJs$1
        //   142: getstatic       io/legado/app/model/analyzeRule/AnalyzeUrl.pagePattern:Ljava/util/regex/Pattern;
        //   145: aload_0         /* this */
        //   146: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getRuleUrl:()Ljava/lang/String;
        //   149: checkcast       Ljava/lang/CharSequence;
        //   152: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   155: astore          matcher
        //   157: aload           matcher
        //   159: invokevirtual   java/util/regex/Matcher.find:()Z
        //   162: ifeq            627
        //   165: aload           matcher
        //   167: iconst_1       
        //   168: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   171: dup            
        //   172: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNull:(Ljava/lang/Object;)V
        //   175: checkcast       Ljava/lang/CharSequence;
        //   178: iconst_1       
        //   179: anewarray       Ljava/lang/String;
        //   182: astore          8
        //   184: aload           8
        //   186: iconst_0       
        //   187: ldc_w           ","
        //   190: aastore        
        //   191: aload           8
        //   193: iconst_0       
        //   194: iconst_0       
        //   195: bipush          6
        //   197: aconst_null    
        //   198: invokestatic    kotlin/text/StringsKt.split$default:(Ljava/lang/CharSequence;[Ljava/lang/String;ZIILjava/lang/Object;)Ljava/util/List;
        //   201: astore          pages
        //   203: aload_0         /* this */
        //   204: aload_0         /* this */
        //   205: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getPage:()Ljava/lang/Integer;
        //   208: invokevirtual   java/lang/Integer.intValue:()I
        //   211: aload           pages
        //   213: invokeinterface java/util/List.size:()I
        //   218: if_icmpge       428
        //   221: aload_0         /* this */
        //   222: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getRuleUrl:()Ljava/lang/String;
        //   225: aload           matcher
        //   227: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //   230: astore          8
        //   232: aload           8
        //   234: ldc_w           "matcher.group()"
        //   237: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   240: aload           8
        //   242: aload           pages
        //   244: aload_0         /* this */
        //   245: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getPage:()Ljava/lang/Integer;
        //   248: invokevirtual   java/lang/Integer.intValue:()I
        //   251: iconst_1       
        //   252: isub           
        //   253: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   258: checkcast       Ljava/lang/String;
        //   261: astore          8
        //   263: astore          10
        //   265: astore          11
        //   267: astore          12
        //   269: iconst_0       
        //   270: istore          $i$f$trim
        //   272: aload           $this$trim$iv
        //   274: checkcast       Ljava/lang/CharSequence;
        //   277: astore          $this$trim$iv$iv
        //   279: iconst_0       
        //   280: istore          $i$f$trim
        //   282: iconst_0       
        //   283: istore          startIndex$iv$iv
        //   285: aload           $this$trim$iv$iv
        //   287: invokeinterface java/lang/CharSequence.length:()I
        //   292: iconst_1       
        //   293: isub           
        //   294: istore          endIndex$iv$iv
        //   296: iconst_0       
        //   297: istore          startFound$iv$iv
        //   299: iload           startIndex$iv$iv
        //   301: iload           endIndex$iv$iv
        //   303: if_icmpgt       393
        //   306: iload           startFound$iv$iv
        //   308: ifne            316
        //   311: iload           startIndex$iv$iv
        //   313: goto            318
        //   316: iload           endIndex$iv$iv
        //   318: istore          index$iv$iv
        //   320: aload           $this$trim$iv$iv
        //   322: iload           index$iv$iv
        //   324: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   329: istore          it
        //   331: iconst_0       
        //   332: istore          $i$a$-trim-AnalyzeUrl$replaceKeyPageJs$1$1
        //   334: iload           it
        //   336: bipush          32
        //   338: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   341: ifgt            348
        //   344: iconst_1       
        //   345: goto            349
        //   348: iconst_0       
        //   349: istore          match$iv$iv
        //   351: iload           startFound$iv$iv
        //   353: ifne            376
        //   356: iload           match$iv$iv
        //   358: ifne            367
        //   361: iconst_1       
        //   362: istore          startFound$iv$iv
        //   364: goto            390
        //   367: iload           startIndex$iv$iv
        //   369: iconst_1       
        //   370: iadd           
        //   371: istore          startIndex$iv$iv
        //   373: goto            390
        //   376: iload           match$iv$iv
        //   378: ifne            384
        //   381: goto            393
        //   384: iload           endIndex$iv$iv
        //   386: iconst_1       
        //   387: isub           
        //   388: istore          endIndex$iv$iv
        //   390: goto            299
        //   393: aload           $this$trim$iv$iv
        //   395: iload           startIndex$iv$iv
        //   397: iload           endIndex$iv$iv
        //   399: iconst_1       
        //   400: iadd           
        //   401: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   406: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   409: astore          22
        //   411: aload           12
        //   413: aload           11
        //   415: aload           10
        //   417: aload           22
        //   419: iconst_0       
        //   420: iconst_4       
        //   421: aconst_null    
        //   422: invokestatic    kotlin/text/StringsKt.replace$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
        //   425: goto            621
        //   428: aload_0         /* this */
        //   429: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getRuleUrl:()Ljava/lang/String;
        //   432: aload           matcher
        //   434: invokevirtual   java/util/regex/Matcher.group:()Ljava/lang/String;
        //   437: astore          8
        //   439: aload           8
        //   441: ldc_w           "matcher.group()"
        //   444: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   447: aload           8
        //   449: aload           pages
        //   451: invokestatic    kotlin/collections/CollectionsKt.last:(Ljava/util/List;)Ljava/lang/Object;
        //   454: checkcast       Ljava/lang/String;
        //   457: astore          8
        //   459: astore          10
        //   461: astore          11
        //   463: astore          12
        //   465: iconst_0       
        //   466: istore          $i$f$trim
        //   468: aload           $this$trim$iv
        //   470: checkcast       Ljava/lang/CharSequence;
        //   473: astore          $this$trim$iv$iv
        //   475: iconst_0       
        //   476: istore          $i$f$trim
        //   478: iconst_0       
        //   479: istore          startIndex$iv$iv
        //   481: aload           $this$trim$iv$iv
        //   483: invokeinterface java/lang/CharSequence.length:()I
        //   488: iconst_1       
        //   489: isub           
        //   490: istore          endIndex$iv$iv
        //   492: iconst_0       
        //   493: istore          startFound$iv$iv
        //   495: iload           startIndex$iv$iv
        //   497: iload           endIndex$iv$iv
        //   499: if_icmpgt       589
        //   502: iload           startFound$iv$iv
        //   504: ifne            512
        //   507: iload           startIndex$iv$iv
        //   509: goto            514
        //   512: iload           endIndex$iv$iv
        //   514: istore          index$iv$iv
        //   516: aload           $this$trim$iv$iv
        //   518: iload           index$iv$iv
        //   520: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   525: istore          it
        //   527: iconst_0       
        //   528: istore          $i$a$-trim-AnalyzeUrl$replaceKeyPageJs$1$2
        //   530: iload           it
        //   532: bipush          32
        //   534: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   537: ifgt            544
        //   540: iconst_1       
        //   541: goto            545
        //   544: iconst_0       
        //   545: istore          match$iv$iv
        //   547: iload           startFound$iv$iv
        //   549: ifne            572
        //   552: iload           match$iv$iv
        //   554: ifne            563
        //   557: iconst_1       
        //   558: istore          startFound$iv$iv
        //   560: goto            586
        //   563: iload           startIndex$iv$iv
        //   565: iconst_1       
        //   566: iadd           
        //   567: istore          startIndex$iv$iv
        //   569: goto            586
        //   572: iload           match$iv$iv
        //   574: ifne            580
        //   577: goto            589
        //   580: iload           endIndex$iv$iv
        //   582: iconst_1       
        //   583: isub           
        //   584: istore          endIndex$iv$iv
        //   586: goto            495
        //   589: aload           $this$trim$iv$iv
        //   591: iload           startIndex$iv$iv
        //   593: iload           endIndex$iv$iv
        //   595: iconst_1       
        //   596: iadd           
        //   597: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   602: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   605: astore          22
        //   607: aload           12
        //   609: aload           11
        //   611: aload           10
        //   613: aload           22
        //   615: iconst_0       
        //   616: iconst_4       
        //   617: aconst_null    
        //   618: invokestatic    kotlin/text/StringsKt.replace$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
        //   621: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   624: goto            157
        //   627: nop            
        //   628: nop            
        //   629: return         
        //    StackMapTable: 00 1D FF 00 66 00 05 07 00 02 07 01 47 07 00 91 07 00 79 01 00 00 40 01 FF 00 07 00 01 07 00 02 00 00 FC 00 0B 07 00 C3 FF 00 21 00 08 07 00 02 07 00 C3 07 00 C3 01 01 01 01 07 00 81 00 00 FF 00 8D 00 13 07 00 02 07 00 C3 07 00 C3 01 01 01 01 07 00 81 07 00 91 07 01 6E 07 00 91 07 00 91 07 00 02 01 07 00 79 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 22 00 0A 07 00 02 07 00 C3 07 00 C3 01 01 01 01 07 00 81 07 01 87 07 01 6E 00 01 07 00 02 FF 00 42 00 13 07 00 02 07 00 C3 07 00 C3 01 01 01 01 07 00 81 07 00 91 07 01 6E 07 00 91 07 00 91 07 00 02 01 07 00 79 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 1F 00 17 07 00 02 07 00 C3 07 00 C3 01 01 01 01 07 00 81 07 00 91 07 01 6E 07 00 91 07 00 91 07 00 02 01 07 00 79 01 01 01 01 00 00 00 07 00 91 00 02 07 00 02 07 00 91 FF 00 05 00 08 07 00 02 07 00 C3 07 00 C3 01 01 01 01 07 00 81 00 00 FF 00 01 00 02 07 00 02 07 00 C3 00 00
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
    
    private final void analyzeUrl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: aload_0         /* this */
        //     4: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //     7: checkcast       Ljava/lang/CharSequence;
        //    10: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //    13: astore_1        /* urlMatcher */
        //    14: aload_1         /* urlMatcher */
        //    15: invokevirtual   java/util/regex/Matcher.find:()Z
        //    18: ifeq            69
        //    21: aload_0         /* this */
        //    22: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //    25: astore_3       
        //    26: iconst_0       
        //    27: istore          4
        //    29: aload_1         /* urlMatcher */
        //    30: invokevirtual   java/util/regex/Matcher.start:()I
        //    33: istore          5
        //    35: iconst_0       
        //    36: istore          6
        //    38: aload_3        
        //    39: dup            
        //    40: ifnonnull       53
        //    43: new             Ljava/lang/NullPointerException;
        //    46: dup            
        //    47: ldc             "null cannot be cast to non-null type java.lang.String"
        //    49: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //    52: athrow         
        //    53: iload           4
        //    55: iload           5
        //    57: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    60: dup            
        //    61: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //    63: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //    66: goto            73
        //    69: aload_0         /* this */
        //    70: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //    73: astore_2        /* urlNoOption */
        //    74: aload_0         /* this */
        //    75: getstatic       io/legado/app/utils/NetworkUtils.INSTANCE:Lio/legado/app/utils/NetworkUtils;
        //    78: aload_0         /* this */
        //    79: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.baseUrl:Ljava/lang/String;
        //    82: aload_2         /* urlNoOption */
        //    83: invokevirtual   io/legado/app/utils/NetworkUtils.getAbsoluteURL:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    86: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //    89: getstatic       io/legado/app/utils/NetworkUtils.INSTANCE:Lio/legado/app/utils/NetworkUtils;
        //    92: aload_0         /* this */
        //    93: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //    96: invokevirtual   io/legado/app/utils/NetworkUtils.getBaseUrl:(Ljava/lang/String;)Ljava/lang/String;
        //    99: astore_3       
        //   100: aload_3        
        //   101: ifnonnull       107
        //   104: goto            131
        //   107: aload_3        
        //   108: astore          4
        //   110: iconst_0       
        //   111: istore          5
        //   113: iconst_0       
        //   114: istore          6
        //   116: aload           4
        //   118: astore          it
        //   120: iconst_0       
        //   121: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$1
        //   123: aload_0         /* this */
        //   124: aload           it
        //   126: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.setBaseUrl:(Ljava/lang/String;)V
        //   129: nop            
        //   130: nop            
        //   131: aload_2         /* urlNoOption */
        //   132: invokevirtual   java/lang/String.length:()I
        //   135: aload_0         /* this */
        //   136: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   139: invokevirtual   java/lang/String.length:()I
        //   142: if_icmpeq       698
        //   145: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
        //   148: astore          4
        //   150: aload_0         /* this */
        //   151: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.ruleUrl:Ljava/lang/String;
        //   154: astore          5
        //   156: aload_1         /* urlMatcher */
        //   157: invokevirtual   java/util/regex/Matcher.end:()I
        //   160: istore          6
        //   162: iconst_0       
        //   163: istore          7
        //   165: aload           5
        //   167: dup            
        //   168: ifnonnull       181
        //   171: new             Ljava/lang/NullPointerException;
        //   174: dup            
        //   175: ldc             "null cannot be cast to non-null type java.lang.String"
        //   177: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   180: athrow         
        //   181: iload           6
        //   183: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   186: dup            
        //   187: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   190: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   193: astore          5
        //   195: nop            
        //   196: iconst_0       
        //   197: istore          $i$f$fromJsonObject
        //   199: iconst_0       
        //   200: istore          7
        //   202: nop            
        //   203: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   206: astore          8
        //   208: iconst_0       
        //   209: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
        //   211: aload           $this$fromJsonObject$iv
        //   213: aload           json$iv
        //   215: iconst_0       
        //   216: istore          $i$f$genericType
        //   218: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl$analyzeUrl$$inlined$fromJsonObject$1;
        //   221: dup            
        //   222: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl$analyzeUrl$$inlined$fromJsonObject$1.<init>:()V
        //   225: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$analyzeUrl$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
        //   228: astore          11
        //   230: aload           11
        //   232: ldc_w           "object : TypeToken<T>() {}.type"
        //   235: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   238: aload           11
        //   240: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   243: dup            
        //   244: instanceof      Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption;
        //   247: ifne            252
        //   250: pop            
        //   251: aconst_null    
        //   252: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption;
        //   255: astore          null
        //   257: iconst_0       
        //   258: istore          10
        //   260: aload           9
        //   262: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   265: astore          8
        //   267: goto            290
        //   270: astore          9
        //   272: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
        //   275: astore          10
        //   277: iconst_0       
        //   278: istore          11
        //   280: aload           9
        //   282: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
        //   285: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
        //   288: astore          8
        //   290: aload           8
        //   292: nop            
        //   293: astore          null
        //   295: iconst_0       
        //   296: istore          5
        //   298: aload           4
        //   300: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
        //   303: ifeq            310
        //   306: aconst_null    
        //   307: goto            312
        //   310: aload           4
        //   312: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption;
        //   315: astore_3       
        //   316: aload_3        
        //   317: ifnonnull       323
        //   320: goto            698
        //   323: aload_3        
        //   324: astore          4
        //   326: iconst_0       
        //   327: istore          5
        //   329: iconst_0       
        //   330: istore          6
        //   332: aload           4
        //   334: astore          option
        //   336: iconst_0       
        //   337: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$2
        //   339: aload           option
        //   341: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getMethod:()Ljava/lang/String;
        //   344: astore          9
        //   346: aload           9
        //   348: ifnonnull       354
        //   351: goto            392
        //   354: aload           9
        //   356: astore          10
        //   358: iconst_0       
        //   359: istore          11
        //   361: iconst_0       
        //   362: istore          12
        //   364: aload           10
        //   366: astore          it
        //   368: iconst_0       
        //   369: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$2$1
        //   371: aload           it
        //   373: ldc_w           "POST"
        //   376: iconst_1       
        //   377: invokestatic    kotlin/text/StringsKt.equals:(Ljava/lang/String;Ljava/lang/String;Z)Z
        //   380: ifeq            390
        //   383: aload_0         /* this */
        //   384: getstatic       io/legado/app/help/http/RequestMethod.POST:Lio/legado/app/help/http/RequestMethod;
        //   387: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.method:Lio/legado/app/help/http/RequestMethod;
        //   390: nop            
        //   391: nop            
        //   392: aload           option
        //   394: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getHeaderMap:()Ljava/util/Map;
        //   397: astore          9
        //   399: aload           9
        //   401: ifnonnull       407
        //   404: goto            517
        //   407: aload           9
        //   409: astore          $this$forEach$iv
        //   411: iconst_0       
        //   412: istore          $i$f$forEach
        //   414: aload           $this$forEach$iv
        //   416: astore          12
        //   418: iconst_0       
        //   419: istore          13
        //   421: aload           12
        //   423: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   428: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   433: astore          14
        //   435: aload           14
        //   437: invokeinterface java/util/Iterator.hasNext:()Z
        //   442: ifeq            516
        //   445: aload           14
        //   447: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   452: checkcast       Ljava/util/Map$Entry;
        //   455: astore          element$iv
        //   457: aload           element$iv
        //   459: astore          entry
        //   461: iconst_0       
        //   462: istore          $i$a$-forEach-AnalyzeUrl$analyzeUrl$2$2
        //   464: aload_0         /* this */
        //   465: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getHeaderMap:()Ljava/util/HashMap;
        //   468: checkcast       Ljava/util/Map;
        //   471: astore          18
        //   473: aload           entry
        //   475: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   480: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //   483: astore          19
        //   485: aload           entry
        //   487: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   492: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //   495: astore          20
        //   497: iconst_0       
        //   498: istore          21
        //   500: aload           18
        //   502: aload           19
        //   504: aload           20
        //   506: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   511: pop            
        //   512: nop            
        //   513: goto            435
        //   516: nop            
        //   517: aload           option
        //   519: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getBody:()Ljava/lang/String;
        //   522: astore          9
        //   524: aload           9
        //   526: ifnonnull       532
        //   529: goto            557
        //   532: aload           9
        //   534: astore          10
        //   536: iconst_0       
        //   537: istore          11
        //   539: iconst_0       
        //   540: istore          12
        //   542: aload           10
        //   544: astore          it
        //   546: iconst_0       
        //   547: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$2$3
        //   549: aload_0         /* this */
        //   550: aload           it
        //   552: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.body:Ljava/lang/String;
        //   555: nop            
        //   556: nop            
        //   557: aload_0         /* this */
        //   558: aload           option
        //   560: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getType:()Ljava/lang/String;
        //   563: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.type:Ljava/lang/String;
        //   566: aload_0         /* this */
        //   567: aload           option
        //   569: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getCharset:()Ljava/lang/String;
        //   572: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.charset:Ljava/lang/String;
        //   575: aload_0         /* this */
        //   576: aload           option
        //   578: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getRetry:()I
        //   581: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.retry:I
        //   584: aload_0         /* this */
        //   585: aload           option
        //   587: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.useWebView:()Z
        //   590: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.useWebView:Z
        //   593: aload_0         /* this */
        //   594: aload           option
        //   596: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getWebJs:()Ljava/lang/String;
        //   599: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.webJs:Ljava/lang/String;
        //   602: aload           option
        //   604: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.getJs:()Ljava/lang/String;
        //   607: astore          9
        //   609: aload           9
        //   611: ifnonnull       617
        //   614: goto            696
        //   617: aload           9
        //   619: astore          10
        //   621: iconst_0       
        //   622: istore          11
        //   624: iconst_0       
        //   625: istore          12
        //   627: aload           10
        //   629: astore          jsStr
        //   631: iconst_0       
        //   632: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$2$4
        //   634: aload_0         /* this */
        //   635: aload           jsStr
        //   637: aload_0         /* this */
        //   638: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getUrl:()Ljava/lang/String;
        //   641: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.evalJS:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
        //   644: astore          15
        //   646: aload           15
        //   648: ifnonnull       654
        //   651: goto            694
        //   654: aload           15
        //   656: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   659: astore          16
        //   661: aload           16
        //   663: ifnonnull       669
        //   666: goto            694
        //   669: aload           16
        //   671: astore          17
        //   673: iconst_0       
        //   674: istore          18
        //   676: iconst_0       
        //   677: istore          19
        //   679: aload           17
        //   681: astore          it
        //   683: iconst_0       
        //   684: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$2$4$1
        //   686: aload_0         /* this */
        //   687: aload           it
        //   689: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //   692: nop            
        //   693: nop            
        //   694: nop            
        //   695: nop            
        //   696: nop            
        //   697: nop            
        //   698: aload_0         /* this */
        //   699: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.headerMap:Ljava/util/HashMap;
        //   702: ldc_w           "User-Agent"
        //   705: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   708: checkcast       Ljava/lang/String;
        //   711: astore_3       
        //   712: aload_3        
        //   713: ifnonnull       776
        //   716: aload_0         /* this */
        //   717: astore          4
        //   719: iconst_0       
        //   720: istore          5
        //   722: iconst_0       
        //   723: istore          6
        //   725: aload           4
        //   727: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl;
        //   730: astore          it
        //   732: iconst_0       
        //   733: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$3
        //   735: aload_0         /* this */
        //   736: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getHeaderMap:()Ljava/util/HashMap;
        //   739: checkcast       Ljava/util/Map;
        //   742: astore          9
        //   744: ldc_w           "User-Agent"
        //   747: astore          10
        //   749: getstatic       io/legado/app/constant/AppConst.INSTANCE:Lio/legado/app/constant/AppConst;
        //   752: invokevirtual   io/legado/app/constant/AppConst.getUserAgent:()Ljava/lang/String;
        //   755: astore          11
        //   757: iconst_0       
        //   758: istore          12
        //   760: aload           9
        //   762: aload           10
        //   764: aload           11
        //   766: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   771: pop            
        //   772: nop            
        //   773: goto            777
        //   776: nop            
        //   777: aload_0         /* this */
        //   778: aload_0         /* this */
        //   779: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //   782: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.urlNoQuery:Ljava/lang/String;
        //   785: aload_0         /* this */
        //   786: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.method:Lio/legado/app/help/http/RequestMethod;
        //   789: astore_3       
        //   790: getstatic       io/legado/app/model/analyzeRule/AnalyzeUrl$WhenMappings.$EnumSwitchMapping$0:[I
        //   793: aload_3        
        //   794: invokevirtual   io/legado/app/help/http/RequestMethod.ordinal:()I
        //   797: iaload         
        //   798: istore          4
        //   800: iload           4
        //   802: tableswitch {
        //                2: 824
        //                3: 944
        //          default: 1043
        //        }
        //   824: aload_0         /* this */
        //   825: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //   828: checkcast       Ljava/lang/CharSequence;
        //   831: bipush          63
        //   833: iconst_0       
        //   834: iconst_0       
        //   835: bipush          6
        //   837: aconst_null    
        //   838: invokestatic    kotlin/text/StringsKt.indexOf$default:(Ljava/lang/CharSequence;CIZILjava/lang/Object;)I
        //   841: istore          pos
        //   843: iload           pos
        //   845: iconst_m1      
        //   846: if_icmpeq       1043
        //   849: aload_0         /* this */
        //   850: aload_0         /* this */
        //   851: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //   854: astore          6
        //   856: iload           pos
        //   858: iconst_1       
        //   859: iadd           
        //   860: istore          7
        //   862: iconst_0       
        //   863: istore          8
        //   865: aload           6
        //   867: dup            
        //   868: ifnonnull       881
        //   871: new             Ljava/lang/NullPointerException;
        //   874: dup            
        //   875: ldc             "null cannot be cast to non-null type java.lang.String"
        //   877: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   880: athrow         
        //   881: iload           7
        //   883: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   886: dup            
        //   887: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   890: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   893: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl.analyzeFields:(Ljava/lang/String;)V
        //   896: aload_0         /* this */
        //   897: aload_0         /* this */
        //   898: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.url:Ljava/lang/String;
        //   901: astore          6
        //   903: iconst_0       
        //   904: istore          7
        //   906: iconst_0       
        //   907: istore          8
        //   909: aload           6
        //   911: dup            
        //   912: ifnonnull       925
        //   915: new             Ljava/lang/NullPointerException;
        //   918: dup            
        //   919: ldc             "null cannot be cast to non-null type java.lang.String"
        //   921: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   924: athrow         
        //   925: iload           7
        //   927: iload           pos
        //   929: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   932: dup            
        //   933: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   935: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   938: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl.urlNoQuery:Ljava/lang/String;
        //   941: goto            1043
        //   944: aload_0         /* this */
        //   945: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.body:Ljava/lang/String;
        //   948: astore          5
        //   950: aload           5
        //   952: ifnonnull       958
        //   955: goto            1043
        //   958: aload           5
        //   960: astore          6
        //   962: iconst_0       
        //   963: istore          7
        //   965: iconst_0       
        //   966: istore          8
        //   968: aload           6
        //   970: astore          it
        //   972: iconst_0       
        //   973: istore          $i$a$-let-AnalyzeUrl$analyzeUrl$4
        //   975: aload           it
        //   977: invokestatic    io/legado/app/utils/StringExtensionsKt.isJson:(Ljava/lang/String;)Z
        //   980: ifne            1041
        //   983: aload           it
        //   985: invokestatic    io/legado/app/utils/StringExtensionsKt.isXml:(Ljava/lang/String;)Z
        //   988: ifne            1041
        //   991: aload_0         /* this */
        //   992: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl.getHeaderMap:()Ljava/util/HashMap;
        //   995: ldc_w           "Content-Type"
        //   998: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  1001: checkcast       Ljava/lang/CharSequence;
        //  1004: astore          11
        //  1006: iconst_0       
        //  1007: istore          12
        //  1009: iconst_0       
        //  1010: istore          13
        //  1012: aload           11
        //  1014: ifnull          1027
        //  1017: aload           11
        //  1019: invokeinterface java/lang/CharSequence.length:()I
        //  1024: ifne            1031
        //  1027: iconst_1       
        //  1028: goto            1032
        //  1031: iconst_0       
        //  1032: ifeq            1041
        //  1035: aload_0         /* this */
        //  1036: aload           it
        //  1038: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl.analyzeFields:(Ljava/lang/String;)V
        //  1041: nop            
        //  1042: nop            
        //  1043: return         
        //    StackMapTable: 00 27 FF 00 35 00 07 07 00 02 07 00 81 00 07 00 91 01 01 01 00 01 07 00 91 FF 00 0F 00 02 07 00 02 07 00 81 00 00 43 07 00 91 FD 00 21 07 00 91 07 00 91 17 FF 00 31 00 08 07 00 02 07 00 81 07 00 91 07 00 91 07 01 AD 07 00 91 01 01 00 01 07 00 91 FF 00 46 00 0C 07 00 02 07 00 81 07 00 91 07 00 91 07 01 AD 07 00 91 01 01 07 02 4D 01 01 07 02 4F 00 01 07 00 04 FF 00 11 00 08 07 00 02 07 00 81 07 00 91 07 00 91 07 01 AD 07 00 91 01 01 00 01 07 01 89 FD 00 13 07 00 04 07 00 04 FF 00 13 00 0A 07 00 02 07 00 81 07 00 91 07 00 91 07 00 04 01 01 01 07 00 04 07 00 04 00 00 41 07 00 04 FF 00 0A 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 00 04 01 01 01 07 00 04 07 00 04 00 00 FF 00 1E 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 91 00 00 FF 00 23 00 0F 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 91 07 00 91 01 01 07 00 91 01 00 00 FF 00 01 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 91 00 00 FF 00 0E 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 A0 00 00 FF 00 1B 00 0F 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 A0 07 00 A0 01 07 00 A0 01 07 01 D9 00 00 FB 00 50 FF 00 00 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 A0 00 00 FF 00 0E 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 91 00 00 18 3B FF 00 24 00 10 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 91 07 00 91 01 01 07 00 91 01 07 00 04 00 00 FC 00 0E 07 00 91 FA 00 18 FF 00 01 00 0A 07 00 02 07 00 81 07 00 91 07 01 B3 07 01 B3 01 01 07 01 B3 01 07 00 91 00 00 FF 00 01 00 04 07 00 02 07 00 81 07 00 91 07 00 04 00 00 FF 00 4D 00 04 07 00 02 07 00 81 07 00 91 07 00 91 00 00 00 FF 00 2E 00 05 07 00 02 07 00 81 07 00 91 07 00 56 01 00 00 FF 00 38 00 09 07 00 02 07 00 81 07 00 91 07 00 56 01 01 07 00 91 01 01 00 02 07 00 02 07 00 91 FF 00 2B 00 09 07 00 02 07 00 81 07 00 91 07 00 56 01 01 07 00 91 01 01 00 02 07 00 02 07 00 91 FF 00 12 00 05 07 00 02 07 00 81 07 00 91 07 00 56 01 00 00 FC 00 0D 07 00 91 FF 00 44 00 0E 07 00 02 07 00 81 07 00 91 07 00 56 01 07 00 91 07 00 91 01 01 07 00 91 01 07 00 79 01 01 00 00 03 40 01 F8 00 08 FF 00 01 00 05 07 00 02 07 00 81 07 00 91 07 00 56 01 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  202    267    270    290    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private final void analyzeFields(final String fieldsTxt) {
        this.queryStr = fieldsTxt;
        final String[] splitNotBlank;
        final String[] queryS = splitNotBlank = StringExtensionsKt.splitNotBlank(fieldsTxt, "&");
        int i = 0;
        while (i < splitNotBlank.length) {
            final String query = splitNotBlank[i];
            ++i;
            final String[] queryM = StringExtensionsKt.splitNotBlank(query, "=");
            final String value = (queryM.length > 1) ? queryM[1] : "";
            final CharSequence charSequence = this.charset;
            if (charSequence == null || charSequence.length() == 0) {
                if (NetworkUtils.INSTANCE.hasUrlEncoded(value)) {
                    this.fieldMap.put(queryM[0], value);
                }
                else {
                    final Map map = this.fieldMap;
                    final String s = queryM[0];
                    final String encode = URLEncoder.encode(value, "UTF-8");
                    Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(value, \"UTF-8\")");
                    map.put(s, encode);
                }
            }
            else if (Intrinsics.areEqual((Object)this.charset, (Object)"escape")) {
                this.fieldMap.put(queryM[0], EncoderUtils.INSTANCE.escape(value));
            }
            else {
                final Map map2 = this.fieldMap;
                final String s2 = queryM[0];
                final String encode2 = URLEncoder.encode(value, this.charset);
                Intrinsics.checkNotNullExpressionValue((Object)encode2, "encode(value, charset)");
                map2.put(s2, encode2);
            }
        }
    }
    
    @Nullable
    public final Object evalJS(@NotNull final String jsStr, @Nullable final Object result) {
        Intrinsics.checkNotNullParameter((Object)jsStr, "jsStr");
        final SimpleBindings bindings = new SimpleBindings();
        ((Map)bindings).put("java", this);
        ((Map)bindings).put("baseUrl", this.baseUrl);
        ((Map)bindings).put("cookie", new CookieStore(this.getUserNameSpace()));
        ((Map)bindings).put("cache", new CacheManager(this.getUserNameSpace()));
        ((Map)bindings).put("page", this.page);
        ((Map)bindings).put("key", this.key);
        ((Map)bindings).put("speakText", this.speakText);
        ((Map)bindings).put("speakSpeed", this.speakSpeed);
        final Map map = (Map)bindings;
        final String s = "book";
        final RuleDataInterface ruleData = this.ruleData;
        map.put(s, (ruleData instanceof Book) ? ((Book)ruleData) : null);
        ((Map)bindings).put("source", this.source);
        ((Map)bindings).put("result", result);
        return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, (Bindings)bindings);
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
        if (instance == null) {
            final RuleDataInterface ruleData = this.ruleData;
            if (ruleData != null) {
                ruleData.putVariable(key, value);
            }
        }
        return value;
    }
    
    @NotNull
    public final String get(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (Intrinsics.areEqual((Object)key, (Object)"bookName")) {
            final RuleDataInterface ruleData = this.ruleData;
            final Book book = (ruleData instanceof Book) ? ((Book)ruleData) : null;
            if (book != null) {
                final Book it = book;
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
        String s2;
        if (s == null) {
            final RuleDataInterface ruleData2 = this.ruleData;
            if (ruleData2 == null) {
                s2 = "";
            }
            else {
                final String variable = ruleData2.getVariable(key);
                s2 = ((variable == null) ? "" : variable);
            }
        }
        else {
            s2 = s;
        }
        return s2;
    }
    
    private final ConcurrentRecord fetchStart() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.source:Lio/legado/app/data/entities/BaseSource;
        //     4: astore_1       
        //     5: aload_1        
        //     6: ifnonnull       11
        //     9: aconst_null    
        //    10: areturn        
        //    11: aload_0         /* this */
        //    12: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.source:Lio/legado/app/data/entities/BaseSource;
        //    15: invokeinterface io/legado/app/data/entities/BaseSource.getConcurrentRate:()Ljava/lang/String;
        //    20: astore_1        /* concurrentRate */
        //    21: aload_1         /* concurrentRate */
        //    22: checkcast       Ljava/lang/CharSequence;
        //    25: astore_2       
        //    26: iconst_0       
        //    27: istore_3       
        //    28: iconst_0       
        //    29: istore          4
        //    31: aload_2        
        //    32: ifnull          44
        //    35: aload_2        
        //    36: invokeinterface java/lang/CharSequence.length:()I
        //    41: ifne            48
        //    44: iconst_1       
        //    45: goto            49
        //    48: iconst_0       
        //    49: ifeq            54
        //    52: aconst_null    
        //    53: areturn        
        //    54: aload_1         /* concurrentRate */
        //    55: checkcast       Ljava/lang/CharSequence;
        //    58: ldc_w           "/"
        //    61: iconst_0       
        //    62: iconst_0       
        //    63: bipush          6
        //    65: aconst_null    
        //    66: invokestatic    kotlin/text/StringsKt.indexOf$default:(Ljava/lang/CharSequence;Ljava/lang/String;IZILjava/lang/Object;)I
        //    69: istore_2        /* rateIndex */
        //    70: aconst_null    
        //    71: astore_3        /* fetchRecord */
        //    72: getstatic       io/legado/app/model/analyzeRule/AnalyzeUrl.concurrentRecordMap:Ljava/util/HashMap;
        //    75: aload_0         /* this */
        //    76: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.source:Lio/legado/app/data/entities/BaseSource;
        //    79: invokeinterface io/legado/app/data/entities/BaseSource.getKey:()Ljava/lang/String;
        //    84: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    87: astore_3        /* fetchRecord */
        //    88: aload_3         /* fetchRecord */
        //    89: ifnonnull       152
        //    92: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //    95: dup            
        //    96: iload_2         /* rateIndex */
        //    97: ifle            104
        //   100: iconst_1       
        //   101: goto            105
        //   104: iconst_0       
        //   105: invokestatic    java/lang/System.currentTimeMillis:()J
        //   108: iconst_1       
        //   109: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.<init>:(ZJI)V
        //   112: astore_3        /* fetchRecord */
        //   113: getstatic       io/legado/app/model/analyzeRule/AnalyzeUrl.concurrentRecordMap:Ljava/util/HashMap;
        //   116: checkcast       Ljava/util/Map;
        //   119: astore          4
        //   121: aload_0         /* this */
        //   122: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl.source:Lio/legado/app/data/entities/BaseSource;
        //   125: invokeinterface io/legado/app/data/entities/BaseSource.getKey:()Ljava/lang/String;
        //   130: astore          5
        //   132: aload_3         /* fetchRecord */
        //   133: astore          6
        //   135: iconst_0       
        //   136: istore          7
        //   138: aload           4
        //   140: aload           5
        //   142: aload           6
        //   144: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   149: pop            
        //   150: aload_3         /* fetchRecord */
        //   151: areturn        
        //   152: aload_3         /* fetchRecord */
        //   153: astore          5
        //   155: iconst_0       
        //   156: istore          6
        //   158: iconst_0       
        //   159: istore          7
        //   161: aload           5
        //   163: monitorenter   
        //   164: nop            
        //   165: iconst_0       
        //   166: istore          $i$a$-synchronized-AnalyzeUrl$fetchStart$waitTime$1
        //   168: nop            
        //   169: iload_2         /* rateIndex */
        //   170: iconst_m1      
        //   171: if_icmpne       261
        //   174: aload_3         /* fetchRecord */
        //   175: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   178: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.getFrequency:()I
        //   181: ifle            198
        //   184: aload_1         /* concurrentRate */
        //   185: astore          9
        //   187: iconst_0       
        //   188: istore          10
        //   190: aload           9
        //   192: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   195: goto            450
        //   198: aload_3         /* fetchRecord */
        //   199: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   202: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.getTime:()J
        //   205: aload_1         /* concurrentRate */
        //   206: astore          11
        //   208: iconst_0       
        //   209: istore          12
        //   211: aload           11
        //   213: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   216: i2l            
        //   217: ladd           
        //   218: lstore          nextTime
        //   220: invokestatic    java/lang/System.currentTimeMillis:()J
        //   223: lload           nextTime
        //   225: lcmp           
        //   226: iflt            251
        //   229: aload_3         /* fetchRecord */
        //   230: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   233: invokestatic    java/lang/System.currentTimeMillis:()J
        //   236: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.setTime:(J)V
        //   239: aload_3         /* fetchRecord */
        //   240: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   243: iconst_1       
        //   244: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.setFrequency:(I)V
        //   247: iconst_0       
        //   248: goto            450
        //   251: lload           nextTime
        //   253: invokestatic    java/lang/System.currentTimeMillis:()J
        //   256: lsub           
        //   257: l2i            
        //   258: goto            450
        //   261: aload_1         /* concurrentRate */
        //   262: astore          10
        //   264: iload_2         /* rateIndex */
        //   265: iconst_1       
        //   266: iadd           
        //   267: istore          11
        //   269: iconst_0       
        //   270: istore          12
        //   272: aload           10
        //   274: dup            
        //   275: ifnonnull       288
        //   278: new             Ljava/lang/NullPointerException;
        //   281: dup            
        //   282: ldc             "null cannot be cast to non-null type java.lang.String"
        //   284: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   287: athrow         
        //   288: iload           11
        //   290: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   293: dup            
        //   294: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   297: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   300: astore          sj
        //   302: aload_3         /* fetchRecord */
        //   303: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   306: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.getTime:()J
        //   309: aload           sj
        //   311: astore          12
        //   313: iconst_0       
        //   314: istore          15
        //   316: aload           12
        //   318: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   321: i2l            
        //   322: ladd           
        //   323: lstore          nextTime
        //   325: invokestatic    java/lang/System.currentTimeMillis:()J
        //   328: lload           nextTime
        //   330: lcmp           
        //   331: iflt            356
        //   334: aload_3         /* fetchRecord */
        //   335: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   338: invokestatic    java/lang/System.currentTimeMillis:()J
        //   341: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.setTime:(J)V
        //   344: aload_3         /* fetchRecord */
        //   345: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   348: iconst_1       
        //   349: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.setFrequency:(I)V
        //   352: iconst_0       
        //   353: goto            450
        //   356: aload_1         /* concurrentRate */
        //   357: astore          15
        //   359: iconst_0       
        //   360: istore          18
        //   362: iconst_0       
        //   363: istore          19
        //   365: aload           15
        //   367: dup            
        //   368: ifnonnull       381
        //   371: new             Ljava/lang/NullPointerException;
        //   374: dup            
        //   375: ldc             "null cannot be cast to non-null type java.lang.String"
        //   377: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   380: athrow         
        //   381: iload           18
        //   383: iload_2         /* rateIndex */
        //   384: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   387: dup            
        //   388: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
        //   390: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   393: astore          cs
        //   395: aload_3         /* fetchRecord */
        //   396: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   399: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.getFrequency:()I
        //   402: aload           cs
        //   404: astore          15
        //   406: iconst_0       
        //   407: istore          18
        //   409: aload           15
        //   411: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   414: if_icmple       427
        //   417: lload           nextTime
        //   419: invokestatic    java/lang/System.currentTimeMillis:()J
        //   422: lsub           
        //   423: l2i            
        //   424: goto            450
        //   427: aload_3         /* fetchRecord */
        //   428: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   431: aload_3         /* fetchRecord */
        //   432: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   435: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.getFrequency:()I
        //   438: iconst_1       
        //   439: iadd           
        //   440: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord.setFrequency:(I)V
        //   443: iconst_0       
        //   444: goto            450
        //   447: astore          e
        //   449: iconst_0       
        //   450: istore          7
        //   452: aload           5
        //   454: monitorexit    
        //   455: iload           7
        //   457: goto            468
        //   460: astore          7
        //   462: aload           5
        //   464: monitorexit    
        //   465: aload           7
        //   467: athrow         
        //   468: istore          waitTime
        //   470: iload           waitTime
        //   472: ifle            512
        //   475: new             Lio/legado/app/exception/ConcurrentException;
        //   478: dup            
        //   479: new             Ljava/lang/StringBuilder;
        //   482: dup            
        //   483: invokespecial   java/lang/StringBuilder.<init>:()V
        //   486: ldc_w           "\u6839\u636e\u5e76\u53d1\u7387\u8fd8\u9700\u7b49\u5f85"
        //   489: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   492: iload           waitTime
        //   494: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   497: ldc_w           "\u6beb\u79d2\u624d\u53ef\u4ee5\u8bbf\u95ee"
        //   500: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   503: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   506: iload           waitTime
        //   508: invokespecial   io/legado/app/exception/ConcurrentException.<init>:(Ljava/lang/String;I)V
        //   511: athrow         
        //   512: aload_3         /* fetchRecord */
        //   513: checkcast       Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;
        //   516: areturn        
        //    StackMapTable: 00 14 FC 00 0B 07 00 5F FF 00 20 00 05 07 00 02 07 00 91 07 00 79 01 01 00 00 03 40 01 04 FF 00 31 00 05 07 00 02 07 00 91 01 07 00 04 01 00 02 08 00 5C 08 00 5C FF 00 00 00 05 07 00 02 07 00 91 01 07 00 04 01 00 03 08 00 5C 08 00 5C 01 2E FF 00 2D 00 09 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 00 00 FF 00 34 00 0E 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 00 00 07 00 91 01 04 00 00 FF 00 09 00 09 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 00 00 FF 00 1A 00 0D 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 00 07 00 91 01 01 00 01 07 00 91 FF 00 43 00 11 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 07 00 91 07 00 91 01 07 00 91 00 00 01 04 00 00 FF 00 18 00 13 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 07 00 91 07 00 91 01 07 00 91 00 00 07 00 91 04 01 01 00 01 07 00 91 2D FF 00 13 00 09 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 00 01 07 02 C7 42 01 FF 00 09 00 07 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 00 01 07 01 89 FF 00 07 00 09 07 00 02 07 00 91 01 07 00 04 01 07 00 04 01 01 01 00 01 01 2B
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  168    447    447    450    Ljava/lang/Exception;
        //  164    452    460    468    Any
        //  460    462    460    468    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:361)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:504)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visitVariable(StackMappingVisitor.java:474)
        //     at com.strobel.assembler.ir.Instruction.accept(Instruction.java:553)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:403)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
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
    
    private final void fetchEnd(final ConcurrentRecord concurrentRecord) {
        if (concurrentRecord != null && !concurrentRecord.getConcurrent()) {
            synchronized (concurrentRecord) {
                final int n = 0;
                concurrentRecord.setFrequency(concurrentRecord.getFrequency() - 1);
                final Unit instance = Unit.INSTANCE;
            }
        }
    }
    
    @Nullable
    public final Object getStrResponseAwait(@Nullable final String jsStr, @Nullable final String sourceRegex, final boolean useWebView, @NotNull final Continuation<? super StrResponse> $completion) {
        final Continuation $continuation;
        Label_0055: {
            if ($completion instanceof AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1) {
                final AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1 analyzeUrl$getStrResponseAwait$1 = (AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$completion;
                if ((analyzeUrl$getStrResponseAwait$1.label & Integer.MIN_VALUE) != 0x0) {
                    final AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1 analyzeUrl$getStrResponseAwait$2 = analyzeUrl$getStrResponseAwait$1;
                    analyzeUrl$getStrResponseAwait$2.label -= Integer.MIN_VALUE;
                    break Label_0055;
                }
            }
            $continuation = (Continuation)new AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1(this, (Continuation)$completion);
        }
        final Object $result = ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ConcurrentRecord concurrentRecord = null;
        final StrResponse strResponse;
        Label_0726: {
            Object callStrResponse = null;
            Label_0712: {
                Label_0622: {
                    Object strResponseByRemoteWebview$default2 = null;
                    Label_0619: {
                        Object strResponseByRemoteWebview$default = null;
                        Label_0456: {
                            StringUtils instance = null;
                            String s = null;
                            Object byteArrayAwait = null;
                            switch (((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).label) {
                                case 0: {
                                    ResultKt.throwOnFailure($result);
                                    if (this.getType() != null) {
                                        final String url = this.getUrl();
                                        instance = StringUtils.INSTANCE;
                                        s = url;
                                        final AnalyzeUrl analyzeUrl = this;
                                        final Continuation $completion2 = $continuation;
                                        ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0 = s;
                                        ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1 = instance;
                                        ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).label = 1;
                                        if ((byteArrayAwait = analyzeUrl.getByteArrayAwait((Continuation<? super byte[]>)$completion2)) == coroutine_SUSPENDED) {
                                            return coroutine_SUSPENDED;
                                        }
                                        break;
                                    }
                                    else {
                                        concurrentRecord = this.fetchStart();
                                        final AnalyzeUrl analyzeUrl2 = this;
                                        final BaseSource source = this.source;
                                        analyzeUrl2.setCookie((source == null) ? null : source.getKey());
                                        if (this.useWebView && useWebView) {
                                            if (WhenMappings.$EnumSwitchMapping$0[this.method.ordinal()] == 2) {
                                                final String urlNoQuery = this.urlNoQuery;
                                                final BaseSource source2 = this.source;
                                                final String s2 = (source2 == null) ? null : source2.getKey();
                                                final String webJs = this.webJs;
                                                final String s3 = (webJs == null) ? jsStr : webJs;
                                                final HashMap<String, String> headerMap = this.getHeaderMap();
                                                final String body = this.getBody();
                                                final String userNameSpace = this.getUserNameSpace();
                                                final DebugLog debugLog = this.getDebugLog();
                                                final ReaderAdapterInterface adapter = ReaderAdapterHelper.INSTANCE.getAdapter();
                                                final String s4 = urlNoQuery;
                                                final String s5 = null;
                                                final String s6 = null;
                                                final String s7 = s2;
                                                final Map map = headerMap;
                                                final String s8 = s3;
                                                final String s9 = null;
                                                final boolean b = true;
                                                final String s10 = body;
                                                final String s11 = userNameSpace;
                                                final DebugLog debugLog2 = debugLog;
                                                final Continuation continuation = $continuation;
                                                final int n = 134;
                                                final Object o = null;
                                                ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0 = this;
                                                ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1 = concurrentRecord;
                                                ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).label = 2;
                                                if ((strResponseByRemoteWebview$default = ReaderAdapterInterface.DefaultImpls.getStrResponseByRemoteWebview$default(adapter, s4, s5, s6, s7, map, sourceRegex, s8, s9, b, s10, s11, debugLog2, continuation, n, o)) == coroutine_SUSPENDED) {
                                                    return coroutine_SUSPENDED;
                                                }
                                                break Label_0456;
                                            }
                                            else {
                                                final String url2 = this.getUrl();
                                                final BaseSource source3 = this.source;
                                                final String s12 = (source3 == null) ? null : source3.getKey();
                                                final String webJs2 = this.webJs;
                                                final String s13 = (webJs2 == null) ? jsStr : webJs2;
                                                final HashMap<String, String> headerMap2 = this.getHeaderMap();
                                                final String userNameSpace2 = this.getUserNameSpace();
                                                final DebugLog debugLog3 = this.getDebugLog();
                                                final ReaderAdapterInterface adapter2 = ReaderAdapterHelper.INSTANCE.getAdapter();
                                                final String s14 = url2;
                                                final String s15 = null;
                                                final String s16 = null;
                                                final String s17 = s12;
                                                final Map map2 = headerMap2;
                                                final String s18 = s13;
                                                final String s19 = null;
                                                final boolean b2 = false;
                                                final String s20 = null;
                                                final String s21 = userNameSpace2;
                                                final DebugLog debugLog4 = debugLog3;
                                                final Continuation continuation2 = $continuation;
                                                final int n2 = 902;
                                                final Object o2 = null;
                                                ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0 = this;
                                                ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1 = concurrentRecord;
                                                ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).label = 3;
                                                if ((strResponseByRemoteWebview$default2 = ReaderAdapterInterface.DefaultImpls.getStrResponseByRemoteWebview$default(adapter2, s14, s15, s16, s17, map2, sourceRegex, s18, s19, b2, s20, s21, debugLog4, continuation2, n2, o2)) == coroutine_SUSPENDED) {
                                                    return coroutine_SUSPENDED;
                                                }
                                                break Label_0619;
                                            }
                                        }
                                        else {
                                            final OkHttpClient proxyClient = HttpHelperKt.getProxyClient(this.proxy, this.getDebugLog());
                                            final int retry = this.retry;
                                            final Function1 builder = (Function1)new AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$2(this);
                                            final Continuation $completion3 = $continuation;
                                            ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0 = this;
                                            ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1 = concurrentRecord;
                                            ((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).label = 4;
                                            if ((callStrResponse = OkHttpUtilsKt.newCallStrResponse(proxyClient, retry, (Function1<? super Request$Builder, Unit>)builder, (Continuation<? super StrResponse>)$completion3)) == coroutine_SUSPENDED) {
                                                return coroutine_SUSPENDED;
                                            }
                                            break Label_0712;
                                        }
                                    }
                                    break;
                                }
                                case 1: {
                                    instance = (StringUtils)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1;
                                    s = (String)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    byteArrayAwait = $result;
                                    break;
                                }
                                case 2: {
                                    concurrentRecord = (ConcurrentRecord)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1;
                                    this = (AnalyzeUrl)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    strResponseByRemoteWebview$default = $result;
                                    break Label_0456;
                                }
                                case 3: {
                                    concurrentRecord = (ConcurrentRecord)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1;
                                    this = (AnalyzeUrl)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    strResponseByRemoteWebview$default2 = $result;
                                    break Label_0619;
                                }
                                case 4: {
                                    concurrentRecord = (ConcurrentRecord)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$1;
                                    this = (AnalyzeUrl)((AnalyzeUrl$getStrResponseAwait.AnalyzeUrl$getStrResponseAwait$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    callStrResponse = $result;
                                    break Label_0712;
                                }
                                default: {
                                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                }
                            }
                            return new StrResponse(s, instance.byteToHexString((byte[])byteArrayAwait));
                        }
                        final StrResponse strResponse2 = (StrResponse)strResponseByRemoteWebview$default;
                        break Label_0622;
                    }
                    final StrResponse strResponse2 = (StrResponse)strResponseByRemoteWebview$default2;
                }
                break Label_0726;
            }
            strResponse = (StrResponse)callStrResponse;
            this.saveCookieJar(strResponse.getRaw());
        }
        this.fetchEnd(concurrentRecord);
        return strResponse;
    }
    
    public final void saveCookieJar(@NotNull final Response response) {
        Intrinsics.checkNotNullParameter((Object)response, "response");
        final List cookieList = response.headers("Set-Cookie");
        if (cookieList.size() > 0) {
            final CookieStore cookieStore = new CookieStore(this.getUserNameSpace());
            final String domain = NetworkUtils.INSTANCE.getSubDomain(this.url);
            final Iterable $this$forEach$iv = cookieList;
            final int $i$f$forEach = 0;
            for (final Object element$iv : $this$forEach$iv) {
                final String it = (String)element$iv;
                final int n = 0;
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, (Object)"_cookieJar"), it);
            }
        }
    }
    
    @JvmOverloads
    @NotNull
    public final StrResponse getStrResponse(@Nullable final String jsStr, @Nullable final String sourceRegex, final boolean useWebView) {
        return (StrResponse)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeUrl$getStrResponse.AnalyzeUrl$getStrResponse$1(this, jsStr, sourceRegex, useWebView, (Continuation)null), 1, (Object)null);
    }
    
    public static /* synthetic */ StrResponse getStrResponse$default(final AnalyzeUrl analyzeUrl, String jsStr, String sourceRegex, boolean useWebView, final int n, final Object o) {
        if ((n & 0x1) != 0x0) {
            jsStr = null;
        }
        if ((n & 0x2) != 0x0) {
            sourceRegex = null;
        }
        if ((n & 0x4) != 0x0) {
            useWebView = true;
        }
        return analyzeUrl.getStrResponse(jsStr, sourceRegex, useWebView);
    }
    
    @Nullable
    public final Object getResponseAwait(@NotNull final Continuation<? super Response> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1) {
                final AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1 analyzeUrl$getResponseAwait$1 = (AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$completion;
                if ((analyzeUrl$getResponseAwait$1.label & Integer.MIN_VALUE) != 0x0) {
                    final AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1 analyzeUrl$getResponseAwait$2 = analyzeUrl$getResponseAwait$1;
                    analyzeUrl$getResponseAwait$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1(this, (Continuation)$completion);
        }
        final Object $result = ((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ConcurrentRecord concurrentRecord = null;
        Object callResponse = null;
        switch (((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                concurrentRecord = this.fetchStart();
                final AnalyzeUrl analyzeUrl = this;
                final BaseSource source = this.source;
                analyzeUrl.setCookie((source == null) ? null : source.getKey());
                final OkHttpClient proxyClient$default = HttpHelperKt.getProxyClient$default(this.proxy, null, 2, null);
                final int retry = this.retry;
                final Function1 builder = (Function1)new AnalyzeUrl$getResponseAwait$response.AnalyzeUrl$getResponseAwait$response$1(this);
                final Continuation $completion2 = $continuation;
                ((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).L$0 = this;
                ((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).L$1 = concurrentRecord;
                ((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).label = 1;
                if ((callResponse = OkHttpUtilsKt.newCallResponse(proxyClient$default, retry, (Function1<? super Request$Builder, Unit>)builder, (Continuation<? super Response>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                concurrentRecord = (ConcurrentRecord)((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).L$1;
                this = (AnalyzeUrl)((AnalyzeUrl$getResponseAwait.AnalyzeUrl$getResponseAwait$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                callResponse = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final Response response = (Response)callResponse;
        this.fetchEnd(concurrentRecord);
        return response;
    }
    
    @NotNull
    public final Response getResponse() {
        return (Response)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeUrl$getResponse.AnalyzeUrl$getResponse$1(this, (Continuation)null), 1, (Object)null);
    }
    
    @Nullable
    public final Object getByteArrayAwait(@NotNull final Continuation<? super byte[]> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1) {
                final AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1 analyzeUrl$getByteArrayAwait$1 = (AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$completion;
                if ((analyzeUrl$getByteArrayAwait$1.label & Integer.MIN_VALUE) != 0x0) {
                    final AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1 analyzeUrl$getByteArrayAwait$2 = analyzeUrl$getByteArrayAwait$1;
                    analyzeUrl$getByteArrayAwait$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1(this, (Continuation)$completion);
        }
        final Object $result = ((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object callResponseBody = null;
        final ConcurrentRecord concurrentRecord2;
        switch (((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final ConcurrentRecord concurrentRecord = this.fetchStart();
                final MatchResult dataUriFindResult = Regex.find$default(AppPattern.INSTANCE.getDataUriRegex(), (CharSequence)this.urlNoQuery, 0, 2, (Object)null);
                if (dataUriFindResult != null) {
                    final String dataUriBase64 = dataUriFindResult.getGroupValues().get(1);
                    final byte[] byteArray = Base64.decode(dataUriBase64, 0);
                    this.fetchEnd(concurrentRecord);
                    Intrinsics.checkNotNullExpressionValue((Object)byteArray, "byteArray");
                    return byteArray;
                }
                final BaseSource source = this.source;
                this.setCookie((source == null) ? null : source.getKey());
                final OkHttpClient proxyClient$default = HttpHelperKt.getProxyClient$default(this.proxy, null, 2, null);
                final int retry = this.retry;
                final Function1 builder = (Function1)new AnalyzeUrl$getByteArrayAwait$byteArray.AnalyzeUrl$getByteArrayAwait$byteArray$1(this);
                final Continuation $completion2 = $continuation;
                ((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).L$0 = this;
                ((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).L$1 = concurrentRecord;
                ((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).label = 1;
                if ((callResponseBody = OkHttpUtilsKt.newCallResponseBody(proxyClient$default, retry, (Function1<? super Request$Builder, Unit>)builder, (Continuation<? super ResponseBody>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                concurrentRecord2 = (ConcurrentRecord)((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).L$1;
                this = (AnalyzeUrl)((AnalyzeUrl$getByteArrayAwait.AnalyzeUrl$getByteArrayAwait$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                callResponseBody = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final byte[] byteArray2 = ((ResponseBody)callResponseBody).bytes();
        this.fetchEnd(concurrentRecord2);
        return byteArray2;
    }
    
    @NotNull
    public final byte[] getByteArray() {
        return (byte[])BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new AnalyzeUrl$getByteArray.AnalyzeUrl$getByteArray$1(this, (Continuation)null), 1, (Object)null);
    }
    
    @Nullable
    public final Object upload(@NotNull final String fileName, @NotNull final Object file, @NotNull final String contentType, @NotNull final Continuation<? super StrResponse> $completion) {
        return OkHttpUtilsKt.newCallStrResponse(HttpHelperKt.getProxyClient$default(this.proxy, null, 2, null), this.retry, (Function1<? super Request$Builder, Unit>)new AnalyzeUrl$upload.AnalyzeUrl$upload$2(this, fileName, file, contentType), $completion);
    }
    
    private final void setCookie(final String tag) {
        final String domain = NetworkUtils.INSTANCE.getSubDomain((tag == null) ? this.url : tag);
        if (domain.length() == 0) {
            return;
        }
        final CookieStore cookieStore = new CookieStore(this.getUserNameSpace());
        if (this.enabledCookieJar) {
            final String key = Intrinsics.stringPlus(domain, (Object)"_cookieJar");
            final String cookie2 = cookieStore.getCookie(key);
            if (cookie2 != null) {
                final String it = cookie2;
                final int n = 0;
                cookieStore.replaceCookie(domain, it);
            }
        }
        final String cookie = cookieStore.getCookie(domain);
        if (cookie.length() > 0) {
            final Map cookieMap = cookieStore.cookieToMap(cookie);
            final CookieStore cookieStore2 = cookieStore;
            final String s = this.headerMap.get("Cookie");
            final Map customCookieMap = cookieStore2.cookieToMap((s == null) ? "" : s);
            cookieMap.putAll(customCookieMap);
            final String mapToCookie;
            final String newCookie = mapToCookie = cookieStore.mapToCookie(cookieMap);
            if (mapToCookie != null) {
                final String it2 = mapToCookie;
                final int n2 = 0;
                final String s2 = this.getHeaderMap().put("Cookie", it2);
            }
        }
    }
    
    @NotNull
    public final String getUserAgent() {
        final String s = this.headerMap.get("User-Agent");
        return (s == null) ? AppConst.INSTANCE.getUserAgent() : s;
    }
    
    public final boolean isPost() {
        return this.method == RequestMethod.POST;
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
    
    @Nullable
    @Override
    public String ajax(@NotNull final String urlStr) {
        return DefaultImpls.ajax(urlStr);
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
    public final StrResponse getStrResponse(@Nullable final String jsStr, @Nullable final String sourceRegex) {
        return getStrResponse$default(this, jsStr, sourceRegex, false, 4, null);
    }
    
    @JvmOverloads
    @NotNull
    public final StrResponse getStrResponse(@Nullable final String jsStr) {
        return getStrResponse$default(this, jsStr, null, false, 6, null);
    }
    
    @JvmOverloads
    @NotNull
    public final StrResponse getStrResponse() {
        return getStrResponse$default(this, null, null, false, 7, null);
    }
    
    public static final /* synthetic */ Pattern access$getParamPattern$cp() {
        return AnalyzeUrl.paramPattern;
    }
    
    static {
        Companion = new Companion(null);
        final Pattern compile = Pattern.compile("\\s*,\\s*(?=\\{)");
        Intrinsics.checkNotNullExpressionValue((Object)compile, "compile(\"\\\\s*,\\\\s*(?=\\\\{)\")");
        paramPattern = compile;
        pagePattern = Pattern.compile("<(.*?)>");
        concurrentRecordMap = new HashMap<String, ConcurrentRecord>();
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002R*\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006`\u0007X\u0082\u0004?\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004?\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\t?\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r：\u0006\u000e" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeUrl$Companion;", "", "()V", "concurrentRecordMap", "Ljava/util/HashMap;", "", "Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;", "Lkotlin/collections/HashMap;", "pagePattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "paramPattern", "getParamPattern", "()Ljava/util/regex/Pattern;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final Pattern getParamPattern() {
            return AnalyzeUrl.access$getParamPattern$cp();
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\f\b\u0086\b\u0018\u00002\u00020\u0001Bq\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0001\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\rJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c2\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0003H\u00c2\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00c2\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00c2\u0003J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\bH\u00c2\u0003?\u0006\u0002\u0010\u0014J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c2\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00c2\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003H\u00c2\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003H\u00c2\u0003Jz\u0010\u0019\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001?\u0006\u0002\u0010\u001aJ\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\b\u0010\u001e\u001a\u0004\u0018\u00010\u0003J\b\u0010\u001f\u001a\u0004\u0018\u00010\u0003J\u0010\u0010 \u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010!J\b\u0010\"\u001a\u0004\u0018\u00010\u0003J\b\u0010#\u001a\u0004\u0018\u00010\u0003J\u0006\u0010$\u001a\u00020\bJ\b\u0010%\u001a\u0004\u0018\u00010\u0003J\b\u0010&\u001a\u0004\u0018\u00010\u0003J\t\u0010'\u001a\u00020\bH\u00d6\u0001J\u0010\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u0010+\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u0010,\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u0010-\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u0010.\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u0010/\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u00100\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\u0010\u00101\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0003J\t\u00102\u001a\u00020\u0003H\u00d6\u0001J\u0006\u00103\u001a\u00020\u001cJ\u000e\u00103\u001a\u00020)2\u0006\u00104\u001a\u00020\u001cR\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0001X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0001X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e?\u0006\u0004\n\u0002\u0010\u000eR\u0010\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u0001X\u0082\u000e?\u0006\u0002\n\u0000：\u00065" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption;", "", "method", "", "charset", "headers", "body", "retry", "", "type", "webView", "webJs", "js", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "()Ljava/lang/Integer;", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption;", "equals", "", "other", "getBody", "getCharset", "getHeaderMap", "", "getJs", "getMethod", "getRetry", "getType", "getWebJs", "hashCode", "setBody", "", "value", "setCharset", "setHeaders", "setJs", "setMethod", "setRetry", "setType", "setWebJs", "toString", "useWebView", "boolean", "reader-pro" })
    public static final class UrlOption
    {
        @Nullable
        private String method;
        @Nullable
        private String charset;
        @Nullable
        private Object headers;
        @Nullable
        private Object body;
        @Nullable
        private Integer retry;
        @Nullable
        private String type;
        @Nullable
        private Object webView;
        @Nullable
        private String webJs;
        @Nullable
        private String js;
        
        public UrlOption(@Nullable final String method, @Nullable final String charset, @Nullable final Object headers, @Nullable final Object body, @Nullable final Integer retry, @Nullable final String type, @Nullable final Object webView, @Nullable final String webJs, @Nullable final String js) {
            this.method = method;
            this.charset = charset;
            this.headers = headers;
            this.body = body;
            this.retry = retry;
            this.type = type;
            this.webView = webView;
            this.webJs = webJs;
            this.js = js;
        }
        
        public final void setMethod(@Nullable final String value) {
            final CharSequence charSequence = value;
            this.method = ((charSequence == null || StringsKt.isBlank(charSequence)) ? null : value);
        }
        
        @Nullable
        public final String getMethod() {
            return this.method;
        }
        
        public final void setCharset(@Nullable final String value) {
            final CharSequence charSequence = value;
            this.charset = ((charSequence == null || StringsKt.isBlank(charSequence)) ? null : value);
        }
        
        @Nullable
        public final String getCharset() {
            return this.charset;
        }
        
        public final void setRetry(@Nullable final String value) {
            final CharSequence charSequence = value;
            this.retry = ((charSequence == null || charSequence.length() == 0) ? null : StringsKt.toIntOrNull(value));
        }
        
        public final int getRetry() {
            final Integer retry = this.retry;
            return (retry == null) ? 0 : retry;
        }
        
        public final void setType(@Nullable final String value) {
            final CharSequence charSequence = value;
            this.type = ((charSequence == null || StringsKt.isBlank(charSequence)) ? null : value);
        }
        
        @Nullable
        public final String getType() {
            return this.type;
        }
        
        public final boolean useWebView() {
            final Object webView = this.webView;
            return webView != null && !Intrinsics.areEqual(webView, (Object)"") && !Intrinsics.areEqual(webView, (Object)false) && !Intrinsics.areEqual(webView, (Object)"false");
        }
        
        public final void useWebView(final boolean boolean) {
            this.webView = (boolean ? Boolean.valueOf(true) : null);
        }
        
        public final void setHeaders(@Nullable final String value) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1         /* value */
            //     2: checkcast       Ljava/lang/CharSequence;
            //     5: astore_2       
            //     6: iconst_0       
            //     7: istore_3       
            //     8: iconst_0       
            //     9: istore          4
            //    11: aload_2        
            //    12: ifnull          22
            //    15: aload_2        
            //    16: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
            //    19: ifeq            26
            //    22: iconst_1       
            //    23: goto            27
            //    26: iconst_0       
            //    27: ifeq            37
            //    30: aconst_null    
            //    31: checkcast       Ljava/util/Map;
            //    34: goto            160
            //    37: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
            //    40: astore_2       
            //    41: astore          9
            //    43: iconst_0       
            //    44: istore_3        /* $i$f$fromJsonObject */
            //    45: iconst_0       
            //    46: istore          4
            //    48: nop            
            //    49: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //    52: astore          5
            //    54: iconst_0       
            //    55: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
            //    57: aload_2         /* $this$fromJsonObject$iv */
            //    58: aload_1         /* value */
            //    59: iconst_0       
            //    60: istore          $i$f$genericType
            //    62: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$setHeaders$$inlined$fromJsonObject$1;
            //    65: dup            
            //    66: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$setHeaders$$inlined$fromJsonObject$1.<init>:()V
            //    69: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$setHeaders$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
            //    72: astore          8
            //    74: aload           8
            //    76: ldc             "object : TypeToken<T>() {}.type"
            //    78: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //    81: aload           8
            //    83: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
            //    86: dup            
            //    87: instanceof      Ljava/util/Map;
            //    90: ifne            95
            //    93: pop            
            //    94: aconst_null    
            //    95: checkcast       Ljava/util/Map;
            //    98: astore          null
            //   100: iconst_0       
            //   101: istore          7
            //   103: aload           6
            //   105: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   108: astore          5
            //   110: goto            133
            //   113: astore          6
            //   115: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   118: astore          7
            //   120: iconst_0       
            //   121: istore          8
            //   123: aload           6
            //   125: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //   128: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   131: astore          5
            //   133: aload           5
            //   135: nop            
            //   136: astore          10
            //   138: aload           9
            //   140: aload           10
            //   142: astore_2       
            //   143: iconst_0       
            //   144: istore_3       
            //   145: aload_2        
            //   146: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
            //   149: ifeq            156
            //   152: aconst_null    
            //   153: goto            157
            //   156: aload_2        
            //   157: checkcast       Ljava/util/Map;
            //   160: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.headers:Ljava/lang/Object;
            //   163: return         
            //    MethodParameters:
            //  Name   Flags  
            //  -----  -----
            //  value  
            //    StackMapTable: 00 0A FF 00 16 00 05 07 00 02 07 00 3A 07 00 32 01 01 00 01 07 00 02 43 07 00 02 FF 00 00 00 05 07 00 02 07 00 3A 07 00 32 01 01 00 02 07 00 02 01 49 07 00 02 FF 00 39 00 0A 07 00 02 07 00 3A 07 00 83 01 01 07 00 9D 01 01 07 00 9F 07 00 02 00 01 07 00 04 FF 00 11 00 0A 07 00 02 07 00 3A 07 00 83 01 01 00 00 00 00 07 00 02 00 01 07 00 67 FF 00 13 00 0A 07 00 02 07 00 3A 07 00 83 01 01 07 00 04 07 00 04 00 00 07 00 02 00 00 FF 00 16 00 0B 07 00 02 07 00 3A 07 00 04 01 01 07 00 04 07 00 04 00 00 07 00 02 07 00 04 00 01 07 00 02 FF 00 00 00 0B 07 00 02 07 00 3A 07 00 04 01 01 07 00 04 07 00 04 00 00 07 00 02 07 00 04 00 02 07 00 02 07 00 04 FF 00 02 00 05 07 00 02 07 00 3A 07 00 04 01 01 00 02 07 00 02 07 00 69
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  48     110    113    133    Ljava/lang/Throwable;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
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
        
        @Nullable
        public final Map<?, ?> getHeaderMap() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.headers:Ljava/lang/Object;
            //     4: astore_1        /* value */
            //     5: aload_1         /* value */
            //     6: instanceof      Ljava/util/Map;
            //     9: ifeq            19
            //    12: aload_1         /* value */
            //    13: checkcast       Ljava/util/Map;
            //    16: goto            151
            //    19: aload_1         /* value */
            //    20: instanceof      Ljava/lang/String;
            //    23: ifeq            150
            //    26: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
            //    29: astore_2       
            //    30: aload_1         /* value */
            //    31: checkcast       Ljava/lang/String;
            //    34: astore_3        /* json$iv */
            //    35: iconst_0       
            //    36: istore          $i$f$fromJsonObject
            //    38: iconst_0       
            //    39: istore          5
            //    41: nop            
            //    42: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //    45: astore          6
            //    47: iconst_0       
            //    48: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
            //    50: aload_2         /* $this$fromJsonObject$iv */
            //    51: aload_3         /* json$iv */
            //    52: iconst_0       
            //    53: istore          $i$f$genericType
            //    55: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$getHeaderMap$$inlined$fromJsonObject$1;
            //    58: dup            
            //    59: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$getHeaderMap$$inlined$fromJsonObject$1.<init>:()V
            //    62: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$getHeaderMap$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
            //    65: astore          9
            //    67: aload           9
            //    69: ldc             "object : TypeToken<T>() {}.type"
            //    71: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //    74: aload           9
            //    76: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
            //    79: dup            
            //    80: instanceof      Ljava/util/Map;
            //    83: ifne            88
            //    86: pop            
            //    87: aconst_null    
            //    88: checkcast       Ljava/util/Map;
            //    91: astore          null
            //    93: iconst_0       
            //    94: istore          8
            //    96: aload           7
            //    98: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   101: astore          6
            //   103: goto            126
            //   106: astore          7
            //   108: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   111: astore          8
            //   113: iconst_0       
            //   114: istore          9
            //   116: aload           7
            //   118: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //   121: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   124: astore          6
            //   126: aload           6
            //   128: nop            
            //   129: astore_2        /* $this$fromJsonObject$iv */
            //   130: iconst_0       
            //   131: istore_3       
            //   132: aload_2        
            //   133: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
            //   136: ifeq            143
            //   139: aconst_null    
            //   140: goto            144
            //   143: aload_2        
            //   144: checkcast       Ljava/util/Map;
            //   147: goto            151
            //   150: aconst_null    
            //   151: areturn        
            //    Signature:
            //  ()Ljava/util/Map<**>;
            //    StackMapTable: 00 08 FC 00 13 07 00 04 FF 00 44 00 0A 07 00 02 07 00 04 07 00 83 07 00 3A 01 01 07 00 9D 01 01 07 00 9F 00 01 07 00 04 FF 00 11 00 06 07 00 02 07 00 04 07 00 83 07 00 3A 01 01 00 01 07 00 67 FD 00 13 07 00 04 07 00 04 FF 00 10 00 08 07 00 02 07 00 04 07 00 04 01 01 01 07 00 04 07 00 04 00 00 40 07 00 04 FF 00 05 00 02 07 00 02 07 00 04 00 00 40 07 00 69
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  41     103    106    126    Ljava/lang/Throwable;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
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
        
        public final void setBody(@Nullable final String value) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_1         /* value */
            //     2: checkcast       Ljava/lang/CharSequence;
            //     5: astore_2       
            //     6: iconst_0       
            //     7: istore_3       
            //     8: iconst_0       
            //     9: istore          4
            //    11: aload_2        
            //    12: ifnull          22
            //    15: aload_2        
            //    16: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
            //    19: ifeq            26
            //    22: iconst_1       
            //    23: goto            27
            //    26: iconst_0       
            //    27: ifeq            34
            //    30: aconst_null    
            //    31: goto            269
            //    34: aload_1         /* value */
            //    35: invokestatic    io/legado/app/utils/StringExtensionsKt.isJsonObject:(Ljava/lang/String;)Z
            //    38: ifeq            152
            //    41: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
            //    44: astore_2       
            //    45: astore          9
            //    47: iconst_0       
            //    48: istore_3        /* $i$f$fromJsonObject */
            //    49: iconst_0       
            //    50: istore          4
            //    52: nop            
            //    53: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //    56: astore          5
            //    58: iconst_0       
            //    59: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
            //    61: aload_2         /* $this$fromJsonObject$iv */
            //    62: aload_1         /* value */
            //    63: iconst_0       
            //    64: istore          $i$f$genericType
            //    66: new             Lio/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$setBody$$inlined$fromJsonObject$1;
            //    69: dup            
            //    70: invokespecial   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$setBody$$inlined$fromJsonObject$1.<init>:()V
            //    73: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption$setBody$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
            //    76: astore          8
            //    78: aload           8
            //    80: ldc             "object : TypeToken<T>() {}.type"
            //    82: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //    85: aload           8
            //    87: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
            //    90: dup            
            //    91: instanceof      Ljava/util/Map;
            //    94: ifne            99
            //    97: pop            
            //    98: aconst_null    
            //    99: checkcast       Ljava/util/Map;
            //   102: astore          null
            //   104: iconst_0       
            //   105: istore          7
            //   107: aload           6
            //   109: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   112: astore          5
            //   114: goto            137
            //   117: astore          6
            //   119: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   122: astore          7
            //   124: iconst_0       
            //   125: istore          8
            //   127: aload           6
            //   129: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //   132: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   135: astore          5
            //   137: aload           5
            //   139: nop            
            //   140: astore          10
            //   142: aload           9
            //   144: aload           10
            //   146: invokestatic    kotlin/Result.box-impl:(Ljava/lang/Object;)Lkotlin/Result;
            //   149: goto            269
            //   152: aload_1         /* value */
            //   153: invokestatic    io/legado/app/utils/StringExtensionsKt.isJsonArray:(Ljava/lang/String;)Z
            //   156: ifeq            265
            //   159: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
            //   162: astore_2       
            //   163: astore          9
            //   165: iconst_0       
            //   166: istore_3        /* $i$f$fromJsonArray */
            //   167: iconst_0       
            //   168: istore          4
            //   170: nop            
            //   171: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   174: astore          5
            //   176: iconst_0       
            //   177: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonArray$1$iv
            //   179: aload_2         /* $this$fromJsonArray$iv */
            //   180: aload_1         /* value */
            //   181: new             Lio/legado/app/utils/ParameterizedTypeImpl;
            //   184: dup            
            //   185: ldc             Ljava/util/Map;.class
            //   187: invokespecial   io/legado/app/utils/ParameterizedTypeImpl.<init>:(Ljava/lang/Class;)V
            //   190: checkcast       Ljava/lang/reflect/Type;
            //   193: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
            //   196: astore          7
            //   198: aload           7
            //   200: instanceof      Ljava/util/List;
            //   203: ifeq            214
            //   206: aload           7
            //   208: checkcast       Ljava/util/List;
            //   211: goto            215
            //   214: aconst_null    
            //   215: astore          null
            //   217: iconst_0       
            //   218: istore          7
            //   220: aload           6
            //   222: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   225: astore          5
            //   227: goto            250
            //   230: astore          6
            //   232: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   235: astore          7
            //   237: iconst_0       
            //   238: istore          8
            //   240: aload           6
            //   242: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //   245: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   248: astore          5
            //   250: aload           5
            //   252: nop            
            //   253: astore          10
            //   255: aload           9
            //   257: aload           10
            //   259: invokestatic    kotlin/Result.box-impl:(Ljava/lang/Object;)Lkotlin/Result;
            //   262: goto            269
            //   265: aload_1         /* value */
            //   266: checkcast       Ljava/io/Serializable;
            //   269: putfield        io/legado/app/model/analyzeRule/AnalyzeUrl$UrlOption.body:Ljava/lang/Object;
            //   272: return         
            //    MethodParameters:
            //  Name   Flags  
            //  -----  -----
            //  value  
            //    StackMapTable: 00 0E FF 00 16 00 05 07 00 02 07 00 3A 07 00 32 01 01 00 01 07 00 02 43 07 00 02 FF 00 00 00 05 07 00 02 07 00 3A 07 00 32 01 01 00 02 07 00 02 01 46 07 00 02 FF 00 40 00 0A 07 00 02 07 00 3A 07 00 83 01 01 07 00 9D 01 01 07 00 9F 07 00 02 00 01 07 00 04 FF 00 11 00 0A 07 00 02 07 00 3A 07 00 83 01 01 00 00 00 00 07 00 02 00 01 07 00 67 FF 00 13 00 0A 07 00 02 07 00 3A 07 00 83 01 01 07 00 04 07 00 04 00 00 07 00 02 00 00 FF 00 0E 00 05 07 00 02 07 00 3A 07 00 32 01 01 00 01 07 00 02 FF 00 3D 00 0A 07 00 02 07 00 3A 07 00 83 01 01 07 00 9D 01 07 00 04 00 07 00 02 00 00 40 07 00 C0 FF 00 0E 00 0A 07 00 02 07 00 3A 07 00 83 01 01 00 00 00 00 07 00 02 00 01 07 00 67 FF 00 13 00 0A 07 00 02 07 00 3A 07 00 83 01 01 07 00 04 07 00 04 00 00 07 00 02 00 00 FF 00 0E 00 05 07 00 02 07 00 3A 07 00 32 01 01 00 01 07 00 02 FF 00 03 00 05 07 00 02 07 00 3A 07 00 04 01 01 00 02 07 00 02 07 00 04
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  52     114    117    137    Ljava/lang/Throwable;
            //  170    227    230    250    Ljava/lang/Throwable;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
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
        
        @Nullable
        public final String getBody() {
            final Object body = this.body;
            String s;
            if (body == null) {
                s = null;
            }
            else {
                final Object it = body;
                final int n = 0;
                s = (String)((it instanceof String) ? it : GsonExtensionsKt.getGSON().toJson(it));
            }
            return s;
        }
        
        public final void setWebJs(@Nullable final String value) {
            final CharSequence charSequence = value;
            this.webJs = ((charSequence == null || StringsKt.isBlank(charSequence)) ? null : value);
        }
        
        @Nullable
        public final String getWebJs() {
            return this.webJs;
        }
        
        public final void setJs(@Nullable final String value) {
            final CharSequence charSequence = value;
            this.js = ((charSequence == null || StringsKt.isBlank(charSequence)) ? null : value);
        }
        
        @Nullable
        public final String getJs() {
            return this.js;
        }
        
        private final String component1() {
            return this.method;
        }
        
        private final String component2() {
            return this.charset;
        }
        
        private final Object component3() {
            return this.headers;
        }
        
        private final Object component4() {
            return this.body;
        }
        
        private final Integer component5() {
            return this.retry;
        }
        
        private final String component6() {
            return this.type;
        }
        
        private final Object component7() {
            return this.webView;
        }
        
        private final String component8() {
            return this.webJs;
        }
        
        private final String component9() {
            return this.js;
        }
        
        @NotNull
        public final UrlOption copy(@Nullable final String method, @Nullable final String charset, @Nullable final Object headers, @Nullable final Object body, @Nullable final Integer retry, @Nullable final String type, @Nullable final Object webView, @Nullable final String webJs, @Nullable final String js) {
            return new UrlOption(method, charset, headers, body, retry, type, webView, webJs, js);
        }
        
        @NotNull
        @Override
        public String toString() {
            return "UrlOption(method=" + (Object)this.method + ", charset=" + (Object)this.charset + ", headers=" + this.headers + ", body=" + this.body + ", retry=" + this.retry + ", type=" + (Object)this.type + ", webView=" + this.webView + ", webJs=" + (Object)this.webJs + ", js=" + (Object)this.js + ')';
        }
        
        @Override
        public int hashCode() {
            int result = (this.method == null) ? 0 : this.method.hashCode();
            result = result * 31 + ((this.charset == null) ? 0 : this.charset.hashCode());
            result = result * 31 + ((this.headers == null) ? 0 : this.headers.hashCode());
            result = result * 31 + ((this.body == null) ? 0 : this.body.hashCode());
            result = result * 31 + ((this.retry == null) ? 0 : this.retry.hashCode());
            result = result * 31 + ((this.type == null) ? 0 : this.type.hashCode());
            result = result * 31 + ((this.webView == null) ? 0 : this.webView.hashCode());
            result = result * 31 + ((this.webJs == null) ? 0 : this.webJs.hashCode());
            result = result * 31 + ((this.js == null) ? 0 : this.js.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof UrlOption)) {
                return false;
            }
            final UrlOption urlOption = (UrlOption)other;
            return Intrinsics.areEqual((Object)this.method, (Object)urlOption.method) && Intrinsics.areEqual((Object)this.charset, (Object)urlOption.charset) && Intrinsics.areEqual(this.headers, urlOption.headers) && Intrinsics.areEqual(this.body, urlOption.body) && Intrinsics.areEqual((Object)this.retry, (Object)urlOption.retry) && Intrinsics.areEqual((Object)this.type, (Object)urlOption.type) && Intrinsics.areEqual(this.webView, urlOption.webView) && Intrinsics.areEqual((Object)this.webJs, (Object)urlOption.webJs) && Intrinsics.areEqual((Object)this.js, (Object)urlOption.js);
        }
        
        public UrlOption() {
            this(null, null, null, null, null, null, null, null, null, 511, null);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007?\u0006\u0002\u0010\bJ\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0007H\u00c6\u0003J'\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00032\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u0007H\u00d6\u0001J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012：\u0006\u001c" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeUrl$ConcurrentRecord;", "", "concurrent", "", "time", "", "frequency", "", "(ZJI)V", "getConcurrent", "()Z", "getFrequency", "()I", "setFrequency", "(I)V", "getTime", "()J", "setTime", "(J)V", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "toString", "", "reader-pro" })
    public static final class ConcurrentRecord
    {
        private final boolean concurrent;
        private long time;
        private int frequency;
        
        public ConcurrentRecord(final boolean concurrent, final long time, final int frequency) {
            this.concurrent = concurrent;
            this.time = time;
            this.frequency = frequency;
        }
        
        public final boolean getConcurrent() {
            return this.concurrent;
        }
        
        public final long getTime() {
            return this.time;
        }
        
        public final void setTime(final long <set-?>) {
            this.time = <set-?>;
        }
        
        public final int getFrequency() {
            return this.frequency;
        }
        
        public final void setFrequency(final int <set-?>) {
            this.frequency = <set-?>;
        }
        
        public final boolean component1() {
            return this.concurrent;
        }
        
        public final long component2() {
            return this.time;
        }
        
        public final int component3() {
            return this.frequency;
        }
        
        @NotNull
        public final ConcurrentRecord copy(final boolean concurrent, final long time, final int frequency) {
            return new ConcurrentRecord(concurrent, time, frequency);
        }
        
        @NotNull
        @Override
        public String toString() {
            return "ConcurrentRecord(concurrent=" + this.concurrent + ", time=" + this.time + ", frequency=" + this.frequency + ')';
        }
        
        @Override
        public int hashCode() {
            int concurrent;
            if ((concurrent = (this.concurrent ? 1 : 0)) != 0) {
                concurrent = 1;
            }
            int result = concurrent;
            result = result * 31 + Long.hashCode(this.time);
            result = result * 31 + Integer.hashCode(this.frequency);
            return result;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ConcurrentRecord)) {
                return false;
            }
            final ConcurrentRecord concurrentRecord = (ConcurrentRecord)other;
            return this.concurrent == concurrentRecord.concurrent && this.time == concurrentRecord.time && this.frequency == concurrentRecord.frequency;
        }
    }
}
