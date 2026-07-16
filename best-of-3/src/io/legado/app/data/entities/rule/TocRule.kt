package io.legado.app.data.entities.rule

public data class TocRule(preUpdateJs: String? = null,
   chapterList: String? = null,
   chapterName: String? = null,
   chapterUrl: String? = null,
   isVolume: String? = null,
   isVip: String? = null,
   updateTime: String? = null,
   nextTocUrl: String? = null
) {
   public final var chapterList: String?
      internal set

   public final var chapterName: String?
      internal set

   public final var chapterUrl: String?
      internal set

   public final var isVip: String?
      internal set

   public final var isVolume: String?
      internal set

   public final var nextTocUrl: String?
      internal set

   public final var preUpdateJs: String?
      internal set

   public final var updateTime: String?
      internal set

   init {
      this.preUpdateJs = preUpdateJs;
      this.chapterList = chapterList;
      this.chapterName = chapterName;
      this.chapterUrl = chapterUrl;
      this.isVolume = isVolume;
      this.isVip = isVip;
      this.updateTime = updateTime;
      this.nextTocUrl = nextTocUrl;
   }

   public operator fun component1(): String? {
      return this.preUpdateJs;
   }

   public operator fun component2(): String? {
      return this.chapterList;
   }

   public operator fun component3(): String? {
      return this.chapterName;
   }

   public operator fun component4(): String? {
      return this.chapterUrl;
   }

   public operator fun component5(): String? {
      return this.isVolume;
   }

   public operator fun component6(): String? {
      return this.isVip;
   }

   public operator fun component7(): String? {
      return this.updateTime;
   }

   public operator fun component8(): String? {
      return this.nextTocUrl;
   }

   public fun copy(
      preUpdateJs: String? = this.preUpdateJs,
      chapterList: String? = this.chapterList,
      chapterName: String? = this.chapterName,
      chapterUrl: String? = this.chapterUrl,
      isVolume: String? = this.isVolume,
      isVip: String? = this.isVip,
      updateTime: String? = this.updateTime,
      nextTocUrl: String? = this.nextTocUrl
   ): TocRule {
      return new TocRule(preUpdateJs, chapterList, chapterName, chapterUrl, isVolume, isVip, updateTime, nextTocUrl);
   }

   public override fun toString(): String {
      return "TocRule(preUpdateJs=${this.preUpdateJs}, chapterList=${this.chapterList}, chapterName=${this.chapterName}, chapterUrl=${this.chapterUrl}, isVolume=${this.isVolume}, isVip=${this.isVip}, updateTime=${this.updateTime}, nextTocUrl=${this.nextTocUrl})";
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          (
                                                   (
                                                            (if (this.preUpdateJs == null) 0 else this.preUpdateJs.hashCode()) * 31
                                                               + (if (this.chapterList == null) 0 else this.chapterList.hashCode())
                                                         )
                                                         * 31
                                                      + (if (this.chapterName == null) 0 else this.chapterName.hashCode())
                                                )
                                                * 31
                                             + (if (this.chapterUrl == null) 0 else this.chapterUrl.hashCode())
                                       )
                                       * 31
                                    + (if (this.isVolume == null) 0 else this.isVolume.hashCode())
                              )
                              * 31
                           + (if (this.isVip == null) 0 else this.isVip.hashCode())
                     )
                     * 31
                  + (if (this.updateTime == null) 0 else this.updateTime.hashCode())
            )
            * 31
         + (if (this.nextTocUrl == null) 0 else this.nextTocUrl.hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is TocRule) {
         return false;
      } else {
         val var2: TocRule = other as TocRule;
         if (!(this.preUpdateJs == (other as TocRule).preUpdateJs)) {
            return false;
         } else if (!(this.chapterList == var2.chapterList)) {
            return false;
         } else if (!(this.chapterName == var2.chapterName)) {
            return false;
         } else if (!(this.chapterUrl == var2.chapterUrl)) {
            return false;
         } else if (!(this.isVolume == var2.isVolume)) {
            return false;
         } else if (!(this.isVip == var2.isVip)) {
            return false;
         } else if (!(this.updateTime == var2.updateTime)) {
            return false;
         } else {
            return this.nextTocUrl == var2.nextTocUrl;
         }
      }
   }

   fun TocRule() {
      this(null, null, null, null, null, null, null, null, 255, null);
   }
}
