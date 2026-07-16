// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.util.commons.io;

import java.util.Objects;
import java.io.IOException;

@FunctionalInterface
public interface IOConsumer<T>
{
    void accept(final T t) throws IOException;
    
    default IOConsumer<T> andThen(final IOConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
}
