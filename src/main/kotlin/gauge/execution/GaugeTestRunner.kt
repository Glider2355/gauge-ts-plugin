package gauge.execution

import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionResult
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunnerSettings

class GaugeTestRunner : ProgramRunner<RunnerSettings> {

    // ランナーの一意の識別子
    override fun getRunnerId(): String = "GaugeApplicationRunner"

    // 実行可能なRunProfileかどうかを判定
    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        return profile is GaugeRunConfiguration
    }

    // Gaugeを実行する
    @Throws(ExecutionException::class)
    override fun execute(environment: ExecutionEnvironment) {
        // すべてのドキュメントを保存
        FileDocumentManager.getInstance().saveAllDocuments()
        // 実行状態を取得
        val state = environment.state ?: throw ExecutionException("Cannot find RunProfileState")
        // プロセスの開始
        val executionResult: ExecutionResult = state.execute(environment.executor, this)
            ?: throw ExecutionException("Execution failed")
        // プロセスの開始を通知
        executionResult.processHandler?.startNotify()
        // 実行結果を表示
        val descriptor = RunContentDescriptor(
            executionResult.executionConsole,
            executionResult.processHandler,
            executionResult.executionConsole.component,
            environment.runProfile.name
        )

        // 既存のツールウィンドウの再利用を設定
        descriptor.isReuseToolWindowActivation = environment.contentToReuse != null
        // 実行結果を表示
        showRunContent(environment.project, environment.executor, descriptor)
    }

    private fun showRunContent(project: Project, executor: Executor, descriptor: RunContentDescriptor) {
        val runContentManager = RunContentManager.getInstance(project)
        runContentManager.showRunContent(executor, descriptor)
    }
}
