package io.legado.app.model.analyzeRule

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

public object AnalyzeByRegex {
   public fun getElement(res: String, regs: Array<String>, index: Int = 0): List<String>? {
      val resM: Matcher = Pattern.compile(regs[index]).matcher(res);
      if (!resM.find()) {
         return null;
      } else {
         val var10000: java.util.List;
         if (index + 1 == regs.length) {
            val result: ArrayList = new ArrayList();
            var var11: Int = 0;
            val var8: Int = resM.groupCount();
            val groupIndex: Int;
            if (0 <= var8) {
               do {
                  groupIndex = var11++;
                  val var10001: java.lang.String = resM.group(groupIndex);
                  result.add(var10001);
               } while (groupIndex != var8);
            }

            var10000 = result;
         } else {
            val var10: StringBuilder = new StringBuilder();

            do {
               var10.append(resM.group());
            } while (resM.find());

            val var12: java.lang.String = var10.toString();
            var10000 = this.getElement(var12, regs, index + 1);
         }

         return var10000;
      }
   }

   public fun getElements(res: String, regs: Array<String>, index: Int = 0): List<List<String>> {
      val resM: Matcher = Pattern.compile(regs[index]).matcher(res);
      if (!resM.find()) {
         return new ArrayList<>();
      } else if (index + 1 == regs.length) {
         val var12: ArrayList = new ArrayList();

         do {
            val var14: ArrayList = new ArrayList();
            var var15: Int = 0;
            val var9: Int = resM.groupCount();
            val groupIndex: Int;
            if (0 <= var9) {
               do {
                  groupIndex = var15++;
                  val var11: java.lang.String = resM.group(groupIndex);
                  var14.add(if (var11 == null) "" else var11);
               } while (groupIndex != var9);
            }

            var12.add(var14);
         } while (resM.find());

         return var12;
      } else {
         val result: StringBuilder = new StringBuilder();

         do {
            result.append(resM.group());
         } while (resM.find());

         val info: java.lang.String = result.toString();
         return this.getElements(info, regs, index + 1);
      }
   }
}
