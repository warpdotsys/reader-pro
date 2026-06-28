package io.legado.app.help.http;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: OkHttpUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt$await$2$2.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u000b"}, d2 = {"io/legado/app/help/http/OkHttpUtilsKt$await$2$2", "Lokhttp3/Callback;", "onFailure", PackageDocumentBase.PREFIX_OPF, "call", "Lokhttp3/Call;", "e", "Ljava/io/IOException;", "onResponse", "response", "Lokhttp3/Response;", "reader-pro"})
public final class OkHttpUtilsKt$await$2$2 implements Callback {
    final /* synthetic */ CancellableContinuation<Response> $block;

    OkHttpUtilsKt$await$2$2(CancellableContinuation<? super Response> $block) {
        this.$block = $block;
    }

    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(e, "e");
        Continuation continuation = this.$block;
        Result.Companion companion = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(ResultKt.createFailure(e)));
    }

    public void onResponse(@NotNull Call call, @NotNull Response response) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(response, "response");
        Continuation continuation = this.$block;
        Result.Companion companion = Result.Companion;
        continuation.resumeWith(Result.constructor-impl(response));
    }
}
