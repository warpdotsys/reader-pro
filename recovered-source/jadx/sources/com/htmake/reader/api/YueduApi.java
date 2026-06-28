package com.htmake.reader.api;

import com.htmake.reader.SpringEvent;
import com.htmake.reader.api.controller.BookController;
import com.htmake.reader.api.controller.BookGroupController;
import com.htmake.reader.api.controller.BookSourceController;
import com.htmake.reader.api.controller.BookmarkController;
import com.htmake.reader.api.controller.FileController;
import com.htmake.reader.api.controller.HttpTTSController;
import com.htmake.reader.api.controller.LicenseController;
import com.htmake.reader.api.controller.ReplaceRuleController;
import com.htmake.reader.api.controller.RssSourceController;
import com.htmake.reader.api.controller.UserController;
import com.htmake.reader.api.controller.WebdavController;
import com.htmake.reader.config.AppConfig;
import com.htmake.reader.config.BookConfig;
import com.htmake.reader.entity.License;
import com.htmake.reader.entity.User;
import com.htmake.reader.init.ReaderAdapter;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.MongoManager;
import com.htmake.reader.utils.RemoteWebview;
import com.htmake.reader.utils.SpringContextUtils;
import com.htmake.reader.utils.VertExtKt;
import com.htmake.reader.verticle.RestVerticle;
import io.legado.app.adapters.ReaderAdapterHelper;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.impl.URIDecoder;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.slf4j.MDCContext;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import mu.KLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/* JADX INFO: compiled from: YueduApi.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0017\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0017J\b\u0010\t\u001a\u00020\bH\u0017J\b\u0010\n\u001a\u00020\bH\u0017J\b\u0010\u000b\u001a\u00020\bH\u0017J\b\u0010\f\u001a\u00020\rH\u0016J\u0019\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0012J\u0019\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\bH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u00112\n\u0010\u001b\u001a\u00060\u001cj\u0002`\u001dH\u0016J\b\u0010\u001e\u001a\u00020\bH\u0016J\b\u0010\u001f\u001a\u00020\bH\u0017J\u0011\u0010 \u001a\u00020\bH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0018J\b\u0010!\u001a\u00020\bH\u0017J\b\u0010\"\u001a\u00020\bH\u0016R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00068\u0002@\u0002X\u0083.¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006#"}, d2 = {"Lcom/htmake/reader/api/YueduApi;", "Lcom/htmake/reader/verticle/RestVerticle;", "()V", "appConfig", "Lcom/htmake/reader/config/AppConfig;", "env", "Lorg/springframework/core/env/Environment;", "autoBackup", PackageDocumentBase.PREFIX_OPF, "autoGC", "checkLicense", "clearUser", "getContextPath", PackageDocumentBase.PREFIX_OPF, "getSystemInfo", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initRouter", "router", "Lio/vertx/ext/web/Router;", "(Lio/vertx/ext/web/Router;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "migration", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onHandlerError", "ctx", "error", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onStartError", "remoteBookSourceSubUpdateJob", "setupPort", "shelfUpdateJob", "started", "reader-pro"})
@Component
public class YueduApi extends RestVerticle {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private Environment env;

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$getSystemInfo$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$getSystemInfo$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "YueduApi.kt", l = {504}, i = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11"}, n = {"returnData", "systemFont", "freeMemory", "totalMemory", "maxMemory", "dayLoginUser", "sevenDayLoginUser", "monthLoginUser", "keepUser", "dayRegisterUser", "sevenDayRegisterUser", "monthRegisterUser"}, m = "getSystemInfo", c = "com.htmake.reader.api.YueduApi")
    static final class C00061 extends ContinuationImpl {
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
        /* synthetic */ Object result;
        int label;

        C00061(Continuation<? super C00061> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return YueduApi.this.getSystemInfo(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "YueduApi.kt", l = {48, 64}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "router"}, m = "initRouter$suspendImpl", c = "com.htmake.reader.api.YueduApi")
    static final class C00071 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        C00071(Continuation<? super C00071> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return YueduApi.initRouter$suspendImpl(YueduApi.this, null, (Continuation) this);
        }
    }

    @Override // com.htmake.reader.verticle.RestVerticle
    @Nullable
    public Object initRouter(@NotNull Router router, @NotNull Continuation<? super Unit> $completion) {
        return initRouter$suspendImpl(this, router, $completion);
    }

    @Nullable
    public Object setupPort(@NotNull Continuation<? super Unit> $completion) {
        return setupPort$suspendImpl(this, $completion);
    }

    @Nullable
    public Object migration(@NotNull Continuation<? super Unit> $completion) {
        return migration$suspendImpl(this, $completion);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static /* synthetic */ Object initRouter$suspendImpl(YueduApi yueduApi, Router router, Continuation continuation) {
        C00071 c00071;
        File assetsDirFile;
        File assetsCssFile;
        AppConfig appConfig;
        if (continuation instanceof C00071) {
            c00071 = (C00071) continuation;
            if ((c00071.label & Integer.MIN_VALUE) != 0) {
                c00071.label -= Integer.MIN_VALUE;
            } else {
                c00071 = yueduApi.new C00071(continuation);
            }
        }
        Object $result = c00071.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00071.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c00071.L$0 = yueduApi;
                c00071.L$1 = router;
                c00071.label = 1;
                if (yueduApi.setupPort(c00071) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                appConfig = yueduApi.appConfig;
                if (appConfig != null) {
                    Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                    throw null;
                }
                if (appConfig.getMongoUri().length() > 0) {
                    MongoManager mongoManager = MongoManager.INSTANCE;
                    AppConfig appConfig2 = yueduApi.appConfig;
                    if (appConfig2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                        throw null;
                    }
                    mongoManager.connect(appConfig2.getMongoUri());
                }
                AppConfig appConfig3 = yueduApi.appConfig;
                if (appConfig3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                    throw null;
                }
                if (appConfig3.getRemoteWebviewApi().length() > 0) {
                    RemoteWebview remoteWebview = RemoteWebview.INSTANCE;
                    AppConfig appConfig4 = yueduApi.appConfig;
                    if (appConfig4 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                        throw null;
                    }
                    remoteWebview.setRemoteApi(appConfig4.getRemoteWebviewApi());
                }
                ReaderAdapterHelper.INSTANCE.setAdapter(ReaderAdapter.INSTANCE);
                c00071.L$0 = yueduApi;
                c00071.L$1 = router;
                c00071.label = 2;
                if (yueduApi.migration(c00071) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                router.route("/*").handler(StaticHandler.create("web").setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                String assetsDir = ExtKt.getWorkDir("storage", "assets");
                assetsDirFile = new File(assetsDir);
                if (!assetsDirFile.exists()) {
                    assetsDirFile.mkdirs();
                }
                String assetsCss = ExtKt.getWorkDir("storage", "assets", "reader.css");
                assetsCssFile = new File(assetsCss);
                if (!assetsCssFile.exists()) {
                    FilesKt.writeText$default(assetsCssFile, "/* 在此处可以编写CSS样式来自定义页面 */", (Charset) null, 2, (Object) null);
                }
                router.route("/assets/*").handler(StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot(assetsDir).setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                Ref.ObjectRef dataDir = new Ref.ObjectRef();
                dataDir.element = ExtKt.getWorkDir("storage", "data");
                router.route("/book-assets/*").handler((v1) -> {
                    m7initRouter$lambda0(r1, v1);
                });
                router.route("/book-assets/*").handler(StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot((String) dataDir.element).setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                router.route("/epub/*").handler((v1) -> {
                    m8initRouter$lambda1(r1, v1);
                });
                router.route("/epub/*").handler(StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot((String) dataDir.element).setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                router.route("/simple-web").handler(YueduApi::m9initRouter$lambda2);
                router.route("/simple-web/*").handler(YueduApi::m10initRouter$lambda3);
                router.route("/simple-web/*").handler(StaticHandler.create("simple-web").setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                Route route = router.get("/reader3/getSystemInfo");
                Intrinsics.checkNotNullExpressionValue(route, "router.get(\"/reader3/getSystemInfo\")");
                yueduApi.coroutineHandler(route, yueduApi.new AnonymousClass6(null));
                BookController bookController = new BookController(yueduApi.getCoroutineContext());
                BookGroupController bookGroupController = new BookGroupController(yueduApi.getCoroutineContext());
                BookSourceController bookSourceController = new BookSourceController(yueduApi.getCoroutineContext());
                RssSourceController rssSourceController = new RssSourceController(yueduApi.getCoroutineContext());
                UserController userController = new UserController(yueduApi.getCoroutineContext());
                WebdavController webdavController = new WebdavController(yueduApi.getCoroutineContext(), router, new YueduApi$initRouter$webdavController$1(yueduApi));
                ReplaceRuleController replaceRuleController = new ReplaceRuleController(yueduApi.getCoroutineContext());
                BookmarkController bookmarkController = new BookmarkController(yueduApi.getCoroutineContext());
                FileController fileController = new FileController(yueduApi.getCoroutineContext());
                LicenseController licenseController = new LicenseController(yueduApi.getCoroutineContext());
                HttpTTSController httpTTSController = new HttpTTSController(yueduApi.getCoroutineContext());
                Route routePost = router.post("/reader3/saveBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost, "router.post(\"/reader3/saveBookSource\")");
                yueduApi.coroutineHandler(routePost, new AnonymousClass7(bookSourceController, null));
                Route routePost2 = router.post("/reader3/saveBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost2, "router.post(\"/reader3/saveBookSources\")");
                yueduApi.coroutineHandler(routePost2, new AnonymousClass8(bookSourceController, null));
                Route route2 = router.get("/reader3/getBookSource");
                Intrinsics.checkNotNullExpressionValue(route2, "router.get(\"/reader3/getBookSource\")");
                yueduApi.coroutineHandler(route2, new AnonymousClass9(bookSourceController, null));
                Route routePost3 = router.post("/reader3/getBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost3, "router.post(\"/reader3/getBookSource\")");
                yueduApi.coroutineHandler(routePost3, new AnonymousClass10(bookSourceController, null));
                Route route3 = router.get("/reader3/getBookSources");
                Intrinsics.checkNotNullExpressionValue(route3, "router.get(\"/reader3/getBookSources\")");
                yueduApi.coroutineHandler(route3, new AnonymousClass11(bookSourceController, null));
                Route routePost4 = router.post("/reader3/getBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost4, "router.post(\"/reader3/getBookSources\")");
                yueduApi.coroutineHandler(routePost4, new AnonymousClass12(bookSourceController, null));
                Route routePost5 = router.post("/reader3/deleteAllBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost5, "router.post(\"/reader3/deleteAllBookSources\")");
                yueduApi.coroutineHandler(routePost5, new AnonymousClass13(bookSourceController, null));
                Route routePost6 = router.post("/reader3/deleteBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost6, "router.post(\"/reader3/deleteBookSource\")");
                yueduApi.coroutineHandler(routePost6, new AnonymousClass14(bookSourceController, null));
                Route routePost7 = router.post("/reader3/deleteBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost7, "router.post(\"/reader3/deleteBookSources\")");
                yueduApi.coroutineHandler(routePost7, new AnonymousClass15(bookSourceController, null));
                Route routePost8 = router.post("/reader3/readSourceFile");
                Intrinsics.checkNotNullExpressionValue(routePost8, "router.post(\"/reader3/readSourceFile\")");
                yueduApi.coroutineHandler(routePost8, new AnonymousClass16(bookSourceController, null));
                Route routePost9 = router.post("/reader3/saveFromRemoteSource");
                Intrinsics.checkNotNullExpressionValue(routePost9, "router.post(\"/reader3/saveFromRemoteSource\")");
                yueduApi.coroutineHandlerWithoutRes(routePost9, new AnonymousClass17(bookSourceController, null));
                Route routePost10 = router.post("/reader3/setAsDefaultBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost10, "router.post(\"/reader3/setAsDefaultBookSources\")");
                yueduApi.coroutineHandler(routePost10, new AnonymousClass18(bookSourceController, null));
                Route routePost11 = router.post("/reader3/deleteUserBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost11, "router.post(\"/reader3/deleteUserBookSource\")");
                yueduApi.coroutineHandler(routePost11, new AnonymousClass19(bookSourceController, null));
                Route routePost12 = router.post("/reader3/deleteBookSourcesFile");
                Intrinsics.checkNotNullExpressionValue(routePost12, "router.post(\"/reader3/deleteBookSourcesFile\")");
                yueduApi.coroutineHandler(routePost12, new AnonymousClass20(bookSourceController, null));
                Route route4 = router.get("/reader3/getBookshelf");
                Intrinsics.checkNotNullExpressionValue(route4, "router.get(\"/reader3/getBookshelf\")");
                yueduApi.coroutineHandler(route4, new AnonymousClass21(bookController, null));
                Route route5 = router.get("/reader3/getShelfBook");
                Intrinsics.checkNotNullExpressionValue(route5, "router.get(\"/reader3/getShelfBook\")");
                yueduApi.coroutineHandler(route5, new AnonymousClass22(bookController, null));
                Route routePost13 = router.post("/reader3/saveBook");
                Intrinsics.checkNotNullExpressionValue(routePost13, "router.post(\"/reader3/saveBook\")");
                yueduApi.coroutineHandler(routePost13, new AnonymousClass23(bookController, null));
                Route routePost14 = router.post("/reader3/deleteBook");
                Intrinsics.checkNotNullExpressionValue(routePost14, "router.post(\"/reader3/deleteBook\")");
                yueduApi.coroutineHandler(routePost14, new AnonymousClass24(bookController, null));
                Route routePost15 = router.post("/reader3/deleteBooks");
                Intrinsics.checkNotNullExpressionValue(routePost15, "router.post(\"/reader3/deleteBooks\")");
                yueduApi.coroutineHandler(routePost15, new AnonymousClass25(bookController, null));
                Route routePost16 = router.post("/reader3/getInvalidBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost16, "router.post(\"/reader3/getInvalidBookSources\")");
                yueduApi.coroutineHandler(routePost16, new AnonymousClass26(bookController, null));
                Route routePost17 = router.post("/reader3/exploreBook");
                Intrinsics.checkNotNullExpressionValue(routePost17, "router.post(\"/reader3/exploreBook\")");
                yueduApi.coroutineHandler(routePost17, new AnonymousClass27(bookController, null));
                Route route6 = router.get("/reader3/exploreBook");
                Intrinsics.checkNotNullExpressionValue(route6, "router.get(\"/reader3/exploreBook\")");
                yueduApi.coroutineHandler(route6, new AnonymousClass28(bookController, null));
                Route route7 = router.get("/reader3/searchBook");
                Intrinsics.checkNotNullExpressionValue(route7, "router.get(\"/reader3/searchBook\")");
                yueduApi.coroutineHandler(route7, new AnonymousClass29(bookController, null));
                Route routePost18 = router.post("/reader3/searchBook");
                Intrinsics.checkNotNullExpressionValue(routePost18, "router.post(\"/reader3/searchBook\")");
                yueduApi.coroutineHandler(routePost18, new AnonymousClass30(bookController, null));
                Route route8 = router.get("/reader3/searchBookMulti");
                Intrinsics.checkNotNullExpressionValue(route8, "router.get(\"/reader3/searchBookMulti\")");
                yueduApi.coroutineHandler(route8, new AnonymousClass31(bookController, null));
                Route routePost19 = router.post("/reader3/searchBookMulti");
                Intrinsics.checkNotNullExpressionValue(routePost19, "router.post(\"/reader3/searchBookMulti\")");
                yueduApi.coroutineHandler(routePost19, new AnonymousClass32(bookController, null));
                Route route9 = router.get("/reader3/searchBookMultiSSE");
                Intrinsics.checkNotNullExpressionValue(route9, "router.get(\"/reader3/searchBookMultiSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route9, new AnonymousClass33(bookController, null));
                Route route10 = router.get("/reader3/getBookInfo");
                Intrinsics.checkNotNullExpressionValue(route10, "router.get(\"/reader3/getBookInfo\")");
                yueduApi.coroutineHandler(route10, new AnonymousClass34(bookController, null));
                Route routePost20 = router.post("/reader3/getBookInfo");
                Intrinsics.checkNotNullExpressionValue(routePost20, "router.post(\"/reader3/getBookInfo\")");
                yueduApi.coroutineHandler(routePost20, new AnonymousClass35(bookController, null));
                Route route11 = router.get("/reader3/getChapterList");
                Intrinsics.checkNotNullExpressionValue(route11, "router.get(\"/reader3/getChapterList\")");
                yueduApi.coroutineHandler(route11, new AnonymousClass36(bookController, null));
                Route routePost21 = router.post("/reader3/getChapterList");
                Intrinsics.checkNotNullExpressionValue(routePost21, "router.post(\"/reader3/getChapterList\")");
                yueduApi.coroutineHandler(routePost21, new AnonymousClass37(bookController, null));
                Route route12 = router.get("/reader3/getBookContent");
                Intrinsics.checkNotNullExpressionValue(route12, "router.get(\"/reader3/getBookContent\")");
                yueduApi.coroutineHandler(route12, new AnonymousClass38(bookController, null));
                Route routePost22 = router.post("/reader3/getBookContent");
                Intrinsics.checkNotNullExpressionValue(routePost22, "router.post(\"/reader3/getBookContent\")");
                yueduApi.coroutineHandler(routePost22, new AnonymousClass39(bookController, null));
                Route routePost23 = router.post("/reader3/saveBookContent");
                Intrinsics.checkNotNullExpressionValue(routePost23, "router.post(\"/reader3/saveBookContent\")");
                yueduApi.coroutineHandler(routePost23, new AnonymousClass40(bookController, null));
                Route routePost24 = router.post("/reader3/saveBookProgress");
                Intrinsics.checkNotNullExpressionValue(routePost24, "router.post(\"/reader3/saveBookProgress\")");
                yueduApi.coroutineHandler(routePost24, new AnonymousClass41(bookController, null));
                Route route13 = router.get("/reader3/cover");
                Intrinsics.checkNotNullExpressionValue(route13, "router.get(\"/reader3/cover\")");
                yueduApi.coroutineHandlerWithoutRes(route13, new AnonymousClass42(bookController, null));
                Route route14 = router.get("/reader3/searchBookSource");
                Intrinsics.checkNotNullExpressionValue(route14, "router.get(\"/reader3/searchBookSource\")");
                yueduApi.coroutineHandler(route14, new AnonymousClass43(bookController, null));
                Route routePost25 = router.post("/reader3/searchBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost25, "router.post(\"/reader3/searchBookSource\")");
                yueduApi.coroutineHandler(routePost25, new AnonymousClass44(bookController, null));
                Route route15 = router.get("/reader3/getAvailableBookSource");
                Intrinsics.checkNotNullExpressionValue(route15, "router.get(\"/reader3/getAvailableBookSource\")");
                yueduApi.coroutineHandler(route15, new AnonymousClass45(bookController, null));
                Route routePost26 = router.post("/reader3/getAvailableBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost26, "router.post(\"/reader3/getAvailableBookSource\")");
                yueduApi.coroutineHandler(routePost26, new AnonymousClass46(bookController, null));
                Route route16 = router.get("/reader3/searchBookSourceSSE");
                Intrinsics.checkNotNullExpressionValue(route16, "router.get(\"/reader3/searchBookSourceSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route16, new AnonymousClass47(bookController, null));
                Route route17 = router.get("/reader3/setBookSource");
                Intrinsics.checkNotNullExpressionValue(route17, "router.get(\"/reader3/setBookSource\")");
                yueduApi.coroutineHandler(route17, new AnonymousClass48(bookController, null));
                Route routePost27 = router.post("/reader3/setBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost27, "router.post(\"/reader3/setBookSource\")");
                yueduApi.coroutineHandler(routePost27, new AnonymousClass49(bookController, null));
                Route routePost28 = router.post("/reader3/saveBookGroupId");
                Intrinsics.checkNotNullExpressionValue(routePost28, "router.post(\"/reader3/saveBookGroupId\")");
                yueduApi.coroutineHandler(routePost28, new AnonymousClass50(bookController, null));
                Route routePost29 = router.post("/reader3/addBookGroupMulti");
                Intrinsics.checkNotNullExpressionValue(routePost29, "router.post(\"/reader3/addBookGroupMulti\")");
                yueduApi.coroutineHandler(routePost29, new AnonymousClass51(bookController, null));
                Route routePost30 = router.post("/reader3/removeBookGroupMulti");
                Intrinsics.checkNotNullExpressionValue(routePost30, "router.post(\"/reader3/removeBookGroupMulti\")");
                yueduApi.coroutineHandler(routePost30, new AnonymousClass52(bookController, null));
                Route routePost31 = router.post("/reader3/importBookPreview");
                Intrinsics.checkNotNullExpressionValue(routePost31, "router.post(\"/reader3/importBookPreview\")");
                yueduApi.coroutineHandler(routePost31, new AnonymousClass53(bookController, null));
                Route routePost32 = router.post("/reader3/refreshLocalBook");
                Intrinsics.checkNotNullExpressionValue(routePost32, "router.post(\"/reader3/refreshLocalBook\")");
                yueduApi.coroutineHandler(routePost32, new AnonymousClass54(bookController, null));
                Route route18 = router.get("/reader3/getTxtTocRules");
                Intrinsics.checkNotNullExpressionValue(route18, "router.get(\"/reader3/getTxtTocRules\")");
                yueduApi.coroutineHandler(route18, new AnonymousClass55(bookController, null));
                Route routePost33 = router.post("/reader3/getChapterListByRule");
                Intrinsics.checkNotNullExpressionValue(routePost33, "router.post(\"/reader3/getChapterListByRule\")");
                yueduApi.coroutineHandler(routePost33, new AnonymousClass56(bookController, null));
                Route route19 = router.get("/reader3/getBookGroups");
                Intrinsics.checkNotNullExpressionValue(route19, "router.get(\"/reader3/getBookGroups\")");
                yueduApi.coroutineHandler(route19, new AnonymousClass57(bookGroupController, null));
                Route routePost34 = router.post("/reader3/saveBookGroup");
                Intrinsics.checkNotNullExpressionValue(routePost34, "router.post(\"/reader3/saveBookGroup\")");
                yueduApi.coroutineHandler(routePost34, new AnonymousClass58(bookGroupController, null));
                Route routePost35 = router.post("/reader3/deleteBookGroup");
                Intrinsics.checkNotNullExpressionValue(routePost35, "router.post(\"/reader3/deleteBookGroup\")");
                yueduApi.coroutineHandler(routePost35, new AnonymousClass59(bookGroupController, null));
                Route routePost36 = router.post("/reader3/saveBookGroupOrder");
                Intrinsics.checkNotNullExpressionValue(routePost36, "router.post(\"/reader3/saveBookGroupOrder\")");
                yueduApi.coroutineHandler(routePost36, new AnonymousClass60(bookGroupController, null));
                Route route20 = router.get("/reader3/bookSourceDebugSSE");
                Intrinsics.checkNotNullExpressionValue(route20, "router.get(\"/reader3/bookSourceDebugSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route20, new AnonymousClass61(bookController, null));
                Route route21 = router.get("/reader3/cacheBookSSE");
                Intrinsics.checkNotNullExpressionValue(route21, "router.get(\"/reader3/cacheBookSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route21, new AnonymousClass62(bookController, null));
                Route routePost37 = router.post("/reader3/cacheBookOnServer");
                Intrinsics.checkNotNullExpressionValue(routePost37, "router.post(\"/reader3/cacheBookOnServer\")");
                yueduApi.coroutineHandler(routePost37, new AnonymousClass63(bookController, null));
                Route route22 = router.get("/reader3/getShelfBookWithCacheInfo");
                Intrinsics.checkNotNullExpressionValue(route22, "router.get(\"/reader3/getShelfBookWithCacheInfo\")");
                yueduApi.coroutineHandler(route22, new AnonymousClass64(bookController, null));
                Route routePost38 = router.post("/reader3/deleteBookCache");
                Intrinsics.checkNotNullExpressionValue(routePost38, "router.post(\"/reader3/deleteBookCache\")");
                yueduApi.coroutineHandler(routePost38, new AnonymousClass65(bookController, null));
                Route routePost39 = router.post("/reader3/exportBook");
                Intrinsics.checkNotNullExpressionValue(routePost39, "router.post(\"/reader3/exportBook\")");
                yueduApi.coroutineHandlerWithoutRes(routePost39, new AnonymousClass66(bookController, null));
                Route route23 = router.get("/reader3/exportBook");
                Intrinsics.checkNotNullExpressionValue(route23, "router.get(\"/reader3/exportBook\")");
                yueduApi.coroutineHandlerWithoutRes(route23, new AnonymousClass67(bookController, null));
                Route route24 = router.get("/reader3/searchBookContent");
                Intrinsics.checkNotNullExpressionValue(route24, "router.get(\"/reader3/searchBookContent\")");
                yueduApi.coroutineHandler(route24, new AnonymousClass68(bookController, null));
                Route routePost40 = router.post("/reader3/searchBookContent");
                Intrinsics.checkNotNullExpressionValue(routePost40, "router.post(\"/reader3/searchBookContent\")");
                yueduApi.coroutineHandler(routePost40, new AnonymousClass69(bookController, null));
                Route routePost41 = router.post("/reader3/backupToMongodb");
                Intrinsics.checkNotNullExpressionValue(routePost41, "router.post(\"/reader3/backupToMongodb\")");
                yueduApi.coroutineHandler(routePost41, new AnonymousClass70(bookController, null));
                Route routePost42 = router.post("/reader3/restoreFromMongodb");
                Intrinsics.checkNotNullExpressionValue(routePost42, "router.post(\"/reader3/restoreFromMongodb\")");
                yueduApi.coroutineHandler(routePost42, new AnonymousClass71(bookController, null));
                Route routePost43 = router.post("/reader3/book/saveBookConfig");
                Intrinsics.checkNotNullExpressionValue(routePost43, "router.post(\"/reader3/book/saveBookConfig\")");
                yueduApi.coroutineHandler(routePost43, new AnonymousClass72(bookController, null));
                Route route25 = router.get("/reader3/book/tts");
                Intrinsics.checkNotNullExpressionValue(route25, "router.get(\"/reader3/book/tts\")");
                yueduApi.coroutineHandlerWithoutRes(route25, new AnonymousClass73(bookController, null));
                Route routePost44 = router.post("/reader3/book/tts");
                Intrinsics.checkNotNullExpressionValue(routePost44, "router.post(\"/reader3/book/tts\")");
                yueduApi.coroutineHandlerWithoutRes(routePost44, new AnonymousClass74(bookController, null));
                Route routePost45 = router.post("/reader3/uploadFile");
                Intrinsics.checkNotNullExpressionValue(routePost45, "router.post(\"/reader3/uploadFile\")");
                yueduApi.coroutineHandler(routePost45, new AnonymousClass75(userController, null));
                Route routePost46 = router.post("/reader3/deleteFile");
                Intrinsics.checkNotNullExpressionValue(routePost46, "router.post(\"/reader3/deleteFile\")");
                yueduApi.coroutineHandler(routePost46, new AnonymousClass76(userController, null));
                Route routePost47 = router.post("/reader3/login");
                Intrinsics.checkNotNullExpressionValue(routePost47, "router.post(\"/reader3/login\")");
                yueduApi.coroutineHandler(routePost47, new AnonymousClass77(userController, null));
                Route routePost48 = router.post("/reader3/logout");
                Intrinsics.checkNotNullExpressionValue(routePost48, "router.post(\"/reader3/logout\")");
                yueduApi.coroutineHandler(routePost48, new AnonymousClass78(userController, null));
                Route route26 = router.get("/reader3/getUserInfo");
                Intrinsics.checkNotNullExpressionValue(route26, "router.get(\"/reader3/getUserInfo\")");
                yueduApi.coroutineHandler(route26, new AnonymousClass79(userController, null));
                Route routePost49 = router.post("/reader3/saveUserConfig");
                Intrinsics.checkNotNullExpressionValue(routePost49, "router.post(\"/reader3/saveUserConfig\")");
                yueduApi.coroutineHandler(routePost49, new AnonymousClass80(userController, null));
                Route route27 = router.get("/reader3/getUserConfig");
                Intrinsics.checkNotNullExpressionValue(route27, "router.get(\"/reader3/getUserConfig\")");
                yueduApi.coroutineHandler(route27, new AnonymousClass81(userController, null));
                Route route28 = router.get("/reader3/getUserList");
                Intrinsics.checkNotNullExpressionValue(route28, "router.get(\"/reader3/getUserList\")");
                yueduApi.coroutineHandler(route28, new AnonymousClass82(userController, null));
                Route routePost50 = router.post("/reader3/deleteUsers");
                Intrinsics.checkNotNullExpressionValue(routePost50, "router.post(\"/reader3/deleteUsers\")");
                yueduApi.coroutineHandler(routePost50, new AnonymousClass83(userController, null));
                Route routePost51 = router.post("/reader3/clearInactiveUsers");
                Intrinsics.checkNotNullExpressionValue(routePost51, "router.post(\"/reader3/clearInactiveUsers\")");
                yueduApi.coroutineHandler(routePost51, new AnonymousClass84(userController, null));
                Route routePost52 = router.post("/reader3/addUser");
                Intrinsics.checkNotNullExpressionValue(routePost52, "router.post(\"/reader3/addUser\")");
                yueduApi.coroutineHandler(routePost52, new AnonymousClass85(userController, null));
                Route routePost53 = router.post("/reader3/resetPassword");
                Intrinsics.checkNotNullExpressionValue(routePost53, "router.post(\"/reader3/resetPassword\")");
                yueduApi.coroutineHandler(routePost53, new AnonymousClass86(userController, null));
                Route routePost54 = router.post("/reader3/updateUser");
                Intrinsics.checkNotNullExpressionValue(routePost54, "router.post(\"/reader3/updateUser\")");
                yueduApi.coroutineHandler(routePost54, new AnonymousClass87(userController, null));
                Route route29 = router.get("/reader3/user/downloadBackupFile");
                Intrinsics.checkNotNullExpressionValue(route29, "router.get(\"/reader3/user/downloadBackupFile\")");
                yueduApi.coroutineHandlerWithoutRes(route29, new AnonymousClass88(userController, null));
                Route route30 = router.get("/reader3/getLicense");
                Intrinsics.checkNotNullExpressionValue(route30, "router.get(\"/reader3/getLicense\")");
                yueduApi.coroutineHandler(route30, new AnonymousClass89(licenseController, null));
                Route routePost55 = router.post("/reader3/importLicense");
                Intrinsics.checkNotNullExpressionValue(routePost55, "router.post(\"/reader3/importLicense\")");
                yueduApi.coroutineHandlerWithoutRes(routePost55, new AnonymousClass90(licenseController, null));
                Route route31 = router.get("/reader3/generateKeys");
                Intrinsics.checkNotNullExpressionValue(route31, "router.get(\"/reader3/generateKeys\")");
                yueduApi.coroutineHandler(route31, new AnonymousClass91(licenseController, null));
                Route routePost56 = router.post("/reader3/generateKeys");
                Intrinsics.checkNotNullExpressionValue(routePost56, "router.post(\"/reader3/generateKeys\")");
                yueduApi.coroutineHandler(routePost56, new AnonymousClass92(licenseController, null));
                Route route32 = router.get("/reader3/generateLicense");
                Intrinsics.checkNotNullExpressionValue(route32, "router.get(\"/reader3/generateLicense\")");
                yueduApi.coroutineHandler(route32, new AnonymousClass93(licenseController, null));
                Route routePost57 = router.post("/reader3/generateLicense");
                Intrinsics.checkNotNullExpressionValue(routePost57, "router.post(\"/reader3/generateLicense\")");
                yueduApi.coroutineHandler(routePost57, new AnonymousClass94(licenseController, null));
                Route route33 = router.get("/reader3/isHostValid");
                Intrinsics.checkNotNullExpressionValue(route33, "router.get(\"/reader3/isHostValid\")");
                yueduApi.coroutineHandler(route33, new AnonymousClass95(licenseController, null));
                Route routePost58 = router.post("/reader3/isHostValid");
                Intrinsics.checkNotNullExpressionValue(routePost58, "router.post(\"/reader3/isHostValid\")");
                yueduApi.coroutineHandler(routePost58, new AnonymousClass96(licenseController, null));
                Route routePost59 = router.post("/reader3/activateLicense");
                Intrinsics.checkNotNullExpressionValue(routePost59, "router.post(\"/reader3/activateLicense\")");
                yueduApi.coroutineHandler(routePost59, new AnonymousClass97(licenseController, null));
                Route route34 = router.get("/reader3/isLicenseValid");
                Intrinsics.checkNotNullExpressionValue(route34, "router.get(\"/reader3/isLicenseValid\")");
                yueduApi.coroutineHandler(route34, new AnonymousClass98(licenseController, null));
                Route routePost60 = router.post("/reader3/isLicenseValid");
                Intrinsics.checkNotNullExpressionValue(routePost60, "router.post(\"/reader3/isLicenseValid\")");
                yueduApi.coroutineHandler(routePost60, new AnonymousClass99(licenseController, null));
                Route routePost61 = router.post("/reader3/decryptLicense");
                Intrinsics.checkNotNullExpressionValue(routePost61, "router.post(\"/reader3/decryptLicense\")");
                yueduApi.coroutineHandler(routePost61, new AnonymousClass100(licenseController, null));
                Route routePost62 = router.post("/reader3/sendCodeToEmail");
                Intrinsics.checkNotNullExpressionValue(routePost62, "router.post(\"/reader3/sendCodeToEmail\")");
                yueduApi.coroutineHandler(routePost62, new AnonymousClass101(licenseController, null));
                Route routePost63 = router.post("/reader3/supplyLicense");
                Intrinsics.checkNotNullExpressionValue(routePost63, "router.post(\"/reader3/supplyLicense\")");
                yueduApi.coroutineHandler(routePost63, new AnonymousClass102(licenseController, null));
                Route routePost64 = router.post("/reader3/backupToWebdav");
                Intrinsics.checkNotNullExpressionValue(routePost64, "router.post(\"/reader3/backupToWebdav\")");
                yueduApi.coroutineHandler(routePost64, new AnonymousClass103(webdavController, null));
                Route route35 = router.get("/reader3/getRssSources");
                Intrinsics.checkNotNullExpressionValue(route35, "router.get(\"/reader3/getRssSources\")");
                yueduApi.coroutineHandler(route35, new AnonymousClass104(rssSourceController, null));
                Route routePost65 = router.post("/reader3/saveRssSource");
                Intrinsics.checkNotNullExpressionValue(routePost65, "router.post(\"/reader3/saveRssSource\")");
                yueduApi.coroutineHandler(routePost65, new AnonymousClass105(rssSourceController, null));
                Route routePost66 = router.post("/reader3/saveRssSources");
                Intrinsics.checkNotNullExpressionValue(routePost66, "router.post(\"/reader3/saveRssSources\")");
                yueduApi.coroutineHandler(routePost66, new AnonymousClass106(rssSourceController, null));
                Route routePost67 = router.post("/reader3/deleteRssSource");
                Intrinsics.checkNotNullExpressionValue(routePost67, "router.post(\"/reader3/deleteRssSource\")");
                yueduApi.coroutineHandler(routePost67, new AnonymousClass107(rssSourceController, null));
                Route route36 = router.get("/reader3/getRssArticles");
                Intrinsics.checkNotNullExpressionValue(route36, "router.get(\"/reader3/getRssArticles\")");
                yueduApi.coroutineHandler(route36, new AnonymousClass108(rssSourceController, null));
                Route routePost68 = router.post("/reader3/getRssArticles");
                Intrinsics.checkNotNullExpressionValue(routePost68, "router.post(\"/reader3/getRssArticles\")");
                yueduApi.coroutineHandler(routePost68, new AnonymousClass109(rssSourceController, null));
                Route route37 = router.get("/reader3/getRssContent");
                Intrinsics.checkNotNullExpressionValue(route37, "router.get(\"/reader3/getRssContent\")");
                yueduApi.coroutineHandler(route37, new AnonymousClass110(rssSourceController, null));
                Route routePost69 = router.post("/reader3/getRssContent");
                Intrinsics.checkNotNullExpressionValue(routePost69, "router.post(\"/reader3/getRssContent\")");
                yueduApi.coroutineHandler(routePost69, new AnonymousClass111(rssSourceController, null));
                Route route38 = router.get("/reader3/getReplaceRules");
                Intrinsics.checkNotNullExpressionValue(route38, "router.get(\"/reader3/getReplaceRules\")");
                yueduApi.coroutineHandler(route38, new AnonymousClass112(replaceRuleController, null));
                Route routePost70 = router.post("/reader3/saveReplaceRule");
                Intrinsics.checkNotNullExpressionValue(routePost70, "router.post(\"/reader3/saveReplaceRule\")");
                yueduApi.coroutineHandler(routePost70, new AnonymousClass113(replaceRuleController, null));
                Route routePost71 = router.post("/reader3/saveReplaceRules");
                Intrinsics.checkNotNullExpressionValue(routePost71, "router.post(\"/reader3/saveReplaceRules\")");
                yueduApi.coroutineHandler(routePost71, new AnonymousClass114(replaceRuleController, null));
                Route routePost72 = router.post("/reader3/deleteReplaceRule");
                Intrinsics.checkNotNullExpressionValue(routePost72, "router.post(\"/reader3/deleteReplaceRule\")");
                yueduApi.coroutineHandler(routePost72, new AnonymousClass115(replaceRuleController, null));
                Route routePost73 = router.post("/reader3/deleteReplaceRules");
                Intrinsics.checkNotNullExpressionValue(routePost73, "router.post(\"/reader3/deleteReplaceRules\")");
                yueduApi.coroutineHandler(routePost73, new AnonymousClass116(replaceRuleController, null));
                Route route39 = router.get("/reader3/getBookmarks");
                Intrinsics.checkNotNullExpressionValue(route39, "router.get(\"/reader3/getBookmarks\")");
                yueduApi.coroutineHandler(route39, new AnonymousClass117(bookmarkController, null));
                Route routePost74 = router.post("/reader3/saveBookmark");
                Intrinsics.checkNotNullExpressionValue(routePost74, "router.post(\"/reader3/saveBookmark\")");
                yueduApi.coroutineHandler(routePost74, new AnonymousClass118(bookmarkController, null));
                Route routePost75 = router.post("/reader3/saveBookmarks");
                Intrinsics.checkNotNullExpressionValue(routePost75, "router.post(\"/reader3/saveBookmarks\")");
                yueduApi.coroutineHandler(routePost75, new AnonymousClass119(bookmarkController, null));
                Route routePost76 = router.post("/reader3/deleteBookmark");
                Intrinsics.checkNotNullExpressionValue(routePost76, "router.post(\"/reader3/deleteBookmark\")");
                yueduApi.coroutineHandler(routePost76, new AnonymousClass120(bookmarkController, null));
                Route routePost77 = router.post("/reader3/deleteBookmarks");
                Intrinsics.checkNotNullExpressionValue(routePost77, "router.post(\"/reader3/deleteBookmarks\")");
                yueduApi.coroutineHandler(routePost77, new AnonymousClass121(bookmarkController, null));
                Route route40 = router.get("/reader3/file/list");
                Intrinsics.checkNotNullExpressionValue(route40, "router.get(\"/reader3/file/list\")");
                yueduApi.coroutineHandler(route40, new AnonymousClass122(fileController, null));
                Route route41 = router.get("/reader3/file/get");
                Intrinsics.checkNotNullExpressionValue(route41, "router.get(\"/reader3/file/get\")");
                yueduApi.coroutineHandler(route41, new AnonymousClass123(fileController, null));
                Route routePost78 = router.post("/reader3/file/save");
                Intrinsics.checkNotNullExpressionValue(routePost78, "router.post(\"/reader3/file/save\")");
                yueduApi.coroutineHandler(routePost78, new AnonymousClass124(fileController, null));
                Route routePost79 = router.post("/reader3/file/mkdir");
                Intrinsics.checkNotNullExpressionValue(routePost79, "router.post(\"/reader3/file/mkdir\")");
                yueduApi.coroutineHandler(routePost79, new AnonymousClass125(fileController, null));
                Route route42 = router.get("/reader3/file/download");
                Intrinsics.checkNotNullExpressionValue(route42, "router.get(\"/reader3/file/download\")");
                yueduApi.coroutineHandlerWithoutRes(route42, new AnonymousClass126(fileController, null));
                Route routePost80 = router.post("/reader3/file/upload");
                Intrinsics.checkNotNullExpressionValue(routePost80, "router.post(\"/reader3/file/upload\")");
                yueduApi.coroutineHandler(routePost80, new AnonymousClass127(fileController, null));
                Route routePost81 = router.post("/reader3/file/delete");
                Intrinsics.checkNotNullExpressionValue(routePost81, "router.post(\"/reader3/file/delete\")");
                yueduApi.coroutineHandler(routePost81, new AnonymousClass128(fileController, null));
                Route routePost82 = router.post("/reader3/file/deleteMulti");
                Intrinsics.checkNotNullExpressionValue(routePost82, "router.post(\"/reader3/file/deleteMulti\")");
                yueduApi.coroutineHandler(routePost82, new AnonymousClass129(fileController, null));
                Route routePost83 = router.post("/reader3/file/importPreview");
                Intrinsics.checkNotNullExpressionValue(routePost83, "router.post(\"/reader3/file/importPreview\")");
                yueduApi.coroutineHandler(routePost83, new AnonymousClass130(fileController, null));
                Route routePost84 = router.post("/reader3/file/restore");
                Intrinsics.checkNotNullExpressionValue(routePost84, "router.post(\"/reader3/file/restore\")");
                yueduApi.coroutineHandler(routePost84, new AnonymousClass131(fileController, null));
                Route route43 = router.get("/reader3/file/parse");
                Intrinsics.checkNotNullExpressionValue(route43, "router.get(\"/reader3/file/parse\")");
                yueduApi.coroutineHandler(route43, new AnonymousClass132(fileController, null));
                Route routePost85 = router.post("/reader3/file/parse");
                Intrinsics.checkNotNullExpressionValue(routePost85, "router.post(\"/reader3/file/parse\")");
                yueduApi.coroutineHandler(routePost85, new AnonymousClass133(fileController, null));
                Route route44 = router.get("/reader3/httpTTS/list");
                Intrinsics.checkNotNullExpressionValue(route44, "router.get(\"/reader3/httpTTS/list\")");
                yueduApi.coroutineHandler(route44, new AnonymousClass134(httpTTSController, null));
                Route routePost86 = router.post("/reader3/httpTTS/save");
                Intrinsics.checkNotNullExpressionValue(routePost86, "router.post(\"/reader3/httpTTS/save\")");
                yueduApi.coroutineHandler(routePost86, new AnonymousClass135(httpTTSController, null));
                Route routePost87 = router.post("/reader3/httpTTS/saveMulti");
                Intrinsics.checkNotNullExpressionValue(routePost87, "router.post(\"/reader3/httpTTS/saveMulti\")");
                yueduApi.coroutineHandler(routePost87, new AnonymousClass136(httpTTSController, null));
                Route routePost88 = router.post("/reader3/httpTTS/delete");
                Intrinsics.checkNotNullExpressionValue(routePost88, "router.post(\"/reader3/httpTTS/delete\")");
                yueduApi.coroutineHandler(routePost88, new AnonymousClass137(httpTTSController, null));
                Route routePost89 = router.post("/reader3/httpTTS/deleteMulti");
                Intrinsics.checkNotNullExpressionValue(routePost89, "router.post(\"/reader3/httpTTS/deleteMulti\")");
                yueduApi.coroutineHandler(routePost89, new AnonymousClass138(httpTTSController, null));
                return Unit.INSTANCE;
            case 1:
                router = (Router) c00071.L$1;
                yueduApi = (YueduApi) c00071.L$0;
                ResultKt.throwOnFailure($result);
                appConfig = yueduApi.appConfig;
                if (appConfig != null) {
                }
                break;
            case 2:
                router = (Router) c00071.L$1;
                yueduApi = (YueduApi) c00071.L$0;
                ResultKt.throwOnFailure($result);
                router.route("/*").handler(StaticHandler.create("web").setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                String assetsDir2 = ExtKt.getWorkDir("storage", "assets");
                assetsDirFile = new File(assetsDir2);
                if (!assetsDirFile.exists()) {
                }
                String assetsCss2 = ExtKt.getWorkDir("storage", "assets", "reader.css");
                assetsCssFile = new File(assetsCss2);
                if (!assetsCssFile.exists()) {
                }
                router.route("/assets/*").handler(StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot(assetsDir2).setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                Ref.ObjectRef dataDir2 = new Ref.ObjectRef();
                dataDir2.element = ExtKt.getWorkDir("storage", "data");
                router.route("/book-assets/*").handler((v1) -> {
                    m7initRouter$lambda0(r1, v1);
                });
                router.route("/book-assets/*").handler(StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot((String) dataDir2.element).setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                router.route("/epub/*").handler((v1) -> {
                    m8initRouter$lambda1(r1, v1);
                });
                router.route("/epub/*").handler(StaticHandler.create().setAllowRootFileSystemAccess(true).setWebRoot((String) dataDir2.element).setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                router.route("/simple-web").handler(YueduApi::m9initRouter$lambda2);
                router.route("/simple-web/*").handler(YueduApi::m10initRouter$lambda3);
                router.route("/simple-web/*").handler(StaticHandler.create("simple-web").setDefaultContentEncoding(Constants.CHARACTER_ENCODING));
                Route route45 = router.get("/reader3/getSystemInfo");
                Intrinsics.checkNotNullExpressionValue(route45, "router.get(\"/reader3/getSystemInfo\")");
                yueduApi.coroutineHandler(route45, yueduApi.new AnonymousClass6(null));
                BookController bookController2 = new BookController(yueduApi.getCoroutineContext());
                BookGroupController bookGroupController2 = new BookGroupController(yueduApi.getCoroutineContext());
                BookSourceController bookSourceController2 = new BookSourceController(yueduApi.getCoroutineContext());
                RssSourceController rssSourceController2 = new RssSourceController(yueduApi.getCoroutineContext());
                UserController userController2 = new UserController(yueduApi.getCoroutineContext());
                WebdavController webdavController2 = new WebdavController(yueduApi.getCoroutineContext(), router, new YueduApi$initRouter$webdavController$1(yueduApi));
                ReplaceRuleController replaceRuleController2 = new ReplaceRuleController(yueduApi.getCoroutineContext());
                BookmarkController bookmarkController2 = new BookmarkController(yueduApi.getCoroutineContext());
                FileController fileController2 = new FileController(yueduApi.getCoroutineContext());
                LicenseController licenseController2 = new LicenseController(yueduApi.getCoroutineContext());
                HttpTTSController httpTTSController2 = new HttpTTSController(yueduApi.getCoroutineContext());
                Route routePost90 = router.post("/reader3/saveBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost90, "router.post(\"/reader3/saveBookSource\")");
                yueduApi.coroutineHandler(routePost90, new AnonymousClass7(bookSourceController2, null));
                Route routePost210 = router.post("/reader3/saveBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost210, "router.post(\"/reader3/saveBookSources\")");
                yueduApi.coroutineHandler(routePost210, new AnonymousClass8(bookSourceController2, null));
                Route route210 = router.get("/reader3/getBookSource");
                Intrinsics.checkNotNullExpressionValue(route210, "router.get(\"/reader3/getBookSource\")");
                yueduApi.coroutineHandler(route210, new AnonymousClass9(bookSourceController2, null));
                Route routePost310 = router.post("/reader3/getBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost310, "router.post(\"/reader3/getBookSource\")");
                yueduApi.coroutineHandler(routePost310, new AnonymousClass10(bookSourceController2, null));
                Route route310 = router.get("/reader3/getBookSources");
                Intrinsics.checkNotNullExpressionValue(route310, "router.get(\"/reader3/getBookSources\")");
                yueduApi.coroutineHandler(route310, new AnonymousClass11(bookSourceController2, null));
                Route routePost410 = router.post("/reader3/getBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost410, "router.post(\"/reader3/getBookSources\")");
                yueduApi.coroutineHandler(routePost410, new AnonymousClass12(bookSourceController2, null));
                Route routePost510 = router.post("/reader3/deleteAllBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost510, "router.post(\"/reader3/deleteAllBookSources\")");
                yueduApi.coroutineHandler(routePost510, new AnonymousClass13(bookSourceController2, null));
                Route routePost610 = router.post("/reader3/deleteBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost610, "router.post(\"/reader3/deleteBookSource\")");
                yueduApi.coroutineHandler(routePost610, new AnonymousClass14(bookSourceController2, null));
                Route routePost710 = router.post("/reader3/deleteBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost710, "router.post(\"/reader3/deleteBookSources\")");
                yueduApi.coroutineHandler(routePost710, new AnonymousClass15(bookSourceController2, null));
                Route routePost810 = router.post("/reader3/readSourceFile");
                Intrinsics.checkNotNullExpressionValue(routePost810, "router.post(\"/reader3/readSourceFile\")");
                yueduApi.coroutineHandler(routePost810, new AnonymousClass16(bookSourceController2, null));
                Route routePost92 = router.post("/reader3/saveFromRemoteSource");
                Intrinsics.checkNotNullExpressionValue(routePost92, "router.post(\"/reader3/saveFromRemoteSource\")");
                yueduApi.coroutineHandlerWithoutRes(routePost92, new AnonymousClass17(bookSourceController2, null));
                Route routePost102 = router.post("/reader3/setAsDefaultBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost102, "router.post(\"/reader3/setAsDefaultBookSources\")");
                yueduApi.coroutineHandler(routePost102, new AnonymousClass18(bookSourceController2, null));
                Route routePost112 = router.post("/reader3/deleteUserBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost112, "router.post(\"/reader3/deleteUserBookSource\")");
                yueduApi.coroutineHandler(routePost112, new AnonymousClass19(bookSourceController2, null));
                Route routePost122 = router.post("/reader3/deleteBookSourcesFile");
                Intrinsics.checkNotNullExpressionValue(routePost122, "router.post(\"/reader3/deleteBookSourcesFile\")");
                yueduApi.coroutineHandler(routePost122, new AnonymousClass20(bookSourceController2, null));
                Route route46 = router.get("/reader3/getBookshelf");
                Intrinsics.checkNotNullExpressionValue(route46, "router.get(\"/reader3/getBookshelf\")");
                yueduApi.coroutineHandler(route46, new AnonymousClass21(bookController2, null));
                Route route52 = router.get("/reader3/getShelfBook");
                Intrinsics.checkNotNullExpressionValue(route52, "router.get(\"/reader3/getShelfBook\")");
                yueduApi.coroutineHandler(route52, new AnonymousClass22(bookController2, null));
                Route routePost132 = router.post("/reader3/saveBook");
                Intrinsics.checkNotNullExpressionValue(routePost132, "router.post(\"/reader3/saveBook\")");
                yueduApi.coroutineHandler(routePost132, new AnonymousClass23(bookController2, null));
                Route routePost142 = router.post("/reader3/deleteBook");
                Intrinsics.checkNotNullExpressionValue(routePost142, "router.post(\"/reader3/deleteBook\")");
                yueduApi.coroutineHandler(routePost142, new AnonymousClass24(bookController2, null));
                Route routePost152 = router.post("/reader3/deleteBooks");
                Intrinsics.checkNotNullExpressionValue(routePost152, "router.post(\"/reader3/deleteBooks\")");
                yueduApi.coroutineHandler(routePost152, new AnonymousClass25(bookController2, null));
                Route routePost162 = router.post("/reader3/getInvalidBookSources");
                Intrinsics.checkNotNullExpressionValue(routePost162, "router.post(\"/reader3/getInvalidBookSources\")");
                yueduApi.coroutineHandler(routePost162, new AnonymousClass26(bookController2, null));
                Route routePost172 = router.post("/reader3/exploreBook");
                Intrinsics.checkNotNullExpressionValue(routePost172, "router.post(\"/reader3/exploreBook\")");
                yueduApi.coroutineHandler(routePost172, new AnonymousClass27(bookController2, null));
                Route route62 = router.get("/reader3/exploreBook");
                Intrinsics.checkNotNullExpressionValue(route62, "router.get(\"/reader3/exploreBook\")");
                yueduApi.coroutineHandler(route62, new AnonymousClass28(bookController2, null));
                Route route72 = router.get("/reader3/searchBook");
                Intrinsics.checkNotNullExpressionValue(route72, "router.get(\"/reader3/searchBook\")");
                yueduApi.coroutineHandler(route72, new AnonymousClass29(bookController2, null));
                Route routePost182 = router.post("/reader3/searchBook");
                Intrinsics.checkNotNullExpressionValue(routePost182, "router.post(\"/reader3/searchBook\")");
                yueduApi.coroutineHandler(routePost182, new AnonymousClass30(bookController2, null));
                Route route82 = router.get("/reader3/searchBookMulti");
                Intrinsics.checkNotNullExpressionValue(route82, "router.get(\"/reader3/searchBookMulti\")");
                yueduApi.coroutineHandler(route82, new AnonymousClass31(bookController2, null));
                Route routePost192 = router.post("/reader3/searchBookMulti");
                Intrinsics.checkNotNullExpressionValue(routePost192, "router.post(\"/reader3/searchBookMulti\")");
                yueduApi.coroutineHandler(routePost192, new AnonymousClass32(bookController2, null));
                Route route92 = router.get("/reader3/searchBookMultiSSE");
                Intrinsics.checkNotNullExpressionValue(route92, "router.get(\"/reader3/searchBookMultiSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route92, new AnonymousClass33(bookController2, null));
                Route route102 = router.get("/reader3/getBookInfo");
                Intrinsics.checkNotNullExpressionValue(route102, "router.get(\"/reader3/getBookInfo\")");
                yueduApi.coroutineHandler(route102, new AnonymousClass34(bookController2, null));
                Route routePost202 = router.post("/reader3/getBookInfo");
                Intrinsics.checkNotNullExpressionValue(routePost202, "router.post(\"/reader3/getBookInfo\")");
                yueduApi.coroutineHandler(routePost202, new AnonymousClass35(bookController2, null));
                Route route112 = router.get("/reader3/getChapterList");
                Intrinsics.checkNotNullExpressionValue(route112, "router.get(\"/reader3/getChapterList\")");
                yueduApi.coroutineHandler(route112, new AnonymousClass36(bookController2, null));
                Route routePost212 = router.post("/reader3/getChapterList");
                Intrinsics.checkNotNullExpressionValue(routePost212, "router.post(\"/reader3/getChapterList\")");
                yueduApi.coroutineHandler(routePost212, new AnonymousClass37(bookController2, null));
                Route route122 = router.get("/reader3/getBookContent");
                Intrinsics.checkNotNullExpressionValue(route122, "router.get(\"/reader3/getBookContent\")");
                yueduApi.coroutineHandler(route122, new AnonymousClass38(bookController2, null));
                Route routePost222 = router.post("/reader3/getBookContent");
                Intrinsics.checkNotNullExpressionValue(routePost222, "router.post(\"/reader3/getBookContent\")");
                yueduApi.coroutineHandler(routePost222, new AnonymousClass39(bookController2, null));
                Route routePost232 = router.post("/reader3/saveBookContent");
                Intrinsics.checkNotNullExpressionValue(routePost232, "router.post(\"/reader3/saveBookContent\")");
                yueduApi.coroutineHandler(routePost232, new AnonymousClass40(bookController2, null));
                Route routePost242 = router.post("/reader3/saveBookProgress");
                Intrinsics.checkNotNullExpressionValue(routePost242, "router.post(\"/reader3/saveBookProgress\")");
                yueduApi.coroutineHandler(routePost242, new AnonymousClass41(bookController2, null));
                Route route132 = router.get("/reader3/cover");
                Intrinsics.checkNotNullExpressionValue(route132, "router.get(\"/reader3/cover\")");
                yueduApi.coroutineHandlerWithoutRes(route132, new AnonymousClass42(bookController2, null));
                Route route142 = router.get("/reader3/searchBookSource");
                Intrinsics.checkNotNullExpressionValue(route142, "router.get(\"/reader3/searchBookSource\")");
                yueduApi.coroutineHandler(route142, new AnonymousClass43(bookController2, null));
                Route routePost252 = router.post("/reader3/searchBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost252, "router.post(\"/reader3/searchBookSource\")");
                yueduApi.coroutineHandler(routePost252, new AnonymousClass44(bookController2, null));
                Route route152 = router.get("/reader3/getAvailableBookSource");
                Intrinsics.checkNotNullExpressionValue(route152, "router.get(\"/reader3/getAvailableBookSource\")");
                yueduApi.coroutineHandler(route152, new AnonymousClass45(bookController2, null));
                Route routePost262 = router.post("/reader3/getAvailableBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost262, "router.post(\"/reader3/getAvailableBookSource\")");
                yueduApi.coroutineHandler(routePost262, new AnonymousClass46(bookController2, null));
                Route route162 = router.get("/reader3/searchBookSourceSSE");
                Intrinsics.checkNotNullExpressionValue(route162, "router.get(\"/reader3/searchBookSourceSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route162, new AnonymousClass47(bookController2, null));
                Route route172 = router.get("/reader3/setBookSource");
                Intrinsics.checkNotNullExpressionValue(route172, "router.get(\"/reader3/setBookSource\")");
                yueduApi.coroutineHandler(route172, new AnonymousClass48(bookController2, null));
                Route routePost272 = router.post("/reader3/setBookSource");
                Intrinsics.checkNotNullExpressionValue(routePost272, "router.post(\"/reader3/setBookSource\")");
                yueduApi.coroutineHandler(routePost272, new AnonymousClass49(bookController2, null));
                Route routePost282 = router.post("/reader3/saveBookGroupId");
                Intrinsics.checkNotNullExpressionValue(routePost282, "router.post(\"/reader3/saveBookGroupId\")");
                yueduApi.coroutineHandler(routePost282, new AnonymousClass50(bookController2, null));
                Route routePost292 = router.post("/reader3/addBookGroupMulti");
                Intrinsics.checkNotNullExpressionValue(routePost292, "router.post(\"/reader3/addBookGroupMulti\")");
                yueduApi.coroutineHandler(routePost292, new AnonymousClass51(bookController2, null));
                Route routePost302 = router.post("/reader3/removeBookGroupMulti");
                Intrinsics.checkNotNullExpressionValue(routePost302, "router.post(\"/reader3/removeBookGroupMulti\")");
                yueduApi.coroutineHandler(routePost302, new AnonymousClass52(bookController2, null));
                Route routePost312 = router.post("/reader3/importBookPreview");
                Intrinsics.checkNotNullExpressionValue(routePost312, "router.post(\"/reader3/importBookPreview\")");
                yueduApi.coroutineHandler(routePost312, new AnonymousClass53(bookController2, null));
                Route routePost322 = router.post("/reader3/refreshLocalBook");
                Intrinsics.checkNotNullExpressionValue(routePost322, "router.post(\"/reader3/refreshLocalBook\")");
                yueduApi.coroutineHandler(routePost322, new AnonymousClass54(bookController2, null));
                Route route182 = router.get("/reader3/getTxtTocRules");
                Intrinsics.checkNotNullExpressionValue(route182, "router.get(\"/reader3/getTxtTocRules\")");
                yueduApi.coroutineHandler(route182, new AnonymousClass55(bookController2, null));
                Route routePost332 = router.post("/reader3/getChapterListByRule");
                Intrinsics.checkNotNullExpressionValue(routePost332, "router.post(\"/reader3/getChapterListByRule\")");
                yueduApi.coroutineHandler(routePost332, new AnonymousClass56(bookController2, null));
                Route route192 = router.get("/reader3/getBookGroups");
                Intrinsics.checkNotNullExpressionValue(route192, "router.get(\"/reader3/getBookGroups\")");
                yueduApi.coroutineHandler(route192, new AnonymousClass57(bookGroupController2, null));
                Route routePost342 = router.post("/reader3/saveBookGroup");
                Intrinsics.checkNotNullExpressionValue(routePost342, "router.post(\"/reader3/saveBookGroup\")");
                yueduApi.coroutineHandler(routePost342, new AnonymousClass58(bookGroupController2, null));
                Route routePost352 = router.post("/reader3/deleteBookGroup");
                Intrinsics.checkNotNullExpressionValue(routePost352, "router.post(\"/reader3/deleteBookGroup\")");
                yueduApi.coroutineHandler(routePost352, new AnonymousClass59(bookGroupController2, null));
                Route routePost362 = router.post("/reader3/saveBookGroupOrder");
                Intrinsics.checkNotNullExpressionValue(routePost362, "router.post(\"/reader3/saveBookGroupOrder\")");
                yueduApi.coroutineHandler(routePost362, new AnonymousClass60(bookGroupController2, null));
                Route route202 = router.get("/reader3/bookSourceDebugSSE");
                Intrinsics.checkNotNullExpressionValue(route202, "router.get(\"/reader3/bookSourceDebugSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route202, new AnonymousClass61(bookController2, null));
                Route route212 = router.get("/reader3/cacheBookSSE");
                Intrinsics.checkNotNullExpressionValue(route212, "router.get(\"/reader3/cacheBookSSE\")");
                yueduApi.coroutineHandlerWithoutRes(route212, new AnonymousClass62(bookController2, null));
                Route routePost372 = router.post("/reader3/cacheBookOnServer");
                Intrinsics.checkNotNullExpressionValue(routePost372, "router.post(\"/reader3/cacheBookOnServer\")");
                yueduApi.coroutineHandler(routePost372, new AnonymousClass63(bookController2, null));
                Route route222 = router.get("/reader3/getShelfBookWithCacheInfo");
                Intrinsics.checkNotNullExpressionValue(route222, "router.get(\"/reader3/getShelfBookWithCacheInfo\")");
                yueduApi.coroutineHandler(route222, new AnonymousClass64(bookController2, null));
                Route routePost382 = router.post("/reader3/deleteBookCache");
                Intrinsics.checkNotNullExpressionValue(routePost382, "router.post(\"/reader3/deleteBookCache\")");
                yueduApi.coroutineHandler(routePost382, new AnonymousClass65(bookController2, null));
                Route routePost392 = router.post("/reader3/exportBook");
                Intrinsics.checkNotNullExpressionValue(routePost392, "router.post(\"/reader3/exportBook\")");
                yueduApi.coroutineHandlerWithoutRes(routePost392, new AnonymousClass66(bookController2, null));
                Route route232 = router.get("/reader3/exportBook");
                Intrinsics.checkNotNullExpressionValue(route232, "router.get(\"/reader3/exportBook\")");
                yueduApi.coroutineHandlerWithoutRes(route232, new AnonymousClass67(bookController2, null));
                Route route242 = router.get("/reader3/searchBookContent");
                Intrinsics.checkNotNullExpressionValue(route242, "router.get(\"/reader3/searchBookContent\")");
                yueduApi.coroutineHandler(route242, new AnonymousClass68(bookController2, null));
                Route routePost402 = router.post("/reader3/searchBookContent");
                Intrinsics.checkNotNullExpressionValue(routePost402, "router.post(\"/reader3/searchBookContent\")");
                yueduApi.coroutineHandler(routePost402, new AnonymousClass69(bookController2, null));
                Route routePost412 = router.post("/reader3/backupToMongodb");
                Intrinsics.checkNotNullExpressionValue(routePost412, "router.post(\"/reader3/backupToMongodb\")");
                yueduApi.coroutineHandler(routePost412, new AnonymousClass70(bookController2, null));
                Route routePost422 = router.post("/reader3/restoreFromMongodb");
                Intrinsics.checkNotNullExpressionValue(routePost422, "router.post(\"/reader3/restoreFromMongodb\")");
                yueduApi.coroutineHandler(routePost422, new AnonymousClass71(bookController2, null));
                Route routePost432 = router.post("/reader3/book/saveBookConfig");
                Intrinsics.checkNotNullExpressionValue(routePost432, "router.post(\"/reader3/book/saveBookConfig\")");
                yueduApi.coroutineHandler(routePost432, new AnonymousClass72(bookController2, null));
                Route route252 = router.get("/reader3/book/tts");
                Intrinsics.checkNotNullExpressionValue(route252, "router.get(\"/reader3/book/tts\")");
                yueduApi.coroutineHandlerWithoutRes(route252, new AnonymousClass73(bookController2, null));
                Route routePost442 = router.post("/reader3/book/tts");
                Intrinsics.checkNotNullExpressionValue(routePost442, "router.post(\"/reader3/book/tts\")");
                yueduApi.coroutineHandlerWithoutRes(routePost442, new AnonymousClass74(bookController2, null));
                Route routePost452 = router.post("/reader3/uploadFile");
                Intrinsics.checkNotNullExpressionValue(routePost452, "router.post(\"/reader3/uploadFile\")");
                yueduApi.coroutineHandler(routePost452, new AnonymousClass75(userController2, null));
                Route routePost462 = router.post("/reader3/deleteFile");
                Intrinsics.checkNotNullExpressionValue(routePost462, "router.post(\"/reader3/deleteFile\")");
                yueduApi.coroutineHandler(routePost462, new AnonymousClass76(userController2, null));
                Route routePost472 = router.post("/reader3/login");
                Intrinsics.checkNotNullExpressionValue(routePost472, "router.post(\"/reader3/login\")");
                yueduApi.coroutineHandler(routePost472, new AnonymousClass77(userController2, null));
                Route routePost482 = router.post("/reader3/logout");
                Intrinsics.checkNotNullExpressionValue(routePost482, "router.post(\"/reader3/logout\")");
                yueduApi.coroutineHandler(routePost482, new AnonymousClass78(userController2, null));
                Route route262 = router.get("/reader3/getUserInfo");
                Intrinsics.checkNotNullExpressionValue(route262, "router.get(\"/reader3/getUserInfo\")");
                yueduApi.coroutineHandler(route262, new AnonymousClass79(userController2, null));
                Route routePost492 = router.post("/reader3/saveUserConfig");
                Intrinsics.checkNotNullExpressionValue(routePost492, "router.post(\"/reader3/saveUserConfig\")");
                yueduApi.coroutineHandler(routePost492, new AnonymousClass80(userController2, null));
                Route route272 = router.get("/reader3/getUserConfig");
                Intrinsics.checkNotNullExpressionValue(route272, "router.get(\"/reader3/getUserConfig\")");
                yueduApi.coroutineHandler(route272, new AnonymousClass81(userController2, null));
                Route route282 = router.get("/reader3/getUserList");
                Intrinsics.checkNotNullExpressionValue(route282, "router.get(\"/reader3/getUserList\")");
                yueduApi.coroutineHandler(route282, new AnonymousClass82(userController2, null));
                Route routePost502 = router.post("/reader3/deleteUsers");
                Intrinsics.checkNotNullExpressionValue(routePost502, "router.post(\"/reader3/deleteUsers\")");
                yueduApi.coroutineHandler(routePost502, new AnonymousClass83(userController2, null));
                Route routePost512 = router.post("/reader3/clearInactiveUsers");
                Intrinsics.checkNotNullExpressionValue(routePost512, "router.post(\"/reader3/clearInactiveUsers\")");
                yueduApi.coroutineHandler(routePost512, new AnonymousClass84(userController2, null));
                Route routePost522 = router.post("/reader3/addUser");
                Intrinsics.checkNotNullExpressionValue(routePost522, "router.post(\"/reader3/addUser\")");
                yueduApi.coroutineHandler(routePost522, new AnonymousClass85(userController2, null));
                Route routePost532 = router.post("/reader3/resetPassword");
                Intrinsics.checkNotNullExpressionValue(routePost532, "router.post(\"/reader3/resetPassword\")");
                yueduApi.coroutineHandler(routePost532, new AnonymousClass86(userController2, null));
                Route routePost542 = router.post("/reader3/updateUser");
                Intrinsics.checkNotNullExpressionValue(routePost542, "router.post(\"/reader3/updateUser\")");
                yueduApi.coroutineHandler(routePost542, new AnonymousClass87(userController2, null));
                Route route292 = router.get("/reader3/user/downloadBackupFile");
                Intrinsics.checkNotNullExpressionValue(route292, "router.get(\"/reader3/user/downloadBackupFile\")");
                yueduApi.coroutineHandlerWithoutRes(route292, new AnonymousClass88(userController2, null));
                Route route302 = router.get("/reader3/getLicense");
                Intrinsics.checkNotNullExpressionValue(route302, "router.get(\"/reader3/getLicense\")");
                yueduApi.coroutineHandler(route302, new AnonymousClass89(licenseController2, null));
                Route routePost552 = router.post("/reader3/importLicense");
                Intrinsics.checkNotNullExpressionValue(routePost552, "router.post(\"/reader3/importLicense\")");
                yueduApi.coroutineHandlerWithoutRes(routePost552, new AnonymousClass90(licenseController2, null));
                Route route312 = router.get("/reader3/generateKeys");
                Intrinsics.checkNotNullExpressionValue(route312, "router.get(\"/reader3/generateKeys\")");
                yueduApi.coroutineHandler(route312, new AnonymousClass91(licenseController2, null));
                Route routePost562 = router.post("/reader3/generateKeys");
                Intrinsics.checkNotNullExpressionValue(routePost562, "router.post(\"/reader3/generateKeys\")");
                yueduApi.coroutineHandler(routePost562, new AnonymousClass92(licenseController2, null));
                Route route322 = router.get("/reader3/generateLicense");
                Intrinsics.checkNotNullExpressionValue(route322, "router.get(\"/reader3/generateLicense\")");
                yueduApi.coroutineHandler(route322, new AnonymousClass93(licenseController2, null));
                Route routePost572 = router.post("/reader3/generateLicense");
                Intrinsics.checkNotNullExpressionValue(routePost572, "router.post(\"/reader3/generateLicense\")");
                yueduApi.coroutineHandler(routePost572, new AnonymousClass94(licenseController2, null));
                Route route332 = router.get("/reader3/isHostValid");
                Intrinsics.checkNotNullExpressionValue(route332, "router.get(\"/reader3/isHostValid\")");
                yueduApi.coroutineHandler(route332, new AnonymousClass95(licenseController2, null));
                Route routePost582 = router.post("/reader3/isHostValid");
                Intrinsics.checkNotNullExpressionValue(routePost582, "router.post(\"/reader3/isHostValid\")");
                yueduApi.coroutineHandler(routePost582, new AnonymousClass96(licenseController2, null));
                Route routePost592 = router.post("/reader3/activateLicense");
                Intrinsics.checkNotNullExpressionValue(routePost592, "router.post(\"/reader3/activateLicense\")");
                yueduApi.coroutineHandler(routePost592, new AnonymousClass97(licenseController2, null));
                Route route342 = router.get("/reader3/isLicenseValid");
                Intrinsics.checkNotNullExpressionValue(route342, "router.get(\"/reader3/isLicenseValid\")");
                yueduApi.coroutineHandler(route342, new AnonymousClass98(licenseController2, null));
                Route routePost602 = router.post("/reader3/isLicenseValid");
                Intrinsics.checkNotNullExpressionValue(routePost602, "router.post(\"/reader3/isLicenseValid\")");
                yueduApi.coroutineHandler(routePost602, new AnonymousClass99(licenseController2, null));
                Route routePost612 = router.post("/reader3/decryptLicense");
                Intrinsics.checkNotNullExpressionValue(routePost612, "router.post(\"/reader3/decryptLicense\")");
                yueduApi.coroutineHandler(routePost612, new AnonymousClass100(licenseController2, null));
                Route routePost622 = router.post("/reader3/sendCodeToEmail");
                Intrinsics.checkNotNullExpressionValue(routePost622, "router.post(\"/reader3/sendCodeToEmail\")");
                yueduApi.coroutineHandler(routePost622, new AnonymousClass101(licenseController2, null));
                Route routePost632 = router.post("/reader3/supplyLicense");
                Intrinsics.checkNotNullExpressionValue(routePost632, "router.post(\"/reader3/supplyLicense\")");
                yueduApi.coroutineHandler(routePost632, new AnonymousClass102(licenseController2, null));
                Route routePost642 = router.post("/reader3/backupToWebdav");
                Intrinsics.checkNotNullExpressionValue(routePost642, "router.post(\"/reader3/backupToWebdav\")");
                yueduApi.coroutineHandler(routePost642, new AnonymousClass103(webdavController2, null));
                Route route352 = router.get("/reader3/getRssSources");
                Intrinsics.checkNotNullExpressionValue(route352, "router.get(\"/reader3/getRssSources\")");
                yueduApi.coroutineHandler(route352, new AnonymousClass104(rssSourceController2, null));
                Route routePost652 = router.post("/reader3/saveRssSource");
                Intrinsics.checkNotNullExpressionValue(routePost652, "router.post(\"/reader3/saveRssSource\")");
                yueduApi.coroutineHandler(routePost652, new AnonymousClass105(rssSourceController2, null));
                Route routePost662 = router.post("/reader3/saveRssSources");
                Intrinsics.checkNotNullExpressionValue(routePost662, "router.post(\"/reader3/saveRssSources\")");
                yueduApi.coroutineHandler(routePost662, new AnonymousClass106(rssSourceController2, null));
                Route routePost672 = router.post("/reader3/deleteRssSource");
                Intrinsics.checkNotNullExpressionValue(routePost672, "router.post(\"/reader3/deleteRssSource\")");
                yueduApi.coroutineHandler(routePost672, new AnonymousClass107(rssSourceController2, null));
                Route route362 = router.get("/reader3/getRssArticles");
                Intrinsics.checkNotNullExpressionValue(route362, "router.get(\"/reader3/getRssArticles\")");
                yueduApi.coroutineHandler(route362, new AnonymousClass108(rssSourceController2, null));
                Route routePost682 = router.post("/reader3/getRssArticles");
                Intrinsics.checkNotNullExpressionValue(routePost682, "router.post(\"/reader3/getRssArticles\")");
                yueduApi.coroutineHandler(routePost682, new AnonymousClass109(rssSourceController2, null));
                Route route372 = router.get("/reader3/getRssContent");
                Intrinsics.checkNotNullExpressionValue(route372, "router.get(\"/reader3/getRssContent\")");
                yueduApi.coroutineHandler(route372, new AnonymousClass110(rssSourceController2, null));
                Route routePost692 = router.post("/reader3/getRssContent");
                Intrinsics.checkNotNullExpressionValue(routePost692, "router.post(\"/reader3/getRssContent\")");
                yueduApi.coroutineHandler(routePost692, new AnonymousClass111(rssSourceController2, null));
                Route route382 = router.get("/reader3/getReplaceRules");
                Intrinsics.checkNotNullExpressionValue(route382, "router.get(\"/reader3/getReplaceRules\")");
                yueduApi.coroutineHandler(route382, new AnonymousClass112(replaceRuleController2, null));
                Route routePost702 = router.post("/reader3/saveReplaceRule");
                Intrinsics.checkNotNullExpressionValue(routePost702, "router.post(\"/reader3/saveReplaceRule\")");
                yueduApi.coroutineHandler(routePost702, new AnonymousClass113(replaceRuleController2, null));
                Route routePost712 = router.post("/reader3/saveReplaceRules");
                Intrinsics.checkNotNullExpressionValue(routePost712, "router.post(\"/reader3/saveReplaceRules\")");
                yueduApi.coroutineHandler(routePost712, new AnonymousClass114(replaceRuleController2, null));
                Route routePost722 = router.post("/reader3/deleteReplaceRule");
                Intrinsics.checkNotNullExpressionValue(routePost722, "router.post(\"/reader3/deleteReplaceRule\")");
                yueduApi.coroutineHandler(routePost722, new AnonymousClass115(replaceRuleController2, null));
                Route routePost732 = router.post("/reader3/deleteReplaceRules");
                Intrinsics.checkNotNullExpressionValue(routePost732, "router.post(\"/reader3/deleteReplaceRules\")");
                yueduApi.coroutineHandler(routePost732, new AnonymousClass116(replaceRuleController2, null));
                Route route392 = router.get("/reader3/getBookmarks");
                Intrinsics.checkNotNullExpressionValue(route392, "router.get(\"/reader3/getBookmarks\")");
                yueduApi.coroutineHandler(route392, new AnonymousClass117(bookmarkController2, null));
                Route routePost742 = router.post("/reader3/saveBookmark");
                Intrinsics.checkNotNullExpressionValue(routePost742, "router.post(\"/reader3/saveBookmark\")");
                yueduApi.coroutineHandler(routePost742, new AnonymousClass118(bookmarkController2, null));
                Route routePost752 = router.post("/reader3/saveBookmarks");
                Intrinsics.checkNotNullExpressionValue(routePost752, "router.post(\"/reader3/saveBookmarks\")");
                yueduApi.coroutineHandler(routePost752, new AnonymousClass119(bookmarkController2, null));
                Route routePost762 = router.post("/reader3/deleteBookmark");
                Intrinsics.checkNotNullExpressionValue(routePost762, "router.post(\"/reader3/deleteBookmark\")");
                yueduApi.coroutineHandler(routePost762, new AnonymousClass120(bookmarkController2, null));
                Route routePost772 = router.post("/reader3/deleteBookmarks");
                Intrinsics.checkNotNullExpressionValue(routePost772, "router.post(\"/reader3/deleteBookmarks\")");
                yueduApi.coroutineHandler(routePost772, new AnonymousClass121(bookmarkController2, null));
                Route route402 = router.get("/reader3/file/list");
                Intrinsics.checkNotNullExpressionValue(route402, "router.get(\"/reader3/file/list\")");
                yueduApi.coroutineHandler(route402, new AnonymousClass122(fileController2, null));
                Route route412 = router.get("/reader3/file/get");
                Intrinsics.checkNotNullExpressionValue(route412, "router.get(\"/reader3/file/get\")");
                yueduApi.coroutineHandler(route412, new AnonymousClass123(fileController2, null));
                Route routePost782 = router.post("/reader3/file/save");
                Intrinsics.checkNotNullExpressionValue(routePost782, "router.post(\"/reader3/file/save\")");
                yueduApi.coroutineHandler(routePost782, new AnonymousClass124(fileController2, null));
                Route routePost792 = router.post("/reader3/file/mkdir");
                Intrinsics.checkNotNullExpressionValue(routePost792, "router.post(\"/reader3/file/mkdir\")");
                yueduApi.coroutineHandler(routePost792, new AnonymousClass125(fileController2, null));
                Route route422 = router.get("/reader3/file/download");
                Intrinsics.checkNotNullExpressionValue(route422, "router.get(\"/reader3/file/download\")");
                yueduApi.coroutineHandlerWithoutRes(route422, new AnonymousClass126(fileController2, null));
                Route routePost802 = router.post("/reader3/file/upload");
                Intrinsics.checkNotNullExpressionValue(routePost802, "router.post(\"/reader3/file/upload\")");
                yueduApi.coroutineHandler(routePost802, new AnonymousClass127(fileController2, null));
                Route routePost812 = router.post("/reader3/file/delete");
                Intrinsics.checkNotNullExpressionValue(routePost812, "router.post(\"/reader3/file/delete\")");
                yueduApi.coroutineHandler(routePost812, new AnonymousClass128(fileController2, null));
                Route routePost822 = router.post("/reader3/file/deleteMulti");
                Intrinsics.checkNotNullExpressionValue(routePost822, "router.post(\"/reader3/file/deleteMulti\")");
                yueduApi.coroutineHandler(routePost822, new AnonymousClass129(fileController2, null));
                Route routePost832 = router.post("/reader3/file/importPreview");
                Intrinsics.checkNotNullExpressionValue(routePost832, "router.post(\"/reader3/file/importPreview\")");
                yueduApi.coroutineHandler(routePost832, new AnonymousClass130(fileController2, null));
                Route routePost842 = router.post("/reader3/file/restore");
                Intrinsics.checkNotNullExpressionValue(routePost842, "router.post(\"/reader3/file/restore\")");
                yueduApi.coroutineHandler(routePost842, new AnonymousClass131(fileController2, null));
                Route route432 = router.get("/reader3/file/parse");
                Intrinsics.checkNotNullExpressionValue(route432, "router.get(\"/reader3/file/parse\")");
                yueduApi.coroutineHandler(route432, new AnonymousClass132(fileController2, null));
                Route routePost852 = router.post("/reader3/file/parse");
                Intrinsics.checkNotNullExpressionValue(routePost852, "router.post(\"/reader3/file/parse\")");
                yueduApi.coroutineHandler(routePost852, new AnonymousClass133(fileController2, null));
                Route route442 = router.get("/reader3/httpTTS/list");
                Intrinsics.checkNotNullExpressionValue(route442, "router.get(\"/reader3/httpTTS/list\")");
                yueduApi.coroutineHandler(route442, new AnonymousClass134(httpTTSController2, null));
                Route routePost862 = router.post("/reader3/httpTTS/save");
                Intrinsics.checkNotNullExpressionValue(routePost862, "router.post(\"/reader3/httpTTS/save\")");
                yueduApi.coroutineHandler(routePost862, new AnonymousClass135(httpTTSController2, null));
                Route routePost872 = router.post("/reader3/httpTTS/saveMulti");
                Intrinsics.checkNotNullExpressionValue(routePost872, "router.post(\"/reader3/httpTTS/saveMulti\")");
                yueduApi.coroutineHandler(routePost872, new AnonymousClass136(httpTTSController2, null));
                Route routePost882 = router.post("/reader3/httpTTS/delete");
                Intrinsics.checkNotNullExpressionValue(routePost882, "router.post(\"/reader3/httpTTS/delete\")");
                yueduApi.coroutineHandler(routePost882, new AnonymousClass137(httpTTSController2, null));
                Route routePost892 = router.post("/reader3/httpTTS/deleteMulti");
                Intrinsics.checkNotNullExpressionValue(routePost892, "router.post(\"/reader3/httpTTS/deleteMulti\")");
                yueduApi.coroutineHandler(routePost892, new AnonymousClass138(httpTTSController2, null));
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: initRouter$lambda-0, reason: not valid java name */
    private static final void m7initRouter$lambda0(Ref.ObjectRef $dataDir, RoutingContext it) {
        Intrinsics.checkNotNullParameter($dataDir, "$dataDir");
        String strPath = it.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "it.request().path()");
        String path = URIDecoder.decodeURIComponent(StringsKt.replace(strPath, "/book-assets/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), false);
        Intrinsics.checkNotNullExpressionValue(path, "decodeURIComponent(path, false)");
        if (StringsKt.endsWith(path, "html", true) || StringsKt.endsWith(path, "htm", true)) {
            File filePath = new File(Intrinsics.stringPlus((String) $dataDir.element, path));
            if (filePath.exists()) {
                BookConfig bookConfig = BookConfig.INSTANCE;
                String string = filePath.toString();
                Intrinsics.checkNotNullExpressionValue(string, "filePath.toString()");
                bookConfig.injectJavascriptToEpubChapter(string);
            }
        }
        it.next();
    }

    /* JADX INFO: renamed from: initRouter$lambda-1, reason: not valid java name */
    private static final void m8initRouter$lambda1(Ref.ObjectRef $dataDir, RoutingContext it) throws UnsupportedEncodingException {
        Intrinsics.checkNotNullParameter($dataDir, "$dataDir");
        String strPath = it.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "it.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/epub/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        if (StringsKt.endsWith(path, "html", true)) {
            File filePath = new File(Intrinsics.stringPlus((String) $dataDir.element, path));
            if (filePath.exists()) {
                BookConfig bookConfig = BookConfig.INSTANCE;
                String string = filePath.toString();
                Intrinsics.checkNotNullExpressionValue(string, "filePath.toString()");
                bookConfig.injectJavascriptToEpubChapter(string);
            }
        }
        it.next();
    }

    /* JADX INFO: renamed from: initRouter$lambda-2, reason: not valid java name */
    private static final void m9initRouter$lambda2(RoutingContext it) throws UnsupportedEncodingException {
        String strPath = it.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "it.request().path()");
        if (StringsKt.endsWith$default(strPath, "/simple-web", false, 2, (Object) null)) {
            HttpServerResponse httpServerResponseResponse = it.response();
            String strDecode = URLDecoder.decode(it.request().absoluteURI(), Constants.CHARACTER_ENCODING);
            Intrinsics.checkNotNullExpressionValue(strDecode, "decode(it.request().absoluteURI(), \"UTF-8\")");
            httpServerResponseResponse.putHeader("Location", StringsKt.replace$default(strDecode, "/simple-web", "/simple-web/", false, 4, (Object) null)).setStatusCode(302).end();
            return;
        }
        it.next();
    }

    /* JADX INFO: renamed from: initRouter$lambda-3, reason: not valid java name */
    private static final void m10initRouter$lambda3(RoutingContext it) {
        License license = ExtKt.getInstalledLicense$default(false, 1, null);
        long simpleWebExpiredAt = 0;
        String strHost = it.request().host();
        Intrinsics.checkNotNullExpressionValue(strHost, "it.request().host()");
        if (license.validHost(strHost)) {
            simpleWebExpiredAt = license.getSimpleWebExpiredAt();
        }
        if (simpleWebExpiredAt != 0 && simpleWebExpiredAt < System.currentTimeMillis()) {
            it.response().putHeader("content-type", "text/html; charset=UTF-8").setStatusCode(403).end("<html><head><title>未激活该功能</title></head><body><div style='text-align: center;padding: 30px 0;'>未激活该功能，请加<a href='https://t.me/+pQ8HDlANPZ84ZWNl'>TG群</a>激活</div></body></html>");
        } else {
            it.next();
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$6, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$6.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {143}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$6")
    static final class AnonymousClass6 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;

        AnonymousClass6(Continuation<? super AnonymousClass6> $completion) {
            super(2, $completion);
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass6 = YueduApi.this.new AnonymousClass6($completion);
            anonymousClass6.L$0 = value;
            return anonymousClass6;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object systemInfo = YueduApi.this.getSystemInfo(it, (Continuation) this);
                    return systemInfo == coroutine_suspended ? coroutine_suspended : systemInfo;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$7, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$7.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {163}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$7")
    static final class AnonymousClass7 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(BookSourceController $bookSourceController, Continuation<? super AnonymousClass7> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass7 = new AnonymousClass7(this.$bookSourceController, $completion);
            anonymousClass7.L$0 = value;
            return anonymousClass7;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookSource = this.$bookSourceController.saveBookSource(it, (Continuation) this);
                    return objSaveBookSource == coroutine_suspended ? coroutine_suspended : objSaveBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$8, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$8.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {164}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$8")
    static final class AnonymousClass8 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(BookSourceController $bookSourceController, Continuation<? super AnonymousClass8> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass8 = new AnonymousClass8(this.$bookSourceController, $completion);
            anonymousClass8.L$0 = value;
            return anonymousClass8;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookSources = this.$bookSourceController.saveBookSources(it, (Continuation<? super ReturnData>) this);
                    return objSaveBookSources == coroutine_suspended ? coroutine_suspended : objSaveBookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$9, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$9.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {166}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$9")
    static final class AnonymousClass9 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(BookSourceController $bookSourceController, Continuation<? super AnonymousClass9> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass9 = new AnonymousClass9(this.$bookSourceController, $completion);
            anonymousClass9.L$0 = value;
            return anonymousClass9;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookSource = this.$bookSourceController.getBookSource(it, (Continuation) this);
                    return bookSource == coroutine_suspended ? coroutine_suspended : bookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$10, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$10.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {167}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$10")
    static final class AnonymousClass10 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(BookSourceController $bookSourceController, Continuation<? super AnonymousClass10> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass10 = new AnonymousClass10(this.$bookSourceController, $completion);
            anonymousClass10.L$0 = value;
            return anonymousClass10;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookSource = this.$bookSourceController.getBookSource(it, (Continuation) this);
                    return bookSource == coroutine_suspended ? coroutine_suspended : bookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$11, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$11.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {168}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$11")
    static final class AnonymousClass11 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass11(BookSourceController $bookSourceController, Continuation<? super AnonymousClass11> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass11 = new AnonymousClass11(this.$bookSourceController, $completion);
            anonymousClass11.L$0 = value;
            return anonymousClass11;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookSources = this.$bookSourceController.getBookSources(it, (Continuation) this);
                    return bookSources == coroutine_suspended ? coroutine_suspended : bookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$12, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$12.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {169}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$12")
    static final class AnonymousClass12 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(BookSourceController $bookSourceController, Continuation<? super AnonymousClass12> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass12 = new AnonymousClass12(this.$bookSourceController, $completion);
            anonymousClass12.L$0 = value;
            return anonymousClass12;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookSources = this.$bookSourceController.getBookSources(it, (Continuation) this);
                    return bookSources == coroutine_suspended ? coroutine_suspended : bookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$13, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$13.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {171}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$13")
    static final class AnonymousClass13 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass13(BookSourceController $bookSourceController, Continuation<? super AnonymousClass13> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass13 = new AnonymousClass13(this.$bookSourceController, $completion);
            anonymousClass13.L$0 = value;
            return anonymousClass13;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteAllBookSources = this.$bookSourceController.deleteAllBookSources(it, (Continuation) this);
                    return objDeleteAllBookSources == coroutine_suspended ? coroutine_suspended : objDeleteAllBookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$14, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$14.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {172}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$14")
    static final class AnonymousClass14 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass14(BookSourceController $bookSourceController, Continuation<? super AnonymousClass14> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass14 = new AnonymousClass14(this.$bookSourceController, $completion);
            anonymousClass14.L$0 = value;
            return anonymousClass14;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteBookSource = this.$bookSourceController.deleteBookSource(it, (Continuation) this);
                    return objDeleteBookSource == coroutine_suspended ? coroutine_suspended : objDeleteBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$15, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$15.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {173}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$15")
    static final class AnonymousClass15 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(BookSourceController $bookSourceController, Continuation<? super AnonymousClass15> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass15 = new AnonymousClass15(this.$bookSourceController, $completion);
            anonymousClass15.L$0 = value;
            return anonymousClass15;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteBookSources = this.$bookSourceController.deleteBookSources(it, (Continuation) this);
                    return objDeleteBookSources == coroutine_suspended ? coroutine_suspended : objDeleteBookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$16, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$16.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {176}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$16")
    static final class AnonymousClass16 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass16(BookSourceController $bookSourceController, Continuation<? super AnonymousClass16> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass16 = new AnonymousClass16(this.$bookSourceController, $completion);
            anonymousClass16.L$0 = value;
            return anonymousClass16;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object sourceFile = this.$bookSourceController.readSourceFile(it, (Continuation) this);
                    return sourceFile == coroutine_suspended ? coroutine_suspended : sourceFile;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$17, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$17.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {179}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$17")
    static final class AnonymousClass17 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass17(BookSourceController $bookSourceController, Continuation<? super AnonymousClass17> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass17 = new AnonymousClass17(this.$bookSourceController, $completion);
            anonymousClass17.L$0 = value;
            return anonymousClass17;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookSourceController.saveFromRemoteSource(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$18, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$18.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {182}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$18")
    static final class AnonymousClass18 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass18(BookSourceController $bookSourceController, Continuation<? super AnonymousClass18> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass18 = new AnonymousClass18(this.$bookSourceController, $completion);
            anonymousClass18.L$0 = value;
            return anonymousClass18;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object asDefaultBookSources = this.$bookSourceController.setAsDefaultBookSources(it, (Continuation) this);
                    return asDefaultBookSources == coroutine_suspended ? coroutine_suspended : asDefaultBookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$19, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$19.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {183}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$19")
    static final class AnonymousClass19 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass19(BookSourceController $bookSourceController, Continuation<? super AnonymousClass19> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass19 = new AnonymousClass19(this.$bookSourceController, $completion);
            anonymousClass19.L$0 = value;
            return anonymousClass19;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteUserBookSource = this.$bookSourceController.deleteUserBookSource(it, (Continuation) this);
                    return objDeleteUserBookSource == coroutine_suspended ? coroutine_suspended : objDeleteUserBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$20, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$20.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {184}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$20")
    static final class AnonymousClass20 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookSourceController $bookSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass20(BookSourceController $bookSourceController, Continuation<? super AnonymousClass20> $completion) {
            super(2, $completion);
            this.$bookSourceController = $bookSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass20 = new AnonymousClass20(this.$bookSourceController, $completion);
            anonymousClass20.L$0 = value;
            return anonymousClass20;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteBookSourcesFile = this.$bookSourceController.deleteBookSourcesFile(it, (Continuation) this);
                    return objDeleteBookSourcesFile == coroutine_suspended ? coroutine_suspended : objDeleteBookSourcesFile;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$21, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$21.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {188}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$21")
    static final class AnonymousClass21 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass21(BookController $bookController, Continuation<? super AnonymousClass21> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass21 = new AnonymousClass21(this.$bookController, $completion);
            anonymousClass21.L$0 = value;
            return anonymousClass21;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookshelf = this.$bookController.getBookshelf(it, (Continuation) this);
                    return bookshelf == coroutine_suspended ? coroutine_suspended : bookshelf;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$22, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$22.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {189}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$22")
    static final class AnonymousClass22 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass22(BookController $bookController, Continuation<? super AnonymousClass22> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass22 = new AnonymousClass22(this.$bookController, $completion);
            anonymousClass22.L$0 = value;
            return anonymousClass22;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object shelfBook = this.$bookController.getShelfBook(it, (Continuation) this);
                    return shelfBook == coroutine_suspended ? coroutine_suspended : shelfBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$23, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$23.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {190}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$23")
    static final class AnonymousClass23 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass23(BookController $bookController, Continuation<? super AnonymousClass23> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass23 = new AnonymousClass23(this.$bookController, $completion);
            anonymousClass23.L$0 = value;
            return anonymousClass23;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBook = this.$bookController.saveBook(it, (Continuation) this);
                    return objSaveBook == coroutine_suspended ? coroutine_suspended : objSaveBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$24, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$24.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {191}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$24")
    static final class AnonymousClass24 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass24(BookController $bookController, Continuation<? super AnonymousClass24> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass24 = new AnonymousClass24(this.$bookController, $completion);
            anonymousClass24.L$0 = value;
            return anonymousClass24;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteBook = this.$bookController.deleteBook(it, (Continuation) this);
                    return objDeleteBook == coroutine_suspended ? coroutine_suspended : objDeleteBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$25, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$25.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {Wbxml.EXT_0}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$25")
    static final class AnonymousClass25 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass25(BookController $bookController, Continuation<? super AnonymousClass25> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass25 = new AnonymousClass25(this.$bookController, $completion);
            anonymousClass25.L$0 = value;
            return anonymousClass25;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteBooks = this.$bookController.deleteBooks(it, (Continuation) this);
                    return objDeleteBooks == coroutine_suspended ? coroutine_suspended : objDeleteBooks;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$26, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$26.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {Wbxml.OPAQUE}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$26")
    static final class AnonymousClass26 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass26(BookController $bookController, Continuation<? super AnonymousClass26> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass26 = new AnonymousClass26(this.$bookController, $completion);
            anonymousClass26.L$0 = value;
            return anonymousClass26;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object invalidBookSources = this.$bookController.getInvalidBookSources(it, (Continuation) this);
                    return invalidBookSources == coroutine_suspended ? coroutine_suspended : invalidBookSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$27, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$27.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {198}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$27")
    static final class AnonymousClass27 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass27(BookController $bookController, Continuation<? super AnonymousClass27> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass27 = new AnonymousClass27(this.$bookController, $completion);
            anonymousClass27.L$0 = value;
            return anonymousClass27;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objExploreBook = this.$bookController.exploreBook(it, (Continuation) this);
                    return objExploreBook == coroutine_suspended ? coroutine_suspended : objExploreBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$28, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$28.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {199}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$28")
    static final class AnonymousClass28 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass28(BookController $bookController, Continuation<? super AnonymousClass28> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass28 = new AnonymousClass28(this.$bookController, $completion);
            anonymousClass28.L$0 = value;
            return anonymousClass28;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objExploreBook = this.$bookController.exploreBook(it, (Continuation) this);
                    return objExploreBook == coroutine_suspended ? coroutine_suspended : objExploreBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$29, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$29.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {202}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$29")
    static final class AnonymousClass29 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass29(BookController $bookController, Continuation<? super AnonymousClass29> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass29 = new AnonymousClass29(this.$bookController, $completion);
            anonymousClass29.L$0 = value;
            return anonymousClass29;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBook = this.$bookController.searchBook(it, (Continuation) this);
                    return objSearchBook == coroutine_suspended ? coroutine_suspended : objSearchBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$30, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$30.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {203}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$30")
    static final class AnonymousClass30 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass30(BookController $bookController, Continuation<? super AnonymousClass30> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass30 = new AnonymousClass30(this.$bookController, $completion);
            anonymousClass30.L$0 = value;
            return anonymousClass30;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBook = this.$bookController.searchBook(it, (Continuation) this);
                    return objSearchBook == coroutine_suspended ? coroutine_suspended : objSearchBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$31, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$31.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {204}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$31")
    static final class AnonymousClass31 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass31(BookController $bookController, Continuation<? super AnonymousClass31> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass31 = new AnonymousClass31(this.$bookController, $completion);
            anonymousClass31.L$0 = value;
            return anonymousClass31;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBookMulti = this.$bookController.searchBookMulti(it, (Continuation) this);
                    return objSearchBookMulti == coroutine_suspended ? coroutine_suspended : objSearchBookMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$32, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$32.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {205}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$32")
    static final class AnonymousClass32 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass32(BookController $bookController, Continuation<? super AnonymousClass32> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass32 = new AnonymousClass32(this.$bookController, $completion);
            anonymousClass32.L$0 = value;
            return anonymousClass32;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBookMulti = this.$bookController.searchBookMulti(it, (Continuation) this);
                    return objSearchBookMulti == coroutine_suspended ? coroutine_suspended : objSearchBookMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$33, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$33.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {206}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$33")
    static final class AnonymousClass33 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass33(BookController $bookController, Continuation<? super AnonymousClass33> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass33 = new AnonymousClass33(this.$bookController, $completion);
            anonymousClass33.L$0 = value;
            return anonymousClass33;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.searchBookMultiSSE(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$34, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$34.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {209}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$34")
    static final class AnonymousClass34 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass34(BookController $bookController, Continuation<? super AnonymousClass34> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass34 = new AnonymousClass34(this.$bookController, $completion);
            anonymousClass34.L$0 = value;
            return anonymousClass34;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookInfo = this.$bookController.getBookInfo(it, (Continuation) this);
                    return bookInfo == coroutine_suspended ? coroutine_suspended : bookInfo;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$35, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$35.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {210}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$35")
    static final class AnonymousClass35 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass35(BookController $bookController, Continuation<? super AnonymousClass35> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass35 = new AnonymousClass35(this.$bookController, $completion);
            anonymousClass35.L$0 = value;
            return anonymousClass35;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookInfo = this.$bookController.getBookInfo(it, (Continuation) this);
                    return bookInfo == coroutine_suspended ? coroutine_suspended : bookInfo;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$36, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$36.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {213}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$36")
    static final class AnonymousClass36 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass36(BookController $bookController, Continuation<? super AnonymousClass36> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass36 = new AnonymousClass36(this.$bookController, $completion);
            anonymousClass36.L$0 = value;
            return anonymousClass36;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object chapterList = this.$bookController.getChapterList(it, (Continuation) this);
                    return chapterList == coroutine_suspended ? coroutine_suspended : chapterList;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$37, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$37.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {214}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$37")
    static final class AnonymousClass37 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass37(BookController $bookController, Continuation<? super AnonymousClass37> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass37 = new AnonymousClass37(this.$bookController, $completion);
            anonymousClass37.L$0 = value;
            return anonymousClass37;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object chapterList = this.$bookController.getChapterList(it, (Continuation) this);
                    return chapterList == coroutine_suspended ? coroutine_suspended : chapterList;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$38, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$38.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {217}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$38")
    static final class AnonymousClass38 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass38(BookController $bookController, Continuation<? super AnonymousClass38> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass38 = new AnonymousClass38(this.$bookController, $completion);
            anonymousClass38.L$0 = value;
            return anonymousClass38;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookContent = this.$bookController.getBookContent(it, (Continuation) this);
                    return bookContent == coroutine_suspended ? coroutine_suspended : bookContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$39, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$39.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {218}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$39")
    static final class AnonymousClass39 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass39(BookController $bookController, Continuation<? super AnonymousClass39> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass39 = new AnonymousClass39(this.$bookController, $completion);
            anonymousClass39.L$0 = value;
            return anonymousClass39;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookContent = this.$bookController.getBookContent(it, (Continuation) this);
                    return bookContent == coroutine_suspended ? coroutine_suspended : bookContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$40, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$40.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {219}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$40")
    static final class AnonymousClass40 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass40(BookController $bookController, Continuation<? super AnonymousClass40> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass40 = new AnonymousClass40(this.$bookController, $completion);
            anonymousClass40.L$0 = value;
            return anonymousClass40;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookContent = this.$bookController.saveBookContent(it, (Continuation) this);
                    return objSaveBookContent == coroutine_suspended ? coroutine_suspended : objSaveBookContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$41, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$41.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {222}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$41")
    static final class AnonymousClass41 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass41(BookController $bookController, Continuation<? super AnonymousClass41> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass41 = new AnonymousClass41(this.$bookController, $completion);
            anonymousClass41.L$0 = value;
            return anonymousClass41;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookProgress = this.$bookController.saveBookProgress(it, (Continuation) this);
                    return objSaveBookProgress == coroutine_suspended ? coroutine_suspended : objSaveBookProgress;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$42, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$42.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {225}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$42")
    static final class AnonymousClass42 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass42(BookController $bookController, Continuation<? super AnonymousClass42> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass42 = new AnonymousClass42(this.$bookController, $completion);
            anonymousClass42.L$0 = value;
            return anonymousClass42;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.getBookCover(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$43, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$43.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {228}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$43")
    static final class AnonymousClass43 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass43(BookController $bookController, Continuation<? super AnonymousClass43> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass43 = new AnonymousClass43(this.$bookController, $completion);
            anonymousClass43.L$0 = value;
            return anonymousClass43;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBookSource = this.$bookController.searchBookSource(it, (Continuation) this);
                    return objSearchBookSource == coroutine_suspended ? coroutine_suspended : objSearchBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$44, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$44.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {229}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$44")
    static final class AnonymousClass44 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass44(BookController $bookController, Continuation<? super AnonymousClass44> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass44 = new AnonymousClass44(this.$bookController, $completion);
            anonymousClass44.L$0 = value;
            return anonymousClass44;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBookSource = this.$bookController.searchBookSource(it, (Continuation) this);
                    return objSearchBookSource == coroutine_suspended ? coroutine_suspended : objSearchBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$45, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$45.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {230}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$45")
    static final class AnonymousClass45 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass45(BookController $bookController, Continuation<? super AnonymousClass45> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass45 = new AnonymousClass45(this.$bookController, $completion);
            anonymousClass45.L$0 = value;
            return anonymousClass45;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object availableBookSource = this.$bookController.getAvailableBookSource(it, (Continuation) this);
                    return availableBookSource == coroutine_suspended ? coroutine_suspended : availableBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$46, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$46.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {231}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$46")
    static final class AnonymousClass46 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass46(BookController $bookController, Continuation<? super AnonymousClass46> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass46 = new AnonymousClass46(this.$bookController, $completion);
            anonymousClass46.L$0 = value;
            return anonymousClass46;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object availableBookSource = this.$bookController.getAvailableBookSource(it, (Continuation) this);
                    return availableBookSource == coroutine_suspended ? coroutine_suspended : availableBookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$47, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$47.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {232}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$47")
    static final class AnonymousClass47 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass47(BookController $bookController, Continuation<? super AnonymousClass47> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass47 = new AnonymousClass47(this.$bookController, $completion);
            anonymousClass47.L$0 = value;
            return anonymousClass47;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.searchBookSourceSSE(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$48, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$48.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {235}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$48")
    static final class AnonymousClass48 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass48(BookController $bookController, Continuation<? super AnonymousClass48> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass48 = new AnonymousClass48(this.$bookController, $completion);
            anonymousClass48.L$0 = value;
            return anonymousClass48;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookSource = this.$bookController.setBookSource(it, (Continuation) this);
                    return bookSource == coroutine_suspended ? coroutine_suspended : bookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$49, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$49.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {236}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$49")
    static final class AnonymousClass49 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass49(BookController $bookController, Continuation<? super AnonymousClass49> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass49 = new AnonymousClass49(this.$bookController, $completion);
            anonymousClass49.L$0 = value;
            return anonymousClass49;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object bookSource = this.$bookController.setBookSource(it, (Continuation) this);
                    return bookSource == coroutine_suspended ? coroutine_suspended : bookSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$50, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$50.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {239}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$50")
    static final class AnonymousClass50 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass50(BookController $bookController, Continuation<? super AnonymousClass50> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass50 = new AnonymousClass50(this.$bookController, $completion);
            anonymousClass50.L$0 = value;
            return anonymousClass50;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookGroupId = this.$bookController.saveBookGroupId(it, (Continuation) this);
                    return objSaveBookGroupId == coroutine_suspended ? coroutine_suspended : objSaveBookGroupId;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$51, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$51.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {240}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$51")
    static final class AnonymousClass51 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass51(BookController $bookController, Continuation<? super AnonymousClass51> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass51 = new AnonymousClass51(this.$bookController, $completion);
            anonymousClass51.L$0 = value;
            return anonymousClass51;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objAddBookGroupMulti = this.$bookController.addBookGroupMulti(it, (Continuation) this);
                    return objAddBookGroupMulti == coroutine_suspended ? coroutine_suspended : objAddBookGroupMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$52, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$52.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {241}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$52")
    static final class AnonymousClass52 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass52(BookController $bookController, Continuation<? super AnonymousClass52> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass52 = new AnonymousClass52(this.$bookController, $completion);
            anonymousClass52.L$0 = value;
            return anonymousClass52;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objRemoveBookGroupMulti = this.$bookController.removeBookGroupMulti(it, (Continuation) this);
                    return objRemoveBookGroupMulti == coroutine_suspended ? coroutine_suspended : objRemoveBookGroupMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$53, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$53.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {244}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$53")
    static final class AnonymousClass53 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass53(BookController $bookController, Continuation<? super AnonymousClass53> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass53 = new AnonymousClass53(this.$bookController, $completion);
            anonymousClass53.L$0 = value;
            return anonymousClass53;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objImportBookPreview = this.$bookController.importBookPreview(it, (Continuation) this);
                    return objImportBookPreview == coroutine_suspended ? coroutine_suspended : objImportBookPreview;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$54, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$54.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {245}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$54")
    static final class AnonymousClass54 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass54(BookController $bookController, Continuation<? super AnonymousClass54> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass54 = new AnonymousClass54(this.$bookController, $completion);
            anonymousClass54.L$0 = value;
            return anonymousClass54;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objRefreshLocalBook = this.$bookController.refreshLocalBook(it, (Continuation) this);
                    return objRefreshLocalBook == coroutine_suspended ? coroutine_suspended : objRefreshLocalBook;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$55, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$55.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {248}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$55")
    static final class AnonymousClass55 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass55(BookController $bookController, Continuation<? super AnonymousClass55> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass55 = new AnonymousClass55(this.$bookController, $completion);
            anonymousClass55.L$0 = value;
            return anonymousClass55;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object txtTocRules = this.$bookController.getTxtTocRules(it, (Continuation) this);
                    return txtTocRules == coroutine_suspended ? coroutine_suspended : txtTocRules;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$56, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$56.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {249}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$56")
    static final class AnonymousClass56 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass56(BookController $bookController, Continuation<? super AnonymousClass56> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass56 = new AnonymousClass56(this.$bookController, $completion);
            anonymousClass56.L$0 = value;
            return anonymousClass56;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object chapterListByRule = this.$bookController.getChapterListByRule(it, (Continuation) this);
                    return chapterListByRule == coroutine_suspended ? coroutine_suspended : chapterListByRule;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$57, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$57.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {252}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$57")
    static final class AnonymousClass57 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookGroupController $bookGroupController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass57(BookGroupController $bookGroupController, Continuation<? super AnonymousClass57> $completion) {
            super(2, $completion);
            this.$bookGroupController = $bookGroupController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass57 = new AnonymousClass57(this.$bookGroupController, $completion);
            anonymousClass57.L$0 = value;
            return anonymousClass57;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object list = this.$bookGroupController.list(it, (Continuation) this);
                    return list == coroutine_suspended ? coroutine_suspended : list;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$58, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$58.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {253}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$58")
    static final class AnonymousClass58 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookGroupController $bookGroupController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass58(BookGroupController $bookGroupController, Continuation<? super AnonymousClass58> $completion) {
            super(2, $completion);
            this.$bookGroupController = $bookGroupController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass58 = new AnonymousClass58(this.$bookGroupController, $completion);
            anonymousClass58.L$0 = value;
            return anonymousClass58;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSave = this.$bookGroupController.save(it, (Continuation) this);
                    return objSave == coroutine_suspended ? coroutine_suspended : objSave;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$59, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$59.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {254}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$59")
    static final class AnonymousClass59 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookGroupController $bookGroupController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass59(BookGroupController $bookGroupController, Continuation<? super AnonymousClass59> $completion) {
            super(2, $completion);
            this.$bookGroupController = $bookGroupController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass59 = new AnonymousClass59(this.$bookGroupController, $completion);
            anonymousClass59.L$0 = value;
            return anonymousClass59;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDelete = this.$bookGroupController.delete(it, (Continuation) this);
                    return objDelete == coroutine_suspended ? coroutine_suspended : objDelete;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$60, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$60.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {255}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$60")
    static final class AnonymousClass60 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookGroupController $bookGroupController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass60(BookGroupController $bookGroupController, Continuation<? super AnonymousClass60> $completion) {
            super(2, $completion);
            this.$bookGroupController = $bookGroupController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass60 = new AnonymousClass60(this.$bookGroupController, $completion);
            anonymousClass60.L$0 = value;
            return anonymousClass60;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookGroupOrder = this.$bookGroupController.saveBookGroupOrder(it, (Continuation) this);
                    return objSaveBookGroupOrder == coroutine_suspended ? coroutine_suspended : objSaveBookGroupOrder;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$61, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$61.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {258}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$61")
    static final class AnonymousClass61 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass61(BookController $bookController, Continuation<? super AnonymousClass61> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass61 = new AnonymousClass61(this.$bookController, $completion);
            anonymousClass61.L$0 = value;
            return anonymousClass61;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.bookSourceDebugSSE(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$62, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$62.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {261}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$62")
    static final class AnonymousClass62 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass62(BookController $bookController, Continuation<? super AnonymousClass62> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass62 = new AnonymousClass62(this.$bookController, $completion);
            anonymousClass62.L$0 = value;
            return anonymousClass62;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.cacheBookSSE(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$63, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$63.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {263}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$63")
    static final class AnonymousClass63 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass63(BookController $bookController, Continuation<? super AnonymousClass63> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass63 = new AnonymousClass63(this.$bookController, $completion);
            anonymousClass63.L$0 = value;
            return anonymousClass63;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objCacheBookOnServer = this.$bookController.cacheBookOnServer(it, (Continuation) this);
                    return objCacheBookOnServer == coroutine_suspended ? coroutine_suspended : objCacheBookOnServer;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$64, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$64.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {265}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$64")
    static final class AnonymousClass64 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass64(BookController $bookController, Continuation<? super AnonymousClass64> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass64 = new AnonymousClass64(this.$bookController, $completion);
            anonymousClass64.L$0 = value;
            return anonymousClass64;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object shelfBookWithCacheInfo = this.$bookController.getShelfBookWithCacheInfo(it, (Continuation) this);
                    return shelfBookWithCacheInfo == coroutine_suspended ? coroutine_suspended : shelfBookWithCacheInfo;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$65, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$65.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {267}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$65")
    static final class AnonymousClass65 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass65(BookController $bookController, Continuation<? super AnonymousClass65> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass65 = new AnonymousClass65(this.$bookController, $completion);
            anonymousClass65.L$0 = value;
            return anonymousClass65;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteBookCache = this.$bookController.deleteBookCache(it, (Continuation) this);
                    return objDeleteBookCache == coroutine_suspended ? coroutine_suspended : objDeleteBookCache;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$66, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$66.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {270}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$66")
    static final class AnonymousClass66 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass66(BookController $bookController, Continuation<? super AnonymousClass66> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass66 = new AnonymousClass66(this.$bookController, $completion);
            anonymousClass66.L$0 = value;
            return anonymousClass66;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.exportBook(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$67, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$67.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {271}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$67")
    static final class AnonymousClass67 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass67(BookController $bookController, Continuation<? super AnonymousClass67> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass67 = new AnonymousClass67(this.$bookController, $completion);
            anonymousClass67.L$0 = value;
            return anonymousClass67;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.exportBook(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$68, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$68.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {274}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$68")
    static final class AnonymousClass68 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass68(BookController $bookController, Continuation<? super AnonymousClass68> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass68 = new AnonymousClass68(this.$bookController, $completion);
            anonymousClass68.L$0 = value;
            return anonymousClass68;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBookContent = this.$bookController.searchBookContent(it, (Continuation) this);
                    return objSearchBookContent == coroutine_suspended ? coroutine_suspended : objSearchBookContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$69, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$69.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {275}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$69")
    static final class AnonymousClass69 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass69(BookController $bookController, Continuation<? super AnonymousClass69> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass69 = new AnonymousClass69(this.$bookController, $completion);
            anonymousClass69.L$0 = value;
            return anonymousClass69;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSearchBookContent = this.$bookController.searchBookContent(it, (Continuation) this);
                    return objSearchBookContent == coroutine_suspended ? coroutine_suspended : objSearchBookContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$70, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$70.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {278}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$70")
    static final class AnonymousClass70 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass70(BookController $bookController, Continuation<? super AnonymousClass70> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass70 = new AnonymousClass70(this.$bookController, $completion);
            anonymousClass70.L$0 = value;
            return anonymousClass70;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objBackupToMongodb = this.$bookController.backupToMongodb(it, (Continuation) this);
                    return objBackupToMongodb == coroutine_suspended ? coroutine_suspended : objBackupToMongodb;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$71, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$71.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {279}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$71")
    static final class AnonymousClass71 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass71(BookController $bookController, Continuation<? super AnonymousClass71> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass71 = new AnonymousClass71(this.$bookController, $completion);
            anonymousClass71.L$0 = value;
            return anonymousClass71;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objRestoreFromMongodb = this.$bookController.restoreFromMongodb(it, (Continuation) this);
                    return objRestoreFromMongodb == coroutine_suspended ? coroutine_suspended : objRestoreFromMongodb;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$72, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$72.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {282}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$72")
    static final class AnonymousClass72 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass72(BookController $bookController, Continuation<? super AnonymousClass72> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass72 = new AnonymousClass72(this.$bookController, $completion);
            anonymousClass72.L$0 = value;
            return anonymousClass72;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveBookConfig = this.$bookController.saveBookConfig(it, (Continuation) this);
                    return objSaveBookConfig == coroutine_suspended ? coroutine_suspended : objSaveBookConfig;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$73, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$73.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {285}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$73")
    static final class AnonymousClass73 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass73(BookController $bookController, Continuation<? super AnonymousClass73> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass73 = new AnonymousClass73(this.$bookController, $completion);
            anonymousClass73.L$0 = value;
            return anonymousClass73;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.textToSpeech(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$74, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$74.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {286}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$74")
    static final class AnonymousClass74 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookController $bookController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass74(BookController $bookController, Continuation<? super AnonymousClass74> $completion) {
            super(2, $completion);
            this.$bookController = $bookController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass74 = new AnonymousClass74(this.$bookController, $completion);
            anonymousClass74.L$0 = value;
            return anonymousClass74;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$bookController.textToSpeech(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$75, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$75.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {290}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$75")
    static final class AnonymousClass75 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass75(UserController $userController, Continuation<? super AnonymousClass75> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass75 = new AnonymousClass75(this.$userController, $completion);
            anonymousClass75.L$0 = value;
            return anonymousClass75;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objUploadFile = this.$userController.uploadFile(it, (Continuation) this);
                    return objUploadFile == coroutine_suspended ? coroutine_suspended : objUploadFile;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$76, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$76.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {293}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$76")
    static final class AnonymousClass76 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass76(UserController $userController, Continuation<? super AnonymousClass76> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass76 = new AnonymousClass76(this.$userController, $completion);
            anonymousClass76.L$0 = value;
            return anonymousClass76;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteFile = this.$userController.deleteFile(it, (Continuation) this);
                    return objDeleteFile == coroutine_suspended ? coroutine_suspended : objDeleteFile;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$77, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$77.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {296}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$77")
    static final class AnonymousClass77 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass77(UserController $userController, Continuation<? super AnonymousClass77> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass77 = new AnonymousClass77(this.$userController, $completion);
            anonymousClass77.L$0 = value;
            return anonymousClass77;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objLogin = this.$userController.login(it, (Continuation) this);
                    return objLogin == coroutine_suspended ? coroutine_suspended : objLogin;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$78, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$78.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {298}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$78")
    static final class AnonymousClass78 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass78(UserController $userController, Continuation<? super AnonymousClass78> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass78 = new AnonymousClass78(this.$userController, $completion);
            anonymousClass78.L$0 = value;
            return anonymousClass78;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objLogout = this.$userController.logout(it, (Continuation) this);
                    return objLogout == coroutine_suspended ? coroutine_suspended : objLogout;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$79, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$79.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {301}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$79")
    static final class AnonymousClass79 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass79(UserController $userController, Continuation<? super AnonymousClass79> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass79 = new AnonymousClass79(this.$userController, $completion);
            anonymousClass79.L$0 = value;
            return anonymousClass79;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object userInfo = this.$userController.getUserInfo(it, (Continuation) this);
                    return userInfo == coroutine_suspended ? coroutine_suspended : userInfo;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$80, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$80.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {304}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$80")
    static final class AnonymousClass80 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass80(UserController $userController, Continuation<? super AnonymousClass80> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass80 = new AnonymousClass80(this.$userController, $completion);
            anonymousClass80.L$0 = value;
            return anonymousClass80;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveUserConfig = this.$userController.saveUserConfig(it, (Continuation) this);
                    return objSaveUserConfig == coroutine_suspended ? coroutine_suspended : objSaveUserConfig;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$81, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$81.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {307}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$81")
    static final class AnonymousClass81 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass81(UserController $userController, Continuation<? super AnonymousClass81> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass81 = new AnonymousClass81(this.$userController, $completion);
            anonymousClass81.L$0 = value;
            return anonymousClass81;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object userConfig = this.$userController.getUserConfig(it, (Continuation) this);
                    return userConfig == coroutine_suspended ? coroutine_suspended : userConfig;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$82, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$82.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {310}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$82")
    static final class AnonymousClass82 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass82(UserController $userController, Continuation<? super AnonymousClass82> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass82 = new AnonymousClass82(this.$userController, $completion);
            anonymousClass82.L$0 = value;
            return anonymousClass82;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object userList = this.$userController.getUserList(it, (Continuation) this);
                    return userList == coroutine_suspended ? coroutine_suspended : userList;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$83, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$83.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {313}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$83")
    static final class AnonymousClass83 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass83(UserController $userController, Continuation<? super AnonymousClass83> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass83 = new AnonymousClass83(this.$userController, $completion);
            anonymousClass83.L$0 = value;
            return anonymousClass83;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteUsers = this.$userController.deleteUsers(it, (Continuation) this);
                    return objDeleteUsers == coroutine_suspended ? coroutine_suspended : objDeleteUsers;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$84, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$84.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {316}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$84")
    static final class AnonymousClass84 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass84(UserController $userController, Continuation<? super AnonymousClass84> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass84 = new AnonymousClass84(this.$userController, $completion);
            anonymousClass84.L$0 = value;
            return anonymousClass84;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objClearInactiveUsers = this.$userController.clearInactiveUsers(it, (Continuation<? super ReturnData>) this);
                    return objClearInactiveUsers == coroutine_suspended ? coroutine_suspended : objClearInactiveUsers;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$85, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$85.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {319}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$85")
    static final class AnonymousClass85 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass85(UserController $userController, Continuation<? super AnonymousClass85> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass85 = new AnonymousClass85(this.$userController, $completion);
            anonymousClass85.L$0 = value;
            return anonymousClass85;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objAddUser = this.$userController.addUser(it, (Continuation) this);
                    return objAddUser == coroutine_suspended ? coroutine_suspended : objAddUser;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$86, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$86.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {322}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$86")
    static final class AnonymousClass86 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass86(UserController $userController, Continuation<? super AnonymousClass86> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass86 = new AnonymousClass86(this.$userController, $completion);
            anonymousClass86.L$0 = value;
            return anonymousClass86;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objResetPassword = this.$userController.resetPassword(it, (Continuation) this);
                    return objResetPassword == coroutine_suspended ? coroutine_suspended : objResetPassword;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$87, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$87.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {325}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$87")
    static final class AnonymousClass87 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass87(UserController $userController, Continuation<? super AnonymousClass87> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass87 = new AnonymousClass87(this.$userController, $completion);
            anonymousClass87.L$0 = value;
            return anonymousClass87;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objUpdateUser = this.$userController.updateUser(it, (Continuation) this);
                    return objUpdateUser == coroutine_suspended ? coroutine_suspended : objUpdateUser;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$88, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$88.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {327}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$88")
    static final class AnonymousClass88 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ UserController $userController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass88(UserController $userController, Continuation<? super AnonymousClass88> $completion) {
            super(2, $completion);
            this.$userController = $userController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass88 = new AnonymousClass88(this.$userController, $completion);
            anonymousClass88.L$0 = value;
            return anonymousClass88;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$userController.downloadBackupFile(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$89, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$89.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {330}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$89")
    static final class AnonymousClass89 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass89(LicenseController $licenseController, Continuation<? super AnonymousClass89> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass89 = new AnonymousClass89(this.$licenseController, $completion);
            anonymousClass89.L$0 = value;
            return anonymousClass89;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object license = this.$licenseController.getLicense(it, (Continuation) this);
                    return license == coroutine_suspended ? coroutine_suspended : license;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$90, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$90.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {331}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$90")
    static final class AnonymousClass90 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass90(LicenseController $licenseController, Continuation<? super AnonymousClass90> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass90 = new AnonymousClass90(this.$licenseController, $completion);
            anonymousClass90.L$0 = value;
            return anonymousClass90;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$licenseController.importLicense(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$91, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$91.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {332}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$91")
    static final class AnonymousClass91 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass91(LicenseController $licenseController, Continuation<? super AnonymousClass91> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass91 = new AnonymousClass91(this.$licenseController, $completion);
            anonymousClass91.L$0 = value;
            return anonymousClass91;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws NoSuchAlgorithmException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objGenerateKeys = this.$licenseController.generateKeys(it, (Continuation) this);
                    return objGenerateKeys == coroutine_suspended ? coroutine_suspended : objGenerateKeys;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$92, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$92.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {333}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$92")
    static final class AnonymousClass92 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass92(LicenseController $licenseController, Continuation<? super AnonymousClass92> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass92 = new AnonymousClass92(this.$licenseController, $completion);
            anonymousClass92.L$0 = value;
            return anonymousClass92;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws NoSuchAlgorithmException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objGenerateKeys = this.$licenseController.generateKeys(it, (Continuation) this);
                    return objGenerateKeys == coroutine_suspended ? coroutine_suspended : objGenerateKeys;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$93, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$93.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {334}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$93")
    static final class AnonymousClass93 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass93(LicenseController $licenseController, Continuation<? super AnonymousClass93> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass93 = new AnonymousClass93(this.$licenseController, $completion);
            anonymousClass93.L$0 = value;
            return anonymousClass93;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws InvalidKeySpecException, IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objGenerateLicense = this.$licenseController.generateLicense(it, (Continuation) this);
                    return objGenerateLicense == coroutine_suspended ? coroutine_suspended : objGenerateLicense;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$94, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$94.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {335}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$94")
    static final class AnonymousClass94 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass94(LicenseController $licenseController, Continuation<? super AnonymousClass94> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass94 = new AnonymousClass94(this.$licenseController, $completion);
            anonymousClass94.L$0 = value;
            return anonymousClass94;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws InvalidKeySpecException, IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objGenerateLicense = this.$licenseController.generateLicense(it, (Continuation) this);
                    return objGenerateLicense == coroutine_suspended ? coroutine_suspended : objGenerateLicense;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$95, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$95.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {336}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$95")
    static final class AnonymousClass95 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass95(LicenseController $licenseController, Continuation<? super AnonymousClass95> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass95 = new AnonymousClass95(this.$licenseController, $completion);
            anonymousClass95.L$0 = value;
            return anonymousClass95;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objIsHostValid = this.$licenseController.isHostValid(it, (Continuation) this);
                    return objIsHostValid == coroutine_suspended ? coroutine_suspended : objIsHostValid;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$96, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$96.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {337}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$96")
    static final class AnonymousClass96 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass96(LicenseController $licenseController, Continuation<? super AnonymousClass96> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass96 = new AnonymousClass96(this.$licenseController, $completion);
            anonymousClass96.L$0 = value;
            return anonymousClass96;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objIsHostValid = this.$licenseController.isHostValid(it, (Continuation) this);
                    return objIsHostValid == coroutine_suspended ? coroutine_suspended : objIsHostValid;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$97, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$97.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {339}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$97")
    static final class AnonymousClass97 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass97(LicenseController $licenseController, Continuation<? super AnonymousClass97> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass97 = new AnonymousClass97(this.$licenseController, $completion);
            anonymousClass97.L$0 = value;
            return anonymousClass97;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws InvalidKeySpecException, IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objActivateLicense = this.$licenseController.activateLicense(it, (Continuation) this);
                    return objActivateLicense == coroutine_suspended ? coroutine_suspended : objActivateLicense;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$98, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$98.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {341}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$98")
    static final class AnonymousClass98 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass98(LicenseController $licenseController, Continuation<? super AnonymousClass98> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass98 = new AnonymousClass98(this.$licenseController, $completion);
            anonymousClass98.L$0 = value;
            return anonymousClass98;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws InvalidKeySpecException, IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objIsLicenseValid = this.$licenseController.isLicenseValid(it, (Continuation) this);
                    return objIsLicenseValid == coroutine_suspended ? coroutine_suspended : objIsLicenseValid;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$99, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$99.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {342}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$99")
    static final class AnonymousClass99 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass99(LicenseController $licenseController, Continuation<? super AnonymousClass99> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass99 = new AnonymousClass99(this.$licenseController, $completion);
            anonymousClass99.L$0 = value;
            return anonymousClass99;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws InvalidKeySpecException, IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objIsLicenseValid = this.$licenseController.isLicenseValid(it, (Continuation) this);
                    return objIsLicenseValid == coroutine_suspended ? coroutine_suspended : objIsLicenseValid;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$100, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$100.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {344}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$100")
    static final class AnonymousClass100 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass100(LicenseController $licenseController, Continuation<? super AnonymousClass100> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass100 = new AnonymousClass100(this.$licenseController, $completion);
            anonymousClass100.L$0 = value;
            return anonymousClass100;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDecryptLicense = this.$licenseController.decryptLicense(it, (Continuation) this);
                    return objDecryptLicense == coroutine_suspended ? coroutine_suspended : objDecryptLicense;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$101, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$101.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {346}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$101")
    static final class AnonymousClass101 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass101(LicenseController $licenseController, Continuation<? super AnonymousClass101> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass101 = new AnonymousClass101(this.$licenseController, $completion);
            anonymousClass101.L$0 = value;
            return anonymousClass101;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSendCodeToEmail = this.$licenseController.sendCodeToEmail(it, (Continuation) this);
                    return objSendCodeToEmail == coroutine_suspended ? coroutine_suspended : objSendCodeToEmail;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$102, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$102.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {348}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$102")
    static final class AnonymousClass102 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ LicenseController $licenseController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass102(LicenseController $licenseController, Continuation<? super AnonymousClass102> $completion) {
            super(2, $completion);
            this.$licenseController = $licenseController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass102 = new AnonymousClass102(this.$licenseController, $completion);
            anonymousClass102.L$0 = value;
            return anonymousClass102;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws InvalidKeySpecException, IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSupplyLicense = this.$licenseController.supplyLicense(it, (Continuation) this);
                    return objSupplyLicense == coroutine_suspended ? coroutine_suspended : objSupplyLicense;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$103, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$103.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {352}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$103")
    static final class AnonymousClass103 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ WebdavController $webdavController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass103(WebdavController $webdavController, Continuation<? super AnonymousClass103> $completion) {
            super(2, $completion);
            this.$webdavController = $webdavController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass103 = new AnonymousClass103(this.$webdavController, $completion);
            anonymousClass103.L$0 = value;
            return anonymousClass103;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objBackupToWebdav = this.$webdavController.backupToWebdav(it, (Continuation) this);
                    return objBackupToWebdav == coroutine_suspended ? coroutine_suspended : objBackupToWebdav;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$104, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$104.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {357}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$104")
    static final class AnonymousClass104 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass104(RssSourceController $rssSourceController, Continuation<? super AnonymousClass104> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass104 = new AnonymousClass104(this.$rssSourceController, $completion);
            anonymousClass104.L$0 = value;
            return anonymousClass104;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object rssSources = this.$rssSourceController.getRssSources(it, (Continuation) this);
                    return rssSources == coroutine_suspended ? coroutine_suspended : rssSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$105, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$105.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {358}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$105")
    static final class AnonymousClass105 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass105(RssSourceController $rssSourceController, Continuation<? super AnonymousClass105> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass105 = new AnonymousClass105(this.$rssSourceController, $completion);
            anonymousClass105.L$0 = value;
            return anonymousClass105;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveRssSource = this.$rssSourceController.saveRssSource(it, (Continuation) this);
                    return objSaveRssSource == coroutine_suspended ? coroutine_suspended : objSaveRssSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$106, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$106.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {359}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$106")
    static final class AnonymousClass106 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass106(RssSourceController $rssSourceController, Continuation<? super AnonymousClass106> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass106 = new AnonymousClass106(this.$rssSourceController, $completion);
            anonymousClass106.L$0 = value;
            return anonymousClass106;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveRssSources = this.$rssSourceController.saveRssSources(it, (Continuation) this);
                    return objSaveRssSources == coroutine_suspended ? coroutine_suspended : objSaveRssSources;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$107, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$107.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {360}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$107")
    static final class AnonymousClass107 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass107(RssSourceController $rssSourceController, Continuation<? super AnonymousClass107> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass107 = new AnonymousClass107(this.$rssSourceController, $completion);
            anonymousClass107.L$0 = value;
            return anonymousClass107;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteRssSource = this.$rssSourceController.deleteRssSource(it, (Continuation) this);
                    return objDeleteRssSource == coroutine_suspended ? coroutine_suspended : objDeleteRssSource;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$108, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$108.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {362}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$108")
    static final class AnonymousClass108 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass108(RssSourceController $rssSourceController, Continuation<? super AnonymousClass108> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass108 = new AnonymousClass108(this.$rssSourceController, $completion);
            anonymousClass108.L$0 = value;
            return anonymousClass108;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object rssArticles = this.$rssSourceController.getRssArticles(it, (Continuation) this);
                    return rssArticles == coroutine_suspended ? coroutine_suspended : rssArticles;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$109, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$109.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {363}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$109")
    static final class AnonymousClass109 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass109(RssSourceController $rssSourceController, Continuation<? super AnonymousClass109> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass109 = new AnonymousClass109(this.$rssSourceController, $completion);
            anonymousClass109.L$0 = value;
            return anonymousClass109;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object rssArticles = this.$rssSourceController.getRssArticles(it, (Continuation) this);
                    return rssArticles == coroutine_suspended ? coroutine_suspended : rssArticles;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$110, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$110.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {365}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$110")
    static final class AnonymousClass110 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass110(RssSourceController $rssSourceController, Continuation<? super AnonymousClass110> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass110 = new AnonymousClass110(this.$rssSourceController, $completion);
            anonymousClass110.L$0 = value;
            return anonymousClass110;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object rssContent = this.$rssSourceController.getRssContent(it, (Continuation) this);
                    return rssContent == coroutine_suspended ? coroutine_suspended : rssContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$111, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$111.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {366}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$111")
    static final class AnonymousClass111 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ RssSourceController $rssSourceController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass111(RssSourceController $rssSourceController, Continuation<? super AnonymousClass111> $completion) {
            super(2, $completion);
            this.$rssSourceController = $rssSourceController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass111 = new AnonymousClass111(this.$rssSourceController, $completion);
            anonymousClass111.L$0 = value;
            return anonymousClass111;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object rssContent = this.$rssSourceController.getRssContent(it, (Continuation) this);
                    return rssContent == coroutine_suspended ? coroutine_suspended : rssContent;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$112, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$112.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {369}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$112")
    static final class AnonymousClass112 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ ReplaceRuleController $replaceRuleController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass112(ReplaceRuleController $replaceRuleController, Continuation<? super AnonymousClass112> $completion) {
            super(2, $completion);
            this.$replaceRuleController = $replaceRuleController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass112 = new AnonymousClass112(this.$replaceRuleController, $completion);
            anonymousClass112.L$0 = value;
            return anonymousClass112;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object list = this.$replaceRuleController.list(it, (Continuation) this);
                    return list == coroutine_suspended ? coroutine_suspended : list;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$113, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$113.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {370}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$113")
    static final class AnonymousClass113 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ ReplaceRuleController $replaceRuleController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass113(ReplaceRuleController $replaceRuleController, Continuation<? super AnonymousClass113> $completion) {
            super(2, $completion);
            this.$replaceRuleController = $replaceRuleController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass113 = new AnonymousClass113(this.$replaceRuleController, $completion);
            anonymousClass113.L$0 = value;
            return anonymousClass113;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSave = this.$replaceRuleController.save(it, (Continuation) this);
                    return objSave == coroutine_suspended ? coroutine_suspended : objSave;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$114, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$114.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {371}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$114")
    static final class AnonymousClass114 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ ReplaceRuleController $replaceRuleController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass114(ReplaceRuleController $replaceRuleController, Continuation<? super AnonymousClass114> $completion) {
            super(2, $completion);
            this.$replaceRuleController = $replaceRuleController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass114 = new AnonymousClass114(this.$replaceRuleController, $completion);
            anonymousClass114.L$0 = value;
            return anonymousClass114;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveMulti = this.$replaceRuleController.saveMulti(it, (Continuation) this);
                    return objSaveMulti == coroutine_suspended ? coroutine_suspended : objSaveMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$115, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$115.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {372}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$115")
    static final class AnonymousClass115 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ ReplaceRuleController $replaceRuleController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass115(ReplaceRuleController $replaceRuleController, Continuation<? super AnonymousClass115> $completion) {
            super(2, $completion);
            this.$replaceRuleController = $replaceRuleController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass115 = new AnonymousClass115(this.$replaceRuleController, $completion);
            anonymousClass115.L$0 = value;
            return anonymousClass115;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDelete = this.$replaceRuleController.delete(it, (Continuation) this);
                    return objDelete == coroutine_suspended ? coroutine_suspended : objDelete;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$116, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$116.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {373}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$116")
    static final class AnonymousClass116 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ ReplaceRuleController $replaceRuleController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass116(ReplaceRuleController $replaceRuleController, Continuation<? super AnonymousClass116> $completion) {
            super(2, $completion);
            this.$replaceRuleController = $replaceRuleController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass116 = new AnonymousClass116(this.$replaceRuleController, $completion);
            anonymousClass116.L$0 = value;
            return anonymousClass116;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteMulti = this.$replaceRuleController.deleteMulti(it, (Continuation) this);
                    return objDeleteMulti == coroutine_suspended ? coroutine_suspended : objDeleteMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$117, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$117.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {376}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$117")
    static final class AnonymousClass117 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookmarkController $bookmarkController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass117(BookmarkController $bookmarkController, Continuation<? super AnonymousClass117> $completion) {
            super(2, $completion);
            this.$bookmarkController = $bookmarkController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass117 = new AnonymousClass117(this.$bookmarkController, $completion);
            anonymousClass117.L$0 = value;
            return anonymousClass117;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object list = this.$bookmarkController.list(it, (Continuation) this);
                    return list == coroutine_suspended ? coroutine_suspended : list;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$118, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$118.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {377}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$118")
    static final class AnonymousClass118 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookmarkController $bookmarkController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass118(BookmarkController $bookmarkController, Continuation<? super AnonymousClass118> $completion) {
            super(2, $completion);
            this.$bookmarkController = $bookmarkController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass118 = new AnonymousClass118(this.$bookmarkController, $completion);
            anonymousClass118.L$0 = value;
            return anonymousClass118;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSave = this.$bookmarkController.save(it, (Continuation) this);
                    return objSave == coroutine_suspended ? coroutine_suspended : objSave;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$119, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$119.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {378}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$119")
    static final class AnonymousClass119 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookmarkController $bookmarkController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass119(BookmarkController $bookmarkController, Continuation<? super AnonymousClass119> $completion) {
            super(2, $completion);
            this.$bookmarkController = $bookmarkController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass119 = new AnonymousClass119(this.$bookmarkController, $completion);
            anonymousClass119.L$0 = value;
            return anonymousClass119;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveMulti = this.$bookmarkController.saveMulti(it, (Continuation) this);
                    return objSaveMulti == coroutine_suspended ? coroutine_suspended : objSaveMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$120, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$120.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {379}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$120")
    static final class AnonymousClass120 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookmarkController $bookmarkController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass120(BookmarkController $bookmarkController, Continuation<? super AnonymousClass120> $completion) {
            super(2, $completion);
            this.$bookmarkController = $bookmarkController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass120 = new AnonymousClass120(this.$bookmarkController, $completion);
            anonymousClass120.L$0 = value;
            return anonymousClass120;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDelete = this.$bookmarkController.delete(it, (Continuation) this);
                    return objDelete == coroutine_suspended ? coroutine_suspended : objDelete;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$121, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$121.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {380}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$121")
    static final class AnonymousClass121 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ BookmarkController $bookmarkController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass121(BookmarkController $bookmarkController, Continuation<? super AnonymousClass121> $completion) {
            super(2, $completion);
            this.$bookmarkController = $bookmarkController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass121 = new AnonymousClass121(this.$bookmarkController, $completion);
            anonymousClass121.L$0 = value;
            return anonymousClass121;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteMulti = this.$bookmarkController.deleteMulti(it, (Continuation) this);
                    return objDeleteMulti == coroutine_suspended ? coroutine_suspended : objDeleteMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$122, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$122.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {384}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$122")
    static final class AnonymousClass122 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass122(FileController $fileController, Continuation<? super AnonymousClass122> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass122 = new AnonymousClass122(this.$fileController, $completion);
            anonymousClass122.L$0 = value;
            return anonymousClass122;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object list = this.$fileController.list(it, (Continuation) this);
                    return list == coroutine_suspended ? coroutine_suspended : list;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$123, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$123.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {387}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$123")
    static final class AnonymousClass123 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass123(FileController $fileController, Continuation<? super AnonymousClass123> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass123 = new AnonymousClass123(this.$fileController, $completion);
            anonymousClass123.L$0 = value;
            return anonymousClass123;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object obj = this.$fileController.get(it, (Continuation) this);
                    return obj == coroutine_suspended ? coroutine_suspended : obj;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$124, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$124.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {390}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$124")
    static final class AnonymousClass124 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass124(FileController $fileController, Continuation<? super AnonymousClass124> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass124 = new AnonymousClass124(this.$fileController, $completion);
            anonymousClass124.L$0 = value;
            return anonymousClass124;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws IOException {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSave = this.$fileController.save(it, (Continuation) this);
                    return objSave == coroutine_suspended ? coroutine_suspended : objSave;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$125, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$125.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {393}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$125")
    static final class AnonymousClass125 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass125(FileController $fileController, Continuation<? super AnonymousClass125> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass125 = new AnonymousClass125(this.$fileController, $completion);
            anonymousClass125.L$0 = value;
            return anonymousClass125;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objMkdir = this.$fileController.mkdir(it, (Continuation) this);
                    return objMkdir == coroutine_suspended ? coroutine_suspended : objMkdir;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$126, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$126.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {396}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$126")
    static final class AnonymousClass126 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Unit>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass126(FileController $fileController, Continuation<? super AnonymousClass126> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass126 = new AnonymousClass126(this.$fileController, $completion);
            anonymousClass126.L$0 = value;
            return anonymousClass126;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    if (this.$fileController.download(it, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$127, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$127.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {399}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$127")
    static final class AnonymousClass127 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass127(FileController $fileController, Continuation<? super AnonymousClass127> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass127 = new AnonymousClass127(this.$fileController, $completion);
            anonymousClass127.L$0 = value;
            return anonymousClass127;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objUpload = this.$fileController.upload(it, (Continuation) this);
                    return objUpload == coroutine_suspended ? coroutine_suspended : objUpload;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$128, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$128.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {402}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$128")
    static final class AnonymousClass128 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass128(FileController $fileController, Continuation<? super AnonymousClass128> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass128 = new AnonymousClass128(this.$fileController, $completion);
            anonymousClass128.L$0 = value;
            return anonymousClass128;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDelete = this.$fileController.delete(it, (Continuation) this);
                    return objDelete == coroutine_suspended ? coroutine_suspended : objDelete;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$129, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$129.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {403}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$129")
    static final class AnonymousClass129 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass129(FileController $fileController, Continuation<? super AnonymousClass129> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass129 = new AnonymousClass129(this.$fileController, $completion);
            anonymousClass129.L$0 = value;
            return anonymousClass129;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteMulti = this.$fileController.deleteMulti(it, (Continuation) this);
                    return objDeleteMulti == coroutine_suspended ? coroutine_suspended : objDeleteMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$130, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$130.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {406}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$130")
    static final class AnonymousClass130 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass130(FileController $fileController, Continuation<? super AnonymousClass130> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass130 = new AnonymousClass130(this.$fileController, $completion);
            anonymousClass130.L$0 = value;
            return anonymousClass130;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objImportPreview = this.$fileController.importPreview(it, (Continuation) this);
                    return objImportPreview == coroutine_suspended ? coroutine_suspended : objImportPreview;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$131, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$131.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {409}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$131")
    static final class AnonymousClass131 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass131(FileController $fileController, Continuation<? super AnonymousClass131> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass131 = new AnonymousClass131(this.$fileController, $completion);
            anonymousClass131.L$0 = value;
            return anonymousClass131;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objRestore = this.$fileController.restore(it, (Continuation) this);
                    return objRestore == coroutine_suspended ? coroutine_suspended : objRestore;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$132, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$132.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {411}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$132")
    static final class AnonymousClass132 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass132(FileController $fileController, Continuation<? super AnonymousClass132> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass132 = new AnonymousClass132(this.$fileController, $completion);
            anonymousClass132.L$0 = value;
            return anonymousClass132;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object obj = this.$fileController.parse(it, (Continuation) this);
                    return obj == coroutine_suspended ? coroutine_suspended : obj;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$133, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$133.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {412}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$133")
    static final class AnonymousClass133 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ FileController $fileController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass133(FileController $fileController, Continuation<? super AnonymousClass133> $completion) {
            super(2, $completion);
            this.$fileController = $fileController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass133 = new AnonymousClass133(this.$fileController, $completion);
            anonymousClass133.L$0 = value;
            return anonymousClass133;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object obj = this.$fileController.parse(it, (Continuation) this);
                    return obj == coroutine_suspended ? coroutine_suspended : obj;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$134, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$134.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {415}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$134")
    static final class AnonymousClass134 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ HttpTTSController $httpTTSController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass134(HttpTTSController $httpTTSController, Continuation<? super AnonymousClass134> $completion) {
            super(2, $completion);
            this.$httpTTSController = $httpTTSController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass134 = new AnonymousClass134(this.$httpTTSController, $completion);
            anonymousClass134.L$0 = value;
            return anonymousClass134;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object list = this.$httpTTSController.list(it, (Continuation) this);
                    return list == coroutine_suspended ? coroutine_suspended : list;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$135, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$135.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {416}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$135")
    static final class AnonymousClass135 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ HttpTTSController $httpTTSController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass135(HttpTTSController $httpTTSController, Continuation<? super AnonymousClass135> $completion) {
            super(2, $completion);
            this.$httpTTSController = $httpTTSController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass135 = new AnonymousClass135(this.$httpTTSController, $completion);
            anonymousClass135.L$0 = value;
            return anonymousClass135;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSave = this.$httpTTSController.save(it, (Continuation) this);
                    return objSave == coroutine_suspended ? coroutine_suspended : objSave;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$136, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$136.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {417}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$136")
    static final class AnonymousClass136 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ HttpTTSController $httpTTSController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass136(HttpTTSController $httpTTSController, Continuation<? super AnonymousClass136> $completion) {
            super(2, $completion);
            this.$httpTTSController = $httpTTSController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass136 = new AnonymousClass136(this.$httpTTSController, $completion);
            anonymousClass136.L$0 = value;
            return anonymousClass136;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objSaveMulti = this.$httpTTSController.saveMulti(it, (Continuation) this);
                    return objSaveMulti == coroutine_suspended ? coroutine_suspended : objSaveMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$137, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$137.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {418}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$137")
    static final class AnonymousClass137 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ HttpTTSController $httpTTSController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass137(HttpTTSController $httpTTSController, Continuation<? super AnonymousClass137> $completion) {
            super(2, $completion);
            this.$httpTTSController = $httpTTSController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass137 = new AnonymousClass137(this.$httpTTSController, $completion);
            anonymousClass137.L$0 = value;
            return anonymousClass137;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDelete = this.$httpTTSController.delete(it, (Continuation) this);
                    return objDelete == coroutine_suspended ? coroutine_suspended : objDelete;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$initRouter$138, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$138.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lio/vertx/ext/web/RoutingContext;"})
    @DebugMetadata(f = "YueduApi.kt", l = {419}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$initRouter$138")
    static final class AnonymousClass138 extends SuspendLambda implements Function2<RoutingContext, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ HttpTTSController $httpTTSController;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass138(HttpTTSController $httpTTSController, Continuation<? super AnonymousClass138> $completion) {
            super(2, $completion);
            this.$httpTTSController = $httpTTSController;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass138 = new AnonymousClass138(this.$httpTTSController, $completion);
            anonymousClass138.L$0 = value;
            return anonymousClass138;
        }

        @Nullable
        public final Object invoke(@NotNull RoutingContext p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext it = (RoutingContext) this.L$0;
                    this.label = 1;
                    Object objDeleteMulti = this.$httpTTSController.deleteMulti(it, (Continuation) this);
                    return objDeleteMulti == coroutine_suspended ? coroutine_suspended : objDeleteMulti;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    static /* synthetic */ Object setupPort$suspendImpl(YueduApi yueduApi, Continuation $completion) {
        YueduApiKt.logger.info("port: {}", Boxing.boxInt(yueduApi.getPort()));
        Environment environment = yueduApi.env;
        if (environment == null) {
            Intrinsics.throwUninitializedPropertyAccessException("env");
            throw null;
        }
        Integer serverPort = (Integer) environment.getProperty("reader.server.port", Integer.TYPE);
        YueduApiKt.logger.info("serverPort: {}", serverPort);
        if (serverPort != null && serverPort.intValue() > 0) {
            yueduApi.setPort(serverPort.intValue());
        }
        return Unit.INSTANCE;
    }

    static /* synthetic */ Object migration$suspendImpl(YueduApi yueduApi, Continuation $completion) {
        try {
            File storageDir = new File(ExtKt.getWorkDir("storage"));
            File dataDir = new File(ExtKt.getWorkDir("storage", "data", "default"));
            if (!storageDir.exists() || !dataDir.exists()) {
                dataDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Unit.INSTANCE;
    }

    @Override // com.htmake.reader.verticle.RestVerticle
    @NotNull
    public String getContextPath() {
        Environment environment = this.env;
        if (environment == null) {
            Intrinsics.throwUninitializedPropertyAccessException("env");
            throw null;
        }
        String contextPath = (String) environment.getProperty("reader.server.contextPath", String.class);
        String str = contextPath;
        if (!(str == null || str.length() == 0)) {
            Intrinsics.checkNotNullExpressionValue(contextPath, "contextPath");
            return contextPath;
        }
        return PackageDocumentBase.PREFIX_OPF;
    }

    @Override // com.htmake.reader.verticle.RestVerticle
    public void started() {
        SpringContextUtils.getApplicationContext().publishEvent(new SpringEvent(this, "READY", PackageDocumentBase.PREFIX_OPF));
    }

    @Override // com.htmake.reader.verticle.RestVerticle
    public void onStartError() {
        YueduApiKt.logger.error("应用启动失败，请检查" + getPort() + "端口是否被占用");
        SpringContextUtils.getApplicationContext().publishEvent(new SpringEvent(this, "START_ERROR", "应用启动失败，请检查" + getPort() + "端口是否被占用"));
    }

    @Override // com.htmake.reader.verticle.RestVerticle
    public void onHandlerError(@NotNull RoutingContext ctx, @NotNull Exception error) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(error, "error");
        ReturnData returnData = new ReturnData();
        YueduApiKt.logger.error("onHandlerError: ", error);
        if (!ctx.response().headWritten()) {
            VertExtKt.success(ctx, returnData.setErrorMsg(error.toString()));
        } else {
            ctx.response().end(error.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getSystemInfo(RoutingContext context, Continuation<? super ReturnData> $completion) {
        C00061 c00061;
        Ref.IntRef monthRegisterUser;
        Ref.IntRef sevenDayRegisterUser;
        Ref.IntRef dayRegisterUser;
        Ref.IntRef keepUser;
        Ref.IntRef monthLoginUser;
        Ref.IntRef sevenDayLoginUser;
        Ref.IntRef dayLoginUser;
        String maxMemory;
        String totalMemory;
        String freeMemory;
        String systemFont;
        ReturnData returnData;
        if ($completion instanceof C00061) {
            c00061 = (C00061) $completion;
            if ((c00061.label & Integer.MIN_VALUE) != 0) {
                c00061.label -= Integer.MIN_VALUE;
            } else {
                c00061 = new C00061($completion);
            }
        }
        Object $result = c00061.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00061.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                systemFont = System.getProperty("reader.system.fonts");
                freeMemory = PackageDocumentBase.PREFIX_OPF + ((Runtime.getRuntime().freeMemory() / ((long) 1024)) / ((long) 1024)) + 'M';
                totalMemory = PackageDocumentBase.PREFIX_OPF + ((Runtime.getRuntime().totalMemory() / ((long) 1024)) / ((long) 1024)) + 'M';
                maxMemory = PackageDocumentBase.PREFIX_OPF + ((Runtime.getRuntime().maxMemory() / ((long) 1024)) / ((long) 1024)) + 'M';
                UserController userController = new UserController(getCoroutineContext());
                dayLoginUser = new Ref.IntRef();
                sevenDayLoginUser = new Ref.IntRef();
                monthLoginUser = new Ref.IntRef();
                keepUser = new Ref.IntRef();
                dayRegisterUser = new Ref.IntRef();
                sevenDayRegisterUser = new Ref.IntRef();
                monthRegisterUser = new Ref.IntRef();
                Calendar calendar = Calendar.getInstance();
                calendar.set(5, 1);
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.getTimeInMillis();
                c00061.L$0 = returnData;
                c00061.L$1 = systemFont;
                c00061.L$2 = freeMemory;
                c00061.L$3 = totalMemory;
                c00061.L$4 = maxMemory;
                c00061.L$5 = dayLoginUser;
                c00061.L$6 = sevenDayLoginUser;
                c00061.L$7 = monthLoginUser;
                c00061.L$8 = keepUser;
                c00061.L$9 = dayRegisterUser;
                c00061.L$10 = sevenDayRegisterUser;
                c00061.L$11 = monthRegisterUser;
                c00061.label = 1;
                if (userController.forEachUser(new AnonymousClass2(dayLoginUser, sevenDayLoginUser, calendar, monthLoginUser, dayRegisterUser, sevenDayRegisterUser, monthRegisterUser, keepUser, null), c00061) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                monthRegisterUser = (Ref.IntRef) c00061.L$11;
                sevenDayRegisterUser = (Ref.IntRef) c00061.L$10;
                dayRegisterUser = (Ref.IntRef) c00061.L$9;
                keepUser = (Ref.IntRef) c00061.L$8;
                monthLoginUser = (Ref.IntRef) c00061.L$7;
                sevenDayLoginUser = (Ref.IntRef) c00061.L$6;
                dayLoginUser = (Ref.IntRef) c00061.L$5;
                maxMemory = (String) c00061.L$4;
                totalMemory = (String) c00061.L$3;
                freeMemory = (String) c00061.L$2;
                systemFont = (String) c00061.L$1;
                returnData = (ReturnData) c00061.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("fonts", systemFont), TuplesKt.to("freeMemory", freeMemory), TuplesKt.to("totalMemory", totalMemory), TuplesKt.to("maxMemory", maxMemory), TuplesKt.to("dayRegisterUser", Boxing.boxInt(dayRegisterUser.element)), TuplesKt.to("dayLoginUser", Boxing.boxInt(dayLoginUser.element)), TuplesKt.to("sevenDayRegisterUser", Boxing.boxInt(sevenDayRegisterUser.element)), TuplesKt.to("sevenDayLoginUser", Boxing.boxInt(sevenDayLoginUser.element)), TuplesKt.to("monthRegisterUser", Boxing.boxInt(monthRegisterUser.element)), TuplesKt.to("monthLoginUser", Boxing.boxInt(monthLoginUser.element)), TuplesKt.to("keepUser", Boxing.boxInt(keepUser.element))}), null, 2, null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$getSystemInfo$2, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$getSystemInfo$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "user", "Lcom/htmake/reader/entity/User;"})
    @DebugMetadata(f = "YueduApi.kt", l = {}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$getSystemInfo$2")
    static final class AnonymousClass2 extends SuspendLambda implements Function3<CoroutineScope, User, Continuation<? super Boolean>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ Ref.IntRef $dayLoginUser;
        final /* synthetic */ Ref.IntRef $sevenDayLoginUser;
        final /* synthetic */ Calendar $calendar;
        final /* synthetic */ Ref.IntRef $monthLoginUser;
        final /* synthetic */ Ref.IntRef $dayRegisterUser;
        final /* synthetic */ Ref.IntRef $sevenDayRegisterUser;
        final /* synthetic */ Ref.IntRef $monthRegisterUser;
        final /* synthetic */ Ref.IntRef $keepUser;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Ref.IntRef $dayLoginUser, Ref.IntRef $sevenDayLoginUser, Calendar $calendar, Ref.IntRef $monthLoginUser, Ref.IntRef $dayRegisterUser, Ref.IntRef $sevenDayRegisterUser, Ref.IntRef $monthRegisterUser, Ref.IntRef $keepUser, Continuation<? super AnonymousClass2> $completion) {
            super(3, $completion);
            this.$dayLoginUser = $dayLoginUser;
            this.$sevenDayLoginUser = $sevenDayLoginUser;
            this.$calendar = $calendar;
            this.$monthLoginUser = $monthLoginUser;
            this.$dayRegisterUser = $dayRegisterUser;
            this.$sevenDayRegisterUser = $sevenDayRegisterUser;
            this.$monthRegisterUser = $monthRegisterUser;
            this.$keepUser = $keepUser;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super Boolean> p3) {
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(this.$dayLoginUser, this.$sevenDayLoginUser, this.$calendar, this.$monthLoginUser, this.$dayRegisterUser, this.$sevenDayRegisterUser, this.$monthRegisterUser, this.$keepUser, p3);
            anonymousClass2.L$0 = p2;
            return anonymousClass2.invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    User user = (User) this.L$0;
                    if (user.getLast_login_at() >= System.currentTimeMillis() - 86400000) {
                        this.$dayLoginUser.element++;
                    }
                    if (user.getLast_login_at() >= System.currentTimeMillis() - 604800000) {
                        this.$sevenDayLoginUser.element++;
                    }
                    if (user.getLast_login_at() >= this.$calendar.getTimeInMillis()) {
                        this.$monthLoginUser.element++;
                    }
                    if (user.getCreated_at() >= System.currentTimeMillis() - 86400000) {
                        this.$dayRegisterUser.element++;
                    }
                    if (user.getCreated_at() >= System.currentTimeMillis() - 604800000) {
                        this.$sevenDayRegisterUser.element++;
                    }
                    if (user.getCreated_at() >= this.$calendar.getTimeInMillis()) {
                        this.$monthRegisterUser.element++;
                    }
                    if (user.getLast_login_at() >= user.getCreated_at() + 604800000 && user.getLast_login_at() >= System.currentTimeMillis() - 604800000) {
                        this.$keepUser.element++;
                    }
                    return Boxing.boxBoolean(false);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void shelfUpdateJob() {
        AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (appConfig.getShelfUpdateInteval() <= 0) {
            return;
        }
        Calendar now = Calendar.getInstance();
        int hour = now.get(11);
        int munite = now.get(12);
        int muniteFromToday = (hour * 60) + munite;
        AppConfig appConfig2 = this.appConfig;
        if (appConfig2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (muniteFromToday % appConfig2.getShelfUpdateInteval() != 0) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope) this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new C00091(null), 2, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$shelfUpdateJob$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$shelfUpdateJob$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "YueduApi.kt", l = {572, 576}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"$this$launch", "bookController"}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$shelfUpdateJob$1")
    static final class C00091 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$1;
        int label;
        private /* synthetic */ Object L$0;

        C00091(Continuation<? super C00091> $completion) {
            super(2, $completion);
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c00091 = new C00091($completion);
            c00091.L$0 = value;
            return c00091;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00bc  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            BookController bookController;
            CoroutineScope $this$launch;
            UserController userController;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$launch = (CoroutineScope) this.L$0;
                    bookController = new BookController($this$launch.getCoroutineContext());
                    YueduApiKt.logger.info("开始检查书架书籍更新");
                    this.L$0 = $this$launch;
                    this.L$1 = bookController;
                    this.label = 1;
                    if (bookController.getBookShelfBooks(true, "default", (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    userController = new UserController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.L$1 = null;
                    this.label = 2;
                    if (userController.forEachUser(new C00021(bookController, null), (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    YueduApiKt.logger.info("书架书籍更新检查结束");
                    return Unit.INSTANCE;
                case 1:
                    bookController = (BookController) this.L$1;
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    userController = new UserController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.L$1 = null;
                    this.label = 2;
                    if (userController.forEachUser(new C00021(bookController, null), (Continuation) this) == coroutine_suspended) {
                    }
                    YueduApiKt.logger.info("书架书籍更新检查结束");
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    YueduApiKt.logger.info("书架书籍更新检查结束");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$shelfUpdateJob$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: YueduApi.kt */
        /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$shelfUpdateJob$1$1.class */
        @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "user", "Lcom/htmake/reader/entity/User;"})
        @DebugMetadata(f = "YueduApi.kt", l = {579}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$shelfUpdateJob$1$1")
        static final class C00021 extends SuspendLambda implements Function3<CoroutineScope, User, Continuation<? super Boolean>, Object> {
            int label;
            /* synthetic */ Object L$0;
            final /* synthetic */ BookController $bookController;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00021(BookController $bookController, Continuation<? super C00021> $completion) {
                super(3, $completion);
                this.$bookController = $bookController;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super Boolean> p3) {
                C00021 c00021 = new C00021(this.$bookController, p3);
                c00021.L$0 = p2;
                return c00021.invokeSuspend(Unit.INSTANCE);
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        User user = (User) this.L$0;
                        if (user.getLast_login_at() >= System.currentTimeMillis() - 259200000) {
                            this.label = 1;
                            if (this.$bookController.getBookShelfBooks(true, user.getUsername(), (Continuation) this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Boxing.boxBoolean(false);
            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void remoteBookSourceSubUpdateJob() {
        AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (appConfig.getRemoteBookSourceUpdateInterval() <= 0) {
            return;
        }
        Calendar now = Calendar.getInstance();
        int hour = now.get(11);
        int munite = now.get(12);
        int muniteFromToday = (hour * 60) + munite;
        AppConfig appConfig2 = this.appConfig;
        if (appConfig2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (muniteFromToday % appConfig2.getRemoteBookSourceUpdateInterval() != 0) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope) this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new C00081(null), 2, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$remoteBookSourceSubUpdateJob$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$remoteBookSourceSubUpdateJob$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "YueduApi.kt", l = {614, 618}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"$this$launch", "bookSourceController"}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$remoteBookSourceSubUpdateJob$1")
    static final class C00081 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$1;
        int label;
        private /* synthetic */ Object L$0;

        C00081(Continuation<? super C00081> $completion) {
            super(2, $completion);
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c00081 = new C00081($completion);
            c00081.L$0 = value;
            return c00081;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00bc  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            BookSourceController bookSourceController;
            CoroutineScope $this$launch;
            UserController userController;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$launch = (CoroutineScope) this.L$0;
                    bookSourceController = new BookSourceController($this$launch.getCoroutineContext());
                    YueduApiKt.logger.info("开始检查远程书源更新");
                    this.L$0 = $this$launch;
                    this.L$1 = bookSourceController;
                    this.label = 1;
                    if (bookSourceController.updateRemoteSourceSub("default", null, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    userController = new UserController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.L$1 = null;
                    this.label = 2;
                    if (userController.forEachUser(new C00011(bookSourceController, null), (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    YueduApiKt.logger.info("远程书源更新检查结束");
                    return Unit.INSTANCE;
                case 1:
                    bookSourceController = (BookSourceController) this.L$1;
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    userController = new UserController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.L$1 = null;
                    this.label = 2;
                    if (userController.forEachUser(new C00011(bookSourceController, null), (Continuation) this) == coroutine_suspended) {
                    }
                    YueduApiKt.logger.info("远程书源更新检查结束");
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    YueduApiKt.logger.info("远程书源更新检查结束");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$remoteBookSourceSubUpdateJob$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: YueduApi.kt */
        /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$remoteBookSourceSubUpdateJob$1$1.class */
        @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "user", "Lcom/htmake/reader/entity/User;"})
        @DebugMetadata(f = "YueduApi.kt", l = {621}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$remoteBookSourceSubUpdateJob$1$1")
        static final class C00011 extends SuspendLambda implements Function3<CoroutineScope, User, Continuation<? super Boolean>, Object> {
            int label;
            /* synthetic */ Object L$0;
            final /* synthetic */ BookSourceController $bookSourceController;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00011(BookSourceController $bookSourceController, Continuation<? super C00011> $completion) {
                super(3, $completion);
                this.$bookSourceController = $bookSourceController;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super Boolean> p3) {
                C00011 c00011 = new C00011(this.$bookSourceController, p3);
                c00011.L$0 = p2;
                return c00011.invokeSuspend(Unit.INSTANCE);
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        User user = (User) this.L$0;
                        if (user.getLast_login_at() >= System.currentTimeMillis() - 259200000) {
                            this.label = 1;
                            if (this.$bookSourceController.updateRemoteSourceSub(user.getUsername(), user, (Continuation) this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Boxing.boxBoolean(false);
            }
        }
    }

    @Scheduled(cron = "0 59 23 * * ?")
    public void clearUser() {
        AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (appConfig.getAutoClearInactiveUser() > 0) {
            AppConfig appConfig2 = this.appConfig;
            if (appConfig2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                throw null;
            }
            if (!appConfig2.getSecure()) {
                return;
            }
            MDC.put("traceId", ExtKt.getTraceId());
            BuildersKt.launch$default((CoroutineScope) this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new C00051(null), 2, (Object) null);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$clearUser$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$clearUser$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "YueduApi.kt", l = {647}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$clearUser$1")
    static final class C00051 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ Object L$0;

        C00051(Continuation<? super C00051> $completion) {
            super(2, $completion);
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c00051 = YueduApi.this.new C00051($completion);
            c00051.L$0 = value;
            return c00051;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        CoroutineScope $this$launch = (CoroutineScope) this.L$0;
                        KLogger kLogger = YueduApiKt.logger;
                        AppConfig appConfig = YueduApi.this.appConfig;
                        if (appConfig == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                            throw null;
                        }
                        kLogger.info("开始清理 {} 天未登录用户", Boxing.boxInt(appConfig.getAutoClearInactiveUser()));
                        UserController userController = new UserController($this$launch.getCoroutineContext());
                        AppConfig appConfig2 = YueduApi.this.appConfig;
                        if (appConfig2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
                            throw null;
                        }
                        this.label = 1;
                        if (userController.clearInactiveUsers(appConfig2.getAutoClearInactiveUser(), (Continuation<? super Unit>) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                YueduApiKt.logger.info("不活跃用户自动清理结束");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Unit.INSTANCE;
        }
    }

    @Scheduled(cron = "0 50 23 * * ?")
    public void autoBackup() {
        AppConfig appConfig = this.appConfig;
        if (appConfig == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appConfig");
            throw null;
        }
        if (!appConfig.getAutoBackupUserData()) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope) this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new AnonymousClass1(null), 2, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$autoBackup$1, reason: invalid class name */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$autoBackup$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "YueduApi.kt", l = {672, 676}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"$this$launch", "bookController"}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$autoBackup$1")
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$1;
        int label;
        private /* synthetic */ Object L$0;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super(2, $completion);
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass1 = new AnonymousClass1($completion);
            anonymousClass1.L$0 = value;
            return anonymousClass1;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00be  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            BookController bookController;
            CoroutineScope $this$launch;
            UserController userController;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$launch = (CoroutineScope) this.L$0;
                    bookController = new BookController($this$launch.getCoroutineContext());
                    YueduApiKt.logger.info("开始备份用户数据");
                    this.L$0 = $this$launch;
                    this.L$1 = bookController;
                    this.label = 1;
                    if (BookController.saveToWebdav$default(bookController, "default", null, (Continuation) this, 2, null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    userController = new UserController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.L$1 = null;
                    this.label = 2;
                    if (userController.forEachUser(new C00001(bookController, null), (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    YueduApiKt.logger.info("备份用户数据结束");
                    return Unit.INSTANCE;
                case 1:
                    bookController = (BookController) this.L$1;
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    userController = new UserController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.L$1 = null;
                    this.label = 2;
                    if (userController.forEachUser(new C00001(bookController, null), (Continuation) this) == coroutine_suspended) {
                    }
                    YueduApiKt.logger.info("备份用户数据结束");
                    return Unit.INSTANCE;
                case 2:
                    ResultKt.throwOnFailure($result);
                    YueduApiKt.logger.info("备份用户数据结束");
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$autoBackup$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: YueduApi.kt */
        /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$autoBackup$1$1.class */
        @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "user", "Lcom/htmake/reader/entity/User;"})
        @DebugMetadata(f = "YueduApi.kt", l = {679}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$autoBackup$1$1")
        static final class C00001 extends SuspendLambda implements Function3<CoroutineScope, User, Continuation<? super Boolean>, Object> {
            int label;
            /* synthetic */ Object L$0;
            final /* synthetic */ BookController $bookController;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C00001(BookController $bookController, Continuation<? super C00001> $completion) {
                super(3, $completion);
                this.$bookController = $bookController;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super Boolean> p3) {
                C00001 c00001 = new C00001(this.$bookController, p3);
                c00001.L$0 = p2;
                return c00001.invokeSuspend(Unit.INSTANCE);
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        User user = (User) this.L$0;
                        if (user.getLast_login_at() >= System.currentTimeMillis() - 259200000) {
                            this.label = 1;
                            if (BookController.saveToWebdav$default(this.$bookController, user.getUsername(), null, (Continuation) this, 2, null) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        break;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return Boxing.boxBoolean(false);
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void autoGC() {
        System.gc();
    }

    @Scheduled(cron = "0 4/15 7-23 * * ?")
    public void checkLicense() throws IOException {
        License license = ExtKt.getInstalledLicense(true);
        if ("default".equals(license.getType())) {
            return;
        }
        MDC.put("traceId", ExtKt.getTraceId());
        BuildersKt.launch$default((CoroutineScope) this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new C00041(license, null), 2, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.YueduApi$checkLicense$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: YueduApi.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$checkLicense$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "YueduApi.kt", l = {713, 714, 718}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.YueduApi$checkLicense$1")
    static final class C00041 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ License $license;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00041(License $license, Continuation<? super C00041> $completion) {
            super(2, $completion);
            this.$license = $license;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c00041 = new C00041(this.$license, $completion);
            c00041.L$0 = value;
            return c00041;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x00b0  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x00f6  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            CoroutineScope $this$launch;
            LicenseController licenseController;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$launch = (CoroutineScope) this.L$0;
                    this.L$0 = $this$launch;
                    this.label = 1;
                    if (DelayKt.delay(((long) RangesKt.random(new IntRange(10, 120), Random.Default)) * 1000, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    this.L$0 = $this$launch;
                    this.label = 2;
                    if (DelayKt.delay(((long) RangesKt.random(new IntRange(1, 10), Random.Default)) * 1000, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    YueduApiKt.logger.info("开始检查授权是否正常");
                    licenseController = new LicenseController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.label = 3;
                    if (licenseController.checkLicense(this.$license, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Unit.INSTANCE;
                case 1:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    this.L$0 = $this$launch;
                    this.label = 2;
                    if (DelayKt.delay(((long) RangesKt.random(new IntRange(1, 10), Random.Default)) * 1000, (Continuation) this) == coroutine_suspended) {
                    }
                    YueduApiKt.logger.info("开始检查授权是否正常");
                    licenseController = new LicenseController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.label = 3;
                    if (licenseController.checkLicense(this.$license, (Continuation) this) == coroutine_suspended) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    YueduApiKt.logger.info("开始检查授权是否正常");
                    licenseController = new LicenseController($this$launch.getCoroutineContext());
                    this.L$0 = null;
                    this.label = 3;
                    if (licenseController.checkLicense(this.$license, (Continuation) this) == coroutine_suspended) {
                    }
                    return Unit.INSTANCE;
                case 3:
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }
}
