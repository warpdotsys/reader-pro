package io.legado.app.utils;

import io.legado.app.constant.AppPattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: StringExtensions.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/StringExtensionsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002\u001a\f\u0010\u0004\u001a\u00020\u0002*\u0004\u0018\u00010\u0002\u001a\f\u0010\u0005\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\u0007\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\b\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\t\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\f\u0010\n\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\u0016\u0010\u000b\u001a\u00020\u0006*\u0004\u0018\u00010\u00022\b\b\u0002\u0010\f\u001a\u00020\u0006\u001a\f\u0010\r\u001a\u00020\u0006*\u0004\u0018\u00010\u0002\u001a\u000e\u0010\u000e\u001a\u0004\u0018\u00010\u0002*\u0004\u0018\u00010\u0002\u001a)\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0011\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0010\"\u00020\u0002¢\u0006\u0002\u0010\u0012\u001a'\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0001¢\u0006\u0002\u0010\u0016\u001a\u0012\u0010\u0017\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002\u001a\u0015\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u0002¢\u0006\u0002\u0010\u001a¨\u0006\u001b"}, d2 = {"cnCompare", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "other", "htmlFormat", "isAbsUrl", PackageDocumentBase.PREFIX_OPF, "isDataUrl", "isJson", "isJsonArray", "isJsonObject", "isTrue", "nullIsTrue", "isXml", "safeTrim", "splitNotBlank", PackageDocumentBase.PREFIX_OPF, "delimiter", "(Ljava/lang/String;[Ljava/lang/String;)[Ljava/lang/String;", "regex", "Lkotlin/text/Regex;", "limit", "(Ljava/lang/String;Lkotlin/text/Regex;I)[Ljava/lang/String;", "startWithIgnoreCase", "start", "toStringArray", "(Ljava/lang/String;)[Ljava/lang/String;", "reader-pro"})
public final class StringExtensionsKt {
    @Nullable
    public static final String safeTrim(@Nullable String $this$safeTrim) {
        String str = $this$safeTrim;
        if (str == null || StringsKt.isBlank(str)) {
            return null;
        }
        if ($this$safeTrim == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        return StringsKt.trim($this$safeTrim).toString();
    }

    public static final boolean isAbsUrl(@Nullable String $this$isAbsUrl) {
        String str = $this$isAbsUrl;
        if (str == null || StringsKt.isBlank(str)) {
            return false;
        }
        return StringsKt.startsWith($this$isAbsUrl, "http://", true) || StringsKt.startsWith($this$isAbsUrl, "https://", true);
    }

    public static final boolean isDataUrl(@Nullable String $this$isDataUrl) {
        if ($this$isDataUrl == null) {
            return false;
        }
        return AppPattern.INSTANCE.getDataUriRegex().matches($this$isDataUrl);
    }

    public static final boolean isJson(@Nullable String $this$isJson) {
        if ($this$isJson == null) {
            return false;
        }
        String str = StringsKt.trim($this$isJson).toString();
        return (StringsKt.startsWith$default(str, "{", false, 2, (Object) null) && StringsKt.endsWith$default(str, "}", false, 2, (Object) null)) ? true : StringsKt.startsWith$default(str, "[", false, 2, (Object) null) && StringsKt.endsWith$default(str, "]", false, 2, (Object) null);
    }

    public static final boolean isJsonObject(@Nullable String $this$isJsonObject) {
        if ($this$isJsonObject == null) {
            return false;
        }
        String str = StringsKt.trim($this$isJsonObject).toString();
        return StringsKt.startsWith$default(str, "{", false, 2, (Object) null) && StringsKt.endsWith$default(str, "}", false, 2, (Object) null);
    }

    public static final boolean isJsonArray(@Nullable String $this$isJsonArray) {
        if ($this$isJsonArray == null) {
            return false;
        }
        String str = StringsKt.trim($this$isJsonArray).toString();
        return StringsKt.startsWith$default(str, "[", false, 2, (Object) null) && StringsKt.endsWith$default(str, "]", false, 2, (Object) null);
    }

    public static final boolean isXml(@Nullable String $this$isXml) {
        if ($this$isXml == null) {
            return false;
        }
        String str = StringsKt.trim($this$isXml).toString();
        return StringsKt.startsWith$default(str, "<", false, 2, (Object) null) && StringsKt.endsWith$default(str, ">", false, 2, (Object) null);
    }

    public static /* synthetic */ boolean isTrue$default(String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return isTrue(str, z);
    }

    public static final boolean isTrue(@Nullable String $this$isTrue, boolean nullIsTrue) {
        String str = $this$isTrue;
        if ((str == null || StringsKt.isBlank(str)) || Intrinsics.areEqual($this$isTrue, "null")) {
            return nullIsTrue;
        }
        return !new Regex("\\s*(?i)(false|no|not|0)\\s*").matches($this$isTrue);
    }

    @NotNull
    public static final String htmlFormat(@Nullable String $this$htmlFormat) {
        String str = $this$htmlFormat;
        if (str == null || StringsKt.isBlank(str)) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        return new Regex("[\\n\\s]+$").replace(new Regex("^[\\n\\s]+").replace(new Regex("\\s*\\n+\\s*").replace(new Regex("<[script>]*.*?>|&nbsp;").replace(new Regex("(?i)<(br[\\s/]*|/*p\\b.*?|/*div\\b.*?)>").replace($this$htmlFormat, "\n"), PackageDocumentBase.PREFIX_OPF), "\n\u3000\u3000"), "\u3000\u3000"), PackageDocumentBase.PREFIX_OPF);
    }

    @NotNull
    public static final String[] splitNotBlank(@NotNull String $this$splitNotBlank, @NotNull String... delimiter) {
        Intrinsics.checkNotNullParameter($this$splitNotBlank, "<this>");
        Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        Iterable $this$map$iv = StringsKt.split$default($this$splitNotBlank, (String[]) Arrays.copyOf(delimiter, delimiter.length), false, 0, 6, (Object) null);
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            String it = (String) item$iv$iv;
            if (it == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            destination$iv$iv.add(StringsKt.trim(it).toString());
        }
        Iterable $this$filterNot$iv = (List) destination$iv$iv;
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv : $this$filterNot$iv) {
            if (!StringsKt.isBlank((String) element$iv$iv)) {
                destination$iv$iv2.add(element$iv$iv);
            }
        }
        Collection $this$toTypedArray$iv = (List) destination$iv$iv2;
        Object[] array = $this$toTypedArray$iv.toArray(new String[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (String[]) array;
    }

    @NotNull
    public static final String[] splitNotBlank(@NotNull String $this$splitNotBlank, @NotNull Regex regex, int limit) {
        Intrinsics.checkNotNullParameter($this$splitNotBlank, "<this>");
        Intrinsics.checkNotNullParameter(regex, "regex");
        Iterable $this$map$iv = regex.split($this$splitNotBlank, limit);
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            String it = (String) item$iv$iv;
            if (it == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            destination$iv$iv.add(StringsKt.trim(it).toString());
        }
        Iterable $this$filterNot$iv = (List) destination$iv$iv;
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv : $this$filterNot$iv) {
            if (!StringsKt.isBlank((String) element$iv$iv)) {
                destination$iv$iv2.add(element$iv$iv);
            }
        }
        Collection $this$toTypedArray$iv = (List) destination$iv$iv2;
        Object[] array = $this$toTypedArray$iv.toArray(new String[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (String[]) array;
    }

    public static /* synthetic */ String[] splitNotBlank$default(String str, Regex regex, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return splitNotBlank(str, regex, i);
    }

    public static final boolean startWithIgnoreCase(@NotNull String $this$startWithIgnoreCase, @NotNull String start) {
        Intrinsics.checkNotNullParameter($this$startWithIgnoreCase, "<this>");
        Intrinsics.checkNotNullParameter(start, "start");
        if (StringsKt.isBlank($this$startWithIgnoreCase)) {
            return false;
        }
        return StringsKt.startsWith($this$startWithIgnoreCase, start, true);
    }

    public static final int cnCompare(@NotNull String $this$cnCompare, @NotNull String other) {
        Intrinsics.checkNotNullParameter($this$cnCompare, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return $this$cnCompare.compareTo(other);
    }

    @NotNull
    public static final String[] toStringArray(@NotNull String $this$toStringArray) {
        String[] strArr;
        Intrinsics.checkNotNullParameter($this$toStringArray, "<this>");
        int codePointIndex = 0;
        try {
            int iCodePointCount = $this$toStringArray.codePointCount(0, $this$toStringArray.length());
            String[] strArr2 = new String[iCodePointCount];
            for (int i = 0; i < iCodePointCount; i++) {
                int start = codePointIndex;
                codePointIndex = $this$toStringArray.offsetByCodePoints(start, 1);
                String strSubstring = $this$toStringArray.substring(start, codePointIndex);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                strArr2[i] = strSubstring;
            }
            strArr = strArr2;
        } catch (Exception e) {
            Collection $this$toTypedArray$iv = StringsKt.split$default($this$toStringArray, new String[]{PackageDocumentBase.PREFIX_OPF}, false, 0, 6, (Object) null);
            Object[] array = $this$toTypedArray$iv.toArray(new String[0]);
            if (array == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            strArr = (String[]) array;
        }
        return strArr;
    }
}
