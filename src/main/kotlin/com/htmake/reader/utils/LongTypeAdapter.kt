package com.htmake.reader.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class LongTypeAdapter : JsonSerializer<Long>, JsonDeserializer<Long?> {
    override fun serialize(src: Long?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Long? =
        json.takeIf { it.isJsonPrimitive }
            ?.asJsonPrimitive
            ?.takeIf { it.isNumber }
            ?.asNumber
            ?.toLong()
}
