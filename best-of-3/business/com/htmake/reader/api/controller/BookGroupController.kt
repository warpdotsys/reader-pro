/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class BookGroupController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "bookGroup")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "bookGroup", a)

    suspend fun getBookGroups(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveBookGroup(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val g = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val id = g.getInteger("groupId") ?: g.getLong("groupId")?.toInt()
        val list = arr.list
        var found = false
        if (id != null) {
            for (i in list.indices) {
                if (arr.getJsonObject(i).getInteger("groupId") == id) {
                    list[i] = g; found = true; break
                }
            }
        }
        if (!found) list.add(g)
        save(ns, JsonArray(list))
        return rd.setData(g)
    }

    suspend fun deleteBookGroup(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val id = context.bodyAsJson?.getInteger("groupId")
            ?: context.queryParam("groupId").firstOrNull()?.toIntOrNull()
            ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getInteger("groupId") != id }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    suspend fun saveBookGroupOrder(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val order = context.bodyAsJsonArray ?: return rd.setErrorMsg("参数错误")
        save(ns, order)
        return rd.setData(true)
    }
}
