package io.legado.app.utils;

import io.legado.app.lib.icu4j.CharsetDetector;
import io.legado.app.lib.icu4j.CharsetMatch;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* JADX INFO: compiled from: EncodingDetect.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/EncodingDetect.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u0012\u0010\n\u001a\u00020\b2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\b¨\u0006\f"}, d2 = {"Lio/legado/app/utils/EncodingDetect;", PackageDocumentBase.PREFIX_OPF, "()V", "getEncode", PackageDocumentBase.PREFIX_OPF, "file", "Ljava/io/File;", "bytes", PackageDocumentBase.PREFIX_OPF, "filePath", "getFileBytes", "getHtmlEncode", "reader-pro"})
public final class EncodingDetect {

    @NotNull
    public static final EncodingDetect INSTANCE = new EncodingDetect();

    private EncodingDetect() {
    }

    @Nullable
    public final String getHtmlEncode(@NotNull byte[] bytes) {
        String strSubstring;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        try {
            Charset charset = StandardCharsets.UTF_8;
            Intrinsics.checkNotNullExpressionValue(charset, "UTF_8");
            Document doc = Jsoup.parse(new String(bytes, charset));
            Elements<Element> metaTags = doc.getElementsByTag("meta");
            for (Element metaTag : metaTags) {
                String charsetStr = metaTag.attr("charset");
                Intrinsics.checkNotNullExpressionValue(charsetStr, "metaTag.attr(\"charset\")");
                if (!(charsetStr.length() == 0)) {
                    return charsetStr;
                }
                String content = metaTag.attr("content");
                String httpEquiv = metaTag.attr(NCXDocumentV3.XHTMLAttributes.http_equiv);
                Intrinsics.checkNotNullExpressionValue(httpEquiv, "httpEquiv");
                Locale locale = Locale.getDefault();
                Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
                String lowerCase = httpEquiv.toLowerCase(locale);
                Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
                if (Intrinsics.areEqual(lowerCase, "content-type")) {
                    Intrinsics.checkNotNullExpressionValue(content, "content");
                    Locale locale2 = Locale.getDefault();
                    Intrinsics.checkNotNullExpressionValue(locale2, "getDefault()");
                    String lowerCase2 = content.toLowerCase(locale2);
                    Intrinsics.checkNotNullExpressionValue(lowerCase2, "(this as java.lang.String).toLowerCase(locale)");
                    if (StringsKt.contains$default(lowerCase2, "charset", false, 2, (Object) null)) {
                        Locale locale3 = Locale.getDefault();
                        Intrinsics.checkNotNullExpressionValue(locale3, "getDefault()");
                        String lowerCase3 = content.toLowerCase(locale3);
                        Intrinsics.checkNotNullExpressionValue(lowerCase3, "(this as java.lang.String).toLowerCase(locale)");
                        strSubstring = content.substring(StringsKt.indexOf$default(lowerCase3, "charset", 0, false, 6, (Object) null) + "charset=".length());
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    } else {
                        Locale locale4 = Locale.getDefault();
                        Intrinsics.checkNotNullExpressionValue(locale4, "getDefault()");
                        String lowerCase4 = content.toLowerCase(locale4);
                        Intrinsics.checkNotNullExpressionValue(lowerCase4, "(this as java.lang.String).toLowerCase(locale)");
                        strSubstring = content.substring(StringsKt.indexOf$default(lowerCase4, ";", 0, false, 6, (Object) null) + 1);
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    }
                    String charsetStr2 = strSubstring;
                    if (!(charsetStr2.length() == 0)) {
                        return charsetStr2;
                    }
                }
            }
        } catch (Exception e) {
        }
        return getEncode(bytes);
    }

    @NotNull
    public final String getEncode(@NotNull byte[] bytes) {
        String name;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        CharsetMatch match = new CharsetDetector().setText(bytes).detect();
        return (match == null || (name = match.getName()) == null) ? Constants.CHARACTER_ENCODING : name;
    }

    @NotNull
    public final String getEncode(@NotNull String filePath) {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        return getEncode(new File(filePath));
    }

    @NotNull
    public final String getEncode(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        byte[] tempByte = getFileBytes(file);
        return getEncode(tempByte);
    }

    private final byte[] getFileBytes(File file) {
        byte[] byteArray = new byte[8000];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Throwable th = (Throwable) null;
            try {
                try {
                    FileInputStream it = fileInputStream;
                    it.read(byteArray);
                    CloseableKt.closeFinally(fileInputStream, th);
                } finally {
                }
            } catch (Throwable th2) {
                CloseableKt.closeFinally(fileInputStream, th);
                throw th2;
            }
        } catch (Exception e) {
            System.err.println(Intrinsics.stringPlus("Error: ", e));
        }
        return byteArray;
    }
}
