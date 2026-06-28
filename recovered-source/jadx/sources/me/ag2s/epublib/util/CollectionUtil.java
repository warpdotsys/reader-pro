package me.ag2s.epublib.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/util/CollectionUtil.class */
public class CollectionUtil {

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/util/CollectionUtil$IteratorEnumerationAdapter.class */
    private static class IteratorEnumerationAdapter<T> implements Enumeration<T> {
        private final Iterator<T> iterator;

        public IteratorEnumerationAdapter(Iterator<T> iter) {
            this.iterator = iter;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }

        @Override // java.util.Enumeration
        public T nextElement() {
            return this.iterator.next();
        }
    }

    public static <T> Enumeration<T> createEnumerationFromIterator(Iterator<T> it) {
        return new IteratorEnumerationAdapter(it);
    }

    public static <T> T first(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
