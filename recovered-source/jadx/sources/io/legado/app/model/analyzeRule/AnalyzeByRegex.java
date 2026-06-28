package io.legado.app.model.analyzeRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: AnalyzeByRegex.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/AnalyzeByRegex.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J3\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00042\u0006\u0010\u0006\u001a\u00020\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ7\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00042\u0006\u0010\u0006\u001a\u00020\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000b¨\u0006\r"}, d2 = {"Lio/legado/app/model/analyzeRule/AnalyzeByRegex;", PackageDocumentBase.PREFIX_OPF, "()V", "getElement", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "res", "regs", PackageDocumentBase.PREFIX_OPF, "index", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;[Ljava/lang/String;I)Ljava/util/List;", "getElements", "reader-pro"})
public final class AnalyzeByRegex {

    @NotNull
    public static final AnalyzeByRegex INSTANCE = new AnalyzeByRegex();

    private AnalyzeByRegex() {
    }

    public static /* synthetic */ List getElement$default(AnalyzeByRegex analyzeByRegex, String str, String[] strArr, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return analyzeByRegex.getElement(str, strArr, i);
    }

    @Nullable
    public final List<String> getElement(@NotNull String res, @NotNull String[] regs, int index) {
        int groupIndex;
        Intrinsics.checkNotNullParameter(res, "res");
        Intrinsics.checkNotNullParameter(regs, "regs");
        Matcher resM = Pattern.compile(regs[index]).matcher(res);
        if (!resM.find()) {
            return null;
        }
        if (index + 1 == regs.length) {
            ArrayList info = new ArrayList();
            int i = 0;
            int iGroupCount = resM.groupCount();
            if (0 <= iGroupCount) {
                do {
                    groupIndex = i;
                    i++;
                    String strGroup = resM.group(groupIndex);
                    Intrinsics.checkNotNull(strGroup);
                    info.add(strGroup);
                } while (groupIndex != iGroupCount);
            }
            return info;
        }
        StringBuilder result = new StringBuilder();
        do {
            result.append(resM.group());
        } while (resM.find());
        String string = result.toString();
        Intrinsics.checkNotNullExpressionValue(string, "result.toString()");
        int vIndex = index + 1;
        return getElement(string, regs, vIndex);
    }

    public static /* synthetic */ List getElements$default(AnalyzeByRegex analyzeByRegex, String str, String[] strArr, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return analyzeByRegex.getElements(str, strArr, i);
    }

    @NotNull
    public final List<List<String>> getElements(@NotNull String res, @NotNull String[] regs, int index) {
        int groupIndex;
        Intrinsics.checkNotNullParameter(res, "res");
        Intrinsics.checkNotNullParameter(regs, "regs");
        Matcher resM = Pattern.compile(regs[index]).matcher(res);
        if (!resM.find()) {
            return new ArrayList();
        }
        if (index + 1 == regs.length) {
            ArrayList books = new ArrayList();
            do {
                ArrayList info = new ArrayList();
                int i = 0;
                int iGroupCount = resM.groupCount();
                if (0 <= iGroupCount) {
                    do {
                        groupIndex = i;
                        i++;
                        String strGroup = resM.group(groupIndex);
                        info.add(strGroup == null ? PackageDocumentBase.PREFIX_OPF : strGroup);
                    } while (groupIndex != iGroupCount);
                }
                books.add(info);
            } while (resM.find());
            return books;
        }
        StringBuilder result = new StringBuilder();
        do {
            result.append(resM.group());
        } while (resM.find());
        String string = result.toString();
        Intrinsics.checkNotNullExpressionValue(string, "result.toString()");
        int vIndex = index + 1;
        return getElements(string, regs, vIndex);
    }
}
