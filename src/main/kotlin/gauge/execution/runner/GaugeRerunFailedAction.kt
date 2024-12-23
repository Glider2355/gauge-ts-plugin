package gauge.execution.runner

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.testframework.actions.AbstractRerunFailedTestsAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.ui.ComponentContainer
import com.intellij.psi.search.GlobalSearchScope
import gauge.GaugeConstants
import gauge.execution.GaugeCommandLine
import gauge.execution.GaugeCommandLineState
import gauge.execution.GaugeRunConfiguration

class GaugeRerunFailedAction(componentContainer: ComponentContainer) :
    AbstractRerunFailedTestsAction(componentContainer) {

    override fun getRunProfile(environment: ExecutionEnvironment): MyRunProfile? {
        val config = myConsoleProperties.configuration
        return if (config is GaugeRunConfiguration) RerunProfile(config) else null
    }

    private class RerunProfile(private val config: GaugeRunConfiguration) : MyRunProfile(config) {

        override fun getState(executor: Executor, env: ExecutionEnvironment): RunProfileState {
            val commandLine = GaugeCommandLine.getInstance(config.selectedModule, project)
            commandLine.addParameters(GaugeConstants.RUN, GaugeConstants.FAILED)
            return GaugeCommandLineState(commandLine, project, env, config)
        }

        override fun getSearchScope(): GlobalSearchScope? {
            return null
        }

        override fun checkConfiguration() {
            // No-op
        }

        override fun getModules(): Array<Module> {
            return config.modules
        }
    }
}
