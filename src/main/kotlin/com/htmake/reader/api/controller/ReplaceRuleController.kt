package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import io.legado.app.data.entities.ReplaceRule
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class ReplaceRuleController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<ReplaceRule> {
    override val tableName: String
        get() = "replaceRule"

    override fun checker(var1: JsonObject, var2: ReplaceRule): Boolean =
        var2.name == var1.getString("name")

    override fun beforeSave(var1: ReplaceRule, db: DB<ReplaceRule>): ReturnData? {
        if (var1.name.isEmpty()) {
            return ReturnData().setErrorMsg("名称不能为空")
        }
        if (var1.pattern.isEmpty()) {
            return ReturnData().setErrorMsg("规则不能为空")
        }
        return null
    }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<ReplaceRule>
        get() = ReplaceRule::class.java
}
