// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import java.util.Iterator;
import java.util.ArrayList;
import kotlin.Result$Companion;
import kotlin.ResultKt;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.List;
import io.legado.app.utils.JsonExtensionsKt;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.Predicate;
import kotlin.Result;
import com.jayway.jsonpath.DocumentContext;
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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "headerMap", "source", "_userNameSpace", "userNameSpace" })
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b(\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u0000 P2\u00020\u0001:\u0001PB\u008d\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003?\u0006\u0002\u0010\u0011J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\u0010\u00104\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003?\u0006\u0002\u0010\u001cJ\u000b\u00105\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0005H\u00c6\u0003J\t\u00108\u001a\u00020\u0005H\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010;\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0096\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u0003H\u00c6\u0001?\u0006\u0002\u0010@J\u0013\u0010A\u001a\u00020\u000e2\b\u0010B\u001a\u0004\u0018\u00010CH\u00d6\u0003J\b\u0010D\u001a\u00020\u0005H\u0016J\n\u0010E\u001a\u0004\u0018\u00010\u001aH\u0016J\b\u0010F\u001a\u00020\u0005H\u0016J\b\u0010G\u001a\u00020\u0005H\u0016J\t\u0010H\u001a\u00020IH\u00d6\u0001J\u0010\u0010J\u001a\u00020K2\b\u0010L\u001a\u0004\u0018\u00010\u001aJ\u000e\u0010M\u001a\u00020K2\u0006\u0010N\u001a\u00020\u0005J\t\u0010O\u001a\u00020\u0005H\u00d6\u0001R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0005X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0014\"\u0004\b\u0018\u0010\u0016R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e?\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0096\u000e?\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0005X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0014\"\u0004\b!\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0014\"\u0004\b%\u0010\u0016R\u001a\u0010\u0010\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b&\u0010#\"\u0004\b'\u0010(R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0014\"\u0004\b*\u0010\u0016R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0005X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u0014\"\u0004\b,\u0010\u0016R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0005X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0014\"\u0004\b.\u0010\u0016R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0014\"\u0004\b0\u0010\u0016R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0014\"\u0004\b2\u0010\u0016¡§\u0006Q" }, d2 = { "Lio/legado/app/data/entities/HttpTTS;", "Lio/legado/app/data/entities/BaseSource;", "id", "", "name", "", "url", "contentType", "concurrentRate", "loginUrl", "loginUi", "header", "jsLib", "enabledCookieJar", "", "loginCheckJs", "lastUpdateTime", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;J)V", "_userNameSpace", "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "getContentType", "setContentType", "debugLog", "Lio/legado/app/model/DebugLog;", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "getHeader", "setHeader", "getId", "()J", "getJsLib", "setJsLib", "getLastUpdateTime", "setLastUpdateTime", "(J)V", "getLoginCheckJs", "setLoginCheckJs", "getLoginUi", "setLoginUi", "getLoginUrl", "setLoginUrl", "getName", "setName", "getUrl", "setUrl", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/String;J)Lio/legado/app/data/entities/HttpTTS;", "equals", "other", "", "getKey", "getLogger", "getTag", "getUserNameSpace", "hashCode", "", "setLogger", "", "logger", "setUserNameSpace", "nameSpace", "toString", "Companion", "reader-pro" })
public final class HttpTTS implements BaseSource
{
    @NotNull
    public static final Companion Companion;
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
    
    public HttpTTS(final long id, @NotNull final String name, @NotNull final String url, @Nullable final String contentType, @Nullable final String concurrentRate, @Nullable final String loginUrl, @Nullable final String loginUi, @Nullable final String header, @Nullable final String jsLib, @Nullable final Boolean enabledCookieJar, @Nullable final String loginCheckJs, final long lastUpdateTime) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)url, "url");
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
        this._userNameSpace = "";
    }
    
    public final long getId() {
        return this.id;
    }
    
    @NotNull
    public final String getName() {
        return this.name;
    }
    
    public final void setName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.name = <set-?>;
    }
    
    @NotNull
    public final String getUrl() {
        return this.url;
    }
    
    public final void setUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.url = <set-?>;
    }
    
    @Nullable
    public final String getContentType() {
        return this.contentType;
    }
    
    public final void setContentType(@Nullable final String <set-?>) {
        this.contentType = <set-?>;
    }
    
    @Nullable
    public String getConcurrentRate() {
        return this.concurrentRate;
    }
    
    public void setConcurrentRate(@Nullable final String <set-?>) {
        this.concurrentRate = <set-?>;
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
    public String getHeader() {
        return this.header;
    }
    
    public void setHeader(@Nullable final String <set-?>) {
        this.header = <set-?>;
    }
    
    @Nullable
    public final String getJsLib() {
        return this.jsLib;
    }
    
    public final void setJsLib(@Nullable final String <set-?>) {
        this.jsLib = <set-?>;
    }
    
    @Nullable
    public Boolean getEnabledCookieJar() {
        return this.enabledCookieJar;
    }
    
    public void setEnabledCookieJar(@Nullable final Boolean <set-?>) {
        this.enabledCookieJar = <set-?>;
    }
    
    @Nullable
    public final String getLoginCheckJs() {
        return this.loginCheckJs;
    }
    
    public final void setLoginCheckJs(@Nullable final String <set-?>) {
        this.loginCheckJs = <set-?>;
    }
    
    public final long getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    
    public final void setLastUpdateTime(final long <set-?>) {
        this.lastUpdateTime = <set-?>;
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
    
    @NotNull
    public String getTag() {
        return this.name;
    }
    
    @NotNull
    public String getKey() {
        return Intrinsics.stringPlus("httpTts:", (Object)this.id);
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
        return this.getConcurrentRate();
    }
    
    @Nullable
    public final String component6() {
        return this.getLoginUrl();
    }
    
    @Nullable
    public final String component7() {
        return this.getLoginUi();
    }
    
    @Nullable
    public final String component8() {
        return this.getHeader();
    }
    
    @Nullable
    public final String component9() {
        return this.jsLib;
    }
    
    @Nullable
    public final Boolean component10() {
        return this.getEnabledCookieJar();
    }
    
    @Nullable
    public final String component11() {
        return this.loginCheckJs;
    }
    
    public final long component12() {
        return this.lastUpdateTime;
    }
    
    @NotNull
    public final HttpTTS copy(final long id, @NotNull final String name, @NotNull final String url, @Nullable final String contentType, @Nullable final String concurrentRate, @Nullable final String loginUrl, @Nullable final String loginUi, @Nullable final String header, @Nullable final String jsLib, @Nullable final Boolean enabledCookieJar, @Nullable final String loginCheckJs, final long lastUpdateTime) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)url, "url");
        return new HttpTTS(id, name, url, contentType, concurrentRate, loginUrl, loginUi, header, jsLib, enabledCookieJar, loginCheckJs, lastUpdateTime);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("HttpTTS(id=").append(this.id).append(", name=").append(this.name).append(", url=").append(this.url).append(", contentType=").append((Object)this.contentType).append(", concurrentRate=").append((Object)this.getConcurrentRate()).append(", loginUrl=").append((Object)this.getLoginUrl()).append(", loginUi=").append((Object)this.getLoginUi()).append(", header=").append((Object)this.getHeader()).append(", jsLib=").append((Object)this.jsLib).append(", enabledCookieJar=").append(this.getEnabledCookieJar()).append(", loginCheckJs=").append((Object)this.loginCheckJs).append(", lastUpdateTime=");
        sb.append(this.lastUpdateTime).append(')');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = Long.hashCode(this.id);
        result = result * 31 + this.name.hashCode();
        result = result * 31 + this.url.hashCode();
        result = result * 31 + ((this.contentType == null) ? 0 : this.contentType.hashCode());
        result = result * 31 + ((this.getConcurrentRate() == null) ? 0 : this.getConcurrentRate().hashCode());
        result = result * 31 + ((this.getLoginUrl() == null) ? 0 : this.getLoginUrl().hashCode());
        result = result * 31 + ((this.getLoginUi() == null) ? 0 : this.getLoginUi().hashCode());
        result = result * 31 + ((this.getHeader() == null) ? 0 : this.getHeader().hashCode());
        result = result * 31 + ((this.jsLib == null) ? 0 : this.jsLib.hashCode());
        result = result * 31 + ((this.getEnabledCookieJar() == null) ? 0 : this.getEnabledCookieJar().hashCode());
        result = result * 31 + ((this.loginCheckJs == null) ? 0 : this.loginCheckJs.hashCode());
        result = result * 31 + Long.hashCode(this.lastUpdateTime);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpTTS)) {
            return false;
        }
        final HttpTTS httpTTS = (HttpTTS)other;
        return this.id == httpTTS.id && Intrinsics.areEqual((Object)this.name, (Object)httpTTS.name) && Intrinsics.areEqual((Object)this.url, (Object)httpTTS.url) && Intrinsics.areEqual((Object)this.contentType, (Object)httpTTS.contentType) && Intrinsics.areEqual((Object)this.getConcurrentRate(), (Object)httpTTS.getConcurrentRate()) && Intrinsics.areEqual((Object)this.getLoginUrl(), (Object)httpTTS.getLoginUrl()) && Intrinsics.areEqual((Object)this.getLoginUi(), (Object)httpTTS.getLoginUi()) && Intrinsics.areEqual((Object)this.getHeader(), (Object)httpTTS.getHeader()) && Intrinsics.areEqual((Object)this.jsLib, (Object)httpTTS.jsLib) && Intrinsics.areEqual((Object)this.getEnabledCookieJar(), (Object)httpTTS.getEnabledCookieJar()) && Intrinsics.areEqual((Object)this.loginCheckJs, (Object)httpTTS.loginCheckJs) && this.lastUpdateTime == httpTTS.lastUpdateTime;
    }
    
    public HttpTTS() {
        this(0L, null, null, null, null, null, null, null, null, null, null, 0L, 4095, null);
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\b\u0010\tJ4\u0010\n\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u00050\u000bj\b\u0012\u0004\u0012\u00020\u0005`\f0\u00042\u0006\u0010\r\u001a\u00020\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u000e\u0010\tJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0010\u001a\u00020\u0011\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u0012\u0010\u0013\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001¡§\u0006\u0014" }, d2 = { "Lio/legado/app/data/entities/HttpTTS$Companion;", "", "()V", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/HttpTTS;", "json", "", "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "jsonArray", "fromJsonArray-IoAF18A", "fromJsonDoc", "doc", "Lcom/jayway/jsonpath/DocumentContext;", "fromJsonDoc-IoAF18A", "(Lcom/jayway/jsonpath/DocumentContext;)Ljava/lang/Object;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final Object fromJsonDoc-IoAF18A(@NotNull final DocumentContext doc) {
            Intrinsics.checkNotNullParameter((Object)doc, "doc");
            Object o2;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Object loginUi = doc.read("$.loginUi", new Predicate[0]);
                final Long long1 = JsonExtensionsKt.readLong((ReadContext)doc, "$.id");
                final long n2 = (long1 == null) ? System.currentTimeMillis() : long1;
                final String string = JsonExtensionsKt.readString((ReadContext)doc, "$.name");
                Intrinsics.checkNotNull((Object)string);
                final String string2 = JsonExtensionsKt.readString((ReadContext)doc, "$.url");
                Intrinsics.checkNotNull((Object)string2);
                final String string3 = JsonExtensionsKt.readString((ReadContext)doc, "$.contentType");
                final String string4 = JsonExtensionsKt.readString((ReadContext)doc, "$.concurrentRate");
                final String string5 = JsonExtensionsKt.readString((ReadContext)doc, "$.loginUrl");
                String json;
                if (loginUi instanceof List) {
                    json = GsonExtensionsKt.getGSON().toJson(loginUi);
                }
                else {
                    final Object o = loginUi;
                    json = ((o == null) ? null : o.toString());
                }
                o2 = Result.constructor-impl((Object)new HttpTTS(n2, string, string2, string3, string4, string5, json, JsonExtensionsKt.readString((ReadContext)doc, "$.header"), null, null, JsonExtensionsKt.readString((ReadContext)doc, "$.loginCheckJs"), 0L, 2816, null));
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o2 = Result.constructor-impl(ResultKt.createFailure(t));
            }
            return o2;
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
                    final Companion companion2 = HttpTTS.Companion;
                    Intrinsics.checkNotNullExpressionValue((Object)jsonItem, "jsonItem");
                    final Object fromJsonDoc-IoAF18A = companion2.fromJsonDoc-IoAF18A(jsonItem);
                    ResultKt.throwOnFailure(fromJsonDoc-IoAF18A);
                    final HttpTTS source = (HttpTTS)fromJsonDoc-IoAF18A;
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
