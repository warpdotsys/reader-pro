package me.ag2s.epublib.domain

import java.io.Serializable
import me.ag2s.epublib.util.StringUtil

class Resources : Serializable {
    private var lastId = 1
    private var resources: MutableMap<String?, Resource> = HashMap()
    fun add(resource: Resource): Resource { fixResourceHref(resource); fixResourceId(resource); resources[resource.getHref()] = resource; return resource }
    fun fixResourceId(resource: Resource) { var id = resource.getId(); if (StringUtil.isBlank(resource.getId())) { id = StringUtil.substringAfterLast(StringUtil.substringBeforeLast(resource.getHref(), '.'), '/') }; id = makeValidId(id, resource); if (StringUtil.isBlank(id) || containsId(id)) id = createUniqueResourceId(resource); resource.setId(id) }
    private fun makeValidId(id: String?, resource: Resource): String? = if (StringUtil.isNotBlank(id) && !Character.isJavaIdentifierStart(id!![0])) getResourceItemPrefix(resource) + id else id
    private fun getResourceItemPrefix(resource: Resource): String = if (MediaTypes.isBitmapImage(resource.getMediaType())) IMAGE_PREFIX else ITEM_PREFIX
    private fun createUniqueResourceId(resource: Resource): String { var counter = lastId; if (counter == Int.MAX_VALUE) { if (resources.size == Int.MAX_VALUE) throw IllegalArgumentException("Resources contains 2147483647 elements: no new elements can be added"); counter = 1 }; val prefix = getResourceItemPrefix(resource); var result = prefix + counter; while (containsId(result)) { counter++; result = prefix + counter }; lastId = counter; return result }
    fun containsId(id: String?): Boolean = !StringUtil.isBlank(id) && resources.values.any { id == it.getId() }
    fun getById(id: String?): Resource? = if (StringUtil.isBlank(id)) null else resources.values.firstOrNull { id == it.getId() }
    fun getByProperties(properties: String?): Resource? = if (StringUtil.isBlank(properties)) null else resources.values.firstOrNull { properties == it.getProperties() }
    fun remove(href: String?): Resource? = resources.remove(href)
    private fun fixResourceHref(resource: Resource) { if (!StringUtil.isNotBlank(resource.getHref()) || resources.containsKey(resource.getHref())) { if (StringUtil.isBlank(resource.getHref())) { val mediaType = resource.getMediaType() ?: throw IllegalArgumentException("Resource must have either a MediaType or a href"); var index = 1; var href = createHref(mediaType, index); while (resources.containsKey(href)) href = createHref(mediaType, ++index); resource.setHref(href) } } }
    private fun createHref(mediaType: MediaType, counter: Int): String = if (MediaTypes.isBitmapImage(mediaType)) "$IMAGE_PREFIX$counter${mediaType.getDefaultExtension()}" else "$ITEM_PREFIX$counter${mediaType.getDefaultExtension()}"
    fun isEmpty(): Boolean = resources.isEmpty()
    fun size(): Int = resources.size
    fun getResourceMap(): MutableMap<String?, Resource> = resources
    fun getAll(): Collection<Resource> = resources.values
    fun notContainsByHref(href: String?): Boolean = StringUtil.isBlank(href) || !resources.containsKey(StringUtil.substringBefore(href, '#'))
    fun containsByHref(href: String?): Boolean = !notContainsByHref(href)
    fun set(resources: Collection<Resource>) { this.resources.clear(); addAll(resources) }
    fun addAll(resources: Collection<Resource>) { resources.forEach { resource -> fixResourceHref(resource); this.resources[resource.getHref()] = resource } }
    fun set(resources: Map<String?, Resource>) { this.resources = HashMap(resources) }
    fun getByIdOrHref(idOrHref: String?): Resource? = getById(idOrHref) ?: getByHref(idOrHref)
    fun getByHref(href: String?): Resource? = if (StringUtil.isBlank(href)) null else resources[StringUtil.substringBefore(href, '#')]
    fun findFirstResourceByMediaType(mediaType: MediaType?): Resource? = findFirstResourceByMediaType(resources.values, mediaType)
    fun getResourcesByMediaType(mediaType: MediaType?): MutableList<Resource> = if (mediaType == null) ArrayList() else resources.values.filterTo(ArrayList()) { it.getMediaType() === mediaType }
    fun getResourcesByMediaTypes(mediaTypes: Array<MediaType>?): MutableList<Resource> = if (mediaTypes == null) ArrayList() else resources.values.filterTo(ArrayList()) { mediaTypes.asList().contains(it.getMediaType()) }
    fun getAllHrefs(): Collection<String?> = resources.keys
    companion object { private const val serialVersionUID = 2450876953383871451L; private const val IMAGE_PREFIX = "image_"; private const val ITEM_PREFIX = "item_"; @JvmStatic fun findFirstResourceByMediaType(resources: Collection<Resource>, mediaType: MediaType?): Resource? = resources.firstOrNull { it.getMediaType() === mediaType } }
}
