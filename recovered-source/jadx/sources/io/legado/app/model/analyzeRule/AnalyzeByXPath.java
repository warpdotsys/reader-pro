package io.legado.app.model.analyzeRule;

import io.legado.app.utils.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

/* JADX INFO: compiled from: AnalyzeByXPath.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByXPath.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\u001d\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\nJ\u0018\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\tH\u0002J\u0010\u0010\f\u001a\u0004\u0018\u00010\t2\u0006\u0010\r\u001a\u00020\tJ\u001b\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\u000fJ\u0010\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByXPath;", PackageDocumentBase.PREFIX_OPF, "doc", "(Ljava/lang/Object;)V", "jxNode", "getElements", PackageDocumentBase.PREFIX_OPF, "Lorg/seimicrawler/xpath/JXNode;", "xPath", PackageDocumentBase.PREFIX_OPF, "getElements$reader_pro", "getResult", "getString", "rule", "getStringList", "getStringList$reader_pro", "parse", "strToJXDocument", "Lorg/seimicrawler/xpath/JXDocument;", "html", "reader-pro"})
public final class AnalyzeByXPath {

    @NotNull
    private Object jxNode;

    public AnalyzeByXPath(@NotNull Object doc) {
        Intrinsics.checkNotNullParameter(doc, "doc");
        this.jxNode = parse(doc);
    }

    private final Object parse(Object doc) {
        if (doc instanceof JXNode) {
            return ((JXNode) doc).isElement() ? doc : strToJXDocument(doc.toString());
        }
        if (doc instanceof Document) {
            JXDocument jXDocumentCreate = JXDocument.create((Document) doc);
            Intrinsics.checkNotNullExpressionValue(jXDocumentCreate, "create(doc)");
            return jXDocumentCreate;
        }
        if (doc instanceof Element) {
            JXDocument jXDocumentCreate2 = JXDocument.create(new Elements(new Element[]{(Element) doc}));
            Intrinsics.checkNotNullExpressionValue(jXDocumentCreate2, "create(Elements(doc))");
            return jXDocumentCreate2;
        }
        if (!(doc instanceof Elements)) {
            return strToJXDocument(doc.toString());
        }
        JXDocument jXDocumentCreate3 = JXDocument.create((Elements) doc);
        Intrinsics.checkNotNullExpressionValue(jXDocumentCreate3, "create(doc)");
        return jXDocumentCreate3;
    }

    private final JXDocument strToJXDocument(String html) {
        String html1 = html;
        if (StringsKt.endsWith$default(html1, "</td>", false, 2, (Object) null)) {
            html1 = "<tr>" + html1 + "</tr>";
        }
        if (StringsKt.endsWith$default(html1, "</tr>", false, 2, (Object) null) || StringsKt.endsWith$default(html1, "</tbody>", false, 2, (Object) null)) {
            html1 = "<table>" + html1 + "</table>";
        }
        JXDocument jXDocumentCreate = JXDocument.create(html1);
        Intrinsics.checkNotNullExpressionValue(jXDocumentCreate, "create(html1)");
        return jXDocumentCreate;
    }

    private final List<JXNode> getResult(String xPath) {
        Object node = this.jxNode;
        if (node instanceof JXNode) {
            return ((JXNode) node).sel(xPath);
        }
        return ((JXDocument) node).selN(xPath);
    }

    @Nullable
    public final List<JXNode> getElements$reader_pro(@NotNull String xPath) {
        Intrinsics.checkNotNullParameter(xPath, "xPath");
        if (xPath.length() == 0) {
            return null;
        }
        ArrayList jxNodes = new ArrayList();
        RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(xPath, false, 2, null);
        ArrayList<String> arrayListSplitRule = ruleAnalyzes.splitRule("&&", "||", "%%");
        if (arrayListSplitRule.size() == 1) {
            String str = arrayListSplitRule.get(0);
            Intrinsics.checkNotNullExpressionValue(str, "rules[0]");
            return getResult(str);
        }
        ArrayList<List> results = new ArrayList();
        for (String rl : arrayListSplitRule) {
            Intrinsics.checkNotNullExpressionValue(rl, "rl");
            List<JXNode> elements$reader_pro = getElements$reader_pro(rl);
            if (elements$reader_pro != null) {
                if (!elements$reader_pro.isEmpty()) {
                    results.add(elements$reader_pro);
                    if ((!elements$reader_pro.isEmpty()) && Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
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
                int size = ((List) results.get(0)).size() - 1;
                if (0 <= size) {
                    do {
                        int i2 = i;
                        i++;
                        for (List temp : results) {
                            if (i2 < temp.size()) {
                                jxNodes.add(temp.get(i2));
                            }
                        }
                    } while (i <= size);
                }
            } else {
                Iterator it = results.iterator();
                while (it.hasNext()) {
                    jxNodes.addAll((List) it.next());
                }
            }
        }
        return jxNodes;
    }

    @NotNull
    public final List<String> getStringList$reader_pro(@NotNull String xPath) {
        Intrinsics.checkNotNullParameter(xPath, "xPath");
        ArrayList result = new ArrayList();
        RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(xPath, false, 2, null);
        ArrayList<String> arrayListSplitRule = ruleAnalyzes.splitRule("&&", "||", "%%");
        if (arrayListSplitRule.size() == 1) {
            Iterable result2 = getResult(xPath);
            if (result2 != null) {
                Iterable $this$map$iv = result2;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                for (Object item$iv$iv : $this$map$iv) {
                    JXNode it = (JXNode) item$iv$iv;
                    destination$iv$iv.add(Boolean.valueOf(result.add(it.asString())));
                }
            }
            return result;
        }
        ArrayList<List> results = new ArrayList();
        for (String rl : arrayListSplitRule) {
            Intrinsics.checkNotNullExpressionValue(rl, "rl");
            List<String> stringList$reader_pro = getStringList$reader_pro(rl);
            if (!stringList$reader_pro.isEmpty()) {
                results.add(stringList$reader_pro);
                if ((!stringList$reader_pro.isEmpty()) && Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
                    break;
                }
            }
        }
        if (results.size() > 0) {
            if (Intrinsics.areEqual("%%", ruleAnalyzes.getElementsType())) {
                int i = 0;
                int size = ((List) results.get(0)).size() - 1;
                if (0 <= size) {
                    do {
                        int i2 = i;
                        i++;
                        for (List temp : results) {
                            if (i2 < temp.size()) {
                                result.add(temp.get(i2));
                            }
                        }
                    } while (i <= size);
                }
            } else {
                Iterator it2 = results.iterator();
                while (it2.hasNext()) {
                    result.addAll((List) it2.next());
                }
            }
        }
        return result;
    }

    @Nullable
    public final String getString(@NotNull String rule) {
        Intrinsics.checkNotNullParameter(rule, "rule");
        RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(rule, false, 2, null);
        ArrayList<String> arrayListSplitRule = ruleAnalyzes.splitRule("&&", "||");
        if (arrayListSplitRule.size() == 1) {
            List<JXNode> result = getResult(rule);
            if (result != null) {
                return TextUtils.join("\n", result);
            }
            return null;
        }
        ArrayList textList = new ArrayList();
        for (String rl : arrayListSplitRule) {
            Intrinsics.checkNotNullExpressionValue(rl, "rl");
            String temp = getString(rl);
            String str = temp;
            if (!(str == null || str.length() == 0)) {
                textList.add(temp);
                if (Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
                    break;
                }
            }
        }
        return CollectionsKt.joinToString$default(textList, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }
}
