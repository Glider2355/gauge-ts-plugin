package com.github.glider2355.gaugetsplugin.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor

class SpecCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project: Project = parameters.editor.project ?: return

        // TypeScriptファイルを解析し、@Stepアノテーションの引数を取得
        val stepAnnotations = findStepAnnotations(project)

        // サジェストするために取得したアノテーションの引数を追加
        for (annotation in stepAnnotations) {
            result.addElement(LookupElementBuilder.create(annotation))
        }
    }

    private fun findStepAnnotations(project: Project): List<String> {
        val stepAnnotations = mutableListOf<String>()

        // TypeScriptファイルを検索
        val tsFiles = FilenameIndex.getAllFilesByExt(project, "ts", GlobalSearchScope.projectScope(project))

        for (file in tsFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(file) ?: continue

            // TypeScriptファイル内の関数を解析
            psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
                override fun visitElement(element: PsiElement) {
                    super.visitElement(element)
                    if (element.text.contains("@Step")) {
                        val annotationText = extractAnnotationText(element.text)
                        if (annotationText.isNotEmpty()) {
                            stepAnnotations.add(annotationText)
                        }
                    }
                }
            })
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
