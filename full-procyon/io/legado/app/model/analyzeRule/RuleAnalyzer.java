// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function2;
import kotlin.collections.CollectionsKt;
import java.util.Arrays;
import java.util.Collection;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KFunction;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u0000 <2\u00020\u0001:\u0001<B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J\u0016\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\tJ\u0016\u0010%\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\tJ\u000e\u0010&\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\u0003J\u001f\u0010(\u001a\u00020\u00052\u0012\u0010'\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030)\"\u00020\u0003?\u0006\u0002\u0010*J\u0014\u0010+\u001a\u00020\u00172\n\u0010'\u001a\u00020,\"\u00020\tH\u0002J8\u0010-\u001a\u00020\u00032\u0006\u0010.\u001a\u00020\u00032\b\b\u0002\u0010/\u001a\u00020\u00172\b\b\u0002\u00100\u001a\u00020\u00172\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u000302J,\u0010-\u001a\u00020\u00032\u0006\u00103\u001a\u00020\u00032\u0006\u00104\u001a\u00020\u00032\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u000302J\u0006\u00105\u001a\u000206J\u001e\u00107\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001bH\u0083\u0010?\u0006\u0002\b8J2\u00107\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001b2\u0012\u00109\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030)\"\u00020\u0003H\u0086\u0010?\u0006\u0002\u0010:J\u0006\u0010;\u001a\u000206R#\u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00050\b?\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001bX\u0082\u000e?\u0006\u0002\n\u0000R!\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001b?\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u0017X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0017X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0017X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006=" }, d2 = { "Lio/legado/app/model/analyzeRule/RuleAnalyzer;", "", "data", "", "code", "", "(Ljava/lang/String;Z)V", "chompBalanced", "Lkotlin/reflect/KFunction2;", "", "getChompBalanced", "()Lkotlin/reflect/KFunction;", "elementsType", "getElementsType", "()Ljava/lang/String;", "setElementsType", "(Ljava/lang/String;)V", "innerType", "getInnerType", "()Z", "setInnerType", "(Z)V", "pos", "", "queue", "rule", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "ruleTypeList", "getRuleTypeList", "()Ljava/util/ArrayList;", "start", "startX", "step", "chompCodeBalanced", "open", "close", "chompRuleBalanced", "consumeTo", "seq", "consumeToAny", "", "([Ljava/lang/String;)Z", "findToAny", "", "innerRule", "inner", "startStep", "endStep", "fr", "Lkotlin/Function1;", "startStr", "endStr", "reSetPos", "", "splitRule", "splitRuleNext", "split", "([Ljava/lang/String;)Ljava/util/ArrayList;", "trim", "Companion", "reader-pro" })
public final class RuleAnalyzer
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private String queue;
    private int pos;
    private int start;
    private int startX;
    @NotNull
    private ArrayList<String> rule;
    private int step;
    @NotNull
    private String elementsType;
    private boolean innerType;
    @NotNull
    private final ArrayList<String> ruleTypeList;
    @NotNull
    private final KFunction<Boolean> chompBalanced;
    private static final char ESC = '\\';
    
    public RuleAnalyzer(@NotNull final String data, final boolean code) {
        Intrinsics.checkNotNullParameter((Object)data, "data");
        this.queue = data;
        this.rule = new ArrayList<String>();
        this.elementsType = "";
        this.innerType = true;
        this.ruleTypeList = new ArrayList<String>();
        this.chompBalanced = (KFunction<Boolean>)(code ? new RuleAnalyzer$chompBalanced.RuleAnalyzer$chompBalanced$1(this) : ((KFunction)new RuleAnalyzer$chompBalanced.RuleAnalyzer$chompBalanced$2(this)));
    }
    
    @NotNull
    public final String getElementsType() {
        return this.elementsType;
    }
    
    public final void setElementsType(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.elementsType = <set-?>;
    }
    
    public final boolean getInnerType() {
        return this.innerType;
    }
    
    public final void setInnerType(final boolean <set-?>) {
        this.innerType = <set-?>;
    }
    
    public final void trim() {
        if (this.queue.charAt(this.pos) == '@' || Intrinsics.compare((int)this.queue.charAt(this.pos), 33) < 0) {
            ++this.pos;
            while (this.queue.charAt(this.pos) == '@' || Intrinsics.compare((int)this.queue.charAt(this.pos), 33) < 0) {
                ++this.pos;
            }
            this.start = this.pos;
            this.startX = this.pos;
        }
    }
    
    public final void reSetPos() {
        this.pos = 0;
        this.startX = 0;
    }
    
    public final boolean consumeTo(@NotNull final String seq) {
        Intrinsics.checkNotNullParameter((Object)seq, "seq");
        this.start = this.pos;
        final int offset = StringsKt.indexOf$default((CharSequence)this.queue, seq, this.pos, false, 4, (Object)null);
        boolean b;
        if (offset != -1) {
            this.pos = offset;
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public final boolean consumeToAny(@NotNull final String... seq) {
        Intrinsics.checkNotNullParameter((Object)seq, "seq");
        for (int pos = this.pos; pos != this.queue.length(); ++pos) {
            int i = 0;
            while (i < seq.length) {
                final String s = seq[i];
                ++i;
                if (StringsKt.regionMatches$default(this.queue, pos, s, 0, s.length(), false, 16, (Object)null)) {
                    this.step = s.length();
                    this.pos = pos;
                    return true;
                }
            }
        }
        return false;
    }
    
    private final int findToAny(final char... seq) {
        for (int pos = this.pos; pos != this.queue.length(); ++pos) {
            int i = 0;
            while (i < seq.length) {
                final char s = seq[i];
                ++i;
                if (this.queue.charAt(pos) == s) {
                    return pos;
                }
            }
        }
        return -1;
    }
    
    public final boolean chompCodeBalanced(final char open, final char close) {
        int pos = this.pos;
        int depth = 0;
        int otherDepth = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        while (true) {
            while (pos != this.queue.length()) {
                final String queue = this.queue;
                final int index = pos;
                pos = index + 1;
                final char c = queue.charAt(index);
                if (c != '\\') {
                    if (c == '\'' && !inDoubleQuote) {
                        inSingleQuote = !inSingleQuote;
                    }
                    else if (c == '\"' && !inSingleQuote) {
                        inDoubleQuote = !inDoubleQuote;
                    }
                    if (!inSingleQuote) {
                        if (!inDoubleQuote) {
                            if (c == '[') {
                                ++depth;
                            }
                            else if (c == ']') {
                                --depth;
                            }
                            else if (depth == 0) {
                                if (c == open) {
                                    ++otherDepth;
                                }
                                else if (c == close) {
                                    --otherDepth;
                                }
                            }
                        }
                    }
                }
                else {
                    ++pos;
                }
                if (depth <= 0 && otherDepth <= 0) {
                    boolean b;
                    if (depth > 0 || otherDepth > 0) {
                        b = false;
                    }
                    else {
                        this.pos = pos;
                        b = true;
                    }
                    return b;
                }
            }
            continue;
        }
    }
    
    public final boolean chompRuleBalanced(final char open, final char close) {
        int pos = this.pos;
        int depth = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        while (true) {
            while (pos != this.queue.length()) {
                final String queue = this.queue;
                final int index = pos;
                pos = index + 1;
                final char c = queue.charAt(index);
                if (c == '\'' && !inDoubleQuote) {
                    inSingleQuote = !inSingleQuote;
                }
                else if (c == '\"' && !inSingleQuote) {
                    inDoubleQuote = !inDoubleQuote;
                }
                if (!inSingleQuote) {
                    if (!inDoubleQuote) {
                        if (c == '\\') {
                            ++pos;
                        }
                        else if (c == open) {
                            ++depth;
                        }
                        else if (c == close) {
                            --depth;
                        }
                    }
                }
                if (depth <= 0) {
                    boolean b;
                    if (depth > 0) {
                        b = false;
                    }
                    else {
                        this.pos = pos;
                        b = true;
                    }
                    return b;
                }
            }
            continue;
        }
    }
    
    @NotNull
    public final ArrayList<String> splitRule(@NotNull final String... split) {
        Intrinsics.checkNotNullParameter((Object)split, "split");
        RuleAnalyzer ruleAnalyzer = this;
        String[] array = split;
        while (true) {
            final RuleAnalyzer ruleAnalyzer2 = ruleAnalyzer;
            final String[] array2 = array;
            if (array2.length == 1) {
                ruleAnalyzer2.elementsType = array2[0];
                ArrayList<String> list;
                if (!ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                    final Collection collection = ruleAnalyzer2.rule;
                    final String queue = ruleAnalyzer2.queue;
                    final int startX = ruleAnalyzer2.startX;
                    final String s = queue;
                    if (s == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring = s.substring(startX);
                    Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
                    collection.add(substring);
                    list = ruleAnalyzer2.rule;
                }
                else {
                    ruleAnalyzer2.step = ruleAnalyzer2.elementsType.length();
                    list = ruleAnalyzer2.splitRuleNext();
                }
                return list;
            }
            if (!ruleAnalyzer2.consumeToAny((String[])Arrays.copyOf(array2, array2.length))) {
                final Collection collection2 = ruleAnalyzer2.rule;
                final String queue2 = ruleAnalyzer2.queue;
                final int startX2 = ruleAnalyzer2.startX;
                final String s2 = queue2;
                if (s2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring2 = s2.substring(startX2);
                Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.String).substring(startIndex)");
                collection2.add(substring2);
                return ruleAnalyzer2.rule;
            }
            else {
                final int end = ruleAnalyzer2.pos;
                ruleAnalyzer2.pos = ruleAnalyzer2.start;
                do {
                    final int st = ruleAnalyzer2.findToAny('[', '(');
                    if (st == -1) {
                        final RuleAnalyzer ruleAnalyzer3 = ruleAnalyzer2;
                        final String[] array3 = { null };
                        final int n = 0;
                        final String queue3 = ruleAnalyzer2.queue;
                        final int startX3 = ruleAnalyzer2.startX;
                        final String s3 = queue3;
                        if (s3 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring3 = s3.substring(startX3, end);
                        Intrinsics.checkNotNullExpressionValue((Object)substring3, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        array3[n] = substring3;
                        ruleAnalyzer3.rule = CollectionsKt.arrayListOf((Object[])array3);
                        final RuleAnalyzer ruleAnalyzer4 = ruleAnalyzer2;
                        final String queue4 = ruleAnalyzer2.queue;
                        final int endIndex = end + ruleAnalyzer2.step;
                        final String s4 = queue4;
                        if (s4 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring4 = s4.substring(end, endIndex);
                        Intrinsics.checkNotNullExpressionValue((Object)substring4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        ruleAnalyzer4.elementsType = substring4;
                        ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                        while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                            final Collection collection3 = ruleAnalyzer2.rule;
                            final String queue5 = ruleAnalyzer2.queue;
                            final int start = ruleAnalyzer2.start;
                            final int pos = ruleAnalyzer2.pos;
                            final String s5 = queue5;
                            if (s5 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            final String substring5 = s5.substring(start, pos);
                            Intrinsics.checkNotNullExpressionValue((Object)substring5, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                            collection3.add(substring5);
                            final RuleAnalyzer ruleAnalyzer5 = ruleAnalyzer2;
                            ruleAnalyzer5.pos += ruleAnalyzer2.step;
                        }
                        final Collection collection4 = ruleAnalyzer2.rule;
                        final String queue6 = ruleAnalyzer2.queue;
                        final int pos2 = ruleAnalyzer2.pos;
                        final String s6 = queue6;
                        if (s6 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring6 = s6.substring(pos2);
                        Intrinsics.checkNotNullExpressionValue((Object)substring6, "(this as java.lang.String).substring(startIndex)");
                        collection4.add(substring6);
                        return ruleAnalyzer2.rule;
                    }
                    else if (st > end) {
                        final RuleAnalyzer ruleAnalyzer6 = ruleAnalyzer2;
                        final String[] array4 = { null };
                        final int n2 = 0;
                        final String queue7 = ruleAnalyzer2.queue;
                        final int startX4 = ruleAnalyzer2.startX;
                        final String s7 = queue7;
                        if (s7 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring7 = s7.substring(startX4, end);
                        Intrinsics.checkNotNullExpressionValue((Object)substring7, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        array4[n2] = substring7;
                        ruleAnalyzer6.rule = CollectionsKt.arrayListOf((Object[])array4);
                        final RuleAnalyzer ruleAnalyzer7 = ruleAnalyzer2;
                        final String queue8 = ruleAnalyzer2.queue;
                        final int endIndex2 = end + ruleAnalyzer2.step;
                        final String s8 = queue8;
                        if (s8 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring8 = s8.substring(end, endIndex2);
                        Intrinsics.checkNotNullExpressionValue((Object)substring8, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        ruleAnalyzer7.elementsType = substring8;
                        ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                        while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType) && ruleAnalyzer2.pos < st) {
                            final Collection collection5 = ruleAnalyzer2.rule;
                            final String queue9 = ruleAnalyzer2.queue;
                            final int start2 = ruleAnalyzer2.start;
                            final int pos3 = ruleAnalyzer2.pos;
                            final String s9 = queue9;
                            if (s9 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            final String substring9 = s9.substring(start2, pos3);
                            Intrinsics.checkNotNullExpressionValue((Object)substring9, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                            collection5.add(substring9);
                            final RuleAnalyzer ruleAnalyzer8 = ruleAnalyzer2;
                            ruleAnalyzer8.pos += ruleAnalyzer2.step;
                        }
                        ArrayList<String> list2;
                        if (ruleAnalyzer2.pos > st) {
                            ruleAnalyzer2.startX = ruleAnalyzer2.start;
                            list2 = ruleAnalyzer2.splitRuleNext();
                        }
                        else {
                            final Collection collection6 = ruleAnalyzer2.rule;
                            final String queue10 = ruleAnalyzer2.queue;
                            final int pos4 = ruleAnalyzer2.pos;
                            final String s10 = queue10;
                            if (s10 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            final String substring10 = s10.substring(pos4);
                            Intrinsics.checkNotNullExpressionValue((Object)substring10, "(this as java.lang.String).substring(startIndex)");
                            collection6.add(substring10);
                            list2 = ruleAnalyzer2.rule;
                        }
                        return list2;
                    }
                    else {
                        ruleAnalyzer2.pos = st;
                        final char next = (char)((ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos) == '[') ? 93 : 41);
                        if (((Function2)ruleAnalyzer2.chompBalanced).invoke((Object)ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos), (Object)next)) {
                            continue;
                        }
                        final String queue11 = ruleAnalyzer2.queue;
                        final int beginIndex = 0;
                        final int start3 = ruleAnalyzer2.start;
                        final String s11 = queue11;
                        if (s11 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring11 = s11.substring(beginIndex, start3);
                        Intrinsics.checkNotNullExpressionValue((Object)substring11, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        throw new Error(Intrinsics.stringPlus(substring11, (Object)"\u540e\u672a\u5e73\u8861"));
                    }
                } while (end > ruleAnalyzer2.pos);
                ruleAnalyzer2.start = ruleAnalyzer2.pos;
                ruleAnalyzer = ruleAnalyzer2;
                array = Arrays.copyOf(array2, array2.length);
            }
        }
    }
    
    @JvmName(name = "splitRuleNext")
    private final ArrayList<String> splitRuleNext() {
        RuleAnalyzer ruleAnalyzer = this;
    Label_0002:
        while (true) {
            final RuleAnalyzer ruleAnalyzer2 = ruleAnalyzer;
            final int end = ruleAnalyzer2.pos;
            ruleAnalyzer2.pos = ruleAnalyzer2.start;
            do {
                final int st = ruleAnalyzer2.findToAny('[', '(');
                if (st == -1) {
                    final Collection collection = ruleAnalyzer2.rule;
                    final String[] array = { null };
                    final int n = 0;
                    final String queue = ruleAnalyzer2.queue;
                    final int startX = ruleAnalyzer2.startX;
                    final String s = queue;
                    if (s == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring = s.substring(startX, end);
                    Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    array[n] = substring;
                    CollectionsKt.addAll(collection, (Object[])array);
                    ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                    while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                        final Collection collection2 = ruleAnalyzer2.rule;
                        final String queue2 = ruleAnalyzer2.queue;
                        final int start = ruleAnalyzer2.start;
                        final int pos = ruleAnalyzer2.pos;
                        final String s2 = queue2;
                        if (s2 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring2 = s2.substring(start, pos);
                        Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        collection2.add(substring2);
                        final RuleAnalyzer ruleAnalyzer3 = ruleAnalyzer2;
                        ruleAnalyzer3.pos += ruleAnalyzer2.step;
                    }
                    final Collection collection3 = ruleAnalyzer2.rule;
                    final String queue3 = ruleAnalyzer2.queue;
                    final int pos2 = ruleAnalyzer2.pos;
                    final String s3 = queue3;
                    if (s3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring3 = s3.substring(pos2);
                    Intrinsics.checkNotNullExpressionValue((Object)substring3, "(this as java.lang.String).substring(startIndex)");
                    collection3.add(substring3);
                    return ruleAnalyzer2.rule;
                }
                else if (st > end) {
                    final Collection collection4 = ruleAnalyzer2.rule;
                    final String[] array2 = { null };
                    final int n2 = 0;
                    final String queue4 = ruleAnalyzer2.queue;
                    final int startX2 = ruleAnalyzer2.startX;
                    final String s4 = queue4;
                    if (s4 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring4 = s4.substring(startX2, end);
                    Intrinsics.checkNotNullExpressionValue((Object)substring4, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    array2[n2] = substring4;
                    CollectionsKt.addAll(collection4, (Iterable)CollectionsKt.arrayListOf((Object[])array2));
                    ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                    while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType) && ruleAnalyzer2.pos < st) {
                        final Collection collection5 = ruleAnalyzer2.rule;
                        final String queue5 = ruleAnalyzer2.queue;
                        final int start2 = ruleAnalyzer2.start;
                        final int pos3 = ruleAnalyzer2.pos;
                        final String s5 = queue5;
                        if (s5 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        final String substring5 = s5.substring(start2, pos3);
                        Intrinsics.checkNotNullExpressionValue((Object)substring5, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                        collection5.add(substring5);
                        final RuleAnalyzer ruleAnalyzer4 = ruleAnalyzer2;
                        ruleAnalyzer4.pos += ruleAnalyzer2.step;
                    }
                    if (ruleAnalyzer2.pos > st) {
                        ruleAnalyzer2.startX = ruleAnalyzer2.start;
                        ruleAnalyzer = ruleAnalyzer2;
                        continue Label_0002;
                    }
                    final Collection collection6 = ruleAnalyzer2.rule;
                    final String queue6 = ruleAnalyzer2.queue;
                    final int pos4 = ruleAnalyzer2.pos;
                    final String s6 = queue6;
                    if (s6 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring6 = s6.substring(pos4);
                    Intrinsics.checkNotNullExpressionValue((Object)substring6, "(this as java.lang.String).substring(startIndex)");
                    collection6.add(substring6);
                    return ruleAnalyzer2.rule;
                }
                else {
                    ruleAnalyzer2.pos = st;
                    final char next = (char)((ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos) == '[') ? 93 : 41);
                    if (((Function2)ruleAnalyzer2.chompBalanced).invoke((Object)ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos), (Object)next)) {
                        continue;
                    }
                    final String queue7 = ruleAnalyzer2.queue;
                    final int beginIndex = 0;
                    final int start3 = ruleAnalyzer2.start;
                    final String s7 = queue7;
                    if (s7 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring7 = s7.substring(beginIndex, start3);
                    Intrinsics.checkNotNullExpressionValue((Object)substring7, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    throw new Error(Intrinsics.stringPlus(substring7, (Object)"\u540e\u672a\u5e73\u8861"));
                }
            } while (end > ruleAnalyzer2.pos);
            ruleAnalyzer2.start = ruleAnalyzer2.pos;
            if (!ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                final Collection collection7 = ruleAnalyzer2.rule;
                final String queue8 = ruleAnalyzer2.queue;
                final int startX3 = ruleAnalyzer2.startX;
                final String s8 = queue8;
                if (s8 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring8 = s8.substring(startX3);
                Intrinsics.checkNotNullExpressionValue((Object)substring8, "(this as java.lang.String).substring(startIndex)");
                collection7.add(substring8);
                return ruleAnalyzer2.rule;
            }
            else {
                ruleAnalyzer = ruleAnalyzer2;
            }
        }
    }
    
    @NotNull
    public final String innerRule(@NotNull final String inner, final int startStep, final int endStep, @NotNull final Function1<? super String, String> fr) {
        Intrinsics.checkNotNullParameter((Object)inner, "inner");
        Intrinsics.checkNotNullParameter((Object)fr, "fr");
        final StringBuilder st = new StringBuilder();
        while (this.consumeTo(inner)) {
            final int posPre = this.pos;
            if (this.chompCodeBalanced('{', '}')) {
                final String queue = this.queue;
                final int beginIndex = posPre + startStep;
                final int endIndex = this.pos - endStep;
                final String s = queue;
                if (s == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring = s.substring(beginIndex, endIndex);
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                final String frv = (String)fr.invoke((Object)substring);
                final CharSequence charSequence = frv;
                if (charSequence != null && charSequence.length() != 0) {
                    final StringBuilder sb = st;
                    final String queue2 = this.queue;
                    final int startX = this.startX;
                    final String s2 = queue2;
                    if (s2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring2 = s2.substring(startX, posPre);
                    Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                    sb.append(Intrinsics.stringPlus(substring2, (Object)frv));
                    this.startX = this.pos;
                    continue;
                }
            }
            this.pos += inner.length();
        }
        String s3;
        if (this.startX == 0) {
            s3 = "";
        }
        else {
            final StringBuilder $this$innerRule_u24lambda_u2d0 = st;
            final int n = 0;
            final StringBuilder sb2 = $this$innerRule_u24lambda_u2d0;
            final String queue3 = this.queue;
            final int startX2 = this.startX;
            final String s4 = queue3;
            if (s4 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring3 = s4.substring(startX2);
            Intrinsics.checkNotNullExpressionValue((Object)substring3, "(this as java.lang.String).substring(startIndex)");
            sb2.append(substring3);
            final String string = $this$innerRule_u24lambda_u2d0.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "st.apply {\n            append(queue.substring(startX))\n        }.toString()");
            s3 = string;
        }
        return s3;
    }
    
    @NotNull
    public final String innerRule(@NotNull final String startStr, @NotNull final String endStr, @NotNull final Function1<? super String, String> fr) {
        Intrinsics.checkNotNullParameter((Object)startStr, "startStr");
        Intrinsics.checkNotNullParameter((Object)endStr, "endStr");
        Intrinsics.checkNotNullParameter((Object)fr, "fr");
        final StringBuilder st = new StringBuilder();
        while (this.consumeTo(startStr)) {
            this.pos += startStr.length();
            final int posPre = this.pos;
            if (this.consumeTo(endStr)) {
                final String queue = this.queue;
                final int pos = this.pos;
                final String s = queue;
                if (s == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring = s.substring(posPre, pos);
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                final String frv = (String)fr.invoke((Object)substring);
                final StringBuilder sb = st;
                final String queue2 = this.queue;
                final int startX = this.startX;
                final int endIndex = posPre - startStr.length();
                final String s2 = queue2;
                if (s2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final String substring2 = s2.substring(startX, endIndex);
                Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                sb.append(Intrinsics.stringPlus(substring2, (Object)frv));
                this.pos += endStr.length();
                this.startX = this.pos;
            }
        }
        String queue3;
        if (this.startX == 0) {
            queue3 = this.queue;
        }
        else {
            final StringBuilder $this$innerRule_u24lambda_u2d1 = st;
            final int n = 0;
            final StringBuilder sb2 = $this$innerRule_u24lambda_u2d1;
            final String queue4 = this.queue;
            final int startX2 = this.startX;
            final String s3 = queue4;
            if (s3 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring3 = s3.substring(startX2);
            Intrinsics.checkNotNullExpressionValue((Object)substring3, "(this as java.lang.String).substring(startIndex)");
            sb2.append(substring3);
            final String string = $this$innerRule_u24lambda_u2d1.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "st.apply {\n            append(queue.substring(startX))\n        }.toString()");
            queue3 = string;
        }
        return queue3;
    }
    
    @NotNull
    public final ArrayList<String> getRuleTypeList() {
        return this.ruleTypeList;
    }
    
    @NotNull
    public final KFunction<Boolean> getChompBalanced() {
        return this.chompBalanced;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000¡§\u0006\u0005" }, d2 = { "Lio/legado/app/model/analyzeRule/RuleAnalyzer$Companion;", "", "()V", "ESC", "", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
