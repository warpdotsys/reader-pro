package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.License
import com.htmake.reader.utils.decryptToLicense
import com.htmake.reader.utils.getInstalledLicense
import com.htmake.reader.utils.getRandomString
import com.htmake.reader.utils.getStorage
import com.htmake.reader.utils.jsonEncode
import com.htmake.reader.utils.saveStorage
import com.htmake.reader.utils.sendEmail
import com.htmake.reader.utils.setLicenseValid
import com.htmake.reader.utils.success
import com.htmake.reader.utils.validateEmail
import io.legado.app.utils.ACache
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import kotlinx.coroutines.launch
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class LicenseController(
    coroutineContext: CoroutineContext,
) : BaseController(coroutineContext) {
    private val webClient: WebClient by lazy { WebClient.create(Vertx.vertx()) }
    private var privateKeyContent = ""
    private var tryCodeCache = ACache.get("tryCodeCache", 2_000_000L, 10_000)
    val backupFileNames: Array<String> by lazy {
        arrayOf(
            "bookShelf.json",
            "bookSource.json",
            "rssSource.json",
            "replaceRule.json",
            "bookmark.json",
            "bookGroup.json",
            "userConfig.json",
        )
    }

    suspend fun getLicense(@Suppress("UNUSED_PARAMETER") context: RoutingContext): ReturnData =
        ReturnData().setData(mapOf("license" to getInstalledLicense()))

    suspend fun importLicense(context: RoutingContext) {
        val result = ReturnData()
        if (!checkAuth(context)) {
            context.success(result.setData("NEED_LOGIN").setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"))
            return
        }
        if (!checkManagerAuth(context)) {
            context.success(result.setData("NEED_SECURE_KEY").setErrorMsg("\u9700\u8981\u7ba1\u7406\u5bc6\u7801"))
            return
        }
        val content = context.bodyAsJson?.getString("content").orEmpty()
        if (content.isEmpty()) {
            context.success(result.setErrorMsg("\u53c2\u6570\u9519\u8bef"))
            return
        }
        saveStorage("data", "license", ext = ".key", value = content)
        decryptToLicense(content)?.let { checkLicense(it) }
        context.success(result.setData(""))
    }

    suspend fun generateKeys(@Suppress("UNUSED_PARAMETER") context: RoutingContext): ReturnData {
        val pair = EncoderUtils.generateKeys()
        return ReturnData().setData(
            mapOf(
                "publicKey" to Base64.encodeToString(pair.public.encoded, Base64.NO_WRAP),
                "privateKey" to Base64.encodeToString(pair.private.encoded, Base64.NO_WRAP),
            )
        )
    }

    suspend fun generateLicense(context: RoutingContext): ReturnData {
        val body = context.bodyAsJson
        val host = if (context.request().method().name == "POST") body?.getString("host").orEmpty()
            else context.queryParam("host").firstOrNull().orEmpty()
        val expiredAt = body?.getLong("expiredAt") ?: context.queryParam("expiredAt").firstOrNull()?.toLongOrNull() ?: 0L
        val userMaxLimit = body?.getInteger("userMaxLimit") ?: context.queryParam("userMaxLimit").firstOrNull()?.toIntOrNull() ?: 15
        val openApi = body?.getBoolean("openApi") ?: context.queryParam("openApi").firstOrNull()?.toBoolean() ?: false
        val simpleWebExpiredAt = body?.getLong("simpleWebExpiredAt") ?: 0L
        val instances = body?.getInteger("instances") ?: 1
        val type = body?.getString("type").orEmpty()
        val code = body?.getString("code").orEmpty()
        val license = License(
            host = host,
            userMaxLimit = userMaxLimit,
            expiredAt = expiredAt,
            openApi = openApi,
            simpleWebExpiredAt = simpleWebExpiredAt,
            instances = instances,
            type = type,
            code = code,
        )
        val privateKey = loadPrivateKey() ?: return ReturnData().setErrorMsg("\u79c1\u94a5\u4e0d\u5b58\u5728")
        return ReturnData().setData(mapOf("key" to EncoderUtils.encryptSegmentByPrivateKey(jsonEncode(license), privateKey)))
    }

    suspend fun isHostValid(context: RoutingContext): ReturnData {
        val host = context.queryParam("host").firstOrNull() ?: context.request().host()
        return ReturnData().setData(getInstalledLicense().validHost(host))
    }

    suspend fun decryptLicense(context: RoutingContext): ReturnData {
        val content = context.bodyAsJson?.getString("key").orEmpty()
        return decryptToLicense(content)?.let { ReturnData().setData(mapOf("license" to it)) }
            ?: ReturnData().setErrorMsg("\u6388\u6743\u7801\u9519\u8bef")
    }

    suspend fun activateLicense(context: RoutingContext): ReturnData {
        val content = context.bodyAsJson?.getString("key").orEmpty()
        val license = decryptToLicense(content) ?: return ReturnData().setErrorMsg("\u6388\u6743\u7801\u9519\u8bef")
        saveStorage("data", "license", ext = ".key", value = content)
        checkLicense(license)
        return ReturnData().setData("")
    }

    suspend fun isLicenseValid(@Suppress("UNUSED_PARAMETER") context: RoutingContext): ReturnData =
        ReturnData().setData(getInstalledLicense().isValid())

    suspend fun checkLicense(license: License) {
        if (!license.isValid()) {
            setLicenseValid(false)
            return
        }
        setLicenseValid(true)
        launch {
            webClient
            logger.debug { "license checked for ${license.host}" }
        }
    }

    suspend fun sendCodeToEmail(context: RoutingContext): ReturnData {
        val email = context.bodyAsJson?.getString("email").orEmpty()
        if (!validateEmail(email)) return ReturnData().setErrorMsg("\u90ae\u7bb1\u5730\u5740\u683c\u5f0f\u9519\u8bef")
        val code = getRandomString(6)
        tryCodeCache.put(email, code, 900)
        sendEmail(email, "Reader Kindle\u6388\u6743\u7801", "\u60a8\u7684 Reader Kindle \u8bd5\u7528\u6388\u6743\u7801\u4e3a: $code")
        return ReturnData().setData("", "\u9a8c\u8bc1\u7801\u5df2\u53d1\u9001")
    }

    suspend fun supplyLicense(context: RoutingContext): ReturnData {
        val email = context.bodyAsJson?.getString("email").orEmpty()
        val code = context.bodyAsJson?.getString("code").orEmpty()
        if (email.isEmpty() || code.isEmpty()) return ReturnData().setErrorMsg("\u53c2\u6570\u9519\u8bef")
        val expected = tryCodeCache.getAsString(email)
        tryCodeCache.remove(email)
        if (code != expected) return ReturnData().setErrorMsg("\u9a8c\u8bc1\u7801\u9519\u8bef")
        val trial = License(expiredAt = System.currentTimeMillis() + 604_800_000L, type = "trial", code = email)
        val privateKey = loadPrivateKey() ?: return ReturnData().setErrorMsg("\u79c1\u94a5\u4e0d\u5b58\u5728")
        return ReturnData().setData(mapOf("key" to EncoderUtils.encryptSegmentByPrivateKey(jsonEncode(trial), privateKey)))
    }

    private fun loadPrivateKey() = runCatching {
        if (privateKeyContent.isEmpty()) privateKeyContent = getStorage("data", "privateKey", ext = ".key").orEmpty()
        if (privateKeyContent.isEmpty()) null else KeyFactory.getInstance("RSA").generatePrivate(
            PKCS8EncodedKeySpec(Base64.decode(privateKeyContent, Base64.NO_WRAP))
        )
    }.getOrNull()
}
