// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.text.StringsKt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;
import kotlin.Result$Companion;
import kotlin.ResultKt;
import kotlin.Result;
import kotlin.text.Regex;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0010\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\n2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011J\u000e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\nJ\u0016\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\nJ\u0016\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nJ\u000e\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nJ\u000e\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nJ\u000e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\nJ\u000e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\nJ\u000e\u0010#\u001a\u00020!2\u0006\u0010$\u001a\u00020\nJ\u0012\u0010%\u001a\u0004\u0018\u00010\n2\b\u0010&\u001a\u0004\u0018\u00010\nJ\u0016\u0010'\u001a\u00020\n2\u0006\u0010$\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u0006J\u0010\u0010)\u001a\u00020\u00062\b\u0010$\u001a\u0004\u0018\u00010\nJ\u000e\u0010*\u001a\u00020\n2\u0006\u0010$\u001a\u00020\nJ\u000e\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\u0016J\u000e\u0010-\u001a\u00020\n2\u0006\u0010.\u001a\u00020\nJ\u0010\u0010/\u001a\u00020\n2\b\u00100\u001a\u0004\u0018\u00010\nR\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T?\u0006\u0002\n\u0000R \u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00048BX\u0082\u0004?\u0006\u0006\u001a\u0004\b\r\u0010\u000e¡§\u00061" }, d2 = { "Lio/legado/app/utils/StringUtils;", "", "()V", "ChnMap", "Ljava/util/HashMap;", "", "", "DAY_OF_YESTERDAY", "HOUR_OF_DAY", "TAG", "", "TIME_UNIT", "chnMap", "getChnMap", "()Ljava/util/HashMap;", "byteToHexString", "bytes", "", "chineseNumToInt", "chNum", "dateConvert", "time", "", "pattern", "source", "formatHtml", "html", "fullToHalf", "input", "halfToFull", "hexStringToByte", "hexString", "isContainNumber", "", "company", "isNumeric", "str", "removeUTFCharacters", "data", "repeat", "n", "stringToInt", "toFirstCapital", "toSize", "length", "trim", "s", "wordCountFormat", "wc", "reader-pro" })
public final class StringUtils
{
    @NotNull
    public static final StringUtils INSTANCE;
    @NotNull
    private static final String TAG;
    private static final int HOUR_OF_DAY = 24;
    private static final int DAY_OF_YESTERDAY = 2;
    private static final int TIME_UNIT = 60;
    @NotNull
    private static final HashMap<Character, Integer> ChnMap;
    
    private StringUtils() {
    }
    
    private final HashMap<Character, Integer> getChnMap() {
        final HashMap map = new HashMap();
        String cnStr = "\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341";
        final char[] charArray = cnStr.toCharArray();
        Intrinsics.checkNotNullExpressionValue((Object)charArray, "(this as java.lang.String).toCharArray()");
        char[] c = charArray;
        int j = 0;
        do {
            final int i = j;
            ++j;
            map.put(c[i], i);
        } while (j <= 10);
        cnStr = "\u3007\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe";
        final char[] charArray2 = cnStr.toCharArray();
        Intrinsics.checkNotNullExpressionValue((Object)charArray2, "(this as java.lang.String).toCharArray()");
        c = charArray2;
        int k = 0;
        do {
            final int i = k;
            ++k;
            map.put(c[i], i);
        } while (k <= 10);
        map.put('\u4e24', 2);
        map.put('\u767e', 100);
        map.put('\u4f70', 100);
        map.put('\u5343', 1000);
        map.put('\u4edf', 1000);
        map.put('\u4e07', 10000);
        map.put('\u4ebf', 100000000);
        return map;
    }
    
    @NotNull
    public final String dateConvert(final long time, @NotNull final String pattern) {
        Intrinsics.checkNotNullParameter((Object)pattern, "pattern");
        final Date date = new Date(time);
        final SimpleDateFormat format = new SimpleDateFormat(pattern);
        final String format2 = format.format(date);
        Intrinsics.checkNotNullExpressionValue((Object)format2, "format.format(date)");
        return format2;
    }
    
    @NotNull
    public final String dateConvert(@NotNull final String source, @NotNull final String pattern) {
        Intrinsics.checkNotNullParameter((Object)source, "source");
        Intrinsics.checkNotNullParameter((Object)pattern, "pattern");
        final SimpleDateFormat format = new SimpleDateFormat(pattern);
        final Calendar calendar = Calendar.getInstance();
        try {
            final Date date = format.parse(source);
            final long curTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            final long difSec = Math.abs((curTime - date.getTime()) / 1000);
            final long difMin = difSec / 60;
            final long difHour = difMin / 60;
            final long difDate = difHour / 60;
            final int oldHour = calendar.get(10);
            if (oldHour != 0) {
                String s;
                if (difSec < 60L) {
                    s = difSec + "\u79d2\u524d";
                }
                else if (difMin < 60L) {
                    s = difMin + "\u5206\u949f\u524d";
                }
                else if (difHour < 24L) {
                    s = difHour + "\u5c0f\u65f6\u524d";
                }
                else if (difDate < 2L) {
                    s = "\u6628\u5929";
                }
                else {
                    final SimpleDateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                    final String format2 = convertFormat.format(date);
                    Intrinsics.checkNotNullExpressionValue((Object)format2, "{\n                    val convertFormat = SimpleDateFormat(\"yyyy-MM-dd\")\n                    convertFormat.format(date)\n                }");
                    s = format2;
                }
                return s;
            }
            if (difDate == 0L) {
                return "\u4eca\u5929";
            }
            if (difDate < 2L) {
                return "\u6628\u5929";
            }
            final SimpleDateFormat convertFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            final String format3 = convertFormat2.format(date);
            Intrinsics.checkNotNullExpressionValue((Object)format3, "convertFormat.format(date)");
            return format3;
        }
        catch (final ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    @NotNull
    public final String toSize(final long length) {
        if (length <= 0L) {
            return "0";
        }
        final String[] units = { "b", "kb", "M", "G", "T" };
        final int digitGroups = (int)(Math.log10((double)length) / Math.log10(1024.0));
        return new DecimalFormat("#,##0.##").format(length / Math.pow(1024.0, digitGroups)) + ' ' + units[digitGroups];
    }
    
    @NotNull
    public final String toFirstCapital(@NotNull final String str) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        final String substring = str.substring(0, 1);
        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String s = substring;
        final Locale default1 = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue((Object)default1, "getDefault()");
        final Locale locale = default1;
        final String s2 = s;
        if (s2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        final String upperCase = s2.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue((Object)upperCase, "(this as java.lang.String).toUpperCase(locale)");
        final String substring2 = str.substring(1);
        Intrinsics.checkNotNullExpressionValue((Object)substring2, "(this as java.lang.String).substring(startIndex)");
        return Intrinsics.stringPlus(upperCase, (Object)substring2);
    }
    
    @NotNull
    public final String halfToFull(@NotNull final String input) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        final char[] charArray = input.toCharArray();
        Intrinsics.checkNotNullExpressionValue((Object)charArray, "(this as java.lang.String).toCharArray()");
        final char[] c = charArray;
        int j = 0;
        final int n = c.length - 1;
        if (j <= n) {
            do {
                final int i = j;
                ++j;
                if (c[i] == ' ') {
                    c[i] = '\u3000';
                }
                else {
                    final char c2 = c[i];
                    if ('!' > c2 || c2 > '~') {
                        continue;
                    }
                    c[i] += '\ufee0';
                }
            } while (j <= n);
        }
        return new String(c);
    }
    
    @NotNull
    public final String fullToHalf(@NotNull final String input) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        final char[] charArray = input.toCharArray();
        Intrinsics.checkNotNullExpressionValue((Object)charArray, "(this as java.lang.String).toCharArray()");
        final char[] c = charArray;
        int j = 0;
        final int n = c.length - 1;
        if (j <= n) {
            do {
                final int i = j;
                ++j;
                if (c[i] == '\u3000') {
                    c[i] = ' ';
                }
                else {
                    final char c2 = c[i];
                    if ('\uff01' > c2 || c2 > '\uff5e') {
                        continue;
                    }
                    c[i] -= '\ufee0';
                }
            } while (j <= n);
        }
        return new String(c);
    }
    
    public final int chineseNumToInt(@NotNull final String chNum) {
        Intrinsics.checkNotNullParameter((Object)chNum, "chNum");
        int result = 0;
        int tmp = 0;
        int billion = 0;
        final char[] charArray = chNum.toCharArray();
        Intrinsics.checkNotNullExpressionValue((Object)charArray, "(this as java.lang.String).toCharArray()");
        final char[] cn = charArray;
        if (cn.length > 1 && new Regex("^[\u3007\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396]$").matches((CharSequence)chNum)) {
            int k = 0;
            final int n = cn.length - 1;
            if (k <= n) {
                do {
                    final int i = k;
                    ++k;
                    final char[] array = cn;
                    final int n2 = i;
                    final int n3 = 48;
                    final Integer value = StringUtils.ChnMap.get(cn[i]);
                    Intrinsics.checkNotNull((Object)value);
                    final Integer n4 = value;
                    Intrinsics.checkNotNullExpressionValue((Object)n4, "ChnMap[cn[i]]!!");
                    array[n2] = (char)(n3 + n4.intValue());
                } while (k <= n);
            }
            return Integer.parseInt(new String(cn));
        }
        Object o;
        try {
            final Result$Companion companion = Result.Companion;
            final int n5 = 0;
            int l = 0;
            final int n6 = cn.length - 1;
            if (l <= n6) {
                do {
                    final int j = l;
                    ++l;
                    final Integer value2 = StringUtils.ChnMap.get(cn[j]);
                    Intrinsics.checkNotNull((Object)value2);
                    final Integer n7 = value2;
                    Intrinsics.checkNotNullExpressionValue((Object)n7, "ChnMap[cn[i]]!!");
                    final int tmpNum = n7.intValue();
                    if (tmpNum == 100000000) {
                        result += tmp;
                        result *= tmpNum;
                        billion = billion * 100000000 + result;
                        result = 0;
                        tmp = 0;
                    }
                    else if (tmpNum == 10000) {
                        result += tmp;
                        result *= tmpNum;
                        tmp = 0;
                    }
                    else if (tmpNum >= 10) {
                        if (tmp == 0) {
                            tmp = 1;
                        }
                        result += tmpNum * tmp;
                        tmp = 0;
                    }
                    else {
                        int n11 = 0;
                        Label_0437: {
                            if (j >= 2 && j == cn.length - 1) {
                                final Integer value3 = StringUtils.ChnMap.get(cn[j - 1]);
                                Intrinsics.checkNotNull((Object)value3);
                                final Integer n8 = value3;
                                Intrinsics.checkNotNullExpressionValue((Object)n8, "ChnMap[cn[i - 1]]!!");
                                if (n8.intValue() > 10) {
                                    final int n9 = tmpNum;
                                    final Integer value4 = StringUtils.ChnMap.get(cn[j - 1]);
                                    Intrinsics.checkNotNull((Object)value4);
                                    final Integer n10 = value4;
                                    Intrinsics.checkNotNullExpressionValue((Object)n10, "ChnMap[cn[i - 1]]!!");
                                    n11 = n9 * n10.intValue() / 10;
                                    break Label_0437;
                                }
                            }
                            n11 = tmp * 10 + tmpNum;
                        }
                        tmp = n11;
                    }
                } while (l <= n6);
            }
            result += tmp + billion;
            o = Result.constructor-impl((Object)result);
        }
        catch (final Throwable t) {
            final Result$Companion companion2 = Result.Companion;
            o = Result.constructor-impl(ResultKt.createFailure(t));
        }
        final Integer n12 = (Integer)o;
        final Integer value5 = -1;
        return (Result.isFailure-impl((Object)n12) ? value5 : n12).intValue();
    }
    
    public final int stringToInt(@Nullable final String str) {
        if (str != null) {
            final String num = new Regex("\\s+").replace((CharSequence)this.fullToHalf(str), "");
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                o = Result.constructor-impl((Object)Integer.parseInt(num));
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            final Throwable exceptionOrNull-impl = Result.exceptionOrNull-impl(o2);
            Object value;
            if (exceptionOrNull-impl == null) {
                value = o2;
            }
            else {
                final Throwable it = exceptionOrNull-impl;
                final int n2 = 0;
                value = StringUtils.INSTANCE.chineseNumToInt(num);
            }
            return ((Number)value).intValue();
        }
        return -1;
    }
    
    public final boolean isContainNumber(@NotNull final String company) {
        Intrinsics.checkNotNullParameter((Object)company, "company");
        final Pattern p = Pattern.compile("[0-9]+");
        final Matcher m = p.matcher(company);
        return m.find();
    }
    
    public final boolean isNumeric(@NotNull final String str) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        final Pattern pattern = Pattern.compile("-?[0-9]+");
        final Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
    
    @NotNull
    public final String wordCountFormat(@Nullable final String wc) {
        if (wc == null) {
            return "";
        }
        String wordsS = "";
        if (this.isNumeric(wc)) {
            final int words = Integer.parseInt(wc);
            if (words > 0) {
                wordsS = new StringBuilder().append(words).append('\u5b57').toString();
                if (words > 10000) {
                    final DecimalFormat df = new DecimalFormat("#.#");
                    wordsS = Intrinsics.stringPlus(df.format(words * 1.0f / 10000.0), (Object)"\u4e07\u5b57");
                }
            }
        }
        else {
            wordsS = wc;
        }
        return wordsS;
    }
    
    @NotNull
    public final String trim(@NotNull final String s) {
        Intrinsics.checkNotNullParameter((Object)s, "s");
        if (s.length() == 0) {
            return "";
        }
        int start = 0;
        final int len = s.length();
        int end;
        for (end = len - 1; start < end && (s.charAt(start) <= ' ' || s.charAt(start) == '\u3000'); ++start) {}
        while (start < end && (s.charAt(end) <= ' ' || s.charAt(end) == '\u3000')) {
            --end;
        }
        if (end < len) {
            ++end;
        }
        String substring;
        if (start > 0 || end < len) {
            Intrinsics.checkNotNullExpressionValue((Object)(substring = s.substring(start, end)), "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        else {
            substring = s;
        }
        return substring;
    }
    
    @NotNull
    public final String repeat(@NotNull final String str, final int n) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        final StringBuilder stringBuilder = new StringBuilder();
        int j = 0;
        if (j < n) {
            do {
                final int i = j;
                ++j;
                stringBuilder.append(str);
            } while (j < n);
        }
        final String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "stringBuilder.toString()");
        return string;
    }
    
    @Nullable
    public final String removeUTFCharacters(@Nullable final String data) {
        if (data == null) {
            return null;
        }
        final Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        final Matcher m = p.matcher(data);
        final StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            final String group = m.group(1);
            Intrinsics.checkNotNull((Object)group);
            final String ch = String.valueOf((char)Integer.parseInt(group, 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf.toString();
    }
    
    @NotNull
    public final String formatHtml(@NotNull final String html) {
        Intrinsics.checkNotNullParameter((Object)html, "html");
        return TextUtils.isEmpty(html) ? "" : new Regex("[\\n\\s]+$").replace((CharSequence)new Regex("^[\\n\\s]+").replace((CharSequence)new Regex("\\s*\\n+\\s*").replace((CharSequence)new Regex("<[script>]*.*?>|&nbsp;").replace((CharSequence)new Regex("(?i)<(br[\\s/]*|/*p.*?|/*div.*?)>").replace((CharSequence)html, "\n"), ""), "\n\u3000\u3000"), "\u3000\u3000"), "");
    }
    
    @NotNull
    public final String byteToHexString(@Nullable final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(bytes.length * 2);
        int i = 0;
        while (i < bytes.length) {
            final byte b = bytes[i];
            ++i;
            final int hex = 0xFF & b;
            if (hex < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(hex));
        }
        final String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "sb.toString()");
        return string;
    }
    
    @NotNull
    public final byte[] hexStringToByte(@NotNull final String hexString) {
        Intrinsics.checkNotNullParameter((Object)hexString, "hexString");
        final String hexStr = StringsKt.replace$default(hexString, " ", "", false, 4, (Object)null);
        final int len = hexStr.length();
        final byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte)((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
    
    static {
        INSTANCE = new StringUtils();
        TAG = "StringUtils";
        ChnMap = StringUtils.INSTANCE.getChnMap();
    }
}
