// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;
import java.io.Serializable;

public class Author implements Serializable
{
    private static final long serialVersionUID = 6663408501416574200L;
    private String firstname;
    private String lastname;
    private Relator relator;
    
    public Author(final String singleName) {
        this("", singleName);
    }
    
    public Author(final String firstname, final String lastname) {
        this.relator = Relator.AUTHOR;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }
    
    @Override
    public String toString() {
        return this.lastname + ", " + this.firstname;
    }
    
    @Override
    public int hashCode() {
        return StringUtil.hashCode(this.firstname, this.lastname);
    }
    
    @Override
    public boolean equals(final Object authorObject) {
        if (!(authorObject instanceof Author)) {
            return false;
        }
        final Author other = (Author)authorObject;
        return StringUtil.equals(this.firstname, other.firstname) && StringUtil.equals(this.lastname, other.lastname);
    }
    
    public void setRole(final String code) {
        Relator result = Relator.byCode(code);
        if (result == null) {
            result = Relator.AUTHOR;
        }
        this.relator = result;
    }
    
    public Relator getRelator() {
        return this.relator;
    }
    
    public void setRelator(final Relator relator) {
        this.relator = relator;
    }
}
