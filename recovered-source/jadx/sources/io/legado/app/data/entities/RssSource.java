package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import com.script.SimpleBindings;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.JsonExtensionsKt;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: RssSource.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/RssSource.class */
@JsonIgnoreProperties({"headerMap", PackageDocumentBase.DCTags.source, "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b[\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\b\u0018\u0000 \u0099\u00012\u00020\u0001:\u0002\u0099\u0001BÃ\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\t\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001e\u001a\u00020\t\u0012\b\b\u0002\u0010\u001f\u001a\u00020\t\u0012\b\b\u0002\u0010 \u001a\u00020\u0014¢\u0006\u0002\u0010!J\t\u0010f\u001a\u00020\u0003HÆ\u0003J\u000b\u0010g\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010h\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010i\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010j\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010k\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010l\u001a\u00020\tHÆ\u0003J\t\u0010m\u001a\u00020\u0014HÆ\u0003J\u000b\u0010n\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010o\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010p\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010q\u001a\u00020\u0003HÆ\u0003J\u000b\u0010r\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010s\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010t\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010u\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010v\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010w\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010x\u001a\u00020\tHÆ\u0003J\t\u0010y\u001a\u00020\tHÆ\u0003J\t\u0010z\u001a\u00020\u0014HÆ\u0003J\t\u0010{\u001a\u00020\u0003HÆ\u0003J\u000b\u0010|\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010}\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010~\u001a\u00020\tHÆ\u0003J\u000b\u0010\u007f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0011\u0010\u0080\u0001\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u00106J\f\u0010\u0081\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003JÎ\u0002\u0010\u0082\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001e\u001a\u00020\t2\b\b\u0002\u0010\u001f\u001a\u00020\t2\b\b\u0002\u0010 \u001a\u00020\u0014HÆ\u0001¢\u0006\u0003\u0010\u0083\u0001J\u0010\u0010\u0084\u0001\u001a\u00020\t2\u0007\u0010\u0085\u0001\u001a\u00020\u0000J\u001f\u0010\u0084\u0001\u001a\u00020\t2\t\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u00032\t\u0010\u0087\u0001\u001a\u0004\u0018\u00010\u0003H\u0002J\u0016\u0010\u0088\u0001\u001a\u00020\t2\n\u0010\u0089\u0001\u001a\u0005\u0018\u00010\u008a\u0001H\u0096\u0002J\t\u0010\u008b\u0001\u001a\u00020\u0003H\u0016J\u000b\u0010\u008c\u0001\u001a\u0004\u0018\u00010.H\u0016J\t\u0010\u008d\u0001\u001a\u00020\u0003H\u0016J\t\u0010\u008e\u0001\u001a\u00020\u0003H\u0016J\t\u0010\u008f\u0001\u001a\u00020\u0014H\u0016J\u0013\u0010\u0090\u0001\u001a\u00030\u0091\u00012\t\u0010\u0092\u0001\u001a\u0004\u0018\u00010.J\u0011\u0010\u0093\u0001\u001a\u00030\u0091\u00012\u0007\u0010\u0094\u0001\u001a\u00020\u0003J\u001b\u0010\u0095\u0001\u001a\u0016\u0012\u0011\u0012\u000f\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0097\u00010\u0096\u0001J\n\u0010\u0098\u0001\u001a\u00020\u0003HÖ\u0001R\u000e\u0010\"\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010(\"\u0004\b)\u0010*R\u001a\u0010 \u001a\u00020\u0014X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010$\"\u0004\b,\u0010&R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u00100\"\u0004\b4\u00102R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0096\u000e¢\u0006\u0010\n\u0002\u00109\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010(\"\u0004\b;\u0010*R\u001a\u0010\u001f\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u00100\"\u0004\b=\u00102R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010(\"\u0004\b?\u0010*R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010(\"\u0004\bA\u0010*R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010(\"\u0004\bC\u0010*R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010(\"\u0004\bE\u0010*R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bF\u0010(\"\u0004\bG\u0010*R\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010(\"\u0004\bI\u0010*R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010(\"\u0004\bK\u0010*R\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010(\"\u0004\bM\u0010*R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bN\u0010(\"\u0004\bO\u0010*R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bP\u0010(\"\u0004\bQ\u0010*R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010(\"\u0004\bS\u0010*R\u001a\u0010\u0012\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bT\u00100\"\u0004\bU\u00102R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bV\u0010(\"\u0004\bW\u0010*R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010(\"\u0004\bY\u0010*R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010(\"\u0004\b[\u0010*R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010(\"\u0004\b]\u0010*R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b^\u0010(\"\u0004\b_\u0010*R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b`\u0010(\"\u0004\ba\u0010*R\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bb\u0010(\"\u0004\bc\u0010*R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bd\u0010(\"\u0004\be\u0010*¨\u0006\u009a\u0001"}, d2 = {"Lio/legado/app/data/entities/RssSource;", "Lio/legado/app/data/entities/BaseSource;", "sourceUrl", PackageDocumentBase.PREFIX_OPF, "sourceName", "sourceIcon", "sourceGroup", "sourceComment", "enabled", PackageDocumentBase.PREFIX_OPF, "variableComment", "enabledCookieJar", "concurrentRate", "header", "loginUrl", "loginUi", "loginCheckJs", "sortUrl", "singleUrl", "articleStyle", PackageDocumentBase.PREFIX_OPF, "ruleArticles", "ruleNextPage", "ruleTitle", "rulePubDate", "ruleDescription", "ruleImage", "ruleLink", "ruleContent", "style", "enableJs", "loadWithBaseUrl", "customOrder", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZI)V", "_userNameSpace", "getArticleStyle", "()I", "setArticleStyle", "(I)V", "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "getCustomOrder", "setCustomOrder", "debugLog", "Lio/legado/app/model/DebugLog;", "getEnableJs", "()Z", "setEnableJs", "(Z)V", "getEnabled", "setEnabled", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getHeader", "setHeader", "getLoadWithBaseUrl", "setLoadWithBaseUrl", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "setLoginUi", "getLoginUrl", "setLoginUrl", "getRuleArticles", "setRuleArticles", "getRuleContent", "setRuleContent", "getRuleDescription", "setRuleDescription", "getRuleImage", "setRuleImage", "getRuleLink", "setRuleLink", "getRuleNextPage", "setRuleNextPage", "getRulePubDate", "setRulePubDate", "getRuleTitle", "setRuleTitle", "getSingleUrl", "setSingleUrl", "getSortUrl", "setSortUrl", "getSourceComment", "setSourceComment", "getSourceGroup", "setSourceGroup", "getSourceIcon", "setSourceIcon", "getSourceName", "setSourceName", "getSourceUrl", "setSourceUrl", "getStyle", "setStyle", "getVariableComment", "setVariableComment", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZI)Lio/legado/app/data/entities/RssSource;", "equal", PackageDocumentBase.DCTags.source, NCXDocumentV3.XHTMLTgs.a, "b", "equals", "other", PackageDocumentBase.PREFIX_OPF, "getKey", "getLogger", "getTag", "getUserNameSpace", "hashCode", "setLogger", PackageDocumentBase.PREFIX_OPF, "logger", "setUserNameSpace", "nameSpace", "sortUrls", PackageDocumentBase.PREFIX_OPF, "Lkotlin/Pair;", "toString", "Companion", "reader-pro"})
public final /* data */ class RssSource implements BaseSource {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

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
        return getEnabledCookieJar();
    }

    @Nullable
    public final String component9() {
        return getConcurrentRate();
    }

    @Nullable
    public final String component10() {
        return getHeader();
    }

    @Nullable
    public final String component11() {
        return getLoginUrl();
    }

    @Nullable
    public final String component12() {
        return getLoginUi();
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
    public final RssSource copy(@NotNull String sourceUrl, @NotNull String sourceName, @NotNull String sourceIcon, @Nullable String sourceGroup, @Nullable String sourceComment, boolean enabled, @Nullable String variableComment, @Nullable Boolean enabledCookieJar, @Nullable String concurrentRate, @Nullable String header, @Nullable String loginUrl, @Nullable String loginUi, @Nullable String loginCheckJs, @Nullable String sortUrl, boolean singleUrl, int articleStyle, @Nullable String ruleArticles, @Nullable String ruleNextPage, @Nullable String ruleTitle, @Nullable String rulePubDate, @Nullable String ruleDescription, @Nullable String ruleImage, @Nullable String ruleLink, @Nullable String ruleContent, @Nullable String style, boolean enableJs, boolean loadWithBaseUrl, int customOrder) {
        Intrinsics.checkNotNullParameter(sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter(sourceName, "sourceName");
        Intrinsics.checkNotNullParameter(sourceIcon, "sourceIcon");
        return new RssSource(sourceUrl, sourceName, sourceIcon, sourceGroup, sourceComment, enabled, variableComment, enabledCookieJar, concurrentRate, header, loginUrl, loginUi, loginCheckJs, sortUrl, singleUrl, articleStyle, ruleArticles, ruleNextPage, ruleTitle, rulePubDate, ruleDescription, ruleImage, ruleLink, ruleContent, style, enableJs, loadWithBaseUrl, customOrder);
    }

    public static /* synthetic */ RssSource copy$default(RssSource rssSource, String str, String str2, String str3, String str4, String str5, boolean z, String str6, Boolean bool, String str7, String str8, String str9, String str10, String str11, String str12, boolean z2, int i, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, boolean z3, boolean z4, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = rssSource.sourceUrl;
        }
        if ((i3 & 2) != 0) {
            str2 = rssSource.sourceName;
        }
        if ((i3 & 4) != 0) {
            str3 = rssSource.sourceIcon;
        }
        if ((i3 & 8) != 0) {
            str4 = rssSource.sourceGroup;
        }
        if ((i3 & 16) != 0) {
            str5 = rssSource.sourceComment;
        }
        if ((i3 & 32) != 0) {
            z = rssSource.enabled;
        }
        if ((i3 & 64) != 0) {
            str6 = rssSource.variableComment;
        }
        if ((i3 & Wbxml.EXT_T_0) != 0) {
            bool = rssSource.getEnabledCookieJar();
        }
        if ((i3 & 256) != 0) {
            str7 = rssSource.getConcurrentRate();
        }
        if ((i3 & 512) != 0) {
            str8 = rssSource.getHeader();
        }
        if ((i3 & 1024) != 0) {
            str9 = rssSource.getLoginUrl();
        }
        if ((i3 & 2048) != 0) {
            str10 = rssSource.getLoginUi();
        }
        if ((i3 & 4096) != 0) {
            str11 = rssSource.loginCheckJs;
        }
        if ((i3 & IOUtil.DEFAULT_BUFFER_SIZE) != 0) {
            str12 = rssSource.sortUrl;
        }
        if ((i3 & 16384) != 0) {
            z2 = rssSource.singleUrl;
        }
        if ((i3 & 32768) != 0) {
            i = rssSource.articleStyle;
        }
        if ((i3 & 65536) != 0) {
            str13 = rssSource.ruleArticles;
        }
        if ((i3 & 131072) != 0) {
            str14 = rssSource.ruleNextPage;
        }
        if ((i3 & 262144) != 0) {
            str15 = rssSource.ruleTitle;
        }
        if ((i3 & 524288) != 0) {
            str16 = rssSource.rulePubDate;
        }
        if ((i3 & 1048576) != 0) {
            str17 = rssSource.ruleDescription;
        }
        if ((i3 & 2097152) != 0) {
            str18 = rssSource.ruleImage;
        }
        if ((i3 & 4194304) != 0) {
            str19 = rssSource.ruleLink;
        }
        if ((i3 & 8388608) != 0) {
            str20 = rssSource.ruleContent;
        }
        if ((i3 & 16777216) != 0) {
            str21 = rssSource.style;
        }
        if ((i3 & 33554432) != 0) {
            z3 = rssSource.enableJs;
        }
        if ((i3 & 67108864) != 0) {
            z4 = rssSource.loadWithBaseUrl;
        }
        if ((i3 & 134217728) != 0) {
            i2 = rssSource.customOrder;
        }
        return rssSource.copy(str, str2, str3, str4, str5, z, str6, bool, str7, str8, str9, str10, str11, str12, z2, i, str13, str14, str15, str16, str17, str18, str19, str20, str21, z3, z4, i2);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RssSource(sourceUrl=").append(this.sourceUrl).append(", sourceName=").append(this.sourceName).append(", sourceIcon=").append(this.sourceIcon).append(", sourceGroup=").append((Object) this.sourceGroup).append(", sourceComment=").append((Object) this.sourceComment).append(", enabled=").append(this.enabled).append(", variableComment=").append((Object) this.variableComment).append(", enabledCookieJar=").append(getEnabledCookieJar()).append(", concurrentRate=").append((Object) getConcurrentRate()).append(", header=").append((Object) getHeader()).append(", loginUrl=").append((Object) getLoginUrl()).append(", loginUi=");
        sb.append((Object) getLoginUi()).append(", loginCheckJs=").append((Object) this.loginCheckJs).append(", sortUrl=").append((Object) this.sortUrl).append(", singleUrl=").append(this.singleUrl).append(", articleStyle=").append(this.articleStyle).append(", ruleArticles=").append((Object) this.ruleArticles).append(", ruleNextPage=").append((Object) this.ruleNextPage).append(", ruleTitle=").append((Object) this.ruleTitle).append(", rulePubDate=").append((Object) this.rulePubDate).append(", ruleDescription=").append((Object) this.ruleDescription).append(", ruleImage=").append((Object) this.ruleImage).append(", ruleLink=").append((Object) this.ruleLink);
        sb.append(", ruleContent=").append((Object) this.ruleContent).append(", style=").append((Object) this.style).append(", enableJs=").append(this.enableJs).append(", loadWithBaseUrl=").append(this.loadWithBaseUrl).append(", customOrder=").append(this.customOrder).append(')');
        return sb.toString();
    }

    public RssSource() {
        this(null, null, null, null, null, false, null, null, null, null, null, null, null, null, false, 0, null, null, null, null, null, null, null, null, null, false, false, 0, 268435455, null);
    }

    public RssSource(@NotNull String sourceUrl, @NotNull String sourceName, @NotNull String sourceIcon, @Nullable String sourceGroup, @Nullable String sourceComment, boolean enabled, @Nullable String variableComment, @Nullable Boolean enabledCookieJar, @Nullable String concurrentRate, @Nullable String header, @Nullable String loginUrl, @Nullable String loginUi, @Nullable String loginCheckJs, @Nullable String sortUrl, boolean singleUrl, int articleStyle, @Nullable String ruleArticles, @Nullable String ruleNextPage, @Nullable String ruleTitle, @Nullable String rulePubDate, @Nullable String ruleDescription, @Nullable String ruleImage, @Nullable String ruleLink, @Nullable String ruleContent, @Nullable String style, boolean enableJs, boolean loadWithBaseUrl, int customOrder) {
        Intrinsics.checkNotNullParameter(sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter(sourceName, "sourceName");
        Intrinsics.checkNotNullParameter(sourceIcon, "sourceIcon");
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
        this._userNameSpace = PackageDocumentBase.PREFIX_OPF;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public Object evalJS(@NotNull String jsStr, @NotNull Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception {
        return BaseSource.DefaultImpls.evalJS(this, jsStr, bindingsConfig);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] aesBase64DecodeToByteArray(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesBase64DecodeToByteArray(this, str, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String aesBase64DecodeToString(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesBase64DecodeToString(this, str, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String aesDecodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesDecodeArgsBase64Str(this, data, key, mode, padding, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] aesDecodeToByteArray(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesDecodeToByteArray(this, str, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String aesDecodeToString(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesDecodeToString(this, str, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String aesEncodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesEncodeArgsBase64Str(this, data, key, mode, padding, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] aesEncodeToBase64ByteArray(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesEncodeToBase64ByteArray(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String aesEncodeToBase64String(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesEncodeToBase64String(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] aesEncodeToByteArray(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesEncodeToByteArray(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String aesEncodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.aesEncodeToString(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String ajax(@NotNull String urlStr) {
        return BaseSource.DefaultImpls.ajax(this, urlStr);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public StrResponse[] ajaxAll(@NotNull String[] urlList) {
        return BaseSource.DefaultImpls.ajaxAll(this, urlList);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String androidId() {
        return BaseSource.DefaultImpls.androidId(this);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String base64Decode(@NotNull String str) {
        return BaseSource.DefaultImpls.base64Decode(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String base64Decode(@NotNull String str, int flags) {
        return BaseSource.DefaultImpls.base64Decode(this, str, flags);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] base64DecodeToByteArray(@Nullable String str) {
        return BaseSource.DefaultImpls.base64DecodeToByteArray(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] base64DecodeToByteArray(@Nullable String str, int flags) {
        return BaseSource.DefaultImpls.base64DecodeToByteArray(this, str, flags);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String base64Encode(@NotNull String str) {
        return BaseSource.DefaultImpls.base64Encode(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String base64Encode(@NotNull String str, int flags) {
        return BaseSource.DefaultImpls.base64Encode(this, str, flags);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String cacheFile(@NotNull String urlStr) {
        return BaseSource.DefaultImpls.cacheFile(this, urlStr);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String cacheFile(@NotNull String urlStr, int saveTime) {
        return BaseSource.DefaultImpls.cacheFile(this, urlStr, saveTime);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public StrResponse connect(@NotNull String urlStr) {
        return BaseSource.DefaultImpls.connect(this, urlStr);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public StrResponse connect(@NotNull String urlStr, @Nullable String header) {
        return BaseSource.DefaultImpls.connect(this, urlStr, header);
    }

    @Override // io.legado.app.help.JsExtensions
    public void deleteFile(@NotNull String path) {
        BaseSource.DefaultImpls.deleteFile(this, path);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String desBase64DecodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.desBase64DecodeToString(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String desDecodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.desDecodeToString(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String desEncodeToBase64String(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.desEncodeToBase64String(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String desEncodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
        return BaseSource.DefaultImpls.desEncodeToString(this, data, key, transformation, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String digestBase64Str(@NotNull String data, @NotNull String algorithm) {
        return BaseSource.DefaultImpls.digestBase64Str(this, data, algorithm);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String digestHex(@NotNull String data, @NotNull String algorithm) {
        return BaseSource.DefaultImpls.digestHex(this, data, algorithm);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String downloadFile(@NotNull String content, @NotNull String url) {
        return BaseSource.DefaultImpls.downloadFile(this, content, url);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String encodeURI(@NotNull String str) {
        return BaseSource.DefaultImpls.encodeURI(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String encodeURI(@NotNull String str, @NotNull String enc) {
        return BaseSource.DefaultImpls.encodeURI(this, str, enc);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public Connection.Response get(@NotNull String urlStr, @NotNull Map<String, String> headers) {
        return BaseSource.DefaultImpls.get(this, urlStr, headers);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String getCookie(@NotNull String tag, @Nullable String key) {
        return BaseSource.DefaultImpls.getCookie(this, tag, key);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public File getFile(@NotNull String path) {
        return BaseSource.DefaultImpls.getFile(this, path);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public HashMap<String, String> getHeaderMap(boolean hasLoginHeader) {
        return BaseSource.DefaultImpls.getHeaderMap(this, hasLoginHeader);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getLoginHeader() {
        return BaseSource.DefaultImpls.getLoginHeader(this);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public Map<String, String> getLoginHeaderMap() {
        return BaseSource.DefaultImpls.getLoginHeaderMap(this);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getLoginInfo() {
        return BaseSource.DefaultImpls.getLoginInfo(this);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public Map<String, String> getLoginInfoMap() {
        return BaseSource.DefaultImpls.getLoginInfoMap(this);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getLoginJs() {
        return BaseSource.DefaultImpls.getLoginJs(this);
    }

    @Override // io.legado.app.data.entities.BaseSource, io.legado.app.help.JsExtensions
    @Nullable
    public BaseSource getSource() {
        return BaseSource.DefaultImpls.getSource(this);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String getTxtInFolder(@NotNull String unzipPath) {
        return BaseSource.DefaultImpls.getTxtInFolder(this, unzipPath);
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getVariable() {
        return BaseSource.DefaultImpls.getVariable(this);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] getZipByteArrayContent(@NotNull String url, @NotNull String path) {
        return BaseSource.DefaultImpls.getZipByteArrayContent(this, url, path);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String getZipStringContent(@NotNull String url, @NotNull String path) {
        return BaseSource.DefaultImpls.getZipStringContent(this, url, path);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String getZipStringContent(@NotNull String url, @NotNull String path, @NotNull String charsetName) {
        return BaseSource.DefaultImpls.getZipStringContent(this, url, path, charsetName);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public Connection.Response head(@NotNull String urlStr, @NotNull Map<String, String> headers) {
        return BaseSource.DefaultImpls.head(this, urlStr, headers);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String htmlFormat(@NotNull String str) {
        return BaseSource.DefaultImpls.htmlFormat(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String importScript(@NotNull String path) {
        return BaseSource.DefaultImpls.importScript(this, path);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String log(@NotNull String msg) {
        return BaseSource.DefaultImpls.log(this, msg);
    }

    @Override // io.legado.app.help.JsExtensions
    public void logType(@Nullable Object any) {
        BaseSource.DefaultImpls.logType(this, any);
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void login() throws Exception {
        BaseSource.DefaultImpls.login(this);
    }

    @Override // io.legado.app.help.JsExtensions
    public void longToast(@Nullable Object msg) {
        BaseSource.DefaultImpls.longToast(this, msg);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String md5Encode(@NotNull String str) {
        return BaseSource.DefaultImpls.md5Encode(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String md5Encode16(@NotNull String str) {
        return BaseSource.DefaultImpls.md5Encode16(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public Connection.Response post(@NotNull String urlStr, @NotNull String body, @NotNull Map<String, String> headers) {
        return BaseSource.DefaultImpls.post(this, urlStr, body, headers);
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void putLoginHeader(@NotNull String header) {
        BaseSource.DefaultImpls.putLoginHeader(this, header);
    }

    @Override // io.legado.app.data.entities.BaseSource
    public boolean putLoginInfo(@NotNull String info) {
        return BaseSource.DefaultImpls.putLoginInfo(this, info);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public QueryTTF queryBase64TTF(@Nullable String base64) {
        return BaseSource.DefaultImpls.queryBase64TTF(this, base64);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public QueryTTF queryTTF(@Nullable String str) {
        return BaseSource.DefaultImpls.queryTTF(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String randomUUID() {
        return BaseSource.DefaultImpls.randomUUID(this);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public byte[] readFile(@NotNull String path) {
        return BaseSource.DefaultImpls.readFile(this, path);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String readTxtFile(@NotNull String path) {
        return BaseSource.DefaultImpls.readTxtFile(this, path);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String readTxtFile(@NotNull String path, @NotNull String charsetName) {
        return BaseSource.DefaultImpls.readTxtFile(this, path, charsetName);
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void removeLoginHeader() {
        BaseSource.DefaultImpls.removeLoginHeader(this);
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void removeLoginInfo() {
        BaseSource.DefaultImpls.removeLoginInfo(this);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String replaceFont(@NotNull String text, @Nullable QueryTTF font1, @Nullable QueryTTF font2) {
        return BaseSource.DefaultImpls.replaceFont(this, text, font1, font2);
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setVariable(@Nullable String variable) {
        BaseSource.DefaultImpls.setVariable(this, variable);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String timeFormat(long time) {
        return BaseSource.DefaultImpls.timeFormat(this, time);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String timeFormatUTC(long time, @NotNull String format, int sh) {
        return BaseSource.DefaultImpls.timeFormatUTC(this, time, format, sh);
    }

    @Override // io.legado.app.help.JsExtensions
    public void toast(@Nullable Object msg) {
        BaseSource.DefaultImpls.toast(this, msg);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String tripleDESDecodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
        return BaseSource.DefaultImpls.tripleDESDecodeArgsBase64Str(this, data, key, mode, padding, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String tripleDESDecodeStr(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
        return BaseSource.DefaultImpls.tripleDESDecodeStr(this, data, key, mode, padding, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String tripleDESEncodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
        return BaseSource.DefaultImpls.tripleDESEncodeArgsBase64Str(this, data, key, mode, padding, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String tripleDESEncodeBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
        return BaseSource.DefaultImpls.tripleDESEncodeBase64Str(this, data, key, mode, padding, iv);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String unzipFile(@NotNull String zipPath) {
        return BaseSource.DefaultImpls.unzipFile(this, zipPath);
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String utf8ToGbk(@NotNull String str) {
        return BaseSource.DefaultImpls.utf8ToGbk(this, str);
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public String webView(@Nullable String html, @Nullable String url, @Nullable String js) {
        return BaseSource.DefaultImpls.webView(this, html, url, js);
    }

    public /* synthetic */ RssSource(String str, String str2, String str3, String str4, String str5, boolean z, String str6, Boolean bool, String str7, String str8, String str9, String str10, String str11, String str12, boolean z2, int i, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, boolean z3, boolean z4, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i3 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i3 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i3 & 8) != 0 ? null : str4, (i3 & 16) != 0 ? null : str5, (i3 & 32) != 0 ? true : z, (i3 & 64) != 0 ? null : str6, (i3 & Wbxml.EXT_T_0) != 0 ? false : bool, (i3 & 256) != 0 ? null : str7, (i3 & 512) != 0 ? null : str8, (i3 & 1024) != 0 ? null : str9, (i3 & 2048) != 0 ? null : str10, (i3 & 4096) != 0 ? null : str11, (i3 & IOUtil.DEFAULT_BUFFER_SIZE) != 0 ? null : str12, (i3 & 16384) != 0 ? false : z2, (i3 & 32768) != 0 ? 0 : i, (i3 & 65536) != 0 ? null : str13, (i3 & 131072) != 0 ? null : str14, (i3 & 262144) != 0 ? null : str15, (i3 & 524288) != 0 ? null : str16, (i3 & 1048576) != 0 ? null : str17, (i3 & 2097152) != 0 ? null : str18, (i3 & 4194304) != 0 ? null : str19, (i3 & 8388608) != 0 ? null : str20, (i3 & 16777216) != 0 ? null : str21, (i3 & 33554432) != 0 ? true : z3, (i3 & 67108864) != 0 ? true : z4, (i3 & 134217728) != 0 ? 0 : i2);
    }

    @NotNull
    public final String getSourceUrl() {
        return this.sourceUrl;
    }

    public final void setSourceUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.sourceUrl = str;
    }

    @NotNull
    public final String getSourceName() {
        return this.sourceName;
    }

    public final void setSourceName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.sourceName = str;
    }

    @NotNull
    public final String getSourceIcon() {
        return this.sourceIcon;
    }

    public final void setSourceIcon(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.sourceIcon = str;
    }

    @Nullable
    public final String getSourceGroup() {
        return this.sourceGroup;
    }

    public final void setSourceGroup(@Nullable String str) {
        this.sourceGroup = str;
    }

    @Nullable
    public final String getSourceComment() {
        return this.sourceComment;
    }

    public final void setSourceComment(@Nullable String str) {
        this.sourceComment = str;
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final void setEnabled(boolean z) {
        this.enabled = z;
    }

    @Nullable
    public final String getVariableComment() {
        return this.variableComment;
    }

    public final void setVariableComment(@Nullable String str) {
        this.variableComment = str;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public Boolean getEnabledCookieJar() {
        return this.enabledCookieJar;
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setEnabledCookieJar(@Nullable Boolean bool) {
        this.enabledCookieJar = bool;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getConcurrentRate() {
        return this.concurrentRate;
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setConcurrentRate(@Nullable String str) {
        this.concurrentRate = str;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getHeader() {
        return this.header;
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setHeader(@Nullable String str) {
        this.header = str;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getLoginUrl() {
        return this.loginUrl;
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setLoginUrl(@Nullable String str) {
        this.loginUrl = str;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getLoginUi() {
        return this.loginUi;
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setLoginUi(@Nullable String str) {
        this.loginUi = str;
    }

    @Nullable
    public final String getLoginCheckJs() {
        return this.loginCheckJs;
    }

    public final void setLoginCheckJs(@Nullable String str) {
        this.loginCheckJs = str;
    }

    @Nullable
    public final String getSortUrl() {
        return this.sortUrl;
    }

    public final void setSortUrl(@Nullable String str) {
        this.sortUrl = str;
    }

    public final boolean getSingleUrl() {
        return this.singleUrl;
    }

    public final void setSingleUrl(boolean z) {
        this.singleUrl = z;
    }

    public final int getArticleStyle() {
        return this.articleStyle;
    }

    public final void setArticleStyle(int i) {
        this.articleStyle = i;
    }

    @Nullable
    public final String getRuleArticles() {
        return this.ruleArticles;
    }

    public final void setRuleArticles(@Nullable String str) {
        this.ruleArticles = str;
    }

    @Nullable
    public final String getRuleNextPage() {
        return this.ruleNextPage;
    }

    public final void setRuleNextPage(@Nullable String str) {
        this.ruleNextPage = str;
    }

    @Nullable
    public final String getRuleTitle() {
        return this.ruleTitle;
    }

    public final void setRuleTitle(@Nullable String str) {
        this.ruleTitle = str;
    }

    @Nullable
    public final String getRulePubDate() {
        return this.rulePubDate;
    }

    public final void setRulePubDate(@Nullable String str) {
        this.rulePubDate = str;
    }

    @Nullable
    public final String getRuleDescription() {
        return this.ruleDescription;
    }

    public final void setRuleDescription(@Nullable String str) {
        this.ruleDescription = str;
    }

    @Nullable
    public final String getRuleImage() {
        return this.ruleImage;
    }

    public final void setRuleImage(@Nullable String str) {
        this.ruleImage = str;
    }

    @Nullable
    public final String getRuleLink() {
        return this.ruleLink;
    }

    public final void setRuleLink(@Nullable String str) {
        this.ruleLink = str;
    }

    @Nullable
    public final String getRuleContent() {
        return this.ruleContent;
    }

    public final void setRuleContent(@Nullable String str) {
        this.ruleContent = str;
    }

    @Nullable
    public final String getStyle() {
        return this.style;
    }

    public final void setStyle(@Nullable String str) {
        this.style = str;
    }

    public final boolean getEnableJs() {
        return this.enableJs;
    }

    public final void setEnableJs(boolean z) {
        this.enableJs = z;
    }

    public final boolean getLoadWithBaseUrl() {
        return this.loadWithBaseUrl;
    }

    public final void setLoadWithBaseUrl(boolean z) {
        this.loadWithBaseUrl = z;
    }

    public final int getCustomOrder() {
        return this.customOrder;
    }

    public final void setCustomOrder(int i) {
        this.customOrder = i;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public String getTag() {
        return this.sourceName;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public String getKey() {
        return this.sourceUrl;
    }

    public boolean equals(@Nullable Object other) {
        if (other instanceof RssSource) {
            return Intrinsics.areEqual(((RssSource) other).sourceUrl, this.sourceUrl);
        }
        return false;
    }

    public int hashCode() {
        return this.sourceUrl.hashCode();
    }

    public final boolean equal(@NotNull RssSource source) {
        Intrinsics.checkNotNullParameter(source, PackageDocumentBase.DCTags.source);
        return equal(this.sourceUrl, source.sourceUrl) && equal(this.sourceIcon, source.sourceIcon) && this.enabled == source.enabled && Intrinsics.areEqual(getEnabledCookieJar(), source.getEnabledCookieJar()) && equal(this.sourceComment, source.sourceComment) && equal(this.sourceGroup, source.sourceGroup) && equal(this.ruleArticles, source.ruleArticles) && equal(this.ruleNextPage, source.ruleNextPage) && equal(this.ruleTitle, source.ruleTitle) && equal(this.rulePubDate, source.rulePubDate) && equal(this.ruleDescription, source.ruleDescription) && equal(this.ruleLink, source.ruleLink) && equal(this.ruleContent, source.ruleContent) && this.enableJs == source.enableJs && this.loadWithBaseUrl == source.loadWithBaseUrl;
    }

    private final boolean equal(String a, String b) {
        if (!Intrinsics.areEqual(a, b)) {
            String str = a;
            if (str == null || str.length() == 0) {
                String str2 = b;
                if (str2 == null || str2.length() == 0) {
                }
            }
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x006d A[Catch: Throwable -> 0x01f3, TryCatch #0 {Throwable -> 0x01f3, blocks: (B:3:0x0017, B:11:0x0049, B:46:0x01c5, B:48:0x01cd, B:49:0x01e0, B:35:0x0117, B:38:0x014a, B:39:0x015d, B:41:0x0167, B:43:0x01a3, B:14:0x0058, B:19:0x006d, B:21:0x0081, B:23:0x0097, B:24:0x00a1, B:25:0x00a2, B:31:0x00f8, B:26:0x00b1, B:28:0x00df, B:29:0x00e9, B:30:0x00ea, B:6:0x0035), top: B:54:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0167 A[Catch: Throwable -> 0x01f3, TryCatch #0 {Throwable -> 0x01f3, blocks: (B:3:0x0017, B:11:0x0049, B:46:0x01c5, B:48:0x01cd, B:49:0x01e0, B:35:0x0117, B:38:0x014a, B:39:0x015d, B:41:0x0167, B:43:0x01a3, B:14:0x0058, B:19:0x006d, B:21:0x0081, B:23:0x0097, B:24:0x00a1, B:25:0x00a2, B:31:0x00f8, B:26:0x00b1, B:28:0x00df, B:29:0x00e9, B:30:0x00ea, B:6:0x0035), top: B:54:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01cd A[Catch: Throwable -> 0x01f3, TryCatch #0 {Throwable -> 0x01f3, blocks: (B:3:0x0017, B:11:0x0049, B:46:0x01c5, B:48:0x01cd, B:49:0x01e0, B:35:0x0117, B:38:0x014a, B:39:0x015d, B:41:0x0167, B:43:0x01a3, B:14:0x0058, B:19:0x006d, B:21:0x0081, B:23:0x0097, B:24:0x00a1, B:25:0x00a2, B:31:0x00f8, B:26:0x00b1, B:28:0x00df, B:29:0x00e9, B:30:0x00ea, B:6:0x0035), top: B:54:0x0017 }] */
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final List<Pair<String, String>> sortUrls() {
        String strSubstring;
        String str;
        Iterable iterableSplit;
        ArrayList $this$sortUrls_u24lambda_u2d2 = new ArrayList();
        try {
            Result.Companion companion = Result.Companion;
            String a = getSortUrl();
            String sortUrl = getSortUrl();
            boolean z = sortUrl != null && StringsKt.startsWith(sortUrl, "<js>", false);
            if (!z) {
                String sortUrl2 = getSortUrl();
                boolean z2 = sortUrl2 != null && StringsKt.startsWith(sortUrl2, "@js:", false);
                if (z2) {
                }
                str = a;
                if (str != null) {
                    Iterable $this$forEach$iv = iterableSplit;
                    while (r0.hasNext()) {
                    }
                }
                if ($this$sortUrls_u24lambda_u2d2.isEmpty()) {
                }
                Result.constructor-impl(Unit.INSTANCE);
            } else {
                String sortUrl3 = getSortUrl();
                Intrinsics.checkNotNull(sortUrl3);
                if (StringsKt.startsWith$default(sortUrl3, "@", false, 2, (Object) null)) {
                    String sortUrl4 = getSortUrl();
                    Intrinsics.checkNotNull(sortUrl4);
                    if (sortUrl4 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    strSubstring = sortUrl4.substring(4);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                } else {
                    String sortUrl5 = getSortUrl();
                    Intrinsics.checkNotNull(sortUrl5);
                    String sortUrl6 = getSortUrl();
                    Intrinsics.checkNotNull(sortUrl6);
                    int iLastIndexOf$default = StringsKt.lastIndexOf$default(sortUrl6, "<", 0, false, 6, (Object) null);
                    if (sortUrl5 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    strSubstring = sortUrl5.substring(4, iLastIndexOf$default);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                }
                String jsStr = strSubstring;
                a = String.valueOf(BaseSource.DefaultImpls.evalJS$default(this, jsStr, null, 2, null));
                str = a;
                if (str != null && (iterableSplit = new Regex("(&&|\n)+").split(str, 0)) != null) {
                    Iterable $this$forEach$iv2 = iterableSplit;
                    for (Object element$iv : $this$forEach$iv2) {
                        String c = (String) element$iv;
                        List d = StringsKt.split$default(c, new String[]{"::"}, false, 0, 6, (Object) null);
                        if (d.size() > 1) {
                            $this$sortUrls_u24lambda_u2d2.add(new Pair(d.get(0), d.get(1)));
                        }
                    }
                }
                if ($this$sortUrls_u24lambda_u2d2.isEmpty()) {
                    $this$sortUrls_u24lambda_u2d2.add(new Pair(PackageDocumentBase.PREFIX_OPF, getSourceUrl()));
                }
                Result.constructor-impl(Unit.INSTANCE);
            }
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            Result.constructor-impl(ResultKt.createFailure(th));
        }
        return $this$sortUrls_u24lambda_u2d2;
    }

    public final void setUserNameSpace(@NotNull String nameSpace) {
        Intrinsics.checkNotNullParameter(nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }

    @Override // io.legado.app.help.JsExtensions
    @NotNull
    public String getUserNameSpace() {
        return this._userNameSpace;
    }

    public final void setLogger(@Nullable DebugLog logger) {
        this.debugLog = logger;
    }

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    public DebugLog getLogger() {
        return null;
    }

    /* JADX INFO: compiled from: RssSource.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/RssSource$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\b\u0010\tJ4\u0010\n\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00050\u000bj\b\u0012\u0004\u0012\u00020\u0005`\f0\u00042\u0006\u0010\r\u001a\u00020\u0007ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u000e\u0010\tJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0010\u001a\u00020\u0011ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0012\u0010\u0013\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u0014"}, d2 = {"Lio/legado/app/data/entities/RssSource$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/RssSource;", "json", PackageDocumentBase.PREFIX_OPF, "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "jsonArray", "fromJsonArray-IoAF18A", "fromJsonDoc", "doc", "Lcom/jayway/jsonpath/DocumentContext;", "fromJsonDoc-IoAF18A", "(Lcom/jayway/jsonpath/DocumentContext;)Ljava/lang/Object;", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonDoc-IoAF18A, reason: not valid java name */
        public final Object m161fromJsonDocIoAF18A(@NotNull DocumentContext doc) {
            Object obj;
            Intrinsics.checkNotNullParameter(doc, "doc");
            try {
                Result.Companion companion = Result.Companion;
                String string = JsonExtensionsKt.readString((ReadContext) doc, "$.sourceUrl");
                Intrinsics.checkNotNull(string);
                String string2 = JsonExtensionsKt.readString((ReadContext) doc, "$.sourceName");
                Intrinsics.checkNotNull(string2);
                String string3 = JsonExtensionsKt.readString((ReadContext) doc, "$.sourceIcon");
                String str = string3 == null ? PackageDocumentBase.PREFIX_OPF : string3;
                String string4 = JsonExtensionsKt.readString((ReadContext) doc, "$.sourceGroup");
                String string5 = JsonExtensionsKt.readString((ReadContext) doc, "$.sourceComment");
                Boolean bool = JsonExtensionsKt.readBool((ReadContext) doc, "$.enabled");
                boolean zBooleanValue = bool == null ? true : bool.booleanValue();
                String string6 = JsonExtensionsKt.readString((ReadContext) doc, "$.concurrentRate");
                String string7 = JsonExtensionsKt.readString((ReadContext) doc, "$.header");
                String string8 = JsonExtensionsKt.readString((ReadContext) doc, "$.loginUrl");
                String string9 = JsonExtensionsKt.readString((ReadContext) doc, "$.loginCheckJs");
                String string10 = JsonExtensionsKt.readString((ReadContext) doc, "$.sortUrl");
                Boolean bool2 = JsonExtensionsKt.readBool((ReadContext) doc, "$.singleUrl");
                boolean zBooleanValue2 = bool2 == null ? false : bool2.booleanValue();
                Integer num = JsonExtensionsKt.readInt((ReadContext) doc, "$.articleStyle");
                int iIntValue = num == null ? 0 : num.intValue();
                String string11 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleArticles");
                String string12 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleNextPage");
                String string13 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleTitle");
                String string14 = JsonExtensionsKt.readString((ReadContext) doc, "$.rulePubDate");
                String string15 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleDescription");
                String string16 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleImage");
                String string17 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleLink");
                String string18 = JsonExtensionsKt.readString((ReadContext) doc, "$.ruleContent");
                String string19 = JsonExtensionsKt.readString((ReadContext) doc, "$.style");
                Boolean bool3 = JsonExtensionsKt.readBool((ReadContext) doc, "$.enableJs");
                boolean zBooleanValue3 = bool3 == null ? true : bool3.booleanValue();
                Boolean bool4 = JsonExtensionsKt.readBool((ReadContext) doc, "$.loadWithBaseUrl");
                boolean zBooleanValue4 = bool4 == null ? true : bool4.booleanValue();
                Boolean bool5 = JsonExtensionsKt.readBool((ReadContext) doc, "$.enabledCookieJar");
                boolean zBooleanValue5 = bool5 == null ? false : bool5.booleanValue();
                Integer num2 = JsonExtensionsKt.readInt((ReadContext) doc, "$.customOrder");
                obj = Result.constructor-impl(new RssSource(string, string2, str, string4, string5, zBooleanValue, null, Boolean.valueOf(zBooleanValue5), string6, string7, string8, null, string9, string10, zBooleanValue2, iIntValue, string11, string12, string13, string14, string15, string16, string17, string18, string19, zBooleanValue3, zBooleanValue4, num2 == null ? 0 : num2.intValue(), 2112, null));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            return obj;
        }

        @NotNull
        /* JADX INFO: renamed from: fromJson-IoAF18A, reason: not valid java name */
        public final Object m162fromJsonIoAF18A(@NotNull String json) {
            Intrinsics.checkNotNullParameter(json, "json");
            DocumentContext documentContext = JsonExtensionsKt.getJsonPath().parse(json);
            Intrinsics.checkNotNullExpressionValue(documentContext, "jsonPath.parse(json)");
            return m161fromJsonDocIoAF18A(documentContext);
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonArray-IoAF18A, reason: not valid java name */
        public final Object m163fromJsonArrayIoAF18A(@NotNull String jsonArray) {
            Object obj;
            Intrinsics.checkNotNullParameter(jsonArray, "jsonArray");
            try {
                Result.Companion companion = Result.Companion;
                ArrayList sources = new ArrayList();
                Iterable doc = (List) JsonExtensionsKt.getJsonPath().parse(jsonArray).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(doc, "doc");
                Iterable $this$forEach$iv = doc;
                for (Object element$iv : $this$forEach$iv) {
                    DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse(element$iv);
                    Companion companion2 = RssSource.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue(jsonItem, "jsonItem");
                    Object objM161fromJsonDocIoAF18A = companion2.m161fromJsonDocIoAF18A(jsonItem);
                    ResultKt.throwOnFailure(objM161fromJsonDocIoAF18A);
                    RssSource source = (RssSource) objM161fromJsonDocIoAF18A;
                    sources.add(source);
                }
                obj = Result.constructor-impl(sources);
            } catch (Throwable th) {
                Result.Companion companion3 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            return obj;
        }
    }
}
