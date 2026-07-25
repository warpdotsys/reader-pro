package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import com.htmake.reader.utils.gson
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.lang.reflect.Array as ReflectArray
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

interface CURD<T> {
    val tableName: String

    fun convertToEntity(var1: JsonObject): T = var1.mapTo(entityClass)

    @Suppress("UNCHECKED_CAST")
    fun convertToEntityList(var1: String): Array<T> =
        gson.fromJson(var1, ReflectArray.newInstance(entityClass, 0).javaClass) as Array<T>

    fun onList(var1: JsonArray, userNameSpace: String): JsonArray = var1

    fun checker(var1: JsonObject, var2: T): Boolean

    fun onCheckEnd(var1: T, existed: Boolean, items: JsonArray) = Unit

    fun beforeSave(var1: T, db: DB<T>): ReturnData? = null

    fun beforeAdd(var1: T, db: DB<T>): ReturnData? = null

    fun beforeDelete(var1: T, db: DB<T>): ReturnData? = null

    suspend fun checkUserAuth(context: RoutingContext): Boolean

    fun getUserNS(context: RoutingContext): String

    val entityClass: Class<T>

    suspend fun list(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkUserAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("\u8BF7\u767B\u5F55\u540E\u4F7F\u7528")
        }

        val userNameSpace = getUserNS(context)
        val values = onList(DB.table<T>(userNameSpace, tableName).readAll(), userNameSpace).list
        return result.setData(values)
    }

    suspend fun save(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkUserAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("\u8BF7\u767B\u5F55\u540E\u4F7F\u7528")
        }

        val value = convertToEntity(context.bodyAsJson)
        val db = DB.table<T>(getUserNS(context), tableName)
        beforeSave(value, db)?.let { return it }
        db.save(value, ::onCheckEnd, ::checker)
        return result.setData("")
    }

    suspend fun saveMulti(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkUserAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("\u8BF7\u767B\u5F55\u540E\u4F7F\u7528")
        }

        val values = convertToEntityList(context.bodyAsString)
        if (values.isEmpty()) {
            return result.setErrorMsg("\u53C2\u6570\u9519\u8BEF")
        }
        val db = DB.table<T>(getUserNS(context), tableName)
        values.forEach { value -> beforeSave(value, db)?.let { return it } }
        db.saveMulti(values, ::onCheckEnd, ::checker)
        return result.setData("")
    }

    suspend fun delete(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkUserAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("\u8BF7\u767B\u5F55\u540E\u4F7F\u7528")
        }

        val value = convertToEntity(context.bodyAsJson)
        val db = DB.table<T>(getUserNS(context), tableName)
        beforeDelete(value, db)?.let { return it }
        db.delete { item -> checker(item, value) }
        return result.setData("")
    }

    suspend fun deleteMulti(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkUserAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("\u8BF7\u767B\u5F55\u540E\u4F7F\u7528")
        }

        val values = convertToEntityList(context.bodyAsString)
        if (values.isEmpty()) {
            return result.setErrorMsg("\u53C2\u6570\u9519\u8BEF")
        }
        val db = DB.table<T>(getUserNS(context), tableName)
        values.forEach { value -> beforeDelete(value, db)?.let { return it } }
        db.delete { item -> values.any { value -> checker(item, value) } }
        return result.setData("")
    }
}
