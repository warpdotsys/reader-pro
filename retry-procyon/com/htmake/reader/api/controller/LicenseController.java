// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import kotlinx.coroutines.Job;
import java.util.LinkedHashMap;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.json.JsonObject;
import java.util.UUID;
import com.htmake.reader.entity.ActiveLicense;
import io.vertx.core.json.JsonArray;
import java.security.PrivateKey;
import java.util.List;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.collections.CollectionsKt;
import io.vertx.core.http.HttpMethod;
import java.security.KeyPair;
import io.legado.app.utils.Base64;
import kotlin.Pair;
import io.legado.app.utils.EncoderUtils;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.Dispatchers;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.Map;
import kotlinx.coroutines.slf4j.MDCContext;
import kotlinx.coroutines.CoroutineScope;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.coroutines.CoroutineContext$Key;
import kotlinx.coroutines.CoroutineExceptionHandler$Key;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlinx.coroutines.CoroutineExceptionHandler;
import com.htmake.reader.utils.VertExtKt;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.Unit;
import org.jetbrains.annotations.Nullable;
import com.htmake.reader.entity.License;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.api.ReturnData;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import io.legado.app.utils.ACache;
import org.jetbrains.annotations.NotNull;
import kotlin.Lazy;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0019\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u001dJ\u0019\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010 \u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010!\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010\"\u001a\u00020\u001a2\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010#\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010$\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010%\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018J\u0019\u0010&\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0018R!\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068FX\u0086\u0084\u0002?\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\u0007X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e?\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002?\u0006\f\n\u0004\b\u0013\u0010\u000b\u001a\u0004\b\u0011\u0010\u0012\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006'" }, d2 = { "Lcom/htmake/reader/api/controller/LicenseController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "backupFileNames", "", "", "getBackupFileNames", "()[Ljava/lang/String;", "backupFileNames$delegate", "Lkotlin/Lazy;", "privateKeyContent", "tryCodeCache", "Lio/legado/app/utils/ACache;", "webClient", "Lio/vertx/ext/web/client/WebClient;", "getWebClient", "()Lio/vertx/ext/web/client/WebClient;", "webClient$delegate", "activateLicense", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkLicense", "", "license", "Lcom/htmake/reader/entity/License;", "(Lcom/htmake/reader/entity/License;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "decryptLicense", "generateKeys", "generateLicense", "getLicense", "importLicense", "isHostValid", "isLicenseValid", "sendCodeToEmail", "supplyLicense", "reader-pro" })
public final class LicenseController extends BaseController
{
    @NotNull
    private final Lazy webClient$delegate;
    @NotNull
    private String privateKeyContent;
    @NotNull
    private ACache tryCodeCache;
    @NotNull
    private final Lazy backupFileNames$delegate;
    
    public LicenseController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
        this.webClient$delegate = LazyKt.lazy((Function0)LicenseController$webClient.LicenseController$webClient$2.INSTANCE);
        this.privateKeyContent = "";
        this.tryCodeCache = ACache.Companion.get("tryCodeCache", 2000000L, 10000);
        this.backupFileNames$delegate = LazyKt.lazy((Function0)LicenseController$backupFileNames.LicenseController$backupFileNames$2.INSTANCE);
    }
    
    private final WebClient getWebClient() {
        final Object value = this.webClient$delegate.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-webClient>(...)");
        return (WebClient)value;
    }
    
    @NotNull
    public final String[] getBackupFileNames() {
        return (String[])this.backupFileNames$delegate.getValue();
    }
    
    @Nullable
    public final Object getLicense(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        final License license = ExtKt.getInstalledLicense$default(false, 1, null);
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(TuplesKt.to((Object)"license", (Object)license)), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object importLicense(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof LicenseController$importLicense.LicenseController$importLicense$1) {
                final LicenseController$importLicense.LicenseController$importLicense$1 licenseController$importLicense$1 = (LicenseController$importLicense.LicenseController$importLicense$1)$completion;
                if ((licenseController$importLicense$1.label & Integer.MIN_VALUE) != 0x0) {
                    final LicenseController$importLicense.LicenseController$importLicense$1 licenseController$importLicense$2 = licenseController$importLicense$1;
                    licenseController$importLicense$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new LicenseController$importLicense.LicenseController$importLicense$1(this, (Continuation)$completion);
        }
        final Object $result = ((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final LicenseController licenseController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).L$0 = this;
                ((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).L$1 = context;
                ((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).L$2 = returnData;
                ((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).label = 1;
                if ((checkAuth = licenseController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).L$2;
                context = (RoutingContext)((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).L$1;
                this = (LicenseController)((LicenseController$importLicense.LicenseController$importLicense$1)$continuation).L$0;
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
        if (!this.checkManagerAuth(context)) {
            VertExtKt.success(context, ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801"));
            return Unit.INSTANCE;
        }
        final String string = context.getBodyAsJson().getString("content");
        final String content = (string == null) ? "" : string;
        if (content.length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("\u8bf7\u8f93\u5165\u5bc6\u94a5"));
            return Unit.INSTANCE;
        }
        final int $i$f$CoroutineExceptionHandler = 0;
        final CoroutineExceptionHandler exceptionHandler = (CoroutineExceptionHandler)new CoroutineExceptionHandler(CoroutineExceptionHandler.Key, context, returnData) {
            public void handleException(@NotNull final CoroutineContext context, @NotNull final Throwable exception) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_2         /* exception */
                //     2: astore_3       
                //     3: astore          ctx
                //     5: iconst_0       
                //     6: istore          $i$a$-CoroutineExceptionHandler-LicenseController$importLicense$exceptionHandler$1
                //     8: invokestatic    com/htmake/reader/api/controller/LicenseControllerKt.access$getLogger$p:()Lmu/KLogger;
                //    11: ldc             "activate license error: {}"
                //    13: aload_3         /* ex */
                //    14: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
                //    17: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
                //    22: aload_0         /* this */
                //    23: getfield        com/htmake/reader/api/controller/LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1.$context$inlined:Lio/vertx/ext/web/RoutingContext;
                //    26: aload_0         /* this */
                //    27: getfield        com/htmake/reader/api/controller/LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1.$returnData$inlined:Lcom/htmake/reader/api/ReturnData;
                //    30: ldc             "\u5bc6\u94a5\u6fc0\u6d3b\u5931\u8d25: "
                //    32: aload_3         /* ex */
                //    33: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
                //    36: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
                //    39: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
                //    42: invokestatic    com/htmake/reader/utils/VertExtKt.success:(Lio/vertx/ext/web/RoutingContext;Ljava/lang/Object;)V
                //    45: nop            
                //    46: return         
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
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
        };
        final Ref$ObjectRef checkUrl = new Ref$ObjectRef();
        checkUrl.element = "https://r.htmake.com/reader3/activateLicense";
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()).plus((CoroutineContext)exceptionHandler), (CoroutineStart)null, (Function2)new LicenseController$importLicense.LicenseController$importLicense$2(this, checkUrl, content, context, returnData, (Continuation)null), 2, (Object)null);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object generateKeys(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        final KeyPair keyPair = EncoderUtils.INSTANCE.generateKeys();
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"publicKey", (Object)Base64.encodeToString(keyPair.getPublic().getEncoded(), 2)), TuplesKt.to((Object)"privateKey", (Object)Base64.encodeToString(keyPair.getPrivate().getEncoded(), 2)) }), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object generateLicense(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        String host = null;
        long expiredAt = 0L;
        int userMaxLimit = 0;
        boolean openApi = false;
        long simpleWebExpiredAt = 0L;
        int instances = 0;
        String type = null;
        String key = null;
        String code = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("host");
            host = ((string == null) ? "" : string);
            final Long long1 = context.getBodyAsJson().getLong("expiredAt");
            expiredAt = ((long1 == null) ? 0L : long1);
            final Integer integer = context.getBodyAsJson().getInteger("userMaxLimit");
            userMaxLimit = ((integer == null) ? 15 : integer);
            final Boolean boolean1 = context.getBodyAsJson().getBoolean("openApi");
            openApi = (boolean1 != null && boolean1);
            final Long long2 = context.getBodyAsJson().getLong("simpleWebExpiredAt");
            simpleWebExpiredAt = ((long2 == null) ? 0L : long2);
            final Integer integer2 = context.getBodyAsJson().getInteger("instances");
            instances = ((integer2 == null) ? 1 : integer2);
            final String string2 = context.getBodyAsJson().getString("type");
            type = ((string2 == null) ? "" : string2);
            final String string3 = context.getBodyAsJson().getString("key");
            key = ((string3 == null) ? "" : string3);
            final String string4 = context.getBodyAsJson().getString("code");
            code = ((string4 == null) ? "" : string4);
        }
        else {
            final List queryParam = context.queryParam("host");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"host\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            host = ((s == null) ? "" : s);
            final List queryParam2 = context.queryParam("expiredAt");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"expiredAt\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
            long n;
            if (s2 == null) {
                n = 0L;
            }
            else {
                final Long boxLong = Boxing.boxLong(Long.parseLong(s2));
                n = ((boxLong == null) ? 0L : boxLong);
            }
            expiredAt = n;
            final List queryParam3 = context.queryParam("userMaxLimit");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"userMaxLimit\")");
            final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
            int n2;
            if (s3 == null) {
                n2 = 15;
            }
            else {
                final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                n2 = ((boxInt == null) ? 15 : boxInt);
            }
            userMaxLimit = n2;
            final List queryParam4 = context.queryParam("openApi");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"openApi\")");
            final String s4 = (String)CollectionsKt.firstOrNull(queryParam4);
            boolean b;
            if (s4 == null) {
                b = false;
            }
            else {
                final Boolean boxBoolean = Boxing.boxBoolean(Boolean.parseBoolean(s4));
                b = (boxBoolean != null && boxBoolean);
            }
            openApi = b;
            final List queryParam5 = context.queryParam("simpleWebExpiredAt");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam5, "context.queryParam(\"simpleWebExpiredAt\")");
            final String s5 = (String)CollectionsKt.firstOrNull(queryParam5);
            long n3;
            if (s5 == null) {
                n3 = 0L;
            }
            else {
                final Long boxLong2 = Boxing.boxLong(Long.parseLong(s5));
                n3 = ((boxLong2 == null) ? 0L : boxLong2);
            }
            simpleWebExpiredAt = n3;
            final List queryParam6 = context.queryParam("instances");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam6, "context.queryParam(\"instances\")");
            final String s6 = (String)CollectionsKt.firstOrNull(queryParam6);
            int n4;
            if (s6 == null) {
                n4 = 1;
            }
            else {
                final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s6));
                n4 = ((boxInt2 == null) ? 1 : boxInt2);
            }
            instances = n4;
            final List queryParam7 = context.queryParam("type");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam7, "context.queryParam(\"type\")");
            final String s7 = (String)CollectionsKt.firstOrNull(queryParam7);
            type = ((s7 == null) ? "" : s7);
            final List queryParam8 = context.queryParam("key");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam8, "context.queryParam(\"key\")");
            final String s8 = (String)CollectionsKt.firstOrNull(queryParam8);
            key = ((s8 == null) ? "" : s8);
            final List queryParam9 = context.queryParam("code");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam9, "context.queryParam(\"code\")");
            final String s9 = (String)CollectionsKt.firstOrNull(queryParam9);
            code = ((s9 == null) ? "" : s9);
        }
        if (host.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u57df\u540d");
        }
        if (!"Pvkp7tMQJpi4kWBE".equals(key)) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final License license = new License(host, userMaxLimit, expiredAt, openApi, simpleWebExpiredAt, instances, type, (String)null, (String)null, false, (Long)null, 1408, (DefaultConstructorMarker)null);
        if (code.length() != 0) {
            license.setCode(code);
        }
        final String licenseContent = ExtKt.jsonEncode$default(license, false, 2, null);
        if (this.privateKeyContent.length() == 0) {
            final String storage = ExtKt.getStorage(new String[] { "data", "privateKey" }, ".key");
            this.privateKeyContent = ((storage == null) ? "" : storage);
        }
        final PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
        final EncoderUtils instance = EncoderUtils.INSTANCE;
        final String s10 = licenseContent;
        Intrinsics.checkNotNullExpressionValue((Object)privateKey, "privateKey");
        final String licenseKey = EncoderUtils.encryptSegmentByPrivateKey$default(instance, s10, privateKey, 0, 4, null);
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"host", (Object)host), TuplesKt.to((Object)"key", (Object)licenseKey) }), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object isHostValid(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        String host = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("host");
            Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"host\")");
            host = string;
        }
        else {
            final List queryParam = context.queryParam("host");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"host\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            host = ((s == null) ? "" : s);
        }
        final License license = ExtKt.getInstalledLicense$default(false, 1, null);
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(TuplesKt.to((Object)"isValid", (Object)Boxing.boxBoolean(license.validHost(host)))), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object decryptLicense(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        final String string = context.getBodyAsJson().getString("content");
        final String content = (string == null) ? "" : string;
        if (content.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u5bc6\u94a5");
        }
        final License license = ExtKt.decryptToLicense(content);
        if (license == null) {
            return returnData.setErrorMsg("\u5bc6\u94a5\u9519\u8bef");
        }
        return ReturnData.setData$default(returnData, (Object)license, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object activateLicense(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        final String string = context.getBodyAsJson().getString("content");
        final String content = (string == null) ? "" : string;
        if (content.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u5bc6\u94a5");
        }
        final License license = ExtKt.decryptToLicense(content);
        if (license == null) {
            return returnData.setErrorMsg("\u5bc6\u94a5\u9519\u8bef");
        }
        if (license.getVerified()) {
            return returnData.setErrorMsg("\u5bc6\u94a5\u5df2\u88ab\u4f7f\u7528");
        }
        JsonArray activeLicenseList = ExtKt.asJsonArray(ExtKt.getStorage$default(new String[] { "data", "activeLicense" }, null, 2, null));
        if (activeLicenseList == null) {
            activeLicenseList = new JsonArray();
        }
        int activeTimes = 0;
        int j = 0;
        final int size = activeLicenseList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final ActiveLicense _activeLicense = (ActiveLicense)activeLicenseList.getJsonObject(i).mapTo((Class)ActiveLicense.class);
                if (_activeLicense.getType().equals(license.getType()) && _activeLicense.getCode().equals(license.getCode())) {
                    ++activeTimes;
                }
            } while (j < size);
        }
        if (activeTimes >= license.getInstances()) {
            return returnData.setErrorMsg("\u5bc6\u94a5\u5df2\u8d85\u8fc7\u6700\u5927\u4f7f\u7528\u6b21\u6570");
        }
        Object ip = null;
        ip = context.request().getHeader("X-Real-IP");
        if (ip == null || ((CharSequence)ip).length() == 0) {
            final SocketAddress remoteAddress = context.request().remoteAddress();
            Object o;
            if (remoteAddress == null) {
                o = "";
            }
            else {
                final String host = remoteAddress.host();
                o = ((host == null) ? "" : host);
            }
            ip = o;
        }
        license.setVerified(true);
        license.setVerifyTime(Boxing.boxLong(System.currentTimeMillis()));
        final License license2 = license;
        final String string2 = UUID.randomUUID().toString();
        Intrinsics.checkNotNullExpressionValue((Object)string2, "randomUUID().toString()");
        license2.setId(string2);
        final ActiveLicense it = license.toActiveLicense();
        final int n = 0;
        it.setActiveOrder(activeTimes + 1);
        it.setActiveTime(System.currentTimeMillis());
        it.setActiveIp((String)ip);
        it.setActiveEmail("");
        final ActiveLicense activeLicense = it;
        final JsonObject map = JsonObject.mapFrom((Object)activeLicense);
        Intrinsics.checkNotNullExpressionValue((Object)map, "mapFrom(activeLicense)");
        final JsonObject activeLicenseObject = map;
        activeLicenseList.add(activeLicenseObject);
        LicenseControllerKt.access$getLogger$p().info("activeLicenseList: {}", (Object)activeLicenseList);
        ExtKt.saveStorage$default(new String[] { "data", "activeLicense" }, activeLicenseList, false, null, 12, null);
        final String licenseContent = ExtKt.jsonEncode$default(license, false, 2, null);
        if (this.privateKeyContent.length() == 0) {
            final String storage = ExtKt.getStorage(new String[] { "data", "privateKey" }, ".key");
            this.privateKeyContent = ((storage == null) ? "" : storage);
        }
        final PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
        final EncoderUtils instance = EncoderUtils.INSTANCE;
        final String s = licenseContent;
        Intrinsics.checkNotNullExpressionValue((Object)privateKey, "privateKey");
        final String licenseKey = EncoderUtils.encryptSegmentByPrivateKey$default(instance, s, privateKey, 0, 4, null);
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(TuplesKt.to((Object)"result", (Object)licenseKey)), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object isLicenseValid(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        String id = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("id");
            Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"id\")");
            id = string;
        }
        else {
            final List queryParam = context.queryParam("id");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"id\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            id = ((s == null) ? "" : s);
        }
        JsonArray activeLicenseList = ExtKt.asJsonArray(ExtKt.getStorage$default(new String[] { "data", "activeLicense" }, null, 2, null));
        if (activeLicenseList == null) {
            activeLicenseList = new JsonArray();
        }
        ActiveLicense activeLicense = null;
        int index = -1;
        int j = 0;
        final int size = activeLicenseList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final ActiveLicense _activeLicense = (ActiveLicense)activeLicenseList.getJsonObject(i).mapTo((Class)ActiveLicense.class);
                if (_activeLicense.getId().equals(id)) {
                    activeLicense = _activeLicense;
                    index = i;
                    break;
                }
            } while (j < size);
        }
        final Map result = new LinkedHashMap();
        String ip = context.request().getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0) {
            final SocketAddress remoteAddress = context.request().remoteAddress();
            String s2;
            if (remoteAddress == null) {
                s2 = "";
            }
            else {
                final String host = remoteAddress.host();
                s2 = ((host == null) ? "" : host);
            }
            ip = s2;
        }
        if (activeLicense == null) {
            result.put("isValid", Boxing.boxBoolean(false));
            result.put("errorMsg", "\u5bc6\u94a5\u672a\u6fc0\u6d3b");
        }
        else {
            result.put("isValid", Boxing.boxBoolean(activeLicense.getVerified()));
            result.put("errorMsg", activeLicense.getErrorMsg());
            if (activeLicense.getLastOnlineTime() != null) {
                final long currentTimeMillis = System.currentTimeMillis();
                final Long lastOnlineTime = activeLicense.getLastOnlineTime();
                Intrinsics.checkNotNull((Object)lastOnlineTime);
                if (currentTimeMillis < lastOnlineTime + 600000 && !ip.equals(activeLicense.getLastOnlineIp())) {
                    result.put("repeat", MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"lastOnlineTime", (Object)activeLicense.getLastOnlineTime()), TuplesKt.to((Object)"lastOnlineIp", (Object)activeLicense.getLastOnlineIp()) }));
                }
            }
            activeLicense.setLastOnlineTime(Boxing.boxLong(System.currentTimeMillis()));
            activeLicense.setLastOnlineIp(ip);
            activeLicenseList.set(index, JsonObject.mapFrom((Object)activeLicense));
            ExtKt.saveStorage$default(new String[] { "data", "activeLicense" }, activeLicenseList, false, null, 12, null);
        }
        final String resultContent = ExtKt.jsonEncode$default(result, false, 2, null);
        if (this.privateKeyContent.length() == 0) {
            final String storage = ExtKt.getStorage(new String[] { "data", "privateKey" }, ".key");
            this.privateKeyContent = ((storage == null) ? "" : storage);
        }
        final PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
        final EncoderUtils instance = EncoderUtils.INSTANCE;
        final String s3 = resultContent;
        Intrinsics.checkNotNullExpressionValue((Object)privateKey, "privateKey");
        final String resultKey = EncoderUtils.encryptSegmentByPrivateKey$default(instance, s3, privateKey, 0, 4, null);
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(TuplesKt.to((Object)"result", (Object)resultKey)), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object checkLicense(@NotNull final License license, @NotNull final Continuation<? super Unit> $completion) {
        final int $i$f$CoroutineExceptionHandler = 0;
        final CoroutineExceptionHandler exceptionHandler = (CoroutineExceptionHandler)new CoroutineExceptionHandler(CoroutineExceptionHandler.Key) {
            public void handleException(@NotNull final CoroutineContext context, @NotNull final Throwable exception) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_2         /* exception */
                //     2: astore_3       
                //     3: astore          ctx
                //     5: iconst_0       
                //     6: istore          $i$a$-CoroutineExceptionHandler-LicenseController$checkLicense$exceptionHandler$1
                //     8: invokestatic    com/htmake/reader/api/controller/LicenseControllerKt.access$getLogger$p:()Lmu/KLogger;
                //    11: ldc             "check license error: {}"
                //    13: aload_3         /* ex */
                //    14: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
                //    17: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
                //    22: nop            
                //    23: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        };
        final Ref$ObjectRef checkUrl = new Ref$ObjectRef();
        checkUrl.element = Intrinsics.stringPlus("https://r.htmake.com/reader3/isLicenseValid?id=", (Object)license.getId());
        final Job launch$default = BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()).plus((CoroutineContext)exceptionHandler), (CoroutineStart)null, (Function2)new LicenseController$checkLicense.LicenseController$checkLicense$2(this, checkUrl, (Continuation)null), 2, (Object)null);
        if (launch$default == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return launch$default;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object sendCodeToEmail(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        String email = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("email");
            email = ((string == null) ? "" : string);
        }
        else {
            final List queryParam = context.queryParam("email");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"email\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            email = ((s == null) ? "" : s);
        }
        if (email.length() == 0) {
            return returnData.setErrorMsg("\u90ae\u7bb1\u9519\u8bef");
        }
        if (!ExtKt.validateEmail(email)) {
            return returnData.setErrorMsg("\u4ec5\u652f\u6301 163|126|qq|yahoo|sina|sohu|yeah|139|189|21cn|outlook|gmail|icloud \u7b49\u90ae\u7bb1");
        }
        JsonArray activeLicenseList = ExtKt.asJsonArray(ExtKt.getStorage$default(new String[] { "data", "activeLicense" }, null, 2, null));
        if (activeLicenseList == null) {
            activeLicenseList = new JsonArray();
        }
        int j = 0;
        final int size = activeLicenseList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                if ("trial".equals(activeLicenseList.getJsonObject(i).getString("type")) && email.equals(activeLicenseList.getJsonObject(i).getString("code"))) {
                    return returnData.setErrorMsg("\u8be5\u90ae\u7bb1\u5df2\u88ab\u4f7f\u7528");
                }
            } while (j < size);
        }
        final String verifyCode = this.tryCodeCache.getAsString(email);
        final CharSequence charSequence = verifyCode;
        if (charSequence != null && charSequence.length() != 0) {
            return returnData.setData((Object)"", "\u60a8\u7684\u9a8c\u8bc1\u7801\u4ecd\u5728\u6709\u6548\u671f\u5185\uff0c\u8bf7\u52ff\u91cd\u590d\u83b7\u53d6");
        }
        final String string2 = UUID.randomUUID().toString();
        Intrinsics.checkNotNullExpressionValue((Object)string2, "randomUUID().toString()");
        final String substring = string2.substring(0, 6);
        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String code = substring;
        this.tryCodeCache.put(email, code, 900);
        ExtKt.sendEmail(email, "Reader Kindle\u7aef\u7684\u8bd5\u7528\u7533\u8bf7\u9a8c\u8bc1", "\u60a8\u6b63\u5728\u7533\u8bf7Reader Kindle\u7aef\u7684\u8bd5\u7528\uff0c\u9a8c\u8bc1\u7801\u662f: " + code + "\uff0c15\u5206\u949f\u5185\u6709\u6548\uff0c\u8bf7\u52ff\u56de\u590d");
        return returnData.setData((Object)"", "\u8bf7\u67e5\u6536\u90ae\u4ef6");
    }
    
    @Nullable
    public final Object supplyLicense(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final ReturnData returnData = new ReturnData();
        final String string = context.getBodyAsJson().getString("email");
        final String email = (string == null) ? "" : string;
        final String string2 = context.getBodyAsJson().getString("code");
        final String code = (string2 == null) ? "" : string2;
        if (email.length() == 0 || code.length() == 0) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final String verifyCode = this.tryCodeCache.getAsString(email);
        this.tryCodeCache.remove(email);
        if (!code.equals(verifyCode)) {
            return returnData.setErrorMsg("\u9a8c\u8bc1\u7801\u9519\u8bef");
        }
        final License license = new License("*", 15, 0L, false, System.currentTimeMillis() + 604800000L, 1, "trial", (String)null, email, false, (Long)null, 1152, (DefaultConstructorMarker)null);
        final String licenseContent = ExtKt.jsonEncode$default(license, false, 2, null);
        if (this.privateKeyContent.length() == 0) {
            final String storage = ExtKt.getStorage(new String[] { "data", "privateKey" }, ".key");
            this.privateKeyContent = ((storage == null) ? "" : storage);
        }
        final PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(this.privateKeyContent, 2)));
        final EncoderUtils instance = EncoderUtils.INSTANCE;
        final String s = licenseContent;
        Intrinsics.checkNotNullExpressionValue((Object)privateKey, "privateKey");
        final String licenseKey = EncoderUtils.encryptSegmentByPrivateKey$default(instance, s, privateKey, 0, 4, null);
        return ReturnData.setData$default(returnData, (Object)MapsKt.mapOf(TuplesKt.to((Object)"key", (Object)licenseKey)), (String)null, 2, (Object)null);
    }
}
