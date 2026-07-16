// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import io.legado.app.model.analyzeRule.QueryTTF;
import java.io.File;
import org.jsoup.Connection$Response;
import io.legado.app.help.http.StrResponse;
import com.script.Bindings;
import io.legado.app.help.http.CookieStore;
import io.legado.app.utils.Base64;
import java.nio.charset.Charset;
import kotlin.text.Charsets;
import io.legado.app.utils.EncoderUtils;
import io.legado.app.constant.AppConst;
import java.lang.reflect.Type;
import kotlin.Result$Companion;
import com.google.gson.Gson;
import kotlin.ResultKt;
import com.google.gson.reflect.TypeToken;
import kotlin.Result;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.help.CacheManager;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import com.script.SimpleBindings;
import kotlin.jvm.functions.Function1;
import java.util.Map;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;
import io.legado.app.help.JsExtensions;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u000f\bf\u0018\u00002\u00020\u0001J-\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u00032\u0019\b\u0002\u0010\u001a\u001a\u0013\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u001d0\u001b?\u0006\u0002\b\u001eH\u0016J.\u0010\u001f\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030 j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`!2\b\b\u0002\u0010\"\u001a\u00020\tH\u0016J\b\u0010#\u001a\u00020\u0003H&J\n\u0010$\u001a\u0004\u0018\u00010\u0003H\u0016J\u0016\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010&H\u0016J\n\u0010'\u001a\u0004\u0018\u00010\u0003H\u0016J\u0016\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010&H\u0016J\n\u0010)\u001a\u0004\u0018\u00010\u0003H\u0016J\n\u0010*\u001a\u0004\u0018\u00010\u0000H\u0016J\b\u0010+\u001a\u00020\u0003H&J\n\u0010,\u001a\u0004\u0018\u00010\u0003H\u0016J\b\u0010-\u001a\u00020\u001dH\u0016J\u0010\u0010.\u001a\u00020\u001d2\u0006\u0010\u000e\u001a\u00020\u0003H\u0016J\u0010\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\u0003H\u0016J\b\u00101\u001a\u00020\u001dH\u0016J\b\u00102\u001a\u00020\u001dH\u0016J\u0012\u00103\u001a\u00020\u001d2\b\u00104\u001a\u0004\u0018\u00010\u0003H\u0016R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u0004\u0018\u00010\tX?\u000e?\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0012\u0010\u0005\"\u0004\b\u0013\u0010\u0007R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0015\u0010\u0005\"\u0004\b\u0016\u0010\u0007¡§\u00065" }, d2 = { "Lio/legado/app/data/entities/BaseSource;", "Lio/legado/app/help/JsExtensions;", "concurrentRate", "", "getConcurrentRate", "()Ljava/lang/String;", "setConcurrentRate", "(Ljava/lang/String;)V", "enabledCookieJar", "", "getEnabledCookieJar", "()Ljava/lang/Boolean;", "setEnabledCookieJar", "(Ljava/lang/Boolean;)V", "header", "getHeader", "setHeader", "loginUi", "getLoginUi", "setLoginUi", "loginUrl", "getLoginUrl", "setLoginUrl", "evalJS", "", "jsStr", "bindingsConfig", "Lkotlin/Function1;", "Lcom/script/SimpleBindings;", "", "Lkotlin/ExtensionFunctionType;", "getHeaderMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "hasLoginHeader", "getKey", "getLoginHeader", "getLoginHeaderMap", "", "getLoginInfo", "getLoginInfoMap", "getLoginJs", "getSource", "getTag", "getVariable", "login", "putLoginHeader", "putLoginInfo", "info", "removeLoginHeader", "removeLoginInfo", "setVariable", "variable", "reader-pro" })
public interface BaseSource extends JsExtensions
{
    @Nullable
    String getConcurrentRate();
    
    void setConcurrentRate(@Nullable final String <set-?>);
    
    @Nullable
    String getLoginUrl();
    
    void setLoginUrl(@Nullable final String <set-?>);
    
    @Nullable
    String getLoginUi();
    
    void setLoginUi(@Nullable final String <set-?>);
    
    @Nullable
    String getHeader();
    
    void setHeader(@Nullable final String <set-?>);
    
    @Nullable
    Boolean getEnabledCookieJar();
    
    void setEnabledCookieJar(@Nullable final Boolean <set-?>);
    
    @NotNull
    String getTag();
    
    @NotNull
    String getKey();
    
    @Nullable
    BaseSource getSource();
    
    @Nullable
    String getLoginJs();
    
    void login();
    
    @NotNull
    HashMap<String, String> getHeaderMap(final boolean hasLoginHeader);
    
    @Nullable
    String getLoginHeader();
    
    @Nullable
    Map<String, String> getLoginHeaderMap();
    
    void putLoginHeader(@NotNull final String header);
    
    void removeLoginHeader();
    
    @Nullable
    String getLoginInfo();
    
    @Nullable
    Map<String, String> getLoginInfoMap();
    
    boolean putLoginInfo(@NotNull final String info);
    
    void removeLoginInfo();
    
    void setVariable(@Nullable final String variable);
    
    @Nullable
    String getVariable();
    
    @Nullable
    Object evalJS(@NotNull final String jsStr, @NotNull final Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception;
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        @Nullable
        public static BaseSource getSource(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return this;
        }
        
        @Nullable
        public static String getLoginJs(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final String loginJs = this.getLoginUrl();
            String s;
            if (loginJs == null) {
                s = null;
            }
            else if (StringsKt.startsWith$default(loginJs, "@js:", false, 2, (Object)null)) {
                Intrinsics.checkNotNullExpressionValue((Object)(s = loginJs.substring(4)), "(this as java.lang.String).substring(startIndex)");
            }
            else if (StringsKt.startsWith$default(loginJs, "<js>", false, 2, (Object)null)) {
                Intrinsics.checkNotNullExpressionValue((Object)(s = loginJs.substring(4, StringsKt.lastIndexOf$default((CharSequence)loginJs, "<", 0, false, 6, (Object)null))), "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            }
            else {
                s = loginJs;
            }
            return s;
        }
        
        public static void login(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final String loginJs = this.getLoginJs();
            if (loginJs != null) {
                final String it = loginJs;
                final int n = 0;
                evalJS$default(this, it, null, 2, null);
            }
        }
        
        @NotNull
        public static HashMap<String, String> getHeaderMap(@NotNull final BaseSource this, final boolean hasLoginHeader) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "this"
            //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //     6: new             Ljava/util/HashMap;
            //     9: dup            
            //    10: invokespecial   java/util/HashMap.<init>:()V
            //    13: astore_2       
            //    14: iconst_0       
            //    15: istore_3       
            //    16: iconst_0       
            //    17: istore          4
            //    19: aload_2        
            //    20: astore          receiver
            //    22: iconst_0       
            //    23: istore          $i$a$-apply-BaseSource$getHeaderMap$1
            //    25: aload           receiver
            //    27: checkcast       Ljava/util/Map;
            //    30: astore          7
            //    32: ldc             "User-Agent"
            //    34: astore          8
            //    36: getstatic       io/legado/app/constant/AppConst.INSTANCE:Lio/legado/app/constant/AppConst;
            //    39: invokevirtual   io/legado/app/constant/AppConst.getUserAgent:()Ljava/lang/String;
            //    42: astore          9
            //    44: iconst_0       
            //    45: istore          10
            //    47: aload           7
            //    49: aload           8
            //    51: aload           9
            //    53: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //    58: pop            
            //    59: aload_0         /* this */
            //    60: invokeinterface io/legado/app/data/entities/BaseSource.getHeader:()Ljava/lang/String;
            //    65: astore          7
            //    67: aload           7
            //    69: ifnonnull       75
            //    72: goto            372
            //    75: aload           7
            //    77: astore          8
            //    79: iconst_0       
            //    80: istore          9
            //    82: iconst_0       
            //    83: istore          10
            //    85: aload           8
            //    87: astore          it
            //    89: iconst_0       
            //    90: istore          $i$a$-let-BaseSource$getHeaderMap$1$1
            //    92: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
            //    95: astore          13
            //    97: nop            
            //    98: aload           it
            //   100: ldc             "@js:"
            //   102: iconst_1       
            //   103: invokestatic    kotlin/text/StringsKt.startsWith:(Ljava/lang/String;Ljava/lang/String;Z)Z
            //   106: ifeq            145
            //   109: aload_0         /* this */
            //   110: aload           it
            //   112: astore          14
            //   114: iconst_4       
            //   115: istore          15
            //   117: iconst_0       
            //   118: istore          16
            //   120: aload           14
            //   122: iload           15
            //   124: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
            //   127: dup            
            //   128: ldc             "(this as java.lang.String).substring(startIndex)"
            //   130: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //   133: aconst_null    
            //   134: iconst_2       
            //   135: aconst_null    
            //   136: invokestatic    io/legado/app/data/entities/BaseSource$DefaultImpls.evalJS$default:(Lio/legado/app/data/entities/BaseSource;Ljava/lang/String;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/Object;
            //   139: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
            //   142: goto            213
            //   145: aload           it
            //   147: ldc             "<js>"
            //   149: iconst_1       
            //   150: invokestatic    kotlin/text/StringsKt.startsWith:(Ljava/lang/String;Ljava/lang/String;Z)Z
            //   153: ifeq            211
            //   156: aload_0         /* this */
            //   157: aload           it
            //   159: astore          14
            //   161: iconst_4       
            //   162: istore          15
            //   164: aload           it
            //   166: checkcast       Ljava/lang/CharSequence;
            //   169: ldc             "<"
            //   171: iconst_0       
            //   172: iconst_0       
            //   173: bipush          6
            //   175: aconst_null    
            //   176: invokestatic    kotlin/text/StringsKt.lastIndexOf$default:(Ljava/lang/CharSequence;Ljava/lang/String;IZILjava/lang/Object;)I
            //   179: istore          16
            //   181: iconst_0       
            //   182: istore          17
            //   184: aload           14
            //   186: iload           15
            //   188: iload           16
            //   190: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
            //   193: dup            
            //   194: ldc             "(this as java.lang.Strin\u2026ing(startIndex, endIndex)"
            //   196: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //   199: aconst_null    
            //   200: iconst_2       
            //   201: aconst_null    
            //   202: invokestatic    io/legado/app/data/entities/BaseSource$DefaultImpls.evalJS$default:(Lio/legado/app/data/entities/BaseSource;Ljava/lang/String;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/Object;
            //   205: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
            //   208: goto            213
            //   211: aload           it
            //   213: astore          14
            //   215: nop            
            //   216: iconst_0       
            //   217: istore          $i$f$fromJsonObject
            //   219: iconst_0       
            //   220: istore          16
            //   222: nop            
            //   223: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   226: astore          17
            //   228: iconst_0       
            //   229: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
            //   231: aload           $this$fromJsonObject$iv
            //   233: aload           json$iv
            //   235: iconst_0       
            //   236: istore          $i$f$genericType
            //   238: new             Lio/legado/app/data/entities/BaseSource$DefaultImpls$getHeaderMap$lambda-4$lambda-2$$inlined$fromJsonObject$1;
            //   241: dup            
            //   242: invokespecial   io/legado/app/data/entities/BaseSource$DefaultImpls$getHeaderMap$lambda-4$lambda-2$$inlined$fromJsonObject$1.<init>:()V
            //   245: invokevirtual   io/legado/app/data/entities/BaseSource$DefaultImpls$getHeaderMap$lambda-4$lambda-2$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
            //   248: astore          20
            //   250: aload           20
            //   252: ldc             "object : TypeToken<T>() {}.type"
            //   254: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //   257: aload           20
            //   259: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
            //   262: dup            
            //   263: instanceof      Ljava/util/Map;
            //   266: ifne            271
            //   269: pop            
            //   270: aconst_null    
            //   271: checkcast       Ljava/util/Map;
            //   274: astore          null
            //   276: iconst_0       
            //   277: istore          19
            //   279: aload           18
            //   281: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   284: astore          17
            //   286: goto            309
            //   289: astore          18
            //   291: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //   294: astore          19
            //   296: iconst_0       
            //   297: istore          20
            //   299: aload           18
            //   301: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //   304: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   307: astore          17
            //   309: aload           17
            //   311: nop            
            //   312: astore          null
            //   314: iconst_0       
            //   315: istore          14
            //   317: aload           13
            //   319: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
            //   322: ifeq            329
            //   325: aconst_null    
            //   326: goto            331
            //   329: aload           13
            //   331: checkcast       Ljava/util/Map;
            //   334: astore          21
            //   336: aload           21
            //   338: ifnonnull       344
            //   341: goto            370
            //   344: aload           21
            //   346: astore          13
            //   348: iconst_0       
            //   349: istore          14
            //   351: iconst_0       
            //   352: istore          15
            //   354: aload           13
            //   356: astore          map
            //   358: iconst_0       
            //   359: istore          $i$a$-let-BaseSource$getHeaderMap$1$1$1
            //   361: aload           receiver
            //   363: aload           map
            //   365: invokevirtual   java/util/HashMap.putAll:(Ljava/util/Map;)V
            //   368: nop            
            //   369: nop            
            //   370: nop            
            //   371: nop            
            //   372: iload_1         /* hasLoginHeader */
            //   373: ifeq            418
            //   376: aload_0         /* this */
            //   377: invokeinterface io/legado/app/data/entities/BaseSource.getLoginHeaderMap:()Ljava/util/Map;
            //   382: astore          7
            //   384: aload           7
            //   386: ifnonnull       392
            //   389: goto            418
            //   392: aload           7
            //   394: astore          8
            //   396: iconst_0       
            //   397: istore          9
            //   399: iconst_0       
            //   400: istore          10
            //   402: aload           8
            //   404: astore          it
            //   406: iconst_0       
            //   407: istore          $i$a$-let-BaseSource$getHeaderMap$1$2
            //   409: aload           receiver
            //   411: aload           it
            //   413: invokevirtual   java/util/HashMap.putAll:(Ljava/util/Map;)V
            //   416: nop            
            //   417: nop            
            //   418: nop            
            //   419: aload_2        
            //   420: areturn        
            //    Signature:
            //  (Z)Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>; [from metadata: (Lio/legado/app/data/entities/BaseSource;Z)Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;]
            //  
            //    MethodParameters:
            //  Name            Flags      
            //  --------------  ---------
            //  this            SYNTHETIC
            //  hasLoginHeader  
            //    StackMapTable: 00 0E FF 00 4B 00 0B 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 07 00 23 01 00 00 FF 00 45 00 0E 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 01 01 07 00 23 01 07 00 87 00 00 FB 00 41 41 07 00 23 FF 00 39 00 15 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 01 01 07 00 23 01 07 00 87 07 00 23 01 01 07 00 B2 01 01 07 00 B4 00 01 07 00 04 FF 00 11 00 11 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 01 01 07 00 23 01 07 00 87 07 00 23 01 01 00 01 07 00 4E FD 00 13 07 00 04 07 00 04 FF 00 13 00 13 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 01 01 07 00 23 01 07 00 04 01 01 01 07 00 04 07 00 04 00 00 41 07 00 04 FE 00 0C 00 00 07 00 56 FF 00 19 00 16 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 01 01 07 00 23 01 07 00 04 01 01 00 00 07 00 04 00 00 07 00 56 00 00 FF 00 01 00 0B 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 23 07 00 23 00 01 00 00 FF 00 13 00 0B 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 56 07 00 23 00 01 00 00 FF 00 19 00 0B 07 00 15 01 07 00 50 01 01 07 00 50 01 07 00 04 07 00 04 00 01 00 00
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  222    286    289    309    Ljava/lang/Throwable;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
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
        public static String getLoginHeader(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            return cacheInstance.get(Intrinsics.stringPlus("loginHeader_", (Object)this.getKey()));
        }
        
        @Nullable
        public static Map<String, String> getLoginHeaderMap(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final String loginHeader = this.getLoginHeader();
            if (loginHeader == null) {
                return null;
            }
            final String cache = loginHeader;
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final String s = cache;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<Map<String, ? extends String>>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(s, type)) instanceof Map)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            return (Map<String, String>)(Result.isFailure-impl(o2) ? null : o2);
        }
        
        public static void putLoginHeader(@NotNull final BaseSource this, @NotNull final String header) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)header, "header");
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            CacheManager.put$default(cacheInstance, Intrinsics.stringPlus("loginHeader_", (Object)this.getKey()), header, 0, 4, null);
        }
        
        public static void removeLoginHeader(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            cacheInstance.delete(Intrinsics.stringPlus("loginHeader_", (Object)this.getKey()));
        }
        
        @Nullable
        public static String getLoginInfo(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            try {
                final byte[] key = StringsKt.encodeToByteArray$default(AppConst.INSTANCE.getUserAgent(), 0, 8, false, 4, (Object)null);
                final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
                final String value = cacheInstance.get(Intrinsics.stringPlus("userInfo_", (Object)this.getKey()));
                if (value == null) {
                    return null;
                }
                final String cache = value;
                final String base64Decode = EncoderUtils.INSTANCE.base64Decode(cache, 0);
                final Charset utf_8 = Charsets.UTF_8;
                final String s = base64Decode;
                if (s == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final byte[] bytes = s.getBytes(utf_8);
                Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
                final byte[] encodeBytes = bytes;
                final byte[] decryptAES$default = EncoderUtils.decryptAES$default(EncoderUtils.INSTANCE, encodeBytes, key, null, null, 12, null);
                if (decryptAES$default == null) {
                    return null;
                }
                final byte[] decodeBytes = decryptAES$default;
                return new String(decodeBytes, Charsets.UTF_8);
            }
            catch (final Exception e) {
                this.log(Intrinsics.stringPlus("\u83b7\u53d6\u767b\u9646\u4fe1\u606f\u51fa\u9519 ", (Object)e.getLocalizedMessage()));
                return null;
            }
        }
        
        @Nullable
        public static Map<String, String> getLoginInfoMap(@NotNull final BaseSource this) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "this"
            //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //     6: invokestatic    io/legado/app/utils/GsonExtensionsKt.getGSON:()Lcom/google/gson/Gson;
            //     9: astore_1       
            //    10: aload_0         /* this */
            //    11: invokeinterface io/legado/app/data/entities/BaseSource.getLoginInfo:()Ljava/lang/String;
            //    16: astore_2        /* json$iv */
            //    17: iconst_0       
            //    18: istore_3        /* $i$f$fromJsonObject */
            //    19: iconst_0       
            //    20: istore          4
            //    22: nop            
            //    23: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //    26: astore          5
            //    28: iconst_0       
            //    29: istore          $i$a$-runCatching-GsonExtensionsKt$fromJsonObject$1$iv
            //    31: aload_1         /* $this$fromJsonObject$iv */
            //    32: aload_2         /* json$iv */
            //    33: iconst_0       
            //    34: istore          $i$f$genericType
            //    36: new             Lio/legado/app/data/entities/BaseSource$DefaultImpls$getLoginInfoMap$$inlined$fromJsonObject$1;
            //    39: dup            
            //    40: invokespecial   io/legado/app/data/entities/BaseSource$DefaultImpls$getLoginInfoMap$$inlined$fromJsonObject$1.<init>:()V
            //    43: invokevirtual   io/legado/app/data/entities/BaseSource$DefaultImpls$getLoginInfoMap$$inlined$fromJsonObject$1.getType:()Ljava/lang/reflect/Type;
            //    46: astore          8
            //    48: aload           8
            //    50: ldc             "object : TypeToken<T>() {}.type"
            //    52: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //    55: aload           8
            //    57: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
            //    60: dup            
            //    61: instanceof      Ljava/util/Map;
            //    64: ifne            69
            //    67: pop            
            //    68: aconst_null    
            //    69: checkcast       Ljava/util/Map;
            //    72: astore          null
            //    74: iconst_0       
            //    75: istore          7
            //    77: aload           6
            //    79: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //    82: astore          5
            //    84: goto            107
            //    87: astore          6
            //    89: getstatic       kotlin/Result.Companion:Lkotlin/Result$Companion;
            //    92: astore          7
            //    94: iconst_0       
            //    95: istore          8
            //    97: aload           6
            //    99: invokestatic    kotlin/ResultKt.createFailure:(Ljava/lang/Throwable;)Ljava/lang/Object;
            //   102: invokestatic    kotlin/Result.constructor-impl:(Ljava/lang/Object;)Ljava/lang/Object;
            //   105: astore          5
            //   107: aload           5
            //   109: nop            
            //   110: astore_1        /* $this$fromJsonObject$iv */
            //   111: iconst_0       
            //   112: istore_2       
            //   113: aload_1        
            //   114: invokestatic    kotlin/Result.isFailure-impl:(Ljava/lang/Object;)Z
            //   117: ifeq            124
            //   120: aconst_null    
            //   121: goto            125
            //   124: aload_1        
            //   125: checkcast       Ljava/util/Map;
            //   128: areturn        
            //    Signature:
            //  ()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>; [from metadata: (Lio/legado/app/data/entities/BaseSource;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;]
            //  
            //    MethodParameters:
            //  Name  Flags      
            //  ----  ---------
            //  this  SYNTHETIC
            //    StackMapTable: 00 05 FF 00 45 00 09 07 00 15 07 00 87 07 00 23 01 01 07 00 B2 01 01 07 00 B4 00 01 07 00 04 FF 00 11 00 05 07 00 15 07 00 87 07 00 23 01 01 00 01 07 00 4E FD 00 13 07 00 04 07 00 04 FF 00 10 00 07 07 00 15 07 00 04 01 01 01 07 00 04 07 00 04 00 00 40 07 00 04
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  22     84     87     107    Ljava/lang/Throwable;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
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
        
        public static boolean putLoginInfo(@NotNull final BaseSource this, @NotNull final String info) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)info, "info");
            boolean b;
            try {
                final byte[] key = StringsKt.encodeToByteArray$default(AppConst.INSTANCE.getUserAgent(), 0, 8, false, 4, (Object)null);
                final EncoderUtils instance = EncoderUtils.INSTANCE;
                final byte[] bytes = info.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
                final byte[] encodeBytes = EncoderUtils.encryptAES$default(instance, bytes, key, null, null, 12, null);
                final String encodeStr = Base64.encodeToString(encodeBytes, 0);
                final CacheManager cacheManager;
                final CacheManager cacheInstance = cacheManager = new CacheManager(this.getUserNameSpace());
                final String stringPlus = Intrinsics.stringPlus("userInfo_", (Object)this.getKey());
                Intrinsics.checkNotNullExpressionValue((Object)encodeStr, "encodeStr");
                CacheManager.put$default(cacheManager, stringPlus, encodeStr, 0, 4, null);
                b = true;
            }
            catch (final Exception e) {
                this.log(Intrinsics.stringPlus("\u4fdd\u5b58\u767b\u9646\u4fe1\u606f\u51fa\u9519 ", (Object)e.getLocalizedMessage()));
                b = false;
            }
            return b;
        }
        
        public static void removeLoginInfo(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            cacheInstance.delete(Intrinsics.stringPlus("userInfo_", (Object)this.getKey()));
        }
        
        public static void setVariable(@NotNull final BaseSource this, @Nullable final String variable) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            if (variable != null) {
                CacheManager.put$default(cacheInstance, Intrinsics.stringPlus("sourceVariable_", (Object)this.getKey()), variable, 0, 4, null);
            }
            else {
                cacheInstance.delete(Intrinsics.stringPlus("sourceVariable_", (Object)this.getKey()));
            }
        }
        
        @Nullable
        public static String getVariable(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final CacheManager cacheInstance = new CacheManager(this.getUserNameSpace());
            return cacheInstance.get(Intrinsics.stringPlus("sourceVariable_", (Object)this.getKey()));
        }
        
        @Nullable
        public static Object evalJS(@NotNull final BaseSource this, @NotNull final String jsStr, @NotNull final Function1<? super SimpleBindings, Unit> bindingsConfig) throws Exception {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)jsStr, "jsStr");
            Intrinsics.checkNotNullParameter((Object)bindingsConfig, "bindingsConfig");
            final SimpleBindings bindings = new SimpleBindings();
            bindingsConfig.invoke((Object)bindings);
            ((Map)bindings).put("java", this);
            ((Map)bindings).put("source", this);
            ((Map)bindings).put("baseUrl", this.getKey());
            ((Map)bindings).put("cookie", new CookieStore(this.getUserNameSpace()));
            ((Map)bindings).put("cache", new CacheManager(this.getUserNameSpace()));
            return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, (Bindings)bindings);
        }
        
        public static /* synthetic */ Object evalJS$default(final BaseSource baseSource, final String jsStr, Function1 bindingsConfig, final int n, final Object o) throws Exception {
            if (o != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: evalJS");
            }
            if ((n & 0x2) != 0x0) {
                bindingsConfig = (Function1)BaseSource$evalJS.BaseSource$evalJS$1.INSTANCE;
            }
            return baseSource.evalJS(jsStr, (Function1<? super SimpleBindings, Unit>)bindingsConfig);
        }
        
        @Nullable
        public static byte[] aesBase64DecodeToByteArray(@NotNull final BaseSource this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesBase64DecodeToByteArray(str, key, transformation, iv);
        }
        
        @Nullable
        public static String aesBase64DecodeToString(@NotNull final BaseSource this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesBase64DecodeToString(str, key, transformation, iv);
        }
        
        @Nullable
        public static String aesDecodeArgsBase64Str(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesDecodeArgsBase64Str(data, key, mode, padding, iv);
        }
        
        @Nullable
        public static byte[] aesDecodeToByteArray(@NotNull final BaseSource this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesDecodeToByteArray(str, key, transformation, iv);
        }
        
        @Nullable
        public static String aesDecodeToString(@NotNull final BaseSource this, @NotNull final String str, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesDecodeToString(str, key, transformation, iv);
        }
        
        @Nullable
        public static String aesEncodeArgsBase64Str(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeArgsBase64Str(data, key, mode, padding, iv);
        }
        
        @Nullable
        public static byte[] aesEncodeToBase64ByteArray(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToBase64ByteArray(data, key, transformation, iv);
        }
        
        @Nullable
        public static String aesEncodeToBase64String(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToBase64String(data, key, transformation, iv);
        }
        
        @Nullable
        public static byte[] aesEncodeToByteArray(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToByteArray(data, key, transformation, iv);
        }
        
        @Nullable
        public static String aesEncodeToString(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.aesEncodeToString(data, key, transformation, iv);
        }
        
        @Nullable
        public static String ajax(@NotNull final BaseSource this, @NotNull final String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return JsExtensions.DefaultImpls.ajax(urlStr);
        }
        
        @NotNull
        public static StrResponse[] ajaxAll(@NotNull final BaseSource this, @NotNull final String[] urlList) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlList, "urlList");
            return JsExtensions.DefaultImpls.ajaxAll(urlList);
        }
        
        @NotNull
        public static String androidId(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.androidId();
        }
        
        @NotNull
        public static String base64Decode(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.base64Decode(str);
        }
        
        @NotNull
        public static String base64Decode(@NotNull final BaseSource this, @NotNull final String str, final int flags) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.base64Decode(str, flags);
        }
        
        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull final BaseSource this, @Nullable final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.base64DecodeToByteArray(str);
        }
        
        @Nullable
        public static byte[] base64DecodeToByteArray(@NotNull final BaseSource this, @Nullable final String str, final int flags) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.base64DecodeToByteArray(str, flags);
        }
        
        @Nullable
        public static String base64Encode(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.base64Encode(str);
        }
        
        @Nullable
        public static String base64Encode(@NotNull final BaseSource this, @NotNull final String str, final int flags) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.base64Encode(str, flags);
        }
        
        @Nullable
        public static String cacheFile(@NotNull final BaseSource this, @NotNull final String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return JsExtensions.DefaultImpls.cacheFile(urlStr);
        }
        
        @Nullable
        public static String cacheFile(@NotNull final BaseSource this, @NotNull final String urlStr, final int saveTime) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return JsExtensions.DefaultImpls.cacheFile(urlStr, saveTime);
        }
        
        @NotNull
        public static StrResponse connect(@NotNull final BaseSource this, @NotNull final String urlStr) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return JsExtensions.DefaultImpls.connect(urlStr);
        }
        
        @NotNull
        public static StrResponse connect(@NotNull final BaseSource this, @NotNull final String urlStr, @Nullable final String header) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            return JsExtensions.DefaultImpls.connect(urlStr, header);
        }
        
        public static void deleteFile(@NotNull final BaseSource this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            JsExtensions.DefaultImpls.deleteFile(path);
        }
        
        @Nullable
        public static String desBase64DecodeToString(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.desBase64DecodeToString(data, key, transformation, iv);
        }
        
        @Nullable
        public static String desDecodeToString(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.desDecodeToString(data, key, transformation, iv);
        }
        
        @Nullable
        public static String desEncodeToBase64String(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.desEncodeToBase64String(data, key, transformation, iv);
        }
        
        @Nullable
        public static String desEncodeToString(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String transformation, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.desEncodeToString(data, key, transformation, iv);
        }
        
        @Nullable
        public static String digestBase64Str(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String algorithm) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)algorithm, "algorithm");
            return JsExtensions.DefaultImpls.digestBase64Str(data, algorithm);
        }
        
        @Nullable
        public static String digestHex(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String algorithm) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)algorithm, "algorithm");
            return JsExtensions.DefaultImpls.digestHex(data, algorithm);
        }
        
        @NotNull
        public static String downloadFile(@NotNull final BaseSource this, @NotNull final String content, @NotNull final String url) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)content, "content");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            return JsExtensions.DefaultImpls.downloadFile(content, url);
        }
        
        @NotNull
        public static String encodeURI(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.encodeURI(str);
        }
        
        @NotNull
        public static String encodeURI(@NotNull final BaseSource this, @NotNull final String str, @NotNull final String enc) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            Intrinsics.checkNotNullParameter((Object)enc, "enc");
            return JsExtensions.DefaultImpls.encodeURI(str, enc);
        }
        
        @NotNull
        public static Connection$Response get(@NotNull final BaseSource this, @NotNull final String urlStr, @NotNull final Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            Intrinsics.checkNotNullParameter((Object)headers, "headers");
            return JsExtensions.DefaultImpls.get(urlStr, headers);
        }
        
        @NotNull
        public static String getCookie(@NotNull final BaseSource this, @NotNull final String tag, @Nullable final String key) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)tag, "tag");
            return JsExtensions.DefaultImpls.getCookie(tag, key);
        }
        
        @NotNull
        public static File getFile(@NotNull final BaseSource this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            return JsExtensions.DefaultImpls.getFile(path);
        }
        
        @NotNull
        public static String getTxtInFolder(@NotNull final BaseSource this, @NotNull final String unzipPath) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)unzipPath, "unzipPath");
            return JsExtensions.DefaultImpls.getTxtInFolder(unzipPath);
        }
        
        @Nullable
        public static byte[] getZipByteArrayContent(@NotNull final BaseSource this, @NotNull final String url, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            return JsExtensions.DefaultImpls.getZipByteArrayContent(url, path);
        }
        
        @NotNull
        public static String getZipStringContent(@NotNull final BaseSource this, @NotNull final String url, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            return JsExtensions.DefaultImpls.getZipStringContent(url, path);
        }
        
        @NotNull
        public static String getZipStringContent(@NotNull final BaseSource this, @NotNull final String url, @NotNull final String path, @NotNull final String charsetName) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)url, "url");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            Intrinsics.checkNotNullParameter((Object)charsetName, "charsetName");
            return JsExtensions.DefaultImpls.getZipStringContent(url, path, charsetName);
        }
        
        @NotNull
        public static Connection$Response head(@NotNull final BaseSource this, @NotNull final String urlStr, @NotNull final Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            Intrinsics.checkNotNullParameter((Object)headers, "headers");
            return JsExtensions.DefaultImpls.head(urlStr, headers);
        }
        
        @NotNull
        public static String htmlFormat(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.htmlFormat(str);
        }
        
        @NotNull
        public static String importScript(@NotNull final BaseSource this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            return JsExtensions.DefaultImpls.importScript(path);
        }
        
        @NotNull
        public static String log(@NotNull final BaseSource this, @NotNull final String msg) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)msg, "msg");
            return JsExtensions.DefaultImpls.log(msg);
        }
        
        public static void logType(@NotNull final BaseSource this, @Nullable final Object any) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            JsExtensions.DefaultImpls.logType(any);
        }
        
        public static void longToast(@NotNull final BaseSource this, @Nullable final Object msg) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            JsExtensions.DefaultImpls.longToast(msg);
        }
        
        @NotNull
        public static String md5Encode(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.md5Encode(str);
        }
        
        @NotNull
        public static String md5Encode16(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.md5Encode16(str);
        }
        
        @NotNull
        public static Connection$Response post(@NotNull final BaseSource this, @NotNull final String urlStr, @NotNull final String body, @NotNull final Map<String, String> headers) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)urlStr, "urlStr");
            Intrinsics.checkNotNullParameter((Object)body, "body");
            Intrinsics.checkNotNullParameter((Object)headers, "headers");
            return JsExtensions.DefaultImpls.post(urlStr, body, headers);
        }
        
        @Nullable
        public static QueryTTF queryBase64TTF(@NotNull final BaseSource this, @Nullable final String base64) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.queryBase64TTF(base64);
        }
        
        @Nullable
        public static QueryTTF queryTTF(@NotNull final BaseSource this, @Nullable final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.queryTTF(str);
        }
        
        @NotNull
        public static String randomUUID(@NotNull final BaseSource this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.randomUUID();
        }
        
        @Nullable
        public static byte[] readFile(@NotNull final BaseSource this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            return JsExtensions.DefaultImpls.readFile(path);
        }
        
        @NotNull
        public static String readTxtFile(@NotNull final BaseSource this, @NotNull final String path) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            return JsExtensions.DefaultImpls.readTxtFile(path);
        }
        
        @NotNull
        public static String readTxtFile(@NotNull final BaseSource this, @NotNull final String path, @NotNull final String charsetName) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)path, "path");
            Intrinsics.checkNotNullParameter((Object)charsetName, "charsetName");
            return JsExtensions.DefaultImpls.readTxtFile(path, charsetName);
        }
        
        @NotNull
        public static String replaceFont(@NotNull final BaseSource this, @NotNull final String text, @Nullable final QueryTTF font1, @Nullable final QueryTTF font2) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)text, "text");
            return JsExtensions.DefaultImpls.replaceFont(text, font1, font2);
        }
        
        @NotNull
        public static String timeFormat(@NotNull final BaseSource this, final long time) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.timeFormat(time);
        }
        
        @Nullable
        public static String timeFormatUTC(@NotNull final BaseSource this, final long time, @NotNull final String format, final int sh) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)format, "format");
            return JsExtensions.DefaultImpls.timeFormatUTC(time, format, sh);
        }
        
        public static void toast(@NotNull final BaseSource this, @Nullable final Object msg) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            JsExtensions.DefaultImpls.toast(msg);
        }
        
        @Nullable
        public static String tripleDESDecodeArgsBase64Str(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESDecodeArgsBase64Str(data, key, mode, padding, iv);
        }
        
        @Nullable
        public static String tripleDESDecodeStr(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESDecodeStr(data, key, mode, padding, iv);
        }
        
        @Nullable
        public static String tripleDESEncodeArgsBase64Str(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESEncodeArgsBase64Str(data, key, mode, padding, iv);
        }
        
        @Nullable
        public static String tripleDESEncodeBase64Str(@NotNull final BaseSource this, @NotNull final String data, @NotNull final String key, @NotNull final String mode, @NotNull final String padding, @NotNull final String iv) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)data, "data");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            Intrinsics.checkNotNullParameter((Object)mode, "mode");
            Intrinsics.checkNotNullParameter((Object)padding, "padding");
            Intrinsics.checkNotNullParameter((Object)iv, "iv");
            return JsExtensions.DefaultImpls.tripleDESEncodeBase64Str(data, key, mode, padding, iv);
        }
        
        @NotNull
        public static String unzipFile(@NotNull final BaseSource this, @NotNull final String zipPath) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)zipPath, "zipPath");
            return JsExtensions.DefaultImpls.unzipFile(zipPath);
        }
        
        @NotNull
        public static String utf8ToGbk(@NotNull final BaseSource this, @NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)str, "str");
            return JsExtensions.DefaultImpls.utf8ToGbk(str);
        }
        
        @Nullable
        public static String webView(@NotNull final BaseSource this, @Nullable final String html, @Nullable final String url, @Nullable final String js) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            return JsExtensions.DefaultImpls.webView(html, url, js);
        }
    }
}
