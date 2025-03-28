package gauge.finder

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import gauge.finder.vfs.FileSystemRepositoryImpl

class Finder {
    private val fileSystemRepository = FileSystemRepositoryImpl()
    private val typeScriptFileCollector = TypeScriptFileCollector()

    fun findStepAnnotations(project: Project, searchDirectories: MutableList<String>): List<String> {
        val stepAnnotationsFinder = StepAnnotationsFinder()
        return stepAnnotationsFinder.findStepAnnotations(project, searchDirectories)
    }

    fun findStepAnnotations(project: Project, searchDirectories: MutableList<String>, stepText: String): PsiElement? {
        val stepAnnotationsFinder = StepFunctionFinder()
        return stepAnnotationsFinder.findStepFunction(project, searchDirectories, stepText)
    }
}