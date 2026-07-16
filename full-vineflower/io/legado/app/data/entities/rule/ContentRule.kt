package io.legado.app.data.entities.rule

public data class ContentRule(content: String? = null,
   nextContentUrl: String? = null,
   webJs: String? = null,
   sourceRegex: String? = null,
   replaceRegex: String? = null,
   imageStyle: String? = null
) {
   public final var content: String?
      internal set

   public final var imageStyle: String?
      internal set

   public final var nextContentUrl: String?
      internal set

   public final var replaceRegex: String?
      internal set

   public final var sourceRegex: String?
      internal set

   public final var webJs: String?
      internal set

   init {
      this.content = content;
      this.nextContentUrl = nextContentUrl;
      this.webJs = webJs;
      this.sourceRegex = sourceRegex;
      this.replaceRegex = replaceRegex;
      this.imageStyle = imageStyle;
   }

   public operator fun component1(): String? {
      return this.content;
   }

   public operator fun component2(): String? {
      return this.nextContentUrl;
   }

   public operator fun component3(): String? {
      return this.webJs;
   }

   public operator fun component4(): String? {
      return this.sourceRegex;
   }

   public operator fun component5(): String? {
      return this.replaceRegex;
   }

   public operator fun component6(): String? {
      return this.imageStyle;
   }

   public fun copy(
      content: String? = this.content,
      nextContentUrl: String? = this.nextContentUrl,
      webJs: String? = this.webJs,
      sourceRegex: String? = this.sourceRegex,
      replaceRegex: String? = this.replaceRegex,
      imageStyle: String? = this.imageStyle
   ): ContentRule {
      return new ContentRule(content, nextContentUrl, webJs, sourceRegex, replaceRegex, imageStyle);
   }

   public override fun toString(): String {
      return "ContentRule(content=${this.content}, nextContentUrl=${this.nextContentUrl}, webJs=${this.webJs}, sourceRegex=${this.sourceRegex}, replaceRegex=${this.replaceRegex}, imageStyle=${this.imageStyle})";
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          (if (this.content == null) 0 else this.content.hashCode()) * 31
                                             + (if (this.nextContentUrl == null) 0 else this.nextContentUrl.hashCode())
                                       )
                                       * 31
                                    + (if (this.webJs == null) 0 else this.webJs.hashCode())
                              )
                              * 31
                           + (if (this.sourceRegex == null) 0 else this.sourceRegex.hashCode())
                     )
                     * 31
                  + (if (this.replaceRegex == null) 0 else this.replaceRegex.hashCode())
            )
            * 31
         + (if (this.imageStyle == null) 0 else this.imageStyle.hashCode());
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is ContentRule) {
         return false;
      } else {
         val var2: ContentRule = other as ContentRule;
         if (!(this.content == (other as ContentRule).content)) {
            return false;
         } else if (!(this.nextContentUrl == var2.nextContentUrl)) {
            return false;
         } else if (!(this.webJs == var2.webJs)) {
            return false;
         } else if (!(this.sourceRegex == var2.sourceRegex)) {
            return false;
         } else if (!(this.replaceRegex == var2.replaceRegex)) {
            return false;
         } else {
            return this.imageStyle == var2.imageStyle;
         }
      }
   }

   fun ContentRule() {
      this(null, null, null, null, null, null, 63, null);
   }
}
