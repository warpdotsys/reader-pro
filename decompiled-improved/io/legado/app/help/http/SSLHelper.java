// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import javax.net.ssl.SSLSession;
import java.security.cert.Certificate;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.KeyManagerFactory;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import javax.net.ssl.SSLSocketFactory;
import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import javax.net.ssl.HostnameVerifier;
import kotlin.Lazy;
import javax.net.ssl.X509TrustManager;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001)B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001b\u0010\u0015\u001a\u00020\u00122\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002?\u0006\u0002\u0010\u0019J \u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0012J1\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001b?\u0006\u0002\u0010 J\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u001e\u001a\u00020\u0012J!\u0010\u0005\u001a\u0004\u0018\u00010\u00042\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001b?\u0006\u0002\u0010!JA\u0010\"\u001a\u0004\u0018\u00010\u00042\b\u0010\u001e\u001a\u0004\u0018\u00010\u00122\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001bH\u0002?\u0006\u0002\u0010#J)\u0010$\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010\u00172\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0002?\u0006\u0002\u0010&J'\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0012\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001b0\u0017\"\u00020\u001bH\u0002?\u0006\u0002\u0010(R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00048F?\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b?\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001b\u0010\u000b\u001a\u00020\f8FX\u0086\u0084\u0002?\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0011\u001a\u00020\u0012?\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¡§\u0006*" }, d2 = { "Lio/legado/app/help/http/SSLHelper;", "", "()V", "sslSocketFactory", "Lio/legado/app/help/http/SSLHelper$SSLParams;", "getSslSocketFactory", "()Lio/legado/app/help/http/SSLHelper$SSLParams;", "unsafeHostnameVerifier", "Ljavax/net/ssl/HostnameVerifier;", "getUnsafeHostnameVerifier", "()Ljavax/net/ssl/HostnameVerifier;", "unsafeSSLSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "getUnsafeSSLSocketFactory", "()Ljavax/net/ssl/SSLSocketFactory;", "unsafeSSLSocketFactory$delegate", "Lkotlin/Lazy;", "unsafeTrustManager", "Ljavax/net/ssl/X509TrustManager;", "getUnsafeTrustManager", "()Ljavax/net/ssl/X509TrustManager;", "chooseTrustManager", "trustManagers", "", "Ljavax/net/ssl/TrustManager;", "([Ljavax/net/ssl/TrustManager;)Ljavax/net/ssl/X509TrustManager;", "bksFile", "Ljava/io/InputStream;", "password", "", "trustManager", "certificates", "(Ljava/io/InputStream;Ljava/lang/String;[Ljava/io/InputStream;)Lio/legado/app/help/http/SSLHelper$SSLParams;", "([Ljava/io/InputStream;)Lio/legado/app/help/http/SSLHelper$SSLParams;", "getSslSocketFactoryBase", "(Ljavax/net/ssl/X509TrustManager;Ljava/io/InputStream;Ljava/lang/String;[Ljava/io/InputStream;)Lio/legado/app/help/http/SSLHelper$SSLParams;", "prepareKeyManager", "Ljavax/net/ssl/KeyManager;", "(Ljava/io/InputStream;Ljava/lang/String;)[Ljavax/net/ssl/KeyManager;", "prepareTrustManager", "([Ljava/io/InputStream;)[Ljavax/net/ssl/TrustManager;", "SSLParams", "reader-pro" })
public final class SSLHelper
{
    @NotNull
    public static final SSLHelper INSTANCE;
    @NotNull
    private static final X509TrustManager unsafeTrustManager;
    @NotNull
    private static final Lazy unsafeSSLSocketFactory$delegate;
    @NotNull
    private static final HostnameVerifier unsafeHostnameVerifier;
    
    private SSLHelper() {
    }
    
    @Nullable
    public final SSLParams getSslSocketFactory() {
        return this.getSslSocketFactoryBase(null, null, null, new InputStream[0]);
    }
    
    @NotNull
    public final X509TrustManager getUnsafeTrustManager() {
        return SSLHelper.unsafeTrustManager;
    }
    
    @NotNull
    public final SSLSocketFactory getUnsafeSSLSocketFactory() {
        final Object value = SSLHelper.unsafeSSLSocketFactory$delegate.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-unsafeSSLSocketFactory>(...)");
        return (SSLSocketFactory)value;
    }
    
    @NotNull
    public final HostnameVerifier getUnsafeHostnameVerifier() {
        return SSLHelper.unsafeHostnameVerifier;
    }
    
    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull final X509TrustManager trustManager) {
        Intrinsics.checkNotNullParameter((Object)trustManager, "trustManager");
        return this.getSslSocketFactoryBase(trustManager, null, null, new InputStream[0]);
    }
    
    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull final InputStream... certificates) {
        Intrinsics.checkNotNullParameter((Object)certificates, "certificates");
        return this.getSslSocketFactoryBase(null, null, null, (InputStream[])Arrays.copyOf(certificates, certificates.length));
    }
    
    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull final InputStream bksFile, @NotNull final String password, @NotNull final InputStream... certificates) {
        Intrinsics.checkNotNullParameter((Object)bksFile, "bksFile");
        Intrinsics.checkNotNullParameter((Object)password, "password");
        Intrinsics.checkNotNullParameter((Object)certificates, "certificates");
        return this.getSslSocketFactoryBase(null, bksFile, password, (InputStream[])Arrays.copyOf(certificates, certificates.length));
    }
    
    @Nullable
    public final SSLParams getSslSocketFactory(@NotNull final InputStream bksFile, @NotNull final String password, @NotNull final X509TrustManager trustManager) {
        Intrinsics.checkNotNullParameter((Object)bksFile, "bksFile");
        Intrinsics.checkNotNullParameter((Object)password, "password");
        Intrinsics.checkNotNullParameter((Object)trustManager, "trustManager");
        return this.getSslSocketFactoryBase(trustManager, bksFile, password, new InputStream[0]);
    }
    
    private final SSLParams getSslSocketFactoryBase(final X509TrustManager trustManager, final InputStream bksFile, final String password, final InputStream... certificates) {
        final SSLParams sslParams = new SSLParams();
        try {
            final KeyManager[] keyManagers = this.prepareKeyManager(bksFile, password);
            final TrustManager[] trustManagers = this.prepareTrustManager((InputStream[])Arrays.copyOf(certificates, certificates.length));
            final X509TrustManager manager = (trustManager == null) ? this.chooseTrustManager(trustManagers) : trustManager;
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, new TrustManager[] { manager }, null);
            final SSLParams sslParams2 = sslParams;
            final SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            Intrinsics.checkNotNullExpressionValue((Object)socketFactory, "sslContext.socketFactory");
            sslParams2.setSSLSocketFactory(socketFactory);
            sslParams.setTrustManager(manager);
            return sslParams;
        }
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (final KeyManagementException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    private final KeyManager[] prepareKeyManager(final InputStream bksFile, final String password) {
        try {
            if (bksFile == null || password == null) {
                return null;
            }
            final KeyStore instance;
            final KeyStore clientKeyStore = instance = KeyStore.getInstance("BKS");
            final char[] charArray = password.toCharArray();
            Intrinsics.checkNotNullExpressionValue((Object)charArray, "(this as java.lang.String).toCharArray()");
            instance.load(bksFile, charArray);
            final KeyManagerFactory instance2;
            final KeyManagerFactory kmf = instance2 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            final KeyStore ks = clientKeyStore;
            final char[] charArray2 = password.toCharArray();
            Intrinsics.checkNotNullExpressionValue((Object)charArray2, "(this as java.lang.String).toCharArray()");
            instance2.init(ks, charArray2);
            return kmf.getKeyManagers();
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private final TrustManager[] prepareTrustManager(final InputStream... certificates) {
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        int i = 0;
        while (i < certificates.length) {
            final int index = i;
            final InputStream certStream = certificates[i];
            ++i;
            final String certificateAlias = Integer.toString(index);
            final Certificate cert = certificateFactory.generateCertificate(certStream);
            keyStore.setCertificateEntry(certificateAlias, cert);
            try {
                certStream.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        final TrustManager[] trustManagers = tmf.getTrustManagers();
        Intrinsics.checkNotNullExpressionValue((Object)trustManagers, "tmf.trustManagers");
        return trustManagers;
    }
    
    private final X509TrustManager chooseTrustManager(final TrustManager[] trustManagers) {
        int i = 0;
        while (i < trustManagers.length) {
            final TrustManager trustManager = trustManagers[i];
            ++i;
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager)trustManager;
            }
        }
        throw new NullPointerException();
    }
    
    private static final boolean unsafeHostnameVerifier$lambda-0(final String $noName_0, final SSLSession $noName_1) {
        return true;
    }
    
    static {
        INSTANCE = new SSLHelper();
        unsafeTrustManager = (X509TrustManager)new SSLHelper$unsafeTrustManager.SSLHelper$unsafeTrustManager$1();
        unsafeSSLSocketFactory$delegate = LazyKt.lazy((Function0)SSLHelper$unsafeSSLSocketFactory.SSLHelper$unsafeSSLSocketFactory$2.INSTANCE);
        unsafeHostnameVerifier = SSLHelper::unsafeHostnameVerifier$lambda-0;
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086.?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¡§\u0006\u000f" }, d2 = { "Lio/legado/app/help/http/SSLHelper$SSLParams;", "", "()V", "sSLSocketFactory", "Ljavax/net/ssl/SSLSocketFactory;", "getSSLSocketFactory", "()Ljavax/net/ssl/SSLSocketFactory;", "setSSLSocketFactory", "(Ljavax/net/ssl/SSLSocketFactory;)V", "trustManager", "Ljavax/net/ssl/X509TrustManager;", "getTrustManager", "()Ljavax/net/ssl/X509TrustManager;", "setTrustManager", "(Ljavax/net/ssl/X509TrustManager;)V", "reader-pro" })
    public static final class SSLParams
    {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
        
        @NotNull
        public final SSLSocketFactory getSSLSocketFactory() {
            final SSLSocketFactory sslSocketFactory = this.sSLSocketFactory;
            if (sslSocketFactory != null) {
                return sslSocketFactory;
            }
            Intrinsics.throwUninitializedPropertyAccessException("sSLSocketFactory");
            throw null;
        }
        
        public final void setSSLSocketFactory(@NotNull final SSLSocketFactory <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.sSLSocketFactory = <set-?>;
        }
        
        @NotNull
        public final X509TrustManager getTrustManager() {
            final X509TrustManager trustManager = this.trustManager;
            if (trustManager != null) {
                return trustManager;
            }
            Intrinsics.throwUninitializedPropertyAccessException("trustManager");
            throw null;
        }
        
        public final void setTrustManager(@NotNull final X509TrustManager <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.trustManager = <set-?>;
        }
    }
}
