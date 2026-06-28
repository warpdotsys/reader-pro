package com.htmake.reader.api.controller;

import com.htmake.reader.utils.ExtKt;
import io.vertx.core.http.HttpServerResponse;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$bookSourceDebugSSE$debugger$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "msg", PackageDocumentBase.PREFIX_OPF})
final class BookController$bookSourceDebugSSE$debugger$1 extends Lambda implements Function1<String, Unit> {
    final /* synthetic */ HttpServerResponse $response;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookController$bookSourceDebugSSE$debugger$1(HttpServerResponse $response) {
        super(1);
        this.$response = $response;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((String) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull String msg) {
        Intrinsics.checkNotNullParameter(msg, "msg");
        this.$response.write("data: " + ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to("msg", msg)), false) + "\n\n");
    }
}
