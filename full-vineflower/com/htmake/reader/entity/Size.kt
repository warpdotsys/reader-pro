package com.htmake.reader.entity

public data class Size(width: Double, height: Double) {
   public final val height: Double
   public final val width: Double

   init {
      this.width = width;
      this.height = height;
   }

   public operator fun component1(): Double {
      return this.width;
   }

   public operator fun component2(): Double {
      return this.height;
   }

   public fun copy(width: Double = this.width, height: Double = this.height): Size {
      return new Size(width, height);
   }

   public override fun toString(): String {
      return "Size(width=${this.width}, height=${this.height})";
   }

   public override fun hashCode(): Int {
      return java.lang.Double.hashCode(this.width) * 31 + java.lang.Double.hashCode(this.height);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is Size) {
         return false;
      } else {
         val var2: Size = other as Size;
         if (!(this.width == (other as Size).width)) {
            return false;
         } else {
            return this.height == var2.height;
         }
      }
   }
}
