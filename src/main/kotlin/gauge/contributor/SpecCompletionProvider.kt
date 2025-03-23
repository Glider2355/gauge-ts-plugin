package gauge.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.util.ProcessingContext
import gauge.finder.StepAnnotationsFinder

class SpecCompletionProvider(
    private val searchDirectoriesProvider: () -> List<String>
) : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val project: Project = parameters.editor.project ?: return
        val searchDirectories = searchDirectoriesProvider().toMutableList()

        // TypeScriptファイルを解析し、@Stepアノテーションの引数を取得
        val stepFinder = StepAnnotationsFinder()
        val stepAnnotations = stepFinder.findStepAnnotations(project, searchDirectories)

        // サジェストするために取得したアノテーションの引数を追加
        for (annotation in stepAnnotations) {
            result.addElement(LookupElementBuilder.create(annotation)
                .withPresentableText(annotation) // "* "はサジェスト表示には影響しない
                .withLookupString("* $annotation") // フィルタリング条件に "* " を含める
                .withInsertHandler { context, _ ->
                    val editor = context.editor
                    val document = context.document
                    val startOffset = context.startOffset
                    val tailOffset = context.tailOffset
                    // 挿入されたテキストを取得
                    val insertedText = document.getText(TextRange(startOffset, tailOffset))

                    // 全ての '<' と '>' を '"' に置換
                    val replacedText = insertedText.replace("<", "\"").replace(">", "\"")
                    // 挿入済みのテキストを置換後のテキストで更新する
                    document.replaceString(startOffset, tailOffset, replacedText)

                    // 置換後のテキスト内で最初の '"' を探す
                    val firstQuoteIndex = replacedText.indexOf('"')
                    if (firstQuoteIndex != -1) {
                        // 最初の '"' の次から、次の '"' を探す
                        val secondQuoteIndex = replacedText.indexOf('"', firstQuoteIndex + 1)
                        if (secondQuoteIndex != -1 && secondQuoteIndex > firstQuoteIndex) {
                            // プレースホルダー内部のみを選択するため、
                            // 最初の '"' の直後から、次の '"' の直前までの範囲を選択
                            val selectionStart = startOffset + firstQuoteIndex + 1
                            val selectionEnd = startOffset + secondQuoteIndex
                            editor.selectionModel.setSelection(selectionStart, selectionEnd)
                            // カーソル位置を選択範囲の末尾に移動（必要に応じて変更可能）
                            editor.caretModel.moveToOffset(selectionEnd)
                        }
                    }
                }
            )
        }
    }
}
