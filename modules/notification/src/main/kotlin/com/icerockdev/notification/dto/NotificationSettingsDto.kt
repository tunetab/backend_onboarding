package com.icerockdev.notification.dto

import com.icerockdev.auth.acl.Role
import java.util.UUID
import java.time.LocalDateTime

internal data class NotificationSettingsDto(
    val id: UUID,
    val entityId: UUID,
    val entityType: Role,
    val type: Int,
    val enabled: Boolean,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
