package gauge.execution

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.thoughtworks.gauge.util.SocketUtils

class GaugeDebugInfo private constructor(
    val shouldDebug: Boolean,
    val port: String
) {
    val host: String = "localhost"

    fun getPortInt(): Int = port.toInt()

    companion object {
        fun getInstance(commandLine: GeneralCommandLine, env: ExecutionEnvironment): GaugeDebugInfo {
            return if (isDebugExecution(env)) {
                val port = debugPort()
                commandLine.environment["GAUGE_DEBUG_OPTS"] = port
                GaugeDebugInfo(true, port)
            } else {
                GaugeDebugInfo(false, "")
            }
        }

        fun isDebugExecution(env: ExecutionEnvironment): Boolean {
            return DefaultDebugExecutor.EXECUTOR_ID == env.executor.id
        }

        private fun debugPort(): String {
            return SocketUtils.findFreePortForApi().toString()
        }
    }
}
