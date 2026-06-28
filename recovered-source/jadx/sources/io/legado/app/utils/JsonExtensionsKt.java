package io.legado.app.utils;

import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: JsonExtensions.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/JsonExtensionsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0019\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\b2\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000b\u001a\u0019\u0010\f\u001a\u0004\u0018\u00010\r*\u00020\b2\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000e\u001a\u0019\u0010\u000f\u001a\u0004\u0018\u00010\u0010*\u00020\b2\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u0011\u001a\u0014\u0010\u0012\u001a\u0004\u0018\u00010\n*\u00020\b2\u0006\u0010\t\u001a\u00020\n\"\u001b\u0010\u0000\u001a\u00020\u00018FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003¨\u0006\u0013"}, d2 = {"jsonPath", "Lcom/jayway/jsonpath/ParseContext;", "getJsonPath", "()Lcom/jayway/jsonpath/ParseContext;", "jsonPath$delegate", "Lkotlin/Lazy;", "readBool", PackageDocumentBase.PREFIX_OPF, "Lcom/jayway/jsonpath/ReadContext;", "path", PackageDocumentBase.PREFIX_OPF, "(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/Boolean;", "readInt", PackageDocumentBase.PREFIX_OPF, "(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/Integer;", "readLong", PackageDocumentBase.PREFIX_OPF, "(Lcom/jayway/jsonpath/ReadContext;Ljava/lang/String;)Ljava/lang/Long;", "readString", "reader-pro"})
public final class JsonExtensionsKt {

    @NotNull
    private static final Lazy jsonPath$delegate = LazyKt.lazy(JsonExtensionsKt$jsonPath$2.INSTANCE);

    @NotNull
    public static final ParseContext getJsonPath() {
        Object value = jsonPath$delegate.getValue();
        Intrinsics.checkNotNullExpressionValue(value, "<get-jsonPath>(...)");
        return (ParseContext) value;
    }

    @Nullable
    public static final String readString(@NotNull ReadContext $this$readString, @NotNull String path) {
        Intrinsics.checkNotNullParameter($this$readString, "<this>");
        Intrinsics.checkNotNullParameter(path, "path");
        return (String) $this$readString.read(path, String.class, new Predicate[0]);
    }

    @Nullable
    public static final Boolean readBool(@NotNull ReadContext $this$readBool, @NotNull String path) {
        Intrinsics.checkNotNullParameter($this$readBool, "<this>");
        Intrinsics.checkNotNullParameter(path, "path");
        return (Boolean) $this$readBool.read(path, Boolean.TYPE, new Predicate[0]);
    }

    @Nullable
    public static final Integer readInt(@NotNull ReadContext $this$readInt, @NotNull String path) {
        Intrinsics.checkNotNullParameter($this$readInt, "<this>");
        Intrinsics.checkNotNullParameter(path, "path");
        return (Integer) $this$readInt.read(path, Integer.TYPE, new Predicate[0]);
    }

    @Nullable
    public static final Long readLong(@NotNull ReadContext $this$readLong, @NotNull String path) {
        Intrinsics.checkNotNullParameter($this$readLong, "<this>");
        Intrinsics.checkNotNullParameter(path, "path");
        return (Long) $this$readLong.read(path, Long.TYPE, new Predicate[0]);
    }
}
