package io.legado.app.help;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookSource;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BookHelp.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/BookHelp$saveImages$2$1$req$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "BookHelp.kt", l = {138}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.BookHelp$saveImages$2$1$req$1")
final class BookHelp$saveImages$2$1$req$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Integer>, Object> {
    int label;
    final /* synthetic */ BookSource $bookSource;
    final /* synthetic */ Book $book;
    final /* synthetic */ String $mSrc;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    BookHelp$saveImages$2$1$req$1(BookSource $bookSource, Book $book, String $mSrc, Continuation<? super BookHelp$saveImages$2$1$req$1> $completion) {
        super(2, $completion);
        this.$bookSource = $bookSource;
        this.$book = $book;
        this.$mSrc = $mSrc;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new BookHelp$saveImages$2$1$req$1(this.$bookSource, this.$book, this.$mSrc, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Integer> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.label = 1;
                if (BookHelp.INSTANCE.saveImage(this.$bookSource, this.$book, this.$mSrc, (Continuation) this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Boxing.boxInt(1);
    }
}
