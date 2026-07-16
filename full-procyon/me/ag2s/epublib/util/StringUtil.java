// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.util;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

public class StringUtil
{
    public static String collapsePathDots(final String path) {
        final String[] stringParts = path.split("/");
        final List<String> parts = new ArrayList<String>(Arrays.asList(stringParts));
        for (int i = 0; i < parts.size() - 1; ++i) {
            final String currentDir = parts.get(i);
            if (currentDir.length() == 0 || currentDir.equals(".")) {
                parts.remove(i);
                --i;
            }
            else if (currentDir.equals("..")) {
                parts.remove(i - 1);
                parts.remove(i - 1);
                i -= 2;
            }
        }
        final StringBuilder result = new StringBuilder();
        if (path.startsWith("/")) {
            result.append('/');
        }
        for (int j = 0; j < parts.size(); ++j) {
            result.append(parts.get(j));
            if (j < parts.size() - 1) {
                result.append('/');
            }
        }
        return result.toString();
    }
    
    public static boolean isNotBlank(final String text) {
        return !isBlank(text);
    }
    
    public static boolean isBlank(final String text) {
        if (isEmpty(text)) {
            return true;
        }
        for (int i = 0; i < text.length(); ++i) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isEmpty(final String text) {
        return text == null || text.length() == 0;
    }
    
    public static boolean endsWithIgnoreCase(final String source, final String suffix) {
        return isEmpty(suffix) || (!isEmpty(source) && suffix.length() <= source.length() && source.substring(source.length() - suffix.length()).toLowerCase().endsWith(suffix.toLowerCase()));
    }
    
    public static String defaultIfNull(final String text) {
        return defaultIfNull(text, "");
    }
    
    public static String defaultIfNull(final String text, final String defaultValue) {
        if (text == null) {
            return defaultValue;
        }
        return text;
    }
    
    public static boolean equals(final String text1, final String text2) {
        if (text1 == null) {
            return text2 == null;
        }
        return text1.equals(text2);
    }
    
    public static String toString(final Object... keyValues) {
        final StringBuilder result = new StringBuilder();
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
            }
            else {
                result.append('\'');
                result.append(value);
                result.append('\'');
            }
        }
        result.append(']');
        return result.toString();
    }
    
    public static int hashCode(final String... values) {
        int result = 31;
        for (final String value : values) {
            result ^= String.valueOf(value).hashCode();
        }
        return result;
    }
    
    public static String substringBefore(final String text, final char separator) {
        if (isEmpty(text)) {
            return text;
        }
        final int sepPos = text.indexOf(separator);
        if (sepPos < 0) {
            return text;
        }
        return text.substring(0, sepPos);
    }
    
    public static String substringBeforeLast(final String text, final char separator) {
        if (isEmpty(text)) {
            return text;
        }
        final int cPos = text.lastIndexOf(separator);
        if (cPos < 0) {
            return text;
        }
        return text.substring(0, cPos);
    }
    
    public static String substringAfterLast(final String text, final char separator) {
        if (isEmpty(text)) {
            return text;
        }
        final int cPos = text.lastIndexOf(separator);
        if (cPos < 0) {
            return "";
        }
        return text.substring(cPos + 1);
    }
    
    public static String substringAfter(final String text, final char c) {
        if (isEmpty(text)) {
            return text;
        }
        final int cPos = text.indexOf(c);
        if (cPos < 0) {
            return "";
        }
        return text.substring(cPos + 1);
    }
    
    public static String formatHtml(final String text) {
        final StringBuilder body = new StringBuilder();
        for (String s : text.split("\\r?\\n")) {
            s = s.replaceAll("^\\s+|\\s+$", "");
            if (s.length() > 0) {
                if (s.matches("(?i)^<img\\s([^>]+)/?>$")) {
                    body.append(s.replaceAll("(?i)^<img\\s([^>]+)/?>$", "<div class=\"duokan-image-single\"><img class=\"picture-80\" $1/></div>"));
                }
                else {
                    body.append("<p>").append(s).append("</p>");
                }
            }
        }
        return body.toString();
    }
}
