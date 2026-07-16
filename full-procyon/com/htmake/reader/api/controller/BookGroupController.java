// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import java.util.List;
import kotlin.coroutines.jvm.internal.Boxing;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.Nullable;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.db.DB;
import io.vertx.core.json.JsonObject;
import com.htmake.reader.utils.ExtKt;
import io.vertx.core.json.JsonArray;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;
import io.legado.app.data.entities.BookGroup;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J \u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u0016J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J \u0010\u0019\u001a\u00020\u001a2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u0018\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\t\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u0017H\u0016J\u0019\u0010\u001f\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006 " }, d2 = { "Lcom/htmake/reader/api/controller/BookGroupController;", "Lcom/htmake/reader/api/controller/BaseController;", "Lcom/htmake/reader/api/controller/CURD;", "Lio/legado/app/data/entities/BookGroup;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "beforeSave", "Lcom/htmake/reader/api/ReturnData;", "var1", "db", "Lcom/htmake/reader/db/DB;", "checkUserAuth", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checker", "Lio/vertx/core/json/JsonObject;", "var2", "getEntityClass", "Ljava/lang/Class;", "getTableName", "", "getUserNS", "onCheckEnd", "", "bookGroupList", "Lio/vertx/core/json/JsonArray;", "onList", "userNameSpace", "saveBookGroupOrder", "reader-pro" })
public final class BookGroupController extends BaseController implements CURD<BookGroup>
{
    public BookGroupController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
    }
    
    @NotNull
    @Override
    public String getTableName() {
        return "bookGroup";
    }
    
    @NotNull
    @Override
    public JsonArray onList(@NotNull final JsonArray var1, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        if (var1.size() == 0) {
            final JsonArray var2 = ExtKt.asJsonArray("\n            [{\"groupId\":-1,\"groupName\":\"\u5168\u90e8\",\"order\":-10,\"show\":true},{\"groupId\":-2,\"groupName\":\"\u672c\u5730\",\"order\":-9,\"show\":true},{\"groupId\":-3,\"groupName\":\"\u97f3\u9891\",\"order\":-8,\"show\":true},{\"groupId\":-4,\"groupName\":\"\u672a\u5206\u7ec4\",\"order\":-7,\"show\":true},{\"groupId\":-5,\"groupName\":\"\u66f4\u65b0\u9519\u8bef\",\"order\":-6,\"show\":true}]\n            ");
            if (var2 != null) {
                this.saveUserStorage(userNameSpace, "bookGroup", var2);
                return var2;
            }
        }
        return var1;
    }
    
    @Override
    public boolean checker(@NotNull final JsonObject var1, @NotNull final BookGroup var2) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)var2, "var2");
        return Long.valueOf(var2.getGroupId()).equals(var1.getLong("groupId"));
    }
    
    @Override
    public void onCheckEnd(@NotNull final BookGroup var1, final boolean var2, @NotNull final JsonArray bookGroupList) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "var1"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_3         /* bookGroupList */
        //     7: ldc             "bookGroupList"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: iload_2         /* var2 */
        //    13: ifne            241
        //    16: iconst_0       
        //    17: istore          maxOrder
        //    19: aload_3         /* bookGroupList */
        //    20: checkcast       Ljava/lang/Iterable;
        //    23: astore          7
        //    25: iconst_0       
        //    26: istore          8
        //    28: lconst_0       
        //    29: lstore          9
        //    31: aload           7
        //    33: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    38: astore          11
        //    40: aload           11
        //    42: invokeinterface java/util/Iterator.hasNext:()Z
        //    47: ifeq            201
        //    50: aload           11
        //    52: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    57: astore          12
        //    59: lload           9
        //    61: aload           12
        //    63: astore          13
        //    65: lstore          20
        //    67: iconst_0       
        //    68: istore          $i$a$-sumOfLong-BookGroupController$onCheckEnd$idsSum$1
        //    70: aload           it
        //    72: invokestatic    com/htmake/reader/utils/ExtKt.asJsonObject:(Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;
        //    75: astore          15
        //    77: aload           15
        //    79: ifnonnull       86
        //    82: lconst_0       
        //    83: goto            113
        //    86: aload           15
        //    88: ldc             "groupId"
        //    90: lconst_0       
        //    91: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //    94: invokevirtual   io/vertx/core/json/JsonObject.getLong:(Ljava/lang/String;Ljava/lang/Long;)Ljava/lang/Long;
        //    97: astore          16
        //    99: aload           16
        //   101: ifnonnull       108
        //   104: lconst_0       
        //   105: goto            113
        //   108: aload           16
        //   110: invokevirtual   java/lang/Long.longValue:()J
        //   113: lstore          id
        //   115: aload           it
        //   117: invokestatic    com/htmake/reader/utils/ExtKt.asJsonObject:(Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;
        //   120: astore          16
        //   122: aload           16
        //   124: ifnonnull       131
        //   127: iconst_0       
        //   128: goto            158
        //   131: aload           16
        //   133: ldc             "order"
        //   135: iconst_0       
        //   136: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   139: invokevirtual   io/vertx/core/json/JsonObject.getInteger:(Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Integer;
        //   142: astore          19
        //   144: aload           19
        //   146: ifnonnull       153
        //   149: iconst_0       
        //   150: goto            158
        //   153: aload           19
        //   155: invokevirtual   java/lang/Integer.intValue:()I
        //   158: istore          order
        //   160: iload           order
        //   162: iload           maxOrder
        //   164: if_icmple       172
        //   167: iload           order
        //   169: goto            174
        //   172: iload           maxOrder
        //   174: istore          maxOrder
        //   176: lload           id
        //   178: lconst_0       
        //   179: lcmp           
        //   180: ifle            188
        //   183: lload           id
        //   185: goto            189
        //   188: lconst_0       
        //   189: lstore          22
        //   191: lload           20
        //   193: lload           22
        //   195: ladd           
        //   196: lstore          9
        //   198: goto            40
        //   201: lload           9
        //   203: lstore          idsSum
        //   205: lconst_1       
        //   206: lstore          id
        //   208: lload           id
        //   210: lload           idsSum
        //   212: land           
        //   213: lconst_0       
        //   214: lcmp           
        //   215: ifeq            227
        //   218: lload           id
        //   220: iconst_1       
        //   221: lshl           
        //   222: lstore          id
        //   224: goto            208
        //   227: aload_1         /* var1 */
        //   228: lload           id
        //   230: invokevirtual   io/legado/app/data/entities/BookGroup.setGroupId:(J)V
        //   233: aload_1         /* var1 */
        //   234: iload           maxOrder
        //   236: iconst_1       
        //   237: iadd           
        //   238: invokevirtual   io/legado/app/data/entities/BookGroup.setOrder:(I)V
        //   241: return         
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  var1           
        //  var2           
        //  bookGroupList  
        //    StackMapTable: 00 0F FF 00 28 00 0B 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 00 00 FF 00 2D 00 14 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 07 00 49 00 00 00 00 04 00 00 FF 00 15 00 14 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 07 00 49 07 00 41 00 00 00 04 00 00 FF 00 04 00 14 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 07 00 49 00 00 00 00 04 00 01 04 FF 00 11 00 13 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 07 00 49 07 00 49 04 00 04 00 00 FF 00 15 00 13 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 07 00 49 07 00 49 04 07 00 77 04 00 00 FF 00 04 00 13 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 07 00 49 07 00 49 04 00 04 00 01 01 FF 00 0D 00 13 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 07 00 4F 07 00 4F 01 01 07 00 49 04 00 04 00 00 41 01 0D 40 04 FF 00 0B 00 0B 07 00 02 07 00 3B 01 07 00 23 01 00 00 07 00 5B 01 04 07 00 61 00 00 FF 00 06 00 09 07 00 02 07 00 3B 01 07 00 23 01 04 04 04 07 00 61 00 00 12 FF 00 0D 00 04 07 00 02 07 00 3B 01 07 00 23 00 00
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
    @Override
    public ReturnData beforeSave(@NotNull final BookGroup var1, @NotNull final DB<BookGroup> db) {
        Intrinsics.checkNotNullParameter((Object)var1, "var1");
        Intrinsics.checkNotNullParameter((Object)db, "db");
        final ReturnData returnData = new ReturnData();
        if (var1.getGroupName().length() == 0) {
            return returnData.setErrorMsg("\u5206\u7ec4\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a");
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
    public Class<BookGroup> getEntityClass() {
        return BookGroup.class;
    }
    
    @Nullable
    public final Object saveBookGroupOrder(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1) {
                final BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1 bookGroupController$saveBookGroupOrder$1 = (BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$completion;
                if ((bookGroupController$saveBookGroupOrder$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1 bookGroupController$saveBookGroupOrder$2 = bookGroupController$saveBookGroupOrder$1;
                    bookGroupController$saveBookGroupOrder$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookGroupController bookGroupController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).L$0 = this;
                ((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).L$1 = context;
                ((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).L$2 = returnData;
                ((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).label = 1;
                if ((checkAuth = bookGroupController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).L$2;
                context = (RoutingContext)((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).L$1;
                this = (BookGroupController)((BookGroupController$saveBookGroupOrder.BookGroupController$saveBookGroupOrder$1)$continuation).L$0;
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
        final JsonArray bookGroupOrder = context.getBodyAsJson().getJsonArray("order", (JsonArray)null);
        if (bookGroupOrder == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookGroupList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookGroup"));
        if (bookGroupList == null) {
            bookGroupList = new JsonArray();
        }
        final Map orderMap = new LinkedHashMap();
        int k = 0;
        final int size = bookGroupOrder.size();
        if (k < size) {
            do {
                final int i = k;
                ++k;
                final Map map = orderMap;
                final Long long1 = bookGroupOrder.getJsonObject(i).getLong("groupId");
                Intrinsics.checkNotNullExpressionValue((Object)long1, "bookGroupOrder.getJsonObject(i).getLong(\"groupId\")");
                final Long n = long1;
                final Integer integer = bookGroupOrder.getJsonObject(i).getInteger("order");
                Intrinsics.checkNotNullExpressionValue((Object)integer, "bookGroupOrder.getJsonObject(i).getInteger(\"order\")");
                map.put(n, integer);
            } while (k < size);
        }
        final List groupList = bookGroupList.getList();
        int l = 0;
        final int size2 = bookGroupList.size();
        if (l < size2) {
            do {
                final int j = l;
                ++l;
                final BookGroup bookGroup = (BookGroup)bookGroupList.getJsonObject(j).mapTo((Class)BookGroup.class);
                if (orderMap.containsKey(Boxing.boxLong(bookGroup.getGroupId()))) {
                    final BookGroup bookGroup2 = bookGroup;
                    final Integer value = orderMap.get(Boxing.boxLong(bookGroup.getGroupId()));
                    final Integer n2 = (value instanceof Integer) ? value : null;
                    bookGroup2.setOrder((n2 == null) ? bookGroup.getOrder() : ((int)n2));
                    groupList.set(j, JsonObject.mapFrom((Object)bookGroup));
                }
            } while (l < size2);
        }
        bookGroupList = new JsonArray(groupList);
        this.saveUserStorage(userNameSpace, "bookGroup", bookGroupList);
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    @Nullable
    @Override
    public ReturnData beforeAdd(@NotNull final BookGroup val1, @NotNull final DB<BookGroup> db) {
        return DefaultImpls.beforeAdd(this, val1, db);
    }
    
    @Nullable
    @Override
    public ReturnData beforeDelete(@NotNull final BookGroup val1, @NotNull final DB<BookGroup> db) {
        return DefaultImpls.beforeDelete(this, val1, db);
    }
    
    @NotNull
    @Override
    public BookGroup convertToEntity(@NotNull final JsonObject var1) {
        return DefaultImpls.convertToEntity((CURD<BookGroup>)this, var1);
    }
    
    @NotNull
    @Override
    public BookGroup[] convertToEntityList(@NotNull final String var1) {
        return DefaultImpls.convertToEntityList((CURD<BookGroup>)this, var1);
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
