// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.slf4j.MDCContext;
import kotlinx.coroutines.CoroutineScope;
import com.htmake.reader.api.ReturnData;
import java.util.UUID;
import java.net.URL;
import io.vertx.core.http.HttpServerResponse;
import kotlin.io.FilesKt;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.Nullable;
import java.net.URLEncoder;
import java.util.Arrays;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.Ref$ObjectRef;
import java.io.File;
import java.net.URLDecoder;
import kotlin.coroutines.Continuation;
import io.vertx.core.json.JsonObject;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import com.htmake.reader.entity.User;
import kotlin.jvm.internal.TypeIntrinsics;
import com.htmake.reader.utils.ExtKt;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.text.StringsKt;
import io.legado.app.utils.EncoderUtils;
import io.vertx.ext.web.Route;
import io.vertx.core.Handler;
import com.htmake.reader.utils.VertExtKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import io.vertx.ext.web.RoutingContext;
import kotlin.jvm.functions.Function2;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u001c\u0010\u0006\u001a\u0018\u0012\u0004\u0012\u00020\b\u0012\b\u0012\u00060\tj\u0002`\n\u0012\u0004\u0012\u00020\u000b0\u0007?\u0006\u0002\u0010\fJ\u0019\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\bJ\u0019\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0019\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u001c" }, d2 = { "Lcom/htmake/reader/api/controller/WebdavController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "router", "Lio/vertx/ext/web/Router;", "onHandlerError", "Lkotlin/Function2;", "Lio/vertx/ext/web/RoutingContext;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "", "(Lkotlin/coroutines/CoroutineContext;Lio/vertx/ext/web/Router;Lkotlin/jvm/functions/Function2;)V", "backupToWebdav", "Lcom/htmake/reader/api/ReturnData;", "context", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkAuthorization", "", "webdavCopy", "webdavDelete", "webdavDownload", "webdavList", "webdavLock", "webdavMkdir", "webdavMove", "webdavUnLock", "webdavUpload", "reader-pro" })
public final class WebdavController extends BaseController
{
    public WebdavController(@NotNull final CoroutineContext coroutineContext, @NotNull final Router router, @NotNull final Function2<? super RoutingContext, ? super Exception, Unit> onHandlerError) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        Intrinsics.checkNotNullParameter((Object)router, "router");
        Intrinsics.checkNotNullParameter((Object)onHandlerError, "onHandlerError");
        super(coroutineContext);
        final Route route = router.route("/reader3/webdav*");
        Intrinsics.checkNotNullExpressionValue((Object)route, "router.route(\"/reader3/webdav*\")");
        VertExtKt.globalHandler(route, (Handler<RoutingContext>)WebdavController::_init_$lambda-1);
    }
    
    public final boolean checkAuthorization(@NotNull final RoutingContext context) {
        Intrinsics.checkNotNullParameter((Object)context, "context");
        if (!this.getAppConfig().getSecure()) {
            return true;
        }
        final String authorization = context.request().getHeader("Authorization");
        WebdavControllerKt.access$getLogger$p().info("authorization: {}", (Object)authorization);
        if (authorization == null || authorization.length() == 0) {
            return false;
        }
        final List auth = StringsKt.split$default((CharSequence)EncoderUtils.base64Decode$default(EncoderUtils.INSTANCE, StringsKt.replace(authorization, "Basic ", "", true), 0, 2, null), new String[] { ":" }, false, 2, 2, (Object)null);
        if (auth.size() < 2) {
            return false;
        }
        final String username = auth.get(0);
        final String password = auth.get(1);
        Map userMap = new LinkedHashMap();
        final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
        if (userMapJson != null) {
            final Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
            }
            userMap = TypeIntrinsics.asMutableMap((Object)map);
        }
        final Map map2 = userMap;
        final Map defaultValue = null;
        final Map map3 = map2;
        if (map3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        final Map existedUser = map3.getOrDefault(username, defaultValue);
        if (existedUser == null) {
            return false;
        }
        final Map $this$toDataClass$iv = existedUser;
        final int $i$f$toDataClass = 0;
        final Object $this$convert$iv$iv = $this$toDataClass$iv;
        final int $i$f$convert = 0;
        final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
        final User userInfo = (User)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() {}.getType());
        if (userInfo == null) {
            return false;
        }
        final String passwordEncrypted = ExtKt.genEncryptedPassword(password, userInfo.getSalt());
        if (!Intrinsics.areEqual((Object)passwordEncrypted, (Object)userInfo.getPassword())) {
            WebdavControllerKt.access$getLogger$p().info("user: {} password error", (Object)userInfo.getUsername());
            return false;
        }
        if (!userInfo.getEnable_webdav()) {
            WebdavControllerKt.access$getLogger$p().info("user: {} enable_webdav: false", (Object)userInfo.getUsername());
            return false;
        }
        context.put("username", (Object)userInfo.getUsername());
        return true;
    }
    
    @Nullable
    public final Object webdavList(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String home = this.getUserWebdavHome(context);
        final String path2 = context.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
        String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        if (!file.exists()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n            <D:multistatus xmlns:D=\"DAV:\">\n                %s\n            </D:multistatus>\n        ";
        final Ref$ObjectRef dirResponse = new Ref$ObjectRef();
        dirResponse.element = "<D:response>\n                <D:href>%s</D:href>\n                <D:propstat>\n                    <D:status>HTTP/1.1 200 OK</D:status>\n                    <D:prop>\n                        <D:getlastmodified>%s</D:getlastmodified>\n                        <D:creationdate>%s</D:creationdate>\n                        <D:resourcetype>\n                            <D:collection />\n                        </D:resourcetype>\n                        <D:displayname>%s</D:displayname>\n                    </D:prop>\n                </D:propstat>\n            </D:response>\n        ";
        final Ref$ObjectRef fileResponse = new Ref$ObjectRef();
        fileResponse.element = "<D:response>\n                <D:href>%s</D:href>\n                <D:propstat>\n                    <D:status>HTTP/1.1 200 OK</D:status>\n                    <D:prop>\n                        <D:getlastmodified>%s</D:getlastmodified>\n                        <D:creationdate>%s</D:creationdate>\n                        <D:resourcetype />\n                        <D:displayname>%s</D:displayname>\n                        <D:getcontentlength>%s</D:getcontentlength>\n                        <D:getcontenttype>%s</D:getcontenttype>\n                    </D:prop>\n                </D:propstat>\n            </D:response>\n        ";
        Object fileUrl = null;
        fileUrl = context.request().absoluteURI();
        Object formatter = null;
        formatter = new WebdavController$webdavList$formatter.WebdavController$webdavList$formatter$1(fileResponse, dirResponse);
        Object response = null;
        response = "";
        if (file.isFile()) {
            final StringCompanionObject instance = StringCompanionObject.INSTANCE;
            final Object[] array = { null };
            final int n = 0;
            final Function3 function3 = (Function3)formatter;
            final File file2 = file;
            final Object o = fileUrl;
            Intrinsics.checkNotNullExpressionValue(o, "fileUrl");
            array[n] = function3.invoke((Object)file2, o, (Object)Boxing.boxBoolean(true));
            final Object[] array2 = array;
            final String format = xml;
            final Object[] original = array2;
            final String format2 = String.format(format, Arrays.copyOf(original, original.length));
            Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
            response = format2;
            context.response().setStatusCode(207).end((String)response);
            return Unit.INSTANCE;
        }
        if (file.isDirectory()) {
            final Object o2 = fileUrl;
            Intrinsics.checkNotNullExpressionValue(o2, "fileUrl");
            fileUrl = (StringsKt.endsWith$default((String)o2, "/", false, 2, (Object)null) ? fileUrl : Intrinsics.stringPlus((String)fileUrl, (Object)"/"));
            final Function3 function4 = (Function3)formatter;
            final File file3 = file;
            final Object o3 = fileUrl;
            Intrinsics.checkNotNullExpressionValue(o3, "fileUrl");
            response = function4.invoke((Object)file3, o3, (Object)Boxing.boxBoolean(false));
            final File[] listFiles = file.listFiles();
            Intrinsics.checkNotNullExpressionValue((Object)listFiles, "file.listFiles()");
            final Object[] $this$forEach$iv = listFiles;
            final int $i$f$forEach = 0;
            for (final Object element$iv : $this$forEach$iv) {
                final File it = (File)element$iv;
                final int n2 = 0;
                final String fileName = URLEncoder.encode(it.getName(), "UTF-8");
                final String s = (String)response;
                final Function3 function5 = (Function3)formatter;
                Intrinsics.checkNotNullExpressionValue((Object)it, "it");
                response = Intrinsics.stringPlus(s, function5.invoke((Object)it, (Object)Intrinsics.stringPlus((String)fileUrl, (Object)fileName), (Object)Boxing.boxBoolean(true)));
            }
            final StringCompanionObject instance2 = StringCompanionObject.INSTANCE;
            final Object[] array4 = { response };
            final String format3 = xml;
            final Object[] original2 = array4;
            final String format4 = String.format(format3, Arrays.copyOf(original2, original2.length));
            Intrinsics.checkNotNullExpressionValue((Object)format4, "java.lang.String.format(format, *args)");
            response = format4;
            context.response().setStatusCode(207).end((String)response);
            return Unit.INSTANCE;
        }
        context.response().setStatusCode(404).end();
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavMkdir(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String home = this.getUserWebdavHome(context);
        final String path2 = context.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
        String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        if (file.exists()) {
            context.response().setStatusCode(201).end();
            return Unit.INSTANCE;
        }
        try {
            file.mkdirs();
            context.response().setStatusCode(201).end();
        }
        catch (final Exception e) {
            context.response().setStatusCode(500).end();
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavUpload(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof WebdavController$webdavUpload.WebdavController$webdavUpload$1) {
                final WebdavController$webdavUpload.WebdavController$webdavUpload$1 webdavController$webdavUpload$1 = (WebdavController$webdavUpload.WebdavController$webdavUpload$1)$completion;
                if ((webdavController$webdavUpload$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebdavController$webdavUpload.WebdavController$webdavUpload$1 webdavController$webdavUpload$2 = webdavController$webdavUpload$1;
                    webdavController$webdavUpload$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new WebdavController$webdavUpload.WebdavController$webdavUpload$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebdavController$webdavUpload.WebdavController$webdavUpload$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0254: {
            switch (((WebdavController$webdavUpload.WebdavController$webdavUpload$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final String home = this.getUserWebdavHome(context);
                    final String path2 = context.request().path();
                    Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
                    String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
                    final String decode = URLDecoder.decode(path, "UTF-8");
                    Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
                    path = decode;
                    final File file = new File(Intrinsics.stringPlus(home, (Object)path));
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
                    break Label_0254;
                }
                case 1: {
                    Label_0399: {
                        break Label_0399;
                        try {
                            final File file;
                            final File file2 = file;
                            final byte[] bytes = context.getBody().getBytes();
                            Intrinsics.checkNotNullExpressionValue((Object)bytes, "context.getBody().getBytes()");
                            FilesKt.writeBytes(file2, bytes);
                            final String string = file.toString();
                            Intrinsics.checkNotNullExpressionValue((Object)string, "file.toString()");
                            while (true) {
                                if (StringsKt.indexOf$default((CharSequence)string, "bookProgress", 0, false, 6, (Object)null) > 0) {
                                    final String string2 = file.toString();
                                    Intrinsics.checkNotNullExpressionValue((Object)string2, "file.toString()");
                                    if (StringsKt.indexOf$default((CharSequence)string2, ".json", 0, false, 6, (Object)null) > 0) {
                                        final String userNameSpace = this.getUserNameSpace(context);
                                        final BookController bookController = new BookController(this.getCoroutineContext());
                                        final File progressFilePath = file;
                                        final String userNameSpace2 = userNameSpace;
                                        final Continuation $completion2 = $continuation;
                                        ((WebdavController$webdavUpload.WebdavController$webdavUpload$1)$continuation).L$0 = context;
                                        ((WebdavController$webdavUpload.WebdavController$webdavUpload$1)$continuation).label = 1;
                                        if (bookController.syncBookProgressFromWebdav(progressFilePath, userNameSpace2, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                                            return coroutine_SUSPENDED;
                                        }
                                    }
                                }
                                context.response().setStatusCode(201).end();
                                return Unit.INSTANCE;
                                context = (RoutingContext)((WebdavController$webdavUpload.WebdavController$webdavUpload$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                continue;
                            }
                        }
                        catch (final Exception ex) {
                            context.response().setStatusCode(500).end();
                        }
                    }
                    return Unit.INSTANCE;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }
    
    @Nullable
    public final Object webdavDownload(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String home = this.getUserWebdavHome(context);
        final String path2 = context.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
        String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        if (!file.exists()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        if (file.isDirectory()) {
            context.response().setStatusCode(405).end();
            return Unit.INSTANCE;
        }
        final HttpServerResponse sendFile = context.response().putHeader("Cache-Control", "86400").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", (Object)URLEncoder.encode(file.getName(), "UTF-8"))).sendFile(file.toString());
        if (sendFile == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return sendFile;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavDelete(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String home = this.getUserWebdavHome(context);
        final String path2 = context.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
        String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        if (!file.exists()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        ExtKt.deleteRecursively(file);
        context.response().setStatusCode(200).end();
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavMove(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String home = this.getUserWebdavHome(context);
        final String path2 = context.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
        String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        if (!file.exists()) {
            context.response().setStatusCode(412).end();
            return Unit.INSTANCE;
        }
        String destination = context.request().getHeader("Destination");
        if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        final URL destinationUrl = new URL(destination);
        final String path3 = destinationUrl.getPath();
        destination = ((path3 == null) ? null : StringsKt.replace(path3, "/reader3/webdav/", "/", true));
        if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        final String overwrite = context.request().getHeader("Overwrite");
        final File destinationFile = new File(Intrinsics.stringPlus(home, (Object)URLDecoder.decode(destination, "UTF-8")));
        if (destinationFile.exists()) {
            if (overwrite == null || overwrite.length() == 0) {
                context.response().setStatusCode(412).end();
                return Unit.INSTANCE;
            }
            ExtKt.deleteRecursively(destinationFile);
        }
        file.renameTo(destinationFile);
        context.response().setStatusCode(201).end();
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavCopy(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String home = this.getUserWebdavHome(context);
        final String path2 = context.request().path();
        Intrinsics.checkNotNullExpressionValue((Object)path2, "context.request().path()");
        String path = StringsKt.replace(path2, "/reader3/webdav/", "/", true);
        final String decode = URLDecoder.decode(path, "UTF-8");
        Intrinsics.checkNotNullExpressionValue((Object)decode, "decode(path, \"UTF-8\")");
        path = decode;
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        if (!file.exists()) {
            context.response().setStatusCode(412).end();
            return Unit.INSTANCE;
        }
        String destination = context.request().getHeader("Destination");
        if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        final URL destinationUrl = new URL(destination);
        final String path3 = destinationUrl.getPath();
        destination = ((path3 == null) ? null : StringsKt.replace(path3, "/reader3/webdav/", "/", true));
        if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        final String overwrite = context.request().getHeader("Overwrite");
        final File destinationFile = new File(Intrinsics.stringPlus(home, (Object)URLDecoder.decode(destination, "UTF-8")));
        if (destinationFile.exists()) {
            if (overwrite == null || overwrite.length() == 0) {
                context.response().setStatusCode(412).end();
                return Unit.INSTANCE;
            }
            ExtKt.deleteRecursively(destinationFile);
        }
        FilesKt.copyRecursively$default(file, destinationFile, false, (Function2)null, 6, (Object)null);
        context.response().setStatusCode(201).end();
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavLock(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String response = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n        <D:prop xmlns:D=\"DAV:\">\n            <D:lockdiscovery>\n                <D:activelock>\n                    <D:locktype>\n                        <write />\n                    </D:locktype>\n                    <D:lockscope>\n                        <exclusive />\n                    </D:lockscope>\n                    <D:locktoken>\n                        <D:href>%s</D:href>\n                    </D:locktoken>\n                    <D:lockroot>\n                        <D:href>%s</D:href>\n                    </D:lockroot>\n                    <D:depth>infinity</D:depth>\n                    <D:owner>\n                        <a:href xmlns:a=\"DAV:\">http://www.apple.com/webdav_fs/</a:href>\n                    </D:owner>\n                    <D:timeout>%s</D:timeout>\n                </D:activelock>\n            </D:lockdiscovery>\n        </D:prop>\n        ";
        final String s = "urn:uuid:";
        final UUID randomUUID = UUID.randomUUID();
        Intrinsics.checkNotNullExpressionValue((Object)randomUUID, "randomUUID()");
        final String lockToken = Intrinsics.stringPlus(s, (Object)randomUUID);
        String timeout = context.request().getHeader("Timeout");
        if (timeout == null) {
            timeout = "Second-3600";
        }
        final String fileUrl = context.request().absoluteURI();
        final HttpServerResponse setStatusCode = context.response().putHeader("Lock-Token", lockToken).setStatusCode(200);
        final StringCompanionObject instance = StringCompanionObject.INSTANCE;
        final Object[] array = { lockToken, fileUrl, timeout };
        final String format = response;
        final Object[] original = array;
        final String format2 = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
        setStatusCode.end(format2);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object webdavUnLock(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final String lockToken = context.request().getHeader("Lock-Token");
        if (lockToken == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
        }
        context.response().putHeader("Lock-Token", lockToken).setStatusCode(204).end();
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object backupToWebdav(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0052: {
            if ($completion instanceof WebdavController$backupToWebdav.WebdavController$backupToWebdav$1) {
                final WebdavController$backupToWebdav.WebdavController$backupToWebdav$1 webdavController$backupToWebdav$1 = (WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$completion;
                if ((webdavController$backupToWebdav$1.label & Integer.MIN_VALUE) != 0x0) {
                    final WebdavController$backupToWebdav.WebdavController$backupToWebdav$1 webdavController$backupToWebdav$2 = webdavController$backupToWebdav$1;
                    webdavController$backupToWebdav$2.label -= Integer.MIN_VALUE;
                    break Label_0052;
                }
            }
            $continuation = (Continuation)new WebdavController$backupToWebdav.WebdavController$backupToWebdav$1(this, (Continuation)$completion);
        }
        final Object $result = ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData l$0 = null;
        Object saveToWebdav = null;
        Label_0440: {
            String userNameSpace = null;
            BookController bookController = null;
            Object lastBackFileFromWebdav = null;
            Label_0375: {
                ReturnData returnData = null;
                Object checkAuth = null;
                switch (((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final WebdavController webdavController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$0 = this;
                        ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$1 = context;
                        ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$2 = returnData;
                        ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).label = 1;
                        if ((checkAuth = webdavController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$2;
                        context = (RoutingContext)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$1;
                        this = (WebdavController)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        checkAuth = $result;
                        break;
                    }
                    case 2: {
                        userNameSpace = (String)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$2;
                        bookController = (BookController)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$1;
                        l$0 = (ReturnData)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        lastBackFileFromWebdav = $result;
                        break Label_0375;
                    }
                    case 3: {
                        l$0 = (ReturnData)((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        saveToWebdav = $result;
                        break Label_0440;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                if (!(boolean)checkAuth) {
                    return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                }
                if (this.getAppConfig().getSecure()) {
                    final User userInfo = (User)context.get("userInfo");
                    if (userInfo == null) {
                        return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                    }
                    if (!userInfo.getEnable_webdav()) {
                        return returnData.setErrorMsg("\u672a\u5f00\u542fwebdav\u529f\u80fd");
                    }
                }
                bookController = new BookController(this.getCoroutineContext());
                userNameSpace = this.getUserNameSpace(context);
                final BookController bookController2 = bookController;
                final String userNameSpace2 = userNameSpace;
                final Continuation $completion3 = $continuation;
                ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$0 = returnData;
                ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$1 = bookController;
                ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$2 = userNameSpace;
                ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).label = 2;
                if ((lastBackFileFromWebdav = bookController2.getLastBackFileFromWebdav(userNameSpace2, (Continuation<? super String>)$completion3)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            final String latestZipFilePath = (String)lastBackFileFromWebdav;
            final BookController bookController3 = bookController;
            final String userNameSpace3 = userNameSpace;
            final String latestZipFilePath2 = latestZipFilePath;
            final Continuation $completion4 = $continuation;
            ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$0 = l$0;
            ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$1 = null;
            ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).L$2 = null;
            ((WebdavController$backupToWebdav.WebdavController$backupToWebdav$1)$continuation).label = 3;
            if ((saveToWebdav = bookController3.saveToWebdav(userNameSpace3, latestZipFilePath2, (Continuation<? super Boolean>)$completion4)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)saveToWebdav) {
            return l$0.setErrorMsg("\u5907\u4efd\u5931\u8d25");
        }
        return ReturnData.setData$default(l$0, (Object)"", (String)null, 2, (Object)null);
    }
    
    private static final void lambda-1$lambda-0(final RoutingContext $it, final WebdavController this$0, final Void $noName_0) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        final HttpServerResponse res = $it.response();
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
    
    private static final void _init_$lambda-1(final WebdavController this$0, final Function2 $onHandlerError, final RoutingContext it) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        Intrinsics.checkNotNullParameter((Object)$onHandlerError, "$onHandlerError");
        it.addHeadersEndHandler(WebdavController::lambda-1$lambda-0);
        final String rawMethod = it.request().rawMethod();
        Intrinsics.checkNotNullExpressionValue((Object)it, "it");
        if (!this$0.checkAuthorization(it)) {
            if (rawMethod.equals("PROPFIND") || rawMethod.equals("MKCOL") || rawMethod.equals("PUT") || rawMethod.equals("GET") || rawMethod.equals("DELETE") || rawMethod.equals("MOVE") || rawMethod.equals("COPY") || rawMethod.equals("LOCK") || rawMethod.equals("UNLOCK")) {
                it.response().setStatusCode(401).end();
                return;
            }
            if (rawMethod.equals("OPTIONS")) {
                final String authorization = it.request().getHeader("Authorization");
                if (authorization != null) {
                    it.response().setStatusCode(401).end();
                    return;
                }
            }
        }
        final String s = rawMethod;
        if (s != null) {
            switch (s) {
                case "MKCOL": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$3(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "DELETE": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$6(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "MOVE": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$7(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "GET": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$5(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "PROPFIND": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$2(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "LOCK": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$9(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "COPY": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$8(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "OPTIONS": {
                    it.response().setStatusCode(200).end();
                    return;
                }
                case "PUT": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$4(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                case "UNLOCK": {
                    BuildersKt.launch$default((CoroutineScope)this$0, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new WebdavController$1.WebdavController$1$10(this$0, it, $onHandlerError, (Continuation)null), 2, (Object)null);
                    return;
                }
                default:
                    break;
            }
        }
        it.response().setStatusCode(405).end();
    }
}
