package com.htmake.reader.api.controller

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler

public class `LicenseController$checkLicense$$inlined$CoroutineExceptionHandler$1` : AbstractCoroutineContextElement, CoroutineExceptionHandler {
   fun `LicenseController$checkLicense$$inlined$CoroutineExceptionHandler$1`(`$super_call_param$1`: CoroutineExceptionHandler.Key) {
      super(`$super_call_param$1`);
   }

   public override fun handleException(context: CoroutineContext, exception: Throwable) {
      LicenseControllerKt.access$getLogger$p().info("check license error: {}", exception.getMessage());
   }
}
