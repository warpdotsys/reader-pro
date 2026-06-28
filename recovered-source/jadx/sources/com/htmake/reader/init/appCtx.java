package com.htmake.reader.init;

import com.htmake.reader.utils.ExtKt;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: appCtx.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/init/appCtx.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006\t"}, d2 = {"Lcom/htmake/reader/init/appCtx;", PackageDocumentBase.PREFIX_OPF, "()V", "cacheDir", PackageDocumentBase.PREFIX_OPF, "getCacheDir", "()Ljava/lang/String;", "cacheDir$delegate", "Lkotlin/Lazy;", "reader-pro"})
public final class appCtx {

    @NotNull
    public static final appCtx INSTANCE = new appCtx();

    /* JADX INFO: renamed from: cacheDir$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy cacheDir = LazyKt.lazy(new Function0<String>() { // from class: com.htmake.reader.init.appCtx$cacheDir$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final String m82invoke() {
            return ExtKt.getWorkDir("storage", "cache");
        }
    });

    private appCtx() {
    }

    @NotNull
    public final String getCacheDir() {
        return (String) cacheDir.getValue();
    }
}
