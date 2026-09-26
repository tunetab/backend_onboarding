package com.icerockdev.notification.response

import com.icerockdev.notification.model.Notification
import java.util.UUID

internal class NotificationResponse(notification: Notification) {
    val id: UUID = notification.id
    val message: String = notification.message
    val type: Int? = notification.type
    val isRead: Boolean = notification.isRead
}
