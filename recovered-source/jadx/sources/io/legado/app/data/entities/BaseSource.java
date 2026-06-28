package io.legado.app.data.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.script.Bindings;
import com.script.SimpleBindings;
import io.legado.app.constant.AppConst;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.help.CacheManager;
import io.legado.app.help.JsExtensions;
import io.legado.app.help.http.CookieStore;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.Base64;
import io.legado.app.utils.EncoderUtils;
import io.legado.app.utils.GsonExtensionsKt;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;

/* JADX INFO: compiled from: BaseSource.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/BaseSource.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u000f\bf\u0018\u00002\u00020\u0001J-\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u00032\u0019\b\u0002\u0010\u001a\u001a\u0013\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d0\u001b¢\u0006\u0002\b\u001eH\u0016J.\u0010\u001f\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030 j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`!2\b\b\u0002\u0010\"\u001a\u00020\tH\u0016J\b\u0010#\u001a\u00020\u0003H&J\n\u0010$\u001a\u0004\u0018\u00010\u0003H\u0016J\u0016\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010&H\u0016J\n\u0010'\u001a\u0004\u0018\u00010\u0003H\u0016J\u0016\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010&H\u0016J\n\u0010)\u001a\u0004\u0018\u00010\u0003H\u0016J\n\u0010*\u001a\u0004\u0018\u00010\u0000H\u0016J\b\u0010+\u001a\u00020\u0003H&J\n\u0010,\u001a\u0004\u0018\u00010\u0003H\u0016J\b\u0010-\u001a\u00020\u001dH\u0016J\u0010\u0010.\u001a\u00020\u001d2\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\u0010\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\u0003H\u0016J\b\u00101\u001a\u00020\u001dH\u0016J\b\u00102\u001a\u00020\u001dH\u0016J\u0012\u00103\u001a\u00020\u001d2\b\u00104\u001a\u0004\u0018\u00010\u0003H\u0016R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u0004\u0018\u00010\tX¦\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0012\u0010\u0005\"\u0004\b\u0013\u0010\u0007R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0015\u0010\u0005\"\u0004\b\u0016\u0010\u0007¨\u00065"}, d2 = {"Lio/legado/app/data/entities/BaseSource;", "Lio/legado/app/help/JsExtensions;", "concurrentRate", PackageDocumentBase.PREFIX_OPF, "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "enabledCookieJar", PackageDocumentBase.PREFIX_OPF, "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "header", "getHeader", "setHeader", "loginUi", "getLoginUi", "setLoginUi", "loginUrl", "getLoginUrl", "setLoginUrl", "evalJS", PackageDocumentBase.PREFIX_OPF, "jsStr", "bindingsConfig", "Lkotlin/Function1;", "Lcom/script/SimpleBindings;", PackageDocumentBase.PREFIX_OPF, "Lkotlin/ExtensionFunctionType;", "getHeaderMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "hasLoginHeader", "getKey", "getLoginHeader", "getLoginHeaderMap", PackageDocumentBase.PREFIX_OPF, "getLoginInfo", "getLoginInfoMap", "getLoginJs", "getSource", "getTag", "getVariable", "login", "putLoginHeader", "putLoginInfo", "info", "removeLoginHeader", "removeLoginInfo", "setVariable", "variable", "reader-pro"})
public interface BaseSource extends JsExtensions {
    @Nullable
    String getConcurrentRate();

    void setConcurrentRate(@Nullable String str);

    @Nullable
    String getLoginUrl();

    void setLoginUrl(@Nullable String str);

    @Nullable
    String getLoginUi();

    void setLoginUi(@Nullable String str);

    @Nullable
    String getHeader();

    void setHeader(@Nullable String str);

    @Nullable
    Boolean getEnabledCookieJar();

    void setEnabledCookieJar(@Nullable Boolean bool);

    @NotNull
    String getTag();

    @NotNull
    String getKey();

    @Override // io.legado.app.help.JsExtensions
    @Nullable
    BaseSource getSource();

    @Nullable
    String getLoginJs();

    void login();

    @NotNull
    HashMap<String, String> getHeaderMap(boolean hasLoginHeader);

    @Nullable
    String getLoginHeader();

    @Nullable
    Map<String, String> getLoginHeaderMap();

    void putLoginHeader(@NotNull String header);

    void removeLoginHeader();

    @Nullable
    String getLoginInfo();

    @Nullable
    Map<String, String> getLoginInfoMap();

    boolean putLoginInfo(@NotNull String info);

    void removeLoginInfo();

    void setVariable(@Nullable String variable);

    @Nullable
    String getVariable();

    @Nullable
    Object evalJS(@NotNull String jsStr, @NotNull Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception;

    /* JADX INFO: compiled from: BaseSource.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/BaseSource$DefaultImpls.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public static final class DefaultImpls {
        @Nullable
        public static byte[] aesBase64DecodeToByteArray(@NotNull BaseSource baseSource, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesBase64DecodeToByteArray(baseSource, str, key, transformation, iv);
        }

        @Nullable
        public static String aesBase64DecodeToString(@NotNull BaseSource baseSource, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesBase64DecodeToString(baseSource, str, key, transformation, iv);
        }

        @Nullable
        public static String aesDecodeArgsBase64Str(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesDecodeArgsBase64Str(baseSource, data, key, mode, padding, iv);
        }

        @Nullable
        public static byte[] aesDecodeToByteArray(@NotNull BaseSource baseSource, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesDecodeToByteArray(baseSource, str, key, transformation, iv);
        }

        @Nullable
        public static String aesDecodeToString(@NotNull BaseSource baseSource, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesDecodeToString(baseSource, str, key, transformation, iv);
        }

        @Nullable
        public static String aesEncodeArgsBase64Str(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeArgsBase64Str(baseSource, data, key, mode, padding, iv);
        }

        @Nullable
        public static byte[] aesEncodeToBase64ByteArray(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToBase64ByteArray(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String aesEncodeToBase64String(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToBase64String(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static byte[] aesEncodeToByteArray(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToByteArray(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String aesEncodeToString(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToString(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String ajax(@NotNull BaseSource baseSource, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return JsExtensions.DefaultImpls.ajax(baseSource, urlStr);
        }

        @NotNull
        public static StrResponse[] ajaxAll(@NotNull BaseSource baseSource, @NotNull String[] urlList) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlList, "urlList");
            return JsExtensions.DefaultImpls.ajaxAll(baseSource, urlList);
        }

        @NotNull
        public static String androidId(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.androidId(baseSource);
        }

        @NotNull
        public static String base64Decode(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.base64Decode(baseSource, str);
        }

        @NotNull
        public static String base64Decode(@NotNull BaseSource baseSource, @NotNull String str, int flags) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.base64Decode(baseSource, str, flags);
        }

        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull BaseSource baseSource, @Nullable String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.base64DecodeToByteArray(baseSource, str);
        }

        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull BaseSource baseSource, @Nullable String str, int flags) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.base64DecodeToByteArray(baseSource, str, flags);
        }

        @Nullable
        public static String base64Encode(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.base64Encode(baseSource, str);
        }

        @Nullable
        public static String base64Encode(@NotNull BaseSource baseSource, @NotNull String str, int flags) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.base64Encode(baseSource, str, flags);
        }

        @Nullable
        public static String cacheFile(@NotNull BaseSource baseSource, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return JsExtensions.DefaultImpls.cacheFile(baseSource, urlStr);
        }

        @Nullable
        public static String cacheFile(@NotNull BaseSource baseSource, @NotNull String urlStr, int saveTime) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return JsExtensions.DefaultImpls.cacheFile(baseSource, urlStr, saveTime);
        }

        @NotNull
        public static StrResponse connect(@NotNull BaseSource baseSource, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return JsExtensions.DefaultImpls.connect(baseSource, urlStr);
        }

        @NotNull
        public static StrResponse connect(@NotNull BaseSource baseSource, @NotNull String urlStr, @Nullable String header) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return JsExtensions.DefaultImpls.connect(baseSource, urlStr, header);
        }

        public static void deleteFile(@NotNull BaseSource baseSource, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            JsExtensions.DefaultImpls.deleteFile(baseSource, path);
        }

        @Nullable
        public static String desBase64DecodeToString(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.desBase64DecodeToString(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String desDecodeToString(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.desDecodeToString(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String desEncodeToBase64String(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.desEncodeToBase64String(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String desEncodeToString(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.desEncodeToString(baseSource, data, key, transformation, iv);
        }

        @Nullable
        public static String digestBase64Str(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String algorithm) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(algorithm, "algorithm");
            return JsExtensions.DefaultImpls.digestBase64Str(baseSource, data, algorithm);
        }

        @Nullable
        public static String digestHex(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String algorithm) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(algorithm, "algorithm");
            return JsExtensions.DefaultImpls.digestHex(baseSource, data, algorithm);
        }

        @NotNull
        public static String downloadFile(@NotNull BaseSource baseSource, @NotNull String content, @NotNull String url) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(content, "content");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            return JsExtensions.DefaultImpls.downloadFile(baseSource, content, url);
        }

        @NotNull
        public static String encodeURI(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.encodeURI(baseSource, str);
        }

        @NotNull
        public static String encodeURI(@NotNull BaseSource baseSource, @NotNull String str, @NotNull String enc) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(enc, "enc");
            return JsExtensions.DefaultImpls.encodeURI(baseSource, str, enc);
        }

        @NotNull
        public static Connection.Response get(@NotNull BaseSource baseSource, @NotNull String urlStr, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            Intrinsics.checkNotNullParameter(headers, "headers");
            return JsExtensions.DefaultImpls.get(baseSource, urlStr, headers);
        }

        @NotNull
        public static String getCookie(@NotNull BaseSource baseSource, @NotNull String tag, @Nullable String key) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(tag, "tag");
            return JsExtensions.DefaultImpls.getCookie(baseSource, tag, key);
        }

        @NotNull
        public static File getFile(@NotNull BaseSource baseSource, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            return JsExtensions.DefaultImpls.getFile(baseSource, path);
        }

        @NotNull
        public static String getTxtInFolder(@NotNull BaseSource baseSource, @NotNull String unzipPath) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(unzipPath, "unzipPath");
            return JsExtensions.DefaultImpls.getTxtInFolder(baseSource, unzipPath);
        }

        @Nullable
        public static byte[] getZipByteArrayContent(@NotNull BaseSource baseSource, @NotNull String url, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullParameter(path, "path");
            return JsExtensions.DefaultImpls.getZipByteArrayContent(baseSource, url, path);
        }

        @NotNull
        public static String getZipStringContent(@NotNull BaseSource baseSource, @NotNull String url, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullParameter(path, "path");
            return JsExtensions.DefaultImpls.getZipStringContent(baseSource, url, path);
        }

        @NotNull
        public static String getZipStringContent(@NotNull BaseSource baseSource, @NotNull String url, @NotNull String path, @NotNull String charsetName) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullParameter(path, "path");
            Intrinsics.checkNotNullParameter(charsetName, "charsetName");
            return JsExtensions.DefaultImpls.getZipStringContent(baseSource, url, path, charsetName);
        }

        @NotNull
        public static Connection.Response head(@NotNull BaseSource baseSource, @NotNull String urlStr, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            Intrinsics.checkNotNullParameter(headers, "headers");
            return JsExtensions.DefaultImpls.head(baseSource, urlStr, headers);
        }

        @NotNull
        public static String htmlFormat(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.htmlFormat(baseSource, str);
        }

        @NotNull
        public static String importScript(@NotNull BaseSource baseSource, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            return JsExtensions.DefaultImpls.importScript(baseSource, path);
        }

        @NotNull
        public static String log(@NotNull BaseSource baseSource, @NotNull String msg) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(msg, "msg");
            return JsExtensions.DefaultImpls.log(baseSource, msg);
        }

        public static void logType(@NotNull BaseSource baseSource, @Nullable Object any) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            JsExtensions.DefaultImpls.logType(baseSource, any);
        }

        public static void longToast(@NotNull BaseSource baseSource, @Nullable Object msg) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            JsExtensions.DefaultImpls.longToast(baseSource, msg);
        }

        @NotNull
        public static String md5Encode(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.md5Encode(baseSource, str);
        }

        @NotNull
        public static String md5Encode16(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.md5Encode16(baseSource, str);
        }

        @NotNull
        public static Connection.Response post(@NotNull BaseSource baseSource, @NotNull String urlStr, @NotNull String body, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            Intrinsics.checkNotNullParameter(body, NCXDocumentV3.XHTMLTgs.body);
            Intrinsics.checkNotNullParameter(headers, "headers");
            return JsExtensions.DefaultImpls.post(baseSource, urlStr, body, headers);
        }

        @Nullable
        public static QueryTTF queryBase64TTF(@NotNull BaseSource baseSource, @Nullable String base64) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.queryBase64TTF(baseSource, base64);
        }

        @Nullable
        public static QueryTTF queryTTF(@NotNull BaseSource baseSource, @Nullable String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.queryTTF(baseSource, str);
        }

        @NotNull
        public static String randomUUID(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.randomUUID(baseSource);
        }

        @Nullable
        public static byte[] readFile(@NotNull BaseSource baseSource, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            return JsExtensions.DefaultImpls.readFile(baseSource, path);
        }

        @NotNull
        public static String readTxtFile(@NotNull BaseSource baseSource, @NotNull String path) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            return JsExtensions.DefaultImpls.readTxtFile(baseSource, path);
        }

        @NotNull
        public static String readTxtFile(@NotNull BaseSource baseSource, @NotNull String path, @NotNull String charsetName) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            Intrinsics.checkNotNullParameter(charsetName, "charsetName");
            return JsExtensions.DefaultImpls.readTxtFile(baseSource, path, charsetName);
        }

        @NotNull
        public static String replaceFont(@NotNull BaseSource baseSource, @NotNull String text, @Nullable QueryTTF font1, @Nullable QueryTTF font2) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(text, NCXDocumentV2.NCXTags.text);
            return JsExtensions.DefaultImpls.replaceFont(baseSource, text, font1, font2);
        }

        @NotNull
        public static String timeFormat(@NotNull BaseSource baseSource, long time) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.timeFormat(baseSource, time);
        }

        @Nullable
        public static String timeFormatUTC(@NotNull BaseSource baseSource, long time, @NotNull String format, int sh) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(format, PackageDocumentBase.DCTags.format);
            return JsExtensions.DefaultImpls.timeFormatUTC(baseSource, time, format, sh);
        }

        public static void toast(@NotNull BaseSource baseSource, @Nullable Object msg) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            JsExtensions.DefaultImpls.toast(baseSource, msg);
        }

        @Nullable
        public static String tripleDESDecodeArgsBase64Str(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESDecodeArgsBase64Str(baseSource, data, key, mode, padding, iv);
        }

        @Nullable
        public static String tripleDESDecodeStr(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESDecodeStr(baseSource, data, key, mode, padding, iv);
        }

        @Nullable
        public static String tripleDESEncodeArgsBase64Str(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESEncodeArgsBase64Str(baseSource, data, key, mode, padding, iv);
        }

        @Nullable
        public static String tripleDESEncodeBase64Str(@NotNull BaseSource baseSource, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESEncodeBase64Str(baseSource, data, key, mode, padding, iv);
        }

        @NotNull
        public static String unzipFile(@NotNull BaseSource baseSource, @NotNull String zipPath) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(zipPath, "zipPath");
            return JsExtensions.DefaultImpls.unzipFile(baseSource, zipPath);
        }

        @NotNull
        public static String utf8ToGbk(@NotNull BaseSource baseSource, @NotNull String str) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return JsExtensions.DefaultImpls.utf8ToGbk(baseSource, str);
        }

        @Nullable
        public static String webView(@NotNull BaseSource baseSource, @Nullable String html, @Nullable String url, @Nullable String js) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return JsExtensions.DefaultImpls.webView(baseSource, html, url, js);
        }

        @Nullable
        public static BaseSource getSource(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            return baseSource;
        }

        @Nullable
        public static String getLoginJs(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            String loginJs = baseSource.getLoginUrl();
            if (loginJs == null) {
                return null;
            }
            if (StringsKt.startsWith$default(loginJs, "@js:", false, 2, (Object) null)) {
                String strSubstring = loginJs.substring(4);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                return strSubstring;
            }
            if (StringsKt.startsWith$default(loginJs, "<js>", false, 2, (Object) null)) {
                String strSubstring2 = loginJs.substring(4, StringsKt.lastIndexOf$default(loginJs, "<", 0, false, 6, (Object) null));
                Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return strSubstring2;
            }
            return loginJs;
        }

        public static void login(@NotNull BaseSource baseSource) throws Exception {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            String it = baseSource.getLoginJs();
            if (it != null) {
                evalJS$default(baseSource, it, null, 2, null);
            }
        }

        /* JADX WARN: Type inference failed for: r2v8, types: [io.legado.app.data.entities.BaseSource$DefaultImpls$getHeaderMap$lambda-4$lambda-2$$inlined$fromJsonObject$1] */
        @NotNull
        public static HashMap<String, String> getHeaderMap(@NotNull BaseSource baseSource, boolean hasLoginHeader) {
            Map<String, String> loginHeaderMap;
            String strValueOf;
            Object obj;
            Intrinsics.checkNotNullParameter(baseSource, "this");
            HashMap<String, String> map = new HashMap<>();
            map.put(AppConst.UA_NAME, AppConst.INSTANCE.getUserAgent());
            String it = baseSource.getHeader();
            if (it != null) {
                Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
                if (StringsKt.startsWith(it, "@js:", true)) {
                    String strSubstring = it.substring(4);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    strValueOf = String.valueOf(evalJS$default(baseSource, strSubstring, null, 2, null));
                } else if (StringsKt.startsWith(it, "<js>", true)) {
                    String strSubstring2 = it.substring(4, StringsKt.lastIndexOf$default(it, "<", 0, false, 6, (Object) null));
                    Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    strValueOf = String.valueOf(evalJS$default(baseSource, strSubstring2, null, 2, null));
                } else {
                    strValueOf = it;
                }
                String json$iv = strValueOf;
                try {
                    Result.Companion companion = Result.Companion;
                    Type type = new TypeToken<Map<String, ? extends String>>() { // from class: io.legado.app.data.entities.BaseSource$DefaultImpls$getHeaderMap$lambda-4$lambda-2$$inlined$fromJsonObject$1
                    }.getType();
                    Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                    Object objFromJson = $this$fromJsonObject$iv.fromJson(json$iv, type);
                    if (!(objFromJson instanceof Map)) {
                        objFromJson = null;
                    }
                    obj = Result.constructor-impl((Map) objFromJson);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.Companion;
                    obj = Result.constructor-impl(ResultKt.createFailure(th));
                }
                Object obj2 = obj;
                Map<? extends String, ? extends String> map2 = (Map) (Result.isFailure-impl(obj2) ? null : obj2);
                if (map2 != null) {
                    map.putAll(map2);
                }
            }
            if (hasLoginHeader && (loginHeaderMap = baseSource.getLoginHeaderMap()) != null) {
                map.putAll(loginHeaderMap);
            }
            return map;
        }

        public static /* synthetic */ HashMap getHeaderMap$default(BaseSource baseSource, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getHeaderMap");
            }
            if ((i & 1) != 0) {
                z = false;
            }
            return baseSource.getHeaderMap(z);
        }

        @Nullable
        public static String getLoginHeader(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
            return cacheInstance.get(Intrinsics.stringPlus("loginHeader_", baseSource.getKey()));
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [io.legado.app.data.entities.BaseSource$DefaultImpls$getLoginHeaderMap$$inlined$fromJsonObject$1] */
        @Nullable
        public static Map<String, String> getLoginHeaderMap(@NotNull BaseSource baseSource) {
            Object obj;
            Intrinsics.checkNotNullParameter(baseSource, "this");
            String cache = baseSource.getLoginHeader();
            if (cache == null) {
                return null;
            }
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new TypeToken<Map<String, ? extends String>>() { // from class: io.legado.app.data.entities.BaseSource$DefaultImpls$getLoginHeaderMap$$inlined$fromJsonObject$1
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(cache, type);
                if (!(objFromJson instanceof Map)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((Map) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (Map) (Result.isFailure-impl(obj2) ? null : obj2);
        }

        public static void putLoginHeader(@NotNull BaseSource baseSource, @NotNull String header) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(header, "header");
            CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
            CacheManager.put$default(cacheInstance, Intrinsics.stringPlus("loginHeader_", baseSource.getKey()), header, 0, 4, null);
        }

        public static void removeLoginHeader(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
            cacheInstance.delete(Intrinsics.stringPlus("loginHeader_", baseSource.getKey()));
        }

        @Nullable
        public static String getLoginInfo(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            try {
                byte[] key = StringsKt.encodeToByteArray$default(AppConst.INSTANCE.getUserAgent(), 0, 8, false, 4, (Object) null);
                CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
                String cache = cacheInstance.get(Intrinsics.stringPlus("userInfo_", baseSource.getKey()));
                if (cache == null) {
                    return null;
                }
                String strBase64Decode = EncoderUtils.INSTANCE.base64Decode(cache, 0);
                Charset charset = Charsets.UTF_8;
                if (strBase64Decode == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                byte[] encodeBytes = strBase64Decode.getBytes(charset);
                Intrinsics.checkNotNullExpressionValue(encodeBytes, "(this as java.lang.String).getBytes(charset)");
                byte[] decodeBytes = EncoderUtils.decryptAES$default(EncoderUtils.INSTANCE, encodeBytes, key, null, null, 12, null);
                if (decodeBytes != null) {
                    return new String(decodeBytes, Charsets.UTF_8);
                }
                return null;
            } catch (Exception e) {
                baseSource.log(Intrinsics.stringPlus("获取登陆信息出错 ", e.getLocalizedMessage()));
                return null;
            }
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [io.legado.app.data.entities.BaseSource$DefaultImpls$getLoginInfoMap$$inlined$fromJsonObject$1] */
        @Nullable
        public static Map<String, String> getLoginInfoMap(@NotNull BaseSource baseSource) {
            Object obj;
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            String json$iv = baseSource.getLoginInfo();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new TypeToken<Map<String, ? extends String>>() { // from class: io.legado.app.data.entities.BaseSource$DefaultImpls$getLoginInfoMap$$inlined$fromJsonObject$1
                }.getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json$iv, type);
                if (!(objFromJson instanceof Map)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((Map) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return (Map) (Result.isFailure-impl(obj2) ? null : obj2);
        }

        public static boolean putLoginInfo(@NotNull BaseSource baseSource, @NotNull String info) {
            boolean z;
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(info, "info");
            try {
                byte[] key = StringsKt.encodeToByteArray$default(AppConst.INSTANCE.getUserAgent(), 0, 8, false, 4, (Object) null);
                EncoderUtils encoderUtils = EncoderUtils.INSTANCE;
                byte[] bytes = info.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
                byte[] encodeBytes = EncoderUtils.encryptAES$default(encoderUtils, bytes, key, null, null, 12, null);
                String encodeStr = Base64.encodeToString(encodeBytes, 0);
                CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
                String strStringPlus = Intrinsics.stringPlus("userInfo_", baseSource.getKey());
                Intrinsics.checkNotNullExpressionValue(encodeStr, "encodeStr");
                CacheManager.put$default(cacheInstance, strStringPlus, encodeStr, 0, 4, null);
                z = true;
            } catch (Exception e) {
                baseSource.log(Intrinsics.stringPlus("保存登陆信息出错 ", e.getLocalizedMessage()));
                z = false;
            }
            return z;
        }

        public static void removeLoginInfo(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
            cacheInstance.delete(Intrinsics.stringPlus("userInfo_", baseSource.getKey()));
        }

        public static void setVariable(@NotNull BaseSource baseSource, @Nullable String variable) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
            if (variable != null) {
                CacheManager.put$default(cacheInstance, Intrinsics.stringPlus("sourceVariable_", baseSource.getKey()), variable, 0, 4, null);
            } else {
                cacheInstance.delete(Intrinsics.stringPlus("sourceVariable_", baseSource.getKey()));
            }
        }

        @Nullable
        public static String getVariable(@NotNull BaseSource baseSource) {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            CacheManager cacheInstance = new CacheManager(baseSource.getUserNameSpace());
            return cacheInstance.get(Intrinsics.stringPlus("sourceVariable_", baseSource.getKey()));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Object evalJS$default(BaseSource baseSource, String str, Function1 function1, int i, Object obj) throws Exception {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: evalJS");
            }
            if ((i & 2) != 0) {
                function1 = new Function1<SimpleBindings, Unit>() { // from class: io.legado.app.data.entities.BaseSource.evalJS.1
                    public final void invoke(@NotNull SimpleBindings $this$null) {
                        Intrinsics.checkNotNullParameter($this$null, "$this$null");
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                        invoke((SimpleBindings) p1);
                        return Unit.INSTANCE;
                    }
                };
            }
            return baseSource.evalJS(str, function1);
        }

        @Nullable
        public static Object evalJS(@NotNull BaseSource baseSource, @NotNull String jsStr, @NotNull Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception {
            Intrinsics.checkNotNullParameter(baseSource, "this");
            Intrinsics.checkNotNullParameter(jsStr, "jsStr");
            Intrinsics.checkNotNullParameter(bindingsConfig, "bindingsConfig");
            Bindings simpleBindings = new SimpleBindings();
            bindingsConfig.invoke(simpleBindings);
            ((Map) simpleBindings).put("java", baseSource);
            ((Map) simpleBindings).put(PackageDocumentBase.DCTags.source, baseSource);
            ((Map) simpleBindings).put("baseUrl", baseSource.getKey());
            ((Map) simpleBindings).put("cookie", new CookieStore(baseSource.getUserNameSpace()));
            ((Map) simpleBindings).put("cache", new CacheManager(baseSource.getUserNameSpace()));
            return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, simpleBindings);
        }
    }
}
