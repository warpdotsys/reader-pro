package io.legado.app.model.analyzeRule

import java.util.HashMap

public interface RuleDataInterface {
   public val variableMap: HashMap<String, String>

   public abstract fun putVariable(key: String, value: String?) {
   }

   public open fun getVariable(key: String): String? {
   }

   public abstract fun getUserNameSpace(): String {
   }

   // $VF: Class flags could not be determined
   internal class DefaultImpls {
      @JvmStatic
      fun getVariable(`this`: RuleDataInterface, key: java.lang.String): java.lang.String? {
         return this.getVariableMap().get(key);
      }
   }
}
