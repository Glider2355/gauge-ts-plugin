package gauge.execution.runner.event

import com.intellij.notification.NotificationType
import org.jetbrains.annotations.Nls

abstract class GaugeNotification(
    @Nls val title: String,
    @Nls val message: String,
    val type: String
){
    fun getType(): NotificationType {
        return when (type) {
            "error" -> NotificationType.ERROR
            "warning" -> NotificationType.WARNING
            else -> NotificationType.INFORMATION
        }
    }
}