package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.db.DB
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.Ref.ObjectRef
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public interface CURD<T> {
   public abstract fun getTableName(): String {
   }

   public open fun convertToEntity(var1: JsonObject): Any {
   }

   public open fun convertToEntityList(var1: String): Array<Any> {
   }

   public open fun onList(var1: JsonArray, userNameSpace: String): JsonArray {
   }

   public abstract fun checker(var1: JsonObject, var2: Any): Boolean {
   }

   public open fun onCheckEnd(var1: Any, var2: Boolean, var3: JsonArray) {
   }

   public open fun beforeSave(val1: Any, db: DB<Any>): ReturnData? {
   }

   public open fun beforeAdd(val1: Any, db: DB<Any>): ReturnData? {
   }

   public open fun beforeDelete(val1: Any, db: DB<Any>): ReturnData? {
   }

   public abstract suspend fun checkUserAuth(context: RoutingContext): Boolean {
   }

   public abstract fun getUserNS(context: RoutingContext): String {
   }

   public abstract fun getEntityClass(): Class<Any> {
   }

   public open suspend fun list(context: RoutingContext): ReturnData {
   }

   public open suspend fun save(context: RoutingContext): ReturnData {
   }

   public open suspend fun saveMulti(context: RoutingContext): ReturnData {
   }

   public open suspend fun delete(context: RoutingContext): ReturnData {
   }

   public open suspend fun deleteMulti(context: RoutingContext): ReturnData {
   }

   internal class DefaultImpls {
      @JvmStatic
      fun <T> convertToEntity(`this`: CURD<T>, var1: JsonObject): T {
         return var1.mapTo(this.getEntityClass());
      }

      @JvmStatic
      fun <T> convertToEntityList(`this`: CURD<T>, var1: java.lang.String): Array<T> {
         val var4: Any = ExtKt.getGson().fromJson(var1, java.lang.reflect.Array.newInstance(this.getEntityClass(), 0).getClass());
         return (T[])(var4 as Array<Any>);
      }

      @JvmStatic
      fun <T> onList(`this`: CURD<T>, var1: JsonArray, userNameSpace: java.lang.String): JsonArray {
         return var1;
      }

      @JvmStatic
      fun <T> onCheckEnd(`this`: CURD<T>, var1: T, var2: Boolean, var3: JsonArray) {
      }

      @JvmStatic
      fun <T> beforeSave(`this`: CURD<T>, val1: T, db: DB<T>): ReturnData? {
         return null;
      }

      @JvmStatic
      fun <T> beforeAdd(`this`: CURD<T>, val1: T, db: DB<T>): ReturnData? {
         return null;
      }

      @JvmStatic
      fun <T> beforeDelete(`this`: CURD<T>, val1: T, db: DB<T>): ReturnData? {
         return null;
      }

      @JvmStatic
      fun <T> list(`this`: CURD<T>, context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
         var `$continuation`: Continuation;
         label24: {
            if (`$completion` is SyntheticContinuation) {
               `$continuation` = `$completion` as SyntheticContinuation;
               if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
                  `$continuation`.label -= Integer.MIN_VALUE;
                  break label24;
               }
            }

            `$continuation` = new ContinuationImpl(`$completion`) {
               Object L$0;
               Object L$1;
               Object L$2;
               int label;

               {
                  super(`$completion`);
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  this.result = `$result`;
                  this.label |= Integer.MIN_VALUE;
                  return CURD.DefaultImpls.list(null, null, this);
               }
            };
         }

         val `$result`: Any = `$continuation`.result;
         val var9: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
               var10000 = this.checkUserAuth(context, `$continuation`);
               if (var10000 === var9) {
                  return var9;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as CURD;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         } else {
            val userNameSpace: java.lang.String = this.getUserNS(context);
            val var6: java.util.List = this.onList(
                  DB.Companion.table$default(DB.Companion, userNameSpace, this.getTableName(), null, 4, null).readAll(), userNameSpace
               )
               .getList();
            return ReturnData.setData$default(returnData, var6, null, 2, null);
         }
      }

      @JvmStatic
      fun <T> save(`this`: CURD<T>, context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
         var `$continuation`: Continuation;
         label28: {
            if (`$completion` is SyntheticContinuation) {
               `$continuation` = `$completion` as SyntheticContinuation;
               if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
                  `$continuation`.label -= Integer.MIN_VALUE;
                  break label28;
               }
            }

            `$continuation` = new ContinuationImpl(`$completion`) {
               Object L$0;
               Object L$1;
               Object L$2;
               int label;

               {
                  super(`$completion`);
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  this.result = `$result`;
                  this.label |= Integer.MIN_VALUE;
                  return CURD.DefaultImpls.save(null, null, this);
               }
            };
         }

         val `$result`: Any = `$continuation`.result;
         val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
               var10000 = this.checkUserAuth(context, `$continuation`);
               if (var10000 === var10) {
                  return var10;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as CURD;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         } else {
            val userNameSpace: JsonObject = context.getBodyAsJson();
            val entity: Any = this.convertToEntity(userNameSpace);
            val db: DB = DB.Companion.table$default(DB.Companion, this.getUserNS(context), this.getTableName(), null, 4, null);
            val result: ReturnData = this.beforeSave(entity, db);
            if (result != null) {
               return result;
            } else {
               db.save(entity, (new Function3<T, java.lang.Boolean, JsonArray, Unit>(this) {
                  {
                     super(3, `<this>`, CURD::class.java, "onCheckEnd", "onCheckEnd(Ljava/lang/Object;ZLio/vertx/core/json/JsonArray;)V", 0);
                  }

                  public final void invoke(T p0, boolean p1, @NotNull JsonArray p2) {
                     (this.receiver as CURD).onCheckEnd(p0, p1, p2);
                  }
               }) as (Any?, java.lang.Boolean?, JsonArray?) -> Unit, (new Function2<JsonObject, T, java.lang.Boolean>(this) {
                  {
                     super(2);
                     this.this$0 = `$receiver`;
                  }

                  public final boolean invoke(@NotNull JsonObject jsonObject, T value) {
                     return this.this$0.checker(jsonObject, (T)value);
                  }
               }) as (JsonObject?, Any?) -> java.lang.Boolean);
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }

      @JvmStatic
      fun <T> saveMulti(`this`: CURD<T>, context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
         var `$continuation`: Continuation;
         label42: {
            if (`$completion` is SyntheticContinuation) {
               `$continuation` = `$completion` as SyntheticContinuation;
               if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
                  `$continuation`.label -= Integer.MIN_VALUE;
                  break label42;
               }
            }

            `$continuation` = new ContinuationImpl(`$completion`) {
               Object L$0;
               Object L$1;
               Object L$2;
               int label;

               {
                  super(`$completion`);
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  this.result = `$result`;
                  this.label |= Integer.MIN_VALUE;
                  return CURD.DefaultImpls.saveMulti(null, null, this);
               }
            };
         }

         val `$result`: Any = `$continuation`.result;
         val var14: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
               var10000 = this.checkUserAuth(context, `$continuation`);
               if (var10000 === var14) {
                  return var14;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as CURD;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         } else {
            val userNameSpace: java.lang.String = context.getBodyAsString();
            val itemList: Array<Any> = this.convertToEntityList(userNameSpace);
            if (itemList.length == 0) {
               return returnData.setErrorMsg("参数错误");
            } else {
               val var16: DB = DB.Companion.table$default(DB.Companion, this.getUserNS(context), this.getTableName(), null, 4, null);
               val var7: Array<Any> = itemList;
               var var8: Int = 0;
               val var9: Int = itemList.length;

               while (var8 < var9) {
                  val item: Any = var7[var8];
                  var8++;
                  val result: ReturnData = this.beforeSave(item, var16);
                  if (result != null) {
                     return result;
                  }
               }

               var16.saveMulti(itemList, (new Function3<T, java.lang.Boolean, JsonArray, Unit>(this) {
                  {
                     super(3, `<this>`, CURD::class.java, "onCheckEnd", "onCheckEnd(Ljava/lang/Object;ZLio/vertx/core/json/JsonArray;)V", 0);
                  }

                  public final void invoke(T p0, boolean p1, @NotNull JsonArray p2) {
                     (this.receiver as CURD).onCheckEnd(p0, p1, p2);
                  }
               }) as (Any?, java.lang.Boolean?, JsonArray?) -> Unit, (new Function2<JsonObject, T, java.lang.Boolean>(this) {
                  {
                     super(2);
                     this.this$0 = `$receiver`;
                  }

                  public final boolean invoke(@NotNull JsonObject jsonObject, T value) {
                     return this.this$0.checker(jsonObject, (T)value);
                  }
               }) as (JsonObject?, Any?) -> java.lang.Boolean);
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }

      @JvmStatic
      fun <T> delete(`this`: CURD<T>, context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
         var `$continuation`: Continuation;
         label28: {
            if (`$completion` is SyntheticContinuation) {
               `$continuation` = `$completion` as SyntheticContinuation;
               if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
                  `$continuation`.label -= Integer.MIN_VALUE;
                  break label28;
               }
            }

            `$continuation` = new ContinuationImpl(`$completion`) {
               Object L$0;
               Object L$1;
               Object L$2;
               int label;

               {
                  super(`$completion`);
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  this.result = `$result`;
                  this.label |= Integer.MIN_VALUE;
                  return CURD.DefaultImpls.delete(null, null, this);
               }
            };
         }

         val `$result`: Any = `$continuation`.result;
         val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
               var10000 = this.checkUserAuth(context, `$continuation`);
               if (var10000 === var10) {
                  return var10;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as CURD;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         } else {
            val userNameSpace: JsonObject = context.getBodyAsJson();
            val entity: Any = this.convertToEntity(userNameSpace);
            val db: DB = DB.Companion.table$default(DB.Companion, this.getUserNS(context), this.getTableName(), null, 4, null);
            val result: ReturnData = this.beforeDelete(entity, db);
            if (result != null) {
               return result;
            } else {
               db.delete((new Function1<JsonObject, java.lang.Boolean>(this, entity) {
                  {
                     super(1);
                     this.this$0 = `$receiver`;
                     this.$entity = (T)`$entity`;
                  }

                  public final boolean invoke(@NotNull JsonObject it) {
                     return this.this$0.checker(it, this.$entity);
                  }
               }) as (JsonObject?) -> java.lang.Boolean);
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }

      @JvmStatic
      fun <T> deleteMulti(`this`: CURD<T>, context: RoutingContext, `$completion`: Continuation<? super ReturnData>): Any? {
         var `$continuation`: Continuation;
         label42: {
            if (`$completion` is SyntheticContinuation) {
               `$continuation` = `$completion` as SyntheticContinuation;
               if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
                  `$continuation`.label -= Integer.MIN_VALUE;
                  break label42;
               }
            }

            `$continuation` = new ContinuationImpl(`$completion`) {
               Object L$0;
               Object L$1;
               Object L$2;
               int label;

               {
                  super(`$completion`);
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  this.result = `$result`;
                  this.label |= Integer.MIN_VALUE;
                  return CURD.DefaultImpls.deleteMulti(null, null, this);
               }
            };
         }

         val `$result`: Any = `$continuation`.result;
         val var14: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
               var10000 = this.checkUserAuth(context, `$continuation`);
               if (var10000 === var14) {
                  return var14;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as CURD;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         } else {
            val itemList: ObjectRef = new ObjectRef();
            val userNameSpace: java.lang.String = context.getBodyAsString();
            itemList.element = (T)this.convertToEntityList(userNameSpace);
            if (itemList.element.length == 0) {
               return returnData.setErrorMsg("参数错误");
            } else {
               val var17: DB = DB.Companion.table$default(DB.Companion, this.getUserNS(context), this.getTableName(), null, 4, null);
               val var7: Array<Any> = itemList.element;
               var var8: Int = 0;
               val var9: Int = var7.length;

               while (var8 < var9) {
                  val item: Any = var7[var8];
                  var8++;
                  val result: ReturnData = this.beforeDelete(item, var17);
                  if (result != null) {
                     return result;
                  }
               }

               var17.delete((new Function1<JsonObject, java.lang.Boolean>(itemList, this) {
                  {
                     super(1);
                     this.$itemList = `$itemList`;
                     this.this$0 = `$receiver`;
                  }

                  public final boolean invoke(@NotNull JsonObject it) {
                     val var2: Int = 0;
                     val var3: Int = (this.$itemList.element as Array<Any>).length;
                     if (0 < (this.$itemList.element as Array<Any>).length) {
                        do {
                           if (this.this$0.checker(it, this.$itemList.element[var2++])) {
                              return true;
                           }
                        } while (var2 < var3);
                     }

                     return false;
                  }
               }) as (JsonObject?) -> java.lang.Boolean);
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }
   }
}
