package io.legado.app.help.http

import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Arrays
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import org.jetbrains.annotations.NotNull

public object SSLHelper {
   public final val sslSocketFactory: io.legado.app.help.http.SSLHelper.SSLParams?
      public final get() {
         return this.getSslSocketFactoryBase(null, null, null);
      }


   public final val unsafeHostnameVerifier: HostnameVerifier = SSLHelper::unsafeHostnameVerifier$lambda-0

   public final val unsafeSSLSocketFactory: SSLSocketFactory by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         val var1: Any = unsafeSSLSocketFactory$delegate.getValue();
         return var1 as SSLSocketFactory;
      }


   public final val unsafeTrustManager: X509TrustManager = (new X509TrustManager() {
      @Override
      public void checkClientTrusted(@NotNull X509Certificate[] chain, @NotNull java.lang.String authType) throws CertificateException {
      }

      @Override
      public void checkServerTrusted(@NotNull X509Certificate[] chain, @NotNull java.lang.String authType) throws CertificateException {
      }

      @NotNull
      @Override
      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }
   }) as X509TrustManager

   public fun getSslSocketFactory(trustManager: X509TrustManager): io.legado.app.help.http.SSLHelper.SSLParams? {
      return this.getSslSocketFactoryBase(trustManager, null, null);
   }

   public fun getSslSocketFactory(vararg certificates: InputStream): io.legado.app.help.http.SSLHelper.SSLParams? {
      return this.getSslSocketFactoryBase(null, null, null, Arrays.copyOf(certificates, certificates.length));
   }

   public fun getSslSocketFactory(bksFile: InputStream, password: String, vararg certificates: InputStream): io.legado.app.help.http.SSLHelper.SSLParams? {
      return this.getSslSocketFactoryBase(null, bksFile, password, Arrays.copyOf(certificates, certificates.length));
   }

   public fun getSslSocketFactory(bksFile: InputStream, password: String, trustManager: X509TrustManager): io.legado.app.help.http.SSLHelper.SSLParams? {
      return this.getSslSocketFactoryBase(trustManager, bksFile, password);
   }

   private fun getSslSocketFactoryBase(trustManager: X509TrustManager?, bksFile: InputStream?, password: String?, vararg certificates: InputStream): io.legado.app.help.http.SSLHelper.SSLParams? {
      val sslParams: SSLHelper.SSLParams = new SSLHelper.SSLParams();

      try {
         val e: Array<KeyManager> = this.prepareKeyManager(bksFile, password);
         val manager: X509TrustManager = if (trustManager == null)
            this.chooseTrustManager(this.prepareTrustManager(Arrays.copyOf(certificates, certificates.length)))
            else
            trustManager;
         val sslContext: SSLContext = SSLContext.getInstance("TLS");
         sslContext.init(e, new TrustManager[]{manager}, null);
         val var13: SSLSocketFactory = sslContext.getSocketFactory();
         sslParams.setSSLSocketFactory(var13);
         sslParams.setTrustManager(manager);
         return sslParams;
      } catch (var11: NoSuchAlgorithmException) {
         var11.printStackTrace();
      } catch (var12: KeyManagementException) {
         var12.printStackTrace();
      }

      return null;
   }

   private fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
      try {
         if (bksFile != null && password != null) {
            val e: KeyStore = KeyStore.getInstance("BKS");
            var var10002: CharArray = password.toCharArray();
            e.load(bksFile, var10002);
            val kmf: KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            var10002 = password.toCharArray();
            kmf.init(e, var10002);
            return kmf.getKeyManagers();
         } else {
            return null;
         }
      } catch (var7: Exception) {
         var7.printStackTrace();
         return null;
      }
   }

   private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager> {
      val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509");
      val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null);
      val tmf: Array<InputStream> = certificates;
      var var5: Int = 0;
      val var6: Int = certificates.length;

      while (var5 < var6) {
         val index: Int = var5;
         val certStream: InputStream = tmf[var5];
         var5++;
         keyStore.setCertificateEntry(Integer.toString(index), certificateFactory.generateCertificate(certStream));

         try {
            certStream.close();
         } catch (var12: IOException) {
            var12.printStackTrace();
         }
      }

      val var13: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      var13.init(keyStore);
      val var14: Array<TrustManager> = var13.getTrustManagers();
      return var14;
   }

   private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager {
      val var2: Array<TrustManager> = trustManagers;
      var var3: Int = 0;
      val var4: Int = trustManagers.length;

      while (var3 < var4) {
         val trustManager: TrustManager = var2[var3];
         var3++;
         if (trustManager is X509TrustManager) {
            return trustManager as X509TrustManager;
         }
      }

      throw new NullPointerException();
   }

   @JvmStatic
   fun `unsafeHostnameVerifier$lambda-0`(`$noName_0`: java.lang.String, `$noName_1`: SSLSession): Boolean {
      return true;
   }

   public class SSLParams {
      public final lateinit var sSLSocketFactory: SSLSocketFactory
         internal set

      public final lateinit var trustManager: X509TrustManager
         internal set
   }
}
