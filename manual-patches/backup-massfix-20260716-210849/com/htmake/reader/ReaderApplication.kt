package com.htmake.reader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.ExtensionsKt
import com.htmake.reader.api.YueduApi
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.json.Json
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import javax.annotation.PostConstruct
import kotlin.jvm.internal.Intrinsics
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(exclude = [MongoAutoConfiguration::class, MongoDataAutoConfiguration::class])
@EnableScheduling
public open class ReaderApplication {
   private final lateinit var yueduApi: YueduApi

   @PostConstruct
   public open fun deployVerticle() {
      var var1: ObjectMapper = Json.mapper;
      ExtensionsKt.registerKotlinModule(var1);
      var1 = Json.prettyMapper;
      ExtensionsKt.registerKotlinModule(var1);
      Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      val var10000: Vertx = Companion.vertx();
      if (this.yueduApi == null) {
         Intrinsics.throwUninitializedPropertyAccessException("yueduApi");
         throw null;
      } else {
         var10000.deployVerticle(this.yueduApi);
      }
   }

   @Bean
   public open fun webClient(): WebClient {
      val webClientOptions: WebClientOptions = new WebClientOptions();
      webClientOptions.setTryUseCompression(true);
      webClientOptions.setLogActivity(true);
      webClientOptions.setFollowRedirects(true);
      webClientOptions.setTrustAll(true);
      val webClient: WebClient = WebClient.wrap(Companion.vertx().createHttpClient(new HttpClientOptions().setTrustAll(true)), webClientOptions);
      return webClient;
   }

   public companion object {
      public final val vertx: Vertx
         public final get() {
            return ReaderApplication.access$getVertx$delegate$cp().getValue() as Vertx;
         }


      public fun vertx(): Vertx {
         return this.getVertx();
      }
   }
}
