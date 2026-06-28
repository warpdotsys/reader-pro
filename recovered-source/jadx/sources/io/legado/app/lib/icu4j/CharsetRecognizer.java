package io.legado.app.lib.icu4j;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/lib/icu4j/CharsetRecognizer.class */
abstract class CharsetRecognizer {
    abstract String getName();

    abstract CharsetMatch match(CharsetDetector det);

    CharsetRecognizer() {
    }

    public String getLanguage() {
        return null;
    }
}
