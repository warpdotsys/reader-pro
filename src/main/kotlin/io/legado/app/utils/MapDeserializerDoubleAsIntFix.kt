package io.legado.app.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.internal.LinkedTreeMap
import java.lang.reflect.Type

class MapDeserializerDoubleAsIntFix : JsonDeserializer<Map<String, Any?>?> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): Map<String, Any?>? {
        @Suppress("UNCHECKED_CAST")
        return read(jsonElement) as? Map<String, Any?>
    }

    fun read(json: JsonElement): Any? {
        if (json.isJsonArray) {
            val list: MutableList<Any?> = ArrayList()
            for (anArr in json.asJsonArray) {
                list.add(read(anArr))
            }
            return list
        }
        if (json.isJsonObject) {
            val map: MutableMap<String, Any?> = LinkedTreeMap()
            val obj: JsonObject = json.asJsonObject
            val entitySet: Set<Map.Entry<String, JsonElement>> = obj.entrySet()
            for ((key, value) in entitySet) {
                map[key] = read(value)
            }
            return map
        }
        if (json.isJsonPrimitive) {
            val prim: JsonPrimitive = json.asJsonPrimitive
            when {
                prim.isBoolean -> return prim.asBoolean
                prim.isString -> return prim.asString
                prim.isNumber -> {
                    val num: Number = prim.asNumber
                    return if (Math.ceil(num.toDouble()) == num.toLong().toDouble()) {
                        num.toLong()
                    } else {
                        num.toDouble()
                    }
                }
            }
        }
        return null
    }
}
