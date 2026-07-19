package io.legado.app.help

import cn.hutool.crypto.digest.DigestUtil
import cn.hutool.crypto.symmetric.AES
import cn.hutool.crypto.symmetric.DESede
import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.constant.AppConst
import io.legado.app.data.entities.BaseSource
import io.legado.app.exception.NoStackTraceException
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.SSLHelper
import io.legado.app.help.http.StrResponse
import io.legado.app.help.http.newCall
import io.legado.app.help.http.okHttpClient
import io.legado.app.model.Debug
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.EncodingDetect
import io.legado.app.utils.FileUtils
import io.legado.app.utils.GSON
import io.legado.app.utils.HtmlFormatter
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.StringUtils
import io.legado.app.utils.ZipUtils
import io.legado.app.utils.fromJsonObject
import io.legado.app.utils.isAbsUrl
import io.legado.app.utils.msg
import io.legado.app.utils.printOnDebug
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.UUID
import java.util.zip.ZipInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jsoup.Connection
import org.jsoup.Jsoup

interface JsExtensions {

    fun getSource(): BaseSource?

    fun getUserNameSpace(): String

    fun getLogger(): DebugLog?

    fun ajax(urlStr: String): String? = runBlocking {
        kotlin.runCatching {
            val analyzeUrl = AnalyzeUrl(urlStr, source = getSource(), debugLog = getLogger())
            analyzeUrl.getStrResponse(urlStr).body
        }.onFailure {
            it.printOnDebug()
        }.getOrElse {
            it.msg
        }
    }

    fun ajaxAll(urlList: Array<String>): Array<StrResponse> = runBlocking {
        val asyncArray = Array(urlList.size) {
            async(Dispatchers.IO) {
                val url = urlList[it]
                val analyzeUrl = AnalyzeUrl(url, source = getSource(), debugLog = getLogger())
                analyzeUrl.getStrResponse(url)
            }
        }
        val resArray = arrayOfNulls<StrResponse>(urlList.size)
        for (i in urlList.indices) {
            resArray[i] = asyncArray[i].await()
        }
        @Suppress("UNCHECKED_CAST")
        resArray as Array<StrResponse>
    }

    fun connect(urlStr: String): StrResponse = runBlocking {
        val analyzeUrl = AnalyzeUrl(urlStr, source = getSource(), debugLog = getLogger())
        kotlin.runCatching {
            analyzeUrl.getStrResponseAwait()
        }.onFailure {
            it.printOnDebug()
        }.getOrElse {
            StrResponse(analyzeUrl.url, it.localizedMessage)
        }
    }

    fun connect(urlStr: String, header: String?): StrResponse = runBlocking {
        val headerMap = GSON.fromJsonObject<Map<String, String>>(header).getOrNull()
        val analyzeUrl = AnalyzeUrl(urlStr, source = getSource(), headerMapF = headerMap, debugLog = getLogger())
        kotlin.runCatching {
            analyzeUrl.getStrResponseAwait()
        }.onFailure {
            it.printOnDebug()
        }.getOrElse {
            StrResponse(analyzeUrl.url, it.localizedMessage)
        }
    }

    fun webView(
        @Suppress("UNUSED_PARAMETER") html: String?,
        @Suppress("UNUSED_PARAMETER") url: String?,
        @Suppress("UNUSED_PARAMETER") js: String?
    ): String? {
        return null
    }

    fun importScript(path: String): String {
        val result = when {
            path.startsWith("http") -> cacheFile(path) ?: ""
            path.startsWith("/storage") -> FileUtils.readText(path)
            else -> readTxtFile(path)
        }
        if (result.isBlank()) {
            throw NoStackTraceException("$path \u5185\u5bb9\u83b7\u53d6\u5931\u8d25\u6216\u8005\u4e3a\u7a7a")
        }
        return result
    }

    fun cacheFile(urlStr: String): String? = cacheFile(urlStr, 0)

    fun cacheFile(urlStr: String, saveTime: Int = 0): String? {
        val key = md5Encode16(urlStr)
        val cacheInstance = CacheManager(getUserNameSpace())
        val cache = cacheInstance.getFile(key)
        if (cache.isNullOrBlank()) {
            log("\u9996\u6b21\u4e0b\u8f7d $urlStr")
            val value = ajax(urlStr) ?: return null
            cacheInstance.putFile(key, value, saveTime)
            return value
        } else {
            return cache
        }
    }

    fun getCookie(tag: String, key: String? = null): String {
        val cookieStore = CookieStore(getUserNameSpace())
        val cookie = cookieStore.getCookie(tag)
        val cookieMap = cookieStore.cookieToMap(cookie)
        return if (key != null) {
            cookieMap[key] ?: ""
        } else {
            cookie
        }
    }

    fun downloadFile(content: String, url: String): String {
        var zipPath = AnalyzeUrl(url).type ?: return ""
        zipPath = FileUtils.getPath(
            FileUtils.createFolderIfNotExist(FileUtils.getCachePath()),
            MD5Utils.md5Encode16(url) + "." + zipPath
        )
        FileUtils.deleteFile(zipPath)
        val zipFile = FileUtils.createFileIfNotExist(zipPath)
        val it = StringUtils.hexStringToByte(content)
        if (it.isNotEmpty()) {
            zipFile.writeBytes(it)
        }
        return zipPath.substring(FileUtils.getCachePath().length)
    }

    fun get(urlStr: String, headers: Map<String, String>): Connection.Response {
        val response = Jsoup.connect(urlStr)
            .sslSocketFactory(SSLHelper.unsafeSSLSocketFactory)
            .ignoreContentType(true)
            .followRedirects(false)
            .headers(headers)
            .method(Connection.Method.GET)
            .execute()
        val cookies = response.cookies()
        val cookieStore = CookieStore(getUserNameSpace())
        cookieStore.mapToCookie(cookies)?.let {
            val domain = NetworkUtils.getSubDomain(urlStr)
            cookieStore.replaceCookie(domain + "_cookieJar", it)
        }
        return response
    }

    fun head(urlStr: String, headers: Map<String, String>): Connection.Response {
        val response = Jsoup.connect(urlStr)
            .sslSocketFactory(SSLHelper.unsafeSSLSocketFactory)
            .ignoreContentType(true)
            .followRedirects(false)
            .headers(headers)
            .method(Connection.Method.HEAD)
            .execute()
        val cookies = response.cookies()
        val cookieStore = CookieStore(getUserNameSpace())
        cookieStore.mapToCookie(cookies)?.let {
            val domain = NetworkUtils.getSubDomain(urlStr)
            cookieStore.replaceCookie(domain + "_cookieJar", it)
        }
        return response
    }

    fun post(urlStr: String, body: String, headers: Map<String, String>): Connection.Response {
        val response = Jsoup.connect(urlStr)
            .sslSocketFactory(SSLHelper.unsafeSSLSocketFactory)
            .ignoreContentType(true)
            .followRedirects(false)
            .requestBody(body)
            .headers(headers)
            .method(Connection.Method.POST)
            .execute()
        val cookies = response.cookies()
        val cookieStore = CookieStore(getUserNameSpace())
        cookieStore.mapToCookie(cookies)?.let {
            val domain = NetworkUtils.getSubDomain(urlStr)
            cookieStore.replaceCookie(domain + "_cookieJar", it)
        }
        return response
    }

    fun base64Decode(str: String): String {
        return EncoderUtils.base64Decode(str, 2)
    }

    fun base64Decode(str: String, flags: Int): String {
        return EncoderUtils.base64Decode(str, flags)
    }

    fun base64DecodeToByteArray(str: String?): ByteArray? {
        if (str.isNullOrBlank()) {
            return null
        }
        return Base64.decode(str, 0)
    }

    fun base64DecodeToByteArray(str: String?, flags: Int): ByteArray? {
        if (str.isNullOrBlank()) {
            return null
        }
        return Base64.decode(str, flags)
    }

    fun base64Encode(str: String): String? {
        return EncoderUtils.base64Encode(str, 2)
    }

    fun base64Encode(str: String, flags: Int): String? {
        return EncoderUtils.base64Encode(str, flags)
    }

    fun md5Encode(str: String): String {
        return MD5Utils.md5Encode(str)
    }

    fun md5Encode16(str: String): String {
        return MD5Utils.md5Encode16(str)
    }

    fun timeFormatUTC(time: Long, format: String, sh: Int): String? {
        val utc = SimpleTimeZone(sh, "UTC")
        return SimpleDateFormat(format, Locale.getDefault()).apply {
            timeZone = utc
        }.format(Date(time))
    }

    fun timeFormat(time: Long): String {
        return AppConst.dateFormat.format(Date(time))
    }

    fun utf8ToGbk(str: String): String {
        val utf8 = String(str.toByteArray(charset("UTF-8")), Charsets.UTF_8)
        val unicode = String(utf8.toByteArray(Charsets.UTF_8), charset("UTF-8"))
        return String(unicode.toByteArray(charset("GBK")), Charsets.UTF_8)
    }

    fun encodeURI(str: String): String {
        return try {
            URLEncoder.encode(str, "UTF-8")
        } catch (e: Exception) {
            ""
        }
    }

    fun encodeURI(str: String, enc: String): String {
        return try {
            URLEncoder.encode(str, enc)
        } catch (e: Exception) {
            ""
        }
    }

    fun htmlFormat(str: String): String {
        return HtmlFormatter.formatKeepImg(str)
    }

    fun getFile(path: String): File {
        val cachePath = ReaderAdapterHelper.getAdapter().cacheDir
        val aPath = if (path.startsWith(File.separator)) {
            cachePath + path
        } else {
            cachePath + File.separator + path
        }
        return File(aPath)
    }

    fun readFile(path: String): ByteArray? {
        val file = getFile(path)
        return if (file.exists()) file.readBytes() else null
    }

    fun readTxtFile(path: String): String {
        val file = getFile(path)
        if (file.exists()) {
            val charsetName = EncodingDetect.getEncode(file)
            return String(file.readBytes(), charset(charsetName))
        }
        return ""
    }

    fun readTxtFile(path: String, charsetName: String): String {
        val file = getFile(path)
        if (file.exists()) {
            return String(file.readBytes(), charset(charsetName))
        }
        return ""
    }

    fun deleteFile(path: String) {
        val file = getFile(path)
        FileUtils.delete(file, true)
    }

    fun unzipFile(zipPath: String): String {
        if (zipPath.isEmpty()) {
            return ""
        }
        val unzipPath = FileUtils.getPath(
            FileUtils.createFolderIfNotExist(FileUtils.getCachePath()),
            FileUtils.getNameExcludeExtension(zipPath)
        )
        FileUtils.deleteFile(unzipPath)
        val zipFile = getFile(zipPath)
        val unzipFolder = FileUtils.createFolderIfNotExist(unzipPath)
        ZipUtils.unzipFile(zipFile, unzipFolder)
        FileUtils.deleteFile(zipFile.absolutePath)
        return unzipPath.substring(FileUtils.getCachePath().length)
    }

    fun getTxtInFolder(unzipPath: String): String {
        if (unzipPath.isEmpty()) {
            return ""
        }
        val unzipFolder = getFile(unzipPath)
        val contents = StringBuilder()
        unzipFolder.listFiles()?.forEach { f ->
            val charsetName = EncodingDetect.getEncode(f)
            contents.append(String(f.readBytes(), charset(charsetName))).append("\n")
            contents.deleteCharAt(contents.length - 1)
        }
        FileUtils.deleteFile(unzipFolder.absolutePath)
        return contents.toString()
    }

    fun getZipStringContent(url: String, path: String): String {
        val byteArray = getZipByteArrayContent(url, path) ?: return ""
        val charsetName = EncodingDetect.getEncode(byteArray)
        return String(byteArray, charset(charsetName))
    }

    fun getZipStringContent(url: String, path: String, charsetName: String): String {
        val byteArray = getZipByteArrayContent(url, path) ?: return ""
        return String(byteArray, charset(charsetName))
    }

    fun getZipByteArrayContent(url: String, path: String): ByteArray? {
        val bytes = if (url.startsWith("http://") || url.startsWith("https://")) {
            runBlocking {
                okHttpClient.newCall {
                    url(url)
                }.bytes()
            }
        } else {
            StringUtils.hexStringToByte(url)
        }
        val bos = ByteArrayOutputStream()
        val zis = ZipInputStream(ByteArrayInputStream(bytes))
        var entry = zis.nextEntry
        while (entry != null) {
            if (entry.name == path) {
                zis.use {
                    it.copyTo(bos)
                }
                return bos.toByteArray()
            }
            entry = zis.nextEntry
        }
        Debug.log("getZipContent \u672a\u53d1\u73b0\u5185\u5bb9")
        return null
    }

    fun queryBase64TTF(base64: String?): QueryTTF? {
        base64DecodeToByteArray(base64)?.let {
            return QueryTTF(it)
        }
        return null
    }

    fun queryTTF(str: String?): QueryTTF? {
        if (str == null) {
            return null
        }
        val key = md5Encode16(str)
        val cacheInstance = CacheManager(getUserNameSpace())
        var qTTF = cacheInstance.getQueryTTF(key)
        if (qTTF != null) {
            return qTTF
        }
        val font = if (str.isAbsUrl()) {
            runBlocking {
                var x = cacheInstance.getByteArray(key)
                if (x != null) {
                    return@runBlocking x
                }
                x = okHttpClient.newCall {
                    url(str)
                }.bytes()
                cacheInstance.put(key, x)
                x
            }
        } else if (str.indexOf("storage/") > 0) {
            File(str).readBytes()
        } else {
            base64DecodeToByteArray(str)
        } ?: return null
        qTTF = QueryTTF(font)
        cacheInstance.put(key, qTTF)
        return qTTF
    }

    fun replaceFont(text: String, font1: QueryTTF?, font2: QueryTTF?): String {
        if (font1 == null || font2 == null) return text
        val contentArray = text.toCharArray()
        contentArray.forEachIndexed { index, item ->
            if (font1.inLimit(item)) {
                val code = font2.getCodeByGlyf(font1.getGlyfByCode(item.code))
                if (code != 0) {
                    contentArray[index] = code.toChar()
                }
            }
        }
        return contentArray.joinToString("")
    }

    fun toast(msg: Any?) {
        getLogger()?.log("toast: $msg")
        Debug.log("toast: $msg")
    }

    fun longToast(msg: Any?) {
        getLogger()?.log("longToast: $msg")
        Debug.log("longToast: $msg")
    }

    fun log(msg: String): String {
        getLogger()?.log(msg)
        Debug.log(msg)
        return msg
    }

    fun logType(any: Any?) {
        if (any == null) {
            log("null")
        } else {
            log(any.javaClass.name)
        }
    }

    fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun aesDecodeToByteArray(str: String, key: String, transformation: String, iv: String): ByteArray? {
        return try {
            EncoderUtils.decryptAES(str.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())
        } catch (e: Exception) {
            e.printOnDebug()
            log(e.localizedMessage ?: "aesDecodeToByteArrayERROR")
            null
        }
    }

    fun aesDecodeToString(str: String, key: String, transformation: String, iv: String): String? {
        aesDecodeToByteArray(str, key, transformation, iv)?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun aesBase64DecodeToByteArray(str: String, key: String, transformation: String, iv: String): ByteArray? {
        return try {
            EncoderUtils.decryptBase64AES(str.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())
        } catch (e: Exception) {
            e.printOnDebug()
            log(e.localizedMessage ?: "aesDecodeToByteArrayERROR")
            null
        }
    }

    fun aesBase64DecodeToString(str: String, key: String, transformation: String, iv: String): String? {
        aesBase64DecodeToByteArray(str, key, transformation, iv)?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun aesEncodeToByteArray(data: String, key: String, transformation: String, iv: String): ByteArray? {
        return try {
            EncoderUtils.encryptAES(data.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())
        } catch (e: Exception) {
            e.printOnDebug()
            log(e.localizedMessage ?: "aesEncodeToByteArrayERROR")
            null
        }
    }

    fun aesEncodeToString(data: String, key: String, transformation: String, iv: String): String? {
        aesEncodeToByteArray(data, key, transformation, iv)?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun aesEncodeToBase64ByteArray(data: String, key: String, transformation: String, iv: String): ByteArray? {
        return try {
            EncoderUtils.encryptAES2Base64(data.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())
        } catch (e: Exception) {
            e.printOnDebug()
            log(e.localizedMessage ?: "aesEncodeToBase64ByteArrayERROR")
            null
        }
    }

    fun aesEncodeToBase64String(data: String, key: String, transformation: String, iv: String): String? {
        aesEncodeToBase64ByteArray(data, key, transformation, iv)?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun androidId(): String {
        return ""
    }

    fun aesDecodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
        return AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data)
    }

    fun tripleDESDecodeStr(data: String, key: String, mode: String, padding: String, iv: String): String? {
        return DESede(mode, padding, key.toByteArray(Charsets.UTF_8), iv.toByteArray(Charsets.UTF_8)).decryptStr(data)
    }

    fun tripleDESDecodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
        return DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data)
    }

    fun aesEncodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
        return AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data)
    }

    fun desDecodeToString(data: String, key: String, transformation: String, iv: String): String? {
        EncoderUtils.decryptDES(data.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun desBase64DecodeToString(data: String, key: String, transformation: String, iv: String): String? {
        EncoderUtils.decryptBase64DES(data.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun desEncodeToString(data: String, key: String, transformation: String, iv: String): String? {
        EncoderUtils.encryptDES(data.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun desEncodeToBase64String(data: String, key: String, transformation: String, iv: String): String? {
        EncoderUtils.encryptDES2Base64(data.encodeToByteArray(), key.encodeToByteArray(), transformation, iv.encodeToByteArray())?.let {
            return String(it, Charsets.UTF_8)
        }
        return null
    }

    fun tripleDESEncodeBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
        return DESede(mode, padding, key.toByteArray(Charsets.UTF_8), iv.toByteArray(Charsets.UTF_8)).encryptBase64(data)
    }

    fun tripleDESEncodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
        return DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data)
    }

    fun digestHex(data: String, algorithm: String): String? {
        return DigestUtil.digester(algorithm).digestHex(data)
    }

    fun digestBase64Str(data: String, algorithm: String): String? {
        return Base64.encodeToString(DigestUtil.digester(algorithm).digest(data), 2)
    }
}
