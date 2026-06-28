package com.htmake.reader.api.controller;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$saveLocalBookCover$result$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0018\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00040\u0003H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "handler", "Lio/vertx/core/Handler;", "Lio/vertx/core/AsyncResult;", "Lio/vertx/ext/web/client/HttpResponse;", "Lio/vertx/core/buffer/Buffer;"})
final class BookController$saveLocalBookCover$result$1 extends Lambda implements Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit> {
    final /* synthetic */ BookController this$0;
    final /* synthetic */ String $coverUrl;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookController$saveLocalBookCover$result$1(BookController this$0, String $coverUrl) {
        super(1);
        this.this$0 = this$0;
        this.$coverUrl = $coverUrl;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Handler<AsyncResult<HttpResponse<Buffer>>>) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.this$0.webClient.getAbs(this.$coverUrl).timeout(3000L).send(handler);
    }
}
