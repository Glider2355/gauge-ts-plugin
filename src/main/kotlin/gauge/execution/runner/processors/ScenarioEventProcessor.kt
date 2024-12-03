package gauge.execution.runner.processors

import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.thoughtworks.gauge.execution.runner.MessageProcessor
import com.thoughtworks.gauge.execution.runner.TestsCache
import com.thoughtworks.gauge.execution.runner.event.ExecutionError
import com.thoughtworks.gauge.execution.runner.event.ExecutionEvent
import com.thoughtworks.gauge.execution.runner.event.ExecutionResult
import java.text.ParseException

class ScenarioEventProcessor(
    processor: MessageProcessor,
    cache: TestsCache
) : GaugeEventProcessor(processor, cache) {

    companion object {
        private const val TABLE_ROW_SEPARATOR = "_"

        private fun getIdentifier(event: ExecutionEvent, value: String): String {
            val table = event.result.table
            return if (table != null) "$value$TABLE_ROW_SEPARATOR${table.rowIndex + 1}" else value
        }
    }

    @Throws(ParseException::class)
    override fun onStart(event: ExecutionEvent): Boolean {
        val parentId = cache.getId(event.parentId)
        return addTest(getIdentifier(event, event.name), parentId, getIdentifier(event, event.id), event)
    }

    @Throws(ParseException::class)
    override fun onEnd(event: ExecutionEvent): Boolean {
        val parentId = cache.getId(event.parentId)
        val id = cache.getId(getIdentifier(event, event.id))
        val name = getIdentifier(event, event.name)

        when {
            event.result.failed() -> scenarioMessage(ServiceMessageBuilder.testFailed(name), id, parentId, event.result, "Failed: ")
            event.result.skipped() -> scenarioMessage(ServiceMessageBuilder.testIgnored(name), id, parentId, event.result, "Skipped: ")
        }

        val scenarioEnd = ServiceMessageBuilder.testFinished(name)
        scenarioEnd.addAttribute("duration", event.result.time.toString())
        return processor.process(scenarioEnd, id, parentId)
    }

    override fun canProcess(event: ExecutionEvent?): Boolean {
        if (event != null) {
            return event.type.equals(ExecutionEvent.SCENARIO_START, ignoreCase = true) ||
                    event.type.equals(ExecutionEvent.SCENARIO_END, ignoreCase = true)
        }
        return false
    }

    @Throws(ParseException::class)
    private fun scenarioMessage(
        msg: ServiceMessageBuilder,
        nodeId: Int,
        parentId: Int,
        result: ExecutionResult,
        status: String
    ) {
        val errors = mutableListOf<ExecutionError>()
        val tableText = result.table?.text?.let {
            if (it.startsWith("\n")) it.substring(1) else it
        } ?: ""

        result.beforeHookFailure?.let { errors.add(it) }
        result.errors?.let { errors.addAll(it) }
        result.afterHookFailure?.let { errors.add(it) }

        msg.addAttribute("messages", "$tableText\n${errors.joinToString("\n\n") { it.format(status) }}")
        processor.process(msg, nodeId, parentId)
    }
}
