// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.db;

import java.util.Iterator;
import java.util.ArrayList;
import kotlin.jvm.functions.Function1;
import java.util.List;
import io.vertx.core.json.JsonObject;
import kotlin.jvm.functions.Function2;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import org.jetbrains.annotations.Nullable;
import com.htmake.reader.utils.ExtKt;
import io.vertx.core.json.JsonArray;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000V\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004?\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\nH\u0016J7\u0010\r\u001a\u0004\u0018\u00018\u0000\"\b\b\u0001\u0010\u000e*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u0002H\u000e2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00000\u0013H\u0016?\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\bH\u0016JQ\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00028\u00002 \u0010\u0018\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u00192\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\u001aH\u0016?\u0006\u0002\u0010\u001bJW\u0010\u001c\u001a\u00020\b2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u001d2 \u0010\u0018\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u00192\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\u001aH\u0016?\u0006\u0002\u0010\u001e¡§\u0006\u001f" }, d2 = { "Lcom/htmake/reader/db/SQLTable;", "T", "Lcom/htmake/reader/db/DB;", "userNameSpace", "", "name", "(Ljava/lang/String;Ljava/lang/String;)V", "delete", "", "checker", "Lkotlin/Function1;", "Lio/vertx/core/json/JsonObject;", "", "findBy", "P", "", "field", "value", "clazz", "Ljava/lang/Class;", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;", "readAll", "Lio/vertx/core/json/JsonArray;", "save", "onCheckEnd", "Lkotlin/Function3;", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;)V", "saveMulti", "", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;)V", "reader-pro" })
public final class SQLTable<T> extends DB<T>
{
    public SQLTable(@NotNull final String userNameSpace, @NotNull final String name) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        Intrinsics.checkNotNullParameter((Object)name, "name");
        super(userNameSpace, name);
    }
    
    @NotNull
    @Override
    public JsonArray readAll() {
        JsonArray dataList = ExtKt.asJsonArray(ExtKt.getStorage$default(new String[] { "data", this.getUserNameSpace(), this.getName() }, null, 2, null));
        if (dataList == null) {
            dataList = new JsonArray();
        }
        this.setCachedValue(dataList);
        return dataList;
    }
    
    @Nullable
    @Override
    public <P> T findBy(@NotNull final String field, @NotNull final P value, @NotNull final Class<T> clazz) {
        Intrinsics.checkNotNullParameter((Object)field, "field");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        Intrinsics.checkNotNullParameter((Object)clazz, "clazz");
        final JsonArray dataList = this.readAll();
        int j = 0;
        final int size = dataList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Object objValue = dataList.getJsonObject(i).getValue(field);
                if (value.equals(objValue)) {
                    return (T)dataList.getJsonObject(i).mapTo((Class)clazz);
                }
            } while (j < size);
        }
        return null;
    }
    
    @Override
    public void save(final T value, @Nullable final Function3<? super T, ? super Boolean, ? super JsonArray, Unit> onCheckEnd, @NotNull final Function2<? super JsonObject, ? super T, Boolean> checker) {
        Intrinsics.checkNotNullParameter((Object)checker, "checker");
        JsonArray dataList = this.readAll();
        int existIndex = -1;
        int j = 0;
        final int size = dataList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final JsonObject jsonObject = dataList.getJsonObject(i);
                Intrinsics.checkNotNullExpressionValue((Object)jsonObject, "dataList.getJsonObject(i)");
                if (checker.invoke((Object)jsonObject, (Object)value)) {
                    existIndex = i;
                    break;
                }
            } while (j < size);
        }
        if (onCheckEnd != null) {
            onCheckEnd.invoke((Object)value, (Object)(existIndex >= 0), (Object)dataList);
        }
        if (existIndex >= 0) {
            final List list = dataList.getList();
            list.set(existIndex, JsonObject.mapFrom((Object)value));
            dataList = new JsonArray(list);
        }
        else {
            dataList.add(JsonObject.mapFrom((Object)value));
        }
        this.setCachedValue(dataList);
        this.save();
    }
    
    @Override
    public void saveMulti(@NotNull final T[] value, @Nullable final Function3<? super T, ? super Boolean, ? super JsonArray, Unit> onCheckEnd, @NotNull final Function2<? super JsonObject, ? super T, Boolean> checker) {
        Intrinsics.checkNotNullParameter((Object)value, "value");
        Intrinsics.checkNotNullParameter((Object)checker, "checker");
        JsonArray dataList = this.readAll();
        int existIndex = -1;
        int k = 0;
        while (k < value.length) {
            final Object j = value[k];
            ++k;
            int l = 0;
            final int size = dataList.size();
            if (l < size) {
                do {
                    final int i = l;
                    ++l;
                    final JsonObject jsonObject = dataList.getJsonObject(i);
                    Intrinsics.checkNotNullExpressionValue((Object)jsonObject, "dataList.getJsonObject(i)");
                    if (checker.invoke((Object)jsonObject, j)) {
                        existIndex = i;
                        break;
                    }
                } while (l < size);
            }
            if (onCheckEnd != null) {
                onCheckEnd.invoke(j, (Object)(existIndex >= 0), (Object)dataList);
            }
            if (existIndex >= 0) {
                final List list = dataList.getList();
                list.set(existIndex, JsonObject.mapFrom(j));
                dataList = new JsonArray(list);
            }
            else {
                dataList.add(JsonObject.mapFrom(j));
            }
        }
        this.setCachedValue(dataList);
        this.save();
    }
    
    @Override
    public void delete(@NotNull final Function1<? super JsonObject, Boolean> checker) {
        Intrinsics.checkNotNullParameter((Object)checker, "checker");
        Object dataList = null;
        dataList = this.readAll();
        final List removeIndexList = new ArrayList();
        int j = 0;
        final int size = ((JsonArray)dataList).size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final JsonObject jsonObject = ((JsonArray)dataList).getJsonObject(i);
                Intrinsics.checkNotNullExpressionValue((Object)jsonObject, "dataList.getJsonObject(i)");
                if (checker.invoke((Object)jsonObject)) {
                    removeIndexList.add(i);
                }
            } while (j < size);
        }
        if (removeIndexList.size() > 0) {
            final Iterable $this$forEach$iv = removeIndexList;
            final int $i$f$forEach = 0;
            for (final Object element$iv : $this$forEach$iv) {
                final int it = ((Number)element$iv).intValue();
                final int n = 0;
                ((JsonArray)dataList).remove(it);
            }
        }
        this.setCachedValue((JsonArray)dataList);
        this.save();
    }
    
    @Override
    public void save() {
        ExtKt.saveStorage$default(new String[] { "data", this.getUserNameSpace(), this.getName() }, this.getCachedValue(), false, null, 12, null);
    }
}
