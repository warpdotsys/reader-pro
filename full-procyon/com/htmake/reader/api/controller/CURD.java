// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.jvm.functions.Function1;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import java.util.List;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import com.htmake.reader.utils.ExtKt;
import java.lang.reflect.Array;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.Nullable;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.db.DB;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J%\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016?\u0006\u0002\u0010\bJ%\u0010\t\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016?\u0006\u0002\u0010\bJ%\u0010\n\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016?\u0006\u0002\u0010\bJ\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH?@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000fJ\u001d\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00028\u0000H&?\u0006\u0002\u0010\u0014J\u0015\u0010\u0015\u001a\u00028\u00002\u0006\u0010\u0011\u001a\u00020\u0012H\u0016?\u0006\u0002\u0010\u0016J\u001b\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00182\u0006\u0010\u0011\u001a\u00020\u0019H\u0016?\u0006\u0002\u0010\u001aJ\u0019\u0010\u001b\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000fJ\u0019\u0010\u001c\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000fJ\u000e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00028\u00000\u001eH&J\b\u0010\u001f\u001a\u00020\u0019H&J\u0010\u0010 \u001a\u00020\u00192\u0006\u0010\r\u001a\u00020\u000eH&J\u0019\u0010!\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000fJ%\u0010\"\u001a\u00020#2\u0006\u0010\u0011\u001a\u00028\u00002\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010$\u001a\u00020%H\u0016?\u0006\u0002\u0010&J\u0018\u0010'\u001a\u00020%2\u0006\u0010\u0011\u001a\u00020%2\u0006\u0010(\u001a\u00020\u0019H\u0016J\u0019\u0010)\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000fJ\u0019\u0010*\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006+" }, d2 = { "Lcom/htmake/reader/api/controller/CURD;", "T", "", "beforeAdd", "Lcom/htmake/reader/api/ReturnData;", "val1", "db", "Lcom/htmake/reader/db/DB;", "(Ljava/lang/Object;Lcom/htmake/reader/db/DB;)Lcom/htmake/reader/api/ReturnData;", "beforeDelete", "beforeSave", "checkUserAuth", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "var1", "Lio/vertx/core/json/JsonObject;", "var2", "(Lio/vertx/core/json/JsonObject;Ljava/lang/Object;)Z", "convertToEntity", "(Lio/vertx/core/json/JsonObject;)Ljava/lang/Object;", "convertToEntityList", "", "", "(Ljava/lang/String;)[Ljava/lang/Object;", "delete", "deleteMulti", "getEntityClass", "Ljava/lang/Class;", "getTableName", "getUserNS", "list", "onCheckEnd", "", "var3", "Lio/vertx/core/json/JsonArray;", "(Ljava/lang/Object;ZLio/vertx/core/json/JsonArray;)V", "onList", "userNameSpace", "save", "saveMulti", "reader-pro" })
public interface CURD<T>
{
    @NotNull
    String getTableName();
    
    T convertToEntity(@NotNull final JsonObject var1);
    
    @NotNull
    T[] convertToEntityList(@NotNull final String var1);
    
    @NotNull
    JsonArray onList(@NotNull final JsonArray var1, @NotNull final String userNameSpace);
    
    boolean checker(@NotNull final JsonObject var1, final T var2);
    
    void onCheckEnd(final T var1, final boolean var2, @NotNull final JsonArray var3);
    
    @Nullable
    ReturnData beforeSave(final T val1, @NotNull final DB<T> db);
    
    @Nullable
    ReturnData beforeAdd(final T val1, @NotNull final DB<T> db);
    
    @Nullable
    ReturnData beforeDelete(final T val1, @NotNull final DB<T> db);
    
    @Nullable
    Object checkUserAuth(@NotNull final RoutingContext context, @NotNull final Continuation<? super Boolean> $completion);
    
    @NotNull
    String getUserNS(@NotNull final RoutingContext context);
    
    @NotNull
    Class<T> getEntityClass();
    
    @Nullable
    Object list(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion);
    
    @Nullable
    Object save(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion);
    
    @Nullable
    Object saveMulti(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion);
    
    @Nullable
    Object delete(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion);
    
    @Nullable
    Object deleteMulti(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion);
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        public static <T> T convertToEntity(@NotNull final CURD<T> this, @NotNull final JsonObject var1) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)var1, "var1");
            return (T)var1.mapTo((Class)this.getEntityClass());
        }
        
        @NotNull
        public static <T> T[] convertToEntityList(@NotNull final CURD<T> this, @NotNull final String var1) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)var1, "var1");
            final Class clazz = Array.newInstance(this.getEntityClass(), 0).getClass();
            final Object fromJson = ExtKt.getGson().fromJson(var1, clazz);
            Intrinsics.checkNotNullExpressionValue(fromJson, "gson.fromJson(var1, clazz)");
            final Object[] itemList = (Object[])fromJson;
            return (T[])itemList;
        }
        
        @NotNull
        public static <T> JsonArray onList(@NotNull final CURD<T> this, @NotNull final JsonArray var1, @NotNull final String userNameSpace) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)var1, "var1");
            Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
            return var1;
        }
        
        public static <T> void onCheckEnd(@NotNull final CURD<T> this, final T var1, final boolean var2, @NotNull final JsonArray var3) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)var3, "var3");
        }
        
        @Nullable
        public static <T> ReturnData beforeSave(@NotNull final CURD<T> this, final T val1, @NotNull final DB<T> db) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)db, "db");
            return null;
        }
        
        @Nullable
        public static <T> ReturnData beforeAdd(@NotNull final CURD<T> this, final T val1, @NotNull final DB<T> db) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)db, "db");
            return null;
        }
        
        @Nullable
        public static <T> ReturnData beforeDelete(@NotNull final CURD<T> this, final T val1, @NotNull final DB<T> db) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)db, "db");
            return null;
        }
        
        @Nullable
        public static <T> Object list(@NotNull CURD<T> this, @NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
            final Continuation $continuation;
            Label_0049: {
                if ($completion instanceof CURD$list.CURD$list$1) {
                    final CURD$list.CURD$list$1 curd$list$1 = (CURD$list.CURD$list$1)$completion;
                    if ((curd$list$1.label & Integer.MIN_VALUE) != 0x0) {
                        final CURD$list.CURD$list$1 curd$list$2 = curd$list$1;
                        curd$list$2.label -= Integer.MIN_VALUE;
                        break Label_0049;
                    }
                }
                $continuation = (Continuation)new CURD$list.CURD$list$1((Continuation)$completion);
            }
            final Object $result = ((CURD$list.CURD$list$1)$continuation).result;
            final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ReturnData returnData = null;
            Object checkUserAuth = null;
            switch (((CURD$list.CURD$list$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final CURD curd = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((CURD$list.CURD$list$1)$continuation).L$0 = this;
                    ((CURD$list.CURD$list$1)$continuation).L$1 = context;
                    ((CURD$list.CURD$list$1)$continuation).L$2 = returnData;
                    ((CURD$list.CURD$list$1)$continuation).label = 1;
                    if ((checkUserAuth = curd.checkUserAuth(context2, $completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((CURD$list.CURD$list$1)$continuation).L$2;
                    context = (RoutingContext)((CURD$list.CURD$list$1)$continuation).L$1;
                    this = (CURD)((CURD$list.CURD$list$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkUserAuth = $result;
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkUserAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final String userNameSpace = this.getUserNS(context);
            JsonArray list = DB.Companion.table$default(DB.Companion, userNameSpace, this.getTableName(), null, 4, null).readAll();
            list = this.onList(list, userNameSpace);
            final ReturnData returnData2 = returnData;
            final List list2 = list.getList();
            Intrinsics.checkNotNullExpressionValue((Object)list2, "list.getList()");
            return ReturnData.setData$default(returnData2, list2, null, 2, null);
        }
        
        @Nullable
        public static <T> Object save(@NotNull CURD<T> this, @NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
            final Continuation $continuation;
            Label_0049: {
                if ($completion instanceof CURD$save.CURD$save$1) {
                    final CURD$save.CURD$save$1 curd$save$1 = (CURD$save.CURD$save$1)$completion;
                    if ((curd$save$1.label & Integer.MIN_VALUE) != 0x0) {
                        final CURD$save.CURD$save$1 curd$save$2 = curd$save$1;
                        curd$save$2.label -= Integer.MIN_VALUE;
                        break Label_0049;
                    }
                }
                $continuation = (Continuation)new CURD$save.CURD$save$1((Continuation)$completion);
            }
            final Object $result = ((CURD$save.CURD$save$1)$continuation).result;
            final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ReturnData returnData = null;
            Object checkUserAuth = null;
            switch (((CURD$save.CURD$save$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final CURD curd = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((CURD$save.CURD$save$1)$continuation).L$0 = this;
                    ((CURD$save.CURD$save$1)$continuation).L$1 = context;
                    ((CURD$save.CURD$save$1)$continuation).L$2 = returnData;
                    ((CURD$save.CURD$save$1)$continuation).label = 1;
                    if ((checkUserAuth = curd.checkUserAuth(context2, $completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((CURD$save.CURD$save$1)$continuation).L$2;
                    context = (RoutingContext)((CURD$save.CURD$save$1)$continuation).L$1;
                    this = (CURD)((CURD$save.CURD$save$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkUserAuth = $result;
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkUserAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final CURD curd2 = this;
            final JsonObject bodyAsJson = context.getBodyAsJson();
            Intrinsics.checkNotNullExpressionValue((Object)bodyAsJson, "context.bodyAsJson");
            final Object entity = curd2.convertToEntity(bodyAsJson);
            final String userNameSpace = this.getUserNS(context);
            final DB db = DB.Companion.table$default(DB.Companion, userNameSpace, this.getTableName(), null, 4, null);
            final ReturnData result = this.beforeSave(entity, db);
            if (result != null) {
                return result;
            }
            db.save(entity, (kotlin.jvm.functions.Function3<? super Object, ? super Boolean, ? super JsonArray, Unit>)new CURD$save.CURD$save$2(this), (kotlin.jvm.functions.Function2<? super JsonObject, ? super Object, Boolean>)new CURD$save.CURD$save$3(this));
            return ReturnData.setData$default(returnData, "", null, 2, null);
        }
        
        @Nullable
        public static <T> Object saveMulti(@NotNull CURD<T> this, @NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
            final Continuation $continuation;
            Label_0049: {
                if ($completion instanceof CURD$saveMulti.CURD$saveMulti$1) {
                    final CURD$saveMulti.CURD$saveMulti$1 curd$saveMulti$1 = (CURD$saveMulti.CURD$saveMulti$1)$completion;
                    if ((curd$saveMulti$1.label & Integer.MIN_VALUE) != 0x0) {
                        final CURD$saveMulti.CURD$saveMulti$1 curd$saveMulti$2 = curd$saveMulti$1;
                        curd$saveMulti$2.label -= Integer.MIN_VALUE;
                        break Label_0049;
                    }
                }
                $continuation = (Continuation)new CURD$saveMulti.CURD$saveMulti$1((Continuation)$completion);
            }
            final Object $result = ((CURD$saveMulti.CURD$saveMulti$1)$continuation).result;
            final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ReturnData returnData = null;
            Object checkUserAuth = null;
            switch (((CURD$saveMulti.CURD$saveMulti$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final CURD curd = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((CURD$saveMulti.CURD$saveMulti$1)$continuation).L$0 = this;
                    ((CURD$saveMulti.CURD$saveMulti$1)$continuation).L$1 = context;
                    ((CURD$saveMulti.CURD$saveMulti$1)$continuation).L$2 = returnData;
                    ((CURD$saveMulti.CURD$saveMulti$1)$continuation).label = 1;
                    if ((checkUserAuth = curd.checkUserAuth(context2, $completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((CURD$saveMulti.CURD$saveMulti$1)$continuation).L$2;
                    context = (RoutingContext)((CURD$saveMulti.CURD$saveMulti$1)$continuation).L$1;
                    this = (CURD)((CURD$saveMulti.CURD$saveMulti$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkUserAuth = $result;
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkUserAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final CURD curd2 = this;
            final String bodyAsString = context.getBodyAsString();
            Intrinsics.checkNotNullExpressionValue((Object)bodyAsString, "context.bodyAsString");
            final Object[] itemList = curd2.convertToEntityList(bodyAsString);
            if (itemList.length == 0) {
                return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
            }
            final String userNameSpace = this.getUserNS(context);
            final DB db = DB.Companion.table$default(DB.Companion, userNameSpace, this.getTableName(), null, 4, null);
            final Object[] array = itemList;
            int i = 0;
            while (i < array.length) {
                final Object item = array[i];
                ++i;
                final ReturnData result = this.beforeSave(item, db);
                if (result != null) {
                    return result;
                }
            }
            db.saveMulti(itemList, (kotlin.jvm.functions.Function3<? super Object, ? super Boolean, ? super JsonArray, Unit>)new CURD$saveMulti.CURD$saveMulti$2(this), (kotlin.jvm.functions.Function2<? super JsonObject, ? super Object, Boolean>)new CURD$saveMulti.CURD$saveMulti$3(this));
            return ReturnData.setData$default(returnData, "", null, 2, null);
        }
        
        @Nullable
        public static <T> Object delete(@NotNull CURD<T> this, @NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
            final Continuation $continuation;
            Label_0049: {
                if ($completion instanceof CURD$delete.CURD$delete$1) {
                    final CURD$delete.CURD$delete$1 curd$delete$1 = (CURD$delete.CURD$delete$1)$completion;
                    if ((curd$delete$1.label & Integer.MIN_VALUE) != 0x0) {
                        final CURD$delete.CURD$delete$1 curd$delete$2 = curd$delete$1;
                        curd$delete$2.label -= Integer.MIN_VALUE;
                        break Label_0049;
                    }
                }
                $continuation = (Continuation)new CURD$delete.CURD$delete$1((Continuation)$completion);
            }
            final Object $result = ((CURD$delete.CURD$delete$1)$continuation).result;
            final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ReturnData returnData = null;
            Object checkUserAuth = null;
            switch (((CURD$delete.CURD$delete$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final CURD curd = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((CURD$delete.CURD$delete$1)$continuation).L$0 = this;
                    ((CURD$delete.CURD$delete$1)$continuation).L$1 = context;
                    ((CURD$delete.CURD$delete$1)$continuation).L$2 = returnData;
                    ((CURD$delete.CURD$delete$1)$continuation).label = 1;
                    if ((checkUserAuth = curd.checkUserAuth(context2, $completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((CURD$delete.CURD$delete$1)$continuation).L$2;
                    context = (RoutingContext)((CURD$delete.CURD$delete$1)$continuation).L$1;
                    this = (CURD)((CURD$delete.CURD$delete$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkUserAuth = $result;
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkUserAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final CURD curd2 = this;
            final JsonObject bodyAsJson = context.getBodyAsJson();
            Intrinsics.checkNotNullExpressionValue((Object)bodyAsJson, "context.bodyAsJson");
            final Object entity = curd2.convertToEntity(bodyAsJson);
            final String userNameSpace = this.getUserNS(context);
            final DB db = DB.Companion.table$default(DB.Companion, userNameSpace, this.getTableName(), null, 4, null);
            final ReturnData result = this.beforeDelete(entity, db);
            if (result != null) {
                return result;
            }
            db.delete((Function1)new CURD$delete.CURD$delete$2(this, entity));
            return ReturnData.setData$default(returnData, "", null, 2, null);
        }
        
        @Nullable
        public static <T> Object deleteMulti(@NotNull CURD<T> this, @NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
            final Continuation $continuation;
            Label_0049: {
                if ($completion instanceof CURD$deleteMulti.CURD$deleteMulti$1) {
                    final CURD$deleteMulti.CURD$deleteMulti$1 curd$deleteMulti$1 = (CURD$deleteMulti.CURD$deleteMulti$1)$completion;
                    if ((curd$deleteMulti$1.label & Integer.MIN_VALUE) != 0x0) {
                        final CURD$deleteMulti.CURD$deleteMulti$1 curd$deleteMulti$2 = curd$deleteMulti$1;
                        curd$deleteMulti$2.label -= Integer.MIN_VALUE;
                        break Label_0049;
                    }
                }
                $continuation = (Continuation)new CURD$deleteMulti.CURD$deleteMulti$1((Continuation)$completion);
            }
            final Object $result = ((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).result;
            final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ReturnData returnData = null;
            Object checkUserAuth = null;
            switch (((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final CURD curd = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).L$0 = this;
                    ((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).L$1 = context;
                    ((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).L$2 = returnData;
                    ((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).label = 1;
                    if ((checkUserAuth = curd.checkUserAuth(context2, $completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).L$2;
                    context = (RoutingContext)((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).L$1;
                    this = (CURD)((CURD$deleteMulti.CURD$deleteMulti$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkUserAuth = $result;
                    break;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkUserAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final Ref$ObjectRef ref$ObjectRef;
            final Ref$ObjectRef itemList = ref$ObjectRef = new Ref$ObjectRef();
            final CURD curd2 = this;
            final String bodyAsString = context.getBodyAsString();
            Intrinsics.checkNotNullExpressionValue((Object)bodyAsString, "context.bodyAsString");
            ref$ObjectRef.element = curd2.convertToEntityList(bodyAsString);
            if (((Object[])itemList.element).length == 0) {
                return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
            }
            final String userNameSpace = this.getUserNS(context);
            final DB db = DB.Companion.table$default(DB.Companion, userNameSpace, this.getTableName(), null, 4, null);
            final Object[] array = (Object[])itemList.element;
            int i = 0;
            while (i < array.length) {
                final Object item = array[i];
                ++i;
                final ReturnData result = this.beforeDelete(item, db);
                if (result != null) {
                    return result;
                }
            }
            db.delete((Function1)new CURD$deleteMulti.CURD$deleteMulti$2(itemList, this));
            return ReturnData.setData$default(returnData, "", null, 2, null);
        }
    }
}
