package gauge.execution

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.testframework.actions.AbstractRerunFailedTestsAction
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil
import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.thoughtworks.gauge.core.GaugeVersion
import gauge.GaugeConstants
import com.thoughtworks.gauge.settings.GaugeSettingsService
import gauge.execution.runner.GaugeConsoleProperties
import com.thoughtworks.gauge.util.GaugeUtil

class GaugeCommandLineState(
    private val commandLine: GeneralCommandLine,
    private val project: Project,
    private val env: ExecutionEnvironment,
    private val config: GaugeRunConfiguration
) : CommandLineState(env) {

    // 指定されたコマンドライン設定に基づいてプロセスを開始
    override fun startProcess(): ProcessHandler {
        return GaugeRunProcessHandler.runCommandLine(
            commandLine,
            GaugeDebugInfo.getInstance(commandLine, env),
            project
        )
    }

    // テストの実行結果を返す
    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        // プロジェクトのクラスパスを追加
        addProjectClasspath()

        // Gauge のバージョンと設定を確認し、IntelliJ のテストランナーを使用するか判断
        if (GaugeVersion.isGreaterOrEqual(GaugeRunConfiguration.TEST_RUNNER_SUPPORT_VERSION, false) &&
            GaugeSettingsService.getSettings().useIntelliJTestRunner()
        ) {
            // プロセスハンドラを開始
            val handler = startProcess()
            // コンソールプロパティを設定
            val properties = GaugeConsoleProperties(config, "Gauge", executor, handler)
            // コンソールビューを作成し、プロセスハンドラに接続
            val console: ConsoleView = SMTestRunnerConnectionUtil.createAndAttachConsole("Gauge", handler, properties)
            // 実行結果を作成し、コンソールとプロセスハンドラを関連付け
            val result = DefaultExecutionResult(console, handler, *createActions(console, handler))
            // "RerunFailedTests" アクションが存在する場合、そのアクションを設定
            ActionManager.getInstance().getAction("RerunFailedTests")?.let {
                val action: AbstractRerunFailedTestsAction = properties.createRerunFailedTestsAction(console)
                action.setModelProvider { (console as SMTRunnerConsoleView).resultsViewer }
                result.setRestartActions(action)
            }
            return result
        }
        // 上記条件を満たさない場合、スーパークラスの execute メソッドを呼び出す
        return super.execute(executor, runner)
    }

    // プロジェクトのクラスパスを追加
    private fun addProjectClasspath() {
        config.selectedModule?.let { module ->
            val cp = GaugeUtil.classpathForModule(module)
            LOG.info("Setting `${GaugeConstants.GAUGE_CUSTOM_CLASSPATH}` to `$cp`")
            commandLine.environment[GaugeConstants.GAUGE_CUSTOM_CLASSPATH] = cp
        }
    }

    companion object {
        private val LOG = Logger.getInstance(GaugeCommandLineState::class.java)
    }
}
