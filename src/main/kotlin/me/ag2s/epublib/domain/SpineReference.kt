package me.ag2s.epublib.domain

import java.io.Serializable

class SpineReference(resource: Resource?, private var linear: Boolean = true) : ResourceReference(resource), Serializable {
    fun isLinear(): Boolean = linear
    fun setLinear(linear: Boolean) { this.linear = linear }
    companion object { private const val serialVersionUID = -7921609197351510248L }
}
