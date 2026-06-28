package io.legado.app.help;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DESede;
import com.google.gson.Gson;
import io.legado.app.adapters.ReaderAdapterHelper;
import io.legado.app.constant.AppConst;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.help.http.CookieStore;
import io.legado.app.help.http.SSLHelper;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.Debug;
import io.legado.app.model.DebugLog;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.Base64;
import io.legado.app.utils.EncoderUtils;
import io.legado.app.utils.EncodingDetect;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.HtmlFormatter;
import io.legado.app.utils.LogUtilsKt;
import io.legado.app.utils.MD5Utils;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.StringExtensionsKt;
import io.legado.app.utils.StringUtils;
import io.legado.app.utils.ThrowableExtensionsKt;
import io.legado.app.utils.ZipUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/* JADX INFO: compiled from: JsExtensions.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\u000f\bf\u0018\u00002\u00020\u0001J*\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u000e\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u000f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0013\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J#\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u00182\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018H\u0016¢\u0006\u0002\u0010\u001bJ\b\u0010\u001c\u001a\u00020\u0005H\u0016J\u0010\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0014\u0010 \u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u001c\u0010 \u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0012\u0010!\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u001a\u0010!\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0012\u0010\"\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J\u001c\u0010\"\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010#\u001a\u00020\u001fH\u0016J\u0010\u0010$\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u0005H\u0016J\u001a\u0010$\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u00052\b\u0010%\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0005H\u0016J*\u0010)\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010*\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010+\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J*\u0010,\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u001a\u0010-\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u0005H\u0016J\u001a\u0010/\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u0005H\u0016J\u0018\u00100\u001a\u00020\u00052\u0006\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u0005H\u0016J\u0010\u00103\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u00103\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u00104\u001a\u00020\u0005H\u0016J$\u00105\u001a\u0002062\u0006\u0010\u0016\u001a\u00020\u00052\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000508H\u0016J\u001c\u00109\u001a\u00020\u00052\u0006\u0010:\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010;\u001a\u00020<2\u0006\u0010(\u001a\u00020\u0005H\u0016J\n\u0010=\u001a\u0004\u0018\u00010>H&J\n\u0010?\u001a\u0004\u0018\u00010@H&J\u0010\u0010A\u001a\u00020\u00052\u0006\u0010B\u001a\u00020\u0005H\u0016J\b\u0010C\u001a\u00020\u0005H&J\u001a\u0010D\u001a\u0004\u0018\u00010\u00032\u0006\u00102\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0018\u0010E\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J \u0010E\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u00052\u0006\u0010F\u001a\u00020\u0005H\u0016J$\u0010G\u001a\u0002062\u0006\u0010\u0016\u001a\u00020\u00052\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000508H\u0016J\u0010\u0010H\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010I\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0010\u0010J\u001a\u00020\u00052\u0006\u0010K\u001a\u00020\u0005H\u0016J\u0012\u0010L\u001a\u00020'2\b\u0010M\u001a\u0004\u0018\u00010\u0001H\u0016J\u0012\u0010N\u001a\u00020'2\b\u0010K\u001a\u0004\u0018\u00010\u0001H\u0016J\u0010\u0010O\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010P\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J,\u0010Q\u001a\u0002062\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010R\u001a\u00020\u00052\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000508H\u0016J\u0014\u0010S\u001a\u0004\u0018\u00010T2\b\u0010U\u001a\u0004\u0018\u00010\u0005H\u0016J\u0014\u0010V\u001a\u0004\u0018\u00010T2\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010W\u001a\u00020\u0005H\u0016J\u0012\u0010X\u001a\u0004\u0018\u00010\u00032\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0010\u0010Y\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0018\u0010Y\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u00052\u0006\u0010F\u001a\u00020\u0005H\u0016J$\u0010Z\u001a\u00020\u00052\u0006\u0010[\u001a\u00020\u00052\b\u0010\\\u001a\u0004\u0018\u00010T2\b\u0010]\u001a\u0004\u0018\u00010TH\u0016J\u0010\u0010^\u001a\u00020\u00052\u0006\u0010_\u001a\u00020`H\u0016J\"\u0010a\u001a\u0004\u0018\u00010\u00052\u0006\u0010_\u001a\u00020`2\u0006\u0010b\u001a\u00020\u00052\u0006\u0010c\u001a\u00020\u001fH\u0016J\u0012\u0010d\u001a\u00020'2\b\u0010K\u001a\u0004\u0018\u00010\u0001H\u0016J2\u0010e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010g\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J2\u0010h\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0010\u0010i\u001a\u00020\u00052\u0006\u0010j\u001a\u00020\u0005H\u0016J\u0010\u0010k\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010l\u001a\u0004\u0018\u00010\u00052\b\u0010m\u001a\u0004\u0018\u00010\u00052\b\u00102\u001a\u0004\u0018\u00010\u00052\b\u0010n\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006o"}, d2 = {"Lio/legado/app/help/JsExtensions;", PackageDocumentBase.PREFIX_OPF, "aesBase64DecodeToByteArray", PackageDocumentBase.PREFIX_OPF, "str", PackageDocumentBase.PREFIX_OPF, "key", "transformation", "iv", "aesBase64DecodeToString", "aesDecodeArgsBase64Str", "data", "mode", "padding", "aesDecodeToByteArray", "aesDecodeToString", "aesEncodeArgsBase64Str", "aesEncodeToBase64ByteArray", "aesEncodeToBase64String", "aesEncodeToByteArray", "aesEncodeToString", "ajax", "urlStr", "ajaxAll", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/help/http/StrResponse;", "urlList", "([Ljava/lang/String;)[Lio/legado/app/help/http/StrResponse;", "androidId", "base64Decode", "flags", PackageDocumentBase.PREFIX_OPF, "base64DecodeToByteArray", "base64Encode", "cacheFile", "saveTime", "connect", "header", "deleteFile", PackageDocumentBase.PREFIX_OPF, "path", "desBase64DecodeToString", "desDecodeToString", "desEncodeToBase64String", "desEncodeToString", "digestBase64Str", "algorithm", "digestHex", "downloadFile", "content", RSSKeywords.RSS_ITEM_URL, "encodeURI", "enc", "get", "Lorg/jsoup/Connection$Response;", "headers", PackageDocumentBase.PREFIX_OPF, "getCookie", "tag", "getFile", "Ljava/io/File;", "getLogger", "Lio/legado/app/model/DebugLog;", "getSource", "Lio/legado/app/data/entities/BaseSource;", "getTxtInFolder", "unzipPath", "getUserNameSpace", "getZipByteArrayContent", "getZipStringContent", "charsetName", "head", "htmlFormat", "importScript", "log", "msg", "logType", "any", "longToast", "md5Encode", "md5Encode16", "post", NCXDocumentV3.XHTMLTgs.body, "queryBase64TTF", "Lio/legado/app/model/analyzeRule/QueryTTF;", "base64", "queryTTF", "randomUUID", "readFile", "readTxtFile", "replaceFont", NCXDocumentV2.NCXTags.text, "font1", "font2", "timeFormat", RSSKeywords.RSS_ITEM_TIME, PackageDocumentBase.PREFIX_OPF, "timeFormatUTC", PackageDocumentBase.DCTags.format, "sh", "toast", "tripleDESDecodeArgsBase64Str", "tripleDESDecodeStr", "tripleDESEncodeArgsBase64Str", "tripleDESEncodeBase64Str", "unzipFile", "zipPath", "utf8ToGbk", "webView", "html", "js", "reader-pro"})
public interface JsExtensions {
    @Nullable
    BaseSource getSource();

    @NotNull
    String getUserNameSpace();

    @Nullable
    DebugLog getLogger();

    @Nullable
    String ajax(@NotNull String urlStr);

    @NotNull
    StrResponse[] ajaxAll(@NotNull String[] urlList);

    @NotNull
    StrResponse connect(@NotNull String urlStr);

    @NotNull
    StrResponse connect(@NotNull String urlStr, @Nullable String header);

    @Nullable
    String webView(@Nullable String html, @Nullable String url, @Nullable String js);

    @NotNull
    String importScript(@NotNull String path);

    @Nullable
    String cacheFile(@NotNull String urlStr);

    @Nullable
    String cacheFile(@NotNull String urlStr, int saveTime);

    @NotNull
    String getCookie(@NotNull String tag, @Nullable String key);

    @NotNull
    String downloadFile(@NotNull String content, @NotNull String url);

    @NotNull
    Connection.Response get(@NotNull String urlStr, @NotNull Map<String, String> headers);

    @NotNull
    Connection.Response head(@NotNull String urlStr, @NotNull Map<String, String> headers);

    @NotNull
    Connection.Response post(@NotNull String urlStr, @NotNull String body, @NotNull Map<String, String> headers);

    @NotNull
    String base64Decode(@NotNull String str);

    @NotNull
    String base64Decode(@NotNull String str, int flags);

    @Nullable
    byte[] base64DecodeToByteArray(@Nullable String str);

    @Nullable
    byte[] base64DecodeToByteArray(@Nullable String str, int flags);

    @Nullable
    String base64Encode(@NotNull String str);

    @Nullable
    String base64Encode(@NotNull String str, int flags);

    @NotNull
    String md5Encode(@NotNull String str);

    @NotNull
    String md5Encode16(@NotNull String str);

    @Nullable
    String timeFormatUTC(long time, @NotNull String format, int sh);

    @NotNull
    String timeFormat(long time);

    @NotNull
    String utf8ToGbk(@NotNull String str);

    @NotNull
    String encodeURI(@NotNull String str);

    @NotNull
    String encodeURI(@NotNull String str, @NotNull String enc);

    @NotNull
    String htmlFormat(@NotNull String str);

    @NotNull
    File getFile(@NotNull String path);

    @Nullable
    byte[] readFile(@NotNull String path);

    @NotNull
    String readTxtFile(@NotNull String path);

    @NotNull
    String readTxtFile(@NotNull String path, @NotNull String charsetName);

    void deleteFile(@NotNull String path);

    @NotNull
    String unzipFile(@NotNull String zipPath);

    @NotNull
    String getTxtInFolder(@NotNull String unzipPath);

    @NotNull
    String getZipStringContent(@NotNull String url, @NotNull String path);

    @NotNull
    String getZipStringContent(@NotNull String url, @NotNull String path, @NotNull String charsetName);

    @Nullable
    byte[] getZipByteArrayContent(@NotNull String url, @NotNull String path);

    @Nullable
    QueryTTF queryBase64TTF(@Nullable String base64);

    @Nullable
    QueryTTF queryTTF(@Nullable String str);

    @NotNull
    String replaceFont(@NotNull String text, @Nullable QueryTTF font1, @Nullable QueryTTF font2);

    void toast(@Nullable Object msg);

    void longToast(@Nullable Object msg);

    @NotNull
    String log(@NotNull String msg);

    void logType(@Nullable Object any);

    @NotNull
    String randomUUID();

    @Nullable
    byte[] aesDecodeToByteArray(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String aesDecodeToString(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    byte[] aesBase64DecodeToByteArray(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String aesBase64DecodeToString(@NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    byte[] aesEncodeToByteArray(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String aesEncodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    byte[] aesEncodeToBase64ByteArray(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String aesEncodeToBase64String(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @NotNull
    String androidId();

    @Nullable
    String aesDecodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv);

    @Nullable
    String tripleDESDecodeStr(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv);

    @Nullable
    String tripleDESDecodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv);

    @Nullable
    String aesEncodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv);

    @Nullable
    String desDecodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String desBase64DecodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String desEncodeToString(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String desEncodeToBase64String(@NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv);

    @Nullable
    String tripleDESEncodeBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv);

    @Nullable
    String tripleDESEncodeArgsBase64Str(@NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv);

    @Nullable
    String digestHex(@NotNull String data, @NotNull String algorithm);

    @Nullable
    String digestBase64Str(@NotNull String data, @NotNull String algorithm);

    /* JADX INFO: compiled from: JsExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$DefaultImpls.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public static final class DefaultImpls {
        @Nullable
        public static String ajax(@NotNull JsExtensions jsExtensions, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return (String) BuildersKt.runBlocking$default((CoroutineContext) null, new AnonymousClass1(urlStr, jsExtensions, null), 1, (Object) null);
        }

        @NotNull
        public static StrResponse[] ajaxAll(@NotNull JsExtensions jsExtensions, @NotNull String[] urlList) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlList, "urlList");
            return (StrResponse[]) BuildersKt.runBlocking$default((CoroutineContext) null, new C01461(urlList, jsExtensions, null), 1, (Object) null);
        }

        @NotNull
        public static StrResponse connect(@NotNull JsExtensions jsExtensions, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return (StrResponse) BuildersKt.runBlocking$default((CoroutineContext) null, new C01471(urlStr, jsExtensions, null), 1, (Object) null);
        }

        @NotNull
        public static StrResponse connect(@NotNull JsExtensions jsExtensions, @NotNull String urlStr, @Nullable String header) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return (StrResponse) BuildersKt.runBlocking$default((CoroutineContext) null, new AnonymousClass2(header, jsExtensions, urlStr, null), 1, (Object) null);
        }

        @Nullable
        public static String webView(@NotNull JsExtensions jsExtensions, @Nullable String html, @Nullable String url, @Nullable String js) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            return null;
        }

        @NotNull
        public static String importScript(@NotNull JsExtensions jsExtensions, @NotNull String path) throws NoStackTraceException {
            String text$default;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            if (StringsKt.startsWith$default(path, "http", false, 2, (Object) null)) {
                String strCacheFile = jsExtensions.cacheFile(path);
                text$default = strCacheFile == null ? PackageDocumentBase.PREFIX_OPF : strCacheFile;
            } else {
                text$default = StringsKt.startsWith$default(path, "/storage", false, 2, (Object) null) ? FileUtils.readText$default(FileUtils.INSTANCE, path, null, 2, null) : jsExtensions.readTxtFile(path);
            }
            String result = text$default;
            if (StringsKt.isBlank(result)) {
                throw new NoStackTraceException(Intrinsics.stringPlus(path, " 内容获取失败或者为空"));
            }
            return result;
        }

        @Nullable
        public static String cacheFile(@NotNull JsExtensions jsExtensions, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            return jsExtensions.cacheFile(urlStr, 0);
        }

        public static /* synthetic */ String cacheFile$default(JsExtensions jsExtensions, String str, int i, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: cacheFile");
            }
            if ((i2 & 2) != 0) {
                i = 0;
            }
            return jsExtensions.cacheFile(str, i);
        }

        @Nullable
        public static String cacheFile(@NotNull JsExtensions jsExtensions, @NotNull String urlStr, int saveTime) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            String key = jsExtensions.md5Encode16(urlStr);
            CacheManager cacheInstance = new CacheManager(jsExtensions.getUserNameSpace());
            String cache = cacheInstance.getFile(key);
            String str = cache;
            if (str == null || StringsKt.isBlank(str)) {
                jsExtensions.log(Intrinsics.stringPlus("首次下载 ", urlStr));
                String value = jsExtensions.ajax(urlStr);
                if (value == null) {
                    return null;
                }
                cacheInstance.putFile(key, value, saveTime);
                return value;
            }
            return cache;
        }

        public static /* synthetic */ String getCookie$default(JsExtensions jsExtensions, String str, String str2, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getCookie");
            }
            if ((i & 2) != 0) {
                str2 = null;
            }
            return jsExtensions.getCookie(str, str2);
        }

        @NotNull
        public static String getCookie(@NotNull JsExtensions jsExtensions, @NotNull String tag, @Nullable String key) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(tag, "tag");
            CookieStore cookieStore = new CookieStore(jsExtensions.getUserNameSpace());
            String cookie = cookieStore.getCookie(tag);
            Map<String, String> mapCookieToMap = cookieStore.cookieToMap(cookie);
            if (key != null) {
                String str = mapCookieToMap.get(key);
                return str == null ? PackageDocumentBase.PREFIX_OPF : str;
            }
            return cookie;
        }

        @NotNull
        public static String downloadFile(@NotNull JsExtensions jsExtensions, @NotNull String content, @NotNull String url) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(content, "content");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            String type = new AnalyzeUrl(url, null, null, null, null, null, null, null, null, null, null, 2046, null).getType();
            if (type == null) {
                return PackageDocumentBase.PREFIX_OPF;
            }
            String zipPath = FileUtils.INSTANCE.getPath(FileUtils.INSTANCE.createFolderIfNotExist(FileUtils.INSTANCE.getCachePath()), MD5Utils.INSTANCE.md5Encode16(url) + '.' + type);
            FileUtils.INSTANCE.deleteFile(zipPath);
            File zipFile = FileUtils.INSTANCE.createFileIfNotExist(zipPath);
            byte[] it = StringUtils.INSTANCE.hexStringToByte(content);
            if (!(it.length == 0)) {
                FilesKt.writeBytes(zipFile, it);
            }
            int length = FileUtils.INSTANCE.getCachePath().length();
            if (zipPath == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring = zipPath.substring(length);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }

        @NotNull
        public static Connection.Response get(@NotNull JsExtensions jsExtensions, @NotNull String urlStr, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            Intrinsics.checkNotNullParameter(headers, "headers");
            Connection.Response response = Jsoup.connect(urlStr).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory()).ignoreContentType(true).followRedirects(false).headers(headers).method(Connection.Method.GET).execute();
            Map<String, String> mapCookies = response.cookies();
            CookieStore cookieStore = new CookieStore(jsExtensions.getUserNameSpace());
            String it = cookieStore.mapToCookie(mapCookies);
            if (it != null) {
                String domain = NetworkUtils.INSTANCE.getSubDomain(urlStr);
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, "_cookieJar"), it);
            }
            Intrinsics.checkNotNullExpressionValue(response, "response");
            return response;
        }

        @NotNull
        public static Connection.Response head(@NotNull JsExtensions jsExtensions, @NotNull String urlStr, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            Intrinsics.checkNotNullParameter(headers, "headers");
            Connection.Response response = Jsoup.connect(urlStr).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory()).ignoreContentType(true).followRedirects(false).headers(headers).method(Connection.Method.HEAD).execute();
            Map<String, String> mapCookies = response.cookies();
            CookieStore cookieStore = new CookieStore(jsExtensions.getUserNameSpace());
            String it = cookieStore.mapToCookie(mapCookies);
            if (it != null) {
                String domain = NetworkUtils.INSTANCE.getSubDomain(urlStr);
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, "_cookieJar"), it);
            }
            Intrinsics.checkNotNullExpressionValue(response, "response");
            return response;
        }

        @NotNull
        public static Connection.Response post(@NotNull JsExtensions jsExtensions, @NotNull String urlStr, @NotNull String body, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(urlStr, "urlStr");
            Intrinsics.checkNotNullParameter(body, NCXDocumentV3.XHTMLTgs.body);
            Intrinsics.checkNotNullParameter(headers, "headers");
            Connection.Response response = Jsoup.connect(urlStr).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory()).ignoreContentType(true).followRedirects(false).requestBody(body).headers(headers).method(Connection.Method.POST).execute();
            Map<String, String> mapCookies = response.cookies();
            CookieStore cookieStore = new CookieStore(jsExtensions.getUserNameSpace());
            String it = cookieStore.mapToCookie(mapCookies);
            if (it != null) {
                String domain = NetworkUtils.INSTANCE.getSubDomain(urlStr);
                cookieStore.replaceCookie(Intrinsics.stringPlus(domain, "_cookieJar"), it);
            }
            Intrinsics.checkNotNullExpressionValue(response, "response");
            return response;
        }

        @NotNull
        public static String base64Decode(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return EncoderUtils.INSTANCE.base64Decode(str, 2);
        }

        @NotNull
        public static String base64Decode(@NotNull JsExtensions jsExtensions, @NotNull String str, int flags) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return EncoderUtils.INSTANCE.base64Decode(str, flags);
        }

        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull JsExtensions jsExtensions, @Nullable String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            String str2 = str;
            if (str2 == null || StringsKt.isBlank(str2)) {
                return null;
            }
            return Base64.decode(str, 0);
        }

        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull JsExtensions jsExtensions, @Nullable String str, int flags) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            String str2 = str;
            if (str2 == null || StringsKt.isBlank(str2)) {
                return null;
            }
            return Base64.decode(str, flags);
        }

        @Nullable
        public static String base64Encode(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return EncoderUtils.INSTANCE.base64Encode(str, 2);
        }

        @Nullable
        public static String base64Encode(@NotNull JsExtensions jsExtensions, @NotNull String str, int flags) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return EncoderUtils.INSTANCE.base64Encode(str, flags);
        }

        @NotNull
        public static String md5Encode(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return MD5Utils.INSTANCE.md5Encode(str);
        }

        @NotNull
        public static String md5Encode16(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return MD5Utils.INSTANCE.md5Encode16(str);
        }

        @Nullable
        public static String timeFormatUTC(@NotNull JsExtensions jsExtensions, long time, @NotNull String format, int sh) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(format, PackageDocumentBase.DCTags.format);
            SimpleTimeZone utc = new SimpleTimeZone(sh, "UTC");
            SimpleDateFormat receiver = new SimpleDateFormat(format, Locale.getDefault());
            receiver.setTimeZone(utc);
            return receiver.format(new Date(time));
        }

        @NotNull
        public static String timeFormat(@NotNull JsExtensions jsExtensions, long time) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            String str = AppConst.INSTANCE.getDateFormat().format(new Date(time));
            Intrinsics.checkNotNullExpressionValue(str, "dateFormat.format(Date(time))");
            return str;
        }

        @NotNull
        public static String utf8ToGbk(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Charset charsetForName = Charset.forName(Constants.CHARACTER_ENCODING);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "Charset.forName(charsetName)");
            byte[] bytes = str.getBytes(charsetForName);
            Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
            String utf8 = new String(bytes, Charsets.UTF_8);
            byte[] bytes2 = utf8.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes2, "(this as java.lang.String).getBytes(charset)");
            Charset charsetForName2 = Charset.forName(Constants.CHARACTER_ENCODING);
            Intrinsics.checkNotNullExpressionValue(charsetForName2, "Charset.forName(charsetName)");
            String unicode = new String(bytes2, charsetForName2);
            Charset charsetForName3 = Charset.forName("GBK");
            Intrinsics.checkNotNullExpressionValue(charsetForName3, "Charset.forName(charsetName)");
            byte[] bytes3 = unicode.getBytes(charsetForName3);
            Intrinsics.checkNotNullExpressionValue(bytes3, "(this as java.lang.String).getBytes(charset)");
            return new String(bytes3, Charsets.UTF_8);
        }

        @NotNull
        public static String encodeURI(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            String str2;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            try {
                String strEncode = URLEncoder.encode(str, Constants.CHARACTER_ENCODING);
                Intrinsics.checkNotNullExpressionValue(strEncode, "{\n            URLEncoder.encode(str, \"UTF-8\")\n        }");
                str2 = strEncode;
            } catch (Exception e) {
                str2 = PackageDocumentBase.PREFIX_OPF;
            }
            return str2;
        }

        @NotNull
        public static String encodeURI(@NotNull JsExtensions jsExtensions, @NotNull String str, @NotNull String enc) {
            String str2;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(enc, "enc");
            try {
                String strEncode = URLEncoder.encode(str, enc);
                Intrinsics.checkNotNullExpressionValue(strEncode, "{\n            URLEncoder.encode(str, enc)\n        }");
                str2 = strEncode;
            } catch (Exception e) {
                str2 = PackageDocumentBase.PREFIX_OPF;
            }
            return str2;
        }

        @NotNull
        public static String htmlFormat(@NotNull JsExtensions jsExtensions, @NotNull String str) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            return HtmlFormatter.formatKeepImg$default(HtmlFormatter.INSTANCE, str, null, 2, null);
        }

        @NotNull
        public static File getFile(@NotNull JsExtensions jsExtensions, @NotNull String path) {
            String strStringPlus;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            String cachePath = ReaderAdapterHelper.INSTANCE.getAdapter().getCacheDir();
            String str = File.separator;
            Intrinsics.checkNotNullExpressionValue(str, "separator");
            if (StringsKt.startsWith$default(path, str, false, 2, (Object) null)) {
                strStringPlus = Intrinsics.stringPlus(cachePath, path);
            } else {
                strStringPlus = cachePath + ((Object) File.separator) + path;
            }
            String aPath = strStringPlus;
            return new File(aPath);
        }

        @Nullable
        public static byte[] readFile(@NotNull JsExtensions jsExtensions, @NotNull String path) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            File file = jsExtensions.getFile(path);
            if (file.exists()) {
                return FilesKt.readBytes(file);
            }
            return null;
        }

        @NotNull
        public static String readTxtFile(@NotNull JsExtensions jsExtensions, @NotNull String path) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            File file = jsExtensions.getFile(path);
            if (file.exists()) {
                String charsetName = EncodingDetect.INSTANCE.getEncode(file);
                byte[] bytes = FilesKt.readBytes(file);
                Charset charsetForName = Charset.forName(charsetName);
                Intrinsics.checkNotNullExpressionValue(charsetForName, "Charset.forName(charsetName)");
                return new String(bytes, charsetForName);
            }
            return PackageDocumentBase.PREFIX_OPF;
        }

        @NotNull
        public static String readTxtFile(@NotNull JsExtensions jsExtensions, @NotNull String path, @NotNull String charsetName) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            Intrinsics.checkNotNullParameter(charsetName, "charsetName");
            File file = jsExtensions.getFile(path);
            if (file.exists()) {
                byte[] bytes = FilesKt.readBytes(file);
                Charset charsetForName = Charset.forName(charsetName);
                Intrinsics.checkNotNullExpressionValue(charsetForName, "Charset.forName(charsetName)");
                return new String(bytes, charsetForName);
            }
            return PackageDocumentBase.PREFIX_OPF;
        }

        public static void deleteFile(@NotNull JsExtensions jsExtensions, @NotNull String path) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(path, "path");
            File file = jsExtensions.getFile(path);
            FileUtils.INSTANCE.delete(file, true);
        }

        @NotNull
        public static String unzipFile(@NotNull JsExtensions jsExtensions, @NotNull String zipPath) throws IOException {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(zipPath, "zipPath");
            if (zipPath.length() == 0) {
                return PackageDocumentBase.PREFIX_OPF;
            }
            String unzipPath = FileUtils.INSTANCE.getPath(FileUtils.INSTANCE.createFolderIfNotExist(FileUtils.INSTANCE.getCachePath()), FileUtils.INSTANCE.getNameExcludeExtension(zipPath));
            FileUtils.INSTANCE.deleteFile(unzipPath);
            File zipFile = jsExtensions.getFile(zipPath);
            File unzipFolder = FileUtils.INSTANCE.createFolderIfNotExist(unzipPath);
            ZipUtils.INSTANCE.unzipFile(zipFile, unzipFolder);
            FileUtils fileUtils = FileUtils.INSTANCE;
            String absolutePath = zipFile.getAbsolutePath();
            Intrinsics.checkNotNullExpressionValue(absolutePath, "zipFile.absolutePath");
            fileUtils.deleteFile(absolutePath);
            int length = FileUtils.INSTANCE.getCachePath().length();
            if (unzipPath == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring = unzipPath.substring(length);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }

        @NotNull
        public static String getTxtInFolder(@NotNull JsExtensions jsExtensions, @NotNull String unzipPath) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(unzipPath, "unzipPath");
            if (unzipPath.length() == 0) {
                return PackageDocumentBase.PREFIX_OPF;
            }
            File unzipFolder = jsExtensions.getFile(unzipPath);
            StringBuilder contents = new StringBuilder();
            File[] it = unzipFolder.listFiles();
            if (it != null) {
                int i = 0;
                int length = it.length;
                while (i < length) {
                    File f = it[i];
                    i++;
                    EncodingDetect encodingDetect = EncodingDetect.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue(f, "f");
                    String charsetName = encodingDetect.getEncode(f);
                    byte[] bytes = FilesKt.readBytes(f);
                    Charset charsetForName = Charset.forName(charsetName);
                    Intrinsics.checkNotNullExpressionValue(charsetForName, "Charset.forName(charsetName)");
                    contents.append(new String(bytes, charsetForName)).append("\n");
                }
                contents.deleteCharAt(contents.length() - 1);
            }
            FileUtils fileUtils = FileUtils.INSTANCE;
            String absolutePath = unzipFolder.getAbsolutePath();
            Intrinsics.checkNotNullExpressionValue(absolutePath, "unzipFolder.absolutePath");
            fileUtils.deleteFile(absolutePath);
            String string = contents.toString();
            Intrinsics.checkNotNullExpressionValue(string, "contents.toString()");
            return string;
        }

        @NotNull
        public static String getZipStringContent(@NotNull JsExtensions jsExtensions, @NotNull String url, @NotNull String path) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullParameter(path, "path");
            byte[] byteArray = jsExtensions.getZipByteArrayContent(url, path);
            if (byteArray == null) {
                return PackageDocumentBase.PREFIX_OPF;
            }
            String charsetName = EncodingDetect.INSTANCE.getEncode(byteArray);
            Charset charsetForName = Charset.forName(charsetName);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(charsetName)");
            return new String(byteArray, charsetForName);
        }

        @NotNull
        public static String getZipStringContent(@NotNull JsExtensions jsExtensions, @NotNull String url, @NotNull String path, @NotNull String charsetName) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullParameter(path, "path");
            Intrinsics.checkNotNullParameter(charsetName, "charsetName");
            byte[] byteArray = jsExtensions.getZipByteArrayContent(url, path);
            if (byteArray == null) {
                return PackageDocumentBase.PREFIX_OPF;
            }
            Charset charsetForName = Charset.forName(charsetName);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(charsetName)");
            return new String(byteArray, charsetForName);
        }

        @Nullable
        public static byte[] getZipByteArrayContent(@NotNull JsExtensions jsExtensions, @NotNull String url, @NotNull String path) throws IOException {
            byte[] bArrHexStringToByte;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullParameter(path, "path");
            if (StringsKt.startsWith$default(url, "http://", false, 2, (Object) null) || StringsKt.startsWith$default(url, "https://", false, 2, (Object) null)) {
                bArrHexStringToByte = (byte[]) BuildersKt.runBlocking$default((CoroutineContext) null, new JsExtensions$getZipByteArrayContent$bytes$1(url, null), 1, (Object) null);
            } else {
                bArrHexStringToByte = StringUtils.INSTANCE.hexStringToByte(url);
            }
            byte[] bytes = bArrHexStringToByte;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
            ZipEntry nextEntry = zis.getNextEntry();
            while (true) {
                ZipEntry entry = nextEntry;
                if (entry != null) {
                    if (entry.getName().equals(path)) {
                        ZipInputStream zipInputStream = zis;
                        Throwable th = (Throwable) null;
                        try {
                            try {
                                ZipInputStream it = zipInputStream;
                                ByteStreamsKt.copyTo$default(it, bos, 0, 2, (Object) null);
                                CloseableKt.closeFinally(zipInputStream, th);
                                return bos.toByteArray();
                            } finally {
                            }
                        } catch (Throwable th2) {
                            CloseableKt.closeFinally(zipInputStream, th);
                            throw th2;
                        }
                    }
                    nextEntry = zis.getNextEntry();
                } else {
                    Debug.INSTANCE.log("getZipContent 未发现内容");
                    return null;
                }
            }
        }

        @Nullable
        public static QueryTTF queryBase64TTF(@NotNull JsExtensions jsExtensions, @Nullable String base64) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            byte[] it = jsExtensions.base64DecodeToByteArray(base64);
            if (it != null) {
                return new QueryTTF(it);
            }
            return null;
        }

        @Nullable
        public static QueryTTF queryTTF(@NotNull JsExtensions jsExtensions, @Nullable String str) {
            byte[] bytes;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            if (str == null) {
                return null;
            }
            String key = jsExtensions.md5Encode16(str);
            Ref.ObjectRef cacheInstance = new Ref.ObjectRef();
            cacheInstance.element = new CacheManager(jsExtensions.getUserNameSpace());
            QueryTTF qTTF = ((CacheManager) cacheInstance.element).getQueryTTF(key);
            if (qTTF != null) {
                return qTTF;
            }
            if (StringExtensionsKt.isAbsUrl(str)) {
                bytes = (byte[]) BuildersKt.runBlocking$default((CoroutineContext) null, new JsExtensions$queryTTF$font$1(cacheInstance, key, str, null), 1, (Object) null);
            } else {
                bytes = StringsKt.indexOf$default(str, "storage/", 0, false, 6, (Object) null) > 0 ? FilesKt.readBytes(new File(str)) : jsExtensions.base64DecodeToByteArray(str);
            }
            byte[] font = bytes;
            if (font == null) {
                return null;
            }
            QueryTTF qTTF2 = new QueryTTF(font);
            CacheManager.put$default((CacheManager) cacheInstance.element, key, qTTF2, 0, 4, null);
            return qTTF2;
        }

        @NotNull
        public static String replaceFont(@NotNull JsExtensions jsExtensions, @NotNull String text, @Nullable QueryTTF font1, @Nullable QueryTTF font2) {
            int code;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(text, NCXDocumentV2.NCXTags.text);
            if (font1 == null || font2 == null) {
                return text;
            }
            char[] contentArray = text.toCharArray();
            Intrinsics.checkNotNullExpressionValue(contentArray, "(this as java.lang.String).toCharArray()");
            int index$iv = 0;
            for (char item$iv : contentArray) {
                int index = index$iv;
                index$iv++;
                if (font1.inLimit(item$iv) && (code = font2.getCodeByGlyf(font1.getGlyfByCode(item$iv))) != 0) {
                    contentArray[index] = (char) code;
                }
            }
            return ArraysKt.joinToString$default(contentArray, PackageDocumentBase.PREFIX_OPF, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
        }

        public static void toast(@NotNull JsExtensions jsExtensions, @Nullable Object msg) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            DebugLog logger = jsExtensions.getLogger();
            if (logger != null) {
                logger.log(Intrinsics.stringPlus("toast: ", msg));
            }
            Debug.INSTANCE.log(Intrinsics.stringPlus("toast: ", msg));
        }

        public static void longToast(@NotNull JsExtensions jsExtensions, @Nullable Object msg) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            DebugLog logger = jsExtensions.getLogger();
            if (logger != null) {
                logger.log(Intrinsics.stringPlus("longToast: ", msg));
            }
            Debug.INSTANCE.log(Intrinsics.stringPlus("longToast: ", msg));
        }

        @NotNull
        public static String log(@NotNull JsExtensions jsExtensions, @NotNull String msg) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(msg, "msg");
            DebugLog logger = jsExtensions.getLogger();
            if (logger != null) {
                logger.log(msg);
            }
            Debug.INSTANCE.log(msg);
            return msg;
        }

        public static void logType(@NotNull JsExtensions jsExtensions, @Nullable Object any) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            if (any == null) {
                jsExtensions.log("null");
                return;
            }
            String name = any.getClass().getName();
            Intrinsics.checkNotNullExpressionValue(name, "any.javaClass.name");
            jsExtensions.log(name);
        }

        @NotNull
        public static String randomUUID(@NotNull JsExtensions jsExtensions) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            String string = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(string, "randomUUID().toString()");
            return string;
        }

        @Nullable
        public static byte[] aesDecodeToByteArray(@NotNull JsExtensions jsExtensions, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            byte[] bArrDecryptAES;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            try {
                bArrDecryptAES = EncoderUtils.INSTANCE.decryptAES(StringsKt.encodeToByteArray(str), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            } catch (Exception e) {
                LogUtilsKt.printOnDebug(e);
                String localizedMessage = e.getLocalizedMessage();
                jsExtensions.log(localizedMessage == null ? "aesDecodeToByteArrayERROR" : localizedMessage);
                bArrDecryptAES = (byte[]) null;
            }
            return bArrDecryptAES;
        }

        @Nullable
        public static String aesDecodeToString(@NotNull JsExtensions jsExtensions, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = jsExtensions.aesDecodeToByteArray(str, key, transformation, iv);
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static byte[] aesBase64DecodeToByteArray(@NotNull JsExtensions jsExtensions, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            byte[] bArrDecryptBase64AES;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            try {
                bArrDecryptBase64AES = EncoderUtils.INSTANCE.decryptBase64AES(StringsKt.encodeToByteArray(str), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            } catch (Exception e) {
                LogUtilsKt.printOnDebug(e);
                String localizedMessage = e.getLocalizedMessage();
                jsExtensions.log(localizedMessage == null ? "aesDecodeToByteArrayERROR" : localizedMessage);
                bArrDecryptBase64AES = (byte[]) null;
            }
            return bArrDecryptBase64AES;
        }

        @Nullable
        public static String aesBase64DecodeToString(@NotNull JsExtensions jsExtensions, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(str, "str");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = jsExtensions.aesBase64DecodeToByteArray(str, key, transformation, iv);
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static byte[] aesEncodeToByteArray(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            byte[] bArrEncryptAES;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            try {
                bArrEncryptAES = EncoderUtils.INSTANCE.encryptAES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            } catch (Exception e) {
                LogUtilsKt.printOnDebug(e);
                String localizedMessage = e.getLocalizedMessage();
                jsExtensions.log(localizedMessage == null ? "aesEncodeToByteArrayERROR" : localizedMessage);
                bArrEncryptAES = (byte[]) null;
            }
            return bArrEncryptAES;
        }

        @Nullable
        public static String aesEncodeToString(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = jsExtensions.aesEncodeToByteArray(data, key, transformation, iv);
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static byte[] aesEncodeToBase64ByteArray(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            byte[] bArrEncryptAES2Base64;
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            try {
                bArrEncryptAES2Base64 = EncoderUtils.INSTANCE.encryptAES2Base64(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            } catch (Exception e) {
                LogUtilsKt.printOnDebug(e);
                String localizedMessage = e.getLocalizedMessage();
                jsExtensions.log(localizedMessage == null ? "aesEncodeToBase64ByteArrayERROR" : localizedMessage);
                bArrEncryptAES2Base64 = (byte[]) null;
            }
            return bArrEncryptAES2Base64;
        }

        @Nullable
        public static String aesEncodeToBase64String(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = jsExtensions.aesEncodeToBase64ByteArray(data, key, transformation, iv);
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @NotNull
        public static String androidId(@NotNull JsExtensions jsExtensions) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            return PackageDocumentBase.PREFIX_OPF;
        }

        @Nullable
        public static String aesDecodeArgsBase64Str(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return new AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data);
        }

        @Nullable
        public static String tripleDESDecodeStr(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] bytes = key.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
            byte[] bytes2 = iv.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes2, "(this as java.lang.String).getBytes(charset)");
            return new DESede(mode, padding, bytes, bytes2).decryptStr(data);
        }

        @Nullable
        public static String tripleDESDecodeArgsBase64Str(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return new DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data);
        }

        @Nullable
        public static String aesEncodeArgsBase64Str(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return new AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data);
        }

        @Nullable
        public static String desDecodeToString(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) throws Exception {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = EncoderUtils.INSTANCE.decryptDES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static String desBase64DecodeToString(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) throws Exception {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = EncoderUtils.INSTANCE.decryptBase64DES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static String desEncodeToString(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) throws Exception {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = EncoderUtils.INSTANCE.encryptDES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static String desEncodeToBase64String(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) throws Exception {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(transformation, "transformation");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] it = EncoderUtils.INSTANCE.encryptDES2Base64(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
            if (it == null) {
                return null;
            }
            return new String(it, Charsets.UTF_8);
        }

        @Nullable
        public static String tripleDESEncodeBase64Str(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            byte[] bytes = key.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
            byte[] bytes2 = iv.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes2, "(this as java.lang.String).getBytes(charset)");
            return new DESede(mode, padding, bytes, bytes2).encryptBase64(data);
        }

        @Nullable
        public static String tripleDESEncodeArgsBase64Str(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(mode, "mode");
            Intrinsics.checkNotNullParameter(padding, "padding");
            Intrinsics.checkNotNullParameter(iv, "iv");
            return new DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data);
        }

        @Nullable
        public static String digestHex(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String algorithm) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(algorithm, "algorithm");
            return DigestUtil.digester(algorithm).digestHex(data);
        }

        @Nullable
        public static String digestBase64Str(@NotNull JsExtensions jsExtensions, @NotNull String data, @NotNull String algorithm) {
            Intrinsics.checkNotNullParameter(jsExtensions, "this");
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(algorithm, "algorithm");
            return Base64.encodeToString(DigestUtil.digester(algorithm).digest(data), 2);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.JsExtensions$ajax$1, reason: invalid class name */
    /* JADX INFO: compiled from: JsExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$ajax$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "JsExtensions.kt", l = {}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.JsExtensions$ajax$1")
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super String>, Object> {
        int label;
        final /* synthetic */ String $urlStr;
        final /* synthetic */ JsExtensions this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(String $urlStr, JsExtensions this$0, Continuation<? super AnonymousClass1> $completion) {
            super(2, $completion);
            this.$urlStr = $urlStr;
            this.this$0 = this$0;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new AnonymousClass1(this.$urlStr, this.this$0, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super String> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object obj;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    String str = this.$urlStr;
                    JsExtensions jsExtensions = this.this$0;
                    try {
                        Result.Companion companion = Result.Companion;
                        AnalyzeUrl analyzeUrl = new AnalyzeUrl(str, null, null, null, null, null, jsExtensions.getSource(), null, null, null, jsExtensions.getLogger(), 958, null);
                        obj = Result.constructor-impl(AnalyzeUrl.getStrResponse$default(analyzeUrl, str, null, false, 6, null).getBody());
                        break;
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.Companion;
                        obj = Result.constructor-impl(ResultKt.createFailure(th));
                    }
                    Object obj2 = obj;
                    Throwable it = Result.exceptionOrNull-impl(obj2);
                    if (it != null) {
                        LogUtilsKt.printOnDebug(it);
                    }
                    Throwable it2 = Result.exceptionOrNull-impl(obj2);
                    return it2 == null ? obj2 : ThrowableExtensionsKt.getMsg(it2);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.JsExtensions$ajaxAll$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: JsExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$ajaxAll$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/help/http/StrResponse;", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "JsExtensions.kt", l = {75}, i = {0}, s = {"L$0"}, n = {"asyncArray"}, m = "invokeSuspend", c = "io.legado.app.help.JsExtensions$ajaxAll$1")
    static final class C01461 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super StrResponse[]>, Object> {
        Object L$1;
        Object L$2;
        int I$0;
        int I$1;
        int I$2;
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ String[] $urlList;
        final /* synthetic */ JsExtensions this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01461(String[] $urlList, JsExtensions this$0, Continuation<? super C01461> $completion) {
            super(2, $completion);
            this.$urlList = $urlList;
            this.this$0 = this$0;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c01461 = new C01461(this.$urlList, this.this$0, $completion);
            c01461.L$0 = value;
            return c01461;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse[]> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            int length;
            StrResponse[] strResponseArr;
            Deferred[] asyncArray;
            int i;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    CoroutineScope $this$runBlocking = (CoroutineScope) this.L$0;
                    int length2 = this.$urlList.length;
                    Deferred[] deferredArr = new Deferred[length2];
                    for (int i2 = 0; i2 < length2; i2++) {
                        int i3 = i2;
                        deferredArr[i3] = BuildersKt.async$default($this$runBlocking, Dispatchers.getIO(), (CoroutineStart) null, new JsExtensions$ajaxAll$1$asyncArray$1$1(this.$urlList, i3, this.this$0, null), 2, (Object) null);
                    }
                    asyncArray = deferredArr;
                    i = 0;
                    length = this.$urlList.length;
                    strResponseArr = new StrResponse[length];
                    break;
                case 1:
                    int i4 = this.I$2;
                    length = this.I$1;
                    int i5 = this.I$0;
                    StrResponse[] strResponseArr2 = (StrResponse[]) this.L$2;
                    strResponseArr = (StrResponse[]) this.L$1;
                    asyncArray = (Deferred[]) this.L$0;
                    ResultKt.throwOnFailure($result);
                    strResponseArr2[i4] = (StrResponse) $result;
                    i = i5 + 1;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            while (i < length) {
                int i6 = i;
                StrResponse[] strResponseArr3 = strResponseArr;
                this.L$0 = asyncArray;
                this.L$1 = strResponseArr;
                this.L$2 = strResponseArr3;
                this.I$0 = i;
                this.I$1 = length;
                this.I$2 = i6;
                this.label = 1;
                Object objAwait = asyncArray[i6].await((Continuation) this);
                if (objAwait == coroutine_suspended) {
                    return coroutine_suspended;
                }
                strResponseArr3[i6] = (StrResponse) objAwait;
                i++;
            }
            StrResponse[] resArray = strResponseArr;
            return resArray;
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.JsExtensions$connect$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: JsExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$connect$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "Lio/legado/app/help/http/StrResponse;", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "JsExtensions.kt", l = {88}, i = {0}, s = {"L$0"}, n = {"analyzeUrl"}, m = "invokeSuspend", c = "io.legado.app.help.JsExtensions$connect$1")
    static final class C01471 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super StrResponse>, Object> {
        Object L$0;
        int label;
        final /* synthetic */ String $urlStr;
        final /* synthetic */ JsExtensions this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01471(String $urlStr, JsExtensions this$0, Continuation<? super C01471> $completion) {
            super(2, $completion);
            this.$urlStr = $urlStr;
            this.this$0 = this$0;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new C01471(this.$urlStr, this.this$0, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object obj;
            AnalyzeUrl analyzeUrl;
            Object strResponseAwait$default;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        analyzeUrl = new AnalyzeUrl(this.$urlStr, null, null, null, null, null, this.this$0.getSource(), null, null, null, this.this$0.getLogger(), 958, null);
                        Result.Companion companion = Result.Companion;
                        this.L$0 = analyzeUrl;
                        this.label = 1;
                        strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, this, 7, null);
                        if (strResponseAwait$default == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        analyzeUrl = (AnalyzeUrl) this.L$0;
                        ResultKt.throwOnFailure($result);
                        strResponseAwait$default = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                obj = Result.constructor-impl((StrResponse) strResponseAwait$default);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            Throwable it = Result.exceptionOrNull-impl(obj2);
            if (it != null) {
                LogUtilsKt.printOnDebug(it);
            }
            Throwable it2 = Result.exceptionOrNull-impl(obj2);
            return it2 == null ? obj2 : new StrResponse(analyzeUrl.getUrl(), it2.getLocalizedMessage());
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.JsExtensions$connect$2, reason: invalid class name */
    /* JADX INFO: compiled from: JsExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$connect$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "Lio/legado/app/help/http/StrResponse;", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "JsExtensions.kt", l = {102}, i = {0}, s = {"L$0"}, n = {"analyzeUrl"}, m = "invokeSuspend", c = "io.legado.app.help.JsExtensions$connect$2")
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super StrResponse>, Object> {
        Object L$0;
        int label;
        final /* synthetic */ String $header;
        final /* synthetic */ JsExtensions this$0;
        final /* synthetic */ String $urlStr;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String $header, JsExtensions this$0, String $urlStr, Continuation<? super AnonymousClass2> $completion) {
            super(2, $completion);
            this.$header = $header;
            this.this$0 = this$0;
            this.$urlStr = $urlStr;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new AnonymousClass2(this.$header, this.this$0, this.$urlStr, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object obj;
            AnalyzeUrl analyzeUrl;
            Object strResponseAwait$default;
            Object obj2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
                        String json$iv = this.$header;
                        try {
                            Result.Companion companion = Result.Companion;
                            Type type = new JsExtensions$connect$2$invokeSuspend$$inlined$fromJsonObject$1().getType();
                            Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                            Object objFromJson = $this$fromJsonObject$iv.fromJson(json$iv, type);
                            if (!(objFromJson instanceof Map)) {
                                objFromJson = null;
                            }
                            obj2 = Result.constructor-impl((Map) objFromJson);
                        } catch (Throwable th) {
                            Result.Companion companion2 = Result.Companion;
                            obj2 = Result.constructor-impl(ResultKt.createFailure(th));
                        }
                        Object obj3 = obj2;
                        Map headerMap = (Map) (Result.isFailure-impl(obj3) ? null : obj3);
                        analyzeUrl = new AnalyzeUrl(this.$urlStr, null, null, null, null, null, this.this$0.getSource(), null, null, headerMap, this.this$0.getLogger(), 446, null);
                        Result.Companion companion3 = Result.Companion;
                        this.L$0 = analyzeUrl;
                        this.label = 1;
                        strResponseAwait$default = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, this, 7, null);
                        if (strResponseAwait$default == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        analyzeUrl = (AnalyzeUrl) this.L$0;
                        ResultKt.throwOnFailure($result);
                        strResponseAwait$default = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                obj = Result.constructor-impl((StrResponse) strResponseAwait$default);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th2));
            }
            Object obj4 = obj;
            Throwable it = Result.exceptionOrNull-impl(obj4);
            if (it != null) {
                LogUtilsKt.printOnDebug(it);
            }
            Throwable it2 = Result.exceptionOrNull-impl(obj4);
            return it2 == null ? obj4 : new StrResponse(analyzeUrl.getUrl(), it2.getLocalizedMessage());
        }
    }
}
