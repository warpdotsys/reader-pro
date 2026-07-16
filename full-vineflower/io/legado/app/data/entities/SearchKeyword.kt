package io.legado.app.data.entities

public data class SearchKeyword(word: String = "", usage: Int = 1, lastUseTime: Long = System.currentTimeMillis()) {
   public final var lastUseTime: Long
      internal set

   public final var usage: Int
      internal set

   public final var word: String
      internal set

   init {
      this.word = word;
      this.usage = usage;
      this.lastUseTime = lastUseTime;
   }

   public operator fun component1(): String {
      return this.word;
   }

   public operator fun component2(): Int {
      return this.usage;
   }

   public operator fun component3(): Long {
      return this.lastUseTime;
   }

   public fun copy(word: String = this.word, usage: Int = this.usage, lastUseTime: Long = this.lastUseTime): SearchKeyword {
      return new SearchKeyword(word, usage, lastUseTime);
   }

   public override fun toString(): String {
      return "SearchKeyword(word=${this.word}, usage=${this.usage}, lastUseTime=${this.lastUseTime})";
   }

   public override fun hashCode(): Int {
      return (this.word.hashCode() * 31 + Integer.hashCode(this.usage)) * 31 + java.lang.Long.hashCode(this.lastUseTime);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is SearchKeyword) {
         return false;
      } else {
         val var2: SearchKeyword = other as SearchKeyword;
         if (!(this.word == (other as SearchKeyword).word)) {
            return false;
         } else if (this.usage != var2.usage) {
            return false;
         } else {
            return this.lastUseTime == var2.lastUseTime;
         }
      }
   }

   fun SearchKeyword() {
      this(null, 0, 0L, 7, null);
   }
}
