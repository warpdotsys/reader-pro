package com.htmake.reader.api.controller;

import com.htmake.reader.utils.SpringContextUtils;
import io.vertx.ext.web.client.WebClient;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* JADX INFO: compiled from: LicenseController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/LicenseController$webClient$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n"}, d2 = {"<anonymous>", "Lio/vertx/ext/web/client/WebClient;", "kotlin.jvm.PlatformType"})
final class LicenseController$webClient$2 extends Lambda implements Function0<WebClient> {
    public static final LicenseController$webClient$2 INSTANCE = new LicenseController$webClient$2();

    LicenseController$webClient$2() {
        super(0);
    }

    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final WebClient m53invoke() {
        return (WebClient) SpringContextUtils.getBean("webClient", WebClient.class);
    }
}
