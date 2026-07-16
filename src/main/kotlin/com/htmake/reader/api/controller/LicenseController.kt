package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.License
import com.htmake.reader.utils.EncoderUtils
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class LicenseController(cc: CoroutineContext) : BaseController(cc) {
    suspend fun getLicense(ctx: RoutingContext): ReturnData {
        val raw = ExtKt.getStorage("data", "license")
        return ReturnData().setData(ExtKt.asJsonObject(raw)?.map ?: emptyMap<String, Any>())
    }

    suspend fun importLicense(ctx: RoutingContext): ReturnData {
        val body = ctx.bodyAsJson ?: return ReturnData().setErrorMsg("参数错误")
        ExtKt.saveStorage(arrayOf("data", "license"), body.encode())
        return ReturnData().setData(true)
    }

    suspend fun generateKeys(ctx: RoutingContext): ReturnData {
        val (pub, pri) = EncoderUtils.genRsaPair()
        return ReturnData().setData(mapOf("publicKey" to pub, "privateKey" to pri))
    }

    suspend fun generateLicense(ctx: RoutingContext): ReturnData {
        val body = ctx.bodyAsJson ?: JsonObject()
        val lic = License(
            host = body.getString("host") ?: "",
            email = body.getString("email") ?: "",
            code = body.getString("code") ?: "DEMO",
            expireAt = System.currentTimeMillis() + 365L * 24 * 3600 * 1000,
            activated = false
        )
        return ReturnData().setData(lic)
    }

    suspend fun isHostValid(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun activateLicense(ctx: RoutingContext): ReturnData {
        val raw = ExtKt.getStorage("data", "license")
        val o = ExtKt.asJsonObject(raw) ?: JsonObject()
        o.put("activated", true)
        ExtKt.saveStorage(arrayOf("data", "license"), o.encode())
        return ReturnData().setData(true)
    }
    suspend fun isLicenseValid(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun decryptLicense(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyMap<String, Any>())
    suspend fun sendCodeToEmail(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun supplyLicense(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
}
