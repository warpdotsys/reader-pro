package me.ag2s.epublib.util.commons.io;

import java.io.IOException;
import java.util.Objects;

@FunctionalInterface
public interface IOConsumer {
   void accept(Object t) throws IOException;

   default IOConsumer andThen(final IOConsumer after) {
      Objects.requireNonNull(after);
      return (t) -> {
         this.accept(t);
         after.accept(t);
      };
   }
}
