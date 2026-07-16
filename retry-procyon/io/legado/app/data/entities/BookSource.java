// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import java.lang.reflect.Type;
import kotlin.Result$Companion;
import com.google.gson.Gson;
import kotlin.ResultKt;
import com.google.gson.reflect.TypeToken;
import kotlin.Result;
import io.legado.app.utils.GsonExtensionsKt;
import java.io.InputStream;
import io.legado.app.help.SourceAnalyzer;
import io.legado.app.model.analyzeRule.QueryTTF;
import java.util.HashMap;
import java.io.File;
import org.jsoup.Connection$Response;
import java.util.Map;
import io.legado.app.help.http.StrResponse;
import kotlin.Unit;
import com.script.SimpleBindings;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.model.DebugLog;
import io.legado.app.data.entities.rule.ContentRule;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.data.entities.rule.SearchRule;
import io.legado.app.data.entities.rule.ExploreRule;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "headerMap", "source", "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap" })
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\bZ\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\b\b\u0087\b\u0018\u0000 ?\u00012\u00020\u0001:\u0006?\u0001?\u0001?\u0001B?\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0016\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0016\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u001b\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e\u0012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010 \u0012\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$?\u0006\u0002\u0010%J\t\u0010w\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010x\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010y\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010z\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010{\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010|\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010}\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010~\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u007f\u001a\u00020\u0016H\u00c6\u0003J\n\u0010\u0080\u0001\u001a\u00020\u0016H\u00c6\u0003J\n\u0010\u0081\u0001\u001a\u00020\u0007H\u00c6\u0003J\n\u0010\u0082\u0001\u001a\u00020\u0003H\u00c6\u0003J\f\u0010\u0083\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u001bH\u00c6\u0003J\f\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u001eH\u00c6\u0003J\f\u0010\u0087\u0001\u001a\u0004\u0018\u00010 H\u00c6\u0003J\f\u0010\u0088\u0001\u001a\u0004\u0018\u00010\"H\u00c6\u0003J\f\u0010\u0089\u0001\u001a\u0004\u0018\u00010$H\u00c6\u0003J\f\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u008b\u0001\u001a\u00020\u0007H\u00c6\u0003J\f\u0010\u008c\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u008d\u0001\u001a\u00020\u0007H\u00c6\u0003J\n\u0010\u008e\u0001\u001a\u00020\u000bH\u00c6\u0003J\n\u0010\u008f\u0001\u001a\u00020\u000bH\u00c6\u0003J\u0011\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003?\u0006\u0002\u0010DJ?\u0002\u0010\u0091\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00162\b\b\u0002\u0010\u0018\u001a\u00020\u00072\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010 2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$H\u00c6\u0001?\u0006\u0003\u0010\u0092\u0001J\u0010\u0010\u0093\u0001\u001a\u00020\u000b2\u0007\u0010\u0094\u0001\u001a\u00020\u0000J\u001f\u0010\u0093\u0001\u001a\u00020\u000b2\t\u0010\u0095\u0001\u001a\u0004\u0018\u00010\u00032\t\u0010\u0096\u0001\u001a\u0004\u0018\u00010\u0003H\u0002J\u0016\u0010\u0097\u0001\u001a\u00020\u000b2\n\u0010\u0098\u0001\u001a\u0005\u0018\u00010\u0099\u0001H\u0096\u0002J\u0007\u0010\u009a\u0001\u001a\u00020 J\u0007\u0010\u009b\u0001\u001a\u00020$J\u0007\u0010\u009c\u0001\u001a\u00020\u001bJ\t\u0010\u009d\u0001\u001a\u00020\u0003H\u0016J\u000b\u0010\u009e\u0001\u001a\u0004\u0018\u00010>H\u0016J\u0007\u0010\u009f\u0001\u001a\u00020\u001eJ\t\u0010?\u0001\u001a\u00020\u0003H\u0016J\u0007\u0010?\u0001\u001a\u00020\"J\t\u0010?\u0001\u001a\u00020\u0003H\u0016J\t\u0010?\u0001\u001a\u00020\u0007H\u0016J\u0013\u0010∴\u0001\u001a\u00030?\u00012\t\u0010?\u0001\u001a\u0004\u0018\u00010>J\u0011\u0010′\u0001\u001a\u00030?\u00012\u0007\u0010：\u0001\u001a\u00020\u0003J\n\u0010?\u0001\u001a\u00020\u0003H\u00d6\u0001R\u000e\u0010&\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u0004\u0018\u00010 X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b,\u0010)\"\u0004\b-\u0010+R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b.\u0010)\"\u0004\b/\u0010+R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b4\u0010)\"\u0004\b5\u0010+R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b6\u0010)\"\u0004\b7\u0010+R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b8\u0010)\"\u0004\b9\u0010+R\u0010\u0010:\u001a\u0004\u0018\u00010$X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b;\u00101\"\u0004\b<\u00103R\u0010\u0010=\u001a\u0004\u0018\u00010>X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u001e\u0010\r\u001a\u0004\u0018\u00010\u000bX\u0096\u000e?\u0006\u0010\n\u0002\u0010G\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bH\u0010@\"\u0004\bI\u0010BR\u0010\u0010J\u001a\u0004\u0018\u00010\u001bX\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bK\u0010)\"\u0004\bL\u0010+R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bM\u0010)\"\u0004\bN\u0010+R\u001a\u0010\u0015\u001a\u00020\u0016X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bO\u0010P\"\u0004\bQ\u0010RR\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bS\u0010)\"\u0004\bT\u0010+R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bU\u0010)\"\u0004\bV\u0010+R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bW\u0010)\"\u0004\bX\u0010+R\u001a\u0010\u0017\u001a\u00020\u0016X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bY\u0010P\"\u0004\bZ\u0010RR\u001c\u0010\u001f\u001a\u0004\u0018\u00010 X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b[\u0010\\\"\u0004\b]\u0010^R\u001c\u0010#\u001a\u0004\u0018\u00010$X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b_\u0010`\"\u0004\ba\u0010bR\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bc\u0010d\"\u0004\be\u0010fR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bg\u0010h\"\u0004\bi\u0010jR\u001c\u0010!\u001a\u0004\u0018\u00010\"X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bk\u0010l\"\u0004\bm\u0010nR\u0010\u0010o\u001a\u0004\u0018\u00010\u001eX\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bp\u0010)\"\u0004\bq\u0010+R\u0010\u0010r\u001a\u0004\u0018\u00010\"X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bs\u0010)\"\u0004\bt\u0010+R\u001a\u0010\u0018\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bu\u00101\"\u0004\bv\u00103：\u0006\u00ad\u0001" }, d2 = { "Lio/legado/app/data/entities/BookSource;", "Lio/legado/app/data/entities/BaseSource;", "bookSourceUrl", "", "bookSourceName", "bookSourceGroup", "bookSourceType", "", "bookUrlPattern", "customOrder", "enabled", "", "enabledExplore", "enabledCookieJar", "concurrentRate", "header", "loginUrl", "loginUi", "loginCheckJs", "bookSourceComment", "variableComment", "lastUpdateTime", "", "respondTime", "weight", "exploreUrl", "ruleExplore", "Lio/legado/app/data/entities/rule/ExploreRule;", "searchUrl", "ruleSearch", "Lio/legado/app/data/entities/rule/SearchRule;", "ruleBookInfo", "Lio/legado/app/data/entities/rule/BookInfoRule;", "ruleToc", "Lio/legado/app/data/entities/rule/TocRule;", "ruleContent", "Lio/legado/app/data/entities/rule/ContentRule;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZLjava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Lio/legado/app/data/entities/rule/ExploreRule;Ljava/lang/String;Lio/legado/app/data/entities/rule/SearchRule;Lio/legado/app/data/entities/rule/BookInfoRule;Lio/legado/app/data/entities/rule/TocRule;Lio/legado/app/data/entities/rule/ContentRule;)V", "_userNameSpace", "bookInfoRuleV", "getBookSourceComment", "()Ljava/lang/String;", "setBookSourceComment", "(Ljava/lang/String;)V", "getBookSourceGroup", "setBookSourceGroup", "getBookSourceName", "setBookSourceName", "getBookSourceType", "()I", "setBookSourceType", "(I)V", "getBookSourceUrl", "setBookSourceUrl", "getBookUrlPattern", "setBookUrlPattern", "getConcurrentRate", "setConcurrentRate", "contentRuleV", "getCustomOrder", "setCustomOrder", "debugLog", "Lio/legado/app/model/DebugLog;", "getEnabled", "()Z", "setEnabled", "(Z)V", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getEnabledExplore", "setEnabledExplore", "exploreRuleV", "getExploreUrl", "setExploreUrl", "getHeader", "setHeader", "getLastUpdateTime", "()J", "setLastUpdateTime", "(J)V", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "setLoginUi", "getLoginUrl", "setLoginUrl", "getRespondTime", "setRespondTime", "getRuleBookInfo", "()Lio/legado/app/data/entities/rule/BookInfoRule;", "setRuleBookInfo", "(Lio/legado/app/data/entities/rule/BookInfoRule;)V", "getRuleContent", "()Lio/legado/app/data/entities/rule/ContentRule;", "setRuleContent", "(Lio/legado/app/data/entities/rule/ContentRule;)V", "getRuleExplore", "()Lio/legado/app/data/entities/rule/ExploreRule;", "setRuleExplore", "(Lio/legado/app/data/entities/rule/ExploreRule;)V", "getRuleSearch", "()Lio/legado/app/data/entities/rule/SearchRule;", "setRuleSearch", "(Lio/legado/app/data/entities/rule/SearchRule;)V", "getRuleToc", "()Lio/legado/app/data/entities/rule/TocRule;", "setRuleToc", "(Lio/legado/app/data/entities/rule/TocRule;)V", "searchRuleV", "getSearchUrl", "setSearchUrl", "tocRuleV", "getVariableComment", "setVariableComment", "getWeight", "setWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZLjava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Lio/legado/app/data/entities/rule/ExploreRule;Ljava/lang/String;Lio/legado/app/data/entities/rule/SearchRule;Lio/legado/app/data/entities/rule/BookInfoRule;Lio/legado/app/data/entities/rule/TocRule;Lio/legado/app/data/entities/rule/ContentRule;)Lio/legado/app/data/entities/BookSource;", "equal", "source", "a", "b", "equals", "other", "", "getBookInfoRule", "getContentRule", "getExploreRule", "getKey", "getLogger", "getSearchRule", "getTag", "getTocRule", "getUserNameSpace", "hashCode", "setLogger", "", "logger", "setUserNameSpace", "nameSpace", "toString", "Companion", "Converters", "ExploreKind", "reader-pro" })
public final class BookSource implements BaseSource
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private String bookSourceUrl;
    @NotNull
    private String bookSourceName;
    @Nullable
    private String bookSourceGroup;
    private int bookSourceType;
    @Nullable
    private String bookUrlPattern;
    private int customOrder;
    private boolean enabled;
    private boolean enabledExplore;
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
    private String bookSourceComment;
    @Nullable
    private String variableComment;
    private long lastUpdateTime;
    private long respondTime;
    private int weight;
    @Nullable
    private String exploreUrl;
    @Nullable
    private ExploreRule ruleExplore;
    @Nullable
    private String searchUrl;
    @Nullable
    private SearchRule ruleSearch;
    @Nullable
    private BookInfoRule ruleBookInfo;
    @Nullable
    private TocRule ruleToc;
    @Nullable
    private ContentRule ruleContent;
    @Nullable
    private SearchRule searchRuleV;
    @Nullable
    private ExploreRule exploreRuleV;
    @Nullable
    private BookInfoRule bookInfoRuleV;
    @Nullable
    private TocRule tocRuleV;
    @Nullable
    private ContentRule contentRuleV;
    @NotNull
    private transient String _userNameSpace;
    @Nullable
    private transient DebugLog debugLog;
    
    public BookSource(@NotNull final String bookSourceUrl, @NotNull final String bookSourceName, @Nullable final String bookSourceGroup, final int bookSourceType, @Nullable final String bookUrlPattern, final int customOrder, final boolean enabled, final boolean enabledExplore, @Nullable final Boolean enabledCookieJar, @Nullable final String concurrentRate, @Nullable final String header, @Nullable final String loginUrl, @Nullable final String loginUi, @Nullable final String loginCheckJs, @Nullable final String bookSourceComment, @Nullable final String variableComment, final long lastUpdateTime, final long respondTime, final int weight, @Nullable final String exploreUrl, @Nullable final ExploreRule ruleExplore, @Nullable final String searchUrl, @Nullable final SearchRule ruleSearch, @Nullable final BookInfoRule ruleBookInfo, @Nullable final TocRule ruleToc, @Nullable final ContentRule ruleContent) {
        Intrinsics.checkNotNullParameter((Object)bookSourceUrl, "bookSourceUrl");
        Intrinsics.checkNotNullParameter((Object)bookSourceName, "bookSourceName");
        this.bookSourceUrl = bookSourceUrl;
        this.bookSourceName = bookSourceName;
        this.bookSourceGroup = bookSourceGroup;
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
        this.variableComment = variableComment;
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
        this._userNameSpace = "";
    }
    
    @NotNull
    public final String getBookSourceUrl() {
        return this.bookSourceUrl;
    }
    
    public final void setBookSourceUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.bookSourceUrl = <set-?>;
    }
    
    @NotNull
    public final String getBookSourceName() {
        return this.bookSourceName;
    }
    
    public final void setBookSourceName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.bookSourceName = <set-?>;
    }
    
    @Nullable
    public final String getBookSourceGroup() {
        return this.bookSourceGroup;
    }
    
    public final void setBookSourceGroup(@Nullable final String <set-?>) {
        this.bookSourceGroup = <set-?>;
    }
    
    public final int getBookSourceType() {
        return this.bookSourceType;
    }
    
    public final void setBookSourceType(final int <set-?>) {
        this.bookSourceType = <set-?>;
    }
    
    @Nullable
    public final String getBookUrlPattern() {
        return this.bookUrlPattern;
    }
    
    public final void setBookUrlPattern(@Nullable final String <set-?>) {
        this.bookUrlPattern = <set-?>;
    }
    
    public final int getCustomOrder() {
        return this.customOrder;
    }
    
    public final void setCustomOrder(final int <set-?>) {
        this.customOrder = <set-?>;
    }
    
    public final boolean getEnabled() {
        return this.enabled;
    }
    
    public final void setEnabled(final boolean <set-?>) {
        this.enabled = <set-?>;
    }
    
    public final boolean getEnabledExplore() {
        return this.enabledExplore;
    }
    
    public final void setEnabledExplore(final boolean <set-?>) {
        this.enabledExplore = <set-?>;
    }
    
    @Nullable
    public Boolean getEnabledCookieJar() {
        return this.enabledCookieJar;
    }
    
    public void setEnabledCookieJar(@Nullable final Boolean <set-?>) {
        this.enabledCookieJar = <set-?>;
    }
    
    @Nullable
    public String getConcurrentRate() {
        return this.concurrentRate;
    }
    
    public void setConcurrentRate(@Nullable final String <set-?>) {
        this.concurrentRate = <set-?>;
    }
    
    @Nullable
    public String getHeader() {
        return this.header;
    }
    
    public void setHeader(@Nullable final String <set-?>) {
        this.header = <set-?>;
    }
    
    @Nullable
    public String getLoginUrl() {
        return this.loginUrl;
    }
    
    public void setLoginUrl(@Nullable final String <set-?>) {
        this.loginUrl = <set-?>;
    }
    
    @Nullable
    public String getLoginUi() {
        return this.loginUi;
    }
    
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
    public final String getBookSourceComment() {
        return this.bookSourceComment;
    }
    
    public final void setBookSourceComment(@Nullable final String <set-?>) {
        this.bookSourceComment = <set-?>;
    }
    
    @Nullable
    public final String getVariableComment() {
        return this.variableComment;
    }
    
    public final void setVariableComment(@Nullable final String <set-?>) {
        this.variableComment = <set-?>;
    }
    
    public final long getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    
    public final void setLastUpdateTime(final long <set-?>) {
        this.lastUpdateTime = <set-?>;
    }
    
    public final long getRespondTime() {
        return this.respondTime;
    }
    
    public final void setRespondTime(final long <set-?>) {
        this.respondTime = <set-?>;
    }
    
    public final int getWeight() {
        return this.weight;
    }
    
    public final void setWeight(final int <set-?>) {
        this.weight = <set-?>;
    }
    
    @Nullable
    public final String getExploreUrl() {
        return this.exploreUrl;
    }
    
    public final void setExploreUrl(@Nullable final String <set-?>) {
        this.exploreUrl = <set-?>;
    }
    
    @Nullable
    public final ExploreRule getRuleExplore() {
        return this.ruleExplore;
    }
    
    public final void setRuleExplore(@Nullable final ExploreRule <set-?>) {
        this.ruleExplore = <set-?>;
    }
    
    @Nullable
    public final String getSearchUrl() {
        return this.searchUrl;
    }
    
    public final void setSearchUrl(@Nullable final String <set-?>) {
        this.searchUrl = <set-?>;
    }
    
    @Nullable
    public final SearchRule getRuleSearch() {
        return this.ruleSearch;
    }
    
    public final void setRuleSearch(@Nullable final SearchRule <set-?>) {
        this.ruleSearch = <set-?>;
    }
    
    @Nullable
    public final BookInfoRule getRuleBookInfo() {
        return this.ruleBookInfo;
    }
    
    public final void setRuleBookInfo(@Nullable final BookInfoRule <set-?>) {
        this.ruleBookInfo = <set-?>;
    }
    
    @Nullable
    public final TocRule getRuleToc() {
        return this.ruleToc;
    }
    
    public final void setRuleToc(@Nullable final TocRule <set-?>) {
        this.ruleToc = <set-?>;
    }
    
    @Nullable
    public final ContentRule getRuleContent() {
        return this.ruleContent;
    }
    
    public final void setRuleContent(@Nullable final ContentRule <set-?>) {
        this.ruleContent = <set-?>;
    }
    
    @NotNull
    public String getTag() {
        return this.bookSourceName;
    }
    
    @NotNull
    public String getKey() {
        return this.bookSourceUrl;
    }
    
    @Override
    public int hashCode() {
        return this.bookSourceUrl.hashCode();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof BookSource && Intrinsics.areEqual((Object)((BookSource)other).bookSourceUrl, (Object)this.bookSourceUrl);
    }
    
    @NotNull
    public final SearchRule getSearchRule() {
        final SearchRule ruleSearch = this.ruleSearch;
        return (ruleSearch == null) ? new SearchRule((String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 1023, (DefaultConstructorMarker)null) : ruleSearch;
    }
    
    @NotNull
    public final ExploreRule getExploreRule() {
        final ExploreRule ruleExplore = this.ruleExplore;
        return (ruleExplore == null) ? new ExploreRule((String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 1023, (DefaultConstructorMarker)null) : ruleExplore;
    }
    
    @NotNull
    public final BookInfoRule getBookInfoRule() {
        final BookInfoRule ruleBookInfo = this.ruleBookInfo;
        return (ruleBookInfo == null) ? new BookInfoRule((String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 2047, (DefaultConstructorMarker)null) : ruleBookInfo;
    }
    
    @NotNull
    public final TocRule getTocRule() {
        final TocRule ruleToc = this.ruleToc;
        return (ruleToc == null) ? new TocRule((String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 255, (DefaultConstructorMarker)null) : ruleToc;
    }
    
    @NotNull
    public final ContentRule getContentRule() {
        final ContentRule ruleContent = this.ruleContent;
        return (ruleContent == null) ? new ContentRule((String)null, (String)null, (String)null, (String)null, (String)null, (String)null, 63, (DefaultConstructorMarker)null) : ruleContent;
    }
    
    public final void setUserNameSpace(@NotNull final String nameSpace) {
        Intrinsics.checkNotNullParameter((Object)nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }
    
    @NotNull
    public String getUserNameSpace() {
        return this._userNameSpace;
    }
    
    public final void setLogger(@Nullable final DebugLog logger) {
        this.debugLog = logger;
    }
    
    @Nullable
    public DebugLog getLogger() {
        return this.debugLog;
    }
    
    public final boolean equal(@NotNull final BookSource source) {
        Intrinsics.checkNotNullParameter((Object)source, "source");
        return this.equal(this.bookSourceName, source.bookSourceName) && this.equal(this.bookSourceUrl, source.bookSourceUrl) && this.equal(this.bookSourceGroup, source.bookSourceGroup) && this.bookSourceType == source.bookSourceType && this.equal(this.bookUrlPattern, source.bookUrlPattern) && this.enabled == source.enabled && this.enabledExplore == source.enabledExplore && Intrinsics.areEqual((Object)this.getEnabledCookieJar(), (Object)source.getEnabledCookieJar()) && this.equal(this.getHeader(), source.getHeader()) && this.equal(this.getLoginUrl(), source.getLoginUrl()) && this.equal(this.exploreUrl, source.exploreUrl) && this.equal(this.searchUrl, source.searchUrl) && Intrinsics.areEqual((Object)this.getSearchRule(), (Object)source.getSearchRule()) && Intrinsics.areEqual((Object)this.getExploreRule(), (Object)source.getExploreRule()) && Intrinsics.areEqual((Object)this.getBookInfoRule(), (Object)source.getBookInfoRule()) && Intrinsics.areEqual((Object)this.getTocRule(), (Object)source.getTocRule()) && Intrinsics.areEqual((Object)this.getContentRule(), (Object)source.getContentRule());
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
    
    @Nullable
    public Object evalJS(@NotNull final String jsStr, @NotNull final Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception {
        return BaseSource$DefaultImpls.evalJS((BaseSource)this, jsStr, (Function1)bindingsConfig);
    }
    
    @Nullable
    public byte[] aesBase64DecodeToByteArray(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesBase64DecodeToByteArray((BaseSource)this, str, key, transformation, iv);
    }
    
    @Nullable
    public String aesBase64DecodeToString(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesBase64DecodeToString((BaseSource)this, str, key, transformation, iv);
    }
    
    @Nullable
    public String aesDecodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesDecodeArgsBase64Str((BaseSource)this, data, key, mode, padding, iv);
    }
    
    @Nullable
    public byte[] aesDecodeToByteArray(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesDecodeToByteArray((BaseSource)this, str, key, transformation, iv);
    }
    
    @Nullable
    public String aesDecodeToString(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesDecodeToString((BaseSource)this, str, key, transformation, iv);
    }
    
    @Nullable
    public String aesEncodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesEncodeArgsBase64Str((BaseSource)this, data, key, mode, padding, iv);
    }
    
    @Nullable
    public byte[] aesEncodeToBase64ByteArray(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesEncodeToBase64ByteArray((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String aesEncodeToBase64String(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesEncodeToBase64String((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public byte[] aesEncodeToByteArray(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesEncodeToByteArray((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String aesEncodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.aesEncodeToString((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String ajax(@NotNull final String urlStr) {
        return BaseSource$DefaultImpls.ajax((BaseSource)this, urlStr);
    }
    
    @NotNull
    public StrResponse[] ajaxAll(@NotNull final String[] urlList) {
        return BaseSource$DefaultImpls.ajaxAll((BaseSource)this, urlList);
    }
    
    @NotNull
    public String androidId() {
        return BaseSource$DefaultImpls.androidId((BaseSource)this);
    }
    
    @NotNull
    public String base64Decode(@NotNull final String str) {
        return BaseSource$DefaultImpls.base64Decode((BaseSource)this, str);
    }
    
    @NotNull
    public String base64Decode(@NotNull final String str, final int flags) {
        return BaseSource$DefaultImpls.base64Decode((BaseSource)this, str, flags);
    }
    
    @Nullable
    public byte[] base64DecodeToByteArray(@Nullable final String str) {
        return BaseSource$DefaultImpls.base64DecodeToByteArray((BaseSource)this, str);
    }
    
    @Nullable
    public byte[] base64DecodeToByteArray(@Nullable final String str, final int flags) {
        return BaseSource$DefaultImpls.base64DecodeToByteArray((BaseSource)this, str, flags);
    }
    
    @Nullable
    public String base64Encode(@NotNull final String str) {
        return BaseSource$DefaultImpls.base64Encode((BaseSource)this, str);
    }
    
    @Nullable
    public String base64Encode(@NotNull final String str, final int flags) {
        return BaseSource$DefaultImpls.base64Encode((BaseSource)this, str, flags);
    }
    
    @Nullable
    public String cacheFile(@NotNull final String urlStr) {
        return BaseSource$DefaultImpls.cacheFile((BaseSource)this, urlStr);
    }
    
    @Nullable
    public String cacheFile(@NotNull final String urlStr, final int saveTime) {
        return BaseSource$DefaultImpls.cacheFile((BaseSource)this, urlStr, saveTime);
    }
    
    @NotNull
    public StrResponse connect(@NotNull final String urlStr) {
        return BaseSource$DefaultImpls.connect((BaseSource)this, urlStr);
    }
    
    @NotNull
    public StrResponse connect(@NotNull final String urlStr, @Nullable final String header) {
        return BaseSource$DefaultImpls.connect((BaseSource)this, urlStr, header);
    }
    
    public void deleteFile(@NotNull final String path) {
        BaseSource$DefaultImpls.deleteFile((BaseSource)this, path);
    }
    
    @Nullable
    public String desBase64DecodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.desBase64DecodeToString((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String desDecodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.desDecodeToString((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String desEncodeToBase64String(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.desEncodeToBase64String((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String desEncodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
        return BaseSource$DefaultImpls.desEncodeToString((BaseSource)this, data, key, transformation, iv);
    }
    
    @Nullable
    public String digestBase64Str(@NotNull final String data, @NotNull final String algorithm) {
        return BaseSource$DefaultImpls.digestBase64Str((BaseSource)this, data, algorithm);
    }
    
    @Nullable
    public String digestHex(@NotNull final String data, @NotNull final String algorithm) {
        return BaseSource$DefaultImpls.digestHex((BaseSource)this, data, algorithm);
    }
    
    @NotNull
    public String downloadFile(@NotNull final String content, @NotNull final String url) {
        return BaseSource$DefaultImpls.downloadFile((BaseSource)this, content, url);
    }
    
    @NotNull
    public String encodeURI(@NotNull final String str) {
        return BaseSource$DefaultImpls.encodeURI((BaseSource)this, str);
    }
    
    @NotNull
    public String encodeURI(@NotNull final String str, @NotNull final String enc) {
        return BaseSource$DefaultImpls.encodeURI((BaseSource)this, str, enc);
    }
    
    @NotNull
    public Connection$Response get(@NotNull final String urlStr, @NotNull final Map<String, String> headers) {
        return BaseSource$DefaultImpls.get((BaseSource)this, urlStr, (Map)headers);
    }
    
    @NotNull
    public String getCookie(@NotNull final String tag, @Nullable final String key) {
        return BaseSource$DefaultImpls.getCookie((BaseSource)this, tag, key);
    }
    
    @NotNull
    public File getFile(@NotNull final String path) {
        return BaseSource$DefaultImpls.getFile((BaseSource)this, path);
    }
    
    @NotNull
    public HashMap<String, String> getHeaderMap(final boolean hasLoginHeader) {
        return BaseSource$DefaultImpls.getHeaderMap((BaseSource)this, hasLoginHeader);
    }
    
    @Nullable
    public String getLoginHeader() {
        return BaseSource$DefaultImpls.getLoginHeader((BaseSource)this);
    }
    
    @Nullable
    public Map<String, String> getLoginHeaderMap() {
        return BaseSource$DefaultImpls.getLoginHeaderMap((BaseSource)this);
    }
    
    @Nullable
    public String getLoginInfo() {
        return BaseSource$DefaultImpls.getLoginInfo((BaseSource)this);
    }
    
    @Nullable
    public Map<String, String> getLoginInfoMap() {
        return BaseSource$DefaultImpls.getLoginInfoMap((BaseSource)this);
    }
    
    @Nullable
    public String getLoginJs() {
        return BaseSource$DefaultImpls.getLoginJs((BaseSource)this);
    }
    
    @Nullable
    public BaseSource getSource() {
        return BaseSource$DefaultImpls.getSource((BaseSource)this);
    }
    
    @NotNull
    public String getTxtInFolder(@NotNull final String unzipPath) {
        return BaseSource$DefaultImpls.getTxtInFolder((BaseSource)this, unzipPath);
    }
    
    @Nullable
    public String getVariable() {
        return BaseSource$DefaultImpls.getVariable((BaseSource)this);
    }
    
    @Nullable
    public byte[] getZipByteArrayContent(@NotNull final String url, @NotNull final String path) {
        return BaseSource$DefaultImpls.getZipByteArrayContent((BaseSource)this, url, path);
    }
    
    @NotNull
    public String getZipStringContent(@NotNull final String url, @NotNull final String path) {
        return BaseSource$DefaultImpls.getZipStringContent((BaseSource)this, url, path);
    }
    
    @NotNull
    public String getZipStringContent(@NotNull final String url, @NotNull final String path, @NotNull final String charsetName) {
        return BaseSource$DefaultImpls.getZipStringContent((BaseSource)this, url, path, charsetName);
    }
    
    @NotNull
    public Connection$Response head(@NotNull final String urlStr, @NotNull final Map<String, String> headers) {
        return BaseSource$DefaultImpls.head((BaseSource)this, urlStr, (Map)headers);
    }
    
    @NotNull
    public String htmlFormat(@NotNull final String str) {
        return BaseSource$DefaultImpls.htmlFormat((BaseSource)this, str);
    }
    
    @NotNull
    public String importScript(@NotNull final String path) {
        return BaseSource$DefaultImpls.importScript((BaseSource)this, path);
    }
    
    @NotNull
    public String log(@NotNull final String msg) {
        return BaseSource$DefaultImpls.log((BaseSource)this, msg);
    }
    
    public void logType(@Nullable final Object any) {
        BaseSource$DefaultImpls.logType((BaseSource)this, any);
    }
    
    public void login() {
        BaseSource$DefaultImpls.login((BaseSource)this);
    }
    
    public void longToast(@Nullable final Object msg) {
        BaseSource$DefaultImpls.longToast((BaseSource)this, msg);
    }
    
    @NotNull
    public String md5Encode(@NotNull final String str) {
        return BaseSource$DefaultImpls.md5Encode((BaseSource)this, str);
    }
    
    @NotNull
    public String md5Encode16(@NotNull final String str) {
        return BaseSource$DefaultImpls.md5Encode16((BaseSource)this, str);
    }
    
    @NotNull
    public Connection$Response post(@NotNull final String urlStr, @NotNull final String body, @NotNull final Map<String, String> headers) {
        return BaseSource$DefaultImpls.post((BaseSource)this, urlStr, body, (Map)headers);
    }
    
    public void putLoginHeader(@NotNull final String header) {
        BaseSource$DefaultImpls.putLoginHeader((BaseSource)this, header);
    }
    
    public boolean putLoginInfo(@NotNull final String info) {
        return BaseSource$DefaultImpls.putLoginInfo((BaseSource)this, info);
    }
    
    @Nullable
    public QueryTTF queryBase64TTF(@Nullable final String base64) {
        return BaseSource$DefaultImpls.queryBase64TTF((BaseSource)this, base64);
    }
    
    @Nullable
    public QueryTTF queryTTF(@Nullable final String str) {
        return BaseSource$DefaultImpls.queryTTF((BaseSource)this, str);
    }
    
    @NotNull
    public String randomUUID() {
        return BaseSource$DefaultImpls.randomUUID((BaseSource)this);
    }
    
    @Nullable
    public byte[] readFile(@NotNull final String path) {
        return BaseSource$DefaultImpls.readFile((BaseSource)this, path);
    }
    
    @NotNull
    public String readTxtFile(@NotNull final String path) {
        return BaseSource$DefaultImpls.readTxtFile((BaseSource)this, path);
    }
    
    @NotNull
    public String readTxtFile(@NotNull final String path, @NotNull final String charsetName) {
        return BaseSource$DefaultImpls.readTxtFile((BaseSource)this, path, charsetName);
    }
    
    public void removeLoginHeader() {
        BaseSource$DefaultImpls.removeLoginHeader((BaseSource)this);
    }
    
    public void removeLoginInfo() {
        BaseSource$DefaultImpls.removeLoginInfo((BaseSource)this);
    }
    
    @NotNull
    public String replaceFont(@NotNull final String text, @Nullable final QueryTTF font1, @Nullable final QueryTTF font2) {
        return BaseSource$DefaultImpls.replaceFont((BaseSource)this, text, font1, font2);
    }
    
    public void setVariable(@Nullable final String variable) {
        BaseSource$DefaultImpls.setVariable((BaseSource)this, variable);
    }
    
    @NotNull
    public String timeFormat(final long time) {
        return BaseSource$DefaultImpls.timeFormat((BaseSource)this, time);
    }
    
    @Nullable
    public String timeFormatUTC(final long time, @NotNull final String format, final int sh) {
        return BaseSource$DefaultImpls.timeFormatUTC((BaseSource)this, time, format, sh);
    }
    
    public void toast(@Nullable final Object msg) {
        BaseSource$DefaultImpls.toast((BaseSource)this, msg);
    }
    
    @Nullable
    public String tripleDESDecodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return BaseSource$DefaultImpls.tripleDESDecodeArgsBase64Str((BaseSource)this, data, key, mode, padding, iv);
    }
    
    @Nullable
    public String tripleDESDecodeStr(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return BaseSource$DefaultImpls.tripleDESDecodeStr((BaseSource)this, data, key, mode, padding, iv);
    }
    
    @Nullable
    public String tripleDESEncodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return BaseSource$DefaultImpls.tripleDESEncodeArgsBase64Str((BaseSource)this, data, key, mode, padding, iv);
    }
    
    @Nullable
    public String tripleDESEncodeBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
        return BaseSource$DefaultImpls.tripleDESEncodeBase64Str((BaseSource)this, data, key, mode, padding, iv);
    }
    
    @NotNull
    public String unzipFile(@NotNull final String zipPath) {
        return BaseSource$DefaultImpls.unzipFile((BaseSource)this, zipPath);
    }
    
    @NotNull
    public String utf8ToGbk(@NotNull final String str) {
        return BaseSource$DefaultImpls.utf8ToGbk((BaseSource)this, str);
    }
    
    @Nullable
    public String webView(@Nullable final String html, @Nullable final String url, @Nullable final String js) {
        return BaseSource$DefaultImpls.webView((BaseSource)this, html, url, js);
    }
    
    @NotNull
    public final String component1() {
        return this.bookSourceUrl;
    }
    
    @NotNull
    public final String component2() {
        return this.bookSourceName;
    }
    
    @Nullable
    public final String component3() {
        return this.bookSourceGroup;
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
    
    @Nullable
    public final Boolean component9() {
        return this.getEnabledCookieJar();
    }
    
    @Nullable
    public final String component10() {
        return this.getConcurrentRate();
    }
    
    @Nullable
    public final String component11() {
        return this.getHeader();
    }
    
    @Nullable
    public final String component12() {
        return this.getLoginUrl();
    }
    
    @Nullable
    public final String component13() {
        return this.getLoginUi();
    }
    
    @Nullable
    public final String component14() {
        return this.loginCheckJs;
    }
    
    @Nullable
    public final String component15() {
        return this.bookSourceComment;
    }
    
    @Nullable
    public final String component16() {
        return this.variableComment;
    }
    
    public final long component17() {
        return this.lastUpdateTime;
    }
    
    public final long component18() {
        return this.respondTime;
    }
    
    public final int component19() {
        return this.weight;
    }
    
    @Nullable
    public final String component20() {
        return this.exploreUrl;
    }
    
    @Nullable
    public final ExploreRule component21() {
        return this.ruleExplore;
    }
    
    @Nullable
    public final String component22() {
        return this.searchUrl;
    }
    
    @Nullable
    public final SearchRule component23() {
        return this.ruleSearch;
    }
    
    @Nullable
    public final BookInfoRule component24() {
        return this.ruleBookInfo;
    }
    
    @Nullable
    public final TocRule component25() {
        return this.ruleToc;
    }
    
    @Nullable
    public final ContentRule component26() {
        return this.ruleContent;
    }
    
    @NotNull
    public final BookSource copy(@NotNull final String bookSourceUrl, @NotNull final String bookSourceName, @Nullable final String bookSourceGroup, final int bookSourceType, @Nullable final String bookUrlPattern, final int customOrder, final boolean enabled, final boolean enabledExplore, @Nullable final Boolean enabledCookieJar, @Nullable final String concurrentRate, @Nullable final String header, @Nullable final String loginUrl, @Nullable final String loginUi, @Nullable final String loginCheckJs, @Nullable final String bookSourceComment, @Nullable final String variableComment, final long lastUpdateTime, final long respondTime, final int weight, @Nullable final String exploreUrl, @Nullable final ExploreRule ruleExplore, @Nullable final String searchUrl, @Nullable final SearchRule ruleSearch, @Nullable final BookInfoRule ruleBookInfo, @Nullable final TocRule ruleToc, @Nullable final ContentRule ruleContent) {
        Intrinsics.checkNotNullParameter((Object)bookSourceUrl, "bookSourceUrl");
        Intrinsics.checkNotNullParameter((Object)bookSourceName, "bookSourceName");
        return new BookSource(bookSourceUrl, bookSourceName, bookSourceGroup, bookSourceType, bookUrlPattern, customOrder, enabled, enabledExplore, enabledCookieJar, concurrentRate, header, loginUrl, loginUi, loginCheckJs, bookSourceComment, variableComment, lastUpdateTime, respondTime, weight, exploreUrl, ruleExplore, searchUrl, ruleSearch, ruleBookInfo, ruleToc, ruleContent);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BookSource(bookSourceUrl=").append(this.bookSourceUrl).append(", bookSourceName=").append(this.bookSourceName).append(", bookSourceGroup=").append((Object)this.bookSourceGroup).append(", bookSourceType=").append(this.bookSourceType).append(", bookUrlPattern=").append((Object)this.bookUrlPattern).append(", customOrder=").append(this.customOrder).append(", enabled=").append(this.enabled).append(", enabledExplore=").append(this.enabledExplore).append(", enabledCookieJar=").append(this.getEnabledCookieJar()).append(", concurrentRate=").append((Object)this.getConcurrentRate()).append(", header=").append((Object)this.getHeader()).append(", loginUrl=");
        sb.append((Object)this.getLoginUrl()).append(", loginUi=").append((Object)this.getLoginUi()).append(", loginCheckJs=").append((Object)this.loginCheckJs).append(", bookSourceComment=").append((Object)this.bookSourceComment).append(", variableComment=").append((Object)this.variableComment).append(", lastUpdateTime=").append(this.lastUpdateTime).append(", respondTime=").append(this.respondTime).append(", weight=").append(this.weight).append(", exploreUrl=").append((Object)this.exploreUrl).append(", ruleExplore=").append(this.ruleExplore).append(", searchUrl=").append((Object)this.searchUrl).append(", ruleSearch=").append(this.ruleSearch);
        sb.append(", ruleBookInfo=").append(this.ruleBookInfo).append(", ruleToc=").append(this.ruleToc).append(", ruleContent=").append(this.ruleContent).append(')');
        return sb.toString();
    }
    
    public BookSource() {
        this(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null);
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u001f\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t：\u0006\u0015" }, d2 = { "Lio/legado/app/data/entities/BookSource$ExploreKind;", "", "title", "", "url", "(Ljava/lang/String;Ljava/lang/String;)V", "getTitle", "()Ljava/lang/String;", "setTitle", "(Ljava/lang/String;)V", "getUrl", "setUrl", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
    public static final class ExploreKind
    {
        @NotNull
        private String title;
        @Nullable
        private String url;
        
        public ExploreKind(@NotNull final String title, @Nullable final String url) {
            Intrinsics.checkNotNullParameter((Object)title, "title");
            this.title = title;
            this.url = url;
        }
        
        @NotNull
        public final String getTitle() {
            return this.title;
        }
        
        public final void setTitle(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.title = <set-?>;
        }
        
        @Nullable
        public final String getUrl() {
            return this.url;
        }
        
        public final void setUrl(@Nullable final String <set-?>) {
            this.url = <set-?>;
        }
        
        @NotNull
        public final String component1() {
            return this.title;
        }
        
        @Nullable
        public final String component2() {
            return this.url;
        }
        
        @NotNull
        public final ExploreKind copy(@NotNull final String title, @Nullable final String url) {
            Intrinsics.checkNotNullParameter((Object)title, "title");
            return new ExploreKind(title, url);
        }
        
        @NotNull
        @Override
        public String toString() {
            return "ExploreKind(title=" + this.title + ", url=" + (Object)this.url + ')';
        }
        
        @Override
        public int hashCode() {
            int result = this.title.hashCode();
            result = result * 31 + ((this.url == null) ? 0 : this.url.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ExploreKind)) {
                return false;
            }
            final ExploreKind exploreKind = (ExploreKind)other;
            return Intrinsics.areEqual((Object)this.title, (Object)exploreKind.title) && Intrinsics.areEqual((Object)this.url, (Object)exploreKind.url);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\b\u0010\tJ*\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0006\u0010\f\u001a\u00020\r\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u000e\u0010\u000fJ*\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u000e\u0010\t\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001：\u0006\u0010" }, d2 = { "Lio/legado/app/data/entities/BookSource$Companion;", "", "()V", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/BookSource;", "json", "", "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "", "inputStream", "Ljava/io/InputStream;", "fromJsonArray-IoAF18A", "(Ljava/io/InputStream;)Ljava/lang/Object;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final Object fromJson-IoAF18A(@NotNull final String json) {
            Intrinsics.checkNotNullParameter((Object)json, "json");
            return SourceAnalyzer.INSTANCE.jsonToBookSource-IoAF18A(json);
        }
        
        @NotNull
        public final Object fromJsonArray-IoAF18A(@NotNull final String json) {
            Intrinsics.checkNotNullParameter((Object)json, "json");
            return SourceAnalyzer.INSTANCE.jsonToBookSources-IoAF18A(json);
        }
        
        @NotNull
        public final Object fromJsonArray-IoAF18A(@NotNull final InputStream inputStream) {
            Intrinsics.checkNotNullParameter((Object)inputStream, "inputStream");
            return SourceAnalyzer.INSTANCE.jsonToBookSources-IoAF18A(inputStream);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u0010\u0010\u0007\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\tJ\u0010\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\fJ\u0010\u0010\r\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0012\u0010\u0010\u001a\u0004\u0018\u00010\u00062\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\t2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0010\u0010\u0017\u001a\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u0016：\u0006\u0019" }, d2 = { "Lio/legado/app/data/entities/BookSource$Converters;", "", "()V", "bookInfoRuleToString", "", "bookInfoRule", "Lio/legado/app/data/entities/rule/BookInfoRule;", "contentRuleToString", "contentRule", "Lio/legado/app/data/entities/rule/ContentRule;", "exploreRuleToString", "exploreRule", "Lio/legado/app/data/entities/rule/ExploreRule;", "searchRuleToString", "searchRule", "Lio/legado/app/data/entities/rule/SearchRule;", "stringToBookInfoRule", "json", "stringToContentRule", "stringToExploreRule", "stringToSearchRule", "stringToTocRule", "Lio/legado/app/data/entities/rule/TocRule;", "tocRuleToString", "tocRule", "reader-pro" })
    public static final class Converters
    {
        @NotNull
        public final String exploreRuleToString(@Nullable final ExploreRule exploreRule) {
            final String json = GsonExtensionsKt.getGSON().toJson((Object)exploreRule);
            Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(exploreRule)");
            return json;
        }
        
        @Nullable
        public final ExploreRule stringToExploreRule(@Nullable final String json) {
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<ExploreRule>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(json, type)) instanceof ExploreRule)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            return (ExploreRule)(Result.isFailure-impl(o2) ? null : o2);
        }
        
        @NotNull
        public final String searchRuleToString(@Nullable final SearchRule searchRule) {
            final String json = GsonExtensionsKt.getGSON().toJson((Object)searchRule);
            Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(searchRule)");
            return json;
        }
        
        @Nullable
        public final SearchRule stringToSearchRule(@Nullable final String json) {
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<SearchRule>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(json, type)) instanceof SearchRule)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            return (SearchRule)(Result.isFailure-impl(o2) ? null : o2);
        }
        
        @NotNull
        public final String bookInfoRuleToString(@Nullable final BookInfoRule bookInfoRule) {
            final String json = GsonExtensionsKt.getGSON().toJson((Object)bookInfoRule);
            Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(bookInfoRule)");
            return json;
        }
        
        @Nullable
        public final BookInfoRule stringToBookInfoRule(@Nullable final String json) {
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<BookInfoRule>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(json, type)) instanceof BookInfoRule)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            return (BookInfoRule)(Result.isFailure-impl(o2) ? null : o2);
        }
        
        @NotNull
        public final String tocRuleToString(@Nullable final TocRule tocRule) {
            final String json = GsonExtensionsKt.getGSON().toJson((Object)tocRule);
            Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(tocRule)");
            return json;
        }
        
        @Nullable
        public final TocRule stringToTocRule(@Nullable final String json) {
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<TocRule>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(json, type)) instanceof TocRule)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            return (TocRule)(Result.isFailure-impl(o2) ? null : o2);
        }
        
        @NotNull
        public final String contentRuleToString(@Nullable final ContentRule contentRule) {
            final String json = GsonExtensionsKt.getGSON().toJson((Object)contentRule);
            Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(contentRule)");
            return json;
        }
        
        @Nullable
        public final ContentRule stringToContentRule(@Nullable final String json) {
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<ContentRule>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(json, type)) instanceof ContentRule)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            return (ContentRule)(Result.isFailure-impl(o2) ? null : o2);
        }
    }
}
