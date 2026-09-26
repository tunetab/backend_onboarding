package com.icerockdev.notification.repository

import com.icerockdev.auth.acl.Role
import com.icerockdev.notification.model.NotificationSettings
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface NotificationSettingRepository : JpaRepository<NotificationSettings, UUID> {
    fun getByEntityIdAndEntityTypeAndType(entityId: UUID, entityType: Role, type: Int): NotificationSettings?
    fun getAllByEntityIdAndEntityType(entityId: UUID, entityType: Role): List<NotificationSettings>
}
