// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import kotlin.jvm.internal.DefaultConstructorMarker;
import io.legado.app.data.entities.RssArticle;
import kotlin.Pair;
import io.legado.app.model.Debug;
import io.legado.app.model.DebugLog;
import io.legado.app.model.rss.Rss;
import kotlin.collections.CollectionsKt;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import kotlin.Result;
import io.legado.app.data.entities.RssSource;
import com.htmake.reader.entity.User;
import kotlin.coroutines.jvm.internal.Boxing;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import io.vertx.core.json.JsonArray;
import java.util.ArrayList;
import com.htmake.reader.utils.ExtKt;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import com.htmake.reader.api.ReturnData;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\tJ\u0019\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\tJ\u0019\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\tJ\u0019\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\tJ\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011J\u0019\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\tJ\u0019\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\tJ\u0019\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0016" }, d2 = { "Lcom/htmake/reader/api/controller/RssSourceController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "canEditRssSource", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRssSource", "Lcom/htmake/reader/api/ReturnData;", "getRssArticles", "getRssContent", "getRssSourceByURL", "Lio/legado/app/data/entities/RssSource;", "url", "", "userNameSpace", "getRssSources", "saveRssSource", "saveRssSources", "reader-pro" })
public final class RssSourceController extends BaseController
{
    public RssSourceController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
    }
    
    @Nullable
    public final Object getRssSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof RssSourceController$getRssSources.RssSourceController$getRssSources$1) {
                final RssSourceController$getRssSources.RssSourceController$getRssSources$1 rssSourceController$getRssSources$1 = (RssSourceController$getRssSources.RssSourceController$getRssSources$1)$completion;
                if ((rssSourceController$getRssSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RssSourceController$getRssSources.RssSourceController$getRssSources$1 rssSourceController$getRssSources$2 = rssSourceController$getRssSources$1;
                    rssSourceController$getRssSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RssSourceController$getRssSources.RssSourceController$getRssSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final RssSourceController rssSourceController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).L$0 = this;
                ((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).L$1 = context;
                ((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).L$2 = returnData;
                ((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).label = 1;
                if ((checkAuth = rssSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).L$2;
                context = (RoutingContext)((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).L$1;
                this = (RssSourceController)((RssSourceController$getRssSources.RssSourceController$getRssSources$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final JsonArray list = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
        if (list != null) {
            final ReturnData returnData2 = returnData;
            final List list2 = list.getList();
            Intrinsics.checkNotNullExpressionValue((Object)list2, "list.getList()");
            return ReturnData.setData$default(returnData2, list2, null, 2, null);
        }
        return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
    }
    
    @Nullable
    public final Object canEditRssSource(@NotNull final RoutingContext context, @NotNull final Continuation<? super Boolean> $completion) {
        if (!this.getAppConfig().getSecure()) {
            return Boxing.boxBoolean(true);
        }
        final User userInfo = (User)context.get("userInfo");
        if (userInfo == null) {
            return Boxing.boxBoolean(false);
        }
        return Boxing.boxBoolean(userInfo.getEnable_book_source());
    }
    
    @Nullable
    public final Object saveRssSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof RssSourceController$saveRssSource.RssSourceController$saveRssSource$1) {
                final RssSourceController$saveRssSource.RssSourceController$saveRssSource$1 rssSourceController$saveRssSource$1 = (RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$completion;
                if ((rssSourceController$saveRssSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RssSourceController$saveRssSource.RssSourceController$saveRssSource$1 rssSourceController$saveRssSource$2 = rssSourceController$saveRssSource$1;
                    rssSourceController$saveRssSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RssSourceController$saveRssSource.RssSourceController$saveRssSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditRssSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final RssSourceController rssSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$0 = this;
                    ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$1 = context;
                    ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$2 = returnData;
                    ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).label = 1;
                    if ((checkAuth = rssSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditRssSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final RssSourceController rssSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$0 = this;
            ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$1 = context;
            ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).L$2 = returnData;
            ((RssSourceController$saveRssSource.RssSourceController$saveRssSource$1)$continuation).label = 2;
            if ((canEditRssSource = rssSourceController2.canEditRssSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditRssSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final RssSource.Companion companion = RssSource.Companion;
        final String bodyAsString = context.getBodyAsString();
        Intrinsics.checkNotNullExpressionValue((Object)bodyAsString, "context.bodyAsString");
        final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(bodyAsString);
        final RssSource rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
        if (rssSource == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        if (rssSource.getSourceUrl().length() == 0) {
            return returnData2.setErrorMsg("RSS\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (rssSource.getSourceName().length() == 0) {
            return returnData2.setErrorMsg("RSS\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray rssSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
        if (rssSourceList == null) {
            rssSourceList = new JsonArray();
        }
        int existIndex = -1;
        int j = 0;
        final int size = rssSourceList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final RssSource.Companion companion2 = RssSource.Companion;
                final String string = rssSourceList.getJsonObject(i).toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "rssSourceList.getJsonObject(i).toString()");
                final Object fromJson-IoAF18A2 = companion2.fromJson-IoAF18A(string);
                final RssSource _rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A2) ? null : fromJson-IoAF18A2);
                if (_rssSource != null && _rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                    existIndex = i;
                    break;
                }
            } while (j < size);
        }
        if (existIndex >= 0) {
            final List list = rssSourceList.getList();
            list.set(existIndex, JsonObject.mapFrom((Object)rssSource));
            rssSourceList = new JsonArray(list);
        }
        else {
            rssSourceList.add(JsonObject.mapFrom((Object)rssSource));
        }
        this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
        return ReturnData.setData$default(returnData2, "", null, 2, null);
    }
    
    @Nullable
    public final Object saveRssSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof RssSourceController$saveRssSources.RssSourceController$saveRssSources$1) {
                final RssSourceController$saveRssSources.RssSourceController$saveRssSources$1 rssSourceController$saveRssSources$1 = (RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$completion;
                if ((rssSourceController$saveRssSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RssSourceController$saveRssSources.RssSourceController$saveRssSources$1 rssSourceController$saveRssSources$2 = rssSourceController$saveRssSources$1;
                    rssSourceController$saveRssSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RssSourceController$saveRssSources.RssSourceController$saveRssSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditRssSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final RssSourceController rssSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$0 = this;
                    ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$1 = context;
                    ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$2 = returnData;
                    ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).label = 1;
                    if ((checkAuth = rssSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditRssSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final RssSourceController rssSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$0 = this;
            ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$1 = context;
            ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).L$2 = returnData;
            ((RssSourceController$saveRssSources.RssSourceController$saveRssSources$1)$continuation).label = 2;
            if ((canEditRssSource = rssSourceController2.canEditRssSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditRssSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final JsonArray rssSourceJsonArray = context.getBodyAsJsonArray();
        if (rssSourceJsonArray == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray rssSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
        if (rssSourceList == null) {
            rssSourceList = new JsonArray();
        }
        int j = 0;
        final int size = rssSourceJsonArray.size();
        if (j < size) {
            do {
                final int k = j;
                ++j;
                final RssSource.Companion companion = RssSource.Companion;
                final String string = rssSourceJsonArray.getJsonObject(k).toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "rssSourceJsonArray.getJsonObject(k).toString()");
                final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(string);
                final RssSource rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                if (rssSource == null || rssSource.getSourceUrl().length() == 0) {
                    continue;
                }
                if (rssSource.getSourceName().length() == 0) {
                    continue;
                }
                int existIndex = -1;
                int l = 0;
                final int size2 = rssSourceList.size();
                if (l < size2) {
                    do {
                        final int i = l;
                        ++l;
                        final RssSource.Companion companion2 = RssSource.Companion;
                        final String string2 = rssSourceList.getJsonObject(i).toString();
                        Intrinsics.checkNotNullExpressionValue((Object)string2, "rssSourceList.getJsonObject(i).toString()");
                        final Object fromJson-IoAF18A2 = companion2.fromJson-IoAF18A(string2);
                        final RssSource _rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A2) ? null : fromJson-IoAF18A2);
                        if (_rssSource != null && _rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                            existIndex = i;
                            break;
                        }
                    } while (l < size2);
                }
                if (existIndex >= 0) {
                    final List list = rssSourceList.getList();
                    list.set(existIndex, JsonObject.mapFrom((Object)rssSource));
                    rssSourceList = new JsonArray(list);
                }
                else {
                    rssSourceList.add(JsonObject.mapFrom((Object)rssSource));
                }
            } while (j < size);
        }
        this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
        return ReturnData.setData$default(returnData2, "", null, 2, null);
    }
    
    @Nullable
    public final Object deleteRssSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1) {
                final RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1 rssSourceController$deleteRssSource$1 = (RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$completion;
                if ((rssSourceController$deleteRssSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1 rssSourceController$deleteRssSource$2 = rssSourceController$deleteRssSource$1;
                    rssSourceController$deleteRssSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditRssSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final RssSourceController rssSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$0 = this;
                    ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$1 = context;
                    ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$2 = returnData;
                    ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).label = 1;
                    if ((checkAuth = rssSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditRssSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final RssSourceController rssSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$0 = this;
            ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$1 = context;
            ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).L$2 = returnData;
            ((RssSourceController$deleteRssSource.RssSourceController$deleteRssSource$1)$continuation).label = 2;
            if ((canEditRssSource = rssSourceController2.canEditRssSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditRssSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final RssSource.Companion companion = RssSource.Companion;
        final String bodyAsString = context.getBodyAsString();
        Intrinsics.checkNotNullExpressionValue((Object)bodyAsString, "context.bodyAsString");
        final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(bodyAsString);
        final RssSource rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
        if (rssSource == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray rssSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
        if (rssSourceList == null) {
            rssSourceList = new JsonArray();
        }
        int existIndex = -1;
        int j = 0;
        final int size = rssSourceList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final RssSource.Companion companion2 = RssSource.Companion;
                final String string = rssSourceList.getJsonObject(i).toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "rssSourceList.getJsonObject(i).toString()");
                final Object fromJson-IoAF18A2 = companion2.fromJson-IoAF18A(string);
                final RssSource _rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A2) ? null : fromJson-IoAF18A2);
                if (_rssSource != null && _rssSource.getSourceUrl().equals(rssSource.getSourceUrl())) {
                    existIndex = i;
                    break;
                }
            } while (j < size);
        }
        if (existIndex >= 0) {
            rssSourceList.remove(existIndex);
        }
        this.saveUserStorage(userNameSpace, "rssSources", rssSourceList);
        return ReturnData.setData$default(returnData2, "", null, 2, null);
    }
    
    @Nullable
    public final RssSource getRssSourceByURL(@NotNull final String url, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        if (url.length() == 0) {
            return null;
        }
        final JsonArray list = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "rssSources"));
        if (list == null) {
            return null;
        }
        int j = 0;
        final int size = list.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final RssSource.Companion companion = RssSource.Companion;
                final String string = list.getJsonObject(i).toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "list.getJsonObject(i).toString()");
                final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(string);
                final RssSource _rssSource = (RssSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                if (_rssSource != null && _rssSource.getSourceUrl().equals(url)) {
                    return _rssSource;
                }
            } while (j < size);
        }
        return null;
    }
    
    @Nullable
    public final Object getRssArticles(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof RssSourceController$getRssArticles.RssSourceController$getRssArticles$1) {
                final RssSourceController$getRssArticles.RssSourceController$getRssArticles$1 rssSourceController$getRssArticles$1 = (RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$completion;
                if ((rssSourceController$getRssArticles$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RssSourceController$getRssArticles.RssSourceController$getRssArticles$1 rssSourceController$getRssArticles$2 = rssSourceController$getRssArticles$1;
                    rssSourceController$getRssArticles$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RssSourceController$getRssArticles.RssSourceController$getRssArticles$1(this, (Continuation)$completion);
        }
        final Object $result = ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object articles = null;
        Label_0727: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final RssSourceController rssSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$0 = this;
                    ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$1 = context;
                    ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$2 = returnData;
                    ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).label = 1;
                    if ((checkAuth = rssSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    articles = $result;
                    break Label_0727;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            String sourceUrl;
            String sortName;
            String sortUrl;
            int page = 0;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("sourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"sourceUrl\")");
                sourceUrl = string;
                final String string2 = context.getBodyAsJson().getString("sortName", "");
                Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"sortName\", \"\")");
                sortName = string2;
                final String string3 = context.getBodyAsJson().getString("sortUrl", "");
                Intrinsics.checkNotNullExpressionValue((Object)string3, "context.bodyAsJson.getString(\"sortUrl\", \"\")");
                sortUrl = string3;
                final Integer integer = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"page\", 1)");
                integer.intValue();
            }
            else {
                final List queryParam = context.queryParam("sourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"sourceUrl\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                sourceUrl = ((s == null) ? "" : s);
                final List queryParam2 = context.queryParam("sortName");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"sortName\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                sortName = ((s2 == null) ? "" : s2);
                final List queryParam3 = context.queryParam("sortUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"sortUrl\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
                sortUrl = ((s3 == null) ? "" : s3);
                final List queryParam4 = context.queryParam("page");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"page\")");
                final String s4 = (String)CollectionsKt.firstOrNull(queryParam4);
                int n;
                if (s4 == null) {
                    n = 1;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s4));
                    n = ((boxInt == null) ? 1 : boxInt);
                }
                page = n;
            }
            if (sourceUrl.length() == 0) {
                return returnData.setErrorMsg("RSS\u6e90\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
            }
            if (sortUrl.length() == 0) {
                sortUrl = sourceUrl;
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final RssSource rssSource = this.getRssSourceByURL(sourceUrl, userNameSpace);
            if (rssSource == null) {
                return returnData.setErrorMsg("RSS\u6e90\u4e0d\u5b58\u5728");
            }
            final Rss instance = Rss.INSTANCE;
            final String sortName2 = sortName;
            final String sortUrl2 = sortUrl;
            final RssSource rssSource2 = rssSource;
            final int page2 = page;
            final DebugLog debugLog = Debug.INSTANCE;
            final Continuation $completion3 = $continuation;
            ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$0 = returnData;
            ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$1 = null;
            ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).L$2 = null;
            ((RssSourceController$getRssArticles.RssSourceController$getRssArticles$1)$continuation).label = 2;
            if ((articles = instance.getArticles(sortName2, sortUrl2, rssSource2, page2, debugLog, (Continuation<? super Pair<? extends List<RssArticle>, String>>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final Pair rssArtcles = (Pair)articles;
        return ReturnData.setData$default(returnData2, rssArtcles, null, 2, null);
    }
    
    @Nullable
    public final Object getRssContent(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof RssSourceController$getRssContent.RssSourceController$getRssContent$1) {
                final RssSourceController$getRssContent.RssSourceController$getRssContent$1 rssSourceController$getRssContent$1 = (RssSourceController$getRssContent.RssSourceController$getRssContent$1)$completion;
                if ((rssSourceController$getRssContent$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RssSourceController$getRssContent.RssSourceController$getRssContent$1 rssSourceController$getRssContent$2 = rssSourceController$getRssContent$1;
                    rssSourceController$getRssContent$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new RssSourceController$getRssContent.RssSourceController$getRssContent$1(this, (Continuation)$completion);
        }
        final Object $result = ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object content2 = null;
        Label_0710: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final RssSourceController rssSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$0 = this;
                    ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$1 = context;
                    ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$2 = returnData;
                    ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).label = 1;
                    if ((checkAuth = rssSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$2;
                    context = (RoutingContext)((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$1;
                    this = (RssSourceController)((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    content2 = $result;
                    break Label_0710;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            String sourceUrl;
            String link;
            String origin = null;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("sourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"sourceUrl\")");
                sourceUrl = string;
                final String string2 = context.getBodyAsJson().getString("link");
                Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"link\")");
                link = string2;
                Intrinsics.checkNotNullExpressionValue((Object)context.getBodyAsJson().getString("origin"), "context.bodyAsJson.getString(\"origin\")");
            }
            else {
                final List queryParam = context.queryParam("sourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"sourceUrl\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                sourceUrl = ((s == null) ? "" : s);
                final List queryParam2 = context.queryParam("link");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"link\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                link = ((s2 == null) ? "" : s2);
                final List queryParam3 = context.queryParam("origin");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"origin\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
                origin = ((s3 == null) ? "" : s3);
            }
            if (sourceUrl.length() == 0) {
                return returnData.setErrorMsg("RSS\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
            }
            if (link.length() == 0) {
                return returnData.setErrorMsg("RSS\u6587\u7ae0\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
            }
            if (origin.length() == 0) {
                return returnData.setErrorMsg("RSS\u6587\u7ae0\u6765\u6e90\u4e0d\u80fd\u4e3a\u7a7a");
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final RssSource rssSource = this.getRssSourceByURL(sourceUrl, userNameSpace);
            if (rssSource == null) {
                return returnData.setErrorMsg("RSS\u6e90\u4e0d\u5b58\u5728");
            }
            final RssArticle rssArticle = new RssArticle(origin, null, null, 0L, link, null, null, null, null, false, null, 2030, null);
            final String content = "";
            if (rssSource.getRuleContent() == null) {
                return ReturnData.setData$default(returnData2, content, null, 2, null);
            }
            final Rss instance = Rss.INSTANCE;
            final RssArticle rssArticle2 = rssArticle;
            final String ruleContent = rssSource.getRuleContent();
            if (ruleContent == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            final String ruleContent2 = ruleContent;
            final RssSource rssSource2 = rssSource;
            final DebugLog debugLog = Debug.INSTANCE;
            final Continuation $completion3 = $continuation;
            ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$0 = returnData;
            ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$1 = null;
            ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).L$2 = null;
            ((RssSourceController$getRssContent.RssSourceController$getRssContent$1)$continuation).label = 2;
            if ((content2 = instance.getContent(rssArticle2, ruleContent2, rssSource2, debugLog, (Continuation<? super String>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final String content = (String)content2;
        return ReturnData.setData$default(returnData2, content, null, 2, null);
    }
}
