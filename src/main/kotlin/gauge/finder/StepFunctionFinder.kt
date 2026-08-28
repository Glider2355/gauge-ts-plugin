package gauge.finder

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor

class StepFunctionFinder {

    private val fileCollector = TypeScriptFileCollector()

    fun findStepFunction(project: Project, searchDirectories: MutableList<String>, stepText: String): PsiElement? {
        val files = if (searchDirectories.isEmpty()) {
            // 未設定 → プロジェクト全体を自動スキャン
            fileCollector.collectAllTypeScriptFilesInProject(project)
        } else {
            // 明示指定あり → 従来通り指定ディレクトリだけ
            searchDirectories.flatMap { directoryPath ->
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(directoryPath)
                fileCollector.collectTypeScriptFiles(project, virtualFile)
            }
        }
        for (file in files) {
            val function = findFunctionFromFile(file, stepText)
            if (function != null) {
                return function
            }
        }
        return null
    }

    private fun findFunctionFromFile(file: PsiFile, stepText: String): PsiElement? {
        if (file !is JSFile) return null

        var foundFunction: PsiElement? = null
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element is JSFunction) {
                    val attributeList = element.attributeList
                    if (attributeList is JSAttributeList) {
                        attributeList.decorators.forEach { decorator ->
                            if (decorator is ES6Decorator && decorator.decoratorName == "Step") {
                                val callExpression = decorator.expression as? JSCallExpression
                                callExpression?.arguments?.forEach { argument ->
                                    for (stepAnnotationText in StepPatternExtractor.extract(argument)) {
                                        if (StepTextProcessor.isStepMatch(stepAnnotationText, stepText)) {
                                            foundFunction = element
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
        return foundFunction
    }
}
