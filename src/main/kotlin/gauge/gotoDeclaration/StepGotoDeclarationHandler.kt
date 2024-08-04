package gauge.gotoDeclaration

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.psi.PsiElement
import com.intellij.openapi.components.service
import gauge.setting.PluginSettings
import com.thoughtworks.gauge.language.token.SpecTokenTypes
import gauge.finder.StepFunctionFinder

class GaugeGotoDeclarationHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(
        sourceElement: PsiElement?,
        offset: Int,
        editor: com.intellij.openapi.editor.Editor?
    ): Array<PsiElement>? {
        if (sourceElement == null || sourceElement.node.elementType != SpecTokenTypes.STEP) {
            return null
        }

        val project = sourceElement.project
        val stepText = cleanStepText(sourceElement.parent.text)

        // PluginSettingsからsearchDirectoriesを取得
        val settings = project.service<PluginSettings>()
        val searchDirectories = settings.validDirectories

        // StepFunctionFinderを使用してTypeScript関数を検索
        val stepFinder = StepFunctionFinder()
        val stepFunction = stepFinder.findStepFunction(project, searchDirectories, stepText)

        return stepFunction?.let { arrayOf(it) } ?: PsiElement.EMPTY_ARRAY
    }

    private fun cleanStepText(text: String): String {
        val noAsterisk = text.replace("*", "").trim()
        val noTable = noAsterisk.split("|")[0]
        return noTable.trim()
    }
}
