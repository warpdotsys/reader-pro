package io.legado.app.model.analyzeRule;

import java.util.ArrayList;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KFunction;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: RuleAnalyzer.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/RuleAnalyzer.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u0000 <2\u00020\u0001:\u0001<B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\tJ\u0016\u0010%\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\tJ\u000e\u0010&\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\u0003J\u001f\u0010(\u001a\u00020\u00052\u0012\u0010'\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030)\"\u00020\u0003¢\u0006\u0002\u0010*J\u0014\u0010+\u001a\u00020\u00172\n\u0010'\u001a\u00020,\"\u00020\tH\u0002J8\u0010-\u001a\u00020\u00032\u0006\u0010.\u001a\u00020\u00032\b\b\u0002\u0010/\u001a\u00020\u00172\b\b\u0002\u00100\u001a\u00020\u00172\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u000302J,\u0010-\u001a\u00020\u00032\u0006\u00103\u001a\u00020\u00032\u0006\u00104\u001a\u00020\u00032\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u000302J\u0006\u00105\u001a\u000206J\u001e\u00107\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001bH\u0083\u0010¢\u0006\u0002\b8J2\u00107\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001b2\u0012\u00109\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030)\"\u00020\u0003H\u0086\u0010¢\u0006\u0002\u0010:J\u0006\u0010;\u001a\u000206R#\u0010\u0007\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00050\b¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R!\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u00020\u00030\u001aj\b\u0012\u0004\u0012\u00020\u0003`\u001b¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006="}, d2 = {"Lio/legado/app/model/analyzeRule/RuleAnalyzer;", PackageDocumentBase.PREFIX_OPF, "data", PackageDocumentBase.PREFIX_OPF, "code", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;Z)V", "chompBalanced", "Lkotlin/reflect/KFunction2;", PackageDocumentBase.PREFIX_OPF, "getChompBalanced", "()Lkotlin/reflect/KFunction;", "elementsType", "getElementsType", "()Ljava/lang/String;", "setElementsType", "(Ljava/lang/String;)V", "innerType", "getInnerType", "()Z", "setInnerType", "(Z)V", "pos", PackageDocumentBase.PREFIX_OPF, "queue", "rule", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "ruleTypeList", "getRuleTypeList", "()Ljava/util/ArrayList;", "start", "startX", "step", "chompCodeBalanced", "open", "close", "chompRuleBalanced", "consumeTo", "seq", "consumeToAny", PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/String;)Z", "findToAny", PackageDocumentBase.PREFIX_OPF, "innerRule", "inner", "startStep", "endStep", "fr", "Lkotlin/Function1;", "startStr", "endStr", "reSetPos", PackageDocumentBase.PREFIX_OPF, "splitRule", "splitRuleNext", "split", "([Ljava/lang/String;)Ljava/util/ArrayList;", "trim", "Companion", "reader-pro"})
public final class RuleAnalyzer {

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

    public RuleAnalyzer(@NotNull String data, boolean code) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.queue = data;
        this.rule = new ArrayList<>();
        this.elementsType = PackageDocumentBase.PREFIX_OPF;
        this.innerType = true;
        this.ruleTypeList = new ArrayList<>();
        this.chompBalanced = code ? (KFunction) new RuleAnalyzer$chompBalanced$1(this) : new RuleAnalyzer$chompBalanced$2(this);
    }

    public /* synthetic */ RuleAnalyzer(String str, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? false : z);
    }

    @NotNull
    public final String getElementsType() {
        return this.elementsType;
    }

    public final void setElementsType(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.elementsType = str;
    }

    public final boolean getInnerType() {
        return this.innerType;
    }

    public final void setInnerType(boolean z) {
        this.innerType = z;
    }

    public final void trim() {
        if (this.queue.charAt(this.pos) == '@' || Intrinsics.compare(this.queue.charAt(this.pos), 33) < 0) {
            this.pos++;
            while (true) {
                if (this.queue.charAt(this.pos) != '@' && Intrinsics.compare(this.queue.charAt(this.pos), 33) >= 0) {
                    this.start = this.pos;
                    this.startX = this.pos;
                    return;
                }
                this.pos++;
            }
        }
    }

    public final void reSetPos() {
        this.pos = 0;
        this.startX = 0;
    }

    public final boolean consumeTo(@NotNull String seq) {
        Intrinsics.checkNotNullParameter(seq, "seq");
        this.start = this.pos;
        int offset = StringsKt.indexOf$default(this.queue, seq, this.pos, false, 4, (Object) null);
        if (offset != -1) {
            this.pos = offset;
            return true;
        }
        return false;
    }

    public final boolean consumeToAny(@NotNull String... seq) {
        Intrinsics.checkNotNullParameter(seq, "seq");
        int i = this.pos;
        while (true) {
            int pos = i;
            if (pos != this.queue.length()) {
                int i2 = 0;
                int length = seq.length;
                while (i2 < length) {
                    String s = seq[i2];
                    i2++;
                    if (StringsKt.regionMatches$default(this.queue, pos, s, 0, s.length(), false, 16, (Object) null)) {
                        this.step = s.length();
                        this.pos = pos;
                        return true;
                    }
                }
                i = pos + 1;
            } else {
                return false;
            }
        }
    }

    private final int findToAny(char... seq) {
        int i = this.pos;
        while (true) {
            int pos = i;
            if (pos != this.queue.length()) {
                int i2 = 0;
                int length = seq.length;
                while (i2 < length) {
                    char s = seq[i2];
                    i2++;
                    if (this.queue.charAt(pos) == s) {
                        return pos;
                    }
                }
                i = pos + 1;
            } else {
                return -1;
            }
        }
    }

    public final boolean chompCodeBalanced(char open, char close) {
        int pos = this.pos;
        int depth = 0;
        int otherDepth = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        while (pos != this.queue.length()) {
            int i = pos;
            pos = i + 1;
            char c = this.queue.charAt(i);
            if (c != ESC) {
                if (c == '\'' && !inDoubleQuote) {
                    inSingleQuote = !inSingleQuote;
                } else if (c == '\"' && !inSingleQuote) {
                    inDoubleQuote = !inDoubleQuote;
                }
                if (!inSingleQuote && !inDoubleQuote) {
                    if (c == '[') {
                        depth++;
                    } else if (c == ']') {
                        depth--;
                    } else if (depth == 0) {
                        if (c == open) {
                            otherDepth++;
                        } else if (c == close) {
                            otherDepth--;
                        }
                    }
                }
            } else {
                pos++;
            }
            if (depth <= 0 && otherDepth <= 0) {
                break;
            }
        }
        if (depth > 0 || otherDepth > 0) {
            return false;
        }
        this.pos = pos;
        return true;
    }

    public final boolean chompRuleBalanced(char open, char close) {
        int pos = this.pos;
        int depth = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        while (pos != this.queue.length()) {
            int i = pos;
            pos = i + 1;
            char c = this.queue.charAt(i);
            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            } else if (c == '\"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            }
            if (!inSingleQuote && !inDoubleQuote) {
                if (c == ESC) {
                    pos++;
                } else if (c == open) {
                    depth++;
                } else if (c == close) {
                    depth--;
                }
            }
            if (depth <= 0) {
                break;
            }
        }
        if (depth > 0) {
            return false;
        }
        this.pos = pos;
        return true;
    }

    @NotNull
    public final ArrayList<String> splitRule(@NotNull String... split) {
        Intrinsics.checkNotNullParameter(split, "split");
        RuleAnalyzer ruleAnalyzer = this;
        String[] strArr = split;
        while (true) {
            String[] strArr2 = strArr;
            RuleAnalyzer ruleAnalyzer2 = ruleAnalyzer;
            if (strArr2.length == 1) {
                ruleAnalyzer2.elementsType = strArr2[0];
                if (!ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                    ArrayList<String> arrayList = ruleAnalyzer2.rule;
                    String str = ruleAnalyzer2.queue;
                    int i = ruleAnalyzer2.startX;
                    if (str == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring = str.substring(i);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    arrayList.add(strSubstring);
                    return ruleAnalyzer2.rule;
                }
                ruleAnalyzer2.step = ruleAnalyzer2.elementsType.length();
                return ruleAnalyzer2.splitRuleNext();
            }
            if (!ruleAnalyzer2.consumeToAny((String[]) Arrays.copyOf(strArr2, strArr2.length))) {
                ArrayList<String> arrayList2 = ruleAnalyzer2.rule;
                String str2 = ruleAnalyzer2.queue;
                int i2 = ruleAnalyzer2.startX;
                if (str2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String strSubstring2 = str2.substring(i2);
                Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
                arrayList2.add(strSubstring2);
                return ruleAnalyzer2.rule;
            }
            int end = ruleAnalyzer2.pos;
            ruleAnalyzer2.pos = ruleAnalyzer2.start;
            do {
                int st = ruleAnalyzer2.findToAny('[', '(');
                if (st == -1) {
                    String[] strArr3 = new String[1];
                    String str3 = ruleAnalyzer2.queue;
                    int i3 = ruleAnalyzer2.startX;
                    if (str3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring3 = str3.substring(i3, end);
                    Intrinsics.checkNotNullExpressionValue(strSubstring3, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    strArr3[0] = strSubstring3;
                    ruleAnalyzer2.rule = CollectionsKt.arrayListOf(strArr3);
                    String str4 = ruleAnalyzer2.queue;
                    int i4 = end + ruleAnalyzer2.step;
                    if (str4 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring4 = str4.substring(end, i4);
                    Intrinsics.checkNotNullExpressionValue(strSubstring4, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    ruleAnalyzer2.elementsType = strSubstring4;
                    ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                    while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                        ArrayList<String> arrayList3 = ruleAnalyzer2.rule;
                        String str5 = ruleAnalyzer2.queue;
                        int i5 = ruleAnalyzer2.start;
                        int i6 = ruleAnalyzer2.pos;
                        if (str5 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring5 = str5.substring(i5, i6);
                        Intrinsics.checkNotNullExpressionValue(strSubstring5, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        arrayList3.add(strSubstring5);
                        ruleAnalyzer2.pos += ruleAnalyzer2.step;
                    }
                    ArrayList<String> arrayList4 = ruleAnalyzer2.rule;
                    String str6 = ruleAnalyzer2.queue;
                    int i7 = ruleAnalyzer2.pos;
                    if (str6 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring6 = str6.substring(i7);
                    Intrinsics.checkNotNullExpressionValue(strSubstring6, "(this as java.lang.String).substring(startIndex)");
                    arrayList4.add(strSubstring6);
                    return ruleAnalyzer2.rule;
                }
                if (st > end) {
                    String[] strArr4 = new String[1];
                    String str7 = ruleAnalyzer2.queue;
                    int i8 = ruleAnalyzer2.startX;
                    if (str7 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring7 = str7.substring(i8, end);
                    Intrinsics.checkNotNullExpressionValue(strSubstring7, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    strArr4[0] = strSubstring7;
                    ruleAnalyzer2.rule = CollectionsKt.arrayListOf(strArr4);
                    String str8 = ruleAnalyzer2.queue;
                    int i9 = end + ruleAnalyzer2.step;
                    if (str8 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring8 = str8.substring(end, i9);
                    Intrinsics.checkNotNullExpressionValue(strSubstring8, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    ruleAnalyzer2.elementsType = strSubstring8;
                    ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                    while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType) && ruleAnalyzer2.pos < st) {
                        ArrayList<String> arrayList5 = ruleAnalyzer2.rule;
                        String str9 = ruleAnalyzer2.queue;
                        int i10 = ruleAnalyzer2.start;
                        int i11 = ruleAnalyzer2.pos;
                        if (str9 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring9 = str9.substring(i10, i11);
                        Intrinsics.checkNotNullExpressionValue(strSubstring9, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        arrayList5.add(strSubstring9);
                        ruleAnalyzer2.pos += ruleAnalyzer2.step;
                    }
                    if (ruleAnalyzer2.pos > st) {
                        ruleAnalyzer2.startX = ruleAnalyzer2.start;
                        return ruleAnalyzer2.splitRuleNext();
                    }
                    ArrayList<String> arrayList6 = ruleAnalyzer2.rule;
                    String str10 = ruleAnalyzer2.queue;
                    int i12 = ruleAnalyzer2.pos;
                    if (str10 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring10 = str10.substring(i12);
                    Intrinsics.checkNotNullExpressionValue(strSubstring10, "(this as java.lang.String).substring(startIndex)");
                    arrayList6.add(strSubstring10);
                    return ruleAnalyzer2.rule;
                }
                ruleAnalyzer2.pos = st;
                char next = ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos) == '[' ? ']' : ')';
                if (!((Boolean) ruleAnalyzer2.chompBalanced.invoke(Character.valueOf(ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos)), Character.valueOf(next))).booleanValue()) {
                    String str11 = ruleAnalyzer2.queue;
                    int i13 = ruleAnalyzer2.start;
                    if (str11 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring11 = str11.substring(0, i13);
                    Intrinsics.checkNotNullExpressionValue(strSubstring11, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    throw new Error(Intrinsics.stringPlus(strSubstring11, "后未平衡"));
                }
            } while (end > ruleAnalyzer2.pos);
            ruleAnalyzer2.start = ruleAnalyzer2.pos;
            ruleAnalyzer = ruleAnalyzer2;
            strArr = (String[]) Arrays.copyOf(strArr2, strArr2.length);
        }
    }

    @JvmName(name = "splitRuleNext")
    private final ArrayList<String> splitRuleNext() {
        RuleAnalyzer ruleAnalyzer = this;
        while (true) {
            RuleAnalyzer ruleAnalyzer2 = ruleAnalyzer;
            int end = ruleAnalyzer2.pos;
            ruleAnalyzer2.pos = ruleAnalyzer2.start;
            while (true) {
                int st = ruleAnalyzer2.findToAny('[', '(');
                if (st == -1) {
                    ArrayList<String> arrayList = ruleAnalyzer2.rule;
                    String[] strArr = new String[1];
                    String str = ruleAnalyzer2.queue;
                    int i = ruleAnalyzer2.startX;
                    if (str == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring = str.substring(i, end);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    strArr[0] = strSubstring;
                    CollectionsKt.addAll(arrayList, strArr);
                    ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                    while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                        ArrayList<String> arrayList2 = ruleAnalyzer2.rule;
                        String str2 = ruleAnalyzer2.queue;
                        int i2 = ruleAnalyzer2.start;
                        int i3 = ruleAnalyzer2.pos;
                        if (str2 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring2 = str2.substring(i2, i3);
                        Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        arrayList2.add(strSubstring2);
                        ruleAnalyzer2.pos += ruleAnalyzer2.step;
                    }
                    ArrayList<String> arrayList3 = ruleAnalyzer2.rule;
                    String str3 = ruleAnalyzer2.queue;
                    int i4 = ruleAnalyzer2.pos;
                    if (str3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring3 = str3.substring(i4);
                    Intrinsics.checkNotNullExpressionValue(strSubstring3, "(this as java.lang.String).substring(startIndex)");
                    arrayList3.add(strSubstring3);
                    return ruleAnalyzer2.rule;
                }
                if (st > end) {
                    ArrayList<String> arrayList4 = ruleAnalyzer2.rule;
                    String[] strArr2 = new String[1];
                    String str4 = ruleAnalyzer2.queue;
                    int i5 = ruleAnalyzer2.startX;
                    if (str4 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring4 = str4.substring(i5, end);
                    Intrinsics.checkNotNullExpressionValue(strSubstring4, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    strArr2[0] = strSubstring4;
                    CollectionsKt.addAll(arrayList4, CollectionsKt.arrayListOf(strArr2));
                    ruleAnalyzer2.pos = end + ruleAnalyzer2.step;
                    while (ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType) && ruleAnalyzer2.pos < st) {
                        ArrayList<String> arrayList5 = ruleAnalyzer2.rule;
                        String str5 = ruleAnalyzer2.queue;
                        int i6 = ruleAnalyzer2.start;
                        int i7 = ruleAnalyzer2.pos;
                        if (str5 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring5 = str5.substring(i6, i7);
                        Intrinsics.checkNotNullExpressionValue(strSubstring5, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        arrayList5.add(strSubstring5);
                        ruleAnalyzer2.pos += ruleAnalyzer2.step;
                    }
                    if (ruleAnalyzer2.pos > st) {
                        ruleAnalyzer2.startX = ruleAnalyzer2.start;
                        ruleAnalyzer = ruleAnalyzer2;
                    } else {
                        ArrayList<String> arrayList6 = ruleAnalyzer2.rule;
                        String str6 = ruleAnalyzer2.queue;
                        int i8 = ruleAnalyzer2.pos;
                        if (str6 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring6 = str6.substring(i8);
                        Intrinsics.checkNotNullExpressionValue(strSubstring6, "(this as java.lang.String).substring(startIndex)");
                        arrayList6.add(strSubstring6);
                        return ruleAnalyzer2.rule;
                    }
                } else {
                    ruleAnalyzer2.pos = st;
                    char next = ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos) == '[' ? ']' : ')';
                    if (!((Boolean) ruleAnalyzer2.chompBalanced.invoke(Character.valueOf(ruleAnalyzer2.queue.charAt(ruleAnalyzer2.pos)), Character.valueOf(next))).booleanValue()) {
                        String str7 = ruleAnalyzer2.queue;
                        int i9 = ruleAnalyzer2.start;
                        if (str7 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring7 = str7.substring(0, i9);
                        Intrinsics.checkNotNullExpressionValue(strSubstring7, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        throw new Error(Intrinsics.stringPlus(strSubstring7, "后未平衡"));
                    }
                    if (end <= ruleAnalyzer2.pos) {
                        ruleAnalyzer2.start = ruleAnalyzer2.pos;
                        if (!ruleAnalyzer2.consumeTo(ruleAnalyzer2.elementsType)) {
                            ArrayList<String> arrayList7 = ruleAnalyzer2.rule;
                            String str8 = ruleAnalyzer2.queue;
                            int i10 = ruleAnalyzer2.startX;
                            if (str8 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            String strSubstring8 = str8.substring(i10);
                            Intrinsics.checkNotNullExpressionValue(strSubstring8, "(this as java.lang.String).substring(startIndex)");
                            arrayList7.add(strSubstring8);
                            return ruleAnalyzer2.rule;
                        }
                        ruleAnalyzer = ruleAnalyzer2;
                    }
                }
            }
        }
    }

    public static /* synthetic */ String innerRule$default(RuleAnalyzer ruleAnalyzer, String str, int i, int i2, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 1;
        }
        if ((i3 & 4) != 0) {
            i2 = 1;
        }
        return ruleAnalyzer.innerRule(str, i, i2, function1);
    }

    @NotNull
    public final String innerRule(@NotNull String inner, int startStep, int endStep, @NotNull Function1<? super String, String> fr) {
        Intrinsics.checkNotNullParameter(inner, "inner");
        Intrinsics.checkNotNullParameter(fr, "fr");
        StringBuilder st = new StringBuilder();
        while (consumeTo(inner)) {
            int posPre = this.pos;
            if (chompCodeBalanced('{', '}')) {
                String str = this.queue;
                int i = posPre + startStep;
                int i2 = this.pos - endStep;
                if (str == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String strSubstring = str.substring(i, i2);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                String frv = (String) fr.invoke(strSubstring);
                String str2 = frv;
                if (!(str2 == null || str2.length() == 0)) {
                    String str3 = this.queue;
                    int i3 = this.startX;
                    if (str3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring2 = str3.substring(i3, posPre);
                    Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    st.append(Intrinsics.stringPlus(strSubstring2, frv));
                    this.startX = this.pos;
                }
            }
            this.pos += inner.length();
        }
        if (this.startX == 0) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        String str4 = this.queue;
        int i4 = this.startX;
        if (str4 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring3 = str4.substring(i4);
        Intrinsics.checkNotNullExpressionValue(strSubstring3, "(this as java.lang.String).substring(startIndex)");
        st.append(strSubstring3);
        String string = st.toString();
        Intrinsics.checkNotNullExpressionValue(string, "st.apply {\n            append(queue.substring(startX))\n        }.toString()");
        return string;
    }

    @NotNull
    public final String innerRule(@NotNull String startStr, @NotNull String endStr, @NotNull Function1<? super String, String> fr) {
        Intrinsics.checkNotNullParameter(startStr, "startStr");
        Intrinsics.checkNotNullParameter(endStr, "endStr");
        Intrinsics.checkNotNullParameter(fr, "fr");
        StringBuilder st = new StringBuilder();
        while (consumeTo(startStr)) {
            this.pos += startStr.length();
            int posPre = this.pos;
            if (consumeTo(endStr)) {
                String str = this.queue;
                int i = this.pos;
                if (str == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String strSubstring = str.substring(posPre, i);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                String frv = (String) fr.invoke(strSubstring);
                String str2 = this.queue;
                int i2 = this.startX;
                int length = posPre - startStr.length();
                if (str2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String strSubstring2 = str2.substring(i2, length);
                Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                st.append(Intrinsics.stringPlus(strSubstring2, frv));
                this.pos += endStr.length();
                this.startX = this.pos;
            }
        }
        if (this.startX == 0) {
            return this.queue;
        }
        String str3 = this.queue;
        int i3 = this.startX;
        if (str3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring3 = str3.substring(i3);
        Intrinsics.checkNotNullExpressionValue(strSubstring3, "(this as java.lang.String).substring(startIndex)");
        st.append(strSubstring3);
        String string = st.toString();
        Intrinsics.checkNotNullExpressionValue(string, "st.apply {\n            append(queue.substring(startX))\n        }.toString()");
        return string;
    }

    @NotNull
    public final ArrayList<String> getRuleTypeList() {
        return this.ruleTypeList;
    }

    @NotNull
    public final KFunction<Boolean> getChompBalanced() {
        return this.chompBalanced;
    }
}
