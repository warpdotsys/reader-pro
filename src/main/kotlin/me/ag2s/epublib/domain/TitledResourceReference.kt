package me.ag2s.epublib.domain

import java.io.Serializable
import me.ag2s.epublib.util.StringUtil

open class TitledResourceReference(resource: Resource?, private var title: String?, private var fragmentId: String? = null) : ResourceReference(resource), Serializable {
    @Deprecated("Deprecated in original API") constructor(resource: Resource?) : this(resource, null)
    fun getFragmentId(): String? = fragmentId
    fun setFragmentId(fragmentId: String?) { this.fragmentId = fragmentId }
    fun getTitle(): String? = title
    fun setTitle(title: String?) { this.title = title }
    fun getCompleteHref(): String? = if (StringUtil.isBlank(fragmentId)) resource!!.getHref() else resource!!.getHref() + '#' + fragmentId
    override fun getResource(): Resource? { if (resource != null && title != null) resource!!.setTitle(title); return resource }
    fun setResource(resource: Resource?, fragmentId: String?) { super.setResource(resource); this.fragmentId = fragmentId }
    override fun setResource(resource: Resource?) { setResource(resource, null) }
    companion object { private const val serialVersionUID = 3918155020095190080L }
}
