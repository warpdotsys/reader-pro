/** Business rewrite from reader-pro-3.2.14.jar — phase2. Readability/audit. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class HttpTTSController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "httpTTS")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "httpTTS", a)

    suspend fun list(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun save(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val item = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        arr.add(item)
        save(ns, arr)
        return rd.setData(item)
    }

    suspend fun saveMulti(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val arr = context.bodyAsJsonArray ?: return rd.setErrorMsg("参数错误")
        save(ns, arr)
        return rd.setData(arr.size())
    }

    suspend fun delete(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val name = context.bodyAsJson?.getString("name")
            ?: context.queryParam("name").firstOrNull()
        val id = context.bodyAsJson?.getLong("id")
            ?: context.queryParam("id").firstOrNull()?.toLongOrNull()
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            val o = arr.getJsonObject(i)
            when {
                name != null && o.getString("name") == name -> false
                id != null && o.getLong("id") == id -> false
                else -> true
            }
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    suspend fun deleteMulti(context: RoutingContext): ReturnData = delete(context)
}
