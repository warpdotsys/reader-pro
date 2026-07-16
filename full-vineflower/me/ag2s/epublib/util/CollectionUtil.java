package me.ag2s.epublib.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {
   public static <T> Enumeration<T> createEnumerationFromIterator(Iterator<T> it) {
      return new CollectionUtil.IteratorEnumerationAdapter<>(it);
   }

   public static <T> T first(List<T> list) {
      return list != null && !list.isEmpty() ? list.get(0) : null;
   }

   public static boolean isEmpty(Collection<?> collection) {
      return collection == null || collection.isEmpty();
   }

   private static class IteratorEnumerationAdapter<T> implements Enumeration<T> {
      private final Iterator<T> iterator;

      public IteratorEnumerationAdapter(Iterator<T> iter) {
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
