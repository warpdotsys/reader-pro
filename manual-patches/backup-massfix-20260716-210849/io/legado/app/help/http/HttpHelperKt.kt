package io.legado.app.help.http

import io.legado.app.model.DebugLog
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Proxy.Type
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.internal.Ref.ObjectRef
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public final val okHttpClient: OkHttpClient by LazyKt.lazy(<unrepresentable>.INSTANCE)
   public final get() {
      return okHttpClient$delegate.getValue() as OkHttpClient;
   }


private final val proxyClientCache: ConcurrentHashMap<String, OkHttpClient> by LazyKt.lazy(<unrepresentable>.INSTANCE)
   private final get() {
      return proxyClientCache$delegate.getValue() as ConcurrentHashMap<java.lang.String, OkHttpClient>;
   }


public fun getProxyClient(proxy: String? = null, debugLog: DebugLog? = null): OkHttpClient {
   if (proxy == null || StringsKt.isBlank(proxy)) {
      if (debugLog == null) {
         return getOkHttpClient();
      } else {
         val var16: Builder = getOkHttpClient().newBuilder();
         val var18: HttpLoggingInterceptor = new HttpLoggingInterceptor(debugLog);
         var18.setLevel(Level.BODY);
         var16.addNetworkInterceptor(var18);
         return var16.build();
      }
   } else {
      if (debugLog == null) {
         val var14: OkHttpClient = getProxyClientCache().get(proxy);
         if (var14 != null) {
            return var14;
         }
      }

      val var19: MatchResult = SequencesKt.first(Regex.findAll$default(new Regex("(http|socks4|socks5)://(.*):(\\d{2,5})(@.*@.*)?"), proxy, 0, 2, null));
      val username: ObjectRef = new ObjectRef();
      username.element = (T)"";
      val password: ObjectRef = new ObjectRef();
      password.element = (T)"";
      val type: java.lang.String = if (var19.getGroupValues().get(1) == "http") "http" else "socks";
      val host: java.lang.String = var19.getGroupValues().get(2);
      val port: Int = Integer.parseInt(var19.getGroupValues().get(3));
      if (!(var19.getGroupValues().get(4) == "")) {
         username.element = (T)StringsKt.split$default(var19.getGroupValues().get(4), new java.lang.String[]{"@"}, false, 0, 6, null).get(1);
         password.element = (T)StringsKt.split$default(var19.getGroupValues().get(4), new java.lang.String[]{"@"}, false, 0, 6, null).get(2);
      }

      if (!(type == "direct") && !(host == "")) {
         val var25: Builder = getOkHttpClient().newBuilder();
         if (type == "http") {
            var25.proxy(new Proxy(Type.HTTP, new InetSocketAddress(host, port)));
         } else {
            var25.proxy(new Proxy(Type.SOCKS, new InetSocketAddress(host, port)));
         }

         if (!(username.element == "") && !(password.element == "")) {
            var25.proxyAuthenticator(
               new Authenticator(username, password) {
                  {
                     this.$username = `$username`;
                     this.$password = `$password`;
                  }

                  @NotNull
                  @Override
                  public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
                     return response.request()
                        .newBuilder()
                        .header("Proxy-Authorization", Credentials.basic$default(this.$username.element, this.$password.element, null, 4, null))
                        .build();
                  }
               }
            );
         }

         if (debugLog != null) {
            val var28: HttpLoggingInterceptor = new HttpLoggingInterceptor(debugLog);
            var28.setLevel(Level.BODY);
            var25.addNetworkInterceptor(var28);
            return var25.build();
         } else {
            val var27: OkHttpClient = var25.build();
            getProxyClientCache().put(proxy, var27);
            return var27;
         }
      } else {
         return getOkHttpClient();
      }
   }
}

@JvmSynthetic
fun `getProxyClient$default`(var0: java.lang.String, var1: DebugLog, var2: Int, var3: Any): OkHttpClient {
   if ((var2 and 1) != 0) {
      var0 = null;
   }

   if ((var2 and 2) != 0) {
      var1 = null;
   }

   return getProxyClient(var0, var1);
}
