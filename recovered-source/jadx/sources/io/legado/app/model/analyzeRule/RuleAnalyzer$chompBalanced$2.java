package io.legado.app.model.analyzeRule;

import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: RuleAnalyzer.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/RuleAnalyzer$chompBalanced$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
/* synthetic */ class RuleAnalyzer$chompBalanced$2 extends FunctionReferenceImpl implements Function2<Character, Character, Boolean> {
    RuleAnalyzer$chompBalanced$2(RuleAnalyzer ruleAnalyzer) {
        super(2, ruleAnalyzer, RuleAnalyzer.class, "chompRuleBalanced", "chompRuleBalanced(CC)Z", 0);
    }

    public final boolean invoke(char p0, char p1) {
        return ((RuleAnalyzer) this.receiver).chompRuleBalanced(p0, p1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
        return Boolean.valueOf(invoke(((Character) p1).charValue(), ((Character) p2).charValue()));
    }
}
