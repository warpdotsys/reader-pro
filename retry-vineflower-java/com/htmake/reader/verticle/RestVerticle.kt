package com.htmake.reader.verticle

import com.htmake.reader.utils.VertExtKt
import io.vertx.core.AsyncResult
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Cookie
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.net.URLDecoder
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Job.DefaultImpls
import kotlinx.coroutines.slf4j.MDCContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public abstract class RestVerticle : CoroutineVerticle {
   public open var port: Int = 8080
      internal final set

   protected final lateinit var router: Router
      internal set

   protected override suspend fun start() {
      return start$suspendImpl(this, `$completion`);
   }

   public abstract suspend fun initRouter(router: Router) {
   }

   public abstract fun getContextPath(): String {
   }

   public open fun onException(error: Throwable) {
      RestVerticleKt.access$getLogger$p().error("vertx exception: {}", error);
   }

   public open fun onStartError() {
   }

   public open fun started() {
   }

   public open fun onHandlerError(ctx: RoutingContext, error: Exception) {
      RestVerticleKt.access$getLogger$p().error("Error: {}", error);
      VertExtKt.error(ctx, error);
   }

   public fun Route.coroutineHandler(fn: (RoutingContext, Continuation<Any>) -> Any?) {
      VertExtKt.globalHandler(`$this$coroutineHandler`, RestVerticle::coroutineHandler$lambda-10);
   }

   public fun Route.coroutineHandlerWithoutRes(fn: (RoutingContext, Continuation<Any>) -> Any?) {
      VertExtKt.globalHandler(`$this$coroutineHandlerWithoutRes`, RestVerticle::coroutineHandlerWithoutRes$lambda-12);
   }

   @JvmStatic
   fun `start$lambda-1$lambda-0`(`$it`: RoutingContext, `$cookieName`: java.lang.String, `$noName_0`: Void) {
      val cookie: Cookie = `$it`.getCookie(`$cookieName`);
      if (cookie != null) {
         cookie.setMaxAge(172800000L);
         cookie.setPath("/");
      }
   }

   @JvmStatic
   fun `start$lambda-1`(`$cookieName`: java.lang.String, it: RoutingContext) {
      it.addHeadersEndHandler(RestVerticle::start$lambda-1$lambda-0);
      it.next();
   }

   @JvmStatic
   fun `start$lambda-3$lambda-2`(`$it`: RoutingContext, `$noName_0`: Void) {
      val origin: java.lang.String = `$it`.request().getHeader("Origin");
      if (origin != null && origin.length() > 0) {
         val var5: HttpServerResponse = `$it`.response();
         var5.putHeader("Access-Control-Allow-Origin", origin);
         var5.putHeader("Access-Control-Allow-Credentials", "true");
         var5.putHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE");
         var5.putHeader(
            "Access-Control-Allow-Headers", "Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, X-Requested-With"
         );
      }
   }

   @JvmStatic
   fun `start$lambda-3`(`$cookieName`: java.lang.String, it: RoutingContext) {
      it.addHeadersEndHandler(RestVerticle::start$lambda-3$lambda-2);
      val origin: java.lang.String = it.request().getHeader("Origin");
      if (origin != null && origin.length() > 0 && it.request().method() === HttpMethod.OPTIONS) {
         it.removeCookie(`$cookieName`);
         VertExtKt.success(it, "");
         return;
      } else {
         it.next();
      }
   }

   @JvmStatic
   fun `start$lambda-4`(it: RoutingContext) {
      val rawMethod: java.lang.String = it.request().rawMethod();
      RestVerticleKt.access$getLogger$p().info("{} {}", rawMethod, URLDecoder.decode(it.request().absoluteURI(), "UTF-8"));
      if (!rawMethod.equals("PUT")
         && (it.fileUploads() == null || it.fileUploads().isEmpty())
         && it.getBodyAsString() != null
         && it.getBodyAsString().length() > 0
         && it.getBodyAsString().length() < 1000) {
         RestVerticleKt.access$getLogger$p().info("Request body: {}", it.getBodyAsString());
      }

      it.next();
   }

   @JvmStatic
   fun `start$lambda-5`(it: RoutingContext) {
      VertExtKt.success(it, "ok!");
   }

   @JvmStatic
   fun `start$lambda-6`(ctx: RoutingContext) {
      val var1: java.lang.Throwable = ctx.failure();
      VertExtKt.error(ctx, var1);
   }

   @JvmStatic
   fun `start$lambda-7`(`this$0`: RestVerticle, error: java.lang.Throwable) {
      `this$0`.onException(error);
   }

   @JvmStatic
   fun `start$lambda-8`(`this$0`: RestVerticle, res: AsyncResult) {
      if (res.succeeded()) {
         RestVerticleKt.access$getLogger$p().info("Server running at: http://localhost:{}", `this$0`.getPort());
         RestVerticleKt.access$getLogger$p().info("Web reader running at: http://localhost:{}", `this$0`.getPort());
         System.out.println("ReaderApplication Started");
         `this$0`.started();
      } else {
         `this$0`.onStartError();
      }
   }

   @JvmStatic
   fun `coroutineHandler$lambda-10$lambda-9`(`$job`: ObjectRef, it: Void) {
      RestVerticleKt.access$getLogger$p().info("客户端已断开链接，终止运行");
      val var2: Job = `$job`.element as Job;
      if (`$job`.element as Job != null) {
         DefaultImpls.cancel$default(var2, null, 1, null);
      }
   }

   @JvmStatic
   fun `coroutineHandler$lambda-10`(`this$0`: RestVerticle, `$fn`: Function2, ctx: RoutingContext) {
      val job: ObjectRef = new ObjectRef();
      ctx.request().connection().closeHandler(RestVerticle::coroutineHandler$lambda-10$lambda-9);
      job.element = (T)BuildersKt.launch$default(
         `this$0`,
         new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
         null,
         (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(ctx, `$fn`, `this$0`, null) {
            Object L$0;
            int label;

            {
               super(2, `$completionx`);
               this.$ctx = `$ctx`;
               this.$fn = `$fn`;
               this.this$0 = `$receiver`;
            }

            // $VF: Handled exception range with multiple entry points by splitting it
            // $VF: Duplicated exception handlers to handle obfuscated exceptions
            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               var var4: RoutingContext;
               var var13: Any;
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);

                     try {
                        val e: RoutingContext = this.$ctx;
                        var4 = e;
                        var13 = this.$fn;
                        val var10: RoutingContext = this.$ctx;
                        this.L$0 = e;
                        this.label = 1;
                        var13 = (Function2)var13.invoke(var10, this);
                     } catch (var8: Exception) {
                        val var14: RestVerticle = this.this$0;
                        val var11: RoutingContext = this.$ctx;
                        var14.onHandlerError(var11, var8);
                        return Unit.INSTANCE;
                     }

                     if (var13 === var6) {
                        return var6;
                     }
                     break;
                  case 1:
                     var4 = this.L$0 as RoutingContext;

                     try {
                        ResultKt.throwOnFailure(`$result`);
                        var13 = (Function2)`$result`;
                        break;
                     } catch (var9: Exception) {
                        val var10000: RestVerticle = this.this$0;
                        val var3: RoutingContext = this.$ctx;
                        var10000.onHandlerError(var3, var9);
                        return Unit.INSTANCE;
                     }
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               try {
                  VertExtKt.success(var4, var13);
               } catch (var7: Exception) {
                  val var16: RestVerticle = this.this$0;
                  val var12: RoutingContext = this.$ctx;
                  var16.onHandlerError(var12, var7);
               }

               return Unit.INSTANCE;
            }

            @NotNull
            @Override
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
               return new <anonymous constructor>(this.$ctx, this.$fn, this.this$0, `$completion`);
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
               return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
            }
         }) as Function2,
         2,
         null
      );
   }

   @JvmStatic
   fun `coroutineHandlerWithoutRes$lambda-12$lambda-11`(`$job`: ObjectRef, it: Void) {
      RestVerticleKt.access$getLogger$p().info("客户端已断开链接，终止运行");
      val var2: Job = `$job`.element as Job;
      if (`$job`.element as Job != null) {
         DefaultImpls.cancel$default(var2, null, 1, null);
      }
   }

   @JvmStatic
   fun `coroutineHandlerWithoutRes$lambda-12`(`this$0`: RestVerticle, `$fn`: Function2, ctx: RoutingContext) {
      val job: ObjectRef = new ObjectRef();
      ctx.request().connection().closeHandler(RestVerticle::coroutineHandlerWithoutRes$lambda-12$lambda-11);
      job.element = (T)BuildersKt.launch$default(
         `this$0`,
         new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
         null,
         (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`$fn`, ctx, `this$0`, null) {
            int label;

            {
               super(2, `$completionx`);
               this.$fn = `$fn`;
               this.$ctx = `$ctx`;
               this.this$0 = `$receiver`;
            }

            // $VF: Handled exception range with multiple entry points by splitting it
            // $VF: Duplicated exception handlers to handle obfuscated exceptions
            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);

                     var var12: Function2;
                     try {
                        var12 = this.$fn;
                        val e: RoutingContext = this.$ctx;
                        this.label = 1;
                        var12 = (Function2)var12.invoke(e, this);
                     } catch (var6: Exception) {
                        val var10: RestVerticle = this.this$0;
                        val var8: RoutingContext = this.$ctx;
                        var10.onHandlerError(var8, var6);
                        return Unit.INSTANCE;
                     }

                     if (var12 === var4) {
                        return var4;
                     }
                     break;
                  case 1:
                     try {
                        ResultKt.throwOnFailure(`$result`);
                        break;
                     } catch (var7: Exception) {
                        val var10000: RestVerticle = this.this$0;
                        val var3: RoutingContext = this.$ctx;
                        var10000.onHandlerError(var3, var7);
                        return Unit.INSTANCE;
                     }
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               try {
                  ;
               } catch (var5: Exception) {
                  val var13: RestVerticle = this.this$0;
                  val var9: RoutingContext = this.$ctx;
                  var13.onHandlerError(var9, var5);
               }

               return Unit.INSTANCE;
            }

            @NotNull
            @Override
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
               return new <anonymous constructor>(this.$fn, this.$ctx, this.this$0, `$completion`);
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
               return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
            }
         }) as Function2,
         2,
         null
      );
   }
}
