// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.io.CloseableKt;
import java.io.FileInputStream;
import java.io.Closeable;
import java.io.File;
import io.legado.app.lib.icu4j.CharsetMatch;
import io.legado.app.lib.icu4j.CharsetDetector;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import java.nio.charset.Charset;
import kotlin.text.StringsKt;
import java.util.Locale;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import java.nio.charset.StandardCharsets;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004J\u0012\u0010\n\u001a\u00020\b2\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\b¡§\u0006\f" }, d2 = { "Lio/legado/app/utils/EncodingDetect;", "", "()V", "getEncode", "", "file", "Ljava/io/File;", "bytes", "", "filePath", "getFileBytes", "getHtmlEncode", "reader-pro" })
public final class EncodingDetect
{
    @NotNull
    public static final EncodingDetect INSTANCE;
    
    private EncodingDetect() {
    }
    
    @Nullable
    public final String getHtmlEncode(@NotNull final byte[] bytes) {
        Intrinsics.checkNotNullParameter((Object)bytes, "bytes");
        try {
            final Charset utf_8 = StandardCharsets.UTF_8;
            Intrinsics.checkNotNullExpressionValue((Object)utf_8, "UTF_8");
            final Document doc = Jsoup.parse(new String(bytes, utf_8));
            final Elements metaTags = doc.getElementsByTag("meta");
            String charsetStr = null;
            for (final Element metaTag : metaTags) {
                final String attr = metaTag.attr("charset");
                Intrinsics.checkNotNullExpressionValue((Object)attr, "metaTag.attr(\"charset\")");
                charsetStr = attr;
                if (charsetStr.length() != 0) {
                    return charsetStr;
                }
                final String content = metaTag.attr("content");
                final String httpEquiv = metaTag.attr("http-equiv");
                Intrinsics.checkNotNullExpressionValue((Object)httpEquiv, "httpEquiv");
                final String s = httpEquiv;
                final Locale default1 = Locale.getDefault();
                Intrinsics.checkNotNullExpressionValue((Object)default1, "getDefault()");
                final String lowerCase = s.toLowerCase(default1);
                Intrinsics.checkNotNullExpressionValue((Object)lowerCase, "(this as java.lang.String).toLowerCase(locale)");
                if (!Intrinsics.areEqual((Object)lowerCase, (Object)"content-type")) {
                    continue;
                }
                Intrinsics.checkNotNullExpressionValue((Object)content, "content");
                final String s2 = content;
                final Locale default2 = Locale.getDefault();
                Intrinsics.checkNotNullExpressionValue((Object)default2, "getDefault()");
                final String lowerCase2 = s2.toLowerCase(default2);
                Intrinsics.checkNotNullExpressionValue((Object)lowerCase2, "(this as java.lang.String).toLowerCase(locale)");
                String s5;
                if (StringsKt.contains$default((CharSequence)lowerCase2, (CharSequence)"charset", false, 2, (Object)null)) {
                    final String s3 = content;
                    final String s4 = content;
                    final Locale default3 = Locale.getDefault();
                    Intrinsics.checkNotNullExpressionValue((Object)default3, "getDefault()");
                    final String lowerCase3 = s4.toLowerCase(default3);
                    Intrinsics.checkNotNullExpressionValue((Object)lowerCase3, "(this as java.lang.String).toLowerCase(locale)");
                    Intrinsics.checkNotNullExpressionValue((Object)(s5 = s3.substring(StringsKt.indexOf$default((CharSequence)lowerCase3, "charset", 0, false, 6, (Object)null) + "charset=".length())), "(this as java.lang.String).substring(startIndex)");
                }
                else {
                    final String s6 = content;
                    final String s7 = content;
                    final Locale default4 = Locale.getDefault();
                    Intrinsics.checkNotNullExpressionValue((Object)default4, "getDefault()");
                    final String lowerCase4 = s7.toLowerCase(default4);
                    Intrinsics.checkNotNullExpressionValue((Object)lowerCase4, "(this as java.lang.String).toLowerCase(locale)");
                    Intrinsics.checkNotNullExpressionValue((Object)(s5 = s6.substring(StringsKt.indexOf$default((CharSequence)lowerCase4, ";", 0, false, 6, (Object)null) + 1)), "(this as java.lang.String).substring(startIndex)");
                }
                charsetStr = s5;
                if (charsetStr.length() != 0) {
                    return charsetStr;
                }
            }
            return this.getEncode(bytes);
        }
        catch (final Exception ex) {}
        return this.getEncode(bytes);
    }
    
    @NotNull
    public final String getEncode(@NotNull final byte[] bytes) {
        Intrinsics.checkNotNullParameter((Object)bytes, "bytes");
        final CharsetMatch detect;
        final CharsetMatch match = detect = new CharsetDetector().setText(bytes).detect();
        String s;
        if (detect == null) {
            s = "UTF-8";
        }
        else {
            final String name = detect.getName();
            s = ((name == null) ? "UTF-8" : name);
        }
        return s;
    }
    
    @NotNull
    public final String getEncode(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        return this.getEncode(new File(filePath));
    }
    
    @NotNull
    public final String getEncode(@NotNull final File file) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        final byte[] tempByte = this.getFileBytes(file);
        return this.getEncode(tempByte);
    }
    
    private final byte[] getFileBytes(final File file) {
        final byte[] byteArray = new byte[8000];
        try {
            final Closeable closeable = new FileInputStream(file);
            Throwable t = null;
            try {
                final FileInputStream it = (FileInputStream)closeable;
                final int n = 0;
                it.read(byteArray);
            }
            catch (final Throwable t2) {
                t = t2;
                throw t2;
            }
            finally {
                CloseableKt.closeFinally(closeable, t);
            }
        }
        catch (final Exception e) {
            System.err.println(Intrinsics.stringPlus("Error: ", (Object)e));
        }
        return byteArray;
    }
    
    static {
        INSTANCE = new EncodingDetect();
    }
}
