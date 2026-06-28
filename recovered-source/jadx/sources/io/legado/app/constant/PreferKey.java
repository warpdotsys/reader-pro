package io.legado.app.constant;

import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: PreferKey.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/constant/PreferKey.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lio/legado/app/constant/PreferKey;", PackageDocumentBase.PREFIX_OPF, "()V", PreferKey.downloadPath, PackageDocumentBase.PREFIX_OPF, PreferKey.hideNavigationBar, PreferKey.hideStatusBar, "nextKey", PreferKey.precisionSearch, "prevKey", "reader-pro"})
public final class PreferKey {

    @NotNull
    public static final PreferKey INSTANCE = new PreferKey();

    @NotNull
    public static final String downloadPath = "downloadPath";

    @NotNull
    public static final String hideStatusBar = "hideStatusBar";

    @NotNull
    public static final String hideNavigationBar = "hideNavigationBar";

    @NotNull
    public static final String precisionSearch = "precisionSearch";

    @NotNull
    public static final String prevKey = "prevKeyCode";

    @NotNull
    public static final String nextKey = "nextKeyCode";

    private PreferKey() {
    }
}
