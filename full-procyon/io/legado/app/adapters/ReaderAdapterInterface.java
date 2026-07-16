// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.adapters;

import io.legado.app.help.http.StrResponse;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J?\u0001\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\u0016\b\u0002\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014H?@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0015J!\u0010\u0016\u001a\u00020\u00032\u0012\u0010\u0017\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u0018\"\u00020\u0003H&?\u0006\u0002\u0010\u0019J\u0012\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u0003H&\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u001b" }, d2 = { "Lio/legado/app/adapters/ReaderAdapterInterface;", "", "getCacheDir", "", "getStrResponseByRemoteWebview", "Lio/legado/app/help/http/StrResponse;", "url", "html", "encode", "tag", "headerMap", "", "sourceRegex", "javaScript", "proxy", "post", "", "body", "userNameSpace", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getWorkDir", "subDirFiles", "", "([Ljava/lang/String;)Ljava/lang/String;", "subPath", "reader-pro" })
public interface ReaderAdapterInterface
{
    @NotNull
    String getWorkDir(@NotNull final String subPath);
    
    @NotNull
    String getWorkDir(@NotNull final String... subDirFiles);
    
    @NotNull
    String getCacheDir();
    
    @Nullable
    Object getStrResponseByRemoteWebview(@Nullable final String url, @Nullable final String html, @Nullable final String encode, @Nullable final String tag, @Nullable final Map<String, String> headerMap, @Nullable final String sourceRegex, @Nullable final String javaScript, @Nullable final String proxy, final boolean post, @Nullable final String body, @NotNull final String userNameSpace, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super StrResponse> $completion);
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
    }
}
