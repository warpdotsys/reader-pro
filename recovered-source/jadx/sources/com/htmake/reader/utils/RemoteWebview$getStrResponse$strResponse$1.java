package com.htmake.reader.utils;

import io.legado.app.help.http.OkHttpUtilsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: RemoteWebview.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/utils/RemoteWebview$getStrResponse$strResponse$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lokhttp3/Request$Builder;"})
final class RemoteWebview$getStrResponse$strResponse$1 extends Lambda implements Function1<Request.Builder, Unit> {
    final /* synthetic */ Ref.ObjectRef<String> $remoteApi;
    final /* synthetic */ Ref.ObjectRef<String> $requestBody;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    RemoteWebview$getStrResponse$strResponse$1(Ref.ObjectRef<String> $remoteApi, Ref.ObjectRef<String> $requestBody) {
        super(1);
        this.$remoteApi = $remoteApi;
        this.$requestBody = $requestBody;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Request.Builder) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull Request.Builder $this$newCallStrResponse) {
        Intrinsics.checkNotNullParameter($this$newCallStrResponse, "$this$newCallStrResponse");
        $this$newCallStrResponse.url((String) this.$remoteApi.element);
        OkHttpUtilsKt.postJson($this$newCallStrResponse, (String) this.$requestBody.element);
    }
}
