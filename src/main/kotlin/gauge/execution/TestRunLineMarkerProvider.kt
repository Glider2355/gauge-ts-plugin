package gauge.execution

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import gauge.language.token.SpecTokenTypes

open class TestRunLineMarkerProvider : RunLineMarkerContributor() {
    // ヘッダーにマーカをつける
    private val types = listOf(SpecTokenTypes.SPEC_HEADING, SpecTokenTypes.SCENARIO_HEADING)

    /**
     * IDE 依存の呼び出し部分を切り出し
     * テスト時にはオーバーライドで差し替える
     */
    open fun getExecutorActions(): Array<AnAction> {
        return ExecutorAction.getActions(1)
    }

    override fun getInfo(element: PsiElement): Info? {
        // ファイルの拡張子が ".spec" であるかを確認
        val containingFile = element.containingFile ?: return null
        if (containingFile.virtualFile.extension != "spec") return null

        if (element is LeafPsiElement && types.contains(element.elementType)) {
            val icon = AllIcons.RunConfigurations.TestState.Run
            val tooltipProvider = { _: PsiElement -> "Run Element" }
            // IDE 上でのガター動作に必要な ExecutorAction 呼び出し
            val actions = getExecutorActions()
            return Info(icon, actions, tooltipProvider)
        }
        return null
    }
}
