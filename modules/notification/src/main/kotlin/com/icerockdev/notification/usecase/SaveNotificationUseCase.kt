package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.dto.NotificationDto
import com.icerockdev.notification.model.Notification
import com.icerockdev.notification.service.NotificationService
import java.util.UUID
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
internal class SaveNotificationUseCase(
    private val notificationService: NotificationService
) {
    fun execute(
        entityId: UUID,
        entityType: Role,
        message: String,
        type: Int? = null
    ): UUID {
        val notificationDto = NotificationDto(
            entityId = entityId,
            entityType = entityType,
            message = message,
            type = type,
            isRead = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return notificationService.insert(Notification(notificationDto))
    }
}
