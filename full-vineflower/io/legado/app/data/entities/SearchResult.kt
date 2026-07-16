package io.legado.app.data.entities

public data class SearchResult(resultCount: Int = 0,
   resultCountWithinChapter: Int = 0,
   resultText: String = "",
   chapterTitle: String = "",
   query: String = "",
   pageSize: Int = 0,
   chapterIndex: Int = 0,
   pageIndex: Int = 0,
   queryIndexInResult: Int = 0,
   queryIndexInChapter: Int = 0
) {
   public final val chapterIndex: Int
   public final val chapterTitle: String
   public final val pageIndex: Int
   public final val pageSize: Int
   public final val query: String
   public final val queryIndexInChapter: Int
   public final val queryIndexInResult: Int
   public final val resultCount: Int
   public final val resultCountWithinChapter: Int
   public final val resultText: String

   init {
      this.resultCount = resultCount;
      this.resultCountWithinChapter = resultCountWithinChapter;
      this.resultText = resultText;
      this.chapterTitle = chapterTitle;
      this.query = query;
      this.pageSize = pageSize;
      this.chapterIndex = chapterIndex;
      this.pageIndex = pageIndex;
      this.queryIndexInResult = queryIndexInResult;
      this.queryIndexInChapter = queryIndexInChapter;
   }

   public operator fun component1(): Int {
      return this.resultCount;
   }

   public operator fun component2(): Int {
      return this.resultCountWithinChapter;
   }

   public operator fun component3(): String {
      return this.resultText;
   }

   public operator fun component4(): String {
      return this.chapterTitle;
   }

   public operator fun component5(): String {
      return this.query;
   }

   public operator fun component6(): Int {
      return this.pageSize;
   }

   public operator fun component7(): Int {
      return this.chapterIndex;
   }

   public operator fun component8(): Int {
      return this.pageIndex;
   }

   public operator fun component9(): Int {
      return this.queryIndexInResult;
   }

   public operator fun component10(): Int {
      return this.queryIndexInChapter;
   }

   public fun copy(
      resultCount: Int = this.resultCount,
      resultCountWithinChapter: Int = this.resultCountWithinChapter,
      resultText: String = this.resultText,
      chapterTitle: String = this.chapterTitle,
      query: String = this.query,
      pageSize: Int = this.pageSize,
      chapterIndex: Int = this.chapterIndex,
      pageIndex: Int = this.pageIndex,
      queryIndexInResult: Int = this.queryIndexInResult,
      queryIndexInChapter: Int = this.queryIndexInChapter
   ): SearchResult {
      return new SearchResult(
         resultCount, resultCountWithinChapter, resultText, chapterTitle, query, pageSize, chapterIndex, pageIndex, queryIndexInResult, queryIndexInChapter
      );
   }

   public override fun toString(): String {
      return "SearchResult(resultCount=${this.resultCount}, resultCountWithinChapter=${this.resultCountWithinChapter}, resultText=${this.resultText}, chapterTitle=${this.chapterTitle}, query=${this.query}, pageSize=${this.pageSize}, chapterIndex=${this.chapterIndex}, pageIndex=${this.pageIndex}, queryIndexInResult=${this.queryIndexInResult}, queryIndexInChapter=${this.queryIndexInChapter})";
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          (
                                                   (
                                                            (
                                                                     (Integer.hashCode(this.resultCount) * 31 + Integer.hashCode(this.resultCountWithinChapter))
                                                                           * 31
                                                                        + this.resultText.hashCode()
                                                                  )
                                                                  * 31
                                                               + this.chapterTitle.hashCode()
                                                         )
                                                         * 31
                                                      + this.query.hashCode()
                                                )
                                                * 31
                                             + Integer.hashCode(this.pageSize)
                                       )
                                       * 31
                                    + Integer.hashCode(this.chapterIndex)
                              )
                              * 31
                           + Integer.hashCode(this.pageIndex)
                     )
                     * 31
                  + Integer.hashCode(this.queryIndexInResult)
            )
            * 31
         + Integer.hashCode(this.queryIndexInChapter);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is SearchResult) {
         return false;
      } else {
         val var2: SearchResult = other as SearchResult;
         if (this.resultCount != (other as SearchResult).resultCount) {
            return false;
         } else if (this.resultCountWithinChapter != var2.resultCountWithinChapter) {
            return false;
         } else if (!(this.resultText == var2.resultText)) {
            return false;
         } else if (!(this.chapterTitle == var2.chapterTitle)) {
            return false;
         } else if (!(this.query == var2.query)) {
            return false;
         } else if (this.pageSize != var2.pageSize) {
            return false;
         } else if (this.chapterIndex != var2.chapterIndex) {
            return false;
         } else if (this.pageIndex != var2.pageIndex) {
            return false;
         } else if (this.queryIndexInResult != var2.queryIndexInResult) {
            return false;
         } else {
            return this.queryIndexInChapter == var2.queryIndexInChapter;
         }
      }
   }

   fun SearchResult() {
      this(0, 0, null, null, null, 0, 0, 0, 0, 0, 1023, null);
   }
}
