package gauge.execution

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gauge.GaugeConstants
import org.jetbrains.annotations.NotNull
import gauge.util.GaugeUtil.isSpecFile

class SpecsExecutionProducer : LazyRunConfigurationProducer<GaugeRunConfiguration>() {

    companion object {
        const val DEFAULT_CONFIGURATION_NAME = "Specifications"
    }

    // 実行構成を作成するためのファクトリー
    @NotNull
    override fun getConfigurationFactory(): ConfigurationFactory {
        return GaugeRunTaskConfigurationType().configurationFactories[0]
    }

    // 実行構成を作成する
    override fun setupConfigurationFromContext(
        configuration: GaugeRunConfiguration,
        configurationContext: ConfigurationContext,
        ref: Ref<PsiElement>
    ): Boolean {
        // 選択されたファイルを取得
        val selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(configurationContext.dataContext)
        val module = configurationContext.module
        if (selectedFiles == null || module == null) {
            return false
        }

        // 選択されたファイルが1つの場合
        if (selectedFiles.size == 1) {
            if (selectedFiles[0].isDirectory) {
                return false
            } else if (selectedFiles[0].path == configurationContext.project.basePath) {
                configuration.name = DEFAULT_CONFIGURATION_NAME
                configuration.selectedModule = module
                return true
            }
        }

        val specsToExecute = getSpecs(selectedFiles)

        // 実行する仕様がない場合
        if (specsToExecute.isEmpty()) {
            return false
        }

        // 複数ファイルを選択している場合
        configuration.name = DEFAULT_CONFIGURATION_NAME
        configuration.selectedModule = module
        configuration.setSpecsArrayToExecute(specsToExecute)
        return true
    }

    // 実行構成がコンテキストに対応しているかどうか
    override fun isConfigurationFromContext(
        config: GaugeRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        if (config.type !is GaugeRunTaskConfigurationType) return false
        if (context.psiLocation !is PsiDirectory && context.psiLocation !is PsiFile) {
            return false
        }
        val selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(context.dataContext) ?: return false
        val specs = config.specs
        return StringUtil.join(getSpecs(selectedFiles), GaugeConstants.SPEC_FILE_DELIMITER) == specs
    }

    @NotNull
    private fun getSpecs(selectedFiles: Array<VirtualFile>): List<String> {
        val specsToExecute = mutableListOf<String>()
        for (selectedFile in selectedFiles) {
            if (isSpecFile(selectedFile)) {
                specsToExecute.add(selectedFile.path)
            } else if (selectedFile.isDirectory && shouldAddDirToExecute(selectedFile)) {
                specsToExecute.add(selectedFile.path)
            }
        }
        return specsToExecute
    }

    private fun shouldAddDirToExecute(selectedFile: VirtualFile): Boolean {
        return numberOfSpecFiles(selectedFile) != 0
    }

    private fun numberOfSpecFiles(directory: VirtualFile): Int {
        var numberOfSpecs = 0
        for (file in directory.children) {
            if (isSpecFile(file)) {
                numberOfSpecs++
            }
        }
        return numberOfSpecs
    }
}
