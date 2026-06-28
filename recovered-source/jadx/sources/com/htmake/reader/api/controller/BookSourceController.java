package com.htmake.reader.api.controller;

import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.SpringContextUtils;
import com.htmake.reader.utils.VertExtKt;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BookSource;
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.slf4j.MDCContext;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: BookSourceController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010\u000e\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010\u000f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010\u0010\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ&\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\u0016\u001a\u00020\u00142\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0019\u0010\u0019\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\u0016\u001a\u00020\u0014J\u0019\u0010\u001b\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0016\u001a\u00020\u0014J4\u0010\u001d\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0016\u001a\u00020\u00142\u0010\b\u0002\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001f2\u0010\b\u0002\u0010 \u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001fJ\u0019\u0010!\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010\"\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0019\u0010#\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0016\u0010#\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010$\u001a\u00020\u0018J\u0019\u0010%\u001a\u00020&2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ \u0010'\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00142\b\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010$\u001a\u00020\u0018J\u0019\u0010*\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ#\u0010+\u001a\u00020&2\u0006\u0010\u0016\u001a\u00020\u00142\b\u0010,\u001a\u0004\u0018\u00010)H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010-R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006."}, d2 = {"Lcom/htmake/reader/api/controller/BookSourceController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "webClient", "Lio/vertx/ext/web/client/WebClient;", "canEditBookSource", PackageDocumentBase.PREFIX_OPF, "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllBookSources", "Lcom/htmake/reader/api/ReturnData;", "deleteBookSource", "deleteBookSources", "deleteBookSourcesFile", "deleteUserBookSource", "generateBookSourceMap", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "userNameSpace", "bookSourceList", "Lio/vertx/core/json/JsonArray;", "getBookSource", "getBookSourceMap", "getBookSources", "getUserBookSourceJson", "getUserBookSourceJsonOpt", "fields", PackageDocumentBase.PREFIX_OPF, "checkNotEmpty", "readSourceFile", "saveBookSource", "saveBookSources", "bookSourceJsonArray", "saveFromRemoteSource", PackageDocumentBase.PREFIX_OPF, "saveUserBookSources", "userInfo", "Lcom/htmake/reader/entity/User;", "setAsDefaultBookSources", "updateRemoteSourceSub", "user", "(Ljava/lang/String;Lcom/htmake/reader/entity/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class BookSourceController extends BaseController {

    @NotNull
    private WebClient webClient;

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$deleteAllBookSources$1, reason: invalid class name */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$deleteAllBookSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {312, 315}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteAllBookSources", c = "com.htmake.reader.api.controller.BookSourceController")
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
            return BookSourceController.this.deleteAllBookSources(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$deleteBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$deleteBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {240, 243}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteBookSource", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00931 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00931(Continuation<? super C00931> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.deleteBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$deleteBookSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$deleteBookSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {271, 274}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteBookSources", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00941 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00941(Continuation<? super C00941> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.deleteBookSources(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$deleteBookSourcesFile$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$deleteBookSourcesFile$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {440}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteBookSourcesFile", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00951 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00951(Continuation<? super C00951> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.deleteBookSourcesFile(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$deleteUserBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$deleteUserBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {420}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteUserBookSource", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00961 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00961(Continuation<? super C00961> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.deleteUserBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$getBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$getBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {183}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getBookSource", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00971 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00971(Continuation<? super C00971> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.getBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$getBookSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$getBookSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {219}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getBookSources", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00981 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00981(Continuation<? super C00981> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.getBookSources(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$saveBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$saveBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {Wbxml.EXT_I_1, Wbxml.LITERAL_C}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBookSource", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C00991 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00991(Continuation<? super C00991> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.saveBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$saveBookSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$saveBookSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {108, 111}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBookSources", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C01001 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01001(Continuation<? super C01001> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.saveBookSources((RoutingContext) null, (Continuation<? super ReturnData>) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$saveFromRemoteSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$saveFromRemoteSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {363}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveFromRemoteSource", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C01011 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01011(Continuation<? super C01011> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.saveFromRemoteSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$setAsDefaultBookSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$setAsDefaultBookSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {326}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "setAsDefaultBookSources", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C01021 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01021(Continuation<? super C01021> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.setAsDefaultBookSources(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$updateRemoteSourceSub$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$updateRemoteSourceSub$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookSourceController.kt", l = {400}, i = {}, s = {}, n = {}, m = "updateRemoteSourceSub", c = "com.htmake.reader.api.controller.BookSourceController")
    static final class C01031 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int I$0;
        int I$1;
        /* synthetic */ Object result;
        int label;

        C01031(Continuation<? super C01031> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookSourceController.this.updateRemoteSourceSub(null, null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BookSourceController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
        Object bean = SpringContextUtils.getBean("webClient", WebClient.class);
        Intrinsics.checkNotNullExpressionValue(bean, "getBean(\"webClient\", WebClient::class.java)");
        this.webClient = (WebClient) bean;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ JsonArray getUserBookSourceJsonOpt$default(BookSourceController bookSourceController, String str, Set set, Set set2, int i, Object obj) {
        if ((i & 2) != 0) {
            set = null;
        }
        if ((i & 4) != 0) {
            set2 = null;
        }
        return bookSourceController.getUserBookSourceJsonOpt(str, set, set2);
    }

    @Nullable
    public final JsonArray getUserBookSourceJsonOpt(@NotNull String userNameSpace, @Nullable Set<String> fields, @Nullable Set<String> checkNotEmpty) {
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        File bookSourceFile = ExtKt.getStorageFile$default(new String[]{"data", userNameSpace, "bookSource"}, null, 2, null);
        if (!bookSourceFile.exists()) {
            bookSourceFile = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
        }
        JsonArray bookSourceList = ExtKt.parseJsonStringList$default(bookSourceFile, fields, null, 0, 0, checkNotEmpty, null, 92, null);
        return bookSourceList;
    }

    @Nullable
    public final JsonArray getUserBookSourceJson(@NotNull String userNameSpace) {
        JsonArray systemBookSourceList;
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        JsonArray bookSourceList = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookSource"));
        if (bookSourceList == null && !userNameSpace.equals("default") && (systemBookSourceList = ExtKt.asJsonArray(getUserStorage("default", "bookSource"))) != null) {
            bookSourceList = systemBookSourceList;
        }
        return bookSourceList;
    }

    @Nullable
    public final Object canEditBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super Boolean> $completion) {
        if (!getAppConfig().getSecure()) {
            return Boxing.boxBoolean(true);
        }
        User userInfo = (User) context.get("userInfo");
        if (userInfo == null) {
            return Boxing.boxBoolean(false);
        }
        return Boxing.boxBoolean(userInfo.getEnable_book_source());
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00991 c00991;
        ReturnData returnData;
        Object objCanEditBookSource;
        Object objCheckAuth;
        if ($completion instanceof C00991) {
            c00991 = (C00991) $completion;
            if ((c00991.label & Integer.MIN_VALUE) != 0) {
                c00991.label -= Integer.MIN_VALUE;
            } else {
                c00991 = new C00991($completion);
            }
        }
        Object $result = c00991.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00991.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00991.L$0 = this;
                c00991.L$1 = context;
                c00991.L$2 = returnData;
                c00991.label = 1;
                objCheckAuth = checkAuth(context, c00991);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                c00991.L$0 = this;
                c00991.L$1 = context;
                c00991.L$2 = returnData;
                c00991.label = 2;
                objCanEditBookSource = this.canEditBookSource(context, c00991);
                if (objCanEditBookSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                BookSource.Companion companion = BookSource.INSTANCE;
                String bodyAsString = context.getBodyAsString();
                Intrinsics.checkNotNullExpressionValue(bodyAsString, "context.bodyAsString");
                Object objM151fromJsonIoAF18A = companion.m151fromJsonIoAF18A(bodyAsString);
                BookSource bookSource = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                if (bookSource == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                String userNameSpace = this.getUserNameSpace(context);
                JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
                if (bookSourceList == null) {
                    bookSourceList = new JsonArray();
                }
                Map urlMap = new LinkedHashMap();
                int i = 0;
                int size = bookSourceList.size();
                if (0 < size) {
                    do {
                        int i2 = i;
                        i++;
                        String string = bookSourceList.getJsonObject(i2).getString("bookSourceUrl");
                        Intrinsics.checkNotNullExpressionValue(string, "bookSourceList.getJsonObject(i).getString(\"bookSourceUrl\")");
                        urlMap.put(string, Boxing.boxInt(i2));
                    } while (i < size);
                }
                int existIndex = ((Number) urlMap.getOrDefault(bookSource.getBookSourceUrl(), Boxing.boxInt(-1))).intValue();
                if (existIndex >= 0) {
                    List sourceList = bookSourceList.getList();
                    sourceList.set(existIndex, JsonObject.mapFrom(bookSource));
                    bookSourceList = new JsonArray(sourceList);
                } else {
                    User userInfo = (User) context.get("userInfo");
                    if (userInfo != null && bookSourceList.size() >= userInfo.getBook_source_limit()) {
                        return returnData.setErrorMsg("你已达到书源数上限，请联系管理员");
                    }
                    bookSourceList.add(JsonObject.mapFrom(bookSource));
                }
                this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
                this.generateBookSourceMap(userNameSpace, bookSourceList);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) c00991.L$2;
                context = (RoutingContext) c00991.L$1;
                this = (BookSourceController) c00991.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c00991.L$2;
                context = (RoutingContext) c00991.L$1;
                this = (BookSourceController) c00991.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditBookSource = $result;
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01001 c01001;
        ReturnData returnData;
        Object objCanEditBookSource;
        Object objCheckAuth;
        if ($completion instanceof C01001) {
            c01001 = (C01001) $completion;
            if ((c01001.label & Integer.MIN_VALUE) != 0) {
                c01001.label -= Integer.MIN_VALUE;
            } else {
                c01001 = new C01001($completion);
            }
        }
        Object $result = c01001.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01001.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01001.L$0 = this;
                c01001.L$1 = context;
                c01001.L$2 = returnData;
                c01001.label = 1;
                objCheckAuth = checkAuth(context, c01001);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                c01001.L$0 = this;
                c01001.L$1 = context;
                c01001.L$2 = returnData;
                c01001.label = 2;
                objCanEditBookSource = this.canEditBookSource(context, c01001);
                if (objCanEditBookSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                JsonArray bookSourceJsonArray = context.getBodyAsJsonArray();
                if (bookSourceJsonArray == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                return this.saveBookSources(context, bookSourceJsonArray);
            case 1:
                returnData = (ReturnData) c01001.L$2;
                context = (RoutingContext) c01001.L$1;
                this = (BookSourceController) c01001.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c01001.L$2;
                context = (RoutingContext) c01001.L$1;
                this = (BookSourceController) c01001.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditBookSource = $result;
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @NotNull
    public final ReturnData saveBookSources(@NotNull RoutingContext context, @NotNull JsonArray bookSourceJsonArray) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bookSourceJsonArray, "bookSourceJsonArray");
        return saveUserBookSources(getUserNameSpace(context), (User) context.get("userInfo"), bookSourceJsonArray);
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0188 A[EDGE_INSN: B:45:0x0188->B:35:0x0188 BREAK  A[LOOP:1: B:11:0x00ac->B:47:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[LOOP:1: B:11:0x00ac->B:47:?, LOOP_END, SYNTHETIC] */
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final ReturnData saveUserBookSources(@NotNull String userNameSpace, @Nullable User userInfo, @NotNull JsonArray bookSourceJsonArray) {
        BookSource bookSource;
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        Intrinsics.checkNotNullParameter(bookSourceJsonArray, "bookSourceJsonArray");
        ReturnData returnData = new ReturnData();
        JsonArray bookSourceList = getUserBookSourceJson(userNameSpace);
        if (bookSourceList == null) {
            bookSourceList = new JsonArray();
        }
        Map urlMap = new LinkedHashMap();
        int i = 0;
        int size = bookSourceList.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String string = bookSourceList.getJsonObject(i2).getString("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue(string, "bookSourceList.getJsonObject(i).getString(\"bookSourceUrl\")");
                urlMap.put(string, Integer.valueOf(i2));
            } while (i < size);
        }
        boolean isOverLimit = false;
        int addCnt = 0;
        int maxIndex = bookSourceList.size() - 1;
        Set updateIndex = new LinkedHashSet();
        int i3 = 0;
        int size2 = bookSourceJsonArray.size();
        if (0 < size2) {
            while (true) {
                int k = i3;
                i3++;
                try {
                    BookSource.Companion companion = BookSource.INSTANCE;
                    String string2 = bookSourceJsonArray.getJsonObject(k).toString();
                    Intrinsics.checkNotNullExpressionValue(string2, "bookSourceJsonArray.getJsonObject(k).toString()");
                    Object objM151fromJsonIoAF18A = companion.m151fromJsonIoAF18A(string2);
                    bookSource = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                } catch (Exception e) {
                    bookSource = (BookSource) null;
                }
                BookSource bookSource2 = bookSource;
                if (bookSource2 == null) {
                    if (i3 < size2) {
                    }
                } else {
                    int existIndex = ((Number) urlMap.getOrDefault(bookSource2.getBookSourceUrl(), -1)).intValue();
                    if (existIndex >= 0) {
                        bookSourceList.set(existIndex, JsonObject.mapFrom(bookSource2));
                        if (existIndex <= maxIndex) {
                            updateIndex.add(Integer.valueOf(existIndex));
                        }
                    } else {
                        if (userInfo != null && bookSourceList.size() >= userInfo.getBook_source_limit()) {
                            isOverLimit = true;
                            break;
                        }
                        addCnt++;
                        bookSourceList.add(JsonObject.mapFrom(bookSource2));
                        urlMap.put(bookSource2.getBookSourceUrl(), Integer.valueOf(bookSourceList.size() - 1));
                    }
                    if (i3 < size2) {
                        break;
                    }
                }
            }
        }
        saveUserStorage(userNameSpace, "bookSource", bookSourceList);
        generateBookSourceMap(userNameSpace, bookSourceList);
        String tip = "新增" + addCnt + "条书源，更新" + updateIndex.size() + "条书源";
        if (isOverLimit) {
            return returnData.setErrorMsg(Intrinsics.stringPlus(tip, "。你已达到书源数上限，请联系管理员"));
        }
        return returnData.setData(PackageDocumentBase.PREFIX_OPF, tip);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00971 c00971;
        ReturnData returnData;
        String bookSourceUrl;
        if ($completion instanceof C00971) {
            c00971 = (C00971) $completion;
            if ((c00971.label & Integer.MIN_VALUE) != 0) {
                c00971.label -= Integer.MIN_VALUE;
            } else {
                c00971 = new C00971($completion);
            }
        }
        Object $result = c00971.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00971.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00971.L$0 = this;
                c00971.L$1 = context;
                c00971.L$2 = returnData;
                c00971.label = 1;
                if (checkAuth(context, c00971) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00971.L$2;
                context = (RoutingContext) c00971.L$1;
                this = (BookSourceController) c00971.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString("bookSourceUrl");
            Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"bookSourceUrl\")");
            bookSourceUrl = string;
        } else {
            List listQueryParam = context.queryParam("bookSourceUrl");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookSourceUrl\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            bookSourceUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (bookSourceUrl.length() == 0) {
            return returnData.setErrorMsg("书源链接不能为空");
        }
        String userNameSpace = this.getUserNameSpace(context);
        int existIndex = this.getBookSourceMap(userNameSpace).getOrDefault(bookSourceUrl, Boxing.boxInt(-1)).intValue();
        if (existIndex < 0) {
            return returnData.setErrorMsg("书源信息不存在");
        }
        File bookSourceFile = ExtKt.getStorageFile$default(new String[]{"data", userNameSpace, "bookSource"}, null, 2, null);
        if (!bookSourceFile.exists()) {
            bookSourceFile = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
        }
        JsonArray bookSourceList = ExtKt.parseJsonStringList$default(bookSourceFile, null, null, existIndex, existIndex, null, null, 102, null);
        if (bookSourceList == null) {
            return returnData.setErrorMsg("书源信息不存在");
        }
        Map map = new JsonObject(bookSourceList.getString(0)).getMap();
        Intrinsics.checkNotNullExpressionValue(map, "JsonObject(bookSourceList.getString(0)).map");
        return ReturnData.setData$default(returnData, map, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00981 c00981;
        ReturnData returnData;
        Integer numBoxInt;
        int simple;
        if ($completion instanceof C00981) {
            c00981 = (C00981) $completion;
            if ((c00981.label & Integer.MIN_VALUE) != 0) {
                c00981.label -= Integer.MIN_VALUE;
            } else {
                c00981 = new C00981($completion);
            }
        }
        Object $result = c00981.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00981.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00981.L$0 = this;
                c00981.L$1 = context;
                c00981.L$2 = returnData;
                c00981.label = 1;
                if (checkAuth(context, c00981) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00981.L$2;
                context = (RoutingContext) c00981.L$1;
                this = (BookSourceController) c00981.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (context.request().method() == HttpMethod.POST) {
            Integer integer = context.getBodyAsJson().getInteger("simple", Boxing.boxInt(0));
            Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"simple\", 0)");
            simple = integer.intValue();
        } else {
            List listQueryParam = context.queryParam("simple");
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"simple\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            int iIntValue = (str == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str))) == null) ? 0 : numBoxInt.intValue();
            simple = iIntValue;
        }
        String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookSourceList = this.getUserBookSourceJsonOpt(userNameSpace, simple > 0 ? SetsKt.setOf(new String[]{"bookSourceGroup", "bookSourceName", "bookSourceUrl"}) : null, simple > 0 ? SetsKt.setOf("exploreUrl") : null);
        if (bookSourceList != null) {
            ReturnData returnData2 = returnData;
            Iterable list = bookSourceList.getList();
            Intrinsics.checkNotNullExpressionValue(list, "bookSourceList.list");
            Iterable $this$map$iv = list;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (Object item$iv$iv : $this$map$iv) {
                if (item$iv$iv == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                }
                destination$iv$iv.add(new JsonObject((String) item$iv$iv).getMap());
            }
            return ReturnData.setData$default(returnData2, (List) destination$iv$iv, null, 2, null);
        }
        return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00931 c00931;
        ReturnData returnData;
        Object objCanEditBookSource;
        Object objCheckAuth;
        if ($completion instanceof C00931) {
            c00931 = (C00931) $completion;
            if ((c00931.label & Integer.MIN_VALUE) != 0) {
                c00931.label -= Integer.MIN_VALUE;
            } else {
                c00931 = new C00931($completion);
            }
        }
        Object $result = c00931.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00931.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00931.L$0 = this;
                c00931.L$1 = context;
                c00931.L$2 = returnData;
                c00931.label = 1;
                objCheckAuth = checkAuth(context, c00931);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                c00931.L$0 = this;
                c00931.L$1 = context;
                c00931.L$2 = returnData;
                c00931.label = 2;
                objCanEditBookSource = this.canEditBookSource(context, c00931);
                if (objCanEditBookSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                BookSource.Companion companion = BookSource.INSTANCE;
                String bodyAsString = context.getBodyAsString();
                Intrinsics.checkNotNullExpressionValue(bodyAsString, "context.bodyAsString");
                Object objM151fromJsonIoAF18A = companion.m151fromJsonIoAF18A(bodyAsString);
                BookSource bookSource = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                if (bookSource == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                String userNameSpace = this.getUserNameSpace(context);
                JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
                if (bookSourceList == null) {
                    bookSourceList = new JsonArray();
                }
                int existIndex = this.getBookSourceMap(userNameSpace).getOrDefault(bookSource.getBookSourceUrl(), Boxing.boxInt(-1)).intValue();
                if (existIndex >= 0) {
                    bookSourceList.remove(existIndex);
                }
                this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
                this.generateBookSourceMap(userNameSpace, bookSourceList);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) c00931.L$2;
                context = (RoutingContext) c00931.L$1;
                this = (BookSourceController) c00931.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c00931.L$2;
                context = (RoutingContext) c00931.L$1;
                this = (BookSourceController) c00931.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditBookSource = $result;
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteBookSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00941 c00941;
        ReturnData returnData;
        Object objCanEditBookSource;
        Object objCheckAuth;
        if ($completion instanceof C00941) {
            c00941 = (C00941) $completion;
            if ((c00941.label & Integer.MIN_VALUE) != 0) {
                c00941.label -= Integer.MIN_VALUE;
            } else {
                c00941 = new C00941($completion);
            }
        }
        Object $result = c00941.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00941.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00941.L$0 = this;
                c00941.L$1 = context;
                c00941.L$2 = returnData;
                c00941.label = 1;
                objCheckAuth = checkAuth(context, c00941);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                c00941.L$0 = this;
                c00941.L$1 = context;
                c00941.L$2 = returnData;
                c00941.label = 2;
                objCanEditBookSource = this.canEditBookSource(context, c00941);
                if (objCanEditBookSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                JsonArray bookSourceJsonArray = context.getBodyAsJsonArray();
                String userNameSpace = this.getUserNameSpace(context);
                JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
                if (bookSourceList == null) {
                    bookSourceList = new JsonArray();
                }
                int i = 0;
                int size = bookSourceJsonArray.size();
                if (0 < size) {
                    do {
                        int k = i;
                        i++;
                        String bookSourceUrl = bookSourceJsonArray.getJsonObject(k).getString("bookSourceUrl");
                        String str = bookSourceUrl;
                        if (!(str == null || str.length() == 0)) {
                            int existIndex = -1;
                            int i2 = 0;
                            int size2 = bookSourceList.size();
                            if (0 < size2) {
                                while (true) {
                                    int i3 = i2;
                                    i2++;
                                    String _bookSourceUrl = bookSourceList.getJsonObject(i3).getString("bookSourceUrl");
                                    if (!bookSourceUrl.equals(_bookSourceUrl)) {
                                        if (i2 >= size2) {
                                        }
                                    } else {
                                        existIndex = i3;
                                    }
                                }
                            }
                            if (existIndex >= 0) {
                                bookSourceList.remove(existIndex);
                            }
                        }
                    } while (i < size);
                }
                this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
                this.generateBookSourceMap(userNameSpace, bookSourceList);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) c00941.L$2;
                context = (RoutingContext) c00941.L$1;
                this = (BookSourceController) c00941.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c00941.L$2;
                context = (RoutingContext) c00941.L$1;
                this = (BookSourceController) c00941.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditBookSource = $result;
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteAllBookSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        AnonymousClass1 anonymousClass1;
        ReturnData returnData;
        Object objCanEditBookSource;
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
                anonymousClass1.L$0 = this;
                anonymousClass1.L$1 = context;
                anonymousClass1.L$2 = returnData;
                anonymousClass1.label = 2;
                objCanEditBookSource = this.canEditBookSource(context, anonymousClass1);
                if (objCanEditBookSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                String userNameSpace = this.getUserNameSpace(context);
                this.saveUserStorage(userNameSpace, "bookSource", new JsonArray());
                this.generateBookSourceMap(userNameSpace, new JsonArray());
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (BookSourceController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (BookSourceController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditBookSource = $result;
                if (((Boolean) objCanEditBookSource).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object setAsDefaultBookSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01021 c01021;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01021) {
            c01021 = (C01021) $completion;
            if ((c01021.label & Integer.MIN_VALUE) != 0) {
                c01021.label -= Integer.MIN_VALUE;
            } else {
                c01021 = new C01021($completion);
            }
        }
        Object $result = c01021.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01021.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01021.L$0 = this;
                c01021.L$1 = context;
                c01021.L$2 = returnData;
                c01021.label = 1;
                objCheckAuth = checkAuth(context, c01021);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01021.L$2;
                context = (RoutingContext) c01021.L$1;
                this = (BookSourceController) c01021.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
        }
        String username = context.getBodyAsJson().getString("username");
        Intrinsics.checkNotNullExpressionValue(username, "username");
        JsonArray bookSourceList = ExtKt.asJsonArray(this.getUserStorage(username, "bookSource"));
        if (bookSourceList == null) {
            return returnData.setErrorMsg("用户书源不存在");
        }
        List list = bookSourceList.getList();
        Intrinsics.checkNotNullExpressionValue(list, "bookSourceList.getList()");
        this.saveUserStorage("default", "bookSource", list);
        this.generateBookSourceMap("default", bookSourceList);
        return ReturnData.setData$default(returnData, "设置默认书源成功", null, 2, null);
    }

    @Nullable
    public final Object readSourceFile(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        ReturnData returnData = new ReturnData();
        if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
            return returnData.setErrorMsg("请上传文件");
        }
        JsonArray jsonArray = new JsonArray();
        Iterable iterableFileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue(iterableFileUploads, "context.fileUploads()");
        Iterable $this$forEach$iv = iterableFileUploads;
        for (Object element$iv : $this$forEach$iv) {
            FileUpload it = (FileUpload) element$iv;
            File file = new File(it.uploadedFileName());
            if (file.exists()) {
                jsonArray.add(FilesKt.readText$default(file, (Charset) null, 1, (Object) null));
                file.delete();
            }
        }
        List list = jsonArray.getList();
        Intrinsics.checkNotNullExpressionValue(list, "sourceList.getList()");
        return ReturnData.setData$default(returnData, list, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveFromRemoteSource(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C01011 c01011;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01011) {
            c01011 = (C01011) $completion;
            if ((c01011.label & Integer.MIN_VALUE) != 0) {
                c01011.label -= Integer.MIN_VALUE;
            } else {
                c01011 = new C01011($completion);
            }
        }
        Object $result = c01011.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01011.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01011.L$0 = this;
                c01011.L$1 = context;
                c01011.L$2 = returnData;
                c01011.label = 1;
                objCheckAuth = checkAuth(context, c01011);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01011.L$2;
                context = (RoutingContext) c01011.L$1;
                this = (BookSourceController) c01011.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
            return Unit.INSTANCE;
        }
        Ref.ObjectRef url = new Ref.ObjectRef();
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
            url.element = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        } else {
            List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            url.element = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        CharSequence charSequence = (CharSequence) url.element;
        if (charSequence == null || charSequence.length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("请输入远程书源链接"));
            return Unit.INSTANCE;
        }
        BuildersKt.launch$default(this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()), (CoroutineStart) null, this.new AnonymousClass2(url, context, returnData, null), 2, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookSourceController$saveFromRemoteSource$2, reason: invalid class name */
    /* JADX INFO: compiled from: BookSourceController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookSourceController$saveFromRemoteSource$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BookSourceController.kt", l = {}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookSourceController$saveFromRemoteSource$2")
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        final /* synthetic */ Ref.ObjectRef<String> $url;
        final /* synthetic */ RoutingContext $context;
        final /* synthetic */ ReturnData $returnData;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Ref.ObjectRef<String> $url, RoutingContext $context, ReturnData $returnData, Continuation<? super AnonymousClass2> $completion) {
            super(2, $completion);
            this.$url = $url;
            this.$context = $context;
            this.$returnData = $returnData;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return BookSourceController.this.new AnonymousClass2(this.$url, this.$context, this.$returnData, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    HttpRequest httpRequestTimeout = BookSourceController.this.webClient.getAbs((String) this.$url.element).timeout(3000L);
                    RoutingContext routingContext = this.$context;
                    BookSourceController bookSourceController = BookSourceController.this;
                    ReturnData returnData = this.$returnData;
                    httpRequestTimeout.send((v3) -> {
                        m34invokeSuspend$lambda0(r1, r2, r3, v3);
                    });
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }

        /* JADX INFO: renamed from: invokeSuspend$lambda-0, reason: not valid java name */
        private static final void m34invokeSuspend$lambda0(RoutingContext $context, BookSourceController this$0, ReturnData $returnData, AsyncResult it) {
            HttpResponse httpResponse = (HttpResponse) it.result();
            JsonArray body = httpResponse == null ? null : httpResponse.bodyAsJsonArray();
            if (body != null) {
                VertExtKt.success($context, this$0.saveBookSources($context, body));
            } else {
                VertExtKt.success($context, $returnData.setErrorMsg("远程书源链接错误"));
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x00a3, code lost:

        if (0 < r16) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00a6, code lost:

        r0 = r15;
        r15 = r15 + 1;
        r0 = new kotlin.jvm.internal.Ref.ObjectRef();
        r0.element = ((io.vertx.core.json.JsonArray) r14.element).getJsonObject(r0);
        r0 = new kotlin.jvm.internal.Ref.ObjectRef();
        r0.element = ((io.vertx.core.json.JsonObject) r0.element).getString("link");
        r0 = (java.lang.CharSequence) r0.element;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00f6, code lost:

        if (r0 == null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0100, code lost:

        if (r0.length() != 0) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0103, code lost:

        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0107, code lost:

        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0108, code lost:

        if (r0 == false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x010e, code lost:

        r4 = r11;
        r5 = r12;
        r6 = r14;
        r24.L$0 = r10;
        r24.L$1 = r11;
        r24.L$2 = r12;
        r24.L$3 = r14;
        r24.I$0 = r15;
        r24.I$1 = r16;
        r24.label = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0158, code lost:

        if (io.vertx.kotlin.coroutines.VertxCoroutineKt.awaitResult(new com.htmake.reader.api.controller.BookSourceController.C01042(r10), r24) != r0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x015d, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x019d, code lost:

        if (r15 >= r16) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x01a0, code lost:

        generateBookSourceMap$default(r10, r11, null, 2, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x01ac, code lost:

        return kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x019d -> B:16:0x00a6). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object updateRemoteSourceSub(@NotNull String userNameSpace, @Nullable User user, @NotNull Continuation<? super Unit> $completion) {
        C01031 c01031;
        int size;
        int i;
        Ref.ObjectRef remoteBookSourceList;
        if ($completion instanceof C01031) {
            c01031 = (C01031) $completion;
            if ((c01031.label & Integer.MIN_VALUE) != 0) {
                c01031.label -= Integer.MIN_VALUE;
            } else {
                c01031 = new C01031($completion);
            }
        }
        Object $result = c01031.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01031.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                remoteBookSourceList = new Ref.ObjectRef();
                JsonArray jsonArrayAsJsonArray = ExtKt.asJsonArray(getUserStorage(userNameSpace, "remoteBookSourceSub"));
                if (jsonArrayAsJsonArray == null) {
                    return Unit.INSTANCE;
                }
                remoteBookSourceList.element = jsonArrayAsJsonArray;
                i = 0;
                size = ((JsonArray) remoteBookSourceList.element).size();
                break;
            case 1:
                size = c01031.I$1;
                i = c01031.I$0;
                remoteBookSourceList = (Ref.ObjectRef) c01031.L$3;
                user = (User) c01031.L$2;
                userNameSpace = (String) c01031.L$1;
                this = (BookSourceController) c01031.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteUserBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00961 c00961;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00961) {
            c00961 = (C00961) $completion;
            if ((c00961.label & Integer.MIN_VALUE) != 0) {
                c00961.label -= Integer.MIN_VALUE;
            } else {
                c00961 = new C00961($completion);
            }
        }
        Object $result = c00961.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00961.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00961.L$0 = this;
                c00961.L$1 = context;
                c00961.L$2 = returnData;
                c00961.label = 1;
                objCheckAuth = checkAuth(context, c00961);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00961.L$2;
                context = (RoutingContext) c00961.L$1;
                this = (BookSourceController) c00961.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
        }
        JsonArray userJsonArray = context.getBodyAsJsonArray();
        int i = 0;
        int size = userJsonArray.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String username = userJsonArray.getString(i2);
                Intrinsics.checkNotNullExpressionValue(username, "username");
                File userBookSourceFile = new File(ExtKt.getWorkDir("storage", "data", username, "bookSource.json"));
                if (userBookSourceFile.exists()) {
                    ExtKt.deleteRecursively(userBookSourceFile);
                }
            } while (i < size);
        }
        return ReturnData.setData$default(returnData, "删除书源成功", null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteBookSourcesFile(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00951 c00951;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00951) {
            c00951 = (C00951) $completion;
            if ((c00951.label & Integer.MIN_VALUE) != 0) {
                c00951.label -= Integer.MIN_VALUE;
            } else {
                c00951 = new C00951($completion);
            }
        }
        Object $result = c00951.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00951.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00951.L$0 = this;
                c00951.L$1 = context;
                c00951.L$2 = returnData;
                c00951.label = 1;
                objCheckAuth = checkAuth(context, c00951);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00951.L$2;
                context = (RoutingContext) c00951.L$1;
                this = (BookSourceController) c00951.L$0;
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
        File userBookSourceFile = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "bookSource.json"));
        if (userBookSourceFile.exists()) {
            ExtKt.deleteRecursively(userBookSourceFile);
        }
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    public static /* synthetic */ Map generateBookSourceMap$default(BookSourceController bookSourceController, String str, JsonArray jsonArray, int i, Object obj) {
        if ((i & 2) != 0) {
            jsonArray = null;
        }
        return bookSourceController.generateBookSourceMap(str, jsonArray);
    }

    @NotNull
    public final Map<String, Integer> generateBookSourceMap(@NotNull String userNameSpace, @Nullable JsonArray bookSourceList) {
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        JsonArray bookSourceJsonArray = bookSourceList != null ? bookSourceList : getUserBookSourceJson(userNameSpace);
        if (bookSourceJsonArray == null) {
            bookSourceJsonArray = new JsonArray();
        }
        Map urlMap = new LinkedHashMap();
        List exploreList = new ArrayList();
        int i = 0;
        int size = bookSourceJsonArray.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String string = bookSourceJsonArray.getJsonObject(i2).getString("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue(string, "bookSourceJsonArray.getJsonObject(i).getString(\"bookSourceUrl\")");
                urlMap.put(string, Integer.valueOf(i2));
                String string2 = bookSourceJsonArray.getJsonObject(i2).getString("exploreUrl");
                if (!(string2 == null || string2.length() == 0)) {
                    exploreList.add(MapsKt.mutableMapOf(new Pair[]{TuplesKt.to("bookSourceUrl", bookSourceJsonArray.getJsonObject(i2).getString("bookSourceUrl")), TuplesKt.to("bookSourceGroup", bookSourceJsonArray.getJsonObject(i2).getString("bookSourceGroup")), TuplesKt.to("bookSourceName", bookSourceJsonArray.getJsonObject(i2).getString("bookSourceName"))}));
                }
            } while (i < size);
        }
        saveUserStorage(userNameSpace, "bookSourceMap", urlMap);
        saveUserStorage(userNameSpace, "bookSourceExploreList", exploreList);
        return urlMap;
    }

    @NotNull
    public final Map<String, Integer> getBookSourceMap(@NotNull String userNameSpace) {
        String userStorage;
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if (ExtKt.getStorageFile$default(new String[]{"data", userNameSpace, "bookSource"}, null, 2, null).exists()) {
            userStorage = getUserStorage(userNameSpace, "bookSourceMap");
        } else {
            userStorage = getUserStorage("default", "bookSourceMap");
        }
        String content = userStorage;
        String str = content;
        if (str == null || str.length() == 0) {
            if (ExtKt.getStorageFile$default(new String[]{"data", userNameSpace, "bookSource"}, null, 2, null).exists()) {
                return generateBookSourceMap$default(this, userNameSpace, null, 2, null);
            }
            return generateBookSourceMap$default(this, "default", null, 2, null);
        }
        JsonObject jsonObjectAsJsonObject = ExtKt.asJsonObject(content);
        Map map = jsonObjectAsJsonObject == null ? null : jsonObjectAsJsonObject.getMap();
        if (map == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.Int>");
        }
        return TypeIntrinsics.asMutableMap(map);
    }
}
