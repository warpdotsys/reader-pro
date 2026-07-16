package com.htmake.reader.entity

public data class MongoFile(path: String = "",
   content: String = "",
   created_at: Long = System.currentTimeMillis(),
   updated_at: Long = System.currentTimeMillis()
) {
   public final var content: String
      internal set

   public final var created_at: Long
      internal set

   public final var path: String
      internal set

   public final var updated_at: Long
      internal set

   init {
      this.path = path;
      this.content = content;
      this.created_at = created_at;
      this.updated_at = updated_at;
   }

   public operator fun component1(): String {
      return this.path;
   }

   public operator fun component2(): String {
      return this.content;
   }

   public operator fun component3(): Long {
      return this.created_at;
   }

   public operator fun component4(): Long {
      return this.updated_at;
   }

   public fun copy(path: String = this.path, content: String = this.content, created_at: Long = this.created_at, updated_at: Long = this.updated_at): MongoFile {
      return new MongoFile(path, content, created_at, updated_at);
   }

   public override fun toString(): String {
      return "MongoFile(path=${this.path}, content=${this.content}, created_at=${this.created_at}, updated_at=${this.updated_at})";
   }

   public override fun hashCode(): Int {
      return ((this.path.hashCode() * 31 + this.content.hashCode()) * 31 + java.lang.Long.hashCode(this.created_at)) * 31
         + java.lang.Long.hashCode(this.updated_at);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is MongoFile) {
         return false;
      } else {
         val var2: MongoFile = other as MongoFile;
         if (!(this.path == (other as MongoFile).path)) {
            return false;
         } else if (!(this.content == var2.content)) {
            return false;
         } else if (this.created_at != var2.created_at) {
            return false;
         } else {
            return this.updated_at == var2.updated_at;
         }
      }
   }

   fun MongoFile() {
      this(null, null, 0L, 0L, 15, null);
   }
}
