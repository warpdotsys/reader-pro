// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import java.util.Iterator;
import com.google.gson.stream.JsonWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.OutputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import kotlin.Result$Companion;
import kotlin.ResultKt;
import com.google.gson.reflect.TypeToken;
import kotlin.Result;
import java.lang.reflect.Type;
import kotlin.jvm.internal.Intrinsics;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import kotlin.Lazy;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\u001a\u0011\u0010\u0006\u001a\u00020\u0007\"\u0006\b\u0000\u0010\b\u0018\u0001H\u0086\b\u001a5\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\b\u0018\u00010\u000b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0086\b\u00f8\u0001\u0000?\u0006\u0002\u0010\u000e\u001a5\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\b\u0018\u00010\u000b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086\b\u00f8\u0001\u0000?\u0006\u0002\u0010\u0011\u001a/\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0086\b\u00f8\u0001\u0000?\u0006\u0002\u0010\u000e\u001a/\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086\b\u00f8\u0001\u0000?\u0006\u0002\u0010\u0011\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018\"\u001b\u0010\u0000\u001a\u00020\u00018FX\u0086\u0084\u0002?\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u0019" }, d2 = { "GSON", "Lcom/google/gson/Gson;", "getGSON", "()Lcom/google/gson/Gson;", "GSON$delegate", "Lkotlin/Lazy;", "genericType", "Ljava/lang/reflect/Type;", "T", "fromJsonArray", "Lkotlin/Result;", "", "inputStream", "Ljava/io/InputStream;", "(Lcom/google/gson/Gson;Ljava/io/InputStream;)Ljava/lang/Object;", "json", "", "(Lcom/google/gson/Gson;Ljava/lang/String;)Ljava/lang/Object;", "fromJsonObject", "writeToOutputStream", "", "out", "Ljava/io/OutputStream;", "any", "", "reader-pro" })
public final class GsonExtensionsKt
{
    @NotNull
    private static final Lazy GSON$delegate;
    
    @NotNull
    public static final Gson getGSON() {
        final Object value = GsonExtensionsKt.GSON$delegate.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-GSON>(...)");
        return (Gson)value;
    }
    
    public static final void writeToOutputStream(@NotNull final Gson $this$writeToOutputStream, @NotNull final OutputStream out, @NotNull final Object any) {
        Intrinsics.checkNotNullParameter((Object)$this$writeToOutputStream, "<this>");
        Intrinsics.checkNotNullParameter((Object)out, "out");
        Intrinsics.checkNotNullParameter(any, "any");
        final JsonWriter writer = new JsonWriter((Writer)new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        if (any instanceof List) {
            writer.beginArray();
            final Iterable $this$forEach$iv = (Iterable)any;
            final int $i$f$forEach = 0;
            for (final Object it : $this$forEach$iv) {
                final Object element$iv = it;
                final int n = 0;
                final Object o = it;
                if (o == null) {
                    continue;
                }
                final Object it2 = o;
                final int n2 = 0;
                $this$writeToOutputStream.toJson(it2, (Type)it2.getClass(), writer);
            }
            writer.endArray();
        }
        else {
            $this$writeToOutputStream.toJson(any, (Type)any.getClass(), writer);
        }
        writer.close();
    }
    
    static {
        GSON$delegate = LazyKt.lazy((Function0)GsonExtensionsKt$GSON.GsonExtensionsKt$GSON$2.INSTANCE);
    }
}
