package gauge.execution.runner.processors

import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import gauge.execution.runner.MessageProcessor
import gauge.execution.runner.TestsCache
import gauge.execution.runner.event.ExecutionError
import gauge.execution.runner.event.ExecutionEvent
import gauge.execution.runner.event.ExecutionResult
import java.text.ParseException

/**
 * 受け取るイベントの例
 * シナリオ開始
 * {
 *   "type": "scenarioStart",
 *   "id": "specs/example.spec:13",
 *   "parentId": "specs/example.spec",
 *   "name": "Vowel counts in single word",
 *   "filename": "specs/example.spec",
 *   "line": 13,
 *   "result": {
 *     "time": 0
 *   }
 * }
 *
 * シナリオ終了
 *{
 *   "type": "scenarioEnd",
 *   "id": "specs/example.spec:13",
 *   "parentId": "specs/example.spec",
 *   "name": "Vowel counts in single word",
 *   "filename": "specs/example.spec",
 *   "line": 13,
 *   "result": {
 *     "status": "pass",
 *     "time": 1
 *   }
 * }
 * https://github.com/getgauge/gauge/issues/799
 */

class ScenarioEventProcessor(
    processor: MessageProcessor,
    cache: TestsCache
) : GaugeEventProcessor(processor, cache) {

    companion object {
        private const val TABLE_ROW_SEPARATOR = "_"

        private fun getIdentifier(event: ExecutionEvent, value: String): String {
            val table = event.result?.table
            return if (table != null) "$value$TABLE_ROW_SEPARATOR${table.rowIndex + 1}" else value
        }
    }

    @Throws(ParseException::class)
    override fun onStart(event: ExecutionEvent): Boolean {
        if (event.parentId == null || event.name == null || event.id == null) {
            throw ParseException("Scenario event is missing required fields", 0)
        }
        val parentId =cache.getId(event.parentId)
        if (parentId === null) {
            throw ParseException("Parent id is missing", 0)
        }
        return addTest(getIdentifier(event, event.name), parentId, getIdentifier(event, event.id), event)
    }

    @Throws(ParseException::class)
    override fun onEnd(event: ExecutionEvent): Boolean {
        if (event.parentId == null || event.name == null || event.id == null || event.result == null) {
            throw ParseException("Scenario event is missing required fields", 0)
        }
        val parentId = cache.getId(event.parentId)
        val id = cache.getId(getIdentifier(event, event.id))
        val name = getIdentifier(event, event.name)

        if(id === null || parentId === null) {
            throw ParseException("Parent id is missing", 0)
        }
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

        msg.addAttribute("message", "$tableText\n${errors.joinToString("\n\n") { it.format(status) }}")
        processor.process(msg, nodeId, parentId)
    }
}
