package io.legado.app.adapters;

import io.legado.app.constant.RSSKeywords;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import java.util.Map;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: ReaderAdapterInterface.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/adapters/ReaderAdapterInterface.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J©\u0001\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\u0016\b\u0002\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014H¦@ø\u0001\u0000¢\u0006\u0002\u0010\u0015J!\u0010\u0016\u001a\u00020\u00032\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u0018\"\u00020\u0003H&¢\u0006\u0002\u0010\u0019J\u0012\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u0003H&\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001b"}, d2 = {"Lio/legado/app/adapters/ReaderAdapterInterface;", PackageDocumentBase.PREFIX_OPF, "getCacheDir", PackageDocumentBase.PREFIX_OPF, "getStrResponseByRemoteWebview", "Lio/legado/app/help/http/StrResponse;", RSSKeywords.RSS_ITEM_URL, "html", "encode", "tag", "headerMap", PackageDocumentBase.PREFIX_OPF, "sourceRegex", "javaScript", "proxy", "post", PackageDocumentBase.PREFIX_OPF, NCXDocumentV3.XHTMLTgs.body, "userNameSpace", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWorkDir", "subDirFiles", PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/String;)Ljava/lang/String;", "subPath", "reader-pro"})
public interface ReaderAdapterInterface {
    @NotNull
    String getWorkDir(@NotNull String subPath);

    @NotNull
    String getWorkDir(@NotNull String... subDirFiles);

    @NotNull
    String getCacheDir();

    @Nullable
    Object getStrResponseByRemoteWebview(@Nullable String url, @Nullable String html, @Nullable String encode, @Nullable String tag, @Nullable Map<String, String> headerMap, @Nullable String sourceRegex, @Nullable String javaScript, @Nullable String proxy, boolean post, @Nullable String body, @NotNull String userNameSpace, @Nullable DebugLog debugLog, @NotNull Continuation<? super StrResponse> $completion);

    /* JADX INFO: compiled from: ReaderAdapterInterface.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/adapters/ReaderAdapterInterface$DefaultImpls.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ String getWorkDir$default(ReaderAdapterInterface readerAdapterInterface, String str, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getWorkDir");
            }
            if ((i & 1) != 0) {
                str = PackageDocumentBase.PREFIX_OPF;
            }
            return readerAdapterInterface.getWorkDir(str);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Object getStrResponseByRemoteWebview$default(ReaderAdapterInterface readerAdapterInterface, String str, String str2, String str3, String str4, Map map, String str5, String str6, String str7, boolean z, String str8, String str9, DebugLog debugLog, Continuation continuation, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getStrResponseByRemoteWebview");
            }
            if ((i & 1) != 0) {
                str = null;
            }
            if ((i & 2) != 0) {
                str2 = null;
            }
            if ((i & 4) != 0) {
                str3 = null;
            }
            if ((i & 8) != 0) {
                str4 = null;
            }
            if ((i & 16) != 0) {
                map = null;
            }
            if ((i & 32) != 0) {
                str5 = null;
            }
            if ((i & 64) != 0) {
                str6 = null;
            }
            if ((i & Wbxml.EXT_T_0) != 0) {
                str7 = null;
            }
            if ((i & 256) != 0) {
                z = false;
            }
            if ((i & 512) != 0) {
                str8 = null;
            }
            if ((i & 1024) != 0) {
                str9 = PackageDocumentBase.PREFIX_OPF;
            }
            if ((i & 2048) != 0) {
                debugLog = null;
            }
            return readerAdapterInterface.getStrResponseByRemoteWebview(str, str2, str3, str4, map, str5, str6, str7, z, str8, str9, debugLog, continuation);
        }
    }
}
