package io.legado.app.help.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Job;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

/* JADX INFO: compiled from: CoroutinesCallAdapterFactory.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CoroutinesCallAdapterFactory.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u000e2\u00020\u0001:\u0003\r\u000e\u000fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J8\u0010\u0003\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0096\u0002¢\u0006\u0002\u0010\f¨\u0006\u0010"}, d2 = {"Lio/legado/app/help/http/CoroutinesCallAdapterFactory;", "Lretrofit2/CallAdapter$Factory;", "()V", "get", "Lretrofit2/CallAdapter;", "returnType", "Ljava/lang/reflect/Type;", "annotations", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "retrofit", "Lretrofit2/Retrofit;", "(Ljava/lang/reflect/Type;[Ljava/lang/annotation/Annotation;Lretrofit2/Retrofit;)Lretrofit2/CallAdapter;", "BodyCallAdapter", "Companion", "ResponseCallAdapter", "reader-pro"})
public final class CoroutinesCallAdapterFactory extends CallAdapter.Factory {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    public /* synthetic */ CoroutinesCallAdapterFactory(DefaultConstructorMarker $constructor_marker) {
        this();
    }

    /* JADX INFO: compiled from: CoroutinesCallAdapterFactory.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CoroutinesCallAdapterFactory$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004¨\u0006\u0005"}, d2 = {"Lio/legado/app/help/http/CoroutinesCallAdapterFactory$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "create", "Lio/legado/app/help/http/CoroutinesCallAdapterFactory;", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final CoroutinesCallAdapterFactory create() {
            return new CoroutinesCallAdapterFactory(null);
        }
    }

    private CoroutinesCallAdapterFactory() {
    }

    @Nullable
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        Intrinsics.checkNotNullParameter(returnType, "returnType");
        Intrinsics.checkNotNullParameter(annotations, "annotations");
        Intrinsics.checkNotNullParameter(retrofit, "retrofit");
        if (!Intrinsics.areEqual(Deferred.class, CallAdapter.Factory.getRawType(returnType))) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>".toString());
        }
        Type responseType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);
        Class rawDeferredType = CallAdapter.Factory.getRawType(responseType);
        if (Intrinsics.areEqual(rawDeferredType, Response.class)) {
            if (!(responseType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized as Response<Foo> or Response<out Foo>".toString());
            }
            Type parameterUpperBound = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) responseType);
            Intrinsics.checkNotNullExpressionValue(parameterUpperBound, "getParameterUpperBound(\n                    0,\n                    responseType\n                )");
            return new ResponseCallAdapter(parameterUpperBound);
        }
        Intrinsics.checkNotNullExpressionValue(responseType, "responseType");
        return new BodyCallAdapter(responseType);
    }

    /* JADX INFO: compiled from: CoroutinesCallAdapterFactory.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CoroutinesCallAdapterFactory$BodyCallAdapter.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u0014\u0012\u0004\u0012\u0002H\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lio/legado/app/help/http/CoroutinesCallAdapterFactory$BodyCallAdapter;", "T", "Lretrofit2/CallAdapter;", "Lkotlinx/coroutines/Deferred;", "responseType", "Ljava/lang/reflect/Type;", "(Ljava/lang/reflect/Type;)V", "adapt", "call", "Lretrofit2/Call;", "reader-pro"})
    private static final class BodyCallAdapter<T> implements CallAdapter<T, Deferred<? extends T>> {

        @NotNull
        private final Type responseType;

        public BodyCallAdapter(@NotNull Type responseType) {
            Intrinsics.checkNotNullParameter(responseType, "responseType");
            this.responseType = responseType;
        }

        @NotNull
        public Type responseType() {
            return this.responseType;
        }

        @NotNull
        /* JADX INFO: renamed from: adapt, reason: merged with bridge method [inline-methods] */
        public Deferred<T> m178adapt(@NotNull Call<T> call) {
            Intrinsics.checkNotNullParameter(call, "call");
            Deferred<T> deferredCompletableDeferred$default = CompletableDeferredKt.CompletableDeferred$default((Job) null, 1, (Object) null);
            deferredCompletableDeferred$default.invokeOnCompletion(new CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$1(deferredCompletableDeferred$default, call));
            call.enqueue(new CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$2(deferredCompletableDeferred$default));
            return deferredCompletableDeferred$default;
        }
    }

    /* JADX INFO: compiled from: CoroutinesCallAdapterFactory.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CoroutinesCallAdapterFactory$ResponseCallAdapter.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u001a\u0012\u0004\u0012\u0002H\u0001\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00040\u00030\u0002B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\"\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00040\u00032\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lio/legado/app/help/http/CoroutinesCallAdapterFactory$ResponseCallAdapter;", "T", "Lretrofit2/CallAdapter;", "Lkotlinx/coroutines/Deferred;", "Lretrofit2/Response;", "responseType", "Ljava/lang/reflect/Type;", "(Ljava/lang/reflect/Type;)V", "adapt", "call", "Lretrofit2/Call;", "reader-pro"})
    private static final class ResponseCallAdapter<T> implements CallAdapter<T, Deferred<? extends Response<T>>> {

        @NotNull
        private final Type responseType;

        public ResponseCallAdapter(@NotNull Type responseType) {
            Intrinsics.checkNotNullParameter(responseType, "responseType");
            this.responseType = responseType;
        }

        @NotNull
        public Type responseType() {
            return this.responseType;
        }

        @NotNull
        /* JADX INFO: renamed from: adapt, reason: merged with bridge method [inline-methods] */
        public Deferred<Response<T>> m179adapt(@NotNull Call<T> call) {
            Intrinsics.checkNotNullParameter(call, "call");
            Deferred<Response<T>> deferredCompletableDeferred$default = CompletableDeferredKt.CompletableDeferred$default((Job) null, 1, (Object) null);
            deferredCompletableDeferred$default.invokeOnCompletion(new CoroutinesCallAdapterFactory$ResponseCallAdapter$adapt$1(deferredCompletableDeferred$default, call));
            call.enqueue(new CoroutinesCallAdapterFactory$ResponseCallAdapter$adapt$2(deferredCompletableDeferred$default));
            return deferredCompletableDeferred$default;
        }
    }
}
