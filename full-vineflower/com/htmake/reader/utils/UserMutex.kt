package com.htmake.reader.utils

import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.MutexKt
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object UserMutex {
   public final val lockerMap: LRUCache<String, Mutex> = new LRUCache(10)
   public final val mutex: Mutex = MutexKt.Mutex$default(false, 1, null)

   public suspend fun getLocker(lockKey: String): Mutex {
      label38: {
         var `$continuation`: Continuation;
         label36: {
            if (`$completion` is <unrepresentable>) {
               `$continuation` = `$completion` as <unrepresentable>;
               if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
                  `$continuation`.label -= Integer.MIN_VALUE;
                  break label36;
               }
            }

            `$continuation` = new ContinuationImpl(this, `$completion`) {
               Object L$0;
               Object L$1;
               int label;

               {
                  super(`$completion`);
                  this.this$0 = `this$0`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  this.result = `$result`;
                  this.label |= Integer.MIN_VALUE;
                  return this.this$0.getLocker(null, this);
               }
            };
         }

         val `$result`: Any = `$continuation`.result;
         val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var `$this$withLock_u24default$iv`: Mutex;
         var `owner$iv`: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               `$this$withLock_u24default$iv` = this.getMutex();
               `owner$iv` = null;
               `$continuation`.L$0 = lockKey;
               `$continuation`.L$1 = `$this$withLock_u24default$iv`;
               `$continuation`.label = 1;
               if (`$this$withLock_u24default$iv`.lock(null, `$continuation`) === var10) {
                  return var10;
               }
               break;
            case 1:
               `owner$iv` = null;
               `$this$withLock_u24default$iv` = `$continuation`.L$1 as Mutex;
               lockKey = `$continuation`.L$0 as java.lang.String;
               ResultKt.throwOnFailure(`$result`);
               break;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         try {
            if (INSTANCE.getLockerMap().get(lockKey) == null) {
               INSTANCE.getLockerMap().put(lockKey, MutexKt.Mutex$default(false, 1, null));
            }

            val var10001: Any = INSTANCE.getLockerMap().get(lockKey);
            val var14: Mutex = var10001 as Mutex;
         } catch (var11: java.lang.Throwable) {
            `$this$withLock_u24default$iv`.unlock(`owner$iv`);
         }

         `$this$withLock_u24default$iv`.unlock(`owner$iv`);
      }
   }
}
