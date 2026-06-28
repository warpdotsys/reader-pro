package com.htmake.reader.api;

import io.vertx.ext.web.RoutingContext;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: YueduApi.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/YueduApi$initRouter$webdavController$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "ctx", "Lio/vertx/ext/web/RoutingContext;", "error", "Ljava/lang/Exception;", "Lkotlin/Exception;"})
final class YueduApi$initRouter$webdavController$1 extends Lambda implements Function2<RoutingContext, Exception, Unit> {
    final /* synthetic */ YueduApi this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    YueduApi$initRouter$webdavController$1(YueduApi this$0) {
        super(2);
        this.this$0 = this$0;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
        invoke((RoutingContext) p1, (Exception) p2);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull RoutingContext ctx, @NotNull Exception error) {
        Intrinsics.checkNotNullParameter(ctx, "ctx");
        Intrinsics.checkNotNullParameter(error, "error");
        this.this$0.onHandlerError(ctx, error);
    }
}
