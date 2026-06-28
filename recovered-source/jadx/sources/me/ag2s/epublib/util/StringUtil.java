package me.ag2s.epublib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/util/StringUtil.class */
public class StringUtil {
    public static String collapsePathDots(String path) {
        String[] stringParts = path.split(TableOfContents.DEFAULT_PATH_SEPARATOR);
        List<String> parts = new ArrayList<>(Arrays.asList(stringParts));
        int i = 0;
        while (i < parts.size() - 1) {
            String currentDir = parts.get(i);
            if (currentDir.length() == 0 || currentDir.equals(".")) {
                parts.remove(i);
                i--;
            } else if (currentDir.equals("..")) {
                parts.remove(i - 1);
                parts.remove(i - 1);
                i -= 2;
            }
            i++;
        }
        StringBuilder result = new StringBuilder();
        if (path.startsWith(TableOfContents.DEFAULT_PATH_SEPARATOR)) {
            result.append('/');
        }
        for (int i2 = 0; i2 < parts.size(); i2++) {
            result.append(parts.get(i2));
            if (i2 < parts.size() - 1) {
                result.append('/');
            }
        }
        return result.toString();
    }

    public static boolean isNotBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isBlank(String text) {
        if (isEmpty(text)) {
            return true;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean endsWithIgnoreCase(String source, String suffix) {
        if (isEmpty(suffix)) {
            return true;
        }
        if (isEmpty(source) || suffix.length() > source.length()) {
            return false;
        }
        return source.substring(source.length() - suffix.length()).toLowerCase().endsWith(suffix.toLowerCase());
    }

    public static String defaultIfNull(String text) {
        return defaultIfNull(text, PackageDocumentBase.PREFIX_OPF);
    }

    public static String defaultIfNull(String text, String defaultValue) {
        if (text == null) {
            return defaultValue;
        }
        return text;
    }

    public static boolean equals(String text1, String text2) {
        if (text1 == null) {
            return text2 == null;
        }
        return text1.equals(text2);
    }

    public static String toString(Object... keyValues) {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < keyValues.length; i += 2) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(keyValues[i]);
            result.append(": ");
            Object value = null;
            if (i + 1 < keyValues.length) {
                value = keyValues[i + 1];
            }
            if (value == null) {
                result.append("<null>");
            } else {
                result.append('\'');
                result.append(value);
                result.append('\'');
            }
        }
        result.append(']');
        return result.toString();
    }

    public static int hashCode(String... values) {
        int result = 31;
        for (String value : values) {
            result ^= String.valueOf(value).hashCode();
        }
        return result;
    }

    public static String substringBefore(String text, char separator) {
        if (isEmpty(text)) {
            return text;
        }
        int sepPos = text.indexOf(separator);
        if (sepPos < 0) {
            return text;
        }
        return text.substring(0, sepPos);
    }

    public static String substringBeforeLast(String text, char separator) {
        if (isEmpty(text)) {
            return text;
        }
        int cPos = text.lastIndexOf(separator);
        if (cPos < 0) {
            return text;
        }
        return text.substring(0, cPos);
    }

    public static String substringAfterLast(String text, char separator) {
        if (isEmpty(text)) {
            return text;
        }
        int cPos = text.lastIndexOf(separator);
        if (cPos < 0) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        return text.substring(cPos + 1);
    }

    public static String substringAfter(String text, char c) {
        if (isEmpty(text)) {
            return text;
        }
        int cPos = text.indexOf(c);
        if (cPos < 0) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        return text.substring(cPos + 1);
    }

    public static String formatHtml(String text) {
        StringBuilder body = new StringBuilder();
        for (String str : text.split("\\r?\\n")) {
            String s = str.replaceAll("^\\s+|\\s+$", PackageDocumentBase.PREFIX_OPF);
            if (s.length() > 0) {
                if (s.matches("(?i)^<img\\s([^>]+)/?>$")) {
                    body.append(s.replaceAll("(?i)^<img\\s([^>]+)/?>$", "<div class=\"duokan-image-single\"><img class=\"picture-80\" $1/></div>"));
                } else {
                    body.append("<p>").append(s).append("</p>");
                }
            }
        }
        return body.toString();
    }
}
