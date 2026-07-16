package io.legado.app.help.coroutine

import java.util.Arrays
import java.util.HashSet

public class CompositeCoroutine : CoroutineContainer {
   public final val isEmpty: Boolean
      public final get() {
         return this.getSize() == 0;
      }


   private final var resources: HashSet<Coroutine<*>>?

   public final val size: Int
      public final get() {
         return if (this.resources == null) 0 else this.resources.size();
      }



   public constructor(vararg coroutines: Coroutine<*>)  {
      this.resources = SetsKt.hashSetOf(Arrays.copyOf(coroutines, coroutines.length));
   }

   public constructor(coroutines: Iterable<Coroutine<*>>)  {
      this.resources = new HashSet<>();

      for (Coroutine d : coroutines) {
         if (this.resources != null) {
            this.resources.add(d);
         }
      }
   }

   public override fun add(coroutine: Coroutine<*>): Boolean {
      synchronized (this) {
         var set: HashSet = this.resources;
         if (this.resources == null) {
            set = new HashSet();
            this.resources = set;
         }

         return set.add(coroutine);
      }
   }

   public override fun addAll(vararg coroutines: Coroutine<*>): Boolean {
      label35: {
         synchronized (this){} // $VF: monitorenter 

         label32: {
            try {
               var set: HashSet = this.resources;
               if (this.resources == null) {
                  set = new HashSet();
                  this.resources = set;
               }

               val var15: Array<Coroutine> = coroutines;
               var var7: Int = 0;
               val var8: Int = coroutines.length;

               while (var7 < var8) {
                  val coroutine: Coroutine = var15[var7];
                  var7++;
                  if (!set.add(coroutine)) {
                     break label32;
                  }
               }
            } catch (var12: java.lang.Throwable) {
               // $VF: monitorexit
            }

            // $VF: monitorexit
         }

         // $VF: monitorexit
      }
   }

   public override fun remove(coroutine: Coroutine<*>): Boolean {
      if (this.delete(coroutine)) {
         Coroutine.cancel$default(coroutine, null, 1, null);
         return true;
      } else {
         return false;
      }
   }

   public override fun delete(coroutine: Coroutine<*>): Boolean {
      label28: {
         synchronized (this){} // $VF: monitorenter 

         label25: {
            try {
               if (this.resources == null || !this.resources.remove(coroutine)) {
                  break label25;
               }
            } catch (var7: java.lang.Throwable) {
               // $VF: monitorexit
            }

            // $VF: monitorexit
         }

         // $VF: monitorexit
      }
   }

   public override fun clear() {
      val var14: HashSet;
      synchronized (this) {
         var14 = this.resources;
         this.resources = null;
      }

      if (var14 != null) {
         val var16: java.lang.Iterable = var14;
         val `index$iv`: Int = 0;

         for (Object item$iv : $this$forEachIndexed$iv) {
            if (`index$iv`++ < 0) {
               CollectionsKt.throwIndexOverflow();
            }

            Coroutine.cancel$default(`item$iv` as Coroutine, null, 1, null);
         }
      }
   }
}
