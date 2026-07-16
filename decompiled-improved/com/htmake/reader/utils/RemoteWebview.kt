package com.htmake.reader.utils

import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.HttpHelperKt
import io.legado.app.help.http.OkHttpUtilsKt
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.utils.NetworkUtils
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function1
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import okhttp3.OkHttpClient
import okhttp3.Request.Builder
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object RemoteWebview {
   public final var remoteWebviewApi: String = ""
      internal set

   public fun setRemoteApi(remoteApi: String) {
      remoteWebviewApi = remoteApi;
   }

   public suspend fun getStrResponse(
      url: String? = ...,
      html: String? = ...,
      encode: String? = ...,
      tag: String? = ...,
      headerMap: Map<String, String>? = ...,
      sourceRegex: String? = ...,
      javaScript: String? = ...,
      proxy: String? = ...,
      post: Boolean = ...,
      body: String? = ...,
      userNameSpace: String = ...,
      debugLog: DebugLog? = ...
   ): StrResponse {
      var `$continuation`: Continuation;
      label65: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label65;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.getStrResponse(null, null, null, null, null, null, null, null, false, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var28: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val requestBody: java.lang.CharSequence = this.getRemoteWebviewApi();
            if (requestBody == null || requestBody.length() == 0) {
               throw new Exception("不支持webview");
            }

            val var29: ObjectRef = new ObjectRef();
            var29.element = (T)ExtKt.jsonEncode$default(
               MapsKt.mapOf(
                  new Pair[]{
                     TuplesKt.to("url", url),
                     TuplesKt.to("html", html),
                     TuplesKt.to("headers", headerMap),
                     TuplesKt.to("js_source", javaScript),
                     TuplesKt.to("proxy", proxy),
                     TuplesKt.to("http_method", if (post) "POST" else "GET"),
                     TuplesKt.to("body", body),
                     TuplesKt.to("encode", encode),
                     TuplesKt.to("tag", tag),
                     TuplesKt.to("sourceRegex", sourceRegex)
                  }
               ),
               false,
               2,
               null
            );
            val var31: ObjectRef = new ObjectRef();
            var31.element = (T)Intrinsics.stringPlus(this.getRemoteWebviewApi(), "/render.html");
            var10000 = HttpHelperKt.getProxyClient$default(null, debugLog, 1, null);
            val var10002: Function1 = (new Function1<Builder, Unit>(var31, var29) {
               {
                  super(1);
                  this.$remoteApi = `$remoteApi`;
                  this.$requestBody = `$requestBody`;
               }

               public final void invoke(@NotNull Builder $this$newCallStrResponse) {
                  `$this$newCallStrResponse`.url(this.$remoteApi.element);
                  OkHttpUtilsKt.postJson(`$this$newCallStrResponse`, this.$requestBody.element);
               }
            }) as Function1;
            `$continuation`.L$0 = url;
            `$continuation`.L$1 = userNameSpace;
            `$continuation`.label = 1;
            var10000 = OkHttpUtilsKt.newCallStrResponse((OkHttpClient)var10000, 0, var10002, `$continuation`);
            if (var10000 === var28) {
               return var28;
            }
            break;
         case 1:
            userNameSpace = `$continuation`.L$1 as java.lang.String;
            url = `$continuation`.L$0 as java.lang.String;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var32: StrResponse = var10000 as StrResponse;
      if (url != null) {
         val domain: java.lang.String = NetworkUtils.INSTANCE.getSubDomain(url);
         if (domain.length() > 0 && var32.getRaw().headers("Set-Cookie").size() > 0) {
            val var34: CookieStore = new CookieStore(userNameSpace);

            val `$this$forEach$iv`: java.lang.Iterable;
            for (Object element$iv : $this$forEach$iv) {
               var34.replaceCookie(Intrinsics.stringPlus(domain, "_cookieJar"), `element$iv` as java.lang.String);
            }
         }
      }

      return new StrResponse(if (url == null) "" else url, var32.getBody());
   }
}
