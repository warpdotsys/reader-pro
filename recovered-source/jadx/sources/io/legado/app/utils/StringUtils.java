package io.legado.app.utils;

import io.legado.app.constant.RSSKeywords;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.browsersupport.NavigationHistory;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: StringUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/StringUtils.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0010\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\n2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011J\u000e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\nJ\u0016\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\nJ\u0016\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nJ\u000e\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nJ\u000e\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nJ\u000e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\nJ\u000e\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\nJ\u000e\u0010#\u001a\u00020!2\u0006\u0010$\u001a\u00020\nJ\u0012\u0010%\u001a\u0004\u0018\u00010\n2\b\u0010&\u001a\u0004\u0018\u00010\nJ\u0016\u0010'\u001a\u00020\n2\u0006\u0010$\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u0006J\u0010\u0010)\u001a\u00020\u00062\b\u0010$\u001a\u0004\u0018\u00010\nJ\u000e\u0010*\u001a\u00020\n2\u0006\u0010$\u001a\u00020\nJ\u000e\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\u0016J\u000e\u0010-\u001a\u00020\n2\u0006\u0010.\u001a\u00020\nJ\u0010\u0010/\u001a\u00020\n2\b\u00100\u001a\u0004\u0018\u00010\nR\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R \u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00048BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u00061"}, d2 = {"Lio/legado/app/utils/StringUtils;", PackageDocumentBase.PREFIX_OPF, "()V", "ChnMap", "Ljava/util/HashMap;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "DAY_OF_YESTERDAY", "HOUR_OF_DAY", "TAG", PackageDocumentBase.PREFIX_OPF, "TIME_UNIT", "chnMap", "getChnMap", "()Ljava/util/HashMap;", "byteToHexString", "bytes", PackageDocumentBase.PREFIX_OPF, "chineseNumToInt", "chNum", "dateConvert", RSSKeywords.RSS_ITEM_TIME, PackageDocumentBase.PREFIX_OPF, "pattern", PackageDocumentBase.DCTags.source, "formatHtml", "html", "fullToHalf", "input", "halfToFull", "hexStringToByte", "hexString", "isContainNumber", PackageDocumentBase.PREFIX_OPF, "company", "isNumeric", "str", "removeUTFCharacters", "data", "repeat", "n", "stringToInt", "toFirstCapital", "toSize", "length", "trim", "s", "wordCountFormat", "wc", "reader-pro"})
public final class StringUtils {
    private static final int HOUR_OF_DAY = 24;
    private static final int DAY_OF_YESTERDAY = 2;
    private static final int TIME_UNIT = 60;

    @NotNull
    public static final StringUtils INSTANCE = new StringUtils();

    @NotNull
    private static final String TAG = "StringUtils";

    @NotNull
    private static final HashMap<Character, Integer> ChnMap = INSTANCE.getChnMap();

    private StringUtils() {
    }

    private final HashMap<Character, Integer> getChnMap() {
        HashMap<Character, Integer> map = new HashMap<>();
        char[] c = "零一二三四五六七八九十".toCharArray();
        Intrinsics.checkNotNullExpressionValue(c, "(this as java.lang.String).toCharArray()");
        int i = 0;
        do {
            int i2 = i;
            i++;
            map.put(Character.valueOf(c[i2]), Integer.valueOf(i2));
        } while (i <= 10);
        char[] c2 = "〇壹贰叁肆伍陆柒捌玖拾".toCharArray();
        Intrinsics.checkNotNullExpressionValue(c2, "(this as java.lang.String).toCharArray()");
        int i3 = 0;
        do {
            int i4 = i3;
            i3++;
            map.put(Character.valueOf(c2[i4]), Integer.valueOf(i4));
        } while (i3 <= 10);
        map.put((char) 20004, 2);
        map.put((char) 30334, 100);
        map.put((char) 20336, 100);
        map.put((char) 21315, Integer.valueOf(NavigationHistory.DEFAULT_MAX_HISTORY_SIZE));
        map.put((char) 20191, Integer.valueOf(NavigationHistory.DEFAULT_MAX_HISTORY_SIZE));
        map.put((char) 19975, 10000);
        map.put((char) 20159, 100000000);
        return map;
    }

    @NotNull
    public final String dateConvert(long time, @NotNull String pattern) {
        Intrinsics.checkNotNullParameter(pattern, "pattern");
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String str = format.format(date);
        Intrinsics.checkNotNullExpressionValue(str, "format.format(date)");
        return str;
    }

    @NotNull
    public final String dateConvert(@NotNull String source, @NotNull String pattern) {
        Intrinsics.checkNotNullParameter(source, PackageDocumentBase.DCTags.source);
        Intrinsics.checkNotNullParameter(pattern, "pattern");
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(source);
            long curTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            long difSec = Math.abs((curTime - date.getTime()) / ((long) NavigationHistory.DEFAULT_MAX_HISTORY_SIZE));
            long difMin = difSec / ((long) TIME_UNIT);
            long difHour = difMin / ((long) TIME_UNIT);
            long difDate = difHour / ((long) TIME_UNIT);
            int oldHour = calendar.get(10);
            if (oldHour == 0) {
                if (difDate == 0) {
                    return "今天";
                }
                if (difDate < 2) {
                    return "昨天";
                }
                SimpleDateFormat convertFormat = new SimpleDateFormat(PackageDocumentBase.dateFormat);
                String str = convertFormat.format(date);
                Intrinsics.checkNotNullExpressionValue(str, "convertFormat.format(date)");
                return str;
            }
            if (difSec < 60) {
                return difSec + "秒前";
            }
            if (difMin < 60) {
                return difMin + "分钟前";
            }
            if (difHour < 24) {
                return difHour + "小时前";
            }
            if (difDate < 2) {
                return "昨天";
            }
            SimpleDateFormat convertFormat2 = new SimpleDateFormat(PackageDocumentBase.dateFormat);
            String str2 = convertFormat2.format(date);
            Intrinsics.checkNotNullExpressionValue(str2, "{\n                    val convertFormat = SimpleDateFormat(\"yyyy-MM-dd\")\n                    convertFormat.format(date)\n                }");
            return str2;
        } catch (ParseException e) {
            e.printStackTrace();
            return PackageDocumentBase.PREFIX_OPF;
        }
    }

    @NotNull
    public final String toSize(long length) {
        if (length <= 0) {
            return "0";
        }
        String[] units = {"b", "kb", "M", "G", "T"};
        int digitGroups = (int) (Math.log10(length) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.##").format(length / Math.pow(1024.0d, digitGroups)) + ' ' + units[digitGroups];
    }

    @NotNull
    public final String toFirstCapital(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        String strSubstring = str.substring(0, 1);
        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        Locale locale = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue(locale, "getDefault()");
        if (strSubstring == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String upperCase = strSubstring.toUpperCase(locale);
        Intrinsics.checkNotNullExpressionValue(upperCase, "(this as java.lang.String).toUpperCase(locale)");
        String strSubstring2 = str.substring(1);
        Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
        return Intrinsics.stringPlus(upperCase, strSubstring2);
    }

    @NotNull
    public final String halfToFull(@NotNull String input) {
        Intrinsics.checkNotNullParameter(input, "input");
        char[] c = input.toCharArray();
        Intrinsics.checkNotNullExpressionValue(c, "(this as java.lang.String).toCharArray()");
        int i = 0;
        int length = c.length - 1;
        if (0 <= length) {
            do {
                int i2 = i;
                i++;
                if (c[i2] == ' ') {
                    c[i2] = 12288;
                } else {
                    char c2 = c[i2];
                    boolean z = '!' <= c2 && c2 <= '~';
                    if (z) {
                        c[i2] = (char) (c[i2] + 65248);
                    }
                }
            } while (i <= length);
        }
        return new String(c);
    }

    @NotNull
    public final String fullToHalf(@NotNull String input) {
        Intrinsics.checkNotNullParameter(input, "input");
        char[] c = input.toCharArray();
        Intrinsics.checkNotNullExpressionValue(c, "(this as java.lang.String).toCharArray()");
        int i = 0;
        int length = c.length - 1;
        if (0 <= length) {
            do {
                int i2 = i;
                i++;
                if (c[i2] == 12288) {
                    c[i2] = ' ';
                } else {
                    char c2 = c[i2];
                    boolean z = 65281 <= c2 && c2 <= 65374;
                    if (z) {
                        c[i2] = (char) (c[i2] - 65248);
                    }
                }
            } while (i <= length);
        }
        return new String(c);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x01ae A[Catch: Throwable -> 0x01d7, TryCatch #0 {Throwable -> 0x01d7, blocks: (B:13:0x00ad, B:15:0x00c7, B:17:0x00fc, B:20:0x011d, B:26:0x0138, B:29:0x014a, B:31:0x0154, B:33:0x017f, B:34:0x01ae, B:38:0x01bd), top: B:47:0x00ad }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0200  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0205  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final int chineseNumToInt(@NotNull String chNum) {
        Object obj;
        int i;
        int length;
        int iIntValue;
        Intrinsics.checkNotNullParameter(chNum, "chNum");
        int result = 0;
        int tmp = 0;
        int billion = 0;
        char[] cn = chNum.toCharArray();
        Intrinsics.checkNotNullExpressionValue(cn, "(this as java.lang.String).toCharArray()");
        if (cn.length > 1) {
            if (new Regex("^[〇零一二三四五六七八九壹贰叁肆伍陆柒捌玖]$").matches(chNum)) {
                int i2 = 0;
                int length2 = cn.length - 1;
                if (0 <= length2) {
                    do {
                        int i3 = i2;
                        i2++;
                        Integer num = ChnMap.get(Character.valueOf(cn[i3]));
                        Intrinsics.checkNotNull(num);
                        Intrinsics.checkNotNullExpressionValue(num, "ChnMap[cn[i]]!!");
                        cn[i3] = (char) (48 + num.intValue());
                    } while (i2 <= length2);
                }
                return Integer.parseInt(new String(cn));
            }
        }
        try {
            Result.Companion companion = Result.Companion;
            i = 0;
            length = cn.length - 1;
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.constructor-impl(ResultKt.createFailure(th));
        }
        if (0 <= length) {
            do {
                int i4 = i;
                i++;
                Integer num2 = ChnMap.get(Character.valueOf(cn[i4]));
                Intrinsics.checkNotNull(num2);
                Intrinsics.checkNotNullExpressionValue(num2, "ChnMap[cn[i]]!!");
                int tmpNum = num2.intValue();
                if (tmpNum == 100000000) {
                    billion = (billion * 100000000) + ((result + tmp) * tmpNum);
                    result = 0;
                    tmp = 0;
                } else if (tmpNum == 10000) {
                    result = (result + tmp) * tmpNum;
                    tmp = 0;
                } else if (tmpNum >= 10) {
                    if (tmp == 0) {
                        tmp = 1;
                    }
                    result += tmpNum * tmp;
                    tmp = 0;
                } else {
                    if (i4 >= 2 && i4 == cn.length - 1) {
                        Integer num3 = ChnMap.get(Character.valueOf(cn[i4 - 1]));
                        Intrinsics.checkNotNull(num3);
                        Intrinsics.checkNotNullExpressionValue(num3, "ChnMap[cn[i - 1]]!!");
                        if (num3.intValue() > 10) {
                            Integer num4 = ChnMap.get(Character.valueOf(cn[i4 - 1]));
                            Intrinsics.checkNotNull(num4);
                            Intrinsics.checkNotNullExpressionValue(num4, "ChnMap[cn[i - 1]]!!");
                            iIntValue = (tmpNum * num4.intValue()) / 10;
                        }
                        tmp = iIntValue;
                    } else {
                        iIntValue = (tmp * 10) + tmpNum;
                        tmp = iIntValue;
                    }
                    Object obj2 = obj;
                    return ((Number) (!Result.isFailure-impl(obj2) ? -1 : obj2)).intValue();
                }
            } while (i <= length);
        }
        obj = Result.constructor-impl(Integer.valueOf(result + tmp + billion));
        Object obj22 = obj;
        return ((Number) (!Result.isFailure-impl(obj22) ? -1 : obj22)).intValue();
    }

    public final int stringToInt(@Nullable String str) {
        Object obj;
        if (str != null) {
            String num = new Regex("\\s+").replace(fullToHalf(str), PackageDocumentBase.PREFIX_OPF);
            try {
                Result.Companion companion = Result.Companion;
                obj = Result.constructor-impl(Integer.valueOf(Integer.parseInt(num)));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            return ((Number) (Result.exceptionOrNull-impl(obj2) == null ? obj2 : Integer.valueOf(INSTANCE.chineseNumToInt(num)))).intValue();
        }
        return -1;
    }

    public final boolean isContainNumber(@NotNull String company) {
        Intrinsics.checkNotNullParameter(company, "company");
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(company);
        return m.find();
    }

    public final boolean isNumeric(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        Pattern pattern = Pattern.compile("-?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    @NotNull
    public final String wordCountFormat(@Nullable String wc) {
        if (wc == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        String wordsS = PackageDocumentBase.PREFIX_OPF;
        if (isNumeric(wc)) {
            int words = Integer.parseInt(wc);
            if (words > 0) {
                wordsS = new StringBuilder().append(words).append((char) 23383).toString();
                if (words > 10000) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    wordsS = Intrinsics.stringPlus(df.format(((double) (words * 1.0f)) / 10000.0d), "万字");
                }
            }
        } else {
            wordsS = wc;
        }
        return wordsS;
    }

    @NotNull
    public final String trim(@NotNull String s) {
        Intrinsics.checkNotNullParameter(s, "s");
        if (s.length() == 0) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        int start = 0;
        int len = s.length();
        int end = len - 1;
        while (start < end && (s.charAt(start) <= ' ' || s.charAt(start) == 12288)) {
            start++;
        }
        while (start < end && (s.charAt(end) <= ' ' || s.charAt(end) == 12288)) {
            end--;
        }
        if (end < len) {
            end++;
        }
        if (start <= 0 && end >= len) {
            return s;
        }
        String strSubstring = s.substring(start, end);
        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    @NotNull
    public final String repeat(@NotNull String str, int n) {
        Intrinsics.checkNotNullParameter(str, "str");
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        if (0 < n) {
            do {
                i++;
                stringBuilder.append(str);
            } while (i < n);
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "stringBuilder.toString()");
        return string;
    }

    @Nullable
    public final String removeUTFCharacters(@Nullable String data) {
        if (data == null) {
            return null;
        }
        Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String strGroup = m.group(1);
            Intrinsics.checkNotNull(strGroup);
            String ch = String.valueOf((char) Integer.parseInt(strGroup, 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf.toString();
    }

    @NotNull
    public final String formatHtml(@NotNull String html) {
        Intrinsics.checkNotNullParameter(html, "html");
        if (TextUtils.isEmpty(html)) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        return new Regex("[\\n\\s]+$").replace(new Regex("^[\\n\\s]+").replace(new Regex("\\s*\\n+\\s*").replace(new Regex("<[script>]*.*?>|&nbsp;").replace(new Regex("(?i)<(br[\\s/]*|/*p.*?|/*div.*?)>").replace(html, "\n"), PackageDocumentBase.PREFIX_OPF), "\n\u3000\u3000"), "\u3000\u3000"), PackageDocumentBase.PREFIX_OPF);
    }

    @NotNull
    public final String byteToHexString(@Nullable byte[] bytes) {
        if (bytes == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        int i = 0;
        int length = bytes.length;
        while (i < length) {
            byte b = bytes[i];
            i++;
            int hex = 255 & b;
            if (hex < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(hex));
        }
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "sb.toString()");
        return string;
    }

    @NotNull
    public final byte[] hexStringToByte(@NotNull String hexString) {
        Intrinsics.checkNotNullParameter(hexString, "hexString");
        String hexStr = StringsKt.replace$default(hexString, " ", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
        int len = hexStr.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}
