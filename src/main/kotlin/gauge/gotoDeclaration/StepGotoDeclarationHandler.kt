package gauge.gotoDeclaration

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.psi.PsiElement
import gauge.language.token.SpecTokenTypes
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

        // TypeScript の @Step 実装にジャンプ
        val stepFunction = StepFunctionFinder().findStepFunction(project, stepText)

        return stepFunction?.let { arrayOf(it) } ?: PsiElement.EMPTY_ARRAY
    }

    private fun cleanStepText(text: String): String {
        val noAsterisk = text.replace("*", "").trim()
        val noTable = noAsterisk.split("|")[0]
        return noTable.trim()
    }
}
