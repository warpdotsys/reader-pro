package io.legado.app.model.analyzeRule

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import io.legado.app.utils.GsonExtensionsKt
import java.util.HashMap

public class RuleData : RuleDataInterface {
   public open val variableMap: HashMap<String, String> by LazyKt.lazy(SyntheticFunction0.INSTANCE)
      public open get() {
         return this.variableMap$delegate.getValue() as HashMap<java.lang.String, java.lang.String>;
      }

   public override fun putVariable(key: String, value: String?) {
      if (value == null) {
         this.getVariableMap().remove(key);
      } else {
         this.getVariableMap().put(key, value);
      }
   }

   public fun getVariable(): String? {
      return if (this.getVariableMap().isEmpty()) null else GsonExtensionsKt.getGSON().toJson(this.getVariableMap());
   }

   public override fun getUserNameSpace(): String {
      return "unknow";
   }

   override fun getVariable(key: java.lang.String): java.lang.String? {
      return RuleDataInterface.DefaultImpls.getVariable(this, key);
   }
}
