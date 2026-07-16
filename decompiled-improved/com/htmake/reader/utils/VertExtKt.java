// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import org.slf4j.MDC;
import io.vertx.core.Handler;
import io.vertx.ext.web.Route;
import kotlin.jvm.functions.Function0;
import com.htmake.reader.entity.BasicError;
import java.net.URLDecoder;
import io.vertx.core.json.JsonObject;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import io.vertx.ext.web.RoutingContext;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0018\u0010\u0005\u001a\u00020\u0001*\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\b\u001a\u0014\u0010\t\u001a\u00020\u0001*\u00020\u00022\b\u0010\n\u001a\u0004\u0018\u00010\u000b¡§\u0006\f" }, d2 = { "error", "", "Lio/vertx/ext/web/RoutingContext;", "throwable", "", "globalHandler", "Lio/vertx/ext/web/Route;", "handler", "Lio/vertx/core/Handler;", "success", "any", "", "reader-pro" })
public final class VertExtKt
{
    public static final void success(@NotNull final RoutingContext $this$success, @Nullable final Object any) {
        Intrinsics.checkNotNullParameter((Object)$this$success, "<this>");
        String string;
        if (any instanceof JsonObject) {
            string = ((JsonObject)any).toString();
        }
        else {
            final String json = ExtKt.getGson().toJson(any);
            Intrinsics.checkNotNullExpressionValue((Object)json, "{\n        gson.toJson(any)\n    }");
            string = json;
        }
        final String toJson = string;
        $this$success.response().putHeader("content-type", "application/json; charset=utf-8").end(toJson);
    }
    
    public static final void error(@NotNull final RoutingContext $this$error, @NotNull final Throwable throwable) {
        Intrinsics.checkNotNullParameter((Object)$this$error, "<this>");
        Intrinsics.checkNotNullParameter((Object)throwable, "throwable");
        final String path = URLDecoder.decode($this$error.request().absoluteURI(), "UTF-8");
        final String s = "Internal Server Error";
        final String string = throwable.toString();
        final String value = String.valueOf(throwable.getMessage());
        Intrinsics.checkNotNullExpressionValue((Object)path, "path");
        final BasicError basicError = new BasicError(s, string, value, path, 500, System.currentTimeMillis());
        final String errorJson = ExtKt.getGson().toJson((Object)basicError);
        ExtKt.getLogger().error("Internal Server Error", throwable);
        ExtKt.getLogger().error((Function0)new VertExtKt$error.VertExtKt$error$1(errorJson));
        $this$error.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(500).end(errorJson);
    }
    
    public static final void globalHandler(@NotNull final Route $this$globalHandler, @NotNull final Handler<RoutingContext> handler) {
        Intrinsics.checkNotNullParameter((Object)$this$globalHandler, "<this>");
        Intrinsics.checkNotNullParameter((Object)handler, "handler");
        $this$globalHandler.handler(VertExtKt::globalHandler$lambda-0);
    }
    
    private static final void globalHandler$lambda-0(final Handler $handler, final RoutingContext ctx) {
        Intrinsics.checkNotNullParameter((Object)$handler, "$handler");
        String traceId = (String)ctx.get("traceId");
        final CharSequence charSequence = traceId;
        if (charSequence == null || charSequence.length() == 0) {
            traceId = ExtKt.getTraceId();
        }
        MDC.put("traceId", traceId);
        ctx.put("traceId", (Object)traceId);
        $handler.handle((Object)ctx);
    }
}
