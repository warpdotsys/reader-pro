package me.ag2s.epublib.domain

import java.io.Serializable

open class ResourceReference(@JvmField protected var resource: Resource?) : Serializable {
    open fun getResource(): Resource? = resource
    open fun setResource(resource: Resource?) { this.resource = resource }
    fun getResourceId(): String? = resource?.getId()
    companion object { private const val serialVersionUID = 2596967243557743048L }
}
