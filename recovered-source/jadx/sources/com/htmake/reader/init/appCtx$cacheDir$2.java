package com.htmake.reader.init;

import com.htmake.reader.utils.ExtKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: appCtx.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/init/appCtx$cacheDir$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF})
final class appCtx$cacheDir$2 extends Lambda implements Function0<String> {
    public static final appCtx$cacheDir$2 INSTANCE = new appCtx$cacheDir$2();

    appCtx$cacheDir$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final String m82invoke() {
        return ExtKt.getWorkDir("storage", "cache");
    }
}
