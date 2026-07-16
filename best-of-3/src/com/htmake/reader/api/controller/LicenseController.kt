package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.ActiveLicense
import com.htmake.reader.entity.License
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.VertExtKt
import io.legado.app.utils.ACache
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.net.SocketAddress
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.VertxCoroutineKt
import java.security.KeyFactory
import java.security.KeyPair
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.Result.Companion
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.slf4j.MDCContext
import mu.KLogger
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class LicenseController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
   public final val backupFileNames: Array<String> by LazyKt.lazy(SyntheticFunction0.INSTANCE)
      public final get() {
         return this.backupFileNames$delegate.getValue() as Array<java.lang.String>;
      }

   private final var privateKeyContent: String = ""
   private final var tryCodeCache: ACache = ACache.Companion.get("tryCodeCache", 2000000L, 10000)

   private final val webClient: WebClient by LazyKt.lazy(SyntheticFunction0.INSTANCE)
      private final get() {
         val var1: Any = this.webClient$delegate.getValue();
         return var1 as WebClient;
      }

   public suspend fun getLicense(context: RoutingContext): ReturnData {
      return ReturnData.setData$default(
         new ReturnData(), MapsKt.mapOf(TuplesKt.to("license", ExtKt.getInstalledLicense$default(false, 1, null))), null, 2, null
      );
   }

   public suspend fun importLicense(context: RoutingContext) {
      var `$continuation`: Continuation;
      label42: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
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
               return this.this$0.importLicense(null, this);
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
            this = `$continuation`.L$0 as LicenseController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
         return Unit.INSTANCE;
      } else if (!this.checkManagerAuth(context)) {
         VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码"));
         return Unit.INSTANCE;
      } else {
         val exceptionHandler: java.lang.String = context.getBodyAsJson().getString("content");
         val content: java.lang.String = if (exceptionHandler == null) "" else exceptionHandler;
         if ((if (exceptionHandler == null) "" else exceptionHandler).length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("请输入密钥"));
            return Unit.INSTANCE;
         } else {
            val var12: CoroutineExceptionHandler = new LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1(
               CoroutineExceptionHandler.Key, context, returnData
            );
            val var14: ObjectRef = new ObjectRef();
            var14.element = (T)"https://r.htmake.com/reader3/activateLicense";
            BuildersKt.launch$default(
               this,
               new MDCContext(null, 1, null).plus(Dispatchers.getIO()).plus(var12),
               null,
               (
                  new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, var14, content, context, returnData, null) {
                     int label;

                     {
                        super(2, `$completionx`);
                        this.this$0 = `$receiver`;
                        this.$checkUrl = `$checkUrl`;
                        this.$content = `$content`;
                        this.$context = `$context`;
                        this.$returnData = `$returnData`;
                     }

                     @Nullable
                     @Override
                     public final Object invokeSuspend(@NotNull Object $result) {
                        val var22: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        var var10000: Any;
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              val var24: CoroutineScope = this.L$0 as CoroutineScope;
                              var10000 = (
                                 new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>(this.this$0, this.$checkUrl, this.$content) {
                                    {
                                       super(1);
                                       this.this$0 = `$receiver`;
                                       this.$checkUrl = `$checkUrl`;
                                       this.$content = `$content`;
                                    }

                                    public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                                       LicenseController.access$getWebClient(this.this$0)
                                          .postAbs(this.$checkUrl.element)
                                          .timeout(5000L)
                                          .sendJsonObject(new JsonObject(MapsKt.mapOf(TuplesKt.to("content", this.$content))), handler);
                                    }
                                 }
                              ) as Function1;
                              val var10001: Continuation = this;
                              this.L$0 = var24;
                              this.label = 1;
                              var10000 = (java.lang.Throwable)VertxCoroutineKt.awaitResult((Function1)var10000, var10001);
                              if (var10000 === var22) {
                                 return var22;
                              }
                              break;
                           case 1:
                              val `$this$launch`: CoroutineScope = this.L$0 as CoroutineScope;
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = (java.lang.Throwable)`$result`;
                              break;
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        val result: HttpResponse = var10000 as HttpResponse;
                        var var5: RoutingContext = this.$context;

                        var var7: Any;
                        try {
                           var7 = Result.Companion;
                           if (result == null) {
                              var10000 = null;
                           } else {
                              val var11: JsonObject = result.bodyAsJsonObject();
                              if (var11 == null) {
                                 var10000 = null;
                              } else {
                                 if (!var11.getBoolean("isSuccess")) {
                                    throw new Exception(var11.getString("errorMsg"));
                                 }

                                 val var12: JsonObject = var11.getJsonObject("data");
                                 if (var12 == null) {
                                    var10000 = null;
                                 } else {
                                    val var43: java.lang.String = var12.getString("result");
                                    if (var43 == null) {
                                       var10000 = null;
                                    } else {
                                       val license: License = ExtKt.decryptToLicense(var43);
                                       if (license == null) {
                                          throw new Exception("密钥错误");
                                       }

                                       val var21: java.lang.String = var5.request().host();
                                       if (!license.validHost(var21)) {
                                          throw new Exception("密钥授权域名错误");
                                       }

                                       var10000 = new Pair<>(var43, license);
                                    }
                                 }
                              }
                           }

                           var7 = Result.constructor-impl(var10000);
                        } catch (var23: java.lang.Throwable) {
                           val it: Companion = Result.Companion;
                           var7 = Result.constructor-impl(ResultKt.createFailure(var23));
                        }

                        var5 = this.$context;
                        var var27: ReturnData = this.$returnData;
                        if (Result.isSuccess-impl(var7)) {
                           val var37: Pair = var7 as Pair;
                           if (var7 as Pair != null) {
                              ExtKt.saveStorage$default(new java.lang.String[]{"data", "license"}, var37.getFirst(), false, ".key", 4, null);
                              VertExtKt.success(var5, ReturnData.setData$default(var27, MapsKt.mapOf(TuplesKt.to("license", var37.getSecond())), null, 2, null));
                           } else {
                              VertExtKt.success(var5, var27.setErrorMsg("密钥激活失败"));
                           }
                        }

                        var5 = this.$context;
                        var27 = this.$returnData;
                        var10000 = Result.exceptionOrNull-impl(var7);
                        if (var10000 != null) {
                           LicenseControllerKt.access$getLogger$p().info("import license error: {}", var10000.getMessage());
                           val var45: java.lang.String = var10000.getMessage();
                           VertExtKt.success(var5, var27.setErrorMsg(if (var45 == null) "密钥激活错误" else var45));
                        }

                        return Unit.INSTANCE;
                     }

                     @NotNull
                     @Override
                     public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                        val var3: Function2 = new <anonymous constructor>(
                           this.this$0, this.$checkUrl, this.$content, this.$context, this.$returnData, `$completion`
                        );
                        var3.L$0 = value;
                        return var3 as Continuation<Unit>;
                     }

                     @Nullable
                     public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                        return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
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

   public suspend fun generateKeys(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val keyPair: KeyPair = EncoderUtils.INSTANCE.generateKeys();
      return ReturnData.setData$default(
         returnData,
         MapsKt.mapOf(
            new Pair[]{
               TuplesKt.to("publicKey", Base64.encodeToString(keyPair.getPublic().getEncoded(), 2)),
               TuplesKt.to("privateKey", Base64.encodeToString(keyPair.getPrivate().getEncoded(), 2))
            }
         ),
         null,
         2,
         null
      );
   }

   public suspend fun generateLicense(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val var20: java.lang.String;
      val var21: Long;
      val var22: Int;
      val var23: Boolean;
      val var24: Long;
      val var25: Int;
      val var26: java.lang.String;
      val var27: java.lang.String;
      val var28: java.lang.String;
      if (context.request().method() === HttpMethod.POST) {
         var license: java.lang.String = context.getBodyAsJson().getString("host");
         var20 = if (license == null) "" else license;
         val var29: java.lang.Long = context.getBodyAsJson().getLong("expiredAt");
         var21 = if (var29 == null) 0L else var29;
         val var30: Int = context.getBodyAsJson().getInteger("userMaxLimit");
         var22 = if (var30 == null) 15 else var30;
         val var31: java.lang.Boolean = context.getBodyAsJson().getBoolean("openApi");
         var23 = var31 != null && var31;
         val var32: java.lang.Long = context.getBodyAsJson().getLong("simpleWebExpiredAt");
         var24 = if (var32 == null) 0L else var32;
         val var33: Int = context.getBodyAsJson().getInteger("instances");
         var25 = if (var33 == null) 1 else var33;
         license = context.getBodyAsJson().getString("type");
         var26 = if (license == null) "" else license;
         license = context.getBodyAsJson().getString("key");
         var27 = if (license == null) "" else license;
         license = context.getBodyAsJson().getString("code");
         var28 = if (license == null) "" else license;
      } else {
         var licenseContent: java.util.List = context.queryParam("host");
         var var37: java.lang.String = CollectionsKt.firstOrNull(licenseContent);
         var20 = if (var37 == null) "" else var37;
         licenseContent = context.queryParam("expiredAt");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         var var10000: Long;
         if (var37 == null) {
            var10000 = 0L;
         } else {
            val var49: java.lang.Long = Boxing.boxLong(java.lang.Long.parseLong(var37));
            var10000 = if (var49 == null) 0L else var49;
         }

         var21 = var10000;
         licenseContent = context.queryParam("userMaxLimit");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         val var76: Int;
         if (var37 == null) {
            var76 = 15;
         } else {
            val var51: Int = Boxing.boxInt(Integer.parseInt(var37));
            var76 = if (var51 == null) 15 else var51;
         }

         var22 = var76;
         licenseContent = context.queryParam("openApi");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         val var77: Boolean;
         if (var37 == null) {
            var77 = false;
         } else {
            val var53: java.lang.Boolean = Boxing.boxBoolean(java.lang.Boolean.parseBoolean(var37));
            var77 = var53 != null && var53;
         }

         var23 = var77;
         licenseContent = context.queryParam("simpleWebExpiredAt");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         if (var37 == null) {
            var10000 = 0L;
         } else {
            val var55: java.lang.Long = Boxing.boxLong(java.lang.Long.parseLong(var37));
            var10000 = if (var55 == null) 0L else var55;
         }

         var24 = var10000;
         licenseContent = context.queryParam("instances");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         val var79: Int;
         if (var37 == null) {
            var79 = 1;
         } else {
            val var57: Int = Boxing.boxInt(Integer.parseInt(var37));
            var79 = if (var57 == null) 1 else var57;
         }

         var25 = var79;
         licenseContent = context.queryParam("type");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         var26 = if (var37 == null) "" else var37;
         licenseContent = context.queryParam("key");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         var27 = if (var37 == null) "" else var37;
         licenseContent = context.queryParam("code");
         var37 = CollectionsKt.firstOrNull(licenseContent);
         var28 = if (var37 == null) "" else var37;
      }

      if (var20.length() == 0) {
         return returnData.setErrorMsg("请输入域名");
      } else if (!"Pvkp7tMQJpi4kWBE".equals(var27)) {
         return returnData.setErrorMsg("参数错误");
      } else {
         val var47: License = new License(var20, var22, var21, var23, var24, var25, var26, null, null, false, null, 1408, null);
         if (var28.length() != 0) {
            var47.setCode(var28);
         }

         val var63: java.lang.String = ExtKt.jsonEncode$default(var47, false, 2, null);
         if (this.privateKeyContent.length() == 0) {
            val var66: java.lang.String = ExtKt.getStorage(new java.lang.String[]{"data", "privateKey"}, ".key");
            this.privateKeyContent = if (var66 == null) "" else var66;
         }

         val var67: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
         val var80: EncoderUtils = EncoderUtils.INSTANCE;
         return ReturnData.setData$default(
            returnData,
            MapsKt.mapOf(
               new Pair[]{TuplesKt.to("host", var20), TuplesKt.to("key", EncoderUtils.encryptSegmentByPrivateKey$default(var80, var63, var67, 0, 4, null))}
            ),
            null,
            2,
            null
         );
      }
   }

   public suspend fun isHostValid(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val var7: java.lang.String;
      if (context.request().method() === HttpMethod.POST) {
         val license: java.lang.String = context.getBodyAsJson().getString("host");
         var7 = license;
      } else {
         val var6: java.util.List = context.queryParam("host");
         val var8: java.lang.String = CollectionsKt.firstOrNull(var6);
         var7 = if (var8 == null) "" else var8;
      }

      return ReturnData.setData$default(
         returnData, MapsKt.mapOf(TuplesKt.to("isValid", Boxing.boxBoolean(ExtKt.getInstalledLicense$default(false, 1, null).validHost(var7)))), null, 2, null
      );
   }

   public suspend fun decryptLicense(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val license: java.lang.String = context.getBodyAsJson().getString("content");
      val content: java.lang.String = if (license == null) "" else license;
      if ((if (license == null) "" else license).length() == 0) {
         return returnData.setErrorMsg("请输入密钥");
      } else {
         val var9: License = ExtKt.decryptToLicense(content);
         return if (var9 == null) returnData.setErrorMsg("密钥错误") else ReturnData.setData$default(returnData, var9, null, 2, null);
      }
   }

   public suspend fun activateLicense(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val license: java.lang.String = context.getBodyAsJson().getString("content");
      val content: java.lang.String = if (license == null) "" else license;
      if ((if (license == null) "" else license).length() == 0) {
         return returnData.setErrorMsg("请输入密钥");
      } else {
         val var17: License = ExtKt.decryptToLicense(content);
         if (var17 == null) {
            return returnData.setErrorMsg("密钥错误");
         } else if (var17.getVerified()) {
            return returnData.setErrorMsg("密钥已被使用");
         } else {
            var var18: JsonArray = ExtKt.asJsonArray(ExtKt.getStorage$default(new java.lang.String[]{"data", "activeLicense"}, null, 2, null));
            if (var18 == null) {
               var18 = new JsonArray();
            }

            var var20: Int = 0;
            var ip: Int = 0;
            val activeLicense: Int = var18.size();
            if (0 < activeLicense) {
               do {
                  val licenseContent: ActiveLicense = var18.getJsonObject(ip++).mapTo(ActiveLicense.class);
                  if (licenseContent.getType().equals(var17.getType()) && licenseContent.getCode().equals(var17.getCode())) {
                     var20++;
                  }
               } while (ip < activeLicense);
            }

            if (var20 >= var17.getInstances()) {
               return returnData.setErrorMsg("密钥已超过最大使用次数");
            } else {
               var var22: java.lang.String = context.request().getHeader("X-Real-IP");
               if (var22 == null || var22.length() == 0) {
                  val var24: SocketAddress = context.request().remoteAddress();
                  val var10000: java.lang.String;
                  if (var24 == null) {
                     var10000 = "";
                  } else {
                     val var27: java.lang.String = var24.host();
                     var10000 = if (var27 == null) "" else var27;
                  }

                  var22 = var10000;
               }

               var17.setVerified(true);
               var17.setVerifyTime(Boxing.boxLong(System.currentTimeMillis()));
               val var25: java.lang.String = UUID.randomUUID().toString();
               var17.setId(var25);
               val var28: ActiveLicense = var17.toActiveLicense();
               var28.setActiveOrder(var20 + 1);
               var28.setActiveTime(System.currentTimeMillis());
               var28.setActiveIp(var22);
               var28.setActiveEmail("");
               val var30: JsonObject = JsonObject.mapFrom(var28);
               var18.add(var30);
               LicenseControllerKt.access$getLogger$p().info("activeLicenseList: {}", var18);
               ExtKt.saveStorage$default(new java.lang.String[]{"data", "activeLicense"}, var18, false, null, 12, null);
               val var32: java.lang.String = ExtKt.jsonEncode$default(var17, false, 2, null);
               if (this.privateKeyContent.length() == 0) {
                  val var34: java.lang.String = ExtKt.getStorage(new java.lang.String[]{"data", "privateKey"}, ".key");
                  this.privateKeyContent = if (var34 == null) "" else var34;
               }

               val var35: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
               val var38: EncoderUtils = EncoderUtils.INSTANCE;
               return ReturnData.setData$default(
                  returnData,
                  MapsKt.mapOf(TuplesKt.to("result", EncoderUtils.encryptSegmentByPrivateKey$default(var38, var32, var35, 0, 4, null))),
                  null,
                  2,
                  null
               );
            }
         }
      }
   }

   public suspend fun isLicenseValid(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val var13: java.lang.String;
      if (context.request().method() === HttpMethod.POST) {
         val activeLicenseList: java.lang.String = context.getBodyAsJson().getString("id");
         var13 = activeLicenseList;
      } else {
         val activeLicense: java.util.List = context.queryParam("id");
         val var14: java.lang.String = CollectionsKt.firstOrNull(activeLicense);
         var13 = if (var14 == null) "" else var14;
      }

      var var15: JsonArray = ExtKt.asJsonArray(ExtKt.getStorage$default(new java.lang.String[]{"data", "activeLicense"}, null, 2, null));
      if (var15 == null) {
         var15 = new JsonArray();
      }

      var var17: ActiveLicense = null;
      var index: Int = -1;
      var result: Int = 0;
      val ip: Int = var15.size();
      if (0 < ip) {
         do {
            val resultContent: Int = result++;
            val privateKey: ActiveLicense = var15.getJsonObject(resultContent).mapTo(ActiveLicense.class);
            if (privateKey.getId().equals(var13)) {
               var17 = privateKey;
               index = resultContent;
               break;
            }
         } while (result < ip);
      }

      val var18: java.util.Map = new LinkedHashMap();
      var var20: java.lang.String = context.request().getHeader("X-Real-IP");
      if (var20 == null || var20.length() == 0) {
         val var22: SocketAddress = context.request().remoteAddress();
         val var10000: java.lang.String;
         if (var22 == null) {
            var10000 = "";
         } else {
            val var27: java.lang.String = var22.host();
            var10000 = if (var27 == null) "" else var27;
         }

         var20 = var10000;
      }

      if (var17 == null) {
         var18.put("isValid", Boxing.boxBoolean(false));
         var18.put("errorMsg", "密钥未激活");
      } else {
         var18.put("isValid", Boxing.boxBoolean(var17.getVerified()));
         var18.put("errorMsg", var17.getErrorMsg());
         if (var17.getLastOnlineTime() != null) {
            val var33: Long = System.currentTimeMillis();
            val var10001: java.lang.Long = var17.getLastOnlineTime();
            if (var33 < var10001 + 600000 && !var20.equals(var17.getLastOnlineIp())) {
               var18.put(
                  "repeat",
                  MapsKt.mapOf(new Pair[]{TuplesKt.to("lastOnlineTime", var17.getLastOnlineTime()), TuplesKt.to("lastOnlineIp", var17.getLastOnlineIp())})
               );
            }
         }

         var17.setLastOnlineTime(Boxing.boxLong(System.currentTimeMillis()));
         var17.setLastOnlineIp(var20);
         var15.set(index, JsonObject.mapFrom(var17));
         ExtKt.saveStorage$default(new java.lang.String[]{"data", "activeLicense"}, var15, false, null, 12, null);
      }

      val var25: java.lang.String = ExtKt.jsonEncode$default(var18, false, 2, null);
      if (this.privateKeyContent.length() == 0) {
         val var29: java.lang.String = ExtKt.getStorage(new java.lang.String[]{"data", "privateKey"}, ".key");
         this.privateKeyContent = if (var29 == null) "" else var29;
      }

      val var30: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
      val var34: EncoderUtils = EncoderUtils.INSTANCE;
      return ReturnData.setData$default(
         returnData, MapsKt.mapOf(TuplesKt.to("result", EncoderUtils.encryptSegmentByPrivateKey$default(var34, var25, var30, 0, 4, null))), null, 2, null
      );
   }

   public suspend fun checkLicense(license: License) {
      val exceptionHandler: CoroutineExceptionHandler = new LicenseController$checkLicense$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key);
      val var6: ObjectRef = new ObjectRef();
      var6.element = (T)Intrinsics.stringPlus("https://r.htmake.com/reader3/isLicenseValid?id=", license.getId());
      val var10000: Job = BuildersKt.launch$default(
         this,
         new MDCContext(null, 1, null).plus(Dispatchers.getIO()).plus(exceptionHandler),
         null,
         (
            new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, var6, null) {
               int label;

               {
                  super(2, `$completionx`);
                  this.this$0 = `$receiver`;
                  this.$checkUrl = `$checkUrl`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  val var23: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  var var10000: Any;
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        val var25: CoroutineScope = this.L$0 as CoroutineScope;
                        var10000 = (new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>(this.this$0, this.$checkUrl) {
                           {
                              super(1);
                              this.this$0 = `$receiver`;
                              this.$checkUrl = `$checkUrl`;
                           }

                           public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                              LicenseController.access$getWebClient(this.this$0).getAbs(this.$checkUrl.element).timeout(5000L).send(handler);
                           }
                        }) as Function1;
                        val var10001: Continuation = this;
                        this.L$0 = var25;
                        this.label = 1;
                        var10000 = (KLogger)VertxCoroutineKt.awaitResult((Function1)var10000, var10001);
                        if (var10000 === var23) {
                           return var23;
                        }
                        break;
                     case 1:
                        val `$this$launch`: CoroutineScope = this.L$0 as CoroutineScope;
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = (KLogger)`$result`;
                        break;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  val result: HttpResponse = var10000 as HttpResponse;

                  var var6: Any;
                  try {
                     var6 = Result.Companion;
                     if (result == null) {
                        var10000 = null;
                     } else {
                        val var10: JsonObject = result.bodyAsJsonObject();
                        if (var10 == null) {
                           var10000 = null;
                        } else {
                           LicenseControllerKt.access$getLogger$p().info("isLicenseValid: {}", var10);
                           val it: JsonObject = var10.getJsonObject("data");
                           if (it == null) {
                              var10000 = null;
                           } else {
                              val var39: java.lang.String = it.getString("result");
                              if (var39 == null) {
                                 var10000 = null;
                              } else {
                                 val var41: JsonObject = new JsonObject(ExtKt.decryptData(var39));
                                 val var21: java.lang.Boolean = var41.getBoolean("isValid");
                                 val isValid: Boolean = var21 == null || var21;
                                 ExtKt.setLicenseValid(isValid);
                                 if (!isValid) {
                                    var10000 = LicenseControllerKt.access$getLogger$p();
                                    val var47: java.lang.String = var41.getString("errorMsg");
                                    var10000.info("密钥错误：{}", if (var47 == null) "" else var47);
                                 }

                                 val itx: JsonObject = var41.getJsonObject("repeat");
                                 if (itx == null) {
                                    var10000 = null;
                                 } else {
                                    var10000 = LicenseControllerKt.access$getLogger$p();
                                    val var48: java.lang.Long = itx.getLong("lastOnlineTime");
                                    var10000.info(
                                       "请勿重复使用授权，上次检查时间：{}，上次检查ip：{}",
                                       LocalDateTime.ofInstant(Instant.ofEpochMilli(var48.longValue()), ZoneId.systemDefault()),
                                       itx.getString("lastOnlineIp")
                                    );
                                    var10000 = Unit.INSTANCE;
                                 }
                              }
                           }
                        }
                     }

                     var6 = Result.constructor-impl(var10000);
                  } catch (var24: java.lang.Throwable) {
                     val var8: Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var24));
                  }

                  if (Result.isSuccess-impl(var6)) {
                     val var32: Unit = var6 as Unit;
                  }

                  val var53: java.lang.Throwable = Result.exceptionOrNull-impl(var6);
                  if (var53 != null) {
                     LicenseControllerKt.access$getLogger$p().info("check license error: {}", var53.getMessage());
                  }

                  return Unit.INSTANCE;
               }

               @NotNull
               @Override
               public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                  val var3: Function2 = new <anonymous constructor>(this.this$0, this.$checkUrl, `$completion`);
                  var3.L$0 = value;
                  return var3 as Continuation<Unit>;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                  return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function2,
         2,
         null
      );
      return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
   }

   public suspend fun sendCodeToEmail(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      val var12: java.lang.String;
      if (context.request().method() === HttpMethod.POST) {
         val activeLicenseList: java.lang.String = context.getBodyAsJson().getString("email");
         var12 = if (activeLicenseList == null) "" else activeLicenseList;
      } else {
         val verifyCode: java.util.List = context.queryParam("email");
         val var13: java.lang.String = CollectionsKt.firstOrNull(verifyCode);
         var12 = if (var13 == null) "" else var13;
      }

      if (var12.length() == 0) {
         return returnData.setErrorMsg("邮箱错误");
      } else if (!ExtKt.validateEmail(var12)) {
         return returnData.setErrorMsg("仅支持 163|126|qq|yahoo|sina|sohu|yeah|139|189|21cn|outlook|gmail|icloud 等邮箱");
      } else {
         var var15: JsonArray = ExtKt.asJsonArray(ExtKt.getStorage$default(new java.lang.String[]{"data", "activeLicense"}, null, 2, null));
         if (var15 == null) {
            var15 = new JsonArray();
         }

         var var18: Int = 0;
         val code: Int = var15.size();
         if (0 < code) {
            do {
               val i: Int = var18++;
               if ("trial".equals(var15.getJsonObject(i).getString("type")) && var12.equals(var15.getJsonObject(i).getString("code"))) {
                  return returnData.setErrorMsg("该邮箱已被使用");
               }
            } while (var18 < code);
         }

         val var20: java.lang.CharSequence = this.tryCodeCache.getAsString(var12);
         if (var20 != null && var20.length() != 0) {
            return returnData.setData("", "您的验证码仍在有效期内，请勿重复获取");
         } else {
            val var23: java.lang.String = UUID.randomUUID().toString();
            val var10000: java.lang.String = var23.substring(0, 6);
            this.tryCodeCache.put(var12, var10000, 900);
            ExtKt.sendEmail(var12, "Reader Kindle端的试用申请验证", "您正在申请Reader Kindle端的试用，验证码是: $var10000，15分钟内有效，请勿回复");
            return returnData.setData("", "请查收邮件");
         }
      }
   }

   public suspend fun supplyLicense(context: RoutingContext): ReturnData {
      val returnData: ReturnData = new ReturnData();
      var code: java.lang.String = context.getBodyAsJson().getString("email");
      val email: java.lang.String = if (code == null) "" else code;
      var verifyCode: java.lang.String = context.getBodyAsJson().getString("code");
      code = if (verifyCode == null) "" else verifyCode;
      if (email.length() != 0 && (if (verifyCode == null) "" else verifyCode).length() != 0) {
         verifyCode = this.tryCodeCache.getAsString(email);
         this.tryCodeCache.remove(email);
         if (!code.equals(verifyCode)) {
            return returnData.setErrorMsg("验证码错误");
         } else {
            val var19: java.lang.String = ExtKt.jsonEncode$default(
               new License("*", 15, 0L, false, System.currentTimeMillis() + 604800000L, 1, "trial", null, email, false, null, 1152, null), false, 2, null
            );
            if (this.privateKeyContent.length() == 0) {
               val var20: java.lang.String = ExtKt.getStorage(new java.lang.String[]{"data", "privateKey"}, ".key");
               this.privateKeyContent = if (var20 == null) "" else var20;
            }

            val var21: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
            val var10000: EncoderUtils = EncoderUtils.INSTANCE;
            return ReturnData.setData$default(
               returnData, MapsKt.mapOf(TuplesKt.to("key", EncoderUtils.encryptSegmentByPrivateKey$default(var10000, var19, var21, 0, 4, null))), null, 2, null
            );
         }
      } else {
         return returnData.setErrorMsg("参数错误");
      }
   }
}
