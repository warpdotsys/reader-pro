package io.legado.app.help.http;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.CompletableDeferred;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

/* JADX INFO: compiled from: CoroutinesCallAdapterFactory.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "it", PackageDocumentBase.PREFIX_OPF})
final class CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$1 extends Lambda implements Function1<Throwable, Unit> {
    final /* synthetic */ CompletableDeferred<T> $deferred;
    final /* synthetic */ Call<T> $call;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$1(CompletableDeferred<T> $deferred, Call<T> $call) {
        super(1);
        this.$deferred = $deferred;
        this.$call = $call;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Throwable) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@Nullable Throwable it) {
        if (this.$deferred.isCancelled()) {
            this.$call.cancel();
        }
    }
}
