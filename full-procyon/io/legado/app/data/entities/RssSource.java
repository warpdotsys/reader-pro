// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import com.jayway.jsonpath.Predicate;
import io.legado.app.utils.JsonExtensionsKt;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.DocumentContext;
import io.legado.app.model.analyzeRule.QueryTTF;
import java.util.HashMap;
import java.io.File;
import org.jsoup.Connection$Response;
import java.util.Map;
import io.legado.app.help.http.StrResponse;
import com.script.SimpleBindings;
import java.util.Iterator;
import kotlin.Result$Companion;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.text.Regex;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;
import kotlin.Result;
import java.util.ArrayList;
import kotlin.Pair;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.model.DebugLog;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "headerMap", "source", "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap" })
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b[\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 \u0099\u00012\u00020\u0001:\u0002\u0099\u0001B\u00c3\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\t\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001e\u001a\u00020\t\u0012\b\b\u0002\u0010\u001f\u001a\u00020\t\u0012\b\b\u0002\u0010 \u001a\u00020\u0014?\u0006\u0002\u0010!J\t\u0010f\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010g\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010h\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010i\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010j\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010k\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010l\u001a\u00020\tH\u00c6\u0003J\t\u0010m\u001a\u00020\u0014H\u00c6\u0003J\u000b\u0010n\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010o\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010p\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010q\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010r\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010s\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010t\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010u\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010v\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010w\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010x\u001a\u00020\tH\u00c6\u0003J\t\u0010y\u001a\u00020\tH\u00c6\u0003J\t\u0010z\u001a\u00020\u0014H\u00c6\u0003J\t\u0010{\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010|\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010}\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010~\u001a\u00020\tH\u00c6\u0003J\u000b\u0010\u007f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\u0080\u0001\u001a\u0004\u0018\u00010\tH\u00c6\u0003?\u0006\u0002\u00106J\f\u0010\u0081\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u00ce\u0002\u0010\u0082\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001e\u001a\u00020\t2\b\b\u0002\u0010\u001f\u001a\u00020\t2\b\b\u0002\u0010 \u001a\u00020\u0014H\u00c6\u0001?\u0006\u0003\u0010\u0083\u0001J\u0010\u0010\u0084\u0001\u001a\u00020\t2\u0007\u0010\u0085\u0001\u001a\u00020\u0000J\u001f\u0010\u0084\u0001\u001a\u00020\t2\t\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u00032\t\u0010\u0087\u0001\u001a\u0004\u0018\u00010\u0003H\u0002J\u0016\u0010\u0088\u0001\u001a\u00020\t2\n\u0010\u0089\u0001\u001a\u0005\u0018\u00010\u008a\u0001H\u0096\u0002J\t\u0010\u008b\u0001\u001a\u00020\u0003H\u0016J\u000b\u0010\u008c\u0001\u001a\u0004\u0018\u00010.H\u0016J\t\u0010\u008d\u0001\u001a\u00020\u0003H\u0016J\t\u0010\u008e\u0001\u001a\u00020\u0003H\u0016J\t\u0010\u008f\u0001\u001a\u00020\u0014H\u0016J\u0013\u0010\u0090\u0001\u001a\u00030\u0091\u00012\t\u0010\u0092\u0001\u001a\u0004\u0018\u00010.J\u0011\u0010\u0093\u0001\u001a\u00030\u0091\u00012\u0007\u0010\u0094\u0001\u001a\u00020\u0003J\u001b\u0010\u0095\u0001\u001a\u0016\u0012\u0011\u0012\u000f\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0097\u00010\u0096\u0001J\n\u0010\u0098\u0001\u001a\u00020\u0003H\u00d6\u0001R\u000e\u0010\"\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010 \u001a\u00020\u0014X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b+\u0010$\"\u0004\b,\u0010&R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b3\u00100\"\u0004\b4\u00102R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0096\u000e?\u0006\u0010\n\u0002\u00109\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b:\u0010(\"\u0004\b;\u0010*R\u001a\u0010\u001f\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b<\u00100\"\u0004\b=\u00102R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b>\u0010(\"\u0004\b?\u0010*R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b@\u0010(\"\u0004\bA\u0010*R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bB\u0010(\"\u0004\bC\u0010*R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bD\u0010(\"\u0004\bE\u0010*R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bF\u0010(\"\u0004\bG\u0010*R\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bH\u0010(\"\u0004\bI\u0010*R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010(\"\u0004\bK\u0010*R\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bL\u0010(\"\u0004\bM\u0010*R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bN\u0010(\"\u0004\bO\u0010*R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bP\u0010(\"\u0004\bQ\u0010*R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bR\u0010(\"\u0004\bS\u0010*R\u001a\u0010\u0012\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bT\u00100\"\u0004\bU\u00102R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bV\u0010(\"\u0004\bW\u0010*R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bX\u0010(\"\u0004\bY\u0010*R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010(\"\u0004\b[\u0010*R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010(\"\u0004\b]\u0010*R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b^\u0010(\"\u0004\b_\u0010*R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b`\u0010(\"\u0004\ba\u0010*R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bb\u0010(\"\u0004\bc\u0010*R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bd\u0010(\"\u0004\be\u0010*¡§\u0006\u009a\u0001" }, d2 = { "Lio/legado/app/data/entities/RssSource;", "Lio/legado/app/data/entities/BaseSource;", "sourceUrl", "", "sourceName", "sourceIcon", "sourceGroup", "sourceComment", "enabled", "", "variableComment", "enabledCookieJar", "concurrentRate", "header", "loginUrl", "loginUi", "loginCheckJs", "sortUrl", "singleUrl", "articleStyle", "", "ruleArticles", "ruleNextPage", "ruleTitle", "rulePubDate", "ruleDescription", "ruleImage", "ruleLink", "ruleContent", "style", "enableJs", "loadWithBaseUrl", "customOrder", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZI)V", "_userNameSpace", "getArticleStyle", "()I", "setArticleStyle", "(I)V", "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "getCustomOrder", "setCustomOrder", "debugLog", "Lio/legado/app/model/DebugLog;", "getEnableJs", "()Z", "setEnableJs", "(Z)V", "getEnabled", "setEnabled", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getHeader", "setHeader", "getLoadWithBaseUrl", "setLoadWithBaseUrl", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "setLoginUi", "getLoginUrl", "setLoginUrl", "getRuleArticles", "setRuleArticles", "getRuleContent", "setRuleContent", "getRuleDescription", "setRuleDescription", "getRuleImage", "setRuleImage", "getRuleLink", "setRuleLink", "getRuleNextPage", "setRuleNextPage", "getRulePubDate", "setRulePubDate", "getRuleTitle", "setRuleTitle", "getSingleUrl", "setSingleUrl", "getSortUrl", "setSortUrl", "getSourceComment", "setSourceComment", "getSourceGroup", "setSourceGroup", "getSourceIcon", "setSourceIcon", "getSourceName", "setSourceName", "getSourceUrl", "setSourceUrl", "getStyle", "setStyle", "getVariableComment", "setVariableComment", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZI)Lio/legado/app/data/entities/RssSource;", "equal", "source", "a", "b", "equals", "other", "", "getKey", "getLogger", "getTag", "getUserNameSpace", "hashCode", "setLogger", "", "logger", "setUserNameSpace", "nameSpace", "sortUrls", "", "Lkotlin/Pair;", "toString", "Companion", "reader-pro" })
public final class RssSource implements BaseSource
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private String sourceUrl;
    @NotNull
    private String sourceName;
    @NotNull
    private String sourceIcon;
    @Nullable
    private String sourceGroup;
    @Nullable
    private String sourceComment;
    private boolean enabled;
    @Nullable
    private String variableComment;
    @Nullable
    private Boolean enabledCookieJar;
    @Nullable
    private String concurrentRate;
    @Nullable
    private String header;
    @Nullable
    private String loginUrl;
    @Nullable
    private String loginUi;
    @Nullable
    private String loginCheckJs;
    @Nullable
    private String sortUrl;
    private boolean singleUrl;
    private int articleStyle;
    @Nullable
    private String ruleArticles;
    @Nullable
    private String ruleNextPage;
    @Nullable
    private String ruleTitle;
    @Nullable
    private String rulePubDate;
    @Nullable
    private String ruleDescription;
    @Nullable
    private String ruleImage;
    @Nullable
    private String ruleLink;
    @Nullable
    private String ruleContent;
    @Nullable
    private String style;
    private boolean enableJs;
    private boolean loadWithBaseUrl;
    private int customOrder;
    @NotNull
    private transient String _userNameSpace;
    @Nullable
    private transient DebugLog debugLog;
    
    public RssSource(@NotNull final String sourceUrl, @NotNull final String sourceName, @NotNull final String sourceIcon, @Nullable final String sourceGroup, @Nullable final String sourceComment, final boolean enabled, @Nullable final String variableComment, @Nullable final Boolean enabledCookieJar, @Nullable final String concurrentRate, @Nullable final String header, @Nullable final String loginUrl, @Nullable final String loginUi, @Nullable final String loginCheckJs, @Nullable final String sortUrl, final boolean singleUrl, final int articleStyle, @Nullable final String ruleArticles, @Nullable final String ruleNextPage, @Nullable final String ruleTitle, @Nullable final String rulePubDate, @Nullable final String ruleDescription, @Nullable final String ruleImage, @Nullable final String ruleLink, @Nullable final String ruleContent, @Nullable final String style, final boolean enableJs, final boolean loadWithBaseUrl, final int customOrder) {
        Intrinsics.checkNotNullParameter((Object)sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter((Object)sourceName, "sourceName");
        Intrinsics.checkNotNullParameter((Object)sourceIcon, "sourceIcon");
        this.sourceUrl = sourceUrl;
        this.sourceName = sourceName;
        this.sourceIcon = sourceIcon;
        this.sourceGroup = sourceGroup;
        this.sourceComment = sourceComment;
        this.enabled = enabled;
        this.variableComment = variableComment;
        this.enabledCookieJar = enabledCookieJar;
        this.concurrentRate = concurrentRate;
        this.header = header;
        this.loginUrl = loginUrl;
        this.loginUi = loginUi;
        this.loginCheckJs = loginCheckJs;
        this.sortUrl = sortUrl;
        this.singleUrl = singleUrl;
        this.articleStyle = articleStyle;
        this.ruleArticles = ruleArticles;
        this.ruleNextPage = ruleNextPage;
        this.ruleTitle = ruleTitle;
        this.rulePubDate = rulePubDate;
        this.ruleDescription = ruleDescription;
        this.ruleImage = ruleImage;
        this.ruleLink = ruleLink;
        this.ruleContent = ruleContent;
        this.style = style;
        this.enableJs = enableJs;
        this.loadWithBaseUrl = loadWithBaseUrl;
        this.customOrder = customOrder;
        this._userNameSpace = "";
    }
    
    @NotNull
    public final String getSourceUrl() {
        return this.sourceUrl;
    }
    
    public final void setSourceUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.sourceUrl = <set-?>;
    }
    
    @NotNull
    public final String getSourceName() {
        return this.sourceName;
    }
    
    public final void setSourceName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.sourceName = <set-?>;
    }
    
    @NotNull
    public final String getSourceIcon() {
        return this.sourceIcon;
    }
    
    public final void setSourceIcon(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.sourceIcon = <set-?>;
    }
    
    @Nullable
    public final String getSourceGroup() {
        return this.sourceGroup;
    }
    
    public final void setSourceGroup(@Nullable final String <set-?>) {
        this.sourceGroup = <set-?>;
    }
    
    @Nullable
    public final String getSourceComment() {
        return this.sourceComment;
    }
    
    public final void setSourceComment(@Nullable final String <set-?>) {
        this.sourceComment = <set-?>;
    }
    
    public final boolean getEnabled() {
        return this.enabled;
    }
    
    public final void setEnabled(final boolean <set-?>) {
        this.enabled = <set-?>;
    }
    
    @Nullable
    public final String getVariableComment() {
        return this.variableComment;
    }
    
    public final void setVariableComment(@Nullable final String <set-?>) {
        this.variableComment = <set-?>;
    }
    
    @Nullable
    @Override
    public Boolean getEnabledCookieJar() {
        return this.enabledCookieJar;
    }
    
    @Override
    public void setEnabledCookieJar(@Nullable final Boolean <set-?>) {
        this.enabledCookieJar = <set-?>;
    }
    
    @Nullable
    @Override
    public String getConcurrentRate() {
        return this.concurrentRate;
    }
    
    @Override
    public void setConcurrentRate(@Nullable final String <set-?>) {
        this.concurrentRate = <set-?>;
    }
    
    @Nullable
    @Override
    public String getHeader() {
        return this.header;
    }
    
    @Override
    public void setHeader(@Nullable final String <set-?>) {
        this.header = <set-?>;
    }
    
    @Nullable
    @Override
    public String getLoginUrl() {
        return this.loginUrl;
    }
    
    @Override
    public void setLoginUrl(@Nullable final String <set-?>) {
        this.loginUrl = <set-?>;
    }
    
    @Nullable
    @Override
    public String getLoginUi() {
        return this.loginUi;
    }
    
    @Override
    public void setLoginUi(@Nullable final String <set-?>) {
        this.loginUi = <set-?>;
    }
    
    @Nullable
    public final String getLoginCheckJs() {
        return this.loginCheckJs;
    }
    
    public final void setLoginCheckJs(@Nullable final String <set-?>) {
        this.loginCheckJs = <set-?>;
    }
    
    @Nullable
    public final String getSortUrl() {
        return this.sortUrl;
    }
    
    public final void setSortUrl(@Nullable final String <set-?>) {
        this.sortUrl = <set-?>;
    }
    
    public final boolean getSingleUrl() {
        return this.singleUrl;
    }
    
    public final void setSingleUrl(final boolean <set-?>) {
        this.singleUrl = <set-?>;
    }
    
    public final int getArticleStyle() {
        return this.articleStyle;
    }
    
    public final void setArticleStyle(final int <set-?>) {
        this.articleStyle = <set-?>;
    }
    
    @Nullable
    public final String getRuleArticles() {
        return this.ruleArticles;
    }
    
    public final void setRuleArticles(@Nullable final String <set-?>) {
        this.ruleArticles = <set-?>;
    }
    
    @Nullable
    public final String getRuleNextPage() {
        return this.ruleNextPage;
    }
    
    public final void setRuleNextPage(@Nullable final String <set-?>) {
        this.ruleNextPage = <set-?>;
    }
    
    @Nullable
    public final String getRuleTitle() {
        return this.ruleTitle;
    }
    
    public final void setRuleTitle(@Nullable final String <set-?>) {
        this.ruleTitle = <set-?>;
    }
    
    @Nullable
    public final String getRulePubDate() {
        return this.rulePubDate;
    }
    
    public final void setRulePubDate(@Nullable final String <set-?>) {
        this.rulePubDate = <set-?>;
    }
    
    @Nullable
    public final String getRuleDescription() {
        return this.ruleDescription;
    }
    
    public final void setRuleDescription(@Nullable final String <set-?>) {
        this.ruleDescription = <set-?>;
    }
    
    @Nullable
    public final String getRuleImage() {
        return this.ruleImage;
    }
    
    public final void setRuleImage(@Nullable final String <set-?>) {
        this.ruleImage = <set-?>;
    }
    
    @Nullable
    public final String getRuleLink() {
        return this.ruleLink;
    }
    
    public final void setRuleLink(@Nullable final String <set-?>) {
        this.ruleLink = <set-?>;
    }
    
    @Nullable
    public final String getRuleContent() {
        return this.ruleContent;
    }
    
    public final void setRuleContent(@Nullable final String <set-?>) {
        this.ruleContent = <set-?>;
    }
    
    @Nullable
    public final String getStyle() {
        return this.style;
    }
    
    public final void setStyle(@Nullable final String <set-?>) {
        this.style = <set-?>;
    }
    
    public final boolean getEnableJs() {
        return this.enableJs;
    }
    
    public final void setEnableJs(final boolean <set-?>) {
        this.enableJs = <set-?>;
    }
    
    public final boolean getLoadWithBaseUrl() {
        return this.loadWithBaseUrl;
    }
    
    public final void setLoadWithBaseUrl(final boolean <set-?>) {
        this.loadWithBaseUrl = <set-?>;
    }
    
    public final int getCustomOrder() {
        return this.customOrder;
    }
    
    public final void setCustomOrder(final int <set-?>) {
        this.customOrder = <set-?>;
    }
    
    @NotNull
    @Override
    public String getTag() {
        return this.sourceName;
    }
    
    @NotNull
    @Override
    public String getKey() {
        return this.sourceUrl;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof RssSource && Intrinsics.areEqual((Object)((RssSource)other).sourceUrl, (Object)this.sourceUrl);
    }
    
    @Override
    public int hashCode() {
        return this.sourceUrl.hashCode();
    }
    
    public final boolean equal(@NotNull final RssSource source) {
        Intrinsics.checkNotNullParameter((Object)source, "source");
        return this.equal(this.sourceUrl, source.sourceUrl) && this.equal(this.sourceIcon, source.sourceIcon) && this.enabled == source.enabled && Intrinsics.areEqual((Object)this.getEnabledCookieJar(), (Object)source.getEnabledCookieJar()) && this.equal(this.sourceComment, source.sourceComment) && this.equal(this.sourceGroup, source.sourceGroup) && this.equal(this.ruleArticles, source.ruleArticles) && this.equal(this.ruleNextPage, source.ruleNextPage) && this.equal(this.ruleTitle, source.ruleTitle) && this.equal(this.rulePubDate, source.rulePubDate) && this.equal(this.ruleDescription, source.ruleDescription) && this.equal(this.ruleLink, source.ruleLink) && this.equal(this.ruleContent, source.ruleContent) && this.enableJs == source.enableJs && this.loadWithBaseUrl == source.loadWithBaseUrl;
    }
    
    private final boolean equal(final String a, final String b) {
        if (!Intrinsics.areEqual((Object)a, (Object)b)) {
            final CharSequence charSequence = a;
            if (charSequence == null || charSequence.length() == 0) {
                final CharSequence charSequence2 = b;
                if (charSequence2 == null || charSequence2.length() == 0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @NotNull
    public final List<Pair<String, String>> sortUrls() {
        final ArrayList $this$sortUrls_u24lambda_u2d2;
        final ArrayList list = $this$sortUrls_u24lambda_u2d2 = new ArrayList();
        final int n = 0;
        try {
            final Result$Companion companion = Result.Companion;
            final int n2 = 0;
            String a = this.getSortUrl();
            final String sortUrl = this.getSortUrl();
            Label_0267: {
                if (sortUrl == null || !StringsKt.startsWith(sortUrl, "<js>", false)) {
                    final String sortUrl2 = this.getSortUrl();
                    if (sortUrl2 == null || !StringsKt.startsWith(sortUrl2, "@js:", false)) {
                        break Label_0267;
                    }
                }
                final String sortUrl3 = this.getSortUrl();
                Intrinsics.checkNotNull((Object)sortUrl3);
                String s3;
                if (StringsKt.startsWith$default(sortUrl3, "@", false, 2, (Object)null)) {
                    final String sortUrl4 = this.getSortUrl();
                    Intrinsics.checkNotNull((Object)sortUrl4);
                    final String s = sortUrl4;
                    final int beginIndex = 4;
                    final String s2 = s;
                    if (s2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkNotNullExpressionValue((Object)(s3 = s2.substring(beginIndex)), "(this as java.lang.String).substring(startIndex)");
                }
                else {
                    final String sortUrl5 = this.getSortUrl();
                    Intrinsics.checkNotNull((Object)sortUrl5);
                    final String s4 = sortUrl5;
                    final int beginIndex2 = 4;
                    final String sortUrl6 = this.getSortUrl();
                    Intrinsics.checkNotNull((Object)sortUrl6);
                    final int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence)sortUrl6, "<", 0, false, 6, (Object)null);
                    final String s5 = s4;
                    if (s5 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    Intrinsics.checkNotNullExpressionValue((Object)(s3 = s5.substring(beginIndex2, lastIndexOf$default)), "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                }
                final String jsStr = s3;
                a = String.valueOf(DefaultImpls.evalJS$default(this, jsStr, null, 2, null));
            }
            final String s6 = a;
            if (s6 != null) {
                final List split = new Regex("(&&|\n)+").split((CharSequence)s6, 0);
                if (split != null) {
                    final Iterable $this$forEach$iv = split;
                    final int $i$f$forEach = 0;
                    for (final Object element$iv : $this$forEach$iv) {
                        final String c = (String)element$iv;
                        final int n3 = 0;
                        final List d = StringsKt.split$default((CharSequence)c, new String[] { "::" }, false, 0, 6, (Object)null);
                        if (d.size() > 1) {
                            $this$sortUrls_u24lambda_u2d2.add(new Pair(d.get(0), d.get(1)));
                        }
                    }
                }
            }
            if ($this$sortUrls_u24lambda_u2d2.isEmpty()) {
                $this$sortUrls_u24lambda_u2d2.add(new Pair((Object)"", (Object)this.getSourceUrl()));
            }
            Result.constructor-impl((Object)Unit.INSTANCE);
        }
        catch (final Throwable t) {
            final Result$Companion companion2 = Result.Companion;
            Result.constructor-impl(ResultKt.createFailure(t));
        }
        return list;
    }
    
    public final void setUserNameSpace(@NotNull final String nameSpace) {
        Intrinsics.checkNotNullParameter((Object)nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }
    
    @NotNull
    @Override
    public String getUserNameSpace() {
        return this._userNameSpace;
    }
    
    public final void setLogger(@Nullable final DebugLog logger) {
        this.debugLog = logger;
    }
    
    @Nullable
    @Override
    public DebugLog getLogger() {
        return null;
    }
    
    @Nullable
    @Override
    public Object evalJS(@NotNull final String jsStr, @NotNull final Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception {
        return DefaultImpls.evalJS(jsStr, bindingsConfig);
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
    public HashMap<String, String> getHeaderMap(final boolean hasLoginHeader) {
        return DefaultImpls.getHeaderMap(hasLoginHeader);
    }
    
    @Nullable
    @Override
    public String getLoginHeader() {
        return DefaultImpls.getLoginHeader();
    }
    
    @Nullable
    @Override
    public Map<String, String> getLoginHeaderMap() {
        return DefaultImpls.getLoginHeaderMap();
    }
    
    @Nullable
    @Override
    public String getLoginInfo() {
        return DefaultImpls.getLoginInfo();
    }
    
    @Nullable
    @Override
    public Map<String, String> getLoginInfoMap() {
        return DefaultImpls.getLoginInfoMap();
    }
    
    @Nullable
    @Override
    public String getLoginJs() {
        return DefaultImpls.getLoginJs();
    }
    
    @Nullable
    @Override
    public BaseSource getSource() {
        return DefaultImpls.getSource();
    }
    
    @NotNull
    @Override
    public String getTxtInFolder(@NotNull final String unzipPath) {
        return DefaultImpls.getTxtInFolder(unzipPath);
    }
    
    @Nullable
    @Override
    public String getVariable() {
        return DefaultImpls.getVariable();
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
    public void login() {
        DefaultImpls.login();
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
    
    @Override
    public void putLoginHeader(@NotNull final String header) {
        DefaultImpls.putLoginHeader(header);
    }
    
    @Override
    public boolean putLoginInfo(@NotNull final String info) {
        return DefaultImpls.putLoginInfo(info);
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
    
    @Override
    public void removeLoginHeader() {
        DefaultImpls.removeLoginHeader();
    }
    
    @Override
    public void removeLoginInfo() {
        DefaultImpls.removeLoginInfo();
    }
    
    @NotNull
    @Override
    public String replaceFont(@NotNull final String text, @Nullable final QueryTTF font1, @Nullable final QueryTTF font2) {
        return DefaultImpls.replaceFont(text, font1, font2);
    }
    
    @Override
    public void setVariable(@Nullable final String variable) {
        DefaultImpls.setVariable(variable);
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
    
    @NotNull
    public final String component1() {
        return this.sourceUrl;
    }
    
    @NotNull
    public final String component2() {
        return this.sourceName;
    }
    
    @NotNull
    public final String component3() {
        return this.sourceIcon;
    }
    
    @Nullable
    public final String component4() {
        return this.sourceGroup;
    }
    
    @Nullable
    public final String component5() {
        return this.sourceComment;
    }
    
    public final boolean component6() {
        return this.enabled;
    }
    
    @Nullable
    public final String component7() {
        return this.variableComment;
    }
    
    @Nullable
    public final Boolean component8() {
        return this.getEnabledCookieJar();
    }
    
    @Nullable
    public final String component9() {
        return this.getConcurrentRate();
    }
    
    @Nullable
    public final String component10() {
        return this.getHeader();
    }
    
    @Nullable
    public final String component11() {
        return this.getLoginUrl();
    }
    
    @Nullable
    public final String component12() {
        return this.getLoginUi();
    }
    
    @Nullable
    public final String component13() {
        return this.loginCheckJs;
    }
    
    @Nullable
    public final String component14() {
        return this.sortUrl;
    }
    
    public final boolean component15() {
        return this.singleUrl;
    }
    
    public final int component16() {
        return this.articleStyle;
    }
    
    @Nullable
    public final String component17() {
        return this.ruleArticles;
    }
    
    @Nullable
    public final String component18() {
        return this.ruleNextPage;
    }
    
    @Nullable
    public final String component19() {
        return this.ruleTitle;
    }
    
    @Nullable
    public final String component20() {
        return this.rulePubDate;
    }
    
    @Nullable
    public final String component21() {
        return this.ruleDescription;
    }
    
    @Nullable
    public final String component22() {
        return this.ruleImage;
    }
    
    @Nullable
    public final String component23() {
        return this.ruleLink;
    }
    
    @Nullable
    public final String component24() {
        return this.ruleContent;
    }
    
    @Nullable
    public final String component25() {
        return this.style;
    }
    
    public final boolean component26() {
        return this.enableJs;
    }
    
    public final boolean component27() {
        return this.loadWithBaseUrl;
    }
    
    public final int component28() {
        return this.customOrder;
    }
    
    @NotNull
    public final RssSource copy(@NotNull final String sourceUrl, @NotNull final String sourceName, @NotNull final String sourceIcon, @Nullable final String sourceGroup, @Nullable final String sourceComment, final boolean enabled, @Nullable final String variableComment, @Nullable final Boolean enabledCookieJar, @Nullable final String concurrentRate, @Nullable final String header, @Nullable final String loginUrl, @Nullable final String loginUi, @Nullable final String loginCheckJs, @Nullable final String sortUrl, final boolean singleUrl, final int articleStyle, @Nullable final String ruleArticles, @Nullable final String ruleNextPage, @Nullable final String ruleTitle, @Nullable final String rulePubDate, @Nullable final String ruleDescription, @Nullable final String ruleImage, @Nullable final String ruleLink, @Nullable final String ruleContent, @Nullable final String style, final boolean enableJs, final boolean loadWithBaseUrl, final int customOrder) {
        Intrinsics.checkNotNullParameter((Object)sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter((Object)sourceName, "sourceName");
        Intrinsics.checkNotNullParameter((Object)sourceIcon, "sourceIcon");
        return new RssSource(sourceUrl, sourceName, sourceIcon, sourceGroup, sourceComment, enabled, variableComment, enabledCookieJar, concurrentRate, header, loginUrl, loginUi, loginCheckJs, sortUrl, singleUrl, articleStyle, ruleArticles, ruleNextPage, ruleTitle, rulePubDate, ruleDescription, ruleImage, ruleLink, ruleContent, style, enableJs, loadWithBaseUrl, customOrder);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RssSource(sourceUrl=").append(this.sourceUrl).append(", sourceName=").append(this.sourceName).append(", sourceIcon=").append(this.sourceIcon).append(", sourceGroup=").append((Object)this.sourceGroup).append(", sourceComment=").append((Object)this.sourceComment).append(", enabled=").append(this.enabled).append(", variableComment=").append((Object)this.variableComment).append(", enabledCookieJar=").append(this.getEnabledCookieJar()).append(", concurrentRate=").append((Object)this.getConcurrentRate()).append(", header=").append((Object)this.getHeader()).append(", loginUrl=").append((Object)this.getLoginUrl()).append(", loginUi=");
        sb.append((Object)this.getLoginUi()).append(", loginCheckJs=").append((Object)this.loginCheckJs).append(", sortUrl=").append((Object)this.sortUrl).append(", singleUrl=").append(this.singleUrl).append(", articleStyle=").append(this.articleStyle).append(", ruleArticles=").append((Object)this.ruleArticles).append(", ruleNextPage=").append((Object)this.ruleNextPage).append(", ruleTitle=").append((Object)this.ruleTitle).append(", rulePubDate=").append((Object)this.rulePubDate).append(", ruleDescription=").append((Object)this.ruleDescription).append(", ruleImage=").append((Object)this.ruleImage).append(", ruleLink=").append((Object)this.ruleLink);
        sb.append(", ruleContent=").append((Object)this.ruleContent).append(", style=").append((Object)this.style).append(", enableJs=").append(this.enableJs).append(", loadWithBaseUrl=").append(this.loadWithBaseUrl).append(", customOrder=").append(this.customOrder).append(')');
        return sb.toString();
    }
    
    public RssSource() {
        this(null, null, null, null, null, false, null, null, null, null, null, null, null, null, false, 0, null, null, null, null, null, null, null, null, null, false, false, 0, 268435455, null);
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\b\u0010\tJ4\u0010\n\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00050\u000bj\b\u0012\u0004\u0012\u00020\u0005`\f0\u00042\u0006\u0010\r\u001a\u00020\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u000e\u0010\tJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0010\u001a\u00020\u0011\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u0012\u0010\u0013\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001¡§\u0006\u0014" }, d2 = { "Lio/legado/app/data/entities/RssSource$Companion;", "", "()V", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/RssSource;", "json", "", "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "jsonArray", "fromJsonArray-IoAF18A", "fromJsonDoc", "doc", "Lcom/jayway/jsonpath/DocumentContext;", "fromJsonDoc-IoAF18A", "(Lcom/jayway/jsonpath/DocumentContext;)Ljava/lang/Object;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final Object fromJsonDoc-IoAF18A(@NotNull final DocumentContext doc) {
            Intrinsics.checkNotNullParameter((Object)doc, "doc");
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final String string = JsonExtensionsKt.readString((ReadContext)doc, "$.sourceUrl");
                Intrinsics.checkNotNull((Object)string);
                final String s = string;
                final String string2 = JsonExtensionsKt.readString((ReadContext)doc, "$.sourceName");
                Intrinsics.checkNotNull((Object)string2);
                final String s2 = string2;
                final String string3 = JsonExtensionsKt.readString((ReadContext)doc, "$.sourceIcon");
                final String s3 = (string3 == null) ? "" : string3;
                final String string4 = JsonExtensionsKt.readString((ReadContext)doc, "$.sourceGroup");
                final String string5 = JsonExtensionsKt.readString((ReadContext)doc, "$.sourceComment");
                final Boolean bool = JsonExtensionsKt.readBool((ReadContext)doc, "$.enabled");
                final boolean b = bool == null || bool;
                final String string6 = JsonExtensionsKt.readString((ReadContext)doc, "$.concurrentRate");
                final String string7 = JsonExtensionsKt.readString((ReadContext)doc, "$.header");
                final String string8 = JsonExtensionsKt.readString((ReadContext)doc, "$.loginUrl");
                final String string9 = JsonExtensionsKt.readString((ReadContext)doc, "$.loginCheckJs");
                final String string10 = JsonExtensionsKt.readString((ReadContext)doc, "$.sortUrl");
                final Boolean bool2 = JsonExtensionsKt.readBool((ReadContext)doc, "$.singleUrl");
                final boolean b2 = bool2 != null && bool2;
                final Integer int1 = JsonExtensionsKt.readInt((ReadContext)doc, "$.articleStyle");
                final int n2 = (int1 == null) ? 0 : int1;
                final String string11 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleArticles");
                final String string12 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleNextPage");
                final String string13 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleTitle");
                final String string14 = JsonExtensionsKt.readString((ReadContext)doc, "$.rulePubDate");
                final String string15 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleDescription");
                final String string16 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleImage");
                final String string17 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleLink");
                final String string18 = JsonExtensionsKt.readString((ReadContext)doc, "$.ruleContent");
                final String string19 = JsonExtensionsKt.readString((ReadContext)doc, "$.style");
                final Boolean bool3 = JsonExtensionsKt.readBool((ReadContext)doc, "$.enableJs");
                final boolean b3 = bool3 == null || bool3;
                final Boolean bool4 = JsonExtensionsKt.readBool((ReadContext)doc, "$.loadWithBaseUrl");
                final boolean b4 = bool4 == null || bool4;
                final Boolean bool5 = JsonExtensionsKt.readBool((ReadContext)doc, "$.enabledCookieJar");
                final boolean b5 = bool5 != null && bool5;
                final Integer int2 = JsonExtensionsKt.readInt((ReadContext)doc, "$.customOrder");
                o = Result.constructor-impl((Object)new RssSource(s, s2, s3, string4, string5, b, null, b5, string6, string7, string8, null, string9, string10, b2, n2, string11, string12, string13, string14, string15, string16, string17, string18, string19, b3, b4, (int2 == null) ? 0 : int2, 2112, null));
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            return o;
        }
        
        @NotNull
        public final Object fromJson-IoAF18A(@NotNull final String json) {
            Intrinsics.checkNotNullParameter((Object)json, "json");
            final DocumentContext parse = JsonExtensionsKt.getJsonPath().parse(json);
            Intrinsics.checkNotNullExpressionValue((Object)parse, "jsonPath.parse(json)");
            return this.fromJsonDoc-IoAF18A(parse);
        }
        
        @NotNull
        public final Object fromJsonArray-IoAF18A(@NotNull final String jsonArray) {
            Intrinsics.checkNotNullParameter((Object)jsonArray, "jsonArray");
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final ArrayList sources = new ArrayList();
                final List doc = (List)JsonExtensionsKt.getJsonPath().parse(jsonArray).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue((Object)doc, "doc");
                final Iterable $this$forEach$iv = doc;
                final int $i$f$forEach = 0;
                for (final Object it : $this$forEach$iv) {
                    final Object element$iv = it;
                    final int n2 = 0;
                    final DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse(it);
                    final Companion companion2 = RssSource.Companion;
                    Intrinsics.checkNotNullExpressionValue((Object)jsonItem, "jsonItem");
                    final Object fromJsonDoc-IoAF18A = companion2.fromJsonDoc-IoAF18A(jsonItem);
                    ResultKt.throwOnFailure(fromJsonDoc-IoAF18A);
                    final RssSource source = (RssSource)fromJsonDoc-IoAF18A;
                    final int n3 = 0;
                    sources.add(source);
                }
                o = Result.constructor-impl((Object)sources);
            }
            catch (final Throwable t) {
                final Result$Companion companion3 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            return o;
        }
    }
}
