package io.legado.app.help.http

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import io.legado.app.utils.EncodingDetect
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.Utf8BomUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.Map.Entry
import kotlin.Result.Companion
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.coroutines.jvm.internal.DebugProbesKt
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CancellableContinuationImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.JobKt
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.Request.Builder
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public suspend fun OkHttpClient.newCallResponse(retry: Int = ..., builder: (Builder) -> Unit): Response {
   return BuildersKt.withContext(
      Dispatchers.getIO(), (new Function2<CoroutineScope, Continuation<? super Response>, Object>(builder, retry, `$this$newCallResponse`, null) {
         Object L$0;
         int I$0;
         int I$1;
         int label;

         {
            super(2, `$completionx`);
            this.$builder = `$builder`;
            this.$retry = `$retry`;
            this.$this_newCallResponse = `$receiver`;
         }

         // NOTE: decompiler split irreducible bytecode (logic preserved)
         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            var response: Response;
            label32: {
               val var7: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               var requestBuilder: Builder;
               var var4: Int;
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     requestBuilder = new Builder();
                     this.$builder.invoke(requestBuilder);
                     response = null;
                     var4 = 0;
                     if (0 > this.$retry) {
                        break label32;
                     }
                     break;
                  case 1:
                     val i: Int = this.I$1;
                     var4 = this.I$0;
                     requestBuilder = this.L$0 as Builder;
                     ResultKt.throwOnFailure(`$result`);
                     response = `$result` as Response;
                     if ((`$result` as Response).isSuccessful()) {
                        return response;
                     }

                     if (i == this.$retry) {
                        break label32;
                     }
                     break;
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               val var10: Int;
               do {
                  var10 = var4++;
                  var var10000: Call = this.$this_newCallResponse.newCall(requestBuilder.build());
                  val var10001: Continuation = this;
                  this.L$0 = requestBuilder;
                  this.I$0 = var4;
                  this.I$1 = var10;
                  this.label = 1;
                  var10000 = (Call)OkHttpUtilsKt.await(var10000, var10001);
                  if (var10000 === var7) {
                     return var7;
                  }

                  response = var10000 as Response;
                  if ((var10000 as Response).isSuccessful()) {
                     return response;
                  }
               } while (i != this.$retry);
            }

            return response;
         }

         @NotNull
         @Override
         public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new <anonymous constructor>(this.$builder, this.$retry, this.$this_newCallResponse, `$completion`);
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Response> p2) {
            return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
         }
      }) as Function2, `$completion`
   );
}

@JvmSynthetic
fun `newCallResponse$default`(var0: OkHttpClient, var1: Int, var2: Function1, var3: Continuation, var4: Int, var5: Any): Any {
   if ((var4 and 1) != 0) {
      var1 = 0;
   }

   return newCallResponse(var0, var1, var2, var3);
}

public suspend fun OkHttpClient.newCallResponseBody(retry: Int = ..., builder: (Builder) -> Unit): ResponseBody {
   var `$continuation`: Continuation;
   label24: {
      if (`$completion` is SyntheticContinuation) {
         `$continuation` = `$completion` as SyntheticContinuation;
         if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
            `$continuation`.label -= Integer.MIN_VALUE;
            break label24;
         }
      }

      `$continuation` = new ContinuationImpl(`$completion`) {
         int label;

         {
            super(`$completion`);
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            this.result = `$result`;
            this.label |= Integer.MIN_VALUE;
            return OkHttpUtilsKt.newCallResponseBody(null, 0, null, this);
         }
      };
   }

   val `$result`: Any = `$continuation`.result;
   val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
   var var10000: Any;
   switch ($continuation.label) {
      case 0:
         ResultKt.throwOnFailure(`$result`);
         `$continuation`.label = 1;
         var10000 = newCallResponse(`$this$newCallResponseBody`, retry, builder, `$continuation`);
         if (var10000 === var12) {
            return var12;
         }
         break;
      case 1:
         ResultKt.throwOnFailure(`$result`);
         var10000 = `$result`;
         break;
      default:
         throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
   }

   val it: Response = var10000 as Response;
   val var9: ResponseBody = (var10000 as Response).body();
   if (var9 == null) {
      throw new IOException(it.message());
   } else {
      return var9;
   }
}

@JvmSynthetic
fun `newCallResponseBody$default`(var0: OkHttpClient, var1: Int, var2: Function1, var3: Continuation, var4: Int, var5: Any): Any {
   if ((var4 and 1) != 0) {
      var1 = 0;
   }

   return newCallResponseBody(var0, var1, var2, var3);
}

public suspend fun OkHttpClient.newCall(retry: Int = ..., builder: (Builder) -> Unit): ResponseBody {
   var `$continuation`: Continuation;
   label47: {
      if (`$completion` is SyntheticContinuation) {
         `$continuation` = `$completion` as SyntheticContinuation;
         if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
            `$continuation`.label -= Integer.MIN_VALUE;
            break label47;
         }
      }

      `$continuation` = new ContinuationImpl(`$completion`) {
         Object L$0;
         Object L$1;
         int I$0;
         int I$1;
         int I$2;
         int label;

         {
            super(`$completion`);
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            this.result = `$result`;
            this.label |= Integer.MIN_VALUE;
            return OkHttpUtilsKt.newCall(null, 0, null, this);
         }
      };
   }

   var response: Response;
   label41: {
      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var requestBuilder: Builder;
      var var6: Int;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            requestBuilder = new Builder();
            builder.invoke(requestBuilder);
            response = null;
            var6 = 0;
            if (0 > retry) {
               break label41;
            }
            break;
         case 1:
            val i: Int = `$continuation`.I$2;
            var6 = `$continuation`.I$1;
            retry = `$continuation`.I$0;
            requestBuilder = `$continuation`.L$1 as Builder;
            `$this$newCall` = `$continuation`.L$0 as OkHttpClient;
            ResultKt.throwOnFailure(`$result`);
            response = `$result` as Response;
            if ((`$result` as Response).isSuccessful()) {
               val var10000: ResponseBody = response.body();
               return var10000;
            }

            if (i == retry) {
               break label41;
            }
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var14: Int;
      do {
         var14 = var6++;
         var var15: Call = `$this$newCall`.newCall(requestBuilder.build());
         `$continuation`.L$0 = `$this$newCall`;
         `$continuation`.L$1 = requestBuilder;
         `$continuation`.I$0 = retry;
         `$continuation`.I$1 = var6;
         `$continuation`.I$2 = var14;
         `$continuation`.label = 1;
         var15 = (Call)await(var15, `$continuation`);
         if (var15 === var10) {
            return var10;
         }

         response = var15 as Response;
         if ((var15 as Response).isSuccessful()) {
            val var17: ResponseBody = response.body();
            return var17;
         }
      } while (i != retry);
   }

   val var12: ResponseBody = response.body();
   if (var12 == null) {
      throw new IOException(response.message());
   } else {
      return var12;
   }
}

@JvmSynthetic
fun `newCall$default`(var0: OkHttpClient, var1: Int, var2: Function1, var3: Continuation, var4: Int, var5: Any): Any {
   if ((var4 and 1) != 0) {
      var1 = 0;
   }

   return newCall(var0, var1, var2, var3);
}

public suspend fun OkHttpClient.newCallStrResponse(retry: Int = ..., builder: (Builder) -> Unit): StrResponse {
   var `$continuation`: Continuation;
   label48: {
      if (`$completion` is SyntheticContinuation) {
         `$continuation` = `$completion` as SyntheticContinuation;
         if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
            `$continuation`.label -= Integer.MIN_VALUE;
            break label48;
         }
      }

      `$continuation` = new ContinuationImpl(`$completion`) {
         Object L$0;
         Object L$1;
         int I$0;
         int I$1;
         int I$2;
         int label;

         {
            super(`$completion`);
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            this.result = `$result`;
            this.label |= Integer.MIN_VALUE;
            return OkHttpUtilsKt.newCallStrResponse(null, 0, null, this);
         }
      };
   }

   var response: Response;
   label42: {
      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var requestBuilder: Builder;
      var var6: Int;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            requestBuilder = new Builder();
            builder.invoke(requestBuilder);
            response = null;
            var6 = 0;
            if (0 > retry) {
               break label42;
            }
            break;
         case 1:
            val i: Int = `$continuation`.I$2;
            var6 = `$continuation`.I$1;
            retry = `$continuation`.I$0;
            requestBuilder = `$continuation`.L$1 as Builder;
            `$this$newCallStrResponse` = `$continuation`.L$0 as OkHttpClient;
            ResultKt.throwOnFailure(`$result`);
            response = `$result` as Response;
            if ((`$result` as Response).isSuccessful()) {
               val var10003: ResponseBody = response.body();
               return new StrResponse(response, text$default(var10003, null, 1, null));
            }

            if (i == retry) {
               break label42;
            }
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var14: Int;
      do {
         var14 = var6++;
         JobKt.ensureActive(`$continuation`.getContext());
         var var10000: Call = `$this$newCallStrResponse`.newCall(requestBuilder.build());
         `$continuation`.L$0 = `$this$newCallStrResponse`;
         `$continuation`.L$1 = requestBuilder;
         `$continuation`.I$0 = retry;
         `$continuation`.I$1 = var6;
         `$continuation`.I$2 = var14;
         `$continuation`.label = 1;
         var10000 = (Call)await(var10000, `$continuation`);
         if (var10000 === var10) {
            return var10;
         }

         response = var10000 as Response;
         if ((var10000 as Response).isSuccessful()) {
            val var16: ResponseBody = response.body();
            return new StrResponse(response, text$default(var16, null, 1, null));
         }
      } while (i != retry);
   }

   val var12: ResponseBody = response.body();
   return new StrResponse(response, if (var12 == null) response.message() else text$default(var12, null, 1, null));
}

@JvmSynthetic
fun `newCallStrResponse$default`(var0: OkHttpClient, var1: Int, var2: Function1, var3: Continuation, var4: Int, var5: Any): Any {
   if ((var4 and 1) != 0) {
      var1 = 0;
   }

   return newCallStrResponse(var0, var1, var2, var3);
}

public suspend fun Call.await(): Response {
   val `cancellable$iv`: CancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(`$completion`), 1);
   `cancellable$iv`.initCancellability();
   val block: CancellableContinuation = `cancellable$iv`;
   `cancellable$iv`.invokeOnCancellation((new Function1<java.lang.Throwable, Unit>(`$this$await`) {
      {
         super(1);
         this.$this_await = `$receiver`;
      }

      public final void invoke(@Nullable java.lang.Throwable it) {
         this.$this_await.cancel();
      }
   }) as (java.lang.Throwable?) -> Unit);
   `$this$await`.enqueue(new Callback(block) {
      {
         this.$block = `$block`;
      }

      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
         val var3: Continuation = this.$block;
         val var5: Companion = Result.Companion;
         var3.resumeWith(Result.constructor-impl(ResultKt.createFailure(e)));
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) {
         val var3: Continuation = this.$block;
         val var5: Companion = Result.Companion;
         var3.resumeWith(Result.constructor-impl(response));
      }
   });
   val var10000: Any = `cancellable$iv`.getResult();
   if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
      DebugProbesKt.probeCoroutineSuspended(`$completion`);
   }

   return var10000;
}

public fun ResponseBody.text(encode: String? = null): String {
   val responseBytes: ByteArray = Utf8BomUtils.INSTANCE.removeUTF8BOM(`$this$text`.bytes());
   if (encode == null) {
      val var4: MediaType = `$this$text`.contentType();
      if (var4 != null) {
         val var5: Charset = MediaType.charset$default(var4, null, 1, null);
         if (var5 != null) {
            return new java.lang.String(responseBytes, var5);
         }
      }

      val var13: Charset = Charset.forName(EncodingDetect.INSTANCE.getHtmlEncode(responseBytes));
      return new java.lang.String(responseBytes, var13);
   } else {
      val var10: Charset = Charset.forName(encode);
      return new java.lang.String(responseBytes, var10);
   }
}

@JvmSynthetic
fun `text$default`(var0: ResponseBody, var1: java.lang.String, var2: Int, var3: Any): java.lang.String {
   if ((var2 and 1) != 0) {
      var1 = null;
   }

   return text(var0, var1);
}

public fun Builder.addHeaders(headers: Map<String, String>) {
   for (Entry element$iv : headers.entrySet()) {
      `$this$addHeaders`.addHeader(`element$iv`.getKey() as java.lang.String, `element$iv`.getValue() as java.lang.String);
   }
}

public fun Builder.get(url: String, queryMap: Map<String, String>, encoded: Boolean = false) {
   val httpBuilder: okhttp3.HttpUrl.Builder = HttpUrl.Companion.get(url).newBuilder();

   for (Entry element$iv : queryMap.entrySet()) {
      if (encoded) {
         httpBuilder.addEncodedQueryParameter(`element$iv`.getKey() as java.lang.String, `element$iv`.getValue() as java.lang.String);
      } else {
         httpBuilder.addQueryParameter(`element$iv`.getKey() as java.lang.String, `element$iv`.getValue() as java.lang.String);
      }
   }

   `$this$get`.url(httpBuilder.build());
}

@JvmSynthetic
fun `get$default`(var0: Builder, var1: java.lang.String, var2: java.util.Map, var3: Boolean, var4: Int, var5: Any) {
   if ((var4 and 4) != 0) {
      var3 = false;
   }

   get(var0, var1, var2, var3);
}

public fun Builder.postForm(form: Map<String, String>, encoded: Boolean = false) {
   val formBody: okhttp3.FormBody.Builder = new okhttp3.FormBody.Builder(null, 1, null);

   for (Entry element$iv : form.entrySet()) {
      if (encoded) {
         formBody.addEncoded(`element$iv`.getKey() as java.lang.String, `element$iv`.getValue() as java.lang.String);
      } else {
         formBody.add(`element$iv`.getKey() as java.lang.String, `element$iv`.getValue() as java.lang.String);
      }
   }

   `$this$postForm`.post(formBody.build());
}

@JvmSynthetic
fun `postForm$default`(var0: Builder, var1: java.util.Map, var2: Boolean, var3: Int, var4: Any) {
   if ((var3 and 2) != 0) {
      var2 = false;
   }

   postForm(var0, var1, var2);
}

public fun Builder.postMultipart(type: String?, form: Map<String, Any>) {
   val multipartBody: okhttp3.MultipartBody.Builder = new okhttp3.MultipartBody.Builder(null, 1, null);
   if (type != null) {
      multipartBody.setType(MediaType.Companion.get(type));
   }

   for (Entry element$iv : form.entrySet()) {
      val value: Any = var22.getValue();
      if (value is java.util.Map) {
         var file: Any = (value as java.util.Map).get("fileName");
         if (file == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
         }

         val fileName: java.lang.String = file as java.lang.String;
         file = (value as java.util.Map).get("file");
         val var29: Any = (value as java.util.Map).get("contentType");
         val var24: MediaType = if ((var29 as? java.lang.String) == null) null else MediaType.Companion.get(var29 as? java.lang.String);
         val var10000: RequestBody;
         if (file is File) {
            var10000 = RequestBody.Companion.create(file as File, var24);
         } else if (file is ByteArray) {
            var10000 = okhttp3.RequestBody.Companion.create$default(RequestBody.Companion, file as ByteArray, var24, 0, 0, 6, null);
         } else if (file is java.lang.String) {
            var10000 = RequestBody.Companion.create(file as java.lang.String, var24);
         } else {
            val var32: okhttp3.RequestBody.Companion = RequestBody.Companion;
            val var31: java.lang.String = GsonExtensionsKt.getGSON().toJson(file);
            var10000 = var32.create(var31, var24);
         }

         multipartBody.addFormDataPart(var22.getKey() as java.lang.String, fileName, var10000);
      } else {
         multipartBody.addFormDataPart(var22.getKey() as java.lang.String, var22.getValue().toString());
      }
   }

   `$this$postMultipart`.post(multipartBody.build());
}

public fun Builder.postJson(json: String?) {
   if (json != null) {
      `$this$postJson`.post(RequestBody.Companion.create(json, MediaType.Companion.get("application/json; charset=UTF-8")));
   }
}
