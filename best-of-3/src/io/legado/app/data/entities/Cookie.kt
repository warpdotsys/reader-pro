package io.legado.app.data.entities

public data class Cookie(url: String = "", cookie: String = "") {
   public final var cookie: String
      internal set

   public final var url: String
      internal set

   init {
      this.url = url;
      this.cookie = cookie;
   }

   public operator fun component1(): String {
      return this.url;
   }

   public operator fun component2(): String {
      return this.cookie;
   }

   public fun copy(url: String = this.url, cookie: String = this.cookie): Cookie {
      return new Cookie(url, cookie);
   }

   public override fun toString(): String {
      return "Cookie(url=${this.url}, cookie=${this.cookie})";
   }

   public override fun hashCode(): Int {
      return this.url.hashCode() * 31 + this.cookie.hashCode();
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is Cookie) {
         return false;
      } else {
         val var2: Cookie = other as Cookie;
         if (!(this.url == (other as Cookie).url)) {
            return false;
         } else {
            return this.cookie == var2.cookie;
         }
      }
   }

   fun Cookie() {
      this(null, null, 3, null);
   }
}
