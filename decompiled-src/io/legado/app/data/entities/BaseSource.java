/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  com.script.Bindings
 *  com.script.SimpleBindings
 *  kotlin.Metadata
 *  kotlin.Result
 *  kotlin.Result$Companion
 *  kotlin.ResultKt
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.Charsets
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jsoup.Connection$Response
 */
package io.legado.app.data.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.script.Bindings;
import com.script.SimpleBindings;
import io.legado.app.constant.AppConst;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.help.CacheManager;
import io.legado.app.help.JsExtensions;
import io.legado.app.help.http.CookieStore;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.Base64;
import io.legado.app.utils.EncoderUtils;
import io.legado.app.utils.GsonExtensionsKt;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u000f\bf\u0018\u00002\u00020\u0001J-\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u00032\u0019\b\u0002\u0010\u001a\u001a\u0013\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d0\u001b\u00a2\u0006\u0002\b\u001eH\u0016J.\u0010\u001f\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030 j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`!2\b\b\u0002\u0010\"\u001a\u00020\tH\u0016J\b\u0010#\u001a\u00020\u0003H&J\n\u0010$\u001a\u0004\u0018\u00010\u0003H\u0016J\u0016\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010&H\u0016J\n\u0010'\u001a\u0004\u0018\u00010\u0003H\u0016J\u0016\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010&H\u0016J\n\u0010)\u001a\u0004\u0018\u00010\u0003H\u0016J\n\u0010*\u001a\u0004\u0018\u00010\u0000H\u0016J\b\u0010+\u001a\u00020\u0003H&J\n\u0010,\u001a\u0004\u0018\u00010\u0003H\u0016J\b\u0010-\u001a\u00020\u001dH\u0016J\u0010\u0010.\u001a\u00020\u001d2\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\u0010\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\u0003H\u0016J\b\u00101\u001a\u00020\u001dH\u0016J\b\u00102\u001a\u00020\u001dH\u0016J\u0012\u00103\u001a\u00020\u001d2\b\u00104\u001a\u0004\u0018\u00010\u0003H\u0016R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u0004\u0018\u00010\tX\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0012\u0010\u0005\"\u0004\b\u0013\u0010\u0007R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0015\u0010\u0005\"\u0004\b\u0016\u0010\u0007\u00a8\u00065"}, d2={"Lio/legado/app/data/entities/BaseSource;", "Lio/legado/app/help/JsExtensions;", "concurrentRate", "", "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "enabledCookieJar", "", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "header", "getHeader", "setHeader", "loginUi", "getLoginUi", "setLoginUi", "loginUrl", "getLoginUrl", "setLoginUrl", "evalJS", "", "jsStr", "bindingsConfig", "Lkotlin/Function1;", "Lcom/script/SimpleBindings;", "", "Lkotlin/ExtensionFunctionType;", "getHeaderMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "hasLoginHeader", "getKey", "getLoginHeader", "getLoginHeaderMap", "", "getLoginInfo", "getLoginInfoMap", "getLoginJs", "getSource", "getTag", "getVariable", "login", "putLoginHeader", "putLoginInfo", "info", "removeLoginHeader", "removeLoginInfo", "setVariable", "variable", "reader-pro"})
public interface BaseSource
extends JsExtensions {
    @Nullable
    public String getConcurrentRate();

    public void setConcurrentRate(@Nullable String var1);

    @Nullable
    public String getLoginUrl();

    public void setLoginUrl(@Nullable String var1);

    @Nullable
    public String getLoginUi();

    public void setLoginUi(@Nullable String var1);

    @Nullable
    public String getHeader();

    public void setHeader(@Nullable String var1);

    @Nullable
    public Boolean getEnabledCookieJar();

    public void setEnabledCookieJar(@Nullable Boolean var1);

    @NotNull
    public String getTag();

    @NotNull
    public String getKey();

    @Override
    @Nullable
    public BaseSource getSource();

    @Nullable
    public String getLoginJs();

    public void login();

    @NotNull
    public HashMap<String, String> getHeaderMap(boolean var1);

    @Nullable
    public String getLoginHeader();

    @Nullable
    public Map<String, String> getLoginHeaderMap();

    public void putLoginHeader(@NotNull String var1);

    public void removeLoginHeader();

    @Nullable
    public String getLoginInfo();

    @Nullable
    public Map<String, String> getLoginInfoMap();

    public boolean putLoginInfo(@NotNull String var1);

    public void removeLoginInfo();

    public void setVariable(@Nullable String var1);

    @Nullable
    public String getVariable();

    @Nullable
    public Object evalJS(@NotNull String var1, @NotNull Function1<? super SimpleBindings, Unit> var2) throws Exception;

    @Metadata(mv={1, 5, 1}, k=3, xi=48)
    public static final class DefaultImpls {
        @Nullable
        public static BaseSource getSource(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return this_;
        }

        @Nullable
        public static String getLoginJs(@NotNull BaseSource this_) {
            String string;
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            String loginJs = this_.getLoginUrl();
            if (loginJs == null) {
                string = null;
            } else if (StringsKt.startsWith$default((String)loginJs, (String)"@js:", (boolean)false, (int)2, null)) {
                String string2 = loginJs;
                int n = 4;
                boolean bl = false;
                String string3 = string2.substring(n);
                string = string3;
                Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"(this as java.lang.String).substring(startIndex)");
            } else if (StringsKt.startsWith$default((String)loginJs, (String)"<js>", (boolean)false, (int)2, null)) {
                String string4 = loginJs;
                int n = 4;
                int n2 = StringsKt.lastIndexOf$default((CharSequence)loginJs, (String)"<", (int)0, (boolean)false, (int)6, null);
                boolean bl = false;
                String string5 = string4.substring(n, n2);
                string = string5;
                Intrinsics.checkNotNullExpressionValue((Object)string5, (String)"(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            } else {
                string = loginJs;
            }
            return string;
        }

        public static void login(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            String string = this_.getLoginJs();
            if (string != null) {
                String string2 = string;
                boolean bl = false;
                boolean bl2 = false;
                String it = string2;
                boolean bl3 = false;
                DefaultImpls.evalJS$default(this_, it, null, 2, null);
            }
        }

        /*
         * WARNING - void declaration
         */
        @NotNull
        public static HashMap<String, String> getHeaderMap(@NotNull BaseSource this_, boolean hasLoginHeader) {
            Map<String, String> it;
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            HashMap<String, String> hashMap = new HashMap<String, String>();
            boolean bl = false;
            boolean bl2 = false;
            HashMap<String, String> receiver = hashMap;
            boolean bl3 = false;
            Map<String, String> map = receiver;
            Map<String, String> map2 = "User-Agent";
            String string = AppConst.INSTANCE.getUserAgent();
            boolean bl4 = false;
            map.put((String)((Object)map2), string);
            map = this_.getHeader();
            if (map != null) {
                Object object;
                Object object2;
                int n;
                int n2;
                Map<String, String> map3;
                map2 = map;
                boolean bl5 = false;
                bl4 = false;
                it = map2;
                boolean bl6 = false;
                Object object3 = GsonExtensionsKt.getGSON();
                if (StringsKt.startsWith(it, (String)"@js:", (boolean)true)) {
                    map3 = it;
                    n2 = 4;
                    n = 0;
                    String string2 = ((String)((Object)map3)).substring(n2);
                    Intrinsics.checkNotNullExpressionValue((Object)string2, (String)"(this as java.lang.String).substring(startIndex)");
                    object2 = String.valueOf(DefaultImpls.evalJS$default(this_, string2, null, 2, null));
                } else if (StringsKt.startsWith(it, (String)"<js>", (boolean)true)) {
                    map3 = it;
                    n2 = 4;
                    n = StringsKt.lastIndexOf$default((CharSequence)((CharSequence)((Object)it)), (String)"<", (int)0, (boolean)false, (int)6, null);
                    boolean bl7 = false;
                    String string3 = ((String)((Object)map3)).substring(n2, n);
                    Intrinsics.checkNotNullExpressionValue((Object)string3, (String)"(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    object2 = String.valueOf(DefaultImpls.evalJS$default(this_, string3, null, 2, null));
                } else {
                    object2 = it;
                }
                map3 = object2;
                boolean $i$f$fromJsonObject = false;
                n = 0;
                try {
                    void json$iv;
                    void $this$fromJsonObject$iv;
                    object = Result.Companion;
                    boolean bl8 = false;
                    boolean $i$f$genericType = false;
                    Type type = new TypeToken<Map<String, ? extends String>>(){}.getType();
                    Intrinsics.checkNotNullExpressionValue((Object)type, (String)"object : TypeToken<T>() {}.type");
                    Object object4 = $this$fromJsonObject$iv.fromJson((String)json$iv, type);
                    if (!(object4 instanceof Map)) {
                        object4 = null;
                    }
                    Map map4 = (Map)object4;
                    boolean bl9 = false;
                    object = Result.constructor-impl((Object)map4);
                }
                catch (Throwable throwable) {
                    Result.Companion companion = Result.Companion;
                    boolean bl10 = false;
                    object = Result.constructor-impl((Object)ResultKt.createFailure((Throwable)throwable));
                }
                object3 = object;
                boolean bl11 = false;
                Map map5 = (Map)(Result.isFailure-impl((Object)object3) ? null : object3);
                if (map5 != null) {
                    object3 = map5;
                    bl11 = false;
                    n2 = 0;
                    Object map6 = object3;
                    boolean bl12 = false;
                    receiver.putAll((Map<String, String>)map6);
                }
            }
            if (hasLoginHeader && (map = this_.getLoginHeaderMap()) != null) {
                map2 = map;
                boolean bl13 = false;
                bl4 = false;
                it = map2;
                boolean bl14 = false;
                receiver.putAll(it);
            }
            return hashMap;
        }

        public static /* synthetic */ HashMap getHeaderMap$default(BaseSource baseSource, boolean bl, int n, Object object) {
            if (object != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getHeaderMap");
            }
            if ((n & 1) != 0) {
                bl = false;
            }
            return baseSource.getHeaderMap(bl);
        }

        @Nullable
        public static String getLoginHeader(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
            return cacheInstance.get(Intrinsics.stringPlus((String)"loginHeader_", (Object)this_.getKey()));
        }

        @Nullable
        public static Map<String, String> getLoginHeaderMap(@NotNull BaseSource this_) {
            Object object;
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            String string = this_.getLoginHeader();
            if (string == null) {
                return null;
            }
            String cache = string;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            boolean $i$f$fromJsonObject = false;
            boolean bl = false;
            try {
                object = Result.Companion;
                boolean bl2 = false;
                boolean $i$f$genericType = false;
                Type type = new TypeToken<Map<String, ? extends String>>(){}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, (String)"object : TypeToken<T>() {}.type");
                Object object2 = $this$fromJsonObject$iv.fromJson(cache, type);
                if (!(object2 instanceof Map)) {
                    object2 = null;
                }
                Map map = (Map)object2;
                boolean bl3 = false;
                object = Result.constructor-impl((Object)map);
            }
            catch (Throwable throwable) {
                Result.Companion companion = Result.Companion;
                boolean bl4 = false;
                object = Result.constructor-impl((Object)ResultKt.createFailure((Throwable)throwable));
            }
            string = object;
            boolean bl5 = false;
            return (Map)((Object)(Result.isFailure-impl((Object)string) ? null : string));
        }

        public static void putLoginHeader(@NotNull BaseSource this_, @NotNull String header) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)header, (String)"header");
            CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
            CacheManager.put$default(cacheInstance, Intrinsics.stringPlus((String)"loginHeader_", (Object)this_.getKey()), header, 0, 4, null);
        }

        public static void removeLoginHeader(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
            cacheInstance.delete(Intrinsics.stringPlus((String)"loginHeader_", (Object)this_.getKey()));
        }

        @Nullable
        public static String getLoginInfo(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            try {
                byte[] key = StringsKt.encodeToByteArray$default((String)AppConst.INSTANCE.getUserAgent(), (int)0, (int)8, (boolean)false, (int)4, null);
                CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
                String string = cacheInstance.get(Intrinsics.stringPlus((String)"userInfo_", (Object)this_.getKey()));
                if (string == null) {
                    return null;
                }
                String cache = string;
                String string2 = EncoderUtils.INSTANCE.base64Decode(cache, 0);
                Object object = Charsets.UTF_8;
                boolean bl = false;
                String string3 = string2;
                if (string3 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                byte[] byArray = string3.getBytes((Charset)object);
                Intrinsics.checkNotNullExpressionValue((Object)byArray, (String)"(this as java.lang.String).getBytes(charset)");
                byte[] encodeBytes = byArray;
                object = EncoderUtils.decryptAES$default(EncoderUtils.INSTANCE, encodeBytes, key, null, null, 12, null);
                if (object == null) {
                    return null;
                }
                Object decodeBytes = object;
                boolean bl2 = false;
                return new String((byte[])decodeBytes, Charsets.UTF_8);
            }
            catch (Exception e) {
                this_.log(Intrinsics.stringPlus((String)"\u83b7\u53d6\u767b\u9646\u4fe1\u606f\u51fa\u9519 ", (Object)e.getLocalizedMessage()));
                return null;
            }
        }

        /*
         * WARNING - void declaration
         */
        @Nullable
        public static Map<String, String> getLoginInfoMap(@NotNull BaseSource this_) {
            Object object;
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Gson gson2 = GsonExtensionsKt.getGSON();
            String json$iv = this_.getLoginInfo();
            boolean $i$f$fromJsonObject = false;
            boolean bl = false;
            try {
                void $this$fromJsonObject$iv;
                object = Result.Companion;
                boolean bl2 = false;
                boolean $i$f$genericType = false;
                Type type = new TypeToken<Map<String, ? extends String>>(){}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, (String)"object : TypeToken<T>() {}.type");
                Object object2 = $this$fromJsonObject$iv.fromJson(json$iv, type);
                if (!(object2 instanceof Map)) {
                    object2 = null;
                }
                Map map = (Map)object2;
                boolean bl3 = false;
                object = Result.constructor-impl((Object)map);
            }
            catch (Throwable throwable) {
                Result.Companion companion = Result.Companion;
                boolean bl4 = false;
                object = Result.constructor-impl((Object)ResultKt.createFailure((Throwable)throwable));
            }
            gson2 = object;
            boolean bl5 = false;
            return (Map)(Result.isFailure-impl((Object)gson2) ? null : gson2);
        }

        public static boolean putLoginInfo(@NotNull BaseSource this_, @NotNull String info) {
            boolean bl;
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)info, (String)"info");
            try {
                byte[] key = StringsKt.encodeToByteArray$default((String)AppConst.INSTANCE.getUserAgent(), (int)0, (int)8, (boolean)false, (int)4, null);
                String string = info;
                Charset charset = Charsets.UTF_8;
                boolean bl2 = false;
                byte[] byArray = string.getBytes(charset);
                Intrinsics.checkNotNullExpressionValue((Object)byArray, (String)"(this as java.lang.String).getBytes(charset)");
                byte[] encodeBytes = EncoderUtils.encryptAES$default(EncoderUtils.INSTANCE, byArray, key, null, null, 12, null);
                String encodeStr = Base64.encodeToString(encodeBytes, 0);
                CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
                String string2 = Intrinsics.stringPlus((String)"userInfo_", (Object)this_.getKey());
                Intrinsics.checkNotNullExpressionValue((Object)encodeStr, (String)"encodeStr");
                CacheManager.put$default(cacheInstance, string2, encodeStr, 0, 4, null);
                bl = true;
            }
            catch (Exception e) {
                this_.log(Intrinsics.stringPlus((String)"\u4fdd\u5b58\u767b\u9646\u4fe1\u606f\u51fa\u9519 ", (Object)e.getLocalizedMessage()));
                bl = false;
            }
            return bl;
        }

        public static void removeLoginInfo(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
            cacheInstance.delete(Intrinsics.stringPlus((String)"userInfo_", (Object)this_.getKey()));
        }

        public static void setVariable(@NotNull BaseSource this_, @Nullable String variable) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
            if (variable != null) {
                CacheManager.put$default(cacheInstance, Intrinsics.stringPlus((String)"sourceVariable_", (Object)this_.getKey()), variable, 0, 4, null);
            } else {
                cacheInstance.delete(Intrinsics.stringPlus((String)"sourceVariable_", (Object)this_.getKey()));
            }
        }

        @Nullable
        public static String getVariable(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            CacheManager cacheInstance = new CacheManager(this_.getUserNameSpace());
            return cacheInstance.get(Intrinsics.stringPlus((String)"sourceVariable_", (Object)this_.getKey()));
        }

        @Nullable
        public static Object evalJS(@NotNull BaseSource this_, @NotNull String jsStr, @NotNull Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)jsStr, (String)"jsStr");
            Intrinsics.checkNotNullParameter(bindingsConfig, (String)"bindingsConfig");
            SimpleBindings bindings = new SimpleBindings();
            Object object = bindings;
            boolean bl = false;
            boolean bl2 = false;
            bindingsConfig.invoke(object);
            object = (Map)bindings;
            String string = "java";
            bl2 = false;
            object.put(string, this_);
            object = (Map)bindings;
            string = "source";
            bl2 = false;
            object.put(string, this_);
            object = (Map)bindings;
            string = "baseUrl";
            Object object2 = this_.getKey();
            boolean bl3 = false;
            object.put(string, object2);
            object = (Map)bindings;
            string = "cookie";
            object2 = new CookieStore(this_.getUserNameSpace());
            bl3 = false;
            object.put(string, object2);
            object = (Map)bindings;
            string = "cache";
            object2 = new CacheManager(this_.getUserNameSpace());
            bl3 = false;
            object.put(string, object2);
            return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, (Bindings)bindings);
        }

        public static /* synthetic */ Object evalJS$default(BaseSource baseSource, String string, Function1 function1, int n, Object object) throws Exception {
            if (object != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: evalJS");
            }
            if ((n & 2) != 0) {
                function1 = evalJS.1.INSTANCE;
            }
            return baseSource.evalJS(string, (Function1<? super SimpleBindings, Unit>)function1);
        }

        @Nullable
        public static byte[] aesBase64DecodeToByteArray(@NotNull BaseSource this_, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesBase64DecodeToByteArray(this_, str, key, transformation, iv);
        }

        @Nullable
        public static String aesBase64DecodeToString(@NotNull BaseSource this_, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesBase64DecodeToString(this_, str, key, transformation, iv);
        }

        @Nullable
        public static String aesDecodeArgsBase64Str(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)mode, (String)"mode");
            Intrinsics.checkNotNullParameter((Object)padding, (String)"padding");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesDecodeArgsBase64Str(this_, data, key, mode, padding, iv);
        }

        @Nullable
        public static byte[] aesDecodeToByteArray(@NotNull BaseSource this_, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesDecodeToByteArray(this_, str, key, transformation, iv);
        }

        @Nullable
        public static String aesDecodeToString(@NotNull BaseSource this_, @NotNull String str, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesDecodeToString(this_, str, key, transformation, iv);
        }

        @Nullable
        public static String aesEncodeArgsBase64Str(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)mode, (String)"mode");
            Intrinsics.checkNotNullParameter((Object)padding, (String)"padding");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesEncodeArgsBase64Str(this_, data, key, mode, padding, iv);
        }

        @Nullable
        public static byte[] aesEncodeToBase64ByteArray(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesEncodeToBase64ByteArray(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String aesEncodeToBase64String(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesEncodeToBase64String(this_, data, key, transformation, iv);
        }

        @Nullable
        public static byte[] aesEncodeToByteArray(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesEncodeToByteArray(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String aesEncodeToString(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.aesEncodeToString(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String ajax(@NotNull BaseSource this_, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            return JsExtensions.DefaultImpls.ajax(this_, urlStr);
        }

        @NotNull
        public static StrResponse[] ajaxAll(@NotNull BaseSource this_, @NotNull String[] urlList) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlList, (String)"urlList");
            return JsExtensions.DefaultImpls.ajaxAll(this_, urlList);
        }

        @NotNull
        public static String androidId(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.androidId(this_);
        }

        @NotNull
        public static String base64Decode(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.base64Decode(this_, str);
        }

        @NotNull
        public static String base64Decode(@NotNull BaseSource this_, @NotNull String str, int flags) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.base64Decode(this_, str, flags);
        }

        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull BaseSource this_, @Nullable String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.base64DecodeToByteArray(this_, str);
        }

        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull BaseSource this_, @Nullable String str, int flags) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.base64DecodeToByteArray(this_, str, flags);
        }

        @Nullable
        public static String base64Encode(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.base64Encode(this_, str);
        }

        @Nullable
        public static String base64Encode(@NotNull BaseSource this_, @NotNull String str, int flags) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.base64Encode(this_, str, flags);
        }

        @Nullable
        public static String cacheFile(@NotNull BaseSource this_, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            return JsExtensions.DefaultImpls.cacheFile(this_, urlStr);
        }

        @Nullable
        public static String cacheFile(@NotNull BaseSource this_, @NotNull String urlStr, int saveTime) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            return JsExtensions.DefaultImpls.cacheFile(this_, urlStr, saveTime);
        }

        @NotNull
        public static StrResponse connect(@NotNull BaseSource this_, @NotNull String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            return JsExtensions.DefaultImpls.connect(this_, urlStr);
        }

        @NotNull
        public static StrResponse connect(@NotNull BaseSource this_, @NotNull String urlStr, @Nullable String header) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            return JsExtensions.DefaultImpls.connect(this_, urlStr, header);
        }

        public static void deleteFile(@NotNull BaseSource this_, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            JsExtensions.DefaultImpls.deleteFile(this_, path);
        }

        @Nullable
        public static String desBase64DecodeToString(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.desBase64DecodeToString(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String desDecodeToString(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.desDecodeToString(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String desEncodeToBase64String(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.desEncodeToBase64String(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String desEncodeToString(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String transformation, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)transformation, (String)"transformation");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.desEncodeToString(this_, data, key, transformation, iv);
        }

        @Nullable
        public static String digestBase64Str(@NotNull BaseSource this_, @NotNull String data, @NotNull String algorithm) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)algorithm, (String)"algorithm");
            return JsExtensions.DefaultImpls.digestBase64Str(this_, data, algorithm);
        }

        @Nullable
        public static String digestHex(@NotNull BaseSource this_, @NotNull String data, @NotNull String algorithm) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)algorithm, (String)"algorithm");
            return JsExtensions.DefaultImpls.digestHex(this_, data, algorithm);
        }

        @NotNull
        public static String downloadFile(@NotNull BaseSource this_, @NotNull String content, @NotNull String url2) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)content, (String)"content");
            Intrinsics.checkNotNullParameter((Object)url2, (String)"url");
            return JsExtensions.DefaultImpls.downloadFile(this_, content, url2);
        }

        @NotNull
        public static String encodeURI(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.encodeURI(this_, str);
        }

        @NotNull
        public static String encodeURI(@NotNull BaseSource this_, @NotNull String str, @NotNull String enc) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            Intrinsics.checkNotNullParameter((Object)enc, (String)"enc");
            return JsExtensions.DefaultImpls.encodeURI(this_, str, enc);
        }

        @NotNull
        public static Connection.Response get(@NotNull BaseSource this_, @NotNull String urlStr, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            Intrinsics.checkNotNullParameter(headers, (String)"headers");
            return JsExtensions.DefaultImpls.get(this_, urlStr, headers);
        }

        @NotNull
        public static String getCookie(@NotNull BaseSource this_, @NotNull String tag, @Nullable String key) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)tag, (String)"tag");
            return JsExtensions.DefaultImpls.getCookie(this_, tag, key);
        }

        @NotNull
        public static File getFile(@NotNull BaseSource this_, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            return JsExtensions.DefaultImpls.getFile(this_, path);
        }

        @NotNull
        public static String getTxtInFolder(@NotNull BaseSource this_, @NotNull String unzipPath) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)unzipPath, (String)"unzipPath");
            return JsExtensions.DefaultImpls.getTxtInFolder(this_, unzipPath);
        }

        @Nullable
        public static byte[] getZipByteArrayContent(@NotNull BaseSource this_, @NotNull String url2, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)url2, (String)"url");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            return JsExtensions.DefaultImpls.getZipByteArrayContent(this_, url2, path);
        }

        @NotNull
        public static String getZipStringContent(@NotNull BaseSource this_, @NotNull String url2, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)url2, (String)"url");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            return JsExtensions.DefaultImpls.getZipStringContent(this_, url2, path);
        }

        @NotNull
        public static String getZipStringContent(@NotNull BaseSource this_, @NotNull String url2, @NotNull String path, @NotNull String charsetName) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)url2, (String)"url");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            Intrinsics.checkNotNullParameter((Object)charsetName, (String)"charsetName");
            return JsExtensions.DefaultImpls.getZipStringContent(this_, url2, path, charsetName);
        }

        @NotNull
        public static Connection.Response head(@NotNull BaseSource this_, @NotNull String urlStr, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            Intrinsics.checkNotNullParameter(headers, (String)"headers");
            return JsExtensions.DefaultImpls.head(this_, urlStr, headers);
        }

        @NotNull
        public static String htmlFormat(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.htmlFormat(this_, str);
        }

        @NotNull
        public static String importScript(@NotNull BaseSource this_, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            return JsExtensions.DefaultImpls.importScript(this_, path);
        }

        @NotNull
        public static String log(@NotNull BaseSource this_, @NotNull String msg) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)msg, (String)"msg");
            return JsExtensions.DefaultImpls.log(this_, msg);
        }

        public static void logType(@NotNull BaseSource this_, @Nullable Object any) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            JsExtensions.DefaultImpls.logType(this_, any);
        }

        public static void longToast(@NotNull BaseSource this_, @Nullable Object msg) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            JsExtensions.DefaultImpls.longToast(this_, msg);
        }

        @NotNull
        public static String md5Encode(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.md5Encode(this_, str);
        }

        @NotNull
        public static String md5Encode16(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.md5Encode16(this_, str);
        }

        @NotNull
        public static Connection.Response post(@NotNull BaseSource this_, @NotNull String urlStr, @NotNull String body, @NotNull Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)urlStr, (String)"urlStr");
            Intrinsics.checkNotNullParameter((Object)body, (String)"body");
            Intrinsics.checkNotNullParameter(headers, (String)"headers");
            return JsExtensions.DefaultImpls.post(this_, urlStr, body, headers);
        }

        @Nullable
        public static QueryTTF queryBase64TTF(@NotNull BaseSource this_, @Nullable String base64) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.queryBase64TTF(this_, base64);
        }

        @Nullable
        public static QueryTTF queryTTF(@NotNull BaseSource this_, @Nullable String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.queryTTF(this_, str);
        }

        @NotNull
        public static String randomUUID(@NotNull BaseSource this_) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.randomUUID(this_);
        }

        @Nullable
        public static byte[] readFile(@NotNull BaseSource this_, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            return JsExtensions.DefaultImpls.readFile(this_, path);
        }

        @NotNull
        public static String readTxtFile(@NotNull BaseSource this_, @NotNull String path) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            return JsExtensions.DefaultImpls.readTxtFile(this_, path);
        }

        @NotNull
        public static String readTxtFile(@NotNull BaseSource this_, @NotNull String path, @NotNull String charsetName) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)path, (String)"path");
            Intrinsics.checkNotNullParameter((Object)charsetName, (String)"charsetName");
            return JsExtensions.DefaultImpls.readTxtFile(this_, path, charsetName);
        }

        @NotNull
        public static String replaceFont(@NotNull BaseSource this_, @NotNull String text, @Nullable QueryTTF font1, @Nullable QueryTTF font2) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)text, (String)"text");
            return JsExtensions.DefaultImpls.replaceFont(this_, text, font1, font2);
        }

        @NotNull
        public static String timeFormat(@NotNull BaseSource this_, long time) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.timeFormat(this_, time);
        }

        @Nullable
        public static String timeFormatUTC(@NotNull BaseSource this_, long time, @NotNull String format, int sh) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)format, (String)"format");
            return JsExtensions.DefaultImpls.timeFormatUTC(this_, time, format, sh);
        }

        public static void toast(@NotNull BaseSource this_, @Nullable Object msg) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            JsExtensions.DefaultImpls.toast(this_, msg);
        }

        @Nullable
        public static String tripleDESDecodeArgsBase64Str(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)mode, (String)"mode");
            Intrinsics.checkNotNullParameter((Object)padding, (String)"padding");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.tripleDESDecodeArgsBase64Str(this_, data, key, mode, padding, iv);
        }

        @Nullable
        public static String tripleDESDecodeStr(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)mode, (String)"mode");
            Intrinsics.checkNotNullParameter((Object)padding, (String)"padding");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.tripleDESDecodeStr(this_, data, key, mode, padding, iv);
        }

        @Nullable
        public static String tripleDESEncodeArgsBase64Str(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)mode, (String)"mode");
            Intrinsics.checkNotNullParameter((Object)padding, (String)"padding");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.tripleDESEncodeArgsBase64Str(this_, data, key, mode, padding, iv);
        }

        @Nullable
        public static String tripleDESEncodeBase64Str(@NotNull BaseSource this_, @NotNull String data, @NotNull String key, @NotNull String mode, @NotNull String padding, @NotNull String iv) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)data, (String)"data");
            Intrinsics.checkNotNullParameter((Object)key, (String)"key");
            Intrinsics.checkNotNullParameter((Object)mode, (String)"mode");
            Intrinsics.checkNotNullParameter((Object)padding, (String)"padding");
            Intrinsics.checkNotNullParameter((Object)iv, (String)"iv");
            return JsExtensions.DefaultImpls.tripleDESEncodeBase64Str(this_, data, key, mode, padding, iv);
        }

        @NotNull
        public static String unzipFile(@NotNull BaseSource this_, @NotNull String zipPath) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)zipPath, (String)"zipPath");
            return JsExtensions.DefaultImpls.unzipFile(this_, zipPath);
        }

        @NotNull
        public static String utf8ToGbk(@NotNull BaseSource this_, @NotNull String str) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            Intrinsics.checkNotNullParameter((Object)str, (String)"str");
            return JsExtensions.DefaultImpls.utf8ToGbk(this_, str);
        }

        @Nullable
        public static String webView(@NotNull BaseSource this_, @Nullable String html, @Nullable String url2, @Nullable String js) {
            Intrinsics.checkNotNullParameter((Object)this_, (String)"this");
            return JsExtensions.DefaultImpls.webView(this_, html, url2, js);
        }
    }
}

