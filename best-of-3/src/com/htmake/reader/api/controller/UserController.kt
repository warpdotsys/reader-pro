package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.License
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.VertExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.FileUpload
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URLEncoder
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Map.Entry
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.TypeIntrinsics
import kotlin.jvm.internal.Ref.BooleanRef
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class UserController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
   public final val userMaxCount: Int = 15

   private fun getUserLimit(context: RoutingContext): Int {
      val license: License = ExtKt.getInstalledLicense$default(false, 1, null);
      val var3: java.lang.String = context.request().host();
      return if (license.validHost(var3))
         Math.min(Math.max(this.getAppConfig().getUserLimit(), 1), license.getUserMaxLimit())
         else
         Math.min(Math.max(this.getAppConfig().getUserLimit(), 1), this.userMaxCount);
   }

   public suspend fun login(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label144: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label144;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
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
               return this.this$0.login(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var46: Any;
      label138: {
         val `$result`: Any = `$continuation`.result;
         val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               var password: java.lang.String = context.getBodyAsJson().getString("username", "");
               val username: java.lang.String = if (password == null) "" else password;
               val isLogin: java.lang.String = context.getBodyAsJson().getString("password", "");
               password = if (isLogin == null) "" else isLogin;
               val userMap: java.lang.Boolean = context.getBodyAsJson().getBoolean("isLogin", Boxing.boxBoolean(false));
               val var20: Boolean = userMap != null && userMap;
               if (username.length() == 0) {
                  return returnData.setErrorMsg("请输入用户名");
               }

               if (password.length() == 0) {
                  return returnData.setErrorMsg("请输入密码");
               }

               var var23: java.util.Map = new LinkedHashMap();
               val var26: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
               if (var26 != null) {
                  val var29: java.util.Map = var26.getMap();
                  if (var29 == null) {
                     throw new NullPointerException(
                        "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
                     );
                  }

                  var23 = TypeIntrinsics.asMutableMap(var29);
               }

               if (var23 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
               }

               val var30: java.util.Map = var23.getOrDefault(username, null) as java.util.Map;
               if (var30 == null) {
                  if (var20) {
                     return returnData.setErrorMsg("用户不存在");
                  }

                  if (username.length() < 5) {
                     return returnData.setErrorMsg("用户名不能低于5位");
                  }

                  if (password.length() < this.getAppConfig().getMinUserPasswordLength()) {
                     return returnData.setErrorMsg("密码不能低于${this.getAppConfig().getMinUserPasswordLength()}位");
                  }

                  if (username.equals("default")) {
                     return returnData.setErrorMsg("用户名不能为非法字符");
                  }

                  if (!new Regex("[a-z0-9]+", RegexOption.IGNORE_CASE).matches(username)) {
                     return returnData.setErrorMsg("用户名只能由字母和数字组成");
                  }

                  if (this.getAppConfig().getInviteCode().length() > 0) {
                     val var39: java.lang.String = context.getBodyAsJson().getString("code");
                     val var34: java.lang.String = if (var39 == null) "" else var39;
                     if ((if (var39 == null) "" else var39).length() == 0) {
                        return returnData.setErrorMsg("请输入邀请码");
                     }

                     if (!this.getAppConfig().getInviteCode().equals(var34)) {
                        return returnData.setErrorMsg("邀请码错误");
                     }
                  }

                  if (var23.keySet().size() >= this.getUserLimit(context)) {
                     return returnData.setErrorMsg("超过用户数上限");
                  }

                  val var41: java.lang.String = ExtKt.getRandomString(8);
                  val var44: User = new User(
                     username, ExtKt.genEncryptedPassword(password, var41), var41, null, 0L, 0L, false, null, false, false, false, 0, 0, 8184, null
                  );
                  var44.setEnable_webdav(this.getAppConfig().getDefaultUserEnableWebdav());
                  var44.setEnable_local_store(this.getAppConfig().getDefaultUserEnableLocalStore());
                  var44.setEnable_book_source(this.getAppConfig().getDefaultUserEnableBookSource());
                  var44.setEnable_rss_source(this.getAppConfig().getDefaultUserEnableRssSource());
                  var44.setBook_source_limit(this.getAppConfig().getDefaultUserBookSourceLimit());
                  var44.setBook_limit(this.getAppConfig().getDefaultUserBookLimit());
                  var46 = this;
                  `$continuation`.L$0 = returnData;
                  `$continuation`.label = 1;
                  var46 = BaseController.saveUserSession$default((BaseController)var46, context, var44, false, `$continuation`, 4, null);
                  if (var46 === var18) {
                     return var18;
                  }
                  break label138;
               }

               if (!var20) {
                  return returnData.setErrorMsg("用户名已被占用");
               }

               val userInfo: User = ExtKt.getGson()
                  .fromJson(
                     if (var30 is java.lang.String) var30 as java.lang.String else ExtKt.getGson().toJson(var30),
                     new UserController$login$$inlined$toDataClass$1().getType()
                  );
               if (userInfo == null) {
                  return returnData.setErrorMsg("用户信息错误");
               }

               if (!(ExtKt.genEncryptedPassword(password, userInfo.getSalt()) == userInfo.getPassword())) {
                  return returnData.setErrorMsg("密码错误");
               }

               var46 = this;
               `$continuation`.L$0 = returnData;
               `$continuation`.label = 2;
               var46 = BaseController.saveUserSession$default((BaseController)var46, context, userInfo, false, `$continuation`, 4, null);
               if (var46 === var18) {
                  return var18;
               }
               break;
            case 1:
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var46 = `$result`;
               break label138;
            case 2:
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var46 = `$result`;
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         return ReturnData.setData$default(returnData, var46 as java.util.Map, null, 2, null);
      }

      return ReturnData.setData$default(returnData, var46 as java.util.Map, null, 2, null);
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + UserController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: decompilation failed (see manual patch)
     */
    public suspend fun logout(context: RoutingContext): ReturnData {
        val returnData = ReturnData()
        if (!checkAuth(context)) {
            return returnData.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!getAppConfig().secure) {
            return returnData.setErrorMsg("不支持的操作")
        }
        val username = (context.session().get("username") as String?) ?: ""
        context.session().destroy()
        var accessToken = context.queryParam("accessToken").firstOrNull() ?: ""
        if (accessToken.isNotEmpty()) {
            val parts = accessToken.split(":", limit = 2)
            if (parts.size >= 2) {
                accessToken = parts[1]
                getUserMutex().lock()
                try {
                    val userMapJson = asJsonObject(getStorage("data", "users"))
                    @Suppress("UNCHECKED_CAST")
                    val userMap: MutableMap<String, MutableMap<String, Any>> =
                        (userMapJson?.map as? MutableMap<String, MutableMap<String, Any>>)
                            ?: linkedMapOf()
                    val currentUser = userMap[username]
                    if (currentUser == null) {
                        return returnData.setErrorMsg("系统错误")
                    }
                    val tokenMapVal = currentUser["token_map"]
                    if (tokenMapVal != null) {
                        @Suppress("UNCHECKED_CAST")
                        val tokenMap = tokenMapVal as MutableMap<Any?, Any?>
                        tokenMap.remove(accessToken)
                        currentUser["token_map"] = tokenMap
                    }
                    if ((currentUser["token"] ?: "") == accessToken) {
                        currentUser["token"] = ""
                    }
                    userMap[username] = currentUser
                    saveStorage(arrayOf("data", "users"), Json.encode(userMap))
                } finally {
                    getUserMutex().unlock()
                }
            }
        }
        return returnData.setErrorMsg("请重新登录").setData("NEED_LOGIN")
    }

   public suspend fun getUserList(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label56: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label56;
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
               return this.this$0.getUserList(null, this);
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
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
         return returnData.setErrorMsg("不支持的操作");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         var var18: java.util.Map = new LinkedHashMap();
         val var20: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
         if (var20 != null) {
            val var21: java.util.Map = var20.getMap();
            if (var21 == null) {
               throw new NullPointerException(
                  "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>"
               );
            }

            var18 = TypeIntrinsics.asMutableMap(var21);
         }

         val var23: ArrayList = new ArrayList();

         for (Entry element$iv : userMap.entrySet()) {
            var23.add(this.formatUser(`element$iv`.getValue()));
         }

         return ReturnData.setData$default(returnData, var23, null, 2, null);
      }
   }

   public suspend fun addUser(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label142: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label142;
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
               return this.this$0.addUser(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var31: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
            if (var10000 === var31) {
               return var31;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (this.getAppConfig().getSecure() && this.getAppConfig().getSecureKey().length() != 0) {
         var var33: java.lang.String = context.getBodyAsJson().getString("username");
         val var32: java.lang.String = if (var33 == null) "" else var33;
         val usernameReg: java.lang.String = context.getBodyAsJson().getString("password");
         var33 = if (usernameReg == null) "" else usernameReg;
         if (var32.length() == 0) {
            return returnData.setErrorMsg("请输入用户名");
         } else if (var33.length() == 0) {
            return returnData.setErrorMsg("请输入密码");
         } else if (var32.length() < 5) {
            return returnData.setErrorMsg("用户名不能低于5位");
         } else if (var33.length() < 8) {
            return returnData.setErrorMsg("密码不能低于8位");
         } else if (var32.equals("default")) {
            return returnData.setErrorMsg("用户名不能为非法字符");
         } else if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
         } else if (!new Regex("[a-z0-9]+", RegexOption.IGNORE_CASE).matches(var32)) {
            return returnData.setErrorMsg("用户名只能由字母和数字组成");
         } else {
            var var39: java.util.Map = new LinkedHashMap();
            val var42: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var42 != null) {
               val var43: java.util.Map = var42.getMap();
               if (var43 == null) {
                  throw new NullPointerException(
                     "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
                  );
               }

               var39 = TypeIntrinsics.asMutableMap(var43);
            }

            if (var39 == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
            } else if (var39.getOrDefault(var32, null) as java.util.Map != null) {
               return returnData.setErrorMsg("用户已存在");
            } else if (var39.keySet().size() >= this.getUserLimit(context)) {
               return returnData.setErrorMsg("超过用户数上限");
            } else {
               val var45: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableWebdav");
               val var46: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableLocalStore");
               val enableBookSource: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableBookSource");
               val enableRssSource: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableRssSource");
               val bookSourceLimit: Int = context.getBodyAsJson().getInteger("bookSourceLimit");
               val bookLimit: Int = context.getBodyAsJson().getInteger("bookLimit");
               val salt: java.lang.String = ExtKt.getRandomString(8);
               val newUser: User = new User(
                  var32, ExtKt.genEncryptedPassword(var33, salt), salt, null, 0L, 0L, false, null, false, false, false, 0, 0, 8184, null
               );
               newUser.setEnable_webdav(if (var45 == null) this.getAppConfig().getDefaultUserEnableWebdav() else var45);
               newUser.setEnable_local_store(if (var46 == null) this.getAppConfig().getDefaultUserEnableLocalStore() else var46);
               newUser.setEnable_book_source(if (enableBookSource == null) this.getAppConfig().getDefaultUserEnableBookSource() else enableBookSource);
               newUser.setEnable_rss_source(if (enableRssSource == null) this.getAppConfig().getDefaultUserEnableRssSource() else enableRssSource);
               newUser.setBook_source_limit(if (bookSourceLimit == null) this.getAppConfig().getDefaultUserBookSourceLimit() else bookSourceLimit);
               newUser.setBook_limit(if (bookLimit == null) this.getAppConfig().getDefaultUserBookLimit() else bookLimit);
               var39.put(newUser.getUsername(), ExtKt.toMap(newUser));
               val userList: Array<java.lang.String> = new java.lang.String[]{"data", "users"};
               val var47: java.lang.String = Json.encode(var39);
               ExtKt.saveStorage$default(userList, var47, false, null, 12, null);
               val var49: ArrayList = new ArrayList();

               for (Entry element$iv : userMap.entrySet()) {
                  var49.add(this.formatUser(`element$iv`.getValue()));
               }

               return ReturnData.setData$default(returnData, var49, null, 2, null);
            }
         }
      } else {
         return returnData.setErrorMsg("不支持的操作");
      }
   }

   public suspend fun resetPassword(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label91: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label91;
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
               return this.this$0.resetPassword(null, this);
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
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (this.getAppConfig().getSecure() && this.getAppConfig().getSecureKey().length() != 0) {
         var var16: java.lang.String = context.getBodyAsJson().getString("username");
         val var15: java.lang.String = if (var16 == null) "" else var16;
         val userMap: java.lang.String = context.getBodyAsJson().getString("password");
         var16 = if (userMap == null) "" else userMap;
         if (var15.length() == 0) {
            return returnData.setErrorMsg("请输入用户名");
         } else if (var16.length() == 0) {
            return returnData.setErrorMsg("请输入密码");
         } else if (var16.length() < this.getAppConfig().getMinUserPasswordLength()) {
            return returnData.setErrorMsg("密码不能低于${this.getAppConfig().getMinUserPasswordLength()}位");
         } else if (var15.equals("default")) {
            return returnData.setErrorMsg("用户不存在");
         } else if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
         } else {
            var var20: java.util.Map = new LinkedHashMap();
            val var23: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var23 != null) {
               val var26: java.util.Map = var23.getMap();
               if (var26 == null) {
                  throw new NullPointerException(
                     "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>"
                  );
               }

               var20 = TypeIntrinsics.asMutableMap(var26);
            }

            if (var20 == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
            } else {
               val var27: java.util.Map = var20.getOrDefault(var15, null) as java.util.Map;
               if (var27 == null) {
                  return returnData.setErrorMsg("用户不存在");
               } else {
                  val salt: java.lang.String = ExtKt.getRandomString(8);
                  val var28: java.lang.String = ExtKt.genEncryptedPassword(var16, salt);
                  var27.put("salt", salt);
                  var27.put("password", var28);
                  var20.put(var15, var27);
                  val var29: Array<java.lang.String> = new java.lang.String[]{"data", "users"};
                  val var30: java.lang.String = Json.encode(var20);
                  ExtKt.saveStorage$default(var29, var30, false, null, 12, null);
                  return ReturnData.setData$default(returnData, "", null, 2, null);
               }
            }
         }
      } else {
         return returnData.setErrorMsg("不支持的操作");
      }
   }

   public suspend fun deleteUsers(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label72: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label72;
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
               return this.this$0.deleteUsers(null, this);
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
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
         return returnData.setErrorMsg("不支持的操作");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         var var18: java.util.Map = new LinkedHashMap();
         val var20: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
         if (var20 != null) {
            val var21: JsonArray = context.getBodyAsJsonArray();
            var `$this$forEach$iv`: Int = 0;
            val `$i$f$forEach`: Int = var21.size();
            if (0 < `$i$f$forEach`) {
               do {
                  val username: java.lang.String = var21.getString(`$this$forEach$iv`++);
                  if (username != null && var20.containsKey(username)) {
                     var20.remove(username);
                     val userHome: File = new File(ExtKt.getWorkDir("storage", "data", username));
                     UserControllerKt.access$getLogger$p().info("delete userHome: {}", userHome);
                     if (userHome.exists()) {
                        ExtKt.deleteRecursively(userHome);
                     }
                  }
               } while ($this$forEach$iv < $i$f$forEach);
            }

            val var24: java.util.Map = var20.getMap();
            if (var24 == null) {
               throw new NullPointerException(
                  "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>"
               );
            }

            var18 = TypeIntrinsics.asMutableMap(var24);
            val var25: Array<java.lang.String> = new java.lang.String[]{"data", "users"};
            val var26: java.lang.String = Json.encode(var18);
            ExtKt.saveStorage$default(var25, var26, false, null, 12, null);
         }

         val var23: ArrayList = new ArrayList();

         for (Entry element$iv : userMap.entrySet()) {
            var23.add(this.formatUser(var31.getValue()));
         }

         return ReturnData.setData$default(returnData, var23, null, 2, null);
      }
   }

   public suspend fun clearInactiveUsers(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label61: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label61;
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
               return this.this$0.clearInactiveUsers(null, this);
            }
         };
      }

      var var8: Any;
      label64: {
         val `$result`: Any = `$continuation`.result;
         var8 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var returnData: ReturnData;
         var var10: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10 = this.checkAuth(context, `$continuation`);
               if (var10 === var8) {
                  return var8;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as UserController;
               ResultKt.throwOnFailure(`$result`);
               var10 = `$result`;
               break;
            case 2:
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as UserController;
               ResultKt.throwOnFailure(`$result`);
               break label64;
            case 3:
               ResultKt.throwOnFailure(`$result`);
               return `$result`;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         if (!this.getAppConfig().getSecure()) {
            return returnData.setErrorMsg("不支持的操作");
         }

         if (this.getAppConfig().getSecureKey().length() == 0) {
            return returnData.setErrorMsg("不支持的操作");
         }

         if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
         }

         val var9: Int = context.getBodyAsJson().getInteger("inactiveDay", Boxing.boxInt(0));
         val var10001: Int = var9;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = null;
         `$continuation`.label = 2;
         if (this.clearInactiveUsers(var10001, `$continuation`) === var8) {
            return var8;
         }
      }

      `$continuation`.L$0 = null;
      `$continuation`.L$1 = null;
      `$continuation`.label = 3;
      val var10000: Any = this.getUserList(context, `$continuation`);
      return if (var10000 === var8) var8 else var10000;
   }

   public suspend fun clearInactiveUsers(day: Int) {
      val expireTime: Long = System.currentTimeMillis() - day * 86400L * 1000L;
      val var10000: Any = this.forEachUser((new Function3<CoroutineScope, User, Continuation<? super java.lang.Boolean>, Object>(expireTime, null) {
         int label;

         {
            super(3, `$completion`);
            this.$expireTime = `$expireTime`;
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  val user: User = this.L$0 as User;
                  val var10000: Boolean;
                  if ((this.L$0 as User).getLast_login_at() < this.$expireTime) {
                     UserControllerKt.access$getLogger$p().info("delete user: {}", user);
                     val userHome: File = new File(ExtKt.getWorkDir("storage", "data", user.getUsername()));
                     UserControllerKt.access$getLogger$p().info("delete userHome: {}", userHome);
                     if (userHome.exists()) {
                        ExtKt.deleteRecursively(userHome);
                     }

                     var10000 = true;
                  } else {
                     var10000 = false;
                  }

                  return Boxing.boxBoolean(var10000);
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super java.lang.Boolean> p3) {
            val var4: Function3 = new <anonymous constructor>(this.$expireTime, p3);
            var4.L$0 = p2;
            return var4.invokeSuspend(Unit.INSTANCE);
         }
      }) as (CoroutineScope?, User?, Continuation<? super java.lang.Boolean>?) -> Any, `$completion`);
      return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
   }

   public suspend fun updateUser(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label102: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label102;
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
               return this.this$0.updateUser(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var24: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
            if (var10000 === var24) {
               return var24;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
         return returnData.setErrorMsg("不支持的操作");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         val var26: java.lang.String = context.getBodyAsJson().getString("username");
         val var25: java.lang.String = if (var26 == null) "" else var26;
         if ((if (var26 == null) "" else var26).length() == 0) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val var28: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableWebdav");
            val var29: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableLocalStore");
            val enableBookSource: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableBookSource");
            val enableRssSource: java.lang.Boolean = context.getBodyAsJson().getBoolean("enableRssSource");
            val bookSourceLimit: Int = context.getBodyAsJson().getInteger("bookSourceLimit");
            val bookLimit: Int = context.getBodyAsJson().getInteger("bookLimit");
            var userMap: java.util.Map = new LinkedHashMap();
            val var30: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var30 != null) {
               var var31: java.util.Map = var30.getMap();
               if (var31 == null) {
                  throw new NullPointerException(
                     "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>"
                  );
               }

               userMap = TypeIntrinsics.asMutableMap(var31);
               if (userMap == null) {
                  throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
               }

               var31 = userMap.getOrDefault(var25, null) as java.util.Map;
               if (var31 == null) {
                  return returnData.setErrorMsg("用户不存在");
               }

               if (var28 != null) {
                  var31.put("enable_webdav", var28);
               }

               if (var29 != null) {
                  var31.put("enable_local_store", var29);
               }

               if (enableBookSource != null) {
                  var31.put("enable_book_source", enableBookSource);
               }

               if (enableRssSource != null) {
                  var31.put("enable_rss_source", enableRssSource);
               }

               if (bookSourceLimit != null) {
                  var31.put("book_source_limit", bookSourceLimit);
               }

               if (bookLimit != null) {
                  var31.put("book_limit", bookLimit);
               }

               userMap.put(var25, var31);
               val `$this$forEach$iv`: Array<java.lang.String> = new java.lang.String[]{"data", "users"};
               val var35: java.lang.String = Json.encode(userMap);
               ExtKt.saveStorage$default(`$this$forEach$iv`, var35, false, null, 12, null);
            }

            val var34: ArrayList = new ArrayList();

            for (Entry element$iv : userMap.entrySet()) {
               var34.add(this.formatUser(`element$iv`.getValue()));
            }

            return ReturnData.setData$default(returnData, var34, null, 2, null);
         }
      }
   }

   public suspend fun getUserInfo(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label51: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
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
               return this.this$0.getUserInfo(null, this);
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
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val username: java.lang.String = context.session().get("username");
      val secure: java.lang.Boolean = this.getEnv().getProperty("reader.app.secure", boolean.class);
      val secureKey: java.lang.String = this.getEnv().getProperty("reader.app.secureKey");
      var userInfo: Any = null;
      if (username != null) {
         val fontsDir: User = this.getUserInfoClass(username);
         if (fontsDir != null) {
            userInfo = this.formatUser(fontsDir);
         }
      }

      val var22: java.lang.String = ExtKt.getWorkDir("storage", "assets", "fonts");
      val var24: ArrayList = new ArrayList();

      val var25: java.lang.Iterable;
      for (Object element$iv : var25) {
         val it: File = `element$iv` as File;
         var fileName: java.lang.String = (`element$iv` as File).getName();
         if (!StringsKt.startsWith$default(fileName, ".", false, 2, null) && it.isFile()) {
            fileName = it.getName();
            val var10000: BaseController = this;
            if (BaseController.getFileExt$default(var10000, fileName, null, 2, null) == "ttf") {
               var24.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("name", it.getName()), TuplesKt.to("size", Boxing.boxLong(it.length()))}));
            }
         }
      }

      return ReturnData.setData$default(
         returnData,
         MapsKt.mapOf(
            new Pair[]{
               TuplesKt.to("userInfo", userInfo),
               TuplesKt.to("secure", secure),
               TuplesKt.to("secureKey", if (secureKey == null) null else Boxing.boxBoolean(secureKey.length() > 0)),
               TuplesKt.to("fonts", var24)
            }
         ),
         null,
         2,
         null
      );
   }

   public suspend fun saveUserConfig(context: RoutingContext): ReturnData {
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
               return this.this$0.saveUserConfig(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var8: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
            if (var10000 === var8) {
               return var8;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val content: JsonObject = context.getBodyAsJson();
         if (content == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            content.put("@updateTime", Boxing.boxLong(System.currentTimeMillis()));
            this.saveUserStorage(this.getUserNameSpace(context), "userConfig", content);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public suspend fun getUserConfig(context: RoutingContext): ReturnData {
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
               return this.this$0.getUserConfig(null, this);
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
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val userConfig: JsonObject = ExtKt.asJsonObject(this.getUserStorage(this.getUserNameSpace(context), new java.lang.String[]{"userConfig"}));
         if (userConfig == null) {
            return returnData.setErrorMsg("没有备份文件");
         } else {
            val var10: java.util.Map = userConfig.getMap();
            return ReturnData.setData$default(returnData, var10, null, 2, null);
         }
      }
   }

   public suspend fun uploadFile(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label66: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label66;
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
               return this.this$0.uploadFile(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var20: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
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
            if (var10000 === var20) {
               return var20;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (context.fileUploads() != null && !context.fileUploads().isEmpty()) {
         val var21: java.lang.String = this.getUserNameSpace(context);
         val var22: JsonArray = new JsonArray();
         var var23: java.lang.String = context.request().getParam("type");
         if (var23 == null || var23.length() == 0) {
            var23 = "images";
         }
         val var25: java.lang.Iterable;
         for (Object element$iv : var25) {
            val it: FileUpload = `element$iv` as FileUpload;
            val file: File = new File((`element$iv` as FileUpload).uploadedFileName());
            UserControllerKt.access$getLogger$p()
               .info("uploadFile: {} {} {}", new Object[]{(`element$iv` as FileUpload).uploadedFileName(), (`element$iv` as FileUpload).fileName(), file});
            if (file.exists()) {
               val var29: java.lang.String = it.fileName();
               val var15: Array<java.lang.String> = new java.lang.String[]{"storage", "assets", var21, null, null};
               var15[3] = var23;
               var15[4] = var29;
               val newFile: File = new File(ExtKt.getWorkDir(var15));
               if (!newFile.getParentFile().exists()) {
                  newFile.getParentFile().mkdirs();
               }

               if (newFile.exists()) {
                  newFile.delete();
               }

               UserControllerKt.access$getLogger$p().info("moveTo: {}", newFile);
               if (FilesKt.copyRecursively$default(file, newFile, false, null, 6, null)) {
                  var22.add("/assets/$var21/$var23/$var29");
               }

               ExtKt.deleteRecursively(file);
            }
         }

         val var26: java.util.List = var22.getList();
         return ReturnData.setData$default(returnData, var26, null, 2, null);
      } else {
         return returnData.setErrorMsg("请上传文件");
      }
   }

   public suspend fun deleteFile(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label52: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
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
               return this.this$0.deleteFile(null, this);
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
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val var11: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val userNameSpace: java.lang.String = context.getBodyAsJson().getString("url");
            var11 = if (userNameSpace == null) "" else userNameSpace;
         } else {
            val file: java.util.List = context.queryParam("url");
            val var12: java.lang.String = CollectionsKt.firstOrNull(file);
            var11 = if (var12 == null) "" else var12;
         }

         if (var11.length() == 0) {
            return returnData.setErrorMsg("请输入文件链接");
         } else if (!StringsKt.startsWith$default(var11, "/assets/${this.getUserNameSpace(context)}/", false, 2, null)) {
            return returnData.setErrorMsg("文件链接错误");
         } else {
            val var16: File = new File(ExtKt.getWorkDir(Intrinsics.stringPlus("storage", var11)));
            UserControllerKt.access$getLogger$p().info("delete file: {}", var16);
            ExtKt.deleteRecursively(var16);
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

   public suspend fun downloadBackupFile(context: RoutingContext) {
      var `$continuation`: Continuation;
      label47: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label47;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
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
               return this.this$0.downloadBackupFile(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label40: {
         var bookController: BookController;
         var userNameSpace: java.lang.String;
         var var12: Any;
         label51: {
            val `$result`: Any = `$continuation`.result;
            var12 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  returnData = new ReturnData();
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.label = 1;
                  var10000 = this.checkAuth(context, `$continuation`);
                  if (var10000 === var12) {
                     return var12;
                  }
                  break;
               case 1:
                  returnData = `$continuation`.L$2 as ReturnData;
                  context = `$continuation`.L$1 as RoutingContext;
                  this = `$continuation`.L$0 as UserController;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break;
               case 2:
                  userNameSpace = `$continuation`.L$3 as java.lang.String;
                  bookController = `$continuation`.L$2 as BookController;
                  returnData = `$continuation`.L$1 as ReturnData;
                  context = `$continuation`.L$0 as RoutingContext;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break label51;
               case 3:
                  returnData = `$continuation`.L$1 as ReturnData;
                  context = `$continuation`.L$0 as RoutingContext;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break label40;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            if (!var10000 as java.lang.Boolean) {
               VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
               return Unit.INSTANCE;
            }

            bookController = new BookController(this.getCoroutineContext());
            userNameSpace = this.getUserNameSpace(context);
            `$continuation`.L$0 = context;
            `$continuation`.L$1 = returnData;
            `$continuation`.L$2 = bookController;
            `$continuation`.L$3 = userNameSpace;
            `$continuation`.label = 2;
            var10000 = bookController.getLastBackFileFromWebdav(userNameSpace, `$continuation`);
            if (var10000 === var12) {
               return var12;
            }
         }

         val latestZipFilePath: java.lang.String = var10000 as java.lang.String;
         val backupDir: java.lang.String = ExtKt.getWorkDir("storage", "data", userNameSpace, "backup");
         `$continuation`.L$0 = context;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = null;
         `$continuation`.L$3 = null;
         `$continuation`.label = 3;
         var10000 = bookController.createUserBackup(userNameSpace, backupDir, latestZipFilePath, `$continuation`);
         if (var10000 === var12) {
            return var12;
         }
      }

      val var13: File = var10000 as File;
      if (var10000 as File == null) {
         VertExtKt.success(context, returnData.setErrorMsg("备份失败"));
         return Unit.INSTANCE;
      } else {
         val response: HttpServerResponse = context.response().putHeader("Cache-Control", "86400");
         response.putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(var13.getName(), "UTF-8")));
         response.sendFile(var13.toString());
         return Unit.INSTANCE;
      }
   }

   public suspend fun forEachUser(handler: (CoroutineScope, User, Continuation<Boolean>) -> Any?) {
      var `$continuation`: Continuation;
      label92: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label92;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
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
               return this.this$0.forEachUser(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var30: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var userMap: ObjectRef;
      var hasChanged: BooleanRef;
      var `$this$forEachUser_u24lambda_u2d7`: java.util.Iterator;
      var var15: java.util.Iterator;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            if (!this.getAppConfig().getSecure()) {
               return Unit.INSTANCE;
            }

            userMap = new ObjectRef();
            userMap.element = (T)((new LinkedHashMap()) as java.util.Map);
            val var31: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var31 != null) {
               val var33: java.util.Map = var31.getMap();
               if (var33 == null) {
                  throw new NullPointerException(
                     "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
                  );
               }

               userMap.element = (T)TypeIntrinsics.asMutableMap(var33);
            }

            hasChanged = new BooleanRef();
            val var34: java.util.Iterator = (userMap.element as java.util.Map).entrySet().iterator();
            `$this$forEachUser_u24lambda_u2d7` = var34;
            var15 = var34;
            break;
         case 1:
            var15 = `$continuation`.L$5 as java.util.Iterator;
            `$this$forEachUser_u24lambda_u2d7` = `$continuation`.L$4 as java.util.Iterator;
            hasChanged = `$continuation`.L$3 as BooleanRef;
            userMap = `$continuation`.L$2 as ObjectRef;
            handler = `$continuation`.L$1 as Function3;
            this = `$continuation`.L$0 as UserController;
            ResultKt.throwOnFailure(`$result`);
            if (`$result` as java.lang.Boolean) {
               hasChanged.element = true;
               `$this$forEachUser_u24lambda_u2d7`.remove();
            }
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      while (var15.hasNext()) {
         val user: java.util.Map = (var15.next() as Entry).getValue() as java.util.Map;
         if (user != null) {
            val existedUser: java.lang.String = user.getOrDefault("username", "");
            val username: java.lang.String = if (existedUser == null) "" else existedUser;
            if ((if (existedUser == null) "" else existedUser).length() > 0) {
               val `$this$toDataClass$iv`: java.util.Map = userMap.element as java.util.Map;
               if (userMap.element as java.util.Map == null) {
                  throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
               }

               val var43: java.util.Map = `$this$toDataClass$iv`.getOrDefault(username, null) as java.util.Map;
               var var10000: User = if (var43 == null)
                  null
                  else
                  ExtKt.getGson()
                     .fromJson(
                        if (var43 is java.lang.String) var43 as java.lang.String else ExtKt.getGson().toJson(var43),
                        new UserController$forEachUser$lambda-7$lambda-6$$inlined$toDataClass$1().getType()
                     );
               if (var10000 != null) {
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = handler;
                  `$continuation`.L$2 = userMap;
                  `$continuation`.L$3 = hasChanged;
                  `$continuation`.L$4 = `$this$forEachUser_u24lambda_u2d7`;
                  `$continuation`.L$5 = var15;
                  `$continuation`.label = 1;
                  var10000 = (User)handler.invoke(this, var10000, `$continuation`);
                  if (var10000 === var30) {
                     return var30;
                  }

                  if (var10000 as java.lang.Boolean) {
                     hasChanged.element = true;
                     `$this$forEachUser_u24lambda_u2d7`.remove();
                  }
               }
            }
         }
      }

      if (hasChanged.element) {
         val var35: Array<java.lang.String> = new java.lang.String[]{"data", "users"};
         val var36: java.lang.String = Json.encode(userMap.element);
         ExtKt.saveStorage$default(var35, var36, false, null, 12, null);
      }

      return Unit.INSTANCE;
   }
}
