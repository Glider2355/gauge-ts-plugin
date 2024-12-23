package gauge.execution

import com.intellij.debugger.impl.GenericDebuggerRunner
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.remote.RemoteConfiguration
import com.intellij.execution.remote.RemoteConfigurationType
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

class GaugeRunProcessHandler private constructor(
    process: Process,
    commandLineString: String
) : ColoredProcessHandler(process, commandLineString) {

    companion object {
        private val LOG: Logger = Logger.getInstance(GaugeRunProcessHandler::class.java)

        @Throws(ExecutionException::class)
        fun runCommandLine(
            commandLine: GeneralCommandLine,
            debugInfo: GaugeDebugInfo,
            project: Project
        ): GaugeRunProcessHandler {
            LOG.info("Running Gauge tests with command : ${commandLine.commandLineString}")
            val gaugeRunProcess = GaugeRunProcessHandler(commandLine.createProcess(), commandLine.commandLineString)
            ProcessTerminatedListener.attach(gaugeRunProcess)

            if (debugInfo.shouldDebug) {
                launchDebugger(project, debugInfo)
            }
            return gaugeRunProcess
        }

        private fun launchDebugger(project: Project, debugInfo: GaugeDebugInfo) {
            val runnable = Runnable {
                val startTime = System.currentTimeMillis()
                val basicProgramRunner = GenericDebuggerRunner()
                val manager = RunManagerImpl(project)
                val configFactory: ConfigurationFactory = RemoteConfigurationType.getInstance().configurationFactories[0]
                val remoteConfig = RemoteConfiguration(project, configFactory).apply {
                    PORT = debugInfo.port
                    HOST = debugInfo.host
                    USE_SOCKET_TRANSPORT = true
                    SERVER_MODE = false
                }
                val configuration = RunnerAndConfigurationSettingsImpl(manager, remoteConfig, false)
                val environment = ExecutionEnvironment(DefaultDebugExecutor(), basicProgramRunner, configuration, project)

                var debuggerConnected = false
                while (!debuggerConnected && (System.currentTimeMillis() - startTime) < 25000) {
                    try {
                        Thread.sleep(5000)
                        basicProgramRunner.execute(environment)
                        debuggerConnected = true
                    } catch (e: Exception) {
                        LOG.warn("Failed to connect debugger. Retrying... : ${e.message}")
                        LOG.debug(e)
                    }
                }
            }

            ApplicationManager.getApplication().invokeAndWait(runnable, ModalityState.any())
        }
    }
}
