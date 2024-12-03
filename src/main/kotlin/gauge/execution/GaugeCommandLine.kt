package gauge.execution

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import gauge.GaugeConstants
import com.thoughtworks.gauge.exception.GaugeNotFoundException
import com.thoughtworks.gauge.settings.GaugeSettingsModel
import com.thoughtworks.gauge.util.GaugeUtil

object GaugeCommandLine {

    fun getInstance(module: Module?, project: Project): GeneralCommandLine {
        val commandLine = GeneralCommandLine()
        try {
            val settings: GaugeSettingsModel = GaugeUtil.getGaugeSettings()
            commandLine.exePath = settings.gaugePath
            val environment = commandLine.environment
            environment[GaugeConstants.GAUGE_HOME] = settings.homePath
        } catch (e: GaugeNotFoundException) {
            commandLine.exePath = GaugeConstants.GAUGE
        } finally {
            commandLine.setWorkDirectory(project.basePath)
            module?.let {
                commandLine.workDirectory = GaugeUtil.moduleDir(it)
            }
        }
        return commandLine
    }
}
