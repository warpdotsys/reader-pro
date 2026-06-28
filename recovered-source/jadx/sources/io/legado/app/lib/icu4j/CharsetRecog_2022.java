package io.legado.app.lib.icu4j;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/lib/icu4j/CharsetRecog_2022.class */
abstract class CharsetRecog_2022 extends CharsetRecognizer {
    CharsetRecog_2022() {
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0086  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    int match(byte[] text, int textLen, byte[][] escapeSequences) {
        int hits = 0;
        int misses = 0;
        int shifts = 0;
        int i = 0;
        while (i < textLen) {
            if (text[i] == 27) {
                for (byte[] seq : escapeSequences) {
                    if (textLen - i >= seq.length) {
                        for (int j = 1; j < seq.length; j++) {
                            if (seq[j] != text[i + j]) {
                                break;
                            }
                        }
                        hits++;
                        i += seq.length - 1;
                        break;
                    }
                }
                misses++;
                if (text[i] != 14 || text[i] == 15) {
                    shifts++;
                }
            } else if (text[i] != 14) {
                shifts++;
            }
            i++;
        }
        if (hits == 0) {
            return 0;
        }
        int quality = ((100 * hits) - (100 * misses)) / (hits + misses);
        if (hits + shifts < 5) {
            quality -= (5 - (hits + shifts)) * 10;
        }
        if (quality < 0) {
            quality = 0;
        }
        return quality;
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/lib/icu4j/CharsetRecog_2022$CharsetRecog_2022JP.class */
    static class CharsetRecog_2022JP extends CharsetRecog_2022 {
        private final byte[][] escapeSequences = {new byte[]{27, 36, 40, 67}, new byte[]{27, 36, 40, 68}, new byte[]{27, 36, 64}, new byte[]{27, 36, 65}, new byte[]{27, 36, 66}, new byte[]{27, 38, 64}, new byte[]{27, 40, 66}, new byte[]{27, 40, 72}, new byte[]{27, 40, 73}, new byte[]{27, 40, 74}, new byte[]{27, 46, 65}, new byte[]{27, 46, 70}};

        /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
        CharsetRecog_2022JP() {
        }

        @Override // io.legado.app.lib.icu4j.CharsetRecognizer
        String getName() {
            return "ISO-2022-JP";
        }

        @Override // io.legado.app.lib.icu4j.CharsetRecognizer
        CharsetMatch match(CharsetDetector det) {
            int confidence = match(det.fInputBytes, det.fInputLen, this.escapeSequences);
            if (confidence == 0) {
                return null;
            }
            return new CharsetMatch(det, this, confidence);
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/lib/icu4j/CharsetRecog_2022$CharsetRecog_2022KR.class */
    static class CharsetRecog_2022KR extends CharsetRecog_2022 {
        private final byte[][] escapeSequences = {new byte[]{27, 36, 41, 67}};

        /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
        CharsetRecog_2022KR() {
        }

        @Override // io.legado.app.lib.icu4j.CharsetRecognizer
        String getName() {
            return "ISO-2022-KR";
        }

        @Override // io.legado.app.lib.icu4j.CharsetRecognizer
        CharsetMatch match(CharsetDetector det) {
            int confidence = match(det.fInputBytes, det.fInputLen, this.escapeSequences);
            if (confidence == 0) {
                return null;
            }
            return new CharsetMatch(det, this, confidence);
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/lib/icu4j/CharsetRecog_2022$CharsetRecog_2022CN.class */
    static class CharsetRecog_2022CN extends CharsetRecog_2022 {
        private final byte[][] escapeSequences = {new byte[]{27, 36, 41, 65}, new byte[]{27, 36, 41, 71}, new byte[]{27, 36, 42, 72}, new byte[]{27, 36, 41, 69}, new byte[]{27, 36, 43, 73}, new byte[]{27, 36, 43, 74}, new byte[]{27, 36, 43, 75}, new byte[]{27, 36, 43, 76}, new byte[]{27, 36, 43, 77}, new byte[]{27, 78}, new byte[]{27, 79}};

        /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
        CharsetRecog_2022CN() {
        }

        @Override // io.legado.app.lib.icu4j.CharsetRecognizer
        String getName() {
            return "ISO-2022-CN";
        }

        @Override // io.legado.app.lib.icu4j.CharsetRecognizer
        CharsetMatch match(CharsetDetector det) {
            int confidence = match(det.fInputBytes, det.fInputLen, this.escapeSequences);
            if (confidence == 0) {
                return null;
            }
            return new CharsetMatch(det, this, confidence);
        }
    }
}
