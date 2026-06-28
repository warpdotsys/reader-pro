package io.legado.app.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: GsonExtensions.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/GsonExtensionsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\u001a\u0011\u0010\u0006\u001a\u00020\u0007\"\u0006\b\u0000\u0010\b\u0018\u0001H\u0086\b\u001a5\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\b\u0018\u00010\u000b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u000e\u001a5\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\b\u0018\u00010\u000b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a/\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u000e\u001a/\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\b0\n\"\u0006\b\u0000\u0010\b\u0018\u0001*\u00020\u00012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018\"\u001b\u0010\u0000\u001a\u00020\u00018FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"GSON", "Lcom/google/gson/Gson;", "getGSON", "()Lcom/google/gson/Gson;", "GSON$delegate", "Lkotlin/Lazy;", "genericType", "Ljava/lang/reflect/Type;", "T", "fromJsonArray", "Lkotlin/Result;", PackageDocumentBase.PREFIX_OPF, "inputStream", "Ljava/io/InputStream;", "(Lcom/google/gson/Gson;Ljava/io/InputStream;)Ljava/lang/Object;", "json", PackageDocumentBase.PREFIX_OPF, "(Lcom/google/gson/Gson;Ljava/lang/String;)Ljava/lang/Object;", "fromJsonObject", "writeToOutputStream", PackageDocumentBase.PREFIX_OPF, "out", "Ljava/io/OutputStream;", "any", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public final class GsonExtensionsKt {

    @NotNull
    private static final Lazy GSON$delegate = LazyKt.lazy(GsonExtensionsKt$GSON$2.INSTANCE);

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: io.legado.app.utils.GsonExtensionsKt$genericType$1, reason: invalid class name */
    /* JADX INFO: compiled from: GsonExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/GsonExtensionsKt$genericType$1.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001¨\u0006\u0002"}, d2 = {"io/legado/app/utils/GsonExtensionsKt$genericType$1", "Lcom/google/gson/reflect/TypeToken;", "reader-pro"})
    public static final class AnonymousClass1<T> extends TypeToken<T> {
    }

    @NotNull
    public static final Gson getGSON() {
        Object value = GSON$delegate.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-GSON>(...)");
        return (Gson) value;
    }

    public static final /* synthetic */ <T> Type genericType() {
        Intrinsics.needClassReification();
        Type type = new AnonymousClass1().getType();
        Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
        return type;
    }

    public static final /* synthetic */ <T> Object fromJsonObject(Gson $this$fromJsonObject, String json) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$fromJsonObject, "<this>");
        try {
            Result.Companion companion = Result.Companion;
            Intrinsics.needClassReification();
            Type type = new GsonExtensionsKt$fromJsonObject$lambda0$$inlined$genericType$1().getType();
            Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
            Object objFromJson = $this$fromJsonObject.fromJson(json, type);
            Intrinsics.reifiedOperationMarker(2, "T");
            obj = Result.constructor-impl(objFromJson);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        return obj;
    }

    public static final /* synthetic */ <T> Object fromJsonArray(Gson $this$fromJsonArray, String json) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$fromJsonArray, "<this>");
        try {
            Result.Companion companion = Result.Companion;
            Intrinsics.reifiedOperationMarker(4, "T");
            Object objFromJson = $this$fromJsonArray.fromJson(json, new ParameterizedTypeImpl(Object.class));
            obj = Result.constructor-impl(objFromJson instanceof List ? (List) objFromJson : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        return obj;
    }

    public static final /* synthetic */ <T> Object fromJsonObject(Gson $this$fromJsonObject, InputStream inputStream) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$fromJsonObject, "<this>");
        try {
            Result.Companion companion = Result.Companion;
            InputStreamReader reader = new InputStreamReader(inputStream);
            Intrinsics.needClassReification();
            Type type = new GsonExtensionsKt$fromJsonObject$lambda2$$inlined$genericType$1().getType();
            Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
            Object objFromJson = $this$fromJsonObject.fromJson(reader, type);
            Intrinsics.reifiedOperationMarker(2, "T");
            obj = Result.constructor-impl(objFromJson);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        return obj;
    }

    public static final /* synthetic */ <T> Object fromJsonArray(Gson $this$fromJsonArray, InputStream inputStream) {
        Object obj;
        Intrinsics.checkNotNullParameter($this$fromJsonArray, "<this>");
        try {
            Result.Companion companion = Result.Companion;
            InputStreamReader reader = new InputStreamReader(inputStream);
            Intrinsics.reifiedOperationMarker(4, "T");
            Object objFromJson = $this$fromJsonArray.fromJson(reader, new ParameterizedTypeImpl(Object.class));
            obj = Result.constructor-impl(objFromJson instanceof List ? (List) objFromJson : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        return obj;
    }

    public static final void writeToOutputStream(@NotNull Gson $this$writeToOutputStream, @NotNull OutputStream out, @NotNull Object any) {
        Intrinsics.checkNotNullParameter($this$writeToOutputStream, "<this>");
        Intrinsics.checkNotNullParameter(out, "out");
        Intrinsics.checkNotNullParameter(any, "any");
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, Constants.CHARACTER_ENCODING));
        writer.setIndent("  ");
        if (any instanceof List) {
            writer.beginArray();
            Iterable $this$forEach$iv = (Iterable) any;
            for (Object element$iv : $this$forEach$iv) {
                if (element$iv != null) {
                    $this$writeToOutputStream.toJson(element$iv, element$iv.getClass(), writer);
                }
            }
            writer.endArray();
        } else {
            $this$writeToOutputStream.toJson(any, any.getClass(), writer);
        }
        writer.close();
    }
}
