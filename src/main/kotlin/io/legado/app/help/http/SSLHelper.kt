package io.legado.app.help.http

import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SSLHelper {
    val sslSocketFactory: SSLParams?
        get() = getSslSocketFactoryBase(null, null, null)

    val unsafeTrustManager: X509TrustManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }

    val unsafeSSLSocketFactory: SSLSocketFactory by lazy {
        try {
            SSLContext.getInstance("SSL").apply {
                init(null, arrayOf(unsafeTrustManager), null)
            }.socketFactory
        } catch (error: Exception) {
            throw RuntimeException(error)
        }
    }

    val unsafeHostnameVerifier = HostnameVerifier { _, _ -> true }

    class SSLParams {
        lateinit var sSLSocketFactory: SSLSocketFactory
        lateinit var trustManager: X509TrustManager
    }

    fun getSslSocketFactory(trustManager: X509TrustManager): SSLParams? =
        getSslSocketFactoryBase(trustManager, null, null)

    fun getSslSocketFactory(vararg certificates: InputStream): SSLParams? =
        getSslSocketFactoryBase(null, null, null, *certificates)

    fun getSslSocketFactory(
        bksFile: InputStream,
        password: String,
        vararg certificates: InputStream
    ): SSLParams? = getSslSocketFactoryBase(null, bksFile, password, *certificates)

    fun getSslSocketFactory(
        bksFile: InputStream,
        password: String,
        trustManager: X509TrustManager
    ): SSLParams? = getSslSocketFactoryBase(trustManager, bksFile, password)

    private fun getSslSocketFactoryBase(
        trustManager: X509TrustManager?,
        bksFile: InputStream?,
        password: String?,
        vararg certificates: InputStream
    ): SSLParams? {
        val sslParams = SSLParams()
        try {
            val manager = trustManager ?: chooseTrustManager(prepareTrustManager(*certificates))
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(prepareKeyManager(bksFile, password), arrayOf<TrustManager>(manager), null)
            sslParams.sSLSocketFactory = sslContext.socketFactory
            sslParams.trustManager = manager
            return sslParams
        } catch (error: NoSuchAlgorithmException) {
            error.printStackTrace()
        } catch (error: KeyManagementException) {
            error.printStackTrace()
        }
        return null
    }

    private fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
        return try {
            if (bksFile == null || password == null) return null
            val clientKeyStore = KeyStore.getInstance("BKS")
            clientKeyStore.load(bksFile, password.toCharArray())
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                init(clientKeyStore, password.toCharArray())
            }.keyManagers
        } catch (error: Exception) {
            error.printStackTrace()
            null
        }
    }

    private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager> {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        for ((index, certStream) in certificates.withIndex()) {
            keyStore.setCertificateEntry(index.toString(), certificateFactory.generateCertificate(certStream))
            try {
                certStream.close()
            } catch (error: IOException) {
                error.printStackTrace()
            }
        }
        return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }.trustManagers
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager {
        return trustManagers.firstOrNull { it is X509TrustManager } as? X509TrustManager
            ?: throw NullPointerException()
    }
}
