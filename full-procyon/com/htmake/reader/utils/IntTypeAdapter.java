// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import kotlin.jvm.internal.Intrinsics;
import com.google.gson.JsonDeserializationContext;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0003B\u0005?\u0006\u0002\u0010\u0004J+\u0010\u0005\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016?\u0006\u0002\u0010\fJ+\u0010\r\u001a\u00020\u00072\b\u0010\u000e\u001a\u0004\u0018\u00010\u00022\b\u0010\u000f\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0010H\u0016?\u0006\u0002\u0010\u0011¡§\u0006\u0012" }, d2 = { "Lcom/htmake/reader/utils/IntTypeAdapter;", "Lcom/google/gson/JsonSerializer;", "", "Lcom/google/gson/JsonDeserializer;", "()V", "deserialize", "json", "Lcom/google/gson/JsonElement;", "typeOfT", "Ljava/lang/reflect/Type;", "context", "Lcom/google/gson/JsonDeserializationContext;", "(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Ljava/lang/Integer;", "serialize", "src", "typeOfSrc", "Lcom/google/gson/JsonSerializationContext;", "(Ljava/lang/Integer;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", "reader-pro" })
public final class IntTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer>
{
    @NotNull
    public JsonElement serialize(@Nullable final Integer src, @Nullable final Type typeOfSrc, @Nullable final JsonSerializationContext context) {
        return (JsonElement)new JsonPrimitive(String.valueOf(src));
    }
    
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
