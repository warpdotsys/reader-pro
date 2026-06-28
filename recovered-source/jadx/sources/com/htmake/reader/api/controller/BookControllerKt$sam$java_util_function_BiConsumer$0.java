package com.htmake.reader.api.controller;

import java.util.function.BiConsumer;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookControllerKt$sam$java_util_function_BiConsumer$0.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
final class BookControllerKt$sam$java_util_function_BiConsumer$0 implements BiConsumer {
    private final /* synthetic */ Function2 function;

    BookControllerKt$sam$java_util_function_BiConsumer$0(Function2 function) {
        this.function = function;
    }

    @Override // java.util.function.BiConsumer
    public final /* synthetic */ void accept(Object p0, Object p1) {
        this.function.invoke(p0, p1);
    }
}
