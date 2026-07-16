package com.htmake.reader.api.controller

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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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

   public suspend fun logout(context: RoutingContext): ReturnData {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:361)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:504)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1064)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:565)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //
      // Bytecode:
      // 000: aload 2
      // 001: instanceof com/htmake/reader/api/controller/UserController$logout$1
      // 004: ifeq 027
      // 007: aload 2
      // 008: checkcast com/htmake/reader/api/controller/UserController$logout$1
      // 00b: astore 16
      // 00d: aload 16
      // 00f: getfield com/htmake/reader/api/controller/UserController$logout$1.label I
      // 012: ldc -2147483648
      // 014: iand
      // 015: ifeq 027
      // 018: aload 16
      // 01a: dup
      // 01b: getfield com/htmake/reader/api/controller/UserController$logout$1.label I
      // 01e: ldc -2147483648
      // 020: isub
      // 021: putfield com/htmake/reader/api/controller/UserController$logout$1.label I
      // 024: goto 032
      // 027: new com/htmake/reader/api/controller/UserController$logout$1
      // 02a: dup
      // 02b: aload 0
      // 02c: aload 2
      // 02d: invokespecial com/htmake/reader/api/controller/UserController$logout$1.<init> (Lcom/htmake/reader/api/controller/UserController;Lkotlin/coroutines/Continuation;)V
      // 030: astore 16
      // 032: aload 16
      // 034: getfield com/htmake/reader/api/controller/UserController$logout$1.result Ljava/lang/Object;
      // 037: astore 15
      // 039: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03c: astore 17
      // 03e: aload 16
      // 040: getfield com/htmake/reader/api/controller/UserController$logout$1.label I
      // 043: tableswitch 792 0 2 25 78 399
      // 05c: aload 15
      // 05e: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 061: new com/htmake/reader/api/ReturnData
      // 064: dup
      // 065: invokespecial com/htmake/reader/api/ReturnData.<init> ()V
      // 068: astore 3
      // 069: aload 0
      // 06a: aload 1
      // 06b: aload 16
      // 06d: aload 16
      // 06f: aload 0
      // 070: putfield com/htmake/reader/api/controller/UserController$logout$1.L$0 Ljava/lang/Object;
      // 073: aload 16
      // 075: aload 1
      // 076: putfield com/htmake/reader/api/controller/UserController$logout$1.L$1 Ljava/lang/Object;
      // 079: aload 16
      // 07b: aload 3
      // 07c: putfield com/htmake/reader/api/controller/UserController$logout$1.L$2 Ljava/lang/Object;
      // 07f: aload 16
      // 081: bipush 1
      // 082: putfield com/htmake/reader/api/controller/UserController$logout$1.label I
      // 085: invokevirtual com/htmake/reader/api/controller/UserController.checkAuth (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 088: dup
      // 089: aload 17
      // 08b: if_acmpne 0b3
      // 08e: aload 17
      // 090: areturn
      // 091: aload 16
      // 093: getfield com/htmake/reader/api/controller/UserController$logout$1.L$2 Ljava/lang/Object;
      // 096: checkcast com/htmake/reader/api/ReturnData
      // 099: astore 3
      // 09a: aload 16
      // 09c: getfield com/htmake/reader/api/controller/UserController$logout$1.L$1 Ljava/lang/Object;
      // 09f: checkcast io/vertx/ext/web/RoutingContext
      // 0a2: astore 1
      // 0a3: aload 16
      // 0a5: getfield com/htmake/reader/api/controller/UserController$logout$1.L$0 Ljava/lang/Object;
      // 0a8: checkcast com/htmake/reader/api/controller/UserController
      // 0ab: astore 0
      // 0ac: aload 15
      // 0ae: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 0b1: aload 15
      // 0b3: checkcast java/lang/Boolean
      // 0b6: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 0b9: ifne 0cd
      // 0bc: aload 3
      // 0bd: ldc_w "NEED_LOGIN"
      // 0c0: aconst_null
      // 0c1: bipush 2
      // 0c2: aconst_null
      // 0c3: invokestatic com/htmake/reader/api/ReturnData.setData$default (Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
      // 0c6: ldc_w "请登录后使用"
      // 0c9: invokevirtual com/htmake/reader/api/ReturnData.setErrorMsg (Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
      // 0cc: areturn
      // 0cd: aload 0
      // 0ce: invokevirtual com/htmake/reader/api/controller/UserController.getAppConfig ()Lcom/htmake/reader/config/AppConfig;
      // 0d1: invokevirtual com/htmake/reader/config/AppConfig.getSecure ()Z
      // 0d4: ifne 0df
      // 0d7: aload 3
      // 0d8: ldc_w "不支持的操作"
      // 0db: invokevirtual com/htmake/reader/api/ReturnData.setErrorMsg (Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
      // 0de: areturn
      // 0df: aload 1
      // 0e0: invokeinterface io/vertx/ext/web/RoutingContext.session ()Lio/vertx/ext/web/Session; 1
      // 0e5: ldc "username"
      // 0e7: invokeinterface io/vertx/ext/web/Session.get (Ljava/lang/String;)Ljava/lang/Object; 2
      // 0ec: checkcast java/lang/String
      // 0ef: astore 5
      // 0f1: aload 5
      // 0f3: ifnonnull 0fb
      // 0f6: ldc ""
      // 0f8: goto 0fd
      // 0fb: aload 5
      // 0fd: astore 4
      // 0ff: aload 1
      // 100: invokeinterface io/vertx/ext/web/RoutingContext.session ()Lio/vertx/ext/web/Session; 1
      // 105: invokeinterface io/vertx/ext/web/Session.destroy ()V 1
      // 10a: aload 1
      // 10b: ldc_w "accessToken"
      // 10e: invokeinterface io/vertx/ext/web/RoutingContext.queryParam (Ljava/lang/String;)Ljava/util/List; 2
      // 113: astore 7
      // 115: aload 7
      // 117: ldc_w "context.queryParam(\"accessToken\")"
      // 11a: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 11d: aload 7
      // 11f: invokestatic kotlin/collections/CollectionsKt.firstOrNull (Ljava/util/List;)Ljava/lang/Object;
      // 122: checkcast java/lang/String
      // 125: astore 6
      // 127: aload 6
      // 129: ifnonnull 131
      // 12c: ldc ""
      // 12e: goto 133
      // 131: aload 6
      // 133: astore 5
      // 135: aload 5
      // 137: checkcast java/lang/CharSequence
      // 13a: astore 6
      // 13c: bipush 0
      // 13d: istore 7
      // 13f: aload 6
      // 141: invokeinterface java/lang/CharSequence.length ()I 1
      // 146: ifle 14d
      // 149: bipush 1
      // 14a: goto 14e
      // 14d: bipush 0
      // 14e: ifeq 34a
      // 151: aload 5
      // 153: checkcast java/lang/CharSequence
      // 156: bipush 1
      // 157: anewarray 83
      // 15a: astore 7
      // 15c: aload 7
      // 15e: bipush 0
      // 15f: ldc_w ":"
      // 162: aastore
      // 163: aload 7
      // 165: bipush 0
      // 166: bipush 2
      // 167: bipush 2
      // 168: aconst_null
      // 169: invokestatic kotlin/text/StringsKt.split$default (Ljava/lang/CharSequence;[Ljava/lang/String;ZIILjava/lang/Object;)Ljava/util/List;
      // 16c: astore 6
      // 16e: aload 6
      // 170: invokeinterface java/util/List.size ()I 1
      // 175: bipush 2
      // 176: if_icmplt 34a
      // 179: aload 6
      // 17b: bipush 1
      // 17c: invokeinterface java/util/List.get (I)Ljava/lang/Object; 2
      // 181: checkcast java/lang/String
      // 184: astore 5
      // 186: bipush 0
      // 187: istore 8
      // 189: new java/util/LinkedHashMap
      // 18c: dup
      // 18d: invokespecial java/util/LinkedHashMap.<init> ()V
      // 190: checkcast java/util/Map
      // 193: astore 7
      // 195: nop
      // 196: aload 0
      // 197: invokevirtual com/htmake/reader/api/controller/UserController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 19a: aconst_null
      // 19b: aload 16
      // 19d: bipush 1
      // 19e: aconst_null
      // 19f: aload 16
      // 1a1: aload 0
      // 1a2: putfield com/htmake/reader/api/controller/UserController$logout$1.L$0 Ljava/lang/Object;
      // 1a5: aload 16
      // 1a7: aload 3
      // 1a8: putfield com/htmake/reader/api/controller/UserController$logout$1.L$1 Ljava/lang/Object;
      // 1ab: aload 16
      // 1ad: aload 4
      // 1af: putfield com/htmake/reader/api/controller/UserController$logout$1.L$2 Ljava/lang/Object;
      // 1b2: aload 16
      // 1b4: aload 5
      // 1b6: putfield com/htmake/reader/api/controller/UserController$logout$1.L$3 Ljava/lang/Object;
      // 1b9: aload 16
      // 1bb: aload 7
      // 1bd: putfield com/htmake/reader/api/controller/UserController$logout$1.L$4 Ljava/lang/Object;
      // 1c0: aload 16
      // 1c2: bipush 2
      // 1c3: putfield com/htmake/reader/api/controller/UserController$logout$1.label I
      // 1c6: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.lock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 1c9: dup
      // 1ca: aload 17
      // 1cc: if_acmpne 20a
      // 1cf: aload 17
      // 1d1: areturn
      // 1d2: aload 16
      // 1d4: getfield com/htmake/reader/api/controller/UserController$logout$1.L$4 Ljava/lang/Object;
      // 1d7: checkcast java/util/Map
      // 1da: astore 7
      // 1dc: aload 16
      // 1de: getfield com/htmake/reader/api/controller/UserController$logout$1.L$3 Ljava/lang/Object;
      // 1e1: checkcast java/lang/String
      // 1e4: astore 5
      // 1e6: aload 16
      // 1e8: getfield com/htmake/reader/api/controller/UserController$logout$1.L$2 Ljava/lang/Object;
      // 1eb: checkcast java/lang/String
      // 1ee: astore 4
      // 1f0: aload 16
      // 1f2: getfield com/htmake/reader/api/controller/UserController$logout$1.L$1 Ljava/lang/Object;
      // 1f5: checkcast com/htmake/reader/api/ReturnData
      // 1f8: astore 3
      // 1f9: aload 16
      // 1fb: getfield com/htmake/reader/api/controller/UserController$logout$1.L$0 Ljava/lang/Object;
      // 1fe: checkcast com/htmake/reader/api/controller/UserController
      // 201: astore 0
      // 202: nop
      // 203: aload 15
      // 205: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 208: aload 15
      // 20a: pop
      // 20b: bipush 2
      // 20c: anewarray 83
      // 20f: astore 9
      // 211: aload 9
      // 213: bipush 0
      // 214: ldc "data"
      // 216: aastore
      // 217: aload 9
      // 219: bipush 1
      // 21a: ldc "users"
      // 21c: aastore
      // 21d: aload 9
      // 21f: aconst_null
      // 220: bipush 2
      // 221: aconst_null
      // 222: invokestatic com/htmake/reader/utils/ExtKt.getStorage$default ([Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
      // 225: invokestatic com/htmake/reader/utils/ExtKt.asJsonObject (Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;
      // 228: astore 8
      // 22a: aload 8
      // 22c: ifnull 24d
      // 22f: aload 8
      // 231: invokevirtual io/vertx/core/json/JsonObject.getMap ()Ljava/util/Map;
      // 234: astore 9
      // 236: aload 9
      // 238: ifnonnull 246
      // 23b: new java/lang/NullPointerException
      // 23e: dup
      // 23f: ldc_w "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>"
      // 242: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
      // 245: athrow
      // 246: aload 9
      // 248: invokestatic kotlin/jvm/internal/TypeIntrinsics.asMutableMap (Ljava/lang/Object;)Ljava/util/Map;
      // 24b: astore 7
      // 24d: aload 7
      // 24f: astore 10
      // 251: aconst_null
      // 252: astore 11
      // 254: bipush 0
      // 255: istore 12
      // 257: aload 10
      // 259: dup
      // 25a: ifnonnull 267
      // 25d: new java/lang/NullPointerException
      // 260: dup
      // 261: ldc "null cannot be cast to non-null type kotlin.collections.Map<K, V>"
      // 263: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
      // 266: athrow
      // 267: aload 4
      // 269: aload 11
      // 26b: invokeinterface java/util/Map.getOrDefault (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 270: checkcast java/util/Map
      // 273: astore 9
      // 275: aload 9
      // 277: ifnonnull 290
      // 27a: aload 3
      // 27b: ldc_w "系统错误"
      // 27e: invokevirtual com/htmake/reader/api/ReturnData.setErrorMsg (Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
      // 281: astore 10
      // 283: aload 0
      // 284: invokevirtual com/htmake/reader/api/controller/UserController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 287: aconst_null
      // 288: bipush 1
      // 289: aconst_null
      // 28a: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 28d: aload 10
      // 28f: areturn
      // 290: aload 9
      // 292: astore 11
      // 294: ldc_w "token_map"
      // 297: astore 12
      // 299: aconst_null
      // 29a: astore 13
      // 29c: bipush 0
      // 29d: istore 14
      // 29f: aload 11
      // 2a1: aload 12
      // 2a3: aload 13
      // 2a5: invokeinterface java/util/Map.getOrDefault (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 2aa: astore 10
      // 2ac: aload 10
      // 2ae: ifnull 2d4
      // 2b1: aload 10
      // 2b3: invokestatic kotlin/jvm/internal/TypeIntrinsics.asMutableMap (Ljava/lang/Object;)Ljava/util/Map;
      // 2b6: astore 11
      // 2b8: aload 11
      // 2ba: ifnull 2d4
      // 2bd: aload 11
      // 2bf: aload 5
      // 2c1: invokeinterface java/util/Map.remove (Ljava/lang/Object;)Ljava/lang/Object; 2
      // 2c6: pop
      // 2c7: aload 9
      // 2c9: ldc_w "token_map"
      // 2cc: aload 11
      // 2ce: invokeinterface java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 2d3: pop
      // 2d4: aload 9
      // 2d6: ldc_w "token"
      // 2d9: ldc ""
      // 2db: invokeinterface java/util/Map.getOrDefault (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 2e0: aload 5
      // 2e2: invokevirtual java/lang/Object.equals (Ljava/lang/Object;)Z
      // 2e5: ifeq 2f5
      // 2e8: aload 9
      // 2ea: ldc_w "token"
      // 2ed: ldc ""
      // 2ef: invokeinterface java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 2f4: pop
      // 2f5: aload 7
      // 2f7: aload 4
      // 2f9: aload 9
      // 2fb: invokeinterface java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 300: pop
      // 301: bipush 2
      // 302: anewarray 83
      // 305: astore 11
      // 307: aload 11
      // 309: bipush 0
      // 30a: ldc "data"
      // 30c: aastore
      // 30d: aload 11
      // 30f: bipush 1
      // 310: ldc "users"
      // 312: aastore
      // 313: aload 11
      // 315: aload 7
      // 317: invokestatic io/vertx/core/json/Json.encode (Ljava/lang/Object;)Ljava/lang/String;
      // 31a: astore 11
      // 31c: aload 11
      // 31e: ldc_w "encode(userMap)"
      // 321: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 324: aload 11
      // 326: bipush 0
      // 327: aconst_null
      // 328: bipush 12
      // 32a: aconst_null
      // 32b: invokestatic com/htmake/reader/utils/ExtKt.saveStorage$default ([Ljava/lang/String;Ljava/lang/Object;ZLjava/lang/String;ILjava/lang/Object;)V
      // 32e: aload 0
      // 32f: invokevirtual com/htmake/reader/api/controller/UserController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 332: aconst_null
      // 333: bipush 1
      // 334: aconst_null
      // 335: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 338: goto 34a
      // 33b: astore 8
      // 33d: aload 0
      // 33e: invokevirtual com/htmake/reader/api/controller/UserController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 341: aconst_null
      // 342: bipush 1
      // 343: aconst_null
      // 344: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 347: aload 8
      // 349: athrow
      // 34a: aload 3
      // 34b: ldc_w "请重新登录"
      // 34e: invokevirtual com/htmake/reader/api/ReturnData.setErrorMsg (Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
      // 351: ldc_w "NEED_LOGIN"
      // 354: aconst_null
      // 355: bipush 2
      // 356: aconst_null
      // 357: invokestatic com/htmake/reader/api/ReturnData.setData$default (Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
      // 35a: areturn
      // 35b: new java/lang/IllegalStateException
      // 35e: dup
      // 35f: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 362: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 365: athrow
   }

   public suspend fun getUserList(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label56: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
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
