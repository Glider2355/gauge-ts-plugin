package gauge.finder

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import gauge.finder.util.StepTextProcessor

class StepAnnotationsFinder {

    fun findStepAnnotations(project: Project, searchDirectories: MutableList<String>): List<String> {
        val stepAnnotations = searchDirectories.flatMap { findStepAnnotationsByDirectoryPath(project, it) }.toSet()
        return stepAnnotations.toList()
    }

    private fun findStepAnnotationsByDirectoryPath(project: Project, directoryPath: String): List<String> {
        val stepAnnotations = mutableListOf<String>()

        // ディレクトリ内のTypeScriptファイルを取得
        val collector = TypeScriptFileCollector()
        val files = collector.collectTypeScriptFiles(project, directoryPath)

        // 各ファイルを解析して@Stepアノテーションを抽出
        for (file in files) {
            if (file is JSFile) {
                extractStepAnnotationsFromFile(file, stepAnnotations)
            }
        }
        return stepAnnotations
    }

    private fun extractStepAnnotationsFromFile(file: JSFile, stepAnnotations: MutableList<String>) {
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                if (element is JSFunction) {
                    val attributeList = element.attributeList
                    if (attributeList is JSAttributeList) {
                        attributeList.decorators.forEach { decorator ->
                            if (decorator is ES6Decorator && decorator.decoratorName == "Step") {
                                // デコレーターの引数を取得してリストに追加
                                val callExpression = decorator.expression as? JSCallExpression
                                callExpression?.arguments?.forEach { argument ->
                                    stepAnnotations.add(StepTextProcessor.fixStepText(argument.text))
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}
