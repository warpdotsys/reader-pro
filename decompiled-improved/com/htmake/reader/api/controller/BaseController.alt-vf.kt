package com.htmake.reader.api.controller

import com.htmake.reader.config.AppConfig
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import io.legado.app.utils.FileUtils
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.util.ArrayList
import java.util.LinkedHashMap
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.internal.SpreadBuilder
import kotlin.jvm.internal.TypeIntrinsics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.MutexKt
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.springframework.core.env.Environment

public open class BaseController(coroutineContext: CoroutineContext) : CoroutineScope {
   public final val appConfig: AppConfig
   public open val coroutineContext: CoroutineContext
   public final val env: Environment
   private final var loginExpireDays: Int
   public final val userMutex: Mutex

   init {
      this.coroutineContext = coroutineContext;
      this.loginExpireDays = 7;
      var var2: Any = SpringContextUtils.getBean("appConfig", AppConfig.class);
      this.appConfig = var2 as AppConfig;
      var2 = SpringContextUtils.getBean(Environment.class);
      this.env = var2 as Environment;
      this.userMutex = MutexKt.Mutex$default(false, 1, null);
   }

   public suspend fun saveUserSession(context: RoutingContext, user: User, regenerateToken: Boolean = ...): Map<String, Any> {
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
      // 000: aload 4
      // 002: instanceof com/htmake/reader/api/controller/BaseController$saveUserSession$1
      // 005: ifeq 029
      // 008: aload 4
      // 00a: checkcast com/htmake/reader/api/controller/BaseController$saveUserSession$1
      // 00d: astore 13
      // 00f: aload 13
      // 011: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 13
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BaseController$saveUserSession$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 4
      // 030: invokespecial com/htmake/reader/api/controller/BaseController$saveUserSession$1.<init> (Lcom/htmake/reader/api/controller/BaseController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 13
      // 035: aload 13
      // 037: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.result Ljava/lang/Object;
      // 03a: astore 12
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 14
      // 041: aload 13
      // 043: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.label I
      // 046: tableswitch 527 0 1 22 79
      // 05c: aload 12
      // 05e: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 061: nop
      // 062: aload 0
      // 063: invokevirtual com/htmake/reader/api/controller/BaseController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 066: aconst_null
      // 067: aload 13
      // 069: bipush 1
      // 06a: aconst_null
      // 06b: aload 13
      // 06d: aload 0
      // 06e: putfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.L$0 Ljava/lang/Object;
      // 071: aload 13
      // 073: aload 1
      // 074: putfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.L$1 Ljava/lang/Object;
      // 077: aload 13
      // 079: aload 2
      // 07a: putfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.L$2 Ljava/lang/Object;
      // 07d: aload 13
      // 07f: iload 3
      // 080: putfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.Z$0 Z
      // 083: aload 13
      // 085: bipush 1
      // 086: putfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.label I
      // 089: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.lock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 08c: dup
      // 08d: aload 14
      // 08f: if_acmpne 0be
      // 092: aload 14
      // 094: areturn
      // 095: aload 13
      // 097: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.Z$0 Z
      // 09a: istore 3
      // 09b: aload 13
      // 09d: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.L$2 Ljava/lang/Object;
      // 0a0: checkcast com/htmake/reader/entity/User
      // 0a3: astore 2
      // 0a4: aload 13
      // 0a6: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.L$1 Ljava/lang/Object;
      // 0a9: checkcast io/vertx/ext/web/RoutingContext
      // 0ac: astore 1
      // 0ad: aload 13
      // 0af: getfield com/htmake/reader/api/controller/BaseController$saveUserSession$1.L$0 Ljava/lang/Object;
      // 0b2: checkcast com/htmake/reader/api/controller/BaseController
      // 0b5: astore 0
      // 0b6: nop
      // 0b7: aload 12
      // 0b9: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 0bc: aload 12
      // 0be: pop
      // 0bf: aload 2
      // 0c0: invokestatic java/lang/System.currentTimeMillis ()J
      // 0c3: invokevirtual com/htmake/reader/entity/User.setLast_login_at (J)V
      // 0c6: iload 3
      // 0c7: ifeq 171
      // 0ca: aload 2
      // 0cb: aload 2
      // 0cc: invokevirtual com/htmake/reader/entity/User.getUsername ()Ljava/lang/String;
      // 0cf: invokestatic java/lang/System.currentTimeMillis ()J
      // 0d2: invokestatic java/lang/String.valueOf (J)Ljava/lang/String;
      // 0d5: invokestatic com/htmake/reader/utils/ExtKt.genEncryptedPassword (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      // 0d8: invokevirtual com/htmake/reader/entity/User.setToken (Ljava/lang/String;)V
      // 0db: aconst_null
      // 0dc: astore 5
      // 0de: invokestatic java/lang/System.currentTimeMillis ()J
      // 0e1: aload 0
      // 0e2: getfield com/htmake/reader/api/controller/BaseController.loginExpireDays I
      // 0e5: ldc 86400
      // 0e7: imul
      // 0e8: sipush 1000
      // 0eb: imul
      // 0ec: i2l
      // 0ed: ladd
      // 0ee: lstore 6
      // 0f0: aload 2
      // 0f1: invokevirtual com/htmake/reader/entity/User.getToken_map ()Ljava/util/Map;
      // 0f4: ifnull 10d
      // 0f7: aload 2
      // 0f8: invokevirtual com/htmake/reader/entity/User.getToken_map ()Ljava/util/Map;
      // 0fb: astore 8
      // 0fd: aload 8
      // 0ff: invokestatic kotlin/jvm/internal/TypeIntrinsics.isMutableMap (Ljava/lang/Object;)Z
      // 102: ifeq 10a
      // 105: aload 8
      // 107: goto 10b
      // 10a: aconst_null
      // 10b: astore 5
      // 10d: aload 5
      // 10f: ifnonnull 132
      // 112: bipush 1
      // 113: anewarray 177
      // 116: astore 8
      // 118: aload 8
      // 11a: bipush 0
      // 11b: aload 2
      // 11c: invokevirtual com/htmake/reader/entity/User.getToken ()Ljava/lang/String;
      // 11f: lload 6
      // 121: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxLong (J)Ljava/lang/Long;
      // 124: invokestatic kotlin/TuplesKt.to (Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
      // 127: aastore
      // 128: aload 8
      // 12a: invokestatic kotlin/collections/MapsKt.mutableMapOf ([Lkotlin/Pair;)Ljava/util/Map;
      // 12d: astore 5
      // 12f: goto 152
      // 132: aload 5
      // 134: astore 8
      // 136: aload 2
      // 137: invokevirtual com/htmake/reader/entity/User.getToken ()Ljava/lang/String;
      // 13a: astore 9
      // 13c: lload 6
      // 13e: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxLong (J)Ljava/lang/Long;
      // 141: astore 10
      // 143: bipush 0
      // 144: istore 11
      // 146: aload 8
      // 148: aload 9
      // 14a: aload 10
      // 14c: invokeinterface java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 151: pop
      // 152: aload 5
      // 154: invokeinterface java/util/Map.values ()Ljava/util/Collection; 1
      // 159: checkcast java/lang/Iterable
      // 15c: new com/htmake/reader/api/controller/BaseController$saveUserSession$2
      // 15f: dup
      // 160: aload 2
      // 161: invokespecial com/htmake/reader/api/controller/BaseController$saveUserSession$2.<init> (Lcom/htmake/reader/entity/User;)V
      // 164: checkcast kotlin/jvm/functions/Function1
      // 167: invokestatic kotlin/collections/CollectionsKt.removeAll (Ljava/lang/Iterable;Lkotlin/jvm/functions/Function1;)Z
      // 16a: pop
      // 16b: aload 2
      // 16c: aload 5
      // 16e: invokevirtual com/htmake/reader/entity/User.setToken_map (Ljava/util/Map;)V
      // 171: bipush 0
      // 172: istore 6
      // 174: new java/util/LinkedHashMap
      // 177: dup
      // 178: invokespecial java/util/LinkedHashMap.<init> ()V
      // 17b: checkcast java/util/Map
      // 17e: astore 5
      // 180: bipush 2
      // 181: anewarray 150
      // 184: astore 7
      // 186: aload 7
      // 188: bipush 0
      // 189: ldc "data"
      // 18b: aastore
      // 18c: aload 7
      // 18e: bipush 1
      // 18f: ldc "users"
      // 191: aastore
      // 192: aload 7
      // 194: aconst_null
      // 195: bipush 2
      // 196: aconst_null
      // 197: invokestatic com/htmake/reader/utils/ExtKt.getStorage$default ([Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
      // 19a: invokestatic com/htmake/reader/utils/ExtKt.asJsonObject (Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;
      // 19d: astore 6
      // 19f: aload 6
      // 1a1: ifnull 1c1
      // 1a4: aload 6
      // 1a6: invokevirtual io/vertx/core/json/JsonObject.getMap ()Ljava/util/Map;
      // 1a9: astore 7
      // 1ab: aload 7
      // 1ad: ifnonnull 1ba
      // 1b0: new java/lang/NullPointerException
      // 1b3: dup
      // 1b4: ldc "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
      // 1b6: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
      // 1b9: athrow
      // 1ba: aload 7
      // 1bc: invokestatic kotlin/jvm/internal/TypeIntrinsics.asMutableMap (Ljava/lang/Object;)Ljava/util/Map;
      // 1bf: astore 5
      // 1c1: aload 5
      // 1c3: astore 7
      // 1c5: aload 2
      // 1c6: invokevirtual com/htmake/reader/entity/User.getUsername ()Ljava/lang/String;
      // 1c9: astore 8
      // 1cb: aload 2
      // 1cc: invokestatic com/htmake/reader/utils/ExtKt.toMap (Ljava/lang/Object;)Ljava/util/Map;
      // 1cf: astore 9
      // 1d1: bipush 0
      // 1d2: istore 10
      // 1d4: aload 7
      // 1d6: aload 8
      // 1d8: aload 9
      // 1da: invokeinterface java/util/Map.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 1df: pop
      // 1e0: bipush 2
      // 1e1: anewarray 150
      // 1e4: astore 7
      // 1e6: aload 7
      // 1e8: bipush 0
      // 1e9: ldc "data"
      // 1eb: aastore
      // 1ec: aload 7
      // 1ee: bipush 1
      // 1ef: ldc "users"
      // 1f1: aastore
      // 1f2: aload 7
      // 1f4: aload 5
      // 1f6: invokestatic io/vertx/core/json/Json.encode (Ljava/lang/Object;)Ljava/lang/String;
      // 1f9: astore 7
      // 1fb: aload 7
      // 1fd: ldc_w "encode(userMap)"
      // 200: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 203: aload 7
      // 205: bipush 0
      // 206: aconst_null
      // 207: bipush 12
      // 209: aconst_null
      // 20a: invokestatic com/htmake/reader/utils/ExtKt.saveStorage$default ([Ljava/lang/String;Ljava/lang/Object;ZLjava/lang/String;ILjava/lang/Object;)V
      // 20d: aload 0
      // 20e: aload 2
      // 20f: invokevirtual com/htmake/reader/api/controller/BaseController.formatUser (Ljava/lang/Object;)Ljava/util/Map;
      // 212: astore 7
      // 214: aload 1
      // 215: invokeinterface io/vertx/ext/web/RoutingContext.session ()Lio/vertx/ext/web/Session; 1
      // 21a: ldc_w "username"
      // 21d: aload 2
      // 21e: invokevirtual com/htmake/reader/entity/User.getUsername ()Ljava/lang/String;
      // 221: invokeinterface io/vertx/ext/web/Session.put (Ljava/lang/String;Ljava/lang/Object;)Lio/vertx/ext/web/Session; 3
      // 226: pop
      // 227: aload 1
      // 228: ldc_w "username"
      // 22b: aload 2
      // 22c: invokevirtual com/htmake/reader/entity/User.getUsername ()Ljava/lang/String;
      // 22f: invokeinterface io/vertx/ext/web/RoutingContext.put (Ljava/lang/String;Ljava/lang/Object;)Lio/vertx/ext/web/RoutingContext; 3
      // 234: pop
      // 235: aload 7
      // 237: astore 8
      // 239: aload 0
      // 23a: invokevirtual com/htmake/reader/api/controller/BaseController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 23d: aconst_null
      // 23e: bipush 1
      // 23f: aconst_null
      // 240: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 243: aload 8
      // 245: areturn
      // 246: astore 5
      // 248: aload 0
      // 249: invokevirtual com/htmake/reader/api/controller/BaseController.getUserMutex ()Lkotlinx/coroutines/sync/Mutex;
      // 24c: aconst_null
      // 24d: bipush 1
      // 24e: aconst_null
      // 24f: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 252: aload 5
      // 254: athrow
      // 255: new java/lang/IllegalStateException
      // 258: dup
      // 259: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 25c: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 25f: athrow
   }

   public suspend fun checkAuth(context: RoutingContext): Boolean {
      var `$continuation`: Continuation;
      label137: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label137;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            int I$0;
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
               return this.this$0.checkAuth(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var20: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var existedUser: User;
      var isLogin: Boolean;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            if (!this.getAppConfig().getSecure()) {
               return Boxing.boxBoolean(true);
            }

            val userInfo: java.lang.String = context.session().get("username");
            val var21: User = this.getUserInfoClass(if (userInfo == null) "" else userInfo);
            if (var21 != null) {
               context.put("username", var21.getUsername());
               context.put("userInfo", var21);
               return Boxing.boxBoolean(true);
            }

            val userMapJson: java.util.List = context.queryParam("accessToken");
            val userMap: java.lang.String = CollectionsKt.firstOrNull(userMapJson);
            val accessToken: java.lang.String = if (userMap == null) "" else userMap;
            if ((if (userMap == null) "" else userMap).length() <= 0) {
               return Boxing.boxBoolean(false);
            }

            var var23: java.util.Map = new LinkedHashMap();
            val var26: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var26 != null) {
               val _username: java.util.Map = var26.getMap();
               val var27: java.util.Map = if (TypeIntrinsics.isMutableMap(_username)) _username else null;
               var23 = if (var27 == null) new LinkedHashMap() else var27;
            }

            val var28: java.util.List = StringsKt.split$default(accessToken, new java.lang.String[]{":"}, false, 2, 2, null);
            if (var28.size() < 2) {
               return Boxing.boxBoolean(false);
            }

            val var31: java.lang.String = var28.get(0) as java.lang.String;
            val token: java.lang.String = var28.get(1) as java.lang.String;
            val var32: java.util.Map = var23.getOrDefault(var31, null) as java.util.Map;
            val var42: User = existedUser = if (var32 == null)
               null
               else
               ExtKt.getGson()
                  .fromJson(
                     if (var32 is java.lang.String) var32 as java.lang.String else ExtKt.getGson().toJson(var32),
                     new BaseController$checkAuth$$inlined$toDataClass$1().getType()
                  );
            if (var42 == null) {
               return Boxing.boxBoolean(false);
            }

            if (token.length() <= 0) {
               return Boxing.boxBoolean(false);
            }

            isLogin = false;
            if (var42.getToken().length() > 0 && var42.getToken() == token) {
               isLogin = (boolean)1;
            }

            if (isLogin == 0 && var42.getToken_map() != null) {
               val var38: java.util.Map = var42.getToken_map();
               val var35: java.util.Map = if (TypeIntrinsics.isMutableMap(var38)) var38 else null;
               if (var35 != null && var35.containsKey(token)) {
                  if (var35.getOrDefault(token, Boxing.boxLong(0L)).longValue() > System.currentTimeMillis()) {
                     isLogin = (boolean)1;
                     var35.put(token, Boxing.boxLong(System.currentTimeMillis() + (long)(this.loginExpireDays * 86400 * 1000)));
                  } else {
                     var35.remove(token);
                  }

                  var42.setToken_map(var35);
               }
            }

            if (isLogin == 0) {
               return Boxing.boxBoolean(isLogin != 0);
            }

            `$continuation`.L$0 = context;
            `$continuation`.L$1 = var42;
            `$continuation`.I$0 = isLogin;
            `$continuation`.label = 1;
            if (this.saveUserSession(context, var42, false, `$continuation`) === var20) {
               return var20;
            }
            break;
         case 1:
            isLogin = (boolean)`$continuation`.I$0;
            existedUser = `$continuation`.L$1 as User;
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      context.put("username", existedUser.getUsername());
      context.put("userInfo", existedUser);
      return Boxing.boxBoolean(isLogin != 0);
   }

   public fun checkManagerAuth(context: RoutingContext): Boolean {
      if (!this.appConfig.getSecure()) {
         return true;
      } else if (this.appConfig.getSecureKey().length() == 0) {
         return true;
      } else {
         var var4: java.util.List = context.queryParam("secureKey");
         var var8: java.lang.String = CollectionsKt.firstOrNull(var4);
         if (!((if (var8 == null) "" else var8) == this.appConfig.getSecureKey())) {
            return false;
         } else {
            var4 = context.queryParam("userNS");
            var8 = CollectionsKt.firstOrNull(var4);
            if (var8 != null && var8.length() != 0) {
               context.put("userNameSpace", var8);
            } else {
               context.remove("userNameSpace");
            }

            return true;
         }
      }
   }

   public fun getUserNameSpace(context: RoutingContext): String {
      if (!this.appConfig.getSecure()) {
         return "default";
      } else {
         this.checkManagerAuth(context);
         val userNS: java.lang.String = context.get("userNameSpace");
         if (userNS != null && userNS.length() != 0) {
            return userNS;
         } else {
            val var6: java.lang.String = context.get("username");
            return var6 ?: "default";
         }
      }
   }

   public fun getUserStorage(context: Any, vararg path: String): String? {
      var userNameSpace: java.lang.String = "";
      if (context is RoutingContext) {
         userNameSpace = this.getUserNameSpace(context as RoutingContext);
      } else if (context is java.lang.String) {
         userNameSpace = context as java.lang.String;
      }

      if (userNameSpace.length() == 0) {
         val var7: SpreadBuilder = new SpreadBuilder(2);
         var7.add("data");
         var7.addSpread(path);
         return ExtKt.getStorage$default(var7.toArray(new java.lang.String[var7.size()]) as Array<java.lang.String>, null, 2, null);
      } else {
         val var6: SpreadBuilder = new SpreadBuilder(3);
         var6.add("data");
         var6.add(userNameSpace);
         var6.addSpread(path);
         return ExtKt.getStorage$default(var6.toArray(new java.lang.String[var6.size()]) as Array<java.lang.String>, null, 2, null);
      }
   }

   public fun saveUserStorage(context: Any, path: String, value: Any) {
      var userNameSpace: java.lang.String = "";
      if (context is RoutingContext) {
         userNameSpace = this.getUserNameSpace(context as RoutingContext);
      } else if (context is java.lang.String) {
         userNameSpace = context as java.lang.String;
      }

      if (userNameSpace.length() == 0) {
         ExtKt.saveStorage$default(new java.lang.String[]{"data", path}, value, false, null, 12, null);
      } else {
         ExtKt.saveStorage$default(new java.lang.String[]{"data", userNameSpace, path}, value, false, null, 12, null);
      }
   }

   public fun getUserInfoClass(username: String): User? {
      val var3: java.util.Map = this.getUserInfoMap(username);
      return if (var3 == null)
         null
         else
         ExtKt.getGson()
            .fromJson(
               if (var3 is java.lang.String) var3 as java.lang.String else ExtKt.getGson().toJson(var3),
               new BaseController$getUserInfoClass$$inlined$toDataClass$1().getType()
            );
   }

   public fun getUserInfoMap(username: String): Map<String, Any>? {
      if (username.length() == 0) {
         return null;
      } else {
         var var7: java.util.Map = new LinkedHashMap();
         val var9: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
         if (var9 != null) {
            val var10: java.util.Map = var9.getMap();
            if (var10 == null) {
               throw new NullPointerException(
                  "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
               );
            }

            var7 = TypeIntrinsics.asMutableMap(var10);
         }

         if (var7 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
         } else {
            return var7.getOrDefault(username, null) as MutableMap<java.lang.String, Any>;
         }
      }
   }

   public fun formatUser(userInfo: Any): MutableMap<String, Any> {
      label26: {
         var user: User = null;
         if (userInfo !is User) {
            val userMap: java.util.Map = userInfo as? java.util.Map;
            if ((userInfo as? java.util.Map) != null) {
               user = ExtKt.getGson()
                  .fromJson(
                     if (userMap is java.lang.String) userMap as java.lang.String else ExtKt.getGson().toJson(userMap),
                     new BaseController$formatUser$$inlined$toDataClass$1().getType()
                  );
            }
         } else {
            user = userInfo as User;
         }

         return (java.util.Map<java.lang.String, Object>)(if (user == null)
            new LinkedHashMap<>()
            else
            MapsKt.mutableMapOf(
               new Pair[]{
                  TuplesKt.to("username", user.getUsername()),
                  TuplesKt.to("lastLoginAt", user.getLast_login_at()),
                  TuplesKt.to("accessToken", "${user.getUsername()}:${user.getToken()}"),
                  TuplesKt.to("enableWebdav", user.getEnable_webdav()),
                  TuplesKt.to("enableLocalStore", user.getEnable_local_store()),
                  TuplesKt.to("enableBookSource", user.getEnable_book_source()),
                  TuplesKt.to("enableRssSource", user.getEnable_rss_source()),
                  TuplesKt.to("bookSourceLimit", user.getBook_source_limit()),
                  TuplesKt.to("bookLimit", user.getBook_limit()),
                  TuplesKt.to("createdAt", user.getCreated_at())
               }
            ));
      }
   }

   public fun getUserWebdavHome(context: Any): String {
      var prefix: java.lang.String = ExtKt.getWorkDir("storage", "data");
      var var7: java.lang.String = "";
      if (context is RoutingContext) {
         var7 = this.getUserNameSpace(context as RoutingContext);
      } else if (context is java.lang.String) {
         var7 = context as java.lang.String;
      }

      if (var7.length() > 0) {
         prefix = "$prefix${File.separator}$var7";
      }

      prefix = "$prefix${File.separator}webdav";
      val var8: File = new File(prefix);
      if (!var8.exists()) {
         var8.mkdirs();
      }

      return prefix;
   }

   public fun getFileExt(url: String, defaultExt: String = ""): String {
      return FileUtils.INSTANCE.getFileExtetion(url, defaultExt);
   }

   public suspend fun limitConcurrent(concurrentCount: Int, startIndex: Int, endIndex: Int, handler: (CoroutineScope, Int, Continuation<Any>) -> Any?) {
      val var10000: Any = this.limitConcurrent(concurrentCount, startIndex, endIndex, handler, <unrepresentable>.INSTANCE, `$completion`);
      return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
   }

   public suspend fun limitConcurrent(
      concurrentCount: Int,
      startIndex: Int,
      endIndex: Int,
      handler: (CoroutineScope, Int, Continuation<Any>) -> Any?,
      needContinue: (ArrayList<Any>, Int) -> Boolean
   ) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.RuntimeException: parsing failure!
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:211)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 6
      // 002: instanceof com/htmake/reader/api/controller/BaseController$limitConcurrent$3
      // 005: ifeq 029
      // 008: aload 6
      // 00a: checkcast com/htmake/reader/api/controller/BaseController$limitConcurrent$3
      // 00d: astore 26
      // 00f: aload 26
      // 011: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 26
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BaseController$limitConcurrent$3
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 6
      // 030: invokespecial com/htmake/reader/api/controller/BaseController$limitConcurrent$3.<init> (Lcom/htmake/reader/api/controller/BaseController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 26
      // 035: aload 26
      // 037: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.result Ljava/lang/Object;
      // 03a: astore 25
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 27
      // 041: aload 26
      // 043: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.label I
      // 046: tableswitch 913 0 2 26 269 755
      // 060: aload 25
      // 062: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 065: iload 2
      // 066: istore 7
      // 068: bipush 0
      // 069: istore 8
      // 06b: bipush 0
      // 06c: istore 9
      // 06e: invokestatic java/lang/System.currentTimeMillis ()J
      // 071: lstore 10
      // 073: lconst_0
      // 074: lstore 12
      // 076: bipush 0
      // 077: istore 15
      // 079: new java/util/ArrayList
      // 07c: dup
      // 07d: invokespecial java/util/ArrayList.<init> ()V
      // 080: astore 14
      // 082: nop
      // 083: aload 14
      // 085: invokevirtual java/util/ArrayList.size ()I
      // 088: istore 15
      // 08a: iload 15
      // 08c: iload 1
      // 08d: if_icmpge 0d7
      // 090: iload 7
      // 092: istore 16
      // 094: iload 16
      // 096: iload 3
      // 097: if_icmpge 0d7
      // 09a: iload 16
      // 09c: istore 17
      // 09e: iinc 16 1
      // 0a1: iinc 15 1
      // 0a4: aload 14
      // 0a6: aload 0
      // 0a7: checkcast kotlinx/coroutines/CoroutineScope
      // 0aa: aconst_null
      // 0ab: aconst_null
      // 0ac: new com/htmake/reader/api/controller/BaseController$limitConcurrent$4
      // 0af: dup
      // 0b0: aload 4
      // 0b2: iload 17
      // 0b4: aconst_null
      // 0b5: invokespecial com/htmake/reader/api/controller/BaseController$limitConcurrent$4.<init> (Lkotlin/jvm/functions/Function3;ILkotlin/coroutines/Continuation;)V
      // 0b8: checkcast kotlin/jvm/functions/Function2
      // 0bb: bipush 3
      // 0bc: aconst_null
      // 0bd: invokestatic kotlinx/coroutines/BuildersKt.async$default (Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function2;ILjava/lang/Object;)Lkotlinx/coroutines/Deferred;
      // 0c0: invokevirtual java/util/ArrayList.add (Ljava/lang/Object;)Z
      // 0c3: pop
      // 0c4: iload 17
      // 0c6: istore 7
      // 0c8: iload 15
      // 0ca: iload 1
      // 0cb: if_icmplt 0d1
      // 0ce: goto 0d7
      // 0d1: iload 16
      // 0d3: iload 3
      // 0d4: if_icmplt 09a
      // 0d7: bipush 0
      // 0d8: istore 17
      // 0da: new java/util/ArrayList
      // 0dd: dup
      // 0de: invokespecial java/util/ArrayList.<init> ()V
      // 0e1: astore 16
      // 0e3: aload 16
      // 0e5: invokevirtual java/util/ArrayList.size ()I
      // 0e8: ifgt 25a
      // 0eb: ldc2_w 10
      // 0ee: aload 26
      // 0f0: aload 26
      // 0f2: aload 0
      // 0f3: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$0 Ljava/lang/Object;
      // 0f6: aload 26
      // 0f8: aload 4
      // 0fa: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$1 Ljava/lang/Object;
      // 0fd: aload 26
      // 0ff: aload 5
      // 101: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$2 Ljava/lang/Object;
      // 104: aload 26
      // 106: aload 14
      // 108: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$3 Ljava/lang/Object;
      // 10b: aload 26
      // 10d: aload 16
      // 10f: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$4 Ljava/lang/Object;
      // 112: aload 26
      // 114: iload 1
      // 115: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$0 I
      // 118: aload 26
      // 11a: iload 3
      // 11b: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$1 I
      // 11e: aload 26
      // 120: iload 7
      // 122: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$2 I
      // 125: aload 26
      // 127: iload 8
      // 129: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$3 I
      // 12c: aload 26
      // 12e: iload 9
      // 130: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$4 I
      // 133: aload 26
      // 135: lload 10
      // 137: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.J$0 J
      // 13a: aload 26
      // 13c: iload 15
      // 13e: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$5 I
      // 141: aload 26
      // 143: bipush 1
      // 144: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.label I
      // 147: invokestatic kotlinx/coroutines/DelayKt.delay (JLkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 14a: dup
      // 14b: aload 27
      // 14d: if_acmpne 1ba
      // 150: aload 27
      // 152: areturn
      // 153: aload 26
      // 155: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$5 I
      // 158: istore 15
      // 15a: aload 26
      // 15c: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.J$0 J
      // 15f: lstore 10
      // 161: aload 26
      // 163: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$4 I
      // 166: istore 9
      // 168: aload 26
      // 16a: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$3 I
      // 16d: istore 8
      // 16f: aload 26
      // 171: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$2 I
      // 174: istore 7
      // 176: aload 26
      // 178: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$1 I
      // 17b: istore 3
      // 17c: aload 26
      // 17e: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$0 I
      // 181: istore 1
      // 182: aload 26
      // 184: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$4 Ljava/lang/Object;
      // 187: checkcast java/util/ArrayList
      // 18a: astore 16
      // 18c: aload 26
      // 18e: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$3 Ljava/lang/Object;
      // 191: checkcast java/util/ArrayList
      // 194: astore 14
      // 196: aload 26
      // 198: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$2 Ljava/lang/Object;
      // 19b: checkcast kotlin/jvm/functions/Function2
      // 19e: astore 5
      // 1a0: aload 26
      // 1a2: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$1 Ljava/lang/Object;
      // 1a5: checkcast kotlin/jvm/functions/Function3
      // 1a8: astore 4
      // 1aa: aload 26
      // 1ac: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$0 Ljava/lang/Object;
      // 1af: checkcast com/htmake/reader/api/controller/BaseController
      // 1b2: astore 0
      // 1b3: aload 25
      // 1b5: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 1b8: aload 25
      // 1ba: pop
      // 1bb: bipush 0
      // 1bc: istore 18
      // 1be: new java/util/ArrayList
      // 1c1: dup
      // 1c2: invokespecial java/util/ArrayList.<init> ()V
      // 1c5: astore 17
      // 1c7: bipush 0
      // 1c8: istore 18
      // 1ca: aload 14
      // 1cc: invokevirtual java/util/ArrayList.size ()I
      // 1cf: istore 19
      // 1d1: iload 18
      // 1d3: iload 19
      // 1d5: if_icmpge 247
      // 1d8: iload 18
      // 1da: istore 20
      // 1dc: iinc 18 1
      // 1df: nop
      // 1e0: aload 14
      // 1e2: iload 20
      // 1e4: invokevirtual java/util/ArrayList.get (I)Ljava/lang/Object;
      // 1e7: astore 22
      // 1e9: aload 22
      // 1eb: ldc_w "deferredList[i]"
      // 1ee: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 1f1: aload 22
      // 1f3: checkcast kotlinx/coroutines/Deferred
      // 1f6: astore 21
      // 1f8: aload 21
      // 1fa: invokeinterface kotlinx/coroutines/Deferred.isCompleted ()Z 1
      // 1ff: ifeq 21c
      // 202: iload 9
      // 204: istore 22
      // 206: iload 22
      // 208: bipush 1
      // 209: iadd
      // 20a: istore 9
      // 20c: aload 16
      // 20e: aload 21
      // 210: invokeinterface kotlinx/coroutines/Deferred.getCompleted ()Ljava/lang/Object; 1
      // 215: invokevirtual java/util/ArrayList.add (Ljava/lang/Object;)Z
      // 218: pop
      // 219: goto 240
      // 21c: aload 21
      // 21e: invokeinterface kotlinx/coroutines/Deferred.isCancelled ()Z 1
      // 223: ifne 231
      // 226: aload 17
      // 228: aload 21
      // 22a: invokevirtual java/util/ArrayList.add (Ljava/lang/Object;)Z
      // 22d: pop
      // 22e: goto 240
      // 231: iload 9
      // 233: istore 22
      // 235: iload 22
      // 237: bipush 1
      // 238: iadd
      // 239: istore 9
      // 23b: goto 240
      // 23e: astore 21
      // 240: iload 18
      // 242: iload 19
      // 244: if_icmplt 1d8
      // 247: aload 14
      // 249: invokevirtual java/util/ArrayList.clear ()V
      // 24c: aload 14
      // 24e: aload 17
      // 250: checkcast java/util/Collection
      // 253: invokevirtual java/util/ArrayList.addAll (Ljava/util/Collection;)Z
      // 256: pop
      // 257: goto 0e3
      // 25a: iload 9
      // 25c: iload 1
      // 25d: idiv
      // 25e: iload 8
      // 260: if_icmple 2bd
      // 263: iload 9
      // 265: iload 1
      // 266: idiv
      // 267: istore 8
      // 269: invokestatic java/lang/System.currentTimeMillis ()J
      // 26c: lload 10
      // 26e: lsub
      // 26f: lstore 12
      // 271: invokestatic com/htmake/reader/api/controller/BaseControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 274: ldc_w "Loop: {} concurrentCount: {} lastIndex: {} endIndex: {} costTime: {} ms deferredList size: {}"
      // 277: bipush 6
      // 279: anewarray 4
      // 27c: astore 17
      // 27e: aload 17
      // 280: bipush 0
      // 281: iload 8
      // 283: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 286: aastore
      // 287: aload 17
      // 289: bipush 1
      // 28a: iload 15
      // 28c: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 28f: aastore
      // 290: aload 17
      // 292: bipush 2
      // 293: iload 7
      // 295: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 298: aastore
      // 299: aload 17
      // 29b: bipush 3
      // 29c: iload 3
      // 29d: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 2a0: aastore
      // 2a1: aload 17
      // 2a3: bipush 4
      // 2a4: lload 12
      // 2a6: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxLong (J)Ljava/lang/Long;
      // 2a9: aastore
      // 2aa: aload 17
      // 2ac: bipush 5
      // 2ad: aload 14
      // 2af: invokevirtual java/util/ArrayList.size ()I
      // 2b2: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 2b5: aastore
      // 2b6: aload 17
      // 2b8: invokeinterface mu/KLogger.info (Ljava/lang/String;[Ljava/lang/Object;)V 3
      // 2bd: iload 7
      // 2bf: iload 3
      // 2c0: bipush 1
      // 2c1: isub
      // 2c2: if_icmplt 3ab
      // 2c5: bipush 0
      // 2c6: istore 17
      // 2c8: aload 14
      // 2ca: invokevirtual java/util/ArrayList.size ()I
      // 2cd: istore 18
      // 2cf: iload 17
      // 2d1: iload 18
      // 2d3: if_icmpge 394
      // 2d6: iload 17
      // 2d8: istore 19
      // 2da: iinc 17 1
      // 2dd: nop
      // 2de: aload 16
      // 2e0: astore 23
      // 2e2: aload 14
      // 2e4: iload 19
      // 2e6: invokevirtual java/util/ArrayList.get (I)Ljava/lang/Object;
      // 2e9: checkcast kotlinx/coroutines/Deferred
      // 2ec: aload 26
      // 2ee: aload 26
      // 2f0: aload 5
      // 2f2: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$0 Ljava/lang/Object;
      // 2f5: aload 26
      // 2f7: aload 14
      // 2f9: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$1 Ljava/lang/Object;
      // 2fc: aload 26
      // 2fe: aload 16
      // 300: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$2 Ljava/lang/Object;
      // 303: aload 26
      // 305: aload 23
      // 307: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$3 Ljava/lang/Object;
      // 30a: aload 26
      // 30c: aconst_null
      // 30d: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$4 Ljava/lang/Object;
      // 310: aload 26
      // 312: iload 8
      // 314: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$0 I
      // 317: aload 26
      // 319: iload 17
      // 31b: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$1 I
      // 31e: aload 26
      // 320: iload 18
      // 322: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$2 I
      // 325: aload 26
      // 327: bipush 2
      // 328: putfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.label I
      // 32b: invokeinterface kotlinx/coroutines/Deferred.await (Lkotlin/coroutines/Continuation;)Ljava/lang/Object; 2
      // 330: dup
      // 331: aload 27
      // 333: if_acmpne 37e
      // 336: aload 27
      // 338: areturn
      // 339: aload 26
      // 33b: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$2 I
      // 33e: istore 18
      // 340: aload 26
      // 342: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$1 I
      // 345: istore 17
      // 347: aload 26
      // 349: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.I$0 I
      // 34c: istore 8
      // 34e: aload 26
      // 350: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$3 Ljava/lang/Object;
      // 353: checkcast java/util/ArrayList
      // 356: astore 23
      // 358: aload 26
      // 35a: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$2 Ljava/lang/Object;
      // 35d: checkcast java/util/ArrayList
      // 360: astore 16
      // 362: aload 26
      // 364: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$1 Ljava/lang/Object;
      // 367: checkcast java/util/ArrayList
      // 36a: astore 14
      // 36c: aload 26
      // 36e: getfield com/htmake/reader/api/controller/BaseController$limitConcurrent$3.L$0 Ljava/lang/Object;
      // 371: checkcast kotlin/jvm/functions/Function2
      // 374: astore 5
      // 376: nop
      // 377: aload 25
      // 379: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 37c: aload 25
      // 37e: astore 24
      // 380: aload 23
      // 382: aload 24
      // 384: invokevirtual java/util/ArrayList.add (Ljava/lang/Object;)Z
      // 387: pop
      // 388: goto 38d
      // 38b: astore 20
      // 38d: iload 17
      // 38f: iload 18
      // 391: if_icmplt 2d6
      // 394: aload 14
      // 396: invokevirtual java/util/ArrayList.clear ()V
      // 399: aload 5
      // 39b: aload 16
      // 39d: iload 8
      // 39f: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 3a2: invokeinterface kotlin/jvm/functions/Function2.invoke (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 3a7: pop
      // 3a8: goto 3d3
      // 3ab: aload 16
      // 3ad: invokevirtual java/util/ArrayList.size ()I
      // 3b0: ifle 3cd
      // 3b3: aload 5
      // 3b5: aload 16
      // 3b7: iload 8
      // 3b9: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 3bc: invokeinterface kotlin/jvm/functions/Function2.invoke (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 3
      // 3c1: checkcast java/lang/Boolean
      // 3c4: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 3c7: ifne 3cd
      // 3ca: goto 3d3
      // 3cd: iinc 7 1
      // 3d0: goto 082
      // 3d3: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 3d6: areturn
      // 3d7: new java/lang/IllegalStateException
      // 3da: dup
      // 3db: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 3de: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 3e1: athrow
   }
}
