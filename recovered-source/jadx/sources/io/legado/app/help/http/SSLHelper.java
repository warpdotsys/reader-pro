package io.legado.app.help.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: SSLHelper.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/SSLHelper.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001)B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001b\u0010\u0015\u001a\u00020\u00122\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002¢\u0006\u0002\u0010\u0019J \u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0012J1\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001b¢\u0006\u0002\u0010 J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001e\u001a\u00020\u0012J!\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001b¢\u0006\u0002\u0010!JA\u0010\"\u001a\u0004\u0018\u00010\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u00122\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001bH\u0002¢\u0006\u0002\u0010#J)\u0010$\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010\u00172\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0002¢\u0006\u0002\u0010&J'\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001bH\u0002¢\u0006\u0002\u0010(R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001b\u0010\u000b\u001a\u00020\f8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006*"}, d2 = {"Lio/legado/app/help/http/SSLHelper;", PackageDocumentBase.PREFIX_OPF, "()V", "sslSocketFactory", "Lio/legado/app/help/http/SSLHelper$SSLParams;", "getSslSocketFactory", "()Lio/legado/app/help/http/SSLHelper$SSLParams;", "unsafeHostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "getUnsafeHostnameVerifier", "()Ljavax/net/ssl/HostnameVerifier;", "unsafeSSLSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "getUnsafeSSLSocketFactory", "()Ljavax/net/ssl/SSLSocketFactory;", "unsafeSSLSocketFactory$delegate", "Lkotlin/Lazy;", "unsafeTrustManager", "Ljavax/net/ssl/X509TrustManager;", "getUnsafeTrustManager", "()Ljavax/net/ssl/X509TrustManager;", "chooseTrustManager", "trustManagers", PackageDocumentBase.PREFIX_OPF, "Ljavax/net/ssl/TrustManager;", "([Ljavax/net/ssl/TrustManager;)Ljavax/net/ssl/X509TrustManager;", "bksFile", "Ljava/io/InputStream;", "password", PackageDocumentBase.PREFIX_OPF, "trustManager", "certificates", "(Ljava/io/InputStream;Ljava/lang/String;[Ljava/io/InputStream;)Lio/legado/app/help/http/SSLHelper$SSLParams;", "([Ljava/io/InputStream;)Lio/legado/app/help/http/SSLHelper$SSLParams;", "getSslSocketFactoryBase", "(Ljavax/net/ssl/X509TrustManager;Ljava/io/InputStream;Ljava/lang/String;[Ljava/io/InputStream;)Lio/legado/app/help/http/SSLHelper$SSLParams;", "prepareKeyManager", "Ljavax/net/ssl/KeyManager;", "(Ljava/io/InputStream;Ljava/lang/String;)[Ljavax/net/ssl/KeyManager;", "prepareTrustManager", "([Ljava/io/InputStream;)[Ljavax/net/ssl/TrustManager;", "SSLParams", "reader-pro"})
public final class SSLHelper {

    @NotNull
    public static final SSLHelper INSTANCE = new SSLHelper();

    @NotNull
    private static final X509TrustManager unsafeTrustManager = new SSLHelper$unsafeTrustManager$1();

    /* JADX INFO: renamed from: unsafeSSLSocketFactory$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy unsafeSSLSocketFactory = LazyKt.lazy(SSLHelper$unsafeSSLSocketFactory$2.INSTANCE);

    @NotNull
    private static final HostnameVerifier unsafeHostnameVerifier = SSLHelper::m188unsafeHostnameVerifier$lambda0;

    private SSLHelper() {
    }

    @Nullable
    public final SSLParams getSslSocketFactory() {
        return getSslSocketFactoryBase(null, null, null, new InputStream[0]);
    }

    @NotNull
    public final X509TrustManager getUnsafeTrustManager() {
        return unsafeTrustManager;
    }

    @NotNull
    public final SSLSocketFactory getUnsafeSSLSocketFactory() {
        Object value = unsafeSSLSocketFactory.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-unsafeSSLSocketFactory>(...)");
        return (SSLSocketFactory) value;
    }

    @NotNull
    public final HostnameVerifier getUnsafeHostnameVerifier() {
        return unsafeHostnameVerifier;
    }

    /* JADX INFO: renamed from: unsafeHostnameVerifier$lambda-0, reason: not valid java name */
    private static final boolean m188unsafeHostnameVerifier$lambda0(String $noName_0, SSLSession $noName_1) {
        return true;
    }

    /* JADX INFO: compiled from: SSLHelper.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/SSLHelper$SSLParams.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lio/legado/app/help/http/SSLHelper$SSLParams;", PackageDocumentBase.PREFIX_OPF, "()V", "sSLSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "getSSLSocketFactory", "()Ljavax/net/ssl/SSLSocketFactory;", "setSSLSocketFactory", "(Ljavax/net/ssl/SSLSocketFactory;)V", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "getTrustManager", "()Ljavax/net/ssl/X509TrustManager;", "setTrustManager", "(Ljavax/net/ssl/X509TrustManager;)V", "reader-pro"})
    public static final class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;

        @NotNull
        public final SSLSocketFactory getSSLSocketFactory() {
            SSLSocketFactory sSLSocketFactory = this.sSLSocketFactory;
            if (sSLSocketFactory != null) {
                return sSLSocketFactory;
            }
            Intrinsics.throwUninitializedPropertyAccessException("sSLSocketFactory");
            throw null;
        }

        public final void setSSLSocketFactory(@NotNull SSLSocketFactory sSLSocketFactory) {
            Intrinsics.checkNotNullParameter(sSLSocketFactory, "<set-?>");
            this.sSLSocketFactory = sSLSocketFactory;
        }

        @NotNull
        public final X509TrustManager getTrustManager() {
            X509TrustManager x509TrustManager = this.trustManager;
            if (x509TrustManager != null) {
                return x509TrustManager;
            }
            Intrinsics.throwUninitializedPropertyAccessException("trustManager");
            throw null;
        }

        public final void setTrustManager(@NotNull X509TrustManager x509TrustManager) {
            Intrinsics.checkNotNullParameter(x509TrustManager, "<set-?>");
            this.trustManager = x509TrustManager;
        }
    }

    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull X509TrustManager trustManager) {
        Intrinsics.checkNotNullParameter(trustManager, "trustManager");
        return getSslSocketFactoryBase(trustManager, null, null, new InputStream[0]);
    }

    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull InputStream... certificates) {
        Intrinsics.checkNotNullParameter(certificates, "certificates");
        return getSslSocketFactoryBase(null, null, null, (InputStream[]) Arrays.copyOf(certificates, certificates.length));
    }

    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull InputStream bksFile, @NotNull String password, @NotNull InputStream... certificates) {
        Intrinsics.checkNotNullParameter(bksFile, "bksFile");
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(certificates, "certificates");
        return getSslSocketFactoryBase(null, bksFile, password, (InputStream[]) Arrays.copyOf(certificates, certificates.length));
    }

    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull InputStream bksFile, @NotNull String password, @NotNull X509TrustManager trustManager) {
        Intrinsics.checkNotNullParameter(bksFile, "bksFile");
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(trustManager, "trustManager");
        return getSslSocketFactoryBase(trustManager, bksFile, password, new InputStream[0]);
    }

    private final SSLParams getSslSocketFactoryBase(X509TrustManager trustManager, InputStream bksFile, String password, InputStream... certificates) throws IOException, CertificateException, KeyStoreException {
        SSLParams sslParams = new SSLParams();
        try {
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            TrustManager[] trustManagers = prepareTrustManager((InputStream[]) Arrays.copyOf(certificates, certificates.length));
            X509TrustManager manager = trustManager == null ? chooseTrustManager(trustManagers) : trustManager;
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, new TrustManager[]{manager}, null);
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            Intrinsics.checkNotNullExpressionValue(socketFactory, "sslContext.socketFactory");
            sslParams.setSSLSocketFactory(socketFactory);
            sslParams.setTrustManager(manager);
            return sslParams;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private final KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        if (bksFile == null || password == null) {
            return null;
        }
        try {
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            char[] charArray = password.toCharArray();
            Intrinsics.checkNotNullExpressionValue(charArray, "(this as java.lang.String).toCharArray()");
            clientKeyStore.load(bksFile, charArray);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            char[] charArray2 = password.toCharArray();
            Intrinsics.checkNotNullExpressionValue(charArray2, "(this as java.lang.String).toCharArray()");
            kmf.init(clientKeyStore, charArray2);
            return kmf.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private final TrustManager[] prepareTrustManager(InputStream... certificates) throws NoSuchAlgorithmException, IOException, CertificateException, KeyStoreException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        int i = 0;
        int length = certificates.length;
        while (i < length) {
            int index = i;
            InputStream certStream = certificates[i];
            i++;
            String certificateAlias = Integer.toString(index);
            Certificate cert = certificateFactory.generateCertificate(certStream);
            keyStore.setCertificateEntry(certificateAlias, cert);
            try {
                certStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        Intrinsics.checkNotNullExpressionValue(trustManagers, "tmf.trustManagers");
        return trustManagers;
    }

    private final X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        int i = 0;
        int length = trustManagers.length;
        while (i < length) {
            TrustManager trustManager = trustManagers[i];
            i++;
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new NullPointerException();
    }
}
