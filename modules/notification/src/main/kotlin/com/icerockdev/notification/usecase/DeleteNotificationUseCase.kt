package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.service.NotificationService
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class DeleteNotificationUseCase(
    private val notificationService: NotificationService
) {
    fun execute(entityId: UUID, entityType: Role, notificationId: UUID) {
        val notification = notificationService.get(notificationId) ?: return
        if (notification.entityId != entityId || notification.entityType != entityType) {
            return
        }

        return notificationService.delete(notificationId = notificationId)
    }
}
