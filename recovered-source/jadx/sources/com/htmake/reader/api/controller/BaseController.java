package com.htmake.reader.api.controller;

import com.google.gson.reflect.TypeToken;
import com.htmake.reader.config.AppConfig;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.SpringContextUtils;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.utils.ACache;
import io.legado.app.utils.FileUtils;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SpreadBuilder;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;
import me.ag2s.epublib.browsersupport.NavigationHistory;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

/* JADX INFO: compiled from: BaseController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BaseController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0019\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0019J\u000e\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u001a\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0\u001c2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0018\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\u001d2\b\b\u0002\u0010\"\u001a\u00020\u001dJ\u0010\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020\u001dJ\u001c\u0010&\u001a\u0010\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e\u0018\u00010'2\u0006\u0010%\u001a\u00020\u001dJ\u000e\u0010(\u001a\u00020\u001d2\u0006\u0010\u0017\u001a\u00020\u0018J)\u0010)\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0017\u001a\u00020\u001e2\u0012\u0010*\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001d0+\"\u00020\u001d¢\u0006\u0002\u0010,J\u000e\u0010-\u001a\u00020\u001d2\u0006\u0010\u0017\u001a\u00020\u001eJX\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u00102\u0006\u00101\u001a\u00020\u00102\u0006\u00102\u001a\u00020\u00102-\u00103\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e05\u0012\u0006\u0012\u0004\u0018\u00010\u001e04¢\u0006\u0002\b6H\u0086@ø\u0001\u0000¢\u0006\u0002\u00107J\u0082\u0001\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u00102\u0006\u00101\u001a\u00020\u00102\u0006\u00102\u001a\u00020\u00102-\u00103\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e05\u0012\u0006\u0012\u0004\u0018\u00010\u001e04¢\u0006\u0002\b62(\u00108\u001a$\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u001e0:j\b\u0012\u0004\u0012\u00020\u001e`;\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u001609H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010<J7\u0010=\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0'2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010>\u001a\u00020$2\b\b\u0002\u0010?\u001a\u00020\u0016H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010@J\u001e\u0010A\u001a\u00020/2\u0006\u0010\u0017\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u001d2\u0006\u0010B\u001a\u00020\u001eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006C"}, d2 = {"Lcom/htmake/reader/api/controller/BaseController;", "Lkotlinx/coroutines/CoroutineScope;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "appConfig", "Lcom/htmake/reader/config/AppConfig;", "getAppConfig", "()Lcom/htmake/reader/config/AppConfig;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "env", "Lorg/springframework/core/env/Environment;", "getEnv", "()Lorg/springframework/core/env/Environment;", "loginExpireDays", PackageDocumentBase.PREFIX_OPF, "userMutex", "Lkotlinx/coroutines/sync/Mutex;", "getUserMutex", "()Lkotlinx/coroutines/sync/Mutex;", "checkAuth", PackageDocumentBase.PREFIX_OPF, "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkManagerAuth", "formatUser", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "userInfo", "getFileExt", RSSKeywords.RSS_ITEM_URL, "defaultExt", "getUserInfoClass", "Lcom/htmake/reader/entity/User;", "username", "getUserInfoMap", PackageDocumentBase.PREFIX_OPF, "getUserNameSpace", "getUserStorage", "path", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/String;", "getUserWebdavHome", "limitConcurrent", PackageDocumentBase.PREFIX_OPF, "concurrentCount", "startIndex", "endIndex", "handler", "Lkotlin/Function3;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(IIILkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "needContinue", "Lkotlin/Function2;", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "(IIILkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveUserSession", "user", "regenerateToken", "(Lio/vertx/ext/web/RoutingContext;Lcom/htmake/reader/entity/User;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveUserStorage", "value", "reader-pro"})
public class BaseController implements CoroutineScope {

    @NotNull
    private final CoroutineContext coroutineContext;
    private int loginExpireDays;

    @NotNull
    private final AppConfig appConfig;

    @NotNull
    private final Environment env;

    @NotNull
    private final Mutex userMutex;

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BaseController$checkAuth$1, reason: invalid class name */
    /* JADX INFO: compiled from: BaseController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BaseController$checkAuth$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BaseController.kt", l = {117}, i = {}, s = {}, n = {}, m = "checkAuth", c = "com.htmake.reader.api.controller.BaseController")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int I$0;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BaseController.this.checkAuth(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BaseController$limitConcurrent$3, reason: invalid class name */
    /* JADX INFO: compiled from: BaseController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BaseController$limitConcurrent$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BaseController.kt", l = {289, 320}, i = {0}, s = {"L$4"}, n = {"resultList"}, m = "limitConcurrent", c = "com.htmake.reader.api.controller.BaseController")
    static final class AnonymousClass3 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        int I$4;
        int I$5;
        long J$0;
        /* synthetic */ Object result;
        int label;

        AnonymousClass3(Continuation<? super AnonymousClass3> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BaseController.this.limitConcurrent(0, 0, 0, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BaseController$saveUserSession$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BaseController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BaseController$saveUserSession$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BaseController.kt", l = {33}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "Z$0"}, n = {"this", "context", "user", "regenerateToken"}, m = "saveUserSession", c = "com.htmake.reader.api.controller.BaseController")
    static final class C00101 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        boolean Z$0;
        /* synthetic */ Object result;
        int label;

        C00101(Continuation<? super C00101> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BaseController.this.saveUserSession(null, null, false, (Continuation) this);
        }
    }

    public BaseController(@NotNull CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
        this.coroutineContext = coroutineContext;
        this.loginExpireDays = 7;
        Object bean = SpringContextUtils.getBean("appConfig", AppConfig.class);
        Intrinsics.checkNotNullExpressionValue(bean, "getBean(\"appConfig\", AppConfig::class.java)");
        this.appConfig = (AppConfig) bean;
        Object bean2 = SpringContextUtils.getBean((Class<Object>) Environment.class);
        Intrinsics.checkNotNullExpressionValue(bean2, "getBean(Environment::class.java)");
        this.env = (Environment) bean2;
        this.userMutex = MutexKt.Mutex$default(false, 1, (Object) null);
    }

    @NotNull
    public CoroutineContext getCoroutineContext() {
        return this.coroutineContext;
    }

    @NotNull
    public final AppConfig getAppConfig() {
        return this.appConfig;
    }

    @NotNull
    public final Environment getEnv() {
        return this.env;
    }

    @NotNull
    public final Mutex getUserMutex() {
        return this.userMutex;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveUserSession(@NotNull RoutingContext context, @NotNull User user, boolean regenerateToken, @NotNull Continuation<? super Map<String, ? extends Object>> $completion) {
        C00101 c00101;
        if ($completion instanceof C00101) {
            c00101 = (C00101) $completion;
            if ((c00101.label & Integer.MIN_VALUE) != 0) {
                c00101.label -= Integer.MIN_VALUE;
            } else {
                c00101 = new C00101($completion);
            }
        }
        Object $result = c00101.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c00101.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c00101.L$0 = this;
                    c00101.L$1 = context;
                    c00101.L$2 = user;
                    c00101.Z$0 = regenerateToken;
                    c00101.label = 1;
                    if (Mutex.DefaultImpls.lock$default(getUserMutex(), (Object) null, c00101, 1, (Object) null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    regenerateToken = c00101.Z$0;
                    user = (User) c00101.L$2;
                    context = (RoutingContext) c00101.L$1;
                    this = (BaseController) c00101.L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            user.setLast_login_at(System.currentTimeMillis());
            if (regenerateToken) {
                user.setToken(ExtKt.genEncryptedPassword(user.getUsername(), String.valueOf(System.currentTimeMillis())));
                Map<String, Long> mapMutableMapOf = null;
                long expire = System.currentTimeMillis() + ((long) (this.loginExpireDays * ACache.TIME_DAY * NavigationHistory.DEFAULT_MAX_HISTORY_SIZE));
                if (user.getToken_map() != null) {
                    Map<String, Long> token_map = user.getToken_map();
                    mapMutableMapOf = TypeIntrinsics.isMutableMap(token_map) ? token_map : null;
                }
                if (mapMutableMapOf == null) {
                    mapMutableMapOf = MapsKt.mutableMapOf(new Pair[]{TuplesKt.to(user.getToken(), Boxing.boxLong(expire))});
                } else {
                    mapMutableMapOf.put(user.getToken(), Boxing.boxLong(expire));
                }
                final User user2 = user;
                CollectionsKt.removeAll(mapMutableMapOf.values(), new Function1<Long, Boolean>() { // from class: com.htmake.reader.api.controller.BaseController.saveUserSession.2
                    {
                        super(1);
                    }

                    public final boolean invoke(long it) {
                        return it < user2.getLast_login_at();
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                        return Boolean.valueOf(invoke(((Number) p1).longValue()));
                    }
                });
                user.setToken_map(mapMutableMapOf);
            }
            Map userMap = new LinkedHashMap();
            JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
            if (userMapJson != null) {
                Map map = userMapJson.getMap();
                if (map == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                }
                userMap = TypeIntrinsics.asMutableMap(map);
            }
            userMap.put(user.getUsername(), ExtKt.toMap(user));
            String strEncode = Json.encode(userMap);
            Intrinsics.checkNotNullExpressionValue(strEncode, "encode(userMap)");
            ExtKt.saveStorage$default(new String[]{"data", "users"}, strEncode, false, null, 12, null);
            Map<String, Object> user3 = this.formatUser(user);
            context.session().put("username", user.getUsername());
            context.put("username", user.getUsername());
            Mutex.DefaultImpls.unlock$default(this.getUserMutex(), (Object) null, 1, (Object) null);
            return user3;
        } catch (Throwable th) {
            Mutex.DefaultImpls.unlock$default(getUserMutex(), (Object) null, 1, (Object) null);
            throw th;
        }
    }

    public static /* synthetic */ Object saveUserSession$default(BaseController baseController, RoutingContext routingContext, User user, boolean z, Continuation continuation, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: saveUserSession");
        }
        if ((i & 4) != 0) {
            z = true;
        }
        return baseController.saveUserSession(routingContext, user, z, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x035e  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0362  */
    /* JADX WARN: Type inference failed for: r2v11, types: [com.htmake.reader.api.controller.BaseController$checkAuth$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object checkAuth(@NotNull RoutingContext context, @NotNull Continuation<? super Boolean> $completion) {
        AnonymousClass1 anonymousClass1;
        int i;
        User existedUser;
        String json;
        User user;
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
                if (!getAppConfig().getSecure()) {
                    return Boxing.boxBoolean(true);
                }
                String str = (String) context.session().get("username");
                String username = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                User userInfo = getUserInfoClass(username);
                if (userInfo != null) {
                    context.put("username", userInfo.getUsername());
                    context.put("userInfo", userInfo);
                    return Boxing.boxBoolean(true);
                }
                List listQueryParam = context.queryParam("accessToken");
                Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"accessToken\")");
                String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                String accessToken = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                if (accessToken.length() > 0) {
                    Map userMap = new LinkedHashMap();
                    JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
                    if (userMapJson != null) {
                        Map map = userMapJson.getMap();
                        Map map2 = TypeIntrinsics.isMutableMap(map) ? map : null;
                        userMap = map2 == null ? new LinkedHashMap() : map2;
                    }
                    List tmp = StringsKt.split$default(accessToken, new String[]{":"}, false, 2, 2, (Object) null);
                    if (tmp.size() >= 2) {
                        String _username = (String) tmp.get(0);
                        String token = (String) tmp.get(1);
                        Object obj = (Map) userMap.getOrDefault(_username, null);
                        if (obj == null) {
                            user = null;
                        } else {
                            if (!(obj instanceof String)) {
                                json = ExtKt.getGson().toJson(obj);
                            } else {
                                json = (String) obj;
                            }
                            String json$iv$iv = json;
                            user = (User) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() { // from class: com.htmake.reader.api.controller.BaseController$checkAuth$$inlined$toDataClass$1
                            }.getType());
                        }
                        existedUser = user;
                        if (existedUser != null) {
                            if (token.length() > 0) {
                                i = 0;
                                if ((existedUser.getToken().length() > 0) && Intrinsics.areEqual(existedUser.getToken(), token)) {
                                    i = 1;
                                }
                                if (i == 0 && existedUser.getToken_map() != null) {
                                    Map<String, Long> token_map = existedUser.getToken_map();
                                    Map<String, Long> map3 = TypeIntrinsics.isMutableMap(token_map) ? token_map : null;
                                    if (map3 != null && map3.containsKey(token)) {
                                        if (map3.getOrDefault(token, Boxing.boxLong(0L)).longValue() > System.currentTimeMillis()) {
                                            i = 1;
                                            map3.put(token, Boxing.boxLong(System.currentTimeMillis() + ((long) (this.loginExpireDays * ACache.TIME_DAY * NavigationHistory.DEFAULT_MAX_HISTORY_SIZE))));
                                        } else {
                                            map3.remove(token);
                                        }
                                        existedUser.setToken_map(map3);
                                    }
                                }
                                if (i != 0) {
                                    anonymousClass1.L$0 = context;
                                    anonymousClass1.L$1 = existedUser;
                                    anonymousClass1.I$0 = i;
                                    anonymousClass1.label = 1;
                                    if (saveUserSession(context, existedUser, false, anonymousClass1) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    context.put("username", existedUser.getUsername());
                                    context.put("userInfo", existedUser);
                                }
                                return Boxing.boxBoolean(i == 0);
                            }
                        }
                    }
                }
                return Boxing.boxBoolean(false);
            case 1:
                i = anonymousClass1.I$0;
                existedUser = (User) anonymousClass1.L$1;
                context = (RoutingContext) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                context.put("username", existedUser.getUsername());
                context.put("userInfo", existedUser);
                return Boxing.boxBoolean(i == 0);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public final boolean checkManagerAuth(@NotNull RoutingContext context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (!this.appConfig.getSecure()) {
            return true;
        }
        if (this.appConfig.getSecureKey().length() == 0) {
            return true;
        }
        List listQueryParam = context.queryParam("secureKey");
        Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"secureKey\")");
        String str = (String) CollectionsKt.firstOrNull(listQueryParam);
        String secureKey = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        if (Intrinsics.areEqual(secureKey, this.appConfig.getSecureKey())) {
            List listQueryParam2 = context.queryParam("userNS");
            Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"userNS\")");
            String userNS = (String) CollectionsKt.firstOrNull(listQueryParam2);
            String str2 = userNS;
            if (!(str2 == null || str2.length() == 0)) {
                context.put("userNameSpace", userNS);
                return true;
            }
            context.remove("userNameSpace");
            return true;
        }
        return false;
    }

    @NotNull
    public final String getUserNameSpace(@NotNull RoutingContext context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (!this.appConfig.getSecure()) {
            return "default";
        }
        checkManagerAuth(context);
        String userNS = (String) context.get("userNameSpace");
        String str = userNS;
        if (!(str == null || str.length() == 0)) {
            return userNS;
        }
        String username = (String) context.get("username");
        if (username != null) {
            return username;
        }
        return "default";
    }

    @Nullable
    public final String getUserStorage(@NotNull Object context, @NotNull String... path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        String userNameSpace = PackageDocumentBase.PREFIX_OPF;
        if (context instanceof RoutingContext) {
            userNameSpace = getUserNameSpace((RoutingContext) context);
        } else if (context instanceof String) {
            userNameSpace = (String) context;
        }
        if (userNameSpace.length() == 0) {
            SpreadBuilder spreadBuilder = new SpreadBuilder(2);
            spreadBuilder.add("data");
            spreadBuilder.addSpread(path);
            return ExtKt.getStorage$default((String[]) spreadBuilder.toArray(new String[spreadBuilder.size()]), null, 2, null);
        }
        SpreadBuilder spreadBuilder2 = new SpreadBuilder(3);
        spreadBuilder2.add("data");
        spreadBuilder2.add(userNameSpace);
        spreadBuilder2.addSpread(path);
        return ExtKt.getStorage$default((String[]) spreadBuilder2.toArray(new String[spreadBuilder2.size()]), null, 2, null);
    }

    public final void saveUserStorage(@NotNull Object context, @NotNull String path, @NotNull Object value) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(value, "value");
        String userNameSpace = PackageDocumentBase.PREFIX_OPF;
        if (context instanceof RoutingContext) {
            userNameSpace = getUserNameSpace((RoutingContext) context);
        } else if (context instanceof String) {
            userNameSpace = (String) context;
        }
        if (userNameSpace.length() == 0) {
            ExtKt.saveStorage$default(new String[]{"data", path}, value, false, null, 12, null);
        } else {
            ExtKt.saveStorage$default(new String[]{"data", userNameSpace, path}, value, false, null, 12, null);
        }
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.htmake.reader.api.controller.BaseController$getUserInfoClass$$inlined$toDataClass$1] */
    @Nullable
    public final User getUserInfoClass(@NotNull String username) {
        String json;
        User user;
        Intrinsics.checkNotNullParameter(username, "username");
        Object userInfoMap = getUserInfoMap(username);
        if (userInfoMap == null) {
            user = null;
        } else {
            if (!(userInfoMap instanceof String)) {
                json = ExtKt.getGson().toJson(userInfoMap);
            } else {
                json = (String) userInfoMap;
            }
            String json$iv$iv = json;
            user = (User) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() { // from class: com.htmake.reader.api.controller.BaseController$getUserInfoClass$$inlined$toDataClass$1
            }.getType());
        }
        User user2 = user;
        return user2;
    }

    @Nullable
    public final Map<String, Object> getUserInfoMap(@NotNull String username) {
        Intrinsics.checkNotNullParameter(username, "username");
        if (username.length() == 0) {
            return null;
        }
        Map userMap = new LinkedHashMap();
        JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
        if (userMapJson != null) {
            Map map = userMapJson.getMap();
            if (map == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
            }
            userMap = TypeIntrinsics.asMutableMap(map);
        }
        Map map2 = userMap;
        if (map2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        return (Map) map2.getOrDefault(username, null);
    }

    /* JADX WARN: Type inference failed for: r2v20, types: [com.htmake.reader.api.controller.BaseController$formatUser$$inlined$toDataClass$1] */
    @NotNull
    public final Map<String, Object> formatUser(@NotNull Object userInfo) {
        Intrinsics.checkNotNullParameter(userInfo, "userInfo");
        User user = null;
        if (userInfo instanceof User) {
            user = (User) userInfo;
        } else {
            Object obj = userInfo instanceof Map ? (Map) userInfo : null;
            if (obj != null) {
                String json$iv$iv = obj instanceof String ? (String) obj : ExtKt.getGson().toJson(obj);
                user = (User) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() { // from class: com.htmake.reader.api.controller.BaseController$formatUser$$inlined$toDataClass$1
                }.getType());
            }
        }
        return user == null ? new LinkedHashMap() : MapsKt.mutableMapOf(new Pair[]{TuplesKt.to("username", user.getUsername()), TuplesKt.to("lastLoginAt", Long.valueOf(user.getLast_login_at())), TuplesKt.to("accessToken", user.getUsername() + ':' + user.getToken()), TuplesKt.to("enableWebdav", Boolean.valueOf(user.getEnable_webdav())), TuplesKt.to("enableLocalStore", Boolean.valueOf(user.getEnable_local_store())), TuplesKt.to("enableBookSource", Boolean.valueOf(user.getEnable_book_source())), TuplesKt.to("enableRssSource", Boolean.valueOf(user.getEnable_rss_source())), TuplesKt.to("bookSourceLimit", Integer.valueOf(user.getBook_source_limit())), TuplesKt.to("bookLimit", Integer.valueOf(user.getBook_limit())), TuplesKt.to("createdAt", Long.valueOf(user.getCreated_at()))});
    }

    @NotNull
    public final String getUserWebdavHome(@NotNull Object context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String prefix = ExtKt.getWorkDir("storage", "data");
        String userNameSpace = PackageDocumentBase.PREFIX_OPF;
        if (context instanceof RoutingContext) {
            userNameSpace = getUserNameSpace((RoutingContext) context);
        } else if (context instanceof String) {
            userNameSpace = (String) context;
        }
        if (userNameSpace.length() > 0) {
            prefix = prefix + ((Object) File.separator) + userNameSpace;
        }
        String prefix2 = prefix + ((Object) File.separator) + "webdav";
        File file = new File(prefix2);
        if (!file.exists()) {
            file.mkdirs();
        }
        return prefix2;
    }

    public static /* synthetic */ String getFileExt$default(BaseController baseController, String str, String str2, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getFileExt");
        }
        if ((i & 2) != 0) {
            str2 = PackageDocumentBase.PREFIX_OPF;
        }
        return baseController.getFileExt(str, str2);
    }

    @NotNull
    public final String getFileExt(@NotNull String url, @NotNull String defaultExt) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(defaultExt, "defaultExt");
        return FileUtils.INSTANCE.getFileExtetion(url, defaultExt);
    }

    @Nullable
    public final Object limitConcurrent(int concurrentCount, int startIndex, int endIndex, @NotNull Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ? extends Object> handler, @NotNull Continuation<? super Unit> $completion) {
        Object objLimitConcurrent = limitConcurrent(concurrentCount, startIndex, endIndex, handler, new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BaseController.limitConcurrent.2
            public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
            }

            public final boolean invoke(@NotNull ArrayList<Object> $noName_0, int $noName_1) {
                Intrinsics.checkNotNullParameter($noName_0, "$noName_0");
                return true;
            }
        }, $completion);
        return objLimitConcurrent == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objLimitConcurrent : Unit.INSTANCE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x02d3, code lost:

        if (0 < r28) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x02d6, code lost:

        r0 = r27;
        r27 = r27 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x02dd, code lost:

        r33 = r26;
        r36.L$0 = r15;
        r36.L$1 = r24;
        r36.L$2 = r26;
        r36.L$3 = r33;
        r36.L$4 = null;
        r36.I$0 = r18;
        r36.I$1 = r27;
        r36.I$2 = r28;
        r36.label = 2;
        r0 = ((kotlinx.coroutines.Deferred) r24.get(r0)).await(r36);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0333, code lost:

        if (r0 != r0) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0338, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0391, code lost:

        if (r27 >= r28) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0394, code lost:

        r24.clear();
        r15.invoke(r26, kotlin.coroutines.jvm.internal.Boxing.boxInt(r18));
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00e3, code lost:

        r24.clear();
        r24.addAll(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0082, code lost:

        r17 = r17 + 1;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x01d8 A[LOOP:1: B:30:0x01d8->B:82:?, LOOP_START, PHI: r19 r28
      0x01d8: PHI (r19v2 'resultCount' int) = (r19v1 'resultCount' int), (r19v3 'resultCount' int) binds: [B:29:0x01d5, B:82:?] A[DONT_GENERATE, DONT_INLINE]
      0x01d8: PHI (r28v7 int) = (r28v6 int), (r28v8 int) binds: [B:29:0x01d5, B:82:?] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:60:0x0391 -> B:49:0x02d6). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object limitConcurrent(int concurrentCount, int startIndex, int endIndex, @NotNull Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ? extends Object> handler, @NotNull Function2<? super ArrayList<Object>, ? super Integer, Boolean> needContinue, @NotNull Continuation<? super Unit> $completion) {
        AnonymousClass3 anonymousClass3;
        int size;
        int i;
        int loopCount;
        ArrayList resultList;
        ArrayList deferredList;
        Object objAwait;
        int coroutineCount;
        long loopStart;
        int resultCount;
        int lastIndex;
        int size2;
        if ($completion instanceof AnonymousClass3) {
            anonymousClass3 = (AnonymousClass3) $completion;
            if ((anonymousClass3.label & Integer.MIN_VALUE) != 0) {
                anonymousClass3.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass3 = new AnonymousClass3($completion);
            }
        }
        Object $result = anonymousClass3.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass3.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                lastIndex = startIndex;
                loopCount = 0;
                resultCount = 0;
                loopStart = System.currentTimeMillis();
                deferredList = new ArrayList();
                coroutineCount = deferredList.size();
                if (coroutineCount < concurrentCount) {
                    int i2 = lastIndex;
                    if (i2 < endIndex) {
                        do {
                            int i3 = i2;
                            i2++;
                            coroutineCount++;
                            deferredList.add(BuildersKt.async$default(this, (CoroutineContext) null, (CoroutineStart) null, new AnonymousClass4(handler, i3, null), 3, (Object) null));
                            lastIndex = i3;
                            if (coroutineCount >= concurrentCount) {
                            }
                        } while (i2 < endIndex);
                    }
                }
                resultList = new ArrayList();
                if (resultList.size() <= 0) {
                    if (resultCount / concurrentCount > loopCount) {
                        loopCount = resultCount / concurrentCount;
                        long costTime = System.currentTimeMillis() - loopStart;
                        BaseControllerKt.logger.info("Loop: {} concurrentCount: {} lastIndex: {} endIndex: {} costTime: {} ms deferredList size: {}", new Object[]{Boxing.boxInt(loopCount), Boxing.boxInt(coroutineCount), Boxing.boxInt(lastIndex), Boxing.boxInt(endIndex), Boxing.boxLong(costTime), Boxing.boxInt(deferredList.size())});
                    }
                    if (lastIndex >= endIndex - 1) {
                        i = 0;
                        size = deferredList.size();
                    } else {
                        if (resultList.size() <= 0 || ((Boolean) needContinue.invoke(resultList, Boxing.boxInt(loopCount))).booleanValue()) {
                            lastIndex++;
                            coroutineCount = deferredList.size();
                            if (coroutineCount < concurrentCount) {
                            }
                            resultList = new ArrayList();
                            if (resultList.size() <= 0) {
                                anonymousClass3.L$0 = this;
                                anonymousClass3.L$1 = handler;
                                anonymousClass3.L$2 = needContinue;
                                anonymousClass3.L$3 = deferredList;
                                anonymousClass3.L$4 = resultList;
                                anonymousClass3.I$0 = concurrentCount;
                                anonymousClass3.I$1 = endIndex;
                                anonymousClass3.I$2 = lastIndex;
                                anonymousClass3.I$3 = loopCount;
                                anonymousClass3.I$4 = resultCount;
                                anonymousClass3.J$0 = loopStart;
                                anonymousClass3.I$5 = coroutineCount;
                                anonymousClass3.label = 1;
                                if (DelayKt.delay(10L, anonymousClass3) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                ArrayList stillDeferredList = new ArrayList();
                                int i4 = 0;
                                size2 = deferredList.size();
                                if (0 >= size2) {
                                    do {
                                        int i5 = i4;
                                        i4++;
                                        Object obj = deferredList.get(i5);
                                        Intrinsics.checkNotNullExpressionValue(obj, "deferredList[i]");
                                        Deferred deferred = (Deferred) obj;
                                        if (deferred.isCompleted()) {
                                            resultCount++;
                                            resultList.add(deferred.getCompleted());
                                        } else if (!deferred.isCancelled()) {
                                            stillDeferredList.add(deferred);
                                        } else {
                                            resultCount++;
                                        }
                                    } while (i4 < size2);
                                    deferredList.clear();
                                    deferredList.addAll(stillDeferredList);
                                } else {
                                    deferredList.clear();
                                    deferredList.addAll(stillDeferredList);
                                }
                                if (resultList.size() <= 0) {
                                }
                            }
                        }
                        return Unit.INSTANCE;
                    }
                    break;
                }
                break;
            case 1:
                coroutineCount = anonymousClass3.I$5;
                loopStart = anonymousClass3.J$0;
                resultCount = anonymousClass3.I$4;
                loopCount = anonymousClass3.I$3;
                lastIndex = anonymousClass3.I$2;
                endIndex = anonymousClass3.I$1;
                concurrentCount = anonymousClass3.I$0;
                resultList = (ArrayList) anonymousClass3.L$4;
                deferredList = (ArrayList) anonymousClass3.L$3;
                needContinue = (Function2) anonymousClass3.L$2;
                handler = (Function3) anonymousClass3.L$1;
                this = (BaseController) anonymousClass3.L$0;
                ResultKt.throwOnFailure($result);
                ArrayList stillDeferredList2 = new ArrayList();
                int i42 = 0;
                size2 = deferredList.size();
                if (0 >= size2) {
                }
                if (resultList.size() <= 0) {
                }
                break;
            case 2:
                size = anonymousClass3.I$2;
                i = anonymousClass3.I$1;
                loopCount = anonymousClass3.I$0;
                ArrayList arrayList = (ArrayList) anonymousClass3.L$3;
                resultList = (ArrayList) anonymousClass3.L$2;
                deferredList = (ArrayList) anonymousClass3.L$1;
                needContinue = (Function2) anonymousClass3.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    objAwait = $result;
                } catch (Exception e) {
                }
                arrayList.add(objAwait);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BaseController$limitConcurrent$4, reason: invalid class name */
    /* JADX INFO: compiled from: BaseController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BaseController$limitConcurrent$4.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BaseController.kt", l = {276}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BaseController$limitConcurrent$4")
    static final class AnonymousClass4 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Object>, Object> {
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ Function3<CoroutineScope, Integer, Continuation<Object>, Object> $handler;
        final /* synthetic */ int $i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ? extends Object> $handler, int $i, Continuation<? super AnonymousClass4> $completion) {
            super(2, $completion);
            this.$handler = $handler;
            this.$i = $i;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass4 = new AnonymousClass4(this.$handler, this.$i, $completion);
            anonymousClass4.L$0 = value;
            return anonymousClass4;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<Object> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    CoroutineScope $this$async = (CoroutineScope) this.L$0;
                    Function3<CoroutineScope, Integer, Continuation<Object>, Object> function3 = this.$handler;
                    Integer numBoxInt = Boxing.boxInt(this.$i);
                    this.label = 1;
                    Object objInvoke = function3.invoke($this$async, numBoxInt, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objInvoke;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }
}
