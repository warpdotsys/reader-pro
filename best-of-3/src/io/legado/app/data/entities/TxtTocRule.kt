package io.legado.app.data.entities

public data class TxtTocRule(id: Long = System.currentTimeMillis(), name: String = "", rule: String = "", serialNumber: Int = -1, enable: Boolean = true) {
   public final var enable: Boolean
      internal set

   public final var id: Long
      internal set

   public final var name: String
      internal set

   public final var rule: String
      internal set

   public final var serialNumber: Int
      internal set

   init {
      this.id = id;
      this.name = name;
      this.rule = rule;
      this.serialNumber = serialNumber;
      this.enable = enable;
   }

   public operator fun component1(): Long {
      return this.id;
   }

   public operator fun component2(): String {
      return this.name;
   }

   public operator fun component3(): String {
      return this.rule;
   }

   public operator fun component4(): Int {
      return this.serialNumber;
   }

   public operator fun component5(): Boolean {
      return this.enable;
   }

   public fun copy(id: Long = this.id, name: String = this.name, rule: String = this.rule, serialNumber: Int = this.serialNumber, enable: Boolean = this.enable): TxtTocRule {
      return new TxtTocRule(id, name, rule, serialNumber, enable);
   }

   public override fun toString(): String {
      return "TxtTocRule(id=${this.id}, name=${this.name}, rule=${this.rule}, serialNumber=${this.serialNumber}, enable=${this.enable})";
   }

   public override fun hashCode(): Int {
      val var10000: Int = (
            ((java.lang.Long.hashCode(this.id) * 31 + this.name.hashCode()) * 31 + this.rule.hashCode()) * 31 + Integer.hashCode(this.serialNumber)
         )
         * 31;
      var var10001: Byte = this.enable;
      if (this.enable) {
         var10001 = 1;
      }

      return var10000 + var10001;
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is TxtTocRule) {
         return false;
      } else {
         val var2: TxtTocRule = other as TxtTocRule;
         if (this.id != (other as TxtTocRule).id) {
            return false;
         } else if (!(this.name == var2.name)) {
            return false;
         } else if (!(this.rule == var2.rule)) {
            return false;
         } else if (this.serialNumber != var2.serialNumber) {
            return false;
         } else {
            return this.enable == var2.enable;
         }
      }
   }

   fun TxtTocRule() {
      this(0L, null, null, 0, false, 31, null);
   }
}
