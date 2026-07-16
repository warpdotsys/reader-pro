//
// Decompiled by Procyon v0.6.0
//

package com.htmake.reader.api.controller;

import java.util.Collection;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Deferred;
import java.util.ArrayList;
import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import io.legado.app.utils.FileUtils;
import java.io.File;
import kotlin.jvm.internal.SpreadBuilder;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.Nullable;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.Json;
import java.util.LinkedHashMap;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.Pair;
import kotlin.jvm.internal.TypeIntrinsics;
import com.htmake.reader.utils.ExtKt;
import kotlinx.coroutines.sync.Mutex$DefaultImpls;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import java.util.Map;
import kotlin.coroutines.Continuation;
import com.htmake.reader.entity.User;
import io.vertx.ext.web.RoutingContext;
import kotlinx.coroutines.sync.MutexKt;
import com.htmake.reader.utils.SpringContextUtils;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.sync.Mutex;
import org.springframework.core.env.Environment;
import com.htmake.reader.config.AppConfig;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.CoroutineContext;
import kotlin.Metadata;
import kotlinx.coroutines.CoroutineScope;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0019\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0019J\u000e\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u001a\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0\u001c2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0018\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\u001d2\b\b\u0002\u0010\"\u001a\u00020\u001dJ\u0010\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020\u001dJ\u001c\u0010&\u001a\u0010\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e\u0018\u00010'2\u0006\u0010%\u001a\u00020\u001dJ\u000e\u0010(\u001a\u00020\u001d2\u0006\u0010\u0017\u001a\u00020\u0018J)\u0010)\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0017\u001a\u00020\u001e2\u0012\u0010*\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001d0+\"\u00020\u001d?\u0006\u0002\u0010,J\u000e\u0010-\u001a\u00020\u001d2\u0006\u0010\u0017\u001a\u00020\u001eJX\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u00102\u0006\u00101\u001a\u00020\u00102\u0006\u00102\u001a\u00020\u00102-\u00103\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e05\u0012\u0006\u0012\u0004\u0018\u00010\u001e04?\u0006\u0002\b6H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u00107J\u0082\u0001\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020\u00102\u0006\u00101\u001a\u00020\u00102\u0006\u00102\u001a\u00020\u00102-\u00103\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e05\u0012\u0006\u0012\u0004\u0018\u00010\u001e04?\u0006\u0002\b62(\u00108\u001a$\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u001e0:j\b\u0012\u0004\u0012\u00020\u001e`;\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u001609H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010<J7\u0010=\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0'2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010>\u001a\u00020$2\b\b\u0002\u0010?\u001a\u00020\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010@J\u001e\u0010A\u001a\u00020/2\u0006\u0010\u0017\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u001d2\u0006\u0010B\u001a\u00020\u001eR\u0011\u0010\u0005\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004?\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f?\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e?\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0012?\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u0082\u0002\u0004\n\u0002\b\u0019��\u0006C" }, d2 = { "Lcom/htmake/reader/api/controller/BaseController;", "Lkotlinx/coroutines/CoroutineScope;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "appConfig", "Lcom/htmake/reader/config/AppConfig;", "getAppConfig", "()Lcom/htmake/reader/config/AppConfig;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "env", "Lorg/springframework/core/env/Environment;", "getEnv", "()Lorg/springframework/core/env/Environment;", "loginExpireDays", "", "userMutex", "Lkotlinx/coroutines/sync/Mutex;", "getUserMutex", "()Lkotlinx/coroutines/sync/Mutex;", "checkAuth", "", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "checkManagerAuth", "formatUser", "", "", "", "userInfo", "getFileExt", "url", "defaultExt", "getUserInfoClass", "Lcom/htmake/reader/entity/User;", "username", "getUserInfoMap", "", "getUserNameSpace", "getUserStorage", "path", "", "(Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/String;", "getUserWebdavHome", "limitConcurrent", "", "concurrentCount", "startIndex", "endIndex", "handler", "Lkotlin/Function3;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(IIILkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "needContinue", "Lkotlin/Function2;", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "(IIILkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveUserSession", "user", "regenerateToken", "(Lio/vertx/ext/web/RoutingContext;Lcom/htmake/reader/entity/User;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveUserStorage", "value", "reader-pro" })
public class BaseController implements CoroutineScope
{
    @NotNull
    private final CoroutineContext coroutineContext;
    private int loginExpireDays;
    @NotNull
    private final AppConfig appConfig;
    @NotNull
    private final Environment env;
    @NotNull
    private final Mutex userMutex;

    public BaseController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        this.coroutineContext = coroutineContext;
        this.loginExpireDays = 7;
        final AppConfig bean = SpringContextUtils.getBean("appConfig", AppConfig.class);
        Intrinsics.checkNotNullExpressionValue((Object)bean, "getBean(\"appConfig\", AppConfig::class.java)");
        this.appConfig = bean;
        final Environment bean2 = SpringContextUtils.getBean(Environment.class);
        Intrinsics.checkNotNullExpressionValue((Object)bean2, "getBean(Environment::class.java)");
        this.env = bean2;
        this.userMutex = MutexKt.Mutex$default(false, 1, (Object)null);
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

    @Nullable
    public final Object saveUserSession(@NotNull RoutingContext context, @NotNull User user, boolean regenerateToken, @NotNull final Continuation<? super Map<String, ?>> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BaseController$saveUserSession.BaseController$saveUserSession$1) {
                final BaseController$saveUserSession.BaseController$saveUserSession$1 baseController$saveUserSession$1 = (BaseController$saveUserSession.BaseController$saveUserSession$1)$completion;
                if ((baseController$saveUserSession$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BaseController$saveUserSession.BaseController$saveUserSession$1 baseController$saveUserSession$2 = baseController$saveUserSession$1;
                    baseController$saveUserSession$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BaseController$saveUserSession.BaseController$saveUserSession$1(this, (Continuation)$completion);
        }
        final Object $result = ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        while (true) {
            switch (((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    try {
                        final Mutex userMutex = this.getUserMutex();
                        final Object o = null;
                        final Continuation continuation = $continuation;
                        final int n = 1;
                        final Object o2 = null;
                        ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).L$0 = this;
                        ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).L$1 = context;
                        ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).L$2 = user;
                        ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).Z$0 = regenerateToken;
                        ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).label = 1;
                        if (Mutex$DefaultImpls.lock$default(userMutex, o, continuation, n, o2) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        while (true) {
                            user.setLast_login_at(System.currentTimeMillis());
                            if (regenerateToken) {
                                user.setToken(ExtKt.genEncryptedPassword(user.getUsername(), String.valueOf(System.currentTimeMillis())));
                                Map tokenMap = null;
                                final long expire = System.currentTimeMillis() + this.loginExpireDays * 86400 * 1000;
                                if (user.getToken_map() != null) {
                                    final Map<String, Long> token_map = user.getToken_map();
                                    tokenMap = (TypeIntrinsics.isMutableMap((Object)token_map) ? token_map : null);
                                }
                                if (tokenMap == null) {
                                    tokenMap = MapsKt.mutableMapOf(new Pair[] { TuplesKt.to((Object)user.getToken(), (Object)Boxing.boxLong(expire)) });
                                }
                                else {
                                    tokenMap.put(user.getToken(), Boxing.boxLong(expire));
                                }
                                CollectionsKt.removeAll((Iterable)tokenMap.values(), (Function1)new BaseController$saveUserSession.BaseController$saveUserSession$2(user));
                                user.setToken_map(tokenMap);
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
                            userMap.put(user.getUsername(), ExtKt.toMap(user));
                            final String[] array = { "data", "users" };
                            final String encode = Json.encode((Object)userMap);
                            Intrinsics.checkNotNullExpressionValue((Object)encode, "encode(userMap)");
                            ExtKt.saveStorage$default(array, encode, false, null, 12, null);
                            final Map loginData = this.formatUser(user);
                            context.session().put("username", (Object)user.getUsername());
                            context.put("username", (Object)user.getUsername());
                            return loginData;
                            regenerateToken = ((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).Z$0;
                            user = (User)((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).L$2;
                            context = (RoutingContext)((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).L$1;
                            this = (BaseController)((BaseController$saveUserSession.BaseController$saveUserSession$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            continue;
                        }
                    }
                    finally {
                        Mutex$DefaultImpls.unlock$default(this.getUserMutex(), (Object)null, 1, (Object)null);
                    }
                    break;
                }
                case 1: {
                    continue;
                }
            }
            break;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }

    @Nullable
    public final Object checkAuth(@NotNull RoutingContext context, @NotNull final Continuation<? super Boolean> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BaseController$checkAuth.BaseController$checkAuth$1) {
                final BaseController$checkAuth.BaseController$checkAuth$1 baseController$checkAuth$1 = (BaseController$checkAuth.BaseController$checkAuth$1)$completion;
                if ((baseController$checkAuth$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BaseController$checkAuth.BaseController$checkAuth$1 baseController$checkAuth$2 = baseController$checkAuth$1;
                    baseController$checkAuth$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BaseController$checkAuth.BaseController$checkAuth$1(this, (Continuation)$completion);
        }
        final Object $result = ((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final int i$0;
        final User user3;
        switch (((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                if (!this.getAppConfig().getSecure()) {
                    return Boxing.boxBoolean(true);
                }
                final String s = (String)context.session().get("username");
                final String username = (s == null) ? "" : s;
                final User userInfo = this.getUserInfoClass(username);
                if (userInfo != null) {
                    context.put("username", (Object)userInfo.getUsername());
                    context.put("userInfo", (Object)userInfo);
                    return Boxing.boxBoolean(true);
                }
                final List queryParam = context.queryParam("accessToken");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"accessToken\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
                final String accessToken = (s2 == null) ? "" : s2;
                if (accessToken.length() > 0) {
                    Map userMap = new LinkedHashMap();
                    final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
                    if (userMapJson != null) {
                        final Map map = userMapJson.getMap();
                        final Map map2 = TypeIntrinsics.isMutableMap((Object)map) ? map : null;
                        userMap = ((map2 == null) ? new LinkedHashMap() : map2);
                    }
                    final List tmp = StringsKt.split$default((CharSequence)accessToken, new String[] { ":" }, false, 2, 2, (Object)null);
                    if (tmp.size() >= 2) {
                        final String _username = tmp.get(0);
                        final String token = tmp.get(1);
                        final Map map3 = userMap.getOrDefault(_username, null);
                        User user;
                        if (map3 == null) {
                            user = null;
                        }
                        else {
                            final Map $this$toDataClass$iv = map3;
                            final int $i$f$toDataClass = 0;
                            final Object $this$convert$iv$iv = $this$toDataClass$iv;
                            final int $i$f$convert = 0;
                            final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                            user = (User)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() {}.getType());
                        }
                        final User existedUser = user;
                        if (existedUser != null && token.length() > 0) {
                            boolean isLogin = false;
                            if (existedUser.getToken().length() > 0 && Intrinsics.areEqual((Object)existedUser.getToken(), (Object)token)) {
                                isLogin = true;
                            }
                            if (!isLogin && existedUser.getToken_map() != null) {
                                final Map<String, Long> token_map = existedUser.getToken_map();
                                final Map tokenMap = TypeIntrinsics.isMutableMap((Object)token_map) ? token_map : null;
                                if (tokenMap != null && tokenMap.containsKey(token)) {
                                    if (tokenMap.getOrDefault(token, Boxing.boxLong(0L)).longValue() > System.currentTimeMillis()) {
                                        isLogin = true;
                                        tokenMap.put(token, Boxing.boxLong(System.currentTimeMillis() + this.loginExpireDays * 86400 * 1000));
                                    }
                                    else {
                                        tokenMap.remove(token);
                                    }
                                    existedUser.setToken_map(tokenMap);
                                }
                            }
                            if (!isLogin) {
                                return Boxing.boxBoolean(i$0 != 0);
                            }
                            final User user2 = existedUser;
                            final boolean regenerateToken = false;
                            final Continuation $completion2 = $continuation;
                            ((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).L$0 = context;
                            ((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).L$1 = existedUser;
                            ((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).I$0 = (isLogin ? 1 : 0);
                            ((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).label = 1;
                            if (this.saveUserSession(context, user2, regenerateToken, (Continuation<? super Map<String, ?>>)$completion2) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            break;
                        }
                    }
                }
                return Boxing.boxBoolean(false);
            }
            case 1: {
                i$0 = ((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).I$0;
                user3 = (User)((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).L$1;
                context = (RoutingContext)((BaseController$checkAuth.BaseController$checkAuth$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        context.put("username", (Object)user3.getUsername());
        context.put("userInfo", (Object)user3);
        return Boxing.boxBoolean(i$0 != 0);
    }

    public final boolean checkManagerAuth(@NotNull final RoutingContext context) {
        Intrinsics.checkNotNullParameter((Object)context, "context");
        if (!this.appConfig.getSecure()) {
            return true;
        }
        if (this.appConfig.getSecureKey().length() == 0) {
            return true;
        }
        final List queryParam = context.queryParam("secureKey");
        Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"secureKey\")");
        final String s = (String)CollectionsKt.firstOrNull(queryParam);
        final String secureKey = (s == null) ? "" : s;
        if (Intrinsics.areEqual((Object)secureKey, (Object)this.appConfig.getSecureKey())) {
            final List queryParam2 = context.queryParam("userNS");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"userNS\")");
            final String userNS = (String)CollectionsKt.firstOrNull(queryParam2);
            final CharSequence charSequence = userNS;
            if (charSequence != null && charSequence.length() != 0) {
                context.put("userNameSpace", (Object)userNS);
            }
            else {
                context.remove("userNameSpace");
            }
            return true;
        }
        return false;
    }

    @NotNull
    public final String getUserNameSpace(@NotNull final RoutingContext context) {
        Intrinsics.checkNotNullParameter((Object)context, "context");
        if (!this.appConfig.getSecure()) {
            return "default";
        }
        this.checkManagerAuth(context);
        final String userNS = (String)context.get("userNameSpace");
        final CharSequence charSequence = userNS;
        if (charSequence != null && charSequence.length() != 0) {
            return userNS;
        }
        final String username = (String)context.get("username");
        if (username != null) {
            return username;
        }
        return "default";
    }

    @Nullable
    public final String getUserStorage(@NotNull final Object context, @NotNull final String... path) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter((Object)path, "path");
        String userNameSpace = "";
        if (context instanceof RoutingContext) {
            userNameSpace = this.getUserNameSpace((RoutingContext)context);
        }
        else if (context instanceof String) {
            userNameSpace = (String)context;
        }
        if (userNameSpace.length() == 0) {
            final SpreadBuilder spreadBuilder = new SpreadBuilder(2);
            spreadBuilder.add((Object)"data");
            spreadBuilder.addSpread((Object)path);
            return ExtKt.getStorage$default((String[])spreadBuilder.toArray((Object[])new String[spreadBuilder.size()]), null, 2, null);
        }
        final SpreadBuilder spreadBuilder2 = new SpreadBuilder(3);
        spreadBuilder2.add((Object)"data");
        spreadBuilder2.add((Object)userNameSpace);
        spreadBuilder2.addSpread((Object)path);
        return ExtKt.getStorage$default((String[])spreadBuilder2.toArray((Object[])new String[spreadBuilder2.size()]), null, 2, null);
    }

    public final void saveUserStorage(@NotNull final Object context, @NotNull final String path, @NotNull final Object value) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter((Object)path, "path");
        Intrinsics.checkNotNullParameter(value, "value");
        String userNameSpace = "";
        if (context instanceof RoutingContext) {
            userNameSpace = this.getUserNameSpace((RoutingContext)context);
        }
        else if (context instanceof String) {
            userNameSpace = (String)context;
        }
        if (userNameSpace.length() == 0) {
            ExtKt.saveStorage$default(new String[] { "data", path }, value, false, null, 12, null);
            return;
        }
        ExtKt.saveStorage$default(new String[] { "data", userNameSpace, path }, value, false, null, 12, null);
    }

    @Nullable
    public final User getUserInfoClass(@NotNull final String username) {
        Intrinsics.checkNotNullParameter((Object)username, "username");
        final Map<String, Object> userInfoMap = this.getUserInfoMap(username);
        User user2;
        if (userInfoMap == null) {
            user2 = null;
        }
        else {
            final Map $this$toDataClass$iv = userInfoMap;
            final int $i$f$toDataClass = 0;
            final Object $this$convert$iv$iv = $this$toDataClass$iv;
            final int $i$f$convert = 0;
            final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
            user2 = (User)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() {}.getType());
        }
        final User user = user2;
        return user;
    }

    @Nullable
    public final Map<String, Object> getUserInfoMap(@NotNull final String username) {
        Intrinsics.checkNotNullParameter((Object)username, "username");
        if (username.length() == 0) {
            return null;
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
        final Map<String, Object> defaultValue = null;
        final Map map3 = map2;
        if (map3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        return map3.getOrDefault(username, defaultValue);
    }

    @NotNull
    public final Map<String, Object> formatUser(@NotNull final Object userInfo) {
        Intrinsics.checkNotNullParameter(userInfo, "userInfo");
        User user = null;
        if (!(userInfo instanceof User)) {
            final Map userMap = (userInfo instanceof Map) ? ((Map)userInfo) : null;
            if (userMap != null) {
                final Map $this$toDataClass$iv = userMap;
                final int $i$f$toDataClass = 0;
                final Object $this$convert$iv$iv = $this$toDataClass$iv;
                final int $i$f$convert = 0;
                final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                user = (User)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<User>() {}.getType());
            }
        }
        else {
            user = (User)userInfo;
        }
        if (user == null) {
            return new LinkedHashMap<String, Object>();
        }
        return MapsKt.mutableMapOf(new Pair[] { TuplesKt.to((Object)"username", (Object)user.getUsername()), TuplesKt.to((Object)"lastLoginAt", (Object)user.getLast_login_at()), TuplesKt.to((Object)"accessToken", (Object)(user.getUsername() + ':' + user.getToken())), TuplesKt.to((Object)"enableWebdav", (Object)user.getEnable_webdav()), TuplesKt.to((Object)"enableLocalStore", (Object)user.getEnable_local_store()), TuplesKt.to((Object)"enableBookSource", (Object)user.getEnable_book_source()), TuplesKt.to((Object)"enableRssSource", (Object)user.getEnable_rss_source()), TuplesKt.to((Object)"bookSourceLimit", (Object)user.getBook_source_limit()), TuplesKt.to((Object)"bookLimit", (Object)user.getBook_limit()), TuplesKt.to((Object)"createdAt", (Object)user.getCreated_at()) });
    }

    @NotNull
    public final String getUserWebdavHome(@NotNull final Object context) {
        Intrinsics.checkNotNullParameter(context, "context");
        String prefix = ExtKt.getWorkDir("storage", "data");
        String userNameSpace = "";
        if (context instanceof RoutingContext) {
            userNameSpace = this.getUserNameSpace((RoutingContext)context);
        }
        else if (context instanceof String) {
            userNameSpace = (String)context;
        }
        if (userNameSpace.length() > 0) {
            prefix = prefix + (Object)File.separator + userNameSpace;
        }
        prefix = prefix + (Object)File.separator + "webdav";
        final File file = new File(prefix);
        if (!file.exists()) {
            file.mkdirs();
        }
        return prefix;
    }

    @NotNull
    public final String getFileExt(@NotNull final String url, @NotNull final String defaultExt) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)defaultExt, "defaultExt");
        return FileUtils.INSTANCE.getFileExtetion(url, defaultExt);
    }

    @Nullable
    public final Object limitConcurrent(final int concurrentCount, final int startIndex, final int endIndex, @NotNull final Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?> handler, @NotNull final Continuation<? super Unit> $completion) {
        final Object limitConcurrent = this.limitConcurrent(concurrentCount, startIndex, endIndex, handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)BaseController$limitConcurrent.BaseController$limitConcurrent$2.INSTANCE, $completion);
        if (limitConcurrent == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return limitConcurrent;
        }
        return Unit.INSTANCE;
    }

    @Nullable
    public final Object limitConcurrent(int concurrentCount, final int startIndex, int endIndex, @NotNull final Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?> handler, @NotNull Function2<? super ArrayList<Object>, ? super Integer, Boolean> needContinue, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BaseController$limitConcurrent.BaseController$limitConcurrent$3) {
                final BaseController$limitConcurrent.BaseController$limitConcurrent$3 baseController$limitConcurrent$3 = (BaseController$limitConcurrent.BaseController$limitConcurrent$3)$completion;
                if ((baseController$limitConcurrent$3.label & Integer.MIN_VALUE) != 0x0) {
                    final BaseController$limitConcurrent.BaseController$limitConcurrent$3 baseController$limitConcurrent$4 = baseController$limitConcurrent$3;
                    baseController$limitConcurrent$4.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BaseController$limitConcurrent.BaseController$limitConcurrent$3(this, (Continuation)$completion);
        }
        final Object $result = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i$7 = 0;
        ArrayList l$1 = null;
        ArrayList list = null;
        Label_0916: {
            while (true) {
                int i$6 = 0;
                ArrayList resultList = null;
            Label_0130_Outer:
                while (true) {
                    int lastIndex = 0;
                    final int loopCount;
                    final int resultCount;
                    final long loopStart;
                    final ArrayList deferredList;
                    final int i$5;
                    final long j$0;
                    int i$8 = 0;
                    switch (((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            lastIndex = startIndex;
                            loopCount = 0;
                            resultCount = 0;
                            loopStart = System.currentTimeMillis();
                            deferredList = new ArrayList();
                            break;
                        }
                        case 1: {
                            i$5 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$5;
                            j$0 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).J$0;
                            i$6 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$4;
                            i$7 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$3;
                            i$8 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$2;
                            endIndex = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$1;
                            concurrentCount = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$0;
                            resultList = (ArrayList)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$4;
                            l$1 = (ArrayList)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$3;
                            var_5_19E = (Function2)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$2;
                            final Function3 function3 = (Function3)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$1;
                            this = (BaseController)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            break Label_0130_Outer;
                        }
                        case 2: {
                            Label_0825: {
                                break Label_0825;
                                int l = 0;
                                int i$9 = 0;
                            Label_0894_Outer:
                                do {
                                    final int i = l;
                                    ++l;
                                    try {
                                        Object l$2 = resultList;
                                        final Deferred deferred2 = l$1.get(i);
                                        final Continuation continuation = $continuation;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$0 = var_5_19E;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$1 = l$1;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$2 = resultList;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$3 = l$2;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$4 = null;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$0 = i$7;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$1 = l;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$2 = i$9;
                                        ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).label = 2;
                                        Object await;
                                        if ((await = deferred2.await(continuation)) == coroutine_SUSPENDED) {
                                            return coroutine_SUSPENDED;
                                        }
                                        while (true) {
                                            ((ArrayList<Object>)l$2).add(await);
                                            continue Label_0894_Outer;
                                            i$9 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$2;
                                            l = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$1;
                                            i$7 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$0;
                                            l$2 = ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$3;
                                            list = (ArrayList)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$2;
                                            l$1 = (ArrayList)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$1;
                                            var_5_19E = (Function2)((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$0;
                                            ResultKt.throwOnFailure($result);
                                            await = $result;
                                            continue Label_0130_Outer;
                                        }
                                    }
                                    catch (final Exception ex) {}
                                } while (l < i$9);
                            }
                            break Label_0916;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    while (true) {
                        int coroutineCount = deferredList.size();
                        if (coroutineCount < concurrentCount) {
                            int n = lastIndex;
                            if (n < endIndex) {
                                do {
                                    final int j = n;
                                    ++n;
                                    ++coroutineCount;
                                    deferredList.add(BuildersKt.async$default((CoroutineScope)this, (CoroutineContext)null, (CoroutineStart)null, (Function2)new BaseController$limitConcurrent.BaseController$limitConcurrent$4((Function3)handler, j, (Continuation)null), 3, (Object)null));
                                    lastIndex = j;
                                    if (coroutineCount >= concurrentCount) {
                                        break;
                                    }
                                } while (n < endIndex);
                            }
                        }
                        resultList = new ArrayList();
                        if (resultList.size() <= 0) {
                            final long n2 = 10L;
                            final Continuation continuation2 = $continuation;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$0 = this;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$1 = handler;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$2 = needContinue;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$3 = deferredList;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).L$4 = resultList;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$0 = concurrentCount;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$1 = endIndex;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$2 = lastIndex;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$3 = loopCount;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$4 = resultCount;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).J$0 = loopStart;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).I$5 = coroutineCount;
                            ((BaseController$limitConcurrent.BaseController$limitConcurrent$3)$continuation).label = 1;
                            if (DelayKt.delay(n2, continuation2) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                        }
                        else {
                            if (i$6 / concurrentCount > i$7) {
                                i$7 = i$6 / concurrentCount;
                                final long costTime = System.currentTimeMillis() - j$0;
                                BaseControllerKt.access$getLogger$p().info("Loop: {} concurrentCount: {} lastIndex: {} endIndex: {} costTime: {} ms deferredList size: {}", new Object[] { Boxing.boxInt(i$7), Boxing.boxInt(i$5), Boxing.boxInt(i$8), Boxing.boxInt(endIndex), Boxing.boxLong(costTime), Boxing.boxInt(l$1.size()) });
                            }
                            if (i$8 >= endIndex - 1) {
                                final int l = 0;
                                final int i$9 = l$1.size();
                                if (l < i$9) {
                                    continue Label_0130_Outer;
                                }
                                break Label_0916;
                            }
                            else {
                                if (list.size() > 0 && !(boolean)var_5_19E.invoke((Object)list, (Object)Boxing.boxInt(i$7))) {
                                    return Unit.INSTANCE;
                                }
                                ++i$8;
                                continue;
                            }
                        }
                        break;
                    }
                    break;
                }
                final ArrayList stillDeferredList = new ArrayList();
                int n3 = 0;
                final int size = l$1.size();
                if (n3 < size) {
                    do {
                        final int k = n3;
                        ++n3;
                        try {
                            final Object value = l$1.get(k);
                            Intrinsics.checkNotNullExpressionValue(value, "deferredList[i]");
                            final Deferred deferred = (Deferred)value;
                            if (deferred.isCompleted()) {
                                ++i$6;
                                resultList.add(deferred.getCompleted());
                            }
                            else if (!deferred.isCancelled()) {
                                stillDeferredList.add(deferred);
                            }
                            else {
                                ++i$6;
                            }
                        }
                        catch (final Exception ex2) {}
                    } while (n3 < size);
                }
                l$1.clear();
                l$1.addAll(stillDeferredList);
                continue;
            }
        }
        l$1.clear();
        var_5_19E.invoke((Object)list, (Object)Boxing.boxInt(i$7));
        return Unit.INSTANCE;
    }
}
