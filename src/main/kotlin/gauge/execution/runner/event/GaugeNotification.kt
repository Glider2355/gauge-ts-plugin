package gauge.execution.runner.event

import com.intellij.notification.NotificationType
import org.jetbrains.annotations.Nls

class GaugeNotification {
    var title: @Nls String? = null
    var message: @Nls String? = null
    var type: String? = null

    fun getType(): NotificationType {
        return when (type) {
            "error" -> NotificationType.ERROR
            "warning" -> NotificationType.WARNING
            else -> NotificationType.INFORMATION
        }
    }
}