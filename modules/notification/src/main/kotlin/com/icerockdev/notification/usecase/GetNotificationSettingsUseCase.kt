package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.model.NotificationSettings
import com.icerockdev.notification.service.NotificationSettingsService
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class GetNotificationSettingsUseCase(
    private val notificationSettingsService: NotificationSettingsService
) {
    fun execute(entityId: UUID, entityType: Role, type: Int): NotificationSettings? {
        return notificationSettingsService.getByEntityIdAndEntityTypeAndType(entityId, entityType, type)
    }
}
