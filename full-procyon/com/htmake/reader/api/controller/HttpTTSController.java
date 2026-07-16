// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import java.util.Iterator;
import io.vertx.core.json.JsonArray;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.htmake.reader.utils.ExtKt;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.Nullable;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.db.DB;
import io.vertx.core.json.JsonObject;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;
import io.legado.app.data.entities.HttpTTS;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J \u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u0016J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u0010\u0010\u0014\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0012H\u0016J\u001b\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u00162\u0006\u0010\t\u001a\u00020\u0017H\u0016?\u0006\u0002\u0010\u0018J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0017H\u0016J\u0010\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u001d" }, d2 = { "Lcom/htmake/reader/api/controller/HttpTTSController;", "Lcom/htmake/reader/api/controller/BaseController;", "Lcom/htmake/reader/api/controller/CURD;", "Lio/legado/app/data/entities/HttpTTS;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "beforeSave", "Lcom/htmake/reader/api/ReturnData;", "var1", "db", "Lcom/htmake/reader/db/DB;", "checkUserAuth", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "Lio/vertx/core/json/JsonObject;", "var2", "convertToEntity", "convertToEntityList", "", "", "(Ljava/lang/String;)[Lio/legado/app/data/entities/HttpTTS;", "getEntityClass", "Ljava/lang/Class;", "getTableName", "getUserNS", "reader-pro" })
public final class HttpTTSController extends BaseController implements CURD<HttpTTS>
{
    public HttpTTSController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
    }
    
    @NotNull
    @Override
    public String getTableName() {
        return "httpTTS";
    }
    
    @Override
    public boolean checker(@NotNull final JsonObject var1, @NotNull final HttpTTS var2) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)var2, "var2");
        return var2.getName().equals(var1.getString("name"));
    }
    
    @Nullable
    @Override
    public ReturnData beforeSave(@NotNull final HttpTTS var1, @NotNull final DB<HttpTTS> db) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)db, "db");
        final ReturnData returnData = new ReturnData();
        if (var1.getName().length() == 0) {
            return returnData.setErrorMsg("\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (var1.getUrl().length() == 0) {
            return returnData.setErrorMsg("\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
        }
        return null;
    }
    
    @Nullable
    @Override
    public Object checkUserAuth(@NotNull final RoutingContext context, @NotNull final Continuation<? super Boolean> $completion) {
        return this.checkAuth(context, $completion);
    }
    
    @NotNull
    @Override
    public String getUserNS(@NotNull final RoutingContext context) {
        Intrinsics.checkNotNullParameter((Object)context, "context");
        return this.getUserNameSpace(context);
    }
    
    @NotNull
    @Override
    public Class<HttpTTS> getEntityClass() {
        return HttpTTS.class;
    }
    
    @NotNull
    @Override
    public HttpTTS convertToEntity(@NotNull final JsonObject var1) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        final HttpTTS.Companion companion = HttpTTS.Companion;
        final String string = var1.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "var1.toString()");
        final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(string);
        final HttpTTS httpTTS = (HttpTTS)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
        Intrinsics.checkNotNull((Object)httpTTS);
        return httpTTS;
    }
    
    @NotNull
    @Override
    public HttpTTS[] convertToEntityList(@NotNull final String var1) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        final JsonArray jsonArray = ExtKt.asJsonArray(var1);
        final List list = new ArrayList();
        final JsonArray jsonArray2 = jsonArray;
        if (jsonArray2 != null) {
            final Iterable $this$forEach$iv = (Iterable)jsonArray2;
            final int $i$f$forEach = 0;
            for (final Object it : $this$forEach$iv) {
                final Object element$iv = it;
                final int n = 0;
                final List list2 = list;
                final Object fromJson-IoAF18A = HttpTTS.Companion.fromJson-IoAF18A(it.toString());
                final Object o = Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A;
                Intrinsics.checkNotNull(o);
                list2.add(o);
            }
        }
        final Collection $this$toTypedArray$iv = list;
        final int $i$f$toTypedArray = 0;
        final Collection thisCollection$iv = $this$toTypedArray$iv;
        final HttpTTS[] array = thisCollection$iv.toArray(new HttpTTS[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return array;
    }
    
    @Nullable
    @Override
    public ReturnData beforeAdd(@NotNull final HttpTTS val1, @NotNull final DB<HttpTTS> db) {
        return DefaultImpls.beforeAdd(this, val1, db);
    }
    
    @Nullable
    @Override
    public ReturnData beforeDelete(@NotNull final HttpTTS val1, @NotNull final DB<HttpTTS> db) {
        return DefaultImpls.beforeDelete(this, val1, db);
    }
    
    @Override
    public void onCheckEnd(@NotNull final HttpTTS var1, final boolean var2, @NotNull final JsonArray var3) {
        DefaultImpls.onCheckEnd(this, var1, var2, var3);
    }
    
    @NotNull
    @Override
    public JsonArray onList(@NotNull final JsonArray var1, @NotNull final String userNameSpace) {
        return DefaultImpls.onList((CURD<Object>)this, var1, userNameSpace);
    }
    
    @Nullable
    @Override
    public Object delete(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        return DefaultImpls.delete((CURD<Object>)this, context, $completion);
    }
    
    @Nullable
    @Override
    public Object deleteMulti(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        return DefaultImpls.deleteMulti((CURD<Object>)this, context, $completion);
    }
    
    @Nullable
    @Override
    public Object list(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        return DefaultImpls.list((CURD<Object>)this, context, $completion);
    }
    
    @Nullable
    @Override
    public Object save(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        return DefaultImpls.save((CURD<Object>)this, context, $completion);
    }
    
    @Nullable
    @Override
    public Object saveMulti(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        return DefaultImpls.saveMulti((CURD<Object>)this, context, $completion);
    }
}
