package com.icerockdev.notification.tests.fixture

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.abstract.INotificationType
import com.icerockdev.notification.model.NotificationSettings
import com.icerockdev.notification.repository.NotificationSettingRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent

@TestComponent
class NotificationSettingsFixtureBuilder {

    @Autowired
    private lateinit var notificationSettingRepository: NotificationSettingRepository

    fun buildNotificationSettings(
        entityId: UUID,
        entityType: Role,
        type: INotificationType,
        enabled: Boolean = true
    ): UUID {
        return notificationSettingRepository.save(
            NotificationSettings(
                entityId = entityId,
                entityType = entityType,
                type = type.getType(),
                enabled = enabled,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        ).id
    }
}
