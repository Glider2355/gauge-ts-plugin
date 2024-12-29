package gauge.execution

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import gauge.GaugeConstants
import gauge.setting.PluginSettings
import javax.swing.JComponent
import javax.swing.JPanel

class GaugeRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : LocatableConfigurationBase<GaugeRunConfiguration>(project, factory, name), RunProfileWithCompileBeforeLaunchOption {

    // 実行するspecのパス
    var specs: String? = null
    // 実行時の環境設定
    private var environment: String? = null
    // 実行するテストのタグ
    private var tags: String? = null
    // 実行するテーブルの行の範囲
    private var rowsRange: String? = null

    // 設定の編集UI(空実装)
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        // return GaugeExecutionConfigurationSettingsEditor()
        return object : SettingsEditor<RunConfiguration>() {
            override fun resetEditorFrom(s: RunConfiguration) {}
            override fun applyEditorTo(s: RunConfiguration) {}
            override fun createEditor(): JComponent {
                return JPanel()
            }
        }
    }

    // 実行時の状態
    override fun getState(executor: Executor, env: ExecutionEnvironment): RunProfileState {
        val commandLine = GaugeCommandLine.getInstance(project)
        val settings = project.service<PluginSettings>()
        val parallelNode = settings.parallelNode
        val commandLineBuilder = CommandLineBuilder()
        val buildCommandLine = commandLineBuilder.buildCommandLine(
            commandLine,
            specs,
            environment,
            tags,
            parallelNode,
            rowsRange
        )
        return GaugeCommandLineState(buildCommandLine, project, env, this)
    }

    override fun getModules(): Array<Module> {
        return ModuleManager.getInstance(project).modules
    }

    private fun setSpecsToExecute(specsToExecute: String) {
        this.specs = specsToExecute
    }

    fun setSpecsArrayToExecute(specsArrayToExecute: List<String>) {
        val joinedSpecs = specsArrayToExecute.joinToString(separator = GaugeConstants.SPEC_FILE_DELIMITER)
        setSpecsToExecute(joinedSpecs)
    }
}
