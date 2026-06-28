package com.htmake.reader.api.controller;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$backupToMongodb$handler$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "userNameSpace", PackageDocumentBase.PREFIX_OPF})
final class BookController$backupToMongodb$handler$1 extends Lambda implements Function1<String, Unit> {
    final /* synthetic */ ArrayList<String> $syncDataFileList;
    final /* synthetic */ BookController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookController$backupToMongodb$handler$1(ArrayList<String> $syncDataFileList, BookController this$0) {
        super(1);
        this.$syncDataFileList = $syncDataFileList;
        this.this$0 = this$0;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((String) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull String userNameSpace) {
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        Iterable $this$forEach$iv = this.$syncDataFileList;
        BookController bookController = this.this$0;
        for (Object element$iv : $this$forEach$iv) {
            String it = (String) element$iv;
            String content = bookController.getUserStorage(userNameSpace, it);
            if (content != null) {
                bookController.saveUserStorage(userNameSpace, it, content);
            }
        }
    }
}
