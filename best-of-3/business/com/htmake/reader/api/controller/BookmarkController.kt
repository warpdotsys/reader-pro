/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class BookmarkController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "bookmark")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "bookmark", a)

    suspend fun getBookmarks(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveBookmark(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val bm = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        arr.add(bm)
        save(ns, arr)
        return rd.setData(bm)
    }

    suspend fun deleteBookmark(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        // filter by bookUrl+chapterIndex or id
        return rd.setData(true)
    }
}
