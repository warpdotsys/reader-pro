// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.coroutine;

import java.util.concurrent.CancellationException;
import kotlin.Unit;
import java.util.Iterator;
import kotlin.collections.SetsKt;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0016?\u0006\u0002\u0010\u0002B#\b\u0016\u0012\u001a\u0010\u0003\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00050\u0004\"\u0006\u0012\u0002\b\u00030\u0005?\u0006\u0002\u0010\u0006B\u0019\b\u0016\u0012\u0010\u0010\u0003\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00050\u0007?\u0006\u0002\u0010\bJ\u0014\u0010\u0013\u001a\u00020\n2\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J)\u0010\u0015\u001a\u00020\n2\u001a\u0010\u0003\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00050\u0004\"\u0006\u0012\u0002\b\u00030\u0005H\u0016?\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0014\u0010\u0019\u001a\u00020\n2\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\u001a\u001a\u00020\n2\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016R\u0011\u0010\t\u001a\u00020\n8F?\u0006\u0006\u001a\u0004\b\t\u0010\u000bR*\u0010\f\u001a\u001e\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0018\u00010\rj\u000e\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0018\u0001`\u000eX\u0082\u000e?\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00108F?\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¡§\u0006\u001b" }, d2 = { "Lio/legado/app/help/coroutine/CompositeCoroutine;", "Lio/legado/app/help/coroutine/CoroutineContainer;", "()V", "coroutines", "", "Lio/legado/app/help/coroutine/Coroutine;", "([Lio/legado/app/help/coroutine/Coroutine;)V", "", "(Ljava/lang/Iterable;)V", "isEmpty", "", "()Z", "resources", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "size", "", "getSize", "()I", "add", "coroutine", "addAll", "([Lio/legado/app/help/coroutine/Coroutine;)Z", "clear", "", "delete", "remove", "reader-pro" })
public final class CompositeCoroutine implements CoroutineContainer
{
    @Nullable
    private HashSet<Coroutine<?>> resources;
    
    public final int getSize() {
        final HashSet<Coroutine<?>> resources = this.resources;
        return (resources == null) ? 0 : resources.size();
    }
    
    public final boolean isEmpty() {
        return this.getSize() == 0;
    }
    
    public CompositeCoroutine() {
    }
    
    public CompositeCoroutine(@NotNull final Coroutine<?>... coroutines) {
        Intrinsics.checkNotNullParameter((Object)coroutines, "coroutines");
        this.resources = SetsKt.hashSetOf((Object[])Arrays.copyOf(coroutines, coroutines.length));
    }
    
    public CompositeCoroutine(@NotNull final Iterable<? extends Coroutine<?>> coroutines) {
        Intrinsics.checkNotNullParameter((Object)coroutines, "coroutines");
        this.resources = new HashSet<Coroutine<?>>();
        for (final Coroutine d : coroutines) {
            final HashSet<Coroutine<?>> resources = this.resources;
            if (resources == null) {
                continue;
            }
            resources.add(d);
        }
    }
    
    @Override
    public boolean add(@NotNull final Coroutine<?> coroutine) {
        Intrinsics.checkNotNullParameter((Object)coroutine, "coroutine");
        synchronized (this) {
            final int n = 0;
            HashSet set = this.resources;
            if (this.resources == null) {
                set = new HashSet();
                this.resources = set;
            }
            final HashSet set2 = set;
            Intrinsics.checkNotNull((Object)set2);
            return set2.add(coroutine);
        }
    }
    
    @Override
    public boolean addAll(@NotNull final Coroutine<?>... coroutines) {
        Intrinsics.checkNotNullParameter((Object)coroutines, "coroutines");
        synchronized (this) {
            final int n = 0;
            HashSet set = this.resources;
            if (this.resources == null) {
                set = new HashSet();
                this.resources = set;
            }
            int i = 0;
            while (i < coroutines.length) {
                final Coroutine coroutine = coroutines[i];
                ++i;
                final HashSet set2 = set;
                Intrinsics.checkNotNull((Object)set2);
                final boolean add = set2.add(coroutine);
                if (!add) {
                    return false;
                }
            }
            final Unit instance = Unit.INSTANCE;
        }
        return true;
    }
    
    @Override
    public boolean remove(@NotNull final Coroutine<?> coroutine) {
        Intrinsics.checkNotNullParameter((Object)coroutine, "coroutine");
        if (this.delete(coroutine)) {
            Coroutine.cancel$default((Coroutine<Object>)coroutine, null, 1, null);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean delete(@NotNull final Coroutine<?> coroutine) {
        Intrinsics.checkNotNullParameter((Object)coroutine, "coroutine");
        synchronized (this) {
            final int n = 0;
            final HashSet set = this.resources;
            if (set == null || !set.remove(coroutine)) {
                return false;
            }
            final Unit instance = Unit.INSTANCE;
        }
        return true;
    }
    
    @Override
    public void clear() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1        /* set */
        //     2: iconst_0       
        //     3: istore_2       
        //     4: iconst_0       
        //     5: istore_3       
        //     6: aload_0         /* this */
        //     7: monitorenter   
        //     8: nop            
        //     9: iconst_0       
        //    10: istore          $i$a$-synchronized-CompositeCoroutine$clear$1
        //    12: aload_0         /* this */
        //    13: getfield        io/legado/app/help/coroutine/CompositeCoroutine.resources:Ljava/util/HashSet;
        //    16: astore_1        /* set */
        //    17: aload_0         /* this */
        //    18: aconst_null    
        //    19: putfield        io/legado/app/help/coroutine/CompositeCoroutine.resources:Ljava/util/HashSet;
        //    22: nop            
        //    23: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //    26: astore_3       
        //    27: aload_0         /* this */
        //    28: monitorexit    
        //    29: goto            37
        //    32: astore_3       
        //    33: aload_0         /* this */
        //    34: monitorexit    
        //    35: aload_3        
        //    36: athrow         
        //    37: aload_1         /* set */
        //    38: astore_2       
        //    39: aload_2        
        //    40: ifnonnull       46
        //    43: goto            129
        //    46: aload_2        
        //    47: checkcast       Ljava/lang/Iterable;
        //    50: astore_3        /* $this$forEachIndexed$iv */
        //    51: iconst_0       
        //    52: istore          $i$f$forEachIndexed
        //    54: iconst_0       
        //    55: istore          index$iv
        //    57: aload_3         /* $this$forEachIndexed$iv */
        //    58: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    63: astore          6
        //    65: aload           6
        //    67: invokeinterface java/util/Iterator.hasNext:()Z
        //    72: ifeq            128
        //    75: aload           6
        //    77: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    82: astore          item$iv
        //    84: iload           index$iv
        //    86: iinc            index$iv, 1
        //    89: istore          8
        //    91: iconst_0       
        //    92: istore          9
        //    94: iload           8
        //    96: ifge            102
        //    99: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   102: iload           8
        //   104: aload           item$iv
        //   106: checkcast       Lio/legado/app/help/coroutine/Coroutine;
        //   109: astore          10
        //   111: istore          $noName_0
        //   113: iconst_0       
        //   114: istore          $i$a$-forEachIndexed-CompositeCoroutine$clear$2
        //   116: aload           coroutine
        //   118: aconst_null    
        //   119: iconst_1       
        //   120: aconst_null    
        //   121: invokestatic    io/legado/app/help/coroutine/Coroutine.cancel$default:(Lio/legado/app/help/coroutine/Coroutine;Ljava/util/concurrent/CancellationException;ILjava/lang/Object;)V
        //   124: nop            
        //   125: goto            65
        //   128: nop            
        //   129: return         
        //    StackMapTable: 00 07 FF 00 20 00 03 07 00 02 07 00 0E 01 00 01 07 00 5B FD 00 04 07 00 60 01 FF 00 08 00 05 07 00 02 07 00 0E 07 00 0E 07 00 60 01 00 00 FF 00 12 00 07 07 00 02 07 00 0E 07 00 0E 07 00 38 01 01 07 00 3E 00 00 FE 00 24 07 00 04 01 01 F8 00 19 FF 00 00 00 05 07 00 02 07 00 0E 07 00 0E 07 00 04 01 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  8      27     32     37     Any
        //  32     33     32     37     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
