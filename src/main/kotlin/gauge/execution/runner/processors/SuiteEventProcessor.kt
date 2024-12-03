package gauge.execution.runner.processors

import com.thoughtworks.gauge.execution.runner.MessageProcessor
import com.thoughtworks.gauge.execution.runner.TestsCache
import com.thoughtworks.gauge.execution.runner.event.ExecutionEvent
import java.text.ParseException

class SuiteEventProcessor(
    processor: MessageProcessor,
    cache: TestsCache
) : GaugeEventProcessor(processor, cache) {

    companion object {
        private const val BEFORE_SUITE = "Before Suite"
        private const val AFTER_SUITE = "After Suite"
        const val SUITE_ID = 0
    }

    override fun onStart(event: ExecutionEvent): Boolean {
        return processor.processLineBreak()
    }

    @Throws(ParseException::class)
    override fun onEnd(event: ExecutionEvent): Boolean {
        return addHooks(event, BEFORE_SUITE, AFTER_SUITE, "", SUITE_ID)
    }

    override fun canProcess(event: ExecutionEvent?): Boolean {
        if (event != null) {
            return event.type.equals(ExecutionEvent.SUITE_START, ignoreCase = true) ||
                    event.type.equals(ExecutionEvent.SUITE_END, ignoreCase = true)
        }
        return false
    }
}
