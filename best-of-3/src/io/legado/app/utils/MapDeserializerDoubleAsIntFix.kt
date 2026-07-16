package io.legado.app.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.internal.LinkedTreeMap
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.Map.Entry

public class MapDeserializerDoubleAsIntFix : JsonDeserializer<java.util.Map<java.lang.String, ? extends Object>> {
   @Throws(com/google/gson/JsonParseException::class)
   public open fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): Map<String, Any?>? {
      val var4: Any = this.read(jsonElement);
      return var4 as? java.util.Map;
   }

   public fun read(json: JsonElement): Any? {
      if (json.isJsonArray()) {
         val var14: java.util.List = new ArrayList();

         for (JsonElement anArr : json.getAsJsonArray()) {
            var14.add(this.read(var20));
         }

         return var14;
      } else if (!json.isJsonObject()) {
         if (json.isJsonPrimitive()) {
            val var13: JsonPrimitive = json.getAsJsonPrimitive();
            if (var13.isBoolean()) {
               return var13.getAsBoolean();
            }

            if (var13.isString()) {
               return var13.getAsString();
            }

            if (var13.isNumber()) {
               val var17: java.lang.Number = var13.getAsNumber();
               return if (Math.ceil(var17.doubleValue()) == var17.longValue()) var17.longValue() else var17.doubleValue();
            }
         }

         return null;
      } else {
         val prim: java.util.Map = new LinkedTreeMap();

         for (Entry var6 : json.getAsJsonObject().entrySet()) {
            val key: java.lang.String = var6.getKey() as java.lang.String;
            val value: JsonElement = var6.getValue() as JsonElement;
            prim.put(key, this.read(value));
         }

         return prim;
      }
   }
}
