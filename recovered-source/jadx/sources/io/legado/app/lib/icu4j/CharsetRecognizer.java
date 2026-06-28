package io.legado.app.lib.icu4j;

/* JADX INFO: loaded from: app-classes.jar:io/legado/app/lib/icu4j/CharsetRecognizer.class */
abstract class CharsetRecognizer {
    abstract String getName();

    abstract CharsetMatch match(CharsetDetector det);

    CharsetRecognizer() {
    }

    public String getLanguage() {
        return null;
    }
}
