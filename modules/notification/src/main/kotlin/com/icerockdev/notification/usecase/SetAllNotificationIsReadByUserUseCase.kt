package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.service.NotificationService
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class SetAllNotificationIsReadByUserUseCase(
    private val notificationService: NotificationService
) {

    fun execute(entityId: UUID, entityType: Role, type: Int? = null) {
        if (type != null) {
            return notificationService.setAllIsReadByUserAndType(
                entityId = entityId,
                entityType = entityType,
                type = type
            )
        }

        return notificationService.setAllIsReadByUser(
            entityId = entityId,
            entityType = entityType,
        )
    }
}
