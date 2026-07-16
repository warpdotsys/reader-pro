/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.module.kotlin.ExtensionsKt
 *  io.vertx.core.Verticle
 *  io.vertx.core.Vertx
 *  io.vertx.core.http.HttpClient
 *  io.vertx.core.http.HttpClientOptions
 *  io.vertx.core.json.Json
 *  io.vertx.ext.web.client.WebClient
 *  io.vertx.ext.web.client.WebClientOptions
 *  javax.annotation.PostConstruct
 *  kotlin.Lazy
 *  kotlin.LazyKt
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.PropertyReference1
 *  kotlin.jvm.internal.PropertyReference1Impl
 *  kotlin.jvm.internal.Reflection
 *  kotlin.reflect.KDeclarationContainer
 *  kotlin.reflect.KProperty
 *  org.jetbrains.annotations.NotNull
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
 *  org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
 *  org.springframework.context.annotation.Bean
 *  org.springframework.scheduling.annotation.EnableScheduling
 */
package com.htmake.reader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import com.htmake.reader.ReaderApplication;
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
import kotlin.jvm.internal.PropertyReference1;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableScheduling
@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0017J\b\u0010\u0007\u001a\u00020\bH\u0017R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lcom/htmake/reader/ReaderApplication;", "", "()V", "yueduApi", "Lcom/htmake/reader/api/YueduApi;", "deployVerticle", "", "webClient", "Lio/vertx/ext/web/client/WebClient;", "Companion", "reader-pro"})
public class ReaderApplication {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Autowired
    private YueduApi yueduApi;
    @NotNull
    private static final Lazy<Vertx> vertx$delegate = LazyKt.lazy((Function0)Companion.vertx.2.INSTANCE);

    @PostConstruct
    public void deployVerticle() {
        Object object = Json.mapper;
        boolean bl = false;
        boolean bl2 = false;
        ObjectMapper $this$deployVerticle_u24lambda_u2d0 = object;
        boolean bl3 = false;
        Intrinsics.checkNotNullExpressionValue((Object)$this$deployVerticle_u24lambda_u2d0, (String)"");
        ExtensionsKt.registerKotlinModule((ObjectMapper)$this$deployVerticle_u24lambda_u2d0);
        object = Json.prettyMapper;
        bl = false;
        bl2 = false;
        ObjectMapper $this$deployVerticle_u24lambda_u2d1 = object;
        boolean bl4 = false;
        Intrinsics.checkNotNullExpressionValue((Object)$this$deployVerticle_u24lambda_u2d1, (String)"");
        ExtensionsKt.registerKotlinModule((ObjectMapper)$this$deployVerticle_u24lambda_u2d1);
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        object = this.yueduApi;
        if (object == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"yueduApi");
            throw null;
        }
        Companion.vertx().deployVerticle((Verticle)object);
    }

    @Bean
    @NotNull
    public WebClient webClient() {
        WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setTryUseCompression(true);
        webClientOptions.setLogActivity(true);
        webClientOptions.setFollowRedirects(true);
        webClientOptions.setTrustAll(true);
        HttpClient httpClient = Companion.vertx().createHttpClient(new HttpClientOptions().setTrustAll(true));
        WebClient webClient2 = WebClient.wrap((HttpClient)httpClient, (WebClientOptions)webClientOptions);
        Intrinsics.checkNotNullExpressionValue((Object)webClient2, (String)"webClient");
        return webClient2;
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004R#\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\n"}, d2={"Lcom/htmake/reader/ReaderApplication$Companion;", "", "()V", "vertx", "Lio/vertx/core/Vertx;", "kotlin.jvm.PlatformType", "getVertx", "()Lio/vertx/core/Vertx;", "vertx$delegate", "Lkotlin/Lazy;", "reader-pro"})
    public static final class Companion {
        static final /* synthetic */ KProperty<Object>[] $$delegatedProperties;

        private Companion() {
        }

        public final Vertx getVertx() {
            Lazy lazy = vertx$delegate;
            boolean bl = false;
            return (Vertx)lazy.getValue();
        }

        public final Vertx vertx() {
            return this.getVertx();
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        static {
            KProperty[] kPropertyArray = new KProperty[]{(KProperty)Reflection.property1((PropertyReference1)((PropertyReference1)new PropertyReference1Impl((KDeclarationContainer)Reflection.getOrCreateKotlinClass(Companion.class), "vertx", "getVertx()Lio/vertx/core/Vertx;")))};
            $$delegatedProperties = kPropertyArray;
        }
    }
}

