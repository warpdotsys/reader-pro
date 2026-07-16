//
// Decompiled by Procyon v0.6.0
//

package io.legado.app.help;

import java.io.File;
import java.util.Iterator;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import io.legado.app.utils.TextUtils;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import java.nio.charset.StandardCharsets;

public class EncodingDetectHelp
{
    public static String getHtmlEncode(final byte[] bytes) {
        try {
            final Document doc = Jsoup.parse(new String(bytes, StandardCharsets.UTF_8));
            final Elements metaTags = doc.getElementsByTag("meta");
            for (final Element metaTag : metaTags) {
                String charsetStr = metaTag.attr("charset");
                if (!TextUtils.isEmpty(charsetStr)) {
                    return charsetStr;
                }
                final String content = metaTag.attr("content");
                final String http_equiv = metaTag.attr("http-equiv");
                if (!http_equiv.toLowerCase().equals("content-type")) {
                    continue;
                }
                if (content.toLowerCase().contains("charset")) {
                    charsetStr = content.substring(content.toLowerCase().indexOf("charset") + "charset=".length());
                }
                else {
                    charsetStr = content.substring(content.toLowerCase().indexOf(";") + 1);
                }
                if (!TextUtils.isEmpty(charsetStr)) {
                    return charsetStr;
                }
            }
        }
        catch (final Exception ex) {}
        return getJavaEncode(bytes);
    }

    public static String getJavaEncode(final byte[] bytes) {
        final int len = (bytes.length > 2000) ? 2000 : bytes.length;
        final byte[] cBytes = new byte[len];
        System.arraycopy(bytes, 0, cBytes, 0, len);
        final BytesEncodingDetect bytesEncodingDetect = new BytesEncodingDetect();
        String code = BytesEncodingDetect.javaname[bytesEncodingDetect.detectEncoding(cBytes)];
        if ("Unicode".equals(code) && cBytes[0] == -1) {
            code = "UTF-16LE";
        }
        return code;
    }

    public static String getJavaEncode(final String filePath) {
        final BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(new File(filePath))];
        if ("Unicode".equals(fileCode)) {
            final byte[] tempByte = BytesEncodingDetect.getFileBytes(new File(filePath));
            if (tempByte[0] == -1) {
                fileCode = "UTF-16LE";
            }
        }
        return fileCode;
    }

    public static String getJavaEncode(final File file) {
        final BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(file)];
        if ("Unicode".equals(fileCode)) {
            final byte[] tempByte = BytesEncodingDetect.getFileBytes(file);
            if (tempByte[0] == -1) {
                fileCode = "UTF-16LE";
            }
        }
        return fileCode;
    }
}
