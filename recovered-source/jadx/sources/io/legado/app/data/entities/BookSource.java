package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.script.SimpleBindings;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.data.entities.rule.ContentRule;
import io.legado.app.data.entities.rule.ExploreRule;
import io.legado.app.data.entities.rule.SearchRule;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.help.SourceAnalyzer;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.GsonExtensionsKt;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: BookSource.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BookSource.class */
@JsonIgnoreProperties({"headerMap", PackageDocumentBase.DCTags.source, "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\bZ\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\b\b\u0087\b\u0018\u0000 ª\u00012\u00020\u0001:\u0006ª\u0001«\u0001¬\u0001B«\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0016\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0016\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u001b\u0012\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e\u0012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010 \u0012\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$¢\u0006\u0002\u0010%J\t\u0010w\u001a\u00020\u0003HÆ\u0003J\u000b\u0010x\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010y\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010z\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010{\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010|\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010}\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010~\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u007f\u001a\u00020\u0016HÆ\u0003J\n\u0010\u0080\u0001\u001a\u00020\u0016HÆ\u0003J\n\u0010\u0081\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u0082\u0001\u001a\u00020\u0003HÆ\u0003J\f\u0010\u0083\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u001bHÆ\u0003J\f\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u001eHÆ\u0003J\f\u0010\u0087\u0001\u001a\u0004\u0018\u00010 HÆ\u0003J\f\u0010\u0088\u0001\u001a\u0004\u0018\u00010\"HÆ\u0003J\f\u0010\u0089\u0001\u001a\u0004\u0018\u00010$HÆ\u0003J\f\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u008b\u0001\u001a\u00020\u0007HÆ\u0003J\f\u0010\u008c\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u008d\u0001\u001a\u00020\u0007HÆ\u0003J\n\u0010\u008e\u0001\u001a\u00020\u000bHÆ\u0003J\n\u0010\u008f\u0001\u001a\u00020\u000bHÆ\u0003J\u0011\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u000bHÆ\u0003¢\u0006\u0002\u0010DJ¶\u0002\u0010\u0091\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00162\b\b\u0002\u0010\u0018\u001a\u00020\u00072\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010 2\n\b\u0002\u0010!\u001a\u0004\u0018\u00010\"2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010$HÆ\u0001¢\u0006\u0003\u0010\u0092\u0001J\u0010\u0010\u0093\u0001\u001a\u00020\u000b2\u0007\u0010\u0094\u0001\u001a\u00020\u0000J\u001f\u0010\u0093\u0001\u001a\u00020\u000b2\t\u0010\u0095\u0001\u001a\u0004\u0018\u00010\u00032\t\u0010\u0096\u0001\u001a\u0004\u0018\u00010\u0003H\u0002J\u0016\u0010\u0097\u0001\u001a\u00020\u000b2\n\u0010\u0098\u0001\u001a\u0005\u0018\u00010\u0099\u0001H\u0096\u0002J\u0007\u0010\u009a\u0001\u001a\u00020 J\u0007\u0010\u009b\u0001\u001a\u00020$J\u0007\u0010\u009c\u0001\u001a\u00020\u001bJ\t\u0010\u009d\u0001\u001a\u00020\u0003H\u0016J\u000b\u0010\u009e\u0001\u001a\u0004\u0018\u00010>H\u0016J\u0007\u0010\u009f\u0001\u001a\u00020\u001eJ\t\u0010 \u0001\u001a\u00020\u0003H\u0016J\u0007\u0010¡\u0001\u001a\u00020\"J\t\u0010¢\u0001\u001a\u00020\u0003H\u0016J\t\u0010£\u0001\u001a\u00020\u0007H\u0016J\u0013\u0010¤\u0001\u001a\u00030¥\u00012\t\u0010¦\u0001\u001a\u0004\u0018\u00010>J\u0011\u0010§\u0001\u001a\u00030¥\u00012\u0007\u0010¨\u0001\u001a\u00020\u0003J\n\u0010©\u0001\u001a\u00020\u0003HÖ\u0001R\u000e\u0010&\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010)\"\u0004\b-\u0010+R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010)\"\u0004\b/\u0010+R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010)\"\u0004\b5\u0010+R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010)\"\u0004\b7\u0010+R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010)\"\u0004\b9\u0010+R\u0010\u0010:\u001a\u0004\u0018\u00010$X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u00101\"\u0004\b<\u00103R\u0010\u0010=\u001a\u0004\u0018\u00010>X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b?\u0010@\"\u0004\bA\u0010BR\u001e\u0010\r\u001a\u0004\u0018\u00010\u000bX\u0096\u000e¢\u0006\u0010\n\u0002\u0010G\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010@\"\u0004\bI\u0010BR\u0010\u0010J\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bK\u0010)\"\u0004\bL\u0010+R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bM\u0010)\"\u0004\bN\u0010+R\u001a\u0010\u0015\u001a\u00020\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bO\u0010P\"\u0004\bQ\u0010RR\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bS\u0010)\"\u0004\bT\u0010+R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bU\u0010)\"\u0004\bV\u0010+R\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bW\u0010)\"\u0004\bX\u0010+R\u001a\u0010\u0017\u001a\u00020\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bY\u0010P\"\u0004\bZ\u0010RR\u001c\u0010\u001f\u001a\u0004\u0018\u00010 X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b[\u0010\\\"\u0004\b]\u0010^R\u001c\u0010#\u001a\u0004\u0018\u00010$X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b_\u0010`\"\u0004\ba\u0010bR\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bc\u0010d\"\u0004\be\u0010fR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bg\u0010h\"\u0004\bi\u0010jR\u001c\u0010!\u001a\u0004\u0018\u00010\"X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bk\u0010l\"\u0004\bm\u0010nR\u0010\u0010o\u001a\u0004\u0018\u00010\u001eX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u001c\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bp\u0010)\"\u0004\bq\u0010+R\u0010\u0010r\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bs\u0010)\"\u0004\bt\u0010+R\u001a\u0010\u0018\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bu\u00101\"\u0004\bv\u00103¨\u0006\u00ad\u0001"}, d2 = {"Lio/legado/app/data/entities/BookSource;", "Lio/legado/app/data/entities/BaseSource;", "bookSourceUrl", PackageDocumentBase.PREFIX_OPF, "bookSourceName", "bookSourceGroup", "bookSourceType", PackageDocumentBase.PREFIX_OPF, "bookUrlPattern", "customOrder", "enabled", PackageDocumentBase.PREFIX_OPF, "enabledExplore", "enabledCookieJar", "concurrentRate", "header", "loginUrl", "loginUi", "loginCheckJs", "bookSourceComment", "variableComment", "lastUpdateTime", PackageDocumentBase.PREFIX_OPF, "respondTime", "weight", "exploreUrl", "ruleExplore", "Lio/legado/app/data/entities/rule/ExploreRule;", "searchUrl", "ruleSearch", "Lio/legado/app/data/entities/rule/SearchRule;", "ruleBookInfo", "Lio/legado/app/data/entities/rule/BookInfoRule;", "ruleToc", "Lio/legado/app/data/entities/rule/TocRule;", "ruleContent", "Lio/legado/app/data/entities/rule/ContentRule;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZLjava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Lio/legado/app/data/entities/rule/ExploreRule;Ljava/lang/String;Lio/legado/app/data/entities/rule/SearchRule;Lio/legado/app/data/entities/rule/BookInfoRule;Lio/legado/app/data/entities/rule/TocRule;Lio/legado/app/data/entities/rule/ContentRule;)V", "_userNameSpace", "bookInfoRuleV", "getBookSourceComment", "()Ljava/lang/String;", "setBookSourceComment", "(Ljava/lang/String;)V", "getBookSourceGroup", "setBookSourceGroup", "getBookSourceName", "setBookSourceName", "getBookSourceType", "()I", "setBookSourceType", "(I)V", "getBookSourceUrl", "setBookSourceUrl", "getBookUrlPattern", "setBookUrlPattern", "getConcurrentRate", "setConcurrentRate", "contentRuleV", "getCustomOrder", "setCustomOrder", "debugLog", "Lio/legado/app/model/DebugLog;", "getEnabled", "()Z", "setEnabled", "(Z)V", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getEnabledExplore", "setEnabledExplore", "exploreRuleV", "getExploreUrl", "setExploreUrl", "getHeader", "setHeader", "getLastUpdateTime", "()J", "setLastUpdateTime", "(J)V", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "setLoginUi", "getLoginUrl", "setLoginUrl", "getRespondTime", "setRespondTime", "getRuleBookInfo", "()Lio/legado/app/data/entities/rule/BookInfoRule;", "setRuleBookInfo", "(Lio/legado/app/data/entities/rule/BookInfoRule;)V", "getRuleContent", "()Lio/legado/app/data/entities/rule/ContentRule;", "setRuleContent", "(Lio/legado/app/data/entities/rule/ContentRule;)V", "getRuleExplore", "()Lio/legado/app/data/entities/rule/ExploreRule;", "setRuleExplore", "(Lio/legado/app/data/entities/rule/ExploreRule;)V", "getRuleSearch", "()Lio/legado/app/data/entities/rule/SearchRule;", "setRuleSearch", "(Lio/legado/app/data/entities/rule/SearchRule;)V", "getRuleToc", "()Lio/legado/app/data/entities/rule/TocRule;", "setRuleToc", "(Lio/legado/app/data/entities/rule/TocRule;)V", "searchRuleV", "getSearchUrl", "setSearchUrl", "tocRuleV", "getVariableComment", "setVariableComment", "getWeight", "setWeight", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZLjava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Lio/legado/app/data/entities/rule/ExploreRule;Ljava/lang/String;Lio/legado/app/data/entities/rule/SearchRule;Lio/legado/app/data/entities/rule/BookInfoRule;Lio/legado/app/data/entities/rule/TocRule;Lio/legado/app/data/entities/rule/ContentRule;)Lio/legado/app/data/entities/BookSource;", "equal", PackageDocumentBase.DCTags.source, NCXDocumentV3.XHTMLTgs.a, "b", "equals", "other", PackageDocumentBase.PREFIX_OPF, "getBookInfoRule", "getContentRule", "getExploreRule", "getKey", "getLogger", "getSearchRule", "getTag", "getTocRule", "getUserNameSpace", "hashCode", "setLogger", PackageDocumentBase.PREFIX_OPF, "logger", "setUserNameSpace", "nameSpace", "toString", "Companion", "Converters", "ExploreKind", "reader-pro"})
public final /* data */ class BookSource implements BaseSource {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

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
        return getEnabledCookieJar();
    }

    @Nullable
    public final String component10() {
        return getConcurrentRate();
    }

    @Nullable
    public final String component11() {
        return getHeader();
    }

    @Nullable
    public final String component12() {
        return getLoginUrl();
    }

    @Nullable
    public final String component13() {
        return getLoginUi();
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
    public final BookSource copy(@NotNull String bookSourceUrl, @NotNull String bookSourceName, @Nullable String bookSourceGroup, int bookSourceType, @Nullable String bookUrlPattern, int customOrder, boolean enabled, boolean enabledExplore, @Nullable Boolean enabledCookieJar, @Nullable String concurrentRate, @Nullable String header, @Nullable String loginUrl, @Nullable String loginUi, @Nullable String loginCheckJs, @Nullable String bookSourceComment, @Nullable String variableComment, long lastUpdateTime, long respondTime, int weight, @Nullable String exploreUrl, @Nullable ExploreRule ruleExplore, @Nullable String searchUrl, @Nullable SearchRule ruleSearch, @Nullable BookInfoRule ruleBookInfo, @Nullable TocRule ruleToc, @Nullable ContentRule ruleContent) {
        Intrinsics.checkNotNullParameter(bookSourceUrl, "bookSourceUrl");
        Intrinsics.checkNotNullParameter(bookSourceName, "bookSourceName");
        return new BookSource(bookSourceUrl, bookSourceName, bookSourceGroup, bookSourceType, bookUrlPattern, customOrder, enabled, enabledExplore, enabledCookieJar, concurrentRate, header, loginUrl, loginUi, loginCheckJs, bookSourceComment, variableComment, lastUpdateTime, respondTime, weight, exploreUrl, ruleExplore, searchUrl, ruleSearch, ruleBookInfo, ruleToc, ruleContent);
    }

    public static /* synthetic */ BookSource copy$default(BookSource bookSource, String str, String str2, String str3, int i, String str4, int i2, boolean z, boolean z2, Boolean bool, String str5, String str6, String str7, String str8, String str9, String str10, String str11, long j, long j2, int i3, String str12, ExploreRule exploreRule, String str13, SearchRule searchRule, BookInfoRule bookInfoRule, TocRule tocRule, ContentRule contentRule, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            str = bookSource.bookSourceUrl;
        }
        if ((i4 & 2) != 0) {
            str2 = bookSource.bookSourceName;
        }
        if ((i4 & 4) != 0) {
            str3 = bookSource.bookSourceGroup;
        }
        if ((i4 & 8) != 0) {
            i = bookSource.bookSourceType;
        }
        if ((i4 & 16) != 0) {
            str4 = bookSource.bookUrlPattern;
        }
        if ((i4 & 32) != 0) {
            i2 = bookSource.customOrder;
        }
        if ((i4 & 64) != 0) {
            z = bookSource.enabled;
        }
        if ((i4 & Wbxml.EXT_T_0) != 0) {
            z2 = bookSource.enabledExplore;
        }
        if ((i4 & 256) != 0) {
            bool = bookSource.getEnabledCookieJar();
        }
        if ((i4 & 512) != 0) {
            str5 = bookSource.getConcurrentRate();
        }
        if ((i4 & 1024) != 0) {
            str6 = bookSource.getHeader();
        }
        if ((i4 & 2048) != 0) {
            str7 = bookSource.getLoginUrl();
        }
        if ((i4 & 4096) != 0) {
            str8 = bookSource.getLoginUi();
        }
        if ((i4 & IOUtil.DEFAULT_BUFFER_SIZE) != 0) {
            str9 = bookSource.loginCheckJs;
        }
        if ((i4 & 16384) != 0) {
            str10 = bookSource.bookSourceComment;
        }
        if ((i4 & 32768) != 0) {
            str11 = bookSource.variableComment;
        }
        if ((i4 & 65536) != 0) {
            j = bookSource.lastUpdateTime;
        }
        if ((i4 & 131072) != 0) {
            j2 = bookSource.respondTime;
        }
        if ((i4 & 262144) != 0) {
            i3 = bookSource.weight;
        }
        if ((i4 & 524288) != 0) {
            str12 = bookSource.exploreUrl;
        }
        if ((i4 & 1048576) != 0) {
            exploreRule = bookSource.ruleExplore;
        }
        if ((i4 & 2097152) != 0) {
            str13 = bookSource.searchUrl;
        }
        if ((i4 & 4194304) != 0) {
            searchRule = bookSource.ruleSearch;
        }
        if ((i4 & 8388608) != 0) {
            bookInfoRule = bookSource.ruleBookInfo;
        }
        if ((i4 & 16777216) != 0) {
            tocRule = bookSource.ruleToc;
        }
        if ((i4 & 33554432) != 0) {
            contentRule = bookSource.ruleContent;
        }
        return bookSource.copy(str, str2, str3, i, str4, i2, z, z2, bool, str5, str6, str7, str8, str9, str10, str11, j, j2, i3, str12, exploreRule, str13, searchRule, bookInfoRule, tocRule, contentRule);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BookSource(bookSourceUrl=").append(this.bookSourceUrl).append(", bookSourceName=").append(this.bookSourceName).append(", bookSourceGroup=").append((Object) this.bookSourceGroup).append(", bookSourceType=").append(this.bookSourceType).append(", bookUrlPattern=").append((Object) this.bookUrlPattern).append(", customOrder=").append(this.customOrder).append(", enabled=").append(this.enabled).append(", enabledExplore=").append(this.enabledExplore).append(", enabledCookieJar=").append(getEnabledCookieJar()).append(", concurrentRate=").append((Object) getConcurrentRate()).append(", header=").append((Object) getHeader()).append(", loginUrl=");
        sb.append((Object) getLoginUrl()).append(", loginUi=").append((Object) getLoginUi()).append(", loginCheckJs=").append((Object) this.loginCheckJs).append(", bookSourceComment=").append((Object) this.bookSourceComment).append(", variableComment=").append((Object) this.variableComment).append(", lastUpdateTime=").append(this.lastUpdateTime).append(", respondTime=").append(this.respondTime).append(", weight=").append(this.weight).append(", exploreUrl=").append((Object) this.exploreUrl).append(", ruleExplore=").append(this.ruleExplore).append(", searchUrl=").append((Object) this.searchUrl).append(", ruleSearch=").append(this.ruleSearch);
        sb.append(", ruleBookInfo=").append(this.ruleBookInfo).append(", ruleToc=").append(this.ruleToc).append(", ruleContent=").append(this.ruleContent).append(')');
        return sb.toString();
    }

    public BookSource() {
        this(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null);
    }

    public BookSource(@NotNull String bookSourceUrl, @NotNull String bookSourceName, @Nullable String bookSourceGroup, int bookSourceType, @Nullable String bookUrlPattern, int customOrder, boolean enabled, boolean enabledExplore, @Nullable Boolean enabledCookieJar, @Nullable String concurrentRate, @Nullable String header, @Nullable String loginUrl, @Nullable String loginUi, @Nullable String loginCheckJs, @Nullable String bookSourceComment, @Nullable String variableComment, long lastUpdateTime, long respondTime, int weight, @Nullable String exploreUrl, @Nullable ExploreRule ruleExplore, @Nullable String searchUrl, @Nullable SearchRule ruleSearch, @Nullable BookInfoRule ruleBookInfo, @Nullable TocRule ruleToc, @Nullable ContentRule ruleContent) {
        Intrinsics.checkNotNullParameter(bookSourceUrl, "bookSourceUrl");
        Intrinsics.checkNotNullParameter(bookSourceName, "bookSourceName");
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

    public /* synthetic */ BookSource(String str, String str2, String str3, int i, String str4, int i2, boolean z, boolean z2, Boolean bool, String str5, String str6, String str7, String str8, String str9, String str10, String str11, long j, long j2, int i3, String str12, ExploreRule exploreRule, String str13, SearchRule searchRule, BookInfoRule bookInfoRule, TocRule tocRule, ContentRule contentRule, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i4 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i4 & 4) != 0 ? null : str3, (i4 & 8) != 0 ? 0 : i, (i4 & 16) != 0 ? null : str4, (i4 & 32) != 0 ? 0 : i2, (i4 & 64) != 0 ? true : z, (i4 & Wbxml.EXT_T_0) != 0 ? true : z2, (i4 & 256) != 0 ? false : bool, (i4 & 512) != 0 ? null : str5, (i4 & 1024) != 0 ? null : str6, (i4 & 2048) != 0 ? null : str7, (i4 & 4096) != 0 ? null : str8, (i4 & IOUtil.DEFAULT_BUFFER_SIZE) != 0 ? null : str9, (i4 & 16384) != 0 ? null : str10, (i4 & 32768) != 0 ? null : str11, (i4 & 65536) != 0 ? 0L : j, (i4 & 131072) != 0 ? 180000L : j2, (i4 & 262144) != 0 ? 0 : i3, (i4 & 524288) != 0 ? null : str12, (i4 & 1048576) != 0 ? null : exploreRule, (i4 & 2097152) != 0 ? null : str13, (i4 & 4194304) != 0 ? null : searchRule, (i4 & 8388608) != 0 ? null : bookInfoRule, (i4 & 16777216) != 0 ? null : tocRule, (i4 & 33554432) != 0 ? null : contentRule);
    }

    @NotNull
    public final String getBookSourceUrl() {
        return this.bookSourceUrl;
    }

    public final void setBookSourceUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.bookSourceUrl = str;
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
    public final String getBookSourceComment() {
        return this.bookSourceComment;
    }

    public final void setBookSourceComment(@Nullable String str) {
        this.bookSourceComment = str;
    }

    @Nullable
    public final String getVariableComment() {
        return this.variableComment;
    }

    public final void setVariableComment(@Nullable String str) {
        this.variableComment = str;
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
    public final ExploreRule getRuleExplore() {
        return this.ruleExplore;
    }

    public final void setRuleExplore(@Nullable ExploreRule exploreRule) {
        this.ruleExplore = exploreRule;
    }

    @Nullable
    public final String getSearchUrl() {
        return this.searchUrl;
    }

    public final void setSearchUrl(@Nullable String str) {
        this.searchUrl = str;
    }

    @Nullable
    public final SearchRule getRuleSearch() {
        return this.ruleSearch;
    }

    public final void setRuleSearch(@Nullable SearchRule searchRule) {
        this.ruleSearch = searchRule;
    }

    @Nullable
    public final BookInfoRule getRuleBookInfo() {
        return this.ruleBookInfo;
    }

    public final void setRuleBookInfo(@Nullable BookInfoRule bookInfoRule) {
        this.ruleBookInfo = bookInfoRule;
    }

    @Nullable
    public final TocRule getRuleToc() {
        return this.ruleToc;
    }

    public final void setRuleToc(@Nullable TocRule tocRule) {
        this.ruleToc = tocRule;
    }

    @Nullable
    public final ContentRule getRuleContent() {
        return this.ruleContent;
    }

    public final void setRuleContent(@Nullable ContentRule contentRule) {
        this.ruleContent = contentRule;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public String getTag() {
        return this.bookSourceName;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public String getKey() {
        return this.bookSourceUrl;
    }

    public int hashCode() {
        return this.bookSourceUrl.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (other instanceof BookSource) {
            return Intrinsics.areEqual(((BookSource) other).bookSourceUrl, this.bookSourceUrl);
        }
        return false;
    }

    @NotNull
    public final SearchRule getSearchRule() {
        SearchRule searchRule = this.ruleSearch;
        return searchRule == null ? new SearchRule(null, null, null, null, null, null, null, null, null, null, 1023, null) : searchRule;
    }

    @NotNull
    public final ExploreRule getExploreRule() {
        ExploreRule exploreRule = this.ruleExplore;
        return exploreRule == null ? new ExploreRule(null, null, null, null, null, null, null, null, null, null, 1023, null) : exploreRule;
    }

    @NotNull
    public final BookInfoRule getBookInfoRule() {
        BookInfoRule bookInfoRule = this.ruleBookInfo;
        return bookInfoRule == null ? new BookInfoRule(null, null, null, null, null, null, null, null, null, null, null, 2047, null) : bookInfoRule;
    }

    @NotNull
    public final TocRule getTocRule() {
        TocRule tocRule = this.ruleToc;
        return tocRule == null ? new TocRule(null, null, null, null, null, null, null, null, 255, null) : tocRule;
    }

    @NotNull
    public final ContentRule getContentRule() {
        ContentRule contentRule = this.ruleContent;
        return contentRule == null ? new ContentRule(null, null, null, null, null, null, 63, null) : contentRule;
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
        return this.debugLog;
    }

    public final boolean equal(@NotNull BookSource source) {
        Intrinsics.checkNotNullParameter(source, PackageDocumentBase.DCTags.source);
        return equal(this.bookSourceName, source.bookSourceName) && equal(this.bookSourceUrl, source.bookSourceUrl) && equal(this.bookSourceGroup, source.bookSourceGroup) && this.bookSourceType == source.bookSourceType && equal(this.bookUrlPattern, source.bookUrlPattern) && this.enabled == source.enabled && this.enabledExplore == source.enabledExplore && Intrinsics.areEqual(getEnabledCookieJar(), source.getEnabledCookieJar()) && equal(getHeader(), source.getHeader()) && equal(getLoginUrl(), source.getLoginUrl()) && equal(this.exploreUrl, source.exploreUrl) && equal(this.searchUrl, source.searchUrl) && Intrinsics.areEqual(getSearchRule(), source.getSearchRule()) && Intrinsics.areEqual(getExploreRule(), source.getExploreRule()) && Intrinsics.areEqual(getBookInfoRule(), source.getBookInfoRule()) && Intrinsics.areEqual(getTocRule(), source.getTocRule()) && Intrinsics.areEqual(getContentRule(), source.getContentRule());
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

    /* JADX INFO: compiled from: BookSource.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BookSource$ExploreKind.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u001f\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t¨\u0006\u0015"}, d2 = {"Lio/legado/app/data/entities/BookSource$ExploreKind;", PackageDocumentBase.PREFIX_OPF, "title", PackageDocumentBase.PREFIX_OPF, RSSKeywords.RSS_ITEM_URL, "(Ljava/lang/String;Ljava/lang/String;)V", "getTitle", "()Ljava/lang/String;", "setTitle", "(Ljava/lang/String;)V", "getUrl", "setUrl", "component1", "component2", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
    public static final /* data */ class ExploreKind {

        @NotNull
        private String title;

        @Nullable
        private String url;

        @NotNull
        public final String component1() {
            return this.title;
        }

        @Nullable
        public final String component2() {
            return this.url;
        }

        @NotNull
        public final ExploreKind copy(@NotNull String title, @Nullable String url) {
            Intrinsics.checkNotNullParameter(title, "title");
            return new ExploreKind(title, url);
        }

        public static /* synthetic */ ExploreKind copy$default(ExploreKind exploreKind, String str, String str2, int i, Object obj) {
            if ((i & 1) != 0) {
                str = exploreKind.title;
            }
            if ((i & 2) != 0) {
                str2 = exploreKind.url;
            }
            return exploreKind.copy(str, str2);
        }

        @NotNull
        public String toString() {
            return "ExploreKind(title=" + this.title + ", url=" + ((Object) this.url) + ')';
        }

        public int hashCode() {
            int result = this.title.hashCode();
            return (result * 31) + (this.url == null ? 0 : this.url.hashCode());
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ExploreKind)) {
                return false;
            }
            ExploreKind exploreKind = (ExploreKind) other;
            return Intrinsics.areEqual(this.title, exploreKind.title) && Intrinsics.areEqual(this.url, exploreKind.url);
        }

        public ExploreKind(@NotNull String title, @Nullable String url) {
            Intrinsics.checkNotNullParameter(title, "title");
            this.title = title;
            this.url = url;
        }

        public /* synthetic */ ExploreKind(String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : str2);
        }

        @NotNull
        public final String getTitle() {
            return this.title;
        }

        public final void setTitle(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.title = str;
        }

        @Nullable
        public final String getUrl() {
            return this.url;
        }

        public final void setUrl(@Nullable String str) {
            this.url = str;
        }
    }

    /* JADX INFO: compiled from: BookSource.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BookSource$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\b\u0010\tJ*\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0006\u0010\f\u001a\u00020\rø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ*\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000b0\u00042\u0006\u0010\u0006\u001a\u00020\u0007ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u000e\u0010\t\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u0010"}, d2 = {"Lio/legado/app/data/entities/BookSource$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/BookSource;", "json", PackageDocumentBase.PREFIX_OPF, "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", PackageDocumentBase.PREFIX_OPF, "inputStream", "Ljava/io/InputStream;", "fromJsonArray-IoAF18A", "(Ljava/io/InputStream;)Ljava/lang/Object;", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        /* JADX INFO: renamed from: fromJson-IoAF18A, reason: not valid java name */
        public final Object m151fromJsonIoAF18A(@NotNull String json) {
            Intrinsics.checkNotNullParameter(json, "json");
            return SourceAnalyzer.INSTANCE.m173jsonToBookSourceIoAF18A(json);
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonArray-IoAF18A, reason: not valid java name */
        public final Object m152fromJsonArrayIoAF18A(@NotNull String json) {
            Intrinsics.checkNotNullParameter(json, "json");
            return SourceAnalyzer.INSTANCE.m171jsonToBookSourcesIoAF18A(json);
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonArray-IoAF18A, reason: not valid java name */
        public final Object m153fromJsonArrayIoAF18A(@NotNull InputStream inputStream) {
            Intrinsics.checkNotNullParameter(inputStream, "inputStream");
            return SourceAnalyzer.INSTANCE.m172jsonToBookSourcesIoAF18A(inputStream);
        }
    }

    /* JADX INFO: compiled from: BookSource.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BookSource$Converters.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u0010\u0010\u0007\u001a\u00020\u00042\b\u0010\b\u001a\u0004\u0018\u00010\tJ\u0010\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\fJ\u0010\u0010\r\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0012\u0010\u0010\u001a\u0004\u0018\u00010\u00062\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\t2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0010\u0010\u0017\u001a\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u0016¨\u0006\u0019"}, d2 = {"Lio/legado/app/data/entities/BookSource$Converters;", PackageDocumentBase.PREFIX_OPF, "()V", "bookInfoRuleToString", PackageDocumentBase.PREFIX_OPF, "bookInfoRule", "Lio/legado/app/data/entities/rule/BookInfoRule;", "contentRuleToString", "contentRule", "Lio/legado/app/data/entities/rule/ContentRule;", "exploreRuleToString", "exploreRule", "Lio/legado/app/data/entities/rule/ExploreRule;", "searchRuleToString", "searchRule", "Lio/legado/app/data/entities/rule/SearchRule;", "stringToBookInfoRule", "json", "stringToContentRule", "stringToExploreRule", "stringToSearchRule", "stringToTocRule", "Lio/legado/app/data/entities/rule/TocRule;", "tocRuleToString", "tocRule", "reader-pro"})
    public static final class Converters {
        @NotNull
        public final String exploreRuleToString(@Nullable ExploreRule exploreRule) {
            String json = GsonExtensionsKt.getGSON().toJson(exploreRule);
            Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(exploreRule)");
            return json;
        }

        @Nullable
        public final ExploreRule stringToExploreRule(@Nullable String json) {
            Object obj;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new BookSource$Converters$stringToExploreRule$$inlined$fromJsonObject$1().getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json, type);
                if (!(objFromJson instanceof ExploreRule)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((ExploreRule) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (ExploreRule) (Result.isFailure-impl(obj2) ? null : obj2);
        }

        @NotNull
        public final String searchRuleToString(@Nullable SearchRule searchRule) {
            String json = GsonExtensionsKt.getGSON().toJson(searchRule);
            Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(searchRule)");
            return json;
        }

        @Nullable
        public final SearchRule stringToSearchRule(@Nullable String json) {
            Object obj;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new BookSource$Converters$stringToSearchRule$$inlined$fromJsonObject$1().getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json, type);
                if (!(objFromJson instanceof SearchRule)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((SearchRule) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (SearchRule) (Result.isFailure-impl(obj2) ? null : obj2);
        }

        @NotNull
        public final String bookInfoRuleToString(@Nullable BookInfoRule bookInfoRule) {
            String json = GsonExtensionsKt.getGSON().toJson(bookInfoRule);
            Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(bookInfoRule)");
            return json;
        }

        @Nullable
        public final BookInfoRule stringToBookInfoRule(@Nullable String json) {
            Object obj;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new BookSource$Converters$stringToBookInfoRule$$inlined$fromJsonObject$1().getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json, type);
                if (!(objFromJson instanceof BookInfoRule)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((BookInfoRule) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (BookInfoRule) (Result.isFailure-impl(obj2) ? null : obj2);
        }

        @NotNull
        public final String tocRuleToString(@Nullable TocRule tocRule) {
            String json = GsonExtensionsKt.getGSON().toJson(tocRule);
            Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(tocRule)");
            return json;
        }

        @Nullable
        public final TocRule stringToTocRule(@Nullable String json) {
            Object obj;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new BookSource$Converters$stringToTocRule$$inlined$fromJsonObject$1().getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json, type);
                if (!(objFromJson instanceof TocRule)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((TocRule) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (TocRule) (Result.isFailure-impl(obj2) ? null : obj2);
        }

        @NotNull
        public final String contentRuleToString(@Nullable ContentRule contentRule) {
            String json = GsonExtensionsKt.getGSON().toJson(contentRule);
            Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(contentRule)");
            return json;
        }

        @Nullable
        public final ContentRule stringToContentRule(@Nullable String json) {
            Object obj;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new BookSource$Converters$stringToContentRule$$inlined$fromJsonObject$1().getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json, type);
                if (!(objFromJson instanceof ContentRule)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((ContentRule) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (ContentRule) (Result.isFailure-impl(obj2) ? null : obj2);
        }
    }
}
