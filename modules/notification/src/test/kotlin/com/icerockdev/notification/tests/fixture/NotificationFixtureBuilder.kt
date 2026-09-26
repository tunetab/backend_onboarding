package com.icerockdev.notification.tests.fixture

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.abstract.INotificationType
import com.icerockdev.notification.model.Notification
import com.icerockdev.notification.repository.NotificationRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent

@TestComponent
class NotificationFixtureBuilder {

    @Autowired
    private lateinit var notificationRepository: NotificationRepository
    fun buildNotification(
        entityId: UUID,
        entityType: Role,
        message: String,
        type: INotificationType? = null,
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): UUID {
        return notificationRepository.save(
            Notification(
                id = UUID.randomUUID(),
                entityId = entityId,
                entityType = entityType,
                message = message,
                type = type?.getType(),
                isRead = false,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        ).id
    }
}
