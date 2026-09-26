package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.service.NotificationService
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class SetNotificationAsReadUseCase(
    private val notificationService: NotificationService
) {
    fun execute(entityId: UUID, entityType: Role, notificationId: UUID) {
        return notificationService.setIsReadByUser(
            entityId = entityId,
            entityType = entityType,
            notificationId = notificationId
        )
    }
}
