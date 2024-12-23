package gauge.execution.runner.processors

import gauge.execution.runner.event.ExecutionEvent
import java.text.ParseException

interface EventProcessor {
    @Throws(ParseException::class)
    fun canProcess(event: ExecutionEvent?): Boolean

    @Throws(ParseException::class)
    fun process(event: ExecutionEvent): Boolean
}
