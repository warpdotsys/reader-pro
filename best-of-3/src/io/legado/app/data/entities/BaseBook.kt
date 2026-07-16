package io.legado.app.data.entities

import io.legado.app.model.analyzeRule.RuleDataInterface
import io.legado.app.utils.StringExtensionsKt
import java.util.ArrayList

public interface BaseBook : RuleDataInterface {
   public var author: String
      internal final set

   public var bookUrl: String
      internal final set

   public var infoHtml: String?
      internal final set

   public var kind: String?
      internal final set

   public var name: String
      internal final set

   public var tocHtml: String?
      internal final set

   public var wordCount: String?
      internal final set

   public open fun getKindList(): List<String> {
   }

   internal class DefaultImpls {
      @JvmStatic
      fun getKindList(`this`: BaseBook): MutableList<java.lang.String> {
         val kindList: ArrayList = new ArrayList();
         var var10: java.lang.String = this.getWordCount();
         if (var10 != null && !StringsKt.isBlank(var10)) {
            kindList.add(var10);
         }

         var10 = this.getKind();
         if (var10 != null) {
            CollectionsKt.addAll(kindList, StringExtensionsKt.splitNotBlank(var10, ",", "\n"));
         }

         return kindList;
      }

      @JvmStatic
      fun getVariable(`this`: BaseBook, key: java.lang.String): java.lang.String? {
         return RuleDataInterface.DefaultImpls.getVariable(this, key);
      }
   }
}
