package io.legado.app.constant;

import java.text.SimpleDateFormat;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: AppConst.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/constant/AppConst$timeFormat$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Ljava/text/SimpleDateFormat;"})
final class AppConst$timeFormat$2 extends Lambda implements Function0<SimpleDateFormat> {
    public static final AppConst$timeFormat$2 INSTANCE = new AppConst$timeFormat$2();

    AppConst$timeFormat$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final SimpleDateFormat m129invoke() {
        return new SimpleDateFormat("HH:mm");
    }
}
