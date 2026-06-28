package io.legado.app.help.http;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Call;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: OkHttpUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt$await$2$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it", PackageDocumentBase.PREFIX_OPF})
final class OkHttpUtilsKt$await$2$1 extends Lambda implements Function1<Throwable, Unit> {
    final /* synthetic */ Call $this_await;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    OkHttpUtilsKt$await$2$1(Call $this_await) {
        super(1);
        this.$this_await = $this_await;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Throwable) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(@Nullable Throwable it) {
        this.$this_await.cancel();
    }
}
