package io.legado.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;

/* JADX INFO: compiled from: GsonExtensions.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/GsonExtensionsKt$GSON$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n"}, d2 = {"<anonymous>", "Lcom/google/gson/Gson;", "kotlin.jvm.PlatformType"})
final class GsonExtensionsKt$GSON$2 extends Lambda implements Function0<Gson> {
    public static final GsonExtensionsKt$GSON$2 INSTANCE = new GsonExtensionsKt$GSON$2();

    GsonExtensionsKt$GSON$2() {
        super(0);
    }

    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final Gson m290invoke() {
        return new GsonBuilder().registerTypeAdapter(new AnonymousClass1().getType(), new MapDeserializerDoubleAsIntFix()).registerTypeAdapter(Integer.TYPE, new IntJsonDeserializer()).disableHtmlEscaping().setPrettyPrinting().create();
    }

    /* JADX INFO: renamed from: io.legado.app.utils.GsonExtensionsKt$GSON$2$1, reason: invalid class name */
    /* JADX INFO: compiled from: GsonExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/GsonExtensionsKt$GSON$2$1.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0000*\u0001\u0000\b\n\u0018\u00002\u001a\u0012\u0016\u0012\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00020\u0001¨\u0006\u0005"}, d2 = {"io/legado/app/utils/GsonExtensionsKt$GSON$2$1", "Lcom/google/gson/reflect/TypeToken;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "reader-pro"})
    public static final class AnonymousClass1 extends TypeToken<Map<String, ? extends Object>> {
        AnonymousClass1() {
        }
    }
}
