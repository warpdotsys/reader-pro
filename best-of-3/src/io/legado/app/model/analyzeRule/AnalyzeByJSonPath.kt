package io.legado.app.model.analyzeRule

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.ReadContext
import java.util.ArrayList
import kotlin.jvm.functions.Function1
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class AnalyzeByJSonPath(json: Any) {
   private final var ctx: ReadContext

   init {
      this.ctx = Companion.parse(json);
   }

   public fun getString(rule: String): String? {
      if (rule.length() == 0) {
         return null;
      } else {
         val var15: RuleAnalyzer = new RuleAnalyzer(rule, true);
         val rules: ArrayList = var15.splitRule("&&", "||");
         if (rules.size() == 1) {
            var15.reSetPos();
            var var14: java.lang.String = RuleAnalyzer.innerRule$default(var15, "{$.", 0, 0, (new Function1<java.lang.String, java.lang.String>(this) {
               {
                  super(1);
                  this.this$0 = `$receiver`;
               }

               @Nullable
               public final java.lang.String invoke(@NotNull java.lang.String it) {
                  return this.this$0.getString(it);
               }
            }) as Function1, 6, null);
            if (var14.length() == 0) {
               try {
                  val var18: Any = this.ctx.read(rule);
                  var14 = if (var18 is java.util.List)
                     CollectionsKt.joinToString$default(var18 as java.lang.Iterable, "\n", null, null, 0, null, null, 62, null)
                     else
                     var18.toString();
               } catch (var12: Exception) {
                  var12.printStackTrace();
               }
            }

            return var14;
         } else {
            val var16: ArrayList = new ArrayList();

            for (java.lang.String rl : rules) {
               val temp: java.lang.String = this.getString(rl);
               if (temp != null && temp.length() != 0) {
                  var16.add(temp);
                  if (var15.getElementsType() == "||") {
                     break;
                  }
               }
            }

            return CollectionsKt.joinToString$default(var16, "\n", null, null, 0, null, null, 62, null);
         }
      }
   }

   internal fun getStringList(rule: String): List<String> {
      val result: ArrayList = new ArrayList();
      if (rule.length() == 0) {
         return result;
      } else {
         val var12: RuleAnalyzer = new RuleAnalyzer(rule, true);
         val var13: ArrayList = var12.splitRule("&&", "||", "%%");
         if (var13.size() == 1) {
            var12.reSetPos();
            val var15: java.lang.String = RuleAnalyzer.innerRule$default(var12, "{$.", 0, 0, (new Function1<java.lang.String, java.lang.String>(this) {
               {
                  super(1);
                  this.this$0 = `$receiver`;
               }

               @Nullable
               public final java.lang.String invoke(@NotNull java.lang.String it) {
                  return this.this$0.getString(it);
               }
            }) as Function1, 6, null);
            if (var15.length() == 0) {
               try {
                  val var19: Any = this.ctx.read(rule);
                  if (var19 is java.util.List) {
                     for (Object o : (java.util.List)obj) {
                        result.add(java.lang.String.valueOf(var25));
                     }
                  } else {
                     result.add(var19.toString());
                  }
               } catch (var11: Exception) {
                  var11.printStackTrace();
               }
            } else {
               result.add(var15);
            }

            return result;
         } else {
            val var14: ArrayList = new ArrayList();

            for (java.lang.String rl : rules) {
               val i: java.util.List = this.getStringList$reader_pro(temp);
               if (!i.isEmpty()) {
                  var14.add(i);
                  if (!i.isEmpty() && var12.getElementsType() == "||") {
                     break;
                  }
               }
            }

            if (var14.size() > 0) {
               if ("%%" == var12.getElementsType()) {
                  var var16: Int = 0;
                  val var20: Int = (var14.get(0) as java.util.List).size() + -1;
                  if (0 <= var20) {
                     do {
                        val var24: Int = var16++;

                        for (java.util.List temp : results) {
                           if (var24 < var29.size()) {
                              result.add(var29.get(var24));
                           }
                        }
                     } while (var16 <= var20);
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
   }

   internal fun getObject(rule: String): Any {
      val var2: Any = this.ctx.read(rule);
      return var2;
   }

   internal fun getList(rule: String): ArrayList<Any>? {
      val result: ArrayList = new ArrayList();
      if (rule.length() == 0) {
         return result;
      } else {
         val var18: RuleAnalyzer = new RuleAnalyzer(rule, true);
         val var19: ArrayList = var18.splitRule("&&", "||", "%%");
         if (var19.size() == 1) {
            val i: ReadContext = this.ctx;

            try {
               return i.read(var19.get(0) as java.lang.String);
            } catch (var17: Exception) {
               var17.printStackTrace();
            }
         } else {
            val var21: ArrayList = new ArrayList();

            for (java.lang.String rl : rules) {
               val var28: ArrayList = this.getList$reader_pro(var25);
               if (var28 != null && !var28.isEmpty()) {
                  var21.add(var28);
                  if (!var28.isEmpty() && var18.getElementsType() == "||") {
                     break;
                  }
               }
            }

            if (var21.size() > 0) {
               if ("%%" == var18.getElementsType()) {
                  var var23: Int = 0;
                  val var26: Int = (var21.get(0) as ArrayList).size();
                  if (0 < var26) {
                     do {
                        val var29: Int = var23++;

                        for (ArrayList temp : results) {
                           if (var29 < var34.size()) {
                              val var11: Any = var34.get(var29);
                              if (var11 != null) {
                                 result.add(var11);
                              }
                           }
                        }
                     } while (var23 < var26);
                  }
               } else {
                  for (ArrayList tempx : results) {
                     result.addAll(tempx);
                  }
               }
            }
         }

         return result;
      }
   }

   public companion object {
      public fun parse(json: Any): ReadContext {
         val var10000: ReadContext;
         if (json is ReadContext) {
            var10000 = json as ReadContext;
         } else if (json is java.lang.String) {
            val var3: DocumentContext = JsonPath.parse(json as java.lang.String);
            var10000 = var3;
         } else {
            val var4: DocumentContext = JsonPath.parse(json);
            var10000 = var4;
         }

         return var10000;
      }
   }
}
