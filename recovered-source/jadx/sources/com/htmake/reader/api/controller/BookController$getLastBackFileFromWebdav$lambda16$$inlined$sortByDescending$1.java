package com.htmake.reader.api.controller;

import java.io.File;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;

/* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1, reason: invalid class name */
/* JADX INFO: compiled from: Comparisons.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", NCXDocumentV3.XHTMLTgs.a, "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareByDescending$1"})
public final class BookController$getLastBackFileFromWebdav$lambda16$$inlined$sortByDescending$1<T> implements Comparator<T> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        File it = (File) t2;
        File it2 = (File) t;
        return ComparisonsKt.compareValues(Long.valueOf(it.lastModified()), Long.valueOf(it2.lastModified()));
    }
}
