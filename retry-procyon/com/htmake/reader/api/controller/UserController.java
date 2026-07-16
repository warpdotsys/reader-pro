// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import com.google.gson.reflect.TypeToken;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$ObjectRef;
import io.vertx.core.http.HttpServerResponse;
import java.net.URLEncoder;
import com.htmake.reader.utils.VertExtKt;
import io.vertx.core.http.HttpMethod;
import java.util.Set;
import kotlin.jvm.functions.Function2;
import kotlin.io.FilesKt;
import io.vertx.ext.web.FileUpload;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import kotlin.Pair;
import kotlinx.coroutines.CoroutineScope;
import kotlin.jvm.functions.Function3;
import kotlin.Unit;
import kotlin.coroutines.jvm.internal.Boxing;
import io.vertx.core.json.JsonArray;
import java.io.File;
import kotlin.jvm.internal.DefaultConstructorMarker;
import com.htmake.reader.entity.User;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import java.util.Iterator;
import java.util.ArrayList;
import io.vertx.core.json.JsonObject;
import kotlinx.coroutines.sync.Mutex;
import java.util.List;
import kotlin.text.StringsKt;
import kotlin.collections.CollectionsKt;
import io.vertx.core.json.Json;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.sync.Mutex$DefaultImpls;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import org.jetbrains.annotations.Nullable;
import com.htmake.reader.api.ReturnData;
import kotlin.coroutines.Continuation;
import com.htmake.reader.entity.License;
import com.htmake.reader.utils.ExtKt;
import io.vertx.ext.web.RoutingContext;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0019\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0006H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010\u0013\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJO\u0010\u0015\u001a\u00020\u000f2<\u0010\u0016\u001a8\b\u0001\u0012\u0004\u0012\u00020\u0018\u0012\u0013\u0012\u00110\u0019?\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001c\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u0017?\u0006\u0002\b H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010!J\u0019\u0010\"\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010#\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0010\u0010$\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0019\u0010%\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010&\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010'\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010(\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010)\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010*\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rJ\u0019\u0010+\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\rR\u0014\u0010\u0005\u001a\u00020\u0006X\u0086D?\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006," }, d2 = { "Lcom/htmake/reader/api/controller/UserController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "userMaxCount", "", "getUserMaxCount", "()I", "addUser", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearInactiveUsers", "", "day", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteFile", "deleteUsers", "downloadBackupFile", "forEachUser", "handler", "Lkotlin/Function3;", "Lkotlinx/coroutines/CoroutineScope;", "Lcom/htmake/reader/entity/User;", "Lkotlin/ParameterName;", "name", "user", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserConfig", "getUserInfo", "getUserLimit", "getUserList", "login", "logout", "resetPassword", "saveUserConfig", "updateUser", "uploadFile", "reader-pro" })
public final class UserController extends BaseController
{
    private final int userMaxCount;
    
    public UserController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
        this.userMaxCount = 15;
    }
    
    public final int getUserMaxCount() {
        return this.userMaxCount;
    }
    
    private final int getUserLimit(final RoutingContext context) {
        final License installedLicense$default;
        final License license = installedLicense$default = ExtKt.getInstalledLicense$default(false, 1, null);
        final String host = context.request().host();
        Intrinsics.checkNotNullExpressionValue((Object)host, "context.request().host()");
        if (installedLicense$default.validHost(host)) {
            return Math.min(Math.max(this.getAppConfig().getUserLimit(), 1), license.getUserMaxLimit());
        }
        return Math.min(Math.max(this.getAppConfig().getUserLimit(), 1), this.userMaxCount);
    }
    
    @Nullable
    public final Object login(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/api/controller/UserController$login$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/api/controller/UserController$login$1;
        //    11: astore          17
        //    13: aload           17
        //    15: getfield        com/htmake/reader/api/controller/UserController$login$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           17
        //    26: dup            
        //    27: getfield        com/htmake/reader/api/controller/UserController$login$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/api/controller/UserController$login$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/api/controller/UserController$login$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/api/controller/UserController$login$1.<init>:(Lcom/htmake/reader/api/controller/UserController;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/api/controller/UserController$login$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          18
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/api/controller/UserController$login$1.label:I
        //    67: tableswitch {
        //                0: 92
        //                1: 836
        //                2: 1026
        //          default: 1057
        //        }
        //    92: aload           $result
        //    94: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    97: new             Lcom/htmake/reader/api/ReturnData;
        //   100: dup            
        //   101: invokespecial   com/htmake/reader/api/ReturnData.<init>:()V
        //   104: astore_3        /* returnData */
        //   105: aload_1         /* context */
        //   106: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   111: ldc             "username"
        //   113: ldc             ""
        //   115: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   118: astore          5
        //   120: aload           5
        //   122: ifnonnull       130
        //   125: ldc             ""
        //   127: goto            132
        //   130: aload           5
        //   132: astore          username
        //   134: aload_1         /* context */
        //   135: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   140: ldc             "password"
        //   142: ldc             ""
        //   144: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   147: astore          6
        //   149: aload           6
        //   151: ifnonnull       159
        //   154: ldc             ""
        //   156: goto            161
        //   159: aload           6
        //   161: astore          password
        //   163: aload_1         /* context */
        //   164: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   169: ldc             "isLogin"
        //   171: iconst_0       
        //   172: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxBoolean:(Z)Ljava/lang/Boolean;
        //   175: invokevirtual   io/vertx/core/json/JsonObject.getBoolean:(Ljava/lang/String;Ljava/lang/Boolean;)Ljava/lang/Boolean;
        //   178: astore          7
        //   180: aload           7
        //   182: ifnonnull       189
        //   185: iconst_0       
        //   186: goto            194
        //   189: aload           7
        //   191: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   194: istore          isLogin
        //   196: aload           username
        //   198: checkcast       Ljava/lang/CharSequence;
        //   201: astore          7
        //   203: iconst_0       
        //   204: istore          8
        //   206: iconst_0       
        //   207: istore          9
        //   209: aload           7
        //   211: invokeinterface java/lang/CharSequence.length:()I
        //   216: ifne            223
        //   219: iconst_1       
        //   220: goto            224
        //   223: iconst_0       
        //   224: ifeq            234
        //   227: aload_3         /* returnData */
        //   228: ldc             "\u8bf7\u8f93\u5165\u7528\u6237\u540d"
        //   230: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   233: areturn        
        //   234: aload           password
        //   236: checkcast       Ljava/lang/CharSequence;
        //   239: astore          7
        //   241: iconst_0       
        //   242: istore          8
        //   244: iconst_0       
        //   245: istore          9
        //   247: aload           7
        //   249: invokeinterface java/lang/CharSequence.length:()I
        //   254: ifne            261
        //   257: iconst_1       
        //   258: goto            262
        //   261: iconst_0       
        //   262: ifeq            272
        //   265: aload_3         /* returnData */
        //   266: ldc             "\u8bf7\u8f93\u5165\u5bc6\u7801"
        //   268: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   271: areturn        
        //   272: iconst_0       
        //   273: istore          8
        //   275: new             Ljava/util/LinkedHashMap;
        //   278: dup            
        //   279: invokespecial   java/util/LinkedHashMap.<init>:()V
        //   282: checkcast       Ljava/util/Map;
        //   285: astore          userMap
        //   287: iconst_2       
        //   288: anewarray       Ljava/lang/String;
        //   291: astore          9
        //   293: aload           9
        //   295: iconst_0       
        //   296: ldc             "data"
        //   298: aastore        
        //   299: aload           9
        //   301: iconst_1       
        //   302: ldc             "users"
        //   304: aastore        
        //   305: aload           9
        //   307: aconst_null    
        //   308: iconst_2       
        //   309: aconst_null    
        //   310: invokestatic    com/htmake/reader/utils/ExtKt.getStorage$default:([Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
        //   313: invokestatic    com/htmake/reader/utils/ExtKt.asJsonObject:(Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;
        //   316: astore          userMapJson
        //   318: aload           userMapJson
        //   320: ifnull          352
        //   323: aload           userMapJson
        //   325: invokevirtual   io/vertx/core/json/JsonObject.getMap:()Ljava/util/Map;
        //   328: astore          9
        //   330: aload           9
        //   332: ifnonnull       345
        //   335: new             Ljava/lang/NullPointerException;
        //   338: dup            
        //   339: ldc             "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
        //   341: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   344: athrow         
        //   345: aload           9
        //   347: invokestatic    kotlin/jvm/internal/TypeIntrinsics.asMutableMap:(Ljava/lang/Object;)Ljava/util/Map;
        //   350: astore          userMap
        //   352: aload           userMap
        //   354: astore          10
        //   356: aconst_null    
        //   357: astore          11
        //   359: iconst_0       
        //   360: istore          12
        //   362: aload           10
        //   364: dup            
        //   365: ifnonnull       378
        //   368: new             Ljava/lang/NullPointerException;
        //   371: dup            
        //   372: ldc             "null cannot be cast to non-null type kotlin.collections.Map<K, V>"
        //   374: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   377: athrow         
        //   378: aload           username
        //   380: aload           11
        //   382: invokeinterface java/util/Map.getOrDefault:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   387: checkcast       Ljava/util/Map;
        //   390: astore          existedUser
        //   392: aload           existedUser
        //   394: ifnonnull       867
        //   397: iload           isLogin
        //   399: ifeq            409
        //   402: aload_3         /* returnData */
        //   403: ldc             "\u7528\u6237\u4e0d\u5b58\u5728"
        //   405: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   408: areturn        
        //   409: aload           username
        //   411: invokevirtual   java/lang/String.length:()I
        //   414: iconst_5       
        //   415: if_icmpge       425
        //   418: aload_3         /* returnData */
        //   419: ldc             "\u7528\u6237\u540d\u4e0d\u80fd\u4f4e\u4e8e5\u4f4d"
        //   421: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   424: areturn        
        //   425: aload           password
        //   427: invokevirtual   java/lang/String.length:()I
        //   430: aload_0         /* this */
        //   431: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   434: invokevirtual   com/htmake/reader/config/AppConfig.getMinUserPasswordLength:()I
        //   437: if_icmpge       476
        //   440: aload_3         /* returnData */
        //   441: new             Ljava/lang/StringBuilder;
        //   444: dup            
        //   445: invokespecial   java/lang/StringBuilder.<init>:()V
        //   448: ldc             "\u5bc6\u7801\u4e0d\u80fd\u4f4e\u4e8e"
        //   450: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   453: aload_0         /* this */
        //   454: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   457: invokevirtual   com/htmake/reader/config/AppConfig.getMinUserPasswordLength:()I
        //   460: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   463: sipush          20301
        //   466: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   469: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   472: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   475: areturn        
        //   476: aload           username
        //   478: ldc             "default"
        //   480: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   483: ifeq            493
        //   486: aload_3         /* returnData */
        //   487: ldc             "\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u975e\u6cd5\u5b57\u7b26"
        //   489: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   492: areturn        
        //   493: new             Lkotlin/text/Regex;
        //   496: dup            
        //   497: ldc             "[a-z0-9]+"
        //   499: getstatic       kotlin/text/RegexOption.IGNORE_CASE:Lkotlin/text/RegexOption;
        //   502: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;Lkotlin/text/RegexOption;)V
        //   505: astore          usernameReg
        //   507: aload           usernameReg
        //   509: aload           username
        //   511: checkcast       Ljava/lang/CharSequence;
        //   514: invokevirtual   kotlin/text/Regex.matches:(Ljava/lang/CharSequence;)Z
        //   517: ifne            528
        //   520: aload_3         /* returnData */
        //   521: ldc_w           "\u7528\u6237\u540d\u53ea\u80fd\u7531\u5b57\u6bcd\u548c\u6570\u5b57\u7ec4\u6210"
        //   524: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   527: areturn        
        //   528: aload_0         /* this */
        //   529: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   532: invokevirtual   com/htmake/reader/config/AppConfig.getInviteCode:()Ljava/lang/String;
        //   535: checkcast       Ljava/lang/CharSequence;
        //   538: astore          11
        //   540: iconst_0       
        //   541: istore          12
        //   543: aload           11
        //   545: invokeinterface java/lang/CharSequence.length:()I
        //   550: ifle            557
        //   553: iconst_1       
        //   554: goto            558
        //   557: iconst_0       
        //   558: ifeq            651
        //   561: aload_1         /* context */
        //   562: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   567: ldc_w           "code"
        //   570: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   573: astore          12
        //   575: aload           12
        //   577: ifnonnull       585
        //   580: ldc             ""
        //   582: goto            587
        //   585: aload           12
        //   587: astore          code
        //   589: aload           code
        //   591: checkcast       Ljava/lang/CharSequence;
        //   594: astore          12
        //   596: iconst_0       
        //   597: istore          13
        //   599: iconst_0       
        //   600: istore          14
        //   602: aload           12
        //   604: invokeinterface java/lang/CharSequence.length:()I
        //   609: ifne            616
        //   612: iconst_1       
        //   613: goto            617
        //   616: iconst_0       
        //   617: ifeq            628
        //   620: aload_3         /* returnData */
        //   621: ldc_w           "\u8bf7\u8f93\u5165\u9080\u8bf7\u7801"
        //   624: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   627: areturn        
        //   628: aload_0         /* this */
        //   629: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   632: invokevirtual   com/htmake/reader/config/AppConfig.getInviteCode:()Ljava/lang/String;
        //   635: aload           code
        //   637: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   640: ifne            651
        //   643: aload_3         /* returnData */
        //   644: ldc_w           "\u9080\u8bf7\u7801\u9519\u8bef"
        //   647: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   650: areturn        
        //   651: aload_0         /* this */
        //   652: aload_1         /* context */
        //   653: invokespecial   com/htmake/reader/api/controller/UserController.getUserLimit:(Lio/vertx/ext/web/RoutingContext;)I
        //   656: istore          userLimit
        //   658: aload           userMap
        //   660: invokeinterface java/util/Map.keySet:()Ljava/util/Set;
        //   665: invokeinterface java/util/Set.size:()I
        //   670: iload           userLimit
        //   672: if_icmplt       683
        //   675: aload_3         /* returnData */
        //   676: ldc_w           "\u8d85\u8fc7\u7528\u6237\u6570\u4e0a\u9650"
        //   679: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   682: areturn        
        //   683: bipush          8
        //   685: invokestatic    com/htmake/reader/utils/ExtKt.getRandomString:(I)Ljava/lang/String;
        //   688: astore          salt
        //   690: aload           password
        //   692: aload           salt
        //   694: invokestatic    com/htmake/reader/utils/ExtKt.genEncryptedPassword:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   697: astore          passwordEncrypted
        //   699: new             Lcom/htmake/reader/entity/User;
        //   702: dup            
        //   703: aload           username
        //   705: aload           passwordEncrypted
        //   707: aload           salt
        //   709: aconst_null    
        //   710: lconst_0       
        //   711: lconst_0       
        //   712: iconst_0       
        //   713: aconst_null    
        //   714: iconst_0       
        //   715: iconst_0       
        //   716: iconst_0       
        //   717: iconst_0       
        //   718: iconst_0       
        //   719: sipush          8184
        //   722: aconst_null    
        //   723: invokespecial   com/htmake/reader/entity/User.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZLjava/util/Map;ZZZIIILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   726: astore          newUser
        //   728: aload           newUser
        //   730: aload_0         /* this */
        //   731: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   734: invokevirtual   com/htmake/reader/config/AppConfig.getDefaultUserEnableWebdav:()Z
        //   737: invokevirtual   com/htmake/reader/entity/User.setEnable_webdav:(Z)V
        //   740: aload           newUser
        //   742: aload_0         /* this */
        //   743: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   746: invokevirtual   com/htmake/reader/config/AppConfig.getDefaultUserEnableLocalStore:()Z
        //   749: invokevirtual   com/htmake/reader/entity/User.setEnable_local_store:(Z)V
        //   752: aload           newUser
        //   754: aload_0         /* this */
        //   755: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   758: invokevirtual   com/htmake/reader/config/AppConfig.getDefaultUserEnableBookSource:()Z
        //   761: invokevirtual   com/htmake/reader/entity/User.setEnable_book_source:(Z)V
        //   764: aload           newUser
        //   766: aload_0         /* this */
        //   767: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   770: invokevirtual   com/htmake/reader/config/AppConfig.getDefaultUserEnableRssSource:()Z
        //   773: invokevirtual   com/htmake/reader/entity/User.setEnable_rss_source:(Z)V
        //   776: aload           newUser
        //   778: aload_0         /* this */
        //   779: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   782: invokevirtual   com/htmake/reader/config/AppConfig.getDefaultUserBookSourceLimit:()I
        //   785: invokevirtual   com/htmake/reader/entity/User.setBook_source_limit:(I)V
        //   788: aload           newUser
        //   790: aload_0         /* this */
        //   791: invokevirtual   com/htmake/reader/api/controller/UserController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   794: invokevirtual   com/htmake/reader/config/AppConfig.getDefaultUserBookLimit:()I
        //   797: invokevirtual   com/htmake/reader/entity/User.setBook_limit:(I)V
        //   800: aload_0         /* this */
        //   801: checkcast       Lcom/htmake/reader/api/controller/BaseController;
        //   804: aload_1         /* context */
        //   805: aload           newUser
        //   807: iconst_0       
        //   808: aload           $continuation
        //   810: iconst_4       
        //   811: aconst_null    
        //   812: aload           $continuation
        //   814: aload_3         /* returnData */
        //   815: putfield        com/htmake/reader/api/controller/UserController$login$1.L$0:Ljava/lang/Object;
        //   818: aload           $continuation
        //   820: iconst_1       
        //   821: putfield        com/htmake/reader/api/controller/UserController$login$1.label:I
        //   824: invokestatic    com/htmake/reader/api/controller/BaseController.saveUserSession$default:(Lcom/htmake/reader/api/controller/BaseController;Lio/vertx/ext/web/RoutingContext;Lcom/htmake/reader/entity/User;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   827: dup            
        //   828: aload           18
        //   830: if_acmpne       852
        //   833: aload           18
        //   835: areturn        
        //   836: aload           $continuation
        //   838: getfield        com/htmake/reader/api/controller/UserController$login$1.L$0:Ljava/lang/Object;
        //   841: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   844: astore_3       
        //   845: aload           $result
        //   847: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   850: aload           $result
        //   852: checkcast       Ljava/util/Map;
        //   855: astore          loginData
        //   857: aload_3        
        //   858: aload           loginData
        //   860: aconst_null    
        //   861: iconst_2       
        //   862: aconst_null    
        //   863: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   866: areturn        
        //   867: iload           6
        //   869: ifne            880
        //   872: aload_3        
        //   873: ldc_w           "\u7528\u6237\u540d\u5df2\u88ab\u5360\u7528"
        //   876: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   879: areturn        
        //   880: aload           existedUser
        //   882: astore          $this$toDataClass$iv
        //   884: iconst_0       
        //   885: istore          $i$f$toDataClass
        //   887: aload           $this$toDataClass$iv
        //   889: astore          $this$convert$iv$iv
        //   891: iconst_0       
        //   892: istore          $i$f$convert
        //   894: aload           $this$convert$iv$iv
        //   896: instanceof      Ljava/lang/String;
        //   899: ifeq            910
        //   902: aload           $this$convert$iv$iv
        //   904: checkcast       Ljava/lang/String;
        //   907: goto            918
        //   910: invokestatic    com/htmake/reader/utils/ExtKt.getGson:()Lcom/google/gson/Gson;
        //   913: aload           $this$convert$iv$iv
        //   915: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //   918: astore          json$iv$iv
        //   920: invokestatic    com/htmake/reader/utils/ExtKt.getGson:()Lcom/google/gson/Gson;
        //   923: aload           json$iv$iv
        //   925: new             Lcom/htmake/reader/api/controller/UserController$login$$inlined$toDataClass$1;
        //   928: dup            
        //   929: invokespecial   com/htmake/reader/api/controller/UserController$login$$inlined$toDataClass$1.<init>:()V
        //   932: invokevirtual   com/htmake/reader/api/controller/UserController$login$$inlined$toDataClass$1.getType:()Ljava/lang/reflect/Type;
        //   935: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   938: nop            
        //   939: checkcast       Lcom/htmake/reader/entity/User;
        //   942: astore          userInfo
        //   944: aload           userInfo
        //   946: ifnonnull       957
        //   949: aload_3        
        //   950: ldc_w           "\u7528\u6237\u4fe1\u606f\u9519\u8bef"
        //   953: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   956: areturn        
        //   957: aload           5
        //   959: aload           userInfo
        //   961: invokevirtual   com/htmake/reader/entity/User.getSalt:()Ljava/lang/String;
        //   964: invokestatic    com/htmake/reader/utils/ExtKt.genEncryptedPassword:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   967: astore          passwordEncrypted
        //   969: aload           passwordEncrypted
        //   971: aload           userInfo
        //   973: invokevirtual   com/htmake/reader/entity/User.getPassword:()Ljava/lang/String;
        //   976: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   979: ifne            990
        //   982: aload_3        
        //   983: ldc_w           "\u5bc6\u7801\u9519\u8bef"
        //   986: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   989: areturn        
        //   990: aload_0        
        //   991: checkcast       Lcom/htmake/reader/api/controller/BaseController;
        //   994: aload_1        
        //   995: aload           userInfo
        //   997: iconst_0       
        //   998: aload           $continuation
        //  1000: iconst_4       
        //  1001: aconst_null    
        //  1002: aload           $continuation
        //  1004: aload_3        
        //  1005: putfield        com/htmake/reader/api/controller/UserController$login$1.L$0:Ljava/lang/Object;
        //  1008: aload           $continuation
        //  1010: iconst_2       
        //  1011: putfield        com/htmake/reader/api/controller/UserController$login$1.label:I
        //  1014: invokestatic    com/htmake/reader/api/controller/BaseController.saveUserSession$default:(Lcom/htmake/reader/api/controller/BaseController;Lio/vertx/ext/web/RoutingContext;Lcom/htmake/reader/entity/User;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //  1017: dup            
        //  1018: aload           18
        //  1020: if_acmpne       1042
        //  1023: aload           18
        //  1025: areturn        
        //  1026: aload           $continuation
        //  1028: getfield        com/htmake/reader/api/controller/UserController$login$1.L$0:Ljava/lang/Object;
        //  1031: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1034: astore_3       
        //  1035: aload           $result
        //  1037: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1040: aload           $result
        //  1042: checkcast       Ljava/util/Map;
        //  1045: astore          loginData
        //  1047: aload_3        
        //  1048: aload           loginData
        //  1050: aconst_null    
        //  1051: iconst_2       
        //  1052: aconst_null    
        //  1053: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //  1056: areturn        
        //  1057: new             Ljava/lang/IllegalStateException;
        //  1060: dup            
        //  1061: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1064: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1067: athrow         
        //    Signature:
        //  (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation<-Lcom/htmake/reader/api/ReturnData;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  context      
        //  $completion  
        //    StackMapTable: 00 2B 27 FF 00 0A 00 12 07 00 02 07 00 25 07 01 9E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 5A 00 00 FF 00 29 00 13 07 00 02 07 00 25 07 01 9E 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 25 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 00 07 00 53 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 41 07 00 53 FF 00 1A 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 07 00 53 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 41 07 00 53 FF 00 1B 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 07 00 53 07 00 94 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 44 01 FF 00 1C 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 9A 01 01 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 40 01 09 1A 40 01 09 FF 00 48 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 06 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 01 A0 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 19 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 01 A0 07 00 AA 05 01 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 01 07 00 AA FF 00 1E 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 AA 05 01 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 0F 32 10 FF 00 22 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 F1 05 01 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 1C 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 F1 07 00 9A 01 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 40 01 FF 00 1A 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 F1 07 00 9A 07 00 53 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 41 07 00 53 FF 00 1C 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 F1 07 00 53 07 00 9A 01 01 00 07 01 A0 07 00 5A 07 01 A0 00 00 40 01 0A FF 00 16 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 F1 07 01 A0 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 1F 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 F1 01 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 98 00 13 07 00 02 07 00 25 07 01 9E 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 0F 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 00 00 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 01 07 01 A0 FF 00 0E 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 AA 05 01 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 0C FF 00 1D 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 00 AA 07 00 AA 01 07 00 AA 01 00 07 01 A0 07 00 5A 07 01 A0 00 00 47 07 00 53 FF 00 26 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 01 24 07 00 AA 01 07 00 AA 01 07 00 53 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 20 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 07 00 53 07 00 53 01 07 00 AA 07 00 80 07 00 AA 07 01 24 07 00 53 01 07 00 AA 01 07 00 53 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 23 00 13 07 00 02 07 00 25 07 01 9E 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00 FF 00 0F 00 13 07 00 02 07 00 25 07 01 9E 07 00 73 00 00 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 01 07 01 A0 FF 00 0E 00 13 07 00 02 07 00 25 07 01 9E 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 A0 07 00 5A 07 01 A0 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public final Object logout(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$logout.UserController$logout$1) {
                final UserController$logout.UserController$logout$1 userController$logout$1 = (UserController$logout.UserController$logout$1)$completion;
                if ((userController$logout$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$logout.UserController$logout$1 userController$logout$2 = userController$logout$1;
                    userController$logout$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$logout.UserController$logout$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$logout.UserController$logout$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData2 = null;
        while (true) {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((UserController$logout.UserController$logout$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final UserController userController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((UserController$logout.UserController$logout$1)$continuation).L$0 = this;
                    ((UserController$logout.UserController$logout$1)$continuation).L$1 = context;
                    ((UserController$logout.UserController$logout$1)$continuation).L$2 = returnData;
                    ((UserController$logout.UserController$logout$1)$continuation).label = 1;
                    if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((UserController$logout.UserController$logout$1)$continuation).L$2;
                    context = (RoutingContext)((UserController$logout.UserController$logout$1)$continuation).L$1;
                    this = (UserController)((UserController$logout.UserController$logout$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    Label_0466: {
                        break Label_0466;
                        final List tmp;
                        final String accessToken = tmp.get(1);
                        Map userMap = new LinkedHashMap();
                        try {
                            final Mutex userMutex = this.getUserMutex();
                            final Object o = null;
                            final Continuation continuation = $continuation;
                            final int n = 1;
                            final Object o2 = null;
                            ((UserController$logout.UserController$logout$1)$continuation).L$0 = this;
                            ((UserController$logout.UserController$logout$1)$continuation).L$1 = returnData;
                            final String username;
                            ((UserController$logout.UserController$logout$1)$continuation).L$2 = username;
                            ((UserController$logout.UserController$logout$1)$continuation).L$3 = accessToken;
                            ((UserController$logout.UserController$logout$1)$continuation).L$4 = userMap;
                            ((UserController$logout.UserController$logout$1)$continuation).label = 2;
                            if (Mutex$DefaultImpls.lock$default(userMutex, o, continuation, n, o2) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
                                if (userMapJson != null) {
                                    final Map map = userMapJson.getMap();
                                    if (map == null) {
                                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
                                    }
                                    userMap = TypeIntrinsics.asMutableMap((Object)map);
                                }
                                final Map map2 = userMap;
                                final Map defaultValue = null;
                                final Map map3 = map2;
                                if (map3 == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
                                }
                                final String key;
                                final Map currentUser = map3.getOrDefault(key, defaultValue);
                                if (currentUser == null) {
                                    return returnData2.setErrorMsg("\u7cfb\u7edf\u9519\u8bef");
                                }
                                final Object tokenMapVal = currentUser.getOrDefault("token_map", null);
                                final String obj;
                                if (tokenMapVal != null) {
                                    final Map tokenMap = TypeIntrinsics.asMutableMap(tokenMapVal);
                                    if (tokenMap != null) {
                                        tokenMap.remove(obj);
                                        currentUser.put("token_map", tokenMap);
                                    }
                                }
                                if (currentUser.getOrDefault("token", "").equals(obj)) {
                                    currentUser.put("token", "");
                                }
                                userMap.put(key, currentUser);
                                final String[] array = { "data", "users" };
                                final String encode = Json.encode((Object)userMap);
                                Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap)");
                                ExtKt.saveStorage$default(array, encode, false, null, 12, null);
                                return ReturnData.setData$default(returnData2.setErrorMsg("\u8bf7\u91cd\u65b0\u767b\u5f55"), (Object)"NEED_LOGIN", (String)null, 2, (Object)null);
                                userMap = (Map)((UserController$logout.UserController$logout$1)$continuation).L$4;
                                obj = (String)((UserController$logout.UserController$logout$1)$continuation).L$3;
                                key = (String)((UserController$logout.UserController$logout$1)$continuation).L$2;
                                returnData2 = (ReturnData)((UserController$logout.UserController$logout$1)$continuation).L$1;
                                this = (UserController)((UserController$logout.UserController$logout$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                continue;
                            }
                        }
                        finally {
                            Mutex$DefaultImpls.unlock$default(this.getUserMutex(), (Object)null, 1, (Object)null);
                        }
                    }
                    return ReturnData.setData$default(returnData2.setErrorMsg("\u8bf7\u91cd\u65b0\u767b\u5f55"), (Object)"NEED_LOGIN", (String)null, 2, (Object)null);
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            if (!this.getAppConfig().getSecure()) {
                return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
            }
            final String s = (String)context.session().get("username");
            final String username = (s == null) ? "" : s;
            context.session().destroy();
            final List queryParam = context.queryParam("accessToken");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"accessToken\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            final String accessToken = (s2 == null) ? "" : s2;
            if (accessToken.length() > 0) {
                final List tmp = StringsKt.split$default((CharSequence)accessToken, new String[] { ":" }, false, 2, 2, (Object)null);
                if (tmp.size() >= 2) {
                    continue;
                }
            }
            break;
        }
        return ReturnData.setData$default(returnData2.setErrorMsg("\u8bf7\u91cd\u65b0\u767b\u5f55"), (Object)"NEED_LOGIN", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object getUserList(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$getUserList.UserController$getUserList$1) {
                final UserController$getUserList.UserController$getUserList$1 userController$getUserList$1 = (UserController$getUserList.UserController$getUserList$1)$completion;
                if ((userController$getUserList$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$getUserList.UserController$getUserList$1 userController$getUserList$2 = userController$getUserList$1;
                    userController$getUserList$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$getUserList.UserController$getUserList$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$getUserList.UserController$getUserList$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$getUserList.UserController$getUserList$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$getUserList.UserController$getUserList$1)$continuation).L$0 = this;
                ((UserController$getUserList.UserController$getUserList$1)$continuation).L$1 = context;
                ((UserController$getUserList.UserController$getUserList$1)$continuation).L$2 = returnData;
                ((UserController$getUserList.UserController$getUserList$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$getUserList.UserController$getUserList$1)$continuation).L$2;
                context = (RoutingContext)((UserController$getUserList.UserController$getUserList$1)$continuation).L$1;
                this = (UserController)((UserController$getUserList.UserController$getUserList$1)$continuation).L$0;
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
        if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
            return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        Map userMap = new LinkedHashMap();
        final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
        if (userMapJson != null) {
            final Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
            }
            userMap = TypeIntrinsics.asMutableMap((Object)map);
        }
        Object userList = null;
        userList = new ArrayList();
        final Map $this$forEach$iv = userMap;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n = 0;
            ((ArrayList<Map<String, Object>>)userList).add(this.formatUser(it.getValue()));
        }
        return ReturnData.setData$default(returnData, userList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object addUser(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$addUser.UserController$addUser$1) {
                final UserController$addUser.UserController$addUser$1 userController$addUser$1 = (UserController$addUser.UserController$addUser$1)$completion;
                if ((userController$addUser$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$addUser.UserController$addUser$1 userController$addUser$2 = userController$addUser$1;
                    userController$addUser$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$addUser.UserController$addUser$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$addUser.UserController$addUser$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$addUser.UserController$addUser$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$addUser.UserController$addUser$1)$continuation).L$0 = this;
                ((UserController$addUser.UserController$addUser$1)$continuation).L$1 = context;
                ((UserController$addUser.UserController$addUser$1)$continuation).L$2 = returnData;
                ((UserController$addUser.UserController$addUser$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$addUser.UserController$addUser$1)$continuation).L$2;
                context = (RoutingContext)((UserController$addUser.UserController$addUser$1)$continuation).L$1;
                this = (UserController)((UserController$addUser.UserController$addUser$1)$continuation).L$0;
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
        if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
            return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
        }
        final String string = context.getBodyAsJson().getString("username");
        final String username = (string == null) ? "" : string;
        final String string2 = context.getBodyAsJson().getString("password");
        final String password = (string2 == null) ? "" : string2;
        if (username.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u7528\u6237\u540d");
        }
        if (password.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u5bc6\u7801");
        }
        if (username.length() < 5) {
            return returnData.setErrorMsg("\u7528\u6237\u540d\u4e0d\u80fd\u4f4e\u4e8e5\u4f4d");
        }
        if (password.length() < 8) {
            return returnData.setErrorMsg("\u5bc6\u7801\u4e0d\u80fd\u4f4e\u4e8e8\u4f4d");
        }
        if (username.equals("default")) {
            return returnData.setErrorMsg("\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u975e\u6cd5\u5b57\u7b26");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        final Regex usernameReg = new Regex("[a-z0-9]+", RegexOption.IGNORE_CASE);
        if (!usernameReg.matches((CharSequence)username)) {
            return returnData.setErrorMsg("\u7528\u6237\u540d\u53ea\u80fd\u7531\u5b57\u6bcd\u548c\u6570\u5b57\u7ec4\u6210");
        }
        Map userMap = new LinkedHashMap();
        final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
        if (userMapJson != null) {
            final Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
            }
            userMap = TypeIntrinsics.asMutableMap((Object)map);
        }
        final Map map2 = userMap;
        final Map defaultValue = null;
        final Map map3 = map2;
        if (map3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        final Map existedUser = map3.getOrDefault(username, defaultValue);
        if (existedUser != null) {
            return returnData.setErrorMsg("\u7528\u6237\u5df2\u5b58\u5728");
        }
        final int userLimit = this.getUserLimit(context);
        if (userMap.keySet().size() >= userLimit) {
            return returnData.setErrorMsg("\u8d85\u8fc7\u7528\u6237\u6570\u4e0a\u9650");
        }
        final Boolean enableWebdav = context.getBodyAsJson().getBoolean("enableWebdav");
        final Boolean enableLocalStore = context.getBodyAsJson().getBoolean("enableLocalStore");
        final Boolean enableBookSource = context.getBodyAsJson().getBoolean("enableBookSource");
        final Boolean enableRssSource = context.getBodyAsJson().getBoolean("enableRssSource");
        final Integer bookSourceLimit = context.getBodyAsJson().getInteger("bookSourceLimit");
        final Integer bookLimit = context.getBodyAsJson().getInteger("bookLimit");
        final String salt = ExtKt.getRandomString(8);
        final String passwordEncrypted = ExtKt.genEncryptedPassword(password, salt);
        final User user;
        final User newUser = user = new User(username, passwordEncrypted, salt, (String)null, 0L, 0L, false, (Map)null, false, false, false, 0, 0, 8184, (DefaultConstructorMarker)null);
        final Boolean b = enableWebdav;
        user.setEnable_webdav((b == null) ? this.getAppConfig().getDefaultUserEnableWebdav() : ((boolean)b));
        final User user2 = newUser;
        final Boolean b2 = enableLocalStore;
        user2.setEnable_local_store((b2 == null) ? this.getAppConfig().getDefaultUserEnableLocalStore() : ((boolean)b2));
        final User user3 = newUser;
        final Boolean b3 = enableBookSource;
        user3.setEnable_book_source((b3 == null) ? this.getAppConfig().getDefaultUserEnableBookSource() : ((boolean)b3));
        final User user4 = newUser;
        final Boolean b4 = enableRssSource;
        user4.setEnable_rss_source((b4 == null) ? this.getAppConfig().getDefaultUserEnableRssSource() : ((boolean)b4));
        final User user5 = newUser;
        final Integer n = bookSourceLimit;
        user5.setBook_source_limit((n == null) ? this.getAppConfig().getDefaultUserBookSourceLimit() : ((int)n));
        final User user6 = newUser;
        final Integer n2 = bookLimit;
        user6.setBook_limit((n2 == null) ? this.getAppConfig().getDefaultUserBookLimit() : ((int)n2));
        userMap.put(newUser.getUsername(), ExtKt.toMap(newUser));
        final String[] array = { "data", "users" };
        final String encode = Json.encode((Object)userMap);
        Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap)");
        ExtKt.saveStorage$default(array, encode, false, null, 12, null);
        Object userList = null;
        userList = new ArrayList();
        final Map $this$forEach$iv = userMap;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n3 = 0;
            ((ArrayList<Map<String, Object>>)userList).add(this.formatUser(it.getValue()));
        }
        return ReturnData.setData$default(returnData, userList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object resetPassword(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$resetPassword.UserController$resetPassword$1) {
                final UserController$resetPassword.UserController$resetPassword$1 userController$resetPassword$1 = (UserController$resetPassword.UserController$resetPassword$1)$completion;
                if ((userController$resetPassword$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$resetPassword.UserController$resetPassword$1 userController$resetPassword$2 = userController$resetPassword$1;
                    userController$resetPassword$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$resetPassword.UserController$resetPassword$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$resetPassword.UserController$resetPassword$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$resetPassword.UserController$resetPassword$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$resetPassword.UserController$resetPassword$1)$continuation).L$0 = this;
                ((UserController$resetPassword.UserController$resetPassword$1)$continuation).L$1 = context;
                ((UserController$resetPassword.UserController$resetPassword$1)$continuation).L$2 = returnData;
                ((UserController$resetPassword.UserController$resetPassword$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$resetPassword.UserController$resetPassword$1)$continuation).L$2;
                context = (RoutingContext)((UserController$resetPassword.UserController$resetPassword$1)$continuation).L$1;
                this = (UserController)((UserController$resetPassword.UserController$resetPassword$1)$continuation).L$0;
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
        if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
            return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
        }
        final String string = context.getBodyAsJson().getString("username");
        final String username = (string == null) ? "" : string;
        final String string2 = context.getBodyAsJson().getString("password");
        final String password = (string2 == null) ? "" : string2;
        if (username.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u7528\u6237\u540d");
        }
        if (password.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u5bc6\u7801");
        }
        if (password.length() < this.getAppConfig().getMinUserPasswordLength()) {
            return returnData.setErrorMsg("\u5bc6\u7801\u4e0d\u80fd\u4f4e\u4e8e" + this.getAppConfig().getMinUserPasswordLength() + '\u4f4d');
        }
        if (username.equals("default")) {
            return returnData.setErrorMsg("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        Map userMap = new LinkedHashMap();
        final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
        if (userMapJson != null) {
            final Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
            }
            userMap = TypeIntrinsics.asMutableMap((Object)map);
        }
        final Map map2 = userMap;
        final Map defaultValue = null;
        final Map map3 = map2;
        if (map3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        final Map existedUser = map3.getOrDefault(username, defaultValue);
        if (existedUser == null) {
            return returnData.setErrorMsg("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        final String salt = ExtKt.getRandomString(8);
        final String passwordEncrypted = ExtKt.genEncryptedPassword(password, salt);
        existedUser.put("salt", salt);
        existedUser.put("password", passwordEncrypted);
        userMap.put(username, existedUser);
        final String[] array = { "data", "users" };
        final String encode = Json.encode((Object)userMap);
        Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap as MutableMap<String, Map<String, Any>>)");
        ExtKt.saveStorage$default(array, encode, false, null, 12, null);
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object deleteUsers(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$deleteUsers.UserController$deleteUsers$1) {
                final UserController$deleteUsers.UserController$deleteUsers$1 userController$deleteUsers$1 = (UserController$deleteUsers.UserController$deleteUsers$1)$completion;
                if ((userController$deleteUsers$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$deleteUsers.UserController$deleteUsers$1 userController$deleteUsers$2 = userController$deleteUsers$1;
                    userController$deleteUsers$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$deleteUsers.UserController$deleteUsers$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).L$0 = this;
                ((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).L$1 = context;
                ((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).L$2 = returnData;
                ((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).L$2;
                context = (RoutingContext)((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).L$1;
                this = (UserController)((UserController$deleteUsers.UserController$deleteUsers$1)$continuation).L$0;
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
        if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
            return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        Map userMap = new LinkedHashMap();
        final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
        if (userMapJson != null) {
            final JsonArray userJsonArray = context.getBodyAsJsonArray();
            int j = 0;
            final int size = userJsonArray.size();
            if (j < size) {
                do {
                    final int i = j;
                    ++j;
                    final String username = userJsonArray.getString(i);
                    if (username != null && userMapJson.containsKey(username)) {
                        userMapJson.remove(username);
                        final File userHome = new File(ExtKt.getWorkDir("storage", "data", username));
                        UserControllerKt.access$getLogger$p().info("delete userHome: {}", (Object)userHome);
                        if (!userHome.exists()) {
                            continue;
                        }
                        ExtKt.deleteRecursively(userHome);
                    }
                } while (j < size);
            }
            final Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
            }
            userMap = TypeIntrinsics.asMutableMap((Object)map);
            final String[] array = { "data", "users" };
            final String encode = Json.encode((Object)userMap);
            Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap)");
            ExtKt.saveStorage$default(array, encode, false, null, 12, null);
        }
        Object userList = null;
        userList = new ArrayList();
        final Map $this$forEach$iv = userMap;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n = 0;
            ((ArrayList<Map<String, Object>>)userList).add(this.formatUser(it.getValue()));
        }
        return ReturnData.setData$default(returnData, userList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object clearInactiveUsers(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$clearInactiveUsers.UserController$clearInactiveUsers$1) {
                final UserController$clearInactiveUsers.UserController$clearInactiveUsers$1 userController$clearInactiveUsers$1 = (UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$completion;
                if ((userController$clearInactiveUsers$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$clearInactiveUsers.UserController$clearInactiveUsers$1 userController$clearInactiveUsers$2 = userController$clearInactiveUsers$1;
                    userController$clearInactiveUsers$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$clearInactiveUsers.UserController$clearInactiveUsers$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0380: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final UserController userController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$0 = this;
                    ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$1 = context;
                    ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$2 = returnData;
                    ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).label = 1;
                    if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$2;
                    context = (RoutingContext)((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$1;
                    this = (UserController)((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    context = (RoutingContext)((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$1;
                    this = (UserController)((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0380;
                }
                case 3: {
                    ResultKt.throwOnFailure($result);
                    return $result;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
                return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
            }
            if (!this.checkManagerAuth(context)) {
                return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
            }
            final Integer inactiveDay = context.getBodyAsJson().getInteger("inactiveDay", Boxing.boxInt(0));
            final UserController userController2 = this;
            Intrinsics.checkNotNullExpressionValue((Object)inactiveDay, "inactiveDay");
            final int intValue = inactiveDay;
            final Continuation $completion3 = $continuation;
            ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$0 = this;
            ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$1 = context;
            ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$2 = null;
            ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).label = 2;
            if (userController2.clearInactiveUsers(intValue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final UserController userController3 = this;
        final RoutingContext context3 = context;
        final Continuation $completion4 = $continuation;
        ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$0 = null;
        ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).L$1 = null;
        ((UserController$clearInactiveUsers.UserController$clearInactiveUsers$1)$continuation).label = 3;
        Object userList;
        if ((userList = userController3.getUserList(context3, (Continuation<? super ReturnData>)$completion4)) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return userList;
    }
    
    @Nullable
    public final Object clearInactiveUsers(final int day, @NotNull final Continuation<? super Unit> $completion) {
        final long expireTime = System.currentTimeMillis() - day * 86400L * 1000L;
        final Object forEachUser = this.forEachUser((Function3<? super CoroutineScope, ? super User, ? super Continuation<? super Boolean>, ?>)new UserController$clearInactiveUsers.UserController$clearInactiveUsers$3(expireTime, (Continuation)null), $completion);
        if (forEachUser == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return forEachUser;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object updateUser(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$updateUser.UserController$updateUser$1) {
                final UserController$updateUser.UserController$updateUser$1 userController$updateUser$1 = (UserController$updateUser.UserController$updateUser$1)$completion;
                if ((userController$updateUser$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$updateUser.UserController$updateUser$1 userController$updateUser$2 = userController$updateUser$1;
                    userController$updateUser$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$updateUser.UserController$updateUser$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$updateUser.UserController$updateUser$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$updateUser.UserController$updateUser$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$updateUser.UserController$updateUser$1)$continuation).L$0 = this;
                ((UserController$updateUser.UserController$updateUser$1)$continuation).L$1 = context;
                ((UserController$updateUser.UserController$updateUser$1)$continuation).L$2 = returnData;
                ((UserController$updateUser.UserController$updateUser$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$updateUser.UserController$updateUser$1)$continuation).L$2;
                context = (RoutingContext)((UserController$updateUser.UserController$updateUser$1)$continuation).L$1;
                this = (UserController)((UserController$updateUser.UserController$updateUser$1)$continuation).L$0;
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
        if (!this.getAppConfig().getSecure() || this.getAppConfig().getSecureKey().length() == 0) {
            return returnData.setErrorMsg("\u4e0d\u652f\u6301\u7684\u64cd\u4f5c");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, (Object)"NEED_SECURE_KEY", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        final String string = context.getBodyAsJson().getString("username");
        final String username = (string == null) ? "" : string;
        if (username.length() == 0) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        final Boolean enableWebdav = context.getBodyAsJson().getBoolean("enableWebdav");
        final Boolean enableLocalStore = context.getBodyAsJson().getBoolean("enableLocalStore");
        final Boolean enableBookSource = context.getBodyAsJson().getBoolean("enableBookSource");
        final Boolean enableRssSource = context.getBodyAsJson().getBoolean("enableRssSource");
        final Integer bookSourceLimit = context.getBodyAsJson().getInteger("bookSourceLimit");
        final Integer bookLimit = context.getBodyAsJson().getInteger("bookLimit");
        Map userMap = new LinkedHashMap();
        final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
        if (userMapJson != null) {
            final Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.MutableMap<kotlin.String, kotlin.Any>>");
            }
            final Map mutableMap;
            userMap = (mutableMap = TypeIntrinsics.asMutableMap((Object)map));
            final Map defaultValue = null;
            final Map map2 = mutableMap;
            if (map2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
            }
            final Map existedUser = map2.getOrDefault(username, defaultValue);
            if (existedUser == null) {
                return returnData.setErrorMsg("\u7528\u6237\u4e0d\u5b58\u5728");
            }
            if (enableWebdav != null) {
                existedUser.put("enable_webdav", enableWebdav);
            }
            if (enableLocalStore != null) {
                existedUser.put("enable_local_store", enableLocalStore);
            }
            if (enableBookSource != null) {
                existedUser.put("enable_book_source", enableBookSource);
            }
            if (enableRssSource != null) {
                existedUser.put("enable_rss_source", enableRssSource);
            }
            if (bookSourceLimit != null) {
                existedUser.put("book_source_limit", bookSourceLimit);
            }
            if (bookLimit != null) {
                existedUser.put("book_limit", bookLimit);
            }
            userMap.put(username, existedUser);
            final String[] array = { "data", "users" };
            final String encode = Json.encode((Object)userMap);
            Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap)");
            ExtKt.saveStorage$default(array, encode, false, null, 12, null);
        }
        Object userList = null;
        userList = new ArrayList();
        final Map $this$forEach$iv = userMap;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n = 0;
            ((ArrayList<Map<String, Object>>)userList).add(this.formatUser(it.getValue()));
        }
        return ReturnData.setData$default(returnData, userList, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object getUserInfo(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$getUserInfo.UserController$getUserInfo$1) {
                final UserController$getUserInfo.UserController$getUserInfo$1 userController$getUserInfo$1 = (UserController$getUserInfo.UserController$getUserInfo$1)$completion;
                if ((userController$getUserInfo$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$getUserInfo.UserController$getUserInfo$1 userController$getUserInfo$2 = userController$getUserInfo$1;
                    userController$getUserInfo$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$getUserInfo.UserController$getUserInfo$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        switch (((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).L$0 = this;
                ((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).L$1 = context;
                ((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).L$2 = returnData;
                ((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).label = 1;
                if (userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).L$2;
                context = (RoutingContext)((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).L$1;
                this = (UserController)((UserController$getUserInfo.UserController$getUserInfo$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final String username = (String)context.session().get("username");
        final Boolean secure = (Boolean)this.getEnv().getProperty("reader.app.secure", (Class)Boolean.TYPE);
        final String secureKey = this.getEnv().getProperty("reader.app.secureKey");
        Object userInfo = null;
        if (username != null) {
            final User user = this.getUserInfoClass(username);
            if (user != null) {
                userInfo = this.formatUser(user);
            }
        }
        final String fontsDir = ExtKt.getWorkDir("storage", "assets", "fonts");
        Object fontsList = null;
        fontsList = new ArrayList();
        final Iterable $this$forEach$iv = ExtKt.listFilesRecursively(new File(fontsDir));
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final File it = (File)element$iv;
            final int n = 0;
            final String name = it.getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object)null) && it.isFile()) {
                final String fileName = it.getName();
                final BaseController baseController = this;
                Intrinsics.checkNotNullExpressionValue((Object)fileName, "fileName");
                final String ext = BaseController.getFileExt$default(baseController, fileName, null, 2, null);
                if (!Intrinsics.areEqual((Object)ext, (Object)"ttf")) {
                    continue;
                }
                ((ArrayList<Map>)fontsList).add(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"name", (Object)it.getName()), TuplesKt.to((Object)"size", (Object)Boxing.boxLong(it.length())) }));
            }
        }
        final ReturnData returnData2 = returnData;
        final Pair[] array2;
        final Pair[] array = array2 = new Pair[] { TuplesKt.to((Object)"userInfo", userInfo), TuplesKt.to((Object)"secure", (Object)secure), null, null };
        final int n2 = 2;
        final String s = "secureKey";
        final String s2 = secureKey;
        array2[n2] = TuplesKt.to((Object)s, (Object)((s2 == null) ? null : Boxing.boxBoolean(s2.length() > 0)));
        array[3] = TuplesKt.to((Object)"fonts", fontsList);
        return ReturnData.setData$default(returnData2, (Object)MapsKt.mapOf(array), (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object saveUserConfig(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$saveUserConfig.UserController$saveUserConfig$1) {
                final UserController$saveUserConfig.UserController$saveUserConfig$1 userController$saveUserConfig$1 = (UserController$saveUserConfig.UserController$saveUserConfig$1)$completion;
                if ((userController$saveUserConfig$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$saveUserConfig.UserController$saveUserConfig$1 userController$saveUserConfig$2 = userController$saveUserConfig$1;
                    userController$saveUserConfig$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$saveUserConfig.UserController$saveUserConfig$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).L$0 = this;
                ((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).L$1 = context;
                ((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).L$2 = returnData;
                ((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).L$2;
                context = (RoutingContext)((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).L$1;
                this = (UserController)((UserController$saveUserConfig.UserController$saveUserConfig$1)$continuation).L$0;
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
        final JsonObject content = context.getBodyAsJson();
        if (content == null) {
            return returnData.setErrorMsg("\u53c2\u6570\u9519\u8bef");
        }
        content.put("@updateTime", Boxing.boxLong(System.currentTimeMillis()));
        final String userNameSpace = this.getUserNameSpace(context);
        this.saveUserStorage(userNameSpace, "userConfig", content);
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object getUserConfig(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$getUserConfig.UserController$getUserConfig$1) {
                final UserController$getUserConfig.UserController$getUserConfig$1 userController$getUserConfig$1 = (UserController$getUserConfig.UserController$getUserConfig$1)$completion;
                if ((userController$getUserConfig$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$getUserConfig.UserController$getUserConfig$1 userController$getUserConfig$2 = userController$getUserConfig$1;
                    userController$getUserConfig$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$getUserConfig.UserController$getUserConfig$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).L$0 = this;
                ((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).L$1 = context;
                ((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).L$2 = returnData;
                ((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).L$2;
                context = (RoutingContext)((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).L$1;
                this = (UserController)((UserController$getUserConfig.UserController$getUserConfig$1)$continuation).L$0;
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
        final JsonObject userConfig = ExtKt.asJsonObject(this.getUserStorage(userNameSpace, "userConfig"));
        if (userConfig == null) {
            return returnData.setErrorMsg("\u6ca1\u6709\u5907\u4efd\u6587\u4ef6");
        }
        final ReturnData returnData2 = returnData;
        final Map map = userConfig.getMap();
        Intrinsics.checkNotNullExpressionValue((Object)map, "userConfig.map");
        return ReturnData.setData$default(returnData2, (Object)map, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object uploadFile(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$uploadFile.UserController$uploadFile$1) {
                final UserController$uploadFile.UserController$uploadFile$1 userController$uploadFile$1 = (UserController$uploadFile.UserController$uploadFile$1)$completion;
                if ((userController$uploadFile$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$uploadFile.UserController$uploadFile$1 userController$uploadFile$2 = userController$uploadFile$1;
                    userController$uploadFile$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$uploadFile.UserController$uploadFile$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$uploadFile.UserController$uploadFile$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$uploadFile.UserController$uploadFile$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$uploadFile.UserController$uploadFile$1)$continuation).L$0 = this;
                ((UserController$uploadFile.UserController$uploadFile$1)$continuation).L$1 = context;
                ((UserController$uploadFile.UserController$uploadFile$1)$continuation).L$2 = returnData;
                ((UserController$uploadFile.UserController$uploadFile$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$uploadFile.UserController$uploadFile$1)$continuation).L$2;
                context = (RoutingContext)((UserController$uploadFile.UserController$uploadFile$1)$continuation).L$1;
                this = (UserController)((UserController$uploadFile.UserController$uploadFile$1)$continuation).L$0;
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
        if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
            return returnData.setErrorMsg("\u8bf7\u4e0a\u4f20\u6587\u4ef6");
        }
        Object userNameSpace = null;
        userNameSpace = this.getUserNameSpace(context);
        Object fileList = null;
        fileList = new JsonArray();
        Object type = null;
        type = context.request().getParam("type");
        final CharSequence charSequence = (CharSequence)type;
        if (charSequence == null || charSequence.length() == 0) {
            type = "images";
        }
        final Set fileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue((Object)fileUploads, "context.fileUploads()");
        final Iterable $this$forEach$iv = fileUploads;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final FileUpload it = (FileUpload)element$iv;
            final int n = 0;
            final File file = new File(it.uploadedFileName());
            UserControllerKt.access$getLogger$p().info("uploadFile: {} {} {}", new Object[] { it.uploadedFileName(), it.fileName(), file });
            if (file.exists()) {
                final String fileName = it.fileName();
                final String[] array;
                final String[] subDirFiles = array = new String[] { "storage", "assets", (String)userNameSpace, null, null };
                final int n2 = 3;
                final Object o = type;
                Intrinsics.checkNotNullExpressionValue(o, "type");
                array[n2] = (String)o;
                final String[] array2 = subDirFiles;
                final int n3 = 4;
                final String s = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s, "fileName");
                array2[n3] = s;
                final File newFile = new File(ExtKt.getWorkDir(subDirFiles));
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                if (newFile.exists()) {
                    newFile.delete();
                }
                UserControllerKt.access$getLogger$p().info("moveTo: {}", (Object)newFile);
                if (FilesKt.copyRecursively$default(file, newFile, false, (Function2)null, 6, (Object)null)) {
                    ((JsonArray)fileList).add("/assets/" + (String)userNameSpace + '/' + type + '/' + (Object)fileName);
                }
                ExtKt.deleteRecursively(file);
            }
        }
        final ReturnData returnData2 = returnData;
        final List list = ((JsonArray)fileList).getList();
        Intrinsics.checkNotNullExpressionValue((Object)list, "fileList.getList()");
        return ReturnData.setData$default(returnData2, (Object)list, (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object deleteFile(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$deleteFile.UserController$deleteFile$1) {
                final UserController$deleteFile.UserController$deleteFile$1 userController$deleteFile$1 = (UserController$deleteFile.UserController$deleteFile$1)$completion;
                if ((userController$deleteFile$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$deleteFile.UserController$deleteFile$1 userController$deleteFile$2 = userController$deleteFile$1;
                    userController$deleteFile$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$deleteFile.UserController$deleteFile$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$deleteFile.UserController$deleteFile$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((UserController$deleteFile.UserController$deleteFile$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final UserController userController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((UserController$deleteFile.UserController$deleteFile$1)$continuation).L$0 = this;
                ((UserController$deleteFile.UserController$deleteFile$1)$continuation).L$1 = context;
                ((UserController$deleteFile.UserController$deleteFile$1)$continuation).L$2 = returnData;
                ((UserController$deleteFile.UserController$deleteFile$1)$continuation).label = 1;
                if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((UserController$deleteFile.UserController$deleteFile$1)$continuation).L$2;
                context = (RoutingContext)((UserController$deleteFile.UserController$deleteFile$1)$continuation).L$1;
                this = (UserController)((UserController$deleteFile.UserController$deleteFile$1)$continuation).L$0;
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
        String url = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("url");
            final String s = (string == null) ? "" : string;
        }
        else {
            final List queryParam = context.queryParam("url");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            url = ((s2 == null) ? "" : s2);
        }
        if (url.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u6587\u4ef6\u94fe\u63a5");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        if (!StringsKt.startsWith$default(url, "/assets/" + userNameSpace + '/', false, 2, (Object)null)) {
            return returnData.setErrorMsg("\u6587\u4ef6\u94fe\u63a5\u9519\u8bef");
        }
        final File file = new File(ExtKt.getWorkDir(Intrinsics.stringPlus("storage", (Object)url)));
        UserControllerKt.access$getLogger$p().info("delete file: {}", (Object)file);
        ExtKt.deleteRecursively(file);
        return ReturnData.setData$default(returnData, (Object)"", (String)null, 2, (Object)null);
    }
    
    @Nullable
    public final Object downloadBackupFile(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$downloadBackupFile.UserController$downloadBackupFile$1) {
                final UserController$downloadBackupFile.UserController$downloadBackupFile$1 userController$downloadBackupFile$1 = (UserController$downloadBackupFile.UserController$downloadBackupFile$1)$completion;
                if ((userController$downloadBackupFile$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$downloadBackupFile.UserController$downloadBackupFile$1 userController$downloadBackupFile$2 = userController$downloadBackupFile$1;
                    userController$downloadBackupFile$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$downloadBackupFile.UserController$downloadBackupFile$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData l$1 = null;
        Object userBackup = null;
        Label_0452: {
            String userNameSpace = null;
            BookController bookController = null;
            Object lastBackFileFromWebdav = null;
            Label_0331: {
                ReturnData returnData = null;
                Object checkAuth = null;
                switch (((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final UserController userController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$0 = this;
                        ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$1 = context;
                        ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$2 = returnData;
                        ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).label = 1;
                        if ((checkAuth = userController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$2;
                        context = (RoutingContext)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$1;
                        this = (UserController)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        checkAuth = $result;
                        break;
                    }
                    case 2: {
                        userNameSpace = (String)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$3;
                        bookController = (BookController)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$2;
                        l$1 = (ReturnData)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$1;
                        context = (RoutingContext)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        lastBackFileFromWebdav = $result;
                        break Label_0331;
                    }
                    case 3: {
                        l$1 = (ReturnData)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$1;
                        context = (RoutingContext)((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        userBackup = $result;
                        break Label_0452;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                if (!(boolean)checkAuth) {
                    VertExtKt.success(context, ReturnData.setData$default(returnData, (Object)"NEED_LOGIN", (String)null, 2, (Object)null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"));
                    return Unit.INSTANCE;
                }
                bookController = new BookController(this.getCoroutineContext());
                userNameSpace = this.getUserNameSpace(context);
                final BookController bookController2 = bookController;
                final String userNameSpace2 = userNameSpace;
                final Continuation $completion3 = $continuation;
                ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$0 = context;
                ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$1 = returnData;
                ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$2 = bookController;
                ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$3 = userNameSpace;
                ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).label = 2;
                if ((lastBackFileFromWebdav = bookController2.getLastBackFileFromWebdav(userNameSpace2, (Continuation<? super String>)$completion3)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            final String latestZipFilePath = (String)lastBackFileFromWebdav;
            final String backupDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "backup");
            final BookController bookController3 = bookController;
            final String userNameSpace3 = userNameSpace;
            final String backupDir2 = backupDir;
            final String latestZipFilePath2 = latestZipFilePath;
            final Continuation $completion4 = $continuation;
            ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$0 = context;
            ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$1 = l$1;
            ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$2 = null;
            ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).L$3 = null;
            ((UserController$downloadBackupFile.UserController$downloadBackupFile$1)$continuation).label = 3;
            if ((userBackup = bookController3.createUserBackup(userNameSpace3, backupDir2, latestZipFilePath2, (Continuation<? super File>)$completion4)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final File backupFile = (File)userBackup;
        if (backupFile == null) {
            VertExtKt.success(context, l$1.setErrorMsg("\u5907\u4efd\u5931\u8d25"));
            return Unit.INSTANCE;
        }
        final HttpServerResponse response = context.response().putHeader("Cache-Control", "86400");
        response.putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", (Object)URLEncoder.encode(backupFile.getName(), "UTF-8")));
        response.sendFile(backupFile.toString());
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object forEachUser(@NotNull final Function3<? super CoroutineScope, ? super User, ? super Continuation<? super Boolean>, ?> handler, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof UserController$forEachUser.UserController$forEachUser$1) {
                final UserController$forEachUser.UserController$forEachUser$1 userController$forEachUser$1 = (UserController$forEachUser.UserController$forEachUser$1)$completion;
                if ((userController$forEachUser$1.label & Integer.MIN_VALUE) != 0x0) {
                    final UserController$forEachUser.UserController$forEachUser$1 userController$forEachUser$2 = userController$forEachUser$1;
                    userController$forEachUser$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new UserController$forEachUser.UserController$forEachUser$1(this, (Continuation)$completion);
        }
        final Object $result = ((UserController$forEachUser.UserController$forEachUser$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        while (true) {
            Label_0648: {
                final Iterator iterator;
                final Ref$BooleanRef ref$BooleanRef;
                Object invoke = null;
                Label_0626: {
                    Ref$ObjectRef userMap = null;
                    Ref$BooleanRef hasChanged = null;
                    Iterator $this$forEachUser_u24lambda_u2d7 = null;
                    Iterator l$5 = null;
                    final Ref$ObjectRef ref$ObjectRef2;
                    switch (((UserController$forEachUser.UserController$forEachUser$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            if (this.getAppConfig().getSecure()) {
                                userMap = new Ref$ObjectRef();
                                userMap.element = new LinkedHashMap<Object, Object>();
                                final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
                                if (userMapJson != null) {
                                    final Ref$ObjectRef ref$ObjectRef = userMap;
                                    final Map map = userMapJson.getMap();
                                    if (map == null) {
                                        throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                                    }
                                    ref$ObjectRef.element = TypeIntrinsics.asMutableMap((Object)map);
                                }
                                hasChanged = new Ref$BooleanRef();
                                $this$forEachUser_u24lambda_u2d7 = ((Map)userMap.element).entrySet().iterator();
                                final int n = 0;
                                final Iterator $this$forEach$iv = $this$forEachUser_u24lambda_u2d7;
                                final int $i$f$forEach = 0;
                                l$5 = $this$forEach$iv;
                                break;
                            }
                            return Unit.INSTANCE;
                        }
                        case 1: {
                            final int n = 0;
                            final int $i$f$forEach = 0;
                            final int n2 = 0;
                            l$5 = (Iterator)((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$5;
                            iterator = (Iterator)((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$4;
                            ref$BooleanRef = (Ref$BooleanRef)((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$3;
                            ref$ObjectRef2 = (Ref$ObjectRef)((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$2;
                            final Function3 function3 = (Function3)((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$1;
                            this = (UserController)((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            invoke = $result;
                            break Label_0626;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    if (l$5.hasNext()) {
                        final Object element$iv = l$5.next();
                        final Map.Entry it = (Map.Entry)element$iv;
                        final int n2 = 0;
                        final Map user = it.getValue();
                        if (user == null) {
                            break Label_0648;
                        }
                        final String s = user.getOrDefault("username", "");
                        final String username = (s == null) ? "" : s;
                        if (username.length() <= 0) {
                            break Label_0648;
                        }
                        final Map map2 = (Map)userMap.element;
                        final Map defaultValue = null;
                        final Map map3 = map2;
                        if (map3 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
                        }
                        final Map map4 = map3.getOrDefault(username, defaultValue);
                        User user2;
                        if (map4 == null) {
                            user2 = null;
                        }
                        else {
                            final Map $this$toDataClass$iv = map4;
                            final int $i$f$toDataClass = 0;
                            final Object $this$convert$iv$iv = $this$toDataClass$iv;
                            final int $i$f$convert = 0;
                            final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                            user2 = (User)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() {}.getType());
                        }
                        final User existedUser = user2;
                        if (existedUser == null) {
                            break Label_0648;
                        }
                        final User user3 = existedUser;
                        final Continuation continuation = $continuation;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$0 = this;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$1 = handler;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$2 = userMap;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$3 = hasChanged;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$4 = $this$forEachUser_u24lambda_u2d7;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).L$5 = l$5;
                        ((UserController$forEachUser.UserController$forEachUser$1)$continuation).label = 1;
                        if ((invoke = handler.invoke((Object)this, (Object)user3, (Object)continuation)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                    }
                    else {
                        if (ref$BooleanRef.element) {
                            final String[] array = { "data", "users" };
                            final String encode = Json.encode(ref$ObjectRef2.element);
                            Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap)");
                            ExtKt.saveStorage$default(array, encode, false, null, 12, null);
                            return Unit.INSTANCE;
                        }
                        return Unit.INSTANCE;
                    }
                }
                if (invoke) {
                    ref$BooleanRef.element = true;
                    iterator.remove();
                }
            }
            continue;
        }
    }
}
