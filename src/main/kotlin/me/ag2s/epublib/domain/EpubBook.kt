package me.ag2s.epublib.domain

import java.io.Serializable

class EpubBook : Serializable {
    private var resources = Resources(); private var metadata = Metadata(); private var spine = Spine(); private var tableOfContents = TableOfContents(); private val guide = Guide(); private var opfResource: Resource? = null; private var ncxResource: Resource? = null; private var coverImage: Resource? = null; private var version = "2.0"
    fun getVersion() = version; fun setVersion(value: String) { version = value }; fun isEpub3() = version.startsWith("3.")
    fun addSection(parent: TOCReference, title: String, resource: Resource) = addSection(parent, title, resource, null)
    fun addSection(parent: TOCReference, title: String, resource: Resource, fragmentId: String?): TOCReference { resources.add(resource); if (spine.findFirstResourceById(resource.getId()) < 0) spine.addSpineReference(SpineReference(resource)); return parent.addChildSection(TOCReference(title, resource, fragmentId)) }
    fun addSection(title: String, resource: Resource) = addSection(title, resource, null)
    fun addSection(title: String, resource: Resource, fragmentId: String?): TOCReference { resources.add(resource); val reference = tableOfContents.addTOCReference(TOCReference(title, resource, fragmentId)); if (spine.findFirstResourceById(resource.getId()) < 0) spine.addSpineReference(SpineReference(resource)); return reference }
    fun generateSpineFromTableOfContents() { val generated = Spine(tableOfContents); generated.setTocResource(spine.getTocResource()); spine = generated }
    fun getMetadata() = metadata; fun setMetadata(value: Metadata) { metadata = value }; fun setResources(value: Resources) { resources = value }; fun addResource(value: Resource) = resources.add(value); fun getResources() = resources; fun getSpine() = spine; fun setSpine(value: Spine) { spine = value }; fun getTableOfContents() = tableOfContents; fun setTableOfContents(value: TableOfContents) { tableOfContents = value }
    fun getCoverPage(): Resource? = guide.getCoverPage() ?: spine.getResource(0)
    fun setCoverPage(value: Resource?) { if (value != null) { if (resources.notContainsByHref(value.getHref())) resources.add(value); guide.setCoverPage(value) } }
    fun getTitle() = metadata.getFirstTitle(); fun getCoverImage() = coverImage; fun setCoverImage(value: Resource?) { if (value != null) { if (resources.notContainsByHref(value.getHref())) resources.add(value); coverImage = value } }; fun getGuide() = guide
    fun getContents(): MutableList<Resource> { val result = LinkedHashMap<String?, Resource>(); fun add(value: Resource?) { if (value != null && !result.containsKey(value.getHref())) result[value.getHref()] = value }; add(getCoverPage()); spine.getSpineReferences().forEach { add(it.getResource()) }; tableOfContents.getAllUniqueResources().forEach { add(it) }; guide.getReferences().forEach { add(it.getResource()) }; return ArrayList(result.values) }
    fun getOpfResource() = opfResource; fun setOpfResource(value: Resource?) { opfResource = value }; fun setNcxResource(value: Resource?) { ncxResource = value }; fun getNcxResource() = ncxResource
    companion object { private const val serialVersionUID = 2068355170895770100L }
}
