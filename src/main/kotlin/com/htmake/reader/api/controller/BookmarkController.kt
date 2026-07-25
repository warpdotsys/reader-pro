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

    override fun checker(var1: JsonObject, var2: Bookmark): Boolean =
        var2.time.equals(var1.getLong("time"))

    override fun beforeSave(var1: Bookmark, db: DB<Bookmark>): ReturnData? =
        if (var1.bookName.isEmpty() || var1.bookAuthor.isEmpty()) {
            ReturnData().setErrorMsg("书签信息错误")
        } else {
            null
        }

    override suspend fun checkUserAuth(context: RoutingContext): Boolean = checkAuth(context)

    override fun getUserNS(context: RoutingContext): String = getUserNameSpace(context)

    override val entityClass: Class<Bookmark>
        get() = Bookmark::class.java
}
