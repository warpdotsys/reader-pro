// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import kotlinx.coroutines.sync.MutexKt;
import org.jetbrains.annotations.Nullable;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.sync.Mutex;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0019\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0005H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u000eR\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004?\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u000f" }, d2 = { "Lcom/htmake/reader/utils/UserMutex;", "", "()V", "lockerMap", "Lcom/htmake/reader/utils/LRUCache;", "", "Lkotlinx/coroutines/sync/Mutex;", "getLockerMap", "()Lcom/htmake/reader/utils/LRUCache;", "mutex", "getMutex", "()Lkotlinx/coroutines/sync/Mutex;", "getLocker", "lockKey", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class UserMutex
{
    @NotNull
    public static final UserMutex INSTANCE;
    @NotNull
    private static final Mutex mutex;
    @NotNull
    private static final LRUCache<String, Mutex> lockerMap;
    
    private UserMutex() {
    }
    
    @NotNull
    public final Mutex getMutex() {
        return UserMutex.mutex;
    }
    
    @NotNull
    public final LRUCache<String, Mutex> getLockerMap() {
        return UserMutex.lockerMap;
    }
    
    @Nullable
    public final Object getLocker(@NotNull final String lockKey, @NotNull final Continuation<? super Mutex> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/utils/UserMutex$getLocker$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/utils/UserMutex$getLocker$1;
        //    11: astore          9
        //    13: aload           9
        //    15: getfield        com/htmake/reader/utils/UserMutex$getLocker$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           9
        //    26: dup            
        //    27: getfield        com/htmake/reader/utils/UserMutex$getLocker$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/utils/UserMutex$getLocker$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/utils/UserMutex$getLocker$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/utils/UserMutex$getLocker$1.<init>:(Lcom/htmake/reader/utils/UserMutex;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/utils/UserMutex$getLocker$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          10
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/utils/UserMutex$getLocker$1.label:I
        //    67: tableswitch {
        //                0: 88
        //                1: 144
        //          default: 255
        //        }
        //    88: aload           $result
        //    90: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    93: aload_0         /* this */
        //    94: invokevirtual   com/htmake/reader/utils/UserMutex.getMutex:()Lkotlinx/coroutines/sync/Mutex;
        //    97: astore_3        /* $this$withLock_u24default$iv */
        //    98: aconst_null    
        //    99: astore          owner$iv
        //   101: iconst_0       
        //   102: istore          $i$f$withLock
        //   104: iconst_0       
        //   105: istore          6
        //   107: aload_3         /* $this$withLock_u24default$iv */
        //   108: aload           owner$iv
        //   110: aload           $continuation
        //   112: aload           $continuation
        //   114: aload_1         /* lockKey */
        //   115: putfield        com/htmake/reader/utils/UserMutex$getLocker$1.L$0:Ljava/lang/Object;
        //   118: aload           $continuation
        //   120: aload_3         /* $this$withLock_u24default$iv */
        //   121: putfield        com/htmake/reader/utils/UserMutex$getLocker$1.L$1:Ljava/lang/Object;
        //   124: aload           $continuation
        //   126: iconst_1       
        //   127: putfield        com/htmake/reader/utils/UserMutex$getLocker$1.label:I
        //   130: invokeinterface kotlinx/coroutines/sync/Mutex.lock:(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   135: dup            
        //   136: aload           10
        //   138: if_acmpne       175
        //   141: aload           10
        //   143: areturn        
        //   144: iconst_0       
        //   145: istore          $i$f$withLock
        //   147: aconst_null    
        //   148: astore          owner$iv
        //   150: aload           $continuation
        //   152: getfield        com/htmake/reader/utils/UserMutex$getLocker$1.L$1:Ljava/lang/Object;
        //   155: checkcast       Lkotlinx/coroutines/sync/Mutex;
        //   158: astore_3        /* $this$withLock_u24default$iv */
        //   159: aload           $continuation
        //   161: getfield        com/htmake/reader/utils/UserMutex$getLocker$1.L$0:Ljava/lang/Object;
        //   164: checkcast       Ljava/lang/String;
        //   167: astore_1        /* lockKey */
        //   168: aload           $result
        //   170: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   173: aload           $result
        //   175: pop            
        //   176: nop            
        //   177: iconst_0       
        //   178: istore          $i$a$-withLock$default-UserMutex$getLocker$2
        //   180: getstatic       com/htmake/reader/utils/UserMutex.INSTANCE:Lcom/htmake/reader/utils/UserMutex;
        //   183: invokevirtual   com/htmake/reader/utils/UserMutex.getLockerMap:()Lcom/htmake/reader/utils/LRUCache;
        //   186: aload_1         /* lockKey */
        //   187: invokevirtual   com/htmake/reader/utils/LRUCache.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   190: ifnonnull       209
        //   193: getstatic       com/htmake/reader/utils/UserMutex.INSTANCE:Lcom/htmake/reader/utils/UserMutex;
        //   196: invokevirtual   com/htmake/reader/utils/UserMutex.getLockerMap:()Lcom/htmake/reader/utils/LRUCache;
        //   199: aload_1         /* lockKey */
        //   200: iconst_0       
        //   201: iconst_1       
        //   202: aconst_null    
        //   203: invokestatic    kotlinx/coroutines/sync/MutexKt.Mutex$default:(ZILjava/lang/Object;)Lkotlinx/coroutines/sync/Mutex;
        //   206: invokevirtual   com/htmake/reader/utils/LRUCache.put:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   209: getstatic       com/htmake/reader/utils/UserMutex.INSTANCE:Lcom/htmake/reader/utils/UserMutex;
        //   212: invokevirtual   com/htmake/reader/utils/UserMutex.getLockerMap:()Lcom/htmake/reader/utils/LRUCache;
        //   215: aload_1         /* lockKey */
        //   216: invokevirtual   com/htmake/reader/utils/LRUCache.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   219: dup            
        //   220: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNull:(Ljava/lang/Object;)V
        //   223: checkcast       Lkotlinx/coroutines/sync/Mutex;
        //   226: astore          6
        //   228: aload_3         /* $this$withLock_u24default$iv */
        //   229: aload           owner$iv
        //   231: invokeinterface kotlinx/coroutines/sync/Mutex.unlock:(Ljava/lang/Object;)V
        //   236: aload           6
        //   238: goto            254
        //   241: astore          6
        //   243: aload_3         /* $this$withLock_u24default$iv */
        //   244: aload           owner$iv
        //   246: invokeinterface kotlinx/coroutines/sync/Mutex.unlock:(Ljava/lang/Object;)V
        //   251: aload           6
        //   253: athrow         
        //   254: areturn        
        //   255: new             Ljava/lang/IllegalStateException;
        //   258: dup            
        //   259: ldc             "call to 'resume' before 'invoke' with coroutine"
        //   261: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   264: athrow         
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/coroutines/Continuation<-Lkotlinx/coroutines/sync/Mutex;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  lockKey      
        //  $completion  
        //    StackMapTable: 00 09 27 FF 00 0A 00 0A 07 00 02 07 00 48 07 00 76 00 00 00 00 00 00 07 00 20 00 00 FF 00 25 00 0B 07 00 02 07 00 48 07 00 76 00 00 00 00 00 07 00 04 07 00 20 07 00 04 00 00 37 FF 00 1E 00 0B 07 00 02 07 00 48 07 00 76 07 00 42 05 01 00 00 07 00 04 07 00 20 07 00 04 00 01 07 00 04 FF 00 21 00 0B 07 00 02 07 00 48 07 00 76 07 00 42 05 01 00 01 07 00 04 07 00 20 07 00 04 00 00 FF 00 1F 00 0B 07 00 02 07 00 48 07 00 76 07 00 42 05 01 00 00 07 00 04 07 00 20 07 00 04 00 01 07 00 78 FF 00 0C 00 0B 07 00 02 07 00 48 07 00 76 07 00 42 05 01 07 00 42 01 07 00 04 07 00 20 07 00 04 00 01 07 00 42 FF 00 00 00 0B 07 00 02 07 00 48 07 00 76 00 00 00 00 00 07 00 04 07 00 20 07 00 04 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  176    228    241    254    Any
        //  241    243    241    254    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:361)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:504)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:599)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
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
    
    static {
        INSTANCE = new UserMutex();
        mutex = MutexKt.Mutex$default(false, 1, (Object)null);
        lockerMap = new LRUCache<String, Mutex>(10);
    }
}
