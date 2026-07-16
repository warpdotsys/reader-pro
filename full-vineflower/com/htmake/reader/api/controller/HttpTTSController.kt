package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.HttpTTS
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.ArrayList
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

public class HttpTTSController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<HttpTTS> {
   public override fun getTableName(): String {
      return "httpTTS";
   }

   public open fun checker(var1: JsonObject, var2: HttpTTS): Boolean {
      return var2.getName().equals(var1.getString("name"));
   }

   public open fun beforeSave(var1: HttpTTS, db: DB<HttpTTS>): ReturnData? {
      val returnData: ReturnData = new ReturnData();
      if (var1.getName().length() == 0) {
         return returnData.setErrorMsg("名称不能为空");
      } else {
         return if (var1.getUrl().length() == 0) returnData.setErrorMsg("链接不能为空") else null;
      }
   }

   public override suspend fun checkUserAuth(context: RoutingContext): Boolean {
      return this.checkAuth(context, `$completion`);
   }

   public override fun getUserNS(context: RoutingContext): String {
      return this.getUserNameSpace(context);
   }

   public override fun getEntityClass(): Class<HttpTTS> {
      return HttpTTS::class.java;
   }

   public open fun convertToEntity(var1: JsonObject): HttpTTS {
      val var10000: HttpTTS.Companion = HttpTTS.Companion;
      var var2: java.lang.String = var1.toString();
      var2 = (java.lang.String)var10000.fromJson-IoAF18A(var2);
      val var5: Any = if (Result.isFailure-impl(var2)) null else var2;
      return var5 as HttpTTS;
   }

   public open fun convertToEntityList(var1: String): Array<HttpTTS> {
      val jsonArray: JsonArray = ExtKt.asJsonArray(var1);
      val list: java.util.List = new ArrayList();
      if (jsonArray != null) {
         val `$i$f$toTypedArray`: java.lang.Iterable;
         for (Object element$iv : $i$f$toTypedArray) {
            val var11: Any = HttpTTS.Companion.fromJson-IoAF18A(`element$iv`.toString());
            val var10001: Any = if (Result.isFailure-impl(var11)) null else var11;
            list.add(var10001);
         }
      }

      val var10000: Array<Any> = list.toArray(new HttpTTS[0]);
      if (var10000 == null) {
         throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
      } else {
         return var10000 as Array<HttpTTS>;
      }
   }

   fun beforeAdd(val1: HttpTTS, db: DB<HttpTTS>): ReturnData? {
      return CURD.DefaultImpls.beforeAdd(this, val1, db);
   }

   fun beforeDelete(val1: HttpTTS, db: DB<HttpTTS>): ReturnData? {
      return CURD.DefaultImpls.beforeDelete(this, val1, db);
   }

   fun onCheckEnd(var1: HttpTTS, var2: Boolean, var3: JsonArray) {
      CURD.DefaultImpls.onCheckEnd(this, var1, var2, var3);
   }

   override fun onList(var1: JsonArray, userNameSpace: java.lang.String): JsonArray {
      return CURD.DefaultImpls.onList(this, var1, userNameSpace);
   }

   override fun delete(context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
      return CURD.DefaultImpls.delete(this, context, `$completion`);
   }

   override fun deleteMulti(context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
      return CURD.DefaultImpls.deleteMulti(this, context, `$completion`);
   }

   override fun list(context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
      return CURD.DefaultImpls.list(this, context, `$completion`);
   }

   override fun save(context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
      return CURD.DefaultImpls.save(this, context, `$completion`);
   }

   override fun saveMulti(context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
      return CURD.DefaultImpls.saveMulti(this, context, `$completion`);
   }
}
