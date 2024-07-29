package gauge.contributor

import gauge.setting.PluginSettings
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.lang.javascript.psi.JSFunction
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.util.ProcessingContext

class SpecCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project: Project = parameters.editor.project ?: return

        // ユーザーがチェックしたディレクトリを取得
        val settings = project.service<PluginSettings>()
        val searchDirectories = settings.validDirectories

        // TypeScriptファイルを解析し、@Stepアノテーションの引数を取得
        val stepAnnotations = searchDirectories.flatMap { findStepAnnotations(project, it) }.toSet()

        // サジェストするために取得したアノテーションの引数を追加
        for (annotation in stepAnnotations) {
            result.addElement(LookupElementBuilder.create("* $annotation"))
        }
    }

    // ディレクトリ内のTypeScriptファイルを解析して@Stepアノテーションの引数を取得
    private fun findStepAnnotations(project: Project, directoryPath: String): List<String> {
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

    // TypeScriptファイルから@Stepアノテーションの引数を取得
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

    // Step名を調整
    private fun fixStepText(text: String): String {
        // 「,」以降を削除
        val noComma = text.split(",")[0]
        // 「"」と「'」を削除
        val noQuotes = noComma.replace("\"", "").replace("'", "")
        // 「[」、「]」、改行を削除
        val cleaned = noQuotes.replace("[", "").replace("]", "").replace("\n", "")
        // 先頭のスペースを削除
        return cleaned.trimStart()
    }
}
