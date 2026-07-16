// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.adapters;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¡§\u0006\r" }, d2 = { "Lio/legado/app/adapters/ReaderAdapterHelper;", "", "()V", "readerAdapter", "Lio/legado/app/adapters/ReaderAdapterInterface;", "getReaderAdapter", "()Lio/legado/app/adapters/ReaderAdapterInterface;", "setReaderAdapter", "(Lio/legado/app/adapters/ReaderAdapterInterface;)V", "getAdapter", "setAdapter", "", "adapter", "reader-pro" })
public final class ReaderAdapterHelper
{
    @NotNull
    public static final ReaderAdapterHelper INSTANCE;
    @NotNull
    private static ReaderAdapterInterface readerAdapter;
    
    private ReaderAdapterHelper() {
    }
    
    @NotNull
    public final ReaderAdapterInterface getReaderAdapter() {
        return ReaderAdapterHelper.readerAdapter;
    }
    
    public final void setReaderAdapter(@NotNull final ReaderAdapterInterface <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        ReaderAdapterHelper.readerAdapter = <set-?>;
    }
    
    public final void setAdapter(@NotNull final ReaderAdapterInterface adapter) {
        Intrinsics.checkNotNullParameter((Object)adapter, "adapter");
        ReaderAdapterHelper.readerAdapter = adapter;
    }
    
    @NotNull
    public final ReaderAdapterInterface getAdapter() {
        return ReaderAdapterHelper.readerAdapter;
    }
    
    static {
        INSTANCE = new ReaderAdapterHelper();
        ReaderAdapterHelper.readerAdapter = new DefaultAdpater();
    }
}
