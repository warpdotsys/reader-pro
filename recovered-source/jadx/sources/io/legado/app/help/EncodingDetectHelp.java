package io.legado.app.help;

import io.legado.app.utils.TextUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import me.ag2s.epublib.epub.NCXDocumentV3;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/EncodingDetectHelp.class */
public class EncodingDetectHelp {
    public static String getHtmlEncode(byte[] bytes) {
        String charsetStr;
        try {
            Document doc = Jsoup.parse(new String(bytes, StandardCharsets.UTF_8));
            Elements<Element> metaTags = doc.getElementsByTag("meta");
            for (Element metaTag : metaTags) {
                String charsetStr2 = metaTag.attr("charset");
                if (!TextUtils.isEmpty(charsetStr2)) {
                    return charsetStr2;
                }
                String content = metaTag.attr("content");
                String http_equiv = metaTag.attr(NCXDocumentV3.XHTMLAttributes.http_equiv);
                if (http_equiv.toLowerCase().equals("content-type")) {
                    if (content.toLowerCase().contains("charset")) {
                        charsetStr = content.substring(content.toLowerCase().indexOf("charset") + "charset=".length());
                    } else {
                        charsetStr = content.substring(content.toLowerCase().indexOf(";") + 1);
                    }
                    if (!TextUtils.isEmpty(charsetStr)) {
                        return charsetStr;
                    }
                }
            }
        } catch (Exception e) {
        }
        return getJavaEncode(bytes);
    }

    public static String getJavaEncode(byte[] bytes) {
        int len = bytes.length > 2000 ? 2000 : bytes.length;
        byte[] cBytes = new byte[len];
        System.arraycopy(bytes, 0, cBytes, 0, len);
        BytesEncodingDetect bytesEncodingDetect = new BytesEncodingDetect();
        String code = BytesEncodingDetect.javaname[bytesEncodingDetect.detectEncoding(cBytes)];
        if ("Unicode".equals(code) && cBytes[0] == -1) {
            code = "UTF-16LE";
        }
        return code;
    }

    public static String getJavaEncode(String filePath) {
        BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(new File(filePath))];
        if ("Unicode".equals(fileCode)) {
            byte[] tempByte = BytesEncodingDetect.getFileBytes(new File(filePath));
            if (tempByte[0] == -1) {
                fileCode = "UTF-16LE";
            }
        }
        return fileCode;
    }

    public static String getJavaEncode(File file) {
        BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(file)];
        if ("Unicode".equals(fileCode)) {
            byte[] tempByte = BytesEncodingDetect.getFileBytes(file);
            if (tempByte[0] == -1) {
                fileCode = "UTF-16LE";
            }
        }
        return fileCode;
    }
}
