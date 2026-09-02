package gauge.gotoDeclaration

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.psi.PsiElement
import com.intellij.openapi.components.service
import gauge.setting.PluginSettings
import gauge.language.token.SpecTokenTypes
import gauge.finder.ConceptIndex
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

        // 1. まず .cpt の concept 見出しにマッチするか確認 (spec -> cpt ジャンプ)
        val normalized = ConceptIndex.normalize(stepText)
        if (normalized.isNotEmpty()) {
            ConceptIndex.findConceptDefinition(project, normalized)?.let { concept ->
                return arrayOf(concept.headingElement)
            }
        }

        // 2. TypeScript の @Step 実装にジャンプ
        val settings = project.service<PluginSettings>()
        val searchDirectories = settings.validDirectories
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
