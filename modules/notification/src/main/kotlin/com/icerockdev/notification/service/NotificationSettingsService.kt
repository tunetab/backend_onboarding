package com.icerockdev.notification.service

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.dto.NotificationSettingsDto
import com.icerockdev.notification.model.NotificationSettings
import com.icerockdev.notification.repository.NotificationSettingRepository
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
internal class NotificationSettingsService(
    private val notificationSettingRepository: NotificationSettingRepository
) {
    fun get(id: UUID): NotificationSettingsDto? {
        val entity = notificationSettingRepository.findByIdOrNull(id)

        return entity?.let {
            NotificationSettingsDto(
                id = it.id,
                entityId = it.entityId,
                entityType = it.entityType,
                type = it.type,
                enabled = it.enabled,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }

    fun get(userId: UUID, userType: Role, type: Int): NotificationSettingsDto? {
        val entity = notificationSettingRepository.getByEntityIdAndEntityTypeAndType(
            entityId = userId,
            entityType = userType,
            type = type
        )

        return entity?.let {
            NotificationSettingsDto(
                id = it.id,
                entityId = it.entityId,
                entityType = it.entityType,
                type = it.type,
                enabled = it.enabled,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }


    fun insert(notificationSettingsDto: NotificationSettingsDto): UUID {
        return notificationSettingRepository.save(
            NotificationSettings(notificationSettingsDto)
        ).id
    }

    fun update(notificationSettingsDto: NotificationSettingsDto): NotificationSettings {
        return notificationSettingRepository.save(NotificationSettings(notificationSettingsDto))
    }

    fun getByEntityIdAndEntityTypeAndType(entityId: UUID, entityType: Role, type: Int): NotificationSettings? {
        return notificationSettingRepository.getByEntityIdAndEntityTypeAndType(entityId, entityType, type)
    }

    fun getAllByEntityIdAndEntityType(entityId: UUID, entityType: Role): List<NotificationSettings> {
        return notificationSettingRepository.getAllByEntityIdAndEntityType(entityId, entityType)
    }
}
