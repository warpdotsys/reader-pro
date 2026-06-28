package io.legado.app.help;

import com.google.gson.Gson;
import io.legado.app.data.entities.TxtTocRule;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.ParameterizedTypeImpl;
import java.net.URL;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.Charsets;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: DefaultData.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/DefaultData$txtTocRules$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/TxtTocRule;"})
final class DefaultData$txtTocRules$2 extends Lambda implements Function0<List<? extends TxtTocRule>> {
    public static final DefaultData$txtTocRules$2 INSTANCE = new DefaultData$txtTocRules$2();

    DefaultData$txtTocRules$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final List<TxtTocRule> m169invoke() {
        Object obj;
        URL resource = DefaultData.class.getResource("/defaultData/txtTocRule.json");
        Intrinsics.checkNotNullExpressionValue(resource, "DefaultData::class.java.getResource(\"/defaultData/${txtTocRuleFileName}\")");
        String json = new String(TextStreamsKt.readBytes(resource), Charsets.UTF_8);
        Gson $this$fromJsonArray$iv = GsonExtensionsKt.getGSON();
        try {
            Result.Companion companion = Result.Companion;
            Object objFromJson = $this$fromJsonArray$iv.fromJson(json, new ParameterizedTypeImpl(TxtTocRule.class));
            obj = Result.constructor-impl(objFromJson instanceof List ? (List) objFromJson : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        Object obj2 = obj;
        List<TxtTocRule> list = (List) (Result.isFailure-impl(obj2) ? null : obj2);
        return list == null ? CollectionsKt.emptyList() : list;
    }
}
