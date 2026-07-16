package io.legado.app.model.analyzeRule

import io.legado.app.utils.TextUtils
import java.util.ArrayList
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.seimicrawler.xpath.JXDocument
import org.seimicrawler.xpath.JXNode

public class AnalyzeByXPath(doc: Any) {
   private final var jxNode: Any

   init {
      this.jxNode = this.parse(doc);
   }

   private fun parse(doc: Any): Any {
      val var10000: Any;
      if (doc is JXNode) {
         var10000 = if ((doc as JXNode).isElement()) doc else this.strToJXDocument(doc.toString());
      } else if (doc is Document) {
         val var3: JXDocument = JXDocument.create(doc as Document);
         var10000 = var3;
      } else if (doc is Element) {
         val var5: JXDocument = JXDocument.create(new Elements(doc as Element));
         var10000 = var5;
      } else if (doc is Elements) {
         val var6: JXDocument = JXDocument.create(doc as Elements);
         var10000 = var6;
      } else {
         var10000 = this.strToJXDocument(doc.toString());
      }

      return var10000;
   }

   private fun strToJXDocument(html: String): JXDocument {
      var html1: java.lang.String = html;
      if (StringsKt.endsWith$default(html, "</td>", false, 2, null)) {
         html1 = "<tr>$html</tr>";
      }

      if (StringsKt.endsWith$default(html1, "</tr>", false, 2, null) || StringsKt.endsWith$default(html1, "</tbody>", false, 2, null)) {
         html1 = "<table>$html1</table>";
      }

      val var3: JXDocument = JXDocument.create(html1);
      return var3;
   }

   private fun getResult(xPath: String): List<JXNode>? {
      return if (this.jxNode is JXNode) (this.jxNode as JXNode).sel(xPath) else (this.jxNode as JXDocument).selN(xPath);
   }

   internal fun getElements(xPath: String): List<JXNode>? {
      if (xPath.length() == 0) {
         return null;
      } else {
         val var11: ArrayList = new ArrayList();
         val var12: RuleAnalyzer = new RuleAnalyzer(xPath, false, 2, null);
         val rules: ArrayList = var12.splitRule("&&", "||", "%%");
         if (rules.size() == 1) {
            val var14: Any = rules.get(0);
            return this.getResult(var14 as java.lang.String);
         } else {
            val var13: ArrayList = new ArrayList();

            for (java.lang.String rl : rules) {
               val i: java.util.List = this.getElements$reader_pro(temp);
               if (i != null && !i.isEmpty()) {
                  var13.add(i);
                  if (!i.isEmpty() && var12.getElementsType() == "||") {
                     break;
                  }
               }
            }

            if (var13.size() > 0) {
               if ("%%" == var12.getElementsType()) {
                  var var15: Int = 0;
                  val var17: Int = (var13.get(0) as java.util.List).size() + -1;
                  if (0 <= var17) {
                     do {
                        val var19: Int = var15++;

                        for (java.util.List temp : results) {
                           if (var19 < var23.size()) {
                              var11.add(var23.get(var19));
                           }
                        }
                     } while (var15 <= var17);
                  }
               } else {
                  for (java.util.List tempx : results) {
                     var11.addAll(tempx);
                  }
               }
            }

            return var11;
         }
      }
   }

   internal fun getStringList(xPath: String): List<String> {
      val result: ArrayList = new ArrayList();
      val ruleAnalyzes: RuleAnalyzer = new RuleAnalyzer(xPath, false, 2, null);
      val rules: ArrayList = ruleAnalyzes.splitRule("&&", "||", "%%");
      if (rules.size() == 1) {
         val var18: java.util.List = this.getResult(xPath);
         if (var18 != null) {
            val var21: java.lang.Iterable = var18;
            val var28: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(var18, 10));

            for (Object item$iv$iv : $this$map$iv) {
               var28.add(result.add((`item$iv$iv` as JXNode).asString()));
            }
         }

         return result;
      } else {
         val var17: ArrayList = new ArrayList();

         for (java.lang.String rl : rules) {
            val i: java.util.List = this.getStringList$reader_pro(temp);
            if (!i.isEmpty()) {
               var17.add(i);
               if (!i.isEmpty() && ruleAnalyzes.getElementsType() == "||") {
                  break;
               }
            }
         }

         if (var17.size() > 0) {
            if ("%%" == ruleAnalyzes.getElementsType()) {
               var var19: Int = 0;
               val var22: Int = (var17.get(0) as java.util.List).size() + -1;
               if (0 <= var22) {
                  do {
                     val var25: Int = var19++;

                     for (java.util.List temp : results) {
                        if (var25 < var30.size()) {
                           result.add(var30.get(var25));
                        }
                     }
                  } while (var19 <= var22);
               }
            } else {
               for (java.util.List tempx : results) {
                  result.addAll(tempx);
               }
            }
         }

         return result;
      }
   }

   public fun getString(rule: String): String? {
      val ruleAnalyzes: RuleAnalyzer = new RuleAnalyzer(rule, false, 2, null);
      val rules: ArrayList = ruleAnalyzes.splitRule("&&", "||");
      label21:
      if (rules.size() == 1) {
         val var12: java.util.List = this.getResult(rule);
         return if (var12 == null) null else TextUtils.join("\n", var12);
      } else {
         val var11: ArrayList = new ArrayList();

         for (java.lang.String rl : rules) {
            val temp: java.lang.String = this.getString(rl);
            if (temp != null && temp.length() != 0) {
               var11.add(temp);
               if (ruleAnalyzes.getElementsType() == "||") {
                  break;
               }
            }
         }

         return CollectionsKt.joinToString$default(var11, "\n", null, null, 0, null, null, 62, null);
      }
   }
}
