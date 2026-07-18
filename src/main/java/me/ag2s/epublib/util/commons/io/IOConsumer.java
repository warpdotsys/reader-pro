package me.ag2s.epublib.util.commons.io;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
public interface IOConsumer<T> {
   void accept(T t) throws IOException;

   default IOConsumer<T> andThen(final IOConsumer<? super T> after) {
      Objects.requireNonNull(after);
      return (t) -> {
         this.accept(t);
         after.accept(t);
      };
   }
}
