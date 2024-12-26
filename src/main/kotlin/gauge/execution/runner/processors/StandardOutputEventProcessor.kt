package gauge.execution.runner.processors

import gauge.execution.runner.MessageProcessor
import gauge.execution.runner.TestsCache
import gauge.execution.runner.event.ExecutionEvent

class StandardOutputEventProcessor(
    processor: MessageProcessor,
    cache: TestsCache
) : GaugeEventProcessor(processor, cache) {

    override fun onStart(event: ExecutionEvent): Boolean {
        // 特定の処理が必要ない場合は true を返す
        return true
    }

    override fun onEnd(event: ExecutionEvent): Boolean {
        if (event.message == null) {
            return true
        }
        var msg = event.message
        // メッセージが改行で終わっていない場合、改行を追加
        if (!msg.endsWith("\n")) {
            msg += "\n"
        }
        // メッセージを処理
        processor.process(msg)
        // 行区切りを追加
        processor.processLineBreak()
        return true
    }

    override fun canProcess(event: ExecutionEvent?): Boolean {
        if (event != null) {
            return event.type.equals(ExecutionEvent.STANDARD_OUTPUT, ignoreCase = true)
        }
        return false
    }
}
