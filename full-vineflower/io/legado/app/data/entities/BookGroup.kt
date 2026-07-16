package io.legado.app.data.entities

public data class BookGroup(groupId: Long = 0L, groupName: String = "", cover: String? = null, order: Int = 0, show: Boolean = true) {
   public final var cover: String?
      internal set

   public final var groupId: Long
      internal set

   public final var groupName: String
      internal set

   public final var order: Int
      internal set

   public final var show: Boolean
      internal set

   init {
      this.groupId = groupId;
      this.groupName = groupName;
      this.cover = cover;
      this.order = order;
      this.show = show;
   }

   public operator fun component1(): Long {
      return this.groupId;
   }

   public operator fun component2(): String {
      return this.groupName;
   }

   public operator fun component3(): String? {
      return this.cover;
   }

   public operator fun component4(): Int {
      return this.order;
   }

   public operator fun component5(): Boolean {
      return this.show;
   }

   public fun copy(
      groupId: Long = this.groupId,
      groupName: String = this.groupName,
      cover: String? = this.cover,
      order: Int = this.order,
      show: Boolean = this.show
   ): BookGroup {
      return new BookGroup(groupId, groupName, cover, order, show);
   }

   public override fun toString(): String {
      return "BookGroup(groupId=${this.groupId}, groupName=${this.groupName}, cover=${this.cover}, order=${this.order}, show=${this.show})";
   }

   public override fun hashCode(): Int {
      val var10000: Int = (
            ((java.lang.Long.hashCode(this.groupId) * 31 + this.groupName.hashCode()) * 31 + (if (this.cover == null) 0 else this.cover.hashCode())) * 31
               + Integer.hashCode(this.order)
         )
         * 31;
      var var10001: Byte = this.show;
      if (this.show) {
         var10001 = 1;
      }

      return var10000 + var10001;
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is BookGroup) {
         return false;
      } else {
         val var2: BookGroup = other as BookGroup;
         if (this.groupId != (other as BookGroup).groupId) {
            return false;
         } else if (!(this.groupName == var2.groupName)) {
            return false;
         } else if (!(this.cover == var2.cover)) {
            return false;
         } else if (this.order != var2.order) {
            return false;
         } else {
            return this.show == var2.show;
         }
      }
   }

   fun BookGroup() {
      this(0L, null, null, 0, false, 31, null);
   }
}
