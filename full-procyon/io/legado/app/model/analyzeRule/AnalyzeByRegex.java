// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import org.jetbrains.annotations.Nullable;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.regex.Pattern;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J3\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00042\u0006\u0010\u0006\u001a\u00020\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\b\b\u0002\u0010\t\u001a\u00020\n?\u0006\u0002\u0010\u000bJ7\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00042\u0006\u0010\u0006\u001a\u00020\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\b\b\u0002\u0010\t\u001a\u00020\n?\u0006\u0002\u0010\u000b¡§\u0006\r" }, d2 = { "Lio/legado/app/model/analyzeRule/AnalyzeByRegex;", "", "()V", "getElement", "", "", "res", "regs", "", "index", "", "(Ljava/lang/String;[Ljava/lang/String;I)Ljava/util/List;", "getElements", "reader-pro" })
public final class AnalyzeByRegex
{
    @NotNull
    public static final AnalyzeByRegex INSTANCE;
    
    private AnalyzeByRegex() {
    }
    
    @Nullable
    public final List<String> getElement(@NotNull final String res, @NotNull final String[] regs, final int index) {
        Intrinsics.checkNotNullParameter((Object)res, "res");
        Intrinsics.checkNotNullParameter((Object)regs, "regs");
        int vIndex = index;
        final Matcher resM = Pattern.compile(regs[vIndex]).matcher(res);
        if (!resM.find()) {
            return null;
        }
        List<String> element;
        if (vIndex + 1 == regs.length) {
            final ArrayList info = new ArrayList();
            int n = 0;
            final int groupCount = resM.groupCount();
            if (n <= groupCount) {
                int groupIndex;
                do {
                    groupIndex = n;
                    ++n;
                    final ArrayList list = info;
                    final String group = resM.group(groupIndex);
                    Intrinsics.checkNotNull((Object)group);
                    list.add(group);
                } while (groupIndex != groupCount);
            }
            element = info;
        }
        else {
            final StringBuilder result = new StringBuilder();
            do {
                result.append(resM.group());
            } while (resM.find());
            final String string = result.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "result.toString()");
            element = this.getElement(string, regs, ++vIndex);
        }
        return element;
    }
    
    @NotNull
    public final List<List<String>> getElements(@NotNull final String res, @NotNull final String[] regs, final int index) {
        Intrinsics.checkNotNullParameter((Object)res, "res");
        Intrinsics.checkNotNullParameter((Object)regs, "regs");
        int vIndex = index;
        final Matcher resM = Pattern.compile(regs[vIndex]).matcher(res);
        if (!resM.find()) {
            return new ArrayList<List<String>>();
        }
        if (vIndex + 1 == regs.length) {
            final ArrayList books = new ArrayList();
            do {
                final ArrayList info = new ArrayList();
                int n = 0;
                final int groupCount = resM.groupCount();
                if (n <= groupCount) {
                    int groupIndex;
                    do {
                        groupIndex = n;
                        ++n;
                        final ArrayList list = info;
                        final String group = resM.group(groupIndex);
                        list.add((group == null) ? "" : group);
                    } while (groupIndex != groupCount);
                }
                books.add(info);
            } while (resM.find());
            return books;
        }
        final StringBuilder result = new StringBuilder();
        do {
            result.append(resM.group());
        } while (resM.find());
        final String string = result.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "result.toString()");
        return this.getElements(string, regs, ++vIndex);
    }
    
    static {
        INSTANCE = new AnalyzeByRegex();
    }
}
