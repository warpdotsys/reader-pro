// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;

public class Identifier implements Serializable
{
    private static final long serialVersionUID = 955949951416391810L;
    private boolean bookId;
    private String scheme;
    private String value;
    
    public Identifier() {
        this("UUID", UUID.randomUUID().toString());
    }
    
    public Identifier(final String scheme, final String value) {
        this.bookId = false;
        this.scheme = scheme;
        this.value = value;
    }
    
    public static Identifier getBookIdIdentifier(final List<Identifier> identifiers) {
        if (identifiers == null || identifiers.isEmpty()) {
            return null;
        }
        Identifier result = null;
        for (final Identifier identifier : identifiers) {
            if (identifier.isBookId()) {
                result = identifier;
                break;
            }
        }
        if (result == null) {
            result = identifiers.get(0);
        }
        return result;
    }
    
    public String getScheme() {
        return this.scheme;
    }
    
    public void setScheme(final String scheme) {
        this.scheme = scheme;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public void setBookId(final boolean bookId) {
        this.bookId = bookId;
    }
    
    public boolean isBookId() {
        return this.bookId;
    }
    
    @Override
    public int hashCode() {
        return StringUtil.defaultIfNull(this.scheme).hashCode() ^ StringUtil.defaultIfNull(this.value).hashCode();
    }
    
    @Override
    public boolean equals(final Object otherIdentifier) {
        return otherIdentifier instanceof Identifier && StringUtil.equals(this.scheme, ((Identifier)otherIdentifier).scheme) && StringUtil.equals(this.value, ((Identifier)otherIdentifier).value);
    }
    
    @Override
    public String toString() {
        if (StringUtil.isBlank(this.scheme)) {
            return "" + this.value;
        }
        return "" + this.scheme + ":" + this.value;
    }
    
    public interface Scheme
    {
        public static final String UUID = "UUID";
        public static final String ISBN = "ISBN";
        public static final String URL = "URL";
        public static final String URI = "URI";
    }
}
