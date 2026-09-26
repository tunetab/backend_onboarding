package com.icerockdev.notification.dto

import com.icerockdev.auth.acl.Role
import java.util.UUID
import java.time.LocalDateTime

internal data class NotificationDto(
    val id: UUID = UUID.randomUUID(),
    val entityId: UUID,
    val entityType: Role,
    val message: String,
    val type: Int?,
    val isRead: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
