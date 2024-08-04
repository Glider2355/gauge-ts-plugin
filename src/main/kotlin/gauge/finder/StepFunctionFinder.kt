package gauge.finder

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor

class StepFunctionFinder {

    private val fileCollector = TypeScriptFileCollector()

    fun findStepFunction(project: Project, searchDirectories: MutableList<String>, stepText: String): PsiElement? {
        for (directoryPath in searchDirectories) {
            // TypeScriptFileCollectorを使ってディレクトリからファイルを収集
            val files = fileCollector.collectTypeScriptFiles(project, directoryPath)
            for (file in files) {
                val function = findFunctionInFile(file, stepText)
                if (function != null) {
                    return function
                }
            }
        }
        return null
    }

    private fun findFunctionInFile(file: PsiFile, stepText: String): PsiElement? {
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
                                    val stepAnnotationText = fixStepText(argument.text)
                                    if (isStepMatch(stepAnnotationText, stepText)) {
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

    private fun fixStepText(text: String): String {
        val noComma = text.split(",")[0]
        val noQuotes = noComma.replace("\"", "").replace("'", "")
        val cleaned = noQuotes.replace("[", "").replace("]", "").replace("\n", "")
        return cleaned.trimStart()
    }

    private fun isStepMatch(stepAnnotationText: String, stepText: String): Boolean {
        val stepAnnotationTextMatch = stepAnnotationText.replace("<[^>]+>".toRegex(), "").trimEnd() // <text> の部分を削除
        val stepTextMatch = stepText.replace("\"[^\"]*\"".toRegex(), "").trimEnd() // "text" の部分を削除

        return stepAnnotationTextMatch == stepTextMatch
    }

}
