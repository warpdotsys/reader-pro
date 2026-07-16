// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import java.util.Map;
import io.legado.app.exception.TocEmptyException;
import io.legado.app.model.localBook.LocalBook;
import io.legado.app.data.entities.Book;
import io.vertx.core.json.JsonArray;
import io.legado.app.utils.FileUtils;
import java.nio.charset.Charset;
import io.vertx.core.http.HttpServerResponse;
import java.net.URLEncoder;
import com.htmake.reader.utils.VertExtKt;
import kotlin.Unit;
import java.util.Iterator;
import java.util.Set;
import kotlin.jvm.functions.Function2;
import kotlin.io.FilesKt;
import io.vertx.ext.web.FileUpload;
import kotlin.collections.MapsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.TuplesKt;
import kotlin.Pair;
import kotlin.text.StringsKt;
import java.util.ArrayList;
import java.io.File;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.entity.User;
import kotlin.collections.CollectionsKt;
import io.vertx.core.http.HttpMethod;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import com.htmake.reader.api.ReturnData;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J/\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\fJ\u0019\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eJ\u0019\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u001a" }, d2 = { "Lcom/htmake/reader/api/controller/FileController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "checkAccess", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "isSave", "", "isDelete", "(Lio/vertx/ext/web/RoutingContext;ZZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMulti", "download", "", "get", "importPreview", "list", "mkdir", "parse", "restore", "save", "upload", "reader-pro" })
public final class FileController extends BaseController
{
    public FileController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
    }
    
    @Nullable
    public final Object checkAccess(@NotNull RoutingContext context, boolean isSave, boolean isDelete, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof FileController$checkAccess.FileController$checkAccess$1) {
                final FileController$checkAccess.FileController$checkAccess$1 fileController$checkAccess$1 = (FileController$checkAccess.FileController$checkAccess$1)$completion;
                if ((fileController$checkAccess$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$checkAccess.FileController$checkAccess$1 fileController$checkAccess$2 = fileController$checkAccess$1;
                    fileController$checkAccess$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new FileController$checkAccess.FileController$checkAccess$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$checkAccess.FileController$checkAccess$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((FileController$checkAccess.FileController$checkAccess$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final FileController fileController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((FileController$checkAccess.FileController$checkAccess$1)$continuation).L$0 = this;
                ((FileController$checkAccess.FileController$checkAccess$1)$continuation).L$1 = context;
                ((FileController$checkAccess.FileController$checkAccess$1)$continuation).L$2 = returnData;
                ((FileController$checkAccess.FileController$checkAccess$1)$continuation).Z$0 = isSave;
                ((FileController$checkAccess.FileController$checkAccess$1)$continuation).Z$1 = isDelete;
                ((FileController$checkAccess.FileController$checkAccess$1)$continuation).label = 1;
                if ((checkAuth = fileController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                isDelete = ((FileController$checkAccess.FileController$checkAccess$1)$continuation).Z$1;
                isSave = ((FileController$checkAccess.FileController$checkAccess$1)$continuation).Z$0;
                returnData = (ReturnData)((FileController$checkAccess.FileController$checkAccess$1)$continuation).L$2;
                context = (RoutingContext)((FileController$checkAccess.FileController$checkAccess$1)$continuation).L$1;
                this = (FileController)((FileController$checkAccess.FileController$checkAccess$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        context.put("__FILE_HOME__", (Object)null);
        String home = null;
        if (context.request().method() == HttpMethod.POST) {
            if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
                final String string = context.getBodyAsJson().getString("home");
                final String s = (string == null) ? "" : string;
            }
            else {
                final String param = context.request().getParam("home");
                home = ((param == null) ? "" : param);
            }
        }
        else {
            final List queryParam = context.queryParam("home");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"home\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            home = ((s2 == null) ? "" : s2);
        }
        final String s3 = home;
        Label_0817: {
            switch (s3) {
                case "__WEBDAV__": {
                    if (this.getAppConfig().getSecure()) {
                        final User userInfo = (User)context.get("userInfo");
                        if (userInfo == null) {
                            return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                        }
                        if (!userInfo.getEnable_webdav()) {
                            return returnData.setErrorMsg("\u672a\u5f00\u542fwebdav\u529f\u80fd");
                        }
                    }
                    context.put("__FILE_HOME__", (Object)ExtKt.toDir$default(this.getUserWebdavHome(context), false, 1, null));
                    break Label_0817;
                }
                case "__LOCAL_STORE__": {
                    if (this.getAppConfig().getSecure()) {
                        final User userInfo = (User)context.get("userInfo");
                        if (userInfo == null) {
                            return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                        }
                        if (!userInfo.getEnable_local_store()) {
                            return returnData.setErrorMsg("\u672a\u5f00\u542f\u672c\u5730\u4e66\u4ed3\u529f\u80fd");
                        }
                    }
                    if ((isSave || isDelete) && !this.checkManagerAuth(context)) {
                        return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
                    }
                    context.put("__FILE_HOME__", (Object)ExtKt.toDir$default(ExtKt.getWorkDir("storage", "localStore"), false, 1, null));
                    break Label_0817;
                }
                case "__HOME__": {
                    final String userNameSpace = this.getUserNameSpace(context);
                    context.put("__FILE_HOME__", (Object)ExtKt.toDir$default(ExtKt.getWorkDir("storage", "data", userNameSpace), false, 1, null));
                    break Label_0817;
                }
                case "__STORAGE__": {
                    if (!this.checkManagerAuth(context)) {
                        return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
                    }
                    context.put("__FILE_HOME__", (Object)ExtKt.toDir$default(ExtKt.getWorkDir("storage"), false, 1, null));
                    break Label_0817;
                }
                default:
                    break;
            }
            return returnData.setErrorMsg("\u975e\u6cd5\u8bbf\u95ee");
        }
        FileControllerKt.access$getLogger$p().info("context.__FILE_HOME__ {}", context.get("__FILE_HOME__"));
        return null;
    }
    
    public static /* synthetic */ Object checkAccess$default(final FileController fileController, final RoutingContext context, boolean isSave, boolean isDelete, final Continuation $completion, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            isSave = false;
        }
        if ((n & 0x4) != 0x0) {
            isDelete = false;
        }
        return fileController.checkAccess(context, isSave, isDelete, (Continuation<? super ReturnData>)$completion);
    }
    
    @Nullable
    public final Object list(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$list.FileController$list$1) {
                final FileController$list.FileController$list$1 fileController$list$1 = (FileController$list.FileController$list$1)$completion;
                if ((fileController$list$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$list.FileController$list$1 fileController$list$2 = fileController$list$1;
                    fileController$list$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$list.FileController$list$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$list.FileController$list$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$list.FileController$list$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext routingContext = context;
                final boolean b = false;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((FileController$list.FileController$list$1)$continuation).L$0 = context;
                ((FileController$list.FileController$list$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(this, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$list.FileController$list$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        String path = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("path");
            final String s = (string == null) ? "" : string;
        }
        else {
            final List queryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            path = ((s2 == null) ? "" : s2);
        }
        if (path.length() == 0) {
            path = "/";
        }
        Object home = null;
        home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        path = ExtKt.toDir(path, true);
        final File file = new File(Intrinsics.stringPlus((String)home, (Object)path));
        FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
        if (!file.exists()) {
            if (!Intrinsics.areEqual((Object)path, (Object)"/")) {
                return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u5b58\u5728");
            }
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u662f\u76ee\u5f55");
        }
        Object fileList = null;
        fileList = new ArrayList();
        final File[] listFiles = file.listFiles();
        Intrinsics.checkNotNullExpressionValue((Object)listFiles, "file.listFiles()");
        final Object[] $this$forEach$iv = listFiles;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final File it = (File)element$iv;
            final int n2 = 0;
            final String name = it.getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object)null)) {
                final Object o2 = fileList;
                final Pair[] array3;
                final Pair[] array2 = array3 = new Pair[] { TuplesKt.to((Object)"name", (Object)it.getName()), TuplesKt.to((Object)"size", (Object)Boxing.boxLong(it.length())), null, null, null };
                final int n3 = 2;
                final String s3 = "path";
                final String string2 = it.toString();
                Intrinsics.checkNotNullExpressionValue((Object)string2, "it.toString()");
                array3[n3] = TuplesKt.to((Object)s3, (Object)StringsKt.replace$default(string2, (String)home, "", false, 4, (Object)null));
                array2[3] = TuplesKt.to((Object)"lastModified", (Object)Boxing.boxLong(it.lastModified()));
                array2[4] = TuplesKt.to((Object)"isDirectory", (Object)Boxing.boxBoolean(it.isDirectory()));
                ((ArrayList<Map>)o2).add(MapsKt.mapOf(array2));
            }
        }
        return ReturnData.setData$default(returnData, fileList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object upload(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$upload.FileController$upload$1) {
                final FileController$upload.FileController$upload$1 fileController$upload$1 = (FileController$upload.FileController$upload$1)$completion;
                if ((fileController$upload$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$upload.FileController$upload$1 fileController$upload$2 = fileController$upload$1;
                    fileController$upload$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$upload.FileController$upload$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$upload.FileController$upload$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        final ReturnData returnData2;
        switch (((FileController$upload.FileController$upload$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final ReturnData returnData = new ReturnData();
                if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
                    return returnData.setErrorMsg("\u8bf7\u4e0a\u4f20\u6587\u4ef6");
                }
                final boolean b = true;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 4;
                final Object o = null;
                ((FileController$upload.FileController$upload$1)$continuation).L$0 = context;
                ((FileController$upload.FileController$upload$1)$continuation).L$1 = returnData;
                ((FileController$upload.FileController$upload$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(this, context, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData2 = (ReturnData)((FileController$upload.FileController$upload$1)$continuation).L$1;
                context = (RoutingContext)((FileController$upload.FileController$upload$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        Object path = null;
        path = context.request().getParam("path");
        final CharSequence charSequence = (CharSequence)path;
        if (charSequence == null || charSequence.length() == 0) {
            path = "/";
        }
        final Object $this$toDir = path;
        Intrinsics.checkNotNullExpressionValue($this$toDir, "path");
        path = ExtKt.toDir((String)$this$toDir, true);
        Object fileList = null;
        fileList = new ArrayList();
        Object home = null;
        home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final Set fileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue((Object)fileUploads, "context.fileUploads()");
        final Iterable $this$forEach$iv = fileUploads;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final FileUpload it = (FileUpload)element$iv;
            final int n2 = 0;
            final File file = new File(it.uploadedFileName());
            FileControllerKt.access$getLogger$p().info("uploadFile: {} {} {}", new Object[] { it.uploadedFileName(), it.fileName(), file });
            if (file.exists()) {
                final String fileName = it.fileName();
                final File newFile = new File(new StringBuilder().append(home).append(path).append((Object)File.separator).append((Object)fileName).toString());
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                if (newFile.exists()) {
                    newFile.delete();
                }
                FileControllerKt.access$getLogger$p().info("moveTo: {}", (Object)newFile);
                if (FilesKt.copyRecursively$default(file, newFile, false, (Function2)null, 6, (Object)null)) {
                    final Object o2 = fileList;
                    final Pair[] array2;
                    final Pair[] array = array2 = new Pair[] { TuplesKt.to((Object)"name", (Object)newFile.getName()), TuplesKt.to((Object)"size", (Object)Boxing.boxLong(newFile.length())), null, null, null };
                    final int n3 = 2;
                    final String s = "path";
                    final String string = newFile.toString();
                    Intrinsics.checkNotNullExpressionValue((Object)string, "newFile.toString()");
                    array2[n3] = TuplesKt.to((Object)s, (Object)StringsKt.replace$default(string, (String)home, "", false, 4, (Object)null));
                    array[3] = TuplesKt.to((Object)"lastModified", (Object)Boxing.boxLong(newFile.lastModified()));
                    array[4] = TuplesKt.to((Object)"isDirectory", (Object)Boxing.boxBoolean(newFile.isDirectory()));
                    ((ArrayList<Map>)o2).add(MapsKt.mapOf(array));
                }
                FilesKt.deleteRecursively(file);
            }
        }
        return ReturnData.setData$default(returnData2, fileList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object download(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$download.FileController$download$1) {
                final FileController$download.FileController$download$1 fileController$download$1 = (FileController$download.FileController$download$1)$completion;
                if ((fileController$download$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$download.FileController$download$1 fileController$download$2 = fileController$download$1;
                    fileController$download$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$download.FileController$download$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$download.FileController$download$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$download.FileController$download$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext routingContext = context;
                final boolean b = false;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((FileController$download.FileController$download$1)$continuation).L$0 = context;
                ((FileController$download.FileController$download$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(this, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$download.FileController$download$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            VertExtKt.success(context, checkResult);
            return Unit.INSTANCE;
        }
        final ReturnData returnData = new ReturnData();
        String path;
        int stream = 0;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("path");
            path = ((string == null) ? "" : string);
            final Integer integer = context.getBodyAsJson().getInteger("stream", Boxing.boxInt(0));
            Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"stream\", 0)");
            integer.intValue();
        }
        else {
            final List queryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            path = ((s == null) ? "" : s);
            final List queryParam2 = context.queryParam("stream");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"stream\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
            int n2;
            if (s2 == null) {
                n2 = 0;
            }
            else {
                final Integer boxInt = Boxing.boxInt(Integer.parseInt(s2));
                n2 = ((boxInt == null) ? 0 : boxInt);
            }
            stream = n2;
        }
        if (path.length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef"));
            return Unit.INSTANCE;
        }
        final String home = (String)context.get("__FILE_HOME__");
        if (home == null) {
            VertExtKt.success(context, returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef"));
            return Unit.INSTANCE;
        }
        path = ExtKt.toDir(path, true);
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
        if (!file.exists()) {
            VertExtKt.success(context, returnData.setErrorMsg("\u8def\u5f84\u4e0d\u5b58\u5728"));
            return Unit.INSTANCE;
        }
        final HttpServerResponse response = context.response().putHeader("Cache-Control", "86400");
        if (stream <= 0) {
            response.putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", (Object)URLEncoder.encode(file.getName(), "UTF-8")));
        }
        response.sendFile(file.toString());
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object get(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$get.FileController$get$1) {
                final FileController$get.FileController$get$1 fileController$get$1 = (FileController$get.FileController$get$1)$completion;
                if ((fileController$get$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$get.FileController$get$1 fileController$get$2 = fileController$get$1;
                    fileController$get$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$get.FileController$get$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$get.FileController$get$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$get.FileController$get$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext routingContext = context;
                final boolean b = false;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((FileController$get.FileController$get$1)$continuation).L$0 = context;
                ((FileController$get.FileController$get$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(this, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$get.FileController$get$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        String path = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("path");
            final String s = (string == null) ? "" : string;
        }
        else {
            final List queryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            path = ((s2 == null) ? "" : s2);
        }
        if (path.length() == 0) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String home = (String)context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        path = ExtKt.toDir(path, true);
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
        if (!file.exists()) {
            return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u5b58\u5728");
        }
        return ReturnData.setData$default(returnData, (Object)FilesKt.readText$default(file, (Charset)null, 1, (Object)null), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object save(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$save.FileController$save$1) {
                final FileController$save.FileController$save$1 fileController$save$1 = (FileController$save.FileController$save$1)$completion;
                if ((fileController$save$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$save.FileController$save$1 fileController$save$2 = fileController$save$1;
                    fileController$save$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$save.FileController$save$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$save.FileController$save$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$save.FileController$save$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext routingContext = context;
                final boolean b = true;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 4;
                final Object o = null;
                ((FileController$save.FileController$save$1)$continuation).L$0 = context;
                ((FileController$save.FileController$save$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(this, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$save.FileController$save$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        final String string = context.getBodyAsJson().getString("path", "");
        String path = (string == null) ? "" : string;
        final String string2 = context.getBodyAsJson().getString("content", "");
        final String content = (string2 == null) ? "" : string2;
        if (path.length() == 0) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String home = (String)context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        path = ExtKt.toDir(path, true);
        final File file = FileUtils.INSTANCE.createFileWithReplace(Intrinsics.stringPlus(home, (Object)path));
        FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
        FilesKt.writeText$default(file, content, (Charset)null, 2, (Object)null);
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object mkdir(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$mkdir.FileController$mkdir$1) {
                final FileController$mkdir.FileController$mkdir$1 fileController$mkdir$1 = (FileController$mkdir.FileController$mkdir$1)$completion;
                if ((fileController$mkdir$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$mkdir.FileController$mkdir$1 fileController$mkdir$2 = fileController$mkdir$1;
                    fileController$mkdir$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$mkdir.FileController$mkdir$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$mkdir.FileController$mkdir$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$mkdir.FileController$mkdir$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext routingContext = context;
                final boolean b = true;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 4;
                final Object o = null;
                ((FileController$mkdir.FileController$mkdir$1)$continuation).L$0 = context;
                ((FileController$mkdir.FileController$mkdir$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(this, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$mkdir.FileController$mkdir$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        final String string = context.getBodyAsJson().getString("path", "");
        String path = (string == null) ? "" : string;
        if (path.length() == 0) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String string2 = context.getBodyAsJson().getString("name", "");
        final String name = (string2 == null) ? "" : string2;
        if (name.length() == 0 || StringsKt.startsWith$default(name, ".", false, 2, (Object)null)) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String home = (String)context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        path = ExtKt.toDir(path, true);
        final File file = new File((Object)home + path + (Object)File.separator + name);
        FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
        if (file.exists()) {
            return returnData.setErrorMsg("\u8def\u5f84\u5df2\u5b58\u5728");
        }
        file.mkdirs();
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object delete(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$delete.FileController$delete$1) {
                final FileController$delete.FileController$delete$1 fileController$delete$1 = (FileController$delete.FileController$delete$1)$completion;
                if ((fileController$delete$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$delete.FileController$delete$1 fileController$delete$2 = fileController$delete$1;
                    fileController$delete$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$delete.FileController$delete$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$delete.FileController$delete$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess = null;
        switch (((FileController$delete.FileController$delete$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext context2 = context;
                final boolean isSave = false;
                final boolean isDelete = true;
                final Continuation $completion2 = $continuation;
                ((FileController$delete.FileController$delete$1)$continuation).L$0 = context;
                ((FileController$delete.FileController$delete$1)$continuation).label = 1;
                if ((checkAccess = this.checkAccess(context2, isSave, isDelete, (Continuation<? super ReturnData>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$delete.FileController$delete$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        String path = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("path");
            final String s = (string == null) ? "" : string;
        }
        else {
            final List queryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            path = ((s2 == null) ? "" : s2);
        }
        if (path.length() == 0) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String home = (String)context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        path = ExtKt.toDir(path, true);
        final File file = new File(Intrinsics.stringPlus(home, (Object)path));
        FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
        if (!file.exists()) {
            return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u5b58\u5728");
        }
        FilesKt.deleteRecursively(file);
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object deleteMulti(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$deleteMulti.FileController$deleteMulti$1) {
                final FileController$deleteMulti.FileController$deleteMulti$1 fileController$deleteMulti$1 = (FileController$deleteMulti.FileController$deleteMulti$1)$completion;
                if ((fileController$deleteMulti$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$deleteMulti.FileController$deleteMulti$1 fileController$deleteMulti$2 = fileController$deleteMulti$1;
                    fileController$deleteMulti$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$deleteMulti.FileController$deleteMulti$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$deleteMulti.FileController$deleteMulti$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess = null;
        switch (((FileController$deleteMulti.FileController$deleteMulti$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final RoutingContext context2 = context;
                final boolean isSave = false;
                final boolean isDelete = true;
                final Continuation $completion2 = $continuation;
                ((FileController$deleteMulti.FileController$deleteMulti$1)$continuation).L$0 = context;
                ((FileController$deleteMulti.FileController$deleteMulti$1)$continuation).label = 1;
                if ((checkAccess = this.checkAccess(context2, isSave, isDelete, (Continuation<? super ReturnData>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$deleteMulti.FileController$deleteMulti$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        final JsonArray path = context.getBodyAsJson().getJsonArray("path");
        if (path == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        Object home = null;
        home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final Iterable $this$forEach$iv = (Iterable)path;
        final int $i$f$forEach = 0;
        for (final Object it : $this$forEach$iv) {
            final Object element$iv = it;
            final int n = 0;
            final String s = (String)it;
            final String filePath = (s == null) ? "" : s;
            if (filePath.length() > 0) {
                final File file = new File(Intrinsics.stringPlus((String)home, (Object)ExtKt.toDir(filePath, true)));
                FilesKt.deleteRecursively(file);
            }
        }
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object importPreview(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$importPreview.FileController$importPreview$1) {
                final FileController$importPreview.FileController$importPreview$1 fileController$importPreview$1 = (FileController$importPreview.FileController$importPreview$1)$completion;
                if ((fileController$importPreview$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$importPreview.FileController$importPreview$1 fileController$importPreview$2 = fileController$importPreview$1;
                    fileController$importPreview$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$importPreview.FileController$importPreview$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$importPreview.FileController$importPreview$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$importPreview.FileController$importPreview$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final FileController fileController = this;
                final RoutingContext routingContext = context;
                final boolean b = false;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((FileController$importPreview.FileController$importPreview$1)$continuation).L$0 = this;
                ((FileController$importPreview.FileController$importPreview$1)$continuation).L$1 = context;
                ((FileController$importPreview.FileController$importPreview$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(fileController, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$importPreview.FileController$importPreview$1)$continuation).L$1;
                this = (FileController)((FileController$importPreview.FileController$importPreview$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        final JsonArray paths = context.getBodyAsJson().getJsonArray("path");
        if (paths == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        Object home = null;
        home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        Object fileList = null;
        fileList = new ArrayList();
        Object userNameSpace = null;
        userNameSpace = this.getUserNameSpace(context);
        final Iterable $this$forEach$iv = (Iterable)paths;
        final int $i$f$forEach = 0;
        for (final Object it : $this$forEach$iv) {
            final Object element$iv = it;
            final int n2 = 0;
            final String s = (String)it;
            String path = (s == null) ? "" : s;
            if (path.length() > 0) {
                path = Intrinsics.stringPlus((String)home, (Object)path);
                final File file = new File(path);
                FileControllerKt.access$getLogger$p().info("localFile: {} {}", (Object)path, (Object)file);
                if (!file.exists() || file.isDirectory()) {
                    continue;
                }
                final String fileName = file.getName();
                final BaseController baseController = this;
                Intrinsics.checkNotNullExpressionValue((Object)fileName, "fileName");
                final String ext = BaseController.getFileExt$default(baseController, fileName, null, 2, null);
                if (!Intrinsics.areEqual((Object)ext, (Object)"txt") && !Intrinsics.areEqual((Object)ext, (Object)"epub") && !Intrinsics.areEqual((Object)ext, (Object)"umd") && !Intrinsics.areEqual((Object)ext, (Object)"cbz") && !Intrinsics.areEqual((Object)ext, (Object)"pdf")) {
                    return returnData.setErrorMsg("\u4e0d\u652f\u6301\u5bfc\u5165" + ext + "\u683c\u5f0f\u7684\u4e66\u7c4d\u6587\u4ef6");
                }
                final String workDir$default;
                String rootDir = workDir$default = ExtKt.getWorkDir$default(null, 1, null);
                final String separator = File.separator;
                Intrinsics.checkNotNullExpressionValue((Object)separator, "separator");
                if (!StringsKt.endsWith$default(workDir$default, separator, false, 2, (Object)null)) {
                    rootDir = Intrinsics.stringPlus(rootDir, (Object)File.separator);
                }
                String relativePath = path;
                FileControllerKt.access$getLogger$p().info("rootDir: {} path: {}", (Object)rootDir, (Object)path);
                if (StringsKt.startsWith$default(relativePath, rootDir, false, 2, (Object)null)) {
                    relativePath = StringsKt.replaceFirst$default(relativePath, rootDir, "", false, 4, (Object)null);
                }
                FileControllerKt.access$getLogger$p().info("relative path: {}", (Object)relativePath);
                final String url = StringsKt.replace$default(relativePath, "\\", "/", false, 4, (Object)null);
                final Book book = Book.Companion.initLocalBook(url, relativePath, rootDir);
                book.setUserNameSpace((String)userNameSpace);
                FileControllerKt.access$getLogger$p().info("book {}", (Object)book);
                try {
                    final ArrayList chapters = LocalBook.INSTANCE.getChapterList(book);
                    ((ArrayList<Map>)fileList).add(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"book", (Object)book), TuplesKt.to((Object)"chapters", (Object)chapters) }));
                }
                catch (final TocEmptyException ex) {
                    ((ArrayList<Map>)fileList).add(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"book", (Object)book), TuplesKt.to((Object)"chapters", (Object)new ArrayList()) }));
                }
            }
        }
        return ReturnData.setData$default(returnData, fileList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object restore(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$restore.FileController$restore$1) {
                final FileController$restore.FileController$restore$1 fileController$restore$1 = (FileController$restore.FileController$restore$1)$completion;
                if ((fileController$restore$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$restore.FileController$restore$1 fileController$restore$2 = fileController$restore$1;
                    fileController$restore$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$restore.FileController$restore$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$restore.FileController$restore$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object syncFromWebdav = null;
        Label_0503: {
            Object checkAccess$default = null;
            switch (((FileController$restore.FileController$restore$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final FileController fileController = this;
                    final RoutingContext routingContext = context;
                    final boolean b = false;
                    final boolean b2 = false;
                    final Continuation continuation = $continuation;
                    final int n = 6;
                    final Object o = null;
                    ((FileController$restore.FileController$restore$1)$continuation).L$0 = this;
                    ((FileController$restore.FileController$restore$1)$continuation).L$1 = context;
                    ((FileController$restore.FileController$restore$1)$continuation).label = 1;
                    if ((checkAccess$default = checkAccess$default(fileController, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    context = (RoutingContext)((FileController$restore.FileController$restore$1)$continuation).L$1;
                    this = (FileController)((FileController$restore.FileController$restore$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAccess$default = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((FileController$restore.FileController$restore$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    syncFromWebdav = $result;
                    break Label_0503;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            final ReturnData checkResult = (ReturnData)checkAccess$default;
            if (checkResult != null) {
                return checkResult;
            }
            final ReturnData returnData = new ReturnData();
            String path = null;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("path");
                final String s = (string == null) ? "" : string;
            }
            else {
                final List queryParam = context.queryParam("path");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
                path = ((s2 == null) ? "" : s2);
            }
            if (path.length() == 0) {
                path = "/";
            }
            final String ext = BaseController.getFileExt$default(this, path, null, 2, null);
            if (!Intrinsics.areEqual((Object)ext, (Object)"zip")) {
                return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u662fzip\u5907\u4efd\u6587\u4ef6");
            }
            final String home = (String)context.get("__FILE_HOME__");
            if (home == null) {
                return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
            }
            final File file = new File(Intrinsics.stringPlus(home, (Object)path));
            FileControllerKt.access$getLogger$p().info("file: {} {}", (Object)path, (Object)file);
            if (!file.exists()) {
                return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u5b58\u5728");
            }
            final BookController bookController2;
            final BookController bookController = bookController2 = new BookController(this.getCoroutineContext());
            final String string2 = file.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string2, "file.toString()");
            final String zipFilePath = string2;
            final String userNameSpace = this.getUserNameSpace(context);
            final Continuation $completion2 = $continuation;
            ((FileController$restore.FileController$restore$1)$continuation).L$0 = returnData;
            ((FileController$restore.FileController$restore$1)$continuation).L$1 = null;
            ((FileController$restore.FileController$restore$1)$continuation).label = 2;
            if ((syncFromWebdav = bookController2.syncFromWebdav(zipFilePath, userNameSpace, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)syncFromWebdav) {
            return returnData2.setErrorMsg("\u6062\u590d\u5931\u8d25");
        }
        return ReturnData.setData$default(returnData2, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object parse(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof FileController$parse.FileController$parse$1) {
                final FileController$parse.FileController$parse$1 fileController$parse$1 = (FileController$parse.FileController$parse$1)$completion;
                if ((fileController$parse$1.label & Integer.MIN_VALUE) != 0x0) {
                    final FileController$parse.FileController$parse$1 fileController$parse$2 = fileController$parse$1;
                    fileController$parse$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new FileController$parse.FileController$parse$1(this, (Continuation)$completion);
        }
        final Object $result = ((FileController$parse.FileController$parse$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object checkAccess$default = null;
        switch (((FileController$parse.FileController$parse$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final FileController fileController = this;
                final RoutingContext routingContext = context;
                final boolean b = false;
                final boolean b2 = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((FileController$parse.FileController$parse$1)$continuation).L$0 = this;
                ((FileController$parse.FileController$parse$1)$continuation).L$1 = context;
                ((FileController$parse.FileController$parse$1)$continuation).label = 1;
                if ((checkAccess$default = checkAccess$default(fileController, routingContext, b, b2, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                context = (RoutingContext)((FileController$parse.FileController$parse$1)$continuation).L$1;
                this = (FileController)((FileController$parse.FileController$parse$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAccess$default = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final ReturnData checkResult = (ReturnData)checkAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        final ReturnData returnData = new ReturnData();
        Object home = null;
        home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        Object path = null;
        int import1 = 0;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("path");
            path = ((string == null) ? "" : string);
            final Integer integer = context.getBodyAsJson().getInteger("import", Boxing.boxInt(0));
            Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"import\", 0)");
            import1 = integer.intValue();
        }
        else {
            final List queryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            path = ((s == null) ? "" : s);
            final List queryParam2 = context.queryParam("import");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"import\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
            int n2;
            if (s2 == null) {
                n2 = 0;
            }
            else {
                final Integer boxInt = Boxing.boxInt(Integer.parseInt(s2));
                n2 = ((boxInt == null) ? 0 : boxInt);
            }
            import1 = n2;
        }
        if (((CharSequence)path).length() == 0) {
            path = "/";
        }
        final File file = new File(Intrinsics.stringPlus((String)home, path));
        FileControllerKt.access$getLogger$p().info("file: {} {}", path, (Object)file);
        if (!file.exists()) {
            return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u5b58\u5728");
        }
        if (!file.isDirectory()) {
            return returnData.setErrorMsg("\u8def\u5f84\u4e0d\u662f\u76ee\u5f55");
        }
        Object fileList = null;
        fileList = new ArrayList();
        Object userNameSpace = null;
        userNameSpace = this.getUserNameSpace(context);
        Object rootDir = null;
        final String workDir$default;
        rootDir = (workDir$default = ExtKt.getWorkDir$default(null, 1, null));
        final String separator = File.separator;
        Intrinsics.checkNotNullExpressionValue((Object)separator, "separator");
        if (!StringsKt.endsWith$default(workDir$default, separator, false, 2, (Object)null)) {
            rootDir = Intrinsics.stringPlus((String)rootDir, (Object)File.separator);
        }
        final BookController bookController = new BookController(this.getCoroutineContext());
        final Iterable $this$forEach$iv = ExtKt.listFilesRecursively(file);
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final File it = (File)element$iv;
            final int n3 = 0;
            final String name = it.getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object)null) && it.isFile()) {
                final String fileName = it.getName();
                final BaseController baseController = this;
                Intrinsics.checkNotNullExpressionValue((Object)fileName, "fileName");
                final String ext = BaseController.getFileExt$default(baseController, fileName, null, 2, null);
                switch (ext.hashCode()) {
                    case 115312: {
                        if (!ext.equals("txt")) {
                            continue;
                        }
                        break;
                    }
                    case 110834: {
                        if (!ext.equals("pdf")) {
                            continue;
                        }
                        break;
                    }
                    case 3120248: {
                        if (!ext.equals("epub")) {
                            continue;
                        }
                        break;
                    }
                    case 98299: {
                        if (!ext.equals("cbz")) {
                            continue;
                        }
                        break;
                    }
                    case 115916: {
                        if (!ext.equals("umd")) {
                            continue;
                        }
                        break;
                    }
                }
                String relativePath = it.getPath();
                FileControllerKt.access$getLogger$p().debug("rootDir: {} path: {}", rootDir, path);
                final String s3 = relativePath;
                Intrinsics.checkNotNullExpressionValue((Object)s3, "relativePath");
                if (StringsKt.startsWith$default(s3, (String)rootDir, false, 2, (Object)null)) {
                    final String s4 = relativePath;
                    Intrinsics.checkNotNullExpressionValue((Object)s4, "relativePath");
                    relativePath = StringsKt.replaceFirst$default(s4, (String)rootDir, "", false, 4, (Object)null);
                }
                FileControllerKt.access$getLogger$p().debug("relative path: {}", (Object)relativePath);
                final String s5 = relativePath;
                Intrinsics.checkNotNullExpressionValue((Object)s5, "relativePath");
                final String url = StringsKt.replace$default(s5, "\\", "/", false, 4, (Object)null);
                final Book.Companion companion = Book.Companion;
                final String bookUrl = url;
                final String localPath = relativePath;
                Intrinsics.checkNotNullExpressionValue((Object)localPath, "relativePath");
                final Book book = companion.initLocalBook(bookUrl, localPath, (String)rootDir);
                book.setUserNameSpace((String)userNameSpace);
                FileControllerKt.access$getLogger$p().debug("book {}", (Object)book);
                if (import1 > 0) {
                    final Pair result = bookController.saveBookToShelf(book, (String)userNameSpace, context);
                    if (result.getSecond() != null || !((Book)result.getFirst()).isInShelf()) {
                        continue;
                    }
                    ((ArrayList<Map>)fileList).add(MapsKt.mapOf(TuplesKt.to((Object)"name", (Object)it.getName())));
                }
                else {
                    final Object o2 = fileList;
                    final Pair[] array2;
                    final Pair[] array = array2 = new Pair[] { TuplesKt.to((Object)"name", (Object)it.getName()), TuplesKt.to((Object)"size", (Object)Boxing.boxLong(it.length())), null, null, null };
                    final int n4 = 2;
                    final String s6 = "path";
                    final String string2 = it.toString();
                    Intrinsics.checkNotNullExpressionValue((Object)string2, "it.toString()");
                    array2[n4] = TuplesKt.to((Object)s6, (Object)StringsKt.replaceFirst$default(string2, (String)home, "", false, 4, (Object)null));
                    array[3] = TuplesKt.to((Object)"lastModified", (Object)Boxing.boxLong(it.lastModified()));
                    array[4] = TuplesKt.to((Object)"book", (Object)book);
                    ((ArrayList<Map>)o2).add(MapsKt.mapOf(array));
                }
            }
        }
        return ReturnData.setData$default(returnData, fileList, (String)null, 2, (Object)null);
    }
}
