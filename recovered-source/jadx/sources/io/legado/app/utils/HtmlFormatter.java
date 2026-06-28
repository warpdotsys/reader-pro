package io.legado.app.utils;

import io.legado.app.model.analyzeRule.AnalyzeUrl;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: HtmlFormatter.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/HtmlFormatter.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000e\u001a\u00020\u0004J\u001c\u0010\u000f\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lio/legado/app/utils/HtmlFormatter;", PackageDocumentBase.PREFIX_OPF, "()V", "commentRegex", "Lkotlin/text/Regex;", "formatImagePattern", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "notImgHtmlRegex", "otherHtmlRegex", "wrapHtmlRegex", PackageDocumentBase.DCTags.format, PackageDocumentBase.PREFIX_OPF, "html", "otherRegex", "formatKeepImg", "redirectUrl", "Ljava/net/URL;", "reader-pro"})
public final class HtmlFormatter {

    @NotNull
    public static final HtmlFormatter INSTANCE = new HtmlFormatter();

    @NotNull
    private static final Regex wrapHtmlRegex = new Regex("</?(?:div|p|br|hr|h\\d|article|dd|dl)[^>]*>");

    @NotNull
    private static final Regex commentRegex = new Regex("<!--[^>]*-->");

    @NotNull
    private static final Regex notImgHtmlRegex = new Regex("</?(?!img)[a-zA-Z]+(?=[ >])[^<>]*>");

    @NotNull
    private static final Regex otherHtmlRegex = new Regex("</?[a-zA-Z]+(?=[ >])[^<>]*>");
    private static final Pattern formatImagePattern = Pattern.compile("<img[^>]*src *= *\"([^\"{]*\\{(?:[^{}]|\\{[^}]+\\})+\\})\"[^>]*>|<img[^>]*data-[^=]*= *\"([^\"]*)\"[^>]*>|<img[^>]*src *= *\"([^\"]*)\"[^>]*>", 2);

    private HtmlFormatter() {
    }

    public static /* synthetic */ String format$default(HtmlFormatter htmlFormatter, String str, Regex regex, int i, Object obj) {
        if ((i & 2) != 0) {
            regex = otherHtmlRegex;
        }
        return htmlFormatter.format(str, regex);
    }

    @NotNull
    public final String format(@Nullable String html, @NotNull Regex otherRegex) {
        Intrinsics.checkNotNullParameter(otherRegex, "otherRegex");
        if (html == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        return new Regex("[\\n\\s]+$").replace(new Regex("^[\\n\\s]+").replace(new Regex("\\s*\\n+\\s*").replace(otherRegex.replace(commentRegex.replace(wrapHtmlRegex.replace(html, "\n"), PackageDocumentBase.PREFIX_OPF), PackageDocumentBase.PREFIX_OPF), "\n\u3000\u3000"), "\u3000\u3000"), PackageDocumentBase.PREFIX_OPF);
    }

    public static /* synthetic */ String formatKeepImg$default(HtmlFormatter htmlFormatter, String str, URL url, int i, Object obj) {
        if ((i & 2) != 0) {
            url = null;
        }
        return htmlFormatter.formatKeepImg(str, url);
    }

    @NotNull
    public final String formatKeepImg(@Nullable String html, @Nullable URL redirectUrl) {
        String strSubstring;
        String str;
        String strGroup;
        if (html == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        String keepImgHtml = format(html, notImgHtmlRegex);
        Matcher matcher = formatImagePattern.matcher(keepImgHtml);
        int appendPos = 0;
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String str2 = PackageDocumentBase.PREFIX_OPF;
            StringBuffer stringBuffer = sb;
            CharSequence[] charSequenceArr = new CharSequence[2];
            int iStart = matcher.start();
            if (keepImgHtml == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring2 = keepImgHtml.substring(appendPos, iStart);
            Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            charSequenceArr[0] = strSubstring2;
            CharSequence[] charSequenceArr2 = charSequenceArr;
            char c = 1;
            StringBuilder sbAppend = new StringBuilder().append("<img src=\"");
            NetworkUtils networkUtils = NetworkUtils.INSTANCE;
            URL url = redirectUrl;
            String it = matcher.group(1);
            if (it == null) {
                str = null;
            } else {
                Matcher urlMatcher = AnalyzeUrl.INSTANCE.getParamPattern().matcher(it);
                if (urlMatcher.find()) {
                    String strSubstring3 = it.substring(urlMatcher.end());
                    Intrinsics.checkNotNullExpressionValue(strSubstring3, "(this as java.lang.String).substring(startIndex)");
                    str2 = String.valueOf(',') + strSubstring3;
                    strSubstring = it.substring(0, urlMatcher.start());
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                } else {
                    strSubstring = it;
                }
                String str3 = strSubstring;
                stringBuffer = stringBuffer;
                charSequenceArr2 = charSequenceArr2;
                c = 1;
                sbAppend = sbAppend;
                networkUtils = networkUtils;
                url = url;
                str = str3;
            }
            String str4 = str;
            String strGroup2 = str4 == null ? matcher.group(2) : str4;
            if (strGroup2 == null) {
                strGroup = matcher.group(3);
                Intrinsics.checkNotNull(strGroup);
            } else {
                strGroup = strGroup2;
            }
            charSequenceArr2[c] = sbAppend.append(networkUtils.getAbsoluteURL(url, strGroup)).append(str2).append("\">").toString();
            StringsKt.append(stringBuffer, charSequenceArr);
            appendPos = matcher.end();
        }
        if (appendPos < keepImgHtml.length()) {
            int length = keepImgHtml.length();
            if (keepImgHtml == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring4 = keepImgHtml.substring(appendPos, length);
            Intrinsics.checkNotNullExpressionValue(strSubstring4, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            sb.append(strSubstring4);
        }
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "sb.toString()");
        return string;
    }
}
