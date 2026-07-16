package com.htmake.reader.help

import com.htmake.reader.config.AppConfig
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Optional SMTP sender. When host is blank, [isConfigured] is false.
 * Uses JavaMail API if present on classpath; otherwise reports unavailable.
 */
object SmtpMailer {

    fun isConfigured(cfg: AppConfig): Boolean =
        cfg.smtpHost.isNotBlank() && cfg.smtpFrom.isNotBlank()

    /**
     * Send plain-text email. Returns map with ok/error.
     * Does not throw.
     */
    fun sendText(cfg: AppConfig, to: String, subject: String, body: String): Map<String, Any?> {
        if (!isConfigured(cfg)) {
            return mapOf("ok" to false, "error" to "smtp not configured")
        }
        return try {
            // Prefer JavaMail if available
            sendWithJavaMail(cfg, to, subject, body)
        } catch (e: NoClassDefFoundError) {
            mapOf("ok" to false, "error" to "javax.mail not on classpath: ${e.message}")
        } catch (e: ClassNotFoundException) {
            mapOf("ok" to false, "error" to "javax.mail not on classpath: ${e.message}")
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "send failed"))
        }
    }

    private fun sendWithJavaMail(cfg: AppConfig, to: String, subject: String, body: String): Map<String, Any?> {
        val props = Properties().apply {
            put("mail.smtp.host", cfg.smtpHost)
            put("mail.smtp.port", cfg.smtpPort.toString())
            put("mail.smtp.auth", (cfg.smtpUser.isNotBlank()).toString())
            if (cfg.smtpStartTls) put("mail.smtp.starttls.enable", "true")
            if (cfg.smtpSsl) put("mail.smtp.ssl.enable", "true")
            put("mail.smtp.connectiontimeout", "8000")
            put("mail.smtp.timeout", "15000")
        }
        val session = if (cfg.smtpUser.isNotBlank()) {
            Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() =
                    PasswordAuthentication(cfg.smtpUser, cfg.smtpPassword)
            })
        } else Session.getInstance(props)

        val msg = MimeMessage(session).apply {
            setFrom(InternetAddress(cfg.smtpFrom))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            setSubject(subject, "UTF-8")
            setText(body, "UTF-8")
        }
        Transport.send(msg)
        return mapOf("ok" to true, "to" to to, "subject" to subject)
    }

    /** Build verification email body (pure, testable). */
    fun buildCodeMailBody(code: String, ttlMinutes: Int = 10): String =
        """
        |您的 Reader 验证码是：$code
        |
        |有效期 $ttlMinutes 分钟，请勿泄露给他人。
        |如非本人操作请忽略本邮件。
        """.trimMargin()
}
