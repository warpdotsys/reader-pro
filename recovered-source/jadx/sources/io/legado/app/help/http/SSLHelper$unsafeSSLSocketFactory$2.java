package io.legado.app.help.http;

import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: compiled from: SSLHelper.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/SSLHelper$unsafeSSLSocketFactory$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n"}, d2 = {"<anonymous>", "Ljavax/net/ssl/SSLSocketFactory;", "kotlin.jvm.PlatformType"})
final class SSLHelper$unsafeSSLSocketFactory$2 extends Lambda implements Function0<SSLSocketFactory> {
    public static final SSLHelper$unsafeSSLSocketFactory$2 INSTANCE = new SSLHelper$unsafeSSLSocketFactory$2();

    SSLHelper$unsafeSSLSocketFactory$2() {
        super(0);
    }

    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final SSLSocketFactory m191invoke() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{SSLHelper.INSTANCE.getUnsafeTrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
