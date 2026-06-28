package io.legado.app.help.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: SSLHelper.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/SSLHelper$unsafeTrustManager$1.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J#\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016¢\u0006\u0002\u0010\tJ#\u0010\n\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016¢\u0006\u0002\u0010\tJ\u0013\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¢\u0006\u0002\u0010\f¨\u0006\r"}, d2 = {"io/legado/app/help/http/SSLHelper$unsafeTrustManager$1", "Ljavax/net/ssl/X509TrustManager;", "checkClientTrusted", PackageDocumentBase.PREFIX_OPF, "chain", PackageDocumentBase.PREFIX_OPF, "Ljava/security/cert/X509Certificate;", "authType", PackageDocumentBase.PREFIX_OPF, "([Ljava/security/cert/X509Certificate;Ljava/lang/String;)V", "checkServerTrusted", "getAcceptedIssuers", "()[Ljava/security/cert/X509Certificate;", "reader-pro"})
public final class SSLHelper$unsafeTrustManager$1 implements X509TrustManager {
    SSLHelper$unsafeTrustManager$1() {
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(@NotNull X509Certificate[] chain, @NotNull String authType) throws CertificateException {
        Intrinsics.checkNotNullParameter(chain, "chain");
        Intrinsics.checkNotNullParameter(authType, "authType");
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(@NotNull X509Certificate[] chain, @NotNull String authType) throws CertificateException {
        Intrinsics.checkNotNullParameter(chain, "chain");
        Intrinsics.checkNotNullParameter(authType, "authType");
    }

    @Override // javax.net.ssl.X509TrustManager
    @NotNull
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
