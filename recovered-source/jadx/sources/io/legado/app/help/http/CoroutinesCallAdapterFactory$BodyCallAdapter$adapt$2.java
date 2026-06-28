package io.legado.app.help.http;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableDeferred;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: compiled from: CoroutinesCallAdapterFactory.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$2.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J$\u0010\b\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0016¨\u0006\u000b"}, d2 = {"io/legado/app/help/http/CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$2", "Lretrofit2/Callback;", "onFailure", PackageDocumentBase.PREFIX_OPF, "call", "Lretrofit2/Call;", "t", PackageDocumentBase.PREFIX_OPF, "onResponse", "response", "Lretrofit2/Response;", "reader-pro"})
public final class CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$2<T> implements Callback<T> {
    final /* synthetic */ CompletableDeferred<T> $deferred;

    CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$2(CompletableDeferred<T> $deferred) {
        this.$deferred = $deferred;
    }

    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(t, "t");
        this.$deferred.completeExceptionally(t);
    }

    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(response, "response");
        if (response.isSuccessful()) {
            CompletableDeferred<T> completableDeferred = this.$deferred;
            Object objBody = response.body();
            Intrinsics.checkNotNull(objBody);
            completableDeferred.complete(objBody);
            return;
        }
        this.$deferred.completeExceptionally(new HttpException(response));
    }
}
