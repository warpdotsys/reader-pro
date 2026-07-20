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

    override fun checker(item: JsonObject, value: HttpTTS): Boolean =
        value.name == item.getString("name")

    override fun beforeSave(value: HttpTTS, db: DB<HttpTTS>): ReturnData? = when {
        value.name.isEmpty() -> ReturnData().setErrorMsg("名称不能为空")
        value.url.isEmpty() -> ReturnData().setErrorMsg("链接不能为空")
        else -> null
    }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<HttpTTS>
        get() = HttpTTS::class.java

    override fun convertToEntity(json: JsonObject): HttpTTS =
        HttpTTS.fromJson(json.toString()).getOrThrow()

    override fun convertToEntityList(content: String): Array<HttpTTS> =
        content.asJsonArray()
            ?.map { item -> HttpTTS.fromJson(item.toString()).getOrThrow() }
            ?.toTypedArray()
            ?: emptyArray()
}
