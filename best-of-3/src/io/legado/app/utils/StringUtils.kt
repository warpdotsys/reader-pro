package io.legado.app.utils

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Result.Companion
import kotlin.jvm.internal.Intrinsics

public object StringUtils {
   private final val ChnMap: HashMap<Char, Int> = INSTANCE.getChnMap()
   private const val DAY_OF_YESTERDAY: Int = 2
   private const val HOUR_OF_DAY: Int = 24
   private final val TAG: String = "StringUtils"
   private const val TIME_UNIT: Int = 60

   private final val chnMap: HashMap<Char, Int>
      private final get() {
         val map: HashMap = new HashMap();
         var var10000: CharArray = "零一二三四五六七八九十".toCharArray();
         var c: CharArray = var10000;
         var var4: Int = 0;

         do {
            val var20: Int = var4++;
            map.put(c[var20], var20);
         } while (var4 <= 10);

         var10000 = "〇壹贰叁肆伍陆柒捌玖拾".toCharArray();
         c = var10000;
         var4 = 0;

         do {
            val var22: Int = var4++;
            map.put(c[var22], var22);
         } while (var4 <= 10);

         map.put('两', 2);
         map.put('百', 100);
         map.put('佰', 100);
         map.put('千', 1000);
         map.put('仟', 1000);
         map.put('万', 10000);
         map.put('亿', 100000000);
         return map;
      }

   public fun dateConvert(time: Long, pattern: String): String {
      val var6: java.lang.String = new SimpleDateFormat(pattern).format(new Date(time));
      return var6;
   }

   public fun dateConvert(source: String, pattern: String): String {
      val format: SimpleDateFormat = new SimpleDateFormat(pattern);
      val calendar: Calendar = Calendar.getInstance();

      try {
         val e: Date = format.parse(source);
         val curTime: Long = calendar.getTimeInMillis();
         calendar.setTime(e);
         val difSec: Long = Math.abs((curTime - e.getTime()) / (long)1000);
         val difMin: Long = difSec / 60;
         val difHour: Long = difSec / 60 / 60;
         val difDate: Long = difSec / 60 / 60 / 60;
         if (calendar.get(10) == 0) {
            if (difDate == 0L) {
               return "今天";
            } else if (difDate < 2L) {
               return "昨天";
            } else {
               val var21: java.lang.String = new SimpleDateFormat("yyyy-MM-dd").format(e);
               return var21;
            }
         } else {
            val var10000: java.lang.String;
            if (difSec < 60L) {
               var10000 = "$difSec秒前";
            } else if (difMin < 60L) {
               var10000 = "$difMin分钟前";
            } else if (difHour < 24L) {
               var10000 = "$difHour小时前";
            } else if (difDate < 2L) {
               var10000 = "昨天";
            } else {
               val convertFormat: java.lang.String = new SimpleDateFormat("yyyy-MM-dd").format(e);
               var10000 = convertFormat;
            }

            return var10000;
         }
      } catch (var19: ParseException) {
         var19.printStackTrace();
         return "";
      }
   }

   public fun toSize(length: Long): String {
      if (length <= 0L) {
         return "0";
      } else {
         val digitGroups: Array<java.lang.String> = new java.lang.String[]{"b", "kb", "M", "G", "T"};
         val var10: Int = (int)(Math.log10((double)length) / Math.log10(1024.0));
         return "${new DecimalFormat("#,##0.##").format((double)length / Math.pow(1024.0, (double)var10))} ${digitGroups[var10]}";
      }
   }

   public fun toFirstCapital(str: String): String {
      var var10000: java.lang.String = str.substring(0, 1);
      val var6: Locale = Locale.getDefault();
      if (var10000 == null) {
         throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
      } else {
         var10000 = var10000.toUpperCase(var6);
         val var10001: java.lang.String = str.substring(1);
         return Intrinsics.stringPlus(var10000, var10001);
      }
   }

   public fun halfToFull(input: String): String {
      val var10000: CharArray = input.toCharArray();
      val c: CharArray = var10000;
      var var3: Int = 0;
      val var10: Int = var10000.length + -1;
      if (0 <= var10000.length + -1) {
         do {
            val i: Int = var3++;
            if (c[i] == ' ') {
               c[i] = 12288;
            } else if ('!' <= c[i] && c[i] <= '~') {
               c[i] += 'ﻠ';
            }
         } while (var3 <= var10);
      }

      return new java.lang.String(c);
   }

   public fun fullToHalf(input: String): String {
      val var10000: CharArray = input.toCharArray();
      val c: CharArray = var10000;
      var var3: Int = 0;
      val var10: Int = var10000.length + -1;
      if (0 <= var10000.length + -1) {
         do {
            val i: Int = var3++;
            if (c[i] == 12288) {
               c[i] = ' ';
            } else if ('！' <= c[i] && c[i] <= '～') {
               c[i] -= 'ﻠ';
            }
         } while (var3 <= var10);
      }

      return new java.lang.String(c);
   }

   public fun chineseNumToInt(chNum: String): Int {
      var result: Int = 0;
      var tmp: Int = 0;
      var billion: Int = 0;
      var var10000: CharArray = chNum.toCharArray();
      val cn: CharArray = var10000;
      if (var10000.length > 1 && new Regex("^[〇零一二三四五六七八九壹贰叁肆伍陆柒捌玖]$").matches(chNum)) {
         var var21: Int = 0;
         val var28: Int = var10000.length + -1;
         if (0 <= var10000.length + -1) {
            do {
               val var33: Int = var21++;
               val var10003: Any = ChnMap.get(cn[var33]);
               cn[var33] = (char)(48 + (var10003 as java.lang.Number).intValue());
            } while (var21 <= var28);
         }

         return Integer.parseInt(new java.lang.String(cn));
      } else {
         var var25: Any;
         try {
            var25 = Result.Companion;
            var var34: Int = 0;
            val var37: Int = cn.length + -1;
            if (0 <= cn.length + -1) {
               do {
                  val i: Int = var34++;
                  var10000 = (char[])ChnMap.get(cn[i]);
                  val tmpNum: Int = (var10000 as java.lang.Number).intValue();
                  if (tmpNum == 100000000) {
                     billion = billion * 100000000 + (result + tmp) * tmpNum;
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
                     label55: {
                        if (i >= 2 && i == cn.length - 1) {
                           var10000 = (char[])ChnMap.get(cn[i - 1]);
                           if ((var10000 as java.lang.Number).intValue() > 10) {
                              val var10001: Any = ChnMap.get(cn[i - 1]);
                              var42 = tmpNum * (var10001 as java.lang.Number).intValue() / 10;
                              break label55;
                           }
                        }

                        var42 = tmp * 10 + tmpNum;
                     }

                     tmp = var42;
                  }
               } while (var34 <= var37);
            }

            var25 = Result.constructor-impl(result + tmp + billion);
         } catch (var14: java.lang.Throwable) {
            val var9: Companion = Result.Companion;
            var25 = Result.constructor-impl(ResultKt.createFailure(var14));
         }

         return ((if (Result.isFailure-impl(var25)) -1 else var25) as java.lang.Number).intValue();
      }
   }

   public fun stringToInt(str: String?): Int {
      if (str != null) {
         val num: java.lang.String = new Regex("\\s+").replace(this.fullToHalf(str), "");

         var var12: Any;
         try {
            var12 = Result.Companion;
            var12 = Result.constructor-impl(Integer.parseInt(num));
         } catch (var8: java.lang.Throwable) {
            val var20: Companion = Result.Companion;
            var12 = Result.constructor-impl(ResultKt.createFailure(var8));
         }

         return ((if (Result.exceptionOrNull-impl(var12) == null) var12 else INSTANCE.chineseNumToInt(num)) as java.lang.Number).intValue();
      } else {
         return -1;
      }
   }

   public fun isContainNumber(company: String): Boolean {
      return Pattern.compile("[0-9]+").matcher(company).find();
   }

   public fun isNumeric(str: String): Boolean {
      return Pattern.compile("-?[0-9]+").matcher(str).matches();
   }

   public fun wordCountFormat(wc: String?): String {
      if (wc == null) {
         return "";
      } else {
         var wordsS: java.lang.String = "";
         if (this.isNumeric(wc)) {
            val words: Int = Integer.parseInt(wc);
            if (words > 0) {
               wordsS = "$words字";
               if (words > 10000) {
                  wordsS = Intrinsics.stringPlus(new DecimalFormat("#.#").format((double)((float)words * 1.0F) / 10000.0), "万字");
               }
            }
         } else {
            wordsS = wc;
         }

         return wordsS;
      }
   }

   public fun trim(s: String): String {
      if (s.length() == 0) {
         return "";
      } else {
         var var7: Int = 0;
         val var8: Int = s.length();

         var end: Int;
         for (end = len - 1; start < end; start++) {
            if (s.charAt(var7) > ' ' && s.charAt(var7) != 12288) {
               break;
            }
         }

         while (start < end) {
            if (s.charAt(end) > ' ' && s.charAt(end) != 12288) {
               break;
            }

            end--;
         }

         if (end < var8) {
            end++;
         }

         val var10000: java.lang.String;
         if (var7 <= 0 && end >= var8) {
            var10000 = s;
         } else {
            var10000 = s.substring(var7, end);
         }

         return var10000;
      }
   }

   public fun repeat(str: String, n: Int): String {
      val stringBuilder: StringBuilder = new StringBuilder();
      var var4: Int = 0;
      if (0 < n) {
         do {
            var4++;
            stringBuilder.append(str);
         } while (var4 < n);
      }

      val var6: java.lang.String = stringBuilder.toString();
      return var6;
   }

   public fun removeUTFCharacters(data: String?): String? {
      if (data == null) {
         return null;
      } else {
         val m: Matcher = Pattern.compile("\\\\u(\\p{XDigit}{4})").matcher(data);
         val buf: StringBuffer = new StringBuffer(data.length());

         while (m.find()) {
            val var10000: java.lang.String = m.group(1);
            m.appendReplacement(buf, Matcher.quoteReplacement(java.lang.String.valueOf((char)Integer.parseInt(var10000, 16))));
         }

         m.appendTail(buf);
         return buf.toString();
      }
   }

   public fun formatHtml(html: String): String {
      return if (TextUtils.isEmpty(html))
         ""
         else
         new Regex("[\\n\\s]+$")
            .replace(
               new Regex("^[\\n\\s]+")
                  .replace(
                     new Regex("\\s*\\n+\\s*")
                        .replace(new Regex("<[script>]*.*?>|&nbsp;").replace(new Regex("(?i)<(br[\\s/]*|/*p.*?|/*div.*?)>").replace(html, "\n"), ""), "\n　　"),
                     "　　"
                  ),
               ""
            );
   }

   public fun byteToHexString(bytes: ByteArray?): String {
      if (bytes == null) {
         return "";
      } else {
         val sb: StringBuilder = new StringBuilder(bytes.length * 2);
         val var3: ByteArray = bytes;
         var var4: Int = 0;
         val var5: Int = bytes.length;

         while (var4 < var5) {
            val b: Byte = var3[var4];
            var4++;
            val hex: Int = 255 and b;
            if ((255 and b) < 16) {
               sb.append('0');
            }

            sb.append(Integer.toHexString(hex));
         }

         val var8: java.lang.String = sb.toString();
         return var8;
      }
   }

   public fun hexStringToByte(hexString: String): ByteArray {
      val len: Int = StringsKt.replace$default(hexString, " ", "", false, 4, null).length();
      val bytes: ByteArray = new byte[len / 2];

      for (int i = 0; i < len; i += 2) {
         bytes[i / 2] = (byte)((Character.digit(hexString.charAt(i), 16) shl 4) + Character.digit(hexString.charAt(i + 1), 16));
      }

      return bytes;
   }
}
