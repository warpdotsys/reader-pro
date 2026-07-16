package com.htmake.reader.db

import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.util.ArrayList

public class JSONTable<T>(userNameSpace: String, name: String) : DB(userNameSpace, name) {
   public override fun readAll(): JsonArray {
      var dataList: JsonArray = ExtKt.asJsonArray(
         ExtKt.getStorage$default(new java.lang.String[]{"data", this.getUserNameSpace(), this.getName()}, null, 2, null)
      );
      if (dataList == null) {
         dataList = new JsonArray();
      }

      this.setCachedValue(dataList);
      return dataList;
   }

   public override fun <P : Any> findBy(field: String, value: P, clazz: Class<Any>): Any? {
      val dataList: JsonArray = this.readAll();
      var var5: Int = 0;
      val var6: Int = dataList.size();
      if (0 < var6) {
         do {
            val i: Int = var5++;
            if (value.equals(dataList.getJsonObject(i).getValue(field))) {
               return dataList.getJsonObject(i).mapTo(clazz);
            }
         } while (var5 < var6);
      }

      return null;
   }

   public override fun save(value: Any, onCheckEnd: ((Any, Boolean, JsonArray) -> Unit)?, checker: (JsonObject, Any) -> Boolean) {
      var dataList: JsonArray = this.readAll();
      var existIndex: Int = -1;
      var list: Int = 0;
      val var7: Int = dataList.size();
      if (0 < var7) {
         do {
            val i: Int = list++;
            val var9: JsonObject = dataList.getJsonObject(i);
            if (checker.invoke(var9, value) as java.lang.Boolean) {
               existIndex = i;
               break;
            }
         } while (list < var7);
      }

      if (onCheckEnd != null) {
         onCheckEnd.invoke(value, existIndex >= 0, dataList);
      }

      if (existIndex >= 0) {
         val var10: java.util.List = dataList.getList();
         var10.set(existIndex, JsonObject.mapFrom(value));
         dataList = new JsonArray(var10);
      } else {
         dataList.add(JsonObject.mapFrom(value));
      }

      this.setCachedValue(dataList);
      this.save();
   }

   public override fun saveMulti(value: Array<Any>, onCheckEnd: ((Any, Boolean, JsonArray) -> Unit)?, checker: (JsonObject, Any) -> Boolean) {
      val dataList: JsonArray = this.readAll();
      var existIndex: Int = -1;
      val var6: Array<Any> = value;
      var var7: Int = 0;
      val var8: Int = value.length;

      while (var7 < var8) {
         val j: Any = var6[var7];
         var7++;
         var var10: Int = 0;
         val var11: Int = dataList.size();
         if (0 < var11) {
            do {
               val i: Int = var10++;
               val var13: JsonObject = dataList.getJsonObject(i);
               if (checker.invoke(var13, j) as java.lang.Boolean) {
                  existIndex = i;
                  break;
               }
            } while (var10 < var11);
         }

         if (onCheckEnd != null) {
            onCheckEnd.invoke(j, existIndex >= 0, dataList);
         }

         if (existIndex >= 0) {
            dataList.set(existIndex, JsonObject.mapFrom(j));
         } else {
            dataList.add(JsonObject.mapFrom(j));
         }
      }

      this.setCachedValue(dataList);
      this.save();
   }

   public override fun delete(checker: (JsonObject) -> Boolean) {
      var dataList: JsonArray = this.readAll();
      val removeIndexList: java.util.List = new ArrayList();
      var var8: Int = 0;
      var var5: Int = dataList.size();
      if (0 < var5) {
         do {
            val i: Int = var8++;
            val ix: JsonObject = dataList.getJsonObject(i);
            if (checker.invoke(ix) as java.lang.Boolean) {
               removeIndexList.add(i);
            }
         } while (var8 < var5);
      }

      if (removeIndexList.size() > 0) {
         val var9: JsonArray = new JsonArray();
         var5 = 0;
         val var11: Int = dataList.size();
         if (0 < var11) {
            do {
               val var12: Int = var5++;
               if (!removeIndexList.contains(var12)) {
                  var9.add(dataList.getJsonObject(var12));
               }
            } while (var5 < var11);
         }

         dataList = var9;
      }

      this.setCachedValue(dataList);
      this.save();
   }

   public override fun save() {
      ExtKt.saveStorage$default(new java.lang.String[]{"data", this.getUserNameSpace(), this.getName()}, this.getCachedValue(), false, null, 12, null);
   }
}
