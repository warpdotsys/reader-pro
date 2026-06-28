package com.htmake.reader.api.controller;

import kotlin.Metadata;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineExceptionHandler;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: CoroutineExceptionHandler.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u00012\u00020\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t¸\u0006\u0000"}, d2 = {"kotlinx/coroutines/CoroutineExceptionHandlerKt$CoroutineExceptionHandler$1", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "handleException", PackageDocumentBase.PREFIX_OPF, "context", "Lkotlin/coroutines/CoroutineContext;", "exception", PackageDocumentBase.PREFIX_OPF, "kotlinx-coroutines-core"})
public final class BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1 extends AbstractCoroutineContextElement implements CoroutineExceptionHandler {
    public BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key $super_call_param$1) {
        super((CoroutineContext.Key) $super_call_param$1);
    }

    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        BookControllerKt.logger.info("cacheBookOnServer error: {}", exception.getMessage());
    }
}
