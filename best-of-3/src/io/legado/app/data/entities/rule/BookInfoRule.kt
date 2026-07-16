package io.legado.app.data.entities.rule

public data class BookInfoRule(init: String? = null,
   name: String? = null,
   author: String? = null,
   intro: String? = null,
   kind: String? = null,
   lastChapter: String? = null,
   updateTime: String? = null,
   coverUrl: String? = null,
   tocUrl: String? = null,
   wordCount: String? = null,
   canReName: String? = null
) {
   public final var author: String?
      internal set

   public final var canReName: String?
      internal set

   public final var coverUrl: String?
      internal set

   public final var init: String?
      internal set

   public final var intro: String?
      internal set

   public final var kind: String?
      internal set

   public final var lastChapter: String?
      internal set

   public final var name: String?
      internal set

   public final var tocUrl: String?
      internal set

   public final var updateTime: String?
      internal set

   public final var wordCount: String?
      internal set

   init {
      this.init = init;
      this.name = name;
      this.author = author;
      this.intro = intro;
      this.kind = kind;
      this.lastChapter = lastChapter;
      this.updateTime = updateTime;
      this.coverUrl = coverUrl;
      this.tocUrl = tocUrl;
      this.wordCount = wordCount;
      this.canReName = canReName;
   }

   public operator fun component1(): String? {
      return this.init;
   }

   public operator fun component2(): String? {
      return this.name;
   }

   public operator fun component3(): String? {
      return this.author;
   }

   public operator fun component4(): String? {
      return this.intro;
   }

   public operator fun component5(): String? {
      return this.kind;
   }

   public operator fun component6(): String? {
      return this.lastChapter;
   }

   public operator fun component7(): String? {
      return this.updateTime;
   }

   public operator fun component8(): String? {
      return this.coverUrl;
   }

   public operator fun component9(): String? {
      return this.tocUrl;
   }

   public operator fun component10(): String? {
      return this.wordCount;
   }

   public operator fun component11(): String? {
      return this.canReName;
   }

   public fun copy(
      init: String? = this.init,
      name: String? = this.name,
      author: String? = this.author,
      intro: String? = this.intro,
      kind: String? = this.kind,
      lastChapter: String? = this.lastChapter,
      updateTime: String? = this.updateTime,
      coverUrl: String? = this.coverUrl,
      tocUrl: String? = this.tocUrl,
      wordCount: String? = this.wordCount,
      canReName: String? = this.canReName
   ): BookInfoRule {
      return new BookInfoRule(init, name, author, intro, kind, lastChapter, updateTime, coverUrl, tocUrl, wordCount, canReName);
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("BookInfoRule(init=")
         .append(this.init)
         .append(", name=")
         .append(this.name)
         .append(", author=")
         .append(this.author)
         .append(", intro=")
         .append(this.intro)
         .append(", kind=")
         .append(this.kind)
         .append(", lastChapter=")
         .append(this.lastChapter)
         .append(", updateTime=")
         .append(this.updateTime)
         .append(", coverUrl=")
         .append(this.coverUrl)
         .append(", tocUrl=")
         .append(this.tocUrl)
         .append(", wordCount=")
         .append(this.wordCount)
         .append(", canReName=")
         .append(this.canReName)
         .append(')');
      return var1.toString();
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          (
                                                   (
                                                            (
                                                                     (
                                                                              (
                                                                                       (if (this.init == null) 0 else this.init.hashCode()) * 31
                                                                                          + (if (this.name == null) 0 else this.name.hashCode())
                                                                                    )
                                                                                    * 31
                                                                                 + (if (this.author == null) 0 else this.author.hashCode())
                                                                           )
                                                                           * 31
                                                                        + (if (this.intro == null) 0 else this.intro.hashCode())
                                                                  )
                                                                  * 31
                                                               + (if (this.kind == null) 0 else this.kind.hashCode())
                                                         )
                                                         * 31
                                                      + (if (this.lastChapter == null) 0 else this.lastChapter.hashCode())
                                                )
                                                * 31
                                             + (if (this.updateTime == null) 0 else this.updateTime.hashCode())
                                       )
                                       * 31
                                    + (if (this.coverUrl == null) 0 else this.coverUrl.hashCode())
                              )
                              * 31
                           + (if (this.tocUrl == null) 0 else this.tocUrl.hashCode())
                     )
                     * 31
                  + (if (this.wordCount == null) 0 else this.wordCount.hashCode())
            )
            * 31
         + (if (this.canReName == null) 0 else this.canReName.hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is BookInfoRule) {
         return false;
      } else {
         val var2: BookInfoRule = other as BookInfoRule;
         if (!(this.init == (other as BookInfoRule).init)) {
            return false;
         } else if (!(this.name == var2.name)) {
            return false;
         } else if (!(this.author == var2.author)) {
            return false;
         } else if (!(this.intro == var2.intro)) {
            return false;
         } else if (!(this.kind == var2.kind)) {
            return false;
         } else if (!(this.lastChapter == var2.lastChapter)) {
            return false;
         } else if (!(this.updateTime == var2.updateTime)) {
            return false;
         } else if (!(this.coverUrl == var2.coverUrl)) {
            return false;
         } else if (!(this.tocUrl == var2.tocUrl)) {
            return false;
         } else if (!(this.wordCount == var2.wordCount)) {
            return false;
         } else {
            return this.canReName == var2.canReName;
         }
      }
   }

   fun BookInfoRule() {
      this(null, null, null, null, null, null, null, null, null, null, null, 2047, null);
   }
}
