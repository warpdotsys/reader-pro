package com.htmake.reader.api.controller;

import com.htmake.reader.api.ReturnData;
import com.htmake.reader.db.DB;
import com.htmake.reader.utils.ExtKt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Array;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: CURD.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J%\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016¢\u0006\u0002\u0010\bJ%\u0010\t\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016¢\u0006\u0002\u0010\bJ%\u0010\n\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00028\u00002\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007H\u0016¢\u0006\u0002\u0010\bJ\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH¦@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u001d\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u0014J\u0015\u0010\u0015\u001a\u00028\u00002\u0006\u0010\u0011\u001a\u00020\u0012H\u0016¢\u0006\u0002\u0010\u0016J\u001b\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00182\u0006\u0010\u0011\u001a\u00020\u0019H\u0016¢\u0006\u0002\u0010\u001aJ\u0019\u0010\u001b\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0019\u0010\u001c\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u000e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00028\u00000\u001eH&J\b\u0010\u001f\u001a\u00020\u0019H&J\u0010\u0010 \u001a\u00020\u00192\u0006\u0010\r\u001a\u00020\u000eH&J\u0019\u0010!\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ%\u0010\"\u001a\u00020#2\u0006\u0010\u0011\u001a\u00028\u00002\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010$\u001a\u00020%H\u0016¢\u0006\u0002\u0010&J\u0018\u0010'\u001a\u00020%2\u0006\u0010\u0011\u001a\u00020%2\u0006\u0010(\u001a\u00020\u0019H\u0016J\u0019\u0010)\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0019\u0010*\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006+"}, d2 = {"Lcom/htmake/reader/api/controller/CURD;", "T", PackageDocumentBase.PREFIX_OPF, "beforeAdd", "Lcom/htmake/reader/api/ReturnData;", "val1", "db", "Lcom/htmake/reader/db/DB;", "(Ljava/lang/Object;Lcom/htmake/reader/db/DB;)Lcom/htmake/reader/api/ReturnData;", "beforeDelete", "beforeSave", "checkUserAuth", PackageDocumentBase.PREFIX_OPF, "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "var1", "Lio/vertx/core/json/JsonObject;", "var2", "(Lio/vertx/core/json/JsonObject;Ljava/lang/Object;)Z", "convertToEntity", "(Lio/vertx/core/json/JsonObject;)Ljava/lang/Object;", "convertToEntityList", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)[Ljava/lang/Object;", "delete", "deleteMulti", "getEntityClass", "Ljava/lang/Class;", "getTableName", "getUserNS", "list", "onCheckEnd", PackageDocumentBase.PREFIX_OPF, "var3", "Lio/vertx/core/json/JsonArray;", "(Ljava/lang/Object;ZLio/vertx/core/json/JsonArray;)V", "onList", "userNameSpace", "save", "saveMulti", "reader-pro"})
public interface CURD<T> {

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$delete$1, reason: invalid class name */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$delete$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "CURD.kt", l = {121}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "delete", c = "com.htmake.reader.api.controller.CURD$DefaultImpls")
    static final class AnonymousClass1<T> extends ContinuationImpl {
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
            return DefaultImpls.delete(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$deleteMulti$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$deleteMulti$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "CURD.kt", l = {143}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteMulti", c = "com.htmake.reader.api.controller.CURD$DefaultImpls")
    static final class C01051<T> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01051(Continuation<? super C01051> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return DefaultImpls.deleteMulti(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$list$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$list$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "CURD.kt", l = {57}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "list", c = "com.htmake.reader.api.controller.CURD$DefaultImpls")
    static final class C01071<T> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01071(Continuation<? super C01071> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return DefaultImpls.list(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$save$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$save$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "CURD.kt", l = {Wbxml.LITERAL_C}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "save", c = "com.htmake.reader.api.controller.CURD$DefaultImpls")
    static final class C01081<T> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01081(Continuation<? super C01081> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return DefaultImpls.save(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$saveMulti$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$saveMulti$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "CURD.kt", l = {92}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveMulti", c = "com.htmake.reader.api.controller.CURD$DefaultImpls")
    static final class C01101<T> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C01101(Continuation<? super C01101> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return DefaultImpls.saveMulti(null, null, (Continuation) this);
        }
    }

    @NotNull
    String getTableName();

    T convertToEntity(@NotNull JsonObject var1);

    @NotNull
    T[] convertToEntityList(@NotNull String var1);

    @NotNull
    JsonArray onList(@NotNull JsonArray var1, @NotNull String userNameSpace);

    boolean checker(@NotNull JsonObject var1, T var2);

    void onCheckEnd(T var1, boolean var2, @NotNull JsonArray var3);

    @Nullable
    ReturnData beforeSave(T val1, @NotNull DB<T> db);

    @Nullable
    ReturnData beforeAdd(T val1, @NotNull DB<T> db);

    @Nullable
    ReturnData beforeDelete(T val1, @NotNull DB<T> db);

    @Nullable
    Object checkUserAuth(@NotNull RoutingContext context, @NotNull Continuation<? super Boolean> $completion);

    @NotNull
    String getUserNS(@NotNull RoutingContext context);

    @NotNull
    Class<T> getEntityClass();

    @Nullable
    Object list(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion);

    @Nullable
    Object save(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion);

    @Nullable
    Object saveMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion);

    @Nullable
    Object delete(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion);

    @Nullable
    Object deleteMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion);

    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$DefaultImpls.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public static final class DefaultImpls {
        public static <T> T convertToEntity(@NotNull CURD<T> curd, @NotNull JsonObject jsonObject) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(jsonObject, "var1");
            return (T) jsonObject.mapTo(curd.getEntityClass());
        }

        @NotNull
        public static <T> T[] convertToEntityList(@NotNull CURD<T> curd, @NotNull String str) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(str, "var1");
            Object objFromJson = ExtKt.getGson().fromJson(str, Array.newInstance((Class<?>) curd.getEntityClass(), 0).getClass());
            Intrinsics.checkNotNullExpressionValue(objFromJson, "gson.fromJson(var1, clazz)");
            return (T[]) ((Object[]) objFromJson);
        }

        @NotNull
        public static <T> JsonArray onList(@NotNull CURD<T> curd, @NotNull JsonArray var1, @NotNull String userNameSpace) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(var1, "var1");
            Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
            return var1;
        }

        public static <T> void onCheckEnd(@NotNull CURD<T> curd, T var1, boolean var2, @NotNull JsonArray var3) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(var3, "var3");
        }

        @Nullable
        public static <T> ReturnData beforeSave(@NotNull CURD<T> curd, T val1, @NotNull DB<T> db) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(db, "db");
            return null;
        }

        @Nullable
        public static <T> ReturnData beforeAdd(@NotNull CURD<T> curd, T val1, @NotNull DB<T> db) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(db, "db");
            return null;
        }

        @Nullable
        public static <T> ReturnData beforeDelete(@NotNull CURD<T> curd, T val1, @NotNull DB<T> db) {
            Intrinsics.checkNotNullParameter(curd, "this");
            Intrinsics.checkNotNullParameter(db, "db");
            return null;
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static <T> Object list(@NotNull CURD<T> curd, @NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
            C01071 c01071;
            ReturnData returnData;
            Object objCheckUserAuth;
            if ($completion instanceof C01071) {
                c01071 = (C01071) $completion;
                if ((c01071.label & Integer.MIN_VALUE) != 0) {
                    c01071.label -= Integer.MIN_VALUE;
                } else {
                    c01071 = new C01071($completion);
                }
            }
            Object $result = c01071.result;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (c01071.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    c01071.L$0 = curd;
                    c01071.L$1 = context;
                    c01071.L$2 = returnData;
                    c01071.label = 1;
                    objCheckUserAuth = curd.checkUserAuth(context, c01071);
                    if (objCheckUserAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    returnData = (ReturnData) c01071.L$2;
                    context = (RoutingContext) c01071.L$1;
                    curd = (CURD) c01071.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckUserAuth = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (!((Boolean) objCheckUserAuth).booleanValue()) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }
            String userNameSpace = curd.getUserNS(context);
            JsonArray list = DB.Companion.table$default(DB.INSTANCE, userNameSpace, curd.getTableName(), null, 4, null).readAll();
            List list2 = curd.onList(list, userNameSpace).getList();
            Intrinsics.checkNotNullExpressionValue(list2, "list.getList()");
            return ReturnData.setData$default(returnData, list2, null, 2, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static <T> Object save(@NotNull CURD<T> curd, @NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
            C01081 c01081;
            ReturnData returnData;
            Object objCheckUserAuth;
            if ($completion instanceof C01081) {
                c01081 = (C01081) $completion;
                if ((c01081.label & Integer.MIN_VALUE) != 0) {
                    c01081.label -= Integer.MIN_VALUE;
                } else {
                    c01081 = new C01081($completion);
                }
            }
            Object $result = c01081.result;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (c01081.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    c01081.L$0 = curd;
                    c01081.L$1 = context;
                    c01081.L$2 = returnData;
                    c01081.label = 1;
                    objCheckUserAuth = curd.checkUserAuth(context, c01081);
                    if (objCheckUserAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    returnData = (ReturnData) c01081.L$2;
                    context = (RoutingContext) c01081.L$1;
                    curd = (CURD) c01081.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckUserAuth = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (!((Boolean) objCheckUserAuth).booleanValue()) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }
            JsonObject bodyAsJson = context.getBodyAsJson();
            Intrinsics.checkNotNullExpressionValue(bodyAsJson, "context.bodyAsJson");
            T tConvertToEntity = curd.convertToEntity(bodyAsJson);
            String userNameSpace = curd.getUserNS(context);
            DB<T> dbTable$default = DB.Companion.table$default(DB.INSTANCE, userNameSpace, curd.getTableName(), null, 4, null);
            ReturnData result = curd.beforeSave(tConvertToEntity, dbTable$default);
            if (result != null) {
                return result;
            }
            dbTable$default.save(tConvertToEntity, new C01092(curd), new AnonymousClass3(curd));
            return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static <T> Object saveMulti(@NotNull CURD<T> curd, @NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
            C01101 c01101;
            ReturnData returnData;
            Object objCheckUserAuth;
            if ($completion instanceof C01101) {
                c01101 = (C01101) $completion;
                if ((c01101.label & Integer.MIN_VALUE) != 0) {
                    c01101.label -= Integer.MIN_VALUE;
                } else {
                    c01101 = new C01101($completion);
                }
            }
            Object $result = c01101.result;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (c01101.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    c01101.L$0 = curd;
                    c01101.L$1 = context;
                    c01101.L$2 = returnData;
                    c01101.label = 1;
                    objCheckUserAuth = curd.checkUserAuth(context, c01101);
                    if (objCheckUserAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    returnData = (ReturnData) c01101.L$2;
                    context = (RoutingContext) c01101.L$1;
                    curd = (CURD) c01101.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckUserAuth = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (!((Boolean) objCheckUserAuth).booleanValue()) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }
            String bodyAsString = context.getBodyAsString();
            Intrinsics.checkNotNullExpressionValue(bodyAsString, "context.bodyAsString");
            T[] tArrConvertToEntityList = curd.convertToEntityList(bodyAsString);
            if (tArrConvertToEntityList.length == 0) {
                return returnData.setErrorMsg("参数错误");
            }
            String userNameSpace = curd.getUserNS(context);
            DB<T> dbTable$default = DB.Companion.table$default(DB.INSTANCE, userNameSpace, curd.getTableName(), null, 4, null);
            int i = 0;
            int length = tArrConvertToEntityList.length;
            while (i < length) {
                T t = tArrConvertToEntityList[i];
                i++;
                ReturnData result = curd.beforeSave(t, dbTable$default);
                if (result != null) {
                    return result;
                }
            }
            dbTable$default.saveMulti(tArrConvertToEntityList, new C01112(curd), new C01123(curd));
            return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static <T> Object delete(@NotNull CURD<T> curd, @NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
            AnonymousClass1 anonymousClass1;
            ReturnData returnData;
            Object objCheckUserAuth;
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
                    anonymousClass1.L$0 = curd;
                    anonymousClass1.L$1 = context;
                    anonymousClass1.L$2 = returnData;
                    anonymousClass1.label = 1;
                    objCheckUserAuth = curd.checkUserAuth(context, anonymousClass1);
                    if (objCheckUserAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    returnData = (ReturnData) anonymousClass1.L$2;
                    context = (RoutingContext) anonymousClass1.L$1;
                    curd = (CURD) anonymousClass1.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckUserAuth = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (!((Boolean) objCheckUserAuth).booleanValue()) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }
            JsonObject bodyAsJson = context.getBodyAsJson();
            Intrinsics.checkNotNullExpressionValue(bodyAsJson, "context.bodyAsJson");
            T tConvertToEntity = curd.convertToEntity(bodyAsJson);
            String userNameSpace = curd.getUserNS(context);
            DB<T> dbTable$default = DB.Companion.table$default(DB.INSTANCE, userNameSpace, curd.getTableName(), null, 4, null);
            ReturnData result = curd.beforeDelete(tConvertToEntity, dbTable$default);
            if (result != null) {
                return result;
            }
            dbTable$default.delete(new AnonymousClass2(curd, tConvertToEntity));
            return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static <T> Object deleteMulti(@NotNull CURD<T> curd, @NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
            C01051 c01051;
            ReturnData returnData;
            Object objCheckUserAuth;
            if ($completion instanceof C01051) {
                c01051 = (C01051) $completion;
                if ((c01051.label & Integer.MIN_VALUE) != 0) {
                    c01051.label -= Integer.MIN_VALUE;
                } else {
                    c01051 = new C01051($completion);
                }
            }
            Object $result = c01051.result;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (c01051.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    c01051.L$0 = curd;
                    c01051.L$1 = context;
                    c01051.L$2 = returnData;
                    c01051.label = 1;
                    objCheckUserAuth = curd.checkUserAuth(context, c01051);
                    if (objCheckUserAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    returnData = (ReturnData) c01051.L$2;
                    context = (RoutingContext) c01051.L$1;
                    curd = (CURD) c01051.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckUserAuth = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (!((Boolean) objCheckUserAuth).booleanValue()) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }
            Ref.ObjectRef itemList = new Ref.ObjectRef();
            String bodyAsString = context.getBodyAsString();
            Intrinsics.checkNotNullExpressionValue(bodyAsString, "context.bodyAsString");
            itemList.element = curd.convertToEntityList(bodyAsString);
            if (((Object[]) itemList.element).length == 0) {
                return returnData.setErrorMsg("参数错误");
            }
            String userNameSpace = curd.getUserNS(context);
            DB db = DB.Companion.table$default(DB.INSTANCE, userNameSpace, curd.getTableName(), null, 4, null);
            Object[] objArr = (Object[]) itemList.element;
            int i = 0;
            int length = objArr.length;
            while (i < length) {
                Object item = objArr[i];
                i++;
                ReturnData result = curd.beforeDelete(item, db);
                if (result != null) {
                    return result;
                }
            }
            db.delete(new C01062(itemList, curd));
            return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$save$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$save$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    /* synthetic */ class C01092 extends FunctionReferenceImpl implements Function3<T, Boolean, JsonArray, Unit> {
        C01092(CURD<T> curd) {
            super(3, curd, CURD.class, "onCheckEnd", "onCheckEnd(Ljava/lang/Object;ZLio/vertx/core/json/JsonArray;)V", 0);
        }

        public final void invoke(T p0, boolean p1, @NotNull JsonArray p2) {
            Intrinsics.checkNotNullParameter(p2, "p2");
            ((CURD) this.receiver).onCheckEnd(p0, p1, p2);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            invoke(p1, ((Boolean) p2).booleanValue(), (JsonArray) p3);
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$save$3, reason: invalid class name */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$save$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u0002H\u0002H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "jsonObject", "Lio/vertx/core/json/JsonObject;", "value"})
    static final class AnonymousClass3 extends Lambda implements Function2<JsonObject, T, Boolean> {
        final /* synthetic */ CURD<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(CURD<T> this$0) {
            super(2);
            this.this$0 = this$0;
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
            return Boolean.valueOf(invoke((JsonObject) p1, p2));
        }

        public final boolean invoke(@NotNull JsonObject jsonObject, T value) {
            Intrinsics.checkNotNullParameter(jsonObject, "jsonObject");
            return this.this$0.checker(jsonObject, value);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$saveMulti$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$saveMulti$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    /* synthetic */ class C01112 extends FunctionReferenceImpl implements Function3<T, Boolean, JsonArray, Unit> {
        C01112(CURD<T> curd) {
            super(3, curd, CURD.class, "onCheckEnd", "onCheckEnd(Ljava/lang/Object;ZLio/vertx/core/json/JsonArray;)V", 0);
        }

        public final void invoke(T p0, boolean p1, @NotNull JsonArray p2) {
            Intrinsics.checkNotNullParameter(p2, "p2");
            ((CURD) this.receiver).onCheckEnd(p0, p1, p2);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            invoke(p1, ((Boolean) p2).booleanValue(), (JsonArray) p3);
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$saveMulti$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$saveMulti$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u0002H\u0002H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "jsonObject", "Lio/vertx/core/json/JsonObject;", "value"})
    static final class C01123 extends Lambda implements Function2<JsonObject, T, Boolean> {
        final /* synthetic */ CURD<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01123(CURD<T> this$0) {
            super(2);
            this.this$0 = this$0;
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
            return Boolean.valueOf(invoke((JsonObject) p1, p2));
        }

        public final boolean invoke(@NotNull JsonObject jsonObject, T value) {
            Intrinsics.checkNotNullParameter(jsonObject, "jsonObject");
            return this.this$0.checker(jsonObject, value);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$delete$2, reason: invalid class name */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$delete$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "it", "Lio/vertx/core/json/JsonObject;"})
    static final class AnonymousClass2 extends Lambda implements Function1<JsonObject, Boolean> {
        final /* synthetic */ CURD<T> this$0;
        final /* synthetic */ T $entity;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(CURD<T> this$0, T $entity) {
            super(1);
            this.this$0 = this$0;
            this.$entity = $entity;
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1) {
            return Boolean.valueOf(invoke((JsonObject) p1));
        }

        public final boolean invoke(@NotNull JsonObject it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return this.this$0.checker(it, this.$entity);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.CURD$deleteMulti$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CURD.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/CURD$deleteMulti$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "it", "Lio/vertx/core/json/JsonObject;"})
    static final class C01062 extends Lambda implements Function1<JsonObject, Boolean> {
        final /* synthetic */ Ref.ObjectRef<T[]> $itemList;
        final /* synthetic */ CURD<T> this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01062(Ref.ObjectRef<T[]> $itemList, CURD<T> this$0) {
            super(1);
            this.$itemList = $itemList;
            this.this$0 = this$0;
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1) {
            return Boolean.valueOf(invoke((JsonObject) p1));
        }

        public final boolean invoke(@NotNull JsonObject jsonObject) {
            Intrinsics.checkNotNullParameter(jsonObject, "it");
            int i = 0;
            int length = ((Object[]) this.$itemList.element).length;
            if (0 < length) {
                do {
                    int i2 = i;
                    i++;
                    if (this.this$0.checker(jsonObject, (T) ((Object[]) this.$itemList.element)[i2])) {
                        return true;
                    }
                } while (i < length);
                return false;
            }
            return false;
        }
    }
}
