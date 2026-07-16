package com.htmake.reader.api.controller

import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler

public class `BookController$getBookCover$$inlined$CoroutineExceptionHandler$1` : AbstractCoroutineContextElement, CoroutineExceptionHandler {
   fun `BookController$getBookCover$$inlined$CoroutineExceptionHandler$1`(`$super_call_param$1`: CoroutineExceptionHandler.Key, var2: RoutingContext) {
      super(`$super_call_param$1`);
      this.$context$inlined = var2;
   }

   public override fun handleException(context: CoroutineContext, exception: Throwable) {
      BookControllerKt.access$getLogger$p().info("get cover error: {}", exception.getMessage());
      this.$context$inlined.response().setStatusCode(404).end();
   }
}
