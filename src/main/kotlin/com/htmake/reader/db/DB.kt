package com.htmake.reader.db

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

open class DB<T>(
    val userNameSpace: String,
    val name: String,
) {
    var cachedValue: JsonArray = JsonArray()

    open fun readAll(): JsonArray = JsonArray()

    open fun <P : Any> findBy(field: String, value: P, clazz: Class<T>): T? = null

    open fun save(
        value: T,
        onCheckEnd: ((T, Boolean, JsonArray) -> Unit)?,
        checker: (JsonObject, T) -> Boolean,
    ) {}

    open fun saveMulti(
        value: Array<T>,
        onCheckEnd: ((T, Boolean, JsonArray) -> Unit)?,
        checker: (JsonObject, T) -> Boolean,
    ) {}

    open fun delete(checker: (JsonObject) -> Boolean) {}

    open fun save() {}

    companion object {
        fun <T> table(userNameSpace: String, name: String, driver: String = "JSON"): DB<T> =
            when (driver) {
                "SQL" -> SQLTable(userNameSpace, name)
                else -> JSONTable(userNameSpace, name)
            }
    }
}
