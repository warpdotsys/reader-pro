package io.legado.app.help;

import io.legado.app.data.entities.TxtTocRule;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: DefaultData.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/DefaultData.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R!\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t¨\u0006\f"}, d2 = {"Lio/legado/app/help/DefaultData;", PackageDocumentBase.PREFIX_OPF, "()V", "txtTocRuleFileName", PackageDocumentBase.PREFIX_OPF, "txtTocRules", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/TxtTocRule;", "getTxtTocRules", "()Ljava/util/List;", "txtTocRules$delegate", "Lkotlin/Lazy;", "reader-pro"})
public final class DefaultData {

    @NotNull
    public static final String txtTocRuleFileName = "txtTocRule.json";

    @NotNull
    public static final DefaultData INSTANCE = new DefaultData();

    /* JADX INFO: renamed from: txtTocRules$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy txtTocRules = LazyKt.lazy(DefaultData$txtTocRules$2.INSTANCE);

    private DefaultData() {
    }

    @NotNull
    public final List<TxtTocRule> getTxtTocRules() {
        return (List) txtTocRules.getValue();
    }
}
