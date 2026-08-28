package gauge.finder

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor

class StepAnnotationsFinder {

    fun findStepAnnotations(project: Project, searchDirectories: List<String>): List<String> {
        val collector = TypeScriptFileCollector()
        val files = if (searchDirectories.isEmpty()) {
            // 未設定 → プロジェクト全体を自動スキャン
            collector.collectAllTypeScriptFilesInProject(project)
        } else {
            // 明示指定あり → 従来通り指定ディレクトリだけ
            searchDirectories.flatMap { directoryPath ->
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(directoryPath)
                collector.collectTypeScriptFiles(project, virtualFile)
            }
        }
        val stepAnnotations = mutableListOf<String>()
        for (file in files) {
            if (file is JSFile) {
                extractStepAnnotationsFromFile(file, stepAnnotations)
            }
        }
        return stepAnnotations.toSet().toList()
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
                                    stepAnnotations.addAll(StepPatternExtractor.extract(argument))
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}
