package com.htmake.reader.api.controller;

import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.RssArticle;
import io.legado.app.data.entities.RssSource;
import io.legado.app.model.Debug;
import io.legado.app.model.rss.Rss;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: RssSourceController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u0019\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u0019\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u0019\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011J\u0019\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u0019\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\tJ\u0019\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0016"}, d2 = {"Lcom/htmake/reader/api/controller/RssSourceController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "canEditRssSource", PackageDocumentBase.PREFIX_OPF, "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRssSource", "Lcom/htmake/reader/api/ReturnData;", "getRssArticles", "getRssContent", "getRssSourceByURL", "Lio/legado/app/data/entities/RssSource;", RSSKeywords.RSS_ITEM_URL, PackageDocumentBase.PREFIX_OPF, "userNameSpace", "getRssSources", "saveRssSource", "saveRssSources", "reader-pro"})
public final class RssSourceController extends BaseController {

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.RssSourceController$deleteRssSource$1, reason: invalid class name */
    /* JADX INFO: compiled from: RssSourceController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController$deleteRssSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RssSourceController.kt", l = {198, 201}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteRssSource", c = "com.htmake.reader.api.controller.RssSourceController")
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
            return RssSourceController.this.deleteRssSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.RssSourceController$getRssArticles$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: RssSourceController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController$getRssArticles$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RssSourceController.kt", l = {249, 282}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getRssArticles", c = "com.htmake.reader.api.controller.RssSourceController")
    static final class C01251 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01251(Continuation<? super C01251> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RssSourceController.this.getRssArticles(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.RssSourceController$getRssContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: RssSourceController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController$getRssContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RssSourceController.kt", l = {289, 324}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getRssContent", c = "com.htmake.reader.api.controller.RssSourceController")
    static final class C01261 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01261(Continuation<? super C01261> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RssSourceController.this.getRssContent(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.RssSourceController$getRssSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: RssSourceController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController$getRssSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RssSourceController.kt", l = {77}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getRssSources", c = "com.htmake.reader.api.controller.RssSourceController")
    static final class C01271 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01271(Continuation<? super C01271> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RssSourceController.this.getRssSources(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.RssSourceController$saveRssSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: RssSourceController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController$saveRssSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RssSourceController.kt", l = {101, 104}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveRssSource", c = "com.htmake.reader.api.controller.RssSourceController")
    static final class C01281 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01281(Continuation<? super C01281> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RssSourceController.this.saveRssSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.RssSourceController$saveRssSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: RssSourceController.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/RssSourceController$saveRssSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RssSourceController.kt", l = {148, 151}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveRssSources", c = "com.htmake.reader.api.controller.RssSourceController")
    static final class C01291 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01291(Continuation<? super C01291> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RssSourceController.this.saveRssSources(null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RssSourceController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getRssSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01271 c01271;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C01271) {
            c01271 = (C01271) $completion;
            if ((c01271.label & Integer.MIN_VALUE) != 0) {
                c01271.label -= Integer.MIN_VALUE;
            } else {
                c01271 = new C01271($completion);
            }
        }
        Object $result = c01271.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01271.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01271.L$0 = this;
                c01271.L$1 = context;
                c01271.L$2 = returnData;
                c01271.label = 1;
                objCheckAuth = checkAuth(context, c01271);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c01271.L$2;
                context = (RoutingContext) c01271.L$1;
                this = (RssSourceController) c01271.L$0;
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
        JsonArray list = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
        if (list != null) {
            List list2 = list.getList();
            Intrinsics.checkNotNullExpressionValue(list2, "list.getList()");
            return ReturnData.setData$default(returnData, list2, null, 2, null);
        }
        return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
    }

    @Nullable
    public final Object canEditRssSource(@NotNull RoutingContext context, @NotNull Continuation<? super Boolean> $completion) {
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
    public final Object saveRssSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01281 c01281;
        ReturnData returnData;
        Object objCanEditRssSource;
        Object objCheckAuth;
        if ($completion instanceof C01281) {
            c01281 = (C01281) $completion;
            if ((c01281.label & Integer.MIN_VALUE) != 0) {
                c01281.label -= Integer.MIN_VALUE;
            } else {
                c01281 = new C01281($completion);
            }
        }
        Object $result = c01281.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01281.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01281.L$0 = this;
                c01281.L$1 = context;
                c01281.L$2 = returnData;
                c01281.label = 1;
                objCheckAuth = checkAuth(context, c01281);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                c01281.L$0 = this;
                c01281.L$1 = context;
                c01281.L$2 = returnData;
                c01281.label = 2;
                objCanEditRssSource = this.canEditRssSource(context, c01281);
                if (objCanEditRssSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditRssSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                RssSource.Companion companion = RssSource.INSTANCE;
                String bodyAsString = context.getBodyAsString();
                Intrinsics.checkNotNullExpressionValue(bodyAsString, "context.bodyAsString");
                Object objM162fromJsonIoAF18A = companion.m162fromJsonIoAF18A(bodyAsString);
                RssSource rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A) ? null : objM162fromJsonIoAF18A);
                if (rssSource == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                if (rssSource.getSourceUrl().length() == 0) {
                    return returnData.setErrorMsg("RSS链接不能为空");
                }
                if (rssSource.getSourceName().length() == 0) {
                    return returnData.setErrorMsg("RSS名称不能为空");
                }
                String userNameSpace = this.getUserNameSpace(context);
                JsonArray rssSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
                if (rssSourceList == null) {
                    rssSourceList = new JsonArray();
                }
                int existIndex = -1;
                int i = 0;
                int size = rssSourceList.size();
                if (0 < size) {
                    while (true) {
                        int i2 = i;
                        i++;
                        RssSource.Companion companion2 = RssSource.INSTANCE;
                        String string = rssSourceList.getJsonObject(i2).toString();
                        Intrinsics.checkNotNullExpressionValue(string, "rssSourceList.getJsonObject(i).toString()");
                        Object objM162fromJsonIoAF18A2 = companion2.m162fromJsonIoAF18A(string);
                        RssSource _rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A2) ? null : objM162fromJsonIoAF18A2);
                        if (_rssSource == null || !_rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                            if (i >= size) {
                            }
                        } else {
                            existIndex = i2;
                        }
                    }
                }
                if (existIndex >= 0) {
                    List list = rssSourceList.getList();
                    list.set(existIndex, JsonObject.mapFrom(rssSource));
                    rssSourceList = new JsonArray(list);
                } else {
                    rssSourceList.add(JsonObject.mapFrom(rssSource));
                }
                this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) c01281.L$2;
                context = (RoutingContext) c01281.L$1;
                this = (RssSourceController) c01281.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c01281.L$2;
                context = (RoutingContext) c01281.L$1;
                this = (RssSourceController) c01281.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditRssSource = $result;
                if (((Boolean) objCanEditRssSource).booleanValue()) {
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
    public final Object saveRssSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01291 c01291;
        ReturnData returnData;
        Object objCanEditRssSource;
        Object objCheckAuth;
        if ($completion instanceof C01291) {
            c01291 = (C01291) $completion;
            if ((c01291.label & Integer.MIN_VALUE) != 0) {
                c01291.label -= Integer.MIN_VALUE;
            } else {
                c01291 = new C01291($completion);
            }
        }
        Object $result = c01291.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01291.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01291.L$0 = this;
                c01291.L$1 = context;
                c01291.L$2 = returnData;
                c01291.label = 1;
                objCheckAuth = checkAuth(context, c01291);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                c01291.L$0 = this;
                c01291.L$1 = context;
                c01291.L$2 = returnData;
                c01291.label = 2;
                objCanEditRssSource = this.canEditRssSource(context, c01291);
                if (objCanEditRssSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditRssSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                JsonArray rssSourceJsonArray = context.getBodyAsJsonArray();
                if (rssSourceJsonArray == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                String userNameSpace = this.getUserNameSpace(context);
                JsonArray rssSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
                if (rssSourceList == null) {
                    rssSourceList = new JsonArray();
                }
                int i = 0;
                int size = rssSourceJsonArray.size();
                if (0 < size) {
                    do {
                        int k = i;
                        i++;
                        RssSource.Companion companion = RssSource.INSTANCE;
                        String string = rssSourceJsonArray.getJsonObject(k).toString();
                        Intrinsics.checkNotNullExpressionValue(string, "rssSourceJsonArray.getJsonObject(k).toString()");
                        Object objM162fromJsonIoAF18A = companion.m162fromJsonIoAF18A(string);
                        RssSource rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A) ? null : objM162fromJsonIoAF18A);
                        if (rssSource != null) {
                            if (!(rssSource.getSourceUrl().length() == 0)) {
                                if (!(rssSource.getSourceName().length() == 0)) {
                                    int existIndex = -1;
                                    int i2 = 0;
                                    int size2 = rssSourceList.size();
                                    if (0 < size2) {
                                        while (true) {
                                            int i3 = i2;
                                            i2++;
                                            RssSource.Companion companion2 = RssSource.INSTANCE;
                                            String string2 = rssSourceList.getJsonObject(i3).toString();
                                            Intrinsics.checkNotNullExpressionValue(string2, "rssSourceList.getJsonObject(i).toString()");
                                            Object objM162fromJsonIoAF18A2 = companion2.m162fromJsonIoAF18A(string2);
                                            RssSource _rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A2) ? null : objM162fromJsonIoAF18A2);
                                            if (_rssSource == null || !_rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                                                if (i2 >= size2) {
                                                }
                                            } else {
                                                existIndex = i3;
                                            }
                                        }
                                    }
                                    if (existIndex >= 0) {
                                        List list = rssSourceList.getList();
                                        list.set(existIndex, JsonObject.mapFrom(rssSource));
                                        rssSourceList = new JsonArray(list);
                                    } else {
                                        rssSourceList.add(JsonObject.mapFrom(rssSource));
                                    }
                                }
                            }
                        }
                    } while (i < size);
                }
                this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) c01291.L$2;
                context = (RoutingContext) c01291.L$1;
                this = (RssSourceController) c01291.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c01291.L$2;
                context = (RoutingContext) c01291.L$1;
                this = (RssSourceController) c01291.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditRssSource = $result;
                if (((Boolean) objCanEditRssSource).booleanValue()) {
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
    public final Object deleteRssSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        AnonymousClass1 anonymousClass1;
        ReturnData returnData;
        Object objCanEditRssSource;
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
                objCanEditRssSource = this.canEditRssSource(context, anonymousClass1);
                if (objCanEditRssSource == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCanEditRssSource).booleanValue()) {
                    return returnData.setErrorMsg("权限不足");
                }
                RssSource.Companion companion = RssSource.INSTANCE;
                String bodyAsString = context.getBodyAsString();
                Intrinsics.checkNotNullExpressionValue(bodyAsString, "context.bodyAsString");
                Object objM162fromJsonIoAF18A = companion.m162fromJsonIoAF18A(bodyAsString);
                RssSource rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A) ? null : objM162fromJsonIoAF18A);
                if (rssSource == null) {
                    return returnData.setErrorMsg("参数错误");
                }
                String userNameSpace = this.getUserNameSpace(context);
                JsonArray rssSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
                if (rssSourceList == null) {
                    rssSourceList = new JsonArray();
                }
                int existIndex = -1;
                int i = 0;
                int size = rssSourceList.size();
                if (0 < size) {
                    while (true) {
                        int i2 = i;
                        i++;
                        RssSource.Companion companion2 = RssSource.INSTANCE;
                        String string = rssSourceList.getJsonObject(i2).toString();
                        Intrinsics.checkNotNullExpressionValue(string, "rssSourceList.getJsonObject(i).toString()");
                        Object objM162fromJsonIoAF18A2 = companion2.m162fromJsonIoAF18A(string);
                        RssSource _rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A2) ? null : objM162fromJsonIoAF18A2);
                        if (_rssSource == null || !_rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                            if (i >= size) {
                            }
                        } else {
                            existIndex = i2;
                        }
                    }
                }
                if (existIndex >= 0) {
                    rssSourceList.remove(existIndex);
                }
                this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 1:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (RssSourceController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (RssSourceController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCanEditRssSource = $result;
                if (((Boolean) objCanEditRssSource).booleanValue()) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @Nullable
    public final RssSource getRssSourceByURL(@NotNull String url, @NotNull String userNameSpace) {
        JsonArray list;
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if ((url.length() == 0) || (list = ExtKt.asJsonArray(getUserStorage(userNameSpace, "rssSources"))) == null) {
            return null;
        }
        int i = 0;
        int size = list.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                RssSource.Companion companion = RssSource.INSTANCE;
                String string = list.getJsonObject(i2).toString();
                Intrinsics.checkNotNullExpressionValue(string, "list.getJsonObject(i).toString()");
                Object objM162fromJsonIoAF18A = companion.m162fromJsonIoAF18A(string);
                RssSource _rssSource = (RssSource) (Result.isFailure-impl(objM162fromJsonIoAF18A) ? null : objM162fromJsonIoAF18A);
                if (_rssSource != null && _rssSource.getSourceUrl().equals(url)) {
                    return _rssSource;
                }
            } while (i < size);
            return null;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getRssArticles(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01251 c01251;
        ReturnData returnData;
        Object articles;
        Object objCheckAuth;
        String sourceUrl;
        String sortName;
        String sortUrl;
        Integer numBoxInt;
        int page;
        if ($completion instanceof C01251) {
            c01251 = (C01251) $completion;
            if ((c01251.label & Integer.MIN_VALUE) != 0) {
                c01251.label -= Integer.MIN_VALUE;
            } else {
                c01251 = new C01251($completion);
            }
        }
        Object $result = c01251.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01251.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01251.L$0 = this;
                c01251.L$1 = context;
                c01251.L$2 = returnData;
                c01251.label = 1;
                objCheckAuth = checkAuth(context, c01251);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("sourceUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"sourceUrl\")");
                    sourceUrl = string;
                    String string2 = context.getBodyAsJson().getString("sortName", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"sortName\", \"\")");
                    sortName = string2;
                    String string3 = context.getBodyAsJson().getString("sortUrl", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string3, "context.bodyAsJson.getString(\"sortUrl\", \"\")");
                    sortUrl = string3;
                    Integer integer = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"page\", 1)");
                    page = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam("sourceUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"sourceUrl\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    sourceUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("sortName");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"sortName\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    sortName = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam3 = context.queryParam("sortUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"sortUrl\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    sortUrl = str3 == null ? PackageDocumentBase.PREFIX_OPF : str3;
                    List listQueryParam4 = context.queryParam("page");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"page\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                    int iIntValue = (str4 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str4))) == null) ? 1 : numBoxInt.intValue();
                    page = iIntValue;
                }
                if (sourceUrl.length() == 0) {
                    return returnData.setErrorMsg("RSS源链接不能为空");
                }
                if (sortUrl.length() == 0) {
                    sortUrl = sourceUrl;
                }
                String userNameSpace = this.getUserNameSpace(context);
                RssSource rssSource = this.getRssSourceByURL(sourceUrl, userNameSpace);
                if (rssSource == null) {
                    return returnData.setErrorMsg("RSS源不存在");
                }
                c01251.L$0 = returnData;
                c01251.L$1 = null;
                c01251.L$2 = null;
                c01251.label = 2;
                articles = Rss.INSTANCE.getArticles(sortName, sortUrl, rssSource, page, Debug.INSTANCE, c01251);
                if (articles == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Pair rssArtcles = (Pair) articles;
                return ReturnData.setData$default(returnData, rssArtcles, null, 2, null);
            case 1:
                returnData = (ReturnData) c01251.L$2;
                context = (RoutingContext) c01251.L$1;
                this = (RssSourceController) c01251.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c01251.L$0;
                ResultKt.throwOnFailure($result);
                articles = $result;
                Pair rssArtcles2 = (Pair) articles;
                return ReturnData.setData$default(returnData, rssArtcles2, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getRssContent(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C01261 c01261;
        ReturnData returnData;
        Object content;
        Object objCheckAuth;
        String content2;
        String sourceUrl;
        String link;
        String origin;
        if ($completion instanceof C01261) {
            c01261 = (C01261) $completion;
            if ((c01261.label & Integer.MIN_VALUE) != 0) {
                c01261.label -= Integer.MIN_VALUE;
            } else {
                c01261 = new C01261($completion);
            }
        }
        Object $result = c01261.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01261.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c01261.L$0 = this;
                c01261.L$1 = context;
                c01261.L$2 = returnData;
                c01261.label = 1;
                objCheckAuth = checkAuth(context, c01261);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("sourceUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"sourceUrl\")");
                    sourceUrl = string;
                    String string2 = context.getBodyAsJson().getString("link");
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"link\")");
                    link = string2;
                    String string3 = context.getBodyAsJson().getString("origin");
                    Intrinsics.checkNotNullExpressionValue(string3, "context.bodyAsJson.getString(\"origin\")");
                    origin = string3;
                } else {
                    List listQueryParam = context.queryParam("sourceUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"sourceUrl\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    sourceUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("link");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"link\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    link = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam3 = context.queryParam("origin");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"origin\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    origin = str3 == null ? PackageDocumentBase.PREFIX_OPF : str3;
                }
                if (sourceUrl.length() == 0) {
                    return returnData.setErrorMsg("RSS链接不能为空");
                }
                if (link.length() == 0) {
                    return returnData.setErrorMsg("RSS文章链接不能为空");
                }
                if (origin.length() == 0) {
                    return returnData.setErrorMsg("RSS文章来源不能为空");
                }
                String userNameSpace = this.getUserNameSpace(context);
                RssSource rssSource = this.getRssSourceByURL(sourceUrl, userNameSpace);
                if (rssSource == null) {
                    return returnData.setErrorMsg("RSS源不存在");
                }
                RssArticle rssArticle = new RssArticle(origin, null, null, 0L, link, null, null, null, null, false, null, 2030, null);
                content2 = PackageDocumentBase.PREFIX_OPF;
                if (rssSource.getRuleContent() != null) {
                    Rss rss = Rss.INSTANCE;
                    String ruleContent = rssSource.getRuleContent();
                    if (ruleContent == null) {
                        throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                    }
                    c01261.L$0 = returnData;
                    c01261.L$1 = null;
                    c01261.L$2 = null;
                    c01261.label = 2;
                    content = rss.getContent(rssArticle, ruleContent, rssSource, Debug.INSTANCE, c01261);
                    if (content == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    content2 = (String) content;
                }
                return ReturnData.setData$default(returnData, content2, null, 2, null);
            case 1:
                returnData = (ReturnData) c01261.L$2;
                context = (RoutingContext) c01261.L$1;
                this = (RssSourceController) c01261.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c01261.L$0;
                ResultKt.throwOnFailure($result);
                content = $result;
                content2 = (String) content;
                return ReturnData.setData$default(returnData, content2, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
