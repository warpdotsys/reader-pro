package com.htmake.reader.api.controller;

import com.htmake.reader.api.ReturnData;
import com.htmake.reader.api.controller.CURD;
import com.htmake.reader.db.DB;
import com.htmake.reader.utils.ExtKt;
import io.legado.app.data.entities.HttpTTS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: HttpTTSController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/HttpTTSController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J \u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u0016J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u0010\u0010\u0014\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0012H\u0016J\u001b\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u00162\u0006\u0010\t\u001a\u00020\u0017H\u0016¢\u0006\u0002\u0010\u0018J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00030\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0017H\u0016J\u0010\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fH\u0016\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001d"}, d2 = {"Lcom/htmake/reader/api/controller/HttpTTSController;", "Lcom/htmake/reader/api/controller/BaseController;", "Lcom/htmake/reader/api/controller/CURD;", "Lio/legado/app/data/entities/HttpTTS;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "beforeSave", "Lcom/htmake/reader/api/ReturnData;", "var1", "db", "Lcom/htmake/reader/db/DB;", "checkUserAuth", PackageDocumentBase.PREFIX_OPF, "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "Lio/vertx/core/json/JsonObject;", "var2", "convertToEntity", "convertToEntityList", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)[Lio/legado/app/data/entities/HttpTTS;", "getEntityClass", "Ljava/lang/Class;", "getTableName", "getUserNS", "reader-pro"})
public final class HttpTTSController extends BaseController implements CURD<HttpTTS> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HttpTTSController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public ReturnData beforeAdd(@NotNull HttpTTS val1, @NotNull DB<HttpTTS> db) {
        return CURD.DefaultImpls.beforeAdd(this, val1, db);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public ReturnData beforeDelete(@NotNull HttpTTS val1, @NotNull DB<HttpTTS> db) {
        return CURD.DefaultImpls.beforeDelete(this, val1, db);
    }

    @Override // com.htmake.reader.api.controller.CURD
    public void onCheckEnd(@NotNull HttpTTS var1, boolean var2, @NotNull JsonArray var3) {
        CURD.DefaultImpls.onCheckEnd(this, var1, var2, var3);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public JsonArray onList(@NotNull JsonArray var1, @NotNull String userNameSpace) {
        return CURD.DefaultImpls.onList(this, var1, userNameSpace);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public Object delete(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        return CURD.DefaultImpls.delete(this, context, $completion);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public Object deleteMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        return CURD.DefaultImpls.deleteMulti(this, context, $completion);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public Object list(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        return CURD.DefaultImpls.list(this, context, $completion);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public Object save(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        return CURD.DefaultImpls.save(this, context, $completion);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public Object saveMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        return CURD.DefaultImpls.saveMulti(this, context, $completion);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public String getTableName() {
        return "httpTTS";
    }

    @Override // com.htmake.reader.api.controller.CURD
    public boolean checker(@NotNull JsonObject var1, @NotNull HttpTTS var2) {
        Intrinsics.checkNotNullParameter(var1, "var1");
        Intrinsics.checkNotNullParameter(var2, "var2");
        return var2.getName().equals(var1.getString("name"));
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public ReturnData beforeSave(@NotNull HttpTTS var1, @NotNull DB<HttpTTS> db) {
        Intrinsics.checkNotNullParameter(var1, "var1");
        Intrinsics.checkNotNullParameter(db, "db");
        ReturnData returnData = new ReturnData();
        if (var1.getName().length() == 0) {
            return returnData.setErrorMsg("名称不能为空");
        }
        if (var1.getUrl().length() == 0) {
            return returnData.setErrorMsg("链接不能为空");
        }
        return null;
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public Object checkUserAuth(@NotNull RoutingContext context, @NotNull Continuation<? super Boolean> $completion) {
        return checkAuth(context, $completion);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public String getUserNS(@NotNull RoutingContext context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return getUserNameSpace(context);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public Class<HttpTTS> getEntityClass() {
        return HttpTTS.class;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public HttpTTS convertToEntity(@NotNull JsonObject var1) {
        Intrinsics.checkNotNullParameter(var1, "var1");
        HttpTTS.Companion companion = HttpTTS.INSTANCE;
        String string = var1.toString();
        Intrinsics.checkNotNullExpressionValue(string, "var1.toString()");
        Object objM156fromJsonIoAF18A = companion.m156fromJsonIoAF18A(string);
        Object obj = Result.isFailure-impl(objM156fromJsonIoAF18A) ? null : objM156fromJsonIoAF18A;
        Intrinsics.checkNotNull(obj);
        return (HttpTTS) obj;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public HttpTTS[] convertToEntityList(@NotNull String var1) {
        Intrinsics.checkNotNullParameter(var1, "var1");
        Iterable iterableAsJsonArray = ExtKt.asJsonArray(var1);
        List list = new ArrayList();
        if (iterableAsJsonArray != null) {
            Iterable $this$forEach$iv = iterableAsJsonArray;
            for (Object element$iv : $this$forEach$iv) {
                Object objM156fromJsonIoAF18A = HttpTTS.INSTANCE.m156fromJsonIoAF18A(element$iv.toString());
                Object obj = Result.isFailure-impl(objM156fromJsonIoAF18A) ? null : objM156fromJsonIoAF18A;
                Intrinsics.checkNotNull(obj);
                list.add(obj);
            }
        }
        List $this$toTypedArray$iv = list;
        Object[] array = $this$toTypedArray$iv.toArray(new HttpTTS[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (HttpTTS[]) array;
    }
}
