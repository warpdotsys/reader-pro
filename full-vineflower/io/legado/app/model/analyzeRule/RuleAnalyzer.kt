package io.legado.app.model.analyzeRule

import java.util.ArrayList
import java.util.Arrays
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.reflect.KFunction2

public class RuleAnalyzer(data: String, code: Boolean = false) {
   public final val chompBalanced: KFunction2<Char, Char, Boolean>

   public final var elementsType: String
      internal set

   public final var innerType: Boolean
      internal set

   private final var pos: Int
   private final var queue: String
   private final var rule: ArrayList<String>
   public final val ruleTypeList: ArrayList<String>
   private final var start: Int
   private final var startX: Int
   private final var step: Int

   init {
      this.queue = data;
      this.rule = new ArrayList<>();
      this.elementsType = "";
      this.innerType = true;
      this.ruleTypeList = new ArrayList<>();
      this.chompBalanced = if (code) new Function2<Character, Character, java.lang.Boolean>(this) {
         {
            super(2, `<this>`, RuleAnalyzer::class.java, "chompCodeBalanced", "chompCodeBalanced(CC)Z", 0);
         }

         public final boolean invoke(char p0, char p1) {
            return (this.receiver as RuleAnalyzer).chompCodeBalanced(p0, p1);
         }
      } else new Function2<Character, Character, java.lang.Boolean>(this) {
         {
            super(2, `<this>`, RuleAnalyzer::class.java, "chompRuleBalanced", "chompRuleBalanced(CC)Z", 0);
         }

         public final boolean invoke(char p0, char p1) {
            return (this.receiver as RuleAnalyzer).chompRuleBalanced(p0, p1);
         }
      };
   }

   public fun trim() {
      if (this.queue.charAt(this.pos) == '@' || Intrinsics.compare(this.queue.charAt(this.pos), 33) < 0) {
         var var2: Int = this.pos++;

         while (this.queue.charAt(this.pos) == '@' || Intrinsics.compare(this.queue.charAt(this.pos), 33) < 0) {
            var2 = this.pos++;
         }

         this.start = this.pos;
         this.startX = this.pos;
      }
   }

   public fun reSetPos() {
      this.pos = 0;
      this.startX = 0;
   }

   public fun consumeTo(seq: String): Boolean {
      this.start = this.pos;
      val offset: Int = StringsKt.indexOf$default(this.queue, seq, this.pos, false, 4, null);
      val var10000: Boolean;
      if (offset != -1) {
         this.pos = offset;
         var10000 = true;
      } else {
         var10000 = false;
      }

      return var10000;
   }

   public fun consumeToAny(vararg seq: String): Boolean {
      for (int pos = this.pos; pos != this.queue.length(); pos++) {
         val var3: Array<java.lang.String> = seq;
         var var4: Int = 0;
         val var5: Int = seq.length;

         while (var4 < var5) {
            val s: java.lang.String = var3[var4];
            var4++;
            if (StringsKt.regionMatches$default(this.queue, pos, s, 0, s.length(), false, 16, null)) {
               this.step = s.length();
               this.pos = pos;
               return true;
            }
         }
      }

      return false;
   }

   private fun findToAny(seq: CharArray): Int {
      for (int pos = this.pos; pos != this.queue.length(); pos++) {
         val var3: CharArray = seq;
         var var4: Int = 0;
         val var5: Int = seq.length;

         while (var4 < var5) {
            val s: Char = var3[var4];
            var4++;
            if (this.queue.charAt(pos) == s) {
               return pos;
            }
         }
      }

      return -1;
   }

   public fun chompCodeBalanced(open: Char, close: Char): Boolean {
      var pos: Int = this.pos;
      var depth: Int = 0;
      var otherDepth: Int = 0;
      var inSingleQuote: Boolean = false;
      var inDoubleQuote: Boolean = false;

      while (pos != this.queue.length()) {
         val c: Char = this.queue.charAt(pos++);
         if (c != '\\') {
            if (c == '\'' && !inDoubleQuote) {
               inSingleQuote = !inSingleQuote;
            } else if (c == '"' && !inSingleQuote) {
               inDoubleQuote = !inDoubleQuote;
            }

            if (!inSingleQuote && !inDoubleQuote) {
               if (c == '[') {
                  depth++;
               } else if (c == ']') {
                  depth += -1;
               } else if (depth == 0) {
                  if (c == open) {
                     otherDepth++;
                  } else if (c == close) {
                     otherDepth += -1;
                  }
               }
            }
         } else {
            pos++;
         }

         if (depth <= 0 && otherDepth <= 0) {
            break;
         }
      }

      val var10000: Boolean;
      if (depth <= 0 && otherDepth <= 0) {
         this.pos = pos;
         var10000 = true;
      } else {
         var10000 = false;
      }

      return var10000;
   }

   public fun chompRuleBalanced(open: Char, close: Char): Boolean {
      var pos: Int = this.pos;
      var depth: Int = 0;
      var inSingleQuote: Boolean = false;
      var inDoubleQuote: Boolean = false;

      while (pos != this.queue.length()) {
         val c: Char = this.queue.charAt(pos++);
         if (c == '\'' && !inDoubleQuote) {
            inSingleQuote = !inSingleQuote;
         } else if (c == '"' && !inSingleQuote) {
            inDoubleQuote = !inDoubleQuote;
         }

         if (!inSingleQuote && !inDoubleQuote) {
            if (c == '\\') {
               pos++;
            } else if (c == open) {
               depth++;
            } else if (c == close) {
               depth += -1;
            }
         }

         if (depth <= 0) {
            break;
         }
      }

      val var10000: Boolean;
      if (depth > 0) {
         var10000 = false;
      } else {
         this.pos = pos;
         var10000 = true;
      }

      return var10000;
   }

   public tailrec fun splitRule(vararg split: String): ArrayList<String> {
      var var2: RuleAnalyzer = this;
      var var3: Array<java.lang.String> = split;

      while (true) {
         val var4: RuleAnalyzer = var2;
         if (var3.length == 1) {
            var2.elementsType = var3[0];
            val var73: ArrayList;
            if (!var2.consumeTo(var2.elementsType)) {
               val var15: java.util.Collection = var2.rule;
               if (var2.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var72: java.lang.String = var2.queue.substring(var2.startX);
               var15.add(var72);
               var73 = var2.rule;
            } else {
               var2.step = var2.elementsType.length();
               var73 = var2.splitRuleNext();
            }

            return var73;
         }

         if (!var2.consumeToAny(Arrays.copyOf(var3, var3.length))) {
            val var14: java.util.Collection = var2.rule;
            if (var2.queue == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var71: java.lang.String = var2.queue.substring(var2.startX);
            var14.add(var71);
            return var2.rule;
         }

         val end: Int = var2.pos;
         var2.pos = var2.start;

         do {
            val st: Int = var4.findToAny('[', '(');
            if (st == -1) {
               val var19: Array<java.lang.String> = new java.lang.String[1];
               if (var4.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var75: java.lang.String = var4.queue.substring(var4.startX, end);
               var19[0] = var75;
               var4.rule = CollectionsKt.arrayListOf(var19);
               val var30: Int = end + var4.step;
               if (var4.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var74: java.lang.String = var4.queue.substring(end, var30);
               var4.elementsType = var74;

               for (var4.pos = end + var4.step; var4.consumeTo(var4.elementsType); var4.pos = var4.pos + var4.step) {
                  val var31: java.util.Collection = var4.rule;
                  if (var4.queue == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var69: java.lang.String = var4.queue.substring(var4.start, var4.pos);
                  var31.add(var69);
               }

               val var32: java.util.Collection = var4.rule;
               if (var4.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var70: java.lang.String = var4.queue.substring(var4.pos);
               var32.add(var70);
               return var4.rule;
            }

            if (st > end) {
               val var17: Array<java.lang.String> = new java.lang.String[1];
               if (var4.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               } else {
                  val var10003: java.lang.String = var4.queue.substring(var4.startX, end);
                  var17[0] = var10003;
                  var4.rule = CollectionsKt.arrayListOf(var17);
                  val var26: Int = end + var4.step;
                  if (var4.queue == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  } else {
                     val var10001: java.lang.String = var4.queue.substring(end, var26);
                     var4.elementsType = var10001;

                     for (var4.pos = end + var4.step; var4.consumeTo(var4.elementsType) && var4.pos < st; var4.pos = var4.pos + var4.step) {
                        val var27: java.util.Collection = var4.rule;
                        if (var4.queue == null) {
                           throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }

                        val var66: java.lang.String = var4.queue.substring(var4.start, var4.pos);
                        var27.add(var66);
                     }

                     val var67: ArrayList;
                     if (var4.pos > st) {
                        var4.startX = var4.start;
                        var67 = var4.splitRuleNext();
                     } else {
                        val var28: java.util.Collection = var4.rule;
                        if (var4.queue == null) {
                           throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }

                        val var68: java.lang.String = var4.queue.substring(var4.pos);
                        var28.add(var68);
                        var67 = var4.rule;
                     }

                     return var67;
                  }
               }
            }

            var4.pos = st;
            if (!(var4.chompBalanced as Function2)
               .invoke(var4.queue.charAt(var4.pos), Character.valueOf((char)(if (var4.queue.charAt(var4.pos) == '[') 93 else 41)))) {
               val var10000: Error = new Error;
               if (var4.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var10002: java.lang.String = var4.queue.substring(0, var4.start);
               var10000./* $VF: Unable to resugar constructor */<init>(Intrinsics.stringPlus(var10002, "后未平衡"));
               throw var10000;
            }
         } while (end > var4.pos);

         var4.start = var4.pos;
         var2 = var4;
         var3 = Arrays.copyOf(var3, var3.length);
      }
   }

   @JvmName(name = "splitRuleNext")
   private tailrec fun splitRule(): ArrayList<String> {
      var var1: RuleAnalyzer = this;

      label90:
      while (true) {
         val var2: RuleAnalyzer = var1;
         val end: Int = var1.pos;
         var1.pos = var1.start;

         do {
            val st: Int = var2.findToAny('[', '(');
            if (st == -1) {
               var var18: java.util.Collection = var2.rule;
               val var29: Array<java.lang.String> = new java.lang.String[1];
               if (var2.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var62: java.lang.String = var2.queue.substring(var2.startX, end);
               var29[0] = var62;
               CollectionsKt.addAll(var18, var29);

               for (var2.pos = end + var2.step; var2.consumeTo(var2.elementsType); var2.pos = var2.pos + var2.step) {
                  var18 = var2.rule;
                  if (var2.queue == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var59: java.lang.String = var2.queue.substring(var2.start, var2.pos);
                  var18.add(var59);
               }

               var18 = var2.rule;
               if (var2.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var60: java.lang.String = var2.queue.substring(var2.pos);
               var18.add(var60);
               return var2.rule;
            }

            if (st > end) {
               var var15: java.util.Collection = var2.rule;
               val var23: Array<java.lang.String> = new java.lang.String[1];
               if (var2.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var61: java.lang.String = var2.queue.substring(var2.startX, end);
               var23[0] = var61;
               CollectionsKt.addAll(var15, CollectionsKt.arrayListOf(var23));

               for (var2.pos = end + var2.step; var2.consumeTo(var2.elementsType) && var2.pos < st; var2.pos = var2.pos + var2.step) {
                  var15 = var2.rule;
                  if (var2.queue == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var57: java.lang.String = var2.queue.substring(var2.start, var2.pos);
                  var15.add(var57);
               }

               if (var2.pos <= st) {
                  var15 = var2.rule;
                  if (var2.queue == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var58: java.lang.String = var2.queue.substring(var2.pos);
                  var15.add(var58);
                  return var2.rule;
               }

               var2.startX = var2.start;
               var1 = var2;
               continue label90;
            }

            var2.pos = st;
            if (!(var2.chompBalanced as Function2)
               .invoke(var2.queue.charAt(var2.pos), Character.valueOf((char)(if (var2.queue.charAt(var2.pos) == '[') 93 else 41)))) {
               val var56: Error = new Error;
               if (var2.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var10002: java.lang.String = var2.queue.substring(0, var2.start);
               var56./* $VF: Unable to resugar constructor */<init>(Intrinsics.stringPlus(var10002, "后未平衡"));
               throw var56;
            }
         } while (end > var2.pos);

         var2.start = var2.pos;
         if (!var2.consumeTo(var2.elementsType)) {
            val var12: java.util.Collection = var2.rule;
            if (var2.queue == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var10000: java.lang.String = var2.queue.substring(var2.startX);
            var12.add(var10000);
            return var2.rule;
         }

         var1 = var2;
      }
   }

   public fun innerRule(inner: String, startStep: Int = 1, endStep: Int = 1, fr: (String) -> String?): String {
      val st: StringBuilder = new StringBuilder();

      while (this.consumeTo(inner)) {
         val posPre: Int = this.pos;
         if (this.chompCodeBalanced('{', '}')) {
            val var9: Int = posPre + startStep;
            val `$this$innerRule_u24lambda_u2d0`: Int = this.pos - endStep;
            if (this.queue == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var var10001: java.lang.String = this.queue.substring(var9, `$this$innerRule_u24lambda_u2d0`);
            val frv: java.lang.String = fr.invoke(var10001) as java.lang.String;
            if (frv != null && frv.length() != 0) {
               if (this.queue == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               var10001 = this.queue.substring(this.startX, posPre);
               st.append(Intrinsics.stringPlus(var10001, frv));
               this.startX = this.pos;
               continue;
            }
         }

         this.pos = this.pos + inner.length();
      }

      val var10000: java.lang.String;
      if (this.startX == 0) {
         var10000 = "";
      } else {
         if (this.queue == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         val var26: java.lang.String = this.queue.substring(this.startX);
         st.append(var26);
         val var15: java.lang.String = st.toString();
         var10000 = var15;
      }

      return var10000;
   }

   public fun innerRule(startStr: String, endStr: String, fr: (String) -> String?): String {
      val st: StringBuilder = new StringBuilder();

      while (this.consumeTo(startStr)) {
         this.pos = this.pos + startStr.length();
         val posPre: Int = this.pos;
         if (this.consumeTo(endStr)) {
            if (this.queue == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var var10001: java.lang.String = this.queue.substring(posPre, this.pos);
            val frv: java.lang.String = fr.invoke(var10001) as java.lang.String;
            val var15: java.lang.String = this.queue;
            val var17: Int = this.startX;
            val var19: Int = posPre - startStr.length();
            if (var15 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var10001 = var15.substring(var17, var19);
            st.append(Intrinsics.stringPlus(var10001, frv));
            this.pos = this.pos + endStr.length();
            this.startX = this.pos;
         }
      }

      val var10000: java.lang.String;
      if (this.startX == 0) {
         var10000 = this.queue;
      } else {
         if (this.queue == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         val var22: java.lang.String = this.queue.substring(this.startX);
         st.append(var22);
         val var14: java.lang.String = st.toString();
         var10000 = var14;
      }

      return var10000;
   }

   public companion object {
      private const val ESC: Char
   }
}
