// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import kotlinx.coroutines.CompletableDeferred;
import retrofit2.Callback;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.CompletableDeferredKt;
import retrofit2.Call;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;
import java.lang.reflect.ParameterizedType;
import kotlinx.coroutines.Deferred;
import kotlin.jvm.internal.Intrinsics;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import retrofit2.CallAdapter$Factory;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u000e2\u00020\u0001:\u0003\r\u000e\u000fB\u0007\b\u0002?\u0006\u0002\u0010\u0002J8\u0010\u0003\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\u0096\u0002?\u0006\u0002\u0010\f：\u0006\u0010" }, d2 = { "Lio/legado/app/help/http/CoroutinesCallAdapterFactory;", "Lretrofit2/CallAdapter$Factory;", "()V", "get", "Lretrofit2/CallAdapter;", "returnType", "Ljava/lang/reflect/Type;", "annotations", "", "", "retrofit", "Lretrofit2/Retrofit;", "(Ljava/lang/reflect/Type;[Ljava/lang/annotation/Annotation;Lretrofit2/Retrofit;)Lretrofit2/CallAdapter;", "BodyCallAdapter", "Companion", "ResponseCallAdapter", "reader-pro" })
public final class CoroutinesCallAdapterFactory extends CallAdapter$Factory
{
    @NotNull
    public static final Companion Companion;
    
    private CoroutinesCallAdapterFactory() {
    }
    
    @Nullable
    public CallAdapter<?, ?> get(@NotNull final Type returnType, @NotNull final Annotation[] annotations, @NotNull final Retrofit retrofit) {
        Intrinsics.checkNotNullParameter((Object)returnType, "returnType");
        Intrinsics.checkNotNullParameter((Object)annotations, "annotations");
        Intrinsics.checkNotNullParameter((Object)retrofit, "retrofit");
        if (!Intrinsics.areEqual((Object)Deferred.class, (Object)CallAdapter$Factory.getRawType(returnType))) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            final int n = 0;
            throw new IllegalStateException("Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>".toString());
        }
        final Type responseType = CallAdapter$Factory.getParameterUpperBound(0, (ParameterizedType)returnType);
        final Class rawDeferredType = CallAdapter$Factory.getRawType(responseType);
        CallAdapter callAdapter;
        if (Intrinsics.areEqual((Object)rawDeferredType, (Object)Response.class)) {
            if (!(responseType instanceof ParameterizedType)) {
                final int n2 = 0;
                throw new IllegalStateException("Response must be parameterized as Response<Foo> or Response<out Foo>".toString());
            }
            final Type parameterUpperBound = CallAdapter$Factory.getParameterUpperBound(0, (ParameterizedType)responseType);
            Intrinsics.checkNotNullExpressionValue((Object)parameterUpperBound, "getParameterUpperBound(\n                    0,\n                    responseType\n                )");
            callAdapter = (CallAdapter)new ResponseCallAdapter(parameterUpperBound);
        }
        else {
            Intrinsics.checkNotNullExpressionValue((Object)responseType, "responseType");
            callAdapter = (CallAdapter)new BodyCallAdapter(responseType);
        }
        return (CallAdapter<?, ?>)callAdapter;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004：\u0006\u0005" }, d2 = { "Lio/legado/app/help/http/CoroutinesCallAdapterFactory$Companion;", "", "()V", "create", "Lio/legado/app/help/http/CoroutinesCallAdapterFactory;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final CoroutinesCallAdapterFactory create() {
            return new CoroutinesCallAdapterFactory(null);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u0014\u0012\u0004\u0012\u0002H\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tH\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004?\u0006\u0002\n\u0000：\u0006\n" }, d2 = { "Lio/legado/app/help/http/CoroutinesCallAdapterFactory$BodyCallAdapter;", "T", "Lretrofit2/CallAdapter;", "Lkotlinx/coroutines/Deferred;", "responseType", "Ljava/lang/reflect/Type;", "(Ljava/lang/reflect/Type;)V", "adapt", "call", "Lretrofit2/Call;", "reader-pro" })
    private static final class BodyCallAdapter<T> implements CallAdapter<T, Deferred<? extends T>>
    {
        @NotNull
        private final Type responseType;
        
        public BodyCallAdapter(@NotNull final Type responseType) {
            Intrinsics.checkNotNullParameter((Object)responseType, "responseType");
            this.responseType = responseType;
        }
        
        @NotNull
        public Type responseType() {
            return this.responseType;
        }
        
        @NotNull
        public Deferred<T> adapt(@NotNull final Call<T> call) {
            Intrinsics.checkNotNullParameter((Object)call, "call");
            final CompletableDeferred deferred = CompletableDeferredKt.CompletableDeferred$default((Job)null, 1, (Object)null);
            deferred.invokeOnCompletion((Function1)new CoroutinesCallAdapterFactory$BodyCallAdapter$adapt.CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$1(deferred, (Call)call));
            call.enqueue((Callback)new CoroutinesCallAdapterFactory$BodyCallAdapter$adapt.CoroutinesCallAdapterFactory$BodyCallAdapter$adapt$2(deferred));
            return (Deferred<T>)deferred;
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u001a\u0012\u0004\u0012\u0002H\u0001\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00040\u00030\u0002B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006?\u0006\u0002\u0010\u0007J\"\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00040\u00032\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0016J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004?\u0006\u0002\n\u0000：\u0006\u000b" }, d2 = { "Lio/legado/app/help/http/CoroutinesCallAdapterFactory$ResponseCallAdapter;", "T", "Lretrofit2/CallAdapter;", "Lkotlinx/coroutines/Deferred;", "Lretrofit2/Response;", "responseType", "Ljava/lang/reflect/Type;", "(Ljava/lang/reflect/Type;)V", "adapt", "call", "Lretrofit2/Call;", "reader-pro" })
    private static final class ResponseCallAdapter<T> implements CallAdapter<T, Deferred<? extends Response<T>>>
    {
        @NotNull
        private final Type responseType;
        
        public ResponseCallAdapter(@NotNull final Type responseType) {
            Intrinsics.checkNotNullParameter((Object)responseType, "responseType");
            this.responseType = responseType;
        }
        
        @NotNull
        public Type responseType() {
            return this.responseType;
        }
        
        @NotNull
        public Deferred<Response<T>> adapt(@NotNull final Call<T> call) {
            Intrinsics.checkNotNullParameter((Object)call, "call");
            final CompletableDeferred deferred = CompletableDeferredKt.CompletableDeferred$default((Job)null, 1, (Object)null);
            deferred.invokeOnCompletion((Function1)new CoroutinesCallAdapterFactory$ResponseCallAdapter$adapt.CoroutinesCallAdapterFactory$ResponseCallAdapter$adapt$1(deferred, (Call)call));
            call.enqueue((Callback)new CoroutinesCallAdapterFactory$ResponseCallAdapter$adapt.CoroutinesCallAdapterFactory$ResponseCallAdapter$adapt$2(deferred));
            return (Deferred<Response<T>>)deferred;
        }
    }
}
