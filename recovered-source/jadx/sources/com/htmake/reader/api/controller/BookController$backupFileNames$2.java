package com.htmake.reader.api.controller;

import io.legado.app.help.DefaultData;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$backupFileNames$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF})
final class BookController$backupFileNames$2 extends Lambda implements Function0<String[]> {
    public static final BookController$backupFileNames$2 INSTANCE = new BookController$backupFileNames$2();

    BookController$backupFileNames$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final String[] m27invoke() {
        return new String[]{"bookSource.json", "bookshelf.json", "bookGroup.json", "rssSources.json", "replaceRule.json", "bookmark.json", "userConfig.json", "httpTTS.json", "remoteBookSourceSub.json", DefaultData.txtTocRuleFileName};
    }
}
