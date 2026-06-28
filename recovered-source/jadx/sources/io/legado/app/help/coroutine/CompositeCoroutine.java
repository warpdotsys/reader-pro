package io.legado.app.help.coroutine;

import java.util.Arrays;
import java.util.HashSet;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: CompositeCoroutine.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/CompositeCoroutine.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B#\b\u0016\u0012\u001a\u0010\u0003\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00050\u0004\"\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006B\u0019\b\u0016\u0012\u0010\u0010\u0003\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00050\u0007¢\u0006\u0002\u0010\bJ\u0014\u0010\u0013\u001a\u00020\n2\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J)\u0010\u0015\u001a\u00020\n2\u001a\u0010\u0003\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00050\u0004\"\u0006\u0012\u0002\b\u00030\u0005H\u0016¢\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0014\u0010\u0019\u001a\u00020\n2\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\u001a\u001a\u00020\n2\n\u0010\u0014\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016R\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR*\u0010\f\u001a\u001e\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0018\u00010\rj\u000e\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0005\u0018\u0001`\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u001b"}, d2 = {"Lio/legado/app/help/coroutine/CompositeCoroutine;", "Lio/legado/app/help/coroutine/CoroutineContainer;", "()V", "coroutines", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/help/coroutine/Coroutine;", "([Lio/legado/app/help/coroutine/Coroutine;)V", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/Iterable;)V", "isEmpty", PackageDocumentBase.PREFIX_OPF, "()Z", "resources", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "size", PackageDocumentBase.PREFIX_OPF, "getSize", "()I", "add", "coroutine", "addAll", "([Lio/legado/app/help/coroutine/Coroutine;)Z", "clear", PackageDocumentBase.PREFIX_OPF, "delete", "remove", "reader-pro"})
public final class CompositeCoroutine implements CoroutineContainer {

    @Nullable
    private HashSet<Coroutine<?>> resources;

    public final int getSize() {
        HashSet<Coroutine<?>> hashSet = this.resources;
        if (hashSet == null) {
            return 0;
        }
        return hashSet.size();
    }

    public final boolean isEmpty() {
        return getSize() == 0;
    }

    public CompositeCoroutine() {
    }

    public CompositeCoroutine(@NotNull Coroutine<?>... coroutines) {
        Intrinsics.checkNotNullParameter(coroutines, "coroutines");
        this.resources = SetsKt.hashSetOf(Arrays.copyOf(coroutines, coroutines.length));
    }

    public CompositeCoroutine(@NotNull Iterable<? extends Coroutine<?>> coroutines) {
        Intrinsics.checkNotNullParameter(coroutines, "coroutines");
        this.resources = new HashSet<>();
        for (Coroutine<?> coroutine : coroutines) {
            HashSet<Coroutine<?>> hashSet = this.resources;
            if (hashSet != null) {
                hashSet.add(coroutine);
            }
        }
    }

    @Override // io.legado.app.help.coroutine.CoroutineContainer
    public boolean add(@NotNull Coroutine<?> coroutine) {
        boolean zAdd;
        Intrinsics.checkNotNullParameter(coroutine, "coroutine");
        synchronized (this) {
            HashSet<Coroutine<?>> hashSet = this.resources;
            if (this.resources == null) {
                hashSet = new HashSet<>();
                this.resources = hashSet;
            }
            HashSet<Coroutine<?>> hashSet2 = hashSet;
            Intrinsics.checkNotNull(hashSet2);
            zAdd = hashSet2.add(coroutine);
        }
        return zAdd;
    }

    @Override // io.legado.app.help.coroutine.CoroutineContainer
    public boolean addAll(@NotNull Coroutine<?>... coroutines) {
        Intrinsics.checkNotNullParameter(coroutines, "coroutines");
        synchronized (this) {
            HashSet<Coroutine<?>> hashSet = this.resources;
            if (this.resources == null) {
                hashSet = new HashSet<>();
                this.resources = hashSet;
            }
            int i = 0;
            int length = coroutines.length;
            while (i < length) {
                Coroutine<?> coroutine = coroutines[i];
                i++;
                HashSet<Coroutine<?>> hashSet2 = hashSet;
                Intrinsics.checkNotNull(hashSet2);
                boolean add = hashSet2.add(coroutine);
                if (!add) {
                    return false;
                }
            }
            Unit unit = Unit.INSTANCE;
            return true;
        }
    }

    @Override // io.legado.app.help.coroutine.CoroutineContainer
    public boolean remove(@NotNull Coroutine<?> coroutine) {
        Intrinsics.checkNotNullParameter(coroutine, "coroutine");
        if (delete(coroutine)) {
            Coroutine.cancel$default(coroutine, null, 1, null);
            return true;
        }
        return false;
    }

    @Override // io.legado.app.help.coroutine.CoroutineContainer
    public boolean delete(@NotNull Coroutine<?> coroutine) {
        Intrinsics.checkNotNullParameter(coroutine, "coroutine");
        synchronized (this) {
            HashSet<Coroutine<?>> hashSet = this.resources;
            if (hashSet == null || !hashSet.remove(coroutine)) {
                return false;
            }
            Unit unit = Unit.INSTANCE;
            return true;
        }
    }

    @Override // io.legado.app.help.coroutine.CoroutineContainer
    public void clear() {
        Iterable set;
        synchronized (this) {
            set = this.resources;
            this.resources = null;
            Unit unit = Unit.INSTANCE;
        }
        if (set != null) {
            Iterable $this$forEachIndexed$iv = set;
            int index$iv = 0;
            for (Object item$iv : $this$forEachIndexed$iv) {
                int i = index$iv;
                index$iv++;
                if (i < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                Coroutine coroutine = (Coroutine) item$iv;
                Coroutine.cancel$default(coroutine, null, 1, null);
            }
        }
    }
}
