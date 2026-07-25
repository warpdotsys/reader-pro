package io.legado.app.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class IntJsonDeserializer : JsonDeserializer<Int?> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Int? {
        if (json.isJsonPrimitive) {
            val primitive = json.asJsonPrimitive
            if (primitive.isNumber) {
                return primitive.asNumber.toInt()
            }
            return null
        }
        return null
    }
}
