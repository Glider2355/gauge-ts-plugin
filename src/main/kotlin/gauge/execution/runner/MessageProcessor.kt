package gauge.execution.runner

import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import java.text.ParseException

interface MessageProcessor {
    @Throws(ParseException::class)
    fun process(msg: ServiceMessageBuilder, nodeId: Int?, parentId: Int?): Boolean

    fun process(text: String)

    fun processLineBreak(): Boolean
}
