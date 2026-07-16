// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api;

import io.vertx.core.http.HttpServerResponse;
import java.net.URLDecoder;
import com.htmake.reader.config.BookConfig;
import io.vertx.core.net.impl.URIDecoder;
import kotlin.text.StringsKt;
import com.htmake.reader.entity.License;
import org.springframework.scheduling.annotation.Scheduled;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.Map;
import kotlinx.coroutines.slf4j.MDCContext;
import org.slf4j.MDC;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import kotlin.Pair;
import com.htmake.reader.entity.User;
import kotlinx.coroutines.CoroutineScope;
import kotlin.jvm.functions.Function3;
import java.util.Calendar;
import kotlin.jvm.internal.Ref$IntRef;
import com.htmake.reader.utils.VertExtKt;
import com.htmake.reader.SpringEvent;
import org.springframework.context.ApplicationEvent;
import com.htmake.reader.utils.SpringContextUtils;
import kotlin.coroutines.jvm.internal.Boxing;
import io.vertx.ext.web.Route;
import com.htmake.reader.api.controller.HttpTTSController;
import com.htmake.reader.api.controller.LicenseController;
import com.htmake.reader.api.controller.FileController;
import com.htmake.reader.api.controller.BookmarkController;
import com.htmake.reader.api.controller.ReplaceRuleController;
import com.htmake.reader.api.controller.WebdavController;
import com.htmake.reader.api.controller.UserController;
import com.htmake.reader.api.controller.RssSourceController;
import com.htmake.reader.api.controller.BookSourceController;
import com.htmake.reader.api.controller.BookGroupController;
import com.htmake.reader.api.controller.BookController;
import io.vertx.ext.web.RoutingContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Ref$ObjectRef;
import java.nio.charset.Charset;
import kotlin.io.FilesKt;
import java.io.File;
import com.htmake.reader.utils.ExtKt;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.Handler;
import com.htmake.reader.init.ReaderAdapter;
import io.legado.app.adapters.ReaderAdapterInterface;
import io.legado.app.adapters.ReaderAdapterHelper;
import com.htmake.reader.utils.RemoteWebview;
import com.htmake.reader.utils.MongoManager;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.Nullable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import io.vertx.ext.web.Router;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import com.htmake.reader.config.AppConfig;
import kotlin.Metadata;
import org.springframework.stereotype.Component;
import com.htmake.reader.verticle.RestVerticle;

@Component
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0017\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0017J\b\u0010\t\u001a\u00020\bH\u0017J\b\u0010\n\u001a\u00020\bH\u0017J\b\u0010\u000b\u001a\u00020\bH\u0017J\b\u0010\f\u001a\u00020\rH\u0016J\u0019\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0012J\u0019\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\bH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u00112\n\u0010\u001b\u001a\u00060\u001cj\u0002`\u001dH\u0016J\b\u0010\u001e\u001a\u00020\bH\u0016J\b\u0010\u001f\u001a\u00020\bH\u0017J\u0011\u0010 \u001a\u00020\bH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\b\u0010!\u001a\u00020\bH\u0017J\b\u0010\"\u001a\u00020\bH\u0016R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.?\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00068\u0002@\u0002X\u0083.?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006#" }, d2 = { "Lcom/htmake/reader/api/YueduApi;", "Lcom/htmake/reader/verticle/RestVerticle;", "()V", "appConfig", "Lcom/htmake/reader/config/AppConfig;", "env", "Lorg/springframework/core/env/Environment;", "autoBackup", "", "autoGC", "checkLicense", "clearUser", "getContextPath", "", "getSystemInfo", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initRouter", "router", "Lio/vertx/ext/web/Router;", "(Lio/vertx/ext/web/Router;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "migration", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onHandlerError", "ctx", "error", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onStartError", "remoteBookSourceSubUpdateJob", "setupPort", "shelfUpdateJob", "started", "reader-pro" })
public class YueduApi extends RestVerticle
{
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private Environment env;
    
    @Nullable
    @Override
    public Object initRouter(@NotNull final Router router, @NotNull final Continuation<? super Unit> $completion) {
        return initRouter$suspendImpl(this, router, $completion);
    }
    
    static /* synthetic */ Object initRouter$suspendImpl(YueduApi this, Router router, final Continuation continuation) {
        final Continuation $continuation;
        Label_0050: {
            if (continuation instanceof YueduApi$initRouter.YueduApi$initRouter$1) {
                final YueduApi$initRouter.YueduApi$initRouter$1 yueduApi$initRouter$1 = (YueduApi$initRouter.YueduApi$initRouter$1)continuation;
                if ((yueduApi$initRouter$1.label & Integer.MIN_VALUE) != 0x0) {
                    final YueduApi$initRouter.YueduApi$initRouter$1 yueduApi$initRouter$2 = yueduApi$initRouter$1;
                    yueduApi$initRouter$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new YueduApi$initRouter.YueduApi$initRouter$1(this, continuation);
        }
        final Object $result = ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0366: {
            switch (((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final YueduApi yueduApi = this;
                    final Continuation $completion = $continuation;
                    ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$0 = this;
                    ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$1 = router;
                    ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).label = 1;
                    if (yueduApi.setupPort((Continuation<? super Unit>)$completion) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    router = (Router)((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$1;
                    this = (YueduApi)((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                case 2: {
                    router = (Router)((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$1;
                    this = (YueduApi)((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0366;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            final AppConfig appConfig = this.appConfig;
            if (appConfig == null) {
                Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                throw null;
            }
            if (appConfig.getMongoUri().length() > 0) {
                final MongoManager instance = MongoManager.INSTANCE;
                final AppConfig appConfig2 = this.appConfig;
                if (appConfig2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                    throw null;
                }
                instance.connect(appConfig2.getMongoUri());
            }
            final AppConfig appConfig3 = this.appConfig;
            if (appConfig3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                throw null;
            }
            if (appConfig3.getRemoteWebviewApi().length() > 0) {
                final RemoteWebview instance2 = RemoteWebview.INSTANCE;
                final AppConfig appConfig4 = this.appConfig;
                if (appConfig4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                    throw null;
                }
                instance2.setRemoteApi(appConfig4.getRemoteWebviewApi());
            }
            ReaderAdapterHelper.INSTANCE.setAdapter(ReaderAdapter.INSTANCE);
            final YueduApi yueduApi2 = this;
            final Continuation $completion2 = $continuation;
            ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$0 = this;
            ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).L$1 = router;
            ((YueduApi$initRouter.YueduApi$initRouter$1)$continuation).label = 2;
            if (yueduApi2.migration((Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        router.route("/*").handler((Handler)StaticHandler.create("web").setDefaultContentEncoding("UTF-8"));
        final String assetsDir = ExtKt.getWorkDir("storage", "assets");
        final File assetsDirFile = new File(assetsDir);
        if (!assetsDirFile.exists()) {
            assetsDirFile.mkdirs();
        }
        final String assetsCss = ExtKt.getWorkDir("storage", "assets", "reader.css");
        final File assetsCssFile = new File(assetsCss);
        if (!assetsCssFile.exists()) {
            FilesKt.writeText$default(assetsCssFile, "/* \u5728\u6b64\u5904\u53ef\u4ee5\u7f16\u5199CSS\u6837\u5f0f\u6765\u81ea\u5b9a\u4e49\u9875\u9762 */", (Charset)null, 2, (Object)null);
        }
        router.route("/assets/*").handler((Handler)StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot(assetsDir).setDefaultContentEncoding("UTF-8"));
        final Ref$ObjectRef dataDir = new Ref$ObjectRef();
        dataDir.element = ExtKt.getWorkDir("storage", "data");
        router.route("/book-assets/*").handler(YueduApi::initRouter$lambda-0);
        router.route("/book-assets/*").handler((Handler)StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot((String)dataDir.element).setDefaultContentEncoding("UTF-8"));
        router.route("/epub/*").handler(YueduApi::initRouter$lambda-1);
        router.route("/epub/*").handler((Handler)StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot((String)dataDir.element).setDefaultContentEncoding("UTF-8"));
        router.route("/simple-web").handler(YueduApi::initRouter$lambda-2);
        router.route("/simple-web/*").handler(YueduApi::initRouter$lambda-3);
        router.route("/simple-web/*").handler((Handler)StaticHandler.create("simple-web").setDefaultContentEncoding("UTF-8"));
        final YueduApi yueduApi3 = this;
        final Route value = router.get("/reader3/getSystemInfo");
        Intrinsics.checkNotNullExpressionValue((Object)value, "router.get(\"/reader3/getSystemInfo\")");
        yueduApi3.coroutineHandler(value, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$6(this, (Continuation)null));
        final BookController bookController = new BookController(this.getCoroutineContext());
        final BookGroupController bookGroupController = new BookGroupController(this.getCoroutineContext());
        final BookSourceController bookSourceController = new BookSourceController(this.getCoroutineContext());
        final RssSourceController rssSourceController = new RssSourceController(this.getCoroutineContext());
        final UserController userController = new UserController(this.getCoroutineContext());
        final WebdavController webdavController = new WebdavController(this.getCoroutineContext(), router, (Function2<? super RoutingContext, ? super Exception, Unit>)new YueduApi$initRouter$webdavController.YueduApi$initRouter$webdavController$1(this));
        final ReplaceRuleController replaceRuleController = new ReplaceRuleController(this.getCoroutineContext());
        final BookmarkController bookmarkController = new BookmarkController(this.getCoroutineContext());
        final FileController fileController = new FileController(this.getCoroutineContext());
        final LicenseController licenseController = new LicenseController(this.getCoroutineContext());
        final HttpTTSController httpTTSController = new HttpTTSController(this.getCoroutineContext());
        final YueduApi yueduApi4 = this;
        final Route post = router.post("/reader3/saveBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post, "router.post(\"/reader3/saveBookSource\")");
        yueduApi4.coroutineHandler(post, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$7(bookSourceController, (Continuation)null));
        final YueduApi yueduApi5 = this;
        final Route post2 = router.post("/reader3/saveBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)post2, "router.post(\"/reader3/saveBookSources\")");
        yueduApi5.coroutineHandler(post2, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$8(bookSourceController, (Continuation)null));
        final YueduApi yueduApi6 = this;
        final Route value2 = router.get("/reader3/getBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)value2, "router.get(\"/reader3/getBookSource\")");
        yueduApi6.coroutineHandler(value2, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$9(bookSourceController, (Continuation)null));
        final YueduApi yueduApi7 = this;
        final Route post3 = router.post("/reader3/getBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post3, "router.post(\"/reader3/getBookSource\")");
        yueduApi7.coroutineHandler(post3, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$10(bookSourceController, (Continuation)null));
        final YueduApi yueduApi8 = this;
        final Route value3 = router.get("/reader3/getBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)value3, "router.get(\"/reader3/getBookSources\")");
        yueduApi8.coroutineHandler(value3, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$11(bookSourceController, (Continuation)null));
        final YueduApi yueduApi9 = this;
        final Route post4 = router.post("/reader3/getBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)post4, "router.post(\"/reader3/getBookSources\")");
        yueduApi9.coroutineHandler(post4, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$12(bookSourceController, (Continuation)null));
        final YueduApi yueduApi10 = this;
        final Route post5 = router.post("/reader3/deleteAllBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)post5, "router.post(\"/reader3/deleteAllBookSources\")");
        yueduApi10.coroutineHandler(post5, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$13(bookSourceController, (Continuation)null));
        final YueduApi yueduApi11 = this;
        final Route post6 = router.post("/reader3/deleteBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post6, "router.post(\"/reader3/deleteBookSource\")");
        yueduApi11.coroutineHandler(post6, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$14(bookSourceController, (Continuation)null));
        final YueduApi yueduApi12 = this;
        final Route post7 = router.post("/reader3/deleteBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)post7, "router.post(\"/reader3/deleteBookSources\")");
        yueduApi12.coroutineHandler(post7, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$15(bookSourceController, (Continuation)null));
        final YueduApi yueduApi13 = this;
        final Route post8 = router.post("/reader3/readSourceFile");
        Intrinsics.checkNotNullExpressionValue((Object)post8, "router.post(\"/reader3/readSourceFile\")");
        yueduApi13.coroutineHandler(post8, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$16(bookSourceController, (Continuation)null));
        final YueduApi yueduApi14 = this;
        final Route post9 = router.post("/reader3/saveFromRemoteSource");
        Intrinsics.checkNotNullExpressionValue((Object)post9, "router.post(\"/reader3/saveFromRemoteSource\")");
        yueduApi14.coroutineHandlerWithoutRes(post9, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$17(bookSourceController, (Continuation)null));
        final YueduApi yueduApi15 = this;
        final Route post10 = router.post("/reader3/setAsDefaultBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)post10, "router.post(\"/reader3/setAsDefaultBookSources\")");
        yueduApi15.coroutineHandler(post10, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$18(bookSourceController, (Continuation)null));
        final YueduApi yueduApi16 = this;
        final Route post11 = router.post("/reader3/deleteUserBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post11, "router.post(\"/reader3/deleteUserBookSource\")");
        yueduApi16.coroutineHandler(post11, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$19(bookSourceController, (Continuation)null));
        final YueduApi yueduApi17 = this;
        final Route post12 = router.post("/reader3/deleteBookSourcesFile");
        Intrinsics.checkNotNullExpressionValue((Object)post12, "router.post(\"/reader3/deleteBookSourcesFile\")");
        yueduApi17.coroutineHandler(post12, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$20(bookSourceController, (Continuation)null));
        final YueduApi yueduApi18 = this;
        final Route value4 = router.get("/reader3/getBookshelf");
        Intrinsics.checkNotNullExpressionValue((Object)value4, "router.get(\"/reader3/getBookshelf\")");
        yueduApi18.coroutineHandler(value4, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$21(bookController, (Continuation)null));
        final YueduApi yueduApi19 = this;
        final Route value5 = router.get("/reader3/getShelfBook");
        Intrinsics.checkNotNullExpressionValue((Object)value5, "router.get(\"/reader3/getShelfBook\")");
        yueduApi19.coroutineHandler(value5, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$22(bookController, (Continuation)null));
        final YueduApi yueduApi20 = this;
        final Route post13 = router.post("/reader3/saveBook");
        Intrinsics.checkNotNullExpressionValue((Object)post13, "router.post(\"/reader3/saveBook\")");
        yueduApi20.coroutineHandler(post13, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$23(bookController, (Continuation)null));
        final YueduApi yueduApi21 = this;
        final Route post14 = router.post("/reader3/deleteBook");
        Intrinsics.checkNotNullExpressionValue((Object)post14, "router.post(\"/reader3/deleteBook\")");
        yueduApi21.coroutineHandler(post14, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$24(bookController, (Continuation)null));
        final YueduApi yueduApi22 = this;
        final Route post15 = router.post("/reader3/deleteBooks");
        Intrinsics.checkNotNullExpressionValue((Object)post15, "router.post(\"/reader3/deleteBooks\")");
        yueduApi22.coroutineHandler(post15, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$25(bookController, (Continuation)null));
        final YueduApi yueduApi23 = this;
        final Route post16 = router.post("/reader3/getInvalidBookSources");
        Intrinsics.checkNotNullExpressionValue((Object)post16, "router.post(\"/reader3/getInvalidBookSources\")");
        yueduApi23.coroutineHandler(post16, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$26(bookController, (Continuation)null));
        final YueduApi yueduApi24 = this;
        final Route post17 = router.post("/reader3/exploreBook");
        Intrinsics.checkNotNullExpressionValue((Object)post17, "router.post(\"/reader3/exploreBook\")");
        yueduApi24.coroutineHandler(post17, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$27(bookController, (Continuation)null));
        final YueduApi yueduApi25 = this;
        final Route value6 = router.get("/reader3/exploreBook");
        Intrinsics.checkNotNullExpressionValue((Object)value6, "router.get(\"/reader3/exploreBook\")");
        yueduApi25.coroutineHandler(value6, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$28(bookController, (Continuation)null));
        final YueduApi yueduApi26 = this;
        final Route value7 = router.get("/reader3/searchBook");
        Intrinsics.checkNotNullExpressionValue((Object)value7, "router.get(\"/reader3/searchBook\")");
        yueduApi26.coroutineHandler(value7, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$29(bookController, (Continuation)null));
        final YueduApi yueduApi27 = this;
        final Route post18 = router.post("/reader3/searchBook");
        Intrinsics.checkNotNullExpressionValue((Object)post18, "router.post(\"/reader3/searchBook\")");
        yueduApi27.coroutineHandler(post18, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$30(bookController, (Continuation)null));
        final YueduApi yueduApi28 = this;
        final Route value8 = router.get("/reader3/searchBookMulti");
        Intrinsics.checkNotNullExpressionValue((Object)value8, "router.get(\"/reader3/searchBookMulti\")");
        yueduApi28.coroutineHandler(value8, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$31(bookController, (Continuation)null));
        final YueduApi yueduApi29 = this;
        final Route post19 = router.post("/reader3/searchBookMulti");
        Intrinsics.checkNotNullExpressionValue((Object)post19, "router.post(\"/reader3/searchBookMulti\")");
        yueduApi29.coroutineHandler(post19, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$32(bookController, (Continuation)null));
        final YueduApi yueduApi30 = this;
        final Route value9 = router.get("/reader3/searchBookMultiSSE");
        Intrinsics.checkNotNullExpressionValue((Object)value9, "router.get(\"/reader3/searchBookMultiSSE\")");
        yueduApi30.coroutineHandlerWithoutRes(value9, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$33(bookController, (Continuation)null));
        final YueduApi yueduApi31 = this;
        final Route value10 = router.get("/reader3/getBookInfo");
        Intrinsics.checkNotNullExpressionValue((Object)value10, "router.get(\"/reader3/getBookInfo\")");
        yueduApi31.coroutineHandler(value10, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$34(bookController, (Continuation)null));
        final YueduApi yueduApi32 = this;
        final Route post20 = router.post("/reader3/getBookInfo");
        Intrinsics.checkNotNullExpressionValue((Object)post20, "router.post(\"/reader3/getBookInfo\")");
        yueduApi32.coroutineHandler(post20, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$35(bookController, (Continuation)null));
        final YueduApi yueduApi33 = this;
        final Route value11 = router.get("/reader3/getChapterList");
        Intrinsics.checkNotNullExpressionValue((Object)value11, "router.get(\"/reader3/getChapterList\")");
        yueduApi33.coroutineHandler(value11, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$36(bookController, (Continuation)null));
        final YueduApi yueduApi34 = this;
        final Route post21 = router.post("/reader3/getChapterList");
        Intrinsics.checkNotNullExpressionValue((Object)post21, "router.post(\"/reader3/getChapterList\")");
        yueduApi34.coroutineHandler(post21, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$37(bookController, (Continuation)null));
        final YueduApi yueduApi35 = this;
        final Route value12 = router.get("/reader3/getBookContent");
        Intrinsics.checkNotNullExpressionValue((Object)value12, "router.get(\"/reader3/getBookContent\")");
        yueduApi35.coroutineHandler(value12, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$38(bookController, (Continuation)null));
        final YueduApi yueduApi36 = this;
        final Route post22 = router.post("/reader3/getBookContent");
        Intrinsics.checkNotNullExpressionValue((Object)post22, "router.post(\"/reader3/getBookContent\")");
        yueduApi36.coroutineHandler(post22, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$39(bookController, (Continuation)null));
        final YueduApi yueduApi37 = this;
        final Route post23 = router.post("/reader3/saveBookContent");
        Intrinsics.checkNotNullExpressionValue((Object)post23, "router.post(\"/reader3/saveBookContent\")");
        yueduApi37.coroutineHandler(post23, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$40(bookController, (Continuation)null));
        final YueduApi yueduApi38 = this;
        final Route post24 = router.post("/reader3/saveBookProgress");
        Intrinsics.checkNotNullExpressionValue((Object)post24, "router.post(\"/reader3/saveBookProgress\")");
        yueduApi38.coroutineHandler(post24, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$41(bookController, (Continuation)null));
        final YueduApi yueduApi39 = this;
        final Route value13 = router.get("/reader3/cover");
        Intrinsics.checkNotNullExpressionValue((Object)value13, "router.get(\"/reader3/cover\")");
        yueduApi39.coroutineHandlerWithoutRes(value13, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$42(bookController, (Continuation)null));
        final YueduApi yueduApi40 = this;
        final Route value14 = router.get("/reader3/searchBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)value14, "router.get(\"/reader3/searchBookSource\")");
        yueduApi40.coroutineHandler(value14, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$43(bookController, (Continuation)null));
        final YueduApi yueduApi41 = this;
        final Route post25 = router.post("/reader3/searchBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post25, "router.post(\"/reader3/searchBookSource\")");
        yueduApi41.coroutineHandler(post25, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$44(bookController, (Continuation)null));
        final YueduApi yueduApi42 = this;
        final Route value15 = router.get("/reader3/getAvailableBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)value15, "router.get(\"/reader3/getAvailableBookSource\")");
        yueduApi42.coroutineHandler(value15, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$45(bookController, (Continuation)null));
        final YueduApi yueduApi43 = this;
        final Route post26 = router.post("/reader3/getAvailableBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post26, "router.post(\"/reader3/getAvailableBookSource\")");
        yueduApi43.coroutineHandler(post26, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$46(bookController, (Continuation)null));
        final YueduApi yueduApi44 = this;
        final Route value16 = router.get("/reader3/searchBookSourceSSE");
        Intrinsics.checkNotNullExpressionValue((Object)value16, "router.get(\"/reader3/searchBookSourceSSE\")");
        yueduApi44.coroutineHandlerWithoutRes(value16, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$47(bookController, (Continuation)null));
        final YueduApi yueduApi45 = this;
        final Route value17 = router.get("/reader3/setBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)value17, "router.get(\"/reader3/setBookSource\")");
        yueduApi45.coroutineHandler(value17, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$48(bookController, (Continuation)null));
        final YueduApi yueduApi46 = this;
        final Route post27 = router.post("/reader3/setBookSource");
        Intrinsics.checkNotNullExpressionValue((Object)post27, "router.post(\"/reader3/setBookSource\")");
        yueduApi46.coroutineHandler(post27, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$49(bookController, (Continuation)null));
        final YueduApi yueduApi47 = this;
        final Route post28 = router.post("/reader3/saveBookGroupId");
        Intrinsics.checkNotNullExpressionValue((Object)post28, "router.post(\"/reader3/saveBookGroupId\")");
        yueduApi47.coroutineHandler(post28, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$50(bookController, (Continuation)null));
        final YueduApi yueduApi48 = this;
        final Route post29 = router.post("/reader3/addBookGroupMulti");
        Intrinsics.checkNotNullExpressionValue((Object)post29, "router.post(\"/reader3/addBookGroupMulti\")");
        yueduApi48.coroutineHandler(post29, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$51(bookController, (Continuation)null));
        final YueduApi yueduApi49 = this;
        final Route post30 = router.post("/reader3/removeBookGroupMulti");
        Intrinsics.checkNotNullExpressionValue((Object)post30, "router.post(\"/reader3/removeBookGroupMulti\")");
        yueduApi49.coroutineHandler(post30, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$52(bookController, (Continuation)null));
        final YueduApi yueduApi50 = this;
        final Route post31 = router.post("/reader3/importBookPreview");
        Intrinsics.checkNotNullExpressionValue((Object)post31, "router.post(\"/reader3/importBookPreview\")");
        yueduApi50.coroutineHandler(post31, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$53(bookController, (Continuation)null));
        final YueduApi yueduApi51 = this;
        final Route post32 = router.post("/reader3/refreshLocalBook");
        Intrinsics.checkNotNullExpressionValue((Object)post32, "router.post(\"/reader3/refreshLocalBook\")");
        yueduApi51.coroutineHandler(post32, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$54(bookController, (Continuation)null));
        final YueduApi yueduApi52 = this;
        final Route value18 = router.get("/reader3/getTxtTocRules");
        Intrinsics.checkNotNullExpressionValue((Object)value18, "router.get(\"/reader3/getTxtTocRules\")");
        yueduApi52.coroutineHandler(value18, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$55(bookController, (Continuation)null));
        final YueduApi yueduApi53 = this;
        final Route post33 = router.post("/reader3/getChapterListByRule");
        Intrinsics.checkNotNullExpressionValue((Object)post33, "router.post(\"/reader3/getChapterListByRule\")");
        yueduApi53.coroutineHandler(post33, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$56(bookController, (Continuation)null));
        final YueduApi yueduApi54 = this;
        final Route value19 = router.get("/reader3/getBookGroups");
        Intrinsics.checkNotNullExpressionValue((Object)value19, "router.get(\"/reader3/getBookGroups\")");
        yueduApi54.coroutineHandler(value19, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$57(bookGroupController, (Continuation)null));
        final YueduApi yueduApi55 = this;
        final Route post34 = router.post("/reader3/saveBookGroup");
        Intrinsics.checkNotNullExpressionValue((Object)post34, "router.post(\"/reader3/saveBookGroup\")");
        yueduApi55.coroutineHandler(post34, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$58(bookGroupController, (Continuation)null));
        final YueduApi yueduApi56 = this;
        final Route post35 = router.post("/reader3/deleteBookGroup");
        Intrinsics.checkNotNullExpressionValue((Object)post35, "router.post(\"/reader3/deleteBookGroup\")");
        yueduApi56.coroutineHandler(post35, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$59(bookGroupController, (Continuation)null));
        final YueduApi yueduApi57 = this;
        final Route post36 = router.post("/reader3/saveBookGroupOrder");
        Intrinsics.checkNotNullExpressionValue((Object)post36, "router.post(\"/reader3/saveBookGroupOrder\")");
        yueduApi57.coroutineHandler(post36, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$60(bookGroupController, (Continuation)null));
        final YueduApi yueduApi58 = this;
        final Route value20 = router.get("/reader3/bookSourceDebugSSE");
        Intrinsics.checkNotNullExpressionValue((Object)value20, "router.get(\"/reader3/bookSourceDebugSSE\")");
        yueduApi58.coroutineHandlerWithoutRes(value20, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$61(bookController, (Continuation)null));
        final YueduApi yueduApi59 = this;
        final Route value21 = router.get("/reader3/cacheBookSSE");
        Intrinsics.checkNotNullExpressionValue((Object)value21, "router.get(\"/reader3/cacheBookSSE\")");
        yueduApi59.coroutineHandlerWithoutRes(value21, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$62(bookController, (Continuation)null));
        final YueduApi yueduApi60 = this;
        final Route post37 = router.post("/reader3/cacheBookOnServer");
        Intrinsics.checkNotNullExpressionValue((Object)post37, "router.post(\"/reader3/cacheBookOnServer\")");
        yueduApi60.coroutineHandler(post37, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$63(bookController, (Continuation)null));
        final YueduApi yueduApi61 = this;
        final Route value22 = router.get("/reader3/getShelfBookWithCacheInfo");
        Intrinsics.checkNotNullExpressionValue((Object)value22, "router.get(\"/reader3/getShelfBookWithCacheInfo\")");
        yueduApi61.coroutineHandler(value22, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$64(bookController, (Continuation)null));
        final YueduApi yueduApi62 = this;
        final Route post38 = router.post("/reader3/deleteBookCache");
        Intrinsics.checkNotNullExpressionValue((Object)post38, "router.post(\"/reader3/deleteBookCache\")");
        yueduApi62.coroutineHandler(post38, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$65(bookController, (Continuation)null));
        final YueduApi yueduApi63 = this;
        final Route post39 = router.post("/reader3/exportBook");
        Intrinsics.checkNotNullExpressionValue((Object)post39, "router.post(\"/reader3/exportBook\")");
        yueduApi63.coroutineHandlerWithoutRes(post39, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$66(bookController, (Continuation)null));
        final YueduApi yueduApi64 = this;
        final Route value23 = router.get("/reader3/exportBook");
        Intrinsics.checkNotNullExpressionValue((Object)value23, "router.get(\"/reader3/exportBook\")");
        yueduApi64.coroutineHandlerWithoutRes(value23, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$67(bookController, (Continuation)null));
        final YueduApi yueduApi65 = this;
        final Route value24 = router.get("/reader3/searchBookContent");
        Intrinsics.checkNotNullExpressionValue((Object)value24, "router.get(\"/reader3/searchBookContent\")");
        yueduApi65.coroutineHandler(value24, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$68(bookController, (Continuation)null));
        final YueduApi yueduApi66 = this;
        final Route post40 = router.post("/reader3/searchBookContent");
        Intrinsics.checkNotNullExpressionValue((Object)post40, "router.post(\"/reader3/searchBookContent\")");
        yueduApi66.coroutineHandler(post40, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$69(bookController, (Continuation)null));
        final YueduApi yueduApi67 = this;
        final Route post41 = router.post("/reader3/backupToMongodb");
        Intrinsics.checkNotNullExpressionValue((Object)post41, "router.post(\"/reader3/backupToMongodb\")");
        yueduApi67.coroutineHandler(post41, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$70(bookController, (Continuation)null));
        final YueduApi yueduApi68 = this;
        final Route post42 = router.post("/reader3/restoreFromMongodb");
        Intrinsics.checkNotNullExpressionValue((Object)post42, "router.post(\"/reader3/restoreFromMongodb\")");
        yueduApi68.coroutineHandler(post42, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$71(bookController, (Continuation)null));
        final YueduApi yueduApi69 = this;
        final Route post43 = router.post("/reader3/book/saveBookConfig");
        Intrinsics.checkNotNullExpressionValue((Object)post43, "router.post(\"/reader3/book/saveBookConfig\")");
        yueduApi69.coroutineHandler(post43, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$72(bookController, (Continuation)null));
        final YueduApi yueduApi70 = this;
        final Route value25 = router.get("/reader3/book/tts");
        Intrinsics.checkNotNullExpressionValue((Object)value25, "router.get(\"/reader3/book/tts\")");
        yueduApi70.coroutineHandlerWithoutRes(value25, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$73(bookController, (Continuation)null));
        final YueduApi yueduApi71 = this;
        final Route post44 = router.post("/reader3/book/tts");
        Intrinsics.checkNotNullExpressionValue((Object)post44, "router.post(\"/reader3/book/tts\")");
        yueduApi71.coroutineHandlerWithoutRes(post44, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$74(bookController, (Continuation)null));
        final YueduApi yueduApi72 = this;
        final Route post45 = router.post("/reader3/uploadFile");
        Intrinsics.checkNotNullExpressionValue((Object)post45, "router.post(\"/reader3/uploadFile\")");
        yueduApi72.coroutineHandler(post45, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$75(userController, (Continuation)null));
        final YueduApi yueduApi73 = this;
        final Route post46 = router.post("/reader3/deleteFile");
        Intrinsics.checkNotNullExpressionValue((Object)post46, "router.post(\"/reader3/deleteFile\")");
        yueduApi73.coroutineHandler(post46, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$76(userController, (Continuation)null));
        final YueduApi yueduApi74 = this;
        final Route post47 = router.post("/reader3/login");
        Intrinsics.checkNotNullExpressionValue((Object)post47, "router.post(\"/reader3/login\")");
        yueduApi74.coroutineHandler(post47, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$77(userController, (Continuation)null));
        final YueduApi yueduApi75 = this;
        final Route post48 = router.post("/reader3/logout");
        Intrinsics.checkNotNullExpressionValue((Object)post48, "router.post(\"/reader3/logout\")");
        yueduApi75.coroutineHandler(post48, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$78(userController, (Continuation)null));
        final YueduApi yueduApi76 = this;
        final Route value26 = router.get("/reader3/getUserInfo");
        Intrinsics.checkNotNullExpressionValue((Object)value26, "router.get(\"/reader3/getUserInfo\")");
        yueduApi76.coroutineHandler(value26, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$79(userController, (Continuation)null));
        final YueduApi yueduApi77 = this;
        final Route post49 = router.post("/reader3/saveUserConfig");
        Intrinsics.checkNotNullExpressionValue((Object)post49, "router.post(\"/reader3/saveUserConfig\")");
        yueduApi77.coroutineHandler(post49, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$80(userController, (Continuation)null));
        final YueduApi yueduApi78 = this;
        final Route value27 = router.get("/reader3/getUserConfig");
        Intrinsics.checkNotNullExpressionValue((Object)value27, "router.get(\"/reader3/getUserConfig\")");
        yueduApi78.coroutineHandler(value27, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$81(userController, (Continuation)null));
        final YueduApi yueduApi79 = this;
        final Route value28 = router.get("/reader3/getUserList");
        Intrinsics.checkNotNullExpressionValue((Object)value28, "router.get(\"/reader3/getUserList\")");
        yueduApi79.coroutineHandler(value28, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$82(userController, (Continuation)null));
        final YueduApi yueduApi80 = this;
        final Route post50 = router.post("/reader3/deleteUsers");
        Intrinsics.checkNotNullExpressionValue((Object)post50, "router.post(\"/reader3/deleteUsers\")");
        yueduApi80.coroutineHandler(post50, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$83(userController, (Continuation)null));
        final YueduApi yueduApi81 = this;
        final Route post51 = router.post("/reader3/clearInactiveUsers");
        Intrinsics.checkNotNullExpressionValue((Object)post51, "router.post(\"/reader3/clearInactiveUsers\")");
        yueduApi81.coroutineHandler(post51, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$84(userController, (Continuation)null));
        final YueduApi yueduApi82 = this;
        final Route post52 = router.post("/reader3/addUser");
        Intrinsics.checkNotNullExpressionValue((Object)post52, "router.post(\"/reader3/addUser\")");
        yueduApi82.coroutineHandler(post52, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$85(userController, (Continuation)null));
        final YueduApi yueduApi83 = this;
        final Route post53 = router.post("/reader3/resetPassword");
        Intrinsics.checkNotNullExpressionValue((Object)post53, "router.post(\"/reader3/resetPassword\")");
        yueduApi83.coroutineHandler(post53, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$86(userController, (Continuation)null));
        final YueduApi yueduApi84 = this;
        final Route post54 = router.post("/reader3/updateUser");
        Intrinsics.checkNotNullExpressionValue((Object)post54, "router.post(\"/reader3/updateUser\")");
        yueduApi84.coroutineHandler(post54, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$87(userController, (Continuation)null));
        final YueduApi yueduApi85 = this;
        final Route value29 = router.get("/reader3/user/downloadBackupFile");
        Intrinsics.checkNotNullExpressionValue((Object)value29, "router.get(\"/reader3/user/downloadBackupFile\")");
        yueduApi85.coroutineHandlerWithoutRes(value29, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$88(userController, (Continuation)null));
        final YueduApi yueduApi86 = this;
        final Route value30 = router.get("/reader3/getLicense");
        Intrinsics.checkNotNullExpressionValue((Object)value30, "router.get(\"/reader3/getLicense\")");
        yueduApi86.coroutineHandler(value30, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$89(licenseController, (Continuation)null));
        final YueduApi yueduApi87 = this;
        final Route post55 = router.post("/reader3/importLicense");
        Intrinsics.checkNotNullExpressionValue((Object)post55, "router.post(\"/reader3/importLicense\")");
        yueduApi87.coroutineHandlerWithoutRes(post55, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$90(licenseController, (Continuation)null));
        final YueduApi yueduApi88 = this;
        final Route value31 = router.get("/reader3/generateKeys");
        Intrinsics.checkNotNullExpressionValue((Object)value31, "router.get(\"/reader3/generateKeys\")");
        yueduApi88.coroutineHandler(value31, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$91(licenseController, (Continuation)null));
        final YueduApi yueduApi89 = this;
        final Route post56 = router.post("/reader3/generateKeys");
        Intrinsics.checkNotNullExpressionValue((Object)post56, "router.post(\"/reader3/generateKeys\")");
        yueduApi89.coroutineHandler(post56, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$92(licenseController, (Continuation)null));
        final YueduApi yueduApi90 = this;
        final Route value32 = router.get("/reader3/generateLicense");
        Intrinsics.checkNotNullExpressionValue((Object)value32, "router.get(\"/reader3/generateLicense\")");
        yueduApi90.coroutineHandler(value32, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$93(licenseController, (Continuation)null));
        final YueduApi yueduApi91 = this;
        final Route post57 = router.post("/reader3/generateLicense");
        Intrinsics.checkNotNullExpressionValue((Object)post57, "router.post(\"/reader3/generateLicense\")");
        yueduApi91.coroutineHandler(post57, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$94(licenseController, (Continuation)null));
        final YueduApi yueduApi92 = this;
        final Route value33 = router.get("/reader3/isHostValid");
        Intrinsics.checkNotNullExpressionValue((Object)value33, "router.get(\"/reader3/isHostValid\")");
        yueduApi92.coroutineHandler(value33, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$95(licenseController, (Continuation)null));
        final YueduApi yueduApi93 = this;
        final Route post58 = router.post("/reader3/isHostValid");
        Intrinsics.checkNotNullExpressionValue((Object)post58, "router.post(\"/reader3/isHostValid\")");
        yueduApi93.coroutineHandler(post58, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$96(licenseController, (Continuation)null));
        final YueduApi yueduApi94 = this;
        final Route post59 = router.post("/reader3/activateLicense");
        Intrinsics.checkNotNullExpressionValue((Object)post59, "router.post(\"/reader3/activateLicense\")");
        yueduApi94.coroutineHandler(post59, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$97(licenseController, (Continuation)null));
        final YueduApi yueduApi95 = this;
        final Route value34 = router.get("/reader3/isLicenseValid");
        Intrinsics.checkNotNullExpressionValue((Object)value34, "router.get(\"/reader3/isLicenseValid\")");
        yueduApi95.coroutineHandler(value34, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$98(licenseController, (Continuation)null));
        final YueduApi yueduApi96 = this;
        final Route post60 = router.post("/reader3/isLicenseValid");
        Intrinsics.checkNotNullExpressionValue((Object)post60, "router.post(\"/reader3/isLicenseValid\")");
        yueduApi96.coroutineHandler(post60, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$99(licenseController, (Continuation)null));
        final YueduApi yueduApi97 = this;
        final Route post61 = router.post("/reader3/decryptLicense");
        Intrinsics.checkNotNullExpressionValue((Object)post61, "router.post(\"/reader3/decryptLicense\")");
        yueduApi97.coroutineHandler(post61, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$100(licenseController, (Continuation)null));
        final YueduApi yueduApi98 = this;
        final Route post62 = router.post("/reader3/sendCodeToEmail");
        Intrinsics.checkNotNullExpressionValue((Object)post62, "router.post(\"/reader3/sendCodeToEmail\")");
        yueduApi98.coroutineHandler(post62, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$101(licenseController, (Continuation)null));
        final YueduApi yueduApi99 = this;
        final Route post63 = router.post("/reader3/supplyLicense");
        Intrinsics.checkNotNullExpressionValue((Object)post63, "router.post(\"/reader3/supplyLicense\")");
        yueduApi99.coroutineHandler(post63, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$102(licenseController, (Continuation)null));
        final YueduApi yueduApi100 = this;
        final Route post64 = router.post("/reader3/backupToWebdav");
        Intrinsics.checkNotNullExpressionValue((Object)post64, "router.post(\"/reader3/backupToWebdav\")");
        yueduApi100.coroutineHandler(post64, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$103(webdavController, (Continuation)null));
        final YueduApi yueduApi101 = this;
        final Route value35 = router.get("/reader3/getRssSources");
        Intrinsics.checkNotNullExpressionValue((Object)value35, "router.get(\"/reader3/getRssSources\")");
        yueduApi101.coroutineHandler(value35, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$104(rssSourceController, (Continuation)null));
        final YueduApi yueduApi102 = this;
        final Route post65 = router.post("/reader3/saveRssSource");
        Intrinsics.checkNotNullExpressionValue((Object)post65, "router.post(\"/reader3/saveRssSource\")");
        yueduApi102.coroutineHandler(post65, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$105(rssSourceController, (Continuation)null));
        final YueduApi yueduApi103 = this;
        final Route post66 = router.post("/reader3/saveRssSources");
        Intrinsics.checkNotNullExpressionValue((Object)post66, "router.post(\"/reader3/saveRssSources\")");
        yueduApi103.coroutineHandler(post66, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$106(rssSourceController, (Continuation)null));
        final YueduApi yueduApi104 = this;
        final Route post67 = router.post("/reader3/deleteRssSource");
        Intrinsics.checkNotNullExpressionValue((Object)post67, "router.post(\"/reader3/deleteRssSource\")");
        yueduApi104.coroutineHandler(post67, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$107(rssSourceController, (Continuation)null));
        final YueduApi yueduApi105 = this;
        final Route value36 = router.get("/reader3/getRssArticles");
        Intrinsics.checkNotNullExpressionValue((Object)value36, "router.get(\"/reader3/getRssArticles\")");
        yueduApi105.coroutineHandler(value36, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$108(rssSourceController, (Continuation)null));
        final YueduApi yueduApi106 = this;
        final Route post68 = router.post("/reader3/getRssArticles");
        Intrinsics.checkNotNullExpressionValue((Object)post68, "router.post(\"/reader3/getRssArticles\")");
        yueduApi106.coroutineHandler(post68, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$109(rssSourceController, (Continuation)null));
        final YueduApi yueduApi107 = this;
        final Route value37 = router.get("/reader3/getRssContent");
        Intrinsics.checkNotNullExpressionValue((Object)value37, "router.get(\"/reader3/getRssContent\")");
        yueduApi107.coroutineHandler(value37, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$110(rssSourceController, (Continuation)null));
        final YueduApi yueduApi108 = this;
        final Route post69 = router.post("/reader3/getRssContent");
        Intrinsics.checkNotNullExpressionValue((Object)post69, "router.post(\"/reader3/getRssContent\")");
        yueduApi108.coroutineHandler(post69, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$111(rssSourceController, (Continuation)null));
        final YueduApi yueduApi109 = this;
        final Route value38 = router.get("/reader3/getReplaceRules");
        Intrinsics.checkNotNullExpressionValue((Object)value38, "router.get(\"/reader3/getReplaceRules\")");
        yueduApi109.coroutineHandler(value38, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$112(replaceRuleController, (Continuation)null));
        final YueduApi yueduApi110 = this;
        final Route post70 = router.post("/reader3/saveReplaceRule");
        Intrinsics.checkNotNullExpressionValue((Object)post70, "router.post(\"/reader3/saveReplaceRule\")");
        yueduApi110.coroutineHandler(post70, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$113(replaceRuleController, (Continuation)null));
        final YueduApi yueduApi111 = this;
        final Route post71 = router.post("/reader3/saveReplaceRules");
        Intrinsics.checkNotNullExpressionValue((Object)post71, "router.post(\"/reader3/saveReplaceRules\")");
        yueduApi111.coroutineHandler(post71, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$114(replaceRuleController, (Continuation)null));
        final YueduApi yueduApi112 = this;
        final Route post72 = router.post("/reader3/deleteReplaceRule");
        Intrinsics.checkNotNullExpressionValue((Object)post72, "router.post(\"/reader3/deleteReplaceRule\")");
        yueduApi112.coroutineHandler(post72, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$115(replaceRuleController, (Continuation)null));
        final YueduApi yueduApi113 = this;
        final Route post73 = router.post("/reader3/deleteReplaceRules");
        Intrinsics.checkNotNullExpressionValue((Object)post73, "router.post(\"/reader3/deleteReplaceRules\")");
        yueduApi113.coroutineHandler(post73, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$116(replaceRuleController, (Continuation)null));
        final YueduApi yueduApi114 = this;
        final Route value39 = router.get("/reader3/getBookmarks");
        Intrinsics.checkNotNullExpressionValue((Object)value39, "router.get(\"/reader3/getBookmarks\")");
        yueduApi114.coroutineHandler(value39, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$117(bookmarkController, (Continuation)null));
        final YueduApi yueduApi115 = this;
        final Route post74 = router.post("/reader3/saveBookmark");
        Intrinsics.checkNotNullExpressionValue((Object)post74, "router.post(\"/reader3/saveBookmark\")");
        yueduApi115.coroutineHandler(post74, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$118(bookmarkController, (Continuation)null));
        final YueduApi yueduApi116 = this;
        final Route post75 = router.post("/reader3/saveBookmarks");
        Intrinsics.checkNotNullExpressionValue((Object)post75, "router.post(\"/reader3/saveBookmarks\")");
        yueduApi116.coroutineHandler(post75, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$119(bookmarkController, (Continuation)null));
        final YueduApi yueduApi117 = this;
        final Route post76 = router.post("/reader3/deleteBookmark");
        Intrinsics.checkNotNullExpressionValue((Object)post76, "router.post(\"/reader3/deleteBookmark\")");
        yueduApi117.coroutineHandler(post76, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$120(bookmarkController, (Continuation)null));
        final YueduApi yueduApi118 = this;
        final Route post77 = router.post("/reader3/deleteBookmarks");
        Intrinsics.checkNotNullExpressionValue((Object)post77, "router.post(\"/reader3/deleteBookmarks\")");
        yueduApi118.coroutineHandler(post77, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$121(bookmarkController, (Continuation)null));
        final YueduApi yueduApi119 = this;
        final Route value40 = router.get("/reader3/file/list");
        Intrinsics.checkNotNullExpressionValue((Object)value40, "router.get(\"/reader3/file/list\")");
        yueduApi119.coroutineHandler(value40, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$122(fileController, (Continuation)null));
        final YueduApi yueduApi120 = this;
        final Route value41 = router.get("/reader3/file/get");
        Intrinsics.checkNotNullExpressionValue((Object)value41, "router.get(\"/reader3/file/get\")");
        yueduApi120.coroutineHandler(value41, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$123(fileController, (Continuation)null));
        final YueduApi yueduApi121 = this;
        final Route post78 = router.post("/reader3/file/save");
        Intrinsics.checkNotNullExpressionValue((Object)post78, "router.post(\"/reader3/file/save\")");
        yueduApi121.coroutineHandler(post78, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$124(fileController, (Continuation)null));
        final YueduApi yueduApi122 = this;
        final Route post79 = router.post("/reader3/file/mkdir");
        Intrinsics.checkNotNullExpressionValue((Object)post79, "router.post(\"/reader3/file/mkdir\")");
        yueduApi122.coroutineHandler(post79, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$125(fileController, (Continuation)null));
        final YueduApi yueduApi123 = this;
        final Route value42 = router.get("/reader3/file/download");
        Intrinsics.checkNotNullExpressionValue((Object)value42, "router.get(\"/reader3/file/download\")");
        yueduApi123.coroutineHandlerWithoutRes(value42, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$126(fileController, (Continuation)null));
        final YueduApi yueduApi124 = this;
        final Route post80 = router.post("/reader3/file/upload");
        Intrinsics.checkNotNullExpressionValue((Object)post80, "router.post(\"/reader3/file/upload\")");
        yueduApi124.coroutineHandler(post80, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$127(fileController, (Continuation)null));
        final YueduApi yueduApi125 = this;
        final Route post81 = router.post("/reader3/file/delete");
        Intrinsics.checkNotNullExpressionValue((Object)post81, "router.post(\"/reader3/file/delete\")");
        yueduApi125.coroutineHandler(post81, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$128(fileController, (Continuation)null));
        final YueduApi yueduApi126 = this;
        final Route post82 = router.post("/reader3/file/deleteMulti");
        Intrinsics.checkNotNullExpressionValue((Object)post82, "router.post(\"/reader3/file/deleteMulti\")");
        yueduApi126.coroutineHandler(post82, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$129(fileController, (Continuation)null));
        final YueduApi yueduApi127 = this;
        final Route post83 = router.post("/reader3/file/importPreview");
        Intrinsics.checkNotNullExpressionValue((Object)post83, "router.post(\"/reader3/file/importPreview\")");
        yueduApi127.coroutineHandler(post83, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$130(fileController, (Continuation)null));
        final YueduApi yueduApi128 = this;
        final Route post84 = router.post("/reader3/file/restore");
        Intrinsics.checkNotNullExpressionValue((Object)post84, "router.post(\"/reader3/file/restore\")");
        yueduApi128.coroutineHandler(post84, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$131(fileController, (Continuation)null));
        final YueduApi yueduApi129 = this;
        final Route value43 = router.get("/reader3/file/parse");
        Intrinsics.checkNotNullExpressionValue((Object)value43, "router.get(\"/reader3/file/parse\")");
        yueduApi129.coroutineHandler(value43, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$132(fileController, (Continuation)null));
        final YueduApi yueduApi130 = this;
        final Route post85 = router.post("/reader3/file/parse");
        Intrinsics.checkNotNullExpressionValue((Object)post85, "router.post(\"/reader3/file/parse\")");
        yueduApi130.coroutineHandler(post85, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$133(fileController, (Continuation)null));
        final YueduApi yueduApi131 = this;
        final Route value44 = router.get("/reader3/httpTTS/list");
        Intrinsics.checkNotNullExpressionValue((Object)value44, "router.get(\"/reader3/httpTTS/list\")");
        yueduApi131.coroutineHandler(value44, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$134(httpTTSController, (Continuation)null));
        final YueduApi yueduApi132 = this;
        final Route post86 = router.post("/reader3/httpTTS/save");
        Intrinsics.checkNotNullExpressionValue((Object)post86, "router.post(\"/reader3/httpTTS/save\")");
        yueduApi132.coroutineHandler(post86, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$135(httpTTSController, (Continuation)null));
        final YueduApi yueduApi133 = this;
        final Route post87 = router.post("/reader3/httpTTS/saveMulti");
        Intrinsics.checkNotNullExpressionValue((Object)post87, "router.post(\"/reader3/httpTTS/saveMulti\")");
        yueduApi133.coroutineHandler(post87, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$136(httpTTSController, (Continuation)null));
        final YueduApi yueduApi134 = this;
        final Route post88 = router.post("/reader3/httpTTS/delete");
        Intrinsics.checkNotNullExpressionValue((Object)post88, "router.post(\"/reader3/httpTTS/delete\")");
        yueduApi134.coroutineHandler(post88, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$137(httpTTSController, (Continuation)null));
        final YueduApi yueduApi135 = this;
        final Route post89 = router.post("/reader3/httpTTS/deleteMulti");
        Intrinsics.checkNotNullExpressionValue((Object)post89, "router.post(\"/reader3/httpTTS/deleteMulti\")");
        yueduApi135.coroutineHandler(post89, (Function2<? super RoutingContext, ? super Continuation<Object>, ?>)new YueduApi$initRouter.YueduApi$initRouter$138(httpTTSController, (Continuation)null));
        return Unit.INSTANCE;
    }
    
    @Nullable
    public Object setupPort(@NotNull final Continuation<? super Unit> $completion) {
        return setupPort$suspendImpl(this, $completion);
    }
    
    static /* synthetic */ Object setupPort$suspendImpl(final YueduApi this, final Continuation $completion) {
        YueduApiKt.access$getLogger$p().info("port: {}", (Object)Boxing.boxInt(this.getPort()));
        final Environment env = this.env;
        if (env == null) {
            Intrinsics.throwUninitializedPropertyAccessException("env");
            throw null;
        }
        final Integer serverPort = (Integer)env.getProperty("reader.server.port", (Class)Integer.TYPE);
        YueduApiKt.access$getLogger$p().info("serverPort: {}", (Object)serverPort);
        if (serverPort != null && serverPort.intValue() > 0) {
            this.setPort(serverPort.intValue());
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public Object migration(@NotNull final Continuation<? super Unit> $completion) {
        return migration$suspendImpl(this, $completion);
    }
    
    static /* synthetic */ Object migration$suspendImpl(final YueduApi this, final Continuation $completion) {
        try {
            final File storageDir = new File(ExtKt.getWorkDir("storage"));
            final File dataDir = new File(ExtKt.getWorkDir("storage", "data", "default"));
            if (!storageDir.exists()) {
                dataDir.mkdirs();
            }
            else if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return Unit.INSTANCE;
    }
    
    @NotNull
    @Override
    public String getContextPath() {
        final Environment env = this.env;
        if (env == null) {
            Intrinsics.throwUninitializedPropertyAccessException("env");
            throw null;
        }
        final String contextPath = (String)env.getProperty("reader.server.contextPath", (Class)String.class);
        final CharSequence charSequence = contextPath;
        if (charSequence != null && charSequence.length() != 0) {
            final String s = contextPath;
            Intrinsics.checkNotNullExpressionValue((Object)s, "contextPath");
            return s;
        }
        return "";
    }
    
    @Override
    public void started() {
        SpringContextUtils.getApplicationContext().publishEvent((ApplicationEvent)new SpringEvent(this, "READY", ""));
    }
    
    @Override
    public void onStartError() {
        YueduApiKt.access$getLogger$p().error("\u5e94\u7528\u542f\u52a8\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5" + this.getPort() + "\u7aef\u53e3\u662f\u5426\u88ab\u5360\u7528");
        SpringContextUtils.getApplicationContext().publishEvent((ApplicationEvent)new SpringEvent(this, "START_ERROR", "\u5e94\u7528\u542f\u52a8\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5" + this.getPort() + "\u7aef\u53e3\u662f\u5426\u88ab\u5360\u7528"));
    }
    
    @Override
    public void onHandlerError(@NotNull final RoutingContext ctx, @NotNull final Exception error) {
        Intrinsics.checkNotNullParameter((Object)ctx, "ctx");
        Intrinsics.checkNotNullParameter((Object)error, "error");
        final ReturnData returnData = new ReturnData();
        YueduApiKt.access$getLogger$p().error("onHandlerError: ", (Throwable)error);
        if (!ctx.response().headWritten()) {
            VertExtKt.success(ctx, returnData.setErrorMsg(error.toString()));
        }
        else {
            ctx.response().end(error.toString());
        }
    }
    
    private final Object getSystemInfo(final RoutingContext context, final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof YueduApi$getSystemInfo.YueduApi$getSystemInfo$1) {
                final YueduApi$getSystemInfo.YueduApi$getSystemInfo$1 yueduApi$getSystemInfo$1 = (YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$completion;
                if ((yueduApi$getSystemInfo$1.label & Integer.MIN_VALUE) != 0x0) {
                    final YueduApi$getSystemInfo.YueduApi$getSystemInfo$1 yueduApi$getSystemInfo$2 = yueduApi$getSystemInfo$1;
                    yueduApi$getSystemInfo$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new YueduApi$getSystemInfo.YueduApi$getSystemInfo$1(this, (Continuation)$completion);
        }
        final Object $result = ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        String systemFont = null;
        String freeMemory = null;
        String totalMemory = null;
        String maxMemory = null;
        Ref$IntRef dayLoginUser = null;
        Ref$IntRef sevenDayLoginUser = null;
        Ref$IntRef monthLoginUser = null;
        Ref$IntRef keepUser = null;
        Ref$IntRef dayRegisterUser = null;
        Ref$IntRef sevenDayRegisterUser = null;
        Ref$IntRef monthRegisterUser = null;
        switch (((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                systemFont = System.getProperty("reader.system.fonts");
                freeMemory = "" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + 'M';
                totalMemory = "" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + 'M';
                maxMemory = "" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + 'M';
                final UserController userController = new UserController(this.getCoroutineContext());
                dayLoginUser = new Ref$IntRef();
                sevenDayLoginUser = new Ref$IntRef();
                monthLoginUser = new Ref$IntRef();
                keepUser = new Ref$IntRef();
                dayRegisterUser = new Ref$IntRef();
                sevenDayRegisterUser = new Ref$IntRef();
                monthRegisterUser = new Ref$IntRef();
                final Calendar calendar = Calendar.getInstance();
                calendar.set(5, 1);
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.getTimeInMillis();
                final UserController userController2 = userController;
                final Function3 handler = (Function3)new YueduApi$getSystemInfo.YueduApi$getSystemInfo$2(dayLoginUser, sevenDayLoginUser, calendar, monthLoginUser, dayRegisterUser, sevenDayRegisterUser, monthRegisterUser, keepUser, (Continuation)null);
                final Continuation $completion2 = $continuation;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$0 = returnData;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$1 = systemFont;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$2 = freeMemory;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$3 = totalMemory;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$4 = maxMemory;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$5 = dayLoginUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$6 = sevenDayLoginUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$7 = monthLoginUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$8 = keepUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$9 = dayRegisterUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$10 = sevenDayRegisterUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$11 = monthRegisterUser;
                ((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).label = 1;
                if (userController2.forEachUser((Function3<? super CoroutineScope, ? super User, ? super Continuation<? super Boolean>, ?>)handler, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                monthRegisterUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$11;
                sevenDayRegisterUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$10;
                dayRegisterUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$9;
                keepUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$8;
                monthLoginUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$7;
                sevenDayLoginUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$6;
                dayLoginUser = (Ref$IntRef)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$5;
                maxMemory = (String)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$4;
                totalMemory = (String)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$3;
                freeMemory = (String)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$2;
                systemFont = (String)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$1;
                returnData = (ReturnData)((YueduApi$getSystemInfo.YueduApi$getSystemInfo$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"fonts", (Object)systemFont), TuplesKt.to((Object)"freeMemory", (Object)freeMemory), TuplesKt.to((Object)"totalMemory", (Object)totalMemory), TuplesKt.to((Object)"maxMemory", (Object)maxMemory), TuplesKt.to((Object)"dayRegisterUser", (Object)Boxing.boxInt(dayRegisterUser.element)), TuplesKt.to((Object)"dayLoginUser", (Object)Boxing.boxInt(dayLoginUser.element)), TuplesKt.to((Object)"sevenDayRegisterUser", (Object)Boxing.boxInt(sevenDayRegisterUser.element)), TuplesKt.to((Object)"sevenDayLoginUser", (Object)Boxing.boxInt(sevenDayLoginUser.element)), TuplesKt.to((Object)"monthRegisterUser", (Object)Boxing.boxInt(monthRegisterUser.element)), TuplesKt.to((Object)"monthLoginUser", (Object)Boxing.boxInt(monthLoginUser.element)), TuplesKt.to((Object)"keepUser", (Object)Boxing.boxInt(keepUser.element)) }), null, 2, null);
    }
    
    @Scheduled(cron = "0 0/10 * * * ?")
    public void shelfUpdateJob() {
        final AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (appConfig.getShelfUpdateInteval() <= 0) {
            return;
        }
        final Calendar now = Calendar.getInstance();
        final int hour = now.get(11);
        final int munite = now.get(12);
        final int n;
        final int muniteFromToday = n = hour * 60 + munite;
        final AppConfig appConfig2 = this.appConfig;
        if (appConfig2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (n % appConfig2.getShelfUpdateInteval() != 0) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new YueduApi$shelfUpdateJob.YueduApi$shelfUpdateJob$1((Continuation)null), 2, (Object)null);
    }
    
    @Scheduled(cron = "0 0/10 * * * ?")
    public void remoteBookSourceSubUpdateJob() {
        final AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (appConfig.getRemoteBookSourceUpdateInterval() <= 0) {
            return;
        }
        final Calendar now = Calendar.getInstance();
        final int hour = now.get(11);
        final int munite = now.get(12);
        final int n;
        final int muniteFromToday = n = hour * 60 + munite;
        final AppConfig appConfig2 = this.appConfig;
        if (appConfig2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (n % appConfig2.getRemoteBookSourceUpdateInterval() != 0) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new YueduApi$remoteBookSourceSubUpdateJob.YueduApi$remoteBookSourceSubUpdateJob$1((Continuation)null), 2, (Object)null);
    }
    
    @Scheduled(cron = "0 59 23 * * ?")
    public void clearUser() {
        final AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (appConfig.getAutoClearInactiveUser() > 0) {
            final AppConfig appConfig2 = this.appConfig;
            if (appConfig2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                throw null;
            }
            if (appConfig2.getSecure()) {
                MDC.put("traceId", ExtKt.getTraceId());
                BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new YueduApi$clearUser.YueduApi$clearUser$1(this, (Continuation)null), 2, (Object)null);
            }
        }
    }
    
    @Scheduled(cron = "0 50 23 * * ?")
    public void autoBackup() {
        final AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (!appConfig.getAutoBackupUserData()) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new YueduApi$autoBackup.YueduApi$autoBackup$1((Continuation)null), 2, (Object)null);
    }
    
    @Scheduled(cron = "0 0 2 * * ?")
    public void autoGC() {
        System.gc();
    }
    
    @Scheduled(cron = "0 4/15 7-23 * * ?")
    public void checkLicense() {
        final License license = ExtKt.getInstalledLicense(true);
        if ("default".equals(license.getType())) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new YueduApi$checkLicense.YueduApi$checkLicense$1(license, (Continuation)null), 2, (Object)null);
    }
    
    private static final void initRouter$lambda-0(final Ref$ObjectRef $dataDir, final RoutingContext it) {
        Intrinsics.checkNotNullParameter((Object)$dataDir, "$dataDir");
        final String path2 = it.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "it.request().path()");
        String path = StringsKt.replace(path2, "/book-assets/", "/", true);
        final String decodeURIComponent = URIDecoder.decodeURIComponent(path, false);
        Intrinsics.checkNotNullExpressionValue((Object)decodeURIComponent, "decodeURIComponent(path, false)");
        path = decodeURIComponent;
        if (StringsKt.endsWith(path, "html", true) || StringsKt.endsWith(path, "htm", true)) {
            final File filePath = new File(Intrinsics.stringPlus((String)$dataDir.element, (Object)path));
            if (filePath.exists()) {
                final BookConfig instance = BookConfig.INSTANCE;
                final String string = filePath.toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "filePath.toString()");
                instance.injectJavascriptToEpubChapter(string);
            }
        }
        it.next();
    }
    
    private static final void initRouter$lambda-1(final Ref$ObjectRef $dataDir, final RoutingContext it) {
        Intrinsics.checkNotNullParameter((Object)$dataDir, "$dataDir");
        final String path2 = it.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "it.request().path()");
        String path = StringsKt.replace(path2, "/epub/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        if (StringsKt.endsWith(path, "html", true)) {
            final File filePath = new File(Intrinsics.stringPlus((String)$dataDir.element, (Object)path));
            if (filePath.exists()) {
                final BookConfig instance = BookConfig.INSTANCE;
                final String string = filePath.toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "filePath.toString()");
                instance.injectJavascriptToEpubChapter(string);
            }
        }
        it.next();
    }
    
    private static final void initRouter$lambda-2(final RoutingContext it) {
        final String path = it.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path, "it.request().path()");
        if (StringsKt.endsWith$default(path, "/simple-web", false, 2, (Object)null)) {
            final HttpServerResponse response = it.response();
            final String s = "Location";
            final String decode = URLDecoder.decode(it.request().absoluteURI(), "UTF-8");
            Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(it.request().absoluteURI(), \"UTF-8\")");
            response.putHeader(s, StringsKt.replace$default(decode, "/simple-web", "/simple-web/", false, 4, (Object)null)).setStatusCode(302).end();
        }
        else {
            it.next();
        }
    }
    
    private static final void initRouter$lambda-3(final RoutingContext it) {
        final License license = ExtKt.getInstalledLicense$default(false, 1, null);
        long simpleWebExpiredAt = 0L;
        final License license2 = license;
        final String host = it.request().host();
        Intrinsics.checkNotNullExpressionValue((Object)host, "it.request().host()");
        if (license2.validHost(host)) {
            simpleWebExpiredAt = license.getSimpleWebExpiredAt();
        }
        if (simpleWebExpiredAt != 0L && simpleWebExpiredAt < System.currentTimeMillis()) {
            it.response().putHeader("content-type", "text/html; charset=UTF-8").setStatusCode(403).end("<html><head><title>\u672a\u6fc0\u6d3b\u8be5\u529f\u80fd</title></head><body><div style='text-align: center;padding: 30px 0;'>\u672a\u6fc0\u6d3b\u8be5\u529f\u80fd\uff0c\u8bf7\u52a0<a href='https://t.me/+pQ8HDlANPZ84ZWNl'>TG\u7fa4</a>\u6fc0\u6d3b</div></body></html>");
        }
        else {
            it.next();
        }
    }
}
