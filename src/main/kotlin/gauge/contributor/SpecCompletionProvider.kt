package gauge.contributor

import gauge.setting.PluginSettings
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.ProcessingContext
import gauge.finder.StepAnnotationsFinder

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
        val stepFinder = StepAnnotationsFinder()
        val stepAnnotations = stepFinder.findStepAnnotations(project, searchDirectories)

        // サジェストするために取得したアノテーションの引数を追加
        for (annotation in stepAnnotations) {
            result.addElement(LookupElementBuilder.create("* $annotation"))
        }
    }
}
