package com.htmake.reader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.htmake.reader.api.YueduApi
import com.htmake.reader.config.AppConfig
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.json.Json
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@SpringBootApplication(exclude = [MongoAutoConfiguration::class, MongoDataAutoConfiguration::class])
@EnableScheduling
@EnableConfigurationProperties(AppConfig::class)
@ComponentScan(basePackages = ["com.htmake.reader"])
open class ReaderApplication(
    private val yueduApi: YueduApi
) {
    @PostConstruct
    open fun deployVerticle() {
        runCatching {
            val km = KotlinModule.Builder().build()
            Json.mapper.registerModule(km)
            Json.prettyMapper.registerModule(km)
        }
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        vertx.deployVerticle(yueduApi)
    }

    @Bean
    open fun webClient(): WebClient {
        val opts = WebClientOptions()
            .setTryUseCompression(true)
            .setFollowRedirects(true)
            .setTrustAll(true)
        val http = vertx.createHttpClient(HttpClientOptions().setTrustAll(true))
        return WebClient.wrap(http, opts)
    }

    companion object {
        val vertx: Vertx by lazy { Vertx.vertx() }

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(ReaderApplication::class.java).run(*args)
        }
    }
}
