package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.response.NotificationResponse
import com.icerockdev.notification.service.NotificationService
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
internal class GetNotificationListUseCase(
    private val notificationService: NotificationService
) {

    fun execute(entityId: UUID, entityType: Role, pagination: Pageable): List<NotificationResponse> =
        notificationService.list(entityId, entityType, pagination)
            .map { notification -> NotificationResponse(notification) }
}
