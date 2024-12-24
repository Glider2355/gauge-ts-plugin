package gauge.execution.runner.processors

import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import gauge.GaugeConstants
import gauge.execution.runner.MessageProcessor
import gauge.execution.runner.TestsCache
import gauge.execution.runner.event.ExecutionEvent
import java.text.ParseException

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
        if (cache.currentId == SuiteEventProcessor.SUITE_ID) processor.processLineBreak()
        cache.setId(event.id)
        if (cache.getId(event.id.split(GaugeConstants.SPEC_SCENARIO_DELIMITER)[0]) == null) {
            cache.getId(event.id)?.let { cache.setId(event.id.split(GaugeConstants.SPEC_SCENARIO_DELIMITER)[0], it) }
        }
        val msg = ServiceMessageBuilder.testSuiteStarted(event.name)
        addLocation(event, msg)
        return processor.process(msg, cache.getId(event.id), SuiteEventProcessor.SUITE_ID)
    }

    // 開始イベント(SPEC_START)でノード終了
    @Throws(ParseException::class)
    override fun onEnd(event: ExecutionEvent): Boolean {
        cache.getId(event.id)?.let { addHooks(event, BEFORE_SPEC, AFTER_SPEC, event.id, it) }
        val msg = ServiceMessageBuilder.testSuiteFinished(event.name)
        msg.addAttribute("duration", event.result?.time.toString())
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
