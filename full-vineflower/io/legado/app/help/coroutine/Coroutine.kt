package io.legado.app.help.coroutine

import java.util.concurrent.CancellationException
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.jvm.functions.Function2
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.InlineMarker
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineScopeKt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutKt
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class Coroutine<T>(scope: CoroutineScope,
   context: CoroutineContext = Dispatchers.getIO() as CoroutineContext,
   block: (CoroutineScope, Continuation<Any>) -> Any?
) {
   private final var cancel: io.legado.app.help.coroutine.Coroutine.VoidCallback?
   private final var error: io.legado.app.help.coroutine.Coroutine.Callback<Throwable>?
   private final var errorReturn: io.legado.app.help.coroutine.Coroutine.Result<Any>?
   private final var finally: io.legado.app.help.coroutine.Coroutine.VoidCallback?

   public final val isActive: Boolean
      public final get() {
         return this.job.isActive();
      }


   public final val isCancelled: Boolean
      public final get() {
         return this.job.isCancelled();
      }


   public final val isCompleted: Boolean
      public final get() {
         return this.job.isCompleted();
      }


   private final val job: Job
   public final val scope: CoroutineScope
   private final var start: io.legado.app.help.coroutine.Coroutine.VoidCallback?
   private final var success: io.legado.app.help.coroutine.Coroutine.Callback<Any>?
   private final var timeMillis: Long?

   init {
      this.scope = scope;
      this.job = this.executeInternal(context, block);
   }

   public fun timeout(timeMillis: () -> Long): Coroutine<Any> {
      this.timeMillis = timeMillis.invoke() as java.lang.Long;
      return this;
   }

   public fun timeout(timeMillis: Long): Coroutine<Any> {
      this.timeMillis = timeMillis;
      return this;
   }

   public fun onErrorReturn(value: () -> Any?): Coroutine<Any> {
      this.errorReturn = new Coroutine.Result<>((T)value.invoke());
      return this;
   }

   public fun onErrorReturn(value: Any?): Coroutine<Any> {
      this.errorReturn = new Coroutine.Result<>((T)value);
      return this;
   }

   public fun onStart(context: CoroutineContext? = null, block: (CoroutineScope, Continuation<Unit>) -> Any?): Coroutine<Any> {
      this.start = new Coroutine.VoidCallback(this, context, block);
      return this;
   }

   public fun onSuccess(context: CoroutineContext? = null, block: (CoroutineScope, Any, Continuation<Unit>) -> Any?): Coroutine<Any> {
      this.success = new Coroutine.Callback<>(this, context, block);
      return this;
   }

   public fun onError(context: CoroutineContext? = null, block: (CoroutineScope, Throwable, Continuation<Unit>) -> Any?): Coroutine<Any> {
      this.error = new Coroutine.Callback<>(this, context, block);
      return this;
   }

   public fun onFinally(context: CoroutineContext? = null, block: (CoroutineScope, Continuation<Unit>) -> Any?): Coroutine<Any> {
      this.finally = new Coroutine.VoidCallback(this, context, block);
      return this;
   }

   public fun onCancel(context: CoroutineContext? = null, block: (CoroutineScope, Continuation<Unit>) -> Any?): Coroutine<Any> {
      this.cancel = new Coroutine.VoidCallback(this, context, block);
      return this;
   }

   public fun cancel(cause: CancellationException? = null) {
      this.job.cancel(cause);
      val var2: Coroutine.VoidCallback = this.cancel;
      if (this.cancel != null) {
         BuildersKt.launch$default(
            CoroutineScopeKt.MainScope(), null, null, (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(var2, this, null) {
               int label;

               {
                  super(2, `$completionx`);
                  this.$it = `$it`;
                  this.this$0 = `$receiver`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        if (this.$it.getContext() == null) {
                           val var3: Function2 = this.$it.getBlock();
                           val var4: CoroutineScope = this.this$0.getScope();
                           this.label = 1;
                           if (var3.invoke(var4, this) === var2) {
                              return var2;
                           }
                        } else {
                           val var10000: CoroutineContext = this.this$0.getScope().getCoroutineContext().plus(this.$it.getContext());
                           val var10001: Function2 = (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this.$it, null) {
                              int label;

                              {
                                 super(2, `$completionx`);
                                 this.$it = `$it`;
                              }

                              @Nullable
                              @Override
                              public final Object invokeSuspend(@NotNull Object $result) {
                                 val var3x: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                 switch (this.label) {
                                    case 0:
                                       ResultKt.throwOnFailure(`$result`);
                                       val `$this$withContext`: CoroutineScope = this.L$0 as CoroutineScope;
                                       val var10000: Function2 = this.$it.getBlock();
                                       this.label = 1;
                                       if (var10000.invoke(`$this$withContext`, this) === var3x) {
                                          return var3x;
                                       }
                                       break;
                                    case 1:
                                       ResultKt.throwOnFailure(`$result`);
                                       break;
                                    default:
                                       throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                 }

                                 return Unit.INSTANCE;
                              }

                              @NotNull
                              @Override
                              public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                                 val var3: Function2 = new <anonymous constructor>(this.$it, `$completion`);
                                 var3.L$0 = value;
                                 return var3 as Continuation<Unit>;
                              }

                              @Nullable
                              public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                                 return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                              }
                           }) as Function2;
                           val var10002: Continuation = this;
                           this.label = 2;
                           if (BuildersKt.withContext(var10000, var10001, var10002) === var2) {
                              return var2;
                           }
                        }
                        break;
                     case 1:
                        ResultKt.throwOnFailure(`$result`);
                        break;
                     case 2:
                        ResultKt.throwOnFailure(`$result`);
                        break;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  return Unit.INSTANCE;
               }

               @NotNull
               @Override
               public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                  return new <anonymous constructor>(this.$it, this.this$0, `$completion`);
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                  return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
               }
            }) as Function2, 3, null
         );
      }
   }

   public fun invokeOnCompletion(handler: (Throwable?) -> Unit): DisposableHandle {
      return this.job.invokeOnCompletion(handler);
   }

   private fun executeInternal(context: CoroutineContext, block: (CoroutineScope, Continuation<Any>) -> Any?): Job {
      return BuildersKt.launch$default(
         CoroutineScopeKt.plus(this.scope, Dispatchers.getIO()),
         null,
         null,
         (
            new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, context, block, null)// $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.modules.decompiler.stats.Statement.getVarDefinitions()" because "stat" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1468)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingExprent(VarDefinitionHelper.java:1679)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1496)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1545)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.remapClashingNames(VarDefinitionHelper.java:1458)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarProcessor.rerunClashing(VarProcessor.java:99)
      //   at org.jetbrains.java.decompiler.main.ClassWriter.invokeProcessors(ClassWriter.java:118)
      //   at org.jetbrains.java.decompiler.main.ClassWriter.writeClass(ClassWriter.java:352)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.NewExprent.toJava(NewExprent.java:407)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.wrapOperandString(FunctionExprent.java:761)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.wrapOperandString(FunctionExprent.java:727)
      
         ) as Function2,
         3,
         null
      );
   }

   private suspend inline fun dispatchVoidCallback(scope: CoroutineScope, callback: io.legado.app.help.coroutine.Coroutine.VoidCallback) {
      if (callback.getContext() == null) {
         val var5: Function2 = callback.getBlock();
         InlineMarker.mark(0);
         var5.invoke(scope, `$completion`);
         InlineMarker.mark(1);
         return Unit.INSTANCE;
      } else {
         val var10000: CoroutineContext = scope.getCoroutineContext().plus(callback.getContext());
         val var10001: Function2 = (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(callback, null) {
            int label;

            {
               super(2, `$completionx`);
               this.$callback = `$callback`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var3x: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     val `$this$withContext`: CoroutineScope = this.L$0 as CoroutineScope;
                     val var10000: Function2 = this.$callback.getBlock();
                     this.label = 1;
                     if (var10000.invoke(`$this$withContext`, this) === var3x) {
                        return var3x;
                     }
                     break;
                  case 1:
                     ResultKt.throwOnFailure(`$result`);
                     break;
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               return Unit.INSTANCE;
            }

            @NotNull
            @Override
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
               val var3: Function2 = new <anonymous constructor>(this.$callback, `$completion`);
               var3.L$0 = value;
               return var3 as Continuation<Unit>;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
               return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
            }
         }) as Function2;
         InlineMarker.mark(0);
         BuildersKt.withContext(var10000, var10001, `$completion`);
         InlineMarker.mark(1);
         return Unit.INSTANCE;
      }
   }

   private suspend inline fun <R> dispatchCallback(scope: CoroutineScope, value: R, callback: io.legado.app.help.coroutine.Coroutine.Callback<R>) {
      if (!CoroutineScopeKt.isActive(scope)) {
         return Unit.INSTANCE;
      } else if (callback.getContext() == null) {
         val var6: Function3 = callback.getBlock();
         InlineMarker.mark(0);
         var6.invoke(scope, value, `$completion`);
         InlineMarker.mark(1);
         return Unit.INSTANCE;
      } else {
         val var10000: CoroutineContext = scope.getCoroutineContext().plus(callback.getContext());
         val var10001: Function2 = (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(callback, value, null) {
            int label;

            {
               super(2, `$completionx`);
               this.$callback = `$callback`;
               this.$value = (R)`$value`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var3x: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     val `$this$withContext`: CoroutineScope = this.L$0 as CoroutineScope;
                     val var10000: Function3 = this.$callback.getBlock();
                     val var10002: Any = this.$value;
                     this.label = 1;
                     if (var10000.invoke(`$this$withContext`, var10002, this) === var3x) {
                        return var3x;
                     }
                     break;
                  case 1:
                     ResultKt.throwOnFailure(`$result`);
                     break;
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               return Unit.INSTANCE;
            }

            @NotNull
            @Override
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
               val var3: Function2 = new <anonymous constructor>(this.$callback, this.$value, `$completion`);
               var3.L$0 = value;
               return var3 as Continuation<Unit>;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
               return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
            }
         }) as Function2;
         InlineMarker.mark(0);
         BuildersKt.withContext(var10000, var10001, `$completion`);
         InlineMarker.mark(1);
         return Unit.INSTANCE;
      }
   }

   private suspend inline fun executeBlock(
      scope: CoroutineScope,
      context: CoroutineContext,
      timeMillis: Long,
      noinline block: (CoroutineScope, Continuation<Any>) -> Any?
   ): Any {
      var var10000: CoroutineContext = scope.getCoroutineContext().plus(context);
      val var10001: Function2 = (new Function2<CoroutineScope, Continuation<? super T>, Object>(timeMillis, block, null) {
         int label;

         {
            super(2, `$completionx`);
            this.$timeMillis = `$timeMillis`;
            this.$block = `$block`;
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            val var3x: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  val `$this$withContext`: CoroutineScope = this.L$0 as CoroutineScope;
                  if (this.$timeMillis > 0L) {
                     val var4: Long = this.$timeMillis;
                     val var10001: Function2 = (new Function2<CoroutineScope, Continuation<? super T>, Object>(this.$block, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.$block = `$block`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var3x: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           var var10000: Any;
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);
                                 val `$this$withTimeout`: CoroutineScope = this.L$0 as CoroutineScope;
                                 var10000 = this.$block;
                                 this.label = 1;
                                 var10000 = (Function2)var10000.invoke(`$this$withTimeout`, this);
                                 if (var10000 === var3x) {
                                    return var3x;
                                 }
                                 break;
                              case 1:
                                 ResultKt.throwOnFailure(`$result`);
                                 var10000 = (Function2)`$result`;
                                 break;
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           return var10000;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           val var3x: Function2 = new <anonymous constructor>(this.$block, `$completion`);
                           var3x.L$0 = value;
                           return var3x as Continuation<Unit>;
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super T> p2) {
                           return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2;
                     val var10002: Continuation = this;
                     this.label = 1;
                     var10000 = (Function2)TimeoutKt.withTimeout(var4, var10001, var10002);
                     if (var10000 === var3x) {
                        return var3x;
                     }
                  } else {
                     var10000 = this.$block;
                     this.label = 2;
                     var10000 = (Function2)var10000.invoke(`$this$withContext`, this);
                     if (var10000 === var3x) {
                        return var3x;
                     }
                  }
                  break;
               case 1:
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = (Function2)`$result`;
                  break;
               case 2:
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = (Function2)`$result`;
                  break;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            return var10000;
         }

         @NotNull
         @Override
         public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            val var3: Function2 = new <anonymous constructor>(this.$timeMillis, this.$block, `$completion`);
            var3.L$0 = value;
            return var3 as Continuation<Unit>;
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super T> p2) {
            return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
         }
      }) as Function2;
      InlineMarker.mark(0);
      var10000 = (CoroutineContext)BuildersKt.withContext(var10000, var10001, `$completion`);
      InlineMarker.mark(1);
      return var10000;
   }

   private inner class Callback<VALUE>(context: CoroutineContext?, block: (CoroutineScope, Any, Continuation<Unit>) -> Any?) {
      public final val block: (CoroutineScope, Any, Continuation<Unit>) -> Any?
      public final val context: CoroutineContext?

      init {
         this.this$0 = `this$0`;
         this.context = context;
         this.block = block;
      }
   }

   public companion object {
      private final val DEFAULT: CoroutineScope

      public fun <T> async(
         scope: CoroutineScope = Coroutine.access$getDEFAULT$cp(),
         context: CoroutineContext = Dispatchers.getIO() as CoroutineContext,
         block: (CoroutineScope, Continuation<T>) -> Any?
      ): Coroutine<T> {
         return new Coroutine<>(scope, context, block);
      }
   }

   private data class Result<T>(value: Any?) {
      public final val value: Any?

      init {
         this.value = (T)value;
      }

      public operator fun component1(): Any? {
         return this.value;
      }

      public fun copy(value: Any? = this.value): io.legado.app.help.coroutine.Coroutine.Result<Any> {
         return new Coroutine.Result<>((T)value);
      }

      public override fun toString(): String {
         return "Result(value=${this.value})";
      }

      public override fun hashCode(): Int {
         return if (this.value == null) 0 else this.value.hashCode();
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is Coroutine.Result) {
            return false;
         } else {
            return this.value == (other as Coroutine.Result).value;
         }
      }
   }

   private inner class VoidCallback(context: CoroutineContext?, block: (CoroutineScope, Continuation<Unit>) -> Any?) {
      public final val block: (CoroutineScope, Continuation<Unit>) -> Any?
      public final val context: CoroutineContext?

      init {
         this.this$0 = `this$0`;
         this.context = context;
         this.block = block;
      }
   }
}
