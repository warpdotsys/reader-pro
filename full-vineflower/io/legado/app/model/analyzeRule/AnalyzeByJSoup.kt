package io.legado.app.model.analyzeRule

import java.util.ArrayList
import java.util.LinkedHashSet
import kotlin.jvm.internal.Intrinsics
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Collector
import org.jsoup.select.Elements
import org.jsoup.select.Evaluator.Id
import org.seimicrawler.xpath.JXNode

public class AnalyzeByJSoup(doc: Any) {
   private final var element: Element

   init {
      this.element = Companion.parse(doc);
   }

   internal fun getElements(rule: String): Elements {
      return this.getElements(this.element, rule);
   }

   internal fun getString(ruleStr: String): String? {
      val var10000: java.lang.String;
      if (ruleStr.length() == 0) {
         var10000 = null;
      } else {
         val var11: java.util.List = this.getStringList$reader_pro(ruleStr);
         val var10: java.util.List = if (!var11.isEmpty()) var11 else null;
         var10000 = if (var10 == null) null else CollectionsKt.joinToString$default(var10, "\n", null, null, 0, null, null, 62, null);
      }

      return var10000;
   }

   internal fun getString0(ruleStr: String): String {
      val var2: java.util.List = this.getStringList$reader_pro(ruleStr);
      return if (var2.isEmpty()) "" else var2.get(0) as java.lang.String;
   }

   internal fun getStringList(ruleStr: String): List<String> {
      val textS: ArrayList = new ArrayList();
      if (ruleStr.length() == 0) {
         return textS;
      } else {
         val var15: AnalyzeByJSoup.SourceRule = new AnalyzeByJSoup.SourceRule(this, ruleStr);
         if (var15.getElementsRule().length() == 0) {
            val var17: java.lang.String = this.element.data();
            textS.add(if (var17 == null) "" else var17);
         } else {
            val var18: RuleAnalyzer = new RuleAnalyzer(var15.getElementsRule(), false, 2, null);
            val var19: ArrayList = var18.splitRule("&&", "||", "%%");
            val var20: ArrayList = new ArrayList();

            for (java.lang.String ruleStrX : ruleStrS) {
               val var10000: java.util.List;
               if (var15.isCss()) {
                  val lastIndex: Int = StringsKt.lastIndexOf$default(temp, '@', 0, false, 6, null);
                  val var10001: Element = this.element;
                  var var10002: java.lang.String = temp.substring(0, lastIndex);
                  val temp: Elements = var10001.select(var10002);
                  var10002 = temp.substring(lastIndex + 1);
                  var10000 = this.getResultLast(temp, var10002);
               } else {
                  var10000 = this.getResultList(temp);
               }

               if (var10000 != null && !var10000.isEmpty()) {
                  var20.add(var10000);
                  if (var18.getElementsType() == "||") {
                     break;
                  }
               }
            }

            if (var20.size() > 0) {
               if ("%%" == var18.getElementsType()) {
                  var var21: Int = 0;
                  val var23: Int = (var20.get(0) as java.util.List).size() + -1;
                  if (0 <= var23) {
                     do {
                        val var25: Int = var21++;

                        for (java.util.List temp : results) {
                           if (var25 < var29.size()) {
                              textS.add(var29.get(var25));
                           }
                        }
                     } while (var21 <= var23);
                  }
               } else {
                  for (java.util.List tempx : results) {
                     textS.addAll(tempx);
                  }
               }
            }
         }

         return textS;
      }
   }

   private fun getElements(temp: Element?, rule: String): Elements {
      if (temp != null && rule.length() != 0) {
         val var19: Elements = new Elements();
         val var20: AnalyzeByJSoup.SourceRule = new AnalyzeByJSoup.SourceRule(this, rule);
         val ruleAnalyzes: RuleAnalyzer = new RuleAnalyzer(var20.getElementsRule(), false, 2, null);
         val ruleStrS: ArrayList = ruleAnalyzes.splitRule("&&", "||", "%%");
         val var21: ArrayList = new ArrayList();
         if (var20.isCss()) {
            for (java.lang.String ruleStr : ruleStrS) {
               val i: Elements = temp.select(es);
               var21.add(i);
               if (i.size() > 0 && ruleAnalyzes.getElementsType() == "||") {
                  break;
               }
            }
         } else {
            for (java.lang.String ruleStrx : ruleStrS) {
               val var28: RuleAnalyzer = new RuleAnalyzer(ruleStrx, false, 2, null);
               var28.trim();
               val rs: ArrayList = var28.splitRule("@");
               val var10000: Elements;
               if (rs.size() <= 1) {
                  var10000 = new AnalyzeByJSoup.ElementsSingle('\u0000', null, null, null, 15, null).getElementsSingle(temp, ruleStrx);
               } else {
                  val el: Elements = new Elements();
                  el.add(temp);

                  for (java.lang.String rl : rs) {
                     val es: Elements = new Elements();

                     for (Element et : el) {
                        es.addAll(this.getElements(et, rl));
                     }

                     el.clear();
                     el.addAll(es);
                  }

                  var10000 = el;
               }

               var21.add(var10000);
               if (var10000.size() > 0 && ruleAnalyzes.getElementsType() == "||") {
                  break;
               }
            }
         }

         if (var21.size() > 0) {
            if ("%%" == ruleAnalyzes.getElementsType()) {
               var var23: Int = 0;
               val var26: Int = (var21.get(0) as Elements).size();
               if (0 < var26) {
                  do {
                     val var29: Int = var23++;

                     for (Elements es : elementsList) {
                        if (var29 < var32.size()) {
                           var19.add(var32.get(var29));
                        }
                     }
                  } while (var23 < var26);
               }
            } else {
               for (Elements esx : elementsList) {
                  var19.addAll(esx);
               }
            }
         }

         return var19;
      } else {
         return new Elements();
      }
   }

   private fun getResultList(ruleStr: String): List<String>? {
      if (ruleStr.length() == 0) {
         return null;
      } else {
         var var12: Elements = new Elements();
         var12.add(this.element);
         val var13: RuleAnalyzer = new RuleAnalyzer(ruleStr, false, 2, null);
         var13.trim();
         val rules: ArrayList = var13.splitRule("@");
         val var14: Int = rules.size() - 1;
         var var6: Int = 0;
         if (0 < var14) {
            do {
               val i: Int = var6++;
               val es: Elements = new Elements();

               for (Element elt : elements) {
                  val var10001: AnalyzeByJSoup.ElementsSingle = new AnalyzeByJSoup.ElementsSingle('\u0000', null, null, null, 15, null);
                  val var11: Any = rules.get(i);
                  es.addAll(var10001.getElementsSingle(elt, var11 as java.lang.String));
               }

               var12.clear();
               var12 = es;
            } while (var6 < last);
         }

         val var10000: java.util.List;
         if (var12.isEmpty()) {
            var10000 = null;
         } else {
            val var15: Any = rules.get(var14);
            var10000 = this.getResultLast(var12, var15 as java.lang.String);
         }

         return var10000;
      }
   }

   private fun getResultLast(elements: Elements, lastRule: String): List<String> {
      val textS: ArrayList = new ArrayList();
      switch (lastRule.hashCode()) {
         case -1055246893:
            if (lastRule.equals("ownText")) {
               for (Element elementx : elements) {
                  val var33: java.lang.String = elementx.ownText();
                  if (var33.length() > 0) {
                     textS.add(var33);
                  }
               }

               return textS;
            }
            break;
         case -1053421180:
            if (lastRule.equals("textNodes")) {
               for (Element elementxx : elements) {
                  val var31: ArrayList = new ArrayList();

                  for (TextNode item : elementxx.textNodes()) {
                     val `$this$trim$iv`: java.lang.String = item.text();
                     val `$this$trim$iv$iv`: java.lang.CharSequence = `$this$trim$iv`;
                     var `startIndex$iv$iv`: Int = 0;
                     var `endIndex$iv$iv`: Int = `$this$trim$iv$iv`.length() - 1;
                     var `startFound$iv$iv`: Boolean = false;

                     while (startIndex$iv$iv <= endIndex$iv$iv) {
                        val var43: Boolean = Intrinsics.compare(
                              `$this$trim$iv$iv`.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32
                           )
                           <= 0;
                        if (!`startFound$iv$iv`) {
                           if (!var43) {
                              `startFound$iv$iv` = true;
                           } else {
                              `startIndex$iv$iv`++;
                           }
                        } else {
                           if (!var43) {
                              break;
                           }

                           `endIndex$iv$iv`--;
                        }
                     }

                     val text: java.lang.String = `$this$trim$iv$iv`.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
                     if (text.length() > 0) {
                        var31.add(text);
                     }
                  }

                  if (!var31.isEmpty()) {
                     textS.add(CollectionsKt.joinToString$default(var31, "\n", null, null, 0, null, null, 62, null));
                  }
               }

               return textS;
            }
            break;
         case 96673:
            if (lastRule.equals("all")) {
               textS.add(elements.outerHtml());
               return textS;
            }
            break;
         case 3213227:
            if (lastRule.equals("html")) {
               elements.select("script").remove();
               elements.select("style").remove();
               val var22: java.lang.String = elements.outerHtml();
               if (var22.length() > 0) {
                  textS.add(var22);
               }

               return textS;
            }
            break;
         case 3556653:
            if (lastRule.equals("text")) {
               for (Element element : elements) {
                  val url: java.lang.String = element.text();
                  if (url.length() > 0) {
                     textS.add(url);
                  }
               }

               return textS;
            }
         default:
      }

      for (Element elementxx : elements) {
         val var32: java.lang.String = elementxx.attr(lastRule);
         if (!StringsKt.isBlank(var32) && !textS.contains(var32)) {
            textS.add(var32);
         }
      }

      return textS;
   }

   public companion object {
      public final val validKeys: Array<String>

      public fun parse(doc: Any): Element {
         val var10000: Element;
         if (doc is Element) {
            var10000 = doc as Element;
         } else if (doc is JXNode) {
            val var3: Element = if ((doc as JXNode).isElement()) (doc as JXNode).asElement() else Jsoup.parse(doc.toString());
            var10000 = var3;
         } else {
            val var4: Document = Jsoup.parse(doc.toString());
            var10000 = var4;
         }

         return var10000;
      }
   }

   public data class ElementsSingle(split: Char = 46,
      beforeRule: String = "",
      indexDefault: MutableList<Int> = (new ArrayList()) as java.util.List,
      indexes: MutableList<Any> = (new ArrayList()) as java.util.List
   ) {
      public final var beforeRule: String
         internal set

      public final val indexDefault: MutableList<Int>
      public final val indexes: MutableList<Any>

      public final var split: Char
         internal set

      init {
         this.split = split;
         this.beforeRule = beforeRule;
         this.indexDefault = indexDefault;
         this.indexes = indexes;
      }

      public fun getElementsSingle(temp: Element, rule: String): Elements {
         this.findIndexSet(rule);
         var var10000: Elements;
         if (this.beforeRule.length() == 0) {
            var10000 = temp.children();
         } else {
            label179: {
               val var16: java.util.List = StringsKt.split$default(this.beforeRule, new java.lang.String[]{"."}, false, 0, 6, null);
               val var19: java.lang.String = var16.get(0) as java.lang.String;
               switch (var19.hashCode()) {
                  case 3355:
                     if (var19.equals("id")) {
                        var10000 = Collector.collect(new Id(var16.get(1) as java.lang.String), temp);
                        break label179;
                     }
                     break;
                  case 114586:
                     if (var19.equals("tag")) {
                        var10000 = temp.getElementsByTag(var16.get(1) as java.lang.String);
                        break label179;
                     }
                     break;
                  case 3556653:
                     if (var19.equals("text")) {
                        var10000 = temp.getElementsContainingOwnText(var16.get(1) as java.lang.String);
                        break label179;
                     }
                     break;
                  case 94742904:
                     if (var19.equals("class")) {
                        var10000 = temp.getElementsByClass(var16.get(1) as java.lang.String);
                        break label179;
                     }
                     break;
                  case 1659526655:
                     if (var19.equals("children")) {
                        var10000 = temp.children();
                        break label179;
                     }
                  default:
               }

               var10000 = temp.select(this.beforeRule);
            }
         }

         var elements: Elements = var10000;
         val var17: Int = var10000.size();
         val es: Int = this.indexDefault.size() - 1;
         val indexSet: Int = if (es.intValue() != -1) es else null;
         val var20: Int = if (indexSet == null) this.indexes.size() - 1 else indexSet;
         val var21: java.util.Set = new LinkedHashSet();
         if (this.indexes.isEmpty()) {
            var var23: Int = var20;
            if (0 <= var20) {
               do {
                  val var31: Int = this.indexDefault.get(var23--).intValue();
                  if (0 <= var31 && var31 < var17) {
                     var21.add(var31);
                  } else if (var31 < 0 && var17 >= -var31) {
                     var21.add(var31 + var17);
                  }
               } while (0 <= var23);
            }
         } else {
            var var24: Int = var20;
            if (0 <= var20) {
               do {
                  val var28: Int = var24--;
                  if (this.indexes.get(var28) is Triple) {
                     val var33: Triple = this.indexes.get(var28) as Triple;
                     val var35: Int = var33.component1() as Int;
                     val var36: Int = var33.component2() as Int;
                     val stepX: Int = (var33.component3() as java.lang.Number).intValue();
                     val start: Int = if (var35 == null)
                        0
                        else
                        (if (var35 >= 0) (if (var35 < var17) var35 else var17 - 1) else (if (-var35 <= var17) var17 + var35 else 0));
                     val end: Int = if (var36 == null)
                        var17 - 1
                        else
                        (if (var36 >= 0) (if (var36 < var17) var36 else var17 - 1) else (if (-var36 <= var17) var17 + var36 else 0));
                     if (start != end && stepX < var17) {
                        CollectionsKt.addAll(
                           var21,
                           if (end > start)
                              RangesKt.step(new IntRange(start, end), if (stepX > 0) stepX else (if (-stepX < var17) stepX + var17 else 1))
                              else
                              RangesKt.step(RangesKt.downTo(start, end), if (stepX > 0) stepX else (if (-stepX < var17) stepX + var17 else 1))
                        );
                     } else {
                        var21.add(start);
                     }
                  } else {
                     val var32: Int = this.indexes.get(var28) as Int;
                     if (0 <= var32 && var32 < var17) {
                        var21.add(var32);
                     } else if (var32 < 0 && var17 >= -var32) {
                        var21.add(var32 + var17);
                     }
                  }
               } while (0 <= var24);
            }
         }

         if (this.split == '!') {
            val var25: java.util.Iterator = var21.iterator();

            while (var25.hasNext()) {
               elements.set((var25.next() as java.lang.Number).intValue(), null);
            }

            elements.removeAll(CollectionsKt.listOf(null));
         } else if (this.split == '.') {
            val var26: Elements = new Elements();
            val var30: java.util.Iterator = var21.iterator();

            while (var30.hasNext()) {
               var26.add(elements.get((var30.next() as java.lang.Number).intValue()));
            }

            elements = var26;
         }

         return elements;
      }

      private fun findIndexSet(rule: String) {
         // $VF: Couldn't be decompiled
         // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
         // java.lang.IllegalStateException: Trying to make ternary but have no SSA-Form! How is this possible?
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.buildIff(SimplifyExprentsHelper.java:1073)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:74)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.SimplifyExprentsHelper.simplifyStackVarsStatement(SimplifyExprentsHelper.java:71)
         //   at org.jetbrains.java.decompiler.modules.decompiler.EliminateLoopsHelper.eliminateLoops(EliminateLoopsHelper.java:24)
         //
         // Bytecode:
         // 000: aload 1
         // 001: astore 3
         // 002: bipush 0
         // 003: istore 4
         // 005: aload 3
         // 006: checkcast java/lang/CharSequence
         // 009: astore 5
         // 00b: bipush 0
         // 00c: istore 6
         // 00e: bipush 0
         // 00f: istore 7
         // 011: aload 5
         // 013: invokeinterface java/lang/CharSequence.length ()I 1
         // 018: bipush 1
         // 019: isub
         // 01a: istore 8
         // 01c: bipush 0
         // 01d: istore 9
         // 01f: iload 7
         // 021: iload 8
         // 023: if_icmpgt 07d
         // 026: iload 9
         // 028: ifne 030
         // 02b: iload 7
         // 02d: goto 032
         // 030: iload 8
         // 032: istore 10
         // 034: aload 5
         // 036: iload 10
         // 038: invokeinterface java/lang/CharSequence.charAt (I)C 2
         // 03d: istore 11
         // 03f: bipush 0
         // 040: istore 12
         // 042: iload 11
         // 044: bipush 32
         // 046: invokestatic kotlin/jvm/internal/Intrinsics.compare (II)I
         // 049: ifgt 050
         // 04c: bipush 1
         // 04d: goto 051
         // 050: bipush 0
         // 051: istore 11
         // 053: iload 9
         // 055: ifne 06c
         // 058: iload 11
         // 05a: ifne 063
         // 05d: bipush 1
         // 05e: istore 9
         // 060: goto 07a
         // 063: iload 7
         // 065: bipush 1
         // 066: iadd
         // 067: istore 7
         // 069: goto 07a
         // 06c: iload 11
         // 06e: ifne 074
         // 071: goto 07d
         // 074: iload 8
         // 076: bipush 1
         // 077: isub
         // 078: istore 8
         // 07a: goto 01f
         // 07d: aload 5
         // 07f: iload 7
         // 081: iload 8
         // 083: bipush 1
         // 084: iadd
         // 085: invokeinterface java/lang/CharSequence.subSequence (II)Ljava/lang/CharSequence; 3
         // 08a: invokevirtual java/lang/Object.toString ()Ljava/lang/String;
         // 08d: astore 2
         // 08e: aload 2
         // 08f: invokevirtual java/lang/String.length ()I
         // 092: istore 3
         // 093: aconst_null
         // 094: astore 4
         // 096: bipush 0
         // 097: istore 5
         // 099: bipush 0
         // 09a: istore 7
         // 09c: new java/util/ArrayList
         // 09f: dup
         // 0a0: invokespecial java/util/ArrayList.<init> ()V
         // 0a3: checkcast java/util/List
         // 0a6: astore 6
         // 0a8: ldc ""
         // 0aa: astore 7
         // 0ac: aload 2
         // 0ad: checkcast java/lang/CharSequence
         // 0b0: invokestatic kotlin/text/StringsKt.last (Ljava/lang/CharSequence;)C
         // 0b3: bipush 93
         // 0b5: if_icmpne 0bc
         // 0b8: bipush 1
         // 0b9: goto 0bd
         // 0bc: bipush 0
         // 0bd: istore 8
         // 0bf: iload 8
         // 0c1: ifeq 258
         // 0c4: iload 3
         // 0c5: istore 9
         // 0c7: iload 9
         // 0c9: bipush -1
         // 0ca: iadd
         // 0cb: istore 3
         // 0cc: iload 3
         // 0cd: istore 9
         // 0cf: iload 9
         // 0d1: bipush -1
         // 0d2: iadd
         // 0d3: istore 3
         // 0d4: iload 9
         // 0d6: iflt 349
         // 0d9: aload 2
         // 0da: iload 3
         // 0db: invokevirtual java/lang/String.charAt (I)C
         // 0de: istore 9
         // 0e0: iload 9
         // 0e2: bipush 32
         // 0e4: if_icmpne 0ea
         // 0e7: goto 0cc
         // 0ea: bipush 48
         // 0ec: iload 9
         // 0ee: if_icmpgt 100
         // 0f1: iload 9
         // 0f3: bipush 57
         // 0f5: if_icmpgt 0fc
         // 0f8: bipush 1
         // 0f9: goto 101
         // 0fc: bipush 0
         // 0fd: goto 101
         // 100: bipush 0
         // 101: ifeq 127
         // 104: iload 9
         // 106: istore 10
         // 108: bipush 0
         // 109: istore 11
         // 10b: new java/lang/StringBuilder
         // 10e: dup
         // 10f: invokespecial java/lang/StringBuilder.<init> ()V
         // 112: iload 10
         // 114: invokestatic java/lang/String.valueOf (C)Ljava/lang/String;
         // 117: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         // 11a: aload 7
         // 11c: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         // 11f: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
         // 122: astore 7
         // 124: goto 0cc
         // 127: iload 9
         // 129: bipush 45
         // 12b: if_icmpne 134
         // 12e: bipush 1
         // 12f: istore 5
         // 131: goto 0cc
         // 134: aload 7
         // 136: checkcast java/lang/CharSequence
         // 139: astore 10
         // 13b: bipush 0
         // 13c: istore 11
         // 13e: aload 10
         // 140: invokeinterface java/lang/CharSequence.length ()I 1
         // 145: ifne 14c
         // 148: bipush 1
         // 149: goto 14d
         // 14c: bipush 0
         // 14d: ifeq 154
         // 150: aconst_null
         // 151: goto 17b
         // 154: iload 5
         // 156: ifeq 16c
         // 159: aload 7
         // 15b: astore 10
         // 15d: bipush 0
         // 15e: istore 11
         // 160: aload 10
         // 162: invokestatic java/lang/Integer.parseInt (Ljava/lang/String;)I
         // 165: ineg
         // 166: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
         // 169: goto 17b
         // 16c: aload 7
         // 16e: astore 10
         // 170: bipush 0
         // 171: istore 11
         // 173: aload 10
         // 175: invokestatic java/lang/Integer.parseInt (Ljava/lang/String;)I
         // 178: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
         // 17b: astore 4
         // 17d: iload 9
         // 17f: istore 10
         // 181: iload 10
         // 183: bipush 58
         // 185: if_icmpne 195
         // 188: aload 6
         // 18a: aload 4
         // 18c: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
         // 191: pop
         // 192: goto 24e
         // 195: aload 6
         // 197: invokeinterface java/util/List.isEmpty ()Z 1
         // 19c: ifeq 1b6
         // 19f: aload 4
         // 1a1: ifnonnull 1a7
         // 1a4: goto 349
         // 1a7: aload 0
         // 1a8: getfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.indexes Ljava/util/List;
         // 1ab: aload 4
         // 1ad: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
         // 1b2: pop
         // 1b3: goto 1ef
         // 1b6: aload 0
         // 1b7: getfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.indexes Ljava/util/List;
         // 1ba: new kotlin/Triple
         // 1bd: dup
         // 1be: aload 4
         // 1c0: aload 6
         // 1c2: invokestatic kotlin/collections/CollectionsKt.last (Ljava/util/List;)Ljava/lang/Object;
         // 1c5: aload 6
         // 1c7: invokeinterface java/util/List.size ()I 1
         // 1cc: bipush 2
         // 1cd: if_icmpne 1db
         // 1d0: aload 6
         // 1d2: invokestatic kotlin/collections/CollectionsKt.first (Ljava/util/List;)Ljava/lang/Object;
         // 1d5: checkcast java/lang/Integer
         // 1d8: goto 1df
         // 1db: bipush 1
         // 1dc: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
         // 1df: invokespecial kotlin/Triple.<init> (Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
         // 1e2: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
         // 1e7: pop
         // 1e8: aload 6
         // 1ea: invokeinterface java/util/List.clear ()V 1
         // 1ef: iload 9
         // 1f1: bipush 33
         // 1f3: if_icmpne 211
         // 1f6: aload 0
         // 1f7: bipush 33
         // 1f9: putfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.split C
         // 1fc: aload 2
         // 1fd: iinc 3 -1
         // 200: iload 3
         // 201: invokevirtual java/lang/String.charAt (I)C
         // 204: istore 9
         // 206: iload 3
         // 207: ifle 211
         // 20a: iload 9
         // 20c: bipush 32
         // 20e: if_icmpeq 1fc
         // 211: iload 9
         // 213: bipush 91
         // 215: if_icmpne 244
         // 218: aload 0
         // 219: aload 2
         // 21a: astore 11
         // 21c: bipush 0
         // 21d: istore 12
         // 21f: bipush 0
         // 220: istore 13
         // 222: aload 11
         // 224: dup
         // 225: ifnonnull 233
         // 228: new java/lang/NullPointerException
         // 22b: dup
         // 22c: ldc_w "null cannot be cast to non-null type java.lang.String"
         // 22f: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
         // 232: athrow
         // 233: iload 12
         // 235: iload 3
         // 236: invokevirtual java/lang/String.substring (II)Ljava/lang/String;
         // 239: dup
         // 23a: ldc_w "(this as java.lang.Strin…ing(startIndex, endIndex)"
         // 23d: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
         // 240: putfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.beforeRule Ljava/lang/String;
         // 243: return
         // 244: iload 9
         // 246: bipush 44
         // 248: if_icmpeq 24e
         // 24b: goto 349
         // 24e: ldc ""
         // 250: astore 7
         // 252: bipush 0
         // 253: istore 5
         // 255: goto 0cc
         // 258: iload 3
         // 259: istore 9
         // 25b: iload 9
         // 25d: bipush -1
         // 25e: iadd
         // 25f: istore 3
         // 260: iload 9
         // 262: iflt 349
         // 265: aload 2
         // 266: iload 3
         // 267: invokevirtual java/lang/String.charAt (I)C
         // 26a: istore 9
         // 26c: iload 9
         // 26e: bipush 32
         // 270: if_icmpne 276
         // 273: goto 258
         // 276: bipush 48
         // 278: iload 9
         // 27a: if_icmpgt 28c
         // 27d: iload 9
         // 27f: bipush 57
         // 281: if_icmpgt 288
         // 284: bipush 1
         // 285: goto 28d
         // 288: bipush 0
         // 289: goto 28d
         // 28c: bipush 0
         // 28d: ifeq 2b3
         // 290: iload 9
         // 292: istore 10
         // 294: bipush 0
         // 295: istore 11
         // 297: new java/lang/StringBuilder
         // 29a: dup
         // 29b: invokespecial java/lang/StringBuilder.<init> ()V
         // 29e: iload 10
         // 2a0: invokestatic java/lang/String.valueOf (C)Ljava/lang/String;
         // 2a3: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         // 2a6: aload 7
         // 2a8: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
         // 2ab: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
         // 2ae: astore 7
         // 2b0: goto 258
         // 2b3: iload 9
         // 2b5: bipush 45
         // 2b7: if_icmpne 2c0
         // 2ba: bipush 1
         // 2bb: istore 5
         // 2bd: goto 258
         // 2c0: iload 9
         // 2c2: bipush 33
         // 2c4: if_icmpeq 2d5
         // 2c7: iload 9
         // 2c9: bipush 46
         // 2cb: if_icmpeq 2d5
         // 2ce: iload 9
         // 2d0: bipush 58
         // 2d2: if_icmpne 33c
         // 2d5: aload 0
         // 2d6: getfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.indexDefault Ljava/util/List;
         // 2d9: iload 5
         // 2db: ifeq 2ee
         // 2de: aload 7
         // 2e0: astore 10
         // 2e2: bipush 0
         // 2e3: istore 11
         // 2e5: aload 10
         // 2e7: invokestatic java/lang/Integer.parseInt (Ljava/lang/String;)I
         // 2ea: ineg
         // 2eb: goto 2fa
         // 2ee: aload 7
         // 2f0: astore 10
         // 2f2: bipush 0
         // 2f3: istore 11
         // 2f5: aload 10
         // 2f7: invokestatic java/lang/Integer.parseInt (Ljava/lang/String;)I
         // 2fa: invokestatic java/lang/Integer.valueOf (I)Ljava/lang/Integer;
         // 2fd: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
         // 302: pop
         // 303: iload 9
         // 305: bipush 58
         // 307: if_icmpeq 33f
         // 30a: aload 0
         // 30b: iload 9
         // 30d: putfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.split C
         // 310: aload 0
         // 311: aload 2
         // 312: astore 10
         // 314: bipush 0
         // 315: istore 11
         // 317: bipush 0
         // 318: istore 12
         // 31a: aload 10
         // 31c: dup
         // 31d: ifnonnull 32b
         // 320: new java/lang/NullPointerException
         // 323: dup
         // 324: ldc_w "null cannot be cast to non-null type java.lang.String"
         // 327: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
         // 32a: athrow
         // 32b: iload 11
         // 32d: iload 3
         // 32e: invokevirtual java/lang/String.substring (II)Ljava/lang/String;
         // 331: dup
         // 332: ldc_w "(this as java.lang.Strin…ing(startIndex, endIndex)"
         // 335: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
         // 338: putfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.beforeRule Ljava/lang/String;
         // 33b: return
         // 33c: goto 349
         // 33f: ldc ""
         // 341: astore 7
         // 343: bipush 0
         // 344: istore 5
         // 346: goto 258
         // 349: aload 0
         // 34a: bipush 32
         // 34c: putfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.split C
         // 34f: aload 0
         // 350: aload 2
         // 351: putfield io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.beforeRule Ljava/lang/String;
         // 354: return
      }

      public operator fun component1(): Char {
         return this.split;
      }

      public operator fun component2(): String {
         return this.beforeRule;
      }

      public operator fun component3(): MutableList<Int> {
         return this.indexDefault;
      }

      public operator fun component4(): MutableList<Any> {
         return this.indexes;
      }

      public fun copy(
         split: Char = this.split,
         beforeRule: String = this.beforeRule,
         indexDefault: MutableList<Int> = this.indexDefault,
         indexes: MutableList<Any> = this.indexes
      ): io.legado.app.model.analyzeRule.AnalyzeByJSoup.ElementsSingle {
         return new AnalyzeByJSoup.ElementsSingle(split, beforeRule, indexDefault, indexes);
      }

      public override fun toString(): String {
         return "ElementsSingle(split=${this.split}, beforeRule=${this.beforeRule}, indexDefault=${this.indexDefault}, indexes=${this.indexes})";
      }

      public override fun hashCode(): Int {
         return ((Character.hashCode(this.split) * 31 + this.beforeRule.hashCode()) * 31 + this.indexDefault.hashCode()) * 31 + this.indexes.hashCode();
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is AnalyzeByJSoup.ElementsSingle) {
            return false;
         } else {
            val var2: AnalyzeByJSoup.ElementsSingle = other as AnalyzeByJSoup.ElementsSingle;
            if (this.split != (other as AnalyzeByJSoup.ElementsSingle).split) {
               return false;
            } else if (!(this.beforeRule == var2.beforeRule)) {
               return false;
            } else if (!(this.indexDefault == var2.indexDefault)) {
               return false;
            } else {
               return this.indexes == var2.indexes;
            }
         }
      }

      fun ElementsSingle() {
         this('\u0000', null, null, null, 15, null);
      }
   }

   internal inner class SourceRule(ruleStr: String) {
      public final var elementsRule: String
         internal set

      public final var isCss: Boolean
         internal set

      init {
         this.this$0 = `this$0`;
         var var10000: AnalyzeByJSoup.SourceRule = this;
         var var18: java.lang.String;
         if (StringsKt.startsWith(ruleStr, "@CSS:", true)) {
            this.isCss = true;
            var18 = ruleStr.substring(5);
            val var16: java.lang.CharSequence = var18;
            var `startIndex$iv$iv`: Int = 0;
            var `endIndex$iv$iv`: Int = var16.length() - 1;
            var `startFound$iv$iv`: Boolean = false;

            while (startIndex$iv$iv <= endIndex$iv$iv) {
               val var17: Boolean = Intrinsics.compare(var16.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
               if (!`startFound$iv$iv`) {
                  if (!var17) {
                     `startFound$iv$iv` = true;
                  } else {
                     `startIndex$iv$iv`++;
                  }
               } else {
                  if (!var17) {
                     break;
                  }

                  `endIndex$iv$iv`--;
               }
            }

            val var14: java.lang.String = var16.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
            var10000 = this;
            var18 = var14;
         } else {
            var18 = ruleStr;
         }

         var10000.elementsRule = var18;
      }
   }
}
