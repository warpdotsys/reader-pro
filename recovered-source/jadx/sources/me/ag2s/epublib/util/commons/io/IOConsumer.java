package me.ag2s.epublib.util.commons.io;

import java.io.IOException;
import java.util.Objects;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/util/commons/io/IOConsumer.class */
@FunctionalInterface
public interface IOConsumer<T> {
    void accept(T t) throws IOException;

    default IOConsumer<T> andThen(final IOConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return obj -> {
            accept(obj);
            after.accept(obj);
        };
    }
}
