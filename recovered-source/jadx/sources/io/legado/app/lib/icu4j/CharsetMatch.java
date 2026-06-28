package io.legado.app.lib.icu4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/* JADX INFO: loaded from: app-classes.jar:io/legado/app/lib/icu4j/CharsetMatch.class */
public class CharsetMatch implements Comparable<CharsetMatch> {
    private final int fConfidence;
    private byte[] fRawInput;
    private int fRawLength;
    private final InputStream fInputStream;
    private final String fCharsetName;
    private final String fLang;

    public Reader getReader() {
        InputStream inputStream = this.fInputStream;
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(this.fRawInput, 0, this.fRawLength);
        }
        try {
            inputStream.reset();
            return new InputStreamReader(inputStream, getName());
        } catch (IOException e) {
            return null;
        }
    }

    public String getString() throws IOException {
        return getString(-1);
    }

    public String getString(int maxLength) throws IOException {
        if (this.fInputStream != null) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            Reader reader = getReader();
            int i = maxLength < 0 ? Integer.MAX_VALUE : maxLength;
            while (true) {
                int max = i;
                int bytesRead = reader.read(buffer, 0, Math.min(max, 1024));
                if (bytesRead >= 0) {
                    sb.append(buffer, 0, bytesRead);
                    i = max - bytesRead;
                } else {
                    reader.close();
                    return sb.toString();
                }
            }
        } else {
            String name = getName();
            int startSuffix = !name.contains("_rtl") ? name.indexOf("_ltr") : name.indexOf("_rtl");
            if (startSuffix > 0) {
                name = name.substring(0, startSuffix);
            }
            String result = new String(this.fRawInput, name);
            return result;
        }
    }

    public int getConfidence() {
        return this.fConfidence;
    }

    public String getName() {
        return this.fCharsetName;
    }

    public String getLanguage() {
        return this.fLang;
    }

    @Override // java.lang.Comparable
    public int compareTo(CharsetMatch other) {
        int compareResult = 0;
        if (this.fConfidence > other.fConfidence) {
            compareResult = 1;
        } else if (this.fConfidence < other.fConfidence) {
            compareResult = -1;
        }
        return compareResult;
    }

    CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf) {
        this.fRawInput = null;
        this.fConfidence = conf;
        if (det.fInputStream == null) {
            this.fRawInput = det.fRawInput;
            this.fRawLength = det.fRawLength;
        }
        this.fInputStream = det.fInputStream;
        this.fCharsetName = rec.getName();
        this.fLang = rec.getLanguage();
    }

    CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf, String csName, String lang) {
        this.fRawInput = null;
        this.fConfidence = conf;
        if (det.fInputStream == null) {
            this.fRawInput = det.fRawInput;
            this.fRawLength = det.fRawLength;
        }
        this.fInputStream = det.fInputStream;
        this.fCharsetName = csName;
        this.fLang = lang;
    }
}
