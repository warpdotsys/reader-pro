package io.legado.app.utils

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    @Suppress("unused")
    private const val TAG = "StringUtils"
    private const val HOUR_OF_DAY = 24
    private const val DAY_OF_YESTERDAY = 2
    private const val TIME_UNIT = 60

    private val ChnMap = getChnMap()

    private fun getChnMap(): HashMap<Char, Int> {
        val map = HashMap<Char, Int>()
        var cnStr = "\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341"
        var c = cnStr.toCharArray()
        for (i in 0..10) {
            map[c[i]] = i
        }
        cnStr = "\u3007\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe"
        c = cnStr.toCharArray()
        for (i in 0..10) {
            map[c[i]] = i
        }
        map['\u4e24'] = 2
        map['\u767e'] = 100
        map['\u4f70'] = 100
        map['\u5343'] = 1000
        map['\u4edf'] = 1000
        map['\u4e07'] = 10000
        map['\u4ebf'] = 100000000
        return map
    }

    /**
     * 将时间戳转换为指定格式字符串
     */
    fun dateConvert(time: Long, pattern: String): String {
        val date = Date(time)
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    /**
     * 将日期字符串转换为相对时间描述
     */
    fun dateConvert(source: String, pattern: String): String {
        val format = SimpleDateFormat(pattern)
        val calendar = Calendar.getInstance()
        return try {
            val date = format.parse(source)
            val curTime = calendar.timeInMillis
            calendar.time = date
            val difSec = Math.abs((curTime - date.time) / 1000)
            val difMin = difSec / 60
            val difHour = difMin / 60
            val difDate = difHour / 60
            val oldHour = calendar.get(Calendar.HOUR)
            if (oldHour == 0) {
                when {
                    difDate == 0L -> "\u4eca\u5929"
                    difDate < DAY_OF_YESTERDAY -> "\u6628\u5929"
                    else -> {
                        val convertFormat = SimpleDateFormat("yyyy-MM-dd")
                        convertFormat.format(date)
                    }
                }
            } else {
                when {
                    difSec < TIME_UNIT -> "${difSec}\u79d2\u524d"
                    difMin < TIME_UNIT -> "${difMin}\u5206\u949f\u524d"
                    difHour < HOUR_OF_DAY -> "${difHour}\u5c0f\u65f6\u524d"
                    difDate < DAY_OF_YESTERDAY -> "\u6628\u5929"
                    else -> {
                        val convertFormat = SimpleDateFormat("yyyy-MM-dd")
                        convertFormat.format(date)
                    }
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 将字节长度转换为可读大小
     */
    fun toSize(length: Long): String {
        if (length <= 0) {
            return "0"
        }
        val units = arrayOf("b", "kb", "M", "G", "T")
        val digitGroups = (Math.log10(length.toDouble()) / Math.log10(1024.0)).toInt()
        return StringBuilder()
            .append(DecimalFormat("#,##0.##").format(length / Math.pow(1024.0, digitGroups.toDouble())))
            .append(' ')
            .append(units[digitGroups])
            .toString()
    }

    /**
     * 首字母大写
     */
    fun toFirstCapital(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }

    /**
     * 半角转全角
     */
    fun halfToFull(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i] == ' ') {
                c[i] = '\u3000'
            } else if (c[i] in '\uff01'..'\uff5e') {
                c[i] = (c[i].code + 0xFEE0).toChar()
            }
        }
        return String(c)
    }

    /**
     * 全角转半角
     */
    fun fullToHalf(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i] == '\u3000') {
                c[i] = ' '
            } else if (c[i] in '\uff01'..'\uff5e') {
                c[i] = (c[i].code - 0xFEE0).toChar()
            }
        }
        return String(c)
    }

    /**
     * 中文数字转阿拉伯数字, 失败返回-1
     */
    fun chineseNumToInt(chNum: String): Int {
        var result = 0
        var tmp = 0
        var billion = 0
        val cn = chNum.toCharArray()
        if (cn.size > 1 && Regex("^[\u3007\u96f6\u4e00\u4e8c\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396]$").matches(chNum)) {
            for (i in cn.indices) {
                cn[i] = (48 + ChnMap[cn[i]]!!).toChar()
            }
            return Integer.parseInt(String(cn))
        }
        return runCatching {
            for (i in cn.indices) {
                val tmpNum = ChnMap[cn[i]]!!
                when {
                    tmpNum == 100000000 -> {
                        result += tmp
                        result *= tmpNum
                        billion = billion * 100000000 + result
                        result = 0
                        tmp = 0
                    }
                    tmpNum == 10000 -> {
                        result += tmp
                        result *= tmpNum
                        tmp = 0
                    }
                    tmpNum >= 10 -> {
                        if (tmp == 0) {
                            tmp = 1
                        }
                        result += tmpNum * tmp
                        tmp = 0
                    }
                    else -> {
                        tmp = if (i >= 2 && i == cn.size - 1 && ChnMap[cn[i - 1]]!! > 10) {
                            tmpNum * ChnMap[cn[i - 1]]!! / 10
                        } else {
                            tmp * 10 + tmpNum
                        }
                    }
                }
            }
            result += tmp + billion
            result
        }.getOrDefault(-1)
    }

    fun stringToInt(str: String?): Int {
        if (str != null) {
            val num = Regex("\\s+").replace(fullToHalf(str), "")
            return kotlin.runCatching {
                Integer.parseInt(num)
            }.getOrElse {
                chineseNumToInt(num)
            }
        }
        return -1
    }

    fun isContainNumber(company: String): Boolean {
        val p = Pattern.compile("[0-9]+")
        val m = p.matcher(company)
        return m.find()
    }

    fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("-?[0-9]+")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    /**
     * 格式化字数, 大于1万转换为万字单位
     */
    fun wordCountFormat(wc: String?): String {
        if (wc == null) {
            return ""
        }
        var wordsS = ""
        if (isNumeric(wc)) {
            val words = Integer.parseInt(wc)
            if (words > 0) {
                wordsS = "$words\u5b57"
                if (words > 10000) {
                    val df = DecimalFormat("#.#")
                    wordsS = df.format(words * 1.0f / 10000.0) + "\u4e07\u5b57"
                }
            }
        } else {
            wordsS = wc
        }
        return wordsS
    }

    /**
     * 去除字符串首尾的空格与全角空格
     */
    fun trim(s: String): String {
        if (s.isEmpty()) {
            return ""
        }
        var start = 0
        val len = s.length
        var end = len - 1
        while (start < end) {
            if (s[start] > ' ' && s[start] != '\u3000') {
                break
            }
            start++
        }
        while (start < end) {
            if (s[end] > ' ' && s[end] != '\u3000') {
                break
            }
            end--
        }
        if (end < len) {
            ++end
        }
        return if (start <= 0 && end >= len) {
            s
        } else {
            s.substring(start, end)
        }
    }

    fun repeat(str: String, n: Int): String {
        val stringBuilder = StringBuilder()
        repeat(n) {
            stringBuilder.append(str)
        }
        return stringBuilder.toString()
    }

    fun removeUTFCharacters(data: String?): String? {
        if (data == null) {
            return null
        }
        val p = Pattern.compile("\\\\u(\\p{XDigit}{4})")
        val m = p.matcher(data)
        val buf = StringBuffer(data.length)
        while (m.find()) {
            val ch = Integer.parseInt(m.group(1), 16).toChar().toString()
            m.appendReplacement(buf, Matcher.quoteReplacement(ch))
        }
        m.appendTail(buf)
        return buf.toString()
    }

    fun formatHtml(html: String): String {
        if (TextUtils.isEmpty(html)) {
            return ""
        }
        return html
            .replace("(?i)<(br[\\s/]*|/*p.*?|/*div.*?)>".toRegex(), "\n")
            .replace("<[script>]*.*?>|&nbsp;".toRegex(), "")
            .replace("\\s*\n+\\s*".toRegex(), "\n\u3000\u3000")
            .replace("^[\\n\\s]+".toRegex(), "\u3000\u3000")
            .replace("[\\n\\s]+$".toRegex(), "")
    }

    /**
     * 字节数组转十六进制字符串
     */
    fun byteToHexString(bytes: ByteArray?): String {
        if (bytes == null) {
            return ""
        }
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val hex = 255 and b.toInt()
            if (hex < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(hex))
        }
        return sb.toString()
    }

    /**
     * 十六进制字符串转字节数组
     */
    fun hexStringToByte(hexString: String): ByteArray {
        val hexStr = hexString.replace(" ", "")
        val len = hexStr.length
        val bytes = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            bytes[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
        }
        return bytes
    }
}
