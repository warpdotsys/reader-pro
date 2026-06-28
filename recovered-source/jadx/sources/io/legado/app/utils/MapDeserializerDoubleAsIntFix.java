package io.legado.app.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: GsonExtensions.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/MapDeserializerDoubleAsIntFix.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0005J0\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000e\u001a\u00020\b¨\u0006\u000f"}, d2 = {"Lio/legado/app/utils/MapDeserializerDoubleAsIntFix;", "Lcom/google/gson/JsonDeserializer;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "()V", "deserialize", "jsonElement", "Lcom/google/gson/JsonElement;", "type", "Ljava/lang/reflect/Type;", "jsonDeserializationContext", "Lcom/google/gson/JsonDeserializationContext;", "read", "json", "reader-pro"})
public final class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, ? extends Object>> {
    @Nullable
    /* JADX INFO: renamed from: deserialize, reason: merged with bridge method [inline-methods] */
    public Map<String, Object> m297deserialize(@NotNull JsonElement jsonElement, @NotNull Type type, @NotNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Intrinsics.checkNotNullParameter(jsonElement, "jsonElement");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(jsonDeserializationContext, "jsonDeserializationContext");
        Object obj = read(jsonElement);
        if (obj instanceof Map) {
            return (Map) obj;
        }
        return null;
    }

    @Nullable
    public final Object read(@NotNull JsonElement json) {
        Intrinsics.checkNotNullParameter(json, "json");
        if (json.isJsonArray()) {
            List list = new ArrayList();
            JsonArray<JsonElement> arr = json.getAsJsonArray();
            for (JsonElement anArr : arr) {
                Intrinsics.checkNotNullExpressionValue(anArr, "anArr");
                list.add(read(anArr));
            }
            return list;
        }
        if (json.isJsonObject()) {
            Map map = new LinkedTreeMap();
            JsonObject obj = json.getAsJsonObject();
            Set<Map.Entry> entitySet = obj.entrySet();
            for (Map.Entry entry : entitySet) {
                Intrinsics.checkNotNullExpressionValue(entry, "entitySet");
                String key = (String) entry.getKey();
                JsonElement value = (JsonElement) entry.getValue();
                Intrinsics.checkNotNullExpressionValue(key, "key");
                Intrinsics.checkNotNullExpressionValue(value, "value");
                map.put(key, read(value));
            }
            return map;
        }
        if (json.isJsonPrimitive()) {
            JsonPrimitive prim = json.getAsJsonPrimitive();
            if (prim.isBoolean()) {
                return Boolean.valueOf(prim.getAsBoolean());
            }
            if (prim.isString()) {
                return prim.getAsString();
            }
            if (prim.isNumber()) {
                Number num = prim.getAsNumber();
                Intrinsics.checkNotNullExpressionValue(num, "prim.asNumber");
                if (Math.ceil(num.doubleValue()) == ((double) num.longValue())) {
                    return Long.valueOf(num.longValue());
                }
                return Double.valueOf(num.doubleValue());
            }
            return null;
        }
        return null;
    }
}
