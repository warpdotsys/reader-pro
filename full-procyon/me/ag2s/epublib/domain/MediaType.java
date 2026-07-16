// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.util.Arrays;
import java.util.Collection;
import java.io.Serializable;

public class MediaType implements Serializable
{
    private static final long serialVersionUID = -7256091153727506788L;
    private final String name;
    private final String defaultExtension;
    private final Collection<String> extensions;
    
    public MediaType(final String name, final String defaultExtension) {
        this(name, defaultExtension, new String[] { defaultExtension });
    }
    
    public MediaType(final String name, final String defaultExtension, final String[] extensions) {
        this(name, defaultExtension, Arrays.asList(extensions));
    }
    
    @Override
    public int hashCode() {
        if (this.name == null) {
            return 0;
        }
        return this.name.hashCode();
    }
    
    public MediaType(final String name, final String defaultExtension, final Collection<String> mextensions) {
        this.name = name;
        this.defaultExtension = defaultExtension;
        this.extensions = mextensions;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDefaultExtension() {
        return this.defaultExtension;
    }
    
    public Collection<String> getExtensions() {
        return this.extensions;
    }
    
    @Override
    public boolean equals(final Object otherMediaType) {
        return otherMediaType instanceof MediaType && this.name.equals(((MediaType)otherMediaType).getName());
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
