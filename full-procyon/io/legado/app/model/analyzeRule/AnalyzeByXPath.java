// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import io.legado.app.utils.TextUtils;
import kotlin.jvm.functions.Function1;
import kotlin.collections.CollectionsKt;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.Collection;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.ArrayList;
import java.util.List;
import kotlin.text.StringsKt;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXNode;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001?\u0006\u0002\u0010\u0003J\u001d\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\tH\u0000?\u0006\u0002\b\nJ\u0018\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\tH\u0002J\u0010\u0010\f\u001a\u0004\u0018\u00010\t2\u0006\u0010\r\u001a\u00020\tJ\u001b\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010\b\u001a\u00020\tH\u0000?\u0006\u0002\b\u000fJ\u0010\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u0014" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByXPath;", "", "doc", "(Ljava/lang/Object;)V", "jxNode", "getElements", "", "Lorg/seimicrawler/xpath/JXNode;", "xPath", "", "getElements$reader_pro", "getResult", "getString", "rule", "getStringList", "getStringList$reader_pro", "parse", "strToJXDocument", "Lorg/seimicrawler/xpath/JXDocument;", "html", "reader-pro" })
public final class AnalyzeByXPath
{
    @NotNull
    private Object jxNode;
    
    public AnalyzeByXPath(@NotNull final Object doc) {
        Intrinsics.checkNotNullParameter(doc, "doc");
        this.jxNode = this.parse(doc);
    }
    
    private final Object parse(final Object doc) {
        Object strToJXDocument;
        if (doc instanceof JXNode) {
            strToJXDocument = (((JXNode)doc).isElement() ? doc : this.strToJXDocument(doc.toString()));
        }
        else if (doc instanceof Document) {
            final JXDocument create = JXDocument.create((Document)doc);
            Intrinsics.checkNotNullExpressionValue((Object)create, "create(doc)");
            strToJXDocument = create;
        }
        else if (doc instanceof Element) {
            final JXDocument create2 = JXDocument.create(new Elements(new Element[] { (Element)doc }));
            Intrinsics.checkNotNullExpressionValue((Object)create2, "create(Elements(doc))");
            strToJXDocument = create2;
        }
        else if (doc instanceof Elements) {
            final JXDocument create3 = JXDocument.create((Elements)doc);
            Intrinsics.checkNotNullExpressionValue((Object)create3, "create(doc)");
            strToJXDocument = create3;
        }
        else {
            strToJXDocument = this.strToJXDocument(doc.toString());
        }
        return strToJXDocument;
    }
    
    private final JXDocument strToJXDocument(final String html) {
        String html2 = html;
        if (StringsKt.endsWith$default(html2, "</td>", false, 2, (Object)null)) {
            html2 = "<tr>" + html2 + "</tr>";
        }
        if (StringsKt.endsWith$default(html2, "</tr>", false, 2, (Object)null) || StringsKt.endsWith$default(html2, "</tbody>", false, 2, (Object)null)) {
            html2 = "<table>" + html2 + "</table>";
        }
        final JXDocument create = JXDocument.create(html2);
        Intrinsics.checkNotNullExpressionValue((Object)create, "create(html1)");
        return create;
    }
    
    private final List<JXNode> getResult(final String xPath) {
        final Object node = this.jxNode;
        return (node instanceof JXNode) ? ((JXNode)node).sel(xPath) : ((JXDocument)node).selN(xPath);
    }
    
    @Nullable
    public final List<JXNode> getElements$reader_pro(@NotNull final String xPath) {
        Intrinsics.checkNotNullParameter((Object)xPath, "xPath");
        if (xPath.length() == 0) {
            return null;
        }
        final ArrayList jxNodes = new ArrayList();
        final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(xPath, false, 2, null);
        final ArrayList rules = ruleAnalyzes.splitRule("&&", "||", "%%");
        if (rules.size() == 1) {
            final String value = rules.get(0);
            Intrinsics.checkNotNullExpressionValue((Object)value, "rules[0]");
            return this.getResult(value);
        }
        final ArrayList results = new ArrayList();
        for (final String rl : rules) {
            Intrinsics.checkNotNullExpressionValue((Object)rl, "rl");
            final List temp = this.getElements$reader_pro(rl);
            if (temp != null && !temp.isEmpty()) {
                results.add(temp);
                if (!temp.isEmpty() && Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                    break;
                }
                continue;
            }
        }
        if (results.size() > 0) {
            if (Intrinsics.areEqual((Object)"%%", (Object)ruleAnalyzes.getElementsType())) {
                int j = 0;
                final int n = results.get(0).size() - 1;
                if (j <= n) {
                    do {
                        final int i = j;
                        ++j;
                        for (final List temp2 : results) {
                            if (i < temp2.size()) {
                                jxNodes.add(temp2.get(i));
                            }
                        }
                    } while (j <= n);
                }
            }
            else {
                for (final List temp3 : results) {
                    jxNodes.addAll(temp3);
                }
            }
        }
        return jxNodes;
    }
    
    @NotNull
    public final List<String> getStringList$reader_pro(@NotNull final String xPath) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "xPath"
        //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     6: new             Ljava/util/ArrayList;
        //     9: dup            
        //    10: invokespecial   java/util/ArrayList.<init>:()V
        //    13: astore_2        /* result */
        //    14: new             Lio/legado/app/model/analyzeRule/RuleAnalyzer;
        //    17: dup            
        //    18: aload_1         /* xPath */
        //    19: iconst_0       
        //    20: iconst_2       
        //    21: aconst_null    
        //    22: invokespecial   io/legado/app/model/analyzeRule/RuleAnalyzer.<init>:(Ljava/lang/String;ZILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //    25: astore_3        /* ruleAnalyzes */
        //    26: aload_3         /* ruleAnalyzes */
        //    27: iconst_3       
        //    28: anewarray       Ljava/lang/String;
        //    31: astore          5
        //    33: aload           5
        //    35: iconst_0       
        //    36: ldc             "&&"
        //    38: aastore        
        //    39: aload           5
        //    41: iconst_1       
        //    42: ldc             "||"
        //    44: aastore        
        //    45: aload           5
        //    47: iconst_2       
        //    48: ldc             "%%"
        //    50: aastore        
        //    51: aload           5
        //    53: invokevirtual   io/legado/app/model/analyzeRule/RuleAnalyzer.splitRule:([Ljava/lang/String;)Ljava/util/ArrayList;
        //    56: astore          rules
        //    58: aload           rules
        //    60: invokevirtual   java/util/ArrayList.size:()I
        //    63: iconst_1       
        //    64: if_icmpne       199
        //    67: aload_0         /* this */
        //    68: aload_1         /* xPath */
        //    69: invokespecial   io/legado/app/model/analyzeRule/AnalyzeByXPath.getResult:(Ljava/lang/String;)Ljava/util/List;
        //    72: astore          5
        //    74: aload           5
        //    76: ifnonnull       82
        //    79: goto            194
        //    82: aload           5
        //    84: checkcast       Ljava/lang/Iterable;
        //    87: astore          $this$map$iv
        //    89: iconst_0       
        //    90: istore          $i$f$map
        //    92: aload           $this$map$iv
        //    94: astore          8
        //    96: new             Ljava/util/ArrayList;
        //    99: dup            
        //   100: aload           $this$map$iv
        //   102: bipush          10
        //   104: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //   107: invokespecial   java/util/ArrayList.<init>:(I)V
        //   110: checkcast       Ljava/util/Collection;
        //   113: astore          destination$iv$iv
        //   115: iconst_0       
        //   116: istore          $i$f$mapTo
        //   118: aload           $this$mapTo$iv$iv
        //   120: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   125: astore          11
        //   127: aload           11
        //   129: invokeinterface java/util/Iterator.hasNext:()Z
        //   134: ifeq            187
        //   137: aload           11
        //   139: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   144: astore          item$iv$iv
        //   146: aload           destination$iv$iv
        //   148: aload           item$iv$iv
        //   150: checkcast       Lorg/seimicrawler/xpath/JXNode;
        //   153: astore          13
        //   155: astore          15
        //   157: iconst_0       
        //   158: istore          $i$a$-map-AnalyzeByXPath$getStringList$1
        //   160: aload_2         /* result */
        //   161: aload           it
        //   163: invokevirtual   org/seimicrawler/xpath/JXNode.asString:()Ljava/lang/String;
        //   166: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   169: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   172: astore          16
        //   174: aload           15
        //   176: aload           16
        //   178: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   183: pop            
        //   184: goto            127
        //   187: aload           destination$iv$iv
        //   189: checkcast       Ljava/util/List;
        //   192: pop            
        //   193: nop            
        //   194: aload_2         /* result */
        //   195: checkcast       Ljava/util/List;
        //   198: areturn        
        //   199: new             Ljava/util/ArrayList;
        //   202: dup            
        //   203: invokespecial   java/util/ArrayList.<init>:()V
        //   206: astore          results
        //   208: aload           rules
        //   210: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //   213: astore          6
        //   215: aload           6
        //   217: invokeinterface java/util/Iterator.hasNext:()Z
        //   222: ifeq            331
        //   225: aload           6
        //   227: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   232: checkcast       Ljava/lang/String;
        //   235: astore          rl
        //   237: aload_0         /* this */
        //   238: aload           rl
        //   240: ldc             "rl"
        //   242: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   245: aload           rl
        //   247: invokevirtual   io/legado/app/model/analyzeRule/AnalyzeByXPath.getStringList$reader_pro:(Ljava/lang/String;)Ljava/util/List;
        //   250: astore          temp
        //   252: aload           temp
        //   254: checkcast       Ljava/util/Collection;
        //   257: astore          9
        //   259: iconst_0       
        //   260: istore          10
        //   262: aload           9
        //   264: invokeinterface java/util/Collection.isEmpty:()Z
        //   269: ifne            276
        //   272: iconst_1       
        //   273: goto            277
        //   276: iconst_0       
        //   277: ifeq            215
        //   280: aload           results
        //   282: aload           temp
        //   284: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   287: pop            
        //   288: aload           temp
        //   290: checkcast       Ljava/util/Collection;
        //   293: astore          9
        //   295: iconst_0       
        //   296: istore          10
        //   298: aload           9
        //   300: invokeinterface java/util/Collection.isEmpty:()Z
        //   305: ifne            312
        //   308: iconst_1       
        //   309: goto            313
        //   312: iconst_0       
        //   313: ifeq            215
        //   316: aload_3         /* ruleAnalyzes */
        //   317: invokevirtual   io/legado/app/model/analyzeRule/RuleAnalyzer.getElementsType:()Ljava/lang/String;
        //   320: ldc             "||"
        //   322: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   325: ifeq            215
        //   328: goto            331
        //   331: aload           results
        //   333: invokevirtual   java/util/ArrayList.size:()I
        //   336: ifle            496
        //   339: ldc             "%%"
        //   341: aload_3         /* ruleAnalyzes */
        //   342: invokevirtual   io/legado/app/model/analyzeRule/RuleAnalyzer.getElementsType:()Ljava/lang/String;
        //   345: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //   348: ifeq            454
        //   351: iconst_0       
        //   352: istore          6
        //   354: aload           results
        //   356: iconst_0       
        //   357: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   360: checkcast       Ljava/util/List;
        //   363: invokeinterface java/util/List.size:()I
        //   368: iconst_m1      
        //   369: iadd           
        //   370: istore          7
        //   372: iload           6
        //   374: iload           7
        //   376: if_icmpgt       496
        //   379: iload           6
        //   381: istore          i
        //   383: iinc            6, 1
        //   386: aload           results
        //   388: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //   391: astore          9
        //   393: aload           9
        //   395: invokeinterface java/util/Iterator.hasNext:()Z
        //   400: ifeq            444
        //   403: aload           9
        //   405: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   410: checkcast       Ljava/util/List;
        //   413: astore          temp
        //   415: iload           i
        //   417: aload           temp
        //   419: invokeinterface java/util/List.size:()I
        //   424: if_icmpge       393
        //   427: aload_2         /* result */
        //   428: aload           temp
        //   430: iload           i
        //   432: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   437: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   440: pop            
        //   441: goto            393
        //   444: iload           6
        //   446: iload           7
        //   448: if_icmple       379
        //   451: goto            496
        //   454: aload           results
        //   456: invokevirtual   java/util/ArrayList.iterator:()Ljava/util/Iterator;
        //   459: astore          6
        //   461: aload           6
        //   463: invokeinterface java/util/Iterator.hasNext:()Z
        //   468: ifeq            496
        //   471: aload           6
        //   473: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   478: checkcast       Ljava/util/List;
        //   481: astore          temp
        //   483: aload_2         /* result */
        //   484: aload           temp
        //   486: checkcast       Ljava/util/Collection;
        //   489: invokevirtual   java/util/ArrayList.addAll:(Ljava/util/Collection;)Z
        //   492: pop            
        //   493: goto            461
        //   496: aload_2         /* result */
        //   497: checkcast       Ljava/util/List;
        //   500: areturn        
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/List<Ljava/lang/String;>;
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  xPath  
        //    StackMapTable: 00 11 FF 00 52 00 06 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 73 00 00 FF 00 2C 00 0C 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 73 07 00 D0 01 07 00 D0 07 00 AC 01 07 00 9F 00 00 3B FF 00 06 00 06 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 73 00 00 FF 00 04 00 06 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 CC 00 00 FF 00 0F 00 07 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 7E 07 00 9F 00 00 FF 00 3C 00 0B 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 7E 07 00 9F 07 00 66 07 00 73 07 00 AC 01 00 00 40 01 22 40 01 FF 00 11 00 07 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 7E 07 00 9F 00 00 FF 00 2F 00 08 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 7E 01 01 00 00 FD 00 0D 01 07 00 9F 32 FF 00 09 00 07 07 00 02 07 00 66 07 00 7E 07 00 81 07 00 7E 07 00 7E 07 00 9F 00 00 06 FA 00 22
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
    public final String getString(@NotNull final String rule) {
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(rule, false, 2, null);
        final ArrayList rules = ruleAnalyzes.splitRule("&&", "||");
        if (rules.size() != 1) {
            final ArrayList textList = new ArrayList();
            for (final String rl : rules) {
                Intrinsics.checkNotNullExpressionValue((Object)rl, "rl");
                final String temp = this.getString(rl);
                final CharSequence charSequence = temp;
                if (charSequence != null && charSequence.length() != 0) {
                    textList.add(temp);
                    if (Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                        break;
                    }
                    continue;
                }
            }
            return CollectionsKt.joinToString$default((Iterable)textList, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
        }
        final List<JXNode> result = this.getResult(rule);
        if (result == null) {
            return null;
        }
        final List it = result;
        final int n = 0;
        return TextUtils.join("\n", it);
    }
}
