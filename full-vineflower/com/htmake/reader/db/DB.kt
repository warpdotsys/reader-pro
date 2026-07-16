package com.htmake.reader.db

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

public open class DB<T>(userNameSpace: String, name: String) {
   public final var cachedValue: JsonArray
      internal set

   public final val name: String
   public final val userNameSpace: String

   init {
      this.userNameSpace = userNameSpace;
      this.name = name;
      this.cachedValue = new JsonArray();
   }

   public open fun readAll(): JsonArray {
      return new JsonArray();
   }

   public open fun <P : Any> findBy(field: String, value: P, clazz: Class<Any>): Any? {
      return null;
   }

   public open fun save(value: Any, onCheckEnd: ((Any, Boolean, JsonArray) -> Unit)?, checker: (JsonObject, Any) -> Boolean) {
   }

   public open fun saveMulti(value: Array<Any>, onCheckEnd: ((Any, Boolean, JsonArray) -> Unit)?, checker: (JsonObject, Any) -> Boolean) {
   }

   public open fun delete(checker: (JsonObject) -> Boolean) {
   }

   public open fun save() {
   }

   public companion object {
      public fun <T> table(userNameSpace: String, name: String, driver: String = "JSON"): DB<T> {
         return (DB<T>)(if (driver == "SQL") new SQLTable<>(userNameSpace, name) else new JSONTable<>(userNameSpace, name));
      }
   }
}
