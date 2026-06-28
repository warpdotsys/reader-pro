package com.htmake.reader.api.controller;

import com.google.gson.reflect.TypeToken;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.License;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.VertExtKt;
import io.legado.app.constant.RSSKeywords;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.sync.Mutex;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: UserController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0019\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0006H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010\u0013\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJO\u0010\u0015\u001a\u00020\u000f2<\u0010\u0016\u001a8\b\u0001\u0012\u0004\u0012\u00020\u0018\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001c\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u0017¢\u0006\u0002\b H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010!J\u0019\u0010\"\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010#\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0010\u0010$\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0019\u0010%\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010&\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010'\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010(\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010)\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010*\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rJ\u0019\u0010+\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\rR\u0014\u0010\u0005\u001a\u00020\u0006X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006,"}, d2 = {"Lcom/htmake/reader/api/controller/UserController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "userMaxCount", PackageDocumentBase.PREFIX_OPF, "getUserMaxCount", "()I", "addUser", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearInactiveUsers", PackageDocumentBase.PREFIX_OPF, "day", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteFile", "deleteUsers", "downloadBackupFile", "forEachUser", "handler", "Lkotlin/Function3;", "Lkotlinx/coroutines/CoroutineScope;", "Lcom/htmake/reader/entity/User;", "Lkotlin/ParameterName;", "name", "user", "Lkotlin/coroutines/Continuation;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserConfig", "getUserInfo", "getUserLimit", "getUserList", "login", "logout", "resetPassword", "saveUserConfig", "updateUser", "uploadFile", "reader-pro"})
public final class UserController extends BaseController {
    private final int userMaxCount;

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$addUser$1, reason: invalid class name */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$addUser$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {251}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "addUser", c = "com.htmake.reader.api.controller.UserController")
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
            return UserController.this.addUser(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$clearInactiveUsers$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$clearInactiveUsers$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {412, 423, 424}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "clearInactiveUsers", c = "com.htmake.reader.api.controller.UserController")
    static final class C01301 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01301(Continuation<? super C01301> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.clearInactiveUsers((RoutingContext) null, (Continuation<? super ReturnData>) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$deleteFile$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$deleteFile$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {612}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteFile", c = "com.htmake.reader.api.controller.UserController")
    static final class C01311 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01311(Continuation<? super C01311> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.deleteFile(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$deleteUsers$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$deleteUsers$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {372}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteUsers", c = "com.htmake.reader.api.controller.UserController")
    static final class C01321 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01321(Continuation<? super C01321> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.deleteUsers(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$downloadBackupFile$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$downloadBackupFile$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {638, 645, 648}, i = {0, 0, 0, 1, 1}, s = {"L$0", "L$1", "L$2", "L$2", "L$3"}, n = {"this", "context", "returnData", "bookController", "userNameSpace"}, m = "downloadBackupFile", c = "com.htmake.reader.api.controller.UserController")
    static final class C01331 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C01331(Continuation<? super C01331> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.downloadBackupFile(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$forEachUser$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$forEachUser$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {674}, i = {}, s = {}, n = {}, m = "forEachUser", c = "com.htmake.reader.api.controller.UserController")
    static final class C01341 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        /* synthetic */ Object result;
        int label;

        C01341(Continuation<? super C01341> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.forEachUser(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$getUserConfig$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$getUserConfig$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {562}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getUserConfig", c = "com.htmake.reader.api.controller.UserController")
    static final class C01351 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01351(Continuation<? super C01351> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.getUserConfig(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$getUserInfo$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$getUserInfo$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {507}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getUserInfo", c = "com.htmake.reader.api.controller.UserController")
    static final class C01361 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01361(Continuation<? super C01361> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.getUserInfo(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$getUserList$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$getUserList$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {228}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getUserList", c = "com.htmake.reader.api.controller.UserController")
    static final class C01371 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01371(Continuation<? super C01371> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.getUserList(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$login$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$login$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {154, 170}, i = {}, s = {}, n = {}, m = "login", c = "com.htmake.reader.api.controller.UserController")
    static final class C01381 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01381(Continuation<? super C01381> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.login(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$logout$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$logout$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {177, Wbxml.OPAQUE}, i = {0, 0, 0, 1}, s = {"L$0", "L$1", "L$2", "L$4"}, n = {"this", "context", "returnData", "userMap"}, m = "logout", c = "com.htmake.reader.api.controller.UserController")
    static final class C01391 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C01391(Continuation<? super C01391> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.logout(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$resetPassword$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$resetPassword$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {326}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "resetPassword", c = "com.htmake.reader.api.controller.UserController")
    static final class C01401 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01401(Continuation<? super C01401> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.resetPassword(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$saveUserConfig$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$saveUserConfig$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {546}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveUserConfig", c = "com.htmake.reader.api.controller.UserController")
    static final class C01411 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01411(Continuation<? super C01411> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.saveUserConfig(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$updateUser$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$updateUser$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {447}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "updateUser", c = "com.htmake.reader.api.controller.UserController")
    static final class C01421 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01421(Continuation<? super C01421> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.updateUser(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$uploadFile$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$uploadFile$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserController.kt", l = {575}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "uploadFile", c = "com.htmake.reader.api.controller.UserController")
    static final class C01431 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01431(Continuation<? super C01431> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserController.this.uploadFile(null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
        this.userMaxCount = 15;
    }

    public final int getUserMaxCount() {
        return this.userMaxCount;
    }

    private final int getUserLimit(RoutingContext context) {
        License license = ExtKt.getInstalledLicense$default(false, 1, null);
        String strHost = context.request().host();
        Intrinsics.checkNotNullExpressionValue(strHost, "context.request().host()");
        if (license.validHost(strHost)) {
            return Math.min(Math.max(getAppConfig().getUserLimit(), 1), license.getUserMaxLimit());
        }
        return Math.min(Math.max(getAppConfig().getUserLimit(), 1), this.userMaxCount);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Type inference failed for: r2v12, types: [com.htmake.reader.api.controller.UserController$login$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object login(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01381 c01381;
        ReturnData returnData;
        Object objSaveUserSession$default;
        Object objSaveUserSession$default2;
        String json;
        if ($completion instanceof C01381) {
            c01381 = (C01381) $completion;
            if ((c01381.label & Integer.MIN_VALUE) != 0) {
                c01381.label -= Integer.MIN_VALUE;
            } else {
                c01381 = new C01381($completion);
            }
        }
        Object $result = c01381.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01381.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                String string = context.getBodyAsJson().getString("username", PackageDocumentBase.PREFIX_OPF);
                String username = string == null ? PackageDocumentBase.PREFIX_OPF : string;
                String string2 = context.getBodyAsJson().getString("password", PackageDocumentBase.PREFIX_OPF);
                String password = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                Boolean bool = context.getBodyAsJson().getBoolean("isLogin", Boxing.boxBoolean(false));
                boolean isLogin = bool == null ? false : bool.booleanValue();
                if (username.length() == 0) {
                    return returnData.setErrorMsg("请输入用户名");
                }
                if (password.length() == 0) {
                    return returnData.setErrorMsg("请输入密码");
                }
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
                    if (isLogin) {
                        return returnData.setErrorMsg("用户不存在");
                    }
                    if (username.length() < 5) {
                        return returnData.setErrorMsg("用户名不能低于5位");
                    }
                    if (password.length() < getAppConfig().getMinUserPasswordLength()) {
                        return returnData.setErrorMsg("密码不能低于" + getAppConfig().getMinUserPasswordLength() + (char) 20301);
                    }
                    if (username.equals("default")) {
                        return returnData.setErrorMsg("用户名不能为非法字符");
                    }
                    Regex usernameReg = new Regex("[a-z0-9]+", RegexOption.IGNORE_CASE);
                    if (!usernameReg.matches(username)) {
                        return returnData.setErrorMsg("用户名只能由字母和数字组成");
                    }
                    if (getAppConfig().getInviteCode().length() > 0) {
                        String string3 = context.getBodyAsJson().getString("code");
                        String code = string3 == null ? PackageDocumentBase.PREFIX_OPF : string3;
                        if (code.length() == 0) {
                            return returnData.setErrorMsg("请输入邀请码");
                        }
                        if (!getAppConfig().getInviteCode().equals(code)) {
                            return returnData.setErrorMsg("邀请码错误");
                        }
                    }
                    int userLimit = getUserLimit(context);
                    if (userMap.keySet().size() >= userLimit) {
                        return returnData.setErrorMsg("超过用户数上限");
                    }
                    String salt = ExtKt.getRandomString(8);
                    String passwordEncrypted = ExtKt.genEncryptedPassword(password, salt);
                    User newUser = new User(username, passwordEncrypted, salt, null, 0L, 0L, false, null, false, false, false, 0, 0, 8184, null);
                    newUser.setEnable_webdav(getAppConfig().getDefaultUserEnableWebdav());
                    newUser.setEnable_local_store(getAppConfig().getDefaultUserEnableLocalStore());
                    newUser.setEnable_book_source(getAppConfig().getDefaultUserEnableBookSource());
                    newUser.setEnable_rss_source(getAppConfig().getDefaultUserEnableRssSource());
                    newUser.setBook_source_limit(getAppConfig().getDefaultUserBookSourceLimit());
                    newUser.setBook_limit(getAppConfig().getDefaultUserBookLimit());
                    c01381.L$0 = returnData;
                    c01381.label = 1;
                    objSaveUserSession$default2 = BaseController.saveUserSession$default(this, context, newUser, false, c01381, 4, null);
                    if (objSaveUserSession$default2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Map loginData = (Map) objSaveUserSession$default2;
                    return ReturnData.setData$default(returnData, loginData, null, 2, null);
                }
                if (!isLogin) {
                    return returnData.setErrorMsg("用户名已被占用");
                }
                if (!(obj instanceof String)) {
                    json = ExtKt.getGson().toJson(obj);
                } else {
                    json = (String) obj;
                }
                String json$iv$iv = json;
                User userInfo = (User) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() { // from class: com.htmake.reader.api.controller.UserController$login$$inlined$toDataClass$1
                }.getType());
                if (userInfo == null) {
                    return returnData.setErrorMsg("用户信息错误");
                }
                String passwordEncrypted2 = ExtKt.genEncryptedPassword(password, userInfo.getSalt());
                if (!Intrinsics.areEqual(passwordEncrypted2, userInfo.getPassword())) {
                    return returnData.setErrorMsg("密码错误");
                }
                c01381.L$0 = returnData;
                c01381.label = 2;
                objSaveUserSession$default = BaseController.saveUserSession$default(this, context, userInfo, false, c01381, 4, null);
                if (objSaveUserSession$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Map loginData2 = (Map) objSaveUserSession$default;
                return ReturnData.setData$default(returnData, loginData2, null, 2, null);
            case 1:
                returnData = (ReturnData) c01381.L$0;
                ResultKt.throwOnFailure($result);
                objSaveUserSession$default2 = $result;
                Map loginData3 = (Map) objSaveUserSession$default2;
                return ReturnData.setData$default(returnData, loginData3, null, 2, null);
            case 2:
                returnData = (ReturnData) c01381.L$0;
                ResultKt.throwOnFailure($result);
                objSaveUserSession$default = $result;
                Map loginData22 = (Map) objSaveUserSession$default;
                return ReturnData.setData$default(returnData, loginData22, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x022f A[Catch: all -> 0x033b, TryCatch #0 {all -> 0x033b, blocks: (B:39:0x0195, B:46:0x020a, B:48:0x022f, B:50:0x023b, B:51:0x0245, B:52:0x0246, B:55:0x025d, B:56:0x0266, B:57:0x0267, B:59:0x027a, B:62:0x0290, B:64:0x02b1, B:66:0x02bd, B:67:0x02d4, B:69:0x02e8, B:70:0x02f5, B:45:0x0202), top: B:80:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x025d A[Catch: all -> 0x033b, TryCatch #0 {all -> 0x033b, blocks: (B:39:0x0195, B:46:0x020a, B:48:0x022f, B:50:0x023b, B:51:0x0245, B:52:0x0246, B:55:0x025d, B:56:0x0266, B:57:0x0267, B:59:0x027a, B:62:0x0290, B:64:0x02b1, B:66:0x02bd, B:67:0x02d4, B:69:0x02e8, B:70:0x02f5, B:45:0x0202), top: B:80:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0267 A[Catch: all -> 0x033b, TryCatch #0 {all -> 0x033b, blocks: (B:39:0x0195, B:46:0x020a, B:48:0x022f, B:50:0x023b, B:51:0x0245, B:52:0x0246, B:55:0x025d, B:56:0x0266, B:57:0x0267, B:59:0x027a, B:62:0x0290, B:64:0x02b1, B:66:0x02bd, B:67:0x02d4, B:69:0x02e8, B:70:0x02f5, B:45:0x0202), top: B:80:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object logout(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01391 c01391;
        Map userMap;
        String accessToken;
        String username;
        ReturnData returnData;
        Object objCheckAuth;
        JsonObject userMapJson;
        Map map;
        Map tokenMap;
        if ($completion instanceof C01391) {
            c01391 = (C01391) $completion;
            if ((c01391.label & Integer.MIN_VALUE) != 0) {
                c01391.label -= Integer.MIN_VALUE;
            } else {
                c01391 = new C01391($completion);
            }
        }
        Object $result = c01391.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c01391.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    c01391.L$0 = this;
                    c01391.L$1 = context;
                    c01391.L$2 = returnData;
                    c01391.label = 1;
                    objCheckAuth = checkAuth(context, c01391);
                    if (objCheckAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (((Boolean) objCheckAuth).booleanValue()) {
                        return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                    }
                    if (!this.getAppConfig().getSecure()) {
                        return returnData.setErrorMsg("不支持的操作");
                    }
                    String str = (String) context.session().get("username");
                    username = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    context.session().destroy();
                    List listQueryParam = context.queryParam("accessToken");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"accessToken\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    String accessToken2 = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    if (accessToken2.length() > 0) {
                        List tmp = StringsKt.split$default(accessToken2, new String[]{":"}, false, 2, 2, (Object) null);
                        if (tmp.size() >= 2) {
                            accessToken = (String) tmp.get(1);
                            userMap = new LinkedHashMap();
                            c01391.L$0 = this;
                            c01391.L$1 = returnData;
                            c01391.L$2 = username;
                            c01391.L$3 = accessToken;
                            c01391.L$4 = userMap;
                            c01391.label = 2;
                            if (Mutex.DefaultImpls.lock$default(this.getUserMutex(), (Object) null, c01391, 1, (Object) null) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                            if (userMapJson != null) {
                                Map map2 = userMapJson.getMap();
                                if (map2 == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
                                }
                                userMap = TypeIntrinsics.asMutableMap(map2);
                            }
                            map = userMap;
                            if (map != null) {
                                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
                            }
                            Map currentUser = (Map) map.getOrDefault(username, null);
                            if (currentUser == null) {
                                ReturnData errorMsg = returnData.setErrorMsg("系统错误");
                                Mutex.DefaultImpls.unlock$default(this.getUserMutex(), (Object) null, 1, (Object) null);
                                return errorMsg;
                            }
                            Object tokenMapVal = currentUser.getOrDefault("token_map", null);
                            if (tokenMapVal != null && (tokenMap = TypeIntrinsics.asMutableMap(tokenMapVal)) != null) {
                                tokenMap.remove(accessToken);
                                currentUser.put("token_map", tokenMap);
                            }
                            if (currentUser.getOrDefault("token", PackageDocumentBase.PREFIX_OPF).equals(accessToken)) {
                                currentUser.put("token", PackageDocumentBase.PREFIX_OPF);
                            }
                            userMap.put(username, currentUser);
                            String strEncode = Json.encode(userMap);
                            Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap)");
                            ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
                            Mutex.DefaultImpls.unlock$default(this.getUserMutex(), (Object) null, 1, (Object) null);
                        }
                    }
                    return ReturnData.setData$default(returnData.setErrorMsg("请重新登录"), "NEED_LOGIN", null, 2, null);
                case 1:
                    returnData = (ReturnData) c01391.L$2;
                    context = (RoutingContext) c01391.L$1;
                    this = (UserController) c01391.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckAuth = $result;
                    if (((Boolean) objCheckAuth).booleanValue()) {
                    }
                    break;
                case 2:
                    userMap = (Map) c01391.L$4;
                    accessToken = (String) c01391.L$3;
                    username = (String) c01391.L$2;
                    returnData = (ReturnData) c01391.L$1;
                    this = (UserController) c01391.L$0;
                    ResultKt.throwOnFailure($result);
                    userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                    if (userMapJson != null) {
                    }
                    map = userMap;
                    if (map != null) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            Mutex.DefaultImpls.unlock$default(getUserMutex(), (Object) null, 1, (Object) null);
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getUserList(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01371 c01371;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01371) {
            c01371 = (C01371) $completion;
            if ((c01371.label & Integer.MIN_VALUE) != 0) {
                c01371.label -= Integer.MIN_VALUE;
            } else {
                c01371 = new C01371($completion);
            }
        }
        Object $result = c01371.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01371.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01371.L$0 = this;
                c01371.L$1 = context;
                c01371.L$2 = returnData;
                c01371.label = 1;
                objCheckAuth = checkAuth(context, c01371);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01371.L$2;
                context = (RoutingContext) c01371.L$1;
                this = (UserController) c01371.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (this.getAppConfig().getSecure()) {
            if (!(this.getAppConfig().getSecureKey().length() == 0)) {
                if (!this.checkManagerAuth(context)) {
                    return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                }
                Map userMap = new LinkedHashMap();
                JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                if (userMapJson != null) {
                    Map map = userMapJson.getMap();
                    if (map == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
                    }
                    userMap = TypeIntrinsics.asMutableMap(map);
                }
                ArrayList arrayList = new ArrayList();
                Map $this$forEach$iv = userMap;
                for (Map.Entry element$iv : $this$forEach$iv.entrySet()) {
                    arrayList.add(this.formatUser(element$iv.getValue()));
                }
                return ReturnData.setData$default(returnData, arrayList, null, 2, null);
            }
        }
        return returnData.setErrorMsg("不支持的操作");
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object addUser(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        AnonymousClass1 anonymousClass1;
        ReturnData returnData;
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
                break;
            case 1:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (UserController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (this.getAppConfig().getSecure()) {
            if (!(this.getAppConfig().getSecureKey().length() == 0)) {
                String string = context.getBodyAsJson().getString("username");
                String username = string == null ? PackageDocumentBase.PREFIX_OPF : string;
                String string2 = context.getBodyAsJson().getString("password");
                String password = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                if (username.length() == 0) {
                    return returnData.setErrorMsg("请输入用户名");
                }
                if (password.length() == 0) {
                    return returnData.setErrorMsg("请输入密码");
                }
                if (username.length() < 5) {
                    return returnData.setErrorMsg("用户名不能低于5位");
                }
                if (password.length() < 8) {
                    return returnData.setErrorMsg("密码不能低于8位");
                }
                if (username.equals("default")) {
                    return returnData.setErrorMsg("用户名不能为非法字符");
                }
                if (!this.checkManagerAuth(context)) {
                    return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                }
                Regex usernameReg = new Regex("[a-z0-9]+", RegexOption.IGNORE_CASE);
                if (!usernameReg.matches(username)) {
                    return returnData.setErrorMsg("用户名只能由字母和数字组成");
                }
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
                Map existedUser = (Map) map2.getOrDefault(username, null);
                if (existedUser != null) {
                    return returnData.setErrorMsg("用户已存在");
                }
                int userLimit = this.getUserLimit(context);
                if (userMap.keySet().size() >= userLimit) {
                    return returnData.setErrorMsg("超过用户数上限");
                }
                Boolean enableWebdav = context.getBodyAsJson().getBoolean("enableWebdav");
                Boolean enableLocalStore = context.getBodyAsJson().getBoolean("enableLocalStore");
                Boolean enableBookSource = context.getBodyAsJson().getBoolean("enableBookSource");
                Boolean enableRssSource = context.getBodyAsJson().getBoolean("enableRssSource");
                Integer bookSourceLimit = context.getBodyAsJson().getInteger("bookSourceLimit");
                Integer bookLimit = context.getBodyAsJson().getInteger("bookLimit");
                String salt = ExtKt.getRandomString(8);
                String passwordEncrypted = ExtKt.genEncryptedPassword(password, salt);
                User newUser = new User(username, passwordEncrypted, salt, null, 0L, 0L, false, null, false, false, false, 0, 0, 8184, null);
                newUser.setEnable_webdav(enableWebdav == null ? this.getAppConfig().getDefaultUserEnableWebdav() : enableWebdav.booleanValue());
                newUser.setEnable_local_store(enableLocalStore == null ? this.getAppConfig().getDefaultUserEnableLocalStore() : enableLocalStore.booleanValue());
                newUser.setEnable_book_source(enableBookSource == null ? this.getAppConfig().getDefaultUserEnableBookSource() : enableBookSource.booleanValue());
                newUser.setEnable_rss_source(enableRssSource == null ? this.getAppConfig().getDefaultUserEnableRssSource() : enableRssSource.booleanValue());
                newUser.setBook_source_limit(bookSourceLimit == null ? this.getAppConfig().getDefaultUserBookSourceLimit() : bookSourceLimit.intValue());
                newUser.setBook_limit(bookLimit == null ? this.getAppConfig().getDefaultUserBookLimit() : bookLimit.intValue());
                userMap.put(newUser.getUsername(), ExtKt.toMap(newUser));
                String strEncode = Json.encode(userMap);
                Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap)");
                ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
                ArrayList arrayList = new ArrayList();
                Map $this$forEach$iv = userMap;
                for (Map.Entry element$iv : $this$forEach$iv.entrySet()) {
                    arrayList.add(this.formatUser(element$iv.getValue()));
                }
                return ReturnData.setData$default(returnData, arrayList, null, 2, null);
            }
        }
        return returnData.setErrorMsg("不支持的操作");
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object resetPassword(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01401 c01401;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01401) {
            c01401 = (C01401) $completion;
            if ((c01401.label & Integer.MIN_VALUE) != 0) {
                c01401.label -= Integer.MIN_VALUE;
            } else {
                c01401 = new C01401($completion);
            }
        }
        Object $result = c01401.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01401.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01401.L$0 = this;
                c01401.L$1 = context;
                c01401.L$2 = returnData;
                c01401.label = 1;
                objCheckAuth = checkAuth(context, c01401);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01401.L$2;
                context = (RoutingContext) c01401.L$1;
                this = (UserController) c01401.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (this.getAppConfig().getSecure()) {
            if (!(this.getAppConfig().getSecureKey().length() == 0)) {
                String string = context.getBodyAsJson().getString("username");
                String username = string == null ? PackageDocumentBase.PREFIX_OPF : string;
                String string2 = context.getBodyAsJson().getString("password");
                String password = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                if (username.length() == 0) {
                    return returnData.setErrorMsg("请输入用户名");
                }
                if (password.length() == 0) {
                    return returnData.setErrorMsg("请输入密码");
                }
                if (password.length() < this.getAppConfig().getMinUserPasswordLength()) {
                    return returnData.setErrorMsg("密码不能低于" + this.getAppConfig().getMinUserPasswordLength() + (char) 20301);
                }
                if (username.equals("default")) {
                    return returnData.setErrorMsg("用户不存在");
                }
                if (!this.checkManagerAuth(context)) {
                    return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                }
                Map userMap = new LinkedHashMap();
                JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                if (userMapJson != null) {
                    Map map = userMapJson.getMap();
                    if (map == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
                    }
                    userMap = TypeIntrinsics.asMutableMap(map);
                }
                Map map2 = userMap;
                if (map2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
                }
                Map existedUser = (Map) map2.getOrDefault(username, null);
                if (existedUser == null) {
                    return returnData.setErrorMsg("用户不存在");
                }
                String salt = ExtKt.getRandomString(8);
                String passwordEncrypted = ExtKt.genEncryptedPassword(password, salt);
                existedUser.put("salt", salt);
                existedUser.put("password", passwordEncrypted);
                userMap.put(username, existedUser);
                String strEncode = Json.encode(userMap);
                Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap as MutableMap<String, Map<String, Any>>)");
                ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            }
        }
        return returnData.setErrorMsg("不支持的操作");
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteUsers(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01321 c01321;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01321) {
            c01321 = (C01321) $completion;
            if ((c01321.label & Integer.MIN_VALUE) != 0) {
                c01321.label -= Integer.MIN_VALUE;
            } else {
                c01321 = new C01321($completion);
            }
        }
        Object $result = c01321.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01321.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01321.L$0 = this;
                c01321.L$1 = context;
                c01321.L$2 = returnData;
                c01321.label = 1;
                objCheckAuth = checkAuth(context, c01321);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01321.L$2;
                context = (RoutingContext) c01321.L$1;
                this = (UserController) c01321.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (this.getAppConfig().getSecure()) {
            if (!(this.getAppConfig().getSecureKey().length() == 0)) {
                if (!this.checkManagerAuth(context)) {
                    return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                }
                Map userMap = new LinkedHashMap();
                JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                if (userMapJson != null) {
                    JsonArray userJsonArray = context.getBodyAsJsonArray();
                    int i = 0;
                    int size = userJsonArray.size();
                    if (0 < size) {
                        do {
                            int i2 = i;
                            i++;
                            String username = userJsonArray.getString(i2);
                            if (username != null && userMapJson.containsKey(username)) {
                                userMapJson.remove(username);
                                File userHome = new File(ExtKt.getWorkDir("storage", "data", username));
                                UserControllerKt.logger.info("delete userHome: {}", userHome);
                                if (userHome.exists()) {
                                    ExtKt.deleteRecursively(userHome);
                                }
                            }
                        } while (i < size);
                    }
                    Map map = userMapJson.getMap();
                    if (map == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
                    }
                    userMap = TypeIntrinsics.asMutableMap(map);
                    String strEncode = Json.encode(userMap);
                    Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap)");
                    ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
                }
                ArrayList arrayList = new ArrayList();
                Map $this$forEach$iv = userMap;
                for (Map.Entry element$iv : $this$forEach$iv.entrySet()) {
                    arrayList.add(this.formatUser(element$iv.getValue()));
                }
                return ReturnData.setData$default(returnData, arrayList, null, 2, null);
            }
        }
        return returnData.setErrorMsg("不支持的操作");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object clearInactiveUsers(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01301 c01301;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01301) {
            c01301 = (C01301) $completion;
            if ((c01301.label & Integer.MIN_VALUE) != 0) {
                c01301.label -= Integer.MIN_VALUE;
            } else {
                c01301 = new C01301($completion);
            }
        }
        Object $result = c01301.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01301.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01301.L$0 = this;
                c01301.L$1 = context;
                c01301.L$2 = returnData;
                c01301.label = 1;
                objCheckAuth = checkAuth(context, c01301);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (this.getAppConfig().getSecure()) {
                    if (!(this.getAppConfig().getSecureKey().length() == 0)) {
                        if (!this.checkManagerAuth(context)) {
                            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                        }
                        Integer inactiveDay = context.getBodyAsJson().getInteger("inactiveDay", Boxing.boxInt(0));
                        Intrinsics.checkNotNullExpressionValue(inactiveDay, "inactiveDay");
                        c01301.L$0 = this;
                        c01301.L$1 = context;
                        c01301.L$2 = null;
                        c01301.label = 2;
                        if (this.clearInactiveUsers(inactiveDay.intValue(), (Continuation<? super Unit>) c01301) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        c01301.L$0 = null;
                        c01301.L$1 = null;
                        c01301.label = 3;
                        Object userList = this.getUserList(context, c01301);
                        return userList != coroutine_suspended ? coroutine_suspended : userList;
                    }
                }
                return returnData.setErrorMsg("不支持的操作");
            case 1:
                returnData = (ReturnData) c01301.L$2;
                context = (RoutingContext) c01301.L$1;
                this = (UserController) c01301.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                context = (RoutingContext) c01301.L$1;
                this = (UserController) c01301.L$0;
                ResultKt.throwOnFailure($result);
                c01301.L$0 = null;
                c01301.L$1 = null;
                c01301.label = 3;
                Object userList2 = this.getUserList(context, c01301);
                if (userList2 != coroutine_suspended) {
                }
                break;
            case 3:
                ResultKt.throwOnFailure($result);
                return $result;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.UserController$clearInactiveUsers$3, reason: invalid class name */
    /* JADX INFO: compiled from: UserController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/UserController$clearInactiveUsers$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "user", "Lcom/htmake/reader/entity/User;"})
    @DebugMetadata(f = "UserController.kt", l = {}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.UserController$clearInactiveUsers$3")
    static final class AnonymousClass3 extends SuspendLambda implements Function3<CoroutineScope, User, Continuation<? super Boolean>, Object> {
        int label;
        /* synthetic */ Object L$0;
        final /* synthetic */ long $expireTime;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(long $expireTime, Continuation<? super AnonymousClass3> $completion) {
            super(3, $completion);
            this.$expireTime = $expireTime;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @NotNull User p2, @Nullable Continuation<? super Boolean> p3) {
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(this.$expireTime, p3);
            anonymousClass3.L$0 = p2;
            return anonymousClass3.invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            boolean z;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    User user = (User) this.L$0;
                    if (user.getLast_login_at() < this.$expireTime) {
                        UserControllerKt.logger.info("delete user: {}", user);
                        File userHome = new File(ExtKt.getWorkDir("storage", "data", user.getUsername()));
                        UserControllerKt.logger.info("delete userHome: {}", userHome);
                        if (userHome.exists()) {
                            ExtKt.deleteRecursively(userHome);
                        }
                        z = true;
                    } else {
                        z = false;
                    }
                    return Boxing.boxBoolean(z);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Nullable
    public final Object clearInactiveUsers(int day, @NotNull Continuation<? super Unit> $completion) {
        long expireTime = System.currentTimeMillis() - ((((long) day) * 86400) * 1000);
        Object objForEachUser = forEachUser(new AnonymousClass3(expireTime, null), $completion);
        return objForEachUser == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objForEachUser : Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object updateUser(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01421 c01421;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01421) {
            c01421 = (C01421) $completion;
            if ((c01421.label & Integer.MIN_VALUE) != 0) {
                c01421.label -= Integer.MIN_VALUE;
            } else {
                c01421 = new C01421($completion);
            }
        }
        Object $result = c01421.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01421.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01421.L$0 = this;
                c01421.L$1 = context;
                c01421.L$2 = returnData;
                c01421.label = 1;
                objCheckAuth = checkAuth(context, c01421);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01421.L$2;
                context = (RoutingContext) c01421.L$1;
                this = (UserController) c01421.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (this.getAppConfig().getSecure()) {
            if (!(this.getAppConfig().getSecureKey().length() == 0)) {
                if (!this.checkManagerAuth(context)) {
                    return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                }
                String string = context.getBodyAsJson().getString("username");
                String username = string == null ? PackageDocumentBase.PREFIX_OPF : string;
                if (username.length() == 0) {
                    return returnData.setErrorMsg("参数错误");
                }
                Boolean enableWebdav = context.getBodyAsJson().getBoolean("enableWebdav");
                Boolean enableLocalStore = context.getBodyAsJson().getBoolean("enableLocalStore");
                Boolean enableBookSource = context.getBodyAsJson().getBoolean("enableBookSource");
                Boolean enableRssSource = context.getBodyAsJson().getBoolean("enableRssSource");
                Integer bookSourceLimit = context.getBodyAsJson().getInteger("bookSourceLimit");
                Integer bookLimit = context.getBodyAsJson().getInteger("bookLimit");
                Map userMap = new LinkedHashMap();
                JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                if (userMapJson != null) {
                    Map map = userMapJson.getMap();
                    if (map == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
                    }
                    userMap = TypeIntrinsics.asMutableMap(map);
                    if (userMap == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
                    }
                    Map existedUser = (Map) userMap.getOrDefault(username, null);
                    if (existedUser == null) {
                        return returnData.setErrorMsg("用户不存在");
                    }
                    if (enableWebdav != null) {
                        existedUser.put("enable_webdav", enableWebdav);
                    }
                    if (enableLocalStore != null) {
                        existedUser.put("enable_local_store", enableLocalStore);
                    }
                    if (enableBookSource != null) {
                        existedUser.put("enable_book_source", enableBookSource);
                    }
                    if (enableRssSource != null) {
                        existedUser.put("enable_rss_source", enableRssSource);
                    }
                    if (bookSourceLimit != null) {
                        existedUser.put("book_source_limit", bookSourceLimit);
                    }
                    if (bookLimit != null) {
                        existedUser.put("book_limit", bookLimit);
                    }
                    userMap.put(username, existedUser);
                    String strEncode = Json.encode(userMap);
                    Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap)");
                    ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
                }
                ArrayList arrayList = new ArrayList();
                Map $this$forEach$iv = userMap;
                for (Map.Entry element$iv : $this$forEach$iv.entrySet()) {
                    arrayList.add(this.formatUser(element$iv.getValue()));
                }
                return ReturnData.setData$default(returnData, arrayList, null, 2, null);
            }
        }
        return returnData.setErrorMsg("不支持的操作");
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getUserInfo(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01361 c01361;
        ReturnData returnData;
        User user;
        if ($completion instanceof C01361) {
            c01361 = (C01361) $completion;
            if ((c01361.label & Integer.MIN_VALUE) != 0) {
                c01361.label -= Integer.MIN_VALUE;
            } else {
                c01361 = new C01361($completion);
            }
        }
        Object $result = c01361.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01361.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01361.L$0 = this;
                c01361.L$1 = context;
                c01361.L$2 = returnData;
                c01361.label = 1;
                if (checkAuth(context, c01361) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01361.L$2;
                context = (RoutingContext) c01361.L$1;
                this = (UserController) c01361.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        String username = (String) context.session().get("username");
        Boolean secure = (Boolean) this.getEnv().getProperty("reader.app.secure", Boolean.TYPE);
        String secureKey = this.getEnv().getProperty("reader.app.secureKey");
        Object userInfo = null;
        if (username != null && (user = this.getUserInfoClass(username)) != null) {
            userInfo = this.formatUser(user);
        }
        String fontsDir = ExtKt.getWorkDir("storage", "assets", "fonts");
        ArrayList arrayList = new ArrayList();
        Iterable $this$forEach$iv = ExtKt.listFilesRecursively(new File(fontsDir));
        for (Object element$iv : $this$forEach$iv) {
            File it = (File) element$iv;
            String name = it.getName();
            Intrinsics.checkNotNullExpressionValue(name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object) null) && it.isFile()) {
                String fileName = it.getName();
                Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                String ext = BaseController.getFileExt$default(this, fileName, null, 2, null);
                if (Intrinsics.areEqual(ext, "ttf")) {
                    arrayList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("name", it.getName()), TuplesKt.to("size", Boxing.boxLong(it.length()))}));
                }
            }
        }
        ReturnData returnData2 = returnData;
        Pair[] pairArr = new Pair[4];
        pairArr[0] = TuplesKt.to("userInfo", userInfo);
        pairArr[1] = TuplesKt.to("secure", secure);
        pairArr[2] = TuplesKt.to("secureKey", secureKey == null ? null : Boxing.boxBoolean(secureKey.length() > 0));
        pairArr[3] = TuplesKt.to("fonts", arrayList);
        return ReturnData.setData$default(returnData2, MapsKt.mapOf(pairArr), null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveUserConfig(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01411 c01411;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01411) {
            c01411 = (C01411) $completion;
            if ((c01411.label & Integer.MIN_VALUE) != 0) {
                c01411.label -= Integer.MIN_VALUE;
            } else {
                c01411 = new C01411($completion);
            }
        }
        Object $result = c01411.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01411.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01411.L$0 = this;
                c01411.L$1 = context;
                c01411.L$2 = returnData;
                c01411.label = 1;
                objCheckAuth = checkAuth(context, c01411);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01411.L$2;
                context = (RoutingContext) c01411.L$1;
                this = (UserController) c01411.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        JsonObject content = context.getBodyAsJson();
        if (content == null) {
            return returnData.setErrorMsg("参数错误");
        }
        content.put("@updateTime", Boxing.boxLong(System.currentTimeMillis()));
        String userNameSpace = this.getUserNameSpace(context);
        this.saveUserStorage(userNameSpace, "userConfig", content);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getUserConfig(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01351 c01351;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01351) {
            c01351 = (C01351) $completion;
            if ((c01351.label & Integer.MIN_VALUE) != 0) {
                c01351.label -= Integer.MIN_VALUE;
            } else {
                c01351 = new C01351($completion);
            }
        }
        Object $result = c01351.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01351.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01351.L$0 = this;
                c01351.L$1 = context;
                c01351.L$2 = returnData;
                c01351.label = 1;
                objCheckAuth = checkAuth(context, c01351);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01351.L$2;
                context = (RoutingContext) c01351.L$1;
                this = (UserController) c01351.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        String userNameSpace = this.getUserNameSpace(context);
        JsonObject userConfig = ExtKt.asJsonObject(this.getUserStorage(userNameSpace, "userConfig"));
        if (userConfig == null) {
            return returnData.setErrorMsg("没有备份文件");
        }
        Map map = userConfig.getMap();
        Intrinsics.checkNotNullExpressionValue(map, "userConfig.map");
        return ReturnData.setData$default(returnData, map, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object uploadFile(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01431 c01431;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01431) {
            c01431 = (C01431) $completion;
            if ((c01431.label & Integer.MIN_VALUE) != 0) {
                c01431.label -= Integer.MIN_VALUE;
            } else {
                c01431 = new C01431($completion);
            }
        }
        Object $result = c01431.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01431.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01431.L$0 = this;
                c01431.L$1 = context;
                c01431.L$2 = returnData;
                c01431.label = 1;
                objCheckAuth = checkAuth(context, c01431);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01431.L$2;
                context = (RoutingContext) c01431.L$1;
                this = (UserController) c01431.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
            return returnData.setErrorMsg("请上传文件");
        }
        String userNameSpace = this.getUserNameSpace(context);
        JsonArray jsonArray = new JsonArray();
        Object type = context.request().getParam("type");
        String str = (CharSequence) type;
        if (str == null || str.length() == 0) {
            type = "images";
        }
        Iterable iterableFileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue(iterableFileUploads, "context.fileUploads()");
        Iterable $this$forEach$iv = iterableFileUploads;
        for (Object element$iv : $this$forEach$iv) {
            FileUpload it = (FileUpload) element$iv;
            File file = new File(it.uploadedFileName());
            UserControllerKt.logger.info("uploadFile: {} {} {}", new Object[]{it.uploadedFileName(), it.fileName(), file});
            if (file.exists()) {
                String fileName = it.fileName();
                Object obj = type;
                Intrinsics.checkNotNullExpressionValue(obj, "type");
                Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                File newFile = new File(ExtKt.getWorkDir("storage", "assets", userNameSpace, obj, fileName));
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                if (newFile.exists()) {
                    newFile.delete();
                }
                UserControllerKt.logger.info("moveTo: {}", newFile);
                if (FilesKt.copyRecursively$default(file, newFile, false, (Function2) null, 6, (Object) null)) {
                    jsonArray.add("/assets/" + userNameSpace + '/' + type + '/' + ((Object) fileName));
                }
                ExtKt.deleteRecursively(file);
            }
        }
        List list = jsonArray.getList();
        Intrinsics.checkNotNullExpressionValue(list, "fileList.getList()");
        return ReturnData.setData$default(returnData, list, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteFile(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01311 c01311;
        ReturnData returnData;
        Object objCheckAuth;
        String url;
        if ($completion instanceof C01311) {
            c01311 = (C01311) $completion;
            if ((c01311.label & Integer.MIN_VALUE) != 0) {
                c01311.label -= Integer.MIN_VALUE;
            } else {
                c01311 = new C01311($completion);
            }
        }
        Object $result = c01311.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01311.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01311.L$0 = this;
                c01311.L$1 = context;
                c01311.L$2 = returnData;
                c01311.label = 1;
                objCheckAuth = checkAuth(context, c01311);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01311.L$2;
                context = (RoutingContext) c01311.L$1;
                this = (UserController) c01311.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
            url = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        } else {
            List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            url = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (url.length() == 0) {
            return returnData.setErrorMsg("请输入文件链接");
        }
        String userNameSpace = this.getUserNameSpace(context);
        if (!StringsKt.startsWith$default(url, "/assets/" + userNameSpace + '/', false, 2, (Object) null)) {
            return returnData.setErrorMsg("文件链接错误");
        }
        File file = new File(ExtKt.getWorkDir(Intrinsics.stringPlus("storage", url)));
        UserControllerKt.logger.info("delete file: {}", file);
        ExtKt.deleteRecursively(file);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object downloadBackupFile(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C01331 c01331;
        ReturnData returnData;
        Object objCreateUserBackup;
        String userNameSpace;
        BookController bookController;
        Object lastBackFileFromWebdav;
        Object objCheckAuth;
        File backupFile;
        if ($completion instanceof C01331) {
            c01331 = (C01331) $completion;
            if ((c01331.label & Integer.MIN_VALUE) != 0) {
                c01331.label -= Integer.MIN_VALUE;
            } else {
                c01331 = new C01331($completion);
            }
        }
        Object $result = c01331.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01331.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01331.L$0 = this;
                c01331.L$1 = context;
                c01331.L$2 = returnData;
                c01331.label = 1;
                objCheckAuth = checkAuth(context, c01331);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
                    return Unit.INSTANCE;
                }
                bookController = new BookController(this.getCoroutineContext());
                userNameSpace = this.getUserNameSpace(context);
                c01331.L$0 = context;
                c01331.L$1 = returnData;
                c01331.L$2 = bookController;
                c01331.L$3 = userNameSpace;
                c01331.label = 2;
                lastBackFileFromWebdav = bookController.getLastBackFileFromWebdav(userNameSpace, c01331);
                if (lastBackFileFromWebdav == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String latestZipFilePath = (String) lastBackFileFromWebdav;
                String backupDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "backup");
                c01331.L$0 = context;
                c01331.L$1 = returnData;
                c01331.L$2 = null;
                c01331.L$3 = null;
                c01331.label = 3;
                objCreateUserBackup = bookController.createUserBackup(userNameSpace, backupDir, latestZipFilePath, c01331);
                if (objCreateUserBackup == coroutine_suspended) {
                    return coroutine_suspended;
                }
                backupFile = (File) objCreateUserBackup;
                if (backupFile == null) {
                    VertExtKt.success(context, returnData.setErrorMsg("备份失败"));
                    return Unit.INSTANCE;
                }
                HttpServerResponse response = context.response().putHeader("Cache-Control", "86400");
                response.putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(backupFile.getName(), Constants.CHARACTER_ENCODING)));
                response.sendFile(backupFile.toString());
                return Unit.INSTANCE;
            case 1:
                returnData = (ReturnData) c01331.L$2;
                context = (RoutingContext) c01331.L$1;
                this = (UserController) c01331.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                userNameSpace = (String) c01331.L$3;
                bookController = (BookController) c01331.L$2;
                returnData = (ReturnData) c01331.L$1;
                context = (RoutingContext) c01331.L$0;
                ResultKt.throwOnFailure($result);
                lastBackFileFromWebdav = $result;
                String latestZipFilePath2 = (String) lastBackFileFromWebdav;
                String backupDir2 = ExtKt.getWorkDir("storage", "data", userNameSpace, "backup");
                c01331.L$0 = context;
                c01331.L$1 = returnData;
                c01331.L$2 = null;
                c01331.L$3 = null;
                c01331.label = 3;
                objCreateUserBackup = bookController.createUserBackup(userNameSpace, backupDir2, latestZipFilePath2, c01331);
                if (objCreateUserBackup == coroutine_suspended) {
                }
                backupFile = (File) objCreateUserBackup;
                if (backupFile == null) {
                }
                break;
            case 3:
                returnData = (ReturnData) c01331.L$1;
                context = (RoutingContext) c01331.L$0;
                ResultKt.throwOnFailure($result);
                objCreateUserBackup = $result;
                backupFile = (File) objCreateUserBackup;
                if (backupFile == null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0297  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Type inference failed for: r2v9, types: [com.htmake.reader.api.controller.UserController$forEachUser$lambda-7$lambda-6$$inlined$toDataClass$1] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:53:0x0278 -> B:20:0x0105). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:54:0x027b -> B:20:0x0105). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object forEachUser(@NotNull Function3<? super CoroutineScope, ? super User, ? super Continuation<? super Boolean>, ? extends Object> handler, @NotNull Continuation<? super Unit> $completion) {
        C01341 c01341;
        Iterator it;
        Iterator $this$forEachUser_u24lambda_u2d7;
        Ref.BooleanRef hasChanged;
        Ref.ObjectRef userMap;
        Object objInvoke;
        String json;
        User user;
        if ($completion instanceof C01341) {
            c01341 = (C01341) $completion;
            if ((c01341.label & Integer.MIN_VALUE) != 0) {
                c01341.label -= Integer.MIN_VALUE;
            } else {
                c01341 = new C01341($completion);
            }
        }
        Object $result = c01341.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01341.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (getAppConfig().getSecure()) {
                    userMap = new Ref.ObjectRef();
                    userMap.element = new LinkedHashMap();
                    JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                    if (userMapJson != null) {
                        Map map = userMapJson.getMap();
                        if (map == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                        }
                        userMap.element = TypeIntrinsics.asMutableMap(map);
                    }
                    hasChanged = new Ref.BooleanRef();
                    $this$forEachUser_u24lambda_u2d7 = ((Map) userMap.element).entrySet().iterator();
                    it = $this$forEachUser_u24lambda_u2d7;
                    while (it.hasNext()) {
                        Object element$iv = it.next();
                        Map.Entry it2 = (Map.Entry) element$iv;
                        Map user2 = (Map) it2.getValue();
                        if (user2 != null) {
                            String str = (String) user2.getOrDefault("username", PackageDocumentBase.PREFIX_OPF);
                            String username = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                            if (username.length() > 0) {
                                Map map2 = (Map) userMap.element;
                                if (map2 == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
                                }
                                Object obj = (Map) map2.getOrDefault(username, null);
                                if (obj != null) {
                                    if (!(obj instanceof String)) {
                                        json = ExtKt.getGson().toJson(obj);
                                    } else {
                                        json = (String) obj;
                                    }
                                    String json$iv$iv = json;
                                    user = (User) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() { // from class: com.htmake.reader.api.controller.UserController$forEachUser$lambda-7$lambda-6$$inlined$toDataClass$1
                                    }.getType());
                                } else {
                                    user = null;
                                }
                                User existedUser = user;
                                if (existedUser != null) {
                                    c01341.L$0 = this;
                                    c01341.L$1 = handler;
                                    c01341.L$2 = userMap;
                                    c01341.L$3 = hasChanged;
                                    c01341.L$4 = $this$forEachUser_u24lambda_u2d7;
                                    c01341.L$5 = it;
                                    c01341.label = 1;
                                    objInvoke = handler.invoke(this, existedUser, c01341);
                                    if (objInvoke == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    if (((Boolean) objInvoke).booleanValue()) {
                                        hasChanged.element = true;
                                        $this$forEachUser_u24lambda_u2d7.remove();
                                    }
                                    while (it.hasNext()) {
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                    if (hasChanged.element) {
                        String strEncode = Json.encode(userMap.element);
                        Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap)");
                        ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
                    }
                }
                return Unit.INSTANCE;
            case 1:
                it = (Iterator) c01341.L$5;
                $this$forEachUser_u24lambda_u2d7 = (Iterator) c01341.L$4;
                hasChanged = (Ref.BooleanRef) c01341.L$3;
                userMap = (Ref.ObjectRef) c01341.L$2;
                handler = (Function3) c01341.L$1;
                this = (UserController) c01341.L$0;
                ResultKt.throwOnFailure($result);
                objInvoke = $result;
                if (((Boolean) objInvoke).booleanValue()) {
                }
                while (it.hasNext()) {
                }
                if (hasChanged.element) {
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
