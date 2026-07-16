package com.htmake.reader.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

public class IntTypeAdapter : JsonSerializer<Integer>, JsonDeserializer<Integer> {
   public open fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
      return new JsonPrimitive(java.lang.String.valueOf(src));
   }

   public open fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Int? {
      val var10000: Int;
      if (json.isJsonPrimitive()) {
         val prim: JsonPrimitive = json.getAsJsonPrimitive();
         var10000 = if (prim.isNumber()) prim.getAsNumber().intValue() else null as Int;
      } else {
         var10000 = null;
      }

      return var10000;
   }
}
