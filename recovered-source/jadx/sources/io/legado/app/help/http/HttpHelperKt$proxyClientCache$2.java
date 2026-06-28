package io.legado.app.help.http;

import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: HttpHelper.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/HttpHelperKt$proxyClientCache$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001H\n"}, d2 = {"<anonymous>", "Ljava/util/concurrent/ConcurrentHashMap;", PackageDocumentBase.PREFIX_OPF, "Lokhttp3/OkHttpClient;"})
final class HttpHelperKt$proxyClientCache$2 extends Lambda implements Function0<ConcurrentHashMap<String, OkHttpClient>> {
    public static final HttpHelperKt$proxyClientCache$2 INSTANCE = new HttpHelperKt$proxyClientCache$2();

    HttpHelperKt$proxyClientCache$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final ConcurrentHashMap<String, OkHttpClient> m186invoke() {
        return new ConcurrentHashMap<>();
    }
}
