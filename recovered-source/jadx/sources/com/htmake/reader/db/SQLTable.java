package com.htmake.reader.db;

import com.htmake.reader.utils.ExtKt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: SQLTable.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/db/SQLTable.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\nH\u0016J7\u0010\r\u001a\u0004\u0018\u00018\u0000\"\b\b\u0001\u0010\u000e*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u0002H\u000e2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00000\u0013H\u0016¢\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\bH\u0016JQ\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00028\u00002 \u0010\u0018\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u00192\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\u001aH\u0016¢\u0006\u0002\u0010\u001bJW\u0010\u001c\u001a\u00020\b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u001d2 \u0010\u0018\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u00192\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\u001aH\u0016¢\u0006\u0002\u0010\u001e¨\u0006\u001f"}, d2 = {"Lcom/htmake/reader/db/SQLTable;", "T", "Lcom/htmake/reader/db/DB;", "userNameSpace", PackageDocumentBase.PREFIX_OPF, "name", "(Ljava/lang/String;Ljava/lang/String;)V", "delete", PackageDocumentBase.PREFIX_OPF, "checker", "Lkotlin/Function1;", "Lio/vertx/core/json/JsonObject;", PackageDocumentBase.PREFIX_OPF, "findBy", "P", PackageDocumentBase.PREFIX_OPF, "field", "value", "clazz", "Ljava/lang/Class;", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;", "readAll", "Lio/vertx/core/json/JsonArray;", "save", "onCheckEnd", "Lkotlin/Function3;", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;)V", "saveMulti", PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;)V", "reader-pro"})
public final class SQLTable<T> extends DB<T> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SQLTable(@NotNull String userNameSpace, @NotNull String name) {
        super(userNameSpace, name);
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        Intrinsics.checkNotNullParameter(name, "name");
    }

    @Override // com.htmake.reader.db.DB
    @NotNull
    public JsonArray readAll() {
        JsonArray dataList = ExtKt.asJsonArray(ExtKt.getStorage$default(new String[]{"data", getUserNameSpace(), getName()}, null, 2, null));
        if (dataList == null) {
            dataList = new JsonArray();
        }
        setCachedValue(dataList);
        return dataList;
    }

    @Override // com.htmake.reader.db.DB
    @Nullable
    public <P> T findBy(@NotNull String field, @NotNull P value, @NotNull Class<T> clazz) {
        Intrinsics.checkNotNullParameter(field, "field");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        JsonArray all = readAll();
        int i = 0;
        int size = all.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                if (value.equals(all.getJsonObject(i2).getValue(field))) {
                    return (T) all.getJsonObject(i2).mapTo(clazz);
                }
            } while (i < size);
            return null;
        }
        return null;
    }

    @Override // com.htmake.reader.db.DB
    public void save(T value, @Nullable Function3<? super T, ? super Boolean, ? super JsonArray, Unit> onCheckEnd, @NotNull Function2<? super JsonObject, ? super T, Boolean> checker) {
        Intrinsics.checkNotNullParameter(checker, "checker");
        JsonArray dataList = readAll();
        int existIndex = -1;
        int i = 0;
        int size = dataList.size();
        if (0 < size) {
            while (true) {
                int i2 = i;
                i++;
                JsonObject jsonObject = dataList.getJsonObject(i2);
                Intrinsics.checkNotNullExpressionValue(jsonObject, "dataList.getJsonObject(i)");
                if (!((Boolean) checker.invoke(jsonObject, value)).booleanValue()) {
                    if (i >= size) {
                        break;
                    }
                } else {
                    existIndex = i2;
                    break;
                }
            }
        }
        if (onCheckEnd != null) {
            onCheckEnd.invoke(value, Boolean.valueOf(existIndex >= 0), dataList);
        }
        if (existIndex >= 0) {
            List list = dataList.getList();
            list.set(existIndex, JsonObject.mapFrom(value));
            dataList = new JsonArray(list);
        } else {
            dataList.add(JsonObject.mapFrom(value));
        }
        setCachedValue(dataList);
        save();
    }

    @Override // com.htmake.reader.db.DB
    public void saveMulti(@NotNull T[] value, @Nullable Function3<? super T, ? super Boolean, ? super JsonArray, Unit> onCheckEnd, @NotNull Function2<? super JsonObject, ? super T, Boolean> checker) {
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(checker, "checker");
        JsonArray dataList = readAll();
        int existIndex = -1;
        int i = 0;
        int length = value.length;
        while (i < length) {
            T t = value[i];
            i++;
            int i2 = 0;
            int size = dataList.size();
            if (0 < size) {
                while (true) {
                    int i3 = i2;
                    i2++;
                    JsonObject jsonObject = dataList.getJsonObject(i3);
                    Intrinsics.checkNotNullExpressionValue(jsonObject, "dataList.getJsonObject(i)");
                    if (!((Boolean) checker.invoke(jsonObject, t)).booleanValue()) {
                        if (i2 >= size) {
                            break;
                        }
                    } else {
                        existIndex = i3;
                        break;
                    }
                }
            }
            if (onCheckEnd != null) {
                onCheckEnd.invoke(t, Boolean.valueOf(existIndex >= 0), dataList);
            }
            if (existIndex >= 0) {
                List list = dataList.getList();
                list.set(existIndex, JsonObject.mapFrom(t));
                dataList = new JsonArray(list);
            } else {
                dataList.add(JsonObject.mapFrom(t));
            }
        }
        setCachedValue(dataList);
        save();
    }

    @Override // com.htmake.reader.db.DB
    public void delete(@NotNull Function1<? super JsonObject, Boolean> checker) {
        Intrinsics.checkNotNullParameter(checker, "checker");
        JsonArray all = readAll();
        List removeIndexList = new ArrayList();
        int i = 0;
        int size = all.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                JsonObject jsonObject = all.getJsonObject(i2);
                Intrinsics.checkNotNullExpressionValue(jsonObject, "dataList.getJsonObject(i)");
                if (((Boolean) checker.invoke(jsonObject)).booleanValue()) {
                    removeIndexList.add(Integer.valueOf(i2));
                }
            } while (i < size);
        }
        if (removeIndexList.size() > 0) {
            List $this$forEach$iv = removeIndexList;
            for (Object element$iv : $this$forEach$iv) {
                int it = ((Number) element$iv).intValue();
                all.remove(it);
            }
        }
        setCachedValue(all);
        save();
    }

    @Override // com.htmake.reader.db.DB
    public void save() {
        ExtKt.saveStorage$default(new String[]{"data", getUserNameSpace(), getName()}, getCachedValue(), false, null, 12, null);
    }
}
