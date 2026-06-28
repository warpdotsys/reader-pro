package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.StringUtil;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/Identifier.class */
public class Identifier implements Serializable {
    private static final long serialVersionUID = 955949951416391810L;
    private boolean bookId;
    private String scheme;
    private String value;

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/Identifier$Scheme.class */
    public interface Scheme {
        public static final String UUID = "UUID";
        public static final String ISBN = "ISBN";
        public static final String URL = "URL";
        public static final String URI = "URI";
    }

    public Identifier() {
        this(Scheme.UUID, UUID.randomUUID().toString());
    }

    public Identifier(String scheme, String value) {
        this.bookId = false;
        this.scheme = scheme;
        this.value = value;
    }

    public static Identifier getBookIdIdentifier(List<Identifier> identifiers) {
        if (identifiers == null || identifiers.isEmpty()) {
            return null;
        }
        Identifier result = null;
        Iterator<Identifier> it = identifiers.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Identifier identifier = it.next();
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

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBookId(boolean bookId) {
        this.bookId = bookId;
    }

    public boolean isBookId() {
        return this.bookId;
    }

    public int hashCode() {
        return StringUtil.defaultIfNull(this.scheme).hashCode() ^ StringUtil.defaultIfNull(this.value).hashCode();
    }

    public boolean equals(Object otherIdentifier) {
        return (otherIdentifier instanceof Identifier) && StringUtil.equals(this.scheme, ((Identifier) otherIdentifier).scheme) && StringUtil.equals(this.value, ((Identifier) otherIdentifier).value);
    }

    public String toString() {
        if (StringUtil.isBlank(this.scheme)) {
            return PackageDocumentBase.PREFIX_OPF + this.value;
        }
        return PackageDocumentBase.PREFIX_OPF + this.scheme + ":" + this.value;
    }
}
