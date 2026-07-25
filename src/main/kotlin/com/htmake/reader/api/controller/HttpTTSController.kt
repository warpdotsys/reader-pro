package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import com.htmake.reader.utils.asJsonArray
import io.legado.app.data.entities.HttpTTS
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class HttpTTSController(
    coroutineContext: CoroutineContext,
) : BaseController(coroutineContext), CURD<HttpTTS> {
    override val tableName: String
        get() = "httpTTS"

    override fun checker(var1: JsonObject, var2: HttpTTS): Boolean =
        var2.name == var1.getString("name")

    override fun beforeSave(var1: HttpTTS, db: DB<HttpTTS>): ReturnData? = when {
        var1.name.isEmpty() -> ReturnData().setErrorMsg("名称不能为空")
        var1.url.isEmpty() -> ReturnData().setErrorMsg("链接不能为空")
        else -> null
    }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<HttpTTS>
        get() = HttpTTS::class.java

    override fun convertToEntity(var1: JsonObject): HttpTTS =
        HttpTTS.fromJson(var1.toString()).getOrThrow()

    override fun convertToEntityList(var1: String): Array<HttpTTS> =
        var1.asJsonArray()
            ?.map { item -> HttpTTS.fromJson(item.toString()).getOrThrow() }
            ?.toTypedArray()
            ?: emptyArray()
}
