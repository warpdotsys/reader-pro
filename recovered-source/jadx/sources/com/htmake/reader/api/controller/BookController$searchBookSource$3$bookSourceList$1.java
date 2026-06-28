package com.htmake.reader.api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$searchBookSource$3$bookSourceList$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", "Lcom/fasterxml/jackson/databind/node/ObjectNode;"})
final class BookController$searchBookSource$3$bookSourceList$1 extends Lambda implements Function1<ObjectNode, Boolean> {
    final /* synthetic */ Ref.ObjectRef<String> $bookSourceGroup;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookController$searchBookSource$3$bookSourceList$1(Ref.ObjectRef<String> $bookSourceGroup) {
        super(1);
        this.$bookSourceGroup = $bookSourceGroup;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        return Boolean.valueOf(invoke((ObjectNode) p1));
    }

    public final boolean invoke(@NotNull ObjectNode it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String _bookSourceGroup = it.get("bookSourceGroup").asText();
        String str = _bookSourceGroup;
        return !(str == null || str.length() == 0) && StringsKt.indexOf$default(Intrinsics.stringPlus(_bookSourceGroup, ","), Intrinsics.stringPlus((String) this.$bookSourceGroup.element, ","), 0, false, 6, (Object) null) >= 0;
    }
}
