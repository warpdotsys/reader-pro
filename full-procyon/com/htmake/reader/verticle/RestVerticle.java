// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.verticle;

import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.Map;
import kotlinx.coroutines.slf4j.MDCContext;
import kotlinx.coroutines.CoroutineScope;
import java.util.concurrent.CancellationException;
import kotlinx.coroutines.Job$DefaultImpls;
import kotlinx.coroutines.Job;
import kotlin.jvm.internal.Ref$ObjectRef;
import io.vertx.core.AsyncResult;
import java.net.URLDecoder;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Cookie;
import kotlin.jvm.functions.Function2;
import io.vertx.ext.web.Route;
import kotlin.coroutines.jvm.internal.Boxing;
import com.htmake.reader.utils.ExtKt;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.RoutingContext;
import com.htmake.reader.utils.VertExtKt;
import io.vertx.core.Handler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.Nullable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.Intrinsics;
import io.vertx.ext.web.Router;
import kotlin.Metadata;
import io.vertx.kotlin.coroutines.CoroutineVerticle;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H&J\u0019\u0010\u0011\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\nH?@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u001c\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u00192\n\u0010\u0015\u001a\u00060\u001aj\u0002`\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0012H\u0016J\u0011\u0010\u001d\u001a\u00020\u0012H\u0094@\u00f8\u0001\u0000?\u0006\u0002\u0010\u001eJ\b\u0010\u001f\u001a\u00020\u0012H\u0016J6\u0010 \u001a\u00020\u0012*\u00020!2\"\u0010\"\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$\u0012\u0006\u0012\u0004\u0018\u00010%0#\u00f8\u0001\u0000?\u0006\u0002\u0010&J6\u0010'\u001a\u00020\u0012*\u00020!2\"\u0010\"\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$\u0012\u0006\u0012\u0004\u0018\u00010%0#\u00f8\u0001\u0000?\u0006\u0002\u0010&R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0084.?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006(" }, d2 = { "Lcom/htmake/reader/verticle/RestVerticle;", "Lio/vertx/kotlin/coroutines/CoroutineVerticle;", "()V", "port", "", "getPort", "()I", "setPort", "(I)V", "router", "Lio/vertx/ext/web/Router;", "getRouter", "()Lio/vertx/ext/web/Router;", "setRouter", "(Lio/vertx/ext/web/Router;)V", "getContextPath", "", "initRouter", "", "(Lio/vertx/ext/web/Router;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onException", "error", "", "onHandlerError", "ctx", "Lio/vertx/ext/web/RoutingContext;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onStartError", "start", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "started", "coroutineHandler", "Lio/vertx/ext/web/Route;", "fn", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "(Lio/vertx/ext/web/Route;Lkotlin/jvm/functions/Function2;)V", "coroutineHandlerWithoutRes", "reader-pro" })
public abstract class RestVerticle extends CoroutineVerticle
{
    protected Router router;
    private int port;
    
    public RestVerticle() {
        this.port = 8080;
    }
    
    @NotNull
    protected final Router getRouter() {
        final Router router = this.router;
        if (router != null) {
            return router;
        }
        Intrinsics.throwUninitializedPropertyAccessException("router");
        throw null;
    }
    
    protected final void setRouter(@NotNull final Router <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.router = <set-?>;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int <set-?>) {
        this.port = <set-?>;
    }
    
    @Nullable
    protected Object start(@NotNull final Continuation<? super Unit> $completion) {
        return start$suspendImpl(this, $completion);
    }
    
    static /* synthetic */ Object start$suspendImpl(RestVerticle this, final Continuation continuation) {
        final Continuation $continuation;
        Label_0050: {
            if (continuation instanceof RestVerticle$start.RestVerticle$start$1) {
                final RestVerticle$start.RestVerticle$start$1 restVerticle$start$1 = (RestVerticle$start.RestVerticle$start$1)continuation;
                if ((restVerticle$start$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RestVerticle$start.RestVerticle$start$1 restVerticle$start$2 = restVerticle$start$1;
                    restVerticle$start$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RestVerticle$start.RestVerticle$start$1(this, continuation);
        }
        final Object $result = ((RestVerticle$start.RestVerticle$start$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0452: {
            switch (((RestVerticle$start.RestVerticle$start$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final RestVerticle restVerticle = this;
                    final Continuation continuation2 = $continuation;
                    ((RestVerticle$start.RestVerticle$start$1)$continuation).L$0 = this;
                    ((RestVerticle$start.RestVerticle$start$1)$continuation).label = 1;
                    if (restVerticle.start(continuation2) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    this = (RestVerticle)((RestVerticle$start.RestVerticle$start$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                case 2: {
                    this = (RestVerticle)((RestVerticle$start.RestVerticle$start$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0452;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            final RestVerticle restVerticle2 = this;
            final Router router = Router.router(this.getVertx());
            Intrinsics.checkNotNullExpressionValue((Object)router, "router(vertx)");
            restVerticle2.setRouter(router);
            final String cookieName = "reader.session";
            final Route route = this.getRouter().route();
            Intrinsics.checkNotNullExpressionValue((Object)route, "router.route()");
            final Route $this$globalHandler = route;
            final SessionHandler setSessionCookiePath = SessionHandler.create((SessionStore)LocalSessionStore.create(this.getVertx())).setSessionCookieName(cookieName).setSessionTimeout(604800000L).setSessionCookiePath("/");
            Intrinsics.checkNotNullExpressionValue((Object)setSessionCookiePath, "create(LocalSessionStore.create(vertx))\n                            .setSessionCookieName(cookieName)\n                            .setSessionTimeout(7L * 86400 * 1000)\n                            .setSessionCookiePath(\"/\")");
            VertExtKt.globalHandler($this$globalHandler, (Handler<RoutingContext>)setSessionCookiePath);
            final Route route2 = this.getRouter().route();
            Intrinsics.checkNotNullExpressionValue((Object)route2, "router.route()");
            VertExtKt.globalHandler(route2, (Handler<RoutingContext>)RestVerticle::start$lambda-1);
            final Route route3 = this.getRouter().route();
            Intrinsics.checkNotNullExpressionValue((Object)route3, "router.route()");
            VertExtKt.globalHandler(route3, (Handler<RoutingContext>)RestVerticle::start$lambda-3);
            final Route route4 = this.getRouter().route();
            Intrinsics.checkNotNullExpressionValue((Object)route4, "router.route()");
            final Route $this$globalHandler2 = route4;
            final BodyHandler create = BodyHandler.create();
            Intrinsics.checkNotNullExpressionValue((Object)create, "create()");
            VertExtKt.globalHandler($this$globalHandler2, (Handler<RoutingContext>)create);
            final Route route5 = this.getRouter().route();
            Intrinsics.checkNotNullExpressionValue((Object)route5, "router.route()");
            final Route $this$globalHandler3 = route5;
            final LoggerHandler create2 = LoggerHandler.create(LoggerFormat.DEFAULT);
            Intrinsics.checkNotNullExpressionValue((Object)create2, "create(LoggerFormat.DEFAULT)");
            VertExtKt.globalHandler($this$globalHandler3, (Handler<RoutingContext>)create2);
            final Route route6 = this.getRouter().route("/reader3/*");
            Intrinsics.checkNotNullExpressionValue((Object)route6, "router.route(\"/reader3/*\")");
            VertExtKt.globalHandler(route6, (Handler<RoutingContext>)RestVerticle::start$lambda-4);
            final Route value = this.getRouter().get("/health");
            Intrinsics.checkNotNullExpressionValue((Object)value, "router.get(\"/health\")");
            VertExtKt.globalHandler(value, (Handler<RoutingContext>)RestVerticle::start$lambda-5);
            final RestVerticle restVerticle3 = this;
            final Router router2 = this.getRouter();
            final Continuation $completion = $continuation;
            ((RestVerticle$start.RestVerticle$start$1)$continuation).L$0 = this;
            ((RestVerticle$start.RestVerticle$start$1)$continuation).label = 2;
            if (restVerticle3.initRouter(router2, (Continuation<? super Unit>)$completion) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        this.getRouter().route().last().failureHandler(RestVerticle::start$lambda-6);
        final String contextPath = this.getContextPath();
        Router mainRouter;
        if (contextPath.length() > 0) {
            final Router router3 = Router.router(this.getVertx());
            Intrinsics.checkNotNullExpressionValue((Object)router3, "router(vertx)");
            mainRouter = router3;
            mainRouter.mountSubRouter(ExtKt.toDir(contextPath, true), this.getRouter());
        }
        else {
            mainRouter = this.getRouter();
        }
        RestVerticleKt.access$getLogger$p().info("port: {}", (Object)Boxing.boxInt(this.getPort()));
        this.getVertx().createHttpServer().requestHandler((Handler)mainRouter).exceptionHandler(RestVerticle::start$lambda-7).listen(this.getPort(), RestVerticle::start$lambda-8);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public abstract Object initRouter(@NotNull final Router router, @NotNull final Continuation<? super Unit> $completion);
    
    @NotNull
    public abstract String getContextPath();
    
    public void onException(@NotNull final Throwable error) {
        Intrinsics.checkNotNullParameter((Object)error, "error");
        RestVerticleKt.access$getLogger$p().error("vertx exception: {}", error);
    }
    
    public void onStartError() {
    }
    
    public void started() {
    }
    
    public void onHandlerError(@NotNull final RoutingContext ctx, @NotNull final Exception error) {
        Intrinsics.checkNotNullParameter((Object)ctx, "ctx");
        Intrinsics.checkNotNullParameter((Object)error, "error");
        RestVerticleKt.access$getLogger$p().error("Error: {}", (Throwable)error);
        VertExtKt.error(ctx, error);
    }
    
    public final void coroutineHandler(@NotNull final Route $this$coroutineHandler, @NotNull final Function2<? super RoutingContext, ? super Continuation<Object>, ?> fn) {
        Intrinsics.checkNotNullParameter((Object)$this$coroutineHandler, "<this>");
        Intrinsics.checkNotNullParameter((Object)fn, "fn");
        VertExtKt.globalHandler($this$coroutineHandler, (Handler<RoutingContext>)RestVerticle::coroutineHandler$lambda-10);
    }
    
    public final void coroutineHandlerWithoutRes(@NotNull final Route $this$coroutineHandlerWithoutRes, @NotNull final Function2<? super RoutingContext, ? super Continuation<Object>, ?> fn) {
        Intrinsics.checkNotNullParameter((Object)$this$coroutineHandlerWithoutRes, "<this>");
        Intrinsics.checkNotNullParameter((Object)fn, "fn");
        VertExtKt.globalHandler($this$coroutineHandlerWithoutRes, (Handler<RoutingContext>)RestVerticle::coroutineHandlerWithoutRes$lambda-12);
    }
    
    private static final void start$lambda-1$lambda-0(final RoutingContext $it, final String $cookieName, final Void $noName_0) {
        Intrinsics.checkNotNullParameter((Object)$cookieName, "$cookieName");
        final Cookie cookie = $it.getCookie($cookieName);
        if (cookie != null) {
            cookie.setMaxAge(172800000L);
            cookie.setPath("/");
        }
    }
    
    private static final void start$lambda-1(final String $cookieName, final RoutingContext it) {
        Intrinsics.checkNotNullParameter((Object)$cookieName, "$cookieName");
        it.addHeadersEndHandler(RestVerticle::start$lambda-1$lambda-0);
        it.next();
    }
    
    private static final void start$lambda-3$lambda-2(final RoutingContext $it, final Void $noName_0) {
        final String origin = $it.request().getHeader("Origin");
        if (origin != null && origin.length() > 0) {
            final HttpServerResponse res = $it.response();
            res.putHeader("Access-Control-Allow-Origin", origin);
            res.putHeader("Access-Control-Allow-Credentials", "true");
            res.putHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE");
            res.putHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, X-Requested-With");
        }
    }
    
    private static final void start$lambda-3(final String $cookieName, final RoutingContext it) {
        Intrinsics.checkNotNullParameter((Object)$cookieName, "$cookieName");
        it.addHeadersEndHandler(RestVerticle::start$lambda-3$lambda-2);
        final String origin = it.request().getHeader("Origin");
        if (origin != null && origin.length() > 0 && it.request().method() == HttpMethod.OPTIONS) {
            it.removeCookie($cookieName);
            Intrinsics.checkNotNullExpressionValue((Object)it, "it");
            VertExtKt.success(it, "");
        }
        else {
            it.next();
        }
    }
    
    private static final void start$lambda-4(final RoutingContext it) {
        final String rawMethod = it.request().rawMethod();
        RestVerticleKt.access$getLogger$p().info("{} {}", (Object)rawMethod, (Object)URLDecoder.decode(it.request().absoluteURI(), "UTF-8"));
        if (!rawMethod.equals("PUT") && (it.fileUploads() == null || it.fileUploads().isEmpty()) && it.getBodyAsString() != null && it.getBodyAsString().length() > 0 && it.getBodyAsString().length() < 1000) {
            RestVerticleKt.access$getLogger$p().info("Request body: {}", (Object)it.getBodyAsString());
        }
        it.next();
    }
    
    private static final void start$lambda-5(final RoutingContext it) {
        Intrinsics.checkNotNullExpressionValue((Object)it, "it");
        VertExtKt.success(it, "ok!");
    }
    
    private static final void start$lambda-6(final RoutingContext ctx) {
        Intrinsics.checkNotNullExpressionValue((Object)ctx, "ctx");
        final Throwable failure = ctx.failure();
        Intrinsics.checkNotNullExpressionValue((Object)failure, "ctx.failure()");
        VertExtKt.error(ctx, failure);
    }
    
    private static final void start$lambda-7(final RestVerticle this$0, final Throwable error) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        Intrinsics.checkNotNullExpressionValue((Object)error, "error");
        this$0.onException(error);
    }
    
    private static final void start$lambda-8(final RestVerticle this$0, final AsyncResult res) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        if (res.succeeded()) {
            RestVerticleKt.access$getLogger$p().info("Server running at: http://localhost:{}", (Object)this$0.getPort());
            RestVerticleKt.access$getLogger$p().info("Web reader running at: http://localhost:{}", (Object)this$0.getPort());
            System.out.println((Object)"ReaderApplication Started");
            this$0.started();
        }
        else {
            this$0.onStartError();
        }
    }
    
    private static final void coroutineHandler$lambda-10$lambda-9(final Ref$ObjectRef $job, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$job, "$job");
        RestVerticleKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u7ec8\u6b62\u8fd0\u884c");
        final Job job = (Job)$job.element;
        if (job != null) {
            Job$DefaultImpls.cancel$default(job, (CancellationException)null, 1, (Object)null);
        }
    }
    
    private static final void coroutineHandler$lambda-10(final RestVerticle this$0, final Function2 $fn, final RoutingContext ctx) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        Intrinsics.checkNotNullParameter((Object)$fn, "$fn");
        final Ref$ObjectRef job = new Ref$ObjectRef();
        ctx.request().connection().closeHandler(RestVerticle::coroutineHandler$lambda-10$lambda-9);
        job.element = BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new RestVerticle$coroutineHandler$1.RestVerticle$coroutineHandler$1$2(ctx, $fn, this$0, (Continuation)null), 2, (Object)null);
    }
    
    private static final void coroutineHandlerWithoutRes$lambda-12$lambda-11(final Ref$ObjectRef $job, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$job, "$job");
        RestVerticleKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u7ec8\u6b62\u8fd0\u884c");
        final Job job = (Job)$job.element;
        if (job != null) {
            Job$DefaultImpls.cancel$default(job, (CancellationException)null, 1, (Object)null);
        }
    }
    
    private static final void coroutineHandlerWithoutRes$lambda-12(final RestVerticle this$0, final Function2 $fn, final RoutingContext ctx) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        Intrinsics.checkNotNullParameter((Object)$fn, "$fn");
        final Ref$ObjectRef job = new Ref$ObjectRef();
        ctx.request().connection().closeHandler(RestVerticle::coroutineHandlerWithoutRes$lambda-12$lambda-11);
        job.element = BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new RestVerticle$coroutineHandlerWithoutRes$1.RestVerticle$coroutineHandlerWithoutRes$1$2($fn, ctx, this$0, (Continuation)null), 2, (Object)null);
    }
}
