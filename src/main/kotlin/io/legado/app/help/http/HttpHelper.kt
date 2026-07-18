package io.legado.app.help.http

import io.legado.app.model.DebugLog
import okhttp3.Authenticator
import okhttp3.ConnectionSpec
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

private val proxyClientCache: ConcurrentHashMap<String, OkHttpClient> by lazy { ConcurrentHashMap() }

val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .sslSocketFactory(SSLHelper.unsafeSSLSocketFactory, SSLHelper.unsafeTrustManager)
        .retryOnConnectionFailure(true)
        .hostnameVerifier(SSLHelper.unsafeHostnameVerifier)
        .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        .followRedirects(true)
        .followSslRedirects(true)
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Keep-Alive", "300")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Cache-Control", "no-cache")
                .build()
            chain.proceed(request)
        })
        .build()
}

fun getProxyClient(proxy: String? = null, debugLog: DebugLog? = null): OkHttpClient {
    if (proxy.isNullOrBlank()) return withDebugLog(okHttpClient, debugLog)
    if (debugLog == null) proxyClientCache[proxy]?.let { return it }

    val group = Regex("(http|socks4|socks5)://(.*):(\\d{2,5})(@.*@.*)?").findAll(proxy).first()
    val username: String
    val password: String
    if (group.groupValues[4].isEmpty()) {
        username = ""
        password = ""
    } else {
        username = group.groupValues[4].split("@")[1]
        password = group.groupValues[4].split("@")[2]
    }
    val type = if (group.groupValues[1] == "http") "http" else "socks"
    val host = group.groupValues[2]
    val port = group.groupValues[3].toInt()
    if (type == "direct" || host.isEmpty()) return okHttpClient

    val builder = okHttpClient.newBuilder().proxy(
        Proxy(if (type == "http") Proxy.Type.HTTP else Proxy.Type.SOCKS, InetSocketAddress(host, port))
    )
    if (username.isNotEmpty() && password.isNotEmpty()) {
        builder.proxyAuthenticator(object : Authenticator {
            @Throws(IOException::class)
            override fun authenticate(route: Route?, response: Response): Request =
                response.request.newBuilder().header("Proxy-Authorization", Credentials.basic(username, password)).build()
        })
    }
    if (debugLog != null) return withDebugLog(builder.build(), debugLog)
    return builder.build().also { proxyClientCache[proxy] = it }
}

private fun withDebugLog(client: OkHttpClient, debugLog: DebugLog?): OkHttpClient {
    if (debugLog == null) return client
    val interceptor = HttpLoggingInterceptor(debugLog).apply { level = HttpLoggingInterceptor.Level.BODY }
    return client.newBuilder().addNetworkInterceptor(interceptor).build()
}
