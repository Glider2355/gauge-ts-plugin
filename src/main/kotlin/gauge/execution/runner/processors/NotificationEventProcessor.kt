package gauge.execution.runner.processors

import com.intellij.notification.Notification
import gauge.NotificationGroups
import gauge.execution.runner.MessageProcessor
import gauge.execution.runner.TestsCache
import gauge.execution.runner.event.ExecutionEvent

class NotificationEventProcessor(processor: MessageProcessor?, cache: TestsCache?) :
    GaugeEventProcessor(processor!!, cache!!) {
    override fun onStart(event: ExecutionEvent): Boolean {
        return true
    }

    override fun onEnd(event: ExecutionEvent): Boolean {
        val title = event.notification?.title
        val message = event.notification?.message
        if (title == null || message == null) {
            return true
        }
        Notification(NotificationGroups.GAUGE_GROUP, title, message, event.notification.getType())
            .notify(null)
        return true
    }

    override fun canProcess(event: ExecutionEvent?): Boolean {
        if (event != null) {
            return event.type.equals(ExecutionEvent.NOTIFICATION, ignoreCase = true)
        }
        return false
    }
}
