// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.adapters;

import io.legado.app.help.http.StrResponse;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u001f\u0010\u0005\u001a\u00020\u00042\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0007\"\u00020\u0004?\u0006\u0002\u0010\bJ\u0091\u0001\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00042\b\u0010\f\u001a\u0004\u0018\u00010\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00042\b\u0010\u0012\u001a\u0004\u0018\u00010\u00042\b\u0010\u0013\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0017\u001a\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u001aJ!\u0010\u001b\u001a\u00020\u00042\u0012\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0007\"\u00020\u0004H\u0016?\u0006\u0002\u0010\bJ\u0010\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u0004H\u0016\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u001d" }, d2 = { "Lio/legado/app/adapters/DefaultAdpater;", "Lio/legado/app/adapters/ReaderAdapterInterface;", "()V", "getCacheDir", "", "getRelativePath", "subDirFiles", "", "([Ljava/lang/String;)Ljava/lang/String;", "getStrResponseByRemoteWebview", "Lio/legado/app/help/http/StrResponse;", "url", "html", "encode", "tag", "headerMap", "", "sourceRegex", "javaScript", "proxy", "post", "", "body", "userNameSpace", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWorkDir", "subPath", "reader-pro" })
public final class DefaultAdpater implements ReaderAdapterInterface
{
    @NotNull
    public String getWorkDir(@NotNull final String subPath) {
        Intrinsics.checkNotNullParameter((Object)subPath, "subPath");
        String workDirPath = "";
        final String osName = System.getProperty("os.name");
        final String currentDir = System.getProperty("user.dir");
        final String s = osName;
        Intrinsics.checkNotNullExpressionValue((Object)s, "osName");
        Label_0110: {
            if (StringsKt.startsWith(s, "Mac OS", true)) {
                final String s2 = currentDir;
                Intrinsics.checkNotNullExpressionValue((Object)s2, "currentDir");
                if (!StringsKt.startsWith$default(s2, "/Users/", false, 2, (Object)null)) {
                    workDirPath = Paths.get(System.getProperty("user.home"), ".reader").toString();
                    break Label_0110;
                }
            }
            final String s3 = currentDir;
            Intrinsics.checkNotNullExpressionValue((Object)s3, "currentDir");
            workDirPath = s3;
        }
        final Path path = Paths.get(workDirPath, subPath);
        return path.toString();
    }
    
    @NotNull
    public String getWorkDir(@NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        return this.getWorkDir(this.getRelativePath((String[])Arrays.copyOf(subDirFiles, subDirFiles.length)));
    }
    
    @NotNull
    public final String getRelativePath(@NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        final StringBuilder path = new StringBuilder("");
        final Object[] $this$forEach$iv = subDirFiles;
        final int $i$f$forEach = 0;
        for (final String it : $this$forEach$iv) {
            final Object element$iv = it;
            final int n = 0;
            if (it.length() > 0) {
                path.append(File.separator).append(it);
            }
        }
        final String it2 = path.toString();
        final int n2 = 0;
        Intrinsics.checkNotNullExpressionValue((Object)it2, "it");
        String substring;
        if (StringsKt.startsWith$default(it2, "/", false, 2, (Object)null)) {
            Intrinsics.checkNotNullExpressionValue((Object)(substring = it2.substring(1)), "(this as java.lang.String).substring(startIndex)");
        }
        else {
            substring = it2;
        }
        return substring;
    }
    
    @NotNull
    public String getCacheDir() {
        return this.getWorkDir("storage", "cache");
    }
    
    @Nullable
    public Object getStrResponseByRemoteWebview(@Nullable final String url, @Nullable final String html, @Nullable final String encode, @Nullable final String tag, @Nullable final Map<String, String> headerMap, @Nullable final String sourceRegex, @Nullable final String javaScript, @Nullable final String proxy, final boolean post, @Nullable final String body, @NotNull final String userNameSpace, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super StrResponse> $completion) {
        throw new Exception("\u4e0d\u652f\u6301webview");
    }
}
