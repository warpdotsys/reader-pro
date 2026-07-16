package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import io.legado.app.data.entities.Bookmark
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

public class BookmarkController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<Bookmark> {
   public override fun getTableName(): String {
      return "bookmark";
   }

   public open fun checker(var1: JsonObject, var2: Bookmark): Boolean {
      return java.lang.Long.valueOf(var2.getTime()).equals(var1.getLong("time"));
   }

   public open fun beforeSave(var1: Bookmark, db: DB<Bookmark>): ReturnData? {
      return if (var1.getBookName().length() == 0 && var1.getBookAuthor().length() == 0) new ReturnData().setErrorMsg("书签信息错误") else null;
   }

   public override suspend fun checkUserAuth(context: RoutingContext): Boolean {
      return this.checkAuth(context, `$completion`);
   }

   public override fun getUserNS(context: RoutingContext): String {
      return this.getUserNameSpace(context);
   }

   public override fun getEntityClass(): Class<Bookmark> {
      return Bookmark::class.java;
   }

   fun beforeAdd(val1: Bookmark, db: DB<Bookmark>): ReturnData? {
      return CURD.DefaultImpls.beforeAdd(this, val1, db);
   }

   fun beforeDelete(val1: Bookmark, db: DB<Bookmark>): ReturnData? {
      return CURD.DefaultImpls.beforeDelete(this, val1, db);
   }

   fun convertToEntity(var1: JsonObject): Bookmark {
      return CURD.DefaultImpls.convertToEntity(this, var1);
   }

   fun convertToEntityList(var1: java.lang.String): Array<Bookmark> {
      return CURD.DefaultImpls.convertToEntityList(this, var1);
   }

   fun onCheckEnd(var1: Bookmark, var2: Boolean, var3: JsonArray) {
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
