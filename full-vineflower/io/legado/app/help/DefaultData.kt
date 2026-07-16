package io.legado.app.help

import io.legado.app.data.entities.TxtTocRule

public object DefaultData {
   public const val txtTocRuleFileName: String = "txtTocRule.json"

   public final val txtTocRules: List<TxtTocRule> by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return txtTocRules$delegate.getValue() as MutableList<TxtTocRule>;
      }

}
