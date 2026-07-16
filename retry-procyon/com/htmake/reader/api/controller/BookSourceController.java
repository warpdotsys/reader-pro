// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import kotlin.Pair;
import java.util.ArrayList;
import io.vertx.kotlin.coroutines.VertxCoroutineKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.Dispatchers;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.slf4j.MDCContext;
import kotlinx.coroutines.CoroutineScope;
import kotlin.jvm.internal.Ref$ObjectRef;
import com.htmake.reader.utils.VertExtKt;
import kotlin.Unit;
import java.util.Iterator;
import java.nio.charset.Charset;
import kotlin.io.FilesKt;
import io.vertx.ext.web.FileUpload;
import kotlin.collections.CollectionsKt;
import io.vertx.core.http.HttpMethod;
import java.util.LinkedHashSet;
import java.util.List;
import io.vertx.core.json.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Result;
import io.legado.app.data.entities.BookSource;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.User;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import java.io.File;
import kotlin.jvm.functions.Function1;
import com.htmake.reader.utils.ExtKt;
import io.vertx.core.json.JsonArray;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import com.htmake.reader.utils.SpringContextUtils;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.NotNull;
import io.vertx.ext.web.client.WebClient;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010\u000e\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010\u000f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010\u0010\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ&\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\u0016\u001a\u00020\u00142\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0019\u0010\u0019\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\u0016\u001a\u00020\u0014J\u0019\u0010\u001b\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0016\u001a\u00020\u0014J4\u0010\u001d\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0016\u001a\u00020\u00142\u0010\b\u0002\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001f2\u0010\b\u0002\u0010 \u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u001fJ\u0019\u0010!\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010\"\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0019\u0010#\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0016\u0010#\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010$\u001a\u00020\u0018J\u0019\u0010%\u001a\u00020&2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ \u0010'\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u00142\b\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010$\u001a\u00020\u0018J\u0019\u0010*\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ#\u0010+\u001a\u00020&2\u0006\u0010\u0016\u001a\u00020\u00142\b\u0010,\u001a\u0004\u0018\u00010)H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010-R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006." }, d2 = { "Lcom/htmake/reader/api/controller/BookSourceController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "webClient", "Lio/vertx/ext/web/client/WebClient;", "canEditBookSource", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllBookSources", "Lcom/htmake/reader/api/ReturnData;", "deleteBookSource", "deleteBookSources", "deleteBookSourcesFile", "deleteUserBookSource", "generateBookSourceMap", "", "", "", "userNameSpace", "bookSourceList", "Lio/vertx/core/json/JsonArray;", "getBookSource", "getBookSourceMap", "getBookSources", "getUserBookSourceJson", "getUserBookSourceJsonOpt", "fields", "", "checkNotEmpty", "readSourceFile", "saveBookSource", "saveBookSources", "bookSourceJsonArray", "saveFromRemoteSource", "", "saveUserBookSources", "userInfo", "Lcom/htmake/reader/entity/User;", "setAsDefaultBookSources", "updateRemoteSourceSub", "user", "(Ljava/lang/String;Lcom/htmake/reader/entity/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class BookSourceController extends BaseController
{
    @NotNull
    private WebClient webClient;
    
    public BookSourceController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
        final Object bean = SpringContextUtils.getBean("webClient", (Class)WebClient.class);
        Intrinsics.checkNotNullExpressionValue(bean, "getBean(\"webClient\", WebClient::class.java)");
        this.webClient = (WebClient)bean;
    }
    
    @Nullable
    public final JsonArray getUserBookSourceJsonOpt(@NotNull final String userNameSpace, @Nullable final Set<String> fields, @Nullable final Set<String> checkNotEmpty) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        File bookSourceFile = ExtKt.getStorageFile$default(new String[] { "data", userNameSpace, "bookSource" }, null, 2, null);
        if (!bookSourceFile.exists()) {
            bookSourceFile = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
        }
        final JsonArray bookSourceList = ExtKt.parseJsonStringList$default(bookSourceFile, fields, null, 0, 0, checkNotEmpty, null, 92, null);
        return bookSourceList;
    }
    
    @Nullable
    public final JsonArray getUserBookSourceJson(@NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        JsonArray bookSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookSource"));
        if (bookSourceList == null && !userNameSpace.equals("default")) {
            final JsonArray systemBookSourceList = ExtKt.asJsonArray(this.getUserStorage("default", "bookSource"));
            if (systemBookSourceList != null) {
                bookSourceList = systemBookSourceList;
            }
        }
        return bookSourceList;
    }
    
    @Nullable
    public final Object canEditBookSource(@NotNull final RoutingContext context, @NotNull final Continuation<? super Boolean> $completion) {
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
    public final Object saveBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$saveBookSource.BookSourceController$saveBookSource$1) {
                final BookSourceController$saveBookSource.BookSourceController$saveBookSource$1 bookSourceController$saveBookSource$1 = (BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$completion;
                if ((bookSourceController$saveBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$saveBookSource.BookSourceController$saveBookSource$1 bookSourceController$saveBookSource$2 = bookSourceController$saveBookSource$1;
                    bookSourceController$saveBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$saveBookSource.BookSourceController$saveBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditBookSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookSourceController bookSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$0 = this;
                    ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$1 = context;
                    ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$2 = returnData;
                    ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).label = 1;
                    if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditBookSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final BookSourceController bookSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$0 = this;
            ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$1 = context;
            ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).L$2 = returnData;
            ((BookSourceController$saveBookSource.BookSourceController$saveBookSource$1)$continuation).label = 2;
            if ((canEditBookSource = bookSourceController2.canEditBookSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditBookSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final BookSource.Companion companion = BookSource.Companion;
        final String bodyAsString = context.getBodyAsString();
        Intrinsics.checkNotNullExpressionValue((Object)bodyAsString, "context.bodyAsString");
        final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(bodyAsString);
        final BookSource bookSource = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
        if (bookSource == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
        if (bookSourceList == null) {
            bookSourceList = new JsonArray();
        }
        final Map urlMap = new LinkedHashMap();
        int j = 0;
        final int size = bookSourceList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = urlMap;
                final String string = bookSourceList.getJsonObject(i).getString("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "bookSourceList.getJsonObject(i).getString(\"bookSourceUrl\")");
                map.put(string, Boxing.boxInt(i));
            } while (j < size);
        }
        final int existIndex = urlMap.getOrDefault(bookSource.getBookSourceUrl(), Boxing.boxInt(-1)).intValue();
        if (existIndex >= 0) {
            final List sourceList = bookSourceList.getList();
            sourceList.set(existIndex, JsonObject.mapFrom((Object)bookSource));
            bookSourceList = new JsonArray(sourceList);
        }
        else {
            final User userInfo = (User)context.get("userInfo");
            if (userInfo != null && bookSourceList.size() >= userInfo.getBook_source_limit()) {
                return returnData2.setErrorMsg("\u4f60\u5df2\u8fbe\u5230\u4e66\u6e90\u6570\u4e0a\u9650\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
            }
            bookSourceList.add(JsonObject.mapFrom((Object)bookSource));
        }
        this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
        this.generateBookSourceMap(userNameSpace, bookSourceList);
        return ReturnData.setData$default(returnData2, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object saveBookSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$saveBookSources.BookSourceController$saveBookSources$1) {
                final BookSourceController$saveBookSources.BookSourceController$saveBookSources$1 bookSourceController$saveBookSources$1 = (BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$completion;
                if ((bookSourceController$saveBookSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$saveBookSources.BookSourceController$saveBookSources$1 bookSourceController$saveBookSources$2 = bookSourceController$saveBookSources$1;
                    bookSourceController$saveBookSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$saveBookSources.BookSourceController$saveBookSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditBookSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookSourceController bookSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$0 = this;
                    ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$1 = context;
                    ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$2 = returnData;
                    ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).label = 1;
                    if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditBookSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final BookSourceController bookSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$0 = this;
            ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$1 = context;
            ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).L$2 = returnData;
            ((BookSourceController$saveBookSources.BookSourceController$saveBookSources$1)$continuation).label = 2;
            if ((canEditBookSource = bookSourceController2.canEditBookSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditBookSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final JsonArray bookSourceJsonArray = context.getBodyAsJsonArray();
        if (bookSourceJsonArray == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        return this.saveBookSources(context, bookSourceJsonArray);
    }
    
    @NotNull
    public final ReturnData saveBookSources(@NotNull final RoutingContext context, @NotNull final JsonArray bookSourceJsonArray) {
        Intrinsics.checkNotNullParameter((Object)context, "context");
        Intrinsics.checkNotNullParameter((Object)bookSourceJsonArray, "bookSourceJsonArray");
        return this.saveUserBookSources(this.getUserNameSpace(context), (User)context.get("userInfo"), bookSourceJsonArray);
    }
    
    @NotNull
    public final ReturnData saveUserBookSources(@NotNull final String userNameSpace, @Nullable final User userInfo, @NotNull final JsonArray bookSourceJsonArray) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        Intrinsics.checkNotNullParameter((Object)bookSourceJsonArray, "bookSourceJsonArray");
        final ReturnData returnData = new ReturnData();
        JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
        if (bookSourceList == null) {
            bookSourceList = new JsonArray();
        }
        final Map urlMap = new LinkedHashMap();
        int j = 0;
        final int size = bookSourceList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = urlMap;
                final String string = bookSourceList.getJsonObject(i).getString("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "bookSourceList.getJsonObject(i).getString(\"bookSourceUrl\")");
                map.put(string, i);
            } while (j < size);
        }
        boolean isOverLimit = false;
        int addCnt = 0;
        final int maxIndex = bookSourceList.size() - 1;
        final Set updateIndex = new LinkedHashSet();
        int l = 0;
        final int size2 = bookSourceJsonArray.size();
        if (l < size2) {
            do {
                final int k = l;
                ++l;
                BookSource bookSource2;
                try {
                    final BookSource.Companion companion = BookSource.Companion;
                    final String string2 = bookSourceJsonArray.getJsonObject(k).toString();
                    Intrinsics.checkNotNullExpressionValue((Object)string2, "bookSourceJsonArray.getJsonObject(k).toString()");
                    final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(string2);
                    bookSource2 = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                }
                catch (final Exception e) {
                    bookSource2 = null;
                }
                final BookSource bookSource = bookSource2;
                if (bookSource == null) {
                    continue;
                }
                final int existIndex = urlMap.getOrDefault(bookSource.getBookSourceUrl(), -1).intValue();
                if (existIndex >= 0) {
                    bookSourceList.set(existIndex, JsonObject.mapFrom((Object)bookSource));
                    if (existIndex > maxIndex) {
                        continue;
                    }
                    updateIndex.add(existIndex);
                }
                else {
                    if (userInfo != null && bookSourceList.size() >= userInfo.getBook_source_limit()) {
                        isOverLimit = true;
                        break;
                    }
                    ++addCnt;
                    bookSourceList.add(JsonObject.mapFrom((Object)bookSource));
                    urlMap.put(bookSource.getBookSourceUrl(), bookSourceList.size() - 1);
                }
            } while (l < size2);
        }
        this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
        this.generateBookSourceMap(userNameSpace, bookSourceList);
        final String tip = "\u65b0\u589e" + addCnt + "\u6761\u4e66\u6e90\uff0c\u66f4\u65b0" + updateIndex.size() + "\u6761\u4e66\u6e90";
        if (isOverLimit) {
            return returnData.setErrorMsg(Intrinsics.stringPlus(tip, (Object)"\u3002\u4f60\u5df2\u8fbe\u5230\u4e66\u6e90\u6570\u4e0a\u9650\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458"));
        }
        return returnData.setData((Object)"", tip);
    }
    
    @Nullable
    public final Object getBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$getBookSource.BookSourceController$getBookSource$1) {
                final BookSourceController$getBookSource.BookSourceController$getBookSource$1 bookSourceController$getBookSource$1 = (BookSourceController$getBookSource.BookSourceController$getBookSource$1)$completion;
                if ((bookSourceController$getBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$getBookSource.BookSourceController$getBookSource$1 bookSourceController$getBookSource$2 = bookSourceController$getBookSource$1;
                    bookSourceController$getBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$getBookSource.BookSourceController$getBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        switch (((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookSourceController bookSourceController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).L$0 = this;
                ((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).L$1 = context;
                ((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).L$2 = returnData;
                ((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).label = 1;
                if (bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).L$2;
                context = (RoutingContext)((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).L$1;
                this = (BookSourceController)((BookSourceController$getBookSource.BookSourceController$getBookSource$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        String bookSourceUrl = null;
        if (context.request().method() == HttpMethod.POST) {
            Intrinsics.checkNotNullExpressionValue((Object)context.getBodyAsJson().getString("bookSourceUrl"), "context.bodyAsJson.getString(\"bookSourceUrl\")");
        }
        else {
            final List queryParam = context.queryParam("bookSourceUrl");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookSourceUrl\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            bookSourceUrl = ((s == null) ? "" : s);
        }
        if (bookSourceUrl.length() == 0) {
            return returnData.setErrorMsg("\u4e66\u6e90\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final Map urlMap = this.getBookSourceMap(userNameSpace);
        final int existIndex = urlMap.getOrDefault(bookSourceUrl, Boxing.boxInt(-1)).intValue();
        if (existIndex < 0) {
            return returnData.setErrorMsg("\u4e66\u6e90\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        File bookSourceFile = ExtKt.getStorageFile$default(new String[] { "data", userNameSpace, "bookSource" }, null, 2, null);
        if (!bookSourceFile.exists()) {
            bookSourceFile = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
        }
        final JsonArray bookSourceList = ExtKt.parseJsonStringList$default(bookSourceFile, null, null, existIndex, existIndex, null, null, 102, null);
        if (bookSourceList == null) {
            return returnData.setErrorMsg("\u4e66\u6e90\u4fe1\u606f\u4e0d\u5b58\u5728");
        }
        final ReturnData returnData2 = returnData;
        final Map map = new JsonObject(bookSourceList.getString(0)).getMap();
        Intrinsics.checkNotNullExpressionValue((Object)map, "JsonObject(bookSourceList.getString(0)).map");
        return ReturnData.setData$default(returnData2, (Object)map, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object getBookSources(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/api/controller/BookSourceController$getBookSources$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/api/controller/BookSourceController$getBookSources$1;
        //    11: astore          20
        //    13: aload           20
        //    15: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           20
        //    26: dup            
        //    27: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/api/controller/BookSourceController$getBookSources$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/api/controller/BookSourceController$getBookSources$1.<init>:(Lcom/htmake/reader/api/controller/BookSourceController;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          21
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.label:I
        //    67: tableswitch {
        //                0: 88
        //                1: 141
        //          default: 557
        //        }
        //    88: aload           $result
        //    90: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    93: new             Lcom/htmake/reader/api/ReturnData;
        //    96: dup            
        //    97: invokespecial   com/htmake/reader/api/ReturnData.<init>:()V
        //   100: astore_3        /* returnData */
        //   101: aload_0         /* this */
        //   102: aload_1         /* context */
        //   103: aload           $continuation
        //   105: aload           $continuation
        //   107: aload_0         /* this */
        //   108: putfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.L$0:Ljava/lang/Object;
        //   111: aload           $continuation
        //   113: aload_1         /* context */
        //   114: putfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.L$1:Ljava/lang/Object;
        //   117: aload           $continuation
        //   119: aload_3         /* returnData */
        //   120: putfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.L$2:Ljava/lang/Object;
        //   123: aload           $continuation
        //   125: iconst_1       
        //   126: putfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.label:I
        //   129: invokevirtual   com/htmake/reader/api/controller/BookSourceController.checkAuth:(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   132: dup            
        //   133: aload           21
        //   135: if_acmpne       175
        //   138: aload           21
        //   140: areturn        
        //   141: aload           $continuation
        //   143: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.L$2:Ljava/lang/Object;
        //   146: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   149: astore_3        /* returnData */
        //   150: aload           $continuation
        //   152: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.L$1:Ljava/lang/Object;
        //   155: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   158: astore_1        /* context */
        //   159: aload           $continuation
        //   161: getfield        com/htmake/reader/api/controller/BookSourceController$getBookSources$1.L$0:Ljava/lang/Object;
        //   164: checkcast       Lcom/htmake/reader/api/controller/BookSourceController;
        //   167: astore_0        /* this */
        //   168: aload           $result
        //   170: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   173: aload           $result
        //   175: pop            
        //   176: iconst_0       
        //   177: istore          4
        //   179: aload_1         /* context */
        //   180: invokeinterface io/vertx/ext/web/RoutingContext.request:()Lio/vertx/core/http/HttpServerRequest;
        //   185: invokeinterface io/vertx/core/http/HttpServerRequest.method:()Lio/vertx/core/http/HttpMethod;
        //   190: getstatic       io/vertx/core/http/HttpMethod.POST:Lio/vertx/core/http/HttpMethod;
        //   193: if_acmpne       235
        //   196: aload_1         /* context */
        //   197: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   202: ldc_w           "simple"
        //   205: iconst_0       
        //   206: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   209: invokevirtual   io/vertx/core/json/JsonObject.getInteger:(Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Integer;
        //   212: astore          5
        //   214: aload           5
        //   216: ldc_w           "context.bodyAsJson.getInteger(\"simple\", 0)"
        //   219: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   222: aload           5
        //   224: checkcast       Ljava/lang/Number;
        //   227: invokevirtual   java/lang/Number.intValue:()I
        //   230: istore          4
        //   232: goto            306
        //   235: aload_1         /* context */
        //   236: ldc_w           "simple"
        //   239: invokeinterface io/vertx/ext/web/RoutingContext.queryParam:(Ljava/lang/String;)Ljava/util/List;
        //   244: astore          6
        //   246: aload           6
        //   248: ldc_w           "context.queryParam(\"simple\")"
        //   251: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   254: aload           6
        //   256: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   259: checkcast       Ljava/lang/String;
        //   262: astore          5
        //   264: aload           5
        //   266: ifnonnull       273
        //   269: iconst_0       
        //   270: goto            304
        //   273: aload           5
        //   275: astore          7
        //   277: iconst_0       
        //   278: istore          8
        //   280: aload           7
        //   282: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   285: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   288: astore          6
        //   290: aload           6
        //   292: ifnonnull       299
        //   295: iconst_0       
        //   296: goto            304
        //   299: aload           6
        //   301: invokevirtual   java/lang/Integer.intValue:()I
        //   304: istore          simple
        //   306: aload_0         /* this */
        //   307: aload_1         /* context */
        //   308: invokevirtual   com/htmake/reader/api/controller/BookSourceController.getUserNameSpace:(Lio/vertx/ext/web/RoutingContext;)Ljava/lang/String;
        //   311: astore          userNameSpace
        //   313: aload_0         /* this */
        //   314: aload           userNameSpace
        //   316: iload           simple
        //   318: ifle            355
        //   321: iconst_3       
        //   322: anewarray       Ljava/lang/String;
        //   325: astore          7
        //   327: aload           7
        //   329: iconst_0       
        //   330: ldc_w           "bookSourceGroup"
        //   333: aastore        
        //   334: aload           7
        //   336: iconst_1       
        //   337: ldc_w           "bookSourceName"
        //   340: aastore        
        //   341: aload           7
        //   343: iconst_2       
        //   344: ldc             "bookSourceUrl"
        //   346: aastore        
        //   347: aload           7
        //   349: invokestatic    kotlin/collections/SetsKt.setOf:([Ljava/lang/Object;)Ljava/util/Set;
        //   352: goto            356
        //   355: aconst_null    
        //   356: iload           simple
        //   358: ifle            370
        //   361: ldc_w           "exploreUrl"
        //   364: invokestatic    kotlin/collections/SetsKt.setOf:(Ljava/lang/Object;)Ljava/util/Set;
        //   367: goto            371
        //   370: aconst_null    
        //   371: invokevirtual   com/htmake/reader/api/controller/BookSourceController.getUserBookSourceJsonOpt:(Ljava/lang/String;Ljava/util/Set;Ljava/util/Set;)Lio/vertx/core/json/JsonArray;
        //   374: astore          bookSourceList
        //   376: aload           bookSourceList
        //   378: ifnull          539
        //   381: aload_3         /* returnData */
        //   382: aload           bookSourceList
        //   384: invokevirtual   io/vertx/core/json/JsonArray.getList:()Ljava/util/List;
        //   387: astore          7
        //   389: aload           7
        //   391: ldc_w           "bookSourceList.list"
        //   394: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   397: aload           7
        //   399: checkcast       Ljava/lang/Iterable;
        //   402: astore          7
        //   404: astore          16
        //   406: iconst_0       
        //   407: istore          $i$f$map
        //   409: aload           $this$map$iv
        //   411: astore          9
        //   413: new             Ljava/util/ArrayList;
        //   416: dup            
        //   417: aload           $this$map$iv
        //   419: bipush          10
        //   421: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   424: invokespecial   java/util/ArrayList.<init>:(I)V
        //   427: checkcast       Ljava/util/Collection;
        //   430: astore          destination$iv$iv
        //   432: iconst_0       
        //   433: istore          $i$f$mapTo
        //   435: aload           $this$mapTo$iv$iv
        //   437: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   442: astore          12
        //   444: aload           12
        //   446: invokeinterface java/util/Iterator.hasNext:()Z
        //   451: ifeq            520
        //   454: aload           12
        //   456: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   461: astore          item$iv$iv
        //   463: aload           destination$iv$iv
        //   465: aload           item$iv$iv
        //   467: astore          14
        //   469: astore          17
        //   471: iconst_0       
        //   472: istore          $i$a$-map-BookSourceController$getBookSources$2
        //   474: new             Lio/vertx/core/json/JsonObject;
        //   477: dup            
        //   478: aload           it
        //   480: ifnonnull       494
        //   483: new             Ljava/lang/NullPointerException;
        //   486: dup            
        //   487: ldc_w           "null cannot be cast to non-null type kotlin.String"
        //   490: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   493: athrow         
        //   494: aload           it
        //   496: checkcast       Ljava/lang/String;
        //   499: invokespecial   io/vertx/core/json/JsonObject.<init>:(Ljava/lang/String;)V
        //   502: invokevirtual   io/vertx/core/json/JsonObject.getMap:()Ljava/util/Map;
        //   505: astore          18
        //   507: aload           17
        //   509: aload           18
        //   511: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   516: pop            
        //   517: goto            444
        //   520: aload           destination$iv$iv
        //   522: checkcast       Ljava/util/List;
        //   525: nop            
        //   526: astore          17
        //   528: aload           16
        //   530: aload           17
        //   532: aconst_null    
        //   533: iconst_2       
        //   534: aconst_null    
        //   535: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   538: areturn        
        //   539: aload_3         /* returnData */
        //   540: iconst_0       
        //   541: istore          7
        //   543: new             Ljava/util/ArrayList;
        //   546: dup            
        //   547: invokespecial   java/util/ArrayList.<init>:()V
        //   550: aconst_null    
        //   551: iconst_2       
        //   552: aconst_null    
        //   553: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   556: areturn        
        //   557: new             Ljava/lang/IllegalStateException;
        //   560: dup            
        //   561: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //   564: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   567: athrow         
        //    Signature:
        //  (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation<-Lcom/htmake/reader/api/ReturnData;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  context      
        //  $completion  
        //    StackMapTable: 00 13 27 FF 00 0A 00 15 07 00 02 07 00 7D 07 01 4C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 E3 00 00 FF 00 25 00 16 07 00 02 07 00 7D 07 01 4C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00 34 FF 00 21 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 01 07 00 65 FF 00 3B 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 25 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 01 1E 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 19 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 01 67 07 00 30 01 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 04 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 00 65 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 01 01 FF 00 01 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 65 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 30 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 02 07 00 02 07 00 30 FF 00 00 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 03 07 00 02 07 00 30 07 01 6F FF 00 0D 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 03 07 00 02 07 00 30 07 01 6F FF 00 00 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 04 07 00 02 07 00 30 07 01 6F 07 01 6F FF 00 48 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 00 63 07 02 0B 01 07 02 0B 07 02 16 01 07 02 1C 00 00 00 07 00 A7 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 31 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 00 63 07 02 0B 01 07 02 0B 07 02 16 01 07 02 1C 07 00 65 07 00 65 01 07 00 A7 07 02 16 00 07 00 65 07 01 E3 07 00 65 00 02 08 01 DA 08 01 DA FF 00 19 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 00 63 07 02 0B 01 07 02 0B 07 02 16 01 07 02 1C 00 00 00 07 00 A7 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 12 00 16 07 00 02 07 00 7D 07 01 4C 07 00 A7 01 07 00 30 07 00 63 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00 FF 00 11 00 16 07 00 02 07 00 7D 07 01 4C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 65 07 01 E3 07 00 65 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public final Object deleteBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1) {
                final BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1 bookSourceController$deleteBookSource$1 = (BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$completion;
                if ((bookSourceController$deleteBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1 bookSourceController$deleteBookSource$2 = bookSourceController$deleteBookSource$1;
                    bookSourceController$deleteBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditBookSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookSourceController bookSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$0 = this;
                    ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$1 = context;
                    ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$2 = returnData;
                    ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).label = 1;
                    if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditBookSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final BookSourceController bookSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$0 = this;
            ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$1 = context;
            ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).L$2 = returnData;
            ((BookSourceController$deleteBookSource.BookSourceController$deleteBookSource$1)$continuation).label = 2;
            if ((canEditBookSource = bookSourceController2.canEditBookSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditBookSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final BookSource.Companion companion = BookSource.Companion;
        final String bodyAsString = context.getBodyAsString();
        Intrinsics.checkNotNullExpressionValue((Object)bodyAsString, "context.bodyAsString");
        final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(bodyAsString);
        final BookSource bookSource = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
        if (bookSource == null) {
            return returnData2.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
        if (bookSourceList == null) {
            bookSourceList = new JsonArray();
        }
        final Map urlMap = this.getBookSourceMap(userNameSpace);
        final int existIndex = urlMap.getOrDefault(bookSource.getBookSourceUrl(), Boxing.boxInt(-1)).intValue();
        if (existIndex >= 0) {
            bookSourceList.remove(existIndex);
        }
        this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
        this.generateBookSourceMap(userNameSpace, bookSourceList);
        return ReturnData.setData$default(returnData2, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object deleteBookSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1) {
                final BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1 bookSourceController$deleteBookSources$1 = (BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$completion;
                if ((bookSourceController$deleteBookSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1 bookSourceController$deleteBookSources$2 = bookSourceController$deleteBookSources$1;
                    bookSourceController$deleteBookSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditBookSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookSourceController bookSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$0 = this;
                    ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$1 = context;
                    ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$2 = returnData;
                    ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).label = 1;
                    if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditBookSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final BookSourceController bookSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$0 = this;
            ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$1 = context;
            ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).L$2 = returnData;
            ((BookSourceController$deleteBookSources.BookSourceController$deleteBookSources$1)$continuation).label = 2;
            if ((canEditBookSource = bookSourceController2.canEditBookSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditBookSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final JsonArray bookSourceJsonArray = context.getBodyAsJsonArray();
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookSourceList = this.getUserBookSourceJson(userNameSpace);
        if (bookSourceList == null) {
            bookSourceList = new JsonArray();
        }
        int j = 0;
        final int size = bookSourceJsonArray.size();
        if (j < size) {
            do {
                final int k = j;
                ++j;
                final String bookSourceUrl = bookSourceJsonArray.getJsonObject(k).getString("bookSourceUrl");
                final CharSequence charSequence = bookSourceUrl;
                if (charSequence == null || charSequence.length() == 0) {
                    continue;
                }
                int existIndex = -1;
                int l = 0;
                final int size2 = bookSourceList.size();
                if (l < size2) {
                    do {
                        final int i = l;
                        ++l;
                        final String _bookSourceUrl = bookSourceList.getJsonObject(i).getString("bookSourceUrl");
                        if (bookSourceUrl.equals(_bookSourceUrl)) {
                            existIndex = i;
                            break;
                        }
                    } while (l < size2);
                }
                if (existIndex < 0) {
                    continue;
                }
                bookSourceList.remove(existIndex);
            } while (j < size);
        }
        this.saveUserStorage(userNameSpace, "bookSource", bookSourceList);
        this.generateBookSourceMap(userNameSpace, bookSourceList);
        return ReturnData.setData$default(returnData2, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object deleteAllBookSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1) {
                final BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1 bookSourceController$deleteAllBookSources$1 = (BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$completion;
                if ((bookSourceController$deleteAllBookSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1 bookSourceController$deleteAllBookSources$2 = bookSourceController$deleteAllBookSources$1;
                    bookSourceController$deleteAllBookSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object canEditBookSource = null;
        Label_0277: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookSourceController bookSourceController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$0 = this;
                    ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$1 = context;
                    ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$2 = returnData;
                    ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).label = 1;
                    if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$2;
                    context = (RoutingContext)((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$1;
                    this = (BookSourceController)((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    canEditBookSource = $result;
                    break Label_0277;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final BookSourceController bookSourceController2 = this;
            final RoutingContext context3 = context;
            final Continuation $completion3 = $continuation;
            ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$0 = this;
            ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$1 = context;
            ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).L$2 = returnData;
            ((BookSourceController$deleteAllBookSources.BookSourceController$deleteAllBookSources$1)$continuation).label = 2;
            if ((canEditBookSource = bookSourceController2.canEditBookSource(context3, (Continuation<? super Boolean>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        if (!(boolean)canEditBookSource) {
            return returnData2.setErrorMsg("\u6743\u9650\u4e0d\u8db3");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        this.saveUserStorage(userNameSpace, "bookSource", new JsonArray());
        this.generateBookSourceMap(userNameSpace, new JsonArray());
        return ReturnData.setData$default(returnData2, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object setAsDefaultBookSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1) {
                final BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1 bookSourceController$setAsDefaultBookSources$1 = (BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$completion;
                if ((bookSourceController$setAsDefaultBookSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1 bookSourceController$setAsDefaultBookSources$2 = bookSourceController$setAsDefaultBookSources$1;
                    bookSourceController$setAsDefaultBookSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookSourceController bookSourceController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).L$0 = this;
                ((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).L$1 = context;
                ((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).L$2 = returnData;
                ((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).label = 1;
                if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).L$2;
                context = (RoutingContext)((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).L$1;
                this = (BookSourceController)((BookSourceController$setAsDefaultBookSources.BookSourceController$setAsDefaultBookSources$1)$continuation).L$0;
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
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        final String username = context.getBodyAsJson().getString("username");
        final BookSourceController bookSourceController2 = this;
        final String context3 = username;
        Intrinsics.checkNotNullExpressionValue((Object)context3, "username");
        final JsonArray bookSourceList = ExtKt.asJsonArray(bookSourceController2.getUserStorage(context3, "bookSource"));
        if (bookSourceList == null) {
            return returnData.setErrorMsg("\u7528\u6237\u4e66\u6e90\u4e0d\u5b58\u5728");
        }
        final BookSourceController bookSourceController3 = this;
        final String context4 = "default";
        final String path = "bookSource";
        final List list = bookSourceList.getList();
        Intrinsics.checkNotNullExpressionValue((Object)list, "bookSourceList.getList()");
        bookSourceController3.saveUserStorage(context4, path, list);
        this.generateBookSourceMap("default", bookSourceList);
        return ReturnData.setData$default(returnData, (Object)"\u8bbe\u7f6e\u9ed8\u8ba4\u4e66\u6e90\u6210\u529f", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object readSourceFile(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
            return returnData.setErrorMsg("\u8bf7\u4e0a\u4f20\u6587\u4ef6");
        }
        Object sourceList = null;
        sourceList = new JsonArray();
        final Set fileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue((Object)fileUploads, "context.fileUploads()");
        final Iterable $this$forEach$iv = fileUploads;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final FileUpload it = (FileUpload)element$iv;
            final int n = 0;
            final File file = new File(it.uploadedFileName());
            if (file.exists()) {
                ((JsonArray)sourceList).add(FilesKt.readText$default(file, (Charset)null, 1, (Object)null));
                file.delete();
            }
        }
        final ReturnData returnData2 = returnData;
        final List list = ((JsonArray)sourceList).getList();
        Intrinsics.checkNotNullExpressionValue((Object)list, "sourceList.getList()");
        return ReturnData.setData$default(returnData2, (Object)list, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object saveFromRemoteSource(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1) {
                final BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1 bookSourceController$saveFromRemoteSource$1 = (BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$completion;
                if ((bookSourceController$saveFromRemoteSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1 bookSourceController$saveFromRemoteSource$2 = bookSourceController$saveFromRemoteSource$1;
                    bookSourceController$saveFromRemoteSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookSourceController bookSourceController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).L$0 = this;
                ((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).L$1 = context;
                ((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).L$2 = returnData;
                ((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).label = 1;
                if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).L$2;
                context = (RoutingContext)((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).L$1;
                this = (BookSourceController)((BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            VertExtKt.success(context, ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"));
            return Unit.INSTANCE;
        }
        final Ref$ObjectRef url = new Ref$ObjectRef();
        if (context.request().method() == HttpMethod.POST) {
            final Ref$ObjectRef ref$ObjectRef = url;
            final String string = context.getBodyAsJson().getString("url");
            ref$ObjectRef.element = ((string == null) ? "" : string);
        }
        else {
            final Ref$ObjectRef ref$ObjectRef2 = url;
            final List queryParam = context.queryParam("url");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            ref$ObjectRef2.element = ((s == null) ? "" : s);
        }
        final CharSequence charSequence = (CharSequence)url.element;
        if (charSequence == null || charSequence.length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("\u8bf7\u8f93\u5165\u8fdc\u7a0b\u4e66\u6e90\u94fe\u63a5"));
            return Unit.INSTANCE;
        }
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()), (CoroutineStart)null, (Function2)new BookSourceController$saveFromRemoteSource.BookSourceController$saveFromRemoteSource$2(this, url, context, returnData, (Continuation)null), 2, (Object)null);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object updateRemoteSourceSub(@NotNull String userNameSpace, @Nullable User user, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1) {
                final BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1 bookSourceController$updateRemoteSourceSub$1 = (BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$completion;
                if ((bookSourceController$updateRemoteSourceSub$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1 bookSourceController$updateRemoteSourceSub$2 = bookSourceController$updateRemoteSourceSub$1;
                    bookSourceController$updateRemoteSourceSub$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0416: {
            while (true) {
                int i$0 = 0;
                int i$2 = 0;
                Label_0408: {
                    final Ref$ObjectRef remoteBookSourceList;
                    switch (((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            final Ref$ObjectRef ref$ObjectRef;
                            remoteBookSourceList = (ref$ObjectRef = new Ref$ObjectRef());
                            final JsonArray jsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "remoteBookSourceSub"));
                            if (jsonArray == null) {
                                return Unit.INSTANCE;
                            }
                            ref$ObjectRef.element = jsonArray;
                            i$0 = 0;
                            i$2 = ((JsonArray)remoteBookSourceList.element).size();
                            if (i$0 < i$2) {
                                break;
                            }
                            break Label_0416;
                        }
                        case 1: {
                            i$2 = ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).I$1;
                            i$0 = ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).I$0;
                            final Ref$ObjectRef ref$ObjectRef2 = (Ref$ObjectRef)((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$3;
                            user = (User)((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$2;
                            userNameSpace = (String)((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$1;
                            this = (BookSourceController)((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            break Label_0408;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    final int i = i$0;
                    ++i$0;
                    final Ref$ObjectRef remoteBookSource = new Ref$ObjectRef();
                    remoteBookSource.element = ((JsonArray)remoteBookSourceList.element).getJsonObject(i);
                    final Ref$ObjectRef url = new Ref$ObjectRef();
                    url.element = ((JsonObject)remoteBookSource.element).getString("link");
                    final CharSequence charSequence = (CharSequence)url.element;
                    if (charSequence != null && charSequence.length() != 0) {
                        final Function1 function1 = (Function1)new BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$2(this, url, userNameSpace, user, remoteBookSourceList, i, remoteBookSource);
                        final Continuation continuation = $continuation;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$0 = this;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$1 = userNameSpace;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$2 = user;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).L$3 = remoteBookSourceList;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).I$0 = i$0;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).I$1 = i$2;
                        ((BookSourceController$updateRemoteSourceSub.BookSourceController$updateRemoteSourceSub$1)$continuation).label = 1;
                        if (VertxCoroutineKt.awaitResult(function1, continuation) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                    }
                }
                if (i$0 < i$2) {
                    continue;
                }
                break;
            }
        }
        generateBookSourceMap$default(this, userNameSpace, null, 2, null);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object deleteUserBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1) {
                final BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1 bookSourceController$deleteUserBookSource$1 = (BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$completion;
                if ((bookSourceController$deleteUserBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1 bookSourceController$deleteUserBookSource$2 = bookSourceController$deleteUserBookSource$1;
                    bookSourceController$deleteUserBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookSourceController bookSourceController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).L$0 = this;
                ((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).L$1 = context;
                ((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).L$2 = returnData;
                ((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).label = 1;
                if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).L$2;
                context = (RoutingContext)((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).L$1;
                this = (BookSourceController)((BookSourceController$deleteUserBookSource.BookSourceController$deleteUserBookSource$1)$continuation).L$0;
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
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        final JsonArray userJsonArray = context.getBodyAsJsonArray();
        int j = 0;
        final int size = userJsonArray.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final String username = userJsonArray.getString(i);
                final String[] array;
                final String[] subDirFiles = array = new String[] { "storage", "data", null, null };
                final int n = 2;
                final String s = username;
                Intrinsics.checkNotNullExpressionValue((Object)s, "username");
                array[n] = s;
                subDirFiles[3] = "bookSource.json";
                final File userBookSourceFile = new File(ExtKt.getWorkDir(subDirFiles));
                if (userBookSourceFile.exists()) {
                    ExtKt.deleteRecursively(userBookSourceFile);
                }
            } while (j < size);
        }
        return ReturnData.setData$default(returnData, (Object)"\u5220\u9664\u4e66\u6e90\u6210\u529f", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object deleteBookSourcesFile(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1) {
                final BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1 bookSourceController$deleteBookSourcesFile$1 = (BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$completion;
                if ((bookSourceController$deleteBookSourcesFile$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1 bookSourceController$deleteBookSourcesFile$2 = bookSourceController$deleteBookSourcesFile$1;
                    bookSourceController$deleteBookSourcesFile$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookSourceController bookSourceController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).L$0 = this;
                ((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).L$1 = context;
                ((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).L$2 = returnData;
                ((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).label = 1;
                if ((checkAuth = bookSourceController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).L$2;
                context = (RoutingContext)((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).L$1;
                this = (BookSourceController)((BookSourceController$deleteBookSourcesFile.BookSourceController$deleteBookSourcesFile$1)$continuation).L$0;
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
        final String userNameSpace = this.getUserNameSpace(context);
        final File userBookSourceFile = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "bookSource.json"));
        if (userBookSourceFile.exists()) {
            ExtKt.deleteRecursively(userBookSourceFile);
        }
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @NotNull
    public final Map<String, Integer> generateBookSourceMap(@NotNull final String userNameSpace, @Nullable final JsonArray bookSourceList) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        JsonArray bookSourceJsonArray = (bookSourceList != null) ? bookSourceList : this.getUserBookSourceJson(userNameSpace);
        if (bookSourceJsonArray == null) {
            bookSourceJsonArray = new JsonArray();
        }
        final Map urlMap = new LinkedHashMap();
        final List exploreList = new ArrayList();
        int j = 0;
        final int size = bookSourceJsonArray.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = urlMap;
                final String string = bookSourceJsonArray.getJsonObject(i).getString("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "bookSourceJsonArray.getJsonObject(i).getString(\"bookSourceUrl\")");
                map.put(string, i);
                final CharSequence charSequence = bookSourceJsonArray.getJsonObject(i).getString("exploreUrl");
                if (charSequence != null && charSequence.length() != 0) {
                    exploreList.add(MapsKt.mutableMapOf(new Pair[] { TuplesKt.to((Object)"bookSourceUrl", (Object)bookSourceJsonArray.getJsonObject(i).getString("bookSourceUrl")), TuplesKt.to((Object)"bookSourceGroup", (Object)bookSourceJsonArray.getJsonObject(i).getString("bookSourceGroup")), TuplesKt.to((Object)"bookSourceName", (Object)bookSourceJsonArray.getJsonObject(i).getString("bookSourceName")) }));
                }
            } while (j < size);
        }
        this.saveUserStorage(userNameSpace, "bookSourceMap", urlMap);
        this.saveUserStorage(userNameSpace, "bookSourceExploreList", exploreList);
        return urlMap;
    }
    
    public static /* synthetic */ Map generateBookSourceMap$default(final BookSourceController bookSourceController, final String userNameSpace, JsonArray bookSourceList, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            bookSourceList = null;
        }
        return bookSourceController.generateBookSourceMap(userNameSpace, bookSourceList);
    }
    
    @NotNull
    public final Map<String, Integer> getBookSourceMap(@NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        final String content = ExtKt.getStorageFile$default(new String[] { "data", userNameSpace, "bookSource" }, null, 2, null).exists() ? this.getUserStorage(userNameSpace, "bookSourceMap") : this.getUserStorage("default", "bookSourceMap");
        final CharSequence charSequence = content;
        if (charSequence != null && charSequence.length() != 0) {
            final JsonObject jsonObject = ExtKt.asJsonObject(content);
            final Map map = (jsonObject == null) ? null : jsonObject.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.Int>");
            }
            return TypeIntrinsics.asMutableMap((Object)map);
        }
        else {
            if (ExtKt.getStorageFile$default(new String[] { "data", userNameSpace, "bookSource" }, null, 2, null).exists()) {
                return generateBookSourceMap$default(this, userNameSpace, null, 2, null);
            }
            return generateBookSourceMap$default(this, "default", null, 2, null);
        }
    }
}
