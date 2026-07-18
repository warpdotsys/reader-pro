package me.ag2s.epublib.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {
   public static Enumeration createEnumerationFromIterator(Iterator it) {
      return new IteratorEnumerationAdapter(it);
   }

   public static Object first(List list) {
      return list != null && !list.isEmpty() ? list.get(0) : null;
   }

   public static boolean isEmpty(Collection collection) {
      return collection == null || collection.isEmpty();
   }

   private static class IteratorEnumerationAdapter implements Enumeration {
      private final Iterator iterator;

      public IteratorEnumerationAdapter(Iterator iter) {
         this.iterator = iter;
      }

      public boolean hasMoreElements() {
         return this.iterator.hasNext();
      }

      public Object nextElement() {
         return this.iterator.next();
      }
   }
}
