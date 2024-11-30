package gauge.execution

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.thoughtworks.gauge.helper.ModuleHelper
import com.thoughtworks.gauge.language.token.SpecTokenTypes

class TestRunLineMarkerProvider : RunLineMarkerContributor() {

    private val helper = ModuleHelper()
    private val types = listOf(SpecTokenTypes.SPEC_HEADING, SpecTokenTypes.SCENARIO_HEADING)

    override fun getInfo(element: PsiElement): Info? {
        // ファイルの拡張子が ".spec" であるかを確認
        val containingFile = element.containingFile ?: return null
        if (containingFile.virtualFile.extension != "spec") return null

        if (element is LeafPsiElement && types.contains(element.elementType)) {
            val icon = AllIcons.RunConfigurations.TestState.Run
            val tooltipProvider = { _: PsiElement -> "Run Element" }
            val actions = ExecutorAction.getActions(1)
            return Info(icon, actions, tooltipProvider)
        }
        return null
    }
}
