package io.legado.app.help.http

public data class Res(url: String, body: String?) {
   public final val body: String?
   public final val url: String

   init {
      this.url = url;
      this.body = body;
   }

   public operator fun component1(): String {
      return this.url;
   }

   public operator fun component2(): String? {
      return this.body;
   }

   public fun copy(url: String = this.url, body: String? = this.body): Res {
      return new Res(url, body);
   }

   public override fun toString(): String {
      return "Res(url=${this.url}, body=${this.body})";
   }

   public override fun hashCode(): Int {
      return this.url.hashCode() * 31 + (if (this.body == null) 0 else this.body.hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is Res) {
         return false;
      } else {
         val var2: Res = other as Res;
         if (!(this.url == (other as Res).url)) {
            return false;
         } else {
            return this.body == var2.body;
         }
      }
   }
}
