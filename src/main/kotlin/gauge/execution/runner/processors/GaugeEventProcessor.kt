//package gauge.execution.runner.processors
//
//import com.intellij.execution.testframework.sm.ServiceMessageBuilder
//import gauge.GaugeConstants
//import com.thoughtworks.gauge.execution.runner.MessageProcessor
//import com.thoughtworks.gauge.execution.runner.TestsCache
//import com.thoughtworks.gauge.execution.runner.event.ExecutionError
//import com.thoughtworks.gauge.execution.runner.event.ExecutionEvent
//
//abstract class GaugeEventProcessor(
//    val processor: MessageProcessor,
//    val cache: TestsCache
//) : EventProcessor {
//
//    companion object {
//        private const val FILE_PREFIX = "gauge://"
//    }
//
//    protected abstract fun onStart(event: ExecutionEvent): Boolean
//
//    protected abstract fun onEnd(event: ExecutionEvent): Boolean
//
//    override fun process(event: ExecutionEvent): Boolean {
//        return if (event.type.endsWith("Start")) onStart(event) else onEnd(event)
//    }
//
//    protected fun addHooks(
//        event: ExecutionEvent,
//        before: String,
//        after: String,
//        prefix: String,
//        parentId: Int
//    ): Boolean {
//        failTest(parentId, before, event.result.beforeHookFailure, "$prefix$before", event)
//        failTest(parentId, after, event.result.afterHookFailure, "$prefix$after", event)
//        return true
//    }
//
//    protected fun addTest(name: String, parentId: Int, key: String, event: ExecutionEvent): Boolean {
//        val test = ServiceMessageBuilder.testStarted(name)
//        addLocation(event, test)
//        cache.setId(key)
//        return processor.process(test, cache.getId(key), parentId)
//    }
//
//    protected fun addLocation(event: ExecutionEvent, msg: ServiceMessageBuilder) {
//        if (event.filename != null && event.line != null) {
//            msg.addAttribute("locationHint", "$FILE_PREFIX${event.filename}${GaugeConstants.SPEC_SCENARIO_DELIMITER}${event.line}")
//        }
//    }
//
//    private fun failTest(
//        parentId: Int,
//        name: String,
//        failure: ExecutionError?,
//        key: String,
//        event: ExecutionEvent
//    ) {
//        if (failure != null) {
//            addTest(name, parentId, key, event)
//            val failed = ServiceMessageBuilder.testFailed(name)
//            failed.addAttribute("messages", failure.format("Failed: "))
//            processor.process(failed, cache.getId(key), parentId)
//            processor.process(ServiceMessageBuilder.testFinished(name), cache.getId(key), parentId)
//        }
//    }
//}
