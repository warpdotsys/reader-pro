package com.htmake.reader.entity

public data class User(username: String = "",
   password: String = "",
   salt: String = "",
   token: String = "",
   last_login_at: Long = System.currentTimeMillis(),
   created_at: Long = System.currentTimeMillis(),
   enable_webdav: Boolean = false,
   token_map: Map<String, Long>? = null,
   enable_local_store: Boolean = false,
   enable_book_source: Boolean = true,
   enable_rss_source: Boolean = true,
   book_source_limit: Int = 100,
   book_limit: Int = 200
) {
   public final var book_limit: Int
      internal set

   public final var book_source_limit: Int
      internal set

   public final var created_at: Long
      internal set

   public final var enable_book_source: Boolean
      internal set

   public final var enable_local_store: Boolean
      internal set

   public final var enable_rss_source: Boolean
      internal set

   public final var enable_webdav: Boolean
      internal set

   public final var last_login_at: Long
      internal set

   public final var password: String
      internal set

   public final var salt: String
      internal set

   public final var token: String
      internal set

   public final var token_map: Map<String, Long>?
      internal set

   public final var username: String
      internal set

   init {
      this.username = username;
      this.password = password;
      this.salt = salt;
      this.token = token;
      this.last_login_at = last_login_at;
      this.created_at = created_at;
      this.enable_webdav = enable_webdav;
      this.token_map = token_map;
      this.enable_local_store = enable_local_store;
      this.enable_book_source = enable_book_source;
      this.enable_rss_source = enable_rss_source;
      this.book_source_limit = book_source_limit;
      this.book_limit = book_limit;
   }

   public operator fun component1(): String {
      return this.username;
   }

   public operator fun component2(): String {
      return this.password;
   }

   public operator fun component3(): String {
      return this.salt;
   }

   public operator fun component4(): String {
      return this.token;
   }

   public operator fun component5(): Long {
      return this.last_login_at;
   }

   public operator fun component6(): Long {
      return this.created_at;
   }

   public operator fun component7(): Boolean {
      return this.enable_webdav;
   }

   public operator fun component8(): Map<String, Long>? {
      return this.token_map;
   }

   public operator fun component9(): Boolean {
      return this.enable_local_store;
   }

   public operator fun component10(): Boolean {
      return this.enable_book_source;
   }

   public operator fun component11(): Boolean {
      return this.enable_rss_source;
   }

   public operator fun component12(): Int {
      return this.book_source_limit;
   }

   public operator fun component13(): Int {
      return this.book_limit;
   }

   public fun copy(
      username: String = this.username,
      password: String = this.password,
      salt: String = this.salt,
      token: String = this.token,
      last_login_at: Long = this.last_login_at,
      created_at: Long = this.created_at,
      enable_webdav: Boolean = this.enable_webdav,
      token_map: Map<String, Long>? = this.token_map,
      enable_local_store: Boolean = this.enable_local_store,
      enable_book_source: Boolean = this.enable_book_source,
      enable_rss_source: Boolean = this.enable_rss_source,
      book_source_limit: Int = this.book_source_limit,
      book_limit: Int = this.book_limit
   ): User {
      return new User(
         username,
         password,
         salt,
         token,
         last_login_at,
         created_at,
         enable_webdav,
         token_map,
         enable_local_store,
         enable_book_source,
         enable_rss_source,
         book_source_limit,
         book_limit
      );
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("User(username=")
         .append(this.username)
         .append(", password=")
         .append(this.password)
         .append(", salt=")
         .append(this.salt)
         .append(", token=")
         .append(this.token)
         .append(", last_login_at=")
         .append(this.last_login_at)
         .append(", created_at=")
         .append(this.created_at)
         .append(", enable_webdav=")
         .append(this.enable_webdav)
         .append(", token_map=")
         .append(this.token_map)
         .append(", enable_local_store=")
         .append(this.enable_local_store)
         .append(", enable_book_source=")
         .append(this.enable_book_source)
         .append(", enable_rss_source=")
         .append(this.enable_rss_source)
         .append(", book_source_limit=");
      var1.append(this.book_source_limit).append(", book_limit=").append(this.book_limit).append(')');
      return var1.toString();
   }

   public override fun hashCode(): Int {
      var var10000: Int = (
            (
                     (((this.username.hashCode() * 31 + this.password.hashCode()) * 31 + this.salt.hashCode()) * 31 + this.token.hashCode()) * 31
                        + java.lang.Long.hashCode(this.last_login_at)
                  )
                  * 31
               + java.lang.Long.hashCode(this.created_at)
         )
         * 31;
      var var10001: Byte = this.enable_webdav;
      if (this.enable_webdav) {
         var10001 = 1;
      }

      var10000 = ((var10000 + var10001) * 31 + (if (this.token_map == null) 0 else this.token_map.hashCode())) * 31;
      var10001 = this.enable_local_store;
      if (this.enable_local_store) {
         var10001 = 1;
      }

      var10000 = (var10000 + var10001) * 31;
      var10001 = this.enable_book_source;
      if (this.enable_book_source) {
         var10001 = 1;
      }

      var10000 = (var10000 + var10001) * 31;
      var10001 = this.enable_rss_source;
      if (this.enable_rss_source) {
         var10001 = 1;
      }

      return ((var10000 + var10001) * 31 + Integer.hashCode(this.book_source_limit)) * 31 + Integer.hashCode(this.book_limit);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is User) {
         return false;
      } else {
         val var2: User = other as User;
         if (!(this.username == (other as User).username)) {
            return false;
         } else if (!(this.password == var2.password)) {
            return false;
         } else if (!(this.salt == var2.salt)) {
            return false;
         } else if (!(this.token == var2.token)) {
            return false;
         } else if (this.last_login_at != var2.last_login_at) {
            return false;
         } else if (this.created_at != var2.created_at) {
            return false;
         } else if (this.enable_webdav != var2.enable_webdav) {
            return false;
         } else if (!(this.token_map == var2.token_map)) {
            return false;
         } else if (this.enable_local_store != var2.enable_local_store) {
            return false;
         } else if (this.enable_book_source != var2.enable_book_source) {
            return false;
         } else if (this.enable_rss_source != var2.enable_rss_source) {
            return false;
         } else if (this.book_source_limit != var2.book_source_limit) {
            return false;
         } else {
            return this.book_limit == var2.book_limit;
         }
      }
   }

   fun User() {
      this(null, null, null, null, 0L, 0L, false, null, false, false, false, 0, 0, 8191, null);
   }
}
