package gauge.execution.runner.processors

import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import gauge.execution.runner.MessageProcessor
import gauge.execution.runner.TestsCache
import gauge.execution.runner.event.ExecutionEvent
import java.text.ParseException

/**
 * おそらく、こんな形式
 * {
 *   "type": "specStart",
 *   "id": "example.spec",
 *   "name": "Example Specification",
 *   "filename": "example.spec",
 *   "line": 1
 * }
 *
 * {
 *   "type": "specEnd",
 *   "id": "example.spec",
 *   "name": "Example Specification",
 *   "filename": "example.spec",
 *   "line": 1,
 *   "result": {
 *     "status": "passed",
 *     "time": 1234,
 *     "out": "",
 *     "errors": [],
 *     "beforeHookFailure": null,
 *     "afterHookFailure": null,
 *     "table": null
 *   }
 * }
 */

class SpecEventProcessor(
    processor: MessageProcessor,
    cache: TestsCache
) : GaugeEventProcessor(processor, cache) {

    companion object {
        private const val BEFORE_SPEC = "Before Specification"
        private const val AFTER_SPEC = "After Specification"
    }

    // 開始イベント(SPEC_START)でテストツリーに追加
    @Throws(ParseException::class)
    override fun onStart(event: ExecutionEvent): Boolean {
        if (event.name == null || event.id == null) {
            throw ParseException("Spec event is missing required fields", 0)
        }
        cache.setId(event.id)
        val msg = ServiceMessageBuilder.testSuiteStarted(event.name)
        addLocation(event, msg)
        return processor.process(msg, cache.getId(event.id), SuiteEventProcessor.SUITE_ID)
    }

    // 開始イベント(SPEC_START)でノード終了
    @Throws(ParseException::class)
    override fun onEnd(event: ExecutionEvent): Boolean {
        if (event.name == null || event.id == null || event.result == null) {
            throw ParseException("Spec event is missing required fields", 0)
        }
        addHooks(event, BEFORE_SPEC, AFTER_SPEC, event.id, cache.getId(event.id)!!)
        val msg = ServiceMessageBuilder.testSuiteFinished(event.name)
        msg.addAttribute("duration", event.result.time.toString())
        return processor.process(msg, cache.getId(event.id), SuiteEventProcessor.SUITE_ID)
    }

    override fun canProcess(event: ExecutionEvent?): Boolean {
        if (event != null) {
            return event.type.equals(ExecutionEvent.SPEC_START, ignoreCase = true) ||
                    event.type.equals(ExecutionEvent.SPEC_END, ignoreCase = true)
        }
        return false
    }
}
