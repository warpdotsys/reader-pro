package com.htmake.reader.api.controller;

import io.legado.app.data.entities.Book;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/BookController$saveBookConfig$newBook$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lio/legado/app/data/entities/Book;", "existBook"})
final class BookController$saveBookConfig$newBook$1 extends Lambda implements Function1<Book, Book> {
    final /* synthetic */ Ref.FloatRef $pdfImageWidth;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookController$saveBookConfig$newBook$1(Ref.FloatRef $pdfImageWidth) {
        super(1);
        this.$pdfImageWidth = $pdfImageWidth;
    }

    @NotNull
    public final Book invoke(@NotNull Book existBook) {
        Intrinsics.checkNotNullParameter(existBook, "existBook");
        existBook.setPdfImageWidth(this.$pdfImageWidth.element);
        BookControllerKt.logger.info("saveBookConfig: {}", existBook);
        return existBook;
    }
}
