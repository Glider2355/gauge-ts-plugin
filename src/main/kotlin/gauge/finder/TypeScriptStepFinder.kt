package gauge.finder

import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementVisitor

class TypeScriptStepFinder {

    fun findStepAnnotations(project: Project, searchDirectories: MutableList<String>): List<String> {
        val stepAnnotations = searchDirectories.flatMap { findStepAnnotationsByDirectoryPath(project, it) }.toSet()
        return stepAnnotations.toList()
    }

    private fun findStepAnnotationsByDirectoryPath(project: Project, directoryPath: String): List<String> {
        val stepAnnotations = mutableListOf<String>()

        // ディレクトリ内のTypeScriptファイルを取得
        val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://$directoryPath")
        val files = mutableListOf<PsiFile>()
        if (virtualFile != null && virtualFile.isDirectory) {
            VfsUtil.iterateChildrenRecursively(virtualFile, null) { file ->
                if (!file.isDirectory && file.extension == "ts") {
                    PsiManager.getInstance(project).findFile(file)?.let { psiFile ->
                        files.add(psiFile)
                    }
                }
                true
            }
        }

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
                                    stepAnnotations.add(fixStepText(argument.text))
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun fixStepText(text: String): String {
        val noComma = text.split(",")[0]
        val noQuotes = noComma.replace("\"", "").replace("'", "")
        val cleaned = noQuotes.replace("[", "").replace("]", "").replace("\n", "")
        return cleaned.trimStart()
    }
}
