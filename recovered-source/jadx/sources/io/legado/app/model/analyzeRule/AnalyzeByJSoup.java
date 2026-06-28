package io.legado.app.model.analyzeRule;

import io.legado.app.constant.RSSKeywords;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Triple;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.seimicrawler.xpath.JXNode;

/* JADX INFO: compiled from: AnalyzeByJSoup.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByJSoup.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000e\u0018\u0000 \u00182\u00020\u0001:\u0003\u0018\u0019\u001aB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\u0015\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0000¢\u0006\u0002\b\nJ\u001a\u0010\u0006\u001a\u00020\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u0002J\u001e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\tH\u0002J\u0018\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\r2\u0006\u0010\u0011\u001a\u00020\tH\u0002J\u0017\u0010\u0012\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0011\u001a\u00020\tH\u0000¢\u0006\u0002\b\u0013J\u0015\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tH\u0000¢\u0006\u0002\b\u0015J\u001b\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\r2\u0006\u0010\u0011\u001a\u00020\tH\u0000¢\u0006\u0002\b\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;", PackageDocumentBase.PREFIX_OPF, "doc", "(Ljava/lang/Object;)V", "element", "Lorg/jsoup/nodes/Element;", "getElements", "Lorg/jsoup/select/Elements;", "rule", PackageDocumentBase.PREFIX_OPF, "getElements$reader_pro", "temp", "getResultLast", PackageDocumentBase.PREFIX_OPF, "elements", "lastRule", "getResultList", "ruleStr", "getString", "getString$reader_pro", "getString0", "getString0$reader_pro", "getStringList", "getStringList$reader_pro", "Companion", "ElementsSingle", "SourceRule", "reader-pro"})
public final class AnalyzeByJSoup {

    @NotNull
    private Element element;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private static final String[] validKeys = {NCXDocumentV2.NCXAttributes.clazz, "id", "tag", NCXDocumentV2.NCXTags.text, "children"};

    /* JADX INFO: compiled from: AnalyzeByJSoup.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByJSoup$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007¨\u0006\f"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByJSoup$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "validKeys", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "getValidKeys", "()[Ljava/lang/String;", "[Ljava/lang/String;", "parse", "Lorg/jsoup/nodes/Element;", "doc", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final String[] getValidKeys() {
            return AnalyzeByJSoup.validKeys;
        }

        @NotNull
        public final Element parse(@NotNull Object doc) {
            Intrinsics.checkNotNullParameter(doc, "doc");
            if (doc instanceof Element) {
                return (Element) doc;
            }
            if (doc instanceof JXNode) {
                Element elementAsElement = ((JXNode) doc).isElement() ? ((JXNode) doc).asElement() : Jsoup.parse(doc.toString());
                Intrinsics.checkNotNullExpressionValue(elementAsElement, "if (doc.isElement) doc.asElement() else Jsoup.parse(doc.toString())");
                return elementAsElement;
            }
            Element element = Jsoup.parse(doc.toString());
            Intrinsics.checkNotNullExpressionValue(element, "parse(doc.toString())");
            return element;
        }
    }

    public AnalyzeByJSoup(@NotNull Object doc) {
        Intrinsics.checkNotNullParameter(doc, "doc");
        this.element = INSTANCE.parse(doc);
    }

    @NotNull
    public final Elements getElements$reader_pro(@NotNull String rule) {
        Intrinsics.checkNotNullParameter(rule, "rule");
        return getElements(this.element, rule);
    }

    @Nullable
    public final String getString$reader_pro(@NotNull String ruleStr) {
        Intrinsics.checkNotNullParameter(ruleStr, "ruleStr");
        if (ruleStr.length() == 0) {
            return null;
        }
        List<String> stringList$reader_pro = getStringList$reader_pro(ruleStr);
        List<String> list = !stringList$reader_pro.isEmpty() ? stringList$reader_pro : null;
        if (list == null) {
            return null;
        }
        return CollectionsKt.joinToString$default(list, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }

    @NotNull
    public final String getString0$reader_pro(@NotNull String ruleStr) {
        Intrinsics.checkNotNullParameter(ruleStr, "ruleStr");
        List<String> stringList$reader_pro = getStringList$reader_pro(ruleStr);
        return stringList$reader_pro.isEmpty() ? PackageDocumentBase.PREFIX_OPF : stringList$reader_pro.get(0);
    }

    @NotNull
    public final List<String> getStringList$reader_pro(@NotNull String ruleStr) {
        List<String> resultList;
        Intrinsics.checkNotNullParameter(ruleStr, "ruleStr");
        ArrayList textS = new ArrayList();
        if (ruleStr.length() == 0) {
            return textS;
        }
        SourceRule sourceRule = new SourceRule(this, ruleStr);
        if (sourceRule.getElementsRule().length() == 0) {
            String strData = this.element.data();
            textS.add(strData == null ? PackageDocumentBase.PREFIX_OPF : strData);
        } else {
            RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(sourceRule.getElementsRule(), false, 2, null);
            ArrayList<String> arrayListSplitRule = ruleAnalyzes.splitRule("&&", "||", "%%");
            ArrayList<List> results = new ArrayList();
            for (String ruleStrX : arrayListSplitRule) {
                if (sourceRule.isCss()) {
                    Intrinsics.checkNotNullExpressionValue(ruleStrX, "ruleStrX");
                    int lastIndex = StringsKt.lastIndexOf$default(ruleStrX, '@', 0, false, 6, (Object) null);
                    Element element = this.element;
                    String strSubstring = ruleStrX.substring(0, lastIndex);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    Elements elementsSelect = element.select(strSubstring);
                    Intrinsics.checkNotNullExpressionValue(elementsSelect, "element.select(ruleStrX.substring(0, lastIndex))");
                    String strSubstring2 = ruleStrX.substring(lastIndex + 1);
                    Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
                    resultList = getResultLast(elementsSelect, strSubstring2);
                } else {
                    Intrinsics.checkNotNullExpressionValue(ruleStrX, "ruleStrX");
                    resultList = getResultList(ruleStrX);
                }
                List<String> list = resultList;
                List<String> list2 = list;
                if (!(list2 == null || list2.isEmpty())) {
                    results.add(list);
                    if (Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
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
                                    textS.add(temp.get(i2));
                                }
                            }
                        } while (i <= size);
                    }
                } else {
                    Iterator it = results.iterator();
                    while (it.hasNext()) {
                        textS.addAll((List) it.next());
                    }
                }
            }
        }
        return textS;
    }

    private final Elements getElements(Element temp, String rule) {
        Elements elementsSingle;
        if (temp != null) {
            if (!(rule.length() == 0)) {
                Elements elements = new Elements();
                SourceRule sourceRule = new SourceRule(this, rule);
                RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(sourceRule.getElementsRule(), false, 2, null);
                ArrayList<String> arrayListSplitRule = ruleAnalyzes.splitRule("&&", "||", "%%");
                ArrayList<Elements> elementsList = new ArrayList();
                if (sourceRule.isCss()) {
                    Iterator<String> it = arrayListSplitRule.iterator();
                    while (it.hasNext()) {
                        Elements tempS = temp.select(it.next());
                        elementsList.add(tempS);
                        if (tempS.size() > 0 && Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
                            break;
                        }
                    }
                } else {
                    for (String ruleStr : arrayListSplitRule) {
                        Intrinsics.checkNotNullExpressionValue(ruleStr, "ruleStr");
                        RuleAnalyzer rsRule = new RuleAnalyzer(ruleStr, false, 2, null);
                        rsRule.trim();
                        ArrayList<String> arrayListSplitRule2 = rsRule.splitRule("@");
                        if (arrayListSplitRule2.size() > 1) {
                            Elements<Element> el = new Elements();
                            el.add(temp);
                            for (String rl : arrayListSplitRule2) {
                                Collection elements2 = new Elements();
                                for (Element et : el) {
                                    Intrinsics.checkNotNullExpressionValue(rl, "rl");
                                    elements2.addAll(getElements(et, rl));
                                }
                                el.clear();
                                el.addAll(elements2);
                            }
                            elementsSingle = el;
                        } else {
                            elementsSingle = new ElementsSingle((char) 0, null, null, null, 15, null).getElementsSingle(temp, ruleStr);
                        }
                        Elements el2 = elementsSingle;
                        elementsList.add(el2);
                        if (el2.size() > 0 && Intrinsics.areEqual(ruleAnalyzes.getElementsType(), "||")) {
                            break;
                        }
                    }
                }
                if (elementsList.size() > 0) {
                    if (Intrinsics.areEqual("%%", ruleAnalyzes.getElementsType())) {
                        int i = 0;
                        int size = ((Elements) elementsList.get(0)).size();
                        if (0 < size) {
                            do {
                                int i2 = i;
                                i++;
                                for (Elements es : elementsList) {
                                    if (i2 < es.size()) {
                                        elements.add(es.get(i2));
                                    }
                                }
                            } while (i < size);
                        }
                    } else {
                        Iterator it2 = elementsList.iterator();
                        while (it2.hasNext()) {
                            elements.addAll((Elements) it2.next());
                        }
                    }
                }
                return elements;
            }
        }
        return new Elements();
    }

    private final List<String> getResultList(String ruleStr) {
        if (ruleStr.length() == 0) {
            return null;
        }
        Elements<Element> elements = new Elements();
        elements.add(this.element);
        RuleAnalyzer rule = new RuleAnalyzer(ruleStr, false, 2, null);
        rule.trim();
        ArrayList<String> arrayListSplitRule = rule.splitRule("@");
        int last = arrayListSplitRule.size() - 1;
        int i = 0;
        if (0 < last) {
            do {
                int i2 = i;
                i++;
                Elements es = new Elements();
                for (Element elt : elements) {
                    ElementsSingle elementsSingle = new ElementsSingle((char) 0, null, null, null, 15, null);
                    Intrinsics.checkNotNullExpressionValue(elt, "elt");
                    String str = arrayListSplitRule.get(i2);
                    Intrinsics.checkNotNullExpressionValue(str, "rules[i]");
                    es.addAll(elementsSingle.getElementsSingle(elt, str));
                }
                elements.clear();
                elements = es;
            } while (i < last);
        }
        if (elements.isEmpty()) {
            return null;
        }
        String str2 = arrayListSplitRule.get(last);
        Intrinsics.checkNotNullExpressionValue(str2, "rules[last]");
        return getResultLast(elements, str2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01e6 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0113 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final List<String> getResultLast(Elements elements, String lastRule) {
        String text;
        ArrayList textS = new ArrayList();
        switch (lastRule.hashCode()) {
            case -1055246893:
                if (lastRule.equals("ownText")) {
                    Iterator it = elements.iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        String text2 = element.ownText();
                        Intrinsics.checkNotNullExpressionValue(text2, NCXDocumentV2.NCXTags.text);
                        if (text2.length() > 0) {
                            textS.add(text2);
                        }
                    }
                } else {
                    Iterator it2 = elements.iterator();
                    while (it2.hasNext()) {
                        Element element2 = (Element) it2.next();
                        String url = element2.attr(lastRule);
                        Intrinsics.checkNotNullExpressionValue(url, RSSKeywords.RSS_ITEM_URL);
                        if (!StringsKt.isBlank(url) && !textS.contains(url)) {
                            textS.add(url);
                        }
                    }
                }
                break;
            case -1053421180:
                if (lastRule.equals("textNodes")) {
                    Iterator it3 = elements.iterator();
                    while (it3.hasNext()) {
                        Element element3 = (Element) it3.next();
                        ArrayList tn = new ArrayList();
                        List<TextNode> contentEs = element3.textNodes();
                        for (TextNode item : contentEs) {
                            CharSequence $this$trim$iv = item.text();
                            Intrinsics.checkNotNullExpressionValue($this$trim$iv, "item.text()");
                            CharSequence $this$trim$iv$iv = $this$trim$iv;
                            int startIndex$iv$iv = 0;
                            int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
                            boolean startFound$iv$iv = false;
                            while (startIndex$iv$iv <= endIndex$iv$iv) {
                                int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
                                char it4 = $this$trim$iv$iv.charAt(index$iv$iv);
                                boolean match$iv$iv = Intrinsics.compare(it4, 32) <= 0;
                                if (!startFound$iv$iv) {
                                    if (!match$iv$iv) {
                                        startFound$iv$iv = true;
                                    } else {
                                        startIndex$iv$iv++;
                                    }
                                } else if (!match$iv$iv) {
                                    text = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
                                    if (!(text.length() <= 0)) {
                                        tn.add(text);
                                    }
                                } else {
                                    endIndex$iv$iv--;
                                }
                            }
                            text = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
                            if (!(text.length() <= 0)) {
                            }
                        }
                        if (!tn.isEmpty()) {
                            textS.add(CollectionsKt.joinToString$default(tn, "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
                        }
                    }
                    break;
                }
                break;
            case 96673:
                if (lastRule.equals("all")) {
                    textS.add(elements.outerHtml());
                    break;
                }
                break;
            case 3213227:
                if (lastRule.equals("html")) {
                    elements.select("script").remove();
                    elements.select("style").remove();
                    String html = elements.outerHtml();
                    Intrinsics.checkNotNullExpressionValue(html, "html");
                    if (html.length() > 0) {
                        textS.add(html);
                    }
                    break;
                }
                break;
            case 3556653:
                if (lastRule.equals(NCXDocumentV2.NCXTags.text)) {
                    Iterator it5 = elements.iterator();
                    while (it5.hasNext()) {
                        Element element4 = (Element) it5.next();
                        String text3 = element4.text();
                        Intrinsics.checkNotNullExpressionValue(text3, NCXDocumentV2.NCXTags.text);
                        if (text3.length() > 0) {
                            textS.add(text3);
                        }
                    }
                    break;
                }
                break;
        }
        return textS;
    }

    /* JADX INFO: compiled from: AnalyzeByJSoup.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007¢\u0006\u0002\u0010\nJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007HÆ\u0003J=\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007HÆ\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0005H\u0002J\u0016\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010 \u001a\u00020\u0005J\t\u0010%\u001a\u00020\bHÖ\u0001J\t\u0010&\u001a\u00020\u0005HÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015¨\u0006'"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle;", PackageDocumentBase.PREFIX_OPF, "split", PackageDocumentBase.PREFIX_OPF, "beforeRule", PackageDocumentBase.PREFIX_OPF, "indexDefault", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "indexes", "(CLjava/lang/String;Ljava/util/List;Ljava/util/List;)V", "getBeforeRule", "()Ljava/lang/String;", "setBeforeRule", "(Ljava/lang/String;)V", "getIndexDefault", "()Ljava/util/List;", "getIndexes", "getSplit", "()C", "setSplit", "(C)V", "component1", "component2", "component3", "component4", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "findIndexSet", PackageDocumentBase.PREFIX_OPF, "rule", "getElementsSingle", "Lorg/jsoup/select/Elements;", "temp", "Lorg/jsoup/nodes/Element;", "hashCode", "toString", "reader-pro"})
    public static final /* data */ class ElementsSingle {
        private char split;

        @NotNull
        private String beforeRule;

        @NotNull
        private final List<Integer> indexDefault;

        @NotNull
        private final List<Object> indexes;

        public final char component1() {
            return this.split;
        }

        @NotNull
        public final String component2() {
            return this.beforeRule;
        }

        @NotNull
        public final List<Integer> component3() {
            return this.indexDefault;
        }

        @NotNull
        public final List<Object> component4() {
            return this.indexes;
        }

        @NotNull
        public final ElementsSingle copy(char split, @NotNull String beforeRule, @NotNull List<Integer> indexDefault, @NotNull List<Object> indexes) {
            Intrinsics.checkNotNullParameter(beforeRule, "beforeRule");
            Intrinsics.checkNotNullParameter(indexDefault, "indexDefault");
            Intrinsics.checkNotNullParameter(indexes, "indexes");
            return new ElementsSingle(split, beforeRule, indexDefault, indexes);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ ElementsSingle copy$default(ElementsSingle elementsSingle, char c, String str, List list, List list2, int i, Object obj) {
            if ((i & 1) != 0) {
                c = elementsSingle.split;
            }
            if ((i & 2) != 0) {
                str = elementsSingle.beforeRule;
            }
            if ((i & 4) != 0) {
                list = elementsSingle.indexDefault;
            }
            if ((i & 8) != 0) {
                list2 = elementsSingle.indexes;
            }
            return elementsSingle.copy(c, str, list, list2);
        }

        @NotNull
        public String toString() {
            return "ElementsSingle(split=" + this.split + ", beforeRule=" + this.beforeRule + ", indexDefault=" + this.indexDefault + ", indexes=" + this.indexes + ')';
        }

        public int hashCode() {
            int result = Character.hashCode(this.split);
            return (((((result * 31) + this.beforeRule.hashCode()) * 31) + this.indexDefault.hashCode()) * 31) + this.indexes.hashCode();
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ElementsSingle)) {
                return false;
            }
            ElementsSingle elementsSingle = (ElementsSingle) other;
            return this.split == elementsSingle.split && Intrinsics.areEqual(this.beforeRule, elementsSingle.beforeRule) && Intrinsics.areEqual(this.indexDefault, elementsSingle.indexDefault) && Intrinsics.areEqual(this.indexes, elementsSingle.indexes);
        }

        public ElementsSingle() {
            this((char) 0, null, null, null, 15, null);
        }

        public ElementsSingle(char split, @NotNull String beforeRule, @NotNull List<Integer> indexDefault, @NotNull List<Object> indexes) {
            Intrinsics.checkNotNullParameter(beforeRule, "beforeRule");
            Intrinsics.checkNotNullParameter(indexDefault, "indexDefault");
            Intrinsics.checkNotNullParameter(indexes, "indexes");
            this.split = split;
            this.beforeRule = beforeRule;
            this.indexDefault = indexDefault;
            this.indexes = indexes;
        }

        public /* synthetic */ ElementsSingle(char c, String str, List list, List list2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? '.' : c, (i & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i & 4) != 0 ? new ArrayList() : list, (i & 8) != 0 ? new ArrayList() : list2);
        }

        public final char getSplit() {
            return this.split;
        }

        public final void setSplit(char c) {
            this.split = c;
        }

        @NotNull
        public final String getBeforeRule() {
            return this.beforeRule;
        }

        public final void setBeforeRule(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.beforeRule = str;
        }

        @NotNull
        public final List<Integer> getIndexDefault() {
            return this.indexDefault;
        }

        @NotNull
        public final List<Object> getIndexes() {
            return this.indexes;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0132  */
        @NotNull
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Elements getElementsSingle(@NotNull Element temp, @NotNull String rule) {
            Elements elementsSelect;
            Intrinsics.checkNotNullParameter(temp, "temp");
            Intrinsics.checkNotNullParameter(rule, "rule");
            findIndexSet(rule);
            if (!(this.beforeRule.length() == 0)) {
                List rules = StringsKt.split$default(this.beforeRule, new String[]{"."}, false, 0, 6, (Object) null);
                String str = (String) rules.get(0);
                switch (str.hashCode()) {
                    case 3355:
                        elementsSelect = !str.equals("id") ? temp.select(this.beforeRule) : Collector.collect(new Evaluator.Id((String) rules.get(1)), temp);
                        break;
                    case 114586:
                        if (str.equals("tag")) {
                            elementsSelect = temp.getElementsByTag((String) rules.get(1));
                            break;
                        }
                        break;
                    case 3556653:
                        if (str.equals(NCXDocumentV2.NCXTags.text)) {
                            elementsSelect = temp.getElementsContainingOwnText((String) rules.get(1));
                            break;
                        }
                        break;
                    case 94742904:
                        if (str.equals(NCXDocumentV2.NCXAttributes.clazz)) {
                            elementsSelect = temp.getElementsByClass((String) rules.get(1));
                            break;
                        }
                        break;
                    case 1659526655:
                        if (str.equals("children")) {
                            elementsSelect = temp.children();
                            break;
                        }
                        break;
                }
            } else {
                elementsSelect = temp.children();
            }
            Elements elements = elementsSelect;
            int len = elements.size();
            Integer numValueOf = Integer.valueOf(this.indexDefault.size() - 1);
            Integer num = numValueOf.intValue() != -1 ? numValueOf : null;
            int lastIndexes = num == null ? this.indexes.size() - 1 : num.intValue();
            Set indexSet = new LinkedHashSet();
            if (this.indexes.isEmpty()) {
                int i = lastIndexes;
                if (0 <= i) {
                    do {
                        int ix = i;
                        i--;
                        int it = this.indexDefault.get(ix).intValue();
                        boolean z = 0 <= it && it < len;
                        if (z) {
                            indexSet.add(Integer.valueOf(it));
                        } else if (it < 0 && len >= (-it)) {
                            indexSet.add(Integer.valueOf(it + len));
                        }
                    } while (0 <= i);
                }
            } else {
                int i2 = lastIndexes;
                if (0 <= i2) {
                    do {
                        int ix2 = i2;
                        i2--;
                        if (this.indexes.get(ix2) instanceof Triple) {
                            Triple triple = (Triple) this.indexes.get(ix2);
                            Integer startX = (Integer) triple.component1();
                            Integer endX = (Integer) triple.component2();
                            int stepX = ((Number) triple.component3()).intValue();
                            int start = startX == null ? 0 : startX.intValue() >= 0 ? startX.intValue() < len ? startX.intValue() : len - 1 : (-startX.intValue()) <= len ? len + startX.intValue() : 0;
                            int end = endX == null ? len - 1 : endX.intValue() >= 0 ? endX.intValue() < len ? endX.intValue() : len - 1 : (-endX.intValue()) <= len ? len + endX.intValue() : 0;
                            if (start == end || stepX >= len) {
                                indexSet.add(Integer.valueOf(start));
                            } else {
                                int step = stepX > 0 ? stepX : (-stepX) < len ? stepX + len : 1;
                                CollectionsKt.addAll(indexSet, (Iterable) (end > start ? RangesKt.step(new IntRange(start, end), step) : RangesKt.step(RangesKt.downTo(start, end), step)));
                            }
                        } else {
                            int it2 = ((Integer) this.indexes.get(ix2)).intValue();
                            boolean z2 = 0 <= it2 && it2 < len;
                            if (z2) {
                                indexSet.add(Integer.valueOf(it2));
                            } else if (it2 < 0 && len >= (-it2)) {
                                indexSet.add(Integer.valueOf(it2 + len));
                            }
                        }
                    } while (0 <= i2);
                }
            }
            if (this.split == '!') {
                Iterator it3 = indexSet.iterator();
                while (it3.hasNext()) {
                    int pcInt = ((Number) it3.next()).intValue();
                    elements.set(pcInt, (Object) null);
                }
                elements.removeAll(CollectionsKt.listOf((Object) null));
            } else if (this.split == '.') {
                Elements es = new Elements();
                Iterator it4 = indexSet.iterator();
                while (it4.hasNext()) {
                    int pcInt2 = ((Number) it4.next()).intValue();
                    es.add(elements.get(pcInt2));
                }
                elements = es;
            }
            Elements elements2 = elements;
            Intrinsics.checkNotNullExpressionValue(elements2, "elements");
            return elements2;
        }

        private final void findIndexSet(String rule) {
            Integer numValueOf;
            String $this$trim$iv$iv = rule;
            int startIndex$iv$iv = 0;
            int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
            boolean startFound$iv$iv = false;
            while (startIndex$iv$iv <= endIndex$iv$iv) {
                int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
                char it = $this$trim$iv$iv.charAt(index$iv$iv);
                boolean match$iv$iv = Intrinsics.compare(it, 32) <= 0;
                if (!startFound$iv$iv) {
                    if (!match$iv$iv) {
                        startFound$iv$iv = true;
                    } else {
                        startIndex$iv$iv++;
                    }
                } else if (!match$iv$iv) {
                    break;
                } else {
                    endIndex$iv$iv--;
                }
            }
            String rus = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
            int len = rus.length();
            boolean curMinus = false;
            List curList = new ArrayList();
            String l = PackageDocumentBase.PREFIX_OPF;
            boolean head = StringsKt.last(rus) == ']';
            if (head) {
                int len2 = len - 1;
                while (true) {
                    int i = len2;
                    len2 = i - 1;
                    if (i < 0) {
                        break;
                    }
                    char rl = rus.charAt(len2);
                    if (rl != ' ') {
                        boolean z = '0' <= rl && rl <= '9';
                        if (z) {
                            l = String.valueOf(rl) + l;
                        } else if (rl == '-') {
                            curMinus = true;
                        } else {
                            if (l.length() == 0) {
                                numValueOf = null;
                            } else {
                                numValueOf = curMinus ? Integer.valueOf(-Integer.parseInt(l)) : Integer.valueOf(Integer.parseInt(l));
                            }
                            Integer curInt = numValueOf;
                            if (rl == ':') {
                                curList.add(curInt);
                            } else {
                                if (curList.isEmpty()) {
                                    if (curInt == null) {
                                        break;
                                    } else {
                                        this.indexes.add(curInt);
                                    }
                                } else {
                                    this.indexes.add(new Triple(curInt, CollectionsKt.last(curList), curList.size() == 2 ? (Integer) CollectionsKt.first(curList) : 1));
                                    curList.clear();
                                }
                                if (rl == '!') {
                                    this.split = '!';
                                    do {
                                        len2--;
                                        rl = rus.charAt(len2);
                                        if (len2 <= 0) {
                                            break;
                                        }
                                    } while (rl == ' ');
                                }
                                if (rl == '[') {
                                    if (rus == null) {
                                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                                    }
                                    String strSubstring = rus.substring(0, len2);
                                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                                    this.beforeRule = strSubstring;
                                    return;
                                }
                                if (rl != ',') {
                                    break;
                                }
                            }
                            l = PackageDocumentBase.PREFIX_OPF;
                            curMinus = false;
                        }
                    }
                }
            } else {
                while (true) {
                    int i2 = len;
                    len = i2 - 1;
                    if (i2 < 0) {
                        break;
                    }
                    char rl2 = rus.charAt(len);
                    if (rl2 != ' ') {
                        boolean z2 = '0' <= rl2 && rl2 <= '9';
                        if (z2) {
                            l = String.valueOf(rl2) + l;
                        } else if (rl2 != '-') {
                            if (rl2 != '!' && rl2 != '.' && rl2 != ':') {
                                break;
                            }
                            this.indexDefault.add(Integer.valueOf(curMinus ? -Integer.parseInt(l) : Integer.parseInt(l)));
                            if (rl2 != ':') {
                                this.split = rl2;
                                if (rus == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                                }
                                String strSubstring2 = rus.substring(0, len);
                                Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                                this.beforeRule = strSubstring2;
                                return;
                            }
                            l = PackageDocumentBase.PREFIX_OPF;
                            curMinus = false;
                        } else {
                            curMinus = true;
                        }
                    }
                }
            }
            this.split = ' ';
            this.beforeRule = rus;
        }
    }

    /* JADX INFO: compiled from: AnalyzeByJSoup.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByJSoup$SourceRule.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0080\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByJSoup$SourceRule;", PackageDocumentBase.PREFIX_OPF, "ruleStr", PackageDocumentBase.PREFIX_OPF, "(Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;Ljava/lang/String;)V", "elementsRule", "getElementsRule", "()Ljava/lang/String;", "setElementsRule", "(Ljava/lang/String;)V", "isCss", PackageDocumentBase.PREFIX_OPF, "()Z", "setCss", "(Z)V", "reader-pro"})
    public final class SourceRule {
        private boolean isCss;

        @NotNull
        private String elementsRule;
        final /* synthetic */ AnalyzeByJSoup this$0;

        public SourceRule(@NotNull AnalyzeByJSoup this$0, String ruleStr) {
            String str;
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(ruleStr, "ruleStr");
            this.this$0 = this$0;
            SourceRule sourceRule = this;
            if (StringsKt.startsWith(ruleStr, "@CSS:", true)) {
                this.isCss = true;
                CharSequence $this$trim$iv = ruleStr.substring(5);
                Intrinsics.checkNotNullExpressionValue($this$trim$iv, "(this as java.lang.String).substring(startIndex)");
                CharSequence $this$trim$iv$iv = $this$trim$iv;
                int startIndex$iv$iv = 0;
                int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
                boolean startFound$iv$iv = false;
                while (startIndex$iv$iv <= endIndex$iv$iv) {
                    int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
                    char it = $this$trim$iv$iv.charAt(index$iv$iv);
                    boolean match$iv$iv = Intrinsics.compare(it, 32) <= 0;
                    if (!startFound$iv$iv) {
                        if (!match$iv$iv) {
                            startFound$iv$iv = true;
                        } else {
                            startIndex$iv$iv++;
                        }
                    } else if (!match$iv$iv) {
                        break;
                    } else {
                        endIndex$iv$iv--;
                    }
                }
                String string = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
                sourceRule = sourceRule;
                str = string;
            } else {
                str = ruleStr;
            }
            sourceRule.elementsRule = str;
        }

        public final boolean isCss() {
            return this.isCss;
        }

        public final void setCss(boolean z) {
            this.isCss = z;
        }

        @NotNull
        public final String getElementsRule() {
            return this.elementsRule;
        }

        public final void setElementsRule(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.elementsRule = str;
        }
    }
}
