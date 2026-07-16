package com.htmake.reader.api.controller

import io.vertx.core.http.HttpServerResponse
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler

public class `BookController$textToSpeech$$inlined$CoroutineExceptionHandler$1` : AbstractCoroutineContextElement, CoroutineExceptionHandler {
   fun `BookController$textToSpeech$$inlined$CoroutineExceptionHandler$1`(`$super_call_param$1`: CoroutineExceptionHandler.Key, var2: HttpServerResponse) {
      super(`$super_call_param$1`);
      this.$response$inlined = var2;
   }

   public override fun handleException(context: CoroutineContext, exception: Throwable) {
      BookControllerKt.access$getLogger$p().info("tts error: {}", exception.getMessage());
      this.$response$inlined.setStatusCode(404).end();
   }
}
