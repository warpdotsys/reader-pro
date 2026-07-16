package io.legado.app.data.entities

public data class Bookmark(time: Long = System.currentTimeMillis(),
   bookName: String = "",
   bookAuthor: String = "",
   chapterIndex: Int = 0,
   chapterPos: Int = 0,
   chapterName: String = "",
   bookText: String = "",
   content: String = ""
) {
   public final val bookAuthor: String
   public final val bookName: String

   public final var bookText: String
      internal set

   public final var chapterIndex: Int
      internal set

   public final var chapterName: String
      internal set

   public final var chapterPos: Int
      internal set

   public final var content: String
      internal set

   public final val time: Long

   init {
      this.time = time;
      this.bookName = bookName;
      this.bookAuthor = bookAuthor;
      this.chapterIndex = chapterIndex;
      this.chapterPos = chapterPos;
      this.chapterName = chapterName;
      this.bookText = bookText;
      this.content = content;
   }

   public operator fun component1(): Long {
      return this.time;
   }

   public operator fun component2(): String {
      return this.bookName;
   }

   public operator fun component3(): String {
      return this.bookAuthor;
   }

   public operator fun component4(): Int {
      return this.chapterIndex;
   }

   public operator fun component5(): Int {
      return this.chapterPos;
   }

   public operator fun component6(): String {
      return this.chapterName;
   }

   public operator fun component7(): String {
      return this.bookText;
   }

   public operator fun component8(): String {
      return this.content;
   }

   public fun copy(
      time: Long = this.time,
      bookName: String = this.bookName,
      bookAuthor: String = this.bookAuthor,
      chapterIndex: Int = this.chapterIndex,
      chapterPos: Int = this.chapterPos,
      chapterName: String = this.chapterName,
      bookText: String = this.bookText,
      content: String = this.content
   ): Bookmark {
      return new Bookmark(time, bookName, bookAuthor, chapterIndex, chapterPos, chapterName, bookText, content);
   }

   public override fun toString(): String {
      return "Bookmark(time=${this.time}, bookName=${this.bookName}, bookAuthor=${this.bookAuthor}, chapterIndex=${this.chapterIndex}, chapterPos=${this.chapterPos}, chapterName=${this.chapterName}, bookText=${this.bookText}, content=${this.content})";
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          ((java.lang.Long.hashCode(this.time) * 31 + this.bookName.hashCode()) * 31 + this.bookAuthor.hashCode()) * 31
                                             + Integer.hashCode(this.chapterIndex)
                                       )
                                       * 31
                                    + Integer.hashCode(this.chapterPos)
                              )
                              * 31
                           + this.chapterName.hashCode()
                     )
                     * 31
                  + this.bookText.hashCode()
            )
            * 31
         + this.content.hashCode();
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is Bookmark) {
         return false;
      } else {
         val var2: Bookmark = other as Bookmark;
         if (this.time != (other as Bookmark).time) {
            return false;
         } else if (!(this.bookName == var2.bookName)) {
            return false;
         } else if (!(this.bookAuthor == var2.bookAuthor)) {
            return false;
         } else if (this.chapterIndex != var2.chapterIndex) {
            return false;
         } else if (this.chapterPos != var2.chapterPos) {
            return false;
         } else if (!(this.chapterName == var2.chapterName)) {
            return false;
         } else if (!(this.bookText == var2.bookText)) {
            return false;
         } else {
            return this.content == var2.content;
         }
      }
   }

   fun Bookmark() {
      this(0L, null, null, 0, 0, null, null, null, 255, null);
   }
}
