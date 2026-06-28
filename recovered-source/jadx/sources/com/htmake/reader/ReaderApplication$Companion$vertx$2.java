package com.htmake.reader;

import io.vertx.core.Vertx;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: compiled from: ReaderApplication.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/ReaderApplication$Companion$vertx$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n"}, d2 = {"<anonymous>", "Lio/vertx/core/Vertx;", "kotlin.jvm.PlatformType"})
final class ReaderApplication$Companion$vertx$2 extends Lambda implements Function0<Vertx> {
    public static final ReaderApplication$Companion$vertx$2 INSTANCE = new ReaderApplication$Companion$vertx$2();

    ReaderApplication$Companion$vertx$2() {
        super(0);
    }

    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final Vertx m3invoke() {
        return Vertx.vertx();
    }
}
