package gauge.execution.runner

import com.google.gson.GsonBuilder
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.testframework.TestConsoleProperties
import com.intellij.execution.testframework.sm.ServiceMessageBuilder
import com.intellij.execution.testframework.sm.runner.GeneralTestEventsProcessor
import com.intellij.execution.testframework.sm.runner.OutputToGeneralTestEventsConverter
import com.intellij.openapi.util.Key
import gauge.execution.runner.event.ExecutionEvent
import gauge.execution.runner.event.ExecutionResult
import gauge.execution.runner.processors.*
import jetbrains.buildServer.messages.serviceMessages.ServiceMessageVisitor
import java.text.ParseException

class GaugeOutputToGeneralTestEventsProcessor(
    testFrameworkName: String,
    consoleProperties: TestConsoleProperties,
    private val handler: ProcessHandler
) : OutputToGeneralTestEventsConverter(testFrameworkName, consoleProperties), MessageProcessor {
    private var outputType: Key<*>? = null
    private var visitor: ServiceMessageVisitor? = null
    private val processors: List<EventProcessor>
    private val unexpectedEndProcessor: EventProcessor

    init {
        val cache = TestsCache()
        // テストイベントの処理を行うプロセッサを初期化
        processors = listOf(
            SuiteEventProcessor(this, cache),
            SpecEventProcessor(this, cache),
            ScenarioEventProcessor(this, cache),
            NotificationEventProcessor(this, cache),
            StandardOutputEventProcessor(this, cache),
        )
        // テスト実行中にプロセスが異常終了した場合の処理を行うプロセッサを初期化
        unexpectedEndProcessor = UnexpectedEndProcessor(this, cache)
    }

    // テストイベントの処理を行うプロセッサを登録
    override fun setProcessor(processor: GeneralTestEventsProcessor?) {
        super.setProcessor(processor)
        processor?.onRootPresentationAdded("Test Suite", null, null)
    }

    // textをイベントに変換
    @Throws(ParseException::class)
    override fun processServiceMessages(
        text: String,
        outputType: Key<*>,
        visitor: ServiceMessageVisitor
    ): Boolean {
        this.outputType = outputType
        this.visitor = visitor
        // JSONとしてパースできる場合、イベント処理を行う
        if (text.startsWith("{")) {
            // JSONをExecutionEventに変換
            val event = GsonBuilder().create().fromJson(text, ExecutionEvent::class.java)
            for (processor in processors) {
                if (processor.canProcess(event)) return processor.process(event)
            }
        }
        // テキストが "Process finished with exit code" で始まり、かつ UnexpectedEndProcessor が処理可能な場合、処理を行う
        if (text.trim().startsWith("Process finished with exit code") && unexpectedEndProcessor.canProcess(null)) {
            unexpectedEndProcessor.process(
                ExecutionEvent(
                    type = "",
                    id = null,
                    filename = null,
                    line = null,
                    parentId = null,
                    name = null,
                    message = null,
                    notification = null,
                    result = ExecutionResult(
                        status = if (SUCCESS == handler.exitCode) ExecutionEvent.SKIP else ExecutionEvent.FAIL,
                        time = null,
                        out = null,
                        errors = null,
                        beforeHookFailure = null,
                        afterHookFailure = null,
                        table = null
                    )
                )
            )
        }
        return super.processServiceMessages(text, outputType, visitor)
    }

    // メッセージをテストランナーに通知
    @Throws(ParseException::class)
    override fun process(
        msg: ServiceMessageBuilder,
        nodeId: Int?,
        parentId: Int?
    ): Boolean {
        msg.addAttribute("nodeId", nodeId.toString())
        msg.addAttribute("parentNodeId", parentId.toString())
        outputType?.let { visitor?.let { it1 -> super.processServiceMessages(msg.toString(), it, it1) } }
        return true
    }

    override fun process(text: String) {
        super.process(text, outputType)
    }

    // プロセス終了時にバッファをフラッシュ
    override fun processLineBreak(): Boolean {
        super.flushBufferOnProcessTermination(0)
        return true
    }

    companion object {
        private const val SUCCESS = 0
    }
}
