package gauge.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.service
import javax.swing.*

class SettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: SettingsComponent? = null

    // 設定画面のコンポーネントを返す
    override fun createComponent(): JComponent? {
        if (mySettingsComponent == null) {
            mySettingsComponent = SettingsComponent()
            SettingsListener(mySettingsComponent!!)
        }
        return mySettingsComponent?.mainPanel
    }

    // 設定が変更されたかどうかを返す
    override fun isModified(): Boolean {
        val settings = project.service<PluginSettings>()
        val currentDirectories = mySettingsComponent?.getDirectories()?.map { it.path } ?: emptyList()
        val currentGaugeBinaryPath = mySettingsComponent?.getGaugeBinaryPath() ?: ""
        val currentGaugeHomePath = mySettingsComponent?.getGaugeHomePath() ?: ""
        val currentParallelNode = mySettingsComponent?.getParallelNode() ?: 1
        val currentEnableEnv = mySettingsComponent?.getEnableEnv() ?: false
        val currentEnvValue = mySettingsComponent?.getEnvValue() ?: ""
        val currentEnableEnvVar = mySettingsComponent?.getEnableEnvVar() ?: false
        val currentEnvVarValue = mySettingsComponent?.getEnvVarValue() ?: ""

        return currentDirectories != settings.searchDirectories ||
                currentGaugeBinaryPath != settings.gaugeBinaryPath ||
                currentGaugeHomePath != settings.gaugeHomePath ||
                currentParallelNode != settings.parallelNode ||
                mySettingsComponent?.getDirectories()?.any {
                    it.path in settings.searchDirectories && it.isChecked != (it.path in settings.validDirectories)
                } == true ||
                currentEnableEnv != settings.enableEnv ||
                currentEnvValue != settings.envValue ||
                currentEnableEnvVar != settings.enableEnvVar ||
                currentEnvVarValue != settings.envVarValue
    }

    // 設定を保存
    override fun apply() {
        val settings = project.service<PluginSettings>()
        settings.searchDirectories = mySettingsComponent?.getDirectories()?.map { it.path }?.toMutableList() ?: mutableListOf()
        settings.validDirectories = mySettingsComponent?.getDirectories()?.filter { it.isChecked }?.map { it.path }?.toMutableList() ?: mutableListOf()
        settings.gaugeBinaryPath = mySettingsComponent?.getGaugeBinaryPath() ?: ""
        settings.gaugeHomePath = mySettingsComponent?.getGaugeHomePath() ?: ""
        settings.parallelNode = mySettingsComponent?.getParallelNode() ?: 1
        settings.enableEnv = mySettingsComponent?.getEnableEnv() ?: false
        settings.envValue = mySettingsComponent?.getEnvValue() ?: ""
        settings.enableEnvVar = mySettingsComponent?.getEnableEnvVar() ?: false
        settings.envVarValue = mySettingsComponent?.getEnvVarValue() ?: ""
    }

    // 設定画面のタイトル
    override fun getDisplayName(): String {
        return "Gauge TS Plugin Settings"
    }

    // リセット時にすべての設定をリセット
    override fun reset() {
        val settings = project.service<PluginSettings>()
        val directoryItems = settings.searchDirectories.map { DirectoryItem(it, it in settings.validDirectories) }
        mySettingsComponent?.setDirectories(directoryItems)
        mySettingsComponent?.setGaugeBinaryPath(settings.gaugeBinaryPath)
        mySettingsComponent?.setGaugeHomePath(settings.gaugeHomePath)
        mySettingsComponent?.setParallelNode(settings.parallelNode)
        mySettingsComponent?.setEnableEnv(settings.enableEnv)
        mySettingsComponent?.setEnvValue(settings.envValue)
        mySettingsComponent?.setEnableEnvVar(settings.enableEnvVar)
        mySettingsComponent?.setEnvVarValue(settings.envVarValue)
    }
}
