package com.htmake.reader.api

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.SpringEvent
import com.htmake.reader.api.controller.BookController
import com.htmake.reader.api.controller.BookSourceController
import com.htmake.reader.api.controller.LicenseController
import com.htmake.reader.api.controller.UserController
import com.htmake.reader.config.AppConfig
import com.htmake.reader.config.BookConfig
import com.htmake.reader.entity.License
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.VertExtKt
import com.htmake.reader.verticle.RestVerticle
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.net.impl.URIDecoder
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URLDecoder
import java.util.Calendar
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function2
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Ref.IntRef
import kotlin.jvm.internal.Ref.ObjectRef
import kotlin.random.Random
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelayKt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.slf4j.MDCContext
import mu.KLogger
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.slf4j.MDC
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
public open class YueduApi : RestVerticle {
   private final lateinit var appConfig: AppConfig
   private final lateinit var env: Environment

   public override suspend fun initRouter(router: Router) {
      return initRouter$suspendImpl(this, router, `$completion`);
   }

   public open suspend fun setupPort() {
      return setupPort$suspendImpl(this, `$completion`);
   }

   public open suspend fun migration() {
      return migration$suspendImpl(this, `$completion`);
   }

   public override fun getContextPath(): String {
      if (this.env == null) {
         Intrinsics.throwUninitializedPropertyAccessException("env");
         throw null;
      } else {
         val contextPath: java.lang.String = this.env.getProperty("reader.server.contextPath", java.lang.String.class);
         if (contextPath != null && contextPath.length() != 0) {
            return contextPath;
         } else {
            return "";
         }
      }
   }

   public override fun started() {
      SpringContextUtils.getApplicationContext().publishEvent(new SpringEvent(this, "READY", ""));
   }

   public override fun onStartError() {
      YueduApiKt.access$getLogger$p().error("应用启动失败，请检查${this.getPort()}端口是否被占用");
      SpringContextUtils.getApplicationContext().publishEvent(new SpringEvent(this, "START_ERROR", "应用启动失败，请检查${this.getPort()}端口是否被占用"));
   }

   public override fun onHandlerError(ctx: RoutingContext, error: Exception) {
      val returnData: ReturnData = new ReturnData();
      YueduApiKt.access$getLogger$p().error("onHandlerError: ", error);
      if (!ctx.response().headWritten()) {
         VertExtKt.success(ctx, returnData.setErrorMsg(error.toString()));
      } else {
         ctx.response().end(error.toString());
      }
   }

   private suspend fun getSystemInfo(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label20: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label20;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
            Object L$6;
            Object L$7;
            Object L$8;
            Object L$9;
            Object L$10;
            Object L$11;
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
               return YueduApi.access$getSystemInfo(this.this$0, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var20: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var systemFont: java.lang.String;
      var freeMemory: java.lang.String;
      var totalMemory: java.lang.String;
      var maxMemory: java.lang.String;
      var dayLoginUser: IntRef;
      var sevenDayLoginUser: IntRef;
      var monthLoginUser: IntRef;
      var keepUser: IntRef;
      var dayRegisterUser: IntRef;
      var sevenDayRegisterUser: IntRef;
      var monthRegisterUser: IntRef;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            systemFont = System.getProperty("reader.system.fonts");
            freeMemory = "${Runtime.getRuntime().freeMemory() / 1024 / 1024}M";
            totalMemory = "${Runtime.getRuntime().totalMemory() / 1024 / 1024}M";
            maxMemory = "${Runtime.getRuntime().maxMemory() / 1024 / 1024}M";
            val userController: UserController = new UserController(this.getCoroutineContext());
            dayLoginUser = new IntRef();
            sevenDayLoginUser = new IntRef();
            monthLoginUser = new IntRef();
            keepUser = new IntRef();
            dayRegisterUser = new IntRef();
            sevenDayRegisterUser = new IntRef();
            monthRegisterUser = new IntRef();
            val calendar: Calendar = Calendar.getInstance();
            calendar.set(5, 1);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.getTimeInMillis();
            val var10001: Function3 = (
               new Function3<CoroutineScope, User, Continuation<? super java.lang.Boolean>, Object>(
                  dayLoginUser, sevenDayLoginUser, calendar, monthLoginUser, dayRegisterUser, sevenDayRegisterUser, monthRegisterUser, keepUser, null
               ) {
                  int label;

                  {
                     super(3, `$completion`);
                     this.$dayLoginUser = `$dayLoginUser`;
                     this.$sevenDayLoginUser = `$sevenDayLoginUser`;
                     this.$calendar = `$calendar`;
                     this.$monthLoginUser = `$monthLoginUser`;
                     this.$dayRegisterUser = `$dayRegisterUser`;
                     this.$sevenDayRegisterUser = `$sevenDayRegisterUser`;
                     this.$monthRegisterUser = `$monthRegisterUser`;
                     this.$keepUser = `$keepUser`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var3: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val user: User = this.L$0 as User;
                           if ((this.L$0 as User).getLast_login_at() >= System.currentTimeMillis() - 86400000L) {
                              this.$dayLoginUser.element++;
                           }

                           if (user.getLast_login_at() >= System.currentTimeMillis() - 604800000L) {
                              this.$sevenDayLoginUser.element++;
                           }

                           if (user.getLast_login_at() >= this.$calendar.getTimeInMillis()) {
                              this.$monthLoginUser.element++;
                           }

                           if (user.getCreated_at() >= System.currentTimeMillis() - 86400000L) {
                              this.$dayRegisterUser.element++;
                           }

                           if (user.getCreated_at() >= System.currentTimeMillis() - 604800000L) {
                              this.$sevenDayRegisterUser.element++;
                           }

                           if (user.getCreated_at() >= this.$calendar.getTimeInMillis()) {
                              this.$monthRegisterUser.element++;
                           }

                           if (user.getLast_login_at() >= user.getCreated_at() + 604800000L
                              && user.getLast_login_at() >= System.currentTimeMillis() - 604800000L) {
                              this.$keepUser.element++;
                           }

                           return Boxing.boxBoolean(false);
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super java.lang.Boolean> p3) {
                     val var4: Function3 = new <anonymous constructor>(
                        this.$dayLoginUser,
                        this.$sevenDayLoginUser,
                        this.$calendar,
                        this.$monthLoginUser,
                        this.$dayRegisterUser,
                        this.$sevenDayRegisterUser,
                        this.$monthRegisterUser,
                        this.$keepUser,
                        p3
                     );
                     var4.L$0 = p2;
                     return var4.invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function3;
            `$continuation`.L$0 = returnData;
            `$continuation`.L$1 = systemFont;
            `$continuation`.L$2 = freeMemory;
            `$continuation`.L$3 = totalMemory;
            `$continuation`.L$4 = maxMemory;
            `$continuation`.L$5 = dayLoginUser;
            `$continuation`.L$6 = sevenDayLoginUser;
            `$continuation`.L$7 = monthLoginUser;
            `$continuation`.L$8 = keepUser;
            `$continuation`.L$9 = dayRegisterUser;
            `$continuation`.L$10 = sevenDayRegisterUser;
            `$continuation`.L$11 = monthRegisterUser;
            `$continuation`.label = 1;
            if (userController.forEachUser(var10001, `$continuation`) === var20) {
               return var20;
            }
            break;
         case 1:
            monthRegisterUser = `$continuation`.L$11 as IntRef;
            sevenDayRegisterUser = `$continuation`.L$10 as IntRef;
            dayRegisterUser = `$continuation`.L$9 as IntRef;
            keepUser = `$continuation`.L$8 as IntRef;
            monthLoginUser = `$continuation`.L$7 as IntRef;
            sevenDayLoginUser = `$continuation`.L$6 as IntRef;
            dayLoginUser = `$continuation`.L$5 as IntRef;
            maxMemory = `$continuation`.L$4 as java.lang.String;
            totalMemory = `$continuation`.L$3 as java.lang.String;
            freeMemory = `$continuation`.L$2 as java.lang.String;
            systemFont = `$continuation`.L$1 as java.lang.String;
            returnData = `$continuation`.L$0 as ReturnData;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return ReturnData.setData$default(
         returnData,
         MapsKt.mapOf(
            new Pair[]{
               TuplesKt.to("fonts", systemFont),
               TuplesKt.to("freeMemory", freeMemory),
               TuplesKt.to("totalMemory", totalMemory),
               TuplesKt.to("maxMemory", maxMemory),
               TuplesKt.to("dayRegisterUser", Boxing.boxInt(dayRegisterUser.element)),
               TuplesKt.to("dayLoginUser", Boxing.boxInt(dayLoginUser.element)),
               TuplesKt.to("sevenDayRegisterUser", Boxing.boxInt(sevenDayRegisterUser.element)),
               TuplesKt.to("sevenDayLoginUser", Boxing.boxInt(sevenDayLoginUser.element)),
               TuplesKt.to("monthRegisterUser", Boxing.boxInt(monthRegisterUser.element)),
               TuplesKt.to("monthLoginUser", Boxing.boxInt(monthLoginUser.element)),
               TuplesKt.to("keepUser", Boxing.boxInt(keepUser.element))
            }
         ),
         null,
         2,
         null
      );
   }

   @Scheduled(cron = "0 0/10 * * * ?")
   public open fun shelfUpdateJob() {
      if (this.appConfig == null) {
         Intrinsics.throwUninitializedPropertyAccessException("appConfig");
         throw null;
      } else if (this.appConfig.getShelfUpdateInteval() > 0) {
         val var6: Calendar = Calendar.getInstance();
         val muniteFromToday: Int = var6.get(11) * 60 + var6.get(12);
         if (this.appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
         } else if (muniteFromToday % this.appConfig.getShelfUpdateInteval() == 0) {
            MDC.put("traceId", ExtKt.getTraceId());
            BuildersKt.launch$default(
               this, new MDCContext(null, 1, null).plus(Dispatchers.getIO()), null, (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(null) {
                  Object L$1;
                  int label;

                  {
                     super(2, `$completion`);
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     label48: {
                        val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        var `$this$launch`: CoroutineScope;
                        var e: BookController;
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              `$this$launch` = this.L$0 as CoroutineScope;

                              var var10000: Any;
                              try {
                                 e = new BookController(`$this$launch`.getCoroutineContext());
                                 YueduApiKt.access$getLogger$p().info("开始检查书架书籍更新");
                                 val var10003: Continuation = this;
                                 this.L$0 = `$this$launch`;
                                 this.L$1 = e;
                                 this.label = 1;
                                 var10000 = e.getBookShelfBooks(true, "default", var10003);
                              } catch (var8: Exception) {
                                 var8.printStackTrace();
                                 return Unit.INSTANCE;
                              }

                              if (var10000 === var5) {
                                 return var5;
                              }
                              break;
                           case 1:
                              e = this.L$1 as BookController;
                              `$this$launch` = this.L$0 as CoroutineScope;

                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break;
                              } catch (var9: Exception) {
                                 var9.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           case 2:
                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break label48;
                              } catch (var10: Exception) {
                                 var10.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        var var11: Any;
                        try {
                           val userController: UserController = new UserController(`$this$launch`.getCoroutineContext());
                           val var10001: Function3 = (new Function3<CoroutineScope, User, Continuation<? super java.lang.Boolean>, Object>(e, null) {
                              int label;

                              {
                                 super(3, `$completionx`);
                                 this.$bookController = `$bookController`;
                              }

                              @Nullable
                              @Override
                              public final Object invokeSuspend(@NotNull Object $result) {
                                 val var3: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                 switch (this.label) {
                                    case 0:
                                       ResultKt.throwOnFailure(`$result`);
                                       val user: User = this.L$0 as User;
                                       if ((this.L$0 as User).getLast_login_at() >= System.currentTimeMillis() - 259200000L) {
                                          val var10000: BookController = this.$bookController;
                                          val var10002: java.lang.String = user.getUsername();
                                          val var10003: Continuation = this;
                                          this.label = 1;
                                          if (var10000.getBookShelfBooks(true, var10002, var10003) === var3) {
                                             return var3;
                                          }
                                       }
                                       break;
                                    case 1:
                                       ResultKt.throwOnFailure(`$result`);
                                       break;
                                    default:
                                       throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                 }

                                 return Boxing.boxBoolean(false);
                              }

                              @Nullable
                              public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super java.lang.Boolean> p3) {
                                 val var4: Function3 = new <anonymous constructor>(this.$bookController, p3);
                                 var4.L$0 = p2;
                                 return var4.invokeSuspend(Unit.INSTANCE);
                              }
                           }) as Function3;
                           val var10002: Continuation = this;
                           this.L$0 = null;
                           this.L$1 = null;
                           this.label = 2;
                           var11 = userController.forEachUser(var10001, var10002);
                        } catch (var7: Exception) {
                           var7.printStackTrace();
                           return Unit.INSTANCE;
                        }

                        if (var11 === var5) {
                           return var5;
                        }
                     }

                     try {
                        YueduApiKt.access$getLogger$p().info("书架书籍更新检查结束");
                     } catch (var6: Exception) {
                        var6.printStackTrace();
                     }

                     return Unit.INSTANCE;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     val var3: Function2 = new <anonymous constructor>(`$completion`);
                     var3.L$0 = value;
                     return var3 as Continuation<Unit>;
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                     return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                  }
               }) as Function2, 2, null
            );
         }
      }
   }

   @Scheduled(cron = "0 0/10 * * * ?")
   public open fun remoteBookSourceSubUpdateJob() {
      if (this.appConfig == null) {
         Intrinsics.throwUninitializedPropertyAccessException("appConfig");
         throw null;
      } else if (this.appConfig.getRemoteBookSourceUpdateInterval() > 0) {
         val var6: Calendar = Calendar.getInstance();
         val muniteFromToday: Int = var6.get(11) * 60 + var6.get(12);
         if (this.appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
         } else if (muniteFromToday % this.appConfig.getRemoteBookSourceUpdateInterval() == 0) {
            MDC.put("traceId", ExtKt.getTraceId());
            BuildersKt.launch$default(
               this, new MDCContext(null, 1, null).plus(Dispatchers.getIO()), null, (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(null) {
                  Object L$1;
                  int label;

                  {
                     super(2, `$completion`);
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     label48: {
                        val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        var `$this$launch`: CoroutineScope;
                        var e: BookSourceController;
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              `$this$launch` = this.L$0 as CoroutineScope;

                              var var10000: Any;
                              try {
                                 e = new BookSourceController(`$this$launch`.getCoroutineContext());
                                 YueduApiKt.access$getLogger$p().info("开始检查远程书源更新");
                                 val var10003: Continuation = this;
                                 this.L$0 = `$this$launch`;
                                 this.L$1 = e;
                                 this.label = 1;
                                 var10000 = e.updateRemoteSourceSub("default", null, var10003);
                              } catch (var8: Exception) {
                                 var8.printStackTrace();
                                 return Unit.INSTANCE;
                              }

                              if (var10000 === var5) {
                                 return var5;
                              }
                              break;
                           case 1:
                              e = this.L$1 as BookSourceController;
                              `$this$launch` = this.L$0 as CoroutineScope;

                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break;
                              } catch (var9: Exception) {
                                 var9.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           case 2:
                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break label48;
                              } catch (var10: Exception) {
                                 var10.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        var var11: Any;
                        try {
                           val userController: UserController = new UserController(`$this$launch`.getCoroutineContext());
                           val var10001: Function3 = (new Function3<CoroutineScope, User, Continuation<? super java.lang.Boolean>, Object>(e, null) {
                              int label;

                              {
                                 super(3, `$completionx`);
                                 this.$bookSourceController = `$bookSourceController`;
                              }

                              @Nullable
                              @Override
                              public final Object invokeSuspend(@NotNull Object $result) {
                                 val var3: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                 switch (this.label) {
                                    case 0:
                                       ResultKt.throwOnFailure(`$result`);
                                       val user: User = this.L$0 as User;
                                       if ((this.L$0 as User).getLast_login_at() >= System.currentTimeMillis() - 259200000L) {
                                          val var10000: BookSourceController = this.$bookSourceController;
                                          val var10001: java.lang.String = user.getUsername();
                                          val var10003: Continuation = this;
                                          this.label = 1;
                                          if (var10000.updateRemoteSourceSub(var10001, user, var10003) === var3) {
                                             return var3;
                                          }
                                       }
                                       break;
                                    case 1:
                                       ResultKt.throwOnFailure(`$result`);
                                       break;
                                    default:
                                       throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                 }

                                 return Boxing.boxBoolean(false);
                              }

                              @Nullable
                              public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super java.lang.Boolean> p3) {
                                 val var4: Function3 = new <anonymous constructor>(this.$bookSourceController, p3);
                                 var4.L$0 = p2;
                                 return var4.invokeSuspend(Unit.INSTANCE);
                              }
                           }) as Function3;
                           val var10002: Continuation = this;
                           this.L$0 = null;
                           this.L$1 = null;
                           this.label = 2;
                           var11 = userController.forEachUser(var10001, var10002);
                        } catch (var7: Exception) {
                           var7.printStackTrace();
                           return Unit.INSTANCE;
                        }

                        if (var11 === var5) {
                           return var5;
                        }
                     }

                     try {
                        YueduApiKt.access$getLogger$p().info("远程书源更新检查结束");
                     } catch (var6: Exception) {
                        var6.printStackTrace();
                     }

                     return Unit.INSTANCE;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     val var3: Function2 = new <anonymous constructor>(`$completion`);
                     var3.L$0 = value;
                     return var3 as Continuation<Unit>;
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                     return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                  }
               }) as Function2, 2, null
            );
         }
      }
   }

   @Scheduled(cron = "0 59 23 * * ?")
   public open fun clearUser() {
      if (this.appConfig == null) {
         Intrinsics.throwUninitializedPropertyAccessException("appConfig");
         throw null;
      } else {
         if (this.appConfig.getAutoClearInactiveUser() > 0) {
            if (this.appConfig == null) {
               Intrinsics.throwUninitializedPropertyAccessException("appConfig");
               throw null;
            }

            if (this.appConfig.getSecure()) {
               MDC.put("traceId", ExtKt.getTraceId());
               BuildersKt.launch$default(
                  this,
                  new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                  null,
                  (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, null) {
                     int label;

                     {
                        super(2, `$completionx`);
                        this.this$0 = `$receiver`;
                     }

                     @Nullable
                     @Override
                     public final Object invokeSuspend(@NotNull Object $result) {
                        val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              val `$this$launch`: CoroutineScope = this.L$0 as CoroutineScope;

                              var var10: KLogger;
                              try {
                                 var10 = YueduApiKt.access$getLogger$p();
                                 val e: AppConfig = YueduApi.access$getAppConfig$p(this.this$0);
                                 if (e == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                                    throw null;
                                 }

                                 var10.info("开始清理 {} 天未登录用户", Boxing.boxInt(e.getAutoClearInactiveUser()));
                                 val var9: UserController = new UserController(`$this$launch`.getCoroutineContext());
                                 val var4: AppConfig = YueduApi.access$getAppConfig$p(this.this$0);
                                 if (var4 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                                    throw null;
                                 }

                                 val var10001: Int = var4.getAutoClearInactiveUser();
                                 val var10002: Continuation = this;
                                 this.label = 1;
                                 var10 = (KLogger)var9.clearInactiveUsers(var10001, var10002);
                              } catch (var8: Exception) {
                                 var8.printStackTrace();
                                 return Unit.INSTANCE;
                              }

                              if (var10 === var5) {
                                 return var5;
                              }
                              break;
                           case 1:
                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break;
                              } catch (var7: Exception) {
                                 var7.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        try {
                           YueduApiKt.access$getLogger$p().info("不活跃用户自动清理结束");
                        } catch (var6: Exception) {
                           var6.printStackTrace();
                        }

                        return Unit.INSTANCE;
                     }

                     @NotNull
                     @Override
                     public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                        val var3: Function2 = new <anonymous constructor>(this.this$0, `$completion`);
                        var3.L$0 = value;
                        return var3 as Continuation<Unit>;
                     }

                     @Nullable
                     public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                        return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                     }
                  }) as Function2,
                  2,
                  null
               );
               return;
            }
         }
      }
   }

   @Scheduled(cron = "0 50 23 * * ?")
   public open fun autoBackup() {
      if (this.appConfig == null) {
         Intrinsics.throwUninitializedPropertyAccessException("appConfig");
         throw null;
      } else if (this.appConfig.getAutoBackupUserData()) {
         MDC.put("traceId", ExtKt.getTraceId());
         BuildersKt.launch$default(
            this, new MDCContext(null, 1, null).plus(Dispatchers.getIO()), null, (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(null) {
               Object L$1;
               int label;

               {
                  super(2, `$completion`);
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  label48: {
                     val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var `$this$launch`: CoroutineScope;
                     var e: BookController;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           `$this$launch` = this.L$0 as CoroutineScope;

                           var var10000: Any;
                           try {
                              e = new BookController(`$this$launch`.getCoroutineContext());
                              YueduApiKt.access$getLogger$p().info("开始备份用户数据");
                              val var10003: Continuation = this;
                              this.L$0 = `$this$launch`;
                              this.L$1 = e;
                              this.label = 1;
                              var10000 = BookController.saveToWebdav$default(e, "default", null, var10003, 2, null);
                           } catch (var8: Exception) {
                              var8.printStackTrace();
                              return Unit.INSTANCE;
                           }

                           if (var10000 === var5) {
                              return var5;
                           }
                           break;
                        case 1:
                           e = this.L$1 as BookController;
                           `$this$launch` = this.L$0 as CoroutineScope;

                           try {
                              ResultKt.throwOnFailure(`$result`);
                              break;
                           } catch (var9: Exception) {
                              var9.printStackTrace();
                              return Unit.INSTANCE;
                           }
                        case 2:
                           try {
                              ResultKt.throwOnFailure(`$result`);
                              break label48;
                           } catch (var10: Exception) {
                              var10.printStackTrace();
                              return Unit.INSTANCE;
                           }
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     var var11: Any;
                     try {
                        val userController: UserController = new UserController(`$this$launch`.getCoroutineContext());
                        val var10001: Function3 = (new Function3<CoroutineScope, User, Continuation<? super java.lang.Boolean>, Object>(e, null) {
                           int label;

                           {
                              super(3, `$completionx`);
                              this.$bookController = `$bookController`;
                           }

                           @Nullable
                           @Override
                           public final Object invokeSuspend(@NotNull Object $result) {
                              val var3: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                              switch (this.label) {
                                 case 0:
                                    ResultKt.throwOnFailure(`$result`);
                                    val user: User = this.L$0 as User;
                                    if ((this.L$0 as User).getLast_login_at() >= System.currentTimeMillis() - 259200000L) {
                                       val var10000: BookController = this.$bookController;
                                       val var10001: java.lang.String = user.getUsername();
                                       val var10003: Continuation = this;
                                       this.label = 1;
                                       if (BookController.saveToWebdav$default(var10000, var10001, null, var10003, 2, null) === var3) {
                                          return var3;
                                       }
                                    }
                                    break;
                                 case 1:
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 default:
                                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                              }

                              return Boxing.boxBoolean(false);
                           }

                           @Nullable
                           public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super java.lang.Boolean> p3) {
                              val var4: Function3 = new <anonymous constructor>(this.$bookController, p3);
                              var4.L$0 = p2;
                              return var4.invokeSuspend(Unit.INSTANCE);
                           }
                        }) as Function3;
                        val var10002: Continuation = this;
                        this.L$0 = null;
                        this.L$1 = null;
                        this.label = 2;
                        var11 = userController.forEachUser(var10001, var10002);
                     } catch (var7: Exception) {
                        var7.printStackTrace();
                        return Unit.INSTANCE;
                     }

                     if (var11 === var5) {
                        return var5;
                     }
                  }

                  try {
                     YueduApiKt.access$getLogger$p().info("备份用户数据结束");
                  } catch (var6: Exception) {
                     var6.printStackTrace();
                  }

                  return Unit.INSTANCE;
               }

               @NotNull
               @Override
               public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                  val var3: Function2 = new <anonymous constructor>(`$completion`);
                  var3.L$0 = value;
                  return var3 as Continuation<Unit>;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                  return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
               }
            }) as Function2, 2, null
         );
      }
   }

   @Scheduled(cron = "0 0 2 * * ?")
   public open fun autoGC() {
      System.gc();
   }

   @Scheduled(cron = "0 4/15 7-23 * * ?")
   public open fun checkLicense() {
      val license: License = ExtKt.getInstalledLicense(true);
      if (!"default".equals(license.getType())) {
         MDC.put("traceId", ExtKt.getTraceId());
         BuildersKt.launch$default(
            this,
            new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
            null,
            (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(license, null) {
               int label;

               {
                  super(2, `$completionx`);
                  this.$license = `$license`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  label63: {
                     var `$this$launch`: CoroutineScope;
                     var var5: Any;
                     label64: {
                        var5 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              `$this$launch` = this.L$0 as CoroutineScope;

                              var var16: Any;
                              try {
                                 val var10000: Long = RangesKt.random(new IntRange(10, 120), Random.Default) * 1000L;
                                 val var10001: Continuation = this;
                                 this.L$0 = `$this$launch`;
                                 this.label = 1;
                                 var16 = DelayKt.delay(var10000, var10001);
                              } catch (var9: Exception) {
                                 var9.printStackTrace();
                                 return Unit.INSTANCE;
                              }

                              if (var16 === var5) {
                                 return var5;
                              }
                              break;
                           case 1:
                              `$this$launch` = this.L$0 as CoroutineScope;

                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break;
                              } catch (var10: Exception) {
                                 var10.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           case 2:
                              `$this$launch` = this.L$0 as CoroutineScope;

                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break label64;
                              } catch (var11: Exception) {
                                 var11.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           case 3:
                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 break label63;
                              } catch (var12: Exception) {
                                 var12.printStackTrace();
                                 return Unit.INSTANCE;
                              }
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        var var18: Any;
                        try {
                           val var17: Long = RangesKt.random(new IntRange(1, 10), Random.Default) * 1000L;
                           val var20: Continuation = this;
                           this.L$0 = `$this$launch`;
                           this.label = 2;
                           var18 = DelayKt.delay(var17, var20);
                        } catch (var8: Exception) {
                           var8.printStackTrace();
                           return Unit.INSTANCE;
                        }

                        if (var18 === var5) {
                           return var5;
                        }
                     }

                     var var19: Any;
                     try {
                        YueduApiKt.access$getLogger$p().info("开始检查授权是否正常");
                        val var14: LicenseController = new LicenseController(`$this$launch`.getCoroutineContext());
                        val var21: License = this.$license;
                        val var10002: Continuation = this;
                        this.L$0 = null;
                        this.label = 3;
                        var19 = var14.checkLicense(var21, var10002);
                     } catch (var7: Exception) {
                        var7.printStackTrace();
                        return Unit.INSTANCE;
                     }

                     if (var19 === var5) {
                        return var5;
                     }
                  }

                  try {
                     ;
                  } catch (var6: Exception) {
                     var6.printStackTrace();
                  }

                  return Unit.INSTANCE;
               }

               @NotNull
               @Override
               public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                  val var3: Function2 = new <anonymous constructor>(this.$license, `$completion`);
                  var3.L$0 = value;
                  return var3 as Continuation<Unit>;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                  return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
               }
            }) as Function2,
            2,
            null
         );
      }
   }

   @JvmStatic
   fun `initRouter$lambda-0`(`$dataDir`: ObjectRef, it: RoutingContext) {
      var filePath: java.lang.String = it.request().path();
      filePath = URIDecoder.decodeURIComponent(StringsKt.replace(filePath, "/book-assets/", "/", true), false);
      if (StringsKt.endsWith(filePath, "html", true) || StringsKt.endsWith(filePath, "htm", true)) {
         val var6: File = new File(Intrinsics.stringPlus(`$dataDir`.element as java.lang.String, filePath));
         if (var6.exists()) {
            val var10000: BookConfig = BookConfig.INSTANCE;
            val var4: java.lang.String = var6.toString();
            var10000.injectJavascriptToEpubChapter(var4);
         }
      }

      it.next();
   }

   @JvmStatic
   fun `initRouter$lambda-1`(`$dataDir`: ObjectRef, it: RoutingContext) {
      var filePath: java.lang.String = it.request().path();
      filePath = URLDecoder.decode(StringsKt.replace(filePath, "/epub/", "/", true), "UTF-8");
      if (StringsKt.endsWith(filePath, "html", true)) {
         val var6: File = new File(Intrinsics.stringPlus(`$dataDir`.element as java.lang.String, filePath));
         if (var6.exists()) {
            val var10000: BookConfig = BookConfig.INSTANCE;
            val var4: java.lang.String = var6.toString();
            var10000.injectJavascriptToEpubChapter(var4);
         }
      }

      it.next();
   }

   @JvmStatic
   fun `initRouter$lambda-2`(it: RoutingContext) {
      var var1: java.lang.String = it.request().path();
      if (StringsKt.endsWith$default(var1, "/simple-web", false, 2, null)) {
         val var10000: HttpServerResponse = it.response();
         var1 = URLDecoder.decode(it.request().absoluteURI(), "UTF-8");
         var10000.putHeader("Location", StringsKt.replace$default(var1, "/simple-web", "/simple-web/", false, 4, null)).setStatusCode(302).end();
      } else {
         it.next();
      }
   }

   @JvmStatic
   fun `initRouter$lambda-3`(it: RoutingContext) {
      val license: License = ExtKt.getInstalledLicense$default(false, 1, null);
      var simpleWebExpiredAt: Long = 0L;
      val var4: java.lang.String = it.request().host();
      if (license.validHost(var4)) {
         simpleWebExpiredAt = license.getSimpleWebExpiredAt();
      }

      if (simpleWebExpiredAt != 0L && simpleWebExpiredAt < System.currentTimeMillis()) {
         it.response()
            .putHeader("content-type", "text/html; charset=UTF-8")
            .setStatusCode(403)
            .end(
               "<html><head><title>未激活该功能</title></head><body><div style='text-align: center;padding: 30px 0;'>未激活该功能，请加<a href='https://t.me/+pQ8HDlANPZ84ZWNl'>TG群</a>激活</div></body></html>"
            );
      } else {
         it.next();
      }
   }
}
