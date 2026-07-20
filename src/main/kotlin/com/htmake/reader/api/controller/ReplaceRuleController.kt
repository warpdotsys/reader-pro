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

    override fun checker(item: JsonObject, value: ReplaceRule): Boolean =
        value.name == item.getString("name")

    override fun beforeSave(value: ReplaceRule, db: DB<ReplaceRule>): ReturnData? {
        if (value.name.isEmpty()) {
            return ReturnData().setErrorMsg("名称不能为空")
        }
        if (value.pattern.isEmpty()) {
            return ReturnData().setErrorMsg("规则不能为空")
        }
        return null
    }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<ReplaceRule>
        get() = ReplaceRule::class.java
}
