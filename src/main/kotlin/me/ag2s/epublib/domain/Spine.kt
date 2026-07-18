package me.ag2s.epublib.domain

import java.io.Serializable
import me.ag2s.epublib.util.StringUtil

class Spine : Serializable {
    private var tocResource: Resource? = null
    private var spineReferences: MutableList<SpineReference>
    constructor() : this(ArrayList())
    constructor(tableOfContents: TableOfContents) { spineReferences = createSpineReferences(tableOfContents.getAllUniqueResources()) }
    constructor(spineReferences: MutableList<SpineReference>) { this.spineReferences = spineReferences }
    fun getSpineReferences(): MutableList<SpineReference> = spineReferences
    fun setSpineReferences(spineReferences: MutableList<SpineReference>) { this.spineReferences = spineReferences }
    fun getResource(index: Int): Resource? = if (index >= 0 && index < spineReferences.size) spineReferences[index].getResource() else null
    fun findFirstResourceById(resourceId: String?): Int = if (StringUtil.isBlank(resourceId)) -1 else spineReferences.indexOfFirst { resourceId == it.getResourceId() }
    fun addSpineReference(spineReference: SpineReference): SpineReference { spineReferences.add(spineReference); return spineReference }
    fun addResource(resource: Resource?): SpineReference = addSpineReference(SpineReference(resource))
    fun size(): Int = spineReferences.size
    fun setTocResource(tocResource: Resource?) { this.tocResource = tocResource }
    fun getTocResource(): Resource? = tocResource
    fun getResourceIndex(currentResource: Resource?): Int = if (currentResource == null) -1 else getResourceIndex(currentResource.getHref())
    fun getResourceIndex(resourceHref: String?): Int = if (StringUtil.isBlank(resourceHref)) -1 else spineReferences.indexOfFirst { resourceHref == it.getResource()!!.getHref() }
    fun isEmpty(): Boolean = spineReferences.isEmpty()
    companion object { private const val serialVersionUID = 3878483958947357246L; @JvmStatic fun createSpineReferences(resources: Collection<Resource>): MutableList<SpineReference> = resources.mapTo(ArrayList(resources.size)) { SpineReference(it) } }
}
