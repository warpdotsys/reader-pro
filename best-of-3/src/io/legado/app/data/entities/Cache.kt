package io.legado.app.data.entities

public data class Cache(key: String = "", value: String? = null, deadline: Long = 0L) {
   public final var deadline: Long
      internal set

   public final val key: String

   public final var value: String?
      internal set

   init {
      this.key = key;
      this.value = value;
      this.deadline = deadline;
   }

   public operator fun component1(): String {
      return this.key;
   }

   public operator fun component2(): String? {
      return this.value;
   }

   public operator fun component3(): Long {
      return this.deadline;
   }

   public fun copy(key: String = this.key, value: String? = this.value, deadline: Long = this.deadline): Cache {
      return new Cache(key, value, deadline);
   }

   public override fun toString(): String {
      return "Cache(key=${this.key}, value=${this.value}, deadline=${this.deadline})";
   }

   public override fun hashCode(): Int {
      return (this.key.hashCode() * 31 + (if (this.value == null) 0 else this.value.hashCode())) * 31 + java.lang.Long.hashCode(this.deadline);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is Cache) {
         return false;
      } else {
         val var2: Cache = other as Cache;
         if (!(this.key == (other as Cache).key)) {
            return false;
         } else if (!(this.value == var2.value)) {
            return false;
         } else {
            return this.deadline == var2.deadline;
         }
      }
   }

   fun Cache() {
      this(null, null, 0L, 7, null);
   }
}
