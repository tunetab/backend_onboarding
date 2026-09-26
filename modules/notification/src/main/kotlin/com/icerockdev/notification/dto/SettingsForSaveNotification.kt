package com.icerockdev.notification.dto

import com.icerockdev.auth.acl.Role
import java.util.UUID

data class SettingsForSaveNotification(
    val entityId: UUID,
    val entityType: Role,
    val notificationType: Int? = null
)
