package com.htmake.reader.db

import com.htmake.reader.utils.asJsonArray
import com.htmake.reader.utils.getStorage
import com.htmake.reader.utils.saveStorage
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class JSONTable<T>(userNameSpace: String, name: String) : DB<T>(userNameSpace, name) {
    override fun readAll(): JsonArray {
        var dataList = getStorage("data", userNameSpace, name).asJsonArray() ?: JsonArray()
        cachedValue = dataList
        return dataList
    }

    override fun <P : Any> findBy(field: String, value: P, clazz: Class<T>): T? {
        val dataList = readAll()
        for (i in 0 until dataList.size()) {
            if (value == dataList.getJsonObject(i).getValue(field)) {
                return dataList.getJsonObject(i).mapTo(clazz)
            }
        }
        return null
    }

    override fun save(
        value: T,
        onCheckEnd: ((T, Boolean, JsonArray) -> Unit)?,
        checker: (JsonObject, T) -> Boolean,
    ) {
        var dataList = readAll()
        var existIndex = -1
        for (i in 0 until dataList.size()) {
            val item: JsonObject = dataList.getJsonObject(i)
            if (checker(item, value)) {
                existIndex = i
                break
            }
        }

        onCheckEnd?.invoke(value, existIndex >= 0, dataList)
        if (existIndex >= 0) {
            val values = dataList.list
            values[existIndex] = JsonObject.mapFrom(value)
            dataList = JsonArray(values)
        } else {
            dataList.add(JsonObject.mapFrom(value))
        }
        cachedValue = dataList
        save()
    }

    override fun saveMulti(
        value: Array<T>,
        onCheckEnd: ((T, Boolean, JsonArray) -> Unit)?,
        checker: (JsonObject, T) -> Boolean,
    ) {
        val dataList = readAll()
        var existIndex = -1
        for (entry in value) {
            for (i in 0 until dataList.size()) {
                val item: JsonObject = dataList.getJsonObject(i)
                if (checker(item, entry)) {
                    existIndex = i
                    break
                }
            }

            onCheckEnd?.invoke(entry, existIndex >= 0, dataList)
            if (existIndex >= 0) {
                dataList.set(existIndex, JsonObject.mapFrom(entry))
            } else {
                dataList.add(JsonObject.mapFrom(entry))
            }
        }
        cachedValue = dataList
        save()
    }

    override fun delete(checker: (JsonObject) -> Boolean) {
        var dataList = readAll()
        val removeIndexList = ArrayList<Int>()
        for (i in 0 until dataList.size()) {
            val item: JsonObject = dataList.getJsonObject(i)
            if (checker(item)) {
                removeIndexList.add(i)
            }
        }

        if (removeIndexList.isNotEmpty()) {
            val newList = JsonArray()
            for (i in 0 until dataList.size()) {
                if (!removeIndexList.contains(i)) {
                    newList.add(dataList.getJsonObject(i))
                }
            }
            dataList = newList
        }
        cachedValue = dataList
        save()
    }

    override fun save() {
        saveStorage("data", userNameSpace, name, value = cachedValue)
    }
}
