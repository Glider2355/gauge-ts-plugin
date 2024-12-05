package gauge.execution

import com.intellij.execution.Executor
import com.intellij.execution.application.ApplicationConfiguration
import com.intellij.execution.application.ApplicationConfigurationType
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.InvalidDataException
import com.intellij.openapi.util.WriteExternalException
import com.intellij.util.xmlb.XmlSerializer
import gauge.GaugeConstants
import org.jdom.Element
import javax.swing.JComponent
import javax.swing.JPanel

class GaugeRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : LocatableConfigurationBase<GaugeRunConfiguration>(project, factory, name), RunProfileWithCompileBeforeLaunchOption {

    // 実行するspecのパス
    var specs: String? = null
    var selectedModule: Module? = null
    // 実行時の環境設定
    private var environment: String? = null
    // 実行するテストのタグ
    private var tags: String? = null
    // 並列実行するかどうか
    private var execInParallel: Boolean = false
    // 並列実行時のノード数
    private var parallelNodes: String? = null
    // 実行時の追加パラメータ
    private var programParameters: ApplicationConfiguration =
        ApplicationConfiguration(name, project, ApplicationConfigurationType.getInstance())
    // 実行するテーブルの行の範囲
    private var rowsRange: String? = null
    private var moduleName: String? = null

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
        val commandLine = GaugeCommandLine.getInstance(selectedModule, project)
        addFlags(commandLine, env)
        return GaugeCommandLineState(commandLine, project, env, this)
    }

    // コマンドラインに必要なフラグやパラメータを追加
    private fun addFlags(commandLine: GeneralCommandLine, env: ExecutionEnvironment) {
        // 基本コマンドの追加 (gauge run)
        commandLine.addParameter(GaugeConstants.RUN)

        // Gaugeが0.9.2以上なら--machine-readableと--hide-suggestionを追加(versionを見てないので追加しない)
//        if (GaugeVersion.isGreaterOrEqual(TEST_RUNNER_SUPPORT_VERSION, true) &&
//            GaugeSettingsService.getSettings().useIntelliJTestRunner()
//        ) {
//            commandLine.addParameter(GaugeConstants.MACHINE_READABLE)
//            commandLine.addParameter(GaugeConstants.HIDE_SUGGESTION)
//        }

        // --simple-consoleフラグを追加
        // commandLine.addParameter(GaugeConstants.SIMPLE_CONSOLE)

        // tagsが指定されている場合、--tagsフラグとその値を追加
        if (!tags.isNullOrBlank()) {
            commandLine.addParameter(GaugeConstants.TAGS)
            commandLine.addParameter(tags!!)
        }

        // 環境が指定されている場合、--eフラグとその値を追加
        if (!environment.isNullOrBlank()) {
            commandLine.addParameters(GaugeConstants.ENV_FLAG, environment)
        }

        // テーブルの行の範囲の設定
        addTableRowsRangeFlags(commandLine)
        // 並列実行の設定
        addParallelExecFlags(commandLine, env)
        // 追加のプログラム引数
        addProgramArguments(commandLine)

        // 実行する仕様、シナリオの指定
        if (!specs.isNullOrBlank()) {
            addSpecs(commandLine, specs!!)
        }
    }

    // 実行するテーブル行の指定
    private fun addTableRowsRangeFlags(commandLine: GeneralCommandLine) {
        if (!rowsRange.isNullOrBlank()) {
            commandLine.addParameter(GaugeConstants.TABLE_ROWS)
            commandLine.addParameter(rowsRange!!)
        }
    }

    // 追加のプログラム引数を追加
    private fun addProgramArguments(commandLine: GeneralCommandLine) {
        programParameters.programParameters?.takeIf { it.isNotEmpty() }?.let {
            commandLine.addParameters(it.split(" "))
        }
        if (programParameters.envs.isNotEmpty()) {
            commandLine.withEnvironment(programParameters.envs)
        }
        programParameters.workingDirectory?.takeIf { it.isNotEmpty() }?.let {
            commandLine.setWorkDirectory(it)
        }
    }

    // 並列実行の設定
    private fun addParallelExecFlags(commandLine: GeneralCommandLine, env: ExecutionEnvironment) {
        if (parallelExec(env)) {
            commandLine.addParameter(GaugeConstants.PARALLEL)
            parallelNodes?.takeIf { it.isNotEmpty() }?.let {
                try {
                    it.toInt()
                    commandLine.addParameters(GaugeConstants.PARALLEL_NODES, it)
                } catch (e: NumberFormatException) {
                    // Ignore
                }
            }
        }
    }

    // 並列実行するかどうか
    private fun parallelExec(env: ExecutionEnvironment): Boolean {
        return execInParallel && !GaugeDebugInfo.isDebugExecution(env)
    }

    // 実行する仕様、シナリオの指定
    private fun addSpecs(commandLine: GeneralCommandLine, specsToExecute: String) {
        specsToExecute.split(GaugeConstants.SPEC_FILE_DELIMITER_REGEX).forEach { specName ->
            if (specName.isNotEmpty()) {
                commandLine.addParameter(specName.trim())
            }
        }
    }

    @Throws(InvalidDataException::class)
    override fun readExternal(element: Element) {
        super.readExternal(element)
        environment = element.getChildText("environment")
        specs = element.getChildText("specsToExecute")
        tags = element.getChildText("tags")
        parallelNodes = element.getChildText("parallelNodes")
        execInParallel = element.getChildText("execInParallel")?.toBoolean() ?: false
        programParameters.programParameters = element.getChildText("programParameters")
        programParameters.workingDirectory = element.getChildText("workingDirectory")
        moduleName = element.getChildText("moduleName")
        val envMap = mutableMapOf<String, String>()
        element.getChild("envMap")?.children?.forEach { child ->
            envMap[child.getAttributeValue("key")] = child.getAttributeValue("value")
        }
        programParameters.envs = envMap
        rowsRange = element.getChildText("rowsRange")
    }

    // xmlから設定を読み込む
    @Throws(WriteExternalException::class)
    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        try {
            XmlSerializer.serializeInto(this, element)
        } catch (e: Exception) {
            throw WriteExternalException(e.toString())
        }
    }

    private fun setSpecsToExecute(specsToExecute: String) {
        this.specs = specsToExecute
    }

    override fun getModules(): Array<Module> {
        return ModuleManager.getInstance(project).modules
    }

    fun setSpecsArrayToExecute(specsArrayToExecute: List<String>) {
        val builder = StringBuilder()
        for (specName in specsArrayToExecute) {
            builder.append(specName)
            if (specsArrayToExecute.indexOf(specName) != specsArrayToExecute.size - 1) {
                builder.append(GaugeConstants.SPEC_FILE_DELIMITER)
            }
        }
        setSpecsToExecute(builder.toString())
    }

    companion object {
        const val TEST_RUNNER_SUPPORT_VERSION = "0.1.0"
    }
}
