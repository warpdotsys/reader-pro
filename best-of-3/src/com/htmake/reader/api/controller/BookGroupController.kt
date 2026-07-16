package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.BookGroup
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.LinkedHashMap
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class BookGroupController(coroutineContext: CoroutineContext) : BaseController(coroutineContext), CURD<BookGroup> {
   public override fun getTableName(): String {
      return "bookGroup";
   }

   public override fun onList(var1: JsonArray, userNameSpace: String): JsonArray {
      if (var1.size() == 0) {
         val var2: JsonArray = ExtKt.asJsonArray(
            "\n            [{\"groupId\":-1,\"groupName\":\"全部\",\"order\":-10,\"show\":true},{\"groupId\":-2,\"groupName\":\"本地\",\"order\":-9,\"show\":true},{\"groupId\":-3,\"groupName\":\"音频\",\"order\":-8,\"show\":true},{\"groupId\":-4,\"groupName\":\"未分组\",\"order\":-7,\"show\":true},{\"groupId\":-5,\"groupName\":\"更新错误\",\"order\":-6,\"show\":true}]\n            "
         );
         if (var2 != null) {
            this.saveUserStorage(userNameSpace, "bookGroup", var2);
            return var2;
         }
      }

      return var1;
   }

   public open fun checker(var1: JsonObject, var2: BookGroup): Boolean {
      return java.lang.Long.valueOf(var2.getGroupId()).equals(var1.getLong("groupId"));
   }

   public open fun onCheckEnd(var1: BookGroup, var2: Boolean, bookGroupList: JsonArray) {
      if (!var2) {
         var maxOrder: Int = 0;
         val id: java.lang.Iterable = bookGroupList;
         var var9: Long = 0L;

         for (Object var12 : id) {
            val order: JsonObject = ExtKt.asJsonObject(var12);
            val var10000: Long;
            if (order == null) {
               var10000 = 0L;
            } else {
               val var16: java.lang.Long = order.getLong("groupId", 0L);
               var10000 = if (var16 == null) 0L else var16;
            }

            val var26: JsonObject = ExtKt.asJsonObject(var12);
            val var27: Int;
            if (var26 == null) {
               var27 = 0;
            } else {
               val var19: Int = var26.getInteger("order", 0);
               var27 = if (var19 == null) 0 else var19;
            }

            maxOrder = if (var27 > maxOrder) var27 else maxOrder;
            var9 += if (var10000 > 0L) var10000 else 0L;
         }

         val idsSum: Long = var9;
         var var24: Long = 1L;

         while ((id & idsSum) != 0L) {
            var24 <<= 1;
         }

         var1.setGroupId(var24);
         var1.setOrder(maxOrder + 1);
      }
   }

   public open fun beforeSave(var1: BookGroup, db: DB<BookGroup>): ReturnData? {
      return if (var1.getGroupName().length() == 0) new ReturnData().setErrorMsg("分组名称不能为空") else null;
   }

   public override suspend fun checkUserAuth(context: RoutingContext): Boolean {
      return this.checkAuth(context, `$completion`);
   }

   public override fun getUserNS(context: RoutingContext): String {
      return this.getUserNameSpace(context);
   }

   public override fun getEntityClass(): Class<BookGroup> {
      return BookGroup::class.java;
   }

   public suspend fun saveBookGroupOrder(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label60: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label60;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.saveBookGroupOrder(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var17: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var17) {
               return var17;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookGroupController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val bookGroupOrder: JsonArray = context.getBodyAsJson().getJsonArray("order", null);
         if (bookGroupOrder == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val userNameSpace: java.lang.String = this.getUserNameSpace(context);
            var bookGroupList: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookGroup"}));
            if (bookGroupList == null) {
               bookGroupList = new JsonArray();
            }

            val var19: java.util.Map = new LinkedHashMap();
            var var20: Int = 0;
            var var9: Int = bookGroupOrder.size();
            if (0 < var9) {
               do {
                  val i: Int = var20++;
                  val ix: java.lang.Long = bookGroupOrder.getJsonObject(i).getLong("groupId");
                  val var24: Int = bookGroupOrder.getJsonObject(i).getInteger("order");
                  var19.put(ix, var24);
               } while (var20 < var9);
            }

            val var21: java.util.List = bookGroupList.getList();
            var9 = 0;
            val var23: Int = bookGroupList.size();
            if (0 < var23) {
               do {
                  val var25: Int = var9++;
                  val bookGroup: BookGroup = bookGroupList.getJsonObject(var25).mapTo(BookGroup.class);
                  if (var19.containsKey(Boxing.boxLong(bookGroup.getGroupId()))) {
                     val var14: Any = var19.get(Boxing.boxLong(bookGroup.getGroupId()));
                     bookGroup.setOrder(if ((var14 as? Int) == null) bookGroup.getOrder() else var14 as? Int);
                     var21.set(var25, JsonObject.mapFrom(bookGroup));
                  }
               } while (var9 < var23);
            }

            this.saveUserStorage(userNameSpace, "bookGroup", new JsonArray(var21));
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   fun beforeAdd(val1: BookGroup, db: DB<BookGroup>): ReturnData? {
      return CURD.DefaultImpls.beforeAdd(this, val1, db);
   }

   fun beforeDelete(val1: BookGroup, db: DB<BookGroup>): ReturnData? {
      return CURD.DefaultImpls.beforeDelete(this, val1, db);
   }

   fun convertToEntity(var1: JsonObject): BookGroup {
      return CURD.DefaultImpls.convertToEntity(this, var1);
   }

   fun convertToEntityList(var1: java.lang.String): Array<BookGroup> {
      return CURD.DefaultImpls.convertToEntityList(this, var1);
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
