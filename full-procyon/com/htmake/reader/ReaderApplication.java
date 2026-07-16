// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader;

import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import kotlin.jvm.internal.PropertyReference1;
import kotlin.reflect.KProperty;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.springframework.context.annotation.Bean;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.client.WebClient;
import javax.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Verticle;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import kotlin.jvm.internal.Intrinsics;
import io.vertx.core.json.Json;
import io.vertx.core.Vertx;
import kotlin.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import com.htmake.reader.api.YueduApi;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
@EnableScheduling
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005?\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0017J\b\u0010\u0007\u001a\u00020\bH\u0017R\u0012\u0010\u0003\u001a\u00020\u00048\u0002@\u0002X\u0083.?\u0006\u0002\n\u0000¡§\u0006\n" }, d2 = { "Lcom/htmake/reader/ReaderApplication;", "", "()V", "yueduApi", "Lcom/htmake/reader/api/YueduApi;", "deployVerticle", "", "webClient", "Lio/vertx/ext/web/client/WebClient;", "Companion", "reader-pro" })
public class ReaderApplication
{
    @NotNull
    public static final Companion Companion;
    @Autowired
    private YueduApi yueduApi;
    @NotNull
    private static final Lazy<Vertx> vertx$delegate;
    
    @PostConstruct
    public void deployVerticle() {
        final ObjectMapper $this$deployVerticle_u24lambda_u2d0 = Json.mapper;
        final int n = 0;
        Intrinsics.checkNotNullExpressionValue((Object)$this$deployVerticle_u24lambda_u2d0, "");
        ExtensionsKt.registerKotlinModule($this$deployVerticle_u24lambda_u2d0);
        final ObjectMapper $this$deployVerticle_u24lambda_u2d2 = Json.prettyMapper;
        final int n2 = 0;
        Intrinsics.checkNotNullExpressionValue((Object)$this$deployVerticle_u24lambda_u2d2, "");
        ExtensionsKt.registerKotlinModule($this$deployVerticle_u24lambda_u2d2);
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final Vertx vertx = ReaderApplication.Companion.vertx();
        final YueduApi yueduApi = this.yueduApi;
        if (yueduApi == null) {
            Intrinsics.throwUninitializedPropertyAccessException("yueduApi");
            throw null;
        }
        vertx.deployVerticle((Verticle)yueduApi);
    }
    
    @Bean
    @NotNull
    public WebClient webClient() {
        final WebClientOptions webClientOptions = new WebClientOptions();
        webClientOptions.setTryUseCompression(true);
        webClientOptions.setLogActivity(true);
        webClientOptions.setFollowRedirects(true);
        webClientOptions.setTrustAll(true);
        final HttpClient httpClient = ReaderApplication.Companion.vertx().createHttpClient(new HttpClientOptions().setTrustAll(true));
        final WebClient webClient = WebClient.wrap(httpClient, webClientOptions);
        Intrinsics.checkNotNullExpressionValue((Object)webClient, "webClient");
        return webClient;
    }
    
    public static final /* synthetic */ Lazy access$getVertx$delegate$cp() {
        return ReaderApplication.vertx$delegate;
    }
    
    static {
        Companion = new Companion(null);
        vertx$delegate = LazyKt.lazy((Function0)ReaderApplication$Companion$vertx.ReaderApplication$Companion$vertx$2.INSTANCE);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004R#\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u00048FX\u0086\u0084\u0002?\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007¡§\u0006\n" }, d2 = { "Lcom/htmake/reader/ReaderApplication$Companion;", "", "()V", "vertx", "Lio/vertx/core/Vertx;", "kotlin.jvm.PlatformType", "getVertx", "()Lio/vertx/core/Vertx;", "vertx$delegate", "Lkotlin/Lazy;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        public final Vertx getVertx() {
            return (Vertx)ReaderApplication.access$getVertx$delegate$cp().getValue();
        }
        
        public final Vertx vertx() {
            return this.getVertx();
        }
        
        static {
            $$delegatedProperties = new KProperty[] { (KProperty)Reflection.property1((PropertyReference1)new PropertyReference1Impl((KDeclarationContainer)Reflection.getOrCreateKotlinClass((Class)Companion.class), "vertx", "getVertx()Lio/vertx/core/Vertx;")) };
        }
    }
}
