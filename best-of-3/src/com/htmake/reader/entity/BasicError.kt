package com.htmake.reader.entity

public data class BasicError(error: String, exception: String, message: String, path: String, status: Int, timestamp: Long) {
   public final val error: String
   public final val exception: String
   public final val message: String
   public final val path: String
   public final val status: Int
   public final val timestamp: Long

   init {
      this.error = error;
      this.exception = exception;
      this.message = message;
      this.path = path;
      this.status = status;
      this.timestamp = timestamp;
   }

   public operator fun component1(): String {
      return this.error;
   }

   public operator fun component2(): String {
      return this.exception;
   }

   public operator fun component3(): String {
      return this.message;
   }

   public operator fun component4(): String {
      return this.path;
   }

   public operator fun component5(): Int {
      return this.status;
   }

   public operator fun component6(): Long {
      return this.timestamp;
   }

   public fun copy(
      error: String = this.error,
      exception: String = this.exception,
      message: String = this.message,
      path: String = this.path,
      status: Int = this.status,
      timestamp: Long = this.timestamp
   ): BasicError {
      return new BasicError(error, exception, message, path, status, timestamp);
   }

   public override fun toString(): String {
      return "BasicError(error=${this.error}, exception=${this.exception}, message=${this.message}, path=${this.path}, status=${this.status}, timestamp=${this.timestamp})";
   }

   public override fun hashCode(): Int {
      return (
               (((this.error.hashCode() * 31 + this.exception.hashCode()) * 31 + this.message.hashCode()) * 31 + this.path.hashCode()) * 31
                  + Integer.hashCode(this.status)
            )
            * 31
         + java.lang.Long.hashCode(this.timestamp);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is BasicError) {
         return false;
      } else {
         val var2: BasicError = other as BasicError;
         if (!(this.error == (other as BasicError).error)) {
            return false;
         } else if (!(this.exception == var2.exception)) {
            return false;
         } else if (!(this.message == var2.message)) {
            return false;
         } else if (!(this.path == var2.path)) {
            return false;
         } else if (this.status != var2.status) {
            return false;
         } else {
            return this.timestamp == var2.timestamp;
         }
      }
   }
}
