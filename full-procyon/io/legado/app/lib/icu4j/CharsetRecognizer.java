// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.lib.icu4j;

abstract class CharsetRecognizer
{
    abstract String getName();
    
    public String getLanguage() {
        return null;
    }
    
    abstract CharsetMatch match(final CharsetDetector det);
}
