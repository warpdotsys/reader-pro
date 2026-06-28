package me.ag2s.epublib.domain;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/domain/LazyResourceProvider.class */
public interface LazyResourceProvider {
    InputStream getResourceStream(String href) throws IOException;
}
