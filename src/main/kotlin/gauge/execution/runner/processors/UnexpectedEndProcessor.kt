package gauge.execution.runner.processors

import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.thoughtworks.gauge.execution.runner.MessageProcessor
import com.thoughtworks.gauge.execution.runner.TestsCache
import com.thoughtworks.gauge.execution.runner.event.ExecutionEvent

class UnexpectedEndProcessor(
    processor: MessageProcessor,
    cache: TestsCache
) : GaugeEventProcessor(processor, cache) {

    override fun onStart(event: ExecutionEvent): Boolean {
        return true
    }

    override fun onEnd(event: ExecutionEvent): Boolean {
        var name = "Failed"
        var msg = ServiceMessageBuilder.testFailed(name)
        if (event.result.skipped()) {
            name = "Ignored"
            msg = ServiceMessageBuilder.testIgnored(name)
        }

        processor.process(ServiceMessageBuilder.testStarted(name), 1, SuiteEventProcessor.SUITE_ID)
        msg.addAttribute("messages", " ")
        processor.process(msg, 1, 0)
        processor.process(ServiceMessageBuilder.testFinished(name), 1, SuiteEventProcessor.SUITE_ID)

        return false
    }

    override fun process(event: ExecutionEvent): Boolean {
        return onEnd(event)
    }

    override fun canProcess(event: ExecutionEvent?): Boolean {
        return cache.currentId == SuiteEventProcessor.SUITE_ID
    }
}
