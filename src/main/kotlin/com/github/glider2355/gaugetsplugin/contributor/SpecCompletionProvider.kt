package com.github.glider2355.gaugetsplugin.contributor

import com.github.glider2355.gaugetsplugin.setting.PluginSettings
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.util.ProcessingContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

class SpecCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project: Project = parameters.editor.project ?: return

        // ユーザーが指定したディレクトリを取得
        val settings = project.service<PluginSettings>()
        val searchDirectory = settings.searchDirectory

        // TypeScriptファイルを解析し、@Stepアノテーションの引数を取得
        val stepAnnotations = findStepAnnotations(project, searchDirectory)

        // サジェストするために取得したアノテーションの引数を追加
        for (annotation in stepAnnotations) {
            result.addElement(LookupElementBuilder.create(annotation))
        }
    }

    private fun findStepAnnotations(project: Project, directoryPath: String): List<String> {
        val stepAnnotations = mutableListOf<String>()

        println(directoryPath)

        // 特定のディレクトリを検索範囲に含める
        val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://$directoryPath")
        val searchScope = GlobalSearchScope.fileScope(project, virtualFile)

        // TypeScriptファイルを検索
        val tsFiles = FilenameIndex.getAllFilesByExt(project, "ts", searchScope)
        println("Found ${tsFiles.size} TypeScript files")

        for (file in tsFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(file) ?: continue

            // TypeScriptファイルのテキストを取得して行ごとに処理
            val lines = psiFile.text.split("\n")

            for (line in lines) {
                if (line.contains("@Step")) {
                    val annotationText = extractAnnotationText(line)
                    if (annotationText.isNotEmpty()) {
                        stepAnnotations.add(annotationText)
                    }
                }
            }
        }

        return stepAnnotations
    }

    private fun extractAnnotationText(commentText: String): String {
        // @Stepアノテーションからテキストを抽出
        val regex = Regex("""@Step\("(.*?)"\)""")
        val matchResult = regex.find(commentText)
        return matchResult?.groupValues?.get(1) ?: ""
    }
}
