package io.legado.app.help.http;

import java.io.IOException;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: HttpHelper.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/HttpHelperKt$getProxyClient$proxyAuthenticator$1.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"io/legado/app/help/http/HttpHelperKt$getProxyClient$proxyAuthenticator$1", "Lokhttp3/Authenticator;", "authenticate", "Lokhttp3/Request;", "route", "Lokhttp3/Route;", "response", "Lokhttp3/Response;", "reader-pro"})
public final class HttpHelperKt$getProxyClient$proxyAuthenticator$1 implements Authenticator {
    final /* synthetic */ Ref.ObjectRef<String> $username;
    final /* synthetic */ Ref.ObjectRef<String> $password;

    HttpHelperKt$getProxyClient$proxyAuthenticator$1(Ref.ObjectRef<String> $username, Ref.ObjectRef<String> $password) {
        this.$username = $username;
        this.$password = $password;
    }

    @NotNull
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        Intrinsics.checkNotNullParameter(response, "response");
        String credential = Credentials.basic$default((String) this.$username.element, (String) this.$password.element, (Charset) null, 4, (Object) null);
        return response.request().newBuilder().header("Proxy-Authorization", credential).build();
    }
}
