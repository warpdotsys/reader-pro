package io.legado.app.data.entities

public data class ReplaceRule(id: Long = System.currentTimeMillis(),
   name: String = "",
   group: String? = null,
   pattern: String = "",
   replacement: String = "",
   scope: String? = null,
   scopeTitle: Boolean = false,
   scopeContent: Boolean = true,
   isEnabled: Boolean = true,
   isRegex: Boolean = false,
   timeoutMillisecond: Long = 3000L,
   order: Int = 0
) {
   public final var group: String?
      internal set

   public final var id: Long
      internal set

   public final var isEnabled: Boolean
      internal set

   public final var isRegex: Boolean
      internal set

   public final var name: String
      internal set

   public final var order: Int
      internal set

   public final var pattern: String
      internal set

   public final var replacement: String
      internal set

   public final var scope: String?
      internal set

   public final var scopeContent: Boolean
      internal set

   public final var scopeTitle: Boolean
      internal set

   public final var timeoutMillisecond: Long
      internal set

   init {
      this.id = id;
      this.name = name;
      this.group = group;
      this.pattern = pattern;
      this.replacement = replacement;
      this.scope = scope;
      this.scopeTitle = scopeTitle;
      this.scopeContent = scopeContent;
      this.isEnabled = isEnabled;
      this.isRegex = isRegex;
      this.timeoutMillisecond = timeoutMillisecond;
      this.order = order;
   }

   public operator fun component1(): Long {
      return this.id;
   }

   public operator fun component2(): String {
      return this.name;
   }

   public operator fun component3(): String? {
      return this.group;
   }

   public operator fun component4(): String {
      return this.pattern;
   }

   public operator fun component5(): String {
      return this.replacement;
   }

   public operator fun component6(): String? {
      return this.scope;
   }

   public operator fun component7(): Boolean {
      return this.scopeTitle;
   }

   public operator fun component8(): Boolean {
      return this.scopeContent;
   }

   public operator fun component9(): Boolean {
      return this.isEnabled;
   }

   public operator fun component10(): Boolean {
      return this.isRegex;
   }

   public operator fun component11(): Long {
      return this.timeoutMillisecond;
   }

   public operator fun component12(): Int {
      return this.order;
   }

   public fun copy(
      id: Long = this.id,
      name: String = this.name,
      group: String? = this.group,
      pattern: String = this.pattern,
      replacement: String = this.replacement,
      scope: String? = this.scope,
      scopeTitle: Boolean = this.scopeTitle,
      scopeContent: Boolean = this.scopeContent,
      isEnabled: Boolean = this.isEnabled,
      isRegex: Boolean = this.isRegex,
      timeoutMillisecond: Long = this.timeoutMillisecond,
      order: Int = this.order
   ): ReplaceRule {
      return new ReplaceRule(id, name, group, pattern, replacement, scope, scopeTitle, scopeContent, isEnabled, isRegex, timeoutMillisecond, order);
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("ReplaceRule(id=")
         .append(this.id)
         .append(", name=")
         .append(this.name)
         .append(", group=")
         .append(this.group)
         .append(", pattern=")
         .append(this.pattern)
         .append(", replacement=")
         .append(this.replacement)
         .append(", scope=")
         .append(this.scope)
         .append(", scopeTitle=")
         .append(this.scopeTitle)
         .append(", scopeContent=")
         .append(this.scopeContent)
         .append(", isEnabled=")
         .append(this.isEnabled)
         .append(", isRegex=")
         .append(this.isRegex)
         .append(", timeoutMillisecond=")
         .append(this.timeoutMillisecond)
         .append(", order=");
      var1.append(this.order).append(')');
      return var1.toString();
   }

   public override fun hashCode(): Int {
      var var10000: Int = (
            (
                     (
                              ((java.lang.Long.hashCode(this.id) * 31 + this.name.hashCode()) * 31 + (if (this.group == null) 0 else this.group.hashCode()))
                                    * 31
                                 + this.pattern.hashCode()
                           )
                           * 31
                        + this.replacement.hashCode()
                  )
                  * 31
               + (if (this.scope == null) 0 else this.scope.hashCode())
         )
         * 31;
      var var10001: Byte = this.scopeTitle;
      if (this.scopeTitle) {
         var10001 = 1;
      }

      var10000 = (var10000 + var10001) * 31;
      var10001 = this.scopeContent;
      if (this.scopeContent) {
         var10001 = 1;
      }

      var10000 = (var10000 + var10001) * 31;
      var10001 = this.isEnabled;
      if (this.isEnabled) {
         var10001 = 1;
      }

      var10000 = (var10000 + var10001) * 31;
      var10001 = this.isRegex;
      if (this.isRegex) {
         var10001 = 1;
      }

      return ((var10000 + var10001) * 31 + java.lang.Long.hashCode(this.timeoutMillisecond)) * 31 + Integer.hashCode(this.order);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is ReplaceRule) {
         return false;
      } else {
         val var2: ReplaceRule = other as ReplaceRule;
         if (this.id != (other as ReplaceRule).id) {
            return false;
         } else if (!(this.name == var2.name)) {
            return false;
         } else if (!(this.group == var2.group)) {
            return false;
         } else if (!(this.pattern == var2.pattern)) {
            return false;
         } else if (!(this.replacement == var2.replacement)) {
            return false;
         } else if (!(this.scope == var2.scope)) {
            return false;
         } else if (this.scopeTitle != var2.scopeTitle) {
            return false;
         } else if (this.scopeContent != var2.scopeContent) {
            return false;
         } else if (this.isEnabled != var2.isEnabled) {
            return false;
         } else if (this.isRegex != var2.isRegex) {
            return false;
         } else if (this.timeoutMillisecond != var2.timeoutMillisecond) {
            return false;
         } else {
            return this.order == var2.order;
         }
      }
   }

   fun ReplaceRule() {
      this(0L, null, null, null, null, null, false, false, false, false, 0L, 0, 4095, null);
   }
}
