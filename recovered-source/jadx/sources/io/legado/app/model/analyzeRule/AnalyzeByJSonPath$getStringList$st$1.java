package io.legado.app.model.analyzeRule;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: AnalyzeByJSonPath.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByJSonPath$getStringList$st$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "it"})
final class AnalyzeByJSonPath$getStringList$st$1 extends Lambda implements Function1<String, String> {
    final /* synthetic */ AnalyzeByJSonPath this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    AnalyzeByJSonPath$getStringList$st$1(AnalyzeByJSonPath this$0) {
        super(1);
        this.this$0 = this$0;
    }

    @Nullable
    public final String invoke(@NotNull String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return this.this$0.getString(it);
    }
}
