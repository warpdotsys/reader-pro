// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import kotlin.text.Regex;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.constant.AppPattern;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002\u001a\f\u0010\u0004\u001a\u00020\u0002*\u0004\u0018\u00010\u0002\u001a\f\u0010\u0005\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\u0007\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\b\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\t\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\n\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\u0016\u0010\u000b\u001a\u00020\u0006*\u0004\u0018\u00010\u00022\b\b\u0002\u0010\f\u001a\u00020\u0006\u001a\f\u0010\r\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\u000e\u0010\u000e\u001a\u0004\u0018\u00010\u0002*\u0004\u0018\u00010\u0002\u001a)\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0011\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0010\"\u00020\u0002?\u0006\u0002\u0010\u0012\u001a'\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0001?\u0006\u0002\u0010\u0016\u001a\u0012\u0010\u0017\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002\u001a\u0015\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u0002?\u0006\u0002\u0010\u001a¡§\u0006\u001b" }, d2 = { "cnCompare", "", "", "other", "htmlFormat", "isAbsUrl", "", "isDataUrl", "isJson", "isJsonArray", "isJsonObject", "isTrue", "nullIsTrue", "isXml", "safeTrim", "splitNotBlank", "", "delimiter", "(Ljava/lang/String;[Ljava/lang/String;)[Ljava/lang/String;", "regex", "Lkotlin/text/Regex;", "limit", "(Ljava/lang/String;Lkotlin/text/Regex;I)[Ljava/lang/String;", "startWithIgnoreCase", "start", "toStringArray", "(Ljava/lang/String;)[Ljava/lang/String;", "reader-pro" })
public final class StringExtensionsKt
{
    @Nullable
    public static final String safeTrim(@Nullable final String $this$safeTrim) {
        final CharSequence charSequence = $this$safeTrim;
        String string;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            string = null;
        }
        else {
            if ($this$safeTrim == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            string = StringsKt.trim((CharSequence)$this$safeTrim).toString();
        }
        return string;
    }
    
    public static final boolean isAbsUrl(@Nullable final String $this$isAbsUrl) {
        final CharSequence charSequence = $this$isAbsUrl;
        return charSequence != null && !StringsKt.isBlank(charSequence) && (StringsKt.startsWith($this$isAbsUrl, "http://", true) || StringsKt.startsWith($this$isAbsUrl, "https://", true));
    }
    
    public static final boolean isDataUrl(@Nullable final String $this$isDataUrl) {
        boolean matches;
        if ($this$isDataUrl == null) {
            matches = false;
        }
        else {
            final String it = $this$isDataUrl;
            final int n = 0;
            matches = AppPattern.INSTANCE.getDataUriRegex().matches((CharSequence)it);
        }
        return matches;
    }
    
    public static final boolean isJson(@Nullable final String $this$isJson) {
        boolean b;
        if ($this$isJson == null) {
            b = false;
        }
        else {
            final String $this$isJson_u24lambda_u2d1 = $this$isJson;
            final int n = 0;
            final String str = StringsKt.trim((CharSequence)$this$isJson_u24lambda_u2d1).toString();
            b = ((StringsKt.startsWith$default(str, "{", false, 2, (Object)null) && StringsKt.endsWith$default(str, "}", false, 2, (Object)null)) || (StringsKt.startsWith$default(str, "[", false, 2, (Object)null) && StringsKt.endsWith$default(str, "]", false, 2, (Object)null)));
        }
        return b;
    }
    
    public static final boolean isJsonObject(@Nullable final String $this$isJsonObject) {
        boolean b;
        if ($this$isJsonObject == null) {
            b = false;
        }
        else {
            final String $this$isJsonObject_u24lambda_u2d2 = $this$isJsonObject;
            final int n = 0;
            final String str = StringsKt.trim((CharSequence)$this$isJsonObject_u24lambda_u2d2).toString();
            b = (StringsKt.startsWith$default(str, "{", false, 2, (Object)null) && StringsKt.endsWith$default(str, "}", false, 2, (Object)null));
        }
        return b;
    }
    
    public static final boolean isJsonArray(@Nullable final String $this$isJsonArray) {
        boolean b;
        if ($this$isJsonArray == null) {
            b = false;
        }
        else {
            final String $this$isJsonArray_u24lambda_u2d3 = $this$isJsonArray;
            final int n = 0;
            final String str = StringsKt.trim((CharSequence)$this$isJsonArray_u24lambda_u2d3).toString();
            b = (StringsKt.startsWith$default(str, "[", false, 2, (Object)null) && StringsKt.endsWith$default(str, "]", false, 2, (Object)null));
        }
        return b;
    }
    
    public static final boolean isXml(@Nullable final String $this$isXml) {
        boolean b;
        if ($this$isXml == null) {
            b = false;
        }
        else {
            final String $this$isXml_u24lambda_u2d4 = $this$isXml;
            final int n = 0;
            final String str = StringsKt.trim((CharSequence)$this$isXml_u24lambda_u2d4).toString();
            b = (StringsKt.startsWith$default(str, "<", false, 2, (Object)null) && StringsKt.endsWith$default(str, ">", false, 2, (Object)null));
        }
        return b;
    }
    
    public static final boolean isTrue(@Nullable final String $this$isTrue, final boolean nullIsTrue) {
        final CharSequence charSequence = $this$isTrue;
        if (charSequence == null || StringsKt.isBlank(charSequence) || Intrinsics.areEqual((Object)$this$isTrue, (Object)"null")) {
            return nullIsTrue;
        }
        return !new Regex("\\s*(?i)(false|no|not|0)\\s*").matches((CharSequence)$this$isTrue);
    }
    
    @NotNull
    public static final String htmlFormat(@Nullable final String $this$htmlFormat) {
        final CharSequence charSequence = $this$htmlFormat;
        return (charSequence == null || StringsKt.isBlank(charSequence)) ? "" : new Regex("[\\n\\s]+$").replace((CharSequence)new Regex("^[\\n\\s]+").replace((CharSequence)new Regex("\\s*\\n+\\s*").replace((CharSequence)new Regex("<[script>]*.*?>|&nbsp;").replace((CharSequence)new Regex("(?i)<(br[\\s/]*|/*p\\b.*?|/*div\\b.*?)>").replace((CharSequence)$this$htmlFormat, "\n"), ""), "\n\u3000\u3000"), "\u3000\u3000"), "");
    }
    
    @NotNull
    public static final String[] splitNotBlank(@NotNull final String $this$splitNotBlank, @NotNull final String... delimiter) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "<this>"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* delimiter */
        //     7: ldc             "delimiter"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $this$splitNotBlank */
        //    13: astore_2       
        //    14: iconst_0       
        //    15: istore_3       
        //    16: iconst_0       
        //    17: istore          4
        //    19: aload_2        
        //    20: astore          $this$splitNotBlank_u24lambda_u2d7
        //    22: iconst_0       
        //    23: istore          $i$a$-run-StringExtensionsKt$splitNotBlank$1
        //    25: aload           $this$splitNotBlank_u24lambda_u2d7
        //    27: checkcast       Ljava/lang/CharSequence;
        //    30: aload_1         /* delimiter */
        //    31: aload_1         /* delimiter */
        //    32: arraylength    
        //    33: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //    36: checkcast       [Ljava/lang/String;
        //    39: iconst_0       
        //    40: iconst_0       
        //    41: bipush          6
        //    43: aconst_null    
        //    44: invokestatic    kotlin/text/StringsKt.split$default:(Ljava/lang/CharSequence;[Ljava/lang/String;ZIILjava/lang/Object;)Ljava/util/List;
        //    47: checkcast       Ljava/lang/Iterable;
        //    50: astore          $this$map$iv
        //    52: iconst_0       
        //    53: istore          $i$f$map
        //    55: aload           $this$map$iv
        //    57: astore          9
        //    59: new             Ljava/util/ArrayList;
        //    62: dup            
        //    63: aload           $this$map$iv
        //    65: bipush          10
        //    67: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //    70: invokespecial   java/util/ArrayList.<init>:(I)V
        //    73: checkcast       Ljava/util/Collection;
        //    76: astore          destination$iv$iv
        //    78: iconst_0       
        //    79: istore          $i$f$mapTo
        //    81: aload           $this$mapTo$iv$iv
        //    83: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    88: astore          12
        //    90: aload           12
        //    92: invokeinterface java/util/Iterator.hasNext:()Z
        //    97: ifeq            171
        //   100: aload           12
        //   102: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   107: astore          item$iv$iv
        //   109: aload           destination$iv$iv
        //   111: aload           item$iv$iv
        //   113: checkcast       Ljava/lang/String;
        //   116: astore          14
        //   118: astore          15
        //   120: iconst_0       
        //   121: istore          $i$a$-map-StringExtensionsKt$splitNotBlank$1$1
        //   123: aload           it
        //   125: astore          17
        //   127: iconst_0       
        //   128: istore          18
        //   130: aload           17
        //   132: dup            
        //   133: ifnonnull       146
        //   136: new             Ljava/lang/NullPointerException;
        //   139: dup            
        //   140: ldc             "null cannot be cast to non-null type kotlin.CharSequence"
        //   142: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   145: athrow         
        //   146: checkcast       Ljava/lang/CharSequence;
        //   149: invokestatic    kotlin/text/StringsKt.trim:(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;
        //   152: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   155: nop            
        //   156: astore          19
        //   158: aload           15
        //   160: aload           19
        //   162: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   167: pop            
        //   168: goto            90
        //   171: aload           destination$iv$iv
        //   173: checkcast       Ljava/util/List;
        //   176: nop            
        //   177: checkcast       Ljava/lang/Iterable;
        //   180: astore          7
        //   182: nop            
        //   183: iconst_0       
        //   184: istore          $i$f$filterNot
        //   186: aload           $this$filterNot$iv
        //   188: astore          9
        //   190: new             Ljava/util/ArrayList;
        //   193: dup            
        //   194: invokespecial   java/util/ArrayList.<init>:()V
        //   197: checkcast       Ljava/util/Collection;
        //   200: astore          destination$iv$iv
        //   202: iconst_0       
        //   203: istore          $i$f$filterNotTo
        //   205: aload           $this$filterNotTo$iv$iv
        //   207: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   212: astore          12
        //   214: aload           12
        //   216: invokeinterface java/util/Iterator.hasNext:()Z
        //   221: ifeq            267
        //   224: aload           12
        //   226: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   231: astore          element$iv$iv
        //   233: aload           element$iv$iv
        //   235: checkcast       Ljava/lang/String;
        //   238: astore          it
        //   240: iconst_0       
        //   241: istore          $i$a$-filterNot-StringExtensionsKt$splitNotBlank$1$2
        //   243: aload           it
        //   245: checkcast       Ljava/lang/CharSequence;
        //   248: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   251: ifne            214
        //   254: aload           destination$iv$iv
        //   256: aload           element$iv$iv
        //   258: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   263: pop            
        //   264: goto            214
        //   267: aload           destination$iv$iv
        //   269: checkcast       Ljava/util/List;
        //   272: nop            
        //   273: checkcast       Ljava/util/Collection;
        //   276: astore          7
        //   278: nop            
        //   279: iconst_0       
        //   280: istore          $i$f$toTypedArray
        //   282: aload           $this$toTypedArray$iv
        //   284: astore          thisCollection$iv
        //   286: aload           thisCollection$iv
        //   288: iconst_0       
        //   289: anewarray       Ljava/lang/String;
        //   292: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   297: dup            
        //   298: ifnonnull       311
        //   301: new             Ljava/lang/NullPointerException;
        //   304: dup            
        //   305: ldc             "null cannot be cast to non-null type kotlin.Array<T>"
        //   307: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   310: athrow         
        //   311: checkcast       [Ljava/lang/String;
        //   314: nop            
        //   315: nop            
        //   316: areturn        
        //    MethodParameters:
        //  Name                 Flags     
        //  -------------------  --------
        //  $this$splitNotBlank  MANDATED
        //  delimiter            
        //    StackMapTable: 00 06 FF 00 5A 00 0D 07 00 23 07 00 A6 07 00 23 01 01 07 00 23 01 07 00 AC 01 07 00 AC 07 00 B9 01 07 00 BF 00 00 FF 00 37 00 13 07 00 23 07 00 A6 07 00 23 01 01 07 00 23 01 07 00 AC 01 07 00 AC 07 00 B9 01 07 00 BF 07 00 04 07 00 23 07 00 B9 01 07 00 23 01 00 01 07 00 23 FF 00 18 00 0D 07 00 23 07 00 A6 07 00 23 01 01 07 00 23 01 07 00 AC 01 07 00 AC 07 00 B9 01 07 00 BF 00 00 2A 34 FF 00 2B 00 0D 07 00 23 07 00 A6 07 00 23 01 01 07 00 23 01 07 00 B9 01 07 00 B9 07 00 B9 01 07 00 BF 00 01 07 00 ED
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String[] splitNotBlank(@NotNull final String $this$splitNotBlank, @NotNull final Regex regex, final int limit) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "<this>"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: aload_1         /* regex */
        //     7: ldc             "regex"
        //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    12: aload_0         /* $this$splitNotBlank */
        //    13: astore_3       
        //    14: iconst_0       
        //    15: istore          4
        //    17: iconst_0       
        //    18: istore          5
        //    20: aload_3        
        //    21: astore          $this$splitNotBlank_u24lambda_u2d10
        //    23: iconst_0       
        //    24: istore          $i$a$-run-StringExtensionsKt$splitNotBlank$2
        //    26: aload           $this$splitNotBlank_u24lambda_u2d10
        //    28: checkcast       Ljava/lang/CharSequence;
        //    31: astore          8
        //    33: iconst_0       
        //    34: istore          9
        //    36: aload_1         /* regex */
        //    37: aload           8
        //    39: iload_2         /* limit */
        //    40: invokevirtual   kotlin/text/Regex.split:(Ljava/lang/CharSequence;I)Ljava/util/List;
        //    43: checkcast       Ljava/lang/Iterable;
        //    46: astore          8
        //    48: nop            
        //    49: iconst_0       
        //    50: istore          $i$f$map
        //    52: aload           $this$map$iv
        //    54: astore          10
        //    56: new             Ljava/util/ArrayList;
        //    59: dup            
        //    60: aload           $this$map$iv
        //    62: bipush          10
        //    64: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //    67: invokespecial   java/util/ArrayList.<init>:(I)V
        //    70: checkcast       Ljava/util/Collection;
        //    73: astore          destination$iv$iv
        //    75: iconst_0       
        //    76: istore          $i$f$mapTo
        //    78: aload           $this$mapTo$iv$iv
        //    80: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    85: astore          13
        //    87: aload           13
        //    89: invokeinterface java/util/Iterator.hasNext:()Z
        //    94: ifeq            168
        //    97: aload           13
        //    99: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   104: astore          item$iv$iv
        //   106: aload           destination$iv$iv
        //   108: aload           item$iv$iv
        //   110: checkcast       Ljava/lang/String;
        //   113: astore          15
        //   115: astore          16
        //   117: iconst_0       
        //   118: istore          $i$a$-map-StringExtensionsKt$splitNotBlank$2$1
        //   120: aload           it
        //   122: astore          18
        //   124: iconst_0       
        //   125: istore          19
        //   127: aload           18
        //   129: dup            
        //   130: ifnonnull       143
        //   133: new             Ljava/lang/NullPointerException;
        //   136: dup            
        //   137: ldc             "null cannot be cast to non-null type kotlin.CharSequence"
        //   139: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   142: athrow         
        //   143: checkcast       Ljava/lang/CharSequence;
        //   146: invokestatic    kotlin/text/StringsKt.trim:(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;
        //   149: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   152: nop            
        //   153: astore          20
        //   155: aload           16
        //   157: aload           20
        //   159: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   164: pop            
        //   165: goto            87
        //   168: aload           destination$iv$iv
        //   170: checkcast       Ljava/util/List;
        //   173: nop            
        //   174: checkcast       Ljava/lang/Iterable;
        //   177: astore          8
        //   179: nop            
        //   180: iconst_0       
        //   181: istore          $i$f$filterNot
        //   183: aload           $this$filterNot$iv
        //   185: astore          10
        //   187: new             Ljava/util/ArrayList;
        //   190: dup            
        //   191: invokespecial   java/util/ArrayList.<init>:()V
        //   194: checkcast       Ljava/util/Collection;
        //   197: astore          destination$iv$iv
        //   199: iconst_0       
        //   200: istore          $i$f$filterNotTo
        //   202: aload           $this$filterNotTo$iv$iv
        //   204: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   209: astore          13
        //   211: aload           13
        //   213: invokeinterface java/util/Iterator.hasNext:()Z
        //   218: ifeq            264
        //   221: aload           13
        //   223: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   228: astore          element$iv$iv
        //   230: aload           element$iv$iv
        //   232: checkcast       Ljava/lang/String;
        //   235: astore          it
        //   237: iconst_0       
        //   238: istore          $i$a$-filterNot-StringExtensionsKt$splitNotBlank$2$2
        //   240: aload           it
        //   242: checkcast       Ljava/lang/CharSequence;
        //   245: invokestatic    kotlin/text/StringsKt.isBlank:(Ljava/lang/CharSequence;)Z
        //   248: ifne            211
        //   251: aload           destination$iv$iv
        //   253: aload           element$iv$iv
        //   255: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   260: pop            
        //   261: goto            211
        //   264: aload           destination$iv$iv
        //   266: checkcast       Ljava/util/List;
        //   269: nop            
        //   270: checkcast       Ljava/util/Collection;
        //   273: astore          8
        //   275: nop            
        //   276: iconst_0       
        //   277: istore          $i$f$toTypedArray
        //   279: aload           $this$toTypedArray$iv
        //   281: astore          thisCollection$iv
        //   283: aload           thisCollection$iv
        //   285: iconst_0       
        //   286: anewarray       Ljava/lang/String;
        //   289: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   294: dup            
        //   295: ifnonnull       308
        //   298: new             Ljava/lang/NullPointerException;
        //   301: dup            
        //   302: ldc             "null cannot be cast to non-null type kotlin.Array<T>"
        //   304: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   307: athrow         
        //   308: checkcast       [Ljava/lang/String;
        //   311: nop            
        //   312: nop            
        //   313: areturn        
        //    MethodParameters:
        //  Name                 Flags     
        //  -------------------  --------
        //  $this$splitNotBlank  MANDATED
        //  regex                
        //  limit                
        //    StackMapTable: 00 06 FF 00 57 00 0E 07 00 23 07 00 3C 01 07 00 23 01 01 07 00 23 01 07 00 AC 01 07 00 AC 07 00 B9 01 07 00 BF 00 00 FF 00 37 00 14 07 00 23 07 00 3C 01 07 00 23 01 01 07 00 23 01 07 00 AC 01 07 00 AC 07 00 B9 01 07 00 BF 07 00 04 07 00 23 07 00 B9 01 07 00 23 01 00 01 07 00 23 FF 00 18 00 0E 07 00 23 07 00 3C 01 07 00 23 01 01 07 00 23 01 07 00 AC 01 07 00 AC 07 00 B9 01 07 00 BF 00 00 2A 34 FF 00 2B 00 0E 07 00 23 07 00 3C 01 07 00 23 01 01 07 00 23 01 07 00 B9 01 07 00 B9 07 00 B9 01 07 00 BF 00 01 07 00 ED
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final boolean startWithIgnoreCase(@NotNull final String $this$startWithIgnoreCase, @NotNull final String start) {
        Intrinsics.checkNotNullParameter((Object)$this$startWithIgnoreCase, "<this>");
        Intrinsics.checkNotNullParameter((Object)start, "start");
        return !StringsKt.isBlank((CharSequence)$this$startWithIgnoreCase) && StringsKt.startsWith($this$startWithIgnoreCase, start, true);
    }
    
    public static final int cnCompare(@NotNull final String $this$cnCompare, @NotNull final String other) {
        Intrinsics.checkNotNullParameter((Object)$this$cnCompare, "<this>");
        Intrinsics.checkNotNullParameter((Object)other, "other");
        return $this$cnCompare.compareTo(other);
    }
    
    @NotNull
    public static final String[] toStringArray(@NotNull final String $this$toStringArray) {
        Intrinsics.checkNotNullParameter((Object)$this$toStringArray, "<this>");
        int codePointIndex = 0;
        String[] array3;
        try {
            int i = 0;
            final int codePointCount = $this$toStringArray.codePointCount(0, $this$toStringArray.length());
            final String[] array = new String[codePointCount];
            while (i < codePointCount) {
                final int n = i;
                final String[] array2 = array;
                final int n2 = n;
                final int start = codePointIndex;
                codePointIndex = $this$toStringArray.offsetByCodePoints(start, 1);
                final String substring = $this$toStringArray.substring(start, codePointIndex);
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                array2[n2] = substring;
                ++i;
            }
            array3 = array;
        }
        catch (final Exception e) {
            final Collection $this$toTypedArray$iv = StringsKt.split$default((CharSequence)$this$toStringArray, new String[] { "" }, false, 0, 6, (Object)null);
            final int $i$f$toTypedArray = 0;
            final Collection thisCollection$iv = $this$toTypedArray$iv;
            final String[] array4 = thisCollection$iv.toArray(new String[0]);
            if (array4 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            array3 = array4;
        }
        return array3;
    }
}
