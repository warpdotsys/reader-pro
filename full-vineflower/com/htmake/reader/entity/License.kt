package com.htmake.reader.entity

import java.util.UUID

public data class License(host: String = "*",
   userMaxLimit: Int = 15,
   expiredAt: Long = 0L,
   openApi: Boolean = false,
   simpleWebExpiredAt: Long = 1688140799000L,
   instances: Int = 1,
   type: String = "default",
   id: String = UUID.randomUUID().toString(),
   code: String = UUID.randomUUID().toString(),
   verified: Boolean = false,
   verifyTime: Long? = null
) {
   public final var code: String
      internal set

   public final var expiredAt: Long
      internal set

   public final var host: String
      internal set

   public final var id: String
      internal set

   public final var instances: Int
      internal set

   public final var openApi: Boolean
      internal set

   public final var simpleWebExpiredAt: Long
      internal set

   public final var type: String
      internal set

   public final var userMaxLimit: Int
      internal set

   public final var verified: Boolean
      internal set

   public final var verifyTime: Long?
      internal set

   init {
      this.host = host;
      this.userMaxLimit = userMaxLimit;
      this.expiredAt = expiredAt;
      this.openApi = openApi;
      this.simpleWebExpiredAt = simpleWebExpiredAt;
      this.instances = instances;
      this.type = type;
      this.id = id;
      this.code = code;
      this.verified = verified;
      this.verifyTime = verifyTime;
   }

   public fun isValid(): Boolean {
      return this.expiredAt == 0L || this.expiredAt >= System.currentTimeMillis();
   }

   public fun validHost(queryHost: String): Boolean {
      if (!this.isValid()) {
         return false;
      } else if (queryHost.length() == 0) {
         return false;
      } else if ("*".equals(this.host)) {
         return true;
      } else {
         val var14: java.util.List = StringsKt.split$default(
            StringsKt.split$default(queryHost, new java.lang.String[]{":"}, false, 0, 6, null).get(0) as java.lang.CharSequence,
            new java.lang.String[]{"."},
            false,
            0,
            6,
            null
         );

         for (java.lang.String hostname : StringsKt.split$default(this.host, new java.lang.String[]{","}, false, 0, 6, null)) {
            val parts: java.util.List = StringsKt.split$default(hostname, new java.lang.String[]{"."}, false, 0, 6, null);
            if (parts.size() == var14.size()) {
               var var18: Boolean = true;
               var var9: Int = 0;
               val var10: Int = parts.size();
               if (0 < var10) {
                  do {
                     val i: Int = var9++;
                     if (!"*".equals(parts.get(i)) && !(parts.get(i) as java.lang.String).equals(var14.get(i))) {
                        var18 = false;
                     }
                  } while (var9 < var10);
               }

               if (var18) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public fun toActiveLicense(): ActiveLicense {
      return new ActiveLicense(
         this.host,
         this.userMaxLimit,
         this.expiredAt,
         this.openApi,
         this.simpleWebExpiredAt,
         this.id,
         this.code,
         this.verified,
         this.verifyTime,
         this.instances,
         this.type,
         0,
         0L,
         null,
         null,
         null,
         null,
         null,
         260096,
         null
      );
   }

   public operator fun component1(): String {
      return this.host;
   }

   public operator fun component2(): Int {
      return this.userMaxLimit;
   }

   public operator fun component3(): Long {
      return this.expiredAt;
   }

   public operator fun component4(): Boolean {
      return this.openApi;
   }

   public operator fun component5(): Long {
      return this.simpleWebExpiredAt;
   }

   public operator fun component6(): Int {
      return this.instances;
   }

   public operator fun component7(): String {
      return this.type;
   }

   public operator fun component8(): String {
      return this.id;
   }

   public operator fun component9(): String {
      return this.code;
   }

   public operator fun component10(): Boolean {
      return this.verified;
   }

   public operator fun component11(): Long? {
      return this.verifyTime;
   }

   public fun copy(
      host: String = this.host,
      userMaxLimit: Int = this.userMaxLimit,
      expiredAt: Long = this.expiredAt,
      openApi: Boolean = this.openApi,
      simpleWebExpiredAt: Long = this.simpleWebExpiredAt,
      instances: Int = this.instances,
      type: String = this.type,
      id: String = this.id,
      code: String = this.code,
      verified: Boolean = this.verified,
      verifyTime: Long? = this.verifyTime
   ): License {
      return new License(host, userMaxLimit, expiredAt, openApi, simpleWebExpiredAt, instances, type, id, code, verified, verifyTime);
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("License(host=")
         .append(this.host)
         .append(", userMaxLimit=")
         .append(this.userMaxLimit)
         .append(", expiredAt=")
         .append(this.expiredAt)
         .append(", openApi=")
         .append(this.openApi)
         .append(", simpleWebExpiredAt=")
         .append(this.simpleWebExpiredAt)
         .append(", instances=")
         .append(this.instances)
         .append(", type=")
         .append(this.type)
         .append(", id=")
         .append(this.id)
         .append(", code=")
         .append(this.code)
         .append(", verified=")
         .append(this.verified)
         .append(", verifyTime=")
         .append(this.verifyTime)
         .append(')');
      return var1.toString();
   }

   public override fun hashCode(): Int {
      var var10000: Int = ((this.host.hashCode() * 31 + Integer.hashCode(this.userMaxLimit)) * 31 + java.lang.Long.hashCode(this.expiredAt)) * 31;
      var var10001: Byte = this.openApi;
      if (this.openApi) {
         var10001 = 1;
      }

      var10000 = (
            (
                     (
                              (((var10000 + var10001) * 31 + java.lang.Long.hashCode(this.simpleWebExpiredAt)) * 31 + Integer.hashCode(this.instances)) * 31
                                 + this.type.hashCode()
                           )
                           * 31
                        + this.id.hashCode()
                  )
                  * 31
               + this.code.hashCode()
         )
         * 31;
      var10001 = this.verified;
      if (this.verified) {
         var10001 = 1;
      }

      return (var10000 + var10001) * 31 + (if (this.verifyTime == null) 0 else this.verifyTime.hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is License) {
         return false;
      } else {
         val var2: License = other as License;
         if (!(this.host == (other as License).host)) {
            return false;
         } else if (this.userMaxLimit != var2.userMaxLimit) {
            return false;
         } else if (this.expiredAt != var2.expiredAt) {
            return false;
         } else if (this.openApi != var2.openApi) {
            return false;
         } else if (this.simpleWebExpiredAt != var2.simpleWebExpiredAt) {
            return false;
         } else if (this.instances != var2.instances) {
            return false;
         } else if (!(this.type == var2.type)) {
            return false;
         } else if (!(this.id == var2.id)) {
            return false;
         } else if (!(this.code == var2.code)) {
            return false;
         } else if (this.verified != var2.verified) {
            return false;
         } else {
            return this.verifyTime == var2.verifyTime;
         }
      }
   }

   fun License() {
      this(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
   }
}
