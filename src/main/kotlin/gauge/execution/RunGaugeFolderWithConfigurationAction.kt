package gauge.execution

import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile

class RunGaugeFolderWithConfigurationAction : AnAction("Run Gauge Specs") {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val selectedFolder: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (!selectedFolder.isDirectory) {
            Messages.showErrorDialog(project, "Please select a folder.", "Error")
            return
        }

        val folderPath = selectedFolder.path

        // RunConfiguration を作成
        val runManager = RunManager.getInstance(project)
        val configurationType = GaugeRunTaskConfigurationType()
        val configurationFactory = configurationType.configurationFactories[0]

        val settings = runManager.createConfiguration("Run Specs in ${selectedFolder.name}", configurationFactory)
        val configuration = settings.configuration as GaugeRunConfiguration

        // フォルダのパスをspecsに設定
        configuration.setSpecsArrayToExecute(listOf(folderPath))
        runManager.addConfiguration(settings)

        // 実行環境を作成
        val executor = DefaultRunExecutor.getRunExecutorInstance()
        val environment = ExecutionEnvironmentBuilder.create(executor, settings).build()

        // 実行
        environment.runner.execute(environment)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = true // 常に表示
    }
}
