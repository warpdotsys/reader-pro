// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import java.util.Iterator;
import kotlin.text.StringsKt;
import java.util.Map;
import io.legado.app.utils.TextUtils;
import io.legado.app.utils.NetworkUtils;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import io.legado.app.adapters.ReaderAdapterHelper;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.utils.ACache;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import io.legado.app.help.http.api.CookieManager;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u001c\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u000e2\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0003H\u0016J\u0016\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003J \u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0014\u0010\u0015\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0003H\u0016J\u0018\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\u001a\u0010\u0019\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u0016R\u0011\u0010\u0005\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¡§\u0006\u001a" }, d2 = { "Lio/legado/app/help/http/CookieStore;", "Lio/legado/app/help/http/api/CookieManager;", "userNameSpace", "", "(Ljava/lang/String;)V", "cacheInstance", "Lio/legado/app/utils/ACache;", "getCacheInstance", "()Lio/legado/app/utils/ACache;", "getUserNameSpace", "()Ljava/lang/String;", "clear", "", "cookieToMap", "", "cookie", "getCookie", "url", "getKey", "key", "mapToCookie", "cookieMap", "", "removeCookie", "replaceCookie", "setCookie", "reader-pro" })
public final class CookieStore implements CookieManager
{
    @NotNull
    private final String userNameSpace;
    @NotNull
    private final ACache cacheInstance;
    
    public CookieStore(@NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        this.userNameSpace = userNameSpace;
        final File cacheDir = new File(ReaderAdapterHelper.INSTANCE.getAdapter().getWorkDir("storage", "cache", "cookie", this.userNameSpace));
        this.cacheInstance = ACache.Companion.get(cacheDir, 50000000L, 1000000);
    }
    
    @NotNull
    public final String getUserNameSpace() {
        return this.userNameSpace;
    }
    
    @NotNull
    public final ACache getCacheInstance() {
        return this.cacheInstance;
    }
    
    @Override
    public void setCookie(@NotNull final String url, @Nullable final String cookie) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        final String domain = NetworkUtils.INSTANCE.getSubDomain(url);
        if (domain.length() > 0) {
            this.cacheInstance.put(domain, (cookie == null) ? "" : cookie);
        }
    }
    
    @Override
    public void replaceCookie(@NotNull final String url, @NotNull final String cookie) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)cookie, "cookie");
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cookie)) {
            return;
        }
        final String oldCookie = this.getCookie(url);
        if (TextUtils.isEmpty(oldCookie)) {
            this.setCookie(url, cookie);
        }
        else {
            final Map cookieMap = this.cookieToMap(oldCookie);
            cookieMap.putAll(this.cookieToMap(cookie));
            final String newCookie = this.mapToCookie(cookieMap);
            this.setCookie(url, newCookie);
        }
    }
    
    @NotNull
    @Override
    public String getCookie(@NotNull final String url) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        final String domain = NetworkUtils.INSTANCE.getSubDomain(url);
        if (domain.length() == 0) {
            return "";
        }
        final String asString = this.cacheInstance.getAsString(domain);
        return (asString == null) ? "" : asString;
    }
    
    @NotNull
    public final String getKey(@NotNull final String url, @NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final String cookie = this.getCookie(url);
        final Map cookieMap = this.cookieToMap(cookie);
        final String s = cookieMap.get(key);
        return (s == null) ? "" : s;
    }
    
    @Override
    public void removeCookie(@NotNull final String url) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        final String domain = NetworkUtils.INSTANCE.getSubDomain(url);
        if (domain.length() == 0) {
            return;
        }
        this.cacheInstance.remove(domain);
    }
    
    @NotNull
    @Override
    public Map<String, String> cookieToMap(@NotNull final String cookie) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "cookie"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: iconst_0       
        //     7: istore_3       
        //     8: new             Ljava/util/LinkedHashMap;
        //    11: dup            
        //    12: invokespecial   java/util/LinkedHashMap.<init>:()V
        //    15: checkcast       Ljava/util/Map;
        //    18: astore_2        /* cookieMap */
        //    19: aload_1         /* cookie */
        //    20: checkcast       Ljava/lang/CharSequence;
        //    23: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //    26: ifeq            31
        //    29: aload_2         /* cookieMap */
        //    30: areturn        
        //    31: aload_1         /* cookie */
        //    32: checkcast       Ljava/lang/CharSequence;
        //    35: astore          4
        //    37: ldc             ";"
        //    39: astore          5
        //    41: iconst_0       
        //    42: istore          6
        //    44: new             Lkotlin/text/Regex;
        //    47: dup            
        //    48: aload           5
        //    50: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //    53: astore          5
        //    55: iconst_0       
        //    56: istore          6
        //    58: iconst_0       
        //    59: istore          7
        //    61: aload           5
        //    63: aload           4
        //    65: iload           6
        //    67: invokevirtual   kotlin/text/Regex.split:(Ljava/lang/CharSequence;I)Ljava/util/List;
        //    70: astore          4
        //    72: nop            
        //    73: iconst_0       
        //    74: istore          $i$f$dropLastWhile
        //    76: aload           $this$dropLastWhile$iv
        //    78: invokeinterface java/util/List.isEmpty:()Z
        //    83: ifne            179
        //    86: aload           $this$dropLastWhile$iv
        //    88: aload           $this$dropLastWhile$iv
        //    90: invokeinterface java/util/List.size:()I
        //    95: invokeinterface java/util/List.listIterator:(I)Ljava/util/ListIterator;
        //   100: astore          iterator$iv
        //   102: aload           iterator$iv
        //   104: invokeinterface java/util/ListIterator.hasPrevious:()Z
        //   109: ifeq            179
        //   112: aload           iterator$iv
        //   114: invokeinterface java/util/ListIterator.previous:()Ljava/lang/Object;
        //   119: checkcast       Ljava/lang/String;
        //   122: astore          it
        //   124: iconst_0       
        //   125: istore          $i$a$-dropLastWhile-CookieStore$cookieToMap$pairArray$1
        //   127: aload           it
        //   129: checkcast       Ljava/lang/CharSequence;
        //   132: astore          9
        //   134: iconst_0       
        //   135: istore          10
        //   137: aload           9
        //   139: invokeinterface java/lang/CharSequence.length:()I
        //   144: ifne            151
        //   147: iconst_1       
        //   148: goto            152
        //   151: iconst_0       
        //   152: nop            
        //   153: ifne            176
        //   156: aload           $this$dropLastWhile$iv
        //   158: checkcast       Ljava/lang/Iterable;
        //   161: aload           iterator$iv
        //   163: invokeinterface java/util/ListIterator.nextIndex:()I
        //   168: iconst_1       
        //   169: iadd           
        //   170: invokestatic    kotlin/collections/CollectionsKt.take:(Ljava/lang/Iterable;I)Ljava/util/List;
        //   173: goto            182
        //   176: goto            102
        //   179: invokestatic    kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
        //   182: checkcast       Ljava/util/Collection;
        //   185: astore          4
        //   187: nop            
        //   188: iconst_0       
        //   189: istore          $i$f$toTypedArray
        //   191: aload           $this$toTypedArray$iv
        //   193: astore          thisCollection$iv
        //   195: aload           thisCollection$iv
        //   197: iconst_0       
        //   198: anewarray       Ljava/lang/String;
        //   201: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   206: dup            
        //   207: ifnonnull       220
        //   210: new             Ljava/lang/NullPointerException;
        //   213: dup            
        //   214: ldc             "null cannot be cast to non-null type kotlin.Array<T>"
        //   216: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   219: athrow         
        //   220: checkcast       [Ljava/lang/String;
        //   223: astore_3        /* pairArray */
        //   224: aload_3         /* pairArray */
        //   225: astore          4
        //   227: iconst_0       
        //   228: istore          5
        //   230: aload           4
        //   232: arraylength    
        //   233: istore          6
        //   235: iload           5
        //   237: iload           6
        //   239: if_icmpge       956
        //   242: aload           4
        //   244: iload           5
        //   246: aaload         
        //   247: astore          pair
        //   249: iinc            5, 1
        //   252: aload           pair
        //   254: checkcast       Ljava/lang/CharSequence;
        //   257: astore          9
        //   259: ldc             "="
        //   261: astore          10
        //   263: iconst_0       
        //   264: istore          11
        //   266: new             Lkotlin/text/Regex;
        //   269: dup            
        //   270: aload           10
        //   272: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;)V
        //   275: astore          10
        //   277: iconst_0       
        //   278: istore          11
        //   280: iconst_0       
        //   281: istore          12
        //   283: aload           10
        //   285: aload           9
        //   287: iload           11
        //   289: invokevirtual   kotlin/text/Regex.split:(Ljava/lang/CharSequence;I)Ljava/util/List;
        //   292: astore          9
        //   294: nop            
        //   295: iconst_0       
        //   296: istore          $i$f$dropLastWhile
        //   298: aload           $this$dropLastWhile$iv
        //   300: invokeinterface java/util/List.isEmpty:()Z
        //   305: ifne            401
        //   308: aload           $this$dropLastWhile$iv
        //   310: aload           $this$dropLastWhile$iv
        //   312: invokeinterface java/util/List.size:()I
        //   317: invokeinterface java/util/List.listIterator:(I)Ljava/util/ListIterator;
        //   322: astore          iterator$iv
        //   324: aload           iterator$iv
        //   326: invokeinterface java/util/ListIterator.hasPrevious:()Z
        //   331: ifeq            401
        //   334: aload           iterator$iv
        //   336: invokeinterface java/util/ListIterator.previous:()Ljava/lang/Object;
        //   341: checkcast       Ljava/lang/String;
        //   344: astore          it
        //   346: iconst_0       
        //   347: istore          $i$a$-dropLastWhile-CookieStore$cookieToMap$pairs$1
        //   349: aload           it
        //   351: checkcast       Ljava/lang/CharSequence;
        //   354: astore          14
        //   356: iconst_0       
        //   357: istore          15
        //   359: aload           14
        //   361: invokeinterface java/lang/CharSequence.length:()I
        //   366: ifne            373
        //   369: iconst_1       
        //   370: goto            374
        //   373: iconst_0       
        //   374: nop            
        //   375: ifne            398
        //   378: aload           $this$dropLastWhile$iv
        //   380: checkcast       Ljava/lang/Iterable;
        //   383: aload           iterator$iv
        //   385: invokeinterface java/util/ListIterator.nextIndex:()I
        //   390: iconst_1       
        //   391: iadd           
        //   392: invokestatic    kotlin/collections/CollectionsKt.take:(Ljava/lang/Iterable;I)Ljava/util/List;
        //   395: goto            404
        //   398: goto            324
        //   401: invokestatic    kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
        //   404: checkcast       Ljava/util/Collection;
        //   407: astore          9
        //   409: nop            
        //   410: iconst_0       
        //   411: istore          $i$f$toTypedArray
        //   413: aload           $this$toTypedArray$iv
        //   415: astore          thisCollection$iv
        //   417: aload           thisCollection$iv
        //   419: iconst_0       
        //   420: anewarray       Ljava/lang/String;
        //   423: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   428: dup            
        //   429: ifnonnull       442
        //   432: new             Ljava/lang/NullPointerException;
        //   435: dup            
        //   436: ldc             "null cannot be cast to non-null type kotlin.Array<T>"
        //   438: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   441: athrow         
        //   442: checkcast       [Ljava/lang/String;
        //   445: astore          pairs
        //   447: aload           pairs
        //   449: arraylength    
        //   450: iconst_1       
        //   451: if_icmpne       457
        //   454: goto            235
        //   457: aload           pairs
        //   459: iconst_0       
        //   460: aaload         
        //   461: astore          $this$trim$iv
        //   463: iconst_0       
        //   464: istore          $i$f$trim
        //   466: aload           $this$trim$iv
        //   468: checkcast       Ljava/lang/CharSequence;
        //   471: astore          $this$trim$iv$iv
        //   473: iconst_0       
        //   474: istore          $i$f$trim
        //   476: iconst_0       
        //   477: istore          startIndex$iv$iv
        //   479: aload           $this$trim$iv$iv
        //   481: invokeinterface java/lang/CharSequence.length:()I
        //   486: iconst_1       
        //   487: isub           
        //   488: istore          endIndex$iv$iv
        //   490: iconst_0       
        //   491: istore          startFound$iv$iv
        //   493: iload           startIndex$iv$iv
        //   495: iload           endIndex$iv$iv
        //   497: if_icmpgt       587
        //   500: iload           startFound$iv$iv
        //   502: ifne            510
        //   505: iload           startIndex$iv$iv
        //   507: goto            512
        //   510: iload           endIndex$iv$iv
        //   512: istore          index$iv$iv
        //   514: aload           $this$trim$iv$iv
        //   516: iload           index$iv$iv
        //   518: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   523: istore          it
        //   525: iconst_0       
        //   526: istore          $i$a$-trim-CookieStore$cookieToMap$key$1
        //   528: iload           it
        //   530: bipush          32
        //   532: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   535: ifgt            542
        //   538: iconst_1       
        //   539: goto            543
        //   542: iconst_0       
        //   543: istore          match$iv$iv
        //   545: iload           startFound$iv$iv
        //   547: ifne            570
        //   550: iload           match$iv$iv
        //   552: ifne            561
        //   555: iconst_1       
        //   556: istore          startFound$iv$iv
        //   558: goto            584
        //   561: iload           startIndex$iv$iv
        //   563: iconst_1       
        //   564: iadd           
        //   565: istore          startIndex$iv$iv
        //   567: goto            584
        //   570: iload           match$iv$iv
        //   572: ifne            578
        //   575: goto            587
        //   578: iload           endIndex$iv$iv
        //   580: iconst_1       
        //   581: isub           
        //   582: istore          endIndex$iv$iv
        //   584: goto            493
        //   587: aload           $this$trim$iv$iv
        //   589: iload           startIndex$iv$iv
        //   591: iload           endIndex$iv$iv
        //   593: iconst_1       
        //   594: iadd           
        //   595: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   600: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   603: astore          key
        //   605: aload           pairs
        //   607: iconst_1       
        //   608: aaload         
        //   609: astore          value
        //   611: aload           value
        //   613: checkcast       Ljava/lang/CharSequence;
        //   616: astore          11
        //   618: iconst_0       
        //   619: istore          12
        //   621: aload           11
        //   623: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   626: ifne            633
        //   629: iconst_1       
        //   630: goto            634
        //   633: iconst_0       
        //   634: ifne            789
        //   637: aload           value
        //   639: astore          $this$trim$iv
        //   641: iconst_0       
        //   642: istore          $i$f$trim
        //   644: aload           $this$trim$iv
        //   646: checkcast       Ljava/lang/CharSequence;
        //   649: astore          $this$trim$iv$iv
        //   651: iconst_0       
        //   652: istore          $i$f$trim
        //   654: iconst_0       
        //   655: istore          startIndex$iv$iv
        //   657: aload           $this$trim$iv$iv
        //   659: invokeinterface java/lang/CharSequence.length:()I
        //   664: iconst_1       
        //   665: isub           
        //   666: istore          endIndex$iv$iv
        //   668: iconst_0       
        //   669: istore          startFound$iv$iv
        //   671: iload           startIndex$iv$iv
        //   673: iload           endIndex$iv$iv
        //   675: if_icmpgt       765
        //   678: iload           startFound$iv$iv
        //   680: ifne            688
        //   683: iload           startIndex$iv$iv
        //   685: goto            690
        //   688: iload           endIndex$iv$iv
        //   690: istore          index$iv$iv
        //   692: aload           $this$trim$iv$iv
        //   694: iload           index$iv$iv
        //   696: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   701: istore          it
        //   703: iconst_0       
        //   704: istore          $i$a$-trim-CookieStore$cookieToMap$1
        //   706: iload           it
        //   708: bipush          32
        //   710: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   713: ifgt            720
        //   716: iconst_1       
        //   717: goto            721
        //   720: iconst_0       
        //   721: istore          match$iv$iv
        //   723: iload           startFound$iv$iv
        //   725: ifne            748
        //   728: iload           match$iv$iv
        //   730: ifne            739
        //   733: iconst_1       
        //   734: istore          startFound$iv$iv
        //   736: goto            762
        //   739: iload           startIndex$iv$iv
        //   741: iconst_1       
        //   742: iadd           
        //   743: istore          startIndex$iv$iv
        //   745: goto            762
        //   748: iload           match$iv$iv
        //   750: ifne            756
        //   753: goto            765
        //   756: iload           endIndex$iv$iv
        //   758: iconst_1       
        //   759: isub           
        //   760: istore          endIndex$iv$iv
        //   762: goto            671
        //   765: aload           $this$trim$iv$iv
        //   767: iload           startIndex$iv$iv
        //   769: iload           endIndex$iv$iv
        //   771: iconst_1       
        //   772: iadd           
        //   773: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   778: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   781: ldc             "null"
        //   783: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   786: ifeq            235
        //   789: aload_2         /* cookieMap */
        //   790: astore          11
        //   792: aload           value
        //   794: astore          $this$trim$iv
        //   796: iconst_0       
        //   797: istore          $i$f$trim
        //   799: aload           $this$trim$iv
        //   801: checkcast       Ljava/lang/CharSequence;
        //   804: astore          $this$trim$iv$iv
        //   806: iconst_0       
        //   807: istore          $i$f$trim
        //   809: iconst_0       
        //   810: istore          startIndex$iv$iv
        //   812: aload           $this$trim$iv$iv
        //   814: invokeinterface java/lang/CharSequence.length:()I
        //   819: iconst_1       
        //   820: isub           
        //   821: istore          endIndex$iv$iv
        //   823: iconst_0       
        //   824: istore          startFound$iv$iv
        //   826: iload           startIndex$iv$iv
        //   828: iload           endIndex$iv$iv
        //   830: if_icmpgt       920
        //   833: iload           startFound$iv$iv
        //   835: ifne            843
        //   838: iload           startIndex$iv$iv
        //   840: goto            845
        //   843: iload           endIndex$iv$iv
        //   845: istore          index$iv$iv
        //   847: aload           $this$trim$iv$iv
        //   849: iload           index$iv$iv
        //   851: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   856: istore          it
        //   858: iconst_0       
        //   859: istore          $i$a$-trim-CookieStore$cookieToMap$2
        //   861: iload           it
        //   863: bipush          32
        //   865: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   868: ifgt            875
        //   871: iconst_1       
        //   872: goto            876
        //   875: iconst_0       
        //   876: istore          match$iv$iv
        //   878: iload           startFound$iv$iv
        //   880: ifne            903
        //   883: iload           match$iv$iv
        //   885: ifne            894
        //   888: iconst_1       
        //   889: istore          startFound$iv$iv
        //   891: goto            917
        //   894: iload           startIndex$iv$iv
        //   896: iconst_1       
        //   897: iadd           
        //   898: istore          startIndex$iv$iv
        //   900: goto            917
        //   903: iload           match$iv$iv
        //   905: ifne            911
        //   908: goto            920
        //   911: iload           endIndex$iv$iv
        //   913: iconst_1       
        //   914: isub           
        //   915: istore          endIndex$iv$iv
        //   917: goto            826
        //   920: aload           $this$trim$iv$iv
        //   922: iload           startIndex$iv$iv
        //   924: iload           endIndex$iv$iv
        //   926: iconst_1       
        //   927: iadd           
        //   928: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   933: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   936: astore          null
        //   938: iconst_0       
        //   939: istore          13
        //   941: aload           11
        //   943: aload           key
        //   945: aload           12
        //   947: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   952: pop            
        //   953: goto            235
        //   956: aload_2         /* cookieMap */
        //   957: areturn        
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  cookie  
        //    StackMapTable: 00 33 FD 00 1F 07 00 7A 01 FE 00 46 07 00 A9 01 07 00 B5 FF 00 30 00 0B 07 00 02 07 00 25 07 00 7A 01 07 00 A9 01 07 00 B5 07 00 25 01 07 00 5E 01 00 00 40 01 17 FF 00 02 00 06 07 00 02 07 00 25 07 00 7A 01 07 00 A9 01 00 00 42 07 00 A9 FF 00 25 00 07 07 00 02 07 00 25 07 00 7A 01 07 00 CD 01 07 00 CD 00 01 07 01 13 FF 00 0E 00 07 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 00 00 FF 00 58 00 0C 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 00 07 00 A9 01 07 00 B5 00 00 FF 00 30 00 10 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 00 07 00 A9 01 07 00 B5 07 00 25 01 07 00 5E 01 00 00 40 01 17 FF 00 02 00 0B 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 00 07 00 A9 01 00 00 42 07 00 A9 FF 00 25 00 0C 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 00 07 00 CD 01 07 00 CD 00 01 07 01 13 FF 00 0E 00 0C 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 07 00 D8 07 00 CD 01 07 00 CD 00 00 FF 00 23 00 11 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 07 00 D8 07 00 CD 07 00 25 01 07 00 5E 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 2D 00 11 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 07 00 D8 07 00 25 07 00 25 07 00 5E 01 01 01 01 01 00 00 40 01 FF 00 24 00 12 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 07 00 D8 07 00 25 07 00 25 07 00 25 01 07 00 5E 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 17 00 11 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 07 00 D8 07 00 25 07 00 25 07 00 04 01 00 01 01 01 00 00 FF 00 24 00 13 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 07 00 25 07 00 D8 07 00 25 07 00 25 07 00 7A 07 00 25 01 07 00 5E 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 23 00 07 07 00 02 07 00 25 07 00 7A 07 00 D8 07 00 D8 01 01 00 00
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
    public String mapToCookie(@Nullable final Map<String, String> cookieMap) {
        if (cookieMap == null || cookieMap.isEmpty()) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        for (final String key : cookieMap.keySet()) {
            final String s;
            final String value = s = cookieMap.get(key);
            if (s != null && !StringsKt.isBlank((CharSequence)s)) {
                builder.append(key).append("=").append(value).append(";");
            }
        }
        return builder.deleteCharAt(builder.lastIndexOf(";")).toString();
    }
    
    public final void clear() {
        this.cacheInstance.clear();
    }
}
