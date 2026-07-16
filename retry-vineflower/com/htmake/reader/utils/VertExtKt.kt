package com.htmake.reader.utils

import com.htmake.reader.entity.BasicError
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import java.net.URLDecoder
import kotlin.jvm.functions.Function0
import org.jetbrains.annotations.Nullable
import org.slf4j.MDC

public fun RoutingContext.success(any: Any?) {
   val var10000: java.lang.String;
   if (any is JsonObject) {
      var10000 = (any as JsonObject).toString();
   } else {
      val var3: java.lang.String = ExtKt.getGson().toJson(any);
      var10000 = var3;
   }

   `$this$success`.response().putHeader("content-type", "application/json; charset=utf-8").end(var10000);
}

public fun RoutingContext.error(throwable: Throwable) {
   val path: java.lang.String = URLDecoder.decode(`$this$error`.request().absoluteURI(), "UTF-8");
   val var10003: java.lang.String = throwable.toString();
   val var10004: java.lang.String = java.lang.String.valueOf(throwable.getMessage());
   val errorJson: java.lang.String = ExtKt.getGson().toJson(new BasicError("Internal Server Error", var10003, var10004, path, 500, System.currentTimeMillis()));
   ExtKt.getLogger().error("Internal Server Error", throwable);
   ExtKt.getLogger().error((new Function0<Object>(errorJson) {
      {
         super(0);
         this.$errorJson = `$errorJson`;
      }

      @Nullable
      @Override
      public final Object invoke() {
         return this.$errorJson;
      }
   }) as () -> Any);
   `$this$error`.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(500).end(errorJson);
}

public fun Route.globalHandler(handler: Handler<RoutingContext>) {
   `$this$globalHandler`.handler(VertExtKt::globalHandler$lambda-0);
}

fun `globalHandler$lambda-0`(`$handler`: Handler, ctx: RoutingContext) {
   var traceId: java.lang.String = ctx.get("traceId");
   if (traceId == null || traceId.length() == 0) {
      traceId = ExtKt.getTraceId();
   }

   MDC.put("traceId", traceId);
   ctx.put("traceId", traceId);
   `$handler`.handle(ctx);
}
