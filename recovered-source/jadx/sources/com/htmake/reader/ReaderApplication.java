package com.htmake.reader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import com.htmake.reader.api.YueduApi;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import javax.annotation.PostConstruct;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/* JADX INFO: compiled from: ReaderApplication.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/ReaderApplication.class */
@EnableScheduling
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0017J\b\u0010\u0007\u001a\u00020\bH\u0017R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/htmake/reader/ReaderApplication;", PackageDocumentBase.PREFIX_OPF, "()V", "yueduApi", "Lcom/htmake/reader/api/YueduApi;", "deployVerticle", PackageDocumentBase.PREFIX_OPF, "webClient", "Lio/vertx/ext/web/client/WebClient;", "Companion", "reader-pro"})
public class ReaderApplication {

    @Autowired
    private YueduApi yueduApi;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private static final Lazy<Vertx> vertx$delegate = LazyKt.lazy(new Function0<Vertx>() { // from class: com.htmake.reader.ReaderApplication$Companion$vertx$2
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final Vertx m3invoke() {
            return Vertx.vertx();
        }
    });

    /* JADX INFO: compiled from: ReaderApplication.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/ReaderApplication$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004R#\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u00048FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007¨\u0006\n"}, d2 = {"Lcom/htmake/reader/ReaderApplication$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "vertx", "Lio/vertx/core/Vertx;", "kotlin.jvm.PlatformType", "getVertx", "()Lio/vertx/core/Vertx;", "vertx$delegate", "Lkotlin/Lazy;", "reader-pro"})
    public static final class Companion {
        static final /* synthetic */ KProperty<Object>[] $$delegatedProperties = {(KProperty) Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(Companion.class), "vertx", "getVertx()Lio/vertx/core/Vertx;"))};

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        public final Vertx getVertx() {
            return (Vertx) ReaderApplication.vertx$delegate.getValue();
        }

        public final Vertx vertx() {
            return getVertx();
        }
    }

    @PostConstruct
    public void deployVerticle() {
        ObjectMapper $this$deployVerticle_u24lambda_u2d0 = Json.mapper;
        Intrinsics.checkNotNullExpressionValue($this$deployVerticle_u24lambda_u2d0, PackageDocumentBase.PREFIX_OPF);
        ExtensionsKt.registerKotlinModule($this$deployVerticle_u24lambda_u2d0);
        ObjectMapper $this$deployVerticle_u24lambda_u2d1 = Json.prettyMapper;
        Intrinsics.checkNotNullExpressionValue($this$deployVerticle_u24lambda_u2d1, PackageDocumentBase.PREFIX_OPF);
        ExtensionsKt.registerKotlinModule($this$deployVerticle_u24lambda_u2d1);
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Vertx vertx = INSTANCE.vertx();
        Verticle verticle = this.yueduApi;
        if (verticle == null) {
            Intrinsics.throwUninitializedPropertyAccessException("yueduApi");
            throw null;
        }
        vertx.deployVerticle(verticle);
    }

    @Bean
    @NotNull
    public WebClient webClient() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setTryUseCompression(true);
        webClientOptions.setLogActivity(true);
        webClientOptions.setFollowRedirects(true);
        webClientOptions.setTrustAll(true);
        HttpClient httpClient = INSTANCE.vertx().createHttpClient(new HttpClientOptions().setTrustAll(true));
        WebClient webClient = WebClient.wrap(httpClient, webClientOptions);
        Intrinsics.checkNotNullExpressionValue(webClient, "webClient");
        return webClient;
    }
}
