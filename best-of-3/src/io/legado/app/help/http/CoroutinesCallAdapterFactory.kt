package io.legado.app.help.http

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.jvm.functions.Function1
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CompletableDeferredKt
import kotlinx.coroutines.Deferred
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.CallAdapter.Factory

public class CoroutinesCallAdapterFactory private constructor() : Factory {
   public override operator fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
      if (!(Deferred::class.java == Factory.getRawType(returnType))) {
         return null;
      } else if (returnType !is ParameterizedType) {
         throw (new IllegalStateException("Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>".toString())) as java.lang.Throwable;
      } else {
         val var10: Type = Factory.getParameterUpperBound(0, returnType as ParameterizedType);
         val var10000: CallAdapter;
         if (Factory.getRawType(var10) == Response::class.java) {
            if (var10 !is ParameterizedType) {
               throw (new IllegalStateException("Response must be parameterized as Response<Foo> or Response<out Foo>".toString())) as java.lang.Throwable;
            }

            val var13: Type = Factory.getParameterUpperBound(0, var10 as ParameterizedType);
            var10000 = new CoroutinesCallAdapterFactory.ResponseCallAdapter(var13);
         } else {
            var10000 = new CoroutinesCallAdapterFactory.BodyCallAdapter(var10);
         }

         return var10000;
      }
   }

   private class BodyCallAdapter<T>(responseType: Type) : CallAdapter<T, Deferred<? extends T>> {
      private final val responseType: Type

      init {
         this.responseType = responseType;
      }

      public override fun responseType(): Type {
         return this.responseType;
      }

      public open fun adapt(call: Call<Any>): Deferred<Any> {
         val deferred: CompletableDeferred = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
         deferred.invokeOnCompletion((new Function1<java.lang.Throwable, Unit>(deferred, call) {
            {
               super(1);
               this.$deferred = `$deferred`;
               this.$call = `$call`;
            }

            public final void invoke(@Nullable java.lang.Throwable it) {
               if (this.$deferred.isCancelled()) {
                  this.$call.cancel();
               }
            }
         }) as (java.lang.Throwable?) -> Unit);
         call.enqueue(new Callback<T>(deferred) {
            {
               this.$deferred = `$deferred`;
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull java.lang.Throwable t) {
               this.$deferred.completeExceptionally(t);
            }

            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
               if (response.isSuccessful()) {
                  val var10000: CompletableDeferred = this.$deferred;
                  val var10001: Any = response.body();
                  var10000.complete(var10001);
               } else {
                  this.$deferred.completeExceptionally(new HttpException(response));
               }
            }
         });
         return deferred;
      }
   }

   public companion object {
      public fun create(): CoroutinesCallAdapterFactory {
         return new CoroutinesCallAdapterFactory(null);
      }
   }

   private class ResponseCallAdapter<T>(responseType: Type) : CallAdapter<T, Deferred<? extends Response<T>>> {
      private final val responseType: Type

      init {
         this.responseType = responseType;
      }

      public override fun responseType(): Type {
         return this.responseType;
      }

      public open fun adapt(call: Call<Any>): Deferred<Response<Any>> {
         val deferred: CompletableDeferred = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
         deferred.invokeOnCompletion((new Function1<java.lang.Throwable, Unit>(deferred, call) {
            {
               super(1);
               this.$deferred = `$deferred`;
               this.$call = `$call`;
            }

            public final void invoke(@Nullable java.lang.Throwable it) {
               if (this.$deferred.isCancelled()) {
                  this.$call.cancel();
               }
            }
         }) as (java.lang.Throwable?) -> Unit);
         call.enqueue(new Callback<T>(deferred) {
            {
               this.$deferred = `$deferred`;
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull java.lang.Throwable t) {
               this.$deferred.completeExceptionally(t);
            }

            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
               this.$deferred.complete(response);
            }
         });
         return deferred;
      }
   }
}
