/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

/**
 * 替换规则 CRUD。
 * 字段：name, pattern/regex, replacement, isRegex, isEnabled, scope(content|title|all),
 *       timeout/timeoutMillisecond, bookName
 */
class ReplaceRuleController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "replaceRule")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "replaceRule", a)

    suspend fun getReplaceRules(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveReplaceRule(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        // multi
        val arrBody = context.bodyAsJsonArray
        if (arrBody != null) {
            save(ns, normalizeArray(arrBody))
            return rd.setData(arrBody.size())
        }
        val rule = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        normalizeRule(rule)
        val name = rule.getString("name") ?: ""
        val arr = load(ns)
        val list = arr.list
        var found = false
        if (name.isNotEmpty()) {
            for (i in list.indices) {
                val o = arr.getJsonObject(i)
                if (o.getString("name") == name) {
                    list[i] = rule
                    found = true
                    break
                }
            }
        }
        if (!found) list.add(rule)
        save(ns, JsonArray(list))
        return rd.setData(rule)
    }

    suspend fun deleteReplaceRule(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson
        val names = mutableSetOf<String>()
        body?.getString("name")?.let { names += it }
        body?.getJsonArray("names")?.forEach { names += it.toString() }
        context.queryParam("name").forEach { names += it }
        if (names.isEmpty() && context.bodyAsJsonArray != null) {
            context.bodyAsJsonArray.forEach { v ->
                when (v) {
                    is JsonObject -> v.getString("name")?.let { names += it }
                    is String -> names += v
                }
            }
        }
        if (names.isEmpty()) return rd.setErrorMsg("name 不能为空")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("name") !in names
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    private fun normalizeArray(arr: JsonArray): JsonArray {
        for (i in 0 until arr.size()) {
            arr.getJsonObject(i)?.let { normalizeRule(it) }
        }
        return arr
    }

    private fun normalizeRule(o: JsonObject) {
        if (!o.containsKey("pattern") && o.containsKey("regex")) {
            o.put("pattern", o.getString("regex"))
        }
        if (!o.containsKey("isEnabled") && o.containsKey("enable")) {
            o.put("isEnabled", o.getBoolean("enable", true))
        }
        if (!o.containsKey("timeout") && o.containsKey("timeoutMillisecond")) {
            o.put("timeout", o.getLong("timeoutMillisecond"))
        }
        if (!o.containsKey("scope")) o.put("scope", "content")
        if (!o.containsKey("timeout")) o.put("timeout", 3000)
    }
}
