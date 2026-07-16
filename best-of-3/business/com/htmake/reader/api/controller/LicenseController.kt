package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.ActiveLicense
import com.htmake.reader.entity.License
import com.htmake.reader.utils.EncoderUtils
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * License Pro API surface:
 * keys under storage/data/{publicKey,privateKey}; license.json; activeLicense.json
 */
class LicenseController(cc: CoroutineContext) : BaseController(cc) {

    private var privateKeyCache: String = ""

    private fun licenseFile() = File(ExtKt.getWorkDir("storage", "data", "license.json"))
    private fun privateKeyFile() = File(ExtKt.getWorkDir("storage", "data", "privateKey"))
    private fun publicKeyFile() = File(ExtKt.getWorkDir("storage", "data", "publicKey"))
    private fun activeFile() = File(ExtKt.getWorkDir("storage", "data", "activeLicense.json"))

    private fun ensurePrivateKey(): String {
        if (privateKeyCache.isNotEmpty()) return privateKeyCache
        privateKeyCache = privateKeyFile().takeIf { it.isFile }?.readText().orEmpty()
        return privateKeyCache
    }

    fun loadLicense(): License? {
        val f = licenseFile()
        if (!f.isFile) {
            // legacy storage path via ExtKt
            val raw = ExtKt.getStorage("data", "license") ?: return null
            return runCatching { Json.decodeValue(raw, License::class.java) }.getOrNull()
        }
        return runCatching { Json.decodeValue(f.readText(), License::class.java) }.getOrNull()
    }

    fun saveLicense(license: License) {
        licenseFile().apply { parentFile?.mkdirs() }.writeText(Json.encode(license))
        ExtKt.saveStorage(arrayOf("data", "license"), Json.encode(license))
    }

    fun checkLicense(license: License) {
        if (license.isExpired()) error("授权已过期")
    }

    suspend fun getLicense(ctx: RoutingContext): ReturnData {
        val lic = loadLicense() ?: return ReturnData().setErrorMsg("未导入授权")
        return ReturnData().setData(
            mapOf(
                "host" to lic.host,
                "email" to lic.email,
                "code" to lic.code,
                "userMax" to lic.userMax,
                "expireAt" to lic.expireAt,
                "activated" to lic.activated,
                "activatedAt" to lic.activatedAt,
                "expired" to lic.isExpired()
            )
        )
    }

    suspend fun importLicense(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        val raw = ctx.bodyAsJson?.getString("license")
            ?: ctx.bodyAsJson?.encode()
            ?: ctx.bodyAsString
            ?: return rd.setErrorMsg("请输入授权内容")
        return try {
            val lic = parseAndVerify(raw)
            checkLicense(lic)
            saveLicense(lic)
            rd.setData(lic)
        } catch (e: Exception) {
            rd.setErrorMsg("授权无效: ${e.message}")
        }
    }

    suspend fun generateKeys(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val (pub, pri) = EncoderUtils.genRsaPair()
        privateKeyFile().apply { parentFile?.mkdirs() }.writeText(pri)
        publicKeyFile().writeText(pub)
        privateKeyCache = pri
        return rd.setData(mapOf("publicKey" to pub, "privateKey" to pri))
    }

    suspend fun generateLicense(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val host = body.getString("host") ?: "*"
        val email = body.getString("email") ?: ""
        val userMax = body.getInteger("userMax") ?: body.getInteger("userLimit") ?: 50
        val expireAt = body.getLong("expireAt")
            ?: body.getLong("expiredAt")
            ?: (System.currentTimeMillis() + 365L * 24 * 3600 * 1000)
        val lic = License(
            host = host,
            email = email,
            code = body.getString("code") ?: "DEMO",
            userMax = userMax,
            expireAt = expireAt,
            simpleWebExpiredAt = body.getLong("simpleWebExpiredAt") ?: 0,
            activated = false
        )
        val pri = ensurePrivateKey()
        if (pri.isEmpty()) {
            // still return plaintext license for offline demo
            return rd.setData(mapOf("license" to Json.encode(lic), "payload" to lic, "encrypted" to false))
        }
        val privateKey = EncoderUtils.privateKeyFromBase64(pri)
        val enc = EncoderUtils.encryptSegmentByPrivateKey(Json.encode(lic), privateKey)
        return rd.setData(mapOf("license" to enc, "payload" to lic, "encrypted" to true))
    }

    suspend fun isHostValid(ctx: RoutingContext): ReturnData {
        val host = ctx.queryParam("host").firstOrNull()
            ?: ctx.bodyAsJson?.getString("host")
            ?: ctx.request().host()
        val lic = loadLicense() ?: return ReturnData().setData(mapOf("isValid" to false)).setErrorMsg("无授权")
        val ok = !lic.isExpired() && lic.validHost(host)
        return ReturnData().setData(mapOf("isValid" to ok, "host" to host, "licenseHost" to lic.host))
    }

    suspend fun activateLicense(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = ctx.bodyAsJson ?: JsonObject()
        // optional email code check
        val email = body.getString("email")
        val code = body.getString("code")
        if (!email.isNullOrBlank() && !code.isNullOrBlank()) {
            if (!com.htmake.reader.help.EmailCodeStore.verify(email, code)) {
                return rd.setErrorMsg("验证码错误或已过期")
            }
        }
        val lic = loadLicense()
        if (lic != null) {
            try {
                checkLicense(lic)
            } catch (e: Exception) {
                return rd.setErrorMsg(e.message ?: "授权无效")
            }
            lic.activated = true
            lic.activatedAt = System.currentTimeMillis()
            if (lic.host.isNullOrBlank()) {
                lic.host = body.getString("host") ?: ctx.request().host()
            }
            if (!email.isNullOrBlank()) lic.email = email
            saveLicense(lic)
        }
        val active = ActiveLicense(
            licenseId = body.getString("licenseId") ?: lic?.code,
            activatedAt = System.currentTimeMillis(),
            host = body.getString("host") ?: ctx.request().host(),
            result = "ok"
        )
        activeFile().apply { parentFile?.mkdirs() }.writeText(Json.encode(active))
        var remote: Map<String, Any?>? = null
        if (appConfig.remoteActivateEnabled && appConfig.remoteActivateUrl.isNotBlank()) {
            remote = com.htmake.reader.help.RemoteLicenseClient.activateRemote(
                body.encode(),
                appConfig.remoteActivateUrl
            )
        }
        val pri = ensurePrivateKey()
        if (pri.isNotEmpty()) {
            val enc = EncoderUtils.encryptSegmentByPrivateKey(Json.encode(active), EncoderUtils.privateKeyFromBase64(pri))
            return rd.setData(mapOf("result" to enc, "activated" to true, "remote" to remote))
        }
        return rd.setData(mapOf("activated" to true, "active" to active, "remote" to remote))
    }

    suspend fun isLicenseValid(ctx: RoutingContext): ReturnData {
        val lic = loadLicense() ?: return ReturnData().setData(false).setErrorMsg("未导入授权")
        return try {
            checkLicense(lic)
            ReturnData().setData(true)
        } catch (e: Exception) {
            ReturnData().setData(false).setErrorMsg(e.message ?: "invalid")
        }
    }

    suspend fun decryptLicense(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        val payload = ctx.bodyAsJson?.getString("license") ?: return rd.setErrorMsg("参数错误")
        return try {
            val plain = tryDecrypt(payload)
            rd.setData(mapOf("plain" to plain, "license" to runCatching {
                Json.decodeValue(plain, License::class.java)
            }.getOrNull()))
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "解密失败")
        }
    }

    suspend fun sendCodeToEmail(ctx: RoutingContext): ReturnData {
        val email = ctx.bodyAsJson?.getString("email") ?: return ReturnData().setErrorMsg("请输入邮箱")
        if (!email.contains("@")) return ReturnData().setErrorMsg("邮箱格式错误")
        val code = com.htmake.reader.help.EmailCodeStore.generateCode(6)
        com.htmake.reader.help.EmailCodeStore.put(email, code)
        // SMTP not wired — in debug/non-secure return code for local testing
        val data = linkedMapOf<String, Any?>(
            "email" to email,
            "sent" to true,
            "ttlMinutes" to 10,
            "note" to "SMTP未配置；secure=false 时返回 code 便于联调"
        )
        if (!appConfig.secure || appConfig.debug) {
            data["code"] = code
        }
        return ReturnData().setData(data)
    }

    suspend fun supplyLicense(ctx: RoutingContext): ReturnData = ReturnData().setData(true)

    private fun parseAndVerify(raw: String): License {
        val trimmed = raw.trim()
        // plain JSON object
        if (trimmed.startsWith("{")) {
            return Json.decodeValue(trimmed, License::class.java)
        }
        // nested { "license": "..." }
        runCatching {
            val o = JsonObject(trimmed)
            o.getString("license")?.let { return parseAndVerify(it) }
        }
        val plain = tryDecrypt(trimmed)
        return Json.decodeValue(plain, License::class.java)
    }

    private fun tryDecrypt(payload: String): String {
        val pri = ensurePrivateKey()
        if (pri.isNotEmpty()) {
            runCatching {
                return EncoderUtils.decryptSegmentByPrivateKey(payload, EncoderUtils.privateKeyFromBase64(pri))
            }
            runCatching {
                return EncoderUtils.rsaDecrypt(pri, payload)
            }
        }
        val pub = publicKeyFile().takeIf { it.isFile }?.readText()
        if (!pub.isNullOrBlank()) {
            runCatching {
                return EncoderUtils.decryptSegmentByPublicKey(payload, EncoderUtils.publicKeyFromBase64(pub))
            }
        }
        error("无法解密授权（缺少密钥或格式错误）")
    }
}
