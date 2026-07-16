// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.DESede;
import cn.hutool.crypto.symmetric.AES;
import io.legado.app.utils.LogUtilsKt;
import java.util.UUID;
import io.legado.app.model.Debug;
import io.legado.app.utils.StringExtensionsKt;
import kotlin.jvm.internal.Ref$ObjectRef;
import io.legado.app.utils.ZipUtils;
import io.legado.app.utils.EncodingDetect;
import io.legado.app.adapters.ReaderAdapterHelper;
import java.net.URL;
import io.legado.app.utils.HtmlFormatter;
import java.net.URLEncoder;
import kotlin.text.Charsets;
import java.nio.charset.Charset;
import io.legado.app.constant.AppConst;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.SimpleTimeZone;
import io.legado.app.utils.Base64;
import io.legado.app.utils.EncoderUtils;
import io.legado.app.utils.NetworkUtils;
import org.jsoup.Connection$Method;
import io.legado.app.help.http.SSLHelper;
import org.jsoup.Jsoup;
import kotlin.io.FilesKt;
import io.legado.app.utils.StringUtils;
import io.legado.app.utils.MD5Utils;
import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.help.http.CookieStore;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.utils.FileUtils;
import kotlin.text.StringsKt;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.model.analyzeRule.QueryTTF;
import java.io.File;
import org.jsoup.Connection$Response;
import java.util.Map;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.legado.app.data.entities.BaseSource;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\u000f\bf\u0018\u00002\u00020\u0001J*\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u000e\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u000f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0013\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J#\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u00182\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018H\u0016?\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u0005H\u0016J\u0010\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0014\u0010 \u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u001c\u0010 \u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0012\u0010!\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u001a\u0010!\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0012\u0010\"\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J\u001c\u0010\"\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010#\u001a\u00020\u001fH\u0016J\u0010\u0010$\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J\u001a\u0010$\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u00052\b\u0010%\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0005H\u0016J*\u0010)\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010*\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010+\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010,\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u001a\u0010-\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u0005H\u0016J\u001a\u0010/\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u0005H\u0016J\u0018\u00100\u001a\u00020\u00052\u0006\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u0005H\u0016J\u0010\u00103\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u00103\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u00104\u001a\u00020\u0005H\u0016J$\u00105\u001a\u0002062\u0006\u0010\u0016\u001a\u00020\u00052\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000508H\u0016J\u001c\u00109\u001a\u00020\u00052\u0006\u0010:\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010;\u001a\u00020<2\u0006\u0010(\u001a\u00020\u0005H\u0016J\n\u0010=\u001a\u0004\u0018\u00010>H&J\n\u0010?\u001a\u0004\u0018\u00010@H&J\u0010\u0010A\u001a\u00020\u00052\u0006\u0010B\u001a\u00020\u0005H\u0016J\b\u0010C\u001a\u00020\u0005H&J\u001a\u0010D\u001a\u0004\u0018\u00010\u00032\u0006\u00102\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0018\u0010E\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J \u0010E\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u00052\u0006\u0010F\u001a\u00020\u0005H\u0016J$\u0010G\u001a\u0002062\u0006\u0010\u0016\u001a\u00020\u00052\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000508H\u0016J\u0010\u0010H\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010I\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0010\u0010J\u001a\u00020\u00052\u0006\u0010K\u001a\u00020\u0005H\u0016J\u0012\u0010L\u001a\u00020'2\b\u0010M\u001a\u0004\u0018\u00010\u0001H\u0016J\u0012\u0010N\u001a\u00020'2\b\u0010K\u001a\u0004\u0018\u00010\u0001H\u0016J\u0010\u0010O\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010P\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J,\u0010Q\u001a\u0002062\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010R\u001a\u00020\u00052\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000508H\u0016J\u0014\u0010S\u001a\u0004\u0018\u00010T2\b\u0010U\u001a\u0004\u0018\u00010\u0005H\u0016J\u0014\u0010V\u001a\u0004\u0018\u00010T2\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010W\u001a\u00020\u0005H\u0016J\u0012\u0010X\u001a\u0004\u0018\u00010\u00032\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0010\u0010Y\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0018\u0010Y\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u00052\u0006\u0010F\u001a\u00020\u0005H\u0016J$\u0010Z\u001a\u00020\u00052\u0006\u0010[\u001a\u00020\u00052\b\u0010\\\u001a\u0004\u0018\u00010T2\b\u0010]\u001a\u0004\u0018\u00010TH\u0016J\u0010\u0010^\u001a\u00020\u00052\u0006\u0010_\u001a\u00020`H\u0016J\"\u0010a\u001a\u0004\u0018\u00010\u00052\u0006\u0010_\u001a\u00020`2\u0006\u0010b\u001a\u00020\u00052\u0006\u0010c\u001a\u00020\u001fH\u0016J\u0012\u0010d\u001a\u00020'2\b\u0010K\u001a\u0004\u0018\u00010\u0001H\u0016J2\u0010e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010g\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010h\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0010\u0010i\u001a\u00020\u00052\u0006\u0010j\u001a\u00020\u0005H\u0016J\u0010\u0010k\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010l\u001a\u0004\u0018\u00010\u00052\b\u0010m\u001a\u0004\u0018\u00010\u00052\b\u00102\u001a\u0004\u0018\u00010\u00052\b\u0010n\u001a\u0004\u0018\u00010\u0005H\u0016¡§\u0006o" }, d2 = { "Lio/legado/app/help/JsExtensions;", "", "aesBase64DecodeToByteArray", "", "str", "", "key", "transformation", "iv", "aesBase64DecodeToString", "aesDecodeArgsBase64Str", "data", "mode", "padding", "aesDecodeToByteArray", "aesDecodeToString", "aesEncodeArgsBase64Str", "aesEncodeToBase64ByteArray", "aesEncodeToBase64String", "aesEncodeToByteArray", "aesEncodeToString", "ajax", "urlStr", "ajaxAll", "", "Lio/legado/app/help/http/StrResponse;", "urlList", "([Ljava/lang/String;)[Lio/legado/app/help/http/StrResponse;", "androidId", "base64Decode", "flags", "", "base64DecodeToByteArray", "base64Encode", "cacheFile", "saveTime", "connect", "header", "deleteFile", "", "path", "desBase64DecodeToString", "desDecodeToString", "desEncodeToBase64String", "desEncodeToString", "digestBase64Str", "algorithm", "digestHex", "downloadFile", "content", "url", "encodeURI", "enc", "get", "Lorg/jsoup/Connection$Response;", "headers", "", "getCookie", "tag", "getFile", "Ljava/io/File;", "getLogger", "Lio/legado/app/model/DebugLog;", "getSource", "Lio/legado/app/data/entities/BaseSource;", "getTxtInFolder", "unzipPath", "getUserNameSpace", "getZipByteArrayContent", "getZipStringContent", "charsetName", "head", "htmlFormat", "importScript", "log", "msg", "logType", "any", "longToast", "md5Encode", "md5Encode16", "post", "body", "queryBase64TTF", "Lio/legado/app/model/analyzeRule/QueryTTF;", "base64", "queryTTF", "randomUUID", "readFile", "readTxtFile", "replaceFont", "text", "font1", "font2", "timeFormat", "time", "", "timeFormatUTC", "format", "sh", "toast", "tripleDESDecodeArgsBase64Str", "tripleDESDecodeStr", "tripleDESEncodeArgsBase64Str", "tripleDESEncodeBase64Str", "unzipFile", "zipPath", "utf8ToGbk", "webView", "html", "js", "reader-pro" })
public interface JsExtensions
{
    @Nullable
    BaseSource getSource();
    
    @NotNull
    String getUserNameSpace();
    
    @Nullable
    DebugLog getLogger();
    
    @Nullable
    String ajax(@NotNull final String urlStr);
    
    @NotNull
    StrResponse[] ajaxAll(@NotNull final String[] urlList);
    
    @NotNull
    StrResponse connect(@NotNull final String urlStr);
    
    @NotNull
    StrResponse connect(@NotNull final String urlStr, @Nullable final String header);
    
    @Nullable
    String webView(@Nullable final String html, @Nullable final String url, @Nullable final String js);
    
    @NotNull
    String importScript(@NotNull final String path);
    
    @Nullable
    String cacheFile(@NotNull final String urlStr);
    
    @Nullable
    String cacheFile(@NotNull final String urlStr, final int saveTime);
    
    @NotNull
    String getCookie(@NotNull final String tag, @Nullable final String key);
    
    @NotNull
    String downloadFile(@NotNull final String content, @NotNull final String url);
    
    @NotNull
    Connection$Response get(@NotNull final String urlStr, @NotNull final Map<String, String> headers);
    
    @NotNull
    Connection$Response head(@NotNull final String urlStr, @NotNull final Map<String, String> headers);
    
    @NotNull
    Connection$Response post(@NotNull final String urlStr, @NotNull final String body, @NotNull final Map<String, String> headers);
    
    @NotNull
    String base64Decode(@NotNull final String str);
    
    @NotNull
    String base64Decode(@NotNull final String str, final int flags);
    
    @Nullable
    byte[] base64DecodeToByteArray(@Nullable final String str);
    
    @Nullable
    byte[] base64DecodeToByteArray(@Nullable final String str, final int flags);
    
    @Nullable
    String base64Encode(@NotNull final String str);
    
    @Nullable
    String base64Encode(@NotNull final String str, final int flags);
    
    @NotNull
    String md5Encode(@NotNull final String str);
    
    @NotNull
    String md5Encode16(@NotNull final String str);
    
    @Nullable
    String timeFormatUTC(final long time, @NotNull final String format, final int sh);
    
    @NotNull
    String timeFormat(final long time);
    
    @NotNull
    String utf8ToGbk(@NotNull final String str);
    
    @NotNull
    String encodeURI(@NotNull final String str);
    
    @NotNull
    String encodeURI(@NotNull final String str, @NotNull final String enc);
    
    @NotNull
    String htmlFormat(@NotNull final String str);
    
    @NotNull
    File getFile(@NotNull final String path);
    
    @Nullable
    byte[] readFile(@NotNull final String path);
    
    @NotNull
    String readTxtFile(@NotNull final String path);
    
    @NotNull
    String readTxtFile(@NotNull final String path, @NotNull final String charsetName);
    
    void deleteFile(@NotNull final String path);
    
    @NotNull
    String unzipFile(@NotNull final String zipPath);
    
    @NotNull
    String getTxtInFolder(@NotNull final String unzipPath);
    
    @NotNull
    String getZipStringContent(@NotNull final String url, @NotNull final String path);
    
    @NotNull
    String getZipStringContent(@NotNull final String url, @NotNull final String path, @NotNull final String charsetName);
    
    @Nullable
    byte[] getZipByteArrayContent(@NotNull final String url, @NotNull final String path);
    
    @Nullable
    QueryTTF queryBase64TTF(@Nullable final String base64);
    
    @Nullable
    QueryTTF queryTTF(@Nullable final String str);
    
    @NotNull
    String replaceFont(@NotNull final String text, @Nullable final QueryTTF font1, @Nullable final QueryTTF font2);
    
    void toast(@Nullable final Object msg);
    
    void longToast(@Nullable final Object msg);
    
    @NotNull
    String log(@NotNull final String msg);
    
    void logType(@Nullable final Object any);
    
    @NotNull
    String randomUUID();
    
    @Nullable
    byte[] aesDecodeToByteArray(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String aesDecodeToString(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    byte[] aesBase64DecodeToByteArray(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String aesBase64DecodeToString(@NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    byte[] aesEncodeToByteArray(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String aesEncodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    byte[] aesEncodeToBase64ByteArray(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String aesEncodeToBase64String(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @NotNull
    String androidId();
    
    @Nullable
    String aesDecodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv);
    
    @Nullable
    String tripleDESDecodeStr(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv);
    
    @Nullable
    String tripleDESDecodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv);
    
    @Nullable
    String aesEncodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv);
    
    @Nullable
    String desDecodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String desBase64DecodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String desEncodeToString(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String desEncodeToBase64String(@NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv);
    
    @Nullable
    String tripleDESEncodeBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv);
    
    @Nullable
    String tripleDESEncodeArgsBase64Str(@NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv);
    
    @Nullable
    String digestHex(@NotNull final String data, @NotNull final String algorithm);
    
    @Nullable
    String digestBase64Str(@NotNull final String data, @NotNull final String algorithm);
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        @Nullable
        public static String ajax(@NotNull final JsExtensions this, @NotNull final String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return (String)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new JsExtensions$ajax.JsExtensions$ajax$1(urlStr, this, (Continuation)null), 1, (Object)null);
        }
        
        @NotNull
        public static StrResponse[] ajaxAll(@NotNull final JsExtensions this, @NotNull final String[] urlList) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlList, "urlList");
            return (StrResponse[])BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new JsExtensions$ajaxAll.JsExtensions$ajaxAll$1(urlList, this, (Continuation)null), 1, (Object)null);
        }
        
        @NotNull
        public static StrResponse connect(@NotNull final JsExtensions this, @NotNull final String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return (StrResponse)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new JsExtensions$connect.JsExtensions$connect$1(urlStr, this, (Continuation)null), 1, (Object)null);
        }
        
        @NotNull
        public static StrResponse connect(@NotNull final JsExtensions this, @NotNull final String urlStr, @Nullable final String header) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return (StrResponse)BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new JsExtensions$connect.JsExtensions$connect$2(header, this, urlStr, (Continuation)null), 1, (Object)null);
        }
        
        @Nullable
        public static String webView(@NotNull final JsExtensions this, @Nullable final String html, @Nullable final String url, @Nullable final String js) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return null;
        }
        
        @NotNull
        public static String importScript(@NotNull final JsExtensions this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            String s;
            if (StringsKt.startsWith$default(path, "http", false, 2, (Object)null)) {
                final String cacheFile = this.cacheFile(path);
                s = ((cacheFile == null) ? "" : cacheFile);
            }
            else {
                s = (StringsKt.startsWith$default(path, "/storage", false, 2, (Object)null) ? FileUtils.readText$default(FileUtils.INSTANCE, path, null, 2, null) : this.readTxtFile(path));
            }
            final String result = s;
            if (StringsKt.isBlank((CharSequence)result)) {
                throw new NoStackTraceException(Intrinsics.stringPlus(path, (Object)" \u5185\u5bb9\u83b7\u53d6\u5931\u8d25\u6216\u8005\u4e3a\u7a7a"));
            }
            return result;
        }
        
        @Nullable
        public static String cacheFile(@NotNull final JsExtensions this, @NotNull final String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return this.cacheFile(urlStr, 0);
        }
        
        @Nullable
        public static String cacheFile(@NotNull final JsExtensions this, @NotNull final String urlStr, final int saveTime) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            final String key = this.md5Encode16(urlStr);
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            final String cache = cacheInstance.getFile(key);
            final CharSequence charSequence = cache;
            if (charSequence != null && !StringsKt.isBlank(charSequence)) {
                return cache;
            }
            this.log(Intrinsics.stringPlus("\u9996\u6b21\u4e0b\u8f7d ", (Object)urlStr));
            final String ajax = this.ajax(urlStr);
            if (ajax == null) {
                return null;
            }
            final String value = ajax;
            cacheInstance.putFile(key, value, saveTime);
            return value;
        }
        
        @NotNull
        public static String getCookie(@NotNull final JsExtensions this, @NotNull final String tag, @Nullable final String key) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)tag, "tag");
            final CookieStore cookieStore = new CookieStore(this.getUserNameSpace());
            final String cookie = cookieStore.getCookie(tag);
            final Map cookieMap = cookieStore.cookieToMap(cookie);
            String s2;
            if (key != null) {
                final String s = cookieMap.get(key);
                s2 = ((s == null) ? "" : s);
            }
            else {
                s2 = cookie;
            }
            return s2;
        }
        
        @NotNull
        public static String downloadFile(@NotNull final JsExtensions this, @NotNull final String content, @NotNull final String url) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)content, "content");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            final String type2 = new AnalyzeUrl(url, null, null, null, null, null, null, null, null, null, null, 2046, null).getType();
            if (type2 == null) {
                return "";
            }
            final String type = type2;
            final String zipPath = FileUtils.INSTANCE.getPath(FileUtils.INSTANCE.createFolderIfNotExist(FileUtils.INSTANCE.getCachePath()), MD5Utils.INSTANCE.md5Encode16(url) + '.' + type);
            FileUtils.INSTANCE.deleteFile(zipPath);
            final File zipFile = FileUtils.INSTANCE.createFileIfNotExist(zipPath);
            final byte[] it = StringUtils.INSTANCE.hexStringToByte(content);
            final int n = 0;
            if (it.length != 0) {
                FilesKt.writeBytes(zipFile, it);
            }
            final String s = zipPath;
            final int length = FileUtils.INSTANCE.getCachePath().length();
            final String s2 = s;
            if (s2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring = s2.substring(length);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        
        @NotNull
        public static Connection$Response get(@NotNull final JsExtensions this, @NotNull final String urlStr, @NotNull final Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            Intrinsics.checkNotNullParameter((Object)headers, "headers");
            final Connection$Response response = Jsoup.connect(urlStr).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory()).ignoreContentType(true).followRedirects(false).headers((Map)headers).method(Connection$Method.GET).execute();
            final Map cookies = response.cookies();
            final CookieStore cookieStore = new CookieStore(this.getUserNameSpace());
            final String mapToCookie = cookieStore.mapToCookie(cookies);
            if (mapToCookie != null) {
                final String it = mapToCookie;
                final int n = 0;
                final String domain = NetworkUtils.INSTANCE.getSubDomain(urlStr);
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, (Object)"_cookieJar"), it);
            }
            Intrinsics.checkNotNullExpressionValue((Object)response, "response");
            return response;
        }
        
        @NotNull
        public static Connection$Response head(@NotNull final JsExtensions this, @NotNull final String urlStr, @NotNull final Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            Intrinsics.checkNotNullParameter((Object)headers, "headers");
            final Connection$Response response = Jsoup.connect(urlStr).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory()).ignoreContentType(true).followRedirects(false).headers((Map)headers).method(Connection$Method.HEAD).execute();
            final Map cookies = response.cookies();
            final CookieStore cookieStore = new CookieStore(this.getUserNameSpace());
            final String mapToCookie = cookieStore.mapToCookie(cookies);
            if (mapToCookie != null) {
                final String it = mapToCookie;
                final int n = 0;
                final String domain = NetworkUtils.INSTANCE.getSubDomain(urlStr);
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, (Object)"_cookieJar"), it);
            }
            Intrinsics.checkNotNullExpressionValue((Object)response, "response");
            return response;
        }
        
        @NotNull
        public static Connection$Response post(@NotNull final JsExtensions this, @NotNull final String urlStr, @NotNull final String body, @NotNull final Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            Intrinsics.checkNotNullParameter((Object)body, "body");
            Intrinsics.checkNotNullParameter((Object)headers, "headers");
            final Connection$Response response = Jsoup.connect(urlStr).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory()).ignoreContentType(true).followRedirects(false).requestBody(body).headers((Map)headers).method(Connection$Method.POST).execute();
            final Map cookies = response.cookies();
            final CookieStore cookieStore = new CookieStore(this.getUserNameSpace());
            final String mapToCookie = cookieStore.mapToCookie(cookies);
            if (mapToCookie != null) {
                final String it = mapToCookie;
                final int n = 0;
                final String domain = NetworkUtils.INSTANCE.getSubDomain(urlStr);
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, (Object)"_cookieJar"), it);
            }
            Intrinsics.checkNotNullExpressionValue((Object)response, "response");
            return response;
        }
        
        @NotNull
        public static String base64Decode(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return EncoderUtils.INSTANCE.base64Decode(str, 2);
        }
        
        @NotNull
        public static String base64Decode(@NotNull final JsExtensions this, @NotNull final String str, final int flags) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return EncoderUtils.INSTANCE.base64Decode(str, flags);
        }
        
        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull final JsExtensions this, @Nullable final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CharSequence charSequence = str;
            if (charSequence == null || StringsKt.isBlank(charSequence)) {
                return null;
            }
            return Base64.decode(str, 0);
        }
        
        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull final JsExtensions this, @Nullable final String str, final int flags) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CharSequence charSequence = str;
            if (charSequence == null || StringsKt.isBlank(charSequence)) {
                return null;
            }
            return Base64.decode(str, flags);
        }
        
        @Nullable
        public static String base64Encode(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return EncoderUtils.INSTANCE.base64Encode(str, 2);
        }
        
        @Nullable
        public static String base64Encode(@NotNull final JsExtensions this, @NotNull final String str, final int flags) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return EncoderUtils.INSTANCE.base64Encode(str, flags);
        }
        
        @NotNull
        public static String md5Encode(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return MD5Utils.INSTANCE.md5Encode(str);
        }
        
        @NotNull
        public static String md5Encode16(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return MD5Utils.INSTANCE.md5Encode16(str);
        }
        
        @Nullable
        public static String timeFormatUTC(@NotNull final JsExtensions this, final long time, @NotNull final String format, final int sh) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)format, "format");
            final SimpleTimeZone utc = new SimpleTimeZone(sh, "UTC");
            final SimpleDateFormat receiver = new SimpleDateFormat(format, Locale.getDefault());
            final int n = 0;
            receiver.setTimeZone(utc);
            return receiver.format(new Date(time));
        }
        
        @NotNull
        public static String timeFormat(@NotNull final JsExtensions this, final long time) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final String format = AppConst.INSTANCE.getDateFormat().format(new Date(time));
            Intrinsics.checkNotNullExpressionValue((Object)format, "dateFormat.format(Date(time))");
            return format;
        }
        
        @NotNull
        public static String utf8ToGbk(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            final Charset forName = Charset.forName("UTF-8");
            Intrinsics.checkNotNullExpressionValue((Object)forName, "Charset.forName(charsetName)");
            final byte[] bytes = str.getBytes(forName);
            Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
            final String utf8 = new String(bytes, Charsets.UTF_8);
            final byte[] bytes2 = utf8.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes2, "(this as java.lang.String).getBytes(charset)");
            final byte[] bytes3 = bytes2;
            final Charset forName2 = Charset.forName("UTF-8");
            Intrinsics.checkNotNullExpressionValue((Object)forName2, "Charset.forName(charsetName)");
            final String s;
            final String unicode = s = new String(bytes3, forName2);
            final Charset forName3 = Charset.forName("GBK");
            Intrinsics.checkNotNullExpressionValue((Object)forName3, "Charset.forName(charsetName)");
            final byte[] bytes4 = s.getBytes(forName3);
            Intrinsics.checkNotNullExpressionValue((Object)bytes4, "(this as java.lang.String).getBytes(charset)");
            return new String(bytes4, Charsets.UTF_8);
        }
        
        @NotNull
        public static String encodeURI(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            String s;
            try {
                final String encode = URLEncoder.encode(str, "UTF-8");
                Intrinsics.checkNotNullExpressionValue((Object)encode, "{\n            URLEncoder.encode(str, \"UTF-8\")\n        }");
                s = encode;
            }
            catch (final Exception e) {
                s = "";
            }
            return s;
        }
        
        @NotNull
        public static String encodeURI(@NotNull final JsExtensions this, @NotNull final String str, @NotNull final String enc) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)enc, "enc");
            String s;
            try {
                final String encode = URLEncoder.encode(str, enc);
                Intrinsics.checkNotNullExpressionValue((Object)encode, "{\n            URLEncoder.encode(str, enc)\n        }");
                s = encode;
            }
            catch (final Exception e) {
                s = "";
            }
            return s;
        }
        
        @NotNull
        public static String htmlFormat(@NotNull final JsExtensions this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return HtmlFormatter.formatKeepImg$default(HtmlFormatter.INSTANCE, str, (URL)null, 2, (Object)null);
        }
        
        @NotNull
        public static File getFile(@NotNull final JsExtensions this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            final String cachePath = ReaderAdapterHelper.INSTANCE.getAdapter().getCacheDir();
            final String separator = File.separator;
            Intrinsics.checkNotNullExpressionValue((Object)separator, "separator");
            final String aPath = StringsKt.startsWith$default(path, separator, false, 2, (Object)null) ? Intrinsics.stringPlus(cachePath, (Object)path) : (cachePath + (Object)File.separator + path);
            return new File(aPath);
        }
        
        @Nullable
        public static byte[] readFile(@NotNull final JsExtensions this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            final File file = this.getFile(path);
            if (file.exists()) {
                return FilesKt.readBytes(file);
            }
            return null;
        }
        
        @NotNull
        public static String readTxtFile(@NotNull final JsExtensions this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            final File file = this.getFile(path);
            if (file.exists()) {
                final String charsetName = EncodingDetect.INSTANCE.getEncode(file);
                final byte[] bytes = FilesKt.readBytes(file);
                final Charset forName = Charset.forName(charsetName);
                Intrinsics.checkNotNullExpressionValue((Object)forName, "Charset.forName(charsetName)");
                return new String(bytes, forName);
            }
            return "";
        }
        
        @NotNull
        public static String readTxtFile(@NotNull final JsExtensions this, @NotNull final String path, @NotNull final String charsetName) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            Intrinsics.checkNotNullParameter((Object)charsetName, "charsetName");
            final File file = this.getFile(path);
            if (file.exists()) {
                final byte[] bytes = FilesKt.readBytes(file);
                final Charset forName = Charset.forName(charsetName);
                Intrinsics.checkNotNullExpressionValue((Object)forName, "Charset.forName(charsetName)");
                return new String(bytes, forName);
            }
            return "";
        }
        
        public static void deleteFile(@NotNull final JsExtensions this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            final File file = this.getFile(path);
            FileUtils.INSTANCE.delete(file, true);
        }
        
        @NotNull
        public static String unzipFile(@NotNull final JsExtensions this, @NotNull final String zipPath) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)zipPath, "zipPath");
            if (zipPath.length() == 0) {
                return "";
            }
            final String unzipPath = FileUtils.INSTANCE.getPath(FileUtils.INSTANCE.createFolderIfNotExist(FileUtils.INSTANCE.getCachePath()), FileUtils.INSTANCE.getNameExcludeExtension(zipPath));
            FileUtils.INSTANCE.deleteFile(unzipPath);
            final File zipFile = this.getFile(zipPath);
            final File unzipFolder = FileUtils.INSTANCE.createFolderIfNotExist(unzipPath);
            ZipUtils.INSTANCE.unzipFile(zipFile, unzipFolder);
            final FileUtils instance = FileUtils.INSTANCE;
            final String absolutePath = zipFile.getAbsolutePath();
            Intrinsics.checkNotNullExpressionValue((Object)absolutePath, "zipFile.absolutePath");
            instance.deleteFile(absolutePath);
            final String s = unzipPath;
            final int length = FileUtils.INSTANCE.getCachePath().length();
            final String s2 = s;
            if (s2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring = s2.substring(length);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        
        @NotNull
        public static String getTxtInFolder(@NotNull final JsExtensions this, @NotNull final String unzipPath) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)unzipPath, "unzipPath");
            if (unzipPath.length() == 0) {
                return "";
            }
            final File unzipFolder = this.getFile(unzipPath);
            final StringBuilder contents = new StringBuilder();
            final File[] it = unzipFolder.listFiles();
            final int n = 0;
            if (it != null) {
                final File[] array = it;
                int i = 0;
                while (i < array.length) {
                    final File f = array[i];
                    ++i;
                    final EncodingDetect instance = EncodingDetect.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue((Object)f, "f");
                    final String charsetName = instance.getEncode(f);
                    final StringBuilder sb = contents;
                    final byte[] bytes = FilesKt.readBytes(f);
                    final Charset forName = Charset.forName(charsetName);
                    Intrinsics.checkNotNullExpressionValue((Object)forName, "Charset.forName(charsetName)");
                    sb.append(new String(bytes, forName)).append("\n");
                }
                contents.deleteCharAt(contents.length() - 1);
            }
            final FileUtils instance2 = FileUtils.INSTANCE;
            final String absolutePath = unzipFolder.getAbsolutePath();
            Intrinsics.checkNotNullExpressionValue((Object)absolutePath, "unzipFolder.absolutePath");
            instance2.deleteFile(absolutePath);
            final String string = contents.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "contents.toString()");
            return string;
        }
        
        @NotNull
        public static String getZipStringContent(@NotNull final JsExtensions this, @NotNull final String url, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            final byte[] zipByteArrayContent = this.getZipByteArrayContent(url, path);
            if (zipByteArrayContent == null) {
                return "";
            }
            final byte[] byteArray = zipByteArrayContent;
            final String charsetName = EncodingDetect.INSTANCE.getEncode(byteArray);
            final Charset forName = Charset.forName(charsetName);
            Intrinsics.checkNotNullExpressionValue((Object)forName, "forName(charsetName)");
            return new String(byteArray, forName);
        }
        
        @NotNull
        public static String getZipStringContent(@NotNull final JsExtensions this, @NotNull final String url, @NotNull final String path, @NotNull final String charsetName) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            Intrinsics.checkNotNullParameter((Object)charsetName, "charsetName");
            final byte[] zipByteArrayContent = this.getZipByteArrayContent(url, path);
            if (zipByteArrayContent == null) {
                return "";
            }
            final byte[] byteArray = zipByteArrayContent;
            final Charset forName = Charset.forName(charsetName);
            Intrinsics.checkNotNullExpressionValue((Object)forName, "forName(charsetName)");
            return new String(byteArray, forName);
        }
        
        @Nullable
        public static byte[] getZipByteArrayContent(@NotNull final JsExtensions this, @NotNull final String url, @NotNull final String path) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "this"
            //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //     6: aload_1         /* url */
            //     7: ldc             "url"
            //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //    12: aload_2         /* path */
            //    13: ldc             "path"
            //    15: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //    18: aload_1         /* url */
            //    19: ldc_w           "http://"
            //    22: iconst_0       
            //    23: iconst_2       
            //    24: aconst_null    
            //    25: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
            //    28: ifne            44
            //    31: aload_1         /* url */
            //    32: ldc_w           "https://"
            //    35: iconst_0       
            //    36: iconst_2       
            //    37: aconst_null    
            //    38: invokestatic    kotlin/text/StringsKt.startsWith$default:(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
            //    41: ifeq            68
            //    44: aconst_null    
            //    45: new             Lio/legado/app/help/JsExtensions$getZipByteArrayContent$bytes$1;
            //    48: dup            
            //    49: aload_1         /* url */
            //    50: aconst_null    
            //    51: invokespecial   io/legado/app/help/JsExtensions$getZipByteArrayContent$bytes$1.<init>:(Ljava/lang/String;Lkotlin/coroutines/Continuation;)V
            //    54: checkcast       Lkotlin/jvm/functions/Function2;
            //    57: iconst_1       
            //    58: aconst_null    
            //    59: invokestatic    kotlinx/coroutines/BuildersKt.runBlocking$default:(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;ILjava/lang/Object;)Ljava/lang/Object;
            //    62: checkcast       [B
            //    65: goto            75
            //    68: getstatic       io/legado/app/utils/StringUtils.INSTANCE:Lio/legado/app/utils/StringUtils;
            //    71: aload_1         /* url */
            //    72: invokevirtual   io/legado/app/utils/StringUtils.hexStringToByte:(Ljava/lang/String;)[B
            //    75: astore_3        /* bytes */
            //    76: new             Ljava/io/ByteArrayOutputStream;
            //    79: dup            
            //    80: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
            //    83: astore          bos
            //    85: new             Ljava/util/zip/ZipInputStream;
            //    88: dup            
            //    89: new             Ljava/io/ByteArrayInputStream;
            //    92: dup            
            //    93: aload_3         /* bytes */
            //    94: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
            //    97: checkcast       Ljava/io/InputStream;
            //   100: invokespecial   java/util/zip/ZipInputStream.<init>:(Ljava/io/InputStream;)V
            //   103: astore          zis
            //   105: aload           zis
            //   107: invokevirtual   java/util/zip/ZipInputStream.getNextEntry:()Ljava/util/zip/ZipEntry;
            //   110: astore          entry
            //   112: aload           entry
            //   114: ifnull          227
            //   117: aload           entry
            //   119: invokevirtual   java/util/zip/ZipEntry.getName:()Ljava/lang/String;
            //   122: aload_2         /* path */
            //   123: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
            //   126: ifeq            217
            //   129: aload           zis
            //   131: checkcast       Ljava/io/Closeable;
            //   134: astore          7
            //   136: iconst_0       
            //   137: istore          8
            //   139: iconst_0       
            //   140: istore          9
            //   142: aconst_null    
            //   143: checkcast       Ljava/lang/Throwable;
            //   146: astore          9
            //   148: nop            
            //   149: aload           7
            //   151: checkcast       Ljava/util/zip/ZipInputStream;
            //   154: astore          it
            //   156: iconst_0       
            //   157: istore          $i$a$-use-JsExtensions$getZipByteArrayContent$1
            //   159: aload           it
            //   161: checkcast       Ljava/io/InputStream;
            //   164: aload           bos
            //   166: checkcast       Ljava/io/OutputStream;
            //   169: iconst_0       
            //   170: iconst_2       
            //   171: aconst_null    
            //   172: invokestatic    kotlin/io/ByteStreamsKt.copyTo$default:(Ljava/io/InputStream;Ljava/io/OutputStream;IILjava/lang/Object;)J
            //   175: lstore          null
            //   177: aload           7
            //   179: aload           9
            //   181: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
            //   184: lload           10
            //   186: goto            210
            //   189: astore          10
            //   191: aload           10
            //   193: astore          9
            //   195: aload           10
            //   197: athrow         
            //   198: astore          10
            //   200: aload           7
            //   202: aload           9
            //   204: invokestatic    kotlin/io/CloseableKt.closeFinally:(Ljava/io/Closeable;Ljava/lang/Throwable;)V
            //   207: aload           10
            //   209: athrow         
            //   210: pop2           
            //   211: aload           bos
            //   213: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
            //   216: areturn        
            //   217: aload           zis
            //   219: invokevirtual   java/util/zip/ZipInputStream.getNextEntry:()Ljava/util/zip/ZipEntry;
            //   222: astore          entry
            //   224: goto            112
            //   227: getstatic       io/legado/app/model/Debug.INSTANCE:Lio/legado/app/model/Debug;
            //   230: ldc_w           "getZipContent \u672a\u53d1\u73b0\u5185\u5bb9"
            //   233: invokevirtual   io/legado/app/model/Debug.log:(Ljava/lang/String;)V
            //   236: aconst_null    
            //   237: areturn        
            //    MethodParameters:
            //  Name  Flags      
            //  ----  ---------
            //  this  SYNTHETIC
            //  url   
            //  path  
            //    StackMapTable: 00 09 2C 17 46 07 01 1D FF 00 24 00 07 07 00 50 07 00 22 07 00 22 07 01 1D 07 02 81 07 02 84 07 02 94 00 00 FF 00 4C 00 0A 07 00 50 07 00 22 07 00 22 07 01 1D 07 02 81 07 02 84 07 02 94 07 02 9D 01 07 02 76 00 01 07 02 76 48 07 02 76 FF 00 0B 00 0C 07 00 50 07 00 22 07 00 22 07 01 1D 07 02 81 07 02 84 07 02 94 07 02 9D 01 07 02 76 04 01 00 01 04 FF 00 06 00 07 07 00 50 07 00 22 07 00 22 07 01 1D 07 02 81 07 02 84 07 02 94 00 00 09
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  148    177    189    198    Ljava/lang/Throwable;
            //  148    177    198    210    Any
            //  189    198    198    210    Any
            //  198    200    198    210    Any
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
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:552)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
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
        public static QueryTTF queryBase64TTF(@NotNull final JsExtensions this, @Nullable final String base64) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final byte[] base64DecodeToByteArray = this.base64DecodeToByteArray(base64);
            if (base64DecodeToByteArray == null) {
                return null;
            }
            final byte[] it = base64DecodeToByteArray;
            final int n = 0;
            return new QueryTTF(it);
        }
        
        @Nullable
        public static QueryTTF queryTTF(@NotNull final JsExtensions this, @Nullable final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            if (str == null) {
                return null;
            }
            final String key = this.md5Encode16(str);
            final Ref$ObjectRef cacheInstance = new Ref$ObjectRef();
            cacheInstance.element = new CacheManager(this.getUserNameSpace());
            QueryTTF qTTF = ((CacheManager)cacheInstance.element).getQueryTTF(key);
            if (qTTF != null) {
                return qTTF;
            }
            final byte[] font = (byte[])(StringExtensionsKt.isAbsUrl(str) ? BuildersKt.runBlocking$default((CoroutineContext)null, (Function2)new JsExtensions$queryTTF$font.JsExtensions$queryTTF$font$1(cacheInstance, key, str, (Continuation)null), 1, (Object)null) : ((StringsKt.indexOf$default((CharSequence)str, "storage/", 0, false, 6, (Object)null) > 0) ? FilesKt.readBytes(new File(str)) : this.base64DecodeToByteArray(str)));
            if (font == null) {
                return null;
            }
            qTTF = new QueryTTF(font);
            CacheManager.put$default((CacheManager)cacheInstance.element, key, (Object)qTTF, 0, 4, (Object)null);
            return qTTF;
        }
        
        @NotNull
        public static String replaceFont(@NotNull final JsExtensions this, @NotNull final String text, @Nullable final QueryTTF font1, @Nullable final QueryTTF font2) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "this"
            //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //     6: aload_1         /* text */
            //     7: ldc_w           "text"
            //    10: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //    13: aload_2         /* font1 */
            //    14: ifnull          21
            //    17: aload_3         /* font2 */
            //    18: ifnonnull       23
            //    21: aload_1         /* text */
            //    22: areturn        
            //    23: aload_1         /* text */
            //    24: astore          5
            //    26: iconst_0       
            //    27: istore          6
            //    29: aload           5
            //    31: invokevirtual   java/lang/String.toCharArray:()[C
            //    34: dup            
            //    35: ldc_w           "(this as java.lang.String).toCharArray()"
            //    38: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //    41: astore          contentArray
            //    43: aload           contentArray
            //    45: astore          $this$forEachIndexed$iv
            //    47: iconst_0       
            //    48: istore          $i$f$forEachIndexed
            //    50: iconst_0       
            //    51: istore          index$iv
            //    53: aload           $this$forEachIndexed$iv
            //    55: astore          8
            //    57: aload           8
            //    59: arraylength    
            //    60: istore          9
            //    62: iconst_0       
            //    63: istore          10
            //    65: iload           10
            //    67: iload           9
            //    69: if_icmpge       145
            //    72: aload           8
            //    74: iload           10
            //    76: caload         
            //    77: istore          item$iv
            //    79: iload           index$iv
            //    81: iinc            index$iv, 1
            //    84: iload           item$iv
            //    86: istore          12
            //    88: istore          index
            //    90: iconst_0       
            //    91: istore          $i$a$-forEachIndexed-JsExtensions$replaceFont$1
            //    93: iload           s
            //    95: istore          15
            //    97: iconst_0       
            //    98: istore          16
            //   100: iload           15
            //   102: istore          oldCode
            //   104: aload_2         /* font1 */
            //   105: iload           s
            //   107: invokevirtual   io/legado/app/model/analyzeRule/QueryTTF.inLimit:(C)Z
            //   110: ifeq            138
            //   113: aload_3         /* font2 */
            //   114: aload_2         /* font1 */
            //   115: iload           oldCode
            //   117: invokevirtual   io/legado/app/model/analyzeRule/QueryTTF.getGlyfByCode:(I)Ljava/lang/String;
            //   120: invokevirtual   io/legado/app/model/analyzeRule/QueryTTF.getCodeByGlyf:(Ljava/lang/String;)I
            //   123: istore          code
            //   125: iload           code
            //   127: ifeq            138
            //   130: aload           contentArray
            //   132: iload           index
            //   134: iload           code
            //   136: i2c            
            //   137: castore        
            //   138: nop            
            //   139: iinc            10, 1
            //   142: goto            65
            //   145: nop            
            //   146: aload           contentArray
            //   148: ldc             ""
            //   150: checkcast       Ljava/lang/CharSequence;
            //   153: aconst_null    
            //   154: aconst_null    
            //   155: iconst_0       
            //   156: aconst_null    
            //   157: aconst_null    
            //   158: bipush          62
            //   160: aconst_null    
            //   161: invokestatic    kotlin/collections/ArraysKt.joinToString$default:([CLjava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/String;
            //   164: areturn        
            //    MethodParameters:
            //  Name   Flags      
            //  -----  ---------
            //  this   SYNTHETIC
            //  text   
            //  font1  
            //  font2  
            //    StackMapTable: 00 05 15 01 FF 00 29 00 0B 07 00 50 07 00 22 07 02 C7 07 02 C7 07 03 18 07 03 18 01 01 07 03 18 01 01 00 00 FF 00 48 00 12 07 00 50 07 00 22 07 02 C7 07 02 C7 07 03 18 07 03 18 01 01 07 03 18 01 01 01 01 01 01 01 01 01 00 00 FF 00 06 00 0B 07 00 50 07 00 22 07 02 C7 07 02 C7 07 03 18 07 03 18 01 01 07 03 18 01 01 00 00
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
        
        public static void toast(@NotNull final JsExtensions this, @Nullable final Object msg) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final DebugLog logger = this.getLogger();
            if (logger != null) {
                logger.log(Intrinsics.stringPlus("toast: ", msg));
            }
            Debug.INSTANCE.log(Intrinsics.stringPlus("toast: ", msg));
        }
        
        public static void longToast(@NotNull final JsExtensions this, @Nullable final Object msg) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final DebugLog logger = this.getLogger();
            if (logger != null) {
                logger.log(Intrinsics.stringPlus("longToast: ", msg));
            }
            Debug.INSTANCE.log(Intrinsics.stringPlus("longToast: ", msg));
        }
        
        @NotNull
        public static String log(@NotNull final JsExtensions this, @NotNull final String msg) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)msg, "msg");
            final DebugLog logger = this.getLogger();
            if (logger != null) {
                logger.log(msg);
            }
            Debug.INSTANCE.log(msg);
            return msg;
        }
        
        public static void logType(@NotNull final JsExtensions this, @Nullable final Object any) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            if (any == null) {
                this.log("null");
            }
            else {
                final String name = any.getClass().getName();
                Intrinsics.checkNotNullExpressionValue((Object)name, "any.javaClass.name");
                this.log(name);
            }
        }
        
        @NotNull
        public static String randomUUID(@NotNull final JsExtensions this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final String string = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "randomUUID().toString()");
            return string;
        }
        
        @Nullable
        public static byte[] aesDecodeToByteArray(@NotNull final JsExtensions this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            byte[] decryptAES;
            try {
                decryptAES = EncoderUtils.INSTANCE.decryptAES(StringsKt.encodeToByteArray(str), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            }
            catch (final Exception e) {
                LogUtilsKt.printOnDebug((Throwable)e);
                final String localizedMessage = e.getLocalizedMessage();
                this.log((localizedMessage == null) ? "aesDecodeToByteArrayERROR" : localizedMessage);
                decryptAES = null;
            }
            return decryptAES;
        }
        
        @Nullable
        public static String aesDecodeToString(@NotNull final JsExtensions this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] aesDecodeToByteArray = this.aesDecodeToByteArray(str, key, transformation, iv);
            String s;
            if (aesDecodeToByteArray == null) {
                s = null;
            }
            else {
                final byte[] it = aesDecodeToByteArray;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static byte[] aesBase64DecodeToByteArray(@NotNull final JsExtensions this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            byte[] decryptBase64AES;
            try {
                decryptBase64AES = EncoderUtils.INSTANCE.decryptBase64AES(StringsKt.encodeToByteArray(str), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            }
            catch (final Exception e) {
                LogUtilsKt.printOnDebug((Throwable)e);
                final String localizedMessage = e.getLocalizedMessage();
                this.log((localizedMessage == null) ? "aesDecodeToByteArrayERROR" : localizedMessage);
                decryptBase64AES = null;
            }
            return decryptBase64AES;
        }
        
        @Nullable
        public static String aesBase64DecodeToString(@NotNull final JsExtensions this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] aesBase64DecodeToByteArray = this.aesBase64DecodeToByteArray(str, key, transformation, iv);
            String s;
            if (aesBase64DecodeToByteArray == null) {
                s = null;
            }
            else {
                final byte[] it = aesBase64DecodeToByteArray;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static byte[] aesEncodeToByteArray(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            byte[] encryptAES;
            try {
                encryptAES = EncoderUtils.INSTANCE.encryptAES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            }
            catch (final Exception e) {
                LogUtilsKt.printOnDebug((Throwable)e);
                final String localizedMessage = e.getLocalizedMessage();
                this.log((localizedMessage == null) ? "aesEncodeToByteArrayERROR" : localizedMessage);
                encryptAES = null;
            }
            return encryptAES;
        }
        
        @Nullable
        public static String aesEncodeToString(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] aesEncodeToByteArray = this.aesEncodeToByteArray(data, key, transformation, iv);
            String s;
            if (aesEncodeToByteArray == null) {
                s = null;
            }
            else {
                final byte[] it = aesEncodeToByteArray;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static byte[] aesEncodeToBase64ByteArray(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            byte[] encryptAES2Base64;
            try {
                encryptAES2Base64 = EncoderUtils.INSTANCE.encryptAES2Base64(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            }
            catch (final Exception e) {
                LogUtilsKt.printOnDebug((Throwable)e);
                final String localizedMessage = e.getLocalizedMessage();
                this.log((localizedMessage == null) ? "aesEncodeToBase64ByteArrayERROR" : localizedMessage);
                encryptAES2Base64 = null;
            }
            return encryptAES2Base64;
        }
        
        @Nullable
        public static String aesEncodeToBase64String(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] aesEncodeToBase64ByteArray = this.aesEncodeToBase64ByteArray(data, key, transformation, iv);
            String s;
            if (aesEncodeToBase64ByteArray == null) {
                s = null;
            }
            else {
                final byte[] it = aesEncodeToBase64ByteArray;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @NotNull
        public static String androidId(@NotNull final JsExtensions this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return "";
        }
        
        @Nullable
        public static String aesDecodeArgsBase64Str(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return new AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data);
        }
        
        @Nullable
        public static String tripleDESDecodeStr(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] bytes = key.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
            final byte[] bytes2 = iv.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes2, "(this as java.lang.String).getBytes(charset)");
            return new DESede(mode, padding, bytes, bytes2).decryptStr(data);
        }
        
        @Nullable
        public static String tripleDESDecodeArgsBase64Str(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return new DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data);
        }
        
        @Nullable
        public static String aesEncodeArgsBase64Str(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return new AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data);
        }
        
        @Nullable
        public static String desDecodeToString(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] decryptDES = EncoderUtils.INSTANCE.decryptDES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            String s;
            if (decryptDES == null) {
                s = null;
            }
            else {
                final byte[] it = decryptDES;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static String desBase64DecodeToString(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] decryptBase64DES = EncoderUtils.INSTANCE.decryptBase64DES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            String s;
            if (decryptBase64DES == null) {
                s = null;
            }
            else {
                final byte[] it = decryptBase64DES;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static String desEncodeToString(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] encryptDES = EncoderUtils.INSTANCE.encryptDES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            String s;
            if (encryptDES == null) {
                s = null;
            }
            else {
                final byte[] it = encryptDES;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static String desEncodeToBase64String(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] encryptDES2Base64 = EncoderUtils.INSTANCE.encryptDES2Base64(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            String s;
            if (encryptDES2Base64 == null) {
                s = null;
            }
            else {
                final byte[] it = encryptDES2Base64;
                final int n = 0;
                s = new String(it, Charsets.UTF_8);
            }
            return s;
        }
        
        @Nullable
        public static String tripleDESEncodeBase64Str(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            final byte[] bytes = key.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
            final byte[] bytes2 = iv.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes2, "(this as java.lang.String).getBytes(charset)");
            return new DESede(mode, padding, bytes, bytes2).encryptBase64(data);
        }
        
        @Nullable
        public static String tripleDESEncodeArgsBase64Str(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return new DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data);
        }
        
        @Nullable
        public static String digestHex(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String algorithm) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)algorithm, "algorithm");
            return DigestUtil.digester(algorithm).digestHex(data);
        }
        
        @Nullable
        public static String digestBase64Str(@NotNull final JsExtensions this, @NotNull final String data, @NotNull final String algorithm) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)algorithm, "algorithm");
            return Base64.encodeToString(DigestUtil.digester(algorithm).digest(data), 2);
        }
    }
}
