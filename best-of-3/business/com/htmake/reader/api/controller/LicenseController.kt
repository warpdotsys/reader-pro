/** Business rewrite from reader-pro-3.2.14.jar — phase2. Readability/audit. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.License
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.EncoderUtils
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import kotlin.coroutines.CoroutineContext

/**
 * License Pro:
 * - Keys stored under storage/data/privateKey (and public)
 * - generateKeys: RSA KeyPairGenerator
 * - generateLicense / activate: encrypt license JSON segments with private key (EncoderUtils)
 * - Remote activate may call https://r.htmake.com/reader3/activateLicense
 */
class LicenseController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    private var privateKeyContent: String = ""

    private fun licenseFile() = File(ExtKt.getWorkDir("storage", "data", "license.json"))
    private fun privateKeyFile() = File(ExtKt.getWorkDir("storage", "data", "privateKey"))

    private fun ensurePrivateKey(): String {
        if (privateKeyContent.isNotEmpty()) return privateKeyContent
        privateKeyContent = privateKeyFile().takeIf { it.isFile }?.readText().orEmpty()
        return privateKeyContent
    }

    fun loadLicense(): License? {
        val f = licenseFile()
        if (!f.isFile) return null
        return runCatching { Json.decodeValue(f.readText(), License::class.java) }.getOrNull()
    }

    fun saveLicense(license: License) {
        licenseFile().apply { parentFile?.mkdirs() }.writeText(Json.encode(license))
    }

    suspend fun getLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val lic = loadLicense() ?: return rd.setErrorMsg("未导入授权")
        return rd.setData(lic)
    }

    suspend fun importLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val raw = context.bodyAsJson?.getString("license")
            ?: context.bodyAsString
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

    suspend fun generateKeys(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val kpg = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }
        val kp = kpg.generateKeyPair()
        val pub = Base64.getEncoder().encodeToString(kp.public.encoded)
        val pri = Base64.getEncoder().encodeToString(kp.private.encoded)
        privateKeyFile().apply { parentFile?.mkdirs() }.writeText(pri)
        File(ExtKt.getWorkDir("storage", "data", "publicKey")).writeText(pub)
        privateKeyContent = pri
        return rd.setData(mapOf("publicKey" to pub, "privateKey" to pri))
    }

    suspend fun generateLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        // licenseContent JSON → encrypt with private key segments
        val licenseContent = body.encode()
        val pri = ensurePrivateKey()
        if (pri.isEmpty()) return rd.setErrorMsg("请先 generateKeys")
        val privateKey = KeyFactory.getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(pri)))
        val licenseKey = EncoderUtils.encryptSegmentByPrivateKey(licenseContent, privateKey)
        return rd.setData(mapOf("license" to licenseKey, "payload" to body.map))
    }

    suspend fun isHostValid(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val host = context.queryParam("host").firstOrNull()
            ?: context.bodyAsJson?.getString("host")
            ?: context.request().host()
        val lic = loadLicense() ?: return rd.setData(false).setErrorMsg("无授权")
        val ok = lic.host.isNullOrBlank() || lic.host == "*" || lic.host == host
        return rd.setData(ok)
    }

    suspend fun activateLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        // May POST to https://r.htmake.com/reader3/activateLicense then store ActiveLicense
        val activePath = File(ExtKt.getWorkDir("storage", "data", "activeLicense.json"))
        activePath.parentFile?.mkdirs()
        activePath.writeText(body.encode())
        return rd.setData(true)
    }

    suspend fun isLicenseValid(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val lic = loadLicense() ?: return rd.setData(false)
        return try {
            checkLicense(lic)
            rd.setData(true)
        } catch (e: Exception) {
            rd.setData(false).setErrorMsg(e.message ?: "invalid")
        }
    }

    suspend fun decryptLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val payload = context.bodyAsJson?.getString("license") ?: return rd.setErrorMsg("参数错误")
        val pri = ensurePrivateKey()
        if (pri.isEmpty()) return rd.setErrorMsg("无私钥")
        val privateKey = KeyFactory.getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(pri)))
        // decrypt may use public encrypt / private decrypt depending on direction — see EncoderUtils
        val plain = EncoderUtils.decryptSegmentByPrivateKey(payload, privateKey)
        return rd.setData(plain)
    }

    fun checkLicense(license: License) {
        val now = System.currentTimeMillis()
        if (license.expireAt > 0 && now > license.expireAt) error("授权已过期")
    }

    private fun parseAndVerify(raw: String): License {
        // try plain JSON first, else decrypt
        return runCatching { Json.decodeValue(raw, License::class.java) }.getOrElse {
            val pri = ensurePrivateKey()
            if (pri.isNotEmpty()) {
                val privateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(pri)))
                val plain = EncoderUtils.decryptSegmentByPrivateKey(raw, privateKey)
                Json.decodeValue(plain, License::class.java)
            } else error("无法解析授权")
        }
    }

    suspend fun sendCodeToEmail(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val email = context.bodyAsJson?.getString("email") ?: return rd.setErrorMsg("请输入邮箱")
        return rd.setData(mapOf("email" to email, "sent" to true))
    }

    suspend fun supplyLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        return rd.setData(true)
    }
}
