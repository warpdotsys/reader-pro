package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import io.legado.app.data.entities.ReplaceRule
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

public class ReplaceRuleController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<ReplaceRule> {
   public override fun getTableName(): String {
      return "replaceRule";
   }

   public open fun checker(var1: JsonObject, var2: ReplaceRule): Boolean {
      return var2.getName().equals(var1.getString("name"));
   }

   public open fun beforeSave(var1: ReplaceRule, db: DB<ReplaceRule>): ReturnData? {
      val returnData: ReturnData = new ReturnData();
      if (var1.getName().length() == 0) {
         return returnData.setErrorMsg("名称不能为空");
      } else {
         return if (var1.getPattern().length() == 0) returnData.setErrorMsg("规则不能为空") else null;
      }
   }

   public override suspend fun checkUserAuth(context: RoutingContext): Boolean {
      return this.checkAuth(context, `$completion`);
   }

   public override fun getUserNS(context: RoutingContext): String {
      return this.getUserNameSpace(context);
   }

   public override fun getEntityClass(): Class<ReplaceRule> {
      return ReplaceRule::class.java;
   }

   fun beforeAdd(val1: ReplaceRule, db: DB<ReplaceRule>): ReturnData? {
      return CURD.DefaultImpls.beforeAdd(this, val1, db);
   }

   fun beforeDelete(val1: ReplaceRule, db: DB<ReplaceRule>): ReturnData? {
      return CURD.DefaultImpls.beforeDelete(this, val1, db);
   }

   fun convertToEntity(var1: JsonObject): ReplaceRule {
      return CURD.DefaultImpls.convertToEntity(this, var1);
   }

   fun convertToEntityList(var1: java.lang.String): Array<ReplaceRule> {
      return CURD.DefaultImpls.convertToEntityList(this, var1);
   }

   fun onCheckEnd(var1: ReplaceRule, var2: Boolean, var3: JsonArray) {
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
