package com.htmake.reader.entity

import java.util.UUID

public data class ActiveLicense(host: String = "*",
   userMaxLimit: Int = 15,
   expiredAt: Long = 0L,
   openApi: Boolean = false,
   simpleWebExpiredAt: Long = 1682870399000L,
   id: String = UUID.randomUUID().toString(),
   code: String = UUID.randomUUID().toString(),
   verified: Boolean = false,
   verifyTime: Long? = null,
   instances: Int = 1,
   type: String = "default",
   activeOrder: Int = 1,
   activeTime: Long = System.currentTimeMillis(),
   activeIp: String = "",
   activeEmail: String = "",
   lastOnlineIp: String = "",
   lastOnlineTime: Long? = null,
   errorMsg: String = ""
) {
   public final var activeEmail: String
      internal set

   public final var activeIp: String
      internal set

   public final var activeOrder: Int
      internal set

   public final var activeTime: Long
      internal set

   public final var code: String
      internal set

   public final var errorMsg: String
      internal set

   public final var expiredAt: Long
      internal set

   public final var host: String
      internal set

   public final var id: String
      internal set

   public final var instances: Int
      internal set

   public final var lastOnlineIp: String
      internal set

   public final var lastOnlineTime: Long?
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
      this.id = id;
      this.code = code;
      this.verified = verified;
      this.verifyTime = verifyTime;
      this.instances = instances;
      this.type = type;
      this.activeOrder = activeOrder;
      this.activeTime = activeTime;
      this.activeIp = activeIp;
      this.activeEmail = activeEmail;
      this.lastOnlineIp = lastOnlineIp;
      this.lastOnlineTime = lastOnlineTime;
      this.errorMsg = errorMsg;
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

   public operator fun component6(): String {
      return this.id;
   }

   public operator fun component7(): String {
      return this.code;
   }

   public operator fun component8(): Boolean {
      return this.verified;
   }

   public operator fun component9(): Long? {
      return this.verifyTime;
   }

   public operator fun component10(): Int {
      return this.instances;
   }

   public operator fun component11(): String {
      return this.type;
   }

   public operator fun component12(): Int {
      return this.activeOrder;
   }

   public operator fun component13(): Long {
      return this.activeTime;
   }

   public operator fun component14(): String {
      return this.activeIp;
   }

   public operator fun component15(): String {
      return this.activeEmail;
   }

   public operator fun component16(): String {
      return this.lastOnlineIp;
   }

   public operator fun component17(): Long? {
      return this.lastOnlineTime;
   }

   public operator fun component18(): String {
      return this.errorMsg;
   }

   public fun copy(
      host: String = this.host,
      userMaxLimit: Int = this.userMaxLimit,
      expiredAt: Long = this.expiredAt,
      openApi: Boolean = this.openApi,
      simpleWebExpiredAt: Long = this.simpleWebExpiredAt,
      id: String = this.id,
      code: String = this.code,
      verified: Boolean = this.verified,
      verifyTime: Long? = this.verifyTime,
      instances: Int = this.instances,
      type: String = this.type,
      activeOrder: Int = this.activeOrder,
      activeTime: Long = this.activeTime,
      activeIp: String = this.activeIp,
      activeEmail: String = this.activeEmail,
      lastOnlineIp: String = this.lastOnlineIp,
      lastOnlineTime: Long? = this.lastOnlineTime,
      errorMsg: String = this.errorMsg
   ): ActiveLicense {
      return new ActiveLicense(
         host,
         userMaxLimit,
         expiredAt,
         openApi,
         simpleWebExpiredAt,
         id,
         code,
         verified,
         verifyTime,
         instances,
         type,
         activeOrder,
         activeTime,
         activeIp,
         activeEmail,
         lastOnlineIp,
         lastOnlineTime,
         errorMsg
      );
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("ActiveLicense(host=")
         .append(this.host)
         .append(", userMaxLimit=")
         .append(this.userMaxLimit)
         .append(", expiredAt=")
         .append(this.expiredAt)
         .append(", openApi=")
         .append(this.openApi)
         .append(", simpleWebExpiredAt=")
         .append(this.simpleWebExpiredAt)
         .append(", id=")
         .append(this.id)
         .append(", code=")
         .append(this.code)
         .append(", verified=")
         .append(this.verified)
         .append(", verifyTime=")
         .append(this.verifyTime)
         .append(", instances=")
         .append(this.instances)
         .append(", type=")
         .append(this.type)
         .append(", activeOrder=");
      var1.append(this.activeOrder)
         .append(", activeTime=")
         .append(this.activeTime)
         .append(", activeIp=")
         .append(this.activeIp)
         .append(", activeEmail=")
         .append(this.activeEmail)
         .append(", lastOnlineIp=")
         .append(this.lastOnlineIp)
         .append(", lastOnlineTime=")
         .append(this.lastOnlineTime)
         .append(", errorMsg=")
         .append(this.errorMsg)
         .append(')');
      return var1.toString();
   }

   public override fun hashCode(): Int {
      var var10000: Int = ((this.host.hashCode() * 31 + Integer.hashCode(this.userMaxLimit)) * 31 + java.lang.Long.hashCode(this.expiredAt)) * 31;
      var var10001: Byte = this.openApi;
      if (this.openApi) {
         var10001 = 1;
      }

      var10000 = ((((var10000 + var10001) * 31 + java.lang.Long.hashCode(this.simpleWebExpiredAt)) * 31 + this.id.hashCode()) * 31 + this.code.hashCode()) * 31;
      var10001 = this.verified;
      if (this.verified) {
         var10001 = 1;
      }

      return (
               (
                        (
                                 (
                                          (
                                                   (
                                                            (
                                                                     (
                                                                              (
                                                                                       (var10000 + var10001) * 31
                                                                                          + (if (this.verifyTime == null) 0 else this.verifyTime.hashCode())
                                                                                    )
                                                                                    * 31
                                                                                 + Integer.hashCode(this.instances)
                                                                           )
                                                                           * 31
                                                                        + this.type.hashCode()
                                                                  )
                                                                  * 31
                                                               + Integer.hashCode(this.activeOrder)
                                                         )
                                                         * 31
                                                      + java.lang.Long.hashCode(this.activeTime)
                                                )
                                                * 31
                                             + this.activeIp.hashCode()
                                       )
                                       * 31
                                    + this.activeEmail.hashCode()
                              )
                              * 31
                           + this.lastOnlineIp.hashCode()
                     )
                     * 31
                  + (if (this.lastOnlineTime == null) 0 else this.lastOnlineTime.hashCode())
            )
            * 31
         + this.errorMsg.hashCode();
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is ActiveLicense) {
         return false;
      } else {
         val var2: ActiveLicense = other as ActiveLicense;
         if (!(this.host == (other as ActiveLicense).host)) {
            return false;
         } else if (this.userMaxLimit != var2.userMaxLimit) {
            return false;
         } else if (this.expiredAt != var2.expiredAt) {
            return false;
         } else if (this.openApi != var2.openApi) {
            return false;
         } else if (this.simpleWebExpiredAt != var2.simpleWebExpiredAt) {
            return false;
         } else if (!(this.id == var2.id)) {
            return false;
         } else if (!(this.code == var2.code)) {
            return false;
         } else if (this.verified != var2.verified) {
            return false;
         } else if (!(this.verifyTime == var2.verifyTime)) {
            return false;
         } else if (this.instances != var2.instances) {
            return false;
         } else if (!(this.type == var2.type)) {
            return false;
         } else if (this.activeOrder != var2.activeOrder) {
            return false;
         } else if (this.activeTime != var2.activeTime) {
            return false;
         } else if (!(this.activeIp == var2.activeIp)) {
            return false;
         } else if (!(this.activeEmail == var2.activeEmail)) {
            return false;
         } else if (!(this.lastOnlineIp == var2.lastOnlineIp)) {
            return false;
         } else if (!(this.lastOnlineTime == var2.lastOnlineTime)) {
            return false;
         } else {
            return this.errorMsg == var2.errorMsg;
         }
      }
   }

   fun ActiveLicense() {
      this(null, 0, 0L, false, 0L, null, null, false, null, 0, null, 0, 0L, null, null, null, null, null, 262143, null);
   }
}
