package com.htmake.reader.api.controller;

import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.VertExtKt;
import io.legado.app.data.entities.Book;
import io.legado.app.exception.TocEmptyException;
import io.legado.app.model.localBook.LocalBook;
import io.legado.app.utils.FileUtils;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: FileController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J/\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\fJ\u0019\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u0019\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000e\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001a"}, d2 = {"Lcom/htmake/reader/api/controller/FileController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "checkAccess", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "isSave", PackageDocumentBase.PREFIX_OPF, "isDelete", "(Lio/vertx/ext/web/RoutingContext;ZZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMulti", "download", PackageDocumentBase.PREFIX_OPF, "get", "importPreview", "list", "mkdir", "parse", "restore", "save", "upload", "reader-pro"})
public final class FileController extends BaseController {

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$checkAccess$1, reason: invalid class name */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$checkAccess$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {26}, i = {0, 0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "Z$0", "Z$1"}, n = {"this", "context", "returnData", "isSave", "isDelete"}, m = "checkAccess", c = "com.htmake.reader.api.controller.FileController")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        boolean Z$0;
        boolean Z$1;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.checkAccess(null, false, false, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$delete$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$delete$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {311}, i = {0}, s = {"L$0"}, n = {"context"}, m = "delete", c = "com.htmake.reader.api.controller.FileController")
    static final class C01131 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01131(Continuation<? super C01131> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.delete(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$deleteMulti$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$deleteMulti$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {342}, i = {0}, s = {"L$0"}, n = {"context"}, m = "deleteMulti", c = "com.htmake.reader.api.controller.FileController")
    static final class C01141 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01141(Continuation<? super C01141> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.deleteMulti(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$download$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$download$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {188}, i = {0}, s = {"L$0"}, n = {"context"}, m = "download", c = "com.htmake.reader.api.controller.FileController")
    static final class C01151 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01151(Continuation<? super C01151> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.download(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$get$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$get$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {229}, i = {0}, s = {"L$0"}, n = {"context"}, m = "get", c = "com.htmake.reader.api.controller.FileController")
    static final class C01161 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01161(Continuation<? super C01161> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.get(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$importPreview$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$importPreview$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {366}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "context"}, m = "importPreview", c = "com.htmake.reader.api.controller.FileController")
    static final class C01171 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        C01171(Continuation<? super C01171> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.importPreview(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$list$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$list$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {91}, i = {0}, s = {"L$0"}, n = {"context"}, m = "list", c = "com.htmake.reader.api.controller.FileController")
    static final class C01181 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01181(Continuation<? super C01181> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.list(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$mkdir$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$mkdir$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {282}, i = {0}, s = {"L$0"}, n = {"context"}, m = "mkdir", c = "com.htmake.reader.api.controller.FileController")
    static final class C01191 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01191(Continuation<? super C01191> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.mkdir(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$parse$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$parse$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {457}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "context"}, m = "parse", c = "com.htmake.reader.api.controller.FileController")
    static final class C01201 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        C01201(Continuation<? super C01201> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.parse(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$restore$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$restore$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {420, 450}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"this", "context"}, m = "restore", c = "com.htmake.reader.api.controller.FileController")
    static final class C01211 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        C01211(Continuation<? super C01211> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.restore(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$save$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$save$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {259}, i = {0}, s = {"L$0"}, n = {"context"}, m = "save", c = "com.htmake.reader.api.controller.FileController")
    static final class C01221 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01221(Continuation<? super C01221> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.save(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.FileController$upload$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: FileController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/FileController$upload$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "FileController.kt", l = {143}, i = {}, s = {}, n = {}, m = "upload", c = "com.htmake.reader.api.controller.FileController")
    static final class C01231 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        C01231(Continuation<? super C01231> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return FileController.this.upload(null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FileController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object checkAccess(@NotNull RoutingContext context, boolean isSave, boolean isDelete, @NotNull Continuation<? super ReturnData> $completion) {
        AnonymousClass1 anonymousClass1;
        ReturnData returnData;
        Object objCheckAuth;
        String home;
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
                anonymousClass1.Z$0 = isSave;
                anonymousClass1.Z$1 = isDelete;
                anonymousClass1.label = 1;
                objCheckAuth = checkAuth(context, anonymousClass1);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                isDelete = anonymousClass1.Z$1;
                isSave = anonymousClass1.Z$0;
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (FileController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        context.put("__FILE_HOME__", (Object) null);
        if (context.request().method() == HttpMethod.POST) {
            if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
                String string = context.getBodyAsJson().getString("home");
                home = string == null ? PackageDocumentBase.PREFIX_OPF : string;
            } else {
                String param = context.request().getParam("home");
                home = param == null ? PackageDocumentBase.PREFIX_OPF : param;
            }
        } else {
            List listQueryParam = context.queryParam("home");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"home\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            home = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        String str2 = home;
        switch (str2.hashCode()) {
            case -1571867763:
                if (str2.equals("__LOCAL_STORE__")) {
                    if (this.getAppConfig().getSecure()) {
                        User userInfo = (User) context.get("userInfo");
                        if (userInfo == null) {
                            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                        }
                        if (!userInfo.getEnable_local_store()) {
                            return returnData.setErrorMsg("未开启本地书仓功能");
                        }
                    }
                    if ((isSave || isDelete) && !this.checkManagerAuth(context)) {
                        return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                    }
                    context.put("__FILE_HOME__", ExtKt.toDir$default(ExtKt.getWorkDir("storage", "localStore"), false, 1, null));
                    FileControllerKt.logger.info("context.__FILE_HOME__ {}", context.get("__FILE_HOME__"));
                    return null;
                }
                return returnData.setErrorMsg("非法访问");
            case -1386618657:
                if (str2.equals("__HOME__")) {
                    String userNameSpace = this.getUserNameSpace(context);
                    context.put("__FILE_HOME__", ExtKt.toDir$default(ExtKt.getWorkDir("storage", "data", userNameSpace), false, 1, null));
                    FileControllerKt.logger.info("context.__FILE_HOME__ {}", context.get("__FILE_HOME__"));
                    return null;
                }
                return returnData.setErrorMsg("非法访问");
            case -1330162107:
                if (str2.equals("__WEBDAV__")) {
                    if (this.getAppConfig().getSecure()) {
                        User userInfo2 = (User) context.get("userInfo");
                        if (userInfo2 == null) {
                            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                        }
                        if (!userInfo2.getEnable_webdav()) {
                            return returnData.setErrorMsg("未开启webdav功能");
                        }
                    }
                    context.put("__FILE_HOME__", ExtKt.toDir$default(this.getUserWebdavHome(context), false, 1, null));
                    FileControllerKt.logger.info("context.__FILE_HOME__ {}", context.get("__FILE_HOME__"));
                    return null;
                }
                return returnData.setErrorMsg("非法访问");
            case -220135525:
                if (str2.equals("__STORAGE__")) {
                    if (!this.checkManagerAuth(context)) {
                        return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                    }
                    context.put("__FILE_HOME__", ExtKt.toDir$default(ExtKt.getWorkDir("storage"), false, 1, null));
                    FileControllerKt.logger.info("context.__FILE_HOME__ {}", context.get("__FILE_HOME__"));
                    return null;
                }
                return returnData.setErrorMsg("非法访问");
            default:
                return returnData.setErrorMsg("非法访问");
        }
    }

    public static /* synthetic */ Object checkAccess$default(FileController fileController, RoutingContext routingContext, boolean z, boolean z2, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            z2 = false;
        }
        return fileController.checkAccess(routingContext, z, z2, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object list(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01181 c01181;
        Object objCheckAccess$default;
        String path;
        if ($completion instanceof C01181) {
            c01181 = (C01181) $completion;
            if ((c01181.label & Integer.MIN_VALUE) != 0) {
                c01181.label -= Integer.MIN_VALUE;
            } else {
                c01181 = new C01181($completion);
            }
        }
        Object $result = c01181.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01181.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01181.L$0 = context;
                c01181.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, false, false, c01181, 6, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01181.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString("path");
            path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        } else {
            List listQueryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            path = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (path.length() == 0) {
            path = TableOfContents.DEFAULT_PATH_SEPARATOR;
        }
        Object home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        String path2 = ExtKt.toDir(path, true);
        File file = new File(Intrinsics.stringPlus((String) home, path2));
        FileControllerKt.logger.info("file: {} {}", path2, file);
        if (!file.exists()) {
            if (!Intrinsics.areEqual(path2, TableOfContents.DEFAULT_PATH_SEPARATOR)) {
                return returnData.setErrorMsg("路径不存在");
            }
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            return returnData.setErrorMsg("路径不是目录");
        }
        ArrayList arrayList = new ArrayList();
        Object[] objArrListFiles = file.listFiles();
        Intrinsics.checkNotNullExpressionValue(objArrListFiles, "file.listFiles()");
        Object[] $this$forEach$iv = objArrListFiles;
        for (Object element$iv : $this$forEach$iv) {
            File it = (File) element$iv;
            String name = it.getName();
            Intrinsics.checkNotNullExpressionValue(name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object) null)) {
                String string2 = it.toString();
                Intrinsics.checkNotNullExpressionValue(string2, "it.toString()");
                arrayList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("name", it.getName()), TuplesKt.to("size", Boxing.boxLong(it.length())), TuplesKt.to("path", StringsKt.replace$default(string2, (String) home, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null)), TuplesKt.to("lastModified", Boxing.boxLong(it.lastModified())), TuplesKt.to("isDirectory", Boxing.boxBoolean(it.isDirectory()))}));
            }
        }
        return ReturnData.setData$default(returnData, arrayList, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object upload(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01231 c01231;
        ReturnData returnData;
        Object objCheckAccess$default;
        if ($completion instanceof C01231) {
            c01231 = (C01231) $completion;
            if ((c01231.label & Integer.MIN_VALUE) != 0) {
                c01231.label -= Integer.MIN_VALUE;
            } else {
                c01231 = new C01231($completion);
            }
        }
        Object $result = c01231.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01231.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
                    return returnData.setErrorMsg("请上传文件");
                }
                c01231.L$0 = context;
                c01231.L$1 = returnData;
                c01231.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, true, false, c01231, 4, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01231.L$1;
                context = (RoutingContext) c01231.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        String param = context.request().getParam("path");
        String str = param;
        if (str == null || str.length() == 0) {
            param = TableOfContents.DEFAULT_PATH_SEPARATOR;
        }
        String str2 = param;
        Intrinsics.checkNotNullExpressionValue(str2, "path");
        Object path = ExtKt.toDir(str2, true);
        ArrayList arrayList = new ArrayList();
        Object home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        Iterable iterableFileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue(iterableFileUploads, "context.fileUploads()");
        Iterable $this$forEach$iv = iterableFileUploads;
        for (Object element$iv : $this$forEach$iv) {
            FileUpload it = (FileUpload) element$iv;
            File file = new File(it.uploadedFileName());
            FileControllerKt.logger.info("uploadFile: {} {} {}", new Object[]{it.uploadedFileName(), it.fileName(), file});
            if (file.exists()) {
                String fileName = it.fileName();
                File newFile = new File(new StringBuilder().append(home).append(path).append((Object) File.separator).append((Object) fileName).toString());
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                if (newFile.exists()) {
                    newFile.delete();
                }
                FileControllerKt.logger.info("moveTo: {}", newFile);
                if (FilesKt.copyRecursively$default(file, newFile, false, (Function2) null, 6, (Object) null)) {
                    String string = newFile.toString();
                    Intrinsics.checkNotNullExpressionValue(string, "newFile.toString()");
                    arrayList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("name", newFile.getName()), TuplesKt.to("size", Boxing.boxLong(newFile.length())), TuplesKt.to("path", StringsKt.replace$default(string, (String) home, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null)), TuplesKt.to("lastModified", Boxing.boxLong(newFile.lastModified())), TuplesKt.to("isDirectory", Boxing.boxBoolean(newFile.isDirectory()))}));
                }
                FilesKt.deleteRecursively(file);
            }
        }
        return ReturnData.setData$default(returnData, arrayList, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object download(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C01151 c01151;
        Object objCheckAccess$default;
        String path;
        Integer numBoxInt;
        int stream;
        if ($completion instanceof C01151) {
            c01151 = (C01151) $completion;
            if ((c01151.label & Integer.MIN_VALUE) != 0) {
                c01151.label -= Integer.MIN_VALUE;
            } else {
                c01151 = new C01151($completion);
            }
        }
        Object $result = c01151.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01151.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01151.L$0 = context;
                c01151.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, false, false, c01151, 6, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01151.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            VertExtKt.success(context, checkResult);
            return Unit.INSTANCE;
        }
        ReturnData returnData = new ReturnData();
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString("path");
            path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
            Integer integer = context.getBodyAsJson().getInteger("stream", Boxing.boxInt(0));
            Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"stream\", 0)");
            stream = integer.intValue();
        } else {
            List listQueryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            path = str == null ? PackageDocumentBase.PREFIX_OPF : str;
            List listQueryParam2 = context.queryParam("stream");
            Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"stream\")");
            String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
            int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? 0 : numBoxInt.intValue();
            stream = iIntValue;
        }
        if (path.length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("参数错误"));
            return Unit.INSTANCE;
        }
        String home = (String) context.get("__FILE_HOME__");
        if (home == null) {
            VertExtKt.success(context, returnData.setErrorMsg("参数错误"));
            return Unit.INSTANCE;
        }
        String path2 = ExtKt.toDir(path, true);
        File file = new File(Intrinsics.stringPlus(home, path2));
        FileControllerKt.logger.info("file: {} {}", path2, file);
        if (!file.exists()) {
            VertExtKt.success(context, returnData.setErrorMsg("路径不存在"));
            return Unit.INSTANCE;
        }
        HttpServerResponse response = context.response().putHeader("Cache-Control", "86400");
        if (stream <= 0) {
            response.putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(file.getName(), Constants.CHARACTER_ENCODING)));
        }
        response.sendFile(file.toString());
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object get(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01161 c01161;
        Object objCheckAccess$default;
        String path;
        if ($completion instanceof C01161) {
            c01161 = (C01161) $completion;
            if ((c01161.label & Integer.MIN_VALUE) != 0) {
                c01161.label -= Integer.MIN_VALUE;
            } else {
                c01161 = new C01161($completion);
            }
        }
        Object $result = c01161.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01161.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01161.L$0 = context;
                c01161.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, false, false, c01161, 6, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01161.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString("path");
            path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        } else {
            List listQueryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            path = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (path.length() == 0) {
            return returnData.setErrorMsg("参数错误");
        }
        String home = (String) context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        String path2 = ExtKt.toDir(path, true);
        File file = new File(Intrinsics.stringPlus(home, path2));
        FileControllerKt.logger.info("file: {} {}", path2, file);
        if (!file.exists()) {
            return returnData.setErrorMsg("路径不存在");
        }
        return ReturnData.setData$default(returnData, FilesKt.readText$default(file, (Charset) null, 1, (Object) null), null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object save(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws IOException {
        C01221 c01221;
        Object objCheckAccess$default;
        if ($completion instanceof C01221) {
            c01221 = (C01221) $completion;
            if ((c01221.label & Integer.MIN_VALUE) != 0) {
                c01221.label -= Integer.MIN_VALUE;
            } else {
                c01221 = new C01221($completion);
            }
        }
        Object $result = c01221.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01221.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01221.L$0 = context;
                c01221.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, true, false, c01221, 4, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01221.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        String string = context.getBodyAsJson().getString("path", PackageDocumentBase.PREFIX_OPF);
        String path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        String string2 = context.getBodyAsJson().getString("content", PackageDocumentBase.PREFIX_OPF);
        String content = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
        if (path.length() == 0) {
            return returnData.setErrorMsg("参数错误");
        }
        String home = (String) context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        String path2 = ExtKt.toDir(path, true);
        File file = FileUtils.INSTANCE.createFileWithReplace(Intrinsics.stringPlus(home, path2));
        FileControllerKt.logger.info("file: {} {}", path2, file);
        FilesKt.writeText$default(file, content, (Charset) null, 2, (Object) null);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object mkdir(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01191 c01191;
        Object objCheckAccess$default;
        if ($completion instanceof C01191) {
            c01191 = (C01191) $completion;
            if ((c01191.label & Integer.MIN_VALUE) != 0) {
                c01191.label -= Integer.MIN_VALUE;
            } else {
                c01191 = new C01191($completion);
            }
        }
        Object $result = c01191.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01191.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01191.L$0 = context;
                c01191.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, true, false, c01191, 4, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01191.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        String string = context.getBodyAsJson().getString("path", PackageDocumentBase.PREFIX_OPF);
        String path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        if (path.length() == 0) {
            return returnData.setErrorMsg("参数错误");
        }
        String string2 = context.getBodyAsJson().getString("name", PackageDocumentBase.PREFIX_OPF);
        String name = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
        if ((name.length() == 0) || StringsKt.startsWith$default(name, ".", false, 2, (Object) null)) {
            return returnData.setErrorMsg("参数错误");
        }
        String home = (String) context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        String path2 = ExtKt.toDir(path, true);
        File file = new File(((Object) home) + path2 + ((Object) File.separator) + name);
        FileControllerKt.logger.info("file: {} {}", path2, file);
        if (file.exists()) {
            return returnData.setErrorMsg("路径已存在");
        }
        file.mkdirs();
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object delete(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01131 c01131;
        Object objCheckAccess;
        String path;
        if ($completion instanceof C01131) {
            c01131 = (C01131) $completion;
            if ((c01131.label & Integer.MIN_VALUE) != 0) {
                c01131.label -= Integer.MIN_VALUE;
            } else {
                c01131 = new C01131($completion);
            }
        }
        Object $result = c01131.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01131.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01131.L$0 = context;
                c01131.label = 1;
                objCheckAccess = checkAccess(context, false, true, c01131);
                if (objCheckAccess == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01131.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString("path");
            path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        } else {
            List listQueryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            path = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (path.length() == 0) {
            return returnData.setErrorMsg("参数错误");
        }
        String home = (String) context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        String path2 = ExtKt.toDir(path, true);
        File file = new File(Intrinsics.stringPlus(home, path2));
        FileControllerKt.logger.info("file: {} {}", path2, file);
        if (!file.exists()) {
            return returnData.setErrorMsg("路径不存在");
        }
        FilesKt.deleteRecursively(file);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01141 c01141;
        Object objCheckAccess;
        if ($completion instanceof C01141) {
            c01141 = (C01141) $completion;
            if ((c01141.label & Integer.MIN_VALUE) != 0) {
                c01141.label -= Integer.MIN_VALUE;
            } else {
                c01141 = new C01141($completion);
            }
        }
        Object $result = c01141.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01141.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01141.L$0 = context;
                c01141.label = 1;
                objCheckAccess = checkAccess(context, false, true, c01141);
                if (objCheckAccess == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01141.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        Iterable jsonArray = context.getBodyAsJson().getJsonArray("path");
        if (jsonArray == null) {
            return returnData.setErrorMsg("参数错误");
        }
        Object home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        Iterable $this$forEach$iv = jsonArray;
        for (Object element$iv : $this$forEach$iv) {
            String str = (String) element$iv;
            String filePath = str == null ? PackageDocumentBase.PREFIX_OPF : str;
            if (filePath.length() > 0) {
                File file = new File(Intrinsics.stringPlus((String) home, ExtKt.toDir(filePath, true)));
                FilesKt.deleteRecursively(file);
            }
        }
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object importPreview(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01171 c01171;
        Object objCheckAccess$default;
        if ($completion instanceof C01171) {
            c01171 = (C01171) $completion;
            if ((c01171.label & Integer.MIN_VALUE) != 0) {
                c01171.label -= Integer.MIN_VALUE;
            } else {
                c01171 = new C01171($completion);
            }
        }
        Object $result = c01171.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01171.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01171.L$0 = this;
                c01171.L$1 = context;
                c01171.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, false, false, c01171, 6, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01171.L$1;
                this = (FileController) c01171.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        Iterable jsonArray = context.getBodyAsJson().getJsonArray("path");
        if (jsonArray == null) {
            return returnData.setErrorMsg("参数错误");
        }
        Object home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        ArrayList arrayList = new ArrayList();
        String userNameSpace = this.getUserNameSpace(context);
        Iterable $this$forEach$iv = jsonArray;
        for (Object element$iv : $this$forEach$iv) {
            String str = (String) element$iv;
            String path = str == null ? PackageDocumentBase.PREFIX_OPF : str;
            if (path.length() > 0) {
                String path2 = Intrinsics.stringPlus((String) home, path);
                File file = new File(path2);
                FileControllerKt.logger.info("localFile: {} {}", path2, file);
                if (file.exists() && !file.isDirectory()) {
                    String fileName = file.getName();
                    Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                    String ext = BaseController.getFileExt$default(this, fileName, null, 2, null);
                    if (!Intrinsics.areEqual(ext, "txt") && !Intrinsics.areEqual(ext, "epub") && !Intrinsics.areEqual(ext, "umd") && !Intrinsics.areEqual(ext, "cbz") && !Intrinsics.areEqual(ext, "pdf")) {
                        return returnData.setErrorMsg("不支持导入" + ext + "格式的书籍文件");
                    }
                    String rootDir = ExtKt.getWorkDir$default(null, 1, null);
                    String str2 = File.separator;
                    Intrinsics.checkNotNullExpressionValue(str2, "separator");
                    if (!StringsKt.endsWith$default(rootDir, str2, false, 2, (Object) null)) {
                        rootDir = Intrinsics.stringPlus(rootDir, File.separator);
                    }
                    String relativePath = path2;
                    FileControllerKt.logger.info("rootDir: {} path: {}", rootDir, path2);
                    if (StringsKt.startsWith$default(relativePath, rootDir, false, 2, (Object) null)) {
                        relativePath = StringsKt.replaceFirst$default(relativePath, rootDir, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
                    }
                    FileControllerKt.logger.info("relative path: {}", relativePath);
                    String url = StringsKt.replace$default(relativePath, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null);
                    Book book = Book.INSTANCE.initLocalBook(url, relativePath, rootDir);
                    book.setUserNameSpace(userNameSpace);
                    FileControllerKt.logger.info("book {}", book);
                    try {
                        arrayList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", LocalBook.INSTANCE.getChapterList(book))}));
                    } catch (TocEmptyException e) {
                        arrayList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", new ArrayList())}));
                    }
                }
            }
        }
        return ReturnData.setData$default(returnData, arrayList, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0209  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object restore(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01211 c01211;
        ReturnData returnData;
        Object objSyncFromWebdav;
        Object objCheckAccess$default;
        ReturnData checkResult;
        String path;
        if ($completion instanceof C01211) {
            c01211 = (C01211) $completion;
            if ((c01211.label & Integer.MIN_VALUE) != 0) {
                c01211.label -= Integer.MIN_VALUE;
            } else {
                c01211 = new C01211($completion);
            }
        }
        Object $result = c01211.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01211.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01211.L$0 = this;
                c01211.L$1 = context;
                c01211.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, false, false, c01211, 6, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                checkResult = (ReturnData) objCheckAccess$default;
                if (checkResult == null) {
                    return checkResult;
                }
                returnData = new ReturnData();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("path");
                    path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
                } else {
                    List listQueryParam = context.queryParam("path");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    path = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                }
                if (path.length() == 0) {
                    path = TableOfContents.DEFAULT_PATH_SEPARATOR;
                }
                String ext = BaseController.getFileExt$default(this, path, null, 2, null);
                if (!Intrinsics.areEqual(ext, "zip")) {
                    return returnData.setErrorMsg("路径不是zip备份文件");
                }
                String home = (String) context.get("__FILE_HOME__");
                if (home == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                File file = new File(Intrinsics.stringPlus(home, path));
                FileControllerKt.logger.info("file: {} {}", path, file);
                if (!file.exists()) {
                    return returnData.setErrorMsg("路径不存在");
                }
                BookController bookController = new BookController(this.getCoroutineContext());
                String string2 = file.toString();
                Intrinsics.checkNotNullExpressionValue(string2, "file.toString()");
                c01211.L$0 = returnData;
                c01211.L$1 = null;
                c01211.label = 2;
                objSyncFromWebdav = bookController.syncFromWebdav(string2, this.getUserNameSpace(context), c01211);
                if (objSyncFromWebdav == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objSyncFromWebdav).booleanValue()) {
                    return returnData.setErrorMsg("恢复失败");
                }
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                context = (RoutingContext) c01211.L$1;
                this = (FileController) c01211.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                checkResult = (ReturnData) objCheckAccess$default;
                if (checkResult == null) {
                }
                break;
            case 2:
                returnData = (ReturnData) c01211.L$0;
                ResultKt.throwOnFailure($result);
                objSyncFromWebdav = $result;
                if (((Boolean) objSyncFromWebdav).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0420 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03e7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0371  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object parse(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01201 c01201;
        Object objCheckAccess$default;
        Object path;
        Integer numBoxInt;
        int iIntValue;
        String relativePath;
        if ($completion instanceof C01201) {
            c01201 = (C01201) $completion;
            if ((c01201.label & Integer.MIN_VALUE) != 0) {
                c01201.label -= Integer.MIN_VALUE;
            } else {
                c01201 = new C01201($completion);
            }
        }
        Object $result = c01201.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01201.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01201.L$0 = this;
                c01201.L$1 = context;
                c01201.label = 1;
                objCheckAccess$default = checkAccess$default(this, context, false, false, c01201, 6, null);
                if (objCheckAccess$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                context = (RoutingContext) c01201.L$1;
                this = (FileController) c01201.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAccess$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ReturnData checkResult = (ReturnData) objCheckAccess$default;
        if (checkResult != null) {
            return checkResult;
        }
        ReturnData returnData = new ReturnData();
        Object home = context.get("__FILE_HOME__");
        if (home == null) {
            return returnData.setErrorMsg("参数错误");
        }
        if (context.request().method() == HttpMethod.POST) {
            Object string = context.getBodyAsJson().getString("path");
            path = string == null ? PackageDocumentBase.PREFIX_OPF : string;
            Integer integer = context.getBodyAsJson().getInteger("import", Boxing.boxInt(0));
            Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"import\", 0)");
            iIntValue = integer.intValue();
        } else {
            List listQueryParam = context.queryParam("path");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
            Object obj = (String) CollectionsKt.firstOrNull(listQueryParam);
            path = obj == null ? PackageDocumentBase.PREFIX_OPF : obj;
            List listQueryParam2 = context.queryParam("import");
            Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"import\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam2);
            int iIntValue2 = (str == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str))) == null) ? 0 : numBoxInt.intValue();
            iIntValue = iIntValue2;
        }
        if (((CharSequence) path).length() == 0) {
            path = TableOfContents.DEFAULT_PATH_SEPARATOR;
        }
        File file = new File(Intrinsics.stringPlus((String) home, path));
        FileControllerKt.logger.info("file: {} {}", path, file);
        if (!file.exists()) {
            return returnData.setErrorMsg("路径不存在");
        }
        if (!file.isDirectory()) {
            return returnData.setErrorMsg("路径不是目录");
        }
        ArrayList arrayList = new ArrayList();
        String userNameSpace = this.getUserNameSpace(context);
        String workDir$default = ExtKt.getWorkDir$default(null, 1, null);
        String str2 = File.separator;
        Intrinsics.checkNotNullExpressionValue(str2, "separator");
        if (!StringsKt.endsWith$default(workDir$default, str2, false, 2, (Object) null)) {
            workDir$default = Intrinsics.stringPlus(workDir$default, File.separator);
        }
        BookController bookController = new BookController(this.getCoroutineContext());
        Iterable $this$forEach$iv = ExtKt.listFilesRecursively(file);
        for (Object element$iv : $this$forEach$iv) {
            File it = (File) element$iv;
            String name = it.getName();
            Intrinsics.checkNotNullExpressionValue(name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object) null) && it.isFile()) {
                String fileName = it.getName();
                Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                String ext = BaseController.getFileExt$default(this, fileName, null, 2, null);
                switch (ext.hashCode()) {
                    case 98299:
                        if (ext.equals("cbz")) {
                            relativePath = it.getPath();
                            FileControllerKt.logger.debug("rootDir: {} path: {}", workDir$default, path);
                            Intrinsics.checkNotNullExpressionValue(relativePath, "relativePath");
                            if (StringsKt.startsWith$default(relativePath, workDir$default, false, 2, (Object) null)) {
                                Intrinsics.checkNotNullExpressionValue(relativePath, "relativePath");
                                relativePath = StringsKt.replaceFirst$default(relativePath, workDir$default, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
                            }
                            FileControllerKt.logger.debug("relative path: {}", relativePath);
                            String str3 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str3, "relativePath");
                            String url = StringsKt.replace$default(str3, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null);
                            Book.Companion companion = Book.INSTANCE;
                            String str4 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str4, "relativePath");
                            Book book = companion.initLocalBook(url, str4, workDir$default);
                            book.setUserNameSpace(userNameSpace);
                            FileControllerKt.logger.debug("book {}", book);
                            if (iIntValue > 0) {
                                Pair<Book, String> pairSaveBookToShelf = bookController.saveBookToShelf(book, userNameSpace, context);
                                if (pairSaveBookToShelf.getSecond() == null && ((Book) pairSaveBookToShelf.getFirst()).isInShelf()) {
                                    arrayList.add(MapsKt.mapOf(TuplesKt.to("name", it.getName())));
                                }
                            } else {
                                String string2 = it.toString();
                                Intrinsics.checkNotNullExpressionValue(string2, "it.toString()");
                                arrayList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("name", it.getName()), TuplesKt.to("size", Boxing.boxLong(it.length())), TuplesKt.to("path", StringsKt.replaceFirst$default(string2, (String) home, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null)), TuplesKt.to("lastModified", Boxing.boxLong(it.lastModified())), TuplesKt.to("book", book)}));
                            }
                        }
                        break;
                    case 110834:
                        if (!ext.equals("pdf")) {
                            break;
                        } else {
                            relativePath = it.getPath();
                            FileControllerKt.logger.debug("rootDir: {} path: {}", workDir$default, path);
                            Intrinsics.checkNotNullExpressionValue(relativePath, "relativePath");
                            if (StringsKt.startsWith$default(relativePath, workDir$default, false, 2, (Object) null)) {
                            }
                            FileControllerKt.logger.debug("relative path: {}", relativePath);
                            String str32 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str32, "relativePath");
                            String url2 = StringsKt.replace$default(str32, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null);
                            Book.Companion companion2 = Book.INSTANCE;
                            String str42 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str42, "relativePath");
                            Book book2 = companion2.initLocalBook(url2, str42, workDir$default);
                            book2.setUserNameSpace(userNameSpace);
                            FileControllerKt.logger.debug("book {}", book2);
                            if (iIntValue > 0) {
                            }
                        }
                        break;
                    case 115312:
                        if (!ext.equals("txt")) {
                            break;
                        } else {
                            relativePath = it.getPath();
                            FileControllerKt.logger.debug("rootDir: {} path: {}", workDir$default, path);
                            Intrinsics.checkNotNullExpressionValue(relativePath, "relativePath");
                            if (StringsKt.startsWith$default(relativePath, workDir$default, false, 2, (Object) null)) {
                            }
                            FileControllerKt.logger.debug("relative path: {}", relativePath);
                            String str322 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str322, "relativePath");
                            String url22 = StringsKt.replace$default(str322, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null);
                            Book.Companion companion22 = Book.INSTANCE;
                            String str422 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str422, "relativePath");
                            Book book22 = companion22.initLocalBook(url22, str422, workDir$default);
                            book22.setUserNameSpace(userNameSpace);
                            FileControllerKt.logger.debug("book {}", book22);
                            if (iIntValue > 0) {
                            }
                        }
                        break;
                    case 115916:
                        if (!ext.equals("umd")) {
                            break;
                        } else {
                            relativePath = it.getPath();
                            FileControllerKt.logger.debug("rootDir: {} path: {}", workDir$default, path);
                            Intrinsics.checkNotNullExpressionValue(relativePath, "relativePath");
                            if (StringsKt.startsWith$default(relativePath, workDir$default, false, 2, (Object) null)) {
                            }
                            FileControllerKt.logger.debug("relative path: {}", relativePath);
                            String str3222 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str3222, "relativePath");
                            String url222 = StringsKt.replace$default(str3222, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null);
                            Book.Companion companion222 = Book.INSTANCE;
                            String str4222 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str4222, "relativePath");
                            Book book222 = companion222.initLocalBook(url222, str4222, workDir$default);
                            book222.setUserNameSpace(userNameSpace);
                            FileControllerKt.logger.debug("book {}", book222);
                            if (iIntValue > 0) {
                            }
                        }
                        break;
                    case 3120248:
                        if (!ext.equals("epub")) {
                            break;
                        } else {
                            relativePath = it.getPath();
                            FileControllerKt.logger.debug("rootDir: {} path: {}", workDir$default, path);
                            Intrinsics.checkNotNullExpressionValue(relativePath, "relativePath");
                            if (StringsKt.startsWith$default(relativePath, workDir$default, false, 2, (Object) null)) {
                            }
                            FileControllerKt.logger.debug("relative path: {}", relativePath);
                            String str32222 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str32222, "relativePath");
                            String url2222 = StringsKt.replace$default(str32222, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null);
                            Book.Companion companion2222 = Book.INSTANCE;
                            String str42222 = relativePath;
                            Intrinsics.checkNotNullExpressionValue(str42222, "relativePath");
                            Book book2222 = companion2222.initLocalBook(url2222, str42222, workDir$default);
                            book2222.setUserNameSpace(userNameSpace);
                            FileControllerKt.logger.debug("book {}", book2222);
                            if (iIntValue > 0) {
                            }
                        }
                        break;
                }
            }
        }
        return ReturnData.setData$default(returnData, arrayList, null, 2, null);
    }
}
