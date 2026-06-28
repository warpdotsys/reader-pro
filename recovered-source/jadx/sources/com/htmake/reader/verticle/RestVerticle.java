package com.htmake.reader.verticle;

import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.VertExtKt;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.kotlin.coroutines.CoroutineVerticle;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.slf4j.MDCContext;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: RestVerticle.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/verticle/RestVerticle.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H&J\u0019\u0010\u0011\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\nH¦@ø\u0001\u0000¢\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u001c\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u00192\n\u0010\u0015\u001a\u00060\u001aj\u0002`\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0012H\u0016J\u0011\u0010\u001d\u001a\u00020\u0012H\u0094@ø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\b\u0010\u001f\u001a\u00020\u0012H\u0016J6\u0010 \u001a\u00020\u0012*\u00020!2\"\u0010\"\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$\u0012\u0006\u0012\u0004\u0018\u00010%0#ø\u0001\u0000¢\u0006\u0002\u0010&J6\u0010'\u001a\u00020\u0012*\u00020!2\"\u0010\"\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0$\u0012\u0006\u0012\u0004\u0018\u00010%0#ø\u0001\u0000¢\u0006\u0002\u0010&R\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0084.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006("}, d2 = {"Lcom/htmake/reader/verticle/RestVerticle;", "Lio/vertx/kotlin/coroutines/CoroutineVerticle;", "()V", "port", PackageDocumentBase.PREFIX_OPF, "getPort", "()I", "setPort", "(I)V", "router", "Lio/vertx/ext/web/Router;", "getRouter", "()Lio/vertx/ext/web/Router;", "setRouter", "(Lio/vertx/ext/web/Router;)V", "getContextPath", PackageDocumentBase.PREFIX_OPF, "initRouter", PackageDocumentBase.PREFIX_OPF, "(Lio/vertx/ext/web/Router;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onException", "error", PackageDocumentBase.PREFIX_OPF, "onHandlerError", "ctx", "Lio/vertx/ext/web/RoutingContext;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onStartError", "start", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "started", "coroutineHandler", "Lio/vertx/ext/web/Route;", "fn", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", PackageDocumentBase.PREFIX_OPF, "(Lio/vertx/ext/web/Route;Lkotlin/jvm/functions/Function2;)V", "coroutineHandlerWithoutRes", "reader-pro"})
public abstract class RestVerticle extends CoroutineVerticle {
    protected Router router;
    private int port = 8080;

    /* JADX INFO: renamed from: com.htmake.reader.verticle.RestVerticle$start$1, reason: invalid class name */
    /* JADX INFO: compiled from: RestVerticle.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/verticle/RestVerticle$start$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RestVerticle.kt", l = {40, 96}, i = {0, 1}, s = {"L$0", "L$0"}, n = {"this", "this"}, m = "start$suspendImpl", c = "com.htmake.reader.verticle.RestVerticle")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RestVerticle.start$suspendImpl(RestVerticle.this, (Continuation) this);
        }
    }

    @Nullable
    protected Object start(@NotNull Continuation<? super Unit> $completion) {
        return start$suspendImpl(this, $completion);
    }

    @Nullable
    public abstract Object initRouter(@NotNull Router router, @NotNull Continuation<? super Unit> $completion);

    @NotNull
    public abstract String getContextPath();

    @NotNull
    protected final Router getRouter() {
        Router router = this.router;
        if (router != null) {
            return router;
        }
        Intrinsics.throwUninitializedPropertyAccessException("router");
        throw null;
    }

    protected final void setRouter(@NotNull Router router) {
        Intrinsics.checkNotNullParameter(router, "<set-?>");
        this.router = router;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int i) {
        this.port = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x01b1  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x01f9  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0201  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0229  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static /* synthetic */ Object start$suspendImpl(RestVerticle restVerticle, Continuation continuation) {
        AnonymousClass1 anonymousClass1;
        String contextPath;
        Router mainRouter;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = restVerticle.new AnonymousClass1(continuation);
            }
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                anonymousClass1.L$0 = restVerticle;
                anonymousClass1.label = 1;
                if (super.start(anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Router router = Router.router(restVerticle.getVertx());
                Intrinsics.checkNotNullExpressionValue(router, "router(vertx)");
                restVerticle.setRouter(router);
                String cookieName = "reader.session";
                Route route = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route, "router.route()");
                Handler sessionCookiePath = SessionHandler.create(LocalSessionStore.create(restVerticle.getVertx())).setSessionCookieName("reader.session").setSessionTimeout(604800000L).setSessionCookiePath(TableOfContents.DEFAULT_PATH_SEPARATOR);
                Intrinsics.checkNotNullExpressionValue(sessionCookiePath, "create(LocalSessionStore.create(vertx))\n                            .setSessionCookieName(cookieName)\n                            .setSessionTimeout(7L * 86400 * 1000)\n                            .setSessionCookiePath(\"/\")");
                VertExtKt.globalHandler(route, sessionCookiePath);
                Route route2 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route2, "router.route()");
                VertExtKt.globalHandler(route2, (v1) -> {
                    m100start$lambda1(r1, v1);
                });
                Route route3 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route3, "router.route()");
                VertExtKt.globalHandler(route3, (v1) -> {
                    m102start$lambda3(r1, v1);
                });
                Route route4 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route4, "router.route()");
                Handler handlerCreate = BodyHandler.create();
                Intrinsics.checkNotNullExpressionValue(handlerCreate, "create()");
                VertExtKt.globalHandler(route4, handlerCreate);
                Route route5 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route5, "router.route()");
                Handler handlerCreate2 = LoggerHandler.create(LoggerFormat.DEFAULT);
                Intrinsics.checkNotNullExpressionValue(handlerCreate2, "create(LoggerFormat.DEFAULT)");
                VertExtKt.globalHandler(route5, handlerCreate2);
                Route route6 = restVerticle.getRouter().route("/reader3/*");
                Intrinsics.checkNotNullExpressionValue(route6, "router.route(\"/reader3/*\")");
                VertExtKt.globalHandler(route6, RestVerticle::m103start$lambda4);
                Route route7 = restVerticle.getRouter().get("/health");
                Intrinsics.checkNotNullExpressionValue(route7, "router.get(\"/health\")");
                VertExtKt.globalHandler(route7, RestVerticle::m104start$lambda5);
                anonymousClass1.L$0 = restVerticle;
                anonymousClass1.label = 2;
                if (restVerticle.initRouter(restVerticle.getRouter(), anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                restVerticle.getRouter().route().last().failureHandler(RestVerticle::m105start$lambda6);
                contextPath = restVerticle.getContextPath();
                if (contextPath.length() > 0) {
                    Router router2 = Router.router(restVerticle.getVertx());
                    Intrinsics.checkNotNullExpressionValue(router2, "router(vertx)");
                    mainRouter = router2;
                    mainRouter.mountSubRouter(ExtKt.toDir(contextPath, true), restVerticle.getRouter());
                } else {
                    mainRouter = restVerticle.getRouter();
                }
                RestVerticleKt.logger.info("port: {}", Boxing.boxInt(restVerticle.getPort()));
                RestVerticle restVerticle2 = restVerticle;
                RestVerticle restVerticle3 = restVerticle;
                restVerticle.getVertx().createHttpServer().requestHandler((Handler) mainRouter).exceptionHandler((v1) -> {
                    m106start$lambda7(r1, v1);
                }).listen(restVerticle.getPort(), (v1) -> {
                    m107start$lambda8(r2, v1);
                });
                return Unit.INSTANCE;
            case 1:
                restVerticle = (RestVerticle) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                Router router3 = Router.router(restVerticle.getVertx());
                Intrinsics.checkNotNullExpressionValue(router3, "router(vertx)");
                restVerticle.setRouter(router3);
                String cookieName2 = "reader.session";
                Route route8 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route8, "router.route()");
                Handler sessionCookiePath2 = SessionHandler.create(LocalSessionStore.create(restVerticle.getVertx())).setSessionCookieName("reader.session").setSessionTimeout(604800000L).setSessionCookiePath(TableOfContents.DEFAULT_PATH_SEPARATOR);
                Intrinsics.checkNotNullExpressionValue(sessionCookiePath2, "create(LocalSessionStore.create(vertx))\n                            .setSessionCookieName(cookieName)\n                            .setSessionTimeout(7L * 86400 * 1000)\n                            .setSessionCookiePath(\"/\")");
                VertExtKt.globalHandler(route8, sessionCookiePath2);
                Route route22 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route22, "router.route()");
                VertExtKt.globalHandler(route22, (v1) -> {
                    m100start$lambda1(r1, v1);
                });
                Route route32 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route32, "router.route()");
                VertExtKt.globalHandler(route32, (v1) -> {
                    m102start$lambda3(r1, v1);
                });
                Route route42 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route42, "router.route()");
                Handler handlerCreate3 = BodyHandler.create();
                Intrinsics.checkNotNullExpressionValue(handlerCreate3, "create()");
                VertExtKt.globalHandler(route42, handlerCreate3);
                Route route52 = restVerticle.getRouter().route();
                Intrinsics.checkNotNullExpressionValue(route52, "router.route()");
                Handler handlerCreate22 = LoggerHandler.create(LoggerFormat.DEFAULT);
                Intrinsics.checkNotNullExpressionValue(handlerCreate22, "create(LoggerFormat.DEFAULT)");
                VertExtKt.globalHandler(route52, handlerCreate22);
                Route route62 = restVerticle.getRouter().route("/reader3/*");
                Intrinsics.checkNotNullExpressionValue(route62, "router.route(\"/reader3/*\")");
                VertExtKt.globalHandler(route62, RestVerticle::m103start$lambda4);
                Route route72 = restVerticle.getRouter().get("/health");
                Intrinsics.checkNotNullExpressionValue(route72, "router.get(\"/health\")");
                VertExtKt.globalHandler(route72, RestVerticle::m104start$lambda5);
                anonymousClass1.L$0 = restVerticle;
                anonymousClass1.label = 2;
                if (restVerticle.initRouter(restVerticle.getRouter(), anonymousClass1) == coroutine_suspended) {
                }
                restVerticle.getRouter().route().last().failureHandler(RestVerticle::m105start$lambda6);
                contextPath = restVerticle.getContextPath();
                if (contextPath.length() > 0) {
                }
                RestVerticleKt.logger.info("port: {}", Boxing.boxInt(restVerticle.getPort()));
                RestVerticle restVerticle22 = restVerticle;
                RestVerticle restVerticle32 = restVerticle;
                restVerticle.getVertx().createHttpServer().requestHandler((Handler) mainRouter).exceptionHandler((v1) -> {
                    m106start$lambda7(r1, v1);
                }).listen(restVerticle.getPort(), (v1) -> {
                    m107start$lambda8(r2, v1);
                });
                return Unit.INSTANCE;
            case 2:
                restVerticle = (RestVerticle) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                restVerticle.getRouter().route().last().failureHandler(RestVerticle::m105start$lambda6);
                contextPath = restVerticle.getContextPath();
                if (contextPath.length() > 0) {
                }
                RestVerticleKt.logger.info("port: {}", Boxing.boxInt(restVerticle.getPort()));
                RestVerticle restVerticle222 = restVerticle;
                RestVerticle restVerticle322 = restVerticle;
                restVerticle.getVertx().createHttpServer().requestHandler((Handler) mainRouter).exceptionHandler((v1) -> {
                    m106start$lambda7(r1, v1);
                }).listen(restVerticle.getPort(), (v1) -> {
                    m107start$lambda8(r2, v1);
                });
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: start$lambda-1, reason: not valid java name */
    private static final void m100start$lambda1(String $cookieName, RoutingContext it) {
        Intrinsics.checkNotNullParameter($cookieName, "$cookieName");
        it.addHeadersEndHandler((v2) -> {
            m99start$lambda1$lambda0(r1, r2, v2);
        });
        it.next();
    }

    /* JADX INFO: renamed from: start$lambda-1$lambda-0, reason: not valid java name */
    private static final void m99start$lambda1$lambda0(RoutingContext $it, String $cookieName, Void $noName_0) {
        Intrinsics.checkNotNullParameter($cookieName, "$cookieName");
        Cookie cookie = $it.getCookie($cookieName);
        if (cookie != null) {
            cookie.setMaxAge(172800000L);
            cookie.setPath(TableOfContents.DEFAULT_PATH_SEPARATOR);
        }
    }

    /* JADX INFO: renamed from: start$lambda-3, reason: not valid java name */
    private static final void m102start$lambda3(String $cookieName, RoutingContext it) {
        Intrinsics.checkNotNullParameter($cookieName, "$cookieName");
        it.addHeadersEndHandler((v1) -> {
            m101start$lambda3$lambda2(r1, v1);
        });
        String origin = it.request().getHeader("Origin");
        if (origin != null) {
            if ((origin.length() > 0) && it.request().method() == HttpMethod.OPTIONS) {
                it.removeCookie($cookieName);
                Intrinsics.checkNotNullExpressionValue(it, "it");
                VertExtKt.success(it, PackageDocumentBase.PREFIX_OPF);
                return;
            }
        }
        it.next();
    }

    /* JADX INFO: renamed from: start$lambda-3$lambda-2, reason: not valid java name */
    private static final void m101start$lambda3$lambda2(RoutingContext $it, Void $noName_0) {
        String origin = $it.request().getHeader("Origin");
        if (origin != null) {
            if (origin.length() > 0) {
                HttpServerResponse res = $it.response();
                res.putHeader("Access-Control-Allow-Origin", origin);
                res.putHeader("Access-Control-Allow-Credentials", "true");
                res.putHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE");
                res.putHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, X-Requested-With");
            }
        }
    }

    /* JADX INFO: renamed from: start$lambda-4, reason: not valid java name */
    private static final void m103start$lambda4(RoutingContext it) {
        String rawMethod = it.request().rawMethod();
        RestVerticleKt.logger.info("{} {}", rawMethod, URLDecoder.decode(it.request().absoluteURI(), Constants.CHARACTER_ENCODING));
        if (!rawMethod.equals("PUT") && ((it.fileUploads() == null || it.fileUploads().isEmpty()) && it.getBodyAsString() != null && it.getBodyAsString().length() > 0 && it.getBodyAsString().length() < 1000)) {
            RestVerticleKt.logger.info("Request body: {}", it.getBodyAsString());
        }
        it.next();
    }

    /* JADX INFO: renamed from: start$lambda-5, reason: not valid java name */
    private static final void m104start$lambda5(RoutingContext it) {
        Intrinsics.checkNotNullExpressionValue(it, "it");
        VertExtKt.success(it, "ok!");
    }

    /* JADX INFO: renamed from: start$lambda-6, reason: not valid java name */
    private static final void m105start$lambda6(RoutingContext ctx) throws UnsupportedEncodingException {
        Intrinsics.checkNotNullExpressionValue(ctx, "ctx");
        Throwable thFailure = ctx.failure();
        Intrinsics.checkNotNullExpressionValue(thFailure, "ctx.failure()");
        VertExtKt.error(ctx, thFailure);
    }

    /* JADX INFO: renamed from: start$lambda-7, reason: not valid java name */
    private static final void m106start$lambda7(RestVerticle this$0, Throwable error) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullExpressionValue(error, "error");
        this$0.onException(error);
    }

    /* JADX INFO: renamed from: start$lambda-8, reason: not valid java name */
    private static final void m107start$lambda8(RestVerticle this$0, AsyncResult res) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (res.succeeded()) {
            RestVerticleKt.logger.info("Server running at: http://localhost:{}", Integer.valueOf(this$0.getPort()));
            RestVerticleKt.logger.info("Web reader running at: http://localhost:{}", Integer.valueOf(this$0.getPort()));
            System.out.println((Object) "ReaderApplication Started");
            this$0.started();
            return;
        }
        this$0.onStartError();
    }

    public void onException(@NotNull Throwable error) {
        Intrinsics.checkNotNullParameter(error, "error");
        RestVerticleKt.logger.error("vertx exception: {}", error);
    }

    public void onStartError() {
    }

    public void started() {
    }

    public void onHandlerError(@NotNull RoutingContext ctx, @NotNull Exception error) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(error, "error");
        RestVerticleKt.logger.error("Error: {}", error);
        VertExtKt.error(ctx, error);
    }

    public final void coroutineHandler(@NotNull Route $this$coroutineHandler, @NotNull Function2<? super RoutingContext, ? super Continuation<Object>, ? extends Object> fn) {
        Intrinsics.checkNotNullParameter($this$coroutineHandler, "<this>");
        Intrinsics.checkNotNullParameter(fn, "fn");
        VertExtKt.globalHandler($this$coroutineHandler, (v2) -> {
            m109coroutineHandler$lambda10(r1, r2, v2);
        });
    }

    /* JADX INFO: renamed from: coroutineHandler$lambda-10, reason: not valid java name */
    private static final void m109coroutineHandler$lambda10(RestVerticle this$0, Function2 $fn, RoutingContext ctx) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter($fn, "$fn");
        Ref.ObjectRef job = new Ref.ObjectRef();
        ctx.request().connection().closeHandler((v1) -> {
            m108coroutineHandler$lambda10$lambda9(r1, v1);
        });
        job.element = BuildersKt.launch$default((CoroutineScope) this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new RestVerticle$coroutineHandler$1$2(ctx, $fn, this$0, null), 2, (Object) null);
    }

    /* JADX INFO: renamed from: coroutineHandler$lambda-10$lambda-9, reason: not valid java name */
    private static final void m108coroutineHandler$lambda10$lambda9(Ref.ObjectRef $job, Void it) {
        Intrinsics.checkNotNullParameter($job, "$job");
        RestVerticleKt.logger.info("客户端已断开链接，终止运行");
        Job job = (Job) $job.element;
        if (job == null) {
            return;
        }
        Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
    }

    public final void coroutineHandlerWithoutRes(@NotNull Route $this$coroutineHandlerWithoutRes, @NotNull Function2<? super RoutingContext, ? super Continuation<Object>, ? extends Object> fn) {
        Intrinsics.checkNotNullParameter($this$coroutineHandlerWithoutRes, "<this>");
        Intrinsics.checkNotNullParameter(fn, "fn");
        VertExtKt.globalHandler($this$coroutineHandlerWithoutRes, (v2) -> {
            m111coroutineHandlerWithoutRes$lambda12(r1, r2, v2);
        });
    }

    /* JADX INFO: renamed from: coroutineHandlerWithoutRes$lambda-12, reason: not valid java name */
    private static final void m111coroutineHandlerWithoutRes$lambda12(RestVerticle this$0, Function2 $fn, RoutingContext ctx) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter($fn, "$fn");
        Ref.ObjectRef job = new Ref.ObjectRef();
        ctx.request().connection().closeHandler((v1) -> {
            m110coroutineHandlerWithoutRes$lambda12$lambda11(r1, v1);
        });
        job.element = BuildersKt.launch$default((CoroutineScope) this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new RestVerticle$coroutineHandlerWithoutRes$1$2($fn, ctx, this$0, null), 2, (Object) null);
    }

    /* JADX INFO: renamed from: coroutineHandlerWithoutRes$lambda-12$lambda-11, reason: not valid java name */
    private static final void m110coroutineHandlerWithoutRes$lambda12$lambda11(Ref.ObjectRef $job, Void it) {
        Intrinsics.checkNotNullParameter($job, "$job");
        RestVerticleKt.logger.info("客户端已断开链接，终止运行");
        Job job = (Job) $job.element;
        if (job == null) {
            return;
        }
        Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
    }
}
