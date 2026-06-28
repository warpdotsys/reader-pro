package io.legado.app.adapters;

import io.legado.app.constant.RSSKeywords;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: DefaultAdapter.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/adapters/DefaultAdpater.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u001f\u0010\u0005\u001a\u00020\u00042\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0007\"\u00020\u0004¢\u0006\u0002\u0010\bJ\u0091\u0001\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00042\b\u0010\f\u001a\u0004\u0018\u00010\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00042\b\u0010\u0012\u001a\u0004\u0018\u00010\u00042\b\u0010\u0013\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0017\u001a\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u001aJ!\u0010\u001b\u001a\u00020\u00042\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0007\"\u00020\u0004H\u0016¢\u0006\u0002\u0010\bJ\u0010\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u0004H\u0016\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001d"}, d2 = {"Lio/legado/app/adapters/DefaultAdpater;", "Lio/legado/app/adapters/ReaderAdapterInterface;", "()V", "getCacheDir", PackageDocumentBase.PREFIX_OPF, "getRelativePath", "subDirFiles", PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/String;)Ljava/lang/String;", "getStrResponseByRemoteWebview", "Lio/legado/app/help/http/StrResponse;", RSSKeywords.RSS_ITEM_URL, "html", "encode", "tag", "headerMap", PackageDocumentBase.PREFIX_OPF, "sourceRegex", "javaScript", "proxy", "post", PackageDocumentBase.PREFIX_OPF, NCXDocumentV3.XHTMLTgs.body, "userNameSpace", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWorkDir", "subPath", "reader-pro"})
public final class DefaultAdpater implements ReaderAdapterInterface {
    /* JADX WARN: Removed duplicated region for block: B:7:0x0060  */
    @Override // io.legado.app.adapters.ReaderAdapterInterface
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getWorkDir(@NotNull String subPath) {
        String workDirPath;
        Intrinsics.checkNotNullParameter(subPath, "subPath");
        String osName = System.getProperty("os.name");
        String currentDir = System.getProperty("user.dir");
        Intrinsics.checkNotNullExpressionValue(osName, "osName");
        if (StringsKt.startsWith(osName, "Mac OS", true)) {
            Intrinsics.checkNotNullExpressionValue(currentDir, "currentDir");
            if (StringsKt.startsWith$default(currentDir, "/Users/", false, 2, (Object) null)) {
                Intrinsics.checkNotNullExpressionValue(currentDir, "currentDir");
                workDirPath = currentDir;
            } else {
                workDirPath = Paths.get(System.getProperty("user.home"), ".reader").toString();
            }
        }
        Path path = Paths.get(workDirPath, subPath);
        return path.toString();
    }

    @Override // io.legado.app.adapters.ReaderAdapterInterface
    @NotNull
    public String getWorkDir(@NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        return getWorkDir(getRelativePath((String[]) Arrays.copyOf(subDirFiles, subDirFiles.length)));
    }

    @NotNull
    public final String getRelativePath(@NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        StringBuilder path = new StringBuilder(PackageDocumentBase.PREFIX_OPF);
        for (String str : subDirFiles) {
            if (str.length() > 0) {
                path.append(File.separator).append(str);
            }
        }
        String it = path.toString();
        Intrinsics.checkNotNullExpressionValue(it, "it");
        if (StringsKt.startsWith$default(it, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
            String strSubstring = it.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }
        return it;
    }

    @Override // io.legado.app.adapters.ReaderAdapterInterface
    @NotNull
    public String getCacheDir() {
        return getWorkDir("storage", "cache");
    }

    @Override // io.legado.app.adapters.ReaderAdapterInterface
    @Nullable
    public Object getStrResponseByRemoteWebview(@Nullable String url, @Nullable String html, @Nullable String encode, @Nullable String tag, @Nullable Map<String, String> headerMap, @Nullable String sourceRegex, @Nullable String javaScript, @Nullable String proxy, boolean post, @Nullable String body, @NotNull String userNameSpace, @Nullable DebugLog debugLog, @NotNull Continuation<? super StrResponse> $completion) throws Exception {
        throw new Exception("不支持webview");
    }
}
