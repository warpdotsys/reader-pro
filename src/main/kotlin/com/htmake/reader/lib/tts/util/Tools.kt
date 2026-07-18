package com.htmake.reader.lib.tts.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern

object Tools {
    @JvmField val NO_VOICE_PATTERN: Pattern = Pattern.compile("[\\s\\p{C}\\p{P}\\p{Z}\\p{S}]")
    const val SDF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z"
    @JvmField val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
    @JvmStatic fun isNoVoice(value: CharSequence): Boolean = NO_VOICE_PATTERN.matcher(value).replaceAll("").isEmpty()
    @JvmStatic fun sleep(second: Int) { try { Thread.sleep(second * 1000L) } catch (_: InterruptedException) {} }
    @JvmStatic fun date(): String = SimpleDateFormat(SDF).format(Date())
    @JvmStatic fun localDateTime(): String = LocalDateTime.now().format(DTF)
    @JvmStatic fun localeToEmoji(locale: Locale): String { val country = locale.country; if (country == "TW" && Locale.getDefault().country == "CN") return ""; return String(Character.toChars(Character.codePointAt(country, 0) - 65 + 127462)) + String(Character.toChars(Character.codePointAt(country, 1) - 65 + 127462)) }
    @JvmStatic fun getRandomId(): String = UUID.randomUUID().toString().replace("-", "")
}
