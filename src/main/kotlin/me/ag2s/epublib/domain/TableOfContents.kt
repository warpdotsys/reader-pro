package me.ag2s.epublib.domain

import java.io.Serializable

class TableOfContents : Serializable {
    private var tocReferences: MutableList<TOCReference>
    constructor() : this(ArrayList())
    constructor(tocReferences: MutableList<TOCReference>) { this.tocReferences = tocReferences }
    fun getTocReferences(): MutableList<TOCReference> = tocReferences
    fun setTocReferences(tocReferences: MutableList<TOCReference>) { this.tocReferences = tocReferences }
    fun addSection(resource: Resource?, path: String): TOCReference? = addSection(resource, path, DEFAULT_PATH_SEPARATOR)
    fun addSection(resource: Resource?, path: String, pathSeparator: String): TOCReference? = addSection(resource, path.split(pathSeparator).toTypedArray())
    fun addSection(resource: Resource?, pathElements: Array<String>?): TOCReference? { if (pathElements.isNullOrEmpty()) return null; var current = tocReferences; var result: TOCReference? = null; pathElements.forEach { title -> result = current.firstOrNull { it.getTitle() == title } ?: TOCReference(title, null).also { current.add(it) }; current = result!!.getChildren() }; result!!.setResource(resource); return result }
    fun addSection(resource: Resource?, pathElements: IntArray?, sectionTitlePrefix: String, sectionNumberSeparator: String): TOCReference? {
        if (pathElements == null || pathElements.isEmpty()) return null
        var current = tocReferences
        var result: TOCReference? = null
        pathElements.forEachIndexed { position, currentIndex ->
            result = if (currentIndex > 0 && currentIndex < current.size - 1) current[currentIndex] else null
            if (result == null) {
                for (index in current.size..currentIndex) current.add(TOCReference(createSectionTitle(pathElements, position, index, sectionTitlePrefix, sectionNumberSeparator), null))
                result = current[currentIndex]
            }
            current = result!!.getChildren()
        }
        result!!.setResource(resource)
        return result
    }
    private fun createSectionTitle(pathElements: IntArray, pathPosition: Int, lastPosition: Int, prefix: String, separator: String): String = buildString { append(prefix); for (index in 0 until pathPosition) { if (index > 0) append(separator); append(pathElements[index] + 1) }; if (pathPosition > 0) append(separator); append(lastPosition + 1) }
    fun addTOCReference(reference: TOCReference): TOCReference { tocReferences.add(reference); return reference }
    fun getAllUniqueResources(): MutableList<Resource> { val hrefs = HashSet<String?>(); val result = ArrayList<Resource>(); fun add(references: List<TOCReference>) { references.forEach { reference -> reference.getResource()?.let { resource -> if (hrefs.add(resource.getHref())) result.add(resource) }; add(reference.getChildren()) } }; add(tocReferences); return result }
    fun size(): Int {
        fun count(references: Collection<TOCReference>): Int {
            var result = references.size
            references.forEach { result += count(it.getChildren()) }
            return result
        }
        return count(tocReferences)
    }
    fun calculateDepth(): Int { fun depth(references: List<TOCReference>, current: Int): Int { var maximum = 0; references.forEach { maximum = maxOf(maximum, depth(it.getChildren(), 1)) }; return current + maximum }; return depth(tocReferences, 0) }
    companion object { private const val serialVersionUID = -3147391239966275152L; const val DEFAULT_PATH_SEPARATOR = "/" }
}
