package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.VertExtKt
import io.legado.app.data.entities.BookSource
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.FileUpload
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.VertxCoroutineKt
import java.io.File
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.LinkedHashSet
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.TypeIntrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.slf4j.MDCContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class BookSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
   private final var webClient: WebClient

   init {
      val var2: Any = SpringContextUtils.getBean("webClient", WebClient.class);
      this.webClient = var2 as WebClient;
   }

   public fun getUserBookSourceJsonOpt(userNameSpace: String, fields: Set<String>? = null, checkNotEmpty: Set<String>? = null): JsonArray? {
      var bookSourceFile: File = ExtKt.getStorageFile$default(new java.lang.String[]{"data", userNameSpace, "bookSource"}, null, 2, null);
      if (!bookSourceFile.exists()) {
         bookSourceFile = ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
      }

      return ExtKt.parseJsonStringList$default(bookSourceFile, fields, null, 0, 0, checkNotEmpty, null, 92, null);
   }

   public fun getUserBookSourceJson(userNameSpace: String): JsonArray? {
      var bookSourceList: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookSource"}));
      if (bookSourceList == null && !userNameSpace.equals("default")) {
         val var5: JsonArray = ExtKt.asJsonArray(this.getUserStorage("default", new java.lang.String[]{"bookSource"}));
         if (var5 != null) {
            bookSourceList = var5;
         }
      }

      return bookSourceList;
   }

   public suspend fun canEditBookSource(context: RoutingContext): Boolean {
      if (!this.getAppConfig().getSecure()) {
         return Boxing.boxBoolean(true);
      } else {
         val userInfo: User = context.get("userInfo");
         return if (userInfo == null) Boxing.boxBoolean(false) else Boxing.boxBoolean(userInfo.getEnable_book_source());
      }
   }

   public suspend fun saveBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label69: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label69;
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
               return this.this$0.saveBookSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label73: {
         val `$result`: Any = `$continuation`.result;
         val var14: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var14) {
                  return var14;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (BookSource.Companion)`$result`;
               break label73;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = returnData;
         `$continuation`.label = 2;
         var10000 = (BookSource.Companion)this.canEditBookSource(context, `$continuation`);
         if (var10000 === var14) {
            return var14;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         var10000 = BookSource.Companion;
         var userNameSpace: java.lang.String = context.getBodyAsString();
         userNameSpace = (java.lang.String)var10000.fromJson-IoAF18A(userNameSpace);
         val bookSource: BookSource = (if (Result.isFailure-impl(userNameSpace)) null else userNameSpace) as BookSource;
         if (bookSource == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            userNameSpace = this.getUserNameSpace(context);
            var var17: JsonArray = this.getUserBookSourceJson(userNameSpace);
            if (var17 == null) {
               var17 = new JsonArray();
            }

            val urlMap: java.util.Map = new LinkedHashMap();
            var var18: Int = 0;
            val userInfo: Int = var17.size();
            if (0 < userInfo) {
               do {
                  val i: Int = var18++;
                  val var11: java.lang.String = var17.getJsonObject(i).getString("bookSourceUrl");
                  urlMap.put(var11, Boxing.boxInt(i));
               } while (existIndex < userInfo);
            }

            var18 = urlMap.getOrDefault(bookSource.getBookSourceUrl(), Boxing.boxInt(-1)).intValue();
            if (var18 >= 0) {
               val var20: java.util.List = var17.getList();
               var20.set(var18, JsonObject.mapFrom(bookSource));
               var17 = new JsonArray(var20);
            } else {
               val var21: User = context.get("userInfo");
               if (var21 != null && var17.size() >= var21.getBook_source_limit()) {
                  return returnData.setErrorMsg("你已达到书源数上限，请联系管理员");
               }

               var17.add(JsonObject.mapFrom(bookSource));
            }

            this.saveUserStorage(userNameSpace, "bookSource", var17);
            this.generateBookSourceMap(userNameSpace, var17);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public suspend fun saveBookSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label42: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label42;
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
               return this.this$0.saveBookSources(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label46: {
         val `$result`: Any = `$continuation`.result;
         val var7: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var7) {
                  return var7;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break label46;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = returnData;
         `$continuation`.label = 2;
         var10000 = this.canEditBookSource(context, `$continuation`);
         if (var10000 === var7) {
            return var7;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         val bookSourceJsonArray: JsonArray = context.getBodyAsJsonArray();
         return if (bookSourceJsonArray == null) returnData.setErrorMsg("参数错误") else this.saveBookSources(context, bookSourceJsonArray);
      }
   }

   public fun saveBookSources(context: RoutingContext, bookSourceJsonArray: JsonArray): ReturnData {
      return this.saveUserBookSources(this.getUserNameSpace(context), context.get("userInfo"), bookSourceJsonArray);
   }

   public fun saveUserBookSources(userNameSpace: String, userInfo: User?, bookSourceJsonArray: JsonArray): ReturnData {
      val returnData: ReturnData = new ReturnData();
      var bookSourceList: JsonArray = this.getUserBookSourceJson(userNameSpace);
      if (bookSourceList == null) {
         bookSourceList = new JsonArray();
      }

      val urlMap: java.util.Map = new LinkedHashMap();
      var var18: Int = 0;
      var addCnt: Int = bookSourceList.size();
      if (0 < addCnt) {
         do {
            val maxIndex: Int = var18++;
            val updateIndex: java.lang.String = bookSourceList.getJsonObject(maxIndex).getString("bookSourceUrl");
            urlMap.put(updateIndex, maxIndex);
         } while (var18 < addCnt);
      }

      var var19: Boolean = false;
      addCnt = 0;
      val var21: Int = bookSourceList.size() - 1;
      val var22: java.util.Set = new LinkedHashSet();
      var var23: Int = 0;
      val var12: Int = bookSourceJsonArray.size();
      if (0 < var12) {
         do {
            val k: Int = var23++;

            var existIndex: BookSource;
            try {
               val var10000: BookSource.Companion = BookSource.Companion;
               val var25: java.lang.String = bookSourceJsonArray.getJsonObject(k).toString();
               existIndex = (BookSource)var10000.fromJson-IoAF18A(var25);
               existIndex = if (Result.isFailure-impl(existIndex)) null else existIndex;
            } catch (var17: Exception) {
               existIndex = null as BookSource;
            }

            if (existIndex != null) {
               val var27: Int = urlMap.getOrDefault(existIndex.getBookSourceUrl(), -1).intValue();
               if (var27 >= 0) {
                  bookSourceList.set(var27, JsonObject.mapFrom(existIndex));
                  if (var27 <= var21) {
                     var22.add(var27);
                  }
               } else {
                  if (userInfo != null && bookSourceList.size() >= userInfo.getBook_source_limit()) {
                     var19 = true;
                     break;
                  }

                  addCnt++;
                  bookSourceList.add(JsonObject.mapFrom(existIndex));
                  urlMap.put(existIndex.getBookSourceUrl(), bookSourceList.size() - 1);
               }
            }
         } while (var23 < var12);
      }

      this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
      this.generateBookSourceMap(userNameSpace, bookSourceList);
      val var24: java.lang.String = "新增$addCnt条书源，更新${var22.size()}条书源";
      return if (var19) returnData.setErrorMsg(Intrinsics.stringPlus(var24, "。你已达到书源数上限，请联系管理员")) else returnData.setData("", var24);
   }

   public suspend fun getBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label51: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label51;
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
               return this.this$0.getBookSource(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            if (this.checkAuth(context, `$continuation`) === var13) {
               return var13;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookSourceController;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var14: java.lang.String;
      if (context.request().method() === HttpMethod.POST) {
         val userNameSpace: java.lang.String = context.getBodyAsJson().getString("bookSourceUrl");
         var14 = userNameSpace;
      } else {
         val urlMap: java.util.List = context.queryParam("bookSourceUrl");
         val var15: java.lang.String = CollectionsKt.firstOrNull(urlMap);
         var14 = if (var15 == null) "" else var15;
      }

      if (var14.length() == 0) {
         return returnData.setErrorMsg("书源链接不能为空");
      } else {
         val var17: java.lang.String = this.getUserNameSpace(context);
         val var20: Int = this.getBookSourceMap(var17).getOrDefault(var14, Boxing.boxInt(-1)).intValue();
         if (var20 < 0) {
            return returnData.setErrorMsg("书源信息不存在");
         } else {
            var bookSourceFile: File = ExtKt.getStorageFile$default(new java.lang.String[]{"data", var17, "bookSource"}, null, 2, null);
            if (!bookSourceFile.exists()) {
               bookSourceFile = ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
            }

            val var22: JsonArray = ExtKt.parseJsonStringList$default(bookSourceFile, null, null, var20, var20, null, null, 102, null);
            if (var22 == null) {
               return returnData.setErrorMsg("书源信息不存在");
            } else {
               val var10: java.util.Map = new JsonObject(var22.getString(0)).getMap();
               return ReturnData.setData$default(returnData, var10, null, 2, null);
            }
         }
      }
   }

   public suspend fun getBookSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label58: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label58;
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
               return this.this$0.getBookSources(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            if (this.checkAuth(context, `$continuation`) === var21) {
               return var21;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookSourceController;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var22: Int;
      if (context.request().method() === HttpMethod.POST) {
         val userNameSpace: Int = context.getBodyAsJson().getInteger("simple", Boxing.boxInt(0));
         var22 = userNameSpace.intValue();
      } else {
         val bookSourceList: java.util.List = context.queryParam("simple");
         val var23: java.lang.String = CollectionsKt.firstOrNull(bookSourceList);
         val var10000: Int;
         if (var23 == null) {
            var10000 = 0;
         } else {
            val var25: Int = Boxing.boxInt(Integer.parseInt(var23));
            var10000 = if (var25 == null) 0 else var25;
         }

         var22 = var10000;
      }

      val var26: JsonArray = this.getUserBookSourceJsonOpt(
         this.getUserNameSpace(context),
         if (var22 > 0) SetsKt.setOf(new java.lang.String[]{"bookSourceGroup", "bookSourceName", "bookSourceUrl"}) else null,
         if (var22 > 0) SetsKt.setOf("exploreUrl") else null
      );
      if (var26 != null) {
         val var28: java.util.List = var26.getList();
         val var29: java.lang.Iterable = var28;
         val `destination$iv$iv`: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(var28, 10));

         for (Object item$iv$iv : var29) {
            val var31: JsonObject = new JsonObject;
            if (`item$iv$iv` == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }

            var31./* $VF: Unable to resugar constructor */<init>(`item$iv$iv` as java.lang.String);
            `destination$iv$iv`.add(var31.getMap());
         }

         return ReturnData.setData$default(returnData, `destination$iv$iv` as java.util.List, null, 2, null);
      } else {
         return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
      }
   }

   public suspend fun deleteBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label55: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label55;
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
               return this.this$0.deleteBookSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label59: {
         val `$result`: Any = `$continuation`.result;
         val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var11) {
                  return var11;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (BookSource.Companion)`$result`;
               break label59;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = returnData;
         `$continuation`.label = 2;
         var10000 = (BookSource.Companion)this.canEditBookSource(context, `$continuation`);
         if (var10000 === var11) {
            return var11;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         var10000 = BookSource.Companion;
         var userNameSpace: java.lang.String = context.getBodyAsString();
         userNameSpace = (java.lang.String)var10000.fromJson-IoAF18A(userNameSpace);
         val bookSource: BookSource = (if (Result.isFailure-impl(userNameSpace)) null else userNameSpace) as BookSource;
         if (bookSource == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            userNameSpace = this.getUserNameSpace(context);
            var var14: JsonArray = this.getUserBookSourceJson(userNameSpace);
            if (var14 == null) {
               var14 = new JsonArray();
            }

            val existIndex: Int = this.getBookSourceMap(userNameSpace).getOrDefault(bookSource.getBookSourceUrl(), Boxing.boxInt(-1)).intValue();
            if (existIndex >= 0) {
               var14.remove(existIndex);
            }

            this.saveUserStorage(userNameSpace, "bookSource", var14);
            this.generateBookSourceMap(userNameSpace, var14);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public suspend fun deleteBookSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label79: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label79;
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
               return this.this$0.deleteBookSources(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label83: {
         val `$result`: Any = `$continuation`.result;
         val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var18) {
                  return var18;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break label83;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = returnData;
         `$continuation`.label = 2;
         var10000 = this.canEditBookSource(context, `$continuation`);
         if (var10000 === var18) {
            return var18;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         val bookSourceJsonArray: JsonArray = context.getBodyAsJsonArray();
         val userNameSpace: java.lang.String = this.getUserNameSpace(context);
         var bookSourceList: JsonArray = this.getUserBookSourceJson(userNameSpace);
         if (bookSourceList == null) {
            bookSourceList = new JsonArray();
         }

         var var7: Int = 0;
         val var8: Int = bookSourceJsonArray.size();
         if (0 < var8) {
            do {
               val bookSourceUrl: java.lang.String = bookSourceJsonArray.getJsonObject(var7++).getString("bookSourceUrl");
               if (bookSourceUrl != null && bookSourceUrl.length() != 0) {
                  var var19: Int = -1;
                  var var20: Int = 0;
                  val var21: Int = bookSourceList.size();
                  if (0 < var21) {
                     do {
                        val i: Int = var20++;
                        if (bookSourceUrl.equals(bookSourceList.getJsonObject(i).getString("bookSourceUrl"))) {
                           var19 = i;
                           break;
                        }
                     } while (var20 < var21);
                  }

                  if (var19 >= 0) {
                     bookSourceList.remove(var19);
                  }
               }
            } while (var7 < var8);
         }

         this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
         this.generateBookSourceMap(userNameSpace, bookSourceList);
         return ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   public suspend fun deleteAllBookSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label38: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label38;
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
               return this.this$0.deleteAllBookSources(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label42: {
         val `$result`: Any = `$continuation`.result;
         val var7: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var7) {
                  return var7;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break label42;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = returnData;
         `$continuation`.label = 2;
         var10000 = this.canEditBookSource(context, `$continuation`);
         if (var10000 === var7) {
            return var7;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         val userNameSpace: java.lang.String = this.getUserNameSpace(context);
         this.saveUserStorage(userNameSpace, "bookSource", new JsonArray());
         this.generateBookSourceMap(userNameSpace, new JsonArray());
         return ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   public suspend fun setAsDefaultBookSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label32: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label32;
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
               return this.this$0.setAsDefaultBookSources(null, this);
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
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var9) {
               return var9;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookSourceController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         val username: java.lang.String = context.getBodyAsJson().getString("username");
         val bookSourceList: JsonArray = ExtKt.asJsonArray(this.getUserStorage(username, new java.lang.String[]{"bookSource"}));
         if (bookSourceList == null) {
            return returnData.setErrorMsg("用户书源不存在");
         } else {
            val var10: java.util.List = bookSourceList.getList();
            this.saveUserStorage("default", "bookSource", var10);
            this.generateBookSourceMap("default", bookSourceList);
            return ReturnData.setData$default(returnData, "设置默认书源成功", null, 2, null);
         }
      }
   }

   public suspend fun readSourceFile(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      if (context.fileUploads() != null && !context.fileUploads().isEmpty()) {
         val var12: JsonArray = new JsonArray();

         val var13: java.lang.Iterable;
         for (Object element$iv : var13) {
            val file: File = new File((`element$iv` as FileUpload).uploadedFileName());
            if (file.exists()) {
               var12.add(FilesKt.readText$default(file, null, 1, null));
               file.delete();
            }
         }

         val var14: java.util.List = var12.getList();
         return ReturnData.setData$default(returnData, var14, null, 2, null);
      } else {
         return returnData.setErrorMsg("请上传文件");
      }
   }

   public suspend fun saveFromRemoteSource(context: RoutingContext) {
      var `$continuation`: Continuation;
      label52: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label52;
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
               return this.this$0.saveFromRemoteSource(null, this);
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
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var10) {
               return var10;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookSourceController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
         return Unit.INSTANCE;
      } else {
         val url: ObjectRef = new ObjectRef();
         if (context.request().method() === HttpMethod.POST) {
            val var5: java.lang.String = context.getBodyAsJson().getString("url");
            url.element = (T)(if (var5 == null) "" else var5);
         } else {
            val var6: java.util.List = context.queryParam("url");
            val var11: java.lang.String = CollectionsKt.firstOrNull(var6);
            url.element = (T)(if (var11 == null) "" else var11);
         }

         if (url.element as java.lang.CharSequence == null || (url.element as java.lang.CharSequence).length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("请输入远程书源链接"));
            return Unit.INSTANCE;
         } else {
            BuildersKt.launch$default(
               this,
               new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
               null,
               (
                  new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, url, context, returnData, null) {
                     int label;

                     {
                        super(2, `$completionx`);
                        this.this$0 = `$receiver`;
                        this.$url = `$url`;
                        this.$context = `$context`;
                        this.$returnData = `$returnData`;
                     }

                     @Nullable
                     @Override
                     public final Object invokeSuspend(@NotNull Object $result) {
                        val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              BookSourceController.access$getWebClient$p(this.this$0)
                                 .getAbs(this.$url.element)
                                 .timeout(3000L)
                                 .send(<unrepresentable>::invokeSuspend$lambda-0);
                              return Unit.INSTANCE;
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                     }

                     @NotNull
                     @Override
                     public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                        return new <anonymous constructor>(this.this$0, this.$url, this.$context, this.$returnData, `$completion`);
                     }

                     @Nullable
                     public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                        return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                     }

                     private static final void invokeSuspend$lambda_0/* $VF was: invokeSuspend$lambda-0*/(
                        RoutingContext $context, BookSourceController this$0, ReturnData $returnData, AsyncResult it
                     ) {
                        val var5: HttpResponse = it.result() as HttpResponse;
                        val body: JsonArray = if (var5 == null) null else var5.bodyAsJsonArray();
                        if (body != null) {
                           VertExtKt.success(`$context`, `this$0`.saveBookSources(`$context`, body));
                        } else {
                           VertExtKt.success(`$context`, `$returnData`.setErrorMsg("远程书源链接错误"));
                        }
                     }
                  }
               ) as Function2,
               2,
               null
            );
            return Unit.INSTANCE;
         }
      }
   }

   public suspend fun updateRemoteSourceSub(userNameSpace: String, user: User?) {
      var `$continuation`: Continuation;
      label54: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label54;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            int I$0;
            int I$1;
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
               return this.this$0.updateRemoteSourceSub(null, null, this);
            }
         };
      }

      label48: {
         val `$result`: Any = `$continuation`.result;
         val var15: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var remoteBookSourceList: ObjectRef;
         var var5: Int;
         var var6: Int;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               remoteBookSourceList = new ObjectRef();
               val var16: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"remoteBookSourceSub"}));
               if (var16 == null) {
                  return Unit.INSTANCE;
               }

               remoteBookSourceList.element = (T)var16;
               var5 = 0;
               var6 = (remoteBookSourceList.element as JsonArray).size();
               if (0 >= var6) {
                  break label48;
               }
               break;
            case 1:
               var6 = `$continuation`.I$1;
               var5 = `$continuation`.I$0;
               remoteBookSourceList = `$continuation`.L$3 as ObjectRef;
               user = `$continuation`.L$2 as User;
               userNameSpace = `$continuation`.L$1 as java.lang.String;
               this = `$continuation`.L$0 as BookSourceController;
               ResultKt.throwOnFailure(`$result`);
               if (var5 >= var6) {
                  break label48;
               }
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         do {
            val i: Int = var5++;
            val remoteBookSource: ObjectRef = new ObjectRef();
            remoteBookSource.element = (T)(remoteBookSourceList.element as JsonArray).getJsonObject(i);
            val url: ObjectRef = new ObjectRef();
            url.element = (T)(remoteBookSource.element as JsonObject).getString("link");
            if (url.element as java.lang.CharSequence != null && (url.element as java.lang.CharSequence).length() != 0) {
               val var10000: Function1 = (
                  new Function1<Handler<AsyncResult<java.lang.Boolean>>, Unit>(this, url, userNameSpace, user, remoteBookSourceList, i, remoteBookSource) {
                     {
                        super(1);
                        this.this$0 = `$receiver`;
                        this.$url = `$url`;
                        this.$userNameSpace = `$userNameSpace`;
                        this.$user = `$user`;
                        this.$remoteBookSourceList = `$remoteBookSourceList`;
                        this.$i = `$i`;
                        this.$remoteBookSource = `$remoteBookSource`;
                     }

                     public final void invoke(@NotNull Handler<AsyncResult<java.lang.Boolean>> handler) {
                        BookSourceController.access$getWebClient$p(this.this$0)
                           .getAbs(this.$url.element)
                           .timeout(3000L)
                           .send(<unrepresentable>::invoke$lambda-0);
                     }

                     private static final void invoke$lambda_0/* $VF was: invoke$lambda-0*/(
                        ObjectRef $url,
                        BookSourceController this$0,
                        java.lang.String $userNameSpace,
                        User $user,
                        ObjectRef $remoteBookSourceList,
                        int $i,
                        ObjectRef $remoteBookSource,
                        Handler $handler,
                        AsyncResult it
                     ) {
                        val e: HttpResponse = it.result() as HttpResponse;
                        val body: JsonArray = if (e == null) null else e.bodyAsJsonArray();
                        if (body != null) {
                           try {
                              BookSourceControllerKt.access$getLogger$p()
                                 .info(
                                    "updateRemoteSourceSub link={}, result={}",
                                    `$url`.element,
                                    `this$0`.saveUserBookSources(`$userNameSpace`, `$user`, body).getErrorMsg()
                                 );
                              val var12: JsonArray = (`$remoteBookSourceList`.element as JsonArray)
                                 .set(`$i`, (`$remoteBookSource`.element as JsonObject).put("lastSyncTime", System.currentTimeMillis()));
                              `this$0`.saveUserStorage(`$userNameSpace`, "remoteBookSourceSub", var12);
                           } catch (var11: Exception) {
                              BookSourceControllerKt.access$getLogger$p().error(var11, <unrepresentable>.INSTANCE);
                           }
                        }

                        `$handler`.handle(Future.succeededFuture(true));
                     }
                  }
               ) as Function1;
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = userNameSpace;
               `$continuation`.L$2 = user;
               `$continuation`.L$3 = remoteBookSourceList;
               `$continuation`.I$0 = var5;
               `$continuation`.I$1 = var6;
               `$continuation`.label = 1;
               if (VertxCoroutineKt.awaitResult(var10000, `$continuation`) === var15) {
                  return var15;
               }
            }
         } while (var5 < var6);
      }

      generateBookSourceMap$default(this, userNameSpace, null, 2, null);
      return Unit.INSTANCE;
   }

   public suspend fun deleteUserBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label39: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label39;
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
               return this.this$0.deleteUserBookSource(null, this);
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
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var14) {
               return var14;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookSourceController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         val userJsonArray: JsonArray = context.getBodyAsJsonArray();
         var var5: Int = 0;
         val var6: Int = userJsonArray.size();
         if (0 < var6) {
            do {
               val username: java.lang.String = userJsonArray.getString(var5++);
               val var10: Array<java.lang.String> = new java.lang.String[]{"storage", "data", null, null};
               var10[2] = username;
               var10[3] = "bookSource.json";
               val userBookSourceFile: File = new File(ExtKt.getWorkDir(var10));
               if (userBookSourceFile.exists()) {
                  ExtKt.deleteRecursively(userBookSourceFile);
               }
            } while (var5 < var6);
         }

         return ReturnData.setData$default(returnData, "删除书源成功", null, 2, null);
      }
   }

   public suspend fun deleteBookSourcesFile(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label28: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label28;
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
               return this.this$0.deleteBookSourcesFile(null, this);
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
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var9) {
               return var9;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookSourceController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val userBookSourceFile: File = new File(ExtKt.getWorkDir("storage", "data", this.getUserNameSpace(context), "bookSource.json"));
         if (userBookSourceFile.exists()) {
            ExtKt.deleteRecursively(userBookSourceFile);
         }

         return ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   public fun generateBookSourceMap(userNameSpace: String, bookSourceList: JsonArray? = null): MutableMap<String, Int> {
      var bookSourceJsonArray: JsonArray = bookSourceList ?: this.getUserBookSourceJson(userNameSpace);
      if (bookSourceJsonArray == null) {
         bookSourceJsonArray = new JsonArray();
      }

      val urlMap: java.util.Map = new LinkedHashMap();
      val var12: java.util.List = new ArrayList();
      var var13: Int = 0;
      val var7: Int = bookSourceJsonArray.size();
      if (0 < var7) {
         do {
            val i: Int = var13++;
            val var9: java.lang.String = bookSourceJsonArray.getJsonObject(i).getString("bookSourceUrl");
            urlMap.put(var9, i);
            val var14: java.lang.CharSequence = bookSourceJsonArray.getJsonObject(i).getString("exploreUrl");
            if (var14 != null && var14.length() != 0) {
               var12.add(
                  MapsKt.mutableMapOf(
                     new Pair[]{
                        TuplesKt.to("bookSourceUrl", bookSourceJsonArray.getJsonObject(i).getString("bookSourceUrl")),
                        TuplesKt.to("bookSourceGroup", bookSourceJsonArray.getJsonObject(i).getString("bookSourceGroup")),
                        TuplesKt.to("bookSourceName", bookSourceJsonArray.getJsonObject(i).getString("bookSourceName"))
                     }
                  )
               );
            }
         } while (var13 < var7);
      }

      this.saveUserStorage(userNameSpace, "bookSourceMap", urlMap);
      this.saveUserStorage(userNameSpace, "bookSourceExploreList", var12);
      return urlMap;
   }

   public fun getBookSourceMap(userNameSpace: String): MutableMap<String, Int> {
      val var10000: java.lang.String = if (ExtKt.getStorageFile$default(new java.lang.String[]{"data", userNameSpace, "bookSource"}, null, 2, null).exists())
         this.getUserStorage(userNameSpace, new java.lang.String[]{"bookSourceMap"})
         else
         this.getUserStorage("default", new java.lang.String[]{"bookSourceMap"});
      if (var10000 != null && var10000.length() != 0) {
         val var11: JsonObject = ExtKt.asJsonObject(var10000);
         val var10: java.util.Map = if (var11 == null) null else var11.getMap();
         if (var10 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.Int>");
         } else {
            return TypeIntrinsics.asMutableMap(var10);
         }
      } else {
         return if (ExtKt.getStorageFile$default(new java.lang.String[]{"data", userNameSpace, "bookSource"}, null, 2, null).exists())
            generateBookSourceMap$default(this, userNameSpace, null, 2, null)
            else
            generateBookSourceMap$default(this, "default", null, 2, null);
      }
   }
}
