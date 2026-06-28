package com.htmake.reader.utils;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: UserMutex.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/utils/UserMutex.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0019\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0005H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000eR\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, d2 = {"Lcom/htmake/reader/utils/UserMutex;", PackageDocumentBase.PREFIX_OPF, "()V", "lockerMap", "Lcom/htmake/reader/utils/LRUCache;", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/sync/Mutex;", "getLockerMap", "()Lcom/htmake/reader/utils/LRUCache;", "mutex", "getMutex", "()Lkotlinx/coroutines/sync/Mutex;", "getLocker", "lockKey", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class UserMutex {

    @NotNull
    public static final UserMutex INSTANCE = new UserMutex();

    @NotNull
    private static final Mutex mutex = MutexKt.Mutex$default(false, 1, (Object) null);

    @NotNull
    private static final LRUCache<String, Mutex> lockerMap = new LRUCache<>(10);

    /* JADX INFO: renamed from: com.htmake.reader.utils.UserMutex$getLocker$1, reason: invalid class name */
    /* JADX INFO: compiled from: UserMutex.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/utils/UserMutex$getLocker$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "UserMutex.kt", l = {24}, i = {0, 0}, s = {"L$0", "L$1"}, n = {"lockKey", "$this$withLock_u24default$iv"}, m = "getLocker", c = "com.htmake.reader.utils.UserMutex")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return UserMutex.this.getLocker(null, (Continuation) this);
        }
    }

    private UserMutex() {
    }

    @NotNull
    public final Mutex getMutex() {
        return mutex;
    }

    @NotNull
    public final LRUCache<String, Mutex> getLockerMap() {
        return lockerMap;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getLocker(@NotNull String lockKey, @NotNull Continuation<? super Mutex> $completion) {
        AnonymousClass1 anonymousClass1;
        Object owner$iv;
        Mutex $this$withLock_u24default$iv;
        if ($completion instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) $completion;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1($completion);
            }
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$withLock_u24default$iv = getMutex();
                owner$iv = null;
                anonymousClass1.L$0 = lockKey;
                anonymousClass1.L$1 = $this$withLock_u24default$iv;
                anonymousClass1.label = 1;
                if ($this$withLock_u24default$iv.lock((Object) null, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                owner$iv = null;
                $this$withLock_u24default$iv = (Mutex) anonymousClass1.L$1;
                lockKey = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        try {
            if (INSTANCE.getLockerMap().get(lockKey) == null) {
                INSTANCE.getLockerMap().put(lockKey, MutexKt.Mutex$default(false, 1, (Object) null));
            }
            Mutex mutex2 = INSTANCE.getLockerMap().get(lockKey);
            Intrinsics.checkNotNull(mutex2);
            Mutex mutex3 = mutex2;
            $this$withLock_u24default$iv.unlock(owner$iv);
            return mutex3;
        } catch (Throwable th) {
            $this$withLock_u24default$iv.unlock(owner$iv);
            throw th;
        }
    }
}
