// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import kotlin.ranges.RangesKt;
import kotlin.ranges.IntRange;
import kotlin.ranges.IntProgression;
import kotlin.Triple;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jsoup.select.Collector;
import org.jsoup.select.Evaluator$Id;
import org.jsoup.select.Evaluator;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.seimicrawler.xpath.JXNode;
import org.jsoup.nodes.TextNode;
import java.util.Iterator;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.collections.CollectionsKt;
import java.util.Collection;
import org.jsoup.select.Elements;
import kotlin.jvm.internal.Intrinsics;
import org.jsoup.nodes.Element;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000e\u0018\u0000 \u00182\u00020\u0001:\u0003\u0018\u0019\u001aB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001?\u0006\u0002\u0010\u0003J\u0015\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0000?\u0006\u0002\b\nJ\u001a\u0010\u0006\u001a\u00020\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u0002J\u001e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\tH\u0002J\u0018\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\r2\u0006\u0010\u0011\u001a\u00020\tH\u0002J\u0017\u0010\u0012\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0011\u001a\u00020\tH\u0000?\u0006\u0002\b\u0013J\u0015\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tH\u0000?\u0006\u0002\b\u0015J\u001b\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\r2\u0006\u0010\u0011\u001a\u00020\tH\u0000?\u0006\u0002\b\u0017R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e?\u0006\u0002\n\u0000：\u0006\u001b" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;", "", "doc", "(Ljava/lang/Object;)V", "element", "Lorg/jsoup/nodes/Element;", "getElements", "Lorg/jsoup/select/Elements;", "rule", "", "getElements$reader_pro", "temp", "getResultLast", "", "elements", "lastRule", "getResultList", "ruleStr", "getString", "getString$reader_pro", "getString0", "getString0$reader_pro", "getStringList", "getStringList$reader_pro", "Companion", "ElementsSingle", "SourceRule", "reader-pro" })
public final class AnalyzeByJSoup
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private Element element;
    @NotNull
    private static final String[] validKeys;
    
    public AnalyzeByJSoup(@NotNull final Object doc) {
        Intrinsics.checkNotNullParameter(doc, "doc");
        this.element = AnalyzeByJSoup.Companion.parse(doc);
    }
    
    @NotNull
    public final Elements getElements$reader_pro(@NotNull final String rule) {
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        return this.getElements(this.element, rule);
    }
    
    @Nullable
    public final String getString$reader_pro(@NotNull final String ruleStr) {
        Intrinsics.checkNotNullParameter((Object)ruleStr, "ruleStr");
        String s;
        if (ruleStr.length() == 0) {
            s = null;
        }
        else {
            final List it;
            final List list = it = this.getStringList$reader_pro(ruleStr);
            final int n = 0;
            final List list2 = it.isEmpty() ? null : list;
            s = ((list2 == null) ? null : CollectionsKt.joinToString$default((Iterable)list2, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null));
        }
        return s;
    }
    
    @NotNull
    public final String getString0$reader_pro(@NotNull final String ruleStr) {
        Intrinsics.checkNotNullParameter((Object)ruleStr, "ruleStr");
        final List it = this.getStringList$reader_pro(ruleStr);
        final int n = 0;
        return it.isEmpty() ? "" : it.get(0);
    }
    
    @NotNull
    public final List<String> getStringList$reader_pro(@NotNull final String ruleStr) {
        Intrinsics.checkNotNullParameter((Object)ruleStr, "ruleStr");
        final ArrayList textS = new ArrayList();
        if (ruleStr.length() == 0) {
            return textS;
        }
        final SourceRule sourceRule = new SourceRule(ruleStr);
        if (sourceRule.getElementsRule().length() == 0) {
            final ArrayList list = textS;
            final String data = this.element.data();
            list.add((data == null) ? "" : data);
        }
        else {
            final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(sourceRule.getElementsRule(), false, 2, null);
            final ArrayList ruleStrS = ruleAnalyzes.splitRule("&&", "||", "%%");
            final ArrayList results = new ArrayList();
            for (final String ruleStrX : ruleStrS) {
                List<String> list2;
                if (sourceRule.isCss()) {
                    Intrinsics.checkNotNullExpressionValue((Object)ruleStrX, "ruleStrX");
                    final int lastIndex = StringsKt.lastIndexOf$default((CharSequence)ruleStrX, '@', 0, false, 6, (Object)null);
                    final Element element = this.element;
                    final String substring = ruleStrX.substring(0, lastIndex);
                    Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    final Elements select = element.select(substring);
                    Intrinsics.checkNotNullExpressionValue((Object)select, "element.select(ruleStrX.substring(0, lastIndex))");
                    final Elements elements = select;
                    final String substring2 = ruleStrX.substring(lastIndex + 1);
                    Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.String).substring(startIndex)");
                    list2 = this.getResultLast(elements, substring2);
                }
                else {
                    Intrinsics.checkNotNullExpressionValue((Object)ruleStrX, "ruleStrX");
                    list2 = this.getResultList(ruleStrX);
                }
                final List temp = list2;
                final Collection collection = temp;
                if (collection != null && !collection.isEmpty()) {
                    results.add(temp);
                    if (Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
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
                                    textS.add(temp2.get(i));
                                }
                            }
                        } while (j <= n);
                    }
                }
                else {
                    for (final List temp3 : results) {
                        textS.addAll(temp3);
                    }
                }
            }
        }
        return textS;
    }
    
    private final Elements getElements(final Element temp, final String rule) {
        if (temp == null || rule.length() == 0) {
            return new Elements();
        }
        final Elements elements = new Elements();
        final SourceRule sourceRule = new SourceRule(rule);
        final RuleAnalyzer ruleAnalyzes = new RuleAnalyzer(sourceRule.getElementsRule(), false, 2, null);
        final ArrayList ruleStrS = ruleAnalyzes.splitRule("&&", "||", "%%");
        final ArrayList elementsList = new ArrayList();
        if (sourceRule.isCss()) {
            for (final String ruleStr : ruleStrS) {
                final Elements tempS = temp.select(ruleStr);
                elementsList.add(tempS);
                if (tempS.size() > 0 && Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                    break;
                }
            }
        }
        else {
            for (final String ruleStr : ruleStrS) {
                Intrinsics.checkNotNullExpressionValue((Object)ruleStr, "ruleStr");
                final RuleAnalyzer rsRule = new RuleAnalyzer(ruleStr, false, 2, null);
                rsRule.trim();
                final ArrayList rs = rsRule.splitRule("@");
                Elements elementsSingle;
                if (rs.size() > 1) {
                    final Elements el = new Elements();
                    el.add((Object)temp);
                    for (final String rl : rs) {
                        final Elements es = new Elements();
                        for (final Element et : el) {
                            final Elements elements2 = es;
                            final Element temp2 = et;
                            Intrinsics.checkNotNullExpressionValue((Object)rl, "rl");
                            elements2.addAll((Collection)this.getElements(temp2, rl));
                        }
                        el.clear();
                        el.addAll((Collection)es);
                    }
                    elementsSingle = el;
                }
                else {
                    elementsSingle = new ElementsSingle('\0', null, null, null, 15, null).getElementsSingle(temp, ruleStr);
                }
                final Elements el2 = elementsSingle;
                elementsList.add(el2);
                if (el2.size() > 0 && Intrinsics.areEqual((Object)ruleAnalyzes.getElementsType(), (Object)"||")) {
                    break;
                }
            }
        }
        if (elementsList.size() > 0) {
            if (Intrinsics.areEqual((Object)"%%", (Object)ruleAnalyzes.getElementsType())) {
                int j = 0;
                final int size = elementsList.get(0).size();
                if (j < size) {
                    do {
                        final int i = j;
                        ++j;
                        for (final Elements es2 : elementsList) {
                            if (i < es2.size()) {
                                elements.add(es2.get(i));
                            }
                        }
                    } while (j < size);
                }
            }
            else {
                for (final Elements es3 : elementsList) {
                    elements.addAll((Collection)es3);
                }
            }
        }
        return elements;
    }
    
    private final List<String> getResultList(final String ruleStr) {
        if (ruleStr.length() == 0) {
            return null;
        }
        Elements elements = new Elements();
        elements.add((Object)this.element);
        final RuleAnalyzer rule = new RuleAnalyzer(ruleStr, false, 2, null);
        rule.trim();
        final ArrayList rules = rule.splitRule("@");
        final int last = rules.size() - 1;
        int j = 0;
        if (j < last) {
            do {
                final int i = j;
                ++j;
                final Elements es = new Elements();
                for (final Element elt : elements) {
                    final Elements elements2 = es;
                    final ElementsSingle elementsSingle = new ElementsSingle('\0', null, null, null, 15, null);
                    Intrinsics.checkNotNullExpressionValue((Object)elt, "elt");
                    final Element temp = elt;
                    final String value = rules.get(i);
                    Intrinsics.checkNotNullExpressionValue((Object)value, "rules[i]");
                    elements2.addAll((Collection)elementsSingle.getElementsSingle(temp, value));
                }
                elements.clear();
                elements = es;
            } while (j < last);
        }
        List<String> resultLast;
        if (elements.isEmpty()) {
            resultLast = null;
        }
        else {
            final Elements elements3 = elements;
            final String value2 = rules.get(last);
            Intrinsics.checkNotNullExpressionValue((Object)value2, "rules[last]");
            resultLast = this.getResultLast(elements3, value2);
        }
        return resultLast;
    }
    
    private final List<String> getResultLast(final Elements elements, final String lastRule) {
        final ArrayList textS = new ArrayList();
        switch (lastRule) {
            case "all": {
                textS.add(elements.outerHtml());
                return textS;
            }
            case "textNodes": {
                for (final Element element : elements) {
                    final ArrayList tn = new ArrayList();
                    final List contentEs = element.textNodes();
                    for (final TextNode item : contentEs) {
                        final String text3 = item.text();
                        Intrinsics.checkNotNullExpressionValue((Object)text3, "item.text()");
                        final String $this$trim$iv = text3;
                        final int $i$f$trim = 0;
                        final CharSequence $this$trim$iv$iv = $this$trim$iv;
                        final int $i$f$trim2 = 0;
                        int startIndex$iv$iv = 0;
                        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
                        boolean startFound$iv$iv = false;
                        while (startIndex$iv$iv <= endIndex$iv$iv) {
                            final int index$iv$iv = startFound$iv$iv ? endIndex$iv$iv : startIndex$iv$iv;
                            final char it = $this$trim$iv$iv.charAt(index$iv$iv);
                            final int n = 0;
                            final boolean match$iv$iv = Intrinsics.compare((int)it, 32) <= 0;
                            if (!startFound$iv$iv) {
                                if (!match$iv$iv) {
                                    startFound$iv$iv = true;
                                }
                                else {
                                    ++startIndex$iv$iv;
                                }
                            }
                            else {
                                if (!match$iv$iv) {
                                    break;
                                }
                                --endIndex$iv$iv;
                            }
                        }
                        final String text = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
                        if (text.length() > 0) {
                            tn.add(text);
                        }
                    }
                    if (!tn.isEmpty()) {
                        textS.add(CollectionsKt.joinToString$default((Iterable)tn, (CharSequence)"\n", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null));
                    }
                }
                return textS;
            }
            case "ownText": {
                for (final Element element : elements) {
                    final String text2 = element.ownText();
                    Intrinsics.checkNotNullExpressionValue((Object)text2, "text");
                    if (text2.length() > 0) {
                        textS.add(text2);
                    }
                }
                return textS;
            }
            case "html": {
                elements.select("script").remove();
                elements.select("style").remove();
                final String html = elements.outerHtml();
                Intrinsics.checkNotNullExpressionValue((Object)html, "html");
                if (html.length() > 0) {
                    textS.add(html);
                    return textS;
                }
                return textS;
            }
            case "text": {
                for (final Element element : elements) {
                    final String text2 = element.text();
                    Intrinsics.checkNotNullExpressionValue((Object)text2, "text");
                    if (text2.length() > 0) {
                        textS.add(text2);
                    }
                }
                return textS;
            }
            default:
                break;
        }
        for (final Element element : elements) {
            final String url = element.attr(lastRule);
            Intrinsics.checkNotNullExpressionValue((Object)url, "url");
            if (!StringsKt.isBlank((CharSequence)url)) {
                if (textS.contains(url)) {
                    continue;
                }
                textS.add(url);
            }
        }
        return textS;
    }
    
    public static final /* synthetic */ String[] access$getValidKeys$cp() {
        return AnalyzeByJSoup.validKeys;
    }
    
    static {
        Companion = new Companion(null);
        validKeys = new String[] { "class", "id", "tag", "text", "children" };
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0001R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004?\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007：\u0006\f" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByJSoup$Companion;", "", "()V", "validKeys", "", "", "getValidKeys", "()[Ljava/lang/String;", "[Ljava/lang/String;", "parse", "Lorg/jsoup/nodes/Element;", "doc", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final String[] getValidKeys() {
            return AnalyzeByJSoup.access$getValidKeys$cp();
        }
        
        @NotNull
        public final Element parse(@NotNull final Object doc) {
            Intrinsics.checkNotNullParameter(doc, "doc");
            Element element;
            if (doc instanceof Element) {
                element = (Element)doc;
            }
            else if (doc instanceof JXNode) {
                final Element element2 = (Element)(((JXNode)doc).isElement() ? ((JXNode)doc).asElement() : Jsoup.parse(doc.toString()));
                Intrinsics.checkNotNullExpressionValue((Object)element2, "if (doc.isElement) doc.asElement() else Jsoup.parse(doc.toString())");
                element = element2;
            }
            else {
                final Document parse = Jsoup.parse(doc.toString());
                Intrinsics.checkNotNullExpressionValue((Object)parse, "parse(doc.toString())");
                element = (Element)parse;
            }
            return element;
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007?\u0006\u0002\u0010\nJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u00c6\u0003J=\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0005H\u0002J\u0016\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010 \u001a\u00020\u0005J\t\u0010%\u001a\u00020\bH\u00d6\u0001J\t\u0010&\u001a\u00020\u0005H\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007?\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015：\u0006'" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByJSoup$ElementsSingle;", "", "split", "", "beforeRule", "", "indexDefault", "", "", "indexes", "(CLjava/lang/String;Ljava/util/List;Ljava/util/List;)V", "getBeforeRule", "()Ljava/lang/String;", "setBeforeRule", "(Ljava/lang/String;)V", "getIndexDefault", "()Ljava/util/List;", "getIndexes", "getSplit", "()C", "setSplit", "(C)V", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "findIndexSet", "", "rule", "getElementsSingle", "Lorg/jsoup/select/Elements;", "temp", "Lorg/jsoup/nodes/Element;", "hashCode", "toString", "reader-pro" })
    public static final class ElementsSingle
    {
        private char split;
        @NotNull
        private String beforeRule;
        @NotNull
        private final List<Integer> indexDefault;
        @NotNull
        private final List<Object> indexes;
        
        public ElementsSingle(final char split, @NotNull final String beforeRule, @NotNull final List<Integer> indexDefault, @NotNull final List<Object> indexes) {
            Intrinsics.checkNotNullParameter((Object)beforeRule, "beforeRule");
            Intrinsics.checkNotNullParameter((Object)indexDefault, "indexDefault");
            Intrinsics.checkNotNullParameter((Object)indexes, "indexes");
            this.split = split;
            this.beforeRule = beforeRule;
            this.indexDefault = indexDefault;
            this.indexes = indexes;
        }
        
        public final char getSplit() {
            return this.split;
        }
        
        public final void setSplit(final char <set-?>) {
            this.split = <set-?>;
        }
        
        @NotNull
        public final String getBeforeRule() {
            return this.beforeRule;
        }
        
        public final void setBeforeRule(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.beforeRule = <set-?>;
        }
        
        @NotNull
        public final List<Integer> getIndexDefault() {
            return this.indexDefault;
        }
        
        @NotNull
        public final List<Object> getIndexes() {
            return this.indexes;
        }
        
        @NotNull
        public final Elements getElementsSingle(@NotNull final Element temp, @NotNull final String rule) {
            Intrinsics.checkNotNullParameter((Object)temp, "temp");
            Intrinsics.checkNotNullParameter((Object)rule, "rule");
            this.findIndexSet(rule);
            Elements elements2 = null;
            Label_0314: {
                if (this.beforeRule.length() == 0) {
                    elements2 = temp.children();
                }
                else {
                    final List rules = StringsKt.split$default((CharSequence)this.beforeRule, new String[] { "." }, false, 0, 6, (Object)null);
                    final String s = rules.get(0);
                    switch (s) {
                        case "children": {
                            elements2 = temp.children();
                            break Label_0314;
                        }
                        case "tag": {
                            elements2 = temp.getElementsByTag((String)rules.get(1));
                            break Label_0314;
                        }
                        case "id": {
                            elements2 = Collector.collect((Evaluator)new Evaluator$Id((String)rules.get(1)), temp);
                            break Label_0314;
                        }
                        case "text": {
                            elements2 = temp.getElementsContainingOwnText((String)rules.get(1));
                            break Label_0314;
                        }
                        case "class": {
                            elements2 = temp.getElementsByClass((String)rules.get(1));
                            break Label_0314;
                        }
                        default:
                            break;
                    }
                    elements2 = temp.select(this.beforeRule);
                }
            }
            Elements elements = elements2;
            final int len = elements.size();
            final Integer value = this.indexDefault.size() - 1;
            final int it = value.intValue();
            final int n = 0;
            final Integer n2 = (it != -1) ? value : null;
            final int lastIndexes = (n2 == null) ? (this.indexes.size() - 1) : n2;
            final Set indexSet = new LinkedHashSet();
            if (this.indexes.isEmpty()) {
                int n3 = lastIndexes;
                if (0 <= n3) {
                    do {
                        final int ix = n3;
                        --n3;
                        final int it2 = this.indexDefault.get(ix).intValue();
                        if (0 <= it2 && it2 < len) {
                            indexSet.add(it2);
                        }
                        else {
                            if (it2 >= 0 || len < -it2) {
                                continue;
                            }
                            indexSet.add(it2 + len);
                        }
                    } while (0 <= n3);
                }
            }
            else {
                int n4 = lastIndexes;
                if (0 <= n4) {
                    do {
                        final int ix = n4;
                        --n4;
                        if (this.indexes.get(ix) instanceof Triple) {
                            final Triple triple = this.indexes.get(ix);
                            final Integer startX = (Integer)triple.component1();
                            final Integer endX = (Integer)triple.component2();
                            final int stepX = ((Number)triple.component3()).intValue();
                            final int start = (startX == null) ? 0 : ((startX >= 0) ? ((startX < len) ? startX : (len - 1)) : ((-startX <= len) ? (len + startX) : 0));
                            final int end = (endX == null) ? (len - 1) : ((endX >= 0) ? ((endX < len) ? endX : (len - 1)) : ((-endX <= len) ? (len + endX) : 0));
                            if (start == end || stepX >= len) {
                                indexSet.add(start);
                            }
                            else {
                                final int step = (stepX > 0) ? stepX : ((-stepX < len) ? (stepX + len) : 1);
                                CollectionsKt.addAll((Collection)indexSet, (Iterable)((end > start) ? RangesKt.step((IntProgression)new IntRange(start, end), step) : RangesKt.step(RangesKt.downTo(start, end), step)));
                            }
                        }
                        else {
                            final int it2 = this.indexes.get(ix);
                            if (0 <= it2 && it2 < len) {
                                indexSet.add(it2);
                            }
                            else {
                                if (it2 >= 0 || len < -it2) {
                                    continue;
                                }
                                indexSet.add(it2 + len);
                            }
                        }
                    } while (0 <= n4);
                }
            }
            if (this.split == '!') {
                final Iterator iterator = indexSet.iterator();
                while (iterator.hasNext()) {
                    final int pcInt = ((Number)iterator.next()).intValue();
                    elements.set(pcInt, (Object)null);
                }
                elements.removeAll((Collection)CollectionsKt.listOf((Object)null));
            }
            else if (this.split == '.') {
                final Elements es = new Elements();
                final Iterator iterator2 = indexSet.iterator();
                while (iterator2.hasNext()) {
                    final int pcInt2 = ((Number)iterator2.next()).intValue();
                    es.add(elements.get(pcInt2));
                }
                elements = es;
            }
            final Elements elements3 = elements;
            Intrinsics.checkNotNullExpressionValue((Object)elements3, "elements");
            return elements3;
        }
        
        private final void findIndexSet(final String rule) {
            final String $this$trim$iv = rule;
            final int $i$f$trim = 0;
            final CharSequence $this$trim$iv$iv = $this$trim$iv;
            final int $i$f$trim2 = 0;
            int startIndex$iv$iv = 0;
            int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
            boolean startFound$iv$iv = false;
            while (startIndex$iv$iv <= endIndex$iv$iv) {
                final int index$iv$iv = startFound$iv$iv ? endIndex$iv$iv : startIndex$iv$iv;
                final char it = $this$trim$iv$iv.charAt(index$iv$iv);
                final int n = 0;
                final boolean match$iv$iv = Intrinsics.compare((int)it, 32) <= 0;
                if (!startFound$iv$iv) {
                    if (!match$iv$iv) {
                        startFound$iv$iv = true;
                    }
                    else {
                        ++startIndex$iv$iv;
                    }
                }
                else {
                    if (!match$iv$iv) {
                        break;
                    }
                    --endIndex$iv$iv;
                }
            }
            final String rus = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
            int len = rus.length();
            Integer curInt = null;
            boolean curMinus = false;
            final List curList = new ArrayList();
            String l = "";
            final boolean head = StringsKt.last((CharSequence)rus) == ']';
            if (head) {
                --len;
                while (true) {
                    final int n2 = len;
                    len = n2 - 1;
                    if (n2 < 0) {
                        break;
                    }
                    char rl = rus.charAt(len);
                    if (rl == ' ') {
                        continue;
                    }
                    if ('0' <= rl && rl <= '9') {
                        l = String.valueOf(rl) + l;
                    }
                    else if (rl == '-') {
                        curMinus = true;
                    }
                    else {
                        curInt = ((l.length() == 0) ? null : (curMinus ? (-Integer.parseInt(l)) : Integer.parseInt(l)));
                        if (rl == ':') {
                            curList.add(curInt);
                        }
                        else {
                            if (curList.isEmpty()) {
                                if (curInt == null) {
                                    break;
                                }
                                this.indexes.add(curInt);
                            }
                            else {
                                this.indexes.add(new Triple((Object)curInt, CollectionsKt.last(curList), (curList.size() == 2) ? CollectionsKt.first(curList) : Integer.valueOf(1)));
                                curList.clear();
                            }
                            if (rl == '!') {
                                this.split = '!';
                                do {
                                    rl = rus.charAt(--len);
                                } while (len > 0 && rl == ' ');
                            }
                            if (rl == '[') {
                                final String s = rus;
                                final int beginIndex = 0;
                                final String s2 = s;
                                if (s2 == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                                }
                                final String substring = s2.substring(beginIndex, len);
                                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                                this.beforeRule = substring;
                                return;
                            }
                            else if (rl != ',') {
                                break;
                            }
                        }
                        l = "";
                        curMinus = false;
                    }
                }
            }
            else {
                while (true) {
                    final int n3 = len;
                    len = n3 - 1;
                    if (n3 < 0) {
                        break;
                    }
                    final char rl = rus.charAt(len);
                    if (rl == ' ') {
                        continue;
                    }
                    if ('0' <= rl && rl <= '9') {
                        l = String.valueOf(rl) + l;
                    }
                    else if (rl == '-') {
                        curMinus = true;
                    }
                    else {
                        if (rl != '!' && rl != '.' && rl != ':') {
                            break;
                        }
                        this.indexDefault.add(curMinus ? (-Integer.parseInt(l)) : Integer.parseInt(l));
                        if (rl != ':') {
                            this.split = rl;
                            final String s3 = rus;
                            final int beginIndex2 = 0;
                            final String s4 = s3;
                            if (s4 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            final String substring2 = s4.substring(beginIndex2, len);
                            Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                            this.beforeRule = substring2;
                            return;
                        }
                        else {
                            l = "";
                            curMinus = false;
                        }
                    }
                }
            }
            this.split = ' ';
            this.beforeRule = rus;
        }
        
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
        public final ElementsSingle copy(final char split, @NotNull final String beforeRule, @NotNull final List<Integer> indexDefault, @NotNull final List<Object> indexes) {
            Intrinsics.checkNotNullParameter((Object)beforeRule, "beforeRule");
            Intrinsics.checkNotNullParameter((Object)indexDefault, "indexDefault");
            Intrinsics.checkNotNullParameter((Object)indexes, "indexes");
            return new ElementsSingle(split, beforeRule, indexDefault, indexes);
        }
        
        @NotNull
        @Override
        public String toString() {
            return "ElementsSingle(split=" + this.split + ", beforeRule=" + this.beforeRule + ", indexDefault=" + this.indexDefault + ", indexes=" + this.indexes + ')';
        }
        
        @Override
        public int hashCode() {
            int result = Character.hashCode(this.split);
            result = result * 31 + this.beforeRule.hashCode();
            result = result * 31 + this.indexDefault.hashCode();
            result = result * 31 + this.indexes.hashCode();
            return result;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ElementsSingle)) {
                return false;
            }
            final ElementsSingle elementsSingle = (ElementsSingle)other;
            return this.split == elementsSingle.split && Intrinsics.areEqual((Object)this.beforeRule, (Object)elementsSingle.beforeRule) && Intrinsics.areEqual((Object)this.indexDefault, (Object)elementsSingle.indexDefault) && Intrinsics.areEqual((Object)this.indexes, (Object)elementsSingle.indexes);
        }
        
        public ElementsSingle() {
            this('\0', null, null, null, 15, null);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0080\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000e：\u0006\u000f" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByJSoup$SourceRule;", "", "ruleStr", "", "(Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;Ljava/lang/String;)V", "elementsRule", "getElementsRule", "()Ljava/lang/String;", "setElementsRule", "(Ljava/lang/String;)V", "isCss", "", "()Z", "setCss", "(Z)V", "reader-pro" })
    public final class SourceRule
    {
        private boolean isCss;
        @NotNull
        private String elementsRule;
        
        public SourceRule(@NotNull final AnalyzeByJSoup this$0, final String ruleStr) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "this$0"
            //     3: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //     6: aload_2         /* ruleStr */
            //     7: ldc             "ruleStr"
            //     9: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
            //    12: aload_0         /* this */
            //    13: aload_1         /* this$0 */
            //    14: putfield        io/legado/app/model/analyzeRule/AnalyzeByJSoup$SourceRule.this$0:Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;
            //    17: aload_0         /* this */
            //    18: invokespecial   java/lang/Object.<init>:()V
            //    21: aload_0         /* this */
            //    22: aload_2         /* ruleStr */
            //    23: ldc             "@CSS:"
            //    25: iconst_1       
            //    26: invokestatic    kotlin/text/StringsKt.startsWith:(Ljava/lang/String;Ljava/lang/String;Z)Z
            //    29: ifeq            208
            //    32: aload_0         /* this */
            //    33: iconst_1       
            //    34: putfield        io/legado/app/model/analyzeRule/AnalyzeByJSoup$SourceRule.isCss:Z
            //    37: aload_2         /* ruleStr */
            //    38: astore_3       
            //    39: iconst_5       
            //    40: istore          4
            //    42: iconst_0       
            //    43: istore          5
            //    45: aload_3        
            //    46: iload           4
            //    48: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
            //    51: dup            
            //    52: ldc             "(this as java.lang.String).substring(startIndex)"
            //    54: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
            //    57: astore_3       
            //    58: astore          13
            //    60: iconst_0       
            //    61: istore          $i$f$trim
            //    63: aload_3         /* $this$trim$iv */
            //    64: checkcast       Ljava/lang/CharSequence;
            //    67: astore          $this$trim$iv$iv
            //    69: iconst_0       
            //    70: istore          $i$f$trim
            //    72: iconst_0       
            //    73: istore          startIndex$iv$iv
            //    75: aload           $this$trim$iv$iv
            //    77: invokeinterface java/lang/CharSequence.length:()I
            //    82: iconst_1       
            //    83: isub           
            //    84: istore          endIndex$iv$iv
            //    86: iconst_0       
            //    87: istore          startFound$iv$iv
            //    89: iload           startIndex$iv$iv
            //    91: iload           endIndex$iv$iv
            //    93: if_icmpgt       183
            //    96: iload           startFound$iv$iv
            //    98: ifne            106
            //   101: iload           startIndex$iv$iv
            //   103: goto            108
            //   106: iload           endIndex$iv$iv
            //   108: istore          index$iv$iv
            //   110: aload           $this$trim$iv$iv
            //   112: iload           index$iv$iv
            //   114: invokeinterface java/lang/CharSequence.charAt:(I)C
            //   119: istore          it
            //   121: iconst_0       
            //   122: istore          $i$a$-trim-AnalyzeByJSoup$SourceRule$elementsRule$1
            //   124: iload           it
            //   126: bipush          32
            //   128: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
            //   131: ifgt            138
            //   134: iconst_1       
            //   135: goto            139
            //   138: iconst_0       
            //   139: istore          match$iv$iv
            //   141: iload           startFound$iv$iv
            //   143: ifne            166
            //   146: iload           match$iv$iv
            //   148: ifne            157
            //   151: iconst_1       
            //   152: istore          startFound$iv$iv
            //   154: goto            180
            //   157: iload           startIndex$iv$iv
            //   159: iconst_1       
            //   160: iadd           
            //   161: istore          startIndex$iv$iv
            //   163: goto            180
            //   166: iload           match$iv$iv
            //   168: ifne            174
            //   171: goto            183
            //   174: iload           endIndex$iv$iv
            //   176: iconst_1       
            //   177: isub           
            //   178: istore          endIndex$iv$iv
            //   180: goto            89
            //   183: aload           $this$trim$iv$iv
            //   185: iload           startIndex$iv$iv
            //   187: iload           endIndex$iv$iv
            //   189: iconst_1       
            //   190: iadd           
            //   191: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
            //   196: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
            //   199: astore          14
            //   201: aload           13
            //   203: aload           14
            //   205: goto            209
            //   208: aload_2         /* ruleStr */
            //   209: putfield        io/legado/app/model/analyzeRule/AnalyzeByJSoup$SourceRule.elementsRule:Ljava/lang/String;
            //   212: return         
            //    Signature:
            //  (Lio/legado/app/model/analyzeRule/AnalyzeByJSoup;Ljava/lang/String;)V [from metadata: (Ljava/lang/String;)V]
            //  
            //    MethodParameters:
            //  Name     Flags     
            //  -------  --------
            //  this$0   MANDATED
            //  ruleStr  
            //    StackMapTable: 00 0C FF 00 59 00 0E 07 00 02 07 00 5A 07 00 26 07 00 26 01 07 00 31 01 01 01 01 00 00 00 07 00 02 00 00 10 41 01 FF 00 1D 00 0E 07 00 02 07 00 5A 07 00 26 07 00 26 01 07 00 31 01 01 01 01 01 01 01 07 00 02 00 00 40 01 11 08 07 05 FF 00 02 00 0E 07 00 02 07 00 5A 07 00 26 07 00 26 01 07 00 31 01 01 01 01 00 00 00 07 00 02 00 00 FF 00 18 00 03 07 00 02 07 00 5A 07 00 26 00 01 07 00 02 FF 00 00 00 03 07 00 02 07 00 5A 07 00 26 00 02 07 00 02 07 00 26
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
            //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:799)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:635)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public final boolean isCss() {
            return this.isCss;
        }
        
        public final void setCss(final boolean <set-?>) {
            this.isCss = <set-?>;
        }
        
        @NotNull
        public final String getElementsRule() {
            return this.elementsRule;
        }
        
        public final void setElementsRule(@NotNull final String <set-?>) {
            Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
            this.elementsRule = <set-?>;
        }
    }
}
