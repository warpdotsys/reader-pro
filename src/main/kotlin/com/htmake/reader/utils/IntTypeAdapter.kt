package com.htmake.reader.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class IntTypeAdapter : JsonSerializer<Int>, JsonDeserializer<Int?> {
    override fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Int? =
        json.takeIf { it.isJsonPrimitive }
            ?.asJsonPrimitive
            ?.takeIf { it.isNumber }
            ?.asNumber
            ?.toInt()
}
