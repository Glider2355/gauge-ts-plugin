package gauge.execution

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import gauge.GaugeConstants
import gauge.exception.GaugeNotFoundException
import gauge.setting.PluginSettings

object GaugeCommandLine {

    fun getInstance(project: Project): GeneralCommandLine {
        val commandLine = GeneralCommandLine()
        try {
            val settings = project.service<PluginSettings>()
            commandLine.exePath = settings.gaugeBinaryPath
            val environment = commandLine.environment
            environment[GaugeConstants.GAUGE_HOME] = settings.gaugeHomePath
        } catch (e: GaugeNotFoundException) {
            commandLine.exePath = GaugeConstants.GAUGE
        }
//        finally {
//            commandLine.setWorkDirectory(project.basePath)
//            module?.let {
//                commandLine.workDirectory = GaugeUtil.moduleDir(it) 廃止済み
//            }
//        }
        return commandLine
    }
}
