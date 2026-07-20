package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import io.legado.app.data.entities.Bookmark
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class BookmarkController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<Bookmark> {
    override val tableName: String
        get() = "bookmark"

    override fun checker(item: JsonObject, value: Bookmark): Boolean =
        value.time == item.getLong("time")

    override fun beforeSave(value: Bookmark, db: DB<Bookmark>): ReturnData? {
        if (value.bookName.isEmpty() && value.bookAuthor.isEmpty()) {
            return ReturnData().setErrorMsg("书签信息错误")
        }
        return null
    }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<Bookmark>
        get() = Bookmark::class.java
}
