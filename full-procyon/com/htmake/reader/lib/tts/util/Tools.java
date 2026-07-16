// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.lib.tts.util;

import org.slf4j.LoggerFactory;
import java.util.UUID;
import java.util.Locale;
import java.time.LocalDateTime;
import java.util.Date;
import java.text.SimpleDateFormat;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Tools
{
    public static final Pattern NO_VOICE_PATTERN;
    public static final String SDF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";
    public static final DateTimeFormatter DTF;
    public static Logger log;
    private static OkHttpClient client;
    
    public static String httpGet(final String url) {
        try {
            final Request request = new Request.Builder().url(url).build();
            final Response response = Tools.client.newCall(request).execute();
            Tools.log.info("response.toString():{}", (Object)response.toString());
            Tools.log.info("response.isSuccessful():{}", (Object)response.isSuccessful());
            if (response.isSuccessful()) {
                final String body = response.body().string();
                return body;
            }
            throw new RuntimeException(String.format("request\uff1a%s fail, message:%s", url, response.code()));
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean isNoVoice(final CharSequence charSequence) {
        return Tools.NO_VOICE_PATTERN.matcher(charSequence).replaceAll("").isEmpty();
    }
    
    public static void sleep(final int second) {
        try {
            Thread.sleep(second * 1000);
        }
        catch (final InterruptedException ex) {}
    }
    
    public static String date() {
        return new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z").format(new Date());
    }
    
    public static String localDateTime() {
        return LocalDateTime.now().format(Tools.DTF);
    }
    
    public static String localeToEmoji(final Locale locale) {
        final String countryCode = locale.getCountry();
        if ("TW".equals(countryCode) && Locale.getDefault().getCountry().equals("CN")) {
            return "";
        }
        final int firstLetter = Character.codePointAt(countryCode, 0) - 65 + 127462;
        final int secondLetter = Character.codePointAt(countryCode, 1) - 65 + 127462;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
    
    public static String getRandomId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    static {
        NO_VOICE_PATTERN = Pattern.compile("[\\s\\p{C}\\p{P}\\p{Z}\\p{S}]");
        DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        Tools.log = LoggerFactory.getLogger((Class)Tools.class);
        Tools.client = new OkHttpClient();
    }
}
