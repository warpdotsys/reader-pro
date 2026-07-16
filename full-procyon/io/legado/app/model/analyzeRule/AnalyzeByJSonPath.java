// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt;
import java.util.List;
import com.jayway.jsonpath.Predicate;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import com.jayway.jsonpath.ReadContext;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0003\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001?\u0006\u0002\u0010\u0003J\u001d\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\tH\u0000?\u0006\u0002\b\nJ\u0015\u0010\u000b\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\tH\u0000?\u0006\u0002\b\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\t2\u0006\u0010\b\u001a\u00020\tJ\u001b\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f2\u0006\u0010\b\u001a\u00020\tH\u0000?\u0006\u0002\b\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u0012" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByJSonPath;", "", "json", "(Ljava/lang/Object;)V", "ctx", "Lcom/jayway/jsonpath/ReadContext;", "getList", "Ljava/util/ArrayList;", "rule", "", "getList$reader_pro", "getObject", "getObject$reader_pro", "getString", "getStringList", "", "getStringList$reader_pro", "Companion", "reader-pro" })
public final class AnalyzeByJSonPath
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private ReadContext ctx;
    
    public AnalyzeByJSonPath(@NotNull final Object json) {
        Intrinsics.checkNotNullParameter(json, "json");
        this.ctx = AnalyzeByJSonPath.Companion.parse(json);
    }
    
    @Nullable
    public final String getString(@NotNull final String rule) {
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        if (rule.length() == 0) {
            return null;
        }
        String result = null;
        final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(rule, true);
        final ArrayList rules = ruleAnalyzes.splitRule("&&", "||");
        if (rules.size() == 1) {
            ruleAnalyzes.reSetPos();
            result = RuleAnalyzer.innerRule$default(ruleAnalyzes, "{$.", 0, 0, (Function1)new AnalyzeByJSonPath$getString.AnalyzeByJSonPath$getString$1(this), 6, null);
            if (result.length() == 0) {
                try {
                    final Object ob = this.ctx.read(rule, new Predicate[0]);
                    result = ((ob instanceof List) ? CollectionsKt.joinToString$default((Iterable)ob, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null) : ob.toString());
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
        final ArrayList textList = new ArrayList();
        for (final String rl : rules) {
            Intrinsics.checkNotNullExpressionValue((Object)rl, "rl");
            final String temp = this.getString(rl);
            final CharSequence charSequence = temp;
            if (charSequence != null && charSequence.length() != 0) {
                textList.add(temp);
                if (Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                    break;
                }
                continue;
            }
        }
        return CollectionsKt.joinToString$default((Iterable)textList, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
    }
    
    @NotNull
    public final List<String> getStringList$reader_pro(@NotNull final String rule) {
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        final ArrayList result = new ArrayList();
        if (rule.length() == 0) {
            return result;
        }
        final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(rule, true);
        final ArrayList rules = ruleAnalyzes.splitRule("&&", "||", "%%");
        if (rules.size() == 1) {
            ruleAnalyzes.reSetPos();
            final String st = RuleAnalyzer.innerRule$default(ruleAnalyzes, "{$.", 0, 0, (Function1)new AnalyzeByJSonPath$getStringList$st.AnalyzeByJSonPath$getStringList$st$1(this), 6, null);
            if (st.length() == 0) {
                try {
                    final Object obj = this.ctx.read(rule, new Predicate[0]);
                    if (obj instanceof List) {
                        for (final Object o : (List)obj) {
                            result.add(String.valueOf(o));
                        }
                    }
                    else {
                        result.add(obj.toString());
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                result.add(st);
            }
            return result;
        }
        final ArrayList results = new ArrayList();
        for (final String rl : rules) {
            Intrinsics.checkNotNullExpressionValue((Object)rl, "rl");
            final List temp = this.getStringList$reader_pro(rl);
            if (!temp.isEmpty()) {
                results.add(temp);
                if (!temp.isEmpty() && Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                    break;
                }
                continue;
            }
        }
        if (results.size() > 0) {
            if (Intrinsics.areEqual((Object)"%%", (Object)ruleAnalyzes.getElementsType())) {
                int j = 0;
                final int n = results.get(0).size() - 1;
                if (j <= n) {
                    do {
                        final int i = j;
                        ++j;
                        for (final List temp2 : results) {
                            if (i < temp2.size()) {
                                result.add(temp2.get(i));
                            }
                        }
                    } while (j <= n);
                }
            }
            else {
                for (final List temp3 : results) {
                    result.addAll(temp3);
                }
            }
        }
        return result;
    }
    
    @NotNull
    public final Object getObject$reader_pro(@NotNull final String rule) {
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        final Object read = this.ctx.read(rule, new Predicate[0]);
        Intrinsics.checkNotNullExpressionValue(read, "ctx.read(rule)");
        return read;
    }
    
    @Nullable
    public final ArrayList<Object> getList$reader_pro(@NotNull final String rule) {
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        final ArrayList result = new ArrayList();
        if (rule.length() == 0) {
            return result;
        }
        final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(rule, true);
        final ArrayList rules = ruleAnalyzes.splitRule("&&", "||", "%%");
        if (rules.size() == 1) {
            final ReadContext it = this.ctx;
            final int n = 0;
            try {
                return (ArrayList)it.read((String)rules.get(0), new Predicate[0]);
            }
            catch (final Exception e) {
                e.printStackTrace();
                return result;
            }
        }
        final ArrayList results = new ArrayList();
        for (final String rl : rules) {
            Intrinsics.checkNotNullExpressionValue((Object)rl, "rl");
            final ArrayList temp = this.getList$reader_pro(rl);
            if (temp != null && !temp.isEmpty()) {
                results.add(temp);
                if (!temp.isEmpty() && Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                    break;
                }
                continue;
            }
        }
        if (results.size() > 0) {
            if (Intrinsics.areEqual((Object)"%%", (Object)ruleAnalyzes.getElementsType())) {
                int j = 0;
                final int size = results.get(0).size();
                if (j < size) {
                    do {
                        final int i = j;
                        ++j;
                        for (final ArrayList temp2 : results) {
                            if (i < temp2.size()) {
                                final Object value = temp2.get(i);
                                if (value == null) {
                                    continue;
                                }
                                final Object it2 = value;
                                final int n2 = 0;
                                result.add(it2);
                            }
                        }
                    } while (j < size);
                }
            }
            else {
                for (final ArrayList temp3 : results) {
                    result.addAll(temp3);
                }
            }
        }
        return result;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001¡§\u0006\u0006" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByJSonPath$Companion;", "", "()V", "parse", "Lcom/jayway/jsonpath/ReadContext;", "json", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final ReadContext parse(@NotNull final Object json) {
            Intrinsics.checkNotNullParameter(json, "json");
            ReadContext readContext;
            if (json instanceof ReadContext) {
                readContext = (ReadContext)json;
            }
            else if (json instanceof String) {
                final DocumentContext parse = JsonPath.parse((String)json);
                Intrinsics.checkNotNullExpressionValue((Object)parse, "parse(json)");
                readContext = (ReadContext)parse;
            }
            else {
                final DocumentContext parse2 = JsonPath.parse(json);
                Intrinsics.checkNotNullExpressionValue((Object)parse2, "parse(json)");
                readContext = (ReadContext)parse2;
            }
            return readContext;
        }
    }
}
