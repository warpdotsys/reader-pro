package io.legado.app.model.analyzeRule;

import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: RuleData.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/RuleData.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\u0005H\u0016J\b\u0010\f\u001a\u0004\u0018\u00010\u0005J\u001a\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u0005H\u0016R7\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005`\u00068VX\u0096\u0084\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b¨\u0006\u0011"}, d2 = {"Lio/legado/app/model/analyzeRule/RuleData;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "()V", "variableMap", "Ljava/util/HashMap;", PackageDocumentBase.PREFIX_OPF, "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "getUserNameSpace", "getVariable", "putVariable", PackageDocumentBase.PREFIX_OPF, "key", "value", "reader-pro"})
public final class RuleData implements RuleDataInterface {

    /* JADX INFO: renamed from: variableMap$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy variableMap = LazyKt.lazy(RuleData$variableMap$2.INSTANCE);

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @Nullable
    public String getVariable(@NotNull String key) {
        return RuleDataInterface.DefaultImpls.getVariable(this, key);
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @NotNull
    public HashMap<String, String> getVariableMap() {
        return (HashMap) this.variableMap.getValue();
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    public void putVariable(@NotNull String key, @Nullable String value) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (value == null) {
            getVariableMap().remove(key);
        } else {
            getVariableMap().put(key, value);
        }
    }

    @Nullable
    public final String getVariable() {
        if (getVariableMap().isEmpty()) {
            return null;
        }
        return GsonExtensionsKt.getGSON().toJson(getVariableMap());
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @NotNull
    public String getUserNameSpace() {
        return "unknow";
    }
}
