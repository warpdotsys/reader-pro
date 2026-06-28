package com.htmake.reader.api.controller;

import com.google.gson.reflect.TypeToken;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.VertExtKt;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.utils.EncoderUtils;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.slf4j.MDCContext;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: WebdavController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/WebdavController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u001c\u0010\u0006\u001a\u0018\u0012\u0004\u0012\u00020\b\u0012\b\u0012\u00060\tj\u0002`\n\u0012\u0004\u0012\u00020\u000b0\u0007¢\u0006\u0002\u0010\fJ\u0019\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\bJ\u0019\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0019\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001c"}, d2 = {"Lcom/htmake/reader/api/controller/WebdavController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "router", "Lio/vertx/ext/web/Router;", "onHandlerError", "Lkotlin/Function2;", "Lio/vertx/ext/web/RoutingContext;", "Ljava/lang/Exception;", "Lkotlin/Exception;", PackageDocumentBase.PREFIX_OPF, "(Lkotlin/coroutines/CoroutineContext;Lio/vertx/ext/web/Router;Lkotlin/jvm/functions/Function2;)V", "backupToWebdav", "Lcom/htmake/reader/api/ReturnData;", "context", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkAuthorization", PackageDocumentBase.PREFIX_OPF, "webdavCopy", "webdavDelete", "webdavDownload", "webdavList", "webdavLock", "webdavMkdir", "webdavMove", "webdavUnLock", "webdavUpload", "reader-pro"})
public final class WebdavController extends BaseController {

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.WebdavController$backupToWebdav$1, reason: invalid class name */
    /* JADX INFO: compiled from: WebdavController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/WebdavController$backupToWebdav$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebdavController.kt", l = {518, 533, 537}, i = {0, 0, 0, 1, 1}, s = {"L$0", "L$1", "L$2", "L$1", "L$2"}, n = {"this", "context", "returnData", "bookController", "userNameSpace"}, m = "backupToWebdav", c = "com.htmake.reader.api.controller.WebdavController")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebdavController.this.backupToWebdav(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.WebdavController$webdavUpload$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: WebdavController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/WebdavController$webdavUpload$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "WebdavController.kt", l = {357}, i = {}, s = {}, n = {}, m = "webdavUpload", c = "com.htmake.reader.api.controller.WebdavController")
    static final class C01441 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01441(Continuation<? super C01441> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return WebdavController.this.webdavUpload(null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WebdavController(@NotNull CoroutineContext coroutineContext, @NotNull Router router, @NotNull Function2<? super RoutingContext, ? super Exception, Unit> onHandlerError) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
        Intrinsics.checkNotNullParameter(router, "router");
        Intrinsics.checkNotNullParameter(onHandlerError, "onHandlerError");
        Route route = router.route("/reader3/webdav*");
        Intrinsics.checkNotNullExpressionValue(route, "router.route(\"/reader3/webdav*\")");
        VertExtKt.globalHandler(route, (v2) -> {
            m67_init_$lambda1(r1, r2, v2);
        });
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX INFO: renamed from: _init_$lambda-1, reason: not valid java name */
    private static final void m67_init_$lambda1(WebdavController this$0, Function2 $onHandlerError, RoutingContext it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter($onHandlerError, "$onHandlerError");
        it.addHeadersEndHandler((v2) -> {
            m66lambda1$lambda0(r1, r2, v2);
        });
        String rawMethod = it.request().rawMethod();
        Intrinsics.checkNotNullExpressionValue(it, "it");
        if (!this$0.checkAuthorization(it)) {
            if (rawMethod.equals("PROPFIND") || rawMethod.equals("MKCOL") || rawMethod.equals("PUT") || rawMethod.equals("GET") || rawMethod.equals("DELETE") || rawMethod.equals("MOVE") || rawMethod.equals("COPY") || rawMethod.equals("LOCK") || rawMethod.equals("UNLOCK")) {
                it.response().setStatusCode(401).end();
                return;
            } else if (rawMethod.equals("OPTIONS")) {
                String authorization = it.request().getHeader("Authorization");
                if (authorization != null) {
                    it.response().setStatusCode(401).end();
                    return;
                }
            }
        }
        if (rawMethod != null) {
            switch (rawMethod.hashCode()) {
                case -1787112636:
                    if (rawMethod.equals("UNLOCK")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$10(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case -531492226:
                    if (rawMethod.equals("OPTIONS")) {
                        it.response().setStatusCode(200).end();
                        return;
                    }
                    break;
                case -210493540:
                    if (rawMethod.equals("PROPFIND")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$2(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 70454:
                    if (rawMethod.equals("GET")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$5(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 79599:
                    if (rawMethod.equals("PUT")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$4(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 2074485:
                    if (rawMethod.equals("COPY")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$8(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 2342187:
                    if (rawMethod.equals("LOCK")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$9(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 2372561:
                    if (rawMethod.equals("MOVE")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$7(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 73412354:
                    if (rawMethod.equals("MKCOL")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$3(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
                case 2012838315:
                    if (rawMethod.equals("DELETE")) {
                        BuildersKt.launch$default(this$0, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, new WebdavController$1$6(this$0, it, $onHandlerError, null), 2, (Object) null);
                        return;
                    }
                    break;
            }
        }
        it.response().setStatusCode(405).end();
    }

    /* JADX INFO: renamed from: lambda-1$lambda-0, reason: not valid java name */
    private static final void m66lambda1$lambda0(RoutingContext $it, WebdavController this$0, Void $noName_0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        HttpServerResponse res = $it.response();
        res.putHeader("DAV", "1,2");
        res.putHeader("Access-Control-Allow-Origin", "*");
        res.putHeader("Access-Control-Allow-Credentials", "true");
        res.putHeader("Access-Control-Expose-Headers", "DAV, content-length, Allow");
        res.putHeader("MS-Author-Via", "DAV");
        res.putHeader("Allow", "OPTIONS,DELETE,GET,PUT,PROPFIND,MKCOL,MOVE,COPY,LOCK,UNLOCK");
        if (this$0.getAppConfig().getSecure()) {
            res.putHeader("WWW-Authenticate", "Basic realm=\"Default realm\"");
        }
    }

    /* JADX WARN: Type inference failed for: r2v9, types: [com.htmake.reader.api.controller.WebdavController$checkAuthorization$$inlined$toDataClass$1] */
    public final boolean checkAuthorization(@NotNull RoutingContext context) {
        String json;
        Intrinsics.checkNotNullParameter(context, "context");
        if (!getAppConfig().getSecure()) {
            return true;
        }
        String authorization = context.request().getHeader("Authorization");
        WebdavControllerKt.logger.info("authorization: {}", authorization);
        if (authorization != null) {
            if (authorization.length() == 0) {
                return false;
            }
            List auth = StringsKt.split$default(EncoderUtils.base64Decode$default(EncoderUtils.INSTANCE, StringsKt.replace(authorization, "Basic ", PackageDocumentBase.PREFIX_OPF, true), 0, 2, null), new String[]{":"}, false, 2, 2, (Object) null);
            if (auth.size() < 2) {
                return false;
            }
            String username = (String) auth.get(0);
            String password = (String) auth.get(1);
            Map userMap = new LinkedHashMap();
            JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
            if (userMapJson != null) {
                Map map = userMapJson.getMap();
                if (map == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                }
                userMap = TypeIntrinsics.asMutableMap(map);
            }
            Map map2 = userMap;
            if (map2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
            }
            Object obj = (Map) map2.getOrDefault(username, null);
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof String)) {
                json = ExtKt.getGson().toJson(obj);
            } else {
                json = (String) obj;
            }
            String json$iv$iv = json;
            User userInfo = (User) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() { // from class: com.htmake.reader.api.controller.WebdavController$checkAuthorization$$inlined$toDataClass$1
            }.getType());
            if (userInfo == null) {
                return false;
            }
            String passwordEncrypted = ExtKt.genEncryptedPassword(password, userInfo.getSalt());
            if (!Intrinsics.areEqual(passwordEncrypted, userInfo.getPassword())) {
                WebdavControllerKt.logger.info("user: {} password error", userInfo.getUsername());
                return false;
            }
            if (!userInfo.getEnable_webdav()) {
                WebdavControllerKt.logger.info("user: {} enable_webdav: false", userInfo.getUsername());
                return false;
            }
            context.put("username", userInfo.getUsername());
            return true;
        }
        return false;
    }

    @Nullable
    public final Object webdavList(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        String home = getUserWebdavHome(context);
        String strPath = context.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        File file = new File(Intrinsics.stringPlus(home, path));
        if (!file.exists()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        final Ref.ObjectRef dirResponse = new Ref.ObjectRef();
        dirResponse.element = "<D:response>\n                <D:href>%s</D:href>\n                <D:propstat>\n                    <D:status>HTTP/1.1 200 OK</D:status>\n                    <D:prop>\n                        <D:getlastmodified>%s</D:getlastmodified>\n                        <D:creationdate>%s</D:creationdate>\n                        <D:resourcetype>\n                            <D:collection />\n                        </D:resourcetype>\n                        <D:displayname>%s</D:displayname>\n                    </D:prop>\n                </D:propstat>\n            </D:response>\n        ";
        final Ref.ObjectRef fileResponse = new Ref.ObjectRef();
        fileResponse.element = "<D:response>\n                <D:href>%s</D:href>\n                <D:propstat>\n                    <D:status>HTTP/1.1 200 OK</D:status>\n                    <D:prop>\n                        <D:getlastmodified>%s</D:getlastmodified>\n                        <D:creationdate>%s</D:creationdate>\n                        <D:resourcetype />\n                        <D:displayname>%s</D:displayname>\n                        <D:getcontentlength>%s</D:getcontentlength>\n                        <D:getcontenttype>%s</D:getcontenttype>\n                    </D:prop>\n                </D:propstat>\n            </D:response>\n        ";
        String strAbsoluteURI = context.request().absoluteURI();
        Object formatter = new Function3<File, String, Boolean, String>() { // from class: com.htmake.reader.api.controller.WebdavController$webdavList$formatter$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(3);
            }

            public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
                return invoke((File) p1, (String) p2, ((Boolean) p3).booleanValue());
            }

            @NotNull
            public final String invoke(@NotNull File f, @NotNull String url, boolean showName) {
                Intrinsics.checkNotNullParameter(f, "f");
                Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
                String name = showName ? f.getName() : PackageDocumentBase.PREFIX_OPF;
                String modifiedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(f.lastModified()));
                if (f.isFile()) {
                    StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                    String str = (String) fileResponse.element;
                    Object[] objArr = {url, modifiedDate, modifiedDate, name, Long.valueOf(f.length()), PackageDocumentBase.PREFIX_OPF};
                    String str2 = String.format(str, Arrays.copyOf(objArr, objArr.length));
                    Intrinsics.checkNotNullExpressionValue(str2, "java.lang.String.format(format, *args)");
                    return str2;
                }
                StringCompanionObject stringCompanionObject2 = StringCompanionObject.INSTANCE;
                Object[] objArr2 = {url, modifiedDate, modifiedDate, name};
                String str3 = String.format((String) dirResponse.element, Arrays.copyOf(objArr2, objArr2.length));
                Intrinsics.checkNotNullExpressionValue(str3, "java.lang.String.format(format, *args)");
                return str3;
            }
        };
        if (file.isFile()) {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(strAbsoluteURI, "fileUrl");
            Object[] objArr = {((Function3) formatter).invoke(file, strAbsoluteURI, Boxing.boxBoolean(true))};
            String str = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n            <D:multistatus xmlns:D=\"DAV:\">\n                %s\n            </D:multistatus>\n        ", Arrays.copyOf(objArr, objArr.length));
            Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
            context.response().setStatusCode(207).end(str);
            return Unit.INSTANCE;
        }
        if (file.isDirectory()) {
            Intrinsics.checkNotNullExpressionValue(strAbsoluteURI, "fileUrl");
            String strStringPlus = StringsKt.endsWith$default(strAbsoluteURI, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null) ? strAbsoluteURI : Intrinsics.stringPlus(strAbsoluteURI, TableOfContents.DEFAULT_PATH_SEPARATOR);
            Intrinsics.checkNotNullExpressionValue(strStringPlus, "fileUrl");
            Object response = ((Function3) formatter).invoke(file, strStringPlus, Boxing.boxBoolean(false));
            Object[] objArrListFiles = file.listFiles();
            Intrinsics.checkNotNullExpressionValue(objArrListFiles, "file.listFiles()");
            Object[] $this$forEach$iv = objArrListFiles;
            for (Object element$iv : $this$forEach$iv) {
                File it = (File) element$iv;
                String fileName = URLEncoder.encode(it.getName(), Constants.CHARACTER_ENCODING);
                Intrinsics.checkNotNullExpressionValue(it, "it");
                response = Intrinsics.stringPlus((String) response, ((Function3) formatter).invoke(it, Intrinsics.stringPlus(strStringPlus, fileName), Boxing.boxBoolean(true)));
            }
            StringCompanionObject stringCompanionObject2 = StringCompanionObject.INSTANCE;
            Object[] objArr2 = {response};
            String str2 = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n            <D:multistatus xmlns:D=\"DAV:\">\n                %s\n            </D:multistatus>\n        ", Arrays.copyOf(objArr2, objArr2.length));
            Intrinsics.checkNotNullExpressionValue(str2, "java.lang.String.format(format, *args)");
            context.response().setStatusCode(207).end(str2);
            return Unit.INSTANCE;
        }
        context.response().setStatusCode(404).end();
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavMkdir(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        String home = getUserWebdavHome(context);
        String strPath = context.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        File file = new File(Intrinsics.stringPlus(home, path));
        if (file.exists()) {
            context.response().setStatusCode(201).end();
            return Unit.INSTANCE;
        }
        try {
            file.mkdirs();
            context.response().setStatusCode(201).end();
        } catch (Exception e) {
            context.response().setStatusCode(500).end();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object webdavUpload(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        C01441 c01441;
        if ($completion instanceof C01441) {
            c01441 = (C01441) $completion;
            if ((c01441.label & Integer.MIN_VALUE) != 0) {
                c01441.label -= Integer.MIN_VALUE;
            } else {
                c01441 = new C01441($completion);
            }
        }
        Object $result = c01441.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c01441.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    String home = getUserWebdavHome(context);
                    String strPath = context.request().path();
                    Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
                    String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
                    Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
                    File file = new File(Intrinsics.stringPlus(home, path));
                    if (!file.getParentFile().exists()) {
                        context.response().setStatusCode(409).end();
                        return Unit.INSTANCE;
                    }
                    if (file.isDirectory()) {
                        context.response().setStatusCode(405).end();
                        return Unit.INSTANCE;
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                    byte[] bytes = context.getBody().getBytes();
                    Intrinsics.checkNotNullExpressionValue(bytes, "context.getBody().getBytes()");
                    FilesKt.writeBytes(file, bytes);
                    String string = file.toString();
                    Intrinsics.checkNotNullExpressionValue(string, "file.toString()");
                    if (StringsKt.indexOf$default(string, "bookProgress", 0, false, 6, (Object) null) > 0) {
                        String string2 = file.toString();
                        Intrinsics.checkNotNullExpressionValue(string2, "file.toString()");
                        if (StringsKt.indexOf$default(string2, ".json", 0, false, 6, (Object) null) > 0) {
                            String userNameSpace = getUserNameSpace(context);
                            c01441.L$0 = context;
                            c01441.label = 1;
                            if (new BookController(getCoroutineContext()).syncBookProgressFromWebdav(file, userNameSpace, c01441) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    }
                    break;
                case 1:
                    context = (RoutingContext) c01441.L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            context.response().setStatusCode(201).end();
        } catch (Exception e) {
            context.response().setStatusCode(500).end();
        }
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavDownload(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        String home = getUserWebdavHome(context);
        String strPath = context.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        File file = new File(Intrinsics.stringPlus(home, path));
        if (!file.exists()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        if (file.isDirectory()) {
            context.response().setStatusCode(405).end();
            return Unit.INSTANCE;
        }
        HttpServerResponse httpServerResponseSendFile = context.response().putHeader("Cache-Control", "86400").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(file.getName(), Constants.CHARACTER_ENCODING))).sendFile(file.toString());
        return httpServerResponseSendFile == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? httpServerResponseSendFile : Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavDelete(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        String home = getUserWebdavHome(context);
        String strPath = context.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        File file = new File(Intrinsics.stringPlus(home, path));
        if (!file.exists()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        ExtKt.deleteRecursively(file);
        context.response().setStatusCode(200).end();
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavMove(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        String home = getUserWebdavHome(context);
        String strPath = context.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        File file = new File(Intrinsics.stringPlus(home, path));
        if (!file.exists()) {
            context.response().setStatusCode(412).end();
            return Unit.INSTANCE;
        }
        String destination = context.request().getHeader("Destination");
        if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        URL destinationUrl = new URL(destination);
        String path2 = destinationUrl.getPath();
        String destination2 = path2 == null ? null : StringsKt.replace(path2, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true);
        if (destination2 == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        String overwrite = context.request().getHeader("Overwrite");
        File destinationFile = new File(Intrinsics.stringPlus(home, URLDecoder.decode(destination2, Constants.CHARACTER_ENCODING)));
        if (destinationFile.exists()) {
            if (overwrite != null) {
                if (!(overwrite.length() == 0)) {
                    ExtKt.deleteRecursively(destinationFile);
                }
            }
            context.response().setStatusCode(412).end();
            return Unit.INSTANCE;
        }
        file.renameTo(destinationFile);
        context.response().setStatusCode(201).end();
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavCopy(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws UnsupportedEncodingException {
        String home = getUserWebdavHome(context);
        String strPath = context.request().path();
        Intrinsics.checkNotNullExpressionValue(strPath, "context.request().path()");
        String path = URLDecoder.decode(StringsKt.replace(strPath, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true), Constants.CHARACTER_ENCODING);
        Intrinsics.checkNotNullExpressionValue(path, "decode(path, \"UTF-8\")");
        File file = new File(Intrinsics.stringPlus(home, path));
        if (!file.exists()) {
            context.response().setStatusCode(412).end();
            return Unit.INSTANCE;
        }
        String destination = context.request().getHeader("Destination");
        if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        URL destinationUrl = new URL(destination);
        String path2 = destinationUrl.getPath();
        String destination2 = path2 == null ? null : StringsKt.replace(path2, "/reader3/webdav/", TableOfContents.DEFAULT_PATH_SEPARATOR, true);
        if (destination2 == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        String overwrite = context.request().getHeader("Overwrite");
        File destinationFile = new File(Intrinsics.stringPlus(home, URLDecoder.decode(destination2, Constants.CHARACTER_ENCODING)));
        if (destinationFile.exists()) {
            if (overwrite != null) {
                if (!(overwrite.length() == 0)) {
                    ExtKt.deleteRecursively(destinationFile);
                }
            }
            context.response().setStatusCode(412).end();
            return Unit.INSTANCE;
        }
        FilesKt.copyRecursively$default(file, destinationFile, false, (Function2) null, 6, (Object) null);
        context.response().setStatusCode(201).end();
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavLock(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        UUID uuidRandomUUID = UUID.randomUUID();
        Intrinsics.checkNotNullExpressionValue(uuidRandomUUID, "randomUUID()");
        String lockToken = Intrinsics.stringPlus("urn:uuid:", uuidRandomUUID);
        String timeout = context.request().getHeader("Timeout");
        if (timeout == null) {
            timeout = "Second-3600";
        }
        String fileUrl = context.request().absoluteURI();
        HttpServerResponse statusCode = context.response().putHeader("Lock-Token", lockToken).setStatusCode(200);
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Object[] objArr = {lockToken, fileUrl, timeout};
        String str = String.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n        <D:prop xmlns:D=\"DAV:\">\n            <D:lockdiscovery>\n                <D:activelock>\n                    <D:locktype>\n                        <write />\n                    </D:locktype>\n                    <D:lockscope>\n                        <exclusive />\n                    </D:lockscope>\n                    <D:locktoken>\n                        <D:href>%s</D:href>\n                    </D:locktoken>\n                    <D:lockroot>\n                        <D:href>%s</D:href>\n                    </D:lockroot>\n                    <D:depth>infinity</D:depth>\n                    <D:owner>\n                        <a:href xmlns:a=\"DAV:\">http://www.apple.com/webdav_fs/</a:href>\n                    </D:owner>\n                    <D:timeout>%s</D:timeout>\n                </D:activelock>\n            </D:lockdiscovery>\n        </D:prop>\n        ", Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
        statusCode.end(str);
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object webdavUnLock(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        String lockToken = context.request().getHeader("Lock-Token");
        if (lockToken == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        context.response().putHeader("Lock-Token", lockToken).setStatusCode(204).end();
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01a5  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object backupToWebdav(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        AnonymousClass1 anonymousClass1;
        ReturnData returnData;
        Object objSaveToWebdav;
        String userNameSpace;
        BookController bookController;
        Object lastBackFileFromWebdav;
        Object objCheckAuth;
        if ($completion instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) $completion;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1($completion);
            }
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                anonymousClass1.L$0 = this;
                anonymousClass1.L$1 = context;
                anonymousClass1.L$2 = returnData;
                anonymousClass1.label = 1;
                objCheckAuth = checkAuth(context, anonymousClass1);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (this.getAppConfig().getSecure()) {
                    User userInfo = (User) context.get("userInfo");
                    if (userInfo == null) {
                        return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                    }
                    if (!userInfo.getEnable_webdav()) {
                        return returnData.setErrorMsg("未开启webdav功能");
                    }
                }
                bookController = new BookController(this.getCoroutineContext());
                userNameSpace = this.getUserNameSpace(context);
                anonymousClass1.L$0 = returnData;
                anonymousClass1.L$1 = bookController;
                anonymousClass1.L$2 = userNameSpace;
                anonymousClass1.label = 2;
                lastBackFileFromWebdav = bookController.getLastBackFileFromWebdav(userNameSpace, anonymousClass1);
                if (lastBackFileFromWebdav == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String latestZipFilePath = (String) lastBackFileFromWebdav;
                anonymousClass1.L$0 = returnData;
                anonymousClass1.L$1 = null;
                anonymousClass1.L$2 = null;
                anonymousClass1.label = 3;
                objSaveToWebdav = bookController.saveToWebdav(userNameSpace, latestZipFilePath, anonymousClass1);
                if (objSaveToWebdav == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return !((Boolean) objSaveToWebdav).booleanValue() ? returnData.setErrorMsg("备份失败") : ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (WebdavController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                userNameSpace = (String) anonymousClass1.L$2;
                bookController = (BookController) anonymousClass1.L$1;
                returnData = (ReturnData) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                lastBackFileFromWebdav = $result;
                String latestZipFilePath2 = (String) lastBackFileFromWebdav;
                anonymousClass1.L$0 = returnData;
                anonymousClass1.L$1 = null;
                anonymousClass1.L$2 = null;
                anonymousClass1.label = 3;
                objSaveToWebdav = bookController.saveToWebdav(userNameSpace, latestZipFilePath2, anonymousClass1);
                if (objSaveToWebdav == coroutine_suspended) {
                }
                if (!((Boolean) objSaveToWebdav).booleanValue()) {
                }
                break;
            case 3:
                returnData = (ReturnData) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objSaveToWebdav = $result;
                if (!((Boolean) objSaveToWebdav).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
