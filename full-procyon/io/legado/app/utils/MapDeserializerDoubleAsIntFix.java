// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import com.google.gson.JsonPrimitive;
import java.util.Set;
import com.google.gson.JsonObject;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonParseException;
import kotlin.jvm.internal.Intrinsics;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonElement;
import kotlin.Metadata;
import java.util.Map;
import com.google.gson.JsonDeserializer;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00020\u0001B\u0005?\u0006\u0002\u0010\u0005J0\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000e\u001a\u00020\b¡§\u0006\u000f" }, d2 = { "Lio/legado/app/utils/MapDeserializerDoubleAsIntFix;", "Lcom/google/gson/JsonDeserializer;", "", "", "", "()V", "deserialize", "jsonElement", "Lcom/google/gson/JsonElement;", "type", "Ljava/lang/reflect/Type;", "jsonDeserializationContext", "Lcom/google/gson/JsonDeserializationContext;", "read", "json", "reader-pro" })
public final class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, ?>>
{
    @Nullable
    public Map<String, Object> deserialize(@NotNull final JsonElement jsonElement, @NotNull final Type type, @NotNull final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Intrinsics.checkNotNullParameter((Object)jsonElement, "jsonElement");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        Intrinsics.checkNotNullParameter((Object)jsonDeserializationContext, "jsonDeserializationContext");
        final Object read = this.read(jsonElement);
        return (read instanceof Map) ? ((Map<String, Object>)read) : null;
    }
    
    @Nullable
    public final Object read(@NotNull final JsonElement json) {
        Intrinsics.checkNotNullParameter((Object)json, "json");
        if (json.isJsonArray()) {
            final List list = new ArrayList();
            final JsonArray arr = json.getAsJsonArray();
            for (final JsonElement anArr : arr) {
                final List list2 = list;
                Intrinsics.checkNotNullExpressionValue((Object)anArr, "anArr");
                list2.add(this.read(anArr));
            }
            return list;
        }
        if (json.isJsonObject()) {
            final Map map = (Map)new LinkedTreeMap();
            final JsonObject obj = json.getAsJsonObject();
            final Set entitySet = obj.entrySet();
            for (final Map.Entry<String, JsonElement> entry : entitySet) {
                Intrinsics.checkNotNullExpressionValue((Object)entry, "entitySet");
                final String key = entry.getKey();
                final JsonElement value = entry.getValue();
                final Map map2 = map;
                Intrinsics.checkNotNullExpressionValue((Object)key, "key");
                final String s = key;
                Intrinsics.checkNotNullExpressionValue((Object)value, "value");
                map2.put(s, this.read(value));
            }
            return map;
        }
        if (json.isJsonPrimitive()) {
            final JsonPrimitive prim = json.getAsJsonPrimitive();
            if (prim.isBoolean()) {
                return prim.getAsBoolean();
            }
            if (prim.isString()) {
                return prim.getAsString();
            }
            if (prim.isNumber()) {
                final Number asNumber = prim.getAsNumber();
                Intrinsics.checkNotNullExpressionValue((Object)asNumber, "prim.asNumber");
                final Number num = asNumber;
                return (Math.ceil(num.doubleValue()) == num.longValue()) ? Long.valueOf(num.longValue()) : Double.valueOf(num.doubleValue());
            }
        }
        return null;
    }
}
