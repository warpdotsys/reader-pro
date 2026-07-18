package me.ag2s.epublib.domain

import java.io.Serializable

class Guide : Serializable {
    private var references: MutableList<GuideReference> = ArrayList()
    private var coverPageIndex = COVERPAGE_NOT_FOUND
    fun getReferences(): MutableList<GuideReference> = references
    fun setReferences(references: MutableList<GuideReference>) { this.references = references; uncheckCoverPage() }
    private fun uncheckCoverPage() { coverPageIndex = COVERPAGE_UNINITIALIZED }
    fun getCoverReference(): GuideReference? { checkCoverPage(); return if (coverPageIndex >= 0) references[coverPageIndex] else null }
    fun setCoverReference(reference: GuideReference): Int { if (coverPageIndex >= 0) references[coverPageIndex] = reference else { references.add(0, reference); coverPageIndex = 0 }; return coverPageIndex }
    private fun checkCoverPage() { if (coverPageIndex == COVERPAGE_UNINITIALIZED) initCoverPage() }
    private fun initCoverPage() { coverPageIndex = references.indexOfFirst { it.getType()!!.equals("cover") } }
    fun getCoverPage(): Resource? = getCoverReference()?.getResource()
    fun setCoverPage(coverPage: Resource?) { setCoverReference(GuideReference(coverPage, "cover", "cover")) }
    fun addReference(reference: GuideReference): ResourceReference { references.add(reference); uncheckCoverPage(); return reference }
    fun getGuideReferencesByType(referenceTypeName: String): MutableList<GuideReference> = references.filterTo(ArrayList()) { referenceTypeName.equals(it.getType(), true) }
    companion object { private const val serialVersionUID = -6256645339915751189L; const val DEFAULT_COVER_TITLE = "cover"; private const val COVERPAGE_NOT_FOUND = -1; private const val COVERPAGE_UNINITIALIZED = -2 }
}
