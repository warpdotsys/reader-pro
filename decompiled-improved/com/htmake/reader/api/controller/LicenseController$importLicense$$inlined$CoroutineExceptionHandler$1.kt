package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.VertExtKt
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.internal.Intrinsics
import kotlinx.coroutines.CoroutineExceptionHandler

public class `LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1` : AbstractCoroutineContextElement, CoroutineExceptionHandler {
   fun `LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1`(
      `$super_call_param$1`: CoroutineExceptionHandler.Key, var2: RoutingContext, var3: ReturnData
   ) {
      super(`$super_call_param$1`);
      this.$context$inlined = var2;
      this.$returnData$inlined = var3;
   }

   public override fun handleException(context: CoroutineContext, exception: Throwable) {
      LicenseControllerKt.access$getLogger$p().info("activate license error: {}", exception.getMessage());
      VertExtKt.success(this.$context$inlined, this.$returnData$inlined.setErrorMsg(Intrinsics.stringPlus("密钥激活失败: ", exception.getMessage())));
   }
}
