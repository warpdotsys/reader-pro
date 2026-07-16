// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import io.vertx.core.json.JsonArray;
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
import io.legado.app.data.entities.Bookmark;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J \u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u0016J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0019" }, d2 = { "Lcom/htmake/reader/api/controller/BookmarkController;", "Lcom/htmake/reader/api/controller/BaseController;", "Lcom/htmake/reader/api/controller/CURD;", "Lio/legado/app/data/entities/Bookmark;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "beforeSave", "Lcom/htmake/reader/api/ReturnData;", "var1", "db", "Lcom/htmake/reader/db/DB;", "checkUserAuth", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "Lio/vertx/core/json/JsonObject;", "var2", "getEntityClass", "Ljava/lang/Class;", "getTableName", "", "getUserNS", "reader-pro" })
public final class BookmarkController extends BaseController implements CURD<Bookmark>
{
    public BookmarkController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
    }
    
    @NotNull
    @Override
    public String getTableName() {
        return "bookmark";
    }
    
    @Override
    public boolean checker(@NotNull final JsonObject var1, @NotNull final Bookmark var2) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)var2, "var2");
        return Long.valueOf(var2.getTime()).equals(var1.getLong("time"));
    }
    
    @Nullable
    @Override
    public ReturnData beforeSave(@NotNull final Bookmark var1, @NotNull final DB<Bookmark> db) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)db, "db");
        final ReturnData returnData = new ReturnData();
        if (var1.getBookName().length() == 0 && var1.getBookAuthor().length() == 0) {
            return returnData.setErrorMsg("\u4e66\u7b7e\u4fe1\u606f\u9519\u8bef");
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
    public Class<Bookmark> getEntityClass() {
        return Bookmark.class;
    }
    
    @Nullable
    @Override
    public ReturnData beforeAdd(@NotNull final Bookmark val1, @NotNull final DB<Bookmark> db) {
        return DefaultImpls.beforeAdd(this, val1, db);
    }
    
    @Nullable
    @Override
    public ReturnData beforeDelete(@NotNull final Bookmark val1, @NotNull final DB<Bookmark> db) {
        return DefaultImpls.beforeDelete(this, val1, db);
    }
    
    @NotNull
    @Override
    public Bookmark convertToEntity(@NotNull final JsonObject var1) {
        return DefaultImpls.convertToEntity((CURD<Bookmark>)this, var1);
    }
    
    @NotNull
    @Override
    public Bookmark[] convertToEntityList(@NotNull final String var1) {
        return DefaultImpls.convertToEntityList((CURD<Bookmark>)this, var1);
    }
    
    @Override
    public void onCheckEnd(@NotNull final Bookmark var1, final boolean var2, @NotNull final JsonArray var3) {
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
