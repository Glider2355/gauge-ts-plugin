package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker

object ConceptIndex {

    fun findConceptTexts(project: Project): Set<String> {
        return CachedValuesManager.getManager(project).getCachedValue(project) {
            CachedValueProvider.Result.create(
                collect(project),
                PsiModificationTracker.MODIFICATION_COUNT
            )
        }
    }

    private fun collect(project: Project): Set<String> {
        val result = mutableSetOf<String>()
        val cptFiles = FilenameIndex.getAllFilesByExt(project, "cpt", GlobalSearchScope.projectScope(project))
        val psiManager = PsiManager.getInstance(project)
        for (vf in cptFiles) {
            val psi = psiManager.findFile(vf) ?: continue
            for (line in psi.text.lineSequence()) {
                val trimmed = line.trimStart()
                if (!trimmed.startsWith("#")) continue
                if (trimmed.startsWith("##")) continue
                val heading = trimmed.removePrefix("#").trim()
                if (heading.isNotEmpty()) {
                    result.add(normalize(heading))
                }
            }
        }
        return result
    }

    fun normalize(text: String): String = text
        .replace("\"[^\"]*\"".toRegex(), "")
        .replace("<[^>]+>".toRegex(), "")
        .trim()
}
