package com.icerockdev.notification.usecase

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.common.Constants
import com.icerockdev.notification.service.NotificationSettingsService
import java.util.UUID
import org.springframework.stereotype.Component

@Component
internal class CheckIsEnabledByNotificationSettingsUseCase(
    private val notificationSettingsService: NotificationSettingsService
) {
    fun execute(entityId: UUID, entityType: Role, type: Int): Boolean {
        return notificationSettingsService.get(
            userId = entityId,
            userType = entityType,
            type = type
        )?.enabled ?: Constants.DEFAULT_NOTIFICATION_SETTING_ENABLED_VALUE
    }
}
