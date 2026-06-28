package io.legado.app.model.analyzeRule;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.StringCompanionObject;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: AnalyzeUrl.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeUrl$replaceKeyPageJs$url$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it"})
final class AnalyzeUrl$replaceKeyPageJs$url$1 extends Lambda implements Function1<String, String> {
    final /* synthetic */ AnalyzeUrl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    AnalyzeUrl$replaceKeyPageJs$url$1(AnalyzeUrl this$0) {
        super(1);
        this.this$0 = this$0;
    }

    @Nullable
    public final String invoke(@NotNull String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        Object objEvalJS$default = AnalyzeUrl.evalJS$default(this.this$0, it, null, 2, null);
        Object jsEval = objEvalJS$default == null ? PackageDocumentBase.PREFIX_OPF : objEvalJS$default;
        if (jsEval instanceof String) {
            return (String) jsEval;
        }
        if (jsEval instanceof Double) {
            if (((Number) jsEval).doubleValue() % 1.0d == 0.0d) {
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                Object[] objArr = {jsEval};
                String str = String.format("%.0f", Arrays.copyOf(objArr, objArr.length));
                Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
                return str;
            }
        }
        return jsEval.toString();
    }
}
