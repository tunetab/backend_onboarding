package com.icerockdev.notification.service

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.model.Notification
import com.icerockdev.notification.repository.NotificationRepository
import java.util.UUID
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
internal class NotificationService(
    private val notificationRepository: NotificationRepository
) {
    fun insert(notification: Notification): UUID {
        return notificationRepository.save(notification).id
    }

    fun setAllIsReadByUser(entityId: UUID, entityType: Role) {
        return notificationRepository.setAllIsReadByEntityIdAndEntityType(
            entityId = entityId,
            entityType = entityType
        )
    }

    fun setAllIsReadByUserAndType(entityId: UUID, entityType: Role, type: Int) {
        return notificationRepository.setAllIsReadByEntityIdAndEntityTypeAndType(
            entityId = entityId,
            entityType = entityType,
            type = type
        )
    }

    fun setIsReadByUser(entityId: UUID, entityType: Role, notificationId: UUID) {
        return notificationRepository.setIsReadByIdAndEntityIdAndEntityType(
            entityId = entityId,
            entityType = entityType,
            id = notificationId
        )
    }

    fun delete(notificationId: UUID) {
        return notificationRepository.deleteById(notificationId)
    }

    fun list(entityId: UUID, entityType: Role, pagination: Pageable): List<Notification> {
        return notificationRepository.findAllByEntityIdAndEntityType(entityId, entityType, pagination)
    }

    fun get(id: UUID): Notification? {
        return notificationRepository.findByIdOrNull(id)
    }
}
