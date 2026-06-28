package com.htmake.reader.lib.tts.exceptions;

/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/lib/tts/exceptions/TtsException.class */
public class TtsException extends RuntimeException {
    private TtsException(String message) {
        super(message);
    }

    public static TtsException of(String message) {
        return new TtsException(message);
    }
}
