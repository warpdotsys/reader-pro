package io.legado.app.utils

import retrofit2.Response
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import java.util.BitSet
import java.util.Enumeration
import java.util.regex.Pattern

object NetworkUtils {
    fun getUrl(response: Response<*>): String =
        response.raw().networkResponse?.request?.url?.toString() ?: response.raw().request.url.toString()

    private val notNeedEncoding: BitSet by lazy {
        BitSet(256).apply {
            for (i in 'a'.code..'z'.code) set(i)
            for (i in 'A'.code..'Z'.code) set(i)
            for (i in '0'.code..'9'.code) set(i)
            for (char in "+-_.$:()!*@&#,[]") set(char.code)
        }
    }

    fun hasUrlEncoded(str: String): Boolean {
        var i = 0
        while (i < str.length) {
            val char = str[i]
            if (notNeedEncoding[char.code]) {
                i++
                continue
            }
            if (char == '%' && i + 2 < str.length && isDigit16Char(str[i + 1]) && isDigit16Char(str[i + 2])) {
                i += 3
                continue
            }
            return false
        }
        return true
    }

    private fun isDigit16Char(char: Char): Boolean =
        char in '0'..'9' || char in 'A'..'F' || char in 'a'..'f'

    fun getAbsoluteURL(baseURL: String?, relativePath: String): String {
        if (baseURL.isNullOrEmpty()) return relativePath
        if (relativePath.isEmpty()) return baseURL
        return try {
            URL(URL(baseURL.substringBefore(',')), relativePath).toString()
        } catch (error: Exception) {
            error.printStackTrace()
            relativePath
        }
    }

    fun getAbsoluteURL(baseURL: URL?, relativePath: String): String {
        if (baseURL == null) return relativePath
        return try {
            URL(baseURL, relativePath).toString()
        } catch (error: Exception) {
            error.printStackTrace()
            relativePath
        }
    }

    fun getBaseUrl(url: String?): String? {
        if (url == null || !url.startsWith("http")) return null
        val index = url.indexOf('/', 9)
        return if (index == -1) url else url.substring(0, index)
    }

    fun getSubDomain(url: String?): String {
        val baseUrl = getBaseUrl(url) ?: return ""
        return if (baseUrl.indexOf('.') == baseUrl.lastIndexOf('.')) {
            baseUrl.substring(baseUrl.lastIndexOf('/') + 1)
        } else {
            baseUrl.substring(baseUrl.indexOf('.') + 1)
        }
    }

    fun getLocalIPAddress(): InetAddress? {
        val interfaces: Enumeration<NetworkInterface> = try {
            NetworkInterface.getNetworkInterfaces()
        } catch (error: SocketException) {
            error.printStackTrace()
            return null
        }
        while (interfaces.hasMoreElements()) {
            val addresses = interfaces.nextElement().inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress && isIPv4Address(address.hostAddress)) return address
            }
        }
        return null
    }

    fun isIPv4Address(input: String): Boolean = IPV4_PATTERN.matcher(input).matches()

    private val IPV4_PATTERN = Pattern.compile(
        "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
    )
}
