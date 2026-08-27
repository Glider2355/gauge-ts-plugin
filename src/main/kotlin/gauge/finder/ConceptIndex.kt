package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import gauge.language.token.SpecTokenTypes

data class ConceptDefinition(val file: PsiFile, val headingElement: PsiElement, val normalized: String)

object ConceptIndex {

    fun findConceptTexts(project: Project): Set<String> =
        cachedDefinitions(project).mapTo(mutableSetOf()) { it.normalized }

    fun findConceptDefinition(project: Project, normalizedStepText: String): ConceptDefinition? =
        cachedDefinitions(project).firstOrNull { it.normalized == normalizedStepText }

    private fun cachedDefinitions(project: Project): List<ConceptDefinition> {
        return CachedValuesManager.getManager(project).getCachedValue(project) {
            CachedValueProvider.Result.create(
                collect(project),
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun collect(project: Project): List<ConceptDefinition> {
        val result = mutableListOf<ConceptDefinition>()
        val cptFiles = FilenameIndex.getAllFilesByExt(project, "cpt", GlobalSearchScope.projectScope(project))
        val psiManager = PsiManager.getInstance(project)
        for (vf in cptFiles) {
            val psi = psiManager.findFile(vf) ?: continue
            val headings = findSpecHeadings(psi)
            for (heading in headings) {
                val headingText = heading.text.trimStart().removePrefix("#").trim()
                val normalized = normalize(headingText)
                if (normalized.isNotEmpty()) {
                    result.add(ConceptDefinition(psi, heading, normalized))
                }
            }
        }
        return result
    }

    private fun findSpecHeadings(file: PsiFile): List<PsiElement> {
        val headings = mutableListOf<PsiElement>()
        file.accept(object : com.intellij.psi.PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node?.elementType == SpecTokenTypes.SPEC_HEADING) {
                    headings.add(element)
                }
                super.visitElement(element)
            }
        })
        return headings
    }

    fun normalize(text: String): String = text
        .replace("\"[^\"]*\"".toRegex(), "")
        .replace("<[^>]+>".toRegex(), "")
        .trim()
}
