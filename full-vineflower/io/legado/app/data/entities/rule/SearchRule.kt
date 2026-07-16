package io.legado.app.data.entities.rule

public data class SearchRule(bookList: String? = null,
      name: String? = null,
      author: String? = null,
      intro: String? = null,
      kind: String? = null,
      lastChapter: String? = null,
      updateTime: String? = null,
      bookUrl: String? = null,
      coverUrl: String? = null,
      wordCount: String? = null
   ) :
   BookListRule {
   public open var author: String?
      internal final set

   public open var bookList: String?
      internal final set

   public open var bookUrl: String?
      internal final set

   public open var coverUrl: String?
      internal final set

   public open var intro: String?
      internal final set

   public open var kind: String?
      internal final set

   public open var lastChapter: String?
      internal final set

   public open var name: String?
      internal final set

   public open var updateTime: String?
      internal final set

   public open var wordCount: String?
      internal final set

   init {
      this.bookList = bookList;
      this.name = name;
      this.author = author;
      this.intro = intro;
      this.kind = kind;
      this.lastChapter = lastChapter;
      this.updateTime = updateTime;
      this.bookUrl = bookUrl;
      this.coverUrl = coverUrl;
      this.wordCount = wordCount;
   }

   public operator fun component1(): String? {
      return this.getBookList();
   }

   public operator fun component2(): String? {
      return this.getName();
   }

   public operator fun component3(): String? {
      return this.getAuthor();
   }

   public operator fun component4(): String? {
      return this.getIntro();
   }

   public operator fun component5(): String? {
      return this.getKind();
   }

   public operator fun component6(): String? {
      return this.getLastChapter();
   }

   public operator fun component7(): String? {
      return this.getUpdateTime();
   }

   public operator fun component8(): String? {
      return this.getBookUrl();
   }

   public operator fun component9(): String? {
      return this.getCoverUrl();
   }

   public operator fun component10(): String? {
      return this.getWordCount();
   }

   public fun copy(
      bookList: String? = this.getBookList(),
      name: String? = this.getName(),
      author: String? = this.getAuthor(),
      intro: String? = this.getIntro(),
      kind: String? = this.getKind(),
      lastChapter: String? = this.getLastChapter(),
      updateTime: String? = this.getUpdateTime(),
      bookUrl: String? = this.getBookUrl(),
      coverUrl: String? = this.getCoverUrl(),
      wordCount: String? = this.getWordCount()
   ): SearchRule {
      return new SearchRule(bookList, name, author, intro, kind, lastChapter, updateTime, bookUrl, coverUrl, wordCount);
   }

   public override fun toString(): String {
      return "SearchRule(bookList=${this.getBookList()}, name=${this.getName()}, author=${this.getAuthor()}, intro=${this.getIntro()}, kind=${this.getKind()}, lastChapter=${this.getLastChapter()}, updateTime=${this.getUpdateTime()}, bookUrl=${this.getBookUrl()}, coverUrl=${this.getCoverUrl()}, wordCount=${this.getWordCount()})";
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
                                                                              (if (this.getBookList() == null) 0 else this.getBookList().hashCode()) * 31
                                                                                 + (if (this.getName() == null) 0 else this.getName().hashCode())
                                                                           )
                                                                           * 31
                                                                        + (if (this.getAuthor() == null) 0 else this.getAuthor().hashCode())
                                                                  )
                                                                  * 31
                                                               + (if (this.getIntro() == null) 0 else this.getIntro().hashCode())
                                                         )
                                                         * 31
                                                      + (if (this.getKind() == null) 0 else this.getKind().hashCode())
                                                )
                                                * 31
                                             + (if (this.getLastChapter() == null) 0 else this.getLastChapter().hashCode())
                                       )
                                       * 31
                                    + (if (this.getUpdateTime() == null) 0 else this.getUpdateTime().hashCode())
                              )
                              * 31
                           + (if (this.getBookUrl() == null) 0 else this.getBookUrl().hashCode())
                     )
                     * 31
                  + (if (this.getCoverUrl() == null) 0 else this.getCoverUrl().hashCode())
            )
            * 31
         + (if (this.getWordCount() == null) 0 else this.getWordCount().hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is SearchRule) {
         return false;
      } else {
         val var2: SearchRule = other as SearchRule;
         if (!(this.getBookList() == (other as SearchRule).getBookList())) {
            return false;
         } else if (!(this.getName() == var2.getName())) {
            return false;
         } else if (!(this.getAuthor() == var2.getAuthor())) {
            return false;
         } else if (!(this.getIntro() == var2.getIntro())) {
            return false;
         } else if (!(this.getKind() == var2.getKind())) {
            return false;
         } else if (!(this.getLastChapter() == var2.getLastChapter())) {
            return false;
         } else if (!(this.getUpdateTime() == var2.getUpdateTime())) {
            return false;
         } else if (!(this.getBookUrl() == var2.getBookUrl())) {
            return false;
         } else if (!(this.getCoverUrl() == var2.getCoverUrl())) {
            return false;
         } else {
            return this.getWordCount() == var2.getWordCount();
         }
      }
   }

   fun SearchRule() {
      this(null, null, null, null, null, null, null, null, null, null, 1023, null);
   }
}
