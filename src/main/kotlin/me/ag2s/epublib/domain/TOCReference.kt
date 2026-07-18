package me.ag2s.epublib.domain

import java.io.Serializable
import java.util.Comparator

class TOCReference(title: String?, resource: Resource?, fragmentId: String?, private var children: MutableList<TOCReference>) : TitledResourceReference(resource, title, fragmentId), Serializable {
    @Deprecated("Deprecated in original API") constructor() : this(null, null, null)
    constructor(name: String?, resource: Resource?) : this(name, resource, null)
    constructor(name: String?, resource: Resource?, fragmentId: String?) : this(name, resource, fragmentId, ArrayList())
    fun getChildren(): MutableList<TOCReference> = children
    fun addChildSection(childSection: TOCReference): TOCReference { children.add(childSection); return childSection }
    fun setChildren(children: MutableList<TOCReference>) { this.children = children }
    companion object {
        private const val serialVersionUID = 5787958246077042456L
        private val comparatorByTitleIgnoreCase = Comparator<TOCReference> { first, second -> String.CASE_INSENSITIVE_ORDER.compare(first.getTitle(), second.getTitle()) }
        @JvmStatic fun getComparatorByTitleIgnoreCase(): Comparator<TOCReference> = comparatorByTitleIgnoreCase
    }
}
