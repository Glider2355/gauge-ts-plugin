package gauge.execution

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gauge.GaugeConstants
import gauge.language.SpecFile
import gauge.language.psi.impl.SpecScenarioImpl
import gauge.util.GaugeUtil

class ScenarioExecutionProducer : LazyRunConfigurationProducer<GaugeRunConfiguration>() {

    // 実行構成を作成するためのファクトリー
    override fun getConfigurationFactory(): ConfigurationFactory {
        return GaugeRunTaskConfigurationType().configurationFactories[0]
    }

    override fun setupConfigurationFromContext(
        configuration: GaugeRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(context.dataContext)
        if (selectedFiles == null || selectedFiles.size > 1) {
            return false
        }

        val psiLocation = context.psiLocation ?: return false

        val containingFile = psiLocation.containingFile ?: return false
        val virtualFile = containingFile.virtualFile ?: return false
        if (!GaugeUtil.isSpecFile(containingFile)) {
            return false
        }

        try {
            val name = virtualFile.canonicalPath
            val scenarioIdentifier = getScenarioIdentifier(context, containingFile)
            if (scenarioIdentifier == NO_SCENARIOS || scenarioIdentifier == NON_SCENARIO_CONTEXT) {
                return false
            } else {
                val scenarioName = getScenarioName(context)
                if (scenarioName != null) {
                    configuration.name = scenarioName
                }
                configuration.specs = "$name${GaugeConstants.SPEC_SCENARIO_DELIMITER}$scenarioIdentifier"
                return true
            }
        } catch (ex: Exception) {
            LOG.debug(ex)
            return false
        }
    }

    override fun isConfigurationFromContext(
        configuration: GaugeRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        if (configuration.type !is GaugeRunTaskConfigurationType) return false

        val location = context.location ?: return false
        val virtualFile = location.virtualFile ?: return false
        val psiLocation = context.psiLocation ?: return false
        val specs = configuration.specs
        val identifier = getScenarioIdentifier(context, psiLocation.containingFile)
        return specs == "$virtualFile.path${GaugeConstants.SPEC_SCENARIO_DELIMITER}$identifier"
    }

    private fun getScenarioName(context: ConfigurationContext): String? {
        val selectedElement = context.psiLocation ?: return null
        var scenarioName: String = if (selectedElement is SpecScenarioImpl) {
            selectedElement.text
        } else {
            val text = getScenarioHeading(selectedElement).trim()
            if (text == "*") {
                selectedElement.parent.parent.node.firstChildNode.text
            } else {
                text
            }
        }
        if (scenarioName.startsWith("##")) {
            scenarioName = scenarioName.replaceFirst("##", "")
        }
        scenarioName = scenarioName.trim()
        return if (scenarioName.contains("\n")) {
            scenarioName.substring(0, scenarioName.indexOf("\n"))
        } else {
            scenarioName
        }
    }

    private fun getScenarioIdentifier(context: ConfigurationContext, file: PsiFile): Int {
        var count = NO_SCENARIOS
        val selectedElement = context.psiLocation ?: return NON_SCENARIO_CONTEXT
        val scenarioHeading = if (selectedElement !is SpecScenarioImpl) {
            getScenarioHeading(selectedElement)
        } else {
            selectedElement.text
        }
        if (scenarioHeading.isEmpty()) {
            return if (getNumberOfScenarios(file) == 0) NO_SCENARIOS else NON_SCENARIO_CONTEXT
        }
        for (psiElement in file.children) {
            if (psiElement is SpecScenarioImpl) {
                count++
                if (psiElement.node.firstChildNode.text == scenarioHeading) {
                    return StringUtil.offsetToLineNumber(
                        psiElement.containingFile.text,
                        psiElement.textOffset
                    ) + 1
                }
            }
        }
        return if (count == NO_SCENARIOS) NO_SCENARIOS else NON_SCENARIO_CONTEXT
    }

    private fun getNumberOfScenarios(file: PsiFile): Int {
        return file.children.count { it is SpecScenarioImpl }
    }

    private fun getScenarioHeading(selectedElement: PsiElement?): String {
        if (selectedElement == null) return ""
        return when (selectedElement) {
            is SpecScenarioImpl -> selectedElement.node.firstChildNode.text
            is SpecFile -> ""
            else -> getScenarioHeading(selectedElement.parent)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(ScenarioExecutionProducer::class.java)
        private const val NO_SCENARIOS = -1
        private const val NON_SCENARIO_CONTEXT = -2
    }
}
