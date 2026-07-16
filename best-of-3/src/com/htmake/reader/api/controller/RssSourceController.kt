package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.Debug
import io.legado.app.model.DebugLog
import io.legado.app.model.rss.Rss
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.ArrayList
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class RssSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
   public suspend fun getRssSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label28: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
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
               return this.this$0.getRssSources(null, this);
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
            this = `$continuation`.L$0 as RssSourceController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val list: JsonArray = ExtKt.asJsonArray(this.getUserStorage(this.getUserNameSpace(context), new java.lang.String[]{"rssSources"}));
         if (list != null) {
            val var11: java.util.List = list.getList();
            return ReturnData.setData$default(returnData, var11, null, 2, null);
         } else {
            return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
         }
      }
   }

   public suspend fun canEditRssSource(context: RoutingContext): Boolean {
      if (!this.getAppConfig().getSecure()) {
         return Boxing.boxBoolean(true);
      } else {
         val userInfo: User = context.get("userInfo");
         return if (userInfo == null) Boxing.boxBoolean(false) else Boxing.boxBoolean(userInfo.getEnable_book_source());
      }
   }

   public suspend fun saveRssSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label93: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label93;
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
               return this.this$0.saveRssSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label97: {
         val `$result`: Any = `$continuation`.result;
         val var16: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var16) {
                  return var16;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (RssSource.Companion)`$result`;
               break label97;
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
         var10000 = (RssSource.Companion)this.canEditRssSource(context, `$continuation`);
         if (var10000 === var16) {
            return var16;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         var10000 = RssSource.Companion;
         var userNameSpace: java.lang.String = context.getBodyAsString();
         userNameSpace = (java.lang.String)var10000.fromJson-IoAF18A(userNameSpace);
         val rssSource: RssSource = (if (Result.isFailure-impl(userNameSpace)) null else userNameSpace) as RssSource;
         if (rssSource == null) {
            return returnData.setErrorMsg("参数错误");
         } else if (rssSource.getSourceUrl().length() == 0) {
            return returnData.setErrorMsg("RSS链接不能为空");
         } else if (rssSource.getSourceName().length() == 0) {
            return returnData.setErrorMsg("RSS名称不能为空");
         } else {
            userNameSpace = this.getUserNameSpace(context);
            var var23: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"rssSources"}));
            if (var23 == null) {
               var23 = new JsonArray();
            }

            var var24: Int = -1;
            var list: Int = 0;
            val var9: Int = var23.size();
            if (0 < var9) {
               do {
                  val i: Int = list++;
                  var10000 = RssSource.Companion;
                  var var12: java.lang.String = var23.getJsonObject(i).toString();
                  var12 = (java.lang.String)var10000.fromJson-IoAF18A(var12);
                  val _rssSource: RssSource = (if (Result.isFailure-impl(var12)) null else var12) as RssSource;
                  if (_rssSource != null && _rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                     var24 = i;
                     break;
                  }
               } while (list < var9);
            }

            if (var24 >= 0) {
               val var25: java.util.List = var23.getList();
               var25.set(var24, JsonObject.mapFrom(rssSource));
               var23 = new JsonArray(var25);
            } else {
               var23.add(JsonObject.mapFrom(rssSource));
            }

            this.saveUserStorage(userNameSpace, "rssSources", var23);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public suspend fun saveRssSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label105: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label105;
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
               return this.this$0.saveRssSources(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label109: {
         val `$result`: Any = `$continuation`.result;
         val var20: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var20) {
                  return var20;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (RssSource.Companion)`$result`;
               break label109;
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
         var10000 = (RssSource.Companion)this.canEditRssSource(context, `$continuation`);
         if (var10000 === var20) {
            return var20;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         val rssSourceJsonArray: JsonArray = context.getBodyAsJsonArray();
         if (rssSourceJsonArray == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val userNameSpace: java.lang.String = this.getUserNameSpace(context);
            var rssSourceList: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"rssSources"}));
            if (rssSourceList == null) {
               rssSourceList = new JsonArray();
            }

            var var21: Int = 0;
            val var8: Int = rssSourceJsonArray.size();
            if (0 < var8) {
               do {
                  val k: Int = var21++;
                  var10000 = RssSource.Companion;
                  var existIndex: java.lang.String = rssSourceJsonArray.getJsonObject(k).toString();
                  existIndex = (java.lang.String)var10000.fromJson-IoAF18A(existIndex);
                  val rssSource: RssSource = (if (Result.isFailure-impl(existIndex)) null else existIndex) as RssSource;
                  if (rssSource != null && rssSource.getSourceUrl().length() != 0 && rssSource.getSourceName().length() != 0) {
                     var var25: Int = -1;
                     var var28: Int = 0;
                     val var13: Int = rssSourceList.size();
                     if (0 < var13) {
                        do {
                           val i: Int = var28++;
                           var10000 = RssSource.Companion;
                           var var16: java.lang.String = rssSourceList.getJsonObject(i).toString();
                           var16 = (java.lang.String)var10000.fromJson-IoAF18A(var16);
                           val _rssSource: RssSource = (if (Result.isFailure-impl(var16)) null else var16) as RssSource;
                           if (_rssSource != null && _rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                              var25 = i;
                              break;
                           }
                        } while (var28 < var13);
                     }

                     if (var25 >= 0) {
                        val var29: java.util.List = rssSourceList.getList();
                        var29.set(var25, JsonObject.mapFrom(rssSource));
                        rssSourceList = new JsonArray(var29);
                     } else {
                        rssSourceList.add(JsonObject.mapFrom(rssSource));
                     }
                  }
               } while (var21 < var8);
            }

            this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public suspend fun deleteRssSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label74: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label74;
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
               return this.this$0.deleteRssSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label78: {
         val `$result`: Any = `$continuation`.result;
         val var16: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var16) {
                  return var16;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (RssSource.Companion)`$result`;
               break label78;
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
         var10000 = (RssSource.Companion)this.canEditRssSource(context, `$continuation`);
         if (var10000 === var16) {
            return var16;
         }
      }

      if (!var10000 as java.lang.Boolean) {
         return returnData.setErrorMsg("权限不足");
      } else {
         var10000 = RssSource.Companion;
         var userNameSpace: java.lang.String = context.getBodyAsString();
         userNameSpace = (java.lang.String)var10000.fromJson-IoAF18A(userNameSpace);
         val rssSource: RssSource = (if (Result.isFailure-impl(userNameSpace)) null else userNameSpace) as RssSource;
         if (rssSource == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            userNameSpace = this.getUserNameSpace(context);
            var var19: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"rssSources"}));
            if (var19 == null) {
               var19 = new JsonArray();
            }

            var var20: Int = -1;
            var var8: Int = 0;
            val var9: Int = var19.size();
            if (0 < var9) {
               do {
                  val i: Int = var8++;
                  var10000 = RssSource.Companion;
                  var var12: java.lang.String = var19.getJsonObject(i).toString();
                  var12 = (java.lang.String)var10000.fromJson-IoAF18A(var12);
                  val _rssSource: RssSource = (if (Result.isFailure-impl(var12)) null else var12) as RssSource;
                  if (_rssSource != null && _rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                     var20 = i;
                     break;
                  }
               } while (var8 < var9);
            }

            if (var20 >= 0) {
               var19.remove(var20);
            }

            this.saveUserStorage(userNameSpace, "rssSources", var19);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public fun getRssSourceByURL(url: String, userNameSpace: String): RssSource? {
      if (url.length() == 0) {
         return null;
      } else {
         val var10: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"rssSources"}));
         if (var10 == null) {
            return null;
         } else {
            var var12: Int = 0;
            val var5: Int = var10.size();
            if (0 < var5) {
               do {
                  val i: Int = var12++;
                  val var10000: RssSource.Companion = RssSource.Companion;
                  var var8: java.lang.String = var10.getJsonObject(i).toString();
                  var8 = (java.lang.String)var10000.fromJson-IoAF18A(var8);
                  val _rssSource: RssSource = (if (Result.isFailure-impl(var8)) null else var8) as RssSource;
                  if (_rssSource != null && _rssSource.getSourceUrl().equals(url)) {
                     return _rssSource;
                  }
               } while (var12 < var5);
            }

            return null;
         }
      }
   }

   public suspend fun getRssArticles(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label81: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label81;
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
               return this.this$0.getRssArticles(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Rss;
      label84: {
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
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (Rss)`$result`;
               break label84;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val var15: java.lang.String;
         val var16: java.lang.String;
         var var17: java.lang.String;
         val var18: Int;
         if (context.request().method() === HttpMethod.POST) {
            var userNameSpace: java.lang.String = context.getBodyAsJson().getString("sourceUrl");
            var15 = userNameSpace;
            userNameSpace = context.getBodyAsJson().getString("sortName", "");
            var16 = userNameSpace;
            userNameSpace = context.getBodyAsJson().getString("sortUrl", "");
            var17 = userNameSpace;
            val var21: Int = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
            var18 = var21.intValue();
         } else {
            var rssSource: java.util.List = context.queryParam("sourceUrl");
            var var22: java.lang.String = CollectionsKt.firstOrNull(rssSource);
            var15 = if (var22 == null) "" else var22;
            rssSource = context.queryParam("sortName");
            var22 = CollectionsKt.firstOrNull(rssSource);
            var16 = if (var22 == null) "" else var22;
            rssSource = context.queryParam("sortUrl");
            var22 = CollectionsKt.firstOrNull(rssSource);
            var17 = if (var22 == null) "" else var22;
            rssSource = context.queryParam("page");
            var22 = CollectionsKt.firstOrNull(rssSource);
            val var37: Int;
            if (var22 == null) {
               var37 = 1;
            } else {
               val var32: Int = Boxing.boxInt(Integer.parseInt(var22));
               var37 = if (var32 == null) 1 else var32;
            }

            var18 = var37;
         }

         if (var15.length() == 0) {
            return returnData.setErrorMsg("RSS源链接不能为空");
         }

         if (var17.length() == 0) {
            var17 = var15;
         }

         val var35: RssSource = this.getRssSourceByURL(var15, this.getUserNameSpace(context));
         if (var35 == null) {
            return returnData.setErrorMsg("RSS源不存在");
         }

         var10000 = Rss.INSTANCE;
         val var10005: DebugLog = Debug.INSTANCE;
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.label = 2;
         var10000 = (Rss)var10000.getArticles(var16, var17, var35, var18, var10005, `$continuation`);
         if (var10000 === var14) {
            return var14;
         }
      }

      return ReturnData.setData$default(returnData, var10000 as Pair, null, 2, null);
   }

   public suspend fun getRssContent(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label103: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label103;
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
               return this.this$0.getRssContent(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Rss;
      label106: {
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
               this = `$continuation`.L$0 as RssSourceController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var10000 = (Rss)`$result`;
               break label106;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val var15: java.lang.String;
         val var16: java.lang.String;
         val var17: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            var userNameSpace: java.lang.String = context.getBodyAsJson().getString("sourceUrl");
            var15 = userNameSpace;
            userNameSpace = context.getBodyAsJson().getString("link");
            var16 = userNameSpace;
            userNameSpace = context.getBodyAsJson().getString("origin");
            var17 = userNameSpace;
         } else {
            var rssSource: java.util.List = context.queryParam("sourceUrl");
            var var20: java.lang.String = CollectionsKt.firstOrNull(rssSource);
            var15 = if (var20 == null) "" else var20;
            rssSource = context.queryParam("link");
            var20 = CollectionsKt.firstOrNull(rssSource);
            var16 = if (var20 == null) "" else var20;
            rssSource = context.queryParam("origin");
            var20 = CollectionsKt.firstOrNull(rssSource);
            var17 = if (var20 == null) "" else var20;
         }

         if (var15.length() == 0) {
            return returnData.setErrorMsg("RSS链接不能为空");
         }

         if (var16.length() == 0) {
            return returnData.setErrorMsg("RSS文章链接不能为空");
         }

         if (var17.length() == 0) {
            return returnData.setErrorMsg("RSS文章来源不能为空");
         }

         val var32: RssSource = this.getRssSourceByURL(var15, this.getUserNameSpace(context));
         if (var32 == null) {
            return returnData.setErrorMsg("RSS源不存在");
         }

         val rssArticle: RssArticle = new RssArticle(var17, null, null, 0L, var16, null, null, null, null, false, null, 2030, null);
         if (var32.getRuleContent() == null) {
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }

         var10000 = Rss.INSTANCE;
         val var11: java.lang.String = var32.getRuleContent();
         if (var11 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
         }

         val var10004: DebugLog = Debug.INSTANCE;
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.label = 2;
         var10000 = (Rss)var10000.getContent(rssArticle, var11, var32, var10004, `$continuation`);
         if (var10000 === var14) {
            return var14;
         }
      }

      return ReturnData.setData$default(returnData, var10000 as java.lang.String, null, 2, null);
   }
}
