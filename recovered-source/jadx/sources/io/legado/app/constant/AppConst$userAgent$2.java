package io.legado.app.constant;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: AppConst.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/constant/AppConst$userAgent$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF})
final class AppConst$userAgent$2 extends Lambda implements Function0<String> {
    public static final AppConst$userAgent$2 INSTANCE = new AppConst$userAgent$2();

    AppConst$userAgent$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final String m131invoke() {
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";
    }
}
