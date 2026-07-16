// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.lib.tts.exceptions;

public class TtsException extends RuntimeException
{
    private TtsException(final String message) {
        super(message);
    }
    
    public static TtsException of(final String message) {
        return new TtsException(message);
    }
}
