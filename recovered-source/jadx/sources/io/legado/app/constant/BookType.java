package io.legado.app.constant;

import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookType.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/constant/BookType.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lio/legado/app/constant/BookType;", PackageDocumentBase.PREFIX_OPF, "()V", "audio", PackageDocumentBase.PREFIX_OPF, "default", "file", "image", "local", PackageDocumentBase.PREFIX_OPF, "video", "reader-pro"})
public final class BookType {

    @NotNull
    public static final BookType INSTANCE = new BookType();
    public static final int default = 0;
    public static final int audio = 1;
    public static final int image = 2;
    public static final int file = 3;
    public static final int video = 4;

    @NotNull
    public static final String local = "loc_book";

    private BookType() {
    }
}
