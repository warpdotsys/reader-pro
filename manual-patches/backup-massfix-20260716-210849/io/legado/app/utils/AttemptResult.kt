package io.legado.app.utils

public data class AttemptResult<T> @PublishedApi  internal constructor(value: Any?, error: Throwable?) {
   public final val error: Throwable?

   public final val hasValue: Boolean
      public final inline get() {
         return this.getError() == null;
      }


   public final val isError: Boolean
      public final inline get() {
         return this.getError() != null;
      }


   public final val value: Any?

   init {
      this.value = (T)value;
      this.error = error;
   }

   public inline fun <R> then(f: (Any) -> R): AttemptResult<R> {
      if (this.getError() != null) {
         return this;
      } else {
         var var8: Any = null;
         var `error$iv`: java.lang.Throwable = null;

         try {
            var8 = f.invoke(this.getValue());
         } catch (var7: java.lang.Throwable) {
            `error$iv` = var7;
         }

         return (AttemptResult<R>)(new AttemptResult<>(var8, `error$iv`));
      }
   }

   public operator fun component1(): Any? {
      return this.value;
   }

   public operator fun component2(): Throwable? {
      return this.error;
   }

   public fun copy(value: Any? = this.value, error: Throwable? = this.error): AttemptResult<Any> {
      return new AttemptResult<>((T)value, error);
   }

   public override fun toString(): String {
      return "AttemptResult(value=${this.value}, error=${this.error})";
   }

   public override fun hashCode(): Int {
      return (if (this.value == null) 0 else this.value.hashCode()) * 31 + (if (this.error == null) 0 else this.error.hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is AttemptResult) {
         return false;
      } else {
         val var2: AttemptResult = other as AttemptResult;
         if (!(this.value == (other as AttemptResult).value)) {
            return false;
         } else {
            return this.error == var2.error;
         }
      }
   }
}
