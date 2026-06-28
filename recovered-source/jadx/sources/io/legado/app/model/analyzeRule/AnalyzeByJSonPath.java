package io.legado.app.model.analyzeRule;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: AnalyzeByJSonPath.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/analyzeRule/AnalyzeByJSonPath.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0003\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\u001d\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\nJ\u0015\u0010\u000b\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\t2\u0006\u0010\b\u001a\u00020\tJ\u001b\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f2\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByJSonPath;", PackageDocumentBase.PREFIX_OPF, "json", "(Ljava/lang/Object;)V", "ctx", "Lcom/jayway/jsonpath/ReadContext;", "getList", "Ljava/util/ArrayList;", "rule", PackageDocumentBase.PREFIX_OPF, "getList$reader_pro", "getObject", "getObject$reader_pro", "getString", "getStringList", PackageDocumentBase.PREFIX_OPF, "getStringList$reader_pro", "Companion", "reader-pro"})
public final class AnalyzeByJSonPath {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private ReadContext ctx;

    public AnalyzeByJSonPath(@NotNull Object json) {
        Intrinsics.checkNotNullParameter(json, "json");
        this.ctx = INSTANCE.parse(json);
    }

    /* JADX INFO: compiled from: AnalyzeByJSonPath.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/analyzeRule/AnalyzeByJSonPath$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001¨\u0006\u0006"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByJSonPath$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "parse", "Lcom/jayway/jsonpath/ReadContext;", "json", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final ReadContext parse(@NotNull Object json) {
            Intrinsics.checkNotNullParameter(json, "json");
            if (json instanceof ReadContext) {
                return (ReadContext) json;
            }
            if (json instanceof String) {
                ReadContext readContext = JsonPath.parse((String) json);
                Intrinsics.checkNotNullExpressionValue(readContext, "parse(json)");
                return readContext;
            }
            ReadContext readContext2 = JsonPath.parse(json);
            Intrinsics.checkNotNullExpressionValue(readContext2, "parse(json)");
            return readContext2;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:16:0x0085
            at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
            at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getString(@org.jetbrains.annotations.NotNull java.lang.String r11) {
        /*
            Method dump skipped, instruction units count: 337
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.legado.app.model.analyzeRule.AnalyzeByJSonPath.getString(java.lang.String):java.lang.String");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:16:0x0097
            at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
            at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getStringList$reader_pro(@org.jetbrains.annotations.NotNull java.lang.String r9) {
        /*
            Method dump skipped, instruction units count: 557
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.legado.app.model.analyzeRule.AnalyzeByJSonPath.getStringList$reader_pro(java.lang.String):java.util.List");
    }

    @NotNull
    public final Object getObject$reader_pro(@NotNull String rule) {
        Intrinsics.checkNotNullParameter(rule, "rule");
        Object obj = this.ctx.read(rule, new Predicate[0]);
        Intrinsics.checkNotNullExpressionValue(obj, "ctx.read(rule)");
        return obj;
    }

    @Nullable
    public final ArrayList<Object> getList$reader_pro(@NotNull String rule) {
        Object it;
        Intrinsics.checkNotNullParameter(rule, "rule");
        ArrayList<Object> arrayList = new ArrayList<>();
        if (rule.length() == 0) {
            return arrayList;
        }
        RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(rule, true);
        ArrayList<String> arrayListSplitRule = ruleAnalyzes.splitRule("&&", "||", "%%");
        if (arrayListSplitRule.size() == 1) {
            try {
                return (ArrayList) this.ctx.read(arrayListSplitRule.get(0), new Predicate[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ArrayList<ArrayList> results = new ArrayList();
            for (String rl : arrayListSplitRule) {
                Intrinsics.checkNotNullExpressionValue(rl, "rl");
                ArrayList<Object> list$reader_pro = getList$reader_pro(rl);
                if (list$reader_pro != null) {
                    if (!list$reader_pro.isEmpty()) {
                        results.add(list$reader_pro);
                        if ((!list$reader_pro.isEmpty()) && Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (results.size() > 0) {
                if (Intrinsics.areEqual("%%", ruleAnalyzes.getElementsType())) {
                    int i = 0;
                    int size = ((ArrayList) results.get(0)).size();
                    if (0 < size) {
                        do {
                            int i2 = i;
                            i++;
                            for (ArrayList temp : results) {
                                if (i2 < temp.size() && (it = temp.get(i2)) != null) {
                                    arrayList.add(it);
                                }
                            }
                        } while (i < size);
                    }
                } else {
                    Iterator it2 = results.iterator();
                    while (it2.hasNext()) {
                        arrayList.addAll((ArrayList) it2.next());
                    }
                }
            }
        }
        return arrayList;
    }
}
