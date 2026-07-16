package com.htmake.reader.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

public class LongTypeAdapter : JsonSerializer<java.lang.Long>, JsonDeserializer<java.lang.Long> {
   public open fun serialize(src: Long?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
      return new JsonPrimitive(java.lang.String.valueOf(src));
   }

   public open fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Long? {
      val var10000: java.lang.Long;
      if (json.isJsonPrimitive()) {
         val prim: JsonPrimitive = json.getAsJsonPrimitive();
         var10000 = if (prim.isNumber()) prim.getAsNumber().longValue() else null as java.lang.Long;
      } else {
         var10000 = null;
      }

      return var10000;
   }
}
