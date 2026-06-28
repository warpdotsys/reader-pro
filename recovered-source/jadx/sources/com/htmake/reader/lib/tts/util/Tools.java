package com.htmake.reader.lib.tts.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
import me.ag2s.epublib.browsersupport.NavigationHistory;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/lib/tts/util/Tools.class */
public class Tools {
    public static final String SDF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";
    public static final Pattern NO_VOICE_PATTERN = Pattern.compile("[\\s\\p{C}\\p{P}\\p{Z}\\p{S}]");
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static Logger log = LoggerFactory.getLogger(Tools.class);
    private static OkHttpClient client = new OkHttpClient();

    public static String httpGet(String url) {
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            log.info("response.toString():{}", response.toString());
            log.info("response.isSuccessful():{}", Boolean.valueOf(response.isSuccessful()));
            if (response.isSuccessful()) {
                String body = response.body().string();
                return body;
            }
            throw new RuntimeException(String.format("request：%s fail, message:%s", url, Integer.valueOf(response.code())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNoVoice(CharSequence charSequence) {
        return NO_VOICE_PATTERN.matcher(charSequence).replaceAll(PackageDocumentBase.PREFIX_OPF).isEmpty();
    }

    public static void sleep(int second) {
        try {
            Thread.sleep(second * NavigationHistory.DEFAULT_MAX_HISTORY_SIZE);
        } catch (InterruptedException e) {
        }
    }

    public static String date() {
        return new SimpleDateFormat(SDF).format(new Date());
    }

    public static String localDateTime() {
        return LocalDateTime.now().format(DTF);
    }

    public static String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        if ("TW".equals(countryCode) && Locale.getDefault().getCountry().equals("CN")) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        int firstLetter = (Character.codePointAt(countryCode, 0) - 65) + 127462;
        int secondLetter = (Character.codePointAt(countryCode, 1) - 65) + 127462;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    public static String getRandomId() {
        return UUID.randomUUID().toString().replace("-", PackageDocumentBase.PREFIX_OPF);
    }
}
