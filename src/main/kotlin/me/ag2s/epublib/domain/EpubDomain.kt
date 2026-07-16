package me.ag2s.epublib.domain

class EpubBook {
    val metadata = Metadata()
    private val sections = mutableListOf<Pair<String, Resource>>()
    fun addSection(title: String, resource: Resource) { sections += title to resource }
    fun getSections(): List<Pair<String, Resource>> = sections
}

class Metadata {
    fun addTitle(t: String) {}
    fun addAuthor(a: Author) {}
}

data class Author(val name: String)
class Resource(val data: ByteArray, val href: String)
