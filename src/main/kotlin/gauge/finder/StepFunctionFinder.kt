package gauge.finder

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor

class StepFunctionFinder {

    private val fileCollector = TypeScriptFileCollector()

    fun findStepFunction(project: Project, searchDirectories: MutableList<String>, stepText: String): PsiElement? {
        for (directoryPath in searchDirectories) {
            // TypeScriptFileCollectorを使ってディレクトリからファイルを収集
            val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://$directoryPath")
            val files = fileCollector.collectTypeScriptFiles(project, virtualFile)
            for (file in files) {
                val function = findFunctionFromFile(file, stepText)
                if (function != null) {
                    return function
                }
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
                                    val stepAnnotationText = StepTextProcessor.fixStepText(argument.text)
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
        })
        return foundFunction
    }
}
