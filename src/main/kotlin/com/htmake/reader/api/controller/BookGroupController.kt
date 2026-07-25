package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import com.htmake.reader.utils.asJsonArray
import com.htmake.reader.utils.asJsonObject
import io.legado.app.data.entities.BookGroup
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class BookGroupController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<BookGroup> {
    override val tableName: String
        get() = "bookGroup"

    override fun onList(var1: JsonArray, userNameSpace: String): JsonArray {
        if (var1.size() == 0) {
            val defaults = """
            [{"groupId":-1,"groupName":"全部","order":-10,"show":true},{"groupId":-2,"groupName":"本地","order":-9,"show":true},{"groupId":-3,"groupName":"音频","order":-8,"show":true},{"groupId":-4,"groupName":"未分组","order":-7,"show":true},{"groupId":-5,"groupName":"更新错误","order":-6,"show":true}]
            """.asJsonArray()
            if (defaults != null) {
                saveUserStorage(userNameSpace, "bookGroup", defaults)
                return defaults
            }
        }
        return var1
    }

    override fun checker(var1: JsonObject, var2: BookGroup): Boolean =
        var2.groupId == var1.getLong("groupId")

    override fun onCheckEnd(var1: BookGroup, existed: Boolean, items: JsonArray) {
        if (existed) {
            return
        }

        var maxOrder = 0
        var groupIdSum = 0L
        for (item in items) {
            val group = item.asJsonObject()
            maxOrder = maxOf(maxOrder, group?.getInteger("order", 0) ?: 0)
            groupIdSum += maxOf(group?.getLong("groupId", 0L) ?: 0L, 0L)
        }

        var groupId = 1L
        while ((groupId and groupIdSum) != 0L) {
            groupId = groupId shl 1
        }
        var1.groupId = groupId
        var1.order = maxOrder + 1
    }

    override fun beforeSave(var1: BookGroup, db: DB<BookGroup>): ReturnData? {
        if (var1.groupName.isEmpty()) {
            return ReturnData().setErrorMsg("分组名称不能为空")
        }
        return null
    }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<BookGroup>
        get() = BookGroup::class.java

    suspend fun saveBookGroupOrder(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }

        val bookGroupOrder = context.bodyAsJson.getJsonArray("order", null)
            ?: return result.setErrorMsg("参数错误")
        val userNameSpace = getUserNameSpace(context)
        var bookGroupList = getUserStorage(userNameSpace, "bookGroup").asJsonArray() ?: JsonArray()
        val orderMap = linkedMapOf<Long, Int>()
        for (index in 0 until bookGroupOrder.size()) {
            val order = bookGroupOrder.getJsonObject(index)
            orderMap[order.getLong("groupId")] = order.getInteger("order")
        }

        val values = bookGroupList.list
        for (index in 0 until bookGroupList.size()) {
            val bookGroup = bookGroupList.getJsonObject(index).mapTo(BookGroup::class.java)
            if (orderMap.containsKey(bookGroup.groupId)) {
                bookGroup.order = orderMap[bookGroup.groupId] ?: bookGroup.order
                values[index] = JsonObject.mapFrom(bookGroup)
            }
        }
        bookGroupList = JsonArray(values)
        saveUserStorage(userNameSpace, "bookGroup", bookGroupList)
        return result.setData("")
    }
}
