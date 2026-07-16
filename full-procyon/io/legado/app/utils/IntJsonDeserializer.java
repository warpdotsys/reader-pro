// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import com.google.gson.JsonPrimitive;
import kotlin.jvm.internal.Intrinsics;
import com.google.gson.JsonDeserializationContext;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonElement;
import kotlin.Metadata;
import com.google.gson.JsonDeserializer;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\u0005?\u0006\u0002\u0010\u0003J+\u0010\u0004\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016?\u0006\u0002\u0010\u000b¡§\u0006\f" }, d2 = { "Lio/legado/app/utils/IntJsonDeserializer;", "Lcom/google/gson/JsonDeserializer;", "", "()V", "deserialize", "json", "Lcom/google/gson/JsonElement;", "typeOfT", "Ljava/lang/reflect/Type;", "context", "Lcom/google/gson/JsonDeserializationContext;", "(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Ljava/lang/Integer;", "reader-pro" })
public final class IntJsonDeserializer implements JsonDeserializer<Integer>
{
    @Nullable
    public Integer deserialize(@NotNull final JsonElement json, @Nullable final Type typeOfT, @Nullable final JsonDeserializationContext context) {
        Intrinsics.checkNotNullParameter((Object)json, "json");
        Integer n;
        if (json.isJsonPrimitive()) {
            final JsonPrimitive prim = json.getAsJsonPrimitive();
            n = (prim.isNumber() ? Integer.valueOf(prim.getAsNumber().intValue()) : null);
        }
        else {
            n = null;
        }
        return n;
    }
}
