// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.util;

import java.util.Collection;
import java.util.List;
import java.util.Enumeration;
import java.util.Iterator;

public class CollectionUtil
{
    public static <T> Enumeration<T> createEnumerationFromIterator(final Iterator<T> it) {
        return new IteratorEnumerationAdapter<T>(it);
    }
    
    public static <T> T first(final List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    private static class IteratorEnumerationAdapter<T> implements Enumeration<T>
    {
        private final Iterator<T> iterator;
        
        public IteratorEnumerationAdapter(final Iterator<T> iter) {
            this.iterator = iter;
        }
        
        @Override
        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }
        
        @Override
        public T nextElement() {
            return this.iterator.next();
        }
    }
}
