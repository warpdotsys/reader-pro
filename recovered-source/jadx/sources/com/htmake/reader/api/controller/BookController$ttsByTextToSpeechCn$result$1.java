package com.htmake.reader.api.controller;

import io.legado.app.constant.AppConst;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.ext.web.client.HttpResponse;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$ttsByTextToSpeechCn$result$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0018\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u00040\u0003H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "handler", "Lio/vertx/core/Handler;", "Lio/vertx/core/AsyncResult;", "Lio/vertx/ext/web/client/HttpResponse;", "Lio/vertx/core/buffer/Buffer;"})
final class BookController$ttsByTextToSpeechCn$result$1 extends Lambda implements Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit> {
    final /* synthetic */ BookController this$0;
    final /* synthetic */ String $ttsUrl;
    final /* synthetic */ CaseInsensitiveHeaders $multiMap;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookController$ttsByTextToSpeechCn$result$1(BookController this$0, String $ttsUrl, CaseInsensitiveHeaders $multiMap) {
        super(1);
        this.this$0 = this$0;
        this.$ttsUrl = $ttsUrl;
        this.$multiMap = $multiMap;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Handler<AsyncResult<HttpResponse<Buffer>>>) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.this$0.webClient.postAbs(this.$ttsUrl).timeout(5000L).putHeader("Origin", "https://www.text-to-speech.cn").putHeader("Referer", "https://www.text-to-speech.cn/").putHeader(AppConst.UA_NAME, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36").sendForm(this.$multiMap, handler);
    }
}
