package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import com.script.SimpleBindings;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.JsonExtensionsKt;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: HttpTTS.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/HttpTTS.class */
@JsonIgnoreProperties({"headerMap", PackageDocumentBase.DCTags.source, "_userNameSpace", "userNameSpace"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b(\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u0000 P2\u00020\u0001:\u0001PB\u008d\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003¢\u0006\u0002\u0010\u0011J\t\u00103\u001a\u00020\u0003HÆ\u0003J\u0010\u00104\u001a\u0004\u0018\u00010\u000eHÆ\u0003¢\u0006\u0002\u0010\u001cJ\u000b\u00105\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\u0005HÆ\u0003J\t\u00108\u001a\u00020\u0005HÆ\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0096\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u0003HÆ\u0001¢\u0006\u0002\u0010@J\u0013\u0010A\u001a\u00020\u000e2\b\u0010B\u001a\u0004\u0018\u00010CHÖ\u0003J\b\u0010D\u001a\u00020\u0005H\u0016J\n\u0010E\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010F\u001a\u00020\u0005H\u0016J\b\u0010G\u001a\u00020\u0005H\u0016J\t\u0010H\u001a\u00020IHÖ\u0001J\u0010\u0010J\u001a\u00020K2\b\u0010L\u001a\u0004\u0018\u00010\u001aJ\u000e\u0010M\u001a\u00020K2\u0006\u0010N\u001a\u00020\u0005J\t\u0010O\u001a\u00020\u0005HÖ\u0001R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0014\"\u0004\b\u0018\u0010\u0016R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0096\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0014\"\u0004\b!\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0014\"\u0004\b%\u0010\u0016R\u001a\u0010\u0010\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010#\"\u0004\b'\u0010(R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0014\"\u0004\b*\u0010\u0016R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0014\"\u0004\b,\u0010\u0016R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0014\"\u0004\b.\u0010\u0016R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0014\"\u0004\b0\u0010\u0016R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0014\"\u0004\b2\u0010\u0016¨\u0006Q"}, d2 = {"Lio/legado/app/data/entities/HttpTTS;", "Lio/legado/app/data/entities/BaseSource;", "id", PackageDocumentBase.PREFIX_OPF, "name", PackageDocumentBase.PREFIX_OPF, RSSKeywords.RSS_ITEM_URL, "contentType", "concurrentRate", "loginUrl", "loginUi", "header", "jsLib", "enabledCookieJar", PackageDocumentBase.PREFIX_OPF, "loginCheckJs", "lastUpdateTime", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;J)V", "_userNameSpace", "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "getContentType", "setContentType", "debugLog", "Lio/legado/app/model/DebugLog;", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getHeader", "setHeader", "getId", "()J", "getJsLib", "setJsLib", "getLastUpdateTime", "setLastUpdateTime", "(J)V", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "setLoginUi", "getLoginUrl", "setLoginUrl", "getName", "setName", "getUrl", "setUrl", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;J)Lio/legado/app/data/entities/HttpTTS;", "equals", "other", PackageDocumentBase.PREFIX_OPF, "getKey", "getLogger", "getTag", "getUserNameSpace", "hashCode", PackageDocumentBase.PREFIX_OPF, "setLogger", PackageDocumentBase.PREFIX_OPF, "logger", "setUserNameSpace", "nameSpace", "toString", "Companion", "reader-pro"})
public final /* data */ class HttpTTS implements BaseSource {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);
    private final long id;

    @NotNull
    private String name;

    @NotNull
    private String url;

    @Nullable
    private String contentType;

    @Nullable
    private String concurrentRate;

    @Nullable
    private String loginUrl;

    @Nullable
    private String loginUi;

    @Nullable
    private String header;

    @Nullable
    private String jsLib;

    @Nullable
    private Boolean enabledCookieJar;

    @Nullable
    private String loginCheckJs;
    private long lastUpdateTime;

    @NotNull
    private transient String _userNameSpace;

    @Nullable
    private transient DebugLog debugLog;

    public final long component1() {
        return this.id;
    }

    @NotNull
    public final String component2() {
        return this.name;
    }

    @NotNull
    public final String component3() {
        return this.url;
    }

    @Nullable
    public final String component4() {
        return this.contentType;
    }

    @Nullable
    public final String component5() {
        return getConcurrentRate();
    }

    @Nullable
    public final String component6() {
        return getLoginUrl();
    }

    @Nullable
    public final String component7() {
        return getLoginUi();
    }

    @Nullable
    public final String component8() {
        return getHeader();
    }

    @Nullable
    public final String component9() {
        return this.jsLib;
    }

    @Nullable
    public final Boolean component10() {
        return getEnabledCookieJar();
    }

    @Nullable
    public final String component11() {
        return this.loginCheckJs;
    }

    public final long component12() {
        return this.lastUpdateTime;
    }

    @NotNull
    public final HttpTTS copy(long id, @NotNull String name, @NotNull String url, @Nullable String contentType, @Nullable String concurrentRate, @Nullable String loginUrl, @Nullable String loginUi, @Nullable String header, @Nullable String jsLib, @Nullable Boolean enabledCookieJar, @Nullable String loginCheckJs, long lastUpdateTime) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        return new HttpTTS(id, name, url, contentType, concurrentRate, loginUrl, loginUi, header, jsLib, enabledCookieJar, loginCheckJs, lastUpdateTime);
    }

    public static /* synthetic */ HttpTTS copy$default(HttpTTS httpTTS, long j, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, Boolean bool, String str9, long j2, int i, Object obj) {
        if ((i & 1) != 0) {
            j = httpTTS.id;
        }
        if ((i & 2) != 0) {
            str = httpTTS.name;
        }
        if ((i & 4) != 0) {
            str2 = httpTTS.url;
        }
        if ((i & 8) != 0) {
            str3 = httpTTS.contentType;
        }
        if ((i & 16) != 0) {
            str4 = httpTTS.getConcurrentRate();
        }
        if ((i & 32) != 0) {
            str5 = httpTTS.getLoginUrl();
        }
        if ((i & 64) != 0) {
            str6 = httpTTS.getLoginUi();
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            str7 = httpTTS.getHeader();
        }
        if ((i & 256) != 0) {
            str8 = httpTTS.jsLib;
        }
        if ((i & 512) != 0) {
            bool = httpTTS.getEnabledCookieJar();
        }
        if ((i & 1024) != 0) {
            str9 = httpTTS.loginCheckJs;
        }
        if ((i & 2048) != 0) {
            j2 = httpTTS.lastUpdateTime;
        }
        return httpTTS.copy(j, str, str2, str3, str4, str5, str6, str7, str8, bool, str9, j2);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HttpTTS(id=").append(this.id).append(", name=").append(this.name).append(", url=").append(this.url).append(", contentType=").append((Object) this.contentType).append(", concurrentRate=").append((Object) getConcurrentRate()).append(", loginUrl=").append((Object) getLoginUrl()).append(", loginUi=").append((Object) getLoginUi()).append(", header=").append((Object) getHeader()).append(", jsLib=").append((Object) this.jsLib).append(", enabledCookieJar=").append(getEnabledCookieJar()).append(", loginCheckJs=").append((Object) this.loginCheckJs).append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime).append(')');
        return sb.toString();
    }

    public int hashCode() {
        int result = Long.hashCode(this.id);
        return (((((((((((((((((((((result * 31) + this.name.hashCode()) * 31) + this.url.hashCode()) * 31) + (this.contentType == null ? 0 : this.contentType.hashCode())) * 31) + (getConcurrentRate() == null ? 0 : getConcurrentRate().hashCode())) * 31) + (getLoginUrl() == null ? 0 : getLoginUrl().hashCode())) * 31) + (getLoginUi() == null ? 0 : getLoginUi().hashCode())) * 31) + (getHeader() == null ? 0 : getHeader().hashCode())) * 31) + (this.jsLib == null ? 0 : this.jsLib.hashCode())) * 31) + (getEnabledCookieJar() == null ? 0 : getEnabledCookieJar().hashCode())) * 31) + (this.loginCheckJs == null ? 0 : this.loginCheckJs.hashCode())) * 31) + Long.hashCode(this.lastUpdateTime);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpTTS)) {
            return false;
        }
        HttpTTS httpTTS = (HttpTTS) other;
        return this.id == httpTTS.id && Intrinsics.areEqual(this.name, httpTTS.name) && Intrinsics.areEqual(this.url, httpTTS.url) && Intrinsics.areEqual(this.contentType, httpTTS.contentType) && Intrinsics.areEqual(getConcurrentRate(), httpTTS.getConcurrentRate()) && Intrinsics.areEqual(getLoginUrl(), httpTTS.getLoginUrl()) && Intrinsics.areEqual(getLoginUi(), httpTTS.getLoginUi()) && Intrinsics.areEqual(getHeader(), httpTTS.getHeader()) && Intrinsics.areEqual(this.jsLib, httpTTS.jsLib) && Intrinsics.areEqual(getEnabledCookieJar(), httpTTS.getEnabledCookieJar()) && Intrinsics.areEqual(this.loginCheckJs, httpTTS.loginCheckJs) && this.lastUpdateTime == httpTTS.lastUpdateTime;
    }

    public HttpTTS() {
        this(0L, null, null, null, null, null, null, null, null, null, null, 0L, 4095, null);
    }

    public HttpTTS(long id, @NotNull String name, @NotNull String url, @Nullable String contentType, @Nullable String concurrentRate, @Nullable String loginUrl, @Nullable String loginUi, @Nullable String header, @Nullable String jsLib, @Nullable Boolean enabledCookieJar, @Nullable String loginCheckJs, long lastUpdateTime) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        this.id = id;
        this.name = name;
        this.url = url;
        this.contentType = contentType;
        this.concurrentRate = concurrentRate;
        this.loginUrl = loginUrl;
        this.loginUi = loginUi;
        this.header = header;
        this.jsLib = jsLib;
        this.enabledCookieJar = enabledCookieJar;
        this.loginCheckJs = loginCheckJs;
        this.lastUpdateTime = lastUpdateTime;
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

    public /* synthetic */ HttpTTS(long j, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, Boolean bool, String str9, long j2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? System.currentTimeMillis() : j, (i & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i & 8) != 0 ? null : str3, (i & 16) != 0 ? "0" : str4, (i & 32) != 0 ? null : str5, (i & 64) != 0 ? null : str6, (i & Wbxml.EXT_T_0) != 0 ? null : str7, (i & 256) != 0 ? null : str8, (i & 512) != 0 ? false : bool, (i & 1024) != 0 ? null : str9, (i & 2048) != 0 ? System.currentTimeMillis() : j2);
    }

    public final long getId() {
        return this.id;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.name = str;
    }

    @NotNull
    public final String getUrl() {
        return this.url;
    }

    public final void setUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.url = str;
    }

    @Nullable
    public final String getContentType() {
        return this.contentType;
    }

    public final void setContentType(@Nullable String str) {
        this.contentType = str;
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

    @Override // io.legado.app.data.entities.BaseSource
    @Nullable
    public String getHeader() {
        return this.header;
    }

    @Override // io.legado.app.data.entities.BaseSource
    public void setHeader(@Nullable String str) {
        this.header = str;
    }

    @Nullable
    public final String getJsLib() {
        return this.jsLib;
    }

    public final void setJsLib(@Nullable String str) {
        this.jsLib = str;
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

    @Nullable
    public final String getLoginCheckJs() {
        return this.loginCheckJs;
    }

    public final void setLoginCheckJs(@Nullable String str) {
        this.loginCheckJs = str;
    }

    public final long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public final void setLastUpdateTime(long j) {
        this.lastUpdateTime = j;
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

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public String getTag() {
        return this.name;
    }

    @Override // io.legado.app.data.entities.BaseSource
    @NotNull
    public String getKey() {
        return Intrinsics.stringPlus("httpTts:", Long.valueOf(this.id));
    }

    /* JADX INFO: compiled from: HttpTTS.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/HttpTTS$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\b\u0010\tJ4\u0010\n\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00050\u000bj\b\u0012\u0004\u0012\u00020\u0005`\f0\u00042\u0006\u0010\r\u001a\u00020\u0007ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u000e\u0010\tJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0010\u001a\u00020\u0011ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0012\u0010\u0013\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u0014"}, d2 = {"Lio/legado/app/data/entities/HttpTTS$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/HttpTTS;", "json", PackageDocumentBase.PREFIX_OPF, "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "jsonArray", "fromJsonArray-IoAF18A", "fromJsonDoc", "doc", "Lcom/jayway/jsonpath/DocumentContext;", "fromJsonDoc-IoAF18A", "(Lcom/jayway/jsonpath/DocumentContext;)Ljava/lang/Object;", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonDoc-IoAF18A, reason: not valid java name */
        public final Object m155fromJsonDocIoAF18A(@NotNull DocumentContext doc) {
            Object obj;
            Intrinsics.checkNotNullParameter(doc, "doc");
            try {
                Result.Companion companion = Result.Companion;
                Object loginUi = doc.read("$.loginUi", new Predicate[0]);
                Long l = JsonExtensionsKt.readLong((ReadContext) doc, "$.id");
                long jCurrentTimeMillis = l == null ? System.currentTimeMillis() : l.longValue();
                String string = JsonExtensionsKt.readString((ReadContext) doc, "$.name");
                Intrinsics.checkNotNull(string);
                String string2 = JsonExtensionsKt.readString((ReadContext) doc, "$.url");
                Intrinsics.checkNotNull(string2);
                obj = Result.constructor-impl(new HttpTTS(jCurrentTimeMillis, string, string2, JsonExtensionsKt.readString((ReadContext) doc, "$.contentType"), JsonExtensionsKt.readString((ReadContext) doc, "$.concurrentRate"), JsonExtensionsKt.readString((ReadContext) doc, "$.loginUrl"), loginUi instanceof List ? GsonExtensionsKt.getGSON().toJson(loginUi) : loginUi == null ? null : loginUi.toString(), JsonExtensionsKt.readString((ReadContext) doc, "$.header"), null, null, JsonExtensionsKt.readString((ReadContext) doc, "$.loginCheckJs"), 0L, 2816, null));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            return obj;
        }

        @NotNull
        /* JADX INFO: renamed from: fromJson-IoAF18A, reason: not valid java name */
        public final Object m156fromJsonIoAF18A(@NotNull String json) {
            Intrinsics.checkNotNullParameter(json, "json");
            DocumentContext documentContext = JsonExtensionsKt.getJsonPath().parse(json);
            Intrinsics.checkNotNullExpressionValue(documentContext, "jsonPath.parse(json)");
            return m155fromJsonDocIoAF18A(documentContext);
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonArray-IoAF18A, reason: not valid java name */
        public final Object m157fromJsonArrayIoAF18A(@NotNull String jsonArray) {
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
                    Companion companion2 = HttpTTS.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue(jsonItem, "jsonItem");
                    Object objM155fromJsonDocIoAF18A = companion2.m155fromJsonDocIoAF18A(jsonItem);
                    ResultKt.throwOnFailure(objM155fromJsonDocIoAF18A);
                    HttpTTS source = (HttpTTS) objM155fromJsonDocIoAF18A;
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
