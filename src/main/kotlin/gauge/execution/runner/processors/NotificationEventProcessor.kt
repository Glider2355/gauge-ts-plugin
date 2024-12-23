//package gauge.execution.runner.processors
//
//import com.intellij.notification.Notification
//import com.thoughtworks.gauge.NotificationGroups
//import com.thoughtworks.gauge.execution.runner.MessageProcessor
//import com.thoughtworks.gauge.execution.runner.TestsCache
//import com.thoughtworks.gauge.execution.runner.event.ExecutionEvent
//
//class NotificationEventProcessor(processor: MessageProcessor?, cache: TestsCache?) :
//    GaugeEventProcessor(processor!!, cache!!) {
//    override fun onStart(event: ExecutionEvent): Boolean {
//        return true
//    }
//
//    override fun onEnd(event: ExecutionEvent): Boolean {
//        val title = event.notification.title
//        val message = event.notification.message
//        Notification(NotificationGroups.GAUGE_GROUP, title, message, event.notification.getType())
//            .notify(null)
//        return true
//    }
//
//    override fun canProcess(event: ExecutionEvent?): Boolean {
//        if (event != null) {
//            return event.type.equals(ExecutionEvent.NOTIFICATION, ignoreCase = true)
//        }
//        return false
//    }
//}
