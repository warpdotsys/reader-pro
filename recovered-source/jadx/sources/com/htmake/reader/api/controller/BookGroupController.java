package com.htmake.reader.api.controller;

import com.htmake.reader.api.ReturnData;
import com.htmake.reader.api.controller.CURD;
import com.htmake.reader.db.DB;
import com.htmake.reader.utils.ExtKt;
import io.legado.app.data.entities.BookGroup;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.ResultKt;
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

/* JADX INFO: compiled from: BookGroupController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookGroupController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J \u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u0016J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J \u0010\u0019\u001a\u00020\u001a2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u0018\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\t\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u0017H\u0016J\u0019\u0010\u001f\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, d2 = {"Lcom/htmake/reader/api/controller/BookGroupController;", "Lcom/htmake/reader/api/controller/BaseController;", "Lcom/htmake/reader/api/controller/CURD;", "Lio/legado/app/data/entities/BookGroup;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "beforeSave", "Lcom/htmake/reader/api/ReturnData;", "var1", "db", "Lcom/htmake/reader/db/DB;", "checkUserAuth", PackageDocumentBase.PREFIX_OPF, "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "Lio/vertx/core/json/JsonObject;", "var2", "getEntityClass", "Ljava/lang/Class;", "getTableName", PackageDocumentBase.PREFIX_OPF, "getUserNS", "onCheckEnd", PackageDocumentBase.PREFIX_OPF, "bookGroupList", "Lio/vertx/core/json/JsonArray;", "onList", "userNameSpace", "saveBookGroupOrder", "reader-pro"})
public final class BookGroupController extends BaseController implements CURD<BookGroup> {

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookGroupController$saveBookGroupOrder$1, reason: invalid class name */
    /* JADX INFO: compiled from: BookGroupController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookGroupController$saveBookGroupOrder$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookGroupController.kt", l = {79}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBookGroupOrder", c = "com.htmake.reader.api.controller.BookGroupController")
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
            return BookGroupController.this.saveBookGroupOrder(null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BookGroupController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public ReturnData beforeAdd(@NotNull BookGroup val1, @NotNull DB<BookGroup> db) {
        return CURD.DefaultImpls.beforeAdd(this, val1, db);
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public ReturnData beforeDelete(@NotNull BookGroup val1, @NotNull DB<BookGroup> db) {
        return CURD.DefaultImpls.beforeDelete(this, val1, db);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public BookGroup convertToEntity(@NotNull JsonObject var1) {
        return (BookGroup) CURD.DefaultImpls.convertToEntity(this, var1);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public BookGroup[] convertToEntityList(@NotNull String var1) {
        return (BookGroup[]) CURD.DefaultImpls.convertToEntityList(this, var1);
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
        return "bookGroup";
    }

    @Override // com.htmake.reader.api.controller.CURD
    @NotNull
    public JsonArray onList(@NotNull JsonArray var1, @NotNull String userNameSpace) {
        JsonArray var2;
        Intrinsics.checkNotNullParameter(var1, "var1");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if (var1.size() == 0 && (var2 = ExtKt.asJsonArray("\n            [{\"groupId\":-1,\"groupName\":\"全部\",\"order\":-10,\"show\":true},{\"groupId\":-2,\"groupName\":\"本地\",\"order\":-9,\"show\":true},{\"groupId\":-3,\"groupName\":\"音频\",\"order\":-8,\"show\":true},{\"groupId\":-4,\"groupName\":\"未分组\",\"order\":-7,\"show\":true},{\"groupId\":-5,\"groupName\":\"更新错误\",\"order\":-6,\"show\":true}]\n            ")) != null) {
            saveUserStorage(userNameSpace, "bookGroup", var2);
            return var2;
        }
        return var1;
    }

    @Override // com.htmake.reader.api.controller.CURD
    public boolean checker(@NotNull JsonObject var1, @NotNull BookGroup var2) {
        Intrinsics.checkNotNullParameter(var1, "var1");
        Intrinsics.checkNotNullParameter(var2, "var2");
        return Long.valueOf(var2.getGroupId()).equals(var1.getLong("groupId"));
    }

    @Override // com.htmake.reader.api.controller.CURD
    public void onCheckEnd(@NotNull BookGroup var1, boolean var2, @NotNull JsonArray bookGroupList) {
        Long l;
        Integer integer;
        Intrinsics.checkNotNullParameter(var1, "var1");
        Intrinsics.checkNotNullParameter(bookGroupList, "bookGroupList");
        if (!var2) {
            int maxOrder = 0;
            long j = 0;
            for (Object it : (Iterable) bookGroupList) {
                long j2 = j;
                JsonObject jsonObjectAsJsonObject = ExtKt.asJsonObject(it);
                long jLongValue = (jsonObjectAsJsonObject == null || (l = jsonObjectAsJsonObject.getLong("groupId", 0L)) == null) ? 0L : l.longValue();
                long id = jLongValue;
                JsonObject jsonObjectAsJsonObject2 = ExtKt.asJsonObject(it);
                int iIntValue = (jsonObjectAsJsonObject2 == null || (integer = jsonObjectAsJsonObject2.getInteger("order", 0)) == null) ? 0 : integer.intValue();
                int order = iIntValue;
                maxOrder = order > maxOrder ? order : maxOrder;
                j = j2 + (id > 0 ? id : 0L);
            }
            long idsSum = j;
            long j3 = 1;
            while (true) {
                long id2 = j3;
                if ((id2 & idsSum) != 0) {
                    j3 = id2 << 1;
                } else {
                    var1.setGroupId(id2);
                    var1.setOrder(maxOrder + 1);
                    return;
                }
            }
        }
    }

    @Override // com.htmake.reader.api.controller.CURD
    @Nullable
    public ReturnData beforeSave(@NotNull BookGroup var1, @NotNull DB<BookGroup> db) {
        Intrinsics.checkNotNullParameter(var1, "var1");
        Intrinsics.checkNotNullParameter(db, "db");
        ReturnData returnData = new ReturnData();
        if (var1.getGroupName().length() == 0) {
            return returnData.setErrorMsg("分组名称不能为空");
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
    public Class<BookGroup> getEntityClass() {
        return BookGroup.class;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookGroupOrder(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
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
                this = (BookGroupController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        JsonArray bookGroupOrder = context.getBodyAsJson().getJsonArray("order", (JsonArray) null);
        if (bookGroupOrder == null) {
            return returnData.setErrorMsg("参数错误");
        }
        String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookGroupList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookGroup"));
        if (bookGroupList == null) {
            bookGroupList = new JsonArray();
        }
        Map orderMap = new LinkedHashMap();
        int i = 0;
        int size = bookGroupOrder.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                Long l = bookGroupOrder.getJsonObject(i2).getLong("groupId");
                Intrinsics.checkNotNullExpressionValue(l, "bookGroupOrder.getJsonObject(i).getLong(\"groupId\")");
                Integer integer = bookGroupOrder.getJsonObject(i2).getInteger("order");
                Intrinsics.checkNotNullExpressionValue(integer, "bookGroupOrder.getJsonObject(i).getInteger(\"order\")");
                orderMap.put(l, integer);
            } while (i < size);
        }
        List groupList = bookGroupList.getList();
        int i3 = 0;
        int size2 = bookGroupList.size();
        if (0 < size2) {
            do {
                int i4 = i3;
                i3++;
                BookGroup bookGroup = (BookGroup) bookGroupList.getJsonObject(i4).mapTo(BookGroup.class);
                if (orderMap.containsKey(Boxing.boxLong(bookGroup.getGroupId()))) {
                    Object obj = orderMap.get(Boxing.boxLong(bookGroup.getGroupId()));
                    Integer num = obj instanceof Integer ? (Integer) obj : null;
                    bookGroup.setOrder(num == null ? bookGroup.getOrder() : num.intValue());
                    groupList.set(i4, JsonObject.mapFrom(bookGroup));
                }
            } while (i3 < size2);
        }
        this.saveUserStorage(userNameSpace, "bookGroup", new JsonArray(groupList));
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }
}
